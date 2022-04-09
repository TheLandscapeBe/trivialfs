package org.fofcn.trivialfs.store.distributed.masterslave;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.fofcn.trivialfs.store.block.BlockFile;

/**
 * 长轮询参数
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/04 23:16
 */
@Data
public class LongPollArgs {

    private final ChannelHandlerContext ctx;

    private final long expectedOffset;

    private final BlockFile blockFile;

    public LongPollArgs(ChannelHandlerContext ctx, long expectedOffset, BlockFile blockFile) {
        this.ctx = ctx;
        this.expectedOffset = expectedOffset;
        this.blockFile = blockFile;
    }
}
