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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_COMPUTED_STYLE_PSEUDO_ACCEPT_WITHOUT_COLON;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_FORMFIELDS_ACCESSIBLE_BY_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_FRAME_BY_ID_RETURNS_WINDOW;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_SELECTION_NULL_IF_INVISIBLE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_TOP_WRITABLE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

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
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.crypto.Crypto;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS2Properties;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.MediaQueryList;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleMedia;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.DataTransfer;
import com.gargoylesoftware.htmlunit.javascript.host.html.DocumentProxy;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.performance.Performance;
import com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesis;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
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
 * @author Carsten Steul
 * @author Colin Alworth
 * @author Atsushi Nakagawa
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535873.aspx">MSDN documentation</a>
 */
@JsxClass
public class Window extends EventTarget implements WindowOrWorkerGlobalScope, Function, AutoCloseable {

    private static final Log LOG = LogFactory.getLog(Window.class);

    /** Definition of special cases for the smart DomHtmlAttributeChangeListenerImpl */
    private static final Set<String> ATTRIBUTES_AFFECTING_PARENT = new HashSet<>(Arrays.asList(
            "style",
            "class",
            "height",
            "width"));

    /** To be documented. */
    @JsxConstant({CHROME, EDGE})
    public static final short TEMPORARY = 0;

    /** To be documented. */
    @JsxConstant({CHROME, EDGE})
    public static final short PERSISTENT = 1;

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
    private Map<Class<? extends Scriptable>, Scriptable> prototypes_ = new HashMap<>();
    private Map<String, Scriptable> prototypesPerJSName_ = new HashMap<>();
    private Object controllers_;
    private Object opener_;
    private Object top_ = NOT_FOUND; // top can be set from JS to any value!
    private Crypto crypto_;

    private CSSPropertiesCache cssPropertiesCache_ = new CSSPropertiesCache();

    private final EnumMap<Type, Storage> storages_ = new EnumMap<>(Type.class);

    private transient List<AnimationFrame> animationFrames_ = new ArrayList<>();

    private static final class AnimationFrame {
        private long id_;
        private Function callback_;

        AnimationFrame(final long id, final Function callback) {
            id_ = id;
            callback_ = callback;
        }
    }

    /**
     * Cache computed styles when possible, because their calculation is very expensive.
     * We use a weak hash map because we don't want this cache to be the only reason
     * nodes are kept around in the JVM, if all other references to them are gone.
     */
    private static final class CSSPropertiesCache implements Serializable {
        private transient WeakHashMap<Element, Map<String, CSS2Properties>> computedStyles_ = new WeakHashMap<>();

        CSSPropertiesCache() {
        }

        public synchronized CSS2Properties get(final Element element, final String normalizedPseudo) {
            final Map<String, CSS2Properties> elementMap = computedStyles_.get(element);
            if (elementMap != null) {
                return elementMap.get(normalizedPseudo);
            }
            return null;
        }

        public synchronized void put(final Element element, final String normalizedPseudo, final CSS2Properties style) {
            Map<String, CSS2Properties> elementMap = computedStyles_.get(element);
            if (elementMap == null) {
                elementMap = new WeakHashMap<>();
                computedStyles_.put(element, elementMap);
            }
            elementMap.put(normalizedPseudo, style);
        }

        public synchronized void nodeChanged(final DomNode changed, final boolean clearParents) {
            final Iterator<Map.Entry<Element, Map<String, CSS2Properties>>> i = computedStyles_.entrySet().iterator();
            while (i.hasNext()) {
                final Map.Entry<Element, Map<String, CSS2Properties>> entry = i.next();
                final DomNode node = entry.getKey().getDomNodeOrDie();
                if (changed == node
                    || changed.getParentNode() == node.getParentNode()
                    || changed.isAncestorOf(node)
                    || clearParents && node.isAncestorOf(changed)) {
                    i.remove();
                }
            }

            // maybe this is a better solution but i have to think a bit more about this
            //
            //            if (computedStyles_.isEmpty()) {
            //                return;
            //            }
            //
            //            // remove all siblings
            //            DomNode parent = changed.getParentNode();
            //            if (parent != null) {
            //                for (DomNode sibling : parent.getChildNodes()) {
            //                    computedStyles_.remove(sibling.getScriptableObject());
            //                }
            //
            //                if (clearParents) {
            //                    // remove all parents
            //                    while (parent != null) {
            //                        computedStyles_.remove(parent.getScriptableObject());
            //                        parent = parent.getParentNode();
            //                    }
            //                }
            //            }
            //
            //            // remove changed itself and all descendants
            //            computedStyles_.remove(changed.getScriptableObject());
            //            for (DomNode descendant : changed.getDescendants()) {
            //                computedStyles_.remove(descendant.getScriptableObject());
            //            }
        }

        public synchronized void clear() {
            computedStyles_.clear();
        }

