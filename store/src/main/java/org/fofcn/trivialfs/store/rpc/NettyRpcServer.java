package org.fofcn.trivialfs.store.rpc;


import org.fofcn.trivialfs.netty.NetworkServer;
import org.fofcn.trivialfs.netty.interceptor.RequestInterceptor;
import org.fofcn.trivialfs.netty.netty.NettyNetworkServer;
import org.fofcn.trivialfs.netty.processor.NettyRequestProcessor;
import org.fofcn.trivialfs.netty.processor.RequestProcessor;
import org.fofcn.trivialfs.store.rpc.config.RpcConfig;

/**
 * RpcServer端
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/01 17:22
 */
public class NettyRpcServer implements RpcServer {

    private final NetworkServer server;

    public NettyRpcServer(final RpcConfig rpcConfig) {
        this.server = new NettyNetworkServer(rpcConfig.toNettyServerConfig());
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

    /**
     * Rpc客户端配置
     *
     * @author errorfatal89@gmail.com
     * @datetime 2022/04/02 11:14
     */
    public static class RpcClientConfig {
    }
}
