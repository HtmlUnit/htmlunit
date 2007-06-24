/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;


/**
 *  Wrapper for the html element "td"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlTableDataCell extends HtmlTableCell {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "td";

    /**
     *  Create an instance
     *
     * @param  page The page that this element is contained within
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlTableDataCell.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlTableDataCell(final HtmlPage page, final Map attributes) {
        this(null, TAG_NAME, page, attributes);
    }

    /**
     *  Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param  page The page that this element is contained within
     * @param attributes the initial attributes
     */
    HtmlTableDataCell(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Return the value of the attribute "abbr".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "abbr"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAbbrAttribute() {
        return getAttributeValue("abbr");
    }


    /**
     * Return the value of the attribute "axis".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "axis"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAxisAttribute() {
        return getAttributeValue("axis");
    }


    /**
     * Return the value of the attribute "headers".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "headers"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHeadersAttribute() {
        return getAttributeValue("headers");
    }


    /**
     * Return the value of the attribute "scope".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "scope"
     * or an empty string if that attribute isn't defined.
     */
    public final String getScopeAttribute() {
        return getAttributeValue("scope");
    }


    /**
     * Return the value of the attribute "rowspan".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "rowspan"
     * or an empty string if that attribute isn't defined.
     */
    public final String getRowSpanAttribute() {
        return getAttributeValue("rowspan");
    }


    /**
     * Return the value of the attribute "colspan".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "colspan"
     * or an empty string if that attribute isn't defined.
     */
    public final String getColumnSpanAttribute() {
        return getAttributeValue("colspan");
    }


    /**
     * Return the value of the attribute "align".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "align"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttributeValue("align");
    }


    /**
     * Return the value of the attribute "char".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "char"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharAttribute() {
        return getAttributeValue("char");
    }


    /**
     * Return the value of the attribute "charoff".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "charoff"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharoffAttribute() {
        return getAttributeValue("charoff");
    }


    /**
     * Return the value of the attribute "valign".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "valign"
     * or an empty string if that attribute isn't defined.
     */
    public final String getValignAttribute() {
        return getAttributeValue("valign");
    }


    /**
     * Return the value of the attribute "nowrap".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "nowrap"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNoWrapAttribute() {
        return getAttributeValue("nowrap");
    }


    /**
     * Return the value of the attribute "bgcolor".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "bgcolor"
     * or an empty string if that attribute isn't defined.
     */
    public final String getBgcolorAttribute() {
        return getAttributeValue("bgcolor");
    }


    /**
     * Return the value of the attribute "width".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "width"
     * or an empty string if that attribute isn't defined.
     */
    public final String getWidthAttribute() {
        return getAttributeValue("width");
    }


    /**
     * Return the value of the attribute "height".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "height"
     * or an empty string if that attribute isn't defined.
     */
    public final String getHeightAttribute() {
        return getAttributeValue("height");
    }
}
