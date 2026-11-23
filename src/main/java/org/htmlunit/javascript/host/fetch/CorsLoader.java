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
package org.htmlunit.javascript.host.fetch;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.htmlunit.BrowserVersionFeatures.XHR_PREFLIGHT_CORS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.HttpHeader;
import org.htmlunit.HttpMethod;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebRequest.HttpHint;
import org.htmlunit.WebResponse;
import org.htmlunit.WebWindow;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.csp.ContentSecurityPolicy;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * The network part of {@code fetch()} and {@code XMLHttpRequest} for requests in 'cors' mode:
 * the CORS checks, the preflight requests and the redirect handling of the fetch standard.
 *
 * @see <a href="https://fetch.spec.whatwg.org/#http-fetch">Fetch Standard - HTTP fetch</a>
 * @author Ronald Brill
 */
public final class CorsLoader {

    private static final Log LOG = LogFactory.getLog(CorsLoader.class);

    // https://fetch.spec.whatwg.org/#cors-safelisted-response-header-name
    private static final Set<String> CORS_SAFELISTED_RESPONSE_HEADERS = Set.of(
            "cache-control", "content-language", "content-length", "content-type",
            "expires", "last-modified", "pragma");

    private static final String ALLOW_ORIGIN_ALL = "*";

    private static final int MAX_REDIRECTS = 20;

    /**
     * The credentials mode of the request.
     */
    public enum CredentialsMode {
        /** Never send credentials (cookies). */
        OMIT,
        /** Send credentials only to the origin of the page. */
        SAME_ORIGIN,
        /** Always send credentials; responses of other origins have to allow this. */
        INCLUDE
    }

    /**
     * The mode of the request (the modes 'navigate' and 'websocket' are not supported here).
     */
    public enum RequestMode {
        /** Requests to other origins have to pass the CORS checks. */
        CORS,
        /** Only requests to the origin of the page are allowed (also after redirects). */
        SAME_ORIGIN,
        /** Requests to other origins are not checked; their responses are opaque. */
        NO_CORS
    }

    /**
     * The redirect mode of the request.
     */
    public enum RedirectMode {
        /** Follow the redirects. */
        FOLLOW,
        /** A redirect is a network error. */
        ERROR,
        /** The redirect response is returned (as opaque redirect). */
        MANUAL
    }

    /**
     * The result of {@link CorsLoader#load(WebRequest, boolean)}.
     *
     * @param response the response
     * @param url the url of the response
     * @param redirected whether a redirect was followed
     * @param corsTainting whether an url of another origin was involved (response tainting 'cors' or,
     *        for the request mode 'no-cors', 'opaque')
     * @param opaqueRedirect whether the response is a redirect returned because of {@link RedirectMode#MANUAL}
     */
    public record Result(WebResponse response, URL url, boolean redirected,
            boolean corsTainting, boolean opaqueRedirect) {
    }

    /**
     * Thrown if the CORS check fails.
     */
    public static final class NoPermittedHeaderException extends IOException {
        /**
         * Ctor.
         * @param msg the message
         */
        public NoPermittedHeaderException(final String msg) {
            super(msg);
        }
    }

    /**
     * A redirect that is not allowed (or not possible); this is a network error.
     */
    public static final class RedirectException extends IOException {
        /**
         * Ctor.
         * @param msg the message
         */
        public RedirectException(final String msg) {
            super(msg);
        }

        /**
         * Ctor.
         * @param msg the message
         * @param cause the cause
         */
        public RedirectException(final String msg, final Throwable cause) {
            super(msg, cause);
        }
    }

    private final WebWindow webWindow_;
    private final CredentialsMode credentialsMode_;
    private final RedirectMode redirectMode_;
    private final int timeout_;
    private final RequestMode requestMode_;

    /**
     * Ctor.
     *
     * @param webWindow the window of the page that sends the request
     * @param credentialsMode the credentials mode
     * @param redirectMode the redirect mode
     * @param timeout the timeout for the preflight requests (0 means no timeout)
     */
    public CorsLoader(final WebWindow webWindow, final CredentialsMode credentialsMode,
            final RedirectMode redirectMode, final int timeout) {
        this(webWindow, credentialsMode, redirectMode, timeout, RequestMode.CORS);
    }

