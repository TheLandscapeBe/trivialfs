package com.fofcn.trivivalfs.bucket.volumn;

import org.fofcn.trivialfs.bucket.volume.zk.ZkVolumeManager;
import org.fofcn.trivialfs.coordinate.zk.ZkClientConfig;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ZkVolumeManagerTest
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/15 17:31
 */
public class ZkVolumeManagerTest {

    private static ZkClientConfig zkClientConfig;

    @BeforeClass
    public static void beforeClass() {
        zkClientConfig = new ZkClientConfig();
    }

    @Test
    public void testInit() {
        ZkVolumeManager zkManager = new ZkVolumeManager(zkClientConfig);
        zkManager.init();
    }
}
