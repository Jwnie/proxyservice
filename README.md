# 爬取网上公开代理

## 支持爬取的代理
* 西刺: http://www.xicidaili.com/
* 全网代理IP: http://www.goubanjia.com/

## 代理查询接口
* <b>http://localhost:8888/proxy/getProxy?isDemostic=true&anonymousType=elite&protocolType=https</b></br>
    默认返回前一百条可用代理;
  <b>参数说明：</b>  
  (1) isDemostic: 可选参数，是否为国内代理，值为true和false；
  (2) anonymousType: 可选参数，代理的匿名类型，分为四种：transparent(透明)、anonymous(匿名)、distorting(混淆)、elite(高匿)；
  (3) protocolType: 可选参数，代理的协议类型，分为http、https、socks4、socks5和socks(未做socks4和socks5的细分，统称为socks)
  
  示例数据:
  ![返回数据](/img/getproxy.png)