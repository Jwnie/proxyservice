package com.meow.proxy.extract.impl;

import com.alibaba.fastjson.JSONObject;
import com.meow.proxy.check.ProxyCheck;
import com.meow.proxy.entity.Proxy;
import com.meow.proxy.enums.CountryType;
import com.meow.proxy.enums.ProxyAnonymousType;
import com.meow.proxy.enums.ProxySite;
import com.meow.proxy.extract.Extractor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 *         date:2017/12/28
 *         email:jwnie@foxmail.com
 */
@Component("proxydbExtractor")
public class ProxydbExtractor implements Extractor {
    private final static Logger LOG = LoggerFactory.getLogger(ProxydbExtractor.class);
    private Map<String, String> countryMap = new HashMap<String, String>(100);

    @Override
    public List<Proxy> extract(String htmlContent) {
        List<Proxy> proxies = new ArrayList<Proxy>(100);
        Document document = Jsoup.parse(htmlContent);
        ProxyCheck proxyCheck = ProxyCheck.getInstance();
        if (document != null) {
            Elements elements = document.select("tbody tr");
            if (countryMap.size() <= 0) {
                reflectCountry(document, countryMap);
            }
            if (CollectionUtils.isNotEmpty(elements)) {
                for (Element element : elements) {
                    long beginTime = System.currentTimeMillis();
                    Element hostEle = element.select("td").first();
                    if (hostEle != null) {
                        String host[] = hostEle.text().split(":");
                        String ip = host[0];
                        int port = Integer.parseInt(host[1]);
                        boolean valid = proxyCheck.checkProxyBySocket(new HttpHost(ip, port), true);
                        if (valid) {
                            long end = System.currentTimeMillis();
                            Element protocolEle = hostEle.nextElementSibling();
                            Element coutryEle = protocolEle.nextElementSibling();
                            Element anonymousEle = coutryEle.nextElementSibling();

                            String country = coutryEle.text();
                            if (countryMap.get(country) != null) {
                                country = countryMap.get(country);
                            } else {
                                //页面结构可能发生修改
                                reflectCountry(document, countryMap);
                                if (countryMap.get(country) != null) {
                                    country = countryMap.get(country);
                                }
                            }


                            if (country.equals("CN")) {
                                country = CountryType.china.getCountryName();
                            }

                            Proxy proxy = new Proxy();
                            proxy.setCountry(country);
                            proxy.setIp(ip);
                            proxy.setPort(port);
                            proxy.setCheckStatus(1);
                            proxy.setAnonymousType(getAnonymousType(anonymousEle));
                            proxy.setProtocolType(protocolEle.text());
                            proxy.setSourceSite(ProxySite.proxydb.getProxySiteName());
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
                        LOG.error("proxydbExtractor can not extract anything..., please check.");
                    }
                }
            }
        }
        return proxies;
    }

    @Override
    public List<Proxy> extract(List<String> htmlContentList) {
        List<Proxy> proxies = new ArrayList<Proxy>(200);
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
     * @param element
     * @return
     */
    private String getAnonymousType(Element element) {
        String text = element.text();
        if (StringUtils.isNoneBlank(text)) {
            switch (text) {
                case "Elite":
                    return ProxyAnonymousType.elite.getAnonymousType();
                case "Transparent":
                    return ProxyAnonymousType.transparent.getAnonymousType();
                case "Anonymous":
                    return ProxyAnonymousType.anonymous.getAnonymousType();
                case "Distorting":
                    return ProxyAnonymousType.distorting.getAnonymousType();
                default:
                    LOG.error("Can not verify the anonymousType of proxy from proxydb>>>:" + text);
            }
        }
        return text;
    }

    private void reflectCountry(Document document, Map<String, String> countryMap) {
        Elements countryEles = document.select("span.select option[value]");
        if (CollectionUtils.isNotEmpty(countryEles)) {
            for (Element element : countryEles) {
                String countryKey = element.attr("value");
                if (StringUtils.isNoneBlank(countryKey)) {
                    String countryValue = element.text().replace(countryKey + " - ", "").replaceAll("\\(\\d+?\\)", "").replaceAll("\\s+", "");
                    countryMap.put(countryKey, countryValue);
                }
            }
        }
//        System.out.println("countryMap: " + JSONObject.toJSONString(countryMap));
    }
}
