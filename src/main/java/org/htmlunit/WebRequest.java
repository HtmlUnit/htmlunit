/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.auth.Credentials;
import org.htmlunit.http.HttpUtils;
import org.htmlunit.httpclient.HtmlUnitUsernamePasswordCredentials;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * Parameter object for making web requests.
 *
 * @author Brad Clarke
 * @author Hans Donner
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Rodney Gitzel
 * @author Ronald Brill
 * @author Adam Afeltowicz
 * @author Joerg Werner
 * @author Michael Lueck
 * @author Lai Quang Duong
 * @author Kristof Neirynck
 */
public class WebRequest implements Serializable {

    public enum HttpHint {
        /** Force to include the charset. */
        IncludeCharsetInContentTypeHeader,

        /** Disable sending of stored cookies and receiving of new cookies. */
        BlockCookies
    }

    private static final Pattern DOT_PATTERN = Pattern.compile("/\\./");
    private static final Pattern DOT_DOT_PATTERN = Pattern.compile("/(?!\\.\\.)[^/]*/\\.\\./");
    private static final Pattern REMOVE_DOTS_PATTERN = Pattern.compile("^/(\\.\\.?/)*");

    private String url_; // String instead of java.net.URL because "about:blank" URLs don't serialize correctly
    private String proxyHost_;
    private int proxyPort_;
    private String proxyScheme_;
    private boolean isSocksProxy_;
    private HttpMethod httpMethod_ = HttpMethod.GET;
    private FormEncodingType encodingType_ = FormEncodingType.URL_ENCODED;
    private Map<String, String> additionalHeaders_ = new HashMap<>();
    private Credentials urlCredentials_;
    private Credentials credentials_;
    private int timeout_;
    private transient Set<HttpHint> httpHints_;

    private transient Charset charset_ = StandardCharsets.ISO_8859_1;
    // https://datatracker.ietf.org/doc/html/rfc6838#section-4.2.1
    // private transient Charset defaultResponseContentCharset_ = StandardCharsets.UTF_8;
    private transient Charset defaultResponseContentCharset_ = StandardCharsets.ISO_8859_1;

    /* These two are mutually exclusive; additionally, requestBody_ should only be set for POST requests. */
    private List<NameValuePair> requestParameters_ = Collections.emptyList();
    private String requestBody_;

    /**
     * Instantiates a {@link WebRequest} for the specified URL.
     * @param url the target URL
     * @param acceptHeader the accept header to use
     * @param acceptEncodingHeader the accept encoding header to use
     */
    public WebRequest(final URL url, final String acceptHeader, final String acceptEncodingHeader) {
        setUrl(url);
        if (acceptHeader != null) {
            setAdditionalHeader(HttpHeader.ACCEPT, acceptHeader);
        }
        if (acceptEncodingHeader != null) {
            setAdditionalHeader(HttpHeader.ACCEPT_ENCODING, acceptEncodingHeader);
        }
        timeout_ = -1;
    }

    /**
     * Instantiates a {@link WebRequest} for the specified URL.
     * @param url the target URL
     * @param charset the charset to use
     * @param refererUrl the url be used by the referer header
     */
    public WebRequest(final URL url, final Charset charset, final URL refererUrl) {
        setUrl(url);
        setCharset(charset);
        setRefererHeader(refererUrl);
    }

    /**
     * @return a new request for about:blank
     */
    public static WebRequest newAboutBlankRequest() {
        return new WebRequest(UrlUtils.URL_ABOUT_BLANK, "*/*", "gzip, deflate");
    }

    /**
     * Instantiates a {@link WebRequest} for the specified URL.
     * @param url the target URL
     */
    public WebRequest(final URL url) {
        this(url, "*/*", "gzip, deflate");
    }

    /**
     * Instantiates a {@link WebRequest} for the specified URL using the specified HTTP submit method.
     * @param url the target URL
     * @param submitMethod the HTTP submit method to use
     */
    public WebRequest(final URL url, final HttpMethod submitMethod) {
        this(url);
        setHttpMethod(submitMethod);
    }

    /**
     * Returns the target URL.
     * @return the target URL
     */
    public URL getUrl() {
        return UrlUtils.toUrlSafe(url_);
    }

