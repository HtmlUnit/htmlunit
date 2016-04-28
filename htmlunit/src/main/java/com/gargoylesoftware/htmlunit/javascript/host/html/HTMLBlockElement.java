/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLKEYGEN_END_TAG_FORBIDDEN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlBlockQuote;
import com.gargoylesoftware.htmlunit.html.HtmlCenter;
import com.gargoylesoftware.htmlunit.html.HtmlExample;
import com.gargoylesoftware.htmlunit.html.HtmlKeygen;
import com.gargoylesoftware.htmlunit.html.HtmlListing;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;

/**
 * The JavaScript object {@code HTMLBlockElement}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@JsxClasses({
        @JsxClass(domClass = HtmlAddress.class, browsers = @WebBrowser(IE)),
        @JsxClass(domClass = HtmlBlockQuote.class, browsers = @WebBrowser(IE)),
        @JsxClass(domClass = HtmlCenter.class, browsers = @WebBrowser(IE)),
        @JsxClass(domClass = HtmlExample.class, browsers = @WebBrowser(IE)),
        @JsxClass(domClass = HtmlKeygen.class, browsers = @WebBrowser(IE)),
        @JsxClass(domClass = HtmlListing.class, browsers = @WebBrowser(IE)),
        @JsxClass(domClass = HtmlPlainText.class, browsers = @WebBrowser(IE))
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
     * Returns the value of the {@code cite} property.
     * @return the value of the {@code cite} property
     */
    public String getCite() {
        return getDomNodeOrDie().getAttribute("cite");
    }

    /**
     * Returns the value of the {@code cite} property.
     * @param cite the value
     */
    public void setCite(final String cite) {
        getDomNodeOrDie().setAttribute("cite", cite);
    }

    /**
     * Returns the value of the {@code dateTime} property.
     * @return the value of the {@code dateTime} property
     */
    public String getDateTime() {
        final String dateTime = getDomNodeOrDie().getAttribute("datetime");
        return dateTime;
    }

    /**
     * Returns the value of the {@code dateTime} property.
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
    @Override
    protected boolean isEndTagForbidden() {
        if ("KEYGEN".equals(getNodeName()) && getBrowserVersion().hasFeature(HTMLKEYGEN_END_TAG_FORBIDDEN)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the value of the {@code clear} property.
     * @return the value of the {@code clear} property
     */
    @JsxGetter
    public String getClear() {
        return getDomNodeOrDie().getAttribute("clear");
    }

    /**
     * Returns the value of the {@code clear} property.
     * @param clear the value
     */
    @JsxSetter
    public void setClear(final String clear) {
        getDomNodeOrDie().setAttribute("clear", clear);
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        return getWidthOrHeight("width", Boolean.TRUE);
    }

    /**
     * Sets the value of the {@code width} property.
     * @param width the value of the {@code width} property
     */
    @JsxSetter
    public void setWidth(final String width) {
        setWidthOrHeight("width", width, true);
    }

}
