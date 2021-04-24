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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DISPLAY_BLOCK2;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "optgroup".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlOptionGroup extends HtmlElement implements DisabledElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "optgroup";

    /**
     * Creates an instance of HtmlOptionGroup
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlOptionGroup(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns {@code true} if the disabled attribute is set for this element. Note that this
     * method always returns {@code false} when emulating IE, because IE does not allow individual
     * option groups to be disabled.
     *
     * @return {@code true} if the disabled attribute is set for this element (always {@code false}
     *         when emulating IE)
     */
    @Override
    public final boolean isDisabled() {
        return hasAttribute("disabled");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttributeDirect("disabled");
    }

    /**
     * Returns the value of the attribute {@code label}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code label} or an empty string if that attribute isn't defined
     */
    public final String getLabelAttribute() {
        return getAttributeDirect("label");
    }

    /**
     * Sets the value of the attribute {@code label}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @param newLabel the value of the attribute {@code label}
     */
    public final void setLabelAttribute(final String newLabel) {
        setAttribute("label", newLabel);
    }

    /**
     * Gets the enclosing select of this HtmlOptionGroup.
     * @return {@code null} if no select is found (for instance malformed html)
     */
    public HtmlSelect getEnclosingSelect() {
        return (HtmlSelect) getEnclosingElement(HtmlSelect.TAG_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        if (hasFeature(CSS_DISPLAY_BLOCK2)) {
            return DisplayStyle.BLOCK;
        }
        return DisplayStyle.INLINE;
    }
}
