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

import org.w3c.dom.Element;

/**
 *  An abstract cell that provides the implementation for HtmlTableDataCell and
 *  HtmlTableHeaderCell.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @see  HtmlTableDataCell
 * @see  HtmlTableHeaderCell
 */
public abstract class HtmlTableCell extends ClickableElement {
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

