/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.URL_AUTH_CREDENTIALS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.TextUtils;

import com.gargoylesoftware.htmlunit.WebRequest.HttpHint;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitCookieSpecProvider;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitCookieStore;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitRedirectStrategie;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitSSLConnectionSocketFactory;
import com.gargoylesoftware.htmlunit.httpclient.SocksConnectionSocketFactory;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Default implementation of {@link WebConnection}, using the HttpClient library to perform HTTP requests.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Nicolas Belisle
 * @author Ronald Brill
 * @author John J Murdoch
 * @author Carsten Steul
 * @author Hartmut Arlt
 */
public class HttpWebConnection implements WebConnection {

    private static final Log LOG = LogFactory.getLog(HttpWebConnection.class);

    private static final String HACKED_COOKIE_POLICY = "mine";

    // have one per thread because this is (re)configured for every call (see configureHttpProcessorBuilder)
    // do not use a ThreadLocal because this in only accessed form this class
    private final Map<Thread, HttpClientBuilder> httpClientBuilder_ = new WeakHashMap<>();
    private final WebClient webClient_;

    private String virtualHost_;
    private final CookieSpecProvider htmlUnitCookieSpecProvider_;
    private final WebClientOptions usedOptions_;
    private PoolingHttpClientConnectionManager connectionManager_;

    /** Authentication cache shared among all threads of a web client. */
    private final AuthCache sharedAuthCache_ = new SynchronizedAuthCache();

    /** Maintains a separate {@link HttpClientContext} object per HttpWebConnection and thread. */
    private final Map<Thread, HttpClientContext> httpClientContextByThread_ = new WeakHashMap<>();

