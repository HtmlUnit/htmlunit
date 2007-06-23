/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 *  Wrapper for the html element "tr"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlTableRow extends ClickableElement {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "tr";

    /**
     *  Create an instance
     *
     * @param  page The page that this element is contained within
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlTableRow.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlTableRow(final HtmlPage page, final Map attributes) {
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
    HtmlTableRow(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
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
    public List getCells() {
        final List result = new ArrayList();
        for(final CellIterator iterator = getCellIterator(); iterator.hasNext(); ) {
            result.add(iterator.next());
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
        for(final CellIterator iterator = getCellIterator(); iterator.hasNext(); count++) {
            final HtmlTableCell next = iterator.nextCell();
            if(count == index) {
                return next;
            }
        }
        throw new IndexOutOfBoundsException();
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
     * Gets the table containing this row
     * @return the table
     */
    public HtmlTable getEnclosingTable() {
        return (HtmlTable) getEnclosingElement("table");
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
     * an Iterator over the HtmlTableCells contained in this row. It will also dive
     * into nested forms, even though that is illegal HTML
     */
    public class CellIterator implements Iterator {

        private HtmlTableCell nextCell_;
        private HtmlForm currentForm_;

        /** create an instance */
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
        public Object next() throws NoSuchElementException {
            return nextCell();
        }

        /**
         * remove the cell under the cursor from the current row
         * @throws IllegalStateException if there is no currenr row
         */
        public void remove() throws IllegalStateException {
            if(nextCell_ == null) {
                throw new IllegalStateException();
            }
            if(nextCell_.getPreviousSibling() != null) {
                nextCell_.getPreviousSibling().remove();
            }
        }

        /**
         * @return the next cell
         * @throws NoSuchElementException if no cell is available
         */
        public HtmlTableCell nextCell() throws NoSuchElementException {

            if(nextCell_ != null) {
                final HtmlTableCell result = nextCell_;
                setNextCell(nextCell_.getNextSibling());
                return result;
            }
            else {
                throw new NoSuchElementException();
            }
        }

        /**
         * set the internal position to the next cell, starting at the given node
         * @param node the node to mark as the next cell. If this is not a cell, the
         * next reachable cell will be marked
         */
        private void setNextCell(final DomNode node) {

            nextCell_ = null;
            for(DomNode next = node; next != null; next = next.getNextSibling()) {
                if(next instanceof HtmlTableCell) {
                    nextCell_ = (HtmlTableCell)next;
                    return;
                }
                else if(currentForm_ == null && next instanceof HtmlForm) {
                    // Completely illegal html but some of the big sites (ie amazon) do this
                    currentForm_ = (HtmlForm)next;
                    setNextCell(next.getFirstChild());
                    return;
                }
            }
            if(currentForm_ != null) {
                final DomNode form = currentForm_;
                currentForm_ = null;
                setNextCell(form.getNextSibling());
            }
        }
    }
}
