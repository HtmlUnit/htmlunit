/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.html.FocusableElement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  An object that represents a web browser
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author Dominique Broeglin
 * @author Noboru Sinohara
 * @author <a href="mailto:chen_jun@users.sourceforge.net"> Chen Jun</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 */
public class WebClient {

    private WebConnection webConnection_;
    private boolean printContentOnFailingStatusCode_ = true;
    private boolean throwExceptionOnFailingStatusCode_ = true;
    private CredentialProvider credentialProvider_ = new DenyAllCredentialProvider();
    private final String proxyHost_;
    private final int proxyPort_;
    private ScriptEngine scriptEngine_;
    private boolean javaScriptEnabled_ = true;
    private FocusableElement elementWithFocus_;
    private final Map requestHeaders_ = Collections.synchronizedMap(new HashMap(89));

    private AlertHandler   alertHandler_;
    private ConfirmHandler confirmHandler_;
    private PromptHandler  promptHandler_;
    private StatusHandler  statusHandler_;

    private BrowserVersion browserVersion_ = BrowserVersion.getDefault();
    private boolean isRedirectEnabled_ = true;
    private PageCreator pageCreator_ = new DefaultPageCreator();

    private final Set webWindowListeners_ = new HashSet(5);
    private final List webWindows_ = Collections.synchronizedList(new ArrayList());

    private WebWindow currentWindow_ = new TopLevelWindow("", this);
    private Stack firstWindowStack_ = new Stack();

    private static URLStreamHandler JavaScriptUrlStreamHandler_
        = new com.gargoylesoftware.htmlunit.protocol.javascript.Handler();
    private static URLStreamHandler AboutUrlStreamHandler_
        = new com.gargoylesoftware.htmlunit.protocol.about.Handler();
    //singleton WebResponse for "about:blank"
    private static final WebResponse WEB_RESPONSE_FOR_ABOUT_BLANK = new WebResponse() {
        public int getStatusCode() {
            return 200;
        }
        public String getStatusMessage() {
            return "OK";
        }
        public String getContentType() {
            return "text/html";
        }
        public String getContentAsString() {
            return "";
        }
        public InputStream getContentAsStream() {
            return TextUtil.toInputStream("");
        }
        public URL getUrl() {
            try {
                return new URL(null,"about:blank",AboutUrlStreamHandler_);
            }
            catch (MalformedURLException e) {
                // impossible
                e.printStackTrace();
                return null;
            }
        }
        public String getResponseHeaderValue(final String key) {
            return "";
        }
        public long getLoadTimeInMilliSeconds() {
            return 0;
        }
        public byte[] getResponseBody() {
            return "".getBytes();
        }
        public String getContentCharSet() {
            return "ISO-8859-1";
        }
    };

    private ScriptPreProcessor scriptPreProcessor_;
    private Map activeXObjectMap_ = Collections.EMPTY_MAP;
    private RefreshHandler refreshHandler_ = new DefaultRefreshHandler();


    /**
     * Create an instance using {@link BrowserVersion#FULL_FEATURED_BROWSER}
     */
    public WebClient() {
        this( BrowserVersion.FULL_FEATURED_BROWSER );
    }


    /**
     *  Create an instance using the specified {@link BrowserVersion}
     * @param  browserVersion The browser version to simulate
     */
    public WebClient( final BrowserVersion browserVersion ) {
        Assert.notNull("browserVersion", browserVersion);

        browserVersion_ = browserVersion;
        proxyHost_ = null;
        proxyPort_ = 0;
        try {
            scriptEngine_ = createJavaScriptEngineIfPossible( this );
        }
        catch( final NoClassDefFoundError e ) {
            scriptEngine_ = null;
        }
    }


    /**
     *  Create an instance that will use the specified {@link BrowserVersion} and proxy server
     * @param  browserVersion The browser version to simulate
     * @param  proxyHost The server that will act as proxy
     * @param  proxyPort The port to use on the proxy server
     */
    public WebClient( final BrowserVersion browserVersion, final String proxyHost, final int proxyPort ) {
        Assert.notNull("browserVersion", browserVersion);
        Assert.notNull( "proxyHost", proxyHost );

        browserVersion_ = browserVersion;
        proxyHost_ = proxyHost;
        proxyPort_ = proxyPort;
        try {
            scriptEngine_ = createJavaScriptEngineIfPossible( this );
        }
        catch( final NoClassDefFoundError e ) {
            scriptEngine_ = null;
        }
    }


    /**
     *  Create a javascript engine if possible.
     *
     * @param  webClient The webclient that we are creating the script engine for.
     * @return A javascript engine or null if one could not be created.
     */
    public static JavaScriptEngine createJavaScriptEngineIfPossible( final WebClient webClient ) {
        try {
            Class.forName( "org.mozilla.javascript.Context" );
            return new JavaScriptEngine( webClient );
        }
        catch( final ClassNotFoundException e ) {
            return null;
        }
        catch( final NoClassDefFoundError e ) {
            return null;
        }
    }


    /**
     * <p>Return the object that will resolve all url requests<p>
     * <p>INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK</p>
     * @return  The connection that will be used.
     */
    public synchronized WebConnection getWebConnection() {
        if( webConnection_ == null ) {
            if( proxyHost_ == null ) {
                webConnection_ = new HttpWebConnection( this );
            }
            else {
                webConnection_ = new HttpWebConnection( this, proxyHost_, proxyPort_ );
            }
        }

        return webConnection_;
    }


    /**
     *  Set the object that will resolve all url requests <p />
     *
     * This method is intended for unit testing HtmlUnit itself.  It is not expected
     * to change but you shouldn't need to call it during normal use of HtmlUnit.
     *
     * @param  webConnection The new web connection
     */
    public void setWebConnection( final WebConnection webConnection ) {
        Assert.notNull( "webConnection", webConnection );
        webConnection_ = webConnection;
    }


