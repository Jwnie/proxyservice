package com.meow.proxy.request;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 *         date:2017/12/14
 *         email:jwnie@foxmail.com
 */
@Component
public class Request {
    /**
     * 请求方法
     */
    private String method;

    /**
     * 网页编码方式
     */
    private String charSet;
    /**
     * 请求头信息
     */
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * post的一些参数
     */
    Map<String, Object> params = new HashMap<>();


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Request setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
