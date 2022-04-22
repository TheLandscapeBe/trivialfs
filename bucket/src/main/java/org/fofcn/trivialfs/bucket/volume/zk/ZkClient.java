package org.fofcn.trivialfs.bucket.volume.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.api.transaction.TransactionOp;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.fofcn.trivialfs.bucket.config.ZkClientConfig;
import org.fofcn.trivialfs.bucket.exception.VolumeException;
import org.fofcn.trivialfs.common.Service;
import org.fofcn.trivialfs.netty.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

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

    public CuratorOp transCreate(TransactionOp transOp, String path, byte[] data) {
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


    public List<String> getChildren(String nodePath) {
        try {
             return zkClient.getChildren().forPath(nodePath);
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
     * @param parent parent node name
     * @param children children nodes of parent
     */
    public boolean transCreate(String parent, String... children) {
        if (StringUtil.isEmpty(parent)) {
            throw new VolumeException("parent can not be null");
        }

        List<CuratorOp> curatorOpList = new ArrayList<>();
        try {
            TransactionOp transOp = createTransaction();
            // create parent node
            CuratorOp parentOp = transOp.create().forPath(parent);
            curatorOpList.add(parentOp);

            // create children node.
            if (children != null) {
                for (String child : children) {
                    CuratorOp childOp = transOp.create().forPath(parent + '/' + child);
                    curatorOpList.add(childOp);
                }
            }

           return executeTrans(curatorOpList);
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

}
