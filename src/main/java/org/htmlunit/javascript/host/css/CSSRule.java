/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSCharsetRuleImpl;
import org.htmlunit.cssparser.dom.CSSFontFaceRuleImpl;
import org.htmlunit.cssparser.dom.CSSImportRuleImpl;
import org.htmlunit.cssparser.dom.CSSMediaRuleImpl;
import org.htmlunit.cssparser.dom.CSSPageRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleRuleImpl;
import org.htmlunit.cssparser.dom.CSSUnknownRuleImpl;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for {@code CSSRule}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CSSRule">MDN doc</a>
 */
@JsxClass
public class CSSRule extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(CSSRule.class);

    /**
     * The rule is a {@code CSSUnknownRule}.
     */
    public static final int UNKNOWN_RULE              = org.w3c.dom.css.CSSRule.UNKNOWN_RULE;

    /**
     * The rule is a {@code CSSStyleRule}.
     */
    @JsxConstant
    public static final int STYLE_RULE                = org.w3c.dom.css.CSSRule.STYLE_RULE;

    /**
     * The rule is a {@code CSSCharsetRule}.
     */
    @JsxConstant
    public static final int CHARSET_RULE              = org.w3c.dom.css.CSSRule.CHARSET_RULE;

    /**
     * The rule is a {@code CSSImportRule}.
     */
    @JsxConstant
    public static final int IMPORT_RULE               = org.w3c.dom.css.CSSRule.IMPORT_RULE;

    /**
     * The rule is a {@code CSSMediaRule}.
     */
    @JsxConstant
    public static final int MEDIA_RULE                = org.w3c.dom.css.CSSRule.MEDIA_RULE;

    /**
     * The rule is a {@code CSSFontFaceRule}.
     */
    @JsxConstant
    public static final int FONT_FACE_RULE            = org.w3c.dom.css.CSSRule.FONT_FACE_RULE;

    /**
     * The rule is a {@code CSSPageRule}.
     */
    @JsxConstant
    public static final int PAGE_RULE                 = org.w3c.dom.css.CSSRule.PAGE_RULE;

    /**
     * The rule is a {@code CSSKeyframesRule}.
     */
    @JsxConstant
    public static final int KEYFRAMES_RULE            = 7;

    /**
     * The rule is a {@code CSSKeyframeRule}.
     */
    @JsxConstant
    public static final int KEYFRAME_RULE             = 8;

    /**
     * The rule is a {@code CSSMerginRule}.
     */
    @JsxConstant({CHROME, EDGE})
    public static final int MARGIN_RULE               = 9;

    /**
     * The rule is a {@code CSSNamespaceRule}.
     */
    @JsxConstant
    public static final int NAMESPACE_RULE           = 10;

    /**
     * The rule is a {@code CSSCounterStyleRule}.
     */
    @JsxConstant
    public static final int COUNTER_STYLE_RULE        = 11;

    /**
     * The rule is a {@code CSSSupportsRule}.
     */
    @JsxConstant
    public static final int SUPPORTS_RULE             = 12;

    /**
     * The rule is a {@code CSSCounterStyleRule}.
     */
    @JsxConstant
    public static final int FONT_FEATURE_VALUES_RULE  = 14;

    /**
     * The rule is a {@code CSSViewportRule}.
     */
    public static final int VIEWPORT_RULE  = 15;

    private final CSSStyleSheet stylesheet_;

    private final AbstractCSSRuleImpl rule_;

    /**
     * Creates a new instance.
     */
    public CSSRule() {
        super();
        stylesheet_ = null;
        rule_ = null;
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Creates a CSSRule according to the specified rule type.
     * @param stylesheet the Stylesheet of this rule
     * @param rule the wrapped rule
     * @return a CSSRule subclass according to the rule type
     */
    public static CSSRule create(final CSSStyleSheet stylesheet, final AbstractCSSRuleImpl rule) {
        if (rule instanceof CSSStyleRuleImpl) {
            return new CSSStyleRule(stylesheet, (CSSStyleRuleImpl) rule);
        }
        if (rule instanceof CSSImportRuleImpl) {
            return new CSSImportRule(stylesheet, (CSSImportRuleImpl) rule);
        }
//        if (rule instanceof CSSCharsetRuleImpl) {
//            return new CSSCharsetRule(stylesheet, (CSSCharsetRuleImpl) rule);
//        }
        if (rule instanceof CSSMediaRuleImpl) {
            return new CSSMediaRule(stylesheet, (CSSMediaRuleImpl) rule);
        }
        if (rule instanceof CSSFontFaceRuleImpl) {
            return new CSSFontFaceRule(stylesheet, (CSSFontFaceRuleImpl) rule);
        }
        if (rule instanceof CSSPageRuleImpl) {
            return new CSSPageRule(stylesheet, rule);
        }
        if (rule instanceof CSSUnknownRuleImpl) {
            final CSSUnknownRuleImpl unknownRule = (CSSUnknownRuleImpl) rule;
            if (unknownRule.getCssText().startsWith("@keyframes")) {
                return new CSSKeyframesRule(stylesheet, (CSSUnknownRuleImpl) rule);
            }
            if (LOG.isWarnEnabled()) {
                LOG.warn("Unknown CSSRule " + rule.getClass().getName()
                        + " is not yet supported; rule content: '" + rule.getCssText() + "'");
            }
        }

        if (LOG.isWarnEnabled()) {
            LOG.warn("CSSRule " + rule.getClass().getName()
                    + " is not yet supported; rule content: '" + rule.getCssText() + "'");
        }

        return null;
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSRule(final CSSStyleSheet stylesheet, final AbstractCSSRuleImpl rule) {
        super();
        stylesheet_ = stylesheet;
        rule_ = rule;
        setParentScope(stylesheet);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the type of the rule.
     * @return the type of the rule.
     */
    @JsxGetter
    public int getType() {
        if (rule_ instanceof CSSCharsetRuleImpl) {
            return CHARSET_RULE;
        }
        if (rule_ instanceof CSSFontFaceRuleImpl) {
            return FONT_FACE_RULE;
        }
        if (rule_ instanceof CSSImportRuleImpl) {
            return IMPORT_RULE;
        }
        if (rule_ instanceof CSSMediaRuleImpl) {
            return MEDIA_RULE;
        }
        if (rule_ instanceof CSSPageRuleImpl) {
            return PAGE_RULE;
        }
        if (rule_ instanceof CSSStyleRuleImpl) {
            return STYLE_RULE;
        }
        if (rule_ instanceof CSSUnknownRuleImpl) {
            return UNKNOWN_RULE;
        }

        return UNKNOWN_RULE;
    }

    /**
     * Returns the parsable textual representation of the rule.
     * This reflects the current state of the rule and not its initial value.
     * @return the parsable textual representation of the rule.
     */
    @JsxGetter
    public String getCssText() {
        return rule_.getCssText();
    }

    /**
     * See <a href="https://developer.mozilla.org/en-US/docs/Web/API/CSSRule/cssText">
     * https://developer.mozilla.org/en-US/docs/Web/API/CSSRule/cssText</a>.
     * @param cssText ignored
     */
    @JsxSetter
    public void setCssText(final String cssText) {
        // nothing to do
    }

    /**
     * Returns the style sheet that contains this rule.
     * @return the style sheet that contains this rule.
     */
    @JsxGetter
    public CSSStyleSheet getParentStyleSheet() {
        return stylesheet_;
    }

    /**
     * If this rule is contained inside another rule (e.g. a style rule inside a @media block),
     * this is the containing rule. If this rule is not nested inside any other rules, this returns {@code null}.
     * @return the parent rule
     */
    @JsxGetter
    public CSSRule getParentRule() {
        final AbstractCSSRuleImpl parentRule = rule_.getParentRule();
        if (parentRule != null) {
            return create(stylesheet_, parentRule);
        }
        return null;
    }

    /**
     * Returns the wrapped rule.
     * @return the wrapped rule.
     */
    protected AbstractCSSRuleImpl getRule() {
        return rule_;
    }
}
