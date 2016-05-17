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
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.DialogWindow;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement2;
import com.gargoylesoftware.htmlunit.svg.SvgPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.js.internal.dynalink.CallSiteDescriptor;
import com.gargoylesoftware.js.internal.dynalink.linker.GuardedInvocation;
import com.gargoylesoftware.js.internal.dynalink.linker.LinkRequest;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Attribute;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.ECMAErrors;
import com.gargoylesoftware.js.nashorn.internal.runtime.FindProperty;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.Undefined;
import com.gargoylesoftware.js.nashorn.internal.runtime.linker.NashornGuards;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

public class Window2 extends EventTarget2 {

    private static final Log LOG = LogFactory.getLog(Window2.class);

    /**
     * The minimum delay that can be used with setInterval() or setTimeout(). Browser minimums are
     * usually in the 10ms to 15ms range, but there's really no reason for us to waste that much time.
     * http://jsninja.com/Timers#Minimum_Timer_Delay_and_Reliability
     */
    private static final int MIN_TIMER_DELAY = 1;

    private Screen2 screen_;
    private Document2 document_;
    private History2 history_;
    private ScriptObject console_;
    private Location2 location_;
    private HTMLCollection2 frames_; // has to be a member to have equality (==) working
    private Event2 currentEvent_;
    private Object controllers_ = new SimpleScriptObject();
    private Object opener_;
    private Object top_;

