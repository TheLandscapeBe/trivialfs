package org.fofcn.trivialfs.store.bucket;

import lombok.extern.slf4j.Slf4j;
import org.fofcn.trivialfs.common.DiskUtil;
import org.fofcn.trivialfs.common.Service;
import org.fofcn.trivialfs.common.thread.PoolHelper;
import org.fofcn.trivialfs.netty.config.NettyClientConfig;
import org.fofcn.trivialfs.netty.netty.NettyNetworkClient;
import org.fofcn.trivialfs.store.config.StoreConfig;
import org.fofcn.trivialfs.store.disk.DiskManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/29 12:19
 */
@Slf4j
public class BucketManager implements Service {
    private final NettyNetworkClient networkClient;
    private final StoreConfig storeConfig;
    private final ScheduledThreadPoolExecutor timer = PoolHelper.newScheduledExecutor(DiskManager.class.getName(), "disk-manager", 1);

    public BucketManager(StoreConfig storeConfig) {
        this.storeConfig = storeConfig;
        // todo 配置
        NettyClientConfig clientConfig = new NettyClientConfig();
        this.networkClient = new NettyNetworkClient(clientConfig);

    }

    @Override
    public boolean init() {
        // 心跳上报硬盘数据与是否可写
        timer.scheduleAtFixedRate(() -> {
            try {
                Map<String, Long> diskInfo = DiskUtil.getDiskSpace(storeConfig.getBlockPath());

            } catch (IOException e) {
                log.error("get disk space error", e);
            }


        }, 0, 500, TimeUnit.MILLISECONDS);
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
