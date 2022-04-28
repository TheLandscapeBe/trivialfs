package org.fofcn.trivialfs.bucket;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.ObjectUtils;
import org.fofcn.trivialfs.bucket.config.BucketConfig;
import org.fofcn.trivialfs.common.YamlUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Main方法
 *
 * @author errorfatal89@gmail.com
 * @datetime 2022/04/11 16:46
 */
@Slf4j
public class BucketControllerMain {

    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("miss start up arguments: configFile");
            return;
        }

        File confFile = new File(args[0]);
        if (!confFile.exists()) {
            log.error("start up config not found: <{}>", args[0]);
            return;
        }

        InputStream in;
        try {
            in = new FileInputStream(confFile);
        } catch (FileNotFoundException e) {
            log.error("start up config open failed: <{}>", args[0]);
            return;
        }

        BucketConfig bucketConfig = YamlUtil.readObject(in, BucketConfig.class, "trivialfs");
        if (ObjectUtils.isEmpty(bucketConfig)) {
            log.error("config file parse error,please check config file.");
            return;
        }

        BucketController controller = new BucketController(bucketConfig);
        controller.init();
        controller.start();

        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> controller.shutdown(),
                "bucket-shutdown-hook")
        );
    }
}
