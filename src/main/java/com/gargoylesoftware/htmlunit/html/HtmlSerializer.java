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
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
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
    /** Indicates a block. Will be rendered as line separator (multiple block marks are ignored) */
    protected static final String AS_TEXT_BLOCK_SEPARATOR = "§bs§";
    private static final int AS_TEXT_BLOCK_SEPARATOR_LENGTH = AS_TEXT_BLOCK_SEPARATOR.length();

    /** Indicates a new line. Will be rendered as line separator. */
    protected static final String AS_TEXT_NEW_LINE = "§nl§";
    private static final int AS_TEXT_NEW_LINE_LENGTH = AS_TEXT_NEW_LINE.length();

    /** Indicates a non blank that can't be trimmed or reduced. */
    protected static final String AS_TEXT_BLANK = "§blank§";
    /** Indicates a tab. */
    protected static final String AS_TEXT_TAB = "§tab§";

    private static final Pattern TEXT_AREA_PATTERN = Pattern.compile("\r?\n");

    private boolean ignoreMaskedElements_ = true;

    /**
     * Converts an HTML node to text.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     */
    public String asText(final DomNode node) {
        final StringBuilder builder = new StringBuilder();
        appendNode(builder, node);
        final String response = builder.toString();
        return cleanUp(response);
    }

    /**
     * Reduce the whitespace and do some more cleanup.
     * @param text the text to clean up
     * @return the new text
     */
    protected String cleanUp(String text) {
        // ignore <br/> at the end of a block
        text = reduceWhitespace(text);
        text = StringUtils.replace(text, AS_TEXT_BLANK, " ");
        final String ls = System.lineSeparator();
        text = StringUtils.replace(text, AS_TEXT_NEW_LINE, ls);
        text = StringUtils.replace(text, AS_TEXT_BLOCK_SEPARATOR, ls);
        text = StringUtils.replace(text, AS_TEXT_TAB, "\t");

        return text;
    }

    private static String reduceWhitespace(String text) {
        text = trim(text);

        // remove white spaces before or after block separators
        text = reduceWhiteSpaceAroundBlockSeparator(text);

        // remove leading block separators
        while (text.startsWith(AS_TEXT_BLOCK_SEPARATOR)) {
            text = text.substring(AS_TEXT_BLOCK_SEPARATOR_LENGTH);
        }

        // remove trailing block separators
        while (text.endsWith(AS_TEXT_BLOCK_SEPARATOR)) {
            text = text.substring(0, text.length() - AS_TEXT_BLOCK_SEPARATOR_LENGTH);
        }
        text = trim(text);

        final StringBuilder builder = new StringBuilder(text.length());

        boolean whitespace = false;
        for (final char ch : text.toCharArray()) {

            // Translate non-breaking space to regular space.
            if (ch == (char) 160) {
                builder.append(' ');
                whitespace = false;
            }
            else {
                if (whitespace) {
                    if (!isSpace(ch)) {
                        builder.append(ch);
                        whitespace = false;
                    }
                }
                else {
                    if (isSpace(ch)) {
                        whitespace = true;
                        builder.append(' ');
                    }
                    else {
                        builder.append(ch);
                    }
                }
            }
        }
        return builder.toString();
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
        int p0 = text.indexOf(AS_TEXT_BLOCK_SEPARATOR);
        if (p0 == -1) {
            return text;
        }

        final int length = text.length();
        if (length <= AS_TEXT_BLOCK_SEPARATOR_LENGTH) {
            return text;
        }

        final StringBuilder result = new StringBuilder(length);
        int start = 0;
        while (p0 != -1) {
            int p1 = p0 + AS_TEXT_BLOCK_SEPARATOR_LENGTH;
            while (p0 != start && isSpace(text.charAt(p0 - 1))) {
                p0--;
            }
            if (p0 >= AS_TEXT_NEW_LINE_LENGTH && text.startsWith(AS_TEXT_NEW_LINE, p0 - AS_TEXT_NEW_LINE_LENGTH)) {
                p0 = p0 - AS_TEXT_NEW_LINE_LENGTH;
            }
            result.append(text.substring(start, p0)).append(AS_TEXT_BLOCK_SEPARATOR);

            while (p1 < length && isSpace(text.charAt(p1))) {
                p1++;
            }
            start = p1;

            // ignore duplicates
            p0 = text.indexOf(AS_TEXT_BLOCK_SEPARATOR, start);
            while (p0 != -1 && p0 == start) {
                start += AS_TEXT_BLOCK_SEPARATOR_LENGTH;
                p0 = text.indexOf(AS_TEXT_BLOCK_SEPARATOR, start);
            }
        }
        if (start < length) {
            result.append(text.substring(start));
        }
        return result.toString();
    }

    /**
     * Iterate over all Children and call appendNode() for every.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     */
    protected void appendChildren(final StringBuilder builder, final DomNode node) {
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
    protected void appendNode(final StringBuilder builder, final DomNode node) {
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
    protected void appendDomNode(final StringBuilder builder, final DomNode domNode) {
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
            builder.append(AS_TEXT_BLOCK_SEPARATOR);
        }
        appendChildren(builder, domNode);
        if (block) {
            builder.append(AS_TEXT_BLOCK_SEPARATOR);
        }
    }

    /**
     * Process {@link HtmlHiddenInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlHiddenInput the target to process
     */
    protected void appendHiddenInput(final StringBuilder builder, final HtmlHiddenInput htmlHiddenInput) {
        // nothing to do
    }

    /**
     * Process {@link HtmlScript}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlScript the target to process
     */
    protected void appendScript(final StringBuilder builder, final HtmlScript htmlScript) {
        // nothing to do
    }

    /**
     * Process {@link HtmlStyle}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlStyle the target to process
     */
    protected void appendStyle(final StringBuilder builder, final HtmlStyle htmlStyle) {
        // nothing to do
    }

    /**
     * Process {@link HtmlNoScript}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlNoScript the target to process
     */
    protected void appendNoScript(final StringBuilder builder, final HtmlNoScript htmlNoScript) {
        // nothing to do
    }

    /**
     * Process {@link HtmlNoFrames}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlNoFrames the target to process
     */
    protected void appendNoFrames(final StringBuilder builder, final HtmlNoFrames htmlNoFrames) {
        // nothing to do
    }

    /**
     * Process {@link HtmlSubmitInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlSubmitInput the target to process
     */
    protected void appendSubmitInput(final StringBuilder builder, final HtmlSubmitInput htmlSubmitInput) {
        builder.append(htmlSubmitInput.asText());
    }

    /**
     * Process {@link HtmlInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlInput the target to process
     */
    protected void appendInput(final StringBuilder builder, final HtmlInput htmlInput) {
        builder.append(htmlInput.getValueAttribute());
    }

    /**
     * Process {@link HtmlResetInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlResetInput the target to process
     */
    protected void appendResetInput(final StringBuilder builder, final HtmlResetInput htmlResetInput) {
        builder.append(htmlResetInput.asText());
    }

    /**
     * Process {@link HtmlUnorderedList}.
     * @param builder the StringBuilder to add to
     * @param htmlUnorderedList the target to process
     */
    protected void appendUnorderedList(final StringBuilder builder, final HtmlUnorderedList htmlUnorderedList) {
        builder.append(AS_TEXT_BLOCK_SEPARATOR);
        boolean first = true;
        for (final DomNode item : htmlUnorderedList.getChildren()) {
            if (!first) {
                builder.append(AS_TEXT_BLOCK_SEPARATOR);
            }
            first = false;
            appendNode(builder, item);
        }
        builder.append(AS_TEXT_BLOCK_SEPARATOR);
    }

    /**
     * Process {@link HtmlTitle}.
     * @param builder the StringBuilder to add to
     * @param htmlTitle the target to process
     */
    protected void appendTitle(final StringBuilder builder, final HtmlTitle htmlTitle) {
        // optimized version
        // for the title there is no need to check the visibility
        // of the containing dom text;
        // this optimization defers the load of the style sheets
        final DomNode child = htmlTitle.getFirstChild();
        if (child instanceof DomText) {
            builder.append(((DomText) child).getData());
            builder.append(AS_TEXT_BLOCK_SEPARATOR);
        }
    }

    /**
     * Process {@link HtmlTableRow}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlTableRow the target to process
     */
    protected void appendTableRow(final StringBuilder builder, final HtmlTableRow htmlTableRow) {
        boolean first = true;
        for (final HtmlTableCell cell : htmlTableRow.getCells()) {
            if (!first) {
                builder.append(AS_TEXT_TAB);
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
    protected void appendTextArea(final StringBuilder builder, final HtmlTextArea htmlTextArea) {
        if (isVisible(htmlTextArea)) {
            String text = htmlTextArea.getText();
            text = StringUtils.stripEnd(text, null);
            text = TEXT_AREA_PATTERN.matcher(text).replaceAll(AS_TEXT_NEW_LINE);
            text = StringUtils.replace(text, "\r", AS_TEXT_NEW_LINE);
            text = StringUtils.replace(text, " ", AS_TEXT_BLANK);
            builder.append(text);
        }
    }

    /**
     * Process {@link HtmlTable}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlTable the target to process
     */
    protected void appendTable(final StringBuilder builder, final HtmlTable htmlTable) {
        builder.append(AS_TEXT_BLOCK_SEPARATOR);
        final String caption = htmlTable.getCaptionText();
        if (caption != null) {
            builder.append(caption);
            builder.append(AS_TEXT_BLOCK_SEPARATOR);
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

        builder.append(AS_TEXT_BLOCK_SEPARATOR);
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
    protected boolean appendTableRows(final StringBuilder builder,
            final List<HtmlTableRow> rows, boolean first, final TableRowGroup skipParent1,
            final TableRowGroup skipParent2) {
        for (final HtmlTableRow row : rows) {
            if (row.getParentNode() == skipParent1 || row.getParentNode() == skipParent2) {
                continue;
            }
            if (!first) {
                builder.append(AS_TEXT_BLOCK_SEPARATOR);
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
    protected void appendSelect(final StringBuilder builder, final HtmlSelect htmlSelect) {
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
                builder.append(AS_TEXT_BLOCK_SEPARATOR);
            }
        }
    }

    /**
     * Process {@link HtmlOrderedList} taking care to numerate it.
     *
     * @param builder the StringBuilder to add to
     * @param htmlOrderedList the OL element
     */
    protected void appendOrderedList(final StringBuilder builder, final HtmlOrderedList htmlOrderedList) {
        builder.append(AS_TEXT_BLOCK_SEPARATOR);
        boolean first = true;
        int i = 1;
        for (final DomNode item : htmlOrderedList.getChildren()) {
            if (!first) {
                builder.append(AS_TEXT_BLOCK_SEPARATOR);
            }
            first = false;
            if (item instanceof HtmlListItem) {
                builder.append(Integer.toString(i++));
                builder.append(". ");
                appendChildren(builder, item);
            }
            else {
                appendNode(builder, item);
            }
        }
        builder.append(AS_TEXT_BLOCK_SEPARATOR);
    }

    /**
     * Process {@link HtmlPreformattedText}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlPreformattedText the target to process
     */
    protected void appendPreformattedText(final StringBuilder builder,
            final HtmlPreformattedText htmlPreformattedText) {
        if (isVisible(htmlPreformattedText)) {
            builder.append(AS_TEXT_BLOCK_SEPARATOR);
            String text = htmlPreformattedText.getTextContent();
            text = StringUtils.replace(text, "\t", AS_TEXT_TAB);
            text = StringUtils.replace(text, " ", AS_TEXT_BLANK);
            text = TEXT_AREA_PATTERN.matcher(text).replaceAll(AS_TEXT_NEW_LINE);
            text = StringUtils.replace(text, "\r", AS_TEXT_NEW_LINE);
            builder.append(text);
            builder.append(AS_TEXT_BLOCK_SEPARATOR);
        }
    }

    /**
     * Process {@link HtmlInlineFrame}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlInlineFrame the target to process
     */
    protected void appendInlineFrame(final StringBuilder builder,
            final HtmlInlineFrame htmlInlineFrame) {
        if (isVisible(htmlInlineFrame)) {
            builder.append(AS_TEXT_BLOCK_SEPARATOR);
            final Page page = htmlInlineFrame.getEnclosedPage();
            if (page instanceof SgmlPage) {
                builder.append(((SgmlPage) page).asText());
            }
            builder.append(AS_TEXT_BLOCK_SEPARATOR);
        }
    }

    /**
     * Process {@link DomText}.
     *
     * @param builder the StringBuilder to add to
     * @param domText the target to process
     */
    protected void appendText(final StringBuilder builder, final DomText domText) {
        final DomNode parent = domText.getParentNode();
        if (parent == null || parent instanceof HtmlTitle || isVisible(parent)) {
            builder.append(domText.getData());
        }
    }

    /**
     * Process {@link DomComment}.
     *
     * @param builder the StringBuilder to add to
     * @param domComment the target to process
     */
    protected void appendComment(final StringBuilder builder, final DomComment domComment) {
        // nothing to do
    }

    /**
     * Process {@link HtmlApplet}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlApplet the target to process
     */
    protected void appendApplet(final StringBuilder builder, final HtmlApplet htmlApplet) {
        // nothing to do
    }

    /**
     * Process {@link HtmlBreak}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlBreak the target to process
     */
    protected void appendBreak(final StringBuilder builder, final HtmlBreak htmlBreak) {
        builder.append(AS_TEXT_NEW_LINE);
    }

    /**
     * Process {@link HtmlCheckBoxInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlCheckBoxInput the target to process
     */
    protected void doAppendCheckBoxInput(final StringBuilder builder, final HtmlCheckBoxInput htmlCheckBoxInput) {
        if (htmlCheckBoxInput.isChecked()) {
            builder.append("checked");
        }
        else {
            builder.append("unchecked");
        }
    }

    /**
     * Process {@link HtmlRadioButtonInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlRadioButtonInput the target to process
     */
    protected void doAppendRadioButtonInput(final StringBuilder builder,
            final HtmlRadioButtonInput htmlRadioButtonInput) {
        if (htmlRadioButtonInput.isChecked()) {
            builder.append("checked");
        }
        else {
            builder.append("unchecked");
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
}
