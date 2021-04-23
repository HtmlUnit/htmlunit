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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSCharsetRuleImpl;
import com.gargoylesoftware.css.dom.CSSFontFaceRuleImpl;
import com.gargoylesoftware.css.dom.CSSImportRuleImpl;
import com.gargoylesoftware.css.dom.CSSMediaRuleImpl;
import com.gargoylesoftware.css.dom.CSSPageRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
import com.gargoylesoftware.css.dom.CSSUnknownRuleImpl;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for {@code CSSRule}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class CSSRule extends SimpleScriptable {

    private static final Log LOG = LogFactory.getLog(CSSRule.class);

    /**
     * The rule is a {@code CSSUnknownRule}.
     */
    @JsxConstant(IE)
    public static final short UNKNOWN_RULE              = org.w3c.dom.css.CSSRule.UNKNOWN_RULE;

    /**
     * The rule is a {@code CSSStyleRule}.
     */
    @JsxConstant
    public static final short STYLE_RULE                = org.w3c.dom.css.CSSRule.STYLE_RULE;

    /**
     * The rule is a {@code CSSCharsetRule}.
     */
    @JsxConstant
    public static final short CHARSET_RULE              = org.w3c.dom.css.CSSRule.CHARSET_RULE;

    /**
     * The rule is a {@code CSSImportRule}.
     */
    @JsxConstant
    public static final short IMPORT_RULE               = org.w3c.dom.css.CSSRule.IMPORT_RULE;

    /**
     * The rule is a {@code CSSMediaRule}.
     */
    @JsxConstant
    public static final short MEDIA_RULE                = org.w3c.dom.css.CSSRule.MEDIA_RULE;

    /**
     * The rule is a {@code CSSFontFaceRule}.
     */
    @JsxConstant
    public static final short FONT_FACE_RULE            = org.w3c.dom.css.CSSRule.FONT_FACE_RULE;

    /**
     * The rule is a {@code CSSPageRule}.
     */
    @JsxConstant
    public static final short PAGE_RULE                 = org.w3c.dom.css.CSSRule.PAGE_RULE;

    /**
     * The rule is a {@code CSSKeyframesRule}.
     */
    @JsxConstant
    public static final short KEYFRAMES_RULE            = 7;

    /**
     * The rule is a {@code CSSKeyframeRule}.
     */
    @JsxConstant
    public static final short KEYFRAME_RULE             = 8;

    /**
     * The rule is a {@code CSSNamespaceRule}.
     */
    @JsxConstant
    public static final short NAMESPACE_RULE           = 10;

    /**
     * The rule is a {@code CSSCounterStyleRule}.
     */
    @JsxConstant({FF, FF78})
    public static final short COUNTER_STYLE_RULE        = 11;

    /**
     * The rule is a {@code CSSSupportsRule}.
     */
    @JsxConstant({CHROME, EDGE, FF, FF78})
    public static final short SUPPORTS_RULE             = 12;

    /**
     * The rule is a {@code CSSCounterStyleRule}.
     */
    @JsxConstant({FF, FF78})
    public static final short FONT_FEATURE_VALUES_RULE  = 14;

    /**
     * The rule is a {@code CSSViewportRule}.
     */
    @JsxConstant(IE)
    public static final short VIEWPORT_RULE  = 15;

    private final CSSStyleSheet stylesheet_;

    private final AbstractCSSRuleImpl rule_;

    /**
     * Creates a new instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public CSSRule() {
        stylesheet_ = null;
        rule_ = null;
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
    public short getType() {
        if (rule_ instanceof CSSCharsetRuleImpl) {
            return CSSRule.CHARSET_RULE;
        }
        if (rule_ instanceof CSSFontFaceRuleImpl) {
            return CSSRule.FONT_FACE_RULE;
        }
        if (rule_ instanceof CSSImportRuleImpl) {
            return CSSRule.IMPORT_RULE;
        }
        if (rule_ instanceof CSSMediaRuleImpl) {
            return CSSRule.MEDIA_RULE;
        }
        if (rule_ instanceof CSSPageRuleImpl) {
            return CSSRule.PAGE_RULE;
        }
        if (rule_ instanceof CSSStyleRuleImpl) {
            return CSSRule.STYLE_RULE;
        }
        if (rule_ instanceof CSSUnknownRuleImpl) {
            return CSSRule.UNKNOWN_RULE;
        }

        return CSSRule.UNKNOWN_RULE;
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
     * Sets the parsable textual representation of the rule.
     * @param cssText the parsable textual representation of the rule
     */
    @JsxSetter({FF, FF78, IE})
    public void setCssText(final String cssText) {
        rule_.setCssText(cssText);
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
            return CSSRule.create(stylesheet_, parentRule);
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
