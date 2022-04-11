package org.fofcn.trivialfs.rpc.grpc;

import com.fofcn.trivialfs.netty.NettyProtos;
import org.fofcn.trivialfs.common.exception.TrivialFsException;
import org.fofcn.trivialfs.rpc.RpcClient;

public class GrpcClient implements RpcClient {

    @Override
    public NettyProtos.NettyReply callSync(String address, int requestCode, NettyProtos.NettyRequest request) throws TrivialFsException {
        return null;
    }

    @Override
    public void callOneWay(String address, int requestCode, NettyProtos.NettyRequest request) throws TrivialFsException {

    }

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
