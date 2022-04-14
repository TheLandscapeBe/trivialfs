package org.fofcn.trivialfs.bucket.volume;

import java.util.List;

/**
 * 逻辑卷管理器
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 16:48
 */
public interface VolumeManager {

    /**
     * create a volume with specific volume name.
     * @param name volume name
     * @return true if success, false otherwise.
     */
    boolean create(String name);

    /**
     * get a readable node list
     * @param name volume name
     * @return
     */
    List<StoreNode> getReadableNode(String name);

    /**
     * get a writable node list
     * @param name volume name
     * @return
     */
    List<StoreNode> getWritableNode(String name);

    /**
     * get a node by file key
     * @param name volume name
     * @param fileKey file key
     * @return a storage node which chosen by load balance algorithm
     */
    StoreNode getNodeByFileKey(String name, String fileKey);
}
