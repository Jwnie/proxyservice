package com.meow.proxy.extract.impl;

import com.meow.proxy.entity.Proxy;
import com.meow.proxy.extract.Extractor;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
@Component
public class XicidailiExtractor implements Extractor {
    @Override
    public List<Proxy> extract(String htmlContent) {
        List<Proxy> proxies = new ArrayList<Proxy>(100);
        Document document = Jsoup.parse(htmlContent);
        if(document != null){
            Elements elements = document.select("tr.odd");
            if(CollectionUtils.isNotEmpty(elements)){
                for(Element element : elements){
//                    String host =

                }
            }
        }
        return proxies;
    }

    @Override
    public List<Proxy> extract(List<String> htmlContentList) {
        return null;
    }
}
