package org.fofcn.trivialfs.bucket.volume.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.fofcn.trivialfs.bucket.config.BucketConfig;
import org.fofcn.trivialfs.bucket.config.ZkClientConfig;
import org.fofcn.trivialfs.bucket.constant.BucketConstant;
import org.fofcn.trivialfs.bucket.volume.StoreNode;
import org.fofcn.trivialfs.bucket.volume.VolumeManager;
import org.fofcn.trivialfs.netty.util.StringUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Volume manager based on zookeeper.
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/14 17:44
 */
@Slf4j
public class ZkVolumeManager implements VolumeManager {

    private final CuratorFramework zkClient;

    private final ZkClientConfig zkClientConfig;

    public ZkVolumeManager(final ZkClientConfig zkClientConfig) {
        this.zkClientConfig = zkClientConfig;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zkClientConfig.getRetryBaseSleepTimeMs(),
                zkClientConfig.getMaxRetryTime());
        this.zkClient = CuratorFrameworkFactory.newClient(zkClientConfig.getAddresses(), retryPolicy);
    }

    @Override
    public boolean init() {
        zkClient.start();

        // check if root bucket name is existing
        // if exists do nothing
        // else create root bucket
        Stat rootStat = null;
        try {
            rootStat = zkClient.checkExists().forPath(BucketConstant.ROOT_PATH);
            if (rootStat == null) {
                String str = zkClient.create().forPath(BucketConstant.ROOT_PATH, BucketConstant.ROOT_PATH.getBytes(StandardCharsets.UTF_8));
                if (StringUtil.isEmpty(str)) {
                    log.error("error create root path: <{}>", str);
                    return false;
                }

                log.info("create root path: <{}>", str);
                return false;
            }
        } catch (Exception e) {
            log.error("check root bucket path error", e);
        }

        return true;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {
        zkClient.close();
    }

    @Override
    public boolean create(String name) {
        return false;
    }

    @Override
    public List<StoreNode> getReadableNode(String name) {
        return null;
    }

    @Override
    public List<StoreNode> getWritableNode(String name) {
        return null;
    }

    @Override
    public StoreNode getNodeByFileKey(String name, String fileKey) {
        return null;
    }
}
