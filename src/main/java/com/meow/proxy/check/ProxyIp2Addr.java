package com.meow.proxy.check;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meow.proxy.entity.IPAddr;
import com.meow.proxy.request.HttpClientUtil;
import com.meow.proxy.request.Response;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alex
 *         date:2017/12/22
 *         email:jwnie@foxmail.com
 */
public class ProxyIp2Addr {
    private final static Logger LOG = LoggerFactory.getLogger(ProxyIp2Addr.class);
    private static ProxyIp2Addr ourInstance = new ProxyIp2Addr();
    private static String GETIPINFO_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    public static ProxyIp2Addr getInstance() {
        return ourInstance;
    }

    private ProxyIp2Addr() {
    }

    public IPAddr getIPAddrBYTaobaoAPI(String ip) {
        IPAddr ipAddr = new IPAddr();
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
        CloseableHttpClient closeableHttpClient = null;
        String url = new StringBuilder().append(GETIPINFO_URL).append(ip).toString();
        try {
            closeableHttpClient = httpClientUtil.createHttpClient();
            Response response = httpClientUtil.getResponse(closeableHttpClient, url);
            if (response != null) {
                JSONObject jsonObject = JSON.parseObject(response.getContent());
                if (jsonObject != null) {
                    ipAddr.setCountry(jsonObject.getString("country"));
                    ipAddr.setProvince(jsonObject.getString("region"));
                    ipAddr.setCity(jsonObject.getString("city"));
                    ipAddr.setIsp(jsonObject.getString("isp"));
                }
            }
        } catch (Exception e) {
            LOG.warn("验证代理请求出错：", e);
        }
        return ipAddr;
    }

}
