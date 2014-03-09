/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTTP_HEADER_HOST_FIRST;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.URL_AUTH_CREDENTIALS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookiePathComparator;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicPathHandler;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.TextUtils;

import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Default implementation of {@link WebConnection}, using the HttpClient library to perform HTTP requests.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Nicolas Belisle
 * @author Ronald Brill
 * @author John J Murdoch
 */
public class HttpWebConnection implements WebConnection {

    private static final String HACKED_COOKIE_POLICY = "mine";
    private HttpClientBuilder httpClientBuilder_;
    private final WebClient webClient_;

    /** Use single HttpContext, so there is no need to re-send authentication for each and every request. */
    private final HttpContext httpContext_;
    private String virtualHost_;
    private final CookieSpecProvider htmlUnitCookieSpecProvider_;
    private final WebClientOptions usedOptions_;

    /**
     * Creates a new HTTP web connection instance.
     * @param webClient the WebClient that is using this connection
     */
    public HttpWebConnection(final WebClient webClient) {
        webClient_ = webClient;
        htmlUnitCookieSpecProvider_ = new CookieSpecProvider() {
            @Override
            public CookieSpec create(final HttpContext context) {
                return new HtmlUnitBrowserCompatCookieSpec(webClient_.getIncorrectnessListener());
            }
        };
        httpContext_ = new HttpClientContext();
        usedOptions_ = new WebClientOptions();
    }

