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
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import org.htmlunit.javascript.background.JavaScriptJob;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.abort.AbortSignal;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * The implementation of {@code fetch()}.
 *
 * <p>Requests to other origins are supported in the mode 'cors' only; the mode 'no-cors'
 * (opaque responses) is not implemented yet.</p>
 *
 * @see <a href="https://fetch.spec.whatwg.org/#fetch-method">Fetch Standard - fetch()</a>
 * @author Ronald Brill
 */
public final class Fetch {

    private static final Log LOG = LogFactory.getLog(Fetch.class);

    private Fetch() {
        // util class
    }

    /**
     * Starts a fetch.
     *
     * @param window the window
     * @param input the input - a url or a {@code Request}
     * @param init the init dictionary or {@code undefined}
     * @return the promise for the {@code Response}
     */
    public static NativePromise fetch(final Window window, final Object input, final Object init) {
        final VarScope scope = window.getParentScope();

        // like the browsers, all errors are reported by rejecting the promise
        if (JavaScriptEngine.isUndefined(input)) {
            // fetch() without arguments; we can't distinguish this from fetch(undefined)
            final EcmaError e = JavaScriptEngine.typeError(
                    "Failed to execute 'fetch' on 'Window': 1 argument required, but only 0 present.");
            return rejectedPromise(scope, JavaScriptEngine.toErrorObject(e, scope));
        }

        final Request request;
        final WebRequest webRequest;
        try {
            request = Request.create(window, input, init);
            webRequest = createWebRequest(window, request);
        }
        catch (final EcmaError e) {
            return rejectedPromise(scope, JavaScriptEngine.toErrorObject(e, scope));
        }

        final PendingFetch pending = new PendingFetch(window, request);
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
     * @param window the window
     * @param request the request
     * @return the web request or {@code null} if the url is not supported (network error)
     */
    private static WebRequest createWebRequest(final Window window, final Request request) {
        if (!(window.getWebWindow().getEnclosedPage() instanceof HtmlPage page)) {
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
                                            window.getBrowserVersion().getAcceptEncodingHeader());
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
                webRequest.setRefererHeader(page.getUrl());
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

        // the Origin header is sent for requests to other origins and for all methods but GET and HEAD
        if ((!isSameOrigin(originUrl, url) || method != HttpMethod.GET && method != HttpMethod.HEAD)
                && ("http".equals(originUrl.getProtocol()) || "https".equals(originUrl.getProtocol()))) {
            webRequest.setAdditionalHeader(HttpHeader.ORIGIN, CorsLoader.serializeOrigin(originUrl));
        }

        final FetchBody body = request.getFetchBody();
        if (body != null) {
            setRequestBody(webRequest, body.getBytes());
        }

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

        if (!UrlUtils.isSameOrigin(originUrl, url)) {
            if ("no-cors".equals(mode)) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("fetch(): requests to other origins with mode 'no-cors' are not supported yet ("
                            + url + ").");
                }
                return false;
            }
            // 'same-origin' does not allow other origins
            return "cors".equals(mode);
        }
        return true;
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
     * A running fetch: holds the promise functions and settles the promise once.
     */
    private static final class PendingFetch {
        private final Window window_;
        private final Request request_;
        private final VarScope scope_;
        private Function resolve_;
        private Function reject_;
        private boolean settled_;
        private int jobId_ = -1;
        private Runnable abortAlgorithm_;

        PendingFetch(final Window window, final Request request) {
            window_ = window;
            request_ = request;
            scope_ = window.getParentScope();
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
            final WebWindow webWindow = window_.getWebWindow();
            final HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
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
            final int status = webResponse.getStatusCode();
            FetchBody body = null;
            if (content != null && !isNullBodyStatus(status)
                    && webRequest.getHttpMethod() != HttpMethod.HEAD) {
                body = FetchBody.of(content, null);
            }

            final Response response;
            if (result.opaqueRedirect()) {
                response = Response.createNetworkResponse(window_, "opaqueredirect", toResponseUrl(result.url()),
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
                response = Response.createNetworkResponse(window_, result.corsTainting() ? "cors" : "basic",
                        toResponseUrl(result.url()), result.redirected(), status, webResponse.getStatusMessage(),
                        headers, body);
            }
            webResponse.cleanUp();

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
            final CorsLoader loader = new CorsLoader(webWindow, credentialsMode, redirectMode, 0,
                    "same-origin".equals(request_.getMode()));

            final URL url = webRequest.getUrl();
            loader.ensureConnectionAllowed(url, url);

            final boolean sameOrigin = isSameOrigin(webRequest.getRequestingUrl(), url);
            if (!sameOrigin && CorsLoader.isPreflight(webRequest)) {
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
                function.call(Context.getCurrentContext(), scope_, window_, new Object[] {value});
                return;
            }

            // the abort algorithm stays registered (the signal is not thread safe); it does nothing
            // because the promise is already settled

            // the job is not a script; the engine takes care of the page lock and the microtasks
            final WebWindow webWindow = window_.getWebWindow();
            if (webWindow.getEnclosedPage() instanceof HtmlPage page) {
                final JavaScriptEngine engine = (JavaScriptEngine) webWindow.getWebClient().getJavaScriptEngine();
                engine.callFunction(page, function, scope_, window_, new Object[] {value});
            }
        }
    }
}
