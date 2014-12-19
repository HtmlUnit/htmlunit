/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_OBJECT_DETECTION;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;

/**
 * A special {@link HTMLCollection}for document.all.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@JsxClass
public class HTMLAllCollection extends HTMLCollection {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     * Don't call.
     */
    @Deprecated
    public HTMLAllCollection() {
        // Empty.
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     * @param description a text useful for debugging
     */
    public HTMLAllCollection(final DomNode parentScope, final String description) {
        super(parentScope, false, description);
    }

    @JsxFunction
    @Override
    public final Object namedItem(final String name) {
        final List<Object> elements = getElements();

        // See if there is an element in the element array with the specified id.
        final List<DomElement> matchingByName = new ArrayList<DomElement>();
        final List<DomElement> matchingById = new ArrayList<DomElement>();

        for (final Object next : elements) {
            if (next instanceof DomElement) {
                final DomElement elem = (DomElement) next;
                final String nodeName = elem.getAttribute("name");
                if (name.equals(nodeName)) {
                    matchingByName.add(elem);
                }
                else {
                    final String id = elem.getAttribute("id");
                    if (name.equals(id)) {
                        matchingById.add(elem);
                    }
                }
            }
        }
        matchingByName.addAll(matchingById);

        if (matchingByName.size() == 1
                || (matchingByName.size() > 1
                        && getBrowserVersion().hasFeature(HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS))) {
            return getScriptableForElement(matchingByName.get(0));
        }
        if (matchingByName.isEmpty()) {
            if (getBrowserVersion().hasFeature(HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND)) {
                return null;
            }
            return Context.getUndefinedValue();
        }

        // many elements => build a sub collection
        final DomNode domNode = getDomNodeOrNull();
        final HTMLCollection collection = new HTMLCollection(domNode, matchingByName);
        collection.setAvoidObjectDetection(!getBrowserVersion().hasFeature(HTMLCOLLECTION_OBJECT_DETECTION));
        return collection;
    }
}
