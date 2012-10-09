/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLPeerUnverifiedException;

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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookiePathComparator;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicPathHandler;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

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
 */
public class HttpWebConnection implements WebConnection {

    private static final String HACKED_COOKIE_POLICY = "mine";
    private AbstractHttpClient httpClient_;
    private final WebClient webClient_;

    /** Use single HttpContext, so there is no need to re-send authentication for each and every request. */
    private HttpContext httpContext_ = new BasicHttpContext();
    private String virtualHost_;
    private final CookieSpecFactory htmlUnitCookieSpecFactory_;
    private final WebClientOptions usedOptions_ = new WebClientOptions();

    /**
     * Creates a new HTTP web connection instance.
     * @param webClient the WebClient that is using this connection
     */
    public HttpWebConnection(final WebClient webClient) {
        webClient_ = webClient;
        htmlUnitCookieSpecFactory_ = new CookieSpecFactory() {
            public CookieSpec newInstance(final HttpParams params) {
                return new HtmlUnitBrowserCompatCookieSpec(webClient_.getIncorrectnessListener());
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public WebResponse getResponse(final WebRequest request) throws IOException {
        final URL url = request.getUrl();
        final AbstractHttpClient httpClient = getHttpClient();

        HttpUriRequest httpMethod = null;
        try {
            try {
                httpMethod = makeHttpMethod(request);
            }
            catch (final URISyntaxException e) {
                throw new IOException("Unable to create URI from URL: " + url.toExternalForm()
                        + " (reason: " + e.getMessage() + ")");
            }
            final HttpHost hostConfiguration = getHostConfiguration(request);
            setProxy(httpClient, request);
            final long startTime = System.currentTimeMillis();

            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(hostConfiguration, httpMethod, httpContext_);
            }
            catch (final SSLPeerUnverifiedException s) {
                // Try to use only SSLv3 instead
                if (webClient_.getOptions().isUseInsecureSSL()) {
                    HtmlUnitSSLSocketFactory.setUseSSL3Only(getHttpClient().getParams(), true);
                    httpResponse = httpClient.execute(hostConfiguration, httpMethod);
                }
                else {
                    throw s;
                }
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
        final HttpHost hostConfiguration = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());

        return hostConfiguration;
    }

    private static void setProxy(final HttpClient httpClient, final WebRequest webRequest) {
        if (webRequest.getProxyHost() != null) {
            final HttpHost proxy = new HttpHost(webRequest.getProxyHost(), webRequest.getProxyPort());
            final HttpParams httpClientParams = httpClient.getParams();
            if (webRequest.isSocksProxy()) {
                SocksSocketFactory.setSocksProxy(httpClientParams, proxy);
            }
            else {
                httpClientParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            }
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
        final URL url = UrlUtils.encodeUrl(webRequest.getUrl(), false);

        final String charset = webRequest.getCharset();
        // URIUtils.createURI is deprecated but as of httpclient-4.2.1, URIBuilder doesn't work here as it encodes path
        // what shouldn't happen here
        URI uri = URIUtils.createURI(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(),
                url.getQuery(), null);
        final HttpRequestBase httpMethod = buildHttpMethod(webRequest.getHttpMethod(), uri);
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
                final StringBuilder boundary = new StringBuilder();
                boundary.append("---------------------------");
                final Random rand = new Random();
                final char[] chars = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                for (int i = 0; i < 14; i++) {
                    boundary.append(chars[rand.nextInt(chars.length)]);
                }
                final Charset c = getCharset(charset, webRequest.getRequestParameters());
                final MultipartEntity multipartEntity =
                    new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, boundary.toString(), c);

                for (final NameValuePair pair : webRequest.getRequestParameters()) {
                    if (pair instanceof KeyDataPair) {
                        final KeyDataPair pairWithFile = (KeyDataPair) pair;
                        final ContentBody contentBody = buildFilePart(pairWithFile);
                        multipartEntity.addPart(pair.getName(), contentBody);
                    }
                    else {
                        final StringBody stringBody =
                            new StringBody(pair.getValue(), Charset.forName(webRequest.getCharset()));
                        multipartEntity.addPart(pair.getName(), stringBody);
                    }
                }
                method.setEntity(multipartEntity);
            }
            else { // for instance a PUT request
                final String body = webRequest.getRequestBody();
                if (body != null) {
                    method.setEntity(new StringEntity(body, charset));
                }
            }
        }

        if (webClient_.getBrowserVersion().hasFeature(BrowserVersionFeatures.HTTP_HEADER_HOST_FIRST)) {
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

        final AbstractHttpClient httpClient = getHttpClient();

        reconfigureHttpClientIfNeeded(httpClient);

        // Tell the client where to get its credentials from
        // (it may have changed on the webClient since last call to getHttpClientFor(...))
        final CredentialsProvider credentialsProvider = webClient_.getCredentialsProvider();

        // if the used url contains credentials, we have to add this
        final Credentials requestUrlCredentials = webRequest.getUrlCredentials();
        if (null != requestUrlCredentials
                && webClient_.getBrowserVersion().hasFeature(BrowserVersionFeatures.URL_AUTH_CREDENTIALS)) {
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
        httpClient.setCredentialsProvider(credentialsProvider);

        if (webClient_.getCookieManager().isCookiesEnabled()) {
            // Cookies are enabled. Note that it's important that we enable single cookie headers,
            // for compatibility purposes.
            httpClient.getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER, Boolean.TRUE);
            httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, HACKED_COOKIE_POLICY);
            httpClient.setCookieStore(new HtmlUnitCookieStore(webClient_.getCookieManager()));
        }
        else {
            // Cookies are disabled.
            httpClient.setCookieStore(new CookieStore() {
                public void addCookie(final Cookie cookie) { }
                public void clear() { }
                public boolean clearExpired(final Date date) {
                    return false;
                }
                @SuppressWarnings("unchecked")
                public List<Cookie> getCookies() {
                    return Collections.EMPTY_LIST;
                }
            });
        }
        return httpMethod;
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

    ContentBody buildFilePart(final KeyDataPair pairWithFile) {
        String contentType = pairWithFile.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        final File file = pairWithFile.getFile();

        if (pairWithFile.getData() != null) {
            if (file == null) {
                return new InputStreamBody(
                        new ByteArrayInputStream(pairWithFile.getData()), contentType, pairWithFile.getValue());
            }

            if (webClient_.getBrowserVersion().hasFeature(
                BrowserVersionFeatures.HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH)) {
                return new InputStreamBody(
                        new ByteArrayInputStream(pairWithFile.getData()), contentType, file.getAbsolutePath());
            }

            return new InputStreamBody(
                    new ByteArrayInputStream(pairWithFile.getData()), contentType, file.getName());
        }

        if (file == null) {
            return new InputStreamBody(new ByteArrayInputStream(new byte[0]), contentType, pairWithFile.getValue()) {
                // Overridden in order not to have a chunked response.
                @Override
                public long getContentLength() {
                    return 0;
                }
            };
        }

        return new FileBody(pairWithFile.getFile(), contentType) {
            @Override
            public String getFilename() {
                if (getFile() == null) {
                    return pairWithFile.getValue();
                }
                else if (webClient_.getBrowserVersion().hasFeature(
                    BrowserVersionFeatures.HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH)) {
                    return getFile().getAbsolutePath();
                }
                else {
                    return super.getFilename();
                }
            }
        };
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
    protected synchronized AbstractHttpClient getHttpClient() {
        if (httpClient_ == null) {
            httpClient_ = createHttpClient();

            // this factory is required later
            // to be sure this is done, we do it outside the createHttpClient() call
            httpClient_.getCookieSpecs().register(HACKED_COOKIE_POLICY, htmlUnitCookieSpecFactory_);
        }

        return httpClient_;
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
    protected AbstractHttpClient createHttpClient() {
        final HttpParams httpParams = new BasicHttpParams();

        HttpClientParams.setRedirecting(httpParams, false);
        // Set timeouts
        configureTimeout(httpParams, webClient_.getOptions().getTimeout());

        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, new SocksSocketFactory()));
        configureHttpsScheme(schemeRegistry);

        final PoolingClientConnectionManager connectionManager =
            new PoolingClientConnectionManager(schemeRegistry);

        final DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);
        httpClient.setCookieStore(new HtmlUnitCookieStore(webClient_.getCookieManager()));

        httpClient.setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(final HttpRequest request, final HttpResponse response,
                    final HttpContext context) throws ProtocolException {
                return super.isRedirected(request, response, context)
                        && response.getFirstHeader("location") != null;
            }
        });

