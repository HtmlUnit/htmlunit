/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Document;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.util.WebResponseWrapper;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;

/**
 * A JavaScript object for a XMLHttpRequest.
 * 
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @version $Revision$
 * @see <a href="http://developer.apple.com/internet/webcontent/xmlhttpreq.html">Safari documentation</a>
 */
public class XMLHttpRequest extends SimpleScriptable {

    private static final long serialVersionUID = 2369039843039430664L;

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

    private int state_;
    private Function stateChangeHandler_;
    private WebRequestSettings requestSettings_;
    private boolean async_;
    private int threadID_;
    private WebResponse webResponse_;
    private String overridenMimeType_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public XMLHttpRequest() {
        state_ = STATE_UNINITIALIZED;
    }

    /**
     * Returns the event handler that fires on every state change.
     * @return The event handler that fires on every state change.
     */
    public Function jsxGet_onreadystatechange() {
        return stateChangeHandler_;
    }

    /**
     * Sets the event handler that fires on every state change.
     * @param stateChangeHandler The event handler that fires on every state change.
     */
    public void jsxSet_onreadystatechange( final Function stateChangeHandler ) {
        stateChangeHandler_ = stateChangeHandler;
    }

    /**
     * Sets the state as specified and invokes the state change handler if one has been set.
     * @param state The new state.
     * @param context The context within which the state change handler is to be invoked;
     *                if <tt>null</tt>, the current thread's context is used.
     */
    private void setState( final int state, Context context ) {
        state_ = state;
        if( stateChangeHandler_ != null ) {
            if( context == null ) {
                context = Context.getCurrentContext();
            }
            final Scriptable scope = stateChangeHandler_.getParentScope();
            final Object[] args = new Object[ 0 ];
            stateChangeHandler_.call( context, scope, this, args );
            // quite strange but IE and Mozilla seem both to fire state loading twice
            // in async mode (at least with html of the unit tests)
            if (async_ && STATE_LOADING == state) {
                stateChangeHandler_.call( context, scope, this, args );
            }
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
     * @return The current state of the HTTP request.
     */
    public int jsxGet_readyState() {
        return state_;
    }

    /**
     * Returns a string version of the data retrieved from the server.
     * @return A string version of the data retrieved from the server.
     */
    public String jsxGet_responseText() {
        if( webResponse_ != null ) {
            return webResponse_.getContentAsString();
        }
        else {
            getLog().debug( "XMLHttpRequest.responseText was retrieved before the response was available." );
            return "";
        }
    }

    /**
     * Returns a DOM-compatible document object version of the data retrieved from the server.
     * @return A DOM-compatible document object version of the data retrieved from the server.
     */
    public Object jsxGet_responseXML() {
        if (webResponse_.getContentType().indexOf("xml") != -1) {
            try {
                final Document doc = XmlUtil.buildDocument(webResponse_);
                return Context.javaToJS(doc, this);
            }
            catch (final Exception e) {
                getLog().warn("Failed parsing xml document " + webResponse_.getUrl() + ": " + e.getMessage());
                return null;
            }
        }
        else {
            getLog().debug("XMLHttpRequest.responseXML was called but the response is "
                    + webResponse_.getContentType());
            return null;
        }
    }

    /**
     * Returns the numeric status returned by the server, such as 404 for "Not Found"
     * or 200 for "OK".
     * @return The numeric status returned by the server.
     */
    public int jsxGet_status() {
        if( webResponse_ != null ) {
            return webResponse_.getStatusCode();
        }
        else {
            getLog().error( "XMLHttpRequest.status was retrieved before the response was available." );
            return 0;
        }
    }

    /**
     * Returns the string message accompanying the status code, such as "Not Found" or "OK".
     * @return The string message accompanying the status code.
     */
    public String jsxGet_statusText() {
        if( webResponse_ != null ) {
            return webResponse_.getStatusMessage();
        }
        else {
            getLog().error( "XMLHttpRequest.statusText was retrieved before the response was available." );
            return null;
        }
    }

    /**
     * Cancels the current HTTP request.
     */
    public void jsxFunction_abort() {
        getWindow().getWebWindow().getThreadManager().stopThread(threadID_);
    }

    /**
     * Returns the labels and values of all the HTTP headers.
     * @return The labels and values of all the HTTP headers.
     */
    public String jsxFunction_getAllResponseHeaders() {
        if (webResponse_ != null) {
            final StringBuffer buffer = new StringBuffer();
            final List headers = webResponse_.getResponseHeaders();
            for (final Iterator i = headers.iterator(); i.hasNext(); ) {
                final NameValuePair header = (NameValuePair) i.next();
                buffer.append(header.getName()).append(": ").append(header.getValue()).append("\n");
            }
            return buffer.toString();
        }
        else {
            getLog().error( "XMLHttpRequest.getAllResponseHeaders() was called before the response was available." );
            return null;
        }
    }

    /**
     * Retrieves the value of an HTTP header from the response body.
     * @param headerName The (case-insensitive) name of the header to retrieve.
     * @return The value of the specified HTTP header.
     */
    public String jsxFunction_getResponseHeader( final String headerName ) {
        if (webResponse_ != null) {
            return webResponse_.getResponseHeaderValue(headerName);
        }
        else {
            getLog().error( "XMLHttpRequest.getResponseHeader() was called before the response was available." );
            return null;
        }
    }

    /**
     * Assigns the destination URL, method and other optional attributes of a pending request.
     * @param method The method to use to send the request to the server (GET, POST, etc).
     * @param url The url to send the request to.
     * @param async Whether or not to send the request to the server asynchronously.
     * @param user If authentication is needed for the specified URL, the username to use to authenticate.
     * @param password If authentication is needed for the specified URL, the password to use to authenticate.
     */
    public void jsxFunction_open( final String method, final String url, final boolean async,
        final String user, final String password ) {
        // (URL + Method + User + Password) become a WebRequestSettings instance.
        try {
            final URL fullUrl = ((HtmlPage) getWindow().getWebWindow().getEnclosedPage()).getFullyQualifiedUrl(url);
            final WebRequestSettings settings = new WebRequestSettings(fullUrl);
            final SubmitMethod submitMethod;
            if( "POST".equalsIgnoreCase( method ) ) {
                submitMethod = SubmitMethod.POST;
            }
            else {
                submitMethod = SubmitMethod.GET;
            }
            settings.setSubmitMethod( submitMethod );
            if( user != null ) {
                final DefaultCredentialsProvider dcp = new DefaultCredentialsProvider();
                dcp.addCredentials( user, password );
                settings.setCredentialsProvider( dcp );
            }
            requestSettings_ = settings;
        }
        catch( final MalformedURLException e ) {
            getLog().error( "Unable to initialize XMLHttpRequest using malformed URL '" + url + "'." );
            return;
        }
        // Async stays a boolean.
        async_ = async;
        // Change the state!
        setState( STATE_LOADING, null );
    }

    /**
     * Sends the specified content to the server in an HTTP request and receives the response.
     * @param content The body of the message being sent with the request.
     */
    public void jsxFunction_send( final Object content ) {
        if (async_) {
            // Create and start a thread in which to execute the request.
            final Object startingScope = getWindow();

            final Runnable t = new Runnable() {
                public void run() {
                    final Context context = Context.enter();
                    try {
                        Context.getCurrentContext().putThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE, startingScope);
                        doSend(content, context);
                    }
                    finally {
                        Context.exit();
                    }
                }
            };
            getLog().debug("Starting XMLHttpRequest thread for asynchronous request");
            threadID_ = getWindow().getWebWindow().getThreadManager().startThread(t, "XMLHttpRequest.send");
        }
        else {
            doSend(content, Context.getCurrentContext());
        }
    }

    /**
     * The real send job
     * @param content the content to send
     * @param context the current context
     */
    private void doSend(final Object content, final Context context) {
        final WebClient wc = getWindow().getWebWindow().getWebClient();
        try {
            setState( STATE_LOADED, context );
            if (Context.getUndefinedValue().equals(content) && wc.getBrowserVersion().isNetscape()) {
                throw Context.reportRuntimeError("XMLHttpRequest.send: not enough arguments");
            }

            if (content != null && !Context.getUndefinedValue().equals(content)) {
                final String body = Context.toString(content);
                if (body.length() > 0) {
                    getLog().debug("Setting request body to: " + body);
                    requestSettings_.setRequestBody(body);
                }
            }
            final WebResponse webResponse = wc.loadWebResponse(requestSettings_);
            if (overridenMimeType_ == null) {
                webResponse_ = webResponse;
            }
            else {
                webResponse_ = new WebResponseWrapper(webResponse) {
                    public String getContentType() {
                        return overridenMimeType_;
                    }
                };
            }
            setState( STATE_INTERACTIVE, context );
            setState( STATE_COMPLETED, context );
        }
        catch( final IOException e ) {
            setState( STATE_LOADING, context );
            throw Context.reportRuntimeError( "Unable to send the XMLHttpRequest: " + e );
        }
    }

    /**
     * Sets the specified header to the specified value. The <tt>open</tt> method must be
     * called before this method, or an error will occur.
     * @param name The name of the header being set.
     * @param value The value of the header being set.
     */
    public void jsxFunction_setRequestHeader( final String name, final String value ) {
        if( requestSettings_ != null ) {
            requestSettings_.addAdditionalHeader( name, value );
        }
        else {
            throw Context.reportRuntimeError( "The open() method must be called before setRequestHeader()." );
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
        overridenMimeType_ = mimeType;
    }
}
