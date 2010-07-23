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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for various clickable elements.
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ClickableElementTest extends WebDriverTestCase {

    /**
     * Full page driver for onClick tests.
     *
     * @param htmlContent HTML fragment for body of page with clickable element
     * identified by clickId ID attribute. Must have onClick that raises
     * an alert of "foo".
     * @throws Exception if the test fails
     */
    private void onClickPageTest(final String htmlContent) throws Exception {
        setExpectedAlerts("foo");
        onClickPageTest(htmlContent, 1);
    }

    /**
     * Full page driver for onClick tests.
     *
     * @param htmlContent HTML fragment for body of page with clickable element
     * identified by clickId ID attribute.
     * @param numClicks number of times to click element
     * @param expectedAlerts array of expected popup values
     * @throws Exception if the test fails
     */
    private void onClickPageTest(final String htmlContent, final int numClicks) throws Exception {
        onClickPageTest(htmlContent, numClicks, false);
    }

    /**
     * Full page driver for onClick tests.
     *
     * @param htmlContent HTML fragment for body of page with clickable element identified by clickId ID attribute
     * @param numClicks number of times to click element
     * @param expectedAlerts array of expected popup values
     * @param exceptionOnError
     * @throws Exception if the test fails
     */
    private void onClickPageTest(final String htmlContent, final int numClicks,
            final boolean exceptionOnError) throws Exception {

        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setThrowExceptionOnScriptError(exceptionOnError);

        final List<String> collectedAlerts = new ArrayList<String>();
        final CollectingAlertHandler alertHandler = new CollectingAlertHandler(collectedAlerts);
        client.setAlertHandler(alertHandler);

        final HtmlPage page = client.getPage(getDefaultUrl());
        final HtmlElement clickable = page.getHtmlElementById("clickId");

        for (int i = 0; i < numClicks; i++) {
            clickable.click();
        }

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Body driver for onClick tests.
     *
     * @param htmlBody HTML text body
     * @throws Exception if the test fails
     */
    private void onClickBodyTest(final String htmlBody) throws Exception {
        onClickPageTest("<html><head><title>foo</title></head>\n" + htmlBody
                 + "</html>");
    }

    /**
     * Simple tag name driver for onClick tests.
     *
     * @param tagName HTML tag name for simple tag with text body
     * @throws Exception if the test fails
     */
    private void onClickSimpleTest(final String tagName) throws Exception {
        onClickBodyTest("<body><" + tagName + " id='clickId' onClick='alert(\"foo\")'>Text</" + tagName + "></body>\n");
    }

    /**
     * Test onClick handler and click method of anchor element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void anchor_onClick() throws Exception {
        onClickSimpleTest("a");
    }

    /**
     * Test onClick handler and click method of abbreviation element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void abbreviation_onClick() throws Exception {
        onClickSimpleTest("abbr");
    }

    /**
     * Test onClick handler and click method of acronym element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void acronym_onClick() throws Exception {
        onClickSimpleTest("acronym");
    }

    /**
     * Test onClick handler and click method of address element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void address_onClick() throws Exception {
        onClickSimpleTest("address");
    }

    /**
     * Test onClick handler and click method of area element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void area_onClick() throws Exception {
        onClickBodyTest("<body><map><area id='clickId' onClick='alert(\"foo\")'/></map></body>\n");
    }

    /**
     * Test onClick handler and click method of bold element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testBold_onClick() throws Exception {
        onClickSimpleTest("b");
    }

    /**
     * Test onClick handler and click method of big element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testBig_onClick() throws Exception {
        onClickSimpleTest("big");
    }

    /**
     * Test onClick handler and click method of blockquote element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testBlockquote_onClick() throws Exception {
        onClickSimpleTest("blockquote");
    }

    /**
     * Test onClick handler and click method of body element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testBody_onClick() throws Exception {
        onClickBodyTest("<body id='clickId' onClick='alert(\"foo\")'>Text</body>\n");
    }

    /**
     * Test onClick handler and click method of button element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testButton_onClick() throws Exception {
        onClickBodyTest("<body><form><button id='clickId' onClick='alert(\"foo\")'>Item</button></form></body>\n");
    }

    /**
     * Test onClick handler can be called multiple times.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "foo0", "foo1" })
    public void testButton_onClickTwice() throws Exception {
        onClickPageTest("<body><form>\n"
                + "<button id='clickId' onClick='alert(\"foo\" + count++); return false;'>Item</button>\n"
                + "<script> var count = 0 </script>\n"
                + "</form></body>\n", 2);
    }

    /**
     * Test onClick handler and click method of cite element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testCite_onClick() throws Exception {
        onClickSimpleTest("cite");
    }

    /**
     * Test onClick handler and click method of code element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testCode_onClick() throws Exception {
        onClickSimpleTest("code");
    }

    /**
     * Test onClick handler and click method of table column element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableColumn_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup>\n"
            + "<col id='clickId' onClick='alert(\"foo\")'/></colgroup><thead><tr><th>\n"
            + "Header</th></tr></thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr>\n"
            + "th>Header</th></tr></tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of table column group element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableColumnGroup_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption>\n"
            + "<colgroup id='clickId' onClick='alert(\"foo\")'><col/></colgroup><thead>\n"
            + "<tr><th>Header</th></tr></thead><tbody><tr><td>Data</td></tr></tbody><tfoot>\n"
            + "<tr><th>Header</th></tr></tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of center element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testCenter_onClick() throws Exception {
        onClickSimpleTest("center");
    }

    /**
     * Test onClick handler and click method of table caption element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableCaption_onClick() throws Exception {
        onClickBodyTest("<body><table><caption id='clickId' onClick='alert(\"foo\")'>\n"
            + "Caption</caption><colgroup><col/></colgroup><thead><tr><th>Header</th></tr>\n"
            + "</thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr><th>Header</th></tr>\n"
            + "</tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of definition description dd element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testDefinitionDescription_onClick() throws Exception {
        onClickBodyTest("<body><dl><dt>Term</dt><dd id='clickId' onClick='alert(\"foo\")'>Definition</dd></dl></body>");
    }

    /**
     * Test that no NPE is thrown when JS fails on link click
     * and WebClient.setThrowExceptionOnScriptError(false) is used.
     * Test for bug 1385864.
     * @throws Exception if the test fails
     */
    @Test
    public void testJavaScriptError_onClick() throws Exception {
        onClickPageTest("<html><head></head><body>\n"
                + "<form method='POST'><input type='button' id='clickId' onclick='y()'></form>\n"
                + "</body></html>",
                1, false);
    }

    /**
     * Test onClick handler and click method of definition element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testDefinition_onClick() throws Exception {
        onClickSimpleTest("dfn");
    }

    /**
     * Test onClick handler and click method of directory element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testDirectory_onClick() throws Exception {
        onClickSimpleTest("dir");
    }

    /**
     * Test onClick handler and click method of definition list dl element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testDefinitionList_onClick() throws Exception {
        onClickBodyTest("<body><dl id='clickId' onClick='alert(\"foo\")'><dt>Term</dt><dd>Definition</dd></dl></body>");
    }

    /**
     * Test onClick handler and click method of definition term dt element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testDefinitionTerm_onClick() throws Exception {
        onClickBodyTest("<body><dl><dt id='clickId' onClick='alert(\"foo\")'>Term</dt><dd>Definition</dd></dl></body>");
    }

    /**
     * Test onClick handler and click method of deleted text element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testDeletedText_onClick() throws Exception {
        onClickSimpleTest("del");
    }

    /**
     * Test onClick handler and click method of division element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testDivision_onClick() throws Exception {
        onClickSimpleTest("div");
    }

    /**
     * Test onClick handler and click method of emphasis element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testEmphasis_onClick() throws Exception {
        onClickSimpleTest("em");
    }

    /**
     * Test onClick handler and click method of field set element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testFieldSet_onClick() throws Exception {
        onClickBodyTest("<body><form><fieldset id='clickId' onClick='alert(\"foo\")'>\n"
            + "<legend>Legend</legend></fieldset></form></body>\n");
    }

    /**
     * Test onClick handler and click method of form element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testForm_onClick() throws Exception {
        onClickSimpleTest("form");
    }

    /**
     * Test onClick handler and click method of italics element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testItalics_onClick() throws Exception {
        onClickSimpleTest("i");
    }

    /**
     * Test onClick handler and click method of image element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testImage_onClick() throws Exception {
        onClickSimpleTest("img");
    }

    /**
     * Test onClick handler and click method of header1 element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testHeader1_onClick() throws Exception {
        onClickSimpleTest("h1");
    }

    /**
     * Test onClick handler and click method of header2 element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testHeader2_onClick() throws Exception {
        onClickSimpleTest("h2");
    }

    /**
     * Test onClick handler and click method of header3 element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testHeader3_onClick() throws Exception {
        onClickSimpleTest("h3");
    }

    /**
     * Test onClick handler and click method of header4 element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testHeader4_onClick() throws Exception {
        onClickSimpleTest("h4");
    }

    /**
     * Test onClick handler and click method of header5 element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testHeader5_onClick() throws Exception {
        onClickSimpleTest("h5");
    }

    /**
     * Test onClick handler and click method of header6 element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testHeader6_onClick() throws Exception {
        onClickSimpleTest("h6");
    }

    /**
     * Test onClick handler and click method of horizontal rule element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testHorizontalRule_onClick() throws Exception {
        onClickSimpleTest("hr");
    }

    /**
     * Test onClick handler and click method of input element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testInput_onClick() throws Exception {
        onClickBodyTest("<body><form><input id='clickId' onClick='alert(\"foo\")'>Item</input></form></body>\n");
    }

    /**
     * Test onClick handler and click method of inserted text element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testInsertedText_onClick() throws Exception {
        onClickSimpleTest("ins");
    }

    /**
     * Test onClick handler and click method of keyboard element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testKeyboard_onClick() throws Exception {
        onClickSimpleTest("kbd");
    }

    /**
     * Test onClick handler and click method of label element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testLabel_onClick() throws Exception {
        onClickBodyTest("<body><form><label id='clickId' onClick='alert(\"foo\")'>Item</label></form></body>\n");
    }

    /**
     * Test onClick handler and click method of legend element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testLegend_onClick() throws Exception {
        onClickBodyTest("<body><form><fieldset><legend id='clickId' onClick='alert(\"foo\")'>\n"
            + "Legend</legend></fieldset></form></body>\n");
    }

    /**
     * Test onClick handler and click method of link element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testLink_onClick() throws Exception {
        onClickPageTest("<html><head><title>foo</title><link id='clickId' onClick='alert(\"foo\")'/>\n"
            + "</head><body></body></html>");
    }

    /**
     * Test onClick handler and click method of List Item element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testListItem_onClick() throws Exception {
        onClickBodyTest("<body><ol><li id='clickId' onClick='alert(\"foo\")'>Item</li></ol></body>\n");
    }

    /**
     * Test onClick handler and click method of map element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testMap_onClick() throws Exception {
        onClickBodyTest("<body><map id='clickId' onClick='alert(\"foo\")'><area/></map></body>\n");
    }

    /**
     * Test onClick handler and click method of menu element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testMenu_onClick() throws Exception {
        onClickBodyTest("<body><menu id='clickId' onClick='alert(\"foo\")'><li>Item</li></menu></body>\n");
    }

    /**
     * Test onClick handler and click method of no frames element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testNoFrames_onClick() throws Exception {
        onClickPageTest("<html><head><title>foo</title></head><frameset><frame/>\n"
            + "<noframes id='clickId' onClick='alert(\"foo\")'/></frameset></html>");
    }

    /**
     * Test onClick handler and click method of no script element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testNoScript_onClick() throws Exception {
        onClickBodyTest("<body><script>var i=0;</script><noscript id='clickId' "
            + "onClick='alert(\"foo\")'>Item</noscript></body>\n");
    }

    /**
     * Test onClick handler and click method of object element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testObject_onClick() throws Exception {
        onClickSimpleTest("object");
    }

    /**
     * Test onClick handler and click method of option element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testOption_onClick() throws Exception {
        onClickBodyTest("<body><form><select><option id='clickId' onClick='alert(\"foo\")'>\n"
            + "Option</option></select></form></body>\n");
    }

    /**
     * Test onClick handler and click method of Option Group element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testOptionGroup_onClick() throws Exception {
        onClickBodyTest("<body><form><select><optgroup id='clickId' onClick='alert(\"foo\")'>\n"
            + "<option>Option</option></optgroup></select></form></body>\n");
    }

    /**
     * Test onClick handler and click method of Ordered List element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testOrderedList_onClick() throws Exception {
        onClickBodyTest("<body><ol id='clickId' onClick='alert(\"foo\")'><li>Item</li></ol></body>\n");
    }

    /**
     * Test onClick handler and click method of paragraph element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testParagraph_onClick() throws Exception {
        onClickSimpleTest("p");
    }

    /**
     * Test onClick handler and click method of pre element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testPre_onClick() throws Exception {
        onClickSimpleTest("pre");
    }

    /**
     * Test onClick handler and click method of inline Quotation element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testQuotation_onClick() throws Exception {
        onClickSimpleTest("q");
    }

    /**
     * Test onClick handler and click method of strikethrough element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testStrikethrough_onClick() throws Exception {
        onClickSimpleTest("s");
    }

    /**
     * Test onClick handler and click method of sample element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testSample_onClick() throws Exception {
        onClickSimpleTest("samp");
    }

    /**
     * Test onClick handler and click method of select element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testSelect_onClick() throws Exception {
        onClickBodyTest("<body><form><select id='clickId' onClick='alert(\"foo\")'>\n"
            + "<option>Option</option></select></form></body>\n");
    }

    /**
     * Test onClick handler and click method of small element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testSmall_onClick() throws Exception {
        onClickSimpleTest("small");
    }

    /**
     * Test onClick handler and click method of span element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testSpan_onClick() throws Exception {
        onClickSimpleTest("span");
    }

    /**
     * Test onClick handler and click method of strike element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testStrike_onClick() throws Exception {
        onClickSimpleTest("strike");
    }

    /**
     * Test onClick handler and click method of subscript element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testSubscript_onClick() throws Exception {
        onClickSimpleTest("sub");
    }

    /**
     * Test onClick handler and click method of superscript element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testSuperscript_onClick() throws Exception {
        onClickSimpleTest("sup");
    }

    /**
     * Test onClick handler and click method of table element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTable_onClick() throws Exception {
        onClickBodyTest("<body><table id='clickId' onClick='alert(\"foo\")'><caption>\n"
            + "Caption</caption><colgroup><col/></colgroup><thead><tr><th>Header</th></tr>\n"
            + "</thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr><th>Header</th></tr>\n"
            + "</tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of table body element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableBody_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>\n"
            + "</colgroup><thead><tr><th>Header</th></tr></thead>\n"
            + "<tbody id='clickId' onClick='alert(\"foo\")'><tr><td>Data</td></tr>\n"
            + "</tbody><tfoot><tr><th>Header</th></tr></tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of table data cell element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableDataCell_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>\n"
            + "</colgroup><thead><tr><th>Header</th></tr></thead><tbody><tr>\n"
            + "<td id='clickId' onClick='alert(\"foo\")'>Data</td></tr></tbody>\n"
            + "<tfoot><tr><th>Header</th></tr></tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of textarea element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTextarea_onClick() throws Exception {
        onClickBodyTest("<body><form><textarea id='clickId' onClick='alert(\"foo\")'>Item</textarea></form></body>\n");
    }

    /**
     * Test onClick handler and click method of table footer element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableFooter_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>\n"
            + "</colgroup><thead><tr><th>Header</th></tr></thead><tbody><tr><td>Data</td>\n"
            + "</tr></tbody><tfoot id='clickId' onClick='alert(\"foo\")'><tr><th>Header</th>\n"
            + "</tr></tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of table header cell element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableHeaderCell_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup>\n"
            + "<col/></colgroup><thead><tr><th id='clickId' onClick='alert(\"foo\")'>\n"
            + "Header</th></tr></thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr>\n"
            + "<th>Header</th></tr></tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of table header element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableHeader_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>\n"
            + "</colgroup><thead id='clickId' onClick='alert(\"foo\")'><tr><th>Header</th>\n"
            + "</tr></thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr><th>Header</th>\n"
            + "</tr></tfoot></table></body>\n");
    }

    /**
     * Test onClick handler and click method of table row element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableRow_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>\n"
            + "</colgroup><thead><tr><th>Header</th></tr></thead><tbody>\n"
            + "<tr id='clickId' onClick='alert(\"foo\")'><td>Data</td></tr></tbody>\n"
            + "<tfoot><tr><th>Header</th></tr></tfoot></table></body>\n");
    }

    /**
     * Test when HtmlTableRow.onclick is set by JavaScript.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableRow_onClickSetOnLoad() throws Exception {
        onClickPageTest("<html><head>\n"
                        + "<script language='JavaScript'>\n"
                        + "function doFoo() { alert('foo');        }\n"
                        + "function doOnload() { document.getElementById('clickId').onclick = doFoo;}\n"
                        + "</script>\n"
                        + "</head><body onload=\"doOnload();\">\n"
                        + "<table><tbody><tr id='clickId'><td>cell value</td></tr></tbody></table>\n"
                        + "</body></html>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testCheckbox_onClickUpdatesStateFirst() throws Exception {
        onClickPageTest("<html><head>\n"
                        + "<script language='JavaScript'>\n"
                        + "function doFoo(event) { if (this.checked) alert('foo'); else alert('bar'); }\n"
                        + "function doOnload() { document.getElementById('clickId').onclick = doFoo;}\n"
                        + "</script>\n"
                        + "</head><body onload=\"doOnload();\">\n"
                        + "<input type='checkbox' id='clickId'>\n"
                        + "</body></html>");
    }

    /**
     * Test when HtmlTableRow.onclick is set by a JavaScript.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTableRow_onClickSetByNestedScript() throws Exception {
        onClickBodyTest("<body><table><tbody><tr id='clickId'><td>cell value</td></tr></tbody></table>\n"
                        + "<script language='JavaScript'>\n"
                                + "function doFoo(event) { alert('foo'); }\n"
                                + "document.getElementById('clickId').onclick = doFoo;</script></body>\n");
    }

    /**
     * Test onClick handler and click method of teletype element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testTeletype_onClick() throws Exception {
        onClickSimpleTest("tt");
    }

    /**
     * Test onClick handler and click method of underline element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testUnderline_onClick() throws Exception {
        onClickSimpleTest("u");
    }

    /**
     * Test onClick handler and click method of unordered List element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testUnorderedList_onClick() throws Exception {
        onClickBodyTest("<body><ul id='clickId' onClick='alert(\"foo\")'><li>Item</li></ul></body>\n");
    }

    /**
     * Test onClick handler and click method of variable element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testVariable_onClick() throws Exception {
        onClickSimpleTest("var");
    }

    /**
     * Test setting onClick handler from inside the onClick handler.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void testSetOnClick() throws Exception {
        onClickPageTest("<html><body><form>\n"
                + "<button type='button' id='clickId' onclick='alert(\"foo\"); onclick=null;'>Item</button>\n"
                + "</form></body></html>", 2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDblClick() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    document.getElementById('myTextarea').value+='click-';\n"
            + "  }\n"
            + "  function dblClickMe() {\n"
            + "    document.getElementById('myTextarea').value+='dblclick-';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onclick='clickMe()' ondblclick='dblClickMe()'>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlBody body = page.getHtmlElementById("myBody");
        // WebDriver has currently no support for double click
        // (http://code.google.com/p/webdriver/issues/detail?id=161)
        body.dblClick();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals("click-dblclick-", textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void clickOnFocus() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form>\n"
            + "    <input type='button' id='textfield1' onfocus='alert(1)'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("textfield1")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
