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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * The JavaScript object representing a TD or TH.
 *
 * @version $Revision$
 * @author <a href="https://sourceforge.net/users/marlee/">Mark van Leeuwen</a>
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Daniel Gredler
 */
public class HTMLTableCellElement extends HTMLTableComponent {

    private static final long serialVersionUID = -4321684413510290017L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void jsxFunction_setAttribute(final String name, String value) {
        if ("noWrap".equals(name) && value != null && getBrowserVersion().isIE()) {
            value = "true";
        }
        super.jsxFunction_setAttribute(name, value);
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

    /**
     * Returns the value of the <tt>abbr</tt> attribute.
     * @return the value of the <tt>abbr</tt> attribute
     */
    public String jsxGet_abbr() {
        return getDomNodeOrDie().getAttribute("abbr");
    }

    /**
     * Sets the value of the <tt>abbr</tt> attribute.
     * @param abbr the value of the <tt>abbr</tt> attribute
     */
    public void jsxSet_abbr(final String abbr) {
        getDomNodeOrDie().setAttribute("abbr", abbr);
    }

    /**
     * Returns the value of the <tt>bgColor</tt> attribute.
     * @return the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    public String jsxGet_bgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the <tt>bgColor</tt> attribute.
     * @param bgColor the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    public void jsxSet_bgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Returns the value of the <tt>noWrap</tt> attribute.
     * @return the value of the <tt>noWrap</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534196.aspx">MSDN Documentation</a>
     */
    public boolean jsxGet_noWrap() {
        return getDomNodeOrDie().hasAttribute("noWrap");
    }

    /**
     * Sets the value of the <tt>noWrap</tt> attribute.
     * @param noWrap the value of the <tt>noWrap</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534196.aspx">MSDN Documentation</a>
     */
    public void jsxSet_noWrap(final boolean noWrap) {
        if (noWrap) {
            final String value = (getBrowserVersion().isIE() ? "true" : "");
            getDomNodeOrDie().setAttribute("noWrap", value);
        }
        else {
            getDomNodeOrDie().removeAttribute("noWrap");
        }
    }

}
