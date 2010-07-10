/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
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
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.util.CharsetUtil;

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
 */
public class HttpWebConnection implements WebConnection {

    private AbstractHttpClient httpClient_;
    private final WebClient webClient_;
    private String virtualHost_;

    /**
     * Creates a new HTTP web connection instance.
     * @param webClient the WebClient that is using this connection
     */
    public HttpWebConnection(final WebClient webClient) {
        webClient_ = webClient;
    }

    /**
     * {@inheritDoc}
     */
    public WebResponse getResponse(final WebRequest request) throws IOException {
        final URL url = request.getUrl();
        final HttpClient httpClient = getHttpClient();
        webClient_.getCookieManager().updateState(getHttpClient().getCookieStore());

        HttpUriRequest httpMethod = null;
        try {
            httpMethod = makeHttpMethod(request);
            final HttpHost hostConfiguration = getHostConfiguration(request);
            setProxy(httpClient, request);
            final long startTime = System.currentTimeMillis();
            final HttpResponse httpResponse = httpClient.execute(hostConfiguration, httpMethod);
            final DownloadedContent downloadedBody = downloadResponseBody(httpResponse);
            final long endTime = System.currentTimeMillis();
            webClient_.getCookieManager().updateFromState(getHttpClient().getCookieStore());
            return makeWebResponse(httpResponse, request, downloadedBody, endTime - startTime);
        }
        catch (final URISyntaxException e) {
            throw new IOException("Unable to create URI from URL: " + url.toExternalForm());
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
     * @throws IOException if the specified request contains an invalid URL
     */
    private static HttpHost getHostConfiguration(final WebRequest webRequest)
        throws IOException {
        final URL url = webRequest.getUrl();
        final HttpHost hostConfiguration = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        try {
            new URI(url.toExternalForm());
        }
        catch (final URISyntaxException e) {
            throw new IOException("Unable to create URI from URL: " + url.toExternalForm());
        }
        return hostConfiguration;
    }

    private static void setProxy(final HttpClient httpClient, final WebRequest webRequest) {
        if (webRequest.getProxyHost() != null) {
            final String proxyHost = webRequest.getProxyHost();
            final int proxyPort = webRequest.getProxyPort();
            final HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            if (webRequest.isSocksProxy()) {
                final SocksSocketFactory factory = (SocksSocketFactory)
                    httpClient.getConnectionManager().getSchemeRegistry().getScheme("http").getSocketFactory();
                factory.setSocksProxy(proxy);
            }
            else {
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
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
    private HttpUriRequest makeHttpMethod(final WebRequest webRequest)
        throws IOException, URISyntaxException {
        // Make sure that the URL is fully encoded. IE actually sends some Unicode chars in request
        // URLs; because of this we allow some Unicode chars in URLs. However, at this point we're
        // handing things over the HttpClient, and HttpClient will blow up if we leave these Unicode
        // chars in the URL.
        final URL url = UrlUtils.encodeUrl(webRequest.getUrl(), false);
        final String charset = webRequest.getCharset();
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
                final MultipartEntity multipartEntity =
                    new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, CharsetUtil.getCharset(charset));

                for (final NameValuePair pair : webRequest.getRequestParameters()) {
                    if (pair instanceof KeyDataPair) {
                        final KeyDataPair pairWithFile = (KeyDataPair) pair;
                        final ContentBody contentBody = buildFilePart(pairWithFile, charset);
                        multipartEntity.addPart(pair.getName(), contentBody);
                        fixBugContentType(multipartEntity, contentBody.getMimeType());
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

        if (!webClient_.getBrowserVersion().isIE()) {
            final int port = webRequest.getUrl().getPort();
            String host = webRequest.getUrl().getHost();
            if (port != 80 && port > 0) {
                host += ":" + port;
            }
            httpMethod.setHeader(new BasicHeader("Host", host));
        }
        httpMethod.setHeader(new BasicHeader("User-Agent", webClient_.getBrowserVersion().getUserAgent()));

        writeRequestHeadersToHttpMethod(httpMethod, webRequest.getAdditionalHeaders());
//        getHttpClient().getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);

        if (webRequest.getCredentialsProvider() != null) {
            getHttpClient().setCredentialsProvider(webRequest.getCredentialsProvider());
        }

        if (webClient_.getCookieManager().isCookiesEnabled()) {
            // Cookies are enabled. Note that it's important that we enable single cookie headers,
            // for compatibility purposes.
            getHttpClient().getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER, true);
            getHttpClient().getParams().setParameter(ClientPNames.COOKIE_POLICY, CookieManager.HTMLUNIT_COOKIE_POLICY);
        }
        else {
            // Cookies are disabled.
            getHttpClient().setCookieStore(new CookieStore() {
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

    /**
     * HttpClient doesn't send Content-Type header for file parts as it should.
     * See:https://issues.apache.org/jira/browse/HTTPCLIENT-960
     * This is a hack to fix it and it should be removed once the issue has been fixed.
     */
    private void fixBugContentType(final MultipartEntity multipartEntity, final String contentType) {
        // hack!!
        try {
            final java.lang.reflect.Field field = MultipartEntity.class.getDeclaredField("multipart");
            field.setAccessible(true);
            final HttpMultipart multipart = (HttpMultipart) field.get(multipartEntity);
            final BodyPart lastOne = multipart.getBodyParts().get(multipart.getBodyParts().size() - 1);
            final org.apache.james.mime4j.message.Header header = lastOne.getHeader();
            final Field cntDispHeader = header.getField(MIME.CONTENT_DISPOSITION);
            header.removeFields(MIME.CONTENT_DISPOSITION);
            final Field newCntDispHeader = new Field() {
                public String getBody() {
                    return cntDispHeader.getBody() + "\r\nContent-Type: " + contentType;
                }
                public String getName() {
                    return MIME.CONTENT_DISPOSITION;
                }
                public org.apache.james.mime4j.util.ByteSequence getRaw() {
                    throw new RuntimeException("No in the hack");
                }
            };
            header.addField(newCntDispHeader);
        }
        catch (final Exception e) {
            throw new RuntimeException("Hack to fix Content-Type submission failed", e);
        }
    }

    // FIXME Change signature ?
    ContentBody buildFilePart(final KeyDataPair pairWithFile, final String charset) throws FileNotFoundException {
        String contentType = pairWithFile.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        if (pairWithFile.getData() != null) {
            return new InputStreamBody(
                    new ByteArrayInputStream(pairWithFile.getData()), contentType, pairWithFile.getName());
        }
        else if (pairWithFile.getFile() == null) {
            return new InputStreamBody(new ByteArrayInputStream(new byte[0]), contentType, pairWithFile.getName());
        }

        return new FileBody(pairWithFile.getFile(), contentType) {
            @Override
            public  String getFilename() {
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
            httpClient_.setRedirectHandler(new DefaultRedirectHandler() {
                public boolean isRedirectRequested(final HttpResponse response,
                        final HttpContext context) {
                    return super.isRedirectRequested(response, context) && response.getFirstHeader("location") != null;
                }
            });

            httpClient_.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, getTimeout());
            httpClient_.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout());

            if (virtualHost_ != null) {
                httpClient_.getParams().setParameter(ClientPNames.VIRTUAL_HOST, virtualHost_);
            }

            final Scheme httpScheme = new Scheme("http", new SocksSocketFactory(), 80);
            httpClient_.getConnectionManager().getSchemeRegistry().register(httpScheme);
        }

        // Tell the client where to get its credentials from
        // (it may have changed on the webClient since last call to getHttpClientFor(...))
        httpClient_.setCredentialsProvider(webClient_.getCredentialsProvider());

        //Set timeouts
        httpClient_.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, webClient_.getTimeout());
        httpClient_.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, webClient_.getTimeout());

        return httpClient_;
    }

    /**
     * Returns the timeout to use for socket and connection timeouts for HttpConnectionManager.
     * Is overridden to 0 by StreamingWebConnection which keeps reading after a timeout and
     * must have long running connections explicitly terminated.
     * @return the WebClient's timeout
     */
    protected int getTimeout() {
        return webClient_.getTimeout();
    }

    /**
     * Creates the <tt>HttpClient</tt> that will be used by this WebClient.
     * Extensions may override this method in order to create a customized
     * <tt>HttpClient</tt> instance (e.g. with a custom
     * {@link org.apache.commons.httpclient.HttpConnectionManager} to perform
     * some tracking; see feature request 1438216).
     * @return the <tt>HttpClient</tt> that will be used by this WebConnection
     */
    protected AbstractHttpClient createHttpClient() {
        final HttpParams httpsParams = new BasicHttpParams();

        HttpClientParams.setRedirecting(httpsParams, false);

        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        final ThreadSafeClientConnManager connectionManager =
            new ThreadSafeClientConnManager(httpsParams, schemeRegistry);
        return new DefaultHttpClient(connectionManager, httpsParams);
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
            final WebRequest request, final DownloadedContent responseBody, final long loadTime) throws IOException {

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
                    return new DownloadedContent.OnFile(file);
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
     * If set to <tt>true</tt>, the client will accept connections to any host, regardless of
     * whether they have valid certificates or not. This is especially useful when you are trying to
     * connect to a server with expired or corrupt certificates.
     *
     * @param useInsecureSSL whether or not to use insecure SSL
     * @throws GeneralSecurityException if a security error occurs
     */
    public void setUseInsecureSSL(final boolean useInsecureSSL) throws GeneralSecurityException {
        HttpWebConnectionInsecureSSL.setUseInsecureSSL(getHttpClient(), useInsecureSSL);
    }

    /**
     * Shutdown the connection manager.
     */
    public void shutdown() {
        if (httpClient_ != null) {
            httpClient_.getConnectionManager().shutdown();
            httpClient_ = null;
        }
    }
}
