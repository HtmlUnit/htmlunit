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

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet2;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document2;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.svg.SvgPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.js.internal.dynalink.CallSiteDescriptor;
import com.gargoylesoftware.js.internal.dynalink.linker.GuardedInvocation;
import com.gargoylesoftware.js.internal.dynalink.linker.LinkRequest;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Attribute;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.ECMAErrors;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.Undefined;

public class Window2 extends EventTarget2 {

    private static final Log LOG = LogFactory.getLog(Window2.class);

    private Screen2 screen_;
    private Document2 document_;
    private History2 history_;
    private HTMLCollection2 frames_; // has to be a member to have equality (==) working
    private Object controllers_ = new SimpleScriptObject();

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
        if (enclosedPage instanceof XmlPage || enclosedPage instanceof SvgPage) {
//            document_ = new XMLDocument2();
        }
        else {
            document_ = HTMLDocument2.constructor(true, null);
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
        }
    }

    public static Window2 constructor(final boolean newObj, final Object self) {
        final Window2 host = new Window2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
    }

    @Function
    public static void alert(final Object self, final Object o) {
        final AlertHandler handler = ((WebWindow) ((Global) self).getDomObject()).getWebClient().getAlertHandler();
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
        final WebWindow webWindow = ((Global) self).getDomObject();
        return webWindow.getInnerHeight();
    }

    @Getter
    public static int getInnerWidth(final Object self) {
        final WebWindow webWindow = ((Global) self).getDomObject();
        return webWindow.getInnerWidth();
    }

    @Getter
    public static int getOuterHeight(final Object self) {
        final WebWindow webWindow = ((Global) self).getDomObject();
        return webWindow.getOuterHeight();
    }

    @Getter
    public static int getOuterWidth(final Object self) {
        final WebWindow webWindow = ((Global) self).getDomObject();
        return webWindow.getOuterWidth();
    }

    @Getter
    public static Object getTop(final Object self) {
        final WebWindow webWindow = ((Global) self).getDomObject();
        final WebWindow top = webWindow.getTopWindow();
        return top.getScriptObject2();
    }

    @Getter(@WebBrowser(FF))
    public static Object getControllers(final Object self) {
        return ((Global) self).<Window2>getWindow().controllers_;
    }

    @Setter(@WebBrowser(FF))
    public static void setControllers(final Object self, final Object value) {
        ((Global) self).<Window2>getWindow().controllers_ = value;
    }

    private Object getHandlerForJavaScript(final String eventName) {
        return getEventListenersContainer().getEventHandlerProp(eventName);
    }

    @Getter
    public static Object getOnload(final Object self) {
        final Window2 window = ((Global) self).getWindow();
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
        final Window2 window = ((Global) self).getWindow();
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
        final Window2 window = ((Global) self).getWindow();
        return window.document_;
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
     * Returns the {@code history} property.
     * @return the {@code history} property
     */
    @Getter
    public static History2 getHistory(final Object self) {
        final Window2 window = (Window2) self;
        if (window.history_ == null) {
            window.history_ = History2.constructor(true, self);
        }
        return window.history_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuardedInvocation noSuchProperty(final CallSiteDescriptor desc, final LinkRequest request) {
        final String name = desc.getNameToken(CallSiteDescriptor.NAME_OPERAND);
        final MethodHandle mh = virtualHandle("getArbitraryProperty", Object.class, String.class);
        return new GuardedInvocation(MethodHandles.insertArguments(mh, 1, name));
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
     * @param element the element
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null})
     * @return the computed style
     */
    @Function
    public ComputedCSSStyleDeclaration2 getComputedStyle(final Element2 element, final String pseudoElement) {
        synchronized (computedStyles_) {
            final Map<String, ComputedCSSStyleDeclaration2> elementMap = computedStyles_.get(element);
            if (elementMap != null) {
                final ComputedCSSStyleDeclaration2 style = elementMap.get(pseudoElement);
                if (style != null) {
                    return style;
                }
            }
        }

        final CSSStyleDeclaration2 original = element.getStyle();
        final ComputedCSSStyleDeclaration2 style = new ComputedCSSStyleDeclaration2(original);

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

        synchronized (computedStyles_) {
            Map<String, ComputedCSSStyleDeclaration2> elementMap = computedStyles_.get(element);
            if (elementMap == null) {
                elementMap = new WeakHashMap<>();
                computedStyles_.put(element, elementMap);
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

    public static final class Prototype extends PrototypeObject {
        public ScriptFunction alert;
        public ScriptFunction atob;
        public ScriptFunction btoa;
        public ScriptFunction CollectGarbage;
        public ScriptFunction find;
        public ScriptFunction getComputedStyle;

        public ScriptFunction G$alert() {
            return alert;
        }

        public void S$alert(final ScriptFunction function) {
            this.alert = function;
        }

        public ScriptFunction G$atob() {
            return atob;
        }

        public void S$atob(final ScriptFunction function) {
            this.atob = function;
        }

        public ScriptFunction G$btoa() {
            return btoa;
        }

        public void S$btoa(final ScriptFunction function) {
            this.btoa = function;
        }

        public ScriptFunction G$CollectGarbage() {
            return CollectGarbage;
        }

        public void S$CollectGarbage(final ScriptFunction function) {
            this.CollectGarbage = function;
        }

        public ScriptFunction G$find() {
            return find;
        }

        public void S$find(final ScriptFunction function) {
            this.find = function;
        }

        public ScriptFunction G$getComputedStyle() {
            return getComputedStyle;
        }

        public void S$getComputedStyle(final ScriptFunction function) {
            this.getComputedStyle = function;
        }

        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "Window";
        }
    }

    public static final class ObjectConstructor extends ScriptObject {
        public ScriptFunction alert;
        public ScriptFunction atob;
        public ScriptFunction btoa;
        public ScriptFunction execScript;
        public ScriptFunction CollectGarbage;
        

        public ScriptFunction G$alert() {
            return this.alert;
        }

        public void S$alert(final ScriptFunction function) {
            this.alert = function;
        }

        public ScriptFunction G$atob() {
            return atob;
        }

        public void S$atob(final ScriptFunction function) {
            this.atob = function;
        }

        public ScriptFunction G$btoa() {
            return btoa;
        }

        public void S$btoa(final ScriptFunction function) {
            this.btoa = function;
        }

        public ScriptFunction G$execScript() {
            return execScript;
        }

        public void S$execScript(final ScriptFunction function) {
            this.execScript = function;
        }

        public ScriptFunction G$CollectGarbage() {
            return CollectGarbage;
        }

        public void S$CollectGarbage(final ScriptFunction function) {
            this.CollectGarbage = function;
        }

        public ObjectConstructor() {
            ScriptUtils.initialize(this);
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