    /**
     * Creates a new HTTP web connection instance.
     * @param webClient the WebClient that is using this connection
     */
    public HttpWebConnection(final WebClient webClient) {
        webClient_ = webClient;
        htmlUnitCookieSpecProvider_ = new HtmlUnitCookieSpecProvider(webClient.getBrowserVersion());
        usedOptions_ = new WebClientOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebResponse getResponse(final WebRequest webRequest) throws IOException {
        final HttpClientBuilder builder = reconfigureHttpClientIfNeeded(getHttpClientBuilder(), webRequest);

        HttpUriRequest httpMethod = null;
        try {
            try {
                httpMethod = makeHttpMethod(webRequest, builder);
            }
            catch (final URISyntaxException e) {
                throw new IOException("Unable to create URI from URL: " + webRequest.getUrl().toExternalForm()
                        + " (reason: " + e.getMessage() + ")", e);
            }

            final URL url = webRequest.getUrl();
            final HttpHost httpHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
            final long startTime = System.currentTimeMillis();

            final HttpContext httpContext = getHttpContext();
            HttpResponse httpResponse = null;
            try {
                try (CloseableHttpClient closeableHttpClient = builder.build()) {
                    httpResponse = closeableHttpClient.execute(httpHost, httpMethod, httpContext);
                }
            }
            catch (final SSLPeerUnverifiedException s) {
                // Try to use only SSLv3 instead
                if (webClient_.getOptions().isUseInsecureSSL()) {
                    HtmlUnitSSLConnectionSocketFactory.setUseSSL3Only(httpContext, true);
                    try (CloseableHttpClient closeableHttpClient = builder.build()) {
                        httpResponse = closeableHttpClient.execute(httpHost, httpMethod, httpContext);
                    }
                }
                else {
                    throw s;
                }
            }
            catch (final Error e) {
                // in case a StackOverflowError occurs while the connection is leased, it won't get released.
                // Calling code may catch the StackOverflowError, but due to the leak, the httpClient_ may
                // come out of connections and throw a ConnectionPoolTimeoutException.
                // => best solution, discard the HttpClient instance.
                httpClientBuilder_.remove(Thread.currentThread());
                throw e;
            }

            final DownloadedContent downloadedBody = downloadResponseBody(httpResponse);
            final long endTime = System.currentTimeMillis();
            return makeWebResponse(httpResponse, webRequest, downloadedBody, endTime - startTime);
        }
        finally {
            if (httpMethod != null) {
                onResponseGenerated(httpMethod);
            }
        }
    }

    /**
     * Called when the response has been generated. Default action is to release
     * the HttpMethod's connection. Subclasses may override.
     * @param httpMethod the httpMethod used (can be null)
     */
    protected void onResponseGenerated(final HttpUriRequest httpMethod) {
    }

    /**
     * Returns the {@link HttpClientContext} for the current thread. Creates a new one if necessary.
     */
    private synchronized HttpContext getHttpContext() {
        HttpClientContext httpClientContext = httpClientContextByThread_.get(Thread.currentThread());
        if (httpClientContext == null) {
            httpClientContext = new HttpClientContext();

            // set the shared authentication cache
            httpClientContext.setAttribute(HttpClientContext.AUTH_CACHE, sharedAuthCache_);

            httpClientContextByThread_.put(Thread.currentThread(), httpClientContext);
        }
        return httpClientContext;
    }

    private void setProxy(final HttpRequestBase httpRequest, final WebRequest webRequest) {
        final InetAddress localAddress = webClient_.getOptions().getLocalAddress();
        final RequestConfig.Builder requestBuilder = createRequestConfigBuilder(getTimeout(webRequest), localAddress);

        if (webRequest.getProxyHost() == null) {
            requestBuilder.setProxy(null);
            httpRequest.setConfig(requestBuilder.build());
        }
        else {
            final HttpHost proxy = new HttpHost(webRequest.getProxyHost(), webRequest.getProxyPort());
            if (webRequest.isSocksProxy()) {
                SocksConnectionSocketFactory.setSocksProxy(getHttpContext(), proxy);
            }
            else {
                requestBuilder.setProxy(proxy);
                httpRequest.setConfig(requestBuilder.build());
            }
        }
    }

    /**
     * Creates an <tt>HttpMethod</tt> instance according to the specified parameters.
     * @param webRequest the request
     * @param httpClientBuilder the httpClientBuilder that will be configured
     * @return the <tt>HttpMethod</tt> instance constructed according to the specified parameters
     * @throws IOException
     * @throws URISyntaxException
     */
    private HttpUriRequest makeHttpMethod(final WebRequest webRequest, final HttpClientBuilder httpClientBuilder)
        throws URISyntaxException {

        final HttpContext httpContext = getHttpContext();
        final Charset charset = webRequest.getCharset();
        // Make sure that the URL is fully encoded. IE actually sends some Unicode chars in request
        // URLs; because of this we allow some Unicode chars in URLs. However, at this point we're
        // handing things over the HttpClient, and HttpClient will blow up if we leave these Unicode
        // chars in the URL.
        final URL url = UrlUtils.encodeUrl(webRequest.getUrl(), false, charset);

        URI uri = UrlUtils.toURI(url, escapeQuery(url.getQuery()));
        if (getVirtualHost() != null) {
            uri = URI.create(getVirtualHost());
        }
        final HttpRequestBase httpMethod = buildHttpMethod(webRequest.getHttpMethod(), uri);
        setProxy(httpMethod, webRequest);

        if (httpMethod instanceof HttpEntityEnclosingRequest) {
            // POST as well as PUT and PATCH
            final HttpEntityEnclosingRequest method = (HttpEntityEnclosingRequest) httpMethod;

            if (webRequest.getEncodingType() == FormEncodingType.URL_ENCODED && method instanceof HttpPost) {
                final HttpPost postMethod = (HttpPost) method;
                if (webRequest.getRequestBody() == null) {
                    final List<NameValuePair> pairs = webRequest.getRequestParameters();
                    final String query = URLEncodedUtils.format(NameValuePair.toHttpClient(pairs), charset);

                    final StringEntity urlEncodedEntity;
                    if (webRequest.hasHint(HttpHint.IncludeCharsetInContentTypeHeader)) {
                        urlEncodedEntity = new StringEntity(query,
                                ContentType.create(URLEncodedUtils.CONTENT_TYPE, charset));

                    }
                    else {
                        urlEncodedEntity = new StringEntity(query, charset);
                        urlEncodedEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
                    }
                    postMethod.setEntity(urlEncodedEntity);
                }
                else {
                    final String body = StringUtils.defaultString(webRequest.getRequestBody());
                    final StringEntity urlEncodedEntity = new StringEntity(body, charset);
                    urlEncodedEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
                    postMethod.setEntity(urlEncodedEntity);
                }
            }
            else if (webRequest.getEncodingType() == FormEncodingType.TEXT_PLAIN && method instanceof HttpPost) {
                final HttpPost postMethod = (HttpPost) method;
                if (webRequest.getRequestBody() == null) {
                    final StringBuilder body = new StringBuilder();
                    for (final NameValuePair pair : webRequest.getRequestParameters()) {
                        body.append(StringUtils.remove(StringUtils.remove(pair.getName(), '\r'), '\n'))
                            .append('=')
                            .append(StringUtils.remove(StringUtils.remove(pair.getValue(), '\r'), '\n'))
                            .append("\r\n");
                    }
                    final StringEntity bodyEntity = new StringEntity(body.toString(), charset);
                    bodyEntity.setContentType(MimeType.TEXT_PLAIN);
                    postMethod.setEntity(bodyEntity);
                }
                else {
                    final String body = StringUtils.defaultString(webRequest.getRequestBody());
                    final StringEntity bodyEntity =
                            new StringEntity(body, ContentType.create(MimeType.TEXT_PLAIN, charset));
                    postMethod.setEntity(bodyEntity);
                }
            }
            else if (FormEncodingType.MULTIPART == webRequest.getEncodingType()) {
                final Charset c = getCharset(charset, webRequest.getRequestParameters());
                final MultipartEntityBuilder builder = MultipartEntityBuilder.create().setLaxMode();
                builder.setCharset(c);

                for (final NameValuePair pair : webRequest.getRequestParameters()) {
                    if (pair instanceof KeyDataPair) {
                        buildFilePart((KeyDataPair) pair, builder);
                    }
                    else {
                        builder.addTextBody(pair.getName(), pair.getValue(),
                                ContentType.create(MimeType.TEXT_PLAIN, charset));
                    }
                }
                method.setEntity(builder.build());
            }
            else { // for instance a PUT or PATCH request
                final String body = webRequest.getRequestBody();
                if (body != null) {
                    method.setEntity(new StringEntity(body, charset));
                }
            }
        }
        else {
            // this is the case for GET as well as TRACE, DELETE, OPTIONS and HEAD
            if (!webRequest.getRequestParameters().isEmpty()) {
                final List<NameValuePair> pairs = webRequest.getRequestParameters();
                final String query = URLEncodedUtils.format(NameValuePair.toHttpClient(pairs), charset);
                uri = UrlUtils.toURI(url, query);
                httpMethod.setURI(uri);
            }
        }

        configureHttpProcessorBuilder(httpClientBuilder, webRequest);

        // Tell the client where to get its credentials from
        // (it may have changed on the webClient since last call to getHttpClientFor(...))
        final CredentialsProvider credentialsProvider = webClient_.getCredentialsProvider();

        // if the used url contains credentials, we have to add this
        final Credentials requestUrlCredentials = webRequest.getUrlCredentials();
        if (null != requestUrlCredentials
                && webClient_.getBrowserVersion().hasFeature(URL_AUTH_CREDENTIALS)) {
            final URL requestUrl = webRequest.getUrl();
            final AuthScope authScope = new AuthScope(requestUrl.getHost(), requestUrl.getPort());
            // updating our client to keep the credentials for the next request
            credentialsProvider.setCredentials(authScope, requestUrlCredentials);
        }

        // if someone has set credentials to this request, we have to add this
        final Credentials requestCredentials = webRequest.getCredentials();
        if (null != requestCredentials) {
            final URL requestUrl = webRequest.getUrl();
            final AuthScope authScope = new AuthScope(requestUrl.getHost(), requestUrl.getPort());
            // updating our client to keep the credentials for the next request
            credentialsProvider.setCredentials(authScope, requestCredentials);
        }
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        httpContext.removeAttribute(HttpClientContext.CREDS_PROVIDER);
        httpContext.removeAttribute(HttpClientContext.TARGET_AUTH_STATE);
        return httpMethod;
    }

    private static String escapeQuery(final String query) {
        if (query == null) {
            return null;
        }
        return query.replace("%%", "%25%25");
    }

    private static Charset getCharset(final Charset charset, final List<NameValuePair> pairs) {
        for (final NameValuePair pair : pairs) {
            if (pair instanceof KeyDataPair) {
                final KeyDataPair pairWithFile = (KeyDataPair) pair;
                if (pairWithFile.getData() == null && pairWithFile.getFile() != null) {
                    final String fileName = pairWithFile.getFile().getName();
                    for (int i = 0; i < fileName.length(); i++) {
                        if (fileName.codePointAt(i) > 127) {
                            return charset;
                        }
                    }
                }
            }
        }
        return null;
    }

    void buildFilePart(final KeyDataPair pairWithFile, final MultipartEntityBuilder builder) {
        String mimeType = pairWithFile.getMimeType();
        if (mimeType == null) {
            mimeType = MimeType.APPLICATION_OCTET_STREAM;
        }

        final ContentType contentType = ContentType.create(mimeType);
        final File file = pairWithFile.getFile();

        if (pairWithFile.getData() != null) {
            final String filename;
            if (file == null) {
                filename = pairWithFile.getValue();
            }
            else if (pairWithFile.getFileName() == null) {
                filename = file.getName();
            }
            else {
                filename = pairWithFile.getFileName();
            }

            builder.addBinaryBody(pairWithFile.getName(), new ByteArrayInputStream(pairWithFile.getData()),
                    contentType, filename);
            return;
        }

        if (file == null) {
            builder.addPart(pairWithFile.getName(),
                    // Overridden in order not to have a chunked response.
                    new InputStreamBody(new ByteArrayInputStream(new byte[0]), contentType, pairWithFile.getValue()) {
                    @Override
                    public long getContentLength() {
                        return 0;
                    }
                });
            return;
        }

        final String filename;
        if (pairWithFile.getFile() == null) {
            filename = pairWithFile.getValue();
        }
        else if (pairWithFile.getFileName() == null) {
            filename = pairWithFile.getFile().getName();
        }
        else {
            filename = pairWithFile.getFileName();
        }
        builder.addBinaryBody(pairWithFile.getName(), pairWithFile.getFile(), contentType, filename);
    }

    /**
     * Creates and returns a new HttpClient HTTP method based on the specified parameters.
     * @param submitMethod the submit method being used
     * @param uri the uri being used
     * @return a new HttpClient HTTP method based on the specified parameters
     */
    private static HttpRequestBase buildHttpMethod(final HttpMethod submitMethod, final URI uri) {
        final HttpRequestBase method;
        switch (submitMethod) {
            case GET:
                method = new HttpGet(uri);
                break;

            case POST:
                method = new HttpPost(uri);
                break;

            case PUT:
                method = new HttpPut(uri);
                break;

            case DELETE:
                method = new HttpDelete(uri);
                break;

            case OPTIONS:
                method = new HttpOptions(uri);
                break;

            case HEAD:
                method = new HttpHead(uri);
                break;

            case TRACE:
                method = new HttpTrace(uri);
                break;

            case PATCH:
                method = new HttpPatch(uri);
                break;

            default:
                throw new IllegalStateException("Submit method not yet supported: " + submitMethod);
        }
        return method;
    }

    /**
     * Lazily initializes the internal HTTP client.
     *
     * @return the initialized HTTP client
     */
    protected HttpClientBuilder getHttpClientBuilder() {
        final Thread currentThread = Thread.currentThread();
        HttpClientBuilder builder = httpClientBuilder_.get(currentThread);
        if (builder == null) {
            builder = createHttpClientBuilder();

            // this factory is required later
            // to be sure this is done, we do it outside the createHttpClient() call
            final RegistryBuilder<CookieSpecProvider> registeryBuilder
                = RegistryBuilder.<CookieSpecProvider>create()
                            .register(HACKED_COOKIE_POLICY, htmlUnitCookieSpecProvider_);
            builder.setDefaultCookieSpecRegistry(registeryBuilder.build());

            builder.setDefaultCookieStore(new HtmlUnitCookieStore(webClient_.getCookieManager()));
            builder.setUserAgent(webClient_.getBrowserVersion().getUserAgent());
            httpClientBuilder_.put(currentThread, builder);
        }

        return builder;
    }

    /**
     * Returns the timeout to use for socket and connection timeouts for HttpConnectionManager.
     * Is overridden to 0 by StreamingWebConnection which keeps reading after a timeout and
     * must have long running connections explicitly terminated.
     * @param webRequest the request might have his own timeout
     * @return the WebClient's timeout
     */
    protected int getTimeout(final WebRequest webRequest) {
        if (webRequest == null || webRequest.getTimeout() < 0) {
            return webClient_.getOptions().getTimeout();
        }

        return webRequest.getTimeout();
    }

    /**
     * Creates the <tt>HttpClientBuilder</tt> that will be used by this WebClient.
     * Extensions may override this method in order to create a customized
     * <tt>HttpClientBuilder</tt> instance (e.g. with a custom
     * {@link org.apache.http.conn.ClientConnectionManager} to perform
     * some tracking; see feature request 1438216).
     * @return the <tt>HttpClientBuilder</tt> that will be used by this WebConnection
     */
    protected HttpClientBuilder createHttpClientBuilder() {
        final HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setRedirectStrategy(new HtmlUnitRedirectStrategie());
        configureTimeout(builder, getTimeout(null));
        configureHttpsScheme(builder);
        builder.setMaxConnPerRoute(6);

        builder.setConnectionManagerShared(true);
        return builder;
    }

    private void configureTimeout(final HttpClientBuilder builder, final int timeout) {
        final InetAddress localAddress = webClient_.getOptions().getLocalAddress();
        final RequestConfig.Builder requestBuilder = createRequestConfigBuilder(timeout, localAddress);
        builder.setDefaultRequestConfig(requestBuilder.build());

        builder.setDefaultSocketConfig(createSocketConfigBuilder(timeout).build());

        getHttpContext().removeAttribute(HttpClientContext.REQUEST_CONFIG);
        usedOptions_.setTimeout(timeout);
    }

    private static RequestConfig.Builder createRequestConfigBuilder(final int timeout, final InetAddress localAddress) {
        final RequestConfig.Builder requestBuilder = RequestConfig.custom()
                .setCookieSpec(HACKED_COOKIE_POLICY)
                .setRedirectsEnabled(false)
                .setLocalAddress(localAddress)

                // timeout
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout);
        return requestBuilder;
    }

