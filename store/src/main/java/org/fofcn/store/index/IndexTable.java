package org.fofcn.store.index;

import org.apache.commons.lang3.ObjectUtils;
import org.fofcn.common.R;
import org.fofcn.common.RWrapper;
import org.fofcn.store.common.AppendResult;
import org.fofcn.store.common.BaseFile;
import org.fofcn.store.common.constant.StoreConstant;
import org.fofcn.store.index.pubsub.IndexFileConsumer;
import org.fofcn.store.pubsub.Broker;

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

    public IndexTable(File file, final Broker broker) {
        super(file);
        this.broker = broker;
        this.superBlock = new IndexSuperBlock();
    }

    public void append(IndexNode indexNode) {
        if (ObjectUtils.isEmpty(indexNode)) {
            throw new IllegalArgumentException("");
        }

        ByteBuffer buffer = indexNode.encode();
        R<AppendResult> appendResult = super.append(buffer);
        if (RWrapper.isSuccess(appendResult)) {

        }
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

        mappedBuffer = map(0, 4096);
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
    protected void doAfterInit() {
        broker.registerConsumer(StoreConstant.BLOCK_TOPIC_NAME, new IndexFileConsumer(this));
    }

}
