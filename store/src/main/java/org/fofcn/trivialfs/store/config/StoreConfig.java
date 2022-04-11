package org.fofcn.trivialfs.store.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.fofcn.trivialfs.netty.config.NettyClientConfig;
import org.fofcn.trivialfs.netty.config.NettyServerConfig;
import org.fofcn.trivialfs.store.common.flush.FlushStrategyConfig;
import org.fofcn.trivialfs.store.guid.snowflake.SnowFlakeConfig;


/**
 * 存储配置
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/24 13:50
 */
@Data
public class StoreConfig {

    /**
     * bucket列表
     */
    @JsonProperty
    private String buckets;

    /**
     * 存储文件路径
     */
    @JsonProperty
    private String blockPath;

    /**
     * 索引文件存储路径
     */
    @JsonProperty
    private String indexPath;

    /**
     * 自动扩展大小
     */
    @JsonProperty
    private long autoExpandSize;

    /**
     * 最大块大小
     */
    @JsonProperty
    private long maxBlockFileSize;

    /**
     * 最多块数
     */
    @JsonProperty
    private int maxBlockFileCnt;

    /**
     * 文件删除比率 删除大小 / maxBlockFileSize
     */
    @JsonProperty
    private int compactRatio;

    /**
     * 磁盘预留空间比率
     */
    @JsonProperty
    private int preserveRatio;

    /**
     * 输盘配置
     */
    @JsonProperty
    private FlushStrategyConfig flushConfig;

    /**
     * TCP server配置
     */
    @JsonProperty
    private NettyServerConfig serverConfig;

    /**
     * TCP client配置
     */
    @JsonProperty
    private NettyClientConfig clientConfig;

    /**
     * cluster configuration
     */
    @JsonProperty
    private ClusterConfig clusterConfig;

    /**
     * Uid configuration
     */
    @JsonProperty
    private SnowFlakeConfig uidConfig;

}
