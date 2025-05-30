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

import org.htmlunit.html.HtmlTableColumn;
import org.htmlunit.html.HtmlTableColumnGroup;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLTableColElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlTableColumn.class)
@JsxClass(domClass = HtmlTableColumnGroup.class)
public class HTMLTableColElement extends HTMLElement {

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
     * Returns the value of the {@code span} property.
     * @return the value of the {@code span} property
     */
    @JsxGetter
    public int getSpan() {
        final String span = getDomNodeOrDie().getAttributeDirect("span");
        int i;
        try {
            i = Integer.parseInt(span);
            if (i < 1) {
                i = 1;
            }
        }
        catch (final NumberFormatException e) {
            i = 1;
        }
        return i;
    }

    /**
     * Sets the value of the {@code span} property.
     * @param span the value of the {@code span} property
     */
    @JsxSetter
    public void setSpan(final Object span) {
        final double d = JavaScriptEngine.toNumber(span);
        int i = (int) d;
        if (i < 1) {
            i = 1;
        }
        getDomNodeOrDie().setAttribute("span", Integer.toString(i));
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
    public void setWidth_js(final Object width) {
        final String value = JavaScriptEngine.toString(width);
        setWidthOrHeight("width", value, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return getDomNodeOrDie() instanceof HtmlTableColumn;
    }

    /**
     * Overwritten to throw an exception.
     * @param value the new value for replacing this node
     */
    @Override
    public void setOuterHTML(final Object value) {
        throw JavaScriptEngine.reportRuntimeError("outerHTML is read-only for tag '"
                            + getDomNodeOrDie().getNodeName() + "'");
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
