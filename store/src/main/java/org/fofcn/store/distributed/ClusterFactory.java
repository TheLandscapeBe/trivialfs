package org.fofcn.store.distributed;


import org.fofcn.store.block.BlockFile;
import org.fofcn.store.config.ClusterConfig;
import org.fofcn.store.pubsub.Broker;

/**
 * 集群工厂类
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/01 ：53
 */
public interface ClusterFactory {

    /**
     * 获取集群管理器
     *
     * @param clusterConfig 集群配置
     * @param broker
     * @param blockFile
     * @return 集群管理器
     */
    ClusterManager getCluster(ClusterConfig clusterConfig, Broker broker, BlockFile blockFile);
}
