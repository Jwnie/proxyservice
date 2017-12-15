package com.meow.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author alex
 */
@SpringBootApplication
public class ProxyserviceApplication {
	private final static Logger LOG = LoggerFactory.getLogger(ProxyserviceApplication.class);
	public static void main(String[] args) {
        LOG.info("Proxyservice start >>>>>>");
        SpringApplication.run(ProxyserviceApplication.class, args);
	}
}