    /**
     *  Send a request to a server and return a Page that represents the
     *  response from the server. This is the same as calling {@link
     *  #getPage(WebWindow,URL,SubmitMethod,List)} with the current window,
     *  the GET method and no parameters
     *
     * @param  url The url of the server
     * @return  See above
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      "throwExceptionOnFailingStatusCode" is set to true (see {@link
     *      #setThrowExceptionOnFailingStatusCode(boolean)})
     */
    public Page getPage( final URL url )
        throws IOException, FailingHttpStatusCodeException {

        return getPage( getCurrentWindow(), url, SubmitMethod.GET, Collections.EMPTY_LIST );
    }


    /**
     *  Send a request to a server and return a Page that represents the
     *  response from the server. This is the same as calling {@link
     *  #getPage(WebWindow,URL,SubmitMethod,List)} with the current window.
     *
     * @param  url The url of the server
     * @param method The submit method to use when sending the request to the server
     * @param  parameters A list of {@link KeyValuePair}'s that are the parameters
     * to the request
     * @return  See above
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      "throwExceptionOnFailingStatusCode" is set to true (see {@link
     *      #setThrowExceptionOnFailingStatusCode(boolean)})
     */
    public Page getPage(
            final URL url,
            final SubmitMethod method,
            final List parameters )
        throws
            IOException,
            FailingHttpStatusCodeException {

        return getPage( getCurrentWindow(), url, method, parameters );
    }


    /**
     * Return a page.
     *
     * @param  webWindow The window that the new page will be loaded into.
     * @param  url The url of the server
     * @param  method The submit method. Ie Submit.GET or SubmitMethod.POST
     * @param  parameters A list of {@link
     *      com.gargoylesoftware.htmlunit.KeyValuePair KeyValuePair}'s that
     *      contain the parameters to send to the server
     * @return  The page that was loaded.
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      "throwExceptionOnFailingStatusCode" is set to true (see {@link
     *      #setThrowExceptionOnFailingStatusCode(boolean)})
     */
    public Page getPage(
            final WebWindow webWindow,
            final URL url,
            final SubmitMethod method,
            final List parameters )
        throws
            IOException,
            FailingHttpStatusCodeException {
        return getPage(webWindow, url, method, parameters, getThrowExceptionOnFailingStatusCode());
    }


    /**
     * Return a page.
     *
     * @param  webWindow The window from which the load is initiated.  If target
     * is not specified then this will also be the window into which the new page
     * is loaded.
     * @param  url The url of the server
     * @param  target The name of the window where the page will be loaded.
     * @param  method The submit method. Ie Submit.GET or SubmitMethod.POST
     * @param  parameters A list of {@link
     *      com.gargoylesoftware.htmlunit.KeyValuePair KeyValuePair}'s that
     *      contain the parameters to send to the server
     * @return  The page that was loaded.
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      "throwExceptionOnFailingStatusCode" is set to true (see {@link
     *      #setThrowExceptionOnFailingStatusCode(boolean)})
     */
    public Page getPage(
            final WebWindow webWindow,
            final URL url,
            final String target,
            final SubmitMethod method,
            final List parameters )
        throws
            IOException,
            FailingHttpStatusCodeException {
        return getPage(openTargetWindow( webWindow, target, "_self" ), url,
            method, parameters, getThrowExceptionOnFailingStatusCode());
    }


    /**
     * Return a page.
     *
     * @param  webWindow The window that the new page will be loaded into.
     * @param  url The url of the server
     * @param  encType Encoding type of the form when done as a POST
     * @param  method The submit method. Ie Submit.GET or SubmitMethod.POST
     * @param  parameters A list of {@link
     *      com.gargoylesoftware.htmlunit.KeyValuePair KeyValuePair}'s that
     *      contain the parameters to send to the server
     * @return  The page that was loaded.
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      "throwExceptionOnFailingStatusCode" is set to true (see {@link
     *      #setThrowExceptionOnFailingStatusCode(boolean)})
     */
    public Page getPage(
            final WebWindow webWindow,
            final URL url,
            final FormEncodingType encType,
            final SubmitMethod method,
            final List parameters )
        throws
            IOException,
            FailingHttpStatusCodeException {
        return getPage(webWindow, url, encType, method, parameters, getThrowExceptionOnFailingStatusCode());
    }


    /**
     * Return a page.
     *
     * @param  webWindow The window that the new page will be loaded into.
     * @param  url The url of the server
     * @param  target The name of the window where the page will be loaded.
     * @param  encType Encoding type of the form when done as a POST
     * @param  method The submit method. Ie Submit.GET or SubmitMethod.POST
     * @param  parameters A list of {@link
     *      com.gargoylesoftware.htmlunit.KeyValuePair KeyValuePair}'s that
     *      contain the parameters to send to the server
     * @return  The page that was loaded.
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      "throwExceptionOnFailingStatusCode" is set to true (see {@link
     *      #setThrowExceptionOnFailingStatusCode(boolean)})
     */
    public Page getPage(
            final WebWindow webWindow,
            final URL url,
            final String target,
            final FormEncodingType encType,
            final SubmitMethod method,
            final List parameters )
        throws
            IOException,
            FailingHttpStatusCodeException {
        return getPage(openTargetWindow( webWindow, target, "_self" ),
            url, encType, method, parameters,
            getThrowExceptionOnFailingStatusCode());
    }


