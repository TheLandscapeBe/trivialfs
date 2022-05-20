package org.fofcn.trivialfs.coordinate;

import lombok.Data;

import java.util.List;

/**
 * storage node information
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/14 17:56
 */
@Data
public class StoreNode {

    /**
     * identity string
     */
    private String peerId;

    /**
     * node address, address format: ip:port, such as 127.0.0.1:60000
     */
    private String address;

    /**
     * master identity
     * if peer id equals master id,then the store node is master
     */
    private String masterId;

    /**
     * slave list
     */
    List<StoreNode> slaveList;
}
