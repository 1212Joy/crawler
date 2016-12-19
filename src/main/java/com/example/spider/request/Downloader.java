package com.example.spider.request;

import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.HttpConstant;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * The http downloader based on HttpClient.
 */

public class Downloader extends AbstractDownloader {

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private PoolingHttpClientConnectionManager connectionManager;

    private boolean enableProxy = false;
    private ProxyConfig proxyConfig;
    private HttpHost httpHost;

    public Downloader() {
        init();
    }

    public Downloader(ProxyConfig proxyConfig) {
        this();
        this.enableProxy = true;
        this.proxyConfig = proxyConfig;
        this.httpHost = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort(), "http");
        // g();
    }

    private void init() {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);
    }

    private CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return getClient(null);
        }
        String domain = site.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = getClient(site);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    public String get(String url) throws Exception {
        HttpUriRequest request = RequestBuilder.get(url).build();
        CloseableHttpResponse httpResponse = execute(request, null);
        return EntityUtils.toString(httpResponse.getEntity());
    }

    public CloseableHttpResponse execute(HttpUriRequest httpUriRequest, Site site) throws IOException {
        if (enableProxy) {
            return getHttpClient(site).execute(httpHost, httpUriRequest, generateClientContext());
        } else {
            return getHttpClient(site).execute(httpUriRequest);
        }
    }

    @Override
    public Page download(Request request, Task task) {
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }
        Set<Integer> acceptStatCode;
        String charset = null;
        Map<String, String> headers = null;
        if (site != null) {
            acceptStatCode = site.getAcceptStatCode();
            charset = site.getCharset();
            headers = site.getHeaders();
        } else {
            acceptStatCode = Sets.newHashSet(200);
        }
        logger.info("downloading page {}", request.getUrl());
        CloseableHttpResponse httpResponse = null;
        int statusCode = 0;
        try {
            HttpUriRequest httpUriRequest = getHttpUriRequest(request, site, headers);
            httpResponse = execute(httpUriRequest, site);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            request.putExtra(Request.STATUS_CODE, statusCode);
            if (statusAccept(acceptStatCode, statusCode)) {
                Page page = handleResponse(request, charset, httpResponse, task);
                onSuccess(request);
                return page;
            } else {
                logger.warn("code error " + statusCode + "\t" + request.getUrl());
                return null;
            }
        } catch (IOException e) {
            logger.warn("download page " + request.getUrl() + " error", e);
            if (site.getCycleRetryTimes() > 0) {
                return addToCycleRetry(request, site);
            }
            onError(request);
            return null;
        } finally {
            request.putExtra(Request.STATUS_CODE, statusCode);
            try {
                if (httpResponse != null) {
                    //ensure the connection is released back to pool
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
                logger.warn("close response fail", e);
            }
        }
    }

    @Override
    public void setThread(int thread) {
        setPoolSize(thread);
    }

    protected boolean statusAccept(Set<Integer> acceptStatCode, int statusCode) {
        return acceptStatCode.contains(statusCode);
    }

    protected HttpUriRequest getHttpUriRequest(Request request, Site site, Map<String, String> headers) {
        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(request.getUrl());
        if (headers != null) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(site.getTimeOut())
                .setSocketTimeout(site.getTimeOut())
                .setConnectTimeout(site.getTimeOut())
                .setCookieSpec(CookieSpecs.BEST_MATCH);
        if (site.getHttpProxyPool() != null && site.getHttpProxyPool().isEnable()) {
            HttpHost host = site.getHttpProxyFromPool();
            requestConfigBuilder.setProxy(host);
            request.putExtra(Request.PROXY, host);
        } else if (site.getHttpProxy() != null) {
            HttpHost host = site.getHttpProxy();
            requestConfigBuilder.setProxy(host);
            request.putExtra(Request.PROXY, host);
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        return requestBuilder.build();
    }

    protected RequestBuilder selectRequestMethod(Request request) {
        String method = request.getMethod();
        if (method == null || method.equalsIgnoreCase(HttpConstant.Method.GET)) {
            //default get
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.POST)) {
            RequestBuilder requestBuilder = RequestBuilder.post();
            NameValuePair[] nameValuePair = (NameValuePair[]) request.getExtra("nameValuePair");
            if (nameValuePair != null && nameValuePair.length > 0) {
                requestBuilder.addParameters(nameValuePair);
            }
            return requestBuilder;
        } else if (method.equalsIgnoreCase(HttpConstant.Method.HEAD)) {
            return RequestBuilder.head();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.PUT)) {
            return RequestBuilder.put();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.DELETE)) {
            return RequestBuilder.delete();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.TRACE)) {
            return RequestBuilder.trace();
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task) throws IOException {
        String content = getContent(charset, httpResponse);
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        return page;
    }

    protected String getContent(String charset, HttpResponse httpResponse) throws IOException {
        if (charset == null) {
            byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
            if (htmlCharset != null) {
                return new String(contentBytes, htmlCharset);
            } else {
                logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
                return new String(contentBytes);
            }
        } else {
            return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
        }
    }

    protected String getHtmlCharset(HttpResponse httpResponse, byte[] contentBytes) throws IOException {
        String charset;
        // charset
        // 1、encoding in http header Content-Type
        String value = httpResponse.getEntity().getContentType().getValue();
        charset = UrlUtils.getCharset(value);
        if (StringUtils.isNotBlank(charset)) {
            logger.debug("Auto get charset: {}", charset);
            return charset;
        }
        // use default charset to decode first time
        Charset defaultCharset = Charset.defaultCharset();
        String content = new String(contentBytes, defaultCharset.name());
        // 2、charset in meta
        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select("meta");
            for (Element link : links) {
                // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                String metaContent = link.attr("content");
                String metaCharset = link.attr("charset");
                if (metaContent.indexOf("charset") != -1) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    charset = metaContent.split("=")[1];
                    break;
                }
                // 2.2、html5 <meta charset="UTF-8" />
                else if (StringUtils.isNotEmpty(metaCharset)) {
                    charset = metaCharset;
                    break;
                }
            }
        }
        logger.debug("Auto get charset: {}", charset);
        // 3、todo use tools as cpdetector for content decode
        return charset;
    }


    public CloseableHttpClient getClient(Site site) {
        return generateClient(site);
    }


    private CloseableHttpClient generateClient(Site site) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);
        if (site != null && site.getUserAgent() != null) {
            httpClientBuilder.setUserAgent(site.getUserAgent());
        } else {
            httpClientBuilder.setUserAgent("");
        }
        if (site == null || site.isUseGzip()) {
            httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

                public void process(
                        final HttpRequest request,
                        final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }

                }
            });
        }
        SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        if (site != null) {
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));
        }
        generateCookie(httpClientBuilder, site);

        if (enableProxy) {
            updateProvider(httpClientBuilder);
        }
        return httpClientBuilder.build();
    }

    public HttpClientContext generateClientContext() {
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(httpHost, basicAuth);
        HttpClientContext httpClientContext = HttpClientContext.create();
        httpClientContext.setAuthCache(authCache);
        return httpClientContext;
    }

    private void updateProvider(HttpClientBuilder httpClientBuilder) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                new AuthScope(httpHost),
                new UsernamePasswordCredentials(proxyConfig.getUser(), proxyConfig.getPassword())
        );

        httpClientBuilder.setDefaultCredentialsProvider(provider);
    }

    private void generateCookie(HttpClientBuilder httpClientBuilder, Site site) {
        if (site == null) {
            return;
        }
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(site.getDomain());
            cookieStore.addCookie(cookie);
        }
        for (Map.Entry<String, Map<String, String>> domainEntry : site.getAllCookies().entrySet()) {
            for (Map.Entry<String, String> cookieEntry : domainEntry.getValue().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie.setDomain(domainEntry.getKey());
                cookieStore.addCookie(cookie);
            }
        }
        httpClientBuilder.setDefaultCookieStore(cookieStore);
    }

    public void setPoolSize(int poolSize) {
        connectionManager.setMaxTotal(poolSize);
    }
}
