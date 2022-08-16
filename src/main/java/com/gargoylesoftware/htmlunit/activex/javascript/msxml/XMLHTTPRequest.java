/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.xml.FormData;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLHTTPRequest.<br>
 * Provides client-side protocol support for communication with HTTP servers.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms759148.aspx">MSDN documentation</a>
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Stuart Begg
 * @author Ronald Brill
 * @author Sebastian Cato
 * @author Frank Danek
 * @author Jake Cobb
 * @author Joerg Werner
 */
@JsxClass(IE)
public class XMLHTTPRequest extends MSXMLScriptable {

    private static final Log LOG = LogFactory.getLog(XMLHTTPRequest.class);

    /** The object has been created, but not initialized (the open() method has not been called). */
    public static final int STATE_UNSENT = 0;
    /** The object has been created, but the send() method has not been called. */
    public static final int STATE_OPENED = 1;
    /** The send() method has been called, but the status and headers are not yet available. */
    public static final int STATE_HEADERS_RECEIVED = 2;
    /** Some data has been received. */
    public static final int STATE_LOADING = 3;
    /** All the data has been received; the complete data is available in responseBody and responseText. */
    public static final int STATE_DONE = 4;

    private static final char REQUEST_HEADERS_SEPARATOR = ',';

    private static final String ALLOW_ORIGIN_ALL = "*";

    private static final String[] ALL_PROPERTIES_ = {"onreadystatechange", "readyState", "responseText", "responseXML",
        "status", "statusText", "abort", "getAllResponseHeaders", "getResponseHeader", "open", "send",
        "setRequestHeader"};

    private static final Collection<String> PROHIBITED_HEADERS_ = Arrays.asList(
        "accept-charset", HttpHeader.ACCEPT_ENCODING_LC,
        HttpHeader.CONNECTION_LC, HttpHeader.CONTENT_LENGTH_LC, HttpHeader.COOKIE_LC, "cookie2",
        "content-transfer-encoding", "date", "expect",
        HttpHeader.HOST_LC, "keep-alive", HttpHeader.REFERER_LC, "te", "trailer", "transfer-encoding", "upgrade",
        HttpHeader.USER_AGENT_LC, "via");

    private int state_ = STATE_UNSENT;
    private Function stateChangeHandler_;
    private WebRequest webRequest_;
    private boolean async_;
    private int jobID_;
    private WebResponse webResponse_;
    private HtmlPage containingPage_;
    private boolean openedMultipleTimes_;
    private boolean sent_;

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public XMLHTTPRequest() {
    }

    /**
     * Returns the event handler to be called when the <code>readyState</code> property changes.
     * @return the event handler to be called when the readyState property changes
     */
    @JsxGetter
    public Object getOnreadystatechange() {
        if (stateChangeHandler_ == null) {
            return Undefined.instance;
        }
        return stateChangeHandler_;
    }

    /**
     * Sets the event handler to be called when the <code>readyState</code> property changes.
     * @param stateChangeHandler the event handler to be called when the readyState property changes
     */
    @JsxSetter
    public void setOnreadystatechange(final Function stateChangeHandler) {
        stateChangeHandler_ = stateChangeHandler;
        if (state_ == STATE_OPENED) {
            setState(state_, null);
        }
    }

