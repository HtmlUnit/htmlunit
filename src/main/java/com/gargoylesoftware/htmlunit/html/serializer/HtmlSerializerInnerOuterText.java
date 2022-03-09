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
package com.gargoylesoftware.htmlunit.html.serializer;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_SCRIPT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_SVG_NL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_SVG_TITLE;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlBreak;
import com.gargoylesoftware.htmlunit.html.HtmlDetails;
import com.gargoylesoftware.htmlunit.html.HtmlHead;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlNoFrames;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlSummary;
import com.gargoylesoftware.htmlunit.html.HtmlSvg;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTitle;
import com.gargoylesoftware.htmlunit.html.ScriptElement;
import com.gargoylesoftware.htmlunit.html.serializer.HtmlSerializerInnerOuterText.HtmlSerializerTextBuilder.Mode;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.svg.SvgTitle;

/**
 * Special serializer to generate the output we need
 * for innerText and outerText.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerInnerOuterText {

    private final BrowserVersion browserVersion_;

    public HtmlSerializerInnerOuterText(final BrowserVersion browserVersion) {
        super();
        browserVersion_ = browserVersion;
    }

    /**
     * Converts an HTML node to text.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     */
    public String asText(final DomNode node) {
        if (node instanceof HtmlBreak) {
            return "";
        }

        // included scripts are ignored, but if we ask for the script itself....
        if (node instanceof ScriptElement) {
            final HtmlSerializerTextBuilder builder = new HtmlSerializerTextBuilder();
            appendChildren(builder, node, Mode.WHITE_SPACE_NORMAL, false);
            return builder.getText();
        }

        // when calling on the title itself we have to output
        final boolean insideHead = node instanceof HtmlTitle;

        final HtmlSerializerTextBuilder builder = new HtmlSerializerTextBuilder();
        appendNode(builder, node, whiteSpaceStyle(node, Mode.WHITE_SPACE_NORMAL), insideHead);
        return builder.getText();
    }

    /**
     * Iterate over all Children and call appendNode() for every.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     */
    protected void appendChildren(final HtmlSerializerTextBuilder builder, final DomNode node,
            final Mode mode, final boolean insideHead) {
        for (final DomNode child : node.getChildren()) {
            appendNode(builder, child, mode, insideHead);
        }
    }

    /**
     * The core distribution method call the different appendXXX
     * methods depending on the type of the given node.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     */
    protected void appendNode(final HtmlSerializerTextBuilder builder, final DomNode node,
            final Mode mode, final boolean insideHead) {
        if (node instanceof DomText) {
            appendText(builder, (DomText) node, mode);
        }
        else if (node instanceof HtmlBreak) {
            appendBreak(builder, (HtmlBreak) node);
        }
        else if (node instanceof HtmlParagraph) {
            appendParagraph(builder, (HtmlParagraph) node, mode, insideHead);
        }
        else if (node instanceof HtmlListItem) {
            appendListItem(builder, (HtmlListItem) node, mode, insideHead);
        }
        else if (node instanceof HtmlDetails) {
            appendDetails(builder, (HtmlDetails) node, mode, insideHead);
        }
        else if (node instanceof HtmlHead) {
            appendChildren(builder, node, mode, true);
        }
        else if (node instanceof HtmlNoFrames) {
            appendChildren(builder, node, Mode.PLAIN, insideHead);
        }
        else if (node instanceof HtmlTitle && !insideHead) {
            // nothing to do
        }
        else if (node instanceof HtmlTextArea) {
            // nothing to do
        }
        else if (node instanceof ScriptElement) {
            if (insideHead || browserVersion_.hasFeature(JS_INNER_TEXT_SCRIPT)) {
                appendChildren(builder, node, mode, insideHead);
            }
        }
        else if (node instanceof HtmlSvg) {
            if (browserVersion_.hasFeature(JS_INNER_TEXT_SVG_NL)) {
                builder.appendRequiredLineBreak();
                appendChildren(builder, node, mode, insideHead);
                builder.appendRequiredLineBreak();
            }
            else {
                appendChildren(builder, node, mode, insideHead);
            }
        }
        else if (node instanceof SvgTitle) {
            if (browserVersion_.hasFeature(JS_INNER_TEXT_SVG_TITLE)) {
                appendChildren(builder, node, mode, insideHead);
            }
        }
        else {
            appendChildren(builder, node, mode, insideHead);
        }
    }

    /**
     * Process {@link DomText}.
     *
     * @param builder the StringBuilder to add to
     * @param domText the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendText(final HtmlSerializerTextBuilder builder, final DomText domText, final Mode mode) {
        final DomNode parent = domText.getParentNode();
        if (parent instanceof HtmlTitle
                || parent instanceof HtmlStyle
                || parent instanceof HtmlScript) {
            builder.append(domText.getData(), Mode.PLAIN);
            return;
        }

        if (parent == null
                || parent instanceof HtmlNoFrames
                || parent.isDisplayed()) {
            builder.append(domText.getData(), mode);
        }
    }

    /**
     * Process {@link HtmlBreak}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlBreak the target to process
     */
    protected void appendBreak(final HtmlSerializerTextBuilder builder,
            final HtmlBreak htmlBreak) {
        builder.appendRequiredLineBreak();
    }

    /**
     * Process {@link HtmlListItem}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlParagraph the target to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     */
    protected void appendParagraph(final HtmlSerializerTextBuilder builder,
            final HtmlParagraph htmlParagraph, final Mode mode, final boolean insideHead) {
        builder.appendRequiredLineBreak();
        appendChildren(builder, htmlParagraph, mode, insideHead);
        builder.appendRequiredLineBreak();
    }

    /**
     * Process {@link HtmlListItem}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlListItem the target to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     */
    protected void appendListItem(final HtmlSerializerTextBuilder builder,
            final HtmlListItem htmlListItem, final Mode mode, final boolean insideHead) {
        builder.appendRequiredLineBreak();
        appendChildren(builder, htmlListItem, mode, insideHead);
        builder.appendRequiredLineBreak();
    }

    /**
     * Process {@link HtmlDetails}.
     * @param builder the StringBuilder to add to
     * @param htmlDetails the target to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     */
    protected void appendDetails(final HtmlSerializerTextBuilder builder,
                    final HtmlDetails htmlDetails, final Mode mode, final boolean insideHead) {
        if (htmlDetails.isOpen()) {
            appendChildren(builder, htmlDetails, mode, insideHead);
            return;
        }

        for (final DomNode child : htmlDetails.getChildren()) {
            if (child instanceof HtmlSummary) {
                appendNode(builder, child, mode, insideHead);
            }
        }
    }

    private static Mode whiteSpaceStyle(final DomNode domNode, final Mode defaultMode) {
        if (domNode instanceof DomElement) {
            final Page page = domNode.getPage();
            if (page != null) {
                final WebWindow window = page.getEnclosingWindow();
                if (window.getWebClient().getOptions().isCssEnabled()) {
                    DomNode node = domNode;
                    while (node != null) {
                        if (node instanceof DomElement) {
                            final ComputedCSSStyleDeclaration style =
                                    window.getComputedStyle((DomElement) domNode, null);
                            final String value = style.getStyleAttribute(Definition.WHITE_SPACE, false);
                            if (StringUtils.isNoneEmpty(value)) {
                                if ("normal".equalsIgnoreCase(value)) {
                                    return Mode.WHITE_SPACE_NORMAL;
                                }
                                if ("nowrap".equalsIgnoreCase(value)) {
                                    return Mode.WHITE_SPACE_NORMAL;
                                }
                                if ("pre".equalsIgnoreCase(value)) {
                                    return Mode.WHITE_SPACE_PRE;
                                }
                                if ("pre-wrap".equalsIgnoreCase(value)) {
                                    return Mode.WHITE_SPACE_PRE;
                                }
                                if ("pre-line".equalsIgnoreCase(value)) {
                                    return Mode.WHITE_SPACE_PRE_LINE;
                                }
                            }
                        }
                        node = node.getParentNode();
                    }
                }
            }
        }
        return defaultMode;
    }

    protected static class HtmlSerializerTextBuilder {
        /** Mode. */
        protected enum Mode {
            /**
             * The mode for plain.
             */
            PLAIN,

            /**
             * Sequences of white space are collapsed. Newline characters
             * in the source are handled the same as other white space.
             * Lines are broken as necessary to fill line boxes.
             */
            WHITE_SPACE_NORMAL,

            /**
             * Sequences of white space are preserved. Lines are only broken
             * at newline characters in the source and at <br> elements.
             */
            WHITE_SPACE_PRE,

            /**
             * Sequences of white space are collapsed. Lines are broken
             * at newline characters, at <br>, and as necessary
             * to fill line boxes.
             */
            WHITE_SPACE_PRE_LINE
        }

        private enum State {
            DEFAULT,
            EMPTY,
            BLANK_AT_END,
            BLANK_AT_END_AFTER_NEWLINE,
            NEWLINE_AT_END,
            BREAK_AT_END,
            BLOCK_SEPARATOR_AT_END,
            REQUIRED_LINE_BREAK_AT_END
        }

        private State state_;
        private final StringBuilder builder_;
        private int trimRightPos_;

        public HtmlSerializerTextBuilder() {
            builder_ = new StringBuilder();
            state_ = State.EMPTY;
            trimRightPos_ = 0;
        }

        public void appendRequiredLineBreak() {
            if (state_ == State.EMPTY) {
                return;
            }

            // trimRight
            builder_.setLength(trimRightPos_);
            if (trimRightPos_ == 0) {
                state_ = State.EMPTY;
            }

            builder_.append('\n');
            state_ = State.REQUIRED_LINE_BREAK_AT_END;
        }

        // see https://drafts.csswg.org/css-text-3/#white-space
        public void append(final String content, final Mode mode) {
            int length = content.length();
            if (length == 0) {
                return;
            }

            if (mode == Mode.PLAIN) {
                builder_.append(content);
                state_ = State.DEFAULT;
                trimRightPos_ = builder_.length();
                return;
            }

            length--;
            int i = -1;
            for (char c : content.toCharArray()) {
                i++;

                // handle \r
                if (c == '\r') {
                    if (length != i) {
                        continue;
                    }
                    c = '\n';
                }

                if (c == '\n') {
                    if (mode == Mode.WHITE_SPACE_PRE) {
                        switch (state_) {
                            case EMPTY:
                            case BLOCK_SEPARATOR_AT_END:
                                break;
                            default:
                                builder_.append('\n');
                                state_ = State.NEWLINE_AT_END;
                                trimRightPos_ = builder_.length();
                                break;
                        }
                        continue;
                    }

                    if (mode == Mode.WHITE_SPACE_PRE_LINE) {
                        switch (state_) {
                            case EMPTY:
                            case BLOCK_SEPARATOR_AT_END:
                                break;
                            case BLANK_AT_END:
                                builder_.setLength(trimRightPos_);
                                builder_.append('\n');
                                state_ = State.NEWLINE_AT_END;
                                trimRightPos_ = builder_.length();
                                break;
                            default:
                                builder_.append('\n');
                                state_ = State.NEWLINE_AT_END;
                                trimRightPos_ = builder_.length();
                                break;
                        }
                        continue;
                    }

                    switch (state_) {
                        case EMPTY:
                        case BLANK_AT_END:
                        case BLANK_AT_END_AFTER_NEWLINE:
                        case BLOCK_SEPARATOR_AT_END:
                        case NEWLINE_AT_END:
                        case BREAK_AT_END:
                        case REQUIRED_LINE_BREAK_AT_END:
                            break;
                        default:
                            builder_.append(' ');
                            state_ = State.BLANK_AT_END;
                            break;
                    }
                    continue;
                }

                if (c == ' ' || c == '\t' || c == '\f') {
                    if (mode == Mode.WHITE_SPACE_PRE) {
                        if (c == '\t') {
                            builder_.append('\t');
                        }
                        else {
                            builder_.append(' ');
                        }
                        state_ = State.BLANK_AT_END;
                        trimRightPos_ = builder_.length();

                        continue;
                    }

                    if (mode == Mode.WHITE_SPACE_PRE_LINE) {
                        switch (state_) {
                            case EMPTY:
                            case BLANK_AT_END:
                            case BLANK_AT_END_AFTER_NEWLINE:
                            case BREAK_AT_END:
                            case NEWLINE_AT_END:
                                break;
                            default:
                                builder_.append(' ');
                                state_ = State.BLANK_AT_END;
                                break;
                        }
                        continue;
                    }

                    switch (state_) {
                        case EMPTY:
                        case BLANK_AT_END:
                        case BLANK_AT_END_AFTER_NEWLINE:
                        case BLOCK_SEPARATOR_AT_END:
                        case NEWLINE_AT_END:
                        case BREAK_AT_END:
                        case REQUIRED_LINE_BREAK_AT_END:
                            break;
                        default:
                            builder_.append(' ');
                            state_ = State.BLANK_AT_END;
                            break;
                    }
                    continue;
                }

                builder_.append(c);
                state_ = State.DEFAULT;
                trimRightPos_ = builder_.length();
            }
        }

        public String getText() {
            return builder_.substring(0, trimRightPos_);
        }
    }
}
