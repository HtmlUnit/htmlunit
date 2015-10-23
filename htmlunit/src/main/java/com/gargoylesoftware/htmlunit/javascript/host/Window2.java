/*
 * Copyright (c) 2015 Gargoyle Software Inc.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (http://www.gnu.org/licenses/).
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.svg.SvgPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.ECMAErrors;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.Undefined;

public class Window2 extends EventTarget2 {

    private static final Log LOG = LogFactory.getLog(Window2.class);

    private Object controllers_ = new SimpleScriptObject();
    private Document2 document_;

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
        final AlertHandler handler = ((WebWindow) Global.instance().getDomObject()).getWebClient().getAlertHandler();
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
        return Global.instance().getDomObject();
    }

    @Getter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static int getInnerHeight(final Object self) {
        final WebWindow webWindow = Global.instance().getDomObject();
        return webWindow.getInnerHeight();
    }

    @Getter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static int getInnerWidth(final Object self) {
        final WebWindow webWindow = Global.instance().getDomObject();
        return webWindow.getInnerWidth();
    }

    @Getter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static int getOuterHeight(final Object self) {
        final WebWindow webWindow = Global.instance().getDomObject();
        return webWindow.getOuterHeight();
    }

    @Getter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static int getOuterWidth(final Object self) {
        final WebWindow webWindow = Global.instance().getDomObject();
        return webWindow.getOuterWidth();
    }

    @Getter
    public static Object getTop(final Object self) {
        final WebWindow webWindow = Global.instance().getDomObject();
        final WebWindow top = webWindow.getTopWindow();
        return top.getScriptObject2();
    }

    @Getter(@WebBrowser(FF))
    public static Object getControllers(final Object self) {
        return Global.instance().<Window2>getWindow().controllers_;
    }

    @Setter(@WebBrowser(FF))
    public static void setControllers(final Object self, final Object value) {
        Global.instance().<Window2>getWindow().controllers_ = value;
    }

    private Object getHandlerForJavaScript(final String eventName) {
        return getEventListenersContainer().getEventHandlerProp(eventName);
    }

    @Getter
    public static Object getOnload(final Object self) {
        final Window2 window = Global.instance().getWindow();
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
        final Window2 window = Global.instance().getWindow();
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
        final Window2 window = Global.instance().getWindow();
        return window.document_;
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

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("Window", 
                    staticHandle("constructor", Window2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends PrototypeObject {
        public ScriptFunction alert;
        public ScriptFunction atob;
        public ScriptFunction btoa;
        public ScriptFunction CollectGarbage;

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
