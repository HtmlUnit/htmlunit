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

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * The JavaScript object "HTMLTableColElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLTableColElement extends HTMLElement {

    private static final long serialVersionUID = -886020322532732229L;

    /** The valid "vAlign" values for this element, when emulating IE. */
    private static final String[] VALIGN_VALID_VALUES_IE = {"top", "bottom", "middle", "baseline"};

    /** The default value of the "vAlign" property. */
    private static final String VALIGN_DEFAULT_VALUE = "top";

    /**
     * The value of the "ch" JavaScript attribute for browsers that do not really provide
     * access to the value of the "char" DOM attribute.
     */
    private String ch_ = "";

    /**
     * The value of the "chOff" JavaScript attribute for browsers that do not really provide
     * access to the value of the "charOff" DOM attribute.
     */
    private String chOff_ = "";

    /**
     * Creates an instance.
     */
    public HTMLTableColElement() {
        // Empty.
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    public String jsxGet_align() {
        final boolean returnInvalidValues = getBrowserVersion().isFirefox();
        return getAlign(returnInvalidValues);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    public void jsxSet_align(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the "ch" property.
     * @return the value of the "ch" property
     */
    public String jsxGet_ch() {
        final boolean ie = getBrowserVersion().isIE();
        if (ie) {
            return ch_;
        }
        String ch = getDomNodeOrDie().getAttribute("char");
        if (ch == DomElement.ATTRIBUTE_NOT_DEFINED) {
            ch = ".";
        }
        return ch;
    }

    /**
     * Sets the value of the "ch" property.
     * @param ch the value of the "ch" property
     */
    public void jsxSet_ch(final String ch) {
        final boolean ie = getBrowserVersion().isIE();
        if (ie) {
            ch_ = ch;
        }
        else {
            getDomNodeOrDie().setAttribute("char", ch);
        }
    }

    /**
     * Returns the value of the "chOff" property.
     * @return the value of the "chOff" property
     */
    public String jsxGet_chOff() {
        final boolean ie = getBrowserVersion().isIE();
        if (ie) {
            return chOff_;
        }
        return getDomNodeOrDie().getAttribute("charOff");
    }

    /**
     * Sets the value of the "chOff" property.
     * @param chOff the value of the "chOff" property
     */
    public void jsxSet_chOff(String chOff) {
        final boolean ie = getBrowserVersion().isIE();
        if (ie) {
            chOff_ = chOff;
        }
        else {
            try {
                Float f = new Float(chOff);
                if (f < 0) {
                    f = 0f;
                }
                chOff = String.valueOf(f.intValue());
            }
            catch (final NumberFormatException e) {
                // Ignore.
            }
            getDomNodeOrDie().setAttribute("charOff", chOff);
        }
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
            if (getBrowserVersion().isIE()) {
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
     * Returns the value of the "vAlign" property.
     * @return the value of the "vAlign" property
     */
    public String jsxGet_vAlign() {
        return getVAlign(getValidVAlignValues(), VALIGN_DEFAULT_VALUE);
    }

    /**
     * Sets the value of the "vAlign" property.
     * @param vAlign the value of the "vAlign" property
     */
    public void jsxSet_vAlign(final Object vAlign) {
        setVAlign(vAlign, getValidVAlignValues());
    }

    /**
     * Returns the valid "vAlign" values for this element, depending on the browser being emulated.
     * @return the valid "vAlign" values for this element, depending on the browser being emulated
     */
    private String[] getValidVAlignValues() {
        String[] valid;
        if (getBrowserVersion().isIE()) {
            valid = VALIGN_VALID_VALUES_IE;
        }
        else {
            valid = null;
        }
        return valid;
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    public String jsxGet_width() {
        final boolean ie = getBrowserVersion().isIE();
        final Boolean returnNegativeValues = ie ? false : null;
        return getWidthOrHeight("width", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    public void jsxSet_width(final String width) {
        setWidthOrHeight("width", width, false);
    }

}
