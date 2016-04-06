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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_DO_NOT_CHECK_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_DO_NOT_CONVERT_STRINGS_TO_NUMBER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_DO_NOT_SUPPORT_PARANTHESES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NULL_IF_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_ITEM_FUNCT_SUPPORTS_DOUBLE_INDEX_ALSO;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;

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
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38),
    @WebBrowser(IE), @WebBrowser(EDGE) })
public class HTMLAllCollection extends HTMLCollection {

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38), @WebBrowser(EDGE) })
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
    @JsxFunction
    public Object item(final Object index) {
        Double numb;

        final BrowserVersion browser;
        if (index instanceof String) {
            final String name = (String) index;
            final Object result = namedItem(name);
            if (null != result && Undefined.instance != result) {
                return result;
            }
            numb = Double.NaN;

            browser = getBrowserVersion();
            if (!browser.hasFeature(HTMLALLCOLLECTION_DO_NOT_CONVERT_STRINGS_TO_NUMBER)) {
                numb = ScriptRuntime.toNumber(index);
            }
            if (ScriptRuntime.NaN == numb || numb.isNaN()) {
                return itemNotFound(browser);
            }
        }
        else {
            numb = ScriptRuntime.toNumber(index);
            browser = getBrowserVersion();
        }

        if (!browser.hasFeature(HTMLCOLLECTION_ITEM_FUNCT_SUPPORTS_DOUBLE_INDEX_ALSO)
                && (Double.isInfinite(numb) || numb != Math.floor(numb))) {
            return itemNotFound(browser);
        }

        final Object object = get(numb.intValue(), this);
        if (object == NOT_FOUND) {
            if (browser.hasFeature(HTMLALLCOLLECTION_DO_NOT_CHECK_NAME) && index instanceof Number && numb >= 0) {
                return null;
            }
            return itemNotFound(browser);
        }
        return object;
    }

    private static Object itemNotFound(final BrowserVersion browser) {
        if (browser.hasFeature(HTMLALLCOLLECTION_NULL_IF_ITEM_NOT_FOUND)) {
            return null;
        }
        return Undefined.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isGetWithPreemptionSearchName() {
        return !getBrowserVersion().hasFeature(HTMLALLCOLLECTION_DO_NOT_CHECK_NAME);
    }

    @JsxFunction
    @Override
    public final Object namedItem(final String name) {
        final List<Object> elements = getElements();

        // See if there is an element in the element array with the specified id.
        final List<DomElement> matching = new ArrayList<>();

        final BrowserVersion browser = getBrowserVersion();
        final boolean idFirst = browser.hasFeature(HTMLALLCOLLECTION_DO_NOT_CHECK_NAME);
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
                if ((!idFirst || (elem instanceof HtmlForm)) && name.equals(elem.getAttribute("name"))) {
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
        final HTMLCollection collection = new HTMLCollection(domNode, matching);
        collection.setAvoidObjectDetection(true);
        return collection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        final BrowserVersion browser = getBrowserVersion();
        if (browser.hasFeature(HTMLALLCOLLECTION_DO_NOT_SUPPORT_PARANTHESES)) {
            if (args.length == 0) {
                throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
            }

            if (args[0] instanceof Number) {
                return null;
            }
        }

        boolean nullIfNotFound = false;
        if (browser.hasFeature(BrowserVersionFeatures.HTMLALLCOLLECTION_INTEGER_INDEX)) {
            if (args[0] instanceof Number) {
                final double val = ((Number) args[0]).doubleValue();
                if (val != (int) val) {
                    return Undefined.instance;
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
        if (nullIfNotFound && value == Undefined.instance) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractList create(final DomNode parentScope, final List<?> initialElements) {
        if (getBrowserVersion().hasFeature(HTMLALLCOLLECTION_DO_NOT_CHECK_NAME)) {
            return new NodeList(parentScope, initialElements);
        }
        return super.create(parentScope, initialElements);
    }

}
