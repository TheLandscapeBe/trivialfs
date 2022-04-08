package org.fofcn.store.network;

import lombok.extern.slf4j.Slf4j;
import org.fofcn.common.network.RequestCode;
import org.fofcn.common.thread.PoolHelper;
import org.fofcn.netty.NetworkServer;
import org.fofcn.netty.config.NettyServerConfig;
import org.fofcn.netty.netty.NettyNetworkServer;
import org.fofcn.store.block.BlockFile;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 存储TCP服务
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/28 17:10
 */
@Slf4j
public class StoreNetworkServer {

    private NetworkServer server;

    private final BlockFile blockFile;

    private final ThreadPoolExecutor blockFilePool = PoolHelper.newFixedPool("blockFileExecutor", "block-file-thread-", 8, 1024);

    public StoreNetworkServer(final BlockFile blockFile, final NettyServerConfig nettyServerConfig) {
        this.blockFile = blockFile;
        log.info("store server config: {}", nettyServerConfig);
        this.server = new NettyNetworkServer(nettyServerConfig);
    }

    public boolean init() {
        server.registerProcessor(RequestCode.FILE_UPLOAD, new FileDataProcessor(blockFile), blockFilePool);
        return true;
    }

    public void start() {
        server.start();
    }

    public void shutdown() {
        server.shutdown();
    }
}
