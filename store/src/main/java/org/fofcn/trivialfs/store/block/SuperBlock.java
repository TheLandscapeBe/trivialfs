package org.fofcn.trivialfs.store.block;

import lombok.Data;
import org.fofcn.trivialfs.store.common.Codec;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 超级块
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/23 17:30:00
 */
@Data
public class SuperBlock implements Codec<SuperBlock> {

    public static final int SUPER_BLOCK_REAL_LENGTH = 7 * Long.BYTES;

    /**
     * 魔数
     */
    private long magic;

    /**
     * 版本号
     */
    private long version;

    /**
     * 文件数量
     */
    private AtomicLong amount = new AtomicLong(0L);

    /**
     * 写入偏移
     */
    private AtomicLong writePos = new AtomicLong(0L);

    /**
     * 最大块大小
     */
    private long maxBlockFileSize;

    /**
     * 压缩比率
     */
    private long compactRatio;

    /**
     * 读写状态 0:只读 1: 读写
     */
    private long readWriteState;

    public SuperBlock(long magic, long version, long amount, long writePos) {
        this.magic = magic;
        this.version = version;
        this.amount.set(amount);
        this.writePos.set(writePos);
    }

    public ByteBuffer encode() {
        int length = 4 * Long.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.putLong(magic);
        buffer.putLong(version);
        buffer.putLong(amount.get());
        buffer.putLong(writePos.get());
        buffer.flip();
        return buffer;
    }

    @Override
    public SuperBlock decode(ByteBuffer buffer) {
        // todo 解码
        return null;
    }
}
