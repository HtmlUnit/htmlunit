/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSerializerVisibleText.HtmlSerializerTextBuilder.Mode;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;

/**
 * Special serializer to generate the output we need
 * at least for selenium WebElement#getText().
 *
 * @author Ronald Brill
 */
public class HtmlSerializerVisibleText {
    private static final String SPACES = " \t\n\f\r";

    /**
     * Converts an HTML node to text.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     */
    public String asText(final DomNode node) {
        final HtmlSerializerTextBuilder builder = new HtmlSerializerTextBuilder();
        appendNode(builder, node, Mode.NORMALIZE);
        return builder.getText();
    }

    /**
     * Iterate over all Children and call appendNode() for every.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendChildren(final HtmlSerializerTextBuilder builder, final DomNode node, final Mode mode) {
        for (final DomNode child : node.getChildren()) {
            appendNode(builder, child, mode);
        }
    }

    /**
     * The core distribution method call the different appendXXX
     * methods depending on the type of the given node.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendNode(final HtmlSerializerTextBuilder builder, final DomNode node, final Mode mode) {
        if (node instanceof DomText) {
            appendText(builder, (DomText) node, mode);
        }
        else if (node instanceof DomComment) {
            appendComment(builder, (DomComment) node, mode);
        }
        else if (node instanceof HtmlApplet
                && node.getPage().getWebClient().getOptions().isAppletEnabled()) {
            appendApplet(builder, (HtmlApplet) node, mode);
        }
        else if (node instanceof HtmlBreak) {
            appendBreak(builder, (HtmlBreak) node, mode);
        }
        else if (node instanceof HtmlHiddenInput) {
            appendHiddenInput(builder, (HtmlHiddenInput) node, mode);
        }
        else if (node instanceof HtmlScript) {
            appendScript(builder, (HtmlScript) node, mode);
        }
        else if (node instanceof HtmlStyle) {
            appendStyle(builder, (HtmlStyle) node, mode);
        }
        else if (node instanceof HtmlNoFrames) {
            appendNoFrames(builder, (HtmlNoFrames) node, mode);
        }
        else if (node instanceof HtmlTextArea) {
            appendTextArea(builder, (HtmlTextArea) node, mode);
        }
        else if (node instanceof HtmlTitle) {
            appendTitle(builder, (HtmlTitle) node, mode);
        }
        else if (node instanceof HtmlTableRow) {
            appendTableRow(builder, (HtmlTableRow) node, mode);
        }
        else if (node instanceof HtmlSelect) {
            appendSelect(builder, (HtmlSelect) node, mode);
        }
        else if (node instanceof HtmlSubmitInput) {
            appendSubmitInput(builder, (HtmlSubmitInput) node, mode);
        }
        else if (node instanceof HtmlResetInput) {
            appendResetInput(builder, (HtmlResetInput) node, mode);
        }
        else if (node instanceof HtmlCheckBoxInput) {
            doAppendCheckBoxInput(builder, (HtmlCheckBoxInput) node, mode);
        }
        else if (node instanceof HtmlRadioButtonInput) {
            doAppendRadioButtonInput(builder, (HtmlRadioButtonInput) node, mode);
        }
        else if (node instanceof HtmlInput) {
            // nothing
        }
        else if (node instanceof HtmlTable) {
            appendTable(builder, (HtmlTable) node, mode);
        }
        else if (node instanceof HtmlOrderedList) {
            appendOrderedList(builder, (HtmlOrderedList) node, mode);
        }
        else if (node instanceof HtmlUnorderedList) {
            appendUnorderedList(builder, (HtmlUnorderedList) node, mode);
        }
        else if (node instanceof HtmlPreformattedText) {
            appendPreformattedText(builder, (HtmlPreformattedText) node, mode);
        }
        else if (node instanceof HtmlInlineFrame) {
            appendInlineFrame(builder, (HtmlInlineFrame) node, mode);
        }
        else if (node instanceof HtmlNoScript && node.getPage().getWebClient().getOptions().isJavaScriptEnabled()) {
            appendNoScript(builder, (HtmlNoScript) node, mode);
        }
        else {
            appendDomNode(builder, node, mode);
        }
    }

    /**
     * Process {@link HtmlHiddenInput}.
     *
     * @param builder the StringBuilder to add to
     * @param domNode the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendDomNode(final HtmlSerializerTextBuilder builder,
            final DomNode domNode, final Mode mode) {
        final boolean block;
        final Object scriptableObject = domNode.getScriptableObject();
        if (domNode instanceof HtmlBody) {
            block = false;
        }
        else if (scriptableObject instanceof Element) {
            final Element element = (Element) scriptableObject;
            final String display = element.getWindow().getComputedStyle(element, null).getDisplay(true);
            block = "block".equals(display);
        }
        else {
            block = false;
        }

        if (block) {
            builder.appendBlockSeparator();
        }
        appendChildren(builder, domNode, mode);
        if (block) {
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlHiddenInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlHiddenInput the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendHiddenInput(final HtmlSerializerTextBuilder builder,
            final HtmlHiddenInput htmlHiddenInput, final Mode mode) {
        // nothing to do
    }

    /**
     * Process {@link HtmlScript}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlScript the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendScript(final HtmlSerializerTextBuilder builder,
            final HtmlScript htmlScript, final Mode mode) {
        // nothing to do
    }

    /**
     * Process {@link HtmlStyle}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlStyle the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendStyle(final HtmlSerializerTextBuilder builder,
            final HtmlStyle htmlStyle, final Mode mode) {
        // nothing to do
    }

    /**
     * Process {@link HtmlNoScript}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlNoScript the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendNoScript(final HtmlSerializerTextBuilder builder,
            final HtmlNoScript htmlNoScript, final Mode mode) {
        // nothing to do
    }

    /**
     * Process {@link HtmlNoFrames}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlNoFrames the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendNoFrames(final HtmlSerializerTextBuilder builder,
            final HtmlNoFrames htmlNoFrames, final Mode mode) {
        // nothing to do
    }

    /**
     * Process {@link HtmlSubmitInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlSubmitInput the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendSubmitInput(final HtmlSerializerTextBuilder builder,
            final HtmlSubmitInput htmlSubmitInput, final Mode mode) {
        builder.append(htmlSubmitInput.asText(), mode);
    }

    /**
     * Process {@link HtmlInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlInput the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendInput(final HtmlSerializerTextBuilder builder,
            final HtmlInput htmlInput, final Mode mode) {
        builder.append(htmlInput.getValueAttribute(), mode);
    }

    /**
     * Process {@link HtmlResetInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlResetInput the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendResetInput(final HtmlSerializerTextBuilder builder,
            final HtmlResetInput htmlResetInput, final Mode mode) {
        builder.append(htmlResetInput.asText(), mode);
    }

    /**
     * Process {@link HtmlUnorderedList}.
     * @param builder the StringBuilder to add to
     * @param htmlUnorderedList the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendUnorderedList(final HtmlSerializerTextBuilder builder,
                    final HtmlUnorderedList htmlUnorderedList, final Mode mode) {
        builder.appendBlockSeparator();
        boolean first = true;
        for (final DomNode item : htmlUnorderedList.getChildren()) {
            if (!first) {
                builder.appendBlockSeparator();
            }
            first = false;
            appendNode(builder, item, mode);
        }
        builder.appendBlockSeparator();
    }

    /**
     * Process {@link HtmlTitle}.
     * @param builder the StringBuilder to add to
     * @param htmlTitle the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendTitle(final HtmlSerializerTextBuilder builder,
            final HtmlTitle htmlTitle, final Mode mode) {
        // optimized version
        // for the title there is no need to check the visibility
        // of the containing dom text;
        // this optimization defers the load of the style sheets
        final DomNode child = htmlTitle.getFirstChild();
        if (child instanceof DomText) {
            builder.append(((DomText) child).getData(), mode);
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlTableRow}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlTableRow the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendTableRow(final HtmlSerializerTextBuilder builder,
            final HtmlTableRow htmlTableRow, final Mode mode) {
        boolean first = true;
        for (final HtmlTableCell cell : htmlTableRow.getCells()) {
            if (!first) {
                builder.appendTab();
            }
            else {
                first = false;
            }
            appendChildren(builder, cell, mode); // trim?
        }
    }

    /**
     * Process {@link HtmlTextArea}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlTextArea the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendTextArea(final HtmlSerializerTextBuilder builder,
            final HtmlTextArea htmlTextArea, final Mode mode) {
        if (isVisible(htmlTextArea)) {
            builder.append(StringUtils.stripEnd(htmlTextArea.getText(), SPACES),
                    whiteSpaceStyle(htmlTextArea, Mode.NORMALIZE_PRE));
        }
    }

    /**
     * Process {@link HtmlTable}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlTable the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendTable(final HtmlSerializerTextBuilder builder,
            final HtmlTable htmlTable, final Mode mode) {
        builder.appendBlockSeparator();
        final String caption = htmlTable.getCaptionText();
        if (caption != null) {
            builder.append(caption, mode);
            builder.appendBlockSeparator();
        }

        boolean first = true;

        // first thead has to be displayed first and first tfoot has to be displayed last
        final HtmlTableHeader tableHeader = htmlTable.getHeader();
        if (tableHeader != null) {
            first = appendTableRows(builder, mode, tableHeader.getRows(), true, null, null);
        }
        final HtmlTableFooter tableFooter = htmlTable.getFooter();

        final List<HtmlTableRow> tableRows = htmlTable.getRows();
        first = appendTableRows(builder, mode, tableRows, first, tableHeader, tableFooter);

        if (tableFooter != null) {
            first = appendTableRows(builder, mode, tableFooter.getRows(), first, null, null);
        }
        else if (tableRows.isEmpty()) {
            final DomNode firstChild = htmlTable.getFirstChild();
            if (firstChild != null) {
                appendNode(builder, firstChild, mode);
            }
        }

        builder.appendBlockSeparator();
    }

    /**
     * Process {@link HtmlTableRow}.
     *
     * @param builder the StringBuilder to add to
     * @param mode the {@link Mode} to use for processing
     * @param rows the rows
     * @param first if true this is the first one
     * @param skipParent1 skip row if the parent is this
     * @param skipParent2 skip row if the parent is this
     * @return true if this was the first one
     */
    protected boolean appendTableRows(final HtmlSerializerTextBuilder builder, final Mode mode,
            final List<HtmlTableRow> rows, boolean first, final TableRowGroup skipParent1,
            final TableRowGroup skipParent2) {
        for (final HtmlTableRow row : rows) {
            if (row.getParentNode() == skipParent1 || row.getParentNode() == skipParent2) {
                continue;
            }
            if (!first) {
                builder.appendBlockSeparator();
            }
            first = false;
            appendTableRow(builder, row, mode);
        }
        return first;
    }

