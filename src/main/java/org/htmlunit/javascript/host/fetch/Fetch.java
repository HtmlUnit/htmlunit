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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.HttpHeader;
import org.htmlunit.HttpMethod;
import org.htmlunit.ReferrerPolicy;
import org.htmlunit.WebRequest;
import org.htmlunit.WebRequest.FetchMode;
import org.htmlunit.WebResponse;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.BaseFunction;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.csp.ContentSecurityPolicy;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import org.htmlunit.javascript.background.JavaScriptJob;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.worker.DedicatedWorkerGlobalScope;
import org.htmlunit.javascript.host.abort.AbortSignal;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * The implementation of {@code fetch()}.
 *
 * <p>Supported are the request modes 'cors', 'same-origin' and 'no-cors' (opaque responses), the
 * credentials, redirect and cache modes, the referrer options, subresource integrity and the size limit
 * of keepalive requests.</p>
 *
 * @see <a href="https://fetch.spec.whatwg.org/#fetch-method">Fetch Standard - fetch()</a>
 * @author Ronald Brill
 */
public final class Fetch {

    private static final Log LOG = LogFactory.getLog(Fetch.class);

    /** The maximal size of the body of a keepalive request (64 KiB). */
    private static final int KEEPALIVE_MAX_BODY_SIZE = 64 * 1024;

    private static final String[] CONDITIONAL_HEADERS =
        {"If-Modified-Since", "If-None-Match", "If-Unmodified-Since", "If-Match", "If-Range"};

    private Fetch() {
        // util class
    }

    /**
     * Starts a fetch.
     *
     * @param global the window or the worker global scope
     * @param input the input - a url or a {@code Request}
     * @param init the init dictionary or {@code undefined}
     * @return the promise for the {@code Response}
     */
    public static NativePromise fetch(final HtmlUnitScriptable global, final Object input, final Object init) {
        final VarScope scope = global.getParentScope();
        final FetchClient client = FetchClient.of(global);

        // like the browsers, all errors are reported by rejecting the promise
        if (JavaScriptEngine.isUndefined(input)) {
            // fetch() without arguments; we can't distinguish this from fetch(undefined)
            final EcmaError e = JavaScriptEngine.typeError("Failed to execute 'fetch' on '"
                    + client.globalName() + "': 1 argument required, but only 0 present.");
            return rejectedPromise(scope, JavaScriptEngine.toErrorObject(e, scope));
        }

        final Request request;
        final WebRequest webRequest;
        try {
            request = Request.create(global, input, init);
            webRequest = createWebRequest(client, request);
        }
        catch (final EcmaError e) {
            return rejectedPromise(scope, JavaScriptEngine.toErrorObject(e, scope));
        }

        final PendingFetch pending = new PendingFetch(client, request);
        final NativePromise promise = pending.createPromise();

        final AbortSignal signal = request.getSignal();
        if (signal.isAborted()) {
            pending.reject(signal.getReason(), false);
            return promise;
        }

        if (webRequest == null) {
            pending.rejectWithTypeError("Failed to fetch", false);
            return promise;
        }

        pending.start(webRequest);
        return promise;
    }

    /**
     * Creates the {@link WebRequest} for the request.
     *
     * @param client the window or the worker
     * @param request the request
     * @return the web request or {@code null} if the url is not supported (network error)
     */
    private static WebRequest createWebRequest(final FetchClient client, final Request request) {
        final HtmlPage page = client.page();
        if (page == null) {
            return null;
        }

        URL url;
        try {
            url = UrlUtils.toUrlUnsafe(request.getUrl());
        }
        catch (final MalformedURLException e) {
            return null;
        }
        url = ContentSecurityPolicy.upgradeInsecureRequest(page, url);

        final URL originUrl = page.getOriginUrl();
        if (!isSupportedUrl(originUrl, url, request.getMode())) {
            return null;
        }

        final HttpMethod method;
        try {
            method = HttpMethod.valueOf(request.getMethod().toUpperCase(Locale.ROOT));
        }
        catch (final IllegalArgumentException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("fetch(): the HTTP method '" + request.getMethod() + "' is not supported.");
            }
            return null;
        }

