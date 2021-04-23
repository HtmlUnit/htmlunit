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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.css.dom.CSSUnknownRuleImpl;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code CSSKeyframesRule}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF78, IE})
public class CSSKeyframesRule extends CSSRule {

    /**
     * Default constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public CSSKeyframesRule() {
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSKeyframesRule(final CSSStyleSheet stylesheet, final CSSUnknownRuleImpl rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the type of the rule.
     * @return the type of the rule.
     */
    @Override
    @JsxGetter
    public short getType() {
        return KEYFRAMES_RULE;
    }
}
