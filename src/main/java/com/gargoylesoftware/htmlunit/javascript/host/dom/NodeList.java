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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSymbol;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ES6Iterator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArrayIterator;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * An array of elements. Used for the element arrays returned by <tt>document.all</tt>,
 * <tt>document.all.tags('x')</tt>, <tt>document.forms</tt>, <tt>window.frames</tt>, etc.
 * Note that this class must not be used for collections that can be modified, for example
 * <tt>map.areas</tt> and <tt>select.options</tt>.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class NodeList extends AbstractList {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public NodeList() {
    }

    /**
     * Creates an instance.
     *
     * @param domNode the {@link DomNode}
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     */
    public NodeList(final DomNode domNode, final boolean attributeChangeSensitive) {
        super(domNode, attributeChangeSensitive);
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param domNode the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    public NodeList(final DomNode domNode, final List<DomNode> initialElements) {
        super(domNode, initialElements);
    }

    /**
     * Creates an instance.
     * @param parentScope the parent scope
     */
    NodeList(final ScriptableObject parentScope) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
        setExternalArrayData(this);
    }

    /**
     * Gets a static NodeList.
     *
     * @param parentScope the parent scope
     * @param elements the elements
     * @return an empty collection
     */
    public static NodeList staticNodeList(final HtmlUnitScriptable parentScope, final List<DomNode> elements) {
        return new NodeList(parentScope) {
            @Override
            public List<DomNode> getElements() {
                return elements;
            }
        };
    }

    /**
     * Returns an Iterator allowing to go through all keys contained in this object.
     * @return an {@link NativeArrayIterator}
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public ES6Iterator keys() {
        return new NativeArrayIterator(getParentScope(), this, NativeArrayIterator.ARRAY_ITERATOR_TYPE.KEYS);
    }

    /**
     * Returns an Iterator allowing to go through all keys contained in this object.
     * @return an {@link NativeArrayIterator}
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public ES6Iterator values() {
        return new NativeArrayIterator(getParentScope(), this, NativeArrayIterator.ARRAY_ITERATOR_TYPE.VALUES);
    }

    /**
     * Returns an Iterator allowing to go through all key/value pairs contained in this object.
     * @return an {@link NativeArrayIterator}
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public ES6Iterator entries() {
        return new NativeArrayIterator(getParentScope(), this, NativeArrayIterator.ARRAY_ITERATOR_TYPE.ENTRIES);
    }

    @JsxSymbol({CHROME, EDGE, FF, FF78})
    public ES6Iterator iterator() {
        return values();
    }

    /**
     * Calls the {@code callback} given in parameter once for each value pair in the list, in insertion order.
     * @param callback function to execute for each element
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public void forEach(final Object callback) {
        final List<DomNode> nodes = getElements();

        final WebClient client = getWindow().getWebWindow().getWebClient();
        final HtmlUnitContextFactory cf = ((JavaScriptEngine) client.getJavaScriptEngine()).getContextFactory();

        final ContextAction<Object> contextAction = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Function function = (Function) callback;
                final Scriptable scope = getParentScope();
                for (int i = 0; i < nodes.size(); i++) {
                    function.call(cx, scope, NodeList.this, new Object[] {
                            nodes.get(i).getScriptableObject(), i, NodeList.this});
                }
                return null;
            }
        };
        cf.call(contextAction);
    }
}
