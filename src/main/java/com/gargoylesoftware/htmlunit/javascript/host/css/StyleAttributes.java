/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.chrome;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ff;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ff31up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ff38up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ffBelow31;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ffBelow38;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ie;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ie11up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ie8up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ieBelow11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Contains information about the style attribute defined for different browser as well as their default values.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ahmed Ashour
 */
final class StyleAttributes {
    private static final Map<String, Definition> styles_ = new HashMap<>();

    private StyleAttributes() {
        // nothing
    }

    /**
     * Gets the style attributes definition with the given name for the specified browser version.
     * @param browserVersion the browser version
     * @return <code>null</code> if no definition exists for this browser version
     */
    public static Definition getDefinition(final String propertyName, final BrowserVersion browserVersion) {
        if (browserVersion == null) {
            return null;
        }
        final Definition definition = styles_.get(propertyName);
        if (definition == null) {
            return null;
        }
        if (!definition.isAvailable(browserVersion)) {
            return null;
        }
        return definition;
    }

    /**
     * Gets the style attributes definitions for the specified browser version.
     * @param browserVersion the browser version
     * @return the list
     */
    public static List<Definition> getDefinitions(final BrowserVersion browserVersion) {
        final List<Definition> list = new ArrayList<>();
        for (final Definition definition : Definition.values()) {
            if (definition.isAvailable(browserVersion)) {
                list.add(definition);
            }
        }

        return list;
    }

    /**
     * Holds information about a style attribute (CSS name, property name, browser availability,
     * default computed value.
     * TODO: move all (?) style attribute definitions here.
     */
    public static enum Definition {

        /** The style property {@code accelerator}. */
        ACCELERATOR("accelerator", "accelerator", ie11up("undefined")),

        /** The style property alignment-content. */
        ALIGN_CONTENT("alignContent", "align-content", ie11up("stretch"), ff31up("stretch"), chrome("start")),

        /** The style property {@code align-content}. */
        ALIGN_CONTENT_("align-content", "align-content", ff38up("stretch")),

        /** The style property {@code align-items}. */
        ALIGN_ITEMS("alignItems", "align-items", ff("stretch"), ie11up("stretch"), chrome("start")),

        /** The style property {@code align-items}. */
        ALIGN_ITEMS_("align-items", "align-items", ff38up("stretch")),

        /** The style property alignment-self. */
        ALIGN_SELF("alignSelf", "align-self", ff("stretch"), ie11up("auto"), chrome("start")),

        /** The style property {@code align-self}. */
        ALIGN_SELF_("align-self", "align-self", ff38up("stretch")),

        /** The style property alignment-baseline. */
        ALIGNMENT_BASELINE("alignmentBaseline", "alignment-baseline", ie11up("auto"), chrome("auto")),

        /** The style property all. */
        ALL("all", "all", ff31up(""), chrome("")),

        /** The style property animation. */
        ANIMATION("animation", "animation", ff(""), ie11up(""), chrome("none 0s ease 0s 1 normal none running")),

        /** The style property animation-delay. */
        ANIMATION_DELAY("animationDelay", "animation-delay", ff("0s"), ie11up("0s"), chrome("0s")),

        /** The style property {@code animation-delay}. */
        ANIMATION_DELAY_("animation-delay", "animation-delay", ff38up("0s")),

        /** The style property animation-direction. */
        ANIMATION_DIRECTION("animationDirection", "animation-direction",
                ff("normal"), ie11up("normal"), chrome("normal")),

        /** The style property {@code animation-direction}. */
        ANIMATION_DIRECTION_("animation-direction", "animation-direction", ff38up("normal")),

        /** The style property animation-duration. */
        ANIMATION_DURATION("animationDuration", "animation-duration", ff("0s"), ie11up("0s"), chrome("0s")),

        /** The style property {@code animation-duration}. */
        ANIMATION_DURATION_("animation-duration", "animation-duration", ff38up("0s")),

        /** The style property animation-fill-mode. */
        ANIMATION_FILL_MODE("animationFillMode", "animation-fill-mode", ff("none"), ie11up("none"), chrome("none")),

        /** The style property {@code animation-fill-mode}. */
        ANIMATION_FILL_MODE_("animation-fill-mode", "animation-fill-mode", ff38up("none")),

        /** The style property animation-iteration-count. */
        ANIMATION_ITERATION_COUNT("animationIterationCount", "animation-iteration-count",
                ff("1"), ie11up("1"), chrome("1")),

        /** The style property {@code animation-iteration-count}. */
        ANIMATION_ITERATION_COUNT_("animation-iteration-count", "animation-iteration-count", ff38up("1")),

        /** The style property animation-name. */
        ANIMATION_NAME("animationName", "animation-name", ff("none"), ie11up("none"), chrome("none")),

        /** The style property animation-name. */
        ANIMATION_NAME_("animation-name", "animation-name", ff38up("none")),

        /** The style property animation-play-state. */
        ANIMATION_PLAY_STATE("animationPlayState", "animation-play-state",
                ff("running"), ie11up("running"), chrome("running")),

        /** The style property {@code animation-play-state}. */
        ANIMATION_PLAY_STATE_("animation-play-state", "animation-play-state", ff38up("running")),

        /** The style property animation-timing-function. */
        ANIMATION_TIMING_FUNCTION("animationTimingFunction",
                "animation-timing-function",
                ff("cubic-bezier(0.25, 0.1, 0.25, 1)"),
                ie11up("cubic-bezier(0.25, 0.1, 0.25, 1)"), chrome("ease")),

