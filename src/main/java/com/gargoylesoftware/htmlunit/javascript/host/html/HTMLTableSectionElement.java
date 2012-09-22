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

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.RowContainer;

/**
 * A JavaScript object representing "HTMLTableSectionElement", it is used by
 * {@link com.gargoylesoftware.htmlunit.html.HtmlTableBody},
 * {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeader}, and
 * {@link com.gargoylesoftware.htmlunit.html.HtmlTableFooter}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@JsxClass
public class HTMLTableSectionElement extends RowContainer {
    /*    Because it is associated with many HtmlClasses, the one-to-many
    configurations are in JavaScriptConfiguration.getHtmlJavaScriptMapping() */

    /** The valid "vAlign" values for this element, when emulating IE. */
    private static final String[] VALIGN_VALID_VALUES_IE = {"top", "bottom", "middle", "baseline"};

    /** The default value of the "vAlign" property. */
    private static final String VALIGN_DEFAULT_VALUE = "top";

    /**
     * Creates an instance.
     */
    public HTMLTableSectionElement() {
        // Empty.
    }

    /**
     * Returns the value of the "vAlign" property.
     * @return the value of the "vAlign" property
     */
    @JsxGetter
    public String get_vAlign() {
        return getVAlign(getValidVAlignValues(), VALIGN_DEFAULT_VALUE);
    }

    /**
     * Sets the value of the "vAlign" property.
     * @param vAlign the value of the "vAlign" property
     */
    @JsxSetter
    public void jsxSet_vAlign(final Object vAlign) {
        setVAlign(vAlign, getValidVAlignValues());
    }

    /**
     * Returns the valid "vAlign" values for this element, depending on the browser being emulated.
     * @return the valid "vAlign" values for this element, depending on the browser being emulated
     */
    private String[] getValidVAlignValues() {
        String[] valid;
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_VALIGN_SUPPORTS_IE_VALUES)) {
            valid = VALIGN_VALID_VALUES_IE;
        }
        else {
            valid = null;
        }
        return valid;
    }

    /**
     * Returns the value of the "ch" property.
     * @return the value of the "ch" property
     */
    @JsxGetter
    public String get_ch() {
        return getCh();
    }

    /**
     * Sets the value of the "ch" property.
     * @param ch the value of the "ch" property
     */
    @JsxSetter
    public void jsxSet_ch(final String ch) {
        setCh(ch);
    }

    /**
     * Returns the value of the "chOff" property.
     * @return the value of the "chOff" property
     */
    @JsxGetter
    public String get_chOff() {
        return getChOff();
    }

    /**
     * Sets the value of the "chOff" property.
     * @param chOff the value of the "chOff" property
     */
    @JsxSetter
    public void jsxSet_chOff(final String chOff) {
        setChOff(chOff);
    }

}
