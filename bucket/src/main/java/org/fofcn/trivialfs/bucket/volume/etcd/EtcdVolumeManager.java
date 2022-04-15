package org.fofcn.trivialfs.bucket.volume.etcd;

import org.fofcn.trivialfs.bucket.volume.StoreNode;
import org.fofcn.trivialfs.bucket.volume.VolumeManager;

import java.util.List;

/**
 * volume manager based on etcd.
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/14 17::44
 */
public class EtcdVolumeManager implements VolumeManager {
    @Override
    public boolean create(String name) {
        return false;
    }

    @Override
    public List<StoreNode> getReadableNode(String name) {
        return null;
    }

    @Override
    public List<StoreNode> getWritableNode(String name) {
        return null;
    }

    @Override
    public StoreNode getNodeByFileKey(String name, String fileKey) {
        return null;
    }

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
