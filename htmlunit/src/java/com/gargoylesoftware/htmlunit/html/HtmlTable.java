/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  Wrapper for the html element "table"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlTable extends HtmlElement {
    private final Map elements_ = new HashMap( 89 );
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

        assertNotNull("id", id);
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
     * Return the value of the attribute "id".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "id"
     * or an empty string if that attribute isn't defined.
     */
    public final String getIdAttribute() {
        return getAttributeValue("id");
    }


    /**
     * Return the value of the attribute "class".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "class"
     * or an empty string if that attribute isn't defined.
     */
    public final String getClassAttribute() {
        return getAttributeValue("class");
    }


    /**
     * Return the value of the attribute "style".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "style"
     * or an empty string if that attribute isn't defined.
     */
    public final String getStyleAttribute() {
        return getAttributeValue("style");
    }


    /**
     * Return the value of the attribute "title".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "title"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTitleAttribute() {
        return getAttributeValue("title");
    }


    /**
     * Return the value of the attribute "lang".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLangAttribute() {
        return getAttributeValue("lang");
    }


    /**
     * Return the value of the attribute "xml:lang".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "xml:lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getXmlLangAttribute() {
        return getAttributeValue("xml:lang");
    }


    /**
     * Return the value of the attribute "dir".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "dir"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTextDirectionAttribute() {
        return getAttributeValue("dir");
    }


    /**
     * Return the value of the attribute "onclick".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onclick"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnClickAttribute() {
        return getAttributeValue("onclick");
    }


    /**
     * Return the value of the attribute "ondblclick".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "ondblclick"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnDblClickAttribute() {
        return getAttributeValue("ondblclick");
    }


    /**
     * Return the value of the attribute "onmousedown".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmousedown"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseDownAttribute() {
        return getAttributeValue("onmousedown");
    }


    /**
     * Return the value of the attribute "onmouseup".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseup"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseUpAttribute() {
        return getAttributeValue("onmouseup");
    }


    /**
     * Return the value of the attribute "onmouseover".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseover"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseOverAttribute() {
        return getAttributeValue("onmouseover");
    }


    /**
     * Return the value of the attribute "onmousemove".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmousemove"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseMoveAttribute() {
        return getAttributeValue("onmousemove");
    }


    /**
     * Return the value of the attribute "onmouseout".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseout"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseOutAttribute() {
        return getAttributeValue("onmouseout");
    }


    /**
     * Return the value of the attribute "onkeypress".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeypress"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyPressAttribute() {
        return getAttributeValue("onkeypress");
    }


    /**
     * Return the value of the attribute "onkeydown".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeydown"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyDownAttribute() {
        return getAttributeValue("onkeydown");
    }


    /**
     * Return the value of the attribute "onkeyup".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeyup"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyUpAttribute() {
        return getAttributeValue("onkeyup");
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
