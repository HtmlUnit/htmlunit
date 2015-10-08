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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.Property;
import com.gargoylesoftware.js.nashorn.internal.runtime.PropertyMap;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

public class Window2 extends SimpleScriptObject {

    private static final Log LOG = LogFactory.getLog(Window2.class);

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
//            clearEventListenersContainer();

//            document_.setDomNode(htmlPage);
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

    @Getter(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static int getInnerHeight(final Object self) {
        final WebWindow webWindow = (WebWindow) Global.instance().getDomObject();
        return webWindow.getInnerHeight();
    }

    @Getter(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static int getInnerWidth(final Object self) {
        final WebWindow webWindow = (WebWindow) Global.instance().getDomObject();
        return webWindow.getInnerWidth();
    }

    @Getter(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static int getOuterHeight(final Object self) {
        final WebWindow webWindow = (WebWindow) Global.instance().getDomObject();
        return webWindow.getOuterHeight();
    }

    @Getter(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static int getOuterWidth(final Object self) {
        final WebWindow webWindow = (WebWindow) Global.instance().getDomObject();
        return webWindow.getOuterWidth();
    }

    @Getter
    public static Object getTop(final Object self) {
        final WebWindow webWindow = (WebWindow) Global.instance().getDomObject();
        final WebWindow top = webWindow.getTopWindow();
        return top.getScriptObject2();
    }

    {
        final List<Property> list = new ArrayList<>(1);
        setMap(PropertyMap.newMap(list));
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

        public ScriptFunction G$alert() {
            return alert;
        }

        public void S$alert(final ScriptFunction function) {
            this.alert = function;
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

        public ScriptFunction G$alert() {
            return this.alert;
        }

        public void S$alert(final ScriptFunction function) {
            this.alert = function;
        }

        public ObjectConstructor() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "Window";
        }
    }
}
