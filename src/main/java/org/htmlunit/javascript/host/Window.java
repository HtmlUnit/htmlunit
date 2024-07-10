/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import static org.htmlunit.BrowserVersionFeatures.JS_WINDOW_SELECTION_NULL_IF_INVISIBLE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.AlertHandler;
import org.htmlunit.BrowserVersion;
import org.htmlunit.ConfirmHandler;
import org.htmlunit.ElementNotFoundException;
import org.htmlunit.Page;
import org.htmlunit.PrintHandler;
import org.htmlunit.PromptHandler;
import org.htmlunit.ScriptException;
import org.htmlunit.ScriptResult;
import org.htmlunit.SgmlPage;
import org.htmlunit.StatusHandler;
import org.htmlunit.StorageHolder.Type;
import org.htmlunit.TopLevelWindow;
import org.htmlunit.WebAssert;
import org.htmlunit.WebClient;
import org.htmlunit.WebConsole;
import org.htmlunit.WebWindow;
import org.htmlunit.WebWindowNotFoundException;
import org.htmlunit.corejs.javascript.AccessorSlot;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.JavaScriptException;
import org.htmlunit.corejs.javascript.NativeConsole.Level;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.Slot;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.html.BaseFrameElement;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlAttributeChangeEvent;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlEmbed;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlMap;
import org.htmlunit.html.HtmlObject;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSelect;
import org.htmlunit.html.HtmlTextArea;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.crypto.Crypto;
import org.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import org.htmlunit.javascript.host.css.MediaQueryList;
import org.htmlunit.javascript.host.css.StyleMedia;
import org.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.javascript.host.dom.Selection;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.event.MessageEvent;
import org.htmlunit.javascript.host.event.MouseEvent;
import org.htmlunit.javascript.host.html.DocumentProxy;
import org.htmlunit.javascript.host.html.HTMLCollection;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.javascript.host.html.HTMLElement;
import org.htmlunit.javascript.host.performance.Performance;
import org.htmlunit.javascript.host.speech.SpeechSynthesis;
import org.htmlunit.javascript.host.xml.XMLDocument;
import org.htmlunit.util.UrlUtils;
import org.htmlunit.xml.XmlPage;

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
 * @author Carsten Steul
 * @author Colin Alworth
 * @author Atsushi Nakagawa
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535873.aspx">MSDN documentation</a>
 */
@JsxClass
public class Window extends EventTarget implements WindowOrWorkerGlobalScope, AutoCloseable {

    private static final Log LOG = LogFactory.getLog(Window.class);

    /** To be documented. */
    @JsxConstant({CHROME, EDGE})
    public static final int TEMPORARY = 0;

    /** To be documented. */
    @JsxConstant({CHROME, EDGE})
    public static final int PERSISTENT = 1;

    private static final Method getterLength;
    private static final Method setterLength;
    private static final Method getterSelf;
    private static final Method setterSelf;
    private static final Method getterParent;
    private static final Method setterParent;
    private static final Method getterFrames;
    private static final Method setterFrames;

