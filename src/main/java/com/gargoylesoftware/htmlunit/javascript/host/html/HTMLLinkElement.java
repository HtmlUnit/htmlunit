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

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;

/**
 * The JavaScript object "HTMLLinkElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLLinkElement extends HTMLElement {

    /**
     * The associated style sheet (only valid for links of type
     * <tt>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</tt>).
     */
    private CSSStyleSheet sheet_;

    /**
     * Creates an instance.
     */
    public HTMLLinkElement() {
        // Empty.
    }

    /**
     * Sets the href property.
     * @param href href attribute value
     */
    public void jsxSet_href(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of the href property.
     * @return the href property
     * @throws Exception if an error occurs
     */
    public String jsxGet_href() throws Exception {
        final HtmlLink link = (HtmlLink) getDomNodeOrDie();
        final String href = link.getHrefAttribute();
        if (href.length() == 0) {
            return href;
        }
        try {
            return ((HtmlPage) link.getPage()).getFullyQualifiedUrl(href).toString();
        }
        catch (final MalformedURLException e) {
            return href;
        }
    }

    /**
     * Sets the rel property.
     * @param rel rel attribute value
     */
    public void jsxSet_rel(final String rel) {
        getDomNodeOrDie().setAttribute("rel", rel);
    }

    /**
     * Returns the value of the rel property.
     * @return the rel property
     * @throws Exception if an error occurs
     */
    public String jsxGet_rel() throws Exception {
        return ((HtmlLink) getDomNodeOrDie()).getRelAttribute();
    }

    /**
     * Sets the rev property.
     * @param rel rev attribute value
     */
    public void jsxSet_rev(final String rel) {
        getDomNodeOrDie().setAttribute("rev", rel);
    }

    /**
     * Returns the value of the rev property.
     * @return the rev property
     * @throws Exception if an error occurs
     */
    public String jsxGet_rev() throws Exception {
        return ((HtmlLink) getDomNodeOrDie()).getRevAttribute();
    }

    /**
     * Sets the type property.
     * @param type type attribute value
     */
    public void jsxSet_type(final String type) {
        getDomNodeOrDie().setAttribute("type", type);
    }

    /**
     * Returns the value of the type property.
     * @return the type property
     * @throws Exception if an error occurs
     */
    public String jsxGet_type() throws Exception {
        return ((HtmlLink) getDomNodeOrDie()).getTypeAttribute();
    }

    /**
     * Returns the associated style sheet (only valid for links of type
     * <tt>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</tt>).
     * @return the associated style sheet
     */
    public CSSStyleSheet getSheet() {
        if (sheet_ == null) {
            sheet_ = CSSStyleSheet.loadStylesheet(getWindow(), this, (HtmlLink) getDomNodeOrDie(), null);
        }
        return sheet_;
    }

}
