package com.meow.proxy.extract.impl;

import com.meow.proxy.check.ProxyCheck;
import com.meow.proxy.check.ProxyIp2Addr;
import com.meow.proxy.entity.IPAddr;
import com.meow.proxy.entity.Proxy;
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
import java.util.List;

/**
 * @author Alex
 *         date:2017/12/27
 *         email:jwnie@foxmail.com
 */
@Component(value = "data5uExtractor")
public class Data5uExtractor implements Extractor {
    private final static Logger LOG = LoggerFactory.getLogger(Data5uExtractor.class);

    @Override
    public List<Proxy> extract(String htmlContent) {
        List<Proxy> proxies = new ArrayList<Proxy>(100);
        Document document = Jsoup.parse(htmlContent);
        ProxyCheck proxyCheck = ProxyCheck.getInstance();
        if (document != null) {
            Elements elements = document.select("ul.l2");
            if (CollectionUtils.isNotEmpty(elements)) {
                for (Element element : elements) {
                    long beginTime = System.currentTimeMillis();
                    Element ipEle = element.select("span").first();
                    if (ipEle != null) {
                        Element portEle = ipEle.nextElementSibling();
                        String ip = ipEle.text();
                        int port = Integer.parseInt(portEle.text());
                        boolean valid = proxyCheck.checkProxyBySocket(new HttpHost(ip, port), true);
                        if (valid) {
                            long end = System.currentTimeMillis();
                            Element anonymousEle = portEle.nextElementSibling();
                            Element protocolEle = anonymousEle.nextElementSibling();
                            Element coutryEle = protocolEle.nextElementSibling();
                            Element areaEle = coutryEle.nextElementSibling();
                            String area = areaEle.text();
                            String country = coutryEle.text();

                            if ("香港".equals(country) || "澳门".equals(country) || "台湾".equals(country)) {
                                country = "中国 " + country;
                            }


                            if (StringUtils.isEmpty(country)) {
                                IPAddr ipAddr = ProxyIp2Addr.getInstance().getIPAddrBYTaobaoAPI(ip);
                                country = ipAddr.getCountry();
                                StringBuilder sb = new StringBuilder();
                                sb.append(ipAddr.getProvince()).append(" ").append(ipAddr.getCity());
                                area = sb.toString();
                            }

                            Proxy proxy = new Proxy();
                            proxy.setCountry(country);
                            proxy.setIp(ip);
                            proxy.setPort(port);
                            proxy.setArea(area);
                            proxy.setCheckStatus(1);
                            proxy.setAnonymousType(getAnonymousType(anonymousEle));
                            proxy.setProtocolType(protocolEle.text());
                            proxy.setSourceSite(ProxySite.data5u.getProxySiteName());
                            proxy.setCheckTime(beginTime);
                            proxy.setCrawlTime(beginTime);
                            proxy.setValidTime(1);
                            //默认值
                            proxy.setLastSurviveTime(-1L);
                            //默认值
                            proxy.setInvalidTime(-1L);
                            proxy.setValid(true);
                            proxy.setResponseTime(end - beginTime);
                            LOG.info("Valid proxy:" + proxy.toString());
                            proxies.add(proxy);
                        }
                    } else {
                        LOG.error("data5uExtractor can not extract anything..., please check.");
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
                case "高匿":
                    return ProxyAnonymousType.elite.getAnonymousType();
                case "匿名":
                    return ProxyAnonymousType.anonymous.getAnonymousType();
                case "透明":
                    return ProxyAnonymousType.transparent.getAnonymousType();
                default:
                    LOG.error("Can not verify the anonymousType of proxy from data5u>>>:" + text);
            }
        }
        return text;
    }
}
