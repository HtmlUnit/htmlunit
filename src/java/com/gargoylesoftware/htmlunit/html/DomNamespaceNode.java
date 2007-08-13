/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.Assert;

/**
 * Intermediate base class for DOM Nodes that have namespaces.  That includes HtmlElement
 * and HtmlAttr.
 *
 * @version $Revision$
 * @author David K. Taylor
 */
public abstract class DomNamespaceNode extends DomNode {

    private final String namespaceURI_;
    private String qualifiedName_;
    private final String localName_;
    private String prefix_;

    /**
     *  Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param htmlPage The page that contains this element
     */
    protected DomNamespaceNode(final String namespaceURI, final String qualifiedName, final HtmlPage htmlPage) {
        super(htmlPage);
        Assert.notNull("qualifiedName", qualifiedName);
        qualifiedName_ = qualifiedName;
        if (namespaceURI != null && namespaceURI.length() > 0) {
            namespaceURI_ = namespaceURI;
            final int colonPosition = qualifiedName_.indexOf(':');
            localName_ = qualifiedName_.substring(colonPosition + 1);
            prefix_ = qualifiedName_.substring(0, colonPosition);
        }
        else {
            namespaceURI_ = null;
            localName_ = qualifiedName_;
            prefix_ = null;
        }
    }

    /**
     * The namespace URI of this node, or null if it is unspecified (see ).  This is not a
     * computed value that is the result of a namespace lookup based on an examination of the
     * namespace declarations in scope.  It is merely the namespace URI given at creation time.
     * For nodes of any type other than ELEMENT_NODE and ATTRIBUTE_NODE and nodes created with
     * a DOM Level 1 method, such as Document.createElement(), this is always null.
     *
     * This method is part of the W3C DOM API.
     *
     * @return The URI that identifies an XML namespace.
     */
    public String getNamespaceURI() {
        return namespaceURI_;
    }

    /**
     * Returns the local part of the qualified name of this node.  For nodes of any
     * type other than ELEMENT_NODE and ATTRIBUTE_NODE and nodes created with a DOM Level 1
     * method, such as Document.createElement(), this is always null.
     *
     * This method is part of the W3C DOM API.
     *
     * @return The local name (without prefix).
     */
    public String getLocalName() {
        return localName_;
    }

    /**
     * The namespace prefix of this node, or null if it is unspecified.  When it is defined
     * to be null, setting it has no effect, including if the node is read-only. Note that
     * setting this attribute, when permitted, changes the nodeName attribute, which holds
     * the qualified name, as well as the tagName and name attributes of the Element and Attr
     * interfaces, when applicable.  Setting the prefix to null makes it unspecified, setting
     * it to an empty string is implementation dependent.  Note also that changing the prefix
     * of an attribute that is known to have a default value, does not make a new attribute
     * with the default value and the original prefix appear, since the namespaceURI and
     * localName do not change. For nodes of any type other than ELEMENT_NODE and ATTRIBUTE_NODE
     * and nodes created with a DOM Level 1 method, such as createElement from the Document
     * interface, this is always null.
     *
     * This method is part of the W3C DOM API.
     *
     * @return The Namespace prefix.
     */
    public String getPrefix() {
        return prefix_;
    }

    /**
     * Set the namespace prefix of this node, or null if it is unspecified.  When it is defined
     * to be null, setting it has no effect, including if the node is read-only.  Note that setting
     * this attribute, when permitted, changes the nodeName attribute, which holds the qualified
     * name, as well as the tagName and name attributes of the Element and Attr interfaces, when
     * applicable.  Setting the prefix to null makes it unspecified, setting it to an empty string
     * is implementation dependent.  Note also that changing the prefix of an attribute that is
     * known to have a default value, does not make a new attribute with the default value and the
     * original prefix appear, since the namespaceURI and localName do not change.  For nodes of
     * any type other than ELEMENT_NODE and ATTRIBUTE_NODE and nodes created with a DOM Level 1
     * method, such as createElement from the Document interface, this is always null.
     *
     * This method is part of the W3C DOM API.
     *
     * @param prefix The namespace prefix of this node, or null if it is unspecified.
     *
     */
    public void setPrefix(final String prefix) {
        prefix_ = prefix;
        if (prefix_ != null && localName_ != null) {
            qualifiedName_ = prefix_ + ":" + localName_;
        }
    }

    /**
     * Returns the qualified name of this node.
     *
     * @return The prefix and local name.
     */
    public String getQualifiedName() {
        return qualifiedName_;
    }
}
