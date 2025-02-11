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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.css.CssStyleSheet;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSCharsetRuleImpl;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.parser.InputSource;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.html.HTMLElement;
import org.w3c.dom.DOMException;

/**
 * A JavaScript object for {@code CSSStyleSheet}.
 *
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms535871.aspx">MSDN doc</a>
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Guy Burton
 * @author Frank Danek
 * @author Carsten Steul
 */
@JsxClass
public class CSSStyleSheet extends StyleSheet {

    private static final Log LOG = LogFactory.getLog(CSSStyleSheet.class);

    /** The parsed stylesheet which this host object wraps. */
    private CssStyleSheet styleSheet_;

    /** The collection of rules defined in this style sheet. */
    private CSSRuleList cssRules_;
    private List<Integer> cssRulesIndexFix_;

    /**
     * Creates a new empty stylesheet.
     */
    public CSSStyleSheet() {
        super(null);
        styleSheet_ = new CssStyleSheet(null, (InputSource) null, null);
    }

    /**
     * Creates a new empty stylesheet.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
        styleSheet_ = new CssStyleSheet(null, (InputSource) null, null);
    }

    /**
     * Creates a new stylesheet representing the CSS stylesheet for the specified input source.
     * @param element the owning node
     * @param source the input source which contains the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CSSStyleSheet(final HTMLElement element, final InputSource source, final String uri) {
        super(element);

        setParentScope(element.getWindow());
        setPrototype(getPrototype(CSSStyleSheet.class));

        styleSheet_ = new CssStyleSheet(element.getDomNodeOrDie(), source, uri);
    }

    /**
     * Creates a new stylesheet representing the CSS stylesheet for the specified input source.
     * @param element the owning node
     * @param styleSheet the source which contains the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CSSStyleSheet(final HTMLElement element, final String styleSheet, final String uri) {
        super(element);

        final Window win = element.getWindow();

        CssStyleSheet css = null;
        try (InputSource source = new InputSource(new StringReader(styleSheet))) {
            css = new CssStyleSheet(element.getDomNodeOrDie(), source, uri);
        }
        catch (final IOException e) {
            LOG.error(e.getMessage(), e);
        }

        setParentScope(win);
        setPrototype(getPrototype(CSSStyleSheet.class));

        styleSheet_ = css;
    }

    /**
     * Creates a new stylesheet representing the specified CSS stylesheet.
     * @param element the owning node
     * @param parentScope the parent scope
     * @param cssStyleSheet the CSS stylesheet which this stylesheet host object represents
     */
    public CSSStyleSheet(final HTMLElement element, final Scriptable parentScope,
            final CssStyleSheet cssStyleSheet) {
        super(element);

        setParentScope(parentScope);
        setPrototype(getPrototype(CSSStyleSheet.class));
        styleSheet_ = cssStyleSheet;
    }

    /**
     * Returns the wrapped stylesheet.
     * @return the wrapped stylesheet
     */
    public CssStyleSheet getCssStyleSheet() {
        return styleSheet_;
    }

    /**
     * Retrieves the collection of rules defined in this style sheet.
     * @return the collection of rules defined in this style sheet
     */
    @JsxGetter
    public CSSRuleList getRules() {
        return getCssRules();
    }

    /**
     * Returns the collection of rules defined in this style sheet.
     * @return the collection of rules defined in this style sheet
     */
    @JsxGetter
    public CSSRuleList getCssRules() {
        initCssRules();
        return cssRules_;
    }

    /**
     * Inserts a new rule.
     * @param rule the CSS rule
     * @param position the position at which to insert the rule
     * @see <a href="http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-CSSStyleSheet">DOM level 2</a>
     * @return the position of the inserted rule
     */
    @JsxFunction
    public int insertRule(final String rule, final int position) {
        try {
            initCssRules();
            getCssStyleSheet().getWrappedSheet().insertRule(rule, fixIndex(position));
            refreshCssRules();
            return position;
        }
        catch (final DOMException e) {
            // in case of error try with an empty rule
            final int pos = rule.indexOf('{');
            if (pos > -1) {
                final String newRule = rule.substring(0, pos) + "{}";
                try {
                    getCssStyleSheet().getWrappedSheet().insertRule(newRule, fixIndex(position));
                    refreshCssRules();
                    return position;
                }
                catch (final DOMException ex) {
                    throw JavaScriptEngine.asJavaScriptException(getWindow(), ex.getMessage(), ex.code);
                }
            }
            throw JavaScriptEngine.asJavaScriptException(getWindow(), e.getMessage(), e.code);
        }
    }

