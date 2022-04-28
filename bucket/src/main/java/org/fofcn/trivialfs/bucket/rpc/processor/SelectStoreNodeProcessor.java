package org.fofcn.trivialfs.bucket.rpc.processor;

import com.fofcn.trivialfs.netty.NettyProtos;
import org.fofcn.trivialfs.bucket.constant.ReadWriteEnum;
import org.fofcn.trivialfs.bucket.volume.StoreNode;
import org.fofcn.trivialfs.bucket.volume.VolumeManager;
import org.fofcn.trivialfs.netty.BucketProtos;
import org.fofcn.trivialfs.netty.enums.ResponseCode;
import org.fofcn.trivialfs.rpc.netty.NettyRequestProcessorAdapter;
import org.fofcn.trivialfs.rpc.netty.ProcessorResult;

import java.util.Optional;

/**
 * choose a readable or writable store node
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 16:45
 */
public class SelectStoreNodeProcessor extends NettyRequestProcessorAdapter {

    private final VolumeManager volumeManager;

    public SelectStoreNodeProcessor(VolumeManager volumeManager) {
        this.volumeManager = volumeManager;
    }

    @Override
    protected ProcessorResult processRequest0(NettyProtos.NettyRequest request) {
        BucketProtos.GetStoreNodeRequest storeNodeReq = request.getBucketRequest().getGetStoreNode();
        Optional<StoreNode> storeNode;
        if (storeNodeReq.getReadWrite() == ReadWriteEnum.READ.getCode()) {
            storeNode = volumeManager.getReadableNode(storeNodeReq.getName());
        } else {
            storeNode = volumeManager.getWritableNode(storeNodeReq.getName());
        }

        ProcessorResult result = new ProcessorResult();
        if (storeNode.isPresent()) {
            result.setCode(ResponseCode.SUCCESS.getCode());
            BucketProtos.GetStoreNodeReply storeNodeReply = BucketProtos.GetStoreNodeReply.newBuilder()
                    .setAddress(storeNode.get().getAddress())
                    .setPeerId(storeNode.get().getPeerId())
                    .build();
            BucketProtos.BucketReply bucketReply = BucketProtos.BucketReply.newBuilder()
                    .setGetStoreNode(storeNodeReply)
                    .build();
            NettyProtos.NettyReply reply = NettyProtos.NettyReply.newBuilder()
                    .setBucketReply(bucketReply)
                    .build();
            result.setReply(reply);
        } else {
            result.setCode(ResponseCode.SYSTEM_ERROR.getCode());
        }

        return result;
    }
}
