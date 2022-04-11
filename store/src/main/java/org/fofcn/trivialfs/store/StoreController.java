package org.fofcn.trivialfs.store;

import lombok.extern.slf4j.Slf4j;
import org.fofcn.trivialfs.store.block.BlockFile;
import org.fofcn.trivialfs.store.config.StoreConfig;
import org.fofcn.trivialfs.store.distributed.ClusterManager;
import org.fofcn.trivialfs.store.distributed.DefaultClusterFactory;
import org.fofcn.trivialfs.store.guid.UidEnum;
import org.fofcn.trivialfs.store.guid.UidFactory;
import org.fofcn.trivialfs.store.guid.UidGenerator;
import org.fofcn.trivialfs.store.index.IndexTable;
import org.fofcn.trivialfs.store.network.StoreNetworkServer;
import org.fofcn.trivialfs.store.pubsub.Broker;
import org.fofcn.trivialfs.store.pubsub.DefaultBroker;

import java.io.File;

/**
 * 存储控制器
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/28 17:29
 */
@Slf4j
public class StoreController {

    private final StoreNetworkServer storeServer;

    private final BlockFile blockFile;

    private final IndexTable indexTable;

    private final Broker broker;

    private final ClusterManager clusterManager;

    private final UidGenerator<Long> uidGenerator;

    public StoreController(StoreConfig storeConfig) {
        this.broker = new DefaultBroker();
        this.uidGenerator = UidFactory.createGenerator(UidEnum.SNOW_FLAKE, storeConfig.getUidConfig());
        if (uidGenerator == null) {
            log.error("init uid generator error");
            throw new IllegalArgumentException("init uid generator error");
        }
        this.blockFile = new BlockFile(new File(storeConfig.getBlockPath() + File.separator + "block"), broker, storeConfig.getFlushConfig(), uidGenerator);
        this.indexTable = new IndexTable(new File(storeConfig.getIndexPath() + File.separator + "index"), broker, storeConfig.getFlushConfig());
        this.storeServer = new StoreNetworkServer(blockFile, storeConfig.getServerConfig());
        this.clusterManager = new DefaultClusterFactory().getCluster(storeConfig.getClusterConfig(), broker, blockFile);
    }

    public void init() {
        log.info("init block file");
        blockFile.init();
        log.info("init index file");
        indexTable.init();
        log.info("init store server");
        storeServer.init();
        log.info("init cluster Manager");
        clusterManager.init();
    }

    public void start() {
        storeServer.start();
        log.info("start store server");
        clusterManager.start();
        log.info("start cluster manager");
    }

    public void shutdown() {
        storeServer.shutdown();
        blockFile.close();
        indexTable.close();
    }
}
