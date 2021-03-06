package org.fofcn.trivialfs.client;

import org.fofcn.trivialfs.client.config.ClientConfig;
import org.fofcn.trivialfs.client.impl.TrivialClientImpl;
import org.fofcn.trivialfs.common.exception.TrivialFsException;
import org.fofcn.trivialfs.common.network.NodeAddress;
import org.fofcn.trivialfs.common.thread.PoolHelper;
import org.fofcn.trivialfs.netty.config.NettyClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * 客户端测试类
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/03/31 17:21
 */
@RunWith(MockitoJUnitRunner.class)
public class TrivialfsClientTest {

    private static TrivialClientImpl trivialClient;

    private static TrivialClientImpl spyClient;

    private static ClientManager clientManager;

    private static final ThreadPoolExecutor testPool = PoolHelper.newFixedPool("", "", 8, 1024000);

    @BeforeClass
    public static void beforeClass() {
        ClientConfig clientConfig = ClientConfig.builder()
                .threadCnt(2)
                .tcpClientConfig(new NettyClientConfig())
                .build();
        clientManager = new ClientManager(clientConfig);
        clientManager.init();
        trivialClient = (TrivialClientImpl) clientManager.getClient();
        spyClient = spy(trivialClient);
    }

    @AfterClass
    public static void afterClass() {
        clientManager.shutdown();
    }

    @Test
    public void testWrite() throws NoSuchFieldException {
        try {
            // "192.168.1.83"
            // "127.0.0.1"
            when(spyClient.getWriteEndPoint("")).thenReturn(new NodeAddress("192.168.1.83", 41234));
            File file = new File("G:\\github.com\\html-exporter-master.zip");
            InputStream in = new FileInputStream(file);
            byte[] content = new byte[in.available()];
            in.read(content);
            for (int i = 0; i < 1000000; i++) {
                testPool.execute(() -> {
                    ApiResult<Long> fileKey = null;
                    try {
                        fileKey = spyClient.write("", content);
                        if (ApiResultWrapper.isSuccess(fileKey)) {
                            System.out.println(fileKey.getData());
                        }
                    } catch (TrivialFsException e) {
                        e.printStackTrace();
                    }
                });
            }
            testPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
