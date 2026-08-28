/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
import java.util.Set;

import org.apache.http.auth.Credentials;
import org.htmlunit.http.HttpUtils;
import org.htmlunit.httpclient.HtmlUnitUsernamePasswordCredentials;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.StringUtils;
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
@SuppressWarnings("PMD.TooManyFields")
public class WebRequest implements Serializable {

    /**
     * Enum to configure request creation.
     */
    public enum HttpHint {
        /** Force to include the charset. */
        IncludeCharsetInContentTypeHeader,

        /** Disable sending of stored cookies and receiving of new cookies. */
        BlockCookies
    }

    /**
     * The destination of a request, as defined by the Fetch spec
     * (<a href="https://fetch.spec.whatwg.org/#concept-request-destination">
     * https://fetch.spec.whatwg.org/#concept-request-destination</a>).
     * <p>
     * This is used to compute the value of the {@code Sec-Fetch-Dest} request
     * header (see <a href="https://www.w3.org/TR/fetch-metadata/">
     * https://www.w3.org/TR/fetch-metadata/</a>) and also determines the default
     * {@link FetchMode} to be used, unless explicitly overridden via
     * {@link #setFetchModeOverride(FetchMode)}.
     * </p>
     */
    public enum FetchDestination {
        /** A top-level document, e.g. loaded by the address bar, a link, or a form submission. */
        DOCUMENT("document"),
        /** A nested browsing context of type {@code <iframe>}. */
        IFRAME("iframe"),
        /** A nested browsing context of type {@code <frame>}. */
        FRAME("frame"),
        /** An {@code <object>} element. */
        OBJECT("object"),
        /** An {@code <embed>} element. */
        EMBED("embed"),
        /** An image, e.g. {@code <img>}, a CSS background-image, or a favicon. */
        IMAGE("image"),
        /** A classic or module script, e.g. {@code <script src>}. */
        SCRIPT("script"),
        /** A stylesheet, e.g. {@code <link rel=stylesheet>} or a CSS {@code @import}. */
        STYLE("style"),
        /** A font resource, e.g. loaded via {@code @font-face}. */
        FONT("font"),
        /** An {@code <audio>} resource. */
        AUDIO("audio"),
        /** A {@code <video>} resource. */
        VIDEO("video"),
        /** A {@code <track>} resource. */
        TRACK("track"),
        /** A dedicated worker script. */
        WORKER("worker"),
        /** A shared worker script. */
        SHARED_WORKER("sharedworker"),
        /** A service worker script. */
        SERVICE_WORKER("serviceworker"),
        /** A web app manifest, e.g. {@code <link rel=manifest>}. */
        MANIFEST("manifest"),
        /** A reporting endpoint request. */
        REPORT("report"),
        /** A WebSocket handshake request. */
        WEBSOCKET("websocket"),
        /** No specific destination, e.g. {@code XMLHttpRequest} or {@code fetch()}. */
        EMPTY("empty");

        private final String value_;

        FetchDestination(final String value) {
            value_ = value;
        }

        /**
         * Returns the value to be used for the {@code Sec-Fetch-Dest} header.
         *
         * @return the value to be used for the {@code Sec-Fetch-Dest} header
         */
        public String getValue() {
            return value_;
        }
    }

    /**
     * The mode of a request, as defined by the Fetch spec
     * (<a href="https://fetch.spec.whatwg.org/#concept-request-mode">
     * https://fetch.spec.whatwg.org/#concept-request-mode</a>).
     * <p>
     * This is used to compute the value of the {@code Sec-Fetch-Mode} request
     * header. Most {@link FetchDestination}s imply a fixed mode; this only needs
     * to be set explicitly to override that default - e.g. for a {@code fetch()}
     * call using an explicit {@code mode} option, or a subresource request using
     * the {@code crossorigin} attribute.
     * </p>
     */
    public enum FetchMode {
        /** Same-origin requests, e.g. worker scripts, or {@code fetch()} with {@code mode: 'same-origin'}. */
        SAME_ORIGIN("same-origin"),
        /** Subresource requests without CORS, e.g. {@code <img>} without {@code crossorigin}. */
        NO_CORS("no-cors"),
        /** CORS-enabled requests, e.g. {@code XMLHttpRequest}, {@code fetch()} default, or module scripts. */
        CORS("cors"),
        /** Navigations, e.g. top-level document loads, {@code <iframe>}/{@code <frame>} loads. */
        NAVIGATE("navigate"),
        /** WebSocket handshake requests. */
        WEBSOCKET("websocket");