    /**
     * {@inheritDoc}
     */
    public WebResponse getResponse(final WebRequest request) throws IOException {
        final URL url = request.getUrl();
        final HttpClientBuilder builder = reconfigureHttpClientIfNeeded(getHttpClientBuilder());

        HtmlUnitHttpClientBuilder.configureConnectionManager(builder);

        HttpUriRequest httpMethod = null;
        try {
            try {
                httpMethod = makeHttpMethod(request);
            }
            catch (final URISyntaxException e) {
                throw new IOException("Unable to create URI from URL: " + url.toExternalForm()
                        + " (reason: " + e.getMessage() + ")", e);
            }
            final HttpHost hostConfiguration = getHostConfiguration(request);
//            setProxy(httpMethod, request);
            final long startTime = System.currentTimeMillis();

            HttpResponse httpResponse = null;
            try {
                httpResponse = builder.build().execute(hostConfiguration, httpMethod, httpContext_);
            }
            catch (final SSLPeerUnverifiedException s) {
                // Try to use only SSLv3 instead
                if (webClient_.getOptions().isUseInsecureSSL()) {
                    HtmlUnitSSLConnectionSocketFactory.setUseSSL3Only(httpContext_, true);
                    httpResponse = builder.build().execute(hostConfiguration, httpMethod);
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
                synchronized (this) {
                    httpClientBuilder_ = null;
                }
                throw e;
            }

            final DownloadedContent downloadedBody = downloadResponseBody(httpResponse);
            final long endTime = System.currentTimeMillis();
            return makeWebResponse(httpResponse, request, downloadedBody, endTime - startTime);
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
     * Returns a new HttpClient host configuration, initialized based on the specified request.
     * @param webRequest the request to use to initialize the returned host configuration
     * @return a new HttpClient host configuration, initialized based on the specified request
     */
    private static HttpHost getHostConfiguration(final WebRequest webRequest) {
        final URL url = webRequest.getUrl();
        return new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
    }

//    private static void setProxy(final HttpUriRequest httpUriRequest, final WebRequest webRequest) {
//        if (webRequest.getProxyHost() != null) {
//            final HttpHost proxy = new HttpHost(webRequest.getProxyHost(), webRequest.getProxyPort());
//            final HttpParams httpRequestParams = httpUriRequest.getParams();
//            if (webRequest.isSocksProxy()) {
//                SocksSocketFactory.setSocksProxy(httpRequestParams, proxy);
//            }
//            else {
//                httpRequestParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
//            }
//        }
//    }

    private void setProxy(final HttpRequestBase httpRequest, final WebRequest webRequest) {
        final RequestConfig.Builder requestBuilder = createRequestConfig();
        configureTimeout(requestBuilder, webClient_.getOptions().getTimeout());

        if (webRequest.getProxyHost() != null) {
            final HttpHost proxy = new HttpHost(webRequest.getProxyHost(), webRequest.getProxyPort());
            if (webRequest.isSocksProxy()) {
                SocksConnectionSocketFactory.setSocksProxy(httpContext_, proxy);
            }
            else {
                requestBuilder.setProxy(proxy);
                httpRequest.setConfig(requestBuilder.build());
            }
        }
        else {
            requestBuilder.setProxy(null);
            httpRequest.setConfig(requestBuilder.build());
        }
    }

    /**
     * Creates an <tt>HttpMethod</tt> instance according to the specified parameters.
     * @param webRequest the request
     * @return the <tt>HttpMethod</tt> instance constructed according to the specified parameters
     * @throws IOException
     * @throws URISyntaxException
     */
    @SuppressWarnings("deprecation")
    private HttpUriRequest makeHttpMethod(final WebRequest webRequest)
        throws IOException, URISyntaxException {

        // Make sure that the URL is fully encoded. IE actually sends some Unicode chars in request
        // URLs; because of this we allow some Unicode chars in URLs. However, at this point we're
        // handing things over the HttpClient, and HttpClient will blow up if we leave these Unicode
        // chars in the URL.
        final URL url = UrlUtils.encodeUrl(webRequest.getUrl(), false, webRequest.getCharset());

        final String charset = webRequest.getCharset();
        // URIUtils.createURI is deprecated but as of httpclient-4.2.1, URIBuilder doesn't work here as it encodes path
        // what shouldn't happen here
        URI uri = URIUtils.createURI(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(),
                escapeQuery(url.getQuery()), null);
        if (getVirtualHost() != null) {
            uri = URI.create(getVirtualHost());
        }
        final HttpRequestBase httpMethod = buildHttpMethod(webRequest.getHttpMethod(), uri);
        setProxy(httpMethod, webRequest);
        if (!(httpMethod instanceof HttpEntityEnclosingRequest)) {
            // this is the case for GET as well as TRACE, DELETE, OPTIONS and HEAD
            if (!webRequest.getRequestParameters().isEmpty()) {
                final List<NameValuePair> pairs = webRequest.getRequestParameters();
                final org.apache.http.NameValuePair[] httpClientPairs = NameValuePair.toHttpClient(pairs);
                final String query = URLEncodedUtils.format(Arrays.asList(httpClientPairs), charset);
                uri = URIUtils.createURI(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(), query, null);
                httpMethod.setURI(uri);
            }
        }
        else { // POST as well as PUT
            final HttpEntityEnclosingRequest method = (HttpEntityEnclosingRequest) httpMethod;

            if (webRequest.getEncodingType() == FormEncodingType.URL_ENCODED && method instanceof HttpPost) {
                final HttpPost postMethod = (HttpPost) method;
                if (webRequest.getRequestBody() == null) {
                    final List<NameValuePair> pairs = webRequest.getRequestParameters();
                    final org.apache.http.NameValuePair[] httpClientPairs = NameValuePair.toHttpClient(pairs);
                    final String query = URLEncodedUtils.format(Arrays.asList(httpClientPairs), charset);
                    final StringEntity urlEncodedEntity = new StringEntity(query, charset);
                    urlEncodedEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
                    postMethod.setEntity(urlEncodedEntity);
                }
                else {
                    final String body = StringUtils.defaultString(webRequest.getRequestBody());
                    final StringEntity urlEncodedEntity = new StringEntity(body, charset);
                    urlEncodedEntity.setContentType(URLEncodedUtils.CONTENT_TYPE);
                    postMethod.setEntity(urlEncodedEntity);
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
                                ContentType.create("text/plain", webRequest.getCharset()));
                    }
                }
                method.setEntity(builder.build());
            }
            else { // for instance a PUT request
                final String body = webRequest.getRequestBody();
                if (body != null) {
                    method.setEntity(new StringEntity(body, charset));
                }
            }
        }

        if (webClient_.getBrowserVersion().hasFeature(HTTP_HEADER_HOST_FIRST)) {
            final int port = webRequest.getUrl().getPort();
            final StringBuilder host = new StringBuilder(webRequest.getUrl().getHost());
            if (port != 80 && port > 0) {
                host.append(':');
                host.append(Integer.toString(port));
            }
            httpMethod.setHeader(new BasicHeader("Host", host.toString()));
        }
        httpMethod.setHeader(new BasicHeader("User-Agent", webClient_.getBrowserVersion().getUserAgent()));

        if (webClient_.getOptions().isDoNotTrackEnabled()) {
            httpMethod.setHeader(new BasicHeader("DNT", "1"));
        }

        writeRequestHeadersToHttpMethod(httpMethod, webRequest.getAdditionalHeaders());
//        getHttpClient().getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);

        final HttpClientBuilder httpClient = getHttpClientBuilder();

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
            httpContext_.removeAttribute(HttpClientContext.TARGET_AUTH_STATE);
        }

        // if someone has set credentials to this request, we have to add this
        final Credentials requestCredentials = webRequest.getCredentials();
        if (null != requestCredentials) {
            final URL requestUrl = webRequest.getUrl();
            final AuthScope authScope = new AuthScope(requestUrl.getHost(), requestUrl.getPort());
            // updating our client to keep the credentials for the next request
            credentialsProvider.setCredentials(authScope, requestCredentials);
            httpContext_.removeAttribute(HttpClientContext.TARGET_AUTH_STATE);
        }
        httpClient.setDefaultCredentialsProvider(credentialsProvider);
        httpContext_.removeAttribute(HttpClientContext.CREDS_PROVIDER);

        if (webClient_.getCookieManager().isCookiesEnabled()) {
            // Cookies are enabled. Note that it's important that we enable single cookie headers,
            // for compatibility purposes.
            httpClient.setDefaultCookieStore(new HtmlUnitCookieStore(webClient_.getCookieManager()));
        }
        else {
            // Cookies are disabled.
            httpClient.setDefaultCookieStore(new CookieStore() {
                public void addCookie(final Cookie cookie) { /* empty */ }
                public void clear() { /* empty */ }
                public boolean clearExpired(final Date date) {
                    return false;
                }
                public List<Cookie> getCookies() {
                    return Collections.<Cookie>emptyList();
                }
            });
        }
        httpContext_.removeAttribute(HttpClientContext.COOKIE_STORE);

        return httpMethod;
    }

