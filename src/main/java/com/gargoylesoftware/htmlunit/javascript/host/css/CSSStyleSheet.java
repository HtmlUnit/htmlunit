/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STYLESHEET_ADD_RULE_RETURNS_POS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STYLESHEET_HREF_EMPTY_IS_NULL;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSCharsetRuleImpl;
import com.gargoylesoftware.css.dom.CSSRuleListImpl;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.css.CssStyleSheet;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

import net.sourceforge.htmlunit.corejs.javascript.Context;

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
    private final CssStyleSheet cssStyleSheet;
    private final HTMLElement ownerNode_;

    /** The collection of rules defined in this style sheet. */
    private com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList cssRules_;
    private List<Integer> cssRulesIndexFix_;

    /**
     * Creates a new empty stylesheet.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public CSSStyleSheet() {
        cssStyleSheet = new CssStyleSheet();
        ownerNode_ = null;
    }

    public CSSStyleSheet(CssStyleSheet cssStyleSheet) {
        this(cssStyleSheet.getOwnerDomNode().getScriptableObject(), cssStyleSheet);
    }

    public CSSStyleSheet(HTMLElement element, CssStyleSheet cssStyleSheet) {
        final Window win = element.getWindow();

        setParentScope(win);
        setPrototype(getPrototype(CSSStyleSheet.class));

        this.cssStyleSheet = cssStyleSheet;
        this.ownerNode_ = element;
    }

    /**
     * Returns the owner node.
     * @return the owner node
     */
    @JsxGetter
    public HTMLElement getOwnerNode() {
        return ownerNode_;
    }

    /**
     * Returns the owner element, same as {@link #getOwnerNode()}.
     * @return the owner element
     */
    @JsxGetter(IE)
    public HTMLElement getOwningElement() {
        return ownerNode_;
    }

    /**
     * Retrieves the collection of rules defined in this style sheet.
     * @return the collection of rules defined in this style sheet
     */
    @JsxGetter({CHROME, EDGE, IE})
    public com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList getRules() {
        return getCssRules();
    }

    /**
     * Returns the collection of rules defined in this style sheet.
     * @return the collection of rules defined in this style sheet
     */
    @JsxGetter
    public com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList getCssRules() {
        initCssRules();
        return cssRules_;
    }

    private void initCssRules() {
        if (cssRules_ == null) {
            cssRules_ = new com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList(this);
            cssRulesIndexFix_ = new ArrayList<>();
            refreshCssRules();
        }
    }

    private void refreshCssRules() {
        if (cssRules_ == null) {
            return;
        }

        cssRules_.clearRules();
        cssRulesIndexFix_.clear();

        final CSSRuleListImpl ruleList = cssStyleSheet.getWrappedSheet().getCssRules();
        final List<AbstractCSSRuleImpl> rules = ruleList.getRules();
        int pos = 0;
        for (final AbstractCSSRuleImpl rule : rules) {
            if (rule instanceof CSSCharsetRuleImpl) {
                cssRulesIndexFix_.add(pos);
                continue;
            }

            final com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule cssRule
                = com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule.create(this, rule);
            if (null == cssRule) {
                cssRulesIndexFix_.add(pos);
            } else {
                cssRules_.addRule(cssRule);
            }
            pos++;
        }

        // reset our index also
        cssStyleSheet.getWrappedSheet().resetRuleIndex();
    }

    /**
     * Inserts a new rule.
     * @param rule the CSS rule
     * @param position the position at which to insert the rule
     * @return the position of the inserted rule
     * @see <a href="http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-CSSStyleSheet">DOM level 2</a>
     */
    @JsxFunction
    public int insertRule(final String rule, final int position) {
        try {
            initCssRules();
            cssStyleSheet.getWrappedSheet().insertRule(rule, fixIndex(position));
            refreshCssRules();
            return position;
        } catch (final DOMException e) {
            // in case of error try with an empty rule
            final int pos = rule.indexOf('{');
            if (pos > -1) {
                final String newRule = rule.substring(0, pos) + "{}";
                try {
                    cssStyleSheet.getWrappedSheet().insertRule(newRule, fixIndex(position));
                    refreshCssRules();
                    return position;
                } catch (final DOMException ex) {
                    throw Context.throwAsScriptRuntimeEx(ex);
                }
            }
            throw Context.throwAsScriptRuntimeEx(e);
        }
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
            cssStyleSheet.getWrappedSheet().deleteRule(fixIndex(position));
            refreshCssRules();
        } catch (final DOMException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Adds a new rule.
     * @param selector the selector name
     * @param rule the rule
     * @return always return -1 as of MSDN documentation
     * @see <a href="http://msdn.microsoft.com/en-us/library/aa358796.aspx">MSDN</a>
     */
    @JsxFunction
    public int addRule(final String selector, final String rule) {
        String completeRule = selector + " {" + rule + "}";
        try {
            initCssRules();
            cssStyleSheet.getWrappedSheet()
                .insertRule(completeRule, cssStyleSheet.getWrappedSheet().getCssRules().getLength());
            refreshCssRules();
        } catch (final DOMException e) {
            // in case of error try with an empty rule
            completeRule = selector + " {}";
            try {
                cssStyleSheet.getWrappedSheet()
                    .insertRule(completeRule, cssStyleSheet.getWrappedSheet().getCssRules().getLength());
                refreshCssRules();
            } catch (final DOMException ex) {
                throw Context.throwAsScriptRuntimeEx(ex);
            }
        }
        if (getBrowserVersion().hasFeature(STYLESHEET_ADD_RULE_RETURNS_POS)) {
            return cssStyleSheet.getWrappedSheet().getCssRules().getLength() - 1;
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
            cssStyleSheet.getWrappedSheet().deleteRule(fixIndex(position));
            refreshCssRules();
        } catch (final DOMException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    public String getUri() {
        return cssStyleSheet.getUri();
    }

    /**
     * Returns the URL of the stylesheet.
     * @return the URL of the stylesheet
     */
    @JsxGetter
    public String getHref() {
        final BrowserVersion version = getBrowserVersion();

        if (ownerNode_ != null) {
            final DomNode node = cssStyleSheet.getOwnerDomNode();
            if (node instanceof HtmlLink) {
                // <link rel="stylesheet" type="text/css" href="..." />
                final HtmlLink link = (HtmlLink) node;
                final HtmlPage page = (HtmlPage) link.getPage();
                final String href = link.getHrefAttribute();
                if ("".equals(href) && version.hasFeature(STYLESHEET_HREF_EMPTY_IS_NULL)) {
                    return null;
                }
                // Expand relative URLs.
                try {
                    final URL url = page.getFullyQualifiedUrl(href);
                    return url.toExternalForm();
                }
                catch (final MalformedURLException e) {
                    // Log the error and fall through to the return values below.
                    LOG.warn(e.getMessage(), e);
                }
            }
        }

        return null;
    }

    public boolean isEnabled() {
        return cssStyleSheet.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        cssStyleSheet.setEnabled(enabled);
    }
}