    /**
     * Ctor.
     *
     * @param webWindow the window of the page that sends the request
     * @param credentialsMode the credentials mode
     * @param redirectMode the redirect mode
     * @param timeout the timeout for the preflight requests (0 means no timeout)
     * @param requestMode the request mode
     */
    public CorsLoader(final WebWindow webWindow, final CredentialsMode credentialsMode,
            final RedirectMode redirectMode, final int timeout, final RequestMode requestMode) {
        webWindow_ = webWindow;
        credentialsMode_ = credentialsMode;
        redirectMode_ = redirectMode;
        timeout_ = timeout;
        requestMode_ = requestMode;
    }

    private boolean isIncludeCredentials() {
        return credentialsMode_ == CredentialsMode.INCLUDE;
    }

    /**
     * Checks the connect-src directive of the Content-Security-Policy of the page.
     *
     * @param url the url to connect to (the request url or a redirect target)
     * @param reportedUrl the url reported in case of a violation (the url of the request)
     * @throws IOException if the connection is not allowed
     */
    public void ensureConnectionAllowed(final URL url, final URL reportedUrl) throws IOException {
        if (webWindow_ != null
                && webWindow_.getEnclosedPage() instanceof HtmlPage page
                && !ContentSecurityPolicy.allowsConnection(page, url, reportedUrl)) {
            throw new IOException("Refused to connect to '" + url
                    + "' because it violates the Content Security Policy of the page.");
        }
    }

    /**
     * Loads the response of the request and follows the redirects the way the
     * <a href="https://fetch.spec.whatwg.org/#http-redirect-fetch">fetch standard</a> describes it for a
     * request in cors mode.
     * <ul>
     *   <li>only http and https locations are followed, not more than {@value #MAX_REDIRECTS} times</li>
     *   <li>as soon as a url of another origin is involved, all responses (also the ones that redirect)
     *       have to pass the CORS check and cookies are only sent if credentials are included</li>
     *   <li>the Origin header is the one of the page; after a redirect from one foreign origin to another
     *       one it is 'null'</li>
     *   <li>a redirect to a foreign url with credentials is a network error</li>
     *   <li>301/302 (for POST) and 303 switch to GET and drop the body; 307/308 keep method and body</li>
     *   <li>the Authorization header and the credentials of the request are dropped if the redirect
     *       leaves the origin</li>
     * </ul>
     * The preflight of the (first) request has to be done by the caller.
     *
     * @param webRequest the request
     * @param sameOrigin whether the url of the request has the origin of the page
     * @return the result
     * @throws IOException in case of a network error
     */
    public Result load(final WebRequest webRequest, final boolean sameOrigin) throws IOException {
        final WebClient wc = webWindow_.getWebClient();
        final URL pageUrl = webRequest.getRequestingUrl();
        final String pageOrigin = serializeOrigin(pageUrl);

        WebRequest request = webRequest;
        // the response tainting 'cors' of the fetch standard: as soon as an url of another origin is involved
        boolean corsTainting = !sameOrigin;
        // the origin of the request is 'null' after a redirect from one foreign origin to another one
        boolean taintedOrigin = false;

        for (int redirects = 0;; redirects++) {
            // the redirects are handled here, the web client has to return the response as it is
            request.addHint(HttpHint.DoNotFollowRedirects);
            if (credentialsMode_ == CredentialsMode.OMIT
                    || corsTainting && credentialsMode_ == CredentialsMode.SAME_ORIGIN) {
                request.addHint(HttpHint.BlockCookies);
            }

            final WebResponse response = wc.loadWebResponse(request);
            final String originHeader = request.getAdditionalHeaders().get(HttpHeader.ORIGIN);
            final URL currentUrl = request.getUrl();

            URL nextUrl = wc.getOptions().isRedirectEnabled() ? getRedirectUrl(response, currentUrl) : null;
            // upgrade-insecure-requests of the Content-Security-Policy
            if (nextUrl != null && webWindow_.getEnclosedPage() instanceof HtmlPage page) {
                nextUrl = ContentSecurityPolicy.upgradeInsecureRequest(page, nextUrl);
            }

            // each response of a cors request has to pass the check, also the one that redirects
            // (in mode 'no-cors' the response is opaque instead)
            if (corsTainting && requestMode_ != RequestMode.NO_CORS && !isCorsAllowed(response, originHeader)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No permitted \"Access-Control-Allow-Origin\" header for URL " + currentUrl);
                }
                throw new NoPermittedHeaderException("No permitted \"Access-Control-Allow-Origin\" header.");
            }

