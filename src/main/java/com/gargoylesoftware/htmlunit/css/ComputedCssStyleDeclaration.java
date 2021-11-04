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
package com.gargoylesoftware.htmlunit.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.AZIMUTH;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_COLLAPSE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_SPACING;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.CAPTION_SIDE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.COLOR;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.CURSOR;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.DIRECTION;
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
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.WORD_SPACING;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * A non-js CSSStyleDeclaration which is computed
 *
 */
public class ComputedCssStyleDeclaration implements CssStyleDeclaration {

    /** The set of 'inheritable' definitions. */
    private static final Set<String> INHERITABLE_DEFINITIONS = EnumSet.of(
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
        VOICE_FAMILY,
        VOLUME,
        WHITE_SPACE,
        WIDOWS,
        WORD_SPACING).stream()
            .map(Definition::getAttributeName)
            .collect(Collectors.toSet());

    private final ElementCssStyleDeclaration elementStyleDeclaration;

    /**
     * Local modifications maintained here rather than in the element. We use a sorted
     * map so that results are deterministic and thus easily testable.
     */
    private final SortedMap<String, StyleElement> localModifications_;

    public ComputedCssStyleDeclaration(ElementCssStyleDeclaration elementStyle) {
        this.elementStyleDeclaration = elementStyle;
        this.localModifications_ = null;
    }

    public ComputedCssStyleDeclaration(ElementCssStyleDeclaration elementStyle,
        Map<String, StyleElement> localModifications) {
        this.elementStyleDeclaration = elementStyle;
        this.localModifications_ = new TreeMap<>(localModifications);
    }

    public ElementCssStyleDeclaration getElementStyleDeclaration() {
        return elementStyleDeclaration;
    }

    private DomElement getDomElement(){
        return elementStyleDeclaration.getDomElement();
    }

    @Override
    public BrowserVersion getBrowserVersion() {
        return elementStyleDeclaration.getBrowserVersion();
    }

    @Override
    public StyleElement getStyleElement(String name) {
        if (!getDomElement().isAttachedToPage()
                && getBrowserVersion().hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
            return EMPTY_FINAl; //prevents getting default value
        }

        StyleElement result = elementStyleDeclaration.getStyleElement(name);

        if (localModifications_ != null) {
            final StyleElement localStyleMod = localModifications_.get(name);
            if (localStyleMod != null) {
                if (result == null) {
                    // Local modifications represent either default style elements or style elements
                    // defined in stylesheets; either way, they shouldn't overwrite any style
                    // elements derived directly from the HTML element's "style" attribute.
                    result = localStyleMod;
                } else {
                    // replace if !IMPORTANT
                    if (StyleElement.PRIORITY_IMPORTANT.equals(localStyleMod.getPriority())) {
                        if (StyleElement.PRIORITY_IMPORTANT.equals(result.getPriority())) {
                            if (result.getSpecificity().compareTo(localStyleMod.getSpecificity()) < 0) {
                                result = localStyleMod;
                            }
                        } else {
                            result = localStyleMod;
                        }
                    }
                }
            }
        }

        //resolve inherited styles
        if (result == null) {
            final DomNode parent = getDomElement().getParentNode();
            if (INHERITABLE_DEFINITIONS.contains(name) && parent instanceof DomElement) {
                result = getDomElement().getPage()
                        .getComputedStyle((DomElement) parent, null)
                        .getStyleElement(name);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * This method does nothing as the object is read-only.
     */
    @Override
    public void setStyleAttribute(String name, String newValue, String important) {
        //read-only
    }

    /**
     * {@inheritDoc}
     *
     * This method does nothing as the object is read-only.
     */
    @Override
    public String removeStyleAttribute(String name) {
        return "";
    }

    @Override
    public String getCssText() {
        return elementStyleDeclaration.getCssText();
    }

    /**
     * {@inheritDoc}
     *
     * This method does nothing as the object is read-only.
     */
    @Override
    public void setCssText(String value) {
        //read-only
    }

    @Override
    public List<Property> getProperties() {
        return elementStyleDeclaration.getProperties();
    }

    @Override
    public AbstractCSSRuleImpl getParentRule() {
        return elementStyleDeclaration.getParentRule();
    }
}
