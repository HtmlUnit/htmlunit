/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  An object that represents a web browser
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
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

    private AlertHandler   alertHandler_;
    private ConfirmHandler confirmHandler_;
    private PromptHandler  promptHandler_;

    private BrowserVersion browserVersion_ = BrowserVersion.getDefault();
    private boolean isRedirectEnabled_ = true;
    private PageCreator pageCreator_ = new DefaultPageCreator();

    private final Set webWindowListeners_ = new HashSet(5);
    private final List webWindows_ = new ArrayList();

    private WebWindow currentWindow_ = new TopLevelWindow("", this);


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
        assertNotNull("browserVersion", browserVersion);

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
        assertNotNull("browserVersion", browserVersion);
        assertNotNull( "proxyHost", proxyHost );

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
     *  Return the object that will resolve all url requests
     *
     * @return  The UrlResolver
     */
    private WebConnection getWebConnection() {
        if( webConnection_ == null ) {
            if( proxyHost_ == null ) {
                webConnection_ = new WebConnection( this );
            }
            else {
                webConnection_ = new WebConnection( this, proxyHost_, proxyPort_ );
            }
        }

        return webConnection_;
    }


    /**
     *  Set the object that will resolve all url requests <p />
     *
     *  THIS METHOD IS FOR TESTING PURPOSES ONLY - DO NOT USE
     *
     * @param  webConnection The new web connection
     */
    public void setWebConnection( final WebConnection webConnection ) {
        assertNotNull( "webConnection", webConnection );
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
     * @deprecated Use {@link #getPage(WebWindow,URL,SubmitMethod,List,boolean)} instead.
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
     * Return a page.  This variation has been deprecated.
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
     * @deprecated Use {@link #getPage(WebWindow,URL,SubmitMethod,List,boolean)} instead
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
     * @param  method The submit method. Ie Submit.GET or SubmitMethod.POST
     * @param  parameters A list of {@link
     *      com.gargoylesoftware.htmlunit.KeyValuePair KeyValuePair}'s that
     *      contain the parameters to send to the server
     * @param throwExceptionOnFailingStatusCode true if this method should throw
     * an exception whenever a failing status code is received.
     * @return  The page that was loaded.
     * @exception  IOException If an IO error occurs
     * @exception  FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the varaible
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

        final WebResponse webResponse = getWebConnection().getResponse( url, method, parameters );
        final String contentType = webResponse.getContentType();
        final int statusCode = webResponse.getStatusCode();

        final boolean wasResponseSuccessful = ( statusCode <= 200 && statusCode < 300 );

        if( statusCode >= 301 && statusCode <=307 && isRedirectEnabled() ) {
            URL newUrl = null;
            String locationString = null;
            try {
                locationString = webResponse.getResponseHeaderValue("Location");
                if( locationString != null ) {
                    newUrl = expandUrl( url, locationString );
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

                return getPage( webWindow, newUrl, SubmitMethod.GET, parameters );
            }
            else if( statusCode == 302 || statusCode == 303 ) {
                return getPage( webWindow, newUrl, SubmitMethod.GET, Collections.EMPTY_LIST );
            }
        }

        if( printContentOnFailingStatusCode_ == true && wasResponseSuccessful == false ) {
            getLog().info( "statusCode=[" + statusCode
                + "] contentType=[" + contentType + "]" );
            getLog().info( webResponse.getContentAsString() );
        }

        final Page oldPage = webWindow.getEnclosedPage();
        final Page newPage = pageCreator_.createPage( this, webResponse, webWindow );
        webWindow.setEnclosedPage(newPage);

        fireWindowContentChanged( new WebWindowEvent(webWindow, oldPage, newPage) );

        if( throwExceptionOnFailingStatusCode == true && wasResponseSuccessful == false ) {
            throw new FailingHttpStatusCodeException( statusCode, webResponse.getStatusMessage() );
        }

        return newPage;
    }


    private void assertNotNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException( description );
        }
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
        getWebConnection().getRequestHeaders().put( name, value );
    }


    /**
     *  Remove a header
     *
     * @param  name Name of the header
     * @see  #addRequestHeader
     */
    public void removeRequestHeader( final String name ) {
        getWebConnection().getRequestHeaders().remove( name );
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
        assertNotNull( "credentialProvider", credentialProvider );
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
     * This method is deprecated - do not use.  It doesn't make any sense to change the
     * browser that we are simulating part was through the use of the client.  There
     * are many spots, particularly in the javascript support, that initialize things
     * differently based on the browser version in use.  If that version was able to
     * change after initialization then unpredictable behaviour could result.
     *
     * @param browserVersion The new browser version.
     * @deprecated Pass the browser version into the constructor.  This method will be
     * removed before the final 1.1 release.
     */
    public void setBrowserVersion( final BrowserVersion browserVersion ) {
        assertNotNull("browserVersion", browserVersion);
        if( browserVersion != browserVersion_ ) {
            throw new IllegalStateException(
                "Not allowed to change the browser version after the client has been created");
        }
    }


    /**
     * Return the current browser version
     * @return the current browser version or null if the default version is being used.
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
     * getPage() is called without specifying a window
     * @param window The new window.
     */
    public void setCurrentWindow( final WebWindow window ) {
        assertNotNull("window", window);
        currentWindow_ = window;
    }


    /**
     * Add a listener for WebWindowEvent's.  All events from all windows associated with this
     * client will be sent to the specified listener.
     * @param listener A listener.
     */
    public void addWebWindowListener( final WebWindowListener listener ) {
        assertNotNull("listener", listener);
        webWindowListeners_.add(listener);
    }


    /**
     * Remove a listener for WebWindowEvent's.
     * @param listener A listener.
     */
    public void removeWebWindowListener( final WebWindowListener listener ) {
        assertNotNull("listener", listener);
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


    /**
     * Open a new window with the specified name.  If the url is non-null then attempt to load
     * a page from that location and put it in the new window.
     *
     * @param url The url to load content from or null if no content is to be loaded.
     * @param windowName The name of the new window
     * @return The new window.
     * @exception  IOException If an IO error occurs
     */
    public WebWindow openWindow( final URL url, final String windowName ) throws IOException {
        assertNotNull("windowName", windowName);

        // TODO: Check to see if there is already a window with the specified name.  If not, create
        // a new one

        final WebWindow window;
        if( windowName.equals("_self") ) {
            window = getCurrentWindow();
        }
        else {
            window = new TopLevelWindow(windowName, this);
            fireWindowOpened( new WebWindowEvent(window, null, null) );
        }

        if( url != null ) {
            getPage(window, url, SubmitMethod.GET, Collections.EMPTY_LIST);
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
        assertNotNull("pageCreator", pageCreator);
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
        assertNotNull("name", name);

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
        assertNotNull("webWindow", webWindow);
        webWindows_.add(webWindow);
    }


    /**
     * Remove a web window to the list of available windows.  This is intended as
     * an internal api only and may change without notice..
     * @param webWindow The WebWindow to remove
     */
    public void deregisterWebWindow( final WebWindow webWindow ) {
        assertNotNull("webWindow", webWindow);
        webWindows_.remove(webWindow);
    }


    /**
     * Return the log object for this web client
     * @return The log object
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
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
    public URL expandUrl( final URL baseUrl, final String relativeUrl )
        throws MalformedURLException {

        // Was a protocol specified?
        if( relativeUrl.indexOf( ":" ) != -1 ) {
            return new URL( relativeUrl );
        }

        if( relativeUrl.startsWith("//") ) {
            return new URL(baseUrl.getProtocol()+":"+relativeUrl);
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

        if( tokens.isEmpty() ) {
            buffer.append("/");
        }
        return new URL( buffer.toString() );
    }
}

