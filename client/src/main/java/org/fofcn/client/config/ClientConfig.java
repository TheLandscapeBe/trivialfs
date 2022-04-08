package org.fofcn.client.config;

import org.fofcn.netty.config.NettyClientConfig;
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

    private int threadCnt;

    private NettyClientConfig tcpClientConfig;

    private List<String> storeNodes;
}
