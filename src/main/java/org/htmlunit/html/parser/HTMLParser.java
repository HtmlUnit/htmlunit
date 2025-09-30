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
package org.htmlunit.html.parser;

import java.io.IOException;

import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.ElementFactory;
import org.htmlunit.html.HtmlPage;
import org.xml.sax.SAXException;

/**
 * <p>Interface for the parser used to parse HTML into a HtmlUnit-specific DOM (HU-DOM) tree.</p>
 *
 * @author Christian Sell
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
     * @param webClient the {@link WebClient}
     * @param parent where the new parsed nodes will be added to
     * @param context the context to build the fragment context stack
     * @param source the (X)HTML to be parsed
     * @param createdByJavascript if true the (script) tag was created by javascript
     * @throws SAXException if a SAX error occurs
     * @throws IOException if an IO error occurs
     */
    void parseFragment(WebClient webClient, DomNode parent, DomNode context, String source,
            boolean createdByJavascript) throws SAXException, IOException;

    /**
     * Parses the WebResponse into an object tree representation.
     *
     * @param webClient the {@link WebClient}
     * @param webResponse the response data
     * @param page the HtmlPage to add the nodes
     * @param xhtml if true use the XHtml parser
     * @param createdByJavascript if true the (script) tag was created by javascript
     * @throws IOException if there is an IO error
     */
    void parse(WebClient webClient, WebResponse webResponse, HtmlPage page,
            boolean xhtml, boolean createdByJavascript) throws IOException;
}
