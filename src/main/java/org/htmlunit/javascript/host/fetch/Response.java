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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URL;
import java.util.List;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.javascript.host.ReadableStream;
import org.htmlunit.util.NameValuePair;

/**
 * A JavaScript object for {@code Response}.
 *
 * @see <a href="https://fetch.spec.whatwg.org/#response-class">Fetch Standard - Response class</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Response extends HtmlUnitScriptable {

    private static final int[] REDIRECT_STATUSES = {301, 302, 303, 307, 308};

    private String type_ = "default";
    private String url_ = "";
    private boolean redirected_;
    private int status_ = 200;
    private String statusText_ = "";
    private Headers headers_;
    private FetchBody body_;

    /**
     * Default constructor.
     */
    public Response() {
        super();
    }

    /**
     * JavaScript constructor.
     *
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments (the optional body and the optional init)
     * @param ctorObj the function object
     * @param inNewExpr whether invoked via {@code new}
     * @return the new {@code Response} instance
     * @see <a href="https://fetch.spec.whatwg.org/#dom-response">Fetch Standard - new Response()</a>
     */
    @JsxConstructor
    public static Response jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        if (!inNewExpr) {
            throw JavaScriptEngine.typeError("Failed to construct 'Response': Please use the 'new' operator, "
                    + "this DOM object constructor cannot be called as a function.");
        }

        final Response response = new Response();
        response.setParentScope(getTopLevelScope(scope));
        response.setPrototype(((FunctionObject) ctorObj).getClassPrototype());
        response.initHeaders();

        final Object body = args.length > 0 ? args[0] : null;
        final FetchBody fetchBody = body == null || JavaScriptEngine.isUndefined(body)
                ? null : FetchBody.extract(body);
        response.initialize(args.length > 1 ? args[1] : null, fetchBody, "Failed to construct 'Response': ");
        return response;
    }

    /**
     * Creates a new response with the scope and the prototype of the given object.
     *
     * @param owner the object
     * @return the new response
     */
    private static Response newResponse(final HtmlUnitScriptable owner) {
        final Response response = new Response();
        response.setParentScope(owner.getParentScope());
        response.setPrototype(owner.getPrototype(Response.class));
        response.initHeaders();
        return response;
    }

    /**
     * Creates the response of a network request ({@code fetch()}); the headers are immutable.
     *
     * @param global the window or the worker global scope
     * @param type the type ('basic', 'cors', ...)
     * @param url the url of the response
     * @param redirected whether the request was redirected
     * @param status the status
     * @param statusText the status text
     * @param headers the headers
     * @param body the body or {@code null}
     * @return the new response
     */
    static Response createNetworkResponse(final HtmlUnitScriptable global, final String type, final String url,
            final boolean redirected, final int status, final String statusText,
            final List<NameValuePair> headers, final FetchBody body) {
        final Response response = newResponse(global);
        response.type_ = type;
        response.url_ = url;
        response.redirected_ = redirected;
        response.status_ = status;
        response.statusText_ = statusText;
        response.headers_.setGuard(Headers.Guard.NONE);
        for (final NameValuePair header : headers) {
            try {
                response.headers_.appendHeader(header.getName(), header.getValue());
            }
            catch (final EcmaError e) {
                // ignore headers with invalid names or values
            }
        }
        response.headers_.setGuard(Headers.Guard.IMMUTABLE);
        response.body_ = body;
        return response;
    }

    private static Response newResponse(final Scriptable funObj) {
        return newResponse((HtmlUnitScriptable) getWindowOrWorkerGlobalScope(funObj));
    }

    private void initHeaders() {
        headers_ = new Headers();
        headers_.setParentScope(getParentScope());
        headers_.setPrototype(getPrototype(Headers.class));
        headers_.setGuard(Headers.Guard.RESPONSE);
    }

    /**
     * Initializes the response from the init dictionary and the body.
     *
     * @param init the init dictionary or {@code null}
     * @param body the body or {@code null}
     * @param errorPrefix the prefix of the error messages
     * @see <a href="https://fetch.spec.whatwg.org/#initialize-a-response">Fetch Standard
     *      - initialize a response</a>
     */
    private void initialize(final Object init, final FetchBody body, final String errorPrefix) {
        Scriptable initObject = null;
        if (init != null && !JavaScriptEngine.isUndefined(init)) {
            if (!(init instanceof Scriptable)) {
                throw JavaScriptEngine.typeError(errorPrefix
                        + "The provided value is not of type 'ResponseInit'.");
            }
            initObject = (Scriptable) init;
        }

        if (initObject != null) {
            final Object status = getMember(initObject, "status");
            if (status != null) {
                final double statusNumber = JavaScriptEngine.toNumber(status);
                final int statusInt = Double.isNaN(statusNumber) || Double.isInfinite(statusNumber)
                        ? 0 : (int) statusNumber;
                if (statusInt < 200 || statusInt > 599) {
                    throw JavaScriptEngine.rangeError(errorPrefix + "The status provided (" + statusInt
                            + ") is outside the range [200, 599].");
                }
                status_ = statusInt;
            }

            final Object statusText = getMember(initObject, "statusText");
            if (statusText != null) {
                final String text = JavaScriptEngine.toString(statusText);
                if (!isReasonPhrase(text)) {
                    throw JavaScriptEngine.typeError(errorPrefix + "Invalid statusText");
                }
                statusText_ = text;
            }

            final Object headers = getMember(initObject, "headers");
            if (headers != null) {
                headers_.fill(headers);
            }
        }

        if (body != null) {
            if (isNullBodyStatus(status_)) {
                throw JavaScriptEngine.typeError(errorPrefix + "Response with null body status cannot have body");
            }
            body_ = body;
            if (body.getContentType() != null && headers_.getHeader("content-type") == null) {
                headers_.appendHeader("Content-Type", body.getContentType());
            }
        }
    }

    private static Object getMember(final Scriptable object, final String name) {
        final Object value = ScriptableObject.getProperty(object, name);
        if (value == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return null;
        }
        return value;
    }

    private static boolean isNullBodyStatus(final int status) {
        return status == 101 || status == 103 || status == 204 || status == 205 || status == 304;
    }

    /**
     * Checks for a valid reason phrase: HTAB, SP, VCHAR and obs-text only.
     *
     * @param text the text to check
     * @return whether the text is a valid reason phrase
     */
    private static boolean isReasonPhrase(final String text) {
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (c != '\t' && (c < 0x20 || c == 0x7F || c > 0xFF)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a new network error response.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param funObj the function
     * @return the error response
     */
    @JsxStaticFunction
    public static Response error(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        final Response response = newResponse(funObj);
        response.type_ = "error";
        response.status_ = 0;
        response.headers_.setGuard(Headers.Guard.IMMUTABLE);
        return response;
    }

    /**
     * Returns a redirect response.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (the url and the optional status)
     * @param funObj the function
     * @return the redirect response
     */
    @JsxStaticFunction
    public static Response redirect(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        final String errorPrefix = "Failed to execute 'redirect' on 'Response': ";
        if (args.length < 1) {
            throw JavaScriptEngine.typeError(errorPrefix + "1 argument required, but only 0 present.");
        }

        final URL url = Request.parseUrl(JavaScriptEngine.toString(args[0]), funObj, errorPrefix);

        int status = 302;
        if (args.length > 1 && !JavaScriptEngine.isUndefined(args[1])) {
            final double statusNumber = JavaScriptEngine.toNumber(args[1]);
            status = Double.isNaN(statusNumber) || Double.isInfinite(statusNumber) ? 0 : (int) statusNumber;
            boolean valid = false;
            for (final int s : REDIRECT_STATUSES) {
                if (s == status) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                throw JavaScriptEngine.rangeError(errorPrefix + "Invalid status code");
            }
        }

        final Response response = newResponse(funObj);
        response.status_ = status;
        response.headers_.appendHeader("Location", url.toExternalForm());
        response.headers_.setGuard(Headers.Guard.IMMUTABLE);
        return response;
    }

    /**
     * Returns a response with the JSON serialization of the given data as body.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (the data and the optional init)
     * @param funObj the function
     * @return the response
     */
    @JsxStaticFunction(functionName = "json")
    public static Response jsonStatic(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        final String errorPrefix = "Failed to execute 'json' on 'Response': ";
        if (args.length < 1) {
            throw JavaScriptEngine.typeError(errorPrefix + "1 argument required, but only 0 present.");
        }

        final Scriptable global = (Scriptable) getWindowOrWorkerGlobalScope(funObj);
        final Scriptable json = (Scriptable) ScriptableObject.getProperty(global, "JSON");
        final Object serialized = ScriptableObject.callMethod(json, "stringify", new Object[] {args[0]});
        if (serialized == null || JavaScriptEngine.isUndefined(serialized)) {
            throw JavaScriptEngine.typeError(errorPrefix + "The data is not JSON serializable");
        }

        final Response response = newResponse(funObj);
        final FetchBody body = FetchBody.of(JavaScriptEngine.toString(serialized).getBytes(UTF_8),
                "application/json");
        response.initialize(args.length > 1 ? args[1] : null, body, errorPrefix);
        return response;
    }

    /**
     * Returns a copy of this response.
     *
     * @return the copy
     */
    @JsxFunction(functionName = "clone")
    public Response jsClone() {
        if (isBodyUsed()) {
            throw JavaScriptEngine.typeError(
                    "Failed to execute 'clone' on 'Response': Response body is already used");
        }

        final Response clone = newResponse(this);
        clone.type_ = type_;
        clone.url_ = url_;
        clone.redirected_ = redirected_;
        clone.status_ = status_;
        clone.statusText_ = statusText_;
        clone.headers_.setGuard(Headers.Guard.NONE);
        clone.headers_.fill(headers_);
        clone.headers_.setGuard(headers_.getGuard());
        clone.body_ = body_ == null ? null : body_.copy();
        return clone;
    }

    /**
     * Returns the type.
     * @return the type
     */
    @JsxGetter
    public String getType() {
        return type_;
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
     * Returns whether the response is the result of a redirect.
     * @return whether the response is the result of a redirect
     */
    @JsxGetter
    public boolean isRedirected() {
        return redirected_;
    }

    /**
     * Returns the status.
     * @return the status
     */
    @JsxGetter
    public int getStatus() {
        return status_;
    }

    /**
     * Returns whether the status is in the range 200-299.
     * @return whether the status is in the range 200-299
     */
    @JsxGetter
    public boolean isOk() {
        return status_ >= 200 && status_ <= 299;
    }

    /**
     * Returns the status text.
     * @return the status text
     */
    @JsxGetter
    public String getStatusText() {
        return statusText_;
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
     * Returns the body as {@link ReadableStream}.
     * @return the stream or {@code null} if there is no body
     */
    @JsxGetter
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
}
