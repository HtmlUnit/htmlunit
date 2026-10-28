/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html.serializer;

import static org.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_SELECT_EMPTY;
import static org.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_SVG_NL;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebWindow;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.css.StyleAttributes.Definition;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.HtmlAudio;
import org.htmlunit.html.HtmlBreak;
import org.htmlunit.html.HtmlCanvas;
import org.htmlunit.html.HtmlDefinitionTerm;
import org.htmlunit.html.HtmlDetails;
import org.htmlunit.html.HtmlDialog;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlExample;
import org.htmlunit.html.HtmlHead;
import org.htmlunit.html.HtmlInlineFrame;
import org.htmlunit.html.HtmlListItem;
import org.htmlunit.html.HtmlListing;
import org.htmlunit.html.HtmlMeter;
import org.htmlunit.html.HtmlNoFrames;
import org.htmlunit.html.HtmlOption;
import org.htmlunit.html.HtmlParagraph;
import org.htmlunit.html.HtmlPlainText;
import org.htmlunit.html.HtmlPreformattedText;
import org.htmlunit.html.HtmlProgress;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.html.HtmlSelect;
import org.htmlunit.html.HtmlStyle;
import org.htmlunit.html.HtmlSummary;
import org.htmlunit.html.HtmlSvg;
import org.htmlunit.html.HtmlTableCell;
import org.htmlunit.html.HtmlTableRow;
import org.htmlunit.html.HtmlTextArea;
import org.htmlunit.html.HtmlTitle;
import org.htmlunit.html.HtmlVideo;
import org.htmlunit.html.ScriptElement;
import org.htmlunit.html.serializer.HtmlSerializerInnerOuterText.HtmlSerializerTextBuilder.Mode;
import org.htmlunit.svg.SvgTitle;
import org.htmlunit.util.StringUtils;

