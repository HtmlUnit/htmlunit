/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

/**
 * A JavaScript object for a CSSStyleRule.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CSSStyleRule extends CSSRule {

    private static final long serialVersionUID = 207943879569003822L;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSStyleRule() {
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSStyleRule(final Stylesheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the textual representation of the selector for the rule set.
     * @return the textual representation of the selector for the rule set
     */
    public String jsxGet_selectorText() {
        String selectorText = ((org.w3c.dom.css.CSSStyleRule) getRule()).getSelectorText();
        if (!getBrowserVersion().isIE()) {
            selectorText = selectorText.toLowerCase();
        }
        return selectorText;
    }

    /**
     * Sets the textual representation of the selector for the rule set.
     * @param selectorText the textual representation of the selector for the rule set
     */
    public void jsxSet_selectorText(final String selectorText) {
        ((org.w3c.dom.css.CSSStyleRule) getRule()).setSelectorText(selectorText);
    }

    /**
     * Returns the declaration-block of this rule set.
     * @return the declaration-block of this rule set
     */
    public CSSStyleDeclaration jsxGet_style() {
        return new CSSStyleDeclaration(getParentScope(), ((org.w3c.dom.css.CSSStyleRule) getRule()).getStyle());
    }
}
