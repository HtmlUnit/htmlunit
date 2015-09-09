/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLBASEFONT_END_TAG_FORBIDDEN;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.Locale;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBaseFont;
import com.gargoylesoftware.htmlunit.html.HtmlKeygen;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object {@code HTMLSpanElement}.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@JsxClasses({
        @JsxClass(domClass = HtmlSpan.class,
                browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) }),
        @JsxClass(domClass = HtmlSpan.class,
            isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlKeygen.class, browsers = @WebBrowser(FF)),
        @JsxClass(domClass = HtmlBaseFont.class, browsers = @WebBrowser(value = FF, maxVersion = 31))
    })
public class HTMLSpanElement extends HTMLElement {

    private boolean endTagForbidden_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public HTMLSpanElement() {
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);
        final BrowserVersion browser = getBrowserVersion();
        if (browser.hasFeature(HTMLBASEFONT_END_TAG_FORBIDDEN)) {
            switch (domNode.getLocalName().toLowerCase(Locale.ROOT)) {
                case "basefont":
                case "keygen":
                    endTagForbidden_ = true;
                    break;
                default:
            }
        }
    }

    /**
     * Returns the value of the {@code cite} property.
     * @return the value of the {@code cite} property
     */
    public String getCite() {
        final String cite = getDomNodeOrDie().getAttribute("cite");
        return cite;
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
     * {@inheritDoc}
     */
    @Override
    protected boolean isLowerCaseInOuterHtml() {
        return super.isLowerCaseInOuterHtml();
    }

    /**
     * Returns whether the end tag is forbidden or not.
     * @see <a href="http://www.w3.org/TR/html4/index/elements.html">HTML 4 specs</a>
     * @return whether the end tag is forbidden or not
     */
    @Override
    protected boolean isEndTagForbidden() {
        return endTagForbidden_;
    }

    /**
     * Returns the {@code dataFld} attribute.
     * @return the {@code dataFld} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataFld() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataFld} attribute.
     * @param dataFld {@code dataFld} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataFld(final String dataFld) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code dataFormatAs} attribute.
     * @return the {@code dataFormatAs} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataFormatAs() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataFormatAs} attribute.
     * @param dataFormatAs {@code dataFormatAs} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataFormatAs(final String dataFormatAs) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code dataSrc} attribute.
     * @return the {@code dataSrc} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataSrc() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataSrc} attribute.
     * @param dataSrc {@code dataSrc} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataSrc(final String dataSrc) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }
}
