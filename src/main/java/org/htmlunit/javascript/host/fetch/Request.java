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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.htmlunit.FormEncodingType;
import org.htmlunit.HttpHeader;
import org.htmlunit.HttpMethod;
import org.htmlunit.Page;
import org.htmlunit.WebRequest;
import org.htmlunit.corejs.javascript.NativeArrayBufferView;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.URLSearchParams;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.javascript.host.xml.FormData;
import org.htmlunit.javascript.host.xml.XMLDocument;

/**
 * A JavaScript object for {@code Request}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Request extends FetchBodyMixin {

    private String url_;
    private String method_ = HttpMethod.GET.name();
    private Headers headers_;
    private String mode_ = "cors";
    private String credentials_ = "same-origin";
    private String cache_ = "default";
    private String redirect_ = "follow";
    private String referrer_ = "about:client";
    private String referrerPolicy_ = "";
    private String integrity_ = "";
    private boolean keepalive_;
    private Object signal_;
    private Object body_;

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor(final Object input, final Object init) {
        headers_ = newHeaders();
        signal_ = JavaScriptEngine.UNDEFINED;

        if (input instanceof Request request) {
            copyFrom(request);
        }
        else {
            final String inputString = JavaScriptEngine.toString(input);
            url_ = resolveUrl(inputString);
        }

        if (init instanceof Scriptable scriptable) {
            applyInit(scriptable);
        }
    }

    @JsxGetter
    public String getUrl() {
        return url_;
    }

    @JsxGetter
    public String getMethod() {
        return method_;
    }

    @JsxGetter
    public Headers getHeaders() {
        return headers_;
    }

    @JsxGetter
    public String getMode() {
        return mode_;
    }

    @JsxGetter
    public String getCredentials() {
        return credentials_;
    }

    @JsxGetter
    public String getCache() {
        return cache_;
    }

    @JsxGetter
    public String getRedirect() {
        return redirect_;
    }

    @JsxGetter
    public String getReferrer() {
        return referrer_;
    }

    @JsxGetter
    public String getReferrerPolicy() {
        return referrerPolicy_;
    }

    @JsxGetter
    public String getIntegrity() {
        return integrity_;
    }

    @JsxGetter
    public boolean isKeepalive() {
        return keepalive_;
    }

    @JsxGetter
    public Object getSignal() {
        return signal_;
    }

    @Override
    @JsxGetter
    public Object getBody() {
        return body_;
    }

    @JsxFunction
    public Request clone() {
        final Request clone = new Request();
        clone.setParentScope(getParentScope());
        clone.setPrototype(getPrototype(Request.class));
        clone.copyFrom(this);
        return clone;
    }

    public WebRequest toWebRequest(final Window window) throws MalformedURLException {
        final URL requestUrl = new URL(url_);
        final String protocol = requestUrl.getProtocol();
        if (!"http".equals(protocol) && !"https".equals(protocol)
                && !"data".equals(protocol) && !"blob".equals(protocol)) {
            throw JavaScriptEngine.typeError("Failed to fetch");
        }

        final WebRequest webRequest = new WebRequest(requestUrl,
                getBrowserVersion().getXmlHttpRequestAcceptHeader(),
                getBrowserVersion().getAcceptEncodingHeader());
        webRequest.setCharset(UTF_8);
        webRequest.setDefaultResponseContentCharset(UTF_8);

        final Page page = window.getWebWindow().getEnclosedPage();
        if (page != null) {
            webRequest.setRefererHeader(page.getUrl());
        }

        webRequest.setHttpMethod(HttpMethod.valueOf(method_));
        for (final org.htmlunit.util.NameValuePair entry : headers_.entriesList()) {
            webRequest.setAdditionalHeader(entry.getName(), entry.getValue());
        }

        applyBodyToWebRequest(webRequest);
        return webRequest;
    }

    private void copyFrom(final Request request) {
        url_ = request.url_;
        method_ = request.method_;
        headers_ = newHeaders();
        headers_.copyFrom(request.headers_);
        mode_ = request.mode_;
        credentials_ = request.credentials_;
        cache_ = request.cache_;
        redirect_ = request.redirect_;
        referrer_ = request.referrer_;
        referrerPolicy_ = request.referrerPolicy_;
        integrity_ = request.integrity_;
        keepalive_ = request.keepalive_;
        signal_ = request.signal_;
        body_ = request.body_;
        request.copyBodyTo(this);
    }

    private Headers newHeaders() {
        final Headers headers = new Headers();
        headers.setParentScope(getParentScope());
        headers.setPrototype(getPrototype(Headers.class));
        headers.jsConstructor(JavaScriptEngine.UNDEFINED);
        return headers;
    }

    private void applyInit(final Scriptable init) {
        final Object method = ScriptableObject.getProperty(init, "method");
        if (!JavaScriptEngine.isUndefined(method)) {
            final String methodString = JavaScriptEngine.toString(method).toUpperCase(Locale.ROOT);
            HttpMethod.validateHttpMethodName(methodString);
            method_ = methodString;
        }

        final Object headers = ScriptableObject.getProperty(init, "headers");
        if (!JavaScriptEngine.isUndefined(headers)) {
            headers_ = newHeaders();
            headers_.jsConstructor(headers);
        }

        final Object body = ScriptableObject.getProperty(init, "body");
        if (!JavaScriptEngine.isUndefined(body) && body != null) {
            if (HttpMethod.GET.name().equals(method_) || HttpMethod.HEAD.name().equals(method_)) {
                throw JavaScriptEngine.typeError("Request with GET/HEAD method cannot have body.");
            }
            body_ = body;
            if (body instanceof Blob blob) {
                setBody(blob.getBytes(), blob.getType());
            }
            else if (body instanceof URLSearchParams params) {
                setBody(params.jsToString().getBytes(UTF_8), "application/x-www-form-urlencoded");
            }
            else if (body instanceof NativeArrayBufferView view) {
                setBody(view.getBuffer().getBuffer(), null);
            }
            else {
                setBody(JavaScriptEngine.toString(body).getBytes(UTF_8), null);
            }
        }

        final Object mode = ScriptableObject.getProperty(init, "mode");
        if (!JavaScriptEngine.isUndefined(mode)) {
            mode_ = JavaScriptEngine.toString(mode);
        }
        final Object credentials = ScriptableObject.getProperty(init, "credentials");
        if (!JavaScriptEngine.isUndefined(credentials)) {
            credentials_ = JavaScriptEngine.toString(credentials);
        }
        final Object cache = ScriptableObject.getProperty(init, "cache");
        if (!JavaScriptEngine.isUndefined(cache)) {
            cache_ = JavaScriptEngine.toString(cache);
        }
        final Object redirect = ScriptableObject.getProperty(init, "redirect");
        if (!JavaScriptEngine.isUndefined(redirect)) {
            redirect_ = JavaScriptEngine.toString(redirect);
        }
        final Object referrer = ScriptableObject.getProperty(init, "referrer");
        if (!JavaScriptEngine.isUndefined(referrer)) {
            referrer_ = JavaScriptEngine.toString(referrer);
        }
        final Object referrerPolicy = ScriptableObject.getProperty(init, "referrerPolicy");
        if (!JavaScriptEngine.isUndefined(referrerPolicy)) {
            referrerPolicy_ = JavaScriptEngine.toString(referrerPolicy);
        }
        final Object integrity = ScriptableObject.getProperty(init, "integrity");
        if (!JavaScriptEngine.isUndefined(integrity)) {
            integrity_ = JavaScriptEngine.toString(integrity);
        }
        final Object keepalive = ScriptableObject.getProperty(init, "keepalive");
        if (!JavaScriptEngine.isUndefined(keepalive)) {
            keepalive_ = JavaScriptEngine.toBoolean(keepalive);
        }
        final Object signal = ScriptableObject.getProperty(init, "signal");
        if (!JavaScriptEngine.isUndefined(signal)) {
            signal_ = signal;
        }
    }

    private String resolveUrl(final String urlString) {
        try {
            final Page page = getWindow().getWebWindow().getEnclosedPage();
            if (page instanceof org.htmlunit.html.HtmlPage htmlPage) {
                return htmlPage.getFullyQualifiedUrl(urlString).toExternalForm();
            }
            return new URL(urlString).toExternalForm();
        }
        catch (final MalformedURLException e) {
            throw JavaScriptEngine.typeError("Failed to construct 'Request': Invalid URL");
        }
    }

    private void applyBodyToWebRequest(final WebRequest webRequest) {
        if (body_ == null || JavaScriptEngine.isUndefined(body_)) {
            return;
        }

        if (body_ instanceof FormData formData) {
            formData.fillRequest(webRequest);
            return;
        }
        if (body_ instanceof URLSearchParams params) {
            params.fillRequest(webRequest);
            webRequest.addHint(WebRequest.HttpHint.IncludeCharsetInContentTypeHeader);
            return;
        }
        if (body_ instanceof Blob blob) {
            blob.fillRequest(webRequest);
            return;
        }
        if (body_ instanceof NativeArrayBufferView view) {
            webRequest.setRequestBody(new String(view.getBuffer().getBuffer(), UTF_8));
            webRequest.setEncodingType(null);
            return;
        }
        if (body_ instanceof HTMLDocument || body_ instanceof XMLDocument) {
            final String body = JavaScriptEngine.toString(body_);
            webRequest.setRequestBody(body);
            webRequest.setCharset(UTF_8);
            if (webRequest.getAdditionalHeader(HttpHeader.CONTENT_TYPE) == null) {
                webRequest.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "text/plain;charset=UTF-8");
                webRequest.setEncodingType(FormEncodingType.TEXT_PLAIN);
            }
            return;
        }

        final String body = JavaScriptEngine.toString(body_);
        if (!body.isEmpty()) {
            webRequest.setRequestBody(body);
            webRequest.setCharset(UTF_8);
            if (webRequest.getAdditionalHeader(HttpHeader.CONTENT_TYPE) == null) {
                webRequest.setEncodingType(FormEncodingType.TEXT_PLAIN);
            }
        }
    }
}
