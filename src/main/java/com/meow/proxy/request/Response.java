package com.meow.proxy.request;

/**
 * @author Alex
 *         date:2017/12/14
 *         email:jwnie@foxmail.com
 */
public class Response {
    private String url;
    private int statusCode;
    private String content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