    /**
     * Return a page.
     *
     * @param  webWindow The window that the new page will be loaded into.
     * @param  url The url of the server
     * @param  method The submit method. Ie Submit.GET or SubmitMethod.POST
     * @param  parameters A list of {@link
     *      com.gargoylesoftware.htmlunit.KeyValuePair KeyValuePair}'s that
     *      contain the parameters to send to the server
     * @param throwExceptionOnFailingStatusCode true if this method should throw
     * an exception whenever a failing status code is received.
     * @return  The page that was loaded.
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the variable
     *      "throwExceptionOnFailingStatusCode" is set to true
     */
    public Page getPage(
            final WebWindow webWindow,
            final URL url,
            final SubmitMethod method,
            final List parameters,
            final boolean throwExceptionOnFailingStatusCode )
        throws
            IOException,
            FailingHttpStatusCodeException {

        return this.getPage(
            webWindow, url, FormEncodingType.URL_ENCODED, method,
            parameters, throwExceptionOnFailingStatusCode);
    }

    /**
     *  Send a request to a server and return a Page that represents the
     *  response from the server. This page will be used to populate this frame.<p>
     *
     * The type of Page will depend on the content type of the http response. <p />
     *
     *  <table>
     *    <tr>
     *      <th>Content type</th>
     *      <th>Type of page</th>
     *    </tr>
     *    <tr>
     *      <td>"text/html"</td>
     *      <td>{@link com.gargoylesoftware.htmlunit.html.HtmlPage}</td>
     *    </tr>
     *    <tr>
     *      <td>"text/xhtml"</td>
     *      <td>{@link com.gargoylesoftware.htmlunit.html.HtmlPage}</td>
     *    </tr>
     *    <tr>
     *      <td>"text/*"</td>
     *      <td>{@link com.gargoylesoftware.htmlunit.TextPage}</td>
     *    </tr>
     *    <tr>
     *      <td>Anything else</td>
     *      <td>{@link com.gargoylesoftware.htmlunit.UnexpectedPage}</td>
     *    </tr>
     *  </table>
     *
     * @param  webWindow The window that the new page will be loaded into.
     * @param  url The url of the server
     * @param  encType Encoding type of the form when done as a POST
     * @param  method The submit method. Ie Submit.GET or SubmitMethod.POST
     * @param  parameters A list of {@link
     *      com.gargoylesoftware.htmlunit.KeyValuePair KeyValuePair}'s that
     *      contain the parameters to send to the server
     * @param throwExceptionOnFailingStatusCode true if this method should throw
     * an exception whenever a failing status code is received.
     * @return  The page that was loaded.
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the variable
     *      "throwExceptionOnFailingStatusCode" is set to true
     */
    public Page getPage(
            final WebWindow webWindow,
            final URL url,
            final FormEncodingType encType,
            final SubmitMethod method,
            final List parameters,
            final boolean throwExceptionOnFailingStatusCode )
        throws
            IOException,
            FailingHttpStatusCodeException {

        final String protocol = url.getProtocol();
        final WebResponse webResponse;
        if( protocol.equals("javascript") ) {
            webResponse = makeWebResponseForJavaScriptUrl(webWindow, url);
        }
        else if (protocol.equals("about")) {
            webResponse = makeWebResponseForAboutUrl(url);
        }
        else if (protocol.equals("file")) {
            webResponse = makeWebResponseForFileUrl(url);
        }
        else {
            webResponse = loadWebResponse( url, encType, method, parameters );
        }
        final String contentType = webResponse.getContentType();
        final int statusCode = webResponse.getStatusCode();

        final boolean wasResponseSuccessful = ( statusCode >= 200 && statusCode < 300 );

        if( printContentOnFailingStatusCode_ == true && wasResponseSuccessful == false ) {
            getLog().info( "statusCode=[" + statusCode
                + "] contentType=[" + contentType + "]" );
            getLog().info( webResponse.getContentAsString() );
        }

        loadWebResponseInto(webResponse, webWindow);

        if( throwExceptionOnFailingStatusCode == true && wasResponseSuccessful == false ) {
            throw new FailingHttpStatusCodeException( statusCode, webResponse.getStatusMessage() );
        }

        return webWindow.getEnclosedPage();
    }

    /**
     * Use the specified WebResponse to create a Page object which will then
     * get inserted into the WebWindow.  All initialization and event notification
     * will be handled here.
     *
     * @param webResponse The response that will be used to create the new page.
     * @param webWindow The window that the new page will be placed within.
     * @throws IOException If an IO error occurs.
     * @return The newly created page.
     */
    public Page loadWebResponseInto(
            final WebResponse webResponse, final WebWindow webWindow )
        throws
            IOException {

        Assert.notNull("webResponse", webResponse);
        Assert.notNull("webWindow", webWindow);

        final Page oldPage = webWindow.getEnclosedPage();
        if (oldPage != null) {
            // Remove the old windows before create new ones.
            oldPage.cleanUp();
        }

        final Page newPage = pageCreator_.createPage( webResponse, webWindow );

        if (! firstWindowStack_.empty() && firstWindowStack_.peek() == null) {
            firstWindowStack_.pop();
            firstWindowStack_.push(webWindow);
        }

        newPage.initialize();

        fireWindowContentChanged( new WebWindowEvent(webWindow, WebWindowEvent.CHANGE, oldPage, newPage) );
        return newPage;
    }


    /**
     *  Specify whether or not the content of the resulting document will be
     *  printed to the console in the event of a failing response code.
     *  Successful response codes are in the range 200-299. The default is true.
     *
     * @param  enabled True to enable this feature
     */
    public void setPrintContentOnFailingStatusCode( final boolean enabled ) {
        printContentOnFailingStatusCode_ = enabled;
    }


