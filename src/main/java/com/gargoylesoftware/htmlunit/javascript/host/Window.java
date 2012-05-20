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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.DialogWindow;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.BaseFrame;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.host.Storage.Type;
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
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535873.aspx">MSDN documentation</a>
 */
public class Window extends SimpleScriptable implements ScriptableWithFallbackGetter, Function {

    private static final Log LOG = LogFactory.getLog(Window.class);

    /** HtmlUnit's "window" width, in pixels. */
    public static final int WINDOW_WIDTH = 1256;

    /** HtmlUnit's "window" height, in pixels. */
    public static final int WINDOW_HEIGHT = 605;

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
    private OfflineResourceList applicationCache_;
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
    public void jsxFunction_alert(final Object message) {
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
    public String jsxFunction_btoa(final String stringToEncode) {
        return new String(Base64.encodeBase64(stringToEncode.getBytes()));
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding..
     * @param encodedData the encoded string
     * @return the decoded value
     */
    public String jsxFunction_atob(final String encodedData) {
        return new String(Base64.decodeBase64(encodedData.getBytes()));
    }

    /**
     * The JavaScript function "confirm()".
     * @param message the message
     * @return true if ok was pressed, false if cancel was pressed
     */
    public boolean jsxFunction_confirm(final String message) {
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
    public String jsxFunction_prompt(final String message) {
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
    public DocumentProxy jsxGet_document() {
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
    public OfflineResourceList jsxGet_applicationCache() {
        return applicationCache_;
    }

    /**
     * Returns the current event (used by JavaScript only when emulating IE).
     * @return the current event, or <tt>null</tt> if no event is currently available
     */
    public Object jsxGet_event() {
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
     * @see WebClient#isPopupBlockerEnabled()
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536651.aspx">MSDN documentation</a>
     */
    public WindowProxy jsxFunction_open(final Object url, final Object name, final Object features,
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
        boolean replaceCurrentEntryInBrowsingHistory = false;
        if (replace != Undefined.instance) {
            replaceCurrentEntryInBrowsingHistory = ((Boolean) replace).booleanValue();
        }
        final WebClient webClient = webWindow_.getWebClient();

        if (webClient.isPopupBlockerEnabled()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignoring window.open() invocation because popups are blocked.");
            }
            return null;
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
            final WebWindow webWindow;
            try {
                webWindow = webClient.getWebWindowByName(windowName);
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
    public Popup jsxFunction_createPopup() {
        final Popup popup = new Popup();
        popup.setParentScope(this);
        popup.setPrototype(getPrototype(Popup.class));
        popup.init(this);
        return popup;
    }

    private URL makeUrlForOpenWindow(final String urlString) {
        if (urlString.length() == 0) {
            return WebClient.URL_ABOUT_BLANK;
        }

        try {
            final Page page = webWindow_.getEnclosedPage();
            if (page != null && page instanceof HtmlPage) {
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
    public int jsxFunction_setTimeout(final Object code, int timeout, final Object language) {
        if (timeout < MIN_TIMER_DELAY) {
            timeout = MIN_TIMER_DELAY;
        }
        final int id;
        final WebWindow w = getWebWindow();
        final Page page = (Page) getDomNodeOrNull();
        if (code == null) {
            throw Context.reportRuntimeError("Function not provided.");
        }
        else if (code instanceof String) {
            final String s = (String) code;
            final String description = "window.setTimeout(" + s + ", " + timeout + ")";
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavaScriptJob(timeout, null, description, w, s);
            id = getWebWindow().getJobManager().addJob(job, page);
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
                    createJavaScriptJob(timeout, null, description, w, f);
            id = getWebWindow().getJobManager().addJob(job, page);
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
    public void jsxFunction_clearTimeout(final int timeoutId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearTimeout(" + timeoutId + ")");
        }
        getWebWindow().getJobManager().removeJob(timeoutId);
    }

    /**
     * Returns the JavaScript property "navigator".
     * @return the navigator
     */
    public Navigator jsxGet_navigator() {
        return navigator_;
    }

    /**
     * Returns the JavaScript property "clientInformation".
     * @return the client information
     */
    public Navigator jsxGet_clientInformation() {
        return navigator_;
    }

    /**
     * Returns the JavaScript property "clipboardData".
     * @return the ClipboardData
     */
    public ClipboardData jsxGet_clipboardData() {
        final ClipboardData clipboardData = new ClipboardData();
        clipboardData.setParentScope(this);
        clipboardData.setPrototype(getPrototype(clipboardData.getClass()));
        return clipboardData;
    }

    /**
     * Returns the window property. This is a synonym for "self".
     * @return the window property (a reference to <tt>this</tt>)
     */
    public WindowProxy jsxGet_window() {
        return windowProxy_;
    }

    /**
     * Returns the "self" property.
     * @return this
     */
    public WindowProxy jsxGet_self() {
        return windowProxy_;
    }

    /**
     * Returns the localStorage property.
     * @return the localStorage property
     */
    public Storage jsxGet_localStorage() {
        final Storage storage = new Storage();
        storage.setParentScope(this);
        storage.setPrototype(getPrototype(storage.getClass()));
        storage.setType(Type.LOCAL_STORAGE);
        return storage;
    }

    /**
     * Returns the sessionStorage property.
     * @return the sessionStorage property
     */
    public Storage jsxGet_sessionStorage() {
        final Storage storage = new Storage();
        storage.setParentScope(this);
        storage.setPrototype(getPrototype(storage.getClass()));
        storage.setType(Type.SESSION_STORAGE);
        return storage;
    }

    /**
     * Returns the globalStorage property.
     * @return the globalStorage property
     */
    public StorageList jsxGet_globalStorage() {
        final StorageList list = new StorageList();
        list.setParentScope(this);
        list.setPrototype(getPrototype(list.getClass()));
        return list;
    }

    /**
     * Returns the location property.
     * @return the location property
     */
    public Location jsxGet_location() {
        return location_;
    }

    /**
     * Sets the location property. This will cause a reload of the window.
     * @param newLocation the URL of the new content
     * @throws IOException when location loading fails
     */
    public void jsxSet_location(final String newLocation) throws IOException {
        location_.jsxSet_href(newLocation);
    }

    /**
     * Returns the "screen" property.
     * @return the screen property
     */
    public Screen jsxGet_screen() {
        return screen_;
    }

    /**
     * Returns the "history" property.
     * @return the "history" property
     */
    public History jsxGet_history() {
        return history_;
    }

    /**
     * Returns the "external" property.
     * @return the "external" property
     */
    public External jsxGet_external() {
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

        if (webWindow.getEnclosedPage() instanceof XmlPage) {
            document_ = new XMLDocument();
        }
        else {
            document_ = new HTMLDocument();
        }
        document_.setParentScope(this);
        document_.setPrototype(getPrototype(document_.getClass()));
        document_.setWindow(this);

        if (webWindow.getEnclosedPage() instanceof SgmlPage) {
            final SgmlPage page = (SgmlPage) webWindow.getEnclosedPage();
            document_.setDomNode(page);

            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            page.addDomChangeListener(listener);

            if (page instanceof HtmlPage) {
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

        applicationCache_ = new OfflineResourceList();
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
        if (enclosedPage instanceof HtmlPage) {
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
    public Object jsxGet_top() {
        if (top_ != NOT_FOUND) {
            return top_;
        }

        final WebWindow top = webWindow_.getTopWindow();
        return getProxy(top);
    }

    /**
     * Sets the value of the top property.
     * @param o the new value
     */
    public void jsxSet_top(final Object o) {
        top_ = o;
    }

    /**
     * Returns the value of the parent property.
     * @return the value of window.parent
     */
    public WindowProxy jsxGet_parent() {
        final WebWindow parent = webWindow_.getParentWindow();
        return getProxy(parent);
    }

    /**
     * Returns the value of the opener property.
     * @return the value of window.opener, <tt>null</tt> for a top level window
     */
    public Object jsxGet_opener() {
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
    public void jsxSet_opener(Object newValue) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_CHANGE_OPENER_NOT_ALLOWED)
                && newValue != opener_) {
            if (opener_ == null || newValue == null || newValue == Context.getUndefinedValue()) {
                newValue = null;
            }
            else {
                throw Context.reportRuntimeError("Can't set opener!");
            }
        }
        opener_ = newValue;
    }

    /**
     * Returns the (i)frame in which the window is contained.
     * @return <code>null</code> for a top level window
     */
    public Object jsxGet_frameElement() {
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
    public WindowProxy jsxGet_frames() {
        return windowProxy_;
    }

    /**
     * Returns the number of frames contained by this window.
     * @return the number of frames contained by this window
     */
    public int jsxGet_length() {
        return getFrames().jsxGet_length();
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
    public void jsxFunction_focus() {
        webWindow_.getWebClient().setCurrentWindow(webWindow_);
    }

    /**
     * Removes focus from this element.
     */
    public void jsxFunction_blur() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.blur() not implemented");
        }
    }

    /**
     * Closes this window.
     */
    public void jsxFunction_close() {
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
    public boolean jsxGet_closed() {
        return !getWebWindow().getWebClient().getWebWindows().contains(getWebWindow());
    }

    /**
     * Does nothing.
     * @param x the horizontal position
     * @param y the vertical position
     */
    public void jsxFunction_moveTo(final int x, final int y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.moveTo() not implemented");
        }
    }

    /**
     * Does nothing.
     * @param x the horizontal position
     * @param y the vertical position
     */
    public void jsxFunction_moveBy(final int x, final int y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.moveBy() not implemented");
        }
    }

    /**
     * Does nothing.
     * @param width the width offset
     * @param height the height offset
     */
    public void jsxFunction_resizeBy(final int width, final int height) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.resizeBy() not implemented");
        }
    }

    /**
     * Does nothing.
     * @param width the width of the Window in pixel after resize
     * @param height the height of the Window in pixel after resize
     */
    public void jsxFunction_resizeTo(final int width, final int height) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.resizeTo() not implemented");
        }
    }

    /**
     * Scrolls to the specified location on the page.
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    public void jsxFunction_scroll(final int x, final int y) {
        jsxFunction_scrollTo(x, y);
    }

    /**
     * Scrolls the window content the specified distance.
     * @param x the horizontal distance to scroll by
     * @param y the vertical distance to scroll by
     */
    public void jsxFunction_scrollBy(final int x, final int y) {
        final HTMLElement body = ((HTMLDocument) document_).jsxGet_body();
        if (body != null) {
            body.jsxSet_scrollLeft(body.jsxGet_scrollLeft() + x);
            body.jsxSet_scrollTop(body.jsxGet_scrollTop() + y);
        }
    }

    /**
     * Scrolls the window content down by the specified number of lines.
     * @param lines the number of lines to scroll down
     */
    public void jsxFunction_scrollByLines(final int lines) {
        final HTMLElement body = ((HTMLDocument) document_).jsxGet_body();
        if (body != null) {
            body.jsxSet_scrollTop(body.jsxGet_scrollTop() + (19 * lines));
        }
    }

    /**
     * Scrolls the window content down by the specified number of pages.
     * @param pages the number of pages to scroll down
     */
    public void jsxFunction_scrollByPages(final int pages) {
        final HTMLElement body = ((HTMLDocument) document_).jsxGet_body();
        if (body != null) {
            body.jsxSet_scrollTop(body.jsxGet_scrollTop() + (WINDOW_HEIGHT * pages));
        }
    }

    /**
     * Scrolls to the specified location on the page.
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    public void jsxFunction_scrollTo(final int x, final int y) {
        final HTMLElement body = ((HTMLDocument) document_).jsxGet_body();
        if (body != null) {
            body.jsxSet_scrollLeft(x);
            body.jsxSet_scrollTop(y);
        }
    }

    /**
     * Sets the value of the onload event handler.
     * @param newOnload the new handler
     */
    public void jsxSet_onload(final Object newOnload) {
        getEventListenersContainer().setEventHandlerProp("load", newOnload);
    }

    /**
     * Sets the value of the onclick event handler.
     * @param newOnload the new handler
     */
    public void jsxSet_onclick(final Object newOnload) {
        getEventListenersContainer().setEventHandlerProp("click", newOnload);
    }

    /**
     * Returns the onclick property (caution this is not necessary a function if something else has
     * been set).
     * @return the onclick property
     */
    public Object jsxGet_onclick() {
        return getEventListenersContainer().getEventHandlerProp("click");
    }

    /**
     * Sets the value of the ondblclick event handler.
     * @param newHandler the new handler
     */
    public void jsxSet_ondblclick(final Object newHandler) {
        getEventListenersContainer().setEventHandlerProp("dblclick", newHandler);
    }

    /**
     * Returns the ondblclick property (caution this is not necessary a function if something else has
     * been set).
     * @return the ondblclick property
     */
    public Object jsxGet_ondblclick() {
        return getEventListenersContainer().getEventHandlerProp("dblclick");
    }

    /**
     * Returns the onload property. Note that this is not necessarily a function if something else has been set.
     * @return the onload property
     */
    public Object jsxGet_onload() {
        final Object onload = getEventListenersContainer().getEventHandlerProp("load");
        if (onload == null) {
            // NB: for IE, the onload of window is the one of the body element but not for Mozilla.
            final HtmlPage page = (HtmlPage) webWindow_.getEnclosedPage();
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
    public boolean jsxFunction_attachEvent(final String type, final Function listener) {
        return getEventListenersContainer().addEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the registration of event listeners on the event target.
     * @param type the event type to listen for (like "onload")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.addEventListener">Mozilla documentation</a>
     */
    public void jsxFunction_addEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().addEventListener(type, listener, useCapture);
    }

    /**
     * Allows the removal of event listeners on the event target.
     * @param type the event type to listen for (like "onload")
     * @param listener the event listener
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536411.aspx">MSDN documentation</a>
     */
    public void jsxFunction_detachEvent(final String type, final Function listener) {
        getEventListenersContainer().removeEventListener(StringUtils.substring(type, 2), listener, false);
    }

    /**
     * Allows the removal of event listeners on the event target.
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:element.removeEventListener">Mozilla documentation</a>
     */
    public void jsxFunction_removeEventListener(final String type, final Function listener, final boolean useCapture) {
        getEventListenersContainer().removeEventListener(type, listener, useCapture);
    }

    /**
     * Returns the value of the window's <tt>name</tt> property.
     * @return the value of the window's <tt>name</tt> property
     */
    public String jsxGet_name() {
        return webWindow_.getName();
    }

     /**
     * Sets the value of the window's <tt>name</tt> property.
     * @param name the value of the window's <tt>name</tt> property
     */
    public void jsxSet_name(final String name) {
        webWindow_.setName(name);
    }

    /**
     * Returns the value of the window's <tt>onbeforeunload</tt> property.
     * @return the value of the window's <tt>onbeforeunload</tt> property
     */
    public Object jsxGet_onbeforeunload() {
        return getHandlerForJavaScript(Event.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Sets the value of the window's <tt>onbeforeunload</tt> property.
     * @param onbeforeunload the value of the window's <tt>onbeforeunload</tt> property
     */
    public void jsxSet_onbeforeunload(final Object onbeforeunload) {
        setHandlerForJavaScript(Event.TYPE_BEFORE_UNLOAD, onbeforeunload);
    }

    /**
     * Returns the value of the window's <tt>onerror</tt> property.
     * @return the value of the window's <tt>onerror</tt> property
     */
    public Object jsxGet_onerror() {
        return getHandlerForJavaScript(Event.TYPE_ERROR);
    }

    /**
     * Sets the value of the window's <tt>onerror</tt> property.
     * @param onerror the value of the window's <tt>onerror</tt> property
     */
    public void jsxSet_onerror(final Object onerror) {
        setHandlerForJavaScript(Event.TYPE_ERROR, onerror);
    }

    /**
     * Triggers the <tt>onerror</tt> handler, if one has been set.
     * @param e the error that needs to be reported
     */
    public void triggerOnError(final ScriptException e) {
        final Object o = jsxGet_onerror();
        if (o instanceof Function) {
            final Function f = (Function) o;
            final String msg = e.getMessage();
            final String url = e.getPage().getWebResponse().getWebRequest().getUrl().toExternalForm();
            final int line = e.getFailingLineNumber();
            final Object[] args = new Object[] {msg, url, Integer.valueOf(line)};
            f.call(Context.getCurrentContext(), this, this, args);
        }
    }

    private Object getHandlerForJavaScript(final String eventName) {
        Object handler = getEventListenersContainer().getEventHandlerProp(eventName);
        if (handler == null && !getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_129)) {
            handler = Scriptable.NOT_FOUND;
        }
        return handler;
    }

    private void setHandlerForJavaScript(final String eventName, final Object handler) {
        if (handler instanceof Function) {
            getEventListenersContainer().setEventHandlerProp(eventName, handler);
        }
        // Otherwise, fail silently.
    }

    /**
     * {@inheritDoc}
     */
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_IS_NOT_A_FUNCTION)) {
            throw Context.reportRuntimeError("Window is not a function.");
        }
        if (args.length > 0) {
            final Object arg = args[0];
            if (arg instanceof String) {
                return ScriptableObject.getProperty(this, (String) arg);
            }
            else if (arg instanceof Number) {
                return ScriptableObject.getProperty(this, ((Number) arg).intValue());
            }
        }
        return Context.getUndefinedValue();
    }

    /**
     * {@inheritDoc}
     */
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WINDOW_IS_NOT_A_FUNCTION)) {
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
                // May be attempting to retrieve element(s) by name. IMPORTANT: We're using map-backed operations
                // like getHtmlElementsByName() and getHtmlElementById() as much as possible, so as to avoid XPath
                // overhead. We only use an XPath-based operation when we have to (where there is more than one
                // matching element). This optimization appears to improve performance in certain situations by ~15%
                // vs using XPath-based operations throughout.
                final List<HtmlElement> elements = page.getElementsByName(name);
                if (elements.size() == 1) {
                    result = getScriptableFor(elements.get(0));
                }
                else if (elements.size() > 1) {
                    result = ((HTMLDocument) document_).jsxFunction_getElementsByName(name);
                }
                else {
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
                    .hasFeature(BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
                final HtmlElement unknownElement = ((HTMLUnknownElement) result).getDomNodeOrDie();
                if ("xml".equals(unknownElement.getNodeName())) {
                    final XMLDocument document = ActiveXObject.buildXMLDocument(getWebWindow());
                    document.setParentScope(this);
                    final Iterator<HtmlElement> children = unknownElement.getHtmlElementDescendants().iterator();
                    if (children.hasNext()) {
                        final HtmlElement root = children.next();
                        document.jsxFunction_loadXML(root.asXml().trim());
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
        if (index >= frames.jsxGet_length()) {
            return Context.getUndefinedValue();
        }
        return frames.jsxFunction_item(Integer.valueOf(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, final Scriptable start) {
        // Hack to make eval work in other window scope when needed.
        // See unit test testEvalScopeOtherWindow().
        // TODO: Find a cleaner way to handle this.
        if ("eval".equals(name)) {
            final Window w = (Window) getTopScope(getStartingScope());
            if (w != this) {
                return getAssociatedValue("custom_eval");
            }
        }
        else if ("Option".equals(name)) {
            name = "HTMLOptionElement";
        }
        else if ("Image".equals(name)) {
            name = "HTMLImageElement";
        }
        return super.get(name, start);
    }

    private static Scriptable getTopScope(final Scriptable s) {
        Scriptable top = s;
        while (top != null && top.getParentScope() != null) {
            top = top.getParentScope();
        }
        return top;
    }

    private static Object getFrameWindowByName(final HtmlPage page, final String name) {
        try {
            return page.getFrameByName(name).getScriptObject();
        }
        catch (final ElementNotFoundException e) {
            return NOT_FOUND;
        }
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
     * Executes the specified script code as long as the language is JavaScript or JScript. Does
     * nothing if the language specified is VBScript.
     * Note: MSDN doc says that the function returns null but in fact this is undefined.
     * @param script the script code to execute
     * @param language the language of the specified code ("JavaScript", "JScript" or "VBScript")
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536420.aspx">MSDN documentation</a>
     */
    public void jsxFunction_execScript(final String script, final Object language) {
        final String languageStr = Context.toString(language);
        if (language == Undefined.instance
            || "javascript".equalsIgnoreCase(languageStr) || "jscript".equalsIgnoreCase(languageStr)) {
            custom_eval(script);
        }
        else if ("vbscript".equalsIgnoreCase(languageStr)) {
            LOG.warn("VBScript not supported in Window.execScript().");
        }
        else {
            // Unrecognized language: use the IE error message ("Invalid class string").
            throw Context.reportRuntimeError("Invalid class string");
        }
    }

    /**
     * Executes the specified script code in the scope of this window.
     * This is used only when eval() is called on a Window other than the starting scope
     * @param scriptCode some JavaScript code
     * @return the evaluation result
     */
    public Object custom_eval(final String scriptCode) {
        final Context context = Context.getCurrentContext();
        final Script script = context.compileString(scriptCode, "eval body", 0, null);
        return script.exec(context, this);
    }

    /**
     * Returns the text from the status line.
     * @return the status line text
     */
    public String jsxGet_status() {
        return status_;
    }

    /**
     * Sets the text from the status line.
     * @param message the status line text
     */
    public void jsxSet_status(final String message) {
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
    public int jsxFunction_setInterval(final Object code, int timeout, final Object language) {
        if (timeout == 0 && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_133)) {
            return jsxFunction_setTimeout(code, timeout, language);
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
    public void jsxFunction_clearInterval(final int intervalID) {
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
    public int jsxGet_innerWidth() {
        return WINDOW_WIDTH;
    }

    /**
     * Returns the outerWidth.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref79.html">Mozilla doc</a>
     */
    public int jsxGet_outerWidth() {
        return WINDOW_WIDTH + 8;
    }

    /**
     * Returns the innerHeight.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref27.html">Mozilla doc</a>
     */
    public int jsxGet_innerHeight() {
        return WINDOW_HEIGHT;
    }

    /**
     * Returns the outer height.
     * @return a dummy value
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref78.html">Mozilla doc</a>
     */
    public int jsxGet_outerHeight() {
        return WINDOW_HEIGHT + 150;
    }

    /**
     * Prints the current page. The current implementation does nothing.
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref85.html">
     * Mozilla documentation</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536672.aspx">MSDN documentation</a>
     */
    public void jsxFunction_print() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.print() not implemented");
        }
    }

    /**
     * Does nothing special anymore... just like FF.
     * @param type the type of events to capture
     * @see Document#jsxFunction_captureEvents(String)
     */
    public void jsxFunction_captureEvents(final String type) {
        // Empty.
    }

    /**
     * An undocumented IE function.
     */
    public void jsxFunction_CollectGarbage() {
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
    public ComputedCSSStyleDeclaration jsxFunction_getComputedStyle(final Element element, final String pseudo) {
        ComputedCSSStyleDeclaration style;

        synchronized (computedStyles_) {
            style = computedStyles_.get(element);
        }
        if (style != null) {
            return style;
        }

        final CSSStyleDeclaration original = element.jsxGet_style();
        style = new ComputedCSSStyleDeclaration(original);

        final StyleSheetList sheets = ((HTMLDocument) document_).jsxGet_styleSheets();
        for (int i = 0; i < sheets.jsxGet_length(); i++) {
            final CSSStyleSheet sheet = (CSSStyleSheet) sheets.jsxFunction_item(i);
            if (sheet.isActive()) {
                if (LOG.isTraceEnabled()) {
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
    public Selection jsxFunction_getSelection() {
        // return null if the window is in a frame that is not displayed
        if (webWindow_ instanceof FrameWindow) {
            final FrameWindow frameWindow = (FrameWindow) webWindow_;
            if (!frameWindow.getFrameElement().isDisplayed()) {
                return null;
            }
        }
        return getSelection();
    }

    /**
     * Returns the current selection.
     * @return the current selection
     */
    public Selection getSelection() {
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
    public Object jsxFunction_showModalDialog(final String url, final Object arguments, final String features) {
        final WebWindow ww = getWebWindow();
        final WebClient client = ww.getWebClient();
        try {
            final URL completeUrl = ((HtmlPage) getDomNodeOrDie()).getFullyQualifiedUrl(url);
            final DialogWindow dialog = client.openDialogWindow(completeUrl, ww, arguments);
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
    public Object jsxFunction_showModelessDialog(final String url, final Object arguments, final String features) {
        final WebWindow ww = getWebWindow();
        final WebClient client = ww.getWebClient();
        try {
            final URL completeUrl = ((HtmlPage) getDomNodeOrDie()).getFullyQualifiedUrl(url);
            final DialogWindow dialog = client.openDialogWindow(completeUrl, ww, arguments);
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
    public Object jsxGet_controllers() {
        return controllers_;
    }

    /**
     * Sets the controllers.
     * @param value the new value
     */
    public void jsxSet_controllers(final Object value) {
        controllers_ = value;
    }

    /** Definition of special cases for the smart DomHtmlAttributeChangeListenerImpl **/
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
    private class DomHtmlAttributeChangeListenerImpl implements DomChangeListener, HtmlAttributeChangeListener,
        Serializable {

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
                final String rel = ((HtmlLink) changed).getRelAttribute().toLowerCase();
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
    public String jsxFunction_ScriptEngine() {
        return "JScript";
    }

    /**
     * Gets the build version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/yftk84kt.aspx">MSDN doc</a>
     * @return the build version
     */
    public int jsxFunction_ScriptEngineBuildVersion() {
        return 12345;
    }

    /**
     * Gets the major version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/x7cbaet3.aspx">MSDN doc</a>
     * @return the major version
     */
    public int jsxFunction_ScriptEngineMajorVersion() {
        return 5;
    }

    /**
     * Gets the minor version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/wzaz8hhz.aspx">MSDN doc</a>
     * @return the minor version
     */
    public int jsxFunction_ScriptEngineMinorVersion() {
        return (int) getBrowserVersion().getBrowserVersionNumeric();
    }

    /**
     * Should implement the stop() function on the window object.
     * (currently empty implementation)
     * @see <a href="https://developer.mozilla.org/en/DOM/window.stop">window.stop</a>
     */
    public void jsxFunction_stop() {
        //empty
    }

    /**
     * Returns the value of "pageXOffset" property.
     * @return the value of "pageXOffset" property
     */
    public int jsxGet_pageXOffset() {
        return 0;
    }

    /**
     * Returns the value of "pageYOffset" property.
     * @return the value of "pageYOffset" property
     */
    public int jsxGet_pageYOffset() {
        return 0;
    }

    /**
     * Returns the value of "scrollX" property.
     * @return the value of "scrollX" property
     */
    public int jsxGet_scrollX() {
        return 0;
    }

    /**
     * Returns the value of "scrollY" property.
     * @return the value of "scrollY" property
     */
    public int jsxGet_scrollY() {
        return 0;
    }
}

class HTMLCollectionFrames extends HTMLCollection {
    private static final Log LOG = LogFactory.getLog(HTMLCollectionFrames.class);

    public HTMLCollectionFrames(final HtmlPage page) {
        super(page, false, "Window.frames");
    }

    @Override
    protected boolean isMatching(final DomNode node) {
        return node instanceof BaseFrame;
    }

    @Override
    protected Scriptable getScriptableForElement(final Object obj) {
        final WebWindow window;
        if (obj instanceof BaseFrame) {
            window = ((BaseFrame) obj).getEnclosedWindow();
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
            final BaseFrame frameElt = (BaseFrame) next;
            final WebWindow window = frameElt.getEnclosedWindow();
            if (name.equals(window.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Property \"" + name + "\" evaluated (by name) to " + window);
                }
                return getScriptableForElement(window);
            }
            else if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_47)
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
            final BaseFrame frameElt = (BaseFrame) next;
            final WebWindow window = frameElt.getEnclosedWindow();
            final String windowName = window.getName();
            if (windowName != null) {
                idList.add(windowName);
            }
        }
    }
}

