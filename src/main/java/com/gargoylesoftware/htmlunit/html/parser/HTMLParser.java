/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.parser;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * <p>Interface for the parser used to parse HTML into a HtmlUnit-specific DOM (HU-DOM) tree.</p>
 *
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ethan Glasser-Camp
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
public interface HTMLParser {

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param tagName an HTML element tag name
     * @return a factory for creating HtmlElements representing the given tag
     */
    ElementFactory getFactory(String tagName);

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @return a factory for creating SvgElements representing the given tag
     */
    ElementFactory getSvgFactory();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the pre-registered element factory corresponding to the specified tag, or an UnknownElementFactory.
     * @param page the page
     * @param namespaceURI the namespace URI
     * @param qualifiedName the qualified name
     * @param insideSvg is the node inside an SVG node or not
     * @param svgSupport true if called from javascript createElementNS
     * @return the pre-registered element factory corresponding to the specified tag, or an UnknownElementFactory
     */
    ElementFactory getElementFactory(SgmlPage page, String namespaceURI,
            String qualifiedName, boolean insideSvg, boolean svgSupport);

    /**
     * Parses the HTML content from the given string into an object tree representation.
     *
     * @param parent the parent for the new nodes
     * @param source the (X)HTML to be parsed
     * @throws SAXException if a SAX error occurs
     * @throws IOException if an IO error occurs
     */
    void parseFragment(DomNode parent, String source) throws SAXException, IOException;

    /**
     * Parses the HTML content from the given string into an object tree representation.
     *
     * @param parent where the new parsed nodes will be added to
     * @param context the context to build the fragment context stack
     * @param source the (X)HTML to be parsed
     * @throws SAXException if a SAX error occurs
     * @throws IOException if an IO error occurs
     */
    void parseFragment(DomNode parent, DomNode context, String source) throws SAXException, IOException;

    /**
     * Parses the WebResponse into an object tree representation.
     *
     * @param webResponse the response data
     * @param page the HtmlPage to add the nodes
     * @param xhtml if true use the XHtml parser
     * @throws IOException if there is an IO error
     */
    void parse(WebResponse webResponse, HtmlPage page, boolean xhtml) throws IOException;
}
