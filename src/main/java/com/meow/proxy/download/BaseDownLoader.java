package com.meow.proxy.download;

import com.meow.proxy.base.Const;
import com.meow.proxy.entity.Task;
import com.meow.proxy.request.HttpClientUtil;
import com.meow.proxy.request.Request;
import com.meow.proxy.request.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
@Component(value="baseDownLoader")
public abstract class BaseDownLoader implements DownLoader {
    private final static Logger LOG = LoggerFactory.getLogger(BaseDownLoader.class);
    
    @Autowired
    protected WebDriverFactory webDriverFactory;
    
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
            if (task != null) {
                closeableHttpClient = HttpClientUtil.getInstance().createHttpClient();
                Request request = new Request();
                setRequestParam(request);
                String origUrl = task.getUrl();
                String htmlContent = downLoad(httpClientUtil, closeableHttpClient, request, origUrl);
                if (StringUtils.isNotBlank(htmlContent)) {
                    htmlContentList.add(htmlContent);
                }
            }
        } catch (Exception e) {
            LOG.error("下载异常，任务url:" + task.getUrl(), e);
        } finally {
            //使用连接池无需关闭
//            httpClientUtil.closeResources(null, closeableHttpClient);
        }
        return htmlContentList;
    }

    /**
     * 设置请求参数
     *
     * @param request
     */
    protected void setRequestParam(Request request) {
        if (request == null) {
            return;
        }
        //默认为httpGet请求(子类Post请求需要覆写此方法)
        request.setMethod(Const.METHOD_HTTPGET);
        request.setCharSet("utf-8");
        request.setHeader("User-Agent", Const.USER_AGENT[new Random().nextInt(Const.USER_AGENT.length)]);
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Accept", "*/*");
        request.setHeader("Accept-Encoding", "gzip, deflate, br");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
    }

    /**
     * @param httpClientUtil
     * @param closeableHttpClient
     * @param request
     * @param url
     * @return
     */
    protected String downLoad(HttpClientUtil httpClientUtil, CloseableHttpClient closeableHttpClient, Request request, String url) {
        String htmlContent = null;
        Response response = httpClientUtil.getResponse(closeableHttpClient, request, url);
        if (response != null) {
            htmlContent = response.getContent();
        }
        return htmlContent;
    }

    /**
     * @param httpClientUtil
     * @param closeableHttpClient
     * @param request
     * @param urlList
     * @return
     */
    protected List<String> downLoad(HttpClientUtil httpClientUtil, CloseableHttpClient closeableHttpClient, Request request, List<String> urlList) {
        List<String> htmlContentList = new ArrayList<String>(50);
        if (CollectionUtils.isEmpty(urlList)) {
            return htmlContentList;
        }
        for (String url : urlList) {
            String htmlContent = downLoad(httpClientUtil, closeableHttpClient, request, url);
            if (StringUtils.isNotBlank(htmlContent)) {
                htmlContentList.add(htmlContent);
            }
        }
        return htmlContentList;
    }
    
    protected String downLoad(WebDriver webDriver, String url)
    {
        String htmlContent = null;
        try {
            for (int i = 0; i < 3; i++)
                try {
                    webDriver.get(url);
                    WebElement webElement = webDriver.findElement(By.xpath("/html"));
                    htmlContent = webElement.getAttribute("outerHTML");
                }
                catch (Exception e) {
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e1) {
                        LOG.error("", e);
                    }
                }
        }
        catch (Exception e) {
            LOG.error("下载失败", e);
        }
        return htmlContent;
    }
    
    protected List<String> downLoad(List<String> urlList) {
        List htmlContentList = new ArrayList(50);
        if (CollectionUtils.isEmpty(urlList)) {
            return htmlContentList;
        }
        WebDriver webDriver = null;
        try {
            webDriver = this.webDriverFactory.getWebDriver();
            for (String url : urlList) {
                String htmlContent = downLoad(webDriver, url);
                if (StringUtils.isNotBlank(htmlContent))
                    htmlContentList.add(htmlContent);
            }
        }
        catch (Exception e) {
            LOG.error("下载异常:", e);
        } finally {
            closeResource(webDriver);
        }
        return htmlContentList;
    }
    
    protected void closeResource(WebDriver webDriver)
    {
        if (webDriver != null)
            webDriver.close();
    }
}
