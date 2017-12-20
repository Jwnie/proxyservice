package com.meow.proxy.check;

import com.meow.proxy.entity.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Alex
 *         date:2017/12/19
 *         email:jwnie@foxmail.com
 */
@Component
public class ProxyRecheckSender implements ProxyRecheckCallBack {
    private final static Logger LOG = LoggerFactory.getLogger(ProxyRecheckSender.class);

    @Autowired
    ProxyRecheckHandler proxyRecheckHandler;

    public void sendRecheckProxies(List<Proxy> proxyList){
        proxyRecheckHandler.handleMessage(this,proxyList);
    }


    @Override
    public void process(String handleStatus) {
        LOG.info("代理重新检测状态："+handleStatus);
    }
}
