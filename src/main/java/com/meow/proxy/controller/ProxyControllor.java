package com.meow.proxy.controller;

import com.alibaba.fastjson.JSONArray;
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
import java.util.Map;

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
                int totalCount = proxyService.queryValidProxyCount(protocolType,isDemostic,anonymousType);
                proxyQueryResult.setTotalProxyCount(totalCount);
                proxyQueryResult.setProxies(proxies);
                proxyQueryResult.setResProxyCount(proxies.size());
            }
            proxyQueryResult.setStatus("success");
        } catch (Exception e) {
            LOG.error("查询代理异常：", e);
            proxyQueryResult.setProxies(proxies);
            proxyQueryResult.setResProxyCount(proxies.size());
            proxyQueryResult.setStatus("failed");
        }
        return proxyQueryResult;
    }

    @RequestMapping(value = "proxyStatistic", method = RequestMethod.GET)
    public JSONArray proxyStatistic() {
        JSONArray js = new JSONArray();
        try {
            List<Map<String, String>> list = proxyService.proxyStatisticBySite();
            if (CollectionUtils.isNotEmpty(list)) {
                js.addAll(list);
            }
        } catch (Exception e) {
            LOG.error("统计代理异常：",e);
        }
        return js;
    }


}
