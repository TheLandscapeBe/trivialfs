package org.fofcn.trivialfs.rpc;

import com.fofcn.trivialfs.netty.NettyProtos;
import org.fofcn.trivialfs.common.Service;
import org.fofcn.trivialfs.common.exception.TrivialFsException;

/**
 * Rpc客户端
 */
public interface RpcClient extends Service {

    /**
     * 同步调用
     * @param address 地址
     * @param requestCode 请求码
     * @param request 请求
     * @return 结果
     */
    public NettyProtos.NettyReply callSync(String address, int requestCode, NettyProtos.NettyRequest request) throws TrivialFsException;

    /**
     * one way调用
     * @param address 地址
     * @param requestCode 请求码
     * @param request 请求
     * @return 结果
     */
    public void callOneWay(String address, int requestCode, NettyProtos.NettyRequest request) throws TrivialFsException;
}
