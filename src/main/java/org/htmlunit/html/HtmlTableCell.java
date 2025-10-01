/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.html;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.SgmlPage;

/**
 * An abstract cell that provides the implementation for HtmlTableDataCell and HtmlTableHeaderCell.
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Christian Sell
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Lai Quang Duong
 * @see HtmlTableDataCell
 * @see HtmlTableHeaderCell
 */
public abstract class HtmlTableCell extends HtmlElement {

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that this element is contained within
     * @param attributes the initial attributes
     */
    protected HtmlTableCell(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * @return the value of the colspan attribute, or <code>1</code> if the attribute wasn't specified
     */
    public int getColumnSpan() {
        final String spanString = StringUtils.replaceChars(getAttributeDirect("colspan"), "\r\n\t ", null);
        if (spanString == null || spanString.isEmpty()) {
            return 1;
        }
        try {
            final int span = (int) Double.parseDouble(spanString);
            if (span < 1) {
                return 1;
            }
            return Math.min(span, 1_000);
        }
        catch (final NumberFormatException e) {
            return 1;
        }
    }

    /**
     * @return the value of the rowspan attribute, or <code>1</code> if the attribute wasn't specified
     */
    public int getRowSpan() {
        final String spanString = StringUtils.replaceChars(getAttributeDirect("rowspan"), "\r\n\t ", null);
        if (spanString == null || spanString.isEmpty()) {
            return 1;
        }
        try {
            final int span = (int) Double.parseDouble(spanString);
            if (span < 0) {
                return 1;
            }
            if (span < 1) {
                return 0;
            }

            return Math.min(span, 65_534);
        }
        catch (final NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Returns the table row containing this cell.
     * @return the table row containing this cell
     */
    public HtmlTableRow getEnclosingRow() {
        return (HtmlTableRow) getEnclosingElement("tr");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.TABLE_CELL;
    }
}
