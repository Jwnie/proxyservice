package com.meow.proxy.check;

import com.meow.proxy.deduplicate.SimpleBloomFilter;
import com.meow.proxy.request.HttpClientUtil;
import com.meow.proxy.request.Response;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
public class ProxyCheck {
    private final static Logger LOG = LoggerFactory.getLogger(ProxyCheck.class);
    private final static int CHECKPROXY_TIMEOUT = 60000;
    /**
     * 使用布隆过滤器进行去重
     */
    private SimpleBloomFilter simpleBloomFilter = SimpleBloomFilter.getInstance();

    public static ProxyCheck getInstance() {
        return ProxyCheckSingleton.PROXY_CHECK;
    }


    /**
     * 检测代理前检查之前是否已经爬取过
     *
     * @param proxy
     * @return
     */
    private boolean isHadCheck(HttpHost proxy) {
        String value = new StringBuilder().append(proxy.getHostName()).append(":").append(proxy.getPort()).toString();
        if (simpleBloomFilter.contains(value)) {
            LOG.info("已经检验过的代理: " + value);
            return true;
        }
        return false;
    }

    /**
     * 检测过的代理加入布隆过滤器
     *
     * @param proxy
     */
    private void addChecked(HttpHost proxy) {
        String value = new StringBuilder().append(proxy.getHostName()).append(":").append(proxy.getPort()).toString();
        simpleBloomFilter.add(value);
    }

    /**
     * @param proxy
     * @return
     */
    public boolean checkProxyBySocket(HttpHost proxy) {
        if (proxy == null || isHadCheck(proxy)) {
            return false;
        }
        Socket socket = null;
        try {
            //失败重试三次
            for (int i = 0; i < 3; i++) {
                try {
                    socket = new Socket();
                    InetSocketAddress endpointSocketAddr = new InetSocketAddress(proxy.getHostName(), proxy.getPort());
                    socket.connect(endpointSocketAddr, CHECKPROXY_TIMEOUT);
                    return true;
                } catch (Exception e) {
                    LOG.error("连接失败, remote: " + proxy.getHostName() + ":" + proxy.getPort());
                }finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            LOG.error("Socket关闭异常：", e);
                        }
                    }
                }
            }
            return false;
        } finally {
            addChecked(proxy);
        }
    }

    public boolean checkProxyByRequestBaidu(HttpHost proxy) {
        if (proxy == null || isHadCheck(proxy)) {
            return false;
        }
        String url = "https://www.baidu.com/";
        CloseableHttpClient closeableHttpClient = null;
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
        try {
            closeableHttpClient = httpClientUtil.createHttpClient(CHECKPROXY_TIMEOUT, proxy, null);
            Response response = httpClientUtil.getResponse(closeableHttpClient, url);
            if (response != null) {
                System.out.println(response.getStatusCode());
//                System.out.println(response.getContent());
                if (response.getContent().contains("百度一下，你就知道")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            LOG.error("验证代理请求出错：", e);
            return false;
        } finally {
            addChecked(proxy);
            //使用连接池无需关闭
            //httpClientUtil.closeResources(null,closeableHttpClient);
        }
    }


    private static class ProxyCheckSingleton {
        private final static ProxyCheck PROXY_CHECK = new ProxyCheck();
    }

}
