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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSymbol;

import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ES6Iterator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArrayIterator;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * An array of elements. Used for the element arrays returned by <code>document.all</code>,
 * <code>document.all.tags('x')</code>, <code>document.forms</code>, <code>window.frames</code>, etc.
 * Note that this class must not be used for collections that can be modified, for example
 * <code>map.areas</code> and <code>select.options</code>.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class NodeList extends AbstractList implements Callable {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
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
        super(domNode, attributeChangeSensitive, null);
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param domNode the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    public NodeList(final DomNode domNode, final List<DomNode> initialElements) {
        super(domNode, true, new ArrayList<>(initialElements));
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
    @JsxFunction({CHROME, EDGE, FF, FF_ESR})
    public ES6Iterator keys() {
        return new NativeArrayIterator(getParentScope(), this, NativeArrayIterator.ARRAY_ITERATOR_TYPE.KEYS);
    }

    /**
     * Returns an Iterator allowing to go through all keys contained in this object.
     * @return an {@link NativeArrayIterator}
     */
    @JsxFunction({CHROME, EDGE, FF, FF_ESR})
    public ES6Iterator values() {
        return new NativeArrayIterator(getParentScope(), this, NativeArrayIterator.ARRAY_ITERATOR_TYPE.VALUES);
    }

    /**
     * Returns an Iterator allowing to go through all key/value pairs contained in this object.
     * @return an {@link NativeArrayIterator}
     */
    @JsxFunction({CHROME, EDGE, FF, FF_ESR})
    public ES6Iterator entries() {
        return new NativeArrayIterator(getParentScope(), this, NativeArrayIterator.ARRAY_ITERATOR_TYPE.ENTRIES);
    }

    @JsxSymbol({CHROME, EDGE, FF, FF_ESR})
    public ES6Iterator iterator() {
        return values();
    }

    /**
     * Calls the {@code callback} given in parameter once for each value pair in the list, in insertion order.
     * @param callback function to execute for each element
     */
    @JsxFunction({CHROME, EDGE, FF, FF_ESR})
    public void forEach(final Object callback) {
        final List<DomNode> nodes = getElements();

        final WebClient client = getWindow().getWebWindow().getWebClient();
        final HtmlUnitContextFactory cf = ((JavaScriptEngine) client.getJavaScriptEngine()).getContextFactory();

        final ContextAction<Object> contextAction = cx -> {
            final Function function = (Function) callback;
            final Scriptable scope = getParentScope();
            for (int i = 0; i < nodes.size(); i++) {
                function.call(cx, scope, NodeList.this, new Object[] {
                        nodes.get(i).getScriptableObject(), i, NodeList.this});
            }
            return null;
        };
        cf.call(contextAction);
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
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536460.aspx">MSDN doc</a>
     */
    @JsxFunction
    public Object item(final Object index) {
        final Object object = getIt(index);
        if (object == NOT_FOUND) {
            return null;
        }
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractList create(final DomNode parentScope, final List<DomNode> initialElements) {
        return new NodeList(parentScope, new ArrayList<>(initialElements));
    }
}
