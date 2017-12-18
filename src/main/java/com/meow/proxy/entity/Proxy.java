package com.meow.proxy.entity;

import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.beans.IDProperty;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *         date:2017/12/13
 *         email:jwnie@foxmail.com
 */
@Component
public class Proxy {
    private Integer id;
    private String ip;
    private int port;
    /**
     * 代理匿名类型
     */
    private String anonymousType;
    /**
     * 代理协议类型
     */
    private String protocolType;
    /**
     * 代理所在国家
     */
    private String country;
    /**
     * 代理所在地区
     */
    private String area;
    /**
     * 是否有效
     */
    private boolean valid;
    /**
     * 代理失效时间(时间戳)
     */
    private Long invalidTime;
    /**
     * 上次存活时长
     */
    private Long lastSurviveTime;
    /**
     * 代理验证时间
     */
    private Long checkTime;
    /**
     * 代理验证状态(0:未验证；1:已验证)
     */
    private Integer checkStatus;
    /**
     * 代理评分
     */
    private float score;
    /**
     * 代理来源站点
     */
    private String sourceSite;
    /**
     * 代理有效次数
     */
    private Integer validTime;
    /**
     * 代理采集时间
     */
    private Long crawlTime;
    /**
     * 代理响应时间
     */
    private Long responseTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAnonymousType() {
        return anonymousType;
    }

    public void setAnonymousType(String anonymousType) {
        this.anonymousType = anonymousType;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Long invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Long getLastSurviveTime() {
        return lastSurviveTime;
    }

    public void setLastSurviveTime(Long lastSurviveTime) {
        this.lastSurviveTime = lastSurviveTime;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getSourceSite() {
        return sourceSite;
    }

    public void setSourceSite(String sourceSite) {
        this.sourceSite = sourceSite;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

    public Long getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Long crawlTime) {
        this.crawlTime = crawlTime;
    }
    
    public Long getResponseTime() {
        return responseTime;
    }
    
    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }
    
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
