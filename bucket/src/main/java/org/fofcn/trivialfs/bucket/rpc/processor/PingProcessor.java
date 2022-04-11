package org.fofcn.trivialfs.bucket.rpc.processor;

import io.netty.channel.ChannelHandlerContext;
import org.fofcn.trivialfs.netty.netty.NetworkCommand;
import org.fofcn.trivialfs.netty.processor.NettyRequestProcessor;

/**
 * 心跳消息处理器
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 15:37
 */
public class PingProcessor implements NettyRequestProcessor {
    @Override
    public NetworkCommand processRequest(ChannelHandlerContext ctx, NetworkCommand request) throws Exception {
        return null;
    }
}
