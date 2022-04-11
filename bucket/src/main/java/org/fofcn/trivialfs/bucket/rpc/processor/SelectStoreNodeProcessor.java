package org.fofcn.trivialfs.bucket.rpc.processor;

import io.netty.channel.ChannelHandlerContext;
import org.fofcn.trivialfs.netty.netty.NetworkCommand;
import org.fofcn.trivialfs.netty.processor.NettyRequestProcessor;

/**
 * 选择存储节点处理器
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 16:45
 */
public class SelectStoreNodeProcessor implements NettyRequestProcessor {
    @Override
    public NetworkCommand processRequest(ChannelHandlerContext ctx, NetworkCommand request) throws Exception {
        return null;
    }
}
