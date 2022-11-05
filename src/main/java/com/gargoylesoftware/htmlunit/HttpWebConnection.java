/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.client5.http.async.methods.SimpleBody;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthScheme;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.CredentialsStore;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.CookieSpecFactory;
import org.apache.hc.client5.http.entity.mime.InputStreamBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.protocol.RequestAddCookies;
import org.apache.hc.client5.http.protocol.RequestClientConnControl;
import org.apache.hc.client5.http.protocol.ResponseProcessCookies;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.function.Factory;
import org.apache.hc.core5.http.ConnectionClosedException;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.net.WWWFormCodec;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.reactor.ssl.TlsDetails;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import com.gargoylesoftware.htmlunit.WebRequest.HttpHint;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitCookieSpecProvider;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitCookieStore;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitRedirectStrategie;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitSSLConnectionSocketFactory;
import com.gargoylesoftware.htmlunit.httpclient.HttpClientConverter;
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
 * @author Joerg Werner
 */
public class HttpWebConnection implements WebConnection {

    private static final Log LOG = LogFactory.getLog(HttpWebConnection.class);

    private static final String HACKED_COOKIE_POLICY = "mine";

    // have one per thread because this is (re)configured for every call (see configureHttpProcessorBuilder)
    // do not use a ThreadLocal because this in only accessed form this class
    private final Map<Thread, HttpAsyncClientBuilder> httpClientBuilder_ = new WeakHashMap<>();
    private final WebClient webClient_;

    private String virtualHost_;
    private final CookieSpecFactory htmlUnitCookieSpecProvider_;
    private final WebClientOptions usedOptions_;
    private PoolingAsyncClientConnectionManager connectionManager_;

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
        final HttpAsyncClientBuilder builder = reconfigureHttpClientIfNeeded(getHttpClientBuilder(), webRequest);

