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

import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.css.StyleAttributes;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlTableCell;
import org.htmlunit.html.HtmlTableRow;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.event.MouseEvent;

/**
 * The JavaScript object representing a TD or TH.
 *
 * @author Mark van Leeuwen
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 * @author Lai Quang Duong
 */
@JsxClass(domClass = HtmlTableCell.class)
public class HTMLTableCellElement extends HTMLElement {

    /** The default value of the "vAlign" property. */
    private static final String VALIGN_DEFAULT_VALUE = "top";

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
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
        final ComputedCssStyleDeclaration style = getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
        return style.getCalculatedHeight(false, true);
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

        final ComputedCssStyleDeclaration style = getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
        if ("collapse".equals(style.getStyleAttribute(StyleAttributes.Definition.BORDER_COLLAPSE, true))) {
            final HtmlTableRow row = getRow();
            if (row != null) {
                w -= 0.5 * style.getBorderLeftValue();
                w -= 0.5 * style.getBorderRightValue();
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
    public int getCellIndex() {
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
        return ((HtmlTableCell) getDomNodeOrDie()).getColumnSpan();
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
            getDomNodeOrDie().setAttribute("colSpan", "1");
        }
    }

    /**
     * Returns the value of the {@code rowSpan} attribute.
     * @return the value of the {@code rowSpan} attribute
     */
    @JsxGetter
    public int getRowSpan() {
        return ((HtmlTableCell) getDomNodeOrDie()).getRowSpan();
    }

    /**
     * Sets the value of the {@code rowSpan} attribute.
     * @param rowSpan the value of the {@code rowSpan} attribute
     */
    @JsxSetter
    public void setRowSpan(final String rowSpan) {
        try {
            final int i = (int) Double.parseDouble(rowSpan);
            if (i < 0) {
                getDomNodeOrDie().setAttribute("rowSpan", "1");
                return;
            }
            if (i == 0) {
                throw new NumberFormatException(rowSpan);
            }
            getDomNodeOrDie().setAttribute("rowSpan", Integer.toString(i));
        }
        catch (final NumberFormatException e) {
            getDomNodeOrDie().setAttribute("rowSpan", "0");
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
        return getWidthOrHeight("width", null);
    }

    /**
     * Sets the value of the {@code width} property.
     * @param width the value of the {@code width} property
     */
    @JsxSetter(propertyName = "width")
    public void setWidth_js(final String width) {
        setWidthOrHeight("width", width, true);
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter(propertyName = "height")
    public String getHeight_js() {
        return getWidthOrHeight("height", null);
    }

    /**
     * Sets the value of the {@code height} property.
     * @param height the value of the {@code height} property
     */
    @JsxSetter(propertyName = "height")
    public void setHeight_js(final String height) {
        setWidthOrHeight("height", height, true);
    }

    /**
     * Overwritten to throw an exception.
     * @param value the new value for replacing this node
     */
    @Override
    public void setOuterHTML(final Object value) {
        throw JavaScriptEngine.reportRuntimeError("outerHTML is read-only for tag '"
                        + getDomNodeOrDie().getTagName() + "'");
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

    /**
     * Returns the value of the {@code vAlign} property.
     * @return the value of the {@code vAlign} property
     */
    @JsxGetter
    public String getVAlign() {
        return getVAlign(getValidVAlignValues(), VALIGN_DEFAULT_VALUE);
    }

    /**
     * Sets the value of the {@code vAlign} property.
     * @param vAlign the value of the {@code vAlign} property
     */
    @JsxSetter
    public void setVAlign(final Object vAlign) {
        setVAlign(vAlign, getValidVAlignValues());
    }

    /**
     * Returns the valid "vAlign" values for this element, depending on the browser being emulated.
     * @return the valid "vAlign" values for this element, depending on the browser being emulated
     */
    private String[] getValidVAlignValues() {
        return null;
    }

    /**
     * Returns the value of the {@code ch} property.
     * @return the value of the {@code ch} property
     */
    @Override
    @JsxGetter
    public String getCh() {
        return super.getCh();
    }

    /**
     * Sets the value of the {@code ch} property.
     * @param ch the value of the {@code ch} property
     */
    @Override
    @JsxSetter
    public void setCh(final String ch) {
        super.setCh(ch);
    }

    /**
     * Returns the value of the {@code chOff} property.
     * @return the value of the {@code chOff} property
     */
    @Override
    @JsxGetter
    public String getChOff() {
        return super.getChOff();
    }

    /**
     * Sets the value of the {@code chOff} property.
     * @param chOff the value of the {@code chOff} property
     */
    @Override
    @JsxSetter
    public void setChOff(final String chOff) {
        super.setChOff(chOff);
    }
}
