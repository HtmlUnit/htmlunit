/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Undefined;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * A JavaScript object representing a TR.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLTableRowElement extends HTMLElement {
    private static final long serialVersionUID = 3256441404401397812L;
    private HTMLCollection cells_; // has to be a member to have equality (==) working

    /**
     * Create an instance.
     */
    public HTMLTableRowElement() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Returns the index of the row within parent's table.
     * @return the index
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534377.aspx">MSDN Documentation</a>
     */
    public int jsxGet_rowIndex() {
        final HtmlTableRow row = (HtmlTableRow) getDomNodeOrDie();
        final HtmlTable table = row.getEnclosingTable();
        return table.getRows().indexOf(row);
    }

    /**
     * Returns the cells in the row.
     * @return the cells in the row
     */
    public Object jsxGet_cells() {
        if (cells_ == null) {
            cells_ = new HTMLCollection(this);
            cells_.init(getDomNodeOrDie(), "./td|th");
        }
        return cells_;
    }

    /**
     * Inserts a new cell at the specified index in the element's cells collection. If the index
     * is -1 or there is no index specified, then the cell is appended at the end of the
     * element's cells collection.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536455.aspx">MSDN Documentation</a>
     * @param index specifies where to insert the cell in the tr.
     *        The default value is -1, which appends the new cell to the end of the cells collection
     * @return the newly-created cell
     */
    public Object jsxFunction_insertCell(final Object index) {
        int position = -1;
        if (index != Undefined.instance) {
            position = (int) Context.toNumber(index);
        }
        final HtmlTableRow htmlRow = (HtmlTableRow) getDomNodeOrDie();

        final boolean indexValid = (position >= -1 && position <= htmlRow.getCells().size());
        if (indexValid) {
            final HtmlElement newCell = ((HtmlPage) htmlRow.getPage()).createElement("td");
            if (position == -1 || position == htmlRow.getCells().size()) {
                htmlRow.appendChild(newCell);
            }
            else {
                htmlRow.getCell(position).insertBefore(newCell);
            }
            return getScriptableFor(newCell);
        }
        throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount");
    }
}