    /**
     *  Return true if the content of the resulting document will be printed to
     *  the console in the event of a failing response code.
     *
     * @return  See above
     * @see  #setPrintContentOnFailingStatusCode
     */
    public boolean getPrintContentOnFailingStatusCode() {
        return printContentOnFailingStatusCode_;
    }


    /**
     *  Specify whether or not an exception will be thrown in the event of a
     *  failing status code. Successful status codes are in the range 200-299.
     *  The default is true.
     *
     * @param  enabled True to enable this feature
     */
    public void setThrowExceptionOnFailingStatusCode( final boolean enabled ) {
        throwExceptionOnFailingStatusCode_ = enabled;
    }


    /**
     *  Return true if an exception will be thrown in the event of a failing
     *  response code.
     *
     * @return  See above
     * @see  #setThrowExceptionOnFailingStatusCode
     */
    public boolean getThrowExceptionOnFailingStatusCode() {
        return throwExceptionOnFailingStatusCode_;
    }


    /**
     *  Set a header which will be sent up on EVERY request from this client.
     *
     * @param  name The name of the header
     * @param  value The value of the header
     */
    public void addRequestHeader( final String name, final String value ) {
        requestHeaders_.put( name, value );
    }


    /**
     *  Remove a header
     *
     * @param  name Name of the header
     * @see  #addRequestHeader
     */
    public void removeRequestHeader( final String name ) {
        requestHeaders_.remove( name );
    }


    /**
     *  Set the credential provider that will provide the authentication
     *  information when trying to access protected information on a web server.
     *  This information is required when the server is using basic
     *  authentication to protect resources. <p>
     *
     *  If you want to return the same user id/password combination for all
     *  realms then use a {@link com.gargoylesoftware.htmlunit.SimpleCredentialProvider}
     *
     * @param  credentialProvider The credential provider
     */
    public void setCredentialProvider( final CredentialProvider credentialProvider ) {
        Assert.notNull( "credentialProvider", credentialProvider );
        credentialProvider_ = credentialProvider;
    }


    /**
     *  Return the credential provider
     *
     * @return  The credential provider
     */
    public CredentialProvider getCredentialProvider() {
        return credentialProvider_;
    }


    /**
     *  Throw an exception with the specified message. If junit is found in the
     *  classpath then a junit.framework.AssertionFailedError will be thrown
     *  (the same behaviour as calling fail() in junit). If junit is not found
     *  then an IllegalStateException will be thrown instead of the
     *  AssertionFailedError. <p>
     *
     *  Override this to provide custom behaviour.
     *
     * @param  message The failure message
     */
    public void assertionFailed( final String message ) {
        try {
            final Class clazz = junit.framework.AssertionFailedError.class;
            final Constructor constructor = clazz.getConstructor( new Class[]{String.class} );
            final Error error = ( Error )constructor.newInstance( new Object[]{message} );
            throw error;
        }
        catch( final Exception e ) {
            throw new IllegalStateException( message );
        }
    }


    private class DenyAllCredentialProvider implements CredentialProvider {
        /** @inheritDoc CredentialProvider#getCredentialsFor(String,int,String) */
        public KeyValuePair getCredentialsFor(
            final String server, final int port, final String realm ) {

            return null;
        }
    }


    /**
     *  This method is intended for testing only - use at your own risk.
     *
     * @return  the current script engine
     */
    public ScriptEngine getScriptEngine() {
        if( javaScriptEnabled_ == true ) {
            return scriptEngine_;
        }
        else {
            return null;
        }
    }


    /**
     *  This method is intended for testing only - use at your own risk.
     *
     * @param engine  The new script engine to use.
     */
    public void setScriptEngine( final ScriptEngine engine ) {
        scriptEngine_ = engine;
    }


    /**
     * Enable/disable javascript support.  By default, this property is enabled.
     *
     * @param isEnabled true to enable javascript support.
     */
    public void setJavaScriptEnabled( final boolean isEnabled ) {
        javaScriptEnabled_ = isEnabled;
    }


    /**
     * Return true if javascript is enabled and the script engine was loaded successfully.
     *
     * @return true if javascript is enabled.
     */
    public boolean isJavaScriptEnabled() {
        return javaScriptEnabled_ && scriptEngine_ != null;
    }


    /**
     * Set the alert handler for this webclient.
     * @param alertHandler The new alerthandler or null if none is specified.
     */
    public void setAlertHandler( final AlertHandler alertHandler ) {
        alertHandler_ = alertHandler;
    }


    /**
     * Return the alert handler for this webclient.
     * @return the alert handler or null if one hasn't been set.
     */
    public AlertHandler getAlertHandler() {
        return alertHandler_;
    }


    /**
     * Set the handler that will be executed when the javascript method Window.confirm() is called.
     * @param handler The new handler or null if no handler is to be used.
     */
    public void setConfirmHandler( final ConfirmHandler handler ) {
        confirmHandler_ = handler;
    }


    /**
     * Return the confirm handler.
     * @return the confirm handler or null if one hasn't been set.
     */
    public ConfirmHandler getConfirmHandler() {
        return confirmHandler_;
    }


    /**
     * Set the handler that will be executed when the javascript method Window.prompt() is called.
     * @param handler The new handler or null if no handler is to be used.
     */
    public void setPromptHandler( final PromptHandler handler ) {
        promptHandler_ = handler;
    }


    /**
     * Return the prompt handler.
     * @return the prompt handler or null if one hasn't been set.
     */
    public PromptHandler getPromptHandler() {
        return promptHandler_;
    }


    /**
     * Set the status handler for this webclient.
     * @param statusHandler The new alerthandler or null if none is specified.
     */
    public void setStatusHandler( final StatusHandler statusHandler ) {
        statusHandler_ = statusHandler;
    }


