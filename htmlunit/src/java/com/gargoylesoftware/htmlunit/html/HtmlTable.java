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

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  Wrapper for the html element "table"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 */
public class HtmlTable extends ClickableElement {
    private List tableRows_;


    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element The xml element that represents this html element
     */
    HtmlTable( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Return the first cell that matches the specified row and column,
     *  searching left to right, top to bottom.
     *
     * @param  row The row index
     * @param  column The column index
     * @return  The HtmlTableCell at that location or null if there are no cells
     *      at that location
     */
    public final HtmlTableCell getCellAt( final int row, final int column ) {
        final List tableRows = getRows();
        final Iterator rowIterator = tableRows.iterator();
        while( rowIterator.hasNext() ) {
            final List tableCells = ( ( HtmlTableRow )rowIterator.next() ).getCells();
            final Iterator cellIterator = tableCells.iterator();
            while( cellIterator.hasNext() ) {
                final HtmlTableCell cell = ( HtmlTableCell )cellIterator.next();

                if( cell.matchesPosition( row, column ) ) {
                    return cell;
                }
            }
        }

        return null;
    }


    /**
     *  Return an immutable list containing all the HtmlTableRow objects
     *
     * @return  See above
     */
    public final synchronized List getRows() {
        // Implementation note:  This method can't be rewritten to use
        // getChildElements() as that would dump you into an infinite
        // loop.

        if( tableRows_ != null ) {
            return tableRows_;
        }

        final List list = new ArrayList();

        final NodeList nodeList = getElement().getChildNodes();
        final int nodeCount = nodeList.getLength();
        final HtmlPage page = getPage();

        int rowIndex = 0;

        for( int i = 0; i < nodeCount; i++ ) {
            final Node node = nodeList.item( i );
            if( node instanceof Element ) {
                final Element element = ( Element )node;
                final String tagName = getTagName(element);
                if( tagName.equals( "tr" ) ) {
                    list.add( new HtmlTableRow( page, element, rowIndex ) );
                    rowIndex++;
                }
                else if( tagName.equals("thead")
                    || tagName.equals("tbody")
                    || tagName.equals("tfoot") ) {
                    final NodeList subList = node.getChildNodes();
                    final int subListCount = subList.getLength();
                    for( int subListIndex = 0; subListIndex<subListCount; subListIndex++) {
                        final Node subNode = subList.item(subListIndex);
                        if( subNode instanceof Element ) {
                            final Element subElement = (Element)subNode;
                            if( getTagName(subElement).equals("tr") ) {
                                list.add( new HtmlTableRow( page, subElement, rowIndex ) );
                                rowIndex++;
                            }
                        }
                    }
                }
            }
        }

        tableRows_ = Collections.unmodifiableList( list );
        return tableRows_;
    }


    /**
     *  Return the number of rows in this table
     *
     * @return  The number of rows in this table
     */
    public final int getRowCount() {
        return getRows().size();
    }


    /**
     * Find and return the row with the specified id.
     *
     * @param id The id of the row
     * @return The row with the specified id.
     * @exception ElementNotFoundException If the row cannot be found.
     */
    public final HtmlTableRow getRowById( final String id )
        throws ElementNotFoundException {

        Assert.notNull("id", id);
        assertNotEmpty("id", id);

        final Iterator iterator = getRows().iterator();
        while( iterator.hasNext() ) {
            final HtmlTableRow row = (HtmlTableRow)iterator.next();
            if( row.getIdAttribute().equals(id) ) {
                return row;
            }
        }

        throw new ElementNotFoundException( "tr", "id", id );
    }


    /**
     * Return the table caption text or an empty string if a caption wasn't specified
     *
     * @return The caption text
     */
    public String getCaptionText() {
        final Iterator iterator = getChildElements().iterator();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if( element instanceof HtmlCaption ) {
                return element.asText();
            }
        }
        return null;
    }


    /**
     * Return the table header or null if a header wasn't specified
     *
     * @return The table header
     */
    public HtmlTableHeader getHeader() {
        final Iterator iterator = getChildElements().iterator();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if( element instanceof HtmlTableHeader ) {
                return (HtmlTableHeader)element;
            }
        }
        return null;
    }


    /**
     * Return the table footer or null if a footer wasn't specified
     *
     * @return The table footer
     */
    public HtmlTableFooter getFooter() {
        final Iterator iterator = getChildElements().iterator();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if( element instanceof HtmlTableFooter ) {
                return (HtmlTableFooter)element;
            }
        }
        return null;
    }


    /**
     * Return a list of tables bodies defined in this table.  If no bodies were defined
     * then an empty list will be returned.
     *
     * @return A list of {@link com.gargoylesoftware.htmlunit.html.HtmlTableBody} objects.
     */
    public List getBodies() {
        final List bodies = new ArrayList();
        final Iterator iterator = getChildElements().iterator();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if( element instanceof HtmlTableBody ) {
                bodies.add( element );
            }
        }
        return bodies;
    }


    /**
     * Return the value of the attribute "summary".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "summary"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSummaryAttribute() {
        return getAttributeValue("summary");
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
     * Return the value of the attribute "border".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "border"
     * or an empty string if that attribute isn't defined.
     */
    public final String getBorderAttribute() {
        return getAttributeValue("border");
    }


    /**
     * Return the value of the attribute "frame".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "frame"
     * or an empty string if that attribute isn't defined.
     */
    public final String getFrameAttribute() {
        return getAttributeValue("frame");
    }


    /**
     * Return the value of the attribute "rules".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "rules"
     * or an empty string if that attribute isn't defined.
     */
    public final String getRulesAttribute() {
        return getAttributeValue("rules");
    }


    /**
     * Return the value of the attribute "cellspacing".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "cellspacing"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCellSpacingAttribute() {
        return getAttributeValue("cellspacing");
    }


    /**
     * Return the value of the attribute "cellpadding".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "cellpadding"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCellPaddingAttribute() {
        return getAttributeValue("cellpadding");
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
