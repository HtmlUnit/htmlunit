/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF60;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF68;

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
import com.gargoylesoftware.htmlunit.javascript.host.Iterator;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
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

    private static final String ITERATOR_NAME = "Iterator";
    private static Iterator ITERATOR_PROTOTYPE_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, FF, FF68, FF60})
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
    private NodeList(final ScriptableObject parentScope) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
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
     * Returns an {@link Iterator} allowing to go through all keys contained in this object.
     * @return an {@link Iterator}
     */
    @JsxFunction({CHROME, FF, FF68, FF60})
    public Iterator keys() {
        final int length = getElements().size();
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(i);
        }
        final Iterator object = new Iterator(ITERATOR_NAME, list.iterator());
        object.setParentScope(getParentScope());
        setIteratorPrototype(object);
        return object;
    }

    /**
     * Returns an {@link Iterator} allowing to go through all keys contained in this object.
     * @return an {@link Iterator}
     */
    @JsxFunction({CHROME, FF, FF68, FF60})
    public Iterator values() {
        final List<DomNode> list = getElements();
        final Iterator object = new Iterator(ITERATOR_NAME, list.iterator());
        object.setParentScope(getParentScope());
        setIteratorPrototype(object);
        return object;
    }

    /**
     * Returns an {@link Iterator} allowing to go through all key/value pairs contained in this object.
     * @return an {@link Iterator}
     */
    @JsxFunction({CHROME, FF, FF68, FF60})
    public Iterator entries() {
        final List<DomNode> elements = getElements();
        final Context context = Context.getCurrentContext();
        final Scriptable scope = getParentScope();

        final List<Scriptable> list = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            final Object[] array = new Object[] {i, elements.get(i).getScriptableObject()};
            list.add(context.newArray(scope, array));
        }
        final Iterator object = new Iterator(ITERATOR_NAME, list.iterator());
        object.setParentScope(scope);
        setIteratorPrototype(object);
        return object;
    }

    private static void setIteratorPrototype(final Scriptable scriptable) {
        if (ITERATOR_PROTOTYPE_ == null) {
            ITERATOR_PROTOTYPE_ = new Iterator(ITERATOR_NAME, null);
        }
        scriptable.setPrototype(ITERATOR_PROTOTYPE_);
    }

    /**
     * Calls the {@code callback} given in parameter once for each value pair in the list, in insertion order.
     * @param callback function to execute for each element
     */
    @JsxFunction({CHROME, FF, FF68, FF60})
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
