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

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * A JavaScript object representing a Table.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLTableElement extends RowContainer {

    private static final long serialVersionUID = 2779888994049521608L;
    private HTMLCollection tBodies_; // has to be a member to have equality (==) working

    /**
     * Create an instance.
     */
    public HTMLTableElement() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    @Override
    public void jsConstructor() {
    }

    /**
     * Returns the table's caption element, or <tt>null</tt> if none exists. If more than one
     * caption is declared in the table, this method returns the first one.
     * @return the table's caption element
     */
    public Object jsxGet_caption() {
        final List<HtmlElement> captions = getDomNodeOrDie().getHtmlElementsByTagName("caption");
        if (captions.isEmpty()) {
            return null;
        }
        return getScriptableFor(captions.get(0));
    }

    /**
     * Returns the table's tfoot element, or <tt>null</tt> if none exists. If more than one
     * tfoot is declared in the table, this method returns the first one.
     * @return the table's tfoot element
     */
    public Object jsxGet_tFoot() {
        final List<HtmlElement> tfoots = getDomNodeOrDie().getHtmlElementsByTagName("tfoot");
        if (tfoots.isEmpty()) {
            return null;
        }
        return getScriptableFor(tfoots.get(0));
    }

    /**
     * Returns the table's thead element, or <tt>null</tt> if none exists. If more than one
     * thead is declared in the table, this method returns the first one.
     * @return the table's thead element
     */
    public Object jsxGet_tHead() {
        final List<HtmlElement> theads = getDomNodeOrDie().getHtmlElementsByTagName("thead");
        if (theads.isEmpty()) {
            return null;
        }
        return getScriptableFor(theads.get(0));
    }

    /**
     * Returns the tbody's in the table.
     * @return the tbody's in the table
     */
    public Object jsxGet_tBodies() {
        if (tBodies_ == null) {
            tBodies_ = new HTMLCollection(this);
            tBodies_.init(getDomNodeOrDie(), "./tbody");
        }
        return tBodies_;
    }

    /**
     * If this table does not have a caption, this method creates an empty table caption,
     * adds it to the table and then returns it. If one or more captions already exist,
     * this method returns the first existing caption.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536381.aspx">MSDN Documentation</a>
     * @return a newly added caption if no caption exists, or the first existing caption
     */
    public Object jsxFunction_createCaption() {
        return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("caption"));
    }

    /**
     * If this table does not have a tfoot element, this method creates an empty tfoot
     * element, adds it to the table and then returns it. If this table already has a
     * tfoot element, this method returns the existing tfoot element.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536402.aspx">MSDN Documentation</a>
     * @return a newly added caption if no caption exists, or the first existing caption
     */
    public Object jsxFunction_createTFoot() {
        return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("tfoot"));
    }

    /**
     * If this table does not have a thead element, this method creates an empty
     * thead element, adds it to the table and then returns it. If this table
     * already has a thead element, this method returns the existing thead element.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536403.aspx">MSDN Documentation</a>
     * @return a newly added caption if no caption exists, or the first existing caption
     */
    public Object jsxFunction_createTHead() {
        return getScriptableFor(getDomNodeOrDie().appendChildIfNoneExists("thead"));
    }

    /**
     * Deletes this table's caption. If the table has multiple captions, this method
     * deletes only the first caption. If this table does not have any captions, this
     * method does nothing.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536405.aspx">MSDN Documentation</a>
     */
    public void jsxFunction_deleteCaption() {
        getDomNodeOrDie().removeChild("caption", 0);
    }

    /**
     * Deletes this table's tfoot element. If the table has multiple tfoot elements, this
     * method deletes only the first tfoot element. If this table does not have any tfoot
     * elements, this method does nothing.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536409.aspx">MSDN Documentation</a>
     */
    public void jsxFunction_deleteTFoot() {
        getDomNodeOrDie().removeChild("tfoot", 0);
    }

    /**
     * Deletes this table's thead element. If the table has multiple thead elements, this
     * method deletes only the first thead element. If this table does not have any thead
     * elements, this method does nothing.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536410.aspx">MSDN Documentation</a>
     */
    public void jsxFunction_deleteTHead() {
        getDomNodeOrDie().removeChild("thead", 0);
    }

    /**
     * Refreshes the content of this table.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536687.aspx">
     * MSDN Documentation</a>
     */
    public void jsxFunction_refresh() {
        // Empty: this method only affects rendering, which we don't care about.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getXPathRows() {
        return "./node()/tr";
    }

    /**
     * Handle special case where table is empty.
     * {@inheritDoc}
     */
    @Override
    protected Object insertRow(final int index) {
        // check if a tbody should be created
        final List< ? > rowContainers =
            getDomNodeOrDie().getByXPath("//tbody | //thead | //tfoot");
        if (rowContainers.isEmpty() || index == 0) {
            final HtmlElement tBody = getDomNodeOrDie().appendChildIfNoneExists("tbody");
            return ((RowContainer) getScriptableFor(tBody)).insertRow(0);
        }
        return super.insertRow(index);
    }

    /**
     * Returns the <tt>width</tt> attribute.
     * @return the <tt>width</tt> attribute
     */
    public String jsxGet_width() {
        return getDomNodeOrDie().getAttribute("width");
    }

    /**
     * Sets the <tt>width</tt> attribute.
     * @param width the <tt>width</tt> attribute
     */
    public void jsxSet_width(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the <tt>cellSpacing</tt> attribute.
     * @return the <tt>cellSpacing</tt> attribute
     */
    public String jsxGet_cellSpacing() {
        return getDomNodeOrDie().getAttribute("cellspacing");
    }

    /**
     * Sets the <tt>cellSpacing</tt> attribute.
     * @param cellSpacing the <tt>cellSpacing</tt> attribute
     */
    public void jsxSet_cellSpacing(final String cellSpacing) {
        getDomNodeOrDie().setAttribute("cellspacing", cellSpacing);
    }

    /**
     * Returns the <tt>cellPadding</tt> attribute.
     * @return the <tt>cellPadding</tt> attribute
     */
    public String jsxGet_cellPadding() {
        return getDomNodeOrDie().getAttribute("cellpadding");
    }

    /**
     * Sets the <tt>cellPadding</tt> attribute.
     * @param cellPadding the <tt>cellPadding</tt> attribute
     */
    public void jsxSet_cellPadding(final String cellPadding) {
        getDomNodeOrDie().setAttribute("cellpadding", cellPadding);
    }
}
