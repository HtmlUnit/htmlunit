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

import java.util.List;

import org.htmlunit.WebResponse;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.util.NameValuePair;

/**
 * A JavaScript object for {@code Response}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Response extends FetchBodyMixin {

    private int status_ = 200;
    private String statusText_ = "";
    private Headers headers_;
    private String url_ = "";
    private boolean redirected_;
    private String type_ = "default";

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor(final Object body, final Object init) {
        headers_ = newHeaders();
        if (init instanceof org.htmlunit.corejs.javascript.Scriptable scriptable) {
            final Object status = ScriptableObject.getProperty(scriptable, "status");
            if (!JavaScriptEngine.isUndefined(status)) {
                status_ = JavaScriptEngine.toInt32(status);
            }

            final Object statusText = ScriptableObject.getProperty(scriptable, "statusText");
            if (!JavaScriptEngine.isUndefined(statusText)) {
                statusText_ = JavaScriptEngine.toString(statusText);
            }

            final Object headers = ScriptableObject.getProperty(scriptable, "headers");
            if (!JavaScriptEngine.isUndefined(headers)) {
                headers_ = newHeaders();
                headers_.jsConstructor(headers);
            }
        }
        setBodyFrom(body);
    }

    @JsxStaticFunction
    public static Response ok() {
        final VarScope scope = JavaScriptEngine.getTopCallScope();
        return (Response) JavaScriptEngine.newObject(scope, "Response", new Object[] {JavaScriptEngine.UNDEFINED,
                JavaScriptEngine.UNDEFINED});
    }

    @JsxStaticFunction
    public static Response error() {
        final Response response = ok();
        response.status_ = 0;
        response.statusText_ = "";
        response.type_ = "error";
        return response;
    }

    @JsxStaticFunction
    public static Response redirect(final String url, final Object status) {
        final Response response = ok();
        final int redirectStatus = JavaScriptEngine.isUndefined(status) ? 302 : JavaScriptEngine.toInt32(status);
        response.status_ = redirectStatus;
        response.headers_.set("location", JavaScriptEngine.toString(url));
        return response;
    }

    @JsxGetter
    public boolean isOk() {
        return status_ >= 200 && status_ < 300;
    }

    @JsxGetter
    public int getStatus() {
        return status_;
    }

    @JsxGetter
    public String getStatusText() {
        return statusText_;
    }

    @JsxGetter
    public Headers getHeaders() {
        return headers_;
    }

    @JsxGetter
    public String getUrl() {
        return url_;
    }

    @JsxGetter
    public boolean isRedirected() {
        return redirected_;
    }

    @JsxGetter
    public String getType() {
        return type_;
    }

    @JsxFunction
    public Response clone() {
        final Response clone = new Response();
        clone.setParentScope(getParentScope());
        clone.setPrototype(getPrototype(Response.class));
        clone.status_ = status_;
        clone.statusText_ = statusText_;
        clone.headers_ = clone.newHeaders();
        clone.headers_.copyFrom(headers_);
        clone.url_ = url_;
        clone.redirected_ = redirected_;
        clone.type_ = type_;
        copyBodyTo(clone);
        return clone;
    }

    public void setFromWebResponse(final WebResponse webResponse, final String requestUrl) {
        status_ = webResponse.getStatusCode();
        statusText_ = webResponse.getStatusMessage();
        headers_ = newHeaders();
        final List<NameValuePair> responseHeaders = webResponse.getResponseHeaders();
        for (final NameValuePair header : responseHeaders) {
            headers_.appendRaw(header.getName(), header.getValue());
        }

        url_ = webResponse.getWebRequest().getUrl().toExternalForm();
        redirected_ = !requestUrl.equals(url_);
        type_ = "basic";
        setBody(webResponse.getContentAsBytes(), webResponse.getContentType());
    }

    private Headers newHeaders() {
        final Headers headers = new Headers();
        headers.setParentScope(getParentScope());
        headers.setPrototype(getPrototype(Headers.class));
        headers.jsConstructor(JavaScriptEngine.UNDEFINED);
        return headers;
    }

    private void setBodyFrom(final Object body) {
        if (body == null || JavaScriptEngine.isUndefined(body)) {
            setBody(new byte[0], null);
            return;
        }
        if (body instanceof org.htmlunit.javascript.host.file.Blob blob) {
            setBody(blob.getBytes(), blob.getType());
            return;
        }
        if (body instanceof org.htmlunit.corejs.javascript.NativeArrayBufferView view) {
            setBody(view.getBuffer().getBuffer(), null);
            return;
        }
        if (body instanceof org.htmlunit.javascript.host.URLSearchParams params) {
            setBody(params.jsToString().getBytes(UTF_8), "application/x-www-form-urlencoded");
            return;
        }
        setBody(JavaScriptEngine.toString(body).getBytes(UTF_8), null);
    }
}
