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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object "HtmlInlineQuotation".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlInlineQuotation.class,
    browsers = { @WebBrowser(value = IE, minVersion = 11), @WebBrowser(FF), @WebBrowser(CHROME) })
public class HTMLInlineQuotationElement extends HTMLElement {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return "HTMLQuoteElement";
    }

    /**
     * Returns the value of the "cite" property.
     * @return the value of the "cite" property
     */
    @JsxGetter
    public String getCite() {
        final String cite = getDomNodeOrDie().getAttribute("cite");
        return cite;
    }

    /**
     * Returns the value of the "cite" property.
     * @param cite the value
     */
    @JsxSetter
    public void setCite(final String cite) {
        getDomNodeOrDie().setAttribute("cite", cite);
    }

    /**
     * Returns the value of the "dateTime" property.
     * @return the value of the "dateTime" property
     */
    @JsxGetter(@WebBrowser(IE))
    public String getDateTime() {
        final String cite = getDomNodeOrDie().getAttribute("datetime");
        return cite;
    }

    /**
     * Returns the value of the "dateTime" property.
     * @param dateTime the value
     */
    @JsxSetter(@WebBrowser(IE))
    public void setDateTime(final String dateTime) {
        getDomNodeOrDie().setAttribute("datetime", dateTime);
    }
}
