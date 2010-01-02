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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.TraceMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;

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
 */
public class HttpWebConnection implements WebConnection {

    private final WebClient webClient_;
    private HttpClient httpClient_;
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
    public WebResponse getResponse(final WebRequestSettings settings)
        throws IOException {

        final URL url = settings.getUrl();
        final HttpClient httpClient = getHttpClient();
        webClient_.getCookieManager().updateState(httpClient.getState());

        final HttpMethodBase httpMethod = makeHttpMethod(settings);
        try {
            final HostConfiguration hostConfiguration = getHostConfiguration(settings);
            final long startTime = System.currentTimeMillis();
            final int responseCode = httpClient.executeMethod(hostConfiguration, httpMethod);
            final long endTime = System.currentTimeMillis();
            webClient_.getCookieManager().updateFromState(httpClient.getState());
            return makeWebResponse(responseCode, httpMethod, settings, endTime - startTime);
        }
        catch (final HttpException e) {
            // KLUDGE: hitting www.yahoo.com will cause an exception to be thrown while
            // www.yahoo.com/ (note the trailing slash) will not. If an exception is
            // caught here then check to see if this is the situation. If so, then retry
            // it with a trailing slash. The bug manifests itself with httpClient
            // complaining about not being able to find a line with HTTP/ on it.
            if (url.getPath().length() == 0) {
                final StringBuilder buffer = new StringBuilder();
                buffer.append(url.getProtocol());
                buffer.append("://");
                buffer.append(url.getHost());
                buffer.append("/");
                if (url.getQuery() != null) {
                    buffer.append(url.getQuery());
                }
                //TODO: There might be a bug here since the original encoding type is lost.
                final WebRequestSettings newRequest = new WebRequestSettings(new URL(buffer.toString()));
                newRequest.setHttpMethod(settings.getHttpMethod());
                newRequest.setRequestParameters(settings.getRequestParameters());
                newRequest.setAdditionalHeaders(settings.getAdditionalHeaders());
                return getResponse(newRequest);
            }
            throw new RuntimeException("HTTP Error: " + e.getMessage(), e);
        }
        finally {
            onResponseGenerated(httpMethod);
        }
    }

    /**
     * Called when the response has been generated. Default action is to release
     * the HttpMethod's connection. Subclasses may override.
     * @param httpMethod the httpMethod used
     */
    protected void onResponseGenerated(final HttpMethodBase httpMethod) {
        httpMethod.releaseConnection();
    }

    /**
     * Returns a new HttpClient host configuration, initialized based on the specified request settings.
     * @param webRequestSettings the request settings to use to initialize the returned host configuration
     * @return a new HttpClient host configuration, initialized based on the specified request settings
     * @throws IOException if the specified request settings contains an invalid URL
     */
    private static HostConfiguration getHostConfiguration(final WebRequestSettings webRequestSettings)
        throws IOException {
        final HostConfiguration hostConfiguration = new HostConfiguration();
        final URL url = webRequestSettings.getUrl();
        final URI uri;
        try {
            uri = new URI(url.toExternalForm(), false);
        }
        catch (final URIException e) {
            throw new IOException("Unable to create URI from URL: " + url.toExternalForm());
        }
        hostConfiguration.setHost(uri);
        if (webRequestSettings.getProxyHost() != null) {
            final String proxyHost = webRequestSettings.getProxyHost();
            final int proxyPort = webRequestSettings.getProxyPort();
            hostConfiguration.setProxy(proxyHost, proxyPort);
        }
        return hostConfiguration;
    }

