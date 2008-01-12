/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.ssl.InsecureSSLProtocolSocketFactory;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * An object that represents a web browser.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author Dominique Broeglin
 * @author Noboru Sinohara
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author Sergey Gorelkin
 * @author Hans Donner
 * @author Paul King
 * @author Ahmed Ashour
 */
public class WebClient implements Serializable {

    private static final long serialVersionUID = -7214321203864969635L;

    /**
     * HtmlUnit's cookie policy is to be browser-compatible. Code which requires access to
     * HtmlUnit's cookie policy should use this constant, rather than making assumptions
     * and using one of the HttpClient {@link CookiePolicy} constants directly.
     */
    public static final String HTMLUNIT_COOKIE_POLICY = CookiePolicy.BROWSER_COMPATIBILITY;

    private transient WebConnection webConnection_;
    private boolean printContentOnFailingStatusCode_ = true;
    private boolean throwExceptionOnFailingStatusCode_ = true;
    private CredentialsProvider credentialsProvider_ = new DefaultCredentialsProvider();
    private final String proxyHost_;
    private final int proxyPort_;
    private final Map<String, Pattern> proxyBypassHosts_;
    private JavaScriptEngine scriptEngine_;
    private boolean javaScriptEnabled_ = true;
    private boolean cookiesEnabled_ = true;
    private boolean popupBlockerEnabled_;
    private String homePage_;
    private final Map<String, String> requestHeaders_ = Collections.synchronizedMap(new HashMap<String, String>(89));
    private IncorrectnessListener incorrectnessListener_ = new IncorrectnessListenerImpl();

    /**
     * like Firefox default value for network.http.redirection-limit
     */
    private static final int ALLOWED_REDIRECTIONS_SAME_URL = 20;

    private AlertHandler   alertHandler_;
    private ConfirmHandler confirmHandler_;
    private PromptHandler  promptHandler_;
    private StatusHandler  statusHandler_;
    private AjaxController ajaxController_ = new AjaxController();

    private BrowserVersion browserVersion_;
    private boolean isRedirectEnabled_ = true;
    private PageCreator pageCreator_ = new DefaultPageCreator();

    private final Set<WebWindowListener> webWindowListeners_ = new HashSet<WebWindowListener>(5);
    private final List<WebWindow> webWindows_ = Collections.synchronizedList(new ArrayList<WebWindow>());

    private WebWindow currentWindow_;
    private Stack<WebWindow> firstWindowStack_ = new Stack<WebWindow>();
    private int timeout_;
    private HTMLParserListener htmlParserListener_;
    private OnbeforeunloadHandler onbeforeunloadHandler_;
    private Cache cache_ = new Cache();

    private static URLStreamHandler JavaScriptUrlStreamHandler_
        = new com.gargoylesoftware.htmlunit.protocol.javascript.Handler();
    private static URLStreamHandler AboutUrlStreamHandler_
        = new com.gargoylesoftware.htmlunit.protocol.about.Handler();

    /**
     * URL for "about:blank"
     */
    public static final URL URL_ABOUT_BLANK;
    static {
        URL tmpUrl = null;
        try {
            tmpUrl = new URL(null, "about:blank", AboutUrlStreamHandler_);
        }
        catch (final MalformedURLException e) {
            // impossible
            e.printStackTrace();
        }
        URL_ABOUT_BLANK = tmpUrl;
    }

    //singleton WebResponse for "about:blank"
    private static final WebResponse WEB_RESPONSE_FOR_ABOUT_BLANK = new StringWebResponse("", URL_ABOUT_BLANK);

    private ScriptPreProcessor scriptPreProcessor_;

    private Map<String, String> activeXObjectMap_ = Collections.EMPTY_MAP;
    private RefreshHandler refreshHandler_ = new ImmediateRefreshHandler();
    private boolean throwExceptionOnScriptError_ = true;

    /**
     * Creates a web client instance using the browser version returned by
     * {@link BrowserVersion#getDefault()}.
     */
    public WebClient() {
        this(BrowserVersion.getDefault());
    }

    /**
     * Creates a web client instance using the specified {@link BrowserVersion}.
     * @param browserVersion The browser version to simulate.
     */
    public WebClient(final BrowserVersion browserVersion) {
        Assert.notNull("browserVersion", browserVersion);

        homePage_ = "http://www.gargoylesoftware.com/";
        browserVersion_ = browserVersion;
        proxyHost_ = null;
        proxyPort_ = 0;
        proxyBypassHosts_ = new HashMap<String, Pattern>();
        try {
            scriptEngine_ = createJavaScriptEngineIfPossible(this);
        }
        catch (final NoClassDefFoundError e) {
            scriptEngine_ = null;
        }
        // The window must be constructed after the script engine.
        currentWindow_ = new TopLevelWindow("", this);
    }

    /**
     * Create an instance that will use the specified {@link BrowserVersion} and proxy server
     * @param browserVersion The browser version to simulate
     * @param proxyHost The server that will act as proxy
     * @param proxyPort The port to use on the proxy server
     */
    public WebClient(final BrowserVersion browserVersion, final String proxyHost, final int proxyPort) {
        Assert.notNull("browserVersion", browserVersion);
        Assert.notNull("proxyHost", proxyHost);

        homePage_ = "http://www.gargoylesoftware.com/";
        browserVersion_ = browserVersion;
        proxyHost_ = proxyHost;
        proxyPort_ = proxyPort;
        proxyBypassHosts_ = new HashMap<String, Pattern>();
        try {
            scriptEngine_ = createJavaScriptEngineIfPossible(this);
        }
        catch (final NoClassDefFoundError e) {
            scriptEngine_ = null;
        }
        // The window must be constructed after the script engine.
        currentWindow_ = new TopLevelWindow("", this);
    }

