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
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Browser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily;
import com.gargoylesoftware.js.nashorn.internal.runtime.AccessorProperty;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.Property;
import com.gargoylesoftware.js.nashorn.internal.runtime.PropertyMap;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

public class Window2 extends SimpleScriptObject {

    private static final Log LOG = LogFactory.getLog(Window2.class);

    private WebWindow webWindow_;

    /**
     * Initializes this window.
     * @param webWindow the web window corresponding to this window
     */
    public void initialize(final WebWindow webWindow) {
        webWindow_ = webWindow;
        webWindow_.setScriptObject(this);
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
//            clearEventListenersContainer();

//            document_.setDomNode(htmlPage);
        }
    }

    /**
     * Returns the WebWindow associated with this Window.
     * @return the WebWindow
     */
    public WebWindow getWebWindow() {
        return webWindow_;
    }

    public static Window2 constructor(final boolean newObj, final Object self) {
        final Window2 host = new Window2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
    }

    public static void alert(final Object self, final Object o) {
        final Window2 window = (Window2) Global.instance().get("window");
        final AlertHandler handler = window.webWindow_.getWebClient().getAlertHandler();
        if (handler == null) {
            LOG.warn("window.alert(\"" + o + "\") no alert handler installed");
        }
        else {
//            handler.handleAlert(document_.getPage(), stringMessage);
          handler.handleAlert(null, o.toString());
        }
    }

    public static int innerHeight(final Object self) {
        final Window2 window = (Window2) Global.instance().get("window");
        return window.getWebWindow().getInnerHeight();
    }

    public static int innerWidth(final Object self) {
        final Window2 window = (Window2) Global.instance().get("window");
        return window.getWebWindow().getInnerWidth();
    }

    public static int outerHeight(final Object self) {
        final Window2 window = (Window2) Global.instance().get("window");
        return window.getWebWindow().getOuterHeight();
    }

    public static int outerWidth(final Object self) {
        final Window2 window = (Window2) Global.instance().get("window");
        return window.getWebWindow().getOuterWidth();
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

    static final class Prototype extends PrototypeObject {
        private ScriptFunction alert = ScriptFunction.createBuiltin("alert",
                staticHandle("alert", void.class, Object.class, Object.class));

        public ScriptFunction G$alert() {
            return alert;
        }

        public void S$alert(final ScriptFunction function) {
            this.alert = function;
        }

        {
            final List<Property> list = new ArrayList<>(1);
            list.add(AccessorProperty.create("alert", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                    virtualHandle("G$alert", ScriptFunction.class),
                    virtualHandle("S$alert", void.class, ScriptFunction.class)));
            final BrowserFamily browserFamily = Browser.getCurrent().getFamily();
            if (browserFamily == CHROME) {
                list.add(AccessorProperty.create("innerWidth", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                        staticHandle("innerWidth", int.class, Object.class),
                        null));
                list.add(AccessorProperty.create("innerHeight", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                        staticHandle("innerHeight", int.class, Object.class),
                        null));
                list.add(AccessorProperty.create("outerWidth", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                        staticHandle("outerWidth", int.class, Object.class),
                        null));
                list.add(AccessorProperty.create("outerHeight", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                        staticHandle("outerHeight", int.class, Object.class),
                        null));
            }
            setMap(PropertyMap.newMap(list));
        }

        public String getClassName() {
            return "Window";
        }

        private static MethodHandle virtualHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
            try {
                return MethodHandles.lookup().findVirtual(Prototype.class, name,
                        MethodType.methodType(rtype, ptypes));
            }
            catch (final ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static final class ObjectConstructor extends ScriptObject {
        private ScriptFunction alert = ScriptFunction.createBuiltin("alert",
                staticHandle("alert", void.class, Object.class, Object.class));

        public ScriptFunction G$alert() {
            return this.alert;
        }

        public void S$alert(final ScriptFunction function) {
            this.alert = function;
        }

        {
            final List<Property> list = new ArrayList<>(1);
            list.add(AccessorProperty.create("alert", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                    virtualHandle("G$alert", ScriptFunction.class),
                    virtualHandle("S$alert", void.class, ScriptFunction.class)));
            final BrowserFamily browserFamily = Browser.getCurrent().getFamily();
            final int browserVersion = Browser.getCurrent().getVersion();
            if ((browserFamily == IE && browserVersion >= 11) || browserFamily == FF) {
                  list.add(AccessorProperty.create("innerWidth", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                          staticHandle("innerWidth", int.class, Object.class),
                          null));
                  list.add(AccessorProperty.create("innerHeight", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                          staticHandle("innerHeight", int.class, Object.class),
                          null));
                  list.add(AccessorProperty.create("outerWidth", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                          staticHandle("outerWidth", int.class, Object.class),
                          null));
                  list.add(AccessorProperty.create("outerHeight", Property.WRITABLE_ENUMERABLE_CONFIGURABLE, 
                          staticHandle("outerHeight", int.class, Object.class),
                          null));
              }
            setMap(PropertyMap.newMap(list));
        }

        public String getClassName() {
            return "Window";
        }

        private static MethodHandle virtualHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
            try {
                return MethodHandles.lookup().findVirtual(ObjectConstructor.class, name,
                        MethodType.methodType(rtype, ptypes));
            }
            catch (final ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
