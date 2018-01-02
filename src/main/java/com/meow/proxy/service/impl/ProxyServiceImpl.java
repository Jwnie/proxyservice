package com.meow.proxy.service.impl;

import com.meow.proxy.dao.ProxyDao;
import com.meow.proxy.entity.Proxy;
import com.meow.proxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Alex
 *         date:2017/12/18
 *         email:jwnie@foxmail.com
 */
@Service
public class ProxyServiceImpl implements ProxyService {
    @Autowired
    ProxyDao proxyDao;

    @Override
    public void saveProxies(List<Proxy> proxyList) {
        proxyDao.saveProxies(proxyList);
    }

    @Override
    public void updateProxies(List<Proxy> proxyList) {
        proxyDao.updateProxies(proxyList);
    }

    @Override
    public List<Proxy> queryValidProxies() {
        return proxyDao.queryValidProxies();
    }

    /**
     * 查询前一百条有效的代理
     *
     * @return
     */
    @Override
    public List<Proxy> queryProxy(String protocolType, String isDemostic, String anonymousType) {
        return proxyDao.queryProxy(protocolType, isDemostic, anonymousType);
    }

    public List<Map<String, String>> proxyStatisticBySite() {
        return proxyDao.proxyStatisticBySite();
    }

    public int queryValidProxyCount(String protocolType, String isDemostic, String anonymousType) {
        return proxyDao.queryValidProxyCount(protocolType, isDemostic, anonymousType);
    }

}