        public synchronized Map<String, CSS2Properties> remove(final Element element) {
            return computedStyles_.remove(element);
        }

        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            computedStyles_ = new WeakHashMap<>();
        }
    }

    /**
     * Creates an instance.
     */
    public Window() {
    }

    /**
     * Creates an instance.
     *
     * @param cx the current context
     * @param args the arguments to the ActiveXObject constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public static Scriptable jsConstructor(final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) {
        throw ScriptRuntime.typeError("Illegal constructor");
    }

    /**
     * Restores the transient fields during deserialization.
     * @param stream the stream to read the object from
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if a class is not found
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        cssPropertiesCache_ = new CSSPropertiesCache();
        animationFrames_ = new ArrayList<>();
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
        if (Undefined.isUndefined(defaultValue)) {
            defaultValue = null;
        }
        else {
            defaultValue = Context.toString(defaultValue);
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
     * Returns the application cache.
     * @return the application cache
     */
    @JsxGetter({FF, FF78, IE})
    public ApplicationCache getApplicationCache() {
        return applicationCache_;
    }

    /**
     * Returns the current event.
     * @return the current event, or {@code null} if no event is currently available
     */
    @JsxGetter
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
        if (!Undefined.isUndefined(url)) {
            urlString = Context.toString(url);
        }
        String windowName = "";
        if (!Undefined.isUndefined(name)) {
            windowName = Context.toString(name);
        }
        String featuresString = null;
        if (!Undefined.isUndefined(features)) {
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
        if (!Undefined.isUndefined(replace)) {
            replaceCurrentEntryInBrowsingHistory = Context.toBoolean(replace);
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
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created timer
     */
    @JsxFunction
    public static Object setTimeout(final Context context, final Scriptable thisObj,
            final Object[] args, final Function function) {
        return WindowOrWorkerGlobalScopeMixin.setTimeout(context, thisObj, args, function);
    }

    /**
     * Sets a chunk of JavaScript to be invoked each time a specified number of milliseconds has elapsed.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setInterval">
     * MDN web docs</a>
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created interval
     */
    @JsxFunction
    public static Object setInterval(final Context context, final Scriptable thisObj,
            final Object[] args, final Function function) {
        return WindowOrWorkerGlobalScopeMixin.setInterval(context, thisObj, args, function);
    }

    /**
     * Cancels a time-out previously set with the
     * {@link #setTimeout(Context, Scriptable, Object[], Function)} method.
     *
     * @param timeoutId identifier for the timeout to clear
     *        as returned by {@link #setTimeout(Context, Scriptable, Object[], Function)}
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
     * {@link #setInterval(Context, Scriptable, Object[], Function)} method.
     * Current implementation does nothing.
     * @param intervalID specifies the interval to cancel as returned by the
     *        {@link #setInterval(Context, Scriptable, Object[], Function)} method
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
    @JsxGetter({CHROME, EDGE, IE})
    public Navigator getClientInformation() {
        return navigator_;
    }

    /**
     * Special setter for IE to ignore this call.
     * @param ignore param gets ignored
     */
    @JsxSetter(IE)
    public void setClientInformation(final Object ignore) {
    }

    /**
     * Returns the JavaScript property {@code clipboardData}.
     * @return the {@link DataTransfer}
     */
    @JsxGetter(IE)
    public DataTransfer getClipboardData() {
        final DataTransfer dataTransfer = new DataTransfer();
        dataTransfer.setParentScope(this);
        dataTransfer.setPrototype(getPrototype(dataTransfer.getClass()));
        return dataTransfer;
    }

    /**
     * Returns the window property. This is a synonym for {@code self}.
     * @return the window property (a reference to <tt>this</tt>)
     */
    @JsxGetter(propertyName = "window")
    public Window getWindow_js() {
        return this;
    }

    /**
     * Returns the {@code self} property.
     * @return this
     */
    @JsxGetter
    public Window getSelf() {
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
     * Returns the {@code console} property.
     * @return the {@code console} property
     */
    @JsxGetter
    public ScriptableObject getConsole() {
        return console_;
    }

    /**
     * Sets the {@code console}.
     * @param console the console
     */
    @JsxSetter
    public void setConsole(final ScriptableObject console) {
        console_ = console;
    }

    /**
     * Prints messages to the {@code console}.
     * @param message the message to log
     */
    @JsxFunction({FF, FF78})
    public void dump(final String message) {
        if (console_ instanceof Console) {
            Console.log(null, console_, new Object[] {message}, null);
        }
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
        final int id = (int) Context.toNumber(requestId);

        final Iterator<AnimationFrame> frames = animationFrames_.iterator();
        while (frames.hasNext()) {
            final Window.AnimationFrame animationFrame = frames.next();
            if (animationFrame.id_ == id) {
                frames.remove();
            }
        }
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

            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            page.addDomChangeListener(listener);

            if (page.isHtmlPage()) {
                ((HtmlPage) page).addHtmlAttributeChangeListener(listener);
                ((HtmlPage) page).addAutoCloseable(this);
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
        location_.initialize(this, pageToEnclose);

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
        if (getBrowserVersion().hasFeature(JS_WINDOW_TOP_WRITABLE)) {
            top_ = o;
        }
    }

    /**
     * Returns the value of the {@code parent} property.
     * @return the value of the {@code parent} property
     */
    @JsxGetter
    public ScriptableObject getParent() {
        final WebWindow parent = getWebWindow().getParentWindow();
        return parent.getScriptableObject();
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
        if (getBrowserVersion().hasFeature(JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT)
            && newValue != null && !Undefined.isUndefined(newValue) && !(newValue instanceof Window)) {
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
     * Returns the value of the {@code frames} property.
     * @return the value of the {@code frames} property
     */
    @JsxGetter(propertyName = "frames")
    public Window getFrames_js() {
        return this;
    }

    /**
     * Returns the number of frames contained by this window.
     * @return the number of frames contained by this window
     */
    @JsxGetter
    public int getLength() {
        final HTMLCollection frames = getFrames();
        if (frames != null) {
            return frames.getLength();
        }
        return 0;
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
     * Loads the new HTML document corresponding to the specified URL.
     * @param url the location of the new HTML document to load
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536638%28VS.85%29.aspx">MSDN Documentation</a>
     */
    @JsxFunction(IE)
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

            final Event event = new Event(body, Event.TYPE_SCROLL);
            body.fireEvent(event);
        }
    }

    /**
     * Scrolls the window content down by the specified number of lines.
     * @param lines the number of lines to scroll down
     */
    @JsxFunction({FF, FF78})
    public void scrollByLines(final int lines) {
        final HTMLElement body = ((HTMLDocument) document_).getBody();
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
    @JsxFunction({FF, FF78})
    public void scrollByPages(final int pages) {
        final HTMLElement body = ((HTMLDocument) document_).getBody();
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
    public void scrollTo(final int x, final int y) {
        final HTMLElement body = ((HTMLDocument) document_).getBody();
        if (body != null) {
            body.setScrollLeft(x);
            body.setScrollTop(y);

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
        if (index < 0 || getWebWindow() == null) {
            return Undefined.instance;
        }

        final HTMLCollection frames = getFrames();
        if (frames == null || index >= frames.getLength()) {
            return Undefined.instance;
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

        if (elements.isEmpty()) {
            return NOT_FOUND;
        }

        if (elements.size() == 1) {
            return getScriptableFor(elements.get(0));
        }

        // Null must be changed to '' for proper collection initialization.
        final String expElementName = "null".equals(name) ? "" : name;

        return new HTMLCollection(page, true) {
            @Override
            protected List<DomNode> computeElements() {
                final List<DomElement> expElements = page.getElementsByName(expElementName);
                final List<DomNode> result = new ArrayList<>(expElements.size());

                for (final DomElement domElement : expElements) {
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
    public void setInneHeight(final int height) {
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
     * An undocumented IE function.
     */
    @JsxFunction(value = IE, functionName = "CollectGarbage")
    public void collectGarbage() {
        // Empty.
    }

    /**
     * Returns computed style of the element. Computed style represents the final computed values
     * of all CSS properties for the element. This method's return value is of the same type as
     * that of <tt>element.style</tt>, but the value returned by this method is read-only.
     *
     * @param element the element
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null});
     * e.g. ':before'
     * @return the computed style
     */
    @JsxFunction
    public CSS2Properties getComputedStyle(final Object element, final String pseudoElement) {
        if (!(element instanceof Element)) {
            throw ScriptRuntime.typeError("parameter 1 is not of type 'Element'");
        }
        final Element e = (Element) element;

        String normalizedPseudo = pseudoElement;
        if (normalizedPseudo != null) {
            if (normalizedPseudo.startsWith("::")) {
                normalizedPseudo = normalizedPseudo.substring(1);
            }
            else if (getBrowserVersion().hasFeature(JS_WINDOW_COMPUTED_STYLE_PSEUDO_ACCEPT_WITHOUT_COLON)
                    && normalizedPseudo.length() > 0 && normalizedPseudo.charAt(0) != ':') {
                normalizedPseudo = ":" + normalizedPseudo;
            }
        }

        final CSS2Properties styleFromCache = cssPropertiesCache_.get(e, normalizedPseudo);
        if (styleFromCache != null) {
            return styleFromCache;
        }

        final CSS2Properties style = new CSS2Properties(e.getStyle());
        final Object ownerDocument = e.getOwnerDocument();
        if (ownerDocument instanceof HTMLDocument) {
            final StyleSheetList sheets = ((HTMLDocument) ownerDocument).getStyleSheets();
            final boolean trace = LOG.isTraceEnabled();
            for (int i = 0; i < sheets.getLength(); i++) {
                final CSSStyleSheet sheet = (CSSStyleSheet) sheets.item(i);
                if (sheet.isActive() && sheet.isEnabled()) {
                    if (trace) {
                        LOG.trace("modifyIfNecessary: " + sheet + ", " + style + ", " + e);
                    }
                    sheet.modifyIfNecessary(style, e, normalizedPseudo);
                }
            }

            cssPropertiesCache_.put(e, normalizedPseudo, style);
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
    @JsxFunction(IE)
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
    @JsxFunction(IE)
    public Object showModelessDialog(final String url, final Object arguments, final String features) {
        final WebWindow webWindow = getWebWindow();
        final WebClient client = webWindow.getWebClient();
        try {
            final URL completeUrl = ((HtmlPage) getDomNodeOrDie()).getFullyQualifiedUrl(url);
            final DialogWindow dialog = client.openDialogWindow(completeUrl, webWindow, arguments);
            return dialog.getScriptableObject();
        }
        catch (final IOException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Gets the {@code controllers}. The result doesn't currently matter but it is important to return an
     * object as some JavaScript libraries check it.
     * @see <a href="https://developer.mozilla.org/En/DOM/Window.controllers">Mozilla documentation</a>
     * @return some object
     */
    @JsxGetter({FF, FF78})
    public Object getControllers() {
        return controllers_;
    }

    /**
     * Sets the {@code controllers}.
     * @param value the new value
     */
    @JsxSetter({FF, FF78})
    public void setControllers(final Object value) {
        controllers_ = value;
    }

    /**
     * Returns the value of {@code mozInnerScreenX} property.
     * @return the value of {@code mozInnerScreenX} property
     */
    @JsxGetter({FF, FF78})
    public int getMozInnerScreenX() {
        return 10;
    }

    /**
     * Returns the value of {@code mozInnerScreenY} property.
     * @return the value of {@code mozInnerScreenY} property
     */
    @JsxGetter({FF, FF78})
    public int getMozInnerScreenY() {
        return 79;
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
     * Clears the computed styles.
     */
    public void clearComputedStyles() {
        cssPropertiesCache_.clear();
    }

    /**
     * Clears the computed styles for a specific {@link Element}.
     * @param element the element to clear its cache
     */
    public void clearComputedStyles(final Element element) {
        cssPropertiesCache_.remove(element);
    }

    /**
     * Clears the computed styles for a specific {@link Element}
     * and all parent elements.
     * @param element the element to clear its cache
     */
    public void clearComputedStylesUpToRoot(final Element element) {
        cssPropertiesCache_.remove(element);

        Element parent = element.getParentElement();
        while (parent != null) {
            cssPropertiesCache_.remove(parent);
            parent = parent.getParentElement();
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
     *   <li><em>Child</em> (i.e. "div &gt; span"): Affected by changes to SN or to its parent.</li>
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

        DomHtmlAttributeChangeListenerImpl() {
        }

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
            final boolean clearParents = ATTRIBUTES_AFFECTING_PARENT.contains(attribName);
            cssPropertiesCache_.nodeChanged(changed, clearParents);
        }
    }

    /**
     * Gets the name of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/efy5bay1.aspx">MSDN doc</a>
     * @return "JScript"
     */
    @JsxFunction(value = IE, functionName = "ScriptEngine")
    public String scriptEngine() {
        return "JScript";
    }

    /**
     * Gets the build version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/yftk84kt.aspx">MSDN doc</a>
     * @return the build version
     */
    @JsxFunction(value = IE, functionName = "ScriptEngineBuildVersion")
    public int scriptEngineBuildVersion() {
        return 12_345;
    }

    /**
     * Gets the major version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/x7cbaet3.aspx">MSDN doc</a>
     * @return the major version
     */
    @JsxFunction(value = IE, functionName = "ScriptEngineMajorVersion")
    public int scriptEngineMajorVersion() {
        return getBrowserVersion().getBrowserVersionNumeric();
    }

    /**
     * Gets the minor version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/wzaz8hhz.aspx">MSDN doc</a>
     * @return the minor version
     */
    @JsxFunction(value = IE, functionName = "ScriptEngineMinorVersion")
    public int scriptEngineMinorVersion() {
        return 0;
    }

    /**
     * Should implement the stop() function on the window object.
     * (currently empty implementation)
     * @see <a href="https://developer.mozilla.org/en/DOM/window.stop">window.stop</a>
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public int getScrollX() {
        return 0;
    }

    /**
     * Returns the value of {@code scrollY} property.
     * @return the value of {@code scrollY} property
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public int getScrollY() {
        return 0;
    }

    /**
     * Returns the value of {@code netscape} property.
     * @return the value of {@code netscape} property
     */
    @JsxGetter({FF, FF78})
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
     * @param message the object passed to the window
     * @param targetOrigin the origin this window must be for the event to be dispatched
     * @param transfer an optional sequence of Transferable objects
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window.postMessage">MDN documentation</a>
     */
    @JsxFunction
    public void postMessage(final String message, final String targetOrigin, final Object transfer) {
        final Page page = getWebWindow().getEnclosedPage();
        final URL currentURL = page.getUrl();

        if (!"*".equals(targetOrigin) && !"/".equals(targetOrigin)) {
            URL targetURL = null;
            try {
                targetURL = new URL(targetOrigin);
            }
            catch (final Exception e) {
                throw Context.throwAsScriptRuntimeEx(
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

        final JavaScriptEngine jsEngine = (JavaScriptEngine) getWebWindow().getWebClient().getJavaScriptEngine();
        final PostponedAction action = new PostponedAction(page) {
            @Override
            public void execute() throws Exception {
                final ContextAction<Object> contextAction = new ContextAction<Object>() {
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
    @JsxGetter({CHROME, EDGE, IE})
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
    @JsxFunction({CHROME, EDGE, FF, FF78})
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
    @JsxGetter({CHROME, EDGE, IE})
    public Object getOffscreenBuffering() {
        if (getBrowserVersion().hasFeature(JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID)) {
            return "auto";
        }
        return true;
    }

    /**
     * Returns the {@code crypto} property.
     * @return the {@code crypto} property
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
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
     * Returns the {@code onfocusin} event handler.
     * @return the {@code onfocusin} event handler
     */
    @JsxGetter(IE)
    public Function getOnfocusin() {
        return getEventHandler(Event.TYPE_FOCUS_IN);
    }

    /**
     * Sets the {@code onfocusin} event handler.
     * @param onfocusin the {@code onfocusin} event handler
     */
    @JsxSetter(IE)
    public void setOnfocusin(final Object onfocusin) {
        setHandlerForJavaScript(Event.TYPE_FOCUS_IN, onfocusin);
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOninvalid() {
        return getEventHandler(Event.TYPE_INVALID);
    }

    /**
     * Sets the {@code oninvalid} event handler.
     * @param oninvalid the {@code oninvalid} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOninvalid(final Object oninvalid) {
        setHandlerForJavaScript(Event.TYPE_INVALID, oninvalid);
    }

    /**
     * Returns the {@code onpointerout} event handler.
     * @return the {@code onpointerout} event handler
     */
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnpointerout() {
        return getEventHandler(Event.TYPE_POINTEROUT);
    }

    /**
     * Sets the {@code onpointerout} event handler.
     * @param onpointerout the {@code onpointerout} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setOnpointerout(final Object onpointerout) {
        setHandlerForJavaScript(Event.TYPE_POINTEROUT, onpointerout);
    }

    /**
     * Returns the {@code onhelp} event handler.
     * @return the {@code onhelp} event handler
     */
    @JsxGetter(IE)
    public Function getOnhelp() {
        return getEventHandler(Event.TYPE_HELP);
    }

    /**
     * Sets the {@code onhelp} event handler.
     * @param onhelp the {@code onhelp} event handler
     */
    @JsxSetter(IE)
    public void setOnhelp(final Object onhelp) {
        setHandlerForJavaScript(Event.TYPE_HELP, onhelp);
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
    @JsxGetter({CHROME, EDGE})
    public Function getOnanimationiteration() {
        return getEventHandler(Event.TYPE_ANIMATIONITERATION);
    }

    /**
     * Sets the {@code onanimationiteration} event handler.
     * @param onanimationiteration the {@code onanimationiteration} event handler
     */
    @JsxSetter({CHROME, EDGE})
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
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnpointerenter() {
        return getEventHandler(Event.TYPE_POINTERENTER);
    }

    /**
     * Sets the {@code onpointerenter} event handler.
     * @param onpointerenter the {@code onpointerenter} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
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
     * Returns the {@code onmspointerleave} event handler.
     * @return the {@code onmspointerleave} event handler
     */
    @JsxGetter(IE)
    public Function getOnmspointerleave() {
        return getEventHandler(Event.TYPE_MSPOINTERLEAVE);
    }

    /**
     * Sets the {@code onmspointerleave} event handler.
     * @param onmspointerleave the {@code onmspointerleave} event handler
     */
    @JsxSetter(IE)
    public void setOnmspointerleave(final Object onmspointerleave) {
        setHandlerForJavaScript(Event.TYPE_MSPOINTERLEAVE, onmspointerleave);
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
     * Returns the {@code onmsgesturestart} event handler.
     * @return the {@code onmsgesturestart} event handler
     */
    @JsxGetter(IE)
    public Function getOnmsgesturestart() {
        return getEventHandler(Event.TYPE_MSGESTURESTART);
    }

    /**
     * Sets the {@code onmsgesturestart} event handler.
     * @param onmsgesturestart the {@code onmsgesturestart} event handler
     */
    @JsxSetter(IE)
    public void setOnmsgesturestart(final Object onmsgesturestart) {
        setHandlerForJavaScript(Event.TYPE_MSGESTURESTART, onmsgesturestart);
    }

    /**
     * Returns the {@code ondeviceproximity} event handler.
     * @return the {@code ondeviceproximity} event handler
     */
    @JsxGetter({FF, FF78})
    public Function getOndeviceproximity() {
        return getEventHandler(Event.TYPE_DEVICEPROXIMITY);
    }

    /**
     * Sets the {@code ondeviceproximity} event handler.
     * @param ondeviceproximity the {@code ondeviceproximity} event handler
     */
    @JsxSetter({FF, FF78})
    public void setOndeviceproximity(final Object ondeviceproximity) {
        setHandlerForJavaScript(Event.TYPE_DEVICEPROXIMITY, ondeviceproximity);
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
     * Returns the {@code onfocusout} event handler.
     * @return the {@code onfocusout} event handler
     */
    @JsxGetter(IE)
    public Function getOnfocusout() {
        return getEventHandler(Event.TYPE_FOCUS_OUT);
    }

    /**
     * Sets the {@code onfocusout} event handler.
     * @param onfocusout the {@code onfocusout} event handler
     */
    @JsxSetter(IE)
    public void setOnfocusout(final Object onfocusout) {
        setHandlerForJavaScript(Event.TYPE_FOCUS_OUT, onfocusout);
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndeviceorientation() {
        return getEventHandler(Event.TYPE_DEVICEORIENTATION);
    }

    /**
     * Sets the {@code ondeviceorientation} event handler.
     * @param ondeviceorientation the {@code ondeviceorientation} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
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
     * Returns the {@code onreadystatechange} event handler.
     * @return the {@code onreadystatechange} event handler
     */
    @JsxGetter(IE)
    public Function getOnreadystatechange() {
        return getEventHandler(Event.TYPE_READY_STATE_CHANGE);
    }

    /**
     * Sets the {@code onreadystatechange} event handler.
     * @param onreadystatechange the {@code onreadystatechange} event handler
     */
    @JsxSetter(IE)
    public void setOnreadystatechange(final Object onreadystatechange) {
        setHandlerForJavaScript(Event.TYPE_READY_STATE_CHANGE, onreadystatechange);
    }

    /**
     * Returns the {@code onmspointerover} event handler.
     * @return the {@code onmspointerover} event handler
     */
    @JsxGetter(IE)
    public Function getOnmspointerover() {
        return getEventHandler(Event.TYPE_MSPOINTEROVER);
    }

    /**
     * Sets the {@code onmspointerover} event handler.
     * @param onmspointerover the {@code onmspointerover} event handler
     */
    @JsxSetter(IE)
    public void setOnmspointerover(final Object onmspointerover) {
        setHandlerForJavaScript(Event.TYPE_MSPOINTEROVER, onmspointerover);
    }

    /**
     * Returns the {@code onpointermove} event handler.
     * @return the {@code onpointermove} event handler
     */
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnpointermove() {
        return getEventHandler(Event.TYPE_POINTERMOVE);
    }

    /**
     * Sets the {@code onpointermove} event handler.
     * @param onpointermove the {@code onpointermove} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setOnpointermove(final Object onpointermove) {
        setHandlerForJavaScript(Event.TYPE_POINTERMOVE, onpointermove);
    }

    /**
     * Returns the {@code onmspointermove} event handler.
     * @return the {@code onmspointermove} event handler
     */
    @JsxGetter(IE)
    public Function getOnmspointermove() {
        return getEventHandler(Event.TYPE_MSPOINTERMOVE);
    }

    /**
     * Sets the {@code onmspointermove} event handler.
     * @param onmspointermove the {@code onmspointermove} event handler
     */
    @JsxSetter(IE)
    public void setOnmspointermove(final Object onmspointermove) {
        setHandlerForJavaScript(Event.TYPE_MSPOINTERMOVE, onmspointermove);
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
     * Returns the {@code onuserproximity} event handler.
     * @return the {@code onuserproximity} event handler
     */
    @JsxGetter({FF, FF78})
    public Function getOnuserproximity() {
        return getEventHandler(Event.TYPE_USERPROXIMITY);
    }

    /**
     * Sets the {@code onuserproximity} event handler.
     * @param onuserproximity the {@code onuserproximity} event handler
     */
    @JsxSetter({FF, FF78})
    public void setOnuserproximity(final Object onuserproximity) {
        setHandlerForJavaScript(Event.TYPE_USERPROXIMITY, onuserproximity);
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
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnpointerover() {
        return getEventHandler(Event.TYPE_POINTEROVER);
    }

    /**
     * Sets the {@code onpointerover} event handler.
     * @param onpointerover the {@code onpointerover} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
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
    @JsxGetter({CHROME, EDGE})
    public Function getOnanimationend() {
        return getEventHandler(Event.TYPE_ANIMATIONEND);
    }

    /**
     * Sets the {@code onanimationend} event handler.
     * @param onanimationend the {@code onanimationend} event handler
     */
    @JsxSetter({CHROME, EDGE})
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
    @JsxGetter({FF, FF78, IE})
    public Function getOnafterprint() {
        return getEventHandler(Event.TYPE_AFTERPRINT);
    }

    /**
     * Sets the {@code onafterprint} event handler.
     * @param onafterprint the {@code onafterprint} event handler
     */
    @JsxSetter({FF, FF78, IE})
    public void setOnafterprint(final Object onafterprint) {
        setHandlerForJavaScript(Event.TYPE_AFTERPRINT, onafterprint);
    }

    /**
     * Returns the {@code onmozfullscreenerror} event handler.
     * @return the {@code onmozfullscreenerror} event handler
     */
    @JsxGetter({FF, FF78})
    public Function getOnmozfullscreenerror() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENERROR);
    }

    /**
     * Sets the {@code onmozfullscreenerror} event handler.
     * @param onmozfullscreenerror the {@code onmozfullscreenerror} event handler
     */
    @JsxSetter({FF, FF78})
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
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnmousewheel() {
        return getEventHandler(Event.TYPE_MOUSEWHEEL);
    }

    /**
     * Sets the {@code onmousewheel} event handler.
     * @param onmousewheel the {@code onmousewheel} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
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
     * Returns the {@code onmspointerenter} event handler.
     * @return the {@code onmspointerenter} event handler
     */
    @JsxGetter(IE)
    public Function getOnmspointerenter() {
        return getEventHandler(Event.TYPE_MSPOINTENTER);
    }

    /**
     * Sets the {@code onmspointerenter} event handler.
     * @param onmspointerenter the {@code onmspointerenter} event handler
     */
    @JsxSetter(IE)
    public void setOnmspointerenter(final Object onmspointerenter) {
        setHandlerForJavaScript(Event.TYPE_MSPOINTENTER, onmspointerenter);
    }

    /**
     * Returns the {@code onmozfullscreenchange} event handler.
     * @return the {@code onmozfullscreenchange} event handler
     */
    @JsxGetter({FF, FF78})
    public Function getOnmozfullscreenchange() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onmozfullscreenchange} event handler.
     * @param onmozfullscreenchange the {@code onmozfullscreenchange} event handler
     */
    @JsxSetter({FF, FF78})
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
        return getEventHandler(Event.TYPE_PLAYNG);
    }

    /**
     * Sets the {@code onplaying} event handler.
     * @param onplaying the {@code onplaying} event handler
     */
    @JsxSetter
    public void setOnplaying(final Object onplaying) {
        setHandlerForJavaScript(Event.TYPE_PLAYNG, onplaying);
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnlanguagechange() {
        return getEventHandler(Event.TYPE_LANGUAGECHANGE);
    }

    /**
     * Sets the {@code onlanguagechange} event handler.
     * @param onlanguagechange the {@code onlanguagechange} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
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
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnpointercancel() {
        return getEventHandler(Event.TYPE_POINTERCANCEL);
    }

    /**
     * Sets the {@code onpointercancel} event handler.
     * @param onpointercancel the {@code onpointercancel} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setOnpointercancel(final Object onpointercancel) {
        setHandlerForJavaScript(Event.TYPE_POINTERCANCEL, onpointercancel);
    }

    /**
     * Returns the {@code onmsgestureend} event handler.
     * @return the {@code onmsgestureend} event handler
     */
    @JsxGetter(IE)
    public Function getOnmsgestureend() {
        return getEventHandler(Event.TYPE_MSGESTUREEND);
    }

    /**
     * Sets the {@code onmsgestureend} event handler.
     * @param onmsgestureend the {@code onmsgestureend} event handler
     */
    @JsxSetter(IE)
    public void setOnmsgestureend(final Object onmsgestureend) {
        setHandlerForJavaScript(Event.TYPE_MSGESTUREEND, onmsgestureend);
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
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnpointerup() {
        return getEventHandler(Event.TYPE_POINTERUP);
    }

    /**
     * Sets the {@code onpointerup} event handler.
     * @param onpointerup the {@code onpointerup} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setOnpointerup(final Object onpointerup) {
        setHandlerForJavaScript(Event.TYPE_POINTERUP, onpointerup);
    }

    /**
     * Returns the {@code onwheel} event handler.
     * @return the {@code onwheel} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnwheel() {
        return getEventHandler(Event.TYPE_WHEEL);
    }

    /**
     * Sets the {@code onwheel} event handler.
     * @param onwheel the {@code onwheel} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnwheel(final Object onwheel) {
        setHandlerForJavaScript(Event.TYPE_WHEEL, onwheel);
    }

    /**
     * Returns the {@code onmspointerdown} event handler.
     * @return the {@code onmspointerdown} event handler
     */
    @JsxGetter(IE)
    public Function getOnmspointerdown() {
        return getEventHandler(Event.TYPE_MSPOINTERDOWN);
    }

    /**
     * Sets the {@code onmspointerdown} event handler.
     * @param onmspointerdown the {@code onmspointerdown} event handler
     */
    @JsxSetter(IE)
    public void setOnmspointerdown(final Object onmspointerdown) {
        setHandlerForJavaScript(Event.TYPE_MSPOINTERDOWN, onmspointerdown);
    }

    /**
     * Returns the {@code onpointerleave} event handler.
     * @return the {@code onpointerleave} event handler
     */
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnpointerleave() {
        return getEventHandler(Event.TYPE_POINTERLEAVE);
    }

    /**
     * Sets the {@code onpointerleave} event handler.
     * @param onpointerleave the {@code onpointerleave} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setOnpointerleave(final Object onpointerleave) {
        setHandlerForJavaScript(Event.TYPE_POINTERLEAVE, onpointerleave);
    }

    /**
     * Returns the {@code onbeforeprint} event handler.
     * @return the {@code onbeforeprint} event handler
     */
    @JsxGetter({FF, FF78, IE})
    public Function getOnbeforeprint() {
        return getEventHandler(Event.TYPE_BEFOREPRINT);
    }

    /**
     * Sets the {@code onbeforeprint} event handler.
     * @param onbeforeprint the {@code onbeforeprint} event handler
     */
    @JsxSetter({FF, FF78, IE})
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
     * Returns the {@code ondevicelight} event handler.
     * @return the {@code ondevicelight} event handler
     */
    @JsxGetter({FF, FF78})
    public Function getOndevicelight() {
        return getEventHandler(Event.TYPE_DEVICELIGHT);
    }

    /**
     * Sets the {@code ondevicelight} event handler.
     * @param ondevicelight the {@code ondevicelight} event handler
     */
    @JsxSetter({FF, FF78})
    public void setOndevicelight(final Object ondevicelight) {
        setHandlerForJavaScript(Event.TYPE_DEVICELIGHT, ondevicelight);
    }

    /**
     * Returns the {@code onanimationstart} event handler.
     * @return the {@code onanimationstart} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnanimationstart() {
        return getEventHandler(Event.TYPE_ANIMATIONSTART);
    }

    /**
     * Sets the {@code onanimationstart} event handler.
     * @param onanimationstart the {@code onanimationstart} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnanimationstart(final Object onanimationstart) {
        setHandlerForJavaScript(Event.TYPE_ANIMATIONSTART, onanimationstart);
    }

    /**
     * Returns the {@code onmspointercancel} event handler.
     * @return the {@code onmspointercancel} event handler
     */
    @JsxGetter(IE)
    public Function getOnmspointercancel() {
        return getEventHandler(Event.TYPE_MSPOINTERCANCEL);
    }

    /**
     * Sets the {@code onmspointercancel} event handler.
     * @param onmspointercancel the {@code onmspointercancel} event handler
     */
    @JsxSetter(IE)
    public void setOnmspointercancel(final Object onmspointercancel) {
        setHandlerForJavaScript(Event.TYPE_MSPOINTERCANCEL, onmspointercancel);
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
     * Returns the {@code onmspointerup} event handler.
     * @return the {@code onmspointerup} event handler
     */
    @JsxGetter(IE)
    public Function getOnmspointerup() {
        return getEventHandler(Event.TYPE_MSPOINTERUP);
    }

    /**
     * Sets the {@code onmspointerup} event handler.
     * @param onmspointerup the {@code onmspointerup} event handler
     */
    @JsxSetter(IE)
    public void setOnmspointerup(final Object onmspointerup) {
        setHandlerForJavaScript(Event.TYPE_MSPOINTERUP, onmspointerup);
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
     * Returns the {@code onmsinertiastart} event handler.
     * @return the {@code onmsinertiastart} event handler
     */
    @JsxGetter(IE)
    public Function getOnmsinertiastart() {
        return getEventHandler(Event.TYPE_MSINERTIASTART);
    }

    /**
     * Sets the {@code onmsinertiastart} event handler.
     * @param onmsinertiastart the {@code onmsinertiastart} event handler
     */
    @JsxSetter(IE)
    public void setOnmsinertiastart(final Object onmsinertiastart) {
        setHandlerForJavaScript(Event.TYPE_MSINERTIASTART, onmsinertiastart);
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
     * Returns the {@code onmsgesturetap} event handler.
     * @return the {@code onmsgesturetap} event handler
     */
    @JsxGetter(IE)
    public Function getOnmsgesturetap() {
        return getEventHandler(Event.TYPE_MSGESTURETAP);
    }

    /**
     * Sets the {@code onmsgesturetap} event handler.
     * @param onmsgesturetap the {@code onmsgesturetap} event handler
     */
    @JsxSetter(IE)
    public void setOnmsgesturetap(final Object onmsgesturetap) {
        setHandlerForJavaScript(Event.TYPE_MSGESTURETAP, onmsgesturetap);
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
     * Returns the {@code onmsgesturedoubletap} event handler.
     * @return the {@code onmsgesturedoubletap} event handler
     */
    @JsxGetter(IE)
    public Function getOnmsgesturedoubletap() {
        return getEventHandler(Event.TYPE_MSGESTUREDOUBLETAP);
    }

    /**
     * Sets the {@code onmsgesturedoubletap} event handler.
     * @param onmsgesturedoubletap the {@code onmsgesturedoubletap} event handler
     */
    @JsxSetter(IE)
    public void setOnmsgesturedoubletap(final Object onmsgesturedoubletap) {
        setHandlerForJavaScript(Event.TYPE_MSGESTUREDOUBLETAP, onmsgesturedoubletap);
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
     * Returns the {@code onmspointerout} event handler.
     * @return the {@code onmspointerout} event handler
     */
    @JsxGetter(IE)
    public Function getOnmspointerout() {
        return getEventHandler(Event.TYPE_MSPOINTEROUT);
    }

    /**
     * Sets the {@code onmspointerout} event handler.
     * @param onmspointerout the {@code onmspointerout} event handler
     */
    @JsxSetter(IE)
    public void setOnmspointerout(final Object onmspointerout) {
        setHandlerForJavaScript(Event.TYPE_MSPOINTEROUT, onmspointerout);
    }

    /**
     * Returns the {@code ondevicemotion} event handler.
     * @return the {@code ondevicemotion} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndevicemotion() {
        return getEventHandler(Event.TYPE_DEVICEMOTION);
    }

    /**
     * Sets the {@code ondevicemotion} event handler.
     * @param ondevicemotion the {@code ondevicemotion} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
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
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnpointerdown() {
        return getEventHandler(Event.TYPE_POINTERDOWN);
    }

    /**
     * Sets the {@code onpointerdown} event handler.
     * @param onpointerdown the {@code onpointerdown} event handler
     */
    @JsxSetter({CHROME, EDGE, IE})
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
     * Returns the {@code onmsgesturehold} event handler.
     * @return the {@code onmsgesturehold} event handler
     */
    @JsxGetter(IE)
    public Function getOnmsgesturehold() {
        return getEventHandler(Event.TYPE_MSGESTUREHOLD);
    }

    /**
     * Sets the {@code onmsgesturehold} event handler.
     * @param onmsgesturehold the {@code onmsgesturehold} event handler
     */
    @JsxSetter(IE)
    public void setOnmsgesturehold(final Object onmsgesturehold) {
        setHandlerForJavaScript(Event.TYPE_MSGESTUREHOLD, onmsgesturehold);
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
     * Returns the {@code onshow} event handler.
     * @return the {@code onshow} event handler
     */
    @JsxGetter(FF78)
    public Function getOnshow() {
        return getEventHandler(Event.TYPE_SHOW);
    }

    /**
     * Sets the {@code onshow} event handler.
     * @param onshow the {@code onshow} event handler
     */
    @JsxSetter(FF78)
    public void setOnshow(final Object onshow) {
        setHandlerForJavaScript(Event.TYPE_SHOW, onshow);
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
     * Returns the {@code onmsgesturechange} event handler.
     * @return the {@code onmsgesturechange} event handler
     */
    @JsxGetter(IE)
    public Function getOnmsgesturechange() {
        return getEventHandler(Event.TYPE_MSGESTURECHANGE);
    }

    /**
     * Sets the {@code onmsgesturechange} event handler.
     * @param onmsgesturechange the {@code onmsgesturechange} event handler
     */
    @JsxSetter(IE)
    public void setOnmsgesturechange(final Object onmsgesturechange) {
        setHandlerForJavaScript(Event.TYPE_MSGESTURECHANGE, onmsgesturechange);
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
     * Returns the {@code doNotTrack} property.
     * @return the {@code doNotTrack} property
     */
    @JsxGetter(IE)
    public Object getDoNotTrack() {
        final WebClient client = getWindow().getWebWindow().getWebClient();
        if (client.getOptions().isDoNotTrackEnabled()) {
            return 1;
        }
        return null;
    }

    @Override
    protected boolean isReadOnlySettable(final String name, final Object value) {
        if ("closed".equals(name)) {
            return false; //ignore
        }
        return super.isReadOnlySettable(name, value);
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
            if (getBrowserVersion().hasFeature(JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID) && frameElt.getId().equals(name)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Property \"" + name + "\" evaluated (by id) to " + window);
                }
                return getScriptableForElement(window);
            }
        }

        return NOT_FOUND;
    }
}
