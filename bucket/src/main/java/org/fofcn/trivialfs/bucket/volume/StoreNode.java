package org.fofcn.trivialfs.bucket.volume;

import lombok.Data;

/**
 * storage node information
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/14 17:56
 */
@Data
public class StoreNode {

    /**
     * 对等端Id
     */
    private String peerId;

    /**
     * 地址
     */
    private String address;

}
