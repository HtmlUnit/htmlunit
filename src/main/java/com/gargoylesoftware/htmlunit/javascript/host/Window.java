/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_FORMFIELDS_ACCESSIBLE_BY_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_FRAME_BY_ID_RETURNS_WINDOW;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_SELECTION_NULL_IF_INVISIBLE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_TOP_WRITABLE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
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

import org.apache.commons.codec.binary.Base64;
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
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnly;
import com.gargoylesoftware.htmlunit.javascript.configuration.CanSetReadOnlyStatus;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.crypto.Crypto;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS2Properties;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.MediaQueryList;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleMedia;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.DataTransfer;
import com.gargoylesoftware.htmlunit.javascript.host.html.DocumentProxy;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.performance.Performance;
import com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesis;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.svg.SvgPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Window}.
 *
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
public class Window extends EventTarget implements ScriptableWithFallbackGetter, Function {

    private static final Log LOG = LogFactory.getLog(Window.class);

    /** To be documented. */
    @JsxConstant(@WebBrowser(CHROME))
    public static final short TEMPORARY = 0;

    /** To be documented. */
    @JsxConstant(@WebBrowser(CHROME))
    public static final short PERSISTENT = 1;

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
    private Map<Class<? extends Scriptable>, Scriptable> prototypes_ = new HashMap<>();
    private Map<String, Scriptable> prototypesPerJSName_ = new HashMap<>();
    private Object controllers_;
    private Object opener_;
    private Object top_ = NOT_FOUND; // top can be set from JS to any value!
    private Crypto crypto_;

    /**
     * Cache computed styles when possible, because their calculation is very expensive.
     * We use a weak hash map because we don't want this cache to be the only reason
     * nodes are kept around in the JVM, if all other references to them are gone.
     */
    private transient WeakHashMap<Node, CSS2Properties> computedStyles_ = new WeakHashMap<>();

