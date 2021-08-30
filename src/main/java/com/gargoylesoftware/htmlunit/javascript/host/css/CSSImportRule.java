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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_CSSTEXT_IE_STYLE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.css.dom.CSSImportRuleImpl;
import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for {@code CSSImportRule}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class CSSImportRule extends CSSRule {

    private MediaList media_;
    private CSSStyleSheet importedStylesheet_;

    /**
     * Creates a new instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public CSSImportRule() {
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSImportRule(final CSSStyleSheet stylesheet, final CSSImportRuleImpl rule) {
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
            final MediaListImpl ml = getImportRule().getMedia();
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
            final CSSStyleSheet importedSheet = owningSheet.getImportedStyleSheet(getImportRule());
            importedStylesheet_ = new CSSStyleSheet(null, ownerNode.getWindow(),
                    importedSheet.getWrappedSheet(), importedSheet.getUri());
        }
        return importedStylesheet_;
    }

    /**
     * Returns the wrapped rule, as an import rule.
     * @return the wrapped rule, as an import rule
     */
    private CSSImportRuleImpl getImportRule() {
        return (CSSImportRuleImpl) getRule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssText() {
        String cssText = super.getCssText();
        if (getBrowserVersion().hasFeature(CSS_CSSTEXT_IE_STYLE)) {
            cssText = REPLACEMENT_IE.matcher(cssText).replaceFirst("url( $1 )");
        }
        return cssText;
    }
}
