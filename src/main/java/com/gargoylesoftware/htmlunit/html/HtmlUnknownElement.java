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

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * An element that is returned for an HTML tag that is not supported by this framework.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 */
public class HtmlUnknownElement extends HtmlElement {

    /**
     * Creates an instance.
     *
     * @param page the page that contains this element
     * @param tagName the HTML tag represented by this object
     * @param attributes the initial attributes
     */
    HtmlUnknownElement(final SgmlPage page, final String tagName, final Map<String, DomAttr> attributes) {
        this(page, null, tagName, attributes);
    }

    /**
     * Creates an instance.
     *
     * @param page the page that contains this element
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param attributes the initial attributes
     */
    HtmlUnknownElement(final SgmlPage page, final String namespaceURI, final String qualifiedName,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isTrimmedText() {
        return false;
    }
}
