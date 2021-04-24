/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_ALL_RESPONSE_HEADERS_APPEND_SEPARATOR;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_ALL_RESPONSE_HEADERS_SEPARATE_BY_LF;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_HANDLE_SYNC_NETWORK_ERRORS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_LENGTH_COMPUTABLE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_LOAD_START_ASYNC;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_NO_CROSS_ORIGIN_TO_ABOUT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_OPEN_ALLOW_EMTPY_URL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_PROGRESS_ON_NETWORK_ERROR_ASYNC;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_USE_CONTENT_CHARSET;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.UsernamePasswordCredentials;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebRequest.HttpHint;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.URLSearchParams;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.ProgressEvent;
import com.gargoylesoftware.htmlunit.javascript.host.file.Blob;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.htmlunit.util.WebResponseWrapper;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;

/**
 * A JavaScript object for an {@code XMLHttpRequest}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Stuart Begg
 * @author Ronald Brill
 * @author Sebastian Cato
 * @author Frank Danek
 * @author Jake Cobb
 * @author Thorsten Wendelmuth
 *
 * @see <a href="http://www.w3.org/TR/XMLHttpRequest/">W3C XMLHttpRequest</a>
 * @see <a href="http://developer.apple.com/internet/webcontent/xmlhttpreq.html">Safari documentation</a>
 */
@JsxClass
public class XMLHttpRequest extends XMLHttpRequestEventTarget {

    private static final Log LOG = LogFactory.getLog(XMLHttpRequest.class);

    /** The object has been created, but not initialized (the open() method has not been called). */
    @JsxConstant
    public static final int UNSENT = 0;

    /** The object has been created, but the send() method has not been called. */
    @JsxConstant
    public static final int OPENED = 1;

    /** The send() method has been called, but the status and headers are not yet available. */
    @JsxConstant
    public static final int HEADERS_RECEIVED = 2;

    /** Some data has been received. */
    @JsxConstant
    public static final int LOADING = 3;

    /** All the data has been received; the complete data is available in responseBody and responseText. */
    @JsxConstant
    public static final int DONE = 4;

    private static final String ALLOW_ORIGIN_ALL = "*";

    private static final String[] ALL_PROPERTIES_ = {"onreadystatechange", "readyState", "responseText", "responseXML",
        "status", "statusText", "abort", "getAllResponseHeaders", "getResponseHeader", "open", "send",
        "setRequestHeader"};

    private static Collection<String> PROHIBITED_HEADERS_ = Arrays.asList(
        "accept-charset", HttpHeader.ACCEPT_ENCODING_LC,
        HttpHeader.CONNECTION_LC, HttpHeader.CONTENT_LENGTH_LC, HttpHeader.COOKIE_LC, "cookie2",
        "content-transfer-encoding", "date", "expect",
        HttpHeader.HOST_LC, "keep-alive", HttpHeader.REFERER_LC, "te", "trailer", "transfer-encoding",
        "upgrade", HttpHeader.USER_AGENT_LC, "via");

    private int state_;
    private WebRequest webRequest_;
    private boolean async_;
    private int jobID_;
    private WebResponse webResponse_;
    private String overriddenMimeType_;
    private HtmlPage containingPage_;
    private final boolean caseSensitiveProperties_;
    private boolean withCredentials_;
    private int timeout_ = 0;
    private boolean aborted_;

    /**
     * Creates a new instance.
     */
    @JsxConstructor
    public XMLHttpRequest() {
        this(true);
    }

    /**
     * Creates a new instance.
     * @param caseSensitiveProperties if properties and methods are case sensitive
     */
    public XMLHttpRequest(final boolean caseSensitiveProperties) {
        caseSensitiveProperties_ = caseSensitiveProperties;
        state_ = UNSENT;
    }

    /**
     * Sets the state as specified and invokes the state change handler if one has been set.
     * @param state the new state
     */
    private void setState(final int state) {
        if (state == OPENED
                || state == HEADERS_RECEIVED
                || state == LOADING
                || state == DONE) {
            state_ = state;
            if (LOG.isDebugEnabled()) {
                LOG.debug("State changed to : " + state);
            }
            return;
        }

        LOG.error("Received an unknown state " + state
                        + ", the state is not implemented, please check setState() implementation.");
    }