    /**
     * Sets the target URL. The URL may be simplified if needed (for instance eliminating
     * irrelevant path portions like "/./").
     * @param url the target URL
     */
    public void setUrl(URL url) {
        if (url == null) {
            url_ = null;
            return;
        }

        final String path = url.getPath();
        if (path.isEmpty()) {
            if (!url.getFile().isEmpty() || url.getProtocol().startsWith("http")) {
                url = buildUrlWithNewPath(url, "/");
            }
        }
        else if (path.contains("/.")) {
            url = buildUrlWithNewPath(url, removeDots(path));
        }

        try {
            final String idn = IDN.toASCII(url.getHost());
            if (!idn.equals(url.getHost())) {
                url = UrlUtils.getUrlWithNewHost(url, idn);
            }
        }
        catch (final Exception e) {
            throw new IllegalArgumentException(
                    "Cannot convert the hostname of URL: '" + url.toExternalForm() + "' to ASCII.", e);
        }

        try {
            url_ = UrlUtils.removeRedundantPort(url).toExternalForm();
        }
        catch (final MalformedURLException e) {
            throw new RuntimeException("Cannot strip default port of URL: " + url.toExternalForm(), e);
        }

        // http://john.smith:secret@localhost
        final String userInfo = url.getUserInfo();
        if (userInfo != null) {
            final int splitPos = userInfo.indexOf(':');
            if (splitPos == -1) {
                urlCredentials_ = new HtmlUnitUsernamePasswordCredentials(userInfo, new char[0]);
            }
            else {
                final String username = userInfo.substring(0, splitPos);
                final String password = userInfo.substring(splitPos + 1);
                urlCredentials_ = new HtmlUnitUsernamePasswordCredentials(username, password.toCharArray());
            }
        }
    }

    /*
     * Strip a URL string of "/./" and "/../" occurrences.
     * <p>
     * One trick here is to repeatedly create new matchers on a given
     * pattern, so that we can see whether it needs to be re-applied;
     * unfortunately .replaceAll() doesn't re-process its own output,
     * so if we create a new match with a replacement, it is missed.
     */
    private static String removeDots(final String path) {
        String newPath = path;

        // remove occurrences at the beginning
        newPath = REMOVE_DOTS_PATTERN.matcher(newPath).replaceAll("/");
        if ("/..".equals(newPath)) {
            newPath = "/";
        }

        // single dots have no effect, so just remove them
        while (DOT_PATTERN.matcher(newPath).find()) {
            newPath = DOT_PATTERN.matcher(newPath).replaceAll("/");
        }

        // mid-path double dots should be removed WITH the previous subdirectory and replaced
        //  with "/" BUT ONLY IF that subdirectory's not also ".." (a regex lookahead helps with this)
        while (DOT_DOT_PATTERN.matcher(newPath).find()) {
            newPath = DOT_DOT_PATTERN.matcher(newPath).replaceAll("/");
        }

        return newPath;
    }

    private static URL buildUrlWithNewPath(URL url, final String newPath) {
        try {
            url = UrlUtils.getUrlWithNewPath(url, newPath);
        }
        catch (final Exception e) {
            throw new RuntimeException("Cannot change path of URL: " + url.toExternalForm(), e);
        }
        return url;
    }

    /**
     * Returns the proxy host to use.
     * @return the proxy host to use
     */
    public String getProxyHost() {
        return proxyHost_;
    }

    /**
     * Sets the proxy host to use.
     * @param proxyHost the proxy host to use
     */
    public void setProxyHost(final String proxyHost) {
        proxyHost_ = proxyHost;
    }

    /**
     * Returns the proxy port to use.
     * @return the proxy port to use
     */
    public int getProxyPort() {
        return proxyPort_;
    }

    /**
     * Sets the proxy port to use.
     * @param proxyPort the proxy port to use
     */
    public void setProxyPort(final int proxyPort) {
        proxyPort_ = proxyPort;
    }

    /**
     * Returns the proxy scheme to use.
     * @return the proxy scheme to use
     */
    public String getProxyScheme() {
        return proxyScheme_;
    }

    /**
     * Sets the proxy scheme to use.
     * @param proxyScheme the proxy scheme to use
     */
    public void setProxyScheme(final String proxyScheme) {
        proxyScheme_ = proxyScheme;
    }

    /**
     * Returns whether SOCKS proxy or not.
     * @return whether SOCKS proxy or not
     */
    public boolean isSocksProxy() {
        return isSocksProxy_;
    }

