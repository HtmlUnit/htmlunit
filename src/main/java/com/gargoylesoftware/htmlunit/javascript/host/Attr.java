/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;

/**
 * A JavaScript object for an Attribute.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-63764602">W3C DOM Level 2</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535187.aspx">MSDN documentation</a>
 * @version $Revision$
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 */
public class Attr extends Node {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Attr() { }

    /**
     * Detaches this attribute from the parent HTML element after caching the attribute value.
     */
    public void detachFromParent() {
        final DomAttr domNode = getDomNodeOrDie();
        final DomElement parent = (DomElement) domNode.getParentNode();
        if (parent != null) {
            domNode.setValue(parent.getAttribute(jsxGet_name()));
        }
        domNode.remove();
    }

    /**
     * Returns <tt>true</tt> if this attribute is an ID.
     * @return <tt>true</tt> if this attribute is an ID
     */
    public boolean jsxGet_isId() {
        return getDomNodeOrDie().isId();
    }

    /**
     * Returns <tt>true</tt> if arbitrary properties can be added to this attribute.
     * @return <tt>true</tt> if arbitrary properties can be added to this attribute
     */
    public boolean jsxGet_expando() {
        return true;
    }

    /**
     * Returns the name of the attribute.
     * @return the name of the attribute
     */
    public String jsxGet_name() {
        return getDomNodeOrDie().getName();
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute
     */
    @Override
    public String jsxGet_nodeValue() {
        return jsxGet_value();
    }

    /**
     * Returns the owner element.
     * @return the owner element
     */
    public Object jsxGet_ownerElement() {
        final DomElement parent = getDomNodeOrDie().getOwnerElement();
        if (parent != null) {
            return parent.getScriptObject();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @return <code>null</code>
     */
    @Override
    public Node jsxGet_parentNode() {
        return null;
    }

    /**
     * Returns <tt>true</tt> if this attribute has been specified.
     * @return <tt>true</tt> if this attribute has been specified
     */
    public boolean jsxGet_specified() {
        return getDomNodeOrDie().getSpecified();
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute
     */
    public String jsxGet_value() {
        return getDomNodeOrDie().getValue();
    }

    /**
     * Sets the value of this attribute.
     * @param value the new value of this attribute
     */
    public void jsxSet_value(final String value) {
        getDomNodeOrDie().setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node jsxGet_firstChild() {
        return jsxGet_lastChild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node jsxGet_lastChild() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_151)) {
            final DomText text = new DomText(getDomNodeOrDie().getPage(), jsxGet_nodeValue());
            return (Node) text.getScriptObject();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomAttr getDomNodeOrDie() throws IllegalStateException {
        return super.getDomNodeOrDie();
    }
}
