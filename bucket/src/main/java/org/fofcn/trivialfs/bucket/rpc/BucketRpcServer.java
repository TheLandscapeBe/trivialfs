package org.fofcn.trivialfs.bucket.rpc;

import org.fofcn.trivialfs.bucket.config.BucketConfig;
import org.fofcn.trivialfs.bucket.rpc.processor.SelectStoreNodeProcessor;
import org.fofcn.trivialfs.bucket.volume.VolumeManager;
import org.fofcn.trivialfs.common.Service;
import org.fofcn.trivialfs.common.network.RequestCode;
import org.fofcn.trivialfs.rpc.RpcServer;
import org.fofcn.trivialfs.rpc.netty.NettyRpcServer;

/**
 * bucket rpc 服务
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 15:33
 */
public class BucketRpcServer implements Service {

    private final RpcServer rpcServer;

    private final VolumeManager volumeManager;

    public BucketRpcServer(final BucketConfig bucketConfig, VolumeManager volumeManager) {
        this.rpcServer = new NettyRpcServer(bucketConfig.getServerConfig());
        this.volumeManager = volumeManager;
    }


    @Override
    public boolean init() {
        rpcServer.init();
        rpcServer.registerProcessor(RequestCode.SELECT_STORE_NODE, new SelectStoreNodeProcessor(volumeManager));
        return true;
    }

    @Override
    public void start() {
        rpcServer.start();
    }

    @Override
    public void shutdown() {
        rpcServer.shutdown();
    }
}
