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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_SET_VALUE_UPDATES_DEFAULT_VALUE;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" where type is "button".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlButtonInput extends HtmlInput {

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlButtonInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc} This method <b>does nothing</b> for button input elements.
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        // Empty.
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
