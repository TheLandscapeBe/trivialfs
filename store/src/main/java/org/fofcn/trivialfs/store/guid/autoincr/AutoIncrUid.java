package org.fofcn.trivialfs.store.guid.autoincr;


import org.fofcn.trivialfs.store.guid.UidGenerator;

/**
 * 自增ID
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/07 18:41
 */
public class AutoIncrUid implements UidGenerator<Long> {

    private final AutoIncrConfig config;

    public AutoIncrUid(AutoIncrConfig config) {
        this.config = config;
    }

    @Override
    public Long generate() {
        return null;
    }
}
