package org.fofcn.trivialfs.bucket.volume.etcd;

import org.fofcn.trivialfs.coordinate.StoreNode;
import org.fofcn.trivialfs.bucket.volume.VolumeManager;

import java.util.Optional;

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
    public boolean delete(String name) {
        return false;
    }

    @Override
    public Optional<StoreNode> getReadableNode(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<StoreNode> getWritableNode(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<StoreNode> getNodeByFileKey(String name, String fileKey) {
        return Optional.empty();
    }

    @Override
    public boolean addWritableNode(String name, StoreNode storeNode) {
        return false;
    }

    @Override
    public boolean addReadableNode(String name, StoreNode storeNode) {
        return false;
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
