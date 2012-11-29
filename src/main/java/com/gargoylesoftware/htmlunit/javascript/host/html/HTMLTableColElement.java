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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.TABLE_COLUMN_SPAN_THROWS_EXCEPTION_IF_LESS_THAN_ONE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.TABLE_COLUMN_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.HtmlTableColumn;
import com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object "HTMLTableColElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClasses = { HtmlTableColumn.class, HtmlTableColumnGroup.class  })
public class HTMLTableColElement extends HTMLTableComponent {

    /**
     * Returns the value of the "span" property.
     * @return the value of the "span" property
     */
    @JsxGetter
    public int getSpan() {
        final String span = getDomNodeOrDie().getAttribute("span");
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
     * Sets the value of the "span" property.
     * @param span the value of the "span" property
     */
    @JsxSetter
    public void setSpan(final Object span) {
        final double d = Context.toNumber(span);
        int i = (int) d;
        if (i < 1) {
            if (getBrowserVersion().hasFeature(TABLE_COLUMN_SPAN_THROWS_EXCEPTION_IF_LESS_THAN_ONE)) {
                final Exception e = new Exception("Cannot set the span property to invalid value: " + span);
                Context.throwAsScriptRuntimeEx(e);
            }
            else {
                i = 1;
            }
        }
        getDomNodeOrDie().setAttribute("span", Integer.toString(i));
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        final boolean ie = getBrowserVersion().hasFeature(TABLE_COLUMN_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES);
        final Boolean returnNegativeValues = ie ? Boolean.FALSE : null;
        return getWidthOrHeight("width", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    @JsxSetter
    public void setWidth(final Object width) {
        setWidthOrHeight("width", (width == null ? "" : Context.toString(width)), Boolean.FALSE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
    }
}
