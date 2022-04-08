package org.fofcn.store.rpc.config;

import lombok.Data;
import org.fofcn.netty.config.NettyClientConfig;
import org.fofcn.netty.config.NettyServerConfig;
import org.fofcn.store.rpc.RpcEnum;

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
