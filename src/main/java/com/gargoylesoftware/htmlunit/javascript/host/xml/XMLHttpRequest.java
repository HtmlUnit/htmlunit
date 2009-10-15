/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.WebResponseWrapper;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for a XMLHttpRequest.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Stuart Begg
 * @see <a href="http://developer.apple.com/internet/webcontent/xmlhttpreq.html">Safari documentation</a>
 */
public class XMLHttpRequest extends SimpleScriptable {

    private static final long serialVersionUID = 2369039843039430664L;
    private static final Log LOG = LogFactory.getLog(XMLHttpRequest.class);

    /** The object has been created, but not initialized (the open() method has not been called). */
    public static final int STATE_UNINITIALIZED = 0;
    /** The object has been created, but the send() method has not been called. */
    public static final int STATE_LOADING = 1;
    /** The send() method has been called, but the status and headers are not yet available. */
    public static final int STATE_LOADED = 2;
    /** Some data has been received. */
    public static final int STATE_INTERACTIVE = 3;
    /** All the data has been received; the complete data is available in responseBody and responseText. */
    public static final int STATE_COMPLETED = 4;

    private static final String[] ALL_PROPERTIES_ = {"onreadystatechange", "readyState", "responseText", "responseXML",
        "status", "statusText", "abort", "getAllResponseHeaders", "getResponseHeader", "open", "send",
        "setRequestHeader"};

    private int state_;
    private Function stateChangeHandler_;
    private Function loadHandler_;
    private Function errorHandler_;
    private WebRequestSettings requestSettings_;
    private boolean async_;
    private int threadID_;
    private WebResponse webResponse_;
    private String overriddenMimeType_;
    private HtmlPage containingPage_;
    private boolean caseSensitiveProperties_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public XMLHttpRequest() {
        this(true);
    }

    /**
     * Creates a new instance.
     * @param caseSensitiveProperties if properties and methods are case sensitive
     */
    public XMLHttpRequest(final boolean caseSensitiveProperties) {
        caseSensitiveProperties_ = caseSensitiveProperties;
        state_ = STATE_UNINITIALIZED;
    }

    /**
     * JavaScript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * Returns the event handler that fires on every state change.
     * @return the event handler that fires on every state change
     */
    public Function jsxGet_onreadystatechange() {
        return stateChangeHandler_;
    }

    /**
     * Sets the event handler that fires on every state change.
     * @param stateChangeHandler the event handler that fires on every state change
     */
    public void jsxSet_onreadystatechange(final Function stateChangeHandler) {
        stateChangeHandler_ = stateChangeHandler;
        if (state_ == STATE_LOADING) {
            setState(state_, null);
        }
    }

