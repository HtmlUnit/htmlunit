/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ff24up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ffBelow31;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ff31up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ie11up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ie8up;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ieBelow11;

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
 * @author Frank Danek
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
        MOZ_ANIMATION("MozAnimation", "-moz-animation", ff("")),

        /** The style property -moz-animation-delay. */
        MOZ_ANIMATION_DELAY("MozAnimationDelay", "-moz-animation-delay",
                ff("0s")),

        /** The style property -moz-animation-direction. */
        MOZ_ANIMATION_DIRECTION("MozAnimationDirection",
                "-moz-animation-direction", ff("normal")),

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
                "-moz-background-inline-policy", ff("continuous")),

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
        MOZ_BORDER_END_COLOR("MozBorderEndColor", "-moz-border-end-color",
                ff("")),

        /** The style property -moz-border-end-style. */
        MOZ_BORDER_END_STYLE("MozBorderEndStyle", "-moz-border-end-style",
                ff("")),

        /** The style property -moz-border-end-width. */
        MOZ_BORDER_END_WIDTH("MozBorderEndWidth", "-moz-border-end-width",
                ff("")),

        /** The style property -moz-border-image. */
        MOZ_BORDER_IMAGE("MozBorderImage", "-moz-border-image", ff("")),

        /** The style property -moz-border-left-colors. */
        MOZ_BORDER_LEFT_COLORS("MozBorderLeftColors",
                "-moz-border-left-colors", ff("none")),

        /** The style property -moz-border-radius. */
        MOZ_BORDER_RADIUS("MozBorderRadius", "-moz-border-radius"),

        /** The style property -moz-border-radius-bottomleft. */
        MOZ_BORDER_RADIUS_BOTTOMLEFT("MozBorderRadiusBottomleft",
                "-moz-border-radius-bottomleft"),

        /** The style property -moz-border-radius-bottomright. */
        MOZ_BORDER_RADIUS_BOTTOMRIGHT("MozBorderRadiusBottomright",
                "-moz-border-radius-bottomright"),

        /** The style property -moz-border-radius-topleft. */
        MOZ_BORDER_RADIUS_TOPLEFT("MozBorderRadiusTopleft",
                "-moz-border-radius-topleft"),

        /** The style property -moz-border-radius-topright. */
        MOZ_BORDER_RADIUS_TOPRIGHT("MozBorderRadiusTopright",
                "-moz-border-radius-topright"),

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
        MOZ_BOX_SHADOW("MozBoxShadow", "-moz-box-shadow"),

        /** The style property -moz-box-sizing. */
        MOZ_BOX_SIZING("MozBoxSizing", "-moz-box-sizing", ff("content-box")),

        /** The style property -moz-column-count. */
        MOZ_COLUMN_COUNT("MozColumnCount", "-moz-column-count", ff("auto")),

        /** The style property -moz-column-fill. */
        MOZ_COLUMN_FILL("MozColumnFill", "-moz-column-fill", ff24up("balance")),

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
        MOZ_ORIENT("MozOrient", "-moz-orient", ff24up("auto"), ff("horizontal")),

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
                "-moz-perspective-origin", ff("624px 171px")),

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
        MOZ_TRANSFORM_ORIGIN("MozTransformOrigin", "-moz-transform-origin", ff("624px 171px")),

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

        /** The style property -moz-window-shadow. */
        MOZ_WINDOW_SHADOW("MozWindowShadow", "-moz-window-shadow",
                ff("default")),

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
        MS_CONTENT_ZOOM_SNAP_TYPE("msContentZoomSnapType",
                "-ms-content-zoom-snap-type", ie11up("none")),

        /** The style property -ms-content-zooming. */
        MS_CONTENT_ZOOMING("msContentZooming",
                "-ms-content-zooming", ie11up("none")),

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
        MS_FONT_FEATURE_SETTINGS("msFontFeatureSettings",
                "-ms-font-feature-settings", ie11up("normal")),

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
        MS_PERSPECTIVE_ORIGIN("msPerspectiveOrigin",
                "-ms-perspective-origin", ie11up("620px 163.2px")),

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
        MS_TRANSFORM_ORIGIN("msTransformOrigin", "-ms-transform-origin",
                ie11up("620px 163.2px")),

        /** The style property -ms-transform-style. */
        MS_TRANSFORM_STYLE("msTransformStyle", "-ms-transform-style",
                ie11up("flat")),

        /** The style property -ms-transition. */
        MS_TRANSITION("msTransition", "-ms-transition", ie11up("")),

        /** The style property -ms-transition-delay. */
        MS_TRANSITION_DELAY("msTransitionDelay", "-ms-transition-delay",
                ie11up("0s")),

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
        /** The style property alignment-content. */
        ALIGN_CONTENT("alignContent", "align-content", ie11up("stretch"), ff31up("stretch")),

        /** The style property alignment-items. */
        ALIGN_ITEMS("alignItems", "align-items", ff24up("stretch"), ie11up("stretch")),

        /** The style property alignment-self. */
        ALIGN_SELF("alignSelf", "align-self", ff24up("stretch"), ie11up("auto")),

        /** The style property alignment-baseline. */
        ALIGNMENT_BASELINE("alignmentBaseline", "alignment-baseline", ie11up("auto")),

        /** The style property all. */
        ALL("all", "all", ff31up("")),

        /** The style property animation. */
        ANIMATION("animation", "animation", ff(""), ie11up("")),

        /** The style property animation-delay. */
        ANIMATION_DELAY("animationDelay", "animation-delay", ff("0s"), ie11up("0s")),

        /** The style property animation-direction. */
        ANIMATION_DIRECTION("animationDirection", "animation-direction",
                ff("normal"), ie11up("normal")),

        /** The style property animation-duration. */
        ANIMATION_DURATION("animationDuration", "animation-duration",
                ff("0s"), ie11up("0s")),

        /** The style property animation-fill-mode. */
        ANIMATION_FILL_MODE("animationFillMode", "animation-fill-mode",
                ff("none"), ie11up("none")),

        /** The style property animation-iteration-count. */
        ANIMATION_ITERATION_COUNT("animationIterationCount",
                "animation-iteration-count", ff("1"), ie11up("1")),

        /** The style property animation-name. */
        ANIMATION_NAME("animationName", "animation-name", ff("none"), ie11up("none")),

        /** The style property animation-play-state. */
        ANIMATION_PLAY_STATE("animationPlayState", "animation-play-state",
                ff("running"), ie11up("running")),

        /** The style property animation-timing-function. */
        ANIMATION_TIMING_FUNCTION("animationTimingFunction",
                "animation-timing-function",
                ff("cubic-bezier(0.25, 0.1, 0.25, 1)"),
                ie11up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property azimuth. */
        AZIMUTH("azimuth", "azimuth"),

        /** The style property backface-visibility. */
        BACKFACE_VISIBILITY("backfaceVisibility", "backface-visibility",
                ff("visible"), ie11up("visible")),

        /** The style property background-blend-mode. */
        BACKGROUND_BLEND_MODE("backgroundBlendMode", "background-blend-mode",
                ff31up("normal")),

        /** The style property background-clip. */
        BACKGROUND_CLIP("backgroundClip", "background-clip",
                ff("border-box"), ie11up("border-box")),

        /** The style property background-origin. */
        BACKGROUND_ORIGIN("backgroundOrigin", "background-origin",
                ff("padding-box"), ie11up("padding-box")),

        /** The style property background-position-x. */
        BACKGROUND_POSITION_X("backgroundPositionX", "background-position-x",
                ie11up("undefined")),

        /** The style property background-position-y. */
        BACKGROUND_POSITION_Y("backgroundPositionY", "background-position-y",
                ie11up("undefined")),

        /** The style property background-size. */
        BACKGROUND_SIZE("backgroundSize", "background-size",
                ff("auto auto"), ie11up("auto")),

        /** The style property baseline-shift. */
        BASELINE_SHIFT("baselineShift", "baseline-shift", ie11up("baseline")),

        /** The style property behavior. */
        BEHAVIOR("behavior", "behavior"),

        /** The style property border-bottom-left-radius. */
        BORDER_BOTTOM_LEFT_RADIUS("borderBottomLeftRadius",
                "border-bottom-left-radius", ff("0px"), ie11up("0px")),

        /** The style property border-bottom-right-radius. */
        BORDER_BOTTOM_RIGHT_RADIUS("borderBottomRightRadius",
                "border-bottom-right-radius", ff("0px"), ie11up("0px")),

        /** The style property border-image. */
        BORDER_IMAGE("borderImage", "border-image", ff(""), ie11up("")),

        /** The style property border-image-outset. */
        BORDER_IMAGE_OUTSET("borderImageOutset", "border-image-outset",
                ff("0 0 0 0"), ie11up("0")),

        /** The style property border-image-repeat. */
        BORDER_IMAGE_REPEAT("borderImageRepeat", "border-image-repeat",
                ff("stretch stretch"), ie11up("stretch")),

        /** The style property border-image-slice. */
        BORDER_IMAGE_SLICE("borderImageSlice", "border-image-slice",
                ff("100% 100% 100% 100%"), ie11up("100%")),

        /** The style property border-image-source. */
        BORDER_IMAGE_SOURCE("borderImageSource", "border-image-source",
                ff("none"), ie11up("none")),

        /** The style property border-image-width. */
        BORDER_IMAGE_WIDTH("borderImageWidth", "border-image-width",
                ff("1 1 1 1"), ie11up("1")),

        /** The style property border-radius. */
        BORDER_RADIUS("borderRadius", "border-radius", ff(""), ie11up("")),

        /** The style property border-top-left-radius. */
        BORDER_TOP_LEFT_RADIUS("borderTopLeftRadius", "border-top-left-radius",
                ff("0px"), ie11up("0px")),

        /** The style property border-top-right-radius. */
        BORDER_TOP_RIGHT_RADIUS("borderTopRightRadius",
                "border-top-right-radius", ff("0px"), ie11up("0px")),

        /** The style property box-shadow. */
        BOX_SHADOW("boxShadow", "box-shadow", ff("none"), ie11up("none")),

        /** The style property box-sizing. */
        BOX_SIZING("boxSizing", "box-sizing", ff31up("content-box"), ie11up("content-box")),

        /** The style property break-after. */
        BREAK_AFTER("breakAfter", "break-after", ie11up("auto")),

        /** The style property break-before. */
        BREAK_BEFORE("breakBefore", "break-before", ie11up("auto")),

        /** The style property break-inside. */
        BREAK_INSIDE("breakInside", "break-inside", ie11up("auto")),

        /** The style property clip-path. */
        CLIP_PATH("clipPath", "clip-path", ff("none"), ie11up("none")),

        /** The style property clip-rule. */
        CLIP_RULE("clipRule", "clip-rule", ff("nonzero"), ie11up("nonzero")),

        /** The style property color-interpolation. */
        COLOR_INTERPOLATION("colorInterpolation", "color-interpolation",
                ff("srgb")),

        /** The style property color-interpolation-filters. */
        COLOR_INTERPOLATION_FILTERS("colorInterpolationFilters",
                "color-interpolation-filters", ff("linearrgb"), ie11up("")),

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
        CONTENT("content", "content", ie11up("normal")),

        /** The style property cue. */
        CUE("cue", "cue"),

        /** The style property cue-after. */
        CUE_AFTER("cueAfter", "cue-after"),

        /** The style property cue-before. */
        CUE_BEFORE("cueBefore", "cue-before"),

        /** The style property dominant-baseline. */
        DOMINANT_BASELINE("dominantBaseline", "dominant-baseline",
                ff("auto"), ie11up("auto")),

        /** The style property empty-cells. */
        EMPTY_CELLS("emptyCells", "empty-cells", ie11up("show")),

        /** The style property enable-background. */
        ENABLE_BACKGROUND("enableBackground", "enable-background", ie11up("accumulate")),

        /** The style property elevation. */
        ELEVATION("elevation", "elevation"),

        /** The style property fill. */
        FILL("fill", "fill", ff("rgb(0, 0, 0)"), ie11up("black")),

        /** The style property fill-opacity. */
        FILL_OPACITY("fillOpacity", "fill-opacity", ff("1"), ie11up("1")),

        /** The style property fill-rule. */
        FILL_RULE("fillRule", "fill-rule", ff("nonzero"), ie11up("nonzero")),

        /** The style property filter. */
        FILTER("filter", "filter", ff("none"), ieBelow11(""), ie11up("none")),

        /** The style property flex. */
        FLEX("flex", "flex", ff24up(""), ie11up("0 1 auto")),

        /** The style property flex-basis. */
        FLEX_BASIS("flexBasis", "flex-basis", ff24up("auto"), ie11up("auto")),

        /** The style property flex-direction. */
        FLEX_DIRECTION("flexDirection", "flex-direction", ff24up("row"), ie11up("row")),

        /** The style property flex-flow. */
        FLEX_FLOW("flexFlow", "flex-flow", ff31up(""), ie11up("row nowrap")),

        /** The style property flex-grow. */
        FLEX_GROW("flexGrow", "flex-grow", ff24up("0"), ie11up("0")),

        /** The style property flex-shrink. */
        FLEX_SHRINK("flexShrink", "flex-shrink", ff24up("1"), ie11up("1")),

        /** The style property flex-wrap. */
        FLEX_WRAP("flexWrap", "flex-wrap", ff31up("nowrap"), ie11up("nowrap")),

        /** The style property flood-color. */
        FLOOD_COLOR("floodColor", "flood-color", ff("rgb(0, 0, 0)"), ie11up("")),

        /** The style property flood-opacity. */
        FLOOD_OPACITY("floodOpacity", "flood-opacity", ff("1"), ie11up("1")),

        /** The style property font-feature-settings. */
        FONT_FEATURE_SETTINGS("fontFeatureSettings", "font-feature-settings", ie11up("normal")),

        /** The style property glyph-orientation-horizontal. */
        GLYPH_ORIENTATION_HORIZONTAL("glyphOrientationHorizontal", "glyph-orientation-horizontal", ie11up("0deg")),

        /** The style property glyph-orientation-vertical. */
        GLYPH_ORIENTATION_VERTICAL("glyphOrientationVertical", "glyph-orientation-vertical", ie11up("auto")),

        /** The style property image-orientation. */
        IMAGE_ORIENTATION("imageOrientation", "image-orientation", ff31up("0deg")),

        /** The style property image-rendering. */
        IMAGE_RENDERING("imageRendering", "image-rendering", ff("auto")),

        /** The style property ime-mode. */
        IME_MODE("imeMode", "ime-mode", ie11up("undefined")),

        /** The style property ime-mode. */
        JUSTIFY_CONTENT("justifyContent", "justify-content", ff24up("flex-start"), ie11up("flex-start")),

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

        /** The style property lighting-color. */
        LIGHTING_COLOR("lightingColor", "lighting-color",
                ff("rgb(255, 255, 255)"), ie11up("")),

        /** The style property line-break. */
        LINE_BREAK("lineBreak", "line-break", ie11up("undefined")),

        /** The style property line-height. */
        LINE_HEIGHT("lineHeight", "line-height",
                ff("20px"), ieBelow11("20px"), ie11up("normal")),

        /** The style property marker. */
        MARKER("marker", "marker", ff(""), ie11up("none")),

        /** The style property marker-end. */
        MARKER_END("markerEnd", "marker-end", ff("none"), ie11up("none")),

        /** The style property marker-mid. */
        MARKER_MID("markerMid", "marker-mid", ff("none"), ie11up("none")),

        /** The style property marker-start. */
        MARKER_START("markerStart", "marker-start", ff("none"), ie11up("none")),

        /** The style property mask. */
        MASK("mask", "mask", ff("none"), ie11up("none")),

        /** The style property order. */
        ORDER("order", "order", ff24up("0"), ie11up("0")),

        /** The style property page-break-inside. */
        ORPHANS("orphans", "orphans", ie11up("2")),

        /** The style property outline-color. */
        OUTLINE_COLOR("outlineColor", "outline-color", ie11up("transparent")),

        /** The style property page-break-inside. */
        PAGE_BREAK_INSIDE("pageBreakInside", "page-break-inside", ff24up("auto"), ie11up("auto")),

        /** The style property pause. */
        PAUSE("pause", "pause"),

        /** The style property pause-after. */
        PAUSE_AFTER("pauseAfter", "pause-after"),

        /** The style property pause-before. */
        PAUSE_BEFORE("pauseBefore", "pause-before"),

        /** The style property pitch. */
        PITCH("pitch", "pitch"),

        /** The style property pitch-range. */
        PITCH_RANGE("pitchRange", "pitch-range"),

        /** The style property paint-order. */
        PAINT_ORDER("paintOrder", "paint-order", ff31up("normal")),

        /** The style property perspective. */
        PERSPECTIVE("perspective", "perspective", ff("none"), ie11up("none")),

        /** The style property perspective-origin. */
        PERSPECTIVE_ORIGIN("perspectiveOrigin", "perspective-origin",
                ff("624px 171px"), ie11up("620px 163.2px")),

        /** The style property pointer-events. */
        POINTER_EVENTS("pointerEvents", "pointer-events", ie11up("visiblePainted")),

        /** The style property quotes. */
        QUOTES("quotes", "quotes",
                ff("\"“\" \"”\" \"‘\" \"’\""), ie8up("")),

        /** The style property resize. */
        RESIZE("resize", "resize", ff("none")),

        /** The style property richness. */
        RICHNESS("richness", "richness"),

        /** The style property ruby-align. */
        RUBY_ALIGN("rubyAlign", "ruby-align"),

        /** The style property ruby-overhang. */
        RUBY_OVERHANG("rubyOverhang", "ruby-overhang", ie11up("auto")),

        /** The style property ruby-position. */
        RUBY_POSITION("rubyPosition", "ruby-position", ie11up("above")),

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

        /** The style property shape-rendering. */
        SHAPE_RENDERING("shapeRendering", "shape-rendering", ff("auto")),

        /** The style property speak. */
        SPEAK("speak", "speak"),

        /** The style property speak. */
        SPEAK_HEADER("speakHeader", "speak-header"),

        /** The style property speak-numeral. */
        SPEAK_NUMERAL("speakNumeral", "speak-numeral"),

        /** The style property speak-punktuation. */
        SPEAK_PUNCTUATION("speakPunctuation", "speak-punctuation"),

        /** The style property speech-rate. */
        SPEECH_RATE("speechRate", "speech-rate"),

        /** The style property stop-color. */
        STOP_COLOR("stopColor", "stop-color", ff("rgb(0, 0, 0)"), ie11up("")),

        /** The style property stop-opacity. */
        STOP_OPACITY("stopOpacity", "stop-opacity", ff("1"), ie11up("1")),

        /** The style property stroke. */
        STROKE("stroke", "stroke", ff("none"), ie11up("")),

        /** The style property stroke-dasharray. */
        STROKE_DASHARRAY("strokeDasharray", "stroke-dasharray", ff("none"), ie11up("none")),

        /** The style property stroke-dashoffset. */
        STROKE_DASHOFFSET("strokeDashoffset", "stroke-dashoffset",
                ff("0px"), ie11up("0px")),

        /** The style property stroke-linecap. */
        STROKE_LINECAP("strokeLinecap", "stroke-linecap", ff("butt"), ie11up("butt")),

        /** The style property stroke-linejoin. */
        STROKE_LINEJOIN("strokeLinejoin", "stroke-linejoin", ff("miter"), ie11up("miter")),

        /** The style property stroke-miterlimit. */
        STROKE_MITERLIMIT("strokeMiterlimit", "stroke-miterlimit", ff("4"), ie11up("4")),

        /** The style property stroke-opacity. */
        STROKE_OPACITY("strokeOpacity", "stroke-opacity", ff("1"), ie11up("1")),

        /** The style property stroke-width. */
        STROKE_WIDTH("strokeWidth", "stroke-width", ff("1px"), ie11up("0.01px")),

        /** The style property stress. */
        STRESS("stress", "stress"),

        /** The style property style-float. */
        STYLE_FLOAT("styleFloat", "style-float", ie11up("undefined")),

        /** The style property text-align. */
        TEXT_ALIGN("textAlign", "text-align", ie11up("left")),

        /** The style property text-align-last. */
        TEXT_ALIGN_LAST("textAlignLast", "text-align-last", ie11up("auto")),

        /** The style property text-anchor. */
        TEXT_ANCHOR("textAnchor", "text-anchor", ff("start"), ie11up("start")),

        /** The style property text-autospace. */
        TEXT_AUTOSPACE("textAutospace", "text-autospace", ie11up("undefined")),

        /** The style property text-justify. */
        TEXT_JUSTIFY("textJustify", "text-justify", ie11up("auto")),

        /** The style property text-justify-trim. */
        TEXT_JUSTIFY_TRIM("textJustifyTrim", "text-justify-trim", ie11up("undefined")),

        /** The style property text-kashida. */
        TEXT_KASHIDA("textKashida", "text-kashida", ie11up("undefined")),

        /** The style property text-kashida-space. */
        TEXT_TEXT_KASHIDA_SPACE("textKashidaSpace", "text-kashida-space", ie11up("undefined")),

        /** The style property text-overflow. */
        TEXT_OVERFLOW("textOverflow", "text-overflow", ff("clip"), ie11up("clip")),

        /** The style property text-rendering. */
        TEXT_RENDERING("textRendering", "text-rendering", ff("auto")),

        /** The style property text-underline-position. */
        TEXT_UNDERLINE_POSITION("textUnderlinePosition", "text-underline-position", ie11up("auto")),

        /** The style property touch-action. */
        TOUCH_ACTION("touchAction", "touch-action", ie11up("auto")),

        /** The style property transform. */
        TRANSFORM("transform", "transform", ff("none"), ie11up("none")),

        /** The style property transform-origin. */
        TRANSFORM_ORIGIN("transformOrigin", "transform-origin",
                ff("624px 171px"), ie11up("620px 163.2px")),

        /** The style property transform-style. */
        TRANSFORM_STYLE("transformStyle", "transform-style", ff("flat"), ie11up("flat")),

        /** The style property transition. */
        TRANSITION("transition", "transition", ff(""), ie11up("")),

        /** The style property transition-delay. */
        TRANSITION_DELAY("transitionDelay", "transition-delay", ff("0s"), ie11up("0s")),

        /** The style property transition-duration. */
        TRANSITION_DURATION("transitionDuration", "transition-duration",
                ff("0s"), ie11up("0s")),

        /** The style property transition-property. */
        TRANSITION_PROPERTY("transitionProperty", "transition-property",
                ff("all"), ie11up("all")),

        /** The style property transition-timing-function. */
        TRANSITION_TIMING_FUNCTION("transitionTimingFunction",
                "transition-timing-function",
                ff("cubic-bezier(0.25, 0.1, 0.25, 1)"),
                ie11up("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property unicode-bidi. */
        UNICODE_BIDI("unicodeBidi", "unicode-bidi", ff("-moz-isolate"), ieBelow11("embed"), ie11up("normal")),

        /** The style property vector-effect. */
        VECTOR_EFFECT("vectorEffect", "vector-effect", ff("none")),

        /** The style property "voice-family". */
        VOICE_FAMILY("voiceFamily", "voice-family"),

        /** The style property volume. */
        VOLUME("volume", "volume"),

        /** The style property widows. */
        WIDOWS("widows", "widows", ie11up("2")),

        /** The style property word-break. */
        WORD_BREAK("wordBreak", "word-break", ff("normal"), ieBelow11(""), ie11up("normal")),

        /** The style property word-wrap. */
        WORD_WRAP("wordWrap", "word-wrap", ie11up("")),

        /** The style property writing-mode. */
        WRITING_MODE("writingMode", "writing-mode", ie11up("undefined")),

        /** The style property zoom. */
        ZOOM("zoom", "zoom", ie11up("undefined"));

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
