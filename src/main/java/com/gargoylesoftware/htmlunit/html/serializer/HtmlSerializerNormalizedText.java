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

import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlApplet;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlBreak;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDetails;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlNoFrames;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.html.HtmlNumberInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList;
import com.gargoylesoftware.htmlunit.html.HtmlPreformattedText;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlSummary;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableFooter;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeader;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTitle;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.gargoylesoftware.htmlunit.html.TableRowGroup;
import com.gargoylesoftware.htmlunit.html.HtmlElement.DisplayStyle;
import com.gargoylesoftware.htmlunit.html.serializer.HtmlSerializerNormalizedText.HtmlSerializerTextBuilder.Mode;

/**
 * Utility to handle conversion from HTML code to string.
 * This implements HtmlUnit's way of normalization.
 * <p>Note: There conversation done by selenium WebElement#getText()
 * is different.</p>
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Rob Kodey
 */
public class HtmlSerializerNormalizedText {

    private boolean ignoreMaskedElements_ = true;

    /**
     * Converts an HTML node to text.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     */
    public String asText(final DomNode node) {
        final HtmlSerializerTextBuilder builder = new HtmlSerializerTextBuilder();
        appendNode(builder, node);
        return builder.getText();
    }

    /**
     * Iterate over all Children and call appendNode() for every.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     */
    protected void appendChildren(final HtmlSerializerTextBuilder builder, final DomNode node) {
        for (final DomNode child : node.getChildren()) {
            appendNode(builder, child);
        }
    }

    /**
     * The core distribution method call the different appendXXX
     * methods depending on the type of the given node.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     */
    protected void appendNode(final HtmlSerializerTextBuilder builder, final DomNode node) {
        if (node instanceof DomText) {
            appendText(builder, (DomText) node);
        }
        else if (node instanceof DomComment) {
            // nothing to do
        }
        else if (node instanceof HtmlApplet
                && node.getPage().getWebClient().getOptions().isAppletEnabled()) {
            // nothing to do
        }
        else if (node instanceof HtmlBreak) {
            appendBreak(builder, (HtmlBreak) node);
        }
        else if (node instanceof HtmlHiddenInput) {
            // nothing to do
        }
        else if (node instanceof HtmlScript) {
            // nothing to do
        }
        else if (node instanceof HtmlStyle) {
            // nothing to do
        }
        else if (node instanceof HtmlNoFrames) {
            // nothing to do
        }
        else if (node instanceof HtmlTextArea) {
            appendTextArea(builder, (HtmlTextArea) node);
        }
        else if (node instanceof HtmlTitle) {
            appendTitle(builder, (HtmlTitle) node);
        }
        else if (node instanceof HtmlTableRow) {
            appendTableRow(builder, (HtmlTableRow) node);
        }
        else if (node instanceof HtmlSelect) {
            appendSelect(builder, (HtmlSelect) node);
        }
        else if (node instanceof HtmlSubmitInput) {
            appendSubmitInput(builder, (HtmlSubmitInput) node);
        }
        else if (node instanceof HtmlResetInput) {
            appendResetInput(builder, (HtmlResetInput) node);
        }
        else if (node instanceof HtmlCheckBoxInput) {
            appendCheckBoxInput(builder, (HtmlCheckBoxInput) node);
        }
        else if (node instanceof HtmlRadioButtonInput) {
            appendRadioButtonInput(builder, (HtmlRadioButtonInput) node);
        }
        else if (node instanceof HtmlNumberInput) {
            appendNumberInput(builder, (HtmlNumberInput) node);
        }
        else if (node instanceof HtmlInput) {
            appendInput(builder, (HtmlInput) node);
        }
        else if (node instanceof HtmlTable) {
            appendTable(builder, (HtmlTable) node);
        }
        else if (node instanceof HtmlOrderedList) {
            appendOrderedList(builder, (HtmlOrderedList) node);
        }
        else if (node instanceof HtmlUnorderedList) {
            appendUnorderedList(builder, (HtmlUnorderedList) node);
        }
        else if (node instanceof HtmlPreformattedText) {
            appendPreformattedText(builder, (HtmlPreformattedText) node);
        }
        else if (node instanceof HtmlInlineFrame) {
            appendInlineFrame(builder, (HtmlInlineFrame) node);
        }
        else if (node instanceof HtmlDetails) {
            appendDetails(builder, (HtmlDetails) node);
        }
        else if (node instanceof HtmlNoScript && node.getPage().getWebClient().isJavaScriptEnabled()) {
            // nothing to do
        }
        else {
            appendDomNode(builder, node);
        }
    }

