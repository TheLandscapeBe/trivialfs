package com.fofcn.trivivalfs.bucket.volumn.zk;

import lombok.extern.slf4j.Slf4j;
import org.fofcn.trivialfs.coordinate.StoreNode;
import org.fofcn.trivialfs.bucket.volume.zk.ZkVolumeManager;
import org.fofcn.trivialfs.coordinate.zk.ZkClientConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * zookeeper volume manager test
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/27 12:25
 */
@Slf4j
@DisplayName("zkVolumeManagerTest")
@Execution(ExecutionMode.CONCURRENT)
public class ZkVolumeManagerTest {

    private static ZkVolumeManager zkVolumeManager;

    private static final String bucketName = "bucket-a";

    @BeforeAll
    public static void beforeAll() {
        log.info("-----BeforeAll------");
        ZkClientConfig zkClientConfig = new ZkClientConfig();
        zkClientConfig.setAddresses("127.0.0.1:2181");
        zkClientConfig.setConnectionTimeoutMs(30000);
        zkClientConfig.setMaxRetryTime(3);
        zkClientConfig.setRetryBaseSleepTimeMs(10000);
        zkClientConfig.setSessionTimeoutMs(60 * 1000);
        zkVolumeManager = new ZkVolumeManager(zkClientConfig);
        zkVolumeManager.init();

        zkVolumeManager.create(bucketName);
    }

    @AfterAll
    public static void afterAll() {
        log.info("-----AfterAll------");
        zkVolumeManager.delete(bucketName);
        zkVolumeManager.shutdown();
    }

    @BeforeEach
    public void beforeEach() {
        log.info("-----beforeEach------");
        zkVolumeManager.delete(bucketName);
    }

    @Test
    public void testCreate1() {
        boolean ret = zkVolumeManager.create(bucketName);
        assertTrue(ret);
    }

    @Test
    public void testGetEmptyNodeShouldEmpty2() {
        Optional<StoreNode> storeNode = zkVolumeManager.getReadableNode(bucketName);
        assertFalse(storeNode.isPresent());
    }


    @Test
    public void testAddReadableNodeNormal3() {
        StoreNode storeNode = new StoreNode();
        storeNode.setPeerId(UUID.randomUUID().toString());
        storeNode.setAddress("127.0.0.1:60000");
        boolean ret = zkVolumeManager.addReadableNode(bucketName, storeNode);
        assertTrue(ret);
    }

    @Test
    public void testGetEmptyNode4() {
        Optional<StoreNode> storeNode = zkVolumeManager.getReadableNode(bucketName);
        assertFalse(storeNode.isPresent());
    }

    @Test
    public void testAddAndGetReadableNode5() {
        StoreNode storeNode = new StoreNode();
        storeNode.setPeerId(UUID.randomUUID().toString());
        storeNode.setAddress("127.0.0.1:60000");
        boolean ret = zkVolumeManager.addReadableNode(bucketName, storeNode);
        assertTrue(ret);

        Optional<StoreNode> selected = zkVolumeManager.getReadableNode(bucketName);
        assertTrue(selected.isPresent());
        assertEquals(selected.get().getPeerId(), storeNode.getPeerId());
    }

    @Test
    public void testGetEmptyWritableNode() {
        Optional<StoreNode> storeNode = zkVolumeManager.getWritableNode(bucketName);
        assertFalse(storeNode.isPresent());
    }

    @Test
    public void testAddAndGetWritableNode() {
        StoreNode storeNode = new StoreNode();
        storeNode.setPeerId(UUID.randomUUID().toString());
        storeNode.setAddress("127.0.0.1:60000");
        boolean ret = zkVolumeManager.addWritableNode(bucketName, storeNode);
        assertTrue(ret);

        Optional<StoreNode> selected = zkVolumeManager.getWritableNode(bucketName);
        assertTrue(selected.isPresent());
        assertEquals(selected.get().getPeerId(), storeNode.getPeerId());
    }
}
