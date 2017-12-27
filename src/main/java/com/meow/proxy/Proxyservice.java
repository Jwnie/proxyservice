package com.meow.proxy;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author alex
 */
@MapperScan("com.meow.proxy.dao")
@EnableScheduling
@SpringBootApplication
public class Proxyservice {
    private final static Logger LOG = LoggerFactory.getLogger(Proxyservice.class);

    public static void main(String[] args) {
        LOG.info("Proxyservice start >>>>>>");
        SpringApplication.run(Proxyservice.class, args);
    }
}
