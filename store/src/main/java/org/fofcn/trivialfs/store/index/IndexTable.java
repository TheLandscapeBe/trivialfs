package org.fofcn.trivialfs.store.index;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.ObjectUtils;
import org.fofcn.trivialfs.common.R;
import org.fofcn.trivialfs.common.RWrapper;
import org.fofcn.trivialfs.store.common.AppendResult;
import org.fofcn.trivialfs.store.common.BaseFile;
import org.fofcn.trivialfs.store.common.constant.StoreConstant;
import org.fofcn.trivialfs.store.common.flush.DefaultFlushStrategyFactory;
import org.fofcn.trivialfs.store.common.flush.FlushStrategy;
import org.fofcn.trivialfs.store.common.flush.FlushStrategyConfig;
import org.fofcn.trivialfs.store.config.StoreConfig;
import org.fofcn.trivialfs.store.index.pubsub.IndexFileConsumer;
import org.fofcn.trivialfs.store.pubsub.Broker;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

/**
 * 索引文件
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/23 17:31:00
 */
public class IndexTable extends BaseFile {

    private final Broker broker;

    private final IndexSuperBlock superBlock;

    private volatile MappedByteBuffer mappedBuffer;

    private FlushStrategy flushStrategy;

    private final FlushStrategyConfig flushStrategyConfig;

    public IndexTable(final File file,
                      final Broker broker,
                      final StoreConfig storeConfig,
                      final FlushStrategyConfig flushConfig) {
        super(file, storeConfig.getAutoExpandSize(), storeConfig.getMaxBlockFileSize());
        this.broker = broker;
        this.superBlock = new IndexSuperBlock();
        this.flushStrategyConfig = flushConfig;
    }

    public void append(IndexNode indexNode) {
        if (ObjectUtils.isEmpty(indexNode)) {
            throw new IllegalArgumentException("");
        }

        ByteBuffer buffer = indexNode.encode();
        R<AppendResult> appendResult = super.append(buffer);
        if (RWrapper.isSuccess(appendResult)) {
            // todo
        }
    }

    /**
     * 删除文件
     * @param fileKey
     * @return
     */
    public R<?> delete(long fileKey) {
       // todo
        throw new NotImplementedException();
    }

    @Override
    protected void doAfterAppend(long offset, int length) {
        superBlock.getAmount().incrementAndGet();
        superBlock.getWritePos().addAndGet(length);
        ByteBuffer superBuffer = mappedBuffer.slice();
        superBuffer.put(superBlock.encode());
        flushStrategy.flush();
    }

    @Override
    protected void doInitNewFile() throws IOException {
        // 文件新建，先预分配超级块
        padFile(StoreConstant.SUPER_BLOCK_LENGTH);
        // 写入超级块魔数
        writeLong(superBlock.getMagic());
        // 写入文件数量
        writeLong(superBlock.getAmount().get());
        // 重定位写入位置
        resetWritePos(StoreConstant.SUPER_BLOCK_LENGTH);
    }

    @Override
    protected void doRecover() throws IOException {
        // 读取超级块
        ByteBuffer buffer = read(0L, IndexSuperBlock.LENGTH);
        superBlock.decode(buffer);
        // todo 核对数据文件数量和索引节点数量，不一致需要从数据文件中恢复索引节点
        // 重置写入偏移
        resetWritePos(superBlock.getAmount().get() * IndexNode.LENGTH);
    }

    @Override
    protected void doAfterInit() throws IOException {
        broker.registerConsumer(StoreConstant.BLOCK_TOPIC_NAME, new IndexFileConsumer(this));
        flushStrategy = new DefaultFlushStrategyFactory().createStrategy(flushStrategyConfig, getFileChannel());
        mappedBuffer = map(0, 4096);
    }

}
