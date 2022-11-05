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

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML "th" tag.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class HtmlTableHeaderCell extends HtmlTableCell {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "th";

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that this element is contained within
     * @param attributes the initial attributes
     */
    HtmlTableHeaderCell(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute {@code abbr}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code abbr}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAbbrAttribute() {
        return getAttributeDirect("abbr");
    }

    /**
     * Returns the value of the attribute {@code axis}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code axis}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAxisAttribute() {
        return getAttributeDirect("axis");
    }

    /**
     * Returns the value of the attribute {@code headers}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code headers}
     * or an empty string if that attribute isn't defined.
     */
    public final String getHeadersAttribute() {
        return getAttributeDirect("headers");
    }

    /**
     * Returns the value of the attribute {@code scope}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code scope}
     * or an empty string if that attribute isn't defined.
     */
    public final String getScopeAttribute() {
        return getAttributeDirect("scope");
    }

    /**
     * Returns the value of the attribute {@code rowspan}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rowspan}
     * or an empty string if that attribute isn't defined.
     */
    public final String getRowSpanAttribute() {
        return getAttributeDirect("rowspan");
    }

    /**
     * Returns the value of the attribute {@code colspan}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code colspan}
     * or an empty string if that attribute isn't defined.
     */
    public final String getColumnSpanAttribute() {
        return getAttributeDirect("colspan");
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

    /**
     * Returns the value of the attribute {@code nowrap}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code nowrap}
     * or an empty string if that attribute isn't defined.
     */
    public final String getNoWrapAttribute() {
        return getAttributeDirect("nowrap");
    }

    /**
     * Returns the value of the attribute {@code bgcolor}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code bgcolor}
     * or an empty string if that attribute isn't defined.
     */
    public final String getBgcolorAttribute() {
        return getAttributeDirect("bgcolor");
    }

    /**
     * Returns the value of the attribute {@code width}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code width}
     * or an empty string if that attribute isn't defined.
     */
    public final String getWidthAttribute() {
        return getAttributeDirect("width");
    }

    /**
     * Returns the value of the attribute {@code height}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code height}
     * or an empty string if that attribute isn't defined.
     */
    public final String getHeightAttribute() {
        return getAttributeDirect("height");
    }
}
