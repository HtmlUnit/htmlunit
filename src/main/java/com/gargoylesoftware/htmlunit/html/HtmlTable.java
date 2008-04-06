/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

/**
 * Wrapper for the HTML element "table".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlTable extends ClickableElement {

    private static final long serialVersionUID = 2484055580262042798L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "table";

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlTable(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the first cell that matches the specified row and column, searching left to right, top to bottom.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the HtmlTableCell at that location or null if there are no cells at that location
     */
    public final HtmlTableCell getCellAt(final int rowIndex, final int columnIndex) {
        final RowIterator rowIterator = getRowIterator();
        for (int rowNo = 0; rowIterator.hasNext(); rowNo++) {
            final HtmlTableRow row = rowIterator.nextRow();
            final HtmlTableRow.CellIterator cellIterator = row.getCellIterator();
            for (int colNo = 0; cellIterator.hasNext(); colNo++) {
                final HtmlTableCell cell = cellIterator.nextCell();
                if (rowNo <= rowIndex && rowNo + cell.getRowSpan() > rowIndex) {
                    if (colNo <= columnIndex && colNo + cell.getColumnSpan() > columnIndex) {
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return an iterator over all the HtmlTableRow objects
     */
    private RowIterator getRowIterator() {
        return new RowIterator();
    }

    /**
     * @return an immutable list containing all the HtmlTableRow objects
     * @see #getRowIterator
     */
    public List<HtmlTableRow> getRows() {
        final List<HtmlTableRow> result = new ArrayList<HtmlTableRow>();
        for (final RowIterator iterator = getRowIterator(); iterator.hasNext();) {
            result.add(iterator.next());
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * @param index the 0-based index of the row
     * @return the HtmlTableRow at the given index
     * @throws IndexOutOfBoundsException if there is no row at the given index
     * @see #getRowIterator
     */
    public HtmlTableRow getRow(final int index) throws IndexOutOfBoundsException {
        int count = 0;
        for (final RowIterator iterator = getRowIterator(); iterator.hasNext(); count++) {
            final HtmlTableRow next = iterator.nextRow();
            if (count == index) {
                return next;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Computes the number of rows in this table. Note that the count is computed dynamically
     * by iterating over all rows.
     *
     * @return the number of rows in this table
     */
    public final int getRowCount() {
        int count = 0;
        for (final RowIterator iterator = getRowIterator(); iterator.hasNext(); iterator.next()) {
            count++;
        }
        return count;
    }

    /**
     * Finds and return the row with the specified id.
     *
     * @param id the id of the row
     * @return the row with the specified id
     * @exception ElementNotFoundException If the row cannot be found.
     */
    public final HtmlTableRow getRowById(final String id) throws ElementNotFoundException {
        final RowIterator iterator = new RowIterator();
        while (iterator.hasNext()) {
            final HtmlTableRow row = (HtmlTableRow) iterator.next();
            if (row.getIdAttribute().equals(id)) {
                return row;
            }
        }
        throw new ElementNotFoundException("tr", "id", id);
    }

    /**
     * Returns the table caption text or an empty string if a caption wasn't specified.
     *
     * @return the caption text
     */
    public String getCaptionText() {
        for (final HtmlElement element : getChildElements()) {
            if (element instanceof HtmlCaption) {
                return element.asText();
            }
        }
        return null;
    }

    /**
     * Returns the table header or null if a header wasn't specified.
     *
     * @return the table header
     */
    public HtmlTableHeader getHeader() {
        for (final HtmlElement element : getChildElements()) {
            if (element instanceof HtmlTableHeader) {
                return (HtmlTableHeader) element;
            }
        }
        return null;
    }

    /**
     * Returns the table footer or null if a footer wasn't specified.
     *
     * @return the table footer
     */
    public HtmlTableFooter getFooter() {
        for (final HtmlElement element : getChildElements()) {
            if (element instanceof HtmlTableFooter) {
                return (HtmlTableFooter) element;
            }
        }
        return null;
    }

    /**
     * Returns a list of tables bodies defined in this table. If no bodies were defined
     * then an empty list will be returned.
     *
     * @return a list of {@link com.gargoylesoftware.htmlunit.html.HtmlTableBody} objects
     */
    public List<HtmlTableBody> getBodies() {
        final List<HtmlTableBody> bodies = new ArrayList<HtmlTableBody>();
        for (final HtmlElement element : getChildElements()) {
            if (element instanceof HtmlTableBody) {
                bodies.add((HtmlTableBody) element);
            }
        }
        return bodies;
    }

    /**
     * Returns the value of the attribute "summary". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "summary"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSummaryAttribute() {
        return getAttributeValue("summary");
    }

    /**
     * Returns the value of the attribute "width". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "width"
     * or an empty string if that attribute isn't defined.
     */
    public final String getWidthAttribute() {
        return getAttributeValue("width");
    }

    /**
     * Returns the value of the attribute "border". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "border"
     * or an empty string if that attribute isn't defined.
     */
    public final String getBorderAttribute() {
        return getAttributeValue("border");
    }

    /**
     * Returns the value of the attribute "frame". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "frame"
     * or an empty string if that attribute isn't defined.
     */
    public final String getFrameAttribute() {
        return getAttributeValue("frame");
    }

    /**
     * Returns the value of the attribute "rules". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rules"
     * or an empty string if that attribute isn't defined.
     */
    public final String getRulesAttribute() {
        return getAttributeValue("rules");
    }

    /**
     * Returns the value of the attribute "cellspacing". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "cellspacing"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCellSpacingAttribute() {
        return getAttributeValue("cellspacing");
    }

    /**
     * Returns the value of the attribute "cellpadding". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "cellpadding"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCellPaddingAttribute() {
        return getAttributeValue("cellpadding");
    }

    /**
     * Returns the value of the attribute "align". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "align"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttributeValue("align");
    }

    /**
     * Returns the value of the attribute "bgcolor". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "bgcolor"
     * or an empty string if that attribute isn't defined.
     */
    public final String getBgcolorAttribute() {
        return getAttributeValue("bgcolor");
    }

    /**
     * An iterator that moves over all rows in this table. The iterator will also
     * enter into nested row group elements (header, footer and body).
     */
    private class RowIterator implements Iterator<HtmlTableRow> {
        private HtmlTableRow nextRow_;
        private TableRowGroup currentGroup_;

        /** Creates a new instance. */
        public RowIterator() {
            setNextRow(getFirstChild());
        }

        /**
         * @return <code>true</code> if there are more rows available
         */
        public boolean hasNext() {
            return nextRow_ != null;
        }

        /**
         * @return the next row from this iterator
         * @throws NoSuchElementException if no more rows are available
         */
        public HtmlTableRow next() throws NoSuchElementException {
            return nextRow();
        }

        /**
         * Removes the current row from the underlying table.
         * @throws IllegalStateException if there is no current element
         */
        public void remove() throws IllegalStateException {
            if (nextRow_ == null) {
                throw new IllegalStateException();
            }
            final DomNode sibling = nextRow_.getPreviousSibling();
            if (sibling != null) {
                sibling.remove();
            }
        }

        /**
         * @return the next row from this iterator
         * @throws NoSuchElementException if no more rows are available
         */
        public HtmlTableRow nextRow() throws NoSuchElementException {
            if (nextRow_ != null) {
                final HtmlTableRow result = nextRow_;
                setNextRow(nextRow_.getNextSibling());
                return result;
            }
            else {
                throw new NoSuchElementException();
            }
        }

        /**
         * Sets the internal position to the next row, starting at the given node.
         * @param node the node to mark as the next row. If this is not a row, the
         * next reachable row will be marked.
         */
        private void setNextRow(final DomNode node) {
            nextRow_ = null;
            for (DomNode next = node; next != null; next = next.getNextSibling()) {
                if (next instanceof HtmlTableRow) {
                    nextRow_ = (HtmlTableRow) next;
                    return;
                }
                else if (currentGroup_ == null && next instanceof TableRowGroup) {
                    currentGroup_ = (TableRowGroup) next;
                    setNextRow(next.getFirstChild());
                    return;
                }
            }
            if (currentGroup_ != null) {
                final DomNode group = currentGroup_;
                currentGroup_ = null;
                setNextRow(group.getNextSibling());
            }
        }
    }
}
