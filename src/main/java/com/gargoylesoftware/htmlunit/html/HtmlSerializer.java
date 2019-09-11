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
import com.gargoylesoftware.htmlunit.html.HtmlSerializer.HtmlSerializerTextBuilder.Mode;
import com.gargoylesoftware.htmlunit.javascript.host.Element;

/**
 * Utility to handle conversion from HTML code to string.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Rob Kodey
 */
public class HtmlSerializer {

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
            appendComment(builder, (DomComment) node);
        }
        else if (node instanceof HtmlApplet
                && node.getPage().getWebClient().getOptions().isAppletEnabled()) {
            appendApplet(builder, (HtmlApplet) node);
        }
        else if (node instanceof HtmlBreak) {
            appendBreak(builder, (HtmlBreak) node);
        }
        else if (node instanceof HtmlHiddenInput) {
            appendHiddenInput(builder, (HtmlHiddenInput) node);
        }
        else if (node instanceof HtmlScript) {
            appendScript(builder, (HtmlScript) node);
        }
        else if (node instanceof HtmlStyle) {
            appendStyle(builder, (HtmlStyle) node);
        }
        else if (node instanceof HtmlNoFrames) {
            appendNoFrames(builder, (HtmlNoFrames) node);
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
            doAppendCheckBoxInput(builder, (HtmlCheckBoxInput) node);
        }
        else if (node instanceof HtmlRadioButtonInput) {
            doAppendRadioButtonInput(builder, (HtmlRadioButtonInput) node);
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
        else if (node instanceof HtmlNoScript && node.getPage().getWebClient().getOptions().isJavaScriptEnabled()) {
            appendNoScript(builder, (HtmlNoScript) node);
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
        appendChildren(builder, domNode);
        if (block) {
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlHiddenInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlHiddenInput the target to process
     */
    protected void appendHiddenInput(final HtmlSerializerTextBuilder builder, final HtmlHiddenInput htmlHiddenInput) {
        // nothing to do
    }

    /**
     * Process {@link HtmlScript}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlScript the target to process
     */
    protected void appendScript(final HtmlSerializerTextBuilder builder, final HtmlScript htmlScript) {
        // nothing to do
    }

    /**
     * Process {@link HtmlStyle}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlStyle the target to process
     */
    protected void appendStyle(final HtmlSerializerTextBuilder builder, final HtmlStyle htmlStyle) {
        // nothing to do
    }

    /**
     * Process {@link HtmlNoScript}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlNoScript the target to process
     */
    protected void appendNoScript(final HtmlSerializerTextBuilder builder, final HtmlNoScript htmlNoScript) {
        // nothing to do
    }

    /**
     * Process {@link HtmlNoFrames}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlNoFrames the target to process
     */
    protected void appendNoFrames(final HtmlSerializerTextBuilder builder, final HtmlNoFrames htmlNoFrames) {
        // nothing to do
    }

    /**
     * Process {@link HtmlSubmitInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlSubmitInput the target to process
     */
    protected void appendSubmitInput(final HtmlSerializerTextBuilder builder, final HtmlSubmitInput htmlSubmitInput) {
        builder.append(htmlSubmitInput.asText(), Mode.NORMALIZE);
    }

    /**
     * Process {@link HtmlInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlInput the target to process
     */
    protected void appendInput(final HtmlSerializerTextBuilder builder, final HtmlInput htmlInput) {
        builder.append(htmlInput.getValueAttribute(), Mode.NORMALIZE);
    }

    /**
     * Process {@link HtmlResetInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlResetInput the target to process
     */
    protected void appendResetInput(final HtmlSerializerTextBuilder builder, final HtmlResetInput htmlResetInput) {
        builder.append(htmlResetInput.asText(), Mode.NORMALIZE);
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
        final List<HtmlOption> options;
        if (htmlSelect.isMultipleSelectEnabled()) {
            options = htmlSelect.getOptions();
        }
        else {
            options = htmlSelect.getSelectedOptions();
        }

        for (final Iterator<HtmlOption> i = options.iterator(); i.hasNext();) {
            final HtmlOption currentOption = i.next();
            appendNode(builder, currentOption);
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
                builder.append(((SgmlPage) page).asText(), Mode.NORMALIZE);
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
     * Process {@link DomComment}.
     *
     * @param builder the StringBuilder to add to
     * @param domComment the target to process
     */
    protected void appendComment(final HtmlSerializerTextBuilder builder, final DomComment domComment) {
        // nothing to do
    }

    /**
     * Process {@link HtmlApplet}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlApplet the target to process
     */
    protected void appendApplet(final HtmlSerializerTextBuilder builder, final HtmlApplet htmlApplet) {
        // nothing to do
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
    protected void doAppendCheckBoxInput(final HtmlSerializerTextBuilder builder,
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
    protected void doAppendRadioButtonInput(final HtmlSerializerTextBuilder builder,
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
        /** Mode */
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
            BLANK_AT_END
        }

        /** Indicates a block. Will be rendered as line separator; multiple block marks are ignored. */
        private static final String BLOCK_SEPARATOR = "§bs§";
        private static final int BLOCK_SEPARATOR_LENGTH = BLOCK_SEPARATOR.length();

        /** Indicates a new line. Will be rendered as line separator. */
        private static final String NEW_LINE = "§nl§";
        private static final int NEW_LINE_LENGTH = NEW_LINE.length();

        /** Indicates a non blank that can't be trimmed or reduced. */
        private static final String BLANK = "§blank§";
        /** Indicates a tab. */
        private static final String TAB = "§tab§";

        private State state_;
        private final StringBuilder builder_;
        private int trimRightPos_;
        private boolean hasPreservedText_;

        public HtmlSerializerTextBuilder() {
            builder_ = new StringBuilder();
            state_ = State.EMPTY;
            hasPreservedText_ = false;
            trimRightPos_ = 0;
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
                            case BLANK_AT_END:
                                break;
                            default:
                                builder_.append(' ');
                                state_ = State.BLANK_AT_END;
                                break;
                            }
                    }
                    else if (c == (char) 160) {
                        builder_.append(BLANK);
                        hasPreservedText_ = true;
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
                        else {
                            builder_.append(' ');
                        }
                    }
                    else if (c == (char) 160) {
                        builder_.append(BLANK);
                        hasPreservedText_ = true;
                    }
                    else if (c == ' ') {
                        builder_.append(BLANK);
                        hasPreservedText_ = true;
                    }
                    else {
                        builder_.append(c);
                    }
                }
                trimRightPos_ = builder_.length();
            }

            if (crFound) {
                appendNewLine();
            }

            if (mode != Mode.NORMALIZE) {
                // reset state to empty to restart whitespace normalization afterwards
                state_ = State.EMPTY;
            }
        }

        public void appendBlockSeparator() {
            builder_.append(BLOCK_SEPARATOR);
            trimRightPos_ = builder_.length();
            hasPreservedText_ = true;
        }

        public void appendNewLine() {
            builder_.append(NEW_LINE);
            trimRightPos_ = builder_.length();
            hasPreservedText_ = true;
        }

        public void appendTab() {
            builder_.append(TAB);
            trimRightPos_ = builder_.length();
            hasPreservedText_ = true;
        }

        public String getText() {
            String response = builder_.substring(0, trimRightPos_);
            if (hasPreservedText_) {
                response = cleanUp(response);
            }
            return response;
        }

        /**
         * Reduce the whitespace and do some more cleanup.
         * @param text the text to clean up
         * @return the new text
         */
        protected String cleanUp(final String text) {
            // ignore <br/> at the end of a block
            String resultText = reduceWhitespace(text);

            final String ls = System.lineSeparator();
            resultText = StringUtils.replaceEach(resultText,
                    new String[] {BLANK, NEW_LINE, BLOCK_SEPARATOR, TAB},
                    new String[] {" ", ls, ls, "\t"});

            return resultText;
        }

        private String reduceWhitespace(final String text) {
            // remove white spaces before or after block separators
            String resultText = reduceWhiteSpaceAroundBlockSeparator(text);

            // remove leading block separators
            int start = 0;
            while (resultText.startsWith(BLOCK_SEPARATOR, start)) {
                start = start + BLOCK_SEPARATOR_LENGTH;
            }

            // remove trailing block separators
            int end = resultText.length() - BLOCK_SEPARATOR_LENGTH;
            while (end > start && resultText.startsWith(BLOCK_SEPARATOR, end)) {
                end = end - BLOCK_SEPARATOR_LENGTH;
            }
            resultText = resultText.substring(start, end + BLOCK_SEPARATOR_LENGTH);
            resultText = trim(resultText);

            return resultText;
        }

        private static boolean isSpace(final char ch) {
            return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\f' || ch == '\r';
        }

        private static String trim(final String string) {
            final int length = string.length();
            if (length == 0) {
                return string;
            }

            int start = 0;
            while (start != length && isSpace(string.charAt(start))) {
                start++;
            }
            if (start == length) {
                return "";
            }

            int end = length;
            while (end > start && isSpace(string.charAt(end - 1))) {
                end--;
            }
            if (end == length && start == 0) {
                return string;
            }
            return string.substring(start, end);
        }

        private static String reduceWhiteSpaceAroundBlockSeparator(final String text) {
            int p0 = text.indexOf(BLOCK_SEPARATOR);
            if (p0 == -1) {
                return text;
            }

            final int length = text.length();
            if (length <= BLOCK_SEPARATOR_LENGTH) {
                return text;
            }

            final StringBuilder result = new StringBuilder(length);
            int start = 0;
            while (p0 != -1) {
                int p1 = p0 + BLOCK_SEPARATOR_LENGTH;
                while (p0 != start && isSpace(text.charAt(p0 - 1))) {
                    p0--;
                }
                if (p0 >= NEW_LINE_LENGTH && text.startsWith(NEW_LINE, p0 - NEW_LINE_LENGTH)) {
                    p0 = p0 - NEW_LINE_LENGTH;
                }
                result.append(text.substring(start, p0)).append(BLOCK_SEPARATOR);

                while (p1 < length && isSpace(text.charAt(p1))) {
                    p1++;
                }
                start = p1;

                // ignore duplicates
                p0 = text.indexOf(BLOCK_SEPARATOR, start);
                while (p0 != -1 && p0 == start) {
                    start += BLOCK_SEPARATOR_LENGTH;
                    p0 = text.indexOf(BLOCK_SEPARATOR, start);
                }
            }
            if (start < length) {
                result.append(text.substring(start));
            }
            return result.toString();
        }
    }
}
