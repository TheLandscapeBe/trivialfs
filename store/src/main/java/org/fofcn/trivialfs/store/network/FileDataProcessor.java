package org.fofcn.trivialfs.store.network;

import com.fofcn.trivialfs.netty.FileDataProtos;
import com.fofcn.trivialfs.netty.NettyProtos;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.fofcn.trivialfs.netty.enums.ResponseCode;
import org.fofcn.trivialfs.netty.netty.NetworkCommand;
import org.fofcn.trivialfs.netty.processor.NettyRequestProcessor;
import org.fofcn.trivialfs.store.block.BlockFile;
import org.fofcn.trivialfs.store.block.FileBlock;
import org.fofcn.trivialfs.store.block.FileHeader;
import org.fofcn.trivialfs.store.block.FileTailor;
import org.fofcn.trivialfs.store.common.AppendResult;

import java.util.Random;

/**
 * 文件数据处理器
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/28 17:08
 */
@Slf4j
public class FileDataProcessor implements NettyRequestProcessor {

    private final BlockFile fileData;

    public FileDataProcessor(BlockFile fileData) {
        this.fileData = fileData;
    }

    @Override
    public NetworkCommand processRequest(ChannelHandlerContext ctx, NetworkCommand request) throws Exception {
        NettyProtos.NettyRequest body = NettyProtos.NettyRequest.parseFrom(request.getBody());
        if (NettyProtos.NettyRequest.RequestCase.FILEREQUEST.equals(body.getRequestCase())) {
            log.info("file upload request..");
        }

        // 组装File对象
        FileHeader header = new FileHeader();
        header.setDeleteStatus(0);
        header.setKey(new Random().nextLong());
        header.setLength(body.getFileRequest().getData().size());
        FileTailor tailor = new FileTailor();
        FileBlock fileBlock = new FileBlock();
        fileBlock.setHeader(header);
        fileBlock.setBody(body.getFileRequest().getData().toByteArray());
        fileBlock.setTailor(tailor);
        AppendResult result = fileData.append(fileBlock);
        FileDataProtos.FileReply fileReply;
        if (result.getOffset() != -1L) {
            fileReply = FileDataProtos.FileReply.newBuilder()
                    .setSuccess(true)
                    .setKey(header.getKey())
                    .build();
        } else {
            fileReply = FileDataProtos.FileReply.newBuilder()
                    .setSuccess(false)
                    .build();
        }

        NettyProtos.NettyReply reply = NettyProtos.NettyReply.newBuilder().setFileReply(fileReply).build();
        return NetworkCommand.createResponseCommand(ResponseCode.SUCCESS.getCode(), reply.toByteArray());
    }
}
