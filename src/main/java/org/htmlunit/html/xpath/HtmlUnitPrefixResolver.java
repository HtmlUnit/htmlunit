/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.html.xpath;

import java.util.Map;

import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.xml.XmlPage;
import org.htmlunit.xpath.xml.utils.PrefixResolverDefault;
import org.w3c.dom.Node;

/**
 * Custom {@link PrefixResolverDefault} extension.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class HtmlUnitPrefixResolver extends PrefixResolverDefault {

    /**
     * Creates a new instance.
     * @param xpathExpressionContext the context from which XPath expression prefixes will be resolved
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
        final String xmlns = "xmlns:";
        final int xmlnsLength = xmlns.length();

        for (final Map.Entry<String, DomAttr> entry : attributes.entrySet()) {
            final String name = entry.getKey();
            if (name.startsWith(xmlns) && name.regionMatches(xmlnsLength, prefix, 0, prefix.length())) {
                return entry.getValue().getValue();
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