    /**
     * Sets whether SOCKS proxy or not.
     * @param isSocksProxy whether SOCKS proxy or not
     */
    public void setSocksProxy(final boolean isSocksProxy) {
        isSocksProxy_ = isSocksProxy;
    }

    /**
     * @return the timeout to use
     */
    public int getTimeout() {
        return timeout_;
    }

    /**
     * Sets the timeout to use.
     * @param timeout the timeout to use
     */
    public void setTimeout(final int timeout) {
        timeout_ = timeout;
    }

    /**
     * Returns the form encoding type to use.
     * @return the form encoding type to use
     */
    public FormEncodingType getEncodingType() {
        return encodingType_;
    }

    /**
     * Sets the form encoding type to use.
     * @param encodingType the form encoding type to use
     */
    public void setEncodingType(final FormEncodingType encodingType) {
        encodingType_ = encodingType;
    }

    /**
     * <p>Retrieves the request parameters used. Similar to the servlet api function
     * getParameterMap() this works depending on the request type and collects the
     * url parameters and the body stuff.<br>
     * The value is also normalized - null is converted to an empty string.</p>
     * <p>In contrast to the servlet api this creates a separate KeyValuePair for every
     * parameter. This means that pairs with the same name can be part of the list. The
     * servlet api will return a string[] as value for the key in this case.<br>
     * Additionally this method includes also the uploaded files for multipart post
     * requests.</p>
     *
     * @return the request parameters to use
     */
    public List<NameValuePair> getParameters() {
        // developer note:
        // this has to be in sync with org.htmlunit.HttpWebConnection.makeHttpMethod(WebRequest, HttpClientBuilder)

        // developer note:
        // the spring org.springframework.test.web.servlet.htmlunitHtmlUnitRequestBuilder uses
        // this method and is sensitive to all the details of the current implementation.

        // POST, PUT, PATCH, DELETE, OPTIONS
        final HttpMethod httpMethod = getHttpMethod();
        if (httpMethod == HttpMethod.POST
                        || httpMethod == HttpMethod.PUT
                        || httpMethod == HttpMethod.PATCH
                        || httpMethod == HttpMethod.DELETE
                        || httpMethod == HttpMethod.OPTIONS) {

            if (FormEncodingType.URL_ENCODED == getEncodingType()) {
                if (getRequestBody() == null) {
                    final List<NameValuePair> allParameters = new ArrayList<>();
                    allParameters.addAll(HttpUtils.parseUrlQuery(getUrl().getQuery(), getCharset()));

                    // for PATCH/DELETE/OPTIONS request browsers are sending the body
                    // but the servlet API does not get it
                    if (httpMethod != HttpMethod.PATCH
                            && httpMethod != HttpMethod.DELETE
                            && httpMethod != HttpMethod.OPTIONS) {
                        allParameters.addAll(getRequestParameters());
                    }

                    return normalize(allParameters);
                }

                // getRequestParameters and getRequestBody are mutually exclusive
                final List<NameValuePair> allParameters = new ArrayList<>();
                allParameters.addAll(HttpUtils.parseUrlQuery(getUrl().getQuery(), getCharset()));

                // for PATCH/DELETE/OPTIONS request browsers are sending the body
                // but the servlet API does not get it
                if (httpMethod != HttpMethod.PATCH
                        && httpMethod != HttpMethod.DELETE
                        && httpMethod != HttpMethod.OPTIONS) {
                    allParameters.addAll(HttpUtils.parseUrlQuery(getRequestBody(), getCharset()));
                }

                return normalize(allParameters);
            }

            if (FormEncodingType.TEXT_PLAIN == getEncodingType()) {
                if (getRequestBody() == null) {
                    final List<NameValuePair> allParameters = new ArrayList<>();
                    allParameters.addAll(HttpUtils.parseUrlQuery(getUrl().getQuery(), getCharset()));

                    // the servlet api ignores the parameters
                    // allParameters.addAll(getRequestParameters());

                    return normalize(allParameters);
                }

                return normalize(HttpUtils.parseUrlQuery(getUrl().getQuery(), getCharset()));
            }

            if (FormEncodingType.MULTIPART == getEncodingType()) {
                final List<NameValuePair> allParameters = new ArrayList<>();

                allParameters.addAll(HttpUtils.parseUrlQuery(getUrl().getQuery(), getCharset()));

                // the servlet api ignores these parameters but to make spring happy we include them
                allParameters.addAll(getRequestParameters());

                return normalize(allParameters);
            }

            return Collections.emptyList();
        }

        // GET, TRACE, HEAD

        // a bit strange but
        // HttpWebConnection.makeHttpMethod() moves the parameters up to the query
        // (in fact replaces the query with the parameters)
        // to reflect this we have to take the parameters into account even if this
        // looks wrong for GET requests
        if (!getRequestParameters().isEmpty()) {
            return normalize(getRequestParameters());
        }

        return normalize(HttpUtils.parseUrlQuery(getUrl().getQuery(), getCharset()));
    }

