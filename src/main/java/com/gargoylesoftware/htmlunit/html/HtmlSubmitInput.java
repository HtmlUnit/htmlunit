/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_SET_VALUE_UPDATES_DEFAULT_VALUE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.SUBMITINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.SUBMITINPUT_DEFAULT_VALUE_UNDEFINED;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlSubmitInput extends HtmlInput {

    /**
     * Value to use if no specified <tt>value</tt> attribute.
     */
    private static final String DEFAULT_VALUE = "Submit Query";

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlSubmitInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, addValueIfNeeded(page, attributes));

        // fix the default value in case we have set it
        if (hasFeature(SUBMITINPUT_DEFAULT_VALUE_UNDEFINED)
                && getAttribute("value") == DEFAULT_VALUE) {
            setDefaultValue(ATTRIBUTE_NOT_DEFINED, false);
        }
    }

    /**
     * Add missing attribute if needed by fixing attribute map rather to add it afterwards as this second option
     * triggers the instantiation of the script object at a time where the DOM node has not yet been added to its
     * parent.
     */
    private static Map<String, DomAttr> addValueIfNeeded(final SgmlPage page,
            final Map<String, DomAttr> attributes) {

        final BrowserVersion browserVersion = page.getWebClient().getBrowserVersion();
        if (browserVersion.hasFeature(SUBMITINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED)) {
            for (final String key : attributes.keySet()) {
                if ("value".equalsIgnoreCase(key)) {
                    return attributes; // value attribute was specified
                }
            }

            // value attribute was not specified, add it
            final DomAttr newAttr = new DomAttr(page, null, "value", DEFAULT_VALUE, true);
            attributes.put("value", newAttr);
        }

        return attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate() throws IOException {
        final HtmlForm form = getEnclosingForm();
        if (form != null) {
            form.submit(this);
            return false;
        }
        super.doClickStateUpdate();
        return false;
    }

    /**
     * {@inheritDoc} This method <b>does nothing</b> for submit input elements.
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        // Empty.
    }

    /**
     * {@inheritDoc} Returns "Submit Query" if <tt>value</tt> attribute is not defined.
     */
    // we need to preserve this method as it is there since many versions with the above documentation.
    @Override
    public String asText() {
        String text = getValueAttribute();
        if (text == ATTRIBUTE_NOT_DEFINED) {
            text = DEFAULT_VALUE;
        }
        return text;
    }

    /**
     * {@inheritDoc} Doesn't print the attribute if it is <tt>value="Submit Query"</tt>.
     */
    @Override
    protected void printOpeningTagContentAsXml(final PrintWriter printWriter) {
        printWriter.print(getTagName());

        for (final DomAttr attribute : getAttributesMap().values()) {
            final String name = attribute.getNodeName();
            final String value = attribute.getValue();
            if (!"value".equals(name) || !DEFAULT_VALUE.equals(value)) {
                printWriter.print(" ");
                printWriter.print(name);
                printWriter.print("=\"");
                printWriter.print(StringUtils.escapeXmlAttributeValue(value));
                printWriter.print("\"");
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * Returns "Submit Query" if <tt>name</tt> attribute is defined and <tt>value</tt> attribute is not defined.
     */
    @Override
    public NameValuePair[] getSubmitKeyValuePairs() {
        if (!getNameAttribute().isEmpty() && !hasAttribute("value")) {
            return new NameValuePair[]{new NameValuePair(getNameAttribute(), DEFAULT_VALUE)};
        }
        return super.getSubmitKeyValuePairs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue) {
        if (hasFeature(HTMLINPUT_SET_VALUE_UPDATES_DEFAULT_VALUE) && "value".equals(qualifiedName)) {
            setDefaultValue(attributeValue, false);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue);
    }
}
