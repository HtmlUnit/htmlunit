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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for a CSSRule.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(isJSObject = false)
public class CSSRule extends SimpleScriptable {

    /**
     * The rule is a <code>CSSUnknownRule</code>.
     */
    public static final short UNKNOWN_RULE              = org.w3c.dom.css.CSSRule.UNKNOWN_RULE;
    /**
     * The rule is a <code>CSSStyleRule</code>.
     */
    public static final short STYLE_RULE                = org.w3c.dom.css.CSSRule.STYLE_RULE;
    /**
     * The rule is a <code>CSSCharsetRule</code>.
     */
    public static final short CHARSET_RULE              = org.w3c.dom.css.CSSRule.CHARSET_RULE;
    /**
     * The rule is a <code>CSSImportRule</code>.
     */
    public static final short IMPORT_RULE               = org.w3c.dom.css.CSSRule.IMPORT_RULE;
    /**
     * The rule is a <code>CSSMediaRule</code>.
     */
    public static final short MEDIA_RULE                = org.w3c.dom.css.CSSRule.MEDIA_RULE;
    /**
     * The rule is a <code>CSSFontFaceRule</code>.
     */
    public static final short FONT_FACE_RULE            = org.w3c.dom.css.CSSRule.FONT_FACE_RULE;
    /**
     * The rule is a <code>CSSPageRule</code>.
     */
    public static final short PAGE_RULE                 = org.w3c.dom.css.CSSRule.PAGE_RULE;

    private final CSSStyleSheet stylesheet_;

    private final org.w3c.dom.css.CSSRule rule_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
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
    public static CSSRule create(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        switch (rule.getType()) {
            case STYLE_RULE:
                return new CSSStyleRule(stylesheet, rule);
            case IMPORT_RULE:
                return new CSSImportRule(stylesheet, rule);
            case CHARSET_RULE:
                return new CSSCharsetRule(stylesheet, (org.w3c.dom.css.CSSCharsetRule) rule);
            case MEDIA_RULE:
                return new CSSMediaRule(stylesheet, (org.w3c.dom.css.CSSMediaRule) rule);
            default:
                throw new UnsupportedOperationException("CSSRule "
                    + rule.getClass().getName() + " is not yet supported.");
        }
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        stylesheet_ = stylesheet;
        rule_ = rule;
        setParentScope(stylesheet);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the type of the rule.
     * @return the type of the rule.
     */
    @JsxGetter(@WebBrowser(FF))
    public short get_type() {
        return rule_.getType();
    }

    /**
     * Returns the parsable textual representation of the rule.
     * This reflects the current state of the rule and not its initial value.
     * @return the parsable textual representation of the rule.
     */
    @JsxGetter(@WebBrowser(FF))
    public String get_cssText() {
        return rule_.getCssText();
    }

    /**
     * Sets the parsable textual representation of the rule.
     * @param cssText the parsable textual representation of the rule
     */
    @JsxSetter(@WebBrowser(FF))
    public void jsxSet_cssText(final String cssText) {
        rule_.setCssText(cssText);
    }

    /**
     * Returns the style sheet that contains this rule.
     * @return the style sheet that contains this rule.
     */
    @JsxGetter(@WebBrowser(FF))
    public CSSStyleSheet get_parentStyleSheet() {
        return stylesheet_;
    }

    /**
     * If this rule is contained inside another rule (e.g. a style rule inside an @media block),
     * this is the containing rule. If this rule is not nested inside any other rules, this returns <code>null</code>.
     * @return the parent rule
     */
    @JsxGetter(@WebBrowser(FF))
    public CSSRule get_parentRule() {
        final org.w3c.dom.css.CSSRule parentRule = rule_.getParentRule();
        if (parentRule != null) {
            return CSSRule.create(stylesheet_, parentRule);
        }
        return null;
    }

    /**
     * Returns the wrapped rule.
     * @return the wrapped rule.
     */
    protected org.w3c.dom.css.CSSRule getRule() {
        return rule_;
    }

}
