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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.css.dom.CSSMediaRuleImpl;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code CSSConditionRule}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CSSConditionRule">MDN doc</a>
 */
@JsxClass({CHROME, EDGE, FF, FF_ESR})
@JsxClass(isJSObject = false, value = IE)
public class CSSConditionRule extends CSSGroupingRule {

    /**
     * Creates a new instance.
     */
    public CSSConditionRule() {
    }

    @Override
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSConditionRule(final CSSStyleSheet stylesheet, final CSSMediaRuleImpl rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the text of the condition of the rule.
     * @return the text of the condition of the rule
     */
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
    public String getConditionText() {
        return getConditionRule().getMediaList().getMediaText();
    }

    /**
     * Returns the wrapped rule, as a media rule.
     * @return the wrapped rule, as a media rule
     */
    private CSSMediaRuleImpl getConditionRule() {
        return (CSSMediaRuleImpl) getRule();
    }
}