    /**
     * Process {@link HtmlSelect}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlSelect the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendSelect(final HtmlSerializerTextBuilder builder,
            final HtmlSelect htmlSelect, final Mode mode) {
        final List<HtmlOption> options;
        if (htmlSelect.isMultipleSelectEnabled()) {
            options = htmlSelect.getOptions();
        }
        else {
            options = htmlSelect.getSelectedOptions();
        }

        for (final Iterator<HtmlOption> i = options.iterator(); i.hasNext();) {
            final HtmlOption currentOption = i.next();
            appendNode(builder, currentOption, mode);
            if (i.hasNext()) {
                builder.appendBlockSeparator();
            }
        }
    }

    /**
     * Process {@link HtmlOrderedList} taking care to numerate it.
     *
     * @param builder the StringBuilder to add to
     * @param htmlOrderedList the OL element
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendOrderedList(final HtmlSerializerTextBuilder builder,
            final HtmlOrderedList htmlOrderedList, final Mode mode) {
        builder.appendBlockSeparator();
        boolean first = true;
        for (final DomNode item : htmlOrderedList.getChildren()) {
            if (!first) {
                builder.appendBlockSeparator();
            }
            first = false;
            if (item instanceof HtmlListItem) {
                appendChildren(builder, item, mode);
            }
            else {
                appendNode(builder, item, mode);
            }
        }
        builder.appendBlockSeparator();
    }

    /**
     * Process {@link HtmlPreformattedText}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlPreformattedText the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendPreformattedText(final HtmlSerializerTextBuilder builder,
            final HtmlPreformattedText htmlPreformattedText, final Mode mode) {
        if (isVisible(htmlPreformattedText)) {
            builder.appendBlockSeparator();
            appendChildren(builder, htmlPreformattedText, Mode.NORMALIZE_PRE);
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlInlineFrame}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlInlineFrame the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendInlineFrame(final HtmlSerializerTextBuilder builder,
            final HtmlInlineFrame htmlInlineFrame, final Mode mode) {
        if (isVisible(htmlInlineFrame)) {
            builder.appendBlockSeparator();
            final Page page = htmlInlineFrame.getEnclosedPage();
            if (page instanceof SgmlPage) {
                builder.append(((SgmlPage) page).asText(), mode);
            }
            builder.appendBlockSeparator();
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
        if (parent == null || parent instanceof HtmlTitle || isVisible(parent)) {
            builder.append(domText.getData(), whiteSpaceStyle(domText, mode));
        }
    }

    /**
     * Process {@link DomComment}.
     *
     * @param builder the StringBuilder to add to
     * @param domComment the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendComment(final HtmlSerializerTextBuilder builder,
            final DomComment domComment, final Mode mode) {
        // nothing to do
    }

    /**
     * Process {@link HtmlApplet}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlApplet the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendApplet(final HtmlSerializerTextBuilder builder,
            final HtmlApplet htmlApplet, final Mode mode) {
        // nothing to do
    }

    /**
     * Process {@link HtmlBreak}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlBreak the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendBreak(final HtmlSerializerTextBuilder builder,
            final HtmlBreak htmlBreak, final Mode mode) {
        builder.appendNewLine();
    }

    /**
     * Process {@link HtmlCheckBoxInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlCheckBoxInput the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void doAppendCheckBoxInput(final HtmlSerializerTextBuilder builder,
                    final HtmlCheckBoxInput htmlCheckBoxInput, final Mode mode) {
        if (htmlCheckBoxInput.isChecked()) {
            builder.append("checked", mode);
        }
        else {
            builder.append("unchecked", mode);
        }
    }

    /**
     * Process {@link HtmlRadioButtonInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlRadioButtonInput the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void doAppendRadioButtonInput(final HtmlSerializerTextBuilder builder,
            final HtmlRadioButtonInput htmlRadioButtonInput, final Mode mode) {
        if (htmlRadioButtonInput.isChecked()) {
            builder.append("checked", mode);
        }
        else {
            builder.append("unchecked", mode);
        }
    }

    private boolean isVisible(final DomNode node) {
        return node.isDisplayed();
    }

    private Mode whiteSpaceStyle(final DomNode domNode, final Mode defaultMode) {
        final Object scriptableObject = domNode.getScriptableObject();
        if (scriptableObject instanceof Node) {
            final HtmlPage htmlPage = domNode.getHtmlPageOrNull();
            if (htmlPage != null && htmlPage.getEnclosingWindow().getWebClient().getOptions().isCssEnabled()) {
                Node node = (Node) scriptableObject;

                while (node != null) {
                    if (node instanceof Element) {
                        final ComputedCSSStyleDeclaration style = node.getWindow().getComputedStyle(node, null);
                        final String value = style.getStyleAttribute(Definition.WHITE_SPACE, false);
                        if (StringUtils.isNoneEmpty(value)) {
                            if ("normal".equalsIgnoreCase(value)) {
                                return Mode.NORMALIZE;
                            }
                            if ("unset".equalsIgnoreCase(value)) {
                                return Mode.NORMALIZE;
                            }
                            if ("nowrap".equalsIgnoreCase(value)) {
                                return Mode.NORMALIZE;
                            }
                            if ("pre".equalsIgnoreCase(value)) {
                                return Mode.NORMALIZE_PRE;
                            }
                            if ("pre-wrap".equalsIgnoreCase(value)) {
                                return Mode.NORMALIZE_PRE;
                            }
                            if ("pre-line".equalsIgnoreCase(value)) {
                                return Mode.NORMALIZE_PRE_LINE;
                            }
                        }
                    }
                    node = node.getParentElement();
                }
            }
        }
        return defaultMode;
    }

    protected static class HtmlSerializerTextBuilder {
        /** Mode. */
        protected enum Mode {
            /**
             * Sequences of white space are collapsed. Newline characters
             * in the source are handled the same as other white space.
             * Lines are broken as necessary to fill line boxes.
             */
            NORMALIZE,

