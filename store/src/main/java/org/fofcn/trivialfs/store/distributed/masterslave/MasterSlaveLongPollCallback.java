package org.fofcn.trivialfs.store.distributed.masterslave;

import com.fofcn.trivialfs.netty.ClusterProtos;
import com.fofcn.trivialfs.netty.NettyProtos;
import com.google.protobuf.ByteString;
import org.fofcn.trivialfs.netty.enums.ResponseCode;
import org.fofcn.trivialfs.netty.netty.NetworkCommand;
import org.fofcn.trivialfs.store.disk.block.FileBlock;
import org.fofcn.trivialfs.store.distributed.masterslave.longpoll.LongPollCallback;

/**
 * 主从复制长轮询客户端
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/04 22:47
 */
public class MasterSlaveLongPollCallback implements LongPollCallback<LongPollData, LongPollArgs> {

    private final int peerId;

    public MasterSlaveLongPollCallback(final int peerId) {
        this.peerId = peerId;
    }

    @Override
    public void onNewData(LongPollData lpData, LongPollArgs lpArgs) {

        // 组装消息进行同步
        ClusterProtos.ReplicateReply replicateReply;
        // 检查数据偏移是否满足我的需求
        if (lpData.getOffset() < lpArgs.getExpectedOffset()) {
            return;
        } else if (lpData.getOffset() == lpArgs.getExpectedOffset()) {
            // 组装消息进行同步
            replicateReply = ClusterProtos.ReplicateReply.newBuilder()
                    .setExists(true)
                    .setOffset(lpData.getOffset())
                    .setContent(ByteString.copyFrom(lpData.getContent()))
                    .setPeerId(peerId)
                    .setSyncLength(lpData.getSize())
                    .build();
        } else {
            FileBlock fileBlock = lpArgs.getBlockFile().read(lpArgs.getExpectedOffset());
            // 组装消息进行同步
            replicateReply = ClusterProtos.ReplicateReply.newBuilder()
                    .setExists(true)
                    .setOffset(lpArgs.getExpectedOffset())
                    .setContent(ByteString.copyFrom(fileBlock.getBody()))
                    .setPeerId(peerId)
                    .setSyncLength(fileBlock.getBody().length)
                    .build();
        }

        ClusterProtos.ClusterReply clusterReply = ClusterProtos.ClusterReply.newBuilder()
                .setReplicateReply(replicateReply).build();
        NettyProtos.NettyReply reply = NettyProtos.NettyReply.newBuilder()
                .setClusterReply(clusterReply).build();
        lpArgs.getCtx().writeAndFlush(NetworkCommand.createResponseCommand(ResponseCode.SUCCESS.getCode(),
                reply.toByteArray()));
    }

    @Override
    public void onTimeout(LongPollArgs lpArgs) {
        ClusterProtos.ReplicateReply replicateReply = ClusterProtos.ReplicateReply.newBuilder()
                .setExists(false).build();
        ClusterProtos.ClusterReply clusterReply = ClusterProtos.ClusterReply.newBuilder()
                .setReplicateReply(replicateReply).build();
        NettyProtos.NettyReply reply = NettyProtos.NettyReply.newBuilder()
                .setClusterReply(clusterReply).build();
        lpArgs.getCtx().writeAndFlush(NetworkCommand.createResponseCommand(ResponseCode.SUCCESS.getCode(),
                reply.toByteArray()));
    }
}
