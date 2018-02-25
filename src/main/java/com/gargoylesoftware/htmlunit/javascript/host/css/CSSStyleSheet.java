/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLLINK_CHECK_TYPE_FOR_STYLESHEET;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NO_TARGET;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTOR_CSS3_PSEUDO_REQUIRE_ATTACHED_NODE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STYLESHEET_ADD_RULE_RETURNS_POS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STYLESHEET_HREF_EMPTY_IS_NULL;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.stylesheets.MediaList;

import com.gargoylesoftware.css.dom.CSSImportRuleImpl;
import com.gargoylesoftware.css.dom.CSSMediaRuleImpl;
import com.gargoylesoftware.css.dom.CSSRuleListImpl;
import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl;
import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.CSS3Parser;
import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.css.parser.InputSource;
import com.gargoylesoftware.css.parser.condition.AttributeCondition;
import com.gargoylesoftware.css.parser.condition.BeginHyphenAttributeCondition;
import com.gargoylesoftware.css.parser.condition.ClassCondition;
import com.gargoylesoftware.css.parser.condition.Condition;
import com.gargoylesoftware.css.parser.condition.IdCondition;
import com.gargoylesoftware.css.parser.condition.LangCondition;
import com.gargoylesoftware.css.parser.condition.OneOfAttributeCondition;
import com.gargoylesoftware.css.parser.condition.PrefixAttributeCondition;
import com.gargoylesoftware.css.parser.condition.PseudoClassCondition;
import com.gargoylesoftware.css.parser.condition.SubstringAttributeCondition;
import com.gargoylesoftware.css.parser.condition.SuffixAttributeCondition;
import com.gargoylesoftware.css.parser.media.MediaQuery;
import com.gargoylesoftware.css.parser.selector.ChildSelector;
import com.gargoylesoftware.css.parser.selector.DescendantSelector;
import com.gargoylesoftware.css.parser.selector.DirectAdjacentSelector;
import com.gargoylesoftware.css.parser.selector.ElementSelector;
import com.gargoylesoftware.css.parser.selector.GeneralAdjacentSelector;
import com.gargoylesoftware.css.parser.selector.PseudoElementSelector;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.Selector.SelectorType;
import com.gargoylesoftware.css.parser.selector.SelectorList;
import com.gargoylesoftware.css.parser.selector.SelectorListImpl;
import com.gargoylesoftware.css.parser.selector.SimpleSelector;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DisabledElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

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
    private static final Pattern NTH_NUMERIC = Pattern.compile("\\d+");
    private static final Pattern NTH_COMPLEX = Pattern.compile("[+-]?\\d*n\\w*([+-]\\w\\d*)?");
    private static final Pattern UNESCAPE_SELECTOR = Pattern.compile("\\\\([\\[\\]\\.:])");

    /** The parsed stylesheet which this host object wraps. */
    private final org.w3c.dom.css.CSSStyleSheet wrapped_;

    /** The HTML element which owns this stylesheet. */
    private final HTMLElement ownerNode_;

    /** The collection of rules defined in this style sheet. */
    private com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList cssRules_;
    private List<Integer> cssRulesIndexFix_;

    /** The CSS import rules and their corresponding stylesheets. */
    private final Map<CSSImportRule, CSSStyleSheet> imports_ = new HashMap<>();

    /** cache parsed media strings */
    private static final transient Map<String, MediaList> media_ = new HashMap<>();

    /** This stylesheet's URI (used to resolved contained @import rules). */
    private String uri_;

    private boolean enabled_ = true;

    private static final Set<String> CSS2_PSEUDO_CLASSES = new HashSet<>(Arrays.asList(
            "link", "visited", "hover", "active",
            "focus", "lang", "first-child"));

    private static final Set<String> CSS3_PSEUDO_CLASSES = new HashSet<>(Arrays.asList(
            "checked", "disabled", "enabled", "indeterminated", "root", "target", "not()",
            "nth-child()", "nth-last-child()", "nth-of-type()", "nth-last-of-type()",
            "last-child", "first-of-type", "last-of-type", "only-child", "only-of-type", "empty",
            "optional", "required"));

    static {
        CSS3_PSEUDO_CLASSES.addAll(CSS2_PSEUDO_CLASSES);
    }

    /**
     * Creates a new empty stylesheet.
     */
    @JsxConstructor({CHROME, FF, EDGE})
    public CSSStyleSheet() {
        wrapped_ = new CSSStyleSheetImpl();
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
        if (source != null) {
            source.setURI(uri);
        }
        wrapped_ = parseCSS(source);
        uri_ = uri;
        ownerNode_ = element;
    }

    /**
     * Creates a new stylesheet representing the specified CSS stylesheet.
     * @param element the owning node
     * @param wrapped the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CSSStyleSheet(final HTMLElement element, final org.w3c.dom.css.CSSStyleSheet wrapped, final String uri) {
        setParentScope(element.getWindow());
        setPrototype(getPrototype(CSSStyleSheet.class));
        wrapped_ = wrapped;
        uri_ = uri;
        ownerNode_ = element;
    }

    /**
     * Returns the wrapped stylesheet.
     * @return the wrapped stylesheet
     */
    public org.w3c.dom.css.CSSStyleSheet getWrappedSheet() {
        return wrapped_;
    }

    /**
     * Modifies the specified style object by adding any style rules which apply to the specified
     * element.
     *
     * @param style the style to modify
     * @param element the element to which style rules must apply in order for them to be added to
     *        the specified style
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null})
     */
    public void modifyIfNecessary(final ComputedCSSStyleDeclaration style, final Element element,
            final String pseudoElement) {
        final CSSRuleList rules = getWrappedSheet().getCssRules();
        modifyIfNecessary(style, element, pseudoElement, rules, new HashSet<String>());
    }

    private void modifyIfNecessary(final ComputedCSSStyleDeclaration style, final Element element,
            final String pseudoElement, final CSSRuleList rules, final Set<String> alreadyProcessing) {
        if (rules == null) {
            return;
        }

        final BrowserVersion browser = getBrowserVersion();
        final DomElement e = element.getDomNodeOrDie();
        final int rulesLength = rules.getLength();
        for (int i = 0; i < rulesLength; i++) {
            final CSSRule rule = rules.item(i);

            final short ruleType = rule.getType();
            if (CSSRule.STYLE_RULE == ruleType) {
                final CSSStyleRuleImpl styleRule = (CSSStyleRuleImpl) rule;
                final SelectorList selectors = styleRule.getSelectors();
                for (Selector selector : selectors) {
                    final boolean selected = selects(browser, selector, e, pseudoElement, false);
                    if (selected) {
                        final org.w3c.dom.css.CSSStyleDeclaration dec = styleRule.getStyle();
                        style.applyStyleFromSelector(dec, selector);
                    }
                }
            }
            else if (CSSRule.IMPORT_RULE == ruleType) {
                final CSSImportRuleImpl importRule = (CSSImportRuleImpl) rule;
                final MediaList mediaList = importRule.getMedia();
                if (isActive(this, mediaList)) {
                    CSSStyleSheet sheet = imports_.get(importRule);
                    if (sheet == null) {
                        // TODO: surely wrong: in which case is it null and why?
                        final String uri = (uri_ != null) ? uri_ : e.getPage().getUrl().toExternalForm();
                        final String href = importRule.getHref();
                        final String url = UrlUtils.resolveUrl(uri, href);
                        sheet = loadStylesheet(ownerNode_, null, url);
                        imports_.put(importRule, sheet);
                    }

                    if (!alreadyProcessing.contains(sheet.getUri())) {
                        final CSSRuleList sheetRules = sheet.getWrappedSheet().getCssRules();
                        alreadyProcessing.add(getUri());
                        sheet.modifyIfNecessary(style, element, pseudoElement, sheetRules, alreadyProcessing);
                    }
                }
            }
            else if (CSSRule.MEDIA_RULE == ruleType) {
                final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) rule;
                final MediaList mediaList = mediaRule.getMedia();
                if (isActive(this, mediaList)) {
                    final CSSRuleList internalRules = mediaRule.getCssRules();
                    modifyIfNecessary(style, element, pseudoElement, internalRules, alreadyProcessing);
                }
            }
        }
    }

    /**
     * Loads the stylesheet at the specified link or href.
     * @param element the parent DOM element
     * @param link the stylesheet's link (may be {@code null} if a <tt>url</tt> is specified)
     * @param url the stylesheet's url (may be {@code null} if a <tt>link</tt> is specified)
     * @return the loaded stylesheet
     */
    public static CSSStyleSheet loadStylesheet(final HTMLElement element, final HtmlLink link, final String url) {
        CSSStyleSheet sheet;
        final HtmlPage page = (HtmlPage) element.getDomNodeOrDie().getPage();
        String uri = page.getUrl().toExternalForm(); // fallback uri for exceptions
        try {
            // Retrieve the associated content and respect client settings regarding failing HTTP status codes.
            final WebRequest request;
            final WebResponse response;
            final WebClient client = page.getWebClient();
            if (link != null) {
                // Use link.
                request = link.getWebRequest();

                if (element.getBrowserVersion().hasFeature(HTMLLINK_CHECK_TYPE_FOR_STYLESHEET)) {
                    final String type = link.getTypeAttribute();
                    if (StringUtils.isNotBlank(type) && !"text/css".equals(type)) {
                        final InputSource source = new InputSource(new StringReader(""));
                        return new CSSStyleSheet(element, source, uri);
                    }
                }

                // our cache is a bit strange;
                // loadWebResponse check the cache for the web response
                // AND also fixes the request url for the following cache lookups
                response = link.getWebResponse(true, request);
            }
            else {
                // Use href.
                final String accept = client.getBrowserVersion().getCssAcceptHeader();
                request = new WebRequest(new URL(url), accept);
                final String referer = page.getUrl().toExternalForm();
                request.setAdditionalHeader(HttpHeader.REFERER, referer);

                // our cache is a bit strange;
                // loadWebResponse check the cache for the web response
                // AND also fixes the request url for the following cache lookups
                response = client.loadWebResponse(request);
            }

            // now we can look into the cache with the fixed request for
            // a cached script
            final Cache cache = client.getCache();
            final Object fromCache = cache.getCachedObject(request);
            if (fromCache instanceof org.w3c.dom.css.CSSStyleSheet) {
                uri = request.getUrl().toExternalForm();
                sheet = new CSSStyleSheet(element, (org.w3c.dom.css.CSSStyleSheet) fromCache, uri);
            }
            else {
                uri = response.getWebRequest().getUrl().toExternalForm();
                client.printContentIfNecessary(response);
                client.throwFailingHttpStatusCodeExceptionIfNecessary(response);
                // CSS content must have downloaded OK; go ahead and build the corresponding stylesheet.

                final InputSource source;
                final String contentType = response.getContentType();
                if (StringUtils.isEmpty(contentType) || "text/css".equals(contentType)) {
                    source = new InputSource(response.getContentAsStream(), response.getContentCharset().name());
                }
                else {
                    source = new InputSource(new StringReader(""));
                }
                sheet = new CSSStyleSheet(element, source, uri);

                // cache the style sheet
                if (!cache.cacheIfPossible(request, response, sheet.getWrappedSheet())) {
                    response.cleanUp();
                }
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            // Got a 404 response or something like that; behave nicely.
            LOG.error("Exception loading " + uri, e);
            final InputSource source = new InputSource(new StringReader(""));
            sheet = new CSSStyleSheet(element, source, uri);
        }
        catch (final IOException e) {
            // Got a basic IO error; behave nicely.
            LOG.error("IOException loading " + uri, e);
            final InputSource source = new InputSource(new StringReader(""));
            sheet = new CSSStyleSheet(element, source, uri);
        }
        catch (final RuntimeException e) {
            // Got something unexpected; we can throw an exception in this case.
            LOG.error("RuntimeException loading " + uri, e);
            throw Context.reportRuntimeError("Exception: " + e);
        }
        catch (final Exception e) {
            // Got something unexpected; we can throw an exception in this case.
            LOG.error("Exception loading " + uri, e);
            throw Context.reportRuntimeError("Exception: " + e);
        }
        return sheet;
    }

    /**
     * Returns {@code true} if the specified selector selects the specified element.
     *
     * @param browserVersion the browser version
     * @param selector the selector to test
     * @param element the element to test
     * @param pseudoElement the pseudo element to match, (can be {@code null})
     * @param fromQuerySelectorAll whether this is called from {@link DomNode#querySelectorAll(String)}
     * @return {@code true} if it does apply, {@code false} if it doesn't apply
     */
    public static boolean selects(final BrowserVersion browserVersion, final Selector selector,
            final DomElement element, final String pseudoElement, final boolean fromQuerySelectorAll) {
        switch (selector.getSelectorType()) {
            case CHILD_SELECTOR:
                final DomNode parentNode = element.getParentNode();
                if (parentNode == element.getPage()) {
                    return false;
                }
                if (!(parentNode instanceof HtmlElement)) {
                    return false; // for instance parent is a DocumentFragment
                }
                final ChildSelector cs = (ChildSelector) selector;
                return selects(browserVersion, cs.getSimpleSelector(), element, pseudoElement, fromQuerySelectorAll)
                    && selects(browserVersion, cs.getAncestorSelector(), (HtmlElement) parentNode,
                            pseudoElement, fromQuerySelectorAll);

            case DESCENDANT_SELECTOR:
                final DescendantSelector ds = (DescendantSelector) selector;
                final SimpleSelector simpleSelector = ds.getSimpleSelector();
                if (selects(browserVersion, simpleSelector, element, pseudoElement, fromQuerySelectorAll)) {
                    DomNode ancestor = element;
                    if (simpleSelector.getSelectorType() != SelectorType.PSEUDO_ELEMENT_SELECTOR) {
                        ancestor = ancestor.getParentNode();
                    }
                    final Selector dsAncestorSelector = ds.getAncestorSelector();
                    while (ancestor instanceof HtmlElement) {
                        if (selects(browserVersion, dsAncestorSelector, (HtmlElement) ancestor, pseudoElement,
                                fromQuerySelectorAll)) {
                            return true;
                        }
                        ancestor = ancestor.getParentNode();
                    }
                }
                return false;

            case ELEMENT_NODE_SELECTOR:
                final ElementSelector es = (ElementSelector) selector;
                final String name = es.getLocalName();
                if (name == null || name.equalsIgnoreCase(element.getLocalName())) {
                    final List<Condition> conditions = es.getConditions();
                    if (conditions != null) {
                        for (Condition condition : conditions) {
                            if (!selects(browserVersion, condition, element, fromQuerySelectorAll)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
                return false;
            case DIRECT_ADJACENT_SELECTOR:
                final DirectAdjacentSelector das = (DirectAdjacentSelector) selector;
                DomNode prev = element.getPreviousSibling();
                while (prev != null && !(prev instanceof HtmlElement)) {
                    prev = prev.getPreviousSibling();
                }
                return prev != null
                    && selects(browserVersion, das.getSelector(), (HtmlElement) prev,
                            pseudoElement, fromQuerySelectorAll)
                    && selects(browserVersion, das.getSiblingSelector(), element, pseudoElement, fromQuerySelectorAll);

            case GENERAL_ADJACENT_SELECTOR:
                final GeneralAdjacentSelector gas = (GeneralAdjacentSelector) selector;

                final SimpleSelector ssSiblingSelector = gas.getSiblingSelector();
                for (DomNode prev1 = element.getPreviousSibling(); prev1 != null; prev1 = prev1.getPreviousSibling()) {
                    if (prev1 instanceof HtmlElement
                        && selects(browserVersion, gas.getSelector(), (HtmlElement) prev1,
                                pseudoElement, fromQuerySelectorAll)
                        && selects(browserVersion, ssSiblingSelector, element,
                                pseudoElement, fromQuerySelectorAll)) {
                        return true;
                    }
                }
                return false;
            case PSEUDO_ELEMENT_SELECTOR:
                if (pseudoElement != null && !pseudoElement.isEmpty() && pseudoElement.charAt(0) == ':') {
                    final String pseudoName = ((PseudoElementSelector) selector).getLocalName();
                    return pseudoName.equals(pseudoElement.substring(1));
                }
                return false;

            default:
                LOG.error("Unknown CSS selector type '" + selector.getSelectorType() + "'.");
                return false;
        }
    }

    /**
     * Returns {@code true} if the specified condition selects the specified element.
     *
     * @param browserVersion the browser version
     * @param condition the condition to test
     * @param element the element to test
     * @param fromQuerySelectorAll whether this is called from {@link DomNode#querySelectorAll(String)
     * @return {@code true} if it does apply, {@code false} if it doesn't apply
     */
    static boolean selects(final BrowserVersion browserVersion, final Condition condition, final DomElement element,
            final boolean fromQuerySelectorAll) {

        switch (condition.getConditionType()) {
            case ID_CONDITION:
                final IdCondition ac4 = (IdCondition) condition;
                return ac4.getValue().equals(element.getId());

            case CLASS_CONDITION:
                final ClassCondition ac3 = (ClassCondition) condition;
                String v3 = ac3.getValue();
                if (v3.indexOf('\\') > -1) {
                    v3 = UNESCAPE_SELECTOR.matcher(v3).replaceAll("$1");
                }
                final String a3 = element.getAttribute("class");
                return selectsWhitespaceSeparated(v3, a3);

            case ATTRIBUTE_CONDITION:
                final AttributeCondition ac1 = (AttributeCondition) condition;
                String value = ac1.getValue();
                if (value != null) {
                    if (value.indexOf('\\') > -1) {
                        value = UNESCAPE_SELECTOR.matcher(value).replaceAll("$1");
                    }
                    final String attrValue = element.getAttribute(ac1.getLocalName());
                    return ATTRIBUTE_NOT_DEFINED != attrValue && attrValue.equals(value);
                }
                return element.hasAttribute(ac1.getLocalName());

            case PREFIX_ATTRIBUTE_CONDITION:
                final PrefixAttributeCondition pac = (PrefixAttributeCondition) condition;
                final String prefixValue = pac.getValue();
                return !"".equals(prefixValue) && element.getAttribute(pac.getLocalName()).startsWith(prefixValue);

            case SUFFIX_ATTRIBUTE_CONDITION:
                final SuffixAttributeCondition sac = (SuffixAttributeCondition) condition;
                final String suffixValue = sac.getValue();
                return !"".equals(suffixValue) && element.getAttribute(sac.getLocalName()).endsWith(suffixValue);

            case SUBSTRING_ATTRIBUTE_CONDITION:
                final SubstringAttributeCondition suac = (SubstringAttributeCondition) condition;
                final String substringValue = suac.getValue();
                return !"".equals(substringValue) && element.getAttribute(suac.getLocalName()).contains(substringValue);

            case BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
                final BeginHyphenAttributeCondition bhac = (BeginHyphenAttributeCondition) condition;
                final String v = bhac.getValue();
                final String a = element.getAttribute(bhac.getLocalName());
                return selects(v, a, '-');

            case ONE_OF_ATTRIBUTE_CONDITION:
                final OneOfAttributeCondition ooac = (OneOfAttributeCondition) condition;
                final String v2 = ooac.getValue();
                final String a2 = element.getAttribute(ooac.getLocalName());
                return selects(v2, a2, ' ');

            case LANG_CONDITION:
                final String lcLang = ((LangCondition) condition).getLang();
                final int lcLangLength = lcLang.length();
                for (DomNode node = element; node instanceof HtmlElement; node = node.getParentNode()) {
                    final String nodeLang = ((HtmlElement) node).getAttribute("lang");
                    if (ATTRIBUTE_NOT_DEFINED != nodeLang) {
                        // "en", "en-GB" should be matched by "en" but not "english"
                        return nodeLang.startsWith(lcLang)
                            && (nodeLang.length() == lcLangLength || '-' == nodeLang.charAt(lcLangLength));
                    }
                }
                return false;

            case PSEUDO_CLASS_CONDITION:
                return selectsPseudoClass(browserVersion,
                        (PseudoClassCondition) condition, element, fromQuerySelectorAll);

            default:
                LOG.error("Unknown CSS condition type '" + condition.getConditionType() + "'.");
                return false;
        }
    }

    private static boolean selects(final String condition, final String attribute, final char separator) {
        // attribute.equals(condition)
        // || attribute.startsWith(condition + " ") || attriubte.endsWith(" " + condition)
        // || attribute.contains(" " + condition + " ");

        final int conditionLength = condition.length();
        if (conditionLength < 1) {
            return false;
        }

        final int attribLength = attribute.length();
        if (attribLength < conditionLength) {
            return false;
        }
        if (attribLength > conditionLength) {
            if (separator == attribute.charAt(conditionLength)
                    && attribute.startsWith(condition)) {
                return true;
            }
            if (separator == attribute.charAt(attribLength - conditionLength - 1)
                    && attribute.endsWith(condition)) {
                return true;
            }
            if (attribLength + 1 > conditionLength) {
                final StringBuilder tmp = new StringBuilder(conditionLength + 2);
                tmp.append(separator).append(condition).append(separator);
                return attribute.contains(tmp);
            }
            return false;
        }
        return attribute.equals(condition);
    }

    private static boolean selectsWhitespaceSeparated(final String condition, final String attribute) {
        final int conditionLength = condition.length();
        if (conditionLength < 1) {
            return false;
        }

        final int attribLength = attribute.length();
        if (attribLength < conditionLength) {
            return false;
        }

        int pos = attribute.indexOf(condition);
        while (pos != -1) {
            if (pos > 0 && !Character.isWhitespace(attribute.charAt(pos - 1))) {
                pos = attribute.indexOf(condition, pos + 1);
            }
            else {
                final int lastPos = pos + condition.length();
                if (lastPos >= attribLength || Character.isWhitespace(attribute.charAt(lastPos))) {
                    return true;
                }
                pos = attribute.indexOf(condition, pos + 1);
            }
        }

        return false;
    }

    private static boolean selectsPseudoClass(final BrowserVersion browserVersion,
            final PseudoClassCondition condition, final DomElement element, final boolean fromQuerySelectorAll) {
        if (browserVersion.hasFeature(QUERYSELECTORALL_NOT_IN_QUIRKS)) {
            final Object sobj = element.getPage().getScriptableObject();
            if (sobj instanceof HTMLDocument && ((HTMLDocument) sobj).getDocumentMode() < 8) {
                return false;
            }
        }

        final String value = condition.getValue();
        switch (value) {
            case "root":
                return element == element.getPage().getDocumentElement();

            case "enabled":
                return element instanceof DisabledElement && !((DisabledElement) element).isDisabled();

            case "disabled":
                return element instanceof DisabledElement && ((DisabledElement) element).isDisabled();

            case "focus":
                final HtmlPage htmlPage = element.getHtmlPageOrNull();
                if (htmlPage != null) {
                    final DomElement focus = htmlPage.getFocusedElement();
                    return element == focus;
                }
                return false;

            case "checked":
                return (element instanceof HtmlCheckBoxInput && ((HtmlCheckBoxInput) element).isChecked())
                        || (element instanceof HtmlRadioButtonInput && ((HtmlRadioButtonInput) element).isChecked()
                                || (element instanceof HtmlOption && ((HtmlOption) element).isSelected()));

            case "required":
                return (element instanceof HtmlInput
                            || element instanceof HtmlSelect
                            || element instanceof HtmlTextArea)
                        && element.hasAttribute("required");

            case "optional":
                return (element instanceof HtmlInput
                            || element instanceof HtmlSelect
                            || element instanceof HtmlTextArea)
                        && !element.hasAttribute("required");

            case "first-child":
                for (DomNode n = element.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
                    if (n instanceof DomElement) {
                        return false;
                    }
                }
                return true;

            case "last-child":
                for (DomNode n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
                    if (n instanceof DomElement) {
                        return false;
                    }
                }
                return true;

            case "first-of-type":
                final String firstType = element.getNodeName();
                for (DomNode n = element.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
                    if (n instanceof DomElement && n.getNodeName().equals(firstType)) {
                        return false;
                    }
                }
                return true;

            case "last-of-type":
                final String lastType = element.getNodeName();
                for (DomNode n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
                    if (n instanceof DomElement && n.getNodeName().equals(lastType)) {
                        return false;
                    }
                }
                return true;

            case "only-child":
                for (DomNode n = element.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
                    if (n instanceof DomElement) {
                        return false;
                    }
                }
                for (DomNode n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
                    if (n instanceof DomElement) {
                        return false;
                    }
                }
                return true;

            case "only-of-type":
                final String type = element.getNodeName();
                for (DomNode n = element.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
                    if (n instanceof DomElement && n.getNodeName().equals(type)) {
                        return false;
                    }
                }
                for (DomNode n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
                    if (n instanceof DomElement && n.getNodeName().equals(type)) {
                        return false;
                    }
                }
                return true;

            case "empty":
                return isEmpty(element);

            case "target":
                if (fromQuerySelectorAll && browserVersion.hasFeature(QUERYSELECTORALL_NO_TARGET)) {
                    return false;
                }
                final String ref = element.getPage().getUrl().getRef();
                return StringUtils.isNotBlank(ref) && ref.equals(element.getId());

            case "hover":
                return element.isMouseOver();

            default:
                if (value.startsWith("nth-child(")) {
                    final String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
                    int index = 0;
                    for (DomNode n = element; n != null; n = n.getPreviousSibling()) {
                        if (n instanceof DomElement) {
                            index++;
                        }
                    }
                    return getNth(nth, index);
                }
                else if (value.startsWith("nth-last-child(")) {
                    final String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
                    int index = 0;
                    for (DomNode n = element; n != null; n = n.getNextSibling()) {
                        if (n instanceof DomElement) {
                            index++;
                        }
                    }
                    return getNth(nth, index);
                }
                else if (value.startsWith("nth-of-type(")) {
                    final String nthType = element.getNodeName();
                    final String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
                    int index = 0;
                    for (DomNode n = element; n != null; n = n.getPreviousSibling()) {
                        if (n instanceof DomElement && n.getNodeName().equals(nthType)) {
                            index++;
                        }
                    }
                    return getNth(nth, index);
                }
                else if (value.startsWith("nth-last-of-type(")) {
                    final String nthLastType = element.getNodeName();
                    final String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
                    int index = 0;
                    for (DomNode n = element; n != null; n = n.getNextSibling()) {
                        if (n instanceof DomElement && n.getNodeName().equals(nthLastType)) {
                            index++;
                        }
                    }
                    return getNth(nth, index);
                }
                else if (value.startsWith("not(")) {
                    final String selectors = value.substring(value.indexOf('(') + 1, value.length() - 1);
                    final AtomicBoolean errorOccured = new AtomicBoolean(false);
                    final CSSErrorHandler errorHandler = new CSSErrorHandler() {
                        @Override
                        public void warning(final CSSParseException exception) throws CSSException {
                            // ignore
                        }

                        @Override
                        public void fatalError(final CSSParseException exception) throws CSSException {
                            errorOccured.set(true);
                        }

                        @Override
                        public void error(final CSSParseException exception) throws CSSException {
                            errorOccured.set(true);
                        }
                    };
                    final CSSOMParser parser = new CSSOMParser(new CSS3Parser());
                    parser.setErrorHandler(errorHandler);
                    try {
                        final SelectorList selectorList
                            = parser.parseSelectors(new InputSource(new StringReader(selectors)));
                        if (errorOccured.get() || selectorList == null || selectorList.size() != 1) {
                            throw new CSSException("Invalid selectors: " + selectors);
                        }

                        validateSelectors(selectorList, 9, element);

                        return !selects(browserVersion, selectorList.get(0), element,
                                null, fromQuerySelectorAll);
                    }
                    catch (final IOException e) {
                        throw new CSSException("Error parsing CSS selectors from '" + selectors + "': "
                                + e.getMessage());
                    }
                }
                return false;
        }
    }

    private static boolean isEmpty(final DomElement element) {
        for (DomNode n = element.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof DomElement || n instanceof DomText) {
                return false;
            }
        }
        return true;
    }

    private static boolean getNth(final String nth, final int index) {
        if ("odd".equalsIgnoreCase(nth)) {
            return index % 2 != 0;
        }

        if ("even".equalsIgnoreCase(nth)) {
            return index % 2 == 0;
        }

        // an+b
        final int nIndex = nth.indexOf('n');
        int a = 0;
        if (nIndex != -1) {
            String value = nth.substring(0, nIndex).trim();
            if ("-".equals(value)) {
                a = -1;
            }
            else {
                if (value.startsWith("+")) {
                    value = value.substring(1);
                }
                a = NumberUtils.toInt(value, 1);
            }
        }

        String value = nth.substring(nIndex + 1).trim();
        if (value.startsWith("+")) {
            value = value.substring(1);
        }
        final int b = NumberUtils.toInt(value, 0);
        if (a == 0) {
            return index == b && b > 0;
        }

        final double n = (index - b) / (double) a;
        return n >= 0 && n % 1 == 0;
    }

    /**
     * Parses the CSS at the specified input source. If anything at all goes wrong, this method
     * returns an empty stylesheet.
     *
     * @param source the source from which to retrieve the CSS to be parsed
     * @return the stylesheet parsed from the specified input source
     */
    private org.w3c.dom.css.CSSStyleSheet parseCSS(final InputSource source) {
        org.w3c.dom.css.CSSStyleSheet ss;
        try {
            final CSSErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
            final CSSOMParser parser = new CSSOMParser(new CSS3Parser());
            parser.setErrorHandler(errorHandler);
            ss = parser.parseStyleSheet(source, null);
        }
        catch (final Throwable t) {
            LOG.error("Error parsing CSS from '" + toString(source) + "': " + t.getMessage(), t);
            ss = new CSSStyleSheetImpl();
        }
        return ss;
    }

    /**
     * Parses the selectors at the specified input source. If anything at all goes wrong, this
     * method returns an empty selector list.
     *
     * @param source the source from which to retrieve the selectors to be parsed
     * @return the selectors parsed from the specified input source
     */
    public SelectorList parseSelectors(final InputSource source) {
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
            LOG.error("Error parsing CSS selectors from '" + toString(source) + "': " + t.getMessage(), t);
            selectors = new SelectorListImpl();
        }
        return selectors;
    }

    /**
     * Parses the given media string. If anything at all goes wrong, this
     * method returns an empty MediaList list.
     *
     * @param source the source from which to retrieve the media to be parsed
     * @return the media parsed from the specified input source
     */
    static MediaList parseMedia(final CSSErrorHandler errorHandler, final String mediaString) {
        MediaList media = media_.get(mediaString);
        if (media != null) {
            return media;
        }

        try {
            final CSSOMParser parser = new CSSOMParser(new CSS3Parser());
            parser.setErrorHandler(errorHandler);

            final InputSource source = new InputSource(new StringReader(mediaString));
            media = new MediaListImpl(parser.parseMedia(source));
            if (media != null) {
                media_.put(mediaString, media);
                return media;
            }
        }
        catch (final Exception e) {
            LOG.error("Error parsing CSS media from '" + mediaString + "': " + e.getMessage(), e);
        }

        media = new MediaListImpl(null);
        media_.put(mediaString, media);
        return media;
    }

    /**
     * Returns the contents of the specified input source, ignoring any {@link IOException}s.
     * @param source the input source from which to read
     * @return the contents of the specified input source, or an empty string if an {@link IOException} occurs
     */
    private static String toString(final InputSource source) {
        try {
            final Reader reader = source.getReader();
            if (null != reader) {
                // try to reset to produce some output
                if (reader instanceof StringReader) {
                    final StringReader sr = (StringReader) reader;
                    sr.reset();
                }
                return IOUtils.toString(reader);
            }
            return "";
        }
        catch (final IOException e) {
            return "";
        }
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
    @JsxGetter({IE, CHROME})
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
        final BrowserVersion version = getBrowserVersion();

        if (ownerNode_ != null) {
            final DomNode node = ownerNode_.getDomNodeOrDie();
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
            final int result = wrapped_.insertRule(rule, fixIndex(position));
            refreshCssRules();
            return result;
        }
        catch (final DOMException e) {
            // in case of error try with an empty rule
            final int pos = rule.indexOf('{');
            if (pos > -1) {
                final String newRule = rule.substring(0, pos) + "{}";
                try {
                    final int result = wrapped_.insertRule(newRule, fixIndex(position));
                    refreshCssRules();
                    return result;
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

        final CSSRuleList ruleList = getWrappedSheet().getCssRules();
        final List<org.w3c.dom.css.CSSRule> rules = ((CSSRuleListImpl) ruleList).getRules();
        int pos = 0;
        for (Iterator<CSSRule> it = rules.iterator(); it.hasNext();) {
            final org.w3c.dom.css.CSSRule rule = it.next();
            if (rule instanceof org.w3c.dom.css.CSSCharsetRule) {
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
            wrapped_.deleteRule(fixIndex(position));
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
    @JsxFunction({IE, CHROME})
    public int addRule(final String selector, final String rule) {
        String completeRule = selector + " {" + rule + "}";
        try {
            initCssRules();
            wrapped_.insertRule(completeRule, wrapped_.getCssRules().getLength());
            refreshCssRules();
        }
        catch (final DOMException e) {
            // in case of error try with an empty rule
            completeRule = selector + " {}";
            try {
                wrapped_.insertRule(completeRule, wrapped_.getCssRules().getLength());
                refreshCssRules();
            }
            catch (final DOMException ex) {
                throw Context.throwAsScriptRuntimeEx(ex);
            }
        }
        if (getBrowserVersion().hasFeature(STYLESHEET_ADD_RULE_RETURNS_POS)) {
            return wrapped_.getCssRules().getLength() - 1;
        }
        return -1;
    }

    /**
     * Deletes an existing rule.
     * @param position the position of the rule to be deleted
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms531195(v=VS.85).aspx">MSDN</a>
     */
    @JsxFunction({IE, CHROME})
    public void removeRule(final int position) {
        try {
            initCssRules();
            wrapped_.deleteRule(fixIndex(position));
            refreshCssRules();
        }
        catch (final DOMException e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Returns this stylesheet's URI (used to resolved contained @import rules).
     * @return this stylesheet's URI (used to resolved contained @import rules)
     */
    public String getUri() {
        return uri_;
    }

    /**
     * Returns {@code true} if this stylesheet is active, based on the media types it is associated with (if any).
     * @return {@code true} if this stylesheet is active, based on the media types it is associated with (if any)
     */
    public boolean isActive() {
        final String media;
        final HtmlElement e = ownerNode_.getDomNodeOrNull();
        if (e instanceof HtmlStyle) {
            final HtmlStyle style = (HtmlStyle) e;
            media = style.getMediaAttribute();
        }
        else if (e instanceof HtmlLink) {
            final HtmlLink link = (HtmlLink) e;
            media = link.getMediaAttribute();
        }
        else {
            return true;
        }

        if (StringUtils.isBlank(media)) {
            return true;
        }

        final WebClient webClient = getWindow().getWebWindow().getWebClient();
        final MediaList mediaList = parseMedia(webClient.getCssErrorHandler(), media);
        return isActive(this, mediaList);
    }

    /**
     * Returns {@code true} if this stylesheet is enabled.
     * @return {@code true} if this stylesheet is enabled
     */
    public boolean isEnabled() {
        return enabled_;
    }

    /**
     * Sets whether this sheet is enabled or not.
     * @param enabled enabled or not
     */
    public void setEnabled(final boolean enabled) {
        enabled_ = enabled;
    }

    /**
     * Returns whether the specified {@link MediaList} is active or not.
     * @param scriptable the scriptable
     * @param mediaList the media list
     * @return whether the specified {@link MediaList} is active or not
     */
    static boolean isActive(final SimpleScriptable scriptable, final MediaList mediaList) {
        if (mediaList.getLength() == 0) {
            return true;
        }

        for (int i = 0; i < mediaList.getLength(); i++) {
            final MediaQuery mediaQuery = ((MediaListImpl) mediaList).mediaQuery(i);
            boolean isActive = isActive(scriptable, mediaQuery);
            if (mediaQuery.isNot()) {
                isActive = !isActive;
            }
            if (isActive) {
                return true;
            }
        }
        return false;
    }

    private static boolean isActive(final SimpleScriptable scriptable, final MediaQuery mediaQuery) {
        final String mediaType = mediaQuery.getMedia();
        if ("screen".equalsIgnoreCase(mediaType) || "all".equalsIgnoreCase(mediaType)) {
            for (final Property property : mediaQuery.getProperties()) {
                final float val;
                switch (property.getName()) {
                    case "max-width":
                        val = pixelValue((CSSValueImpl) property.getValue(), scriptable);
                        if (val < scriptable.getWindow().getWebWindow().getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "min-width":
                        val = pixelValue((CSSValueImpl) property.getValue(), scriptable);
                        if (val > scriptable.getWindow().getWebWindow().getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "max-device-width":
                        val = pixelValue((CSSValueImpl) property.getValue(), scriptable);
                        if (val < scriptable.getWindow().getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "min-device-width":
                        val = pixelValue((CSSValueImpl) property.getValue(), scriptable);
                        if (val > scriptable.getWindow().getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "max-height":
                        val = pixelValue((CSSValueImpl) property.getValue(), scriptable);
                        if (val < scriptable.getWindow().getWebWindow().getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "min-height":
                        val = pixelValue((CSSValueImpl) property.getValue(), scriptable);
                        if (val > scriptable.getWindow().getWebWindow().getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "max-device-height":
                        val = pixelValue((CSSValueImpl) property.getValue(), scriptable);
                        if (val < scriptable.getWindow().getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "min-device-height":
                        val = pixelValue((CSSValueImpl) property.getValue(), scriptable);
                        if (val > scriptable.getWindow().getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "resolution":
                        val = resolutionValue((CSSValueImpl) property.getValue());
                        if (Math.round(val) != scriptable.getWindow().getScreen().getDeviceXDPI()) {
                            return false;
                        }
                        break;

                    case "max-resolution":
                        val = resolutionValue((CSSValueImpl) property.getValue());
                        if (val < scriptable.getWindow().getScreen().getDeviceXDPI()) {
                            return false;
                        }
                        break;

                    case "min-resolution":
                        val = resolutionValue((CSSValueImpl) property.getValue());
                        if (val > scriptable.getWindow().getScreen().getDeviceXDPI()) {
                            return false;
                        }
                        break;

                    case "orientation":
                        final String orient = property.getValue().getCssText();
                        final WebWindow window = scriptable.getWindow().getWebWindow();
                        if ("portrait".equals(orient)) {
                            if (window.getInnerWidth() > window.getInnerHeight()) {
                                return false;
                            }
                        }
                        else if ("landscape".equals(orient)) {
                            if (window.getInnerWidth() < window.getInnerHeight()) {
                                return false;
                            }
                        }
                        else {
                            LOG.warn("CSSValue '" + property.getValue().getCssText()
                                        + "' not supported for feature 'orientation'.");
                            return false;
                        }
                        break;

                    default:
                }
            }
            return true;
        }
        return false;
    }

    private static float pixelValue(final CSSValueImpl cssValue, final SimpleScriptable scriptable) {
        final int dpi;
        switch (cssValue.getPrimitiveType()) {
            case CSSPrimitiveValue.CSS_PX:
                return cssValue.getFloatValue(CSSPrimitiveValue.CSS_PX);
            case CSSPrimitiveValue.CSS_EMS:
                // hard coded default for the moment 16px = 1 em
                return 16f * cssValue.getFloatValue(CSSPrimitiveValue.CSS_EMS);
            case CSSPrimitiveValue.CSS_PERCENTAGE:
                // hard coded default for the moment 16px = 100%
                return 0.16f * cssValue.getFloatValue(CSSPrimitiveValue.CSS_PERCENTAGE);
            case CSSPrimitiveValue.CSS_EXS:
                // hard coded default for the moment 16px = 100%
                return 0.16f * cssValue.getFloatValue(CSSPrimitiveValue.CSS_EXS);
            case CSSPrimitiveValue.CSS_MM:
                dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
                return (dpi / 25.4f) * cssValue.getFloatValue(CSSPrimitiveValue.CSS_MM);
            case CSSPrimitiveValue.CSS_CM:
                dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
                return (dpi / 254f) * cssValue.getFloatValue(CSSPrimitiveValue.CSS_CM);
            case CSSPrimitiveValue.CSS_PT:
                dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
                return (dpi / 72f) * cssValue.getFloatValue(CSSPrimitiveValue.CSS_PT);
            default:
                break;
        }
        LOG.warn("CSSValue '" + cssValue.getCssText() + "' has to be a 'px', 'em', '%', 'mm', 'ex', or 'pt' value.");
        return -1;
    }

    private static float resolutionValue(final CSSValueImpl cssValue) {
        if (cssValue.getPrimitiveType() == CSSPrimitiveValue.CSS_DIMENSION) {
            final String text = cssValue.getCssText();
            if (text.endsWith("dpi")) {
                return cssValue.getFloatValue(CSSPrimitiveValue.CSS_DIMENSION);
            }
            if (text.endsWith("dpcm")) {
                return 2.54f * cssValue.getFloatValue(CSSPrimitiveValue.CSS_DIMENSION);
            }
            if (text.endsWith("dppx")) {
                return 96 * cssValue.getFloatValue(CSSPrimitiveValue.CSS_DIMENSION);
            }
        }

        LOG.warn("CSSValue '" + cssValue.getCssText() + "' has to be a 'dpi', 'dpcm', or 'dppx' value.");
        return -1;
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
        for (Selector selector : selectorList) {
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
                    for (Condition condition : conditions) {
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
                        && isValidSelector(das.getSiblingSelector(), documentMode, domNode);
            case GENERAL_ADJACENT_SELECTOR:
                final GeneralAdjacentSelector gas = (GeneralAdjacentSelector) selector;
                return isValidSelector(gas.getSelector(), documentMode, domNode)
                        && isValidSelector(gas.getSiblingSelector(), documentMode, domNode);
            default:
                LOG.warn("Unhandled CSS selector type '" + selector.getSelectorType() + "'. Accepting it silently.");
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
                return true;
            case PSEUDO_CLASS_CONDITION:
                final PseudoClassCondition pcc = (PseudoClassCondition) condition;
                String value = pcc.getValue();
                if (value.endsWith(")")) {
                    if (value.endsWith("()")) {
                        return false;
                    }
                    value = value.substring(0, value.indexOf('(') + 1) + ')';
                }
                if (documentMode < 9) {
                    return CSS2_PSEUDO_CLASSES.contains(value);
                }

                if (!CSS2_PSEUDO_CLASSES.contains(value)
                        && domNode.hasFeature(QUERYSELECTOR_CSS3_PSEUDO_REQUIRE_ATTACHED_NODE)
                        && !domNode.isAttachedToPage()
                        && !domNode.hasChildNodes()) {
                    throw new CSSException("Syntax Error");
                }

                if ("nth-child()".equals(value)) {
                    final String arg = StringUtils.substringBetween(pcc.getValue(), "(", ")").trim();
                    return "even".equalsIgnoreCase(arg) || "odd".equalsIgnoreCase(arg)
                            || NTH_NUMERIC.matcher(arg).matches()
                            || NTH_COMPLEX.matcher(arg).matches();
                }
                return CSS3_PSEUDO_CLASSES.contains(value);
            default:
                LOG.warn("Unhandled CSS condition type '" + condition.getConditionType() + "'. Accepting it silently.");
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
