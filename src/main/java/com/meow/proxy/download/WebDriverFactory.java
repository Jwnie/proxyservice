package com.meow.proxy.download;

import com.meow.proxy.configure.Configure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Alex
 *         date:2017/12/22
 *         email:jwnie@foxmail.com
 */
@Component
public class WebDriverFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebDriverFactory.class);
    @Autowired
    Configure configure;

    public WebDriver getWebDriver() {
        WebDriver mDriver = null;
        try {
            System.setProperty("webdriver.chrome.driver", configure.getChromeDriverPath());
            ChromeOptions options = new ChromeOptions();
            //设置不弹窗口
            options.addArguments("--headless");

            //设置Chrome不加载图片
            Map<String, Object> contentSettings = new HashMap<String, Object>();
            contentSettings.put("images", 2);
            Map<String, Object> preferences = new HashMap<String, Object>();
            preferences.put("profile.default_content_setting_values", contentSettings);
            options.setExperimentalOption("prefs", preferences);
            mDriver = new ChromeDriver(options);
            mDriver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
            return mDriver;
        } catch (Exception e) {
            LOG.error("启动Chrome发生异常：{}", e);
        }
        return mDriver;
    }
}
