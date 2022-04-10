package org.fofcn.trivialfs.rpc.netty;

import org.fofcn.trivialfs.netty.config.NettyServerConfig;

public class NettyServerBuilder {
    private final NettyServerConfig nettyServerConfig;

    public NettyServerBuilder() {
        this.nettyServerConfig = new NettyServerConfig();
    }

    public static NettyServerBuilder builder() {
        return new NettyServerBuilder();
    }

    public NettyRpcServer buildNettyRpcServer() {
        return new NettyRpcServer(nettyServerConfig);
    }

    public NettyServerConfig host(String host) {
        nettyServerConfig.setHost(host);
        return nettyServerConfig;
    }

    public NettyServerConfig listenPort(int listenPort) {
        nettyServerConfig.setListenPort(listenPort);
        return nettyServerConfig;
    }

    public NettyServerConfig serverWorkerThreads(int serverWorkerThreads) {
        nettyServerConfig.setServerWorkerThreads(serverWorkerThreads);
        return nettyServerConfig;
    }

    public NettyServerConfig serverSelectorThreads(int serverSelectorThreads) {
        nettyServerConfig.setServerSelectorThreads(serverSelectorThreads);
        return nettyServerConfig;
    }

    public NettyServerConfig serverChannelMaxIdleTimeSeconds(int serverChannelMaxIdleTimeSeconds) {
        nettyServerConfig.setServerChannelMaxIdleTimeSeconds(serverChannelMaxIdleTimeSeconds);
        return nettyServerConfig;
    }

    public NettyServerConfig serverSocketSndBufSize(int serverSocketSndBufSize) {
        nettyServerConfig.setServerSocketSndBufSize(serverSocketSndBufSize);
        return nettyServerConfig;
    }

    public NettyServerConfig serverSocketRcvBufSize(int serverSocketRcvBufSize) {
        nettyServerConfig.setServerSocketRcvBufSize(serverSocketRcvBufSize);
        return nettyServerConfig;
    }

    public NettyServerConfig useTLS(boolean useTLS) {
        nettyServerConfig.setUseTLS(useTLS);
        return nettyServerConfig;
    }

    public NettyServerConfig tlsFile(String tlsFile) {
        nettyServerConfig.setTlsFile(tlsFile);
        return nettyServerConfig;
    }
}
