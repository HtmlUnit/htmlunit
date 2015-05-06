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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLABBREVIATED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_PHRASE_COMMON_CLASS_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAcronym;
import com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride;
import com.gargoylesoftware.htmlunit.html.HtmlBig;
import com.gargoylesoftware.htmlunit.html.HtmlBlink;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlCitation;
import com.gargoylesoftware.htmlunit.html.HtmlCode;
import com.gargoylesoftware.htmlunit.html.HtmlDefinition;
import com.gargoylesoftware.htmlunit.html.HtmlDeletedText;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation;
import com.gargoylesoftware.htmlunit.html.HtmlInsertedText;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlNoBreak;
import com.gargoylesoftware.htmlunit.html.HtmlRp;
import com.gargoylesoftware.htmlunit.html.HtmlRt;
import com.gargoylesoftware.htmlunit.html.HtmlRuby;
import com.gargoylesoftware.htmlunit.html.HtmlS;
import com.gargoylesoftware.htmlunit.html.HtmlSample;
import com.gargoylesoftware.htmlunit.html.HtmlSmall;
import com.gargoylesoftware.htmlunit.html.HtmlStrike;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubscript;
import com.gargoylesoftware.htmlunit.html.HtmlSuperscript;
import com.gargoylesoftware.htmlunit.html.HtmlTeletype;
import com.gargoylesoftware.htmlunit.html.HtmlUnderlined;
import com.gargoylesoftware.htmlunit.html.HtmlVariable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;

/**
 * The JavaScript object "HTMLPhraseElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@JsxClasses({
        @JsxClass(domClass = HtmlAbbreviated.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlAbbreviated.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlAcronym.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlAcronym.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlBidirectionalOverride.class,
            isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlBidirectionalOverride.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlBig.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlBig.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlBlink.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlBlink.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlBold.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlBold.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlCitation.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlCitation.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlCode.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlCode.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlDefinition.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlDefinition.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlDeletedText.class,
            isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlEmphasis.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlEmphasis.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlInlineQuotation.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlInsertedText.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlItalic.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlItalic.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlKeyboard.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlKeyboard.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlNoBreak.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlNoBreak.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlRt.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlRt.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlRp.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlRp.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlRuby.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlRuby.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlS.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlS.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlSample.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlSample.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlSmall.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlSmall.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlStrike.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlStrike.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlSubscript.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlSubscript.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlSuperscript.class,
            isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlSuperscript.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlStrong.class, isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlStrong.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlTeletype.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlTeletype.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlUnderlined.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlUnderlined.class, browsers = @WebBrowser(value = IE, minVersion = 11)),
        @JsxClass(domClass = HtmlVariable.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
        @JsxClass(domClass = HtmlVariable.class, browsers = @WebBrowser(value = IE, minVersion = 11))
    })
public class HTMLPhraseElement extends HTMLElement {

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        if (getBrowserVersion().hasFeature(JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
            if ((domNode instanceof HtmlAbbreviated && getBrowserVersion().hasFeature(HTMLABBREVIATED))
                || domNode instanceof HtmlAcronym
                || domNode instanceof HtmlBidirectionalOverride
                || domNode instanceof HtmlBig
                || domNode instanceof HtmlBlink
                || domNode instanceof HtmlBold
                || domNode instanceof HtmlCitation
                || domNode instanceof HtmlCode
                || domNode instanceof HtmlDefinition
                || domNode instanceof HtmlEmphasis
                || domNode instanceof HtmlItalic
                || domNode instanceof HtmlKeyboard
                || domNode instanceof HtmlNoBreak
                || domNode instanceof HtmlS
                || domNode instanceof HtmlSample
                || domNode instanceof HtmlSmall
                || domNode instanceof HtmlStrong
                || domNode instanceof HtmlStrike
                || domNode instanceof HtmlSubscript
                || domNode instanceof HtmlSuperscript
                || domNode instanceof HtmlTeletype
                || domNode instanceof HtmlUnderlined
                || domNode instanceof HtmlVariable
                ) {
                ActiveXObject.addProperty(this, "cite", true, true);
                ActiveXObject.addProperty(this, "dateTime", true, true);
            }
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
        if (getWindow().getWebWindow() != null && getBrowserVersion().hasFeature(JS_PHRASE_COMMON_CLASS_NAME)) {
            return "HTMLElement";
        }
        return super.getClassName();
    }
}
