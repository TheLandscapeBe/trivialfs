package org.fofcn.trivialfs.rpc.grpc;

import org.fofcn.trivialfs.netty.interceptor.RequestInterceptor;
import org.fofcn.trivialfs.netty.processor.RequestProcessor;
import org.fofcn.trivialfs.rpc.RpcServer;

public class GrpcServer implements RpcServer {

    @Override
    public void registerProcessor(int code, RequestProcessor processor) {

    }

    @Override
    public void registerInterceptor(RequestInterceptor interceptor) {

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