    private String escapeQuery(final String query) {
        if (query == null) {
            return null;
        }
        return query.replace("%%", "%25%25");
    }

    private Charset getCharset(final String charset, final List<NameValuePair> pairs) {
        for (final NameValuePair pair : pairs) {
            if (pair instanceof KeyDataPair) {
                final KeyDataPair pairWithFile = (KeyDataPair) pair;
                if (pairWithFile.getData() == null && pairWithFile.getFile() != null) {
                    final String fileName = pairWithFile.getFile().getName();
                    for (int i = 0; i < fileName.length(); i++) {
                        if (fileName.codePointAt(i) > 127) {
                            return Charset.forName(charset);
                        }
                    }
                }
            }
        }
        return null;
    }

    void buildFilePart(final KeyDataPair pairWithFile, final MultipartEntityBuilder builder)
        throws IOException {
        String mimeType = pairWithFile.getMimeType();
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        final ContentType contentType = ContentType.create(mimeType, pairWithFile.getCharset());
        final File file = pairWithFile.getFile();

        if (pairWithFile.getData() != null) {
            final String filename;
            if (file == null) {
                filename = pairWithFile.getValue();
            }
            else if (webClient_.getBrowserVersion().hasFeature(HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH)) {
                filename = file.getAbsolutePath();
            }
            else {
                filename = file.getName();
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

        String filename;
        if (pairWithFile.getFile() == null) {
            filename = pairWithFile.getValue();
        }
        else if (webClient_.getBrowserVersion().hasFeature(HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH)) {
            filename = pairWithFile.getFile().getAbsolutePath();
        }
        else {
            filename = pairWithFile.getFile().getName();
        }
        filename = new String(filename.getBytes(pairWithFile.getCharset()));
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
    protected synchronized HttpClientBuilder getHttpClientBuilder() {
        if (httpClientBuilder_ == null) {
            httpClientBuilder_ = createHttpClient();

            // this factory is required later
            // to be sure this is done, we do it outside the createHttpClient() call
            final RegistryBuilder<CookieSpecProvider> registeryBuilder
                = RegistryBuilder.<CookieSpecProvider>create()
                    .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                    .register(CookieSpecs.STANDARD, new RFC2965SpecFactory())
                    .register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
                    .register(CookieSpecs.NETSCAPE, new NetscapeDraftSpecFactory())
                    .register(CookieSpecs.IGNORE_COOKIES, new IgnoreSpecFactory())
                    .register("rfc2109", new RFC2109SpecFactory())
                    .register("rfc2965", new RFC2965SpecFactory());

            registeryBuilder.register(HACKED_COOKIE_POLICY, htmlUnitCookieSpecProvider_);
            httpClientBuilder_.setDefaultCookieSpecRegistry(registeryBuilder.build());
        }

        return httpClientBuilder_;
    }

    /**
     * Returns the timeout to use for socket and connection timeouts for HttpConnectionManager.
     * Is overridden to 0 by StreamingWebConnection which keeps reading after a timeout and
     * must have long running connections explicitly terminated.
     * @return the WebClient's timeout
     */
    protected int getTimeout() {
        return webClient_.getOptions().getTimeout();
    }

    /**
     * Creates the <tt>HttpClient</tt> that will be used by this WebClient.
     * Extensions may override this method in order to create a customized
     * <tt>HttpClient</tt> instance (e.g. with a custom
     * {@link org.apache.http.conn.ClientConnectionManager} to perform
     * some tracking; see feature request 1438216).
     * @return the <tt>HttpClient</tt> that will be used by this WebConnection
     */
    protected HttpClientBuilder createHttpClient() {
        final HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(final HttpRequest request, final HttpResponse response,
                    final HttpContext context) throws ProtocolException {
                return super.isRedirected(request, response, context)
                        && response.getFirstHeader("location") != null;
            }
        });
        configureTimeout(builder, webClient_.getOptions().getTimeout());
        builder.setMaxConnPerRoute(6);
        return builder;
    }

    private void configureTimeout(final HttpClientBuilder builder, final int timeout) {
        final RequestConfig.Builder requestBuilder = createRequestConfig();
        configureTimeout(requestBuilder, timeout);

        builder.setDefaultRequestConfig(requestBuilder.build());
        httpContext_.removeAttribute(HttpClientContext.REQUEST_CONFIG);
        usedOptions_.setTimeout(timeout);
    }

    private RequestConfig.Builder createRequestConfig() {
        final RequestConfig.Builder requestBuilder = RequestConfig.custom()
                .setCookieSpec(HACKED_COOKIE_POLICY)
                .setRedirectsEnabled(false);
        return requestBuilder;
    }

    private void configureTimeout(final RequestConfig.Builder builder, final int timeout) {
        builder.setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout)
            .setSocketTimeout(timeout);
    }

