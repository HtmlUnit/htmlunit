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

import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * The JavaScript object representing a TD or TH.
 *
 * @version $Revision$
 * @author <a href="https://sourceforge.net/users/marlee/">Mark van Leeuwen</a>
 * @author Ahmed Ashour
 */
public class HTMLTableCellElement extends HTMLElement {

    private static final long serialVersionUID = -4321684413510290017L;

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Returns the index of this cell within the parent row.
     * @return the index of this cell within the parent row
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533549.aspx">MSDN Documentation</a>
     */
    public Integer jsxGet_cellIndex() {
        final HtmlTableCell cell = (HtmlTableCell) getDomNodeOrDie();
        final HtmlTableRow row = cell.getEnclosingRow();
        return new Integer(row.getCells().indexOf(cell));
    }
}
