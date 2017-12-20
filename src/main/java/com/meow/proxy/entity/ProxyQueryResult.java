package com.meow.proxy.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 *         date:2017/12/20
 *         email:jwnie@foxmail.com
 */
public class ProxyQueryResult implements Serializable {
    /**
     * 请求状态(success/failed)
     */
    private String status = "failed";
    /**
     * 返回的代理数量
     */
    private int proxyCount;
    /**
     * 返回的代理详情
     */
    private List<Proxy> proxies = new ArrayList<Proxy>(200);


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProxyCount() {
        return proxyCount;
    }

    public void setProxyCount(int proxyCount) {
        this.proxyCount = proxyCount;
    }

    public List<Proxy> getProxies() {
        return proxies;
    }

    public void setProxies(List<Proxy> proxies) {
        this.proxies = proxies;
    }

    @Override
    public String toString() {
        return JSONObject.toJSON(this).toString();
    }
}
