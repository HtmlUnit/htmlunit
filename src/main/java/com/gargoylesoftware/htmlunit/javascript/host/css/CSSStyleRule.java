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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxSetter;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * A JavaScript object for a CSSStyleRule.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class CSSStyleRule extends CSSRule {
    private static final Pattern SELECTOR_PARTS_PATTERN = Pattern.compile("[\\.#]?[a-zA-Z]+");
    private static final Pattern SELECTOR_REPLACE_PATTERN = Pattern.compile("\\*([\\.#])");

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
    protected CSSStyleRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the textual representation of the selector for the rule set.
     * @return the textual representation of the selector for the rule set
     */
    @JsxGetter
    public String jsxGet_selectorText() {
        String selectorText = ((org.w3c.dom.css.CSSStyleRule) getRule()).getSelectorText();
        final Matcher m = SELECTOR_PARTS_PATTERN.matcher(selectorText);
        final StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String fixedName = m.group();
            // this should be handled with the right regex but...
            if ((fixedName.length() > 0)
                    && (('.' == fixedName.charAt(0)) || ('#' == fixedName.charAt(0)))) {
                // nothing
            }
            else if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_SELECTOR_TEXT_UPPERCASE)) {
                fixedName = fixedName.toUpperCase();
            }
            else {
                fixedName = fixedName.toLowerCase();
            }
            fixedName = StringUtils.sanitizeForAppendReplacement(fixedName);
            m.appendReplacement(sb, fixedName);
        }
        m.appendTail(sb);

        // ".foo" and not "*.foo"
        selectorText = SELECTOR_REPLACE_PATTERN.matcher(sb.toString()).replaceAll("$1");
        return selectorText;
    }

    /**
     * Sets the textual representation of the selector for the rule set.
     * @param selectorText the textual representation of the selector for the rule set
     */
    @JsxSetter
    public void jsxSet_selectorText(final String selectorText) {
        ((org.w3c.dom.css.CSSStyleRule) getRule()).setSelectorText(selectorText);
    }

    /**
     * Returns the declaration-block of this rule set.
     * @return the declaration-block of this rule set
     */
    @JsxGetter
    public CSSStyleDeclaration jsxGet_style() {
        return new CSSStyleDeclaration(getParentScope(), ((org.w3c.dom.css.CSSStyleRule) getRule()).getStyle());
    }
}
