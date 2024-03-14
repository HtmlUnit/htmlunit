/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.css;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.htmlunit.BrowserVersionFeatures.HTMLLINK_CHECK_TYPE_FOR_STYLESHEET;
import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.BrowserVersion;
import org.htmlunit.Cache;
import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.Page;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.WebClient.PooledCSS3Parser;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.WebWindow;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSImportRuleImpl;
import org.htmlunit.cssparser.dom.CSSMediaRuleImpl;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleDeclarationImpl;
import org.htmlunit.cssparser.dom.CSSStyleRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.dom.CSSValueImpl;
import org.htmlunit.cssparser.dom.CSSValueImpl.CSSPrimitiveValueType;
import org.htmlunit.cssparser.dom.MediaListImpl;
import org.htmlunit.cssparser.dom.Property;
import org.htmlunit.cssparser.parser.CSSErrorHandler;
import org.htmlunit.cssparser.parser.CSSException;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.condition.AttributeCondition;
import org.htmlunit.cssparser.parser.condition.Condition;
import org.htmlunit.cssparser.parser.condition.Condition.ConditionType;
import org.htmlunit.cssparser.parser.condition.NotPseudoClassCondition;
import org.htmlunit.cssparser.parser.javacc.CSS3Parser;
import org.htmlunit.cssparser.parser.media.MediaQuery;
import org.htmlunit.cssparser.parser.selector.ChildSelector;
import org.htmlunit.cssparser.parser.selector.DescendantSelector;
import org.htmlunit.cssparser.parser.selector.DirectAdjacentSelector;
import org.htmlunit.cssparser.parser.selector.ElementSelector;
import org.htmlunit.cssparser.parser.selector.GeneralAdjacentSelector;
import org.htmlunit.cssparser.parser.selector.PseudoElementSelector;
import org.htmlunit.cssparser.parser.selector.Selector;
import org.htmlunit.cssparser.parser.selector.Selector.SelectorType;
import org.htmlunit.cssparser.parser.selector.SelectorList;
import org.htmlunit.cssparser.parser.selector.SimpleSelector;
import org.htmlunit.html.DisabledElement;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.HtmlCheckBoxInput;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlLink;
import org.htmlunit.html.HtmlOption;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlRadioButtonInput;
import org.htmlunit.html.HtmlStyle;
import org.htmlunit.html.HtmlTextArea;
import org.htmlunit.html.ValidatableElement;
import org.htmlunit.javascript.host.css.MediaList;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.util.EncodingSniffer;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.UrlUtils;

/**
 * A css StyleSheet.
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Guy Burton
 * @author Frank Danek
 * @author Carsten Steul
 */
public class CssStyleSheet implements Serializable {

    /** "none". */
    public static final String NONE = "none";
    /** "auto". */
    public static final String AUTO = "auto";
    /** "static". */
    public static final String STATIC = "static";
    /** "inherit". */
    public static final String INHERIT = "inherit";
    /** "initial". */
    public static final String INITIAL = "initial";
    /** "relative". */
    public static final String RELATIVE = "relative";
    /** "fixed". */
    public static final String FIXED = "fixed";
    /** "absolute". */
    public static final String ABSOLUTE = "absolute";
    /** "repeat". */
    public static final String REPEAT = "repeat";
    /** "block". */
    public static final String BLOCK = "block";
    /** "inline". */
    public static final String INLINE = "inline";
    /** "scroll". */
    public static final String SCROLL = "scroll";

    private static final Log LOG = LogFactory.getLog(CssStyleSheet.class);

    private static final Pattern NTH_NUMERIC = Pattern.compile("\\d+");
    private static final Pattern NTH_COMPLEX = Pattern.compile("[+-]?\\d*n\\w*([+-]\\w\\d*)?");
    private static final Pattern UNESCAPE_SELECTOR = Pattern.compile("\\\\([\\[\\].:])");

    /** The parsed stylesheet which this host object wraps. */
    private final CSSStyleSheetImpl wrapped_;

    /** The HTML element which owns this stylesheet. */
    private final HtmlElement owner_;

    /** The CSS import rules and their corresponding stylesheets. */
    private final Map<CSSImportRuleImpl, CssStyleSheet> imports_ = new HashMap<>();

    /** cache parsed media strings */
    private static final transient Map<String, MediaListImpl> media_ = new HashMap<>();

