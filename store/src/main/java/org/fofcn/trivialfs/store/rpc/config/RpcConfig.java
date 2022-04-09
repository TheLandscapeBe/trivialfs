package org.fofcn.trivialfs.store.rpc.config;

import lombok.Data;
import org.fofcn.trivialfs.netty.config.NettyClientConfig;
import org.fofcn.trivialfs.netty.config.NettyServerConfig;
import org.fofcn.trivialfs.store.rpc.RpcEnum;

/**
 * Rpc配置
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/01
 */
@Data
public class RpcConfig {

    private RpcEnum rpcFramework;

    private RpcClientConfig rpcClientConfig;

    private RpcServerConfig rpcServerConfig;

    public NettyClientConfig toNettyClientConfig() {
        return rpcClientConfig.toNettyClientConfig();
    }

    public NettyServerConfig toNettyServerConfig() {
        return rpcServerConfig.toNettyServerConfig();
    }
}
