/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.AZIMUTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_COLLAPSE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_SPACING;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.CAPTION_SIDE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.COLOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.CURSOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.DIRECTION;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.ELEVATION;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.EMPTY_CELLS;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT_FAMILY;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT_SIZE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT_STYLE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT_VARIANT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT_WEIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LETTER_SPACING;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LINE_HEIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LIST_STYLE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LIST_STYLE_IMAGE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LIST_STYLE_POSITION;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LIST_STYLE_TYPE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.ORPHANS;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PITCH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PITCH_RANGE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.QUOTES;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.RICHNESS;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.SPEAK;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.SPEAK_HEADER;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.SPEAK_NUMERAL;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.SPEAK_PUNCTUATION;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.SPEECH_RATE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.STRESS;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.TEXT_ALIGN;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.TEXT_INDENT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.TEXT_TRANSFORM;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.VISIBILITY;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.VOICE_FAMILY;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.VOLUME;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.WHITE_SPACE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.WIDOWS;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.WORD_SPACING;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.EnumSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.css.sac.Selector;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

public class ComputedCSSStyleDeclaration2 extends CSSStyleDeclaration2 {

    /** Denotes a value which should be returned as is. */
    private static final String EMPTY_FINAL = new String("");

    /**
     * Local modifications maintained here rather than in the element. We use a sorted
     * map so that results are deterministic and thus easily testable.
     */
    private final SortedMap<String, StyleElement> localModifications_ = new TreeMap<>();

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
        VOICE_FAMILY,
        VOLUME,
        WHITE_SPACE,
        WIDOWS,
        WORD_SPACING);

    ComputedCSSStyleDeclaration2() {
        ScriptUtils.initialize(this);
    }

    /**
     * Creates an instance.
     *
     * @param style the original Style
     */
    public ComputedCSSStyleDeclaration2(final Global global, final CSSStyleDeclaration2 style) {
        super(style.getElement());
        ScriptUtils.initialize(this);
        setProto(global.getPrototype(getClass()));
    }

    public static ComputedCSSStyleDeclaration2 constructor(final boolean newObj, final Object self) {
        final ComputedCSSStyleDeclaration2 host = new ComputedCSSStyleDeclaration2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
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
        final StyleElement element = new StyleElement(name, newValue);
        localModifications_.put(name, element);
    }

    /**
     * Makes a local, "computed", modification to this CSS style.
     *
     * @param declaration the style declaration
     * @param selector the selector determining that the style applies to this element
     */
    public void applyStyleFromSelector(final org.w3c.dom.css.CSSStyleDeclaration declaration, final Selector selector) {
        final BrowserVersion browserVersion = getBrowserVersion();
        final SelectorSpecificity specificity = new SelectorSpecificity(selector);
        for (int k = 0; k < declaration.getLength(); k++) {
            final String name = declaration.item(k);
            final String value = declaration.getPropertyValue(name);
            final String priority = declaration.getPropertyPriority(name);
            if (!"z-index".equals(name) || !browserVersion.hasFeature(CSS_COMPUTED_NO_Z_INDEX)) {
                applyLocalStyleAttribute(name, value, priority, specificity);
            }
        }
    }

    private void applyLocalStyleAttribute(final String name, final String newValue, final String priority,
            final SelectorSpecificity specificity) {
        if (!PRIORITY_IMPORTANT.equals(priority)) {
            final StyleElement existingElement = localModifications_.get(name);
            if (existingElement != null) {
                if (PRIORITY_IMPORTANT.equals(existingElement.getPriority())) {
                    return; // can't override a !important rule by a normal rule. Ignore it!
                }
                else if (specificity.compareTo(existingElement.getSpecificity()) < 0) {
                    return; // can't override a rule with a rule having higher specificity
                }
            }
        }
        final StyleElement element = new StyleElement(name, newValue, priority, specificity, getCurrentElementIndex());
        localModifications_.put(name, element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColor() {
        final String value = defaultIfEmpty(super.getColor(), "rgb(0, 0, 0)", null);
        return toRGBColor(value);
    }

    @Override
    protected StyleElement getStyleElement(final String name) {
        final StyleElement existent = super.getStyleElement(name);

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
            if (PRIORITY_IMPORTANT.equals(localStyleMod.getPriority())) {
                if (PRIORITY_IMPORTANT.equals(existent.getPriority())) {
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

    private String defaultIfEmpty(final String str, final StyleAttributes.Definition definition) {
        return defaultIfEmpty(str, definition, false);
    }

    private String defaultIfEmpty(final String str, final StyleAttributes.Definition definition,
            final boolean isPixel) {
        if (!getElement().getDomNodeOrDie().isAttachedToPage()
                && getBrowserVersion().hasFeature(CSS_COMPUTED_NO_Z_INDEX)) {
            return EMPTY_FINAL;
        }
        if (str == null || str.isEmpty()) {
            return definition.getDefaultComputedValue(getBrowserVersion());
        }
        if (isPixel) {
            return pixelString(str);
        }
        return str;
    }

    /**
     * Returns the specified length value as a pixel length value, as long as we're not emulating IE.
     * This method does <b>NOT</b> handle percentages correctly; use {@link #pixelValue(Element2, CssValue)}
     * if you need percentage support).
     * @param value the length value to convert to a pixel length value
     * @return the specified length value as a pixel length value
     * @see #pixelString(Element2, CSSStyleDeclaration2.CssValue)
     */
    protected String pixelString(final String value) {
        if (value == EMPTY_FINAL || value.endsWith("px")) {
            return value;
        }
        return pixelValue(value) + "px";
    }

    /**
     * Returns the specified length CSS attribute value value as a pixel length value, as long as
     * we're not emulating IE. If the specified CSS attribute value is a percentage, this method
     * uses the specified value object to recursively retrieve the base (parent) CSS attribute value.
     * @param element the element for which the CSS attribute value is to be retrieved
     * @param value the CSS attribute value which is to be retrieved
     * @return the specified length CSS attribute value as a pixel length value
     * @see #pixelString(String)
     */
    protected String pixelString(final Element2 element, final CssValue value) {
        final String s = value.get(element);
        if (s.endsWith("px")) {
            return s;
        }
        return pixelValue(element, value) + "px";
    }

    /**
     * @param toReturnIfEmptyOrDefault the value to return if empty or equals the {@code defualtValue}
     * @param defaultValue the default value of the string
     * @return the string, or {@code toReturnIfEmptyOrDefault}
     */
    private String defaultIfEmpty(final String str, final String toReturnIfEmptyOrDefault, final String defaultValue) {
        if (!getElement().getDomNodeOrDie().isAttachedToPage()
                && getBrowserVersion().hasFeature(CSS_COMPUTED_NO_Z_INDEX)) {
            return EMPTY_FINAL;
        }
        if (str == null || str.isEmpty() || str.equals(defaultValue)) {
            return toReturnIfEmptyOrDefault;
        }
        return str;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final Definition style, final boolean getDefaultValueIfEmpty) {
        if (!getElement().getDomNodeOrDie().isAttachedToPage()
                && getBrowserVersion().hasFeature(CSS_COMPUTED_NO_Z_INDEX)) {
            return EMPTY_FINAL;
        }
        String value = super.getStyleAttribute(style, getDefaultValueIfEmpty);
        if (value.isEmpty()) {
            final Element2 parent = getElement().getParentElement();
            if (INHERITABLE_DEFINITIONS.contains(style) && parent != null) {
                value = Window2.getComputedStyle(getWindow(), parent, null).getStyleAttribute(style, getDefaultValueIfEmpty);
            }
            else if (getDefaultValueIfEmpty) {
                value = style.getDefaultComputedValue(getBrowserVersion());
            }
        }

        return value;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(ComputedCSSStyleDeclaration2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("ComputedCSSStyleDeclaration", 
                    staticHandle("constructor", ComputedCSSStyleDeclaration2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends PrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "ComputedCSSStyleDeclaration";
        }
    }
}
