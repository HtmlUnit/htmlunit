/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_90;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLABBREVIATED;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAcronym;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride;
import com.gargoylesoftware.htmlunit.html.HtmlBig;
import com.gargoylesoftware.htmlunit.html.HtmlBlink;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlCenter;
import com.gargoylesoftware.htmlunit.html.HtmlCitation;
import com.gargoylesoftware.htmlunit.html.HtmlCode;
import com.gargoylesoftware.htmlunit.html.HtmlDefinition;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlExample;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlListing;
import com.gargoylesoftware.htmlunit.html.HtmlMultiColumn;
import com.gargoylesoftware.htmlunit.html.HtmlNoBreak;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
import com.gargoylesoftware.htmlunit.html.HtmlS;
import com.gargoylesoftware.htmlunit.html.HtmlSample;
import com.gargoylesoftware.htmlunit.html.HtmlSmall;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlStrike;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubscript;
import com.gargoylesoftware.htmlunit.html.HtmlSuperscript;
import com.gargoylesoftware.htmlunit.html.HtmlTeletype;
import com.gargoylesoftware.htmlunit.html.HtmlUnderlined;
import com.gargoylesoftware.htmlunit.html.HtmlVariable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;

/**
 * The JavaScript object "HTMLSpanElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@JsxClass(domClasses = { HtmlAbbreviated.class, HtmlAcronym.class, HtmlAddress.class,
        HtmlBidirectionalOverride.class, HtmlBig.class, HtmlBold.class, HtmlBlink.class, HtmlCenter.class,
        HtmlCitation.class, HtmlCode.class, HtmlDefinition.class, HtmlDefinitionDescription.class,
        HtmlDefinitionTerm.class, HtmlEmphasis.class, HtmlItalic.class, HtmlKeyboard.class, HtmlListing.class,
        HtmlMultiColumn.class, HtmlNoBreak.class, HtmlPlainText.class, HtmlS.class, HtmlSample.class,
        HtmlSmall.class, HtmlSpan.class, HtmlStrike.class, HtmlStrong.class, HtmlSubscript.class,
        HtmlSuperscript.class, HtmlTeletype.class, HtmlUnderlined.class, HtmlVariable.class, HtmlExample.class })
public class HTMLSpanElement extends HTMLElement {

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);
        final BrowserVersion browser = getBrowserVersion();
        if (!browser.hasFeature(GENERATED_90)) {
            return;
        }

        if ((domNode instanceof HtmlAbbreviated && browser.hasFeature(HTMLABBREVIATED))
            || domNode instanceof HtmlAcronym
            || domNode instanceof HtmlBidirectionalOverride
            || domNode instanceof HtmlBig
            || domNode instanceof HtmlBold
            || domNode instanceof HtmlBlink
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
            || domNode instanceof HtmlStrike
            || domNode instanceof HtmlStrong
            || domNode instanceof HtmlSubscript
            || domNode instanceof HtmlSuperscript
            || domNode instanceof HtmlTeletype
            || domNode instanceof HtmlUnderlined
            || domNode instanceof HtmlVariable) {
            ActiveXObject.addProperty(this, "cite", true, true);
            ActiveXObject.addProperty(this, "dateTime", true, true);
            return;
        }

        if (domNode instanceof HtmlAddress
            || domNode instanceof HtmlCenter
            || domNode instanceof HtmlExample
            || domNode instanceof HtmlListing
            || domNode instanceof HtmlPlainText) {
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
     * {@inheritDoc}
     */
    protected boolean isLowerCaseInOuterHtml() {
        if (getDomNodeOrDie() instanceof HtmlMultiColumn) {
            return true;
        }
        return super.isLowerCaseInOuterHtml();
    }
}