    private static SocketConfig.Builder createSocketConfigBuilder(final int timeout) {
        final SocketConfig.Builder socketBuilder = SocketConfig.custom()
                // timeout
                .setSoTimeout(timeout);
        return socketBuilder;
    }

    /**
     * React on changes that may have occurred on the WebClient settings.
     * Registering as a listener would be probably better.
     */
    private HttpClientBuilder reconfigureHttpClientIfNeeded(final HttpClientBuilder httpClientBuilder,
            final WebRequest webRequest) {
        final WebClientOptions options = webClient_.getOptions();

        // register new SSL factory only if settings have changed
        if (options.isUseInsecureSSL() != usedOptions_.isUseInsecureSSL()
                || options.getSSLClientCertificateStore() != usedOptions_.getSSLClientCertificateStore()
                || options.getSSLTrustStore() != usedOptions_.getSSLTrustStore()
                || options.getSSLClientCipherSuites() != usedOptions_.getSSLClientCipherSuites()
                || options.getSSLClientProtocols() != usedOptions_.getSSLClientProtocols()
                || options.getProxyConfig() != usedOptions_.getProxyConfig()) {
            configureHttpsScheme(httpClientBuilder);

            if (connectionManager_ != null) {
                connectionManager_.shutdown();
                connectionManager_ = null;
            }
        }

        final int timeout = getTimeout(webRequest);
        if (timeout != usedOptions_.getTimeout()) {
            configureTimeout(httpClientBuilder, timeout);
        }

        final long connectionTimeToLive = webClient_.getOptions().getConnectionTimeToLive();
        if (connectionTimeToLive != usedOptions_.getConnectionTimeToLive()) {
            httpClientBuilder.setConnectionTimeToLive(connectionTimeToLive, TimeUnit.MILLISECONDS);
            usedOptions_.setConnectionTimeToLive(connectionTimeToLive);
        }

        if (connectionManager_ == null) {
            connectionManager_ = createConnectionManager(httpClientBuilder);
        }
        httpClientBuilder.setConnectionManager(connectionManager_);

        return httpClientBuilder;
    }

