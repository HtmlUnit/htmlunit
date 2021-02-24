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

import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.chromeAndEdge;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.chromeAndEdgeAndFirefox;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.chromeAndEdgeNotIterable;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.edge;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ff;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ffLatest;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ffNotIterable;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ie;
import static com.gargoylesoftware.htmlunit.javascript.host.css.BrowserConfiguration.ieNotIterable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Contains information about the style attribute defined for different browser as well as their default values.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class StyleAttributes {
    private static final Map<String, Definition> styles_ = new HashMap<>();

    static {
        for (final Definition definition : Definition.values()) {
            styles_.put(definition.getPropertyName(), definition);
        }
    }

    private StyleAttributes() {
        // nothing
    }

    /**
     * Gets the style attributes definition with the given name for the specified browser version.
     * @param propertyName the name of the property
     * @param browserVersion the browser version
     * @return {@code null} if no definition exists for this browser version
     */
    public static Definition getDefinition(final String propertyName, final BrowserVersion browserVersion) {
        if (browserVersion == null) {
            return null;
        }

        final Definition definition = styles_.get(propertyName);
        if (definition == null) {
            return null;
        }
        if (!definition.isAvailable(browserVersion, false)) {
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
            if (definition.isAvailable(browserVersion, true)) {
                list.add(definition);
            }
        }

        return list;
    }

    /**
     * Holds information about a style attribute (CSS name, property name, browser availability, default computed value.
     * TODO: move all (?) style attribute definitions here.
     */
    public enum Definition {
        /** The style property {@code accelerator}. */
        ACCELERATOR("accelerator", "accelerator", ie("undefined")),

        /** The style property {@code alignContent}. */
        ALIGN_CONTENT("alignContent", "align-content", ie("stretch"), chromeAndEdgeAndFirefox("normal")),

        /** The style property {@code align-content}. */
        ALIGN_CONTENT_("align-content", "align-content", ff("normal")),

        /** The style property {@code alignItems}. */
        ALIGN_ITEMS("alignItems", "align-items",  ie("stretch"), chromeAndEdgeAndFirefox("normal")),

        /** The style property {@code align-items}. */
        ALIGN_ITEMS_("align-items", "align-items", ff("normal")),

        /** The style property {@code alignSelf}. */
        ALIGN_SELF("alignSelf", "align-self", ie("auto"), chromeAndEdgeAndFirefox("auto")),

        /** The style property {@code align-self}. */
        ALIGN_SELF_("align-self", "align-self", ff("auto")),

        /** The style property {@code alignmentBaseline}. */
        ALIGNMENT_BASELINE("alignmentBaseline", "alignment-baseline", ie("auto"), chromeAndEdge("auto")),

        /** The style property {@code all}. */
        ALL("all", "all", chromeAndEdgeAndFirefox("")),

        /** The style property {@code animation}. */
        ANIMATION("animation", "animation", ff(""), ie(""), chromeAndEdge("none 0s ease 0s 1 normal none running")),

        /** The style property {@code animationDelay}. */
        ANIMATION_DELAY("animationDelay", "animation-delay", ie("0s"), chromeAndEdgeAndFirefox("0s")),

        /** The style property {@code animation-delay}. */
        ANIMATION_DELAY_("animation-delay", "animation-delay", ff("0s")),

        /** The style property {@code animationDirection}. */
        ANIMATION_DIRECTION("animationDirection", "animation-direction", ie("normal"),
                chromeAndEdgeAndFirefox("normal")),

        /** The style property {@code animation-direction}. */
        ANIMATION_DIRECTION_("animation-direction", "animation-direction", ff("normal")),

        /** The style property {@code animationDuration}. */
        ANIMATION_DURATION("animationDuration", "animation-duration", ie("0s"), chromeAndEdgeAndFirefox("0s")),

        /** The style property {@code animation-duration}. */
        ANIMATION_DURATION_("animation-duration", "animation-duration", ff("0s")),

        /** The style property {@code animationFillMode}. */
        ANIMATION_FILL_MODE("animationFillMode", "animation-fill-mode", ie("none"), chromeAndEdgeAndFirefox("none")),

        /** The style property {@code animation-fill-mode}. */
        ANIMATION_FILL_MODE_("animation-fill-mode", "animation-fill-mode", ff("none")),

        /** The style property {@code animationIterationCount}. */
        ANIMATION_ITERATION_COUNT("animationIterationCount", "animation-iteration-count",
                ie("1"), chromeAndEdgeAndFirefox("1")),

        /** The style property {@code animation-iteration-count}. */
        ANIMATION_ITERATION_COUNT_("animation-iteration-count", "animation-iteration-count", ff("1")),

        /** The style property {@code animationName}. */
        ANIMATION_NAME("animationName", "animation-name", ie("none"), chromeAndEdgeAndFirefox("none")),

        /** The style property {@code animation-name}. */
        ANIMATION_NAME_("animation-name", "animation-name", ff("none")),

        /** The style property {@code animationPlayState}. */
        ANIMATION_PLAY_STATE("animationPlayState", "animation-play-state", ie("running"),
                chromeAndEdgeAndFirefox("running")),

        /** The style property {@code animation-play-state}. */
        ANIMATION_PLAY_STATE_("animation-play-state", "animation-play-state", ff("running")),

        /** The style property {@code animationTimingFunction}. */
        ANIMATION_TIMING_FUNCTION("animationTimingFunction", "animation-timing-function",
                ie("cubic-bezier(0.25, 0.1, 0.25, 1)"), chromeAndEdgeAndFirefox("ease")),

        /** The style property {@code animation-timing-function}. */
        ANIMATION_TIMING_FUNCTION_("animation-timing-function", "animation-timing-function", ff("ease")),

        /** The style property {@code appearance}. */
        APPEARANCE("appearance", "appearance", chromeAndEdge("none"), ffLatest("none")),

        /** The style property {@code ascentOverride}. */
        ASCENT_OVERRIDE("ascentOverride", "ascent-override", chromeAndEdge("")),

        /** The style property {@code aspectRatio}. */
        ASPECT_RATIO("aspectRatio", "aspect-ratio", chromeAndEdge("auto")),

        /** The style property {@code azimuth}. */
        AZIMUTH("azimuth", "azimuth"),

        /** The style property {@code backdropFilter}. */
        BACKDROP_FILTER("backdropFilter", "backdrop-filter", chromeAndEdge("none")),

        /** The style property {@code backfaceVisibility}. */
        BACKFACE_VISIBILITY("backfaceVisibility", "backface-visibility", ie("visible"),
                chromeAndEdgeAndFirefox("visible")),

        /** The style property {@code backface-visibility}. */
        BACKFACE_VISIBILITY_("backface-visibility", "backface-visibility", ff("visible")),

        /** The style property {@code background}. */
        BACKGROUND("background", "background", ff(""), ie(""),
                chromeAndEdge(
                        "rgba(0, 0, 0, 0) none repeat scroll 0% 0% / auto padding-box border-box")),

        /** The style property {@code backgroundAttachment}. */
        BACKGROUND_ATTACHMENT("backgroundAttachment", "background-attachment",
                chromeAndEdgeAndFirefox("scroll"), ie("scroll")),

        /** The style property {@code background-attachment}. */
        BACKGROUND_ATTACHMENT_("background-attachment", "background-attachment", ff("scroll")),

        /** The style property {@code backgroundBlendMode}. */
        BACKGROUND_BLEND_MODE("backgroundBlendMode", "background-blend-mode", chromeAndEdgeAndFirefox("normal")),

        /** The style property {@code background-blend-mode}. */
        BACKGROUND_BLEND_MODE_("background-blend-mode", "background-blend-mode", ff("normal")),

        /** The style property {@code backgroundClip}. */
        BACKGROUND_CLIP("backgroundClip", "background-clip",
                ie("border-box"), chromeAndEdgeAndFirefox("border-box")),

        /** The style property {@code background-clip}. */
        BACKGROUND_CLIP_("background-clip", "background-clip", ff("border-box")),

        /** The style property {@code backgroundColor}. */
        BACKGROUND_COLOR("backgroundColor", "background-color",
                chromeAndEdgeAndFirefox("rgba(0, 0, 0, 0)"), ie("transparent")),

        /** The style property {@code background-color}. */
        BACKGROUND_COLOR_("background-color", "background-color", ff("rgba(0, 0, 0, 0)")),

        /** The style property {@code backgroundImage}. */
        BACKGROUND_IMAGE("backgroundImage", "background-image", chromeAndEdgeAndFirefox("none"), ie("none")),

        /** The style property {@code background-image}. */
        BACKGROUND_IMAGE_("background-image", "background-image", ff("none")),

        /** The style property {@code backgroundOrigin}. */
        BACKGROUND_ORIGIN("backgroundOrigin", "background-origin", ie("padding-box"),
                chromeAndEdgeAndFirefox("padding-box")),

        /** The style property {@code background-origin}. */
        BACKGROUND_ORIGIN_("background-origin", "background-origin", ff("padding-box")),

        /** The style property {@code backgroundPosition}. */
        BACKGROUND_POSITION("backgroundPosition", "background-position", chromeAndEdgeAndFirefox("0% 0%"), ie("0% 0%")),

        /** The style property {@code background-position}. */
        BACKGROUND_POSITION_("background-position", "background-position", ff("0% 0%")),

        /** The style property {@code backgroundPositionX}. */
        BACKGROUND_POSITION_X("backgroundPositionX", "background-position-x",
                ie("undefined"), chromeAndEdgeAndFirefox("0%")),

        /** The style property {@code background-position-x}. */
        BACKGROUND_POSITION_X_("background-position-x", "background-position-x", ff("0%")),

        /** The style property {@code backgroundPositionY}. */
        BACKGROUND_POSITION_Y("backgroundPositionY", "background-position-y",
                ie("undefined"), chromeAndEdge("0%"),
                ff("0%")),

        /** The style property {@code background-position-y}. */
        BACKGROUND_POSITION_Y_("background-position-y", "background-position-y", ff("0%")),

        /** The style property {@code backgroundRepeat}. */
        BACKGROUND_REPEAT("backgroundRepeat", "background-repeat", chromeAndEdge("repeat"),
                ff("repeat"), ie("repeat")),

        /** The style property {@code background-repeat}. */
        BACKGROUND_REPEAT_("background-repeat", "background-repeat", ff("repeat")),

        /** The style property {@code backgroundRepeatX}. */
        BACKGROUND_REPEAT_X("backgroundRepeatX", "background-repeat-x", chromeAndEdge("")),

        /** The style property {@code backgroundRepeatY}. */
        BACKGROUND_REPEAT_Y("backgroundRepeatY", "background-repeat-y", chromeAndEdge("")),

        /** The style property {@code backgroundSize}. */
        BACKGROUND_SIZE("backgroundSize", "background-size", ff("auto"),
                ie("auto"), chromeAndEdge("auto")),

        /** The style property {@code background-size}. */
        BACKGROUND_SIZE_("background-size", "background-size", ff("auto")),

        /** The style property {@code baselineShift}. */
        BASELINE_SHIFT("baselineShift", "baseline-shift", ie("baseline"),
                chromeAndEdge("0px")),

        /** The style property {@code behavior}. */
        BEHAVIOR("behavior", "behavior"),

        /** The style property {@code blockSize}. */
        BLOCK_SIZE("blockSize", "block-size", chromeAndEdge("322px"), ff("343px")),

        /** The style property {@code block-size}. */
        BLOCK_SIZE_("block-size", "block-size", ff("343px")),

        /** The style property {@code border}. */
        BORDER("border", "border", chromeAndEdge("0px none rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code borderBlock}. */
        BORDER_BLOCK("borderBlock", "border-block", chromeAndEdge("0px none rgb(0, 0, 0)"), ff("")),

        /** The style property {@code border-block}. */
        BORDER_BLOCK_("border-block", "border-block", ff("")),

        /** The style property {@code borderBlockColor}. */
        BORDER_BLOCK_COLOR("borderBlockColor", "border-block-color", chromeAndEdge("rgb(0, 0, 0)"), ff("")),

        /** The style property {@code border-block-color}. */
        BORDER_BLOCK_COLOR_("border-block-color", "border-block-color", ff("")),

        /** The style property {@code borderBlockEnd}. */
        BORDER_BLOCK_END("borderBlockEnd", "border-block-end", chromeAndEdge("0px none rgb(0, 0, 0)"), ff("")),

        /** The style property {@code border-block-end}. */
        BORDER_BLOCK_END_("border-block-end", "border-block-end", ff("")),

        /** The style property {@code borderBlockEndColor}. */
        BORDER_BLOCK_END_COLOR("borderBlockEndColor", "border-block-end-color", chromeAndEdge("rgb(0, 0, 0)"),
                ff("rgb(0, 0, 0)")),

        /** The style property {@code border-block-end-color}. */
        BORDER_BLOCK_END_COLOR_("border-block-end-color", "border-block-end-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code borderBlockEndStyle}. */
        BORDER_BLOCK_END_STYLE("borderBlockEndStyle", "border-block-end-style", chromeAndEdge("none"),
                ff("none")),

        /** The style property {@code border-block-end-style}. */
        BORDER_BLOCK_END_STYLE_("border-block-end-style", "border-block-end-style", ff("none")),

        /** The style property {@code borderBlockEndWidth}. */
        BORDER_BLOCK_END_WIDTH("borderBlockEndWidth", "border-block-end-width", chromeAndEdge("0px"),
                ff("0px")),

        /** The style property {@code border-block-end-width}. */
        BORDER_BLOCK_END_WIDTH_("border-block-end-width", "border-block-end-width", ff("0px")),

        /** The style property {@code borderBlockStart}. */
        BORDER_BLOCK_START("borderBlockStart", "border-block-start", chromeAndEdge("0px none rgb(0, 0, 0)"), ff("")),

        /** The style property {@code border-block-start}. */
        BORDER_BLOCK_START_("border-block-start", "border-block-start", ff("")),

        /** The style property {@code borderBlockStartColor}. */
        BORDER_BLOCK_START_COLOR("borderBlockStartColor", "border-block-start-color", chromeAndEdge("rgb(0, 0, 0)"),
                ff("rgb(0, 0, 0)")),

        /** The style property {@code border-block-start-color}. */
        BORDER_BLOCK_START_COLOR_("border-block-start-color", "border-block-start-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code borderBlockStartStyle}. */
        BORDER_BLOCK_START_STYLE("borderBlockStartStyle", "border-block-start-style", chromeAndEdge("none"),
                ff("none")),

        /** The style property {@code border-block-start-style}. */
        BORDER_BLOCK_START_STYLE_("border-block-start-style", "border-block-start-style",
                ff("none")),

        /** The style property {@code borderBlockStartWidth}. */
        BORDER_BLOCK_START_WIDTH("borderBlockStartWidth", "border-block-start-width", chromeAndEdge("0px"),
                ff("0px")),

        /** The style property {@code border-block-start-width}. */
        BORDER_BLOCK_START_WIDTH_("border-block-start-width", "border-block-start-width",
                ff("0px")),

        /** The style property {@code borderBlockStyle}. */
        BORDER_BLOCK_STYLE("borderBlockStyle", "border-block-style", chromeAndEdge("none"), ff("")),

        /** The style property {@code border-block-style}. */
        BORDER_BLOCK_STYLE_("border-block-style", "border-block-style", ff("")),

        /** The style property {@code borderBlockWidth}. */
        BORDER_BLOCK_WIDTH("borderBlockWidth", "border-block-width", chromeAndEdge("0px"), ff("")),

        /** The style property {@code border-block-width}. */
        BORDER_BLOCK_WIDTH_("border-block-width", "border-block-width", ff("")),

        /** The style property {@code borderBottom}. */
        BORDER_BOTTOM("borderBottom", "border-bottom", chromeAndEdge("0px none rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code border-bottom}. */
        BORDER_BOTTOM_("border-bottom", "border-bottom", ff("")),

        /** The style property {@code borderBottomColor}. */
        BORDER_BOTTOM_COLOR("borderBottomColor", "border-bottom-color", chromeAndEdge("rgb(0, 0, 0)"),
                ff("rgb(0, 0, 0)"), ie("rgb(0, 0, 0)")),

        /** The style property {@code border-bottom-color}. */
        BORDER_BOTTOM_COLOR_("border-bottom-color", "border-bottom-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code borderBottomLeftRadius}. */
        BORDER_BOTTOM_LEFT_RADIUS("borderBottomLeftRadius", "border-bottom-left-radius",
                ff("0px"), ie("0px"), chromeAndEdge("0px")),

        /** The style property {@code border-bottom-left-radius}. */
        BORDER_BOTTOM_LEFT_RADIUS_("border-bottom-left-radius", "border-bottom-left-radius", ff("0px")),

        /** The style property {@code borderBottomRightRadius}. */
        BORDER_BOTTOM_RIGHT_RADIUS("borderBottomRightRadius", "border-bottom-right-radius",
                ff("0px"), ie("0px"), chromeAndEdge("0px")),

        /** The style property {@code border-bottom-right-radius}. */
        BORDER_BOTTOM_RIGHT_RADIUS_("border-bottom-right-radius", "border-bottom-right-radius", ff("0px")),

        /** The style property {@code borderBottomStyle}. */
        BORDER_BOTTOM_STYLE("borderBottomStyle", "border-bottom-style", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code border-bottom-style}. */
        BORDER_BOTTOM_STYLE_("border-bottom-style", "border-bottom-style", ff("none")),

        /** The style property {@code borderBottomWidth}. */
        BORDER_BOTTOM_WIDTH("borderBottomWidth", "border-bottom-width", chromeAndEdge("0px"), ff("0px"), ie("0px")),

        /** The style property {@code border-bottom-width}. */
        BORDER_BOTTOM_WIDTH_("border-bottom-width", "border-bottom-width", ff("0px")),

        /** The style property {@code borderCollapse}. */
        BORDER_COLLAPSE("borderCollapse", "border-collapse", chromeAndEdge("separate"), ff("separate"), ie("separate")),

        /** The style property {@code border-collapse}. */
        BORDER_COLLAPSE_("border-collapse", "border-collapse", ff("separate")),

        /** The style property {@code borderColor}. */
        BORDER_COLOR("borderColor", "border-color", chromeAndEdge("rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code border-color}. */
        BORDER_COLOR_("border-color", "border-color", ff("")),

        /** The style property {@code borderEndEndRadius}. */
        BORDER_END_END_RADIUS("borderEndEndRadius", "border-end-end-radius", ff("0px")),

        /** The style property {@code border-end-end-radius}. */
        BORDER_END_END_RADIUS_("border-end-end-radius", "border-end-end-radius", ff("0px")),

        /** The style property {@code borderEndStartRadius}. */
        BORDER_END_START_RADIUS("borderEndStartRadius", "border-end-start-radius", ff("0px")),

        /** The style property {@code border-end-start-radius}. */
        BORDER_END_START_RADIUS_("border-end-start-radius", "border-end-start-radius", ff("0px")),

        /** The style property {@code borderImage}. */
        BORDER_IMAGE("borderImage", "border-image", ff(""), ie(""), chromeAndEdge("none")),

        /** The style property {@code border-image}. */
        BORDER_IMAGE_("border-image", "border-image", ff("")),

        /** The style property {@code borderImageOutset}. */
        BORDER_IMAGE_OUTSET("borderImageOutset", "border-image-outset", ie("0"), chromeAndEdge("0"),
                ff("0")),

        /** The style property {@code border-image-outset}. */
        BORDER_IMAGE_OUTSET_("border-image-outset", "border-image-outset", ff("0")),

        /** The style property {@code borderImageRepeat}. */
        BORDER_IMAGE_REPEAT("borderImageRepeat", "border-image-repeat",
                ff("stretch"), ie("stretch"), chromeAndEdge("stretch")),

        /** The style property {@code border-image-repeat}. */
        BORDER_IMAGE_REPEAT_("border-image-repeat", "border-image-repeat",
                ff("stretch")),

        /** The style property {@code borderImageSlice}. */
        BORDER_IMAGE_SLICE("borderImageSlice", "border-image-slice", ie("100%"), chromeAndEdge("100%"),
                ff("100%")),

        /** The style property {@code border-image-slice}. */
        BORDER_IMAGE_SLICE_("border-image-slice", "border-image-slice",
                ff("100%")),

        /** The style property {@code borderImageSource}. */
        BORDER_IMAGE_SOURCE("borderImageSource", "border-image-source", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code border-image-source}. */
        BORDER_IMAGE_SOURCE_("border-image-source", "border-image-source", ff("none")),

        /** The style property {@code borderImageWidth}. */
        BORDER_IMAGE_WIDTH("borderImageWidth", "border-image-width", ie("1"), chromeAndEdge("1"),
                ff("1")),

        /** The style property {@code border-image-width}. */
        BORDER_IMAGE_WIDTH_("border-image-width", "border-image-width", ff("1")),

        /** The style property {@code borderInline}. */
        BORDER_INLINE("borderInline", "border-inline", chromeAndEdge("0px none rgb(0, 0, 0)"), ff("")),

        /** The style property {@code border-inline}. */
        BORDER_INLINE_("border-inline", "border-inline", ff("")),

        /** The style property {@code borderInlineColor}. */
        BORDER_INLINE_COLOR("borderInlineColor", "border-inline-color", chromeAndEdge("rgb(0, 0, 0)"), ff("")),

        /** The style property {@code border-inline-color}. */
        BORDER_INLINE_COLOR_("border-inline-color", "border-inline-color", ff("")),

        /** The style property {@code borderInlineEnd}. */
        BORDER_INLINE_END("borderInlineEnd", "border-inline-end", chromeAndEdge("0px none rgb(0, 0, 0)"), ff("")),

        /** The style property {@code border-inline-end}. */
        BORDER_INLINE_END_("border-inline-end", "border-inline-end", ff("")),

        /** The style property {@code borderInlineEndColor}. */
        BORDER_INLINE_END_COLOR("borderInlineEndColor", "border-inline-end-color", chromeAndEdge("rgb(0, 0, 0)"),
                ff("rgb(0, 0, 0)")),

        /** The style property {@code border-inline-end-color}. */
        BORDER_INLINE_END_COLOR_("border-inline-end-color", "border-inline-end-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code borderInlineEndStyle}. */
        BORDER_INLINE_END_STYLE("borderInlineEndStyle", "border-inline-end-style", chromeAndEdge("none"),
                ff("none")),

        /** The style property {@code border-inline-end-style}. */
        BORDER_INLINE_END_STYLE_("border-inline-end-style", "border-inline-end-style",
                ff("none")),

        /** The style property {@code borderInlineEndWidth}. */
        BORDER_INLINE_END_WIDTH("borderInlineEndWidth", "border-inline-end-width", chromeAndEdge("0px"),
                ff("0px")),

        /** The style property {@code border-inline-end-width}. */
        BORDER_INLINE_END_WIDTH_("border-inline-end-width", "border-inline-end-width",
                ff("0px")),

        /** The style property {@code borderInlineStart}. */
        BORDER_INLINE_START("borderInlineStart", "border-inline-start", chromeAndEdge("0px none rgb(0, 0, 0)"), ff("")),

        /** The style property {@code border-inline-start}. */
        BORDER_INLINE_START_("border-inline-start", "border-inline-start", ff("")),

        /** The style property {@code borderInlineStartColor}. */
        BORDER_INLINE_START_COLOR("borderInlineStartColor", "border-inline-start-color",
                chromeAndEdge("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)")),

        /** The style property {@code border-inline-start-color}. */
        BORDER_INLINE_START_COLOR_("border-inline-start-color", "border-inline-start-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code borderInlineStartStyle}. */
        BORDER_INLINE_START_STYLE("borderInlineStartStyle", "border-inline-start-style", chromeAndEdge("none"),
                ff("none")),

        /** The style property {@code border-inline-start-style}. */
        BORDER_INLINE_START_STYLE_("border-inline-start-style", "border-inline-start-style",
                ff("none")),

        /** The style property {@code borderInlineStartWidth}. */
        BORDER_INLINE_START_WIDTH("borderInlineStartWidth", "border-inline-start-width", chromeAndEdge("0px"),
                ff("0px")),

        /** The style property {@code border-inline-start-width}. */
        BORDER_INLINE_START_WIDTH_("border-inline-start-width", "border-inline-start-width",
                ff("0px")),

        /** The style property {@code borderInlineStyle}. */
        BORDER_INLINE_STYLE("borderInlineStyle", "border-inline-style", chromeAndEdge("none"), ff("")),

        /** The style property {@code border-inline-style}. */
        BORDER_INLINE_STYLE_("border-inline-style", "border-inline-style", ff("")),

        /** The style property {@code borderInlineWidth}. */
        BORDER_INLINE_WIDTH("borderInlineWidth", "border-inline-color", chromeAndEdge("0px"), ff("")),

        /** The style property {@code border-inline-width}. */
        BORDER_INLINE_WIDTH_("border-inline-width", "border-inline-color", ff("")),

        /** The style property {@code borderLeft}. */
        BORDER_LEFT("borderLeft", "border-left", chromeAndEdge("0px none rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code border-left}. */
        BORDER_LEFT_("border-left", "border-left", ff("")),

        /** The style property {@code borderLeftColor}. */
        BORDER_LEFT_COLOR("borderLeftColor", "border-left-color", chromeAndEdge("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)"),
                ie("rgb(0, 0, 0)")),

        /** The style property {@code border-left-color}. */
        BORDER_LEFT_COLOR_("border-left-color", "border-left-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code borderLeftStyle}. */
        BORDER_LEFT_STYLE("borderLeftStyle", "border-left-style", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code border-left-style}. */
        BORDER_LEFT_STYLE_("border-left-style", "border-left-style", ff("none")),

        /** The style property {@code borderLeftWidth}. */
        BORDER_LEFT_WIDTH("borderLeftWidth", "border-left-width", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code border-left-width}. */
        BORDER_LEFT_WIDTH_("border-left-width", "border-left-width", ff("0px")),

        /** The style property {@code borderRadius}. */
        BORDER_RADIUS("borderRadius", "border-radius", ff(""), ie(""), chromeAndEdge("0px")),

        /** The style property {@code border-radius}. */
        BORDER_RADIUS_("border-radius", "border-radius", ff("")),

        /** The style property {@code borderRight}. */
        BORDER_RIGHT("borderRight", "border-right", chromeAndEdge("0px none rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code border-right}. */
        BORDER_RIGHT_("border-right", "border-right", ff("")),

        /** The style property {@code borderRightColor}. */
        BORDER_RIGHT_COLOR("borderRightColor", "border-right-color", chromeAndEdge("rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code border-right-color}. */
        BORDER_RIGHT_COLOR_("border-right-color", "border-right-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code borderRightStyle}. */
        BORDER_RIGHT_STYLE("borderRightStyle", "border-right-style", chromeAndEdge("none"), ff(""), ie("")),

        /** The style property {@code border-right-style}. */
        BORDER_RIGHT_STYLE_("border-right-style", "border-right-style", ff("none")),

        /** The style property {@code borderRightWidth}. */
        BORDER_RIGHT_WIDTH("borderRightWidth", "border-right-width", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code border-right-width}. */
        BORDER_RIGHT_WIDTH_("border-right-width", "border-right-width", ff("0px")),

        /** The style property {@code borderSpacing}. */
        BORDER_SPACING("borderSpacing", "border-spacing", chromeAndEdge("0px 0px"), ff("0px 0px"), ie("0px 0px")),

        /** The style property {@code border-spacing}. */
        BORDER_SPACING_("border-spacing", "border-spacing", ff("0px 0px")),

        /** The style property {@code borderStartEndRadius}. */
        BORDER_START_END_RADIUS("borderStartEndRadius", "border-start-end-radius", ff("0px")),

        /** The style property {@code border-start-end-radius}. */
        BORDER_START_END_RADIUS_("border-start-end-radius", "border-start-end-radius", ff("0px")),

        /** The style property {@code borderStartStartRadius}. */
        BORDER_START_START_RADIUS("borderStartStartRadius", "border-start-start-radius", ff("0px")),

        /** The style property {@code border-start-start-radius}. */
        BORDER_START_START_RADIUS_("border-start-start-radius", "border-start-start-radius", ff("0px")),

        /** The style property {@code borderStyle}. */
        BORDER_STYLE("borderStyle", "border-style", chromeAndEdge("none"), ff(""), ie("")),

        /** The style property {@code border-style}. */
        BORDER_STYLE_("border-style", "border-style", ff("")),

        /** The style property {@code borderTop}. */
        BORDER_TOP("borderTop", "border-top", chromeAndEdge("0px none rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code border-top}. */
        BORDER_TOP_("border-top", "border-top", ff("")),

        /** The style property {@code borderTopColor}. */
        BORDER_TOP_COLOR("borderTopColor", "border-top-color", chromeAndEdge("rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code border-top-color}. */
        BORDER_TOP_COLOR_("border-top-color", "border-top-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code borderTopLeftRadius}. */
        BORDER_TOP_LEFT_RADIUS("borderTopLeftRadius", "border-top-left-radius",
                ff("0px"), ie("0px"), chromeAndEdge("0px")),

        /** The style property {@code border-top-left-radius}. */
        BORDER_TOP_LEFT_RADIUS_("border-top-left-radius", "border-top-left-radius", ff("0px")),

        /** The style property {@code borderTopRightRadius}. */
        BORDER_TOP_RIGHT_RADIUS("borderTopRightRadius", "border-top-right-radius",
                ff("0px"), ie("0px"), chromeAndEdge("0px")),

        /** The style property {@code border-top-right-radius}. */
        BORDER_TOP_RIGHT_RADIUS_("border-top-right-radius", "border-top-right-radius", ff("0px")),

        /** The style property {@code borderTopStyle}. */
        BORDER_TOP_STYLE("borderTopStyle", "border-top-style", chromeAndEdge("none"), ff(""), ie("")),

        /** The style property {@code border-top-style}. */
        BORDER_TOP_STYLE_("border-top-style", "border-top-style", ff("none")),

        /** The style property {@code borderTopWidth}. */
        BORDER_TOP_WIDTH("borderTopWidth", "border-top-width", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code border-top-width}. */
        BORDER_TOP_WIDTH_("border-top-width", "border-top-width", ff("0px")),

        /** The style property {@code borderWidth}. */
        BORDER_WIDTH("borderWidth", "border-width", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code border-width}. */
        BORDER_WIDTH_("border-width", "border-width", ff("")),

        /** The style property {@code bottom}. */
        BOTTOM("bottom", "bottom", chromeAndEdge("auto"), ff(""), ie("")),

        /** The style property {@code boxDecorationBreak}. */
        BOX_DECORATION_BREAK("boxDecorationBreak", "box-decoration-break", ff("slice")),

        /** The style property {@code box-decoration-break}. */
        BOX_DECORATION_BREAK_("box-decoration-break", "box-decoration-break", ff("slice")),

        /** The style property {@code boxShadow}. */
        BOX_SHADOW("boxShadow", "box-shadow", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code box-shadow}. */
        BOX_SHADOW_("box-shadow", "box-shadow", ff("none")),

        /** The style property {@code boxSizing}. */
        BOX_SIZING("boxSizing", "box-sizing", ff("content-box"), ie("content-box"), chromeAndEdge("content-box")),

        /** The style property {@code box-sizing}. */
        BOX_SIZING_("box-sizing", "box-sizing", ff("content-box")),

        /** The style property {@code breakAfter}. */
        BREAK_AFTER("breakAfter", "break-after", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code break-after}. */
        BREAK_AFTER_("break-after", "break-after", ff("auto")),

        /** The style property {@code breakBefore}. */
        BREAK_BEFORE("breakBefore", "break-before", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code break-before}. */
        BREAK_BEFORE_("break-before", "break-before", ff("auto")),

        /** The style property {@code breakInside}. */
        BREAK_INSIDE("breakInside", "break-inside", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code break-inside}. */
        BREAK_INSIDE_("break-inside", "break-inside", ff("auto")),

        /** The style property {@code bufferedRendering}. */
        BUFFERED_RENDERING("bufferedRendering", "buffered-rendering", chromeAndEdge("auto")),

        /** The style property {@code captionSide}. */
        CAPTION_SIDE("captionSide", "caption-side", chromeAndEdge("top"), ff("top"), ie("top")),

        /** The style property {@code caption-side}. */
        CAPTION_SIDE_("caption-side", "caption-side", ff("top")),

        /** The style property {@code caretColor}. */
        CARET_COLOR("caretColor", "caret-color", chromeAndEdge("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)")),

        /** The style property {@code caret-color}. */
        CARET_COLOR_("caret-color", "caret-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code clear}. */
        CLEAR("clear", "clear", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code clip}. */
        CLIP("clip", "clip", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code clipPath}. */
        CLIP_PATH("clipPath", "clip-path", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code clip-path}. */
        CLIP_PATH_("clip-path", "clip-path", ff("none")),

        /** The style property {@code clipRule}. */
        CLIP_RULE("clipRule", "clip-rule", ff("nonzero"), ie("nonzero"), chromeAndEdge("nonzero")),

        /** The style property {@code clip-rule}. */
        CLIP_RULE_("clip-rule", "clip-rule", ff("nonzero")),

        /** The style property {@code color}. */
        COLOR("color", "color", chromeAndEdge("rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code colorAdjust}. */
        COLOR_ADJUST("colorAdjust", "color-adjust", ff("economy")),

        /** The style property {@code color-adjust}. */
        COLOR_ADJUST_("color-adjust", "color-adjust", ff("economy")),

        /** The style property {@code colorInterpolation}. */
        COLOR_INTERPOLATION("colorInterpolation", "color-interpolation", ff("srgb"), chromeAndEdge("srgb")),

        /** The style property {@code color-interpolation}. */
        COLOR_INTERPOLATION_("color-interpolation", "color-interpolation", ff("srgb")),

        /** The style property {@code colorInterpolationFilters}. */
        COLOR_INTERPOLATION_FILTERS("colorInterpolationFilters",
                "color-interpolation-filters", ff("linearrgb"), ie(""), chromeAndEdge("linearrgb")),

        /** The style property {@code color-interpolation-filters}. */
        COLOR_INTERPOLATION_FILTERS_("color-interpolation-filters", "color-interpolation-filters", ff("linearrgb")),

        /** The style property {@code colorRendering}. */
        COLOR_RENDERING("colorRendering", "color-rendering", chromeAndEdge("auto")),

        /** The style property {@code colorScheme}. */
        COLOR_SCHEME("colorScheme", "color-scheme", chromeAndEdge("normal")),

        /** The style property {@code columnCount}. */
        COLUMN_COUNT("columnCount", "column-count", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code column-count}. */
        COLUMN_COUNT_("column-count", "column-count", ff("auto")),

        /** The style property {@code columnFill}. */
        COLUMN_FILL("columnFill", "column-fill", chromeAndEdge("balance"), ff("balance"), ie("balance")),

        /** The style property {@code column-fill}. */
        COLUMN_FILL_("column-fill", "column-fill", ff("balance")),

        /** The style property {@code columnGap}. */
        COLUMN_GAP("columnGap", "column-gap", chromeAndEdge("normal"), ie("normal"), ff("normal")),

        /** The style property {@code column-gap}. */
        COLUMN_GAP_("column-gap", "column-gap", ff("normal")),

        /** The style property {@code columnRule}. */
        COLUMN_RULE("columnRule", "column-rule", chromeAndEdge("0px none rgb(0, 0, 0)"), ff(""), ie("")),

        /** The style property {@code column-rule}. */
        COLUMN_RULE_("column-rule", "column-rule", ff("")),

        /** The style property {@code columnRuleColor}. */
        COLUMN_RULE_COLOR("columnRuleColor", "column-rule-color",
                chromeAndEdge("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)"), ie("rgb(0, 0, 0)")),

        /** The style property {@code column-rule-color}. */
        COLUMN_RULE_COLOR_("column-rule-color", "column-rule-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code columnRuleStyle}. */
        COLUMN_RULE_STYLE("columnRuleStyle", "column-rule-style", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code column-rule-style}. */
        COLUMN_RULE_STYLE_("column-rule-style", "column-rule-style", ff("none")),

        /** The style property {@code columnRuleWidth}. */
        COLUMN_RULE_WIDTH("columnRuleWidth", "column-rule-width", chromeAndEdge("0px"), ff("0px"), ie("medium")),

        /** The style property {@code column-rule-width}. */
        COLUMN_RULE_WIDTH_("column-rule-width", "column-rule-width", ff("0px")),

        /** The style property {@code columnSpan}. */
        COLUMN_SPAN("columnSpan", "column-span", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code column-span}. */
        COLUMN_SPAN_("column-span", "column-span", ff("none")),

        /** The style property {@code columnWidth}. */
        COLUMN_WIDTH("columnWidth", "column-width", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code column-width}. */
        COLUMN_WIDTH_("column-width", "column-width", ff("auto")),

        /** The style property {@code columns}. */
        COLUMNS("columns", "columns", chromeAndEdge("auto auto"), ff(""), ie("")),

        /** The style property {@code contain}. */
        CONTAIN("contain", "contain", chromeAndEdge("none"), ff("none")),

        /** The style property {@code containIntrinsicSize}. */
        CONTAIN_INTRINSIC_SIZE("containIntrinsicSize", "contain-intrinsic-size", chromeAndEdge("auto")),

        /** The style property {@code content}. */
        CONTENT("content", "content", ie("normal"), chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code contentVisibility}. */
        CONTENT_VISISBILITY("contentVisibility", "content-visibility", chromeAndEdge("visible")),

        /** The style property {@code counterIncrement}. */
        COUNTER_INCREMENT("counterIncrement", "counter-increment", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code counter-increment}. */
        COUNTER_INCREMENT_("counter-increment", "counter-increment", ff("none")),

        /** The style property {@code counterReset}. */
        COUNTER_RESET("counterReset", "counter-reset", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code counter-reset}. */
        COUNTER_RESET_("counter-reset", "counter-reset", ff("none")),

        /** The style property {@code counterSet}. */
        COUNTER_SET("counterSet", "counter-set", chromeAndEdge("none"), ff("none")),

        /** The style property {@code counter-set}. */
        COUNTER_SET_("counter-set", "counter-set", ff("none")),

        /** The style property {@code cssFloat}. */
        CSS_FLOAT("cssFloat", "css-float", chromeAndEdge("none"), ff("none"), ie("none")),

        //TODO: seems to be a combination of all other properties.
        /** The style property {@code cssText}. */
        CSS_TEXT("cssText", "css-text", chromeAndEdge(""), ff(""), ie("")),

        /** The style property {@code cue}. */
        CUE("cue", "cue"),

        /** The style property {@code cueAfter}. */
        CUE_AFTER("cueAfter", "cue-after"),

        /** The style property {@code cueBefore}. */
        CUE_BEFORE("cueBefore", "cue-before"),

        /** The style property {@code cursor}. */
        CURSOR("cursor", "cursor", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code cx}. */
        CX("cx", "cx", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code cy}. */
        CY("cy", "cy", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code d}. */
        D("d", "d", chromeAndEdge("none")),

        /** The style property {@code descentOverride}. */
        DESCENT_OVERRIDE("descentOverride", "descent-dverride", chromeAndEdge("")),

        /** The style property {@code direction}. */
        DIRECTION("direction", "direction", chromeAndEdge("ltr"), ff("ltr"), ie("ltr")),

        /** The style property {@code display}. */
        DISPLAY("display", "display", chromeAndEdge("block"), ff(""), ie("")),

        /** The style property {@code dominantBaseline}. */
        DOMINANT_BASELINE("dominantBaseline", "dominant-baseline", ff("auto"), ie("auto"), chromeAndEdge("auto")),

        /** The style property {@code dominant-baseline}. */
        DOMINANT_BASELINE_("dominant-baseline", "dominant-baseline", ff("auto")),

        /** The style property {@code elevation}. */
        ELEVATION("elevation", "elevation"),

        /** The style property {@code emptyCells}. */
        EMPTY_CELLS("emptyCells", "empty-cells", ie("show"), ff("show"),
                chromeAndEdge("show")),

        /** The style property {@code empty-cells}. */
        EMPTY_CELLS_("empty-cells", "empty-cells", ff("show")),

        /** The style property {@code enableBackground}. */
        ENABLE_BACKGROUND("enableBackground", "enable-background", ie("accumulate")),

        /** The style property {@code fill}. */
        FILL("fill", "fill", ff("rgb(0, 0, 0)"), ie("black"), chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code fillOpacity}. */
        FILL_OPACITY("fillOpacity", "fill-opacity", ff("1"), ie("1"), chromeAndEdge("1")),

        /** The style property {@code fill-opacity}. */
        FILL_OPACITY_("fill-opacity", "fill-opacity", ff("1")),

        /** The style property {@code fillRule}. */
        FILL_RULE("fillRule", "fill-rule", ff("nonzero"), ie("nonzero"), chromeAndEdge("nonzero")),

        /** The style property {@code fill-rule}. */
        FILL_RULE_("fill-rule", "fill-rule", ff("nonzero")),

        /** The style property {@code filter}. */
        FILTER("filter", "filter", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code flex}. */
        FLEX("flex", "flex", ff(""), ie("0 1 auto"), chromeAndEdge("0 1 auto")),

        /** The style property {@code flexBasis}. */
        FLEX_BASIS("flexBasis", "flex-basis", ff("auto"), ie("auto"), chromeAndEdge("auto")),

        /** The style property {@code flex-basis}. */
        FLEX_BASIS_("flex-basis", "flex-basis", ff("auto")),

        /** The style property {@code flexDirection}. */
        FLEX_DIRECTION("flexDirection", "flex-direction", ff("row"), ie("row"), chromeAndEdge("row")),

        /** The style property {@code flex-direction}. */
        FLEX_DIRECTION_("flex-direction", "flex-direction", ff("row")),

        /** The style property {@code flexFlow}. */
        FLEX_FLOW("flexFlow", "flex-flow", ff(""), ie("row nowrap"), chromeAndEdge("row nowrap")),

        /** The style property {@code flex-flow}. */
        FLEX_FLOW_("flex-flow", "flex-flow", ff("")),

        /** The style property {@code flexGrow}. */
        FLEX_GROW("flexGrow", "flex-grow", ff("0"), ie("0"), chromeAndEdge("0")),

        /** The style property {@code flex-grow}. */
        FLEX_GROW_("flex-grow", "flex-grow", ff("0")),

        /** The style property {@code flexShrink}. */
        FLEX_SHRINK("flexShrink", "flex-shrink", ff("1"), ie("1"), chromeAndEdge("1")),

        /** The style property {@code flex-shrink}. */
        FLEX_SHRINK_("flex-shrink", "flex-shrink", ff("1")),

        /** The style property {@code flexWrap}. */
        FLEX_WRAP("flexWrap", "flex-wrap", ff("nowrap"), ie("nowrap"), chromeAndEdge("nowrap")),

        /** The style property {@code flex-wrap}. */
        FLEX_WRAP_("flex-wrap", "flex-wrap", ff("nowrap")),

        /** The style property {@code float}. */
        FLOAT("float", "float", ff("none"), chromeAndEdge("none")),

        /** The style property {@code floodColor}. */
        FLOOD_COLOR("floodColor", "flood-color", ff("rgb(0, 0, 0)"), ie(""), chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code flood-color}. */
        FLOOD_COLOR_("flood-color", "flood-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code floodOpacity}. */
        FLOOD_OPACITY("floodOpacity", "flood-opacity", ff("1"), ie("1"), chromeAndEdge("1")),

        /** The style property {@code flood-opacity}. */
        FLOOD_OPACITY_("flood-opacity", "flood-opacity", ff("1")),

        /** The style property {@code font}. */
        FONT("font", "font", chromeAndEdge("16px \"Times New Roman\""), ff(""), ie("")),

        /** The style property {@code fontDisplay}. */
        FONT_DISPLAY("fontDisplay", "font-display", chromeAndEdge("")),

        /** The style property {@code fontFamily}. */
        FONT_FAMILY("fontFamily", "font-family", chromeAndEdge("\"Times New Roman\""),
                ie("Times New Roman"), ff("serif")),

        /** The style property {@code font-family}. */
        FONT_FAMILY_("font-family", "font-family", ff("serif")),

        /** The style property {@code fontFeatureSettings}. */
        FONT_FEATURE_SETTINGS("fontFeatureSettings", "font-feature-settings",
                ie("normal"), ff("normal"), chromeAndEdge("normal")),

        /** The style property {@code font-feature-settings}. */
        FONT_FEATURE_SETTINGS_("font-feature-settings", "font-feature-settings", ff("normal")),

        /** The style property {@code fontKerning}. */
        FONT_KERNING("fontKerning", "font-kerning", ff("auto"), chromeAndEdge("auto")),

        /** The style property {@code font-kerning}. */
        FONT_KERNING_("font-kerning", "font-kerning", ff("auto")),

        /** The style property {@code fontLanguageOverride}. */
        FONT_LANGUAGE_OVERRIDE("fontLanguageOverride", "font-language-override", ff("normal")),

        /** The style property {@code font-language-override}. */
        FONT_LANGUAGE_OVERRIDE_("font-language-override", "font-language-override", ff("normal")),

        /** The style property {@code fontOpticalSizing}. */
        FONT_OPTICAL_SIZING("fontOpticalSizing", "font-optical-sizing", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code font-optical-sizing}. */
        FONT_OPTICAL_SIZING_("font-optical-sizing", "font-optical-sizing", ff("auto")),

        /** The style property {@code fontSize}. */
        FONT_SIZE("fontSize", "font-size", chromeAndEdge("16px"), ff("16px"), ie("16px")),

        /** The style property {@code font-size}. */
        FONT_SIZE_("font-size", "font-size", ff("16px")),

        /** The style property {@code fontSizeAdjust}. */
        FONT_SIZE_ADJUST("fontSizeAdjust", "font-size-adjust", ff("none"), ie("none")),

        /** The style property {@code font-size-adjust}. */
        FONT_SIZE_ADJUST_("font-size-adjust", "font-size-adjust", ff("none")),

        /** The style property {@code fontStretch}. */
        FONT_STRETCH("fontStretch", "font-stretch", chromeAndEdge("100%"),
                ff("100%"), ie("normal")),

        /** The style property {@code font-stretch}. */
        FONT_STRETCH_("font-stretch", "font-stretch", ff("100%")),

        /** The style property {@code fontStyle}. */
        FONT_STYLE("fontStyle", "font-style", chromeAndEdge("normal"), ff("normal"), ie("normal")),

        /** The style property {@code font-style}. */
        FONT_STYLE_("font-style", "font-style", ff("normal")),

        /** The style property {@code fontSynthesis}. */
        FONT_SYNTHESIS("fontSynthesis", "font-synthesis", ff("weight style")),

        /** The style property {@code font-synthesis}. */
        FONT_SYNTHESIS_("font-synthesis", "font-synthesis", ff("weight style")),

        /** The style property {@code fontVariant}. */
        FONT_VARIANT("fontVariant", "font-variant", chromeAndEdge("normal"), ff("normal"), ie("normal")),

        /** The style property {@code font-variant}. */
        FONT_VARIANT_("font-variant", "font-variant", ff("normal")),

        /** The style property {@code fontVariantAlternates}. */
        FONT_VARIANT_ALTERNATES("fontVariantAlternates", "font-variant-alternates", ff("normal")),

        /** The style property {@code font-variant-alternates}. */
        FONT_VARIANT_ALTERNATES_("font-variant-alternates", "font-variant-alternates", ff("normal")),

        /** The style property {@code fontVariantCaps}. */
        FONT_VARIANT_CAPS("fontVariantCaps", "font-variant-caps", ff("normal"), chromeAndEdge("normal")),

        /** The style property {@code font-variant-caps}. */
        FONT_VARIANT_CAPS_("font-variant-caps", "font-variant-caps", ff("normal")),

        /** The style property {@code fontVariantEastAsian}. */
        FONT_VARIANT_EAST_ASIAN("fontVariantEastAsian", "font-variant-east-asian", ff("normal"),
                chromeAndEdge("normal")),

        /** The style property {@code font-variant-east-asian}. */
        FONT_VARIANT_EAST_ASIAN_("font-variant-east-asian", "font-variant-east-asian", ff("normal")),

        /** The style property {@code fontVariantLigatures}. */
        FONT_VARIANT_LIGATURES("fontVariantLigatures", "font-variant-ligatures", ff("normal"), chromeAndEdge("normal")),

        /** The style property {@code font-variant-ligatures}. */
        FONT_VARIANT_LIGATURES_("font-variant-ligatures", "font-variant-ligatures", ff("normal")),

        /** The style property {@code fontVariantNumeric}. */
        FONT_VARIANT_NUMERIC("fontVariantNumeric", "font-variant-numeric", ff("normal"), chromeAndEdge("normal")),

        /** The style property {@code font-variant-numeric}. */
        FONT_VARIANT_NUMERIC_("font-variant-numeric", "font-variant-numeric", ff("normal")),

        /** The style property {@code fontVariantPosition}. */
        FONT_VARIANT_POSITION("fontVariantPosition", "font-variant-position", ff("normal")),

        /** The style property {@code font-variant-position}. */
        FONT_VARIANT_POSITION_("font-variant-position", "font-variant-position", ff("normal")),

        /** The style property {@code fontVariationSettings}. */
        FONT_VARIATION_SETTING("fontVariationSettings", "font-variation-settings",
                chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code font-variation-settings}. */
        FONT_VARIATION_SETTING_("font-variation-settings", "font-variation-settings", ff("normal")),

        /** The style property {@code fontWeight}. */
        FONT_WEIGHT("fontWeight", "font-weight", chromeAndEdge("400"), ff("400"), ie("400")),

        /** The style property {@code font-weight}. */
        FONT_WEIGHT_("font-weight", "font-weight", ff("400")),

        /** The style property {@code forcedColorAdjust}. */
        FORCED_COLOR_ADJUST("forcedColorAdjust", "forced-color-adjust",
                edge("auto")),

        /** The style property {@code gap}. */
        GAP("gap", "gap", chromeAndEdge("normal"), ff("")),

        /** The style property {@code glyphOrientationHorizontal}. */
        GLYPH_ORIENTATION_HORIZONTAL("glyphOrientationHorizontal", "glyph-orientation-horizontal",
                ie("0deg")),

        /** The style property {@code glyphOrientationVertical}. */
        GLYPH_ORIENTATION_VERTICAL("glyphOrientationVertical", "glyph-orientation-vertical",
                ie("auto")),

        /** The style property {@code grid}. */
        GRID("grid", "grid", chromeAndEdge("none / none / none / row / auto / auto"),
                ff("")),

        /** The style property {@code gridArea}. */
        GRID_AREA("gridArea", "grid-area", chromeAndEdge("auto / auto / auto / auto"), ff("")),

        /** The style property {@code grid-area}. */
        GRID_AREA_("grid-area", "grid-area", ff("")),

        /** The style property {@code gridAutoColumns}. */
        GRID_AUTO_COLUMNS("gridAutoColumns", "grid-auto-columns", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code grid-auto-columns}. */
        GRID_AUTO_COLUMNS_("grid-auto-columns", "grid-auto-columns", ff("auto")),

        /** The style property {@code gridAutoFlow}. */
        GRID_AUTO_FLOW("gridAutoFlow", "grid-auto-flow", chromeAndEdge("row"), ff("row")),

        /** The style property {@code grid-auto-flow}. */
        GRID_AUTO_FLOW_("grid-auto-flow", "grid-auto-flow", ff("row")),

        /** The style property {@code gridAutoRows}. */
        GRID_AUTO_ROWS("gridAutoRows", "grid-auto-rows", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code grid-auto-rows}. */
        GRID_AUTO_ROWS_("grid-auto-rows", "grid-auto-rows", ff("auto")),

        /** The style property {@code gridColumn}. */
        GRID_COLUMN("gridColumn", "grid-column", chromeAndEdge("auto / auto"), ff("")),

        /** The style property {@code grid-column}. */
        GRID_COLUMN_("grid-column", "grid-column", ff("")),

        /** The style property {@code gridColumnEnd}. */
        GRID_COLUMN_END("gridColumnEnd", "grid-column-end", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code grid-column-end}. */
        GRID_COLUMN_END_("grid-column-end", "grid-column-end", ff("auto")),

        /** The style property {@code gridColumnGap}. */
        GRID_COLUMN_GAP("gridColumnGap", "grid-column-gap", chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code grid-column-gap}. */
        GRID_COLUMN_GAP_("grid-column-gap", "grid-column-gap", ff("normal")),

        /** The style property {@code gridColumnStart}. */
        GRID_COLUMN_START("gridColumnStart", "grid-column-start", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code grid-column-start}. */
        GRID_COLUMN_START_("grid-column-start", "grid-column-start", ff("auto")),

        /** The style property {@code gridGap}. */
        GRID_GAP("gridGap", "grid-gap", chromeAndEdge("normal normal"), ff("")),

        /** The style property {@code grid-gap}. */
        GRID_GAP_("grid-gap", "grid-gap", ff("")),

        /** The style property {@code gridRow}. */
        GRID_ROW("gridRow", "grid-row", chromeAndEdge("auto / auto"), ff("")),

        /** The style property {@code grid-row}. */
        GRID_ROW_("grid-row", "grid-row", ff("")),

        /** The style property {@code gridRowEnd}. */
        GRID_ROW_END("gridRowEnd", "grid-row-end", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code grid-row-end}. */
        GRID_ROW_END_("grid-row-end", "grid-row-end", ff("auto")),

        /** The style property {@code gridRowGap}. */
        GRID_ROW_GAP("gridRowGap", "grid-row-gap", chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code grid-row-gap}. */
        GRID_ROW_GAP_("grid-row-gap", "grid-row-gap", ff("normal")),

        /** The style property {@code gridRowStart}. */
        GRID_ROW_START("gridRowStart", "grid-row-start", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code grid-row-start}. */
        GRID_ROW_START_("grid-row-start", "grid-row-start", ff("auto")),

        /** The style property {@code gridTemplate}. */
        GRID_TEMPLATE("gridTemplate", "grid-template", chromeAndEdge("none / none / none"), ff("")),

        /** The style property {@code grid-template}. */
        GRID_TEMPLATE_("grid-template", "grid-template", ff("")),

        /** The style property {@code gridTemplateAreas}. */
        GRID_TEMPLATE_AREAS("gridTemplateAreas", "grid-template-areas", chromeAndEdge("none"),
                ff("none")),

        /** The style property {@code grid-template-areas}. */
        GRID_TEMPLATE_AREAS_("grid-template-areas", "grid-template-areas",
                ff("none")),

        /** The style property {@code gridTemplateColumns}. */
        GRID_TEMPLATE_COLUMNS("gridTemplateColumns", "grid-template-columns", chromeAndEdge("none"), ff("none")),

        /** The style property {@code grid-template-columns}. */
        GRID_TEMPLATE_COLUMNS_("grid-template-columns", "grid-template-columns", ff("none")),

        /** The style property {@code gridTemplateRows}. */
        GRID_TEMPLATE_ROWS("gridTemplateRows", "grid-template-rows", chromeAndEdge("none"), ff("none")),

        /** The style property {@code grid-template-rows}. */
        GRID_TEMPLATE_ROWS_("grid-template-rows", "grid-template-rows", ff("none")),

        /** The style property {@code height}. */
        HEIGHT("height", "height", chromeAndEdge(""), ff(""), ie("")),

        /** The style property {@code hyphens}. */
        HYPHENS("hyphens", "hyphens", ff("manual"), chromeAndEdge("manual")),

        /** The style property {@code imageOrientation}. */
        IMAGE_ORIENTATION("imageOrientation", "image-orientation", chromeAndEdge("from-image"),
                ff("from-image")),

        /** The style property {@code image-orientation}. */
        IMAGE_ORIENTATION_("image-orientation", "image-orientation",
                ff("from-image")),

        /** The style property {@code imageRendering}. */
        IMAGE_RENDERING("imageRendering", "image-rendering", ff("auto"), chromeAndEdge("auto")),

        /** The style property {@code image-rendering}. */
        IMAGE_RENDERING_("image-rendering", "image-rendering", ff("auto")),

        /** The style property {@code imeMode}. */
        IME_MODE("imeMode", "ime-mode", ie("undefined"), ff("auto")),

        /** The style property {@code ime-mode}. */
        IME_MODE_("ime-mode", "ime-mode", ff("auto")),

        /** The style property {@code inherits}. */
        INHERITS("inherits", "inherits", chromeAndEdge("")),

        /** The style property {@code initialValue}. */
        INITIAL_VALUE("initialValue", "initial-value", chromeAndEdge("")),

        /** The style property {@code inlineSize}. */
        INLINE_SIZE("inlineSize", "inline-size", ff("1244px"), chromeAndEdge("1240px")),

        /** The style property {@code inline-size}. */
        INLINE_SIZE_("inline-size", "inline-size", ff("1244px")),

        /** The style property {@code inset}. */
        INSET("inset", "inset", chromeAndEdge("auto"), ff("")),

        /** The style property {@code insetBlock}. */
        INSET_BLOCK("insetBlock", "inset-block", chromeAndEdge("auto"), ff("")),

        /** The style property {@code inset-block}. */
        INSET_BLOCK_("inset-block", "inset-block", ff("")),

        /** The style property {@code insetBlockEnd}. */
        INSET_BLOCK_END("insetBlockEnd", "inset-block-end", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code inset-block-end}. */
        INSET_BLOCK_END_("inset-block-end", "inset-block-end", ff("auto")),

        /** The style property {@code insetBlockStart}. */
        INSET_BLOCK_START("insetBlockStart", "inset-block-start", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code inset-block-start}. */
        INSET_BLOCK_START_("inset-block-start", "inset-block-start", ff("auto")),

        /** The style property {@code insetInline}. */
        INSET_INLINE("insetInline", "inset-inline", chromeAndEdge("auto"), ff("")),

        /** The style property {@code inset-inline}. */
        INSET_INLINE_("inset-inline", "inset-inline", ff("")),

        /** The style property {@code insetInlineEnd}. */
        INSET_INLINE_END("insetInlineEnd", "inset-inline-end", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code inset-inline-end}. */
        INSET_INLINE_END_("inset-inline-end", "inset-inline-end", ff("auto")),

        /** The style property {@code insetInlineStart}. */
        INSET_INLINE_START("insetInlineStart", "inset-inline-start", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code inset-inline-start}. */
        INSET_INLINE_START_("inset-inline-start", "inset-inline-start", ff("auto")),

        /** The style property {@code isolation}. */
        ISOLATION("isolation", "isolation", ff("auto"), chromeAndEdge("auto")),

        /** The style property {@code justifyContent}. */
        JUSTIFY_CONTENT("justifyContent", "justify-content",
                ff("normal"),
                ie("flex-start"), chromeAndEdge("normal")),

        /** The style property {@code justify-content}. */
        JUSTIFY_CONTENT_("justify-content", "justify-content", ff("normal")),

        /** The style property {@code justifyItems}. */
        JUSTIFY_ITEMS("justifyItems", "justify-items", ff("normal"), chromeAndEdge("normal")),

        /** The style property {@code justify-items}. */
        JUSTIFY_ITEMS_("justify-items", "justify-items", ff("normal")),

        /** The style property {@code justifySelf}. */
        JUSTIFY_SELF("justifySelf", "justify-self", ff("auto"), chromeAndEdge("auto")),

        /** The style property {@code justify-self}. */
        JUSTIFY_SELF_("justify-self", "justify-self", ff("auto")),

        /** The style property {@code kerning}. */
        KERNING("kerning", "kerning", ie("auto")),

        /** The style property {@code layoutFlow}. */
        LAYOUT_FLOW("layoutFlow", "layout-flow", ie("undefined")),

        /** The style property {@code layoutGrid}. */
        LAYOUT_GRID("layoutGrid", "layout-grid", ie("undefined")),

        /** The style property {@code layoutGridChar}. */
        LAYOUT_GRID_CHAR("layoutGridChar", "layout-grid-char", ie("undefined")),

        /** The style property {@code layoutGridLine}. */
        LAYOUT_GRID_LINE("layoutGridLine", "layout-grid-line", ie("undefined")),

        /** The style property {@code layoutGridMode}. */
        LAYOUT_GRID_MODE("layoutGridMode", "layout-grid-mode", ie("undefined")),

        /** The style property {@code layoutGridType}. */
        LAYOUT_GRID_TYPE("layoutGridType", "layout-grid-type", ie("undefined")),

        /** The style property {@code left}. */
        LEFT("left", "left", chromeAndEdge("auto"), ff(""), ie("")),

        /** The style property {@code letterSpacing}. */
        LETTER_SPACING("letterSpacing", "letter-spacing", chromeAndEdge("normal"), ff(""), ie("")),

        /** The style property {@code letter-spacing}. */
        LETTER_SPACING_("letter-spacing", "letter-spacing", ff("normal")),

        /** The style property {@code lightingColor}. */
        LIGHTING_COLOR("lightingColor", "lighting-color",
                ff("rgb(255, 255, 255)"), ie(""), chromeAndEdge("rgb(255, 255, 255)")),

        /** The style property {@code lighting-color}. */
        LIGHTING_COLOR_("lighting-color", "lighting-color", ff("rgb(255, 255, 255)")),

        /** The style property {@code lineBreak}. */
        LINE_BREAK("lineBreak", "line-break", ie("undefined"), chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code line-break}. */
        LINE_BREAK_("line-break", "line-break", ff("auto")),

        /** The style property {@code lineGapOverride}. */
        LINE_GAP_OVERRIDE("lineGapOverride", "line-gap-oOverride", chromeAndEdge("")),

        /** The style property {@code lineHeight}. */
        LINE_HEIGHT("lineHeight", "line-height", ff("normal"),
                ie("normal"), chromeAndEdge("normal")),

        /** The style property {@code line-height}. */
        LINE_HEIGHT_("line-height", "line-height", ff("normal")),

        /** The style property {@code listStyle}. */
        LIST_STYLE("listStyle", "list-style", chromeAndEdge("outside none disc"), ff(""), ie("")),

        /** The style property {@code list-style}. */
        LIST_STYLE_("list-style", "list-style", ff("")),

        /** The style property {@code listStyleImage}. */
        LIST_STYLE_IMAGE("listStyleImage", "list-style-image", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code list-style-image}. */
        LIST_STYLE_IMAGE_("list-style-image", "list-style-image", ff("none")),

        /** The style property {@code listStylePosition}. */
        LIST_STYLE_POSITION("listStylePosition", "list-style-position",
                chromeAndEdge("outside"), ff("outside"), ie("outside")),

        /** The style property {@code list-style-position}. */
        LIST_STYLE_POSITION_("list-style-position", "list-style-position", ff("outside")),

        /** The style property {@code listStyleType}. */
        LIST_STYLE_TYPE("listStyleType", "list-style-type", chromeAndEdge("disc"), ff("disc"), ie("disc")),

        /** The style property {@code list-style-type}. */
        LIST_STYLE_TYPE_("list-style-type", "list-style-type", ff("disc")),

        /** The style property {@code margin}. */
        MARGIN("margin", "margin", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code marginBlock}. */
        MARGIN_BLOCK("marginBlock", "margin-block", chromeAndEdge("0px"), ff("")),

        /** The style property {@code margin-block}. */
        MARGIN_BLOCK_("margin-block", "margin-block", ff("")),

        /** The style property {@code marginBlockEnd}. */
        MARGIN_BLOCK_END("marginBlockEnd", "margin-block-end", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code margin-block-end}. */
        MARGIN_BLOCK_END_("margin-block-end", "margin-block-end", ff("0px")),

        /** The style property {@code marginBlockStart}. */
        MARGIN_BLOCK_START("marginBlockStart", "margin-block-start", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code margin-block-start}. */
        MARGIN_BLOCK_START_("margin-block-start", "margin-block-start", ff("0px")),

        /** The style property {@code marginBottom}. */
        MARGIN_BOTTOM("marginBottom", "margin-bottom", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code margin-bottom}. */
        MARGIN_BOTTOM_("margin-bottom", "margin-bottom", ff("0px")),

        /** The style property {@code marginInline}. */
        MARGIN_INLINE("marginInline", "margin-inline", chromeAndEdge("0px"), ff("")),

        /** The style property {@code margin-inline}. */
        MARGIN_INLINE_("margin-inline", "margin-inline", ff("")),

        /** The style property {@code marginInlineEnd}. */
        MARGIN_INLINE_END("marginInlineEnd", "margin-inline-end", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code margin-inline-end}. */
        MARGIN_INLINE_END_("margin-inline-end", "margin-inline-end", ff("0px")),

        /** The style property {@code marginInlineStart}. */
        MARGIN_INLINE_START("marginInlineStart", "margin-inline-start", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code margin-inline-start}. */
        MARGIN_INLINE_START_("margin-inline-start", "margin-inline-start", ff("0px")),

        /** The style property {@code marginLeft}. */
        MARGIN_LEFT("marginLeft", "margin-left", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code margin-left}. */
        MARGIN_LEFT_("margin-left", "margin-left", ff("0px")),

        /** The style property {@code marginRight}. */
        MARGIN_RIGHT("marginRight", "margin-right", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code margin-right}. */
        MARGIN_RIGHT_("margin-right", "margin-right", ff("0px")),

        /** The style property {@code marginTop}. */
        MARGIN_TOP("marginTop", "margin-top", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code margin-top}. */
        MARGIN_TOP_("margin-top", "margin-top", ff("0px")),

        /** The style property {@code marker}. */
        MARKER("marker", "marker", ff(""), ie("none"), chromeAndEdge("none")),

        /** The style property {@code markerEnd}. */
        MARKER_END("markerEnd", "marker-end", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code marker-end}. */
        MARKER_END_("marker-end", "marker-end", ff("none")),

        /** The style property {@code markerMid}. */
        MARKER_MID("markerMid", "marker-mid", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code marker-mid}. */
        MARKER_MID_("marker-mid", "marker-mid", ff("none")),

        /** The style property {@code markerOffset}. */
        MARKER_OFFSET("markerOffset", "marker-offset"),

        /** The style property {@code marker-offset}. */
        MARKER_OFFSET_("marker-offset", "marker-offset"),

        /** The style property {@code markerStart}. */
        MARKER_START("markerStart", "marker-start", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code marker-start}. */
        MARKER_START_("marker-start", "marker-start", ff("none")),

        /** The style property {@code mask}. */
        MASK("mask", "mask", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code maskClip}. */
        MASK_CLIP("maskClip", "mask-clip", ff("border-box")),

        /** The style property {@code mask-clip}. */
        MASK_CLIP_("mask-clip", "mask-clip", ff("border-box")),

        /** The style property {@code maskComposite}. */
        MASK_COMPOSITE("maskComposite", "mask-composite", ff("add")),

        /** The style property {@code mask-composite}. */
        MASK_COMPOSITE_("mask-composite", "mask-composite", ff("add")),

        /** The style property {@code maskImage}. */
        MASK_IMAGE("maskImage", "mask-image", ff("none")),

        /** The style property {@code mask-image}. */
        MASK_IMAGE_("mask-image", "mask-image", ff("none")),

        /** The style property {@code maskMode}. */
        MASK_MODE("maskMode", "mask-mode", ff("match-source")),

        /** The style property {@code mask-mode}. */
        MASK_MODE_("mask-mode", "mask-mode", ff("match-source")),

        /** The style property {@code maskOrigin}. */
        MASK_ORIGIN("maskOrigin", "mask-origin", ff("border-box")),

        /** The style property {@code mask-origin}. */
        MASK_ORIGIN_("mask-origin", "mask-origin", ff("border-box")),

        /** The style property {@code maskPosition}. */
        MASK_POSITION("maskPosition", "mask-position", ff("0% 0%")),

        /** The style property {@code mask-position}. */
        MASK_POSITION_("mask-position", "mask-position", ff("0% 0%")),

        /** The style property {@code maskPositionX}. */
        MASK_POSITION_X("maskPositionX", "mask-position-x", ff("0%")),

        /** The style property {@code mask-position-x}. */
        MASK_POSITION_X_("mask-position-x", "mask-position-x", ff("0%")),

        /** The style property {@code maskPositionY}. */
        MASK_POSITION_Y("maskPositionY", "mask-position-y", ff("0%")),

        /** The style property {@code mask-position-y}. */
        MASK_POSITION_Y_("mask-position-y", "mask-position-y", ff("0%")),

        /** The style property {@code maskRepeat}. */
        MASK_REPEAT("maskRepeat", "mask-repeat", ff("repeat")),

        /** The style property {@code mask-repeat}. */
        MASK_REPEAT_("mask-repeat", "mask-repeat", ff("repeat")),

        /** The style property {@code maskSize}. */
        MASK_SIZE("maskSize", "mask-size", ff("auto")),

        /** The style property {@code mask-size}. */
        MASK_SIZE_("mask-size", "mask-size", ff("auto")),

        /** The style property {@code maskType}. */
        MASK_TYPE("maskType", "mask-type", ff("luminance"), chromeAndEdge("luminance")),

        /** The style property {@code mask-type}. */
        MASK_TYPE_("mask-type", "mask-type", ff("luminance")),

        /** The style property {@code maxBlockSize}. */
        MAX_BLOCK_SIZE("maxBlockSize", "max-block-size", ff("none"), chromeAndEdge("none")),

        /** The style property {@code max-block-size}. */
        MAX_BLOCK_SIZE_("max-block-size", "max-block-size", ff("none")),

        /** The style property {@code maxHeight}. */
        MAX_HEIGHT("maxHeight", "max-height", chromeAndEdge("none"), ff(""), ie("")),

        /** The style property {@code max-height}. */
        MAX_HEIGHT_("max-height", "max-height", ff("none")),

        /** The style property {@code maxInlineSize}. */
        MAX_INLINE_SIZE("maxInlineSize", "max-inline-size", ff("none"), chromeAndEdge("none")),

        /** The style property {@code max-inline-size}. */
        MAX_INLINE_SIZE_("max-inline-size", "max-inline-size", ff("none")),

        /** The style property {@code maxWidth}. */
        MAX_WIDTH("maxWidth", "max-width", chromeAndEdge("none"), ff(""), ie("")),

        /** The style property {@code max-width}. */
        MAX_WIDTH_("max-width", "max-width", ff("none")),

        /** The style property {@code maxZoom}. */
        MAX_ZOOM("maxZoom", "max-zoom", chromeAndEdge("")),

        /** The style property {@code minBlockSize}. */
        MIN_BLOCK_SIZE("minBlockSize", "min-block-size", ff("0px"), chromeAndEdge("0px")),

        /** The style property {@code min-block-size}. */
        MIN_BLOCK_SIZE_("min-block-size", "min-block-size", ff("0px")),

        /** The style property {@code minHeight}. */
        MIN_HEIGHT("minHeight", "min-height", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code min-height}. */
        MIN_HEIGHT_("min-height", "min-height", ff("0px")),

        /** The style property {@code minInlineSize}. */
        MIN_INLINE_SIZE("minInlineSize", "min-inline-size", ff("0px"), chromeAndEdge("0px")),

        /** The style property {@code min-inline-size}. */
        MIN_INLINE_SIZE_("min-inline-size", "min-inline-size", ff("0px")),

        /** The style property {@code minWidth}. */
        MIN_WIDTH("minWidth", "min-width", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code min-width}. */
        MIN_WIDTH_("min-width", "min-width", ff("0px")),

        /** The style property {@code minZoom}. */
        MIN_ZOOM("minZoom", "min-zoom", chromeAndEdge("")),

        /** The style property {@code mixBlendMode}. */
        MIX_BLEND_MODE("mixBlendMode", "mix-blend-mode", ff("normal"), chromeAndEdge("normal")),

        /** The style property {@code mix-blend-mode}. */
        MIX_BLEND_MODE_("mix-blend-mode", "mix-blend-mode", ff("normal")),

        /** The style property {@code MozAnimation}. */
        MOZ_ANIMATION("MozAnimation", "-moz-animation", ff("")),

        /** The style property {@code -moz-animation}. */
        MOZ_ANIMATION__("-moz-animation", "-moz-animation", ff("")),

        /** The style property {@code MozAnimationDelay}. */
        MOZ_ANIMATION_DELAY("MozAnimationDelay", "-moz-animation-delay", ff("0s")),

        /** The style property {@code -moz-animation-delay}. */
        MOZ_ANIMATION_DELAY__("-moz-animation-delay", "-moz-animation-delay", ff("0s")),

        /** The style property {@code MozAnimationDirection}. */
        MOZ_ANIMATION_DIRECTION("MozAnimationDirection", "-moz-animation-direction", ff("normal")),

        /** The style property {@code -moz-animation-direction}. */
        MOZ_ANIMATION_DIRECTION__("-moz-animation-direction", "-moz-animation-direction", ff("normal")),

        /** The style property {@code MozAnimationDuration}. */
        MOZ_ANIMATION_DURATION("MozAnimationDuration", "-moz-animation-duration", ff("0s")),

        /** The style property {@code -moz-animation-duration}. */
        MOZ_ANIMATION_DURATION__("-moz-animation-duration", "-moz-animation-duration", ff("0s")),

        /** The style property {@code MozAnimationFillMode}. */
        MOZ_ANIMATION_FILL_MODE("MozAnimationFillMode", "-moz-animation-fill-mode", ff("none")),

        /** The style property {@code -moz-animation-fill-mode}. */
        MOZ_ANIMATION_FILL_MODE__("-moz-animation-fill-mode", "-moz-animation-fill-mode", ff("none")),

        /** The style property {@code MozAnimationIterationCount}. */
        MOZ_ANIMATION_ITERATION_COUNT("MozAnimationIterationCount", "-moz-animation-iteration-count", ff("1")),

        /** The style property {@code -moz-animation-iteration-count}. */
        MOZ_ANIMATION_ITERATION_COUNT__("-moz-animation-iteration-count", "-moz-animation-iteration-count",
                ff("1")),

        /** The style property {@code MozAnimationName}. */
        MOZ_ANIMATION_NAME("MozAnimationName", "-moz-animation-name", ff("none")),

        /** The style property {@code -moz-animation-name}. */
        MOZ_ANIMATION_NAME__("-moz-animation-name", "-moz-animation-name", ff("none")),

        /** The style property {@code MozAnimationPlayState}. */
        MOZ_ANIMATION_PLAY_STATE("MozAnimationPlayState", "-moz-animation-play-state", ff("running")),

        /** The style property {@code -moz-animation-play-state}. */
        MOZ_ANIMATION_PLAY_STATE__("-moz-animation-play-state", "-moz-animation-play-state", ff("running")),

        /** The style property {@code MozAnimationTimingFunction}. */
        MOZ_ANIMATION_TIMING_FUNCTION("MozAnimationTimingFunction", "-moz-animation-timing-function",
                ff("ease")),

        /** The style property {@code -moz-animation-timing-function}. */
        MOZ_ANIMATION_TIMING_FUNCTION__("-moz-animation-timing-function", "-moz-animation-timing-function",
                ff("ease")),

        /** The style property {@code MozAppearance}. */
        MOZ_APPEARANCE("MozAppearance", "-moz-appearance", ff("none")),

        /** The style property {@code -moz-appearance}. */
        MOZ_APPEARANCE__("-moz-appearance", "-moz-appearance", ff("none")),

        /** The style property {@code MozBackfaceVisibility}. */
        MOZ_BACKFACE_VISIBILITY("MozBackfaceVisibility", "-moz-backface-visibility", ff("visible")),

        /** The style property {@code -moz-backface-visibility}. */
        MOZ_BACKFACE_VISIBILITY__("-moz-backface-visibility", "-moz-backface-visibility", ff("visible")),

        /** The style property {@code MozBackgroundClip}. */
        MOZ_BACKGROUND_CLIP("MozBackgroundClip", "-moz-background-clip"),

        /** The style property {@code MozBackgroundOrigin}. */
        MOZ_BACKGROUND_ORIGIN("MozBackgroundOrigin", "-moz-background-origin"),

        /** The style property {@code MozBackgroundSize}. */
        MOZ_BACKGROUND_SIZE("MozBackgroundSize", "-moz-background-size"),

        /** The style property {@code MozBorderBottomColors}. */
        MOZ_BORDER_BOTTOM_COLORS("MozBorderBottomColors", "-moz-border-bottom-colors",
                ffNotIterable("none")),

        /** The style property {@code -moz-border-bottom-colors}. */
        MOZ_BORDER_BOTTOM_COLORS__("-moz-border-bottom-colors", "-moz-border-bottom-colors",
                ffNotIterable("none")),

        /** The style property {@code MozBorderEnd}. */
        MOZ_BORDER_END("MozBorderEnd", "-moz-border-end", ff("")),

        /** The style property {@code -moz-border-end}. */
        MOZ_BORDER_END__("-moz-border-end", "-moz-border-end", ff("")),

        /** The style property {@code MozBorderEndColor}. */
        MOZ_BORDER_END_COLOR("MozBorderEndColor", "-moz-border-end-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code -moz-border-end-color}. */
        MOZ_BORDER_END_COLOR__("-moz-border-end-color", "-moz-border-end-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code MozBorderEndStyle}. */
        MOZ_BORDER_END_STYLE("MozBorderEndStyle", "-moz-border-end-style", ff("none")),

        /** The style property {@code -moz-border-end-style}. */
        MOZ_BORDER_END_STYLE__("-moz-border-end-style", "-moz-border-end-style", ff("none")),

        /** The style property {@code MozBorderEndWidth}. */
        MOZ_BORDER_END_WIDTH("MozBorderEndWidth", "-moz-border-end-width", ff("0px")),

        /** The style property {@code -moz-border-end-width}. */
        MOZ_BORDER_END_WIDTH__("-moz-border-end-width", "-moz-border-end-width", ff("0px")),

        /** The style property {@code MozBorderImage}. */
        MOZ_BORDER_IMAGE("MozBorderImage", "-moz-border-image", ff("")),

        /** The style property {@code -moz-border-image}. */
        MOZ_BORDER_IMAGE__("-moz-border-image", "-moz-border-image", ff("")),

        /** The style property {@code MozBorderLeftColors}. */
        MOZ_BORDER_LEFT_COLORS("MozBorderLeftColors", "-moz-border-left-colors",
                ffNotIterable("none")),

        /** The style property {@code -moz-border-left-colors}. */
        MOZ_BORDER_LEFT_COLORS__("-moz-border-left-colors", "-moz-border-left-colors",
                ffNotIterable("none")),

        /** The style property {@code MozBorderRadius}. */
        MOZ_BORDER_RADIUS("MozBorderRadius", "-moz-border-radius"),

        /** The style property {@code MozBorderRadiusBottomleft}. */
        MOZ_BORDER_RADIUS_BOTTOMLEFT("MozBorderRadiusBottomleft", "-moz-border-radius-bottomleft"),

        /** The style property {@code MozBorderRadiusBottomright}. */
        MOZ_BORDER_RADIUS_BOTTOMRIGHT("MozBorderRadiusBottomright", "-moz-border-radius-bottomright"),

        /** The style property {@code MozBorderRadiusTopleft}. */
        MOZ_BORDER_RADIUS_TOPLEFT("MozBorderRadiusTopleft", "-moz-border-radius-topleft"),

        /** The style property {@code MozBorderRadiusTopright}. */
        MOZ_BORDER_RADIUS_TOPRIGHT("MozBorderRadiusTopright", "-moz-border-radius-topright"),

        /** The style property {@code MozBorderRightColors}. */
        MOZ_BORDER_RIGHT_COLORS("MozBorderRightColors", "-moz-border-right-colors",
                ffNotIterable("none")),

        /** The style property {@code -moz-border-right-colors}. */
        MOZ_BORDER_RIGHT_COLORS__("-moz-border-right-colors", "-moz-border-right-colors",
                ffNotIterable("none")),

        /** The style property {@code MozBorderStart}. */
        MOZ_BORDER_START("MozBorderStart", "-moz-border-start", ff("")),

        /** The style property {@code -moz-border-start}. */
        MOZ_BORDER_START__("-moz-border-start", "-moz-border-start", ff("")),

        /** The style property {@code MozBorderStartColor}. */
        MOZ_BORDER_START_COLOR("MozBorderStartColor", "-moz-border-start-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code -moz-border-start-color}. */
        MOZ_BORDER_START_COLOR__("-moz-border-start-color", "-moz-border-start-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code MozBorderStartStyle}. */
        MOZ_BORDER_START_STYLE("MozBorderStartStyle", "-moz-border-start-style", ff("none")),

        /** The style property {@code -moz-border-start-style}. */
        MOZ_BORDER_START_STYLE__("-moz-border-start-style", "-moz-border-start-style", ff("none")),

        /** The style property {@code MozBorderStartWidth}. */
        MOZ_BORDER_START_WIDTH("MozBorderStartWidth", "-moz-border-start-width", ff("0px")),

        /** The style property {@code -moz-border-start-width}. */
        MOZ_BORDER_START_WIDTH__("-moz-border-start-width", "-moz-border-start-width", ff("0px")),

        /** The style property {@code MozBorderTopColors}. */
        MOZ_BORDER_TOP_COLORS("MozBorderTopColors", "-moz-border-top-colors",
                ffNotIterable("none")),

        /** The style property {@code -moz-border-top-colors}. */
        MOZ_BORDER_TOP_COLORS__("-moz-border-top-colors", "-moz-border-top-colors",
                ffNotIterable("none")),

        /** The style property {@code MozBoxAlign}. */
        MOZ_BOX_ALIGN("MozBoxAlign", "-moz-box-align", ff("stretch")),

        /** The style property {@code -moz-box-align}. */
        MOZ_BOX_ALIGN__("-moz-box-align", "-moz-box-align", ff("stretch")),

        /** The style property {@code MozBoxDirection}. */
        MOZ_BOX_DIRECTION("MozBoxDirection", "-moz-box-direction", ff("normal")),

        /** The style property {@code -moz-box-direction}. */
        MOZ_BOX_DIRECTION__("-moz-box-direction", "-moz-box-direction", ff("normal")),

        /** The style property {@code MozBoxFlex}. */
        MOZ_BOX_FLEX("MozBoxFlex", "-moz-box-flex", ff("0")),

        /** The style property {@code -moz-box-flex}. */
        MOZ_BOX_FLEX__("-moz-box-flex", "-moz-box-flex", ff("0")),

        /** The style property {@code MozBoxOrdinalGroup}. */
        MOZ_BOX_ORDINAL_GROUP("MozBoxOrdinalGroup", "-moz-box-ordinal-group", ff("1")),

        /** The style property {@code -moz-box-ordinal-group}. */
        MOZ_BOX_ORDINAL_GROUP__("-moz-box-ordinal-group", "-moz-box-ordinal-group", ff("1")),

        /** The style property {@code MozBoxOrient}. */
        MOZ_BOX_ORIENT("MozBoxOrient", "-moz-box-orient", ff("horizontal")),

        /** The style property {@code -moz-box-orient}. */
        MOZ_BOX_ORIENT__("-moz-box-orient", "-moz-box-orient", ff("horizontal")),

        /** The style property {@code MozBoxPack}. */
        MOZ_BOX_PACK("MozBoxPack", "-moz-box-pack", ff("start")),

        /** The style property {@code -moz-box-pack}. */
        MOZ_BOX_PACK__("-moz-box-pack", "-moz-box-pack", ff("start")),

        /** The style property {@code MozBoxShadow}. */
        MOZ_BOX_SHADOW("MozBoxShadow", "-moz-box-shadow"),

        /** The style property {@code MozBoxSizing}. */
        MOZ_BOX_SIZING("MozBoxSizing", "-moz-box-sizing", ff("content-box")),

        /** The style property {@code -moz-box-sizing}. */
        MOZ_BOX_SIZING__("-moz-box-sizing", "-moz-box-sizing", ff("content-box")),

        /** The style property {@code MozFloatEdge}. */
        MOZ_FLOAT_EDGE("MozFloatEdge", "-moz-float-edge", ff("content-box")),

        /** The style property {@code -moz-float-edge}. */
        MOZ_FLOAT_EDGE__("-moz-float-edge", "-moz-float-edge", ff("content-box")),

        /** The style property {@code MozFontFeatureSettings}. */
        MOZ_FONT_FEATURE_SETTINGS("MozFontFeatureSettings", "-moz-font-feature-settings",
                ff("normal")),

        /** The style property {@code -moz-font-feature-settings}. */
        MOZ_FONT_FEATURE_SETTINGS__("-moz-font-feature-settings", "-moz-font-feature-settings",
                ff("normal")),

        /** The style property {@code MozFontLanguageOverride}. */
        MOZ_FONT_LANGUAGE_OVERRIDE("MozFontLanguageOverride", "-moz-font-language-override",
                ff("normal")),

        /** The style property {@code -moz-font-language-override}. */
        MOZ_FONT_LANGUAGE_OVERRIDE__("-moz-font-language-override", "-moz-font-language-override",
                ff("normal")),

        /** The style property {@code MozForceBrokenImageIcon}. */
        MOZ_FORCE_BROKEN_IMAGE_ICON("MozForceBrokenImageIcon", "-moz-force-broken-image-icon",
                ff("0")),

        /** The style property {@code -moz-force-broken-image-icon}. */
        MOZ_FORCE_BROKEN_IMAGE_ICON__("-moz-force-broken-image-icon", "-moz-force-broken-image-icon",
                ff("0")),

        /** The style property {@code MozHyphens}. */
        MOZ_HYPHENS("MozHyphens", "-moz-hyphens", ff("manual")),

        /** The style property {@code -moz-hyphens}. */
        MOZ_HYPHENS__("-moz-hyphens", "-moz-hyphens", ff("manual")),

        /** The style property {@code MozImageRegion}. */
        MOZ_IMAGE_REGION("MozImageRegion", "-moz-image-region", ff("auto")),

        /** The style property {@code -moz-image-region}. */
        MOZ_IMAGE_REGION__("-moz-image-region", "-moz-image-region", ff("auto")),

        /** The style property {@code MozMarginEnd}. */
        MOZ_MARGIN_END("MozMarginEnd", "-moz-margin-end", ff("0px")),

        /** The style property {@code -moz-margin-end}. */
        MOZ_MARGIN_END__("-moz-margin-end", "-moz-margin-end", ff("0px")),

        /** The style property {@code MozMarginStart}. */
        MOZ_MARGIN_START("MozMarginStart", "-moz-margin-start", ff("0px")),

        /** The style property {@code -moz-margin-start}. */
        MOZ_MARGIN_START__("-moz-margin-start", "-moz-margin-start", ff("0px")),

        /** The style property {@code MozOpacity}. */
        MOZ_OPACITY("MozOpacity", "-moz-opacity"),

        /** The style property {@code MozOrient}. */
        MOZ_ORIENT("MozOrient", "-moz-orient", ff("inline")),

        /** The style property {@code -moz-orient}. */
        MOZ_ORIENT__("-moz-orient", "-moz-orient", ff("inline")),

        /** The style property {@code MozOutline}. */
        MOZ_OUTLINE("MozOutline", "-moz-outline"),

        /** The style property {@code MozOutlineColor}. */
        MOZ_OUTLINE_COLOR("MozOutlineColor", "-moz-outline-color"),

        /** The style property {@code MozOutlineOffset}. */
        MOZ_OUTLINE_OFFSET("MozOutlineOffset", "-moz-outline-offset"),

        /** The style property {@code MozOutlineRadius}. */
        MOZ_OUTLINE_RADIUS("MozOutlineRadius", "-moz-outline-radius", ff("")),

        /** The style property {@code -moz-outline-radius}. */
        MOZ_OUTLINE_RADIUS__("-moz-outline-radius", "-moz-outline-radius", ff("")),

        /** The style property {@code MozOutlineRadiusBottomleft}. */
        MOZ_OUTLINE_RADIUS_BOTTOMLEFT("MozOutlineRadiusBottomleft",
                "-moz-outline-radius-bottomleft", ff("0px")),

        /** The style property {@code -moz-outline-radius-bottomleft}. */
        MOZ_OUTLINE_RADIUS_BOTTOMLEFT__("-moz-outline-radius-bottomleft",
                "-moz-outline-radius-bottomleft", ff("0px")),

        /** The style property {@code MozOutlineRadiusBottomright}. */
        MOZ_OUTLINE_RADIUS_BOTTOMRIGHT("MozOutlineRadiusBottomright",
                "-moz-outline-radius-bottomright", ff("0px")),

        /** The style property {@code -moz-outline-radius-bottomright}. */
        MOZ_OUTLINE_RADIUS_BOTTOMRIGHT__("-moz-outline-radius-bottomright",
                "-moz-outline-radius-bottomright", ff("0px")),

        /** The style property {@code MozOutlineRadiusTopleft}. */
        MOZ_OUTLINE_RADIUS_TOPLEFT("MozOutlineRadiusTopleft",
                "-moz-outline-radius-topleft", ff("0px")),

        /** The style property {@code -moz-outline-radius-topleft}. */
        MOZ_OUTLINE_RADIUS_TOPLEFT__("-moz-outline-radius-topleft",
                "-moz-outline-radius-topleft", ff("0px")),

        /** The style property {@code MozOutlineRadiusTopright}. */
        MOZ_OUTLINE_RADIUS_TOPRIGHT("MozOutlineRadiusTopright",
                "-moz-outline-radius-topright", ff("0px")),

        /** The style property {@code -moz-outline-radius-topright}. */
        MOZ_OUTLINE_RADIUS_TOPRIGHT__("-moz-outline-radius-topright",
                "-moz-outline-radius-topright", ff("0px")),

        /** The style property {@code MozOutlineStyle}. */
        MOZ_OUTLINE_STYLE("MozOutlineStyle", "-moz-outline-style"),

        /** The style property {@code MozOutlineWidth}. */
        MOZ_OUTLINE_WIDTH("MozOutlineWidth", "-moz-outline-width"),

        /** The style property {@code MozPaddingEnd}. */
        MOZ_PADDING_END("MozPaddingEnd", "-moz-padding-end", ff("0px")),

        /** The style property {@code -moz-padding-end}. */
        MOZ_PADDING_END__("-moz-padding-end", "-moz-padding-end", ff("0px")),

        /** The style property {@code MozPaddingStart}. */
        MOZ_PADDING_START("MozPaddingStart", "-moz-padding-start", ff("0px")),

        /** The style property {@code -moz-padding-start}. */
        MOZ_PADDING_START__("-moz-padding-start", "-moz-padding-start", ff("0px")),

        /** The style property {@code MozPerspective}. */
        MOZ_PERSPECTIVE("MozPerspective", "-moz-perspective", ff("none")),

        /** The style property {@code -moz-perspective}. */
        MOZ_PERSPECTIVE__("-moz-perspective", "-moz-perspective", ff("none")),

        /** The style property {@code MozPerspectiveOrigin}. */
        MOZ_PERSPECTIVE_ORIGIN("MozPerspectiveOrigin",
                "-moz-perspective-origin", ff("622px 171.5px")),

        /** The style property {@code -moz-perspective-origin}. */
        MOZ_PERSPECTIVE_ORIGIN__("-moz-perspective-origin",
                "-moz-perspective-origin", ff("622px 171.5px")),

        /** The style property {@code MozTabSize}. */
        MOZ_TAB_SIZE("MozTabSize", "-moz-tab-size", ff("8")),

        /** The style property {@code -moz-tab-size}. */
        MOZ_TAB_SIZE__("-moz-tab-size", "-moz-tab-size", ff("8")),

        /** The style property {@code MozTextAlignLast}. */
        MOZ_TEXT_ALIGN_LAST("MozTextAlignLast", "-moz-text-align-last",
                ffNotIterable("auto")),

        /** The style property {@code -moz-text-align-last}. */
        MOZ_TEXT_ALIGN_LAST__("-moz-text-align-last", "-moz-text-align-last",
                ffNotIterable("auto")),

        /** The style property {@code MozTextSizeAdjust}. */
        MOZ_TEXT_SIZE_ADJUST("MozTextSizeAdjust", "-moz-text-size-adjust",
                ff("auto")),

        /** The style property {@code -moz-text-size-adjust}. */
        MOZ_TEXT_SIZE_ADJUST__("-moz-text-size-adjust", "-moz-text-size-adjust",
                ff("auto")),

        /** The style property {@code MozTransform}. */
        MOZ_TRANSFORM("MozTransform", "-moz-transform", ff("none")),

        /** The style property {@code -moz-transform}. */
        MOZ_TRANSFORM__("-moz-transform", "-moz-transform", ff("none")),

        /** The style property {@code MozTransformOrigin}. */
        MOZ_TRANSFORM_ORIGIN("MozTransformOrigin", "-moz-transform-origin",
                ff("622px 171.5px")),

        /** The style property {@code -moz-transform-origin}. */
        MOZ_TRANSFORM_ORIGIN__("-moz-transform-origin", "-moz-transform-origin",
                ff("622px 171.5px")),

        /** The style property {@code MozTransformStyle}. */
        MOZ_TRANSFORM_STYLE("MozTransformStyle", "-moz-transform-style",
                ff("flat")),

        /** The style property {@code -moz-transform-style}. */
        MOZ_TRANSFORM_STYLE__("-moz-transform-style", "-moz-transform-style",
                ff("flat")),

        /** The style property {@code MozTransition}. */
        MOZ_TRANSITION("MozTransition", "-moz-transition", ff("")),

        /** The style property {@code -moz-transition}. */
        MOZ_TRANSITION__("-moz-transition", "-moz-transition", ff("")),

        /** The style property {@code MozTransitionDelay}. */
        MOZ_TRANSITION_DELAY("MozTransitionDelay", "-moz-transition-delay",
                ff("0s")),

        /** The style property {@code -moz-transition-delay}. */
        MOZ_TRANSITION_DELAY__("-moz-transition-delay", "-moz-transition-delay",
                ff("0s")),

        /** The style property {@code MozTransitionDuration}. */
        MOZ_TRANSITION_DURATION("MozTransitionDuration", "-moz-transition-duration",
                ff("0s")),

        /** The style property {@code -moz-transition-duration}. */
        MOZ_TRANSITION_DURATION__("-moz-transition-duration", "-moz-transition-duration",
                ff("0s")),

        /** The style property {@code MozTransitionProperty}. */
        MOZ_TRANSITION_PROPERTY("MozTransitionProperty", "-moz-transition-property",
                ff("all")),

        /** The style property {@code -moz-transition-property}. */
        MOZ_TRANSITION_PROPERTY__("-moz-transition-property", "-moz-transition-property",
                ff("all")),

        /** The style property {@code MozTransitionTimingFunction}. */
        MOZ_TRANSITION_TIMING_FUNCTION("MozTransitionTimingFunction",
                "-moz-transition-timing-function", ff("ease")),

        /** The style property {@code -moz-transition-timing-function}. */
        MOZ_TRANSITION_TIMING_FUNCTION__("-moz-transition-timing-function",
                "-moz-transition-timing-function", ff("ease")),

        /** The style property {@code MozUserFocus}. */
        MOZ_USER_FOCUS("MozUserFocus", "-moz-user-focus", ff("none")),

        /** The style property {@code -moz-user-focus}. */
        MOZ_USER_FOCUS__("-moz-user-focus", "-moz-user-focus", ff("none")),

        /** The style property {@code MozUserInput}. */
        MOZ_USER_INPUT("MozUserInput", "-moz-user-input", ff("auto")),

        /** The style property {@code -moz-user-input}. */
        MOZ_USER_INPUT__("-moz-user-input", "-moz-user-input", ff("auto")),

        /** The style property {@code MozUserModify}. */
        MOZ_USER_MODIFY("MozUserModify", "-moz-user-modify", ff("read-only")),

        /** The style property {@code -moz-user-modify}. */
        MOZ_USER_MODIFY__("-moz-user-modify", "-moz-user-modify", ff("read-only")),

        /** The style property {@code MozUserSelect}. */
        MOZ_USER_SELECT("MozUserSelect", "-moz-user-select", ff("auto")),

        /** The style property {@code -moz-user-select}. */
        MOZ_USER_SELECT__("-moz-user-select", "-moz-user-select", ff("auto")),

        /** The style property {@code MozWindowDragging}. */
        MOZ_WINDOW_DRAGGING("MozWindowDragging", "-moz-window-dragging", ff("default")),

        /** The style property {@code -moz-window-dragging}. */
        MOZ_WINDOW_DRAGGING__("-moz-window-dragging", "-moz-window-dragging", ff("default")),

        /** The style property {@code msAnimation}. */
        MS_ANIMATION("msAnimation", "-ms-animation", ie("")),

        /** The style property {@code msAnimationDelay}. */
        MS_ANIMATION_DELAY("msAnimationDelay", "-ms-animation-delay",
                ie("0s")),

        /** The style property {@code msAnimationDirection}. */
        MS_ANIMATION_DIRECTION("msAnimationDirection",
                "-ms-animation-direction", ie("normal")),

        /** The style property {@code msAnimationDuration}. */
        MS_ANIMATION_DURATION("msAnimationDuration",
                "-ms-animation-duration", ie("0s")),

        /** The style property {@code msAnimationFillMode}. */
        MS_ANIMATION_FILL_MODE("msAnimationFillMode",
                "-ms-animation-fill-mode", ie("none")),

        /** The style property {@code msAnimationIterationCount}. */
        MS_ANIMATION_ITERATION_COUNT("msAnimationIterationCount",
                "-ms-animation-iteration-count", ie("1")),

        /** The style property {@code msAnimationName}. */
        MS_ANIMATION_NAME("msAnimationName", "-ms-annimation-name",
                ie("none")),

        /** The style property {@code msAnimationPlayState}. */
        MS_ANIMATION_PLAY_STATE("msAnimationPlayState",
                "-ms-animation-play-state", ie("running")),

        /** The style property {@code msAnimationTimingFunction}. */
        MS_ANIMATION_TIMING_FUNCTION("msAnimationTimingFunction",
                "-ms-animation-timing-function",
                ie("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property {@code msBackfaceVisibility}. */
        MS_BACKFACE_VISIBILITY("msBackfaceVisibility",
                "-ms-backface-visibility", ie("visible")),

        /** The style property {@code msBlockProgression}. */
        MS_BLOCK_PROGRESSION("msBlockProgression", "-ms-block-progression",
                ie("undefined")),

        /** The style property {@code msContentZoomChaining}. */
        MS_CONTENT_ZOOM_CHAINING("msContentZoomChaining",
                "-ms-content-zoom-chaining", ie("none")),

        /** The style property {@code msContentZoomLimit}. */
        MS_CONTENT_ZOOM_LIMIT("msContentZoomLimit", "-ms-content-zoom-limit",
                ie("")),

        /** The style property {@code msContentZoomLimitMax}. */
        MS_CONTENT_ZOOM_LIMIT_MAX("msContentZoomLimitMax", "-ms-content-zoom-limit-max",
                ie("400%")),

        /** The style property {@code msContentZoomLimitMin}. */
        MS_CONTENT_ZOOM_LIMIT_MIN("msContentZoomLimitMin", "-ms-content-zoom-limit-min",
                ie("100%")),

        /** The style property {@code msContentZoomSnap}. */
        MS_CONTENT_ZOOM_SNAP("msContentZoomSnap",
                "-ms-content-zoom-snap", ie("none snapInterval(0%, 100%)")),

        /** The style property {@code msContentZoomSnapPoints}. */
        MS_CONTENT_ZOOM_SNAP_POINTS("msContentZoomSnapPoints",
                "-ms-content-zoom-snap-points", ie("snapInterval(0%, 100%)")),

        /** The style property {@code msContentZoomSnapType}. */
        MS_CONTENT_ZOOM_SNAP_TYPE("msContentZoomSnapType", "-ms-content-zoom-snap-type", ie("none")),

        /** The style property {@code msContentZooming}. */
        MS_CONTENT_ZOOMING("msContentZooming", "-ms-content-zooming", ie("none")),

        /** The style property {@code msFlex}. */
        MS_FLEX("msFlex", "-ms-flex", ie("0 1 auto")),

        /** The style property {@code msFlexAlign}. */
        MS_FLEX_ALIGN("msFlexAlign", "-ms-flex-align", ie("stretch")),

        /** The style property {@code msFlexDirection}. */
        MS_FLEX_DIRECTION("msFlexDirection", "-ms-flex-direction", ie("row")),

        /** The style property {@code msFlexFlow}. */
        MS_FLEX_FLOW("msFlexFlow", "-ms-flex-flow", ie("row nowrap")),

        /** The style property {@code msFlexItemAlign}. */
        MS_FLEX_ITEM_ALIGN("msFlexItemAlign", "-ms-flex-item-align", ie("auto")),

        /** The style property {@code msFlexLinePack}. */
        MS_FLEX_LINE_PACK("msFlexLinePack", "-ms-flex-line-pack", ie("stretch")),

        /** The style property {@code msFlexNegative}. */
        MS_FLEX_NEGATIVE("msFlexNegative", "-ms-flex-negative", ie("1")),

        /** The style property {@code msFlexOrder}. */
        MS_FLEX_ORDER("msFlexOrder", "-ms-flex-order", ie("0")),

        /** The style property {@code msFlexPack}. */
        MS_FLEX_PACK("msFlexPack", "-ms-flex-pack", ie("start")),

        /** The style property {@code msFlexPositive}. */
        MS_FLEX_POSITIVE("msFlexPositive", "-ms-flex-positive", ie("0")),

        /** The style property {@code msFlexPreferredSize}. */
        MS_FLEX_PREFERRED_SIZE("msFlexPreferredSize", "-ms-flex-preferred-size", ie("auto")),

        /** The style property {@code msFlexWrap}. */
        MS_FLEX_WRAP("msFlexWrap", "-ms-flex-wrap", ie("nowrap")),

        /** The style property {@code msFlowFrom}. */
        MS_FLOW_FROM("msFlowFrom", "-ms-flow-from", ie("none")),

        /** The style property {@code msFlowInto}. */
        MS_FLOW_INTO("msFlowInto", "-ms-flow-into", ie("none")),

        /** The style property {@code msFontFeatureSettings}. */
        MS_FONT_FEATURE_SETTINGS("msFontFeatureSettings", "-ms-font-feature-settings", ie("normal")),

        /** The style property {@code msGridColumn}. */
        MS_GRID_COLUMN("msGridColumn", "-ms-grid-column", ie("1")),

        /** The style property {@code msGridColumnAlign}. */
        MS_GRID_COLUMN_ALIGN("msGridColumnAlign", "-ms-grid-column-align", ie("stretch")),

        /** The style property {@code msGridColumnSpan}. */
        MS_GRID_COLUMN_SPAN("msGridColumnSpan", "-ms-grid-column-span", ie("1")),

        /** The style property {@code msGridColumns}. */
        MS_GRID_COLUMNS("msGridColumns", "-ms-grid-columns", ie("none")),

        /** The style property {@code msGridRow}. */
        MS_GRID_ROW("msGridRow", "-ms-grid-row", ie("1")),

        /** The style property {@code msGridRowAlign}. */
        MS_GRID_ROW_ALIGN("msGridRowAlign", "-ms-grid-row-align", ie("stretch")),

        /** The style property {@code msGridRowSpan}. */
        MS_GRID_ROW_SPAN("msGridRowSpan", "-ms-grid-row-span", ie("1")),

        /** The style property {@code msGridRows}. */
        MS_GRID_ROWS("msGridRows", "-ms-grid-rows", ie("none")),

        /** The style property {@code msHighContrastAdjust}. */
        MS_HIGH_CONTRAST_ADJUST("msHighContrastAdjust", "-ms-high-contrast-adjust", ie("auto"), edge("auto")),

        /** The style property {@code msHyphenateLimitChars}. */
        MS_HYPHENATE_LIMIT_CHARS("msHyphenateLimitChars", "-ms-hyphenate-limit-chars", ie("5 2 2")),

        /** The style property {@code msHyphenateLimitLines}. */
        MS_HYPHENATE_LIMIT_LINES("msHyphenateLimitLines", "-ms-hyphenate-limit-lines", ie("no-limit")),

        /** The style property {@code msHyphenateLimitZone}. */
        MS_HYPHENATE_LIMIT_ZONE("msHyphenateLimitZone", "-ms-hyphenate-limit-zone", ie("0px")),

        /** The style property {@code msHyphens}. */
        MS_HYPHENS("msHyphens", "-ms-hyphens", ie("manual")),

        /** The style property {@code msImeAlign}. */
        MS_IME_ALIGN("msImeAlign", "-ms-ime-align", ie("")),

        /** The style property {@code msInterpolationMode}. */
        MS_INTERPOLATION_MODE("msInterpolationMode", "-ms-interpolation-mode", ie("undefined")),

        /** The style property {@code msOverflowStyle}. */
        MS_OVERFLOW_STYLE("msOverflowStyle", "-ms-overflow-style", ie("scrollbar")),

        /** The style property {@code msPerspective}. */
        MS_PERSPECTIVE("msPerspective", "-ms-perspective", ie("none")),

        /** The style property {@code msPerspectiveOrigin}. */
        MS_PERSPECTIVE_ORIGIN("msPerspectiveOrigin", "-ms-perspective-origin", ie("620px 163.2px")),

        /** The style property {@code msScrollChaining}. */
        MS_SCROLL_CHAINING("msScrollChaining", "-ms-scroll-chaining", ie("chained")),

        /** The style property {@code msScrollLimit}. */
        MS_SCROLL_LIMIT("msScrollLimit", "-ms-scroll-limit", ie("")),

        /** The style property {@code msScrollLimitXMax}. */
        MS_SCROLL_LIMIT_X_MAX("msScrollLimitXMax", "-ms-scroll-limit-x-max", ie("0px")),

        /** The style property {@code msScrollLimitXMin}. */
        MS_SCROLL_LIMIT_X_MIN("msScrollLimitXMin", "-ms-scroll-limit-x-min", ie("0px")),

        /** The style property {@code msScrollLimitYMax}. */
        MS_SCROLL_LIMIT_Y_MAX("msScrollLimitYMax", "-ms-scroll-limit-y-max", ie("0px")),

        /** The style property {@code msScrollLimitYMin}. */
        MS_SCROLL_LIMIT_Y_MIN("msScrollLimitYMin", "-ms-scroll-limit-y-min", ie("0px")),

        /** The style property {@code msScrollRails}. */
        MS_SCROLL_RAILS("msScrollRails", "-ms-scroll-rails", ie("railed")),

        /** The style property {@code msScrollSnapPointsX}. */
        MS_SCROLL_SNAP_POINTS_X("msScrollSnapPointsX", "-ms-scroll-snap-points-x", ie("snapInterval(0%, 100%)")),

        /** The style property {@code msScrollSnapPointsY}. */
        MS_SCROLL_SNAP_POINTS_Y("msScrollSnapPointsY", "-ms-scroll-snap-points-y", ie("snapInterval(0%, 100%)")),

        /** The style property {@code msScrollSnapType}. */
        MS_SCROLL_SNAP_TYPE("msScrollSnapType", "-ms-scroll-snap-type", ie("none")),

        /** The style property {@code msScrollSnapX}. */
        MS_SCROLL_SNAP_X("msScrollSnapX", "-ms-scroll-snap-x", ie("none snapInterval(0%, 100%)")),

        /** The style property {@code msScrollSnapY}. */
        MS_SCROLL_SNAP_Y("msScrollSnapY", "-ms-scroll-snap-y", ie("none snapInterval(0%, 100%)")),

        /** The style property {@code msScrollTranslation}. */
        MS_SCROLL_TRANSLATION("msScrollTranslation", "-ms-scroll-translation", ie("none")),

        /** The style property {@code msTextCombineHorizontal}. */
        MS_TEXT_COMBINE_HORIZONTAL("msTextCombineHorizontal", "-ms-text-combine-horizontal", ie("none")),

        /** The style property {@code msTextSizeAdjust}. */
        MS_TEXT_SIZE_ADJUST("msTextSizeAdjust", "-ms-text-size-adjust", ie("auto")),

        /** The style property {@code msTouchAction}. */
        MS_TOUCH_ACTION("msTouchAction", "-ms-touch-action", ie("auto")),

        /** The style property {@code msTouchSelect}. */
        MS_TOUCH_SELECT("msTouchSelect", "-ms-touch-select", ie("")),

        /** The style property {@code msTransform}. */
        MS_TRANSFORM("msTransform", "-ms-transform", ie("none")),

        /** The style property {@code msTransformOrigin}. */
        MS_TRANSFORM_ORIGIN("msTransformOrigin", "-ms-transform-origin", ie("620px 163.2px")),

        /** The style property {@code msTransformStyle}. */
        MS_TRANSFORM_STYLE("msTransformStyle", "-ms-transform-style", ie("flat")),

        /** The style property {@code msTransition}. */
        MS_TRANSITION("msTransition", "-ms-transition", ie("")),

        /** The style property {@code msTransitionDelay}. */
        MS_TRANSITION_DELAY("msTransitionDelay", "-ms-transition-delay", ie("0s")),

        /** The style property {@code msTransitionDuration}. */
        MS_TRANSITION_DURATION("msTransitionDuration",
                "-ms-transition-duration", ie("0s")),

        /** The style property {@code msTransitionProperty}. */
        MS_TRANSITION_PROPERTY("msTransitionProperty",
                "-ms-transition-property", ie("all")),

        /** The style property {@code msTransitionTimingFunction}. */
        MS_TRANSITION_TIMING_FUNCTION("msTransitionTimingFunction",
                "-ms-transition-timing-function",
                ie("cubic-bezier(0.25, 0.1, 0.25, 1)")),

        /** The style property {@code msUserSelect}. */
        MS_USER_SELECT("msUserSelect", "-ms-user-select", ie("text")),

        /** The style property {@code msWrapFlow}. */
        MS_WRAP_FLOW("msWrapFlow", "-ms-wrap-flow", ie("auto")),

        /** The style property {@code msWrapMargin}. */
        MS_WRAP_MARGIN("msWrapMargin", "-ms-wrap-margin", ie("auto")),

        /** The style property {@code msWrapThrough}. */
        MS_WRAP_THROUGH("msWrapThrough", "-ms-wrap-through", ie("wrap")),

        /** The style property {@code objectFit}. */
        OBJECT_FIT("objectFit", "object-fit", ff("fill"), chromeAndEdge("fill")),

        /** The style property {@code object-fit}. */
        OBJECT_FIT_("object-fit", "object-fit", ff("fill")),

        /** The style property {@code objectPosition}. */
        OBJECT_POSITION("objectPosition", "object-position", ff("50% 50%"), chromeAndEdge("50% 50%")),

        /** The style property {@code object-position}. */
        OBJECT_POSITION_("object-position", "object-position", ff("50% 50%")),

        /** The style property {@code offset}. */
        OFFSET("offset", "offset", chromeAndEdge("none 0px auto 0deg"), ff("")),

        /** The style property {@code offsetAnchor}. */
        OFFSET_ANCHOR("offsetAnchor", "offset-anchor", ff("auto")),

        /** The style property {@code offset-anchor}. */
        OFFSET_ANCHOR_("offset-anchor", "offset-anchor", ff("auto")),

        /** The style property {@code offsetDistance}. */
        OFFSET_DISTANCE("offsetDistance", "offset-distance", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code offset-distance}. */
        OFFSET_DISTANCE_("offset-distance", "offset-distance", ff("0px")),

        /** The style property {@code offsetPath}. */
        OFFSET_PATH("offsetPath", "offset-path", chromeAndEdge("none"), ff("none")),

        /** The style property {@code offset-path}. */
        OFFSET_PATH_("offset-path", "offset-path", ff("none")),

        /** The style property {@code offsetRotate}. */
        OFFSET_ROTATE("offsetRotate", "offset-rotate", chromeAndEdge("auto 0deg"), ff("auto")),

        /** The style property {@code offset-rotate}. */
        OFFSET_ROTATE_("offset-rotate", "offset-rotate", ff("auto")),

        /** The style property {@code opacity}. */
        OPACITY("opacity", "opacity", chromeAndEdge("1"), ff(""), ie("")),

        /** The style property {@code order}. */
        ORDER("order", "order", ff("0"), ie("0"), chromeAndEdge("0")),

        /** The style property {@code orientation}. */
        ORIENTATION("orientation", "orientation", chromeAndEdge("")),

        /** The style property {@code orphans}. */
        ORPHANS("orphans", "orphans", ie("2"), chromeAndEdge("2")),

        /** The style property {@code outline}. */
        OUTLINE("outline", "outline", chromeAndEdge("rgb(0, 0, 0) none 0px"), ff(""), ie("")),

        /** The style property {@code outlineColor}. */
        OUTLINE_COLOR("outlineColor", "outline-color", ie("transparent"), chromeAndEdge("rgb(0, 0, 0)"),
                ff("rgb(0, 0, 0)")),

        /** The style property {@code outline-color}. */
        OUTLINE_COLOR_("outline-color", "outline-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code outlineOffset}. */
        OUTLINE_OFFSET("outlineOffset", "outline-offset", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code outline-offset}. */
        OUTLINE_OFFSET_("outline-offset", "outline-offset", ff("0px")),

        /** The style property {@code outlineStyle}. */
        OUTLINE_STYLE("outlineStyle", "outline-style", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code outline-style}. */
        OUTLINE_STYLE_("outline-style", "outline-style", ff("none")),

        /** The style property {@code outlineWidth}. */
        OUTLINE_WIDTH("outlineWidth", "outline-width", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code outline-width}. */
        OUTLINE_WIDTH_("outline-width", "outline-width", ff("0px")),

        /** The style property {@code overflow}. */
        OVERFLOW("overflow", "overflow", chromeAndEdge("visible"), ff("visible"), ie("visible")),

        /** The style property {@code overflowAnchor}. */
        OVERFLOW_ANCHOR("overflowAnchor", "overflow-anchor", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code overflow-anchor}. */
        OVERFLOW_ANCHOR_("overflow-anchor", "overflow-anchor", ff("auto")),

        /** The style property {@code overflowBlock}. */
        OVERFLOW_BLOCK("overflowBlock", "overflow-block", ff("visible")),

        /** The style property {@code overflow-block}. */
        OVERFLOW_BLOCK_("overflow-block", "overflow-block", ff("visible")),

        /** The style property {@code overflowInline}. */
        OVERFLOW_INLINE("overflowInline", "overflow-inline", ff("visible")),

        /** The style property {@code overflow-inline}. */
        OVERFLOW_INLINE_("overflow-inline", "overflow-inline", ff("visible")),

        /** The style property {@code overflowWrap}. */
        OVERFLOW_WRAP("overflowWrap", "overflow-wrap", chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code overflow-wrap}. */
        OVERFLOW_WRAP_("overflow-wrap", "overflow-wrap", ff("normal")),

        /** The style property {@code overflowX}. */
        OVERFLOW_X("overflowX", "overflow-x", chromeAndEdge("visible"), ff("visible"), ie("visible")),

        /** The style property {@code overflow-x}. */
        OVERFLOW_X_("overflow-x", "overflow-x", ff("visible")),

        /** The style property {@code overflowY}. */
        OVERFLOW_Y("overflowY", "overflow-y", chromeAndEdge("visible"), ff("visible"), ie("visible")),

        /** The style property {@code overflow-y}. */
        OVERFLOW_Y_("overflow-y", "overflow-y", ff("visible")),

        /** The style property {@code overscrollBehavior}. */
        OVERSCROLL_BEHAVIOR("overscrollBehavior", "overscroll-behavior", chromeAndEdge("auto"), ff("")),

        /** The style property {@code overscroll-behavior}. */
        OVERSCROLL_BEHAVIOR_("overscroll-behavior", "overscroll-behavior", ff("")),

        /** The style property {@code overscrollBehaviorBlock}. */
        OVERSCROLL_BEHAVIOR_BLOCK("overscrollBehaviorBlock", "overscroll-behavior-block", chromeAndEdge("auto"),
                ff("auto")),

        /** The style property {@code overscroll-behavior-block}. */
        OVERSCROLL_BEHAVIOR_BLOCK_("overscroll-behavior-block", "overscroll-behavior-block", ff("auto")),

        /** The style property {@code overscrollBehaviorInline}. */
        OVERSCROLL_BEHAVIOR_INLINE("overscrollBehaviorInline", "overscroll-behavior-inline", chromeAndEdge("auto"),
                ff("auto")),

        /** The style property {@code overscroll-behavior-inline}. */
        OVERSCROLL_BEHAVIOR_INLINE_("overscroll-behavior-inline", "overscroll-behavior-inline", ff("auto")),

        /** The style property {@code overscrollBehaviorX}. */
        OVERSCROLL_BEHAVIOR_X("overscrollBehaviorX", "overscroll-behavior-x", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code overscroll-behavior-x}. */
        OVERSCROLL_BEHAVIOR_X_("overscroll-behavior-x", "overscroll-behavior-x", ff("auto")),

        /** The style property {@code overscrollBehaviorY}. */
        OVERSCROLL_BEHAVIOR_Y("overscrollBehaviorY", "overscroll-behavior-y", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code overscroll-behavior-y}. */
        OVERSCROLL_BEHAVIOR_Y_("overscroll-behavior-y", "overscroll-behavior-y", ff("auto")),

        /** The style property {@code padding}. */
        PADDING("padding", "padding", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code paddingBlock}. */
        PADDING_BLOCK("paddingBlock", "padding-block", chromeAndEdge("0px"), ff("")),

        /** The style property {@code padding-block}. */
        PADDING_BLOCK_("padding-block", "padding-block", ff("")),

        /** The style property {@code paddingBlockEnd}. */
        PADDING_BLOCK_END("paddingBlockEnd", "padding-block-end", chromeAndEdge("0px"),
                ff("0px")),

        /** The style property {@code padding-block-end}. */
        PADDING_BLOCK_END_("padding-block-end", "padding-block-end", ff("0px")),

        /** The style property {@code paddingBlockStart}. */
        PADDING_BLOCK_START("paddingBlockStart", "padding-block-start", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code padding-block-start}. */
        PADDING_BLOCK_START_("padding-block-start", "padding-block-start", ff("0px")),

        /** The style property {@code paddingBottom}. */
        PADDING_BOTTOM("paddingBottom", "padding-bottom", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code padding-bottom}. */
        PADDING_BOTTOM_("padding-bottom", "padding-bottom", ff("0px")),

        /** The style property {@code paddingInline}. */
        PADDING_INLINE("paddingInline", "padding-inline", chromeAndEdge("0px"), ff("")),

        /** The style property {@code padding-inline}. */
        PADDING_INLINE_("padding-inline", "padding-inline", ff("")),

        /** The style property {@code paddingInlineEnd}. */
        PADDING_INLINE_END("paddingInlineEnd", "padding-inline-end", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code padding-inline-end}. */
        PADDING_INLINE_END_("padding-inline-end", "padding-inline-end", ff("0px")),

        /** The style property {@code paddingInlineStart}. */
        PADDING_INLINE_START("paddingInlineStart", "padding-inline-start", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code padding-inline-start}. */
        PADDING_INLINE_START_("padding-inline-start", "padding-inline-start", ff("0px")),

        /** The style property {@code paddingLeft}. */
        PADDING_LEFT("paddingLeft", "padding-left", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code padding-left}. */
        PADDING_LEFT_("padding-left", "padding-left", ff("0px")),

        /** The style property {@code paddingRight}. */
        PADDING_RIGHT("paddingRight", "padding-right", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code padding-right}. */
        PADDING_RIGHT_("padding-right", "padding-right", ff("0px")),

        /** The style property {@code paddingTop}. */
        PADDING_TOP("paddingTop", "padding-top", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code padding-top}. */
        PADDING_TOP_("padding-top", "padding-top", ff("0px")),

        /** The style property {@code page}. */
        PAGE("page", "page", chromeAndEdge("auto")),

        /** The style property {@code pageBreakAfter}. */
        PAGE_BREAK_AFTER("pageBreakAfter", "page-break-after", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code page-break-after}. */
        PAGE_BREAK_AFTER_("page-break-after", "page-break-after", ff("auto")),

        /** The style property {@code pageBreakBefore}. */
        PAGE_BREAK_BEFORE("pageBreakBefore", "page-break-before", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code page-break-before}. */
        PAGE_BREAK_BEFORE_("page-break-before", "page-break-before", ff("auto")),

        /** The style property {@code pageBreakInside}. */
        PAGE_BREAK_INSIDE("pageBreakInside", "page-break-inside", ff("auto"), ie("auto"), chromeAndEdge("auto")),

        /** The style property {@code page-break-inside}. */
        PAGE_BREAK_INSIDE_("page-break-inside", "page-break-inside", ff("auto")),

        /** The style property {@code pageOrientation}. */
        PAGE_ORIENTATION("pageOrientation", "page-orientation", chromeAndEdge("")),

        /** The style property {@code paintOrder}. */
        PAINT_ORDER("paintOrder", "paint-order", ff("normal"), chromeAndEdge("normal")),

        /** The style property {@code paint-order}. */
        PAINT_ORDER_("paint-order", "paint-order", ff("normal")),

        /** The style property {@code pause}. */
        PAUSE("pause", "pause"),

        /** The style property {@code pauseAfter}. */
        PAUSE_AFTER("pauseAfter", "pause-after"),

        /** The style property {@code pauseBefore}. */
        PAUSE_BEFORE("pauseBefore", "pause-before"),

        /** The style property {@code perspective}. */
        PERSPECTIVE("perspective", "perspective", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code perspectiveOrigin}. */
        PERSPECTIVE_ORIGIN("perspectiveOrigin", "perspective-origin",
                ff("622px 171.5px"), ie("620px 163.2px"), chromeAndEdge("620px 161px")),

        /** The style property {@code perspective-origin}. */
        PERSPECTIVE_ORIGIN_("perspective-origin", "perspective-origin",
                ff("622px 171.5px")),

        /** The style property {@code pitch}. */
        PITCH("pitch", "pitch"),

        /** The style property {@code pitchRange}. */
        PITCH_RANGE("pitchRange", "pitch-range"),

        /** The style property {@code pixelBottom}. */
        PIXEL_BOTTOM("pixelBottom", "pixel-bottom", ie("")),

        /** The style property {@code pixelHeight}. */
        PIXEL_HEIGHT("pixelHeight", "pixel-height", ie("")),

        /** The style property {@code pixelLeft}. */
        PIXEL_LEFT("pixelLeft", "pixel-left", ie("")),

        /** The style property {@code pixelRight}. */
        PIXEL_RIGHT("pixelRight", "pixel-right", ie("")),

        /** The style property {@code pixelTop}. */
        PIXEL_TOP("pixelTop", "pixel-top", ie("")),

        /** The style property {@code pixelWidth}. */
        PIXEL_WIDTH("pixelWidth", "pixel-width", ie("")),

        /** The style property {@code placeContent}. */
        PLACE_CONTENT("placeContent", "place-content", chromeAndEdge("normal"), ff("")),

        /** The style property {@code place-content}. */
        PLACE_CONTENT_("place-content", "place-content", ff("")),

        /** The style property {@code placeItems}. */
        PLACE_ITEMS("placeItems", "place-items", chromeAndEdge("normal"), ff("")),

        /** The style property {@code place-items}. */
        PLACE_ITEMS_("place-items", "place-items", ff("")),

        /** The style property {@code placeSelf}. */
        PLACE_SELF("placeSelf", "place-self", chromeAndEdge("auto"), ff("")),

        /** The style property {@code place-self}. */
        PLACE_SELF_("place-self", "place-self", ff("")),

        /** The style property {@code pointerEvents}. */
        POINTER_EVENTS("pointerEvents", "pointer-events", ie("visiblePainted"), chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code pointer-events}. */
        POINTER_EVENTS_("pointer-events", "pointer-events", ff("auto")),

        /** The style property {@code posBottom}. */
        POS_BOTTOM("posBottom", "pos-bottom", ie("")),

        /** The style property {@code posHeight}. */
        POS_HEIGHT("posHeight", "pos-height", ie("")),

        /** The style property {@code posLeft}. */
        POS_LEFT("posLeft", "pos-left", ie("")),

        /** The style property {@code posRight}. */
        POS_RIGHT("posRight", "pos-right", ie("")),

        /** The style property {@code posTop}. */
        POS_TOP("posTop", "pos-top", ie("")),

        /** The style property {@code posWidth}. */
        POS_WIDTH("posWidth", "pos-width", ie("")),

        /** The style property {@code position}. */
        POSITION("position", "position", chromeAndEdge("static"), ff("static"), ie("static")),

        /** The style property {@code quotes}. */
        QUOTES("quotes", "quotes", ff("auto"), ie(""), chromeAndEdge("auto")),

        /** The style property {@code r}. */
        R("r", "r", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code resize}. */
        RESIZE("resize", "resize", ff("none"), chromeAndEdge("none")),

        /** The style property {@code richness}. */
        RICHNESS("richness", "richness"),

        /** The style property {@code right}. */
        RIGHT("right", "right", chromeAndEdge("auto"), ff(""), ie("")),

        /** The style property {@code rotate}. */
        ROTATE("rotate", "rotate", ff("none")),

        /** The style property {@code rowGap}. */
        ROW_GAP("rowGap", "row-gap", chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code row-gap}. */
        ROW_GAP_("row-gap", "row-gap", ff("normal")),

        /** The style property {@code rubyAlign}. */
        RUBY_ALIGN("rubyAlign", "ruby-align", ff("space-around"), ie("")),

        /** The style property {@code ruby-align}. */
        RUBY_ALIGN_("ruby-align", "ruby-align", ff("space-around")),

        /** The style property {@code rubyOverhang}. */
        RUBY_OVERHANG("rubyOverhang", "ruby-overhang", ie("auto")),

        /** The style property {@code rubyPosition}. */
        RUBY_POSITION("rubyPosition", "ruby-position", chromeAndEdge("over"), ie("above"), ff("over")),

        /** The style property {@code ruby-position}. */
        RUBY_POSITION_("ruby-position", "ruby-position", ff("over")),

        /** The style property {@code rx}. */
        RX("rx", "rx", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code ry}. */
        RY("ry", "ry", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scale}. */
        SCALE("scale", "scale", ff("none")),

        /** The style property {@code scrollBehavior}. */
        SCROLL_BEHAVIOR("scrollBehavior", "scroll-behavior", ff("auto"), chromeAndEdge("auto")),

        /** The style property {@code scroll-behavior}. */
        SCROLL_BEHAVIOR_("scroll-behavior", "scroll-behavior", ff("auto")),

        /** The style property {@code scrollMargin}. */
        SCROLL_MARGIN("scrollMargin", "scroll-margin", chromeAndEdge("0px"), ff("")),

        /** The style property {@code scroll-margin}. */
        SCROLL_MARGIN_("scroll-margin", "scroll-margin", ff("")),

        /** The style property {@code scrollMarginBlock}. */
        SCROLL_MARGIN_BLOCK("scrollMarginBlock", "scroll-margin-block", chromeAndEdge("0px"), ff("")),

        /** The style property {@code scroll-margin-block}. */
        SCROLL_MARGIN_BLOCK_("scroll-margin-block", "scroll-margin-block", ff("")),

        /** The style property {@code scrollMarginBlockEnd}. */
        SCROLL_MARGIN_BLOCK_END("scrollMarginBlockEnd", "scroll-margin-block-end",
                chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code scroll-margin-block-end}. */
        SCROLL_MARGIN_BLOCK_END_("scroll-margin-block-end", "scroll-margin-block-end", ff("0px")),

        /** The style property {@code scrollMarginBlockStart}. */
        SCROLL_MARGIN_BLOCK_START("scrollMarginBlockStart", "scroll-margin-block-start",
                chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code scroll-margin-block-start}. */
        SCROLL_MARGIN_BLOCK_START_("scroll-margin-block-start", "scroll-margin-block-start", ff("0px")),

        /** The style property {@code scrollMarginBottom}. */
        SCROLL_MARGIN_BOTTOM("scrollMarginBottom", "scroll-margin-bottom", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code scroll-margin-bottom}. */
        SCROLL_MARGIN_BOTTOM_("scroll-margin-bottom", "scroll-margin-bottom", ff("0px")),

        /** The style property {@code scrollMarginInline}. */
        SCROLL_MARGIN_INLINE("scrollMarginInline", "scroll-margin-inline", chromeAndEdge("0px"), ff("")),

        /** The style property {@code scroll-margin-inline}. */
        SCROLL_MARGIN_INLINE_("scroll-margin-inline", "scroll-margin-inline", ff("")),

        /** The style property {@code scrollMarginInlineEnd}. */
        SCROLL_MARGIN_INLINE_END("scrollMarginInlineEnd", "scroll-margin-inline-end",
                chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code scroll-margin-inline-end}. */
        SCROLL_MARGIN_INLINE_END_("scroll-margin-inline-end", "scroll-margin-inline-end", ff("0px")),

        /** The style property {@code scrollMarginInlineStart}. */
        SCROLL_MARGIN_INLINE_START("scrollMarginInlineStart", "scroll-margin-inline-start",
                chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code scroll-margin-inline-start}. */
        SCROLL_MARGIN_INLINE_START_("scroll-margin-inline-start", "scroll-margin-inline-start", ff("0px")),

        /** The style property {@code scrollMarginLeft}. */
        SCROLL_MARGIN_LEFT("scrollMarginLeft", "scroll-margin-left", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code scroll-margin-left}. */
        SCROLL_MARGIN_LEFT_("scroll-margin-left", "scroll-margin-left", ff("0px")),

        /** The style property {@code scrollMarginRight}. */
        SCROLL_MARGIN_RIGHT("scrollMarginRight", "scroll-margin-right", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code scroll-margin-right}. */
        SCROLL_MARGIN_RIGHT_("scroll-margin-right", "scroll-margin-right", ff("0px")),

        /** The style property {@code scrollMarginTop}. */
        SCROLL_MARGIN_TOP("scrollMarginTop", "scroll-margin-top", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code scroll-margin-top}. */
        SCROLL_MARGIN_TOP_("scroll-margin-top", "scroll-margin-top", ff("0px")),

        /** The style property {@code scrollPadding}. */
        SCROLL_PADDING("scrollPadding", "scroll-padding", chromeAndEdge("auto"), ff("")),

        /** The style property {@code scroll-padding}. */
        SCROLL_PADDING_("scroll-padding", "scroll-padding", ff("")),

        /** The style property {@code scrollPaddingBlock}. */
        SCROLL_PADDING_BLOCK("scrollPaddingBlock", "scroll-padding-block",
                chromeAndEdge("auto"), ff("")),

        /** The style property {@code scroll-padding-block}. */
        SCROLL_PADDING_BLOCK_("scroll-padding-block", "scroll-padding-block", ff("")),

        /** The style property {@code scrollPaddingBlockEnd}. */
        SCROLL_PADDING_BLOCK_END("scrollPaddingBlockEnd", "scroll-padding-block-end",
                chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scroll-padding-block-end}. */
        SCROLL_PADDING_BLOCK_END_("scroll-padding-block-end", "scroll-padding-block-end", ff("auto")),

        /** The style property {@code scrollPaddingBlockStart}. */
        SCROLL_PADDING_BLOCK_START("scrollPaddingBlockStart", "scroll-padding-block-start",
                chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scroll-padding-block-start}. */
        SCROLL_PADDING_BLOCK_START_("scroll-padding-block-start", "scroll-padding-block-start", ff("auto")),

        /** The style property {@code scrollPaddingBottom}. */
        SCROLL_PADDING_BOTTOM("scrollPaddingBottom", "scroll-padding-bottom", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scroll-padding-bottom}. */
        SCROLL_PADDING_BOTTOM_("scroll-padding-bottom", "scroll-padding-bottom", ff("auto")),

        /** The style property {@code scrollPaddingInline}. */
        SCROLL_PADDING_INLINE("scrollPaddingInline", "scroll-padding-inline", chromeAndEdge("auto"), ff("")),

        /** The style property {@code scroll-padding-inline}. */
        SCROLL_PADDING_INLINE_("scroll-padding-inline", "scroll-padding-inline", ff("")),

        /** The style property {@code scrollPaddingInlineEnd}. */
        SCROLL_PADDING_INLINE_END("scrollPaddingInlineEnd", "scroll-padding-inline-end",
                chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scroll-padding-inline-end}. */
        SCROLL_PADDING_INLINE_END_("scroll-padding-inline-end", "scroll-padding-inline-end", ff("auto")),

        /** The style property {@code scrollPaddingInlineStart}. */
        SCROLL_PADDING_INLINE_START("scrollPaddingInlineStart", "scroll-padding-inline-start",
                chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scroll-padding-inline-start}. */
        SCROLL_PADDING_INLINE_START_("scroll-padding-inline-start", "scroll-padding-inline-start", ff("auto")),

        /** The style property {@code scrollPaddingLeft}. */
        SCROLL_PADDING_LEFT("scrollPaddingLeft", "scroll-padding-left", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scroll-padding-left}. */
        SCROLL_PADDING_LEFT_("scroll-padding-left", "scroll-padding-left", ff("auto")),

        /** The style property {@code scrollPaddingRight}. */
        SCROLL_PADDING_RIGHT("scrollPaddingRight", "scroll-padding-right", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scroll-padding-right}. */
        SCROLL_PADDING_RIGHT_("scroll-padding-right", "scroll-padding-right", ff("auto")),

        /** The style property {@code scrollPaddingTop}. */
        SCROLL_PADDING_TOP("scrollPaddingTop", "scroll-padding-top", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code scroll-padding-top}. */
        SCROLL_PADDING_TOP_("scroll-padding-top", "scroll-padding-top", ff("auto")),

        /** The style property {@code scrollSnapAlign}. */
        SCROLL_SNAP_ALIGN("scrollSnapAlign", "scroll-snap-align", chromeAndEdge("none"), ff("none")),

        /** The style property {@code scroll-snap-align}. */
        SCROLL_SNAP_ALIGN_("scroll-snap-align", "scroll-snap-align", ff("none")),

        /** The style property {@code scrollSnapStop}. */
        SCROLL_SNAP_STOP("scrollSnapStop", "scroll-snap-stop", chromeAndEdge("normal")),

        /** The style property {@code scrollSnapType}. */
        SCROLL_SNAP_TYPE("scrollSnapType", "scroll-snap-type", chromeAndEdge("none"), ff("none")),

        /** The style property {@code scroll-snap-type}. */
        SCROLL_SNAP_TYPE_("scroll-snap-type", "scroll-snap-type", ff("none")),

        /** The style property {@code scrollbar3dLightColor}. */
        SCROLLBAR_3DLIGHT_COLOR("scrollbar3dLightColor", "scrollbar-3dlight-color", ie("undefined")),

        /** The style property {@code scrollbarArrowColor}. */
        SCROLLBAR_ARROW_COLOR("scrollbarArrowColor", "scrollbar-arrow-color", ie("undefined")),

        /** The style property {@code scrollbarBaseColor}. */
        SCROLLBAR_BASE_COLOR("scrollbarBaseColor", "scrollbar-base-color", ie("undefined")),

        /** The style property {@code scrollbarColor}. */
        SCROLLBAR_COLOR("scrollbarColor", "scrollbar-color", ff("auto")),

        /** The style property {@code scrollbar-color}. */
        SCROLLBAR_COLOR_("scrollbar-color", "scrollbar-color", ff("auto")),

        /** The style property {@code scrollbarDarkShadowColor}. */
        SCROLLBAR_DARKSHADOW_COLOR("scrollbarDarkShadowColor", "scrollbar-darkshadow-color", ie("undefined")),

        /** The style property {@code scrollbarFaceColor}. */
        SCROLLBAR_FACE_COLOR("scrollbarFaceColor", "scrollbar-face-color", ie("undefined")),

        /** The style property {@code scrollbarHighlightColor}. */
        SCROLLBAR_HIGHLIGHT_COLOR("scrollbarHighlightColor", "scrollbar-highlight-color", ie("undefined")),

        /** The style property {@code scrollbarShadowColor}. */
        SCROLLBAR_SHADOW_COLOR("scrollbarShadowColor", "scrollbar-shadow-color", ie("undefined")),

        /** The style property {@code scrollbarTrackColor}. */
        SCROLLBAR_TRACK_COLOR("scrollbarTrackColor", "scrollbar-track-color", ie("undefined")),

        /** The style property {@code scrollbarWidth}. */
        SCROLLBAR_WIDTH("scrollbarWidth", "scrollbar-width", ff("auto")),

        /** The style property {@code scrollbar-width}. */
        SCROLLBAR_WIDTH_("scrollbar-width", "scrollbar-width", ff("auto")),

        /** The style property {@code shapeImageThreshold}. */
        SHAPE_IMAGE_THRESHOLD("shapeImageThreshold", "shape-image-threshold", chromeAndEdge("0"), ff("0")),

        /** The style property {@code shape-image-threshold}. */
        SHAPE_IMAGE_THRESHOLD_("shape-image-threshold", "shape-image-threshold", ff("0")),

        /** The style property {@code shapeMargin}. */
        SHAPE_MARGIN("shapeMargin", "shape-margin", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code shape-margin}. */
        SHAPE_MARGIN_("shape-margin", "shape-margin", ff("0px")),

        /** The style property {@code shapeOutside}. */
        SHAPE_OUTSIDE("shapeOutside", "shape-outside", chromeAndEdge("none"), ff("none")),

        /** The style property {@code shape-outside}. */
        SHAPE_OUTSIDE_("shape-outside", "shape-outside", ff("none")),

        /** The style property {@code shapeRendering}. */
        SHAPE_RENDERING("shapeRendering", "shape-rendering", ff("auto"), chromeAndEdge("auto")),

        /** The style property {@code shape-rendering}. */
        SHAPE_RENDERING_("shape-rendering", "shape-rendering", ff("auto")),

        /** The style property {@code size}. */
        SIZE("size", "size", chromeAndEdge("")),

        /** The style property {@code speak}. */
        SPEAK("speak", "speak", chromeAndEdge("normal")),

        /** The style property {@code speakHeader}. */
        SPEAK_HEADER("speakHeader", "speak-header"),

        /** The style property {@code speakNumeral}. */
        SPEAK_NUMERAL("speakNumeral", "speak-numeral"),

        /** The style property {@code speakPunctuation}. */
        SPEAK_PUNCTUATION("speakPunctuation", "speak-punctuation"),

        /** The style property {@code speechRate}. */
        SPEECH_RATE("speechRate", "speech-rate"),

        /** The style property {@code src}. */
        SRC("src", "src", chromeAndEdge("")),

        /** The style property {@code stopColor}. */
        STOP_COLOR("stopColor", "stop-color", ff("rgb(0, 0, 0)"), ie(""), chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code stop-color}. */
        STOP_COLOR_("stop-color", "stop-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code stopOpacity}. */
        STOP_OPACITY("stopOpacity", "stop-opacity", ff("1"), ie("1"), chromeAndEdge("1")),

        /** The style property {@code stop-opacity}. */
        STOP_OPACITY_("stop-opacity", "stop-opacity", ff("1")),

        /** The style property {@code stress}. */
        STRESS("stress", "stress"),

        /** The style property {@code stroke}. */
        STROKE("stroke", "stroke", ff("none"), ie(""), chromeAndEdge("none")),

        /** The style property {@code strokeDasharray}. */
        STROKE_DASHARRAY("strokeDasharray", "stroke-dasharray", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code stroke-dasharray}. */
        STROKE_DASHARRAY_("stroke-dasharray", "stroke-dasharray", ff("none")),

        /** The style property {@code strokeDashoffset}. */
        STROKE_DASHOFFSET("strokeDashoffset", "stroke-dashoffset", ff("0px"), ie("0px"), chromeAndEdge("0px")),

        /** The style property {@code stroke-dashoffset}. */
        STROKE_DASHOFFSET_("stroke-dashoffset", "stroke-dashoffset", ff("0px")),

        /** The style property {@code strokeLinecap}. */
        STROKE_LINECAP("strokeLinecap", "stroke-linecap", ff("butt"), ie("butt"), chromeAndEdge("butt")),

        /** The style property {@code stroke-linecap}. */
        STROKE_LINECAP_("stroke-linecap", "stroke-linecap", ff("butt")),

        /** The style property {@code strokeLinejoin}. */
        STROKE_LINEJOIN("strokeLinejoin", "stroke-linejoin", ff("miter"), ie("miter"), chromeAndEdge("miter")),

        /** The style property {@code stroke-linejoin}. */
        STROKE_LINEJOIN_("stroke-linejoin", "stroke-linejoin", ff("miter")),

        /** The style property {@code strokeMiterlimit}. */
        STROKE_MITERLIMIT("strokeMiterlimit", "stroke-miterlimit", ff("4"), ie("4"), chromeAndEdge("4")),

        /** The style property {@code stroke-miterlimit}. */
        STROKE_MITERLIMIT_("stroke-miterlimit", "stroke-miterlimit", ff("4")),

        /** The style property {@code strokeOpacity}. */
        STROKE_OPACITY("strokeOpacity", "stroke-opacity", ff("1"), ie("1"), chromeAndEdge("1")),

        /** The style property {@code stroke-opacity}. */
        STROKE_OPACITY_("stroke-opacity", "stroke-opacity", ff("1")),

        /** The style property {@code strokeWidth}. */
        STROKE_WIDTH("strokeWidth", "stroke-width", ff("1px"), ie("0.01px"), chromeAndEdge("1px")),

        /** The style property {@code stroke-width}. */
        STROKE_WIDTH_("stroke-width", "stroke-width", ff("1px")),

        /** The style property {@code styleFloat}. */
        STYLE_FLOAT("styleFloat", "style-float", ie("undefined")),

        /** The style property {@code syntax}. */
        SYNTAX("syntax", "syntax", chromeAndEdge("")),

        /** The style property {@code tabSize}. */
        TAB_SIZE("tabSize", "tab-size", chromeAndEdge("8")),

        /** The style property {@code tableLayout}. */
        TABLE_LAYOUT("tableLayout", "table-layout", chromeAndEdge("auto"), ff("auto"), ie("auto")),

        /** The style property {@code table-layout}. */
        TABLE_LAYOUT_("table-layout", "table-layout", ff("auto")),

        /** The style property {@code textAlign}. */
        TEXT_ALIGN("textAlign", "text-align", ie("left"), chromeAndEdge("start"), ff("start")),

        /** The style property {@code text-align}. */
        TEXT_ALIGN_("text-align", "text-align", ff("start")),

        /** The style property {@code textAlignLast}. */
        TEXT_ALIGN_LAST("textAlignLast", "text-align-last", ie("auto"), chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code text-align-last}. */
        TEXT_ALIGN_LAST_("text-align-last", "text-align-last", ff("auto")),

        /** The style property {@code textAnchor}. */
        TEXT_ANCHOR("textAnchor", "text-anchor", ff("start"), ie("start"), chromeAndEdge("start")),

        /** The style property {@code text-anchor}. */
        TEXT_ANCHOR_("text-anchor", "text-anchor", ff("start")),

        /** The style property {@code textAutospace}. */
        TEXT_AUTOSPACE("textAutospace", "text-autospace", ie("undefined")),

        /** The style property {@code textCombineUpright}. */
        TEXT_COMBINE_UPRIGHT("textCombineUpright", "text-combine-upright", chromeAndEdge("none"), ff("none")),

        /** The style property {@code text-combine-upright}. */
        TEXT_COMBINE_UPRIGHT_("text-combine-upright", "text-combine-upright", ff("none")),

        /** The style property {@code textDecoration}. */
        TEXT_DECORATION("textDecoration", "text-decoration", chromeAndEdge("none solid rgb(0, 0, 0)"),
                ff("rgb(0, 0, 0)"), ie("none")),

        /** The style property {@code text-decoration}. */
        TEXT_DECORATION_("text-decoration", "text-decoration", ff("rgb(0, 0, 0)")),

        /** The style property {@code textDecorationBlink}. */
        TEXT_DECORATION_BLINK("textDecorationBlink", "text-decoration-blink", ie("false")),

        /** The style property {@code textDecorationColor}. */
        TEXT_DECORATION_COLOR("textDecorationColor", "text-decoration-color",
                chromeAndEdge("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)")),

        /** The style property {@code text-decoration-color}. */
        TEXT_DECORATION_COLOR_("text-decoration-color", "text-decoration-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code textDecorationLine}. */
        TEXT_DECORATION_LINE("textDecorationLine", "text-decoration-line", chromeAndEdge("none"), ff("none")),

        /** The style property {@code text-decoration-line}. */
        TEXT_DECORATION_LINE_("text-decoration-line", "text-decoration-line", ff("none")),

        /** The style property {@code textDecorationLineThrough}. */
        TEXT_DECORATION_LINE_THROUGH("textDecorationLineThrough", "text-decoration-line-through",
                ie("false")),

        /** The style property {@code textDecorationNone}. */
        TEXT_DECORATION_NONE("textDecorationNone", "text-decoration-none", ie("false")),

        /** The style property {@code textDecorationOverline}. */
        TEXT_DECORATION_OVERLINE("textDecorationOverline", "text-decoration-overline",
                ie("false")),

        /** The style property {@code textDecorationSkipInk}. */
        TEXT_DECORATION_SKIP_INK("textDecorationSkipInk", "text-decoration-skip-ink",
                chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code text-decoration-skip-ink}. */
        TEXT_DECORATION_SKIP_INK_("text-decoration-skip-ink", "text-decoration-skip-ink", ff("auto")),

        /** The style property {@code textDecorationStyle}. */
        TEXT_DECORATION_STYLE("textDecorationStyle", "text-decoration-style", chromeAndEdge("solid"), ff("solid")),

        /** The style property {@code text-decoration-style}. */
        TEXT_DECORATION_STYLE_("text-decoration-style", "text-decoration-style", ff("solid")),

        /** The style property {@code textDecorationThickness}. */
        TEXT_DECORATION_THICKNESS("textDecorationThickness", "text-decoration-thickness",
                chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code text-decoration-thickness}. */
        TEXT_DECORATION_THICKNESS_("text-decoration-thickness", "text-decoration-thickness", ff("auto")),

        /** The style property {@code textDecorationUnderline}. */
        TEXT_DECORATION_UNDERLINE("textDecorationUnderline", "text-decoration-underline",
                ie("false")),

        /** The style property {@code textEmphasis}. */
        TEXT_EMPHASIS("textEmphasis", "text-emphasis", ff("")),

        /** The style property {@code text-emphasis}. */
        TEXT_EMPHASIS_("text-emphasis", "text-emphasis", ff("")),

        /** The style property {@code textEmphasisColor}. */
        TEXT_EMPHASIS_COLOR("textEmphasisColor", "text-emphasis-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code text-emphasis-color}. */
        TEXT_EMPHASIS_COLOR_("text-emphasis-color", "text-emphasis-color", ff("rgb(0, 0, 0)")),

        /** The style property {@code textEmphasisPosition}. */
        TEXT_EMPHASIS_POSITION("textEmphasisPosition", "text-emphasis-position", ff("over right")),

        /** The style property {@code text-emphasis-position}. */
        TEXT_EMPHASIS_POSITION_("text-emphasis-position", "text-emphasis-position", ff("over right")),

        /** The style property {@code textEmphasisStyle}. */
        TEXT_EMPHASIS_STYLE("textEmphasisStyle", "text-emphasis-style", ff("none")),

        /** The style property {@code text-emphasis-style}. */
        TEXT_EMPHASIS_STYLE_("text-emphasis-style", "text-emphasis-style", ff("none")),

        /** The style property {@code textIndent}. */
        TEXT_INDENT("textIndent", "text-indent", chromeAndEdge("0px"), ff(""), ie("")),

        /** The style property {@code text-indent}. */
        TEXT_INDENT_("text-indent", "text-indent", ff("0px")),

        /** The style property {@code textJustify}. */
        TEXT_JUSTIFY("textJustify", "text-justify", ie("auto"), ff("auto")),

        /** The style property {@code text-justify}. */
        TEXT_JUSTIFY_("text-justify", "text-justify", ff("auto")),

        /** The style property {@code textJustifyTrim}. */
        TEXT_JUSTIFY_TRIM("textJustifyTrim", "text-justify-trim", ie("undefined")),

        /** The style property {@code textKashida}. */
        TEXT_KASHIDA("textKashida", "text-kashida", ie("undefined")),

        /** The style property {@code textKashidaSpace}. */
        TEXT_KASHIDA_SPACE("textKashidaSpace", "text-kashida-space", ie("undefined")),

        /** The style property {@code textOrientation}. */
        TEXT_ORIENTATION("textOrientation", "text-orientation", chromeAndEdge("mixed"), ff("mixed")),

        /** The style property {@code text-orientation}. */
        TEXT_ORIENTATION_("text-orientation", "text-orientation", ff("mixed")),

        /** The style property {@code textOverflow}. */
        TEXT_OVERFLOW("textOverflow", "text-overflow", ff("clip"), ie("clip"), chromeAndEdge("clip")),

        /** The style property {@code text-overflow}. */
        TEXT_OVERFLOW_("text-overflow", "text-overflow", ff("clip")),

        /** The style property {@code textRendering}. */
        TEXT_RENDERING("textRendering", "text-rendering", ff("auto"), chromeAndEdge("auto")),

        /** The style property {@code text-rendering}. */
        TEXT_RENDERING_("text-rendering", "text-rendering", ff("auto")),

        /** The style property {@code textShadow}. */
        TEXT_SHADOW("textShadow", "text-shadow", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code text-shadow}. */
        TEXT_SHADOW_("text-shadow", "text-shadow", ff("none")),

        /** The style property {@code textSizeAdjust}. */
        TEXT_SIZE_ADJUST("textSizeAdjust", "text-size-adjust", chromeAndEdge("auto")),

        /** The style property {@code textTransform}. */
        TEXT_TRANSFORM("textTransform", "text-transform", chromeAndEdge("none"), ff("none"), ie("none")),

        /** The style property {@code text-transform}. */
        TEXT_TRANSFORM_("text-transform", "text-transform", ff("none")),

        /** The style property {@code textUnderlineOffset}. */
        TEXT_UNDERLINE_OFFSET("textUnderlineOffset", "text-underline-offset", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code text-underline-offset}. */
        TEXT_UNDERLINE_OFFSET_("text-underline-offset", "text-underline-offset", ff("auto")),

        /** The style property {@code textUnderlinePosition}. */
        TEXT_UNDERLINE_POSITION("textUnderlinePosition", "text-underline-position",
                chromeAndEdge("auto"), ie("auto"), ff("auto")),

        /** The style property {@code text-underline-position}. */
        TEXT_UNDERLINE_POSITION_("text-underline-position", "text-underline-position", ff("auto")),

        /** The style property {@code top}. */
        TOP("top", "top", ff("auto"), ie("auto"), chromeAndEdge("auto")),

        /** The style property {@code touchAction}. */
        TOUCH_ACTION("touchAction", "touch-action", ie("auto"), chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code touch-action}. */
        TOUCH_ACTION_("touch-action", "touch-action", ff("auto")),

        /** The style property {@code transform}. */
        TRANSFORM("transform", "transform", ff("none"), ie("none"), chromeAndEdge("none")),

        /** The style property {@code transformBox}. */
        TRANSFORM_BOX("transformBox", "transform-box", chromeAndEdge("view-box"), ff("border-box")),

        /** The style property {@code transform-box}. */
        TRANSFORM_BOX_("transform-box", "transform-box", ff("border-box")),

        /** The style property {@code transformOrigin}. */
        TRANSFORM_ORIGIN("transformOrigin", "transform-origin",
                ff("622px 171.5px"), ie("620px 163.2px"), chromeAndEdge("620px 161px")),

        /** The style property {@code transform-origin}. */
        TRANSFORM_ORIGIN_("transform-origin", "transform-origin", ff("622px 171.5px")),

        /** The style property {@code transformStyle}. */
        TRANSFORM_STYLE("transformStyle", "transform-style", ff("flat"), ie("flat"), chromeAndEdge("flat")),

        /** The style property {@code transform-style}. */
        TRANSFORM_STYLE_("transform-style", "transform-style", ff("flat")),

        /** The style property {@code transition}. */
        TRANSITION("transition", "transition", ff(""), ie(""), chromeAndEdge("all 0s ease 0s")),

        /** The style property {@code transitionDelay}. */
        TRANSITION_DELAY("transitionDelay", "transition-delay", ff("0s"), ie("0s"), chromeAndEdge("0s")),

        /** The style property {@code transition-delay}. */
        TRANSITION_DELAY_("transition-delay", "transition-delay", ff("0s")),

        /** The style property {@code transitionDuration}. */
        TRANSITION_DURATION("transitionDuration", "transition-duration", ff("0s"), ie("0s"), chromeAndEdge("0s")),

        /** The style property {@code transition-duration}. */
        TRANSITION_DURATION_("transition-duration", "transition-duration", ff("0s")),

        /** The style property {@code transitionProperty}. */
        TRANSITION_PROPERTY("transitionProperty", "transition-property", ff("all"), ie("all"), chromeAndEdge("all")),

        /** The style property {@code transition-property}. */
        TRANSITION_PROPERTY_("transition-property", "transition-property", ff("all")),

        /** The style property {@code transitionTimingFunction}. */
        TRANSITION_TIMING_FUNCTION("transitionTimingFunction",
                "transition-timing-function",
                ff("ease"),
                ie("cubic-bezier(0.25, 0.1, 0.25, 1)"),
                chromeAndEdge("ease")),

        /** The style property {@code transition-timing-function}. */
        TRANSITION_TIMING_FUNCTION_("transition-timing-function", "transition-timing-function",
                ff("ease")),

        /** The style property {@code translate}. */
        TRANSLATE("translate", "translate", ff("none")),

        /** The style property {@code unicodeBidi}. */
        UNICODE_BIDI("unicodeBidi", "unicode-bidi",
                ff("isolate"), ie("normal"), chromeAndEdge("normal")),

        /** The style property {@code unicode-bidi}. */
        UNICODE_BIDI_("unicode-bidi", "unicode-bidi", ff("isolate")),

        /** The style property {@code unicodeRange}. */
        UNICODE_RANGE("unicodeRange", "unicode-range", chromeAndEdge("")),

        /** The style property {@code userSelect}. */
        USER_SELECT("userSelect", "user-select", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code user-select}. */
        USER_SELECT_("user-select", "user-select", ff("auto")),

        /** The style property {@code userZoom}. */
        USER_ZOOM("userZoom", "user-zoom", chromeAndEdge("")),

        /** The style property {@code vectorEffect}. */
        VECTOR_EFFECT("vectorEffect", "vector-effect", ff("none"), chromeAndEdge("none")),

        /** The style property {@code vector-effect}. */
        VECTOR_EFFECT_("vector-effect", "vector-effect", ff("none")),

        /** The style property {@code verticalAlign}. */
        VERTICAL_ALIGN("verticalAlign", "vertical-align", chromeAndEdge("baseline"), ff(""), ie("")),

        /** The style property {@code vertical-align}. */
        VERTICAL_ALIGN_("vertical-align", "vertical-align", ff("baseline")),

        /** The style property {@code visibility}. */
        VISIBILITY("visibility", "visibility", chromeAndEdge("visible"), ff("visible"), ie("visible")),

        /** The style property {@code voiceFamily}. */
        VOICE_FAMILY("voiceFamily", "voice-family"),

        /** The style property {@code volume}. */
        VOLUME("volume", "volume"),

        /** The style property {@code webkitAlignContent}. */
        WEBKIT_ALIGN_CONTENT("webkitAlignContent", "webkit-align-content", chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code WebkitAlignContent}. */
        WEBKIT_ALIGN_CONTENT_("WebkitAlignContent", "webkit-align-content", ff("normal")),

        /** The style property {@code -webkit-align-content}. */
        WEBKIT_ALIGN_CONTENT__("-webkit-align-content", "webkit-align-content", ff("normal")),

        /** The style property {@code webkitAlignItems}. */
        WEBKIT_ALIGN_ITEMS("webkitAlignItems", "webkit-align-items", chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code WebkitAlignItems}. */
        WEBKIT_ALIGN_ITEMS_("WebkitAlignItems", "webkit-align-items", ff("normal")),

        /** The style property {@code -webkit-align-items}. */
        WEBKIT_ALIGN_ITEMS__("-webkit-align-items", "webkit-align-items", ff("normal")),

        /** The style property {@code webkitAlignSelf}. */
        WEBKIT_ALIGN_SELF("webkitAlignSelf", "webkit-align-self", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code WebkitAlignSelf}. */
        WEBKIT_ALIGN_SELF_("WebkitAlignSelf", "webkit-align-self", ff("auto")),

        /** The style property {@code -webkit-align-self}. */
        WEBKIT_ALIGN_SELF__("-webkit-align-self", "webkit-align-self", ff("auto")),

        /** The style property {@code webkitAnimation}. */
        WEBKIT_ANIMATION("webkitAnimation", "webkit-animation",
                chromeAndEdge("none 0s ease 0s 1 normal none running"), ff("")),

        /** The style property {@code WebkitAnimation}. */
        WEBKIT_ANIMATION_("WebkitAnimation", "webkit-animation", ff("")),

        /** The style property {@code -webkit-animation}. */
        WEBKIT_ANIMATION__("-webkit-animation", "webkit-animation", ff("")),

        /** The style property {@code webkitAnimationDelay}. */
        WEBKIT_ANIMATION_DELAY("webkitAnimationDelay", "webkit-animation-delay", chromeAndEdge("0s"), ff("0s")),

        /** The style property {@code WebkitAnimationDelay}. */
        WEBKIT_ANIMATION_DELAY_("WebkitAnimationDelay", "webkit-animation-delay", ff("0s")),

        /** The style property {@code -webkit-animation-delay}. */
        WEBKIT_ANIMATION_DELAY__("-webkit-animation-delay", "webkit-animation-delay", ff("0s")),

        /** The style property {@code webkitAnimationDirection}. */
        WEBKIT_ANIMATION_DIRECTION("webkitAnimationDirection", "webkit-animation-direction",
                chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code WebkitAnimationDirection}. */
        WEBKIT_ANIMATION_DIRECTION_("WebkitAnimationDirection", "webkit-animation-direction", ff("normal")),

        /** The style property {@code -webkit-animation-direction}. */
        WEBKIT_ANIMATION_DIRECTION__("-webkit-animation-direction", "webkit-animation-direction", ff("normal")),

        /** The style property {@code webkitAnimationDuration}. */
        WEBKIT_ANIMATION_DURATION("webkitAnimationDuration", "webkit-animation-duration",
                chromeAndEdge("0s"), ff("0s")),

        /** The style property {@code WebkitAnimationDuration}. */
        WEBKIT_ANIMATION_DURATION_("WebkitAnimationDuration", "webkit-animation-duration", ff("0s")),

        /** The style property {@code -webkit-animation-duration}. */
        WEBKIT_ANIMATION_DURATION__("-webkit-animation-duration", "webkit-animation-duration", ff("0s")),

        /** The style property {@code webkitAnimationFillMode}. */
        WEBKIT_ANIMATION_FILL_MODE("webkitAnimationFillMode", "webkit-animation-fill-mode",
                chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitAnimationFillMode}. */
        WEBKIT_ANIMATION_FILL_MODE_("WebkitAnimationFillMode", "webkit-animation-fill-mode", ff("none")),

        /** The style property {@code -webkit-animation-fill-mode}. */
        WEBKIT_ANIMATION_FILL_MODE__("-webkit-animation-fill-mode", "webkit-animation-fill-mode", ff("none")),

        /** The style property {@code webkitAnimationIterationCount}. */
        WEBKIT_ANIMATION_ITERATION_COUNT("webkitAnimationIterationCount", "webkit-animation-iteration-count",
                chromeAndEdge("1"), ff("1")),

        /** The style property {@code WebkitAnimationIterationCount}. */
        WEBKIT_ANIMATION_ITERATION_COUNT_("WebkitAnimationIterationCount", "webkit-animation-iteration-count",
                ff("1")),

        /** The style property {@code -webkit-animation-iteration-count}. */
        WEBKIT_ANIMATION_ITERATION_COUNT__("-webkit-animation-iteration-count", "webkit-animation-iteration-count",
                ff("1")),

        /** The style property {@code webkitAnimationName}. */
        WEBKIT_ANIMATION_NAME("webkitAnimationName", "webkit-animation-name", chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitAnimationName}. */
        WEBKIT_ANIMATION_NAME_("WebkitAnimationName", "webkit-animation-name", ff("none")),

        /** The style property {@code -webkit-animation-name}. */
        WEBKIT_ANIMATION_NAME__("-webkit-animation-name", "webkit-animation-name", ff("none")),

        /** The style property {@code webkitAnimationPlayState}. */
        WEBKIT_ANIMATION_PLAY_STATE("webkitAnimationPlayState", "webkit-animation-play-state",
                chromeAndEdge("running"), ff("running")),

        /** The style property {@code WebkitAnimationPlayState}. */
        WEBKIT_ANIMATION_PLAY_STATE_("WebkitAnimationPlayState", "webkit-animation-play-state", ff("running")),

        /** The style property {@code -webkit-animation-play-state}. */
        WEBKIT_ANIMATION_PLAY_STATE__("-webkit-animation-play-state", "webkit-animation-play-state", ff("running")),

        /** The style property {@code webkitAnimationTimingFunction}. */
        WEBKIT_ANIMATION_TIMING_FUNCTION("webkitAnimationTimingFunction", "webkit-animation-timing-function",
                chromeAndEdge("ease"), ff("ease")),

        /** The style property {@code WebkitAnimationTimingFunction}. */
        WEBKIT_ANIMATION_TIMING_FUNCTION_("WebkitAnimationTimingFunction", "webkit-animation-timing-function",
                ff("ease")),

        /** The style property {@code -webkit-animation-timing-function}. */
        WEBKIT_ANIMATION_TIMING_FUNCTION__("-webkit-animation-timing-function", "webkit-animation-timing-function",
                ff("ease")),

        /** The style property {@code webkitAppRegion}. */
        WEBKIT_APP_REGION("webkitAppRegion", "webkit-app-region", chromeAndEdge("none")),

        /** The style property {@code webkitAppearance}. */
        WEBKIT_APPEARANCE("webkitAppearance", "webkit-appearance", chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitAppearance}. */
        WEBKIT_APPEARANCE_("WebkitAppearance", "webkit-appearance", ff("none")),

        /** The style property {@code -webkit-appearance}. */
        WEBKIT_APPEARANCE__("-webkit-appearance", "webkit-appearance", ff("none")),

        /** The style property {@code webkitBackfaceVisibility}. */
        WEBKIT_BACKFACE_VISIBILITY("webkitBackfaceVisibility", "webkit-backface-visibility",
                chromeAndEdge("visible"), ff("visible")),

        /** The style property {@code WebkitBackfaceVisibility}. */
        WEBKIT_BACKFACE_VISIBILITY_("WebkitBackfaceVisibility", "webkit-backface-visibility",
                ff("visible")),

        /** The style property {@code -webkit-backface-visibility}. */
        WEBKIT_BACKFACE_VISIBILITY__("-webkit-backface-visibility", "webkit-backface-visibility",
                ff("visible")),

        /** The style property {@code webkitBackgroundClip}. */
        WEBKIT_BACKGROUND_CLIP("webkitBackgroundClip", "webkit-background-clip",
                chromeAndEdge("border-box"), ff("border-box")),

        /** The style property {@code WebkitBackgroundClip}. */
        WEBKIT_BACKGROUND_CLIP_("WebkitBackgroundClip", "webkit-background-clip", ff("border-box")),

        /** The style property {@code -webkit-background-clip}. */
        WEBKIT_BACKGROUND_CLIP__("-webkit-background-clip", "webkit-background-clip", ff("border-box")),

        /** The style property {@code webkitBackgroundOrigin}. */
        WEBKIT_BACKGROUND_ORIGIN("webkitBackgroundOrigin", "webkit-background-origin",
                chromeAndEdge("padding-box"), ff("padding-box")),

        /** The style property {@code WebkitBackgroundOrigin}. */
        WEBKIT_BACKGROUND_ORIGIN_("WebkitBackgroundOrigin", "webkit-background-origin", ff("padding-box")),

        /** The style property {@code -webkit-background-origin}. */
        WEBKIT_BACKGROUND_ORIGIN__("-webkit-background-origin", "webkit-background-origin", ff("padding-box")),

        /** The style property {@code webkitBackgroundSize}. */
        WEBKIT_BACKGROUND_SIZE("webkitBackgroundSize", "webkit-background-size", chromeAndEdge("auto"),
                ff("auto")),

        /** The style property {@code WebkitBackgroundSize}. */
        WEBKIT_BACKGROUND_SIZE_("WebkitBackgroundSize", "webkit-background-size",
                ff("auto")),

        /** The style property {@code -webkit-background-size}. */
        WEBKIT_BACKGROUND_SIZE__("-webkit-background-size", "webkit-background-size",
                ff("auto")),

        /** The style property {@code webkitBorderAfter}. */
        WEBKIT_BORDER_AFTER("webkitBorderAfter", "webkit-border-after", chromeAndEdge("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderAfterColor}. */
        WEBKIT_BORDER_AFTER_COLOR("webkitBorderAfterColor", "webkit-border-after-color", chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderAfterStyle}. */
        WEBKIT_BORDER_AFTER_STYLE("webkitBorderAfterStyle", "webkit-border-after-style", chromeAndEdge("none")),

        /** The style property {@code webkitBorderAfterWidth}. */
        WEBKIT_BORDER_AFTER_WIDTH("webkitBorderAfterWidth", "webkit-border-after-width", chromeAndEdge("0px")),

        /** The style property {@code webkitBorderBefore}. */
        WEBKIT_BORDER_BEFORE("webkitBorderBefore", "webkit-border-before", chromeAndEdge("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderBeforeColor}. */
        WEBKIT_BORDER_BEFORE_COLOR("webkitBorderBeforeColor", "webkit-border-before-color",
                chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderBeforeStyle}. */
        WEBKIT_BORDER_BEFORE_STYLE("webkitBorderBeforeStyle", "webkit-border-before-style", chromeAndEdge("none")),

        /** The style property {@code webkitBorderBeforeWidth}. */
        WEBKIT_BORDER_BEFORE_WIDTH("webkitBorderBeforeWidth", "webkit-border-before-width", chromeAndEdge("0px")),

        /** The style property {@code webkitBorderBottomLeftRadius}. */
        WEBKIT_BORDER_BOTTOM_LEFT_RADIUS("webkitBorderBottomLeftRadius", "webkit-border-bottom-left-radius",
                chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code WebkitBorderBottomLeftRadius}. */
        WEBKIT_BORDER_BOTTOM_LEFT_RADIUS_("WebkitBorderBottomLeftRadius", "webkit-border-bottom-left-radius",
                ff("0px")),

        /** The style property {@code -webkit-border-bottom-left-radius}. */
        WEBKIT_BORDER_BOTTOM_LEFT_RADIUS__("-webkit-border-bottom-left-radius", "webkit-border-bottom-left-radius",
                ff("0px")),

        /** The style property {@code webkitBorderBottomRightRadius}. */
        WEBKIT_BORDER_BOTTOM_RIGHT_RADIUS("webkitBorderBottomRightRadius", "webkit-border-bottom-right-radius",
                chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code WebkitBorderBottomRightRadius}. */
        WEBKIT_BORDER_BOTTOM_RIGHT_RADIUS_("WebkitBorderBottomRightRadius", "webkit-border-bottom-right-radius",
                ff("0px")),

        /** The style property {@code -webkit-border-bottom-right-radius}. */
        WEBKIT_BORDER_BOTTOM_RIGHT_RADIUS__("-webkit-border-bottom-right-radius", "webkit-border-bottom-right-radius",
                ff("0px")),

        /** The style property {@code webkitBorderEnd}. */
        WEBKIT_BORDER_END("webkitBorderEnd", "webkit-border-end", chromeAndEdge("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderEndColor}. */
        WEBKIT_BORDER_END_COLOR("webkitBorderEndColor", "webkit-border-end-color", chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderEndStyle}. */
        WEBKIT_BORDER_END_STYLE("webkitBorderEndStyle", "webkit-border-end-style", chromeAndEdge("none")),

        /** The style property {@code webkitBorderEndWidth}. */
        WEBKIT_BORDER_END_WIDTH("webkitBorderEndWidth", "webkit-border-end-width", chromeAndEdge("0px")),

        /** The style property {@code webkitBorderHorizontalSpacing}. */
        WEBKIT_BORDER_HORIZONTAL_SPACING("webkitBorderHorizontalSpacing", "webkit-border-horizontal-spacing",
                chromeAndEdge("0px")),

        /** The style property {@code webkitBorderImage}. */
        WEBKIT_BORDER_IMAGE("webkitBorderImage", "webkit-border-image", chromeAndEdge("none"), ff("")),

        /** The style property {@code WebkitBorderImage}. */
        WEBKIT_BORDER_IMAGE_("WebkitBorderImage", "webkit-border-image", ff("")),

        /** The style property {@code -webkit-border-image}. */
        WEBKIT_BORDER_IMAGE__("-webkit-border-image", "webkit-border-image", ff("")),

        /** The style property {@code webkitBorderRadius}. */
        WEBKIT_BORDER_RADIUS("webkitBorderRadius", "webkit-border-radius", chromeAndEdge("0px"), ff("")),

        /** The style property {@code WebkitBorderRadius}. */
        WEBKIT_BORDER_RADIUS_("WebkitBorderRadius", "webkit-border-radius", ff("")),

        /** The style property {@code -webkit-border-radius}. */
        WEBKIT_BORDER_RADIUS__("-webkit-border-radius", "webkit-border-radius", ff("")),

        /** The style property {@code webkitBorderStart}. */
        WEBKIT_BORDER_START("webkitBorderStart", "webkit-border-start", chromeAndEdge("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderStartColor}. */
        WEBKIT_BORDER_START_COLOR("webkitBorderStartColor", "webkit-border-start-color", chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code webkitBorderStartStyle}. */
        WEBKIT_BORDER_START_STYLE("webkitBorderStartStyle", "webkit-border-start-style", chromeAndEdge("none")),

        /** The style property {@code webkitBorderStartWidth}. */
        WEBKIT_BORDER_START_WIDTH("webkitBorderStartWidth", "webkit-border-start-width", chromeAndEdge("0px")),

        /** The style property {@code webkitBorderTopLeftRadius}. */
        WEBKIT_BORDER_TOP_LEFT_RADIUS("webkitBorderTopLeftRadius", "webkit-border-top-left-radius",
                chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code WebkitBorderTopLeftRadius}. */
        WEBKIT_BORDER_TOP_LEFT_RADIUS_("WebkitBorderTopLeftRadius", "webkit-border-top-left-radius", ff("0px")),

        /** The style property {@code -webkit-border-top-left-radius}. */
        WEBKIT_BORDER_TOP_LEFT_RADIUS__("-webkit-border-top-left-radius", "webkit-border-top-left-radius",
                ff("0px")),

        /** The style property {@code webkitBorderTopRightRadius}. */
        WEBKIT_BORDER_TOP_RIGHT_RADIUS("webkitBorderTopRightRadius", "webkit-border-top-right-radius",
                chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code WebkitBorderTopRightRadius}. */
        WEBKIT_BORDER_TOP_RIGHT_RADIUS_("WebkitBorderTopRightRadius", "webkit-border-top-right-radius", ff("0px")),

        /** The style property {@code -webkit-border-top-right-radius}. */
        WEBKIT_BORDER_TOP_RIGHT_RADIUS__("-webkit-border-top-right-radius", "webkit-border-top-right-radius",
                ff("0px")),

        /** The style property {@code webkitBorderVerticalSpacing}. */
        WEBKIT_BORDER_VERTICAL_SPACING("webkitBorderVerticalSpacing", "webkit-border-vertical-spacing",
                chromeAndEdge("0px")),

        /** The style property {@code webkitBoxAlign}. */
        WEBKIT_BOX_ALIGN("webkitBoxAlign", "webkit-box-align", chromeAndEdge("stretch"), ff("stretch")),

        /** The style property {@code WebkitBoxAlign}. */
        WEBKIT_BOX_ALIGN_("WebkitBoxAlign", "webkit-box-align", ff("stretch")),

        /** The style property {@code -webkit-box-align}. */
        WEBKIT_BOX_ALIGN__("-webkit-box-align", "webkit-box-align", ff("stretch")),

        /** The style property {@code webkitBoxDecorationBreak}. */
        WEBKIT_BOX_DECORATION_BREAK("webkitBoxDecorationBreak", "webkit-box-decoration-break", chromeAndEdge("slice")),

        /** The style property {@code webkitBoxDirection}. */
        WEBKIT_BOX_DIRECTION("webkitBoxDirection", "webkit-box-direction", chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code WebkitBoxDirection}. */
        WEBKIT_BOX_DIRECTION_("WebkitBoxDirection", "webkit-box-direction", ff("normal")),

        /** The style property {@code -webkit-box-direction}. */
        WEBKIT_BOX_DIRECTION__("-webkit-box-direction", "webkit-box-direction", ff("normal")),

        /** The style property {@code webkitBoxFlex}. */
        WEBKIT_BOX_FLEX("webkitBoxFlex", "webkit-box-flex", chromeAndEdge("0"), ff("0")),

        /** The style property {@code WebkitBoxFlex}. */
        WEBKIT_BOX_FLEX_("WebkitBoxFlex", "webkit-box-flex", ff("0")),

        /** The style property {@code -webkit-box-flex}. */
        WEBKIT_BOX_FLEX__("-webkit-box-flex", "webkit-box-flex", ff("0")),

        /** The style property {@code webkitBoxFlexGroup}. */
        WEBKIT_BOX_FLEX_GROUP("webkitBoxFlexGroup", "webkit-box-flex-group", chromeAndEdgeNotIterable("1")),

        /** The style property {@code webkitBoxLines}. */
        WEBKIT_BOX_LINES("webkitBoxLines", "webkit-box-lines", chromeAndEdgeNotIterable("single")),

        /** The style property {@code webkitBoxOrdinalGroup}. */
        WEBKIT_BOX_ORDINAL_GROUP("webkitBoxOrdinalGroup", "webkit-box-ordinal-group", chromeAndEdge("1"), ff("1")),

        /** The style property {@code WebkitBoxOrdinalGroup}. */
        WEBKIT_BOX_ORDINAL_GROUP_("WebkitBoxOrdinalGroup", "webkit-box-ordinal-group", ff("1")),

        /** The style property {@code -webkit-box-ordinal-group}. */
        WEBKIT_BOX_ORDINAL_GROUP__("-webkit-box-ordinal-group", "webkit-box-ordinal-group", ff("1")),

        /** The style property {@code webkitBoxOrient}. */
        WEBKIT_BOX_ORIENT("webkitBoxOrient", "webkit-box-orient", chromeAndEdge("horizontal"), ff("horizontal")),

        /** The style property {@code WebkitBoxOrient}. */
        WEBKIT_BOX_ORIENT_("WebkitBoxOrient", "webkit-box-orient", ff("horizontal")),

        /** The style property {@code -webkit-box-orient}. */
        WEBKIT_BOX_ORIENT__("-webkit-box-orient", "webkit-box-orient", ff("horizontal")),

        /** The style property {@code webkitBoxPack}. */
        WEBKIT_BOX_PACK("webkitBoxPack", "webkit-box-pack", chromeAndEdge("start"), ff("start")),

        /** The style property {@code WebkitBoxPack}. */
        WEBKIT_BOX_PACK_("WebkitBoxPack", "webkit-box-pack", ff("start")),

        /** The style property {@code -webkit-box-pack}. */
        WEBKIT_BOX_PACK__("-webkit-box-pack", "webkit-box-pack", ff("start")),

        /** The style property {@code webkitBoxReflect}. */
        WEBKIT_BOX_REFLECT("webkitBoxReflect", "webkit-box-reflect", chromeAndEdge("none")),

        /** The style property {@code webkitBoxShadow}. */
        WEBKIT_BOX_SHADOW("webkitBoxShadow", "webkit-box-shadow", chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitBoxShadow}. */
        WEBKIT_BOX_SHADOW_("WebkitBoxShadow", "webkit-box-shadow", ff("none")),

        /** The style property {@code -webkit-box-shadow}. */
        WEBKIT_BOX_SHADOW__("-webkit-box-shadow", "webkit-box-shadow", ff("none")),

        /** The style property {@code webkitBoxSizing}. */
        WEBKIT_BOX_SIZING("webkitBoxSizing", "webkit-box-sizing", chromeAndEdge("content-box"), ff("content-box")),

        /** The style property {@code WebkitBoxSizing}. */
        WEBKIT_BOX_SIZING_("WebkitBoxSizing", "webkit-box-sizing", ff("content-box")),

        /** The style property {@code -webkit-box-sizing}. */
        WEBKIT_BOX_SIZING__("-webkit-box-sizing", "webkit-box-sizing", ff("content-box")),

        /** The style property {@code webkitClipPath}. */
        WEBKIT_CLIP_PATH("webkitClipPath", "webkit-clip-path", chromeAndEdge("none")),

        /** The style property {@code webkitColumnBreakAfter}. */
        WEBKIT_COLUMN_BREAK_AFTER("webkitColumnBreakAfter", "webkit-column-break-after", chromeAndEdge("auto")),

        /** The style property {@code webkitColumnBreakBefore}. */
        WEBKIT_COLUMN_BREAK_BEFORE("webkitColumnBreakBefore", "webkit-column-break-before", chromeAndEdge("auto")),

        /** The style property {@code webkitColumnBreakInside}. */
        WEBKIT_COLUMN_BREAK_INSIDE("webkitColumnBreakInside", "webkit-column-break-inside", chromeAndEdge("auto")),

        /** The style property {@code webkitColumnCount}. */
        WEBKIT_COLUMN_COUNT("webkitColumnCount", "webkit-column-count", chromeAndEdge("auto")),

        /** The style property {@code webkitColumnGap}. */
        WEBKIT_COLUMN_GAP("webkitColumnGap", "webkit-column-gap", chromeAndEdge("normal")),

        /** The style property {@code webkitColumnRule}. */
        WEBKIT_COLUMN_RULE("webkitColumnRule", "webkit-column-rule", chromeAndEdge("0px none rgb(0, 0, 0)")),

        /** The style property {@code webkitColumnRuleColor}. */
        WEBKIT_COLUMN_RULE_COLOR("webkitColumnRuleColor", "webkit-column-rule-color", chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code webkitColumnRuleStyle}. */
        WEBKIT_COLUMN_RULE_STYLE("webkitColumnRuleStyle", "webkit-column-rule-style", chromeAndEdge("none")),

        /** The style property {@code webkitColumnRuleWidth}. */
        WEBKIT_COLUMN_RULE_WIDTH("webkitColumnRuleWidth", "webkit-column-rule-width", chromeAndEdge("0px")),

        /** The style property {@code webkitColumnSpan}. */
        WEBKIT_COLUMN_SPAN("webkitColumnSpan", "webkit-column-rule-span", chromeAndEdge("none")),

        /** The style property {@code webkitColumnWidth}. */
        WEBKIT_COLUMN_WIDTH("webkitColumnWidth", "webkit-column-width", chromeAndEdge("auto")),

        /** The style property {@code webkitColumns}. */
        WEBKIT_COLUMNS("webkitColumns", "webkit-columns", chromeAndEdge("auto auto")),

        /** The style property {@code webkitFilter}. */
        WEBKIT_FILTER("webkitFilter", "webkit-filter", chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitFilter}. */
        WEBKIT_FILTER_("WebkitFilter", "webkit-filter", ff("none")),

        /** The style property {@code -webkit-filter}. */
        WEBKIT_FILTER__("-webkit-filter", "webkit-filter", ff("none")),

        /** The style property {@code webkitFlex}. */
        WEBKIT_FLEX("webkitFlex", "webkit-flex", chromeAndEdge("0 1 auto"), ff("")),

        /** The style property {@code WebkitFlex}. */
        WEBKIT_FLEX_("WebkitFlex", "webkit-flex", ff("")),

        /** The style property {@code -webkit-flex}. */
        WEBKIT_FLEX__("-webkit-flex", "webkit-flex", ff("")),

        /** The style property {@code webkitFlexBasis}. */
        WEBKIT_FLEX_BASIS("webkitFlexBasis", "webkit-flex-basis", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code WebkitFlexBasis}. */
        WEBKIT_FLEX_BASIS_("WebkitFlexBasis", "webkit-flex-basis", ff("auto")),

        /** The style property {@code -webkit-flex-basis}. */
        WEBKIT_FLEX_BASIS__("-webkit-flex-basis", "webkit-flex-basis", ff("auto")),

        /** The style property {@code webkitFlexDirection}. */
        WEBKIT_FLEX_DIRECTION("webkitFlexDirection", "webkit-flex-direction", chromeAndEdge("row"), ff("row")),

        /** The style property {@code WebkitFlexDirection}. */
        WEBKIT_FLEX_DIRECTION_("WebkitFlexDirection", "webkit-flex-direction", ff("row")),

        /** The style property {@code -webkit-flex-direction}. */
        WEBKIT_FLEX_DIRECTION__("-webkit-flex-direction", "webkit-flex-direction", ff("row")),

        /** The style property {@code webkitFlexFlow}. */
        WEBKIT_FLEX_FLOW("webkitFlexFlow", "webkit-flex-flow", chromeAndEdge("row nowrap"), ff("")),

        /** The style property {@code WebkitFlexFlow}. */
        WEBKIT_FLEX_FLOW_("WebkitFlexFlow", "webkit-flex-flow", ff("")),

        /** The style property {@code -webkit-flex-flow}. */
        WEBKIT_FLEX_FLOW__("-webkit-flex-flow", "webkit-flex-flow", ff("")),

        /** The style property {@code webkitFlexGrow}. */
        WEBKIT_FLEX_GROW("webkitFlexGrow", "webkit-flex-grow", chromeAndEdge("0"), ff("0")),

        /** The style property {@code WebkitFlexGrow}. */
        WEBKIT_FLEX_GROW_("WebkitFlexGrow", "webkit-flex-grow", ff("0")),

        /** The style property {@code -webkit-flex-grow}. */
        WEBKIT_FLEX_GROW__("-webkit-flex-grow", "webkit-flex-grow", ff("0")),

        /** The style property {@code webkitFlexShrink}. */
        WEBKIT_FLEX_SHRINK("webkitFlexShrink", "webkit-flex-shrink", chromeAndEdge("1"), ff("1")),

        /** The style property {@code WebkitFlexShrink}. */
        WEBKIT_FLEX_SHRINK_("WebkitFlexShrink", "webkit-flex-shrink", ff("1")),

        /** The style property {@code -webkit-flex-shrink}. */
        WEBKIT_FLEX_SHRINK__("-webkit-flex-shrink", "webkit-flex-shrink", ff("1")),

        /** The style property {@code webkitFlexWrap}. */
        WEBKIT_FLEX_WRAP("webkitFlexWrap", "webkit-flex-wrap", chromeAndEdge("nowrap"), ff("nowrap")),

        /** The style property {@code WebkitFlexWrap}. */
        WEBKIT_FLEX_WRAP_("WebkitFlexWrap", "webkit-flex-wrap", ff("nowrap")),

        /** The style property {@code -webkit-flex-wrap}. */
        WEBKIT_FLEX_WRAP__("-webkit-flex-wrap", "webkit-flex-wrap", ff("nowrap")),

        /** The style property {@code webkitFontFeatureSettings}. */
        WEBKIT_FONT_FEATURE_SETTINGS("webkitFontFeatureSettings", "webkit-font-feature-settings",
                chromeAndEdge("normal")),

        /** The style property {@code webkitFontSmoothing}. */
        WEBKIT_FONT_SMOOTHING("webkitFontSmoothing", "webkit-font-smoothing", chromeAndEdge("auto")),

        /** The style property {@code webkitHighlight}. */
        WEBKIT_HIGHLIGHT("webkitHighlight", "webkit-highlight", chromeAndEdge("none")),

        /** The style property {@code webkitHyphenateCharacter}. */
        WEBKIT_HYPHENATE_CHARACTER("webkitHyphenateCharacter", "webkit-hyphenate-character", chromeAndEdge("auto")),

        /** The style property {@code webkitJustifyContent}. */
        WEBKIT_JUSTIFY_CONTENT("webkitJustifyContent", "webkit-justify-content", chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code WebkitJustifyContent}. */
        WEBKIT_JUSTIFY_CONTENT_("WebkitJustifyContent", "webkit-justify-content", ff("normal")),

        /** The style property {@code -webkit-justify-content}. */
        WEBKIT_JUSTIFY_CONTENT__("-webkit-justify-content", "webkit-justify-content", ff("normal")),

        /** The style property {@code webkitLineBreak}. */
        WEBKIT_LINE_BREAK("webkitLineBreak", "webkit-line-break", chromeAndEdge("auto")),

        /** The style property {@code webkitLineClamp}. */
        WEBKIT_LINE_CLAMP("webkitLineClamp", "webkit-line-clamp", chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitLineClamp}. */
        WEBKIT_LINE_CLAMP_("WebkitLineClamp", "webkit-line-clamp", ff("none")),

        /** The style property {@code -webkit-line-clamp}. */
        WEBKIT_LINE_CLAMP__("-webkit-line-clamp", "webkit-line-clamp", ff("none")),

        /** The style property {@code webkitLocale}. */
        WEBKIT_LOCALE("webkitLocale", "webkit-locale", chromeAndEdge("auto")),

        /** The style property {@code webkitLogicalHeight}. */
        WEBKIT_LOGICAL_HEIGHT("webkitLogicalHeight", "webkit-logical-height", chromeAndEdge("322px")),

        /** The style property {@code webkitLogicalWidth}. */
        WEBKIT_LOGICAL_WIDTH("webkitLogicalWidth", "webkit-logical-width", chromeAndEdge("1240px")),

        /** The style property {@code webkitMarginAfter}. */
        WEBKIT_MARGIN_AFTER("webkitMarginAfter", "webkit-margin-after", chromeAndEdge("0px")),

        /** The style property {@code webkitMarginBefore}. */
        WEBKIT_MARGIN_BEFORE("webkitMarginBefore", "webkit-margin-before", chromeAndEdge("0px")),

        /** The style property {@code webkitMarginEnd}. */
        WEBKIT_MARGIN_END("webkitMarginEnd", "webkit-margin-end", chromeAndEdge("0px")),

        /** The style property {@code webkitMarginStart}. */
        WEBKIT_MARGIN_START("webkitMarginStart", "webkit-margin-start", chromeAndEdge("0px")),

        /** The style property {@code webkitMask}. */
        WEBKIT_MASK("webkitMask", "webkit-mask", chromeAndEdge(""), ff("none")),

        /** The style property {@code WebkitMask}. */
        WEBKIT_MASK_("WebkitMask", "webkit-mask", ff("none")),

        /** The style property {@code -webkit-mask}. */
        WEBKIT_MASK__("-webkit-mask", "webkit-mask", ff("none")),

        /** The style property {@code webkitMaskBoxImage}. */
        WEBKIT_MASK_BOX_IMAGE("webkitMaskBoxImage", "webkit-mask-box-image", chromeAndEdge("none")),

        /** The style property {@code webkitMaskBoxImageOutset}. */
        WEBKIT_MASK_BOX_IMAGE_OUTSET("webkitMaskBoxImageOutset", "webkit-mask-box-image-outset", chromeAndEdge("0")),

        /** The style property {@code webkitMaskBoxImageRepeat}. */
        WEBKIT_MASK_BOX_IMAGE_REPEAT("webkitMaskBoxImageRepeat", "webkit-mask-box-image-repeat",
                chromeAndEdge("stretch")),

        /** The style property {@code webkitMaskBoxImageSlice}. */
        WEBKIT_MASK_BOX_IMAGE_SLICE("webkitMaskBoxImageSlice", "webkit-mask-box-image-slice", chromeAndEdge("0 fill")),

        /** The style property {@code webkitMaskBoxImageSource}. */
        WEBKIT_MASK_BOX_IMAGE_SOURCE("webkitMaskBoxImageSource", "webkit-mask-box-image-source", chromeAndEdge("none")),

        /** The style property {@code webkitMaskBoxImageWidth}. */
        WEBKIT_MASK_BOX_IMAGE_WIDTH("webkitMaskBoxImageWidth", "webkit-mask-box-image-width", chromeAndEdge("auto")),

        /** The style property {@code webkitMaskClip}. */
        WEBKIT_MASK_CLIP("webkitMaskClip", "webkit-mask-clip", chromeAndEdge("border-box"), ff("border-box")),

        /** The style property {@code WebkitMaskClip}. */
        WEBKIT_MASK_CLIP_("WebkitMaskClip", "webkit-mask-clip", ff("border-box")),

        /** The style property {@code -webkit-mask-clip}. */
        WEBKIT_MASK_CLIP__("-webkit-mask-clip", "webkit-mask-clip", ff("border-box")),

        /** The style property {@code webkitMaskComposite}. */
        WEBKIT_MASK_COMPOSITE("webkitMaskComposite", "webkit-mask-composite", chromeAndEdge("source-over"), ff("add")),

        /** The style property {@code WebkitMaskComposite}. */
        WEBKIT_MASK_COMPOSITE_("WebkitMaskComposite", "webkit-mask-composite", ff("add")),

        /** The style property {@code -webkit-mask-composite}. */
        WEBKIT_MASK_COMPOSITE__("-webkit-mask-composite", "webkit-mask-composite", ff("add")),

        /** The style property {@code webkitMaskImage}. */
        WEBKIT_MASK_IMAGE("webkitMaskImage", "webkit-mask-image", chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitMaskImage}. */
        WEBKIT_MASK_IMAGE_("WebkitMaskImage", "webkit-mask-image", ff("none")),

        /** The style property {@code -webkit-mask-image}. */
        WEBKIT_MASK_IMAGE__("-webkit-mask-image", "webkit-mask-image", ff("none")),

        /** The style property {@code webkitMaskOrigin}. */
        WEBKIT_MASK_ORIGIN("webkitMaskOrigin", "webkit-mask-origin", chromeAndEdge("border-box"), ff("border-box")),

        /** The style property {@code WebkitMaskOrigin}. */
        WEBKIT_MASK_ORIGIN_("WebkitMaskOrigin", "webkit-mask-origin", ff("border-box")),

        /** The style property {@code -webkit-mask-origin}. */
        WEBKIT_MASK_ORIGIN__("-webkit-mask-origin", "webkit-mask-origin", ff("border-box")),

        /** The style property {@code webkitMaskPosition}. */
        WEBKIT_MASK_POSITION("webkitMaskPosition", "webkit-mask-position", chromeAndEdge("0% 0%"), ff("0% 0%")),

        /** The style property {@code WebkitMaskPosition}. */
        WEBKIT_MASK_POSITION_("WebkitMaskPosition", "webkit-mask-position", ff("0% 0%")),

        /** The style property {@code -webkit-mask-position}. */
        WEBKIT_MASK_POSITION__("-webkit-mask-position", "webkit-mask-position", ff("0% 0%")),

        /** The style property {@code webkitMaskPositionX}. */
        WEBKIT_MASK_POSITION_X("webkitMaskPositionX", "webkit-mask-position-x", chromeAndEdge("0%"), ff("0%")),

        /** The style property {@code WebkitMaskPositionX}. */
        WEBKIT_MASK_POSITION_X_("WebkitMaskPositionX", "webkit-mask-position-x", ff("0%")),

        /** The style property {@code -webkit-mask-position-x}. */
        WEBKIT_MASK_POSITION_X__("-webkit-mask-position-x", "webkit-mask-position-x", ff("0%")),

        /** The style property {@code webkitMaskPositionY}. */
        WEBKIT_MASK_POSITION_Y("webkitMaskPositionY", "webkit-mask-position-y", chromeAndEdge("0%"), ff("0%")),

        /** The style property {@code WebkitMaskPositionY}. */
        WEBKIT_MASK_POSITION_Y_("WebkitMaskPositionY", "webkit-mask-position-y", ff("0%")),

        /** The style property {@code -webkit-mask-position-y}. */
        WEBKIT_MASK_POSITION_Y__("-webkit-mask-position-y", "webkit-mask-position-y", ff("0%")),

        /** The style property {@code webkitMaskRepeat}. */
        WEBKIT_MASK_REPEAT("webkitMaskRepeat", "webkit-mask-repeat", chromeAndEdge("repeat"), ff("repeat")),

        /** The style property {@code WebkitMaskRepeat}. */
        WEBKIT_MASK_REPEAT_("WebkitMaskRepeat", "webkit-mask-repeat", ff("repeat")),

        /** The style property {@code -webkit-mask-repeat}. */
        WEBKIT_MASK_REPEAT__("-webkit-mask-repeat", "webkit-mask-repeat", ff("repeat")),

        /** The style property {@code webkitMaskRepeatX}. */
        WEBKIT_MASK_REPEAT_X("webkitMaskRepeatX", "webkit-mask-repeat-x", chromeAndEdge("")),

        /** The style property {@code webkitMaskRepeatY}. */
        WEBKIT_MASK_REPEAT_Y("webkitMaskRepeatY", "webkit-mask-repeat-y", chromeAndEdge("")),

        /** The style property {@code webkitMaskSize}. */
        WEBKIT_MASK_SIZE("webkitMaskSize", "webkit-mask-size", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code WebkitMaskSize}. */
        WEBKIT_MASK_SIZE_("WebkitMaskSize", "webkit-mask-size", ff("auto")),

        /** The style property {@code -webkit-mask-size}. */
        WEBKIT_MASK_SIZE__("-webkit-mask-size", "webkit-mask-size", ff("auto")),

        /** The style property {@code webkitMaxLogicalHeight}. */
        WEBKIT_MAX_LOGICAL_HEIGHT("webkitMaxLogicalHeight", "webkit-max-logical-height", chromeAndEdge("none")),

        /** The style property {@code webkitMaxLogicalWidth}. */
        WEBKIT_MAX_LOGICAL_WIDTH("webkitMaxLogicalWidth", "webkit-max-logical-width", chromeAndEdge("none")),

        /** The style property {@code webkitMinLogicalHeight}. */
        WEBKIT_MIN_LOGICAL_HEIGHT("webkitMinLogicalHeight", "webkit-min-logical-height", chromeAndEdge("0px")),

        /** The style property {@code webkitMinLogicalWidth}. */
        WEBKIT_MIN_LOGICAL_WIDTH("webkitMinLogicalWidth", "webkit-min-logical-width", chromeAndEdge("0px")),

        /** The style property {@code webkitOpacity}. */
        WEBKIT_OPACITY("webkitOpacity", "webkit-opacity", chromeAndEdge("1")),

        /** The style property {@code webkitOrder}. */
        WEBKIT_ORDER("webkitOrder", "webkit-order", chromeAndEdge("0"), ff("0")),

        /** The style property {@code WebkitOrder}. */
        WEBKIT_ORDER_("WebkitOrder", "webkit-order", ff("0")),

        /** The style property {@code -webkit-order}. */
        WEBKIT_ORDER__("-webkit-order", "webkit-order", ff("0")),

        /** The style property {@code webkitPaddingAfter}. */
        WEBKIT_PADDING_AFTER("webkitPaddingAfter", "webkit-padding-after", chromeAndEdge("0px")),

        /** The style property {@code webkitPaddingBefore}. */
        WEBKIT_PADDING_BEFORE("webkitPaddingBefore", "webkit-padding-before", chromeAndEdge("0px")),

        /** The style property {@code webkitPaddingEnd}. */
        WEBKIT_PADDING_END("webkitPaddingEnd", "webkit-padding-end", chromeAndEdge("0px")),

        /** The style property {@code webkitPaddingStart}. */
        WEBKIT_PADDING_START("webkitPaddingStart", "webkit-padding-start", chromeAndEdge("0px")),

        /** The style property {@code webkitPerspective}. */
        WEBKIT_PERSPECTIVE("webkitPerspective", "webkit-perspective", chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitPerspective}. */
        WEBKIT_PERSPECTIVE_("WebkitPerspective", "webkit-perspective", ff("none")),

        /** The style property {@code -webkit-perspective}. */
        WEBKIT_PERSPECTIVE__("-webkit-perspective", "webkit-perspective", ff("none")),

        /** The style property {@code webkitPerspectiveOrigin}. */
        WEBKIT_PERSPECTIVE_ORIGIN("webkitPerspectiveOrigin", "webkit-perspective-origin",
                chromeAndEdge("620px 161px"), ff("622px 171.5px")),

        /** The style property {@code WebkitPerspectiveOrigin}. */
        WEBKIT_PERSPECTIVE_ORIGIN_("WebkitPerspectiveOrigin", "webkit-perspective-origin",
                ff("622px 171.5px")),

        /** The style property {@code -webkit-perspective-origin}. */
        WEBKIT_PERSPECTIVE_ORIGIN__("-webkit-perspective-origin", "webkit-perspective-origin",
                ff("622px 171.5px")),

        /** The style property {@code webkitPerspectiveOriginX}. */
        WEBKIT_PERSPECTIVE_ORIGIN_X("webkitPerspectiveOriginX", "webkit-perspective-origin-x", chromeAndEdge("")),

        /** The style property {@code webkitPerspectiveOriginY}. */
        WEBKIT_PERSPECTIVE_ORIGIN_Y("webkitPerspectiveOriginY", "webkit-perspective-origin-y", chromeAndEdge("")),

        /** The style property {@code webkitPrintColorAdjust}. */
        WEBKIT_PRINT_COLOR_ADJUST("webkitPrintColorAdjust", "webkit-print-color-adjust", chromeAndEdge("economy")),

        /** The style property {@code webkitRtlOrdering}. */
        WEBKIT_RTL_ORDERING("webkitRtlOrdering", "webkit-rtl-ordering", chromeAndEdge("logical")),

        /** The style property {@code webkitRubyPosition}. */
        WEBKIT_RUBY_POSITION("webkitRubyPosition", "webkit-ruby-position", chromeAndEdge("before")),

        /** The style property {@code webkitShapeImageThreshold}. */
        WEBKIT_SHAPE_IMAGE_THRESHOLD("webkitShapeImageThreshold", "webkit-shape-image-threshold", chromeAndEdge("0")),

        /** The style property {@code webkitShapeMargin}. */
        WEBKIT_SHAPE_MARGIN("webkitShapeMargin", "webkit-shape-margin", chromeAndEdge("0px")),

        /** The style property {@code webkitShapeOutside}. */
        WEBKIT_SHAPE_OUTSIDE("webkitShapeOutside", "webkit-shape-outside", chromeAndEdge("none")),

        /** The style property {@code webkitTapHighlightColor}. */
        WEBKIT_TAP_HIGHLIGHT_COLOR("webkitTapHighlightColor", "webkit-tap-highlight-color",
                chromeAndEdge("rgba(0, 0, 0, 0.18)")),

        /** The style property {@code webkitTextCombine}. */
        WEBKIT_TEXT_COMBINE("webkitTextCombine", "webkit-text-combine", chromeAndEdge("none")),

        /** The style property {@code webkitTextDecorationsInEffect}. */
        WEBKIT_TEXT_DECORATIONS_IN_EFFECT("webkitTextDecorationsInEffect", "webkit-text-decorations-in-effect",
                chromeAndEdge("none")),

        /** The style property {@code webkitTextEmphasis}. */
        WEBKIT_TEXT_EMPHASIS("webkitTextEmphasis", "webkit-text-emphasis", chromeAndEdge("")),

        /** The style property {@code webkitTextEmphasisColor}. */
        WEBKIT_TEXT_EMPHASIS_COLOR("webkitTextEmphasisColor", "webkit-text-emphasis-color",
                chromeAndEdge("rgb(0, 0, 0)")),

        /** The style property {@code webkitTextEmphasisPosition}. */
        WEBKIT_TEXT_EMPHASIS_POSITION("webkitTextEmphasisPosition", "webkit-text-emphasis-position",
                chromeAndEdge("over right")),

        /** The style property {@code webkitTextEmphasisStyle}. */
        WEBKIT_TEXT_EMPHASIS_STYLE("webkitTextEmphasisStyle", "webkit-text-emphasis-style",
                chromeAndEdge("none")),

        /** The style property {@code webkitTextFillColor}. */
        WEBKIT_TEXT_FILL_COLOR("webkitTextFillColor", "webkit-text-fill-color",
                chromeAndEdge("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)")),

        /** The style property {@code WebkitTextFillColor}. */
        WEBKIT_TEXT_FILL_COLOR_("WebkitTextFillColor", "webkit-text-fill-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code -webkit-text-fill-color}. */
        WEBKIT_TEXT_FILL_COLOR__("-webkit-text-fill-color", "webkit-text-fill-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code webkitTextOrientation}. */
        WEBKIT_TEXT_ORIENTATION("webkitTextOrientation", "webkit-text-orientation", chromeAndEdge("vertical-right")),

        /** The style property {@code webkitTextSecurity}. */
        WEBKIT_TEXT_SECURITY("webkitTextSecurity", "webkit-text-security", chromeAndEdge("none")),

        /** The style property {@code webkitTextSizeAdjust}. */
        WEBKIT_TEXT_SIZE_ADJUST("webkitTextSizeAdjust", "webkit-text-size-adjust", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code WebkitTextSizeAdjust}. */
        WEBKIT_TEXT_SIZE_ADJUST_("WebkitTextSizeAdjust", "webkit-text-size-adjust", ff("auto")),

        /** The style property {@code -webkit-text-size-adjust}. */
        WEBKIT_TEXT_SIZE_ADJUST__("-webkit-text-size-adjust", "webkit-text-size-adjust", ff("auto")),

        /** The style property {@code webkitTextStroke}. */
        WEBKIT_TEXT_STROKE("webkitTextStroke", "webkit-text-stroke", chromeAndEdge(""), ff("")),

        /** The style property {@code WebkitTextStroke}. */
        WEBKIT_TEXT_STROKE_("WebkitTextStroke", "webkit-text-stroke", ff("")),

        /** The style property {@code -webkit-text-stroke}. */
        WEBKIT_TEXT_STROKE__("-webkit-text-stroke", "webkit-text-stroke", ff("")),

        /** The style property {@code webkitTextStrokeColor}. */
        WEBKIT_TEXT_STROKE_COLOR("webkitTextStrokeColor", "webkit-text-stroke-color",
                chromeAndEdge("rgb(0, 0, 0)"), ff("rgb(0, 0, 0)")),

        /** The style property {@code WebkitTextStrokeColor}. */
        WEBKIT_TEXT_STROKE_COLOR_("WebkitTextStrokeColor", "webkit-text-stroke-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code -webkit-text-stroke-color}. */
        WEBKIT_TEXT_STROKE_COLOR__("-webkit-text-stroke-color", "webkit-text-stroke-color",
                ff("rgb(0, 0, 0)")),

        /** The style property {@code webkitTextStrokeWidth}. */
        WEBKIT_TEXT_STROKE_WIDTH("webkitTextStrokeWidth", "webkit-text-stroke-width", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code WebkitTextStrokeWidth}. */
        WEBKIT_TEXT_STROKE_WIDTH_("WebkitTextStrokeWidth", "webkit-text-stroke-width", ff("0px")),

        /** The style property {@code -webkit-text-stroke-width}. */
        WEBKIT_TEXT_STROKE_WIDTH__("-webkit-text-stroke-width", "webkit-text-stroke-width", ff("0px")),

        /** The style property {@code webkitTransform}. */
        WEBKIT_TRANSFORM("webkitTransform", "webkit-transform", chromeAndEdge("none"), ff("none")),

        /** The style property {@code WebkitTransform}. */
        WEBKIT_TRANSFORM_("WebkitTransform", "webkit-transform", ff("none")),

        /** The style property {@code -webkit-transform}. */
        WEBKIT_TRANSFORM__("-webkit-transform", "webkit-transform", ff("none")),

        /** The style property {@code webkitTransformOrigin}. */
        WEBKIT_TRANSFORM_ORIGIN("webkitTransformOrigin", "webkit-transform-origin",
                chromeAndEdge("620px 161px"), ff("622px 171.5px")),

        /** The style property {@code WebkitTransformOrigin}. */
        WEBKIT_TRANSFORM_ORIGIN_("WebkitTransformOrigin", "webkit-transform-origin",
                ff("622px 171.5px")),

        /** The style property {@code -webkit-transform-origin}. */
        WEBKIT_TRANSFORM_ORIGIN__("-webkit-transform-origin", "webkit-transform-origin",
                ff("622px 171.5px")),

        /** The style property {@code webkitTransformOriginX}. */
        WEBKIT_TRANSFORM_ORIGIN_X("webkitTransformOriginX", "webkit-transform-origin-x", chromeAndEdge("")),

        /** The style property {@code webkitTransformOriginY}. */
        WEBKIT_TRANSFORM_ORIGIN_Y("webkitTransformOriginY", "webkit-transform-origin-y", chromeAndEdge("")),

        /** The style property {@code webkitTransformOriginZ}. */
        WEBKIT_TRANSFORM_ORIGIN_Z("webkitTransformOriginZ", "webkit-transform-origin-z", chromeAndEdge("")),

        /** The style property {@code webkitTransformStyle}. */
        WEBKIT_TRANSFORM_STYLE("webkitTransformStyle", "webkit-transform-style", chromeAndEdge("flat"), ff("flat")),

        /** The style property {@code WebkitTransformStyle}. */
        WEBKIT_TRANSFORM_STYLE_("WebkitTransformStyle", "webkit-transform-style", ff("flat")),

        /** The style property {@code -webkit-transform-style}. */
        WEBKIT_TRANSFORM_STYLE__("-webkit-transform-style", "webkit-transform-style", ff("flat")),

        /** The style property {@code webkitTransition}. */
        WEBKIT_TRANSITION("webkitTransition", "webkit-transition", chromeAndEdge("all 0s ease 0s"), ff("")),

        /** The style property {@code WebkitTransition}. */
        WEBKIT_TRANSITION_("WebkitTransition", "webkit-transition", ff("")),

        /** The style property {@code -webkit-transition}. */
        WEBKIT_TRANSITION__("-webkit-transition", "webkit-transition", ff("")),

        /** The style property {@code webkitTransitionDelay}. */
        WEBKIT_TRANSITION_DELAY("webkitTransitionDelay", "webkit-transition-delay", chromeAndEdge("0s"), ff("0s")),

        /** The style property {@code WebkitTransitionDelay}. */
        WEBKIT_TRANSITION_DELAY_("WebkitTransitionDelay", "webkit-transition-delay", ff("0s")),

        /** The style property {@code -webkit-transition-delay}. */
        WEBKIT_TRANSITION_DELAY__("-webkit-transition-delay", "webkit-transition-delay", ff("0s")),

        /** The style property {@code webkitTransitionDuration}. */
        WEBKIT_TRANSITION_DURATION("webkitTransitionDuration", "webkit-transition-duration",
                chromeAndEdge("0s"), ff("0s")),

        /** The style property {@code WebkitTransitionDuration}. */
        WEBKIT_TRANSITION_DURATION_("WebkitTransitionDuration", "webkit-transition-duration", ff("0s")),

        /** The style property {@code -webkit-transition-duration}. */
        WEBKIT_TRANSITION_DURATION__("-webkit-transition-duration", "webkit-transition-duration", ff("0s")),

        /** The style property {@code webkitTransitionProperty}. */
        WEBKIT_TRANSITION_PROPERTY("webkitTransitionProperty", "webkit-transition-property",
                chromeAndEdge("all"), ff("all")),

        /** The style property {@code WebkitTransitionProperty}. */
        WEBKIT_TRANSITION_PROPERTY_("WebkitTransitionProperty", "webkit-transition-property", ff("all")),

        /** The style property {@code -webkit-transition-property}. */
        WEBKIT_TRANSITION_PROPERTY__("-webkit-transition-property", "webkit-transition-property", ff("all")),

        /** The style property {@code webkitTransitionTimingFunction}. */
        WEBKIT_TRANSITION_TIMING_FUNCTION("webkitTransitionTimingFunction", "webkit-transition-timing-function",
                chromeAndEdge("ease"), ff("ease")),

        /** The style property {@code WebkitTransitionTimingFunction}. */
        WEBKIT_TRANSITION_TIMING_FUNCTION_("WebkitTransitionTimingFunction", "webkit-transition-timing-function",
                ff("ease")),

        /** The style property {@code -webkit-transition-timing-function}. */
        WEBKIT_TRANSITION_TIMING_FUNCTION__("-webkit-transition-timing-function", "webkit-transition-timing-function",
                ff("ease")),

        /** The style property {@code webkitUserDrag}. */
        WEBKIT_USER_DRAG("webkitUserDrag", "webkit-user-drag", chromeAndEdge("auto")),

        /** The style property {@code webkitUserModify}. */
        WEBKIT_USER_MODIFY("webkitUserModify", "webkit-user-modify", chromeAndEdge("read-only")),

        /** The style property {@code webkitUserSelect}. */
        WEBKIT_USER_SELECT("webkitUserSelect", "webkit-user-select", chromeAndEdge("auto"), ff("auto")),

        /** The style property {@code WebkitUserSelect}. */
        WEBKIT_USER_SELECT_("WebkitUserSelect", "webkit-user-select", ff("auto")),

        /** The style property {@code -webkit-user-select}. */
        WEBKIT_USER_SELECT__("-webkit-user-select", "webkit-user-select", ff("auto")),

        /** The style property {@code webkitWritingMode}. */
        WEBKIT_WRITING_MODE("webkitWritingMode", "webkit-writing-mode", chromeAndEdge("horizontal-tb")),

        /** The style property {@code whiteSpace}. */
        WHITE_SPACE("whiteSpace", "white-space", chromeAndEdge("normal"), ff("normal"), ie("normal")),

        /** The style property {@code white-space}. */
        WHITE_SPACE_("white-space", "white-space", ff("normal")),

        /** The style property {@code widows}. */
        WIDOWS("widows", "widows", ie("2"), chromeAndEdge("2")),

        /** The style property {@code width}. */
        WIDTH("width", "width", chromeAndEdge(""), ff(""), ie("")),

        /** The style property {@code willChange}. */
        WILL_CHANGE("willChange", "will-change", ff("auto"), chromeAndEdge("auto")),

        /** The style property {@code will-change}. */
        WILL_CHANGE_("will-change", "will-change", ff("auto")),

        /** The style property {@code wordBreak}. */
        WORD_BREAK("wordBreak", "word-break", ff("normal"), ie("normal"), chromeAndEdge("normal")),

        /** The style property {@code word-break}. */
        WORD_BREAK_("word-break", "word-break", ff("normal")),

        /** The style property {@code wordSpacing}. */
        WORD_SPACING("wordSpacing", "word-spacing", chromeAndEdge("0px"), ff("0px"), ie("0px")),

        /** The style property {@code word-spacing}. */
        WORD_SPACING_("word-spacing", "word-spacing", ff("0px")),

        /** The style property {@code wordWrap}. */
        WORD_WRAP("wordWrap", "word-wrap", ie(""), chromeAndEdge("normal"), ff("normal")),

        /** The style property {@code word-wrap}. */
        WORD_WRAP_("word-wrap", "word-wrap", ff("normal")),

        /** The style property {@code writingMode}. */
        WRITING_MODE("writingMode", "writing-mode", ie("undefined"), chromeAndEdge("horizontal-tb"),
                ff("horizontal-tb")),

        /** The style property {@code writing-mode}. */
        WRITING_MODE_("writing-mode", "writing-mode", ff("horizontal-tb")),

        /** The style property {@code x}. */
        X("x", "x", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code y}. */
        Y("y", "y", chromeAndEdge("0px"), ff("0px")),

        /** The style property {@code zIndex}. */
        Z_INDEX("zIndex", "z-index", ff("auto"), chromeAndEdge("auto"), ie("auto")),

        /** The style property {@code z-index}. */
        Z_INDEX_("z-index", "z-index", ff("auto"), chromeAndEdgeNotIterable("auto"), ieNotIterable("auto")),

        /** The style property {@code zoom}. */
        ZOOM("zoom", "zoom", ie("undefined"), chromeAndEdge("1"));

        private final String propertyName_;
        private final String attributeName_;
        private final BrowserConfiguration[] browserConfigurations_;

        Definition(final String propertyName, final String attributeName,
                final BrowserConfiguration... browserConfigurations) {
            propertyName_ = propertyName;
            attributeName_ = attributeName;
            browserConfigurations_ = browserConfigurations;
        }

        boolean isAvailable(final BrowserVersion browserVersion, final boolean onlyIfIteratable) {
            if (browserConfigurations_ == null) {
                return true; // defined for all browsers
            }

            final BrowserConfiguration config
                = BrowserConfiguration.getMatchingConfiguration(browserVersion, browserConfigurations_);
            return config != null && (!onlyIfIteratable || config.isIteratable());
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

        /**
         * @param browserVersion the browser version
         * @return the default value for this attribute
         */
        public String getDefaultComputedValue(final BrowserVersion browserVersion) {
            final BrowserConfiguration config
                = BrowserConfiguration.getMatchingConfiguration(browserVersion, browserConfigurations_);
            if (config == null) {
                return "";
            }
            return config.getDefaultValue();
        }
    }
}
