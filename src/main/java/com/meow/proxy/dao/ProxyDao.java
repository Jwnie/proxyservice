package com.meow.proxy.dao;

import com.meow.proxy.entity.Proxy;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Alex
 *         date:2017/12/18
 *         email:jwnie@foxmail.com
 */
public interface ProxyDao {

    void saveProxies(List<Proxy> proxyList);

    void updateProxies(List<Proxy> proxyList);

    List<Proxy> queryValidProxies();

    /**
     * 查询前一百条有效的代理
     *
     * @return
     */
    List<Proxy> queryProxy(@Param("protocolType") String protocolType,@Param("isDemostic") String isDemostic,@Param("anonymousType") String anonymousType);

    List<Map<String,String>> proxyStatisticBySite();

    int queryValidProxyCount(@Param("protocolType") String protocolType,@Param("isDemostic") String isDemostic,@Param("anonymousType") String anonymousType);

}
