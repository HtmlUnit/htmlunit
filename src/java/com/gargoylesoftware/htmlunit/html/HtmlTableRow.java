/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  Wrapper for the html element "tr"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 */
public class HtmlTableRow extends ClickableElement {

    private final int row_;
    private List tableCells_;


    /**
     *  Create an instance
     *
     * @param  page The page that this element is contained within
     * @param  element The xml element that represents this html element
     * @param  row The row within the table that this cell is located at
     */
    HtmlTableRow( final HtmlPage page, final Element element, final int row ) {
        super( page, element );
        row_ = row;
    }


    /**
     *  Return the index of this row
     *
     * @return  See above
     */
    public int getRow() {
        return row_;
    }


    /**
     *  Return a List containing all the HtmlTableCell objects in this row
     *
     * @return  See above
     */
    public List getCells() {
        if( tableCells_ != null ) {
            return tableCells_;
        }

        tableCells_ = Collections.unmodifiableList(getCells( getElement() ));
        return tableCells_;
    }
    /**
     *  Return a List containing all the HtmlTableCell objects in this row
     * @param parent The parent element
     * @return  See above
     */
    private List getCells( final Element parent ) {
        if( tableCells_ != null ) {
            return tableCells_;
        }

        final List list = new ArrayList();

        final NodeList nodeList = parent.getChildNodes();
        final int nodeCount = nodeList.getLength();
        final HtmlPage page = getPage();

        final int rowIndex = getRow();
        int columnIndex = 0;

        for( int i = 0; i < nodeCount; i++ ) {
            final Node node = nodeList.item( i );
            if( node instanceof Element ) {
                final Element element = (Element)node;
                final String tagName = getTagName(element);

                if( tagName.equals( "td" ) ) {
                    list.add( new HtmlTableDataCell( page, element, rowIndex, columnIndex++ ) );
                }
                else if( tagName.equals( "th" ) ) {
                    list.add( new HtmlTableHeaderCell( page, element, rowIndex, columnIndex++ ) );
                }
                else if( tagName.equals( "form" ) ) {
                    // Completely illegal html but some of the big sites (ie amazon) do this
                    list.addAll( getCells(element) );
                }
                else {
                    getLog().debug("Illegal html: found <"+tagName+"> under a <tr> tag");
                }
            }
        }

        return list;
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
}
