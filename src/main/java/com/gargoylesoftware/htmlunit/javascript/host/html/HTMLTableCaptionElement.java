/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_91;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLELEMENT_ALIGN_INVALID;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.HtmlCaption;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object "HTMLTableCaptionElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlCaption.class)
public class HTMLTableCaptionElement extends HTMLElement {

    /** The valid <tt>vAlign</tt> values for this element, when emulating IE. */
    private static final String[] VALIGN_VALID_VALUES_IE = {"top", "bottom"};

    /** The default value of the "vAlign" property. */
    private static final String VALIGN_DEFAULT_VALUE = "";

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    @JsxGetter
    public String getAlign() {
        final boolean invalidValues = getBrowserVersion().hasFeature(HTMLELEMENT_ALIGN_INVALID);
        return getAlign(invalidValues);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    @JsxSetter
    public void setAlign(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the "vAlign" property.
     * @return the value of the "vAlign" property
     */
    @JsxGetter(@WebBrowser(IE))
    public String getVAlign() {
        return getVAlign(getValidVAlignValues(), VALIGN_DEFAULT_VALUE);
    }

    /**
     * Sets the value of the "vAlign" property.
     * @param vAlign the value of the "vAlign" property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setVAlign(final Object vAlign) {
        setVAlign(vAlign, getValidVAlignValues());
    }

    /**
     * Returns the valid "vAlign" values for this element, depending on the browser being emulated.
     * @return the valid "vAlign" values for this element, depending on the browser being emulated
     */
    private String[] getValidVAlignValues() {
        String[] valid;
        if (getBrowserVersion().hasFeature(GENERATED_91)) {
            valid = VALIGN_VALID_VALUES_IE;
        }
        else {
            valid = null;
        }
        return valid;
    }

    /**
     * Overwritten to throw an exception in IE8/9.
     * @param value the new value for replacing this node
     */
    @JsxSetter
    @Override
    public void setOuterHTML(final String value) {
        throw Context.reportRuntimeError("outerHTML is read-only for tag 'caption'");
    }
}
