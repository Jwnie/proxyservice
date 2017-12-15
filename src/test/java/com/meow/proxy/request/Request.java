package com.meow.proxy.request;

import com.meow.proxy.base.Const;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alex
 *         date:2017/12/15
 *         email:jwnie@foxmail.com
 */
public class Request {

    @Test
    public void testMatch(){
        System.out.println(getCharSet("charset=utf-8\n"));
        System.out.println(getCharSet("CHARSET = gb2312"));
        System.out.println(getCharSet("text/html;charset=UTF-8"));


    }

    /**
     * 截取编码方式
     * @param str
     * @return
     */
    private String getCharSet(String str){
        String charSet = findCharset(str, Const.CHARSET_PATTERN);
        return charSet;
    }

    /**
     * 正则匹配
     * @param s
     * @param pattern
     * @return
     */
    private String findCharset(String s, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
        Matcher matcher = p.matcher(s);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
