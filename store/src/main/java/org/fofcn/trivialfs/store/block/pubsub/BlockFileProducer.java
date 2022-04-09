package org.fofcn.trivialfs.store.block.pubsub;


import org.fofcn.trivialfs.store.block.FileHeader;
import org.fofcn.trivialfs.store.pubsub.Broker;
import org.fofcn.trivialfs.store.pubsub.DefaultProducer;

/**
 * 文件块存储生产者
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/25 16:38
 */
public class BlockFileProducer extends DefaultProducer {

    public BlockFileProducer(Broker broker) {
        super(broker);
    }

    public void produce(FileHeader fileHeader, long offset) {
        BlockFileMessage message = new BlockFileMessage();
        message.setKey(fileHeader.getKey());
        message.setDeleteStatus(fileHeader.getDeleteStatus());
        message.setOffset(offset);
        message.setSize(fileHeader.getLength());
        super.produce(message);
    }
}
