package com.meow.proxy.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Alex
 *         date:2017/12/22
 *         email:jwnie@foxmail.com
 */
public class IPAddr {
    private String country;
    private String province;
    private String city;
    private String isp;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    @Override
    public String toString() {
        return JSONObject.toJSON(this).toString();
    }
}