    /**
     * Sets the state as specified and invokes the state change handler if one has been set.
     * @param state the new state
     * @param context the context within which the state change handler is to be invoked;
     *                if <tt>null</tt>, the current thread's context is used.
     */
    private void setState(final int state, Context context) {
        state_ = state;

        //Firefox doesn't trigger onreadystatechange handler for sync requests
        final boolean ie = getBrowserVersion().isIE();
        if (stateChangeHandler_ != null && (ie || async_)) {
            if (context == null) {
                context = Context.getCurrentContext();
            }
            final Scriptable scope = stateChangeHandler_.getParentScope();
            final JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();

            final int nbExecutions;
            if (async_ && STATE_LOADING == state) {
                // quite strange but IE and Mozilla seem both to fire state loading twice
                // in async mode (at least with HTML of the unit tests)
                nbExecutions = 2;
            }
            else {
                nbExecutions = 1;
            }

            final Scriptable thisValue;
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.XMLHTTPREQUEST_HANDLER_THIS_IS_FUNCTION)) {
                thisValue = stateChangeHandler_;
            }
            else {
                thisValue = this;
            }
            for (int i = 0; i < nbExecutions; i++) {
                LOG.debug("Calling onreadystatechange handler for state " + state);
                jsEngine.callFunction(containingPage_, stateChangeHandler_, context,
                        scope, thisValue, ArrayUtils.EMPTY_OBJECT_ARRAY);
                LOG.debug("onreadystatechange handler: " + context.decompileFunction(stateChangeHandler_, 4));
                LOG.debug("Calling onreadystatechange handler for state " + state + ". Done.");
            }
        }

        // Firefox has a separate onload handler, too.
        if (!ie && loadHandler_ != null && state == STATE_COMPLETED) {
            if (context == null) {
                context = Context.getCurrentContext();
            }
            final Scriptable scope = loadHandler_.getParentScope();
            final JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();
            jsEngine.callFunction(containingPage_, loadHandler_, context, scope, this, ArrayUtils.EMPTY_OBJECT_ARRAY);
        }
    }

    /**
     * Returns the event handler that fires on load.
     * @return the event handler that fires on load
     */
    public Function jsxGet_onload() {
        return loadHandler_;
    }

    /**
     * Sets the event handler that fires on load.
     * @param loadHandler the event handler that fires on load
     */
    public void jsxSet_onload(final Function loadHandler) {
        loadHandler_ = loadHandler;
    }

    /**
     * Returns the event handler that fires on error.
     * @return the event handler that fires on error
     */
    public Function jsxGet_onerror() {
        return errorHandler_;
    }

    /**
     * Sets the event handler that fires on error.
     * @param errorHandler the event handler that fires on error
     */
    public void jsxSet_onerror(final Function errorHandler) {
        errorHandler_ = errorHandler;
    }

    /**
     * Invokes the onerror handler if one has been set.
     * @param context the context within which the onerror handler is to be invoked;
     *                if <tt>null</tt>, the current thread's context is used.
     */
    private void processError(Context context) {
        if (errorHandler_ != null && !getBrowserVersion().isIE()) {
            if (context == null) {
                context = Context.getCurrentContext();
            }
            final Scriptable scope = errorHandler_.getParentScope();
            final JavaScriptEngine jsEngine = containingPage_.getWebClient().getJavaScriptEngine();

            LOG.debug("Calling onerror handler");
            jsEngine.callFunction(containingPage_, errorHandler_, context, this, scope, ArrayUtils.EMPTY_OBJECT_ARRAY);
            LOG.debug("onerror handler: " + context.decompileFunction(errorHandler_, 4));
            LOG.debug("Calling onerror handler done.");
        }
    }

    /**
     * Returns the current state of the HTTP request. The possible values are:
     * <ul>
     *   <li>0 = uninitialized</li>
     *   <li>1 = loading</li>
     *   <li>2 = loaded</li>
     *   <li>3 = interactive</li>
     *   <li>4 = complete</li>
     * </ul>
     * @return the current state of the HTTP request
     */
    public int jsxGet_readyState() {
        return state_;
    }

    /**
     * Returns a string version of the data retrieved from the server.
     * @return a string version of the data retrieved from the server
     */
    public String jsxGet_responseText() {
        if (webResponse_ != null) {
            return webResponse_.getContentAsString();
        }
        LOG.debug("XMLHttpRequest.responseText was retrieved before the response was available.");
        return "";
    }

    /**
     * Returns a DOM-compatible document object version of the data retrieved from the server.
     * @return a DOM-compatible document object version of the data retrieved from the server
     */
    public Object jsxGet_responseXML() {
        if (webResponse_.getContentType().contains("xml")) {
            try {
                final XmlPage page = new XmlPage(webResponse_, getWindow().getWebWindow());
                final XMLDocument doc;
                if (getBrowserVersion().isIE()) {
                    doc = ActiveXObject.buildXMLDocument(null);
                }
                else {
                    doc = new XMLDocument();
                    doc.setPrototype(getPrototype(doc.getClass()));
                }
                doc.setParentScope(getWindow());
                doc.setDomNode(page);
                return doc;
            }
            catch (final IOException e) {
                LOG.warn("Failed parsing XML document " + webResponse_.getRequestSettings().getUrl() + ": "
                        + e.getMessage());
                return null;
            }
        }
        LOG.debug("XMLHttpRequest.responseXML was called but the response is "
                + webResponse_.getContentType());
        return null;
    }

    /**
     * Returns the numeric status returned by the server, such as 404 for "Not Found"
     * or 200 for "OK".
     * @return the numeric status returned by the server
     */
    public int jsxGet_status() {
        if (webResponse_ != null) {
            return webResponse_.getStatusCode();
        }
        LOG.error("XMLHttpRequest.status was retrieved before the response was available.");
        return 0;
    }

    /**
     * Returns the string message accompanying the status code, such as "Not Found" or "OK".
     * @return the string message accompanying the status code
     */
    public String jsxGet_statusText() {
        if (webResponse_ != null) {
            return webResponse_.getStatusMessage();
        }
        LOG.error("XMLHttpRequest.statusText was retrieved before the response was available.");
        return null;
    }

    /**
     * Cancels the current HTTP request.
     */
    public void jsxFunction_abort() {
        getWindow().getWebWindow().getJobManager().stopJob(threadID_);
    }

    /**
     * Returns the labels and values of all the HTTP headers.
     * @return the labels and values of all the HTTP headers
     */
    public String jsxFunction_getAllResponseHeaders() {
        if (webResponse_ != null) {
            final StringBuilder buffer = new StringBuilder();
            for (final NameValuePair header : webResponse_.getResponseHeaders()) {
                buffer.append(header.getName()).append(": ").append(header.getValue()).append("\n");
            }
            return buffer.toString();
        }
        LOG.error("XMLHttpRequest.getAllResponseHeaders() was called before the response was available.");
        return null;
    }

    /**
     * Retrieves the value of an HTTP header from the response body.
     * @param headerName the (case-insensitive) name of the header to retrieve
     * @return the value of the specified HTTP header
     */
    public String jsxFunction_getResponseHeader(final String headerName) {
        if (webResponse_ != null) {
            return webResponse_.getResponseHeaderValue(headerName);
        }
        LOG.error("XMLHttpRequest.getResponseHeader() was called before the response was available.");
        return null;
    }

    /**
     * Assigns the destination URL, method and other optional attributes of a pending request.
     * @param method the method to use to send the request to the server (GET, POST, etc)
     * @param urlParam the URL to send the request to
     * @param async Whether or not to send the request to the server asynchronously
     * @param user If authentication is needed for the specified URL, the username to use to authenticate
     * @param password If authentication is needed for the specified URL, the password to use to authenticate
     */
    public void jsxFunction_open(final String method, final Object urlParam, final boolean async,
        final String user, final String password) {
        if (urlParam == null || "".equals(urlParam)) {
            throw Context.reportRuntimeError("URL for XHR.open can't be empty!");
        }

        final String url = Context.toString(urlParam);

        // (URL + Method + User + Password) become a WebRequestSettings instance.
        containingPage_ = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        try {
            final URL fullUrl = containingPage_.getFullyQualifiedUrl(url);
            final WebRequestSettings settings = new WebRequestSettings(fullUrl);
            settings.setCharset("UTF-8");
            settings.setAdditionalHeader("Referer", containingPage_.getWebResponse().getRequestSettings().getUrl()
                    .toExternalForm());
            final HttpMethod submitMethod = HttpMethod.valueOf(method.toUpperCase());
            settings.setHttpMethod(submitMethod);
            if (user != null) {
                final DefaultCredentialsProvider dcp = new DefaultCredentialsProvider();
                dcp.addCredentials(user, password);
                settings.setCredentialsProvider(dcp);
            }
            requestSettings_ = settings;
        }
        catch (final MalformedURLException e) {
            LOG.error("Unable to initialize XMLHttpRequest using malformed URL '" + url + "'.");
            return;
        }
        // Async stays a boolean.
        async_ = async;
        // Change the state!
        setState(STATE_LOADING, null);
    }

    /**
     * Sends the specified content to the server in an HTTP request and receives the response.
     * @param content the body of the message being sent with the request
     */
    public void jsxFunction_send(final Object content) {
        prepareRequest(content);

        final WebClient client = getWindow().getWebWindow().getWebClient();
        final AjaxController ajaxController = client.getAjaxController();
        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        final boolean synchron = ajaxController.processSynchron(page, requestSettings_, async_);
        if (synchron) {
            doSend(Context.getCurrentContext());
        }
        else {
            // Create and start a thread in which to execute the request.
            final Object startingScope = getWindow();
            final ContextFactory cf = client.getJavaScriptEngine().getContextFactory();
            final ContextAction action = new ContextAction() {
                public Object run(final Context cx) {
                    cx.putThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE, startingScope);
                    doSend(cx);
                    return null;
                }
            };
            final JavaScriptJob job = new JavaScriptJob() {
                public void run() {
                    cf.call(action);
                }
                @Override
                public String toString() {
                    return "XMLHttpRequest Job " + getId();
                }
            };
            LOG.debug("Starting XMLHttpRequest thread for asynchronous request");
            threadID_ = getWindow().getWebWindow().getJobManager().addJob(job, page);
        }
    }

    /**
     * Prepares the WebRequestSettings that will be sent.
     * @param content the content to send
     */
    private void prepareRequest(final Object content) {
        if (HttpMethod.POST == requestSettings_.getHttpMethod()
            && content != null
            && !Context.getUndefinedValue().equals(content)) {
            final String body = Context.toString(content);
            if (body.length() > 0) {
                LOG.debug("Setting request body to: " + body);
                requestSettings_.setRequestBody(body);
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
            setState(STATE_LOADED, context);
            final WebResponse webResponse = wc.loadWebResponse(requestSettings_);
            LOG.debug("Web response loaded successfully.");
            if (overriddenMimeType_ == null) {
                webResponse_ = webResponse;
            }
            else {
                webResponse_ = new WebResponseWrapper(webResponse) {
                    private static final long serialVersionUID = -3359539772772336918L;
                    @Override
                    public String getContentType() {
                        return overriddenMimeType_;
                    }
                };
            }
            setState(STATE_INTERACTIVE, context);
            setState(STATE_COMPLETED, context);
        }
        catch (final IOException e) {
            LOG.debug("IOException: returning a network error response.");
            webResponse_ = new NetworkErrorWebResponse(requestSettings_);
            setState(STATE_COMPLETED, context);
            processError(context);
        }
    }

    /**
     * Sets the specified header to the specified value. The <tt>open</tt> method must be
     * called before this method, or an error will occur.
     * @param name the name of the header being set
     * @param value the value of the header being set
     */
    public void jsxFunction_setRequestHeader(final String name, final String value) {
        if (requestSettings_ != null) {
            requestSettings_.setAdditionalHeader(name, value);
        }
        else {
            throw Context.reportRuntimeError("The open() method must be called before setRequestHeader().");
        }
    }

    /**
     * Override the mime type returned by the server (if any). This may be used, for example, to force a stream
     * to be treated and parsed as text/xml, even if the server does not report it as such.
     * This must be done before the send method is invoked.
     * @param mimeType the type used to override that returned by the server (if any)
     * @see <a href="http://xulplanet.com/references/objref/XMLHttpRequest.html#method_overrideMimeType">XUL Planet</a>
     */
    public void jsxFunction_overrideMimeType(final String mimeType) {
        overriddenMimeType_ = mimeType;
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

    private static final class NetworkErrorWebResponse implements WebResponse {

        private static final long serialVersionUID = 6354426394575804571L;

        private final WebRequestSettings webRequestSettings_;

        private NetworkErrorWebResponse(final WebRequestSettings webRequestSettings) {
            webRequestSettings_ = webRequestSettings;
        }

        public int getStatusCode() {
            return 0;
        }

        public String getStatusMessage() {
            return "";
        }

        public String getContentType() {
            return "";
        }

        public String getContentAsString() {
            return "";
        }

        public String getContentAsString(final String encoding) {
            return "";
        }

        public InputStream getContentAsStream() throws IOException {
            return null;
        }

        public byte[] getContentAsBytes() {
            return new byte[0];
        }

        /**
         * {@inheritDoc}
         * @deprecated As of 2.6, please use {@link #getRequestSettings()}.getUrl()
         */
        @Deprecated
        public URL getRequestUrl() {
            return webRequestSettings_.getUrl();
        }

        /**
         * {@inheritDoc}
         * @deprecated As of 2.6, please use {@link #getRequestSettings()}.getHttpMethod()
         */
        @Deprecated
        public HttpMethod getRequestMethod() {
            return webRequestSettings_.getHttpMethod();
        }

        public List<NameValuePair> getResponseHeaders() {
            return Collections.emptyList();
        }

        public String getResponseHeaderValue(final String headerName) {
            return "";
        }

        public long getLoadTime() {
            return 0;
        }

        public String getContentCharSet() {
            return "";
        }

        public String getContentCharset() {
            return "";
        }

        public String getContentCharsetOrNull() {
            return "";
        }

        public WebRequestSettings getRequestSettings() {
            return webRequestSettings_;
        }
    }
}
