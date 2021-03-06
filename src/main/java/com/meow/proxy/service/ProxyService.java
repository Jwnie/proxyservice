package com.meow.proxy.service;

import com.meow.proxy.entity.Proxy;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Alex
 *         date:2017/12/18
 *         email:jwnie@foxmail.com
 */
public interface ProxyService {
    void saveProxies(List<Proxy> proxyList);

    void updateProxies(List<Proxy> proxyList);

    List<Proxy> queryValidProxies();

    /**
     * 默认查询前一百条有效的代理
     * @return
     */
    List<Proxy> queryProxy(String protocolType, String isDemostic,String anonymousType);

    List<Map<String,String>> proxyStatisticBySite();

    int queryValidProxyCount(String protocolType, String isDemostic,String anonymousType);
}