    /**
     * Sets the state as specified and invokes the state change handler if one has been set.
     * @param state the new state
     * @param context the context within which the state change handler is to be invoked;
     *     if {@code null}, the current thread's context is used
     */
    private void setState(final int state, Context context) {
        state_ = state;

        if (stateChangeHandler_ != null && !openedMultipleTimes_) {
            final Scriptable scope = stateChangeHandler_.getParentScope();
            final JavaScriptEngine jsEngine = (JavaScriptEngine) containingPage_.getWebClient().getJavaScriptEngine();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Calling onreadystatechange handler for state " + state);
            }
            final Object[] params = ArrayUtils.EMPTY_OBJECT_ARRAY;

            jsEngine.callFunction(containingPage_, stateChangeHandler_, scope, this, params);
            if (LOG.isDebugEnabled()) {
                if (context == null) {
                    context = Context.getCurrentContext();
                }
                LOG.debug("onreadystatechange handler: " + context.decompileFunction(stateChangeHandler_, 4));
                LOG.debug("Calling onreadystatechange handler for state " + state + ". Done.");
            }
        }
    }

    /**
     * Returns the state of the request. The possible values are:
     * <ul>
     *   <li>0 = unsent</li>
     *   <li>1 = opened</li>
     *   <li>2 = headers_received</li>
     *   <li>3 = loading</li>
     *   <li>4 = done</li>
     * </ul>
     * @return the state of the request
     */
    @JsxGetter
    public int getReadyState() {
        return state_;
    }

    /**
     * Returns the response entity body as a string.
     * @return the response entity body as a string
     */
    @JsxGetter
    public String getResponseText() {
        if (state_ == STATE_UNSENT) {
            throw Context.reportRuntimeError(
                    "The data necessary to complete this operation is not yet available (request not opened).");
        }
        if (state_ != STATE_DONE) {
            throw Context.reportRuntimeError("Unspecified error (request not sent).");
        }
        if (webResponse_ != null) {
            final String content = webResponse_.getContentAsString();
            if (content == null) {
                return "";
            }
            return content;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("XMLHTTPRequest.responseText was retrieved before the response was available.");
        }
        return "";
    }

    /**
     * Returns the parsed response entity body.
     * @return the parsed response entity body
     */
    @JsxGetter
    public Object getResponseXML() {
        if (state_ == STATE_UNSENT) {
            throw Context.reportRuntimeError("Unspecified error (request not opened).");
        }
        final Window w = getWindow();
        if (state_ == STATE_DONE && webResponse_ != null && !(webResponse_ instanceof NetworkErrorWebResponse)) {
            final String contentType = webResponse_.getContentType();
            if (contentType.contains("xml")) {
                try {
                    final XmlPage page = new XmlPage(webResponse_, w.getWebWindow(), true, false);
                    final XMLDOMDocument doc = new XMLDOMDocument();
                    doc.setDomNode(page);
                    doc.setPrototype(getPrototype(doc.getClass()));
                    doc.setEnvironment(getEnvironment());
                    doc.setParentScope(w);
                    return doc;
                }
                catch (final IOException e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Failed parsing XML document " + webResponse_.getWebRequest().getUrl() + ": "
                                + e.getMessage());
                    }
                    return null;
                }
            }
        }
        final XMLDOMDocument doc = new XMLDOMDocument(w.getWebWindow());
        doc.setPrototype(getPrototype(doc.getClass()));
        doc.setEnvironment(getEnvironment());
        return doc;
    }

    /**
     * Returns the HTTP status code returned by a request.
     * @return the HTTP status code returned by a request
     */
    @JsxGetter
    public int getStatus() {
        if (state_ != STATE_DONE) {
            throw Context.reportRuntimeError("Unspecified error (request not sent).");
        }
        if (webResponse_ != null) {
            return webResponse_.getStatusCode();
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("XMLHTTPRequest.status was retrieved without a response available (readyState: "
                + state_ + ").");
        }
        return 0;
    }

    /**
     * Returns the HTTP response line status.
     * @return the HTTP response line status
     */
    @JsxGetter
    public String getStatusText() {
        if (state_ != STATE_DONE) {
            throw Context.reportRuntimeError("Unspecified error (request not sent).");
        }
        if (webResponse_ != null) {
            return webResponse_.getStatusMessage();
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("XMLHTTPRequest.statusText was retrieved without a response available (readyState: "
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
        setState(STATE_UNSENT, Context.getCurrentContext());
    }

    /**
     * Returns the values of all the HTTP headers.
     * @return the resulting header information
     */
    @JsxFunction
    public String getAllResponseHeaders() {
        if (state_ == STATE_UNSENT || state_ == STATE_OPENED) {
            throw Context.reportRuntimeError("Unspecified error (request not sent).");
        }
        if (webResponse_ != null) {
            final StringBuilder builder = new StringBuilder();
            for (final NameValuePair header : webResponse_.getResponseHeaders()) {
                builder.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
            }
            return builder.append("\r\n").toString();
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("XMLHTTPRequest.getAllResponseHeaders() was called without a response available (readyState: "
                + state_ + ").");
        }
        return null;
    }

    /**
     * Retrieves the value of an HTTP header from the response body.
     * @param header the case-insensitive header name
     * @return the resulting header information
     */
    @JsxFunction
    public String getResponseHeader(final String header) {
        if (state_ == STATE_UNSENT || state_ == STATE_OPENED) {
            throw Context.reportRuntimeError("Unspecified error (request not sent).");
        }
        if (header == null || "null".equals(header)) {
            throw Context.reportRuntimeError("Type mismatch (header is null).");
        }
        if ("".equals(header)) {
            throw Context.reportRuntimeError("Invalid argument (header is empty).");
        }
        if (webResponse_ != null) {
            final String value = webResponse_.getResponseHeaderValue(header);
            if (value == null) {
                return "";
            }
            return value;
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("XMLHTTPRequest.getResponseHeader(..) was called without a response available (readyState: "
                + state_ + ").");
        }
        return null;
    }

    /**
     * Initializes the request and specifies the method, URL, and authentication information for the request.
     * @param method the HTTP method used to open the connection, such as GET, POST, PUT, or PROPFIND;
     *      for XMLHTTP, this parameter is not case-sensitive; the verbs TRACE and TRACK are not allowed.
     * @param url the requested URL; this can be either an absolute URL or a relative URL
     * @param asyncParam indicator of whether the call is asynchronous; the default is {@code true} (the call
     *     returns immediately); if set to {@code true}, attach an <code>onreadystatechange</code> property
     *     callback so that you can tell when the <code>send</code> call has completed
     * @param user the name of the user for authentication
     * @param password the password for authentication
     */
    @JsxFunction
    public void open(final String method, final Object url, final Object asyncParam,
        final Object user, final Object password) {
        if (method == null || "null".equals(method)) {
            throw Context.reportRuntimeError("Type mismatch (method is null).");
        }
        if (url == null || "null".equals(url)) {
            throw Context.reportRuntimeError("Type mismatch (url is null).");
        }
        state_ = STATE_UNSENT;
        openedMultipleTimes_ = webRequest_ != null;
        sent_ = false;
        webRequest_ = null;
        webResponse_ = null;
        if ("".equals(method) || "TRACE".equalsIgnoreCase(method)) {
            throw Context.reportRuntimeError("Invalid procedure call or argument (method is invalid).");
        }
        if ("".equals(url)) {
            throw Context.reportRuntimeError("Invalid procedure call or argument (url is empty).");
        }

        // defaults to true if not specified
        boolean async = true;
        if (!Undefined.isUndefined(asyncParam)) {
            async = ScriptRuntime.toBoolean(asyncParam);
        }

        final String urlAsString = Context.toString(url);

        // (URL + Method + User + Password) become a WebRequest instance.
        containingPage_ = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();

        try {
            final URL fullUrl = containingPage_.getFullyQualifiedUrl(urlAsString);

            final WebRequest request = new WebRequest(fullUrl, UTF_8, containingPage_.getUrl());
            request.setHttpMethod(HttpMethod.valueOf(method.toUpperCase(Locale.ROOT)));

            // password is ignored if no user defined
            if (user != null && !Undefined.isUndefined(user)) {
                final String userCred = user.toString();

                String passwordCred = "";
                if (password != null && !Undefined.isUndefined(password)) {
                    passwordCred = password.toString();
                }

                request.setCredentials(new UsernamePasswordCredentials(userCred, passwordCred.toCharArray()));
            }
            webRequest_ = request;
        }
        catch (final MalformedURLException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Unable to initialize XMLHTTPRequest using malformed URL '" + urlAsString + "'.");
            }
            return;
        }
        catch (final IllegalArgumentException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Unable to initialize XMLHTTPRequest using illegal argument '" + e.getMessage() + "'.");
            }
            webRequest_ = null;
        }
        // Async stays a boolean.
        async_ = async;
        // Change the state!
        setState(STATE_OPENED, null);
    }

    /**
     * Sends an HTTP request to the server and receives a response.
     * @param body the body of the message being sent with the request.
     */
    @JsxFunction
    public void send(final Object body) {
        if (webRequest_ == null) {
            setState(STATE_DONE, Context.getCurrentContext());
            return;
        }
        if (sent_) {
            throw Context.reportRuntimeError("Unspecified error (request already sent).");
        }
        sent_ = true;

        prepareRequest(body);

        // quite strange but IE seems to fire state loading twice
        setState(STATE_OPENED, Context.getCurrentContext());

        final Window w = getWindow();
        final WebClient client = w.getWebWindow().getWebClient();
        final AjaxController ajaxController = client.getAjaxController();
        final HtmlPage page = (HtmlPage) w.getWebWindow().getEnclosedPage();
        final boolean synchron = ajaxController.processSynchron(page, webRequest_, async_);
        if (synchron) {
            doSend(Context.getCurrentContext());
        }
        else {
            // Create and start a thread in which to execute the request.
            final ContextFactory cf = ((JavaScriptEngine) client.getJavaScriptEngine()).getContextFactory();
            final ContextAction<Object> action = cx -> {
                // KEY_STARTING_SCOPE maintains a stack of scopes
                @SuppressWarnings("unchecked")
                Deque<Scriptable> stack =
                        (Deque<Scriptable>) cx.getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
                if (null == stack) {
                    stack = new ArrayDeque<>();
                    cx.putThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE, stack);
                }
                stack.push(w);

                try {
                    doSend(cx);
                }
                finally {
                    stack.pop();
                }
                return null;
            };
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavascriptXMLHttpRequestJob(cf, action);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Starting XMLHTTPRequest thread for asynchronous request");
            }
            jobID_ = w.getWebWindow().getJobManager().addJob(job, page);
        }
    }

    /**
     * Prepares the WebRequest that will be sent.
     * @param content the content to send
     */
    private void prepareRequest(final Object content) {
        if (content != null && !Undefined.isUndefined(content)) {
            if (!"".equals(content) && HttpMethod.GET == webRequest_.getHttpMethod()) {
                webRequest_.setHttpMethod(HttpMethod.POST);
            }
            if (HttpMethod.POST == webRequest_.getHttpMethod()
                    || HttpMethod.PUT == webRequest_.getHttpMethod()
                    || HttpMethod.PATCH == webRequest_.getHttpMethod()) {
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
    }

    /**
     * The real send job.
     * @param context the current context
     */
    void doSend(final Context context) {
        final WebClient wc = getWindow().getWebWindow().getWebClient();
        try {
            final String originHeaderValue = webRequest_.getAdditionalHeaders().get(HttpHeader.ORIGIN);
            if (originHeaderValue != null && isPreflight()) {
                final WebRequest preflightRequest = new WebRequest(webRequest_.getUrl(), HttpMethod.OPTIONS);

                // header origin
                preflightRequest.setAdditionalHeader(HttpHeader.ORIGIN, originHeaderValue);

                // header request-method
                preflightRequest.setAdditionalHeader(
                        HttpHeader.ACCESS_CONTROL_REQUEST_METHOD,
                        webRequest_.getHttpMethod().name());

                // header request-headers
                final StringBuilder builder = new StringBuilder();
                for (final Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
                    final String name = header.getKey().toLowerCase(Locale.ROOT);
                    if (isPreflightHeader(name, header.getValue())) {
                        if (builder.length() != 0) {
                            builder.append(REQUEST_HEADERS_SEPARATOR);
                        }
                        builder.append(name);
                    }
                }
                preflightRequest.setAdditionalHeader(HttpHeader.ACCESS_CONTROL_REQUEST_HEADERS, builder.toString());

                // do the preflight request
                final WebResponse preflightResponse = wc.loadWebResponse(preflightRequest);
                if (!isPreflightAuthorized(preflightResponse)) {
                    setState(STATE_HEADERS_RECEIVED, context);
                    setState(STATE_LOADING, context);
                    setState(STATE_DONE, context);
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
            // this kind of web responses using UTF-8 as default encoding
            webResponse.defaultCharsetUtf8();

            boolean allowOriginResponse = true;
            if (originHeaderValue != null) {
                final String value = webResponse.getResponseHeaderValue(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN);
                allowOriginResponse = originHeaderValue.equals(value);
                allowOriginResponse = allowOriginResponse || ALLOW_ORIGIN_ALL.equals(value);
            }
            if (allowOriginResponse) {
                webResponse_ = webResponse;
            }
            if (allowOriginResponse) {
                setState(STATE_HEADERS_RECEIVED, context);
                setState(STATE_LOADING, context);
                setState(STATE_DONE, context);
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
            setState(STATE_HEADERS_RECEIVED, context);
            setState(STATE_DONE, context);
            if (!async_) {
                throw Context.reportRuntimeError("Object not found.");
            }
        }
    }

    private boolean isPreflight() {
        final HttpMethod method = webRequest_.getHttpMethod();
        if (method != HttpMethod.GET && method != HttpMethod.HEAD && method != HttpMethod.POST) {
            return true;
        }
        for (final Entry<String, String> header : webRequest_.getAdditionalHeaders().entrySet()) {
            if (isPreflightHeader(header.getKey().toLowerCase(Locale.ROOT),
                    header.getValue())) {
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
            return !(lcValue.startsWith(FormEncodingType.URL_ENCODED.getName())
                || lcValue.startsWith(FormEncodingType.MULTIPART.getName())
                || lcValue.startsWith(MimeType.TEXT_PLAIN));
        }

        return !(HttpHeader.ACCEPT_LC.equals(name)
                || HttpHeader.ACCEPT_LANGUAGE_LC.equals(name)
                || HttpHeader.CONTENT_LANGUAGE_LC.equals(name)
                || HttpHeader.REFERER_LC.equals(name)
                || HttpHeader.ACCEPT_ENCODING_LC.equals(name)
                || HttpHeader.ORIGIN_LC.equals(name));
    }

    /**
     * Sets the specified header to the specified value.<br>
     * The <code>open</code> method must be called before this method, or an error will occur.
     * @param name the header name to set
     * @param value the value of the header
     */
    @JsxFunction
    public void setRequestHeader(final String name, final String value) {
        if (name == null || "null".equals(name)) {
            throw Context.reportRuntimeError("Type mismatch (name is null).");
        }
        if ("".equals(name)) {
            throw Context.reportRuntimeError("Invalid argument (name is empty).");
        }
        if (value == null || "null".equals(value)) {
            throw Context.reportRuntimeError("Type mismatch (value is null).");
        }
        if (StringUtils.isBlank(value)) {
            return;
        }
        if (!isAuthorizedHeader(name)) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Ignoring XMLHTTPRequest.setRequestHeader for " + name
                        + ": it is a restricted header");
            }
            return;
        }

        if (webRequest_ == null) {
            throw Context.reportRuntimeError("Unspecified error (request not opened).");
        }
        webRequest_.setAdditionalHeader(name, value);
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
        if (nameLowerCase.startsWith("proxy-") || nameLowerCase.startsWith("sec-")) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, final Scriptable start) {
        for (final String property : ALL_PROPERTIES_) {
            if (property.equalsIgnoreCase(name)) {
                name = property;
                break;
            }
        }
        return super.get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(String name, final Scriptable start, final Object value) {
        for (final String property : ALL_PROPERTIES_) {
            if (property.equalsIgnoreCase(name)) {
                name = property;
                break;
            }
        }
        super.put(name, start, value);
    }

    private static final class NetworkErrorWebResponse extends WebResponse {
        private final WebRequest request_;

        NetworkErrorWebResponse(final WebRequest webRequest) {
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
    }
}
