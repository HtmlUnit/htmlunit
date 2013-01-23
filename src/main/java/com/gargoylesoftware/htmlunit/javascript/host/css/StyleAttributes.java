/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ff;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ff17up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ffBelow17;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ie;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ie8up;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Contains information about the style attribute defined for different browser
 * as well as their default values.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
final class StyleAttributes {
    private static final Map<String, Definition> styles_ = new HashMap<String, Definition>();

    static {
        // force loading of Definition to fill the styles_ map
        Definition.values();
    }

    private StyleAttributes() {
        // nothing
    }

    /**
     * Gets the style attributes definition with the given name for the specified browser version.
     * @param browserVersion the browser version
     * @return <code>null</code> if no definition exists for this browser version
     */
    public static Definition getDefinition(final String propertyName, final BrowserVersion browserVersion) {
        final Definition definition = styles_.get(propertyName);
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
        final List<Definition> list = new ArrayList<Definition>();
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
        /** The style property -moz-animation. */
        MOZ_ANIMATION("MozAnimation", "-moz-animation", ff17up("")),

        /** The style property -moz-animation-delay. */
        MOZ_ANIMATION_DELAY("MozAnimationDelay", "-moz-animation-delay",
                ff17up("0s")),

        /** The style property -moz-animation-direction. */
        MOZ_ANIMATION_DIRECTION("MozAnimationDirection",
                "-moz-animation-direction", ff17up("normal")),

        /** The style property -moz-animation-duration. */
        MOZ_ANIMATION_DURATION("MozAnimationDuration",
                "-moz-animation-duration", ff17up("0s")),

        /** The style property -moz-animation-fill-mode. */
        MOZ_ANIMATION_FILL_MODE("MozAnimationFillMode",
                "moz-annimation-fill-mode", ff17up("none")),

        /** The style property -moz-animation-iteration-count. */
        MOZ_ANIMATION_ITERATION_COUNT("MozAnimationIterationCount",
                "-moz-animation-iteration-count", ff17up("1")),

        /** The style property -moz-animation-name. */
        MOZ_ANIMATION_NAME("MozAnimationName", "moz-annimation-name",
                ff17up("none")),

        /** The style property -moz-animation-play-state. */
        MOZ_ANIMATION_PLAY_STATE("MozAnimationPlayState",
                "moz-annimation-play-state", ff17up("running")),

        /** The style property -moz-animation-timing-function. */
        MOZ_ANIMATION_TIMING_FUNCTION("MozAnimationTimingFunction",
                "moz-annimation-timing-function",
                ff17up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property -moz-appearance. */
        MOZ_APPEARANCE("MozAppearance", "-moz-appearance", ff("none")),

        /** The style property -moz-backface-visibility. */
        MOZ_BACKFACE_VISIBILITY("MozBackfaceVisibility",
                "-moz-backface-visibility", ff17up("visible")),

        /** The style property -moz-background-clip. */
        MOZ_BACKGROUND_CLIP("MozBackgroundClip", "-moz-background-clip",
                ffBelow17("border")),

        /** The style property -moz-background-inline-policy. */
        MOZ_BACKGROUND_INLINE_POLICY("MozBackgroundInlinePolicy",
                "-moz-background-inline-policy", ff("continuous")),

        /** The style property -moz-background-origin. */
        MOZ_BACKGROUND_ORIGIN("MozBackgroundOrigin", "-moz-background-origin",
                ffBelow17("padding")),

        /** The style property -moz-background-size. */
        MOZ_BACKGROUND_SIZE("MozBackgroundSize", "-moz-background-size",
                ffBelow17("auto auto")),

        /** The style property -moz-binding. */
        MOZ_BINDING("MozBinding", "-moz-binding", ff("none")),

        /** The style property -moz-border-bottom-colors. */
        MOZ_BORDER_BOTTOM_COLORS("MozBorderBottomColors",
                "-moz-border-bottom-colors", ff("none")),

        /** The style property -moz-border-end. */
        MOZ_BORDER_END("MozBorderEnd", "-moz-border-end", ff("")),

        /** The style property -moz-border-end-color. */
        MOZ_BORDER_END_COLOR("MozBorderEndColor", "-moz-border-end-color",
                ff("")),

        /** The style property -moz-border-end-style. */
        MOZ_BORDER_END_STYLE("MozBorderEndStyle", "-moz-border-end-style",
                ff("")),

        /** The style property -moz-border-end-width. */
        MOZ_BORDER_END_WIDTH("MozBorderEndWidth", "-moz-border-end-width",
                ff("")),

        /** The style property -moz-border-image. */
        MOZ_BORDER_IMAGE("MozBorderImage", "-moz-border-image",
                ffBelow17("none"), ff("")),

        /** The style property -moz-border-left-colors. */
        MOZ_BORDER_LEFT_COLORS("MozBorderLeftColors",
                "-moz-border-left-colors", ff("none")),

        /** The style property -moz-border-radius. */
        MOZ_BORDER_RADIUS("MozBorderRadius", "-moz-border-radius",
                ffBelow17("")),

        /** The style property -moz-border-radius-bottomleft. */
        MOZ_BORDER_RADIUS_BOTTOMLEFT("MozBorderRadiusBottomleft",
                "-moz-border-radius-bottomleft", ffBelow17("0px")),

        /** The style property -moz-border-radius-bottomright. */
        MOZ_BORDER_RADIUS_BOTTOMRIGHT("MozBorderRadiusBottomright",
                "-moz-border-radius-bottomright", ffBelow17("0px")),

        /** The style property -moz-border-radius-topleft. */
        MOZ_BORDER_RADIUS_TOPLEFT("MozBorderRadiusTopleft",
                "-moz-border-radius-topleft", ffBelow17("0px")),

        /** The style property -moz-border-radius-topright. */
        MOZ_BORDER_RADIUS_TOPRIGHT("MozBorderRadiusTopright",
                "-moz-border-radius-topright", ffBelow17("0px")),

        /** The style property -moz-border-right-colors. */
        MOZ_BORDER_RIGHT_COLORS("MozBorderRightColors",
                "-moz-border-right-colors", ff("none")),

        /** The style property -moz-border-start. */
        MOZ_BORDER_START("MozBorderStart", "-moz-border-start", ff("")),

        /** The style property -moz-border-start-color. */
        MOZ_BORDER_START_COLOR("MozBorderStartColor",
                "-moz-border-start-color", ff("")),

        /** The style property -moz-border-start-style. */
        MOZ_BORDER_START_STYLE("MozBorderStartStyle",
                "-moz-border-start-style", ff("")),

        /** The style property -moz-border-start-width. */
        MOZ_BORDER_START_WIDTH("MozBorderStartWidth",
                "-moz-border-start-width", ff("")),

        /** The style property -moz-border-top-colors. */
        MOZ_BORDER_TOP_COLORS("MozBorderTopColors", "-moz-border-top-colors",
                ff("none")),

        /** The style property -moz-box-align. */
        MOZ_BOX_ALIGN("MozBoxAlign", "-moz-box-align", ff("stretch")),

        /** The style property -moz-box-direction. */
        MOZ_BOX_DIRECTION("MozBoxDirection", "-moz-box-direction", ff("normal")),

        /** The style property -moz-box-flex. */
        MOZ_BOX_FLEX("MozBoxFlex", "-moz-box-flex", ff("0")),

        /** The style property -moz-box-ordinal-group. */
        MOZ_BOX_ORDINAL_GROUP("MozBoxOrdinalGroup", "-moz-box-ordinal-group",
                ff("1")),

        /** The style property -moz-box-orient. */
        MOZ_BOX_ORIENT("MozBoxOrient", "-moz-box-orient", ff("horizontal")),

        /** The style property -moz-box-pack. */
        MOZ_BOX_PACK("MozBoxPack", "-moz-box-pack", ff("start")),

        /** The style property -moz-box-shadow. */
        MOZ_BOX_SHADOW("MozBoxShadow", "-moz-box-shadow", ffBelow17("none")),

        /** The style property -moz-box-sizing. */
        MOZ_BOX_SIZING("MozBoxSizing", "-moz-box-sizing", ff("content-box")),

        /** The style property -moz-column-count. */
        MOZ_COLUMN_COUNT("MozColumnCount", "-moz-column-count", ff("auto")),

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
        MOZ_COLUMNS("MozColumns", "-moz-columns", ff17up("")),

        /** The style property -moz-float-edge. */
        MOZ_FLOAT_EDGE("MozFloatEdge", "-moz-float-edge", ff("content-box")),

        /** The style property -moz-font-feature-settings. */
        MOZ_FONT_FEATURE_SETTINGS("MozFontFeatureSettings",
                "-moz-font-feature-settings", ff17up("normal")),

        /** The style property -moz-font-language-override. */
        MOZ_FONT_LANGUAGE_OVERRIDE("MozFontLanguageOverride",
                "-moz-font-language-override", ff17up("normal")),

        /** The style property -moz-force-broken-image-icon. */
        MOZ_FORCE_BROKEN_IMAGE_ICON("MozForceBrokenImageIcon",
                "-moz-force-broken-image-icon", ff("0")),

        /** The style property -moz-hyphens. */
        MOZ_HYPHENS("MozHyphens", "-moz-hyphens", ff17up("manual")),

        /** The style property -moz-image-region. */
        MOZ_IMAGE_REGION("MozImageRegion", "-moz-image-region", ff("auto")),

        /** The style property -moz-margin-end. */
        MOZ_MARGIN_END("MozMarginEnd", "-moz-margin-end", ff("")),

        /** The style property -moz-margin-start. */
        MOZ_MARGIN_START("MozMarginStart", "-moz-margin-start", ff("")),

        /** The style property -moz-opacity. */
        MOZ_OPACITY("MozOpacity", "-moz-opacity", ffBelow17("1")),

        /** The style property -moz-orient. */
        MOZ_ORIENT("MozOrient", "-moz-orient", ff17up("horizontal")),

        /** The style property -moz-outline. */
        MOZ_OUTLINE("MozOutline", "-moz-outline", ffBelow17("")),

        /** The style property -moz-outline-color. */
        MOZ_OUTLINE_COLOR("MozOutlineColor", "-moz-outline-color",
                ffBelow17("rgb(0, 0, 0)")),

        /** The style property -moz-outline-offset. */
        MOZ_OUTLINE_OFFSET("MozOutlineOffset", "-moz-outline-offset",
                ffBelow17("0px")),

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
        MOZ_OUTLINE_STYLE("MozOutlineStyle", "-moz-outline-style",
                ffBelow17("none")),

        /** The style property -moz-outline-width. */
        MOZ_OUTLINE_WIDTH("MozOutlineWidth", "-moz-outline-width",
                ffBelow17("0px")),

        /** The style property -moz-padding-end. */
        MOZ_PADDING_END("MozPaddingEnd", "-moz-padding-end", ff("")),

        /** The style property -moz-padding-start. */
        MOZ_PADDING_START("MozPaddingStart", "-moz-padding-start", ff("")),

        /** The style property -moz-perspective. */
        MOZ_PERSPECTIVE("MozPerspective", "-moz-perspective", ff17up("none")),

        /** The style property -moz-perspective-origin. */
        MOZ_PERSPECTIVE_ORIGIN("MozPerspectiveOrigin",
                "-moz-perspective-origin", ff17up("628px 170px")),

        /** The style property -moz-stack-sizing. */
        MOZ_STACK_SIZING("MozStackSizing", "-moz-stack-sizing",
                ff("stretch-to-fit")),

        /** The style property -moz-tab-size. */
        MOZ_TAB_SIZE("MozTabSize", "-moz-tab-size", ff17up("8")),

        /** The style property -moz-text-align-last. */
        MOZ_TEXT_ALIGN_LAST("MozTextAlignLast", "-moz-text-align-last",
                ff17up("auto")),

        /** The style property -moz-text-blink. */
        MOZ_TEXT_BLINK("MozTextBlink", "-moz-text-blink", ff17up("none")),

        /** The style property -moz-text-decoration-color. */
        MOZ_TEXT_DECORATION_COLOR("MozTextDecorationColor",
                "-moz-text-decoration-color", ff17up("rgb(0, 0, 0)")),

        /** The style property -moz-text-decoration-line. */
        MOZ_TEXT_DECORATION_LINE("MozTextDecorationLine",
                "-moz-text-decoration-line", ff17up("none")),

        /** The style property -moz-text-decoration-style. */
        MOZ_TEXT_DECORATION_STYLE("MozTextDecorationStyle",
                "-moz-text-decoration-style", ff17up("solid")),

        /** The style property -moz-text-size-adjust. */
        MOZ_TEXT_SIZE_ADJUST("MozTextSizeAdjust", "-moz-text-size-adjust",
                ff17up("auto")),

        /** The style property -moz-transform. */
        MOZ_TRANSFORM("MozTransform", "-moz-transform", ff("none")),

        /** The style property -moz-transform-origin. */
        MOZ_TRANSFORM_ORIGIN("MozTransformOrigin", "-moz-transform-origin",
                ffBelow17("50% 50%"), ff("628px 170px")),

        /** The style property -moz-transform-style. */
        MOZ_TRANSFORM_STYLE("MozTransformStyle", "-moz-transform-style",
                ff17up("flat")),

        /** The style property -moz-transition. */
        MOZ_TRANSITION("MozTransition", "-moz-transition", ff17up("")),

        /** The style property -moz-transition-delay. */
        MOZ_TRANSITION_DELAY("MozTransitionDelay", "-moz-transition-delay",
                ff17up("0s")),

        /** The style property -moz-transition-duration. */
        MOZ_TRANSITION_DURATION("MozTransitionDuration",
                "-moz-transition-duration", ff17up("0s")),

        /** The style property -moz-transition-property. */
        MOZ_TRANSITION_PROPERTY("MozTransitionProperty",
                "-moz-transition-property", ff17up("all")),

        /** The style property -moz-transition-timing-function. */
        MOZ_TRANSITION_TIMING_FUNCTION("MozTransitionTimingFunction",
                "-moz-transition-timing-function",
                ff17up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property -moz-user-focus. */
        MOZ_USER_FOCUS("MozUserFocus", "-moz-user-focus", ff("none")),

        /** The style property -moz-user-input. */
        MOZ_USER_INPUT("MozUserInput", "-moz-user-input", ff("auto")),

        /** The style property -moz-user-modify. */
        MOZ_USER_MODIFY("MozUserModify", "-moz-user-modify", ff("read-only")),

        /** The style property -moz-user-select. */
        MOZ_USER_SELECT("MozUserSelect", "-moz-user-select", ff("auto")),

        /** The style property -moz-window-shadow. */
        MOZ_WINDOW_SHADOW("MozWindowShadow", "-moz-window-shadow",
                ff("default")),

        /** The style property animation. */
        ANIMATION("animation", "animation", ff17up("")),

        /** The style property animation-delay. */
        ANIMATION_DELAY("animationDelay", "animation-delay", ff17up("0s")),

        /** The style property animation-direction. */
        ANIMATION_DIRECTION("animationDirection", "animation-direction",
                ff17up("normal")),

        /** The style property animation-duration. */
        ANIMATION_DURATION("animationDuration", "animation-duration",
                ff17up("0s")),

        /** The style property animation-fill-mode. */
        ANIMATION_FILL_MODE("animationFillMode", "animation-fill-mode",
                ff17up("none")),

        /** The style property animation-iteration-count. */
        ANIMATION_ITERATION_COUNT("animationIterationCount",
                "animation-iteration-count", ff17up("1")),

        /** The style property animation-name. */
        ANIMATION_NAME("animationName", "animation-name", ff17up("none")),

        /** The style property animation-play-state. */
        ANIMATION_PLAY_STATE("animationPlayState", "animation-play-state",
                ff17up("running")),

        /** The style property animation-timing-function. */
        ANIMATION_TIMING_FUNCTION("animationTimingFunction",
                "animation-timing-function",
                ff17up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property azimuth. */
        AZIMUTH("azimuth", "azimuth", ffBelow17("")),

        /** The style property backface-visibility. */
        BACKFACE_VISIBILITY("backfaceVisibility", "backface-visibility",
                ff17up("visible")),

        /** The style property background-clip. */
        BACKGROUND_CLIP("backgroundClip", "background-clip",
                ff17up("border-box")),

        /** The style property background-origin. */
        BACKGROUND_ORIGIN("backgroundOrigin", "background-origin",
                ff17up("padding-box")),

        /** The style property background-size. */
        BACKGROUND_SIZE("backgroundSize", "background-size",
                ff17up("auto auto")),

        /** The style property border-bottom-left-radius. */
        BORDER_BOTTOM_LEFT_RADIUS("borderBottomLeftRadius",
                "border-bottom-left-radius", ff17up("0px")),

        /** The style property border-bottom-right-radius. */
        BORDER_BOTTOM_RIGHT_RADIUS("borderBottomRightRadius",
                "border-bottom-right-radius", ff17up("0px")),

        /** The style property border-image. */
        BORDER_IMAGE("borderImage", "border-image", ff17up("")),

        /** The style property border-image-outset. */
        BORDER_IMAGE_OUTSET("borderImageOutset", "border-image-outset",
                ff17up("0 0 0 0")),

        /** The style property border-image-repeat. */
        BORDER_IMAGE_REPEAT("borderImageRepeat", "border-image-repeat",
                ff17up("stretch stretch")),

        /** The style property border-image-slice. */
        BORDER_IMAGE_SLICE("borderImageSlice", "border-image-slice",
                ff17up("100% 100% 100% 100%")),

        /** The style property border-image-source. */
        BORDER_IMAGE_SOURCE("borderImageSource", "border-image-source",
                ff17up("none")),

        /** The style property border-image-width. */
        BORDER_IMAGE_WIDTH("borderImageWidth", "border-image-width",
                ff17up("1 1 1 1")),

        /** The style property border-radius. */
        BORDER_RADIUS("borderRadius", "border-radius", ff17up("")),

        /** The style property border-top-left-radius. */
        BORDER_TOP_LEFT_RADIUS("borderTopLeftRadius", "border-top-left-radius",
                ff17up("0px")),

        /** The style property border-top-right-radius. */
        BORDER_TOP_RIGHT_RADIUS("borderTopRightRadius",
                "border-top-right-radius", ff17up("0px")),

        /** The style property box-shadow. */
        BOX_SHADOW("boxShadow", "box-shadow", ff17up("none")),

        /** The style property clip-path. */
        CLIP_PATH("clipPath", "clip-path", ff17up("none")),

        /** The style property clip-rule. */
        CLIP_RULE("clipRule", "clip-rule", ff17up("nonzero")),

        /** The style property color-interpolation. */
        COLOR_INTERPOLATION("colorInterpolation", "color-interpolation",
                ff17up("srgb")),

        /** The style property color-interpolation-filters. */
        COLOR_INTERPOLATION_FILTERS("colorInterpolationFilters",
                "color-interpolation-filters", ff17up("linearrgb")),

        /** The style property cue. */
        CUE("cue", "cue", ffBelow17("")),

        /** The style property cue-after. */
        CUE_AFTER("cueAfter", "cue-after", ffBelow17("")),

        /** The style property cue-before. */
        CUE_BEFORE("cueBefore", "cue-before", ffBelow17("")),

        /** The style property dominant-baseline. */
        DOMINANT_BASELINE("dominantBaseline", "dominant-baseline",
                ff17up("auto")),

        /** The style property elevation. */
        ELEVATION("elevation", "elevation", ffBelow17("")),

        /** The style property fill. */
        FILL("fill", "fill", ff17up("rgb(0, 0, 0)")),

        /** The style property fill-opacity. */
        FILL_OPACITY("fillOpacity", "fill-opacity", ff17up("1")),

        /** The style property fill-rule. */
        FILL_RULE("fillRule", "fill-rule", ff17up("nonzero")),

        /** The style property filter. */
        FILTER("filter", "filter", ff17up("none"), ie("")),

        /** The style property flood-color. */
        FLOOD_COLOR("floodColor", "flood-color", ff17up("rgb(0, 0, 0)")),

        /** The style property flood-opacity. */
        FLOOD_OPACITY("floodOpacity", "flood-opacity", ff17up("1")),

        /** The style property image-rendering. */
        IMAGE_RENDERING("imageRendering", "image-rendering", ff17up("auto")),

        /** The style property lighting-color. */
        LIGHTING_COLOR("lightingColor", "lighting-color",
                ff17up("rgb(255, 255, 255)")),

        /** The style property line-height. */
        LINE_HEIGHT("lineHeight", "line-height", ff17up("19px"),
                ffBelow17("20px"), ie("20px")),

        /** The style property marker. */
        MARKER("marker", "marker", ff17up("")),

        /** The style property marker-end. */
        MARKER_END("markerEnd", "marker-end", ff17up("none")),

        /** The style property marker-mid. */
        MARKER_MID("markerMid", "marker-mid", ff17up("none")),

        /** The style property marker-start. */
        MARKER_START("markerStart", "marker-start", ff17up("none")),

        /** The style property mask. */
        MASK("mask", "mask", ff17up("none")),

        /** The style property pause. */
        PAUSE("pause", "pause", ffBelow17("")),

        /** The style property pause-after. */
        PAUSE_AFTER("pauseAfter", "pause-after", ffBelow17("")),

        /** The style property pause-before. */
        PAUSE_BEFORE("pauseBefore", "pause-before", ffBelow17("")),

        /** The style property pitch. */
        PITCH("pitch", "pitch", ffBelow17("")),

        /** The style property pitch-range. */
        PITCH_RANGE("pitchRange", "pitch-range", ffBelow17("")),

        /** The style property perspective. */
        PERSPECTIVE("perspective", "perspective", ff17up("none")),

        /** The style property perspective-origin. */
        PERSPECTIVE_ORIGIN("perspectiveOrigin", "perspective-origin",
                ff17up("628px 170px")),

        /** The style property quotes. */
        QUOTES("quotes", "quotes", ffBelow17(""),
                ff17up("\"“\" \"”\" \"‘\" \"’\""), ie8up("")),

        /** The style property resize. */
        RESIZE("resize", "resize", ff17up("none")),

        /** The style property richness. */
        RICHNESS("richness", "richness", ffBelow17("")),

        /** The style property shape-rendering. */
        SHAPE_RENDERING("shapeRendering", "shape-rendering", ff17up("auto")),

        /** The style property speak. */
        SPEAK("speak", "speak", ffBelow17("")),

        /** The style property speak. */
        SPEAK_HEADER("speakHeader", "speak-header", ffBelow17("")),

        /** The style property speak-numeral. */
        SPEAK_NUMERAL("speakNumeral", "speak-numeral", ffBelow17("")),

        /** The style property speak-punktuation. */
        SPEAK_PUNCTUATION("speakPunctuation", "speak-punctuation",
                ffBelow17("")),

        /** The style property speech-rate. */
        SPEECH_RATE("speechRate", "speech-rate", ffBelow17("")),

        /** The style property stop-color. */
        STOP_COLOR("stopColor", "stop-color", ff17up("rgb(0, 0, 0)")),

        /** The style property stop-opacity. */
        STOP_OPACITY("stopOpacity", "stop-opacity", ff17up("1")),

        /** The style property stroke. */
        STROKE("stroke", "stroke", ff17up("none")),

        /** The style property stroke-dasharray. */
        STROKE_DASHARRAY("strokeDasharray", "stroke-dasharray", ff17up("none")),

        /** The style property stroke-dashoffset. */
        STROKE_DASHOFFSET("strokeDashoffset", "stroke-dashoffset",
                ff17up("0px")),

        /** The style property stroke-linecap. */
        STROKE_LINECAP("strokeLinecap", "stroke-linecap", ff17up("butt")),

        /** The style property stroke-linejoin. */
        STROKE_LINEJOIN("strokeLinejoin", "stroke-linejoin", ff17up("miter")),

        /** The style property stroke-miterlimit. */
        STROKE_MITERLIMIT("strokeMiterlimit", "stroke-miterlimit", ff17up("4")),

        /** The style property stroke-opacity. */
        STROKE_OPACITY("strokeOpacity", "stroke-opacity", ff17up("1")),

        /** The style property stroke-width. */
        STROKE_WIDTH("strokeWidth", "stroke-width", ff17up("1px")),

        /** The style property stress. */
        STRESS("stress", "stress", ffBelow17("")),

        /** The style property text-anchor. */
        TEXT_ANCHOR("textAnchor", "text-anchor", ff17up("start")),

        /** The style property text-overflow. */
        TEXT_OVERFLOW("textOverflow", "text-overflow", ff17up("clip")),

        /** The style property text-rendering. */
        TEXT_RENDERING("textRendering", "text-rendering", ff17up("auto")),

        /** The style property transform. */
        TRANSFORM("transform", "transform", ff17up("none")),

        /** The style property transform-origin. */
        TRANSFORM_ORIGIN("transformOrigin", "transform-origin",
                ff17up("628px 170px")),

        /** The style property transform-style. */
        TRANSFORM_STYLE("transformStyle", "transform-style", ff17up("flat")),

        /** The style property transition. */
        TRANSITION("transition", "transition", ff17up("")),

        /** The style property transition-delay. */
        TRANSITION_DELAY("transitionDelay", "transition-delay", ff17up("0s")),

        /** The style property transition-duration. */
        TRANSITION_DURATION("transitionDuration", "transition-duration",
                ff17up("0s")),

        /** The style property transition-property. */
        TRANSITION_PROPERTY("transitionProperty", "transition-property",
                ff17up("all")),

        /** The style property transition-timing-function. */
        TRANSITION_TIMING_FUNCTION("transitionTimingFunction",
                "transition-timing-function",
                ff17up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property unicode-bidi. */
        UNICODE_BIDI("unicodeBidi", "unicode-bidi", ffBelow17("embed"),
                ff17up("-moz-isolate"), ie("embed")),

        /** The style property vector-effect. */
        VECTOR_EFFECT("vectorEffect", "vector-effect", ff17up("none")),

        /** The style property "voice-family". */
        VOICE_FAMILY("voiceFamily", "voice-family", ffBelow17("")),

        /** The style property volume. */
        VOLUME("volume", "volume", ffBelow17("")),

        /** The style property word-break. */
        WORD_BREAK("wordBreak", "word-break", ff17up("normal"), ie(""));

        private final String propertyName_;
        private final String attributeName_;
        private final BrowserConfiguration[] browserConfigurations_;

        private Definition(final String propertyName,
                final String styleAttributeName,
                final BrowserConfiguration... browserConfigurations) {
            attributeName_ = styleAttributeName;
            propertyName_ = propertyName;
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
            return BrowserConfiguration.getMatchingConfiguration(
                    browserVersion, browserConfigurations_).getDefaultValue();
        }
    }
}
