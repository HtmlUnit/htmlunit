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
 * The JavaScript object "HTMLSpanElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@JsxClasses({
    @JsxClass(domClass = HtmlAbbreviated.class),
    @JsxClass(domClass = HtmlAcronym.class),
    @JsxClass(domClass = HtmlBidirectionalOverride.class),
    @JsxClass(domClass = HtmlBig.class),
    @JsxClass(domClass = HtmlBlink.class),
    @JsxClass(domClass = HtmlBold.class),
    @JsxClass(domClass = HtmlCitation.class),
    @JsxClass(domClass = HtmlCode.class),
    @JsxClass(domClass = HtmlDefinition.class),
    @JsxClass(domClass = HtmlDeletedText.class, browsers = @WebBrowser(value = IE, maxVersion = 8)),
    @JsxClass(domClass = HtmlEmphasis.class),
    @JsxClass(domClass = HtmlInlineQuotation.class, browsers = @WebBrowser(value = IE, maxVersion = 8)),
    @JsxClass(domClass = HtmlInsertedText.class, browsers = @WebBrowser(value = IE, maxVersion = 8)),
    @JsxClass(domClass = HtmlItalic.class),
    @JsxClass(domClass = HtmlKeyboard.class),
    @JsxClass(domClass = HtmlNoBreak.class, browsers = @WebBrowser(IE)),
    @JsxClass(domClass = HtmlRt.class),
    @JsxClass(domClass = HtmlRp.class),
    @JsxClass(domClass = HtmlRuby.class),
    @JsxClass(domClass = HtmlS.class),
    @JsxClass(domClass = HtmlSample.class),
    @JsxClass(domClass = HtmlSmall.class),
    @JsxClass(domClass = HtmlStrike.class),
    @JsxClass(domClass = HtmlSubscript.class),
    @JsxClass(domClass = HtmlSuperscript.class),
    @JsxClass(domClass = HtmlStrong.class),
    @JsxClass(domClass = HtmlTeletype.class),
    @JsxClass(domClass = HtmlUnderlined.class),
    @JsxClass(domClass = HtmlVariable.class)
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
