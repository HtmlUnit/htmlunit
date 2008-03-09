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
package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.Map;

import org.apache.xml.utils.PrefixResolverDefault;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.xml.XmlAttr;
import com.gargoylesoftware.htmlunit.xml.XmlElement;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class HtmlUnitPrefixResolver extends PrefixResolverDefault {

    /**
     * Constructor
     * @param xpathExpressionContext The context from which XPath expression prefixes will be resolved.
     */
    public HtmlUnitPrefixResolver(final Node xpathExpressionContext) {
        super(xpathExpressionContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceForPrefix(final String prefix, final Node namespaceContext) {
        String namespace = super.getNamespaceForPrefix(prefix, namespaceContext);
        if (namespace == null) {
            if (namespaceContext instanceof XmlPage) {
                final XmlElement documentElement = ((XmlPage) namespaceContext).getDocumentXmlElement();
                if (documentElement != null) {
                    namespace = getNamespace(documentElement, prefix);
                }
            }
            else if (namespaceContext instanceof XmlElement) {
                namespace = getNamespace((XmlElement) namespaceContext, prefix);
            }
        }
        return namespace;
    }

    private String getNamespace(final XmlElement element, final String prefix) {
        final Map<String, XmlAttr> attributes = element.getAttributesMap();
        for (final String name : attributes.keySet()) {
            if (name.startsWith("xmlns:")) {
                if (name.substring("xmlns:".length()).equals(prefix)) {
                    return attributes.get(name).getValue();
                }
            }
        }
        for (final DomNode child : element.getChildren()) {
            if (child instanceof XmlElement) {
                final String namespace = getNamespace((XmlElement) child, prefix);
                if (namespace != null) {
                    return namespace;
                }
            }
        }
        return null;
    }
}