        /** The style property {@code animation-timing-function}. */
        ANIMATION_TIMING_FUNCTION_("animation-timing-function", "animation-timing-function",
                ff38up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property azimuth. */
        AZIMUTH("azimuth", "azimuth"),

        /** The style property backface-visibility. */
        BACKFACE_VISIBILITY("backfaceVisibility", "backface-visibility",
                ff("visible"), ie11up("visible"), chrome("visible")),

        /** The style property {@code backface-visibility}. */
        BACKFACE_VISIBILITY_("backface-visibility", "backface-visibility", ff38up("visible")),

        /** The style property {@code background}. */
        BACKGROUND("background", "background",
                chrome("rgba(0, 0, 0, 0) none repeat scroll 0% 0% / auto padding-box border-box")),

        /** The style property {@code backgroundAttachment}. */
        BACKGROUND_ATTACHMENT("backgroundAttachment", "background-attachment", chrome("scroll"),
                ff("scroll"), ie11up("scroll")),

        /** The style property {@code background-attachment}. */
        BACKGROUND_ATTACHMENT_("background-attachment", "background-attachment", ff38up("scroll")),

        /** The style property background-blend-mode. */
        BACKGROUND_BLEND_MODE("backgroundBlendMode", "background-blend-mode", ff31up("normal"), chrome("normal")),

        /** The style property {@code background-blend-mode}. */
        BACKGROUND_BLEND_MODE_("background-blend-mode", "background-blend-mode", ff38up("normal")),

        /** The style property background-clip. */
        BACKGROUND_CLIP("backgroundClip", "background-clip",
                ff("border-box"), ie11up("border-box"), chrome("border-box")),

        /** The style property {@code background-clip}. */
        BACKGROUND_CLIP_("background-clip", "background-clip", ff38up("border-box")),

        /** The style property {@code backgroundColor}. */
        BACKGROUND_COLOR("backgroundColor", "background-color", chrome("rgba(0, 0, 0, 0)"), ff("transparent"),
                ie("transparent")),

        /** The style property {@code background-color}. */
        BACKGROUND_COLOR_("background-color", "background-color", ff38up("transparent")),

        /** The style property {@code backgroundImage}. */
        BACKGROUND_IMAGE("backgroundImage", "background-image", chrome("none"), ff("none"), ie("none")),

        /** The style property {@code background-image}. */
        BACKGROUND_IMAGE_("background-image", "background-image", ff38up("none")),

        /** The style property background-origin. */
        BACKGROUND_ORIGIN("backgroundOrigin", "background-origin",
                ff("padding-box"), ie11up("padding-box"), chrome("padding-box")),

        /** The style property {@code background-origin}. */
        BACKGROUND_ORIGIN_("background-origin", "background-origin", ff38up("padding-box")),

        /** The style property {@code backgroundPosition}. */
        BACKGROUND_POSITION("backgroundPosition", "background-position", chrome("0% 0%"), ff("0% 0%"), ie11up("0% 0%")),

        /** The style property {@code background-position}. */
        BACKGROUND_POSITION_("background-position", "background-position", ff38up("0% 0%")),

        /** The style property background-position-x. */
        BACKGROUND_POSITION_X("backgroundPositionX", "background-position-x",
                ie11up("undefined"), chrome("0%")),

        /** The style property background-position-y. */
        BACKGROUND_POSITION_Y("backgroundPositionY", "background-position-y", ie11up("undefined"), chrome("0%")),

        /** The style property {@code backgroundRepeat}. */
        BACKGROUND_REPEAT("backgroundRepeat", "background-repeat", chrome("repeat"), ff("repeat"), ie("repeat")),

        /** The style property {@code background-repeat}. */
        BACKGROUND_REPEAT_("background-repeat", "background-repeat", ff38up("repeat")),

        /** The style property {@code backgroundRepeatX}. */
        BACKGROUND_REPEAT_X("backgroundRepeatX", "background-repeat-x", chrome("")),

        /** The style property {@code backgroundRepeatY}. */
        BACKGROUND_REPEAT_Y("backgroundRepeatY", "background-repeat-y", chrome("")),

        /** The style property background-size. */
        BACKGROUND_SIZE("backgroundSize", "background-size", ff("auto auto"), ie11up("auto"), chrome("auto")),

        /** The style property {@code background-size}. */
        BACKGROUND_SIZE_("background-size", "background-size", ff38up("auto auto")),

        /** The style property baseline-shift. */
        BASELINE_SHIFT("baselineShift", "baseline-shift", ie11up("baseline"), chrome("0px")),

        /** The style property behavior. */
        BEHAVIOR("behavior", "behavior"),

        /** The style property {@code border}. */
        BORDER("border", "border", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code borderBottom}. */
        BORDER_BOTTOM("borderBottom", "border-bottom", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code border-bottom}. */
        BORDER_BOTTOM_("border-bottom", "border-bottom", ff38up("")),

        /** The style property {@code borderBottomColor}. */
        BORDER_BOTTOM_COLOR("borderBottomColor", "border-bottom-color", chrome("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)"),
                ie("rgb(0, 0, 0)")),

        /** The style property {@code border-bottom-color}. */
        BORDER_BOTTOM_COLOR_("border-bottom-color", "border-bottom-color", ff38up("rgb(0, 0, 0)")),

        /** The style property border-bottom-left-radius. */
        BORDER_BOTTOM_LEFT_RADIUS("borderBottomLeftRadius", "border-bottom-left-radius",
                ff("0px"), ie11up("0px"), chrome("0px")),

        /** The style property {@code border-bottom-left-radius}. */
        BORDER_BOTTOM_LEFT_RADIUS_("border-bottom-left-radius", "border-bottom-left-radius", ff38up("0px")),

        /** The style property border-bottom-right-radius. */
        BORDER_BOTTOM_RIGHT_RADIUS("borderBottomRightRadius", "border-bottom-right-radius",
                ff("0px"), ie11up("0px"), chrome("0px")),

        /** The style property {@code border-bottom-right-radius}. */
        BORDER_BOTTOM_RIGHT_RADIUS_("border-bottom-right-radius", "border-bottom-right-radius", ff38up("0px")),

        /** The style property {@code borderBottomStyle}. */
        BORDER_BOTTOM_STYLE("borderBottomStyle", "border-bottom-style", chrome("none"), ff("none"), ie("none")),

        /** The style property {@code border-bottom-style}. */
        BORDER_BOTTOM_STYLE_("border-bottom-style", "border-bottom-style", ff38up("none")),

        /** The style property {@code borderBottomWidth}. */
        BORDER_BOTTOM_WIDTH("borderBottomWidth", "border-bottom-width", chrome("0px"), ff("0px"), ie("0px")),

        /** The style property {@code border-bottom-width}. */
        BORDER_BOTTOM_WIDTH_("border-bottom-width", "border-bottom-width", ff38up("0px")),

        /** The style property {@code borderCollapse}. */
        BORDER_COLLAPSE("borderCollapse", "border-collapse", chrome("separate"), ff("separate"), ie("separate")),

        /** The style property {@code border-collapse}. */
        BORDER_COLLAPSE_("border-collapse", "border-collapse", ff38up("separate")),

        /** The style property {@code borderColor}. */
        BORDER_COLOR("borderColor", "border-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code border-color}. */
        BORDER_COLOR_("border-color", "border-color", ff38up("")),

        /** The style property border-image. */
        BORDER_IMAGE("borderImage", "border-image", ff(""), ie11up(""), chrome("none")),

        /** The style property {@code border-image}. */
        BORDER_IMAGE_("border-image", "border-image", ff38up("")),

        /** The style property border-image-outset. */
        BORDER_IMAGE_OUTSET("borderImageOutset", "border-image-outset", ff("0 0 0 0"), ie11up("0"), chrome("0px")),

        /** The style property {@code border-image-outset}. */
        BORDER_IMAGE_OUTSET_("border-image-outset", "border-image-outset", ff38up("0 0 0 0")),

        /** The style property border-image-repeat. */
        BORDER_IMAGE_REPEAT("borderImageRepeat", "border-image-repeat",
                ff("stretch stretch"), ie11up("stretch"), chrome("stretch")),

        /** The style property {@code border-image-repeat}. */
        BORDER_IMAGE_REPEAT_("border-image-repeat", "border-image-repeat", ff38up("stretch stretch")),

        /** The style property border-image-slice. */
        BORDER_IMAGE_SLICE("borderImageSlice", "border-image-slice",
                ff("100% 100% 100% 100%"), ie11up("100%"), chrome("100%")),

        /** The style property {@code border-image-slice}. */
        BORDER_IMAGE_SLICE_("border-image-slice", "border-image-slice", ff38up("100% 100% 100% 100%")),

        /** The style property border-image-source. */
        BORDER_IMAGE_SOURCE("borderImageSource", "border-image-source", ff("none"), ie11up("none"), chrome("none")),

        /** The style property {@code border-image-source}. */
        BORDER_IMAGE_SOURCE_("border-image-source", "border-image-source", ff38up("none")),

        /** The style property border-image-width. */
        BORDER_IMAGE_WIDTH("borderImageWidth", "border-image-width", ff("1 1 1 1"), ie11up("1"), chrome("1")),

        /** The style property {@code border-image-width}. */
        BORDER_IMAGE_WIDTH_("border-image-width", "border-image-width", ff38up("1 1 1 1")),

        /** The style property {@code borderLeft}. */
        BORDER_LEFT("borderLeft", "border-left", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code border-left}. */
        BORDER_LEFT_("border-left", "border-left", ff38up("")),

        /** The style property {@code borderLeftColor}. */
        BORDER_LEFT_COLOR("borderLeftColor", "border-left-color", chrome("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)"),
                ie("rgb(0, 0, 0)")),

        /** The style property {@code border-left-color}. */
        BORDER_LEFT_COLOR_("border-left-color", "border-left-color", ff38up("rgb(0, 0, 0)")),

        /** The style property {@code borderLeftStyle}. */
        BORDER_LEFT_STYLE("borderLeftStyle", "border-left-style", chrome("none"), ff("none"), ie("none")),

        /** The style property {@code border-left-style}. */
        BORDER_LEFT_STYLE_("border-left-style", "border-left-style", ff38up("none")),

        /** The style property {@code borderLeftWidth}. */
        BORDER_LEFT_WIDTH("borderLeftWidth", "border-left-width", chrome("0px")),

        /** The style property {@code border-left-width}. */
        BORDER_LEFT_WIDTH_("border-left-width", "border-left-width", ff38up("0px")),

        /** The style property border-radius. */
        BORDER_RADIUS("borderRadius", "border-radius", ff(""), ie11up(""), chrome("0px")),

        /** The style property {@code border-radius}. */
        BORDER_RADIUS_("border-radius", "border-radius", ff38up("")),

        /** The style property {@code borderRight}. */
        BORDER_RIGHT("borderRight", "border-right", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code border-right}. */
        BORDER_RIGHT_("border-right", "border-right", ff38up("")),

        /** The style property {@code borderRightColor}. */
        BORDER_RIGHT_COLOR("borderRightColor", "border-right-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code border-right-color}. */
        BORDER_RIGHT_COLOR_("border-right-color", "border-right-color", ff38up("rgb(0, 0, 0)")),

        /** The style property {@code borderRightStyle}. */
        BORDER_RIGHT_STYLE("borderRightStyle", "border-right-style", chrome("none")),

        /** The style property {@code border-right-style}. */
        BORDER_RIGHT_STYLE_("border-right-style", "border-right-style", ff38up("none")),

        /** The style property {@code borderRightWidth}. */
        BORDER_RIGHT_WIDTH("borderRightWidth", "border-right-width", chrome("0px")),

        /** The style property {@code border-right-width}. */
        BORDER_RIGHT_WIDTH_("border-right-width", "border-right-width", ff38up("0px")),

        /** The style property {@code borderSpacing}. */
        BORDER_SPACING("borderSpacing", "border-spacing", chrome("0px 0px")),

        /** The style property {@code border-spacing}. */
        BORDER_SPACING_("border-spacing", "border-spacing", ff38up("0px 0px")),

        /** The style property {@code borderStyle}. */
        BORDER_STYLE("borderStyle", "border-style", chrome("none")),

        /** The style property {@code border-style}. */
        BORDER_STYLE_("border-style", "border-style", ff38up("")),

        /** The style property {@code borderTop}. */
        BORDER_TOP("borderTop", "border-top", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code border-top}. */
        BORDER_TOP_("border-top", "border-top", ff38up("")),

        /** The style property {@code borderTopColor}. */
        BORDER_TOP_COLOR("borderTopColor", "border-top-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code border-top-color}. */
        BORDER_TOP_COLOR_("border-top-color", "border-top-color", ff38up("rgb(0, 0, 0)")),

        /** The style property border-top-left-radius. */
        BORDER_TOP_LEFT_RADIUS("borderTopLeftRadius", "border-top-left-radius",
                ff("0px"), ie11up("0px"), chrome("0px")),

        /** The style property {@code border-top-left-radius}. */
        BORDER_TOP_LEFT_RADIUS_("border-top-left-radius", "border-top-left-radius", ff38up("0px")),

        /** The style property border-top-right-radius. */
        BORDER_TOP_RIGHT_RADIUS("borderTopRightRadius", "border-top-right-radius",
                ff("0px"), ie11up("0px"), chrome("0px")),

        /** The style property {@code border-top-right-radius}. */
        BORDER_TOP_RIGHT_RADIUS_("border-top-right-radius", "border-top-right-radius", ff38up("0px")),

        /** The style property {@code borderTopStyle}. */
        BORDER_TOP_STYLE("borderTopStyle", "border-top-style", chrome("none")),

        /** The style property {@code border-top-style}. */
        BORDER_TOP_STYLE_("border-top-style", "border-top-style", ff38up("none")),

        /** The style property {@code borderTopWidth}. */
        BORDER_TOP_WIDTH("borderTopWidth", "border-top-width", chrome("0px")),

        /** The style property {@code border-top-width}. */
        BORDER_TOP_WIDTH_("border-top-width", "border-top-width", ff38up("0px")),

        /** The style property {@code borderWidth}. */
        BORDER_WIDTH("borderWidth", "border-width", chrome("0px")),

        /** The style property {@code border-width}. */
        BORDER_WIDTH_("border-width", "border-width", ff38up("")),

        /** The style property {@code bottom}. */
        BOTTOM("bottom", "bottom", chrome("auto")),

        /** The style property box-decoration-break. */
        BOX_DECORATION_BREAK("boxDecorationBreak", "box-decoration-break", ff38up("slice")),

        /** The style property {@code box-decoration-break}. */
        BOX_DECORATION_BREAK_("box-decoration-break", "box-decoration-break", ff38up("slice")),

        /** The style property box-shadow. */
        BOX_SHADOW("boxShadow", "box-shadow", ff("none"), ie11up("none"), chrome("none")),

        /** The style property {@code box-shadow}. */
        BOX_SHADOW_("box-shadow", "box-shadow", ff38up("none")),

        /** The style property box-sizing. */
        BOX_SIZING("boxSizing", "box-sizing", ff31up("content-box"), ie11up("content-box"), chrome("content-box")),

        /** The style property {@code box-sizing}. */
        BOX_SIZING_("box-sizing", "box-sizing", ff38up("content-box")),

        /** The style property break-after. */
        BREAK_AFTER("breakAfter", "break-after", ie11up("auto")),

        /** The style property break-before. */
        BREAK_BEFORE("breakBefore", "break-before", ie11up("auto")),

        /** The style property break-inside. */
        BREAK_INSIDE("breakInside", "break-inside", ie11up("auto")),

        /** The style property {@code bufferedRendering}. */
        BUFFERED_RENDERING("bufferedRendering", "buffered-rendering", chrome("auto")),

        /** The style property {@code captionSide}. */
        CAPTION_SIDE("captionSide", "caption-side", chrome("top")),

        /** The style property {@code caption-side}. */
        CAPTION_SIDE_("caption-side", "caption-side", ff38up("top")),

        /** The style property {@code clear}. */
        CLEAR("clear", "clear", chrome("none")),

        /** The style property {@code clip}. */
        CLIP("clip", "clip", chrome("auto")),

        /** The style property clip-path. */
        CLIP_PATH("clipPath", "clip-path", ff("none"), ie11up("none"), chrome("none")),

        /** The style property {@code clip-path}. */
        CLIP_PATH_("clip-path", "clip-path", ff38up("none")),

        /** The style property clip-rule. */
        CLIP_RULE("clipRule", "clip-rule", ff("nonzero"), ie11up("nonzero"), chrome("nonzero")),

        /** The style property {@code clip-rule}. */
        CLIP_RULE_("clip-rule", "clip-rule", ff38up("nonzero")),

        /** The style property {@code color}. */
        COLOR("color", "color", chrome("rgb(0, 0, 0)")),

        /** The style property color-interpolation. */
        COLOR_INTERPOLATION("colorInterpolation", "color-interpolation", ff("srgb"), chrome("sRGB")),

        /** The style property {@code color-interpolation}. */
        COLOR_INTERPOLATION_("color-interpolation", "color-interpolation", ff38up("srgb")),

        /** The style property color-interpolation-filters. */
        COLOR_INTERPOLATION_FILTERS("colorInterpolationFilters",
                "color-interpolation-filters", ff("linearrgb"), ie11up(""), chrome("linearRGB")),

        /** The style property {@code color-interpolation-filters}. */
        COLOR_INTERPOLATION_FILTERS_("color-interpolation-filters", "color-interpolation-filters", ff38up("linearrgb")),

        /** The style property {@code colorRendering}. */
        COLOR_RENDERING("colorRendering", "color-rendering", chrome("auto")),

        /** The style property column-count. */
        COLUMN_COUNT("columnCount", "column-count", ie11up("auto")),

        /** The style property column-fill. */
        COLUMN_FILL("columnFill", "column-fill", ie11up("balance")),

        /** The style property column-gap. */
        COLUMN_GAP("columnGap", "column-gap", ie11up("normal")),

        /** The style property column-rule. */
        COLUMN_RULE("columnRule", "column-rule", ie11up("")),

        /** The style property column-rule-color. */
        COLUMN_RULE_COLOR("columnRuleColor", "column-rule-color", ie11up("rgb(0, 0, 0)")),

        /** The style property column-rule-style. */
        COLUMN_RULE_STYLE("columnRuleStyle", "column-rule-style", ie11up("none")),

        /** The style property column-rule-width. */
        COLUMN_RULE_WIDTH("columnRuleWidth", "column-rule-width", ie11up("medium")),

        /** The style property column-span. */
        COLUMN_SPAN("columnSpan", "column-span", ie11up("1")),

        /** The style property column-width. */
        COLUMN_WIDTH("columnWidth", "column-width", ie11up("auto")),

        /** The style property columns. */
        COLUMNS("columns", "columns", ie11up("")),

        /** The style property content. */
        CONTENT("content", "content", ie11up("normal"), chrome(""), ff("none")),

        /** The style property {@code counterIncrement}. */
        COUNTER_INCREMENT("counterIncrement", "counter-increment", chrome("none")),

        /** The style property {@code counter-increment}. */
        COUNTER_INCREMENT_("counter-increment", "counter-increment", ff38up("none")),

        /** The style property {@code counterReset}. */
        COUNTER_RESET("counterReset", "counter-reset", chrome("none")),

        /** The style property {@code counter-reset}. */
        COUNTER_RESET_("counter-reset", "counter-reset", ff38up("none")),

        /** The style property {@code cssFloat}. */
        CSS_FLOAT("cssFloat", "css-float", chrome("none"), ff("none"), ie11up("none")),

        //TODO: seems to be a combination of all other properties.
        /** The style property {@code cssText}. */
        CSS_TEXT("cssText", "css-text", chrome("")),

        /** The style property cue. */
        CUE("cue", "cue"),

        /** The style property cue-after. */
        CUE_AFTER("cueAfter", "cue-after"),

        /** The style property cue-before. */
        CUE_BEFORE("cueBefore", "cue-before"),

        /** The style property {@code cursor}. */
        CURSOR("cursor", "cursor", chrome("auto")),

        /** The style property {@code cx}. */
        CX("cx", "cx", chrome("0px")),

        /** The style property {@code cy}. */
        CY("cy", "cy", chrome("0px")),

        /** The style property {@code direction}. */
        DIRECTION("direction", "direction", chrome("ltr")),

        /** The style property {@code display}. */
        DISPLAY("display", "display", chrome("block")),

        /** The style property dominant-baseline. */
        DOMINANT_BASELINE("dominantBaseline", "dominant-baseline", ff("auto"), ie11up("auto"), chrome("auto")),

        /** The style property {@code dominant-baseline}. */
        DOMINANT_BASELINE_("dominant-baseline", "dominant-baseline", ff38up("auto")),

        /** The style property elevation. */
        ELEVATION("elevation", "elevation"),

        /** The style property empty-cells. */
        EMPTY_CELLS("emptyCells", "empty-cells", ie11up("show"), ffBelow38("-moz-show-background"), ff38up("show"),
                chrome("show")),

        /** The style property {@code empty-cells}. */
        EMPTY_CELLS_("empty-cells", "empty-cells", ff38up("show")),

        /** The style property enable-background. */
        ENABLE_BACKGROUND("enableBackground", "enable-background", ie11up("accumulate"), chrome("")),

        /** The style property fill. */
        FILL("fill", "fill", ff("rgb(0, 0, 0)"), ie11up("black"), chrome("rgb(0, 0, 0)")),

        /** The style property fill-opacity. */
        FILL_OPACITY("fillOpacity", "fill-opacity", ff("1"), ie11up("1"), chrome("1")),

        /** The style property {@code fill-opacity}. */
        FILL_OPACITY_("fill-opacity", "fill-opacity", ff38up("1")),

        /** The style property fill-rule. */
        FILL_RULE("fillRule", "fill-rule", ff("nonzero"), ie11up("nonzero"), chrome("nonzero")),

        /** The style property {@code fill-rule}. */
        FILL_RULE_("fill-rule", "fill-rule", ff38up("nonzero")),

        /** The style property filter. */
        FILTER("filter", "filter", ff("none"), ieBelow11(""), ie11up("none"), chrome("none")),

        /** The style property flex. */
        FLEX("flex", "flex", ff(""), ie11up("0 1 auto"), chrome("0 1 auto")),

        /** The style property flex-basis. */
        FLEX_BASIS("flexBasis", "flex-basis", ff("auto"), ie11up("auto"), chrome("auto")),

        /** The style property {@code flex-basis}. */
        FLEX_BASIS_("flex-basis", "flex-basis", ff38up("auto")),

        /** The style property flex-direction. */
        FLEX_DIRECTION("flexDirection", "flex-direction", ff("row"), ie11up("row"), chrome("row")),

        /** The style property {@code flex-direction}. */
        FLEX_DIRECTION_("flex-direction", "flex-direction", ff38up("row")),

        /** The style property flex-flow. */
        FLEX_FLOW("flexFlow", "flex-flow", ff31up(""), ie11up("row nowrap"), chrome("row nowrap")),

        /** The style property {@code flex-flow}. */
        FLEX_FLOW_("flex-flow", "flex-flow", ff38up("")),

        /** The style property flex-grow. */
        FLEX_GROW("flexGrow", "flex-grow", ff("0"), ie11up("0"), chrome("0")),

        /** The style property {@code flex-grow}. */
        FLEX_GROW_("flex-grow", "flex-grow", ff38up("0")),

        /** The style property flex-shrink. */
        FLEX_SHRINK("flexShrink", "flex-shrink", ff("1"), ie11up("1"), chrome("1")),

        /** The style property {@code flex-shrink}. */
        FLEX_SHRINK_("flex-shrink", "flex-shrink", ff38up("1")),

        /** The style property {@code flexWrap}. */
        FLEX_WRAP("flexWrap", "flex-wrap", ff31up("nowrap"), ie11up("nowrap"), chrome("nowrap")),

        /** The style property {@code flex-wrap}. */
        FLEX_WRAP_("flex-wrap", "flex-wrap", ff38up("nowrap")),

        /** The style property {@code float}. */
        FLOAT("float", "float", ff38up("none"), chrome("none")),

        /** The style property {@code floodColor}. */
        FLOOD_COLOR("floodColor", "flood-color", ff("rgb(0, 0, 0)"), ie11up(""), chrome("rgb(0, 0, 0)")),

        /** The style property {@code flood-color}. */
        FLOOD_COLOR_("flood-color", "flood-color", ff38up("rgb(0, 0, 0)")),

        /** The style property flood-opacity. */
        FLOOD_OPACITY("floodOpacity", "flood-opacity", ff("1"), ie11up("1"), chrome("1")),

        /** The style property {@code flood-opacity}. */
        FLOOD_OPACITY_("flood-opacity", "flood-opacity", ff38up("1")),

        /** The style property {@code font}. */
        FONT("font", "font", chrome("normal normal normal normal 16px/normal 'Times New Roman'")),

        /** The style property {@code fontFamily}. */
        FONT_FAMILY("fontFamily", "font-family", chrome("'Times New Roman'"), ie11up("Times New Roman"), ff("serif")),

        /** The style property {@code font-family}. */
        FONT_FAMILY_("font-family", "font-family", ff38up("serif")),

        /** The style property font-feature-settings. */
        FONT_FEATURE_SETTINGS("fontFeatureSettings", "font-feature-settings", ie11up("normal"), ff38up("normal")),

        /** The style property {@code font-feature-settings}. */
        FONT_FEATURE_SETTINGS_("font-feature-settings", "font-feature-settings", ff38up("normal")),

        /** The style property font-kerning. */
        FONT_KERNING("fontKerning", "font-kerning", ff38up("auto"), chrome("auto")),

        /** The style property {@code font-kerning}. */
        FONT_KERNING_("font-kerning", "font-kerning", ff38up("auto")),

        /** The style property font-language-override. */
        FONT_LANGUAGE_OVERRIDE("fontLanguageOverride", "font-language-override", ff38up("normal")),

        /** The style property {@code font-language-override}. */
        FONT_LANGUAGE_OVERRIDE_("font-language-override", "font-language-override", ff38up("normal")),

        /** The style property {@code fontSize}. */
        FONT_SIZE("fontSize", "font-size", chrome("16px")),

        /** The style property {@code font-size}. */
        FONT_SIZE_("font-size", "font-size", ff38up("16px")),

        /** The style property {@code font-size-adjust}. */
        FONT_SIZE_ADJUST_("font-size-adjust", "font-size-adjust", ff38up("none")),

        /** The style property {@code fontStretch}. */
        FONT_STRETCH("fontStretch", "font-stretch", chrome("normal")),

        /** The style property {@code font-stretch}. */
        FONT_STRETCH_("font-stretch", "font-stretch", ff38up("normal")),

        /** The style property {@code fontStyle}. */
        FONT_STYLE("fontStyle", "font-style", chrome("normal")),

        /** The style property {@code font-style}. */
        FONT_STYLE_("font-style", "font-style", ff38up("normal")),

        /** The style property font-synthesis. */
        FONT_SYNTHESIS("fontSynthesis", "font-synthesis", ff38up("weight style")),

        /** The style property {@code font-synthesis}. */
        FONT_SYNTHESIS_("font-synthesis", "font-synthesis", ff38up("weight style")),

        /** The style property {@code fontVariant}. */
        FONT_VARIANT("fontVariant", "font-variant", chrome("normal")),

        /** The style property {@code font-variant}. */
        FONT_VARIANT_("font-variant", "font-variant", ff38up("normal")),

        /** The style property font-variant-alternates. */
        FONT_VARIANT_ALTERNATES("fontVariantAlternates", "font-variant-alternates", ff38up("normal")),

        /** The style property {@code font-variant-alternates}. */
        FONT_VARIANT_ALTERNATES_("font-variant-alternates", "font-variant-alternates", ff38up("normal")),

        /** The style property font-variant-caps. */
        FONT_VARIANT_CAPS("fontVariantCaps", "font-variant-caps", ff38up("normal")),

        /** The style property {@code font-variant-caps}. */
        FONT_VARIANT_CAPS_("font-variant-caps", "font-variant-caps", ff38up("normal")),

        /** The style property font-variant-east-asian. */
        FONT_VARIANT_EAST_ASIAN("fontVariantEastAsian", "font-variant-east-asian", ff38up("normal")),

        /** The style property {@code font-variant-east-asian}. */
        FONT_VARIANT_EAST_ASIAN_("font-variant-east-asian", "font-variant-east-asian", ff38up("normal")),

        /** The style property font-variant-ligatures. */
        FONT_VARIANT_LIGATURES("fontVariantLigatures", "font-variant-ligatures", ff38up("normal"), chrome("normal")),

        /** The style property {@code font-variant-ligatures}. */
        FONT_VARIANT_LIGATURES_("font-variant-ligatures", "font-variant-ligatures", ff38up("normal")),

        /** The style property font-variant-numeric. */
        FONT_VARIANT_NUMERIC("fontVariantNumeric", "font-variant-numeric", ff38up("normal")),

        /** The style property {@code font-variant-numeric}. */
        FONT_VARIANT_NUMERIC_("font-variant-numeric", "font-variant-numeric", ff38up("normal")),

        /** The style property font-variant-position. */
        FONT_VARIANT_POSITION("fontVariantPosition", "font-variant-position", ff38up("normal")),

        /** The style property {@code font-variant-position}. */
        FONT_VARIANT_POSITION_("font-variant-position", "font-variant-position", ff38up("normal")),

        /** The style property {@code fontWeight}. */
        FONT_WEIGHT("fontWeight", "font-weight", chrome("normal"), ff("400"), ie("400")),

        /** The style property {@code font-weight}. */
        FONT_WEIGHT_("font-weight", "font-weight", ff38up("400")),

        /** The style property glyph-orientation-horizontal. */
        GLYPH_ORIENTATION_HORIZONTAL("glyphOrientationHorizontal", "glyph-orientation-horizontal",
                ie11up("0deg"), chrome("0deg")),

        /** The style property glyph-orientation-vertical. */
        GLYPH_ORIENTATION_VERTICAL("glyphOrientationVertical", "glyph-orientation-vertical",
                ie11up("auto"), chrome("auto")),

        /** The style property {@code height}. */
        HEIGHT("height", "height", chrome("skipped")),

        /** The style property image-orientation. */
        IMAGE_ORIENTATION("imageOrientation", "image-orientation", ff31up("0deg")),

        /** The style property {@code image-orientation}. */
        IMAGE_ORIENTATION_("image-orientation", "image-orientation", ff38up("0deg")),

        /** The style property image-rendering. */
        IMAGE_RENDERING("imageRendering", "image-rendering", ff("auto"), chrome("auto")),

        /** The style property {@code image-rendering}. */
        IMAGE_RENDERING_("image-rendering", "image-rendering", ff38up("auto")),

        /** The style property ime-mode. */
        IME_MODE("imeMode", "ime-mode", ie11up("undefined"), ff("auto")),

        /** The style property {@code ime-mode}. */
        IME_MODE_("ime-mode", "ime-mode", ff38up("auto")),

        /** The style property {@code isolation}. */
        ISOLATION("isolation", "isolation", ff38up("auto"), chrome("auto")),

        /** The style property {@code justify-content}. */
        JUSTIFY_CONTENT("justifyContent", "justify-content",
                ff("flex-start"), ie11up("flex-start"), chrome("start")),

        /** The style property {@code justify-content}. */
        JUSTIFY_CONTENT_("justify-content", "justify-content", ff38up("flex-start")),

        /** The style property kerning. */
        KERNING("kerning", "kerning", ie11up("auto")),

        /** The style property layout-flow. */
        LAYOUT_FLOW("layoutFlow", "layout-flow", ie11up("undefined")),

        /** The style property layout-grid. */
        LAYOUT_GRID("layoutGrid", "layout-grid", ie11up("undefined")),

        /** The style property layout-grid-char. */
        LAYOUT_GRID_CHAR("layoutGridChar", "layout-grid-char", ie11up("undefined")),

        /** The style property layout-grid-line. */
        LAYOUT_GRID_LINE("layoutGridLine", "layout-grid-line", ie11up("undefined")),

        /** The style property layout-grid-mode. */
        LAYOUT_GRID_MODE("layoutGridMode", "layout-grid-mode", ie11up("undefined")),

        /** The style property layout-grid-type. */
        LAYOUT_GRID_TYPE("layoutGridType", "layout-grid-type", ie11up("undefined")),

        /** The style property {@code left}. */
        LEFT("left", "left", chrome("auto")),

        /** The style property {@code letterSpacing}. */
        LETTER_SPACING("letterSpacing", "letter-spacing", chrome("normal")),

        /** The style property {@code letter-spacing}. */
        LETTER_SPACING_("letter-spacing", "letter-spacing", ff38up("normal")),

        /** The style property lighting-color. */
        LIGHTING_COLOR("lightingColor", "lighting-color",
                ff("rgb(255, 255, 255)"), ie11up(""), chrome("rgb(255, 255, 255)")),

        /** The style property {@code lighting-color}. */
        LIGHTING_COLOR_("lighting-color", "lighting-color", ff38up("rgb(255, 255, 255)")),

        /** The style property line-break. */
        LINE_BREAK("lineBreak", "line-break", ie11up("undefined")),

        /** The style property line-height. */
        LINE_HEIGHT("lineHeight", "line-height", ff("20px"), ieBelow11("20px"), ie11up("normal"), chrome("normal")),

        /** The style property {@code line-height}. */
        LINE_HEIGHT_("line-height", "line-height", ff38up("20px")),

        /** The style property {@code listStyle}. */
        LIST_STYLE("listStyle", "list-style", chrome("disc outside none")),

        /** The style property {@code list-style}. */
        LIST_STYLE_("list-style", "list-style", ff38up("")),

        /** The style property {@code listStyleImage}. */
        LIST_STYLE_IMAGE("listStyleImage", "list-style-image", chrome("none")),

        /** The style property {@code list-style-image}. */
        LIST_STYLE_IMAGE_("list-style-image", "list-style-image", ff38up("none")),

        /** The style property {@code listStylePosition}. */
        LIST_STYLE_POSITION("listStylePosition", "list-style-position", chrome("outside")),

        /** The style property {@code list-style-position}. */
        LIST_STYLE_POSITION_("list-style-position", "list-style-position", ff38up("outside")),

        /** The style property {@code listStyleType}. */
        LIST_STYLE_TYPE("listStyleType", "list-style-type", chrome("disc")),

        /** The style property {@code list-style-type}. */
        LIST_STYLE_TYPE_("list-style-type", "list-style-type", ff38up("disc")),

        /** The style property {@code margin}. */
        MARGIN("margin", "margin", chrome("0px"), ff("")),

        /** The style property {@code marginBottom}. */
        MARGIN_BOTTOM("marginBottom", "margin-bottom", chrome("0px")),

        /** The style property {@code margin-bottom}. */
        MARGIN_BOTTOM_("margin-bottom", "margin-bottom", ff38up("0px")),

        /** The style property {@code marginLeft}. */
        MARGIN_LEFT("marginLeft", "margin-left", chrome("0px")),

        /** The style property {@code margin-left}. */
        MARGIN_LEFT_("margin-left", "margin-left", ff38up("0px")),

        /** The style property {@code marginRight}. */
        MARGIN_RIGHT("marginRight", "margin-right", chrome("0px")),

        /** The style property {@code margin-right}. */
        MARGIN_RIGHT_("margin-right", "margin-right", ff38up("0px")),

        /** The style property {@code marginTop}. */
        MARGIN_TOP("marginTop", "margin-top", chrome("0px")),

        /** The style property {@code margin-top}. */
        MARGIN_TOP_("margin-top", "margin-top", ff38up("0px")),

        /** The style property marker. */
        MARKER("marker", "marker", ff(""), ie11up("none"), chrome("")),

        /** The style property marker-end. */
        MARKER_END("markerEnd", "marker-end", ff("none"), ie11up("none"), chrome("none")),

        /** The style property {@code marker-end}. */
        MARKER_END_("marker-end", "marker-end", ff38up("none")),

        /** The style property marker-mid. */
        MARKER_MID("markerMid", "marker-mid", ff("none"), ie11up("none"), chrome("none")),

        /** The style property {@code marker-mid}. */
        MARKER_MID_("marker-mid", "marker-mid", ff38up("none")),

        /** The style property {@code marker-offset}. */
        MARKER_OFFSET_("marker-offset", "marker-offset", ff38up("auto")),

        /** The style property marker-start. */
        MARKER_START("markerStart", "marker-start", ff("none"), ie11up("none"), chrome("none")),

        /** The style property {@code marker-start}. */
        MARKER_START_("marker-start", "marker-start", ff38up("none")),

        /** The style property mask. */
        MASK("mask", "mask", ff("none"), ie11up("none"), chrome("none")),

        /** The style property mask-type. */
        MASK_TYPE("maskType", "mask-type", ff38up("luminance"), chrome("luminance")),

        /** The style property {@code mask-type}. */
        MASK_TYPE_("mask-type", "mask-type", ff38up("luminance")),

        /** The style property {@code maxHeight}. */
        MAX_HEIGHT("maxHeight", "max-height", chrome("none")),

        /** The style property {@code max-height}. */
        MAX_HEIGHT_("max-height", "max-height", ff38up("none")),

        /** The style property {@code maxWidth}. */
        MAX_WIDTH("maxWidth", "max-width", chrome("none")),

        /** The style property {@code max-width}. */
        MAX_WIDTH_("max-width", "max-width", ff38up("none")),

        /** The style property {@code maxZoom}. */
        MAX_ZOOM("maxZoom", "max-zoom", chrome("")),

        /** The style property {@code minHeight}. */
        MIN_HEIGHT("minHeight", "min-height", chrome("0px")),

        /** The style property {@code min-height}. */
        MIN_HEIGHT_("min-height", "min-height", ff38up("0px")),

        /** The style property {@code minWidth}. */
        MIN_WIDTH("minWidth", "min-width", chrome("0px")),

        /** The style property {@code min-width}. */
        MIN_WIDTH_("min-width", "min-width", ff38up("0px")),

        /** The style property {@code minZoom}. */
        MIN_ZOOM("minZoom", "min-zoom", chrome("")),

        /** The style property mix-blend-mode. */
        MIX_BLEND_MODE("mixBlendMode", "mix-blend-mode", ff38up("normal"), chrome("normal")),

        /** The style property {@code mix-blend-mode}. */
        MIX_BLEND_MODE_("mix-blend-mode", "mix-blend-mode", ff38up("normal")),

        /** The style property -moz-animation. */
        MOZ_ANIMATION("MozAnimation", "-moz-animation", ff("")),

        /** The style property -moz-animation-delay. */
        MOZ_ANIMATION_DELAY("MozAnimationDelay", "-moz-animation-delay",
                ff("0s")),

        /** The style property -moz-animation-direction. */
        MOZ_ANIMATION_DIRECTION("MozAnimationDirection", "-moz-animation-direction", ff("normal")),

        /** The style property -moz-animation-duration. */
        MOZ_ANIMATION_DURATION("MozAnimationDuration",
                "-moz-animation-duration", ff("0s")),

        /** The style property -moz-animation-fill-mode. */
        MOZ_ANIMATION_FILL_MODE("MozAnimationFillMode",
                "moz-animation-fill-mode", ff("none")),

        /** The style property -moz-animation-iteration-count. */
        MOZ_ANIMATION_ITERATION_COUNT("MozAnimationIterationCount",
                "-moz-animation-iteration-count", ff("1")),

        /** The style property -moz-animation-name. */
        MOZ_ANIMATION_NAME("MozAnimationName", "moz-annimation-name",
                ff("none")),

        /** The style property -moz-animation-play-state. */
        MOZ_ANIMATION_PLAY_STATE("MozAnimationPlayState",
                "moz-annimation-play-state", ff("running")),

        /** The style property -moz-animation-timing-function. */
        MOZ_ANIMATION_TIMING_FUNCTION("MozAnimationTimingFunction",
                "moz-annimation-timing-function",
                ff("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property -moz-appearance. */
        MOZ_APPEARANCE("MozAppearance", "-moz-appearance", ff("none")),

        /** The style property -moz-backface-visibility. */
        MOZ_BACKFACE_VISIBILITY("MozBackfaceVisibility",
                "-moz-backface-visibility", ff("visible")),

        /** The style property -moz-background-clip. */
        MOZ_BACKGROUND_CLIP("MozBackgroundClip", "-moz-background-clip"),

        /** The style property -moz-background-inline-policy. */
        MOZ_BACKGROUND_INLINE_POLICY("MozBackgroundInlinePolicy",
                "-moz-background-inline-policy", ffBelow38("continuous")),

        /** The style property -moz-background-origin. */
        MOZ_BACKGROUND_ORIGIN("MozBackgroundOrigin", "-moz-background-origin"),

        /** The style property -moz-background-size. */
        MOZ_BACKGROUND_SIZE("MozBackgroundSize", "-moz-background-size"),

        /** The style property -moz-binding. */
        MOZ_BINDING("MozBinding", "-moz-binding", ff("none")),

        /** The style property -moz-border-bottom-colors. */
        MOZ_BORDER_BOTTOM_COLORS("MozBorderBottomColors",
                "-moz-border-bottom-colors", ff("none")),

        /** The style property -moz-border-end. */
        MOZ_BORDER_END("MozBorderEnd", "-moz-border-end", ff("")),

        /** The style property -moz-border-end-color. */
        MOZ_BORDER_END_COLOR("MozBorderEndColor", "-moz-border-end-color", ff("")),

        /** The style property -moz-border-end-style. */
        MOZ_BORDER_END_STYLE("MozBorderEndStyle", "-moz-border-end-style", ff("")),

        /** The style property -moz-border-end-width. */
        MOZ_BORDER_END_WIDTH("MozBorderEndWidth", "-moz-border-end-width", ff("")),

        /** The style property -moz-border-image. */
        MOZ_BORDER_IMAGE("MozBorderImage", "-moz-border-image", ff("")),

        /** The style property -moz-border-left-colors. */
        MOZ_BORDER_LEFT_COLORS("MozBorderLeftColors", "-moz-border-left-colors", ff("none")),

        /** The style property -moz-border-radius. */
        MOZ_BORDER_RADIUS("MozBorderRadius", "-moz-border-radius"),

        /** The style property -moz-border-radius-bottomleft. */
        MOZ_BORDER_RADIUS_BOTTOMLEFT("MozBorderRadiusBottomleft", "-moz-border-radius-bottomleft"),

        /** The style property -moz-border-radius-bottomright. */
        MOZ_BORDER_RADIUS_BOTTOMRIGHT("MozBorderRadiusBottomright", "-moz-border-radius-bottomright"),

        /** The style property -moz-border-radius-topleft. */
        MOZ_BORDER_RADIUS_TOPLEFT("MozBorderRadiusTopleft", "-moz-border-radius-topleft"),

        /** The style property -moz-border-radius-topright. */
        MOZ_BORDER_RADIUS_TOPRIGHT("MozBorderRadiusTopright", "-moz-border-radius-topright"),

        /** The style property -moz-border-right-colors. */
        MOZ_BORDER_RIGHT_COLORS("MozBorderRightColors", "-moz-border-right-colors", ff("none")),

        /** The style property -moz-border-start. */
        MOZ_BORDER_START("MozBorderStart", "-moz-border-start", ff("")),

        /** The style property -moz-border-start-color. */
        MOZ_BORDER_START_COLOR("MozBorderStartColor", "-moz-border-start-color", ff("")),

        /** The style property -moz-border-start-style. */
        MOZ_BORDER_START_STYLE("MozBorderStartStyle", "-moz-border-start-style", ff("")),

        /** The style property -moz-border-start-width. */
        MOZ_BORDER_START_WIDTH("MozBorderStartWidth", "-moz-border-start-width", ff("")),

        /** The style property -moz-border-top-colors. */
        MOZ_BORDER_TOP_COLORS("MozBorderTopColors", "-moz-border-top-colors", ff("none")),

        /** The style property -moz-box-align. */
        MOZ_BOX_ALIGN("MozBoxAlign", "-moz-box-align", ff("stretch")),

        /** The style property -moz-box-direction. */
        MOZ_BOX_DIRECTION("MozBoxDirection", "-moz-box-direction", ff("normal")),

        /** The style property -moz-box-flex. */
        MOZ_BOX_FLEX("MozBoxFlex", "-moz-box-flex", ff("0")),

        /** The style property -moz-box-ordinal-group. */
        MOZ_BOX_ORDINAL_GROUP("MozBoxOrdinalGroup", "-moz-box-ordinal-group", ff("1")),

        /** The style property -moz-box-orient. */
        MOZ_BOX_ORIENT("MozBoxOrient", "-moz-box-orient", ff("horizontal")),

        /** The style property -moz-box-pack. */
        MOZ_BOX_PACK("MozBoxPack", "-moz-box-pack", ff("start")),

        /** The style property -moz-box-shadow. */
        MOZ_BOX_SHADOW("MozBoxShadow", "-moz-box-shadow"),

        /** The style property -moz-box-sizing. */
        MOZ_BOX_SIZING("MozBoxSizing", "-moz-box-sizing", ff("content-box")),

        /** The style property -moz-column-count. */
        MOZ_COLUMN_COUNT("MozColumnCount", "-moz-column-count", ff("auto")),

        /** The style property -moz-column-fill. */
        MOZ_COLUMN_FILL("MozColumnFill", "-moz-column-fill", ff("balance")),

        /** The style property -moz-column-gap. */
        MOZ_COLUMN_GAP("MozColumnGap", "-moz-column-gap", ff("16px")),

        /** The style property -moz-column-rule. */
        MOZ_COLUMN_RULE("MozColumnRule", "-moz-column-rule", ff("")),

        /** The style property -moz-column-rule-color. */
        MOZ_COLUMN_RULE_COLOR("MozColumnRuleColor", "-moz-column-rule-color",
                ff("rgb(0, 0, 0)")),

        /** The style property -moz-column-rule-style. */
        MOZ_COLUMN_RULE_STYLE("MozColumnRuleStyle", "-moz-column-rule-style",
                ff("none")),

        /** The style property -moz-column-rule-width. */
        MOZ_COLUMN_RULE_WIDTH("MozColumnRuleWidth", "-moz-column-rule-width",
                ff("0px")),

        /** The style property -moz-column-width. */
        MOZ_COLUMN_WIDTH("MozColumnWidth", "-moz-column-width", ff("auto")),

        /** The style property -moz-columns. */
        MOZ_COLUMNS("MozColumns", "-moz-columns", ff("")),

        /** The style property -moz-float-edge. */
        MOZ_FLOAT_EDGE("MozFloatEdge", "-moz-float-edge", ff("content-box")),

        /** The style property -moz-font-feature-settings. */
        MOZ_FONT_FEATURE_SETTINGS("MozFontFeatureSettings",
                "-moz-font-feature-settings", ff("normal")),

        /** The style property -moz-font-language-override. */
        MOZ_FONT_LANGUAGE_OVERRIDE("MozFontLanguageOverride",
                "-moz-font-language-override", ff("normal")),

        /** The style property -moz-force-broken-image-icon. */
        MOZ_FORCE_BROKEN_IMAGE_ICON("MozForceBrokenImageIcon",
                "-moz-force-broken-image-icon", ff("0")),

        /** The style property -moz-hyphens. */
        MOZ_HYPHENS("MozHyphens", "-moz-hyphens", ff("manual")),

        /** The style property -moz-image-region. */
        MOZ_IMAGE_REGION("MozImageRegion", "-moz-image-region", ff("auto")),

        /** The style property -moz-margin-end. */
        MOZ_MARGIN_END("MozMarginEnd", "-moz-margin-end", ff("")),

        /** The style property -moz-margin-start. */
        MOZ_MARGIN_START("MozMarginStart", "-moz-margin-start", ff("")),

        /** The style property -moz-opacity. */
        MOZ_OPACITY("MozOpacity", "-moz-opacity"),

        /** The style property -moz-orient. */
        MOZ_ORIENT("MozOrient", "-moz-orient", ff("auto"), ff("horizontal")),

        /** The style property -moz-outline. */
        MOZ_OUTLINE("MozOutline", "-moz-outline"),

        /** The style property -moz-outline-color. */
        MOZ_OUTLINE_COLOR("MozOutlineColor", "-moz-outline-color"),

        /** The style property -moz-outline-offset. */
        MOZ_OUTLINE_OFFSET("MozOutlineOffset", "-moz-outline-offset"),

        /** The style property -mz-outline-radius. */
        MOZ_OUTLINE_RADIUS("MozOutlineRadius", "-mz-outline-radius", ff("")),

        /** The style property -moz-outline-radius-bottomleft. */
        MOZ_OUTLINE_RADIUS_BOTTOMLEFT("MozOutlineRadiusBottomleft",
                "-moz-outline-radius-bottomleft", ff("0px")),

        /** The style property -moz-outline-radius-bottomright. */
        MOZ_OUTLINE_RADIUS_BOTTOMRIGHT("MozOutlineRadiusBottomright",
                "-moz-outline-radius-bottomright", ff("0px")),

        /** The style property -moz-outline-radius-topleft. */
        MOZ_OUTLINE_RADIUS_TOPLEFT("MozOutlineRadiusTopleft",
                "-moz-outline-radius-topleft", ff("0px")),

        /** The style property -moz-outline-radius-topright. */
        MOZ_OUTLINE_RADIUS_TOPRIGHT("MozOutlineRadiusTopright",
                "-moz-outline-radius-topright", ff("0px")),

        /** The style property -moz-outline-style. */
        MOZ_OUTLINE_STYLE("MozOutlineStyle", "-moz-outline-style"),

        /** The style property -moz-outline-width. */
        MOZ_OUTLINE_WIDTH("MozOutlineWidth", "-moz-outline-width"),

        /** The style property -moz-padding-end. */
        MOZ_PADDING_END("MozPaddingEnd", "-moz-padding-end", ff("")),

        /** The style property -moz-padding-start. */
        MOZ_PADDING_START("MozPaddingStart", "-moz-padding-start", ff("")),

        /** The style property -moz-perspective. */
        MOZ_PERSPECTIVE("MozPerspective", "-moz-perspective", ff("none")),

        /** The style property -moz-perspective-origin. */
        MOZ_PERSPECTIVE_ORIGIN("MozPerspectiveOrigin",
                "-moz-perspective-origin", ff("621px 172.5px")),

        /** The style property -moz-stack-sizing. */
        MOZ_STACK_SIZING("MozStackSizing", "-moz-stack-sizing",
                ff("stretch-to-fit")),

        /** The style property -moz-tab-size. */
        MOZ_TAB_SIZE("MozTabSize", "-moz-tab-size", ff("8")),

        /** The style property -moz-text-align-last. */
        MOZ_TEXT_ALIGN_LAST("MozTextAlignLast", "-moz-text-align-last",
                ff("auto")),

        /** The style property -moz-text-blink. */
        MOZ_TEXT_BLINK("MozTextBlink", "-moz-text-blink", ffBelow31("none")),

        /** The style property -moz-text-decoration-color. */
        MOZ_TEXT_DECORATION_COLOR("MozTextDecorationColor",
                "-moz-text-decoration-color", ff("rgb(0, 0, 0)")),

        /** The style property -moz-text-decoration-line. */
        MOZ_TEXT_DECORATION_LINE("MozTextDecorationLine",
                "-moz-text-decoration-line", ff("none")),

        /** The style property -moz-text-decoration-style. */
        MOZ_TEXT_DECORATION_STYLE("MozTextDecorationStyle",
                "-moz-text-decoration-style", ff("solid")),

        /** The style property -moz-text-size-adjust. */
        MOZ_TEXT_SIZE_ADJUST("MozTextSizeAdjust", "-moz-text-size-adjust",
                ff("auto")),

        /** The style property -moz-transform. */
        MOZ_TRANSFORM("MozTransform", "-moz-transform", ff("none")),

        /** The style property -moz-transform-origin. */
        MOZ_TRANSFORM_ORIGIN("MozTransformOrigin", "-moz-transform-origin", ff("621px 172.5px")),

        /** The style property -moz-transform-style. */
        MOZ_TRANSFORM_STYLE("MozTransformStyle", "-moz-transform-style",
                ff("flat")),

        /** The style property -moz-transition. */
        MOZ_TRANSITION("MozTransition", "-moz-transition", ff("")),

        /** The style property -moz-transition-delay. */
        MOZ_TRANSITION_DELAY("MozTransitionDelay", "-moz-transition-delay",
                ff("0s")),

        /** The style property -moz-transition-duration. */
        MOZ_TRANSITION_DURATION("MozTransitionDuration",
                "-moz-transition-duration", ff("0s")),

        /** The style property -moz-transition-property. */
        MOZ_TRANSITION_PROPERTY("MozTransitionProperty",
                "-moz-transition-property", ff("all")),

        /** The style property -moz-transition-timing-function. */
        MOZ_TRANSITION_TIMING_FUNCTION("MozTransitionTimingFunction",
                "-moz-transition-timing-function",
                ff("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property -moz-user-focus. */
        MOZ_USER_FOCUS("MozUserFocus", "-moz-user-focus", ff("none")),

        /** The style property -moz-user-input. */
        MOZ_USER_INPUT("MozUserInput", "-moz-user-input", ff("auto")),

        /** The style property -moz-user-modify. */
        MOZ_USER_MODIFY("MozUserModify", "-moz-user-modify", ff("read-only")),

        /** The style property -moz-user-select. */
        MOZ_USER_SELECT("MozUserSelect", "-moz-user-select", ff("auto")),

        /** The style property -moz-window-dragging. */
        MOZ_WINDOW_DRAGGING("MozWindowDragging", "-moz-window-dragging", ff38up("no-drag")),

        /** The style property -moz-window-shadow. */
        MOZ_WINDOW_SHADOW("MozWindowShadow", "-moz-window-shadow", ff("default")),

        /** The style property -ms-animation. */
        MS_ANIMATION("msAnimation", "-ms-animation", ie11up("")),

        /** The style property -ms-animation-delay. */
        MS_ANIMATION_DELAY("msAnimationDelay", "-ms-animation-delay",
                ie11up("0s")),

        /** The style property -ms-animation-direction. */
        MS_ANIMATION_DIRECTION("msAnimationDirection",
                "-ms-animation-direction", ie11up("normal")),

        /** The style property -ms-animation-duration. */
        MS_ANIMATION_DURATION("msAnimationDuration",
                "-ms-animation-duration", ie11up("0s")),

        /** The style property -ms-animation-fill-mode. */
        MS_ANIMATION_FILL_MODE("msAnimationFillMode",
                "-ms-animation-fill-mode", ie11up("none")),

        /** The style property -ms-animation-iteration-count. */
        MS_ANIMATION_ITERATION_COUNT("msAnimationIterationCount",
                "-ms-animation-iteration-count", ie11up("1")),

        /** The style property -ms-animation-name. */
        MS_ANIMATION_NAME("msAnimationName", "-ms-annimation-name",
                ie11up("none")),

        /** The style property -ms-animation-play-state. */
        MS_ANIMATION_PLAY_STATE("msAnimationPlayState",
                "-ms-animation-play-state", ie11up("running")),

        /** The style property -ms-animation-timing-function. */
        MS_ANIMATION_TIMING_FUNCTION("msAnimationTimingFunction",
                "-ms-animation-timing-function",
                ie11up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property -ms-backface-visibility. */
        MS_BACKFACE_VISIBILITY("msBackfaceVisibility",
                "-ms-backface-visibility", ie11up("visible")),

        /** The style property -ms-block-progression. */
        MS_BLOCK_PROGRESSION("msBlockProgression", "-ms-block-progression",
                ie11up("undefined")),

        /** The style property -ms-content-zoom-chaining. */
        MS_CONTENT_ZOOM_CHAINING("msContentZoomChaining",
                "-ms-content-zoom-chaining", ie11up("none")),

        /** The style property -ms-content-zoom-limit. */
        MS_CONTENT_ZOOM_LIMIT("msContentZoomLimit", "-ms-content-zoom-limit",
                ie11up("")),

        /** The style property -ms-content-zoom-limit-max. */
        MS_CONTENT_ZOOM_LIMIT_MAX("msContentZoomLimitMax", "-ms-content-zoom-limit-max",
                ie11up("400%")),

        /** The style property -ms-content-zoom-limit-min. */
        MS_CONTENT_ZOOM_LIMIT_MIN("msContentZoomLimitMin", "-ms-content-zoom-limit-min",
                ie11up("100%")),

        /** The style property -ms-content-zoom-snap. */
        MS_CONTENT_ZOOM_SNAP("msContentZoomSnap",
                "-ms-content-zoom-snap", ie11up("none snapInterval(0%, 100%)")),

        /** The style property -ms-content-zoom-snap-points. */
        MS_CONTENT_ZOOM_SNAP_POINTS("msContentZoomSnapPoints",
                "-ms-content-zoom-snap-points", ie11up("snapInterval(0%, 100%)")),

        /** The style property -ms-content-zoom-snap-type. */
        MS_CONTENT_ZOOM_SNAP_TYPE("msContentZoomSnapType", "-ms-content-zoom-snap-type", ie11up("none")),

        /** The style property -ms-content-zooming. */
        MS_CONTENT_ZOOMING("msContentZooming", "-ms-content-zooming", ie11up("none")),

        /** The style property -ms-flex. */
        MS_FLEX("msFlex", "-ms-flex", ie11up("0 1 auto")),

        /** The style property -ms-flex-align. */
        MS_FLEX_ALIGN("msFlexAlign", "-ms-flex-align", ie11up("stretch")),

        /** The style property -ms-flex-direction. */
        MS_FLEX_DIRECTION("msFlexDirection", "-ms-flex-direction", ie11up("row")),

        /** The style property -ms-flex-flow. */
        MS_FLEX_FLOW("msFlexFlow", "-ms-flex-flow", ie11up("row nowrap")),

        /** The style property -ms-flex-item-align. */
        MS_FLEX_ITEM_ALIGN("msFlexItemAlign", "-ms-flex-item-align", ie11up("auto")),

        /** The style property -ms-flex-line-pack. */
        MS_FLEX_LINE_PACK("msFlexLinePack", "-ms-flex-line-pack", ie11up("stretch")),

        /** The style property -ms-flex-negative. */
        MS_FLEX_NEGATIVE("msFlexNegative", "-ms-flex-negative", ie11up("1")),

        /** The style property -ms-flex-order. */
        MS_FLEX_ORDER("msFlexOrder", "-ms-flex-order", ie11up("0")),

        /** The style property -ms-flex-pack. */
        MS_FLEX_PACK("msFlexPack", "-ms-flex-pack", ie11up("start")),

        /** The style property -ms-flex-positive. */
        MS_FLEX_POSITIVE("msFlexPositive", "-ms-flex-positive", ie11up("0")),

        /** The style property -ms-flex-preferred-size. */
        MS_FLEX_PREFERRED_SIZE("msFlexPreferredSize", "-ms-flex-preferred-size", ie11up("auto")),

        /** The style property -ms-flex-wrap. */
        MS_FLEX_WRAP("msFlexWrap", "-ms-flex-wrap", ie11up("nowrap")),

        /** The style property -ms-flow-from. */
        MS_FLOW_FROM("msFlowFrom", "-ms-flow-from", ie11up("none")),

        /** The style property -ms-flow-into. */
        MS_FLOW_INTO("msFlowInto", "-ms-flow-into", ie11up("none")),

        /** The style property -ms-font-feature-settings. */
        MS_FONT_FEATURE_SETTINGS("msFontFeatureSettings", "-ms-font-feature-settings", ie11up("normal")),

        /** The style property -ms-grid-column. */
        MS_GRID_COLUMN("msGridColumn", "-ms-grid-column", ie11up("1")),

        /** The style property -ms-grid-column-align. */
        MS_GRID_COLUMN_ALIGN("msGridColumnAlign", "-ms-grid-column-align", ie11up("stretch")),

        /** The style property -ms-grid-column-span. */
        MS_GRID_COLUMN_SPAN("msGridColumnSpan", "-ms-grid-column-span", ie11up("1")),

        /** The style property -ms-grid-columns. */
        MS_GRID_COLUMNS("msGridColumns", "-ms-grid-columns", ie11up("none")),

        /** The style property -ms-grid-row. */
        MS_GRID_ROW("msGridRow", "-ms-grid-row", ie11up("1")),

        /** The style property -ms-grid-row-align. */
        MS_GRID_ROW_ALIGN("msGridRowAlign", "-ms-grid-row-align", ie11up("stretch")),

        /** The style property -ms-grid-row-span. */
        MS_GRID_ROW_SPAN("msGridRowSpan", "-ms-grid-row-span", ie11up("1")),

        /** The style property -ms-grid-rows. */
        MS_GRID_ROWS("msGridRows", "-ms-grid-rows", ie11up("none")),

        /** The style property -ms-high-contrast-adjust. */
        MS_HIGH_CONTRAST_ADJUST("msHighContrastAdjust", "-ms-high-contrast-adjust", ie11up("auto")),

        /** The style property -ms-hyphenate-limit-chars. */
        MS_HYPHENATE_LIMIT_CHARS("msHyphenateLimitChars", "-ms-hyphenate-limit-chars", ie11up("5 2 2")),

        /** The style property -ms-hyphenate-limit-lines. */
        MS_HYPHENATE_LIMIT_LINES("msHyphenateLimitLines", "-ms-hyphenate-limit-lines", ie11up("no-limit")),

        /** The style property -ms-hyphenate-limit-zone. */
        MS_HYPHENATE_LIMIT_ZONE("msHyphenateLimitZone", "-ms-hyphenate-limit-zone", ie11up("0px")),

        /** The style property -ms-hyphens. */
        MS_HYPHENS("msHyphens", "-ms-hyphens", ie11up("manual")),

        /** The style property -ms-ime-align. */
        MS_IME_ALIGN("msImeAlign", "-ms-ime-align", ie11up("")),

        /** The style property -ms-interpolation-mode. */
        MS_INTERPOLATION_MODE("msInterpolationMode", "-ms-interpolation-mode", ie11up("undefined")),

        /** The style property -ms-overflow-style. */
        MS_OVERFLOW_STYLE("msOverflowStyle", "-ms-overflow-style", ie11up("scrollbar")),

        /** The style property -ms-perspective. */
        MS_PERSPECTIVE("msPerspective", "-ms-perspective", ie11up("none")),

        /** The style property -ms-perspective-origin. */
        MS_PERSPECTIVE_ORIGIN("msPerspectiveOrigin", "-ms-perspective-origin", ie11up("620px 163.2px")),

        /** The style property -ms-scroll-chaining. */
        MS_SCROLL_CHAINING("msScrollChaining", "-ms-scroll-chaining", ie11up("chained")),

        /** The style property -ms-scroll-limit. */
        MS_SCROLL_LIMIT("msScrollLimit", "-ms-scroll-limit", ie11up("")),

        /** The style property -ms-scroll-limit-x-max. */
        MS_SCROLL_LIMIT_X_MAX("msScrollLimitXMax", "-ms-scroll-limit-x-max", ie11up("0px")),

        /** The style property -ms-scroll-limit-x-min. */
        MS_SCROLL_LIMIT_X_MIN("msScrollLimitXMin", "-ms-scroll-limit-x-min", ie11up("0px")),

        /** The style property -ms-scroll-limit. */
        MS_SCROLL_LIMIT_Y_MAX("msScrollLimitYMax", "-ms-scroll-limit-y-max", ie11up("0px")),

        /** The style property -ms-scroll-limit. */
        MS_SCROLL_LIMIT_Y_MIN("msScrollLimitYMin", "-ms-scroll-limit-y-min", ie11up("0px")),

        /** The style property -ms-scroll-rails. */
        MS_SCROLL_RAILS("msScrollRails", "-ms-scroll-rails", ie11up("railed")),

        /** The style property -ms-scroll-snap-points-x. */
        MS_SCROLL_SNAP_POINTS_X("msScrollSnapPointsX", "-ms-scroll-snap-points-x", ie11up("snapInterval(0%, 100%)")),

        /** The style property -ms-scroll-snap-points-y. */
        MS_SCROLL_SNAP_POINTS_Y("msScrollSnapPointsY", "-ms-scroll-snap-points-y", ie11up("snapInterval(0%, 100%)")),

        /** The style property -ms-scroll-snap-type. */
        MS_SCROLL_SNAP_TYPE("msScrollSnapType", "-ms-scroll-snap-type", ie11up("none")),

        /** The style property -ms-scroll-snap-x. */
        MS_SCROLL_SNAP_X("msScrollSnapX", "-ms-scroll-snap-x", ie11up("none snapInterval(0%, 100%)")),

        /** The style property -ms-scroll-snap-y. */
        MS_SCROLL_SNAP_Y("msScrollSnapY", "-ms-scroll-snap-y", ie11up("none snapInterval(0%, 100%)")),

        /** The style property -ms-scroll-translation. */
        MS_SCROLL_TRANSLATION("msScrollTranslation", "-ms-scroll-translation", ie11up("none")),

        /** The style property -ms-text-combine-horizontal. */
        MS_TEXT_COMBINE_HORIZONTAL("msTextCombineHorizontal", "-ms-text-combine-horizontal", ie11up("none")),

        /** The style property -ms-touch-action. */
        MS_TOUCH_ACTION("msTouchAction", "-ms-touch-action", ie11up("auto")),

        /** The style property -ms-touch-select. */
        MS_TOUCH_SELECT("msTouchSelect", "-ms-touch-select", ie11up("")),

        /** The style property -ms-transform. */
        MS_TRANSFORM("msTransform", "-ms-transform", ie11up("none")),

        /** The style property -ms-transform-origin. */
        MS_TRANSFORM_ORIGIN("msTransformOrigin", "-ms-transform-origin", ie11up("620px 163.2px")),

        /** The style property -ms-transform-style. */
        MS_TRANSFORM_STYLE("msTransformStyle", "-ms-transform-style", ie11up("flat")),

        /** The style property -ms-transition. */
        MS_TRANSITION("msTransition", "-ms-transition", ie11up("")),

        /** The style property -ms-transition-delay. */
        MS_TRANSITION_DELAY("msTransitionDelay", "-ms-transition-delay", ie11up("0s")),

        /** The style property -ms-transition-duration. */
        MS_TRANSITION_DURATION("msTransitionDuration",
                "-ms-transition-duration", ie11up("0s")),

        /** The style property -ms-transition-property. */
        MS_TRANSITION_PROPERTY("msTransitionProperty",
                "-ms-transition-property", ie11up("all")),

        /** The style property -ms-transition-timing-function. */
        MS_TRANSITION_TIMING_FUNCTION("msTransitionTimingFunction",
                "-ms-transition-timing-function",
                ie11up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property -ms-user-select. */
        MS_USER_SELECT("msUserSelect", "-ms-user-select", ie11up("text")),

        /** The style property -ms-wrap-flow. */
        MS_WRAP_FLOW("msWrapFlow", "-ms-wrap-flow", ie11up("auto")),

        /** The style property -ms-wrap-margin. */
        MS_WRAP_MARGIN("msWrapMargin", "-ms-wrap-margin", ie11up("auto")),

        /** The style property -ms-wrap-through. */
        MS_WRAP_THROUGH("msWrapThrough", "-ms-wrap-through", ie11up("wrap")),

        /** The style property {@code object-fit}. */
        OBJECT_FIT("objectFit", "object-fit", ff38up("fill"), chrome("fill")),

        /** The style property {@code object-fit}. */
        OBJECT_FIT_("object-fit", "object-fit", ff38up("fill")),

        /** The style property object-position. */
        OBJECT_POSITION("objectPosition", "object-position", ff38up("50% 50%"), chrome("50% 50%")),

        /** The style property {@code object-position}. */
        OBJECT_POSITION_("object-position", "object-position", ff38up("50% 50%")),

        /** The style property {@code opacity}. */
        OPACITY("opacity", "opacity", chrome("1")),

        /** The style property order. */
        ORDER("order", "order", ff("0"), ie11up("0"), chrome("0")),

        /** The style property {@code orientation}. */
        ORIENTATION("orientation", "orientation", chrome("")),

        /** The style property page-break-inside. */
        ORPHANS("orphans", "orphans", ie11up("2"), chrome("auto")),

        /** The style property {@code outline}. */
        OUTLINE("outline", "outline", chrome("rgb(0, 0, 0) none 0px")),

        /** The style property outline-color. */
        OUTLINE_COLOR("outlineColor", "outline-color", ie11up("transparent"), chrome("rgb(0, 0, 0)"),
                ff("rgb(0, 0, 0)")),

        /** The style property {@code outline-color}. */
        OUTLINE_COLOR_("outline-color", "outline-color", ff38up("rgb(0, 0, 0)")),

        /** The style property {@code outlineOffset}. */
        OUTLINE_OFFSET("outlineOffset", "outline-offset", chrome("0px")),

        /** The style property {@code outline-offset}. */
        OUTLINE_OFFSET_("outline-offset", "outline-offset", ff38up("0px")),

        /** The style property {@code outlineStyle}. */
        OUTLINE_STYLE("outlineStyle", "outline-style", chrome("none")),

        /** The style property {@code outline-style}. */
        OUTLINE_STYLE_("outline-style", "outline-style", ff38up("none")),

        /** The style property {@code outlineWidth}. */
        OUTLINE_WIDTH("outlineWidth", "outline-width", chrome("0px")),

        /** The style property {@code outline-width}. */
        OUTLINE_WIDTH_("outline-width", "outline-width", ff38up("0px")),

        /** The style property {@code overflow}. */
        OVERFLOW("overflow", "overflow", chrome("visible")),

        /** The style property {@code overflowWrap}. */
        OVERFLOW_WRAP("overflowWrap", "overflow-wrap", chrome("normal")),

        /** The style property {@code overflowX}. */
        OVERFLOW_X("overflowX", "overflow-x", chrome("visible")),

        /** The style property {@code overflow-x}. */
        OVERFLOW_X_("overflow-x", "overflow-x", ff38up("visible")),

        /** The style property {@code overflowY}. */
        OVERFLOW_Y("overflowY", "overflow-y", chrome("visible")),

        /** The style property {@code overflow-y}. */
        OVERFLOW_Y_("overflow-y", "overflow-y", ff38up("visible")),

        /** The style property {@code padding}. */
        PADDING("padding", "padding", chrome("0px")),

        /** The style property {@code paddingBottom}. */
        PADDING_BOTTOM("paddingBottom", "padding-bottom", chrome("0px")),

        /** The style property {@code padding-bottom}. */
        PADDING_BOTTOM_("padding-bottom", "padding-bottom", ff38up("0px")),

        /** The style property {@code paddingLeft}. */
        PADDING_LEFT("paddingLeft", "padding-left", chrome("0px")),

        /** The style property {@code padding-left}. */
        PADDING_LEFT_("padding-left", "padding-left", ff38up("0px")),

        /** The style property {@code paddingRight}. */
        PADDING_RIGHT("paddingRight", "padding-right", chrome("0px")),

        /** The style property {@code padding-right}. */
        PADDING_RIGHT_("padding-right", "padding-right", ff38up("0px")),

        /** The style property {@code paddingTop}. */
        PADDING_TOP("paddingTop", "padding-top", chrome("0px")),

        /** The style property {@code padding-top}. */
        PADDING_TOP_("padding-top", "padding-top", ff38up("0px")),

        /** The style property {@code page}. */
        PAGE("page", "page", chrome("")),

        /** The style property {@code pageBreakAfter}. */
        PAGE_BREAK_AFTER("pageBreakAfter", "page-break-after", chrome("auto")),

        /** The style property {@code page-break-after}. */
        PAGE_BREAK_AFTER_("page-break-after", "page-break-after", ff38up("auto")),

        /** The style property {@code pageBreakBefore}. */
        PAGE_BREAK_BEFORE("pageBreakBefore", "page-break-before", chrome("auto")),

        /** The style property {@code page-break-before}. */
        PAGE_BREAK_BEFORE_("page-break-before", "page-break-before", ff38up("auto")),

        /** The style property page-break-inside. */
        PAGE_BREAK_INSIDE("pageBreakInside", "page-break-inside", ff("auto"), ie11up("auto"), chrome("auto")),

        /** The style property {@code page-break-inside}. */
        PAGE_BREAK_INSIDE_("page-break-inside", "page-break-inside", ff38up("auto")),

        /** The style property paint-order. */
        PAINT_ORDER("paintOrder", "paint-order", ff31up("normal"), chrome("fill stroke markers")),

        /** The style property {@code paint-order}. */
        PAINT_ORDER_("paint-order", "paint-order", ff38up("normal")),

        /** The style property pause. */
        PAUSE("pause", "pause"),

        /** The style property pause-after. */
        PAUSE_AFTER("pauseAfter", "pause-after"),

        /** The style property pause-before. */
        PAUSE_BEFORE("pauseBefore", "pause-before"),

        /** The style property perspective. */
        PERSPECTIVE("perspective", "perspective", ff("none"), ie11up("none"), chrome("none")),

        /** The style property perspective-origin. */
        PERSPECTIVE_ORIGIN("perspectiveOrigin", "perspective-origin",
                ff("621px 172.5px"), ie11up("620px 163.2px"), chrome("620px 161px")),

        /** The style property {@code perspective-origin}. */
        PERSPECTIVE_ORIGIN_("perspective-origin", "perspective-origin", ff38up("621px 172.5px")),

        /** The style property pitch. */
        PITCH("pitch", "pitch"),

        /** The style property pitch-range. */
        PITCH_RANGE("pitchRange", "pitch-range"),

        /** The style property pointer-events. */
        POINTER_EVENTS("pointerEvents", "pointer-events", ie11up("visiblePainted"), chrome("auto"), ff("auto")),

        /** The style property {@code pointer-events}. */
        POINTER_EVENTS_("pointer-events", "pointer-events", ff38up("auto")),

        /** The style property {@code position}. */
        POSITION("position", "position", chrome("static")),

        /** The style property quotes. */
        QUOTES("quotes", "quotes", ff("\"“\" \"”\" \"‘\" \"’\""), ie8up(""), chrome("")),

        /** The style property {@code r}. */
        R("r", "r", chrome("0px")),

        /** The style property resize. */
        RESIZE("resize", "resize", ff("none"), chrome("none")),

        /** The style property richness. */
        RICHNESS("richness", "richness"),

        /** The style property {@code right}. */
        RIGHT("right", "right", chrome("auto")),

        /** The style property ruby-align. */
        RUBY_ALIGN("rubyAlign", "ruby-align", ff38up("space-around")),

        /** The style property {@code ruby-align}. */
        RUBY_ALIGN_("ruby-align", "ruby-align", ff38up("space-around")),

        /** The style property ruby-overhang. */
        RUBY_OVERHANG("rubyOverhang", "ruby-overhang", ie11up("auto")),

        /** The style property ruby-position. */
        RUBY_POSITION("rubyPosition", "ruby-position", ie11up("above"), ff38up("over")),

        /** The style property {@code ruby-position}. */
        RUBY_POSITION_("ruby-position", "ruby-position", ff38up("over")),

        /** The style property {@code rx}. */
        RX("rx", "rx", chrome("0px")),

        /** The style property {@code ry}. */
        RY("ry", "ry", chrome("0px")),

        /** The style property scroll-behavior. */
        SCROLL_BEHAVIOR("scrollBehavior", "scroll-behavior", ff38up("auto")),

        /** The style property {@code scroll-behavior}. */
        SCROLL_BEHAVIOR_("scroll-behavior", "scroll-behavior", ff38up("auto")),

        /** The style property scrollbar-3dlight-color. */
        SCROLLBAR_3DLIGHT_COLOR("scrollbar3dLightColor", "scrollbar-3dlight-color", ie11up("undefined")),

        /** The style property scrollbar-arrow-color. */
        SCROLLBAR_ARROW_COLOR("scrollbarArrowColor", "scrollbar-arrow-color", ie11up("undefined")),

        /** The style property scrollbar-base-color. */
        SCROLLBAR_BASE_COLOR("scrollbarBaseColor", "scrollbar-base-color", ie11up("undefined")),

        /** The style property scrollbar-darkshadow-color. */
        SCROLLBAR_DARKSHADOW_COLOR("scrollbarDarkShadowColor", "scrollbar-darkshadow-color", ie11up("undefined")),

        /** The style property scrollbar-face-color. */
        SCROLLBAR_FACE_COLOR("scrollbarFaceColor", "scrollbar-face-color", ie11up("undefined")),

        /** The style property scrollbar-highlight-color. */
        SCROLLBAR_HIGHLIGHT_COLOR("scrollbarHighlightColor", "scrollbar-highlight-color", ie11up("undefined")),

        /** The style property scrollbar-shadow-color. */
        SCROLLBAR_SHADOW_COLOR("scrollbarShadowColor", "scrollbar-shadow-color", ie11up("undefined")),

        /** The style property scrollbar-track-color. */
        SCROLLBAR_TRACK_COLOR("scrollbarTrackColor", "scrollbar-track-color", ie11up("undefined")),

        /** The style property {@code shapeImageThreshold}. */
        SHAPE_IMAGE_THRESHOLD("shapeImageThreshold", "shape-image-threshold", chrome("0")),

        /** The style property {@code shapeMargin}. */
        SHAPE_MARGIN("shapeMargin", "shape-margin", chrome("0px")),

        /** The style property {@code shapeOutside}. */
        SHAPE_OUTSIDE("shapeOutside", "shape-outside", chrome("none")),

        /** The style property shape-rendering. */
        SHAPE_RENDERING("shapeRendering", "shape-rendering", ff("auto"), chrome("auto")),

        /** The style property {@code shape-rendering}. */
        SHAPE_RENDERING_("shape-rendering", "shape-rendering", ff38up("auto")),

        /** The style property {@code size}. */
        SIZE("size", "size", chrome("")),

        /** The style property speak. */
        SPEAK("speak", "speak", chrome("normal")),

        /** The style property speak. */
        SPEAK_HEADER("speakHeader", "speak-header"),

        /** The style property speak-numeral. */
        SPEAK_NUMERAL("speakNumeral", "speak-numeral"),

        /** The style property speak-punktuation. */
        SPEAK_PUNCTUATION("speakPunctuation", "speak-punctuation"),

        /** The style property speech-rate. */
        SPEECH_RATE("speechRate", "speech-rate"),

        /** The style property {@code src}. */
        SRC("src", "src", chrome("")),

        /** The style property stop-color. */
        STOP_COLOR("stopColor", "stop-color", ff("rgb(0, 0, 0)"), ie11up(""), chrome("rgb(0, 0, 0)")),

        /** The style property {@code stop-color}. */
        STOP_COLOR_("stop-color", "stop-color", ff38up("rgb(0, 0, 0)")),

        /** The style property stop-opacity. */
        STOP_OPACITY("stopOpacity", "stop-opacity", ff("1"), ie11up("1"), chrome("1")),

        /** The style property {@code stop-opacity}. */
        STOP_OPACITY_("stop-opacity", "stop-opacity", ff38up("1")),

        /** The style property stress. */
        STRESS("stress", "stress"),

        /** The style property stroke. */
        STROKE("stroke", "stroke", ff("none"), ie11up(""), chrome("none")),

        /** The style property stroke-dasharray. */
        STROKE_DASHARRAY("strokeDasharray", "stroke-dasharray", ff("none"), ie11up("none"), chrome("none")),

        /** The style property {@code stroke-dasharray}. */
        STROKE_DASHARRAY_("stroke-dasharray", "stroke-dasharray", ff38up("none")),

        /** The style property stroke-dashoffset. */
        STROKE_DASHOFFSET("strokeDashoffset", "stroke-dashoffset", ff("0px"), ie11up("0px"), chrome("0px")),

        /** The style property {@code stroke-dashoffset}. */
        STROKE_DASHOFFSET_("stroke-dashoffset", "stroke-dashoffset", ff38up("0px")),

        /** The style property stroke-linecap. */
        STROKE_LINECAP("strokeLinecap", "stroke-linecap", ff("butt"), ie11up("butt"), chrome("butt")),

        /** The style property {@code stroke-linecap}. */
        STROKE_LINECAP_("stroke-linecap", "stroke-linecap", ff38up("butt")),

        /** The style property stroke-linejoin. */
        STROKE_LINEJOIN("strokeLinejoin", "stroke-linejoin", ff("miter"), ie11up("miter"), chrome("miter")),

        /** The style property {@code stroke-linejoin}. */
        STROKE_LINEJOIN_("stroke-linejoin", "stroke-linejoin", ff38up("miter")),

        /** The style property stroke-miterlimit. */
        STROKE_MITERLIMIT("strokeMiterlimit", "stroke-miterlimit", ff("4"), ie11up("4"), chrome("4")),

        /** The style property {@code stroke-miterlimit}. */
        STROKE_MITERLIMIT_("stroke-miterlimit", "stroke-miterlimit", ff38up("4")),

        /** The style property stroke-opacity. */
        STROKE_OPACITY("strokeOpacity", "stroke-opacity", ff("1"), ie11up("1"), chrome("1")),

        /** The style property {@code stroke-opacity}. */
        STROKE_OPACITY_("stroke-opacity", "stroke-opacity", ff38up("1")),

        /** The style property stroke-width. */
        STROKE_WIDTH("strokeWidth", "stroke-width", ff("1px"), ie11up("0.01px"), chrome("1px")),

        /** The style property {@code stroke-width}. */
        STROKE_WIDTH_("stroke-width", "stroke-width", ff38up("1px")),

        /** The style property style-float. */
        STYLE_FLOAT("styleFloat", "style-float", ie11up("undefined")),

        /** The style property {@code tabSize}. */
        TAB_SIZE("tabSize", "tab-size", chrome("8")),

        /** The style property {@code tableLayout}. */
        TABLE_LAYOUT("tableLayout", "table-layout", chrome("auto")),

        /** The style property {@code table-layout}. */
        TABLE_LAYOUT_("table-layout", "table-layout", ff38up("auto")),

        /** The style property text-align. */
        TEXT_ALIGN("textAlign", "text-align", ie11up("left"), chrome("start"), ff("start")),

        /** The style property {@code text-align}. */
        TEXT_ALIGN_("text-align", "text-align", ff38up("start")),

        /** The style property text-align-last. */
        TEXT_ALIGN_LAST("textAlignLast", "text-align-last", ie11up("auto")),

        /** The style property text-anchor. */
        TEXT_ANCHOR("textAnchor", "text-anchor", ff("start"), ie11up("start"), chrome("start")),

        /** The style property {@code text-anchor}. */
        TEXT_ANCHOR_("text-anchor", "text-anchor", ff38up("start")),

        /** The style property text-autospace. */
        TEXT_AUTOSPACE("textAutospace", "text-autospace", ie11up("undefined")),

        /** The style property {@code textDecoration}. */
        TEXT_DECORATION("textDecoration", "text-decoration", chrome("none")),

        /** The style property {@code text-decoration}. */
        TEXT_DECORATION_("text-decoration", "text-decoration", ff38up("none")),

        /** The style property text-decoration-color. */
        TEXT_DECORATION_COLOR("textDecorationColor", "text-decoration-color", ff38up("rgb(0, 0, 0)")),

        /** The style property {@code text-decoration-color}. */
        TEXT_DECORATION_COLOR_("text-decoration-color", "text-decoration-color", ff38up("rgb(0, 0, 0)")),

        /** The style property text-decoration-line. */
        TEXT_DECORATION_LINE("textDecorationLine", "text-decoration-line", ff38up("none")),

        /** The style property {@code text-decoration-line}. */
        TEXT_DECORATION_LINE_("text-decoration-line", "text-decoration-line", ff38up("none")),

        /** The style property text-decoration-style. */
        TEXT_DECORATION_STYLE("textDecorationStyle", "text-decoration-style", ff38up("solid")),

        /** The style property {@code text-decoration-style}. */
        TEXT_DECORATION_STYLE_("text-decoration-style", "text-decoration-style", ff38up("solid")),

        /** The style property {@code textIndent}. */
        TEXT_INDENT("textIndent", "text-indent", chrome("0px")),

        /** The style property {@code text-indent}. */
        TEXT_INDENT_("text-indent", "text-indent", ff38up("0px")),

        /** The style property text-justify. */
        TEXT_JUSTIFY("textJustify", "text-justify", ie11up("auto")),

        /** The style property text-justify-trim. */
        TEXT_JUSTIFY_TRIM("textJustifyTrim", "text-justify-trim", ie11up("undefined")),

        /** The style property text-kashida. */
        TEXT_KASHIDA("textKashida", "text-kashida", ie11up("undefined")),

        /** The style property text-kashida-space. */
        TEXT_KASHIDA_SPACE("textKashidaSpace", "text-kashida-space", ie11up("undefined")),

        /** The style property text-overflow. */
        TEXT_OVERFLOW("textOverflow", "text-overflow", ff("clip"), ie11up("clip"), chrome("clip")),

        /** The style property {@code text-overflow}. */
        TEXT_OVERFLOW_("text-overflow", "text-overflow", ff38up("clip")),

        /** The style property text-rendering. */
        TEXT_RENDERING("textRendering", "text-rendering", ff("auto"), chrome("auto")),

        /** The style property {@code text-rendering}. */
        TEXT_RENDERING_("text-rendering", "text-rendering", ff38up("auto")),

        /** The style property {@code textShadow}. */
        TEXT_SHADOW("textShadow", "text-shadow", chrome("none"), ff("none"), ie11up("none")),

        /** The style property {@code text-shadow}. */
        TEXT_SHADOW_("text-shadow", "text-shadow", ff38up("none")),

        /** The style property {@code textTransform}. */
        TEXT_TRANSFORM("textTransform", "text-transform", chrome("none")),

        /** The style property {@code text-transform}. */
        TEXT_TRANSFORM_("text-transform", "text-transform", ff38up("none")),

        /** The style property text-underline-position. */
        TEXT_UNDERLINE_POSITION("textUnderlinePosition", "text-underline-position", ie11up("auto")),

        /** The style property {@code top}. */
        TOP("top", "top", chrome("auto")),

        /** The style property touch-action. */
        TOUCH_ACTION("touchAction", "touch-action", ie11up("auto"), chrome("auto")),

        /** The style property transform. */
        TRANSFORM("transform", "transform", ff("none"), ie11up("none"), chrome("none")),

        /** The style property transform-origin. */
        TRANSFORM_ORIGIN("transformOrigin", "transform-origin",
                ff("621px 172.5px"), ie11up("620px 163.2px"), chrome("620px 161px")),

        /** The style property {@code transform-origin}. */
        TRANSFORM_ORIGIN_("transform-origin", "transform-origin", ff38up("621px 172.5px")),

        /** The style property transform-style. */
        TRANSFORM_STYLE("transformStyle", "transform-style", ff("flat"), ie11up("flat"), chrome("flat")),

        /** The style property {@code transform-style}. */
        TRANSFORM_STYLE_("transform-style", "transform-style", ff38up("flat")),

        /** The style property transition. */
        TRANSITION("transition", "transition", ff(""), ie11up(""), chrome("all 0s ease 0s")),

        /** The style property transition-delay. */
        TRANSITION_DELAY("transitionDelay", "transition-delay", ff("0s"), ie11up("0s"), chrome("0s")),

        /** The style property {@code transition-delay}. */
        TRANSITION_DELAY_("transition-delay", "transition-delay", ff38up("0s")),

        /** The style property transition-duration. */
        TRANSITION_DURATION("transitionDuration", "transition-duration", ff("0s"), ie11up("0s"), chrome("0s")),

        /** The style property {@code transition-duration}. */
        TRANSITION_DURATION_("transition-duration", "transition-duration", ff38up("0s")),

        /** The style property transition-property. */
        TRANSITION_PROPERTY("transitionProperty", "transition-property", ff("all"), ie11up("all"), chrome("all")),

        /** The style property {@code transition-property}. */
        TRANSITION_PROPERTY_("transition-property", "transition-property", ff38up("all")),

        /** The style property transition-timing-function. */
        TRANSITION_TIMING_FUNCTION("transitionTimingFunction",
                "transition-timing-function",
                ff("cubic-bezier(0.25, 0.1, 0.25, 1)"),
                ie11up("cubic-bezier(0.25, 0.1, 0.25, 1)"), chrome("ease")),

        /** The style property {@code transition-timing-function}. */
        TRANSITION_TIMING_FUNCTION_("transition-timing-function", "transition-timing-function",
                ff38up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property unicode-bidi. */
        UNICODE_BIDI("unicodeBidi", "unicode-bidi",
                ff("-moz-isolate"), ieBelow11("embed"), ie11up("normal"), chrome("normal")),

        /** The style property {@code unicode-bidi}. */
        UNICODE_BIDI_("unicode-bidi", "unicode-bidi", ff38up("-moz-isolate")),

        /** The style property {@code unicodeRange}. */
        UNICODE_RANGE("unicodeRange", "unicode-range", chrome("")),

        /** The style property {@code userZoom}. */
        USER_ZOOM("userZoom", "user-zoom", chrome("")),

        /** The style property vector-effect. */
        VECTOR_EFFECT("vectorEffect", "vector-effect", ff("none"), chrome("none")),

        /** The style property {@code vector-effect}. */
        VECTOR_EFFECT_("vector-effect", "vector-effect", ff38up("none")),

        /** The style property {@code verticalAlign}. */
        VERTICAL_ALIGN("verticalAlign", "vertical-align", chrome("baseline")),

        /** The style property {@code vertical-align}. */
        VERTICAL_ALIGN_("vertical-align", "vertical-align", ff38up("baseline")),

        /** The style property {@code visibility}. */
        VISIBILITY("visibility", "visibility", chrome("visible")),

        /** The style property "voice-family". */
        VOICE_FAMILY("voiceFamily", "voice-family"),

        /** The style property volume. */
        VOLUME("volume", "volume"),

        /** The style property {@code webkitAnimation}. */
        WEBKIT_ANIMATION("webkitAnimation", "webkit-animation", chrome("none 0s ease 0s 1 normal none running")),

        /** The style property {@code webkitAnimationDelay}. */
        WEBKIT_ANIMATION_DELAY("webkitAnimationDelay", "webkit-animation-delay", chrome("0s")),

        /** The style property {@code webkitAnimationDirection}. */
        WEBKIT_ANIMATION_DIRECTION("webkitAnimationDirection", "webkit-animation-direction", chrome("normal")),

        /** The style property {@code webkitAnimationDuration}. */
        WEBKIT_ANIMATION_DURATION("webkitAnimationDuration", "webkit-animation-duration", chrome("0s")),

        /** The style property {@code webkitAnimationFillMode}. */
        WEBKIT_ANIMATION_FILL_MODE("webkitAnimationFillMode", "webkit-animation-fill-mode", chrome("none")),

        /** The style property {@code webkitAnimationIterationCount}. */
        WEBKIT_ANIMATION_ITERATION_COUNT("webkitAnimationIterationCount", "webkit-animation-iteration-count",
                chrome("1")),

        /** The style property {@code webkitAnimationName}. */
        WEBKIT_ANIMATION_NAME("webkitAnimationName", "webkit-animation-name", chrome("none")),

        /** The style property {@code webkitAnimationPlayState}. */
        WEBKIT_ANIMATION_PLAY_STATE("webkitAnimationPlayState", "webkit-animation-play-state", chrome("running")),

        /** The style property {@code webkitAnimationTimingFunction}. */
        WEBKIT_ANIMATION_TIMING_FUNCTION("webkitAnimationTimingFunction", "webkit-animation-timing-function",
                chrome("ease")),

        /** The style property {@code webkitAppRegion}. */
        WEBKIT_APP_REGION("webkitAppRegion", "webkit-app-region", chrome("no-drag")),

        /** The style property {@code webkitAppearance}. */
        WEBKIT_APPEARANCE("webkitAppearance", "webkit-appearance", chrome("none")),

        /** The style property {@code webkitBackfaceVisibility}. */
        WEBKIT_BACKFACE_VISIBILITY("webkitBackfaceVisibility", "webkit-backface-visibility", chrome("visible")),

        /** The style property {@code webkitBackgroundClip}. */
        WEBKIT_BACKGROUND_CLIP("webkitBackgroundClip", "webkit-background-clip", chrome("border-box")),

        /** The style property {@code webkitBackgroundComposite}. */
        WEBKIT_BACKGROUND_COMPOSITE("webkitBackgroundComposite", "webkit-background-composite", chrome("source-over")),

        /** The style property {@code webkitBackgroundOrigin}. */
        WEBKIT_BACKGROUND_ORIGIN("webkitBackgroundOrigin", "webkit-background-origin", chrome("padding-box")),

        /** The style property {@code webkitBackgroundSize}. */
        WEBKIT_BACKGROUND_SIZE("webkitBackgroundSize", "webkit-background-size", chrome("auto")),

        /** The style property {@code webkitBorderAfter}. */
        WEBKIT_BORDER_AFTER("webkitBorderAfter", "webkit-border-after", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderAfterColor}. */
        WEBKIT_BORDER_AFTER_COLOR("webkitBorderAfterColor", "webkit-border-after-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderAfterStyle}. */
        WEBKIT_BORDER_AFTER_STYLE("webkitBorderAfterStyle", "webkit-border-after-style", chrome("none")),

        /** The style property {@code webkitBorderAfterWidth}. */
        WEBKIT_BORDER_AFTER_WIDTH("webkitBorderAfterWidth", "webkit-border-after-width", chrome("0px")),

        /** The style property {@code webkitBorderBefore}. */
        WEBKIT_BORDER_BEFORE("webkitBorderBefore", "webkit-border-before", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderBeforeColor}. */
        WEBKIT_BORDER_BEFORE_COLOR("webkitBorderBeforeColor", "webkit-border-before-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderBeforeStyle}. */
        WEBKIT_BORDER_BEFORE_STYLE("webkitBorderBeforeStyle", "webkit-border-before-style", chrome("none")),

        /** The style property {@code webkitBorderBeforeWidth}. */
        WEBKIT_BORDER_BEFORE_WIDTH("webkitBorderBeforeWidth", "webkit-border-before-width", chrome("0px")),

        /** The style property {@code webkitBorderEnd}. */
        WEBKIT_BORDER_END("webkitBorderEnd", "webkit-border-end", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderEndColor}. */
        WEBKIT_BORDER_END_COLOR("webkitBorderEndColor", "webkit-border-end-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderEndStyle}. */
        WEBKIT_BORDER_END_STYLE("webkitBorderEndStyle", "webkit-border-end-style", chrome("none")),

        /** The style property {@code webkitBorderEndWidth}. */
        WEBKIT_BORDER_END_WIDTH("webkitBorderEndWidth", "webkit-border-end-width", chrome("0px")),

        /** The style property {@code webkitBorderHorizontalSpacing}. */
        WEBKIT_BORDER_HORIZONTAL_SPACING("webkitBorderHorizontalSpacing", "webkit-border-horizontal-spacing",
                chrome("0px")),

        /** The style property {@code webkitBorderImage}. */
        WEBKIT_BORDER_IMAGE("webkitBorderImage", "webkit-border-image", chrome("none")),

        /** The style property {@code webkitBorderRadius}. */
        WEBKIT_BORDER_RADIUS("webkitBorderRadius", "webkit-border-radius", chrome("")),

        /** The style property {@code webkitBorderStart}. */
        WEBKIT_BORDER_START("webkitBorderStart", "webkit-border-start", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderStartColor}. */
        WEBKIT_BORDER_START_COLOR("webkitBorderStartColor", "webkit-border-start-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderStartStyle}. */
        WEBKIT_BORDER_START_STYLE("webkitBorderStartStyle", "webkit-border-start-style", chrome("none")),

        /** The style property {@code webkitBorderStartWidth}. */
        WEBKIT_BORDER_START_WIDTH("webkitBorderStartWidth", "webkit-border-start-width", chrome("0px")),

        /** The style property {@code webkitBorderVerticalSpacing}. */
        WEBKIT_BORDER_VERTICAL_SPACING("webkitBorderVerticalSpacing", "webkit-border-vertical-spacing", chrome("0px")),

        /** The style property {@code webkitBoxAlign}. */
        WEBKIT_BOX_ALIGN("webkitBoxAlign", "webkit-box-align", chrome("stretch")),

        /** The style property {@code webkitBoxDecorationBreak}. */
        WEBKIT_BOX_DECORATION_BREAK("webkitBoxDecorationBreak", "webkit-box-decoration-break", chrome("slice")),

        /** The style property {@code webkitBoxDirection}. */
        WEBKIT_BOX_DIRECTION("webkitBoxDirection", "webkit-box-direction", chrome("normal")),

        /** The style property {@code webkitBoxFlex}. */
        WEBKIT_BOX_FLEX("webkitBoxFlex", "webkit-box-flex", chrome("0")),

        /** The style property {@code webkitBoxFlexGroup}. */
        WEBKIT_BOX_FLEX_GROUP("webkitBoxFlexGroup", "webkit-box-flex-group", chrome("1")),

        /** The style property {@code webkitBoxLines}. */
        WEBKIT_BOX_LINES("webkitBoxLines", "webkit-box-lines", chrome("single")),

        /** The style property {@code webkitBoxOrdinalGroup}. */
        WEBKIT_BOX_ORDINAL_GROUP("webkitBoxOrdinalGroup", "webkit-box-ordinal-group", chrome("1")),

        /** The style property {@code webkitBoxOrient}. */
        WEBKIT_BOX_ORIENT("webkitBoxOrient", "webkit-box-orient", chrome("horizontal")),

        /** The style property {@code webkitBoxPack}. */
        WEBKIT_BOX_PACK("webkitBoxPack", "webkit-box-pack", chrome("start")),

        /** The style property {@code webkitBoxReflect}. */
        WEBKIT_BOX_REFLECT("webkitBoxReflect", "webkit-box-reflect", chrome("none")),

        /** The style property {@code webkitBoxShadow}. */
        WEBKIT_BOX_SHADOW("webkitBoxShadow", "webkit-box-shadow", chrome("none")),

        /** The style property {@code webkitClipPath}. */
        WEBKIT_CLIP_PATH("webkitClipPath", "webkit-clip-path", chrome("none")),

        /** The style property {@code webkitColumnBreakAfter}. */
        WEBKIT_COLUMN_BREAK_AFTER("webkitColumnBreakAfter", "webkit-column-break-after", chrome("auto")),

        /** The style property {@code webkitColumnBreakBefore}. */
        WEBKIT_COLUMN_BREAK_BEFORE("webkitColumnBreakBefore", "webkit-column-break-before", chrome("auto")),

        /** The style property {@code webkitColumnBreakInside}. */
        WEBKIT_COLUMN_BREAK_INSIDE("webkitColumnBreakInside", "webkit-column-break-inside", chrome("auto")),

        /** The style property {@code webkitColumnCount}. */
        WEBKIT_COLUMN_COUNT("webkitColumnCount", "webkit-column-count", chrome("auto")),

        /** The style property {@code webkitColumnGap}. */
        WEBKIT_COLUMN_GAP("webkitColumnGap", "webkit-column-gap", chrome("normal")),

        /** The style property {@code webkitColumnRule}. */
        WEBKIT_COLUMN_RULE("webkitColumnRule", "webkit-column-rule", chrome("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitColumnRuleColor}. */
        WEBKIT_COLUMN_RULE_COLOR("webkitColumnRuleColor", "webkit-column-rule-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code webkitColumnRuleStyle}. */
        WEBKIT_COLUMN_RULE_STYLE("webkitColumnRuleStyle", "webkit-column-rule-style", chrome("none")),

        /** The style property {@code webkitColumnRuleWidth}. */
        WEBKIT_COLUMN_RULE_WIDTH("webkitColumnRuleWidth", "webkit-column-rule-width", chrome("0px")),

        /** The style property {@code webkitColumnSpan}. */
        WEBKIT_COLUMN_SPAN("webkitColumnSpan", "webkit-column-span", chrome("none")),

        /** The style property {@code webkitColumnWidth}. */
        WEBKIT_COLUMN_WIDTH("webkitColumnWidth", "webkit-column-width", chrome("auto")),

        /** The style property {@code webkitColumns}. */
        WEBKIT_COLUMNS("webkitColumns", "webkit-columns", chrome("auto auto")),

        /** The style property {@code webkitFilter}. */
        WEBKIT_FILTER("webkitFilter", "webkit-filter", chrome("none")),

        /** The style property {@code webkitFontFeatureSettings}. */
        WEBKIT_FONT_FEATURE_SETTINGS("webkitFontFeatureSettings", "webkit-font-feature-settings", chrome("normal")),

        /** The style property {@code webkitFontSizeDelta}. */
        WEBKIT_FONT_SIZE_DELTA("webkitFontSizeDelta", "webkit-font-size-delta", chrome("")),

        /** The style property {@code webkitFontSmoothing}. */
        WEBKIT_FONT_SMOOTHING("webkitFontSmoothing", "webkit-font-smoothing", chrome("auto")),

        /** The style property {@code webkitHighlight}. */
        WEBKIT_HIGHLIGHT("webkitHighlight", "webkit-highlight", chrome("none")),

        /** The style property {@code webkitHyphenateCharacter}. */
        WEBKIT_HYPHENATE_CHARACTER("webkitHyphenateCharacter", "webkit-hyphenate-character", chrome("auto")),

        /** The style property {@code webkitLineBoxContain}. */
        WEBKIT_LINE_BOX_CONTAIN("webkitLineBoxContain", "webkit-line-box-contain", chrome("block inline replaced")),

        /** The style property {@code webkitLineBreak}. */
        WEBKIT_LINE_BREAK("webkitLineBreak", "webkit-line-break", chrome("auto")),

        /** The style property {@code webkitLineClamp}. */
        WEBKIT_LINE_CLAMP("webkitLineClamp", "webkit-line-clamp", chrome("none")),

        /** The style property {@code webkitLocale}. */
        WEBKIT_LOCALE("webkitLocale", "webkit-locale", chrome("auto")),

        /** The style property {@code webkitLogicalHeight}. */
        WEBKIT_LOGICAL_HEIGHT("webkitLogicalHeight", "webkit-logical-height", chrome("322px")),

        /** The style property {@code webkitLogicalWidth}. */
        WEBKIT_LOGICAL_WIDTH("webkitLogicalWidth", "webkit-logical-width", chrome("1240px")),

        /** The style property {@code webkitMarginAfter}. */
        WEBKIT_MARGIN_AFTER("webkitMarginAfter", "webkit-margin-after", chrome("0px")),

        /** The style property {@code webkitMarginAfterCollapse}. */
        WEBKIT_MARGIN_AFTER_COLLAPSE("webkitMarginAfterCollapse", "webkit-margin-after-collapse", chrome("collapse")),

        /** The style property {@code webkitMarginBefore}. */
        WEBKIT_MARGIN_BEFORE("webkitMarginBefore", "webkit-margin-before", chrome("0px")),

        /** The style property {@code webkitMarginBeforeCollapse}. */
        WEBKIT_MARGIN_BEFORE_COLLAPSE("webkitMarginBeforeCollapse", "webkit-margin-before-collapse",
                chrome("collapse")),

        /** The style property {@code webkitMarginBottomCollapse}. */
        WEBKIT_MARGIN_BOTTOM_COLLAPSE("webkitMarginBottomCollapse", "webkit-margin-bottom-collapse",
                chrome("collapse")),

        /** The style property {@code webkitMarginCollapse}. */
        WEBKIT_MARGIN_COLLAPSE("webkitMarginCollapse", "webkit-margin-collapse", chrome("")),

        /** The style property {@code webkitMarginEnd}. */
        WEBKIT_MARGIN_END("webkitMarginEnd", "webkit-margin-end", chrome("0px")),

        /** The style property {@code webkitMarginStart}. */
        WEBKIT_MARGIN_START("webkitMarginStart", "webkit-margin-start", chrome("0px")),

        /** The style property {@code webkitMarginTopCollapse}. */
        WEBKIT_MARGIN_TOP_COLLAPSE("webkitMarginTopCollapse", "webkit-margin-top-collapse", chrome("collapse")),

        /** The style property {@code webkitMask}. */
        WEBKIT_MASK("webkitMask", "webkit-mask", chrome("")),

        /** The style property {@code webkitMaskBoxImage}. */
        WEBKIT_MASK_BOX_IMAGE("webkitMaskBoxImage", "webkit-mask-box-image", chrome("none")),

        /** The style property {@code webkitMaskBoxImageOutset}. */
        WEBKIT_MASK_BOX_IMAGE_OUTSET("webkitMaskBoxImageOutset", "webkit-mask-box-image-outset", chrome("0px")),

        /** The style property {@code webkitMaskBoxImageRepeat}. */
        WEBKIT_MASK_BOX_IMAGE_REPEAT("webkitMaskBoxImageRepeat", "webkit-mask-box-image-repeat", chrome("stretch")),

        /** The style property {@code webkitMaskBoxImageSlice}. */
        WEBKIT_MASK_BOX_IMAGE_SLICE("webkitMaskBoxImageSlice", "webkit-mask-box-image-slice", chrome("0 fill")),

        /** The style property {@code webkitMaskBoxImageSource}. */
        WEBKIT_MASK_BOX_IMAGE_SOURCE("webkitMaskBoxImageSource", "webkit-mask-box-image-source", chrome("none")),

        /** The style property {@code webkitMaskBoxImageWidth}. */
        WEBKIT_MASK_BOX_IMAGE_WIDTH("webkitMaskBoxImageWidth", "webkit-mask-box-image-width", chrome("auto")),

        /** The style property {@code webkitMaskClip}. */
        WEBKIT_MASK_CLIP("webkitMaskClip", "webkit-mask-clip", chrome("border-box")),

        /** The style property {@code webkitMaskComposite}. */
        WEBKIT_MASK_COMPOSITE("webkitMaskComposite", "webkit-mask-composite", chrome("source-over")),

        /** The style property {@code webkitMaskImage}. */
        WEBKIT_MASK_IMAGE("webkitMaskImage", "webkit-mask-image", chrome("none")),

        /** The style property {@code webkitMaskOrigin}. */
        WEBKIT_MASK_ORIGIN("webkitMaskOrigin", "webkit-mask-origin", chrome("border-box")),

        /** The style property {@code webkitMaskPosition}. */
        WEBKIT_MASK_POSITION("webkitMaskPosition", "webkit-mask-position", chrome("0% 0%")),

        /** The style property {@code webkitMaskPositionX}. */
        WEBKIT_MASK_POSITION_X("webkitMaskPositionX", "webkit-mask-position-x", chrome("0%")),

        /** The style property {@code webkitMaskPositionY}. */
        WEBKIT_MASK_POSITION_Y("webkitMaskPositionY", "webkit-mask-position-y", chrome("0%")),

        /** The style property {@code webkitMaskRepeat}. */
        WEBKIT_MASK_REPEAT("webkitMaskRepeat", "webkit-mask-repeat", chrome("repeat")),

        /** The style property {@code webkitMaskRepeatX}. */
        WEBKIT_MASK_REPEAT_X("webkitMaskRepeatX", "webkit-mask-repeat-x", chrome("")),

        /** The style property {@code webkitMaskRepeatY}. */
        WEBKIT_MASK_REPEAT_Y("webkitMaskRepeatY", "webkit-mask-repeat-y", chrome("")),

        /** The style property {@code webkitMaskSize}. */
        WEBKIT_MASK_SIZE("webkitMaskSize", "webkit-mask-size", chrome("auto")),

        /** The style property {@code webkitMaxLogicalHeight}. */
        WEBKIT_MAX_LOGICAL_HEIGHT("webkitMaxLogicalHeight", "webkit-max-logical-height", chrome("none")),

        /** The style property {@code webkitMaxLogicalWidth}. */
        WEBKIT_MAX_LOGICAL_WIDTH("webkitMaxLogicalWidth", "webkit-max-logical-width", chrome("none")),

        /** The style property {@code webkitMinLogicalHeight}. */
        WEBKIT_MIN_LOGICAL_HEIGHT("webkitMinLogicalHeight", "webkit-min-logical-height", chrome("0px")),

        /** The style property {@code webkitMinLogicalWidth}. */
        WEBKIT_MIN_LOGICAL_WIDTH("webkitMinLogicalWidth", "webkit-min-logical-width", chrome("0px")),

        /** The style property {@code webkitPaddingAfter}. */
        WEBKIT_PADDING_AFTER("webkitPaddingAfter", "webkit-padding-after", chrome("0px")),

        /** The style property {@code webkitPaddingBefore}. */
        WEBKIT_PADDING_BEFORE("webkitPaddingBefore", "webkit-padding-before", chrome("0px")),

        /** The style property {@code webkitPaddingEnd}. */
        WEBKIT_PADDING_END("webkitPaddingEnd", "webkit-padding-end", chrome("0px")),

        /** The style property {@code webkitPaddingStart}. */
        WEBKIT_PADDING_START("webkitPaddingStart", "webkit-padding-start", chrome("0px")),

        /** The style property {@code webkitPerspective}. */
        WEBKIT_PERSPECTIVE("webkitPerspective", "webkit-perspective", chrome("none")),

        /** The style property {@code webkitPerspectiveOrigin}. */
        WEBKIT_PERSPECTIVE_ORIGIN("webkitPerspectiveOrigin", "webkit-perspective-origin", chrome("620px 161px")),

        /** The style property {@code webkitPerspectiveOriginX}. */
        WEBKIT_PERSPECTIVE_ORIGIN_X("webkitPerspectiveOriginX", "webkit-perspective-origin-x", chrome("")),

        /** The style property {@code webkitPerspectiveOriginY}. */
        WEBKIT_PERSPECTIVE_ORIGIN_Y("webkitPerspectiveOriginY", "webkit-perspective-origin-y", chrome("")),

        /** The style property {@code webkitPrintColorAdjust}. */
        WEBKIT_PRINT_COLOR_ADJUST("webkitPrintColorAdjust", "webkit-print-color-adjust", chrome("economy")),

        /** The style property {@code webkitRtlOrdering}. */
        WEBKIT_RTL_ORDERING("webkitRtlOrdering", "webkit-rtl-ordering", chrome("logical")),

        /** The style property {@code webkitRubyPosition}. */
        WEBKIT_RUBY_POSITION("webkitRubyPosition", "webkit-ruby-position", chrome("before")),

        /** The style property {@code webkitTapHighlightColor}. */
        WEBKIT_TAP_HIGHLIGHT_COLOR("webkitTapHighlightColor", "webkit-tap-highlight-color",
                chrome("rgba(0, 0, 0, 0.180392)")),

        /** The style property {@code webkitTextCombine}. */
        WEBKIT_TEXT_COMBINE("webkitTextCombine", "webkit-text-combine", chrome("none")),

        /** The style property {@code webkitTextDecorationsInEffect}. */
        WEBKIT_TEXT_DECORATIONS_IN_EFFECT("webkitTextDecorationsInEffect", "webkit-text-decorations-in-effect",
                chrome("none")),

        /** The style property {@code webkitTextEmphasis}. */
        WEBKIT_TEXT_EMPHASIS("webkitTextEmphasis", "webkit-text-emphasis", chrome("")),

        /** The style property {@code webkitTextEmphasisColor}. */
        WEBKIT_TEXT_EMPHASIS_COLOR("webkitTextEmphasisColor", "webkit-text-emphasis-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code webkitTextEmphasisPosition}. */
        WEBKIT_TEXT_EMPHASIS_POSITION("webkitTextEmphasisPosition", "webkit-text-emphasis-position", chrome("over")),

        /** The style property {@code webkitTextEmphasisStyle}. */
        WEBKIT_TEXT_EMPHASIS_STYLE("webkitTextEmphasisStyle", "webkit-text-emphasis-style", chrome("none")),

        /** The style property {@code webkitTextFillColor}. */
        WEBKIT_TEXT_FILL_COLOR("webkitTextFillColor", "webkit-text-fill-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code webkitTextOrientation}. */
        WEBKIT_TEXT_ORIENTATION("webkitTextOrientation", "webkit-text-orientation", chrome("vertical-right")),

        /** The style property {@code webkitTextSecurity}. */
        WEBKIT_TEXT_SECURITY("webkitTextSecurity", "webkit-text-security", chrome("none")),

        /** The style property {@code webkitTextStroke}. */
        WEBKIT_TEXT_STROKE("webkitTextStroke", "webkit-text-stroke", chrome("")),

        /** The style property {@code webkitTextStrokeColor}. */
        WEBKIT_TEXT_STROKE_COLOR("webkitTextStrokeColor", "webkit-text-stroke-color", chrome("rgb(0, 0, 0)")),

        /** The style property {@code webkitTextStrokeWidth}. */
        WEBKIT_TEXT_STROKE_WIDTH("webkitTextStrokeWidth", "webkit-text-stroke-width", chrome("0px")),

        /** The style property {@code webkitTransform}. */
        WEBKIT_TRANSFORM("webkitTransform", "webkit-transform", chrome("none")),

        /** The style property {@code webkitTransformOrigin}. */
        WEBKIT_TRANSFORM_ORIGIN("webkitTransformOrigin", "webkit-transform-origin", chrome("620px 161px")),

        /** The style property {@code webkitTransformOriginX}. */
        WEBKIT_TRANSFORM_ORIGIN_X("webkitTransformOriginX", "webkit-transform-origin-x", chrome("")),

        /** The style property {@code webkitTransformOriginY}. */
        WEBKIT_TRANSFORM_ORIGIN_Y("webkitTransformOriginY", "webkit-transform-origin-y", chrome("")),

        /** The style property {@code webkitTransformOriginZ}. */
        WEBKIT_TRANSFORM_ORIGIN_Z("webkitTransformOriginZ", "webkit-transform-origin-z", chrome("")),

        /** The style property {@code webkitTransformStyle}. */
        WEBKIT_TRANSFORM_STYLE("webkitTransformStyle", "webkit-transform-style", chrome("flat")),

        /** The style property {@code webkitTransition}. */
        WEBKIT_TRANSITION("webkitTransition", "webkit-transition", chrome("all 0s ease 0s")),

        /** The style property {@code webkitTransitionDelay}. */
        WEBKIT_TRANSITION_DELAY("webkitTransitionDelay", "webkit-transition-delay", chrome("0s")),

        /** The style property {@code webkitTransitionDuration}. */
        WEBKIT_TRANSITION_DURATION("webkitTransitionDuration", "webkit-transition-duration", chrome("0s")),

        /** The style property {@code webkitTransitionProperty}. */
        WEBKIT_TRANSITION_PROPERTY("webkitTransitionProperty", "webkit-transition-property", chrome("all")),

        /** The style property {@code webkitTransitionTimingFunction}. */
        WEBKIT_TRANSITION_TIMING_FUNCTION("webkitTransitionTimingFunction", "webkit-transition-timing-function",
                chrome("ease")),

        /** The style property {@code webkitUserDrag}. */
        WEBKIT_USER_DRAG("webkitUserDrag", "webkit-user-drag", chrome("auto")),

        /** The style property {@code webkitUserModify}. */
        WEBKIT_USER_MODIFY("webkitUserModify", "webkit-user-modify", chrome("read-only")),

        /** The style property {@code webkitUserSelect}. */
        WEBKIT_USER_SELECT("webkitUserSelect", "webkit-user-select", chrome("text")),

        /** The style property {@code webkitWritingMode}. */
        WEBKIT_WRITING_MODE("webkitWritingMode", "webkit-writing-mode", chrome("horizontal-tb")),

        /** The style property {@code whiteSpace}. */
        WHITE_SPACE("whiteSpace", "white-space", chrome("normal")),

        /** The style property {@code white-space}. */
        WHITE_SPACE_("white-space", "white-space", ff38up("normal")),

        /** The style property widows. */
        WIDOWS("widows", "widows", ie11up("2"), chrome("1")),

        /** The style property {@code width}. */
        WIDTH("width", "width", chrome("skipped")),

        /** The style property will-change. */
        WILL_CHANGE("willChange", "will-change", ff38up("auto"), chrome("auto")),

        /** The style property {@code will-change}. */
        WILL_CHANGE_("will-change", "will-change", ff38up("auto")),

        /** The style property word-break. */
        WORD_BREAK("wordBreak", "word-break", ff("normal"), ieBelow11(""), ie11up("normal"), chrome("normal")),

        /** The style property {@code word-break}. */
        WORD_BREAK_("word-break", "word-break", ff38up("normal")),

        /** The style property {@code wordSpacing}. */
        WORD_SPACING("wordSpacing", "word-spacing", chrome("0px"), ff("0px"), ie("0px")),

        /** The style property {@code word-spacing}. */
        WORD_SPACING_("word-spacing", "word-spacing", ff38up("0px")),

        /** The style property word-wrap. */
        WORD_WRAP("wordWrap", "word-wrap", ie11up(""), chrome("normal"), ff("normal")),

        /** The style property {@code word-wrap}. */
        WORD_WRAP_("word-wrap", "word-wrap", ff38up("normal")),

        /** The style property writing-mode. */
        WRITING_MODE("writingMode", "writing-mode", ie11up("undefined"), chrome("lr-tb")),

        /** The style property {@code x}. */
        X("x", "x", chrome("0px")),

        /** The style property {@code y}. */
        Y("y", "y", chrome("0px")),

        /** The style property {@code z-index}. */
        Z_INDEX_("z-index", "z-index", ff38up("auto")),

        /** The style property zoom. */
        ZOOM("zoom", "zoom", ie11up("undefined"), chrome("1"));

        private final String propertyName_;
        private final String attributeName_;
        private final BrowserConfiguration[] browserConfigurations_;

        private Definition(final String propertyName,
                final String attributeName,
                final BrowserConfiguration... browserConfigurations) {
            propertyName_ = propertyName;
            attributeName_ = attributeName;
            browserConfigurations_ = browserConfigurations;
            styles_.put(propertyName_, this);
        }

        private boolean isAvailable(final BrowserVersion browserVersion) {
            return BrowserConfiguration.isDefined(browserVersion,
                    browserConfigurations_);
        }

        /**
         * Gets the name of the JavaScript property for this attribute.
         * @return the name of the JavaScript property
         */
        public String getPropertyName() {
            return propertyName_;
        }

        /**
         * Gets the name of the style attribute.
         * @return the name of the style attribute
         */
        public String getAttributeName() {
            return attributeName_;
        }

        public String getDefaultComputedValue(
                final BrowserVersion browserVersion) {
            final BrowserConfiguration config
                = BrowserConfiguration.getMatchingConfiguration(browserVersion, browserConfigurations_);
            if (config == null) {
                return "";
            }
            return config.getDefaultValue();
        }
    }
}