    /** This stylesheet's URI (used to resolved contained @import rules). */
    private final String uri_;

    private boolean enabled_ = true;

    /**
     * Set of CSS2 pseudo class names.
     */
    public static final Set<String> CSS2_PSEUDO_CLASSES;

    private static final Set<String> CSS3_PSEUDO_CLASSES;

    /**
     * Set of CSS4 pseudo class names.
     */
    public static final Set<String> CSS4_PSEUDO_CLASSES;

    static {
        CSS2_PSEUDO_CLASSES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                "link", "visited", "hover", "active", "focus", "lang", "first-child")));

        final Set<String> css3 = new HashSet<>(Arrays.asList(
                "checked", "disabled", "enabled", "indeterminated", "root", "target", "not()",
                "nth-child()", "nth-last-child()", "nth-of-type()", "nth-last-of-type()",
                "last-child", "first-of-type", "last-of-type", "only-child", "only-of-type", "empty",
                "optional", "required", "valid", "invalid"));
        css3.addAll(CSS2_PSEUDO_CLASSES);
        CSS3_PSEUDO_CLASSES = Collections.unmodifiableSet(css3);

        final Set<String> css4 = new HashSet<>(Arrays.asList(
                // only what is supported at the moment
                "focus-within", "focus-visible"));
        css4.addAll(CSS3_PSEUDO_CLASSES);
        CSS4_PSEUDO_CLASSES = Collections.unmodifiableSet(css4);
    }

    /**
     * Creates a new stylesheet representing the CSS stylesheet for the specified input source.
     * @param element the owning node
     * @param source the input source which contains the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CssStyleSheet(final HtmlElement element, final InputSource source, final String uri) {
        if (source == null) {
            wrapped_ = new CSSStyleSheetImpl();
        }
        else {
            source.setURI(uri);
            wrapped_ = parseCSS(source, element.getPage().getWebClient());
        }
        uri_ = uri;
        owner_ = element;
    }

    /**
     * Creates a new stylesheet representing the CSS stylesheet for the specified input source.
     * @param element the owning node
     * @param styleSheet the source which contains the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CssStyleSheet(final HtmlElement element, final String styleSheet, final String uri) {
        CSSStyleSheetImpl css = null;
        try (InputSource source = new InputSource(new StringReader(styleSheet))) {
            source.setURI(uri);
            css = parseCSS(source, element.getPage().getWebClient());
        }
        catch (final IOException e) {
            LOG.error(e.getMessage(), e);
        }

        wrapped_ = css;
        uri_ = uri;
        owner_ = element;
    }

    /**
     * Creates a new stylesheet representing the specified CSS stylesheet.
     * @param element the owning node
     * @param wrapped the CSS stylesheet which this stylesheet host object represents
     * @param uri this stylesheet's URI (used to resolved contained @import rules)
     */
    public CssStyleSheet(final HtmlElement element, final CSSStyleSheetImpl wrapped, final String uri) {
        wrapped_ = wrapped;
        uri_ = uri;
        owner_ = element;
    }

    /**
     * Returns the wrapped stylesheet.
     * @return the wrapped stylesheet
     */
    public CSSStyleSheetImpl getWrappedSheet() {
        return wrapped_;
    }

    /**
     * Returns this stylesheet's URI (used to resolved contained @import rules).
     * For inline styles this is the page uri.
     * @return this stylesheet's URI (used to resolved contained @import rules)
     */
    public String getUri() {
        return uri_;
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
     * Loads the stylesheet at the specified link or href.
     * @param element the parent DOM element
     * @param link the stylesheet's link (may be {@code null} if a <code>url</code> is specified)
     * @param url the stylesheet's url (may be {@code null} if a <code>link</code> is specified)
     * @return the loaded stylesheet
     */
    public static CssStyleSheet loadStylesheet(final HtmlElement element, final HtmlLink link, final String url) {
        final HtmlPage page = (HtmlPage) element.getPage();
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
                // https://www.w3.org/TR/css-syntax-3/#input-byte-stream
                request.setDefaultResponseContentCharset(UTF_8);

                // our cache is a bit strange;
                // loadWebResponse check the cache for the web response
                // AND also fixes the request url for the following cache lookups
                response = client.loadWebResponse(request);
            }
            else {
                // Use link.
                request = link.getWebRequest();

                if (client.getBrowserVersion().hasFeature(HTMLLINK_CHECK_TYPE_FOR_STYLESHEET)) {
                    final String type = link.getTypeAttribute();
                    if (StringUtils.isNotBlank(type) && !MimeType.TEXT_CSS.equals(type)) {
                        return new CssStyleSheet(element, "", uri);
                    }
                }

                // https://www.w3.org/TR/css-syntax-3/#input-byte-stream
                request.setDefaultResponseContentCharset(UTF_8);

                // our cache is a bit strange;
                // loadWebResponse check the cache for the web response
                // AND also fixes the request url for the following cache lookups
                response = link.getWebResponse(true, request);
            }

            // now we can look into the cache with the fixed request for
            // a cached style sheet
            final Cache cache = client.getCache();
            final Object fromCache = cache.getCachedObject(request);
            if (fromCache instanceof CSSStyleSheetImpl) {
                uri = request.getUrl().toExternalForm();
                return new CssStyleSheet(element, (CSSStyleSheetImpl) fromCache, uri);
            }

            uri = response.getWebRequest().getUrl().toExternalForm();
            client.printContentIfNecessary(response);
            client.throwFailingHttpStatusCodeExceptionIfNecessary(response);
            // CSS content must have downloaded OK; go ahead and build the corresponding stylesheet.

            final CssStyleSheet sheet;
            final String contentType = response.getContentType();
            if (StringUtils.isEmpty(contentType) || MimeType.TEXT_CSS.equals(contentType)) {

                final InputStream in = response.getContentAsStreamWithBomIfApplicable();
                if (in == null) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Loading stylesheet for url '" + uri + "' returns empty responseData");
                    }
                    return new CssStyleSheet(element, "", uri);
                }
                try {
                    Charset cssEncoding = response.getContentCharset();
                    try (InputSource source = new InputSource(new InputStreamReader(in, cssEncoding))) {
                        source.setURI(uri);
                        sheet = new CssStyleSheet(element, source, uri);
                    }
                }
                finally {
                    in.close();
                }
            }
            else {
                sheet = new CssStyleSheet(element, "", uri);
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
            return new CssStyleSheet(element, "", uri);
        }
        catch (final IOException e) {
            // Got a basic IO error; behave nicely.
            if (LOG.isErrorEnabled()) {
                LOG.error("IOException loading " + uri, e);
            }
            return new CssStyleSheet(element, "", uri);
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
     * @param throwOnSyntax throw exception if the selector syntax is incorrect
     * @return {@code true} if it does apply, {@code false} if it doesn't apply
     */
    public static boolean selects(final BrowserVersion browserVersion, final Selector selector,
            final DomElement element, final String pseudoElement, final boolean fromQuerySelectorAll,
            final boolean throwOnSyntax) {
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
                            if (!selects(browserVersion, condition, element, fromQuerySelectorAll, throwOnSyntax)) {
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
                return selects(browserVersion, cs.getSimpleSelector(), element, pseudoElement,
                            fromQuerySelectorAll, throwOnSyntax)
                    && selects(browserVersion, cs.getAncestorSelector(), (DomElement) parentNode,
                            pseudoElement, fromQuerySelectorAll, throwOnSyntax);

            case DESCENDANT_SELECTOR:
                final DescendantSelector ds = (DescendantSelector) selector;
                final SimpleSelector simpleSelector = ds.getSimpleSelector();
                if (selects(browserVersion, simpleSelector, element, pseudoElement,
                            fromQuerySelectorAll, throwOnSyntax)) {
                    DomNode ancestor = element;
                    if (simpleSelector.getSelectorType() != SelectorType.PSEUDO_ELEMENT_SELECTOR) {
                        ancestor = ancestor.getParentNode();
                    }
                    final Selector dsAncestorSelector = ds.getAncestorSelector();
                    while (ancestor instanceof DomElement) {
                        if (selects(browserVersion, dsAncestorSelector, (DomElement) ancestor, pseudoElement,
                                fromQuerySelectorAll, throwOnSyntax)) {
                            return true;
                        }
                        ancestor = ancestor.getParentNode();
                    }
                }
                return false;

            case DIRECT_ADJACENT_SELECTOR:
                final DirectAdjacentSelector das = (DirectAdjacentSelector) selector;
                if (selects(browserVersion, das.getSimpleSelector(), element, pseudoElement,
                            fromQuerySelectorAll, throwOnSyntax)) {
                    DomNode prev = element.getPreviousSibling();
                    while (prev != null && !(prev instanceof DomElement)) {
                        prev = prev.getPreviousSibling();
                    }
                    return prev != null
                            && selects(browserVersion, das.getSelector(),
                                    (DomElement) prev, pseudoElement, fromQuerySelectorAll, throwOnSyntax);
                }
                return false;

            case GENERAL_ADJACENT_SELECTOR:
                final GeneralAdjacentSelector gas = (GeneralAdjacentSelector) selector;
                if (selects(browserVersion, gas.getSimpleSelector(), element, pseudoElement,
                            fromQuerySelectorAll, throwOnSyntax)) {
                    for (DomNode prev1 = element.getPreviousSibling(); prev1 != null;
                                                        prev1 = prev1.getPreviousSibling()) {
                        if (prev1 instanceof DomElement
                            && selects(browserVersion, gas.getSelector(), (DomElement) prev1,
                                    pseudoElement, fromQuerySelectorAll, throwOnSyntax)) {
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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * Returns {@code true} if the specified condition selects the specified element.
     *
     * @param browserVersion the browser version
     * @param condition the condition to test
     * @param element the element to test
     * @param fromQuerySelectorAll whether this is called from {@link DomNode#querySelectorAll(String)}
     * @param throwOnSyntax throw exception if the selector syntax is incorrect
     * @return {@code true} if it does apply, {@code false} if it doesn't apply
     */
    static boolean selects(final BrowserVersion browserVersion,
            final Condition condition, final DomElement element,
            final boolean fromQuerySelectorAll, final boolean throwOnSyntax) {

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
                final AttributeCondition attributeCondition = (AttributeCondition) condition;
                String value = attributeCondition.getValue();
                if (value != null) {
                    if (value.indexOf('\\') > -1) {
                        value = UNESCAPE_SELECTOR.matcher(value).replaceAll("$1");
                    }
                    final String name = attributeCondition.getLocalName();
                    final String attrValue = element.getAttribute(name);
                    if (attributeCondition.isCaseInSensitive() || DomElement.TYPE_ATTRIBUTE.equals(name)) {
                        return ATTRIBUTE_NOT_DEFINED != attrValue && attrValue.equalsIgnoreCase(value);
                    }
                    return ATTRIBUTE_NOT_DEFINED != attrValue && attrValue.equals(value);
                }
                return element.hasAttribute(condition.getLocalName());

            case PREFIX_ATTRIBUTE_CONDITION:
                final AttributeCondition prefixAttributeCondition = (AttributeCondition) condition;
                final String prefixValue = prefixAttributeCondition.getValue();
                if (prefixAttributeCondition.isCaseInSensitive()) {
                    return !"".equals(prefixValue)
                            && StringUtils.startsWithIgnoreCase(
                                    element.getAttribute(prefixAttributeCondition.getLocalName()), prefixValue);
                }
                return !"".equals(prefixValue)
                        && element.getAttribute(prefixAttributeCondition.getLocalName()).startsWith(prefixValue);

            case SUFFIX_ATTRIBUTE_CONDITION:
                final AttributeCondition suffixAttributeCondition = (AttributeCondition) condition;
                final String suffixValue = suffixAttributeCondition.getValue();
                if (suffixAttributeCondition.isCaseInSensitive()) {
                    return !"".equals(suffixValue)
                            && StringUtils.endsWithIgnoreCase(
                                    element.getAttribute(suffixAttributeCondition.getLocalName()), suffixValue);
                }
                return !"".equals(suffixValue)
                        && element.getAttribute(suffixAttributeCondition.getLocalName()).endsWith(suffixValue);

            case SUBSTRING_ATTRIBUTE_CONDITION:
                final AttributeCondition substringAttributeCondition = (AttributeCondition) condition;
                final String substringValue = substringAttributeCondition.getValue();
                if (substringAttributeCondition.isCaseInSensitive()) {
                    return !"".equals(substringValue)
                            && StringUtils.containsIgnoreCase(
                                    element.getAttribute(substringAttributeCondition.getLocalName()), substringValue);
                }
                return !"".equals(substringValue)
                        && element.getAttribute(substringAttributeCondition.getLocalName()).contains(substringValue);

            case BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
                final AttributeCondition beginHyphenAttributeCondition = (AttributeCondition) condition;
                final String v = beginHyphenAttributeCondition.getValue();
                final String a = element.getAttribute(beginHyphenAttributeCondition.getLocalName());
                if (beginHyphenAttributeCondition.isCaseInSensitive()) {
                    return selectsHyphenSeparated(
                            org.htmlunit.util.StringUtils.toRootLowerCase(v),
                            org.htmlunit.util.StringUtils.toRootLowerCase(a));
                }
                return selectsHyphenSeparated(v, a);

            case ONE_OF_ATTRIBUTE_CONDITION:
                final AttributeCondition oneOfAttributeCondition = (AttributeCondition) condition;
                final String v2 = oneOfAttributeCondition.getValue();
                final String a2 = element.getAttribute(oneOfAttributeCondition.getLocalName());
                if (oneOfAttributeCondition.isCaseInSensitive()) {
                    return selectsOneOf(
                            org.htmlunit.util.StringUtils.toRootLowerCase(v2),
                            org.htmlunit.util.StringUtils.toRootLowerCase(a2));
                }
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

            case NOT_PSEUDO_CLASS_CONDITION:
                final NotPseudoClassCondition notPseudoCondition = (NotPseudoClassCondition) condition;
                final SelectorList selectorList = notPseudoCondition.getSelectors();
                for (final Selector selector : selectorList) {
                    if (selects(browserVersion, selector, element, null, fromQuerySelectorAll, throwOnSyntax)) {
                        return false;
                    }
                }
                return true;

            case PSEUDO_CLASS_CONDITION:
                return selectsPseudoClass(browserVersion, condition, element);

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
            return '-' == attribute.charAt(conditionLength)
                    && attribute.startsWith(condition);
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
            final Condition condition, final DomElement element) {
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

            case "focus-within":
                final HtmlPage htmlPage2 = element.getHtmlPageOrNull();
                if (htmlPage2 != null) {
                    final DomElement focus = htmlPage2.getFocusedElement();
                    return element == focus || element.isAncestorOf(focus);
                }
                return false;

            case "focus-visible":
                final HtmlPage htmlPage3 = element.getHtmlPageOrNull();
                if (htmlPage3 != null) {
                    final DomElement focus = htmlPage3.getFocusedElement();
                    return element == focus
                            && ((element instanceof HtmlInput && !((HtmlInput) element).isReadOnly())
                                || (element instanceof HtmlTextArea && !((HtmlTextArea) element).isReadOnly()));
                }
                return false;

            case "checked":
                return (element instanceof HtmlCheckBoxInput && ((HtmlCheckBoxInput) element).isChecked())
                        || (element instanceof HtmlRadioButtonInput && ((HtmlRadioButtonInput) element).isChecked()
                                || (element instanceof HtmlOption && ((HtmlOption) element).isSelected()));

            case "required":
                return element instanceof HtmlElement && ((HtmlElement) element).isRequired();

            case "optional":
                return element instanceof HtmlElement && ((HtmlElement) element).isOptional();

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

            case "valid":
                if (element instanceof HtmlForm || element instanceof ValidatableElement) {
                    return ((HtmlElement) element).isValid();
                }
                return false;

            case "invalid":
                if (element instanceof HtmlForm || element instanceof ValidatableElement) {
                    return !((HtmlElement) element).isValid();
                }
                return false;

            case "empty":
                return isEmpty(element);

            case "target":
                final String ref = element.getPage().getUrl().getRef();
                return StringUtils.isNotBlank(ref) && ref.equals(element.getId());

            case "hover":
                return element.isMouseOver();

            case "placeholder-shown":
                return element instanceof HtmlInput
                        && StringUtils.isEmpty(((HtmlInput) element).getValue())
                        && StringUtils.isNotEmpty(((HtmlInput) element).getPlaceholder());

            default:
                if (value.startsWith("nth-child(")) {
                    final String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
                    int index = 0;
                    for (DomNode n = element; n != null; n = n.getPreviousSibling()) {
                        if (n instanceof DomElement) {
                            index++;
                        }
                    }
                    return getNthElement(nth, index);
                }
                else if (value.startsWith("nth-last-child(")) {
                    final String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
                    int index = 0;
                    for (DomNode n = element; n != null; n = n.getNextSibling()) {
                        if (n instanceof DomElement) {
                            index++;
                        }
                    }
                    return getNthElement(nth, index);
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
                    return getNthElement(nth, index);
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
                    return getNthElement(nth, index);
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

    private static boolean getNthElement(final String nth, final int index) {
        if ("odd".equalsIgnoreCase(nth)) {
            return index % 2 != 0;
        }

        if ("even".equalsIgnoreCase(nth)) {
            return index % 2 == 0;
        }

        // (numerator) * n + (denominator)
        final int nIndex = nth.indexOf('n');
        int denominator = 0;
        if (nIndex != -1) {
            String value = nth.substring(0, nIndex).trim();
            if ("-".equals(value)) {
                denominator = -1;
            }
            else {
                if (value.length() > 0 && value.charAt(0) == '+') {
                    value = value.substring(1);
                }
                denominator = NumberUtils.toInt(value, 1);
            }
        }

        String value = nth.substring(nIndex + 1).trim();
        if (value.length() > 0 && value.charAt(0) == '+') {
            value = value.substring(1);
        }
        final int numerator = NumberUtils.toInt(value, 0);
        if (denominator == 0) {
            return index == numerator && numerator > 0;
        }

        final double n = (index - numerator) / (double) denominator;
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

        // use a pooled parser, if any available to avoid expensive recreation
        try (PooledCSS3Parser pooledParser = client.getCSS3Parser()) {
            final CSSErrorHandler errorHandler = client.getCssErrorHandler();
            final CSSOMParser parser = new CSSOMParser(pooledParser);
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
     * Parses the given media string. If anything at all goes wrong, this
     * method returns an empty MediaList list.
     *
     * @param mediaString the source from which to retrieve the media to be parsed
     * @param webClient the {@link WebClient} to be used
     * @return the media parsed from the specified input source
     */
    public static MediaListImpl parseMedia(final String mediaString, final WebClient webClient) {
        MediaListImpl media = media_.get(mediaString);
        if (media != null) {
            return media;
        }

        // get us a pooled parser for efficiency because a new parser is expensive
        try (PooledCSS3Parser pooledParser = webClient.getCSS3Parser()) {
            final CSSOMParser parser = new CSSOMParser(pooledParser);
            parser.setErrorHandler(webClient.getCssErrorHandler());

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
     * Parses the given media string. If anything at all goes wrong, this
     * method returns an empty MediaList list.
     *
     * @param mediaString the source from which to retrieve the media to be parsed
     * @param errorHandler the {@link CSSErrorHandler} to be used
     * @return the media parsed from the specified input source
     *
     * @deprecated as of version 3.8.0; use {@link #parseMedia(String, WebClient)} instead
     */
    @Deprecated
    public static MediaListImpl parseMedia(final CSSErrorHandler errorHandler, final String mediaString) {
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
     * Validates the list of selectors.
     * @param selectorList the selectors
     * @param documentMode see {@link Document#getDocumentMode()}
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
     * @param documentMode see {@link Document#getDocumentMode()}
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
     * @param documentMode see {@link Document#getDocumentMode()}
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
            case NOT_PSEUDO_CLASS_CONDITION:
                final NotPseudoClassCondition notPseudoCondition = (NotPseudoClassCondition) condition;
                final SelectorList selectorList = notPseudoCondition.getSelectors();
                for (final Selector selector : selectorList) {
                    if (!isValidSelector(selector, documentMode, domNode)) {
                        return false;
                    }
                }
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

                if ("nth-child()".equals(value)) {
                    final String arg = StringUtils.substringBetween(condition.getValue(), "(", ")").trim();
                    return "even".equalsIgnoreCase(arg) || "odd".equalsIgnoreCase(arg)
                            || NTH_NUMERIC.matcher(arg).matches()
                            || NTH_COMPLEX.matcher(arg).matches();
                }

                if ("placeholder-shown".equals(value)) {
                    return true;
                }

                return CSS4_PSEUDO_CLASSES.contains(value);
            default:
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Unhandled CSS condition type '"
                                + condition.getConditionType() + "'. Accepting it silently.");
                }
                return true;
        }
    }

    public CssStyleSheet getImportedStyleSheet(final CSSImportRuleImpl importRule) {
        CssStyleSheet sheet = imports_.get(importRule);
        if (sheet == null) {
            final String href = importRule.getHref();
            final String url = UrlUtils.resolveUrl(getUri(), href);
            sheet = loadStylesheet(owner_, null, url);
            imports_.put(importRule, sheet);
        }
        return sheet;
    }

    /**
     * Returns {@code true} if this stylesheet is active, based on the media types it is associated with (if any).
     * @return {@code true} if this stylesheet is active, based on the media types it is associated with (if any)
     */
    public boolean isActive() {
        final String media;
        if (owner_ instanceof HtmlStyle) {
            final HtmlStyle style = (HtmlStyle) owner_;
            media = style.getMediaAttribute();
        }
        else if (owner_ instanceof HtmlLink) {
            final HtmlLink link = (HtmlLink) owner_;
            media = link.getMediaAttribute();
        }
        else {
            return true;
        }

        if (StringUtils.isBlank(media)) {
            return true;
        }

        final WebWindow webWindow = owner_.getPage().getEnclosingWindow();
        final MediaListImpl mediaList = parseMedia(media, webWindow.getWebClient());
        return isActive(mediaList, webWindow);
    }

    /**
     * Returns whether the specified {@link MediaList} is active or not.
     * @param mediaList the media list
     * @param webWindow the {@link WebWindow} for some basic data
     * @return whether the specified {@link MediaList} is active or not
     */
    public static boolean isActive(final MediaListImpl mediaList, final WebWindow webWindow) {
        if (mediaList.getLength() == 0) {
            return true;
        }

        for (int i = 0; i < mediaList.getLength(); i++) {
            final MediaQuery mediaQuery = mediaList.mediaQuery(i);
            boolean isActive = isActive(mediaQuery, webWindow);
            if (mediaQuery.isNot()) {
                isActive = !isActive;
            }
            if (isActive) {
                return true;
            }
        }
        return false;
    }

    private static boolean isActive(final MediaQuery mediaQuery, final WebWindow webWindow) {
        final String mediaType = mediaQuery.getMedia();
        if ("screen".equalsIgnoreCase(mediaType) || "all".equalsIgnoreCase(mediaType)) {
            for (final Property property : mediaQuery.getProperties()) {
                final double val;
                switch (property.getName()) {
                    case "max-width":
                        val = pixelValue(property.getValue(), webWindow);
                        if (val == -1 || val < webWindow.getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "min-width":
                        val = pixelValue(property.getValue(), webWindow);
                        if (val == -1 || val > webWindow.getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "max-device-width":
                        val = pixelValue(property.getValue(), webWindow);
                        if (val == -1 || val < webWindow.getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "min-device-width":
                        val = pixelValue(property.getValue(), webWindow);
                        if (val == -1 || val > webWindow.getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "max-height":
                        val = pixelValue(property.getValue(), webWindow);
                        if (val == -1 || val < webWindow.getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "min-height":
                        val = pixelValue(property.getValue(), webWindow);
                        if (val == -1 || val > webWindow.getInnerWidth()) {
                            return false;
                        }
                        break;

                    case "max-device-height":
                        val = pixelValue(property.getValue(), webWindow);
                        if (val == -1 || val < webWindow.getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "min-device-height":
                        val = pixelValue(property.getValue(), webWindow);
                        if (val == -1 || val > webWindow.getScreen().getWidth()) {
                            return false;
                        }
                        break;

                    case "resolution":
                        final CSSValueImpl propValue = property.getValue();
                        val = resolutionValue(propValue);
                        if (propValue == null) {
                            return true;
                        }
                        if (val == -1 || Math.round(val) != webWindow.getScreen().getDeviceXDPI()) {
                            return false;
                        }
                        break;

                    case "max-resolution":
                        val = resolutionValue(property.getValue());
                        if (val == -1 || val < webWindow.getScreen().getDeviceXDPI()) {
                            return false;
                        }
                        break;

                    case "min-resolution":
                        val = resolutionValue(property.getValue());
                        if (val == -1 || val > webWindow.getScreen().getDeviceXDPI()) {
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
                        if ("portrait".equals(orient)) {
                            if (webWindow.getInnerWidth() > webWindow.getInnerHeight()) {
                                return false;
                            }
                        }
                        else if ("landscape".equals(orient)) {
                            if (webWindow.getInnerWidth() < webWindow.getInnerHeight()) {
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
        else if ("print".equalsIgnoreCase(mediaType)) {
            final Page page = webWindow.getEnclosedPage();
            if (page instanceof SgmlPage) {
                return ((SgmlPage) page).isPrinting();
            }
        }
        return false;
    }

    private static double pixelValue(final CSSValueImpl cssValue, final WebWindow webWindow) {
        if (cssValue == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("CSSValue is null but has to be a 'px', 'em', '%', 'ex', 'ch', "
                        + "'vw', 'vh', 'vmin', 'vmax', 'rem', 'mm', 'cm', 'Q', or 'pt' value.");
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
                    dpi = webWindow.getScreen().getDeviceXDPI();
                    return (dpi / 25.4f) * cssValue.getDoubleValue();
                case QUATER:
                    // One quarter of a millimeter. 1Q = 1/40th of 1cm.
                    dpi = webWindow.getScreen().getDeviceXDPI();
                    return ((dpi / 25.4f) * cssValue.getDoubleValue()) / 4d;
                case CENTIMETER:
                    dpi = webWindow.getScreen().getDeviceXDPI();
                    return (dpi / 254f) * cssValue.getDoubleValue();
                case POINT:
                    dpi = webWindow.getScreen().getDeviceXDPI();
                    return (dpi / 72f) * cssValue.getDoubleValue();
                default:
                    break;
            }
        }
        if (LOG.isWarnEnabled()) {
            LOG.warn("CSSValue '" + cssValue.getCssText()
                        + "' has to be a 'px', 'em', '%', 'ex', 'ch', "
                        + "'vw', 'vh', 'vmin', 'vmax', 'rem', 'mm', 'cm', 'Q', or 'pt' value.");
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
     * Modifies the specified style object by adding any style rules which apply to the specified
     * element.
     *
     * @param style the style to modify
     * @param element the element to which style rules must apply in order for them to be added to
     *        the specified style
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null})
     */
    public void modifyIfNecessary(final ComputedCssStyleDeclaration style, final DomElement element,
            final String pseudoElement) {

        final BrowserVersion browser = element.getPage().getWebClient().getBrowserVersion();
        final List<CSSStyleSheetImpl.SelectorEntry> matchingRules =
                selects(getRuleIndex(), browser, element, pseudoElement, false);
        for (final CSSStyleSheetImpl.SelectorEntry entry : matchingRules) {
            final CSSStyleDeclarationImpl dec = entry.getRule().getStyle();
            style.applyStyleFromSelector(dec, entry.getSelector());
        }
    }

    private CSSStyleSheetImpl.CSSStyleSheetRuleIndex getRuleIndex() {
        final CSSStyleSheetImpl styleSheet = getWrappedSheet();
        CSSStyleSheetImpl.CSSStyleSheetRuleIndex index = styleSheet.getRuleIndex();

        if (index == null) {
            index = new CSSStyleSheetImpl.CSSStyleSheetRuleIndex();
            final CSSRuleListImpl ruleList = styleSheet.getCssRules();
            index(index, ruleList, new HashSet<>());

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

                final CssStyleSheet sheet = getImportedStyleSheet(importRule);

                if (!alreadyProcessing.contains(sheet.getUri())) {
                    final CSSRuleListImpl sheetRuleList = sheet.getWrappedSheet().getCssRules();
                    alreadyProcessing.add(sheet.getUri());

                    final MediaListImpl mediaList = importRule.getMedia();
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
                            final BrowserVersion browserVersion, final DomElement element,
                            final String pseudoElement, final boolean fromQuerySelectorAll) {

        final List<CSSStyleSheetImpl.SelectorEntry> matchingRules = new ArrayList<>();

        if (isActive(index.getMediaList(), element.getPage().getEnclosingWindow())) {
            final String elementName = element.getLowercaseName();
            final String[] classes = org.htmlunit.util.StringUtils.splitAtJavaWhitespace(
                                                            element.getAttributeDirect("class"));
            final Iterator<CSSStyleSheetImpl.SelectorEntry> iter =
                    index.getSelectorEntriesIteratorFor(elementName, classes);

            CSSStyleSheetImpl.SelectorEntry entry = iter.next();
            while (null != entry) {
                if (CssStyleSheet.selects(browserVersion, entry.getSelector(),
                                            element, pseudoElement, fromQuerySelectorAll, false)) {
                    matchingRules.add(entry);
                }
                entry = iter.next();
            }

            for (final CSSStyleSheetImpl.CSSStyleSheetRuleIndex child : index.getChildren()) {
                matchingRules.addAll(selects(child, browserVersion,
                                                    element, pseudoElement, fromQuerySelectorAll));
            }
        }

        return matchingRules;
    }
}
