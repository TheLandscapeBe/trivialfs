package org.fofcn.trivialfs.rpc.netty;

import com.fofcn.trivialfs.netty.NettyProtos;
import lombok.Data;

/**
 * processor result
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/28 13:22
 */
@Data
public class ProcessorResult {

    private int code;

    private NettyProtos.NettyReply reply;
}
