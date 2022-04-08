package org.fofcn.store.distributed;

import org.fofcn.common.Service;

/**
 * 集群管理
 *
 * 流程
 * 1、获取配置中的集群方式
 * 2、
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/01 13:08
 */
public interface ClusterManager extends Service {

    /**
     * 增加对等端
     * @param peer 对等端ip  “127.0.0.1::6000”
     */
    void addPeer(String peer);

    /**
     * 获取id
     * @return
     */
    int getPeerId();
}
