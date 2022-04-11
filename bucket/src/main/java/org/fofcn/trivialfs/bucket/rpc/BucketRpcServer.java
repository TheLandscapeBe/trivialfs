package org.fofcn.trivialfs.bucket.rpc;

import org.fofcn.trivialfs.bucket.config.BucketConfig;
import org.fofcn.trivialfs.bucket.rpc.processor.PingProcessor;
import org.fofcn.trivialfs.bucket.rpc.processor.SelectStoreNodeProcessor;
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

    public BucketRpcServer(final BucketConfig bucketConfig) {
        this.rpcServer = new NettyRpcServer(bucketConfig.getNettyServerConfig());
    }


    @Override
    public boolean init() {
        rpcServer.init();

        rpcServer.registerProcessor(RequestCode.BUCKET_PING, new PingProcessor());
        rpcServer.registerProcessor(RequestCode.SELECT_STORE_NODE, new SelectStoreNodeProcessor());
        return true;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
