package org.fofcn.client.impl;

import com.fofcn.trivialfs.netty.FileDataProtos;
import com.fofcn.trivialfs.netty.NettyProtos;
import com.google.protobuf.ByteString;
import org.fofcn.client.ApiResult;
import org.fofcn.client.ApiResultWrapper;
import org.fofcn.client.TrickyClient;
import org.fofcn.client.rpc.RpcClient;
import org.fofcn.common.exception.TrickyFsNetworkException;
import org.fofcn.common.network.RequestCode;

import java.io.File;
import java.io.InputStream;

/**
 * 客户端Api实现
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/29 12:13
 */
public class TrickyClientImpl implements TrickyClient {

    private final RpcClient rpcClient;

    public TrickyClientImpl(final RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public ApiResult write(String bucket, byte[] content) throws TrickyFsNetworkException {
        long fileKey = writeFile(content);
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

    private long writeFile(byte[] content) throws TrickyFsNetworkException {
        NettyProtos.NettyRequest request;
        FileDataProtos.FileRequest fileRequest = FileDataProtos.FileRequest.newBuilder()
                .setData(ByteString.copyFrom(content))
                .setLength(content.length)
                .build();
        request = NettyProtos.NettyRequest.newBuilder().setFileRequest(fileRequest).build();
        NettyProtos.NettyReply reply = rpcClient.callSync(RequestCode.FILE_UPLOAD, request);
        if (reply.getFileReply().getSuccess()) {
            return reply.getFileReply().getKey();
        }

        return -1L;
    }
}
