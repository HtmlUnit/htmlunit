/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for an Attribute.
 * 
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-63764602">W3C DOM Level 2</a>
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/attribute.asp">MSDN documentation</a>
 * @version $Revision$
 * @author Daniel Gredler
 */
public class Attribute extends SimpleScriptable {

    /**
     * The name of the JavaScript object corresponding to this class as registered
     * by the JavaScript context.
     */
    public static final String JS_OBJECT_NAME = "Attribute";

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
     * The HTML element to which this attribute belongs. May be <tt>null</tt> if
     * document.createAttribute() has been called but element.setAttributeNode()
     * has not been called yet, or if document.setAttributeNode() has been called
     * and this is the replaced attribute returned by said method.
     */
    private HtmlElement parent_;

    /**
     * Create an instance. Javascript objects must have a default constructor.
     */
    public Attribute() {}

    /**
     * Initializes this attribute.
     * @param name the name of the attribute.
     * @param parent the parent html element.
     */
    public void init(final String name, final HtmlElement parent) {
        name_ = name;
        parent_ = parent;
        if(parent_ == null) {
            value_ = "";
        }
    }

    /**
     * Detaches this attribute from the parent HTML element after caching the attribute value.
     */
    public void detachFromParent() {
        if(parent_ != null) {
            value_ = parent_.getAttributeValue(name_);
        }
        parent_ = null;
    }

    /**
     * Returns <tt>true</tt> if arbitrary properties can be added to this attribute.
     * @return <tt>true</tt> if arbitrary properties can be added to this attribute.
     */
    public boolean jsGet_expando() {
        return true;
    }

    /**
     * Returns <code>null</code>
     * @return <code>null</code>
     */
    public Object jsGet_firstChild() {
        return null;
    }

    /**
     * Returns <code>null</code>
     * @return <code>null</code>
     */
    public Object jsGet_lastChild() {
        return null;
    }

    /**
     * Returns the name of the attribute.
     * @return the name of the attribute.
     */
    public String jsGet_name() {
        return name_;
    }

    /**
     * Returns <code>null</code>
     * @return <code>null</code>
     */
    public Object jsGet_nextSibling() {
        return null;
    }

    /**
     * Returns the name of this attribute.
     * @return the name of this attribute.
     */
    public String jsGet_nodeName() {
        return jsGet_name();
    }

    /**
     * Returns the type of DOM node this attribute represents.
     * @return the type of DOM node this attribute represents.
     */
    public int jsGet_nodeType() {
        return 2;
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute.
     */
    public String jsGet_nodeValue() {
        return jsGet_value();
    }

    /**
     * Returns the containing document.
     * @return the containing document.
     */
    public Object jsGet_ownerDocument() {
        if(parent_ != null) {
            final SimpleScriptable documentScriptable = getScriptableFor(parent_.getPage());
            return documentScriptable;
        }
        else {
            return null;
        }
    }

    /**
     * Returns <code>null</code>
     * @return <code>null</code>
     */
    public Object jsGet_parentNode() {
        return null;
    }

    /**
     * Returns <code>null</code>
     * @return <code>null</code>
     */
    public Object jsGet_previousSibling() {
        return null;
    }

    /**
     * Returns <tt>true</tt> if this attribute has been specified.
     * @return <tt>true</tt> if this attribute has been specified.
     */
    public boolean jsGet_specified() {
        return true;
    }

    /**
     * Returns the value of this attribute.
     * @return the value of this attribute.
     */
    public String jsGet_value() {
        if(parent_ != null) {
            return parent_.getAttributeValue(name_);
        }
        else {
            return value_;
        }
    }

    /**
     * Sets the value of this attribute.
     * @param value the new value of this attribute.
     */
    public void jsSet_value(final String value) {
        if(parent_ != null) {
            parent_.setAttributeValue(name_, value);
        }
        else {
            value_ = value;
        }
    }
}
