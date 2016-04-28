/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * A specialized creator that knows how to create input objects.
 *
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author David K. Taylor
 * @author Dmitri Zoubkov
 * @author Frank Danek
 */
public final class InputElementFactory implements ElementFactory {

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
    @Override
    public HtmlElement createElement(
            final SgmlPage page, final String tagName,
            final Attributes attributes) {
        return createElementNS(page, null, tagName, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes) {
        return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes, final boolean asdf) {

        Map<String, DomAttr> attributeMap = DefaultElementFactory.setAttributes(page, attributes);
        if (attributeMap == null) {
            attributeMap = new HashMap<>();
        }

        String type = null;
        if (attributes != null) {
            type = attributes.getValue("type");
        }
        if (type == null) {
            type = "";
        }

        final HtmlInput result;
        switch (type.toLowerCase(Locale.ROOT)) {
            case "":
                // This not an illegal value, as it defaults to "text"
                // cf http://www.w3.org/TR/REC-html40/interact/forms.html#adef-type-INPUT
                // and the common browsers seem to treat it as a "text" input so we will as well.
            case "text":
                result = new HtmlTextInput(qualifiedName, page, attributeMap);
                break;

            case "submit":
                result = new HtmlSubmitInput(qualifiedName, page, attributeMap);
                break;

            case "checkbox":
                result = new HtmlCheckBoxInput(qualifiedName, page, attributeMap);
                break;

            case "radio":
                result = new HtmlRadioButtonInput(qualifiedName, page, attributeMap);
                break;

            case "hidden":
                result = new HtmlHiddenInput(qualifiedName, page, attributeMap);
                break;

            case "password":
                result = new HtmlPasswordInput(qualifiedName, page, attributeMap);
                break;

            case "image":
                result = new HtmlImageInput(qualifiedName, page, attributeMap);
                break;

            case "reset":
                result = new HtmlResetInput(qualifiedName, page, attributeMap);
                break;

            case "button":
                result = new HtmlButtonInput(qualifiedName, page, attributeMap);
                break;

            case "file":
                result = new HtmlFileInput(qualifiedName, page, attributeMap);
                break;

            case "color":
                result = new HtmlColorInput(qualifiedName, page, attributeMap);
                break;

            case "date":
                result = new HtmlDateInput(qualifiedName, page, attributeMap);
                break;

            case "datetime-local":
                result = new HtmlDateTimeLocalInput(qualifiedName, page, attributeMap);
                break;

            case "month":
                result = new HtmlMonthInput(qualifiedName, page, attributeMap);
                break;

            case "number":
                result = new HtmlNumberInput(qualifiedName, page, attributeMap);
                break;

            case "range":
                result = new HtmlRangeInput(qualifiedName, page, attributeMap);
                break;

            case "search":
                result = new HtmlSearchInput(qualifiedName, page, attributeMap);
                break;

            case "time":
                result = new HtmlTimeInput(qualifiedName, page, attributeMap);
                break;

            case "week":
                result = new HtmlWeekInput(qualifiedName, page, attributeMap);
                break;

            default:
                LOG.info("Bad input type: \"" + type + "\", creating a text input");
                result = new HtmlTextInput(qualifiedName, page, attributeMap);
                break;
        }
        return result;
    }
}
