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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECTOR_TEXT_LOWERCASE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
import com.gargoylesoftware.htmlunit.css.WrappedCssStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * A JavaScript object for {@code CSSStyleRule}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@JsxClass
public class CSSStyleRule extends CSSRule {
    private static final Pattern SELECTOR_PARTS_PATTERN = Pattern.compile("[.#]?[a-zA-Z]+");
    private static final Pattern SELECTOR_REPLACE_PATTERN = Pattern.compile("\\*([.#])");

    /**
     * Creates a new instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public CSSStyleRule() {
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSStyleRule(final CSSStyleSheet stylesheet, final CSSStyleRuleImpl rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the textual representation of the selector for the rule set.
     * @return the textual representation of the selector for the rule set
     */
    @JsxGetter
    public String getSelectorText() {
        String selectorText = ((CSSStyleRuleImpl) getRule()).getSelectorText();
        final Matcher m = SELECTOR_PARTS_PATTERN.matcher(selectorText);
        final StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String fixedName = m.group();
            // this should be handled with the right regex but...
            if (getBrowserVersion().hasFeature(JS_SELECTOR_TEXT_LOWERCASE)
                    && !fixedName.isEmpty() && '.' != fixedName.charAt(0) && '#' != fixedName.charAt(0)) {
                fixedName = fixedName.toLowerCase(Locale.ROOT);
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
    public void setSelectorText(final String selectorText) {
        ((CSSStyleRuleImpl) getRule()).setSelectorText(selectorText);
    }

    /**
     * Returns the declaration-block of this rule set.
     * @return the declaration-block of this rule set
     */
    @JsxGetter
    public CSSStyleDeclaration getStyle() {
        final WrappedCssStyleDeclaration styleDeclaration
                = new WrappedCssStyleDeclaration(((CSSStyleRuleImpl) getRule()).getStyle());
        return new CSSStyleDeclaration(getParentStyleSheet(), styleDeclaration);
    }

    /**
     * Returns the readonly property.
     * @return the readonly value.
     */
    @JsxGetter(IE)
    public boolean isReadOnly() {
        return false;
    }
}
