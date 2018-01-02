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
    private int resProxyCount;
    /**
     * 代理总数
     */
    private int totalProxyCount;
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

    public int getResProxyCount() {
        return resProxyCount;
    }

    public void setResProxyCount(int resProxyCount) {
        this.resProxyCount = resProxyCount;
    }

    public int getTotalProxyCount() {
        return totalProxyCount;
    }

    public void setTotalProxyCount(int totalProxyCount) {
        this.totalProxyCount = totalProxyCount;
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
