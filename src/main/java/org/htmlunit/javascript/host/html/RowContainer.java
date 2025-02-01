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
package org.htmlunit.javascript.host.html;

import java.io.Serializable;
import java.util.function.Predicate;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTableRow;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.DOMException;

/**
 * Superclass for all row-containing JavaScript host classes, including tables,
 * table headers, table bodies and table footers.
 *
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@JsxClass(isJSObject = false)
public class RowContainer extends HTMLElement {

    /**
     * Returns the rows in the element.
     * @return the rows in the element
     */
    @JsxGetter
    public HTMLCollection getRows() {
        final HTMLCollection rows = new HTMLCollection(getDomNodeOrDie(), false);
        rows.setIsMatchingPredicate(
                (Predicate<DomNode> & Serializable)
                node -> node instanceof HtmlTableRow && isContainedRow((HtmlTableRow) node));
        return rows;
    }

    /**
     * Indicates if the row belongs to this container.
     * @param row the row to test
     * @return {@code true} if it belongs to this container
     */
    protected boolean isContainedRow(final HtmlTableRow row) {
        return row.getParentNode() == getDomNodeOrDie();
    }

    /**
     * Deletes the row at the specified index.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536408.aspx">MSDN Documentation</a>
     * @param rowIndex the zero-based index of the row to delete
     */
    @JsxFunction
    public void deleteRow(int rowIndex) {
        final HTMLCollection rows = getRows();
        final int rowCount = rows.getLength();
        if (rowIndex == -1) {
            rowIndex = rowCount - 1;
        }
        final boolean rowIndexValid = rowIndex >= 0 && rowIndex < rowCount;
        if (rowIndexValid) {
            final HtmlUnitScriptable row = (HtmlUnitScriptable) rows.item(Integer.valueOf(rowIndex));
            row.getDomNodeOrDie().remove();
        }
    }

    /**
     * Inserts a new row at the specified index in the element's row collection. If the index
     * is -1 or there is no index specified, then the row is appended at the end of the
     * element's row collection.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536457.aspx">MSDN Documentation</a>
     * @param index specifies where to insert the row in the rows collection.
     *        The default value is -1, which appends the new row to the end of the rows collection
     * @return the newly-created row
     */
    @JsxFunction
    public HtmlUnitScriptable insertRow(final Object index) {
        int rowIndex = -1;
        if (!JavaScriptEngine.isUndefined(index)) {
            rowIndex = (int) JavaScriptEngine.toNumber(index);
        }
        final HTMLCollection rows = getRows();
        final int rowCount = rows.getLength();
        final int r;
        if (rowIndex == -1 || rowIndex == rowCount) {
            r = Math.max(0, rowCount);
        }
        else {
            r = rowIndex;
        }

        if (r < 0 || r > rowCount) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    new DOMException(
                            "Index or size is negative or greater than the allowed amount "
                                    + "(index: " + rowIndex + ", " + rowCount + " rows)",
                            DOMException.INDEX_SIZE_ERR));
        }

        return insertRow(r);
    }

    /**
     * Inserts a new row at the given position.
     * @param index the index where the row should be inserted (0 &lt;= index &lt;= nbRows)
     * @return the inserted row
     */
    public HtmlUnitScriptable insertRow(final int index) {
        final HTMLCollection rows = getRows();
        final int rowCount = rows.getLength();
        final DomElement newRow = ((HtmlPage) getDomNodeOrDie().getPage()).createElement("tr");
        if (rowCount == 0) {
            getDomNodeOrDie().appendChild(newRow);
        }
        else if (index == rowCount) {
            final HtmlUnitScriptable row = (HtmlUnitScriptable) rows.item(Integer.valueOf(index - 1));
            row.getDomNodeOrDie().getParentNode().appendChild(newRow);
        }
        else {
            final HtmlUnitScriptable row = (HtmlUnitScriptable) rows.item(Integer.valueOf(index));
            // if at the end, then in the same "sub-container" as the last existing row
            if (index > rowCount - 1) {
                row.getDomNodeOrDie().getParentNode().appendChild(newRow);
            }
            else {
                row.getDomNodeOrDie().insertBefore(newRow);
            }
        }
        return getScriptableFor(newRow);
    }

    /**
     * Returns the value of the {@code align} property.
     * @return the value of the {@code align} property
     */
    @JsxGetter
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     */
    @JsxSetter
    public void setAlign(final String align) {
        setAlign(align, false);
    }

}
