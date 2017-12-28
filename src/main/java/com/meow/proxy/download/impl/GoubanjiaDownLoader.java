package com.meow.proxy.download.impl;

import com.meow.proxy.download.BaseDownLoader;
import com.meow.proxy.download.DownLoader;
import com.meow.proxy.entity.Task;
import com.meow.proxy.request.HttpClientUtil;
import com.meow.proxy.request.Request;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 全网代理IP http://www.goubanjia.com
 *
 * @author Alex
 *         date:2017/12/22
 *         email:jwnie@foxmail.com
 */
@Component(value = "goubanjiaDownLoader")
public class GoubanjiaDownLoader extends BaseDownLoader implements DownLoader {
    private final static Logger LOG = LoggerFactory.getLogger(GoubanjiaDownLoader.class);

    /**
     * 包括翻页下载，返回List<String>
     *
     * @param task
     * @return
     */
    @Override
    public List<String> downLoad(Task task) {
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
                    proxyUrlList.add("http://www.goubanjia.com/free/gngn/index" + i + ".shtml");
                    //国内透明代理
                    proxyUrlList.add("http://www.goubanjia.com/free/gnpt/index" + i + ".shtml");
                    //HTTPS代理
                    proxyUrlList.add("http://www.goubanjia.com/free/gwgn/index" + i + ".shtml");
                    //HTTP代理
                    proxyUrlList.add("http://www.goubanjia.com/free/gwpt/index" + i + ".shtml");
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
