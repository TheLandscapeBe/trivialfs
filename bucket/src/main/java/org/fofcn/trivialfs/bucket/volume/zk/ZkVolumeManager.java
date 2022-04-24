package org.fofcn.trivialfs.bucket.volume.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.TransactionOp;
import org.fofcn.trivialfs.bucket.config.ZkClientConfig;
import org.fofcn.trivialfs.bucket.constant.BucketConstant;
import org.fofcn.trivialfs.bucket.exception.VolumeException;
import org.fofcn.trivialfs.bucket.volume.StoreNode;
import org.fofcn.trivialfs.bucket.volume.VolumeManager;
import org.fofcn.trivialfs.bucket.volume.lb.LoadBalance;
import org.fofcn.trivialfs.bucket.volume.lb.impl.VolumeRoundRobinLoadBalance;
import org.fofcn.trivialfs.netty.util.NetworkSerializable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Volume manager based on zookeeper.
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/14 17:44
 */
@Slf4j
public class ZkVolumeManager implements VolumeManager {

    private final ZkClient zkClient;

    private final ConcurrentHashMap<String, LoadBalance<StoreNode>> lbTable = new ConcurrentHashMap<>(16);

    private final ReentrantLock mainLock = new ReentrantLock();

    public ZkVolumeManager(final ZkClientConfig zkClientConfig) {
        this.zkClient = new ZkClient(zkClientConfig);
    }

    @Override
    public boolean init() {
        // check if root bucket name is existing
        // if exists do nothing
        // else create root bucket
        boolean root = zkClient.createIfNotExisting(BucketConstant.ROOT_PATH);
        if (root) {
            log.error("error create root path: <{}>", BucketConstant.ROOT_PATH);
            return false;
        }
        log.info("create root path: <{}>", BucketConstant.ROOT_PATH);
        return true;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {
    }

    @Override
    public boolean create(String name) {
        // create bucket with transactional operations
        try {
            String readableName = String.format(BucketConstant.STORE_CLUSTER_READABLE_FMT, name);
            String writableName = String.format(BucketConstant.STORE_CLUSTER_WRITABLE_FMT, name);
            zkClient.transCreate(name, readableName, writableName);
        } catch (VolumeException e) {
            log.error("error create bucket, name: <{}>", name);
            return false;
        }
        return true;
    }

    @Override
    public Optional<StoreNode> getReadableNode(String name) {
        return doGeNode(name, BucketConstant.STORE_CLUSTER_READABLE_FMT);
    }

    private Optional<StoreNode> doGeNode(String name, String storeClusterFmt) {
        StoreNode selected = null;
        LoadBalance<StoreNode> lb = null;
        mainLock.lock();
        try {
            if (!lbTable.contains(name)) {
                String readableName = String.format(storeClusterFmt, name);
                Optional<List<StoreNode>> nodeList = getStoreNodes(readableName);

                if (nodeList.isPresent()) {
                    lb = lbTable.putIfAbsent(name, new VolumeRoundRobinLoadBalance(nodeList.get()));
                }
            }
        } finally {
            mainLock.unlock();
        }

        selected = lb.selectOne();
        return selected == null ? Optional.empty() : Optional.of(selected);
    }

    @Override
    public Optional<StoreNode> getWritableNode(String name) {
        return doGeNode(name, BucketConstant.STORE_CLUSTER_WRITABLE_FMT);
    }

    @Override
    public Optional<StoreNode> getNodeByFileKey(String name, String fileKey) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addWritableNode(String name, StoreNode storeNode) {
        // use distribute lock
        String bucketPath = buildBucketPath(name);
        if (zkClient.lock(bucketPath)) {
            try {
                // check node if existing, if existing we do nothing and return true
                // otherwise we will create a node
                String nodePath = buildWritablePath(storeNode.getPeerId());
                String readablePath = buildReadablePath(storeNode.getPeerId());
                boolean readableExists = zkClient.exists(readablePath);
                boolean writableExists = zkClient.exists(nodePath);
                byte[] nodeData = NetworkSerializable.jsonEncode(storeNode);
                // start a transaction
                List<CuratorOp> transOpList = new ArrayList<>(2);
                TransactionOp trans = zkClient.createTransaction();

                // if writable node exists, we will update its node data;
                // otherwise we should create the node and set node data.
                if (writableExists) {
                    CuratorOp upDataOp = zkClient.transUpData(trans, readablePath, nodeData);
                    transOpList.add(upDataOp);
                } else {
                    CuratorOp writableOp = zkClient.transCreate(trans, nodePath, nodeData);
                    transOpList.add(writableOp);
                }

                // if readable node exists, we should delete the readable node;
                // otherwise we do nothing.
                if (readableExists) {
                    CuratorOp readableOp = zkClient.transDel(trans, readablePath);
                    transOpList.add(readableOp);
                }

                // commit the transaction
                return zkClient.executeTrans(transOpList);
            } finally {
                zkClient.unlock(bucketPath);
            }
        }

        return false;
    }

    @Override
    public boolean addReadableNode(StoreNode storeNode) {
        throw new NotImplementedException();
    }

    private Optional<List<StoreNode>> getStoreNodes(String path) {
        List<String> nodeList = zkClient.getChildren(path);
        if (CollectionUtils.isEmpty(nodeList)) {
            return Optional.empty();
        }

        List<StoreNode> storeNodeList = new ArrayList<>();
        for (String node : nodeList) {
            String nodePath = path + '/' + node;
            byte[] nodeData = zkClient.getNodeData(nodePath);
            if (nodeData == null) {
                log.warn("Data of the node:<{}> is empty.", nodePath);
                continue;
            }
            StoreNode storeNode = NetworkSerializable.jsonDecode(nodeData, StoreNode.class);
            storeNodeList.add(storeNode);
        }

        return CollectionUtils.isEmpty(storeNodeList) ? Optional.empty() : Optional.of(storeNodeList);
    }

    private String buildWritablePath(String peerId) {
        return String.format(BucketConstant.STORE_CLUSTER_WRITABLE_NODE_FMT, peerId);
    }

    private String buildReadablePath(String peerId) {
        return String.format(BucketConstant.STORE_CLUSTER_READABLE_FMT, peerId);
    }

    private String buildBucketPath(String name) {
        return String.format(BucketConstant.STORE_CLUSTER_FMT, name);
    }
}