            if (nextUrl == null) {
                return new Result(response, currentUrl, redirects > 0, corsTainting, false);
            }

            if (redirectMode_ == RedirectMode.ERROR) {
                throw new RedirectException("Redirect for '" + currentUrl + "' with redirect mode 'error'.");
            }
            if (redirectMode_ == RedirectMode.MANUAL) {
                return new Result(response, currentUrl, redirects > 0, corsTainting, true);
            }

            if (redirects >= MAX_REDIRECTS) {
                throw new RedirectException("Too many redirects for " + webRequest.getUrl());
            }

            ensureConnectionAllowed(nextUrl, webRequest.getUrl());

            final boolean crossOriginRedirect = !UrlUtils.isSameOrigin(currentUrl, nextUrl);
            final boolean foreignTarget = !UrlUtils.isSameOrigin(pageUrl, nextUrl);
            if (requestMode_ == RequestMode.SAME_ORIGIN && foreignTarget) {
                throw new RedirectException("Redirect to another origin with request mode 'same-origin': "
                        + nextUrl);
            }
            if (requestMode_ == RequestMode.CORS && nextUrl.getUserInfo() != null
                    && (corsTainting || foreignTarget)) {
                throw new RedirectException("Redirect to a URL with credentials is not allowed: " + currentUrl);
            }

            if (crossOriginRedirect && !UrlUtils.isSameOrigin(pageUrl, currentUrl)) {
                taintedOrigin = true;
            }
            corsTainting = corsTainting || foreignTarget;

            request = createRedirectRequest(request, response.getStatusCode(), nextUrl, crossOriginRedirect);
            setOriginHeader(request, corsTainting && requestMode_ != RequestMode.NO_CORS, taintedOrigin, pageOrigin);

