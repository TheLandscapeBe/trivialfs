package org.fofcn.trivialfs.store.config;

import lombok.Data;
import org.fofcn.trivialfs.coordinate.constant.CoordinateTypeEnum;
import org.fofcn.trivialfs.coordinate.zk.ZkClientConfig;

/**
 * bucket configure
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/05/06 16:04
 */
@Data
public class BucketConfig {
    /**
     * coordinate type
     */
    private CoordinateTypeEnum type;

    /**
     * bucket name
     */
    private String name;

    /**
     * zookeeper client configure
     */
    private ZkClientConfig zkConfig;
}
