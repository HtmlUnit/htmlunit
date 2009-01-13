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

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * The JavaScript object "HTMLSpanElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class HTMLSpanElement extends HTMLElement {

    private static final long serialVersionUID = -1837052392526933150L;

    /**
     * Creates an instance.
     */
    public HTMLSpanElement() {
        // Empty.
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);
        final HtmlElement element = (HtmlElement) domNode;
        if (getBrowserVersion().isIE()) {
            if (element instanceof HtmlAbbreviated) {
                ActiveXObject.addProperty(this, "cite", true, true);
            }
        }
    }

    /**
     * Simulates a click on a scrollbar component (IE only).
     * @param scrollAction the type of scroll action to simulate
     */
    public void jsxFunction_doScroll(final String scrollAction) {
        // Ignore because we aren't displaying anything!
    }

    /**
     * Returns the value of the "cite" property.
     * @return the value of the "cite" property
     */
    public String jsxGet_cite() {
        String align = getDomNodeOrDie().getAttribute("cite");
        if (align == NOT_FOUND) {
            align = "";
        }
        return align;
    }

    /**
     * Returns the value of the "cite" property.
     * @param cite the value
     */
    public void jsxSet_cite(final String cite) {
        getDomNodeOrDie().setAttribute("cite", cite);
    }
}
