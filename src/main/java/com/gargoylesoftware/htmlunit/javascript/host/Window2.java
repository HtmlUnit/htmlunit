/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlEmbed;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.NashornJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet2;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Selection2;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement2;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument2;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.js.internal.dynalink.CallSiteDescriptor;
import com.gargoylesoftware.js.internal.dynalink.linker.GuardedInvocation;
import com.gargoylesoftware.js.internal.dynalink.linker.LinkRequest;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Attribute;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.ECMAErrors;
import com.gargoylesoftware.js.nashorn.internal.runtime.FindProperty;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.Undefined;
import com.gargoylesoftware.js.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import com.gargoylesoftware.js.nashorn.internal.runtime.linker.NashornGuards;

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
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535873.aspx">MSDN documentation</a>
 */
@ScriptClass
public class Window2 extends EventTarget2 implements AutoCloseable {

    private static final Log LOG = LogFactory.getLog(Window2.class);

    /**
     * The minimum delay that can be used with setInterval() or setTimeout(). Browser minimums are
     * usually in the 10ms to 15ms range, but there's really no reason for us to waste that much time.
     * http://jsninja.com/Timers#Minimum_Timer_Delay_and_Reliability
     */
    private static final int MIN_TIMER_DELAY = 1;
    private WebWindow webWindow_;
    private boolean isProxy_;
    private Screen2 screen_;
    private Document2 document_;
    private History2 history_;
    private ScriptObject console_;
    private Location2 location_;
    private Selection2 selection_;
    private Event2 currentEvent_;
    private String status_ = "";
    private HTMLCollection2 frames_; // has to be a member to have equality (==) working
    private Object controllers_ = new SimpleScriptObject();
    private Object opener_;
    private Object top_;

    /**
     * Cache computed styles when possible, because their calculation is very expensive.
     * We use a weak hash map because we don't want this cache to be the only reason
     * nodes are kept around in the JVM, if all other references to them are gone.
     */
    private transient WeakHashMap<Element2, Map<String, ComputedCSSStyleDeclaration2>> computedStyles_
        = new WeakHashMap<>();

