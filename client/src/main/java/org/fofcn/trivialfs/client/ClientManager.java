package org.fofcn.trivialfs.client;

import org.fofcn.trivialfs.client.config.ClientConfig;
import org.fofcn.trivialfs.client.impl.TrickyClientImpl;
import org.fofcn.trivialfs.client.rpc.RpcClient;

/**
 * 客户端管理
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/28 18:13
 */
public class ClientManager {

    private RpcClient rpcClient;

    private final ClientConfig clientConfig;

    public ClientManager(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public void init() {
        this.rpcClient = new RpcClient(clientConfig.getTcpClientConfig(), clientConfig.getStoreNodes().get(0));
    }

    public void shutdown() {
        this.rpcClient.shutdown();
    }

    public TrickyClient getClient() {
        return new TrickyClientImpl(rpcClient);
    }

}
