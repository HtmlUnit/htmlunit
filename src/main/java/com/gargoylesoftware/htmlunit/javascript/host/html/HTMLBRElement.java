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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLEAR_RESTRICT;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.lang3.ArrayUtils;

import com.gargoylesoftware.htmlunit.html.HtmlBreak;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object "HTMLBRElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlBreak.class)
public class HTMLBRElement extends HTMLElement {

    /** Valid values for the {@link #getClear() clear} property. */
    private static final String[] VALID_CLEAR_VALUES = new String[] {"left", "right", "all", "none"};

    /**
     * Returns the value of the <tt>clear</tt> property.
     * @return the value of the <tt>clear</tt> property
     */
    @JsxGetter
    public String getClear() {
        final String clear = getDomNodeOrDie().getAttribute("clear");
        if (getBrowserVersion().hasFeature(JS_CLEAR_RESTRICT) && !ArrayUtils.contains(VALID_CLEAR_VALUES, clear)) {
            return "";
        }
        return clear;
    }

    /**
     * Sets the value of the <tt>clear</tt> property.
     * @param clear the value of the <tt>clear</tt> property
     */
    @JsxSetter
    public void setClear(final String clear) {
        if (getBrowserVersion().hasFeature(JS_CLEAR_RESTRICT) && !ArrayUtils.contains(VALID_CLEAR_VALUES, clear)) {
            throw Context.reportRuntimeError("Invalid clear property value: '" + clear + "'.");
        }
        getDomNodeOrDie().setAttribute("clear", clear);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
    }
}
