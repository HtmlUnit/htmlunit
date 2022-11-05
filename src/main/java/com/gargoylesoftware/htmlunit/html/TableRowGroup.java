/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Superclass for the wrappers for the HTML elements "thead", "tbody" and "tfoot".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
public abstract class TableRowGroup extends HtmlElement {

    /**
     * Creates an instance of TableRowGroup.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    protected TableRowGroup(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns a list of table rows contained in this element.
     *
     * @return a list of table rows
     */
    public final List<HtmlTableRow> getRows() {
        final List<HtmlTableRow> resultList = new ArrayList<>();

        for (final DomElement element : getChildElements()) {
            if (element instanceof HtmlTableRow) {
                resultList.add((HtmlTableRow) element);
            }
        }

        return resultList;
    }

    /**
     * Returns the value of the attribute {@code align}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code align}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttributeDirect("align");
    }

    /**
     * Returns the value of the attribute {@code char}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code char}
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharAttribute() {
        return getAttributeDirect("char");
    }

    /**
     * Returns the value of the attribute {@code charoff}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code charoff}
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharoffAttribute() {
        return getAttributeDirect("charoff");
    }

    /**
     * Returns the value of the attribute {@code valign}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code valign}
     * or an empty string if that attribute isn't defined.
     */
    public final String getValignAttribute() {
        return getAttributeDirect("valign");
    }
}
