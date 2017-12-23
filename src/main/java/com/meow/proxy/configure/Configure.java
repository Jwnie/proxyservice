package com.meow.proxy.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Jwnie on 2017/12/17.
 */
@Component
public class Configure {
	@Value("${com.meow.proxy.configure.chromedriver.path}")
	private String chromeDriverPath;
	
	public String getChromeDriverPath()
	{
		return this.chromeDriverPath;
	}
	
	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}

}
