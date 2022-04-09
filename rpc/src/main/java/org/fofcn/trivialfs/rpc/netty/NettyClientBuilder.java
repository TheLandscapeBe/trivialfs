package org.fofcn.trivialfs.rpc.netty;

import org.fofcn.trivialfs.netty.config.NettyClientConfig;

public class NettyClientBuilder  {

    private final NettyClientConfig nettyClientConfig;

    public NettyClientBuilder() {
        this.nettyClientConfig = new NettyClientConfig();
    }

    public static NettyClientBuilder builder() {
        return new NettyClientBuilder();
    }

    public NettyClientConfig setClientWorkerThreads(int workerThreads) {
        nettyClientConfig.setClientWorkerThreads(workerThreads);
        return nettyClientConfig;
    }

    public NettyClientConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        nettyClientConfig.setConnectTimeoutMillis(connectTimeoutMillis);
        return nettyClientConfig;
    }


    public NettyClientConfig setChannelNotActiveInterval(int channelNotActiveInterval) {
        nettyClientConfig.setChannelNotActiveInterval(channelNotActiveInterval);
        return nettyClientConfig;
    }

    public NettyClientConfig setClientChannelMaxIdleTimeSeconds(int channelMaxIdleTimeSeconds) {
        nettyClientConfig.setClientChannelMaxIdleTimeSeconds(channelMaxIdleTimeSeconds);
        return nettyClientConfig;
    }
    public NettyClientConfig setClientSocketSndBufSize(int socketSndBufSize) {
        nettyClientConfig.setClientSocketSndBufSize(socketSndBufSize);
        return nettyClientConfig;
    }
    public NettyClientConfig setClientSocketRcvBufSize(int socketRcvBufSize) {
        nettyClientConfig.setClientSocketRcvBufSize(socketRcvBufSize);
        return nettyClientConfig;
    }
    public NettyClientConfig setQueueCapacity(int queueCapacity) {
        nettyClientConfig.setQueueCapacity(queueCapacity);
        return nettyClientConfig;
    }
    public NettyClientConfig setUseTLS(boolean useTLS) {
        nettyClientConfig.setUseTLS(useTLS);
        return nettyClientConfig;
    }
    public NettyClientConfig setTlsFile(String tlsFile) {
        nettyClientConfig.setTlsFile(tlsFile);
        return nettyClientConfig;
    }

    public NettyRpcClient buildRpcClient() {
        return new NettyRpcClient(nettyClientConfig);
    }
}
