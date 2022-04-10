package org.fofcn.trivialfs.rpc.netty;

import org.fofcn.trivialfs.netty.NetworkServer;
import org.fofcn.trivialfs.netty.config.NettyServerConfig;
import org.fofcn.trivialfs.netty.interceptor.RequestInterceptor;
import org.fofcn.trivialfs.netty.netty.NettyNetworkServer;
import org.fofcn.trivialfs.netty.processor.NettyRequestProcessor;
import org.fofcn.trivialfs.netty.processor.RequestProcessor;
import org.fofcn.trivialfs.rpc.RpcServer;

/**
 * 基于Netty的RPC服务端
 */
public class NettyRpcServer implements RpcServer {

    private final NetworkServer server;

    public NettyRpcServer(final NettyServerConfig nettyServerConfig) {
        this.server = new NettyNetworkServer(nettyServerConfig);
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void shutdown() {
        server.shutdown();
    }

    @Override
    public void registerProcessor(int code, RequestProcessor processor) {
        server.registerProcessor(code, (NettyRequestProcessor) processor, null);
    }

    @Override
    public void registerInterceptor(RequestInterceptor interceptor) {
        server.registerInterceptor(interceptor);
    }
}
