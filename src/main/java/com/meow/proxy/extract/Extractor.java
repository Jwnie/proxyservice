package com.meow.proxy.extract;

import com.meow.proxy.entity.Proxy;

import java.util.List;

/**
 * 代理抽取类
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
public interface Extractor {

    public List<Proxy> extract(String htmlContent);

    public List<Proxy> extract(List<String> htmlContentList);

}
