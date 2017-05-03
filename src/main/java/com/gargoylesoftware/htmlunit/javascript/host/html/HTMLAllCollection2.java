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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_DO_NOT_CONVERT_STRINGS_TO_NUMBER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_DO_NOT_SUPPORT_PARANTHESES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_INTEGER_INDEX;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NULL_IF_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_ITEM_FUNCT_SUPPORTS_DOUBLE_INDEX_ALSO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NAMED_ITEM_ID_FIRST;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;

/**
 * A special {@link HTMLCollection} for <code>document.all</code>.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@ScriptClass(nullProto = true)
public class HTMLAllCollection2 extends HTMLCollection2 {

    private HTMLAllCollection2() {
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     */
    public HTMLAllCollection2(final DomNode parentScope) {
        super(parentScope, false);
    }

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static HTMLAllCollection2 constructor(final boolean newObj, final Object self) {
        final HTMLAllCollection2 host = new HTMLAllCollection2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536460.aspx">MSDN doc</a>
     */
    @Override
    public Object item(final Object index) {
        Double numb;

        final BrowserVersion browser;
        if (index instanceof String) {
            final String name = (String) index;
            final Object result = namedItem(name);
            if (null != result && ScriptRuntime.UNDEFINED != result) {
                return result;
            }
            numb = Double.NaN;

            browser = getBrowserVersion();
            if (!browser.hasFeature(HTMLALLCOLLECTION_DO_NOT_CONVERT_STRINGS_TO_NUMBER)) {
                numb = toDouble(name);
            }
            if (numb.isNaN()) {
                return itemNotFound(browser);
            }
        }
        else {
            numb = ((Number) index).doubleValue();
            browser = getBrowserVersion();
        }

        if (numb < 0) {
            return itemNotFound(browser);
        }

        if (!browser.hasFeature(HTMLCOLLECTION_ITEM_FUNCT_SUPPORTS_DOUBLE_INDEX_ALSO)
                && (Double.isInfinite(numb) || numb != Math.floor(numb))) {
            return itemNotFound(browser);
        }

        final Object object = get(numb.intValue());
//        if (object == NOT_FOUND) {
//            return null;
//        }
        return object;
    }

    private static Double toDouble(final String str) {
        try {
            return Double.parseDouble(str);
        }
        catch (final NumberFormatException nfe) {
            return Double.NaN;
        }
    }

    private static Object itemNotFound(final BrowserVersion browser) {
        if (browser.hasFeature(HTMLALLCOLLECTION_NULL_IF_ITEM_NOT_FOUND)) {
            return null;
        }
        return ScriptRuntime.UNDEFINED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object namedItem(final String name) {
        final List<DomNode> elements = getElements();

        // See if there is an element in the element array with the specified id.
        final List<DomElement> matching = new ArrayList<>();

        final BrowserVersion browser = getBrowserVersion();

        final boolean idFirst = browser.hasFeature(HTMLCOLLECTION_NAMED_ITEM_ID_FIRST);
        if (idFirst) {
            for (final Object next : elements) {
                if (next instanceof DomElement) {
                    final DomElement elem = (DomElement) next;
                    if (name.equals(elem.getId())) {
                        matching.add(elem);
                    }
                }
            }
        }

        for (final Object next : elements) {
            if (next instanceof DomElement) {
                final DomElement elem = (DomElement) next;
                if (name.equals(elem.getAttribute("name"))) {
                    matching.add(elem);
                }
                else if (!idFirst && name.equals(elem.getId())) {
                    matching.add(elem);
                }
            }
        }

        if (matching.size() == 1
                || (matching.size() > 1
                        && browser.hasFeature(HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS))) {
            return getScriptObjectForElement(matching.get(0));
        }
        if (matching.isEmpty()) {
            if (browser.hasFeature(HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND)) {
                return null;
            }
            return ScriptRuntime.UNDEFINED;
        }

        // many elements => build a sub collection
        final DomNode domNode = getDomNodeOrNull();
        final HTMLCollection2 collection = new HTMLCollection2(domNode, new ArrayList<>(matching));
        collection.setAvoidObjectDetection(true);
        return collection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(Object index) {
        final BrowserVersion browser = getBrowserVersion();
        if (browser.hasFeature(HTMLALLCOLLECTION_DO_NOT_SUPPORT_PARANTHESES)) {
            if (index == null) {
                throw new RuntimeException("Zero arguments; need an index or a key.");
            }

            if (index instanceof Number) {
                return null;
            }
        }

        boolean nullIfNotFound = false;
        if (browser.hasFeature(HTMLALLCOLLECTION_INTEGER_INDEX)) {
            if (index instanceof Number) {
                final double val = ((Number) index).doubleValue();
                if (val != (int) val) {
                    return ScriptRuntime.UNDEFINED;
                }
                if (val >= 0) {
                    nullIfNotFound = true;
                }
            }
            else {
                final String val = String.valueOf(index);
                try {
                    index = Integer.parseInt(val);
                }
                catch (final NumberFormatException e) {
                    // ignore
                }
            }
        }

        final Object value = super.call(index);
        if (nullIfNotFound && value == ScriptRuntime.UNDEFINED) {
            return null;
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean supportsParanteses() {
        return true;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLAllCollection2.class,
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
            super("HTMLAllCollection",
                    staticHandle("constructor", HTMLAllCollection2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("HTMLAllCollection");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("HTMLAllCollection");
        }
    }
}
