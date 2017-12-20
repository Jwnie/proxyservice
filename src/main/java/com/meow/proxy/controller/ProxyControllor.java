package com.meow.proxy.controller;

import com.meow.proxy.entity.Proxy;
import com.meow.proxy.entity.ProxyQueryResult;
import com.meow.proxy.service.ProxyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 *         date:2017/12/20
 *         email:jwnie@foxmail.com
 */
@RestController
@RequestMapping(value = "/proxy")
public class ProxyControllor {
    private final static Logger LOG = LoggerFactory.getLogger(ProxyControllor.class);

    @Autowired
    ProxyService proxyService;

    @RequestMapping(value = "/getProxy", method = RequestMethod.GET)
    public ProxyQueryResult getProxy(@Param("protocolType") String protocolType, @Param("isDemostic") String isDemostic, @Param("anonymousType") String anonymousType) {
        ProxyQueryResult proxyQueryResult = new ProxyQueryResult();
        List<Proxy> proxies = new ArrayList<Proxy>();
        try {
            proxies = proxyService.queryProxy(protocolType, isDemostic, anonymousType);
            if (CollectionUtils.isNotEmpty(proxies)) {
                proxyQueryResult.setProxies(proxies);
                proxyQueryResult.setProxyCount(proxies.size());
            }
            proxyQueryResult.setStatus("success");
        } catch (Exception e) {
            LOG.error("查询代理出错", e);
            proxyQueryResult.setProxies(proxies);
            proxyQueryResult.setProxyCount(proxies.size());
            proxyQueryResult.setStatus("failed");
        }
        return proxyQueryResult;
    }



    //@RequestMapping(value = "/getSpecificProxy", method = RequestMethod.GET)
    public ProxyQueryResult getSpecificProxy(@Param("protocolType") String protocolType, @Param("isDemostic") int isDemostic, @Param("anonymousType") String anonymousType) {
        ProxyQueryResult proxyQueryResult = new ProxyQueryResult();
        List<Proxy> proxies = null;
        if (isDemostic > 0) {
            proxies = proxyService.queryDomesticProxies(anonymousType, protocolType);
        } else {
            proxies = proxyService.queryForeignProxies(anonymousType, protocolType);
        }
        if (CollectionUtils.isNotEmpty(proxies)) {
            proxyQueryResult.setProxies(proxies);
            proxyQueryResult.setProxyCount(proxies.size());
            proxyQueryResult.setStatus("success");
        }
        return proxyQueryResult;
    }


}