    static {
        try {
            getterLength = Window.class.getDeclaredMethod("jsGetLength");
            setterLength = Window.class.getDeclaredMethod("jsSetLength", Scriptable.class);

            getterSelf = Window.class.getDeclaredMethod("jsGetSelf");
            setterSelf = Window.class.getDeclaredMethod("jsSetSelf", Scriptable.class);

            getterParent = Window.class.getDeclaredMethod("jsGetParent");
            setterParent = Window.class.getDeclaredMethod("jsSetParent", Scriptable.class);

            getterFrames = Window.class.getDeclaredMethod("jsGetFrames");
            setterFrames = Window.class.getDeclaredMethod("jsSetFrames", Scriptable.class);
        }
        catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private Scriptable lengthShadow_;
    private Scriptable selfShadow_;
    private Scriptable parentShadow_;
    private Scriptable framesShadow_;

    private Document document_;
    private DocumentProxy documentProxy_;
    private Navigator navigator_;
    private Object clientInformation_;
    private WebWindow webWindow_;
    private WindowProxy windowProxy_;
    private Screen screen_;
    private History history_;
    private Location location_;
    private Selection selection_;
    private Event currentEvent_;
    private String status_ = "";
    private Map<Class<? extends Scriptable>, Scriptable> prototypes_ = new HashMap<>();
    private Object controllers_;
    private Object opener_;
    private Object top_ = NOT_FOUND; // top can be set from JS to any value!
    private Crypto crypto_;

    private final EnumMap<Type, Storage> storages_ = new EnumMap<>(Type.class);

    private transient List<AnimationFrame> animationFrames_ = new ArrayList<>();

    private static final class AnimationFrame {
        private final long id_;
        private final Function callback_;

        AnimationFrame(final long id, final Function callback) {
            id_ = id;
            callback_ = callback;
        }
    }

    /**
     * Creates an instance.
     *
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        throw JavaScriptEngine.typeError("Illegal constructor");
    }

    /**
     * Restores the transient fields during deserialization.
     * @param stream the stream to read the object from
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if a class is not found
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        animationFrames_ = new ArrayList<>();
    }

    /**
     * Returns the prototype object corresponding to the specified HtmlUnit class inside the window scope.
     * @param jsClass the class whose prototype is to be returned
     * @return the prototype object corresponding to the specified class inside the specified scope
     */
    @Override
    public Scriptable getPrototype(final Class<? extends HtmlUnitScriptable> jsClass) {
        return prototypes_.get(jsClass);
    }

    /**
     * Sets the prototypes for HtmlUnit host classes.
     * @param map a Map of ({@link Class}, {@link Scriptable})
     */
    public void setPrototypes(final Map<Class<? extends Scriptable>, Scriptable> map) {
        prototypes_ = map;
    }

    /**
     * The JavaScript function {@code alert()}.
     * @param message the message
     */
    @JsxFunction
    public void alert(final Object message) {
        // use Object as parameter and perform String conversion by ourself
        // this allows to place breakpoint here and "see" the message object and its properties
        final String stringMessage = JavaScriptEngine.toString(message);
        final AlertHandler handler = getWebWindow().getWebClient().getAlertHandler();
        if (handler == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("window.alert(\"" + stringMessage + "\") no alert handler installed");
            }
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
    @Override
    public String btoa(final String stringToEncode) {
        return WindowOrWorkerGlobalScopeMixin.btoa(stringToEncode);
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding.
     * @param encodedData the encoded string
     * @return the decoded value
     */
    @JsxFunction
    @Override
    public String atob(final String encodedData) {
        return WindowOrWorkerGlobalScopeMixin.atob(encodedData);
    }

    /**
     * The JavaScript function {@code confirm}.
     * @param message the message
     * @return true if ok was pressed, false if cancel was pressed
     */
    @JsxFunction
    public boolean confirm(final String message) {
        final ConfirmHandler handler = getWebWindow().getWebClient().getConfirmHandler();
        if (handler == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("window.confirm(\""
                        + message + "\") no confirm handler installed, simulating the OK button");
            }
            return true;
        }
        return handler.handleConfirm(document_.getPage(), message);
    }

    /**
     * The JavaScript function {@code prompt}.
     * @param message the message
     * @param defaultValue the default value displayed in the text input field
     * @return the value typed in or {@code null} if the user pressed {@code cancel}
     */
    @JsxFunction
    public String prompt(final String message, Object defaultValue) {
        final PromptHandler handler = getWebWindow().getWebClient().getPromptHandler();
        if (handler == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("window.prompt(\"" + message + "\") no prompt handler installed");
            }
            return null;
        }
        if (JavaScriptEngine.isUndefined(defaultValue)) {
            defaultValue = null;
        }
        else {
            defaultValue = JavaScriptEngine.toString(defaultValue);
        }
        return handler.handlePrompt(document_.getPage(), message, (String) defaultValue);
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
     * Returns the current event.
     * @return the current event, or {@code null} if no event is currently available
     */
    @JsxGetter
    public Object getEvent() {
        if (currentEvent_ == null) {
            return JavaScriptEngine.Undefined;
        }
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
     * @see org.htmlunit.WebClientOptions#isPopupBlockerEnabled()
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536651.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public WindowProxy open(final Object url, final Object name, final Object features,
            final Object replace) {
        String urlString = null;
        if (!JavaScriptEngine.isUndefined(url)) {
            urlString = JavaScriptEngine.toString(url);
        }
        String windowName = "";
        if (!JavaScriptEngine.isUndefined(name)) {
            windowName = JavaScriptEngine.toString(name);
        }
        String featuresString = null;
        if (!JavaScriptEngine.isUndefined(features)) {
            featuresString = JavaScriptEngine.toString(features);
        }
        final WebClient webClient = getWebWindow().getWebClient();

        if (webClient.getOptions().isPopupBlockerEnabled()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignoring window.open() invocation because popups are blocked.");
            }
            return null;
        }

        boolean replaceCurrentEntryInBrowsingHistory = false;
        if (!JavaScriptEngine.isUndefined(replace)) {
            replaceCurrentEntryInBrowsingHistory = JavaScriptEngine.toBoolean(replace);
        }
        if ((featuresString != null || replaceCurrentEntryInBrowsingHistory) && LOG.isDebugEnabled()) {
            LOG.debug(
                   "window.open: features and replaceCurrentEntryInBrowsingHistory "
                    + "not implemented: url=[" + urlString
                    + "] windowName=[" + windowName
                    + "] features=[" + featuresString
                    + "] replaceCurrentEntry=[" + replaceCurrentEntryInBrowsingHistory
                    + "]");
        }

        // if specified name is the name of an existing window, then hold it
        if (StringUtils.isEmpty(urlString) && !"".equals(windowName)) {
            try {
                final WebWindow webWindow = webClient.getWebWindowByName(windowName);
                return getProxy(webWindow);
            }
            catch (final WebWindowNotFoundException ignored) {
                // nothing
            }
        }
        final URL newUrl = makeUrlForOpenWindow(urlString);
        final WebWindow newWebWindow = webClient.openWindow(newUrl, windowName, webWindow_);
        return getProxy(newWebWindow);
    }

    private URL makeUrlForOpenWindow(final String urlString) {
        if (urlString.isEmpty()) {
            return UrlUtils.URL_ABOUT_BLANK;
        }

        try {
            final Page page = getWebWindow().getEnclosedPage();
            if (page != null && page.isHtmlPage()) {
                return ((HtmlPage) page).getFullyQualifiedUrl(urlString);
            }
            return new URL(urlString);
        }
        catch (final MalformedURLException e) {
            if (LOG.isWarnEnabled()) {
                LOG.error("Unable to create URL for openWindow: relativeUrl=[" + urlString + "]", e);
            }
            return null;
        }
    }

    /**
     * Sets a chunk of JavaScript to be invoked at some specified time later.
     * The invocation occurs only if the window is opened after the delay
     * and does not contain an other page than the one that originated the setTimeout.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setTimeout">
     * MDN web docs</a>
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created timer
     */
    @JsxFunction
    public static Object setTimeout(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        return WindowOrWorkerGlobalScopeMixin.setTimeout(context, thisObj, args, function);
    }

    /**
     * Sets a chunk of JavaScript to be invoked each time a specified number of milliseconds has elapsed.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setInterval">
     * MDN web docs</a>
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created interval
     */
    @JsxFunction
    public static Object setInterval(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        return WindowOrWorkerGlobalScopeMixin.setInterval(context, thisObj, args, function);
    }

    /**
     * Cancels a time-out previously set with the
     * {@link #setTimeout(Context, Scriptable, Scriptable, Object[], Function)} method.
     *
     * @param timeoutId identifier for the timeout to clear
     *        as returned by {@link #setTimeout(Context, Scriptable, Scriptable, Object[], Function)}
     */
    @JsxFunction
    public void clearTimeout(final int timeoutId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearTimeout(" + timeoutId + ")");
        }
        getWebWindow().getJobManager().removeJob(timeoutId);
    }

    /**
     * Cancels the interval previously started using the
     * {@link #setInterval(Context, Scriptable, Scriptable, Object[], Function)} method.
     * Current implementation does nothing.
     * @param intervalID specifies the interval to cancel as returned by the
     *        {@link #setInterval(Context, Scriptable, Scriptable, Object[], Function)} method
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
    @JsxGetter
    public Object getClientInformation() {
        if (clientInformation_ != null) {
            return clientInformation_;
        }
        return navigator_;
    }

    /**
     * @param clientInformation the new value
     */
    @JsxSetter({CHROME, EDGE})
    public void setClientInformation(final Object clientInformation) {
        clientInformation_ = clientInformation;
    }

    /**
     * Returns the window property. This is a synonym for {@code self}.
     * @return the window property (a reference to <code>this</code>)
     */
    @JsxGetter(propertyName = "window")
    public Window getWindow_js() {
        return this;
    }

    /**
     * Returns the {@code localStorage} property.
     * @return the {@code localStorage} property
     */
    @JsxGetter
    public Storage getLocalStorage() {
        return getStorage(Type.LOCAL_STORAGE);
    }

    /**
     * Returns the {@code sessionStorage} property.
     * @return the {@code sessionStorage} property
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
        return storages_.computeIfAbsent(storageType,
            k -> {
                final WebWindow webWindow = getWebWindow();
                final Map<String, String> store = webWindow.getWebClient().getStorageHolder().
                        getStore(storageType, webWindow.getEnclosedPage());
                return new Storage(this, store);
            }
        );
    }

    /**
     * Returns the {@code location} property.
     * @return the {@code location} property
     */
    @JsxGetter
    public Location getLocation() {
        return location_;
    }

    /**
     * Sets the {@code location} property. This will cause a reload of the window.
     * @param newLocation the URL of the new content
     * @throws IOException when location loading fails
     */
    @JsxSetter
    public void setLocation(final String newLocation) throws IOException {
        location_.setHref(newLocation);
    }

    /**
     * Logs messages to the browser's standard output (stdout). If the browser was started
     * from a terminal, output sent to dump() will appear in the terminal.
     * Output from dump() is not sent to the browser's developer tools console.
     * To log to the developer tools console, use console.log().
     * <p>
     * HtmlUnit always uses the WebConsole.
     *
     * @param message the message to log
     */
    @JsxFunction({FF, FF_ESR})
    public void dump(final String message) {
        final WebConsole console = getWebWindow().getWebClient().getWebConsole();
        console.print(Context.getCurrentContext(), this, Level.INFO, new String[] {message}, null);
    }

    /**
     * Invokes all the animation callbacks registered for this window by
     * calling {@link #requestAnimationFrame(Object)} once.
     * @return the number of pending animation callbacks
     */
    public int animateAnimationsFrames() {
        final List<AnimationFrame> animationFrames = new ArrayList<>(animationFrames_);
        animationFrames_.clear();

        final double now = System.nanoTime() / 1_000_000d;
        final Object[] args = {now};

        final WebWindow ww = getWindow().getWebWindow();
        final JavaScriptEngine jsEngine = (JavaScriptEngine) ww.getWebClient().getJavaScriptEngine();

        for (final AnimationFrame animationFrame : animationFrames) {
            jsEngine.callFunction((HtmlPage) ww.getEnclosedPage(),
                        animationFrame.callback_, this, getParentScope(), args);
        }
        return animationFrames_.size();
    }

    /**
     * Add callback to the list of animationFrames.
     * @param callback the function to call when it's time to update the animation
     * @return an identification id
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window/requestAnimationFrame">MDN Doc</a>
     */
    @JsxFunction
    public int requestAnimationFrame(final Object callback) {
        if (callback instanceof Function) {
            final int id = animationFrames_.size();
            final AnimationFrame animationFrame = new AnimationFrame(id, (Function) callback);
            animationFrames_.add(animationFrame);
            return id;
        }
        return -1;
    }

    /**
     * Remove the callback from the list of animationFrames.
     * @param requestId the ID value returned by the call to window.requestAnimationFrame()
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Window/cancelAnimationFrame">MDN Doc</a>
     */
    @JsxFunction
    public void cancelAnimationFrame(final Object requestId) {
        final int id = (int) JavaScriptEngine.toNumber(requestId);

        animationFrames_.removeIf(animationFrame -> animationFrame.id_ == id);
    }

    /**
     * Returns the {@code screen} property.
     * @return the {@code screen} property
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
     * @param pageToEnclose the page that will become the enclosing page
     */
    public void initialize(final WebWindow webWindow, final Page pageToEnclose) {
        webWindow_ = webWindow;
        webWindow_.setScriptableObject(this);

        defineProperty("length", null, getterLength, setterLength, ScriptableObject.READONLY);
        defineProperty("self", null, getterSelf, setterSelf, ScriptableObject.READONLY);
        defineProperty("parent", null, getterParent, setterParent, ScriptableObject.READONLY);
        defineProperty("frames", null, getterFrames, setterFrames, ScriptableObject.READONLY);

        windowProxy_ = new WindowProxy(webWindow_);

        if (pageToEnclose instanceof XmlPage) {
            document_ = new XMLDocument();
        }
        else {
            document_ = new HTMLDocument();
        }
        document_.setParentScope(this);
        document_.setPrototype(getPrototype(document_.getClass()));
        document_.setWindow(this);

        if (pageToEnclose instanceof SgmlPage) {
            final SgmlPage page = (SgmlPage) pageToEnclose;
            document_.setDomNode(page);

            if (page.isHtmlPage()) {
                final HtmlPage htmlPage = (HtmlPage) page;

                htmlPage.addAutoCloseable(this);
            }
        }

        documentProxy_ = new DocumentProxy(webWindow_);

        navigator_ = new Navigator();
        navigator_.setParentScope(this);
        navigator_.setPrototype(getPrototype(navigator_.getClass()));

        screen_ = new Screen(getWebWindow().getScreen());
        screen_.setParentScope(this);
        screen_.setPrototype(getPrototype(screen_.getClass()));

        history_ = new History();
        history_.setParentScope(this);
        history_.setPrototype(getPrototype(history_.getClass()));

        location_ = new Location();
        location_.setParentScope(this);
        location_.setPrototype(getPrototype(location_.getClass()));
        location_.jsConstructor();
        location_.initialize(this, pageToEnclose);

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
            // variable to be the page. If this isn't set then HtmlUnitScriptable.get()
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
     * Returns the value of the {@code top} property.
     * @return the value of {@code top}
     */
    @JsxGetter
    public Object getTop() {
        if (top_ != NOT_FOUND) {
            return top_;
        }

        final WebWindow top = getWebWindow().getTopWindow();
        return top.getScriptableObject();
    }

    /**
     * Sets the value of the {@code top} property.
     * @param o the new value
     */
    @JsxSetter
    public void setTop(final Object o) {
        // ignore
    }

    /**
     * Returns the value of the {@code opener} property.
     * @return the value of the {@code opener}, or {@code null} for a top level window
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
     * Sets the {@code opener} property.
     * @param newValue the new value
     */
    @JsxSetter
    public void setOpener(final Object newValue) {
        opener_ = newValue;
    }

    /**
     * Returns the (i)frame in which the window is contained.
     * @return {@code null} for a top level window
     */
    @JsxGetter
    public HtmlUnitScriptable getFrameElement() {
        final WebWindow window = getWebWindow();
        if (window instanceof FrameWindow) {
            return ((FrameWindow) window).getFrameElement().getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the number of frames contained by this window.
     * @return the number of frames contained by this window
     */
    @JsxGetter
    public Object getLength() {
        return JavaScriptEngine.Undefined;
    }

    /**
     * Gets the {@code length} property. Setting this shadows the
     * defined value (see https://webidl.spec.whatwg.org/#Replaceable)
     * @return the shadow value if set otherwise the number of frames
     */
    public Object jsGetLength() {
        if (lengthShadow_ != null) {
            return lengthShadow_;
        }

        final HTMLCollection frames = getFrames();
        if (frames != null) {
            return frames.getLength();
        }
        return 0;
    }

    /**
     * Sets the {@code length} property. Setting this shadows the
     * defined value (see https://webidl.spec.whatwg.org/#Replaceable)
     * @param lengthShadow the value to overwrite the defined property value
     */
    public void jsSetLength(final Scriptable lengthShadow) {
        lengthShadow_ = lengthShadow;
    }

    /**
     * Returns the {@code self} property.
     * @return this
     */
    @JsxGetter
    public Object getSelf() {
        return JavaScriptEngine.Undefined;
    }

    /**
     * Gets the {@code self} property. Setting this shadows the
     * defined value (see https://webidl.spec.whatwg.org/#Replaceable)
     * @return the shadow value if set otherwise the number of frames
     */
    public Object jsGetSelf() {
        if (selfShadow_ != null) {
            return selfShadow_;
        }

        return this;
    }

    /**
     * Sets the {@code self} property. Setting this shadows the
     * defined value (see https://webidl.spec.whatwg.org/#Replaceable)
     * @param selfShadow the value to overwrite the defined property value
     */
    public void jsSetSelf(final Scriptable selfShadow) {
        selfShadow_ = selfShadow;
    }

    /**
     * Returns the value of the {@code parent} property.
     * @return the value of the {@code parent} property
     */
    @JsxGetter
    public Object getParent() {
        return JavaScriptEngine.Undefined;
    }

    /**
     * Gets the {@code parent} property. Setting this shadows the
     * defined value (see https://webidl.spec.whatwg.org/#Replaceable)
     * @return the shadow value if set otherwise the number of frames
     */
    public Object jsGetParent() {
        if (parentShadow_ != null) {
            return parentShadow_;
        }

        final WebWindow parent = getWebWindow().getParentWindow();
        return parent.getScriptableObject();
    }

    /**
     * Sets the {@code parent} property. Setting this shadows the
     * defined value (see https://webidl.spec.whatwg.org/#Replaceable)
     * @param parentShadow the value to overwrite the defined property value
     */
    public void jsSetParent(final Scriptable parentShadow) {
        parentShadow_ = parentShadow;
    }

    /**
     * Returns the value of the {@code frames} property.
     * @return the value of the {@code frames} property
     */
    @JsxGetter(propertyName = "frames")
    public Object getFrames_js() {
        return JavaScriptEngine.Undefined;
    }

    /**
     * Gets the {@code frames} property. Setting this shadows the
     * defined value (see https://webidl.spec.whatwg.org/#Replaceable)
     * @return the shadow value if set otherwise the number of frames
     */
    public Object jsGetFrames() {
        if (framesShadow_ != null) {
            return framesShadow_;
        }

        return this;
    }

    /**
     * Sets the {@code frames} property. Setting this shadows the
     * defined value (see https://webidl.spec.whatwg.org/#Replaceable)
     * @param framesShadow the value to overwrite the defined property value
     */
    public void jsSetFrames(final Scriptable framesShadow) {
        framesShadow_ = framesShadow;
    }

    /**
     * Returns the live collection of frames contained by this window.
     * @return the live collection of frames contained by this window
     */
    private HTMLCollection getFrames() {
        final Page page = getWebWindow().getEnclosedPage();
        if (page instanceof HtmlPage) {
            return new HTMLCollectionFrames((HtmlPage) page);
        }
        return null;
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
    @JsxFunction(functionName = "close")
    public void close_js() {
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
    public boolean isClosed() {
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
    public void scroll(final Scriptable x, final Scriptable y) {
        scrollTo(x, y);
    }

    /**
     * Scrolls the window content the specified distance.
     * @param x the horizontal distance to scroll by
     * @param y the vertical distance to scroll by
     */
    @JsxFunction
    public void scrollBy(final Scriptable x, final Scriptable y) {
        final HTMLElement body = document_.getBody();
        if (body != null) {
            int xOff = 0;
            int yOff = 0;
            if (y != null) {
                xOff = JavaScriptEngine.toInt32(x);
                yOff = JavaScriptEngine.toInt32(y);
            }
            else {
                if (!(x instanceof NativeObject)) {
                    throw JavaScriptEngine.typeError("eee");
                }
                if (x.has("left", x)) {
                    xOff = JavaScriptEngine.toInt32(x.get("left", x));
                }
                if (x.has("top", x)) {
                    yOff = JavaScriptEngine.toInt32(x.get("top", x));
                }
            }

            body.setScrollLeft(body.getScrollLeft() + xOff);
            body.setScrollTop(body.getScrollTop() + yOff);

            final Event event = new Event(body, Event.TYPE_SCROLL);
            body.fireEvent(event);
        }
    }

    /**
     * Scrolls the window content down by the specified number of lines.
     * @param lines the number of lines to scroll down
     */
    @JsxFunction({FF, FF_ESR})
    public void scrollByLines(final int lines) {
        final HTMLElement body = document_.getBody();
        if (body != null) {
            body.setScrollTop(body.getScrollTop() + (19 * lines));

            final Event event = new Event(body, Event.TYPE_SCROLL);
            body.fireEvent(event);
        }
    }

    /**
     * Scrolls the window content down by the specified number of pages.
     * @param pages the number of pages to scroll down
     */
    @JsxFunction({FF, FF_ESR})
    public void scrollByPages(final int pages) {
        final HTMLElement body = document_.getBody();
        if (body != null) {
            body.setScrollTop(body.getScrollTop() + (getInnerHeight() * pages));

            final Event event = new Event(body, Event.TYPE_SCROLL);
            body.fireEvent(event);
        }
    }

    /**
     * Scrolls to the specified location on the page.
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    @JsxFunction
    public void scrollTo(final Scriptable x, final Scriptable y) {
        final HTMLElement body = document_.getBody();
        if (body != null) {
            int xOff = 0;
            int yOff = 0;
            if (y != null) {
                xOff = JavaScriptEngine.toInt32(x);
                yOff = JavaScriptEngine.toInt32(y);
            }
            else {
                if (!(x instanceof NativeObject)) {
                    throw JavaScriptEngine.typeError("eee");
                }

                xOff = body.getScrollLeft();
                yOff = body.getScrollTop();

                if (x.has("left", x)) {
                    xOff = JavaScriptEngine.toInt32(x.get("left", x));
                }
                if (x.has("top", x)) {
                    yOff = JavaScriptEngine.toInt32(x.get("top", x));
                }
            }
            body.setScrollLeft(xOff);
            body.setScrollTop(yOff);

            final Event event = new Event(body, Event.TYPE_SCROLL);
            body.fireEvent(event);
        }
    }

    /**
     * Returns the {@code onload} property. Note that this is not necessarily a function if something else has been set.
     * @return the {@code onload} property
     */
    @JsxGetter
    public Object getOnload() {
        return getEventHandler(Event.TYPE_LOAD);
    }

    /**
     * Sets the value of the {@code onload} event handler.
     * @param onload the new handler
     */
    @JsxSetter
    public void setOnload(final Object onload) {
        setHandlerForJavaScript(Event.TYPE_LOAD, onload);
    }

    /**
     * Sets the value of the {@code onblur} event handler.
     * @param onblur the new handler
     */
    @JsxSetter
    public void setOnblur(final Object onblur) {
        setHandlerForJavaScript(Event.TYPE_BLUR, onblur);
    }

    /**
     * Returns the {@code onblur} property (not necessary a function if something else has been set).
     * @return the {@code onblur} property
     */
    @JsxGetter
    public Object getOnblur() {
        return getEventHandler(Event.TYPE_BLUR);
    }

    /**
     * Returns the {@code onclick} property (not necessary a function if something else has been set).
     * @return the {@code onclick} property
     */
    @JsxGetter
    public Object getOnclick() {
        return getEventHandler(MouseEvent.TYPE_CLICK);
    }

    /**
     * Sets the value of the {@code onclick} event handler.
     * @param onclick the new handler
     */
    @JsxSetter
    public void setOnclick(final Object onclick) {
        setHandlerForJavaScript(MouseEvent.TYPE_CLICK, onclick);
    }

    /**
     * Returns the {@code ondblclick} property (not necessary a function if something else has been set).
     * @return the {@code ondblclick} property
     */
    @JsxGetter
    public Object getOndblclick() {
        return getEventHandler(MouseEvent.TYPE_DBL_CLICK);
    }

    /**
     * Sets the value of the {@code ondblclick} event handler.
     * @param ondblclick the new handler
     */
    @JsxSetter
    public void setOndblclick(final Object ondblclick) {
        setHandlerForJavaScript(MouseEvent.TYPE_DBL_CLICK, ondblclick);
    }

    /**
     * Returns the {@code onhashchange} property (not necessary a function if something else has been set).
     * @return the {@code onhashchange} property
     */
    @JsxGetter
    public Object getOnhashchange() {
        return getEventHandler(Event.TYPE_HASH_CHANGE);
    }

    /**
     * Sets the value of the {@code onhashchange} event handler.
     * @param onhashchange the new handler
     */
    @JsxSetter
    public void setOnhashchange(final Object onhashchange) {
        setHandlerForJavaScript(Event.TYPE_HASH_CHANGE, onhashchange);
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
        return getEventHandler(Event.TYPE_BEFORE_UNLOAD);
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
        return getEventHandler(Event.TYPE_ERROR);
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
        return getEventHandler(Event.TYPE_MESSAGE);
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
     * Triggers the {@code onerror} handler, if one has been set.
     * @param e the error that needs to be reported
     */
    public void triggerOnError(final ScriptException e) {
        final Object o = getOnerror();
        if (o instanceof Function) {
            final Function f = (Function) o;
            String msg = e.getMessage();
            final String url = e.getPage().getUrl().toExternalForm();

            final int line = e.getFailingLineNumber();
            final int column = e.getFailingColumnNumber();

            Object jsError = e.getMessage();
            if (e.getCause() instanceof JavaScriptException) {
                msg = "uncaught exception: " + e.getCause().getMessage();
                jsError = ((JavaScriptException) e.getCause()).getValue();
            }
            else if (e.getCause() instanceof EcmaError) {
                msg = "uncaught " + e.getCause().getMessage();

                final EcmaError ecmaError = (EcmaError) e.getCause();
                final Scriptable err = Context.getCurrentContext().newObject(this, "Error");
                ScriptableObject.putProperty(err, "message", ecmaError.getMessage());
                ScriptableObject.putProperty(err, "fileName", ecmaError.sourceName());
                ScriptableObject.putProperty(err, "lineNumber", Integer.valueOf(ecmaError.lineNumber()));
                jsError = err;
            }

            final Object[] args = {msg, url, Integer.valueOf(line), Integer.valueOf(column), jsError};
            f.call(Context.getCurrentContext(), this, this, args);
        }
    }

    private void setHandlerForJavaScript(final String eventName, final Object handler) {
        getEventListenersContainer().setEventHandler(eventName, handler);
    }

    /**
     * To be called when the property detection fails in normal scenarios.
     *
     * @param name the name
     * @return the found object, or {@link Scriptable#NOT_FOUND}
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
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (index < 0 || getWebWindow() == null) {
            return JavaScriptEngine.Undefined;
        }

        final HTMLCollection frames = getFrames();
        if (frames == null || index >= frames.getLength()) {
            return JavaScriptEngine.Undefined;
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

        final Filter filter = new Filter(false);

        elements.removeIf(domElement -> !filter.matches(domElement));

        if (elements.isEmpty()) {
            return NOT_FOUND;
        }

        if (elements.size() == 1) {
            return getScriptableFor(elements.get(0));
        }

        // Null must be changed to '' for proper collection initialization.
        final String expElementName = "null".equals(name) ? "" : name;

        final HTMLCollection coll = new HTMLCollection(page, true);
        coll.setElementsSupplier(
                (Supplier<List<DomNode>> & Serializable)
                () -> {
                    final List<DomElement> expElements = page.getElementsByName(expElementName);
                    final List<DomNode> result = new ArrayList<>(expElements.size());

                    for (final DomElement domElement : expElements) {
                        if (filter.matches(domElement)) {
                            result.add(domElement);
                        }
                    }
                    return result;
                });

        coll.setEffectOnCacheFunction(
                (java.util.function.Function<HtmlAttributeChangeEvent, EffectOnCache> & Serializable)
                event -> {
                    if ("name".equals(event.getName())) {
                        return EffectOnCache.RESET;
                    }
                    return EffectOnCache.NONE;
                });

        return coll;
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
     * Returns the {@code innerWidth}.
     * @return the {@code innerWidth}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref28.html">Mozilla doc</a>
     */
    @JsxGetter
    public int getInnerWidth() {
        return getWebWindow().getInnerWidth();
    }

    /**
     * Sets the {@code innerWidth}.
     * @param width the {@code innerWidth}
     */
    @JsxSetter
    public void setInnerWidth(final int width) {
        getWebWindow().setInnerWidth(width);
    }

    /**
     * Returns the {@code outerWidth}.
     * @return the {@code outerWidth}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref79.html">Mozilla doc</a>
     */
    @JsxGetter
    public int getOuterWidth() {
        return getWebWindow().getOuterWidth();
    }

    /**
     * Sets the {@code outerWidth}.
     * @param width the {@code outerWidth}
     */
    @JsxSetter
    public void setOuterWidth(final int width) {
        getWebWindow().setOuterWidth(width);
    }

    /**
     * Returns the {@code innerHeight}.
     * @return the {@code innerHeight}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref27.html">Mozilla doc</a>
     */
    @JsxGetter
    public int getInnerHeight() {
        return getWebWindow().getInnerHeight();
    }

    /**
     * Sets the {@code innerHeight}.
     * @param height the {@code innerHeight}
     */
    @JsxSetter
    public void setInnerHeight(final int height) {
        getWebWindow().setInnerHeight(height);
    }

    /**
     * Returns the {@code outerHeight}.
     * @return the {@code outerHeight}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref78.html">Mozilla doc</a>
     */
    @JsxGetter
    public int getOuterHeight() {
        return getWebWindow().getOuterHeight();
    }

    /**
     * Sets the {@code outerHeight}.
     * @param height the {@code outerHeight}
     */
    @JsxSetter
    public void setOuterHeight(final int height) {
        getWebWindow().setOuterHeight(height);
    }

    /**
     * Prints the current page. The current implementation uses the {@link PrintHandler}
     * defined for the {@link WebClient} to process the window.
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref85.html">
     * Mozilla documentation</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536672.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public void print() {
        final PrintHandler handler = getWebWindow().getWebClient().getPrintHandler();
        if (handler == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("No PrintHandler installed - window.print() ignored");
            }
            return;
        }

        final SgmlPage sgmlPage = getDocument().getPage();
        if (!(sgmlPage instanceof HtmlPage)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Page is not an HtmlPage - window.print() ignored");
            }
            return;
        }

        Event event = new Event(this, Event.TYPE_BEFOREPRINT);
        fireEvent(event);

        final HtmlPage page = (HtmlPage) sgmlPage;
        page.setPrinting(true);
        try {
            handler.handlePrint(page);
        }
        finally {
            page.setPrinting(false);
        }
        event = new Event(this, Event.TYPE_AFTERPRINT);
        fireEvent(event);
    }

    /**
     * Does nothing special anymore.
     * @param type the type of events to capture
     * @see HTMLDocument#captureEvents(String)
     */
    @JsxFunction
    public void captureEvents(final String type) {
        // Empty.
    }

    /**
     * Does nothing special anymore.
     * @param type the type of events to capture
     * @see HTMLDocument#releaseEvents(String)
     */
    @JsxFunction
    public void releaseEvents(final String type) {
        // Empty.
    }

    /**
     * Returns computed style of the element. Computed style represents the final computed values
     * of all CSS properties for the element. This method's return value is of the same type as
     * that of <code>element.style</code>, but the value returned by this method is read-only.
     *
     * @param element the element
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null});
     * e.g. ':before'
     * @return the computed style
     */
    @JsxFunction
    public ComputedCSSStyleDeclaration getComputedStyle(final Object element, final String pseudoElement) {
        if (!(element instanceof Element)) {
            throw JavaScriptEngine.typeError("parameter 1 is not of type 'Element'");
        }
        final Element e = (Element) element;

        final ComputedCssStyleDeclaration style = getWebWindow().getComputedStyle(e.getDomNodeOrDie(), pseudoElement);
        return new ComputedCSSStyleDeclaration(e, style);
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
     * Gets the {@code controllers}. The result doesn't currently matter but it is important to return an
     * object as some JavaScript libraries check it.
     * @see <a href="https://developer.mozilla.org/En/DOM/Window.controllers">Mozilla documentation</a>
     * @return some object
     */
    @JsxGetter({FF, FF_ESR})
    public Object getControllers() {
        return controllers_;
    }

    /**
     * Sets the {@code controllers}.
     * @param value the new value
     */
    @JsxSetter({FF, FF_ESR})
    public void setControllers(final Object value) {
        controllers_ = value;
    }

    /**
     * Returns the value of {@code mozInnerScreenX} property.
     * @return the value of {@code mozInnerScreenX} property
     */
    @JsxGetter({FF, FF_ESR})
    public int getMozInnerScreenX() {
        return 10;
    }

    /**
     * Returns the value of {@code mozInnerScreenY} property.
     * @return the value of {@code mozInnerScreenY} property
     */
    @JsxGetter({FF, FF_ESR})
    public int getMozInnerScreenY() {
        return 89;
    }

    private static final class Filter {
        private final boolean includeFormFields_;

        Filter(final boolean includeFormFields) {
            includeFormFields_ = includeFormFields;
        }

        boolean matches(final Object object) {
            if (object instanceof HtmlEmbed
                || object instanceof HtmlForm
                || object instanceof HtmlImage
                || object instanceof HtmlObject) {
                return true;
            }

            return includeFormFields_
                    && (object instanceof HtmlAnchor
                        || object instanceof HtmlButton
                        || object instanceof HtmlInput
                        || object instanceof HtmlMap
                        || object instanceof HtmlSelect
                        || object instanceof HtmlTextArea);
        }
    }

    /**
     * Should implement the stop() function on the window object.
     * (currently empty implementation)
     * @see <a href="https://developer.mozilla.org/en/DOM/window.stop">window.stop</a>
     */
    @JsxFunction
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
    @JsxGetter
    public int getScrollX() {
        return 0;
    }

    /**
     * Returns the value of {@code scrollY} property.
     * @return the value of {@code scrollY} property
     */
    @JsxGetter
    public int getScrollY() {
        return 0;
    }

    /**
     * Returns the value of {@code netscape} property.
     * @return the value of {@code netscape} property
     */
    @JsxGetter({FF, FF_ESR})
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
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchEvent(final Event event) {
        event.setTarget(this);
        final ScriptResult result = fireEvent(event);
        return !event.isAborted(result);
    }

    /**
     * Getter for the {@code onchange} event handler.
     * @return the handler
     */
    @JsxGetter
    public Object getOnchange() {
        return getEventHandler(Event.TYPE_CHANGE);
    }

    /**
     * Setter for the {@code onchange} event handler.
     * @param onchange the handler
     */
    @JsxSetter
    public void setOnchange(final Object onchange) {
        setHandlerForJavaScript(Event.TYPE_CHANGE, onchange);
    }

    /**
     * Getter for the {@code onsubmit} event handler.
     * @return the handler
     */
    @JsxGetter
    public Object getOnsubmit() {
        return getEventHandler(Event.TYPE_SUBMIT);
    }

    /**
     * Setter for the {@code onsubmit} event handler.
     * @param onsubmit the handler
     */
    @JsxSetter
    public void setOnsubmit(final Object onsubmit) {
        setHandlerForJavaScript(Event.TYPE_SUBMIT, onsubmit);
    }

    /**
     * Posts a message.
     * @param context the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the script(s) to import
     * @param funObj the JS function called
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window.postMessage">MDN documentation</a>
     */
    @JsxFunction
    public static void postMessage(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function funObj) {

        // support the structured clone algorithm
        if (args.length < 1) {
            throw JavaScriptEngine.typeError("message not provided");
        }
        final Object message = args[0];

        String targetOrigin = "*";
        if (args.length > 1) {
            targetOrigin = JavaScriptEngine.toString(args[1]);
        }

        Object transfer = JavaScriptEngine.Undefined;
        if (args.length > 2) {
            transfer = args[2];
        }

        final Window sender = (Window) scope;
        final Window receiver = (Window) thisObj;
        final URL receiverURL = receiver.getWebWindow().getEnclosedPage().getUrl();

        final WebWindow webWindow = sender.getWebWindow();
        final Page page = webWindow.getEnclosedPage();
        final URL senderURL = page.getUrl();

        if (!"*".equals(targetOrigin)) {
            final URL targetURL;
            if ("/".equals(targetOrigin)) {
                targetURL = senderURL;
            }
            else {
                try {
                    targetURL = new URL(targetOrigin);
                }
                catch (final Exception e) {
                    throw JavaScriptEngine.throwAsScriptRuntimeEx(
                            new Exception(
                                    "SyntaxError: Failed to execute 'postMessage' on 'Window': Invalid target origin '"
                                            + targetOrigin + "' was specified (reason: " + e.getMessage() + "."));
                }
            }

            if (getPort(targetURL) != getPort(receiverURL)) {
                return;
            }
            if (!targetURL.getHost().equals(receiverURL.getHost())) {
                return;
            }
            if (!targetURL.getProtocol().equals(receiverURL.getProtocol())) {
                return;
            }
        }

        String origin = "";
        try {
            final URL originUrl = UrlUtils.getUrlWithoutPathRefQuery(senderURL);
            origin = UrlUtils.removeRedundantPort(originUrl).toExternalForm();
        }
        catch (final MalformedURLException e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }

        final MessageEvent event = new MessageEvent();
        event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin, "", sender, transfer);
        event.setParentScope(scope);
        event.setPrototype(receiver.getPrototype(event.getClass()));

        final AbstractJavaScriptEngine<?> jsEngine = webWindow.getWebClient().getJavaScriptEngine();
        final PostponedAction action = new PostponedAction(page, "Window.postMessage") {
            @Override
            public void execute() {
                final HtmlUnitContextFactory cf = jsEngine.getContextFactory();
                cf.call(cx -> receiver.dispatchEvent(event));
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
    @JsxGetter({CHROME, EDGE})
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
    @JsxFunction
    public boolean find(final String search, final boolean caseSensitive,
            final boolean backwards, final boolean wrapAround,
            final boolean wholeWord, final boolean searchInFrames, final boolean showDialog) {
        return false;
    }

    /**
     * Returns the {@code speechSynthesis} property.
     * @return the {@code speechSynthesis} property
     */
    @JsxGetter({CHROME, EDGE})
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
    @JsxGetter({CHROME, EDGE})
    public Object getOffscreenBuffering() {
        return true;
    }

    /**
     * Returns the {@code crypto} property.
     * @return the {@code crypto} property
     */
    @JsxGetter
    public Crypto getCrypto() {
        if (crypto_ == null) {
            crypto_ = new Crypto(this);
        }
        return crypto_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        // nothing to do
    }

    /**
     * Does nothing.
     * @param parent the new parent scope
     */
    @Override
    public void setParentScope(final Scriptable parent) {
        // nothing as the window is the top level scope and its parent scope should stay null
    }

    /**
     * Returns the {@code onfocus} event handler.
     * @return the {@code onfocus} event handler
     */
    @JsxGetter
    public Function getOnfocus() {
        return getEventHandler(Event.TYPE_FOCUS);
    }

    /**
     * Sets the {@code onfocus} event handler.
     * @param onfocus the {@code onfocus} event handler
     */
    @JsxSetter
    public void setOnfocus(final Object onfocus) {
        setHandlerForJavaScript(Event.TYPE_FOCUS, onfocus);
    }

    /**
     * Returns the {@code ondragend} event handler.
     * @return the {@code ondragend} event handler
     */
    @JsxGetter
    public Function getOndragend() {
        return getEventHandler(Event.TYPE_DRAGEND);
    }

    /**
     * Sets the {@code ondragend} event handler.
     * @param ondragend the {@code ondragend} event handler
     */
    @JsxSetter
    public void setOndragend(final Object ondragend) {
        setHandlerForJavaScript(Event.TYPE_DRAGEND, ondragend);
    }

    /**
     * Returns the {@code oninvalid} event handler.
     * @return the {@code oninvalid} event handler
     */
    @JsxGetter
    public Function getOninvalid() {
        return getEventHandler(Event.TYPE_INVALID);
    }

    /**
     * Sets the {@code oninvalid} event handler.
     * @param oninvalid the {@code oninvalid} event handler
     */
    @JsxSetter
    public void setOninvalid(final Object oninvalid) {
        setHandlerForJavaScript(Event.TYPE_INVALID, oninvalid);
    }

    /**
     * Returns the {@code onpointerout} event handler.
     * @return the {@code onpointerout} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerout() {
        return getEventHandler(Event.TYPE_POINTEROUT);
    }

    /**
     * Sets the {@code onpointerout} event handler.
     * @param onpointerout the {@code onpointerout} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerout(final Object onpointerout) {
        setHandlerForJavaScript(Event.TYPE_POINTEROUT, onpointerout);
    }

    /**
     * Returns the {@code onratechange} event handler.
     * @return the {@code onratechange} event handler
     */
    @JsxGetter
    public Function getOnratechange() {
        return getEventHandler(Event.TYPE_RATECHANGE);
    }

    /**
     * Sets the {@code onratechange} event handler.
     * @param onratechange the {@code onratechange} event handler
     */
    @JsxSetter
    public void setOnratechange(final Object onratechange) {
        setHandlerForJavaScript(Event.TYPE_RATECHANGE, onratechange);
    }

    /**
     * Returns the {@code onanimationiteration} event handler.
     * @return the {@code onanimationiteration} event handler
     */
    @JsxGetter
    public Function getOnanimationiteration() {
        return getEventHandler(Event.TYPE_ANIMATIONITERATION);
    }

    /**
     * Sets the {@code onanimationiteration} event handler.
     * @param onanimationiteration the {@code onanimationiteration} event handler
     */
    @JsxSetter
    public void setOnanimationiteration(final Object onanimationiteration) {
        setHandlerForJavaScript(Event.TYPE_ANIMATIONITERATION, onanimationiteration);
    }

    /**
     * Returns the {@code oncanplaythrough} event handler.
     * @return the {@code oncanplaythrough} event handler
     */
    @JsxGetter
    public Function getOncanplaythrough() {
        return getEventHandler(Event.TYPE_CANPLAYTHROUGH);
    }

    /**
     * Sets the {@code oncanplaythrough} event handler.
     * @param oncanplaythrough the {@code oncanplaythrough} event handler
     */
    @JsxSetter
    public void setOncanplaythrough(final Object oncanplaythrough) {
        setHandlerForJavaScript(Event.TYPE_CANPLAYTHROUGH, oncanplaythrough);
    }

    /**
     * Returns the {@code oncancel} event handler.
     * @return the {@code oncancel} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOncancel() {
        return getEventHandler(Event.TYPE_CANCEL);
    }

    /**
     * Sets the {@code oncancel} event handler.
     * @param oncancel the {@code oncancel} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOncancel(final Object oncancel) {
        setHandlerForJavaScript(Event.TYPE_CANCEL, oncancel);
    }

    /**
     * Returns the {@code onpointerenter} event handler.
     * @return the {@code onpointerenter} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerenter() {
        return getEventHandler(Event.TYPE_POINTERENTER);
    }

    /**
     * Sets the {@code onpointerenter} event handler.
     * @param onpointerenter the {@code onpointerenter} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerenter(final Object onpointerenter) {
        setHandlerForJavaScript(Event.TYPE_POINTERENTER, onpointerenter);
    }

    /**
     * Returns the {@code onselect} event handler.
     * @return the {@code onselect} event handler
     */
    @JsxGetter
    public Function getOnselect() {
        return getEventHandler(Event.TYPE_SELECT);
    }

    /**
     * Sets the {@code onselect} event handler.
     * @param onselect the {@code onselect} event handler
     */
    @JsxSetter
    public void setOnselect(final Object onselect) {
        setHandlerForJavaScript(Event.TYPE_SELECT, onselect);
    }

    /**
     * Returns the {@code onauxclick} event handler.
     * @return the {@code onauxclick} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnauxclick() {
        return getEventHandler(Event.TYPE_AUXCLICK);
    }

    /**
     * Sets the {@code onauxclick} event handler.
     * @param onauxclick the {@code onauxclick} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnauxclick(final Object onauxclick) {
        setHandlerForJavaScript(Event.TYPE_AUXCLICK, onauxclick);
    }

    /**
     * Returns the {@code onscroll} event handler.
     * @return the {@code onscroll} event handler
     */
    @JsxGetter
    public Function getOnscroll() {
        return getEventHandler(Event.TYPE_SCROLL);
    }

    /**
     * Sets the {@code onscroll} event handler.
     * @param onscroll the {@code onscroll} event handler
     */
    @JsxSetter
    public void setOnscroll(final Object onscroll) {
        setHandlerForJavaScript(Event.TYPE_SCROLL, onscroll);
    }

    /**
     * Returns the {@code onkeydown} event handler.
     * @return the {@code onkeydown} event handler
     */
    @JsxGetter
    public Function getOnkeydown() {
        return getEventHandler(Event.TYPE_KEY_DOWN);
    }

    /**
     * Sets the {@code onkeydown} event handler.
     * @param onkeydown the {@code onkeydown} event handler
     */
    @JsxSetter
    public void setOnkeydown(final Object onkeydown) {
        setHandlerForJavaScript(Event.TYPE_KEY_DOWN, onkeydown);
    }

    /**
     * Returns the {@code onwebkitanimationstart} event handler.
     * @return the {@code onwebkitanimationstart} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitanimationstart() {
        return getEventHandler(Event.TYPE_WEBANIMATIONSTART);
    }

    /**
     * Sets the {@code onwebkitanimationstart} event handler.
     * @param onwebkitanimationstart the {@code onwebkitanimationstart} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkitanimationstart(final Object onwebkitanimationstart) {
        setHandlerForJavaScript(Event.TYPE_WEBANIMATIONSTART, onwebkitanimationstart);
    }

    /**
     * Returns the {@code onkeyup} event handler.
     * @return the {@code onkeyup} event handler
     */
    @JsxGetter
    public Function getOnkeyup() {
        return getEventHandler(Event.TYPE_KEY_UP);
    }

    /**
     * Sets the {@code onkeyup} event handler.
     * @param onkeyup the {@code onkeyup} event handler
     */
    @JsxSetter
    public void setOnkeyup(final Object onkeyup) {
        setHandlerForJavaScript(Event.TYPE_KEY_UP, onkeyup);
    }

    /**
     * Returns the {@code onreset} event handler.
     * @return the {@code onreset} event handler
     */
    @JsxGetter
    public Function getOnreset() {
        return getEventHandler(Event.TYPE_RESET);
    }

    /**
     * Sets the {@code onreset} event handler.
     * @param onreset the {@code onreset} event handler
     */
    @JsxSetter
    public void setOnreset(final Object onreset) {
        setHandlerForJavaScript(Event.TYPE_RESET, onreset);
    }

    /**
     * Returns the {@code onkeypress} event handler.
     * @return the {@code onkeypress} event handler
     */
    @JsxGetter
    public Function getOnkeypress() {
        return getEventHandler(Event.TYPE_KEY_PRESS);
    }

    /**
     * Sets the {@code onkeypress} event handler.
     * @param onkeypress the {@code onkeypress} event handler
     */
    @JsxSetter
    public void setOnkeypress(final Object onkeypress) {
        setHandlerForJavaScript(Event.TYPE_KEY_PRESS, onkeypress);
    }

    /**
     * Returns the {@code ondrag} event handler.
     * @return the {@code ondrag} event handler
     */
    @JsxGetter
    public Function getOndrag() {
        return getEventHandler(Event.TYPE_DRAG);
    }

    /**
     * Sets the {@code ondrag} event handler.
     * @param ondrag the {@code ondrag} event handler
     */
    @JsxSetter
    public void setOndrag(final Object ondrag) {
        setHandlerForJavaScript(Event.TYPE_DRAG, ondrag);
    }

    /**
     * Returns the {@code onseeked} event handler.
     * @return the {@code onseeked} event handler
     */
    @JsxGetter
    public Function getOnseeked() {
        return getEventHandler(Event.TYPE_SEEKED);
    }

    /**
     * Sets the {@code onseeked} event handler.
     * @param onseeked the {@code onseeked} event handler
     */
    @JsxSetter
    public void setOnseeked(final Object onseeked) {
        setHandlerForJavaScript(Event.TYPE_SEEKED, onseeked);
    }

    /**
     * Returns the {@code onoffline} event handler.
     * @return the {@code onoffline} event handler
     */
    @JsxGetter
    public Function getOnoffline() {
        return getEventHandler(Event.TYPE_OFFLINE);
    }

    /**
     * Sets the {@code onoffline} event handler.
     * @param onoffline the {@code onoffline} event handler
     */
    @JsxSetter
    public void setOnoffline(final Object onoffline) {
        setHandlerForJavaScript(Event.TYPE_OFFLINE, onoffline);
    }

    /**
     * Returns the {@code ondeviceorientation} event handler.
     * @return the {@code ondeviceorientation} event handler
     */
    @JsxGetter
    public Function getOndeviceorientation() {
        return getEventHandler(Event.TYPE_DEVICEORIENTATION);
    }

    /**
     * Sets the {@code ondeviceorientation} event handler.
     * @param ondeviceorientation the {@code ondeviceorientation} event handler
     */
    @JsxSetter
    public void setOndeviceorientation(final Object ondeviceorientation) {
        setHandlerForJavaScript(Event.TYPE_DEVICEORIENTATION, ondeviceorientation);
    }

    /**
     * Returns the {@code ontoggle} event handler.
     * @return the {@code ontoggle} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOntoggle() {
        return getEventHandler(Event.TYPE_TOGGLE);
    }

    /**
     * Sets the {@code ontoggle} event handler.
     * @param ontoggle the {@code ontoggle} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOntoggle(final Object ontoggle) {
        setHandlerForJavaScript(Event.TYPE_TOGGLE, ontoggle);
    }

    /**
     * Returns the {@code onplay} event handler.
     * @return the {@code onplay} event handler
     */
    @JsxGetter
    public Function getOnplay() {
        return getEventHandler(Event.TYPE_PLAY);
    }

    /**
     * Sets the {@code onplay} event handler.
     * @param onplay the {@code onplay} event handler
     */
    @JsxSetter
    public void setOnplay(final Object onplay) {
        setHandlerForJavaScript(Event.TYPE_PLAY, onplay);
    }

    /**
     * Returns the {@code oncontextmenu} event handler.
     * @return the {@code oncontextmenu} event handler
     */
    @JsxGetter
    public Function getOncontextmenu() {
        return getEventHandler(MouseEvent.TYPE_CONTEXT_MENU);
    }

    /**
     * Sets the {@code oncontextmenu} event handler.
     * @param oncontextmenu the {@code oncontextmenu} event handler
     */
    @JsxSetter
    public void setOncontextmenu(final Object oncontextmenu) {
        setHandlerForJavaScript(MouseEvent.TYPE_CONTEXT_MENU, oncontextmenu);
    }

    /**
     * Returns the {@code onmousemove} event handler.
     * @return the {@code onmousemove} event handler
     */
    @JsxGetter
    public Function getOnmousemove() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_MOVE);
    }

    /**
     * Sets the {@code onmousemove} event handler.
     * @param onmousemove the {@code onmousemove} event handler
     */
    @JsxSetter
    public void setOnmousemove(final Object onmousemove) {
        setHandlerForJavaScript(MouseEvent.TYPE_MOUSE_MOVE, onmousemove);
    }

    /**
     * Returns the {@code onpointermove} event handler.
     * @return the {@code onpointermove} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointermove() {
        return getEventHandler(Event.TYPE_POINTERMOVE);
    }

    /**
     * Sets the {@code onpointermove} event handler.
     * @param onpointermove the {@code onpointermove} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointermove(final Object onpointermove) {
        setHandlerForJavaScript(Event.TYPE_POINTERMOVE, onpointermove);
    }

    /**
     * Returns the {@code onmouseover} event handler.
     * @return the {@code onmouseover} event handler
     */
    @JsxGetter
    public Function getOnmouseover() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OVER);
    }

    /**
     * Sets the {@code onmouseover} event handler.
     * @param onmouseover the {@code onmouseover} event handler
     */
    @JsxSetter
    public void setOnmouseover(final Object onmouseover) {
        setHandlerForJavaScript(MouseEvent.TYPE_MOUSE_OVER, onmouseover);
    }

    /**
     * Returns the {@code onlostpointercapture} event handler.
     * @return the {@code onlostpointercapture} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnlostpointercapture() {
        return getEventHandler(Event.TYPE_LOSTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code onlostpointercapture} event handler.
     * @param onlostpointercapture the {@code onlostpointercapture} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnlostpointercapture(final Object onlostpointercapture) {
        setHandlerForJavaScript(Event.TYPE_LOSTPOINTERCAPTURE, onlostpointercapture);
    }

    /**
     * Returns the {@code onpointerover} event handler.
     * @return the {@code onpointerover} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerover() {
        return getEventHandler(Event.TYPE_POINTEROVER);
    }

    /**
     * Sets the {@code onpointerover} event handler.
     * @param onpointerover the {@code onpointerover} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerover(final Object onpointerover) {
        setHandlerForJavaScript(Event.TYPE_POINTEROVER, onpointerover);
    }

    /**
     * Returns the {@code onclose} event handler.
     * @return the {@code onclose} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnclose() {
        return getEventHandler(Event.TYPE_CLOSE);
    }

    /**
     * Sets the {@code onclose} event handler.
     * @param onclose the {@code onclose} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnclose(final Object onclose) {
        setHandlerForJavaScript(Event.TYPE_CLOSE, onclose);
    }

    /**
     * Returns the {@code onanimationend} event handler.
     * @return the {@code onanimationend} event handler
     */
    @JsxGetter
    public Function getOnanimationend() {
        return getEventHandler(Event.TYPE_ANIMATIONEND);
    }

    /**
     * Sets the {@code onanimationend} event handler.
     * @param onanimationend the {@code onanimationend} event handler
     */
    @JsxSetter
    public void setOnanimationend(final Object onanimationend) {
        setHandlerForJavaScript(Event.TYPE_ANIMATIONEND, onanimationend);
    }

    /**
     * Returns the {@code ondragenter} event handler.
     * @return the {@code ondragenter} event handler
     */
    @JsxGetter
    public Function getOndragenter() {
        return getEventHandler(Event.TYPE_DRAGENTER);
    }

    /**
     * Sets the {@code ondragenter} event handler.
     * @param ondragenter the {@code ondragenter} event handler
     */
    @JsxSetter
    public void setOndragenter(final Object ondragenter) {
        setHandlerForJavaScript(Event.TYPE_DRAGENTER, ondragenter);
    }

    /**
     * Returns the {@code onafterprint} event handler.
     * @return the {@code onafterprint} event handler
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnafterprint() {
        return getEventHandler(Event.TYPE_AFTERPRINT);
    }

    /**
     * Sets the {@code onafterprint} event handler.
     * @param onafterprint the {@code onafterprint} event handler
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnafterprint(final Object onafterprint) {
        setHandlerForJavaScript(Event.TYPE_AFTERPRINT, onafterprint);
    }

    /**
     * Returns the {@code onmozfullscreenerror} event handler.
     * @return the {@code onmozfullscreenerror} event handler
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnmozfullscreenerror() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENERROR);
    }

    /**
     * Sets the {@code onmozfullscreenerror} event handler.
     * @param onmozfullscreenerror the {@code onmozfullscreenerror} event handler
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnmozfullscreenerror(final Object onmozfullscreenerror) {
        setHandlerForJavaScript(Event.TYPE_MOZFULLSCREENERROR, onmozfullscreenerror);
    }

    /**
     * Returns the {@code onmouseleave} event handler.
     * @return the {@code onmouseleave} event handler
     */
    @JsxGetter
    public Function getOnmouseleave() {
        return getEventHandler(Event.TYPE_MOUSELEAVE);
    }

    /**
     * Sets the {@code onmouseleave} event handler.
     * @param onmouseleave the {@code onmouseleave} event handler
     */
    @JsxSetter
    public void setOnmouseleave(final Object onmouseleave) {
        setHandlerForJavaScript(Event.TYPE_MOUSELEAVE, onmouseleave);
    }

    /**
     * Returns the {@code onmousewheel} event handler.
     * @return the {@code onmousewheel} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnmousewheel() {
        return getEventHandler(Event.TYPE_MOUSEWHEEL);
    }

    /**
     * Sets the {@code onmousewheel} event handler.
     * @param onmousewheel the {@code onmousewheel} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnmousewheel(final Object onmousewheel) {
        setHandlerForJavaScript(Event.TYPE_MOUSEWHEEL, onmousewheel);
    }

    /**
     * Returns the {@code onseeking} event handler.
     * @return the {@code onseeking} event handler
     */
    @JsxGetter
    public Function getOnseeking() {
        return getEventHandler(Event.TYPE_SEEKING);
    }

    /**
     * Sets the {@code onseeking} event handler.
     * @param onseeking the {@code onseeking} event handler
     */
    @JsxSetter
    public void setOnseeking(final Object onseeking) {
        setHandlerForJavaScript(Event.TYPE_SEEKING, onseeking);
    }

    /**
     * Returns the {@code oncuechange} event handler.
     * @return the {@code oncuechange} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOncuechange() {
        return getEventHandler(Event.TYPE_CUECHANGE);
    }

    /**
     * Sets the {@code oncuechange} event handler.
     * @param oncuechange the {@code oncuechange} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOncuechange(final Object oncuechange) {
        setHandlerForJavaScript(Event.TYPE_CUECHANGE, oncuechange);
    }

    /**
     * Returns the {@code onpageshow} event handler.
     * @return the {@code onpageshow} event handler
     */
    @JsxGetter
    public Function getOnpageshow() {
        return getEventHandler(Event.TYPE_PAGESHOW);
    }

    /**
     * Sets the {@code onpageshow} event handler.
     * @param onpageshow the {@code onpageshow} event handler
     */
    @JsxSetter
    public void setOnpageshow(final Object onpageshow) {
        setHandlerForJavaScript(Event.TYPE_PAGESHOW, onpageshow);
    }

    /**
     * Returns the {@code onmozfullscreenchange} event handler.
     * @return the {@code onmozfullscreenchange} event handler
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnmozfullscreenchange() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onmozfullscreenchange} event handler.
     * @param onmozfullscreenchange the {@code onmozfullscreenchange} event handler
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnmozfullscreenchange(final Object onmozfullscreenchange) {
        setHandlerForJavaScript(Event.TYPE_MOZFULLSCREENCHANGE, onmozfullscreenchange);
    }

    /**
     * Returns the {@code ondurationchange} event handler.
     * @return the {@code ondurationchange} event handler
     */
    @JsxGetter
    public Function getOndurationchange() {
        return getEventHandler(Event.TYPE_DURATIONCHANGE);
    }

    /**
     * Sets the {@code ondurationchange} event handler.
     * @param ondurationchange the {@code ondurationchange} event handler
     */
    @JsxSetter
    public void setOndurationchange(final Object ondurationchange) {
        setHandlerForJavaScript(Event.TYPE_DURATIONCHANGE, ondurationchange);
    }

    /**
     * Returns the {@code onplaying} event handler.
     * @return the {@code onplaying} event handler
     */
    @JsxGetter
    public Function getOnplaying() {
        return getEventHandler(Event.TYPE_PLAYING);
    }

    /**
     * Sets the {@code onplaying} event handler.
     * @param onplaying the {@code onplaying} event handler
     */
    @JsxSetter
    public void setOnplaying(final Object onplaying) {
        setHandlerForJavaScript(Event.TYPE_PLAYING, onplaying);
    }

    /**
     * Returns the {@code onended} event handler.
     * @return the {@code onended} event handler
     */
    @JsxGetter
    public Function getOnended() {
        return getEventHandler(Event.TYPE_ENDED);
    }

    /**
     * Sets the {@code onended} event handler.
     * @param onended the {@code onended} event handler
     */
    @JsxSetter
    public void setOnended(final Object onended) {
        setHandlerForJavaScript(Event.TYPE_ENDED, onended);
    }

    /**
     * Returns the {@code onloadeddata} event handler.
     * @return the {@code onloadeddata} event handler
     */
    @JsxGetter
    public Function getOnloadeddata() {
        return getEventHandler(Event.TYPE_LOADEDDATA);
    }

    /**
     * Sets the {@code onloadeddata} event handler.
     * @param onloadeddata the {@code onloadeddata} event handler
     */
    @JsxSetter
    public void setOnloadeddata(final Object onloadeddata) {
        setHandlerForJavaScript(Event.TYPE_LOADEDDATA, onloadeddata);
    }

    /**
     * Returns the {@code onunhandledrejection} event handler.
     * @return the {@code onunhandledrejection} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnunhandledrejection() {
        return getEventHandler(Event.TYPE_UNHANDLEDREJECTION);
    }

    /**
     * Sets the {@code onunhandledrejection} event handler.
     * @param onunhandledrejection the {@code onunhandledrejection} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnunhandledrejection(final Object onunhandledrejection) {
        setHandlerForJavaScript(Event.TYPE_UNHANDLEDREJECTION, onunhandledrejection);
    }

    /**
     * Returns the {@code onmouseout} event handler.
     * @return the {@code onmouseout} event handler
     */
    @JsxGetter
    public Function getOnmouseout() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OUT);
    }

    /**
     * Sets the {@code onmouseout} event handler.
     * @param onmouseout the {@code onmouseout} event handler
     */
    @JsxSetter
    public void setOnmouseout(final Object onmouseout) {
        setHandlerForJavaScript(MouseEvent.TYPE_MOUSE_OUT, onmouseout);
    }

    /**
     * Returns the {@code onsuspend} event handler.
     * @return the {@code onsuspend} event handler
     */
    @JsxGetter
    public Function getOnsuspend() {
        return getEventHandler(Event.TYPE_SUSPEND);
    }

    /**
     * Sets the {@code onsuspend} event handler.
     * @param onsuspend the {@code onsuspend} event handler
     */
    @JsxSetter
    public void setOnsuspend(final Object onsuspend) {
        setHandlerForJavaScript(Event.TYPE_SUSPEND, onsuspend);
    }

    /**
     * Returns the {@code onwaiting} event handler.
     * @return the {@code onwaiting} event handler
     */
    @JsxGetter
    public Function getOnwaiting() {
        return getEventHandler(Event.TYPE_WAITING);
    }

    /**
     * Sets the {@code onwaiting} event handler.
     * @param onwaiting the {@code onwaiting} event handler
     */
    @JsxSetter
    public void setOnwaiting(final Object onwaiting) {
        setHandlerForJavaScript(Event.TYPE_WAITING, onwaiting);
    }

    /**
     * Returns the {@code oncanplay} event handler.
     * @return the {@code oncanplay} event handler
     */
    @JsxGetter
    public Function getOncanplay() {
        return getEventHandler(Event.TYPE_CANPLAY);
    }

    /**
     * Sets the {@code oncanplay} event handler.
     * @param oncanplay the {@code oncanplay} event handler
     */
    @JsxSetter
    public void setOncanplay(final Object oncanplay) {
        setHandlerForJavaScript(Event.TYPE_CANPLAY, oncanplay);
    }

    /**
     * Returns the {@code onmousedown} event handler.
     * @return the {@code onmousedown} event handler
     */
    @JsxGetter
    public Function getOnmousedown() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_DOWN);
    }

    /**
     * Sets the {@code onmousedown} event handler.
     * @param onmousedown the {@code onmousedown} event handler
     */
    @JsxSetter
    public void setOnmousedown(final Object onmousedown) {
        setHandlerForJavaScript(MouseEvent.TYPE_MOUSE_DOWN, onmousedown);
    }

    /**
     * Returns the {@code onlanguagechange} event handler.
     * @return the {@code onlanguagechange} event handler
     */
    @JsxGetter
    public Function getOnlanguagechange() {
        return getEventHandler(Event.TYPE_LANGUAGECHANGE);
    }

    /**
     * Sets the {@code onlanguagechange} event handler.
     * @param onlanguagechange the {@code onlanguagechange} event handler
     */
    @JsxSetter
    public void setOnlanguagechange(final Object onlanguagechange) {
        setHandlerForJavaScript(Event.TYPE_LANGUAGECHANGE, onlanguagechange);
    }

    /**
     * Returns the {@code onemptied} event handler.
     * @return the {@code onemptied} event handler
     */
    @JsxGetter
    public Function getOnemptied() {
        return getEventHandler(Event.TYPE_EMPTIED);
    }

    /**
     * Sets the {@code onemptied} event handler.
     * @param onemptied the {@code onemptied} event handler
     */
    @JsxSetter
    public void setOnemptied(final Object onemptied) {
        setHandlerForJavaScript(Event.TYPE_EMPTIED, onemptied);
    }

    /**
     * Returns the {@code onrejectionhandled} event handler.
     * @return the {@code onrejectionhandled} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnrejectionhandled() {
        return getEventHandler(Event.TYPE_REJECTIONHANDLED);
    }

    /**
     * Sets the {@code onrejectionhandled} event handler.
     * @param onrejectionhandled the {@code onrejectionhandled} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnrejectionhandled(final Object onrejectionhandled) {
        setHandlerForJavaScript(Event.TYPE_REJECTIONHANDLED, onrejectionhandled);
    }

    /**
     * Returns the {@code onpointercancel} event handler.
     * @return the {@code onpointercancel} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointercancel() {
        return getEventHandler(Event.TYPE_POINTERCANCEL);
    }

    /**
     * Sets the {@code onpointercancel} event handler.
     * @param onpointercancel the {@code onpointercancel} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointercancel(final Object onpointercancel) {
        setHandlerForJavaScript(Event.TYPE_POINTERCANCEL, onpointercancel);
    }

    /**
     * Returns the {@code onresize} event handler.
     * @return the {@code onresize} event handler
     */
    @JsxGetter
    public Function getOnresize() {
        return getEventHandler(Event.TYPE_RESIZE);
    }

    /**
     * Sets the {@code onresize} event handler.
     * @param onresize the {@code onresize} event handler
     */
    @JsxSetter
    public void setOnresize(final Object onresize) {
        setHandlerForJavaScript(Event.TYPE_RESIZE, onresize);
    }

    /**
     * Returns the {@code onpause} event handler.
     * @return the {@code onpause} event handler
     */
    @JsxGetter
    public Function getOnpause() {
        return getEventHandler(Event.TYPE_PAUSE);
    }

    /**
     * Sets the {@code onpause} event handler.
     * @param onpause the {@code onpause} event handler
     */
    @JsxSetter
    public void setOnpause(final Object onpause) {
        setHandlerForJavaScript(Event.TYPE_PAUSE, onpause);
    }

    /**
     * Returns the {@code onloadstart} event handler.
     * @return the {@code onloadstart} event handler
     */
    @JsxGetter
    public Function getOnloadstart() {
        return getEventHandler(Event.TYPE_LOAD_START);
    }

    /**
     * Sets the {@code onloadstart} event handler.
     * @param onloadstart the {@code onloadstart} event handler
     */
    @JsxSetter
    public void setOnloadstart(final Object onloadstart) {
        setHandlerForJavaScript(Event.TYPE_LOAD_START, onloadstart);
    }

    /**
     * Returns the {@code onprogress} event handler.
     * @return the {@code onprogress} event handler
     */
    @JsxGetter
    public Function getOnprogress() {
        return getEventHandler(Event.TYPE_PROGRESS);
    }

    /**
     * Sets the {@code onprogress} event handler.
     * @param onprogress the {@code onprogress} event handler
     */
    @JsxSetter
    public void setOnprogress(final Object onprogress) {
        setHandlerForJavaScript(Event.TYPE_PROGRESS, onprogress);
    }

    /**
     * Returns the {@code onpointerup} event handler.
     * @return the {@code onpointerup} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerup() {
        return getEventHandler(Event.TYPE_POINTERUP);
    }

    /**
     * Sets the {@code onpointerup} event handler.
     * @param onpointerup the {@code onpointerup} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerup(final Object onpointerup) {
        setHandlerForJavaScript(Event.TYPE_POINTERUP, onpointerup);
    }

    /**
     * Returns the {@code onwheel} event handler.
     * @return the {@code onwheel} event handler
     */
    @JsxGetter
    public Function getOnwheel() {
        return getEventHandler(Event.TYPE_WHEEL);
    }

    /**
     * Sets the {@code onwheel} event handler.
     * @param onwheel the {@code onwheel} event handler
     */
    @JsxSetter
    public void setOnwheel(final Object onwheel) {
        setHandlerForJavaScript(Event.TYPE_WHEEL, onwheel);
    }

    /**
     * Returns the {@code onpointerleave} event handler.
     * @return the {@code onpointerleave} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerleave() {
        return getEventHandler(Event.TYPE_POINTERLEAVE);
    }

    /**
     * Sets the {@code onpointerleave} event handler.
     * @param onpointerleave the {@code onpointerleave} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerleave(final Object onpointerleave) {
        setHandlerForJavaScript(Event.TYPE_POINTERLEAVE, onpointerleave);
    }

    /**
     * Returns the {@code onbeforeprint} event handler.
     * @return the {@code onbeforeprint} event handler
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnbeforeprint() {
        return getEventHandler(Event.TYPE_BEFOREPRINT);
    }

    /**
     * Sets the {@code onbeforeprint} event handler.
     * @param onbeforeprint the {@code onbeforeprint} event handler
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnbeforeprint(final Object onbeforeprint) {
        setHandlerForJavaScript(Event.TYPE_BEFOREPRINT, onbeforeprint);
    }

    /**
     * Returns the {@code onstorage} event handler.
     * @return the {@code onstorage} event handler
     */
    @JsxGetter
    public Function getOnstorage() {
        return getEventHandler(Event.TYPE_STORAGE);
    }

    /**
     * Sets the {@code onstorage} event handler.
     * @param onstorage the {@code onstorage} event handler
     */
    @JsxSetter
    public void setOnstorage(final Object onstorage) {
        setHandlerForJavaScript(Event.TYPE_STORAGE, onstorage);
    }

    /**
     * Returns the {@code onanimationstart} event handler.
     * @return the {@code onanimationstart} event handler
     */
    @JsxGetter
    public Function getOnanimationstart() {
        return getEventHandler(Event.TYPE_ANIMATIONSTART);
    }

    /**
     * Sets the {@code onanimationstart} event handler.
     * @param onanimationstart the {@code onanimationstart} event handler
     */
    @JsxSetter
    public void setOnanimationstart(final Object onanimationstart) {
        setHandlerForJavaScript(Event.TYPE_ANIMATIONSTART, onanimationstart);
    }

    /**
     * Returns the {@code ontimeupdate} event handler.
     * @return the {@code ontimeupdate} event handler
     */
    @JsxGetter
    public Function getOntimeupdate() {
        return getEventHandler(Event.TYPE_TIMEUPDATE);
    }

    /**
     * Sets the {@code ontimeupdate} event handler.
     * @param ontimeupdate the {@code ontimeupdate} event handler
     */
    @JsxSetter
    public void setOntimeupdate(final Object ontimeupdate) {
        setHandlerForJavaScript(Event.TYPE_TIMEUPDATE, ontimeupdate);
    }

    /**
     * Returns the {@code onpagehide} event handler.
     * @return the {@code onpagehide} event handler
     */
    @JsxGetter
    public Function getOnpagehide() {
        return getEventHandler(Event.TYPE_PAGEHIDE);
    }

    /**
     * Sets the {@code onpagehide} event handler.
     * @param onpagehide the {@code onpagehide} event handler
     */
    @JsxSetter
    public void setOnpagehide(final Object onpagehide) {
        setHandlerForJavaScript(Event.TYPE_PAGEHIDE, onpagehide);
    }

    /**
     * Returns the {@code onwebkitanimationiteration} event handler.
     * @return the {@code onwebkitanimationiteration} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitanimationiteration() {
        return getEventHandler(Event.TYPE_WEBKITANIMATIONITERATION);
    }

    /**
     * Sets the {@code onwebkitanimationiteration} event handler.
     * @param onwebkitanimationiteration the {@code onwebkitanimationiteration} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkitanimationiteration(final Object onwebkitanimationiteration) {
        setHandlerForJavaScript(Event.TYPE_WEBKITANIMATIONITERATION, onwebkitanimationiteration);
    }

    /**
     * Returns the {@code onabort} event handler.
     * @return the {@code onabort} event handler
     */
    @JsxGetter
    public Function getOnabort() {
        return getEventHandler(Event.TYPE_ABORT);
    }

    /**
     * Sets the {@code onabort} event handler.
     * @param onabort the {@code onabort} event handler
     */
    @JsxSetter
    public void setOnabort(final Object onabort) {
        setHandlerForJavaScript(Event.TYPE_ABORT, onabort);
    }

    /**
     * Returns the {@code onloadedmetadata} event handler.
     * @return the {@code onloadedmetadata} event handler
     */
    @JsxGetter
    public Function getOnloadedmetadata() {
        return getEventHandler(Event.TYPE_LOADEDMETADATA);
    }

    /**
     * Sets the {@code onloadedmetadata} event handler.
     * @param onloadedmetadata the {@code onloadedmetadata} event handler
     */
    @JsxSetter
    public void setOnloadedmetadata(final Object onloadedmetadata) {
        setHandlerForJavaScript(Event.TYPE_LOADEDMETADATA, onloadedmetadata);
    }

    /**
     * Returns the {@code onmouseup} event handler.
     * @return the {@code onmouseup} event handler
     */
    @JsxGetter
    public Function getOnmouseup() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_UP);
    }

    /**
     * Sets the {@code onmouseup} event handler.
     * @param onmouseup the {@code onmouseup} event handler
     */
    @JsxSetter
    public void setOnmouseup(final Object onmouseup) {
        setHandlerForJavaScript(MouseEvent.TYPE_MOUSE_UP, onmouseup);
    }

    /**
     * Returns the {@code ondragover} event handler.
     * @return the {@code ondragover} event handler
     */
    @JsxGetter
    public Function getOndragover() {
        return getEventHandler(Event.TYPE_DRAGOVER);
    }

    /**
     * Sets the {@code ondragover} event handler.
     * @param ondragover the {@code ondragover} event handler
     */
    @JsxSetter
    public void setOndragover(final Object ondragover) {
        setHandlerForJavaScript(Event.TYPE_DRAGOVER, ondragover);
    }

    /**
     * Returns the {@code ononline} event handler.
     * @return the {@code ononline} event handler
     */
    @JsxGetter
    public Function getOnonline() {
        return getEventHandler(Event.TYPE_ONLINE);
    }

    /**
     * Sets the {@code ononline} event handler.
     * @param ononline the {@code ononline} event handler
     */
    @JsxSetter
    public void setOnonline(final Object ononline) {
        setHandlerForJavaScript(Event.TYPE_ONLINE, ononline);
    }

    /**
     * Returns the {@code onsearch} event handler.
     * @return the {@code onsearch} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnsearch() {
        return getEventHandler(Event.TYPE_SEARCH);
    }

    /**
     * Sets the {@code onsearch} event handler.
     * @param onsearch the {@code onsearch} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnsearch(final Object onsearch) {
        setHandlerForJavaScript(Event.TYPE_SEARCH, onsearch);
    }

    /**
     * Returns the {@code oninput} event handler.
     * @return the {@code oninput} event handler
     */
    @JsxGetter
    public Function getOninput() {
        return getEventHandler(Event.TYPE_INPUT);
    }

    /**
     * Sets the {@code oninput} event handler.
     * @param oninput the {@code oninput} event handler
     */
    @JsxSetter
    public void setOninput(final Object oninput) {
        setHandlerForJavaScript(Event.TYPE_INPUT, oninput);
    }

    /**
     * Returns the {@code onwebkittransitionend} event handler.
     * @return the {@code onwebkittransitionend} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkittransitionend() {
        return getEventHandler(Event.TYPE_WEBKITTRANSITIONEND);
    }

    /**
     * Sets the {@code onwebkittransitionend} event handler.
     * @param onwebkittransitionend the {@code onwebkittransitionend} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkittransitionend(final Object onwebkittransitionend) {
        setHandlerForJavaScript(Event.TYPE_WEBKITTRANSITIONEND, onwebkittransitionend);
    }

    /**
     * Returns the {@code ondevicemotion} event handler.
     * @return the {@code ondevicemotion} event handler
     */
    @JsxGetter
    public Function getOndevicemotion() {
        return getEventHandler(Event.TYPE_DEVICEMOTION);
    }

    /**
     * Sets the {@code ondevicemotion} event handler.
     * @param ondevicemotion the {@code ondevicemotion} event handler
     */
    @JsxSetter
    public void setOndevicemotion(final Object ondevicemotion) {
        setHandlerForJavaScript(Event.TYPE_DEVICEMOTION, ondevicemotion);
    }

    /**
     * Returns the {@code onstalled} event handler.
     * @return the {@code onstalled} event handler
     */
    @JsxGetter
    public Function getOnstalled() {
        return getEventHandler(Event.TYPE_STALLED);
    }

    /**
     * Sets the {@code onstalled} event handler.
     * @param onstalled the {@code onstalled} event handler
     */
    @JsxSetter
    public void setOnstalled(final Object onstalled) {
        setHandlerForJavaScript(Event.TYPE_STALLED, onstalled);
    }

    /**
     * Returns the {@code onmouseenter} event handler.
     * @return the {@code onmouseenter} event handler
     */
    @JsxGetter
    public Function getOnmouseenter() {
        return getEventHandler(Event.TYPE_MOUDEENTER);
    }

    /**
     * Sets the {@code onmouseenter} event handler.
     * @param onmouseenter the {@code onmouseenter} event handler
     */
    @JsxSetter
    public void setOnmouseenter(final Object onmouseenter) {
        setHandlerForJavaScript(Event.TYPE_MOUDEENTER, onmouseenter);
    }

    /**
     * Returns the {@code ondragleave} event handler.
     * @return the {@code ondragleave} event handler
     */
    @JsxGetter
    public Function getOndragleave() {
        return getEventHandler(Event.TYPE_DRAGLEAVE);
    }

    /**
     * Sets the {@code ondragleave} event handler.
     * @param ondragleave the {@code ondragleave} event handler
     */
    @JsxSetter
    public void setOndragleave(final Object ondragleave) {
        setHandlerForJavaScript(Event.TYPE_DRAGLEAVE, ondragleave);
    }

    /**
     * Returns the {@code onpointerdown} event handler.
     * @return the {@code onpointerdown} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerdown() {
        return getEventHandler(Event.TYPE_POINTERDOWN);
    }

    /**
     * Sets the {@code onpointerdown} event handler.
     * @param onpointerdown the {@code onpointerdown} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerdown(final Object onpointerdown) {
        setHandlerForJavaScript(Event.TYPE_POINTERDOWN, onpointerdown);
    }

    /**
     * Returns the {@code ondrop} event handler.
     * @return the {@code ondrop} event handler
     */
    @JsxGetter
    public Function getOndrop() {
        return getEventHandler(Event.TYPE_DROP);
    }

    /**
     * Sets the {@code ondrop} event handler.
     * @param ondrop the {@code ondrop} event handler
     */
    @JsxSetter
    public void setOndrop(final Object ondrop) {
        setHandlerForJavaScript(Event.TYPE_DROP, ondrop);
    }

    /**
     * Returns the {@code onunload} event handler.
     * @return the {@code onunload} event handler
     */
    @JsxGetter
    public Function getOnunload() {
        return getEventHandler(Event.TYPE_UNLOAD);
    }

    /**
     * Sets the {@code onunload} event handler.
     * @param onunload the {@code onunload} event handler
     */
    @JsxSetter
    public void setOnunload(final Object onunload) {
        setHandlerForJavaScript(Event.TYPE_UNLOAD, onunload);
    }

    /**
     * Returns the {@code onwebkitanimationend} event handler.
     * @return the {@code onwebkitanimationend} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnwebkitanimationend() {
        return getEventHandler(Event.TYPE_WEBKITANIMATIONEND);
    }

    /**
     * Sets the {@code onwebkitanimationend} event handler.
     * @param onwebkitanimationend the {@code onwebkitanimationend} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnwebkitanimationend(final Object onwebkitanimationend) {
        setHandlerForJavaScript(Event.TYPE_WEBKITANIMATIONEND, onwebkitanimationend);
    }

    /**
     * Returns the {@code ondragstart} event handler.
     * @return the {@code ondragstart} event handler
     */
    @JsxGetter
    public Function getOndragstart() {
        return getEventHandler(Event.TYPE_DRAGSTART);
    }

    /**
     * Sets the {@code ondragstart} event handler.
     * @param ondragstart the {@code ondragstart} event handler
     */
    @JsxSetter
    public void setOndragstart(final Object ondragstart) {
        setHandlerForJavaScript(Event.TYPE_DRAGSTART, ondragstart);
    }

    /**
     * Returns the {@code ontransitionend} event handler.
     * @return the {@code ontransitionend} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOntransitionend() {
        return getEventHandler(Event.TYPE_TRANSITIONEND);
    }

    /**
     * Sets the {@code ontransitionend} event handler.
     * @param ontransitionend the {@code ontransitionend} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOntransitionend(final Object ontransitionend) {
        setHandlerForJavaScript(Event.TYPE_TRANSITIONEND, ontransitionend);
    }

    /**
     * Returns the {@code ondeviceorientationabsolute} event handler.
     * @return the {@code ondeviceorientationabsolute} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOndeviceorientationabsolute() {
        return getEventHandler(Event.TYPE_DEVICEORIENTATIONABSOLUTE);
    }

    /**
     * Sets the {@code ondeviceorientationabsolute} event handler.
     * @param ondeviceorientationabsolute the {@code ondeviceorientationabsolute} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOndeviceorientationabsolute(final Object ondeviceorientationabsolute) {
        setHandlerForJavaScript(Event.TYPE_DEVICEORIENTATIONABSOLUTE, ondeviceorientationabsolute);
    }

    /**
     * Returns the {@code onvolumechange} event handler.
     * @return the {@code onvolumechange} event handler
     */
    @JsxGetter
    public Function getOnvolumechange() {
        return getEventHandler(Event.TYPE_VOLUMECHANGE);
    }

    /**
     * Sets the {@code onvolumechange} event handler.
     * @param onvolumechange the {@code onvolumechange} event handler
     */
    @JsxSetter
    public void setOnvolumechange(final Object onvolumechange) {
        setHandlerForJavaScript(Event.TYPE_VOLUMECHANGE, onvolumechange);
    }

    /**
     * Returns the {@code ongotpointercapture} event handler.
     * @return the {@code ongotpointercapture} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOngotpointercapture() {
        return getEventHandler(Event.TYPE_GOTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code ongotpointercapture} event handler.
     * @param ongotpointercapture the {@code ongotpointercapture} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOngotpointercapture(final Object ongotpointercapture) {
        setHandlerForJavaScript(Event.TYPE_GOTPOINTERCAPTURE, ongotpointercapture);
    }

    /**
     * Returns the {@code onpopstate} event handler.
     * @return the {@code onpopstate} event handler
     */
    @JsxGetter
    public Function getOnpopstate() {
        return getEventHandler(Event.TYPE_POPSTATE);
    }

    /**
     * Sets the {@code onpopstate} event handler.
     * @param onpopstate the {@code onpopstate} event handler
     */
    @JsxSetter
    public void setOnpopstate(final Object onpopstate) {
        setHandlerForJavaScript(Event.TYPE_POPSTATE, onpopstate);
    }

    /**
     * {@inheritDoc}
     * optimized version
     */
    @Override
    public BrowserVersion getBrowserVersion() {
        return getWebWindow().getWebClient().getBrowserVersion();
    }

    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        // see https://dom.spec.whatwg.org/#window-current-event
        // because event is replaceable we need this hack here
        if ("event".equals(name)) {
            final Slot slot = querySlot(Context.getCurrentContext(), "event");
            if (slot instanceof AccessorSlot) {
                delete("event");
            }
        }
        super.put(name, start, value);
    }

