package org.fofcn.trivialfs.rpc.netty;

import com.fofcn.trivialfs.netty.NettyProtos;
import io.netty.channel.ChannelHandlerContext;
import org.fofcn.trivialfs.netty.netty.NetworkCommand;
import org.fofcn.trivialfs.netty.processor.NettyRequestProcessor;

/**
 * abstract request processor
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/28 13:13
 */
public abstract class NettyRequestProcessorAdapter implements NettyRequestProcessor {

    @Override
    public NetworkCommand processRequest(ChannelHandlerContext ctx, NetworkCommand request) throws Exception {
        NettyProtos.NettyRequest req = NettyProtos.NettyRequest.parseFrom(request.getBody());
        ProcessorResult replyResult = processRequest0(req);
        return NetworkCommand.createResponseCommand(replyResult.getCode(), replyResult.getReply().toByteArray());
    }

    /**
     * process request
     * @param request netty protocol request
     * @return process result
     */
    protected abstract ProcessorResult processRequest0(NettyProtos.NettyRequest request);
}
