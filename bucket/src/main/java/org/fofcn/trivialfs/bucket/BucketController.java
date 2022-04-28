package org.fofcn.trivialfs.bucket;

import org.fofcn.trivialfs.bucket.config.BucketConfig;
import org.fofcn.trivialfs.bucket.rpc.BucketRpcServer;
import org.fofcn.trivialfs.bucket.volume.VolumeManager;
import org.fofcn.trivialfs.bucket.volume.zk.ZkVolumeManager;
import org.fofcn.trivialfs.common.Service;

/**
 * 所有模块控制器
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 16:46
 */
public class BucketController implements Service {

    private final VolumeManager volumeManager;

    private final BucketRpcServer bucketRpcServer;

    public BucketController(BucketConfig bucketConfig) {
        this.volumeManager = new ZkVolumeManager(bucketConfig.getZkConfig());
        this.bucketRpcServer = new BucketRpcServer(bucketConfig, volumeManager);
    }

    @Override
    public boolean init() {
        bucketRpcServer.init();
        volumeManager.init();
        return true;
    }

    @Override
    public void start() {
        volumeManager.start();
        bucketRpcServer.start();
    }

    @Override
    public void shutdown() {
        volumeManager.shutdown();
        bucketRpcServer.shutdown();
    }
}
