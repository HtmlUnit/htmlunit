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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.ABSOLUTE;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.AUTO;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.NONE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.AZIMUTH;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_COLLAPSE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_SPACING;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.CAPTION_SIDE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.COLOR;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.CURSOR;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.DIRECTION;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.DISPLAY;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.ELEVATION;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.EMPTY_CELLS;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.FONT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.FONT_FAMILY;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.FONT_SIZE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.FONT_STYLE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.FONT_VARIANT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.FONT_WEIGHT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.LETTER_SPACING;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.LINE_HEIGHT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.LIST_STYLE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.LIST_STYLE_IMAGE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.LIST_STYLE_POSITION;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.LIST_STYLE_TYPE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.ORPHANS;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.PITCH;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.PITCH_RANGE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.POSITION;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.QUOTES;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.RICHNESS;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.SPEAK;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.SPEAK_HEADER;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.SPEAK_NUMERAL;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.SPEAK_PUNCTUATION;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.SPEECH_RATE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.STRESS;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.TEXT_ALIGN;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.TEXT_INDENT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.TEXT_TRANSFORM;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.VISIBILITY;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.VOICE_FAMILY;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.VOLUME;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.WHITE_SPACE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.WIDOWS;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.WIDTH;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.WORD_SPACING;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * An object for a CSSStyleDeclaration, which is computed.
 *
 * @see com.gargoylesoftware.htmlunit.javascript.host.Window#getComputedStyle(Object, String)
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Alex Gorbatovsky
 * @author cd alexndr
 */
public class ComputedCssStyleDeclaration extends AbstractCssStyleDeclaration {

    /** The set of 'inheritable' definitions. */
    private static final Set<Definition> INHERITABLE_DEFINITIONS = EnumSet.of(
        AZIMUTH,
        BORDER_COLLAPSE,
        BORDER_SPACING,
        CAPTION_SIDE,
        COLOR,
        CURSOR,
        DIRECTION,
        ELEVATION,
        EMPTY_CELLS,
        FONT_FAMILY,
        FONT_SIZE,
        FONT_STYLE,
        FONT_VARIANT,
        FONT_WEIGHT,
        FONT,
        LETTER_SPACING,
        LINE_HEIGHT,
        LIST_STYLE_IMAGE,
        LIST_STYLE_POSITION,
        LIST_STYLE_TYPE,
        LIST_STYLE,
        ORPHANS,
        PITCH_RANGE,
        PITCH,
        QUOTES,
        RICHNESS,
        SPEAK_HEADER,
        SPEAK_NUMERAL,
        SPEAK_PUNCTUATION,
        SPEAK,
        SPEECH_RATE,
        STRESS,
        TEXT_ALIGN,
        TEXT_INDENT,
        TEXT_TRANSFORM,
        VISIBILITY,
        VOICE_FAMILY,
        VOLUME,
        WHITE_SPACE,
        WIDOWS,
        WORD_SPACING);

    /** Denotes a value which should be returned as is. */
    public static final String EMPTY_FINAL = new String("");

    /**
     * Local modifications maintained here rather than in the element. We use a sorted
     * map so that results are deterministic and thus easily testable.
     */
    private final SortedMap<String, StyleElement> localModifications_ = new TreeMap<>();

    /** The wrapped CSSStyleDeclaration */
    private ElementCssStyleDeclaration elementStyleDeclaration_;

