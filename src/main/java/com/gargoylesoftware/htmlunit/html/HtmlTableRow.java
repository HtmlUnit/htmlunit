/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "tr".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlTableRow extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "tr";

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that this element is contained within
     * @param attributes the initial attributes
     */
    HtmlTableRow(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * @return an Iterator over the all HtmlTableCell objects in this row
     */
    public CellIterator getCellIterator() {
        return new CellIterator();
    }

    /**
     * @return an immutable list containing all the HtmlTableCells held by this object
     * @see #getCellIterator
     */
    public List<HtmlTableCell> getCells() {
        final List<HtmlTableCell> result = new ArrayList<HtmlTableCell>();
        for (final HtmlTableCell cell : getCellIterator()) {
            result.add(cell);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * @param index the 0-based index
     * @return the cell at the given index
     * @throws IndexOutOfBoundsException if there is no cell at the given index
     */
    public HtmlTableCell getCell(final int index) throws IndexOutOfBoundsException {
        int count = 0;
        for (final HtmlTableCell cell : getCellIterator()) {
            if (count == index) {
                return cell;
            }
            count++;
        }
        throw new IndexOutOfBoundsException();
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
     * Returns the value of the attribute "char". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "char"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharAttribute() {
        return getAttribute("char");
    }

    /**
     * Returns the value of the attribute "charoff". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "charoff"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharoffAttribute() {
        return getAttribute("charoff");
    }

    /**
     * Returns the value of the attribute "valign". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "valign"
     * or an empty string if that attribute isn't defined.
     */
    public final String getValignAttribute() {
        return getAttribute("valign");
    }

    /**
     * Gets the table containing this row.
     * @return the table
     */
    public HtmlTable getEnclosingTable() {
        return (HtmlTable) getEnclosingElement("table");
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
     * An Iterator over the HtmlTableCells contained in this row. It will also dive
     * into nested forms, even though that is illegal HTML.
     */
    public class CellIterator implements Iterator<HtmlTableCell>, Iterable<HtmlTableCell> {
        private HtmlTableCell nextCell_;
        private HtmlForm currentForm_;

        /** Creates an instance. */
        public CellIterator() {
            setNextCell(getFirstChild());
        }

        /** @return whether there is another cell available */
        public boolean hasNext() {
            return nextCell_ != null;
        }

        /**
         * @return the next cell
         * @throws NoSuchElementException if no cell is available
         */
        public HtmlTableCell next() throws NoSuchElementException {
            return nextCell();
        }

        /**
         * Removes the cell under the cursor from the current row.
         * @throws IllegalStateException if there is no current row
         */
        public void remove() throws IllegalStateException {
            if (nextCell_ == null) {
                throw new IllegalStateException();
            }
            final DomNode sibling = nextCell_.getPreviousSibling();
            if (sibling != null) {
                sibling.remove();
            }
        }

        /**
         * @return the next cell
         * @throws NoSuchElementException if no cell is available
         */
        public HtmlTableCell nextCell() throws NoSuchElementException {
            if (nextCell_ != null) {
                final HtmlTableCell result = nextCell_;
                setNextCell(nextCell_.getNextSibling());
                return result;
            }
            throw new NoSuchElementException();
        }

        /**
         * Sets the internal position to the next cell, starting at the given node
         * @param node the node to mark as the next cell; if this is not a cell, the
         *        next reachable cell will be marked.
         */
        private void setNextCell(final DomNode node) {
            nextCell_ = null;
            for (DomNode next = node; next != null; next = next.getNextSibling()) {
                if (next instanceof HtmlTableCell) {
                    nextCell_ = (HtmlTableCell) next;
                    return;
                }
                else if (currentForm_ == null && next instanceof HtmlForm) {
                    // Completely illegal HTML but some of the big sites (ie amazon) do this
                    currentForm_ = (HtmlForm) next;
                    setNextCell(next.getFirstChild());
                    return;
                }
            }
            if (currentForm_ != null) {
                final DomNode form = currentForm_;
                currentForm_ = null;
                setNextCell(form.getNextSibling());
            }
        }

        /**
         * Returns an HtmlTableCell iterator.
         *
         * @return an HtmlTableCell Iterator.
         */
        public Iterator<HtmlTableCell> iterator() {
            return this;
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the default display style.
     *
     * @return the default display style.
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.TABLE_ROW;
    }
}