    /**
     * Return the status handler for this webclient.
     * @return the status handler or null if one hasn't been set.
     */
    public StatusHandler getStatusHandler() {
        return statusHandler_;
    }

    /**
     * Return the current browser version
     * @return the current browser version.
     */
    public BrowserVersion getBrowserVersion() {
        return browserVersion_;
    }


    /**
     * Return the "current" window for this client.  This is the window that will be used
     * when getPage() is called without specifying a window.
     * @return The current window.
     */
    public WebWindow getCurrentWindow() {
        return currentWindow_;
    }


    /**
     * Set the current window for this client.  This is the window that will be used when
     * getPage() is called without specifying a window.
     * @param window The new window.
     */
    public void setCurrentWindow( final WebWindow window ) {
        Assert.notNull("window", window);
        currentWindow_ = window;
    }


    /**
     * Return the "first" window for this client.  This is the first window
     * opened since pushClearFirstWindow() was last called.
     * @return The first window.
     */
    public WebWindow popFirstWindow() {
        return (WebWindow)firstWindowStack_.pop();
    }


    /**
     * Clear the first window for this client.
     */
    public void pushClearFirstWindow() {
        firstWindowStack_.push(null);
    }


    /**
     * Add a listener for WebWindowEvent's.  All events from all windows associated with this
     * client will be sent to the specified listener.
     * @param listener A listener.
     */
    public void addWebWindowListener( final WebWindowListener listener ) {
        Assert.notNull("listener", listener);
        webWindowListeners_.add(listener);
    }


    /**
     * Remove a listener for WebWindowEvent's.
     * @param listener A listener.
     */
    public void removeWebWindowListener( final WebWindowListener listener ) {
        Assert.notNull("listener", listener);
        webWindowListeners_.remove(listener);
    }


    private void fireWindowContentChanged( final WebWindowEvent event ) {
        final Iterator iterator = new ArrayList(webWindowListeners_).iterator();
        while( iterator.hasNext() ) {
            final WebWindowListener listener = (WebWindowListener)iterator.next();
            listener.webWindowContentChanged(event);
        }
    }


    private void fireWindowOpened( final WebWindowEvent event ) {
        final Iterator iterator = new ArrayList(webWindowListeners_).iterator();
        while( iterator.hasNext() ) {
            final WebWindowListener listener = (WebWindowListener)iterator.next();
            listener.webWindowOpened(event);
        }
    }


    private void fireWindowClosed( final WebWindowEvent event ) {
        final Iterator iterator = new ArrayList(webWindowListeners_).iterator();
        while( iterator.hasNext() ) {
            final WebWindowListener listener = (WebWindowListener)iterator.next();
            listener.webWindowClosed(event);
        }
    }

    /**
     * Open a new window with the specified name.  If the url is non-null then attempt to load
     * a page from that location and put it in the new window.
     *
     * @param url The url to load content from or null if no content is to be loaded.
     * @param windowName The name of the new window
     * @return The new window.
     */
    public WebWindow openWindow( final URL url, final String windowName ) {
        Assert.notNull("windowName", windowName);
        return openWindow( url, windowName, getCurrentWindow() );
    }


    /**
     * Open a new window with the specified name.  If the url is non-null then attempt to load
     * a page from that location and put it in the new window.
     *
     * @param url The url to load content from or null if no content is to be loaded.
     * @param windowName The name of the new window
     * @param opener The web window that is calling openWindow
     * @return The new window.
     */
    public WebWindow openWindow( final URL url, String windowName, final WebWindow opener ) {
        WebWindow window = openTargetWindow( opener, windowName, "_blank" );
        if( url != null ) {
            try {
                getPage(window, url, SubmitMethod.GET, Collections.EMPTY_LIST);
            }
            catch( final IOException e ) {
                getLog().error("Error when loading content into window", e);
            }
        }
        return window;
    }


    /**
     * Open the window with the specified name.  The name may be a special
     * target name of _self, _parent, _top, or _blank.  An empty or null
     * name is set to the default.  The special target names are relative to
     * the opener window.
     *
     * @param opener The web window that is calling openWindow
     * @param windowName The name of the new window
     * @param defaultName The default target if no name is given
     * @return The new window.
     */
    private WebWindow openTargetWindow( final WebWindow opener,
        String windowName, String defaultName ) {
        Assert.notNull("opener", opener);
        Assert.notNull("defaultName", defaultName);
        if( windowName == null || windowName.length() == 0 ) {
            windowName = defaultName;
        }

        WebWindow window = null;
        if( windowName.equals("_self") ) {
            window = opener;
            windowName = "";
        }
        else if( windowName.equals("_parent") ) {
            window = opener.getParentWindow();
            windowName = "";
        }
        else if( windowName.equals("_top") ) {
            window = opener.getTopWindow();
            windowName = "";
        }
        else if( windowName.equals("_blank") ) {
            // Leave window null to create a new window.
            windowName = "";
        }
        else if( windowName.length() != 0 ) {
            try {
                window = getWebWindowByName(windowName);
            }
            catch( final WebWindowNotFoundException e ) {
                // Fall through - a new window will be created below
            }
        }

        if( window == null ) {
            window = new TopLevelWindow(windowName, this);
            fireWindowOpened( new WebWindowEvent(window, WebWindowEvent.OPEN, null, null) );
        }

        if( window instanceof TopLevelWindow && window != opener.getTopWindow() ) {
            ((TopLevelWindow)window).setOpener(opener);
        }

        return window;
    }


    /**
     * Set whether or not redirections will be followed automatically on receipt of
     * a redirect status code from the server.
     * @param enabled true to enable automatic redirection.
     */
    public void setRedirectEnabled( final boolean enabled ) {
        isRedirectEnabled_ = enabled;
    }


