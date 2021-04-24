/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_DO_NOT_CONVERT_STRINGS_TO_NUMBER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_INTEGER_INDEX;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_ITEM_FUNCT_SUPPORTS_DOUBLE_INDEX_ALSO;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A special {@link HTMLCollection} for <code>document.all</code>.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@JsxClass
public class HTMLAllCollection extends HTMLCollection {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLAllCollection() {
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     */
    public HTMLAllCollection(final DomNode parentScope) {
        super(parentScope, false);
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
            if (null != result && !Undefined.isUndefined(result)) {
                return result;
            }
            numb = Double.NaN;

            browser = getBrowserVersion();
            if (!browser.hasFeature(HTMLALLCOLLECTION_DO_NOT_CONVERT_STRINGS_TO_NUMBER)) {
                numb = ScriptRuntime.toNumber(index);
            }
            if (numb.isNaN()) {
                return null;
            }
        }
        else {
            numb = ScriptRuntime.toNumber(index);
            browser = getBrowserVersion();
        }

        if (numb < 0) {
            return null;
        }

        if (!browser.hasFeature(HTMLCOLLECTION_ITEM_FUNCT_SUPPORTS_DOUBLE_INDEX_ALSO)
                && (Double.isInfinite(numb) || numb != Math.floor(numb))) {
            return null;
        }

        final Object object = get(numb.intValue(), this);
        if (object == NOT_FOUND) {
            return null;
        }
        return object;
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
        for (final DomNode next : elements) {
            if (next instanceof DomElement) {
                final DomElement elem = (DomElement) next;
                if (name.equals(elem.getAttributeDirect("name"))
                        || name.equals(elem.getId())) {
                    matching.add(elem);
                }
            }
        }

        if (matching.size() == 1
                || (matching.size() > 1
                        && browser.hasFeature(HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS))) {
            return getScriptableForElement(matching.get(0));
        }
        if (matching.isEmpty()) {
            if (browser.hasFeature(HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND)) {
                return null;
            }
            return Undefined.instance;
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
        final BrowserVersion browser = getBrowserVersion();
        boolean nullIfNotFound = false;
        if (browser.hasFeature(HTMLALLCOLLECTION_INTEGER_INDEX)) {
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
                final String val = Context.toString(args[0]);
                try {
                    args[0] = Integer.parseInt(val);
                }
                catch (final NumberFormatException e) {
                    // ignore
                }
            }
        }

        final Object value = super.call(cx, scope, thisObj, args);
        if (nullIfNotFound && Undefined.isUndefined(value)) {
            return null;
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean supportsParentheses() {
        return true;
    }
}