        private final String value_;

        FetchMode(final String value) {
            value_ = value;
        }

        /**
         * Returns the value to be used for the {@code Sec-Fetch-Mode} header.
         *
         * @return the value to be used for the {@code Sec-Fetch-Mode} header
         */
        public String getValue() {
            return value_;
        }
    }

    // String instead of java.net.URL because "about:blank" URLs don't serialize correctly
    private String url_;

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

    /*
     * These two are mutually exclusive; additionally, requestBody_ should only be
     * set for POST requests.
     */
    private List<NameValuePair> requestParameters_ = Collections.emptyList();
    private String requestBody_;

    // Sec-Fetch-* support; see https://www.w3.org/TR/fetch-metadata/
    private FetchDestination fetchDestination_ = FetchDestination.EMPTY;
    private FetchMode fetchModeOverride_;
    private boolean userActivation_;

    // String instead of java.net.URL for the same serialization reason as url_ above;
    // null means "no initiator" (e.g. a browser-chrome-initiated navigation), which
    // maps to Sec-Fetch-Site: none.
    private String requestingUrl_;

    /**
     * Instantiates a {@link WebRequest} for the specified URL,
     * setting the Accept and Accept-Encoding headers if provided.
     *
     * @param url                  the target URL
     * @param acceptHeader         the accept header to use
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

        // for backward compatibility
        setFetchDestination(FetchDestination.DOCUMENT);
        setFetchModeOverride(FetchMode.NAVIGATE);
        setUserActivation(true);
    }

    /**
     * Instantiates a {@link WebRequest} for the specified URL,
     * using the given charset and referer.
     *
     * @param url        the target URL
     * @param charset    the charset to use
     * @param refererUrl the url be used by the referer header
     */
    public WebRequest(final URL url, final Charset charset, final URL refererUrl) {
        setUrl(url);
        setCharset(charset);
        setRefererHeader(refererUrl);
    }

    /**
     * Returns a new request for about:blank.
     *
     * @return a new request for about:blank
     */
    public static WebRequest newAboutBlankRequest() {
        return new WebRequest(UrlUtils.URL_ABOUT_BLANK, "*/*", "gzip, deflate");
    }

    /**
     * Instantiates a {@link WebRequest} for the specified URL.
     *
     * @param url the target URL
     */
    public WebRequest(final URL url) {
        this(url, "*/*", "gzip, deflate");
    }

    /**
     * Instantiates a {@link WebRequest} for the specified URL
     * using the given HTTP submit method.
     *
     * @param url          the target URL
     * @param submitMethod the HTTP submit method to use
     */
    public WebRequest(final URL url, final HttpMethod submitMethod) {
        this(url);
        setHttpMethod(submitMethod);
    }

    /**
     * Returns the target URL.
     *
     * @return the target URL
     */
    public URL getUrl() {
        return UrlUtils.toUrlSafe(url_);
    }

    /**
     * Sets the target URL. The URL may be simplified if needed (for instance
     * eliminating irrelevant path portions like "/./").
     *
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
            url = buildUrlWithNewPath(url, StringUtils.removeDots(path));
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
     *
     * @return the proxy host to use
     */
    public String getProxyHost() {
        return proxyHost_;
    }

    /**
     * Sets the proxy host to use.
     *
     * @param proxyHost the proxy host to use
     */
    public void setProxyHost(final String proxyHost) {
        proxyHost_ = proxyHost;
    }

    /**
     * Returns the proxy port to use.
     *
     * @return the proxy port to use
     */
    public int getProxyPort() {
        return proxyPort_;
    }

    /**
     * Sets the proxy port to use.
     *
     * @param proxyPort the proxy port to use
     */
    public void setProxyPort(final int proxyPort) {
        proxyPort_ = proxyPort;
    }

    /**
     * Returns the proxy scheme to use.
     *
     * @return the proxy scheme to use
     */
    public String getProxyScheme() {
        return proxyScheme_;
    }

    /**
     * Sets the proxy scheme to use.
     *
     * @param proxyScheme the proxy scheme to use
     *
     */
    public void setProxyScheme(final String proxyScheme) {
        proxyScheme_ = proxyScheme;
    }

