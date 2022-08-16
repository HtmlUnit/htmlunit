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
package com.gargoylesoftware.htmlunit.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_PSEUDO_SELECTOR_MS_PLACEHHOLDER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_PSEUDO_SELECTOR_PLACEHOLDER_SHOWN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLLINK_CHECK_TYPE_FOR_STYLESHEET;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTOR_CSS3_PSEUDO_REQUIRE_ATTACHED_NODE;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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

import com.gargoylesoftware.css.dom.CSSImportRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.css.parser.InputSource;
import com.gargoylesoftware.css.parser.condition.Condition;
import com.gargoylesoftware.css.parser.javacc.CSS3Parser;
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
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.MimeType;

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

    private static final Log LOG = LogFactory.getLog(CssStyleSheet.class);
    private static final Pattern NTH_NUMERIC = Pattern.compile("\\d+");
    private static final Pattern NTH_COMPLEX = Pattern.compile("[+-]?\\d*n\\w*([+-]\\w\\d*)?");
    private static final Pattern UNESCAPE_SELECTOR = Pattern.compile("\\\\([\\[\\].:])");

    /** The parsed stylesheet which this host object wraps. */
    private final CSSStyleSheetImpl wrapped_;

    /** The HTML element which owns this stylesheet. */
    private final HtmlElement owner_;

    /** The collection of rules defined in this style sheet. */
    private com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList cssRules_;
    private List<Integer> cssRulesIndexFix_;

    /** The CSS import rules and their corresponding stylesheets. */
    private final Map<CSSImportRuleImpl, CssStyleSheet> imports_ = new HashMap<>();

    /** cache parsed media strings */
    private static final transient Map<String, MediaListImpl> media_ = new HashMap<>();

    /** This stylesheet's URI (used to resolved contained @import rules). */
    private final String uri_;

    private final boolean enabled_ = true;

    /**
     * Set of CSS2 pseudo class names.
     */
    public static final Set<String> CSS2_PSEUDO_CLASSES = new HashSet<>(Arrays.asList(
            "link", "visited", "hover", "active",
            "focus", "lang", "first-child"));

    private static final Set<String> CSS3_PSEUDO_CLASSES = new HashSet<>(Arrays.asList(
            "checked", "disabled", "enabled", "indeterminated", "root", "target", "not()",
            "nth-child()", "nth-last-child()", "nth-of-type()", "nth-last-of-type()",
            "last-child", "first-of-type", "last-of-type", "only-child", "only-of-type", "empty",
            "optional", "required", "valid", "invalid"));

    /**
     * Set of CSS4 pseudo class names.
     */
    public static final Set<String> CSS4_PSEUDO_CLASSES = new HashSet<>(Arrays.asList(
            // only what is supported at the moment
            "focus-within", "focus-visible"));

    static {
        CSS3_PSEUDO_CLASSES.addAll(CSS2_PSEUDO_CLASSES);
        CSS4_PSEUDO_CLASSES.addAll(CSS3_PSEUDO_CLASSES);
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
     * Loads the stylesheet at the specified link or href.
     * @param element the parent DOM element
     * @param link the stylesheet's link (may be {@code null} if a <tt>url</tt> is specified)
     * @param url the stylesheet's url (may be {@code null} if a <tt>link</tt> is specified)
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
    // TODO make (package) private again
    public static boolean selects(final BrowserVersion browserVersion,
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
                return selectsPseudoClass(browserVersion, condition, element, fromQuerySelectorAll, throwOnSyntax);

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
            final Condition condition, final DomElement element, final boolean fromQuerySelectorAll,
            final boolean throwOnSyntax) {
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
                return element instanceof HtmlElement && ((HtmlElement) element).isValid();

            case "invalid":
                return element instanceof HtmlElement && !((HtmlElement) element).isValid();

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
                            && StringUtils.isEmpty(((HtmlInput) element).getValue())
                            && StringUtils.isNotEmpty(((HtmlInput) element).getPlaceholder());
                }

            case "-ms-input-placeholder":
                if (browserVersion.hasFeature(CSS_PSEUDO_SELECTOR_MS_PLACEHHOLDER)) {
                    return element instanceof HtmlInput
                            && StringUtils.isEmpty(((HtmlInput) element).getValue())
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
                            if (throwOnSyntax) {
                                throw new CSSException("Invalid selectors: " + selectors);
                            }
                            return false;
                        }

                        for (final Selector selector : selectorList) {
                            if (!isValidSelector(selector, 9, element)) {
                                if (throwOnSyntax) {
                                    throw new CSSException("Invalid selector: " + selector);
                                }
                                return false;
                            }
                        }

                        return !selects(browserVersion, selectorList.get(0), element,
                                null, fromQuerySelectorAll, throwOnSyntax);
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
            final CSSErrorHandler errorHandler = owner_.getPage().getWebClient().getCssErrorHandler();
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

                return CSS4_PSEUDO_CLASSES.contains(value);
            default:
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Unhandled CSS condition type '"
                                + condition.getConditionType() + "'. Accepting it silently.");
                }
                return true;
        }
    }
}
