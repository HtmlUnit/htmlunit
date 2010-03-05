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
package com.gargoylesoftware.htmlunit.html;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * A specialized creator that knows how to create input objects.
 *
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author David K. Taylor
 * @author Dmitri Zoubkov
 */
public final class InputElementFactory implements IElementFactory {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(InputElementFactory.class);

    /** The singleton instance. */
    public static final InputElementFactory instance = new InputElementFactory();

    /** Private singleton constructor. */
    private InputElementFactory() {
        // Empty.
    }

    /**
     * Creates an HtmlElement for the specified xmlElement, contained in the specified page.
     *
     * @param page the page that this element will belong to
     * @param tagName the HTML tag name
     * @param attributes the SAX attributes
     *
     * @return a new HtmlInput element
     */
    public HtmlElement createElement(
            final SgmlPage page, final String tagName,
            final Attributes attributes) {
        return createElementNS(page, null, tagName, attributes);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes) {

        Map<String, DomAttr> attributeMap = DefaultElementFactory.setAttributes(page, attributes);
        if (attributeMap == null) {
            attributeMap = new HashMap<String, DomAttr>();
        }

        String type = null;
        if (attributes != null) {
            type = attributes.getValue("type");
        }
        if (type == null) {
            type = "";
        }
        else {
            type = type.toLowerCase();
            attributeMap.get("type").setValue(type); // type value has to be lower case
        }

        final HtmlInput result;
        if (type.length() == 0) {
            // This not an illegal value, as it defaults to "text"
            // cf http://www.w3.org/TR/REC-html40/interact/forms.html#adef-type-INPUT
            // and the common browsers seem to treat it as a "text" input so we will as well.
            HtmlElement.addAttributeToMap(page, attributeMap, null, "type", "text");
            result = new HtmlTextInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("submit")) {
            result = new HtmlSubmitInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("checkbox")) {
            result = new HtmlCheckBoxInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("radio")) {
            result = new HtmlRadioButtonInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("text")) {
            result = new HtmlTextInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("hidden")) {
            result = new HtmlHiddenInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("password")) {
            result = new HtmlPasswordInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("image")) {
            result = new HtmlImageInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("reset")) {
            result = new HtmlResetInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("button")) {
            result = new HtmlButtonInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else if (type.equals("file")) {
            result = new HtmlFileInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        else {
            LOG.info("Bad input type: \"" + type + "\", creating a text input");
            result = new HtmlTextInput(namespaceURI, qualifiedName, page, attributeMap);
        }
        return result;
    }
}