        if (getVirtualHost() != null) {
            httpClient.getParams().setParameter(ClientPNames.VIRTUAL_HOST, virtualHost_);
        }

        return httpClient;
    }

    private void configureTimeout(final HttpParams httpParams, final int timeout) {
        httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);

        usedOptions_.setTimeout(timeout);
    }

    /**
     * React on changes that may have occurred on the WebClient settings.
     * Registering as a listener would be probably better.
     */
    private void reconfigureHttpClientIfNeeded(final AbstractHttpClient httpClient) {
        final WebClientOptions options = webClient_.getOptions();

        // register new SSL factory only if settings have changed
        if (options.isUseInsecureSSL() != usedOptions_.isUseInsecureSSL()
                || options.getSSLClientCertificateUrl() != usedOptions_.getSSLClientCertificateUrl()) {
            configureHttpsScheme(httpClient.getConnectionManager().getSchemeRegistry());
        }

        if (options.getTimeout() != usedOptions_.getTimeout()) {
            configureTimeout(httpClient.getParams(), options.getTimeout());
        }
    }

    private void configureHttpsScheme(final SchemeRegistry schemeRegistry) {
        final WebClientOptions options = webClient_.getOptions();

        final SSLSocketFactory socketFactory = HtmlUnitSSLSocketFactory.buildSSLSocketFactory(options);

        schemeRegistry.register(new Scheme("https", 443, socketFactory));

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
        if (virtualHost_ != null) {
            getHttpClient().getParams().setParameter(ClientPNames.VIRTUAL_HOST, virtualHost_);
        }
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
     * Shutdown the connection manager.
     */
    public synchronized void shutdown() {
        if (httpClient_ != null) {
            httpClient_.getConnectionManager().shutdown();
            httpClient_ = null;
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

    private static final SimpleDateFormat INCORRECT_FORMAT = new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:ss z");
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
                final int length = value.length();
                if (value.endsWith("GMT") && length > 16 && !Character.isDigit(value.charAt(length - 16))) {
                    //add "19" prefix to the year
                    value = value.substring(0, length - 15) + "19" + value.substring(length - 15);
                    try {
                        INCORRECT_FORMAT.parse(value);
                        value = value.substring(0, 4) + ' ' + value.substring(4);
                    }
                    catch (final Exception e) {
                        //this is ok
                    }
                }
                try {
                    originalExpiresHandler.parse(cookie, value);
                }
                catch (final MalformedCookieException e) {
                    incorrectnessListener.notify("Incorrect cookie expiration time: " + value, this);
                }
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
 * @version $Revision$
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