            /**
             * Sequences of white space are preserved. Lines are only broken
             * at newline characters in the source and at <br> elements.
             */
            NORMALIZE_PRE,

            /**
             * Sequences of white space are collapsed. Lines are broken
             * at newline characters, at <br>, and as necessary
             * to fill line boxes.
             */
            NORMALIZE_PRE_LINE
        }

        private enum State {
            DEFAULT,
            EMPTY,
            TRIM,
            BLANK_AT_END,
            BLANK_AT_END_AFTER_NEWLINE,
            NEWLINE_AT_END,
            BLOCK_SEPARATOR_AT_END
        }

        private State state_;
        private final StringBuilder builder_;
        private int trimRightPos_;

        public HtmlSerializerTextBuilder() {
            builder_ = new StringBuilder();
            state_ = State.EMPTY;
            trimRightPos_ = 0;
        }

        // see https://drafts.csswg.org/css-text-3/#white-space
        public void append(final String content, final Mode mode) {
            int length = content.length();
            if (length == 0) {
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

                if (mode == Mode.NORMALIZE || mode == Mode.NORMALIZE_PRE_LINE && c != '\n' && c != '\r') {
                    if (isSpace(c)) {
                        switch (state_) {
                            case EMPTY:
                            case TRIM:
                            case BLANK_AT_END:
                            case BLANK_AT_END_AFTER_NEWLINE:
                            case BLOCK_SEPARATOR_AT_END:
                                break;
                            case NEWLINE_AT_END:
                                builder_.append(' ');
                                state_ = State.BLANK_AT_END_AFTER_NEWLINE;
                                break;
                            default:
                                builder_.append(' ');
                                state_ = State.BLANK_AT_END;
                                break;
                        }
                    }
                    else if (c == (char) 160) {
                        builder_.append(' ');
                        state_ = State.DEFAULT;
                        trimRightPos_ = builder_.length();
                    }
                    else {
                        builder_.append(c);
                        state_ = State.DEFAULT;
                        trimRightPos_ = builder_.length();
                    }
                    continue;
                }

                // preserve mode
                if (c == '\n') {
                    appendNewLine();
                }
                else {
                    if (c == '\t') {
                        if (state_ != State.BLOCK_SEPARATOR_AT_END) {
                            appendBlank();
                        }
                    }
                    else if (c == (char) 160) {
                        appendBlank();
                    }
                    else if (c == ' ') {
                        appendBlank();
                    }
                    else if (c != '\r') {
                        builder_.append(c);
                        state_ = State.DEFAULT;
                        trimRightPos_ = builder_.length();
                    }
                }
            }

            if (mode != Mode.NORMALIZE) {
                // reset state to empty to restart whitespace normalization afterwards
                state_ = State.TRIM;
            }
        }

