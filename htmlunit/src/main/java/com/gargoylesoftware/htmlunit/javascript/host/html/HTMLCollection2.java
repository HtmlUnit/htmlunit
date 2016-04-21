/*
 * Copyright (c) 2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_COMMENT_IS_ELEMENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_EXCEPTION_FOR_NEGATIVE_INDEX;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_ITEM_SUPPORTS_ID_SEARCH_ALSO;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

public class HTMLCollection2 extends AbstractList2 {

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLCollection2() {
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     * @param description a text useful for debugging
     */
    public HTMLCollection2(final DomNode parentScope, final boolean attributeChangeSensitive, final String description) {
        super(parentScope, attributeChangeSensitive, description);
    }

    public static HTMLCollection2 constructor(final boolean newObj, final Object self) {
        final HTMLCollection2 host = new HTMLCollection2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
    }

    /**
     * Returns the elements whose associated host objects are available through this collection.
     * @return the elements whose associated host objects are available through this collection
     */
    @Override
    protected List<Object> computeElements() {
        final List<Object> response = new ArrayList<>();
        final DomNode domNode = getDomNodeOrNull();
        if (domNode == null) {
            return response;
        }
        final boolean commentIsElement = getBrowserVersion().hasFeature(HTMLCOLLECTION_COMMENT_IS_ELEMENT);
        for (final DomNode node : getCandidates()) {
            if ((node instanceof DomElement
                    || (commentIsElement && node instanceof DomComment)) && isMatching(node)) {
                response.add(node);
            }
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Getter
    public final Object getLength() {
        return getElements().size();
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536460.aspx">MSDN doc</a>
     */
    @JsxFunction
    public Object item(final Object index) {
        if (index instanceof String && getBrowserVersion().hasFeature(HTMLCOLLECTION_ITEM_SUPPORTS_ID_SEARCH_ALSO)) {
            final String name = (String) index;
            final Object result = namedItem(name);
            return result;
        }

        int idx = 0;
        final Double doubleValue = (double) index;
        if (ScriptRuntime.NaN != doubleValue && !doubleValue.isNaN()) {
            idx = doubleValue.intValue();
        }

        if (idx < 0 && getBrowserVersion().hasFeature(HTMLCOLLECTION_EXCEPTION_FOR_NEGATIVE_INDEX)) {
//            throw Context.reportRuntimeError("Invalid index.");
        }

        final Object object = get(idx);
//        if (object == NOT_FOUND) {
//            return null;
//        }
        return object;
    }

    /**
     * Retrieves the item or items corresponding to the specified name (checks ids, and if
     * that does not work, then names).
     * @param name the name or id the element or elements to return
     * @return the element or elements corresponding to the specified name or id
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536634.aspx">MSDN doc</a>
     */
    @JsxFunction
    public Object namedItem(final String name) {
        final List<Object> elements = getElements();
        for (final Object next : elements) {
            if (next instanceof DomElement) {
                final DomElement elem = (DomElement) next;
                final String nodeName = elem.getAttribute("name");
                if (name.equals(nodeName)) {
                    return getScriptObjectForElement(elem);
                }

                final String id = elem.getAttribute("id");
                if (name.equals(id)) {
                    return getScriptObjectForElement(elem);
                }
            }
        }
        return null;
    }

    /**
     * Gets the scriptable for the provided element that may already be the right scriptable.
     * @param object the object for which to get the scriptable
     * @return the scriptable
     */
    protected SimpleScriptObject getScriptObjectForElement(final Object object) {
        if (object instanceof SimpleScriptObject) {
            return (SimpleScriptObject) object;
        }
        return getScriptableFor(object);
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLCollection2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("HTMLCollection", 
                    staticHandle("constructor", HTMLCollection2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends PrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "HTMLCollection";
        }
    }
}
