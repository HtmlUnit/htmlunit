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
package com.gargoylesoftware.htmlunit.html.xpath;

import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;
import org.jaxen.XPath;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * Implementation of the Jaxen Navigator to navigate through part of the HtmlUnit DOM object model
 * in the context of XPath evaluation.
 * <p>
 * <em>This class allows to make evaluation of xpath be relative to a special node that is
 * considered as the root. The rest of the html page is not considered.
 * </em>
 * <em>This class is not intended for direct usage, but is used by the Jaxen engine
 * during evaluation.
 * </em>
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Mike Bowler
 * @see HtmlUnitXPath
 */
class NodeRelativeNavigator extends DocumentNavigator {
    
    private static final long serialVersionUID = 3833748784969691447L;
    private final DomNode rootNode_;

    /**
     * Constructs a navigator that will see the provided node as the root
     * of the hierarchy it has to walk through.
     * @param node the node to consider as the root when navigating
     */
    public NodeRelativeNavigator(final DomNode node) {
        rootNode_ = node;
    }

    /**
     * @param xpath an xpath expression
     * @return a parsed form of the given xpath string, which will be suitable
     *  for queries on DOM documents.
     * @throws JaxenException if the expression could not be parsed
     */
    public XPath parseXPath (final String xpath) throws JaxenException {
        return new BaseXPath(xpath, this);
    }

    /**
     * Get the top-level document node.
     *
     * @param contextNode Any node in the document.
     * @return The root node.
     */
    public Object getDocumentNode (final Object contextNode) {
        return rootNode_;
    }

    /**
     * Test if a node is a top-level document.
     *
     * @param object The target node.
     * @return true if the node is the document root, false otherwise.
     */
    public boolean isDocument (final Object object) {
        return (object == rootNode_);
    }

    /**
     * Test if a node is an element.
     *
     * @param object The target node.
     * @return true if the node is an element, false otherwise.
     */
    public boolean isElement (final Object object) {
        return super.isElement(object) && (object != rootNode_);
    }

    /**
     *  Returns the element whose ID is given by elementId.
     *  If no such element exists, returns null.
     *  Attributes with the name "ID" are not of type ID unless so defined.
     *  Atribute types are only known if when the parser understands DTD's or
     *  schemas that declare attributes of type ID. When JAXP is used, you
     *  must call <code>setValidating(true)</code> on the
     *  DocumentBuilderFactory.
     *
     *  @param contextNode   a node from the document in which to look for the
     *                       id
     *  @param elementId   id to look for
     *
     *  @return   element whose ID is given by elementId, or null if no such
     *            element exists in the document or if the implementation
     *            does not know about attribute types
     *  @see   javax.xml.parsers.DocumentBuilderFactory
     */
    public Object getElementById(final Object contextNode, final String elementId) {
        final DomNode node = (DomNode) super.getElementById(contextNode, elementId);
        if (isChild(node)) {
            return node;
        }
        return null; 
    }

    /**
     * Tests if the document's node is a child of the node considered as the root by this Navigator
     * @param node the node to test
     * @return <code>true</code> if it is a child of the root.
     */
    private boolean isChild(final DomNode node) {
        DomNode parent = node;
        while (parent != null) {
            if (parent == rootNode_) {
                return true;
            }
            parent = parent.getParentNode();
        }
        
        return false;
    }
}
