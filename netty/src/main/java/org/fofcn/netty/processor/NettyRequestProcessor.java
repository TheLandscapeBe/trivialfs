package org.fofcn.netty.processor;

import io.netty.channel.ChannelHandlerContext;
import org.fofcn.netty.netty.NetworkCommand;

/**
 * @author errorfatal89@gmail.com
 */
public interface NettyRequestProcessor extends RequestProcessor {

    NetworkCommand processRequest(ChannelHandlerContext ctx, NetworkCommand request)
            throws Exception;

}
