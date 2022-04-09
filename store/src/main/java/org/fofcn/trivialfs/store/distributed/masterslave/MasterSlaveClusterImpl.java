package org.fofcn.trivialfs.store.distributed.masterslave;

import com.fofcn.trivialfs.netty.ClusterProtos;
import com.fofcn.trivialfs.netty.NettyProtos;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.fofcn.trivialfs.common.EnumUtil;
import org.fofcn.trivialfs.common.R;
import org.fofcn.trivialfs.common.RWrapper;
import org.fofcn.trivialfs.common.exception.TrivialFsException;
import org.fofcn.trivialfs.common.network.RequestCode;
import org.fofcn.trivialfs.common.thread.PoolHelper;
import org.fofcn.trivialfs.netty.config.NettyClientConfig;
import org.fofcn.trivialfs.store.block.BlockFile;
import org.fofcn.trivialfs.store.common.AppendResult;
import org.fofcn.trivialfs.store.common.constant.StoreConstant;
import org.fofcn.trivialfs.store.config.ClusterConfig;
import org.fofcn.trivialfs.store.distributed.ClusterManager;
import org.fofcn.trivialfs.store.distributed.ClusterMode;
import org.fofcn.trivialfs.store.distributed.masterslave.longpoll.LongPolling;
import org.fofcn.trivialfs.store.distributed.masterslave.processor.PingProcessor;
import org.fofcn.trivialfs.store.distributed.masterslave.processor.ReplicateProcessor;
import org.fofcn.trivialfs.store.pubsub.Broker;
import org.fofcn.trivialfs.store.rpc.NettyRpcClient;
import org.fofcn.trivialfs.store.rpc.RpcServer;
import org.fofcn.trivialfs.store.rpc.RpcServerFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主从集群模式实现
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/01 17:07
 */
@Slf4j
public class MasterSlaveClusterImpl implements ClusterManager {

    private final ScheduledThreadPoolExecutor timerExecutor = PoolHelper.newScheduledExecutor("masterSlave", "master-slave-", 1);

    private final ThreadPoolExecutor replicatePool = PoolHelper.newSingleThreadPool("masterSlaveReplicate", "masterSlaveReplicate", 1024);

    private final ClusterConfig clusterConfig;

    private final NettyRpcClient nettyRpcClient;

    private final RpcServer rpcServer;

    private final ClusterMode clusterMode;

    private volatile MasterSlaveRole masterSlaveRole;

    private final Broker broker;

    private final BlockFile blockFile;

    private final ConcurrentHashMap<Integer, String> peerTable = new ConcurrentHashMap<>(4);

    private final AtomicInteger timeoutCounter = new AtomicInteger(0);

    private final LongPolling<LongPollData, LongPollArgs> longPolling;

    private volatile boolean isReplicateStart = false;

    public MasterSlaveClusterImpl(final ClusterConfig clusterConfig, final Broker broker, final BlockFile blockFile) {
        this.clusterConfig = clusterConfig;
        this.broker = broker;
        this.blockFile = blockFile;
        this.rpcServer = new RpcServerFactoryImpl().getRpcServer(clusterConfig.getRpcConfig());
        ClusterMode confMode = ClusterMode.getByCode(clusterConfig.getMode());
        if (confMode.equals(ClusterMode.INVALID_CLUSTER)) {
            throw new IllegalArgumentException("invalid cluster configuration: " + clusterConfig.getMode());
        }
        this.clusterMode = confMode;

        MasterSlaveRole confRole = EnumUtil.getByCode(MasterSlaveRole.values(), clusterConfig.getRole());
        if (confMode.equals(MasterSlaveRole.INVALID)) {
            throw new IllegalArgumentException("invalid cluster configuration: " + clusterConfig.getRole());
        }
        masterSlaveRole = confRole;
        if (masterSlaveRole.equals(MasterSlaveRole.SLAVE)) {
            isReplicateStart = true;
            log.info("start as slave role, set replicate start true.");
        }

        parsePeer();

        // 超时时间需要是长轮询的三倍 + C 秒
        NettyClientConfig nettyClientConfig = clusterConfig.getRpcConfig().toNettyClientConfig();
        nettyClientConfig.setConnectTimeoutMillis(190 * 1000);
        this.nettyRpcClient = new NettyRpcClient(nettyClientConfig, 1, new ArrayList<>(peerTable.values()));

        this.longPolling = new LongPolling();
    }

