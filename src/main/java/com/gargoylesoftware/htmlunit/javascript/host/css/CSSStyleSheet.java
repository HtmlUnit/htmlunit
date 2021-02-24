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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_PSEUDO_SELECTOR_MS_PLACEHHOLDER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_PSEUDO_SELECTOR_PLACEHOLDER_SHOWN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLLINK_CHECK_TYPE_FOR_STYLESHEET;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTOR_CSS3_PSEUDO_REQUIRE_ATTACHED_NODE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STYLESHEET_ADD_RULE_RETURNS_POS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STYLESHEET_HREF_EMPTY_IS_NULL;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSCharsetRuleImpl;
import com.gargoylesoftware.css.dom.CSSImportRuleImpl;
import com.gargoylesoftware.css.dom.CSSMediaRuleImpl;
import com.gargoylesoftware.css.dom.CSSRuleListImpl;
import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl.CSSPrimitiveValueType;
import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.css.parser.InputSource;
import com.gargoylesoftware.css.parser.LexicalUnit;
import com.gargoylesoftware.css.parser.condition.Condition;
import com.gargoylesoftware.css.parser.condition.Condition.ConditionType;
import com.gargoylesoftware.css.parser.javacc.CSS3Parser;
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
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.MimeType;
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
    private final CSSStyleSheetImpl wrapped_;

    /** The HTML element which owns this stylesheet. */
    private final HTMLElement ownerNode_;

    /** The collection of rules defined in this style sheet. */
    private com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList cssRules_;
    private List<Integer> cssRulesIndexFix_;

    /** The CSS import rules and their corresponding stylesheets. */
    private final Map<CSSImportRuleImpl, CSSStyleSheet> imports_ = new HashMap<>();

    /** cache parsed media strings */
    private static final transient Map<String, MediaListImpl> media_ = new HashMap<>();

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
    @JsxConstructor({CHROME, EDGE, FF, FF78})
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
        final Window win = element.getWindow();

        setParentScope(win);
        setPrototype(getPrototype(CSSStyleSheet.class));
        if (source != null) {
            source.setURI(uri);
        }
        wrapped_ = parseCSS(source, win.getWebWindow().getWebClient());
        uri_ = uri;
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

        CSSStyleSheetImpl css = null;
        try (InputSource source = new InputSource(new StringReader(styleSheet))) {
            source.setURI(uri);
            css = parseCSS(source, win.getWebWindow().getWebClient());
        }
        catch (final IOException e) {
            LOG.error(e.getMessage(), e);
        }

        setParentScope(win);
        setPrototype(getPrototype(CSSStyleSheet.class));
        wrapped_ = css;
        uri_ = uri;
        ownerNode_ = element;
    }

    /**
     * Creates a new stylesheet representing the specified CSS stylesheet.
     * @param element the owning node
     * @param wrapped the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CSSStyleSheet(final HTMLElement element, final CSSStyleSheetImpl wrapped, final String uri) {
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
    public CSSStyleSheetImpl getWrappedSheet() {
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

        final BrowserVersion browser = getBrowserVersion();
        final DomElement e = element.getDomNodeOrDie();
        final List<CSSStyleSheetImpl.SelectorEntry> matchingRules =
                selects(getRuleIndex(), this, browser, e, pseudoElement, false);
        for (final CSSStyleSheetImpl.SelectorEntry entry : matchingRules) {
            final CSSStyleDeclarationImpl dec = entry.getRule().getStyle();
            style.applyStyleFromSelector(dec, entry.getSelector());
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
        final HtmlPage page = (HtmlPage) element.getDomNodeOrDie().getPage();
        String uri = page.getUrl().toExternalForm();
        try {
            // Retrieve the associated content and respect client settings regarding failing HTTP status codes.
            final WebRequest request;
            final WebResponse response;
            final WebClient client = page.getWebClient();
            if (link == null) {
                // Use href.
                final BrowserVersion browser = client.getBrowserVersion();
                request = new WebRequest(new URL(url), browser.getCssAcceptHeader(), browser.getAcceptEncodingHeader());
                request.setRefererlHeader(page.getUrl());

                // our cache is a bit strange;
                // loadWebResponse check the cache for the web response
                // AND also fixes the request url for the following cache lookups
                response = client.loadWebResponse(request);
            }
            else {
                // Use link.
                request = link.getWebRequest();

                if (element.getBrowserVersion().hasFeature(HTMLLINK_CHECK_TYPE_FOR_STYLESHEET)) {
                    final String type = link.getTypeAttribute();
                    if (StringUtils.isNotBlank(type) && !MimeType.TEXT_CSS.equals(type)) {
                        return new CSSStyleSheet(element, "", uri);
                    }
                }

                // our cache is a bit strange;
                // loadWebResponse check the cache for the web response
                // AND also fixes the request url for the following cache lookups
                response = link.getWebResponse(true, request);
            }

            // now we can look into the cache with the fixed request for
            // a cached script
            final Cache cache = client.getCache();
            final Object fromCache = cache.getCachedObject(request);
            if (fromCache instanceof CSSStyleSheetImpl) {
                uri = request.getUrl().toExternalForm();
                return new CSSStyleSheet(element, (CSSStyleSheetImpl) fromCache, uri);
            }

            uri = response.getWebRequest().getUrl().toExternalForm();
            client.printContentIfNecessary(response);
            client.throwFailingHttpStatusCodeExceptionIfNecessary(response);
            // CSS content must have downloaded OK; go ahead and build the corresponding stylesheet.

            final CSSStyleSheet sheet;
            final String contentType = response.getContentType();
            if (StringUtils.isEmpty(contentType) || MimeType.TEXT_CSS.equals(contentType)) {

                final InputStream in = response.getContentAsStreamWithBomIfApplicable();
                if (in == null) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Loading stylesheet for url '" + uri + "' returns empty responseData");
                    }
                    return new CSSStyleSheet(element, "", uri);
                }
                try {
                    Charset cssEncoding = Charset.forName("windows-1252");
                    final Charset contentCharset =
                            EncodingSniffer.sniffEncodingFromHttpHeaders(response.getResponseHeaders());
                    if (contentCharset == null && request.getCharset() != null) {
                        cssEncoding = request.getCharset();
                    }
                    else if (contentCharset != null) {
                        cssEncoding = contentCharset;
                    }

                    if (in instanceof BOMInputStream) {
                        final BOMInputStream bomIn = (BOMInputStream) in;
                        // there seems to be a bug in BOMInputStream
                        // we have to call this before hasBOM(ByteOrderMark)
                        if (bomIn.hasBOM()) {
                            if (bomIn.hasBOM(ByteOrderMark.UTF_8)) {
                                cssEncoding = UTF_8;
                            }
                            else if (bomIn.hasBOM(ByteOrderMark.UTF_16BE)) {
                                cssEncoding = UTF_16BE;
                            }
                            else if (bomIn.hasBOM(ByteOrderMark.UTF_16LE)) {
                                cssEncoding = UTF_16LE;
                            }
                        }
                    }
                    try (InputSource source = new InputSource(new InputStreamReader(in, cssEncoding))) {
                        source.setURI(uri);
                        sheet = new CSSStyleSheet(element, source, uri);
                    }
                }
                finally {
                    in.close();
                }
            }
            else {
                sheet = new CSSStyleSheet(element, "", uri);
            }

            // cache the style sheet
            if (!cache.cacheIfPossible(request, response, sheet.getWrappedSheet())) {
                response.cleanUp();
            }

            return sheet;
        }
        catch (final FailingHttpStatusCodeException e) {
            // Got a 404 response or something like that; behave nicely.
            if (LOG.isErrorEnabled()) {
                LOG.error("Exception loading " + uri, e);
            }
            return new CSSStyleSheet(element, "", uri);
        }
        catch (final IOException e) {
            // Got a basic IO error; behave nicely.
            if (LOG.isErrorEnabled()) {
                LOG.error("IOException loading " + uri, e);
            }
            return new CSSStyleSheet(element, "", uri);
        }
        catch (final RuntimeException e) {
            // Got something unexpected; we can throw an exception in this case.
            if (LOG.isErrorEnabled()) {
                LOG.error("RuntimeException loading " + uri, e);
            }
            throw Context.reportRuntimeError("Exception: " + e);
        }
        catch (final Exception e) {
            // Got something unexpected; we can throw an exception in this case.
            if (LOG.isErrorEnabled()) {
                LOG.error("Exception loading " + uri, e);
            }
            throw Context.reportRuntimeError("Exception: " + e);
        }
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
            case ELEMENT_NODE_SELECTOR:
                final ElementSelector es = (ElementSelector) selector;

                final String name;
                final String elementName;
                if (element.getPage().hasCaseSensitiveTagNames()) {
                    name = es.getLocalName();
                    elementName = element.getLocalName();
                }
                else {
                    name = es.getLocalNameLowerCase();
                    elementName = element.getLowercaseName();
                }

                if (name == null || name.equals(elementName)) {
                    final List<Condition> conditions = es.getConditions();
                    if (conditions != null) {
                        for (final Condition condition : conditions) {
                            if (!selects(browserVersion, condition, element, fromQuerySelectorAll)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }

                return false;

            case CHILD_SELECTOR:
                final DomNode parentNode = element.getParentNode();
                if (parentNode == element.getPage()) {
                    return false;
                }
                if (!(parentNode instanceof DomElement)) {
                    return false; // for instance parent is a DocumentFragment
                }
                final ChildSelector cs = (ChildSelector) selector;
                return selects(browserVersion, cs.getSimpleSelector(), element, pseudoElement, fromQuerySelectorAll)
                    && selects(browserVersion, cs.getAncestorSelector(), (DomElement) parentNode,
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
                    while (ancestor instanceof DomElement) {
                        if (selects(browserVersion, dsAncestorSelector, (DomElement) ancestor, pseudoElement,
                                fromQuerySelectorAll)) {
                            return true;
                        }
                        ancestor = ancestor.getParentNode();
                    }
                }
                return false;

            case DIRECT_ADJACENT_SELECTOR:
                final DirectAdjacentSelector das = (DirectAdjacentSelector) selector;
                if (selects(browserVersion, das.getSimpleSelector(), element, pseudoElement, fromQuerySelectorAll)) {
                    DomNode prev = element.getPreviousSibling();
                    while (prev != null && !(prev instanceof DomElement)) {
                        prev = prev.getPreviousSibling();
                    }
                    return prev != null
                            && selects(browserVersion, das.getSelector(),
                                    (DomElement) prev, pseudoElement, fromQuerySelectorAll);
                }
                return false;

            case GENERAL_ADJACENT_SELECTOR:
                final GeneralAdjacentSelector gas = (GeneralAdjacentSelector) selector;
                if (selects(browserVersion, gas.getSimpleSelector(), element, pseudoElement, fromQuerySelectorAll)) {
                    for (DomNode prev1 = element.getPreviousSibling(); prev1 != null;
                                                        prev1 = prev1.getPreviousSibling()) {
                        if (prev1 instanceof DomElement
                            && selects(browserVersion, gas.getSelector(), (DomElement) prev1,
                                    pseudoElement, fromQuerySelectorAll)) {
                            return true;
                        }
                    }
                }
                return false;
            case PSEUDO_ELEMENT_SELECTOR:
                if (pseudoElement != null && pseudoElement.length() != 0 && pseudoElement.charAt(0) == ':') {
                    final String pseudoName = ((PseudoElementSelector) selector).getLocalName();
                    return pseudoName.equals(pseudoElement.substring(1));
                }
                return false;

            default:
                if (LOG.isErrorEnabled()) {
                    LOG.error("Unknown CSS selector type '" + selector.getSelectorType() + "'.");
                }
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
                return condition.getValue().equals(element.getId());

            case CLASS_CONDITION:
                String v3 = condition.getValue();
                if (v3.indexOf('\\') > -1) {
                    v3 = UNESCAPE_SELECTOR.matcher(v3).replaceAll("$1");
                }
                final String a3 = element.getAttributeDirect("class");
                return selectsWhitespaceSeparated(v3, a3);

            case ATTRIBUTE_CONDITION:
                String value = condition.getValue();
                if (value != null) {
                    if (value.indexOf('\\') > -1) {
                        value = UNESCAPE_SELECTOR.matcher(value).replaceAll("$1");
                    }
                    final String attrValue = element.getAttribute(condition.getLocalName());
                    return ATTRIBUTE_NOT_DEFINED != attrValue && attrValue.equals(value);
                }
                return element.hasAttribute(condition.getLocalName());

            case PREFIX_ATTRIBUTE_CONDITION:
                final String prefixValue = condition.getValue();
                return !"".equals(prefixValue)
                        && element.getAttribute(condition.getLocalName()).startsWith(prefixValue);

            case SUFFIX_ATTRIBUTE_CONDITION:
                final String suffixValue = condition.getValue();
                return !"".equals(suffixValue)
                        && element.getAttribute(condition.getLocalName()).endsWith(suffixValue);

            case SUBSTRING_ATTRIBUTE_CONDITION:
                final String substringValue = condition.getValue();
                return !"".equals(substringValue)
                        && element.getAttribute(condition.getLocalName()).contains(substringValue);

            case BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
                final String v = condition.getValue();
                final String a = element.getAttribute(condition.getLocalName());
                return selectsHyphenSeparated(v, a);

            case ONE_OF_ATTRIBUTE_CONDITION:
                final String v2 = condition.getValue();
                final String a2 = element.getAttribute(condition.getLocalName());
                return selectsOneOf(v2, a2);

            case LANG_CONDITION:
                final String lcLang = condition.getValue();
                final int lcLangLength = lcLang.length();
                for (DomNode node = element; node instanceof HtmlElement; node = node.getParentNode()) {
                    final String nodeLang = ((HtmlElement) node).getAttributeDirect("lang");
                    if (ATTRIBUTE_NOT_DEFINED != nodeLang) {
                        // "en", "en-GB" should be matched by "en" but not "english"
                        return nodeLang.startsWith(lcLang)
                            && (nodeLang.length() == lcLangLength || '-' == nodeLang.charAt(lcLangLength));
                    }
                }
                return false;

            case PSEUDO_CLASS_CONDITION:
                return selectsPseudoClass(browserVersion, condition, element, fromQuerySelectorAll);

            default:
                if (LOG.isErrorEnabled()) {
                    LOG.error("Unknown CSS condition type '" + condition.getConditionType() + "'.");
                }
                return false;
        }
    }

    private static boolean selectsOneOf(final String condition, final String attribute) {
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
            if (' ' == attribute.charAt(conditionLength)
                    && attribute.startsWith(condition)) {
                return true;
            }
            if (' ' == attribute.charAt(attribLength - conditionLength - 1)
                    && attribute.endsWith(condition)) {
                return true;
            }
            if (attribLength + 1 > conditionLength) {
                final StringBuilder tmp = new StringBuilder(conditionLength + 2);
                tmp.append(' ').append(condition).append(' ');
                return attribute.contains(tmp);
            }
            return false;
        }
        return attribute.equals(condition);
    }

    private static boolean selectsHyphenSeparated(final String condition, final String attribute) {
        final int conditionLength = condition.length();
        if (conditionLength < 1) {
            if (attribute != ATTRIBUTE_NOT_DEFINED) {
                final int attribLength = attribute.length();
                return attribLength == 0 || '-' == attribute.charAt(0);
            }
            return false;
        }

        final int attribLength = attribute.length();
        if (attribLength < conditionLength) {
            return false;
        }
        if (attribLength > conditionLength) {
            if ('-' == attribute.charAt(conditionLength)
                    && attribute.startsWith(condition)) {
                return true;
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
            final Condition condition, final DomElement element, final boolean fromQuerySelectorAll) {
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
                final String ref = element.getPage().getUrl().getRef();
                return StringUtils.isNotBlank(ref) && ref.equals(element.getId());

            case "hover":
                return element.isMouseOver();

            case "placeholder-shown":
                if (browserVersion.hasFeature(CSS_PSEUDO_SELECTOR_PLACEHOLDER_SHOWN)) {
                    return element instanceof HtmlInput
                            && StringUtils.isEmpty(((HtmlInput) element).getValueAttribute())
                            && StringUtils.isNotEmpty(((HtmlInput) element).getPlaceholder());
                }

            case "-ms-input-placeholder":
                if (browserVersion.hasFeature(CSS_PSEUDO_SELECTOR_MS_PLACEHHOLDER)) {
                    return element instanceof HtmlInput
                            && StringUtils.isEmpty(((HtmlInput) element).getValueAttribute())
                            && StringUtils.isNotEmpty(((HtmlInput) element).getPlaceholder());
                }

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
                        final SelectorList selectorList = parser.parseSelectors(selectors);
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
                if (value.length() > 0 && value.charAt(0) == '+') {
                    value = value.substring(1);
                }
                a = NumberUtils.toInt(value, 1);
            }
        }

        String value = nth.substring(nIndex + 1).trim();
        if (value.length() > 0 && value.charAt(0) == '+') {
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
     * @param client the client
     * @return the stylesheet parsed from the specified input source
     */
    private static CSSStyleSheetImpl parseCSS(final InputSource source, final WebClient client) {
        CSSStyleSheetImpl ss;
        try {
            final CSSErrorHandler errorHandler = client.getCssErrorHandler();
            final CSSOMParser parser = new CSSOMParser(new CSS3Parser());
            parser.setErrorHandler(errorHandler);
            ss = parser.parseStyleSheet(source, null);
        }
        catch (final Throwable t) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Error parsing CSS from '" + toString(source) + "': " + t.getMessage(), t);
            }
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
     * Parses the given media string. If anything at all goes wrong, this
     * method returns an empty MediaList list.
     *
     * @param source the source from which to retrieve the media to be parsed
     * @return the media parsed from the specified input source
     */
    static MediaListImpl parseMedia(final CSSErrorHandler errorHandler, final String mediaString) {
        MediaListImpl media = media_.get(mediaString);
        if (media != null) {
            return media;
        }

        try {
            final CSSOMParser parser = new CSSOMParser(new CSS3Parser());
            parser.setErrorHandler(errorHandler);

            media = new MediaListImpl(parser.parseMedia(mediaString));
            media_.put(mediaString, media);
            return media;
        }
        catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Error parsing CSS media from '" + mediaString + "': " + e.getMessage(), e);
            }
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
            LOG.error(e.getMessage(), e);
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
            wrapped_.insertRule(rule, fixIndex(position));
            refreshCssRules();
            return position;
        }
        catch (final DOMException e) {
            // in case of error try with an empty rule
            final int pos = rule.indexOf('{');
            if (pos > -1) {
                final String newRule = rule.substring(0, pos) + "{}";
                try {
                    wrapped_.insertRule(newRule, fixIndex(position));
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

        final CSSRuleListImpl ruleList = getWrappedSheet().getCssRules();
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
        getWrappedSheet().resetRuleIndex();
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
    @JsxFunction
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
    @JsxFunction
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
        final MediaListImpl mediaList = parseMedia(webClient.getCssErrorHandler(), media);
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
    static boolean isActive(final SimpleScriptable scriptable, final MediaListImpl mediaList) {
        if (mediaList.getLength() == 0) {
            return true;
        }

        for (int i = 0; i < mediaList.getLength(); i++) {
            final MediaQuery mediaQuery = mediaList.mediaQuery(i);
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
                final double val;
                switch (property.getName()) {
                    case "max-width":
                        val = pixelValue(property.getValue(), scriptable);
                        if (val == -1 || val < scriptable.getWindow().getWebWindow().getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "min-width":
                        val = pixelValue(property.getValue(), scriptable);
                        if (val == -1 || val > scriptable.getWindow().getWebWindow().getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "max-device-width":
                        val = pixelValue(property.getValue(), scriptable);
                        if (val == -1 || val < scriptable.getWindow().getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "min-device-width":
                        val = pixelValue(property.getValue(), scriptable);
                        if (val == -1 || val > scriptable.getWindow().getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "max-height":
                        val = pixelValue(property.getValue(), scriptable);
                        if (val == -1 || val < scriptable.getWindow().getWebWindow().getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "min-height":
                        val = pixelValue(property.getValue(), scriptable);
                        if (val == -1 || val > scriptable.getWindow().getWebWindow().getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "max-device-height":
                        val = pixelValue(property.getValue(), scriptable);
                        if (val == -1 || val < scriptable.getWindow().getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "min-device-height":
                        val = pixelValue(property.getValue(), scriptable);
                        if (val == -1 || val > scriptable.getWindow().getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "resolution":
                        final CSSValueImpl propValue = property.getValue();
                        val = resolutionValue(propValue);
                        if (propValue == null) {
                            return true;
                        }
                        if (val == -1 || Math.round(val) != scriptable.getWindow().getScreen().getDeviceXDPI()) {
                            return false;
                        }
                        break;

                    case "max-resolution":
                        val = resolutionValue(property.getValue());
                        if (val == -1 || val < scriptable.getWindow().getScreen().getDeviceXDPI()) {
                            return false;
                        }
                        break;

                    case "min-resolution":
                        val = resolutionValue(property.getValue());
                        if (val == -1 || val > scriptable.getWindow().getScreen().getDeviceXDPI()) {
                            return false;
                        }
                        break;

                    case "orientation":
                        final CSSValueImpl cssValue = property.getValue();
                        if (cssValue == null) {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn("CSSValue is null not supported for feature 'orientation'");
                            }
                            return true;
                        }

                        final String orient = cssValue.getCssText();
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
                            if (LOG.isWarnEnabled()) {
                                LOG.warn("CSSValue '" + property.getValue().getCssText()
                                            + "' not supported for feature 'orientation'.");
                            }
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

    private static double pixelValue(final CSSValueImpl cssValue, final SimpleScriptable scriptable) {
        if (cssValue == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("CSSValue is null but has to be a 'px', 'em', '%', 'mm', 'ex', or 'pt' value.");
            }
            return -1;
        }

        final LexicalUnit.LexicalUnitType luType = cssValue.getLexicalUnitType();
        if (luType != null) {
            final int dpi;

            switch (luType) {
                case PIXEL:
                    return cssValue.getDoubleValue();
                case EM:
                    // hard coded default for the moment 16px = 1 em
                    return 16f * cssValue.getDoubleValue();
                case PERCENTAGE:
                    // hard coded default for the moment 16px = 100%
                    return 0.16f * cssValue.getDoubleValue();
                case EX:
                    // hard coded default for the moment 16px = 100%
                    return 0.16f * cssValue.getDoubleValue();
                case CH:
                    // hard coded default for the moment 16px = 100%
                    return 0.16f * cssValue.getDoubleValue();
                case VW:
                    // hard coded default for the moment 16px = 100%
                    return 0.16f * cssValue.getDoubleValue();
                case VH:
                    // hard coded default for the moment 16px = 100%
                    return 0.16f * cssValue.getDoubleValue();
                case VMIN:
                    // hard coded default for the moment 16px = 100%
                    return 0.16f * cssValue.getDoubleValue();
                case VMAX:
                    // hard coded default for the moment 16px = 100%
                    return 0.16f * cssValue.getDoubleValue();
                case REM:
                    // hard coded default for the moment 16px = 100%
                    return 0.16f * cssValue.getDoubleValue();
                case MILLIMETER:
                    dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
                    return (dpi / 25.4f) * cssValue.getDoubleValue();
                case CENTIMETER:
                    dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
                    return (dpi / 254f) * cssValue.getDoubleValue();
                case POINT:
                    dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
                    return (dpi / 72f) * cssValue.getDoubleValue();
                default:
                    break;
            }
        }
        if (LOG.isWarnEnabled()) {
            LOG.warn("CSSValue '" + cssValue.getCssText()
                        + "' has to be a 'px', 'em', '%', 'mm', 'ex', or 'pt' value.");
        }
        return -1;
    }

    private static double resolutionValue(final CSSValueImpl cssValue) {
        if (cssValue == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("CSSValue is null but has to be a 'dpi', 'dpcm', or 'dppx' value.");
            }
            return -1;
        }

        if (cssValue.getPrimitiveType() == CSSPrimitiveValueType.CSS_DIMENSION) {
            final String text = cssValue.getCssText();
            if (text.endsWith("dpi")) {
                return cssValue.getDoubleValue();
            }
            if (text.endsWith("dpcm")) {
                return 2.54f * cssValue.getDoubleValue();
            }
            if (text.endsWith("dppx")) {
                return 96 * cssValue.getDoubleValue();
            }
        }

        if (LOG.isWarnEnabled()) {
            LOG.warn("CSSValue '" + cssValue.getCssText() + "' has to be a 'dpi', 'dpcm', or 'dppx' value.");
        }
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
                    return CSS2_PSEUDO_CLASSES.contains(value);
                }

                if (!CSS2_PSEUDO_CLASSES.contains(value)
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

                return CSS3_PSEUDO_CLASSES.contains(value);
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

    private CSSStyleSheetImpl.CSSStyleSheetRuleIndex getRuleIndex() {
        final CSSStyleSheetImpl styleSheet = getWrappedSheet();
        CSSStyleSheetImpl.CSSStyleSheetRuleIndex index = styleSheet.getRuleIndex();

        if (index == null) {
            index = new CSSStyleSheetImpl.CSSStyleSheetRuleIndex();
            final CSSRuleListImpl ruleList = styleSheet.getCssRules();
            index(index, ruleList, new HashSet<String>());

            styleSheet.setRuleIndex(index);
        }
        return index;
    }

    private void index(final CSSStyleSheetImpl.CSSStyleSheetRuleIndex index, final CSSRuleListImpl ruleList,
            final Set<String> alreadyProcessing) {

        for (final AbstractCSSRuleImpl rule : ruleList.getRules()) {
            if (rule instanceof CSSStyleRuleImpl) {
                final CSSStyleRuleImpl styleRule = (CSSStyleRuleImpl) rule;
                final SelectorList selectors = styleRule.getSelectors();
                for (final Selector selector : selectors) {
                    final SimpleSelector simpleSel = selector.getSimpleSelector();
                    if (SelectorType.ELEMENT_NODE_SELECTOR == simpleSel.getSelectorType()) {
                        final ElementSelector es = (ElementSelector) simpleSel;
                        boolean wasClass = false;
                        final List<Condition> conds = es.getConditions();
                        if (conds != null && conds.size() == 1) {
                            final Condition c = conds.get(0);
                            if (ConditionType.CLASS_CONDITION == c.getConditionType()) {
                                index.addClassSelector(es, c.getValue(), selector, styleRule);
                                wasClass = true;
                            }
                        }
                        if (!wasClass) {
                            index.addElementSelector(es, selector, styleRule);
                        }
                    }
                    else {
                        index.addOtherSelector(selector, styleRule);
                    }
                }
            }
            else if (rule instanceof CSSImportRuleImpl) {
                final CSSImportRuleImpl importRule = (CSSImportRuleImpl) rule;
                final MediaListImpl mediaList = importRule.getMedia();

                CSSStyleSheet sheet = imports_.get(importRule);
                if (sheet == null) {
                    final String href = importRule.getHref();
                    final String url = UrlUtils.resolveUrl(getUri(), href);
                    sheet = loadStylesheet(ownerNode_, null, url);
                    imports_.put(importRule, sheet);
                }

                if (!alreadyProcessing.contains(sheet.getUri())) {
                    final CSSRuleListImpl sheetRuleList = sheet.getWrappedSheet().getCssRules();
                    alreadyProcessing.add(sheet.getUri());

                    if (mediaList.getLength() == 0 && index.getMediaList().getLength() == 0) {
                        index(index, sheetRuleList, alreadyProcessing);
                    }
                    else {
                        index(index.addMedia(mediaList), sheetRuleList, alreadyProcessing);
                    }
                }
            }
            else if (rule instanceof CSSMediaRuleImpl) {
                final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) rule;
                final MediaListImpl mediaList = mediaRule.getMediaList();
                if (mediaList.getLength() == 0 && index.getMediaList().getLength() == 0) {
                    index(index, mediaRule.getCssRules(), alreadyProcessing);
                }
                else {
                    index(index.addMedia(mediaList), mediaRule.getCssRules(), alreadyProcessing);
                }
            }
        }
    }

    private List<CSSStyleSheetImpl.SelectorEntry> selects(
                            final CSSStyleSheetImpl.CSSStyleSheetRuleIndex index,
                            final SimpleScriptable scriptable,
                            final BrowserVersion browserVersion, final DomElement element,
                            final String pseudoElement, final boolean fromQuerySelectorAll) {

        final List<CSSStyleSheetImpl.SelectorEntry> matchingRules = new ArrayList<>();

        if (CSSStyleSheet.isActive(scriptable, index.getMediaList())) {
            final String elementName = element.getLowercaseName();
            final String[] classes = StringUtils.split(element.getAttributeDirect("class"), null, -1);
            final Iterator<CSSStyleSheetImpl.SelectorEntry> iter =
                    index.getSelectorEntriesIteratorFor(elementName, classes);

            CSSStyleSheetImpl.SelectorEntry entry = iter.next();
            while (null != entry) {
                if (CSSStyleSheet.selects(browserVersion, entry.getSelector(),
                                            element, pseudoElement, fromQuerySelectorAll)) {
                    matchingRules.add(entry);
                }
                entry = iter.next();
            }

            for (final CSSStyleSheetImpl.CSSStyleSheetRuleIndex child : index.getChildren()) {
                matchingRules.addAll(selects(child, scriptable, browserVersion,
                                                    element, pseudoElement, fromQuerySelectorAll));
            }
        }

        return matchingRules;
    }
}
