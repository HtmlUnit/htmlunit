/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for DOMTokenList.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass
public final class DOMTokenList extends SimpleScriptable {

    private String attributeName_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public DOMTokenList() {
    }

    /**
     * Creates an instance.
     * @param node the node which contains the underlying string
     * @param attributeName the attribute name of the DomElement of the specified node
     */
    public DOMTokenList(final Node node, final String attributeName) {
        setDomNode(node.getDomNodeOrDie(), false);
        setParentScope(node.getParentScope());
        setPrototype(getPrototype(getClass()));
        attributeName_ = attributeName;
    }

    /**
     * Returns the length property.
     * @return the length
     */
    @JsxGetter
    public int jsxGet_length() {
        final String value = getDefaultValue(null);
        return value.split(" ").length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue(final Class<?> hint) {
        final DomAttr attr = (DomAttr) getDomNodeOrDie().getAttributes().getNamedItem(attributeName_);
        if (attr != null) {
            final String value = attr.getValue();
            return value;
        }
        return "";
    }

    /**
     * Adds the specified token to the underlying string.
     * @param token the token to add
     */
    @JsxFunction
    public void jsxFunction_add(final String token) {
        if (!jsxFunction_contains(token)) {
            final DomAttr attr = (DomAttr) getDomNodeOrDie().getAttributes().getNamedItem(attributeName_);
            attr.setValue(attr.getValue() + ' ' + token);
        }
    }

    /**
     * Removes the specified token from the underlying string.
     * @param token the token to remove
     */
    @JsxFunction
    public void jsxFunction_remove(final String token) {
        if (jsxFunction_contains(token)) {
            final DomAttr attr = (DomAttr) getDomNodeOrDie().getAttributes().getNamedItem(attributeName_);
            if (attr != null) {
                final List<String> values = new ArrayList<String>(Arrays.asList(attr.getValue().split(" ")));
                values.remove(token);
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < values.size(); i++) {
                    builder.append(values.get(i));
                    if (i < values.size() - 1) {
                        builder.append(' ');
                    }
                }
                attr.setValue(builder.toString());
            }
        }
    }

    /**
     * Toggle the token, by adding or removing.
     * @param token the token to add or remove
     * @return whether the string now contains the token or not
     */
    @JsxFunction
    public boolean jsxFunction_toggle(final String token) {
        if (jsxFunction_contains(token)) {
            jsxFunction_remove(token);
            return false;
        }
        jsxFunction_add(token);
        return true;
    }

    /**
     * Checks if the specified token is contained in the underlying string.
     * @param token the token to add
     * @return true if the underlying string contains token, otherwise false
     */
    @JsxFunction
    public boolean jsxFunction_contains(final String token) {
        final DomAttr attr = (DomAttr) getDomNodeOrDie().getAttributes().getNamedItem(attributeName_);
        if (attr != null) {
            final List<String> values = Arrays.asList(attr.getValue().split(" "));
            return values.contains(token);
        }
        return false;
    }

    /**
     * Returns the item at the specified index.
     * @param index the index of the item
     * @return the item
     */
    @JsxFunction
    public Object jsxFunction_item(final int index) {
        final DomAttr attr = (DomAttr) getDomNodeOrDie().getAttributes().getNamedItem(attributeName_);
        if (attr != null) {
            final List<String> values = Arrays.asList(attr.getValue().split(" "));
            if (index < values.size()) {
                return values.get(index);
            }
        }
        return null;
    }
}
