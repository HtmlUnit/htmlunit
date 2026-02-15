/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code DOMStringMap}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public final class DOMStringMap extends HtmlUnitScriptable {

    private static final String DATA_PREFIX = "data-";

    /**
     * Creates an instance.
     */
    public DOMStringMap() {
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
     */
    public DOMStringMap(final Node node) {
        super();
        setDomNode(node.getDomNodeOrDie(), false);
        setParentScope(node.getParentScope());
        setPrototype(getPrototype(getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e != null) {
            final String value = e.getAttribute(DATA_PREFIX + deCamelize(name));
            if (ATTRIBUTE_NOT_DEFINED != value) {
                return value;
            }
        }
        return super.get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e == null) {
            super.put(name, start, value);
        }
        else {
            e.setAttribute("data-" + deCamelize(name), JavaScriptEngine.toString(value));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String name) {
        final HtmlElement e = (HtmlElement) getDomNodeOrNull();
        if (e == null) {
            super.delete(name);
        }
        else {
            e.removeAttribute("data-" + deCamelize(name));
        }
    }

    /**
     * Transforms the specified string from camel-cased to dash separated.
     *
     * @param string the string to decamelize
     * @return the transformed string
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/dataset#name_conversion">
     *      MDN - HTMLElement.dataset - Name conversion</a>
     */
    private static String deCamelize(final String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            if (Character.isUpperCase(ch)) {
                builder.append('-').append(Character.toLowerCase(ch));
            }
            else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

}
