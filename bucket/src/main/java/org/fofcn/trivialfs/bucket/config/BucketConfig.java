package org.fofcn.trivialfs.bucket.config;

import lombok.Data;
import org.fofcn.trivialfs.coordinate.zk.ZkClientConfig;
import org.fofcn.trivialfs.netty.config.NettyServerConfig;

/**
 * bucket配置
 *
 * @author errorfatal89@gmail.com
 * @date 2022/04/11 16:49:00
 */
@Data
public class BucketConfig {

    private NettyServerConfig serverConfig;

    /**
     * coordinate type
     */
    private String coordinateType;

    /**
     * configuration of zookeeper
     */
    private ZkClientConfig zkConfig;
}
