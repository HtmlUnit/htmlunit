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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;

/**
 * The JavaScript object that represents an {@code HTMLScriptElement}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@ScriptClass
public class HTMLScriptElement2 extends HTMLElement2 {

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static HTMLScriptElement2 constructor(final boolean newObj, final Object self) {
        final HTMLScriptElement2 host = new HTMLScriptElement2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Returns the {@code src} property.
     * @return the {@code src} property
     */
    @Getter
    public String getSrc() {
        final HtmlScript tmpScript = (HtmlScript) getDomNodeOrDie();
        String src = tmpScript.getSrcAttribute();
        if (ATTRIBUTE_NOT_DEFINED == src) {
            return src;
        }
        try {
            final URL expandedSrc = ((HtmlPage) tmpScript.getPage()).getFullyQualifiedUrl(src);
            src = expandedSrc.toString();
        }
        catch (final MalformedURLException e) {
            // ignore
        }
        return src;
    }

    /**
     * Sets the {@code src} property.
     * @param src the {@code src} property
     */
    @Setter
    public void setSrc(final String src) {
        getDomNodeOrDie().setAttribute("src", src);
    }

    /**
     * Returns the {@code text} property.
     * @return the {@code text} property
     */
    @Getter
    public String getText() {
        final StringBuilder scriptCode = new StringBuilder();
        for (final DomNode node : getDomNodeOrDie().getChildren()) {
            if (node instanceof DomText) {
                final DomText domText = (DomText) node;
                scriptCode.append(domText.getData());
            }
        }
        return scriptCode.toString();
    }

    /**
     * Sets the {@code text} property.
     * @param text the {@code text} property
     */
    @Setter
    public void setText(final String text) {
        final HtmlElement htmlElement = getDomNodeOrDie();
        htmlElement.removeAllChildren();
        final DomNode textChild = new DomText(htmlElement.getPage(), text);
        htmlElement.appendChild(textChild);

        final HtmlScript tmpScript = (HtmlScript) htmlElement;
        tmpScript.executeScriptIfNeeded();
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @Getter
    public String getType() {
        return getDomNodeOrDie().getAttribute("type");
    }

    /**
     * Sets the {@code type} property.
     * @param type the {@code type} property
     */
    @Setter
    public void setType(final String type) {
        getDomNodeOrDie().setAttribute("type", type);
    }

    /**
     * Returns the event handler that fires on every state change.
     * @return the event handler that fires on every state change
     */
    @Getter(IE)
    public Object getOnreadystatechange() {
        return getEventHandlerProp("onreadystatechange");
    }

    /**
     * Sets the event handler that fires on every state change.
     * @param handler the event handler that fires on every state change
     */
    @Setter(IE)
    public void setOnreadystatechange(final Object handler) {
        setEventHandler(Event.TYPE_READY_STATE_CHANGE, handler);
    }

    /**
     * Returns the event handler that fires on load.
     * @return the event handler that fires on load
     */
    @Getter
    public Object getOnload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Sets the event handler that fires on load.
     * @param handler the event handler that fires on load
     */
    @Setter
    public void setOnload(final Object handler) {
        setEventHandler(Event2.TYPE_LOAD, handler);
    }

    /**
     * Returns the ready state of the script. This is an IE-only property.
     * @return the ready state of the script
     * @see DomNode#READY_STATE_UNINITIALIZED
     * @see DomNode#READY_STATE_LOADING
     * @see DomNode#READY_STATE_LOADED
     * @see DomNode#READY_STATE_INTERACTIVE
     * @see DomNode#READY_STATE_COMPLETE
     */
    @Getter(IE)
    public Object getReadyState() {
        final HtmlScript tmpScript = (HtmlScript) getDomNodeOrDie();
        if (tmpScript.wasCreatedByJavascript()) {
            return ScriptRuntime.UNDEFINED;
        }
        return tmpScript.getReadyState();
    }

    /**
     * Overwritten for special IE handling.
     *
     * @param childObject the node to add to this node
     * @return the newly added child node
     */
    @Override
    public Object appendChild(final Object childObject) {
        final HtmlScript tmpScript = (HtmlScript) getDomNodeOrDie();
        final boolean wasEmpty = tmpScript.getFirstChild() == null;
        final Object result = super.appendChild(childObject);

        if (wasEmpty) {
            tmpScript.executeScriptIfNeeded();
        }
        return result;
    }

    /**
     * Returns the {@code async} property.
     * @return the {@code async} property
     */
    @Getter
    public Boolean isAsync() {
        return getDomNodeOrDie().hasAttribute("async");
    }

    /**
     * Sets the {@code async} property.
     * @param async the {@code async} property
     */
    @Setter
    public void setAsync(final boolean async) {
        if (async) {
            getDomNodeOrDie().setAttribute("async", "");
        }
        else {
            getDomNodeOrDie().removeAttribute("async");
        }
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLScriptElement2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("HTMLScriptElement",
                    staticHandle("constructor", HTMLScriptElement2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends PrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        @Override
        public String getClassName() {
            return "HTMLScriptElement";
        }
    }
}
