package org.fofcn.trivialfs.store.block;

import lombok.Data;
import org.fofcn.trivialfs.store.common.Codec;
import org.fofcn.trivialfs.store.common.constant.StoreConstant;

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

    public static final long SUPER_BLOCK_LENGTH = 4096;

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

    public SuperBlock(long maxBlockFileSize, long compactRatio) {
        this.maxBlockFileSize = maxBlockFileSize;
        this.compactRatio = compactRatio;
        this.readWriteState = 1;
        this.magic = StoreConstant.STORE_SUPER_MAGIC_NUMBER;
        this.version = StoreConstant.STORE_VERSION;
        this.amount.set(0);
        this.writePos.set(0);
    }

    public ByteBuffer encode() {
        ByteBuffer buffer = ByteBuffer.allocate(SUPER_BLOCK_REAL_LENGTH);
        buffer.putLong(magic);
        buffer.putLong(version);
        buffer.putLong(amount.get());
        buffer.putLong(writePos.get());
        buffer.putLong(maxBlockFileSize);
        buffer.putLong(compactRatio);
        buffer.putLong(readWriteState);
        buffer.flip();
        return buffer;
    }

    @Override
    public SuperBlock decode(ByteBuffer buffer) {
        magic = buffer.getLong();
        version = buffer.getLong();
        amount.set(buffer.getLong());
        writePos.set(buffer.getLong());
        maxBlockFileSize = buffer.getLong();
        compactRatio = buffer.getLong();
        readWriteState = buffer.getLong();
        return this;
    }
}
