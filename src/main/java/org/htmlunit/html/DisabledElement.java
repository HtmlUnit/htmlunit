/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A marker interface for those classes that can be disabled.
 *
 * @author David D. Kilzer
 * @author Ronald Brill
 */
public interface DisabledElement extends Element {

    /** The "disabled" attribute name. */
    String ATTRIBUTE_DISABLED = "disabled";

    /**
     * Returns {@code true} if the disabled attribute is set for this element.
     * <p>
     * Per spec, a disabled {@link HtmlFieldSet} ancestor disables its
     * descendants EXCEPT for descendants of that fieldset's first
     * {@code <legend>} child, if any. This is checked cheaply while walking
     * up the ancestor chain: at each step, the node we just came from
     * (tracked as {@code previous}) is by construction a direct child of the
     * node we're currently examining, so when that node turns out to be a
     * disabled fieldset we already know its relevant direct child without
     * any second traversal -- see {@link #isFirstLegendChild(HtmlFieldSet, Node)}.
     * </p>
     *
     * @return {@code true} if the disabled attribute is set for this element
     */
    default boolean isDisabled() {
        if (hasAttribute(ATTRIBUTE_DISABLED)) {
            return true;
        }

        Node previous = this;
        Node node = getParentNode();
        while (node != null) {
            if (node instanceof HtmlFieldSet fieldSet) {
                if (fieldSet.hasAttribute(ATTRIBUTE_DISABLED)
                        && !isFirstLegendChild(fieldSet, previous)) {
                    return true;
                }
            }
            else if (node instanceof DisabledElement element
                    && element.hasAttribute(ATTRIBUTE_DISABLED)) {
                return true;
            }
            previous = node;
            node = node.getParentNode();
        }

        return false;
    }

    /**
     * Checks whether {@code candidate} -- the direct child of {@code fieldSet}
     * that lies on the path up from this element -- is {@code fieldSet}'s
     * FIRST {@code <legend>} child. The exemption from fieldset disabling
     * applies only to the first legend -- a control inside a second,
     * non-conforming {@code <legend>} is still disabled. Bounded by how many
     * children precede the first legend (in practice O(1), since a legend is
     * almost always the first child), not by how deep this element is nested
     * inside it.
     *
     * @param fieldSet the disabled fieldset ancestor to check the exemption against
     * @param candidate the direct child of {@code fieldSet} on the path up from this element
     * @return {@code true} if {@code candidate} is {@code fieldSet}'s first {@code <legend>} child
     */
    private static boolean isFirstLegendChild(final HtmlFieldSet fieldSet, final Node candidate) {
        if (!(candidate instanceof HtmlLegend)) {
            return false;
        }

        for (final DomNode child : fieldSet.getChildren()) {
            if (child instanceof HtmlLegend) {
                return child == candidate;
            }
        }
        return false;
    }

    /**
     * Returns the value of the attribute {@code disabled}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code disabled} or an empty string if that attribute isn't defined
     */
    String getDisabledAttribute();
}
