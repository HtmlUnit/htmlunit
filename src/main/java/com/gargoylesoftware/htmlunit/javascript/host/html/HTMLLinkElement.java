/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;

/**
 * The JavaScript object {@code HTMLLinkElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlLink.class)
public class HTMLLinkElement extends HTMLElement {

    /**
     * The associated style sheet (only valid for links of type
     * <tt>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</tt>).
     */
    private CSSStyleSheet sheet_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLLinkElement() {
    }

    /**
     * Sets the href property.
     * @param href href attribute value
     */
    @JsxSetter
    public void setHref(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of the href property.
     * @return the href property
     */
    @JsxGetter
    public String getHref() {
        final HtmlLink link = (HtmlLink) getDomNodeOrDie();
        final String href = link.getHrefAttribute();
        if (href.isEmpty()) {
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
    @JsxSetter
    public void setRel(final String rel) {
        getDomNodeOrDie().setAttribute("rel", rel);
    }

    /**
     * Returns the value of the rel property.
     * @return the rel property
     */
    @JsxGetter
    public String getRel() {
        return ((HtmlLink) getDomNodeOrDie()).getRelAttribute();
    }

    /**
     * Sets the rev property.
     * @param rel rev attribute value
     */
    @JsxSetter
    public void setRev(final String rel) {
        getDomNodeOrDie().setAttribute("rev", rel);
    }

    /**
     * Returns the value of the rev property.
     * @return the rev property
     */
    @JsxGetter
    public String getRev() {
        return ((HtmlLink) getDomNodeOrDie()).getRevAttribute();
    }

    /**
     * Sets the type property.
     * @param type type attribute value
     */
    @JsxSetter
    public void setType(final String type) {
        getDomNodeOrDie().setAttribute("type", type);
    }

    /**
     * Returns the value of the type property.
     * @return the type property
     */
    @JsxGetter
    public String getType() {
        return ((HtmlLink) getDomNodeOrDie()).getTypeAttribute();
    }

    /**
     * Returns the associated style sheet (only valid for links of type
     * <tt>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</tt>).
     * @return the associated style sheet
     */
    public CSSStyleSheet getSheet() {
        if (sheet_ == null) {
            sheet_ = new CSSStyleSheet(this, getWindow(), ((HtmlLink) getDomNodeOrDie()).getSheet());
        }
        return sheet_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
    }

    /**
     * Returns the {@code relList} attribute.
     * @return the {@code relList} attribute
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public DOMTokenList getRelList() {
        return new DOMTokenList(this, "rel");
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public boolean isDisabled() {
        return super.isDisabled();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter
    public void setDisabled(final boolean disabled) {
        super.setDisabled(disabled);
    }

}