    /**
     * Creates an <tt>HttpMethod</tt> instance according to the specified parameters.
     * @param webRequestSettings the parameters
     * @return the <tt>HttpMethod</tt> instance constructed according to the specified parameters
     * @throws IOException
     */
    private HttpMethodBase makeHttpMethod(final WebRequestSettings webRequestSettings)
        throws IOException {

        // Make sure that the URL is fully encoded. IE actually sends some Unicode chars in request
        // URLs; because of this we allow some Unicode chars in URLs. However, at this point we're
        // handing things over the HttpClient, and HttpClient will blow up if we leave these Unicode
        // chars in the URL.
        final URL url = UrlUtils.encodeUrl(webRequestSettings.getUrl(), false);

        String path = url.getPath();
        if (path.length() == 0) {
            path = "/";
        }
        else if (path.startsWith("//")) {
            path = "//" + path; // see https://issues.apache.org/jira/browse/HTTPCLIENT-727
        }

        final HttpMethodBase httpMethod = buildHttpMethod(webRequestSettings.getHttpMethod(), path);
        if (!(httpMethod instanceof EntityEnclosingMethod)) {
            // this is the case for GET as well as TRACE, DELETE, OPTIONS and HEAD
            if (webRequestSettings.getRequestParameters().isEmpty()) {
                final String queryString = url.getQuery();
                httpMethod.setQueryString(queryString);
            }
            else {
                final NameValuePair[] pairs = new NameValuePair[webRequestSettings.getRequestParameters().size()];
                webRequestSettings.getRequestParameters().toArray(pairs);
                httpMethod.setQueryString(NameValuePair.toHttpClient(pairs));
            }
        }
        else { // POST as well as PUT
            final EntityEnclosingMethod method = (EntityEnclosingMethod) httpMethod;
            method.getParams().setContentCharset(webRequestSettings.getCharset());

            final String queryString = url.getQuery();
            method.setQueryString(queryString);
            if (webRequestSettings.getRequestBody() != null) {
                final String body = webRequestSettings.getRequestBody();
                final String charset = webRequestSettings.getCharset();
                method.setRequestEntity(new StringRequestEntity(body, null, charset));
            }

            // Note that this has to be done in two loops otherwise it won't
            // be able to support two elements with the same name.
            if (webRequestSettings.getEncodingType() == FormEncodingType.URL_ENCODED
                    && method instanceof PostMethod) {
                final PostMethod postMethod = (PostMethod) httpMethod;
                for (final NameValuePair pair : webRequestSettings.getRequestParameters()) {
                    postMethod.removeParameter(pair.getName(), pair.getValue());
                }

                for (final NameValuePair pair : webRequestSettings.getRequestParameters()) {
                    postMethod.addParameter(pair.getName(), pair.getValue());
                }
            }
            else if (FormEncodingType.MULTIPART == webRequestSettings.getEncodingType()) {
                final List<PartBase> partList = new ArrayList<PartBase>();
                for (final NameValuePair pair : webRequestSettings.getRequestParameters()) {
                    final PartBase newPart;
                    if (pair instanceof KeyDataPair) {
                        final KeyDataPair pairWithFile = (KeyDataPair) pair;
                        final String charset = webRequestSettings.getCharset();
                        newPart = buildFilePart(pairWithFile, charset);
                    }
                    else {
                        newPart = new StringPart(pair.getName(), pair.getValue(), webRequestSettings.getCharset());
                        newPart.setContentType(null); // Firefox and IE seem not to send a content type
                    }
                    newPart.setTransferEncoding(null); // Firefox and IE don't send transfer encoding headers
                    partList.add(newPart);
                }
                Part[] parts = new Part[partList.size()];
                parts = partList.toArray(parts);
                method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
            }
            else { // for instance a PUT request
                final String body = webRequestSettings.getRequestBody();
                if (body != null) {
                    final String contentType = webRequestSettings.getAdditionalHeaders().get("Content-type");
                    final String charset = webRequestSettings.getCharset();
                    method.setRequestEntity(new StringRequestEntity(body, contentType, charset));
                }
            }
        }

        httpMethod.setRequestHeader("User-Agent", webClient_.getBrowserVersion().getUserAgent());

        writeRequestHeadersToHttpMethod(httpMethod, webRequestSettings.getAdditionalHeaders());
        httpMethod.setFollowRedirects(false);

        if (webRequestSettings.getCredentialsProvider() != null) {
            httpMethod.getParams().setParameter(CredentialsProvider.PROVIDER,
                    webRequestSettings.getCredentialsProvider());
        }

        if (webClient_.getCookieManager().isCookiesEnabled()) {
            // Cookies are enabled. Note that it's important that we enable single cookie headers,
            // for compatibility purposes.
            httpMethod.getParams().setBooleanParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, true);
            httpMethod.getParams().setCookiePolicy(CookieManager.HTMLUNIT_COOKIE_POLICY);
        }
        else {
            // Cookies are disabled.
            httpMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }

        return httpMethod;
    }

    FilePart buildFilePart(final KeyDataPair pairWithFile, final String charset) throws FileNotFoundException {
        final FilePartPageCharSet part;
        if (pairWithFile.getData() != null) {
            part = new FilePartPageCharSet(pairWithFile.getName(),
                    new ByteArrayPartSource(pairWithFile.getValue(), pairWithFile.getData()),
                    pairWithFile.getContentType(), charset);
        }
        else {
            part = new FilePartPageCharSet(pairWithFile.getName(), pairWithFile.getValue(), pairWithFile.getFile(),
                pairWithFile.getContentType(), charset);
        }
        part.pairWithFile_ = pairWithFile;
        part.webClient_ = webClient_;

        // Firefox and IE seem not to specify a charset for a file part
        part.setCharSet(null);

        return part;
    }

    /**
     * Creates and returns a new HttpClient HTTP method based on the specified parameters.
     * @param submitMethod the submit method being used
     * @param path the path being used
     * @return a new HttpClient HTTP method based on the specified parameters
     */
    private static HttpMethodBase buildHttpMethod(final HttpMethod submitMethod, final String path) {
        final HttpMethodBase method;
        switch (submitMethod) {
            case GET:
                method = new GetMethod(path);
                break;

            case POST:
                method = new PostMethod(path);
                break;

            case PUT:
                method = new PutMethod(path);
                break;

            case DELETE:
                method = new DeleteMethod(path);
                break;

            case OPTIONS:
                method = new OptionsMethod(path);
                break;

            case HEAD:
                method = new HeadMethod(path);
                break;

            case TRACE:
                method = new TraceMethod(path);
                break;

            default:
                throw new IllegalStateException("Submit method not yet supported: " + submitMethod);
        }
        return method;
    }

    /**
     * Lazily initializes the internal HTTP client.
     * @return the initialized HTTP client
     */
    protected synchronized HttpClient getHttpClient() {
        if (httpClient_ == null) {
            httpClient_ = createHttpClient();

            // Disable informational messages from httpclient
            final Log log = LogFactory.getLog("httpclient.wire");
            if (log instanceof SimpleLog) {
                ((SimpleLog) log).setLevel(SimpleLog.LOG_LEVEL_WARN);
            }

            httpClient_.getHttpConnectionManager().getParams().setSoTimeout(getTimeout());
            httpClient_.getHttpConnectionManager().getParams().setConnectionTimeout(getTimeout());

            if (virtualHost_ != null) {
                httpClient_.getParams().setVirtualHost(virtualHost_);
            }
        }

        // Tell the client where to get its credentials from
        // (it may have changed on the webClient since last call to getHttpClientFor(...))
        httpClient_.getParams().setParameter(CredentialsProvider.PROVIDER, webClient_.getCredentialsProvider());

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
     * Creates the <tt>HttpClient</tt> that will be used by this WebConnection. Extensions may
     * override this method in order to create a customized <tt>HttpClient</tt> instance (e.g. with
     * a custom {@link org.apache.commons.httpclient.HttpConnectionManager} to perform some tracking;
     * see feature request 1438216).
     * @return the <tt>HttpClient</tt> that will be used by this WebConnection
     */
    protected HttpClient createHttpClient() {
        final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        return new HttpClient(connectionManager);
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
    private WebResponse makeWebResponse(final int statusCode, final HttpMethodBase method,
            final WebRequestSettings requestSettings, final long loadTime) throws IOException {

        String statusMessage = method.getStatusText();
        if (statusMessage == null || statusMessage.length() == 0) {
            statusMessage = HttpStatus.getStatusText(statusCode);
        }
        if (statusMessage == null) {
            statusMessage = "Unknown status code";
        }
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        for (final Header header : method.getResponseHeaders()) {
            headers.add(new NameValuePair(header.getName(), header.getValue()));
        }
        final WebResponseData responseData = newWebResponseDataInstance(statusMessage, headers, statusCode, method);
        return newWebResponseInstance(responseData, loadTime, requestSettings);
    }

    /**
     * Constructs an appropriate WebResponseData.
     * May be overridden by subclasses to return a specialized WebResponseData.
     * @param statusMessage StatusMessage from the response
     * @param headers response headers
     * @param statusCode response status code
     * @param method request method
     * @return the WebResponseData to use for this response
     * @throws IOException if there is a problem reading the response body
     */
    protected WebResponseData newWebResponseDataInstance(
            final String statusMessage,
            final List<NameValuePair> headers,
            final int statusCode,
            final HttpMethodBase method
    ) throws IOException {
        return new WebResponseData(method.getResponseBodyAsStream(), statusCode, statusMessage, headers);
    }

    /**
     * Constructs an appropriate WebResponse.
     * May be overridden by subclasses to return a specialized WebResponse.
     * @param responseData Data that was send back
     * @param charset Charset used if not returned in the response
     * @param requestSettings the request settings used to get this response
     * @param loadTime How long the response took to be sent
     * @return the new WebResponse
     * @deprecated As of 2.6, please use {@link #newWebResponseInstance(WebResponseData, long, WebRequestSettings)}
     */
    @Deprecated
    protected WebResponse newWebResponseInstance(
            final String charset,
            final WebResponseData responseData,
            final long loadTime,
            final WebRequestSettings requestSettings) {
        return new WebResponseImpl(responseData, charset, requestSettings, loadTime);
    }

    /**
     * Constructs an appropriate WebResponse.
     * May be overridden by subclasses to return a specialized WebResponse.
     * @param responseData Data that was send back
     * @param requestSettings the request settings used to get this response
     * @param loadTime How long the response took to be sent
     * @return the new WebResponse
     */
    protected WebResponse newWebResponseInstance(
            final WebResponseData responseData,
            final long loadTime,
            final WebRequestSettings requestSettings) {
        return new WebResponseImpl(responseData, requestSettings, loadTime);
    }

    private static void writeRequestHeadersToHttpMethod(final org.apache.commons.httpclient.HttpMethod httpMethod,
        final Map<String, String> requestHeaders) {
        synchronized (requestHeaders) {
            for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                httpMethod.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * This implementation overrides the superclass' method by encoding filename according to the page charset.
     * @see <a href="http://issues.apache.org/jira/browse/HTTPCLIENT-293">HTTPCLIENT-293</a>
     * {@inheritDoc}
     */
    private static final class FilePartPageCharSet extends FilePart {
        private KeyDataPair pairWithFile_;
        private WebClient webClient_;
        private String pageCharset_;

        private FilePartPageCharSet(final String name, final ByteArrayPartSource byteArrayPartSource,
            final String contentType, final String charset) {
            super(name, byteArrayPartSource, contentType, charset);
            pageCharset_ = charset;
        }

        private FilePartPageCharSet(final String name, final String value, final File file, final String contentType,
            final String charset) throws FileNotFoundException {
            super(name, value, file, contentType, charset);
            pageCharset_ = charset;
        }

        @Override
        protected void sendDispositionHeader(final OutputStream out) throws IOException {
            out.write(CONTENT_DISPOSITION_BYTES);
            out.write(QUOTE_BYTES);
            out.write(EncodingUtil.getAsciiBytes(getName()));
            out.write(QUOTE_BYTES);
            final String filename = getSource().getFileName();
            if (filename != null) {
                out.write(EncodingUtil.getAsciiBytes(FILE_NAME));
                out.write(QUOTE_BYTES);
                out.write(EncodingUtil.getBytes(getFileName(), pageCharset_));
                out.write(QUOTE_BYTES);
            }
        }

        private String getFileName() {
            if (pairWithFile_.getFile() == null) {
                return pairWithFile_.getValue();
            }
            else if (webClient_.getBrowserVersion().isIE()) {
                return pairWithFile_.getFile().getAbsolutePath();
            }
            else {
                return pairWithFile_.getValue();
            }
        }
    }
}