    /** To be documented. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
        where = Where.CONSTRUCTOR, value = CHROME)
    public static final int TEMPORARY = 0;

    /** To be documented. */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE,
        where = Where.CONSTRUCTOR, value = CHROME)
    public static final int PERSISTENT = 1;

    /**
     * Initialize the object.
     * @param enclosedPage the page containing the JavaScript
     */
    public void initialize(final Page enclosedPage) {
        final Global global = NashornJavaScriptEngine.getGlobal(enclosedPage.getEnclosingWindow());
        if (enclosedPage instanceof XmlPage) {
            document_ = XMLDocument2.constructor(true, global);
        }
        else {
            document_ = HTMLDocument2.constructor(true, global);
        }
        document_.setWindow(this);

        if (enclosedPage.isHtmlPage()) {
            final HtmlPage htmlPage = (HtmlPage) enclosedPage;

            setWebWindow(htmlPage.getEnclosingWindow());
            clearEventListenersContainer();

            document_.setDomNode(htmlPage);

            final WebWindow webWindow = getWebWindow();

            console_ = Console2.constructor(true, global);
            ((Console2) console_).setWebWindow(webWindow);

            location_ = Location2.constructor(true, global);
            location_.initialize(this);

            if (webWindow instanceof TopLevelWindow) {
                final WebWindow opener = ((TopLevelWindow) webWindow).getOpener();
                if (opener != null) {
                    opener_ = opener.getScriptableObject();
                }
            }
        }
    }

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static Window2 constructor(final boolean newObj, final Object self) {
        final Window2 host = new Window2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * The JavaScript function {@code alert()}.
     * @param self this object
     * @param message the message
     */
    @Function
    public static void alert(final Object self, String message) {
        final Window2 window = getWindow(self);
        final AlertHandler handler = window.getWebWindow().getWebClient().getAlertHandler();
        if (handler == null) {
            LOG.warn("window.alert(\"" + message + "\") no alert handler installed");
        }
        else {
            if (message == null) {
                message = "null";
            }
            handler.handleAlert(window.document_.getPage(), message);
        }
    }

    /**
     * Returns the WebWindow associated with this Window.
     * @return the WebWindow
     */
    public WebWindow getWebWindow() {
        return webWindow_;
    }

    /**
     * Sets the web window.
     * @param webWindow the {@link WebWindow}
     */
    public void setWebWindow(final WebWindow webWindow) {
        webWindow_ = webWindow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode getDomNodeOrNull() {
        return (HtmlPage) getWebWindow().getEnclosedPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode getDomNodeOrDie() {
        final DomNode domNode = getDomNodeOrNull();
        if (domNode == null) {
            final String clazz = getClass().getName();
            throw new IllegalStateException("DomNode has not been set for this SimpleScriptObject: " + clazz);
        }
        return domNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Window2 getWindow() {
        return this;
    }

    /**
     * Returns the value of the {@code top} property.
     * @param self this object
     * @return the value of {@code top}
     */
    @Getter
    public static Global getTop(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        final WebWindow top = webWindow.getTopWindow();
        return top.getScriptableObject();
    }

    /**
     * Gets the {@code controllers}. The result doesn't currently matter but it is important to return an
     * object as some JavaScript libraries check it.
     * @param self this object
     * @see <a href="https://developer.mozilla.org/En/DOM/Window.controllers">Mozilla documentation</a>
     * @return some object
     */
    @Getter(FF)
    public static Object getControllers(final Object self) {
        return getWindow(self).controllers_;
    }

    /**
     * Sets the {@code controllers}.
     * @param self this object
     * @param value the new value
     */
    @Setter(FF)
    public static void setControllers(final Object self, final Object value) {
        getWindow(self).controllers_ = value;
    }

    private Object getHandlerForJavaScript(final String eventName) {
        return getEventListenersContainer().getEventHandlerProp(eventName);
    }

    private void setHandlerForJavaScript(final String eventName, final Object handler) {
        if (handler == null || handler instanceof ScriptFunction) {
            getEventListenersContainer().setEventHandler(eventName, handler);
        }
        // Otherwise, fail silently.
    }

    /**
     * Returns the {@code onload} property. Note that this is not necessarily a function if something else has been set.
     * @param self this object
     * @return the {@code onload} property
     */
    @Getter
    public static Object getOnload(final Object self) {
        final Window2 window = getWindow(self);
        final Object onload = window.getHandlerForJavaScript(Event2.TYPE_LOAD);
        if (onload == null) {
            final HtmlPage page = (HtmlPage) window.getWebWindow().getEnclosedPage();
            final HtmlElement body = page.getBody();
            if (body != null) {
                final HTMLBodyElement2 b = (HTMLBodyElement2) body.getScriptObject2();
                return b.getEventHandler("onload");
            }
            return null;
        }
        return onload;
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param self this object
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    @Function
    public static String btoa(final Object self, final String stringToEncode) {
        return new String(Base64.encodeBase64(stringToEncode.getBytes()));
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding..
     * @param self this object
     * @param encodedData the encoded string
     * @return the decoded value
     */
    @Function
    public static String atob(final Object self, final String encodedData) {
        return new String(Base64.decodeBase64(encodedData.getBytes()));
    }

    /**
     * An undocumented IE function.
     * @param self this object
     */
    @Function(IE)
    public static void CollectGarbage(final Object self) {
        // Empty.
    }

    /**
     * Returns the JavaScript property {@code document}.
     * @param self this object
     * @return the document
     */
    @Getter
    public static Document2 getDocument(final Object self) {
        return getWindow(self).document_;
    }

    private static Window2 getWindow(Object self) {
        if (self instanceof ScriptFunction) {
            self = ((ScriptFunction) self).getScope();
        }
        if (self instanceof Global) {
            Window2 window = ((Global) self).getWindow();
            if (window.isProxy_) {
                window = window.getWebWindow().<Global>getScriptableObject().getWindow();
            }
            return window;
        }
        if (self instanceof Window2) {
            return (Window2) self;
        }
        return Global.instance().getWindow();
    }

    /**
     * Returns the value of the window's {@code name} property.
     * @param self this object
     * @return the value of the window's {@code name} property
     */
    @Getter
    public static String getName(final Object self) {
        return getWindow(self).getWebWindow().getName();
    }

    /**
     * Sets the value of the window's {@code name} property.
     * @param self this object
     * @param name the value of the window's {@code name} property
     */
    @Setter
    public static void setName(final Object self, final Object name) {
        getWindow(self).getWebWindow().setName(name.toString());
    }

    /**
     * Returns the {@code history} property.
     * @param self this object
     * @return the {@code history} property
     */
    @Getter
    public static History2 getHistory(final Object self) {
        final Window2 window = getWindow(self);
        if (window.history_ == null) {
            final Global global = window.getGlobal();
            window.history_ = History2.constructor(true, global);
        }
        return window.history_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FindProperty findProperty(final String key, final boolean deep, final ScriptObject start) {
        FindProperty prop = super.findProperty(key, deep, start);
        if (prop == null && getWebWindow() != null) {
            final Global global = getGlobal();
            prop = global.findProperty(key, deep);
        }
        return prop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuardedInvocation noSuchProperty(final CallSiteDescriptor desc, final LinkRequest request) {
        final String name = desc.getNameToken(CallSiteDescriptor.NAME_OPERAND);
        final boolean scopeAccess = NashornCallSiteDescriptor.isScope(desc);

        final MethodHandle mh = MethodHandles.insertArguments(
                staticHandle("getArbitraryProperty", Object.class, Global.class, String.class, boolean.class),
                1, name, scopeAccess);
        final boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(desc, request);
        return new GuardedInvocation(mh,
                NashornGuards.getMapGuard(getMap(), explicitInstanceOfCheck),
                getProtoSwitchPoints(name, null),
                explicitInstanceOfCheck ? null : ClassCastException.class);
    }

    @SuppressWarnings("unused")
    private static Object getArbitraryProperty(final Global self, final String name, final boolean scopeAccess) {
        final Window2 window = getWindow(self);
        final HtmlPage page = (HtmlPage) window.getWebWindow().getEnclosedPage();
        Object object = getFrameWindowByName(page, name);
        if (object == null) {
            object = window.getElementsByName(page, name);
        }
        if (object == null) {
            try {
                final HtmlElement htmlElement = page.getHtmlElementById(name);
                if (window.getBrowserVersion().hasFeature(JS_WINDOW_FRAME_BY_ID_RETURNS_WINDOW)
                        && htmlElement instanceof HtmlFrame) {
                    final HtmlFrame frame = (HtmlFrame) htmlElement;
                    object = window.getScriptableFor(frame.getEnclosedWindow());
                }
                else {
                    object = window.getScriptableFor(htmlElement);
                }
            }
            catch (final ElementNotFoundException e) {
            }
        }
        if (object == null) {
            if (scopeAccess) {
                throw ECMAErrors.referenceError("not.defined", name);
            }
            object = ScriptRuntime.UNDEFINED;
        }
        return object;
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
            return null;
        }

        if (elements.size() == 1) {
            return getScriptableFor(elements.get(0));
        }

        // Null must be changed to '' for proper collection initialization.
        final String expElementName = "null".equals(name) ? "" : name;

        return new HTMLCollection2(page, true) {
            @Override
            protected List<DomNode> computeElements() {
                final List<DomElement> expElements = page.getElementsByName(expElementName);
                final List<DomNode> result = new ArrayList<>(expElements.size());

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

    private static Global getFrameWindowByName(final HtmlPage page, final String name) {
        try {
            return page.getFrameByName(name).getScriptableObject();
        }
        catch (final ElementNotFoundException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final Object key) {
        if (key instanceof Number) {
            final int index = ((Number) key).intValue();
            if (index >= 0 && index < ((Number) getFrames2().getLength()).intValue()) {
                return true;
            }
        }
        return super.has(key);
    }

    /**
     * Returns the number of frames contained by this window.
     * @param self this object
     * @return the number of frames contained by this window
     */
    @Getter
    public static int getLength(final Object self) {
        final Window2 window = getWindow(self);
        return (int) window.getFrames2().getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int key) {
        final HTMLCollection2 frames = getFrames2();
        if (key >= (int) frames.getLength()) {
            return ScriptRuntime.UNDEFINED;
        }
        return frames.item(Integer.valueOf(key));
    }

    /**
     * Stub only at the moment.
     * @param self this object
     * @param search the text string for which to search
     * @param caseSensitive if true, specifies a case-sensitive search
     * @param backwards if true, specifies a backward search
     * @param wrapAround if true, specifies a wrap around search
     * @param wholeWord if true, specifies a whole word search
     * @param searchInFrames if true, specifies a search in frames
     * @param showDialog if true, specifies a show Dialog.
     * @return false
     */
    @Function({CHROME, FF})
    public static boolean find(final Object self, final String search, final boolean caseSensitive,
            final boolean backwards, final boolean wrapAround,
            final boolean wholeWord, final boolean searchInFrames, final boolean showDialog) {
        return false;
    }

    /**
     * Returns the {@code frames} property.
     * @param self this object
     * @return the {@code frames} property
     */
    @Getter
    public static Object getFrames(final Object self) {
        return self;
    }

    /**
     * Returns the {@code self} property.
     * @param self this object
     * @return the {@code self} property
     */
    @Getter
    public static Object getSelf(final Object self) {
        return self;
    }

    /**
     * Dispatches an event into the event system (standards-conformant browsers only). See
     * <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/dispatchEvent">the Gecko
     * DOM reference</a> for more information.
     *
     * @param target the target
     * @param event the event to be dispatched
     * @return {@code false} if at least one of the event handlers which handled the event
     *         called <tt>preventDefault</tt>; {@code true} otherwise
     */
    public static boolean dispatchEvent(final EventTarget2 target, final Event2 event) {
        event.setTarget(target);
        final ScriptResult result = target.getWindow().fireEvent(event);
        return !event.isAborted(result);
    }

    /**
     * Returns the live collection of frames contained by this window.
     * @return the live collection of frames contained by this window
     */
    private HTMLCollection2 getFrames2() {
        if (frames_ == null) {
            final HtmlPage page = (HtmlPage) getWebWindow().getEnclosedPage();
            frames_ = new HTMLCollectionFrames2(page);
        }
        return frames_;
    }

    /**
     * Returns computed style of the element. Computed style represents the final computed values
     * of all CSS properties for the element. This method's return value is of the same type as
     * that of <tt>element.style</tt>, but the value returned by this method is read-only.
     *
     * @param self this object
     * @param element the element
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null})
     * @return the computed style
     */
    @Function
    public static ComputedCSSStyleDeclaration2 getComputedStyle(final Object self,
            final Element2 element, final String pseudoElement) {
        final Window2 window2 = getWindow(self);
        synchronized (window2.computedStyles_) {
            final Map<String, ComputedCSSStyleDeclaration2> elementMap = window2.computedStyles_.get(element);
            if (elementMap != null) {
                final ComputedCSSStyleDeclaration2 style = elementMap.get(pseudoElement);
                if (style != null) {
                    return style;
                }
            }
        }

        final CSSStyleDeclaration2 original = element.getStyle();
        final Global global = window2.getGlobal();
        final ComputedCSSStyleDeclaration2 style = new ComputedCSSStyleDeclaration2(global, original);

        if (element.getOwnerDocument() instanceof HTMLDocument2) {
            final StyleSheetList2 sheets = ((HTMLDocument2) element.getOwnerDocument()).getStyleSheets();
            final boolean trace = LOG.isTraceEnabled();
            for (int i = 0; i < (int) sheets.getLength(); i++) {
                final CSSStyleSheet2 sheet = (CSSStyleSheet2) sheets.item(i);
                if (sheet.isActive() && sheet.isEnabled()) {
                    if (trace) {
                        LOG.trace("modifyIfNecessary: " + sheet + ", " + style + ", " + element);
                    }
                    sheet.modifyIfNecessary(style, element, pseudoElement);
                }
            }

            synchronized (window2.computedStyles_) {
                Map<String, ComputedCSSStyleDeclaration2> elementMap = window2.computedStyles_.get(element);
                if (elementMap == null) {
                    elementMap = new WeakHashMap<>();
                    window2.computedStyles_.put(element, elementMap);
                }
                elementMap.put(pseudoElement, style);
            }
        }

        return style;
    }

    /**
     * Returns the {@code screen} property.
     * @param self this object
     * @return the screen property
     */
    @Getter
    public static Screen2 getScreen(final Object self) {
        return getWindow(self).screen_;
    }

    /**
     * Returns the {@code location} property.
     * @param self this object
     * @return the {@code location} property
     */
    @Getter
    public static Location2 getLocation(final Object self) {
        return getWindow(self).location_;
    }

    /**
     * Sets the location property. This will cause a reload of the window.
     * @param self this object
     * @param newLocation the URL of the new content
     * @throws IOException when location loading fails
     */
    @Setter
    public static void setLocation(final Object self, final String newLocation) throws IOException {
        getWindow(self).location_.setHref(newLocation);
    }

    /**
     * Triggers the {@code onerror} handler, if one has been set.
     * @param e the error that needs to be reported
     */
    public void triggerOnError(final ScriptException e) {
        final Object o = getOnerror(this);
        if (o instanceof ScriptFunction) {
            final ScriptFunction f = (ScriptFunction) o;
            final Global global = getGlobal();

            final String msg = e.getMessage();
            final String url = e.getPage().getUrl().toExternalForm();
            final int line = e.getFailingLineNumber();
            final int column = e.getFailingColumnNumber();

            final Global oldGlobal = Context.getGlobal();
            try {
                Context.setGlobal(global);
                ScriptRuntime.apply(f, global,
                        msg, url, Integer.valueOf(line), Integer.valueOf(column), e);
            }
            finally {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    /**
     * Returns the value of the window's {@code onerror} property.
     * @param self this object
     * @return the value of the window's {@code onerror} property
     */
    @Getter
    public static Object getOnerror(final Object self) {
        return getWindow(self).getHandlerForJavaScript(Event2.TYPE_ERROR);
    }

    /**
     * Sets the value of the window's {@code onerror} property.
     * @param self this object
     * @param onerror the value of the window's {@code onerror} property
     */
    @Setter
    public static void setOnerror(final Object self, final Object onerror) {
        getWindow(self).setHandlerForJavaScript(Event2.TYPE_ERROR, onerror);
    }

    /**
     * Sets the value of the {@code onload} event handler.
     * @param self this object
     * @param onload the new handler
     */
    @Setter
    public static void setOnload(final Object self, final Object onload) {
        getWindow(self).getEventListenersContainer().setEventHandler(Event2.TYPE_LOAD, onload);
    }

    /**
     * Returns the {@code onclick} property (not necessary a function if something else has been set).
     * @param self this object
     * @return the {@code onclick} property
     */
    @Getter
    public static Object getOnclick(final Object self) {
        return getWindow(self).getHandlerForJavaScript(MouseEvent2.TYPE_CLICK);
    }

    /**
     * Sets the value of the {@code onclick} event handler.
     * @param self this object
     * @param onclick the new handler
     */
    @Setter
    public static void setOnclick(final Object self, final Object onclick) {
        getWindow(self).setHandlerForJavaScript(MouseEvent2.TYPE_CLICK, onclick);
    }

    /**
     * Returns the {@code ondblclick} property (not necessary a function if something else has been set).
     * @param self this object
     * @return the {@code ondblclick} property
     */
    @Getter
    public static Object getOndblclick(final Object self) {
        return getWindow(self).getHandlerForJavaScript(MouseEvent2.TYPE_DBL_CLICK);
    }

    /**
     * Sets the value of the {@code ondblclick} event handler.
     * @param self this object
     * @param ondblclick the new handler
     */
    @Setter
    public static void setOndblclick(final Object self, final Object ondblclick) {
        getWindow(self).setHandlerForJavaScript(MouseEvent2.TYPE_DBL_CLICK, ondblclick);
    }

    /**
     * Returns the {@code onhashchange} property (not necessary a function if something else has been set).
     * @param self this object
     * @return the {@code onhashchange} property
     */
    @Getter
    public static Object getOnhashchange(final Object self) {
        return getWindow(self).getHandlerForJavaScript(Event2.TYPE_HASH_CHANGE);
    }

    /**
     * Sets the value of the {@code onhashchange} event handler.
     * @param self this object
     * @param onhashchange the new handler
     */
    @Setter
    public static void setOnhashchange(final Object self, final Object onhashchange) {
        getWindow(self).setHandlerForJavaScript(Event2.TYPE_HASH_CHANGE, onhashchange);
    }

    /**
     * Returns the value of the window's {@code onbeforeunload} property.
     * @param self this object
     * @return the value of the window's {@code onbeforeunload} property
     */
    @Getter
    public static Object getOnbeforeunload(final Object self) {
        return getWindow(self).getHandlerForJavaScript(Event2.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Sets the value of the window's {@code onbeforeunload} property.
     * @param self this object
     * @param onbeforeunload the value of the window's {@code onbeforeunload} property
     */
    @Setter
    public static void setOnbeforeunload(final Object self, final Object onbeforeunload) {
        getWindow(self).setHandlerForJavaScript(Event2.TYPE_BEFORE_UNLOAD, onbeforeunload);
    }

    /**
     * Getter for the {@code onchange} event handler.
     * @param self this object
     * @return the handler
     */
    @Getter
    public static Object getOnchange(final Object self) {
        return getWindow(self).getHandlerForJavaScript(Event2.TYPE_CHANGE);
    }

    /**
     * Setter for the {@code onchange} event handler.
     * @param self this object
     * @param onchange the handler
     */
    @Setter
    public static void setOnchange(final Object self, final Object onchange) {
        getWindow(self).setHandlerForJavaScript(Event2.TYPE_CHANGE, onchange);
    }

    /**
     * Getter for the {@code onsubmit} event handler.
     * @param self this object
     * @return the handler
     */
    @Getter
    public static Object getOnsubmit(final Object self) {
        return getWindow(self).getHandlerForJavaScript(Event2.TYPE_SUBMIT);
    }

    /**
     * Setter for the {@code onsubmit} event handler.
     * @param self this object
     * @param onsubmit the handler
     */
    @Setter
    public static void setOnsubmit(final Object self, final Object onsubmit) {
        getWindow(self).setHandlerForJavaScript(Event2.TYPE_SUBMIT, onsubmit);
    }

    /**
     * Returns the current event.
     * @param self this object
     * @return the current event, or {@code null} if no event is currently available
     */
    @Getter({IE, CHROME})
    public static Object getEvent(final Object self) {
        return getWindow(self).currentEvent_;
    }

    /**
     * Returns the current event (used internally regardless of the emulation mode).
     * @return the current event, or {@code null} if no event is currently available
     */
    public Event2 getCurrentEvent() {
        return currentEvent_;
    }

    /**
     * Sets the current event.
     * @param event the current event
     */
    public void setCurrentEvent(final Event2 event) {
        currentEvent_ = event;
    }

    /**
     * Opens a new window.
     *
     * @param self this object
     * @param url when a new document is opened, <i>url</i> is a String that specifies a MIME type for the document.
     *        When a new window is opened, <i>url</i> is a String that specifies the URL to render in the new window
     * @param name the name
     * @param features the features
     * @param replace whether to replace in the history list or no
     * @return the newly opened window, or {@code null} if popup windows have been disabled
     * @see com.gargoylesoftware.htmlunit.WebClientOptions#isPopupBlockerEnabled()
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536651.aspx">MSDN documentation</a>
     */
    @Function
    public static ScriptObject open(final Object self, final Object url, final Object name, final Object features,
            final Object replace) {
        String urlString = null;
        if (url != ScriptRuntime.UNDEFINED) {
            urlString = url.toString();
        }
        String windowName = "";
        if (name != ScriptRuntime.UNDEFINED) {
            windowName = name.toString();
        }
        String featuresString = null;
        if (features != ScriptRuntime.UNDEFINED) {
            featuresString = features.toString();
        }
        final Window2 window = getWindow(self);
        final WebWindow webWindow = window.getWebWindow();
        final WebClient webClient = webWindow.getWebClient();

        if (webClient.getOptions().isPopupBlockerEnabled()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignoring window.open() invocation because popups are blocked.");
            }
            return null;
        }

        boolean replaceCurrentEntryInBrowsingHistory = false;
        if (replace != ScriptRuntime.UNDEFINED) {
            replaceCurrentEntryInBrowsingHistory = Boolean.parseBoolean(replace.toString());
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
                final Global global = webClient.getWebWindowByName(windowName).getScriptableObject();
                global.<Window2>getWindow().isProxy_ = true;
                return global;
            }
            catch (final WebWindowNotFoundException e) {
                // nothing
            }
        }
        final URL newUrl = window.makeUrlForOpenWindow(urlString);
        final WebWindow newWebWindow = webClient.openWindow(newUrl, windowName, webWindow);
        final Global global = newWebWindow.getScriptableObject();
        global.<Window2>getWindow().isProxy_ = true;
        return global;
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
     * Sets the {@code opener} property.
     * @param self this object
     * @param newValue the new value
     */
    @Setter
    public static void setOpener(final Object self, final Object newValue) {
        final Window2 window = getWindow(self);
        if (window.getBrowserVersion().hasFeature(JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT)
            && newValue != null && newValue != ScriptRuntime.UNDEFINED && !(newValue instanceof Global)) {
            throw new RuntimeException("Can't set opener to something other than a window!");
        }
        window.opener_ = newValue;
    }

    /**
     * Returns the value of the {@code opener} property.
     * @param self this object
     * @return the value of the {@code opener}, or {@code null} for a top level window
     */
    @Getter
    public static Object getOpener(final Object self) {
        return getWindow(self).opener_;
    }

    /**
     * Scrolls to the specified location on the page.
     * @param self this object
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    @Function
    public static void scroll(final Object self, final int x, final int y) {
        scrollTo(self, x, y);
    }

    /**
     * Scrolls the window content the specified distance.
     * @param self this object
     * @param x the horizontal distance to scroll by
     * @param y the vertical distance to scroll by
     */
    @Function
    public static void scrollBy(final Object self, final int x, final int y) {
        final HTMLElement2 body = ((HTMLDocument2) getWindow(self).document_).getBody();
        if (body != null) {
            body.setScrollLeft(body.getScrollLeft() + x);
            body.setScrollTop(body.getScrollTop() + y);
        }
    }

    /**
     * Returns the value of {@code pageXOffset} property.
     * @param self this object
     * @return the value of {@code pageXOffset} property
     */
    @Getter
    public static int getPageXOffset(final Object self) {
        return 0;
    }

    /**
     * Returns the value of {@code pageYOffset} property.
     * @param self this object
     * @return the value of {@code pageYOffset} property
     */
    @Getter
    public static int getPageYOffset(final Object self) {
        return 0;
    }

    /**
     * Returns the value of {@code scrollX} property.
     * @param self this object
     * @return the value of {@code scrollX} property
     */
    @Getter({CHROME, FF})
    public static int getScrollX(final Object self) {
        return 0;
    }

    /**
     * Returns the value of {@code scrollY} property.
     * @param self this object
     * @return the value of {@code scrollY} property
     */
    @Getter({CHROME, FF})
    public static int getScrollY(final Object self) {
        return 0;
    }

    /**
     * Posts a message.
     * @param self this object
     * @param message the object passed to the window
     * @param targetOrigin the origin this window must be for the event to be dispatched
     * @param transfer an optional sequence of Transferable objects
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window.postMessage">MDN documentation</a>
     */
    @Function
    public static void postMessage(final Object self, final String message, final String targetOrigin,
            final Object transfer) {
        final Window2 window = getWindow(self);
        final URL currentURL = window.getWebWindow().getEnclosedPage().getUrl();

        // TODO: do the same origin check for '/' also
        if (!"*".equals(targetOrigin) && !"/".equals(targetOrigin)) {
            URL targetURL = null;
            try {
                targetURL = new URL(targetOrigin);
            }
            catch (final Exception e) {
                throw new RuntimeException(
                                "SyntaxError: Failed to execute 'postMessage' on 'Window': Invalid target origin '"
                                + targetOrigin + "' was specified (reason: " + e.getMessage() + ".");
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
        final Global global = window.getGlobal();
        final MessageEvent2 event = MessageEvent2.constructor(true, global);
        final String origin = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();
        event.initMessageEvent(Event2.TYPE_MESSAGE, false, false, message, origin, "", window, transfer);

        final NashornJavaScriptEngine jsEngine =
                (NashornJavaScriptEngine) window.getWebWindow().getWebClient().getJavaScriptEngine();
        final PostponedAction action = new PostponedAction(window.getWebWindow().getEnclosedPage()) {
            @Override
            public void execute() throws Exception {
                final Global oldGlobal = Context.getGlobal();
                try {
                    Context.setGlobal(global);

                    dispatchEvent(global, event);
                }
                finally {
                    Context.setGlobal(oldGlobal);
                }
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
     * Dummy implementation for {@code requestAnimationFrame}.
     * @param self this object
     * @param callback the function to call when it's time to update the animation
     * @return an identification id
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window/requestAnimationFrame">MDN Doc</a>
     */
    @Function
    public static int requestAnimationFrame(final Object self, final Object callback) {
        // nothing for now
        return 1;
    }

    /**
     * Dummy implementation for {@code cancelAnimationFrame}.
     * @param self this object
     * @param requestId the ID value returned by the call to window.requestAnimationFrame()
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Window/cancelAnimationFrame">MDN Doc</a>
     */
    @Function
    public static void cancelAnimationFrame(final Object self, final Object requestId) {
        // nothing for now
    }

    /**
     * Scrolls to the specified location on the page.
     * @param self this object
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    @Function
    public static void scrollTo(final Object self, final int x, final int y) {
        final HTMLElement2 body = ((HTMLDocument2) getWindow(self).document_).getBody();
        if (body != null) {
            body.setScrollLeft(x);
            body.setScrollTop(y);
        }
    }

    /**
     * Sets a chunk of JavaScript to be invoked at some specified time later.
     * The invocation occurs only if the window is opened after the delay
     * and does not contain an other page than the one that originated the setTimeout.
     *
     * @param self this object
     * @param code specifies the function pointer or string that indicates the code to be executed
     *        when the specified interval has elapsed
     * @param timeout specifies the number of milliseconds
     * @param language specifies language
     * @return the id of the created timer
     */
    @Function
    public static int setTimeout(final Object self, final Object code, int timeout, final Object language) {
        if (timeout < MIN_TIMER_DELAY) {
            timeout = MIN_TIMER_DELAY;
        }
        if (code == null) {
            throw new RuntimeException("Function not provided.");
        }

        final int id;
        final Window2 window = getWindow(self);
        final WebWindow webWindow = window.getWebWindow();
        final Page page = (Page) window.getDomNodeOrNull();
        if (code instanceof String) {
            final String s = (String) code;
            final String description = "window.setTimeout(" + s + ", " + timeout + ")";
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavaScriptJob(timeout, null, description, webWindow, s);
            id = webWindow.getJobManager().addJob(job, page);
        }
        else if (code instanceof ScriptFunction) {
            final ScriptFunction f = (ScriptFunction) code;
            final String functionName = f.getName();

            final String description = "window.setTimeout(" + functionName + ", " + timeout + ")";
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                    createJavaScriptJob(timeout, null, description, webWindow, f);
            id = webWindow.getJobManager().addJob(job, page);
        }
        else {
            throw new RuntimeException("Unknown type for function.");
        }
        return id;
    }

    /**
     * Cancels a time-out previously set with the {@link #setTimeout(Object, Object, int, Object)} method.
     *
     * @param self this object
     * @param timeoutId identifier for the timeout to clear
     *        (returned by {@link #setTimeout(Object, Object, int, Object)})
     */
    @Function
    public static void clearTimeout(final Object self, final int timeoutId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearTimeout(" + timeoutId + ")");
        }
        getWindow(self).getWebWindow().getJobManager().removeJob(timeoutId);
    }

    /**
     * Returns the {@code console} property.
     * @param self this object
     * @return the {@code console} property
     */
    @Getter
    public static ScriptObject getConsole(final Object self) {
        return getWindow(self).console_;
    }

    /**
     * Sets the {@code console}.
     * @param self this object
     * @param console the console
     */
    @Setter
    public static void setConsole(final Object self, final ScriptObject console) {
        getWindow(self).console_ = console;
    }

    /**
     * Prints messages to the {@code console}.
     * @param self this object
     * @param message the message to log
     */
    @Function(FF)
    public static void dump(final Object self, final String message) {
        final Object console = getWindow(self).console_;
        if (console instanceof Console2) {
            ((Console2) console).log(message);
        }
    }

    /**
     * Returns the value of {@code mozInnerScreenX} property.
     * @param self this object
     * @return the value of {@code mozInnerScreenX} property
     */
    @Getter(FF)
    public static int getMozInnerScreenX(final Object self) {
        return 11;
    }

    /**
     * Returns the value of {@code mozInnerScreenY} property.
     * @param self this object
     * @return the value of {@code mozInnerScreenY} property
     */
    @Getter(FF)
    public static int getMozInnerScreenY(final Object self) {
        return 91;
    }

    /**
     * Returns the value of {@code mozPaintCount} property.
     * @param self this object
     * @return the value of {@code mozPaintCount} property
     */
    @Getter(FF)
    public static int getMozPaintCount(final Object self) {
        return 0;
    }

    /**
     * Scrolls the window content down by the specified number of lines.
     * @param self this object
     * @param lines the number of lines to scroll down
     */
    @Function(FF)
    public static void scrollByLines(final Object self, final int lines) {
        final HTMLElement2 body = ((HTMLDocument2) getWindow(self).document_).getBody();
        if (body != null) {
            body.setScrollTop(body.getScrollTop() + (19 * lines));
        }
    }

    /**
     * Scrolls the window content down by the specified number of pages.
     * @param self this object
     * @param pages the number of pages to scroll down
     */
    @Function(FF)
    public static void scrollByPages(final Object self, final int pages) {
        final Window2 window = getWindow(self);
        final HTMLElement2 body = ((HTMLDocument2) window.document_).getBody();
        if (body != null) {
            body.setScrollTop(body.getScrollTop() + (getInnerHeight(self) * pages));
        }
    }

    /**
     * Returns the {@code innerHeight}.
     * @param self this object
     * @return the {@code innerHeight}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref27.html">Mozilla doc</a>
     */
    @Getter
    public static int getInnerHeight(final Object self) {
        return getWindow(self).getWebWindow().getInnerHeight();
    }

    /**
     * Returns the {@code outerHeight}.
     * @param self this object
     * @return the {@code outerHeight}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref78.html">Mozilla doc</a>
     */
    @Getter
    public static int getOuterHeight(final Object self) {
        return getWindow(self).getWebWindow().getOuterHeight();
    }

    /**
     * Returns the {@code innerWidth}.
     * @param self this object
     * @return the {@code innerWidth}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref28.html">Mozilla doc</a>
     */
    @Getter
    public static int getInnerWidth(final Object self) {
        return getWindow(self).getWebWindow().getInnerWidth();
    }

    /**
     * Returns the {@code outerWidth}.
     * @param self this object
     * @return the {@code outerWidth}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref79.html">Mozilla doc</a>
     */
    @Getter
    public static int getOuterWidth(final Object self) {
        return getWindow(self).getWebWindow().getOuterWidth();
    }

    /**
     * Creates a modal dialog box that displays the specified HTML document.
     * @param self this object
     * @param url the URL of the document to load and display
     * @param arguments object to be made available via <tt>window.dialogArguments</tt> in the dialog window
     * @param features string that specifies the window ornaments for the dialog window
     * @return the value of the {@code returnValue} property as set by the modal dialog's window
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536759.aspx">MSDN Documentation</a>
     * @see <a href="https://developer.mozilla.org/en/DOM/window.showModalDialog">Mozilla Documentation</a>
     */
    @Function({IE, FF})
    public static Object showModalDialog(final Object self, final String url, final Object arguments,
            final String features) {
        final Window2 window = getWindow(self);
        final WebWindow webWindow = window.getWebWindow();
        final WebClient client = webWindow.getWebClient();
        try {
            final URL completeUrl = ((HtmlPage) window.getDomNodeOrDie()).getFullyQualifiedUrl(url);
            final DialogWindow dialog = client.openDialogWindow(completeUrl, webWindow, arguments);
            // TODO: Theoretically, we shouldn't return until the dialog window has been close()'ed...
            // But we have to return so that the window can be close()'ed...
            // Maybe we can use Rhino's continuation support to save state and restart when
            // the dialog window is close()'ed? Would only work in interpreted mode, though.
            final ScriptObject jsDialog = dialog.getScriptableObject();
            return jsDialog.getProperty("returnValue").getObjectValue(jsDialog, jsDialog);
        }
        catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a modeless dialog box that displays the specified HTML document.
     * @param self this object
     * @param url the URL of the document to load and display
     * @param arguments object to be made available via <tt>window.dialogArguments</tt> in the dialog window
     * @param features string that specifies the window ornaments for the dialog window
     * @return a reference to the new window object created for the modeless dialog
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536761.aspx">MSDN Documentation</a>
     */
    @Function(IE)
    public static Global showModelessDialog(final Object self, final String url, final Object arguments,
            final String features) {
        final Window2 window = getWindow(self);
        final WebWindow webWindow = window.getWebWindow();
        final WebClient client = webWindow.getWebClient();
        try {
            final URL completeUrl = ((HtmlPage) window.getDomNodeOrDie()).getFullyQualifiedUrl(url);
            final DialogWindow dialog = client.openDialogWindow(completeUrl, webWindow, arguments);
            return dialog.getScriptableObject();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the name of the scripting engine.
     * @param self this object
     * @see <a href="http://msdn.microsoft.com/en-us/library/efy5bay1.aspx">MSDN doc</a>
     * @return "JScript"
     */
    @Function(IE)
    public static String ScriptEngine(final Object self) {
        return "JScript";
    }

    /**
     * Gets the build version of the scripting engine.
     * @param self this object
     * @see <a href="http://msdn.microsoft.com/en-us/library/yftk84kt.aspx">MSDN doc</a>
     * @return the build version
     */
    @Function(IE)
    public static int ScriptEngineBuildVersion(final Object self) {
        return 12345;
    }

    /**
     * Gets the major version of the scripting engine.
     * @param self this object
     * @see <a href="http://msdn.microsoft.com/en-us/library/x7cbaet3.aspx">MSDN doc</a>
     * @return the major version
     */
    @Function(IE)
    public static int ScriptEngineMajorVersion(final Object self) {
        return getWindow(self).getBrowserVersion().getBrowserVersionNumeric();
    }

    /**
     * Gets the minor version of the scripting engine.
     * @param self this object
     * @see <a href="http://msdn.microsoft.com/en-us/library/wzaz8hhz.aspx">MSDN doc</a>
     * @return the minor version
     */
    @Function(IE)
    public static int ScriptEngineMinorVersion(final Object self) {
        return 0;
    }

    /**
     * Closes this window.
     * @param self this object
     */
    @Function(name = "close")
    public static void close_js(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        if (webWindow instanceof TopLevelWindow) {
            ((TopLevelWindow) webWindow).close();
        }
        else {
            webWindow.getWebClient().deregisterWebWindow(webWindow);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        Symbol2.remove(this);
    }

    /**
     * The JavaScript function {@code confirm}.
     * @param self this object
     * @param message the message
     * @return true if ok was pressed, false if cancel was pressed
     */
    @Function
    public static boolean confirm(final Object self, final String message) {
        final Window2 window = getWindow(self);
        final ConfirmHandler handler = window.getWebWindow().getWebClient().getConfirmHandler();
        if (handler == null) {
            LOG.warn("window.confirm(\""
                    + message + "\") no confirm handler installed, simulating the OK button");
            return true;
        }
        return handler.handleConfirm(window.document_.getPage(), message);
    }

    /**
     * Sets the focus to this element.
     * @param self this object
     */
    @Function
    public static void focus(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        webWindow.getWebClient().setCurrentWindow(webWindow);
    }

    /**
     * Removes focus from this element.
     * @param self this object
     */
    @Function
    public static void blur(final Object self) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.blur() not implemented");
        }
    }

    /**
     * Returns the value of the {@code parent} property.
     * @param self this object
     * @return the value of the {@code parent} property
     */
    @Getter
    public static Global getParent(final Object self) {
        final WebWindow parent = getWindow(self).getWebWindow().getParentWindow();
        return parent.getScriptableObject();
    }

    /**
     * The JavaScript function {@code prompt}.
     * @param self this object
     * @param message the message
     * @param defaultValue the default value displayed in the text input field
     * @return true if ok was pressed, false if cancel was pressed
     */
    @Function
    public static String prompt(final Object self, final String message, Object defaultValue) {
        final Window2 window = getWindow(self);
        final PromptHandler handler = window.getWebWindow().getWebClient().getPromptHandler();
        if (handler == null) {
            LOG.warn("window.prompt(\"" + message + "\") no prompt handler installed");
            return null;
        }
        if (defaultValue == Undefined.getUndefined()) {
            defaultValue = null;
        }
        else {
            defaultValue = ScriptRuntime.safeToString(defaultValue);
        }
        return handler.handlePrompt(window.document_.getPage(), message, (String) defaultValue);
    }

    /**
     * Returns the text from the status line.
     * @param self this object
     * @return the status line text
     */
    @Getter
    public static String getStatus(final Object self) {
        return getWindow(self).status_;
    }

    /**
     * Sets the text from the status line.
     * @param self this object
     * @param message the status line text
     */
    @Setter
    public static void setStatus(final Object self, final String message) {
        final Window2 window = getWindow(self);
        final WebWindow webWindow = window.getWebWindow();
        window.status_ = message;

        final StatusHandler statusHandler = webWindow.getWebClient().getStatusHandler();
        if (statusHandler != null) {
            statusHandler.statusMessageChanged(webWindow.getEnclosedPage(), message);
        }
    }

    /**
     * Does nothing special anymore.
     * @param self self
     * @param type the type of events to capture
     * @see Document2#captureEvents(String)
     */
    @Function
    public static void captureEvents(final Object self, final String type) {
        // Empty.
    }

    /**
     * Indicates if this window is closed.
     * @param self self
     * @return {@code true} if this window is closed
     */
    @Getter
//    @CanSetReadOnly(CanSetReadOnlyStatus.IGNORE)
    public static Boolean getClosed(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return !webWindow.getWebClient().containsWebWindow(webWindow);
    }

    /**
     * Returns the {@code devicePixelRatio} property.
     * @param self self
     * @return the {@code devicePixelRatio} property
     */
    @Getter
    public static int getDevicePixelRatio(final Object self) {
        return 1;
    }

    /**
     * Does nothing.
     * @param self self
     * @param x the horizontal position
     * @param y the vertical position
     */
    @Function
    public static void moveTo(final Object self, final int x, final int y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.moveTo() not implemented");
        }
    }

    /**
     * Does nothing.
     * @param self self
     * @param x the horizontal position
     * @param y the vertical position
     */
    @Function
    public static void moveBy(final Object self, final int x, final int y) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.moveBy() not implemented");
        }
    }

    /**
     * Does nothing.
     * @param self self
     * @param width the width offset
     * @param height the height offset
     */
    @Function
    public static void resizeBy(final Object self, final int width, final int height) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.resizeBy() not implemented");
        }
    }

    /**
     * Does nothing.
     * @param self self
     * @param width the width of the Window in pixel after resize
     * @param height the height of the Window in pixel after resize
     */
    @Function
    public static void resizeTo(final Object self, final int width, final int height) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("window.resizeTo() not implemented");
        }
    }

    /**
     * Should implement the stop() function on the window object.
     * (currently empty implementation)
     * @param self self
     * @see <a href="https://developer.mozilla.org/en/DOM/window.stop">window.stop</a>
     */
    @Function({CHROME, FF})
    public static void stop(final Object self) {
        //empty
    }

    /**
     * Returns the {@code offscreenBuffering} property.
     * @param self self
     * @return the {@code offscreenBuffering} property
     */
    @Getter({CHROME, IE})
    public static Object getOffscreenBuffering(final Object self) {
        if (getWindow(self).getBrowserVersion().hasFeature(JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID)) {
            return "auto";
        }
        return true;
    }

    /**
     * Loads the new HTML document corresponding to the specified URL.
     * @param self self
     * @param url the location of the new HTML document to load
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536638%28VS.85%29.aspx">MSDN Documentation</a>
     */
    @Function(IE)
    public static void navigate(final Object self, final String url) throws IOException {
        getLocation(self).assign(url);
    }

    /**
     * Sets a chunk of JavaScript to be invoked each time a specified number of milliseconds has elapsed.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536749.aspx">MSDN documentation</a>
     * @param self self
     * @param code specifies the function pointer or string that indicates the code to be executed
     *        when the specified interval has elapsed
     * @param timeout specifies the number of milliseconds
     * @param language specifies language
     * @return the id of the created interval
     */
    @Function
    public static int setInterval(final Object self, final Object code, int timeout, final Object language) {
        if (timeout < MIN_TIMER_DELAY) {
            timeout = MIN_TIMER_DELAY;
        }
        final Window2 window = getWindow(self);
        final int id;

        final WebWindow w = window.getWebWindow();
        final Page page = (Page) window.getDomNodeOrNull();
        final String description = "window.setInterval(" + timeout + ")";
        if (code == null) {
            throw new RuntimeException("Function not provided.");
        }
        else if (code instanceof String) {
            final String s = (String) code;
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                createJavaScriptJob(timeout, Integer.valueOf(timeout), description, w, s);
            id = w.getJobManager().addJob(job, page);
        }
        else if (code instanceof ScriptFunction) {
            final ScriptFunction f = (ScriptFunction) code;
            final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                createJavaScriptJob(timeout, Integer.valueOf(timeout), description, w, f);
            id = w.getJobManager().addJob(job, page);
        }
        else {
            throw new RuntimeException("Unknown type for function.");
        }
        return id;
    }

    /**
     * Cancels the interval previously started using the {@link #setInterval(Object, Object, int, Object)} method.
     * Current implementation does nothing.
     * @param self self
     * @param intervalID specifies the interval to cancel as returned by the
     *        {@link #setInterval(Object, Object, int, Object)} method
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536353.aspx">MSDN documentation</a>
     */
    @Function
    public static void clearInterval(final Object self, final int intervalID) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearInterval(" + intervalID + ")");
        }
        getWindow(self).getWebWindow().getJobManager().removeJob(intervalID);
    }

    /**
     * Returns the global.
     * @return the global
     */
    public Global getGlobal() {
        return NashornJavaScriptEngine.getGlobal(getWebWindow());
    }

    /**
     * Returns the current selection.
     * @param self self
     * @return the current selection
     */
    @Function
    public static Selection2 getSelection(final Object self) {
        final Window2 window = getWindow(self);
        final WebWindow webWindow = window.getWebWindow();
        // return null if the window is in a frame that is not displayed
        if (webWindow instanceof FrameWindow) {
            final FrameWindow frameWindow = (FrameWindow) webWindow;
            if (window.getBrowserVersion().hasFeature(JS_WINDOW_SELECTION_NULL_IF_INVISIBLE)
                    && !frameWindow.getFrameElement().isDisplayed()) {
                return null;
            }
        }
        return window.getSelectionImpl();
    }

    /**
     * Returns the current selection.
     * @return the current selection
     */
    public Selection2 getSelectionImpl() {
        if (selection_ == null) {
            selection_ = Selection2.constructor(true, getGlobal());
        }
        return selection_;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Window2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("Window",
                    staticHandle("constructor", Window2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
            ScriptUtils.initialize(this);
        }

        /**
         * @return {@link Window2#TEMPORARY}.
         */
        public int G$TEMPORARY() {
            return TEMPORARY;
        }

        /**
         * @return {@link Window2#PERSISTENT}.
         */
        public int G$PERSISTENT() {
            return PERSISTENT;
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        /** Constructor. */
        public Prototype() {
            super("Window");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("Window");
        }
    }

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
     * Clears the computed styles for a specific {@link Element}.
     * @param element the element to clear its cache
     */
    public void clearComputedStyles(final Element element) {
        synchronized (computedStyles_) {
            computedStyles_.remove(element);
        }
    }
}

@ScriptClass
class HTMLCollectionFrames2 extends HTMLCollection2 {
    private static final Log LOG = LogFactory.getLog(HTMLCollectionFrames2.class);

    HTMLCollectionFrames2(final HtmlPage page) {
        super(page, false);
    }

    @Override
    protected boolean isMatching(final DomNode node) {
        return node instanceof BaseFrameElement;
    }

    @Override
    protected ScriptObject getScriptObjectForElement(final Object obj) {
        final WebWindow window;
        if (obj instanceof BaseFrameElement) {
            window = ((BaseFrameElement) obj).getEnclosedWindow();
        }
        else {
            window = ((FrameWindow) obj).getFrameElement().getEnclosedWindow();
        }

        return window.getScriptableObject();
    }

//    @Override
//    protected void addElementIds(final List<String> idList, final List<Object> elements) {
//        for (final Object next : elements) {
//            final BaseFrameElement frameElt = (BaseFrameElement) next;
//            final WebWindow window = frameElt.getEnclosedWindow();
//            final String windowName = window.getName();
//            if (windowName != null) {
//                idList.add(windowName);
//            }
//        }
//    }
}