/**
 * Special serializer to generate the output we need
 * for innerText and outerText.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerInnerOuterText {

    private final BrowserVersion browserVersion_;
    private DomNode root_;

    /**
     * Ctor.
     *
     * @param browserVersion the {@link BrowserVersion}
     */
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
        root_ = node;
        if (node instanceof HtmlBreak) {
            return "";
        }

        // included scripts are ignored, but if we ask for the script itself....
        if (node instanceof ScriptElement) {
            final HtmlSerializerTextBuilder builder = new HtmlSerializerTextBuilder();
            appendChildren(builder, node, Mode.WHITE_SPACE_NORMAL, false, false);
            return builder.getText();
        }

        // an element that is not rendered (and an option) returns the text content
        if (node instanceof HtmlOption
                || (node instanceof HtmlElement && !(node instanceof HtmlTitle) && isDisplayNone(node))) {
            return node.getTextContent();
        }

        // when calling on the title itself we have to output
        final boolean insideHead = node instanceof HtmlTitle;

        final HtmlSerializerTextBuilder builder = new HtmlSerializerTextBuilder();
        appendNode(builder, node, whiteSpaceStyle(node, Mode.WHITE_SPACE_NORMAL), insideHead, false);
        return builder.getText();
    }

    /**
     * Iterate over all Children and call appendNode() for every.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendChildren(final HtmlSerializerTextBuilder builder, final DomNode node,
            final Mode mode, final boolean insideHead, final boolean insideSelect) {
        for (final DomNode child : node.getChildren()) {
            appendNode(builder, child, mode, insideHead, insideSelect);
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
     * @param insideSelect true if inside a select
     */
    protected void appendNode(final HtmlSerializerTextBuilder builder, final DomNode node,
            final Mode mode, final boolean insideHead, final boolean insideSelect) {
        if (insideSelect && node instanceof HtmlBreak) {
            return;
        }

        if (node instanceof DomText text) {
            appendText(builder, text, mode);
        }
        else if (node instanceof HtmlBreak break1) {
            appendBreak(builder, break1);
        }
        else if (node instanceof HtmlParagraph paragraph) {
            appendParagraph(builder, paragraph, mode, insideHead, insideSelect);
        }
        else if (node instanceof HtmlListItem item) {
            appendListItem(builder, item, mode, insideHead, insideSelect);
        }
        else if (node instanceof HtmlPreformattedText
                || node instanceof HtmlListing
                || node instanceof HtmlExample
                || node instanceof HtmlPlainText) {
            appendPreformattedText(builder, (HtmlElement) node, insideHead, insideSelect);
        }
        else if (node instanceof HtmlCanvas
                || node instanceof HtmlMeter
                || node instanceof HtmlProgress
                || node instanceof HtmlInlineFrame
                || node instanceof HtmlVideo
                || node instanceof HtmlAudio) {
            // the fallback content is not rendered
        }
        else if (node instanceof HtmlDetails details) {
            appendDetails(builder, details, mode, insideHead, insideSelect);
        }
        else if (node instanceof HtmlHead) {
            appendChildren(builder, node, mode, true, insideSelect);
        }
        else if (node instanceof HtmlNoFrames) {
            appendChildren(builder, node, Mode.PLAIN, insideHead, insideSelect);
        }
        else if (node instanceof HtmlTitle && !insideHead) {
            // nothing to do
        }
        else if (node instanceof HtmlTextArea) {
            // nothing to do
        }
        else if (node instanceof ScriptElement) {
            if (insideHead) {
                appendChildren(builder, node, mode, insideHead, insideSelect);
            }
        }
        else if (node instanceof HtmlSelect select) {
            appendSelect(builder, select, mode, insideHead, insideSelect);
        }
        else if (node instanceof HtmlDefinitionTerm item) {
            appendDefinitionTerm(builder, item, mode, insideHead, insideSelect);
        }
        else if (node instanceof HtmlSvg) {
            if (browserVersion_.hasFeature(JS_INNER_TEXT_SVG_NL)) {
                builder.appendRequiredLineBreak();
                appendChildren(builder, node, mode, insideHead, insideSelect);
                builder.appendRequiredLineBreak();
            }
            else {
                appendChildren(builder, node, mode, insideHead, insideSelect);
            }
        }
        else if (node instanceof SvgTitle) {
            // nothing to do
        }
        else {
            appendElement(builder, node, mode, insideHead, insideSelect);
        }
    }

    /**
     * Process all other nodes; block level elements are surrounded
     * by a required line break and elements with an own white-space style
     * switch the mode for their children.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendElement(final HtmlSerializerTextBuilder builder, final DomNode node,
            final Mode mode, final boolean insideHead, final boolean insideSelect) {
        if (!(node instanceof HtmlElement) || insideHead || insideSelect) {
            appendChildren(builder, node, mode, insideHead, insideSelect);
            return;
        }

        final HtmlElement element = (HtmlElement) node;
        if (isNotRendered(element)) {
            return;
        }

        final Mode childMode = mode == Mode.PLAIN ? mode : whiteSpaceStyle(node, mode);
        final boolean visible = isVisible(element);
        final boolean block = visible && isBlockLevel(element);
        if (block) {
            builder.appendRequiredLineBreak();
        }
        appendChildren(builder, node, childMode, insideHead, insideSelect);
        if (!visible) {
            return;
        }
        if (block) {
            builder.appendRequiredLineBreak();
        }
        else if (isTableCell(element)) {
            // cells of a row are separated by a tab
            final DomElement next = element.getNextElementSibling();
            if (next instanceof HtmlElement nextElement && isTableCell(nextElement)) {
                builder.appendTab();
            }
        }
        else if (node instanceof HtmlTableRow || "table-row".equals(display(element))) {
            builder.appendRequiredLineBreak();
        }
    }

    /**
     * An element with display none (or the hidden attribute) does not contribute
     * to the text; this is not true for the element the text was requested for.
     *
     * @param element the element to check
     * @return true if the element (and its children) has to be ignored
     */
    private boolean isNotRendered(final HtmlElement element) {
        if (element == root_) {
            return false;
        }
        if (element.isHidden()) {
            return true;
        }
        if (element instanceof HtmlDialog dialog) {
            return !dialog.isOpen();
        }
        return "none".equals(display(element));
    }

    /**
     * Checks if the given node or one of its ancestors is not rendered because
     * of the hidden attribute or the display style 'none'.
     *
     * @param node the node to check
     * @return true if the node or one of the ancestors has display none
     */
    private static boolean isDisplayNone(final DomNode node) {
        DomNode current = node;
        while (current != null) {
            if (current instanceof HtmlElement element) {
                if (element.isHidden() || "none".equals(display(element))) {
                    return true;
                }
            }
            current = current.getParentNode();
        }
        return false;
    }

    /**
     * An element with visibility hidden still contributes the text of visible children,
     * but no line breaks.
     *
     * @param element the element to check
     * @return true if the element is the root or displayed
     */
    private boolean isVisible(final HtmlElement element) {
        return element == root_ || element.isDisplayed();
    }

    /**
     * Determines the display value of the given element.
     *
     * @param element the element to check
     * @return the computed display of the element (or the default display if css is disabled)
     */
    private static String display(final HtmlElement element) {
        final SgmlPage page = element.getPage();
        final WebWindow window = page == null ? null : page.getEnclosingWindow();
        if (window != null && window.getWebClient().getOptions().isCssEnabled()) {
            return window.getComputedStyle(element, null).getDisplay();
        }
        return element.getDefaultStyleDisplay().value();
    }

    /**
     * Checks if the given element is a table cell (td/th or display 'table-cell').
     *
     * @param element the element to check
     * @return true if the element is a table cell
     */
    private static boolean isTableCell(final HtmlElement element) {
        return element instanceof HtmlTableCell || "table-cell".equals(display(element));
    }

    /**
     * Checks if the given element is block level; this includes elements that are
     * blockified (open dialog, absolute positioned, floating, children of flex and grid containers).
     *
     * @param element the element to check
     * @return true if the element is block level
     */
    private static boolean isBlockLevel(final HtmlElement element) {
        if (element instanceof HtmlDialog dialog && dialog.isOpen()) {
            return true;
        }

        final String display = display(element);
        if ("block".equals(display)
                || "flex".equals(display)
                || "grid".equals(display)
                || "flow-root".equals(display)
                || "list-item".equals(display)
                || "table".equals(display)
                || "table-caption".equals(display)) {
            return true;
        }
        if ("none".equals(display) || "contents".equals(display)) {
            return false;
        }

        final SgmlPage page = element.getPage();
        final WebWindow window = page == null ? null : page.getEnclosingWindow();
        if (window == null || !window.getWebClient().getOptions().isCssEnabled()) {
            return false;
        }

        // absolute positioned and floating boxes are blockified
        final ComputedCssStyleDeclaration style = window.getComputedStyle(element, null);
        final String position = style.getPosition();
        if ("absolute".equals(position) || "fixed".equals(position)) {
            return true;
        }
        final String floatValue = style.getStyleAttribute(Definition.FLOAT, false);
        if ("left".equals(floatValue) || "right".equals(floatValue)) {
            return true;
        }

        // children of flex and grid containers are blockified
        if (element.getParentNode() instanceof HtmlElement parent) {
            final String parentDisplay = display(parent);
            return "flex".equals(parentDisplay) || "inline-flex".equals(parentDisplay)
                    || "grid".equals(parentDisplay) || "inline-grid".equals(parentDisplay);
        }
        return false;
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
        builder.appendLineBreak();
    }

    /**
     * Process {@link HtmlParagraph}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlParagraph the target to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendParagraph(final HtmlSerializerTextBuilder builder,
            final HtmlParagraph htmlParagraph, final Mode mode, final boolean insideHead, final boolean insideSelect) {
        if (isNotRendered(htmlParagraph)) {
            return;
        }
        // a paragraph is surrounded by a blank line, independent of the display style
        final boolean visible = isVisible(htmlParagraph);
        if (visible) {
            builder.appendRequiredLineBreak(2);
        }
        appendChildren(builder, htmlParagraph, whiteSpaceStyle(htmlParagraph, mode), insideHead, insideSelect);
        if (visible) {
            builder.appendRequiredLineBreak(2);
        }
    }

    /**
     * Process {@link HtmlPreformattedText}.
     * The default white-space handling of a pre element is 'pre', but an explicit
     * white-space style of the element (or one of its ancestors) wins.
     *
     * @param builder the StringBuilder to add to
     * @param htmlPre the target to process
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendPreformattedText(final HtmlSerializerTextBuilder builder,
            final HtmlElement htmlPre, final boolean insideHead, final boolean insideSelect) {
        if (isNotRendered(htmlPre)) {
            return;
        }
        builder.appendRequiredLineBreak();
        appendChildren(builder, htmlPre, whiteSpaceStyle(htmlPre, Mode.WHITE_SPACE_PRE), insideHead, insideSelect);
        builder.appendRequiredLineBreak();
    }

    /**
     * Process {@link HtmlListItem}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlListItem the target to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendListItem(final HtmlSerializerTextBuilder builder,
            final HtmlListItem htmlListItem, final Mode mode, final boolean insideHead, final boolean insideSelect) {
        if (isNotRendered(htmlListItem)) {
            return;
        }
        // a list item with display inline has no line breaks
        final boolean block = isVisible(htmlListItem) && isBlockLevel(htmlListItem);
        if (block) {
            builder.appendRequiredLineBreak();
        }
        appendChildren(builder, htmlListItem, mode, insideHead, insideSelect);
        if (block) {
            builder.appendRequiredLineBreak();
        }
    }

    /**
     * Process {@link HtmlDetails}.
     * @param builder the StringBuilder to add to
     * @param htmlDetails the target to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendDetails(final HtmlSerializerTextBuilder builder,
                    final HtmlDetails htmlDetails, final Mode mode,
                    final boolean insideHead, final boolean insideSelect) {
        if (htmlDetails.isOpen()) {
            appendChildren(builder, htmlDetails, mode, insideHead, insideSelect);
            return;
        }

        for (final DomNode child : htmlDetails.getChildren()) {
            if (child instanceof HtmlSummary) {
                // only the first summary is the summary of the details
                appendNode(builder, child, mode, insideHead, insideSelect);
                return;
            }
        }
    }

    /**
     * Process {@link HtmlDefinitionTerm}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlDefinitionTerm the target to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendDefinitionTerm(final HtmlSerializerTextBuilder builder,
            final HtmlDefinitionTerm htmlDefinitionTerm, final Mode mode,
            final boolean insideHead, final boolean insideSelect) {
        if (isNotRendered(htmlDefinitionTerm)) {
            return;
        }
        final boolean block = isVisible(htmlDefinitionTerm) && isBlockLevel(htmlDefinitionTerm);
        if (block) {
            builder.appendRequiredLineBreak();
        }
        appendChildren(builder, htmlDefinitionTerm, mode, insideHead, insideSelect);
        if (block) {
            builder.appendRequiredLineBreak();
        }
    }

    /**
     * Process {@link HtmlSelect}.
     * <p>
     * Select and its Option children are rendered as native form-control widgets in real browsers,
     * not as normal inline/block boxes. The widget's internal text drawing doesn't go through the
     * same CSS text-layout pipeline that divs or spans use,
     * so properties like white-space: pre on a &lt;select&gt; or &lt;option&gt; get ignored by Chrome/Firefox/etc.
     * </p>
     *
     * @param builder the StringBuilder to add to
     * @param htmlSelect the target to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendSelect(final HtmlSerializerTextBuilder builder,
            final HtmlSelect htmlSelect, final Mode mode, final boolean insideHead, final boolean insideSelect) {
        // a list box (multiple or size > 1) shows the options
        if (browserVersion_.hasFeature(JS_INNER_TEXT_SELECT_EMPTY)
                && !htmlSelect.isMultipleSelectEnabled()
                && htmlSelect.getSize() <= 1) {
            return;
        }

        appendDescendantOptions(builder, htmlSelect, Mode.WHITE_SPACE_NORMAL, insideHead, true);
    }

    /**
     * Iterate over all descendant options.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     * @param insideHead true if inside head section
     * @param insideSelect true if inside a select
     */
    protected void appendDescendantOptions(final HtmlSerializerTextBuilder builder, final DomNode node,
            final Mode mode, final boolean insideHead, final boolean insideSelect) {
        for (final DomNode child : node.getChildNodes()) {
            if (child instanceof HtmlOption) {
                builder.appendRequiredLineBreak();
                appendChildren(builder, child, Mode.WHITE_SPACE_NORMAL, insideHead, true);
            }
            else {
                appendDescendantOptions(builder, child, Mode.WHITE_SPACE_NORMAL, insideHead, true);
            }
        }
    }

    private static Mode whiteSpaceStyle(final DomNode domNode, final Mode defaultMode) {
        if (domNode instanceof DomElement) {
            final SgmlPage page = domNode.getPage();
            if (page != null) {
                if (page.getWebClient().getOptions().isCssEnabled()) {
                    DomNode node = domNode;
                    while (node != null) {
                        if (node instanceof DomElement domElement) {
                            final WebWindow window = page.getEnclosingWindow();
                            if (window != null) {
                                final ComputedCssStyleDeclaration style =
                                        window.getComputedStyle(domElement, null);
                                final String value = style.getStyleAttribute(Definition.WHITE_SPACE, false);

                                if (!StringUtils.isEmptyOrNull(value)) {
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
                        }
                        node = node.getParentNode();
                    }
                }
            }
        }
        return defaultMode;
    }

    /**
     * Helper to compose the text for the serializer based on several modes.
     */
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
             * at newline characters, at <br> and as necessary
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
        private int pendingLineBreaks_;

        /**
         * Ctor.
         */
        public HtmlSerializerTextBuilder() {
            builder_ = new StringBuilder();
            state_ = State.EMPTY;
            trimRightPos_ = 0;
        }

        /**
         * Append a line separator.
         */
        public void appendRequiredLineBreak() {
            appendRequiredLineBreak(1);
        }

        /**
         * Append a required line break count; consecutive required line breaks
         * are merged to the biggest one. Required line breaks at the start
         * or the end are dropped.
         *
         * @param count the number of line breaks
         */
        public void appendRequiredLineBreak(final int count) {
            if (state_ == State.EMPTY) {
                return;
            }

            // trimRight
            builder_.setLength(trimRightPos_);
            if (trimRightPos_ == 0) {
                state_ = State.EMPTY;
                pendingLineBreaks_ = 0;
                return;
            }

            pendingLineBreaks_ = Math.max(pendingLineBreaks_, count);
            state_ = State.REQUIRED_LINE_BREAK_AT_END;
        }

        /**
         * Append a line break that is part of the content (like the one from a br element).
         */
        public void appendLineBreak() {
            // collapsible white space at the end of the line is removed
            builder_.setLength(trimRightPos_);
            flushPendingLineBreaks();
            builder_.append('\n');
            state_ = State.BREAK_AT_END;
            trimRightPos_ = builder_.length();
        }

        /**
         * Append a tab (used to separate table cells).
         */
        public void appendTab() {
            flushPendingLineBreaks();
            builder_.append('\t');
            state_ = State.BREAK_AT_END;
            trimRightPos_ = builder_.length();
        }

        /**
         * Writes the pending required line breaks (if any) to the output.
         */
        private void flushPendingLineBreaks() {
            if (pendingLineBreaks_ > 0) {
                builder_.setLength(trimRightPos_);
                for (int i = 0; i < pendingLineBreaks_; i++) {
                    builder_.append('\n');
                }
                pendingLineBreaks_ = 0;
                trimRightPos_ = builder_.length();
            }
        }

        /**
         * Append the provided content.
         * see https://drafts.csswg.org/css-text-3/#white-space
         *
         * @param content the content to add
         * @param mode the {@link Mode}
         */
        public void append(final String content, final Mode mode) {
            if (content == null) {
                return;
            }
            int length = content.length();
            if (length == 0) {
                return;
            }

            if (mode == Mode.PLAIN) {
                flushPendingLineBreaks();
                builder_.append(content);
                state_ = State.DEFAULT;
                trimRightPos_ = builder_.length();
                return;
            }

            length--;
            final int contentLength = content.length();
            for (int i = 0; i < contentLength; i++) {
                char c = content.charAt(i);

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
                            case BLOCK_SEPARATOR_AT_END:
                                break;
                            default:
                                flushPendingLineBreaks();
                                builder_.append('\n');
                                state_ = State.NEWLINE_AT_END;
                                trimRightPos_ = builder_.length();
                                break;
                        }
                        continue;
                    }

                    if (mode == Mode.WHITE_SPACE_PRE_LINE) {
                        switch (state_) {
                            case BLOCK_SEPARATOR_AT_END:
                                break;
                            case BLANK_AT_END:
                                builder_.setLength(trimRightPos_);
                                flushPendingLineBreaks();
                                builder_.append('\n');
                                state_ = State.NEWLINE_AT_END;
                                trimRightPos_ = builder_.length();
                                break;
                            default:
                                flushPendingLineBreaks();
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
                        flushPendingLineBreaks();
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
                            case REQUIRED_LINE_BREAK_AT_END:
                            case BLOCK_SEPARATOR_AT_END:
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

                flushPendingLineBreaks();
                builder_.append(c);
                state_ = State.DEFAULT;
                trimRightPos_ = builder_.length();
            }
        }

        /**
         * Returns the constructed text.
         *
         * @return the constructed text.
         */
        public String getText() {
            return builder_.substring(0, trimRightPos_);
        }
    }
}
