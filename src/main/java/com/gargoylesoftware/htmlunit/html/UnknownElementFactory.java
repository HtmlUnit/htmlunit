/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;

import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * A factory for elements encountered in parsing the input which are not represented
 * by dedicated element classes.
 *
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author David K. Taylor
 * @author Ronald Brill
 */
public final class UnknownElementFactory implements ElementFactory {

    /** The singleton instance. */
    public static final UnknownElementFactory instance = new UnknownElementFactory();

    /** Private singleton constructor. */
    private UnknownElementFactory() {
    }

    /**
     * {@inheritDoc}
     */
    public HtmlElement createElement(final SgmlPage page, final String tagName, final Attributes attributes) {
        String namespace = null;
        if (page != null && page.isHtmlPage() && tagName.indexOf(':') != -1) {
            final HtmlPage htmlPage = (HtmlPage) page;
            final String prefix = tagName.substring(0, tagName.indexOf(':'));
            final Map<String, String> namespaces = htmlPage.getNamespaces();
            if (namespaces.containsKey(prefix)) {
                namespace = namespaces.get(prefix);
            }
        }
        return createElementNS(page, namespace, tagName, attributes);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes) {
        return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes, final boolean checkBrowserCompatibility) {
        final Map<String, DomAttr> attributeMap = DefaultElementFactory.setAttributes(page, attributes);
        return new HtmlUnknownElement(page, namespaceURI, qualifiedName, attributeMap);
    }
}
