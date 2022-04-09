package org.fofcn.trivialfs.client.config;

import org.fofcn.trivialfs.netty.config.NettyClientConfig;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 客户端配置
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/31 16:41
 */
@Builder
@Data
public class ClientConfig {

    /**
     * 线程数量
     */
    private int threadCnt;

    /**
     * 客户端TCP配置
     */
    private NettyClientConfig tcpClientConfig;

    private List<String> storeNodes;
}
