package com.fofcn.trivivalfs.bucket.volumn;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.fofcn.trivialfs.bucket.constant.BucketConstant;
import org.junit.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Zk客户端测试
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/14 17:21
 */
public class ZookeeperClientTest {

    @BeforeClass
    public static void beforeClass() {

    }

    @AfterClass
    public static void afterClass() {

    }

    @Before
    public void doBefore() {

    }

    @After
    public void after() {

    }

    @Test
    public void testZNode() throws Exception {
        String hostPort = "127.0.0.1:2181";
        String znode = "/buckets";

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(hostPort, retryPolicy);
        zkClient.start();
        Stat rootStat = zkClient.checkExists().forPath(BucketConstant.ROOT_PATH);
        if (rootStat == null) {
            String str = zkClient.create().forPath(znode, znode.getBytes(StandardCharsets.UTF_8));
        }

    }

    @Test
    public void testReverseString() {
        String str = "12";
        char[] charArr = str.toCharArray();
        int n = charArr.length - 1;
        for (int i = n - 1 >> 1; i >= 0; i--) {
            int k = n - i;
            char t = charArr[k];
            charArr[k] = charArr[i];
            charArr[i] = t;
        }

        Assert.assertEquals("21", new String(charArr));
    }

    @Test
    public void testQuickSort() {
        Integer[] array = {7, 1, 4, 6, 3, 10, 50, 20};
//        Arrays.sort(array);

        Arrays.sort(array, (o1, o2) -> o2 - o1);
        Assert.assertEquals((Integer)50, array[0]);
    }

    @Test
    public void testMergeSortedArray() {

    }

    @Test
    public void testStartTimer() {

    }

    @Test
    public void testCube() {
        Integer[] array = {7, 1, 4, 6, 3, 10, 50, 20};

        OptionalDouble average = Arrays.stream(array).map(o -> o * o * o).filter(o -> o > 100).mapToInt(o -> o).average();
        if (average.isPresent()) {
            System.out.println(average.getAsDouble());
        }
    }

    @Test
    public void testSort() {
        List<Test> testList = new ArrayList<>(10);
        testList.stream().sorted((o1, o2) -> {
            return 0;
        }).collect(Collectors.toList());
    }

    class Test {

    }
}
