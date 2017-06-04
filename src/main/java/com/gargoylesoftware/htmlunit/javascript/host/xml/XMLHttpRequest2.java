/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_ALL_RESPONSE_HEADERS_APPEND_CRLF;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_IGNORE_PORT_FOR_SAME_ORIGIN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_LENGTH_COMPUTABLE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_NO_CROSS_ORIGIN_TO_ABOUT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_OPEN_ALLOW_EMTPY_URL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_OPEN_WITHCREDENTIALS_TRUE_IN_SYNC_EXCEPTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_OVERRIDE_MIME_TYPE_BEFORE_SEND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_USE_DEFAULT_CHARSET_FROM_PAGE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_WITHCREDENTIALS_ALLOW_ORIGIN_ALL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_WITHCREDENTIALS_NOT_WRITEABLE_IN_SYNC_EXCEPTION;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
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
import com.gargoylesoftware.htmlunit.javascript.AbstractJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.GlobalAction;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.event.ProgressEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.WebResponseWrapper;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Attribute;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;

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
@ScriptClass
public class XMLHttpRequest2 extends EventTarget2 {

    private static final Log LOG = LogFactory.getLog(XMLHttpRequest.class);

    /** The object has been created, but not initialized (the open() method has not been called). */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int UNSENT = 0;
    /** The object has been created, but the send() method has not been called. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int OPENED = 1;
    /** The send() method has been called, but the status and headers are not yet available. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int HEADERS_RECEIVED = 2;
    /** Some data has been received. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int LOADING = 3;
    /** All the data has been received; the complete data is available in responseBody and responseText. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
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
    private ScriptFunction stateChangeHandler_;
    private ScriptFunction loadHandler_;
    private ScriptFunction errorHandler_;
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
    public XMLHttpRequest2() {
        this(true);
    }

    /**
     * Creates a new instance.
     * @param caseSensitiveProperties if properties and methods are case sensitive
     */
    public XMLHttpRequest2(final boolean caseSensitiveProperties) {
        caseSensitiveProperties_ = caseSensitiveProperties;
        state_ = UNSENT;
    }

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static XMLHttpRequest2 constructor(final boolean newObj, final Object self) {
        final XMLHttpRequest2 host = new XMLHttpRequest2();
        host.setProto(Global.instance().getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Returns a string version of the data retrieved from the server.
     * @return a string version of the data retrieved from the server
     */
    @Getter
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
    @Getter
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
                LOG.debug("XMLHttpRequest.responseXML returns of an network error ("
                        + ((NetworkErrorWebResponse) webResponse_).getError() + ")");
            }
            return null;
        }
        final String contentType = webResponse_.getContentType();
        if (contentType.isEmpty() || contentType.contains("xml")) {
            final WebWindow webWindow = containingPage_.getEnclosingWindow();
            try {
                final XmlPage page = new XmlPage(webResponse_, webWindow);
                final XMLDocument2 document = XMLDocument2.constructor(true, webWindow.getScriptableObject());
//                document.setPrototype(getPrototype(document.getClass()));
//                document.setParentScope(getWindow());
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
    @Getter
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
    @Getter
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
    @Function
    public void abort() {
        getWindow().getWebWindow().getJobManager().stopJob(jobID_);
    }

    /**
     * Returns the labels and values of all the HTTP headers.
     * @return the labels and values of all the HTTP headers
     */
    @Function
    public String getAllResponseHeaders() {
        if (state_ == UNSENT || state_ == OPENED) {
            return "";
        }
        if (webResponse_ != null) {
            final StringBuilder builder = new StringBuilder();
            for (final NameValuePair header : webResponse_.getResponseHeaders()) {
                builder.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
            }
            if (getBrowserVersion().hasFeature(XHR_ALL_RESPONSE_HEADERS_APPEND_CRLF)) {
                builder.append("\r\n");
            }
            return builder.toString();
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
    @Function
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
    @Function
    public void open(final String method, final Object urlParam, final Object asyncParam,
        final Object user, final Object password) {
        if ((urlParam == null || "".equals(urlParam)) && !getBrowserVersion().hasFeature(XHR_OPEN_ALLOW_EMTPY_URL)) {
            throw new RuntimeException("URL for XHR.open can't be empty!");
        }

        // async defaults to true if not specified
        boolean async = true;
        if (asyncParam != ScriptRuntime.UNDEFINED) {
            async = Boolean.parseBoolean(asyncParam.toString());
        }

        if (!async
                && isWithCredentials()
                && getBrowserVersion().hasFeature(XHR_OPEN_WITHCREDENTIALS_TRUE_IN_SYNC_EXCEPTION)) {
            throw new RuntimeException(
                            "open() in sync mode is not possible because 'withCredentials' is set to true");
        }

        final String url = ScriptRuntime.safeToString(urlParam);

        // (URL + Method + User + Password) become a WebRequest instance.
        containingPage_ = (HtmlPage) Global.instance().<Window2>getWindow().getWebWindow().getEnclosedPage();

        try {
            final URL fullUrl = containingPage_.getFullyQualifiedUrl(url);
            final URL originUrl = containingPage_.getFullyQualifiedUrl("");
            if (!isAllowCrossDomainsFor(originUrl, fullUrl)) {
                throw new RuntimeException("Access to restricted URI denied");
            }

            final WebRequest request = new WebRequest(fullUrl, getBrowserVersion().getXmlHttpRequestAcceptHeader());
            request.setCharset(UTF_8);
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
            if (user != null && user != ScriptRuntime.UNDEFINED) {
                final String userCred = user.toString();

                String passwordCred = "";
                if (password != null && password != ScriptRuntime.UNDEFINED) {
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

    /**
     * Override the mime type returned by the server (if any). This may be used, for example, to force a stream
     * to be treated and parsed as text/xml, even if the server does not report it as such.
     * This must be done before the send method is invoked.
     * @param mimeType the type used to override that returned by the server (if any)
     * @see <a href="http://xulplanet.com/references/objref/XMLHttpRequest.html#method_overrideMimeType">XUL Planet</a>
     */
    @Function
    public void overrideMimeType(final String mimeType) {
        if (getBrowserVersion().hasFeature(XHR_OVERRIDE_MIME_TYPE_BEFORE_SEND)
                && state_ != UNSENT && state_ != OPENED) {
            throw new RuntimeException("Property 'overrideMimeType' not writable after sent.");
        }
        overriddenMimeType_ = mimeType;
    }

    /**
     * Returns the {@code withCredentials} property.
     * @return the {@code withCredentials} property
     */
    @Getter
    public Boolean isWithCredentials() {
        return withCredentials_;
    }

    /**
     * Sets the {@code withCredentials} property.
     * @param withCredentials the {@code withCredentials} property.
     */
    @Setter
    public void setWithCredentials(final boolean withCredentials) {
        if (!async_ && state_ != UNSENT) {
            if (getBrowserVersion().hasFeature(XHR_WITHCREDENTIALS_NOT_WRITEABLE_IN_SYNC_EXCEPTION)) {
                throw new RuntimeException("Property 'withCredentials' not writable in sync mode.");
            }
        }
        withCredentials_ = withCredentials;
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

    private boolean isAllowCrossDomainsFor(final URL originUrl, final URL newUrl) {
        final BrowserVersion browser = getBrowserVersion();
        if (browser.hasFeature(XHR_NO_CROSS_ORIGIN_TO_ABOUT)
                && "about".equals(newUrl.getProtocol())) {
            return false;
        }

        return true;
    }

    @Override
    public BrowserVersion getBrowserVersion() {
        return containingPage_.getWebClient().getBrowserVersion();
    }

    /**
     * Sets the state as specified and invokes the state change handler if one has been set.
     * @param state the new state
     * @param context the context within which the state change handler is to be invoked;
     *                if {@code null}, the current thread's context is used.
     */
    private void setState(final int state, Global context) {
        state_ = state;

        final BrowserVersion browser = getBrowserVersion();
        if (stateChangeHandler_ != null && (async_ || state == DONE)) {
//            final Scriptable scope = stateChangeHandler_.getParentScope();
            final AbstractJavaScriptEngine<?> jsEngine = containingPage_.getWebClient().getJavaScriptEngine();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Calling onreadystatechange handler for state " + state);
            }
            final Object[] params = new Event2[] {new Event2(this, Event2.TYPE_READY_STATE_CHANGE)};
            ScriptRuntime.apply(stateChangeHandler_, context, params);

            if (LOG.isDebugEnabled()) {
                if (context == null) {
                    context = Global.instance();
                }
                LOG.debug("Calling onreadystatechange handler for state " + state + ". Done.");
            }
        }

        if (state == DONE) {
            final AbstractJavaScriptEngine<?> jsEngine = containingPage_.getWebClient().getJavaScriptEngine();

            final ProgressEvent2 event = new ProgressEvent2(this, Event.TYPE_LOAD);
            final Object[] params = new Event2[] {event};
            final boolean lengthComputable = browser.hasFeature(XHR_LENGTH_COMPUTABLE);
            if (lengthComputable) {
                event.setLengthComputable(true);
            }

            if (webResponse_ != null) {
                final long contentLength = webResponse_.getContentLength();
                event.setLoaded(contentLength);
                if (lengthComputable) {
                    event.setTotal(contentLength);
                }
            }

            if (loadHandler_ != null) {
                ScriptRuntime.apply(loadHandler_, context, params);
//                jsEngine.callFunction(containingPage_, loadHandler_, loadHandler_.getParentScope(), this, params);
            }

            List<ScriptObject> handlers = getEventListenersContainer().getListeners(Event.TYPE_LOAD, false);
            if (handlers != null) {
                for (final ScriptObject scriptable : handlers) {
                    if (scriptable instanceof ScriptFunction) {
                        final ScriptFunction function = (ScriptFunction) scriptable;
                        ScriptRuntime.apply(function, context, params);
//                        jsEngine.callFunction(containingPage_, function, function.getParentScope(), this, params);
                    }
                }
            }

            handlers = getEventListenersContainer().getListeners(Event.TYPE_LOAD, true);
            if (handlers != null) {
                for (final ScriptObject scriptable : handlers) {
                    if (scriptable instanceof ScriptFunction) {
                        final ScriptFunction function = (ScriptFunction) scriptable;
                        ScriptRuntime.apply(function, context, params);
//                        jsEngine.callFunction(containingPage_, function, function.getParentScope(), this, params);
                    }
                }
            }
        }
    }

    /**
     * Returns the event handler that fires on load.
     * @return the event handler that fires on load
     */
    @Getter
    public ScriptFunction getOnload() {
        return loadHandler_;
    }

    /**
     * Sets the event handler that fires on load.
     * @param loadHandler the event handler that fires on load
     */
    @Setter
    public void setOnload(final ScriptFunction loadHandler) {
        loadHandler_ = loadHandler;
    }

    /**
     * Sends the specified content to the server in an HTTP request and receives the response.
     * @param content the body of the message being sent with the request
     */
    @Function
    public void send(final Object content) {
        if (webRequest_ == null) {
            return;
        }
        prepareRequest(content);

        final WebClient client = containingPage_.getWebClient();
        final AjaxController ajaxController = client.getAjaxController();
        final HtmlPage page = containingPage_;
        final boolean synchron = ajaxController.processSynchron(page, webRequest_, async_);
        final Global global = Global.instance();
        if (synchron) {
            doSend(global);
        }
        else {
            if (getBrowserVersion().hasFeature(XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE)) {
                // quite strange but IE seems to fire state loading twice
                // in async mode (at least with HTML of the unit tests)
                setState(OPENED, Global.instance());
            }

            // Create and start a thread in which to execute the request.
//            final Window2 startingScope = getWindow();
            final GlobalAction action = new GlobalAction() {
                @Override
                public Object run(final Global global) {
                    // KEY_STARTING_SCOPE maintains a stack of scopes
//                    @SuppressWarnings("unchecked")
//                    Stack<Scriptable> stack =
//                            (Stack<Scriptable>) cx.getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
//                    if (null == stack) {
//                        stack = new Stack<>();
//                        cx.putThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE, stack);
//                    }
//                    stack.push(startingScope);

                    try {
                        doSend(global);
                    }
                    finally {
//                        stack.pop();
                    }
                    return null;
                }
            };
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavascriptXMLHttpRequestJob(global, action);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Starting XMLHttpRequest thread for asynchronous request");
            }
            jobID_ = containingPage_.getEnclosingWindow().getJobManager().addJob(job, page);
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
            && ScriptRuntime.UNDEFINED != content) {
            if (content instanceof FormData) {
                ((FormData) content).fillRequest(webRequest_);
            }
            else {
                final String body = content.toString();
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
    private void doSend(final Global context) {
        final WebClient wc = containingPage_.getWebClient();
        try {
            final String originHeaderValue = webRequest_.getAdditionalHeaders().get(HEADER_ORIGIN);
            if (originHeaderValue != null && isPreflight()) {
                final WebRequest preflightRequest = new WebRequest(webRequest_.getUrl(), HttpMethod.OPTIONS);

                // header origin
                preflightRequest.setAdditionalHeader(HEADER_ORIGIN, originHeaderValue);

                // header request-method
                preflightRequest.setAdditionalHeader(
                        HEADER_ACCESS_CONTROL_REQUEST_METHOD,
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
                    throw new RuntimeException("No permitted \"Access-Control-Allow-Origin\" header.");
                }
            }
            final WebResponse webResponse = wc.loadWebResponse(webRequest_);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Web response loaded successfully.");
            }
            boolean allowOriginResponse = true;
            if (originHeaderValue != null) {
                String value = webResponse.getResponseHeaderValue(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
                allowOriginResponse = originHeaderValue.equals(value);
                if (isWithCredentials()) {
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
                    String charsetName = "";
                    if (index != -1) {
                        charsetName = overriddenMimeType_.substring(index + "charset=".length());
                    }
                    Charset charset = EncodingSniffer.toCharset(charsetName);
                    if (charset == null
                            && getBrowserVersion().hasFeature(XHR_USE_DEFAULT_CHARSET_FROM_PAGE)) {
                        final HTMLDocument2 doc = (HTMLDocument2) containingPage_.getScriptObject2();
                        charset = Charset.forName(doc.getDefaultCharset());
                    }
                    final String charsetNameFinal = charsetName;
                    final Charset charsetFinal = charset;
                    webResponse_ = new WebResponseWrapper(webResponse) {
                        @Override
                        public String getContentType() {
                            return overriddenMimeType_;
                        }
                        @Override
                        public Charset getContentCharset() {
                            if (charsetNameFinal.isEmpty()
                                    || (charsetFinal == null && getBrowserVersion()
                                                .hasFeature(XHR_OPEN_WITHCREDENTIALS_TRUE_IN_SYNC_EXCEPTION))) {
                                return super.getContentCharset();
                            }
                            return charsetFinal;
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
            webResponse_ = new NetworkErrorWebResponse(webRequest_, e);
            setState(HEADERS_RECEIVED, context);
            setState(DONE, context);
            if (async_) {
                processError(context);
            }
            else {
                throw new RuntimeException(e);
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
        if ("content-type".equals(name)) {
            final String lcValue = value.toLowerCase(Locale.ROOT);
            if (lcValue.startsWith(FormEncodingType.URL_ENCODED.getName())
                || lcValue.startsWith(FormEncodingType.MULTIPART.getName())
                || lcValue.startsWith("text/plain")) {
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
     * Invokes the onerror handler if one has been set.
     * @param context the context within which the onerror handler is to be invoked;
     *                if {@code null}, the current thread's context is used.
     */
    private void processError(final Global context) {
        if (errorHandler_ != null) {
//            final Scriptable scope = errorHandler_.getParentScope();
            final AbstractJavaScriptEngine<?> jsEngine = containingPage_.getWebClient().getJavaScriptEngine();

            final Object[] params = new Event2[] {new ProgressEvent2(this, Event2.TYPE_ERROR)};

            if (LOG.isDebugEnabled()) {
                LOG.debug("Calling onerror handler");
            }
            ScriptRuntime.apply(errorHandler_, context, params);
//            jsEngine.callFunction(containingPage_, errorHandler_, this, scope, params);
            if (LOG.isDebugEnabled()) {
                if (context == null) {
//                    context = Context.getCurrentContext();
                }
                LOG.debug("Calling onerror handler done.");
            }
        }
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(XMLHttpRequest2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final class NetworkErrorWebResponse extends WebResponse {
        private final WebRequest request_;
        private final IOException error_;

        private NetworkErrorWebResponse(final WebRequest webRequest, final IOException error) {
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

    /**
     * Function constructor.
     */
    @ClassConstructor
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("XMLHttpRequest",
                    staticHandle("constructor", XMLHttpRequest2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("XMLHttpRequest");
        }
    }
}
