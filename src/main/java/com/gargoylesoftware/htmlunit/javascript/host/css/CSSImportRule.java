/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.MediaList;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a CSSImportRule.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@JsxClass
public class CSSImportRule extends CSSRule {

    private MediaList media_;
    private CSSStyleSheet importedStylesheet_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSImportRule() {
        // Empty.
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSImportRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSImportRule rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the URL of the imported style sheet.
     * @return the URL of the imported style sheet
     */
    @JsxGetter
    public String getHref() {
        return getImportRule().getHref();
    }

    /**
     * Returns the media types that the imported CSS style sheet applies to.
     * @return the media types that the imported CSS style sheet applies to
     */
    @JsxGetter
    public MediaList getMedia() {
        if (media_ == null) {
            final CSSStyleSheet parent = getParentStyleSheet();
            final org.w3c.dom.stylesheets.MediaList ml = getImportRule().getMedia();
            media_ = new MediaList(parent, ml);
        }
        return media_;
    }

    /**
     * Returns the style sheet referred to by this rule.
     * @return the style sheet referred to by this rule
     */
    @JsxGetter
    public CSSStyleSheet getStyleSheet() {
        if (importedStylesheet_ == null) {
            final CSSStyleSheet owningSheet = getParentStyleSheet();
            final HTMLElement ownerNode = owningSheet.getOwnerNode();
            final org.w3c.dom.css.CSSStyleSheet importedStylesheet = getImportRule().getStyleSheet();
            importedStylesheet_ = new CSSStyleSheet(ownerNode, importedStylesheet, owningSheet.getUri());
        }
        return importedStylesheet_;
    }

    /**
     * Returns the wrapped rule, as an import rule.
     * @return the wrapped rule, as an import rule
     */
    private org.w3c.dom.css.CSSImportRule getImportRule() {
        return (org.w3c.dom.css.CSSImportRule) getRule();
    }

}