    /**
     * Return whether or not redirections will be followed automatically on receipt of
     * a redirect status code from the server.
     * @return true if automatic redirection is enabled.
     */
    public boolean isRedirectEnabled() {
        return isRedirectEnabled_;
    }


    /**
     * Set the object that will be used to create pages.  Set this if you want
     * to customize the type of page that is returned for a given content type.
     *
     * @param pageCreator The new page creator
     */
    public void setPageCreator( final PageCreator pageCreator ) {
        Assert.notNull("pageCreator", pageCreator);
        pageCreator_ = pageCreator;
    }


    /**
     * Return the current page creator.
     *
     * @return the page creator
     */
    public PageCreator getPageCreator() {
        return pageCreator_;
    }


    /**
     * Return the first {@link WebWindow} that matches the specified name.
     *
     * @param name The name to search for.
     * @return The {@link WebWindow} with the specified name
     * @throws WebWindowNotFoundException If the {@link WebWindow} can't be found.
     */
    public WebWindow getWebWindowByName( final String name ) throws WebWindowNotFoundException {
        Assert.notNull("name", name);

        final Iterator iterator = webWindows_.iterator();
        while( iterator.hasNext() ) {
            final WebWindow webWindow = (WebWindow)iterator.next();
            if( webWindow.getName().equals(name) ) {
                return webWindow;
            }
        }

        throw new WebWindowNotFoundException(name);
    }


    /**
     * Add a new web window to the list of available windows.  This is intended as
     * an internal api only and may change without notice..
     * @param webWindow The new WebWindow
     */
    public void registerWebWindow( final WebWindow webWindow ) {
        Assert.notNull("webWindow", webWindow);
        webWindows_.add(webWindow);
    }


    /**
     * Remove a web window to the list of available windows.  This is intended as
     * an internal api only and may change without notice..
     * @param webWindow The WebWindow to remove
     */
    public void deregisterWebWindow( final WebWindow webWindow ) {
        Assert.notNull("webWindow", webWindow);
        webWindows_.remove(webWindow);

        if( currentWindow_ == webWindow ) {
            if( webWindows_.size() == 0 ) {
                // Create a new one - we always have to have at least one window.
                currentWindow_ = new TopLevelWindow("", this);
            }
            else {
                currentWindow_ = (WebWindow)webWindows_.get(0);
            }
        }
        fireWindowClosed(new WebWindowEvent(webWindow, WebWindowEvent.CLOSE, webWindow.getEnclosedPage(), null));
    }


    /**
     * Return the log object for this web client
     * @return The log object
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }


    private static URL makeUrl( final String urlString ) throws MalformedURLException {
        Assert.notNull("urlString", urlString);

        if( TextUtil.startsWithIgnoreCase(urlString, "javascript:") ) {
            return new URL(null, urlString, JavaScriptUrlStreamHandler_);
        }
        else if (TextUtil.startsWithIgnoreCase(urlString,"about:")){
            return new URL(null, urlString, AboutUrlStreamHandler_);
        }
        else {
            return new URL(urlString);
        }
    }


    /**
     * Expand a relative url relative to the specified base.  In most situations this
     * is the same as <code>new URL(baseUrl, relativeUrl)</code> but there are some cases
     * that URL doesn't handle correctly.
     *
     * @param  baseUrl The base url
     * @param  relativeUrl The relative url
     * @return  See above
     * @exception  MalformedURLException If an error occurred when creating a
     *      URL object
     */
    public static URL expandUrl( final URL baseUrl, String relativeUrl )
        throws MalformedURLException {

        // Was a protocol specified?
        final int colonIndex = relativeUrl.indexOf(":");
        if( colonIndex != -1 ) {
            boolean isProtocolSpecified = true;
            for( int i=0; i<colonIndex; i++ ) {
                if( Character.isLetter(relativeUrl.charAt(i)) == false ) {
                    isProtocolSpecified = false;
                    break;
                }
            }
            if( isProtocolSpecified == true ) {
                return makeUrl( relativeUrl );
            }
        }

        if( relativeUrl.startsWith("//") ) {
            return makeUrl(baseUrl.getProtocol()+":"+relativeUrl);
        }

        final List tokens = new ArrayList();
        final String stringToTokenize;
        if( relativeUrl.length() == 0 ) {
            stringToTokenize = baseUrl.getPath();
        }
        else if( relativeUrl.startsWith("/") ) {
            stringToTokenize = relativeUrl;
        }
        else {
            String path = baseUrl.getPath();
            if( path.endsWith("/") == false ) {
                path += "/..";
            }
            stringToTokenize = path+"/"+relativeUrl;
        }
        final StringTokenizer tokenizer = new StringTokenizer(stringToTokenize,"/");
        while( tokenizer.hasMoreTokens() ) {
            tokens.add( tokenizer.nextToken() );
        }

        for( int i=0; i<tokens.size(); i++ ) {
            String oneToken = (String)tokens.get(i);
            if( oneToken.length() == 0 || oneToken.equals(".") ) {
                tokens.remove(i--);
            }
            else if( oneToken.equals("..") ) {
                tokens.remove(i--);
                if( i >= 0 ) {
                    tokens.remove(i--);
                }
            }
        }

        final StringBuffer buffer = new StringBuffer();
        buffer.append( baseUrl.getProtocol() );
        buffer.append( "://" );
        buffer.append( baseUrl.getHost() );
        final int port = baseUrl.getPort();
        if( port != -1 ) {
            buffer.append( ":" );
            buffer.append( port );
        }

        final Iterator iterator = tokens.iterator();
        while( iterator.hasNext() ) {
            buffer.append("/");
            buffer.append(iterator.next());
        }

        if( tokens.isEmpty() || stringToTokenize.endsWith("/") ) {
            buffer.append("/");
        }

        String newUrlString = buffer.toString();
        final int lastPoundSignIndex = newUrlString.lastIndexOf("#");
        if( lastPoundSignIndex != -1 ) {
            newUrlString = newUrlString.substring(0,lastPoundSignIndex);
        }
        return makeUrl( newUrlString );
    }

