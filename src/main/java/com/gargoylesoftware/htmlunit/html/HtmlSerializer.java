/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

/**
 * Utility to handle conversion from HTML code to string.
 * TODO: simplify it (it is just copied from what was available in DomNode and subclasses).
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
class HtmlSerializer {
    private final StringBuilder buffer_ = new StringBuilder();
    /** Indicates a block. Will be rendered as line separator (multiple block marks are ignored) */
    protected static final String AS_TEXT_BLOCK_SEPARATOR = "§bs§";
    /** Indicates a new line. Will be rendered as line separator. */
    protected static final String AS_TEXT_NEW_LINE = "§nl§";
    /** Indicates a non blank that can't be trimmed or reduced. */
    protected static final String AS_TEXT_BLANK = "§blank§";
    /** Indicates a tab. */
    protected static final String AS_TEXT_TAB = "§tab§";
    private boolean appletEnabled_;
    private boolean ignoreMaskedElements_ = true;

    /**
     * Converts an HTML node to text.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     */
    public String asText(final DomNode node) {
        appletEnabled_ = node.getPage().getWebClient().isAppletEnabled();
        buffer_.setLength(0);
        appendNode(node);
        final String response = buffer_.toString();
        buffer_.setLength(0);
        return cleanUp(response);
    }

    private String cleanUp(String text) {
        // ignore <br/> at the end of a block
        text = text.replaceAll(AS_TEXT_NEW_LINE + AS_TEXT_BLOCK_SEPARATOR, AS_TEXT_BLOCK_SEPARATOR);
        text = reduceWhitespace(text);
        text = text.replaceAll(AS_TEXT_BLANK, " ");
        final String ls = System.getProperty("line.separator");
        text = text.replaceAll(AS_TEXT_NEW_LINE, ls);
        text = text.replaceAll("(?:" + AS_TEXT_BLOCK_SEPARATOR + ")+", ls); // many block sep => 1 new line
        text = text.replaceAll(AS_TEXT_TAB, "\t");

        return text;
    }

    protected String reduceWhitespace(String text) {
        text = text.trim();

        // remove white spaces before or after block separators
        text = text.replaceAll("\\s*" + AS_TEXT_BLOCK_SEPARATOR + "\\s*", AS_TEXT_BLOCK_SEPARATOR);

        // remove leading block separators
        while (text.startsWith(AS_TEXT_BLOCK_SEPARATOR)) {
            text = text.substring(AS_TEXT_BLOCK_SEPARATOR.length());
        }

        // remove trailing block separators
        while (text.endsWith(AS_TEXT_BLOCK_SEPARATOR)) {
            text = text.substring(0, text.length() - AS_TEXT_BLOCK_SEPARATOR.length());
        }
        text = text.trim();

        final StringBuilder buffer = new StringBuilder(text.length());

        boolean whitespace = false;
        for (final char ch : text.toCharArray()) {

            // Translate non-breaking space to regular space.
            if (ch == (char) 160) {
                buffer.append(' ');
                whitespace = false;
            }
            else {
                if (whitespace) {
                    if (!Character.isWhitespace(ch)) {
                        buffer.append(ch);
                        whitespace = false;
                    }
                }
                else {
                    if (Character.isWhitespace(ch)) {
                        whitespace = true;
                        buffer.append(' ');
                    }
                    else {
                        buffer.append(ch);
                    }
                }
            }
        }
        return buffer.toString();
    }

    protected void appendNode(final DomNode node) {
        if (node instanceof DomText) {
            appendText((DomText) node);
        }
        else if (node instanceof DomComment) {
            appendComment((DomComment) node);
        }
        else if ((node instanceof HtmlApplet) && appletEnabled_) {
            // nothing
        }
        else if (node instanceof HtmlBreak) {
            doAppendNewLine();
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
            appendHtmlSubmitInput((HtmlSubmitInput) node);
        }
        else if (node instanceof HtmlCheckBoxInput) {
            final String str;
            if (((HtmlCheckBoxInput) node).isChecked()) {
                str = "checked";
            }
            else {
                str = "unchecked";
            }
            doAppend(str);
        }
        else if (node instanceof HtmlRadioButtonInput) {
            final String str;
            if (((HtmlRadioButtonInput) node).isChecked()) {
                str = "checked";
            }
            else {
                str = "unchecked";
            }
            doAppend(str);
        }
        else if (node instanceof HtmlInput) {
            doAppend(((HtmlInput) node).getValueAttribute());
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
        else if (node instanceof HtmlNoScript && node.getPage().getWebClient().isJavaScriptEnabled()) {
            return;
        }
        else {
            final boolean block = node.isBlock();
            if (block) {
                doAppendBlockSeparator();
            }
            appendChildren(node);
            if (block) {
                doAppendBlockSeparator();
            }
        }
    }

    private void doAppendBlockSeparator() {
        buffer_.append(AS_TEXT_BLOCK_SEPARATOR);
    }

    private void doAppend(final String str) {
        buffer_.append(str);
    }

    private void doAppendNewLine() {
        buffer_.append(AS_TEXT_NEW_LINE);
    }

    private void doAppendTab() {
        buffer_.append(AS_TEXT_TAB);
    }

    private void appendHtmlUnorderedList(final HtmlUnorderedList htmlUnorderedList) {
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

    private void appendHtmlTitle(final HtmlTitle htmlTitle) {
        appendChildren(htmlTitle);
        doAppendBlockSeparator();
    }

    private void appendChildren(final DomNode node) {
        for (final DomNode child : node.getChildren()) {
            appendNode(child);
        }
    }

    private void appendHtmlTableRow(final HtmlTableRow htmlTableRow) {
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

    private void appendHtmlTextArea(final HtmlTextArea htmlTextArea) {
        if (isVisible(htmlTextArea)) {
            String text = htmlTextArea.getText();
            text = text.replaceAll(" ", AS_TEXT_BLANK);
            text = text.replaceAll("\r?\n", AS_TEXT_NEW_LINE);
            text = text.replaceAll("\r", AS_TEXT_NEW_LINE);
            doAppend(text);
        }
    }

    private void appendHtmlTable(final HtmlTable htmlTable) {
        doAppendBlockSeparator();
        final String caption = htmlTable.getCaptionText();
        if (caption != null) {
            doAppend(caption);
            doAppendBlockSeparator();
        }

        boolean first = true;

        // first thead has to be displayed first and first tfoot has to be displayed last
        final HtmlTableHeader tableHeader = htmlTable.getHeader();
        if (tableHeader != null) {
            first = appendHtmlTableRows(tableHeader.getRows(), true, null, null);
        }
        final HtmlTableFooter tableFooter = htmlTable.getFooter();

        first = appendHtmlTableRows(htmlTable.getRows(), first, tableHeader, tableFooter);

        if (tableFooter != null) {
            first = appendHtmlTableRows(tableFooter.getRows(), first, null, null);
        }

        doAppendBlockSeparator();
    }

    private boolean appendHtmlTableRows(final List<HtmlTableRow> rows, boolean first, final TableRowGroup skipParent1,
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

    private void appendHtmlSubmitInput(final HtmlSubmitInput htmlSubmitInput) {
        String value = htmlSubmitInput.getValueAttribute();
        if (value == HtmlOption.ATTRIBUTE_NOT_DEFINED) {
            value = "Submit Query";
        }

        doAppend(value);
    }

    /**
     * @param htmlSelect
     */
    private void appendHtmlSelect(final HtmlSelect htmlSelect) {
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
     * Appends a &lt;ol&gt; taking care to numerate it.
     * @param htmlOrderedList the OL element
     */
    private void appendHtmlOrderedList(final HtmlOrderedList htmlOrderedList) {
        doAppendBlockSeparator();
        boolean first = true;
        int i = 1;
        for (final DomNode item : htmlOrderedList.getChildren()) {
            if (!(item instanceof HtmlListItem)) {
                continue;
            }
            if (!first) {
                doAppendBlockSeparator();
            }
            first = false;
            doAppend(String.valueOf(i++));
            doAppend(". ");
            appendChildren(item);
        }
        doAppendBlockSeparator();
    }

    private void appendText(final DomText domText) {
        if (isVisible(domText.getParentNode())) {
            append(domText.getData());
        }
    }

    private void appendComment(final DomComment child) {
        // nothing to do
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

    private void append(final String text) {
        doAppend(text);
    }
}
