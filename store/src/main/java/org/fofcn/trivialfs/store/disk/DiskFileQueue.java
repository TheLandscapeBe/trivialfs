package org.fofcn.trivialfs.store.disk;


import org.fofcn.trivialfs.common.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Disk manager
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/29 12:20
 */
public class DiskFileQueue<T> implements Service {

    private final CopyOnWriteArrayList<T> diskFiles = new CopyOnWriteArrayList<>();

    public DiskFileQueue() {

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