        final Headers headers = request.getHeaders();
        String accept = headers.getHeader(HttpHeader.ACCEPT_LC);
        if (accept == null) {
            accept = "*/*";
        }
        final WebRequest webRequest = new WebRequest(url, accept,
                                            client.global().getBrowserVersion().getAcceptEncodingHeader());
        webRequest.setHttpMethod(method);
        webRequest.setCharset(UTF_8);
        webRequest.setDefaultResponseContentCharset(UTF_8);
        webRequest.setFetchDestination(WebRequest.FetchDestination.EMPTY);
        webRequest.setFetchModeOverride(toFetchMode(request.getMode()));
        webRequest.setRequestingUrl(originUrl);
        webRequest.setSiteForCookies(page.getSiteForCookiesUrl());

        // the referrer
        final String referrer = request.getReferrer();
        if (!referrer.isEmpty()) {
            if ("about:client".equals(referrer)) {
                webRequest.setRefererHeader(client.url());
            }
            else {
                try {
                    webRequest.setRefererHeader(UrlUtils.toUrlUnsafe(referrer));
                }
                catch (final MalformedURLException e) {
                    // ignore
                }
            }
            final ReferrerPolicy policy = request.getReferrerPolicy().isEmpty()
                    ? page.getReferrerPolicy()
                    : ReferrerPolicy.fromToken(request.getReferrerPolicy());
            webRequest.setReferrerPolicy(policy);
        }

        // headers with the same name are combined
        final Map<String, NameValuePair> combined = new LinkedHashMap<>();
        for (final NameValuePair header : headers.getHeaderList()) {
            final String key = header.getName().toLowerCase(Locale.ROOT);
            if (!HttpHeader.ACCEPT_LC.equals(key)) {
                final NameValuePair existing = combined.get(key);
                combined.put(key, existing == null
                        ? header
                        : new NameValuePair(existing.getName(), existing.getValue() + ", " + header.getValue()));
            }
        }
        for (final NameValuePair header : combined.values()) {
            webRequest.setAdditionalHeader(header.getName(), header.getValue());
        }

        // the Origin header is sent for requests to other origins in mode 'cors'
        // and for all methods but GET and HEAD
        final boolean corsRequest = !isSameOrigin(originUrl, url) && "cors".equals(request.getMode());
        if ((corsRequest || method != HttpMethod.GET && method != HttpMethod.HEAD)
                && ("http".equals(originUrl.getProtocol()) || "https".equals(originUrl.getProtocol()))) {
            webRequest.setAdditionalHeader(HttpHeader.ORIGIN, CorsLoader.serializeOrigin(originUrl));
        }

