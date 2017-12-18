package com.meow.proxy.enums;

/**
 * Created by Jwnie on 2017/12/16.
 */
public enum CountryType {
	china(1,"china"),
	;
	private int key;
	private String countryName;
	
	public int getKey() {
		return key;
	}
	
	public void setKey(int key) {
		this.key = key;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	CountryType(int key, String countryName) {
		this.key = key;
		this.countryName = countryName;
	}
}
