package org.fofcn.trivialfs.bucket.volume;

import org.fofcn.trivialfs.bucket.exception.VolumeException;
import org.fofcn.trivialfs.common.Service;

import java.util.List;
import java.util.Optional;

/**
 * 逻辑卷管理器
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 16:48
 */
public interface VolumeManager extends Service {

    /**
     * create a volume with specific volume name.
     * @param name volume name
     * @return true if success, false otherwise.
     * @throws VolumeException
     */
    boolean create(String name) throws VolumeException;

    /**
     * delete a volume with specific volume name
     * @param name volume name
     * @return true if success, false otherwise
     */
    boolean delete(String name);

    /**
     * get a readable node list
     * @param name volume name
     * @return
     */
    Optional<StoreNode> getReadableNode(String name);

    /**
     * get a writable node list
     * @param name volume name
     * @return
     */
    Optional<StoreNode> getWritableNode(String name);

    /**
     * get a node by file key
     * @param name volume name
     * @param fileKey file key
     * @return a storage node which chosen by load balance algorithm
     */
    Optional<StoreNode> getNodeByFileKey(String name, String fileKey);

    /**
     * add a writable store node to volume
     * @param name bucket name
     * @param storeNode store node information
     * @return true if added success, false otherwise
     */
    boolean addWritableNode(String name, StoreNode storeNode);

    /**
     * add a readable store node to volume
     * @param name bucket name
     * @param storeNode store node information
     * @return true if added success, false otherwise
     */
    boolean addReadableNode(String name, StoreNode storeNode);

}
