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
package org.htmlunit.html;

import org.htmlunit.SgmlPage;
import org.xml.sax.Attributes;

/**
 * Specification of a factory capable of creating {@link DomElement} objects.
 *
 * @author Christian Sell
 * @author Ahmed Ashour
 */
public interface ElementFactory {

    /**
     * Creates an element according to this factory's specification. Note that even though this method
     * takes a page parameter, the element is <em>not</em> automatically added to the page's DOM tree.
     *
     * @param page the enclosing page for the new element
     * @param tagName the tag name (most factories will be responsible for a specific tag, but this
     *        parameter is passed in for factories that don't follow this rule)
     * @param attributes the attributes encountered during XML/HTML parsing (possibly {@code null}
     *        if no attributes specified
     * @return the newly created and initialized element
     */
    DomElement createElement(SgmlPage page, String tagName, Attributes attributes);

    /**
     * Creates an element according to this factory's specification. Note that even though this method
     * takes a page parameter, the element is <em>not</em> automatically added to the page's DOM tree.
     *
     * @param page the enclosing page for the new element
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param attributes the attributes encountered during XML/HTML parsing (possibly {@code null}
     *        if no attributes specified
     * @return the newly created and initialized element
     */
    DomElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes);
}