    private WebResponse makeWebResponseForAboutUrl(final URL url) {
        if (!url.toExternalForm().substring("about:".length()).equalsIgnoreCase("blank")){
            throw new IllegalArgumentException(
                url.toExternalForm()+"is not supported, only about:blank is supported now.");
        }
        return WEB_RESPONSE_FOR_ABOUT_BLANK;
    }

    /**
     * Builds a WebResponse for a file url.
     * This first implementation is basic. 
     * It assumes that the file contains an html page encoded with computer's default 
     * encoding.
     * @param url The file url
     * @return The web response
     * @throws IOException If an IO problem occurs
     */
    private WebResponse makeWebResponseForFileUrl(final URL url) throws IOException {
        final File file = FileUtils.toFile(url);
        
        // take default encoding of the computer (in J2SE5 it's easier but...)
        final String encoding = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();
        final String str = FileUtils.readFileToString(file, encoding);
        
        return new WebResponse() {
            public int getStatusCode() {
                return 200;
            }
            public String getStatusMessage() {
                return "OK";
            }
            public String getContentType() {
                return "text/html";
            }
            public String getContentAsString() {
                return str;
            }
            public InputStream getContentAsStream() {
                return TextUtil.toInputStream(str);
            }
            public URL getUrl() {
                return url;
            }
            public String getResponseHeaderValue(final String key) {
                return "";
            }
            public long getLoadTimeInMilliSeconds() {
                return 0;
            }
            public byte[] getResponseBody() {
                return str.getBytes();
            }
            public String getContentCharSet() {
                return encoding;
            }
        };
    }

    private WebResponse makeWebResponseForJavaScriptUrl( final WebWindow webWindow, final URL url ) {
        if( webWindow instanceof HtmlElement == false ) {
            throw new IllegalArgumentException(
                "javascript urls can only be used to load content into frames and iframes");
        }

        final HtmlPage enclosingPage = ((HtmlElement)webWindow).getPage();
        final ScriptResult scriptResult = enclosingPage.executeJavaScriptIfPossible(
            url.toExternalForm(), "javascript url", false, null );

        final String contentString = scriptResult.getJavaScriptResult().toString();
        return new WebResponse() {
            public int getStatusCode() { return 200; }
            public String getStatusMessage() { return "OK"; }
            public String getContentType() { return "text/html"; }
            public String getContentAsString() { return contentString; }
            public InputStream getContentAsStream(){ return TextUtil.toInputStream(contentString); }
            public URL getUrl() { return url; }
            public String getResponseHeaderValue( final String key ) { return ""; }
            public long getLoadTimeInMilliSeconds() { return 0; }
            public byte[] getResponseBody() {
                try {
                    /*
                     * this method must return raw bytes.
                     * without encoding, getBytes use locale encoding.
                     */
                    return contentString.getBytes("ISO-8859-1");
                }
                catch( final UnsupportedEncodingException e ){
                    return null;
                }
            }
            public String getContentCharSet() { return "ISO-8859-1"; }
        };
    }

    /**
     * Load a {@link WebResponse} from the server
     * @param url The url to load the response from.
     * @param method The {@link SubmitMethod} to use
     * @param parameters Any parameters that are being passed into the request
     * @throws IOException if an IO problem occurs
     * @return The WebResponse
     */
    public final WebResponse loadWebResponse(
            final URL url, final SubmitMethod method, final List parameters)
        throws
            IOException {
        return this.loadWebResponse(url, FormEncodingType.URL_ENCODED, method, parameters);
    }

    /**
     * Load a {@link WebResponse} from the server
     * @param url The url to load the response from.
     * @param encType Encoding type of the form when done as a POST
     * @param method The {@link SubmitMethod} to use
     * @param parameters Any parameters that are being passed into the request
     * @throws IOException if an IO problem occurs
     * @return The WebResponse
     */
    public final WebResponse loadWebResponse(
            final URL url, final FormEncodingType encType, final SubmitMethod method, final List parameters)
        throws
            IOException {

        Assert.notNull("url", url);
        Assert.notNull("method", method);
        Assert.notNull("parameters", parameters);

        final WebResponse webResponse
            = getWebConnection().getResponse( url, encType, method, parameters, requestHeaders_ );
        final int statusCode = webResponse.getStatusCode();

        if( statusCode >= 301 && statusCode <=307 && isRedirectEnabled() ) {
            URL newUrl = null;
            String locationString = null;
            try {
                locationString = webResponse.getResponseHeaderValue("Location");
                if( locationString != null ) {
                    // HttpClient sometimes returns a delimited list of values where the delimiter is a comma.
                    // We'll take a guess and go with the first value.
                    int indexOfComma = locationString.indexOf(',');
                    if( indexOfComma >= 0) {
                       newUrl = expandUrl( url, locationString.substring(0, indexOfComma));
                    }
                    else {
                       newUrl = expandUrl( url, locationString);
                    }
                }
            }
            catch( final MalformedURLException e ) {
                getLog().warn("Got a redirect status code ["+statusCode+" "
                    +webResponse.getStatusMessage()
                    +"] but the location is not a valid url ["+locationString+"]");
            }
            getLog().debug("Got a redirect status code ["+statusCode
                +"] new location=["+locationString+"]");
            if( webResponse.getUrl().toExternalForm().equals(locationString) ) {
                getLog().warn("Got a redirect but the location is the same as the page we just loaded ["
                    +locationString+"]");
            }
            else if( newUrl == null ) {
                // We don't have a new location to go to so just fall through
            }
            else if( ( statusCode == 301 || statusCode == 307 )
                && method.equals(SubmitMethod.GET) ) {

                return loadWebResponse( newUrl, SubmitMethod.GET, parameters );
            }
            else if( statusCode == 302 || statusCode == 303 ) {
                return loadWebResponse( newUrl, SubmitMethod.GET, Collections.EMPTY_LIST );
            }
        }

        return webResponse;
    }


