package com.meow.proxy.enums;

/**
 * Created by Jwnie on 2017/12/16.
 */
public enum ProxySite {
	xicidaili("www.xicidaili.com","西刺免费代理IP"),
	goubanjia("www.goubanjia.com", "全网代理IP"),
	;
	
	private String proxySiteDomain;
	private String proxySiteName;
	
	public String getProxySiteDomain() {
		return proxySiteDomain;
	}
	
	public void setProxySiteDomain(String proxySiteDomain) {
		this.proxySiteDomain = proxySiteDomain;
	}
	
	public String getProxySiteName() {
		return proxySiteName;
	}
	
	public void setProxySiteName(String proxySiteName) {
		this.proxySiteName = proxySiteName;
	}
	
	ProxySite(String proxySiteDomain, String proxySiteName) {
		this.proxySiteDomain = proxySiteDomain;
		this.proxySiteName = proxySiteName;
	}
}
