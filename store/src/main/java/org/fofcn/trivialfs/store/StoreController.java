package org.fofcn.trivialfs.store;


import org.fofcn.trivialfs.store.block.BlockFile;
import org.fofcn.trivialfs.store.config.StoreConfig;
import org.fofcn.trivialfs.store.distributed.ClusterManager;
import org.fofcn.trivialfs.store.distributed.DefaultClusterFactory;
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
public class StoreController {

    private final StoreNetworkServer storeServer;

    private final BlockFile blockFile;

    private final IndexTable indexTable;

    private final Broker broker;

    private final ClusterManager clusterManager;

    public StoreController(StoreConfig storeConfig) {
        this.broker = new DefaultBroker();
        this.blockFile = new BlockFile(new File(storeConfig.getBlockPath() + File.separator + "block"), broker, storeConfig.getFlushConfig());
        this.indexTable = new IndexTable(new File(storeConfig.getIndexPath() + File.separator + "index"), broker);
        this.storeServer = new StoreNetworkServer(blockFile, storeConfig.getServerConfig());
        this.clusterManager = new DefaultClusterFactory().getCluster(storeConfig.getClusterConfig(), broker, blockFile);
    }

    public void init() {
        blockFile.init();
        indexTable.init();
        storeServer.init();
        clusterManager.init();
    }

    public void start() {
        storeServer.start();
        clusterManager.start();
    }

    public void shutdown() {
        storeServer.shutdown();
        blockFile.close();
        indexTable.close();
    }
}
