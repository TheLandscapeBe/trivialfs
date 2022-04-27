package com.fofcn.trivivalfs.bucket.volumn;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.fofcn.trivialfs.bucket.constant.BucketConstant;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * Zk客户端测试
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/14 17:21
 */
public class ZookeeperClientTest {

    @Test
    public void testZNode() throws Exception {
        String hostPort = "127.0.0.1:2181";
        String znode = "/buckets";

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        zkClient.start();
        Stat rootStat = zkClient.checkExists().forPath(BucketConstant.ROOT_PATH);
        if (rootStat == null) {
            String str = zkClient.create().forPath(znode, znode.getBytes(StandardCharsets.UTF_8));
        }

    }
}
