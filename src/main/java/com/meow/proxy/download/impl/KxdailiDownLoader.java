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
 * 开心代理：http://www.kxdaili.com/dailiip.html
 *
 * @author Alex
 *         date:2017/12/28
 *         email:jwnie@foxmail.com
 */
@Component(value = "kxdailiDownLoader")
public class KxdailiDownLoader extends BaseDownLoader implements DownLoader {
    private final static Logger LOG = LoggerFactory.getLogger(KxdailiDownLoader.class);

    /**
     * 包括翻页下载，返回List<String>
     *
     * @param task
     * @return
     */
    @Override
    public List<String> downLoad(Task task) {
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
        List<String> htmlContentList = new ArrayList<String>(20);
        try {
            if (task != null) {
                String origUrl = task.getUrl();
                Request request = new Request();
                setRequestParam(request);
                int pageSize = 1;
                if (task.isSubPageCrawl()) {
                    pageSize = task.getSubPageSize() + 1;
                }
                List<String> proxyUrlList = new ArrayList<>(pageSize * 2);
                //代理url拼接
                for (int i = 1; i <= pageSize; i++) {
                    //国内高匿代理
                    proxyUrlList.add("http://www.kxdaili.com/dailiip/1/" + i + ".html");
                    //国内普匿代理
                    proxyUrlList.add("http://www.kxdaili.com/dailiip/1/" + i + ".html");
                }
                if (CollectionUtils.isNotEmpty(proxyUrlList)) {
                    htmlContentList.addAll(downLoad(proxyUrlList));
                }
            }
        } catch (Exception e) {
            LOG.warn("下载异常，任务url:" + task.getUrl(), e);
        }
        return htmlContentList;
    }

}
