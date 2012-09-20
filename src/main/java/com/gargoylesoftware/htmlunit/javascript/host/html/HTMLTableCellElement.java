/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;

/**
 * The JavaScript object representing a TD or TH.
 *
 * @version $Revision$
 * @author <a href="https://sourceforge.net/users/marlee/">Mark van Leeuwen</a>
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Daniel Gredler
 */
@JsxClass(domClass = HtmlTableCell.class)
public class HTMLTableCellElement extends HTMLTableComponent {

    /**
     * {@inheritDoc}
     */
    @Override
    public void jsxFunction_setAttribute(final String name, String value) {
        if ("noWrap".equals(name) && value != null
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_92)) {
            value = "true";
        }
        super.jsxFunction_setAttribute(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int jsxGet_offsetHeight() {
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            return super.jsxGet_offsetHeight();
        }

        final ComputedCSSStyleDeclaration style = jsxGet_currentStyle();
        final boolean includeBorder = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_93);
        return style.getCalculatedHeight(includeBorder, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int jsxGet_offsetWidth() {
        float w = super.jsxGet_offsetWidth();
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            return (int) w;
        }

        final ComputedCSSStyleDeclaration style = jsxGet_currentStyle();
        if ("collapse".equals(style.jsxGet_borderCollapse())) {
            final HtmlTableRow row = getRow();
            if (row != null) {
                final HtmlElement thiz = getDomNodeOrDie();
                final List<HtmlTableCell> cells = row.getCells();
                final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_94);
                final boolean leftmost = (cells.indexOf(thiz) == 0);
                final boolean rightmost = (cells.indexOf(thiz) == cells.size() - 1);
                w -= ((ie && leftmost ? 0 : 0.5) * style.getBorderLeft());
                w -= ((ie && rightmost ? 0 : 0.5) * style.getBorderRight());
            }
        }

        return (int) w;
    }

    /**
     * Returns the index of this cell within the parent row.
     * @return the index of this cell within the parent row
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533549.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public Integer jsxGet_cellIndex() {
        final HtmlTableCell cell = (HtmlTableCell) getDomNodeOrDie();
        final HtmlTableRow row = cell.getEnclosingRow();
        if (row == null) { // a not attached document.createElement('TD')
            return Integer.valueOf(-1);
        }
        return Integer.valueOf(row.getCells().indexOf(cell));
    }

    /**
     * Returns the value of the <tt>abbr</tt> attribute.
     * @return the value of the <tt>abbr</tt> attribute
     */
    @JsxGetter
    public String jsxGet_abbr() {
        return getDomNodeOrDie().getAttribute("abbr");
    }

    /**
     * Sets the value of the <tt>abbr</tt> attribute.
     * @param abbr the value of the <tt>abbr</tt> attribute
     */
    @JsxSetter
    public void jsxSet_abbr(final String abbr) {
        getDomNodeOrDie().setAttribute("abbr", abbr);
    }

    /**
     * Returns the value of the <tt>axis</tt> attribute.
     * @return the value of the <tt>axis</tt> attribute
     */
    @JsxGetter
    public String jsxGet_axis() {
        return getDomNodeOrDie().getAttribute("axis");
    }

    /**
     * Sets the value of the <tt>axis</tt> attribute.
     * @param axis the value of the <tt>axis</tt> attribute
     */
    @JsxSetter
    public void jsxSet_axis(final String axis) {
        getDomNodeOrDie().setAttribute("axis", axis);
    }

    /**
     * Returns the value of the <tt>bgColor</tt> attribute.
     * @return the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String jsxGet_bgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the <tt>bgColor</tt> attribute.
     * @param bgColor the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void jsxSet_bgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Returns the value of the <tt>colSpan</tt> attribute.
     * @return the value of the <tt>colSpan</tt> attribute
     */
    @JsxGetter
    public int jsxGet_colSpan() {
        final String s = getDomNodeOrDie().getAttribute("colSpan");
        try {
            return Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Sets the value of the <tt>colSpan</tt> attribute.
     * @param colSpan the value of the <tt>colSpan</tt> attribute
     */
    @JsxSetter
    public void jsxSet_colSpan(final String colSpan) {
        String s;
        try {
            final int i = (int) Double.parseDouble(colSpan);
            if (i > 0) {
                s = Integer.toString(i);
            }
            else {
                throw new NumberFormatException(colSpan);
            }
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_95)) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
            s = "1";
        }
        getDomNodeOrDie().setAttribute("colSpan", s);
    }

    /**
     * Returns the value of the <tt>rowSpan</tt> attribute.
     * @return the value of the <tt>rowSpan</tt> attribute
     */
    @JsxGetter
    public int jsxGet_rowSpan() {
        final String s = getDomNodeOrDie().getAttribute("rowSpan");
        try {
            return Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Sets the value of the <tt>rowSpan</tt> attribute.
     * @param rowSpan the value of the <tt>rowSpan</tt> attribute
     */
    @JsxSetter
    public void jsxSet_rowSpan(final String rowSpan) {
        String s;
        try {
            final int i = (int) Double.parseDouble(rowSpan);
            if (i > 0) {
                s = Integer.toString(i);
            }
            else {
                throw new NumberFormatException(rowSpan);
            }
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_96)) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
            s = "1";
        }
        getDomNodeOrDie().setAttribute("rowSpan", s);
    }

    /**
     * Returns the value of the <tt>noWrap</tt> attribute.
     * @return the value of the <tt>noWrap</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534196.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public boolean jsxGet_noWrap() {
        return getDomNodeOrDie().hasAttribute("noWrap");
    }

    /**
     * Sets the value of the <tt>noWrap</tt> attribute.
     * @param noWrap the value of the <tt>noWrap</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534196.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void jsxSet_noWrap(final boolean noWrap) {
        if (noWrap) {
            final String value = (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_97) ? "true" : "");
            getDomNodeOrDie().setAttribute("noWrap", value);
        }
        else {
            getDomNodeOrDie().removeAttribute("noWrap");
        }
    }

    /**
     * Returns the row element which contains this cell's HTML element; may return <tt>null</tt>.
     * @return the row element which contains this cell's HTML element
     */
    private HtmlTableRow getRow() {
        DomNode node = getDomNodeOrDie();
        while (node != null && !(node instanceof HtmlTableRow)) {
            node = node.getParentNode();
        }
        return (HtmlTableRow) node;
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @JsxGetter
    public String jsxGet_width() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_98);
        final Boolean returnNegativeValues = ie ? Boolean.TRUE : null;
        return getWidthOrHeight("width", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    @JsxSetter
    public void jsxSet_width(final String width) {
        setWidthOrHeight("width", width, !getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_99));
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @JsxGetter
    public String jsxGet_height() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_100);
        final Boolean returnNegativeValues = ie ? Boolean.TRUE : null;
        return getWidthOrHeight("height", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    @JsxSetter
    public void jsxSet_height(final String width) {
        setWidthOrHeight("height", width, !getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_101));
    }

}
