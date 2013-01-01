/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_104;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLELEMENT_ALIGN_INVALID;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * Contains attributes common to various table components.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@JsxClass(isJSObject = false)
public class HTMLTableComponent extends HTMLElement {

    /** The valid "vAlign" values for this element, when emulating IE. */
    private static final String[] VALIGN_VALID_VALUES_IE = {"top", "bottom", "middle", "baseline"};

    /** The default value of the "vAlign" property. */
    private static final String VALIGN_DEFAULT_VALUE = "top";

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
    @JsxGetter
    public String getVAlign() {
        return getVAlign(getValidVAlignValues(), VALIGN_DEFAULT_VALUE);
    }

    /**
     * Sets the value of the "vAlign" property.
     * @param vAlign the value of the "vAlign" property
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
        String[] valid;
        if (getBrowserVersion().hasFeature(GENERATED_104)) {
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
    @Override
    @JsxGetter
    public String getCh() {
        return super.getCh();
    }

    /**
     * Sets the value of the "ch" property.
     * @param ch the value of the "ch" property
     */
    @Override
    @JsxSetter
    public void setCh(final String ch) {
        super.setCh(ch);
    }

    /**
     * Returns the value of the "chOff" property.
     * @return the value of the "chOff" property
     */
    @Override
    @JsxGetter
    public String getChOff() {
        return super.getChOff();
    }

    /**
     * Sets the value of the "chOff" property.
     * @param chOff the value of the "chOff" property
     */
    @Override
    @JsxSetter
    public void setChOff(final String chOff) {
        super.setChOff(chOff);
    }

}
