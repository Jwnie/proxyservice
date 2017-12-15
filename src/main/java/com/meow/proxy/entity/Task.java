package com.meow.proxy.entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 任务类
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
@Component
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
    private String downLoadClassPath;
    /**
     * 抽取类class路径
     */
    private String extractClassPath;
    /**
     * 站点名称
     */
    private String siteName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSubPageCrawl() {
        return subPageCrawl;
    }

    public void setSubPageCrawl(boolean subPageCrawl) {
        this.subPageCrawl = subPageCrawl;
    }

    public int getSubPageSize() {
        return subPageSize;
    }

    public void setSubPageSize(int subPageSize) {
        this.subPageSize = subPageSize;
    }

    public String getDownLoadClassPath() {
        return downLoadClassPath;
    }

    public void setDownLoadClassPath(String downLoadClassPath) {
        this.downLoadClassPath = downLoadClassPath;
    }

    public String getExtractClassPath() {
        return extractClassPath;
    }

    public void setExtractClassPath(String extractClassPath) {
        this.extractClassPath = extractClassPath;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
