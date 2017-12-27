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
 * 云代理IP：www.ip3366.net
 *
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
@Component(value = "ip3366DownLoader")
public class Ip3366DownLoader extends BaseDownLoader implements DownLoader {
    private final static Logger LOG = LoggerFactory.getLogger(Ip3366DownLoader.class);

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
                request.setCharSet("gbk");
                setRequestParam(request);
                int pageSize = 1;
                if (task.isSubPageCrawl()) {
                    pageSize = task.getSubPageSize() + 1;
                }
                List<String> proxyUrlList = new ArrayList<>(pageSize * 2);
                //代理url拼接
                for (int i = 1; i <= pageSize; i++) {
                    //国内高匿代理
                    proxyUrlList.add("http://www.ip3366.net/free/?stype=1&page=" + i);
                    //国内透明代理
                    proxyUrlList.add("http://www.ip3366.net/free/?stype=2&page=" + i);
                    //国外高匿代理
                    proxyUrlList.add("http://www.ip3366.net/free/?stype=3&page=" + i);
                    //国外普通代理
                    proxyUrlList.add("http://www.ip3366.net/free/?stype=4&page=" + i);
                }
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
