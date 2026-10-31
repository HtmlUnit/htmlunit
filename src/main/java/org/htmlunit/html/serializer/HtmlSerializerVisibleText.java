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

import static org.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_SVG_NL;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.htmlunit.Page;
import org.htmlunit.WebWindow;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.css.StyleAttributes.Definition;
import org.htmlunit.html.DomCDataSection;
import org.htmlunit.html.DomComment;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.HtmlBody;
import org.htmlunit.html.HtmlBreak;
import org.htmlunit.html.HtmlCheckBoxInput;
import org.htmlunit.html.HtmlDetails;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlExample;
import org.htmlunit.html.HtmlHiddenInput;
import org.htmlunit.html.HtmlInlineFrame;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlListing;
import org.htmlunit.html.HtmlMenu;
import org.htmlunit.html.HtmlNoFrames;
import org.htmlunit.html.HtmlNoScript;
import org.htmlunit.html.HtmlOption;
import org.htmlunit.html.HtmlOrderedList;
import org.htmlunit.html.HtmlPlainText;
import org.htmlunit.html.HtmlPreformattedText;
import org.htmlunit.html.HtmlRadioButtonInput;
import org.htmlunit.html.HtmlResetInput;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.html.HtmlSelect;
import org.htmlunit.html.HtmlStyle;
import org.htmlunit.html.HtmlSubmitInput;
import org.htmlunit.html.HtmlSummary;
import org.htmlunit.html.HtmlSvg;
import org.htmlunit.html.HtmlTable;
import org.htmlunit.html.HtmlTableBody;
import org.htmlunit.html.HtmlTableCell;
import org.htmlunit.html.HtmlTableFooter;
import org.htmlunit.html.HtmlTableHeader;
import org.htmlunit.html.HtmlTableRow;
import org.htmlunit.html.HtmlTextArea;
import org.htmlunit.html.HtmlTitle;
import org.htmlunit.html.HtmlUnorderedList;
import org.htmlunit.html.TableRowGroup;
import org.htmlunit.html.serializer.HtmlSerializerVisibleText.HtmlSerializerTextBuilder.Mode;
import org.htmlunit.svg.SvgDesc;
import org.htmlunit.svg.SvgTitle;
import org.htmlunit.util.StringUtils;

/**
 * Special serializer to generate the output we need
 * at least for selenium WebElement#getText().
 * <p>This is also used for estimations by ComputedCssStyleDeclaration.</p>
 *
 * @author Ronald Brill
 * @author cd alexndr
 */
public class HtmlSerializerVisibleText {

    /** Rough estimation of the width of a char used to detect offscreen elements. */
    private static final int ESTIMATED_CHAR_WIDTH = 16;

    /** Rough estimation of the height of a line used to detect offscreen elements. */
    private static final int ESTIMATED_LINE_HEIGHT = 20;

    /**
     * The displays handled as block by WebDriver (everything that is not one of the
     * inline display boxes 'inline', 'inline-block', 'inline-table', 'none', 'table-cell', ...).
     */
    private static final Set<String> BLOCK_DISPLAYS = Set.of(
            "block", "list-item", "flex", "grid", "flow-root", "contents",
            "table", "table-row", "table-caption",
            "table-row-group", "table-header-group", "table-footer-group",
            "ruby", "ruby-base", "ruby-text", "ruby-text-container");

    /*
     * Caches for the current run; the dom and the styles are not changed while serializing
     * and many checks have to walk up the ancestors for every node.
     */

    /** The result of {@link #isDisplayed(DomNode)}. */
    private final Map<DomNode, Boolean> displayedCache_ = new IdentityHashMap<>();
    /** The result of {@link #isTransparent(DomNode)}. */
    private final Map<DomNode, Boolean> transparentCache_ = new IdentityHashMap<>();
    /** The white-space mode defined by the node or the ancestors ({@code null} if not defined). */
    private final Map<DomNode, Mode> whiteSpaceCache_ = new IdentityHashMap<>();

