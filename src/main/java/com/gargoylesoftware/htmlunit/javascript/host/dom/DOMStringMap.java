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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for DOMStringMap.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass
public final class DOMStringMap extends SimpleScriptable {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public DOMStringMap() {
    }

    /**
     * Creates an instance.
     * @param node the node which contains the underlying string
     */
    public DOMStringMap(final Node node) {
        setDomNode(node.getDomNodeOrDie(), false);
        setParentScope(node.getParentScope());
        setPrototype(getPrototype(getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        final HtmlElement e = getDomNodeOrNull();
        if (e != null) {
            final String value = e.getAttribute("data-" + decamelize(name));
            if (value != DomElement.ATTRIBUTE_NOT_DEFINED) {
                return value;
            }
        }
        return NOT_FOUND;
    }

    /**
     * {@inheritDoc}
     */
    public void put(final String name, final Scriptable start, final Object value) {
        if (!(ScriptableObject.getTopLevelScope(this) instanceof Window) || getWindow().getWebWindow() == null) {
            super.put(name, start, value);
        }
        else {
            final HtmlElement e = getDomNodeOrNull();
            e.setAttribute("data-" + decamelize(name), Context.toString(value));
        }
    }

    /**
     * Transforms the specified string from camel-cased (e.g. <tt>fontSize</tt>)
     * to delimiter-separated (e.g. <tt>font-size</tt>).
     * to camel-cased .
     * @param string the string to decamelize
     * @return the transformed string
     * @see com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration#camelize
     */
    public static String decamelize(final String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            if (Character.isUpperCase(ch)) {
                buffer.append('-').append(Character.toLowerCase(ch));
            }
            else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
}