            if (corsTainting && requestMode_ != RequestMode.NO_CORS && isPreflight(request)) {
                final WebResponse preflightResponse = sendPreflight(request);
                if (!preflightResponse.isSuccessOrUseProxyOrNotModified()
                        || !isPreflightAuthorized(request, preflightResponse)) {
                    throw new NoPermittedHeaderException("No permitted \"Access-Control-Allow-Origin\" header.");
                }
            }
        }
    }

    /**
     * Returns the url a response redirects to.
     *
     * @param response the response
     * @param currentUrl the url the response was loaded from
     * @return the url or {@code null} if the response is not a redirect (or has no location)
     * @throws IOException if the location is invalid or not a http(s) url
     */
    private static URL getRedirectUrl(final WebResponse response, final URL currentUrl) throws IOException {
        final int status = response.getStatusCode();
        if (status != 301 && status != 302 && status != 303 && status != 307 && status != 308) {
            return null;
        }

        String location = response.getResponseHeaderValue("Location");
        if (location == null) {
            return null;
        }
        location = new String(location.getBytes(ISO_8859_1), UTF_8);

        final URL url;
        try {
            url = WebClient.expandUrl(currentUrl, location);
        }
        catch (final MalformedURLException e) {
            throw new RedirectException("Invalid redirect location '" + location + "'", e);
        }

        final String protocol = url.getProtocol();
        if (!"http".equals(protocol) && !"https".equals(protocol)) {
            throw new RedirectException("Redirect to the unsupported scheme '" + protocol + "' is not allowed.");
        }
        return url;
    }

    /**
     * Creates the request for the target of a redirect.
     *
     * @param request the request that was redirected
     * @param status the status of the redirect response
     * @param url the target of the redirect
     * @param crossOriginRedirect whether the redirect leaves the origin of the request
     * @return the new request
     */
    private static WebRequest createRedirectRequest(final WebRequest request, final int status,
            final URL url, final boolean crossOriginRedirect) {
        final HttpMethod method = request.getHttpMethod();
        final boolean toGet = ((status == 301 || status == 302) && method == HttpMethod.POST)
                || (status == 303 && method != HttpMethod.GET && method != HttpMethod.HEAD);

        final WebRequest next = new WebRequest(url, toGet ? HttpMethod.GET : method);
        next.setCharset(request.getCharset());
        next.setDefaultResponseContentCharset(request.getDefaultResponseContentCharset());
        next.setFetchDestination(request.getFetchDestination());
        next.setFetchModeOverride(request.getFetchModeOverride());
        next.setRequestingUrl(request.getRequestingUrl());
        next.setSiteForCookies(request.getSiteForCookies());
        next.setTimeout(request.getTimeout());
        next.setCacheMode(request.getCacheMode());
        if (request.hasHint(HttpHint.IncludeCharsetInContentTypeHeader)) {
            next.addHint(HttpHint.IncludeCharsetInContentTypeHeader);
        }

        for (final Entry<String, String> header : request.getAdditionalHeaders().entrySet()) {
            next.setAdditionalHeader(header.getKey(), header.getValue());
        }

        if (toGet) {
            // the body is gone, and the headers that describe it
            next.removeAdditionalHeader(HttpHeader.CONTENT_TYPE);
            next.removeAdditionalHeader("Content-Length");
            next.removeAdditionalHeader("Content-Encoding");
            next.removeAdditionalHeader("Content-Language");
            next.removeAdditionalHeader("Content-Location");
        }
        else if (request.getRequestBody() != null) {
            next.setRequestBody(request.getRequestBody());
            next.setEncodingType(request.getEncodingType());
        }
        else if (!request.getRequestParameters().isEmpty()) {
            next.setRequestParameters(request.getRequestParameters());
        }

        if (crossOriginRedirect) {
            // credentials are not forwarded to another origin
            next.removeAdditionalHeader("Authorization");
        }
        else {
            next.setCredentials(request.getCredentials());
        }
        return next;
    }

    /**
     * The Origin header is sent for requests of another origin and for all methods but GET and HEAD.
     */
    private static void setOriginHeader(final WebRequest request, final boolean corsTainting,
            final boolean taintedOrigin, final String pageOrigin) {
        final HttpMethod method = request.getHttpMethod();
        if (corsTainting || (method != HttpMethod.GET && method != HttpMethod.HEAD)) {
            request.setAdditionalHeader(HttpHeader.ORIGIN, taintedOrigin ? "null" : pageOrigin);
        }
        else {
            request.removeAdditionalHeader(HttpHeader.ORIGIN);
        }
    }

    /**
     * Serializes the origin of the url (scheme, host and port).
     *
     * @param url the url
     * @return the origin
     */
    public static String serializeOrigin(final URL url) {
        final StringBuilder origin = new StringBuilder().append(url.getProtocol()).append("://")
                .append(url.getHost());
        if (url.getPort() != -1) {
            origin.append(':').append(url.getPort());
        }
        return origin.toString();
    }

    /**
     * The CORS check of the fetch standard.
     *
     * @param response the response
     * @param origin the origin sent with the request
     * @return whether the response passes the check
     */
    public boolean isCorsAllowed(final WebResponse response, final String origin) {
        String value = response.getResponseHeaderValue(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN);
        final boolean allowed = origin != null && origin.equals(value);
        if (isIncludeCredentials()) {
            // second step: check the allow-credentials header for true
            value = response.getResponseHeaderValue(HttpHeader.ACCESS_CONTROL_ALLOW_CREDENTIALS);
            return allowed && Boolean.parseBoolean(value);
        }
        return allowed || ALLOW_ORIGIN_ALL.equals(value);
    }

    /**
     * Sends the preflight request for the given request.
     *
     * @param request the request
     * @return the response of the preflight request
     * @throws IOException in case of a network error
     */
    public WebResponse sendPreflight(final WebRequest request) throws IOException {
        final WebRequest preflightRequest = new WebRequest(request.getUrl(), HttpMethod.OPTIONS);
        preflightRequest.setEncodingType(null);

        // preflight request shouldn't have cookies
        preflightRequest.addHint(HttpHint.BlockCookies);

        // header origin
        final String originHeaderValue = request.getAdditionalHeaders().get(HttpHeader.ORIGIN);
        preflightRequest.setAdditionalHeader(HttpHeader.ORIGIN, originHeaderValue);

        // header request-method
        preflightRequest.setAdditionalHeader(
                HttpHeader.ACCESS_CONTROL_REQUEST_METHOD,
                request.getHttpMethod().name());

        // header request-headers
        final StringBuilder builder = new StringBuilder();
        for (final Entry<String, String> header
                : new TreeMap<>(request.getAdditionalHeaders()).entrySet()) {
            final String name = StringUtils.toRootLowerCase(header.getKey());
            if (isPreflightHeader(name, header.getValue())) {
                if (builder.length() != 0) {
                    builder.append(',');
                }
                builder.append(name);
            }
        }
        preflightRequest.setAdditionalHeader(HttpHeader.ACCESS_CONTROL_REQUEST_HEADERS, builder.toString());
        if (timeout_ > 0) {
            preflightRequest.setTimeout(timeout_);
        }

        final WebClient wc = webWindow_.getWebClient();
        if (wc.getBrowserVersion().hasFeature(XHR_PREFLIGHT_CORS)) {
            // preflightRequest.setFetchModeOverride(FetchMode.CORS);
            preflightRequest.setAdditionalHeader(HttpHeader.SEC_FETCH_MODE, "cors");
        }

        if (webWindow_.getEnclosedPage() instanceof HtmlPage containingPage) {
            preflightRequest.setRefererHeader(containingPage.getUrl());
            preflightRequest.setReferrerPolicy(containingPage.getReferrerPolicy());
        }

        // do the preflight request
        return wc.loadWebResponse(preflightRequest);
    }

    /**
     * Returns whether the request requires a preflight request (if it is a request to another origin).
     *
     * @param request the request
     * @return whether the request requires a preflight request
     */
    public static boolean isPreflight(final WebRequest request) {
        final HttpMethod method = request.getHttpMethod();
        if (method != HttpMethod.GET && method != HttpMethod.HEAD && method != HttpMethod.POST) {
            return true;
        }
        for (final Entry<String, String> header : request.getAdditionalHeaders().entrySet()) {
            if (isPreflightHeader(header.getKey().toLowerCase(Locale.ROOT), header.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the response of the preflight request.
     *
     * @param request the request
     * @param preflightResponse the response of the preflight request
     * @return whether the request is allowed
     */
    public boolean isPreflightAuthorized(final WebRequest request, final WebResponse preflightResponse) {
        // the preflight response has to pass the CORS check too; for a request with credentials
        // this requires the exact origin and Access-Control-Allow-Credentials
        if (!isCorsAllowed(preflightResponse, request.getAdditionalHeaders().get(HttpHeader.ORIGIN))) {
            return false;
        }

        // the wildcard is not supported for requests with credentials
        final boolean wildcard = !isIncludeCredentials();

        final HttpMethod method = request.getHttpMethod();
        if (method != HttpMethod.GET && method != HttpMethod.HEAD && method != HttpMethod.POST
                && !isPreflightMethodAllowed(method.name(), preflightResponse, wildcard)) {
            return false;
        }

        // there is no test case for this because the servlet API has no support
        // for adding the same header twice
        final HashSet<String> accessControlValues = new HashSet<>();
        for (final NameValuePair pair : preflightResponse.getResponseHeaders()) {
            if (HttpHeader.ACCESS_CONTROL_ALLOW_HEADERS.equalsIgnoreCase(pair.getName())) {
                String value = pair.getValue();
                if (value != null) {
                    if (wildcard && ALLOW_ORIGIN_ALL.equals(value)) {
                        // all headers are allowed
                        return true;
                    }
                    value = StringUtils.toRootLowerCase(value);
                    final String[] values = StringUtils.splitAtComma(value);
                    for (String part : values) {
                        part = part.trim();
                        if (!StringUtils.isEmptyOrNull(part)) {
                            accessControlValues.add(part);
                        }
                    }
                }
            }
        }

        for (final Entry<String, String> header : request.getAdditionalHeaders().entrySet()) {
            final String key = StringUtils.toRootLowerCase(header.getKey());
            if (isPreflightHeader(key, header.getValue())
                    && !accessControlValues.contains(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the given method is listed in the Access-Control-Allow-Methods header
     * of the preflight response (the method names are case-sensitive).
     *
     * @param method the method name
     * @param preflightResponse the preflight response
     * @param wildcard whether the wildcard '*' allows all methods
     * @return {@code true} if the method is allowed
     */
    private static boolean isPreflightMethodAllowed(final String method, final WebResponse preflightResponse,
            final boolean wildcard) {
        for (final NameValuePair pair : preflightResponse.getResponseHeaders()) {
            if (HttpHeader.ACCESS_CONTROL_ALLOW_METHODS.equalsIgnoreCase(pair.getName())
                    && pair.getValue() != null) {
                for (final String part : StringUtils.splitAtComma(pair.getValue())) {
                    final String allowed = part.trim();
                    if (method.equals(allowed) || (wildcard && ALLOW_ORIGIN_ALL.equals(allowed))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks whether the given header requires a preflight request. Accept, Accept-Language,
     * Content-Language and Content-Type need no preflight only if the value is CORS-safelisted.
     *
     * @param name the header name (MUST be lower-case for performance reasons)
     * @param value the header value
     * @return {@code true} if this header requires a preflight
     * @see <a href="https://fetch.spec.whatwg.org/#cors-safelisted-request-header">Fetch Standard -
     *     CORS-safelisted request-header</a>
     */
    private static boolean isPreflightHeader(final String name, final String value) {
        if (HttpHeader.REFERER_LC.equals(name)
                || "accept-encoding".equals(name)
                || HttpHeader.ORIGIN_LC.equals(name)) {
            return false;
        }
        return !Headers.isCorsSafelistedRequestHeader(name, value);
    }

    /**
     * Set-Cookie and Set-Cookie2 are never exposed to scripts, otherwise HttpOnly cookies are readable.
     *
     * @see <a href="https://fetch.spec.whatwg.org/#forbidden-response-header-name">Fetch Standard</a>
     * @param name the header name
     * @return {@code true} if the header is a forbidden response header
     */
    public static boolean isForbiddenResponseHeader(final String name) {
        return "set-cookie".equalsIgnoreCase(name) || "set-cookie2".equalsIgnoreCase(name);
    }

    /**
     * Returns whether the response header is visible for scripts. Forbidden response headers are never
     * visible; for a response of a cross-origin request (response tainting 'cors') only the CORS-safelisted
     * response headers and the headers listed in Access-Control-Expose-Headers are visible.
     *
     * @param response the response
     * @param name the header name
     * @param corsTainting whether the response has the response tainting 'cors'
     * @param includeCredentials whether the request included credentials (the wildcard is not supported then)
     * @return {@code true} if the header is visible
     * @see <a href="https://fetch.spec.whatwg.org/#concept-filtered-response-cors">Fetch Standard - CORS filtered
     *     response</a>
     */
    public static boolean isExposedResponseHeader(final WebResponse response, final String name,
            final boolean corsTainting, final boolean includeCredentials) {
        if (isForbiddenResponseHeader(name)) {
            return false;
        }
        if (!corsTainting) {
            return true;
        }

        if (name == null) {
            return false;
        }
        final String nameLC = name.toLowerCase(Locale.ROOT);
        if (CORS_SAFELISTED_RESPONSE_HEADERS.contains(nameLC)) {
            return true;
        }

        for (final NameValuePair header : response.getResponseHeaders()) {
            if (HttpHeader.ACCESS_CONTROL_EXPOSE_HEADERS.equalsIgnoreCase(header.getName())) {
                for (final String token : StringUtils.splitAtComma(header.getValue())) {
                    final String exposed = token.trim();
                    // the wildcard is a header name for requests with credentials
                    if (exposed.equalsIgnoreCase(name) || ("*".equals(exposed) && !includeCredentials)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
