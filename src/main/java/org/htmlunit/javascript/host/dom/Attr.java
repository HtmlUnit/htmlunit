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

import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomElement;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

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
    public Attr() {
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
    public HtmlUnitScriptable getOwnerElement() {
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
        return null;
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

    /**
     * Returns the owner document.
     * @return the document
     */
    @Override
    public Object getRootNode() {
        return this;
    }
}
