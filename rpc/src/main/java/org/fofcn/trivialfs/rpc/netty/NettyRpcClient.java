package org.fofcn.trivialfs.rpc.netty;

import com.fofcn.trivialfs.netty.NettyProtos;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.fofcn.trivialfs.common.exception.TrivialFsException;
import org.fofcn.trivialfs.common.exception.TrivialFsNetworkException;
import org.fofcn.trivialfs.netty.NetworkClient;
import org.fofcn.trivialfs.netty.config.NettyClientConfig;
import org.fofcn.trivialfs.netty.exception.NetworkConnectException;
import org.fofcn.trivialfs.netty.exception.NetworkSendRequestException;
import org.fofcn.trivialfs.netty.exception.NetworkTimeoutException;
import org.fofcn.trivialfs.netty.netty.NettyNetworkClient;
import org.fofcn.trivialfs.netty.netty.NetworkCommand;
import org.fofcn.trivialfs.rpc.RpcClient;

/**
 * 基于Netty的RPC客户端
 */
@Slf4j
public class NettyRpcClient implements RpcClient {

    private final NetworkClient networkClient;

    private final long waitMillis;

    public NettyRpcClient(final NettyClientConfig nettyClientConfig) {
        this.networkClient = new NettyNetworkClient(nettyClientConfig);
        this.waitMillis = nettyClientConfig.getConnectTimeoutMillis();
    }

    /**
     * 同步调用
     * @param address 地址
     * @param requestCode 请求码
     * @param request 请求
     * @return 结果
     */
    @Override
    public NettyProtos.NettyReply callSync(String address, int requestCode, NettyProtos.NettyRequest request) throws TrivialFsException {
        NetworkCommand networkCommand = NetworkCommand.createRequestCommand(requestCode, request.toByteArray());

        NetworkCommand response = null;
        try {
            response = networkClient.sendSync(address, networkCommand, waitMillis);
            if (response.getBody() == null) {
                throw new TrivialFsNetworkException("error response body");
            }

            return NettyProtos.NettyReply.parseFrom(response.getBody());
        } catch (NetworkConnectException e) {
            throw new TrivialFsException(e);
        } catch (NetworkSendRequestException e) {
            throw new TrivialFsException(e);
        } catch (InterruptedException e) {
            throw new TrivialFsException(e);
        } catch (NetworkTimeoutException e) {
            throw new TrivialFsException(e);
        } catch (InvalidProtocolBufferException e) {
            throw new TrivialFsException(e);
        }
    }


    /**
     * one way调用
     * @param address 地址
     * @param requestCode 请求码
     * @param request 请求
     * @return 结果
     */
    @Override
    public void callOneWay(String address, int requestCode, NettyProtos.NettyRequest request) throws TrivialFsException {
        NetworkCommand networkCommand = NetworkCommand.createRequestCommand(requestCode, request.toByteArray());
        try {
            networkClient.sendOneway(address, networkCommand, waitMillis, null);
        } catch (NetworkConnectException e) {
            throw new TrivialFsException(e);
        } catch (NetworkSendRequestException e) {
            throw new TrivialFsException(e);
        } catch (InterruptedException e) {
            throw new TrivialFsException(e);
        } catch (NetworkTimeoutException e) {
            throw new TrivialFsException(e);
        }
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void start() {
        this.networkClient.start();
    }

    public void shutdown() {
        networkClient.shutdown();
    }
}
