/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;

/**
 *  An abstract cell that provides the implementation for HtmlTableData and
 *  HtmlTableHeader.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @see  HtmlTableData
 * @see  HtmlTableHeader
 */
public abstract class HtmlTableCell extends HtmlElement {
    private final int rowIndex_;
    private final int columnIndex_;


    /**
     *  Create an instance
     *
     * @param  page The page that this element is contained within
     * @param  element The xml element that this object is wrapping
     * @param  row The starting row index for this cell
     * @param  column The starting column index for this cell
     */
    HtmlTableCell(
            final HtmlPage page,
            final Element element,
            final int row,
            final int column ) {
        super( page, element );

        rowIndex_ = row;
        columnIndex_ = column;
    }


    /**
     *  Return the first column that this cell appears in the table. Check with
     *  the getColumnSpan() method to determine if this cell spans multiple
     *  columns
     *
     * @return  See above
     */
    public int getColumnIndex() {
        return columnIndex_;
    }


    /**
     *  Return the value of the colspan attribute or 1 if the attribute wasn't
     *  specified
     *
     * @return  See above
     */
    public int getColumnSpan() {
        final String spanString = getAttributeValue( "colspan" );
        if( spanString == null || spanString.length() == 0 ) {
            return 1;
        }
        else {
            return Integer.parseInt( spanString );
        }
    }


    /**
     *  Return the first row that this cell appears in the table. Check with the
     *  getRowSpan() method to determine if this cell spans multiple rows
     *
     * @return  See above
     */
    public int getRowIndex() {
        return rowIndex_;
    }


    /**
     *  Return the value of the rowspan attribute or 1 if the attribute wasn't
     *  specified
     *
     * @return  See above
     */
    public int getRowSpan() {
        final String spanString = getAttributeValue( "rowspan" );
        if( spanString == null || spanString.length() == 0 ) {
            return 1;
        }
        else {
            return Integer.parseInt( spanString );
        }
    }


    /**
     *  Return true if this cell occupies the specified row and column
     *
     * @param  row The index of the row
     * @param  column The index of the column
     * @return  true or false
     */
    public boolean matchesPosition( final int row, final int column ) {
        final int rowMin = getRowIndex();
        final int rowMax = rowMin + getRowSpan() - 1;
        final int columnMin = getColumnIndex();
        final int columnMax = columnMin + getColumnSpan() - 1;

        return row >= rowMin
                 && row <= rowMax
                 && column >= columnMin
                 && column <= columnMax;
    }
}

