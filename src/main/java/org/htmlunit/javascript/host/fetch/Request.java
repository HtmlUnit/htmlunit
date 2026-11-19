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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.ReadableStream;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.WindowOrWorkerGlobalScope;
import org.htmlunit.javascript.host.abort.AbortSignal;
import org.htmlunit.javascript.host.worker.DedicatedWorkerGlobalScope;
import org.htmlunit.util.UrlUtils;

/**
 * A JavaScript object for {@code Request}.
 *
 * @see <a href="https://fetch.spec.whatwg.org/#request-class">Fetch Standard - Request class</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Request extends HtmlUnitScriptable {

    private static final String ABOUT_CLIENT = "about:client";

    private static final String[] MODES = {"same-origin", "no-cors", "cors"};
    private static final String[] CREDENTIALS = {"omit", "same-origin", "include"};
    private static final String[] CACHE_MODES =
        {"default", "no-store", "reload", "no-cache", "force-cache", "only-if-cached"};
    private static final String[] REDIRECT_MODES = {"follow", "error", "manual"};
    private static final String[] REFERRER_POLICIES = {"no-referrer", "no-referrer-when-downgrade",
        "same-origin", "origin", "strict-origin", "origin-when-cross-origin",
        "strict-origin-when-cross-origin", "unsafe-url"};

    private String method_ = "GET";
    private String url_;
    private Headers headers_;
    private FetchBody body_;
    private String mode_ = "cors";
    private String credentials_ = "same-origin";
    private String cache_ = "default";
    private String redirect_ = "follow";
    private String referrer_ = ABOUT_CLIENT;
    private String referrerPolicy_ = "";
    private String integrity_ = "";
    private boolean keepalive_;
    private AbortSignal signal_;

    /**
     * Default constructor.
     */
    public Request() {
        super();
    }

    /**
     * JavaScript constructor.
     *
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments (the input - a url or a {@code Request} - and the optional init)
     * @param ctorObj the function object
     * @param inNewExpr whether invoked via {@code new}
     * @return the new {@code Request} instance
     * @see <a href="https://fetch.spec.whatwg.org/#dom-request">Fetch Standard - new Request()</a>
     */
    @JsxConstructor
    public static Request jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        if (!inNewExpr) {
            throw JavaScriptEngine.typeError("Failed to construct 'Request': Please use the 'new' operator, "
                    + "this DOM object constructor cannot be called as a function.");
        }
        if (args.length < 1) {
            throw JavaScriptEngine.typeError("Failed to construct 'Request': "
                    + "1 argument required, but only 0 present.");
        }

        final Request request = new Request();
        request.setParentScope(getTopLevelScope(scope));
        request.setPrototype(((FunctionObject) ctorObj).getClassPrototype());

        request.init(args[0], args.length > 1 ? args[1] : JavaScriptEngine.UNDEFINED, ctorObj);
        return request;
    }

    /**
     * Creates a new {@code Request} like {@code new Request(input, init)}; used by {@code fetch()}.
     *
     * @param global the window or the worker global scope
     * @param input the input - a url or a {@code Request}
     * @param init the init dictionary or {@code undefined}
     * @return the new request
     */
    static Request create(final HtmlUnitScriptable global, final Object input, final Object init) {
        final Request request = new Request();
        request.setParentScope(global.getParentScope());
        request.setPrototype(global.getPrototype(Request.class));

        request.init(input, init, global);
        return request;
    }

    private void init(final Object input, final Object init, final Scriptable ctorObj) {
        final Request inputRequest = input instanceof Request r ? r : null;
        final List<AbortSignal> signals = new ArrayList<>();

        if (inputRequest == null) {
            final URL url = parseUrl(JavaScriptEngine.toString(input), ctorObj);
            if (url.getUserInfo() != null) {
                throw JavaScriptEngine.typeError("Failed to construct 'Request': Request cannot be constructed "
                        + "from a URL that includes credentials: " + JavaScriptEngine.toString(input));
            }
            url_ = url.toExternalForm();
        }
        else {
            if (inputRequest.isBodyUsed()) {
                throw JavaScriptEngine.typeError("Failed to construct 'Request': Cannot construct a Request "
                        + "with a Request object that has already been used.");
            }
            url_ = inputRequest.url_;
            method_ = inputRequest.method_;
            mode_ = inputRequest.mode_;
            credentials_ = inputRequest.credentials_;
            cache_ = inputRequest.cache_;
            redirect_ = inputRequest.redirect_;
            referrer_ = inputRequest.referrer_;
            referrerPolicy_ = inputRequest.referrerPolicy_;
            integrity_ = inputRequest.integrity_;
            keepalive_ = inputRequest.keepalive_;
            if (inputRequest.signal_ != null) {
                signals.add(inputRequest.signal_);
            }
        }

        final Scriptable initObject = init instanceof Scriptable s ? s : null;
        if (init != null && !JavaScriptEngine.isUndefined(init) && initObject == null) {
            throw JavaScriptEngine.typeError("Failed to construct 'Request': "
                    + "The provided value is not of type 'RequestInit'.");
        }

        if (initObject != null && initObject.getIds().length > 0) {
            // a non empty init resets these values
            referrer_ = ABOUT_CLIENT;
            referrerPolicy_ = "";
        }

        if (initObject != null) {
            final Object referrer = getMember(initObject, "referrer");
            if (referrer != null) {
                final String referrerString = JavaScriptEngine.toString(referrer);
                if (referrerString.isEmpty()) {
                    referrer_ = "";
                }
                else {
                    final URL referrerUrl = parseUrl(referrerString, ctorObj);
                    // a referrer of another origin is replaced by 'about:client'
                    if (ABOUT_CLIENT.equals(referrerUrl.toExternalForm())
                            || !UrlUtils.isSameOrigin(referrerUrl, getOwnerUrl(ctorObj))) {
                        referrer_ = ABOUT_CLIENT;
                    }
                    else {
                        referrer_ = referrerUrl.toExternalForm();
                    }
                }
            }

            final Object referrerPolicy = getMember(initObject, "referrerPolicy");
            if (referrerPolicy != null) {
                final String policy = JavaScriptEngine.toString(referrerPolicy);
                if (!policy.isEmpty()) {
                    checkEnum("referrerPolicy", policy, REFERRER_POLICIES);
                }
                referrerPolicy_ = policy;
            }

            final Object mode = getMember(initObject, "mode");
            if (mode != null) {
                final String modeString = JavaScriptEngine.toString(mode);
                if ("navigate".equals(modeString)) {
                    throw JavaScriptEngine.typeError("Failed to construct 'Request': Cannot construct a Request "
                            + "with a RequestInit whose mode member is set as 'navigate'.");
                }
                mode_ = checkEnum("mode", modeString, MODES);
            }

            final Object credentials = getMember(initObject, "credentials");
            if (credentials != null) {
                credentials_ = checkEnum("credentials", JavaScriptEngine.toString(credentials), CREDENTIALS);
            }

            final Object cache = getMember(initObject, "cache");
            if (cache != null) {
                cache_ = checkEnum("cache", JavaScriptEngine.toString(cache), CACHE_MODES);
            }
            if ("only-if-cached".equals(cache_) && !"same-origin".equals(mode_)) {
                throw JavaScriptEngine.typeError("Failed to construct 'Request': 'only-if-cached' can be set "
                        + "only with 'same-origin' mode");
            }

            final Object redirect = getMember(initObject, "redirect");
            if (redirect != null) {
                redirect_ = checkEnum("redirect", JavaScriptEngine.toString(redirect), REDIRECT_MODES);
            }

            final Object integrity = getMember(initObject, "integrity");
            if (integrity != null) {
                integrity_ = JavaScriptEngine.toString(integrity);
            }

            final Object keepalive = getMember(initObject, "keepalive");
            if (keepalive != null) {
                keepalive_ = JavaScriptEngine.toBoolean(keepalive);
            }

            final Object method = getMember(initObject, "method");
            if (method != null) {
                method_ = normalizeMethod(JavaScriptEngine.toString(method));
            }

            // null is a valid value for the signal; it removes the signal of the input request
            final Object signal = ScriptableObject.getProperty(initObject, "signal");
            if (signal != Scriptable.NOT_FOUND && !JavaScriptEngine.isUndefined(signal)) {
                if (signal != null && !(signal instanceof AbortSignal)) {
                    throw JavaScriptEngine.typeError("Failed to construct 'Request': Failed to read the 'signal' "
                            + "property from 'RequestInit': Failed to convert value to 'AbortSignal'.");
                }
                signals.clear();
                if (signal != null) {
                    signals.add((AbortSignal) signal);
                }
            }
        }

        // like the browsers, the signal of a request follows the given signal
        signal_ = AbortSignal.createDependentSignal(this, signals);

        // the headers
        headers_ = new Headers();
        headers_.setParentScope(getParentScope());
        headers_.setPrototype(getPrototype(Headers.class));
        headers_.setGuard(Headers.Guard.REQUEST);
        if ("no-cors".equals(mode_)) {
            if (!"GET".equals(method_) && !"HEAD".equals(method_) && !"POST".equals(method_)) {
                throw JavaScriptEngine.typeError("Failed to construct 'Request': '" + method_
                        + "' is unsupported in no-cors mode.");
            }
            // only the no-CORS-safelisted request headers are allowed
            headers_.setGuard(Headers.Guard.REQUEST_NO_CORS);
        }

        final Object initHeaders = initObject == null ? null : getMember(initObject, "headers");
        if (initHeaders != null) {
            headers_.fill(initHeaders);
        }
        else if (inputRequest != null) {
            headers_.fill(inputRequest.headers_);
        }

        // the body
        final Object initBody = initObject == null ? null : getMember(initObject, "body");
        final FetchBody inputBody = inputRequest == null ? null : inputRequest.body_;
        if ((initBody != null || inputBody != null) && ("GET".equals(method_) || "HEAD".equals(method_))) {
            throw JavaScriptEngine.typeError("Failed to construct 'Request': Request with GET/HEAD method "
                    + "cannot have body.");
        }

        if (initBody != null) {
            body_ = FetchBody.extract(initBody);
            if (body_ != null && body_.getContentType() != null && headers_.getHeader("content-type") == null) {
                headers_.appendHeader("Content-Type", body_.getContentType());
            }
        }
        else if (inputBody != null) {
            // the body is taken from the input
            body_ = inputBody.copy();
            inputBody.markUsed();
        }
    }

    private static Object getMember(final Scriptable object, final String name) {
        final Object value = ScriptableObject.getProperty(object, name);
        if (value == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return null;
        }
        return value;
    }

    private static String checkEnum(final String member, final String value, final String[] allowed) {
        for (final String a : allowed) {
            if (a.equals(value)) {
                return value;
            }
        }
        throw invalidEnum(member, value);
    }

    private static EcmaError invalidEnum(final String member, final String value) {
        return JavaScriptEngine.typeError("Failed to construct 'Request': Failed to read the '" + member
                + "' property from 'RequestInit': The provided value '" + value
                + "' is not a valid enum value of type Request" + Character.toUpperCase(member.charAt(0))
                + member.substring(1) + ".");
    }

    /**
     * Normalizes the method: the well known methods are converted to upper case;
     * CONNECT, TRACE and TRACK are forbidden.
     *
     * @param method the method
     * @return the normalized method
     */
    private static String normalizeMethod(final String method) {
        if (method.isEmpty()) {
            throw JavaScriptEngine.typeError("Failed to construct 'Request': '' is not a valid HTTP method.");
        }
        for (int i = 0; i < method.length(); i++) {
            final char c = method.charAt(i);
            if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9'
                    || "!#$%&'*+-.^_`|~".indexOf(c) >= 0)) {
                throw JavaScriptEngine.typeError("Failed to construct 'Request': '" + method
                        + "' is not a valid HTTP method.");
            }
        }

        final String upper = method.toUpperCase(Locale.ROOT);
        if ("CONNECT".equals(upper) || "TRACE".equals(upper) || "TRACK".equals(upper)) {
            throw JavaScriptEngine.typeError("Failed to construct 'Request': '" + method
                    + "' HTTP method is unsupported.");
        }
        if ("DELETE".equals(upper) || "GET".equals(upper) || "HEAD".equals(upper)
                || "OPTIONS".equals(upper) || "POST".equals(upper) || "PUT".equals(upper)) {
            return upper;
        }
        return method;
    }

    private static URL parseUrl(final String url, final Scriptable ctorObj) {
        return parseUrl(url, ctorObj, "Failed to construct 'Request': ");
    }

    /**
     * Parses the url relative to the base url of the document of the window
     * (in workers only absolute urls are supported).
     *
     * @param url the url to parse
     * @param ctorObj a scriptable to find the window or worker global scope
     * @param errorPrefix the prefix of the error message
     * @return the parsed url
     */
    static URL parseUrl(final String url, final Scriptable ctorObj, final String errorPrefix) {
        try {
            final WindowOrWorkerGlobalScope global = getWindowOrWorkerGlobalScope(ctorObj);
            if (global instanceof Window window
                    && window.getWebWindow().getEnclosedPage() instanceof HtmlPage page) {
                return page.getFullyQualifiedUrl(url);
            }
            // in workers the url of the worker script is the base url
            if (global instanceof DedicatedWorkerGlobalScope worker && worker.getWorkerUrl() != null) {
                return UrlUtils.toUrlUnsafe(UrlUtils.resolveUrl(worker.getWorkerUrl(), url));
            }
            return UrlUtils.toUrlUnsafe(url);
        }
        catch (final MalformedURLException e) {
            throw JavaScriptEngine.typeError(errorPrefix + "Failed to parse URL from " + url);
        }
    }

    private static URL getOwnerUrl(final Scriptable ctorObj) {
        final WindowOrWorkerGlobalScope global = getWindowOrWorkerGlobalScope(ctorObj);
        if (global instanceof Window window && window.getWebWindow().getEnclosedPage() instanceof HtmlPage page) {
            return page.getOriginUrl();
        }
        // a worker has the origin of the page that created it
        if (global instanceof DedicatedWorkerGlobalScope worker
                && worker.getOwningWindow().getWebWindow().getEnclosedPage() instanceof HtmlPage page) {
            return page.getOriginUrl();
        }
        return UrlUtils.URL_ABOUT_BLANK;
    }

    /**
     * Returns a copy of this request.
     *
     * @return the copy
     */
    @JsxFunction(functionName = "clone")
    public Request jsClone() {
        if (isBodyUsed()) {
            throw JavaScriptEngine.typeError("Failed to execute 'clone' on 'Request': Request body is already used");
        }

        final Request clone = new Request();
        clone.setParentScope(getParentScope());
        clone.setPrototype(getPrototype(Request.class));

        clone.method_ = method_;
        clone.url_ = url_;
        clone.mode_ = mode_;
        clone.credentials_ = credentials_;
        clone.cache_ = cache_;
        clone.redirect_ = redirect_;
        clone.referrer_ = referrer_;
        clone.referrerPolicy_ = referrerPolicy_;
        clone.integrity_ = integrity_;
        clone.keepalive_ = keepalive_;

        clone.headers_ = new Headers();
        clone.headers_.setParentScope(getParentScope());
        clone.headers_.setPrototype(getPrototype(Headers.class));
        clone.headers_.fill(headers_);
        clone.headers_.setGuard(headers_.getGuard());

        clone.body_ = body_ == null ? null : body_.copy();

        final List<AbortSignal> signals = new ArrayList<>();
        signals.add(signal_);
        clone.signal_ = AbortSignal.createDependentSignal(this, signals);
        return clone;
    }

    /**
     * Returns the method.
     * @return the method
     */
    @JsxGetter
    public String getMethod() {
        return method_;
    }

    /**
     * Returns the url.
     * @return the url
     */
    @JsxGetter
    public String getUrl() {
        return url_;
    }

    /**
     * Returns the headers.
     * @return the headers
     */
    @JsxGetter
    public Headers getHeaders() {
        return headers_;
    }

    /**
     * Returns the destination; always empty for requests created by script.
     * @return the destination
     */
    @JsxGetter
    public String getDestination() {
        return "";
    }

    /**
     * Returns the referrer: 'about:client', an empty string (no referrer) or the url.
     * @return the referrer
     */
    @JsxGetter
    public String getReferrer() {
        return referrer_;
    }

    /**
     * Returns the referrer policy.
     * @return the referrer policy
     */
    @JsxGetter
    public String getReferrerPolicy() {
        return referrerPolicy_;
    }

    /**
     * Returns the mode.
     * @return the mode
     */
    @JsxGetter
    public String getMode() {
        return mode_;
    }

    /**
     * Returns the credentials mode.
     * @return the credentials mode
     */
    @JsxGetter
    public String getCredentials() {
        return credentials_;
    }

    /**
     * Returns the cache mode.
     * @return the cache mode
     */
    @JsxGetter
    public String getCache() {
        return cache_;
    }

    /**
     * Returns the redirect mode.
     * @return the redirect mode
     */
    @JsxGetter
    public String getRedirect() {
        return redirect_;
    }

    /**
     * Returns the integrity metadata.
     * @return the integrity metadata
     */
    @JsxGetter
    public String getIntegrity() {
        return integrity_;
    }

    /**
     * Returns the keepalive flag.
     * @return the keepalive flag
     */
    @JsxGetter
    public boolean isKeepalive() {
        return keepalive_;
    }

    /**
     * Returns the signal.
     * @return the signal
     */
    @JsxGetter
    public AbortSignal getSignal() {
        return signal_;
    }

    /**
     * Returns the duplex mode; always 'half'.
     * @return the duplex mode
     */
    @JsxGetter({CHROME, EDGE})
    public String getDuplex() {
        return "half";
    }

    /**
     * Returns whether this is a history navigation request; always false.
     * @return false
     */
    @JsxGetter({CHROME, EDGE})
    public boolean isIsHistoryNavigation() {
        return false;
    }

    /**
     * Returns whether this is a reload navigation request; always false.
     * @return false
     */
    @JsxGetter({CHROME, EDGE})
    public boolean isIsReloadNavigation() {
        return false;
    }

    /**
     * Returns the body as {@link ReadableStream}.
     * @return the stream or {@code null} if there is no body
     */
    @JsxGetter({CHROME, EDGE})
    public ReadableStream getBody() {
        return body_ == null ? null : body_.getStream(this);
    }

    /**
     * Returns whether the body was used.
     * @return whether the body was used
     */
    @JsxGetter
    public boolean isBodyUsed() {
        return body_ != null && body_.isUsed();
    }

    /**
     * Returns a promise for the body as text.
     * @return the promise
     */
    @JsxFunction
    public NativePromise text() {
        return consume(FetchBody.Kind.TEXT);
    }

    /**
     * Returns a promise for the body parsed as JSON.
     * @return the promise
     */
    @JsxFunction
    public NativePromise json() {
        return consume(FetchBody.Kind.JSON);
    }

    /**
     * Returns a promise for the body as ArrayBuffer.
     * @return the promise
     */
    @JsxFunction
    public NativePromise arrayBuffer() {
        return consume(FetchBody.Kind.ARRAY_BUFFER);
    }

    /**
     * Returns a promise for the body as Uint8Array.
     * @return the promise
     */
    @JsxFunction
    public NativePromise bytes() {
        return consume(FetchBody.Kind.BYTES);
    }

    /**
     * Returns a promise for the body as Blob.
     * @return the promise
     */
    @JsxFunction
    public NativePromise blob() {
        return consume(FetchBody.Kind.BLOB);
    }

    /**
     * Returns a promise for the body as FormData.
     * @return the promise
     */
    @JsxFunction
    public NativePromise formData() {
        return consume(FetchBody.Kind.FORM_DATA);
    }

    private NativePromise consume(final FetchBody.Kind kind) {
        try {
            final Object result = FetchBody.consume(body_, kind, this, headers_.getHeader("content-type"));
            return setupPromise(() -> result);
        }
        catch (final EcmaError e) {
            return setupRejectedPromise(() -> JavaScriptEngine.toErrorObject(e, getParentScope()));
        }
    }

    /**
     * Returns the body.
     *
     * @return the body or {@code null}
     */
    FetchBody getFetchBody() {
        return body_;
    }
}