    /**
     * Create a javascript engine if possible.
     *
     * @param webClient The webclient that we are creating the script engine for.
     * @return A javascript engine or null if one could not be created.
     */
    private static JavaScriptEngine createJavaScriptEngineIfPossible(final WebClient webClient) {
        try {
            Class.forName("org.mozilla.javascript.Context");
            return new JavaScriptEngine(webClient);
        }
        catch (final ClassNotFoundException e) {
            return null;
        }
        catch (final NoClassDefFoundError e) {
            return null;
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * <p>Return the object that will resolve all url requests<p>
     * @return The connection that will be used.
     */
    public synchronized WebConnection getWebConnection() {
        if (webConnection_ == null) {
            webConnection_ = new HttpWebConnection(this);
        }
        return webConnection_;
    }

    /**
     * Set the object that will resolve all url requests <p />
     *
     * This method is intended for unit testing HtmlUnit itself.  It is not expected
     * to change but you shouldn't need to call it during normal use of HtmlUnit.
     *
     * @param webConnection The new web connection
     */
    public void setWebConnection(final WebConnection webConnection) {
        Assert.notNull("webConnection", webConnection);
        webConnection_ = webConnection;
    }

    /**
     * Send a request to a server and return a Page that represents the
     * response from the server. This page will be used to populate this frame.<p>
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
     *      <td>"application/xhtml+xml"</td>
     *      <td>{@link com.gargoylesoftware.htmlunit.html.HtmlPage} for now, in the
     *          future it will be XML validated as well
     *      </td>
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
     *
     * @param webWindow The WebWindow to load this request into
     * @param parameters Parameter object for the web request
     * @return See above
     * @throws IOException If an IO error occurs
     * @throws FailingHttpStatusCodeException If the server returns a failing status code AND the property
     * {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     *
     * @see WebRequestSettings
     */
    public Page getPage(final WebWindow webWindow, final WebRequestSettings parameters)
        throws IOException, FailingHttpStatusCodeException {

        final Page page = webWindow.getEnclosedPage();
        if (page != null && page instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) page;
            if (!htmlPage.isOnbeforeunloadAccepted()) {
                getLog().debug("The registered OnbeforeunloadHandler rejected to load a new page.");
                return page;
            }
        }

        getLog().debug("Get page for window named '" + webWindow.getName() + "', using " + parameters);

        final WebResponse webResponse;
        final String protocol = parameters.getURL().getProtocol();
        if (protocol.equals("javascript")) {
            webResponse = makeWebResponseForJavaScriptUrl(webWindow, parameters.getURL(), parameters.getCharset());
        }
        else {
            webResponse = loadWebResponse(parameters);
        }

        printContentIfNecessary(webResponse);
        loadWebResponseInto(webResponse, webWindow);
        throwFailingHttpStatusCodeExceptionIfNecessary(webResponse);

        return webWindow.getEnclosedPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * <p>Open a new web window and populate it with a page loaded by
     * {@link #getPage(WebWindow,WebRequestSettings)}</p>
     *
     * @param opener The web window that initiated the request.
     * @param target The name of the window to be opened.  This is the name that would
     * be passed into the javascript open() method.
     * @param params Any parameters
     * @return The new page.
     * @throws FailingHttpStatusCodeException If the server returns a failing status code AND the property
     * {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException If an IO problem occurs.
     */
    public Page getPage(final WebWindow opener, final String target, final WebRequestSettings params)
        throws FailingHttpStatusCodeException, IOException {
        return getPage(openTargetWindow(opener, target, "_self"), params);
    }

    /**
     * Convenient method to build an URL and load it into the current WebWindow
     * @param url The url of the new content.
     * @return The new page.
     * @throws FailingHttpStatusCodeException If the server returns a failing status code AND the property
     * {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException If an IO problem occurs.
     * @throws MalformedURLException if no url can be created from the provided string
     */
    public Page getPage(final String url) throws IOException, FailingHttpStatusCodeException, MalformedURLException {
        return getPage(new URL(url));
    }

    /**
     * Convenient method to load a URL into the current WebWindow
     * @param url The url of the new content.
     * @return The new page.
     * @throws FailingHttpStatusCodeException If the server returns a failing status code AND the property
     * {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException If an IO problem occurs.
     */
    public Page getPage(final URL url) throws IOException, FailingHttpStatusCodeException {
        return getPage(getCurrentWindow(), new WebRequestSettings(url));
    }

    /**
     * Convenient method to load a web request into the current WebWindow
     * @param request The request parameters
     * @return The new page.
     * @throws FailingHttpStatusCodeException If the server returns a failing status code AND the property
     * {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException If an IO problem occurs.
     * @see #getPage(WebWindow,WebRequestSettings)
     */
    public Page getPage(final WebRequestSettings request) throws IOException,
            FailingHttpStatusCodeException {
        return getPage(getCurrentWindow(), request);
    }

    /**
     * Use the specified WebResponse to create a Page object which will then
     * get inserted into the WebWindow.  All initialization and event notification
     * will be handled here.
     *
     * @param webResponse The response that will be used to create the new page.
     * @param webWindow The window that the new page will be placed within.
     * @throws IOException If an IO error occurs.
     * @throws FailingHttpStatusCodeException If the server returns a failing status code AND the property
     * {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     * @return The newly created page.
     */
    public Page loadWebResponseInto(
            final WebResponse webResponse, final WebWindow webWindow)
        throws
            IOException, FailingHttpStatusCodeException {

        Assert.notNull("webResponse", webResponse);
        Assert.notNull("webWindow", webWindow);

        final Page oldPage = webWindow.getEnclosedPage();
        if (oldPage != null) {
            // Remove the old windows before create new ones.
            oldPage.cleanUp();
        }

        final Page newPage = pageCreator_.createPage(webResponse, webWindow);

        synchronized (firstWindowStack_) {
            if (!firstWindowStack_.empty() && firstWindowStack_.peek() == null) {
                firstWindowStack_.pop();
                firstWindowStack_.push(webWindow);
            }
        }

        // the page being loaded may already have been replaced by an other one through js code
        if (webWindow.getEnclosedPage() == newPage) {
            newPage.initialize();
        }

        fireWindowContentChanged(new WebWindowEvent(webWindow, WebWindowEvent.CHANGE, oldPage, newPage));
        return newPage;
    }

    /**
     * Specify whether or not the content of the resulting document will be
     * printed to the console in the event of a failing response code.
     * Successful response codes are in the range 200-299. The default is true.
     *
     * @param enabled True to enable this feature
     */
    public void setPrintContentOnFailingStatusCode(final boolean enabled) {
        printContentOnFailingStatusCode_ = enabled;
    }

    /**
     * Return true if the content of the resulting document will be printed to
     * the console in the event of a failing response code.
     *
     * @return See above
     * @see #setPrintContentOnFailingStatusCode
     */
    public boolean getPrintContentOnFailingStatusCode() {
        return printContentOnFailingStatusCode_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     *
     * <p>Logs the response's content if its status code indicates a request failure and
     * {@link #getPrintContentOnFailingStatusCode()} returns <tt>true</tt>.
     *
     * @param webResponse the response whose content may be logged
     */
    public void printContentIfNecessary(final WebResponse webResponse) {
        final String contentType = webResponse.getContentType();
        final int statusCode = webResponse.getStatusCode();
        final boolean successful = (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES);
        if (getPrintContentOnFailingStatusCode() && !successful) {
            getLog().info("statusCode=[" + statusCode + "] contentType=[" + contentType + "]");
            getLog().info(webResponse.getContentAsString());
        }
    }

    /**
     * Specify whether or not an exception will be thrown in the event of a
     * failing status code. Successful status codes are in the range 200-299.
     * The default is true.
     *
     * @param enabled True to enable this feature
     */
    public void setThrowExceptionOnFailingStatusCode(final boolean enabled) {
        throwExceptionOnFailingStatusCode_ = enabled;
    }

    /**
     * Return true if an exception will be thrown in the event of a failing response code.
     * @return See above
     * @see #setThrowExceptionOnFailingStatusCode
     */
    public boolean isThrowExceptionOnFailingStatusCode() {
        return throwExceptionOnFailingStatusCode_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     *
     * <p>Throws a {@link FailingHttpStatusCodeException} if the request's status code indicates a request
     * failure and {@link #isThrowExceptionOnFailingStatusCode()} returns <tt>true</tt>.
     *
     * @param webResponse the response which may trigger a {@link FailingHttpStatusCodeException}
     */
    public void throwFailingHttpStatusCodeExceptionIfNecessary(final WebResponse webResponse) {
        final int statusCode = webResponse.getStatusCode();
        final boolean successful = (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES);
        if (isThrowExceptionOnFailingStatusCode() && !successful) {
            throw new FailingHttpStatusCodeException(webResponse);
        }
    }

    /**
     * Set a header which will be sent up on EVERY request from this client.
     *
     * @param name The name of the header
     * @param value The value of the header
     */
    public void addRequestHeader(final String name, final String value) {
        requestHeaders_.put(name, value);
    }

    /**
     * Remove a header
     *
     * @param name Name of the header
     * @see #addRequestHeader
     */
    public void removeRequestHeader(final String name) {
        requestHeaders_.remove(name);
    }

    /**
     * Sets the credentials provider that will provide authentication information when
     * trying to access protected information on a web server. This information is
     * required when the server is using Basic HTTP authentication, NTLM authentication,
     * or Digest authentication.
     * @param credentialsProvider The new credentials provider to use to authenticate.
     */
    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        Assert.notNull("credentialsProvider", credentialsProvider);
        credentialsProvider_ = credentialsProvider;
    }

    /**
     * Returns the credentials provider for this client instance. By default, this
     * method returns an instance of {@link DefaultCredentialsProvider}.
     * @return The credentials provider for this client instance.
     */
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider_;
    }

    /**
     * Throw an exception with the specified message. If junit is found in the
     * classpath then a junit.framework.AssertionFailedError will be thrown
     * (the same behavior as calling fail() in junit). If junit is not found
     * then an IllegalStateException will be thrown instead of the
     * AssertionFailedError. <p>
     *
     * Override this to provide custom behavior.
     *
     * @param message The failure message
     * @deprecated
     * @see WebAssert
     */
    public void assertionFailed(final String message) {
        try {
            final Class< ? > clazz = Class.forName("junit.framework.AssertionFailedError");
            final Constructor< ? > constructor = clazz.getConstructor(new Class[]{String.class});
            final Error error = (Error) constructor.newInstance(new Object[]{message});
            throw error;
        }
        catch (final Exception e) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * This method is intended for testing only - use at your own risk.
     * @return the current JavaScript engine (never <code>null</code>).
     */
    public JavaScriptEngine getJavaScriptEngine() {
        return scriptEngine_;
    }

    /**
     * This method is intended for testing only - use at your own risk.
     *
     * @param engine  The new script engine to use.
     */
    public void setJavaScriptEngine(final JavaScriptEngine engine) {
        if (engine == null) {
            throw new NullPointerException("Can't set JavaScriptEngine to null");
        }
        scriptEngine_ = engine;
    }

    /**
     * Enable/disable JavaScript support. By default, this property is enabled.
     *
     * @param enabled <tt>true</tt> to enable JavaScript support
     */
    public void setJavaScriptEnabled(final boolean enabled) {
        javaScriptEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if JavaScript is enabled and the script engine was loaded successfully.
     *
     * @return <tt>true</tt> if JavaScript is enabled
     */
    public boolean isJavaScriptEnabled() {
        return javaScriptEnabled_ && scriptEngine_ != null;
    }

    /**
     * Enable/disable cookie support. By default, this property is enabled.
     *
     * @param enabled <tt>true</tt> to enable cookie support
     */
    public void setCookiesEnabled(final boolean enabled) {
        cookiesEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if cookies are enabled.
     *
     * @return <tt>true</tt> if cookies are enabled
     */
    public boolean isCookiesEnabled() {
        return cookiesEnabled_;
    }

    /**
     * Enable/disable the popup window blocker. By default, the popup blocker is disabled, and popup
     * windows are allowed. When set to <tt>true</tt>, <tt>window.open()</tt> has no effect and
     * returns <tt>null</tt>.
     *
     * @param enabled <tt>true</tt> to enable the popup window blocker
     */
    public void setPopupBlockerEnabled(final boolean enabled) {
        popupBlockerEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if the popup window blocker is enabled.
     *
     * @return <tt>true</tt> if the popup window blocker is enabled
     */
    public boolean isPopupBlockerEnabled() {
        return popupBlockerEnabled_;
    }

    /**
     * Returns the client's current homepage.
     * @return the client's current homepage.
     */
    public String getHomePage() {
        return homePage_;
    }

    /**
     * Sets the client's homepage.
     * @param homePage the new homepage URL
     */
    public void setHomePage(final String homePage) {
        homePage_ = homePage;
    }

    /**
     * Any hosts matched by the specified regular expression pattern will bypass the configured proxy.
     * @param pattern A regular expression pattern that matches the hostnames of the hosts which should
     * bypass the configured proxy.
     * @see Pattern
     */
    public void addHostsToProxyBypass(final String pattern) {
        proxyBypassHosts_.put(pattern, Pattern.compile(pattern));
    }

    /**
     * Any hosts matched by the specified regular expression pattern will no longer bypass the configured proxy.
     * @param pattern The previously added regular expression pattern.
     * @see Pattern
     */
    public void removeHostsFromProxyBypass(final String pattern) {
        proxyBypassHosts_.remove(pattern);
    }

    /**
     * Returns <tt>true</tt> if the host with the specified hostname should be accessed bypassing the
     * configured proxy.
     * @param hostname The name of the host to check.
     * @return <tt>true</tt> if the host with the specified hostname should be accessed bypassing the
     * configured proxy, <tt>false</tt> otherwise.
     */
    private boolean shouldBypassProxy(final String hostname) {
        boolean bypass = false;
        for (final Pattern p : proxyBypassHosts_.values()) {
            if (p.matcher(hostname).find()) {
                bypass = true;
                break;
            }
        }
        return bypass;
    }

    /**
     * Set the alert handler for this webclient.
     * @param alertHandler The new alerthandler or null if none is specified.
     */
    public void setAlertHandler(final AlertHandler alertHandler) {
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
    public void setConfirmHandler(final ConfirmHandler handler) {
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
    public void setPromptHandler(final PromptHandler handler) {
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
    public void setStatusHandler(final StatusHandler statusHandler) {
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
    public void setCurrentWindow(final WebWindow window) {
        Assert.notNull("window", window);
        currentWindow_ = window;
    }

    /**
     * Return the "first" window for this client.  This is the first window
     * opened since pushClearFirstWindow() was last called.
     * @return The first window.
     */
    public WebWindow popFirstWindow() {
        synchronized (firstWindowStack_) {
            return (WebWindow) firstWindowStack_.pop();
        }
    }

    /**
     * Clear the first window for this client.
     */
    public void pushClearFirstWindow() {
        synchronized (firstWindowStack_) {
            firstWindowStack_.push(null);
        }
    }

    /**
     * Add a listener for WebWindowEvent's.  All events from all windows associated with this
     * client will be sent to the specified listener.
     * @param listener A listener.
     */
    public void addWebWindowListener(final WebWindowListener listener) {
        Assert.notNull("listener", listener);
        webWindowListeners_.add(listener);
    }

    /**
     * Remove a listener for WebWindowEvent's.
     * @param listener A listener.
     */
    public void removeWebWindowListener(final WebWindowListener listener) {
        Assert.notNull("listener", listener);
        webWindowListeners_.remove(listener);
    }

    private void fireWindowContentChanged(final WebWindowEvent event) {
        for (final WebWindowListener listener : new ArrayList<WebWindowListener>(webWindowListeners_)) {
            listener.webWindowContentChanged(event);
        }
    }

    private void fireWindowOpened(final WebWindowEvent event) {
        for (final WebWindowListener listener : new ArrayList<WebWindowListener>(webWindowListeners_)) {
            listener.webWindowOpened(event);
        }
    }

    private void fireWindowClosed(final WebWindowEvent event) {
        for (final WebWindowListener listener : new ArrayList<WebWindowListener>(webWindowListeners_)) {
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
    public WebWindow openWindow(final URL url, final String windowName) {
        Assert.notNull("windowName", windowName);
        return openWindow(url, windowName, getCurrentWindow());
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
    public WebWindow openWindow(final URL url, final String windowName, final WebWindow opener) {
        final WebWindow window = openTargetWindow(opener, windowName, "_blank");
        if (url != null) {
            try {
                final WebRequestSettings settings = new WebRequestSettings(url);
                final HtmlPage openerPage = (HtmlPage) opener.getEnclosedPage();
                if (!getBrowserVersion().isIE()) {
                    settings.addAdditionalHeader("Referer", openerPage.getWebResponse().getUrl().toExternalForm());
                }
                getPage(window, settings);
            }
            catch (final IOException e) {
                getLog().error("Error when loading content into window", e);
            }
        }
        else {
            initializeEmptyWindow(window);
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
    private WebWindow openTargetWindow(
            final WebWindow opener, final String windowName, final String defaultName) {

        Assert.notNull("opener", opener);
        Assert.notNull("defaultName", defaultName);

        String windowToOpen = windowName;
        if (windowToOpen == null || windowToOpen.length() == 0) {
            windowToOpen = defaultName;
        }

        WebWindow webWindow = null;
        if (windowToOpen.equals("_self")) {
            webWindow = opener;
            windowToOpen = "";
        }
        else if (windowToOpen.equals("_parent")) {
            webWindow = opener.getParentWindow();
            windowToOpen = "";
        }
        else if (windowToOpen.equals("_top")) {
            webWindow = opener.getTopWindow();
            windowToOpen = "";
        }
        else if (windowToOpen.equals("_blank")) {
            // Leave window null to create a new window.
            windowToOpen = "";
        }
        else if (windowToOpen.length() != 0) {
            try {
                webWindow = getWebWindowByName(windowToOpen);
            }
            catch (final WebWindowNotFoundException e) {
                // Fall through - a new window will be created below
            }
        }

        if (webWindow == null) {
            webWindow = new TopLevelWindow(windowToOpen, this);
            fireWindowOpened(new WebWindowEvent(webWindow, WebWindowEvent.OPEN, null, null));
        }

        if (webWindow instanceof TopLevelWindow && webWindow != opener.getTopWindow()) {
            ((TopLevelWindow) webWindow).setOpener(opener);
        }

        return webWindow;
    }

    /**
     * Set whether or not redirections will be followed automatically on receipt of
     * a redirect status code from the server.
     * @param enabled true to enable automatic redirection.
     */
    public void setRedirectEnabled(final boolean enabled) {
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
     * If set to <tt>true</tt>, the client will accept connections to any host, regardless of
     * whether they have valid certificates or not. This is especially useful when you are trying to
     * connect to a server with expired or corrupt certificates.
     *
     * @param useInsecureSSL whether or not to use insecure SSL
     * @throws GeneralSecurityException if a security error occurs
     */
    public void setUseInsecureSSL(final boolean useInsecureSSL) throws GeneralSecurityException {
        if (useInsecureSSL) {
            final ProtocolSocketFactory factory = new InsecureSSLProtocolSocketFactory();
            final Protocol https = new Protocol("https", factory, 443);
            Protocol.registerProtocol("https", https);
        }
        else {
            Protocol.unregisterProtocol("https");
        }
    }

    /**
     * Set the object that will be used to create pages.  Set this if you want
     * to customize the type of page that is returned for a given content type.
     *
     * @param pageCreator The new page creator
     */
    public void setPageCreator(final PageCreator pageCreator) {
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
    public WebWindow getWebWindowByName(final String name) throws WebWindowNotFoundException {
        Assert.notNull("name", name);

        for (final WebWindow webWindow : webWindows_) {
            if (webWindow.getName().equals(name)) {
                return webWindow;
            }
        }

        throw new WebWindowNotFoundException(name);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Initialize a new web window for JavaScript.
     * @param webWindow The new WebWindow
     */
    public void initialize(final WebWindow webWindow) {
        Assert.notNull("webWindow", webWindow);
        if (scriptEngine_ != null) {
            scriptEngine_.initialize(webWindow);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Initialize a new page for JavaScript.
     * @param newPage The new page.
     */
    public void initialize(final Page newPage) {
        Assert.notNull("newPage", newPage);
        if (scriptEngine_ != null) {
            ((Window) newPage.getEnclosingWindow().getScriptObject()).initialize(newPage);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Initialize a new empty web window for JavaScript.
     * @param webWindow The new WebWindow
     */
    public void initializeEmptyWindow(final WebWindow webWindow) {
        Assert.notNull("webWindow", webWindow);
        if (scriptEngine_ != null) {
            initialize(webWindow);
            ((Window) webWindow.getScriptObject()).initialize();
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Add a new web window to the list of available windows.
     * @param webWindow The new WebWindow
     */
    public void registerWebWindow(final WebWindow webWindow) {
        Assert.notNull("webWindow", webWindow);
        webWindows_.add(webWindow);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Remove a web window from the list of available windows.
     * @param webWindow The WebWindow to remove
     */
    public void deregisterWebWindow(final WebWindow webWindow) {
        Assert.notNull("webWindow", webWindow);
        webWindows_.remove(webWindow);

        if (getCurrentWindow() == webWindow) {
            if (webWindows_.size() == 0) {
                // Create a new one - we always have to have at least one window.
                setCurrentWindow(new TopLevelWindow("", this));
            }
            else {
                setCurrentWindow((WebWindow) webWindows_.get(0));
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

    private static URL makeUrl(final String urlString) throws MalformedURLException {
        Assert.notNull("urlString", urlString);

        if (TextUtil.startsWithIgnoreCase(urlString, "javascript:")) {
            return new URL(null, urlString, JavaScriptUrlStreamHandler_);
        }
        else if (TextUtil.startsWithIgnoreCase(urlString, "about:")) {
            return new URL(null, urlString, AboutUrlStreamHandler_);
        }
        else {
            return new URL(urlString);
        }
    }

    /**
     * Expands a relative url relative to the specified base. In most situations
     * this is the same as <code>new URL(baseUrl, relativeUrl)</code> but
     * there are some cases that URL doesn't handle correctly. See
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>
     * regarding Relative Uniform Resource Locators for more information.
     *
     * @param baseUrl The base url
     * @param relativeUrl The relative url
     * @return See above
     * @throws MalformedURLException If an error occurred when creating a URL object
     */
    public static URL expandUrl(final URL baseUrl, final String relativeUrl)
        throws MalformedURLException {

        if (StringUtils.isEmpty(relativeUrl)) {
            return baseUrl;
        }
        String parseUrl = relativeUrl;

        // section 2.4.2 - parsing scheme
        final int schemeIndex = parseUrl.indexOf(":");
        if (schemeIndex != -1) {
            boolean isProtocolSpecified = true;
            for (int i = 0; i < schemeIndex; i++) {
                if (Character.isLetter(parseUrl.charAt(i)) == false) {
                    isProtocolSpecified = false;
                    break;
                }
            }
            if (isProtocolSpecified) {
                return makeUrl(parseUrl);
            }
        }

        // section 2.4.3 - parsing network location/login
        if (parseUrl.startsWith("//")) {
            return makeUrl(baseUrl.getProtocol() + ":" + parseUrl);
        }

        // section 2.4.1 - parsing fragment
        final int fragmentIndex = parseUrl.lastIndexOf("#");
        String reference = null;
        if (fragmentIndex != -1) {
            reference = StringUtils.substringAfterLast(parseUrl, "#");
            parseUrl = parseUrl.substring(0, fragmentIndex);
        }

        // section 2.4.4 - parsing query
        String stringQuery = null;
        final int queryIndex = parseUrl.lastIndexOf("?");
        if (queryIndex != -1) {
            stringQuery = parseUrl.substring(queryIndex);
            parseUrl = parseUrl.substring(0, queryIndex);
        }

        // section 2.4.5 - parsing parameters
        String stringParameters = null;
        final int parametersIndex = parseUrl.lastIndexOf(";");
        if (parametersIndex != -1) {
            stringParameters = parseUrl.substring(parametersIndex);
            parseUrl = parseUrl.substring(0, parametersIndex);
        }

        // section 2.4.6 - parse path
        final List<String> tokens = new ArrayList<String>();
        final String stringToTokenize;
        if (parseUrl.trim().length() == 0) {
            stringToTokenize = baseUrl.getPath();
        }
        else if (parseUrl.startsWith("/")) {
            stringToTokenize = parseUrl;
        }
        else {
            String path = baseUrl.getPath();
            if (!path.endsWith("/") && parseUrl.length() != 0) {
                path += "/..";
            }
            stringToTokenize = path + "/" + parseUrl;
        }

        final String pathToTokenize = stringToTokenize;
        final StringTokenizer tokenizer = new StringTokenizer(pathToTokenize, "/");
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }

        for (int i = 0; i < tokens.size(); i++) {
            final String oneToken = (String) tokens.get(i);
            if (oneToken.length() == 0 || oneToken.equals(".")) {
                tokens.remove(i--);
            }
            else if (oneToken.equals("..")) {
                tokens.remove(i--);
                if (i >= 0) {
                    tokens.remove(i--);
                }
            }
        }

        final StringBuffer buffer = new StringBuffer();
        buffer.append(baseUrl.getProtocol());
        buffer.append("://");
        buffer.append(baseUrl.getHost());
        final int port = baseUrl.getPort();
        if (port != -1) {
            buffer.append(":");
            buffer.append(port);
        }

        for (final String token : tokens) {
            buffer.append("/");
            buffer.append(token);
        }

        if (pathToTokenize.endsWith("/")) {
            buffer.append("/");
        }

        if (stringParameters != null) {
            buffer.append(stringParameters);
        }
        if (stringQuery != null) {
            buffer.append(stringQuery);
        }
        if (reference != null) {
            buffer.append("#").append(reference);
        }
        final String newUrlString = buffer.toString();
        return makeUrl(newUrlString);
    }

    private WebResponse makeWebResponseForAboutUrl(final URL url) {
        final String urlWithoutQuery = StringUtils.substringBefore(url.toExternalForm(), "?");
        if (!StringUtils.substringAfter(urlWithoutQuery, "about:").equalsIgnoreCase("blank")) {
            throw new IllegalArgumentException(
                url.toExternalForm() + "is not supported, only about:blank is supported now.");
        }
        return WEB_RESPONSE_FOR_ABOUT_BLANK;
    }

    /**
     * Builds a WebResponse for a file URL.
     * This first implementation is basic.
     * It assumes that the file contains an html page encoded with the specified encoding.
     * @param url The file url
     * @param charset encoding to use
     * @return The web response
     * @throws IOException If an IO problem occurs
     */
    private WebResponse makeWebResponseForFileUrl(final URL url, final String charset) throws IOException {

        URL cleanUrl = url;
        if (cleanUrl.getQuery() != null) {
            // Get rid of the query portion before trying to load the file.
            cleanUrl = UrlUtils.getUrlWithNewQuery(cleanUrl, null);
        }
        if (cleanUrl.getRef() != null) {
            // Get rid of the ref portion before trying to load the file.
            cleanUrl = UrlUtils.getUrlWithNewRef(cleanUrl, null);
        }

        final File file = FileUtils.toFile(cleanUrl);
        final String contentType = guessContentType(file);

        if (contentType.startsWith("text")) {
            final String str = IOUtils.toString(new FileInputStream(file), charset);
            return new StringWebResponse(str, charset, url) {
                private static final long serialVersionUID = 5713127877370126236L;
                public String getContentType() {
                    return contentType;
                }
            };
        }
        else {
            final byte[] data = IOUtils.toByteArray(new FileInputStream(file));
            return new BinaryWebResponse(data, url, contentType);
        }
    }

    /**
     * A simple WebResponse created from a byte array. Content is assumed to be
     * of some binary type.
     *
     * @author Paul King
     */
    private static final class BinaryWebResponse extends WebResponseImpl {

        private static final long serialVersionUID = 8000117717229261957L;

        private final byte[] data_;

        private static WebResponseData getWebResponseData(final byte[] data, final String contentType) {
            final List<NameValuePair> compiledHeaders = new ArrayList<NameValuePair>();
            compiledHeaders.add(new NameValuePair("Content-Type", contentType));
            return new WebResponseData(data, HttpStatus.SC_OK, "OK", compiledHeaders);
        }

        private BinaryWebResponse(final byte[] data, final URL originatingURL, final String contentType) {
            super(getWebResponseData(data, contentType), originatingURL, SubmitMethod.GET, 0);
            data_ = data;
        }

        public InputStream getContentAsStream() {
            return new ByteArrayInputStream(data_);
        }
    }

    /**
     * Tries to guess the content type of the file.<br/>
     * This utility could be located in an helper class but we can compare this functionality
     * for instance with the "Helper Applications" settings of Mozilla and therefore see it as a
     * property of the "browser".
     * @param file the file
     * @return "application/octet-stream" if nothing could be guessed.
     */
    public String guessContentType(final File file) {
        String contentType = null;
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            contentType = URLConnection.guessContentTypeFromStream(inputStream);
        }
        catch (final IOException e) {
            // nothing, silently ignore
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }

        if (contentType == null) {
            contentType = URLConnection.guessContentTypeFromName(file.getName());
        }
        if (contentType == null) {
            if (file.getName().endsWith(".js")) {
                contentType = "text/javascript";
            }
            else {
                contentType = "application/octet-stream";
            }
        }

        return contentType;
    }

    private WebResponse makeWebResponseForJavaScriptUrl(final WebWindow webWindow, final URL url,
            final String charset) {
        if (!(webWindow instanceof FrameWindow)) {
            throw new IllegalArgumentException(
                "javascript urls can only be used to load content into frames and iframes");
        }

        final FrameWindow frameWindow = (FrameWindow) webWindow;
        final HtmlPage enclosingPage = frameWindow.getEnclosingPage();
        final ScriptResult scriptResult = enclosingPage.executeJavaScriptIfPossible(
            url.toExternalForm(), "javascript url", 1);

        final String contentString = scriptResult.getJavaScriptResult().toString();
        return new StringWebResponse(contentString, charset);
    }

    /**
     * Loads a {@link WebResponse} from the server
     * @param webRequestSettings settings to use when making the request
     * @throws IOException if an IO problem occurs
     * @return The WebResponse
     */
    public WebResponse loadWebResponse(final WebRequestSettings webRequestSettings)
        throws
            IOException {

        final WebResponse responseFromCache = cache_.getCachedContent(webRequestSettings);
        if (responseFromCache != null) {
            return responseFromCache;
        }

        final WebResponse response;
        final String protocol = webRequestSettings.getURL().getProtocol();
        if (protocol.equals("about")) {
            response = makeWebResponseForAboutUrl(webRequestSettings.getURL());
        }
        else if (protocol.equals("file")) {
            response = makeWebResponseForFileUrl(webRequestSettings.getURL(), webRequestSettings.getCharset());
        }
        else {
            response = loadWebResponseFromWebConnection(webRequestSettings, ALLOWED_REDIRECTIONS_SAME_URL);
        }

        cache_.cacheIfNeeded(webRequestSettings, response);
        return response;
    }

    /**
     * Loads a {@link WebResponse} from the server through the WebConnection.
     * @param webRequestSettings settings to use when making the request
     * @throws IOException if an IO problem occurs
     * @return The WebResponse
     */
    private WebResponse loadWebResponseFromWebConnection(final WebRequestSettings webRequestSettings,
                final int nbAllowedRedirections)
        throws
            IOException {
        final URL url = webRequestSettings.getURL();
        final SubmitMethod method = webRequestSettings.getSubmitMethod();
        final List<KeyValuePair> parameters = webRequestSettings.getRequestParameters();

        Assert.notNull("url", url);
        Assert.notNull("method", method);
        Assert.notNull("parameters", parameters);

        getLog().debug("Load response for " + url.toExternalForm());

        // If the request settings don't specify a custom proxy, use the default client proxy...
        if (webRequestSettings.getProxyHost() == null) {
            // ...unless the host needs to bypass the configured client proxy!
            if (!shouldBypassProxy(webRequestSettings.getURL().getHost())) {
                webRequestSettings.setProxyHost(proxyHost_);
                webRequestSettings.setProxyPort(proxyPort_);
            }
        }

        //TODO: this should probably be handled inside of WebRequestSettings and
        // could cause a bug if anything above here reads the url again
        final URL fixedUrl = encodeUrl(url);
        webRequestSettings.setURL(fixedUrl);

        // adds the headers that are sent on every request
        webRequestSettings.getAdditionalHeaders().putAll(requestHeaders_);

        final WebResponse webResponse = getWebConnection().getResponse(webRequestSettings);
        final int statusCode = webResponse.getStatusCode();

        if (statusCode >= HttpStatus.SC_MOVED_PERMANENTLY && statusCode <= HttpStatus.SC_TEMPORARY_REDIRECT
                && isRedirectEnabled()) {
            final URL newUrl;
            String locationString = null;
            try {
                locationString = webResponse.getResponseHeaderValue("Location");
                newUrl = expandUrl(fixedUrl, locationString);
            }
            catch (final MalformedURLException e) {
                getIncorrectnessListener().notify("Got a redirect status code [" + statusCode + " "
                    + webResponse.getStatusMessage()

                    + "] but the location is not a valid url [" + locationString
                    + "]. Skipping redirection processing.", this);
                return webResponse;
            }

            getLog().debug("Got a redirect status code [" + statusCode + "] new location=[" + locationString + "]");

            if (webRequestSettings.getSubmitMethod().equals(SubmitMethod.GET)
                    && webResponse.getUrl().toExternalForm().equals(locationString)) {

                if (nbAllowedRedirections == 0) {
                    getLog().warn("Max redirections allowed to the same location reached for ["
                            + locationString + "]. Skipping redirection.");
                }
                else {
                    getLog().debug("Got a redirect with location same as the page we just loaded. "
                            + "Nb self redirection allowed: " + nbAllowedRedirections);
                    return loadWebResponseFromWebConnection(webRequestSettings, nbAllowedRedirections - 1);
                }
            }
            else if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)
                && method.equals(SubmitMethod.GET)) {

                final WebRequestSettings wrs = new WebRequestSettings(newUrl);
                wrs.setRequestParameters(parameters);
                return loadWebResponse(wrs);
            }
            else if (statusCode <= HttpStatus.SC_SEE_OTHER) {
                final WebRequestSettings wrs = new WebRequestSettings(newUrl);
                wrs.setSubmitMethod(SubmitMethod.GET);
                return loadWebResponse(wrs);
            }
        }

        return webResponse;
    }

    /**
     * Encodes illegal parameter in path or query string (if any) as done by browsers.
     * Example: changes "http://first?a=b c" to "http://first?a=b%20c"
     * @param url the url to encode
     * @return the provided url if no change needed, the fixed url else
     * @throws MalformedURLException if the new URL could note be instantiated
     * @throws URIException if the default protocol charset is not supported
     */
    protected URL encodeUrl(final URL url) throws MalformedURLException, URIException {
        final String path = url.getPath();
        final String fixedPath = encode(path, URI.allowed_abs_path);
        final String query = url.getQuery();
        final String fixedQuery = encode(query, URI.allowed_query);

        if (!StringUtils.equals(path, fixedPath) || !StringUtils.equals(query, fixedQuery)) {
            final StringBuffer newUrl = new StringBuffer();
            newUrl.append(url.getProtocol());
            newUrl.append("://");
            newUrl.append(url.getHost());
            if (url.getPort() != -1) {
                newUrl.append(":");
                newUrl.append(url.getPort());
            }
            newUrl.append(fixedPath);
            if (url.getUserInfo() != null) {
                newUrl.append(url.getUserInfo());
            }
            if (fixedQuery != null) {
                newUrl.append("?");
                newUrl.append(fixedQuery);
            }
            if (url.getRef() != null) {
                newUrl.append("#");
                newUrl.append(url.getRef());
            }

            return new URL(newUrl.toString());
        }
        else {
            return url;
        }
    }

    /**
     * Encodes unallowed characters in a string
     * @param str the string to encode
     * @param allowed the allowed characters
     * @return the encoded string
     * @throws URIException if encoding fails
     */
    private String encode(final String str, final BitSet allowed) throws URIException {
        if (str == null) {
            return null;
        }
        final BitSet bits = new BitSet(str.length());
        bits.set('%');
        bits.set('+');
        bits.or(allowed);
        return URIUtil.encode(str, bits);
    }

    /**
     * Return an immutable list of open web windows (top windows or not).
     * @return The web windows
     */
    public List<WebWindow> getWebWindows() {
        return Collections.unmodifiableList(webWindows_);
    }

    /**
     * Set the handler to be used whenever a refresh is triggered.  Refer
     * to the documentation for {@link RefreshHandler} for more details.
     * @param handler The new handler
     */
    public void setRefreshHandler(final RefreshHandler handler) {
        if (handler == null) {
            refreshHandler_ = new ImmediateRefreshHandler();
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
    public void setScriptPreProcessor(final ScriptPreProcessor scriptPreProcessor) {
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
    public void setActiveXObjectMap(final Map<String, String> activeXObjectMap) {
        activeXObjectMap_ = activeXObjectMap;
    }

    /**
     * Return the active X object map for this webclient.
     * @return the active X object map.
     */
    public Map<String, String> getActiveXObjectMap() {
        return activeXObjectMap_;
    }

    /**
     * Defines a listener for messages generated by the html parser.<br/>
     * <b>Note</b>: If {@link #getIgnoreOutsideContent()} returns <code>false</code>, the parser
     * will ignore closing &lt;body&gt; and &lt;html&gt; tags to be able to handle html content
     * incorrectly located after the end of the html file. As a consequence it will finally
     * notify as errors that &lt;body&gt; and &lt;html&gt; are not closed properly even if
     * they were correctly present.
     * @param listener the new listener, <code>null</code> if messages should be totally ignored.
     */
    public void setHTMLParserListener(final HTMLParserListener listener) {
        htmlParserListener_ = listener;
    }

    /**
     * Gets the configured listener for messages generated by the html parser.
     * @return <code>null</code> if no listener is defined (default value).
     */
    public HTMLParserListener getHTMLParserListener() {
        return htmlParserListener_;
    }

    /**
     * Set the flag on the HtmlParse to ignore the content that is outside of the BODY
     * and HTML tags.
     * @param ignoreOutsideContent The boolean flag to enable or disable the support of
     * content outside of the HTML and BODY tags
     */
    public static void setIgnoreOutsideContent(final boolean ignoreOutsideContent) {
        HTMLParser.setIgnoreOutsideContent(ignoreOutsideContent);
    }

    /**
     * Get the state of the flag to ignore content outside the BODY and HTML tags
     * @return - The current state
     */
    public static boolean getIgnoreOutsideContent() {
        return HTMLParser.getIgnoreOutsideContent();
    }

    /**
     * Gets the timeout value for the WebConnection
     *
     * @return The timeout value in milliseconds
     * @see WebClient#setTimeout(int)
     */
    public int getTimeout() {
        return timeout_;
    }
    
    /**
     * Sets the timeout of the WebConnection. Set to zero (the default) for an infinite wait.
     *
     * Note: The timeout is used twice. The first is for making the socket connection, the
     * second is for data retrieval. If the time is critical you must allow for twice the
     * time specified here.
     *
     * @param timeout The value of the timeout in milliseconds
     */
    public void setTimeout(final int timeout) {
        timeout_ = timeout;
    }

    /**
     * Indicates if an exception should be thrown when a script execution fails
     * (the default) or if it should be caught and just logged to allow page
     * execution to continue.
     * @return <code>true</code> if an exception is thrown on script error (the default)
     */
    public boolean isThrowExceptionOnScriptError() {
        return throwExceptionOnScriptError_;
    }

    /**
     * Changes the behavior of this webclient when a script error occurs.
     * @param newValue indicates if exception should be thrown or not
     */
    public void setThrowExceptionOnScriptError(final boolean newValue) {
        throwExceptionOnScriptError_ = newValue;
    }

    /**
     * Gets the current listener for encountered incorrectness (except HTML parsing messages that
     * are handled by the HTML parser listener). Default value is an instance of
     * {@link IncorrectnessListenerImpl}.
     * @return the current listener (not <code>null</code>).
     */
    public IncorrectnessListener getIncorrectnessListener() {
        return incorrectnessListener_;
    }

    /**
     * Returns the current HTML incorrectness listener.
     * @param listener the new value (not <code>null</code>)
     */
    public void setIncorrectnessListener(final IncorrectnessListener listener) {
        if (listener == null) {
            throw new NullPointerException("Null incorrectness listener.");
        }
        incorrectnessListener_ = listener;
    }
    
    /**
     * Gets the current Ajax controller
     * @return the controller
     */
    public AjaxController getAjaxController() {
        return ajaxController_;
    }

    /**
     * Sets the current Ajax controller
     * <span style="color:red">EXPERIMENTAL - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * @param newValue the controller
     */
    public void setAjaxController(final AjaxController newValue) {
        if (newValue == null) {
            throw new NullPointerException();
        }
        ajaxController_ = newValue;
    }

    /**
     * Set the onbeforeunload handler for this webclient.
     * @param onbeforeunloadHandler The new onbeforeunloadHandler or null if none is specified.
     */
    public void setOnbeforeunloadHandler(final OnbeforeunloadHandler onbeforeunloadHandler) {
        onbeforeunloadHandler_ = onbeforeunloadHandler;
    }

    /**
     * Return the onbeforeunload handler for this webclient.
     * @return the onbeforeunload handler or null if one hasn't been set.
     */
    public OnbeforeunloadHandler getOnbeforeunloadHandler() {
        return onbeforeunloadHandler_;
    }


    /**
     * Gets the cache currently used
     * @return the cache (may not be null)
     */
    public Cache getCache() {
        return cache_;
    }

    /**
     * Sets the cache to use
     * @param cache the new cache (must not be <code>null</code>)
     */
    public void setCache(final Cache cache) {
        if (cache == null) {
            throw new IllegalArgumentException("cache should not be null!");
        }
        cache_ = cache;
    }
}