    private static List<NameValuePair> normalize(final List<NameValuePair> pairs) {
        if (pairs == null || pairs.isEmpty()) {
            return pairs;
        }

        final List<NameValuePair> resultingPairs = new ArrayList<>();
        for (final NameValuePair pair : pairs) {
            resultingPairs.add(pair.normalized());
        }

        return resultingPairs;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Retrieves the request parameters to use. If set, these request parameters will overwrite any
     * request parameters which may be present in the {@link #getUrl() URL}. Should not be used in
     * combination with the {@link #setRequestBody(String) request body}.
     * @return the request parameters to use
     */
    public List<NameValuePair> getRequestParameters() {
        return requestParameters_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the request parameters to use. If set, these request parameters will overwrite any request
     * parameters which may be present in the {@link #getUrl() URL}. Should not be used in combination
     * with the {@link #setRequestBody(String) request body}.
     * @param requestParameters the request parameters to use
     * @throws RuntimeException if the request body has already been set
     */
    public void setRequestParameters(final List<NameValuePair> requestParameters) throws RuntimeException {
        if (requestBody_ != null) {
            final String msg = "Trying to set the request parameters, but the request body has already been specified;"
                             + "the two are mutually exclusive!";
            throw new RuntimeException(msg);
        }
        requestParameters_ = requestParameters;
    }

    /**
     * Returns the body content to be submitted if this is a <code>POST</code> request. Ignored for all other request
     * types. Should not be used in combination with {@link #setRequestParameters(List) request parameters}.
     * @return the body content to be submitted if this is a <code>POST</code> request
     */
    public String getRequestBody() {
        return requestBody_;
    }

    /**
     * Sets the body content to be submitted if this is a {@code POST}, {@code PUT} or {@code PATCH} request.
     * Other request types result in {@link RuntimeException}.
     * Should not be used in combination with {@link #setRequestParameters(List) request parameters}.
     * @param requestBody the body content to be submitted if this is a {@code POST}, {@code PUT}
     * or {@code PATCH} request
     * @throws RuntimeException if the request parameters have already been set
     *                          or this is not a {@code POST}, {@code PUT} or {@code PATCH} request.
     */
    public void setRequestBody(final String requestBody) throws RuntimeException {
        if (requestParameters_ != null && !requestParameters_.isEmpty()) {
            final String msg = "Trying to set the request body, but the request parameters have already been specified;"
                       + "the two are mutually exclusive!";
            throw new RuntimeException(msg);
        }
        if (httpMethod_ != HttpMethod.POST
                && httpMethod_ != HttpMethod.PUT
                && httpMethod_ != HttpMethod.PATCH
                && httpMethod_ != HttpMethod.DELETE
                && httpMethod_ != HttpMethod.OPTIONS) {
            final String msg = "The request body may only be set for POST, PUT, PATCH, DELETE or OPTIONS requests!";
            throw new RuntimeException(msg);
        }
        requestBody_ = requestBody;
    }

    /**
     * Returns the HTTP submit method to use.
     * @return the HTTP submit method to use
     */
    public HttpMethod getHttpMethod() {
        return httpMethod_;
    }

    /**
     * Sets the HTTP submit method to use.
     * @param submitMethod the HTTP submit method to use
     */
    public void setHttpMethod(final HttpMethod submitMethod) {
        httpMethod_ = submitMethod;
    }

    /**
     * Returns the additional HTTP headers to use.
     * @return the additional HTTP headers to use
     */
    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders_;
    }

    /**
     * Sets the additional HTTP headers to use.
     * @param additionalHeaders the additional HTTP headers to use
     */
    public void setAdditionalHeaders(final Map<String, String> additionalHeaders) {
        additionalHeaders_ = additionalHeaders;
    }

    /**
     * Returns whether the specified header name is already included in the additional HTTP headers.
     * @param name the name of the additional HTTP header
     * @return true if the specified header name is included in the additional HTTP headers
     */
    public boolean isAdditionalHeader(final String name) {
        for (final String key : additionalHeaders_.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the header value associated with this name.
     * @param name the name of the additional HTTP header
     * @return the value or null
     */
    public String getAdditionalHeader(final String name) {
        String newKey = name;
        for (final String key : additionalHeaders_.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                newKey = key;
                break;
            }
        }
        return additionalHeaders_.get(newKey);
    }

    /**
     * Sets the referer HTTP header - only if the provided url is valid.
     * @param url the url for the referer HTTP header
     */
    public void setRefererHeader(final URL url) {
        if (url == null || !url.getProtocol().startsWith("http")) {
            return;
        }

        try {
            setAdditionalHeader(HttpHeader.REFERER, UrlUtils.getUrlWithoutRef(url).toExternalForm());
        }
        catch (final MalformedURLException ignored) {
            // bad luck us the whole url from the pager
        }
    }

    /**
     * Sets the referer HTTP header - only if the provided url is valid.
     * @param url the url for the referer HTTP header
     *
     * @deprecated as of version 4.5.0; use {@link #setRefererHeader(URL)} instead
     */
    @Deprecated
    public void setRefererlHeader(final URL url) {
        setRefererHeader(url);
    }

    /**
     * Sets the specified name/value pair in the additional HTTP headers.
     * @param name the name of the additional HTTP header
     * @param value the value of the additional HTTP header
     */
    public void setAdditionalHeader(final String name, final String value) {
        String newKey = name;
        for (final String key : additionalHeaders_.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                newKey = key;
                break;
            }
        }
        additionalHeaders_.put(newKey, value);
    }

    /**
     * Removed the specified name/value pair from the additional HTTP headers.
     * @param name the name of the additional HTTP header
     */
    public void removeAdditionalHeader(String name) {
        for (final String key : additionalHeaders_.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                name = key;
                break;
            }
        }
        additionalHeaders_.remove(name);
    }

    /**
     * Returns the credentials to use.
     * @return the credentials if set as part of the url
     */
    public Credentials getUrlCredentials() {
        return urlCredentials_;
    }

    /**
     * Returns the credentials to use.
     * @return the credentials if set from the external builder
     */
    public Credentials getCredentials() {
        return credentials_;
    }

    /**
     * Sets the credentials to use.
     * @param credentials the credentials to use
     */
    public void setCredentials(final Credentials credentials) {
        credentials_ = credentials;
    }

    /**
     * Returns the character set to use to perform the request.
     * @return the character set to use to perform the request
     */
    public Charset getCharset() {
        return charset_;
    }

    /**
     * Sets the character set to use to perform the request. The default value
     * is {@link java.nio.charset.StandardCharsets#ISO_8859_1}.
     * @param charset the character set to use to perform the request
     */
    public void setCharset(final Charset charset) {
        charset_ = charset;
    }

    /**
     * @return the default character set to use for the response when it does not specify one.
     */
    public Charset getDefaultResponseContentCharset() {
        return defaultResponseContentCharset_;
    }

    /**
     * Sets the default character set to use for the response when it does not specify one.
     * <p>
     * Unless set, the default is {@link java.nio.charset.StandardCharsets#UTF_8}.
     * @param defaultResponseContentCharset the default character set of the response
     */
    public void setDefaultResponseContentCharset(final Charset defaultResponseContentCharset) {
        this.defaultResponseContentCharset_ = Objects.requireNonNull(defaultResponseContentCharset);
    }

    public boolean hasHint(final HttpHint hint) {
        if (httpHints_ == null) {
            return false;
        }
        return httpHints_.contains(hint);
    }

    public void addHint(final HttpHint hint) {
        if (httpHints_ == null) {
            httpHints_ = EnumSet.noneOf(HttpHint.class);
        }
        httpHints_.add(hint);
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(100)
                .append(getClass().getSimpleName())
                .append("[<url=\"").append(url_).append('"')
                .append(", ").append(httpMethod_)
                .append(", ").append(encodingType_)
                .append(", ").append(requestParameters_)
                .append(", ").append(additionalHeaders_)
                .append(", ").append(credentials_)
                .append(">]");
        return builder.toString();
    }

    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(charset_ == null ? null : charset_.name());
    }

    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        final String charsetName = (String) ois.readObject();
        if (charsetName != null) {
            charset_ = Charset.forName(charsetName);
        }
    }
}
