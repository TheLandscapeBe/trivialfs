package org.fofcn.trivialfs.store.bucket;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.TransactionOp;
import org.fofcn.trivialfs.common.Service;
import org.fofcn.trivialfs.coordinate.StoreNode;
import org.fofcn.trivialfs.coordinate.constant.BucketConstant;
import org.fofcn.trivialfs.coordinate.exception.CoordinateException;
import org.fofcn.trivialfs.coordinate.zk.ZkClient;
import org.fofcn.trivialfs.netty.util.NetworkSerializable;
import org.fofcn.trivialfs.store.config.BucketConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * bucket manager
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/05/06 16:02
 */
@Slf4j
public class BucketManager implements Service {

    private final BucketConfig bucketConfig;

    private final ZkClient zkClient;

    public BucketManager(BucketConfig bucketConfig) {
        this.bucketConfig = bucketConfig;
        this.zkClient = new ZkClient(bucketConfig.getZkConfig());
    }

    @Override
    public boolean init() {
        zkClient.start();
        return false;
    }

    @Override
    public void start() {
        // register self to bucket store node list
    }

    @Override
    public void shutdown() {
        zkClient.shutdown();
    }

    private boolean addWritableNode(String name, StoreNode storeNode) {
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
            } catch (CoordinateException e) {
                log.error("Add writable store node error. ", e);
            } finally {
                try {
                    zkClient.unlock(bucketPath);
                } catch (CoordinateException ignore) {
                }
            }
        }

        return false;
    }

    private boolean addReadableNode(String name, StoreNode storeNode) {
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
            } catch (CoordinateException e) {
                log.error("Add writable store node error.", e);
            } finally {
                try {
                    zkClient.unlock(bucketPath);
                } catch (CoordinateException ignore) {
                }
            }
        }

        return false;
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
