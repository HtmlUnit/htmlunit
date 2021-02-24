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
package com.gargoylesoftware.htmlunit.html.serializer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlApplet;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlBreak;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlMenu;
import com.gargoylesoftware.htmlunit.html.HtmlNoFrames;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPreformattedText;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableFooter;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeader;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTitle;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.gargoylesoftware.htmlunit.html.TableRowGroup;
import com.gargoylesoftware.htmlunit.html.serializer.HtmlSerializerVisibleText.HtmlSerializerTextBuilder.Mode;
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

    /**
     * Converts an HTML node to text.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     */
    public String asText(final DomNode node) {
        if (node instanceof HtmlBreak) {
            return "";
        }
        final HtmlSerializerTextBuilder builder = new HtmlSerializerTextBuilder();
        appendNode(builder, node, whiteSpaceStyle(node, Mode.WHITE_SPACE_NORMAL));
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
        else if (node instanceof HtmlOption) {
            appendOption(builder, (HtmlOption) node, mode);
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
        else if (node instanceof HtmlMenu) {
            appendMenu(builder, (HtmlMenu) node, mode);
        }
        else if (node instanceof HtmlNoScript && node.getPage().getWebClient().isJavaScriptEnabled()) {
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
            final String display = element.getWindow().getComputedStyle(element, null).getDisplay();
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
        // nothing to do
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
        // nothing to do
    }

    /**
     * Process {@link HtmlMenu}.
     * @param builder the StringBuilder to add to
     * @param htmlMenu the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendMenu(final HtmlSerializerTextBuilder builder,
                    final HtmlMenu htmlMenu, final Mode mode) {
        builder.appendBlockSeparator();
        boolean first = true;
        for (final DomNode item : htmlMenu.getChildren()) {
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
        // nothing to do
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
                builder.appendBlank();
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
            builder.append(htmlTextArea.getDefaultValue(), whiteSpaceStyle(htmlTextArea, Mode.PRE));
            builder.trimRight(Mode.PRE);
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
        builder.appendBlockSeparator();
        boolean leadingNlPending = false;
        final Mode selectMode = whiteSpaceStyle(htmlSelect, mode);
        for (final DomNode item : htmlSelect.getChildren()) {
            if (leadingNlPending) {
                builder.appendBlockSeparator();
                leadingNlPending = false;
            }

            builder.resetContentAdded();
            appendNode(builder, item, whiteSpaceStyle(item, selectMode));
            if (!leadingNlPending && builder.contentAdded_) {
                leadingNlPending = true;
            }
        }
        builder.appendBlockSeparator();
    }

    /**
     * Process {@link HtmlSelect}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlOption the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendOption(final HtmlSerializerTextBuilder builder,
            final HtmlOption htmlOption, final Mode mode) {
        builder.ignoreHtmlBreaks();
        appendChildren(builder, htmlOption, mode);
        builder.processHtmlBreaks();
    }

    /**
     * Process {@link HtmlOrderedList}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlOrderedList the OL element
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendOrderedList(final HtmlSerializerTextBuilder builder,
            final HtmlOrderedList htmlOrderedList, final Mode mode) {
        builder.appendBlockSeparator();
        boolean leadingNlPending = false;
        final Mode olMode = whiteSpaceStyle(htmlOrderedList, mode);
        for (final DomNode item : htmlOrderedList.getChildren()) {
            if (leadingNlPending) {
                builder.appendBlockSeparator();
                leadingNlPending = false;
            }

            builder.resetContentAdded();
            appendNode(builder, item, whiteSpaceStyle(item, olMode));
            if (!leadingNlPending && builder.contentAdded_) {
                leadingNlPending = true;
            }
        }
        builder.appendBlockSeparator();
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
        boolean leadingNlPending = false;
        final Mode ulMode = whiteSpaceStyle(htmlUnorderedList, mode);
        for (final DomNode item : htmlUnorderedList.getChildren()) {
            if (leadingNlPending) {
                builder.appendBlockSeparator();
                leadingNlPending = false;
            }

            builder.resetContentAdded();
            appendNode(builder, item, whiteSpaceStyle(item, ulMode));
            if (!leadingNlPending && builder.contentAdded_) {
                leadingNlPending = true;
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
            appendChildren(builder, htmlPreformattedText, whiteSpaceStyle(htmlPreformattedText, Mode.PRE));
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
            builder.append(domText.getData(), mode);
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
        builder.appendBreak(mode);
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
        // nothing to do
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
        // nothing to do
    }

    private boolean isVisible(final DomNode node) {
        return node.isDisplayed();
    }

    private Mode whiteSpaceStyle(final DomNode domNode, final Mode defaultMode) {
        final Object scriptableObject = domNode.getScriptableObject();
        if (scriptableObject instanceof Node) {
            final Page page = domNode.getPage();
            if (page != null && page.getEnclosingWindow().getWebClient().getOptions().isCssEnabled()) {
                Node node = (Node) scriptableObject;

                while (node != null) {
                    if (node instanceof Element) {
                        final ComputedCSSStyleDeclaration style = node.getWindow().getComputedStyle(node, null);
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
             * The mode for the pre tag.
             */
            PRE,

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
            BLOCK_SEPARATOR_AT_END
        }

        private State state_;
        private final StringBuilder builder_;
        private int trimRightPos_;
        private boolean contentAdded_;
        private boolean ignoreHtmlBreaks_;

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

                    if (mode == Mode.PRE) {
                        builder_.append('\n');
                        state_ = State.NEWLINE_AT_END;
                        trimRightPos_ = builder_.length();

                        continue;
                    }

                    if (mode == Mode.WHITE_SPACE_PRE_LINE) {
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

                    switch (state_) {
                        case EMPTY:
                        case BLANK_AT_END:
                        case BLANK_AT_END_AFTER_NEWLINE:
                        case BLOCK_SEPARATOR_AT_END:
                        case NEWLINE_AT_END:
                        case BREAK_AT_END:
                            break;
                        default:
                            builder_.append(' ');
                            state_ = State.BLANK_AT_END;
                            break;
                    }
                    continue;
                }

                if (c == ' ' || c == '\t' || c == '\f') {
                    if (mode == Mode.WHITE_SPACE_PRE || mode == Mode.PRE) {
                        appendBlank();
                        continue;
                    }

                    if (mode == Mode.WHITE_SPACE_PRE_LINE) {
                        switch (state_) {
                            case EMPTY:
                            case BLANK_AT_END:
                            case BLANK_AT_END_AFTER_NEWLINE:
                            case BREAK_AT_END:
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
                            break;
                        default:
                            builder_.append(' ');
                            state_ = State.BLANK_AT_END;
                            break;
                    }
                    continue;
                }

                if (c == (char) 160) {
                    appendBlank();
                    if (mode == Mode.WHITE_SPACE_NORMAL || mode == Mode.WHITE_SPACE_PRE_LINE) {
                        state_ = State.DEFAULT;
                    }
                    continue;
                }
                builder_.append(c);
                state_ = State.DEFAULT;
                trimRightPos_ = builder_.length();
                contentAdded_ = true;
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
                case BREAK_AT_END:
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

        public void appendBreak(final Mode mode) {
            if (ignoreHtmlBreaks_) {
                return;
            }

            builder_.setLength(trimRightPos_);

            builder_.append('\n');
            state_ = State.BREAK_AT_END;
            trimRightPos_ = builder_.length();
        }

        public void appendBlank() {
            builder_.append(' ');
            state_ = State.BLANK_AT_END;
            trimRightPos_ = builder_.length();
        }

        public void trimRight(final Mode mode) {
            if (mode == Mode.PRE) {
                switch (state_) {
                    case BLOCK_SEPARATOR_AT_END:
                    case NEWLINE_AT_END:
                    case BREAK_AT_END:
                        if (trimRightPos_ == builder_.length()) {
                            trimRightPos_--;
                        }
                        break;
                    default:
                        break;
                }
            }

            builder_.setLength(trimRightPos_);
            state_ = State.DEFAULT;
            if (builder_.length() == 0) {
                state_ = State.EMPTY;
            }
        }

        public boolean wasContentAdded() {
            return contentAdded_;
        }

        public void resetContentAdded() {
            contentAdded_ = false;
        }

        public void ignoreHtmlBreaks() {
            ignoreHtmlBreaks_ = true;
        }

        public void processHtmlBreaks() {
            ignoreHtmlBreaks_ = false;
        }

        public String getText() {
            return builder_.substring(0, trimRightPos_);
        }
    }
}
