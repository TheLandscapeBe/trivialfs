package org.fofcn.trivialfs.rpc;

import org.fofcn.trivialfs.common.Service;
import org.fofcn.trivialfs.netty.interceptor.RequestInterceptor;
import org.fofcn.trivialfs.netty.processor.RequestProcessor;

/**
 * RPC服务端
 */
public interface RpcServer extends Service {

    /**
     * 注册消息处理器
     * @param code 消息码
     * @param processor 消息处理器
     */
    void registerProcessor(int code, RequestProcessor processor);

    /**
     * 注册消息拦截器
     * @param interceptor 消息拦截器
     */
    void registerInterceptor(RequestInterceptor interceptor);
}