    /**
     * React on changes that may have occurred on the WebClient settings.
     * Registering as a listener would be probably better.
     */
    private HttpClientBuilder reconfigureHttpClientIfNeeded(final HttpClientBuilder httpClientBuilder) {
        final WebClientOptions options = webClient_.getOptions();

        // register new SSL factory only if settings have changed
        if (options.isUseInsecureSSL() != usedOptions_.isUseInsecureSSL()
                || options.getSSLClientCertificateUrl() != usedOptions_.getSSLClientCertificateUrl()) {
            configureHttpsScheme(httpClientBuilder);
        }

        if (options.getTimeout() != usedOptions_.getTimeout()) {
            configureTimeout(httpClientBuilder, options.getTimeout());
        }
        return httpClientBuilder;
    }

    private void configureHttpsScheme(final HttpClientBuilder builder) {
        final WebClientOptions options = webClient_.getOptions();

        final SSLConnectionSocketFactory socketFactory =
                HtmlUnitSSLConnectionSocketFactory.buildSSLSocketFactory(options);

        builder.setSSLSocketFactory(socketFactory);

        usedOptions_.setUseInsecureSSL(options.isUseInsecureSSL());
        usedOptions_.setSSLClientCertificate(options.getSSLClientCertificateUrl(),
                options.getSSLClientCertificatePassword(), options.getSSLClientCertificateType());
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
            final WebRequest request, final DownloadedContent responseBody, final long loadTime) {

        String statusMessage = httpResponse.getStatusLine().getReasonPhrase();
        if (statusMessage == null) {
            statusMessage = "Unknown status message";
        }
        final int statusCode = httpResponse.getStatusLine().getStatusCode();
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        for (final Header header : httpResponse.getAllHeaders()) {
            headers.add(new NameValuePair(header.getName(), header.getValue()));
        }
        final WebResponseData responseData = new WebResponseData(responseBody, statusCode, statusMessage, headers);
        return newWebResponseInstance(responseData, loadTime, request);
    }

    private static final long MAX_IN_MEMORY = 500 * 1024;

    /**
     * Downloads the response body.
     * @param httpResponse the web server's response
     * @return a wrapper for the downloaded body.
     * @throws IOException in case of problem reading/saving the body
     */
    protected DownloadedContent downloadResponseBody(final HttpResponse httpResponse) throws IOException {
        final HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity == null) {
            return new DownloadedContent.InMemory(new byte[] {});
        }