        public void appendBlockSeparator() {
            switch (state_) {
                case EMPTY:
                    break;
                case BLANK_AT_END:
                    builder_.setLength(trimRightPos_);
                    if (builder_.length() == 0) {
                        state_ = State.EMPTY;
                    }
                    else {
                        builder_.append('\n');
                        state_ = State.BLOCK_SEPARATOR_AT_END;
                    }
                    break;
                case BLANK_AT_END_AFTER_NEWLINE:
                    builder_.setLength(trimRightPos_ - 1);
                    if (builder_.length() == 0) {
                        state_ = State.EMPTY;
                    }
                    else {
                        builder_.append('\n');
                        state_ = State.BLOCK_SEPARATOR_AT_END;
                    }
                    break;
                case BLOCK_SEPARATOR_AT_END:
                    break;
                case NEWLINE_AT_END:
                    builder_.setLength(builder_.length() - 1);
                    trimRightPos_ = trimRightPos_ - 1;
                    if (builder_.length() == 0) {
                        state_ = State.EMPTY;
                    }
                    else {
                        builder_.append('\n');
                        state_ = State.BLOCK_SEPARATOR_AT_END;
                    }
                    break;
                default:
                    builder_.append('\n');
                    state_ = State.BLOCK_SEPARATOR_AT_END;
                    break;
            }
        }

        public void appendNewLine() {
            builder_.append('\n');
            state_ = State.NEWLINE_AT_END;
            trimRightPos_ = builder_.length();
        }

        public void appendTab() {
            builder_.append('\t');
            trimRightPos_ = builder_.length();
        }

        private void appendBlank() {
            builder_.append(' ');
            state_ = State.BLANK_AT_END;
            trimRightPos_ = builder_.length();
        }

        public String getText() {
            return builder_.substring(0, trimRightPos_);
        }

        private static boolean isSpace(final char ch) {
            return " \t\n\f\r".indexOf(ch) > -1;
        }
    }
}
