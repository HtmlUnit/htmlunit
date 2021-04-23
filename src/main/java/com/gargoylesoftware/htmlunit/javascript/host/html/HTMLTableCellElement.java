/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TABLE_CELL_HEIGHT_DOES_NOT_RETURN_NEGATIVE_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TABLE_CELL_OFFSET_INCLUDES_BORDER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TABLE_CELL_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TABLE_SPAN_SET_ZERO_IF_INVALID;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_TABLE_SPAN_THROWS_EXCEPTION_IF_INVALID;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object representing a TD or TH.
 *
 * @author <a href="https://sourceforge.net/users/marlee/">Mark van Leeuwen</a>
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlTableCell.class)
public class HTMLTableCellElement extends HTMLTableComponent {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLTableCellElement() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOffsetHeight() {
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            return super.getOffsetHeight();
        }

        if (isDisplayNone()) {
            return 0;
        }
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        final boolean includeBorder = getBrowserVersion().hasFeature(JS_TABLE_CELL_OFFSET_INCLUDES_BORDER);
        return style.getCalculatedHeight(includeBorder, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOffsetWidth() {
        float w = super.getOffsetWidth();
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            return (int) w;
        }

        if (isDisplayNone()) {
            return 0;
        }

        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        if ("collapse".equals(style.getStyleAttribute(StyleAttributes.Definition.BORDER_COLLAPSE))) {
            final HtmlTableRow row = getRow();
            if (row != null) {
                final HtmlElement thiz = getDomNodeOrDie();
                final List<HtmlTableCell> cells = row.getCells();
                final boolean ie = getBrowserVersion().hasFeature(JS_TABLE_CELL_OFFSET_INCLUDES_BORDER);
                final boolean leftmost = cells.indexOf(thiz) == 0;
                final boolean rightmost = cells.indexOf(thiz) == cells.size() - 1;
                w -= (ie && leftmost ? 0 : 0.5) * style.getBorderLeftValue();
                w -= (ie && rightmost ? 0 : 0.5) * style.getBorderRightValue();
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
    public Integer getCellIndex() {
        final HtmlTableCell cell = (HtmlTableCell) getDomNodeOrDie();
        final HtmlTableRow row = cell.getEnclosingRow();
        if (row == null) { // a not attached document.createElement('TD')
            return Integer.valueOf(-1);
        }
        return Integer.valueOf(row.getCells().indexOf(cell));
    }

    /**
     * Returns the value of the {@code abbr} attribute.
     * @return the value of the {@code abbr} attribute
     */
    @JsxGetter
    public String getAbbr() {
        return getDomNodeOrDie().getAttributeDirect("abbr");
    }

    /**
     * Sets the value of the {@code abbr} attribute.
     * @param abbr the value of the {@code abbr} attribute
     */
    @JsxSetter
    public void setAbbr(final String abbr) {
        getDomNodeOrDie().setAttribute("abbr", abbr);
    }

    /**
     * Returns the value of the {@code axis} attribute.
     * @return the value of the {@code axis} attribute
     */
    @JsxGetter
    public String getAxis() {
        return getDomNodeOrDie().getAttributeDirect("axis");
    }

    /**
     * Sets the value of the {@code axis} attribute.
     * @param axis the value of the {@code axis} attribute
     */
    @JsxSetter
    public void setAxis(final String axis) {
        getDomNodeOrDie().setAttribute("axis", axis);
    }

    /**
     * Returns the value of the {@code bgColor} attribute.
     * @return the value of the {@code bgColor} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getBgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the {@code bgColor} attribute.
     * @param bgColor the value of the {@code bgColor} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setBgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Returns the value of the {@code colSpan} attribute.
     * @return the value of the {@code colSpan} attribute
     */
    @JsxGetter
    public int getColSpan() {
        final String s = getDomNodeOrDie().getAttribute("colSpan");
        try {
            return Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Sets the value of the {@code colSpan} attribute.
     * @param colSpan the value of the {@code colSpan} attribute
     */
    @JsxSetter
    public void setColSpan(final String colSpan) {
        try {
            final int i = (int) Double.parseDouble(colSpan);
            if (i <= 0) {
                throw new NumberFormatException(colSpan);
            }
            getDomNodeOrDie().setAttribute("colSpan", Integer.toString(i));
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(JS_TABLE_SPAN_THROWS_EXCEPTION_IF_INVALID)) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
            getDomNodeOrDie().setAttribute("colSpan", "1");
        }
    }

    /**
     * Returns the value of the {@code rowSpan} attribute.
     * @return the value of the {@code rowSpan} attribute
     */
    @JsxGetter
    public int getRowSpan() {
        final String s = getDomNodeOrDie().getAttribute("rowSpan");
        try {
            return Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            return 1;
        }
    }

    /**
     * Sets the value of the {@code rowSpan} attribute.
     * @param rowSpan the value of the {@code rowSpan} attribute
     */
    @JsxSetter
    public void setRowSpan(final String rowSpan) {
        try {
            final int i = (int) Double.parseDouble(rowSpan);
            if (i < 0 && getBrowserVersion().hasFeature(JS_TABLE_SPAN_SET_ZERO_IF_INVALID)) {
                getDomNodeOrDie().setAttribute("rowSpan", "1");
                return;
            }
            if (i <= 0) {
                throw new NumberFormatException(rowSpan);
            }
            getDomNodeOrDie().setAttribute("rowSpan", Integer.toString(i));
        }
        catch (final NumberFormatException e) {
            if (getBrowserVersion().hasFeature(JS_TABLE_SPAN_THROWS_EXCEPTION_IF_INVALID)) {
                throw Context.throwAsScriptRuntimeEx(e);
            }
            if (getBrowserVersion().hasFeature(JS_TABLE_SPAN_SET_ZERO_IF_INVALID)) {
                getDomNodeOrDie().setAttribute("rowSpan", "0");
            }
            else {
                getDomNodeOrDie().setAttribute("rowSpan", "1");
            }
        }
    }

    /**
     * Returns the value of the {@code noWrap} attribute.
     * @return the value of the {@code noWrap} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534196.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public boolean isNoWrap() {
        return getDomNodeOrDie().hasAttribute("noWrap");
    }

    /**
     * Sets the value of the {@code noWrap} attribute.
     * @param noWrap the value of the {@code noWrap} attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534196.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setNoWrap(final boolean noWrap) {
        if (noWrap) {
            getDomNodeOrDie().setAttribute("noWrap", "");
        }
        else {
            getDomNodeOrDie().removeAttribute("noWrap");
        }
    }

    /**
     * Returns the row element which contains this cell's HTML element; may return {@code null}.
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
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        final boolean ie = getBrowserVersion().hasFeature(JS_TABLE_CELL_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES);
        final Boolean returnNegativeValues = ie ? Boolean.TRUE : null;
        return getWidthOrHeight("width", returnNegativeValues);
    }

    /**
     * Sets the value of the {@code width} property.
     * @param width the value of the {@code width} property
     */
    @JsxSetter
    public void setWidth(final String width) {
        setWidthOrHeight("width", width,
                !getBrowserVersion().hasFeature(JS_TABLE_CELL_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES));
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter(propertyName = "height")
    public String getHeight_js() {
        final boolean ie = getBrowserVersion().hasFeature(JS_TABLE_CELL_HEIGHT_DOES_NOT_RETURN_NEGATIVE_VALUES);
        final Boolean returnNegativeValues = ie ? Boolean.TRUE : null;
        return getWidthOrHeight("height", returnNegativeValues);
    }

    /**
     * Sets the value of the {@code height} property.
     * @param height the value of the {@code height} property
     */
    @JsxSetter
    public void setHeight(final String height) {
        setWidthOrHeight("height", height,
                !getBrowserVersion().hasFeature(JS_TABLE_CELL_HEIGHT_DOES_NOT_RETURN_NEGATIVE_VALUES));
    }

    /**
     * Overwritten to throw an exception.
     * @param value the new value for replacing this node
     */
    @Override
    public void setOuterHTML(final Object value) {
        throw Context.reportRuntimeError("outerHTML is read-only for tag '"
                        + getDomNodeOrDie().getTagName() + "'");
    }

    /**
     * Gets the {@code borderColor} attribute.
     * @return the attribute
     */
    @JsxGetter(IE)
    public String getBorderColor() {
        return getDomNodeOrDie().getAttribute("borderColor");
    }

    /**
     * Sets the {@code borderColor} attribute.
     * @param borderColor the new attribute
     */
    @JsxSetter(IE)
    public void setBorderColor(final String borderColor) {
        setColorAttribute("borderColor", borderColor);
    }

    /**
     * Gets the {@code borderColor} attribute.
     * @return the attribute
     */
    @JsxGetter(IE)
    public String getBorderColorDark() {
        return "";
    }

    /**
     * Sets the {@code borderColor} attribute.
     * @param borderColor the new attribute
     */
    @JsxSetter(IE)
    public void setBorderColorDark(final String borderColor) {
        // ignore
    }

    /**
     * Gets the {@code borderColor} attribute.
     * @return the attribute
     */
    @JsxGetter(IE)
    public String getBorderColorLight() {
        return "";
    }

    /**
     * Sets the {@code borderColor} attribute.
     * @param borderColor the new attribute
     */
    @JsxSetter(IE)
    public void setBorderColorLight(final String borderColor) {
        // ignore
    }

    /**
     * Returns the {@code headers} attribute.
     * @return the {@code headers} attribute
     */
    @JsxGetter
    public String getHeaders() {
        return getDomNodeOrDie().getAttributeDirect("headers");
    }

    /**
     * Sets the {@code headers} attribute.
     * @param headers the new attribute
     */
    @JsxSetter
    public void setHeaders(final String headers) {
        getDomNodeOrDie().setAttribute("headers", headers);
    }

    /**
     * Returns the {@code scope} attribute.
     * @return the {@code scope} attribute
     */
    @JsxGetter
    public String getScope() {
        return getDomNodeOrDie().getAttributeDirect("scope");
    }

    /**
     * Sets the {@code scope} attribute.
     * @param scope the new attribute
     */
    @JsxSetter
    public void setScope(final String scope) {
        getDomNodeOrDie().setAttribute("scope", scope);
    }
}