    private final Map<Type, Storage> storages_ = new HashMap<>();

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38), @WebBrowser(EDGE) })
    public Window() {
    }

    /**
     * Restores the transient {@link #computedStyles_} map during deserialization.
     * @param stream the stream to read the object from
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if a class is not found
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        computedStyles_ = new WeakHashMap<>();
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
     * Returns the prototype object corresponding to the specified HtmlUnit class inside the window scope.
     * @param className the class name whose prototype is to be returned
     * @return the prototype object corresponding to the specified class inside the specified scope
     */
    public Scriptable getPrototype(final String className) {
        return prototypesPerJSName_.get(className);
    }

    /**
     * Sets the prototypes for HtmlUnit host classes.
     * @param map a Map of ({@link Class}, {@link Scriptable})
     * @param prototypesPerJSName map of {@link String} and {@link Scriptable}
     */
    public void setPrototypes(final Map<Class<? extends Scriptable>, Scriptable> map,
            final Map<String, Scriptable> prototypesPerJSName) {
        prototypes_ = map;
        prototypesPerJSName_ = prototypesPerJSName;
    }

    /**
     * The JavaScript function {@code alert()}.
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
            handler.handleAlert(document_.getPage(), stringMessage);
        }
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    @JsxFunction
    public String btoa(final String stringToEncode) {
        return new String(Base64.encodeBase64(stringToEncode.getBytes()));
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding..
     * @param encodedData the encoded string
     * @return the decoded value
     */
    @JsxFunction
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
        return handler.handleConfirm(document_.getPage(), message);
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
        return handler.handlePrompt(document_.getPage(), message);
    }

    /**
     * Returns the JavaScript property {@code document}.
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
    @JsxGetter
    public ApplicationCache getApplicationCache() {
        return applicationCache_;
    }

    /**
     * Returns the current event (used by JavaScript only when emulating IE).
     * @return the current event, or {@code null} if no event is currently available
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(CHROME) })
    public Object getEvent() {
        return currentEvent_;
    }

    /**
     * Returns the current event (used internally regardless of the emulation mode).
     * @return the current event, or {@code null} if no event is currently available
     */
    public Event getCurrentEvent() {
        return currentEvent_;
    }

    /**
     * Sets the current event.
     * @param event the current event
     */
    public void setCurrentEvent(final Event event) {
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
     * @return the newly opened window, or {@code null} if popup windows have been disabled
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
     * Returns the JavaScript property {@code navigator}.
     * @return the navigator
     */
    @JsxGetter
    public Navigator getNavigator() {
        return navigator_;
    }

    /**
     * Returns the JavaScript property {@code clientInformation}.
     * @return the client information
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(CHROME) })
    public Navigator getClientInformation() {
        return navigator_;
    }

    /**
     * Returns the JavaScript property {@code clipboardData}.
     * @return the {@link DataTransfer}
     */
    @JsxGetter(@WebBrowser(IE))
    public DataTransfer getClipboardData() {
        final DataTransfer dataTransfer = new DataTransfer();
        dataTransfer.setParentScope(this);
        dataTransfer.setPrototype(getPrototype(dataTransfer.getClass()));
        return dataTransfer;
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
     * Returns the {@code self} property.
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
    @JsxGetter
    public ScriptableObject getConsole() {
        return console_;
    }

    /**
     * Sets the console.
     * @param console the console
     */
    @JsxSetter
    public void setConsole(final ScriptableObject console) {
        console_ = console;
    }

    /**
     * Prints messages to the console.
     * @param message the message to log
     */
    @JsxFunction(@WebBrowser(FF))
    public void dump(final String message) {
        if (console_ instanceof Console) {
            Console.log(null, console_, new Object[] {message}, null);
        }
    }

    /**
     * Dummy implementation for requestAnimationFrame.
     * @param callback the function to call when it's time to update the animation
     * @return an identification id
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window/requestAnimationFrame">MDN Doc</a>
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 10), @WebBrowser(CHROME) })
    public int requestAnimationFrame(final Object callback) {
        // nothing for now
        return 1;
    }

    /**
     * Dummy implementation for cancelAnimationFrame.
     * @param requestId the ID value returned by the call to window.requestAnimationFrame()
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Window/cancelAnimationFrame">MDN Doc</a>
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 10), @WebBrowser(CHROME) })
    public void cancelAnimationFrame(final Object requestId) {
        // nothing for now
    }

    /**
     * Returns the {@code screen} property.
     * @return the screen property
     */
    @JsxGetter
    public Screen getScreen() {
        return screen_;
    }

    /**
     * Returns the {@code history} property.
     * @return the {@code history} property
     */
    @JsxGetter
    public History getHistory() {
        return history_;
    }

    /**
     * Returns the {@code external} property.
     * @return the {@code external} property
     */
    @JsxGetter
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
        webWindow_.setScriptableObject(this);

        windowProxy_ = new WindowProxy(webWindow_);

        final Page enclosedPage = webWindow.getEnclosedPage();
        if (enclosedPage instanceof XmlPage || enclosedPage instanceof SvgPage) {
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
                opener_ = opener.getScriptableObject();
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
            clearEventListenersContainer();

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
        if (getBrowserVersion().hasFeature(JS_WINDOW_TOP_WRITABLE)) {
            top_ = o;
        }
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
     * @return the value of window.opener, {@code null} for a top level window
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
    public void setOpener(final Object newValue) {
        if (getBrowserVersion().hasFeature(JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT)
            && newValue != null && newValue != Context.getUndefinedValue() && !(newValue instanceof Window)) {
            throw Context.reportRuntimeError("Can't set opener to something other than a window!");
        }
        opener_ = newValue;
    }

    /**
     * Returns the (i)frame in which the window is contained.
     * @return {@code null} for a top level window
     */
    @JsxGetter
    public Object getFrameElement() {
        final WebWindow window = getWebWindow();
        if (window instanceof FrameWindow) {
            return ((FrameWindow) window).getFrameElement().getScriptableObject();
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
     * @return {@code true} if this window is closed
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
     * Returns the onload property. Note that this is not necessarily a function if something else has been set.
     * @return the onload property
     */
    @JsxGetter
    public Object getOnload() {
        final Object onload = getHandlerForJavaScript("load");
        if (onload == null) {
            // NB: for IE, the onload of window is the one of the body element but not for Mozilla.
            final HtmlPage page = (HtmlPage) getWebWindow().getEnclosedPage();
            final HtmlElement body = page.getBody();
            if (body != null) {
                final HTMLBodyElement b = (HTMLBodyElement) body.getScriptableObject();
                return b.getEventHandler("onload");
            }
            return null;
        }
        return onload;
    }

    /**
     * Sets the value of the onload event handler.
     * @param newOnload the new handler
     */
    @JsxSetter
    public void setOnload(final Object newOnload) {
        getEventListenersContainer().setEventHandlerProp("load", newOnload);
    }

    /**
     * Returns the onclick property (caution this is not necessary a function if something else has
     * been set).
     * @return the onclick property
     */
    @JsxGetter
    public Object getOnclick() {
        return getHandlerForJavaScript("click");
    }

    /**
     * Sets the value of the onclick event handler.
     * @param newOnload the new handler
     */
    @JsxSetter
    public void setOnclick(final Object newOnload) {
        setHandlerForJavaScript("click", newOnload);
    }

    /**
     * Returns the ondblclick property (caution this is not necessary a function if something else has
     * been set).
     * @return the ondblclick property
     */
    @JsxGetter
    public Object getOndblclick() {
        return getHandlerForJavaScript("dblclick");
    }

    /**
     * Sets the value of the ondblclick event handler.
     * @param newHandler the new handler
     */
    @JsxSetter
    public void setOndblclick(final Object newHandler) {
        setHandlerForJavaScript("dblclick", newHandler);
    }

    /**
     * Returns the onhashchange property (caution this is not necessary a function if something else has
     * been set).
     * @return the onhashchange property
     */
    @JsxGetter
    public Object getOnhashchange() {
        return getHandlerForJavaScript(Event.TYPE_HASH_CHANGE);
    }

    /**
     * Sets the value of the onhashchange event handler.
     * @param newHandler the new handler
     */
    @JsxSetter
    public void setOnhashchange(final Object newHandler) {
        setHandlerForJavaScript(Event.TYPE_HASH_CHANGE, newHandler);
    }

    /**
     * Returns the value of the window's {@code name} property.
     * @return the value of the window's {@code name} property
     */
    @JsxGetter
    public String getName() {
        return getWebWindow().getName();
    }

     /**
     * Sets the value of the window's {@code name} property.
     * @param name the value of the window's {@code name} property
     */
    @JsxSetter
    public void setName(final String name) {
        getWebWindow().setName(name);
    }

    /**
     * Returns the value of the window's {@code onbeforeunload} property.
     * @return the value of the window's {@code onbeforeunload} property
     */
    @JsxGetter
    public Object getOnbeforeunload() {
        return getHandlerForJavaScript(Event.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Sets the value of the window's {@code onbeforeunload} property.
     * @param onbeforeunload the value of the window's {@code onbeforeunload} property
     */
    @JsxSetter
    public void setOnbeforeunload(final Object onbeforeunload) {
        setHandlerForJavaScript(Event.TYPE_BEFORE_UNLOAD, onbeforeunload);
    }

    /**
     * Returns the value of the window's {@code onerror} property.
     * @return the value of the window's {@code onerror} property
     */
    @JsxGetter
    public Object getOnerror() {
        return getHandlerForJavaScript(Event.TYPE_ERROR);
    }

    /**
     * Sets the value of the window's {@code onerror} property.
     * @param onerror the value of the window's {@code onerror} property
     */
    @JsxSetter
    public void setOnerror(final Object onerror) {
        setHandlerForJavaScript(Event.TYPE_ERROR, onerror);
    }

    /**
     * Returns the value of the window's {@code onmessage} property.
     * @return the value of the window's {@code onmessage} property
     */
    @JsxGetter
    public Object getOnmessage() {
        return getHandlerForJavaScript(Event.TYPE_MESSAGE);
    }

    /**
     * Sets the value of the window's {@code onmessage} property.
     * @param onmessage the value of the window's {@code onmessage} property
     */
    @JsxSetter
    public void setOnmessage(final Object onmessage) {
        setHandlerForJavaScript(Event.TYPE_MESSAGE, onmessage);
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

            final int column = e.getFailingColumnNumber();
            final Object[] args = new Object[] {msg, url, Integer.valueOf(line), Integer.valueOf(column), e};
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
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        throw Context.reportRuntimeError("Window is not a function.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        throw Context.reportRuntimeError("Window is not a function.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
                        if (getBrowserVersion().hasFeature(JS_WINDOW_FRAME_BY_ID_RETURNS_WINDOW)
                                && htmlElement instanceof HtmlFrame) {
                            final HtmlFrame frame = (HtmlFrame) htmlElement;
                            result = getScriptableFor(frame.getEnclosedWindow());
                        }
                        else {
                            result = getScriptableFor(htmlElement);
                        }
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

    private static Object getFrameWindowByName(final HtmlPage page, final String name) {
        try {
            return page.getFrameByName(name).getScriptableObject();
        }
        catch (final ElementNotFoundException e) {
            return NOT_FOUND;
        }
    }

    private Object getElementsByName(final HtmlPage page, final String name) {

        // May be attempting to retrieve element(s) by name. IMPORTANT: We're using map-backed operations
        // like getHtmlElementsByName() and getHtmlElementById() as much as possible, so as to avoid XPath
        // overhead. We only use an XPath-based operation when we have to (where there is more than one
        // matching element). This optimization appears to improve performance in certain situations by ~15%
        // vs using XPath-based operations throughout.
        final List<DomElement> elements = page.getElementsByName(name);

        final boolean includeFormFields = getBrowserVersion().hasFeature(JS_WINDOW_FORMFIELDS_ACCESSIBLE_BY_NAME);
        final Filter filter = new Filter(includeFormFields);

        final Iterator<DomElement> it = elements.iterator();
        while (it.hasNext()) {
            if (!filter.matches(it.next())) {
                it.remove();
            }
        }

        if (elements.size() == 0) {
            return NOT_FOUND;
        }

        if (elements.size() == 1) {
            return getScriptableFor(elements.get(0));
        }

        // Null must be changed to '' for proper collection initialization.
        final String expElementName = "null".equals(name) ? "" : name;

        return new HTMLCollection(page, true) {
            @Override
            protected List<Object> computeElements() {
                final List<DomElement> expElements = page.getElementsByName(expElementName);
                final List<Object> result = new ArrayList<>(expElements.size());

                for (DomElement domElement : expElements) {
                    if (filter.matches(domElement)) {
                        result.add(domElement);
                    }
                }
                return result;
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

    /**
     * Returns the proxy for the specified window.
     * @param w the window whose proxy is to be returned
     * @return the proxy for the specified window
     */
    public static WindowProxy getProxy(final WebWindow w) {
        return ((Window) w.getScriptableObject()).windowProxy_;
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
    @JsxGetter
    public int getInnerWidth() {
        return getWebWindow().getInnerWidth();
    }

    /**
     * Returns the outerWidth.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref79.html">Mozilla doc</a>
     */
    @JsxGetter
    public int getOuterWidth() {
        return getWebWindow().getOuterWidth();
    }

    /**
     * Returns the innerHeight.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref27.html">Mozilla doc</a>
     */
    @JsxGetter
    public int getInnerHeight() {
        return getWebWindow().getInnerHeight();
    }

    /**
     * Returns the outer height.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref78.html">Mozilla doc</a>
     */
    @JsxGetter
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
    @JsxFunction
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
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null})
     * @return the computed style
     */
    @JsxFunction
    public CSS2Properties getComputedStyle(final Element element, final String pseudoElement) {
        synchronized (computedStyles_) {
            final CSS2Properties style = computedStyles_.get(element);
            if (style != null) {
                return style;
            }
        }

        final CSSStyleDeclaration original = element.getStyle();
        final CSS2Properties style = new CSS2Properties(original);

        final StyleSheetList sheets = ((HTMLDocument) document_).getStyleSheets();
        final boolean trace = LOG.isTraceEnabled();
        for (int i = 0; i < sheets.getLength(); i++) {
            final CSSStyleSheet sheet = (CSSStyleSheet) sheets.item(i);
            if (sheet.isActive() && sheet.isEnabled()) {
                if (trace) {
                    LOG.trace("modifyIfNecessary: " + sheet + ", " + style + ", " + element);
                }
                sheet.modifyIfNecessary(style, element, pseudoElement);
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
    @JsxFunction
    public Selection getSelection() {
        final WebWindow webWindow = getWebWindow();
        // return null if the window is in a frame that is not displayed
        if (webWindow instanceof FrameWindow) {
            final FrameWindow frameWindow = (FrameWindow) webWindow;
            if (getBrowserVersion().hasFeature(JS_WINDOW_SELECTION_NULL_IF_INVISIBLE)
                    && !frameWindow.getFrameElement().isDisplayed()) {
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
     * @return the value of the {@code returnValue} property as set by the modal dialog's window
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
            final ScriptableObject jsDialog = dialog.getScriptableObject();
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
            final Window jsDialog = (Window) dialog.getScriptableObject();
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
     * Returns the value of {@code mozInnerScreenX} property.
     * @return the value of {@code mozInnerScreenX} property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getMozInnerScreenX() {
        return 11;
    }

    /**
     * Returns the value of {@code mozInnerScreenY} property.
     * @return the value of {@code mozInnerScreenY} property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getMozInnerScreenY() {
        return 91;
    }

    /**
     * Returns the value of {@code mozPaintCount} property.
     * @return the value of {@code mozPaintCount} property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getMozPaintCount() {
        return 0;
    }

    /** Definition of special cases for the smart DomHtmlAttributeChangeListenerImpl */
    private static final Set<String> ATTRIBUTES_AFFECTING_PARENT = new HashSet<>(Arrays.asList(
            "style",
            "class",
            "height",
            "width"));

    private static final class Filter {
        private final boolean includeFormFields_;

        private Filter(final boolean includeFormFields) {
            includeFormFields_ = includeFormFields;
        }

        private boolean matches(final Object object) {
            if (object instanceof HtmlEmbed
                || object instanceof HtmlForm
                || object instanceof HtmlImage
                || object instanceof HtmlObject) {
                return true;
            }
            if (includeFormFields_ && (
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
    }

    /**
     * Clears the computed styles.
     */
    public void clearComputedStyles() {
        synchronized (computedStyles_) {
            computedStyles_.clear();
        }
    }

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
        @Override
        public void nodeAdded(final DomChangeEvent event) {
            nodeChanged(event.getChangedNode(), null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void nodeDeleted(final DomChangeEvent event) {
            nodeChanged(event.getChangedNode(), null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            nodeChanged(event.getHtmlElement(), event.getName());
        }

        private void nodeChanged(final DomNode changed, final String attribName) {
            // If a stylesheet was changed, all of our calculations could be off; clear the cache.
            if (changed instanceof HtmlStyle) {
                clearComputedStyles();
                return;
            }
            if (changed instanceof HtmlLink) {
                final String rel = ((HtmlLink) changed).getRelAttribute().toLowerCase(Locale.ROOT);
                if ("stylesheet".equals(rel)) {
                    clearComputedStyles();
                    return;
                }
            }
            // Apparently it wasn't a stylesheet that changed; be semi-smart about what we evict and when.
            synchronized (computedStyles_) {
                final boolean clearParents = ATTRIBUTES_AFFECTING_PARENT.contains(attribName);
                final Iterator<Map.Entry<Node, CSS2Properties>> i = computedStyles_.entrySet().iterator();
                while (i.hasNext()) {
                    final Map.Entry<Node, CSS2Properties> entry = i.next();
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
     * Returns the value of {@code pageXOffset} property.
     * @return the value of {@code pageXOffset} property
     */
    @JsxGetter
    public int getPageXOffset() {
        return 0;
    }

    /**
     * Returns the value of {@code pageYOffset} property.
     * @return the value of {@code pageYOffset} property
     */
    @JsxGetter
    public int getPageYOffset() {
        return 0;
    }

    /**
     * Returns the value of {@code scrollX} property.
     * @return the value of {@code scrollX} property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public int getScrollX() {
        return 0;
    }

    /**
     * Returns the value of {@code scrollY} property.
     * @return the value of {@code scrollY} property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public int getScrollY() {
        return 0;
    }

    /**
     * Returns the value of {@code netscape} property.
     * @return the value of {@code netscape} property
     */
    @JsxGetter(@WebBrowser(FF))
    public Netscape getNetscape() {
        return new Netscape(this);
    }

    /**
     * {@inheritDoc}
     * Used to allow re-declaration of constants (eg: "var undefined;").
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
     * @return {@code false} if at least one of the event handlers which handled the event
     *         called <tt>preventDefault</tt>; {@code true} otherwise
     */
    @Override
    @JsxFunction
    public boolean dispatchEvent(final Event event) {
        event.setTarget(this);
        final ScriptResult result = fireEvent(event);
        return !event.isAborted(result);
    }

    /**
     * Getter for the onchange event handler.
     * @return the handler
     */
    @JsxGetter({@WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(IE) })
    public Object getOnchange() {
        return getHandlerForJavaScript(Event.TYPE_CHANGE);
    }

    /**
     * Setter for the onchange event handler.
     * @param onchange the handler
     */
    @JsxSetter({@WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(IE) })
    public void setOnchange(final Object onchange) {
        setHandlerForJavaScript(Event.TYPE_CHANGE, onchange);
    }

    /**
     * Getter for the onsubmit event handler.
     * @return the handler
     */
    @JsxGetter({@WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(IE) })
    public Object getOnsubmit() {
        return getHandlerForJavaScript(Event.TYPE_SUBMIT);
    }

    /**
     * Setter for the onsubmit event handler.
     * @param onsubmit the handler
     */
    @JsxSetter({@WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(IE) })
    public void setOnsubmit(final Object onsubmit) {
        setHandlerForJavaScript(Event.TYPE_SUBMIT, onsubmit);
    }

    /**
     * Posts a message.
     * @param message the object passed to the window
     * @param targetOrigin the origin this window must be for the event to be dispatched
     * @param transfer an optional sequence of Transferable objects
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window.postMessage">MDN documentation</a>
     */
    @JsxFunction
    public void postMessage(final String message, final String targetOrigin, final Object transfer) {
        final URL currentURL = getWebWindow().getEnclosedPage().getUrl();

        // TODO: do the same origin check for '/' also
        if (!"*".equals(targetOrigin) && !"/".equals(targetOrigin)) {
            URL targetURL = null;
            try {
                targetURL = new URL(targetOrigin);
            }
            catch (final Exception e) {
                Context.throwAsScriptRuntimeEx(
                        new Exception(
                                "SyntaxError: Failed to execute 'postMessage' on 'Window': Invalid target origin '"
                                + targetOrigin + "' was specified (reason: " + e.getMessage() + "."));
            }

            if (getPort(targetURL) != getPort(currentURL)) {
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
        event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin, "", this, transfer);
        event.setParentScope(this);
        event.setPrototype(getPrototype(event.getClass()));

        final JavaScriptEngine jsEngine = getWebWindow().getWebClient().getJavaScriptEngine();
        final PostponedAction action = new PostponedAction(getDomNodeOrDie().getPage()) {
            @Override
            public void execute() throws Exception {
                final ContextAction contextAction = new ContextAction() {
                    @Override
                    public Object run(final Context cx) {
                        return dispatchEvent(event);
                    }
                };

                final ContextFactory cf = jsEngine.getContextFactory();
                cf.call(contextAction);
            }
        };
        jsEngine.addPostponedAction(action);
    }

    /**
     * Returns the port of the specified URL.
     * @param url the URL
     * @return the port
     */
    public static int getPort(final URL url) {
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

    /**
     * Returns the {@code performance} property.
     * @return the {@code performance} property
     */
    @JsxGetter
    public Performance getPerformance() {
        final Performance performance = new Performance();
        performance.setParentScope(this);
        performance.setPrototype(getPrototype(performance.getClass()));
        return performance;
    }

    /**
     * Returns the {@code devicePixelRatio} property.
     * @return the {@code devicePixelRatio} property
     */
    @JsxGetter
    public int getDevicePixelRatio() {
        return 1;
    }

    /**
     * Returns the {@code styleMedia} property.
     * @return the {@code styleMedia} property
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(IE) })
    public StyleMedia getStyleMedia() {
        final StyleMedia styleMedia = new StyleMedia();
        styleMedia.setParentScope(this);
        styleMedia.setPrototype(getPrototype(styleMedia.getClass()));
        return styleMedia;
    }

    /**
     * Returns a new MediaQueryList object representing the parsed results of the specified media query string.
     *
     * @param mediaQueryString the media query
     * @return a new MediaQueryList object
     */
    @JsxFunction
    public MediaQueryList matchMedia(final String mediaQueryString) {
        final MediaQueryList mediaQueryList = new MediaQueryList(mediaQueryString);
        mediaQueryList.setParentScope(this);
        mediaQueryList.setPrototype(getPrototype(mediaQueryList.getClass()));
        return mediaQueryList;
    }

    /**
     * Stub only at the moment.
     * @param search the text string for which to search
     * @param caseSensitive if true, specifies a case-sensitive search
     * @param backwards if true, specifies a backward search
     * @param wrapAround if true, specifies a wrap around search
     * @param wholeWord if true, specifies a whole word search
     * @param searchInFrames if true, specifies a search in frames
     * @param showDialog if true, specifies a show Dialog.
     * @return false
     */
    @JsxFunction({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public boolean find(final String search, final boolean caseSensitive,
            final boolean backwards, final boolean wrapAround,
            final boolean wholeWord, final boolean searchInFrames, final boolean showDialog) {
        return false;
    }

    /**
     * Returns the {@code speechSynthesis} property.
     * @return the {@code speechSynthesis} property
     */
    @JsxGetter(@WebBrowser(CHROME))
    public SpeechSynthesis getSpeechSynthesis() {
        final SpeechSynthesis speechSynthesis = new SpeechSynthesis();
        speechSynthesis.setParentScope(this);
        speechSynthesis.setPrototype(getPrototype(speechSynthesis.getClass()));
        return speechSynthesis;
    }

    /**
     * Returns the {@code offscreenBuffering} property.
     * @return the {@code offscreenBuffering} property
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(IE) })
    public Object getOffscreenBuffering() {
        if (getBrowserVersion().hasFeature(JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID)) {
            return "auto";
        }
        return true;
    }

    /**
     * Returns the crypto property.
     * @return the crypto property
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public Crypto getCrypto() {
        if (crypto_ == null) {
            crypto_ = new Crypto(this);
        }
        return crypto_;
    }
}

class HTMLCollectionFrames extends HTMLCollection {
    private static final Log LOG = LogFactory.getLog(HTMLCollectionFrames.class);

    HTMLCollectionFrames(final HtmlPage page) {
        super(page, false);
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
            if (getBrowserVersion().hasFeature(JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID) && frameElt.getId().equals(name)) {
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
