package com.meow.proxy.base;

/**
 * @author Alex
 *         date:2017/12/14
 *         email:jwnie@foxmail.com
 */
public class Const {
    /**
     *http响应状态码
     */
    public final static int REDICT_301 = 301;
    public final static int REDICT_302 = 302;

    /**
     * userAgent
     */
    public final static String USERAGENT = "User-Agent";
    public final static String USER_AGENT[]={
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; CIBA; MAXTHON 2.0)",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3",
            "Opera/9.80 (Windows NT 6.1; WOW64; U; en) Presto/2.10.229 Version/11.62",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.11 (KHTML, like Gecko) Ubuntu/11.10 Chromium/27.0.1453.93 Chrome/27.0.1453.93 Safari/537.36",
            "Mozilla/5.0 (Linux; Android 4.1.2; Nexus 7 Build/JZ054K) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19"
    };

    /**
     * 请求类型(httpGet/httpPost)
     */
    public final static String METHOD_HTTPGET = "httpGet";
    public final static String METHOD_HTTPPOST = "httpPost";

    public static String CHARSET_PATTERN ="charset\\s?=\\s?((\\w+-?)?\\w+)\\s?";

    public static String SYMBOL_ZIP ="zip";

    public static String CHINESE_CHAR ="[\u4e00-\u9fa5]";

}
