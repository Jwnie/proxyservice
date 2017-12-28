package com.meow.proxy.extract.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meow.proxy.check.ProxyCheck;
import com.meow.proxy.check.ProxyIp2Addr;
import com.meow.proxy.entity.IPAddr;
import com.meow.proxy.entity.Proxy;
import com.meow.proxy.enums.CountryType;
import com.meow.proxy.enums.ProxyAnonymousType;
import com.meow.proxy.enums.ProxySite;
import com.meow.proxy.extract.Extractor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
@Component("xdailiExtractor")
public class XdailiExtractor implements Extractor {
    private final static Logger LOG = LoggerFactory.getLogger(XdailiExtractor.class);

    @Override
    public List<Proxy> extract(String htmlContent) {
        List<Proxy> proxies = new ArrayList<Proxy>(100);
        JSONObject jsonObject = JSONObject.parseObject(htmlContent);
        ProxyCheck proxyCheck = ProxyCheck.getInstance();
        if (jsonObject != null) {
            JSONObject result = jsonObject.getJSONObject("RESULT");
            if (result != null) {
                JSONArray jsonArray = result.getJSONArray("rows");
                if (CollectionUtils.isNotEmpty(jsonArray)) {
                    for (Object o : jsonArray) {
                        JSONObject json = (JSONObject) o;
                        long beginTime = System.currentTimeMillis();
                        if (json != null) {
                            String ip = json.getString("ip");
                            int port = Integer.parseInt(json.getString("port"));
                            boolean valid = proxyCheck.checkProxyBySocket(new HttpHost(ip, port), true);
                            if (valid) {
                                long end = System.currentTimeMillis();
                                String area = json.getString("position");
                                String coutry = "";
                                if (area.contains("中国")) {
                                    coutry = "china";
                                } else {
                                    IPAddr ipAddr = ProxyIp2Addr.getInstance().getIPAddrBYTaobaoAPI(ip);
                                    coutry = ipAddr.getCountry();
                                }
                                String anonymous = json.getString("anony");
                                String protocol = json.getString("type");
                                if (protocol.contains("HTTP/HTTPS")) {
                                    protocol = "http";
                                }

                                Proxy proxy = new Proxy();
                                proxy.setCountry(CountryType.china.getCountryName());
                                proxy.setIp(ip);
                                proxy.setPort(port);
                                proxy.setArea(area);
                                proxy.setCheckStatus(1);
                                proxy.setAnonymousType(getAnonymousType(anonymous));
                                proxy.setProtocolType(protocol);
                                proxy.setSourceSite(ProxySite.xdaili.getProxySiteName());
                                proxy.setCheckTime(beginTime);
                                proxy.setCrawlTime(beginTime);
                                proxy.setValidTime(1);
                                proxy.setLastSurviveTime(-1L);
                                proxy.setInvalidTime(-1L);
                                proxy.setValid(true);
                                proxy.setResponseTime(end - beginTime);
                                LOG.info("Valid proxy:" + proxy.toString());
                                proxies.add(proxy);
                            }
                        } else {
                            LOG.error("XdailiExtractor can not extract anything..., please check.");
                        }
                    }
                }
            }
        }
        return proxies;
    }

    @Override
    public List<Proxy> extract(List<String> htmlContentList) {
        List<Proxy> proxies = new ArrayList<Proxy>(20);
        if (CollectionUtils.isNotEmpty(htmlContentList)) {
            for (String htmlContent : htmlContentList) {
                proxies.addAll(extract(htmlContent));
            }
        }
        return proxies;
    }

    /**
     * 代理匿名类型清洗
     *
     * @param text
     * @return
     */
    private String getAnonymousType(String text) {
        if (StringUtils.isNoneBlank(text)) {
            switch (text) {
                case "高匿":
                    return ProxyAnonymousType.elite.getAnonymousType();
                case "透明":
                    return ProxyAnonymousType.transparent.getAnonymousType();
                default:
                    LOG.error("Can not verify the anonymousType of proxy from Xdaili>>>:" + text);
            }
        }
        return text;
    }
}
