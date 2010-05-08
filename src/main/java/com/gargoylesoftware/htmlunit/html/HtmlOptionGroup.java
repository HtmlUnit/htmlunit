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

import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "optgroup".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class HtmlOptionGroup extends HtmlElement implements DisabledElement {

    private static final long serialVersionUID = 7854731553754432321L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "optgroup";

    /**
     * Creates an instance of HtmlOptionGroup
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlOptionGroup(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns <tt>true</tt> if the disabled attribute is set for this element. Note that this
     * method always returns <tt>false</tt> when emulating IE, because IE does not allow individual
     * option groups to be disabled.
     *
     * @return <tt>true</tt> if the disabled attribute is set for this element (always <tt>false</tt>
     *         when emulating IE)
     */
    public final boolean isDisabled() {
        if (getPage().getWebClient().getBrowserVersion()
                .hasFeature(BrowserVersionFeatures.HTMLOPTIONGROUP_NO_DISABLED)) {
            return false;
        }
        return hasAttribute("disabled");
    }

    /**
     * {@inheritDoc}
     */
    public final String getDisabledAttribute() {
        return getAttribute("disabled");
    }

    /**
     * Returns the value of the attribute "label". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "label" or an empty string if that attribute isn't defined
     */
    public final String getLabelAttribute() {
        return getAttribute("label");
    }
}
