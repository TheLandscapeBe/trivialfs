package org.fofcn.trivialfs.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author errorfatal89@gmail.com
 */
@MapperScan("org.fofcn.trivialfs.web.**.mapper")
@SpringBootApplication
public class TrivialFsWebSpringBootMain {

    public static void main(String[] args) {
        SpringApplication.run(TrivialFsWebSpringBootMain.class, args);
    }
}
