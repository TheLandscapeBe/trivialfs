package org.fofcn.trivialfs.store.distributed.masterslave;


import org.fofcn.trivialfs.store.disk.block.BlockFile;
import org.fofcn.trivialfs.store.disk.block.FileBlock;
import org.fofcn.trivialfs.store.disk.block.pubsub.BlockFileMessage;
import org.fofcn.trivialfs.store.distributed.masterslave.longpoll.LongPolling;
import org.fofcn.trivialfs.store.pubsub.Consumer;
import org.fofcn.trivialfs.store.pubsub.Message;

/**
 * 集群消费同步
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/02 15:54
 */
public class ClusterConsumer implements Consumer {

    private final BlockFile blockFile;

    private final LongPolling longPolling;

    public ClusterConsumer(final BlockFile blockFile,
                           final LongPolling longPolling) {
        this.blockFile = blockFile;
        this.longPolling = longPolling;
    }


    @Override
    public void consume(Message message) {
        BlockFileMessage blockFileMsg = (BlockFileMessage) message;

        FileBlock fileBlock = blockFile.read(blockFileMsg.getOffset());

        LongPollData longPollData = new LongPollData(blockFileMsg.getOffset(),
                fileBlock.getHeader().getKey(),
                fileBlock.getBody());
        longPolling.notifyWrite(longPollData);
    }
}
