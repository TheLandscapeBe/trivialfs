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

    public NettyClientConfig clientWorkerThreads(int workerThreads) {
        nettyClientConfig.setClientWorkerThreads(workerThreads);
        return nettyClientConfig;
    }

    public NettyClientConfig connectTimeoutMillis(int connectTimeoutMillis) {
        nettyClientConfig.setConnectTimeoutMillis(connectTimeoutMillis);
        return nettyClientConfig;
    }


    public NettyClientConfig channelNotActiveInterval(int channelNotActiveInterval) {
        nettyClientConfig.setChannelNotActiveInterval(channelNotActiveInterval);
        return nettyClientConfig;
    }

    public NettyClientConfig clientChannelMaxIdleTimeSeconds(int channelMaxIdleTimeSeconds) {
        nettyClientConfig.setClientChannelMaxIdleTimeSeconds(channelMaxIdleTimeSeconds);
        return nettyClientConfig;
    }
    public NettyClientConfig clientSocketSndBufSize(int socketSndBufSize) {
        nettyClientConfig.setClientSocketSndBufSize(socketSndBufSize);
        return nettyClientConfig;
    }
    public NettyClientConfig clientSocketRcvBufSize(int socketRcvBufSize) {
        nettyClientConfig.setClientSocketRcvBufSize(socketRcvBufSize);
        return nettyClientConfig;
    }
    public NettyClientConfig queueCapacity(int queueCapacity) {
        nettyClientConfig.setQueueCapacity(queueCapacity);
        return nettyClientConfig;
    }
    public NettyClientConfig useTLS(boolean useTLS) {
        nettyClientConfig.setUseTLS(useTLS);
        return nettyClientConfig;
    }
    public NettyClientConfig tlsFile(String tlsFile) {
        nettyClientConfig.setTlsFile(tlsFile);
        return nettyClientConfig;
    }

    public NettyRpcClient buildRpcClient() {
        return new NettyRpcClient(nettyClientConfig);
    }
}
