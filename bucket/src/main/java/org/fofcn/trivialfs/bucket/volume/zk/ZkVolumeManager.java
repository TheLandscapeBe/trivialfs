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
import org.fofcn.trivialfs.netty.util.ArrayUtil;
import org.fofcn.trivialfs.netty.util.NetworkSerializable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

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
        zkClient.init();
        // check if root bucket name is existing
        // if exists do nothing
        // else create root bucket
        boolean root = zkClient.createIfNotExisting(BucketConstant.ROOT_PATH);
        if (!root) {
            log.error("error create root path: <{}>", BucketConstant.ROOT_PATH);
            return false;
        }
        log.info("create root path: <{}>", BucketConstant.ROOT_PATH);
        return true;
    }

    @Override
    public void start() {
        zkClient.start();
    }

    @Override
    public void shutdown() {
        zkClient.shutdown();
    }

    @Override
    public boolean create(String name) {
        // create bucket with transactional operations
        try {
            String readableName = String.format(BucketConstant.STORE_CLUSTER_READABLE_FMT, name);
            String writableName = String.format(BucketConstant.STORE_CLUSTER_WRITABLE_FMT, name);
            zkClient.mkDirs(readableName, writableName);
        } catch (VolumeException e) {
            log.error("error create bucket, name: <{}>", name, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(String name) {
        String path = buildBucketPath(name);
        try {
            zkClient.deleteParent(path);
        } catch (VolumeException e) {
            log.error("delete path error.", e);
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
                    LoadBalance<StoreNode> newLb = new VolumeRoundRobinLoadBalance(nodeList.get());
                    lb = lbTable.putIfAbsent(name, newLb);
                    if (lb == null) {
                        lb = newLb;
                    }
                } else {
                    return Optional.empty();
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
                String writablePath = buildWritablePath(name, storeNode.getPeerId());
                String readablePath = buildReadablePath(name, storeNode.getPeerId());
                boolean readableExists = zkClient.exists(readablePath);
                boolean writableExists = zkClient.exists(writablePath);
                byte[] nodeData = NetworkSerializable.jsonEncode(storeNode);
                // start a transaction
                List<CuratorOp> transOpList = new ArrayList<>(2);
                TransactionOp trans = zkClient.createTransaction();

                // if writable node exists, we will update its node data;
                // otherwise we should create the node and set node data.
                if (!writableExists) {
                    zkClient.mkDirs(writablePath);
                }
                CuratorOp upDataOp = zkClient.transUpData(trans, writablePath, nodeData);
                transOpList.add(upDataOp);

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
    public boolean addReadableNode(String name, StoreNode storeNode) {
        // use distribute lock
        String bucketPath = buildBucketPath(name);
        if (zkClient.lock(bucketPath)) {
            try {
                // check node if existing, if existing we do nothing and return true
                // otherwise we will create a node
                String writablePath = buildWritablePath(name, storeNode.getPeerId());
                String readablePath = buildReadablePath(name, storeNode.getPeerId());
                boolean readableExists = zkClient.exists(readablePath);
                boolean writableExists = zkClient.exists(writablePath);
                byte[] nodeData = NetworkSerializable.jsonEncode(storeNode);
                // start a transaction
                List<CuratorOp> transOpList = new ArrayList<>(2);
                TransactionOp trans = zkClient.createTransaction();

                // if readable node exists, we will update its node data;
                // otherwise we should create the node and set node data.
                if (!readableExists) {
                    zkClient.mkDirs(readablePath);
                }

                CuratorOp upDataOp = zkClient.transUpData(trans, readablePath, nodeData);
                transOpList.add(upDataOp);

                // if readable node exists, we should delete the readable node;
                // otherwise we do nothing.
                if (writableExists) {
                    CuratorOp readableOp = zkClient.transDel(trans, writablePath);
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

    private Optional<List<StoreNode>> getStoreNodes(String path) {
        Optional<List<String>> nodeList = zkClient.getChildren(path);
        if (!nodeList.isPresent()) {
            return Optional.empty();
        }

        List<StoreNode> storeNodeList = new ArrayList<>();
        for (String node : nodeList.get()) {
            String nodePath = path + '/' + node;
            byte[] nodeData = zkClient.getNodeData(nodePath);
            if (ArrayUtil.isEmpty(nodeData)) {
                log.warn("Data of the node:<{}> is empty.", nodePath);
                continue;
            } else {
                StoreNode storeNode = NetworkSerializable.jsonDecode(nodeData, StoreNode.class);
                storeNodeList.add(storeNode);
            }
        }

        return CollectionUtils.isEmpty(storeNodeList) ? Optional.empty() : Optional.of(storeNodeList);
    }

    private String buildWritablePath(String name, String peerId) {
        return String.format(BucketConstant.STORE_CLUSTER_WRITABLE_NODE_FMT, name, peerId);
    }

    private String buildReadablePath(String name, String peerId) {
        return String.format(BucketConstant.STORE_CLUSTER_READABLE_NODE_FMT, name, peerId);
    }

    private String buildBucketPath(String name) {
        return String.format(BucketConstant.STORE_CLUSTER_FMT, name);
    }
}
