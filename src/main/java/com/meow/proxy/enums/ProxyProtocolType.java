package com.meow.proxy.enums;

/**
 * @author Alex
 *         date:2017/12/13
 *         email:jwnie@foxmail.com
 */
public enum ProxyProtocolType {
    http(1, "http"),
    https(2, "https"),
    socks4(3, "socks4"),
    socks5(4, "socks5"),
    //不区分socks4或5
    socks(5, "socks");

    private int key;
    private String requestType;
    
    public int getKey() {
        return key;
    }
    
    public void setKey(int key) {
        this.key = key;
    }
    
    public String getRequestType() {
        return requestType;
    }
    
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    
    ProxyProtocolType(int key, String requestType) {
        this.key = key;
        this.requestType = requestType;
    }
}
