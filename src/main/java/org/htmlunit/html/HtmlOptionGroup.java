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

import java.util.Map;

import org.htmlunit.SgmlPage;
import org.w3c.dom.Node;

/**
 * Wrapper for the HTML element "optgroup".
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Christian Sell
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
     * @return {@code true} if the disabled attribute is set for this element
     */
    @Override
    public final boolean isDisabled() {
        if (hasAttribute(ATTRIBUTE_DISABLED)) {
            return true;
        }

        Node node = getParentNode();
        while (node != null) {
            if (node instanceof DisabledElement
                    && ((DisabledElement) node).isDisabled()) {
                return true;
            }
            node = node.getParentNode();
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getDisabledAttribute() {
        return getAttributeDirect(ATTRIBUTE_DISABLED);
    }

    /**
     * Returns the value of the attribute {@code label}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code label} or an empty string if that attribute isn't defined
     */
    public final String getLabelAttribute() {
        return getAttributeDirect("label");
    }

    /**
     * Sets the value of the attribute {@code label}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
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
        return DisplayStyle.BLOCK;
    }
}
