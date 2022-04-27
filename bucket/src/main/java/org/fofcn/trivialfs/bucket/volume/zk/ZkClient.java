package org.fofcn.trivialfs.bucket.volume.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.api.transaction.TransactionOp;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.fofcn.trivialfs.bucket.config.ZkClientConfig;
import org.fofcn.trivialfs.bucket.exception.VolumeException;
import org.fofcn.trivialfs.common.Service;
import org.fofcn.trivialfs.netty.util.ArrayUtil;
import org.fofcn.trivialfs.netty.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static org.fofcn.trivialfs.bucket.constant.BucketConstant.PATH_SEPARATOR_CHAR;

/**
 * Zookeeper client
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/21 11:35
 */
@Slf4j
public class ZkClient implements Service {

    private final CuratorFramework zkClient;

    private final ConcurrentHashMap<String, InterProcessLock> lockTable = new ConcurrentHashMap<>(2);

    private final ReentrantLock mainLock = new ReentrantLock();

    public ZkClient(final ZkClientConfig zkClientConfig) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zkClientConfig.getRetryBaseSleepTimeMs(),
                zkClientConfig.getMaxRetryTime());
        this.zkClient = CuratorFrameworkFactory.newClient(zkClientConfig.getAddresses(), retryPolicy);
    }

    public boolean createIfNotExisting(String path) throws VolumeException {
        try {
            Stat exists = zkClient.checkExists().forPath(path);
            if (exists == null) {
                String p = zkClient.create().forPath(path);
                if (StringUtil.isEmpty(p)) {
                    return false;
                }
            }

        } catch (Exception e) {
            log.error("create path error, path: <{}>", path);
            throw new VolumeException(e);
        }

        return true;
    }

    public boolean exists(String path) {
        try {
            Stat exists = zkClient.checkExists().forPath(path);
            return exists == null ? false : true;
        } catch (Exception e) {
            throw new VolumeException(e);
        }
    }

    public CuratorOp mkDirs(TransactionOp transOp, String path, byte[] data) {
        try {
            return data == null ? transOp.create().forPath(path) :
                    transOp.create().forPath(path, data);
        } catch (Exception e) {
            throw new VolumeException(e);
        }
    }

    public CuratorOp transDel(TransactionOp transOp, String path) {
        try {
            return transOp.delete().forPath(path);
        } catch (Exception e) {
            throw new VolumeException(e);
        }
    }

    public CuratorOp transUpData(TransactionOp transOp, String path, byte[] data) {
        try {
            return transOp.setData().forPath(path, data);
        } catch (Exception e) {
            throw new VolumeException(e);
        }
    }

    public boolean createIfNotExisting(String path, byte[] data) throws VolumeException {
        try {
            Stat exists = zkClient.checkExists().forPath(path);
            if (exists == null) {
                String p = zkClient.create().forPath(path, data);
                if (StringUtil.isEmpty(p)) {
                    return false;
                }
            } else {
                zkClient.setData().forPath(path, data);
            }

        } catch (Exception e) {
            log.error("create path error, path: <{}>", path);
            throw new VolumeException(e);
        }

        return true;
    }


    public Optional<List<String>> getChildren(String nodePath) {
        try {
            if (zkClient.checkExists().forPath(nodePath) == null) {
                return Optional.empty();
            }

            List<String> children = zkClient.getChildren().forPath(nodePath);
            return CollectionUtils.isEmpty(children) ? Optional.empty() : Optional.of(children);
        } catch (Exception e) {
            throw new VolumeException(e);
        }
    }

    public byte[] getNodeData(String nodePath) {
        try {
            return zkClient.getData().forPath(nodePath);
        } catch (Exception e) {
            throw new VolumeException(e);
        }
    }

    /**
     * create multiple children node with specified parent node using transaction
     * @param nodes node list
     * @return true if transaction success otherwise false
     */
    public boolean mkDirs(String... nodes) {
        if (ArrayUtil.isEmpty(nodes)) {
            return false;
        }

        try {
            // create children node.
            for (String node : nodes) {
                ZKPaths.mkdirs(zkClient.getZookeeperClient().getZooKeeper(), node);
            }
            return true;
        } catch (Exception e) {
            throw new VolumeException(e);
        }
    }

    /**
     * alloc a transaction operation object
     * @return transaction operation object
     */
    public TransactionOp createTransaction() {
        return zkClient.transactionOp();
    }

    /**
     * execute curator operations
     * @param curatorOps curator operation array
     * @return true if execute success, false otherwise
     */
    public boolean executeTrans(CuratorOp... curatorOps) {
        // execute transaction
        List<CuratorOp> curatorList = Arrays.asList(curatorOps);
        return executeTrans(curatorList);
    }

    /**
     * execute curator operations
     * @param curatorOpList curator operation list
     * @return true if execute success, false otherwise
     */
    public boolean executeTrans(List<CuratorOp> curatorOpList) {
        if (CollectionUtils.isEmpty(curatorOpList)) {
            return true;
        }
        // execute transaction
        try {
            List<CuratorTransactionResult> transResults = zkClient.transaction().forOperations(curatorOpList);
            for (CuratorTransactionResult transResult : transResults) {
                if (transResult.getError() != 0) {
                    log.error("transaction error, path: <{}>, operation type: <{}>, error code: <{}>",
                            transResult.getForPath(), transResult.getType(), transResult.getError());
                    return false;
                }
            }
        } catch (Exception e) {
            throw new VolumeException(e);
        }

        return true;
    }

    public boolean lock(String path) {
        mainLock.lock();
        try {
            InterProcessLock lock = lockTable.get(path);
            if (lock == null) {
                InterProcessLock newLock = new InterProcessMutex(zkClient, path);
                lockTable.put(path, newLock);
                lock = newLock;
            }
            lock.acquire();
            return true;
        } catch (Exception e) {
            log.error("lock failed.", e);
        } finally {
            mainLock.unlock();
        }

        return false;
    }

    public void unlock(String path) {
        mainLock.lock();
        try {
            InterProcessLock lock = lockTable.get(path);
            if (lock == null) {
                throw new VolumeException("lock not exists");
            }
            lock.release();
        } catch (Exception e) {
            throw new VolumeException(e);
        } finally {
            mainLock.unlock();
        }
    }

    @Override
    public boolean init() {
        zkClient.start();
        return true;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {
        zkClient.close();
    }

    private List<CuratorOp> mkDirs(TransactionOp transOp, String node) throws Exception {
        List<CuratorOp> curatorOpList = new ArrayList<>();
        int pos = 0;
        boolean last = true;
        do {
            pos = node.indexOf(PATH_SEPARATOR_CHAR, pos + 1);
            if (pos == -1) {
                if (last) {
                    pos = node.length();
                    last = false;
                } else {
                    break;
                }
            }

            String subPath = node.substring(0, pos);
            if (zkClient.checkExists().forPath(subPath) == null) {
                CuratorOp nodeOp = transOp.create().forPath(node);
                curatorOpList.add(nodeOp);
            }
        } while (pos < node.length());

        return curatorOpList;
    }

    public void deleteParent(String path) {
        try {
            ZKPaths.deleteChildren(zkClient.getZookeeperClient().getZooKeeper(), path, true);
        } catch (Exception e) {
            throw new VolumeException(e);
        }
    }
}
