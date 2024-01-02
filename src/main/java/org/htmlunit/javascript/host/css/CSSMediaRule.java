/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.css;

import static org.htmlunit.BrowserVersionFeatures.CSS_CSSTEXT_IE_STYLE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.css.CssMediaList;
import org.htmlunit.cssparser.dom.CSSMediaRuleImpl;
import org.htmlunit.cssparser.dom.MediaListImpl;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for a {@link CSSMediaRuleImpl}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Frank Danek
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CSSMediaRule">MDN doc</a>
 */
@JsxClass
public class CSSMediaRule extends CSSConditionRule {

    private MediaList media_;

    /**
     * Creates a new instance.
     */
    public CSSMediaRule() {
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    @Override
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSMediaRule(final CSSStyleSheet stylesheet, final CSSMediaRuleImpl rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the media types that the imported CSS style sheet applies to.
     * @return the media types that the imported CSS style sheet applies to
     */
    @JsxGetter
    public MediaList getMedia() {
        if (media_ == null) {
            final CSSStyleSheet parent = getParentStyleSheet();
            final MediaListImpl ml = getMediaRule().getMediaList();

            media_ = new MediaList(parent, new CssMediaList(ml));
        }
        return media_;
    }

    /**
     * Returns the wrapped rule, as a media rule.
     * @return the wrapped rule, as a media rule
     */
    private CSSMediaRuleImpl getMediaRule() {
        return (CSSMediaRuleImpl) getRule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssText() {
        String cssText = super.getCssText();
        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(CSS_CSSTEXT_IE_STYLE)) {
            cssText = StringUtils.replace(cssText, "\n  ", "\n\t");
            cssText = StringUtils.replace(cssText, " { }", " {  }");
        }
        return cssText;
    }
}
