package org.fofcn.trivialfs.bucket.volume.lb.impl;

import org.fofcn.trivialfs.bucket.volume.StoreNode;
import org.fofcn.trivialfs.bucket.volume.lb.LoadBalance;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * volume load balance algorithm:round robin
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/21 14:48
 */
public class VolumeRoundRobinLoadBalance implements LoadBalance<StoreNode> {

    private final CopyOnWriteArrayList<StoreNode> storeNodeList = new CopyOnWriteArrayList<>();

    private volatile int curIdx = 0;

    public VolumeRoundRobinLoadBalance(List<StoreNode> storeNodeList) {
        this.storeNodeList.addAll(storeNodeList);
    }

    @Override
    public StoreNode selectOne() {
        StoreNode selected;
        synchronized (this) {
            selected = storeNodeList.get(curIdx++);
            if (curIdx == storeNodeList.size()) {
                curIdx = 0;
            }
        }
        return selected;
    }
}