    private void configureHttpsScheme(final HttpClientBuilder builder) {
        final WebClientOptions options = webClient_.getOptions();

        final SSLConnectionSocketFactory socketFactory =
                HtmlUnitSSLConnectionSocketFactory.buildSSLSocketFactory(options);

        builder.setSSLSocketFactory(socketFactory);

        usedOptions_.setUseInsecureSSL(options.isUseInsecureSSL());
        usedOptions_.setSSLClientCertificateStore(options.getSSLClientCertificateStore());
        usedOptions_.setSSLTrustStore(options.getSSLTrustStore());
        usedOptions_.setSSLClientCipherSuites(options.getSSLClientCipherSuites());
        usedOptions_.setSSLClientProtocols(options.getSSLClientProtocols());
        usedOptions_.setProxyConfig(options.getProxyConfig());
    }

    private void configureHttpProcessorBuilder(final HttpClientBuilder builder, final WebRequest webRequest) {
        final HttpProcessorBuilder b = HttpProcessorBuilder.create();
        for (final HttpRequestInterceptor i : getHttpRequestInterceptors(webRequest)) {
            b.add(i);
        }

        // These are the headers used in HttpClientBuilder, excluding the already added ones
        // (RequestClientConnControl and RequestAddCookies)
        b.addAll(new RequestDefaultHeaders(null),
                new RequestContent(),
                new RequestTargetHost(),
                new RequestExpectContinue());
        b.add(new RequestAcceptEncoding());
        b.add(new RequestAuthCache());
        b.add(new ResponseProcessCookies());
        builder.setHttpProcessor(b.build());
    }

