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
package org.htmlunit.javascript.host.dom;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code DOMTokenList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marek Gawlicki
 * @author Markus Winter
 */
@JsxClass
public class DOMTokenList extends HtmlUnitScriptable {

    private static final String WHITESPACE_CHARS = " \t\r\n\u000C";

    private String attributeName_;

    /**
     * Creates an instance.
     */
    public DOMTokenList() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Creates an instance.
     * @param node the node which contains the underlying string
     * @param attributeName the attribute name of the DomElement of the specified node
     */
    public DOMTokenList(final Node node, final String attributeName) {
        super();
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
    public int getLength() {
        final String value = getAttribValue();
        if (StringUtils.isBlank(value)) {
            return 0;
        }

        final String[] parts = StringUtils.split(value, WHITESPACE_CHARS);
        final HashSet<String> elements = new HashSet<>(parts.length);
        elements.addAll(Arrays.asList(parts));
        return elements.size();
    }

    private String getAttribValue() {
        final DomNode node = getDomNodeOrNull();
        if (node != null) {
            final DomAttr attr = (DomAttr) node.getAttributes().getNamedItem(attributeName_);
            if (attr != null) {
                return attr.getValue();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue(final Class<?> hint) {
        if (getPrototype() == null) {
            return (String) super.getDefaultValue(hint);
        }

        final String value = getAttribValue();
        if (value != null) {
            return String.join(" ", StringUtils.split(value, WHITESPACE_CHARS));
        }
        return "";
    }

    /**
     * Adds the specified token to the underlying string.
     * @param token the token to add
     */
    @JsxFunction
    public void add(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw JavaScriptEngine.reportRuntimeError("Empty input not allowed");
        }
        if (StringUtils.containsAny(token, WHITESPACE_CHARS)) {
            throw JavaScriptEngine.reportRuntimeError("Empty input not allowed");
        }

        String value = getAttribValue();
        if (StringUtils.isEmpty(value)) {
            value = token;
        }
        else {
            value = String.join(" ", StringUtils.split(value, WHITESPACE_CHARS));
            if (position(value, token) < 0) {
                if (value.length() != 0 && !isWhitespace(value.charAt(value.length() - 1))) {
                    value = value + " ";
                }
                value = value + token;
            }
            else {
                value = String.join(" ", StringUtils.split(value, WHITESPACE_CHARS));
            }
        }

        updateAttribute(value);
    }

    /**
     * Removes the specified token from the underlying string.
     * @param token the token to remove
     */
    @JsxFunction
    public void remove(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw JavaScriptEngine.reportRuntimeError("Empty input not allowed");
        }
        if (StringUtils.containsAny(token, WHITESPACE_CHARS)) {
            throw JavaScriptEngine.reportRuntimeError("Empty input not allowed");
        }

        final String oldValue = getAttribValue();
        if (oldValue == null) {
            return;
        }

        String value = String.join(" ", StringUtils.split(oldValue, WHITESPACE_CHARS));
        int pos = position(value, token);
        while (pos != -1) {
            int from = pos;
            int to = pos + token.length();

            while (from > 0 && isWhitespace(value.charAt(from - 1))) {
                from = from - 1;
            }
            while (to < value.length() - 1 && isWhitespace(value.charAt(to))) {
                to = to + 1;
            }

            final StringBuilder result = new StringBuilder();
            if (from > 0) {
                result.append(value, 0, from);
                if (to < value.length()) {
                    result.append(' ');
                }
            }
            result.append(value, to, value.length());
            value = result.toString();

            pos = position(value, token);
        }

        value = String.join(" ", StringUtils.split(value, WHITESPACE_CHARS));
        updateAttribute(value);
    }

    /**
     * Toggle the token, by adding or removing.
     * @param token the token to add or remove
     * @return whether the string now contains the token or not
     */
    @JsxFunction
    public boolean toggle(final String token) {
        if (contains(token)) {
            remove(token);
            return false;
        }
        add(token);
        return true;
    }

    /**
     * Checks if the specified token is contained in the underlying string.
     * @param token the token to add
     * @return true if the underlying string contains token, otherwise false
     */
    @JsxFunction
    public boolean contains(final String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }

        if (StringUtils.isEmpty(token)) {
            throw JavaScriptEngine.reportRuntimeError("Empty input not allowed");
        }
        if (StringUtils.containsAny(token, WHITESPACE_CHARS)) {
            throw JavaScriptEngine.reportRuntimeError("Empty input not allowed");
        }

        String value = getAttribValue();
        if (StringUtils.isEmpty(value)) {
            return false;
        }

        value = String.join(" ", StringUtils.split(value, WHITESPACE_CHARS));
        return position(value, token) > -1;
    }

    /**
     * Returns the item at the specified index.
     * @param index the index of the item
     * @return the item
     */
    @JsxFunction
    public Object item(final int index) {
        if (index < 0) {
            return null;
        }

        final String value = getAttribValue();
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        final String[] values = StringUtils.split(value, WHITESPACE_CHARS);
        if (index < values.length) {
            return values[index];
        }

        return null;
    }

    /**
     * Calls the {@code callback} given in parameter once for each value in the list.
     * @param callback function to execute for each element
     */
    @JsxFunction
    public void forEach(final Object callback) {
        if (!(callback instanceof Function)) {
            throw JavaScriptEngine.typeError(
                    "Foreach callback '" + JavaScriptEngine.toString(callback) + "' is not a function");
        }

        final String value = getAttribValue();
        if (StringUtils.isEmpty(value)) {
            return;
        }

        final WebClient client = getWindow().getWebWindow().getWebClient();
        final HtmlUnitContextFactory cf = client.getJavaScriptEngine().getContextFactory();

        final ContextAction<Object> contextAction = cx -> {
            final Function function = (Function) callback;
            final Scriptable scope = getParentScope();
            final String[] values = StringUtils.split(value, WHITESPACE_CHARS);
            for (int i = 0; i < values.length; i++) {
                function.call(cx, scope, this, new Object[] {values[i], i, this});
            }
            return null;
        };
        cf.call(contextAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        final Object value = item(index);
        if (value == null) {
            return JavaScriptEngine.UNDEFINED;
        }
        return value;
    }

    private void updateAttribute(final String value) {
        final DomElement domNode = (DomElement) getDomNodeOrDie();

        // always create a new one because the old one is used later for the mutation observer
        // to get the old value from
        final DomAttr attr = domNode.getPage().createAttribute(attributeName_);
        attr.setValue(value);
        domNode.setAttributeNode(attr);
    }

    private static int position(final String value, final String token) {
        final int pos = value.indexOf(token);
        if (pos < 0) {
            return -1;
        }

        // whitespace before
        if (pos != 0 && !isWhitespace(value.charAt(pos - 1))) {
            return -1;
        }

        // whitespace after
        final int end = pos + token.length();
        if (end != value.length() && !isWhitespace(value.charAt(end))) {
            return -1;
        }
        return pos;
    }

    private static boolean isWhitespace(final int ch) {
        return WHITESPACE_CHARS.indexOf(ch) > -1;
    }
}
