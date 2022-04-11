package org.fofcn.trivialfs.bucket;

/**
 * Main方法
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 16:46
 */
public class BucketControllerMain {

    public static void main(String[] args) {
        BucketController controller = new BucketController();
        controller.init();
        controller.start();

        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> controller.shutdown(),
                "bucket-shutdown-hook")
        );
    }
}