    /**
     * Returns whether SOCKS proxy or not.
     *
     * Returns whether SOCKS proxy or not.
     *
     * @return whether SOCKS proxy or not
     *
     */
    public boolean isSocksProxy() {
        return isSocksProxy_;
    }

    /**
     * Sets whether SOCKS proxy or not.
     *
     * @param isSocksProxy whether SOCKS proxy or not
     *
     */
    public void setSocksProxy(final boolean isSocksProxy) {
        isSocksProxy_ = isSocksProxy;
    }

    /**
     * Returns the timeout to use.
     *
     * @return the timeout to use
     *
     */
    public int getTimeout() {
        return timeout_;
    }

    /**
     * Sets the timeout to use.
     *
     * @param timeout the timeout to use
     *
     */
    public void setTimeout(final int timeout) {
        timeout_ = timeout;
    }

    /**
     * Returns the form encoding type to use.
     *
     * Returns the form encoding type to use.
     *
     * @return the form encoding type to use
     *
     */
    public FormEncodingType getEncodingType() {
        return encodingType_;
    }

    /**
     * Sets the form encoding type to use.
     *
     * @param encodingType the form encoding type to use
     *
     */
    public void setEncodingType(final FormEncodingType encodingType) {
        encodingType_ = encodingType;
    }

