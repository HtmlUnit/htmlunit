/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.Map;

import org.apache.xml.utils.PrefixResolverDefault;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Custom {@link PrefixResolverDefault} extension.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
final class HtmlUnitPrefixResolver extends PrefixResolverDefault {

    /**
     * Creates a new instance.
     * @param xpathExpressionContext the context from which XPath expression prefixes will be resolved
     */
    HtmlUnitPrefixResolver(final Node xpathExpressionContext) {
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
                final DomElement documentElement = ((XmlPage) namespaceContext).getDocumentElement();
                if (documentElement != null) {
                    namespace = getNamespace(documentElement, prefix);
                }
            }
            else if (namespaceContext instanceof DomElement) {
                namespace = getNamespace((DomElement) namespaceContext, prefix);
            }
        }
        return namespace;
    }

    private String getNamespace(final DomElement element, final String prefix) {
        final Map<String, DomAttr> attributes = element.getAttributesMap();
        for (final String name : attributes.keySet()) {
            if (name.startsWith("xmlns:")) {
                if (name.substring("xmlns:".length()).equals(prefix)) {
                    return attributes.get(name).getValue();
                }
            }
        }
        for (final DomNode child : element.getChildren()) {
            if (child instanceof DomElement) {
                final String namespace = getNamespace((DomElement) child, prefix);
                if (namespace != null) {
                    return namespace;
                }
            }
        }
        return null;
    }
}
