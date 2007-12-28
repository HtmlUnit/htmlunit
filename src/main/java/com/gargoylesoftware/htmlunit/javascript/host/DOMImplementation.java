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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for DOMImplementation.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 *
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-core.html#ID-102161490">
 * W3C Dom Level 1</a>
 */
public class DOMImplementation extends SimpleScriptable {

    private static final long serialVersionUID = -6824157544527299635L;

    /**
     * Javascript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * Test if the DOM implementation implements a specific feature.
     * @param feature The name of the feature to test (case-insensitive).
     * @param version The version number of the feature to test.
     * @return true if the feature is implemented in the specified version, false otherwise.
     */
    public boolean jsxFunction_hasFeature(final String feature, final String version) {
        if (getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE()) {
            if ("HTML".equals(feature) && "1.0".equals(version)) {
                return true;
            }
        }
        else {
            if ("HTML".equals(feature) && ("1.0".equals(version) || "2.0".equals(version))) {
                return true;
            }
            else if ("XML".equals(feature) && ("1.0".equals(version) || "2.0".equals(version))) {
                return true;
            }
            else if ("CSS2".equals(feature) && "2.0".equals(version)) {
                return true;
            }
            else if ("XPath".equals(feature) && "3.0".equals(version)) {
                return true;
            }
            //TODO: other features.
        }
        return false;
    }

    /**
     * Creates an {@link XMLDocument}.
     *
     * @param namespaceURI The URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the document to instantiate.
     * @param doctype The document types of the document.
     * @return the newly created {@link XMLDocument}.
     */
    //TODO: change doctype type to "DocType"
    public XMLDocument jsxFunction_createDocument(final String namespaceURI, final String qualifiedName,
            final Object doctype) {
        final XMLDocument document = new XMLDocument(getWindow().getWebWindow());
        document.setParentScope(getParentScope());
        document.setPrototype(getPrototype(document.getClass()));
        if (qualifiedName != null && qualifiedName.length() != 0) {
            final XmlPage page = (XmlPage) document.getDomNodeOrDie();
            page.appendDomChild(page.createXmlElementNS(namespaceURI, qualifiedName));
        }
        return document;
    }
}