    /**
     * Retrieves the request parameters used. Similar to the servlet api function
     * getParameterMap() this works depending on the request type and collects the
     * url parameters and the body stuff.<br>
     * The value is also normalized - null is converted to an empty string.
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

        final List<NameValuePair> allParameters = new ArrayList<>(
                HttpUtils.parseUrlQuery(getUrl().getQuery(), getCharset()));

        // the servlet api ignores these parameters but to make spring happy we include them
        final HttpMethod httpMethod = getHttpMethod();
        if (httpMethod == HttpMethod.POST
            || httpMethod == HttpMethod.PUT
            || httpMethod == HttpMethod.PATCH
            || httpMethod == HttpMethod.DELETE
            || httpMethod == HttpMethod.OPTIONS) {
            if (FormEncodingType.URL_ENCODED == getEncodingType()
                && httpMethod != HttpMethod.OPTIONS) {
                // spring ignores URL_ENCODED parameters for OPTIONS requests
                // getRequestParameters and getRequestBody are mutually exclusive
                if (getRequestBody() == null) {
                    allParameters.addAll(getRequestParameters());
                }
                else {
                    allParameters.addAll(HttpUtils.parseUrlQuery(getRequestBody(), getCharset()));
                }
            }
            else if (FormEncodingType.MULTIPART == getEncodingType()) {
                if (httpMethod == HttpMethod.POST) {
                    allParameters.addAll(getRequestParameters());
                }
                else {
                    // for PUT, PATCH, DELETE and OPTIONS spring moves the parameters up to the query
                    // it doesn't replace the query
                    allParameters.addAll(0, getRequestParameters());
                }
            }
        }

        return normalize(allParameters);
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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT
     * YOUR OWN RISK.</span><br>
     *
     * Returns the request parameters to use. If set, these request parameters
     * will overwrite any request parameters which may be present in the
     * {@link #getUrl() URL}. Should not be used in combination with the
     * {@link #setRequestBody(String) request body}.
     *
     * @return the request parameters to use
     */
    public List<NameValuePair> getRequestParameters() {
        return requestParameters_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT
     * YOUR OWN RISK.</span><br>
     *
     * Sets the request parameters to use. If set, these request parameters will
     * overwrite any request parameters which may be present in the {@link #getUrl()
     * URL}. Should not be used in combination with the
     * {@link #setRequestBody(String) request body}.
     *
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
     * Returns the body content to be submitted if this is a {@code POST}
     * request. Ignored for all other request types. Should not be used in
     * combination with {@link #setRequestParameters(List) request parameters}.
     *
     * @return the body content, or {@code null} if not set
     */
    public String getRequestBody() {
        return requestBody_;
    }

    /**
     * Sets the body content to be submitted if this is a {@code POST}, {@code PUT}
     * or {@code PATCH} request. Other request types result in
     * {@link IllegalStateException}. Should not be used in combination with
     * {@link #setRequestParameters(List) request parameters}.
     *
     * @param requestBody the body content to be submitted if this is a
     *                    {@code POST}, {@code PUT} or {@code PATCH} request
     * @throws IllegalStateException if the request parameters have already been set or
     *                          this is not a {@code POST}, {@code PUT} or
     *                          {@code PATCH} request.
     *
     */
    public void setRequestBody(final String requestBody) throws IllegalStateException {
        if (requestParameters_ != null && !requestParameters_.isEmpty()) {
            final String msg = "Trying to set the request body, but the request parameters have already been specified;"
                       + "the two are mutually exclusive!";
            throw new IllegalStateException(msg);
        }
        if (httpMethod_ != HttpMethod.POST
                && httpMethod_ != HttpMethod.PUT
                && httpMethod_ != HttpMethod.PATCH
                && httpMethod_ != HttpMethod.DELETE
                && httpMethod_ != HttpMethod.OPTIONS) {
            final String msg = "The request body may only be set for POST, PUT, PATCH, DELETE or OPTIONS requests!";
            throw new IllegalStateException(msg);
        }
        requestBody_ = requestBody;
    }

    /**
     * Returns the HTTP submit method to use.
     *
     * @return the HTTP submit method to use
     */
    public HttpMethod getHttpMethod() {
        return httpMethod_;
    }

    /**
     * Sets the HTTP submit method to use.
     *
     * @param submitMethod the HTTP submit method to use
     */
    public void setHttpMethod(final HttpMethod submitMethod) {
        httpMethod_ = submitMethod;
    }

    /**
     * Returns the additional HTTP headers to use.
     *
     * @return the additional HTTP headers to use
     */
    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders_;
    }

    /**
     * Sets the additional HTTP headers to use.
     *
     * @param additionalHeaders the additional HTTP headers to use
     */
    public void setAdditionalHeaders(final Map<String, String> additionalHeaders) {
        additionalHeaders_ = additionalHeaders;
    }

    /**
     * Returns whether the specified header name is already included in the
     * additional HTTP headers. The comparison is case-insensitive.
     *
     * @param name the header name to look up
     * @return {@code true} if the header is present; {@code false} otherwise
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
     * Returns the value of the specified additional HTTP header,
     * or {@code null} if the header is not set. The name comparison
     * is case-insensitive.
     *
     * @param name the header name to look up
     * @return the header value, or {@code null} if not present
     */
    public String getAdditionalHeader(final String name) {
        final String val = additionalHeaders_.get(name);
        if (val != null) {
            return val;
        }

        // fall back to case-insensitive scan
        for (final Map.Entry<String, String> entry : additionalHeaders_.entrySet()) {
            if (name.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Sets the {@code Referer} HTTP header to the external form of the given
     * URL. Does nothing if the URL is {@code null} or does not use the
     * {@code http} or {@code https} scheme.
     *
     * @param url the URL to use as the referer, or {@code null}
     */
    public void setRefererHeader(final URL url) {
        if (url == null || !url.getProtocol().startsWith("http")) {
            return;
        }

        try {
            setAdditionalHeader(HttpHeader.REFERER, UrlUtils.getUrlWithoutRef(url).toExternalForm());
        }
        catch (final MalformedURLException ignored) {
            // bad luck use the whole url from the pager
        }
    }

    /**
     * Returns the destination of this request, used to compute the
     * {@code Sec-Fetch-Dest} header (and, unless overridden, the default
     * {@code Sec-Fetch-Mode}). Defaults to {@link FetchDestination#EMPTY},
     * which is correct for plain {@code XMLHttpRequest}/{@code fetch()} calls.
     *
     * @return the destination of this request
     */
    public FetchDestination getFetchDestination() {
        return fetchDestination_;
    }

    /**
     * Sets the destination of this request.
     *
     * @param fetchDestination the destination of this request, or {@code null}
     *                         to reset to {@link FetchDestination#EMPTY}
     */
    public void setFetchDestination(final FetchDestination fetchDestination) {
        fetchDestination_ = fetchDestination == null ? FetchDestination.EMPTY : fetchDestination;
    }

    /**
     * Returns the explicit mode override for this request, if any. When
     * {@code null} (the default), the mode is derived from the
     * {@link #getFetchDestination() destination}.
     *
     * @return the mode override, or {@code null} if none was set
     */
    public FetchMode getFetchModeOverride() {
        return fetchModeOverride_;
    }

    /**
     * Sets an explicit mode override for this request, e.g. for a {@code fetch()}
     * call using an explicit {@code mode} option, or a subresource request using
     * the {@code crossorigin} attribute (which forces CORS mode).
     *
     * @param fetchMode the mode to use, or {@code null} to derive it from the
     *                  {@link #getFetchDestination() destination}
     */
    public void setFetchModeOverride(final FetchMode fetchMode) {
        fetchModeOverride_ = fetchMode;
    }

    /**
     * Returns whether this request is the result of a navigation backed by
     * genuine user activation (e.g. a click on a link, a typed URL, or a form
     * submitted via a click on its submit button) as opposed to one triggered
     * purely by script (e.g. {@code location.href = ...}, a {@code <meta
     * http-equiv="refresh">}, or an automatically-loaded {@code <iframe>}).
     * <p>
     * Only relevant for requests whose {@code Sec-Fetch-Mode} is {@code
     * navigate}; used to compute the presence of the {@code Sec-Fetch-User}
     * header, which real browsers omit entirely (never send as {@code ?0})
     * whenever this is {@code false}.
     * </p>
     *
     * @return whether this request was triggered by a real user gesture
     */
    public boolean isUserActivation() {
        return userActivation_;
    }

    /**
     * Sets whether this request is the result of a navigation backed by genuine
     * user activation.
     *
     * @param userActivation whether this request was triggered by a real user
     *                       gesture
     */
    public void setUserActivation(final boolean userActivation) {
        userActivation_ = userActivation;
    }

    /**
     * Returns the URL of the document or script that initiated this request, used
     * to compute the {@code Sec-Fetch-Site} header. {@code null} means there is
     * no initiator (e.g. a browser-chrome-initiated navigation such as a typed
     * URL or bookmark), which maps to {@code Sec-Fetch-Site: none}.
     * <p>
     * Note this is tracked separately from the {@code Referer} header: unlike
     * the referrer, it must not be affected by referrer-policy stripping, since
     * {@code Sec-Fetch-Site} always reflects the true relationship between the
     * initiator and the target, even when no {@code Referer} header is sent.
     * </p>
     *
     * @return the URL of the initiator, or {@code null} if there is none
     */
    public URL getRequestingUrl() {
        return requestingUrl_ == null ? null : UrlUtils.toUrlSafe(requestingUrl_);
    }

    /**
     * Sets the URL of the document or script that initiated this request.
     *
     * @param requestingUrl the URL of the initiator, or {@code null} if there is
     *                      none
     */
    public void setRequestingUrl(final URL requestingUrl) {
        requestingUrl_ = requestingUrl == null ? null : requestingUrl.toExternalForm();
    }

    /**
     * Convenience method for the common case of a top-level navigation (an
     * anchor/area click, a form submission, a script-driven location change, ...):
     * sets {@link FetchDestination#DOCUMENT}, the initiator URL, and whether the
     * navigation was backed by genuine user activation, all in one call.
     * <p>
     * Every navigation-triggering call site needs all three of these set
     * together for correct {@code Sec-Fetch-*} headers; bundling them here
     * makes it harder for a call site to set some of them and forget the rest.
     * </p>
     * <p>
     * For navigations whose destination is not {@link FetchDestination#DOCUMENT}
     * (e.g. an {@code <iframe>}/{@code <frame>} load), use
     * {@link #markAsNavigation(FetchDestination, URL, boolean)} instead.
     * </p>
     *
     * @param requestingUrl the URL of the page initiating this navigation, or
     *                      {@code null} if there is none (e.g. a typed URL)
     * @param userActivation whether this navigation was triggered by a real
     *                       user gesture as opposed to script
     */
    public void markAsNavigation(final URL requestingUrl, final boolean userActivation) {
        markAsNavigation(FetchDestination.DOCUMENT, requestingUrl, userActivation);
    }

    /**
     * Same as {@link #markAsNavigation(URL, boolean)}, but for navigations whose
     * destination isn't a top-level {@link FetchDestination#DOCUMENT} - currently
     * only {@code <iframe>}/{@code <frame>} loads ({@link FetchDestination#IFRAME}/
     * {@link FetchDestination#FRAME}).
     *
     * @param destination the navigation's destination
     * @param requestingUrl the URL of the page initiating this navigation, or
     *                      {@code null} if there is none
     * @param userActivation whether this navigation was triggered by a real
     *                       user gesture as opposed to script
     */
    public void markAsNavigation(final FetchDestination destination, final URL requestingUrl,
            final boolean userActivation) {
        setFetchDestination(destination);
        setRequestingUrl(requestingUrl);
        setUserActivation(userActivation);
    }

    /**
     * Sets the specified name/value pair in the additional HTTP headers,
     * replacing any existing header with the same name (case-insensitive).
     *
     * @param name  the header name
     * @param value the header value
     */
    public void setAdditionalHeader(final String name, final String value) {
        if (additionalHeaders_.containsKey(name)) {
            additionalHeaders_.put(name, value);
            return;
        }

        // fall back to case-insensitive scan
        for (final String key : additionalHeaders_.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                additionalHeaders_.put(key, value);
                return;
            }
        }

        // no existing header found, insert with the given name
        additionalHeaders_.put(name, value);
    }

    /**
     * Removes the specified header from the additional HTTP headers.
     * The name comparison is case-insensitive. Does nothing if the
     * header is not present.
     *
     * @param name the header name to remove
     */
    public void removeAdditionalHeader(final String name) {
        if (additionalHeaders_.remove(name) != null) {
            return;
        }

        // fall back to case-insensitive scan
        for (final String key : additionalHeaders_.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                additionalHeaders_.remove(key);
                return;
            }
        }
    }

    /**
     * Returns the credentials extracted from the URL's userinfo component
     * (e.g. {@code http://user:secret@host/}), or {@code null} if none were present.
     *
     * @return the URL-embedded credentials, or {@code null}
     */
    public Credentials getUrlCredentials() {
        return urlCredentials_;
    }

    /**
     * Returns the credentials explicitly set via {@link #setCredentials(Credentials)},
     * or {@code null} if none were set.
     *
     * @return the explicitly configured credentials, or {@code null}
     */
    public Credentials getCredentials() {
        return credentials_;
    }

    /**
     * Sets the credentials to use.
     *
     * @param credentials the credentials to use
     */
    public void setCredentials(final Credentials credentials) {
        credentials_ = credentials;
    }

    /**
     * Returns the character set to use to perform the request.
     *
     * @return the character set to use to perform the request
     */
    public Charset getCharset() {
        return charset_;
    }

    /**
     * Sets the character set to use to perform the request. The default value is
     * {@link java.nio.charset.StandardCharsets#ISO_8859_1}.
     *
     * @param charset the character set to use to perform the request
     */
    public void setCharset(final Charset charset) {
        charset_ = charset;
    }

    /**
     * Returns the default character set to use for the response when it does not
     * specify one.
     *
     * @return the default character set to use for the response when it does not
     *         specify one.
     */
    public Charset getDefaultResponseContentCharset() {
        return defaultResponseContentCharset_;
    }

    /**
     * Sets the default character set to use when the response does not declare
     * one explicitly.
     * <p>
     * Unless overridden, the default is
     * {@link java.nio.charset.StandardCharsets#ISO_8859_1} (per HTTP/1.1).
     * </p>
     *
     * @param defaultResponseContentCharset the fallback charset; must not be {@code null}
     */
    public void setDefaultResponseContentCharset(final Charset defaultResponseContentCharset) {
        WebAssert.notNull("defaultResponseContentCharset", defaultResponseContentCharset);
        defaultResponseContentCharset_ = defaultResponseContentCharset;
    }

    /**
     * Returns whether the given {@link HttpHint} is currently enabled for this request.
     *
     * @param hint the hint to check
     * @return {@code true} if the hint is enabled; {@code false} otherwise
     */
    public boolean hasHint(final HttpHint hint) {
        if (httpHints_ == null) {
            return false;
        }
        return httpHints_.contains(hint);
    }

    /**
     * Enables the given {@link HttpHint} for this request.
     *
     * @param hint the hint to enable
     */
    public void addHint(final HttpHint hint) {
        if (httpHints_ == null) {
            httpHints_ = EnumSet.noneOf(HttpHint.class);
        }
        httpHints_.add(hint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(100)
                .append(getClass().getSimpleName())
                .append("[<url=\"")
                .append(url_)
                .append("\", ").append(httpMethod_)
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
        oos.writeObject(defaultResponseContentCharset_ == null ? null : defaultResponseContentCharset_.name());
    }

    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        final String charsetName = (String) ois.readObject();
        if (charsetName != null) {
            charset_ = Charset.forName(charsetName);
        }
        final String defaultResponseContentCharset = (String) ois.readObject();
        if (defaultResponseContentCharset != null) {
            defaultResponseContentCharset_ = Charset.forName(defaultResponseContentCharset);
        }
    }
}