    public ComputedCssStyleDeclaration(final ElementCssStyleDeclaration styleDeclaration) {
        elementStyleDeclaration_ = styleDeclaration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStylePriority(final String name) {
        return elementStyleDeclaration_.getStylePriority(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssText() {
        return elementStyleDeclaration_.getCssText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final String name) {
        final StyleElement element = getStyleElement(name);
        if (element != null && element.getValue() != null) {
            final String value = element.getValue();
            if (!value.contains("url")) {
                return value.toLowerCase(Locale.ROOT);
            }
            return value;
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final Definition definition, final boolean getDefaultValueIfEmpty) {
        final DomElement domElem = getDomElementOrNull();
        final BrowserVersion browserVersion = domElem.getPage().getWebClient().getBrowserVersion();

        if (!domElem.isAttachedToPage()
                && browserVersion.hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
            return EMPTY_FINAL;
        }

        String value = getStyleAttribute(definition.getAttributeName());
        if (value.isEmpty()) {
            final DomNode parent = domElem.getParentNode();
            if (parent instanceof DomElement && INHERITABLE_DEFINITIONS.contains(definition)) {
                final Window window = domElem.getPage().getEnclosingWindow().getScriptableObject();
                value = window.getWebWindow()
                        .getComputedStyle((DomElement) parent, null)
                        .getStyleAttribute(definition, getDefaultValueIfEmpty);
            }
            else if (getDefaultValueIfEmpty) {
                value = definition.getDefaultComputedValue(browserVersion);
            }
        }

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCssText(final String value) {
        // read only
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleAttribute(final String name, final String newValue, final String important) {
        // read only
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeStyleAttribute(final String name) {
        // read only
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return elementStyleDeclaration_.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object item(final int index) {
        return elementStyleDeclaration_.item(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCSSRuleImpl getParentRule() {
        return elementStyleDeclaration_.getParentRule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElement(final String name) {
        final StyleElement existent = elementStyleDeclaration_.getStyleElement(name);

        if (localModifications_ != null) {
            final StyleElement localStyleMod = localModifications_.get(name);
            if (localStyleMod == null) {
                return existent;
            }

            if (existent == null) {
                // Local modifications represent either default style elements or style elements
                // defined in stylesheets; either way, they shouldn't overwrite any style
                // elements derived directly from the HTML element's "style" attribute.
                return localStyleMod;
            }

            // replace if !IMPORTANT
            if (StyleElement.PRIORITY_IMPORTANT.equals(localStyleMod.getPriority())) {
                if (existent.isImportant()) {
                    if (existent.getSpecificity().compareTo(localStyleMod.getSpecificity()) < 0) {
                        return localStyleMod;
                    }
                }
                else {
                    return localStyleMod;
                }
            }
        }
        return existent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElementCaseInSensitive(final String name) {
        return elementStyleDeclaration_.getStyleElementCaseInSensitive(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, StyleElement> getStyleMap() {
        return elementStyleDeclaration_.getStyleMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element getElementOrNull() {
        return elementStyleDeclaration_.getElementOrNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getDomElementOrNull() {
        return elementStyleDeclaration_.getDomElementOrNull();
    }

    /**
     * @return the display setting
     */
    public String getDisplay() {
        final DomElement domElem = getDomElementOrNull();
        if (!domElem.isAttachedToPage()) {
            final BrowserVersion browserVersion = domElem.getPage().getWebClient().getBrowserVersion();
            if (browserVersion.hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
                return "";
            }
        }

        // don't use defaultIfEmpty for performance
        // (no need to calculate the default if not empty)
        final String value = getStyleAttribute(DISPLAY.getAttributeName());
        if (StringUtils.isEmpty(value)) {
            if (domElem instanceof HtmlElement) {
                return ((HtmlElement) domElem).getDefaultStyleDisplay().value();
            }
            return "";
        }
        return value;
    }

    /**
     * @return the width
     */
    public String getWidth() {
        if (NONE.equals(getDisplay())) {
            return AUTO;
        }

        final DomElement domElem = getDomElementOrNull();
        if (!domElem.isAttachedToPage()) {
            final BrowserVersion browserVersion = domElem.getPage().getWebClient().getBrowserVersion();
            if (browserVersion.hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
                return "";
            }
            if (getStyleAttribute(WIDTH, true).isEmpty()) {
                return AUTO;
            }
        }

        final int windowWidth = domElem.getPage().getEnclosingWindow().getInnerWidth();
        return ValueUtils.pixelString(domElem, new ValueUtils.CssValue(0, windowWidth) {
            @Override
            public String get(final ComputedCssStyleDeclaration style) {
                final String value = style.getStyleAttribute(WIDTH, true);
                if (StringUtils.isEmpty(value)) {
                    if (ABSOLUTE.equals(getStyleAttribute(POSITION, true))) {
                        final String content = domElem.getVisibleText();
                        // do this only for small content
                        // at least for empty div's this is more correct
                        if (null != content && content.length() < 13) {
                            return (content.length() * 7) + "px";
                        }
                    }

                    int windowDefaultValue = getWindowDefaultValue();
                    if (domElem instanceof HtmlBody) {
                        windowDefaultValue -= 16;
                    }
                    return windowDefaultValue + "px";
                }
                else if (AUTO.equals(value)) {
                    int windowDefaultValue = getWindowDefaultValue();
                    if (domElem instanceof HtmlBody) {
                        windowDefaultValue -= 16;
                    }
                    return windowDefaultValue + "px";
                }

                return value;
            }
        });
    }

    /**
     * Makes a local, "computed", modification to this CSS style.
     *
     * @param declaration the style declaration
     * @param selector the selector determining that the style applies to this element
     */
    public void applyStyleFromSelector(final CSSStyleDeclarationImpl declaration, final Selector selector) {
        final SelectorSpecificity specificity = selector.getSelectorSpecificity();
        for (final Property prop : declaration.getProperties()) {
            final String name = prop.getName();
            final String value = declaration.getPropertyValue(name);
            final String priority = declaration.getPropertyPriority(name);
            applyLocalStyleAttribute(name, value, priority, specificity);
        }
    }

    private void applyLocalStyleAttribute(final String name, final String newValue, final String priority,
            final SelectorSpecificity specificity) {
        if (!StyleElement.PRIORITY_IMPORTANT.equals(priority)) {
            final StyleElement existingElement = localModifications_.get(name);
            if (existingElement != null) {
                if (existingElement.isImportant()) {
                    return; // can't override a !important rule by a normal rule. Ignore it!
                }
                else if (specificity.compareTo(existingElement.getSpecificity()) < 0) {
                    return; // can't override a rule with a rule having higher specificity
                }
            }
        }
        final StyleElement element = new StyleElement(name, newValue, priority, specificity);
        localModifications_.put(name, element);
    }

    /**
     * Makes a local, "computed", modification to this CSS style that won't override other
     * style attributes of the same name. This method should be used to set default values
     * for style attributes.
     *
     * @param name the name of the style attribute to set
     * @param newValue the value of the style attribute to set
     */
    public void setDefaultLocalStyleAttribute(final String name, final String newValue) {
        final StyleElement element = new StyleElement(name, newValue, "", SelectorSpecificity.DEFAULT_STYLE_ATTRIBUTE);
        localModifications_.put(name, element);
    }
}