        final FetchBody body = request.getFetchBody();
        if (body != null) {
            if (request.isKeepalive() && body.getBytes().length > KEEPALIVE_MAX_BODY_SIZE) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("fetch(): the body of a keepalive request is limited to 64 KiB.");
                }
                return null;
            }
            setRequestBody(webRequest, body.getBytes());
        }

        webRequest.setCacheMode(toCacheMode(request.getCache(), webRequest));
        return webRequest;
    }

    /**
     * Sets the body; the bytes are transferred as string with a charset that restores the bytes.
     */
    private static void setRequestBody(final WebRequest webRequest, final byte[] bytes) {
        final HttpMethod method = webRequest.getHttpMethod();
        if (method == HttpMethod.GET || method == HttpMethod.HEAD || method == HttpMethod.TRACE) {
            return;
        }

        // the encoding type 'null' sends the body as it is
        webRequest.setEncodingType(null);
        try {
            final String body = UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes))
                    .toString();
            webRequest.setRequestBody(body);
        }
        catch (final CharacterCodingException e) {
            // binary content; latin1 maps every byte to one char
            webRequest.setCharset(ISO_8859_1);
            webRequest.setRequestBody(new String(bytes, ISO_8859_1));
        }
    }

    private static boolean isSupportedUrl(final URL originUrl, final URL url, final String mode) {
        final String protocol = url.getProtocol();
        if ("data".equals(protocol)) {
            return !"same-origin".equals(mode);
        }
        if ("blob".equals(protocol)) {
            try {
                final URL blobOrigin = UrlUtils.toUrlUnsafe(url.toExternalForm().substring("blob:".length()));
                return UrlUtils.isSameOrigin(originUrl, blobOrigin);
            }
            catch (final MalformedURLException e) {
                return false;
            }
        }
        if (!"http".equals(protocol) && !"https".equals(protocol)) {
            return false;
        }

        // 'same-origin' does not allow other origins
        return !"same-origin".equals(mode) || UrlUtils.isSameOrigin(originUrl, url);
    }

    /**
     * Returns whether the url has the origin of the page (data: urls and same origin blob: urls too).
     *
     * @param originUrl the url of the origin of the page
     * @param url the url
     * @return whether the url has the origin of the page
     */
    static boolean isSameOrigin(final URL originUrl, final URL url) {
        final String protocol = url.getProtocol();
        if ("data".equals(protocol) || "blob".equals(protocol)) {
            // only same origin blob urls are supported (see isSupportedUrl())
            return true;
        }
        return UrlUtils.isSameOrigin(originUrl, url);
    }

    /**
     * Returns the cache mode; requests with conditional headers bypass the cache.
     *
     * @see <a href="https://fetch.spec.whatwg.org/#http-network-or-cache-fetch">Fetch Standard
     *      - HTTP-network-or-cache fetch</a>
     */
    private static WebRequest.CacheMode toCacheMode(final String cache, final WebRequest webRequest) {
        if ("default".equals(cache)) {
            for (final String conditional : CONDITIONAL_HEADERS) {
                if (webRequest.getAdditionalHeader(conditional) != null) {
                    return WebRequest.CacheMode.NO_STORE;
                }
            }
        }

        return switch (cache) {
            case "no-store" -> WebRequest.CacheMode.NO_STORE;
            case "reload" -> WebRequest.CacheMode.RELOAD;
            case "no-cache" -> WebRequest.CacheMode.NO_CACHE;
            case "force-cache" -> WebRequest.CacheMode.FORCE_CACHE;
            case "only-if-cached" -> WebRequest.CacheMode.ONLY_IF_CACHED;
            default -> WebRequest.CacheMode.DEFAULT;
        };
    }

    private static FetchMode toFetchMode(final String mode) {
        return switch (mode) {
            case "same-origin" -> FetchMode.SAME_ORIGIN;
            case "no-cors" -> FetchMode.NO_CORS;
            default -> FetchMode.CORS;
        };
    }

    private static NativePromise rejectedPromise(final VarScope scope, final Object reason) {
        final Function ctor = (Function) ScriptableObject.getProperty(scope, "Promise");
        final Function reject = (Function) ScriptableObject.getProperty(ctor, "reject");
        return (NativePromise) reject.call(Context.getCurrentContext(), scope, ctor, new Object[] {reason});
    }

    /**
     * The environment of a fetch: a window or a dedicated worker (that uses the window that created it).
     *
     * @param global the window or the worker global scope
     * @param webWindow the web window
     * @param page the page (of the window that created the worker) or {@code null}
     * @param url the url of the client (the page or the worker script); used as referrer
     * @param globalName the name of the global object (for error messages)
     */
    private record FetchClient(HtmlUnitScriptable global, WebWindow webWindow, HtmlPage page, URL url,
            String globalName) {

        /**
         * Creates the client for the window or the worker global scope.
         *
         * @param global the window or the worker global scope
         * @return the client
         */
        static FetchClient of(final HtmlUnitScriptable global) {
            if (global instanceof DedicatedWorkerGlobalScope worker) {
                final WebWindow webWindow = worker.getOwningWindow().getWebWindow();
                final HtmlPage page = webWindow.getEnclosedPage() instanceof HtmlPage p ? p : null;
                URL url = worker.getWorkerUrl();
                if (url == null && page != null) {
                    url = page.getUrl();
                }
                return new FetchClient(global, webWindow, page, url, "WorkerGlobalScope");
            }

            final WebWindow webWindow = ((Window) global).getWebWindow();
            final HtmlPage page = webWindow.getEnclosedPage() instanceof HtmlPage p ? p : null;
            return new FetchClient(global, webWindow, page, page == null ? null : page.getUrl(), "Window");
        }
    }

    /**
     * A running fetch: holds the promise functions and settles the promise once.
     */
    private static final class PendingFetch {
        private final FetchClient client_;
        private final HtmlUnitScriptable global_;
        private final Request request_;
        private final VarScope scope_;
        private Function resolve_;
        private Function reject_;
        private boolean settled_;
        private int jobId_ = -1;
        private Runnable abortAlgorithm_;

        PendingFetch(final FetchClient client, final Request request) {
            client_ = client;
            global_ = client.global();
            request_ = request;
            scope_ = global_.getParentScope();
        }

        NativePromise createPromise() {
            final BaseFunction executor = new BaseFunction(scope_) {
                @Override
                public Object call(final Context cx, final Object nt, final VarScope scope,
                        final Object thisObj, final Object[] args) {
                    resolve_ = (Function) args[0];
                    reject_ = (Function) args[1];
                    return JavaScriptEngine.UNDEFINED;
                }
            };
            return (NativePromise) JavaScriptEngine.newObject(scope_, "Promise", new Object[] {executor});
        }

        void start(final WebRequest webRequest) {
            final WebWindow webWindow = client_.webWindow();
            final HtmlPage page = client_.page();
            final JavaScriptEngine engine = (JavaScriptEngine) webWindow.getWebClient().getJavaScriptEngine();

            abortAlgorithm_ = () -> {
                if (jobId_ != -1) {
                    webWindow.getJobManager().removeJob(jobId_);
                }
                // called by the script that aborts
                reject(request_.getSignal().getReason(), false);
            };
            request_.getSignal().addAbortAlgorithm(abortAlgorithm_);

            final ContextAction<Object> action = new ContextAction<>() {
                @Override
                public Object run(final Context cx) {
                    load(webWindow, webRequest);
                    return null;
                }

                @Override
                public String toString() {
                    return "fetch " + webRequest.getHttpMethod() + " '" + webRequest.getUrl() + "'";
                }
            };
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
                    .createJavascriptXMLHttpRequestJob(engine.getContextFactory(), action);
            jobId_ = webWindow.getJobManager().addJob(job, page);
        }

        private void load(final WebWindow webWindow, final WebRequest webRequest) {
            final CorsLoader.Result result;
            final byte[] content;
            final boolean includeCredentials = "include".equals(request_.getCredentials());
            try {
                result = loadResponse(webWindow, webRequest);
                content = result.opaqueRedirect() ? null : readContent(result.response());
            }
            catch (final IOException e) {
                LOG.debug("fetch(): network error", e);
                rejectWithTypeError("Failed to fetch", true);
                return;
            }

            final WebResponse webResponse = result.response();
            final boolean opaque = result.corsTainting() && "no-cors".equals(request_.getMode());

            // subresource integrity; the content of an opaque response can't be checked
            final String integrity = request_.getIntegrity();
            if (!integrity.isEmpty() && !result.opaqueRedirect()
                    && (opaque || !SubresourceIntegrity.matches(integrity, content))) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("fetch(): the integrity check for '" + result.url() + "' failed.");
                }
                rejectWithTypeError("Failed to fetch", true);
                return;
            }

            final int status = webResponse.getStatusCode();
            FetchBody body = null;
            if (content != null && !isNullBodyStatus(status)
                    && webRequest.getHttpMethod() != HttpMethod.HEAD) {
                body = FetchBody.of(content, null);
            }

            final Response response;
            if (opaque && !result.opaqueRedirect()) {
                // nothing of an opaque response is visible
                response = Response.createNetworkResponse(global_, "opaque", "",
                        false, 0, "", new ArrayList<>(), null);
            }
            else if (result.opaqueRedirect()) {
                response = Response.createNetworkResponse(global_, "opaqueredirect", toResponseUrl(result.url()),
                        false, 0, "", new ArrayList<>(), null);
            }
            else {
                // the cookie headers are never exposed; for responses of other origins only the
                // CORS-safelisted and the exposed headers are visible
                final List<NameValuePair> headers = new ArrayList<>();
                for (final NameValuePair header : webResponse.getResponseHeaders()) {
                    if (CorsLoader.isExposedResponseHeader(webResponse, header.getName(),
                            result.corsTainting(), includeCredentials)) {
                        headers.add(header);
                    }
                }
                response = Response.createNetworkResponse(global_, result.corsTainting() ? "cors" : "basic",
                        toResponseUrl(result.url()), result.redirected(), status, webResponse.getStatusMessage(),
                        headers, body);
            }
            // no cleanUp() of the web response; the response may be stored in the cache

            settle(resolve_, response, true);
        }

        private CorsLoader.Result loadResponse(final WebWindow webWindow, final WebRequest webRequest)
                throws IOException {
            final CorsLoader.CredentialsMode credentialsMode = switch (request_.getCredentials()) {
                case "omit" -> CorsLoader.CredentialsMode.OMIT;
                case "include" -> CorsLoader.CredentialsMode.INCLUDE;
                default -> CorsLoader.CredentialsMode.SAME_ORIGIN;
            };
            final CorsLoader.RedirectMode redirectMode = switch (request_.getRedirect()) {
                case "error" -> CorsLoader.RedirectMode.ERROR;
                case "manual" -> CorsLoader.RedirectMode.MANUAL;
                default -> CorsLoader.RedirectMode.FOLLOW;
            };
            final CorsLoader.RequestMode requestMode = switch (request_.getMode()) {
                case "same-origin" -> CorsLoader.RequestMode.SAME_ORIGIN;
                case "no-cors" -> CorsLoader.RequestMode.NO_CORS;
                default -> CorsLoader.RequestMode.CORS;
            };
            final CorsLoader loader = new CorsLoader(webWindow, credentialsMode, redirectMode, 0, requestMode);

            final URL url = webRequest.getUrl();
            loader.ensureConnectionAllowed(url, url);

            final boolean sameOrigin = isSameOrigin(webRequest.getRequestingUrl(), url);
            if (!sameOrigin && requestMode == CorsLoader.RequestMode.NO_CORS
                    && redirectMode != CorsLoader.RedirectMode.FOLLOW) {
                throw new CorsLoader.RedirectException(
                        "The redirect mode has to be 'follow' for a request in mode 'no-cors'.");
            }
            if (!sameOrigin && requestMode == CorsLoader.RequestMode.CORS && CorsLoader.isPreflight(webRequest)) {
                final WebResponse preflightResponse = loader.sendPreflight(webRequest);
                if (!preflightResponse.isSuccessOrUseProxyOrNotModified()
                        || !loader.isPreflightAuthorized(webRequest, preflightResponse)) {
                    throw new CorsLoader.NoPermittedHeaderException(
                            "No permitted \"Access-Control-Allow-Origin\" header.");
                }
            }

            return loader.load(webRequest, sameOrigin);
        }

        private static byte[] readContent(final WebResponse webResponse) throws IOException {
            try (InputStream in = webResponse.getContentAsStream()) {
                if (in == null) {
                    return new byte[0];
                }
                return IOUtils.toByteArray(in);
            }
        }

        private static boolean isNullBodyStatus(final int status) {
            return status == 101 || status == 103 || status == 204 || status == 205 || status == 304;
        }

        private static String toResponseUrl(final URL url) {
            try {
                return UrlUtils.getUrlWithoutRef(url).toExternalForm();
            }
            catch (final MalformedURLException e) {
                return url.toExternalForm();
            }
        }

        void reject(final Object reason, final boolean fromJob) {
            settle(reject_, reason, fromJob);
        }

        void rejectWithTypeError(final String message, final boolean fromJob) {
            final EcmaError error = JavaScriptEngine.typeError(message);
            reject(JavaScriptEngine.toErrorObject(error, scope_), fromJob);
        }

        /**
         * Resolves or rejects the promise (only the first call has an effect).
         *
         * @param function the resolve or the reject function
         * @param value the value
         * @param fromJob true if called from the background job; false if called from a running script
         */
        private void settle(final Function function, final Object value, final boolean fromJob) {
            synchronized (this) {
                if (settled_) {
                    return;
                }
                settled_ = true;
            }
            if (!fromJob) {
                if (abortAlgorithm_ != null) {
                    request_.getSignal().removeAbortAlgorithm(abortAlgorithm_);
                }
                function.call(Context.getCurrentContext(), scope_, global_, new Object[] {value});
                return;
            }

            // the abort algorithm stays registered (the signal is not thread safe); it does nothing
            // because the promise is already settled

            // the job is not a script; the engine takes care of the page lock and the microtasks
            final WebWindow webWindow = client_.webWindow();
            if (webWindow.getEnclosedPage() instanceof HtmlPage page) {
                final JavaScriptEngine engine = (JavaScriptEngine) webWindow.getWebClient().getJavaScriptEngine();
                engine.callFunction(page, function, scope_, global_, new Object[] {value});
            }
        }
    }
}