    /**
     * Sets the virtual host.
     * @param virtualHost the virtualHost to set
     */
    public void setVirtualHost(final String virtualHost) {
        virtualHost_ = virtualHost;
    }

    /**
     * Gets the virtual host.
     * @return virtualHost The current virtualHost
     */
    public String getVirtualHost() {
        return virtualHost_;
    }

    /**
     * Converts an HttpMethod into a WebResponse.
     */
    private WebResponse makeWebResponse(final HttpResponse httpResponse,
            final WebRequest webRequest, final DownloadedContent responseBody, final long loadTime) {

        String statusMessage = httpResponse.getStatusLine().getReasonPhrase();
        if (statusMessage == null) {
            statusMessage = "Unknown status message";
        }
        final int statusCode = httpResponse.getStatusLine().getStatusCode();
        final List<NameValuePair> headers = new ArrayList<>();
        for (final Header header : httpResponse.getAllHeaders()) {
            headers.add(new NameValuePair(header.getName(), header.getValue()));
        }
        final WebResponseData responseData = new WebResponseData(responseBody, statusCode, statusMessage, headers);
        return newWebResponseInstance(responseData, loadTime, webRequest);
    }

    /**
     * Downloads the response body.
     * @param httpResponse the web server's response
     * @return a wrapper for the downloaded body.
     * @throws IOException in case of problem reading/saving the body
     */
    protected DownloadedContent downloadResponseBody(final HttpResponse httpResponse) throws IOException {
        final HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity == null) {
            return new DownloadedContent.InMemory(null);
        }

