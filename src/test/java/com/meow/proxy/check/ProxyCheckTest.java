package com.meow.proxy.check;

import org.apache.http.HttpHost;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
public class ProxyCheckTest{
    ProxyCheck proxyCheck = null;
    @Before
    public void init(){
        proxyCheck = ProxyCheck.getInstance();
    }

    @Test
    public void checkProxy(){
//        HttpHost httpHost = new HttpHost("113.218.191.170",8888);
//        HttpHost httpHost = new HttpHost("223.241.119.16",8180);
//        HttpHost httpHost = new HttpHost("121.31.103.33",6666);
//        HttpHost httpHost = new HttpHost("139.59.169.81",8118);
        HttpHost httpHost = new HttpHost("191.252.111.249",3128);
//        System.out.println(proxyCheck.checkProxyBySocket(httpHost,false));
        System.out.println(proxyCheck.checkProxyByRequestBaidu(httpHost,false));
    }
}
