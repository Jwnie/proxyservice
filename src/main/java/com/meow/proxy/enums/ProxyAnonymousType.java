package com.meow.proxy.enums;

/**
 * @author Alex
 *         date:2017/12/13
 *         email:jwnie@foxmail.com
 */
public enum ProxyAnonymousType {
    transparent(1, "transparent"),
    anonymous(2, "anonymous"),
    distorting(3, "distorting"),
    elite(4, "elite");

    private int anonymousKey;
    private String anonymousType;

    ProxyAnonymousType(int anonymousKey, String anonymousType) {
        this.anonymousKey = anonymousKey;
        this.anonymousType = anonymousType;
    }
}
