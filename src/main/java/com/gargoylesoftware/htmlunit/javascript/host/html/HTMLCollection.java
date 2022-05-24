/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_ITEM_SUPPORTS_DOUBLE_INDEX_ALSO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_ITEM_SUPPORTS_ID_SEARCH_ALSO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NAMED_ITEM_ID_FIRST;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_SUPPORTS_PARANTHESES;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;

import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * An array of elements. Used for the element arrays returned by <tt>document.all</tt>,
 * <tt>document.all.tags('x')</tt>, <tt>document.forms</tt>, <tt>window.frames</tt>, etc.
 * Note that this class must not be used for collections that can be modified, for example
 * <tt>map.areas</tt> and <tt>select.options</tt>.
 * <br>
 * This class (like all classes in this package) is specific for the JavaScript engine.
 * Users of HtmlUnit shouldn't use it directly.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class HTMLCollection extends AbstractList implements Callable {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public HTMLCollection() {
    }

    /**
     * Creates an instance.
     * @param domNode parent scope
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     */
    public HTMLCollection(final DomNode domNode, final boolean attributeChangeSensitive) {
        super(domNode, attributeChangeSensitive, null);
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param domNode the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    HTMLCollection(final DomNode domNode, final List<DomNode> initialElements) {
        super(domNode, true, new ArrayList<>(initialElements));
    }

    private HTMLCollection(final DomNode domNode, final boolean attributeChangeSensitive,
            final List<DomNode> initialElements) {
        super(domNode, attributeChangeSensitive, new ArrayList<>(initialElements));
    }

    /**
     * Gets an empty collection.
     * @param domNode the DOM node
     * @return an empty collection
     */
    public static HTMLCollection emptyCollection(final DomNode domNode) {
        return new HTMLCollection(domNode, false, Collections.emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HTMLCollection create(final DomNode parentScope, final List<DomNode> initialElements) {
        return new HTMLCollection(parentScope, initialElements);
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
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (supportsParentheses()) {
            if (args.length == 0) {
                throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
            }
            final Object object = getIt(args[0]);
            if (object == NOT_FOUND) {
                if (getBrowserVersion().hasFeature(HTMLCOLLECTION_NULL_IF_NOT_FOUND)) {
                    return null;
                }
                return Undefined.instance;
            }
            return object;
        }

        throw ScriptRuntime.typeError("HTMLCollection does nont support function like access");
    }

    /**
     * Is parentheses supported.
     *
     * @return true or false
     */
    protected boolean supportsParentheses() {
        return getBrowserVersion().hasFeature(HTMLCOLLECTION_SUPPORTS_PARANTHESES);
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
                final String nodeName = ((DomElement) next).getAttributeDirect("name");
                if (name.equals(nodeName)) {
                    matchingElements.add(next);
                }
            }
        }

        if (matchingElements.isEmpty()) {
            if (getBrowserVersion().hasFeature(HTMLCOLLECTION_ITEM_SUPPORTS_DOUBLE_INDEX_ALSO)) {
                final double doubleValue = Context.toNumber(name);
                if (!Double.isNaN(doubleValue)) {
                    final Object object = get((int) doubleValue, this);
                    if (object != NOT_FOUND) {
                        return object;
                    }
                }
            }
            return NOT_FOUND;
        }
        else if (matchingElements.size() == 1) {
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
            return namedItem(name);
        }

        int idx = 0;
        final double doubleValue = Context.toNumber(index);
        if (!Double.isNaN(doubleValue)) {
            idx = (int) doubleValue;
        }

        final Object object = get(idx, this);
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
    public Object namedItem(final String name) {
        final List<DomNode> elements = getElements();
        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(HTMLCOLLECTION_NAMED_ITEM_ID_FIRST)) {
            for (final Object next : elements) {
                if (next instanceof DomElement) {
                    final DomElement elem = (DomElement) next;
                    final String id = elem.getId();
                    if (name.equals(id)) {
                        return getScriptableForElement(elem);
                    }
                }
            }
        }
        for (final Object next : elements) {
            if (next instanceof DomElement) {
                final DomElement elem = (DomElement) next;
                final String nodeName = elem.getAttributeDirect("name");
                if (name.equals(nodeName)) {
                    return getScriptableForElement(elem);
                }

                final String id = elem.getId();
                if (name.equals(id)) {
                    return getScriptableForElement(elem);
                }
            }
        }
        return null;
    }

    /**
     * Returns all the elements in this element array that have the specified tag name.
     * This method returns an empty element array if there are no elements with the
     * specified tag name.
     * @param tagName the name of the tag of the elements to return
     * @return all the elements in this element array that have the specified tag name
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536776.aspx">MSDN doc</a>
     */
    @JsxFunction(IE)
    public Object tags(final String tagName) {
        final HTMLCollection tags = new HTMLCollection(getDomNodeOrDie(), false);
        tags.setElementsSupplier(
                (Supplier<List<DomNode>> & Serializable)
                () -> {
                    final List<DomNode> list = new ArrayList<>();
                    for (final DomNode elem : this.getElements()) {
                        if (tagName.equalsIgnoreCase(elem.getLocalName())) {
                            list.add(elem);
                        }
                    }
                    return list;
                });
        return tags;
    }
}
