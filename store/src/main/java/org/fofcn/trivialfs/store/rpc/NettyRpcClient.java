package org.fofcn.trivialfs.store.rpc;

import com.fofcn.trivialfs.netty.NettyProtos;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.fofcn.trivialfs.common.exception.TrivialFsException;
import org.fofcn.trivialfs.common.thread.PoolHelper;
import org.fofcn.trivialfs.netty.NetworkClient;
import org.fofcn.trivialfs.netty.config.NettyClientConfig;
import org.fofcn.trivialfs.netty.exception.NetworkConnectException;
import org.fofcn.trivialfs.netty.exception.NetworkSendRequestException;
import org.fofcn.trivialfs.netty.exception.NetworkTimeoutException;
import org.fofcn.trivialfs.netty.netty.NettyNetworkClient;
import org.fofcn.trivialfs.netty.netty.NetworkCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Rpc客户端
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/31 14:33
 */
@Slf4j
public class NettyRpcClient {

    private final NetworkClient networkClient;

    private final ThreadPoolExecutor broadcastPool;

    private final long waitMillis;

    /**
     * 对等端表
     */
    private final CopyOnWriteArrayList<String> peers = new CopyOnWriteArrayList<>();

    public NettyRpcClient(final NettyClientConfig nettyClientConfig, final int concurrent, final List<String> peers) {
        this.networkClient = new NettyNetworkClient(nettyClientConfig);
        this.networkClient.start();
        this.waitMillis = nettyClientConfig.getConnectTimeoutMillis();
        this.broadcastPool = PoolHelper.newFixedPool("offset", "offset-", concurrent, 1024);

        for (int i = 0; i < peers.size(); i++) {
            peers.add(peers.get(i));
        }
    }

    /**
     * 同步调用
     * @param requestCode 请求码
     * @param request 请求
     * @return 结果
     */
    public NettyProtos.NettyReply callSync(int requestCode, NettyProtos.NettyRequest request) throws TrivialFsException {
        NetworkCommand networkCommand = NetworkCommand.createRequestCommand(requestCode, request.toByteArray());
        if (peers.size() == 1) {
            try {
                networkClient.sendSync(peers.get(0), networkCommand, waitMillis);
            } catch (NetworkConnectException e) {
                throw new TrivialFsException(e);
            } catch (NetworkSendRequestException e) {
                throw new TrivialFsException(e);
            } catch (InterruptedException e) {
                throw new TrivialFsException(e);
            } catch (NetworkTimeoutException e) {
                throw new TrivialFsException(e);
            }
        }

        throw new TrivialFsException("multi-peers exists");
    }


    /**
     * 同步调用
     * @param requestCode 请求码
     * @param request 请求
     * @return 结果
     */
    public void callOneWay(int requestCode, NettyProtos.NettyRequest request) throws TrivialFsException {
        NetworkCommand networkCommand = NetworkCommand.createRequestCommand(requestCode, request.toByteArray());
        if (peers.size() != 1) {
            try {
                networkClient.sendOneway(peers.get(0), networkCommand, waitMillis, null);
            } catch (NetworkConnectException e) {
                throw new TrivialFsException(e);
            } catch (NetworkSendRequestException e) {
                throw new TrivialFsException(e);
            } catch (InterruptedException e) {
                throw new TrivialFsException(e);
            } catch (NetworkTimeoutException e) {
                throw new TrivialFsException(e);
            }
        }
    }

    /**
     * 同步组播
     * @param tasks 组播任务
     * @param <T> 组播返回
     * @return
     */
    private <T> List<T> parallelTask(List<Callable<T>> tasks, long waitMillis) {
        if (CollectionUtils.isEmpty(tasks)) {
            return null;
        }

        List<T> result = new ArrayList<>(tasks.size());
        List<Future<T>> taskResultLst = new ArrayList<>(tasks.size());
        CountDownLatch countDownLatch = new CountDownLatch(tasks.size());
        tasks.forEach(task -> {
            Future<T> taskResult = broadcastPool.submit(task);
            taskResultLst.add(taskResult);
            countDownLatch.countDown();
        });

        // 根据超时时间进行
        try {
            countDownLatch.await(waitMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("", e);
        }

        // 获取任务执行结果，能获取一个是一个
        for (Future<T> taskFuture : taskResultLst) {
            try {
                T ret = taskFuture.get();
                result.add(ret);
            } catch (ExecutionException | InterruptedException e) {
                log.error("", e);
            }
        }

        return result;
    }

    public void shutdown() {
        networkClient.shutdown();
    }
}
