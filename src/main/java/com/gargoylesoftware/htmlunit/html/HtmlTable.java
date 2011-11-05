/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "table".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlTable extends HtmlElement {

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
    HtmlTable(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the first cell that matches the specified row and column, searching left to right, top to bottom.
     * This method returns different values than getRow(rowIndex).getCell(cellIndex) because this takes cellspan
     * and rowspan into account.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the HtmlTableCell at that location or null if there are no cells at that location
     */
    public final HtmlTableCell getCellAt(final int rowIndex, final int columnIndex) {
        final RowIterator rowIterator = getRowIterator();
        final HashSet<Point> occupied = new HashSet<Point>();
        int row = 0;
        for (final HtmlTableRow htmlTableRow : rowIterator) {
            final HtmlTableRow.CellIterator cellIterator = htmlTableRow.getCellIterator();
            int col = 0;
            for (final HtmlTableCell cell : cellIterator) {
                while (occupied.contains(new Point(row, col))) {
                    col++;
                }
                final int nextRow = row + cell.getRowSpan();
                if (row <= rowIndex && nextRow > rowIndex) {
                    final int nextCol = col + cell.getColumnSpan();
                    if (col <= columnIndex && nextCol > columnIndex) {
                        return cell;
                    }
                }
                if (cell.getRowSpan() > 1 || cell.getColumnSpan() > 1) {
                    for (int i = 0; i < cell.getRowSpan(); i++) {
                        for (int j = 0; j < cell.getColumnSpan(); j++) {
                            occupied.add(new Point(row + i, col + j));
                        }
                    }
                }
                col++;
            }
            row++;
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
        for (final HtmlTableRow row : getRowIterator()) {
            result.add(row);
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
        for (final HtmlTableRow row : getRowIterator()) {
            if (count == index) {
                return row;
            }
            count++;
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
        for (final HtmlTableRow row : getRowIterator()) {
            if (row.getAttribute("id").equals(id)) {
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
        return getAttribute("summary");
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
        return getAttribute("width");
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
        return getAttribute("border");
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
        return getAttribute("frame");
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
        return getAttribute("rules");
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
        return getAttribute("cellspacing");
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
        return getAttribute("cellpadding");
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
        return getAttribute("align");
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
        return getAttribute("bgcolor");
    }

    /**
     * An iterator that moves over all rows in this table. The iterator will also
     * enter into nested row group elements (header, footer and body).
     */
    private class RowIterator implements Iterator<HtmlTableRow>, Iterable<HtmlTableRow> {
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
            throw new NoSuchElementException();
        }

        /**
         * Sets the internal position to the next row, starting at the given node.
         * @param node the node to mark as the next row; if this is not a row, the
         *        next reachable row will be marked.
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

        public Iterator<HtmlTableRow> iterator() {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isBlock() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return <code>true</code> as browsers ignore self closing <code>table</code> tags.
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }
}
