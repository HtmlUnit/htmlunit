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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BLOCK_COMMON_CLASS_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlBlockQuote;
import com.gargoylesoftware.htmlunit.html.HtmlCenter;
import com.gargoylesoftware.htmlunit.html.HtmlExample;
import com.gargoylesoftware.htmlunit.html.HtmlListing;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
import com.gargoylesoftware.htmlunit.html.HtmlPreformattedText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;

/**
 * The JavaScript object "HTMLBlockElement".
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@JsxClasses({
    @JsxClass(domClass = HtmlAddress.class),
    @JsxClass(domClass = HtmlBlockQuote.class, browsers = @WebBrowser(IE)),
    @JsxClass(domClass = HtmlCenter.class),
    @JsxClass(domClass = HtmlExample.class),
    @JsxClass(domClass = HtmlListing.class),
    @JsxClass(domClass = HtmlPlainText.class),
    @JsxClass(domClass = HtmlPreformattedText.class, browsers = @WebBrowser(value = IE, maxVersion = 8))
})
public class HTMLBlockElement extends HTMLElement {

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        if (getBrowserVersion().hasFeature(JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
            ActiveXObject.addProperty(this, "cite", true, true);
        }
    }

    /**
     * Returns the value of the "cite" property.
     * @return the value of the "cite" property
     */
    public String getCite() {
        final String cite = getDomNodeOrDie().getAttribute("cite");
        return cite;
    }

    /**
     * Returns the value of the "cite" property.
     * @param cite the value
     */
    public void setCite(final String cite) {
        getDomNodeOrDie().setAttribute("cite", cite);
    }

    /**
     * Returns the value of the "dateTime" property.
     * @return the value of the "dateTime" property
     */
    public String getDateTime() {
        final String dateTime = getDomNodeOrDie().getAttribute("datetime");
        return dateTime;
    }

    /**
     * Returns the value of the "dateTime" property.
     * @param dateTime the value
     */
    public void setDateTime(final String dateTime) {
        getDomNodeOrDie().setAttribute("datetime", dateTime);
    }

    /**
     * Returns whether the end tag is forbidden or not.
     * @see <a href="http://www.w3.org/TR/html4/index/elements.html">HTML 4 specs</a>
     * @return whether the end tag is forbidden or not
     */
    protected boolean isEndTagForbidden() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        if (getWindow().getWebWindow() != null && getBrowserVersion().hasFeature(JS_BLOCK_COMMON_CLASS_NAME)) {
            return "HTMLElement";
        }
        return super.getClassName();
    }
}
