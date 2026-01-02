/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.corejs.javascript.Context;
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
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.configuration.JsxSymbol;

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
     * @return the value
     */
    @JsxGetter
    public String getValue() {
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
     * @param value the new value
     */
    @JsxSetter
    public void setValue(final String value) {
        final DomNode node = getDomNodeOrNull();
        if (node != null) {
            updateAttribute(value);
        }
    }

    /**
     * Returns the length property.
     * @return the length
     */
    @JsxGetter
    public int getLength() {
        final String value = getValue();
        if (org.htmlunit.util.StringUtils.isBlank(value)) {
            return 0;
        }

        return split(value).size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue(final Class<?> hint) {
        if (getPrototype() == null) {
            return (String) super.getDefaultValue(hint);
        }

        final String value = getValue();
        if (value != null) {
            return String.join(" ", StringUtils.split(value, WHITESPACE_CHARS));
        }
        return "";
    }

    /**
     * Adds the given tokens to the list, omitting any that are already present.
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     */
    @JsxFunction
    public static void add(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (args.length > 0) {
            final DOMTokenList list = (DOMTokenList) thisObj;
            final List<String> parts = split(list.getValue());

            for (final Object arg : args) {
                final String token = JavaScriptEngine.toString(arg);

                if (org.htmlunit.util.StringUtils.isEmptyOrNull(token)) {
                    throw JavaScriptEngine.asJavaScriptException(
                            (HtmlUnitScriptable) getTopLevelScope(thisObj),
                            "DOMTokenList: add() does not support empty tokens",
                            DOMException.SYNTAX_ERR);
                }
                if (StringUtils.containsAny(token, WHITESPACE_CHARS)) {
                    throw JavaScriptEngine.asJavaScriptException(
                            (HtmlUnitScriptable) getTopLevelScope(thisObj),
                            "DOMTokenList: add() does not support whitespace chars in tokens",
                            DOMException.INVALID_CHARACTER_ERR);
                }

                if (!parts.contains(token)) {
                    parts.add(token);
                }
            }
            list.updateAttribute(String.join(" ", parts));
        }
    }

    /**
     * Removes the specified tokens from the underlying string.
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     */
    @JsxFunction
    public static void remove(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        final DOMTokenList list = (DOMTokenList) thisObj;

        final String value = list.getValue();
        if (value == null) {
            return;
        }

        if (args.length > 0) {
            final List<String> parts = split(list.getValue());

            for (final Object arg : args) {
                final String token = JavaScriptEngine.toString(arg);

                if (org.htmlunit.util.StringUtils.isEmptyOrNull(token)) {
                    throw JavaScriptEngine.asJavaScriptException(
                            (HtmlUnitScriptable) getTopLevelScope(thisObj),
                            "DOMTokenList: remove() does not support empty tokens",
                            DOMException.SYNTAX_ERR);
                }
                if (StringUtils.containsAny(token, WHITESPACE_CHARS)) {
                    throw JavaScriptEngine.asJavaScriptException(
                            (HtmlUnitScriptable) getTopLevelScope(thisObj),
                            "DOMTokenList: remove() does not support whitespace chars in tokens",
                            DOMException.INVALID_CHARACTER_ERR);
                }

                parts.remove(token);
            }
            list.updateAttribute(String.join(" ", parts));
        }
    }

    /**
     * Replaces an existing token with a new token. If the first token doesn't exist, replace()
     * returns false immediately, without adding the new token to the token list.
     * @param oldToken a string representing the token you want to replace
     * @param newToken a string representing the token you want to replace oldToken with
     * @return true if oldToken was successfully replaced, or false if not
     */
    @JsxFunction
    public boolean replace(final String oldToken, final String newToken) {
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(oldToken)) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "Empty oldToken not allowed",
                    DOMException.SYNTAX_ERR);
        }
        if (StringUtils.containsAny(oldToken, WHITESPACE_CHARS)) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "DOMTokenList: replace() oldToken contains whitespace",
                    DOMException.INVALID_CHARACTER_ERR);
        }

        if (org.htmlunit.util.StringUtils.isEmptyOrNull(newToken)) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "Empty newToken not allowed",
                    DOMException.SYNTAX_ERR);
        }
        if (StringUtils.containsAny(newToken, WHITESPACE_CHARS)) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "DOMTokenList: replace() newToken contains whitespace",
                    DOMException.INVALID_CHARACTER_ERR);
        }

        final String value = getValue();
        if (value == null) {
            return false;
        }
        final List<String> parts = split(value);
        final int pos = parts.indexOf(oldToken);
        if (pos == -1) {
            return false;
        }

        parts.set(pos, newToken);
        updateAttribute(String.join(" ", parts));
        return true;
    }

    /**
     * Toggle the token, by adding or removing.
     * @param token the token to add or remove
     * @return whether the string now contains the token or not
     */
    @JsxFunction
    public boolean toggle(final String token) {
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(token)) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "DOMTokenList: toggle() does not support empty tokens",
                    DOMException.SYNTAX_ERR);
        }
        if (StringUtils.containsAny(token, WHITESPACE_CHARS)) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "DOMTokenList: toggle() does not support whitespace chars in tokens",
                    DOMException.INVALID_CHARACTER_ERR);
        }

        final List<String> parts = split(getValue());
        if (parts.contains(token)) {
            parts.remove(token);
            updateAttribute(String.join(" ", parts));
            return false;
        }

        parts.add(token);
        updateAttribute(String.join(" ", parts));
        return true;
    }

    /**
     * Checks if the specified token is contained in the underlying string.
     * @param token the token to add
     * @return true if the underlying string contains token, otherwise false
     */
    @JsxFunction
    public boolean contains(final String token) {
        if (org.htmlunit.util.StringUtils.isBlank(token)) {
            return false;
        }

        if (org.htmlunit.util.StringUtils.isEmptyOrNull(token)) {
            throw JavaScriptEngine.reportRuntimeError("DOMTokenList: contains() does not support empty tokens");
        }
        if (StringUtils.containsAny(token, WHITESPACE_CHARS)) {
            throw JavaScriptEngine.reportRuntimeError(
                    "DOMTokenList: contains() does not support whitespace chars in tokens");
        }

        final List<String> parts = split(getValue());
        return parts.contains(token);
    }

    /**
     * Returns the item at the specified index.
     * @param index the index of the item
     * @return the item
     */
    @JsxFunction
    public String item(final int index) {
        if (index < 0) {
            return null;
        }

        final String value = getValue();
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(value)) {
            return null;
        }

        final List<String> parts = split(value);
        if (index < parts.size()) {
            return parts.get(index);
        }

        return null;
    }

    /**
     * Returns an Iterator allowing to go through all keys contained in this object.
     * @return a NativeArrayIterator
     */
    @JsxFunction
    public Scriptable keys() {
        return JavaScriptEngine.newArrayIteratorTypeKeys(getParentScope(), this);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Object[] getIds() {
        final Object[] normalIds = super.getIds();

        final String value = getValue();
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(value)) {
            return normalIds;
        }

        final List<String> parts = split(getValue());
        final Object[] ids = new Object[parts.size() + normalIds.length];
        final int size = parts.size();
        for (int i = 0; i < size; i++) {
            ids[i] = i;
        }
        System.arraycopy(normalIds, 0, ids, parts.size(), normalIds.length);

        return ids;
    }

    /**
     * Returns an Iterator allowing to go through all keys contained in this object.
     * @return a NativeArrayIterator
     */
    @JsxFunction
    @JsxSymbol(symbolName = "iterator")
    public Scriptable values() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }

    /**
     * Returns an Iterator allowing to go through all key/value pairs contained in this object.
     * @return a NativeArrayIterator
     */
    @JsxFunction
    public Scriptable entries() {
        return JavaScriptEngine.newArrayIteratorTypeEntries(getParentScope(), this);
    }

    /**
     * Calls the {@code callback} given in parameter once for each value in the list.
     * @param callback function to execute for each element
     */
    @JsxFunction
    public void forEach(final Object callback) {
        if (!(callback instanceof Function function)) {
            throw JavaScriptEngine.typeError(
                    "Foreach callback '" + JavaScriptEngine.toString(callback) + "' is not a function");
        }

        final String value = getValue();
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(value)) {
            return;
        }

        final WebClient client = getWindow().getWebWindow().getWebClient();
        final HtmlUnitContextFactory cf = client.getJavaScriptEngine().getContextFactory();

        final ContextAction<Object> contextAction = cx -> {
            final Scriptable scope = getParentScope();

            List<String> parts = split(value);
            final int size = parts.size();
            int i = 0;
            while (i < size && i < parts.size()) {
                function.call(cx, scope, this, new Object[] {parts.get(i), i, this});

                // refresh
                parts = split(getValue());
                i++;
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

    private static List<String> split(final String value) {
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(value)) {
            return new ArrayList<>();
        }

        final String[] parts = StringUtils.split(value, WHITESPACE_CHARS);

        // usually a short list, no index needed
        final List<String> result = new ArrayList<>();
        for (final String part : parts) {
            if (!result.contains(part)) {
                result.add(part);
            }
        }
        return result;
    }
}