    /**
     * Converts an HTML node to text.
     * @param node a node
     * @return the text representation according to the setting of this serializer
     */
    public String asText(final DomNode node) {
        if (node instanceof HtmlBreak) {
            return "";
        }

        displayedCache_.clear();
        transparentCache_.clear();
        whiteSpaceCache_.clear();

        final HtmlSerializerTextBuilder builder = new HtmlSerializerTextBuilder();
        appendNode(builder, node, whiteSpaceStyle(node, Mode.WHITE_SPACE_NORMAL));
        return builder.getText();
    }

    /**
     * Iterates over all children and calls appendNode() for every child.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendChildren(final HtmlSerializerTextBuilder builder, final DomNode node, final Mode mode) {
        final Mode childMode = updateWhiteSpaceStyle(node, mode);
        for (final DomNode child : node.getChildren()) {
            appendNode(builder, child, childMode);
        }
    }

    /**
     * The core distribution method; calls the different appendXXX
     * methods depending on the type of the given node.
     *
     * @param builder the StringBuilder to add to
     * @param node the node to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendNode(final HtmlSerializerTextBuilder builder, final DomNode node, final Mode mode) {
        if (node instanceof DomCDataSection) {
            // ignore
        }
        else if (node instanceof DomText text1) {
            appendText(builder, text1, mode);
        }
        else if (node instanceof DomComment comment) {
            appendComment(builder, comment, mode);
        }
        else if (node instanceof HtmlBreak break1) {
            appendBreak(builder, break1, mode);
        }
        else if (node instanceof HtmlHiddenInput input4) {
            appendHiddenInput(builder, input4, mode);
        }
        else if (node instanceof HtmlScript script1) {
            appendScript(builder, script1, mode);
        }
        else if (node instanceof HtmlStyle style) {
            appendStyle(builder, style, mode);
        }
        else if (node instanceof HtmlNoFrames frames) {
            appendNoFrames(builder, frames, mode);
        }
        else if (node instanceof HtmlTextArea area) {
            appendTextArea(builder, area, mode);
        }
        else if (node instanceof HtmlTitle title) {
            appendTitle(builder, title, mode);
        }
        else if (node instanceof HtmlTableRow row) {
            appendTableRow(builder, row, mode);
        }
        else if (node instanceof HtmlSelect select) {
            appendSelect(builder, select, mode);
        }
        else if (node instanceof HtmlOption option) {
            appendOption(builder, option, mode);
        }
        else if (node instanceof HtmlSubmitInput input3) {
            appendSubmitInput(builder, input3, mode);
        }
        else if (node instanceof HtmlResetInput input2) {
            appendResetInput(builder, input2, mode);
        }
        else if (node instanceof HtmlCheckBoxInput input1) {
            appendCheckBoxInput(builder, input1, mode);
        }
        else if (node instanceof HtmlRadioButtonInput input) {
            appendRadioButtonInput(builder, input, mode);
        }
        else if (node instanceof HtmlInput) {
            // nothing
        }
        else if (node instanceof HtmlTable table) {
            appendTable(builder, table, mode);
        }
        else if (node instanceof HtmlOrderedList list1) {
            appendOrderedList(builder, list1, mode);
        }
        else if (node instanceof HtmlUnorderedList list) {
            appendUnorderedList(builder, list, mode);
        }
        else if (node instanceof HtmlPreformattedText
                || node instanceof HtmlListing
                || node instanceof HtmlExample
                || node instanceof HtmlPlainText) {
            appendPreformattedText(builder, (HtmlElement) node, mode);
        }
        else if (node instanceof HtmlInlineFrame frame) {
            appendInlineFrame(builder, frame, mode);
        }
        else if (node instanceof HtmlMenu menu) {
            appendMenu(builder, menu, mode);
        }
        else if (node instanceof HtmlDetails details) {
            appendDetails(builder, details, mode);
        }
        else if (node instanceof HtmlSvg svg) {
            appendSvg(builder, svg, mode);
        }
        else if (node instanceof HtmlNoScript script && node.getPage().getWebClient().isJavaScriptEnabled()) {
            appendNoScript(builder, script, mode);
        }
        else {
            appendDomNode(builder, node, mode);
        }
    }

    /**
     * Process {@link DomNode}.
     *
     * @param builder the StringBuilder to add to
     * @param domNode the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendDomNode(final HtmlSerializerTextBuilder builder,
            final DomNode domNode, final Mode mode) {
        final boolean block;
        if (domNode instanceof HtmlBody) {
            block = false;
        }
        else if (domNode instanceof DomElement element) {
            block = isBlockLevel(element);
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
        else if (isCssTableCell(domNode) && isCssTableCell(domNode.getNextElementSibling())) {
            builder.appendCellSeparator();
        }
    }

    /**
     * Checks if the given node is an element with display 'table-cell'.
     *
     * @param node the node to check
     * @return true if the node is an element with display 'table-cell'
     */
    private static boolean isCssTableCell(final DomNode node) {
        if (node instanceof DomElement element && !(node instanceof HtmlTableCell)) {
            final WebWindow window = node.getPage().getEnclosingWindow();
            return "table-cell".equals(window.getComputedStyle(element, null).getDisplay());
        }
        return false;
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
     * Checks if the given node is block level; this includes elements that are
     * blockified (children of flex and grid containers).
     *
     * @param node the node to check
     * @return true if the node is a block level element
     */
    private static boolean isBlockLevel(final DomNode node) {
        if (node instanceof DomElement element) {
            final WebWindow window = node.getPage().getEnclosingWindow();
            final ComputedCssStyleDeclaration style = window.getComputedStyle(element, null);
            final String display = style.getDisplay();
            if (BLOCK_DISPLAYS.contains(display)) {
                return true;
            }

            // absolute positioned and floating boxes are blockified
            final String position = style.getPosition();
            if ("absolute".equals(position) || "fixed".equals(position)) {
                return true;
            }
            final String floatValue = style.getStyleAttribute(Definition.FLOAT, false);
            if ("left".equals(floatValue) || "right".equals(floatValue)) {
                return true;
            }

            // children of flex and grid containers are blockified
            if (element.getParentNode() instanceof DomElement parent) {
                final String parentDisplay = window.getComputedStyle(parent, null).getDisplay();
                return "flex".equals(parentDisplay) || "inline-flex".equals(parentDisplay)
                        || "grid".equals(parentDisplay) || "inline-grid".equals(parentDisplay);
            }
        }
        return false;
    }

    /**
     * Process {@link HtmlMenu}; every child is placed on a line of its own.
     *
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
     * Process {@link HtmlDetails}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlDetails the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendDetails(final HtmlSerializerTextBuilder builder,
                    final HtmlDetails htmlDetails, final Mode mode) {
        if (htmlDetails.isOpen()) {
            appendChildren(builder, htmlDetails, mode);
            return;
        }

        // like WebDriver: element children of a closed details are hidden (except the summary),
        // text children are still visible
        for (final DomNode child : htmlDetails.getChildren()) {
            if (child instanceof HtmlSummary || child instanceof DomText) {
                appendNode(builder, child, mode);
            }
        }
    }

    /**
     * Process {@link HtmlTitle}.
     *
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
            if (first) {
                first = false;
            }
            else {
                builder.appendCellSeparator();
            }
            appendChildren(builder, cell, mode);
        }
    }

    /**
     * Checks if the given node is visible for WebDriver; this includes the
     * checks done by {@link DomNode#isDisplayed()} and the WebDriver specific checks
     * (opacity, content-visibility, zero size, offscreen).
     * The result is cached for the current run.
     *
     * @param domNode the node to check
     * @return true if the node is visible
     */
    protected boolean isDisplayed(final DomNode domNode) {
        return displayedCache_.computeIfAbsent(domNode, node -> node.isDisplayed() && !isTransparent(node));
    }

    /**
     * Checks if the element has a height or width of zero and hides the overflow.
     *
     * @param style the computed style of the element
     * @return true if the content is not visible
     */
    private static boolean isZeroSizeWithOverflowHidden(final ComputedCssStyleDeclaration style) {
        if (!"hidden".equals(style.getStyleAttribute(Definition.OVERFLOW, false))) {
            return false;
        }
        return isZeroLength(style.getStyleAttribute(Definition.HEIGHT, false))
                || isZeroLength(style.getStyleAttribute(Definition.WIDTH, false));
    }

    /**
     * Checks if the given css length is zero ('0', '0px', '0em', ...).
     *
     * @param length the length
     * @return true if the length is zero
     */
    private static boolean isZeroLength(final String length) {
        if (StringUtils.isEmptyOrNull(length)) {
            return false;
        }
        int i = 0;
        while (i < length.length() && (length.charAt(i) == '0' || length.charAt(i) == '.')) {
            i++;
        }
        return i > 0 && (i == length.length() || Character.isLetter(length.charAt(i)));
    }

    /**
     * Checks if the element is positioned outside of the page (e.g. 'left: -9999px').
     * <p>The calculated width/height can't be used here because the calculation
     * itself depends on the visible text. Therefore we use the explicit width/height
     * or a rough estimation based on the text length.</p>
     *
     * @param element the element
     * @param style the computed style of the element
     * @return true if the element is not visible
     */
    private static boolean isOffscreen(final DomElement element, final ComputedCssStyleDeclaration style) {
        final String position = style.getPosition();
        if (!"absolute".equals(position) && !"fixed".equals(position)) {
            return false;
        }

        final int left = pixelValue(style.getStyleAttribute(Definition.LEFT, false));
        if (left < 0) {
            int width = pixelValue(style.getStyleAttribute(Definition.WIDTH, false));
            if (width <= 0) {
                width = element.getTextContent().length() * ESTIMATED_CHAR_WIDTH;
            }
            if (left + width <= 0) {
                return true;
            }
        }

        final int top = pixelValue(style.getStyleAttribute(Definition.TOP, false));
        if (top < 0) {
            int height = pixelValue(style.getStyleAttribute(Definition.HEIGHT, false));
            if (height <= 0) {
                height = ESTIMATED_LINE_HEIGHT;
            }
            return top + height <= 0;
        }
        return false;
    }

    /**
     * Returns the integer value of a pixel length; 0 for all other values.
     *
     * @param length the length
     * @return the pixel value
     */
    private static int pixelValue(final String length) {
        if (StringUtils.isEmptyOrNull(length) || !length.endsWith("px")) {
            return 0;
        }
        try {
            return (int) Double.parseDouble(length.substring(0, length.length() - 2).trim());
        }
        catch (final NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Checks if the given node or one of its ancestors is not visible for WebDriver
     * (opacity of zero, content-visibility hidden, zero size with hidden overflow or offscreen).
     * The results are cached for the current run; the ancestors share the cached result.
     *
     * @param domNode the node to check
     * @return true if the node is not visible
     */
    private boolean isTransparent(final DomNode domNode) {
        final Page page = domNode.getPage();
        if (page == null) {
            return false;
        }
        final WebWindow window = page.getEnclosingWindow();
        if (!window.getWebClient().getOptions().isCssEnabled()) {
            return false;
        }

        // walk up until the first transparent element or the first known result;
        // all the nodes on the way share this result
        final List<DomNode> path = new ArrayList<>();
        boolean transparent = false;
        DomNode node = domNode;
        while (node != null) {
            final Boolean cached = transparentCache_.get(node);
            if (cached != null) {
                transparent = cached;
                break;
            }
            path.add(node);
            if (node instanceof DomElement element
                    && isTransparentElement(element, window.getComputedStyle(element, null))) {
                transparent = true;
                break;
            }
            node = node.getParentNode();
        }

        for (final DomNode pathNode : path) {
            transparentCache_.put(pathNode, transparent);
        }
        return transparent;
    }

    /**
     * Checks if the given element itself (the ancestors are not checked) is not visible
     * for WebDriver (opacity of zero, content-visibility hidden, zero size with hidden overflow
     * or offscreen).
     *
     * @param element the element to check
     * @param style the computed style of the element
     * @return true if the element is not visible
     */
    private static boolean isTransparentElement(final DomElement element, final ComputedCssStyleDeclaration style) {
        if ("hidden".equals(style.getStyleAttribute("content-visibility"))
                || isZeroSizeWithOverflowHidden(style)
                || isOffscreen(element, style)) {
            return true;
        }

        final String opacity = style.getStyleAttribute(Definition.OPACITY, false);
        if (!StringUtils.isEmptyOrNull(opacity)) {
            try {
                return Double.parseDouble(opacity.trim()) <= 0;
            }
            catch (final NumberFormatException e) {
                // ignore
            }
        }
        return false;
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
        if (isDisplayed(htmlTextArea)) {
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

        // like WebDriver we use the document order if the footer is before the body
        final boolean footerFirst = tableFooter != null && isFooterBeforeBody(htmlTable);
        if (footerFirst) {
            first = appendTableRows(builder, mode, tableFooter.getRows(), first, null, null);
        }

        final List<HtmlTableRow> tableRows = htmlTable.getRows();
        first = appendTableRows(builder, mode, tableRows, first, tableHeader, tableFooter);

        if (tableFooter != null && !footerFirst) {
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
     * Process the given {@link HtmlTableRow}s; rows are separated by a block separator.
     *
     * @param builder the StringBuilder to add to
     * @param mode the {@link Mode} to use for processing
     * @param rows the rows
     * @param first if true this is the first one
     * @param skipParent1 skip row if the parent is this
     * @param skipParent2 skip row if the parent is this
     * @return true if no row was written so far (the new value of first)
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
     * Checks if the first footer of the table is placed before the first body (or row)
     * in the document.
     *
     * @param htmlTable the table
     * @return true if the footer is before the body
     */
    private static boolean isFooterBeforeBody(final HtmlTable htmlTable) {
        for (final DomNode child : htmlTable.getChildren()) {
            if (child instanceof HtmlTableFooter) {
                return true;
            }
            if (child instanceof HtmlTableBody || child instanceof HtmlTableRow) {
                return false;
            }
        }
        return false;
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
        // an empty select has no text and does not separate
        final List<HtmlOption> options = htmlSelect.getOptions();
        if (options.isEmpty()) {
            return;
        }

        builder.appendBlockSeparator();
        boolean leadingNlPending = false;
        // all options (also the ones inside of an optgroup) are rendered on a separate line;
        // the white-space style is ignored for the options (see appendOption())
        for (final HtmlOption item : options) {
            if (leadingNlPending) {
                builder.appendBlockSeparator();
            }

            builder.resetContentAdded();
            appendNode(builder, item, mode);
            leadingNlPending = builder.wasContentAdded();
        }
        builder.appendBlockSeparator();
    }

    /**
     * Process {@link HtmlOption}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlOption the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendOption(final HtmlSerializerTextBuilder builder,
            final HtmlOption htmlOption, final Mode mode) {
        // like WebDriver: an option is only shown inside of a select;
        // but it is still a block
        if (htmlOption.getEnclosingSelect() == null) {
            builder.appendBlockSeparator();
            builder.appendBlockSeparator();
            return;
        }

        // options are rendered by the select (form control); the white-space style
        // (also the one inherited from the select) is ignored
        for (final DomNode child : htmlOption.getChildren()) {
            appendNode(builder, child, Mode.WHITE_SPACE_NORMAL);
        }
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
        appendListItems(builder, htmlOrderedList, mode);
    }

    /**
     * Process {@link HtmlUnorderedList}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlUnorderedList the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendUnorderedList(final HtmlSerializerTextBuilder builder,
                    final HtmlUnorderedList htmlUnorderedList, final Mode mode) {
        appendListItems(builder, htmlUnorderedList, mode);
    }

    /**
     * Process the children of a list (ol, ul); a block level item
     * with some content is placed on a line of its own.
     *
     * @param builder the StringBuilder to add to
     * @param list the list element
     * @param mode the {@link Mode} to use for processing
     */
    private void appendListItems(final HtmlSerializerTextBuilder builder,
                    final HtmlElement list, final Mode mode) {
        builder.appendBlockSeparator();
        boolean leadingNlPending = false;
        final Mode listMode = whiteSpaceStyle(list, mode);
        for (final DomNode item : list.getChildren()) {
            if (leadingNlPending) {
                builder.appendBlockSeparator();
            }

            builder.resetContentAdded();
            appendNode(builder, item, whiteSpaceStyle(item, listMode));
            leadingNlPending = builder.wasContentAdded() && isBlockLevel(item);
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
            final HtmlElement htmlPreformattedText, final Mode mode) {
        if (isDisplayed(htmlPreformattedText)) {
            builder.appendBlockSeparator();
            appendChildren(builder, htmlPreformattedText, whiteSpaceStyle(htmlPreformattedText, Mode.PRE));
            builder.appendBlockSeparator();
        }
    }

    /**
     * Process {@link HtmlSvg}.
     * The svg itself is inline; Chrome/Edge render the svg text elements as blocks.
     *
     * @param builder the StringBuilder to add to
     * @param htmlSvg the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendSvg(final HtmlSerializerTextBuilder builder,
            final HtmlSvg htmlSvg, final Mode mode) {
        final boolean block = htmlSvg.getPage().getWebClient().getBrowserVersion()
                    .hasFeature(JS_INNER_TEXT_SVG_NL)
                && hasSvgText(htmlSvg);
        if (block) {
            builder.appendBlockSeparator();
        }
        appendChildren(builder, htmlSvg, mode);
        if (block) {
            builder.appendBlockSeparator();
        }
    }

    /**
     * Checks if the given svg node has some text that is not part of a title or desc element.
     *
     * @param node the node to check
     * @return true if there is some text
     */
    private static boolean hasSvgText(final DomNode node) {
        for (final DomNode child : node.getChildren()) {
            if (child instanceof SvgTitle || child instanceof SvgDesc) {
                continue;
            }
            if (child instanceof DomText text) {
                if (!StringUtils.isBlank(text.getData())) {
                    return true;
                }
            }
            else if (hasSvgText(child)) {
                return true;
            }
        }
        return false;
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
        // like WebDriver: the content of the frame is not part of the text,
        // the iframe is an inline element with the (fallback) text of its children
        if (isDisplayed(htmlInlineFrame)) {
            appendChildren(builder, htmlInlineFrame, mode);
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
                || parent instanceof HtmlScript) {
            builder.append(domText.getData(), Mode.WHITE_SPACE_PRE_LINE);
            return;
        }

        // options are not rendered by css; the display style is ignored
        if (parent == null
                || parent instanceof HtmlOption
                || isDisplayed(parent)) {
            String data = domText.getData();
            // zero width space is not part of the visible text
            if (data.indexOf('\u200B') > -1) {
                data = data.replace("\u200B", "");
            }
            builder.append(data, mode);
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
     * Process {@link HtmlBreak}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlBreak the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendBreak(final HtmlSerializerTextBuilder builder,
            final HtmlBreak htmlBreak, final Mode mode) {
        // a leading line break is not part of the visible text
        if (builder.isEmpty()) {
            return;
        }
        builder.appendBreak(mode);
    }

    /**
     * Process {@link HtmlCheckBoxInput}.
     *
     * @param builder the StringBuilder to add to
     * @param htmlCheckBoxInput the target to process
     * @param mode the {@link Mode} to use for processing
     */
    protected void appendCheckBoxInput(final HtmlSerializerTextBuilder builder,
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
    protected void appendRadioButtonInput(final HtmlSerializerTextBuilder builder,
            final HtmlRadioButtonInput htmlRadioButtonInput, final Mode mode) {
        // nothing to do
    }

    /**
     * Determines the white-space mode for the given node; the white-space style of the node
     * and the ancestors is checked. The result is cached for the current run.
     *
     * @param domNode the node
     * @param defaultMode the mode used if no white-space style is defined
     * @return the {@link Mode}
     */
    protected Mode whiteSpaceStyle(final DomNode domNode, final Mode defaultMode) {
        final Page page = domNode.getPage();
        if (page == null) {
            return defaultMode;
        }
        final WebWindow window = page.getEnclosingWindow();
        if (!window.getWebClient().getOptions().isCssEnabled()) {
            return defaultMode;
        }

        // walk up until the first node defining a white-space style or the first known result;
        // all the nodes on the way share this result
        final List<DomNode> path = new ArrayList<>();
        Mode mode = null;
        DomNode node = domNode;
        while (node != null) {
            if (whiteSpaceCache_.containsKey(node)) {
                mode = whiteSpaceCache_.get(node);
                break;
            }
            path.add(node);
            if (node instanceof DomElement element) {
                mode = whiteSpaceMode(window.getComputedStyle(element, null));
                if (mode != null) {
                    break;
                }
            }
            node = node.getParentNode();
        }

        for (final DomNode pathNode : path) {
            whiteSpaceCache_.put(pathNode, mode);
        }
        return mode == null ? defaultMode : mode;
    }

    /**
     * Determines the white-space mode for the given node; only the white-space style
     * of the node itself is checked.
     *
     * @param domNode the node
     * @param defaultMode the mode used if the node does not define a white-space style
     * @return the {@link Mode}
     */
    protected Mode updateWhiteSpaceStyle(final DomNode domNode, final Mode defaultMode) {
        if (domNode instanceof DomElement element) {
            final Page page = element.getPage();
            if (page != null) {
                final WebWindow window = page.getEnclosingWindow();
                if (window.getWebClient().getOptions().isCssEnabled()) {
                    final Mode mode = whiteSpaceMode(window.getComputedStyle(element, null));
                    if (mode != null) {
                        return mode;
                    }
                }
            }
        }
        return defaultMode;
    }

    /**
     * Maps the white-space (and white-space-collapse) style to the {@link Mode}.
     *
     * @param style the computed style
     * @return the {@link Mode} or {@code null} if the style does not define the white-space handling
     */
    private static Mode whiteSpaceMode(final ComputedCssStyleDeclaration style) {
        final String value = style.getStyleAttribute(Definition.WHITE_SPACE, false);
        if (!StringUtils.isEmptyOrNull(value)) {
            if ("normal".equalsIgnoreCase(value) || "nowrap".equalsIgnoreCase(value)) {
                return Mode.WHITE_SPACE_NORMAL;
            }
            if ("pre".equalsIgnoreCase(value) || "pre-wrap".equalsIgnoreCase(value)) {
                return Mode.WHITE_SPACE_PRE;
            }
            // WebDriver collapses the spaces of break-spaces but keeps the line breaks
            if ("pre-line".equalsIgnoreCase(value) || "break-spaces".equalsIgnoreCase(value)) {
                return Mode.WHITE_SPACE_PRE_LINE;
            }
        }

        final String collapse = style.getStyleAttribute(Definition.WHITE_SPACE_COLLAPSE, false);
        if ("preserve".equalsIgnoreCase(collapse)) {
            return Mode.WHITE_SPACE_PRE;
        }
        if ("preserve-breaks".equalsIgnoreCase(collapse)
                || "break-spaces".equalsIgnoreCase(collapse)) {
            return Mode.WHITE_SPACE_PRE_LINE;
        }
        return null;
    }

    /**
     * Helper to compose the text for the serializer based on several modes.
     */
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
             * at newline characters in the source and at {@code <br>} elements.
             */
            WHITE_SPACE_PRE,

            /**
             * Sequences of white space are collapsed. Lines are broken
             * at newline characters, at {@code <br>} and as necessary
             * to fill line boxes.
             */
            WHITE_SPACE_PRE_LINE
        }

        private enum State {
            DEFAULT,
            EMPTY,
            BLANK_AT_END,
            NEWLINE_AT_END,
            BREAK_AT_END,
            BLOCK_SEPARATOR_AT_END
        }

        private State state_;
        private final StringBuilder builder_;
        private int trimRightPos_;
        private boolean contentAdded_;
        private int lineStart_;

        /**
         * Ctor.
         */
        public HtmlSerializerTextBuilder() {
            builder_ = new StringBuilder();
            state_ = State.EMPTY;
            trimRightPos_ = 0;
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
            final int length = content.length();
            if (length == 0) {
                return;
            }

            final int lastIndex = length - 1;
            for (int i = 0; i < length; i++) {
                char c = content.charAt(i);

                // a \r is ignored; only at the end it is handled like a \n
                if (c == '\r') {
                    if (i != lastIndex) {
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
                        // a leading line break is not part of the visible text
                        if (state_ == State.EMPTY) {
                            continue;
                        }
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

        /**
         * Append a block separator.
         */
        public void appendBlockSeparator() {
            // like WebDriver: no line break if the current line contains only (preserved) white space
            if (state_ == State.BLANK_AT_END
                    && trimRightPos_ == builder_.length()
                    && isWhitespaceOnly(lineStart_)) {
                return;
            }

            final int length = builder_.length();
            appendBlockSeparatorWorker();
            if (builder_.length() != length) {
                lineStart_ = builder_.length();
            }
        }

        /**
         * Checks if the text starting at the given position contains only white space.
         *
         * @param start the start position
         * @return true if there is only white space
         */
        private boolean isWhitespaceOnly(final int start) {
            for (int i = start; i < builder_.length(); i++) {
                final char c = builder_.charAt(i);
                if (c != ' ' && c != '\t' && c != '\n' && c != (char) 160) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Append a block separator.
         */
        private void appendBlockSeparatorWorker() {
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

        /**
         * Append a break.
         *
         * @param mode the {@link Mode}
         */
        public void appendBreak(final Mode mode) {
            // a break after a block adds an empty line
            if (state_ != State.BLOCK_SEPARATOR_AT_END) {
                builder_.setLength(trimRightPos_);
            }

            builder_.append('\n');
            state_ = State.BREAK_AT_END;
            trimRightPos_ = builder_.length();
            lineStart_ = builder_.length();
        }

        /**
         * Append a blank.
         */
        public void appendBlank() {
            builder_.append(' ');
            state_ = State.BLANK_AT_END;
            trimRightPos_ = builder_.length();
        }

        /**
         * Append the separator between two table cells; the separator is
         * collapsible (not added twice, removed at the end of a line).
         */
        public void appendCellSeparator() {
            switch (state_) {
                case EMPTY:
                case BLANK_AT_END:
                case BLOCK_SEPARATOR_AT_END:
                case NEWLINE_AT_END:
                case BREAK_AT_END:
                    return;
                default:
                    builder_.append(' ');
                    state_ = State.BLANK_AT_END;
            }
        }

        /**
         * Remove all trailing whitespace from the end.
         *
         * @param mode the {@link Mode}
         */
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

        /**
         * Returns {@code true} if nothing was added so far.
         *
         * @return {@code true} if nothing was added so far
         */
        public boolean isEmpty() {
            return state_ == State.EMPTY;
        }

        /**
         * Returns true if some content was already added.
         *
         * @return true if some content was already added
         */
        public boolean wasContentAdded() {
            return contentAdded_;
        }

        /**
         * Resets the contentAdded state to false.
         */
        public void resetContentAdded() {
            contentAdded_ = false;
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