    /**
     * @return a boolean indicating whether the current context is secure (true) or not (false).
     */
    @JsxGetter
    public Object getIsSecureContext() {
        final Page page = getWebWindow().getEnclosedPage();
        if (page != null) {
            final String protocol = page.getUrl().getProtocol();
            if ("https".equals(protocol)
                    || "wss".equals(protocol)
                    || "file".equals(protocol)) {
                return true;
            }

            final String host = page.getUrl().getHost();
            if ("localhost".equals(host)
                    || "localhost.".equals(host)
                    || host.endsWith(".localhost")
                    || host.endsWith(".localhost.")) {
                return true;
            }
        }

        return false;
    }
}

class HTMLCollectionFrames extends HTMLCollection {
    private static final Log LOG = LogFactory.getLog(HTMLCollectionFrames.class);

    HTMLCollectionFrames(final HtmlPage page) {
        super(page, false);
        this.setIsMatchingPredicate((Predicate<DomNode> & Serializable) node -> node instanceof BaseFrameElement);
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

        return window.getScriptableObject();
    }

    @Override
    protected Object getWithPreemption(final String name) {
        final List<DomNode> elements = getElements();

        for (final Object next : elements) {
            final BaseFrameElement frameElt = (BaseFrameElement) next;
            final WebWindow window = frameElt.getEnclosedWindow();
            if (name.equals(window.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Property \"" + name + "\" evaluated (by name) to " + window);
                }
                return getScriptableForElement(window);
            }
        }

        return NOT_FOUND;
    }
}
