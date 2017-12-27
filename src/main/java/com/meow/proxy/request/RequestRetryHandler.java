package com.meow.proxy.request;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * 请求重试处理
 *
 * @author Alex
 *         date:2017/12/14
 *         email:jwnie@foxmail.com
 */
public class RequestRetryHandler implements HttpRequestRetryHandler {


    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
        if (executionCount >= 5) {// 如果已经重试了5次，就放弃
            return false;
        }
        if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
            return true;
        }
        if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
            return false;
        }
        if (exception instanceof InterruptedIOException) {// 超时
            return false;
        }
        if (exception instanceof UnknownHostException) {// 目标服务器不可达
            return false;
        }
        if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
            return false;
        }
        if (exception instanceof SSLException) {// SSL握手异常
            return false;
        }

        HttpClientContext clientContext = HttpClientContext
                .adapt(httpContext);
        HttpRequest request = clientContext.getRequest();
        if (!(request instanceof HttpEntityEnclosingRequest)) {// 如果请求是幂等的，就再次尝试
            return true;
        }
        return false;
    }
}