    private void refreshCssRules() {
        if (cssRules_ == null) {
            return;
        }

        cssRules_.clearRules();
        cssRulesIndexFix_.clear();

        final CSSRuleListImpl ruleList = getCssStyleSheet().getWrappedSheet().getCssRules();
        final List<AbstractCSSRuleImpl> rules = ruleList.getRules();
        int pos = 0;
        for (final AbstractCSSRuleImpl rule : rules) {
            if (rule instanceof CSSCharsetRuleImpl) {
                cssRulesIndexFix_.add(pos);
                continue;
            }

            final CSSRule cssRule = CSSRule.create(this, rule);
            if (null == cssRule) {
                cssRulesIndexFix_.add(pos);
            }
            else {
                cssRules_.addRule(cssRule);
            }
            pos++;
        }

        // reset our index also
        getCssStyleSheet().getWrappedSheet().resetRuleIndex();
    }

    private int fixIndex(int index) {
        for (final int fix : cssRulesIndexFix_) {
            if (fix > index) {
                return index;
            }
            index++;
        }
        return index;
    }

    /**
     * Deletes an existing rule.
     * @param position the position of the rule to be deleted
     * @see <a href="http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-CSSStyleSheet">DOM level 2</a>
     */
    @JsxFunction
    public void deleteRule(final int position) {
        try {
            initCssRules();
            getCssStyleSheet().getWrappedSheet().deleteRule(fixIndex(position));
            refreshCssRules();
        }
        catch (final DOMException e) {
            throw JavaScriptEngine.asJavaScriptException(getWindow(), e.getMessage(), e.code);
        }
    }

    /**
     * Adds a new rule.
     * @see <a href="http://msdn.microsoft.com/en-us/library/aa358796.aspx">MSDN</a>
     * @param selector the selector name
     * @param rule the rule
     * @return always return -1 as of MSDN documentation
     */
    @JsxFunction
    public int addRule(final String selector, final String rule) {
        String completeRule = selector + " {" + rule + "}";
        try {
            initCssRules();
            getCssStyleSheet().getWrappedSheet().insertRule(completeRule,
                    getCssStyleSheet().getWrappedSheet().getCssRules().getLength());
            refreshCssRules();
        }
        catch (final DOMException e) {
            // in case of error try with an empty rule
            completeRule = selector + " {}";
            try {
                getCssStyleSheet().getWrappedSheet().insertRule(completeRule,
                        getCssStyleSheet().getWrappedSheet().getCssRules().getLength());
                refreshCssRules();
            }
            catch (final DOMException ex) {
                throw JavaScriptEngine.asJavaScriptException(getWindow(), ex.getMessage(), ex.code);
            }
        }
        return -1;
    }

    /**
     * Deletes an existing rule.
     * @param position the position of the rule to be deleted
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms531195(v=VS.85).aspx">MSDN</a>
     */
    @JsxFunction
    public void removeRule(final int position) {
        try {
            initCssRules();
            getCssStyleSheet().getWrappedSheet().deleteRule(fixIndex(position));
            refreshCssRules();
        }
        catch (final DOMException e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Returns this stylesheet's URI (used to resolved contained @import rules).
     * For inline styles this is the page uri.
     * @return this stylesheet's URI (used to resolved contained @import rules)
     */
    @Override
    public String getUri() {
        return getCssStyleSheet().getUri();
    }

    private void initCssRules() {
        if (cssRules_ == null) {
            cssRules_ = new CSSRuleList(this);
            cssRulesIndexFix_ = new ArrayList<>();
            refreshCssRules();
        }
    }
}
