package org.fofcn.trivialfs.bucket.config;

import lombok.Data;

/**
 * Configuration for zookeeper client
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/15 16:56
 */
@Data
public class ZkClientConfig {

    /**
     * connection string for zookeeper cluster
     */
    private String addresses = "127.0.0.1:2181";

    /**
     * client session timeout in milliseconds default 3000
     */
    private int sessionTimeoutMs = 3000;

    /**
     * client connection timeout in milliseconds default 3000
     */
    private int connectionTimeoutMs = 3000;

    /**
     * client retry sleep time in milliseconds default 3000
     */
    private int retryBaseSleepTimeMs = 3000;

    /**
     * client maximum retry time default 3
     */
    private int maxRetryTime = 3;
}
