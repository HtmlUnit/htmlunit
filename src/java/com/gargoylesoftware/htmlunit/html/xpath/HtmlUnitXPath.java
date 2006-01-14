/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
import org.jaxen.Navigator;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * Jaxen XPath adapter implementation for the HtmlUnit DOM model
 *
 * <p>This is the main entry point for matching an XPath against a HU-DOM
 * tree.  You create a compiled XPath object, then match it against one or
 * more context nodes using the {@link BaseXPath#selectNodes(Object)}
 * method, as in the following example:</p>
 *
 * <pre>
 * XPath path = new HtmlUnitXPath("a/b/c");
 * List results = path.selectNodes(domNode);
 * </pre>
 *
 * @see org.jaxen.BaseXPath
 *
 * @version  $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Mike Bowler
 * @author Marc Guillemot
 */
public class HtmlUnitXPath extends BaseXPath {

    private static final long serialVersionUID = -3902959929710269843L;
    private final String xpath_;

    /** 
     * Construct given an XPath expression string.
     *  @param xpathExpr The XPath expression.
     *  @throws org.jaxen.JaxenException if there is a syntax error while
     *          parsing the expression.
     */
    public HtmlUnitXPath(final String xpathExpr) throws JaxenException {
        this( xpathExpr, DocumentNavigator.instance );
    }

    /** 
     * Construct given an XPath expression string and a Document Navigator.
     * @param xpathExpr The XPath expression.
     * @param navigator the document navigator to use for evaluation
     * @throws org.jaxen.JaxenException if there is a syntax error while
     *          parsing the expression.
     */
    public HtmlUnitXPath(final String xpathExpr, final Navigator navigator) throws JaxenException {
        super(xpathExpr, navigator);
        xpath_ = xpathExpr;
    }
    
    
    /**
     * Builds a navigator that will see the provided node as the "document" 
     * and only navigate in its children.<br/>
     * The returned navigator can only be used to evaluate xpath expressions on nodes
     * of the same document as the provided one. The behavior when used on an other document is undefined. 
     * @param node the node that should be considered as the root by the navigator 
     * @return a navigator
     */
    public static Navigator buildSubtreeNavigator(final DomNode node) {
        return new NodeRelativeNavigator(node);
    }
    
    /**
     * Gives the xpath expression provided to c'tor.
     * @see org.jaxen.BaseXPath#toString()
     * @return the xpath expression provided to c'tor.
     */
    public String toString() {
        return xpath_;
    }
}
