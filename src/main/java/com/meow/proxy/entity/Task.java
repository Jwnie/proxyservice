package com.meow.proxy.entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 任务类
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
public class Task {
    /**
     * 任務url
     */
    private String url;
    /**
     * 是否翻頁採集
     */
    private boolean subPageCrawl;
    /**
     * 翻頁數量
     */
    private int subPageSize;
    /**
     * 下載類class路徑
     */
    private String downLoadClassName;
    /**
     * 抽取类class路径
     */
    private String extractClassName;
    /**
     * 站点名称
     */
    private String siteName;
    
    public String getUrl()
    {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public boolean isSubPageCrawl() {
        return this.subPageCrawl;
    }
    
    public void setSubPageCrawl(boolean subPageCrawl) {
        this.subPageCrawl = subPageCrawl;
    }
    
    public int getSubPageSize() {
        return this.subPageSize;
    }
    
    public void setSubPageSize(int subPageSize) {
        this.subPageSize = subPageSize;
    }
    
    public String getDownLoadClassName() {
        return this.downLoadClassName;
    }
    
    public void setDownLoadClassName(String downLoadClassName) {
        this.downLoadClassName = downLoadClassName;
    }
    
    public String getExtractClassName() {
        return this.extractClassName;
    }
    
    public void setExtractClassName(String extractClassName) {
        this.extractClassName = extractClassName;
    }
    
    public String toString()
    {
        return JSONObject.toJSONString(this);
    }
    
    public Task(String url, boolean subPageCrawl, int subPageSize, String downLoadClassName, String extractClassName, String siteName) {
        this.url = url;
        this.subPageCrawl = subPageCrawl;
        this.subPageSize = subPageSize;
        this.downLoadClassName = downLoadClassName;
        this.extractClassName = extractClassName;
        this.siteName = siteName;
    }
}
