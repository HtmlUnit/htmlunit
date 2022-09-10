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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_PSEUDO_SELECTOR_MS_PLACEHHOLDER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_PSEUDO_SELECTOR_PLACEHOLDER_SHOWN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTOR_CSS3_PSEUDO_REQUIRE_ATTACHED_NODE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STYLESHEET_ADD_RULE_RETURNS_POS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STYLESHEET_HREF_EMPTY_IS_NULL;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSCharsetRuleImpl;
import com.gargoylesoftware.css.dom.CSSRuleListImpl;
import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;
import com.gargoylesoftware.css.parser.condition.Condition;
import com.gargoylesoftware.css.parser.javacc.CSS3Parser;
import com.gargoylesoftware.css.parser.selector.ChildSelector;
import com.gargoylesoftware.css.parser.selector.DescendantSelector;
import com.gargoylesoftware.css.parser.selector.DirectAdjacentSelector;
import com.gargoylesoftware.css.parser.selector.ElementSelector;
import com.gargoylesoftware.css.parser.selector.GeneralAdjacentSelector;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.SelectorList;
import com.gargoylesoftware.css.parser.selector.SelectorListImpl;
import com.gargoylesoftware.htmlunit.css.CssStyleSheet;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

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

    private static final Pattern NTH_NUMERIC = Pattern.compile("\\d+");
    private static final Pattern NTH_COMPLEX = Pattern.compile("[+-]?\\d*n\\w*([+-]\\w\\d*)?");

    /** The parsed stylesheet which this host object wraps. */
    private final CssStyleSheet styleSheet_;

    /** The HTML element which owns this stylesheet. */
    private final HTMLElement ownerNode_;

    /** The collection of rules defined in this style sheet. */
    private com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList cssRules_;
    private List<Integer> cssRulesIndexFix_;

    /**
     * Creates a new empty stylesheet.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public CSSStyleSheet() {
        styleSheet_ = new CssStyleSheet(null, (InputSource) null, null);
        ownerNode_ = null;
    }

    /**
     * Creates a new stylesheet representing the CSS stylesheet for the specified input source.
     * @param element the owning node
     * @param source the input source which contains the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CSSStyleSheet(final HTMLElement element, final InputSource source, final String uri) {
        setParentScope(element.getWindow());
        setPrototype(getPrototype(CSSStyleSheet.class));

        styleSheet_ = new CssStyleSheet(element.getDomNodeOrDie(), source, uri);
        ownerNode_ = element;
    }

    /**
     * Creates a new stylesheet representing the CSS stylesheet for the specified input source.
     * @param element the owning node
     * @param styleSheet the source which contains the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CSSStyleSheet(final HTMLElement element, final String styleSheet, final String uri) {
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
        ownerNode_ = element;
    }

    /**
     * Creates a new stylesheet representing the specified CSS stylesheet.
     * @param element the owning node
     * @param parentScope the parent scope
     * @param cssStyleSheet the CSS stylesheet which this stylesheet host object represents
     */
    public CSSStyleSheet(final HTMLElement element, final Scriptable parentScope,
            final CssStyleSheet cssStyleSheet) {
        setParentScope(parentScope);
        setPrototype(getPrototype(CSSStyleSheet.class));
        styleSheet_ = cssStyleSheet;
        ownerNode_ = element;
    }

    /**
     * Returns the wrapped stylesheet.
     * @return the wrapped stylesheet
     */
    public CssStyleSheet getCssStyleSheet() {
        return styleSheet_;
    }

    /**
     * Parses the selectors at the specified input source. If anything at all goes wrong, this
     * method returns an empty selector list.
     *
     * @param source the source from which to retrieve the selectors to be parsed
     * @return the selectors parsed from the specified input source
     */
    public SelectorList parseSelectors(final String source) {
        SelectorList selectors;
        try {
            final CSSErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
            final CSSOMParser parser = new CSSOMParser(new CSS3Parser());
            parser.setErrorHandler(errorHandler);
            selectors = parser.parseSelectors(source);
            // in case of error parseSelectors returns null
            if (null == selectors) {
                selectors = new SelectorListImpl();
            }
        }
        catch (final Throwable t) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Error parsing CSS selectors from '" + source + "': " + t.getMessage(), t);
            }
            selectors = new SelectorListImpl();
        }
        return selectors;
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

    /**
     * Returns the URL of the stylesheet.
     * @return the URL of the stylesheet
     */
    @JsxGetter
    public String getHref() {
        if (ownerNode_ != null) {
            final DomNode node = ownerNode_.getDomNodeOrDie();
            if (node instanceof HtmlStyle) {
                return null;
            }
            if (node instanceof HtmlLink) {
                // <link rel="stylesheet" type="text/css" href="..." />
                final HtmlLink link = (HtmlLink) node;
                final String href = link.getHrefAttribute();
                if ("".equals(href) && getBrowserVersion().hasFeature(STYLESHEET_HREF_EMPTY_IS_NULL)) {
                    return null;
                }
                // Expand relative URLs.
                try {
                    final HtmlPage page = (HtmlPage) link.getPage();
                    final URL url = page.getFullyQualifiedUrl(href);
                    return url.toExternalForm();
                }
                catch (final MalformedURLException e) {
                    // Log the error and fall through to the return values below.
                    LOG.warn(e.getMessage(), e);
                }
            }
        }

        return getUri();
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
                    throw Context.throwAsScriptRuntimeEx(ex);
                }
            }
            throw Context.throwAsScriptRuntimeEx(e);
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

            final com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule cssRule
                        = com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule.create(this, rule);
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
            throw Context.throwAsScriptRuntimeEx(e);
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
                throw Context.throwAsScriptRuntimeEx(ex);
            }
        }
        if (getBrowserVersion().hasFeature(STYLESHEET_ADD_RULE_RETURNS_POS)) {
            return getCssStyleSheet().getWrappedSheet().getCssRules().getLength() - 1;
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
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Returns this stylesheet's URI (used to resolved contained @import rules).
     * For inline styles this is the page uri.
     * @return this stylesheet's URI (used to resolved contained @import rules)
     */
    public String getUri() {
        return getCssStyleSheet().getUri();
    }

    /**
     * Validates the list of selectors.
     * @param selectorList the selectors
     * @param documentMode see {@link HTMLDocument#getDocumentMode()}
     * @param domNode the dom node the query should work on
     * @throws CSSException if a selector is invalid
     */
    public static void validateSelectors(final SelectorList selectorList, final int documentMode,
                final DomNode domNode) throws CSSException {
        for (final Selector selector : selectorList) {
            if (!isValidSelector(selector, documentMode, domNode)) {
                throw new CSSException("Invalid selector: " + selector);
            }
        }
    }

    /**
     * @param documentMode see {@link HTMLDocument#getDocumentMode()}
     */
    private static boolean isValidSelector(final Selector selector, final int documentMode, final DomNode domNode) {
        switch (selector.getSelectorType()) {
            case ELEMENT_NODE_SELECTOR:
                final List<Condition> conditions = ((ElementSelector) selector).getConditions();
                if (conditions != null) {
                    for (final Condition condition : conditions) {
                        if (!isValidCondition(condition, documentMode, domNode)) {
                            return false;
                        }
                    }
                }
                return true;
            case DESCENDANT_SELECTOR:
                final DescendantSelector ds = (DescendantSelector) selector;
                return isValidSelector(ds.getAncestorSelector(), documentMode, domNode)
                        && isValidSelector(ds.getSimpleSelector(), documentMode, domNode);
            case CHILD_SELECTOR:
                final ChildSelector cs = (ChildSelector) selector;
                return isValidSelector(cs.getAncestorSelector(), documentMode, domNode)
                        && isValidSelector(cs.getSimpleSelector(), documentMode, domNode);
            case DIRECT_ADJACENT_SELECTOR:
                final DirectAdjacentSelector das = (DirectAdjacentSelector) selector;
                return isValidSelector(das.getSelector(), documentMode, domNode)
                        && isValidSelector(das.getSimpleSelector(), documentMode, domNode);
            case GENERAL_ADJACENT_SELECTOR:
                final GeneralAdjacentSelector gas = (GeneralAdjacentSelector) selector;
                return isValidSelector(gas.getSelector(), documentMode, domNode)
                        && isValidSelector(gas.getSimpleSelector(), documentMode, domNode);
            default:
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Unhandled CSS selector type '"
                                + selector.getSelectorType() + "'. Accepting it silently.");
                }
                return true; // at least in a first time to break less stuff
        }
    }

    /**
     * @param documentMode see {@link HTMLDocument#getDocumentMode()}
     */
    private static boolean isValidCondition(final Condition condition, final int documentMode, final DomNode domNode) {
        switch (condition.getConditionType()) {
            case ATTRIBUTE_CONDITION:
            case ID_CONDITION:
            case LANG_CONDITION:
            case ONE_OF_ATTRIBUTE_CONDITION:
            case BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
            case CLASS_CONDITION:
            case PREFIX_ATTRIBUTE_CONDITION:
            case SUBSTRING_ATTRIBUTE_CONDITION:
            case SUFFIX_ATTRIBUTE_CONDITION:
                return true;
            case PSEUDO_CLASS_CONDITION:
                String value = condition.getValue();
                if (value.endsWith(")")) {
                    if (value.endsWith("()")) {
                        return false;
                    }
                    value = value.substring(0, value.indexOf('(') + 1) + ')';
                }
                if (documentMode < 9) {
                    return CssStyleSheet.CSS2_PSEUDO_CLASSES.contains(value);
                }

                if (!CssStyleSheet.CSS2_PSEUDO_CLASSES.contains(value)
                        && domNode.hasFeature(QUERYSELECTOR_CSS3_PSEUDO_REQUIRE_ATTACHED_NODE)
                        && !domNode.isAttachedToPage()
                        && !domNode.hasChildNodes()) {
                    throw new CSSException("Syntax Error");
                }

                if ("nth-child()".equals(value)) {
                    final String arg = StringUtils.substringBetween(condition.getValue(), "(", ")").trim();
                    return "even".equalsIgnoreCase(arg) || "odd".equalsIgnoreCase(arg)
                            || NTH_NUMERIC.matcher(arg).matches()
                            || NTH_COMPLEX.matcher(arg).matches();
                }

                if ("placeholder-shown".equals(value)) {
                    return domNode.hasFeature(CSS_PSEUDO_SELECTOR_PLACEHOLDER_SHOWN);
                }

                if ("-ms-input-placeholder".equals(value)) {
                    return domNode.hasFeature(CSS_PSEUDO_SELECTOR_MS_PLACEHHOLDER);
                }

                return CssStyleSheet.CSS4_PSEUDO_CLASSES.contains(value);
            default:
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Unhandled CSS condition type '"
                                + condition.getConditionType() + "'. Accepting it silently.");
                }
                return true;
        }
    }

    private void initCssRules() {
        if (cssRules_ == null) {
            cssRules_ = new com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList(this);
            cssRulesIndexFix_ = new ArrayList<>();
            refreshCssRules();
        }
    }
}
