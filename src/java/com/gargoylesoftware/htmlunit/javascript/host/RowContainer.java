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
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.ElementArray;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * Superclass for all row-containing JavaScript host classes, including tables,
 * table headers, table bodies and table footers.
 * 
 * @version $Revision$
 * @author Daniel Gredler
 * @author Chris Erskine
 */
public class RowContainer extends HTMLElement {

    private static final long serialVersionUID = 3258129146093056308L;
    private ElementArray rows_; // has to be a member to have equality (==) working

    /**
     * Create an instance.
     */
    public RowContainer() {
    }

    /**
     * Javascript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Returns the rows in the element.
     * @return The rows in the element.
     */
    public Object jsxGet_rows() {
        if (rows_ == null) {
            rows_ = (ElementArray) makeJavaScriptObject(ElementArray.JS_OBJECT_NAME);
            try {
                rows_.init(getDomNodeOrDie(), new HtmlUnitXPath(".//tr"));
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize rowContainer.rows: " + e.getMessage());
            }
        }
        return rows_;
    }

    /**
     * Deletes the row at the specified index.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/deleterow.asp">
     * MSDN Documentation</a>
     * @param rowIndex the zero-based index of the row to delete.
     */
    public void jsxFunction_deleteRow(final int rowIndex) {
        final ElementArray rows = (ElementArray) jsxGet_rows();
        final boolean rowIndexValid = (rowIndex >= 0 && rowIndex < rows.jsGet_length());
        if (rowIndexValid) {
            final SimpleScriptable row = (SimpleScriptable) rows.jsFunction_item(new Integer(rowIndex));
            row.getDomNodeOrDie().remove();
        }
    }

    /**
     * Inserts a new row at the specified index in the element's row collection. If the index
     * is -1 or there is no index specified, then the row is appended at the end of the
     * element's row collection.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/insertrow.asp">
     * MSDN Documentation</a>
     * @param cx the current JavaScript context.
     * @param s this scriptable object.
     * @param args the arguments for the function call.
     * @param f the function object that invoked this function.
     * @return the newly-created row.
     */
    public static Object jsxFunction_insertRow(
            final Context cx, final Scriptable s, final Object[] args,
            final Function f) {
        final RowContainer rowContainer = (RowContainer) s;
        final ElementArray rows = (ElementArray) rowContainer.jsxGet_rows();
        final Number rowIndex;
        if (args.length > 0) {
            rowIndex = (Number) args[0];
        }
        else {
            rowIndex = null;
        }
        final int r;
        if (rowIndex == null || rowIndex.intValue() == -1) {
            r = rows.jsGet_length() - 1;
        }
        else {
            r = rowIndex.intValue();
        }
        final boolean rowIndexValid = (r >= 0 && r < rows.jsGet_length());
        if (rowIndexValid) {
            final HtmlElement newRow = rowContainer.getDomNodeOrDie().getPage().createElement("tr");
            if (rows.jsGet_length() == 0) {
                rowContainer.getDomNodeOrDie().appendChild(newRow);
            }
            else {
                final SimpleScriptable row = (SimpleScriptable) rows.jsFunction_item(new Integer(r));
                // if at the end, then in the same "sub-container" as the last existing row
                if (r == rows.jsGet_length() - 1) {
                    row.getDomNodeOrDie().getParentNode().appendChild(newRow);
                }
                else {
                    row.getDomNodeOrDie().insertBefore(newRow);
                }
            }
            return rowContainer.getScriptableFor(newRow);
        }
        else {
            throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount");
        }
    }

    /**
     * Moves the row at the specified source index to the specified target index, returning
     * the row that was moved.
     * @param sourceIndex the index of the row to move.
     * @param targetIndex the index to move the row to.
     * @return the row that was moved.
     */
    public Object jsxFunction_moveRow(final int sourceIndex, final int targetIndex) {
        final ElementArray rows = (ElementArray) jsxGet_rows();
        final boolean sourceIndexValid = (sourceIndex >= 0 && sourceIndex < rows.jsGet_length());
        final boolean targetIndexValid = (targetIndex >= 0 && targetIndex < rows.jsGet_length());
        if (sourceIndexValid && targetIndexValid) {
            final SimpleScriptable sourceRow = (SimpleScriptable) rows.jsFunction_item(new Integer(sourceIndex));
            final SimpleScriptable targetRow = (SimpleScriptable) rows.jsFunction_item(new Integer(targetIndex));
            targetRow.getDomNodeOrDie().insertBefore(sourceRow.getDomNodeOrDie());
            return sourceRow;
        }
        else {
            return null;
        }
    }

}
