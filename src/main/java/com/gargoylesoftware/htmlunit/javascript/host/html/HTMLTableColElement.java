/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;

/**
 * The JavaScript object "HTMLTableColElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLTableColElement extends HTMLTableComponent {

    private static final long serialVersionUID = -886020322532732229L;

    /**
     * Creates an instance.
     */
    public HTMLTableColElement() {
        // Empty.
    }

    /**
     * Returns the value of the "span" property.
     * @return the value of the "span" property
     */
    public int jsxGet_span() {
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
    public void jsxSet_span(final Object span) {
        final Double d = Context.toNumber(span);
        Integer i = d.intValue();
        if (i < 1) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_102)) {
                final Exception e = new Exception("Cannot set the span property to invalid value: " + span);
                Context.throwAsScriptRuntimeEx(e);
            }
            else {
                i = 1;
            }
        }
        getDomNodeOrDie().setAttribute("span", i.toString());
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    public String jsxGet_width() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_103);
        final Boolean returnNegativeValues = ie ? false : null;
        return getWidthOrHeight("width", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    public void jsxSet_width(final Object width) {
        setWidthOrHeight("width", (width == null ? "" : Context.toString(width)), false);
    }

}