        try (InputStream is = httpEntity.getContent()) {
            return downloadContent(is, webClient_.getOptions().getMaxInMemory());
        }
    }

    /**
     * Reads the content of the stream and saves it in memory or on the file system.
     * @param is the stream to read
     * @param maxInMemory the maximumBytes to store in memory, after which save to a local file
     * @return a wrapper around the downloaded content
     * @throws IOException in case of read issues
     */
    public static DownloadedContent downloadContent(final InputStream is, final int maxInMemory) throws IOException {
        if (is == null) {
            return new DownloadedContent.InMemory(null);
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[1024];
            int nbRead;
            try {
                while ((nbRead = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, nbRead);
                    if (bos.size() > maxInMemory) {
                        // we have exceeded the max for memory, let's write everything to a temporary file
                        final File file = File.createTempFile("htmlunit", ".tmp");
                        file.deleteOnExit();
                        try (OutputStream fos = Files.newOutputStream(file.toPath())) {
                            bos.writeTo(fos); // what we have already read
                            IOUtils.copyLarge(is, fos); // what remains from the server response
                        }
                        return new DownloadedContent.OnFile(file, true);
                    }
                }
            }
            catch (final ConnectionClosedException e) {
                LOG.warn("Connection was closed while reading from stream.", e);
                return new DownloadedContent.InMemory(bos.toByteArray());
            }
            catch (final EOFException e) {
                // this might happen with broken gzip content
                LOG.warn("EOFException while reading from stream.", e);
                return new DownloadedContent.InMemory(bos.toByteArray());
            }

            return new DownloadedContent.InMemory(bos.toByteArray());
        }
    }

    /**
     * Constructs an appropriate WebResponse.
     * May be overridden by subclasses to return a specialized WebResponse.
     * @param responseData Data that was send back
     * @param webRequest the request used to get this response
     * @param loadTime How long the response took to be sent
     * @return the new WebResponse
     */
    protected WebResponse newWebResponseInstance(
            final WebResponseData responseData,
            final long loadTime,
            final WebRequest webRequest) {
        return new WebResponse(responseData, webRequest, loadTime);
    }

    private List<HttpRequestInterceptor> getHttpRequestInterceptors(final WebRequest webRequest) {
        final List<HttpRequestInterceptor> list = new ArrayList<>();
        final Map<String, String> requestHeaders = webRequest.getAdditionalHeaders();
        final URL url = webRequest.getUrl();
        final StringBuilder host = new StringBuilder(url.getHost());

        final int port = url.getPort();
        if (port > 0 && port != url.getDefaultPort()) {
            host.append(':');
            host.append(Integer.toString(port));
        }

        // make sure the headers are added in the right order
        final String[] headerNames = webClient_.getBrowserVersion().getHeaderNamesOrdered();
        for (final String header : headerNames) {
            if (HttpHeader.HOST.equals(header)) {
                list.add(new HostHeaderHttpRequestInterceptor(host.toString()));
            }
            else if (HttpHeader.USER_AGENT.equals(header)) {
                String headerValue = webRequest.getAdditionalHeader(HttpHeader.USER_AGENT);
                if (headerValue == null) {
                    headerValue = webClient_.getBrowserVersion().getUserAgent();
                }
                list.add(new UserAgentHeaderHttpRequestInterceptor(headerValue));
            }
            else if (HttpHeader.ACCEPT.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.ACCEPT);
                if (headerValue != null) {
                    list.add(new AcceptHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.ACCEPT_LANGUAGE.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE);
                if (headerValue != null) {
                    list.add(new AcceptLanguageHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.ACCEPT_ENCODING.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.ACCEPT_ENCODING);
                if (headerValue != null) {
                    list.add(new AcceptEncodingHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.SEC_FETCH_DEST.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.SEC_FETCH_DEST);
                if (headerValue != null) {
                    list.add(new SecFetchDestHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.SEC_FETCH_MODE.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.SEC_FETCH_MODE);
                if (headerValue != null) {
                    list.add(new SecFetchModeHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.SEC_FETCH_SITE.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.SEC_FETCH_SITE);
                if (headerValue != null) {
                    list.add(new SecFetchSiteHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.SEC_FETCH_USER.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.SEC_FETCH_USER);
                if (headerValue != null) {
                    list.add(new SecFetchUserHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.UPGRADE_INSECURE_REQUESTS.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.UPGRADE_INSECURE_REQUESTS);
                if (headerValue != null) {
                    list.add(new UpgradeInsecureRequestHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.REFERER.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.REFERER);
                if (headerValue != null) {
                    list.add(new RefererHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.CONNECTION.equals(header)) {
                list.add(new RequestClientConnControl());
            }
            else if (HttpHeader.COOKIE.equals(header)) {
                list.add(new RequestAddCookies());
            }
            else if (HttpHeader.DNT.equals(header) && webClient_.getOptions().isDoNotTrackEnabled()) {
                list.add(new DntHeaderHttpRequestInterceptor("1"));
            }
        }

        // not all browser versions have DNT by default as part of getHeaderNamesOrdered()
        // so we add it again, in case
        if (webClient_.getOptions().isDoNotTrackEnabled()) {
            list.add(new DntHeaderHttpRequestInterceptor("1"));
        }

        synchronized (requestHeaders) {
            list.add(new MultiHttpRequestInterceptor(new HashMap<>(requestHeaders)));
        }
        return list;
    }

    /** We must have a separate class per header, because of org.apache.http.protocol.ChainBuilder. */
    private static final class HostHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        HostHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.HOST, value_);
        }
    }

    private static final class UserAgentHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        UserAgentHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.USER_AGENT, value_);
        }
    }

    private static final class AcceptHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        AcceptHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.ACCEPT, value_);
        }
    }

    private static final class AcceptLanguageHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        AcceptLanguageHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.ACCEPT_LANGUAGE, value_);
        }
    }

    private static final class UpgradeInsecureRequestHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        UpgradeInsecureRequestHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.UPGRADE_INSECURE_REQUESTS, value_);
        }
    }

    private static final class AcceptEncodingHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        AcceptEncodingHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader("Accept-Encoding", value_);
        }
    }

    private static final class RefererHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        RefererHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.REFERER, value_);
        }
    }

    private static final class DntHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        DntHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.DNT, value_);
        }
    }

    private static final class SecFetchModeHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecFetchModeHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_FETCH_MODE, value_);
        }
    }

    private static final class SecFetchSiteHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecFetchSiteHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_FETCH_SITE, value_);
        }
    }

    private static final class SecFetchUserHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecFetchUserHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_FETCH_USER, value_);
        }
    }

    private static final class SecFetchDestHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecFetchDestHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_FETCH_DEST, value_);
        }
    }

    private static class MultiHttpRequestInterceptor implements HttpRequestInterceptor {
        private final Map<String, String> map_;

        MultiHttpRequestInterceptor(final Map<String, String> map) {
            map_ = map;
        }

        @Override
        public void process(final HttpRequest request, final HttpContext context)
            throws HttpException, IOException {
            for (final Map.Entry<String, String> entry : map_.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * An authentication cache that is synchronized.
     */
    private static final class SynchronizedAuthCache extends BasicAuthCache {

        SynchronizedAuthCache() {
        }

        @Override
        public synchronized void put(final HttpHost host, final AuthScheme authScheme) {
            super.put(host, authScheme);
        }

        @Override
        public synchronized AuthScheme get(final HttpHost host) {
            return super.get(host);
        }

        @Override
        public synchronized void remove(final HttpHost host) {
            super.remove(host);
        }

        @Override
        public synchronized void clear() {
            super.clear();
        }

        @Override
        public synchronized String toString() {
            return super.toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        httpClientBuilder_.clear();

        if (connectionManager_ != null) {
            connectionManager_.shutdown();
            connectionManager_ = null;
        }
    }

    /**
     * Has the exact logic in {@link HttpClientBuilder#build()} which sets the {@code connManager} part,
     * but with the ability to configure {@code socketFactory}.
     */
    private static PoolingHttpClientConnectionManager createConnectionManager(final HttpClientBuilder builder) {
        try {
            PublicSuffixMatcher publicSuffixMatcher = getField(builder, "publicSuffixMatcher");
            if (publicSuffixMatcher == null) {
                publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();
            }

            LayeredConnectionSocketFactory sslSocketFactory = getField(builder, "sslSocketFactory");
            final SocketConfig defaultSocketConfig = getField(builder, "defaultSocketConfig");
            final ConnectionConfig defaultConnectionConfig = getField(builder, "defaultConnectionConfig");
            final boolean systemProperties = getField(builder, "systemProperties");
            final int maxConnTotal = getField(builder, "maxConnTotal");
            final int maxConnPerRoute = getField(builder, "maxConnPerRoute");
            HostnameVerifier hostnameVerifier = getField(builder, "hostnameVerifier");
            final SSLContext sslcontext = getField(builder, "sslContext");
            final DnsResolver dnsResolver = getField(builder, "dnsResolver");
            final long connTimeToLive = getField(builder, "connTimeToLive");
            final TimeUnit connTimeToLiveTimeUnit = getField(builder, "connTimeToLiveTimeUnit");

            if (sslSocketFactory == null) {
                final String[] supportedProtocols = systemProperties
                        ? split(System.getProperty("https.protocols")) : null;
                final String[] supportedCipherSuites = systemProperties
                        ? split(System.getProperty("https.cipherSuites")) : null;
                if (hostnameVerifier == null) {
                    hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
                }
                if (sslcontext == null) {
                    if (systemProperties) {
                        sslSocketFactory = new SSLConnectionSocketFactory(
                                (SSLSocketFactory) SSLSocketFactory.getDefault(),
                                supportedProtocols, supportedCipherSuites, hostnameVerifier);
                    }
                    else {
                        sslSocketFactory = new SSLConnectionSocketFactory(
                                SSLContexts.createDefault(),
                                hostnameVerifier);
                    }
                }
                else {
                    sslSocketFactory = new SSLConnectionSocketFactory(
                            sslcontext, supportedProtocols, supportedCipherSuites, hostnameVerifier);
                }
            }

            final PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", new SocksConnectionSocketFactory())
                        .register("https", sslSocketFactory)
                        .build(),
                        null,
                        null,
                        dnsResolver,
                        connTimeToLive,
                        connTimeToLiveTimeUnit != null ? connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
            if (defaultSocketConfig != null) {
                poolingmgr.setDefaultSocketConfig(defaultSocketConfig);
            }
            if (defaultConnectionConfig != null) {
                poolingmgr.setDefaultConnectionConfig(defaultConnectionConfig);
            }
            if (systemProperties) {
                String s = System.getProperty("http.keepAlive", "true");
                if ("true".equalsIgnoreCase(s)) {
                    s = System.getProperty("http.maxConnections", "5");
                    final int max = Integer.parseInt(s);
                    poolingmgr.setDefaultMaxPerRoute(max);
                    poolingmgr.setMaxTotal(2 * max);
                }
            }
            if (maxConnTotal > 0) {
                poolingmgr.setMaxTotal(maxConnTotal);
            }
            if (maxConnPerRoute > 0) {
                poolingmgr.setDefaultMaxPerRoute(maxConnPerRoute);
            }
            return poolingmgr;
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] split(final String s) {
        if (TextUtils.isBlank(s)) {
            return null;
        }
        return s.split(" *, *");
    }

    @SuppressWarnings("unchecked")
    private static <T> T getField(final Object target, final String fieldName) throws IllegalAccessException {
        return (T) FieldUtils.readDeclaredField(target, fieldName, true);
    }
}
