package org.fofcn.trivialfs.store.guid;


import org.fofcn.trivialfs.store.guid.autoincr.AutoIncrConfig;
import org.fofcn.trivialfs.store.guid.autoincr.AutoIncrUid;
import org.fofcn.trivialfs.store.guid.snowflake.SnowFlakeConfig;
import org.fofcn.trivialfs.store.guid.snowflake.SnowFlakeUid;

/**
 * Uid工厂
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/07 14:18
 */
public class UidFactory {

    /**
     * 创建UID生成器
     * @return
     */
    public static UidGenerator createGenerator(UidEnum uidEnum, UidConfig uidConfig) {
        if (uidEnum.equals(UidEnum.SNOW_FLAKE)) {
            return new SnowFlakeUid((SnowFlakeConfig) uidConfig);
        } else if (uidEnum.equals(UidEnum.OTHER)) {
            return new AutoIncrUid((AutoIncrConfig) uidConfig);
        } else {
            return null;
        }
    }
}