    private void fireJavascriptEvent(final String eventName) {
        if (aborted_) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Firing javascript XHR event: " + eventName + " for an already aborted request - ignored.");
            }

            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Firing javascript XHR event: " + eventName);
        }

        final boolean isReadyStateChange = Event.TYPE_READY_STATE_CHANGE.equalsIgnoreCase(eventName);
        final Event event;
        if (isReadyStateChange) {
            event = new Event(this, Event.TYPE_READY_STATE_CHANGE);
        }
        else {
            final ProgressEvent progressEvent = new ProgressEvent(this, eventName);

            final boolean lengthComputable = getBrowserVersion().hasFeature(XHR_LENGTH_COMPUTABLE);
            if (lengthComputable) {
                progressEvent.setLengthComputable(true);
            }

            if (webResponse_ != null) {
                final long contentLength = webResponse_.getContentLength();
                progressEvent.setLoaded(contentLength);
                if (lengthComputable) {
                    progressEvent.setTotal(contentLength);
                }
            }
            event = progressEvent;
        }

        executeEventLocally(event);
    }

    /**
     * Returns the current state of the HTTP request. The possible values are:
     * <ul>
     *   <li>0 = unsent</li>
     *   <li>1 = opened</li>
     *   <li>2 = headers_received</li>
     *   <li>3 = loading</li>
     *   <li>4 = done</li>
     * </ul>
     * @return the current state of the HTTP request
     */
    @JsxGetter
    public int getReadyState() {
        return state_;
    }

    /**
     * Returns a string version of the data retrieved from the server.
     * @return a string version of the data retrieved from the server
     */
    @JsxGetter
    public String getResponseText() {
        if (state_ == UNSENT || state_ == OPENED) {
            return "";
        }
        if (webResponse_ != null) {
            final Charset encoding = webResponse_.getContentCharset();
            if (encoding == null) {
                return "";
            }
            final String content = webResponse_.getContentAsString(encoding);
            if (content == null) {
                return "";
            }
            return content;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("XMLHttpRequest.responseText was retrieved before the response was available.");
        }
        return "";
    }

    /**
     * Returns a DOM-compatible document object version of the data retrieved from the server.
     * @return a DOM-compatible document object version of the data retrieved from the server
     */
    @JsxGetter
    public Object getResponseXML() {
        if (webResponse_ == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("XMLHttpRequest.responseXML returns null because there "
                        + "in no web resonse so far (has send() been called?)");
            }
            return null;
        }
        if (webResponse_ instanceof NetworkErrorWebResponse) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("XMLHttpRequest.responseXML returns of a network error ("
                        + ((NetworkErrorWebResponse) webResponse_).getError() + ")");
            }
            return null;
        }
        final String contentType = webResponse_.getContentType();
        if (contentType.isEmpty() || contentType.contains("xml")) {
            final Window w = getWindow();
            try {
                final XmlPage page = new XmlPage(webResponse_, w.getWebWindow());
                final XMLDocument document = new XMLDocument();
                document.setPrototype(getPrototype(document.getClass()));
                document.setParentScope(w);
                document.setDomNode(page);
                return document;
            }
            catch (final IOException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Failed parsing XML document " + webResponse_.getWebRequest().getUrl() + ": "
                            + e.getMessage());
                }
                return null;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("XMLHttpRequest.responseXML was called but the response is "
                + webResponse_.getContentType());
        }
        return null;
    }

    /**
     * Returns the numeric status returned by the server, such as 404 for "Not Found"
     * or 200 for "OK".
     * @return the numeric status returned by the server
     */
    @JsxGetter
    public int getStatus() {
        if (state_ == UNSENT || state_ == OPENED) {
            return 0;
        }
        if (webResponse_ != null) {
            return webResponse_.getStatusCode();
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("XMLHttpRequest.status was retrieved without a response available (readyState: "
                + state_ + ").");
        }
        return 0;
    }

    /**
     * Returns the string message accompanying the status code, such as "Not Found" or "OK".
     * @return the string message accompanying the status code
     */
    @JsxGetter
    public String getStatusText() {
        if (state_ == UNSENT || state_ == OPENED) {
            return "";
        }
        if (webResponse_ != null) {
            return webResponse_.getStatusMessage();
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("XMLHttpRequest.statusText was retrieved without a response available (readyState: "
                + state_ + ").");
        }
        return null;
    }

    /**
     * Cancels the current HTTP request.
     */
    @JsxFunction
    public void abort() {
        getWindow().getWebWindow().getJobManager().stopJob(jobID_);

        webResponse_ = null;
        setState(DONE);
        fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
        fireJavascriptEvent(Event.TYPE_ABORT);
        fireJavascriptEvent(Event.TYPE_LOAD_END);
        aborted_ = true;
    }

    /**
     * Returns the labels and values of all the HTTP headers.
     * @return the labels and values of all the HTTP headers
     */
    @JsxFunction
    public String getAllResponseHeaders() {
        if (state_ == UNSENT || state_ == OPENED) {
            return "";
        }
        if (webResponse_ != null) {
            final StringBuilder builder = new StringBuilder();
            for (final NameValuePair header : webResponse_.getResponseHeaders()) {
                builder.append(header.getName()).append(": ").append(header.getValue());

                if (!getBrowserVersion().hasFeature(XHR_ALL_RESPONSE_HEADERS_SEPARATE_BY_LF)) {
                    builder.append('\r');
                }
                builder.append('\n');
            }
            if (getBrowserVersion().hasFeature(XHR_ALL_RESPONSE_HEADERS_APPEND_SEPARATOR)) {
                if (!getBrowserVersion().hasFeature(XHR_ALL_RESPONSE_HEADERS_SEPARATE_BY_LF)) {
                    builder.append('\r');
                }
                builder.append('\n');
            }
            return builder.toString();
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("XMLHttpRequest.getAllResponseHeaders() was called without a response available (readyState: "
                + state_ + ").");
        }
        return null;
    }

    /**
     * Retrieves the value of an HTTP header from the response body.
     * @param headerName the (case-insensitive) name of the header to retrieve
     * @return the value of the specified HTTP header
     */
    @JsxFunction
    public String getResponseHeader(final String headerName) {
        if (state_ == UNSENT || state_ == OPENED) {
            return null;
        }
        if (webResponse_ != null) {
            return webResponse_.getResponseHeaderValue(headerName);
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("XMLHttpRequest.getAllResponseHeaders(..) was called without a response available (readyState: "
                + state_ + ").");
        }
        return null;
    }

    /**
     * Assigns the destination URL, method and other optional attributes of a pending request.
     * @param method the method to use to send the request to the server (GET, POST, etc)
     * @param urlParam the URL to send the request to
     * @param asyncParam Whether or not to send the request to the server asynchronously, defaults to {@code true}
     * @param user If authentication is needed for the specified URL, the username to use to authenticate
     * @param password If authentication is needed for the specified URL, the password to use to authenticate
     */
    @JsxFunction
    public void open(final String method, final Object urlParam, final Object asyncParam,
        final Object user, final Object password) {
        if ((urlParam == null || "".equals(urlParam)) && !getBrowserVersion().hasFeature(XHR_OPEN_ALLOW_EMTPY_URL)) {
            throw Context.reportRuntimeError("URL for XHR.open can't be empty!");
        }

        // async defaults to true if not specified
        boolean async = true;
        if (!Undefined.isUndefined(asyncParam)) {
            async = ScriptRuntime.toBoolean(asyncParam);
        }

        final String url = Context.toString(urlParam);

        // (URL + Method + User + Password) become a WebRequest instance.
        containingPage_ = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();

        try {
            final URL pageRequestUrl = containingPage_.getUrl();
            final URL fullUrl = containingPage_.getFullyQualifiedUrl(url);
            if (!isAllowCrossDomainsFor(fullUrl)) {
                throw Context.reportRuntimeError("Access to restricted URI denied");
            }

            final WebRequest request = new WebRequest(fullUrl, getBrowserVersion().getXmlHttpRequestAcceptHeader(),
                                                                getBrowserVersion().getAcceptEncodingHeader());
            request.setCharset(UTF_8);
            request.setRefererlHeader(containingPage_.getUrl());

            if (!isSameOrigin(pageRequestUrl, fullUrl)) {
                final StringBuilder origin = new StringBuilder().append(pageRequestUrl.getProtocol()).append("://")
                        .append(pageRequestUrl.getHost());
                if (pageRequestUrl.getPort() != -1) {
                    origin.append(':').append(pageRequestUrl.getPort());
                }
                request.setAdditionalHeader(HttpHeader.ORIGIN, origin.toString());
            }

            try {
                request.setHttpMethod(HttpMethod.valueOf(method.toUpperCase(Locale.ROOT)));
            }
            catch (final IllegalArgumentException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Incorrect HTTP Method '" + method + "'");
                }
                return;
            }

            // password is ignored if no user defined
            if (user != null && !Undefined.isUndefined(user)) {
                final String userCred = user.toString();

                String passwordCred = "";
                if (password != null && !Undefined.isUndefined(password)) {
                    passwordCred = password.toString();
                }

                request.setCredentials(new UsernamePasswordCredentials(userCred, passwordCred));
            }
            webRequest_ = request;
        }
        catch (final MalformedURLException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Unable to initialize XMLHttpRequest using malformed URL '" + url + "'.");
            }
            return;
        }

        // Async stays a boolean.
        async_ = async;

        // Change the state!
        setState(OPENED);
        fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
    }

    private boolean isAllowCrossDomainsFor(final URL newUrl) {
        return !(getBrowserVersion().hasFeature(XHR_NO_CROSS_ORIGIN_TO_ABOUT)
                    && UrlUtils.ABOUT.equals(newUrl.getProtocol()));
    }

    private static boolean isSameOrigin(final URL originUrl, final URL newUrl) {
        if (!originUrl.getHost().equals(newUrl.getHost())) {
            return false;
        }

        int originPort = originUrl.getPort();
        if (originPort == -1) {
            originPort = originUrl.getDefaultPort();
        }
        int newPort = newUrl.getPort();
        if (newPort == -1) {
            newPort = newUrl.getDefaultPort();
        }
        return originPort == newPort;
    }

    /**
     * Sends the specified content to the server in an HTTP request and receives the response.
     * @param content the body of the message being sent with the request
     */
    @JsxFunction
    public void send(final Object content) {
        if (webRequest_ == null) {
            return;
        }
        if (!async_ && timeout_ > 0) {
            Context.throwAsScriptRuntimeEx(new RuntimeException("Synchronous requests must not set a timeout."));
            return;
        }

        prepareRequestContent(content);
        if (timeout_ > 0) {
            webRequest_.setTimeout(timeout_);
        }

        final Window w = getWindow();
        final WebWindow ww = w.getWebWindow();
        final WebClient client = ww.getWebClient();
        final AjaxController ajaxController = client.getAjaxController();
        final HtmlPage page = (HtmlPage) ww.getEnclosedPage();
        final boolean synchron = ajaxController.processSynchron(page, webRequest_, async_);
        if (synchron) {
            doSend();
        }
        else {
            // Create and start a thread in which to execute the request.
            final Scriptable startingScope = w;
            final ContextFactory cf = ((JavaScriptEngine) client.getJavaScriptEngine()).getContextFactory();
            final ContextAction<Object> action = new ContextAction<Object>() {
                @Override
                public Object run(final Context cx) {
                    // KEY_STARTING_SCOPE maintains a stack of scopes
                    @SuppressWarnings("unchecked")
                    Deque<Scriptable> stack =
                            (Deque<Scriptable>) cx.getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
                    if (null == stack) {
                        stack = new ArrayDeque<>();
                        cx.putThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE, stack);
                    }
                    stack.push(startingScope);

                    try {
                        doSend();
                    }
                    finally {
                        stack.pop();
                    }
                    return null;
                }

                @Override
                public String toString() {
                    return "XMLHttpRequest " + webRequest_.getHttpMethod() + " '" + webRequest_.getUrl() + "'";
                }
            };
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavascriptXMLHttpRequestJob(cf, action);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Starting XMLHttpRequest thread for asynchronous request");
            }
            jobID_ = ww.getJobManager().addJob(job, page);

            if (getBrowserVersion().hasFeature(XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE)) {
                // quite strange but IE seems to fire state loading twice
                // in async mode (at least with HTML of the unit tests)
                fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
            }
            if (!getBrowserVersion().hasFeature(XHR_LOAD_START_ASYNC)) {
                fireJavascriptEvent(Event.TYPE_LOAD_START);
            }
        }
    }

    /**
     * Prepares the WebRequest that will be sent.
     * @param content the content to send
     */
    private void prepareRequestContent(final Object content) {
        if (content != null
            && (HttpMethod.POST == webRequest_.getHttpMethod()
                    || HttpMethod.PUT == webRequest_.getHttpMethod()
                    || HttpMethod.PATCH == webRequest_.getHttpMethod())
            && !Undefined.isUndefined(content)) {

            final boolean setEncodingType = webRequest_.getAdditionalHeader(HttpHeader.CONTENT_TYPE) == null;
            if (content instanceof FormData) {
                ((FormData) content).fillRequest(webRequest_);
            }
            else if (content instanceof NativeArrayBufferView) {
                final NativeArrayBufferView view = (NativeArrayBufferView) content;
                webRequest_.setRequestBody(new String(view.getBuffer().getBuffer(), UTF_8));
                if (setEncodingType) {
                    webRequest_.setEncodingType(null);
                }
            }
            else if (content instanceof URLSearchParams) {
                ((URLSearchParams) content).fillRequest(webRequest_);
                webRequest_.addHint(HttpHint.IncludeCharsetInContentTypeHeader);
            }
            else if (content instanceof Blob) {
                ((Blob) content).fillRequest(webRequest_);
            }
            else {
                final String body = Context.toString(content);
                if (!body.isEmpty()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Setting request body to: " + body);
                    }
                    webRequest_.setRequestBody(body);
                    webRequest_.setCharset(UTF_8);
                    if (setEncodingType) {
                        webRequest_.setEncodingType(FormEncodingType.TEXT_PLAIN);
                    }
                }
            }
        }
    }

    /**
     * The real send job.
     * @param context the current context
     */
    void doSend() {
        if (async_ && getBrowserVersion().hasFeature(XHR_LOAD_START_ASYNC)) {
            fireJavascriptEvent(Event.TYPE_LOAD_START);
        }

        final WebClient wc = getWindow().getWebWindow().getWebClient();
        boolean preflighted = false;
        try {
            final String originHeaderValue = webRequest_.getAdditionalHeaders().get(HttpHeader.ORIGIN);
            if (originHeaderValue != null && isPreflight()) {
                preflighted = true;
                final WebRequest preflightRequest = new WebRequest(webRequest_.getUrl(), HttpMethod.OPTIONS);

                // header origin
                preflightRequest.setAdditionalHeader(HttpHeader.ORIGIN, originHeaderValue);

                // header request-method
                preflightRequest.setAdditionalHeader(
                        HttpHeader.ACCESS_CONTROL_REQUEST_METHOD,
                        webRequest_.getHttpMethod().name());

                // header request-headers
                final StringBuilder builder = new StringBuilder();
                for (final Entry<String, String> header
                        : new TreeMap<>(webRequest_.getAdditionalHeaders()).entrySet()) {
                    final String name = header.getKey().toLowerCase(Locale.ROOT);
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

                // do the preflight request
                final WebResponse preflightResponse = wc.loadWebResponse(preflightRequest);
                final int statusCode = preflightResponse.getStatusCode();
                final boolean successful = statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES
                    || statusCode == HttpStatus.SC_USE_PROXY
                    || statusCode == HttpStatus.SC_NOT_MODIFIED;
                if (!successful || !isPreflightAuthorized(preflightResponse)) {
                    setState(DONE);
                    if (async_ || getBrowserVersion().hasFeature(XHR_HANDLE_SYNC_NETWORK_ERRORS)) {
                        fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
                        fireJavascriptEvent(Event.TYPE_ERROR);
                        fireJavascriptEvent(Event.TYPE_LOAD_END);
                    }

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("No permitted request for URL " + webRequest_.getUrl());
                    }
                    Context.throwAsScriptRuntimeEx(
                            new RuntimeException("No permitted \"Access-Control-Allow-Origin\" header."));
                    return;
                }
            }

            webResponse_ = wc.loadWebResponse(webRequest_);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Web response loaded successfully.");
            }
            // this kind of web responses using UTF-8 as default encoding
            webResponse_.defaultCharsetUtf8();

            boolean allowOriginResponse = true;
            if (originHeaderValue != null) {
                String value = webResponse_.getResponseHeaderValue(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN);
                allowOriginResponse = originHeaderValue.equals(value);
                if (isWithCredentials()) {
                    // second step: check the allow-credentials header for true
                    value = webResponse_.getResponseHeaderValue(HttpHeader.ACCESS_CONTROL_ALLOW_CREDENTIALS);
                    allowOriginResponse = allowOriginResponse && Boolean.parseBoolean(value);
                }
                else {
                    allowOriginResponse = allowOriginResponse || ALLOW_ORIGIN_ALL.equals(value);
                }
            }
            if (allowOriginResponse) {
                if (overriddenMimeType_ != null) {
                    final int index = overriddenMimeType_.toLowerCase(Locale.ROOT).indexOf("charset=");
                    String charsetName = "";
                    if (index != -1) {
                        charsetName = overriddenMimeType_.substring(index + "charset=".length());
                    }
                    final Charset charset = EncodingSniffer.toCharset(charsetName);
                    final String charsetNameFinal = charsetName;
                    final Charset charsetFinal = charset;
                    webResponse_ = new WebResponseWrapper(webResponse_) {
                        @Override
                        public String getContentType() {
                            return overriddenMimeType_;
                        }
                        @Override
                        public Charset getContentCharset() {
                            if (charsetNameFinal.isEmpty()
                                    || (charsetFinal == null && getBrowserVersion()
                                                .hasFeature(XHR_USE_CONTENT_CHARSET))) {
                                return super.getContentCharset();
                            }
                            return charsetFinal;
                        }
                    };
                }
            }
            if (!allowOriginResponse) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No permitted \"Access-Control-Allow-Origin\" header for URL " + webRequest_.getUrl());
                }
                throw new IOException("No permitted \"Access-Control-Allow-Origin\" header.");
            }

            setState(HEADERS_RECEIVED);
            if (async_) {
                fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
            }

            if (async_) {
                setState(LOADING);
                fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
                fireJavascriptEvent(Event.TYPE_PROGRESS);
            }

            setState(DONE);
            fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
            fireJavascriptEvent(Event.TYPE_LOAD);
            fireJavascriptEvent(Event.TYPE_LOAD_END);
        }
        catch (final IOException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("IOException: returning a network error response.", e);
            }

            if (async_) {
                if (e instanceof SocketTimeoutException
                        && getBrowserVersion().hasFeature(XHR_LOAD_START_ASYNC)) {
                    try {
                        webResponse_ = wc.loadWebResponse(WebRequest.newAboutBlankRequest());
                    }
                    catch (final IOException eIgnored) {
                        // ignore
                    }
                    setState(HEADERS_RECEIVED);
                    fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
                }

                if (!preflighted
                        && e instanceof NoHttpResponseException
                        && getBrowserVersion().hasFeature(XHR_PROGRESS_ON_NETWORK_ERROR_ASYNC)) {
                    fireJavascriptEvent(Event.TYPE_PROGRESS);
                }
            }

            webResponse_ = new NetworkErrorWebResponse(webRequest_, e);
            if (async_) {
                setState(DONE);
                fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
                if (e instanceof SocketTimeoutException) {
                    fireJavascriptEvent(Event.TYPE_TIMEOUT);
                }
                else {
                    fireJavascriptEvent(Event.TYPE_ERROR);
                }
                fireJavascriptEvent(Event.TYPE_LOAD_END);
            }
            else {
                setState(DONE);
                if (getBrowserVersion().hasFeature(XHR_HANDLE_SYNC_NETWORK_ERRORS)) {
                    fireJavascriptEvent(Event.TYPE_READY_STATE_CHANGE);
                    if (e instanceof SocketTimeoutException) {
                        fireJavascriptEvent(Event.TYPE_TIMEOUT);
                    }
                    else {
                        fireJavascriptEvent(Event.TYPE_ERROR);
                    }
                    fireJavascriptEvent(Event.TYPE_LOAD_END);
                }

                Context.throwAsScriptRuntimeEx(e);
            }
        }
    }

    private boolean isPreflight() {
        final HttpMethod method = webRequest_.getHttpMethod();
        if (method != HttpMethod.GET && method != HttpMethod.HEAD && method != HttpMethod.POST) {
            return true;
        }
        for (final Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
            if (isPreflightHeader(header.getKey().toLowerCase(Locale.ROOT), header.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean isPreflightAuthorized(final WebResponse preflightResponse) {
        final String originHeader = preflightResponse.getResponseHeaderValue(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN);
        if (!ALLOW_ORIGIN_ALL.equals(originHeader)
                && !webRequest_.getAdditionalHeaders().get(HttpHeader.ORIGIN).equals(originHeader)) {
            return false;
        }
        String headersHeader = preflightResponse.getResponseHeaderValue(HttpHeader.ACCESS_CONTROL_ALLOW_HEADERS);
        if (headersHeader == null) {
            headersHeader = "";
        }
        else {
            headersHeader = headersHeader.toLowerCase(Locale.ROOT);
        }
        for (final Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
            final String key = header.getKey().toLowerCase(Locale.ROOT);
            if (isPreflightHeader(key, header.getValue())
                    && !headersHeader.contains(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param name header name (MUST be lower-case for performance reasons)
     * @param value header value
     */
    private static boolean isPreflightHeader(final String name, final String value) {
        if (HttpHeader.CONTENT_TYPE_LC.equals(name)) {
            final String lcValue = value.toLowerCase(Locale.ROOT);
            if (lcValue.startsWith(FormEncodingType.URL_ENCODED.getName())
                || lcValue.startsWith(FormEncodingType.MULTIPART.getName())
                || lcValue.startsWith(FormEncodingType.TEXT_PLAIN.getName())) {
                return false;
            }
            return true;
        }
        if (HttpHeader.ACCEPT_LC.equals(name)
                || HttpHeader.ACCEPT_LANGUAGE_LC.equals(name)
                || HttpHeader.CONTENT_LANGUAGE_LC.equals(name)
                || HttpHeader.REFERER_LC.equals(name)
                || "accept-encoding".equals(name)
                || HttpHeader.ORIGIN_LC.equals(name)) {
            return false;
        }
        return true;
    }

    /**
     * Sets the specified header to the specified value. The <tt>open</tt> method must be
     * called before this method, or an error will occur.
     * @param name the name of the header being set
     * @param value the value of the header being set
     */
    @JsxFunction
    public void setRequestHeader(final String name, final String value) {
        if (!isAuthorizedHeader(name)) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Ignoring XMLHttpRequest.setRequestHeader for " + name
                    + ": it is a restricted header");
            }
            return;
        }

        if (webRequest_ != null) {
            webRequest_.setAdditionalHeader(name, value);
        }
        else {
            throw Context.reportRuntimeError("The open() method must be called before setRequestHeader().");
        }
    }

    /**
     * Not all request headers can be set from JavaScript.
     * @see <a href="http://www.w3.org/TR/XMLHttpRequest/#the-setrequestheader-method">W3C doc</a>
     * @param name the header name
     * @return {@code true} if the header can be set from JavaScript
     */
    static boolean isAuthorizedHeader(final String name) {
        final String nameLowerCase = name.toLowerCase(Locale.ROOT);
        if (PROHIBITED_HEADERS_.contains(nameLowerCase)) {
            return false;
        }
        else if (nameLowerCase.startsWith("proxy-") || nameLowerCase.startsWith("sec-")) {
            return false;
        }
        return true;
    }

    /**
     * Override the mime type returned by the server (if any). This may be used, for example, to force a stream
     * to be treated and parsed as text/xml, even if the server does not report it as such.
     * This must be done before the send method is invoked.
     * @param mimeType the type used to override that returned by the server (if any)
     * @see <a href="http://xulplanet.com/references/objref/XMLHttpRequest.html#method_overrideMimeType">XUL Planet</a>
     */
    @JsxFunction
    public void overrideMimeType(final String mimeType) {
        if (state_ != UNSENT && state_ != OPENED) {
            throw Context.reportRuntimeError("Property 'overrideMimeType' not writable after sent.");
        }
        overriddenMimeType_ = mimeType;
    }

    /**
     * Returns the {@code withCredentials} property.
     * @return the {@code withCredentials} property
     */
    @JsxGetter
    public boolean isWithCredentials() {
        return withCredentials_;
    }

    /**
     * Sets the {@code withCredentials} property.
     * @param withCredentials the {@code withCredentials} property.
     */
    @JsxSetter
    public void setWithCredentials(final boolean withCredentials) {
        withCredentials_ = withCredentials;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, final Scriptable start) {
        if (!caseSensitiveProperties_) {
            for (final String property : ALL_PROPERTIES_) {
                if (property.equalsIgnoreCase(name)) {
                    name = property;
                    break;
                }
            }
        }
        return super.get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(String name, final Scriptable start, final Object value) {
        if (!caseSensitiveProperties_) {
            for (final String property : ALL_PROPERTIES_) {
                if (property.equalsIgnoreCase(name)) {
                    name = property;
                    break;
                }
            }
        }
        super.put(name, start, value);
    }

    /**
     * Returns the {@code upload} property.
     * @return the {@code upload} property
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public XMLHttpRequestUpload getUpload() {
        final XMLHttpRequestUpload upload = new XMLHttpRequestUpload();
        upload.setParentScope(getParentScope());
        upload.setPrototype(getPrototype(upload.getClass()));
        return upload;
    }

    /**
     * Returns the {@code upload} property - IE version.
     * @return the {@code upload} property
     */
    @JsxGetter(value = IE, propertyName = "upload")
    public XMLHttpRequestEventTarget getUploadIE() {
        final XMLHttpRequestEventTarget upload = new XMLHttpRequestEventTarget();
        upload.setParentScope(getParentScope());
        upload.setPrototype(getPrototype(upload.getClass()));
        return upload;
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public Function getOnreadystatechange() {
        return super.getOnreadystatechange();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setOnreadystatechange(final Function readyStateChangeHandler) {
        super.setOnreadystatechange(readyStateChangeHandler);
    }

    @JsxGetter
    public int getTimeout() {
        return timeout_;
    }

    @JsxSetter
    public void setTimeout(final int timeout) {
        timeout_ = timeout;
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter(IE)
    @Override
    public Function getOntimeout() {
        return super.getOntimeout();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setOntimeout(final Function timeoutHandler) {
        super.setOntimeout(timeoutHandler);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter(IE)
    @Override
    public Function getOnprogress() {
        return super.getOnprogress();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setOnprogress(final Function progressHandler) {
        super.setOnprogress(progressHandler);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter(IE)
    @Override
    public Function getOnload() {
        return super.getOnload();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setOnload(final Function loadHandler) {
        super.setOnload(loadHandler);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter(IE)
    @Override
    public Function getOnloadstart() {
        return super.getOnloadstart();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setOnloadstart(final Function loadstartHandler) {
        super.setOnloadstart(loadstartHandler);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter(IE)
    @Override
    public Function getOnloadend() {
        return super.getOnloadend();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setOnloadend(final Function loadendHandler) {
        super.setOnloadend(loadendHandler);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter(IE)
    @Override
    public Function getOnabort() {
        return super.getOnabort();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setOnabort(final Function abortHandler) {
        super.setOnabort(abortHandler);
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter(IE)
    @Override
    public Function getOnerror() {
        return super.getOnerror();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setOnerror(final Function errorHandler) {
        super.setOnerror(errorHandler);
    }

    private static final class NetworkErrorWebResponse extends WebResponse {
        private final WebRequest request_;
        private final IOException error_;

        NetworkErrorWebResponse(final WebRequest webRequest, final IOException error) {
            super(null, null, 0);
            request_ = webRequest;
            error_ = error;
        }

        @Override
        public int getStatusCode() {
            return 0;
        }

        @Override
        public String getStatusMessage() {
            return "";
        }

        @Override
        public String getContentType() {
            return "";
        }

        @Override
        public String getContentAsString() {
            return "";
        }

        @Override
        public InputStream getContentAsStream() {
            return null;
        }

        @Override
        public List<NameValuePair> getResponseHeaders() {
            return Collections.emptyList();
        }

        @Override
        public String getResponseHeaderValue(final String headerName) {
            return "";
        }

        @Override
        public long getLoadTime() {
            return 0;
        }

        @Override
        public Charset getContentCharset() {
            return null;
        }

        @Override
        public Charset getContentCharsetOrNull() {
            return null;
        }

        @Override
        public WebRequest getWebRequest() {
            return request_;
        }

        /**
         * @return the error
         */
        public IOException getError() {
            return error_;
        }
    }
}
