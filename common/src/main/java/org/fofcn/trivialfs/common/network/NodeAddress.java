package org.fofcn.trivialfs.common.network;

/**
 * 节点地址
 */
public class NodeAddress {

    private String ip;

    private int port;

    public NodeAddress() {
    }

    public NodeAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getAddress() {
        return ip + ':' + port;
    }
}
