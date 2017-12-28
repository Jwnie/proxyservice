package com.meow.proxy.extract.impl;

import com.meow.proxy.check.ProxyCheck;
import com.meow.proxy.entity.Proxy;
import com.meow.proxy.enums.ProxyAnonymousType;
import com.meow.proxy.enums.ProxyProtocolType;
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
import java.util.List;

/**
 * @author Alex
 *         date:2017/12/28
 *         email:jwnie@foxmail.com
 */
@Component("coderbusyExtractor")
public class CoderbusyExtractor implements Extractor {
    private final static Logger LOG = LoggerFactory.getLogger(CoderbusyExtractor.class);

    @Override
    public List<Proxy> extract(String htmlContent) {
        List<Proxy> proxies = new ArrayList<Proxy>(100);
        Document document = Jsoup.parse(htmlContent);
        ProxyCheck proxyCheck = ProxyCheck.getInstance();
        if (document != null) {
            Elements elements = document.select("tbody tr");
            if (CollectionUtils.isNotEmpty(elements)) {
                for (Element element : elements) {
                    long beginTime = System.currentTimeMillis();
                    Element ipEle = element.select("td").first();
                    if (ipEle != null) {
                        Element portELe = ipEle.nextElementSibling();
                        String ip = ipEle.text();
                        int port = Integer.parseInt(portELe.text());
                        boolean valid = proxyCheck.checkProxyBySocket(new HttpHost(ip, port), true);
                        if (valid) {
                            long end = System.currentTimeMillis();
                            Element countryEle = portELe.nextElementSibling();
                            Element protocolEle = countryEle.nextElementSibling().nextElementSibling();
                            Element anonymousEle = protocolEle.nextElementSibling().nextElementSibling();
                            String isHttps = anonymousEle.nextElementSibling().text();
                            String protocol = protocolEle.text();
                            if (StringUtils.isNotEmpty(isHttps) && "check".equals(isHttps)) {
                                protocol = ProxyProtocolType.https.getRequestType();
                            }

                            Proxy proxy = new Proxy();
                            proxy.setCountry(countryEle.text());
                            proxy.setIp(ip);
                            proxy.setPort(port);
                            proxy.setCheckStatus(1);
                            proxy.setAnonymousType(getAnonymousType(anonymousEle));
                            proxy.setProtocolType(protocol);
                            proxy.setSourceSite(ProxySite.coderbusy.getProxySiteName());
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
                        LOG.error("coderbusyExtractor can not extract anything..., please check.");
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
                case "高匿名":
                    return ProxyAnonymousType.elite.getAnonymousType();
                case "透明":
                    return ProxyAnonymousType.transparent.getAnonymousType();
                case "匿名":
                    return ProxyAnonymousType.anonymous.getAnonymousType();
                default:
                    LOG.error("Can not verify the anonymousType of proxy from ip3366>>>:" + text);
            }
        }
        return text;
    }
}
