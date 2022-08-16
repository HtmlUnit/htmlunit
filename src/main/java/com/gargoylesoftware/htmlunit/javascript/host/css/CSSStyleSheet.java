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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.Selector.SelectorType;
import com.gargoylesoftware.css.parser.selector.SelectorList;
import com.gargoylesoftware.css.parser.selector.SelectorListImpl;
import com.gargoylesoftware.css.parser.selector.SimpleSelector;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.css.CssStyleSheet;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

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

    /** The CSS import rules and their corresponding stylesheets. */
    private final Map<CSSImportRuleImpl, CSSStyleSheet> imports_ = new HashMap<>();

    /** cache parsed media strings */
    private static final transient Map<String, MediaListImpl> media_ = new HashMap<>();

    private boolean enabled_ = true;

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
     * Modifies the specified style object by adding any style rules which apply to the specified
     * element.
     *
     * @param style the style to modify
     * @param element the element to which style rules must apply in order for them to be added to
     *        the specified style
     * @param pseudoElement a string specifying the pseudo-element to match (may be {@code null})
     */
    public void modifyIfNecessary(final ComputedCSSStyleDeclaration style, final DomElement element,
            final String pseudoElement) {

        final BrowserVersion browser = getBrowserVersion();
        final List<CSSStyleSheetImpl.SelectorEntry> matchingRules =
                selects(getRuleIndex(), this, browser, element, pseudoElement, false);
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
        try {
            final CssStyleSheet css = CssStyleSheet.loadStylesheet(element.getDomNodeOrDie(), link, url);
            return new CSSStyleSheet(element, element.getWindow(), css);
        }
        catch (final RuntimeException e) {
            // Got something unexpected; we can throw an exception in this case.
            if (LOG.isErrorEnabled()) {
                LOG.error("RuntimeException loading " + url, e);
            }
            throw Context.reportRuntimeError("Exception: " + e);
        }
        catch (final Exception e) {
            // Got something unexpected; we can throw an exception in this case.
            if (LOG.isErrorEnabled()) {
                LOG.error("Exception loading " + url, e);
            }
            throw Context.reportRuntimeError("Exception: " + e);
        }
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
    static boolean isActive(final HtmlUnitScriptable scriptable, final MediaListImpl mediaList) {
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

    private static boolean isActive(final HtmlUnitScriptable scriptable, final MediaQuery mediaQuery) {
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

    private static double pixelValue(final CSSValueImpl cssValue, final HtmlUnitScriptable scriptable) {
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
                    dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
                    return (dpi / 25.4f) * cssValue.getDoubleValue();
                case QUATER:
                    // One quarter of a millimeter. 1Q = 1/40th of 1cm.
                    dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
                    return ((dpi / 25.4f) * cssValue.getDoubleValue()) / 4d;
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

    private CSSStyleSheetImpl.CSSStyleSheetRuleIndex getRuleIndex() {
        final CSSStyleSheetImpl styleSheet = getCssStyleSheet().getWrappedSheet();
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

                final CSSStyleSheet sheet = getImportedStyleSheet(importRule);

                if (!alreadyProcessing.contains(sheet.getUri())) {
                    final CSSRuleListImpl sheetRuleList = sheet.getCssStyleSheet().getWrappedSheet().getCssRules();
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
                            final HtmlUnitScriptable scriptable,
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
                if (CssStyleSheet.selects(browserVersion, entry.getSelector(),
                                            element, pseudoElement, fromQuerySelectorAll, false)) {
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

    public CSSStyleSheet getImportedStyleSheet(final CSSImportRuleImpl importRule) {
        CSSStyleSheet sheet = imports_.get(importRule);
        if (sheet == null) {
            final String href = importRule.getHref();
            final String url = UrlUtils.resolveUrl(getUri(), href);
            sheet = loadStylesheet(ownerNode_, null, url);
            imports_.put(importRule, sheet);
        }
        return sheet;
    }
}
