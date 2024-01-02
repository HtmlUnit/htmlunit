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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.cssparser.dom.CSSUnknownRuleImpl;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code CSSKeyframesRule}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class CSSKeyframesRule extends CSSRule {

    /**
     * Default constructor.
     */
    public CSSKeyframesRule() {
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
    protected CSSKeyframesRule(final CSSStyleSheet stylesheet, final CSSUnknownRuleImpl rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the type of the rule.
     * @return the type of the rule.
     */
    @Override
    @JsxGetter
    public int getType() {
        return KEYFRAMES_RULE;
    }
}
