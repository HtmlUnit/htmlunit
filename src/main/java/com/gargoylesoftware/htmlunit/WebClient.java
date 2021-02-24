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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CONTENT_SECURITY_POLICY_IGNORED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.DIALOGWINDOW_REFERER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTTP_HEADER_SEC_FETCH;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTTP_HEADER_UPGRADE_INSECURE_REQUEST;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTTP_REDIRECT_WITHOUT_HASH;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.URL_MINIMAL_QUERY_ENCODING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.WINDOW_EXECUTE_EVENTS;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;

import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLActiveXObjectFactory;
import com.gargoylesoftware.htmlunit.attachment.AttachmentHandler;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.FrameWindow.PageDenied;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserListener;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitBrowserCompatCookieSpec;
import com.gargoylesoftware.htmlunit.javascript.AbstractJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement;
import com.gargoylesoftware.htmlunit.protocol.data.DataURLConnection;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.TextUtils;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.htmlunit.webstart.WebStartHandler;
import com.shapesecurity.salvation2.Policy;
import com.shapesecurity.salvation2.URLs.URI;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

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
 * final WebClient webClient = new WebClient();<br>
 * final {@link HtmlPage} startPage = webClient.getPage("http://htmlunit.sf.net");<br>
 * assertEquals("HtmlUnit - Welcome to HtmlUnit", startPage.{@link HtmlPage#getTitleText() getTitleText}());
 * </code>
 * <p>
 * Note: a {@link WebClient} instance is <b>not thread safe</b>. It is intended to be used from a single thread.
 * </p>
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
 * @author Frank Danek
 * @author Joerg Werner
 * @author Anton Demydenko
 */
public class WebClient implements Serializable, AutoCloseable {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(WebClient.class);

    /** Like the Firefox default value for {@code network.http.redirection-limit}. */
    private static final int ALLOWED_REDIRECTIONS_SAME_URL = 20;
    private static final WebResponseData RESPONSE_DATA_NO_HTTP_RESPONSE = new WebResponseData(
            0, "No HTTP Response", Collections.<NameValuePair>emptyList());

    private transient WebConnection webConnection_;
    private CredentialsProvider credentialsProvider_ = new DefaultCredentialsProvider();
    private CookieManager cookieManager_ = new CookieManager();
    private transient AbstractJavaScriptEngine<?> scriptEngine_;
    private transient List<LoadJob> loadQueue_;
    private final Map<String, String> requestHeaders_ = Collections.synchronizedMap(new HashMap<String, String>(89));
    private IncorrectnessListener incorrectnessListener_ = new IncorrectnessListenerImpl();
    private WebConsole webConsole_;
    private transient ExecutorService executor_;

    private AlertHandler alertHandler_;
    private ConfirmHandler confirmHandler_;
    private PromptHandler promptHandler_;
    private StatusHandler statusHandler_;
    private AttachmentHandler attachmentHandler_;
    private WebStartHandler webStartHandler_;
    private FrameContentHandler frameContentHandler_;
    private AppletConfirmHandler appletConfirmHandler_;
    private AjaxController ajaxController_ = new AjaxController();

    private BrowserVersion browserVersion_;
    private PageCreator pageCreator_ = new DefaultPageCreator();

    private final Set<WebWindowListener> webWindowListeners_ = new HashSet<>(5);
    private final List<TopLevelWindow> topLevelWindows_ =
            Collections.synchronizedList(new ArrayList<TopLevelWindow>()); // top-level windows
    private final List<WebWindow> windows_ = Collections.synchronizedList(new ArrayList<WebWindow>()); // all windows
    private transient List<WeakReference<JavaScriptJobManager>> jobManagers_ =
            Collections.synchronizedList(new ArrayList<WeakReference<JavaScriptJobManager>>());
    private WebWindow currentWindow_;

    private HTMLParserListener htmlParserListener_;
    private CSSErrorHandler cssErrorHandler_ = new DefaultCssErrorHandler();
    private OnbeforeunloadHandler onbeforeunloadHandler_;
    private Cache cache_ = new Cache();

    /** target "_blank". */
    private static final String TARGET_BLANK = "_blank";
    /** target "_parent". */
    private static final String TARGET_SELF = "_self";
    /** target "_parent". */
    private static final String TARGET_PARENT = "_parent";
    /** target "_top". */
    private static final String TARGET_TOP = "_top";

    /**
     * "about:".
     * @deprecated as of version 2.47.0; use UrlUtils.ABOUT_BLANK instead
     */
    @Deprecated
    public static final String ABOUT_SCHEME = UrlUtils.ABOUT_SCHEME;

    /**
     * "about:blank".
     * @deprecated as of version 2.47.0; use UrlUtils.ABOUT_BLANK instead
     */
    @Deprecated
    public static final String ABOUT_BLANK = UrlUtils.ABOUT_BLANK;

    /**
     * URL for "about:blank".
     * @deprecated as of version 2.47.0; use UrlUtils.URL_ABOUT_BLANK instead
     */
    @Deprecated
    public static final URL URL_ABOUT_BLANK = UrlUtils.URL_ABOUT_BLANK;

    private ScriptPreProcessor scriptPreProcessor_;

    private Map<String, String> activeXObjectMap_ = Collections.emptyMap();
    private transient MSXMLActiveXObjectFactory msxmlActiveXObjectFactory_;
    private RefreshHandler refreshHandler_ = new NiceRefreshHandler(2);
    private JavaScriptErrorListener javaScriptErrorListener_ = new DefaultJavaScriptErrorListener();

    private WebClientOptions options_ = new WebClientOptions();
    private final boolean javaScriptEngineEnabled_;
    private WebClientInternals internals_ = new WebClientInternals();
    private final StorageHolder storageHolder_ = new StorageHolder();

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
        this(browserVersion, null, -1);
    }

    /**
     * Creates an instance that will use the specified {@link BrowserVersion} and proxy server.
     * @param browserVersion the browser version to simulate
     * @param proxyHost the server that will act as proxy or null for no proxy
     * @param proxyPort the port to use on the proxy server
     */
    public WebClient(final BrowserVersion browserVersion, final String proxyHost, final int proxyPort) {
        this(browserVersion, true, proxyHost, proxyPort);
    }

    /**
     * Creates an instance that will use the specified {@link BrowserVersion} and proxy server.
     * @param browserVersion the browser version to simulate
     * @param javaScriptEngineEnabled set to false if the simulated browser should not support javaScript
     * @param proxyHost the server that will act as proxy or null for no proxy
     * @param proxyPort the port to use on the proxy server
     */
    public WebClient(final BrowserVersion browserVersion, final boolean javaScriptEngineEnabled,
            final String proxyHost, final int proxyPort) {
        WebAssert.notNull("browserVersion", browserVersion);

        browserVersion_ = browserVersion;
        javaScriptEngineEnabled_ = javaScriptEngineEnabled;

        if (proxyHost == null) {
            getOptions().setProxyConfig(new ProxyConfig());
        }
        else {
            getOptions().setProxyConfig(new ProxyConfig(proxyHost, proxyPort));
        }

        webConnection_ = new HttpWebConnection(this); // this has to be done after the browser version was set
        if (javaScriptEngineEnabled_) {
            scriptEngine_ = new JavaScriptEngine(this);
        }
        loadQueue_ = new ArrayList<>();

        // The window must be constructed AFTER the script engine.
        addWebWindowListener(new CurrentWindowTracker(this));
        currentWindow_ = new TopLevelWindow("", this);

        if (isJavaScriptEnabled()) {
            fireWindowOpened(new WebWindowEvent(currentWindow_, WebWindowEvent.OPEN, null, null));

            if (getBrowserVersion().hasFeature(JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
                initMSXMLActiveX();
            }
        }
    }

    /**
     * Our simple impl of a ThreadFactory (decorator) to be able to name
     * our threads.
     */
    private static final class ThreadNamingFactory implements ThreadFactory {
        private static int ID_ = 1;
        private ThreadFactory baseFactory_;

        ThreadNamingFactory(final ThreadFactory aBaseFactory) {
            baseFactory_ = aBaseFactory;
        }

        @Override
        public Thread newThread(final Runnable aRunnable) {
            final Thread thread = baseFactory_.newThread(aRunnable);
            thread.setName("WebClient Thread " + ID_++);
            return thread;
        }
    }

    private void initMSXMLActiveX() {
        msxmlActiveXObjectFactory_ = new MSXMLActiveXObjectFactory();
        // TODO [IE] initialize in #init or in #initialize?
        try {
            msxmlActiveXObjectFactory_.init(getBrowserVersion());
        }
        catch (final Exception e) {
            LOG.error("Exception while initializing MSXML ActiveX for the page", e);
            throw new ScriptException(null, e); // BUG: null is not useful.
        }
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
     *         {@link WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     *
     * @see WebRequest
     */
    public <P extends Page> P getPage(final WebWindow webWindow, final WebRequest webRequest)
            throws IOException, FailingHttpStatusCodeException {
        return getPage(webWindow, webRequest, true);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
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
     * @param addToHistory true if the page should be part of the history
     * @param <P> the page type
     * @return the page returned by the server when the specified request was made in the specified window
     * @throws IOException if an IO error occurs
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     *
     * @see WebRequest
     */
    @SuppressWarnings("unchecked")
    <P extends Page> P getPage(final WebWindow webWindow, final WebRequest webRequest,
            final boolean addToHistory)
        throws IOException, FailingHttpStatusCodeException {

        final Page page = webWindow.getEnclosedPage();

        if (page != null) {
            final URL prev = page.getUrl();
            final URL current = webRequest.getUrl();
            if (UrlUtils.sameFile(current, prev)
                        && current.getRef() != null
                        && !StringUtils.equals(current.getRef(), prev.getRef())) {
                // We're just navigating to an anchor within the current page.
                page.getWebResponse().getWebRequest().setUrl(current);
                if (addToHistory) {
                    webWindow.getHistory().addPage(page);
                }

                final Window window = webWindow.getScriptableObject();
                if (window != null) { // js enabled
                    window.getLocation().setHash(current.getRef());
                    window.clearComputedStyles();
                }
                return (P) page;
            }

            if (page.isHtmlPage()) {
                final HtmlPage htmlPage = (HtmlPage) page;
                if (!htmlPage.isOnbeforeunloadAccepted()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("The registered OnbeforeunloadHandler rejected to load a new page.");
                    }
                    return (P) page;
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get page for window named '" + webWindow.getName() + "', using " + webRequest);
        }

        WebResponse webResponse;
        final String protocol = webRequest.getUrl().getProtocol();
        if ("javascript".equals(protocol)) {
            webResponse = makeWebResponseForJavaScriptUrl(webWindow, webRequest.getUrl(), webRequest.getCharset());
            if (webWindow.getEnclosedPage() != null && webWindow.getEnclosedPage().getWebResponse() == webResponse) {
                // a javascript:... url with result of type undefined didn't changed the page
                return (P) webWindow.getEnclosedPage();
            }
        }
        else {
            try {
                webResponse = loadWebResponse(webRequest);
            }
            catch (final NoHttpResponseException e) {
                webResponse = new WebResponse(RESPONSE_DATA_NO_HTTP_RESPONSE, webRequest, 0);
            }
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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
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
     *         {@link WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException if an IO problem occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P getPage(final WebWindow opener, final String target, final WebRequest params)
        throws FailingHttpStatusCodeException, IOException {
        return (P) getPage(openTargetWindow(opener, target, TARGET_SELF), params);
    }

    /**
     * Convenient method to build a URL and load it into the current WebWindow as it would be done
     * by {@link #getPage(WebWindow, WebRequest)}.
     * @param url the URL of the new content
     * @param <P> the page type
     * @return the new page
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
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
     *         {@link WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException if an IO problem occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P getPage(final URL url) throws IOException, FailingHttpStatusCodeException {
        final WebRequest request = new WebRequest(url, getBrowserVersion().getHtmlAcceptHeader(),
                                                          getBrowserVersion().getAcceptEncodingHeader());
        request.setCharset(UTF_8);

        return (P) getPage(getCurrentWindow().getTopWindow(), request);
    }

    /**
     * Convenient method to load a web request into the current top WebWindow.
     * @param request the request parameters
     * @param <P> the page type
     * @return the new page
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
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
     *         {@link WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is set to true
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

        if (webStartHandler_ != null && "application/x-java-jnlp-file".equals(webResponse.getContentType())) {
            webStartHandler_.handleJnlpResponse(webResponse);
            return webWindow.getEnclosedPage();
        }

        if (attachmentHandler_ != null && attachmentHandler_.isAttachment(webResponse)) {
            if (attachmentHandler_.handleAttachment(webResponse)) {
                // the handling is done by the attachment handler;
                // do not open a new window
                return webWindow.getEnclosedPage();
            }

            final WebWindow w = openWindow(null, null, webWindow);
            final Page page = pageCreator_.createPage(webResponse, w);
            attachmentHandler_.handleAttachment(page);
            return page;
        }

        final Page oldPage = webWindow.getEnclosedPage();
        if (oldPage != null) {
            // Remove the old page before create new one.
            oldPage.cleanUp();
        }

        Page newPage = null;
        FrameWindow.PageDenied pageDenied = PageDenied.NONE;
        if (windows_.contains(webWindow) || getBrowserVersion().hasFeature(WINDOW_EXECUTE_EVENTS)) {
            if (webWindow instanceof FrameWindow) {
                final String contentSecurityPolicy =
                        webResponse.getResponseHeaderValue(HttpHeader.CONTENT_SECURIRY_POLICY);
                if (StringUtils.isNotBlank(contentSecurityPolicy)
                        && !getBrowserVersion().hasFeature(CONTENT_SECURITY_POLICY_IGNORED)) {
                    final URL origin = UrlUtils.getUrlWithoutPathRefQuery(
                            ((FrameWindow) webWindow).getEnclosingPage().getUrl());
                    final URL source = UrlUtils.getUrlWithoutPathRefQuery(webResponse.getWebRequest().getUrl());
                    final Policy policy = Policy.parseSerializedCSP(contentSecurityPolicy,
                                                    Policy.PolicyErrorConsumer.ignored);
                    if (!policy.allowsFrameAncestor(
                            Optional.of(URI.parseURI(source.toExternalForm()).orElse(null)),
                            Optional.of(URI.parseURI(origin.toExternalForm()).orElse(null)))) {
                        pageDenied = PageDenied.BY_CONTENT_SECURIRY_POLICY;

                        if (LOG.isWarnEnabled()) {
                            LOG.warn("Load denied by Content-Security-Policy: '" + contentSecurityPolicy + "' - "
                                    + webResponse.getWebRequest().getUrl() + "' does not permit framing.");
                        }
                    }
                }

                if (pageDenied == PageDenied.NONE) {
                    final String xFrameOptions = webResponse.getResponseHeaderValue(HttpHeader.X_FRAME_OPTIONS);
                    if ("DENY".equalsIgnoreCase(xFrameOptions)) {
                        pageDenied = PageDenied.BY_X_FRAME_OPTIONS;

                        if (LOG.isWarnEnabled()) {
                            LOG.warn("Load denied by X-Frame-Options: DENY; - '"
                                    + webResponse.getWebRequest().getUrl() + "' does not permit framing.");
                        }
                    }
                }
            }

            if (pageDenied == PageDenied.NONE) {
                newPage = pageCreator_.createPage(webResponse, webWindow);
            }
            else {
                try {
                    final WebResponse aboutBlank = loadWebResponse(WebRequest.newAboutBlankRequest());
                    newPage = pageCreator_.createPage(aboutBlank, webWindow);
                    // TODO - maybe we have to attach to original request/response to the page

                    ((FrameWindow) webWindow).setPageDenied(pageDenied);
                }
                catch (final IOException e) {
                    // ignore
                }
            }

            if (windows_.contains(webWindow)) {
                fireWindowContentChanged(new WebWindowEvent(webWindow, WebWindowEvent.CHANGE, oldPage, newPage));

                // The page being loaded may already have been replaced by another page via JavaScript code.
                if (webWindow.getEnclosedPage() == newPage) {
                    newPage.initialize();
                    // hack: onload should be fired the same way for all type of pages
                    // here is a hack to handle non HTML pages
                    if (isJavaScriptEnabled()
                            && webWindow instanceof FrameWindow && !newPage.isHtmlPage()) {
                        final FrameWindow fw = (FrameWindow) webWindow;
                        final BaseFrameElement frame = fw.getFrameElement();
                        if (frame.hasEventHandlers("onload")) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Executing onload handler for " + frame);
                            }
                            final Event event = new Event(frame, Event.TYPE_LOAD);
                            ((Node) frame.getScriptableObject()).executeEventLocally(event);
                        }
                    }
                }
            }
        }
        return newPage;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     *
     * <p>Logs the response's content if its status code indicates a request failure and
     * {@link WebClientOptions#isPrintContentOnFailingStatusCode()} returns {@code true}.
     *
     * @param webResponse the response whose content may be logged
     */
    public void printContentIfNecessary(final WebResponse webResponse) {
        if (getOptions().isPrintContentOnFailingStatusCode()) {
            final int statusCode = webResponse.getStatusCode();
            final boolean successful = statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES;
            if (!successful && LOG.isInfoEnabled()) {
                final String contentType = webResponse.getContentType();
                LOG.info("statusCode=[" + statusCode + "] contentType=[" + contentType + "]");
                LOG.info(webResponse.getContentAsString());
            }
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     *
     * <p>Throws a {@link FailingHttpStatusCodeException} if the request's status code indicates a request
     * failure and {@link WebClientOptions#isThrowExceptionOnFailingStatusCode()} returns {@code true}.
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
        if (HttpHeader.COOKIE_LC.equalsIgnoreCase(name)) {
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
     * @return the current JavaScript engine (never {@code null})
     */
    public AbstractJavaScriptEngine<?> getJavaScriptEngine() {
        return scriptEngine_;
    }

    /**
     * This method is intended for testing only - use at your own risk.
     *
     * @param engine the new script engine to use
     */
    public void setJavaScriptEngine(final AbstractJavaScriptEngine<?> engine) {
        if (engine == null) {
            throw new IllegalArgumentException("Can't set JavaScriptEngine to null");
        }
        scriptEngine_ = engine;
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
     * Returns the executor for this webclient.
     * @return the executor
     */
    public synchronized Executor getExecutor() {
        if (executor_ == null) {
            final ThreadPoolExecutor tmpThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            tmpThreadPool.setThreadFactory(new ThreadNamingFactory(tmpThreadPool.getThreadFactory()));
            // tmpThreadPool.prestartAllCoreThreads();
            executor_ = tmpThreadPool;
        }

        return executor_;
    }

    /**
     * Sets the javascript error listener for this webclient.
     * When setting to null, the {@link DefaultJavaScriptErrorListener} is used.
     * @param javaScriptErrorListener the new JavaScriptErrorListener or null if none is specified
     */
    public void setJavaScriptErrorListener(final JavaScriptErrorListener javaScriptErrorListener) {
        if (javaScriptErrorListener == null) {
            javaScriptErrorListener_ = new DefaultJavaScriptErrorListener();
        }
        else {
            javaScriptErrorListener_ = javaScriptErrorListener;
        }
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
        // onBlur event is triggered for focused element of old current window
        if (currentWindow_ != null && !currentWindow_.isClosed()) {
            final Page enclosedPage = currentWindow_.getEnclosedPage();
            if (enclosedPage != null && enclosedPage.isHtmlPage()) {
                final DomElement focusedElement = ((HtmlPage) enclosedPage).getFocusedElement();
                if (focusedElement != null) {
                    focusedElement.fireEvent(Event.TYPE_BLUR);
                }
            }
        }
        currentWindow_ = window;

        // when marking an iframe window as current we have no need to move the focus
        final boolean isIFrame = currentWindow_ instanceof FrameWindow
                && ((FrameWindow) currentWindow_).getFrameElement() instanceof HtmlInlineFrame;
        if (!isIFrame) {
            //1. activeElement becomes focused element for new current window
            //2. onFocus event is triggered for focusedElement of new current window
            final Page enclosedPage = currentWindow_.getEnclosedPage();
            if (enclosedPage != null && enclosedPage.isHtmlPage()) {
                final Window jsWindow = currentWindow_.getScriptableObject();
                if (jsWindow != null) {
                    final HTMLElement activeElement =
                            ((HTMLDocument) jsWindow.getDocument()).getActiveElement();
                    if (activeElement != null) {
                        ((HtmlPage) enclosedPage).setFocusedElement(activeElement.getDomNodeOrDie(), true);
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
        for (final WebWindowListener listener : new ArrayList<>(webWindowListeners_)) {
            listener.webWindowContentChanged(event);
        }
    }

    private void fireWindowOpened(final WebWindowEvent event) {
        for (final WebWindowListener listener : new ArrayList<>(webWindowListeners_)) {
            listener.webWindowOpened(event);
        }
    }

    private void fireWindowClosed(final WebWindowEvent event) {
        for (final WebWindowListener listener : new ArrayList<>(webWindowListeners_)) {
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
        final WebWindow window = openTargetWindow(opener, windowName, TARGET_BLANK);
        if (url == null) {
            initializeEmptyWindow(window, window.getEnclosedPage());
        }
        else {
            try {
                final WebRequest request = new WebRequest(url, getBrowserVersion().getHtmlAcceptHeader(),
                                                                getBrowserVersion().getAcceptEncodingHeader());
                request.setCharset(UTF_8);

                final Page openerPage = opener.getEnclosedPage();
                if (getBrowserVersion().hasFeature(DIALOGWINDOW_REFERER)
                        && openerPage != null
                        && openerPage.getUrl() != null) {
                    request.setRefererlHeader(openerPage.getUrl());
                }
                getPage(window, request);
            }
            catch (final IOException e) {
                LOG.error("Error loading content into window", e);
            }
        }
        return window;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
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
    public WebWindow openTargetWindow(
            final WebWindow opener, final String windowName, final String defaultName) {

        WebAssert.notNull("opener", opener);
        WebAssert.notNull("defaultName", defaultName);

        String windowToOpen = windowName;
        if (windowToOpen == null || windowToOpen.isEmpty()) {
            windowToOpen = defaultName;
        }

        WebWindow webWindow = resolveWindow(opener, windowToOpen);

        if (webWindow == null) {
            if (TARGET_BLANK.equals(windowToOpen)) {
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
        if (name == null || name.isEmpty() || TARGET_SELF.equals(name)) {
            return opener;
        }

        if (TARGET_PARENT.equals(name)) {
            return opener.getParentWindow();
        }

        if (TARGET_TOP.equals(name)) {
            return opener.getTopWindow();
        }

        if (TARGET_BLANK.equals(name)) {
            return null;
        }

        // first search for frame windows inside our window hierarchy
        WebWindow window = opener;
        while (true) {
            final Page page = window.getEnclosedPage();
            if (page != null && page.isHtmlPage()) {
                try {
                    final FrameWindow frame = ((HtmlPage) page).getFrameByName(name);
                    final ScriptableObject scriptable = frame.getFrameElement().getScriptableObject();
                    if (scriptable instanceof HTMLIFrameElement) {
                        ((HTMLIFrameElement) scriptable).onRefresh();
                    }
                    return frame;
                }
                catch (final ElementNotFoundException e) {
                    // Fall through
                }
            }

            if (window == window.getParentWindow()) {
                // TODO: should getParentWindow() return null on top windows?
                break;
            }
            window = window.getParentWindow();
        }

        try {
            return getWebWindowByName(name);
        }
        catch (final WebWindowNotFoundException e) {
            // Fall through - a new window will be created below
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
        final WebRequest request = new WebRequest(url, getBrowserVersion().getHtmlAcceptHeader(),
                                                        getBrowserVersion().getAcceptEncodingHeader());
        request.setCharset(UTF_8);

        if (getBrowserVersion().hasFeature(DIALOGWINDOW_REFERER) && openerPage != null) {
            request.setRefererlHeader(openerPage.getUrl());
        }

        getPage(window, request);

        return window;
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
            if (name.equals(webWindow.getName())) {
                return webWindow;
            }
        }

        throw new WebWindowNotFoundException(name);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Initializes a new web window for JavaScript.
     * @param webWindow the new WebWindow
     * @param page the page that will become the enclosing page
     */
    public void initialize(final WebWindow webWindow, final Page page) {
        WebAssert.notNull("webWindow", webWindow);

        if (isJavaScriptEngineEnabled()) {
            scriptEngine_.initialize(webWindow, page);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Initializes a new empty window for JavaScript.
     *
     * @param webWindow the new WebWindow
     * @param page the page that will become the enclosing page
     */
    public void initializeEmptyWindow(final WebWindow webWindow, final Page page) {
        WebAssert.notNull("webWindow", webWindow);

        if (isJavaScriptEngineEnabled()) {
            initialize(webWindow, page);
            ((Window) webWindow.getScriptableObject()).initialize();
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Adds a new window to the list of available windows.
     *
     * @param webWindow the new WebWindow
     */
    public void registerWebWindow(final WebWindow webWindow) {
        WebAssert.notNull("webWindow", webWindow);
        windows_.add(webWindow);
        // register JobManager here but don't deregister in deregisterWebWindow as it can live longer
        jobManagers_.add(new WeakReference<>(webWindow.getJobManager()));
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Removes a window from the list of available windows.
     *
     * @param webWindow the window to remove
     */
    public void deregisterWebWindow(final WebWindow webWindow) {
        WebAssert.notNull("webWindow", webWindow);
        if (windows_.remove(webWindow)) {
            fireWindowClosed(new WebWindowEvent(webWindow, WebWindowEvent.CLOSE, webWindow.getEnclosedPage(), null));
        }
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
        final List<NameValuePair> responseHeaders = new ArrayList<>();
        final DataURLConnection connection;
        try {
            connection = new DataURLConnection(url);
        }
        catch (final DecoderException e) {
            throw new IOException(e.getMessage());
        }
        responseHeaders.add(new NameValuePair(HttpHeader.CONTENT_TYPE_LC,
            connection.getMediaType() + ";charset=" + connection.getCharset()));

        try (InputStream is = connection.getInputStream()) {
            final DownloadedContent downloadedContent =
                    HttpWebConnection.downloadContent(is, getOptions().getMaxInMemory());
            final WebResponseData data = new WebResponseData(downloadedContent, 200, "OK", responseHeaders);
            return new WebResponse(data, url, webRequest.getHttpMethod(), 0);
        }
    }

    private static WebResponse makeWebResponseForAboutUrl(final WebRequest webRequest) throws MalformedURLException {
        final URL url = webRequest.getUrl();
        final String urlString = url.toExternalForm();
        if (UrlUtils.ABOUT_BLANK.equalsIgnoreCase(urlString)) {
            return new StringWebResponse("", UrlUtils.URL_ABOUT_BLANK);
        }

        final String urlWithoutQuery = StringUtils.substringBefore(urlString, "?");
        if (!"blank".equalsIgnoreCase(StringUtils.substringAfter(urlWithoutQuery, UrlUtils.ABOUT_SCHEME))) {
            throw new MalformedURLException(url + " is not supported, only about:blank is supported at the moment.");
        }
        return new StringWebResponse("", url);
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

        final WebResponse fromCache = getCache().getCachedResponse(webRequest);
        if (fromCache != null) {
            return new WebResponseFromCache(fromCache, webRequest);
        }

        String fileUrl = cleanUrl.toExternalForm();
        fileUrl = URLDecoder.decode(fileUrl, UTF_8.name());
        final File file = new File(fileUrl.substring(5));
        if (!file.exists()) {
            // construct 404
            final List<NameValuePair> compiledHeaders = new ArrayList<>();
            compiledHeaders.add(new NameValuePair(HttpHeader.CONTENT_TYPE, MimeType.TEXT_HTML));
            final WebResponseData responseData =
                new WebResponseData(
                    TextUtils.stringToByteArray("File: " + file.getAbsolutePath(), UTF_8),
                    404, "Not Found", compiledHeaders);
            return new WebResponse(responseData, webRequest, 0);
        }

        final String contentType = guessContentType(file);

        final DownloadedContent content = new DownloadedContent.OnFile(file, false);
        final List<NameValuePair> compiledHeaders = new ArrayList<>();
        compiledHeaders.add(new NameValuePair(HttpHeader.CONTENT_TYPE, contentType));
        compiledHeaders.add(new NameValuePair(HttpHeader.LAST_MODIFIED,
                DateUtils.formatDate(new Date(file.lastModified()))));
        final WebResponseData responseData = new WebResponseData(content, 200, "OK", compiledHeaders);
        final WebResponse webResponse = new WebResponse(responseData, webRequest, 0);
        getCache().cacheIfPossible(webRequest, webResponse, null);
        return webResponse;
    }

    /**
     * Tries to guess the content type of the file.<br>
     * This utility could be located in a helper class but we can compare this functionality
     * for instance with the "Helper Applications" settings of Mozilla and therefore see it as a
     * property of the "browser".
     * @param file the file
     * @return "application/octet-stream" if nothing could be guessed
     */
    public String guessContentType(final File file) {
        final String fileName = file.getName();
        if (fileName.endsWith(".xhtml")) {
            // Java's mime type map returns application/xml in JDK8.
            return MimeType.APPLICATION_XHTML;
        }

        // Java's mime type map does not know these in JDK8.
        if (fileName.endsWith(".js")) {
            return MimeType.APPLICATION_JAVASCRIPT;
        }
        if (fileName.toLowerCase(Locale.ROOT).endsWith(".css")) {
            return MimeType.TEXT_CSS;
        }

        String contentType = URLConnection.guessContentTypeFromName(fileName);
        if (contentType == null) {
            try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                contentType = URLConnection.guessContentTypeFromStream(inputStream);
            }
            catch (final IOException e) {
                // Ignore silently.
            }
        }
        if (contentType == null) {
            contentType = MimeType.APPLICATION_OCTET_STREAM;
        }
        return contentType;
    }

    private WebResponse makeWebResponseForJavaScriptUrl(final WebWindow webWindow, final URL url,
        final Charset charset) throws FailingHttpStatusCodeException, IOException {

        HtmlPage page = null;
        if (webWindow instanceof FrameWindow) {
            final FrameWindow frameWindow = (FrameWindow) webWindow;
            page = (HtmlPage) frameWindow.getEnclosedPage();
        }
        else {
            final Page currentPage = webWindow.getEnclosedPage();
            if (currentPage instanceof HtmlPage) {
                page = (HtmlPage) currentPage;
            }
        }

        if (page == null) {
            page = getPage(webWindow, WebRequest.newAboutBlankRequest());
        }
        final ScriptResult r = page.executeJavaScript(url.toExternalForm(), "JavaScript URL", 1);
        if (r.getJavaScriptResult() == null || ScriptResult.isUndefined(r)) {
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
    public WebResponse loadWebResponse(final WebRequest webRequest) throws IOException {
        switch (webRequest.getUrl().getProtocol()) {
            case UrlUtils.ABOUT:
                return makeWebResponseForAboutUrl(webRequest);

            case "file":
                return makeWebResponseForFileUrl(webRequest);

            case "data":
                return makeWebResponseForDataUrl(webRequest);

            default:
                return loadWebResponseFromWebConnection(webRequest, ALLOWED_REDIRECTIONS_SAME_URL);
        }
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

        url = UrlUtils.encodeUrl(url, getBrowserVersion().hasFeature(URL_MINIMAL_QUERY_ENCODING),
                                        webRequest.getCharset());
        webRequest.setUrl(url);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Load response for " + method + " " + url.toExternalForm());
        }

        // If the request settings don't specify a custom proxy, use the default client proxy...
        if (webRequest.getProxyHost() == null) {
            final ProxyConfig proxyConfig = getOptions().getProxyConfig();
            if (proxyConfig.getProxyAutoConfigUrl() != null) {
                if (!UrlUtils.sameFile(new URL(proxyConfig.getProxyAutoConfigUrl()), url)) {
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
        final WebResponse fromCache = getCache().getCachedResponse(webRequest);
        final WebResponse webResponse;
        if (fromCache == null) {
            webResponse = getWebConnection().getResponse(webRequest);
        }
        else {
            webResponse = new WebResponseFromCache(fromCache, webRequest);
        }

        // Continue according to the HTTP status code.
        final int status = webResponse.getStatusCode();
        if (status == HttpStatus.SC_USE_PROXY) {
            getIncorrectnessListener().notify("Ignoring HTTP status code [305] 'Use Proxy'", this);
        }
        else if (status >= HttpStatus.SC_MOVED_PERMANENTLY
            && status <= 308
            && status != HttpStatus.SC_NOT_MODIFIED
            && getOptions().isRedirectEnabled()) {

            URL newUrl;
            String locationString = null;
            try {
                locationString = webResponse.getResponseHeaderValue("Location");
                if (locationString == null) {
                    return webResponse;
                }
                if (!getBrowserVersion().hasFeature(URL_MINIMAL_QUERY_ENCODING)) {
                    locationString = new String(locationString.getBytes(ISO_8859_1), UTF_8);
                }
                newUrl = expandUrl(url, locationString);

                if (getBrowserVersion().hasFeature(HTTP_REDIRECT_WITHOUT_HASH)) {
                    newUrl = UrlUtils.getUrlWithNewRef(newUrl, null);
                }
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

            if (status == HttpStatus.SC_MOVED_PERMANENTLY
                    || status == HttpStatus.SC_MOVED_TEMPORARILY
                    || status == HttpStatus.SC_SEE_OTHER) {
                final WebRequest wrs = new WebRequest(newUrl, HttpMethod.GET);
                wrs.setCharset(webRequest.getCharset());

                if (HttpMethod.HEAD == webRequest.getHttpMethod()) {
                    wrs.setHttpMethod(HttpMethod.HEAD);
                }
                for (final Map.Entry<String, String> entry : webRequest.getAdditionalHeaders().entrySet()) {
                    wrs.setAdditionalHeader(entry.getKey(), entry.getValue());
                }
                return loadWebResponseFromWebConnection(wrs, allowedRedirects - 1);
            }
            else if (status == HttpStatus.SC_TEMPORARY_REDIRECT
                        || status == 308) {
                // https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/307
                // https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/308
                // reuse method and body
                final WebRequest wrs = new WebRequest(newUrl, webRequest.getHttpMethod());
                wrs.setCharset(webRequest.getCharset());
                if (webRequest.getRequestBody() != null) {
                    if (HttpMethod.POST == webRequest.getHttpMethod()
                            || HttpMethod.PUT == webRequest.getHttpMethod()
                            || HttpMethod.PATCH == webRequest.getHttpMethod()) {
                        wrs.setRequestBody(webRequest.getRequestBody());
                        wrs.setEncodingType(webRequest.getEncodingType());
                    }
                }
                else {
                    wrs.setRequestParameters(parameters);
                }

                for (final Map.Entry<String, String> entry : webRequest.getAdditionalHeaders().entrySet()) {
                    wrs.setAdditionalHeader(entry.getKey(), entry.getValue());
                }

                return loadWebResponseFromWebConnection(wrs, allowedRedirects - 1);
            }
        }

        if (fromCache == null) {
            getCache().cacheIfPossible(webRequest, webResponse, null);
        }
        return webResponse;
    }

    /**
     * Adds the headers that are sent with every request to the specified {@link WebRequest} instance.
     * @param wrs the <tt>WebRequestSettings</tt> instance to modify
     */
    private void addDefaultHeaders(final WebRequest wrs) {
        // Add user-specified headers to the web request if not present there yet.
        requestHeaders_.forEach((name, value) -> {
            if (!wrs.isAdditionalHeader(name)) {
                wrs.setAdditionalHeader(name, value);
            }
        });

        // Add standard HtmlUnit headers to the web request if still not present there yet.
        if (!wrs.isAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE)) {
            wrs.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, getBrowserVersion().getAcceptLanguageHeader());
        }

        if (getBrowserVersion().hasFeature(HTTP_HEADER_SEC_FETCH)
                && !wrs.isAdditionalHeader(HttpHeader.SEC_FETCH_DEST)) {
            wrs.setAdditionalHeader(HttpHeader.SEC_FETCH_DEST, "document");
        }
        if (getBrowserVersion().hasFeature(HTTP_HEADER_SEC_FETCH)
                && !wrs.isAdditionalHeader(HttpHeader.SEC_FETCH_MODE)) {
            wrs.setAdditionalHeader(HttpHeader.SEC_FETCH_MODE, "navigate");
        }
        if (getBrowserVersion().hasFeature(HTTP_HEADER_SEC_FETCH)
                && !wrs.isAdditionalHeader(HttpHeader.SEC_FETCH_SITE)) {
            wrs.setAdditionalHeader(HttpHeader.SEC_FETCH_SITE, "same-origin");
        }
        if (getBrowserVersion().hasFeature(HTTP_HEADER_SEC_FETCH)
                && !wrs.isAdditionalHeader(HttpHeader.SEC_FETCH_USER)) {
            wrs.setAdditionalHeader(HttpHeader.SEC_FETCH_USER, "?1");
        }

        if (getBrowserVersion().hasFeature(HTTP_HEADER_UPGRADE_INSECURE_REQUEST)
                && !wrs.isAdditionalHeader(HttpHeader.UPGRADE_INSECURE_REQUESTS)) {
            wrs.setAdditionalHeader(HttpHeader.UPGRADE_INSECURE_REQUESTS, "1");
        }
    }

    /**
     * Returns an immutable list of open web windows (whether they are top level windows or not).
     * This is a snapshot; future changes are not reflected by this list.
     * <p>
     * The list is ordered by age, the oldest one first.
     *
     * @return an immutable list of open web windows (whether they are top level windows or not)
     * @see #getWebWindowByName(String)
     * @see #getTopLevelWindows()
     */
    public List<WebWindow> getWebWindows() {
        return Collections.unmodifiableList(new ArrayList<>(windows_));
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns true if the list of WebWindows contains the provided one.
     * This method is there to improve the performance of some internal checks because
     * calling getWebWindows().contains(.) creates some objects without any need.
     *
     * @param webWindow the window to check
     * @return true or false
     */
    public boolean containsWebWindow(final WebWindow webWindow) {
        return windows_.contains(webWindow);
    }

    /**
     * Returns an immutable list of open top level windows.
     * This is a snapshot; future changes are not reflected by this list.
     * <p>
     * The list is ordered by age, the oldest one first.
     *
     * @return an immutable list of open top level windows
     * @see #getWebWindowByName(String)
     * @see #getWebWindows()
     */
    public List<TopLevelWindow> getTopLevelWindows() {
        return Collections.unmodifiableList(new ArrayList<>(topLevelWindows_));
    }

    /**
     * Sets the handler to be used whenever a refresh is triggered. Refer
     * to the documentation for {@link RefreshHandler} for more details.
     * @param handler the new handler
     */
    public void setRefreshHandler(final RefreshHandler handler) {
        if (handler == null) {
            refreshHandler_ = new NiceRefreshHandler(2);
        }
        else {
            refreshHandler_ = handler;
        }
    }

    /**
     * Returns the current refresh handler.
     * The default refresh handler is a {@link NiceRefreshHandler NiceRefreshHandler(2)}.
     * @return the current RefreshHandler
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
     * Returns the MSXML ActiveX object factory (if supported).
     * @return the msxmlActiveXObjectFactory
     */
    public MSXMLActiveXObjectFactory getMSXMLActiveXObjectFactory() {
        return msxmlActiveXObjectFactory_;
    }

    /**
     * Sets the listener for messages generated by the HTML parser.
     * @param listener the new listener, {@code null} if messages should be totally ignored
     */
    public void setHTMLParserListener(final HTMLParserListener listener) {
        htmlParserListener_ = listener;
    }

    /**
     * Gets the configured listener for messages generated by the HTML parser.
     * @return {@code null} if no listener is defined (default value)
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
    public CSSErrorHandler getCssErrorHandler() {
        return cssErrorHandler_;
    }

    /**
     * Sets the CSS error handler used by this web client when CSS problems are encountered.
     * @param cssErrorHandler the CSS error handler used by this web client when CSS problems are encountered
     * @see DefaultCssErrorHandler
     * @see SilentCssErrorHandler
     */
    public void setCssErrorHandler(final CSSErrorHandler cssErrorHandler) {
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
        scriptEngine_.setJavaScriptTimeout(timeout);
    }

    /**
     * Returns the number of milliseconds that a script is allowed to execute before being terminated.
     * A value of 0 or less means no timeout.
     *
     * @return the timeout value, in milliseconds
     */
    public long getJavaScriptTimeout() {
        return scriptEngine_.getJavaScriptTimeout();
    }

    /**
     * Gets the current listener for encountered incorrectness (except HTML parsing messages that
     * are handled by the HTML parser listener). Default value is an instance of
     * {@link IncorrectnessListenerImpl}.
     * @return the current listener (not {@code null})
     */
    public IncorrectnessListener getIncorrectnessListener() {
        return incorrectnessListener_;
    }

    /**
     * Returns the current HTML incorrectness listener.
     * @param listener the new value (not {@code null})
     */
    public void setIncorrectnessListener(final IncorrectnessListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null is not a valid IncorrectnessListener");
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
            throw new IllegalArgumentException("Null is not a valid AjaxController");
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
     * Sets the WebStart handler.
     * @param handler the new WebStart handler
     */
    public void setWebStartHandler(final WebStartHandler handler) {
        webStartHandler_ = handler;
    }

    /**
     * Returns the current WebStart handler.
     * @return the current WebStart handler
     */
    public WebStartHandler getWebStartHandler() {
        return webStartHandler_;
    }

    /**
     * Sets the applet confirm handler.
     * @param handler the new applet confirm handler handler
     */
    public void setAppletConfirmHandler(final AppletConfirmHandler handler) {
        appletConfirmHandler_ = handler;
    }

    /**
     * Returns the current applet confirm handler.
     * @return the current applet confirm handler
     */
    public AppletConfirmHandler getAppletConfirmHandler() {
        return appletConfirmHandler_;
    }

    /**
     * Returns the current FrameContent handler.
     * @return the current FrameContent handler
     */
    public FrameContentHandler getFrameContentHandler() {
        return frameContentHandler_;
    }

    /**
     * Sets the FrameContent handler.
     * @param handler the new FrameContent handler
     */
    public void setFrameContentHandler(final FrameContentHandler handler) {
        frameContentHandler_ = handler;
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
     * @param cache the new cache (must not be {@code null})
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

        CurrentWindowTracker(final WebClient webClient) {
            webClient_ = webClient;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void webWindowClosed(final WebWindowEvent event) {
            final WebWindow window = event.getWebWindow();
            if (window instanceof TopLevelWindow) {
                webClient_.topLevelWindows_.remove(window);
                if (window == webClient_.getCurrentWindow()) {
                    if (webClient_.topLevelWindows_.isEmpty()) {
                        // Must always have at least window, and there are no top-level windows left; must create one.
                        final TopLevelWindow newWindow = new TopLevelWindow("", webClient_);
                        webClient_.topLevelWindows_.add(newWindow);
                        webClient_.setCurrentWindow(newWindow);
                    }
                    else {
                        // The current window is now the previous top-level window.
                        webClient_.setCurrentWindow(
                                webClient_.topLevelWindows_.get(webClient_.topLevelWindows_.size() - 1));
                    }
                }
            }
            else if (window == webClient_.getCurrentWindow()) {
                // The current window is now the last top-level window.
                if (webClient_.topLevelWindows_.isEmpty()) {
                    webClient_.setCurrentWindow(null);
                }
                else {
                    webClient_.setCurrentWindow(
                            webClient_.topLevelWindows_.get(webClient_.topLevelWindows_.size() - 1));
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void webWindowContentChanged(final WebWindowEvent event) {
            final WebWindow window = event.getWebWindow();
            boolean use = false;
            if (window instanceof DialogWindow) {
                use = true;
            }
            else if (window instanceof TopLevelWindow) {
                use = event.getOldPage() == null;
            }
            else if (window instanceof FrameWindow) {
                final FrameWindow fw = (FrameWindow) window;
                final String enclosingPageState = fw.getEnclosingPage().getDocumentElement().getReadyState();
                final URL frameUrl = fw.getEnclosedPage().getUrl();
                if (!DomNode.READY_STATE_COMPLETE.equals(enclosingPageState) || frameUrl == UrlUtils.URL_ABOUT_BLANK) {
                    return;
                }

                // now looks at the visibility of the frame window
                final BaseFrameElement frameElement = fw.getFrameElement();
                if (webClient_.isJavaScriptEnabled() && frameElement.isDisplayed()) {
                    final Object element = frameElement.getScriptableObject();
                    final HTMLElement htmlElement = (HTMLElement) element;
                    final ComputedCSSStyleDeclaration style =
                            htmlElement.getWindow().getComputedStyle(htmlElement, null);
                    use = style.getCalculatedWidth(false, false) != 0
                            && style.getCalculatedHeight(false, false) != 0;
                }
            }
            if (use) {
                webClient_.setCurrentWindow(window);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void webWindowOpened(final WebWindowEvent event) {
            final WebWindow window = event.getWebWindow();
            if (window instanceof TopLevelWindow) {
                final TopLevelWindow tlw = (TopLevelWindow) window;
                webClient_.topLevelWindows_.add(tlw);
            }
            // Page is not loaded yet, don't set it now as current window.
        }
    }

    /**
     * Closes all opened windows, stopping all background JavaScript processing.
     *
     * {@inheritDoc}
     */
    @Override
    public void close() {
        // NB: this implementation is too simple as a new TopLevelWindow may be opened by
        // some JS script while we are closing the others
        final List<TopLevelWindow> topWindows = new ArrayList<>(topLevelWindows_);
        for (final TopLevelWindow topWindow : topWindows) {
            if (topLevelWindows_.contains(topWindow)) {
                try {
                    topWindow.close(true);
                }
                catch (final Exception e) {
                    LOG.error("Exception while closing a topLevelWindow", e);
                }
            }
        }

        // do this after closing the windows, otherwise some unload event might
        // start a new window that will start the thread again
        if (scriptEngine_ != null) {
            try {
                scriptEngine_.shutdown();
            }
            catch (final Exception e) {
                LOG.error("Exception while shutdown the scriptEngine", e);
            }
        }

        try {
            webConnection_.close();
        }
        catch (final Exception e) {
            LOG.error("Exception while closing the connection", e);
        }

        synchronized (this) {
            if (executor_ != null) {
                try {
                    executor_.shutdownNow();
                }
                catch (final Exception e) {
                    LOG.error("Exception while shutdown the executor service", e);
                }
            }
        }

        cache_.clear();
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
        for (Iterator<WeakReference<JavaScriptJobManager>> i = jobManagers_.iterator(); i.hasNext();) {
            final JavaScriptJobManager jobManager;
            final WeakReference<JavaScriptJobManager> reference;
            try {
                reference = i.next();
                jobManager = reference.get();
                if (jobManager == null) {
                    i.remove();
                    continue;
                }
            }
            catch (final ConcurrentModificationException e) {
                i = jobManagers_.iterator();
                count = 0;
                continue;
            }

            final long newTimeout = endTime - System.currentTimeMillis();
            count += jobManager.waitForJobs(newTimeout);
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
        for (Iterator<WeakReference<JavaScriptJobManager>> i = jobManagers_.iterator(); i.hasNext();) {
            final JavaScriptJobManager jobManager;
            final WeakReference<JavaScriptJobManager> reference;
            try {
                reference = i.next();
                jobManager = reference.get();
                if (jobManager == null) {
                    i.remove();
                    continue;
                }
            }
            catch (final ConcurrentModificationException e) {
                i = jobManagers_.iterator();
                count = 0;
                continue;
            }
            final long newDelay = endTime - System.currentTimeMillis();
            count += jobManager.waitForJobsStartingBefore(newDelay);
        }
        if (count != getAggregateJobCount()) {
            final long newDelay = endTime - System.currentTimeMillis();
            return waitForBackgroundJavaScriptStartingBefore(newDelay);
        }
        return count;
    }

    /**
     * Returns the aggregate background JavaScript job count across all windows.
     * @return the aggregate background JavaScript job count across all windows
     */
    private int getAggregateJobCount() {
        int count = 0;
        for (Iterator<WeakReference<JavaScriptJobManager>> i = jobManagers_.iterator(); i.hasNext();) {
            final JavaScriptJobManager jobManager;
            final WeakReference<JavaScriptJobManager> reference;
            try {
                reference = i.next();
                jobManager = reference.get();
                if (jobManager == null) {
                    i.remove();
                    continue;
                }
            }
            catch (final ConcurrentModificationException e) {
                i = jobManagers_.iterator();
                count = 0;
                continue;
            }
            final int jobCount = jobManager.getJobCount();
            count += jobCount;
        }
        return count;
    }

    /**
     * When we deserialize, re-initializie transient fields.
     * @param in the object input stream
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if an error occurs
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        webConnection_ = new HttpWebConnection(this);
        scriptEngine_ = new JavaScriptEngine(this);
        jobManagers_ = Collections.synchronizedList(new ArrayList<WeakReference<JavaScriptJobManager>>());
        loadQueue_ = new ArrayList<>();

        if (getBrowserVersion().hasFeature(JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
            initMSXMLActiveX();
        }
    }

    private static class LoadJob {
        private final WebWindow requestingWindow_;
        private final String target_;
        private final WebResponse response_;
        private final WeakReference<Page> originalPage_;
        private final WebRequest request_;

        LoadJob(final WebRequest request, final WebWindow requestingWindow, final String target,
                final WebResponse response) {
            request_ = request;
            requestingWindow_ = requestingWindow;
            target_ = target;
            response_ = response;
            originalPage_ = new WeakReference<>(requestingWindow.getEnclosedPage());
        }

        public boolean isOutdated() {
            if (target_ != null && !target_.isEmpty()) {
                return false;
            }

            if (requestingWindow_.isClosed()) {
                return true;
            }

            if (requestingWindow_.getEnclosedPage() != originalPage_.get()) {
                return true;
            }

            return false;
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Perform the downloads and stores it for loading later into a window.
     * In the future downloads should be performed in parallel in separated threads.
     * TODO: refactor it before next release.
     * @param requestingWindow the window from which the request comes
     * @param target the name of the target window
     * @param request the request to perform
     * @param checkHash if true check for hashChenage
     * @param forceLoad if true always load the request even if there is already the same in the queue
     * @param description information about the origin of the request. Useful for debugging.
     */
    public void download(final WebWindow requestingWindow, final String target,
        final WebRequest request, final boolean checkHash, final boolean forceLoad, final String description) {
        final WebWindow targetWindow = resolveWindow(requestingWindow, target);
        final URL url = request.getUrl();
        boolean justHashJump = false;

        if (targetWindow != null && HttpMethod.POST != request.getHttpMethod()) {
            final Page page = targetWindow.getEnclosedPage();
            if (page != null) {
                if (page.isHtmlPage() && !((HtmlPage) page).isOnbeforeunloadAccepted()) {
                    return;
                }

                if (checkHash) {
                    final URL current = page.getUrl();
                    justHashJump =
                            HttpMethod.GET == request.getHttpMethod()
                            && UrlUtils.sameFile(url, current)
                            && null != url.getRef();

                    if (justHashJump) {
                        processOnlyHashChange(targetWindow, url);
                        return;
                    }
                }
            }
        }

        synchronized (loadQueue_) {
            // verify if this load job doesn't already exist
            for (final LoadJob loadJob : loadQueue_) {
                if (loadJob.response_ == null) {
                    continue;
                }
                final WebRequest otherRequest = loadJob.request_;
                final URL otherUrl = otherRequest.getUrl();

                // TODO: investigate but it seems that IE considers query string too but not FF
                if (!forceLoad
                    && url.getPath().equals(otherUrl.getPath()) // fail fast
                    && url.toString().equals(otherUrl.toString())
                    && request.getRequestParameters().equals(otherRequest.getRequestParameters())
                    && StringUtils.equals(request.getRequestBody(), otherRequest.getRequestBody())) {
                    return; // skip it;
                }
            }
        }

        final LoadJob loadJob;
        try {
            WebResponse response;
            try {
                response = loadWebResponse(request);
            }
            catch (final NoHttpResponseException e) {
                LOG.error("NoHttpResponseException while downloading; generating a NoHttpResponse", e);
                response = new WebResponse(RESPONSE_DATA_NO_HTTP_RESPONSE, request, 0);
            }
            loadJob = new LoadJob(request, requestingWindow, target, response);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }

        synchronized (loadQueue_) {
            loadQueue_.add(loadJob);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
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
            queue = new ArrayList<>(loadQueue_);
            loadQueue_.clear();
        }

        final HashSet<WebWindow> updatedWindows = new HashSet<>();
        for (int i = queue.size() - 1; i >= 0; --i) {
            final LoadJob loadJob = queue.get(i);
            if (loadJob.isOutdated()) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("No usage of download: " + loadJob);
                }
                continue;
            }

            final WebWindow window = resolveWindow(loadJob.requestingWindow_, loadJob.target_);
            if (updatedWindows.contains(window)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("No usage of download: " + loadJob);
                }
                continue;
            }

            final WebWindow win = openTargetWindow(loadJob.requestingWindow_, loadJob.target_, "_self");
            final Page pageBeforeLoad = win.getEnclosedPage();
            loadWebResponseInto(loadJob.response_, win);

            // start execution here.
            if (scriptEngine_ != null) {
                scriptEngine_.registerWindowAndMaybeStartEventLoop(win);
            }

            if (pageBeforeLoad != win.getEnclosedPage()) {
                updatedWindows.add(win);
            }

            // check and report problems if needed
            throwFailingHttpStatusCodeExceptionIfNecessary(loadJob.response_);
        }
    }

    private static void processOnlyHashChange(final WebWindow window, final URL urlWithOnlyHashChange) {
        final Page page = window.getEnclosedPage();
        final String oldURL = page.getUrl().toExternalForm();

        // update request url
        final WebRequest req = page.getWebResponse().getWebRequest();
        req.setUrl(urlWithOnlyHashChange);

        // update location.hash
        final Window jsWindow = window.getScriptableObject();
        if (null != jsWindow) {
            final Location location = jsWindow.getLocation();
            location.setHash(oldURL, urlWithOnlyHashChange.getRef());
        }

        // add to history
        window.getHistory().addPage(page);
    }

    /**
     * Returns the options object of this WebClient.
     * @return the options object
     */
    public WebClientOptions getOptions() {
        return options_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the internals object of this WebClient.
     * @return the internals object
     */
    public WebClientInternals getInternals() {
        return internals_;
    }

    /**
     * Gets the holder for the different storages.
     * <p><span style="color:red">Experimental API: May be changed in next release!</span></p>
     * @return the holder
     */
    public StorageHolder getStorageHolder() {
        return storageHolder_;
    }

    /**
     * Returns the currently configured cookies applicable to the specified URL, in an unmodifiable set.
     * If disabled, this returns an empty set.
     * @param url the URL on which to filter the returned cookies
     * @return the currently configured cookies applicable to the specified URL, in an unmodifiable set
     */
    public synchronized Set<Cookie> getCookies(final URL url) {
        final CookieManager cookieManager = getCookieManager();

        if (!cookieManager.isCookiesEnabled()) {
            return Collections.<Cookie>emptySet();
        }

        final URL normalizedUrl = cookieManager.replaceForCookieIfNecessary(url);

        final String host = normalizedUrl.getHost();
        // URLs like "about:blank" don't have cookies and we need to catch these
        // cases here before HttpClient complains
        if (host.isEmpty()) {
            return Collections.emptySet();
        }

        final String path = normalizedUrl.getPath();
        final String protocol = normalizedUrl.getProtocol();
        final boolean secure = "https".equals(protocol);

        final int port = cookieManager.getPort(normalizedUrl);

        // discard expired cookies
        cookieManager.clearExpired(new Date());

        final List<org.apache.http.cookie.Cookie> all = Cookie.toHttpClient(cookieManager.getCookies());
        final List<org.apache.http.cookie.Cookie> matches = new ArrayList<>();

        if (all.size() > 0) {
            final CookieOrigin cookieOrigin = new CookieOrigin(host, port, path, secure);
            final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(getBrowserVersion());
            for (final org.apache.http.cookie.Cookie cookie : all) {
                if (cookieSpec.match(cookie, cookieOrigin)) {
                    matches.add(cookie);
                }
            }
        }

        final Set<Cookie> cookies = new LinkedHashSet<>();
        cookies.addAll(Cookie.fromHttpClient(matches));
        return Collections.unmodifiableSet(cookies);
    }

    /**
     * Parses the given cookie and adds this to our cookie store.
     * @param cookieString the string to parse
     * @param pageUrl the url of the page that likes to set the cookie
     * @param origin the requester
     */
    public void addCookie(final String cookieString, final URL pageUrl, final Object origin) {
        final CookieManager cookieManager = getCookieManager();
        if (!cookieManager.isCookiesEnabled()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Skipped adding cookie: '" + cookieString
                        + "' because cookies are not enabled for the CookieManager.");
            }
            return;
        }

        final CharArrayBuffer buffer = new CharArrayBuffer(cookieString.length() + 22);
        buffer.append("Set-Cookie: ");
        buffer.append(cookieString);

        final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(getBrowserVersion());

        try {
            final List<org.apache.http.cookie.Cookie> cookies =
                    cookieSpec.parse(new BufferedHeader(buffer), cookieManager.buildCookieOrigin(pageUrl));

            for (final org.apache.http.cookie.Cookie cookie : cookies) {
                final Cookie htmlUnitCookie = new Cookie((ClientCookie) cookie);
                cookieManager.addCookie(htmlUnitCookie);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Added cookie: '" + cookieString + "'");
                }
            }
        }
        catch (final MalformedCookieException e) {
            if (LOG.isDebugEnabled()) {
                LOG.warn("Adding cookie '" + cookieString + "' failed; reason: '" + e.getMessage() + "'.");
            }
            getIncorrectnessListener().notify("Adding cookie '" + cookieString
                        + "' failed; reason: '" + e.getMessage() + "'.", origin);
        }
    }

    /**
     * Returns true if the javaScript support is enabled.
     * To disable the javascript support (eg. temporary)
     * you have to use the {@link WebClientOptions#setJavaScriptEnabled(boolean)} setter.
     * @see #isJavaScriptEngineEnabled()
     * @return true if the javaScript engine and the javaScript support is enabled.
     */
    public boolean isJavaScriptEnabled() {
        return javaScriptEngineEnabled_ && getOptions().isJavaScriptEnabled();
    }

    /**
     * Returns true if the javaScript engine is enabled.
     * To disable the javascript engine you have to use the
     * {@link WebClient#WebClient(BrowserVersion, boolean, String, int)} constructor.
     * @return true if the javaScript engine is enabled.
     */
    public boolean isJavaScriptEngineEnabled() {
        return javaScriptEngineEnabled_;
    }

    /**
     * Parses the given XHtml code string and loads the resulting XHtmlPage into
     * the current window.
     *
     * @param htmlCode the html code as string
     * @return the HtmlPage
     * @throws IOException in case of error
     */
    public HtmlPage loadHtmlCodeIntoCurrentWindow(final String htmlCode) throws IOException {
        final HTMLParser htmlParser = getPageCreator().getHtmlParser();
        final WebWindow webWindow = getCurrentWindow();

        final StringWebResponse webResponse =
                new StringWebResponse(htmlCode, new URL("http://htmlunit.sourceforge.net/dummy.html"));
        final HtmlPage page = new HtmlPage(webResponse, webWindow);
        webWindow.setEnclosedPage(page);

        htmlParser.parse(webResponse, page, true);
        return page;
    }

    /**
     * Parses the given XHtml code string and loads the resulting XHtmlPage into
     * the current window.
     *
     * @param xhtmlCode the xhtml code as string
     * @return the XHtmlPage
     * @throws IOException in case of error
     */
    public XHtmlPage loadXHtmlCodeIntoCurrentWindow(final String xhtmlCode) throws IOException {
        final HTMLParser htmlParser = getPageCreator().getHtmlParser();
        final WebWindow webWindow = getCurrentWindow();

        final StringWebResponse webResponse =
                new StringWebResponse(xhtmlCode, new URL("http://htmlunit.sourceforge.net/dummy.html"));
        final XHtmlPage page = new XHtmlPage(webResponse, webWindow);
        webWindow.setEnclosedPage(page);

        htmlParser.parse(webResponse, page, true);
        return page;
    }
}
