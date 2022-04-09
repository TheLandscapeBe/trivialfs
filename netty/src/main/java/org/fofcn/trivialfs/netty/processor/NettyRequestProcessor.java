package org.fofcn.trivialfs.netty.processor;

import io.netty.channel.ChannelHandlerContext;
import org.fofcn.trivialfs.netty.netty.NetworkCommand;

/**
 * @author errorfatal89@gmail.com
 */
public interface NettyRequestProcessor extends RequestProcessor {

    NetworkCommand processRequest(ChannelHandlerContext ctx, NetworkCommand request)
            throws Exception;

}
