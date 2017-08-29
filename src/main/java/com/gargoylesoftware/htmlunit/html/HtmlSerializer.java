/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;

/**
 * Utility to handle conversion from HTML code to string.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Rob Kodey
 */
public class HtmlSerializer {
    private final StringBuilder builder_ = new StringBuilder();

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

    private boolean appletEnabled_;
    private boolean ignoreMaskedElements_ = true;

    /**
     * Converts an HTML node to text.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     */
    public String asText(final DomNode node) {
        appletEnabled_ = node.getPage().getWebClient().getOptions().isAppletEnabled();
        builder_.setLength(0);
        appendNode(node);
        final String response = builder_.toString();
        builder_.setLength(0);
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

    private static String trim(String string) {
        int length = string.length();

        int start = 0;
        while (start != length && isSpace(string.charAt(start))) {
            start++;
        }
        if (start != 0) {
            string = string.substring(start);
            length = string.length();
        }

        if (length != 0) {
            int end = length;
            while (end != 0 && isSpace(string.charAt(end - 1))) {
                end--;
            }
            if (end != length) {
                string = string.substring(0, end);
            }
        }

        return string;
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
     * @param node the node to process
     */
    protected void appendChildren(final DomNode node) {
        for (final DomNode child : node.getChildren()) {
            appendNode(child);
        }
    }

    /**
     * The core distribution method call the different appendXXX
     * methods depending on the type of the given node.
     *
     * @param node the node to process
     */
    protected void appendNode(final DomNode node) {
        if (node instanceof DomText) {
            appendText((DomText) node);
        }
        else if (node instanceof DomComment) {
            appendComment((DomComment) node);
        }
        else if (node instanceof HtmlApplet && appletEnabled_) {
            appendApplet((HtmlApplet) node);
        }
        else if (node instanceof HtmlBreak) {
            appendBreak((HtmlBreak) node);
        }
        else if (node instanceof HtmlHiddenInput
                || node instanceof HtmlScript
                || node instanceof HtmlStyle
                || node instanceof HtmlNoFrames) {
            // nothing
        }
        else if (node instanceof HtmlTextArea) {
            appendHtmlTextArea((HtmlTextArea) node);
        }
        else if (node instanceof HtmlTitle) {
            appendHtmlTitle((HtmlTitle) node);
        }
        else if (node instanceof HtmlTableRow) {
            appendHtmlTableRow((HtmlTableRow) node);
        }
        else if (node instanceof HtmlSelect) {
            appendHtmlSelect((HtmlSelect) node);
        }
        else if (node instanceof HtmlSubmitInput) {
            doAppendString(((HtmlSubmitInput) node).asText());
        }
        else if (node instanceof HtmlResetInput) {
            doAppendString(((HtmlResetInput) node).asText());
        }
        else if (node instanceof HtmlCheckBoxInput) {
            doAppendCheckBoxInput((HtmlCheckBoxInput) node);
        }
        else if (node instanceof HtmlRadioButtonInput) {
            doAppendRadioButtonInput((HtmlRadioButtonInput) node);
        }
        else if (node instanceof HtmlInput) {
            doAppendString(((HtmlInput) node).getValueAttribute());
        }
        else if (node instanceof HtmlTable) {
            appendHtmlTable((HtmlTable) node);
        }
        else if (node instanceof HtmlOrderedList) {
            appendHtmlOrderedList((HtmlOrderedList) node);
        }
        else if (node instanceof HtmlUnorderedList) {
            appendHtmlUnorderedList((HtmlUnorderedList) node);
        }
        else if (node instanceof HtmlPreformattedText) {
            appendHtmlPreformattedText((HtmlPreformattedText) node);
        }
        else if (node instanceof HtmlInlineFrame) {
            appendHtmlInlineFrame((HtmlInlineFrame) node);
        }
        else if (node instanceof HtmlNoScript && node.getPage().getWebClient().getOptions().isJavaScriptEnabled()) {
            // nothing
        }
        else {
            final boolean block;
            final Object scriptableObject = node.getScriptableObject();
            if (node instanceof HtmlBody) {
                block = false;
            }
            else if (scriptableObject instanceof Element) {
                final Element element = (Element) scriptableObject;
                final String display = element.getWindow().getComputedStyle(element, null).getDisplay(true);
                block = "block".equals(display);
            }
            else if (scriptableObject instanceof Element2) {
                final Element2 element = (Element2) scriptableObject;
                final String display = Window2.getComputedStyle(element.getWindow(), element, null).getDisplay(true);
                block = "block".equals(display);
            }
            else {
                block = false;
            }

            if (block) {
                doAppendBlockSeparator();
            }
            appendChildren(node);
            if (block) {
                doAppendBlockSeparator();
            }
        }
    }

    /**
     * Helper appending AS_TEXT_BLOCK_SEPARATOR to the result.
     */
    protected void doAppendBlockSeparator() {
        builder_.append(AS_TEXT_BLOCK_SEPARATOR);
    }

    /**
     * Helper appending the given string to the result.
     * @param str the string to append
     */
    protected void doAppendString(final String str) {
        builder_.append(str);
    }

    /**
     * Helper appending AS_TEXT_NEW_LINE to the result.
     */
    protected void doAppendNewLine() {
        builder_.append(AS_TEXT_NEW_LINE);
    }

    /**
     * Helper appending AS_TEXT_TAB to the result.
     */
    protected void doAppendTab() {
        builder_.append(AS_TEXT_TAB);
    }

    /**
     * Process {@link HtmlUnorderedList}.
     * @param htmlUnorderedList the target to process
     */
    protected void appendHtmlUnorderedList(final HtmlUnorderedList htmlUnorderedList) {
        doAppendBlockSeparator();
        boolean first = true;
        for (final DomNode item : htmlUnorderedList.getChildren()) {
            if (!first) {
                doAppendBlockSeparator();
            }
            first = false;
            appendNode(item);
        }
        doAppendBlockSeparator();
    }

    /**
     * Process {@link HtmlTitle}.
     * @param htmlTitle the target to process
     */
    protected void appendHtmlTitle(final HtmlTitle htmlTitle) {
        // optimized version
        // for the title there is no need to check the visibility
        // of the containing dom text;
        // this optimization defers the load of the style sheets
        final DomNode child = htmlTitle.getFirstChild();
        if (child instanceof DomText) {
            doAppendString(((DomText) child).getData());
            doAppendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlTableRow}.
     * @param htmlTableRow the target to process
     */
    protected void appendHtmlTableRow(final HtmlTableRow htmlTableRow) {
        boolean first = true;
        for (final HtmlTableCell cell : htmlTableRow.getCells()) {
            if (!first) {
                doAppendTab();
            }
            else {
                first = false;
            }
            appendChildren(cell); // trim?
        }
    }

    /**
     * Process {@link HtmlTextArea}.
     * @param htmlTextArea the target to process
     */
    protected void appendHtmlTextArea(final HtmlTextArea htmlTextArea) {
        if (isVisible(htmlTextArea)) {
            String text = htmlTextArea.getText();
            text = StringUtils.stripEnd(text, null);
            text = TEXT_AREA_PATTERN.matcher(text).replaceAll(AS_TEXT_NEW_LINE);
            text = StringUtils.replace(text, "\r", AS_TEXT_NEW_LINE);
            text = StringUtils.replace(text, " ", AS_TEXT_BLANK);
            doAppendString(text);
        }
    }

    /**
     * Process {@link HtmlTable}.
     * @param htmlTable the target to process
     */
    protected void appendHtmlTable(final HtmlTable htmlTable) {
        doAppendBlockSeparator();
        final String caption = htmlTable.getCaptionText();
        if (caption != null) {
            doAppendString(caption);
            doAppendBlockSeparator();
        }

        boolean first = true;

        // first thead has to be displayed first and first tfoot has to be displayed last
        final HtmlTableHeader tableHeader = htmlTable.getHeader();
        if (tableHeader != null) {
            first = appendHtmlTableRows(tableHeader.getRows(), true, null, null);
        }
        final HtmlTableFooter tableFooter = htmlTable.getFooter();

        final List<HtmlTableRow> tableRows = htmlTable.getRows();
        first = appendHtmlTableRows(tableRows, first, tableHeader, tableFooter);

        if (tableFooter != null) {
            first = appendHtmlTableRows(tableFooter.getRows(), first, null, null);
        }
        else if (tableRows.isEmpty()) {
            final DomNode firstChild = htmlTable.getFirstChild();
            if (firstChild != null) {
                appendNode(firstChild);
            }
        }

        doAppendBlockSeparator();
    }

    /**
     * Process {@link HtmlTableRow}.
     * @param rows the rows
     * @param first if true this is the first one
     * @param skipParent1 skip row if the parent is this
     * @param skipParent2 skip row if the parent is this
     * @return true if this was the first one
     */
    protected boolean appendHtmlTableRows(final List<HtmlTableRow> rows, boolean first, final TableRowGroup skipParent1,
            final TableRowGroup skipParent2) {
        for (final HtmlTableRow row : rows) {
            if (row.getParentNode() == skipParent1 || row.getParentNode() == skipParent2) {
                continue;
            }
            if (!first) {
                doAppendBlockSeparator();
            }
            first = false;
            appendHtmlTableRow(row);
        }
        return first;
    }

    /**
     * Process {@link HtmlSelect}.
     * @param htmlSelect the target to process
     */
    protected void appendHtmlSelect(final HtmlSelect htmlSelect) {
        final List<HtmlOption> options;
        if (htmlSelect.isMultipleSelectEnabled()) {
            options = htmlSelect.getOptions();
        }
        else {
            options = htmlSelect.getSelectedOptions();
        }

        for (final Iterator<HtmlOption> i = options.iterator(); i.hasNext();) {
            final HtmlOption currentOption = i.next();
            appendNode(currentOption);
            if (i.hasNext()) {
                doAppendBlockSeparator();
            }
        }
    }

    /**
     * Process {@link HtmlOrderedList} taking care to numerate it.
     * @param htmlOrderedList the OL element
     */
    protected void appendHtmlOrderedList(final HtmlOrderedList htmlOrderedList) {
        doAppendBlockSeparator();
        boolean first = true;
        int i = 1;
        for (final DomNode item : htmlOrderedList.getChildren()) {
            if (!first) {
                doAppendBlockSeparator();
            }
            first = false;
            if (item instanceof HtmlListItem) {
                doAppendString(Integer.toString(i++));
                doAppendString(". ");
                appendChildren(item);
            }
            else {
                appendNode(item);
            }
        }
        doAppendBlockSeparator();
    }

    /**
     * Process {@link HtmlPreformattedText}.
     * @param htmlPreformattedText the target to process
     */
    protected void appendHtmlPreformattedText(final HtmlPreformattedText htmlPreformattedText) {
        if (isVisible(htmlPreformattedText)) {
            doAppendBlockSeparator();
            String text = htmlPreformattedText.getTextContent();
            text = StringUtils.replace(text, "\t", AS_TEXT_TAB);
            text = StringUtils.replace(text, " ", AS_TEXT_BLANK);
            text = TEXT_AREA_PATTERN.matcher(text).replaceAll(AS_TEXT_NEW_LINE);
            text = StringUtils.replace(text, "\r", AS_TEXT_NEW_LINE);
            doAppendString(text);
            doAppendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlInlineFrame}.
     * @param htmlInlineFrame the target to process
     */
    protected void appendHtmlInlineFrame(final HtmlInlineFrame htmlInlineFrame) {
        if (isVisible(htmlInlineFrame)) {
            doAppendBlockSeparator();
            final Page page = htmlInlineFrame.getEnclosedPage();
            if (page instanceof SgmlPage) {
                doAppendString(((SgmlPage) page).asText());
            }
            doAppendBlockSeparator();
        }
    }

    /**
     * Process {@link DomText}.
     * @param domText the target to process
     */
    protected void appendText(final DomText domText) {
        final DomNode parent = domText.getParentNode();
        if (parent == null || parent instanceof HtmlTitle || isVisible(parent)) {
            doAppendString(domText.getData());
        }
    }

    /**
     * Process {@link DomComment}.
     * @param domComment the target to process
     */
    protected void appendComment(final DomComment domComment) {
        // nothing to do
    }

    /**
     * Process {@link HtmlApplet}.
     * @param htmlApplet the target to process
     */
    protected void appendApplet(final HtmlApplet htmlApplet) {
        // nothing to do
    }

    /**
     * Process {@link HtmlBreak}.
     * @param htmlBreak the target to process
     */
    protected void appendBreak(final HtmlBreak htmlBreak) {
        doAppendNewLine();
    }

    /**
     * Process {@link HtmlCheckBoxInput}.
     * @param htmlCheckBoxInput the target to process
     */
    protected void doAppendCheckBoxInput(final HtmlCheckBoxInput htmlCheckBoxInput) {
        if (htmlCheckBoxInput.isChecked()) {
            doAppendString("checked");
        }
        else {
            doAppendString("unchecked");
        }
    }

    /**
     * Process {@link HtmlRadioButtonInput}.
     * @param htmlRadioButtonInput the target to process
     */
    protected void doAppendRadioButtonInput(final HtmlRadioButtonInput htmlRadioButtonInput) {
        if (htmlRadioButtonInput.isChecked()) {
            doAppendString("checked");
        }
        else {
            doAppendString("unchecked");
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