    /**
     * Process {@link HtmlHiddenInput}.
     *
     * @param builder the StringBuilder to add to
     * @param domNode the target to process
     */
    protected void appendDomNode(final HtmlSerializerTextBuilder builder, final DomNode domNode) {
        boolean block = false;
        if (!(domNode instanceof HtmlBody)) {
            final SgmlPage page = domNode.getPage();
            final WebWindow window = page.getEnclosingWindow();
            if (page != null
                    && page.getWebClient().isJavaScriptEngineEnabled()  // TODO
                    && window.getWebClient().getOptions().isCssEnabled()) {
                if (domNode instanceof DomElement) {
                    final String display = window.getComputedStyle((DomElement) domNode, null).getDisplay();
                    block = "block".equals(display);
                }
            }
            else if (domNode instanceof HtmlElement) {
                block = DisplayStyle.BLOCK == ((HtmlElement) domNode).getDefaultStyleDisplay();
            }
        }

        if (block) {
            builder.appendBlockSeparator();
        }
        appendChildren(builder, domNode);
        if (block) {
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlSubmitInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlSubmitInput the target to process
     */
    protected void appendSubmitInput(final HtmlSerializerTextBuilder builder, final HtmlSubmitInput htmlSubmitInput) {
        String text = htmlSubmitInput.getValueAttribute();
        if (ATTRIBUTE_NOT_DEFINED == text) {
            text = HtmlSubmitInput.DEFAULT_VALUE;
        }

        builder.append(text, Mode.NORMALIZE);
    }

    /**
     * Process {@link HtmlInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlInput the target to process
     */
    protected void appendInput(final HtmlSerializerTextBuilder builder, final HtmlInput htmlInput) {
        builder.append(" ", Mode.NORMALIZE);
        builder.append(htmlInput.getValue(), Mode.NORMALIZE);
        builder.append(" ", Mode.NORMALIZE);
    }

    /**
     * Process {@link HtmlNumberInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlNumberInput the target to process
     */
    protected void appendNumberInput(final HtmlSerializerTextBuilder builder, final HtmlNumberInput htmlNumberInput) {
        builder.append(" ", Mode.NORMALIZE);

        String val = htmlNumberInput.getValueAttribute();
        final int lastPos = val.length() - 1;
        if (lastPos >= 0 && val.charAt(lastPos) == '.') {
            val = val.substring(0, lastPos);
        }
        builder.append(val, Mode.NORMALIZE);

        builder.append(" ", Mode.NORMALIZE);
    }

    /**
     * Process {@link HtmlResetInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlResetInput the target to process
     */
    protected void appendResetInput(final HtmlSerializerTextBuilder builder, final HtmlResetInput htmlResetInput) {
        String text = htmlResetInput.getValueAttribute();
        if (ATTRIBUTE_NOT_DEFINED == text) {
            text = HtmlResetInput.DEFAULT_VALUE;
        }

        builder.append(text, Mode.NORMALIZE);
    }

    /**
     * Process {@link HtmlUnorderedList}.
     * @param builder the StringBuilder to add to
     * @param htmlUnorderedList the target to process
     */
    protected void appendUnorderedList(final HtmlSerializerTextBuilder builder,
                                          final HtmlUnorderedList htmlUnorderedList) {
        builder.appendBlockSeparator();
        boolean first = true;
        for (final DomNode item : htmlUnorderedList.getChildren()) {
            if (!first) {
                builder.appendBlockSeparator();
            }
            first = false;
            appendNode(builder, item);
        }
        builder.appendBlockSeparator();
    }

    /**
     * Process {@link HtmlDetails}.
     * @param builder the StringBuilder to add to
     * @param htmlDetails the target to process
     */
    protected void appendDetails(final HtmlSerializerTextBuilder builder,
                    final HtmlDetails htmlDetails) {
        if (htmlDetails.isOpen()) {
            appendChildren(builder, htmlDetails);
            return;
        }

        for (final DomNode child : htmlDetails.getChildren()) {
            if (child instanceof HtmlSummary) {
                appendNode(builder, child);
            }
        }
    }

    /**
     * Process {@link HtmlTitle}.
     * @param builder the StringBuilder to add to
     * @param htmlTitle the target to process
     */
    protected void appendTitle(final HtmlSerializerTextBuilder builder, final HtmlTitle htmlTitle) {
        // optimized version
        // for the title there is no need to check the visibility
        // of the containing dom text;
        // this optimization defers the load of the style sheets
        final DomNode child = htmlTitle.getFirstChild();
        if (child instanceof DomText) {
            builder.append(((DomText) child).getData(), Mode.NORMALIZE);
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlTableRow}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlTableRow the target to process
     */
    protected void appendTableRow(final HtmlSerializerTextBuilder builder, final HtmlTableRow htmlTableRow) {
        boolean first = true;
        for (final HtmlTableCell cell : htmlTableRow.getCells()) {
            if (!first) {
                builder.appendTab();
            }
            else {
                first = false;
            }
            appendChildren(builder, cell); // trim?
        }
    }

    /**
     * Process {@link HtmlTextArea}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlTextArea the target to process
     */
    protected void appendTextArea(final HtmlSerializerTextBuilder builder, final HtmlTextArea htmlTextArea) {
        if (isVisible(htmlTextArea)) {
            builder.append(htmlTextArea.getText(), Mode.PRESERVE_BLANK_NEWLINE);
        }
    }

    /**
     * Process {@link HtmlTable}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlTable the target to process
     */
    protected void appendTable(final HtmlSerializerTextBuilder builder, final HtmlTable htmlTable) {
        builder.appendBlockSeparator();
        final String caption = htmlTable.getCaptionText();
        if (caption != null) {
            builder.append(caption, Mode.NORMALIZE);
            builder.appendBlockSeparator();
        }

        boolean first = true;

        // first thead has to be displayed first and first tfoot has to be displayed last
        final HtmlTableHeader tableHeader = htmlTable.getHeader();
        if (tableHeader != null) {
            first = appendTableRows(builder, tableHeader.getRows(), true, null, null);
        }
        final HtmlTableFooter tableFooter = htmlTable.getFooter();

        final List<HtmlTableRow> tableRows = htmlTable.getRows();
        first = appendTableRows(builder, tableRows, first, tableHeader, tableFooter);

        if (tableFooter != null) {
            first = appendTableRows(builder, tableFooter.getRows(), first, null, null);
        }
        else if (tableRows.isEmpty()) {
            final DomNode firstChild = htmlTable.getFirstChild();
            if (firstChild != null) {
                appendNode(builder, firstChild);
            }
        }

        builder.appendBlockSeparator();
    }

    /**
     * Process {@link HtmlTableRow}.
     *
     * @param builder the StringBuilder to add to
     * @param rows the rows
     * @param first if true this is the first one
     * @param skipParent1 skip row if the parent is this
     * @param skipParent2 skip row if the parent is this
     * @return true if this was the first one
     */
    protected boolean appendTableRows(final HtmlSerializerTextBuilder builder,
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
            appendTableRow(builder, row);
        }
        return first;
    }

    /**
     * Process {@link HtmlSelect}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlSelect the target to process
     */
    protected void appendSelect(final HtmlSerializerTextBuilder builder, final HtmlSelect htmlSelect) {
        final List<HtmlOption> options = htmlSelect.getSelectedOptions();

        for (final Iterator<HtmlOption> i = options.iterator(); i.hasNext();) {
            final HtmlOption currentOption = i.next();
            appendChildren(builder, currentOption);
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
     */
    protected void appendOrderedList(final HtmlSerializerTextBuilder builder, final HtmlOrderedList htmlOrderedList) {
        builder.appendBlockSeparator();
        boolean first = true;
        int i = 1;
        for (final DomNode item : htmlOrderedList.getChildren()) {
            if (!first) {
                builder.appendBlockSeparator();
            }
            first = false;
            if (item instanceof HtmlListItem) {
                builder.append(Integer.toString(i++), Mode.NORMALIZE);
                builder.append(". ", Mode.NORMALIZE);
                appendChildren(builder, item);
            }
            else {
                appendNode(builder, item);
            }
        }
        builder.appendBlockSeparator();
    }

    /**
     * Process {@link HtmlPreformattedText}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlPreformattedText the target to process
     */
    protected void appendPreformattedText(final HtmlSerializerTextBuilder builder,
            final HtmlPreformattedText htmlPreformattedText) {
        if (isVisible(htmlPreformattedText)) {
            builder.appendBlockSeparator();
            builder.append(htmlPreformattedText.getTextContent(), Mode.PRESERVE_BLANK_TAB_NEWLINE);
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlInlineFrame}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlInlineFrame the target to process
     */
    protected void appendInlineFrame(final HtmlSerializerTextBuilder builder,
            final HtmlInlineFrame htmlInlineFrame) {
        if (isVisible(htmlInlineFrame)) {
            builder.appendBlockSeparator();
            final Page page = htmlInlineFrame.getEnclosedPage();
            if (page instanceof SgmlPage) {
                builder.append(((SgmlPage) page).asNormalizedText(), Mode.NORMALIZE);
            }
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link DomText}.
     *
     * @param builder the StringBuilder to add to
     * @param domText the target to process
     */
    protected void appendText(final HtmlSerializerTextBuilder builder, final DomText domText) {
        final DomNode parent = domText.getParentNode();
        if (parent == null || parent instanceof HtmlTitle || isVisible(parent)) {
            builder.append(domText.getData(), Mode.NORMALIZE);
        }
    }

    /**
     * Process {@link HtmlBreak}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlBreak the target to process
     */
    protected void appendBreak(final HtmlSerializerTextBuilder builder, final HtmlBreak htmlBreak) {
        builder.appendNewLine();
    }

    /**
     * Process {@link HtmlCheckBoxInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlCheckBoxInput the target to process
     */
    protected void appendCheckBoxInput(final HtmlSerializerTextBuilder builder,
                                            final HtmlCheckBoxInput htmlCheckBoxInput) {
        if (htmlCheckBoxInput.isChecked()) {
            builder.append("checked", Mode.NORMALIZE);
        }
        else {
            builder.append("unchecked", Mode.NORMALIZE);
        }
    }

    /**
     * Process {@link HtmlRadioButtonInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlRadioButtonInput the target to process
     */
    protected void appendRadioButtonInput(final HtmlSerializerTextBuilder builder,
            final HtmlRadioButtonInput htmlRadioButtonInput) {
        if (htmlRadioButtonInput.isChecked()) {
            builder.append("checked", Mode.NORMALIZE);
        }
        else {
            builder.append("unchecked", Mode.NORMALIZE);
        }
    }

    private boolean isVisible(final DomNode node) {
        return !ignoreMaskedElements_ || node.isDisplayed();
    }

    /**
     * Indicates if element that are not displayed due to style settings
     * (visibility or display) should be visible in generated text.
     * @param ignore indicates if masked elements should be ignored or not
     */
    public void setIgnoreMaskedElements(final boolean ignore) {
        ignoreMaskedElements_ = ignore;
    }

    protected static class HtmlSerializerTextBuilder {
        /** Mode. */
        protected enum Mode {
            /** Collapse whitespace. */
            NORMALIZE,

            /** Preserve tab, blank, newline. */
            PRESERVE_BLANK_TAB_NEWLINE,

            /** Preserve blank, newline. */
            PRESERVE_BLANK_NEWLINE
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

        private static final String LINE_SEPARATOR = "\n";
        private static final int LINE_SEPARATOR_LENGTH = LINE_SEPARATOR.length();

        private State state_;
        private final StringBuilder builder_;
        private int trimRightPos_;

        public HtmlSerializerTextBuilder() {
            builder_ = new StringBuilder();
            state_ = State.EMPTY;
            trimRightPos_ = builder_.length();
        }

        public void append(final String content, final Mode mode) {
            final int length = content.length();
            if (length == 0) {
                return;
            }

            String text = content;
            if (mode == Mode.PRESERVE_BLANK_NEWLINE) {
                text = StringUtils.stripEnd(text, null);
            }

            boolean crFound = false;
            for (final char c : text.toCharArray()) {
                if (mode == Mode.NORMALIZE) {
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
                    crFound = false;
                }
                else {
                    if (crFound) {
                        appendNewLine();
                    }
                    crFound = c == '\r';

                    if (c == '\t') {
                        if (mode == Mode.PRESERVE_BLANK_TAB_NEWLINE) {
                            appendTab();
                        }
                        else if (state_ != State.BLOCK_SEPARATOR_AT_END) {
                            builder_.append(' ');
                        }
                    }
                    else if (c == (char) 160) {
                        appendBlank();
                    }
                    else if (c == ' ') {
                        appendBlank();
                    }
                    else {
                        builder_.append(c);
                    }
                    trimRightPos_ = builder_.length();
                }
            }

            if (crFound) {
                appendNewLine();
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
                        builder_.append(LINE_SEPARATOR);
                        state_ = State.BLOCK_SEPARATOR_AT_END;
                    }
                    break;
                case BLANK_AT_END_AFTER_NEWLINE:
                    builder_.setLength(trimRightPos_ - LINE_SEPARATOR_LENGTH);
                    trimRightPos_ = trimRightPos_ - LINE_SEPARATOR_LENGTH;
                    if (builder_.length() == 0) {
                        state_ = State.EMPTY;
                    }
                    else {
                        builder_.append(LINE_SEPARATOR);
                        state_ = State.BLOCK_SEPARATOR_AT_END;
                    }
                    break;
                case BLOCK_SEPARATOR_AT_END:
                    break;
                case NEWLINE_AT_END:
                    builder_.setLength(builder_.length() - LINE_SEPARATOR_LENGTH);
                    trimRightPos_ = trimRightPos_ - LINE_SEPARATOR_LENGTH;
                    if (builder_.length() == 0) {
                        state_ = State.EMPTY;
                    }
                    else {
                        builder_.append(LINE_SEPARATOR);
                        state_ = State.BLOCK_SEPARATOR_AT_END;
                    }
                    break;
                default:
                    builder_.append(LINE_SEPARATOR);
                    state_ = State.BLOCK_SEPARATOR_AT_END;
                    break;
            }
        }

        public void appendNewLine() {
            builder_.append(LINE_SEPARATOR);
            state_ = State.NEWLINE_AT_END;
            trimRightPos_ = builder_.length();
        }

        public void appendTab() {
            builder_.append('\t');
            trimRightPos_ = builder_.length();
        }

        private void appendBlank() {
            builder_.append(' ');
            trimRightPos_ = builder_.length();
        }

        public String getText() {
            return builder_.substring(0, trimRightPos_);
        }

        private static boolean isSpace(final char ch) {
            return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\f' || ch == '\r';
        }
    }
}
