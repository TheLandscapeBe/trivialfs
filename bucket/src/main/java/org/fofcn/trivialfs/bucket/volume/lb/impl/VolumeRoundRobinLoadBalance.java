package org.fofcn.trivialfs.bucket.volume.lb.impl;

import org.fofcn.trivialfs.bucket.volume.StoreNode;
import org.fofcn.trivialfs.bucket.volume.lb.LoadBalance;

import java.util.List;

/**
 * volume load balance algorithm:round robin
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/21 14:48
 */
public class VolumeRoundRobinLoadBalance implements LoadBalance<StoreNode> {

    private final List<StoreNode> storeNodeList;

    public VolumeRoundRobinLoadBalance(List<StoreNode> storeNodeList) {
        this.storeNodeList = storeNodeList;
    }

    @Override
    public StoreNode selectOne(List<StoreNode> list) {
        return null;
    }
}