        return downloadContent(httpEntity.getContent());
    }

    /**
     * Reads the content of the stream and saves it in memory or on the file system.
     * @param is the stream to read
     * @return a wrapper around the downloaded content
     * @throws IOException in case of read issues
     */
    public static DownloadedContent downloadContent(final InputStream is) throws IOException {
        if (is == null) {
            return new DownloadedContent.InMemory(new byte[] {});
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        final byte[] buffer = new byte[1024];
        int nbRead;
        try {
            while ((nbRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, nbRead);
                if (bos.size() > MAX_IN_MEMORY) {
                    // we have exceeded the max for memory, let's write everything to a temporary file
                    final File file = File.createTempFile("htmlunit", ".tmp");
                    file.deleteOnExit();
                    final FileOutputStream fos = new FileOutputStream(file);
                    bos.writeTo(fos); // what we have already read
                    IOUtils.copyLarge(is, fos); // what remains from the server response
                    fos.close();
                    return new DownloadedContent.OnFile(file, true);
                }
            }
        }
        finally {
            IOUtils.closeQuietly(is);
        }

        return new DownloadedContent.InMemory(bos.toByteArray());
    }

    /**
     * Constructs an appropriate WebResponse.
     * May be overridden by subclasses to return a specialized WebResponse.
     * @param responseData Data that was send back
     * @param request the request used to get this response
     * @param loadTime How long the response took to be sent
     * @return the new WebResponse
     */
    protected WebResponse newWebResponseInstance(
            final WebResponseData responseData,
            final long loadTime,
            final WebRequest request) {
        return new WebResponse(responseData, request, loadTime);
    }

    private static void writeRequestHeadersToHttpMethod(final HttpUriRequest httpMethod,
        final Map<String, String> requestHeaders) {
        synchronized (requestHeaders) {
            for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Shutdown the connection.
     */
    public synchronized void shutdown() {
        if (httpClientBuilder_ != null) {
            httpClientBuilder_ = null;
        }
    }
}

/**
 * Workaround for <a href="https://issues.apache.org/jira/browse/HTTPCLIENT-1006">HttpClient bug 1006</a>:
 * quotes are wrongly removed in cookie's values.
 */
class HtmlUnitBrowserCompatCookieSpec extends BrowserCompatSpec {
    /**
     * Comparator for sending cookies in right order.
     * See specification:
     * - RFC2109 (#4.3.4) http://www.ietf.org/rfc/rfc2109.txt
     * - RFC2965 (#3.3.4) http://www.ietf.org/rfc/rfc2965.txt http://www.ietf.org/rfc/rfc2109.txt
     */
    private static final Comparator<Cookie> COOKIE_COMPARATOR = new CookiePathComparator();

    private static final Date DATE_1_1_1970;

    static {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(DateUtils.GMT);
        calendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        DATE_1_1_1970 = calendar.getTime();
    }

    // simplified patterns from BrowserCompatSpec, with yy patterns before similar yyyy patterns
    private static final String[] DEFAULT_DATE_PATTERNS = new String[] {
        "EEE dd MMM yy HH mm ss zzz",
        "EEE dd MMM yyyy HH mm ss zzz",
        "EEE MMM d HH mm ss yyyy",
        "EEE dd MMM yy HH mm ss z ",
        "EEE dd MMM yyyy HH mm ss z ",
        "EEE dd MM yy HH mm ss z ",
        "EEE dd MM yyyy HH mm ss z ",
        "d/M/yyyy",
    };

    HtmlUnitBrowserCompatCookieSpec(final IncorrectnessListener incorrectnessListener) {
        super();
        final BasicPathHandler pathHandler = new BasicPathHandler() {
            @Override
            public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
                // nothing, browsers seem not to perform any validation
            }
        };
        registerAttribHandler(ClientCookie.PATH_ATTR, pathHandler);

        final CookieAttributeHandler originalExpiresHandler = getAttribHandler(ClientCookie.EXPIRES_ATTR);
        final CookieAttributeHandler wrapperExpiresHandler = new CookieAttributeHandler() {

            public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
                originalExpiresHandler.validate(cookie, origin);
            }

            public void parse(final SetCookie cookie, String value) throws MalformedCookieException {
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                value = value.replaceAll("[ ,:-]+", " ");
                cookie.setExpiryDate(DateUtils.parseDate(value, DEFAULT_DATE_PATTERNS, DATE_1_1_1970));
            }

            public boolean match(final Cookie cookie, final CookieOrigin origin) {
                return originalExpiresHandler.match(cookie, origin);
            }
        };
        registerAttribHandler(ClientCookie.EXPIRES_ATTR, wrapperExpiresHandler);

        final CookieAttributeHandler httpOnlyHandler = new CookieAttributeHandler() {
            public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
                // nothing
            }

            public void parse(final SetCookie cookie, final String value) throws MalformedCookieException {
                ((BasicClientCookie) cookie).setAttribute("httponly", "true");
            }

            public boolean match(final Cookie cookie, final CookieOrigin origin) {
                return true;
            }
        };
        registerAttribHandler("httponly", httpOnlyHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Cookie> parse(final Header header, final CookieOrigin origin) throws MalformedCookieException {
        final List<Cookie> cookies = super.parse(header, origin);
        for (final Cookie c : cookies) {
            // re-add quotes around value if parsing as incorrectly trimmed them
            if (header.getValue().contains(c.getName() + "=\"" + c.getValue())) {
                ((BasicClientCookie) c).setValue('"' + c.getValue() + '"');
            }
        }
        return cookies;
    }

    @Override
    public List<Header> formatCookies(final List<Cookie> cookies) {
        Collections.sort(cookies, COOKIE_COMPARATOR);

        return super.formatCookies(cookies);
    }
}

/**
 * Implementation of {@link CookieStore} like {@link org.apache.http.impl.client.BasicCookieStore}
 * BUT storing cookies in the order of addition.
 * @author Marc Guillemot
 */
class HtmlUnitCookieStore implements CookieStore, Serializable {
    private CookieManager manager_;

    HtmlUnitCookieStore(final CookieManager manager) {
        manager_ = manager;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void addCookie(final Cookie cookie) {
        manager_.addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(cookie));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized List<Cookie> getCookies() {
        if (manager_.isCookiesEnabled()) {
            return Arrays.asList(com.gargoylesoftware.htmlunit.util.Cookie.toHttpClient(manager_.getCookies()));
        }
        return Collections.<Cookie>emptyList();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean clearExpired(final Date date) {
        return manager_.clearExpired(date);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void clear() {
        manager_.clearCookies();
    }

}

/**
 * A helper class for configuring {@link HttpClientBuilder}.
 *
 * @author Ahmed Ashour
 */
final class HtmlUnitHttpClientBuilder {

    private HtmlUnitHttpClientBuilder() {
    }

    /**
     * Has the exact logic in HttpClientBuilder, but with the ability to configure
     * <code>socketFactory</code>.
     */
    public static void configureConnectionManager(final HttpClientBuilder builder) {
        final ConnectionSocketFactory socketFactory = new SocksConnectionSocketFactory();

        LayeredConnectionSocketFactory sslSocketFactory =
                (LayeredConnectionSocketFactory) getField(builder, "sslSocketFactory");
        final SocketConfig defaultSocketConfig = (SocketConfig) getField(builder, "defaultSocketConfig");
        final ConnectionConfig defaultConnectionConfig =
                (ConnectionConfig) getField(builder, "defaultConnectionConfig");
        final boolean systemProperties = (Boolean) getField(builder, "systemProperties");
        final int maxConnTotal = (Integer) getField(builder, "maxConnTotal");
        final int maxConnPerRoute = (Integer) getField(builder, "maxConnPerRoute");
        X509HostnameVerifier hostnameVerifier = (X509HostnameVerifier) getField(builder, "hostnameVerifier");
        final SSLContext sslcontext = (SSLContext) getField(builder, "sslcontext");

        if (sslSocketFactory == null) {
            final String[] supportedProtocols = systemProperties ? split(
                    System.getProperty("https.protocols")) : null;
            final String[] supportedCipherSuites = systemProperties ? split(
                    System.getProperty("https.cipherSuites")) : null;
            if (hostnameVerifier == null) {
                hostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
            }
            if (sslcontext != null) {
                sslSocketFactory = new SSLConnectionSocketFactory(
                        sslcontext, supportedProtocols, supportedCipherSuites, hostnameVerifier);
            }
            else {
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
        }

        final PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", socketFactory)
                    .register("https", sslSocketFactory)
                    .build());
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
        builder.setConnectionManager(poolingmgr);
    }

    private static String[] split(final String s) {
        if (TextUtils.isBlank(s)) {
            return null;
        }
        return s.split(" *, *");
    }

    private static Object getField(final HttpClientBuilder builder, final String fieldName) {
        try {
            final Field field = HttpClientBuilder.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(builder);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
