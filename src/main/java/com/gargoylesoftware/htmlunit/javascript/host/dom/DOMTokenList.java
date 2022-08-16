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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOMTOKENLIST_CONTAINS_RETURNS_FALSE_FOR_BLANK;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOMTOKENLIST_ENHANCED_WHITESPACE_CHARS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOMTOKENLIST_GET_NULL_IF_OUTSIDE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOMTOKENLIST_LENGTH_IGNORES_DUPLICATES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_ADD;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_REMOVE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code DOMTokenList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marek Gawlicki
 */
@JsxClass
public class DOMTokenList extends HtmlUnitScriptable {

    private static final String WHITESPACE_CHARS = " \t\r\n\u000C";
    private static final String WHITESPACE_CHARS_IE_11 = WHITESPACE_CHARS + "\u000B";

    private String attributeName_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
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
    public int getLength() {
        final String value = getDefaultValue(null);
        final String[] parts = StringUtils.split(value, whitespaceChars());
        if (getBrowserVersion().hasFeature(JS_DOMTOKENLIST_LENGTH_IGNORES_DUPLICATES)) {
            final HashSet<String> elements = new HashSet<>(parts.length);
            elements.addAll(Arrays.asList(parts));
            return elements.size();
        }
        return parts.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue(final Class<?> hint) {
        if (getPrototype() == null) {
            return (String) super.getDefaultValue(hint);
        }
        final DomNode node = getDomNodeOrNull();
        if (node != null) {
            final DomAttr attr = (DomAttr) node.getAttributes().getNamedItem(attributeName_);
            if (attr != null) {
                return String.join(" ", StringUtils.split(attr.getValue(), whitespaceChars()));
            }
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
            throw Context.reportRuntimeError("Empty input not allowed");
        }
        if (StringUtils.containsAny(token, whitespaceChars())) {
            throw Context.reportRuntimeError("Empty input not allowed");
        }

        String value = getDefaultValue(null);
        boolean changed = false;
        if (position(value, token) < 0) {
            if (value.length() != 0 && !isWhitespace(value.charAt(value.length() - 1))) {
                value = value + " ";
            }
            value = value + token;
            changed = true;
        }
        else if (getBrowserVersion().hasFeature(JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_ADD)) {
            value = String.join(" ", StringUtils.split(value, whitespaceChars()));
            changed = true;
        }

        if (changed) {
            updateAttribute(value);
        }
    }

    /**
     * Removes the specified token from the underlying string.
     * @param token the token to remove
     */
    @JsxFunction
    public void remove(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw Context.reportRuntimeError("Empty input not allowed");
        }
        if (StringUtils.containsAny(token, whitespaceChars())) {
            throw Context.reportRuntimeError("Empty input not allowed");
        }

        String value = getDefaultValue(null);
        boolean changed = false;
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
            changed = true;

            pos = position(value, token);
        }

        if (getBrowserVersion().hasFeature(JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_REMOVE)) {
            value = String.join(" ", StringUtils.split(value, whitespaceChars()));
            changed = true;
        }

        if (changed) {
            updateAttribute(value);
        }
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
        if (getBrowserVersion().hasFeature(JS_DOMTOKENLIST_CONTAINS_RETURNS_FALSE_FOR_BLANK)
                && StringUtils.isBlank(token)) {
            return false;
        }

        if (StringUtils.isEmpty(token)) {
            throw Context.reportRuntimeError("Empty input not allowed");
        }
        if (StringUtils.containsAny(token, whitespaceChars())) {
            throw Context.reportRuntimeError("Empty input not allowed");
        }
        return position(getDefaultValue(null), token) > -1;
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
        final String value = getDefaultValue(null);
        final String[] values = StringUtils.split(value, whitespaceChars());
        if (index < values.length) {
            return values[index];
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        final Object value = item(index);
        if (value == null && !getBrowserVersion().hasFeature(JS_DOMTOKENLIST_GET_NULL_IF_OUTSIDE)) {
            return Undefined.instance;
        }
        return value;
    }

    private void updateAttribute(final String value) {
        final DomElement domNode = (DomElement) getDomNodeOrDie();
        DomAttr attr = (DomAttr) domNode.getAttributes().getNamedItem(attributeName_);
        if (null == attr) {
            attr = domNode.getPage().createAttribute(attributeName_);
        }
        attr.setValue(value);
        domNode.setAttributeNode(attr);
    }

    private int position(final String value, final String token) {
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

    private String whitespaceChars() {
        if (getBrowserVersion().hasFeature(JS_DOMTOKENLIST_ENHANCED_WHITESPACE_CHARS)) {
            return WHITESPACE_CHARS_IE_11;
        }
        return WHITESPACE_CHARS;
    }

    private boolean isWhitespace(final int ch) {
        return whitespaceChars().indexOf(ch) > -1;
    }
}
