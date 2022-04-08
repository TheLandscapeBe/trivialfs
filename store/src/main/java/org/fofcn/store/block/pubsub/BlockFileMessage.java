package org.fofcn.store.block.pubsub;


import lombok.Data;
import org.fofcn.store.common.constant.StoreConstant;
import org.fofcn.store.pubsub.Message;

/**
 * 文件块消息
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/25 16:40
 */
@Data
public class BlockFileMessage implements Message {

    /**
     * 文件key
     */
    private long key;

    /**
     * 文件偏移
     */
    private long offset;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 是否删除
     */
    private long deleteStatus;

    @Override
    public String getTopicName() {
        return StoreConstant.BLOCK_TOPIC_NAME;
    }
}
