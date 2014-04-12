/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONLOAD_UNDEFINED_THROWS_ERROR;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_HANDLER_AS_PROPERTY_DONT_RECEIVE_EVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SET_INTERVAL_ZERO_TIMEOUT_FORCES_SET_TIMEOUT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_CHANGE_OPENER_NOT_ALLOWED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_FORMFIELDS_ACCESSIBLE_BY_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_IS_A_FUNCTION;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_ONERROR_COLUMN_ARGUMENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_POST_MESSAGE_ALLOW_INVALID_PORT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_POST_MESSAGE_CANCELABLE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_POST_MESSAGE_SYNCHRONOUS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML_IN_HTML_VIA_ACTIVEXOBJECT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.DialogWindow;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.StorageHolder.Type;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLActiveXObjectFactory;
import com.gargoylesoftware.htmlunit.activex.javascript.msxml.XMLDOMDocument;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlEmbed;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnly;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnlyStatus;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.html.DocumentProxy;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for a Window.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Darrell DeBoer
 * @author Marc Guillemot
 * @author Dierk Koenig
 * @author Daniel Gredler
 * @author David D. Kilzer
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535873.aspx">MSDN documentation</a>
 */
@JsxClass
public class Window extends SimpleScriptable implements ScriptableWithFallbackGetter, Function {

    private static final Log LOG = LogFactory.getLog(Window.class);

    /**
     * The minimum delay that can be used with setInterval() or setTimeout(). Browser minimums are
     * usually in the 10ms to 15ms range, but there's really no reason for us to waste that much time.
     * http://jsninja.com/Timers#Minimum_Timer_Delay_and_Reliability
     */
    private static final int MIN_TIMER_DELAY = 1;

    private Document document_;
    private DocumentProxy documentProxy_;
    private Navigator navigator_;
    private WebWindow webWindow_;
    private WindowProxy windowProxy_;
    private Screen screen_;
    private History history_;
    private Location location_;
    private ScriptableObject console_;
    private ApplicationCache applicationCache_;
    private Selection selection_;
    private Event currentEvent_;
    private String status_ = "";
    private HTMLCollection frames_; // has to be a member to have equality (==) working
    private Map<Class<? extends SimpleScriptable>, Scriptable> prototypes_ =
        new HashMap<Class<? extends SimpleScriptable>, Scriptable>();
    private EventListenersContainer eventListenersContainer_;
    private Object controllers_;
    private Object opener_;
    private Object top_ = NOT_FOUND; // top can be set from JS to any value!

    /**
     * Cache computed styles when possible, because their calculation is very expensive.
     * We use a weak hash map because we don't want this cache to be the only reason
     * nodes are kept around in the JVM, if all other references to them are gone.
     */
    private transient WeakHashMap<Node, ComputedCSSStyleDeclaration> computedStyles_ =
        new WeakHashMap<Node, ComputedCSSStyleDeclaration>();

    private final Map<Type, Storage> storages_ = new HashMap<Type, Storage>();
    private StorageList storageList_;

    /**
     * Restores the transient {@link #computedStyles_} map during deserialization.
     * @param stream the stream to read the object from
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if a class is not found
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        computedStyles_ = new WeakHashMap<Node, ComputedCSSStyleDeclaration>();
    }

    /**
     * Creates an instance.
     */
    public Window() {
    }

    /**
     * Returns the prototype object corresponding to the specified HtmlUnit class inside the window scope.
     * @param jsClass the class whose prototype is to be returned
     * @return the prototype object corresponding to the specified class inside the specified scope
     */
    @Override
    public Scriptable getPrototype(final Class<? extends SimpleScriptable> jsClass) {
        return prototypes_.get(jsClass);
    }

    /**
     * Sets the prototypes for HtmlUnit host classes.
     * @param map a Map of ({@link Class}, {@link Scriptable})
     */
    public void setPrototypes(final Map<Class<? extends SimpleScriptable>, Scriptable> map) {
        prototypes_ = map;
    }

