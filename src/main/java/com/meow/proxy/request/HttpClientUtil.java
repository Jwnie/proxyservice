package com.meow.proxy.request;

import com.google.common.io.ByteStreams;
import com.meow.proxy.base.Const;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * @author Alex
 *         date:2017/12/13
 *         email:jwnie@foxmail.com
 */
public class HttpClientUtil {
    private final static Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);
    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 400;
    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 40;
    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 30000;
    /**
     * 连接池
     */
    private static PoolingHttpClientConnectionManager clientConnectionManager = null;

    static {
        initPoolingHttpClientConnectionManager();
    }

    public static HttpClientUtil getInstance() {
        return HttpUtilSingleton.HTTP_UTIL;
    }

    public CloseableHttpClient createHttpClient(){
        return createHttpClient(CONNECT_TIMEOUT, null ,null);
    }


    public CloseableHttpClient createHttpClient(int timeOut, HttpHost httpHost, BasicClientCookie basicClientCookie){
        RequestConfig.Builder builder = RequestConfig.custom()
                .setConnectionRequestTimeout(timeOut)
                .setConnectTimeout(timeOut)
                .setSocketTimeout(timeOut)
                .setCookieSpec(CookieSpecs.STANDARD);//RFC6265第4节定义的更为宽松的概要，用于与不符合标准的现有服务器的互操作性表现得很好。


        //设置代理
        if(httpHost != null && StringUtils.isNotBlank(httpHost.getHostName())&& httpHost.getPort() > 0){
            builder.setProxy(httpHost);
        }

        RequestConfig requestConfig = builder.build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setDefaultRequestConfig(requestConfig).setRetryHandler(new RequestRetryHandler())
                .setConnectionManager(clientConnectionManager);

        if(basicClientCookie != null){
            CookieStore cookieStore = new BasicCookieStore();
            cookieStore.addCookie(basicClientCookie);
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }

        CloseableHttpClient closeableHttpClient = httpClientBuilder.setRedirectStrategy(
                new DefaultRedirectStrategy() {
                    @Override
                    public boolean isRedirected(org.apache.http.HttpRequest request, HttpResponse response, HttpContext context) {
                        boolean isRedirect = false;
                        try {
                            isRedirect = super.isRedirected((org.apache.http.HttpRequest) request, response, context);
                        } catch (ProtocolException e) {
                            LOG.error("", e);
                        }
                        if (!isRedirect) {
                            int responseCode = response.getStatusLine().getStatusCode();
                            if (responseCode == Const.REDICT_301 || responseCode == Const.REDICT_302) {
                                return true;
                            }
                        }
                        return isRedirect;
                    }
                    @Override
                    protected URI createLocationURI(String location) throws ProtocolException {
                        location = location.replace("|", "%7C");
                        return super.createLocationURI(location);
                    }
                }
        ).build();
        return closeableHttpClient;
    }


    public Response getResponse(CloseableHttpClient client,String url) {
        return getResponse(client,null, url);
    }

    /**
     * Request 參數為null，則默認為httpGet請求
     * @param client
     * @param request
     * @param url
     * @return
     */
    public Response getResponse(CloseableHttpClient client,Request request, String url) {
        if(request != null){
            if(request.getMethod().equals(Const.METHOD_HTTPGET)){
                return httpGetResponse(client,request,url);
            }else if(request.getMethod().equals(Const.METHOD_HTTPPOST)){
                return httpPostResponse(client,request,url);
            }else{
                LOG.warn("暂不支持的http请求类型："+ request.getMethod());
                return null;
            }
        }else{
            return httpGetResponse(client,null,url);
        }
    }

    public Response httpGetResponse(CloseableHttpClient client,String url){
        return httpGetResponse(client,null,url);
    }

    public Response httpPostResponse(CloseableHttpClient client,String url){
        return httpPostResponse(client,null,url);
    }



    private Response httpGetResponse(CloseableHttpClient client,Request request, String url){
        CloseableHttpResponse closeableHttpResponse = null;
        HttpGet httpGet = new HttpGet(urlEncode(url));
        Response response = null;
        //请求头设置
        if(request != null){
            Map<String, String> headers = request.getHeaders();
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
        }

        try{
            closeableHttpResponse = client.execute(httpGet);
            response = getHttpResponse(request,closeableHttpResponse);
            response.setUrl(url);
        }catch(Exception e){
            LOG.error("请求失败，url:"+url,e);
        }finally {
            closeResources(closeableHttpResponse, null);
        }
        return response;
    }

    private Response httpPostResponse(CloseableHttpClient client,Request request, String url){
        Response response = null;
        HttpPost httpPost = new HttpPost(urlEncode(url));
        if(request != null){
            Map<String, String> headers = request.getHeaders();
            // 设置头
            if (headers != null && headers.size() != 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            Map<String, Object> params = request.getParams();
            if(params != null && params.size()>0){
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for(Map.Entry<String,Object> entry : params.entrySet()){
                    nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    LOG.error("设置post请求参数失败:",e);
                }
            }
        }
        CloseableHttpResponse closeableHttpResponse = null;
        try{
            closeableHttpResponse = client.execute(httpPost);
            response = getHttpResponse(request,closeableHttpResponse);
            response.setUrl(url);
        }catch(Exception e){
            LOG.error("请求失败，url:"+url,e);
        }finally{
            closeResources(closeableHttpResponse, null);
        }
        return response;
    }



    /**
     * 设置response
     * @param request
     * @param httpResponse
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private Response getHttpResponse(Request request, CloseableHttpResponse httpResponse) throws UnsupportedEncodingException,IOException {
        Response response = new Response();
        String charSet = "utf-8";
        if(request != null){
            charSet = request.getCharSet();
            if(StringUtils.isBlank(charSet)){
                charSet = httpResponse.getEntity().getContentType()==null ? "utf-8":StringUtils.contains(httpResponse.getEntity().getContentType().getValue(),"charset") ? getCharSet(httpResponse.getEntity().getContentType().getValue()):"utf-8";
            }
        }
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        // 获取返回数据
        HttpEntity entity = httpResponse.getEntity();
        Header header = entity.getContentEncoding();
        if (header != null && Const.SYMBOL_ZIP.equals(header.getValue().toLowerCase())) {
            byte[] bytes = ByteStreams.toByteArray(new GZIPInputStream(entity.getContent()));
            String content = new String(bytes, charSet);
            response.setContent(content);
        } else {
            byte[] bytes = EntityUtils.toByteArray(entity);
            String content = new String(bytes, charSet);
            response.setContent(content);
        }
        return response;
    }

    /**
     * 将url进行encode编码，这里不能直接使用URlEncode(url,"utf-8");方法进编码，
     * 会报org.apache.http.client.ClientProtocolException，这里只将特殊字符转义
     * 如：+、空格、#、{、}、“ 等
     * @param url
     * @return
     */
    private String urlEncode(String url){
        url = url.replaceAll("\\+","%2b")
                .replaceAll(" ","%20")
                .replaceAll("\\{","%7b")
                .replaceAll("}","%7d")
                .replaceAll("\"","%22");
        return url;
    }

    /**
     * 初始化连接池，支持http/https
     */
    private static void initPoolingHttpClientConnectionManager() {
        SSLContext sslcontext = null;
        try {
            //TLS安全协议上下文获取
            sslcontext = SSLContext.getInstance("TLS");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager x509m = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
            };
            sslcontext.init(null, new TrustManager[]{x509m}, new java.security.SecureRandom());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
            RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            org.apache.http.config.Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslConnectionSocketFactory).build();

            clientConnectionManager = new PoolingHttpClientConnectionManager(registry);
            // 设置最大连接数
            clientConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
            // 设置每个连接的路由数
            clientConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("",e);
        } catch (KeyManagementException e) {
            LOG.error("",e);
        }
    }

    private static class HttpUtilSingleton {
        private final static HttpClientUtil HTTP_UTIL = new HttpClientUtil();
    }

    /**
     * 截取编码方式
     * @param str
     * @return
     */
    private String getCharSet(String str){
        String charSet = match(str, Const.CHARSET_PATTERN);
        return charSet;
    }


    /**
     * 正则匹配
     * @param s
     * @param pattern
     * @return
     */
    private String match(String s, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
        Matcher matcher = p.matcher(s);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    public void closeResources(CloseableHttpResponse closeableHttpResponse, CloseableHttpClient closeableHttpClient){
        try{
            if(closeableHttpResponse != null){
                closeableHttpResponse.close();
            }
            if(closeableHttpClient != null){
                closeableHttpClient.close();
            }
        }catch(IOException e){
            LOG.error("关闭closeableHttpResponse失败：",e);
        }
    }
}
