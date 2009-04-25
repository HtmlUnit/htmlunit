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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * The JavaScript object "HTMLTableColElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLTableColElement extends HTMLElement {

    private static final long serialVersionUID = -886020322532732229L;

    /**
     * The value of the "ch" JavaScript attribute for browsers that do not really provide
     * access to the value of the "char" attribute in the DOM.
     */
    private String ch_ = "";

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
        else {
            String ch = getDomNodeOrDie().getAttribute("char");
            if (ch == DomElement.ATTRIBUTE_NOT_DEFINED) {
                ch = ".";
            }
            return ch;
        }
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

}
