/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.corejs.javascript.Callable;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.host.dom.AbstractList;

/**
 * A special {@link HTMLCollection} for <code>document.all</code>.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@JsxClass
public class HTMLAllCollection extends AbstractList implements Callable {

    /**
     * Creates an instance.
     */
    public HTMLAllCollection() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     */
    public HTMLAllCollection(final DomNode parentScope) {
        super(parentScope, false, null);
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536460.aspx">MSDN doc</a>
     */
    @JsxFunction
    public Object item(final Object index) {
        final double numb;

        if (index instanceof String) {
            final String name = (String) index;
            final Object result = namedItem(name);
            if (null != result && !JavaScriptEngine.isUndefined(result)) {
                return result;
            }
            numb = JavaScriptEngine.toNumber(index);
            if (Double.isNaN(numb)) {
                return null;
            }
        }
        else {
            numb = JavaScriptEngine.toNumber(index);
        }

        if (numb < 0) {
            return null;
        }

        if (Double.isInfinite(numb) || numb != Math.floor(numb)) {
            return null;
        }

        final Object object = get((int) numb, this);
        if (object == NOT_FOUND) {
            return null;
        }
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
    public final Scriptable namedItem(final String name) {
        final List<DomNode> elements = getElements();

        // See if there is an element in the element array with the specified id.
        final List<DomElement> matching = new ArrayList<>();

        for (final DomNode next : elements) {
            if (next instanceof DomElement) {
                final DomElement elem = (DomElement) next;
                if (name.equals(elem.getAttributeDirect(DomElement.NAME_ATTRIBUTE))
                        || name.equals(elem.getId())) {
                    matching.add(elem);
                }
            }
        }

        if (matching.size() == 1) {
            return getScriptableForElement(matching.get(0));
        }
        if (matching.isEmpty()) {
            return null;
        }

        // many elements => build a sub collection
        final DomNode domNode = getDomNodeOrNull();
        final List<DomNode> nodes = new ArrayList<>(matching);
        final HTMLCollection collection = new HTMLCollection(domNode, nodes);
        collection.setAvoidObjectDetection(true);
        return collection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        boolean nullIfNotFound = false;
        if (args[0] instanceof Number) {
            final double val = ((Number) args[0]).doubleValue();
            if (val != (int) val) {
                return null;
            }
            if (val >= 0) {
                nullIfNotFound = true;
            }
        }
        else {
            final String val = JavaScriptEngine.toString(args[0]);
            try {
                args[0] = Integer.parseInt(val);
            }
            catch (final NumberFormatException ignored) {
                // ignore
            }
        }

        if (args.length == 0) {
            throw JavaScriptEngine.reportRuntimeError("Zero arguments; need an index or a key.");
        }
        Object value = getIt(args[0]);
        if (value == NOT_FOUND) {
            value = null;
        }
        if (nullIfNotFound && JavaScriptEngine.isUndefined(value)) {
            return null;
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object equivalentValues(final Object value) {
        if (value == null || JavaScriptEngine.isUndefined(value)) {
            return Boolean.TRUE;
        }

        return super.equivalentValues(value);
    }
    /**
     * @return the Iterator symbol
     */
    @JsxSymbol
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }

    /**
     * Returns the length.
     * @return the length
     */
    @JsxGetter
    @Override
    public final int getLength() {
        return super.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getWithPreemptionByName(final String name, final List<DomNode> elements) {
        final List<DomNode> matchingElements = new ArrayList<>();
        final boolean searchName = isGetWithPreemptionSearchName();
        for (final DomNode next : elements) {
            if (next instanceof DomElement
                    && (searchName || next instanceof HtmlInput || next instanceof HtmlForm)) {
                final String nodeName = ((DomElement) next).getAttributeDirect(DomElement.NAME_ATTRIBUTE);
                if (name.equals(nodeName)) {
                    matchingElements.add(next);
                }
            }
        }

        if (matchingElements.isEmpty()) {
            return NOT_FOUND;
        }

        if (matchingElements.size() == 1) {
            return getScriptableForElement(matchingElements.get(0));
        }

        // many elements => build a sub collection
        final DomNode domNode = getDomNodeOrNull();
        final HTMLCollection collection = new HTMLCollection(domNode, matchingElements);
        collection.setAvoidObjectDetection(true);
        return collection;
    }

    /**
     * Returns whether {@link #getWithPreemption(String)} should search by name or not.
     * @return whether {@link #getWithPreemption(String)} should search by name or not
     */
    protected boolean isGetWithPreemptionSearchName() {
        return true;
    }
}
