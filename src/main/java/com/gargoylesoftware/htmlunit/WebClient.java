/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.DIALOGWINDOW_REFERER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_150;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.PROTOCOL_DATA;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.URL_MINIMAL_QUERY_ENCODING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.WINDOW_ACTIVE_ELEMENT_FOCUSED;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.CredentialsProvider;
import org.w3c.css.sac.ErrorHandler;

import com.gargoylesoftware.htmlunit.attachment.Attachment;
import com.gargoylesoftware.htmlunit.attachment.AttachmentHandler;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.ProxyAutoConfig;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.protocol.data.DataUrlDecoder;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * The main starting point in HtmlUnit: this class simulates a web browser.
 * <p>
 * A standard usage of HtmlUnit will start with using the {@link #getPage(String)} method
 * (or {@link #getPage(URL)}) to load a first {@link Page}
 * and will continue with further processing on this page depending on its type.
 * </p>
 * <b>Example:</b><br>
 * <br>
 * <code>
 * final WebClient webClient = new WebClient();<br/>
 * final {@link HtmlPage} startPage = webClient.getPage("http://htmlunit.sf.net");<br/>
 * assertEquals("HtmlUnit - Welcome to HtmlUnit", startPage.{@link HtmlPage#getTitleText() getTitleText}());
 * </code>
 * <p>
 * Note: a {@link WebClient} instance is <b>not thread safe</b>. It is intended to be used from a single thread.
 * </p>
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
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Martin Tamme
 * @author Amit Manjhi
 * @author Nicolas Belisle
 * @author Ronald Brill
 */
public class WebClient implements Serializable {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(WebClient.class);

    /** Like the Firefox default value for network.http.redirection-limit. */
    private static final int ALLOWED_REDIRECTIONS_SAME_URL = 20;

    private transient WebConnection webConnection_ = createWebConnection();
    private CredentialsProvider credentialsProvider_ = new DefaultCredentialsProvider();
    private CookieManager cookieManager_ = new CookieManager();
    private transient JavaScriptEngine scriptEngine_;
    private final Map<String, String> requestHeaders_ = Collections.synchronizedMap(new HashMap<String, String>(89));
    private IncorrectnessListener incorrectnessListener_ = new IncorrectnessListenerImpl();
    private WebConsole webConsole_;

    private AlertHandler alertHandler_;
    private ConfirmHandler confirmHandler_;
    private PromptHandler promptHandler_;
    private StatusHandler statusHandler_;
    private AttachmentHandler attachmentHandler_;
    private AjaxController ajaxController_ = new AjaxController();

    private BrowserVersion browserVersion_;
    private PageCreator pageCreator_ = new DefaultPageCreator();

    private final Set<WebWindowListener> webWindowListeners_ = new HashSet<WebWindowListener>(5);
    private final Stack<TopLevelWindow> topLevelWindows_ = new Stack<TopLevelWindow>(); // top-level windows
    private final List<WebWindow> windows_ = Collections.synchronizedList(new ArrayList<WebWindow>()); // all windows
    private WebWindow currentWindow_;

    private HTMLParserListener htmlParserListener_;
    private ErrorHandler cssErrorHandler_ = new DefaultCssErrorHandler();
    private OnbeforeunloadHandler onbeforeunloadHandler_;
    private Cache cache_ = new Cache();

    /** URL for "about:blank". */
    public static final URL URL_ABOUT_BLANK = UrlUtils.toUrlSafe("about:blank");

    /** Singleton {@link WebResponse} for "about:blank". */
    private static final WebResponse WEB_RESPONSE_FOR_ABOUT_BLANK = new StringWebResponse("", URL_ABOUT_BLANK);

    private ScriptPreProcessor scriptPreProcessor_;

    private Map<String, String> activeXObjectMap_ = Collections.emptyMap();
    private RefreshHandler refreshHandler_ = new ImmediateRefreshHandler();
    private JavaScriptErrorListener javaScriptErrorListener_;

    private WebClientOptions options_ = new WebClientOptions();

    /**
     * Creates a web client instance using the browser version returned by
     * {@link BrowserVersion#getDefault()}.
     */
    public WebClient() {
        this(BrowserVersion.getDefault());
    }

    /**
     * Creates a web client instance using the specified {@link BrowserVersion}.
     * @param browserVersion the browser version to simulate
     */
    public WebClient(final BrowserVersion browserVersion) {
        WebAssert.notNull("browserVersion", browserVersion);
        init(browserVersion, new ProxyConfig());
    }

    /**
     * Creates an instance that will use the specified {@link BrowserVersion} and proxy server.
     * @param browserVersion the browser version to simulate
     * @param proxyHost the server that will act as proxy
     * @param proxyPort the port to use on the proxy server
     */
    public WebClient(final BrowserVersion browserVersion, final String proxyHost, final int proxyPort) {
        WebAssert.notNull("browserVersion", browserVersion);
        WebAssert.notNull("proxyHost", proxyHost);
        init(browserVersion, new ProxyConfig(proxyHost, proxyPort));
    }

    /**
     * Generic initialization logic used by all constructors. This method does not perform any
     * parameter validation; such validation must be handled by the constructors themselves.
     * @param browserVersion the browser version to simulate
     * @param proxyConfig the proxy configuration to use
     */
    private void init(final BrowserVersion browserVersion, final ProxyConfig proxyConfig) {
        browserVersion_ = browserVersion;
        getOptions().setProxyConfig(proxyConfig);

        scriptEngine_ = new JavaScriptEngine(this);
        // The window must be constructed AFTER the script engine.
        addWebWindowListener(new CurrentWindowTracker(this));
        currentWindow_ = new TopLevelWindow("", this);
        fireWindowOpened(new WebWindowEvent(currentWindow_, WebWindowEvent.OPEN, null, null));
    }

    /**
     * Returns the object that will resolve all URL requests.
     *
     * @return the connection that will be used
     */
    public WebConnection getWebConnection() {
        return webConnection_;
    }

    /**
     * Sets the object that will resolve all URL requests.
     *
     * @param webConnection the new web connection
     */
    public void setWebConnection(final WebConnection webConnection) {
        WebAssert.notNull("webConnection", webConnection);
        webConnection_ = webConnection;
    }

    /**
     * Send a request to a server and return a Page that represents the
     * response from the server. This page will be used to populate the provided window.
     * <p>
     * The returned {@link Page} will be created by the {@link PageCreator}
     * configured by {@link #setPageCreator(PageCreator)}, if any.
     * <p>
     * The {@link DefaultPageCreator} will create a {@link Page} depending on the content type of the HTTP response,
     * basically {@link HtmlPage} for HTML content, {@link com.gargoylesoftware.htmlunit.xml.XmlPage} for XML content,
     * {@link TextPage} for other text content and {@link UnexpectedPage} for anything else.
     *
     * @param webWindow the WebWindow to load the result of the request into
     * @param webRequest the web request
     * @param <P> the page type
     * @return the page returned by the server when the specified request was made in the specified window
     * @throws IOException if an IO error occurs
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     *
     * @see WebRequest
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P getPage(final WebWindow webWindow, final WebRequest webRequest)
        throws IOException, FailingHttpStatusCodeException {

        final Page page = webWindow.getEnclosedPage();

        if (page != null) {
            final URL prev = page.getUrl();
            final URL current = webRequest.getUrl();
            if (current.sameFile(prev) && current.getRef() != null
                && !StringUtils.equals(current.getRef(), prev.getRef())) {
                // We're just navigating to an anchor within the current page.
                page.getWebResponse().getWebRequest().setUrl(current);
                webWindow.getHistory().addPage(page);
                return (P) page;
            }
        }

        if (page instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) page;
            if (!htmlPage.isOnbeforeunloadAccepted()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("The registered OnbeforeunloadHandler rejected to load a new page.");
                }
                return (P) page;
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get page for window named '" + webWindow.getName() + "', using " + webRequest);
        }

        final WebResponse webResponse;
        final String protocol = webRequest.getUrl().getProtocol();
        if ("javascript".equals(protocol)) {
            webResponse = makeWebResponseForJavaScriptUrl(webWindow, webRequest.getUrl(), webRequest.getCharset());
            if (webWindow.getEnclosedPage() != null && webWindow.getEnclosedPage().getWebResponse() == webResponse) {
                // a javascript:... url with result of type undefined didn't changed the page
                return (P) webWindow.getEnclosedPage();
            }
        }
        else {
            webResponse = loadWebResponse(webRequest);
        }

        printContentIfNecessary(webResponse);
        loadWebResponseInto(webResponse, webWindow);

        // start execution here
        // note: we have to do this also if the server reports an error!
        //       e.g. if the server returns a 404 error page that includes javascript
        if (scriptEngine_ != null) {
            scriptEngine_.registerWindowAndMaybeStartEventLoop(webWindow);
        }

        // check and report problems if needed
        throwFailingHttpStatusCodeExceptionIfNecessary(webResponse);
        return (P) webWindow.getEnclosedPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * <p>Open a new web window and populate it with a page loaded by
     * {@link #getPage(WebWindow,WebRequest)}</p>
     *
     * @param opener the web window that initiated the request
     * @param target the name of the window to be opened (the name that will be passed into the
     *        JavaScript <tt>open()</tt> method)
     * @param params any parameters
     * @param <P> the page type
     * @return the new page
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException if an IO problem occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P getPage(final WebWindow opener, final String target, final WebRequest params)
        throws FailingHttpStatusCodeException, IOException {
        return (P) getPage(openTargetWindow(opener, target, "_self"), params);
    }

    /**
     * Convenient method to build an URL and load it into the current WebWindow as it would be done
     * by {@link #getPage(WebWindow, WebRequest)}.
     * @param url the URL of the new content
     * @param <P> the page type
     * @return the new page
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException if an IO problem occurs
     * @throws MalformedURLException if no URL can be created from the provided string
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P getPage(final String url) throws IOException, FailingHttpStatusCodeException,
        MalformedURLException {
        return (P) getPage(UrlUtils.toUrlUnsafe(url));
    }

    /**
     * Convenient method to load a URL into the current top WebWindow as it would be done
     * by {@link #getPage(WebWindow, WebRequest)}.
     * @param url the URL of the new content
     * @param <P> the page type
     * @return the new page
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException if an IO problem occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P getPage(final URL url) throws IOException, FailingHttpStatusCodeException {
        return (P) getPage(getCurrentWindow().getTopWindow(), new WebRequest(url));
    }

    /**
     * Convenient method to load a web request into the current top WebWindow.
     * @param request the request parameters
     * @param <P> the page type
     * @return the new page
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException if an IO problem occurs
     * @see #getPage(WebWindow,WebRequest)
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P getPage(final WebRequest request) throws IOException,
        FailingHttpStatusCodeException {
        return (P) getPage(getCurrentWindow().getTopWindow(), request);
    }

    /**
     * <p>Creates a page based on the specified response and inserts it into the specified window. All page
     * initialization and event notification is handled here.</p>
     *
     * <p>Note that if the page created is an attachment page, and an {@link AttachmentHandler} has been
     * registered with this client, the page is <b>not</b> loaded into the specified window; in this case,
     * the page is loaded into a new window, and attachment handling is delegated to the registered
     * <tt>AttachmentHandler</tt>.</p>
     *
     * @param webResponse the response that will be used to create the new page
     * @param webWindow the window that the new page will be placed within
     * @throws IOException if an IO error occurs
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     * @return the newly created page
     * @see #setAttachmentHandler(AttachmentHandler)
     */
    public Page loadWebResponseInto(final WebResponse webResponse, final WebWindow webWindow)
        throws IOException, FailingHttpStatusCodeException {

        WebAssert.notNull("webResponse", webResponse);
        WebAssert.notNull("webWindow", webWindow);

        if (webResponse.getStatusCode() == HttpStatus.SC_NO_CONTENT) {
            return webWindow.getEnclosedPage();
        }

        if (attachmentHandler_ != null && Attachment.isAttachment(webResponse)) {
            final WebWindow w = openWindow(null, null, webWindow);
            final Page page = pageCreator_.createPage(webResponse, w);
            attachmentHandler_.handleAttachment(page);
            return page;
        }

        final Page oldPage = webWindow.getEnclosedPage();
        if (oldPage != null) {
            // Remove the old windows before create new ones.
            oldPage.cleanUp();
        }
        Page newPage = null;
        if (windows_.contains(webWindow) || getBrowserVersion().hasFeature(GENERATED_150)) {
            newPage = pageCreator_.createPage(webResponse, webWindow);

            if (windows_.contains(webWindow)) {
                fireWindowContentChanged(new WebWindowEvent(webWindow, WebWindowEvent.CHANGE, oldPage, newPage));

                // The page being loaded may already have been replaced by another page via JavaScript code.
                if (webWindow.getEnclosedPage() == newPage) {
                    newPage.initialize();
                    // hack: onload should be fired the same way for all type of pages
                    // here is a hack to handle non HTML pages
                    if (webWindow instanceof FrameWindow && !(newPage instanceof HtmlPage)) {
                        final FrameWindow fw = (FrameWindow) webWindow;
                        final BaseFrameElement frame = fw.getFrameElement();
                        if (frame.hasEventHandlers("onload")) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Executing onload handler for " + frame);
                            }
                            final Event event = new Event(frame, Event.TYPE_LOAD);
                            ((Node) frame.getScriptObject()).executeEvent(event);
                        }
                    }
                }
            }
        }
        return newPage;
    }

    /**
     * Specify whether or not the content of the resulting document will be
     * printed to the console in the event of a failing response code.
     * Successful response codes are in the range 200-299. The default is true.
     *
     * @param enabled True to enable this feature
     * @deprecated as of 2.11, please use {@link #getOptions()}.setPrintContentOnFailingStatusCode instead.
     */
    @Deprecated
    public void setPrintContentOnFailingStatusCode(final boolean enabled) {
        getOptions().setPrintContentOnFailingStatusCode(enabled);
    }

    /**
     * Returns <tt>true</tt> if the content of the resulting document will be printed to
     * the console in the event of a failing response code.
     *
     * @return <tt>true</tt> if the content of the resulting document will be printed to
     *         the console in the event of a failing response code
     * @see #setPrintContentOnFailingStatusCode
     * @deprecated as of 2.11, please use {@link #getOptions()}.getPrintContentOnFailingStatusCode instead.
     */
    @Deprecated
    public boolean getPrintContentOnFailingStatusCode() {
        return getOptions().getPrintContentOnFailingStatusCode();
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
            LOG.info("statusCode=[" + statusCode + "] contentType=[" + contentType + "]");
            LOG.info(webResponse.getContentAsString());
        }
    }

    /**
     * Specify whether or not an exception will be thrown in the event of a
     * failing status code. Successful status codes are in the range 200-299.
     * The default is true.
     *
     * @param enabled <tt>true</tt> to enable this feature
     * @deprecated as of 2.11, please use {@link #getOptions()}.setThrowExceptionOnFailingStatusCode instead.
     */
    @Deprecated
    public void setThrowExceptionOnFailingStatusCode(final boolean enabled) {
        getOptions().setThrowExceptionOnFailingStatusCode(enabled);
    }

    /**
     * Returns <tt>true</tt> if an exception will be thrown in the event of a failing response code.
     * @return <tt>true</tt> if an exception will be thrown in the event of a failing response code
     * @see #setThrowExceptionOnFailingStatusCode
     * @deprecated as of 2.11, please use {@link #getOptions()}.isThrowExceptionOnFailingStatusCode instead.
     */
    @Deprecated
    public boolean isThrowExceptionOnFailingStatusCode() {
        return getOptions().isThrowExceptionOnFailingStatusCode();
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
        final boolean successful = (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES)
            || statusCode == HttpStatus.SC_USE_PROXY
            || statusCode == HttpStatus.SC_NOT_MODIFIED;
        if (getOptions().isThrowExceptionOnFailingStatusCode() && !successful) {
            throw new FailingHttpStatusCodeException(webResponse);
        }
    }

    /**
     * Adds a header which will be sent with EVERY request from this client.
     * @param name the name of the header to add
     * @param value the value of the header to add
     * @see #removeRequestHeader(String)
     */
    public void addRequestHeader(final String name, final String value) {
        if ("cookie".equalsIgnoreCase(name)) {
            throw new IllegalArgumentException("Do not add 'Cookie' header, use .getCookieManager() instead");
        }
        requestHeaders_.put(name, value);
    }

    /**
     * Removes a header from being sent with EVERY request from this client.
     * @param name the name of the header to remove
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
     * @param credentialsProvider the new credentials provider to use to authenticate
     */
    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        WebAssert.notNull("credentialsProvider", credentialsProvider);
        credentialsProvider_ = credentialsProvider;
    }

    /**
     * Returns the credentials provider for this client instance. By default, this
     * method returns an instance of {@link DefaultCredentialsProvider}.
     * @return the credentials provider for this client instance
     */
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider_;
    }

    /**
     * This method is intended for testing only - use at your own risk.
     * @return the current JavaScript engine (never <code>null</code>)
     */
    public JavaScriptEngine getJavaScriptEngine() {
        return scriptEngine_;
    }

    /**
     * This method is intended for testing only - use at your own risk.
     *
     * @param engine  the new script engine to use
     */
    public void setJavaScriptEngine(final JavaScriptEngine engine) {
        if (engine == null) {
            throw new NullPointerException("Can't set JavaScriptEngine to null");
        }
        scriptEngine_ = engine;
    }

    /**
     * Enables/disables JavaScript support. By default, this property is enabled.
     *
     * @param enabled <tt>true</tt> to enable JavaScript support
     * @deprecated as of 2.11, please use {@link #getOptions()}.setJavaScriptEnabled instead.
     */
    @Deprecated
    public void setJavaScriptEnabled(final boolean enabled) {
        getOptions().setJavaScriptEnabled(enabled);
    }

    /**
     * Returns <tt>true</tt> if JavaScript is enabled and the script engine was loaded successfully.
     *
     * @return <tt>true</tt> if JavaScript is enabled
     * @deprecated as of 2.11, please use {@link #getOptions()}.isJavaScriptEnabled instead.
     */
    @Deprecated
    public boolean isJavaScriptEnabled() {
        return getOptions().isJavaScriptEnabled();
    }

    /**
     * Enables/disables CSS support. By default, this property is enabled.
     *
     * @param enabled <tt>true</tt> to enable CSS support
     * @deprecated as of 2.11, please use {@link #getOptions()}.setCssEnabled instead.
     */
    @Deprecated
    public void setCssEnabled(final boolean enabled) {
        getOptions().setCssEnabled(enabled);
    }

    /**
     * Returns <tt>true</tt> if CSS is enabled.
     *
     * @return <tt>true</tt> if CSS is enabled
     * @deprecated as of 2.11, please use {@link #getOptions()}.isCssEnabled instead.
     */
    @Deprecated
    public boolean isCssEnabled() {
        return getOptions().isCssEnabled();
    }

    /**
     * Enables/disables Applet support. By default, this property is disabled.<br/>
     * <p>
     * Note: as of HtmlUnit-2.4, Applet support is experimental and minimal
     * </p>
     * @param enabled <tt>true</tt> to enable Applet support
     * @since HtmlUnit-2.4
     * @deprecated as of 2.11, please use {@link #getOptions()}.setAppletEnabled instead.
     */
    @Deprecated
    public void setAppletEnabled(final boolean enabled) {
        getOptions().setAppletEnabled(enabled);
    }

    /**
     * Returns <tt>true</tt> if Applet are enabled.
     *
     * @return <tt>true</tt> if Applet is enabled
     * @deprecated as of 2.11, please use {@link #getOptions()}.isAppletEnabled instead.
     */
    @Deprecated
    public boolean isAppletEnabled() {
        return getOptions().isAppletEnabled();
    }

    /**
     * Enable/disable the popup window blocker. By default, the popup blocker is disabled, and popup
     * windows are allowed. When set to <tt>true</tt>, <tt>window.open()</tt> has no effect and
     * returns <tt>null</tt>.
     *
     * @param enabled <tt>true</tt> to enable the popup window blocker
     * @deprecated as of 2.11, please use {@link #getOptions()}.setPopupBlockerEnabled instead.
     */
    @Deprecated
    public void setPopupBlockerEnabled(final boolean enabled) {
        getOptions().setPopupBlockerEnabled(enabled);
    }

    /**
     * Returns <tt>true</tt> if the popup window blocker is enabled.
     *
     * @return <tt>true</tt> if the popup window blocker is enabled
     * @deprecated as of 2.11, please use {@link #getOptions()}.isPopupBlockerEnabled instead.
     */
    @Deprecated
    public boolean isPopupBlockerEnabled() {
        return getOptions().isPopupBlockerEnabled();
    }

    /**
     * Returns the client's current homepage.
     * @return the client's current homepage
     * @deprecated as of 2.11, please use {@link #getOptions()}.getHomePage instead.
     */
    @Deprecated
    public String getHomePage() {
        return getOptions().getHomePage();
    }

    /**
     * Sets the client's homepage.
     * @param homePage the new homepage URL
     * @deprecated as of 2.11, please use {@link #getOptions()}.setHomePage instead.
     */
    @Deprecated
    public void setHomePage(final String homePage) {
        getOptions().setHomePage(homePage);
    }

    /**
     * Returns the proxy configuration for this client.
     * @return the proxy configuration for this client
     * @deprecated as of 2.11, please use {@link #getOptions()}.getProxyConfig instead.
     */
    @Deprecated
    public ProxyConfig getProxyConfig() {
        return getOptions().getProxyConfig();
    }

    /**
     * Sets the proxy configuration for this client.
     * @param proxyConfig the proxy configuration for this client
     * @deprecated as of 2.11, please use {@link #getOptions()}.setProxyConfig instead.
     */
    @Deprecated
    public void setProxyConfig(final ProxyConfig proxyConfig) {
        getOptions().setProxyConfig(proxyConfig);
    }

    /**
     * Returns the cookie manager used by this web client.
     * @return the cookie manager used by this web client
     */
    public CookieManager getCookieManager() {
        return cookieManager_;
    }

    /**
     * Sets the cookie manager used by this web client.
     * @param cookieManager the cookie manager used by this web client
     */
    public void setCookieManager(final CookieManager cookieManager) {
        WebAssert.notNull("cookieManager", cookieManager);
        cookieManager_ = cookieManager;
    }

    /**
     * Sets the alert handler for this webclient.
     * @param alertHandler the new alerthandler or null if none is specified
     */
    public void setAlertHandler(final AlertHandler alertHandler) {
        alertHandler_ = alertHandler;
    }

    /**
     * Returns the alert handler for this webclient.
     * @return the alert handler or null if one hasn't been set
     */
    public AlertHandler getAlertHandler() {
        return alertHandler_;
    }

    /**
     * Sets the handler that will be executed when the JavaScript method Window.confirm() is called.
     * @param handler the new handler or null if no handler is to be used
     */
    public void setConfirmHandler(final ConfirmHandler handler) {
        confirmHandler_ = handler;
    }

    /**
     * Returns the confirm handler.
     * @return the confirm handler or null if one hasn't been set
     */
    public ConfirmHandler getConfirmHandler() {
        return confirmHandler_;
    }

    /**
     * Sets the handler that will be executed when the JavaScript method Window.prompt() is called.
     * @param handler the new handler or null if no handler is to be used
     */
    public void setPromptHandler(final PromptHandler handler) {
        promptHandler_ = handler;
    }

    /**
     * Returns the prompt handler.
     * @return the prompt handler or null if one hasn't been set
     */
    public PromptHandler getPromptHandler() {
        return promptHandler_;
    }

    /**
     * Sets the status handler for this webclient.
     * @param statusHandler the new status handler or null if none is specified
     */
    public void setStatusHandler(final StatusHandler statusHandler) {
        statusHandler_ = statusHandler;
    }

    /**
     * Returns the status handler for this webclient.
     * @return the status handler or null if one hasn't been set
     */
    public StatusHandler getStatusHandler() {
        return statusHandler_;
    }

    /**
     * Sets the javascript error listener for this webclient.
     * @param javaScriptErrorListener the new javaScriptErrorHandler or null if none is specified
     */
    public void setJavaScriptErrorListener(final JavaScriptErrorListener javaScriptErrorListener) {
        javaScriptErrorListener_ = javaScriptErrorListener;
    }

    /**
     * Returns the javascript error listener for this webclient.
     * @return the javascript error listener or null if one hasn't been set
     */
    public JavaScriptErrorListener getJavaScriptErrorListener() {
        return javaScriptErrorListener_;
    }

    /**
     * Returns the current browser version.
     * @return the current browser version
     */
    public BrowserVersion getBrowserVersion() {
        return browserVersion_;
    }

    /**
     * Returns the "current" window for this client. This window (or its top window) will be used
     * when <tt>getPage(...)</tt> is called without specifying a window.
     * @return the "current" window for this client
     */
    public WebWindow getCurrentWindow() {
        return currentWindow_;
    }

    /**
     * Sets the "current" window for this client. This is the window that will be used when
     * <tt>getPage(...)</tt> is called without specifying a window.
     * @param window the new "current" window for this client
     */
    public void setCurrentWindow(final WebWindow window) {
        WebAssert.notNull("window", window);
        if (currentWindow_ == window) {
            return;
        }
        //onBlur event is triggered for focused element of old current window
        if (currentWindow_ != null && !currentWindow_.isClosed()) {
            final Page enclosedPage = currentWindow_.getEnclosedPage();
            if (enclosedPage instanceof HtmlPage) {
                final HtmlElement focusedElement = ((HtmlPage) enclosedPage).getFocusedElement();
                if (focusedElement != null) {
                    focusedElement.fireEvent(Event.TYPE_BLUR);
                }
            }
        }
        currentWindow_ = window;
        //1. In IE activeElement becomes focused element for new current window
        //2. onFocus event is triggered for focusedElement of new current window
        final Page enclosedPage = currentWindow_.getEnclosedPage();
        if (enclosedPage instanceof HtmlPage) {
            final Window jsWindow = (Window) currentWindow_.getScriptObject();
            if (jsWindow != null) {
                if (getBrowserVersion().hasFeature(WINDOW_ACTIVE_ELEMENT_FOCUSED)) {
                    final HTMLElement activeElement =
                            (HTMLElement) ((HTMLDocument) jsWindow.getDocument()).getActiveElement();
                    if (activeElement != null) {
                        ((HtmlPage) enclosedPage).setFocusedElement(activeElement.getDomNodeOrDie(), true);
                    }
                }
                else {
                    final HtmlElement focusedElement = ((HtmlPage) enclosedPage).getFocusedElement();
                    if (focusedElement != null) {
                        ((HtmlPage) enclosedPage).setFocusedElement(focusedElement, true);
                    }
                }
            }
        }
    }

    /**
     * Adds a listener for {@link WebWindowEvent}s. All events from all windows associated with this
     * client will be sent to the specified listener.
     * @param listener a listener
     */
    public void addWebWindowListener(final WebWindowListener listener) {
        WebAssert.notNull("listener", listener);
        webWindowListeners_.add(listener);
    }

    /**
     * Removes a listener for {@link WebWindowEvent}s.
     * @param listener a listener
     */
    public void removeWebWindowListener(final WebWindowListener listener) {
        WebAssert.notNull("listener", listener);
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
     * Open a new window with the specified name. If the URL is non-null then attempt to load
     * a page from that location and put it in the new window.
     *
     * @param url the URL to load content from or null if no content is to be loaded
     * @param windowName the name of the new window
     * @return the new window
     */
    public WebWindow openWindow(final URL url, final String windowName) {
        WebAssert.notNull("windowName", windowName);
        return openWindow(url, windowName, getCurrentWindow());
    }

    /**
     * Open a new window with the specified name. If the URL is non-null then attempt to load
     * a page from that location and put it in the new window.
     *
     * @param url the URL to load content from or null if no content is to be loaded
     * @param windowName the name of the new window
     * @param opener the web window that is calling openWindow
     * @return the new window
     */
    public WebWindow openWindow(final URL url, final String windowName, final WebWindow opener) {
        final WebWindow window = openTargetWindow(opener, windowName, "_blank");
        final HtmlPage openerPage = (HtmlPage) opener.getEnclosedPage();
        if (url != null) {
            try {
                final WebRequest request = new WebRequest(url);
                if (getBrowserVersion().hasFeature(DIALOGWINDOW_REFERER)
                        && openerPage != null) {
                    final String referer = openerPage.getUrl().toExternalForm();
                    request.setAdditionalHeader("Referer", referer);
                }
                getPage(window, request);
            }
            catch (final IOException e) {
                LOG.error("Error loading content into window", e);
            }
        }
        else {
            initializeEmptyWindow(window);
            if (openerPage != null) {
                final Window jsWindow = (Window) window.getScriptObject();
                jsWindow.setDomNode(openerPage);
                jsWindow.getDocument().setDomNode(openerPage);
            }
        }
        return window;
    }

    /**
     * Open the window with the specified name. The name may be a special
     * target name of _self, _parent, _top, or _blank. An empty or null
     * name is set to the default. The special target names are relative to
     * the opener window.
     *
     * @param opener the web window that is calling openWindow
     * @param windowName the name of the new window
     * @param defaultName the default target if no name is given
     * @return the new window
     */
    private WebWindow openTargetWindow(
            final WebWindow opener, final String windowName, final String defaultName) {

        WebAssert.notNull("opener", opener);
        WebAssert.notNull("defaultName", defaultName);

        String windowToOpen = windowName;
        if (windowToOpen == null || windowToOpen.isEmpty()) {
            windowToOpen = defaultName;
        }

        WebWindow webWindow = resolveWindow(opener, windowToOpen);

        if (webWindow == null) {
            if ("_blank".equals(windowToOpen)) {
                windowToOpen = "";
            }
            webWindow = new TopLevelWindow(windowToOpen, this);
            fireWindowOpened(new WebWindowEvent(webWindow, WebWindowEvent.OPEN, null, null));
        }

        if (webWindow instanceof TopLevelWindow && webWindow != opener.getTopWindow()) {
            ((TopLevelWindow) webWindow).setOpener(opener);
        }

        return webWindow;
    }

    private WebWindow resolveWindow(final WebWindow opener, final String name) {
        if (name == null || name.isEmpty() || "_self".equals(name)) {
            return opener;
        }
        else if ("_parent".equals(name)) {
            return opener.getParentWindow();
        }
        else if ("_top".equals(name)) {
            return opener.getTopWindow();
        }
        else if ("_blank".equals(name)) {
            return null;
        }
        else if (!name.isEmpty()) {
            try {
                return getWebWindowByName(name);
            }
            catch (final WebWindowNotFoundException e) {
                // Fall through - a new window will be created below
            }
        }
        return null;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * Opens a new dialog window.
     * @param url the URL of the document to load and display
     * @param opener the web window that is opening the dialog
     * @param dialogArguments the object to make available inside the dialog via <tt>window.dialogArguments</tt>
     * @return the new dialog window
     * @throws IOException if there is an IO error
     */
    public DialogWindow openDialogWindow(final URL url, final WebWindow opener, final Object dialogArguments)
        throws IOException {

        WebAssert.notNull("url", url);
        WebAssert.notNull("opener", opener);

        final DialogWindow window = new DialogWindow(this, dialogArguments);
        fireWindowOpened(new WebWindowEvent(window, WebWindowEvent.OPEN, null, null));

        final HtmlPage openerPage = (HtmlPage) opener.getEnclosedPage();
        final WebRequest request = new WebRequest(url);
        if (getBrowserVersion().hasFeature(DIALOGWINDOW_REFERER) && openerPage != null) {
            final String referer = openerPage.getUrl().toExternalForm();
            request.setAdditionalHeader("Referer", referer);
        }

        getPage(window, request);

        return window;
    }

    /**
     * Sets whether or not redirections will be followed automatically on receipt of a redirect
     * status code from the server.
     * @param enabled true to enable automatic redirection
     * @deprecated as of 2.11, please use {@link #getOptions()}.setRedirectEnabled instead.
     */
    @Deprecated
    public void setRedirectEnabled(final boolean enabled) {
        getOptions().setRedirectEnabled(enabled);
    }

    /**
     * Returns whether or not redirections will be followed automatically on receipt of
     * a redirect status code from the server.
     * @return true if automatic redirection is enabled
     * @deprecated as of 2.11, please use {@link #getOptions()}.isRedirectEnabled instead.
     */
    @Deprecated
    public boolean isRedirectEnabled() {
        return getOptions().isRedirectEnabled();
    }

    /**
     * If set to <code>true</code>, the client will accept connections to any host, regardless of
     * whether they have valid certificates or not. This is especially useful when you are trying to
     * connect to a server with expired or corrupt certificates.
     * <p>
     * This method works only if {@link #getWebConnection()} returns an {@link HttpWebConnection}
     * (which is the default) or a {@link com.gargoylesoftware.htmlunit.util.WebConnectionWrapper}
     * wrapping an {@link HttpWebConnection}.
     * </p>
     * @param useInsecureSSL whether or not to use insecure SSL
     * @throws GeneralSecurityException if a security error occurs
     * @deprecated as of 2.11, please use {@link #getOptions()}.setUseInsecureSSL instead.
     */
    @Deprecated
    public void setUseInsecureSSL(final boolean useInsecureSSL) throws GeneralSecurityException {
        getOptions().setUseInsecureSSL(useInsecureSSL);
    }

    /**
     * Sets the object that will be used to create pages. Set this if you want
     * to customize the type of page that is returned for a given content type.
     *
     * @param pageCreator the new page creator
     */
    public void setPageCreator(final PageCreator pageCreator) {
        WebAssert.notNull("pageCreator", pageCreator);
        pageCreator_ = pageCreator;
    }

    /**
     * Returns the current page creator.
     *
     * @return the page creator
     */
    public PageCreator getPageCreator() {
        return pageCreator_;
    }

    /**
     * Returns the first {@link WebWindow} that matches the specified name.
     *
     * @param name the name to search for
     * @return the {@link WebWindow} with the specified name
     * @throws WebWindowNotFoundException if the {@link WebWindow} can't be found
     * @see #getWebWindows()
     * @see #getTopLevelWindows()
     */
    public WebWindow getWebWindowByName(final String name) throws WebWindowNotFoundException {
        WebAssert.notNull("name", name);

        for (final WebWindow webWindow : windows_) {
            if (webWindow.getName().equals(name)) {
                return webWindow;
            }
        }

        throw new WebWindowNotFoundException(name);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Initializes a new web window for JavaScript.
     * @param webWindow the new WebWindow
     */
    public void initialize(final WebWindow webWindow) {
        WebAssert.notNull("webWindow", webWindow);
        scriptEngine_.initialize(webWindow);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Initializes a new page for JavaScript.
     * @param newPage the new page
     */
    public void initialize(final Page newPage) {
        WebAssert.notNull("newPage", newPage);
        ((Window) newPage.getEnclosingWindow().getScriptObject()).initialize(newPage);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Initializes a new empty window for JavaScript.
     *
     * @param webWindow the new WebWindow
     */
    public void initializeEmptyWindow(final WebWindow webWindow) {
        WebAssert.notNull("webWindow", webWindow);
        initialize(webWindow);
        ((Window) webWindow.getScriptObject()).initialize();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Adds a new window to the list of available windows.
     *
     * @param webWindow the new WebWindow
     */
    public void registerWebWindow(final WebWindow webWindow) {
        WebAssert.notNull("webWindow", webWindow);
        windows_.add(webWindow);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Removes a window from the list of available windows.
     *
     * @param webWindow the window to remove
     */
    public void deregisterWebWindow(final WebWindow webWindow) {
        WebAssert.notNull("webWindow", webWindow);
        windows_.remove(webWindow);
        fireWindowClosed(new WebWindowEvent(webWindow, WebWindowEvent.CLOSE, webWindow.getEnclosedPage(), null));
    }

    /**
     * Expands a relative URL relative to the specified base. In most situations
     * this is the same as <code>new URL(baseUrl, relativeUrl)</code> but
     * there are some cases that URL doesn't handle correctly. See
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>
     * regarding Relative Uniform Resource Locators for more information.
     *
     * @param baseUrl the base URL
     * @param relativeUrl the relative URL
     * @return the expansion of the specified base and relative URLs
     * @throws MalformedURLException if an error occurred when creating a URL object
     */
    public static URL expandUrl(final URL baseUrl, final String relativeUrl) throws MalformedURLException {
        final String newUrl = UrlUtils.resolveUrl(baseUrl, relativeUrl);
        return UrlUtils.toUrlUnsafe(newUrl);
    }

    private WebResponse makeWebResponseForDataUrl(final WebRequest webRequest) throws IOException {
        final URL url = webRequest.getUrl();
        final List<NameValuePair> responseHeaders = new ArrayList<NameValuePair>();
        DataUrlDecoder decoder;
        try {
            decoder = DataUrlDecoder.decode(url);
        }
        catch (final DecoderException e) {
            throw new IOException(e.getMessage());
        }
        responseHeaders.add(new NameValuePair("content-type",
            decoder.getMediaType() + ";charset=" + decoder.getCharset()));
        final DownloadedContent downloadedContent = HttpWebConnection.downloadContent(url.openStream());
        final WebResponseData data = new WebResponseData(downloadedContent, 200, "OK", responseHeaders);
        return new WebResponse(data, url, webRequest.getHttpMethod(), 0);
    }

    private WebResponse makeWebResponseForAboutUrl(final URL url) {
        final String urlWithoutQuery = StringUtils.substringBefore(url.toExternalForm(), "?");
        if (!"blank".equalsIgnoreCase(StringUtils.substringAfter(urlWithoutQuery, "about:"))) {
            throw new IllegalArgumentException(url + " is not supported, only about:blank is supported now.");
        }
        return WEB_RESPONSE_FOR_ABOUT_BLANK;
    }

    /**
     * Builds a WebResponse for a file URL.
     * This first implementation is basic.
     * It assumes that the file contains an HTML page encoded with the specified encoding.
     * @param url the file URL
     * @param charset encoding to use
     * @return the web response
     * @throws IOException if an IO problem occurs
     */
    private WebResponse makeWebResponseForFileUrl(final WebRequest webRequest) throws IOException {
        URL cleanUrl = webRequest.getUrl();
        if (cleanUrl.getQuery() != null) {
            // Get rid of the query portion before trying to load the file.
            cleanUrl = UrlUtils.getUrlWithNewQuery(cleanUrl, null);
        }
        if (cleanUrl.getRef() != null) {
            // Get rid of the ref portion before trying to load the file.
            cleanUrl = UrlUtils.getUrlWithNewRef(cleanUrl, null);
        }

        final File file = FileUtils.toFile(cleanUrl);
        if (!file.exists()) {
            // construct 404
            final List<NameValuePair> compiledHeaders = new ArrayList<NameValuePair>();
            compiledHeaders.add(new NameValuePair("Content-Type", "text/html"));
            final WebResponseData responseData =
                new WebResponseData(
                    TextUtil.stringToByteArray("File: " + file.getAbsolutePath()), 404, "Not Found", compiledHeaders);
            return new WebResponse(responseData, webRequest, 0);
        }

        final String contentType = guessContentType(file);

        final DownloadedContent content = new DownloadedContent.OnFile(file, false);
        final List<NameValuePair> compiledHeaders = new ArrayList<NameValuePair>();
        compiledHeaders.add(new NameValuePair("Content-Type", contentType));
        final WebResponseData responseData = new WebResponseData(content, 200, "OK", compiledHeaders);
        return new WebResponse(responseData, webRequest, 0);
    }

    /**
     * Tries to guess the content type of the file.<br/>
     * This utility could be located in an helper class but we can compare this functionality
     * for instance with the "Helper Applications" settings of Mozilla and therefore see it as a
     * property of the "browser".
     * @param file the file
     * @return "application/octet-stream" if nothing could be guessed
     */
    public String guessContentType(final File file) {
        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        if (file.getName().endsWith(".xhtml")) {
            // Java's mime type map doesn't know about XHTML files (at least in Sun JDK5).
            contentType = "application/xhtml+xml";
        }
        if (contentType == null) {
            InputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(file));
                contentType = URLConnection.guessContentTypeFromStream(inputStream);
            }
            catch (final IOException e) {
                // Ignore silently.
            }
            finally {
                IOUtils.closeQuietly(inputStream);
            }
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
        final String charset) throws FailingHttpStatusCodeException, IOException {

        final HtmlPage page;
        if (webWindow instanceof FrameWindow) {
            final FrameWindow frameWindow = (FrameWindow) webWindow;
            page = frameWindow.getEnclosingPage();
        }
        else {
            Page currentPage = webWindow.getEnclosedPage();
            if (currentPage == null) {
                // Starting with a JavaScript URL; quickly fill an "about:blank".
                currentPage = getPage(webWindow, new WebRequest(WebClient.URL_ABOUT_BLANK));
            }
            page = (HtmlPage) currentPage;
        }

        final ScriptResult r = page.executeJavaScriptIfPossible(url.toExternalForm(), "JavaScript URL", 1);
        if ((r != null && r.getJavaScriptResult() == null) || ScriptResult.isUndefined(r)) {
            // No new WebResponse to produce.
            return webWindow.getEnclosedPage().getWebResponse();
        }

        final String contentString = r.getJavaScriptResult().toString();
        final StringWebResponse response = new StringWebResponse(contentString, charset, url);
        response.setFromJavascript(true);
        return response;
    }

    /**
     * Loads a {@link WebResponse} from the server.
     * @param webRequest the request
     * @throws IOException if an IO problem occurs
     * @return the WebResponse
     */
    public WebResponse loadWebResponse(final WebRequest webRequest)
        throws IOException {

        final WebResponse response;
        final String protocol = webRequest.getUrl().getProtocol();
        if ("about".equals(protocol)) {
            response = makeWebResponseForAboutUrl(webRequest.getUrl());
        }
        else if ("file".equals(protocol)) {
            response = makeWebResponseForFileUrl(webRequest);
        }
        else if ("data".equals(protocol)) {
            if (browserVersion_.hasFeature(PROTOCOL_DATA)) {
                response = makeWebResponseForDataUrl(webRequest);
            }
            else {
                throw new MalformedURLException("Unknown protocol: data");
            }
        }
        else {
            response = loadWebResponseFromWebConnection(webRequest, ALLOWED_REDIRECTIONS_SAME_URL);
        }

        return response;
    }

    /**
     * Loads a {@link WebResponse} from the server through the WebConnection.
     * @param webRequest the request
     * @param allowedRedirects the number of allowed redirects remaining
     * @throws IOException if an IO problem occurs
     * @return the resultant {@link WebResponse}
     */
    private WebResponse loadWebResponseFromWebConnection(final WebRequest webRequest,
        final int allowedRedirects) throws IOException {

        URL url = webRequest.getUrl();
        final HttpMethod method = webRequest.getHttpMethod();
        final List<NameValuePair> parameters = webRequest.getRequestParameters();

        WebAssert.notNull("url", url);
        WebAssert.notNull("method", method);
        WebAssert.notNull("parameters", parameters);

        url = UrlUtils.encodeUrl(url, getBrowserVersion().hasFeature(URL_MINIMAL_QUERY_ENCODING));
        webRequest.setUrl(url);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Load response for " + method + " " + url.toExternalForm());
        }

        // If the request settings don't specify a custom proxy, use the default client proxy...
        if (webRequest.getProxyHost() == null) {
            final ProxyConfig proxyConfig = getOptions().getProxyConfig();
            if (proxyConfig.getProxyAutoConfigUrl() != null) {
                if (!proxyConfig.getProxyAutoConfigUrl().equals(url.toExternalForm())) {
                    String content = proxyConfig.getProxyAutoConfigContent();
                    if (content == null) {
                        content = getPage(proxyConfig.getProxyAutoConfigUrl())
                            .getWebResponse().getContentAsString();
                        proxyConfig.setProxyAutoConfigContent(content);
                    }
                    final String allValue = ProxyAutoConfig.evaluate(content, url);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Proxy Auto-Config: value '" + allValue + "' for URL " + url);
                    }
                    String value = allValue.split(";")[0].trim();
                    if (value.startsWith("PROXY")) {
                        value = value.substring(6);
                        final int colonIndex = value.indexOf(':');
                        webRequest.setSocksProxy(false);
                        webRequest.setProxyHost(value.substring(0, colonIndex));
                        webRequest.setProxyPort(Integer.parseInt(value.substring(colonIndex + 1)));
                    }
                    else if (value.startsWith("SOCKS")) {
                        value = value.substring(6);
                        final int colonIndex = value.indexOf(':');
                        webRequest.setSocksProxy(true);
                        webRequest.setProxyHost(value.substring(0, colonIndex));
                        webRequest.setProxyPort(Integer.parseInt(value.substring(colonIndex + 1)));
                    }
                }
            }
            // ...unless the host needs to bypass the configured client proxy!
            else if (!proxyConfig.shouldBypassProxy(webRequest.getUrl().getHost())) {
                webRequest.setProxyHost(proxyConfig.getProxyHost());
                webRequest.setProxyPort(proxyConfig.getProxyPort());
                webRequest.setSocksProxy(proxyConfig.isSocksProxy());
            }
        }

        // Add the headers that are sent with every request.
        addDefaultHeaders(webRequest);

        // Retrieve the response, either from the cache or from the server.
        final Object fromCache = getCache().getCachedObject(webRequest);
        final WebResponse webResponse;
        if (fromCache != null && fromCache instanceof WebResponse) {
            webResponse = new WebResponseFromCache((WebResponse) fromCache, webRequest);
        }
        else {
            webResponse = getWebConnection().getResponse(webRequest);
            getCache().cacheIfPossible(webRequest, webResponse, webResponse);
        }

        // Continue according to the HTTP status code.
        final int status = webResponse.getStatusCode();
        if (status == HttpStatus.SC_USE_PROXY) {
            getIncorrectnessListener().notify("Ignoring HTTP status code [305] 'Use Proxy'", this);
        }
        else if (status >= HttpStatus.SC_MOVED_PERMANENTLY
            && status <= HttpStatus.SC_TEMPORARY_REDIRECT
            && status != HttpStatus.SC_NOT_MODIFIED
            && isRedirectEnabled()) {

            final URL newUrl;
            String locationString = null;
            try {
                locationString = webResponse.getResponseHeaderValue("Location");
                if (locationString == null) {
                    return webResponse;
                }
                newUrl = expandUrl(url, locationString);
            }
            catch (final MalformedURLException e) {
                getIncorrectnessListener().notify("Got a redirect status code [" + status + " "
                    + webResponse.getStatusMessage()
                    + "] but the location is not a valid URL [" + locationString
                    + "]. Skipping redirection processing.", this);
                return webResponse;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Got a redirect status code [" + status + "] new location = [" + locationString + "]");
            }

            if (allowedRedirects == 0) {
                throw new FailingHttpStatusCodeException("Too much redirect for "
                    + webResponse.getWebRequest().getUrl(), webResponse);
            }
            else if ((status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_TEMPORARY_REDIRECT)
                && method == HttpMethod.GET) {
                final WebRequest wrs = new WebRequest(newUrl);
                wrs.setRequestParameters(parameters);
                for (final Map.Entry<String, String> entry : webRequest.getAdditionalHeaders().entrySet()) {
                    wrs.setAdditionalHeader(entry.getKey(), entry.getValue());
                }
                return loadWebResponseFromWebConnection(wrs, allowedRedirects - 1);
            }
            else if (status <= HttpStatus.SC_SEE_OTHER) {
                final WebRequest wrs = new WebRequest(newUrl);
                wrs.setHttpMethod(HttpMethod.GET);
                for (final Map.Entry<String, String> entry : webRequest.getAdditionalHeaders().entrySet()) {
                    wrs.setAdditionalHeader(entry.getKey(), entry.getValue());
                }
                return loadWebResponseFromWebConnection(wrs, allowedRedirects - 1);
            }
        }

        return webResponse;
    }

    /**
     * Adds the headers that are sent with every request to the specified {@link WebRequest} instance.
     * @param wrs the <tt>WebRequestSettings</tt> instance to modify
     */
    private void addDefaultHeaders(final WebRequest wrs) {
        // Add standard HtmlUnit headers.
        if (!wrs.isAdditionalHeader("Accept-Language")) {
            wrs.setAdditionalHeader("Accept-Language", getBrowserVersion().getBrowserLanguage());
        }
        // Add user-specified headers last so that they can override HtmlUnit defaults.
        wrs.getAdditionalHeaders().putAll(requestHeaders_);
    }

    /**
     * Returns an immutable list of open web windows (whether they are top level windows or not).
     * @return an immutable list of open web windows (whether they are top level windows or not)
     * @see #getWebWindowByName(String)
     * @see #getTopLevelWindows()
     */
    public List<WebWindow> getWebWindows() {
        return Collections.unmodifiableList(windows_);
    }

    /**
     * Returns an immutable list of open top level windows.
     * @return an immutable list of open top level windows
     * @see #getWebWindowByName(String)
     * @see #getWebWindows()
     */
    public List<TopLevelWindow> getTopLevelWindows() {
        return Collections.unmodifiableList(topLevelWindows_);
    }

    /**
     * Sets the handler to be used whenever a refresh is triggered. Refer
     * to the documentation for {@link RefreshHandler} for more details.
     * @param handler the new handler
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
     * Returns the current refresh handler or null if one has not been set.
     * @return the current RefreshHandler or null
     */
    public RefreshHandler getRefreshHandler() {
        return refreshHandler_;
    }

    /**
     * Sets the script pre processor for this webclient.
     * @param scriptPreProcessor the new preprocessor or null if none is specified
     */
    public void setScriptPreProcessor(final ScriptPreProcessor scriptPreProcessor) {
        scriptPreProcessor_ = scriptPreProcessor;
    }

    /**
     * Returns the script pre processor for this webclient.
     * @return the pre processor or null of one hasn't been set
     */
    public ScriptPreProcessor getScriptPreProcessor() {
        return scriptPreProcessor_;
    }

    /**
     * Sets the active X object map for this webclient. The <code>Map</code> is used to map the
     * string passed into the <code>ActiveXObject</code> constructor to a java class name. Therefore
     * you can emulate <code>ActiveXObject</code>s in a web page's JavaScript by mapping the object
     * name to a java class to emulate the active X object.
     * @param activeXObjectMap the new preprocessor or null if none is specified
     */
    public void setActiveXObjectMap(final Map<String, String> activeXObjectMap) {
        activeXObjectMap_ = activeXObjectMap;
    }

    /**
     * Returns the active X object map for this webclient.
     * @return the active X object map
     */
    public Map<String, String> getActiveXObjectMap() {
        return activeXObjectMap_;
    }

    /**
     * Sets whether to allow native ActiveX or no. Default value is false.
     * Beware that you should never allow running native ActiveX components unless you fully trust
     * the JavaScript code, as it is not controlled by the Java Virtual Machine.
     *
     * @param allow whether to allow or no
     * @deprecated as of 2.11, please use {@link #getOptions()}.setActiveXNative instead.
     */
    @Deprecated
    public void setActiveXNative(final boolean allow) {
        getOptions().setActiveXNative(allow);
    }

    /**
     * Returns whether native ActiveX components are allowed or no.
     * @return whether native ActiveX components are allowed or no
     * @deprecated as of 2.11, please use {@link #getOptions()}.isActiveXNative instead.
     */
    @Deprecated
    public boolean isActiveXNative() {
        return getOptions().isActiveXNative();
    }

    /**
     * Sets the listener for messages generated by the HTML parser.
     * @param listener the new listener, <code>null</code> if messages should be totally ignored
     */
    public void setHTMLParserListener(final HTMLParserListener listener) {
        htmlParserListener_ = listener;
    }

    /**
     * Gets the configured listener for messages generated by the HTML parser.
     * @return <code>null</code> if no listener is defined (default value)
     */
    public HTMLParserListener getHTMLParserListener() {
        return htmlParserListener_;
    }

    /**
     * Returns the CSS error handler used by this web client when CSS problems are encountered.
     * @return the CSS error handler used by this web client when CSS problems are encountered
     * @see DefaultCssErrorHandler
     * @see SilentCssErrorHandler
     */
    public ErrorHandler getCssErrorHandler() {
        return cssErrorHandler_;
    }

    /**
     * Sets the CSS error handler used by this web client when CSS problems are encountered.
     * @param cssErrorHandler the CSS error handler used by this web client when CSS problems are encountered
     * @see DefaultCssErrorHandler
     * @see SilentCssErrorHandler
     */
    public void setCssErrorHandler(final ErrorHandler cssErrorHandler) {
        WebAssert.notNull("cssErrorHandler", cssErrorHandler);
        cssErrorHandler_ = cssErrorHandler;
    }

    /**
     * Sets the number of milliseconds that a script is allowed to execute before being terminated.
     * A value of 0 or less means no timeout.
     *
     * @param timeout the timeout value, in milliseconds
     */
    public void setJavaScriptTimeout(final long timeout) {
        scriptEngine_.getContextFactory().setTimeout(timeout);
    }

    /**
     * Returns the number of milliseconds that a script is allowed to execute before being terminated.
     * A value of 0 or less means no timeout.
     *
     * @return the timeout value, in milliseconds
     */
    public long getJavaScriptTimeout() {
        return scriptEngine_.getContextFactory().getTimeout();
    }

    /**
     * Gets the timeout value for the {@link WebConnection}.
     *
     * @return the timeout value in milliseconds
     * @see WebClient#setTimeout(int)
     * @deprecated as of 2.11, please use {@link #getOptions()}.{@link WebClientOptions#getTimeout getTimeout()}
     * instead.
     */
    @Deprecated
    public int getTimeout() {
        return getOptions().getTimeout();
    }

    /**
     * <p>Sets the timeout of the {@link WebConnection}. Set to zero (the default) for an infinite wait.</p>
     *
     * <p>Note: The timeout is used twice. The first is for making the socket connection, the second is
     * for data retrieval. If the time is critical you must allow for twice the time specified here.</p>
     *
     * @param timeout the value of the timeout in milliseconds
     * @deprecated as of 2.11, please use {@link #getOptions()}.{@link WebClientOptions#setTimeout setTimeout()}
     * instead.
     */
    @Deprecated
    public void setTimeout(final int timeout) {
        getOptions().setTimeout(timeout);
    }

    /**
     * Indicates if an exception should be thrown when a script execution fails
     * (the default) or if it should be caught and just logged to allow page
     * execution to continue.
     * @return <code>true</code> if an exception is thrown on script error (the default)
     * @deprecated as of 2.11, please use {@link #getOptions()}.isThrowExceptionOnScriptError instead.
     */
    @Deprecated
    public boolean isThrowExceptionOnScriptError() {
        return getOptions().isThrowExceptionOnScriptError();
    }

    /**
     * Changes the behavior of this webclient when a script error occurs.
     * @param newValue indicates if exception should be thrown or not
     * @deprecated as of 2.11, please use {@link #getOptions()}.setThrowExceptionOnScriptError instead.
     */
    @Deprecated
    public void setThrowExceptionOnScriptError(final boolean newValue) {
        getOptions().setThrowExceptionOnScriptError(newValue);
    }

    /**
     * Gets the current listener for encountered incorrectness (except HTML parsing messages that
     * are handled by the HTML parser listener). Default value is an instance of
     * {@link IncorrectnessListenerImpl}.
     * @return the current listener (not <code>null</code>)
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
     * Returns the WebConsole.
     * @return the web console
     */
    public WebConsole getWebConsole() {
        if (webConsole_ == null) {
            webConsole_ = new WebConsole();
        }
        return webConsole_;
    }

    /**
     * Gets the current AJAX controller.
     * @return the controller
     */
    public AjaxController getAjaxController() {
        return ajaxController_;
    }

    /**
     * Sets the current AJAX controller.
     * @param newValue the controller
     */
    public void setAjaxController(final AjaxController newValue) {
        if (newValue == null) {
            throw new NullPointerException();
        }
        ajaxController_ = newValue;
    }

    /**
     * Sets the attachment handler.
     * @param handler the new attachment handler
     */
    public void setAttachmentHandler(final AttachmentHandler handler) {
        attachmentHandler_ = handler;
    }

    /**
     * Returns the current attachment handler.
     * @return the current attachment handler
     */
    public AttachmentHandler getAttachmentHandler() {
        return attachmentHandler_;
    }

    /**
     * Sets the onbeforeunload handler for this webclient.
     * @param onbeforeunloadHandler the new onbeforeunloadHandler or null if none is specified
     */
    public void setOnbeforeunloadHandler(final OnbeforeunloadHandler onbeforeunloadHandler) {
        onbeforeunloadHandler_ = onbeforeunloadHandler;
    }

    /**
     * Returns the onbeforeunload handler for this webclient.
     * @return the onbeforeunload handler or null if one hasn't been set
     */
    public OnbeforeunloadHandler getOnbeforeunloadHandler() {
        return onbeforeunloadHandler_;
    }

    /**
     * Gets the cache currently being used.
     * @return the cache (may not be null)
     */
    public Cache getCache() {
        return cache_;
    }

    /**
     * Sets the cache to use.
     * @param cache the new cache (must not be <code>null</code>)
     */
    public void setCache(final Cache cache) {
        if (cache == null) {
            throw new IllegalArgumentException("cache should not be null!");
        }
        cache_ = cache;
    }

    /**
     * Keeps track of the current window. Inspired by WebTest's logic to track the current response.
     */
    private static final class CurrentWindowTracker implements WebWindowListener, Serializable {
        private final WebClient webClient_;

        private CurrentWindowTracker(final WebClient webClient) {
            webClient_ = webClient;
        }

        /**
         * {@inheritDoc}
         */
        public void webWindowClosed(final WebWindowEvent event) {
            final WebWindow window = event.getWebWindow();
            if (window instanceof TopLevelWindow) {
                webClient_.topLevelWindows_.remove(window);
                if (window == webClient_.getCurrentWindow()) {
                    if (webClient_.topLevelWindows_.isEmpty()) {
                        // Must always have at least window, and there are no top-level windows left; must create one.
                        final TopLevelWindow newWindow = new TopLevelWindow("", webClient_);
                        webClient_.topLevelWindows_.push(newWindow);
                        webClient_.setCurrentWindow(newWindow);
                    }
                    else {
                        // The current window is now the previous top-level window.
                        webClient_.setCurrentWindow(webClient_.topLevelWindows_.peek());
                    }
                }
            }
            else if (window == webClient_.getCurrentWindow()) {
                // The current window is now the last top-level window.
                webClient_.setCurrentWindow(webClient_.topLevelWindows_.peek());
            }
        }
        /**
         * {@inheritDoc}
         */
        public void webWindowContentChanged(final WebWindowEvent event) {
            final WebWindow window = event.getWebWindow();
            boolean use = false;
            if (window instanceof DialogWindow) {
                use = true;
            }
            else if (window instanceof TopLevelWindow) {
                use = (event.getOldPage() == null);
            }
            else if (window instanceof FrameWindow) {
                final FrameWindow fw = (FrameWindow) window;
                final String enclosingPageState = fw.getEnclosingPage().getDocumentElement().getReadyState();
                final URL frameUrl = fw.getEnclosedPage().getUrl();
                if (!DomNode.READY_STATE_COMPLETE.equals(enclosingPageState) || frameUrl == URL_ABOUT_BLANK) {
                    return;
                }

                // now looks at the visibility of the frame window
                final BaseFrameElement frameElement = fw.getFrameElement();
                if (frameElement.isDisplayed()) {
                    final ScriptableObject scriptableObject = frameElement.getScriptObject();
                    final ComputedCSSStyleDeclaration style = ((HTMLElement) scriptableObject).getCurrentStyle();
                    use = (style.getCalculatedWidth(false, false) != 0)
                        && (style.getCalculatedHeight(false, false) != 0);
                }
            }
            if (use) {
                webClient_.setCurrentWindow(window);
            }
        }
        /**
         * {@inheritDoc}
         */
        public void webWindowOpened(final WebWindowEvent event) {
            final WebWindow window = event.getWebWindow();
            if (window instanceof TopLevelWindow) {
                final TopLevelWindow tlw = (TopLevelWindow) window;
                webClient_.topLevelWindows_.push(tlw);
            }
            // Page is not loaded yet, don't set it now as current window.
        }
    }

    /**
     * Closes all opened windows, stopping all background JavaScript processing.
     */
    public void closeAllWindows() {
        if (scriptEngine_ != null) {
            scriptEngine_.shutdownJavaScriptExecutor();
        }
        // NB: this implementation is too simple as a new TopLevelWindow may be opened by
        // some JS script while we are closing the others
        final List<TopLevelWindow> topWindows = new ArrayList<TopLevelWindow>();
        for (final WebWindow window : topLevelWindows_) {
            if (window instanceof TopLevelWindow) {
                topWindows.add((TopLevelWindow) window);
            }
        }
        for (final TopLevelWindow topWindow : topWindows) {
            if (topLevelWindows_.contains(topWindow)) {
                topWindow.close();
            }
        }
        //FIXME Depends on the implementation
        if (webConnection_ instanceof HttpWebConnection) {
            ((HttpWebConnection) webConnection_).shutdown();
        }
    }

    /**
     * <p><span style="color:red">Experimental API: May be changed in next release
     * and may not yet work perfectly!</span></p>
     *
     * <p>This method blocks until all background JavaScript tasks have finished executing. Background
     * JavaScript tasks are JavaScript tasks scheduled for execution via <tt>window.setTimeout</tt>,
     * <tt>window.setInterval</tt> or asynchronous <tt>XMLHttpRequest</tt>.</p>
     *
     * <p>If a job is scheduled to begin executing after <tt>(now + timeoutMillis)</tt>, this method will
     * wait for <tt>timeoutMillis</tt> milliseconds and then return a value greater than <tt>0</tt>. This
     * method will never block longer than <tt>timeoutMillis</tt> milliseconds.</p>
     *
     * <p>Use this method instead of {@link #waitForBackgroundJavaScriptStartingBefore(long)} if you
     * don't know when your background JavaScript is supposed to start executing, but you're fairly sure
     * that you know how long it should take to finish executing.</p>
     *
     * @param timeoutMillis the maximum amount of time to wait (in milliseconds)
     * @return the number of background JavaScript jobs still executing or waiting to be executed when this
     *         method returns; will be <tt>0</tt> if there are no jobs left to execute
     */
    public int waitForBackgroundJavaScript(final long timeoutMillis) {
        int count = 0;
        final long endTime = System.currentTimeMillis() + timeoutMillis;
        for (Iterator<WebWindow> i = windows_.iterator(); i.hasNext();) {
            final WebWindow window;
            try {
                window = i.next();
            }
            catch (final ConcurrentModificationException e) {
                i = windows_.iterator();
                count = 0;
                continue;
            }
            final long newTimeout = endTime - System.currentTimeMillis();
            count += window.getJobManager().waitForJobs(newTimeout);
        }
        if (count != getAggregateJobCount()) {
            final long newTimeout = endTime - System.currentTimeMillis();
            return waitForBackgroundJavaScript(newTimeout);
        }
        return count;
    }

    /**
     * <p><span style="color:red">Experimental API: May be changed in next release
     * and may not yet work perfectly!</span></p>
     *
     * <p>This method blocks until all background JavaScript tasks scheduled to start executing before
     * <tt>(now + delayMillis)</tt> have finished executing. Background JavaScript tasks are JavaScript
     * tasks scheduled for execution via <tt>window.setTimeout</tt>, <tt>window.setInterval</tt> or
     * asynchronous <tt>XMLHttpRequest</tt>.</p>
     *
     * <p>If there is no background JavaScript task currently executing, and there is no background JavaScript
     * task scheduled to start executing within the specified time, this method returns immediately -- even
     * if there are tasks scheduled to be executed after <tt>(now + delayMillis)</tt>.</p>
     *
     * <p>Note that the total time spent executing a background JavaScript task is never known ahead of
     * time, so this method makes no guarantees as to how long it will block.</p>
     *
     * <p>Use this method instead of {@link #waitForBackgroundJavaScript(long)} if you know roughly when
     * your background JavaScript is supposed to start executing, but you're not necessarily sure how long
     * it will take to execute.</p>
     *
     * @param delayMillis the delay which determines the background tasks to wait for (in milliseconds)
     * @return the number of background JavaScript jobs still executing or waiting to be executed when this
     *         method returns; will be <tt>0</tt> if there are no jobs left to execute
     */
    public int waitForBackgroundJavaScriptStartingBefore(final long delayMillis) {
        int count = 0;
        final long endTime = System.currentTimeMillis() + delayMillis;
        for (Iterator<WebWindow> i = windows_.iterator(); i.hasNext();) {
            final WebWindow window;
            try {
                window = i.next();
            }
            catch (final ConcurrentModificationException e) {
                i = windows_.iterator();
                count = 0;
                continue;
            }
            final long newDelay = endTime - System.currentTimeMillis();
            count += window.getJobManager().waitForJobsStartingBefore(newDelay);
        }
        if (count != getAggregateJobCount()) {
            final long newDelay = endTime - System.currentTimeMillis();
            return waitForBackgroundJavaScriptStartingBefore(newDelay);
        }
        return count;
    }

    /**
     * Sets the SSL client certificate to use.
     * The needed parameters are used to construct a {@link java.security.KeyStore}.
     *
     * If the web server requires Renegotiation, you have to set sytem property
     * "sun.security.ssl.allowUnsafeRenegotiation" to true, as hinted in
     * <a href="http://www.oracle.com/technetwork/java/javase/documentation/tlsreadme2-176330.html">
     * TLS Renegotiation Issue</a>.
     *
     * @param certificateUrl the URL which locates the certificate
     * @param certificatePassword the certificate password
     * @param certificateType the type of certificate, usually "jks" or "pkcs12".
     * @deprecated as of 2.11, please use {@link #getOptions()}.setSSLClientCertificate instead.
     */
    @Deprecated
    public void setSSLClientCertificate(final URL certificateUrl, final String certificatePassword,
            final String certificateType) {
        getOptions().setSSLClientCertificate(certificateUrl, certificatePassword, certificateType);
    }

    /**
     * Returns the aggregate background JavaScript job count across all windows.
     * @return the aggregate background JavaScript job count across all windows
     */
    private int getAggregateJobCount() {
        int count = 0;
        for (Iterator<WebWindow> i = windows_.iterator(); i.hasNext();) {
            final WebWindow window;
            try {
                window = i.next();
            }
            catch (final ConcurrentModificationException e) {
                i = windows_.iterator();
                count = 0;
                continue;
            }
            count += window.getJobManager().getJobCount();
        }
        return count;
    }

    /**
     * When we deserialize, re-initializie transient fields.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        webConnection_ = createWebConnection();
        scriptEngine_ = new JavaScriptEngine(this);
    }

    private WebConnection createWebConnection() {
        if (GAEUtils.isGaeMode()) {
            return new UrlFetchWebConnection(this);
        }

        return new HttpWebConnection(this);
    }

    private static class LoadJob {
        private final WebWindow requestingWindow_;
        private final String target_;
        private final WebResponse response_;
        private final URL urlWithOnlyHashChange_;
        private final WeakReference<Page> originalPage_;

        LoadJob(final WebWindow requestingWindow, final String target, final WebResponse response) {
            requestingWindow_ = requestingWindow;
            target_ = target;
            response_ = response;
            urlWithOnlyHashChange_ = null;
            originalPage_ = new WeakReference<Page>(requestingWindow.getEnclosedPage());
        }
        LoadJob(final WebWindow requestingWindow, final String target, final URL urlWithOnlyHashChange) {
            requestingWindow_ = requestingWindow;
            target_ = target;
            response_ = null;
            urlWithOnlyHashChange_ = urlWithOnlyHashChange;
            originalPage_ = new WeakReference<Page>(requestingWindow.getEnclosedPage());
        }
        public boolean isOutdated() {
            if (target_ != null && !target_.isEmpty()) {
                return false;
            }
            else if (requestingWindow_.isClosed()) {
                return true;
            }
            else if (requestingWindow_.getEnclosedPage() != originalPage_.get()) {
                return true;
            }

            return false;
        }
    }

    private final List<LoadJob> loadQueue_ = new ArrayList<LoadJob>();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Perform the downloads and stores it for loading later into a window.
     * In the future downloads should be performed in parallel in separated threads.
     * TODO: refactor it before next release.
     * @param requestingWindow the window from which the request comes
     * @param target the name of the target window
     * @param request the request to perform
     * @param isHashJump in at least one case (anchor url is '#') it is not possible
     *        to decide that this is only a hash jump; in this case you can provide
     *        true here; otherwise use false
     * @param description information about the origin of the request. Useful for debugging.
     */
    public void download(final WebWindow requestingWindow, final String target,
        final WebRequest request, final boolean isHashJump, final String description) {
        final WebWindow win = resolveWindow(requestingWindow, target);
        final URL url = request.getUrl();
        boolean justHashJump = isHashJump;

        if (win != null && HttpMethod.POST != request.getHttpMethod()) {
            final Page page = win.getEnclosedPage();
            if (page instanceof HtmlPage && !((HtmlPage) page).isOnbeforeunloadAccepted()) {
                return;
            }
            final URL current = page.getUrl();
            if (!justHashJump && url.sameFile(current) && StringUtils.isNotEmpty(url.getRef())) {
                justHashJump = true;
            }
        }

        synchronized (loadQueue_) {
            // verify if this load job doesn't already exist
            for (final LoadJob loadJob : loadQueue_) {
                if (loadJob.response_ == null) {
                    continue;
                }
                final WebRequest otherRequest = loadJob.response_.getWebRequest();
                final URL otherUrl = otherRequest.getUrl();
                // TODO: investigate but it seems that IE considers query string too but not FF
                if (url.getPath().equals(otherUrl.getPath())
                    && url.getHost().equals(otherUrl.getHost())
                    && url.getProtocol().equals(otherUrl.getProtocol())
                    && url.getPort() == otherUrl.getPort()
                    && request.getHttpMethod() == otherRequest.getHttpMethod()) {
                    return; // skip it;
                }
            }
        }

        final LoadJob loadJob;
        if (justHashJump) {
            loadJob = new LoadJob(win, target, url);
        }
        else {
            try {
                final WebResponse response = loadWebResponse(request);
                loadJob = new LoadJob(requestingWindow, target, response);
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        synchronized (loadQueue_) {
            loadQueue_.add(loadJob);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Loads downloaded responses into the corresponding windows.
     * TODO: refactor it before next release.
     * @throws IOException in case of exception
     * @throws FailingHttpStatusCodeException in case of exception
     */
    public void loadDownloadedResponses() throws FailingHttpStatusCodeException, IOException {
        final List<LoadJob> queue;

        // synchronize access to the loadQueue_,
        // to be sure no job is ignored
        synchronized (loadQueue_) {
            if (loadQueue_.isEmpty()) {
                return;
            }
            queue = new ArrayList<LoadJob>(loadQueue_);
            loadQueue_.clear();
        }

        final HashSet<WebWindow> updatedWindows = new HashSet<WebWindow>();
        for (int i = queue.size() - 1; i >= 0; --i) {
            final LoadJob downloadedResponse = queue.get(i);
            if (downloadedResponse.isOutdated()) {
                LOG.info("No usage of download: " + downloadedResponse);
                continue;
            }
            if (downloadedResponse.urlWithOnlyHashChange_ != null) {
                final WebWindow window = downloadedResponse.requestingWindow_;
                final HtmlPage page = (HtmlPage) window.getEnclosedPage();
                page.getWebResponse().getWebRequest().setUrl(downloadedResponse.urlWithOnlyHashChange_);
                window.getHistory().addPage(page);

                // update location.hash
                final Window jsWindow = (Window) window.getScriptObject();
                if (null != jsWindow) {
                    final Location location = jsWindow.getLocation();
                    location.setHash(downloadedResponse.urlWithOnlyHashChange_.getRef());
                }
            }
            else {
                final WebWindow window = resolveWindow(downloadedResponse.requestingWindow_,
                    downloadedResponse.target_);
                if (!updatedWindows.contains(window)) {
                    final WebWindow win = openTargetWindow(downloadedResponse.requestingWindow_,
                            downloadedResponse.target_, "_self");
                    final Page pageBeforeLoad = win.getEnclosedPage();
                    loadWebResponseInto(downloadedResponse.response_, win);

                    // start execution here.
                    if (scriptEngine_ != null) {
                        scriptEngine_.registerWindowAndMaybeStartEventLoop(win);
                    }

                    // check and report problems if needed
                    throwFailingHttpStatusCodeExceptionIfNecessary(downloadedResponse.response_);

                    if (pageBeforeLoad != win.getEnclosedPage()) {
                        updatedWindows.add(win);
                    }
                }
                else {
                    LOG.info("No usage of download: " + downloadedResponse);
                }
            }
        }
    }

    /**
     * Returns the options object of this WebClient.
     * @return the options object
     */
    public WebClientOptions getOptions() {
        return options_;
    }
}