    @Override
    public void addPeer(String peer) {
    }

    @Override
    public int getPeerId() {
        return clusterConfig.getPeerId();
    }


    @Override
    public boolean init() {
        rpcServer.init();
        rpcServer.registerProcessor(RequestCode.PING, new PingProcessor(this, blockFile));
        rpcServer.registerProcessor(RequestCode.REPLICATE, new ReplicateProcessor(blockFile, clusterConfig.getPeerId(),
                longPolling));

        // 文件新增消费者，监听文件新增以满足长轮询
        broker.registerConsumer(StoreConstant.BLOCK_TOPIC_NAME, new ClusterConsumer(blockFile, longPolling));

        // 启动ping定时任务
        timerExecutor.scheduleAtFixedRate(() -> {
            try {
                if (masterSlaveRole.equals(MasterSlaveRole.SLAVE)) {
                    // 组装请求
                    ClusterProtos.PingRequest pingReq = ClusterProtos.PingRequest.newBuilder()
                            .setPeerId(clusterConfig.getPeerId())
                            .setTotalSpace(0)
                            .setUsedSpace(0)
                            .setFreeSpace(0)
                            .setCanWrite(true)
                            .build();
                    ClusterProtos.ClusterRequest clusterRequest = ClusterProtos.ClusterRequest.newBuilder()
                            .setPingRequest(pingReq).build();
                    NettyProtos.NettyRequest request = NettyProtos.NettyRequest.newBuilder()
                            .setClusterRequest(clusterRequest)
                            .build();
                    NettyProtos.NettyReply reply = nettyRpcClient.callSync(RequestCode.PING, request);
                    ClusterProtos.PingReply pingReply = reply.getClusterReply().getPingReply();
                    if (!pingReply.getSuccess()) {
                        log.error("master error");
                    }
                }
            } catch (Exception e) {
                log.error("heart beat error", e);
            }
        }, 0L, 500L, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public void start() {
        rpcServer.start();
        // 启动复制
        startReplicate();
    }

    @Override
    public void shutdown() {
        isReplicateStart = false;
        rpcServer.shutdown();
    }

    /**
     * 解析对等配置
     */
    private void parsePeer() {
        List<String> peerList = clusterConfig.getPeers();
        if (CollectionUtils.isNotEmpty(peerList)) {
            peerList.forEach(peer -> {
                if (StringUtils.isNotEmpty(peer)) {
                    String[] peerParts = peer.split(":");
                    if (peerParts.length != 3) {
                        log.error("peer config error, config:<{}>", peer);
                        return;
                    }

                    peerTable.put(Integer.parseInt(peerParts[0]), peerParts[1] + ":" + peerParts[2]);
                }
            });
        }
    }

    /**
     * 发送同步请求
     */
    private void startReplicate() {
        replicatePool.execute(() -> {
            while (isReplicateStart) {
                // 偏移大于0就开始同步
                ClusterProtos.ReplicateRequest replicateRequest = ClusterProtos.ReplicateRequest.newBuilder()
                        .setOffset(blockFile.getWritePos())
                        .setPeerId(clusterConfig.getPeerId())
                        .build();
                ClusterProtos.ClusterRequest clusterRequest = ClusterProtos.ClusterRequest.newBuilder()
                        .setReplicateRequest(replicateRequest)
                        .build();
                NettyProtos.NettyRequest request = NettyProtos.NettyRequest.newBuilder()
                        .setClusterRequest(clusterRequest)
                        .build();

                try {
                    NettyProtos.NettyReply replicateReply = nettyRpcClient.callSync(RequestCode.REPLICATE, request);
                    ClusterProtos.ReplicateReply replicateData = replicateReply.getClusterReply().getReplicateReply();
                    if (replicateData.getExists()) {
                        // 存在文件数据更新，则将数据文件写入到block file中
                        ByteString content = replicateData.getContent();
                        long offset = replicateData.getOffset();
                        R<AppendResult> r = blockFile.append(offset, content.asReadOnlyByteBuffer());
                        if (RWrapper.isFailed(r)) {
                            log.error("replicate and write block file error");
                        }
                    }
                } catch (TrivialFsException e) {
                    log.error("replicate error", e);
                }
            }
        });
    }
}
