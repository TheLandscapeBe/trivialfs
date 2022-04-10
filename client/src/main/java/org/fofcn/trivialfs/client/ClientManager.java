package org.fofcn.trivialfs.client;

import org.fofcn.trivialfs.client.config.ClientConfig;
import org.fofcn.trivialfs.client.impl.TrivialClientImpl;
import org.fofcn.trivialfs.rpc.RpcClient;
import org.fofcn.trivialfs.rpc.netty.NettyRpcClient;

/**
 * 客户端管理
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/28 18:13
 */
public class ClientManager {

    private final RpcClient rpcClient;

    private final ClientConfig clientConfig;

    public ClientManager(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.rpcClient = new NettyRpcClient(clientConfig.getTcpClientConfig());
    }

    public void init() {
        rpcClient.init();
        rpcClient.start();
    }

    public void shutdown() {
        this.rpcClient.shutdown();
    }

    public TrivialClient getClient() {
        return new TrivialClientImpl(rpcClient);
    }

}