        SimpleHttpRequest httpMethod = null;
        try {
            try {
                httpMethod = makeHttpMethod(webRequest, builder);
            }
            catch (final URISyntaxException e) {
                throw new IOException("Unable to create URI from URL: " + webRequest.getUrl().toExternalForm()
                        + " (reason: " + e.getMessage() + ")", e);
            }

            final long startTime = System.currentTimeMillis();

            final HttpContext httpContext = getHttpContext();
            HttpResponse httpResponse = null;
            try {
                try (CloseableHttpAsyncClient closeableHttpClient = builder.build()) {
                    httpResponse = execute(closeableHttpClient, httpMethod);
                }
            }
            catch (final SSLPeerUnverifiedException s) {
                // TODO: not sure if we can still support this
                // Try to use only SSLv3 instead
                if (webClient_.getOptions().isUseInsecureSSL()) {
                    HtmlUnitSSLConnectionSocketFactory.setUseSSL3Only(httpContext, true);
                    try (CloseableHttpAsyncClient closeableHttpClient = builder.build()) {
                        httpResponse = execute(closeableHttpClient, httpMethod);
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
     * Executes the asynchronous operation and waits for the result. This effectively
     * makes the asynchronous client working synchronously.
     * @param httpClient the http client instance
     * @param httpRequest the request
     * @return the response
     * @throws IOException if anything went wrongö
     */
    private SimpleHttpResponse execute(final CloseableHttpAsyncClient httpClient, final SimpleHttpRequest httpRequest)
        throws IOException {
        // first start the client if not done so far
        httpClient.start();

        final Future<SimpleHttpResponse> future =
            httpClient.execute(SimpleRequestProducer.create(httpRequest), SimpleResponseConsumer.create(), null);

        try {
            return future.get();
        }
        catch (final InterruptedException e) {
            throw new IOException(e);
        }
        catch (final ExecutionException e) {
            // throw the causing exception
            final Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            else if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            else {
                // need to wrap the exception so we can throw it
                throw new IOException(cause);
            }
        }
    }

    /**
     * Called when the response has been generated. Default action is to release
     * the HttpMethod's connection. Subclasses may override.
     * @param httpMethod the httpMethod used (can be null)
     */
    protected void onResponseGenerated(final SimpleHttpRequest httpMethod) {
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

    private void setProxy(final SimpleHttpRequest httpRequest, final WebRequest webRequest,
            final HttpAsyncClientBuilder clientBuilder) {
        final InetAddress localAddress = webClient_.getOptions().getLocalAddress();
        final RequestConfig.Builder requestBuilder = createRequestConfigBuilder(getTimeout(webRequest), localAddress);

        // clean potential settings made for a previous request
        clientBuilder.setIOReactorConfig(null);

        if (webRequest.getProxyHost() == null) {
            requestBuilder.setProxy(null);
            httpRequest.setConfig(requestBuilder.build());
            return;
        }

        final HttpHost proxy = new HttpHost(webRequest.getProxyScheme(), webRequest.getProxyHost(),
                                            webRequest.getProxyPort());
        if (webRequest.isSocksProxy()) {
            final IOReactorConfig.Builder configBuilder = IOReactorConfig.custom();
            configBuilder.setSocksProxyAddress(new InetSocketAddress(webRequest.getProxyHost(),
                    webRequest.getProxyPort()));
            final IOReactorConfig ioConfig = configBuilder.build();

            clientBuilder.setIOReactorConfig(ioConfig);
        }
        else {
            requestBuilder.setProxy(proxy);
            httpRequest.setConfig(requestBuilder.build());
        }
    }

    /**
     * Creates an <code>HttpMethod</code> instance according to the specified parameters.
     * @param webRequest the request
     * @param httpClientBuilder the httpClientBuilder that will be configured
     * @return the <code>HttpMethod</code> instance constructed according to the specified parameters
     * @throws URISyntaxException in case of syntax problems
     */
    private SimpleHttpRequest makeHttpMethod(final WebRequest webRequest,
            final HttpAsyncClientBuilder httpClientBuilder)
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
        final SimpleHttpRequest httpMethod = buildHttpMethod(webRequest.getHttpMethod(), uri);
        setProxy(httpMethod, webRequest, httpClientBuilder);

        // POST, PUT and PATCH
        if (StringUtils.equalsAny(httpMethod.getMethod(), Method.POST.name(), Method.PUT.name(), Method.PATCH.name())) {
            // developer note:
            // this has to be in sync with
            // com.gargoylesoftware.htmlunit.WebRequest.getRequestParameters()

            if (webRequest.getEncodingType()
                    == FormEncodingType.URL_ENCODED && httpMethod.getMethod().equals(Method.POST.name())) {
                if (webRequest.getRequestBody() == null) {
                    final List<NameValuePair> pairs = webRequest.getRequestParameters();
                    final String query = WWWFormCodec.format(
                            HttpClientConverter.nameValuePairsToHttpClient(pairs), charset);

                    final ContentType contentType;
                    if (webRequest.hasHint(HttpHint.IncludeCharsetInContentTypeHeader)) {
                        contentType = ContentType.APPLICATION_FORM_URLENCODED.withCharset(charset);
                    }
                    else {
                        contentType = ContentType.APPLICATION_FORM_URLENCODED.withCharset((Charset) null);
                    }
                    httpMethod.setBody(query, contentType);
                }
                else {
                    final String body = StringUtils.defaultString(webRequest.getRequestBody());
                    httpMethod.setBody(body, ContentType.APPLICATION_FORM_URLENCODED.withCharset(charset));
                }
            }
            else if (webRequest.getEncodingType()
                    == FormEncodingType.TEXT_PLAIN && httpMethod.getMethod().equals(Method.POST.name())) {
                if (webRequest.getRequestBody() == null) {
                    final StringBuilder body = new StringBuilder();
                    for (final NameValuePair pair : webRequest.getRequestParameters()) {
                        body.append(StringUtils.remove(StringUtils.remove(pair.getName(), '\r'), '\n'))
                            .append('=')
                            .append(StringUtils.remove(StringUtils.remove(pair.getValue(), '\r'), '\n'))
                            .append("\r\n");
                    }
                    httpMethod.setBody(body.toString(), ContentType.TEXT_PLAIN.withCharset(charset));
                }
                else {
                    final String body = StringUtils.defaultString(webRequest.getRequestBody());
                    httpMethod.setBody(body, ContentType.TEXT_PLAIN.withCharset(charset));
                }
            }
            else if (FormEncodingType.MULTIPART == webRequest.getEncodingType()) {
                final Charset c = getCharset(charset, webRequest.getRequestParameters());

                // TODO: There ought to be a way to create a multi-part body for the asynchronous client.
                // For now, use the classic classes.
                final MultipartEntityBuilder builder = MultipartEntityBuilder.create().setLaxMode();
                builder.setCharset(c);

                for (final NameValuePair pair : webRequest.getRequestParameters()) {
                    if (pair instanceof KeyDataPair) {
                        buildFilePart((KeyDataPair) pair, builder);
                    }
                    else {
                        builder.addTextBody(pair.getName(), pair.getValue(),
                                            ContentType.TEXT_PLAIN.withCharset(charset));
                    }
                }

                final HttpEntity entity = builder.build();

                // convert the classic multi-part entity to bytes
                final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try {
                    entity.writeTo(buffer);
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
                final byte[] body = buffer.toByteArray();

                httpMethod.setBody(body, ContentType.MULTIPART_FORM_DATA.withCharset(charset));
            }
            else { // for instance a PUT or PATCH request
                final String body = webRequest.getRequestBody();
                if (body != null) {
                    // TODO: can't set charset without a content type
                    //httpMethod.setBody(body, ContentType.create(body, charset);
                    httpMethod.setBody(body, null);
                }
            }
        }
        else {
            // this is the case for GET as well as TRACE, DELETE, OPTIONS and HEAD
            if (!webRequest.getRequestParameters().isEmpty()) {
                final List<NameValuePair> pairs = webRequest.getRequestParameters();
                final String query = WWWFormCodec.format(
                        HttpClientConverter.nameValuePairsToHttpClient(pairs), charset);
                uri = UrlUtils.toURI(url, query);
                httpMethod.setUri(uri);
            }
        }

        configureHttpProcessorBuilder(httpClientBuilder, webRequest);

        // Tell the client where to get its credentials from
        // (it may have changed on the webClient since last call to getHttpClientFor(...))
        final CredentialsStore credentialsProvider = webClient_.getCredentialsProvider();

        // if the used url contains credentials, we have to add this
        final Credentials requestUrlCredentials = webRequest.getUrlCredentials();
        if (null != requestUrlCredentials
                && webClient_.getBrowserVersion().hasFeature(URL_AUTH_CREDENTIALS)) {
            final URL requestUrl = webRequest.getUrl();
            final AuthScope authScope = new AuthScope(requestUrl.getHost(), requestUrl.getPort());
            // updating our client to keep the credentials for the next request
            credentialsProvider.setCredentials(authScope, requestUrlCredentials);
            // TODO: not sure if this is the correct way to invalidate a previous auth
            HttpClientContext.adapt(httpContext).getAuthExchanges().remove(HttpHost.create(uri));
            sharedAuthCache_.remove(HttpHost.create(uri));
        }

        // if someone has set credentials to this request, we have to add this
        final Credentials requestCredentials = webRequest.getCredentials();
        if (null != requestCredentials) {
            final URL requestUrl = webRequest.getUrl();
            final AuthScope authScope = new AuthScope(requestUrl.getHost(), requestUrl.getPort());
            // updating our client to keep the credentials for the next request
            credentialsProvider.setCredentials(authScope, requestCredentials);
            // TODO: not sure if this is the correct way to invalidate a previous auth
            HttpClientContext.adapt(httpContext).getAuthExchanges().remove(HttpHost.create(uri));
            sharedAuthCache_.remove(HttpHost.create(uri));
        }
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        httpContext.removeAttribute(HttpClientContext.CREDS_PROVIDER);

        // TODO: don't know how to replace this???
        //httpContext.removeAttribute(HttpClientContext.TARGET_AUTH_STATE);

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
    private static SimpleHttpRequest buildHttpMethod(final HttpMethod submitMethod, final URI uri) {
        final SimpleHttpRequest method;

        switch (submitMethod) {
            case GET:
                method = new SimpleHttpRequest(Method.GET, uri);
                break;

            case POST:
                method = new SimpleHttpRequest(Method.POST, uri);
                break;

            case PUT:
                method = new SimpleHttpRequest(Method.PUT, uri);
                break;

            case DELETE:
                method = new SimpleHttpRequest(Method.DELETE, uri);
                break;

            case OPTIONS:
                method = new SimpleHttpRequest(Method.OPTIONS, uri);
                break;

            case HEAD:
                method = new SimpleHttpRequest(Method.HEAD, uri);
                break;

            case TRACE:
                method = new SimpleHttpRequest(Method.TRACE, uri);
                break;

            case PATCH:
                method = new SimpleHttpRequest(Method.PATCH, uri);
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
    protected HttpAsyncClientBuilder getHttpClientBuilder() {
        final Thread currentThread = Thread.currentThread();
        HttpAsyncClientBuilder builder = httpClientBuilder_.get(currentThread);
        if (builder == null) {
            builder = createHttpClientBuilder();

            // this factory is required later
            // to be sure this is done, we do it outside the createHttpClient() call
            final RegistryBuilder<CookieSpecFactory> registeryBuilder
                = RegistryBuilder.<CookieSpecFactory>create()
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
     * Creates the <code>HttpClientBuilder</code> that will be used by this WebClient.
     * Extensions may override this method in order to create a customized
     * <code>HttpClientBuilder</code> instance (e.g. with a custom
     * {@link org.apache.http.conn.ClientConnectionManager} to perform
     * some tracking; see feature request 1438216).
     * @return the <code>HttpClientBuilder</code> that will be used by this WebConnection
     */
    protected HttpAsyncClientBuilder createHttpClientBuilder() {
        final HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();
        builder.setRedirectStrategy(new HtmlUnitRedirectStrategie());
        configureTimeout(builder, getTimeout(null));

        //builder.disableContentCompression();
        builder.disableRedirectHandling();
        //builder.setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1);

        builder.setConnectionManagerShared(true);
        return builder;
    }

    private void configureTimeout(final HttpAsyncClientBuilder builder, final int timeout) {
        final InetAddress localAddress = webClient_.getOptions().getLocalAddress();
        final RequestConfig.Builder requestBuilder = createRequestConfigBuilder(timeout, localAddress);
        builder.setDefaultRequestConfig(requestBuilder.build());

        // TODO:
        //builder.setDefaultSocketConfig(createSocketConfigBuilder(timeout).build());

        getHttpContext().removeAttribute(HttpClientContext.REQUEST_CONFIG);
        usedOptions_.setTimeout(timeout);
    }

    private static RequestConfig.Builder createRequestConfigBuilder(final int timeout, final InetAddress localAddress) {

        final Timeout timeoutMS = Timeout.ofMilliseconds(timeout);

        final RequestConfig.Builder requestBuilder = RequestConfig.custom()
                .setCookieSpec(HACKED_COOKIE_POLICY)
                .setRedirectsEnabled(false)

                // TODO: Has been moved elsewhere, but where? HttpRoutePlanner?
                //.setLocalAddress(localAddress)

                // timeout
                .setConnectTimeout(timeoutMS)
                .setConnectionRequestTimeout(timeoutMS)
                .setResponseTimeout(timeoutMS);
        return requestBuilder;
    }

    /**
     * Creates the <tt>PoolingHttpClientConnectionManagerBuilder</tt> that will be used by this WebClient.
     * Extensions may override this method in order to create a customized
     * <tt>PoolingHttpClientConnectionManagerBuilder</tt> instance.
     * @param options the options
     * @return the <tt>PoolingHttpClientConnectionManagerBuilder</tt> that will be used by this WebConnection
     */
    protected PoolingAsyncClientConnectionManagerBuilder createConnectionManagerBuilder(
            final WebClientOptions options) {
        final PoolingAsyncClientConnectionManagerBuilder builder = PoolingAsyncClientConnectionManagerBuilder.create();

        configureHttpsScheme(builder);

        final long connectionTimeToLive = options.getConnectionTimeToLive();
        if (connectionTimeToLive >= 0) {
            final TimeValue timeToLive = TimeValue.ofMilliseconds(connectionTimeToLive);
            builder.setConnectionTimeToLive(timeToLive);
        }

        builder.setMaxConnPerRoute(6);

        return builder;
    }

    // TODO: settings probably moved to IORecactorConfig?
    private static SocketConfig.Builder createSocketConfigBuilder(final int timeout) {
        final SocketConfig.Builder socketBuilder = SocketConfig.custom()
                // timeout
                .setSoTimeout(Timeout.ofMilliseconds(timeout));
        return socketBuilder;
    }

    /**
     * React on changes that may have occurred on the WebClient settings.
     * Registering as a listener would be probably better.
     */
    private HttpAsyncClientBuilder reconfigureHttpClientIfNeeded(final HttpAsyncClientBuilder httpClientBuilder,
            final WebRequest webRequest) {
        final WebClientOptions options = webClient_.getOptions();

//        // register new SSL factory only if settings have changed
//        if (options.isUseInsecureSSL() != usedOptions_.isUseInsecureSSL()
//                || options.getSSLClientCertificateStore() != usedOptions_.getSSLClientCertificateStore()
//                || options.getSSLTrustStore() != usedOptions_.getSSLTrustStore()
//                || options.getSSLClientCipherSuites() != usedOptions_.getSSLClientCipherSuites()
//                || options.getSSLClientProtocols() != usedOptions_.getSSLClientProtocols()
//                || options.getProxyConfig() != usedOptions_.getProxyConfig()) {
//            configureHttpsScheme(httpClientBuilder);
//
//            if (connectionManager_ != null) {
//                connectionManager_.close();
//                connectionManager_ = null;
//            }
//        }
//
        final int timeout = getTimeout(webRequest);
        if (timeout != usedOptions_.getTimeout()) {
            configureTimeout(httpClientBuilder, timeout);
        }

//        final long connectionTimeToLive = webClient_.getOptions().getConnectionTimeToLive();
//        if (connectionTimeToLive != usedOptions_.getConnectionTimeToLive()) {
//            // TODO
//            //httpClientBuilder.setConnectionTimeToLive(connectionTimeToLive, TimeUnit.MILLISECONDS);
//            usedOptions_.setConnectionTimeToLive(connectionTimeToLive);
//        }

        if (connectionManager_ == null) {
            connectionManager_ = createConnectionManagerBuilder(options).build();
        }
        httpClientBuilder.setConnectionManager(connectionManager_);

        return httpClientBuilder;
    }

    private void configureHttpsScheme(final PoolingAsyncClientConnectionManagerBuilder builder) {
        final WebClientOptions options = webClient_.getOptions();

        final TlsStrategy tlsStrategy = createTlsStrategyBuilder(options).build();
        builder.setTlsStrategy(tlsStrategy);

        usedOptions_.setUseInsecureSSL(options.isUseInsecureSSL());
        usedOptions_.setSSLClientCertificateStore(options.getSSLClientCertificateStore());
        usedOptions_.setSSLTrustStore(options.getSSLTrustStore());
        usedOptions_.setSSLClientCipherSuites(options.getSSLClientCipherSuites());
        usedOptions_.setSSLClientProtocols(options.getSSLClientProtocols());
        usedOptions_.setProxyConfig(options.getProxyConfig());
    }

    private void configureHttpProcessorBuilder(final HttpAsyncClientBuilder builder, final WebRequest webRequest) {

        // HttpProcessor is now created internally only, hence we have to configure
        // the request interceptors directly at the builder.

        // Disable certain features so the related request interceptors are not added by default.
        // We will add them on our own, in the order we want.
        builder.disableConnectionState();
        builder.disableCookieManagement();

        // first clear the current list of request interceptors at the builder
        try {
            // HACK: Seems there is no other way in the moment.
            setField(builder, "requestInterceptors", null);
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // fill the list of request interceptors anew, specifically for the web request
        final List<HttpRequestInterceptor> interceptors = getHttpRequestInterceptors(webRequest);

        // reverse the list so we can use addRequestInterceptorFirst() and get the wanted result
        Collections.reverse(interceptors);
        for (final HttpRequestInterceptor i : interceptors) {
            builder.addRequestInterceptorFirst(i);
        }

        builder.addResponseInterceptorFirst(new ResponseProcessCookies());
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

        String statusMessage = new StatusLine(httpResponse).getReasonPhrase();
        if (statusMessage == null) {
            statusMessage = "Unknown status message";
        }
        final int statusCode = httpResponse.getCode();
        final List<NameValuePair> headers = new ArrayList<>();
        for (final Header header : httpResponse.getHeaders()) {
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
//        final HttpEntity httpEntity = ((CloseableHttpResponse) httpResponse).getEntity();
//        if (httpEntity == null) {
//            return new DownloadedContent.InMemory(null);
//        }
//
//        try (InputStream is = httpEntity.getContent()) {
//            return downloadContent(is, webClient_.getOptions().getMaxInMemory());
//        }

        // TODO: replace SimpleHttpResponse with a streaming response
        final SimpleBody httpEntity = ((SimpleHttpResponse) httpResponse).getBody();
        if (httpEntity == null) {
            return new DownloadedContent.InMemory(null);
        }

        try (InputStream is = new ByteArrayInputStream(httpEntity.getBodyBytes())) {
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
            else if (HttpHeader.SEC_CH_UA.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.SEC_CH_UA);
                if (headerValue != null) {
                    list.add(new SecClientHintUserAgentHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.SEC_CH_UA_MOBILE.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.SEC_CH_UA_MOBILE);
                if (headerValue != null) {
                    list.add(new SecClientHintUserAgentMobileHeaderHttpRequestInterceptor(headerValue));
                }
            }
            else if (HttpHeader.SEC_CH_UA_PLATFORM.equals(header)) {
                final String headerValue = webRequest.getAdditionalHeader(HttpHeader.SEC_CH_UA_PLATFORM);
                if (headerValue != null) {
                    list.add(new SecClientHintUserAgentPlatformHeaderHttpRequestInterceptor(headerValue));
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
                // TODO: add this only if not HTTP/2
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
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.HOST, value_);
        }
    }

    private static final class UserAgentHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        UserAgentHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.USER_AGENT, value_);
        }
    }

    private static final class AcceptHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        AcceptHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.ACCEPT, value_);
        }
    }

    private static final class AcceptLanguageHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        AcceptLanguageHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.ACCEPT_LANGUAGE, value_);
        }
    }

    private static final class UpgradeInsecureRequestHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        UpgradeInsecureRequestHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.UPGRADE_INSECURE_REQUESTS, value_);
        }
    }

    private static final class AcceptEncodingHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        AcceptEncodingHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader("Accept-Encoding", value_);
        }
    }

    private static final class RefererHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        RefererHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.REFERER, value_);
        }
    }

    private static final class DntHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        DntHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.DNT, value_);
        }
    }

    private static final class SecFetchModeHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecFetchModeHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_FETCH_MODE, value_);
        }
    }

    private static final class SecFetchSiteHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecFetchSiteHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_FETCH_SITE, value_);
        }
    }

    private static final class SecFetchUserHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecFetchUserHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_FETCH_USER, value_);
        }
    }

    private static final class SecFetchDestHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecFetchDestHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_FETCH_DEST, value_);
        }
    }

    private static final class SecClientHintUserAgentHeaderHttpRequestInterceptor implements HttpRequestInterceptor {
        private final String value_;

        SecClientHintUserAgentHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_CH_UA, value_);
        }
    }

    private static final class SecClientHintUserAgentMobileHeaderHttpRequestInterceptor
            implements HttpRequestInterceptor {
        private final String value_;

        SecClientHintUserAgentMobileHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_CH_UA_MOBILE, value_);
        }
    }

    private static final class SecClientHintUserAgentPlatformHeaderHttpRequestInterceptor
            implements HttpRequestInterceptor {
        private final String value_;

        SecClientHintUserAgentPlatformHeaderHttpRequestInterceptor(final String value) {
            value_ = value;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
                throws HttpException, IOException {
            request.setHeader(HttpHeader.SEC_CH_UA_PLATFORM, value_);
        }
    }

    private static class MultiHttpRequestInterceptor implements HttpRequestInterceptor {
        private final Map<String, String> map_;

        MultiHttpRequestInterceptor(final Map<String, String> map) {
            map_ = map;
        }

        @Override
        public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context)
            throws HttpException, IOException {
            for (final Map.Entry<String, String> entry : map_.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * An authentication cache that is synchronized.
     */
    // TODO: Looks like BasicAuthCache is already thread-safe so we can remove this class?
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
            connectionManager_.close();
            connectionManager_ = null;
        }
    }

    /**
     * Creates a preconfigured builder for {@link TlsStrategy} objects. Sub classes
     * may override this method to add further customizations to the builder.
     * @param options the WebClient options
     * @return the builder
     */
    protected ClientTlsStrategyBuilder createTlsStrategyBuilder(final WebClientOptions options) {
        try {
            final String[] sslClientProtocols = options.getSSLClientProtocols();
            final String[] sslClientCipherSuites = options.getSSLClientCipherSuites();
            final boolean useInsecureSSL = options.isUseInsecureSSL();

            final SSLContext sslContext = createSslContextBuilder(options).build();

            final ClientTlsStrategyBuilder tlsStrategyBuilder = ClientTlsStrategyBuilder.create();
            tlsStrategyBuilder.setSslContext(sslContext);
            tlsStrategyBuilder.setCiphers(sslClientCipherSuites);
            tlsStrategyBuilder.setTlsVersions(sslClientProtocols);
            tlsStrategyBuilder.setHostnameVerifier(useInsecureSSL
                    ? NoopHostnameVerifier.INSTANCE : new DefaultHostnameVerifier());

            if (SystemUtils.isJavaVersionAtMost(JavaVersion.JAVA_9)) {
                // From the async HttpClient examples:

                // IMPORTANT uncomment the following method when running Java 9 or older
                // in order for ALPN support to work and avoid the illegal reflective
                // access operation warning
                tlsStrategyBuilder.setTlsDetailsFactory(new Factory<SSLEngine, TlsDetails>() {
                    @Override
                    public TlsDetails create(final SSLEngine sslEngine) {
                        return new TlsDetails(sslEngine.getSession(), sslEngine.getApplicationProtocol());
                    }
                });
            }

            return tlsStrategyBuilder;
        }
        catch (final GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a preconfigured builder for {@link SSLContext} objects. Sub classes
     * may override this method to add further customizations to the builder.
     * @param options the WebClient options
     * @return the builder
     */
    protected SSLContextBuilder createSslContextBuilder(final WebClientOptions options) {
        try {
            final SSLContextBuilder sslContextBuilder = SSLContexts.custom();

            // custom key store
            final KeyStore keyStore = options.getSSLClientCertificateStore();
            final char[] keyStorePassword = options.getSSLClientCertificatePassword();

            sslContextBuilder.loadKeyMaterial(keyStore, keyStorePassword);

            // custom trust store
            final boolean useInsecureSSL = options.isUseInsecureSSL();
            if (useInsecureSSL) {
                sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            }
            else {
                final KeyStore trustStore = options.getSSLTrustStore();
                sslContextBuilder.loadTrustMaterial(trustStore, null);
            }

            return sslContextBuilder;
        }
        catch (final GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getField(final Object target, final String fieldName) throws IllegalAccessException {
        return (T) FieldUtils.readDeclaredField(target, fieldName, true);
    }

    private static <T> void setField(final Object target, final String fieldName, final T value)
            throws IllegalAccessException {
        FieldUtils.writeDeclaredField(target, fieldName, value, true);
    }
}
