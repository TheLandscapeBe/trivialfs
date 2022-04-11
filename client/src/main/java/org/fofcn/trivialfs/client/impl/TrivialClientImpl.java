package org.fofcn.trivialfs.client.impl;

import com.fofcn.trivialfs.netty.FileDataProtos;
import com.fofcn.trivialfs.netty.NettyProtos;
import com.google.protobuf.ByteString;
import org.fofcn.trivialfs.client.ApiResult;
import org.fofcn.trivialfs.client.ApiResultWrapper;
import org.fofcn.trivialfs.client.TrivialClient;
import org.fofcn.trivialfs.common.exception.TrivialFsException;
import org.fofcn.trivialfs.common.network.NodeAddress;
import org.fofcn.trivialfs.common.network.RequestCode;
import org.fofcn.trivialfs.rpc.RpcClient;

import java.io.File;
import java.io.InputStream;

/**
 * 客户端Api实现
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/29 12:13
 */
public class TrivialClientImpl implements TrivialClient {

    private final RpcClient rpcClient;

    public TrivialClientImpl(final RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public ApiResult write(String bucket, byte[] content) throws TrivialFsException {
        // 通过bucket服务获取写入节点地址
        NodeAddress storePoint = getWriteEndPoint(bucket);
        // 写入store节点
        long fileKey = writeFile(storePoint, content);
        return fileKey == -1L ? ApiResultWrapper.fail("") :
                ApiResultWrapper.success(fileKey);
    }

    @Override
    public ApiResult write(String bucket, File file) {
        return null;
    }

    @Override
    public ApiResult write(String bucket, String filePath) {
        return null;
    }

    @Override
    public ApiResult write(String bucket, InputStream inputFile) {
        return null;
    }

    @Override
    public ApiResult read(String bucket, long fileId) {
        return null;
    }

    @Override
    public ApiResult delete(String bucket, long fileKey) {
        return null;
    }

    private long writeFile(NodeAddress nodeAddress, byte[] content) throws TrivialFsException {
        NettyProtos.NettyRequest request;
        FileDataProtos.FileRequest fileRequest = FileDataProtos.FileRequest.newBuilder()
                .setData(ByteString.copyFrom(content))
                .setLength(content.length)
                .build();
        request = NettyProtos.NettyRequest.newBuilder().setFileRequest(fileRequest).build();
        NettyProtos.NettyReply reply = rpcClient.callSync(nodeAddress.getAddress(), RequestCode.FILE_UPLOAD, request);
        if (reply.getFileReply().getSuccess()) {
            return reply.getFileReply().getKey();
        }

        return -1L;
    }

    public NodeAddress getWriteEndPoint(String bucket) {
        return new NodeAddress("127.0.0.1", 50000);
    }
}
