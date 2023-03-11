/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.html;

import static org.htmlunit.BrowserVersionFeatures.HTMLABBREVIATED;
import static org.htmlunit.BrowserVersionFeatures.JS_PHRASE_COMMON_CLASS_NAME;
import static org.htmlunit.BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT;
import static org.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlAbbreviated;
import org.htmlunit.html.HtmlAcronym;
import org.htmlunit.html.HtmlBidirectionalOverride;
import org.htmlunit.html.HtmlBig;
import org.htmlunit.html.HtmlBlink;
import org.htmlunit.html.HtmlBold;
import org.htmlunit.html.HtmlCitation;
import org.htmlunit.html.HtmlCode;
import org.htmlunit.html.HtmlDefinition;
import org.htmlunit.html.HtmlEmphasis;
import org.htmlunit.html.HtmlItalic;
import org.htmlunit.html.HtmlKeyboard;
import org.htmlunit.html.HtmlNoBreak;
import org.htmlunit.html.HtmlRp;
import org.htmlunit.html.HtmlRt;
import org.htmlunit.html.HtmlRuby;
import org.htmlunit.html.HtmlS;
import org.htmlunit.html.HtmlSample;
import org.htmlunit.html.HtmlSmall;
import org.htmlunit.html.HtmlStrike;
import org.htmlunit.html.HtmlStrong;
import org.htmlunit.html.HtmlSubscript;
import org.htmlunit.html.HtmlSuperscript;
import org.htmlunit.html.HtmlTeletype;
import org.htmlunit.html.HtmlUnderlined;
import org.htmlunit.html.HtmlVariable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.host.ActiveXObject;

/**
 * The JavaScript object {@code HTMLPhraseElement}.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlAbbreviated.class, value = IE)
@JsxClass(domClass = HtmlAcronym.class, value = IE)
@JsxClass(domClass = HtmlBidirectionalOverride.class, value = IE)
@JsxClass(domClass = HtmlBig.class, value = IE)
@JsxClass(domClass = HtmlBlink.class, value = IE)
@JsxClass(domClass = HtmlBold.class, value = IE)
@JsxClass(domClass = HtmlCitation.class, value = IE)
@JsxClass(domClass = HtmlCode.class, value = IE)
@JsxClass(domClass = HtmlDefinition.class, value = IE)
@JsxClass(domClass = HtmlEmphasis.class, value = IE)
@JsxClass(domClass = HtmlItalic.class, value = IE)
@JsxClass(domClass = HtmlKeyboard.class, value = IE)
@JsxClass(domClass = HtmlNoBreak.class, value = IE)
@JsxClass(domClass = HtmlRt.class, value = IE)
@JsxClass(domClass = HtmlRp.class, value = IE)
@JsxClass(domClass = HtmlRuby.class, value = IE)
@JsxClass(domClass = HtmlS.class, value = IE)
@JsxClass(domClass = HtmlSample.class, value = IE)
@JsxClass(domClass = HtmlSmall.class, value = IE)
@JsxClass(domClass = HtmlStrike.class, value = IE)
@JsxClass(domClass = HtmlSubscript.class, value = IE)
@JsxClass(domClass = HtmlSuperscript.class, value = IE)
@JsxClass(domClass = HtmlStrong.class, value = IE)
@JsxClass(domClass = HtmlTeletype.class, value = IE)
@JsxClass(domClass = HtmlUnderlined.class, value = IE)
@JsxClass(domClass = HtmlVariable.class, value = IE)
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
     * Returns the value of the {@code cite} property.
     * @return the value of the {@code cite} property
     */
    public String getCite() {
        return getDomNodeOrDie().getAttributeDirect("cite");
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
        return getDomNodeOrDie().getAttributeDirect("datetime");
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
