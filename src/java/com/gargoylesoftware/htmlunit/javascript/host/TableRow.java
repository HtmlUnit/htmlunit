/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.jaxen.JaxenException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.ElementArray;

/**
 * A JavaScript object representing a TR.
 * 
 * @author Marc Guillemot
 * @version $Revision$
 */
public class TableRow extends HTMLElement {
    private ElementArray cells_; // has to be a member to have equality (==) working

    /**
     * Create an instance.
     */
    public TableRow() {
    }

    /**
     * Javascript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Returns the index of the row within parent's table
     * @return the index.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/rowindex.asp">
     * MSDN Documentation</a>
     */
    public int jsGet_rowIndex() {
        final HtmlTableRow row = (HtmlTableRow) getHtmlElementOrDie();
        final HtmlTable table = row.getEnclosingTable();
        return table.getRows().indexOf(row);
    }

    /**
     * Returns the rows in the element.
     * @return The rows in the element.
     */
    public Object jsGet_cells() {
        if (cells_ == null) {
            cells_ = (ElementArray) makeJavaScriptObject(ElementArray.JS_OBJECT_NAME);
            try {
                cells_.init(getDomNodeOrDie(), new HtmlUnitXPath(".//td"));
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize row.cells: " + e.getMessage());
            }
        }
        return cells_;
    }

    /**
     * Inserts a new cell at the specified index in the element's cells collection. If the index
     * is -1 or there is no index specified, then the cell is appended at the end of the
     * element's cells collection.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/insertcell.asp">
     * MSDN Documentation</a>
     * @param cx the current JavaScript context.
     * @param s this scriptable object.
     * @param args the arguments for the function call.
     * @param f the function object that invoked this function.
     * @return the newly-created cell.
     */
    public static Object jsFunction_insertCell(final Context cx, final Scriptable s, 
            final Object[] args, final Function f) {
        final TableRow row = (TableRow) s;
        final HtmlTableRow htmlRow = (HtmlTableRow) row.getDomNodeOrDie();
        
        final int position = getIntArg(0, args, -1);

        final boolean indexValid = (position >= -1 && position <= htmlRow.getCells().size());
        if (indexValid) {
            final HtmlElement newCell = htmlRow.getPage().createElement("td");
            if (position == -1 || position == htmlRow.getCells().size()) {
                htmlRow.appendChild(newCell);
            }
            else {
                htmlRow.getCell(position).insertBefore(newCell);
            }
            return row.getScriptableFor(newCell);
        }
        else {
            throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount");
        }
    }
}
