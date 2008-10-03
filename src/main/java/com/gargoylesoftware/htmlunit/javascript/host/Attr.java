/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.xml.XmlElement;

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
public class Attr extends SimpleScriptable {

    private static final long serialVersionUID = 3256441425892750900L;

    /**
     * The name of this attribute.
     */
    private String name_;

    /**
     * The value of this attribute, used only when this attribute is detached from
     * a parent HTML element (<tt>parent_</tt> is <tt>null</tt>).
     */
    private String value_;

    /**
     * The element to which this attribute belongs. May be <tt>null</tt> if
     * document.createAttribute() has been called but element.setAttributeNode()
     * has not been called yet, or if document.setAttributeNode() has been called
     * and this is the replaced attribute returned by said method.
     */
    private DomElement parent_;

    /**
     * Create an instance. JavaScript objects must have a default constructor.
     */
    public Attr() { }

    /**
     * Initializes this attribute.
     * @param name the name of the attribute
     * @param parent the parent HTML element
     */
    public void init(final String name, final DomElement parent) {
        name_ = name;
        parent_ = parent;
        if (parent_ == null) {
            value_ = "";
        }
    }

    /**
     * Ensures that all attributes are initialized correctly via {@link #init(String, HtmlElement)}.
     *
     * {@inheritDoc}
     */
    @Override
    protected void setDomNode(final DomNode domNode, final boolean assignScriptObject) {
        super.setDomNode(domNode, assignScriptObject);

        final String name = domNode.getNodeName();
        final DomElement parent = (DomElement) domNode.getParentNode();
        this.init(name, parent);
    }

    /**
     * Detaches this attribute from the parent HTML element after caching the attribute value.
     */
    public void detachFromParent() {
        if (parent_ != null) {
            if (parent_ instanceof HtmlElement) {
                value_ = ((HtmlElement) parent_).getAttributeValue(name_);
            }
            else {
                value_ = ((XmlElement) parent_).getAttributeValue(name_);
            }
        }
        parent_ = null;
    }

    /**
     * Returns <tt>true</tt> if arbitrary properties can be added to this attribute.
     * @return <tt>true</tt> if arbitrary properties can be added to this attribute
     */
    public boolean jsxGet_expando() {
        return true;
    }

    /**
     * Returns <code>null</code>.
     * @return <code>null</code>
     */
    public Object jsxGet_firstChild() {
        return null;
    }

    /**
     * Returns <code>null</code>.
     * @return <code>null</code>
     */
    public Object jsxGet_lastChild() {
        return null;
    }

    /**
     * Returns the name of the attribute.
     * @return the name of the attribute
     */
    public String jsxGet_name() {
        return name_;
    }

    /**
     * Returns <code>null</code>.
     * @return <code>null</code>
     */
    public Object jsxGet_nextSibling() {
        return null;
    }

    /**
     * Returns the name of this attribute.
     * @return the name of this attribute
     */
    public String jsxGet_nodeName() {
        return jsxGet_name();
    }

    /**
     * Returns the type of DOM node this attribute represents.
     * @return the type of DOM node this attribute represents
     */
    public int jsxGet_nodeType() {
        return org.w3c.dom.Node.ATTRIBUTE_NODE;
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute
     */
    public String jsxGet_nodeValue() {
        return jsxGet_value();
    }

    /**
     * Returns the containing document.
     * @return the containing document
     */
    public Object jsxGet_ownerDocument() {
        if (parent_ != null) {
            final SimpleScriptable documentScriptable = getScriptableFor(parent_.getPage());
            return documentScriptable;
        }
        return null;
    }

    /**
     * Returns <code>null</code>.
     * @return <code>null</code>
     */
    public Object jsxGet_parentNode() {
        return null;
    }

    /**
     * Returns <code>null</code>.
     * @return <code>null</code>
     */
    public Object jsxGet_previousSibling() {
        return null;
    }

    /**
     * Returns <tt>true</tt> if this attribute has been specified.
     * @return <tt>true</tt> if this attribute has been specified
     */
    public boolean jsxGet_specified() {
        if (parent_ != null) {
            if (parent_ instanceof HtmlElement) {
                return ((HtmlElement) parent_).isAttributeDefined(name_);
            }
            return ((XmlElement) parent_).hasAttribute(name_);
        }
        return true;
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute
     */
    public String jsxGet_value() {
        if (parent_ != null) {
            if (parent_ instanceof HtmlElement) {
                return ((HtmlElement) parent_).getAttributeValue(name_);
            }
            return ((XmlElement) parent_).getAttributeValue(name_);
        }
        return value_;
    }

    /**
     * Sets the value of this attribute.
     * @param value the new value of this attribute
     */
    public void jsxSet_value(final String value) {
        if (parent_ != null) {
            if (parent_ instanceof HtmlElement) {
                ((HtmlElement) parent_).setAttributeValue(name_, value);
            }
            ((XmlElement) parent_).setAttributeValue(name_, value);
        }
        else {
            value_ = value;
        }
    }
}
