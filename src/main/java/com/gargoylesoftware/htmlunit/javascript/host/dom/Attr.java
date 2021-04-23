/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ATTR_FIRST_LAST_CHILD_RETURNS_NULL;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for {@code Attr}.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-63764602">W3C DOM Level 2</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535187.aspx">MSDN documentation</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = DomAttr.class)
public class Attr extends Node {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public Attr() {
    }

    /**
     * Detaches this attribute from the parent HTML element after caching the attribute value.
     */
    public void detachFromParent() {
        final DomAttr domNode = getDomNodeOrDie();
        final DomElement parent = (DomElement) domNode.getParentNode();
        if (parent != null) {
            domNode.setValue(parent.getAttribute(getName()));
        }
        domNode.remove();
    }

    /**
     * Returns {@code true} if the attribute is a custom property.
     * @return {@code true} if the attribute is a custom property
     */
    @JsxGetter(IE)
    public boolean isExpando() {
        final Object owner = getOwnerElement();
        if (null == owner) {
            return false;
        }
        return !ScriptableObject.hasProperty((Scriptable) owner, getName());
    }

    /**
     * Returns the name of the attribute.
     * @return the name of the attribute
     */
    @JsxGetter
    public String getName() {
        return getDomNodeOrDie().getName();
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute
     */
    @Override
    public String getNodeValue() {
        return getValue();
    }

    /**
     * Returns the owner element.
     * @return the owner element
     */
    @JsxGetter
    public Object getOwnerElement() {
        final DomElement parent = getDomNodeOrDie().getOwnerElement();
        if (parent != null) {
            return parent.getScriptableObject();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @return {@code null}
     */
    @Override
    public Node getParentNode() {
        return null;
    }

    /**
     * Returns {@code true} if this attribute has been specified.
     * @return {@code true} if this attribute has been specified
     */
    @JsxGetter
    public boolean isSpecified() {
        return getDomNodeOrDie().getSpecified();
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute
     */
    @JsxGetter
    public String getValue() {
        return getDomNodeOrDie().getValue();
    }

    /**
     * Sets the value of this attribute.
     * @param value the new value of this attribute
     */
    @JsxSetter
    public void setValue(final String value) {
        getDomNodeOrDie().setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getFirstChild() {
        return getLastChild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getLastChild() {
        if (getBrowserVersion().hasFeature(JS_ATTR_FIRST_LAST_CHILD_RETURNS_NULL)) {
            return null;
        }

        final DomText text = new DomText(getDomNodeOrDie().getPage(), getNodeValue());
        return (Node) text.getScriptableObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr getDomNodeOrDie() {
        return (DomAttr) super.getDomNodeOrDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Object getPrefix() {
        return super.getPrefix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Object getLocalName() {
        return super.getLocalName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Object getNamespaceURI() {
        return super.getNamespaceURI();
    }

}
