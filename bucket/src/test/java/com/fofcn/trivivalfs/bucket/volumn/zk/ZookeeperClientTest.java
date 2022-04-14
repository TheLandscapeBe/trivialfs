package com.fofcn.trivivalfs.bucket.volumn.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

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
        CuratorFramework client = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        client.start();

        String str = client.create().forPath(znode, znode.getBytes(StandardCharsets.UTF_8));
    }
}
