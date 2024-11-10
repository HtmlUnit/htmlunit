/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.host.dom.RadioNodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * A JavaScript object for {@code HTMLFormControlsCollection}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
@JsxClass
public class HTMLFormControlsCollection extends HTMLCollection {

    /**
     * Creates an instance.
     */
    public HTMLFormControlsCollection() {
        super();
    }

    /**
     * Creates an instance.
     * @param domNode parent scope
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     */
    public HTMLFormControlsCollection(final DomNode domNode, final boolean attributeChangeSensitive) {
        super(domNode, attributeChangeSensitive);
    }

    /**
     * Constructs an instance with an initial cache value.
     * @param domNode the parent scope, on which we listen for changes
     * @param initialElements the initial content for the cache
     */
    HTMLFormControlsCollection(final DomNode domNode, final List<DomNode> initialElements) {
        super(domNode, initialElements);
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the element with ID or name match the specified value from the collection.
     * If there are multiple matching elements, then a RadioNodeList object containing all those elements is returned.
     * @param name the name or id the element or elements to return
     * @return the element or elements corresponding to the specified name or id
     * @see <a href="https://html.spec.whatwg.org/multipage/common-dom-interfaces.html#the-htmlformcontrolscollection-interface">HTML Standard</a>
     */
    @Override
    @JsxFunction
    public Scriptable namedItem(final String name) {
        if (name.isEmpty()) {
            return null;
        }

        final List<DomNode> elements = new ArrayList<>();
        for (final Object next : getElements()) {
            if (next instanceof DomElement) {
                final DomElement elem = (DomElement) next;
                final String nodeName = elem.getAttributeDirect(DomElement.NAME_ATTRIBUTE);
                if (name.equals(nodeName)) {
                    elements.add(elem);
                    continue;
                }

                final String id = elem.getId();
                if (name.equals(id)) {
                    elements.add(elem);
                }
            }
        }

        if (elements.isEmpty()) {
            return null;
        }
        if (elements.size() == 1) {
            return getScriptableForElement(elements.get(0));
        }

        final RadioNodeList nodeList = new RadioNodeList(getDomNodeOrDie(), elements);
        nodeList.setElementsSupplier(getElementSupplier());
        return nodeList;
    }

    @JsxSymbol
    @Override
    public Scriptable iterator() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
