package com.meow.proxy.download.impl;

import com.meow.proxy.download.BaseDownLoader;
import com.meow.proxy.download.DownLoader;
import com.meow.proxy.entity.Task;
import com.meow.proxy.request.HttpClientUtil;
import com.meow.proxy.request.Request;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * free-proxy-list.net 境外代理
 *
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
@Component(value = "freeProxyListDownLoader")
public class FreeProxyListDownLoader extends BaseDownLoader implements DownLoader {
    private final static Logger LOG = LoggerFactory.getLogger(FreeProxyListDownLoader.class);

    /**
     * 包括翻页下载，返回List<String>
     *
     * @param task
     * @return
     */
    @Override
    public List<String> downLoad(Task task) {
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
        CloseableHttpClient closeableHttpClient = null;
        List<String> htmlContentList = new ArrayList<String>(20);
        try {
            closeableHttpClient = HttpClientUtil.getInstance().createHttpClient();
            if (task != null) {
                String origUrl = task.getUrl();
                Request request = new Request();
                request.setCharSet("utf-8");
//                request.setHeader("Host", "free-proxy-list.net");
//                request.setHeader("Upgrade-Insecure-Requests", "1");
                setRequestParam(request);
                //重新設置編碼(去掉br)
                request.setHeader("Accept-Encoding", "gzip, deflate");
                int pageSize = 1;
                if (task.isSubPageCrawl()) {
                    pageSize = task.getSubPageSize() + 1;
                }
                List<String> proxyUrlList = new ArrayList<>(pageSize * 2);
                //代理url拼接
                //uk代理
//                proxyUrlList.add("https://free-proxy-list.net/uk-proxy.html");
                //匿名代理
//                proxyUrlList.add("https://free-proxy-list.net/anonymous-proxy.html");
                //us代理
                proxyUrlList.add("https://www.us-proxy.org/");
                //socs代理
                proxyUrlList.add("https://www.socks-proxy.net/");
                if (CollectionUtils.isNotEmpty(proxyUrlList)) {
                    htmlContentList.addAll(downLoad(httpClientUtil, closeableHttpClient, request, proxyUrlList));
                }
            }
        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            //使用连接池无需关闭
            //httpClientUtil.closeResources(null, closeableHttpClient);
        }

        return htmlContentList;
    }

}
