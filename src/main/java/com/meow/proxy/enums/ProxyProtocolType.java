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
    socks5(4, "socks5");

    private int key;
    private String requestType;

    ProxyProtocolType(int key, String requestType) {
        this.key = key;
        this.requestType = requestType;
    }
}