    /**
     * The JavaScript function "alert()".
     * @param message the message
     */
    @JsxFunction
    public void alert(final Object message) {
        // use Object as parameter and perform String conversion by ourself
        // this allows to place breakpoint here and "see" the message object and its properties
        final String stringMessage = Context.toString(message);
        final AlertHandler handler = getWebWindow().getWebClient().getAlertHandler();
        if (handler == null) {
            LOG.warn("window.alert(\"" + stringMessage + "\") no alert handler installed");
        }
        else {
            handler.handleAlert(((HTMLDocument) document_).getHtmlPage(), stringMessage);
        }
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String btoa(final String stringToEncode) {
        return new String(Base64.encodeBase64(stringToEncode.getBytes()));
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding..
     * @param encodedData the encoded string
     * @return the decoded value
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String atob(final String encodedData) {
        return new String(Base64.decodeBase64(encodedData.getBytes()));
    }

    /**
     * The JavaScript function "confirm()".
     * @param message the message
     * @return true if ok was pressed, false if cancel was pressed
     */
    @JsxFunction
    public boolean confirm(final String message) {
        final ConfirmHandler handler = getWebWindow().getWebClient().getConfirmHandler();
        if (handler == null) {
            LOG.warn("window.confirm(\""
                    + message + "\") no confirm handler installed, simulating the OK button");
            return true;
        }
        return handler.handleConfirm(((HTMLDocument) document_).getHtmlPage(), message);
    }

    /**
     * The JavaScript function "prompt()".
     * @param message the message
     * @return true if ok was pressed, false if cancel was pressed
     */
    @JsxFunction
    public String prompt(final String message) {
        final PromptHandler handler = getWebWindow().getWebClient().getPromptHandler();
        if (handler == null) {
            LOG.warn("window.prompt(\"" + message + "\") no prompt handler installed");
            return null;
        }
        return handler.handlePrompt(((HTMLDocument) document_).getHtmlPage(), message);
    }

    /**
     * Returns the JavaScript property "document".
     * @return the document
     */
    @JsxGetter(propertyName = "document")
    public DocumentProxy getDocument_js() {
        return documentProxy_;
    }

    /**
     * Returns the window's current document.
     * @return the window's current document
     */
    public Document getDocument() {
        return document_;
    }

    /**
     * Returns the application cache.
     * @return the application cache
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public ApplicationCache getApplicationCache() {
        return applicationCache_;
    }

    /**
     * Returns the current event (used by JavaScript only when emulating IE).
     * @return the current event, or <tt>null</tt> if no event is currently available
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(CHROME) })
    public Object getEvent() {
        return currentEvent_;
    }

    /**
     * Returns the current event (used internally regardless of the emulation mode).
     * @return the current event, or <tt>null</tt> if no event is currently available
     */
    public Event getCurrentEvent() {
        return currentEvent_;
    }

    /**
     * Sets the current event.
     * @param event the current event
     */
    void setCurrentEvent(final Event event) {
        currentEvent_ = event;
    }

    /**
     * Opens a new window.
     *
     * @param url when a new document is opened, <i>url</i> is a String that specifies a MIME type for the document.
     *        When a new window is opened, <i>url</i> is a String that specifies the URL to render in the new window
     * @param name the name
     * @param features the features
     * @param replace whether to replace in the history list or no
     * @return the newly opened window, or <tt>null</tt> if popup windows have been disabled
     * @see com.gargoylesoftware.htmlunit.WebClientOptions#isPopupBlockerEnabled()
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536651.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public WindowProxy open(final Object url, final Object name, final Object features,
            final Object replace) {
        String urlString = null;
        if (url != Undefined.instance) {
            urlString = Context.toString(url);
        }
        String windowName = "";
        if (name != Undefined.instance) {
            windowName = Context.toString(name);
        }
        String featuresString = null;
        if (features != Undefined.instance) {
            featuresString = Context.toString(features);
        }
        final WebClient webClient = getWebWindow().getWebClient();

        if (webClient.getOptions().isPopupBlockerEnabled()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignoring window.open() invocation because popups are blocked.");
            }
            return null;
        }

        boolean replaceCurrentEntryInBrowsingHistory = false;
        if (replace != Undefined.instance) {
            replaceCurrentEntryInBrowsingHistory = Context.toBoolean(replace);
        }
        if (featuresString != null || replaceCurrentEntryInBrowsingHistory) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                        "window.open: features and replaceCurrentEntryInBrowsingHistory "
                        + "not implemented: url=[" + urlString
                        + "] windowName=[" + windowName
                        + "] features=[" + featuresString
                        + "] replaceCurrentEntry=[" + replaceCurrentEntryInBrowsingHistory
                        + "]");
            }
        }

        // if specified name is the name of an existing window, then hold it
        if (StringUtils.isEmpty(urlString) && !"".equals(windowName)) {
            try {
                final WebWindow webWindow = webClient.getWebWindowByName(windowName);
                return getProxy(webWindow);
            }
            catch (final WebWindowNotFoundException e) {
                // nothing
            }
        }
        final URL newUrl = makeUrlForOpenWindow(urlString);
        final WebWindow newWebWindow = webClient.openWindow(newUrl, windowName, webWindow_);
        return getProxy(newWebWindow);
    }

    /**
     * Creates a popup window.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536392.aspx">MSDN documentation</a>
     * @return the created popup
     */
    @JsxFunction(@WebBrowser(value = IE, maxVersion = 10))
    public Popup createPopup() {
        final Popup popup = new Popup();
        popup.setParentScope(this);
        popup.setPrototype(getPrototype(Popup.class));
        popup.init(this);
        return popup;
    }

    private URL makeUrlForOpenWindow(final String urlString) {
        if (urlString.isEmpty()) {
            return WebClient.URL_ABOUT_BLANK;
        }

        try {
            final Page page = getWebWindow().getEnclosedPage();
            if (page != null && page.isHtmlPage()) {
                return ((HtmlPage) page).getFullyQualifiedUrl(urlString);
            }
            return new URL(urlString);
        }
        catch (final MalformedURLException e) {
            LOG.error("Unable to create URL for openWindow: relativeUrl=[" + urlString + "]", e);
            return null;
        }
    }

    /**
     * Sets a chunk of JavaScript to be invoked at some specified time later.
     * The invocation occurs only if the window is opened after the delay
     * and does not contain an other page than the one that originated the setTimeout.
     *
     * @param code specifies the function pointer or string that indicates the code to be executed
     *        when the specified interval has elapsed
     * @param timeout specifies the number of milliseconds
     * @param language specifies language
     * @return the id of the created timer
     */
    @JsxFunction
    public int setTimeout(final Object code, int timeout, final Object language) {
        if (timeout < MIN_TIMER_DELAY) {
            timeout = MIN_TIMER_DELAY;
        }
        if (code == null) {
            throw Context.reportRuntimeError("Function not provided.");
        }

        final int id;
        final WebWindow webWindow = getWebWindow();
        final Page page = (Page) getDomNodeOrNull();
        if (code instanceof String) {
            final String s = (String) code;
            final String description = "window.setTimeout(" + s + ", " + timeout + ")";
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavaScriptJob(timeout, null, description, webWindow, s);
            id = webWindow.getJobManager().addJob(job, page);
        }
        else if (code instanceof Function) {
            final Function f = (Function) code;
            final String functionName;
            if (f instanceof FunctionObject) {
                functionName = ((FunctionObject) f).getFunctionName();
            }
            else {
                functionName = String.valueOf(f); // can this happen?
            }

            final String description = "window.setTimeout(" + functionName + ", " + timeout + ")";
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavaScriptJob(timeout, null, description, webWindow, f);
            id = webWindow.getJobManager().addJob(job, page);
        }
        else {
            throw Context.reportRuntimeError("Unknown type for function.");
        }
        return id;
    }

    /**
     * Cancels a time-out previously set with the <tt>setTimeout</tt> method.
     *
     * @param timeoutId identifier for the timeout to clear (returned by <tt>setTimeout</tt>)
     */
    @JsxFunction
    public void clearTimeout(final int timeoutId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearTimeout(" + timeoutId + ")");
        }
        getWebWindow().getJobManager().removeJob(timeoutId);
    }

    /**
     * Returns the JavaScript property "navigator".
     * @return the navigator
     */
    @JsxGetter
    public Navigator getNavigator() {
        return navigator_;
    }

    /**
     * Returns the JavaScript property "clientInformation".
     * @return the client information
     */
    @JsxGetter(@WebBrowser(IE))
    public Navigator getClientInformation() {
        return navigator_;
    }

    /**
     * Returns the JavaScript property "clipboardData".
     * @return the ClipboardData
     */
    @JsxGetter(@WebBrowser(IE))
    public ClipboardData getClipboardData() {
        final ClipboardData clipboardData = new ClipboardData();
        clipboardData.setParentScope(this);
        clipboardData.setPrototype(getPrototype(clipboardData.getClass()));
        return clipboardData;
    }

    /**
     * Returns the window property. This is a synonym for "self".
     * @return the window property (a reference to <tt>this</tt>)
     */
    @JsxGetter(propertyName = "window")
    public WindowProxy getWindow_js() {
        return windowProxy_;
    }

    /**
     * Returns the "self" property.
     * @return this
     */
    @JsxGetter
    public WindowProxy getSelf() {
        return windowProxy_;
    }

    /**
     * Returns the localStorage property.
     * @return the localStorage property
     */
    @JsxGetter
    public Storage getLocalStorage() {
        return getStorage(Type.LOCAL_STORAGE);
    }

    /**
     * Returns the sessionStorage property.
     * @return the sessionStorage property
     */
    @JsxGetter
    public Storage getSessionStorage() {
        return getStorage(Type.SESSION_STORAGE);
    }

    /**
     * Gets the storage of the specified type.
     * @param storageType the type
     * @return the storage
     */
    public Storage getStorage(final Type storageType) {
        Storage storage = storages_.get(storageType);
        if (storage == null) {
            final WebWindow webWindow = getWebWindow();
            final Map<String, String> store = webWindow.getWebClient().getStorageHolder().getStore(storageType,
                webWindow.getEnclosedPage());
            storage = new Storage(this, store);
            storages_.put(storageType, storage);
        }

        return storage;
    }

    /**
     * Returns the globalStorage property.
     * @return the globalStorage property
     */
    @JsxGetter(@WebBrowser(value = FF, maxVersion = 10))
    public StorageList getGlobalStorage() {
        if (storageList_ == null) {
            storageList_ = new StorageList();
            storageList_.setParentScope(this);
            storageList_.setPrototype(getPrototype(StorageList.class));
        }
        return storageList_;
    }

    /**
     * Returns the location property.
     * @return the location property
     */
    @JsxGetter
    public Location getLocation() {
        return location_;
    }

    /**
     * Sets the location property. This will cause a reload of the window.
     * @param newLocation the URL of the new content
     * @throws IOException when location loading fails
     */
    @JsxSetter
    public void setLocation(final String newLocation) throws IOException {
        location_.setHref(newLocation);
    }

    /**
     * Returns the console property.
     * @return the console property
     */
    @JsxGetter({ @WebBrowser(value = FF, minVersion = 4), @WebBrowser(value = IE, minVersion = 9),
            @WebBrowser(CHROME) })
    public ScriptableObject getConsole() {
        return console_;
    }

    /**
     * Sets the console.
     * @param console the console
     */
    @JsxSetter({ @WebBrowser(value = FF, minVersion = 4), @WebBrowser(value = IE, minVersion = 9),
        @WebBrowser(CHROME) })
    public void setConsole(final ScriptableObject console) {
        console_ = console;
    }

    /**
     * Prints messages to the console.
     * @param message the message to log
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void dump(final String message) {
        if (console_ instanceof Console) {
            Console.log(null, console_, new Object[] {message}, null);
        }
    }

    /**
     * Returns the "screen" property.
     * @return the screen property
     */
    @JsxGetter
    public Screen getScreen() {
        return screen_;
    }

    /**
     * Returns the "history" property.
     * @return the "history" property
     */
    @JsxGetter
    public History getHistory() {
        return history_;
    }

    /**
     * Returns the "external" property.
     * @return the "external" property
     */
    @JsxGetter(@WebBrowser(IE))
    public External getExternal() {
        final External external = new External();
        external.setParentScope(this);
        external.setPrototype(getPrototype(external.getClass()));
        return external;
    }

    /**
     * Initializes this window.
     * @param webWindow the web window corresponding to this window
     */
    public void initialize(final WebWindow webWindow) {
        webWindow_ = webWindow;
        webWindow_.setScriptObject(this);

        windowProxy_ = new WindowProxy(webWindow_);

        final Page enclosedPage = webWindow.getEnclosedPage();
        if (enclosedPage instanceof XmlPage) {
            document_ = new XMLDocument();
        }
        else {
            document_ = new HTMLDocument();
        }
        document_.setParentScope(this);
        document_.setPrototype(getPrototype(document_.getClass()));
        document_.setWindow(this);

        if (enclosedPage instanceof SgmlPage) {
            final SgmlPage page = (SgmlPage) enclosedPage;
            document_.setDomNode(page);

            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            page.addDomChangeListener(listener);

            if (page.isHtmlPage()) {
                ((HtmlPage) page).addHtmlAttributeChangeListener(listener);
            }
        }

        documentProxy_ = new DocumentProxy(webWindow_);

        navigator_ = new Navigator();
        navigator_.setParentScope(this);
        navigator_.setPrototype(getPrototype(navigator_.getClass()));

        screen_ = new Screen();
        screen_.setParentScope(this);
        screen_.setPrototype(getPrototype(screen_.getClass()));

        history_ = new History();
        history_.setParentScope(this);
        history_.setPrototype(getPrototype(history_.getClass()));

        location_ = new Location();
        location_.setParentScope(this);
        location_.setPrototype(getPrototype(location_.getClass()));
        location_.initialize(this);

        console_ = new Console();
        ((Console) console_).setWebWindow(webWindow_);
        console_.setParentScope(this);
        ((Console) console_).setPrototype(getPrototype(((SimpleScriptable) console_).getClass()));

        applicationCache_ = new ApplicationCache();
        applicationCache_.setParentScope(this);
        applicationCache_.setPrototype(getPrototype(applicationCache_.getClass()));

        // like a JS new Object()
        final Context ctx = Context.getCurrentContext();
        controllers_ = ctx.newObject(this);

        if (webWindow_ instanceof TopLevelWindow) {
            final WebWindow opener = ((TopLevelWindow) webWindow_).getOpener();
            if (opener != null) {
                opener_ = opener.getScriptObject();
            }
        }
    }

    /**
     * Initialize the object.
     * @param enclosedPage the page containing the JavaScript
     */
    public void initialize(final Page enclosedPage) {
        if (enclosedPage != null && enclosedPage.isHtmlPage()) {
            final HtmlPage htmlPage = (HtmlPage) enclosedPage;

            // Windows don't have corresponding DomNodes so set the domNode
            // variable to be the page. If this isn't set then SimpleScriptable.get()
            // won't work properly
            setDomNode(htmlPage);
            eventListenersContainer_ = null;

            WebAssert.notNull("document_", document_);
            document_.setDomNode(htmlPage);
        }
    }

    /**
     * Initializes the object. Only called for Windows with no contents.
     */
    public void initialize() {
        // Empty.
    }

    /**
     * Returns the value of the top property.
     * @return the value of "top"
     */
    @JsxGetter
    public Object getTop() {
        if (top_ != NOT_FOUND) {
            return top_;
        }

        final WebWindow top = getWebWindow().getTopWindow();
        return getProxy(top);
    }

    /**
     * Sets the value of the top property.
     * @param o the new value
     */
    @JsxSetter
    public void setTop(final Object o) {
        top_ = o;
    }

    /**
     * Returns the value of the parent property.
     * @return the value of window.parent
     */
    @JsxGetter
    public WindowProxy getParent() {
        final WebWindow parent = getWebWindow().getParentWindow();
        return getProxy(parent);
    }

    /**
     * Returns the value of the opener property.
     * @return the value of window.opener, <tt>null</tt> for a top level window
     */
    @JsxGetter
    public Object getOpener() {
        Object opener = opener_;
        if (opener instanceof Window) {
            opener = ((Window) opener).windowProxy_;
        }
        return opener;
    }

    /**
     * Sets the opener property.
     * @param newValue the new value
     */
    @JsxSetter
    public void setOpener(Object newValue) {
        if (getBrowserVersion().hasFeature(JS_WINDOW_CHANGE_OPENER_NOT_ALLOWED) && newValue != opener_) {
            if (opener_ == null || newValue == null || newValue == Context.getUndefinedValue()) {
                newValue = null;
            }
            else {
                throw Context.reportRuntimeError("Can't set opener!");
            }
        }
        if (getBrowserVersion().hasFeature(JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT)
            && newValue != null && newValue != Context.getUndefinedValue() && !(newValue instanceof Window)) {
            throw Context.reportRuntimeError("Can't set opener to something other than a window!");
        }
        opener_ = newValue;
    }

    /**
     * Returns the (i)frame in which the window is contained.
     * @return <code>null</code> for a top level window
     */
    @JsxGetter
    public Object getFrameElement() {
        final WebWindow window = getWebWindow();
        if (window instanceof FrameWindow) {
            return ((FrameWindow) window).getFrameElement().getScriptObject();
        }
        return null;
    }

    /**
     * Returns the value of the frames property.
     * @return the value of the frames property
     */
    @JsxGetter(propertyName = "frames")
    public WindowProxy getFrames_js() {
        return windowProxy_;
    }

    /**
     * Returns the number of frames contained by this window.
     * @return the number of frames contained by this window
     */
    @JsxGetter
    public int getLength() {
        return getFrames().getLength();
    }

    /**
     * Returns the live collection of frames contained by this window.
     * @return the live collection of frames contained by this window
     */
    private HTMLCollection getFrames() {
        if (frames_ == null) {
            final HtmlPage page = (HtmlPage) getWebWindow().getEnclosedPage();
            frames_ = new HTMLCollectionFrames(page);
        }
        return frames_;
    }

    /**
     * Returns the WebWindow associated with this Window.
     * @return the WebWindow
     */
    public WebWindow getWebWindow() {
        return webWindow_;
    }

    /**
     * Sets the focus to this element.
     */
    @JsxFunction
    public void focus() {
        final WebWindow window = getWebWindow();
        window.getWebClient().setCurrentWindow(window);
    }

    /**
     * Removes focus from this element.
     */
    @JsxFunction
    public void blur() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.blur() not implemented");
        }
    }

    /**
     * Closes this window.
     */
    @JsxFunction
    public void close() {
        final WebWindow webWindow = getWebWindow();
        if (webWindow instanceof TopLevelWindow) {
            ((TopLevelWindow) webWindow).close();
        }
        else {
            webWindow.getWebClient().deregisterWebWindow(webWindow);
        }
    }

    /**
     * Indicates if this window is closed.
     * @return <code>true</code> if this window is closed
     */
    @JsxGetter
    @CanSetReadOnly(CanSetReadOnlyStatus.IGNORE)
    public boolean getClosed() {
        final WebWindow webWindow = getWebWindow();
        return !webWindow.getWebClient().containsWebWindow(webWindow);
    }

    /**
     * Does nothing.
     * @param x the horizontal position
     * @param y the vertical position
     */
    @JsxFunction
    public void moveTo(final int x, final int y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.moveTo() not implemented");
        }
    }

    /**
     * Does nothing.
     * @param x the horizontal position
     * @param y the vertical position
     */
    @JsxFunction
    public void moveBy(final int x, final int y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.moveBy() not implemented");
        }
    }

    /**
     * Loads the new HTML document corresponding to the specified URL.
     * @param url the location of the new HTML document to load
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536638%28VS.85%29.aspx">MSDN Documentation</a>
     */
    @JsxFunction(@WebBrowser(IE))
    public void navigate(final String url) throws IOException {
        getLocation().assign(url);
    }

    /**
     * Does nothing.
     * @param width the width offset
     * @param height the height offset
     */
    @JsxFunction
    public void resizeBy(final int width, final int height) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.resizeBy() not implemented");
        }
    }

    /**
     * Does nothing.
     * @param width the width of the Window in pixel after resize
     * @param height the height of the Window in pixel after resize
     */
    @JsxFunction
    public void resizeTo(final int width, final int height) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.resizeTo() not implemented");
        }
    }

    /**
     * Scrolls to the specified location on the page.
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    @JsxFunction
    public void scroll(final int x, final int y) {
        scrollTo(x, y);
    }

    /**
     * Scrolls the window content the specified distance.
     * @param x the horizontal distance to scroll by
     * @param y the vertical distance to scroll by
     */
    @JsxFunction
    public void scrollBy(final int x, final int y) {
        final HTMLElement body = ((HTMLDocument) document_).getBody();
        if (body != null) {
            body.setScrollLeft(body.getScrollLeft() + x);
            body.setScrollTop(body.getScrollTop() + y);
        }
    }

    /**
     * Scrolls the window content down by the specified number of lines.
     * @param lines the number of lines to scroll down
     */
    @JsxFunction(@WebBrowser(FF))
    public void scrollByLines(final int lines) {
        final HTMLElement body = ((HTMLDocument) document_).getBody();
        if (body != null) {
            body.setScrollTop(body.getScrollTop() + (19 * lines));
        }
    }

    /**
     * Scrolls the window content down by the specified number of pages.
     * @param pages the number of pages to scroll down
     */
    @JsxFunction(@WebBrowser(FF))
    public void scrollByPages(final int pages) {
        final HTMLElement body = ((HTMLDocument) document_).getBody();
        if (body != null) {
            body.setScrollTop(body.getScrollTop() + (getInnerHeight() * pages));
        }
    }

    /**
     * Scrolls to the specified location on the page.
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    @JsxFunction
    public void scrollTo(final int x, final int y) {
        final HTMLElement body = ((HTMLDocument) document_).getBody();
        if (body != null) {
            body.setScrollLeft(x);
            body.setScrollTop(y);
        }
    }

    /**
     * Sets the value of the onload event handler.
     * @param newOnload the new handler
     */
    @JsxSetter
    public void setOnload(final Object newOnload) {
        if (getBrowserVersion().hasFeature(EVENT_ONLOAD_UNDEFINED_THROWS_ERROR)
            && Context.getUndefinedValue().equals(newOnload)) {
            throw Context.reportRuntimeError("Invalid onload value: undefined.");
        }
        getEventListenersContainer().setEventHandlerProp("load", newOnload);
    }

    /**
     * Sets the value of the onclick event handler.
     * @param newOnload the new handler
     */
    @JsxSetter
    public void setOnclick(final Object newOnload) {
        getEventListenersContainer().setEventHandlerProp("click", newOnload);
    }

    /**
     * Returns the onclick property (caution this is not necessary a function if something else has
     * been set).
     * @return the onclick property
     */
    @JsxGetter
    public Object getOnclick() {
        return getEventListenersContainer().getEventHandlerProp("click");
    }

    /**
     * Sets the value of the ondblclick event handler.
     * @param newHandler the new handler
     */
    @JsxSetter
    public void setOndblclick(final Object newHandler) {
        getEventListenersContainer().setEventHandlerProp("dblclick", newHandler);
    }

    /**
     * Returns the ondblclick property (caution this is not necessary a function if something else has
     * been set).
     * @return the ondblclick property
     */
    @JsxGetter
    public Object getOndblclick() {
        return getEventListenersContainer().getEventHandlerProp("dblclick");
    }

    /**
     * Returns the onload property. Note that this is not necessarily a function if something else has been set.
     * @return the onload property
     */
    @JsxGetter
    public Object getOnload() {
        final Object onload = getEventListenersContainer().getEventHandlerProp("load");
        if (onload == null) {
            // NB: for IE, the onload of window is the one of the body element but not for Mozilla.
            final HtmlPage page = (HtmlPage) getWebWindow().getEnclosedPage();
            final HtmlElement body = page.getBody();
            if (body != null) {
                final HTMLBodyElement b = (HTMLBodyElement) body.getScriptObject();
                return b.getEventHandler("onload");
            }
            return null;
        }
        return onload;
    }

    /**
     * Returns the onhashchange property (caution this is not necessary a function if something else has
     * been set).
     * @return the onhashchange property
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF), @WebBrowser(CHROME) })
    public Object getOnhashchange() {
        return getEventListenersContainer().getEventHandlerProp(Event.TYPE_HASH_CHANGE);
    }

    /**
     * Sets the value of the onhashchange event handler.
     * @param newHandler the new handler
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF), @WebBrowser(CHROME) })
    public void setOnhashchange(final Object newHandler) {
        getEventListenersContainer().setEventHandlerProp(Event.TYPE_HASH_CHANGE, newHandler);
    }

    /**
     * Gets the container for event listeners.
     * @return the container (newly created if needed)
     */
    public EventListenersContainer getEventListenersContainer() {
        if (eventListenersContainer_ == null) {
            eventListenersContainer_ = new EventListenersContainer(this);
        }
        return eventListenersContainer_;
    }

    /**
     * Allows the registration of event listeners on the event target.
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536343.aspx">MSDN documentation</a>
     * @return <code>true</code> if the listener has been added
     */
    @JsxFunction(@WebBrowser(value = IE, maxVersion = 9))
    public boolean attachEvent(final String type, final Function listener) {
        return getEventListenersContainer().addEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the registration of event listeners on the event target.
     * @param type the event type to listen for (like "onload")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/element.addEventListener">Mozilla documentation</a>
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 9) })
    public void addEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().addEventListener(type, listener, useCapture);
    }

    /**
     * Allows the removal of event listeners on the event target.
     * @param type the event type to listen for (like "onload")
     * @param listener the event listener
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536411.aspx">MSDN documentation</a>
     */
    @JsxFunction(@WebBrowser(value = IE, maxVersion = 9))
    public void detachEvent(final String type, final Function listener) {
        getEventListenersContainer().removeEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the removal of event listeners on the event target.
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/element.removeEventListener">Mozilla
     * documentation</a>
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 9) })
    public void removeEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().removeEventListener(type, listener, useCapture);
    }

    /**
     * Returns the value of the window's <tt>name</tt> property.
     * @return the value of the window's <tt>name</tt> property
     */
    @JsxGetter
    public String getName() {
        return getWebWindow().getName();
    }

     /**
     * Sets the value of the window's <tt>name</tt> property.
     * @param name the value of the window's <tt>name</tt> property
     */
    @JsxSetter
    public void setName(final String name) {
        getWebWindow().setName(name);
    }

    /**
     * Returns the value of the window's <tt>onbeforeunload</tt> property.
     * @return the value of the window's <tt>onbeforeunload</tt> property
     */
    @JsxGetter
    public Object getOnbeforeunload() {
        return getHandlerForJavaScript(Event.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Sets the value of the window's <tt>onbeforeunload</tt> property.
     * @param onbeforeunload the value of the window's <tt>onbeforeunload</tt> property
     */
    @JsxSetter
    public void setOnbeforeunload(final Object onbeforeunload) {
        setHandlerForJavaScript(Event.TYPE_BEFORE_UNLOAD, onbeforeunload);
    }

    /**
     * Returns the value of the window's <tt>onerror</tt> property.
     * @return the value of the window's <tt>onerror</tt> property
     */
    @JsxGetter
    public Object getOnerror() {
        return getHandlerForJavaScript(Event.TYPE_ERROR);
    }

    /**
     * Sets the value of the window's <tt>onerror</tt> property.
     * @param onerror the value of the window's <tt>onerror</tt> property
     */
    @JsxSetter
    public void setOnerror(final Object onerror) {
        setHandlerForJavaScript(Event.TYPE_ERROR, onerror);
    }

    /**
     * Triggers the <tt>onerror</tt> handler, if one has been set.
     * @param e the error that needs to be reported
     */
    public void triggerOnError(final ScriptException e) {
        final Object o = getOnerror();
        if (o instanceof Function) {
            final Function f = (Function) o;
            final String msg = e.getMessage();
            final String url = e.getPage().getUrl().toExternalForm();
            final int line = e.getFailingLineNumber();

            Object[] args;
            if (getBrowserVersion().hasFeature(JS_WINDOW_ONERROR_COLUMN_ARGUMENT)) {
                final int column = e.getFailingColumnNumber();
                args = new Object[] {msg, url, Integer.valueOf(line), Integer.valueOf(column)};
            }
            else {
                args = new Object[] {msg, url, Integer.valueOf(line)};
            }
            f.call(Context.getCurrentContext(), this, this, args);
        }
    }

    private Object getHandlerForJavaScript(final String eventName) {
        return getEventListenersContainer().getEventHandlerProp(eventName);
    }

    private void setHandlerForJavaScript(final String eventName, final Object handler) {
        if (handler == null || handler instanceof Function) {
            getEventListenersContainer().setEventHandlerProp(eventName, handler);
        }
        // Otherwise, fail silently.
    }

    /**
     * {@inheritDoc}
     */
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (!getBrowserVersion().hasFeature(JS_WINDOW_IS_A_FUNCTION)) {
            throw Context.reportRuntimeError("Window is not a function.");
        }
        if (args.length > 0) {
            final Object arg = args[0];
            if (arg instanceof String) {
                return ScriptableObject.getProperty(this, (String) arg);
            }
            if (arg instanceof Number) {
                return ScriptableObject.getProperty(this, ((Number) arg).intValue());
            }
        }
        return Context.getUndefinedValue();
    }

    /**
     * {@inheritDoc}
     */
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        if (!getBrowserVersion().hasFeature(JS_WINDOW_IS_A_FUNCTION)) {
            throw Context.reportRuntimeError("Window is not a function.");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        Object result = NOT_FOUND;

        final DomNode domNode = getDomNodeOrNull();
        if (domNode != null) {

            // May be attempting to retrieve a frame by name.
            final HtmlPage page = (HtmlPage) domNode.getPage();
            result = getFrameWindowByName(page, name);

            if (result == NOT_FOUND) {
                result = getElementsByName(page, name);

                if (result == NOT_FOUND) {
                    // May be attempting to retrieve element by ID (try map-backed operation again instead of XPath).
                    try {
                        final HtmlElement htmlElement = page.getHtmlElementById(name);
                        result = getScriptableFor(htmlElement);
                    }
                    catch (final ElementNotFoundException e) {
                        result = NOT_FOUND;
                    }
                }
            }

            if (result instanceof Window) {
                final WebWindow webWindow = ((Window) result).getWebWindow();
                result = getProxy(webWindow);
            }
            else if (result instanceof HTMLUnknownElement && getBrowserVersion()
                    .hasFeature(JS_XML_IN_HTML_VIA_ACTIVEXOBJECT)) {
                final HtmlElement unknownElement = ((HTMLUnknownElement) result).getDomNodeOrDie();
                if ("xml".equals(unknownElement.getNodeName())) {
                    final MSXMLActiveXObjectFactory factory =
                            getWebWindow().getWebClient().getMSXMLActiveXObjectFactory();
                    final XMLDOMDocument document = (XMLDOMDocument) factory.create("Microsoft.XMLDOM", getWebWindow());
                    final Iterator<HtmlElement> children = unknownElement.getHtmlElementDescendants().iterator();
                    if (children.hasNext()) {
                        final HtmlElement root = children.next();
                        document.loadXML(root.asXml().trim());
                    }
                    result = document;
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        final HTMLCollection frames = getFrames();
        if (index >= frames.getLength()) {
            return Context.getUndefinedValue();
        }
        return frames.item(Integer.valueOf(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, final Scriptable start) {
        if ("Option".equals(name)) {
            name = "HTMLOptionElement";
        }
        else if ("Image".equals(name)) {
            name = "HTMLImageElement";
        }
        return super.get(name, start);
    }

    private static Object getFrameWindowByName(final HtmlPage page, final String name) {
        try {
            return page.getFrameByName(name).getScriptObject();
        }
        catch (final ElementNotFoundException e) {
            return NOT_FOUND;
        }
    }

    private Object getElementsByName(final HtmlPage page, final String name) {
        Object result = NOT_FOUND;

        // May be attempting to retrieve element(s) by name. IMPORTANT: We're using map-backed operations
        // like getHtmlElementsByName() and getHtmlElementById() as much as possible, so as to avoid XPath
        // overhead. We only use an XPath-based operation when we have to (where there is more than one
        // matching element). This optimization appears to improve performance in certain situations by ~15%
        // vs using XPath-based operations throughout.
        final List<DomElement> elements = page.getElementsByName(name);

        final boolean includeFormFields = getBrowserVersion().hasFeature(JS_WINDOW_FORMFIELDS_ACCESSIBLE_BY_NAME);
        final Predicate filter = new Predicate() {
            @Override
            public boolean evaluate(final Object object) {
                if (object instanceof HtmlEmbed
                    || object instanceof HtmlForm
                    || object instanceof HtmlImage
                    || object instanceof HtmlObject) {
                    return true;
                }
                if (includeFormFields && (
                        object instanceof HtmlAnchor
                        || object instanceof HtmlButton
                        || object instanceof HtmlInput
                        || object instanceof HtmlMap
                        || object instanceof HtmlSelect
                        || object instanceof HtmlTextArea)) {
                    return true;
                }
                return false;
            }
        };
        CollectionUtils.filter(elements, filter);

        if (elements.size() == 1) {
            result = getScriptableFor(elements.get(0));
        }
        else if (elements.size() > 1) {
            // Null must be changed to '' for proper collection initialization.
            final String expElementName = "null".equals(name) ? "" : name;

            result = new HTMLCollection(page, true, "Window.getElementsByName('" + name + "')") {
                @Override
                protected List<Object> computeElements() {
                    final List<DomElement> elements = page.getElementsByName(expElementName);
                    CollectionUtils.filter(elements, filter);

                    return new ArrayList<Object>(elements);
                }

                @Override
                protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                    if ("name".equals(event.getName())) {
                        return EffectOnCache.RESET;
                    }
                    return EffectOnCache.NONE;
                }
            };
        }

        return result;
    }

    /**
     * Returns the proxy for the specified window.
     * @param w the window whose proxy is to be returned
     * @return the proxy for the specified window
     */
    public static WindowProxy getProxy(final WebWindow w) {
        return ((Window) w.getScriptObject()).windowProxy_;
    }

    /**
     * Executes the specified script code as long as the language is JavaScript or JScript.
     * @param script the script code to execute
     * @param language the language of the specified code ("JavaScript" or "JScript")
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536420.aspx">MSDN documentation</a>
     */
    @JsxFunction(@WebBrowser(value = IE, maxVersion = 9))
    public void execScript(final String script, final Object language) {
        final String languageStr = Context.toString(language);
        if (language == Undefined.instance
            || "javascript".equalsIgnoreCase(languageStr) || "jscript".equalsIgnoreCase(languageStr)) {
            ScriptRuntime.evalSpecial(Context.getCurrentContext(), this, this, new Object[] {script}, null, 0);
        }
        else if ("vbscript".equalsIgnoreCase(languageStr)) {
            throw Context.reportRuntimeError("VBScript not supported in Window.execScript().");
        }
        else {
            // Unrecognized language: use the IE error message ("Invalid class string").
            throw Context.reportRuntimeError("Invalid class string");
        }
    }

    /**
     * Returns the text from the status line.
     * @return the status line text
     */
    @JsxGetter
    public String getStatus() {
        return status_;
    }

    /**
     * Sets the text from the status line.
     * @param message the status line text
     */
    @JsxSetter
    public void setStatus(final String message) {
        status_ = message;

        final StatusHandler statusHandler = webWindow_.getWebClient().getStatusHandler();
        if (statusHandler != null) {
            statusHandler.statusMessageChanged(webWindow_.getEnclosedPage(), message);
        }
    }

    /**
     * Sets a chunk of JavaScript to be invoked each time a specified number of milliseconds has elapsed.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536749.aspx">MSDN documentation</a>
     * @param code specifies the function pointer or string that indicates the code to be executed
     *        when the specified interval has elapsed
     * @param timeout specifies the number of milliseconds
     * @param language specifies language
     * @return the id of the created interval
     */
    @JsxFunction
    public int setInterval(final Object code, int timeout, final Object language) {
        if (timeout == 0 && getBrowserVersion().hasFeature(JS_SET_INTERVAL_ZERO_TIMEOUT_FORCES_SET_TIMEOUT)) {
            return setTimeout(code, timeout, language);
        }

        if (timeout < MIN_TIMER_DELAY) {
            timeout = MIN_TIMER_DELAY;
        }
        final int id;
        final WebWindow w = getWebWindow();
        final Page page = (Page) getDomNodeOrNull();
        final String description = "window.setInterval(" + timeout + ")";
        if (code == null) {
            throw Context.reportRuntimeError("Function not provided.");
        }
        else if (code instanceof String) {
            final String s = (String) code;
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                createJavaScriptJob(timeout, Integer.valueOf(timeout), description, w, s);
            id = getWebWindow().getJobManager().addJob(job, page);
        }
        else if (code instanceof Function) {
            final Function f = (Function) code;
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                createJavaScriptJob(timeout, Integer.valueOf(timeout), description, w, f);
            id = getWebWindow().getJobManager().addJob(job, page);
        }
        else {
            throw Context.reportRuntimeError("Unknown type for function.");
        }
        return id;
    }

    /**
     * Cancels the interval previously started using the setInterval method.
     * Current implementation does nothing.
     * @param intervalID specifies the interval to cancel as returned by the setInterval method
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536353.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public void clearInterval(final int intervalID) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearInterval(" + intervalID + ")");
        }
        getWebWindow().getJobManager().removeJob(intervalID);
    }

    /**
     * Returns the innerWidth.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref28.html">Mozilla doc</a>
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getInnerWidth() {
        return getWebWindow().getInnerWidth();
    }

    /**
     * Returns the outerWidth.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref79.html">Mozilla doc</a>
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getOuterWidth() {
        return getWebWindow().getOuterWidth();
    }

    /**
     * Returns the innerHeight.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref27.html">Mozilla doc</a>
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getInnerHeight() {
        return getWebWindow().getInnerHeight();
    }

    /**
     * Returns the outer height.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref78.html">Mozilla doc</a>
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getOuterHeight() {
        return getWebWindow().getOuterHeight();
    }

    /**
     * Prints the current page. The current implementation does nothing.
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref85.html">
     * Mozilla documentation</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536672.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public void print() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.print() not implemented");
        }
    }

    /**
     * Does nothing special anymore... just like FF.
     * @param type the type of events to capture
     * @see Document#captureEvents(String)
     */
    @JsxFunction({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void captureEvents(final String type) {
        // Empty.
    }

    /**
     * An undocumented IE function.
     */
    @JsxFunction(@WebBrowser(IE))
    public void CollectGarbage() {
        // Empty.
    }

    /**
     * Returns computed style of the element. Computed style represents the final computed values
     * of all CSS properties for the element. This method's return value is of the same type as
     * that of <tt>element.style</tt>, but the value returned by this method is read-only.
     *
     * @param element the element
     * @param pseudo a string specifying the pseudo-element to match (may be <tt>null</tt>)
     * @return the computed style
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 9), @WebBrowser(CHROME) })
    public ComputedCSSStyleDeclaration getComputedStyle(final Element element, final String pseudo) {
        ComputedCSSStyleDeclaration style;

        synchronized (computedStyles_) {
            style = computedStyles_.get(element);
        }
        if (style != null) {
            return style;
        }

        final CSSStyleDeclaration original = element.getStyle();
        style = new ComputedCSSStyleDeclaration(original);

        final StyleSheetList sheets = ((HTMLDocument) document_).getStyleSheets();
        final boolean trace = LOG.isTraceEnabled();
        for (int i = 0; i < sheets.getLength(); i++) {
            final CSSStyleSheet sheet = (CSSStyleSheet) sheets.item(i);
            if (sheet.isActive()) {
                if (trace) {
                    LOG.trace("modifyIfNecessary: " + sheet + ", " + style + ", " + element);
                }
                sheet.modifyIfNecessary(style, element);
            }
        }

        synchronized (computedStyles_) {
            computedStyles_.put(element, style);
        }

        return style;
    }

    /**
     * Returns the current selection.
     * @return the current selection
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public Selection getSelection() {
        final WebWindow webWindow = getWebWindow();
        // return null if the window is in a frame that is not displayed
        if (webWindow instanceof FrameWindow) {
            final FrameWindow frameWindow = (FrameWindow) webWindow;
            if (!frameWindow.getFrameElement().isDisplayed()) {
                return null;
            }
        }
        return getSelectionImpl();
    }

    /**
     * Returns the current selection.
     * @return the current selection
     */
    public Selection getSelectionImpl() {
        if (selection_ == null) {
            selection_ = new Selection();
            selection_.setParentScope(this);
            selection_.setPrototype(getPrototype(selection_.getClass()));
        }
        return selection_;
    }

    /**
     * Creates a modal dialog box that displays the specified HTML document.
     * @param url the URL of the document to load and display
     * @param arguments object to be made available via <tt>window.dialogArguments</tt> in the dialog window
     * @param features string that specifies the window ornaments for the dialog window
     * @return the value of the <tt>returnValue</tt> property as set by the modal dialog's window
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536759.aspx">MSDN Documentation</a>
     * @see <a href="https://developer.mozilla.org/en/DOM/window.showModalDialog">Mozilla Documentation</a>
     */
    @JsxFunction({ @WebBrowser(IE), @WebBrowser(FF) })
    public Object showModalDialog(final String url, final Object arguments, final String features) {
        final WebWindow webWindow = getWebWindow();
        final WebClient client = webWindow.getWebClient();
        try {
            final URL completeUrl = ((HtmlPage) getDomNodeOrDie()).getFullyQualifiedUrl(url);
            final DialogWindow dialog = client.openDialogWindow(completeUrl, webWindow, arguments);
            // TODO: Theoretically, we shouldn't return until the dialog window has been close()'ed...
            // But we have to return so that the window can be close()'ed...
            // Maybe we can use Rhino's continuation support to save state and restart when
            // the dialog window is close()'ed? Would only work in interpreted mode, though.
            final ScriptableObject jsDialog = (ScriptableObject) dialog.getScriptObject();
            return jsDialog.get("returnValue", jsDialog);
        }
        catch (final IOException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Creates a modeless dialog box that displays the specified HTML document.
     * @param url the URL of the document to load and display
     * @param arguments object to be made available via <tt>window.dialogArguments</tt> in the dialog window
     * @param features string that specifies the window ornaments for the dialog window
     * @return a reference to the new window object created for the modeless dialog
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536761.aspx">MSDN Documentation</a>
     */
    @JsxFunction(@WebBrowser(IE))
    public Object showModelessDialog(final String url, final Object arguments, final String features) {
        final WebWindow webWindow = getWebWindow();
        final WebClient client = webWindow.getWebClient();
        try {
            final URL completeUrl = ((HtmlPage) getDomNodeOrDie()).getFullyQualifiedUrl(url);
            final DialogWindow dialog = client.openDialogWindow(completeUrl, webWindow, arguments);
            final Window jsDialog = (Window) dialog.getScriptObject();
            return jsDialog;
        }
        catch (final IOException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Gets the controllers. The result doesn't currently matter but it is important to return an
     * object as some JavaScript libraries check it.
     * @see <a href="https://developer.mozilla.org/En/DOM/Window.controllers">Mozilla documentation</a>
     * @return some object
     */
    @JsxGetter(@WebBrowser(FF))
    public Object getControllers() {
        return controllers_;
    }

    /**
     * Sets the controllers.
     * @param value the new value
     */
    @JsxSetter(@WebBrowser(FF))
    public void setControllers(final Object value) {
        controllers_ = value;
    }

    /**
     * Returns the value of "mozInnerScreenX" property.
     * @return the value of "mozInnerScreenX" property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getMozInnerScreenX() {
        return 8;
    }

    /**
     * Returns the value of "mozInnerScreenY" property.
     * @return the value of "mozInnerScreenY" property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getMozInnerScreenY() {
        return 91;
    }

    /**
     * Returns the value of "mozPaintCount" property.
     * @return the value of "mozPaintCount" property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getMozPaintCount() {
        return 0;
    }

    /** Definition of special cases for the smart DomHtmlAttributeChangeListenerImpl */
    private static final Set<String> ATTRIBUTES_AFFECTING_PARENT = new HashSet<String>(Arrays.asList(
            "style",
            "class",
            "height",
            "width"));

    /**
     * <p>Listens for changes anywhere in the document and evicts cached computed styles whenever something relevant
     * changes. Note that the very lazy way of doing this (completely clearing the cache every time something happens)
     * results in very meager performance gains. In order to get good (but still correct) performance, we need to be
     * a little smarter.</p>
     *
     * <p>CSS 2.1 has the following <a href="http://www.w3.org/TR/CSS21/selector.html">selector types</a> (where "SN" is
     * shorthand for "the selected node"):</p>
     *
     * <ol>
     *   <li><em>Universal</em> (i.e. "*"): Affected by the removal of SN from the document.</li>
     *   <li><em>Type</em> (i.e. "div"): Affected by the removal of SN from the document.</li>
     *   <li><em>Descendant</em> (i.e. "div span"): Affected by changes to SN or to any of its ancestors.</li>
     *   <li><em>Child</em> (i.e. "div > span"): Affected by changes to SN or to its parent.</li>
     *   <li><em>Adjacent Sibling</em> (i.e. "table + p"): Affected by changes to SN or its previous sibling.</li>
     *   <li><em>Attribute</em> (i.e. "div.up, div[class~=up]"): Affected by changes to an attribute of SN.</li>
     *   <li><em>ID</em> (i.e. "#header): Affected by changes to the <tt>id</tt> attribute of SN.</li>
     *   <li><em>Pseudo-Elements and Pseudo-Classes</em> (i.e. "p:first-child"): Affected by changes to parent.</li>
     * </ol>
     *
     * <p>Together, these rules dictate that the smart (but still lazy) way of removing elements from the computed style
     * cache is as follows -- whenever a node changes in any way, the cache needs to be cleared of styles for nodes
     * which:</p>
     *
     * <ul>
     *   <li>are actually the same node as the node that changed</li>
     *   <li>are siblings of the node that changed</li>
     *   <li>are descendants of the node that changed</li>
     * </ul>
     *
     * <p>Additionally, whenever a <tt>style</tt> node or a <tt>link</tt> node with <tt>rel=stylesheet</tt> is added or
     * removed, all elements should be removed from the computed style cache.</p>
     */
    private class DomHtmlAttributeChangeListenerImpl implements DomChangeListener, HtmlAttributeChangeListener {

        /**
         * {@inheritDoc}
         */
        public void nodeAdded(final DomChangeEvent event) {
            nodeChanged(event.getChangedNode(), null);
        }

        /**
         * {@inheritDoc}
         */
        public void nodeDeleted(final DomChangeEvent event) {
            nodeChanged(event.getChangedNode(), null);
        }

        /**
         * {@inheritDoc}
         */
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        /**
         * {@inheritDoc}
         */
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        /**
         * {@inheritDoc}
         */
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        private void nodeChanged(final DomNode changed, final String attribName) {
            // If a stylesheet was changed, all of our calculations could be off; clear the cache.
            if (changed instanceof HtmlStyle) {
                synchronized (computedStyles_) {
                    computedStyles_.clear();
                }
                return;
            }
            if (changed instanceof HtmlLink) {
                final String rel = ((HtmlLink) changed).getRelAttribute().toLowerCase(Locale.ENGLISH);
                if ("stylesheet".equals(rel)) {
                    synchronized (computedStyles_) {
                        computedStyles_.clear();
                    }
                    return;
                }
            }
            // Apparently it wasn't a stylesheet that changed; be semi-smart about what we evict and when.
            synchronized (computedStyles_) {
                final boolean clearParents = ATTRIBUTES_AFFECTING_PARENT.contains(attribName);
                final Iterator<Map.Entry<Node, ComputedCSSStyleDeclaration>> i = computedStyles_.entrySet().iterator();
                while (i.hasNext()) {
                    final Map.Entry<Node, ComputedCSSStyleDeclaration> entry = i.next();
                    final DomNode node = entry.getKey().getDomNodeOrDie();
                    if (changed == node
                        || changed.getParentNode() == node.getParentNode()
                        || changed.isAncestorOf(node)
                        || (clearParents && node.isAncestorOf(changed))) {
                        i.remove();
                    }
                }
            }
        }
    }

    /**
     * Gets the name of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/efy5bay1.aspx">MSDN doc</a>
     * @return "JScript"
     */
    @JsxFunction(@WebBrowser(IE))
    public String ScriptEngine() {
        return "JScript";
    }

    /**
     * Gets the build version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/yftk84kt.aspx">MSDN doc</a>
     * @return the build version
     */
    @JsxFunction(@WebBrowser(IE))
    public int ScriptEngineBuildVersion() {
        return 12345;
    }

    /**
     * Gets the major version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/x7cbaet3.aspx">MSDN doc</a>
     * @return the major version
     */
    @JsxFunction(@WebBrowser(IE))
    public int ScriptEngineMajorVersion() {
        if (getBrowserVersion().getBrowserVersionNumeric() < 10) {
            return 5;
        }
        return (int) getBrowserVersion().getBrowserVersionNumeric();
    }

    /**
     * Gets the minor version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/wzaz8hhz.aspx">MSDN doc</a>
     * @return the minor version
     */
    @JsxFunction(@WebBrowser(IE))
    public int ScriptEngineMinorVersion() {
        if (getBrowserVersion().getBrowserVersionNumeric() < 10) {
            return (int) getBrowserVersion().getBrowserVersionNumeric();
        }
        return 0;
    }

    /**
     * Should implement the stop() function on the window object.
     * (currently empty implementation)
     * @see <a href="https://developer.mozilla.org/en/DOM/window.stop">window.stop</a>
     */
    @JsxFunction({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public void stop() {
        //empty
    }

    /**
     * Returns the value of "pageXOffset" property.
     * @return the value of "pageXOffset" property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getPageXOffset() {
        return 0;
    }

    /**
     * Returns the value of "pageYOffset" property.
     * @return the value of "pageYOffset" property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getPageYOffset() {
        return 0;
    }

    /**
     * Returns the value of "scrollX" property.
     * @return the value of "scrollX" property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getScrollX() {
        return 0;
    }

    /**
     * Returns the value of "scrollY" property.
     * @return the value of "scrollY" property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getScrollY() {
        return 0;
    }

    /**
     * Returns the value of "netscape" property.
     * @return the value of "netscape" property
     */
    @JsxGetter(@WebBrowser(FF))
    public Netscape getNetscape() {
        return new Netscape(this);
    }

    /**
     * Executes the event on this object only (needed for instance for onload on (i)frame tags).
     * @param event the event
     * @return the result
     */
    public ScriptResult executeEvent(final Event event) {
        return executeEvent(event, eventListenersContainer_);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Executes the event on this window only. Internal helper to share the impl with
     * Node.java.
     * @param event the event
     * @param eventListenersContainer the container with the listeners
     * @return the result
     */
    protected ScriptResult executeEvent(final Event event, final EventListenersContainer eventListenersContainer) {
        if (eventListenersContainer != null) {
            final boolean eventParam = getBrowserVersion().hasFeature(
                    JS_EVENT_HANDLER_AS_PROPERTY_DONT_RECEIVE_EVENT);
            final Object[] args = new Object[] {event};

            // handlers declared as property on a node don't receive the event as argument for IE
            final Object[] propHandlerArgs;
            if (eventParam) {
                propHandlerArgs = ArrayUtils.EMPTY_OBJECT_ARRAY;
            }
            else {
                propHandlerArgs = args;
            }

            setCurrentEvent(event);
            try {
                return eventListenersContainer.executeListeners(event, args, propHandlerArgs);
            }
            finally {
                setCurrentEvent(null); // reset event
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * Used to allow re-declaration of constants (eg: "var undefined;").
     * @see com.gargoylesoftware.htmlunit.javascript.NativeGlobalTest
     */
    @Override
    public boolean isConst(final String name) {
        if ("undefined".equals(name) || "Infinity".equals(name) || "NaN".equals(name)) {
            return false;
        }

        return super.isConst(name);
    }

    /**
     * Dispatches an event into the event system (standards-conformant browsers only). See
     * <a href="https://developer.mozilla.org/en-US/docs/DOM/window.dispatchEvent">the Gecko
     * DOM reference</a> for more information.
     *
     * @param event the event to be dispatched
     * @return <tt>false</tt> if at least one of the event handlers which handled the event
     *         called <tt>preventDefault</tt>; <tt>true</tt> otherwise
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public boolean dispatchEvent(final Event event) {
        event.setTarget(this);
        final ScriptResult result = Node.fireEvent(this, event);
        return !event.isAborted(result);
    }

    /**
     * Getter for the onchange event handler.
     * @return the handler
     */
    @JsxGetter({@WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME),
        @WebBrowser(value = IE, minVersion = 11) })
    public Object getOnchange() {
        return getHandlerForJavaScript(Event.TYPE_CHANGE);
    }

    /**
     * Setter for the onchange event handler.
     * @param onchange the handler
     */
    @JsxSetter({@WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME),
        @WebBrowser(value = IE, minVersion = 11) })
    public void setOnchange(final Object onchange) {
        setHandlerForJavaScript(Event.TYPE_CHANGE, onchange);
    }

    /**
     * Posts a message.
     * @param message the object passed to the window
     * @param targetOrigin the origin this window must be for the event to be dispatched
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window.postMessage">MDN documentation</a>
     */
    @JsxFunction
    public void postMessage(final String message, final String targetOrigin) {
        final URL currentURL = getWebWindow().getEnclosedPage().getUrl();
        if (!"*".equals(targetOrigin)) {
            URL targetURL = null;
            try {
                targetURL = new URL(targetOrigin);
            }
            catch (final Exception e) {
                Context.throwAsScriptRuntimeEx(
                        new Exception("SyntaxError: An invalid or illegal string was specified."));
            }

            if (!getBrowserVersion().hasFeature(JS_WINDOW_POST_MESSAGE_ALLOW_INVALID_PORT)
                    && getPort(targetURL) != getPort(currentURL)) {
                return;
            }
            if (!targetURL.getHost().equals(currentURL.getHost())) {
                return;
            }
            if (!targetURL.getProtocol().equals(currentURL.getProtocol())) {
                return;
            }
        }
        final MessageEvent event = new MessageEvent();
        final String origin = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();
        final boolean cancelable = getBrowserVersion().hasFeature(JS_WINDOW_POST_MESSAGE_CANCELABLE);
        event.initMessageEvent(Event.TYPE_MESSAGE, false, cancelable, message, origin, "", this);
        event.setParentScope(this);
        event.setPrototype(getPrototype(event.getClass()));

        if (getBrowserVersion().hasFeature(JS_WINDOW_POST_MESSAGE_SYNCHRONOUS)) {
            dispatchEvent(event);
            return;
        }

        final PostponedAction action = new PostponedAction(getDomNodeOrDie().getPage()) {
            @Override
            public void execute() throws Exception {
                dispatchEvent(event);
            }
        };
        getWebWindow().getWebClient().getJavaScriptEngine().addPostponedAction(action);
    }

    private static int getPort(final URL url) {
        int port = url.getPort();
        if (port == -1) {
            if ("http".equals(url.getProtocol())) {
                port = 80;
            }
            else {
                port = 443;
            }
        }
        return port;
    }
}

class HTMLCollectionFrames extends HTMLCollection {
    private static final Log LOG = LogFactory.getLog(HTMLCollectionFrames.class);

    public HTMLCollectionFrames(final HtmlPage page) {
        super(page, false, "Window.frames");
    }

    @Override
    protected boolean isMatching(final DomNode node) {
        return node instanceof BaseFrameElement;
    }

    @Override
    protected Scriptable getScriptableForElement(final Object obj) {
        final WebWindow window;
        if (obj instanceof BaseFrameElement) {
            window = ((BaseFrameElement) obj).getEnclosedWindow();
        }
        else {
            window = ((FrameWindow) obj).getFrameElement().getEnclosedWindow();
        }

        return Window.getProxy(window);
    }

    @Override
    protected Object getWithPreemption(final String name) {
        final List<Object> elements = getElements();

        for (final Object next : elements) {
            final BaseFrameElement frameElt = (BaseFrameElement) next;
            final WebWindow window = frameElt.getEnclosedWindow();
            if (name.equals(window.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Property \"" + name + "\" evaluated (by name) to " + window);
                }
                return getScriptableForElement(window);
            }
            if (getBrowserVersion().hasFeature(JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID)
                    && frameElt.getAttribute("id").equals(name)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Property \"" + name + "\" evaluated (by id) to " + window);
                }
                return getScriptableForElement(window);
            }
        }

        return NOT_FOUND;
    }

    @Override
    protected void addElementIds(final List<String> idList, final List<Object> elements) {
        for (final Object next : elements) {
            final BaseFrameElement frameElt = (BaseFrameElement) next;
            final WebWindow window = frameElt.getEnclosedWindow();
            final String windowName = window.getName();
            if (windowName != null) {
                idList.add(windowName);
            }
        }
    }

}
