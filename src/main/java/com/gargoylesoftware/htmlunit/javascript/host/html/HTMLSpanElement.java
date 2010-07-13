/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlExample;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlListing;
import com.gargoylesoftware.htmlunit.html.HtmlNoBreak;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
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
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;

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
        final BrowserVersion browser = getBrowserVersion();
        if (browser.hasFeature(BrowserVersionFeatures.GENERATED_90)) {
            if ((element instanceof HtmlAbbreviated && browser.hasFeature(BrowserVersionFeatures.HTMLABBREVIATED))
                || element instanceof HtmlAcronym
                || element instanceof HtmlAddress
                || element instanceof HtmlBidirectionalOverride
                || element instanceof HtmlBig
                || element instanceof HtmlBold
                || element instanceof HtmlBlink
                || element instanceof HtmlCenter
                || element instanceof HtmlCitation
                || element instanceof HtmlCode
                || element instanceof HtmlDefinition
                || element instanceof HtmlExample
                || element instanceof HtmlEmphasis
                || element instanceof HtmlItalic
                || element instanceof HtmlKeyboard
                || element instanceof HtmlListing
                || element instanceof HtmlNoBreak
                || element instanceof HtmlPlainText
                || element instanceof HtmlS
                || element instanceof HtmlSample
                || element instanceof HtmlSmall
                || element instanceof HtmlStrike
                || element instanceof HtmlStrong
                || element instanceof HtmlSubscript
                || element instanceof HtmlSuperscript
                || element instanceof HtmlTeletype
                || element instanceof HtmlUnderlined
                || element instanceof HtmlVariable) {
                ActiveXObject.addProperty(this, "cite", true, true);
            }
            if ((element instanceof HtmlAbbreviated && browser.hasFeature(BrowserVersionFeatures.HTMLABBREVIATED))
                    || element instanceof HtmlAcronym
                    || element instanceof HtmlBold
                    || element instanceof HtmlBidirectionalOverride
                    || element instanceof HtmlBig
                    || element instanceof HtmlBlink
                    || element instanceof HtmlCitation
                    || element instanceof HtmlCode
                    || element instanceof HtmlDefinition
                    || element instanceof HtmlEmphasis
                    || element instanceof HtmlItalic
                    || element instanceof HtmlKeyboard
                    || element instanceof HtmlNoBreak
                    || element instanceof HtmlS
                    || element instanceof HtmlSample
                    || element instanceof HtmlSmall
                    || element instanceof HtmlStrike
                    || element instanceof HtmlStrong
                    || element instanceof HtmlSubscript
                    || element instanceof HtmlSuperscript
                    || element instanceof HtmlTeletype
                    || element instanceof HtmlUnderlined
                    || element instanceof HtmlVariable) {
                ActiveXObject.addProperty(this, "dateTime", true, true);
            }
        }
    }

    /**
     * Returns the value of the "cite" property.
     * @return the value of the "cite" property
     */
    public String jsxGet_cite() {
        String cite = getDomNodeOrDie().getAttribute("cite");
        if (cite == NOT_FOUND) {
            cite = "";
        }
        return cite;
    }

    /**
     * Returns the value of the "cite" property.
     * @param cite the value
     */
    public void jsxSet_cite(final String cite) {
        getDomNodeOrDie().setAttribute("cite", cite);
    }

    /**
     * Returns the value of the "dateTime" property.
     * @return the value of the "dateTime" property
     */
    public String jsxGet_dateTime() {
        String dateTime = getDomNodeOrDie().getAttribute("datetime");
        if (dateTime == NOT_FOUND) {
            dateTime = "";
        }
        return dateTime;
    }

    /**
     * Returns the value of the "dateTime" property.
     * @param dateTime the value
     */
    public void jsxSet_dateTime(final String dateTime) {
        getDomNodeOrDie().setAttribute("datetime", dateTime);
    }
}