    /**
     * Cache computed styles when possible, because their calculation is very expensive.
     * We use a weak hash map because we don't want this cache to be the only reason
     * nodes are kept around in the JVM, if all other references to them are gone.
     */
    private transient WeakHashMap<Element2, Map<String, ComputedCSSStyleDeclaration2>> computedStyles_ = new WeakHashMap<>();

    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR, value = @WebBrowser(CHROME))
    public static final int TEMPORARY = 0;

    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property(attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR, value = @WebBrowser(CHROME))
    public static final int PERSISTENT = 1;

    /**
     * Initialize the object.
     * @param enclosedPage the page containing the JavaScript
     */
    public void initialize(final Page enclosedPage) {
        final Global global = NashornJavaScriptEngine.getGlobal(enclosedPage.getEnclosingWindow().getScriptContext());
        if (enclosedPage instanceof XmlPage || enclosedPage instanceof SvgPage) {
//            document_ = new XMLDocument2();
        }
        else {
            document_ = HTMLDocument2.constructor(true, global);
        }
        document_.setWindow(this);


        if (enclosedPage != null && enclosedPage.isHtmlPage()) {
            final HtmlPage htmlPage = (HtmlPage) enclosedPage;

            // Windows don't have corresponding DomNodes so set the domNode
            // variable to be the page. If this isn't set then SimpleScriptable.get()
            // won't work properly
            setDomNode(htmlPage);
//            clearEventListenersContainer();

            document_.setDomNode(htmlPage);

            final WebWindow webWindow = getWebWindow();

            console_ = Console2.constructor(true, global);
            ((Console2) console_).setWebWindow(webWindow);

            location_ = Location2.constructor(true, global);
            location_.initialize(this);

            if (webWindow instanceof TopLevelWindow) {
                final WebWindow opener = ((TopLevelWindow) webWindow).getOpener();
                if (opener != null) {
                    opener_ = opener.getScriptObject2();
                }
            }
        }
    }

    public static Window2 constructor(final boolean newObj, final Object self) {
        final Window2 host = new Window2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    @Function
    public static void alert(final Object self, final Object o) {
        final AlertHandler handler = getWindow(self).getWebWindow().getWebClient().getAlertHandler();
        if (handler == null) {
            LOG.warn("window.alert(\"" + o + "\") no alert handler installed");
        }
        else {
//            handler.handleAlert(document_.getPage(), stringMessage);
          handler.handleAlert(null, o.toString());
        }
    }

    /**
     * Returns the WebWindow associated with this Window.
     * @return the WebWindow
     */
    public WebWindow getWebWindow() {
        return ((HtmlPage) getDomNodeOrDie()).getEnclosingWindow();
    }

    @Getter
    public static int getInnerHeight(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return webWindow.getInnerHeight();
    }

    @Getter
    public static int getInnerWidth(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return webWindow.getInnerWidth();
    }

    @Getter
    public static int getOuterHeight(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return webWindow.getOuterHeight();
    }

    @Getter
    public static int getOuterWidth(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        return webWindow.getOuterWidth();
    }

    @Getter
    public static Object getTop(final Object self) {
        final WebWindow webWindow = getWindow(self).getWebWindow();
        final WebWindow top = webWindow.getTopWindow();
        return top.getScriptObject2();
    }

    @Getter(@WebBrowser(FF))
    public static Object getControllers(final Object self) {
        return getWindow(self).controllers_;
    }

    @Setter(@WebBrowser(FF))
    public static void setControllers(final Object self, final Object value) {
        getWindow(self).controllers_ = value;
    }

    private Object getHandlerForJavaScript(final String eventName) {
        return getEventListenersContainer().getEventHandlerProp(eventName);
    }

    private void setHandlerForJavaScript(final String eventName, final Object handler) {
        if (handler == null || handler instanceof ScriptFunction) {
            getEventListenersContainer().setEventHandlerProp(eventName, handler);
        }
        // Otherwise, fail silently.
    }

    @Getter
    public static Object getOnload(final Object self) {
        final Window2 window = getWindow(self);
        final Object onload = window.getHandlerForJavaScript("load");
        if (onload == null) {
            // NB: for IE, the onload of window is the one of the body element but not for Mozilla.
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

    @Setter
    public static void setOnload(final Object self, final Object newOnload) {
        final Window2 window = getWindow(self);
//        if (window.getBrowserVersion().hasFeature(EVENT_ONLOAD_UNDEFINED_THROWS_ERROR)
//                && Context.getUndefinedValue().equals(newOnload)) {
//                throw Context.reportRuntimeError("Invalid onload value: undefined.");
//            }
        window.getEventListenersContainer().setEventHandlerProp("load", newOnload);
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    @Function({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public static String btoa(final Object self, final String stringToEncode) {
        return new String(Base64.encodeBase64(stringToEncode.getBytes()));
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding..
     * @param encodedData the encoded string
     * @return the decoded value
     */
    @Function({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public static String atob(final Object self, final String encodedData) {
        return new String(Base64.decodeBase64(encodedData.getBytes()));
    }

    /**
     * Executes the specified script code as long as the language is {@code JavaScript} or {@code JScript}.
     * @param script the script code to execute
     * @param language the language of the specified code ({@code JavaScript} or {@code JScript})
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536420.aspx">MSDN documentation</a>
     */
    @Function(@WebBrowser(value = IE, maxVersion = 8))
    public void execScript(final String script, final Object language) {
        final String languageStr = ScriptRuntime.safeToString(language);
        if (language == Undefined.getUndefined()
            || "javascript".equalsIgnoreCase(languageStr) || "jscript".equalsIgnoreCase(languageStr)) {
            final Global global = Context.getGlobal();
            Context.getContext().eval(global, script, global, null);
        }
        else if ("vbscript".equalsIgnoreCase(languageStr)) {
            throw ECMAErrors.typeError("VBScript not supported in Window.execScript().");
        }
        else {
            throw ECMAErrors.typeError("Invalid class string");
        }
    }

    /**
     * An undocumented IE function.
     */
    @Function(@WebBrowser(IE))
    public void CollectGarbage() {
        // Empty.
    }

    /**
     * Returns the JavaScript property {@code document}.
     * @return the document
     */
    @Getter
    public static Document2 getDocument(final Object self) {
        return getWindow(self).document_;
    }

    private static Window2 getWindow(final Object self) {
        if (self instanceof Global) {
            return ((Global) self).getWindow();
        }
        if (self instanceof Window2) {
            return (Window2) self;
        }
        return Global.instance().getWindow();
    }
    
    /**
     * Returns the value of the window's {@code name} property.
     * @return the value of the window's {@code name} property
     */
    @Getter
    public String getName() {
        return getWebWindow().getName();
    }

    /**
    * Sets the value of the window's {@code name} property.
    * @param name the value of the window's {@code name} property
    */
   @Setter
   public void setName(final Object name) {
       getWebWindow().setName(name.toString());
   }

    /**
     * Returns the {@code history} property.
     * @return the {@code history} property
     */
    @Getter
    public static History2 getHistory(final Object self) {
        final Window2 window = getWindow(self);
        if (window.history_ == null) {
            final Global global = NashornJavaScriptEngine.getGlobal(window.getWebWindow().getScriptContext());
            window.history_ = History2.constructor(true, global);
        }
        return window.history_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FindProperty findProperty(final String key, final boolean deep, final ScriptObject start) {
        FindProperty prop = super.findProperty(key, deep, start);
        if (prop == null && getDomNodeOrNull() != null) {
            final Global global = NashornJavaScriptEngine.getGlobal(getWebWindow().getScriptContext());
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
        final MethodHandle mh = virtualHandle("getArbitraryProperty", Object.class, String.class);
        final boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(desc, request);
        return new GuardedInvocation(MethodHandles.insertArguments(mh, 1, name),
                NashornGuards.getMapGuard(getMap(), explicitInstanceOfCheck),
                getProtoSwitchPoints(name, null),
                explicitInstanceOfCheck ? null : ClassCastException.class);
    }

    @SuppressWarnings("unused")
    private Object getArbitraryProperty(final String name) {
        final HtmlPage page = (HtmlPage) getDomNodeOrDie();
        Object object = getFrameWindowByName(page, name);
        if (object == null) {
            object = Undefined.getUndefined();
        }
        return object;
    }

    private static Object getFrameWindowByName(final HtmlPage page, final String name) {
        try {
            return page.getFrameByName(name).getScriptObject2();
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
     * @return the number of frames contained by this window
     */
    @Getter
    public static int getLength(final Object self) {
        final Window2 window = (Window2) self;
        return (int) window.getFrames2().getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(int key) {
        final HTMLCollection2 frames = getFrames2();
        if (key >= (int) frames.getLength()) {
            return Undefined.getUndefined();
        }
        return frames.item(Integer.valueOf(key));
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
    @Function({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public boolean find(final String search, final boolean caseSensitive,
            final boolean backwards, final boolean wrapAround,
            final boolean wholeWord, final boolean searchInFrames, final boolean showDialog) {
        return false;
    }

    /**
     * Returns the {@code frames} property.
     * @return the {@code frames} property
     */
    @Getter
    public Window2 getFrames() {
        return this;
    }

    /**
     * Returns the {@code self} property.
     * @return the {@code self} property
     */
    @Getter
    public Window2 getSelf() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dispatchEvent(final Event2 event) {
        event.setTarget(this);
        final ScriptResult result = fireEvent(event);
        return !event.isAborted(result);
    }

    /**
     * Returns the live collection of frames contained by this window.
     * @return the live collection of frames contained by this window
     */
    private HTMLCollection2 getFrames2() {
        if (frames_ == null) {
            final HtmlPage page = (HtmlPage) getDomNodeOrDie();
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
        final Global global = NashornJavaScriptEngine.getGlobal(window2.getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = new ComputedCSSStyleDeclaration2(global, original);

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

        return style;
    }

    /**
     * Returns the {@code screen} property.
     * @return the screen property
     */
    @Getter
    public Screen2 getScreen() {
        return screen_;
    }

    /**
     * Returns the {@code location} property.
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
        final Object o = getOnerror();
        if (o instanceof ScriptFunction) {
            final ScriptFunction f = (ScriptFunction) o;
            final Global global = NashornJavaScriptEngine.getGlobal(getWebWindow().getScriptContext());

            final String msg = e.getMessage();
            final String url = e.getPage().getUrl().toExternalForm();
            final int line = e.getFailingLineNumber();
            final int column = e.getFailingColumnNumber();

            ScriptRuntime.apply(f, global,
                    msg, url, Integer.valueOf(line), Integer.valueOf(column), e);
        }
    }

    /**
     * Returns the value of the window's {@code onerror} property.
     * @return the value of the window's {@code onerror} property
     */
    @Getter
    public Object getOnerror() {
        return getHandlerForJavaScript(Event2.TYPE_ERROR);
    }

    /**
     * Sets the value of the window's {@code onerror} property.
     * @param onerror the value of the window's {@code onerror} property
     */
    @Setter
    public void setOnerror(final Object onerror) {
        setHandlerForJavaScript(Event2.TYPE_ERROR, onerror);
    }

    /**
     * Sets the value of the {@code onload} event handler.
     * @param onload the new handler
     */
    @Setter
    public void setOnload(final Object onload) {
        getEventListenersContainer().setEventHandlerProp("load", onload);
    }

    /**
     * Returns the {@code onclick} property (not necessary a function if something else has been set).
     * @return the {@code onclick} property
     */
    @Getter
    public Object getOnclick() {
        return getHandlerForJavaScript("click");
    }

    /**
     * Sets the value of the {@code onclick} event handler.
     * @param onclick the new handler
     */
    @Setter
    public void setOnclick(final Object onclick) {
        setHandlerForJavaScript("click", onclick);
    }

    /**
     * Returns the {@code ondblclick} property (not necessary a function if something else has been set).
     * @return the {@code ondblclick} property
     */
    @Getter
    public Object getOndblclick() {
        return getHandlerForJavaScript("dblclick");
    }

    /**
     * Sets the value of the {@code ondblclick} event handler.
     * @param ondblclick the new handler
     */
    @Setter
    public void setOndblclick(final Object ondblclick) {
        setHandlerForJavaScript("dblclick", ondblclick);
    }

    /**
     * Returns the {@code onhashchange} property (not necessary a function if something else has been set).
     * @return the {@code onhashchange} property
     */
    @Getter
    public Object getOnhashchange() {
        return getHandlerForJavaScript(Event2.TYPE_HASH_CHANGE);
    }

    /**
     * Sets the value of the {@code onhashchange} event handler.
     * @param onhashchange the new handler
     */
    @Setter
    public void setOnhashchange(final Object onhashchange) {
        setHandlerForJavaScript(Event2.TYPE_HASH_CHANGE, onhashchange);
    }

    /**
     * Returns the value of the window's {@code onbeforeunload} property.
     * @return the value of the window's {@code onbeforeunload} property
     */
    @Getter
    public Object getOnbeforeunload() {
        return getHandlerForJavaScript(Event2.TYPE_BEFORE_UNLOAD);
    }

    /**
     * Sets the value of the window's {@code onbeforeunload} property.
     * @param onbeforeunload the value of the window's {@code onbeforeunload} property
     */
    @Setter
    public void setOnbeforeunload(final Object onbeforeunload) {
        setHandlerForJavaScript(Event2.TYPE_BEFORE_UNLOAD, onbeforeunload);
    }

    /**
     * Getter for the {@code onchange} event handler.
     * @return the handler
     */
    @Getter
    public Object getOnchange() {
        return getHandlerForJavaScript(Event2.TYPE_CHANGE);
    }

    /**
     * Setter for the {@code onchange} event handler.
     * @param onchange the handler
     */
    @Setter
    public void setOnchange(final Object onchange) {
        setHandlerForJavaScript(Event2.TYPE_CHANGE, onchange);
    }

    /**
     * Getter for the {@code onsubmit} event handler.
     * @return the handler
     */
    @Getter
    public Object getOnsubmit() {
        return getHandlerForJavaScript(Event2.TYPE_SUBMIT);
    }

    /**
     * Setter for the {@code onsubmit} event handler.
     * @param onsubmit the handler
     */
    @Setter
    public void setOnsubmit(final Object onsubmit) {
        setHandlerForJavaScript(Event2.TYPE_SUBMIT, onsubmit);
    }

    /**
     * Returns the current event.
     * @return the current event, or {@code null} if no event is currently available
     */
    @Getter({@WebBrowser(IE), @WebBrowser(CHROME)})
    public Object getEvent() {
        return currentEvent_;
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
    public ScriptObject open(final Object url, final Object name, final Object features,
            final Object replace) {
        String urlString = null;
        if (url != Undefined.getUndefined()) {
            urlString = url.toString();
        }
        String windowName = "";
        if (name != Undefined.getUndefined()) {
            windowName = name.toString();
        }
        String featuresString = null;
        if (features != Undefined.getUndefined()) {
            featuresString = features.toString();
        }
        final WebClient webClient = getWebWindow().getWebClient();

        if (webClient.getOptions().isPopupBlockerEnabled()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignoring window.open() invocation because popups are blocked.");
            }
            return null;
        }

        boolean replaceCurrentEntryInBrowsingHistory = false;
        if (replace != Undefined.getUndefined()) {
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
                final WebWindow webWindow = webClient.getWebWindowByName(windowName);
                return webWindow.getScriptObject2();
            }
            catch (final WebWindowNotFoundException e) {
                // nothing
            }
        }
        final URL newUrl = makeUrlForOpenWindow(urlString);
        final WebWindow newWebWindow = webClient.openWindow(newUrl, windowName, getWebWindow());
        return newWebWindow.getScriptObject2();
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
     * @param newValue the new value
     */
    @Setter
    public void setOpener(final Object newValue) {
        if (getBrowserVersion().hasFeature(JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT)
            && newValue != null && newValue != Undefined.getUndefined() && !(newValue instanceof Window2)) {
            throw new RuntimeException("Can't set opener to something other than a window!");
        }
        opener_ = newValue;
    }

    /**
     * Returns the value of the {@code opener} property.
     * @return the value of the {@code opener}, or {@code null} for a top level window
     */
    @Getter
    public Object getOpener() {
        return opener_;
    }

    /**
     * Scrolls to the specified location on the page.
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    @Function
    public void scroll(final int x, final int y) {
        scrollTo(x, y);
    }

    /**
     * Scrolls the window content the specified distance.
     * @param x the horizontal distance to scroll by
     * @param y the vertical distance to scroll by
     */
    @Function
    public void scrollBy(final int x, final int y) {
        final HTMLElement2 body = ((HTMLDocument2) document_).getBody();
        if (body != null) {
            body.setScrollLeft(body.getScrollLeft() + x);
            body.setScrollTop(body.getScrollTop() + y);
        }
    }

    /**
     * Returns the value of {@code pageXOffset} property.
     * @return the value of {@code pageXOffset} property
     */
    @Getter
    public int getPageXOffset() {
        return 0;
    }

    /**
     * Returns the value of {@code pageYOffset} property.
     * @return the value of {@code pageYOffset} property
     */
    @Getter
    public int getPageYOffset() {
        return 0;
    }

    /**
     * Returns the value of {@code scrollX} property.
     * @return the value of {@code scrollX} property
     */
    @Getter({@WebBrowser(FF), @WebBrowser(CHROME)})
    public int getScrollX() {
        return 0;
    }

    /**
     * Returns the value of {@code scrollY} property.
     * @return the value of {@code scrollY} property
     */
    @Getter({@WebBrowser(FF), @WebBrowser(CHROME)})
    public int getScrollY() {
        return 0;
    }

    /**
     * Posts a message.
     * @param message the object passed to the window
     * @param targetOrigin the origin this window must be for the event to be dispatched
     * @param transfer an optional sequence of Transferable objects
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window.postMessage">MDN documentation</a>
     */
    @Function
    public void postMessage(final String message, final String targetOrigin, final Object transfer) {
        final URL currentURL = getWebWindow().getEnclosedPage().getUrl();

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
        final Global global = NashornJavaScriptEngine.getGlobal(getWebWindow().getScriptContext());
        final MessageEvent2 event = MessageEvent2.constructor(true, global);
        final String origin = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();
        event.initMessageEvent(Event2.TYPE_MESSAGE, false, false, message, origin, "", this, transfer);

        final NashornJavaScriptEngine jsEngine = getWebWindow().getWebClient().getJavaScriptEngine2();
        final PostponedAction action = new PostponedAction(getDomNodeOrDie().getPage()) {
            @Override
            public void execute() throws Exception {
                dispatchEvent(event);
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
     * @param callback the function to call when it's time to update the animation
     * @return an identification id
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window/requestAnimationFrame">MDN Doc</a>
     */
    @Function
    public int requestAnimationFrame(final Object callback) {
        // nothing for now
        return 1;
    }

    /**
     * Dummy implementation for {@code cancelAnimationFrame}.
     * @param requestId the ID value returned by the call to window.requestAnimationFrame()
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Window/cancelAnimationFrame">MDN Doc</a>
     */
    @Function
    public void cancelAnimationFrame(final Object requestId) {
        // nothing for now
    }

    /**
     * Scrolls to the specified location on the page.
     * @param x the horizontal position to scroll to
     * @param y the vertical position to scroll to
     */
    @Function
    public void scrollTo(final int x, final int y) {
        final HTMLElement2 body = ((HTMLDocument2) document_).getBody();
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
     * Cancels a time-out previously set with the {@link #setTimeout(Object, int, Object)} method.
     *
     * @param self this object
     * @param timeoutId identifier for the timeout to clear (returned by {@link #setTimeout(Object, int, Object)})
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
     * @return the {@code console} property
     */
    @Getter
    public ScriptObject getConsole() {
        return console_;
    }

    /**
     * Sets the {@code console}.
     * @param console the console
     */
    @Setter
    public void setConsole(final ScriptObject console) {
        console_ = console;
    }

    /**
     * Prints messages to the {@code console}.
     * @param message the message to log
     */
    @Function(@WebBrowser(FF))
    public void dump(final String message) {
        if (console_ instanceof Console2) {
            Console2.log(null, console_, new Object[] {message}, null);
        }
    }

    /**
     * Returns the value of {@code mozInnerScreenX} property.
     * @return the value of {@code mozInnerScreenX} property
     */
    @Getter(@WebBrowser(FF))
    public int getMozInnerScreenX() {
        return 11;
    }

    /**
     * Returns the value of {@code mozInnerScreenY} property.
     * @return the value of {@code mozInnerScreenY} property
     */
    @Getter(@WebBrowser(FF))
    public int getMozInnerScreenY() {
        return 91;
    }

    /**
     * Returns the value of {@code mozPaintCount} property.
     * @return the value of {@code mozPaintCount} property
     */
    @Getter(@WebBrowser(FF))
    public int getMozPaintCount() {
        return 0;
    }

    /**
     * Scrolls the window content down by the specified number of lines.
     * @param lines the number of lines to scroll down
     */
    @Function(@WebBrowser(FF))
    public void scrollByLines(final int lines) {
        final HTMLElement2 body = ((HTMLDocument2) document_).getBody();
        if (body != null) {
            body.setScrollTop(body.getScrollTop() + (19 * lines));
        }
    }

    /**
     * Scrolls the window content down by the specified number of pages.
     * @param pages the number of pages to scroll down
     */
    @Function(@WebBrowser(FF))
    public void scrollByPages(final int pages) {
        final HTMLElement2 body = ((HTMLDocument2) document_).getBody();
        if (body != null) {
            body.setScrollTop(body.getScrollTop() + (getInnerHeight() * pages));
        }
    }

    /**
     * Returns the {@code innerHeight}.
     * @return the {@code innerHeight}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref27.html">Mozilla doc</a>
     */
    @Getter
    public int getInnerHeight() {
        return getWebWindow().getInnerHeight();
    }

    /**
     * Returns the {@code outerHeight}.
     * @return the {@code outerHeight}
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref78.html">Mozilla doc</a>
     */
    @Getter
    public int getOuterHeight() {
        return getWebWindow().getOuterHeight();
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
    @Function({@WebBrowser(IE), @WebBrowser(FF)})
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
            throw new RuntimeException(e);
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
    @Function(@WebBrowser(IE))
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the name of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/efy5bay1.aspx">MSDN doc</a>
     * @return "JScript"
     */
    @Function(@WebBrowser(IE))
    public static String ScriptEngine(final Object self) {
        return "JScript";
    }

    /**
     * Gets the build version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/yftk84kt.aspx">MSDN doc</a>
     * @return the build version
     */
    @Function(@WebBrowser(IE))
    public static int ScriptEngineBuildVersion(final Object self) {
        return 12345;
    }

    /**
     * Gets the major version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/x7cbaet3.aspx">MSDN doc</a>
     * @return the major version
     */
    @Function(@WebBrowser(IE))
    public static int ScriptEngineMajorVersion(final Object self) {
        return getWindow(self).getBrowserVersion().getBrowserVersionNumeric();
    }

    /**
     * Gets the minor version of the scripting engine.
     * @see <a href="http://msdn.microsoft.com/en-us/library/wzaz8hhz.aspx">MSDN doc</a>
     * @return the minor version
     */
    @Function(@WebBrowser(IE))
    public static int ScriptEngineMinorVersion(final Object self) {
        return 0;
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

    private static MethodHandle virtualHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findVirtual(Window2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("Window", 
                    staticHandle("constructor", Window2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
            ScriptUtils.initialize(this);
        }
 
        public int G$TEMPORARY() {
            return TEMPORARY;
        }

        public int G$PERSISTENT() {
            return PERSISTENT;
        }
    }

    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "Window";
        }
    }

    public static final class ObjectConstructor extends SimpleObjectConstructor {
        public ObjectConstructor() {
            ScriptUtils.initialize(this);
        }

        public Object getDefaultValue(final Class<?> typeHint) {
            return "[object Window]";
        }

        public String getClassName() {
            return "Window";
        }
    }
}

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

        return window.getScriptObject2();
    }

//    @Override
//    protected Object getWithPreemption(final String name) {
//        final List<Object> elements = getElements();
//
//        for (final Object next : elements) {
//            final BaseFrameElement frameElt = (BaseFrameElement) next;
//            final WebWindow window = frameElt.getEnclosedWindow();
//            if (name.equals(window.getName())) {
//                if (LOG.isDebugEnabled()) {
//                    LOG.debug("Property \"" + name + "\" evaluated (by name) to " + window);
//                }
//                return getScriptableForElement(window);
//            }
//            if (getBrowserVersion().hasFeature(JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID)
//                    && frameElt.getAttribute("id").equals(name)) {
//                if (LOG.isDebugEnabled()) {
//                    LOG.debug("Property \"" + name + "\" evaluated (by id) to " + window);
//                }
//                return getScriptableForElement(window);
//            }
//        }
//
//        return NOT_FOUND;
//    }

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