    /**
     * Remove the focus to the specified component.  This will trigger any relevant javascript
     * event handlers.
     *
     * @param oldElement The element that will lose the focus.
     * @see #moveFocusToElement(FocusableElement)
     * @return true if the focus changed and a new page was not loaded
     */
    public boolean moveFocusFromElement( final FocusableElement oldElement ) {
        if (oldElement != null && elementWithFocus_ == oldElement) {
            final String onBlurHandler = oldElement.getAttributeValue("onblur");
            if( onBlurHandler.length() != 0 ) {
                final HtmlPage currentPage = oldElement.getPage();
                final Page newPage = currentPage.executeJavaScriptIfPossible(
                    onBlurHandler, "OnBlur handler", true, oldElement).getNewPage();

                // If a page reload happened as a result of the focus change then obviously this
                // element will not have the focus because its page has gone away.
                if( currentPage != newPage ) {
                    elementWithFocus_ = null;
                    return false;
                }
            }
            elementWithFocus_ = null;
            return true;
        }
        return false;
    }


    /**
     * Move the focus to the specified component.  This will trigger any relevant javascript
     * event handlers.
     *
     * @param newElement The element that will recieve the focus.
     * @return true if the specified element now has the focus.
     * @see #getElementWithFocus()
     * @see HtmlPage#tabToNextElement()
     * @see HtmlPage#tabToPreviousElement()
     * @see HtmlPage#pressAccessKey(char)
     * @see HtmlPage#assertAllTabIndexAttributesSet()
     */
    public boolean moveFocusToElement( final FocusableElement newElement ) {

        if( newElement == null ) {
            throw new IllegalArgumentException("Cannot move focus to null");
        }

        if( elementWithFocus_ == newElement ) {
            // nothing to do
            return true;
        }

        if( elementWithFocus_ != null ) {
            elementWithFocus_.blur();
        }

        final String onFocusHandler = newElement.getAttributeValue("onfocus");
        if( onFocusHandler.length() != 0 ) {
            final HtmlPage currentPage = newElement.getPage();
            final Page newPage = currentPage.executeJavaScriptIfPossible(
                onFocusHandler, "OnFocus handler", true, newElement).getNewPage();

            // If a page reload happened as a result of the focus change then obviously this
            // element will not have the focus because its page has gone away.
            if( currentPage != newPage ) {
                elementWithFocus_ = null;
                return false;
            }
        }

        elementWithFocus_ = newElement;
        return true;
    }


    /**
     * Return the element with the focus or null if no elements have the focus.
     *
     * @return The element with focus or null.
     * @see #moveFocusToElement(FocusableElement)
     */
    public FocusableElement getElementWithFocus() {
        return elementWithFocus_;
    }

    /**
     * Return an immutable list of open web windows.
     * @return The web windows
     */
    public List getWebWindows() {
        return Collections.unmodifiableList(webWindows_);
    }
    
    /**
     * Set the handler to be used whenever a refresh is triggered.  Refer
     * to the documentation for {@link RefreshHandler} for more details.
     * @param handler The new handler
     */
    public void setRefreshHandler( final RefreshHandler handler ) {
        if( handler == null ) {
            refreshHandler_ = new DefaultRefreshHandler();
        }
        else {
            refreshHandler_ = handler;
        }
    }
    
    /**
     * Return the current refresh handler or null if one has not been set.
     * @return The current RefreshHandler or null
     */
    public RefreshHandler getRefreshHandler() {
        return refreshHandler_;
    }
    
    /**
     * Set the script pre processor for this webclient.
     * @param scriptPreProcessor The new preprocessor or null if none is specified
     */
    public void setScriptPreProcessor( final ScriptPreProcessor scriptPreProcessor ) {
        scriptPreProcessor_ = scriptPreProcessor;
    }


    /**
     * Return the script pre processor for this webclient.
     * @return the pre processor or null of one hasn't been set.
     */
    public ScriptPreProcessor getScriptPreProcessor() {
        return scriptPreProcessor_;
    }

    /**
     * Set the active X object map for this webclient. The <code>Map</code> is used to map the
     * string passed into the <code>ActiveXObject</code> constructor to a java class name. Therefore
     * you can emulate <code>ActiveXObject</code>s in a web page's javascript by mapping the object
     * name to a java class to emulate the active X object.
     * @param activeXObjectMap The new preprocessor or null if none is specified
     */
    public void setActiveXObjectMap( final Map activeXObjectMap ) {
        activeXObjectMap_ = activeXObjectMap;
    }


    /**
     * Return the active X object map for this webclient.
     * @return the active X object map.
     */
    public Map getActiveXObjectMap() {
        return activeXObjectMap_;
    }

    /**
     * Set the flag on the HtmlParse to log html errors to standard error
     * @param validateFlag The boolean flag to enable or disable parsing errors
     */
    public static void setValidateHtml(boolean validateFlag) {
        HTMLParser.setValidateHtml(validateFlag);
    }
}

