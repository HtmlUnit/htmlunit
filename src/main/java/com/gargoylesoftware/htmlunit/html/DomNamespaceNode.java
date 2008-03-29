/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;

/**
 * Intermediate base class for DOM Nodes that have namespaces.  That includes HtmlElement
 * and HtmlAttr.
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public abstract class DomNamespaceNode extends DomNode {

    private final String namespaceURI_;
    private String qualifiedName_;
    private final String localName_;
    private String prefix_;

    /**
     * Create an instance of a DOM node that can have a namespace.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     */
    protected DomNamespaceNode(final String namespaceURI, final String qualifiedName, final Page page) {
        super(page);
        WebAssert.notNull("qualifiedName", qualifiedName);
        qualifiedName_ = qualifiedName;
        if (namespaceURI != null && namespaceURI.length() > 0 && qualifiedName.indexOf(':') != -1) {
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
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI() {
        return namespaceURI_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        return localName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return prefix_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix(final String prefix) {
        prefix_ = prefix;
        if (prefix_ != null && localName_ != null) {
            qualifiedName_ = prefix_ + ":" + localName_;
        }
    }

    /**
     * Returns this node's qualified name.
     * @return this node's qualified name
     */
    public String getQualifiedName() {
        return qualifiedName_;
    }
}
