package org.fofcn.store.index.pubsub;

import org.fofcn.store.block.pubsub.BlockFileMessage;
import org.fofcn.store.index.IndexNode;
import org.fofcn.store.index.IndexTable;
import org.fofcn.store.pubsub.Consumer;
import org.fofcn.store.pubsub.Message;

/**
 * 块文件消息消费者
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/25 17:09
 */
public class IndexFileConsumer implements Consumer {

    private final IndexTable indexFile;

    public IndexFileConsumer(IndexTable indexFile) {
        this.indexFile = indexFile;
    }

    @Override
    public void consume(Message message) {
        BlockFileMessage blockFileMsg = (BlockFileMessage) message;
        IndexNode indexNode = new IndexNode();
        indexNode.setDeleteStatus(blockFileMsg.getDeleteStatus());
        indexNode.setKey(blockFileMsg.getKey());
        indexNode.setOffset(blockFileMsg.getOffset());
        indexNode.setSize(blockFileMsg.getSize());

        indexFile.append(indexNode);
    }
}
