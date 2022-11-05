/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.css.CssStyleSheet;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object {@code HTMLLinkElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlLink.class)
public class HTMLLinkElement extends HTMLElement {

    private static final Log LOG = LogFactory.getLog(HTMLLinkElement.class);

    /**
     * The associated style sheet (only valid for links of type
     * <code>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</code>).
     */
    private CSSStyleSheet sheet_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
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
     * <code>&lt;link rel="stylesheet" type="text/css" href="..." /&gt;</code>).
     * @return the associated style sheet
     */
    public CSSStyleSheet getSheet() {
        if (sheet_ == null) {
            try {
                final CssStyleSheet sheet =
                        CssStyleSheet.loadStylesheet(getDomNodeOrDie(), (HtmlLink) getDomNodeOrDie(), null);
                sheet_ = new CSSStyleSheet(this, this.getWindow(), sheet);
            }
            catch (final RuntimeException e) {
                // Got something unexpected; we can throw an exception in this case.
                if (LOG.isErrorEnabled()) {
                    LOG.error("RuntimeException loading stylesheet", e);
                }
                throw Context.reportRuntimeError("Exception: " + e);
            }
            catch (final Exception e) {
                // Got something unexpected; we can throw an exception in this case.
                if (LOG.isErrorEnabled()) {
                    LOG.error("Exception loading stylesheet", e);
                }
                throw Context.reportRuntimeError("Exception: " + e);
            }
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
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
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
