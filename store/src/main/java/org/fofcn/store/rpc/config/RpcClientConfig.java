package org.fofcn.store.rpc.config;

import lombok.Data;
import org.fofcn.netty.config.NettyClientConfig;

/**
 * Rpc客户端配置
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/02 11:24
 */
@Data
public class RpcClientConfig {

    private int clientWorkerThreads = 4;

    private int connectTimeoutMillis = 30000;

    private long channelNotActiveInterval = 1000L * 60;

    private int clientChannelMaxIdleTimeSeconds = 120;

    private int clientSocketSndBufSize = 65535;

    private int clientSocketRcvBufSize = 65535;

    private int queueCapacity = 1000;

    private boolean useTLS = false;

    private String tlsFile;

    public NettyClientConfig toNettyClientConfig() {
        NettyClientConfig clientConfig = new NettyClientConfig();
        clientConfig.setConnectTimeoutMillis(connectTimeoutMillis);
        clientConfig.setChannelNotActiveInterval(channelNotActiveInterval);
        clientConfig.setClientChannelMaxIdleTimeSeconds(clientChannelMaxIdleTimeSeconds);
        clientConfig.setClientSocketRcvBufSize(clientSocketRcvBufSize);
        clientConfig.setClientSocketSndBufSize(clientSocketSndBufSize);
        clientConfig.setClientWorkerThreads(clientWorkerThreads);
        clientConfig.setQueueCapacity(queueCapacity);
        clientConfig.setUseTLS(useTLS);
        clientConfig.setTlsFile(tlsFile);
        return clientConfig;
    }
}
