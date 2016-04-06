/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_TYPE_XMLHTTPREQUESTPROGRESSEVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_IGNORE_PORT_FOR_SAME_ORIGIN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_NO_CROSS_ORIGIN_TO_ABOUT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_OPEN_ALLOW_EMTPY_URL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_OPEN_WITHCREDENTIALS_TRUE_IN_SYNC_EXCEPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_OVERRIDE_MIME_TYPE_BEFORE_SEND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_WITHCREDENTIALS_ALLOW_ORIGIN_ALL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_WITHCREDENTIALS_NOT_WRITEABLE_IN_SYNC_EXCEPTION;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.auth.UsernamePasswordCredentials;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
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
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.ProgressEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.XMLHttpRequestProgressEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.WebResponseWrapper;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

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
 *
 * @see <a href="http://www.w3.org/TR/XMLHttpRequest/">W3C XMLHttpRequest</a>
 * @see <a href="http://developer.apple.com/internet/webcontent/xmlhttpreq.html">Safari documentation</a>
 */
@JsxClass
public class XMLHttpRequest extends EventTarget {

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

    private static final String HEADER_ORIGIN = "Origin";

    private static final String HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String HEADER_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    private static final String ALLOW_ORIGIN_ALL = "*";

    private static final String[] ALL_PROPERTIES_ = {"onreadystatechange", "readyState", "responseText", "responseXML",
        "status", "statusText", "abort", "getAllResponseHeaders", "getResponseHeader", "open", "send",
        "setRequestHeader"};

    private static Collection<String> PROHIBITED_HEADERS_ = Arrays.asList("accept-charset", "accept-encoding",
        "connection", "content-length", "cookie", "cookie2", "content-transfer-encoding", "date", "expect",
        "host", "keep-alive", "referer", "te", "trailer", "transfer-encoding", "upgrade", "user-agent", "via");

    private int state_;
    private Function stateChangeHandler_;
    private Function loadHandler_;
    private Function errorHandler_;
    private WebRequest webRequest_;
    private boolean async_;
    private int jobID_;
    private WebResponse webResponse_;
    private String overriddenMimeType_;
    private HtmlPage containingPage_;
    private final boolean caseSensitiveProperties_;
    private boolean withCredentials_;

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
     * Returns the event handler that fires on every state change.
     * @return the event handler that fires on every state change
     */
    @JsxGetter
    public Function getOnreadystatechange() {
        return stateChangeHandler_;
    }

    /**
     * Sets the event handler that fires on every state change.
     * @param stateChangeHandler the event handler that fires on every state change
     */
    @JsxSetter
    public void setOnreadystatechange(final Function stateChangeHandler) {
        stateChangeHandler_ = stateChangeHandler;
        if (state_ == OPENED) {
            setState(state_, null);
        }
    }

    /**
     * Sets the state as specified and invokes the state change handler if one has been set.
     * @param state the new state
     * @param context the context within which the state change handler is to be invoked;
     *                if {@code null}, the current thread's context is used.
     */
    private void setState(final int state, Context context) {
        state_ = state;

        final BrowserVersion browser = getBrowserVersion();
        if (stateChangeHandler_ != null && (async_ || state == DONE)) {
            final Scriptable scope = stateChangeHandler_.getParentScope();
            final JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Calling onreadystatechange handler for state " + state);
            }
            final Object[] params = new Event[] {new Event(this, Event.TYPE_READY_STATE_CHANGE)};
            jsEngine.callFunction(containingPage_, stateChangeHandler_, scope, this, params);
            if (LOG.isDebugEnabled()) {
                if (context == null) {
                    context = Context.getCurrentContext();
                }
                LOG.debug("onreadystatechange handler: " + context.decompileFunction(stateChangeHandler_, 4));
                LOG.debug("Calling onreadystatechange handler for state " + state + ". Done.");
            }
        }

