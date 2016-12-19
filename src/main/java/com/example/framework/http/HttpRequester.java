package com.example.framework.http;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ZhaiJiaYi on 2016/12/19.
 */
public class HttpRequester {
    private final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
    private int timeout = 10000;
    private Map<String, String> headers = new HashedMap();
    private Map<String, String> parameters = new HashedMap();

    public HttpRequester() {
    }

    public static HttpRequester newInstance() {
        return new HttpRequester();
    }

    public HttpRequester setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public HttpRequester setHeader(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequester setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public HttpResponse get(String url) {
        if(StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException();
        } else {
            HttpGet httpGet = new HttpGet(url);
            return this.executeRequest(httpGet);
        }
    }

    public HttpResponse post(String url) {
        if(StringUtils.isBlank(url)) {
            throw new IllegalArgumentException();
        } else {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity entity = this.convertRequsetEntity(this.parameters);
            httpPost.setEntity(entity);
            return this.executeRequest(httpPost);
        }
    }

    public HttpResponse put(String url) {
        if(StringUtils.isBlank(url)) {
            throw new IllegalArgumentException();
        } else {
            HttpPut httpPut = new HttpPut(url);
            HttpEntity entity = this.convertRequsetEntity(this.parameters);
            httpPut.setEntity(entity);
            return this.executeRequest(httpPut);
        }
    }

    public HttpResponse delete(String url) {
        if(StringUtils.isBlank(url)) {
            throw new IllegalArgumentException();
        } else {
            HttpDelete httpDelete = new HttpDelete(url);
            return this.executeRequest(httpDelete);
        }
    }

    private HttpEntity convertRequsetEntity(Map<String, String> parameters) {
        String parameterString = "";
        if(null != parameters && parameters.isEmpty()) {
            StringBuilder stringEntity = new StringBuilder();
            Iterator var4 = parameters.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry entry = (Map.Entry)var4.next();
                stringEntity.append((String)entry.getKey()).append("=").append((String)entry.getValue()).append("&");
            }

            parameterString = stringEntity.substring(0, stringEntity.length() - 1);
        }

        StringEntity stringEntity1 = new StringEntity(parameterString, this.CHARSET_UTF_8);
        stringEntity1.setContentType(ContentType.APPLICATION_JSON.toString());
        stringEntity1.setContentEncoding(this.CHARSET_UTF_8.toString());
        return stringEntity1;
    }

    private HttpResponse executeRequest(HttpRequestBase httpRequest) {
        RequestConfig config = this.constructHttpConfig();
        SSLConnectionSocketFactory sslConnectionSocketFactory = this.constructSSLFactory();
        Iterator e = this.headers.entrySet().iterator();

        while(e.hasNext()) {
            Map.Entry entry = (Map.Entry)e.next();
            httpRequest.setHeader((String)entry.getKey(), (String)entry.getValue());
        }

        try {
            CloseableHttpClient e1 = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setDefaultRequestConfig(config).build();
            Throwable entry1 = null;

            HttpResponse var9;
            try {
                CloseableHttpResponse response = e1.execute(httpRequest);
                Throwable var7 = null;

                try {
                    HttpResponse httpResponse = new HttpResponse();
                    httpResponse.setStatus(response.getStatusLine().getStatusCode());
                    httpResponse.setContent(EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8")));
                    httpResponse.setHeaders(response.getAllHeaders());
                    httpResponse.setProtocolVersion(response.getProtocolVersion());
                    var9 = httpResponse;
                } catch (Throwable var34) {
                    var7 = var34;
                    throw var34;
                } finally {
                    if(response != null) {
                        if(var7 != null) {
                            try {
                                response.close();
                            } catch (Throwable var33) {
                                var7.addSuppressed(var33);
                            }
                        } else {
                            response.close();
                        }
                    }

                }
            } catch (Throwable var36) {
                entry1 = var36;
                throw var36;
            } finally {
                if(e1 != null) {
                    if(entry1 != null) {
                        try {
                            e1.close();
                        } catch (Throwable var32) {
                            entry1.addSuppressed(var32);
                        }
                    } else {
                        e1.close();
                    }
                }

            }

            return var9;
        } catch (IOException var38) {
            var38.printStackTrace();
            return HttpResponse.empty();
        }
    }

    private SSLConnectionSocketFactory constructSSLFactory() {
        X509TrustManager x509TrustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string) {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[])null, new TrustManager[]{x509TrustManager}, (SecureRandom)null);
        } catch (KeyManagementException | NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        }

        return new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    private RequestConfig constructHttpConfig() {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setRedirectsEnabled(true);
        builder.setCircularRedirectsAllowed(true);
        builder.setConnectTimeout(this.timeout);
        builder.setConnectionRequestTimeout(this.timeout);
        builder.setSocketTimeout(this.timeout);
        return builder.build();
    }
}