        if (state == DONE) {
            final JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();

            final XMLHttpRequestProgressEvent event = new XMLHttpRequestProgressEvent(this, Event.TYPE_LOAD);
            final Object[] params = new Event[] {event};
            if (!browser.hasFeature(EVENT_TYPE_XMLHTTPREQUESTPROGRESSEVENT)) {
                event.setLengthComputable(true);
            }

            if (webResponse_ != null) {
                final long contentLength = webResponse_.getContentLength();
                event.setLoaded(contentLength);
                if (!browser.hasFeature(EVENT_TYPE_XMLHTTPREQUESTPROGRESSEVENT)) {
                    event.setTotal(contentLength);
                }
            }

            if (loadHandler_ != null) {
                jsEngine.callFunction(containingPage_, loadHandler_, loadHandler_.getParentScope(), this, params);
            }

            List<Scriptable> handlers = getEventListenersContainer().getHandlers(Event.TYPE_LOAD, false);
            if (handlers != null) {
                for (final Scriptable scriptable : handlers) {
                    if (scriptable instanceof Function) {
                        final Function function = (Function) scriptable;
                        jsEngine.callFunction(containingPage_, function, function.getParentScope(), this, params);
                    }
                }
            }

            handlers = getEventListenersContainer().getHandlers(Event.TYPE_LOAD, true);
            if (handlers != null) {
                for (final Scriptable scriptable : handlers) {
                    if (scriptable instanceof Function) {
                        final Function function = (Function) scriptable;
                        jsEngine.callFunction(containingPage_, function, function.getParentScope(), this, params);
                    }
                }
            }
        }
    }

    /**
     * Returns the event handler that fires on load.
     * @return the event handler that fires on load
     */
    @JsxGetter
    public Function getOnload() {
        return loadHandler_;
    }

    /**
     * Sets the event handler that fires on load.
     * @param loadHandler the event handler that fires on load
     */
    @JsxSetter
    public void setOnload(final Function loadHandler) {
        loadHandler_ = loadHandler;
    }

    /**
     * Returns the event handler that fires on error.
     * @return the event handler that fires on error
     */
    @JsxGetter
    public Function getOnerror() {
        return errorHandler_;
    }

    /**
     * Sets the event handler that fires on error.
     * @param errorHandler the event handler that fires on error
     */
    @JsxSetter
    public void setOnerror(final Function errorHandler) {
        errorHandler_ = errorHandler;
    }

    /**
     * Invokes the onerror handler if one has been set.
     * @param context the context within which the onerror handler is to be invoked;
     *                if {@code null}, the current thread's context is used.
     */
    private void processError(Context context) {
        final BrowserVersion browser = getBrowserVersion();
        if (errorHandler_ != null) {
            final Scriptable scope = errorHandler_.getParentScope();
            final JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();

            final Object[] params;
            if (browser.hasFeature(EVENT_TYPE_XMLHTTPREQUESTPROGRESSEVENT)) {
                params = new Event[] {new XMLHttpRequestProgressEvent(this, Event.TYPE_ERROR)};
            }
            else {
                params = new Event[] {new ProgressEvent(this, Event.TYPE_ERROR)};
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Calling onerror handler");
            }
            jsEngine.callFunction(containingPage_, errorHandler_, this, scope, params);
            if (LOG.isDebugEnabled()) {
                if (context == null) {
                    context = Context.getCurrentContext();
                }
                LOG.debug("onerror handler: " + context.decompileFunction(errorHandler_, 4));
                LOG.debug("Calling onerror handler done.");
            }
        }
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
            final String encoding = webResponse_.getContentCharset();
            String defaultEncoding = null;
            if (getBrowserVersion().hasFeature(EVENT_TYPE_XMLHTTPREQUESTPROGRESSEVENT)) {
                defaultEncoding = ((HTMLDocument) containingPage_.getScriptableObject()).getDefaultCharset();
            }
            if (getBrowserVersion().hasFeature(XHR_NO_CROSS_ORIGIN_TO_ABOUT)) {
                defaultEncoding = encoding;
            }
            return webResponse_.getContentAsString(encoding, defaultEncoding);
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
            return null; // send() has not been called
        }
        if (webResponse_ instanceof NetworkErrorWebResponse) {
            return null;
        }
        final String contentType = webResponse_.getContentType();
        if (contentType.isEmpty() || contentType.contains("xml")) {
            final WebWindow webWindow = getWindow().getWebWindow();
            try {
                final XmlPage page = new XmlPage(webResponse_, webWindow);
                final XMLDocument document = new XMLDocument();
                document.setPrototype(getPrototype(document.getClass()));
                document.setParentScope(getWindow());
                document.setDomNode(page);
                return document;
            }
            catch (final IOException e) {
                LOG.warn("Failed parsing XML document " + webResponse_.getWebRequest().getUrl() + ": "
                        + e.getMessage());
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

        LOG.error("XMLHttpRequest.status was retrieved without a response available (readyState: "
            + state_ + ").");
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

        LOG.error("XMLHttpRequest.statusText was retrieved without a response available (readyState: "
            + state_ + ").");
        return null;
    }

    /**
     * Cancels the current HTTP request.
     */
    @JsxFunction
    public void abort() {
        getWindow().getWebWindow().getJobManager().stopJob(jobID_);
    }

    /**
     * Returns the labels and values of all the HTTP headers.
     * @return the labels and values of all the HTTP headers
     */
    @JsxFunction
    public String getAllResponseHeaders() {
        if (state_ == UNSENT || state_ == OPENED) {
            return null;
        }
        if (webResponse_ != null) {
            final StringBuilder buffer = new StringBuilder();
            for (final NameValuePair header : webResponse_.getResponseHeaders()) {
                buffer.append(header.getName()).append(": ").append(header.getValue()).append("\n");
            }
            return buffer.toString();
        }

        LOG.error("XMLHttpRequest.getAllResponseHeaders() was called without a response available (readyState: "
            + state_ + ").");
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

        LOG.error("XMLHttpRequest.getAllResponseHeaders(..) was called without a response available (readyState: "
            + state_ + ").");
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
        if (asyncParam != Undefined.instance) {
            async = ScriptRuntime.toBoolean(asyncParam);
        }

        if (!async
                && getWithCredentials()
                && getBrowserVersion().hasFeature(XHR_OPEN_WITHCREDENTIALS_TRUE_IN_SYNC_EXCEPTION)) {
            throw Context.reportRuntimeError(
                            "open() in sync mode is not possible because 'withCredentials' is set to true");
        }

        final String url = Context.toString(urlParam);

        // (URL + Method + User + Password) become a WebRequest instance.
        containingPage_ = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();

        try {
            final URL fullUrl = containingPage_.getFullyQualifiedUrl(url);
            final URL originUrl = containingPage_.getFullyQualifiedUrl("");
            if (!isAllowCrossDomainsFor(originUrl, fullUrl)) {
                throw Context.reportRuntimeError("Access to restricted URI denied");
            }

            final WebRequest request = new WebRequest(fullUrl, getBrowserVersion().getXmlHttpRequestAcceptHeader());
            request.setCharset("UTF-8");
            request.setAdditionalHeader("Referer", containingPage_.getUrl().toExternalForm());

            if (!isSameOrigin(originUrl, fullUrl)) {
                final StringBuilder origin = new StringBuilder().append(originUrl.getProtocol()).append("://")
                        .append(originUrl.getHost());
                if (originUrl.getPort() != -1) {
                    origin.append(':').append(originUrl.getPort());
                }
                request.setAdditionalHeader(HEADER_ORIGIN, origin.toString());
            }

            try {
                request.setHttpMethod(HttpMethod.valueOf(method.toUpperCase(Locale.ROOT)));
            }
            catch (final IllegalArgumentException e) {
                LOG.info("Incorrect HTTP Method '" + method + "'");
                return;
            }

            // password is ignored if no user defined
            final boolean userIsNull = null == user || Undefined.instance == user;
            if (!userIsNull) {
                final String userCred = user.toString();

                final boolean passwordIsNull = null == password || Undefined.instance == password;
                String passwordCred = "";
                if (!passwordIsNull) {
                    passwordCred = password.toString();
                }

                request.setCredentials(new UsernamePasswordCredentials(userCred, passwordCred));
            }
            webRequest_ = request;
        }
        catch (final MalformedURLException e) {
            LOG.error("Unable to initialize XMLHttpRequest using malformed URL '" + url + "'.");
            return;
        }
        // Async stays a boolean.
        async_ = async;
        // Change the state!
        setState(OPENED, null);
    }

    private boolean isAllowCrossDomainsFor(final URL originUrl, final URL newUrl) {
        final BrowserVersion browser = getBrowserVersion();
        if (browser.hasFeature(XHR_NO_CROSS_ORIGIN_TO_ABOUT)
                && "about".equals(newUrl.getProtocol())) {
            return false;
        }

        return true;
    }

    private boolean isSameOrigin(final URL originUrl, final URL newUrl) {
        if (!originUrl.getHost().equals(newUrl.getHost())) {
            return false;
        }

        if (getBrowserVersion().hasFeature(XHR_IGNORE_PORT_FOR_SAME_ORIGIN)) {
            return true;
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
        prepareRequest(content);

        final WebClient client = getWindow().getWebWindow().getWebClient();
        final AjaxController ajaxController = client.getAjaxController();
        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        final boolean synchron = ajaxController.processSynchron(page, webRequest_, async_);
        if (synchron) {
            doSend(Context.getCurrentContext());
        }
        else {
            if (getBrowserVersion().hasFeature(XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE)) {
                // quite strange but IE seems to fire state loading twice
                // in async mode (at least with HTML of the unit tests)
                setState(OPENED, Context.getCurrentContext());
            }

            // Create and start a thread in which to execute the request.
            final Scriptable startingScope = getWindow();
            final ContextFactory cf = client.getJavaScriptEngine().getContextFactory();
            final ContextAction action = new ContextAction() {
                @Override
                public Object run(final Context cx) {
                    // KEY_STARTING_SCOPE maintains a stack of scopes
                    @SuppressWarnings("unchecked")
                    Stack<Scriptable> stack =
                            (Stack<Scriptable>) cx.getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
                    if (null == stack) {
                        stack = new Stack<>();
                        cx.putThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE, stack);
                    }
                    stack.push(startingScope);

                    try {
                        doSend(cx);
                    }
                    finally {
                        stack.pop();
                    }
                    return null;
                }
            };
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavascriptXMLHttpRequestJob(cf, action);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Starting XMLHttpRequest thread for asynchronous request");
            }
            jobID_ = getWindow().getWebWindow().getJobManager().addJob(job, page);
        }
    }

    /**
     * Prepares the WebRequest that will be sent.
     * @param content the content to send
     */
    private void prepareRequest(final Object content) {
        if (content != null
            && (HttpMethod.POST == webRequest_.getHttpMethod()
                    || HttpMethod.PUT == webRequest_.getHttpMethod()
                    || HttpMethod.PATCH == webRequest_.getHttpMethod())
            && !Context.getUndefinedValue().equals(content)) {
            if (content instanceof FormData) {
                ((FormData) content).fillRequest(webRequest_);
            }
            else {
                final String body = Context.toString(content);
                if (!body.isEmpty()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Setting request body to: " + body);
                    }
                    webRequest_.setRequestBody(body);
                }
            }
        }
    }

    /**
     * The real send job.
     * @param context the current context
     */
    private void doSend(final Context context) {
        final WebClient wc = getWindow().getWebWindow().getWebClient();
        try {
            final String originHeaderValue = webRequest_.getAdditionalHeaders().get(HEADER_ORIGIN);
            final boolean crossOriginResourceSharing = originHeaderValue != null;
            if (crossOriginResourceSharing && isPreflight()) {
                final WebRequest preflightRequest = new WebRequest(webRequest_.getUrl(), HttpMethod.OPTIONS);

                // header origin
                preflightRequest.setAdditionalHeader(HEADER_ORIGIN, originHeaderValue);

                // header request-method
                preflightRequest.setAdditionalHeader(
                        HEADER_ACCESS_CONTROL_REQUEST_METHOD,
                        webRequest_.getHttpMethod().name());

                // header request-headers
                final StringBuilder builder = new StringBuilder();
                final boolean acceptContentType
                    = getBrowserVersion().hasFeature(EVENT_TYPE_XMLHTTPREQUESTPROGRESSEVENT);
                for (final Entry<String, String> header
                        : new TreeMap<>(webRequest_.getAdditionalHeaders()).entrySet()) {
                    final String name = header.getKey().toLowerCase(Locale.ROOT);
                    if (isPreflightHeader(name, header.getValue(), acceptContentType)) {
                        if (builder.length() != 0) {
                            builder.append(acceptContentType ? ", " : ",");
                        }
                        builder.append(name);
                    }
                }
                preflightRequest.setAdditionalHeader(HEADER_ACCESS_CONTROL_REQUEST_HEADERS, builder.toString());

                // do the preflight request
                final WebResponse preflightResponse = wc.loadWebResponse(preflightRequest);
                if (!isPreflightAuthorized(preflightResponse)) {
                    setState(HEADERS_RECEIVED, context);
                    setState(LOADING, context);
                    setState(DONE, context);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("No permitted request for URL " + webRequest_.getUrl());
                    }
                    Context.throwAsScriptRuntimeEx(
                            new RuntimeException("No permitted \"Access-Control-Allow-Origin\" header."));
                    return;
                }
            }
            final WebResponse webResponse = wc.loadWebResponse(webRequest_);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Web response loaded successfully.");
            }
            boolean allowOriginResponse = true;
            if (crossOriginResourceSharing) {
                String value = webResponse.getResponseHeaderValue(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
                allowOriginResponse = originHeaderValue.equals(value);
                if (getWithCredentials()) {
                    allowOriginResponse = allowOriginResponse
                            || (getBrowserVersion().hasFeature(XHR_WITHCREDENTIALS_ALLOW_ORIGIN_ALL)
                            && ALLOW_ORIGIN_ALL.equals(value));

                    // second step: check the allow-credentials header for true
                    value = webResponse.getResponseHeaderValue(HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS);
                    allowOriginResponse = allowOriginResponse && Boolean.parseBoolean(value);
                }
                else {
                    allowOriginResponse = allowOriginResponse || ALLOW_ORIGIN_ALL.equals(value);
                }
            }
            if (allowOriginResponse) {
                if (overriddenMimeType_ == null) {
                    webResponse_ = webResponse;
                }
                else {
                    final int index = overriddenMimeType_.toLowerCase(Locale.ROOT).indexOf("charset=");
                    String charsetString = "";
                    if (index != -1) {
                        charsetString = overriddenMimeType_.substring(index + "charset=".length());
                    }
                    final String charset;
                    if (!charsetString.isEmpty()) {
                        charset = charsetString;
                    }
                    else {
                        charset = null;
                    }
                    webResponse_ = new WebResponseWrapper(webResponse) {
                        @Override
                        public String getContentType() {
                            return overriddenMimeType_;
                        }
                        @Override
                        public String getContentCharset() {
                            if (charset != null) {
                                return charset;
                            }
                            return super.getContentCharset();
                        }
                    };
                }
            }
            if (allowOriginResponse) {
                setState(HEADERS_RECEIVED, context);
                setState(LOADING, context);
                setState(DONE, context);
            }
            else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No permitted \"Access-Control-Allow-Origin\" header for URL " + webRequest_.getUrl());
                }
                throw new IOException("No permitted \"Access-Control-Allow-Origin\" header.");
            }
        }
        catch (final IOException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("IOException: returning a network error response.", e);
            }
            webResponse_ = new NetworkErrorWebResponse(webRequest_);
            setState(HEADERS_RECEIVED, context);
            setState(DONE, context);
            if (async_) {
                processError(context);
            }
            else {
                Context.throwAsScriptRuntimeEx(e);
            }
        }
    }

    private boolean isPreflight() {
        final HttpMethod method = webRequest_.getHttpMethod();
        if (method != HttpMethod.GET && method != HttpMethod.HEAD && method != HttpMethod.POST) {
            return true;
        }
        final boolean acceptContentType = getBrowserVersion().hasFeature(EVENT_TYPE_XMLHTTPREQUESTPROGRESSEVENT);
        for (final Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
            if (isPreflightHeader(header.getKey().toLowerCase(Locale.ROOT),
                    header.getValue(), acceptContentType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPreflightAuthorized(final WebResponse preflightResponse) {
        final String originHeader = preflightResponse.getResponseHeaderValue(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
        if (!ALLOW_ORIGIN_ALL.equals(originHeader)
                && !webRequest_.getAdditionalHeaders().get(HEADER_ORIGIN).equals(originHeader)) {
            return false;
        }
        String headersHeader = preflightResponse.getResponseHeaderValue(HEADER_ACCESS_CONTROL_ALLOW_HEADERS);
        if (headersHeader == null) {
            headersHeader = "";
        }
        else {
            headersHeader = headersHeader.toLowerCase(Locale.ROOT);
        }
        for (final Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
            final String key = header.getKey().toLowerCase(Locale.ROOT);
            if (isPreflightHeader(key, header.getValue(), false)
                    && !headersHeader.contains(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param name header name (MUST be lower-case for performance reasons)
     * @param value header value
     * @param acceptContentType whether to consider {@code content-type} header as preflight or not
     */
    private static boolean isPreflightHeader(final String name, final String value, final boolean acceptContentType) {
        if ("content-type".equals(name)) {
            final String lcValue = value.toLowerCase(Locale.ROOT);
            if (FormEncodingType.URL_ENCODED.getName().equals(lcValue)
                || FormEncodingType.MULTIPART.getName().equals(lcValue)
                || (!acceptContentType
                        && ("text/plain".equals(lcValue) || lcValue.startsWith("text/plain;charset=")))) {
                return false;
            }
            return true;
        }
        if ("accept".equals(name) || "accept-language".equals(name) || "content-language".equals(name)
                || "referer".equals(name) || "accept-encoding".equals(name) || "origin".equals(name)) {
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
            LOG.warn("Ignoring XMLHttpRequest.setRequestHeader for " + name
                + ": it is a restricted header");
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
        if (getBrowserVersion().hasFeature(XHR_OVERRIDE_MIME_TYPE_BEFORE_SEND)
                && state_ != UNSENT && state_ != OPENED) {
            throw Context.reportRuntimeError("Property 'overrideMimeType' not writable after sent.");
        }
        overriddenMimeType_ = mimeType;
    }

    /**
     * Returns the {@code withCredentials} property.
     * @return the {@code withCredentials} property
     */
    @JsxGetter
    public boolean getWithCredentials() {
        return withCredentials_;
    }

    /**
     * Sets the {@code withCredentials} property.
     * @param withCredentials the {@code withCredentials} property.
     */
    @JsxSetter
    public void setWithCredentials(final boolean withCredentials) {
        if (!async_ && state_ != UNSENT) {
            if (getBrowserVersion().hasFeature(XHR_WITHCREDENTIALS_NOT_WRITEABLE_IN_SYNC_EXCEPTION)) {
                throw Context.reportRuntimeError("Property 'withCredentials' not writable in sync mode.");
            }
        }
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
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF) })
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
    @JsxGetter(value = @WebBrowser(IE), propertyName = "upload")
    public XMLHttpRequestEventTarget getUploadIE() {
        final XMLHttpRequestEventTarget upload = new XMLHttpRequestEventTarget();
        upload.setParentScope(getParentScope());
        upload.setPrototype(getPrototype(upload.getClass()));
        return upload;
    }

    private static final class NetworkErrorWebResponse extends WebResponse {
        private final WebRequest request_;

        private NetworkErrorWebResponse(final WebRequest webRequest) {
            super(null, null, 0);
            request_ = webRequest;
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
        public String getContentAsString(final String encoding) {
            return "";
        }

        @Override
        public String getContentAsString(final String encoding, final String defaultEncoding) {
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
        public String getContentCharset() {
            return "";
        }

        @Override
        public String getContentCharsetOrNull() {
            return "";
        }

        @Override
        public WebRequest getWebRequest() {
            return request_;
        }
    }
}
