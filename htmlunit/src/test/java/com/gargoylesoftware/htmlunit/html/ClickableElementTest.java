/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link ClickableElement}.
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 */
public class ClickableElementTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public ClickableElementTest( final String name ) {
        super( name );
    }


    /**
     * Full page driver for onClick tests.
     *
     * @param htmlContent HTML fragment for body of page with clickable element
     * identified by clickId ID attribute.  Must have onClick that raises
     * an alert of "foo".
     * @throws Exception if the test fails
     */
    private void onClickPageTest(final String htmlContent) throws Exception {
        final List expectedAlerts = Arrays.asList( new String[]{"foo"} );
        onClickPageTest(htmlContent, 1, expectedAlerts);
    }

    /**
     * Full page driver for onClick tests.
     *
     * @param htmlContent HTML fragment for body of page with clickable element
     * identified by clickId ID attribute.
     * @param numClicks number of times to click element
     * @param expectedAlerts List of expected popup values
     * @throws Exception if the test fails
     */
    private void onClickPageTest(final String htmlContent, final int numClicks, final List expectedAlerts)
        throws Exception {
        onClickPageTest(htmlContent, numClicks, expectedAlerts, false);
    }

        /**
        * Full page driver for onClick tests.
        *
        * @param htmlContent HTML fragment for body of page with clickable element
        * identified by clickId ID attribute.
        * @param numClicks number of times to click element
        * @param expectedAlerts List of expected popup values
        * @param exceptionOnError
        * @throws Exception if the test fails
        */
    private void onClickPageTest(final String htmlContent, final int numClicks,
            final List expectedAlerts, final boolean exceptionOnError) throws Exception {
        final BrowserVersion bv = new BrowserVersion("Netscape", "7", "", "1.2", 7);
        final WebClient client = new WebClient(bv);

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );
        client.setThrowExceptionOnScriptError(exceptionOnError);

        final List collectedAlerts = new ArrayList();
        final CollectingAlertHandler alertHandler = new CollectingAlertHandler(collectedAlerts);
        client.setAlertHandler(alertHandler);

        final HtmlPage page = ( HtmlPage ) client.getPage(URL_GARGOYLE);
        final ClickableElement clickable = ( ClickableElement )page.getHtmlElementById( "clickId" );

        for (int i=0; i<numClicks; i++) {
            clickable.click();
        }

        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Body driver for onClick tests.
     *
     * @param htmlBody HTML text body
     * @throws Exception if the test fails
     */
    private void onClickBodyTest(final String htmlBody) throws Exception {
        onClickPageTest("<html><head><title>foo</title></head>" + htmlBody
                 + "</html>");
    }


    /**
     * Simple tag name driver for onClick tests.
     *
     * @param tagName HTML tag name for simple tag with text body
     * @throws Exception if the test fails
     */
    private void onClickSimpleTest(final String tagName) throws Exception {
        onClickBodyTest("<body><" + tagName + " id='clickId' onClick='alert(\"foo\")'>Text</" + tagName + "></body>");
    }


    /**
     * Test onClick handler and click method of anchor element.
     *
     * @throws Exception if the test fails
     */
    public void testAnchor_onClick() throws Exception {
        onClickSimpleTest("a");
    }


    /**
     * Test onClick handler and click method of abbreviation element.
     *
     * @throws Exception if the test fails
     */
    public void testAbbreviation_onClick() throws Exception {
        onClickSimpleTest("abbr");
    }


    /**
     * Test onClick handler and click method of acronym element.
     *
     * @throws Exception if the test fails
     */
    public void testAcronym_onClick() throws Exception {
        onClickSimpleTest("acronym");
    }


    /**
     * Test onClick handler and click method of address element.
     *
     * @throws Exception if the test fails
     */
    public void testAddress_onClick() throws Exception {
        onClickSimpleTest("address");
    }


    /**
     * Test onClick handler and click method of area element.
     *
     * @throws Exception if the test fails
     */
    public void testArea_onClick() throws Exception {
        onClickBodyTest("<body><map><area id='clickId' onClick='alert(\"foo\")'/></map></body>");
    }


    /**
     * Test onClick handler and click method of bold element.
     *
     * @throws Exception if the test fails
     */
    public void testBold_onClick() throws Exception {
        onClickSimpleTest("b");
    }


    /**
     * Test onClick handler and click method of big element.
     *
     * @throws Exception if the test fails
     */
    public void testBig_onClick() throws Exception {
        onClickSimpleTest("big");
    }


    /**
     * Test onClick handler and click method of blockquote element.
     *
     * @throws Exception if the test fails
     */
    public void testBlockquote_onClick() throws Exception {
        onClickSimpleTest("blockquote");
    }


    /**
     * Test onClick handler and click method of body element.
     *
     * @throws Exception if the test fails
     */
    public void testBody_onClick() throws Exception {
        onClickBodyTest("<body id='clickId' onClick='alert(\"foo\")'>Text</body>");
    }


    /**
     * Test onClick handler and click method of button element.
     *
     * @throws Exception if the test fails
     */
    public void testButton_onClick() throws Exception {
        onClickBodyTest("<body><form><button id='clickId' onClick='alert(\"foo\")'>Item</button></form></body>");
    }

    /**
     * Test onClick handler can be called multiple times
     *
     * @throws Exception if the test fails
     */
    public void testButton_onClickTwice() throws Exception {
        final List expectedAlerts = Arrays.asList(new String[] {"foo0", "foo1"});
        onClickPageTest("<body><form>" +
                "<button id='clickId' onClick='alert(\"foo\" + count++)'>Item</button>" +
                "<script> var count = 0 </script>" +
                "</form></body>", 2, expectedAlerts);
    }

    /**
     * Test onClick handler and click method of cite element.
     *
     * @throws Exception if the test fails
     */
    public void testCite_onClick() throws Exception {
        onClickSimpleTest("cite");
    }


    /**
     * Test onClick handler and click method of code element.
     *
     * @throws Exception if the test fails
     */
    public void testCode_onClick() throws Exception {
        onClickSimpleTest("code");
    }


    /**
     * Test onClick handler and click method of table column element.
     *
     * @throws Exception if the test fails
     */
    public void testTableColumn_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup>"
            + "<col id='clickId' onClick='alert(\"foo\")'/></colgroup><thead><tr><th>"
            + "Header</th></tr></thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr>"
            + "th>Header</th></tr></tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of table column group element.
     *
     * @throws Exception if the test fails
     */
    public void testTableColumnGroup_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption>"
            + "<colgroup id='clickId' onClick='alert(\"foo\")'><col/></colgroup><thead>"
            + "<tr><th>Header</th></tr></thead><tbody><tr><td>Data</td></tr></tbody><tfoot>"
            + "<tr><th>Header</th></tr></tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of center element.
     *
     * @throws Exception if the test fails
     */
    public void testCenter_onClick() throws Exception {
        onClickSimpleTest("center");
    }


    /**
     * Test onClick handler and click method of table caption element.
     *
     * @throws Exception if the test fails
     */
    public void testTableCaption_onClick() throws Exception {
        onClickBodyTest("<body><table><caption id='clickId' onClick='alert(\"foo\")'>"
            + "Caption</caption><colgroup><col/></colgroup><thead><tr><th>Header</th></tr>"
            + "</thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr><th>Header</th></tr>"
            + "</tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of definition description dd element.
     *
     * @throws Exception if the test fails
     */
    public void testDefinitionDescription_onClick() throws Exception {
        onClickBodyTest("<body><dl><dt>Term</dt><dd id='clickId' onClick='alert(\"foo\")'>Definition</dd></dl></body>");
    }


    /**
     * Test that no NPE is thrown when JS fails on link click 
     * and WebClient.setThrowExceptionOnScriptError(false) is used.
     * Test for bug 1385864.
     * @throws Exception if the test fails
     */
    public void testJavaScriptError_onClick() throws Exception {
        onClickPageTest("<html><head></head><body>"
                + "<form method='POST'><input type='button' id='clickId' onclick='y()'></form>"
                + "</body></html>",
                1, new ArrayList(), false);
    }


    /**
     * Test onClick handler and click method of definition element.
     *
     * @throws Exception if the test fails
     */
    public void testDefinition_onClick() throws Exception {
        onClickSimpleTest("dfn");
    }


    /**
     * Test onClick handler and click method of directory element.
     *
     * @throws Exception if the test fails
     */
    public void testDirectory_onClick() throws Exception {
        onClickSimpleTest("dir");
    }


    /**
     * Test onClick handler and click method of definition list dl element.
     *
     * @throws Exception if the test fails
     */
    public void testDefinitionList_onClick() throws Exception {
        onClickBodyTest("<body><dl id='clickId' onClick='alert(\"foo\")'><dt>Term</dt><dd>Definition</dd></dl></body>");
    }


    /**
     * Test onClick handler and click method of definition term dt element.
     *
     * @throws Exception if the test fails
     */
    public void testDefinitionTerm_onClick() throws Exception {
        onClickBodyTest("<body><dl><dt id='clickId' onClick='alert(\"foo\")'>Term</dt><dd>Definition</dd></dl></body>");
    }


    /**
     * Test onClick handler and click method of deleted text element.
     *
     * @throws Exception if the test fails
     */
    public void testDeletedText_onClick() throws Exception {
        onClickSimpleTest("del");
    }


    /**
     * Test onClick handler and click method of Text Direction element.
     *
     * @throws Exception if the test fails
     */
    public void testTextDirection_onClick() throws Exception {
        onClickSimpleTest("dir");
    }


    /**
     * Test onClick handler and click method of division element.
     *
     * @throws Exception if the test fails
     */
    public void testDivision_onClick() throws Exception {
        onClickSimpleTest("div");
    }


    /**
     * Test onClick handler and click method of emphasis element.
     *
     * @throws Exception if the test fails
     */
    public void testEmphasis_onClick() throws Exception {
        onClickSimpleTest("em");
    }


    /**
     * Test onClick handler and click method of field set element.
     *
     * @throws Exception if the test fails
     */
    public void testFieldSet_onClick() throws Exception {
        onClickBodyTest("<body><form><fieldset id='clickId' onClick='alert(\"foo\")'>"
            + "<legend>Legend</legend></fieldset></form></body>");
    }


    /**
     * Test onClick handler and click method of form element.
     *
     * @throws Exception if the test fails
     */
    public void testForm_onClick() throws Exception {
        onClickSimpleTest("form");
    }


    /**
     * Test onClick handler and click method of italics element.
     *
     * @throws Exception if the test fails
     */
    public void testItalics_onClick() throws Exception {
        onClickSimpleTest("i");
    }


    /**
     * Test onClick handler and click method of image element.
     *
     * @throws Exception if the test fails
     */
    public void testImage_onClick() throws Exception {
        onClickSimpleTest("img");
    }


    /**
     * Test onClick handler and click method of header1 element.
     *
     * @throws Exception if the test fails
     */
    public void testHeader1_onClick() throws Exception {
        onClickSimpleTest("h1");
    }


    /**
     * Test onClick handler and click method of header2 element.
     *
     * @throws Exception if the test fails
     */
    public void testHeader2_onClick() throws Exception {
        onClickSimpleTest("h2");
    }


    /**
     * Test onClick handler and click method of header3 element.
     *
     * @throws Exception if the test fails
     */
    public void testHeader3_onClick() throws Exception {
        onClickSimpleTest("h3");
    }


    /**
     * Test onClick handler and click method of header4 element.
     *
     * @throws Exception if the test fails
     */
    public void testHeader4_onClick() throws Exception {
        onClickSimpleTest("h4");
    }


    /**
     * Test onClick handler and click method of header5 element.
     *
     * @throws Exception if the test fails
     */
    public void testHeader5_onClick() throws Exception {
        onClickSimpleTest("h5");
    }


    /**
     * Test onClick handler and click method of header6 element.
     *
     * @throws Exception if the test fails
     */
    public void testHeader6_onClick() throws Exception {
        onClickSimpleTest("h6");
    }


    /**
     * Test onClick handler and click method of horizontal rule element.
     *
     * @throws Exception if the test fails
     */
    public void testHorizontalRule_onClick() throws Exception {
        onClickSimpleTest("hr");
    }

    /**
     * Test onClick handler and click method of input element.
     *
     * @throws Exception if the test fails
     */
    public void testInput_onClick() throws Exception {
        onClickBodyTest("<body><form><input id='clickId' onClick='alert(\"foo\")'>Item</input></form></body>");
    }


    /**
     * Test onClick handler and click method of inserted text element.
     *
     * @throws Exception if the test fails
     */
    public void testInsertedText_onClick() throws Exception {
        onClickSimpleTest("ins");
    }


    /**
     * Test onClick handler and click method of keyboard element.
     *
     * @throws Exception if the test fails
     */
    public void testKeyboard_onClick() throws Exception {
        onClickSimpleTest("kbd");
    }


    /**
     * Test onClick handler and click method of label element.
     *
     * @throws Exception if the test fails
     */
    public void testLabel_onClick() throws Exception {
        onClickBodyTest("<body><form><label id='clickId' onClick='alert(\"foo\")'>Item</label></form></body>");
    }


    /**
     * Test onClick handler and click method of legend element.
     *
     * @throws Exception if the test fails
     */
    public void testLegend_onClick() throws Exception {
        onClickBodyTest("<body><form><fieldset><legend id='clickId' onClick='alert(\"foo\")'>"
            + "Legend</legend></fieldset></form></body>");
    }


    /**
     * Test onClick handler and click method of link element.
     *
     * @throws Exception if the test fails
     */
    public void XXtestLink_onClick() throws Exception {
        onClickPageTest("<html><head><title>foo</title><link id='clickId' onClick='alert(\"foo\")'/>"
            + "</head><body></body></html>");
    }


    /**
     * Test onClick handler and click method of List Item element.
     *
     * @throws Exception if the test fails
     */
    public void testListItem_onClick() throws Exception {
        onClickBodyTest("<body><ol><li id='clickId' onClick='alert(\"foo\")'>Item</li></ol></body>");
    }


    /**
     * Test onClick handler and click method of map element.
     *
     * @throws Exception if the test fails
     */
    public void testMap_onClick() throws Exception {
        onClickBodyTest("<body><map id='clickId' onClick='alert(\"foo\")'><area/></map></body>");
    }


    /**
     * Test onClick handler and click method of menu element.
     *
     * @throws Exception if the test fails
     */
    public void testMenu_onClick() throws Exception {
        onClickBodyTest("<body><menu id='clickId' onClick='alert(\"foo\")'><li>Item</li></menu></body>");
    }


    /**
     * Test onClick handler and click method of no frames element.
     *
     * @throws Exception if the test fails
     */
    public void XXtestNoFrames_onClick() throws Exception {
        onClickPageTest("<html><head><title>foo</title></head><frameset><frame/>"
            + "<noframes id='clickId' onClick='alert(\"foo\")'/></frameset></html>");
    }


    /**
     * Test onClick handler and click method of no script element.
     *
     * @throws Exception if the test fails
     */
    public void testNoScript_onClick() throws Exception {
        onClickBodyTest("<body><script>var i=0;</script><noscript id='clickId' "
            + "onClick='alert(\"foo\")'>Item</noscript></body>");
    }


    /**
     * Test onClick handler and click method of object element.
     *
     * @throws Exception if the test fails
     */
    public void testObject_onClick() throws Exception {
        onClickSimpleTest("object");
    }


    /**
     * Test onClick handler and click method of option element.
     *
     * @throws Exception if the test fails
     */
    public void testOption_onClick() throws Exception {
        onClickBodyTest("<body><form><select><option id='clickId' onClick='alert(\"foo\")'>"
            + "Option</option></select></form></body>");
    }


    /**
     * Test onClick handler and click method of Option Group element.
     *
     * @throws Exception if the test fails
     */
    public void testOptionGroup_onClick() throws Exception {
        onClickBodyTest("<body><form><select><optgroup id='clickId' onClick='alert(\"foo\")'>"
            + "<option>Option</option></optgroup></select></form></body>");
    }


    /**
     * Test onClick handler and click method of Ordered List element.
     *
     * @throws Exception if the test fails
     */
    public void testOrderedList_onClick() throws Exception {
        onClickBodyTest("<body><ol id='clickId' onClick='alert(\"foo\")'><li>Item</li></ol></body>");
    }


    /**
     * Test onClick handler and click method of paragraph element.
     *
     * @throws Exception if the test fails
     */
    public void testParagraph_onClick() throws Exception {
        onClickSimpleTest("p");
    }


    /**
     * Test onClick handler and click method of pre element.
     *
     * @throws Exception if the test fails
     */
    public void testPre_onClick() throws Exception {
        onClickSimpleTest("pre");
    }


    /**
     * Test onClick handler and click method of inline Quotation element.
     *
     * @throws Exception if the test fails
     */
    public void testQuotation_onClick() throws Exception {
        onClickSimpleTest("q");
    }


    /**
     * Test onClick handler and click method of strikethrough element.
     *
     * @throws Exception if the test fails
     */
    public void testStrikethrough_onClick() throws Exception {
        onClickSimpleTest("s");
    }


    /**
     * Test onClick handler and click method of sample element.
     *
     * @throws Exception if the test fails
     */
    public void testSample_onClick() throws Exception {
        onClickSimpleTest("samp");
    }


    /**
     * Test onClick handler and click method of select element.
     *
     * @throws Exception if the test fails
     */
    public void testSelect_onClick() throws Exception {
        onClickBodyTest("<body><form><select id='clickId' onClick='alert(\"foo\")'>"
            + "<option>Option</option></select></form></body>");
    }


    /**
     * Test onClick handler and click method of small element.
     *
     * @throws Exception if the test fails
     */
    public void testSmall_onClick() throws Exception {
        onClickSimpleTest("small");
    }


    /**
     * Test onClick handler and click method of span element.
     *
     * @throws Exception if the test fails
     */
    public void testSpan_onClick() throws Exception {
        onClickSimpleTest("span");
    }


    /**
     * Test onClick handler and click method of strike element.
     *
     * @throws Exception if the test fails
     */
    public void testStrike_onClick() throws Exception {
        onClickSimpleTest("strike");
    }


    /**
     * Test onClick handler and click method of subscript element.
     *
     * @throws Exception if the test fails
     */
    public void testSubscript_onClick() throws Exception {
        onClickSimpleTest("sub");
    }


    /**
     * Test onClick handler and click method of superscript element.
     *
     * @throws Exception if the test fails
     */
    public void testSuperscript_onClick() throws Exception {
        onClickSimpleTest("sup");
    }


    /**
     * Test onClick handler and click method of table element.
     *
     * @throws Exception if the test fails
     */
    public void testTable_onClick() throws Exception {
        onClickBodyTest("<body><table id='clickId' onClick='alert(\"foo\")'><caption>"
            + "Caption</caption><colgroup><col/></colgroup><thead><tr><th>Header</th></tr>"
            + "</thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr><th>Header</th></tr>"
            + "</tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of table body element.
     *
     * @throws Exception if the test fails
     */
    public void testTableBody_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>"
            + "</colgroup><thead><tr><th>Header</th></tr></thead>"
            + "<tbody id='clickId' onClick='alert(\"foo\")'><tr><td>Data</td></tr>"
            + "</tbody><tfoot><tr><th>Header</th></tr></tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of table data cell element.
     *
     * @throws Exception if the test fails
     */
    public void testTableDataCell_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>"
            + "</colgroup><thead><tr><th>Header</th></tr></thead><tbody><tr>"
            + "<td id='clickId' onClick='alert(\"foo\")'>Data</td></tr></tbody>"
            + "<tfoot><tr><th>Header</th></tr></tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of textarea element.
     *
     * @throws Exception if the test fails
     */
    public void testTextarea_onClick() throws Exception {
        onClickBodyTest("<body><form><textarea id='clickId' onClick='alert(\"foo\")'>Item</textarea></form></body>");
    }


    /**
     * Test onClick handler and click method of table footer element.
     *
     * @throws Exception if the test fails
     */
    public void testTableFooter_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>"
            + "</colgroup><thead><tr><th>Header</th></tr></thead><tbody><tr><td>Data</td>"
            + "</tr></tbody><tfoot id='clickId' onClick='alert(\"foo\")'><tr><th>Header</th>"
            + "</tr></tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of table header cell element.
     *
     * @throws Exception if the test fails
     */
    public void testTableHeaderCell_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup>"
            + "<col/></colgroup><thead><tr><th id='clickId' onClick='alert(\"foo\")'>"
            + "Header</th></tr></thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr>"
            + "<th>Header</th></tr></tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of table header element.
     *
     * @throws Exception if the test fails
     */
    public void testTableHeader_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>"
            + "</colgroup><thead id='clickId' onClick='alert(\"foo\")'><tr><th>Header</th>"
            + "</tr></thead><tbody><tr><td>Data</td></tr></tbody><tfoot><tr><th>Header</th>"
            + "</tr></tfoot></table></body>");
    }


    /**
     * Test onClick handler and click method of table row element.
     *
     * @throws Exception if the test fails
     */
    public void testTableRow_onClick() throws Exception {
        onClickBodyTest("<body><table><caption>Caption</caption><colgroup><col/>"
            + "</colgroup><thead><tr><th>Header</th></tr></thead><tbody>"
            + "<tr id='clickId' onClick='alert(\"foo\")'><td>Data</td></tr></tbody>"
            + "<tfoot><tr><th>Header</th></tr></tfoot></table></body>");
    }

    /**
     * Test when HtmlTableRow.onclick is set by a javascript
     *
     * @throws Exception if the test fails
     */
    public void testTableRow_onClickSetOnLoad() throws Exception {
        onClickPageTest("<html><head>"
                        + "<script language='JavaScript'>"
                        + "function doFoo() { alert('foo');        }"
                        + "function doOnload() { document.getElementById('clickId').onclick = doFoo;}"
                        + "</script>"
                        + "</head><body onload=\"doOnload();\">"
                        + "<table><tbody><tr id='clickId'><td>cell value</td></tr></tbody></table>"
                        + "</body></html>");
    }

    /**
     *
     * @throws Exception if the test fails
     */
    public void testCheckbox_onClickUpdatesStateFirst() throws Exception {
        onClickPageTest("<html><head>"
                        + "<script language='JavaScript'>"
                        + "function doFoo(event) { if (this.checked) alert('foo'); else alert('bar'); }"
                        + "function doOnload() { document.getElementById('clickId').onclick = doFoo;}"
                        + "</script>"
                        + "</head><body onload=\"doOnload();\">"
                        + "<input type='checkbox' id='clickId'>"
                        + "</body></html>");
    }

    /**
     * Test when HtmlTableRow.onclick is set by a javascript
     *
     * @throws Exception if the test fails
     */
    public void testTableRow_onClickSetByNestedScript() throws Exception {
        onClickBodyTest("<body><table><tbody><tr id='clickId'><td>cell value</td></tr></tbody></table>"
                        + "<script language='JavaScript'>"
                                + "function doFoo(event) { alert('foo');        }"
                                + "document.getElementById('clickId').onclick = doFoo;</script></body>");
    }

    /**
     * Test onClick handler and click method of teletype element.
     *
     * @throws Exception if the test fails
     */
    public void testTeletype_onClick() throws Exception {
        onClickSimpleTest("tt");
    }


    /**
     * Test onClick handler and click method of underline element.
     *
     * @throws Exception if the test fails
     */
    public void testUnderline_onClick() throws Exception {
        onClickSimpleTest("u");
    }


    /**
     * Test onClick handler and click method of unordered List element.
     *
     * @throws Exception if the test fails
     */
    public void testUnorderedList_onClick() throws Exception {
        onClickBodyTest("<body><ul id='clickId' onClick='alert(\"foo\")'><li>Item</li></ul></body>");
    }


    /**
     * Test onClick handler and click method of variable element.
     *
     * @throws Exception if the test fails
     */
    public void testVariable_onClick() throws Exception {
        onClickSimpleTest("var");
    }

    /**
     * Test setting onClick handler from inside the onClick handler
     *
     * @throws Exception if the test fails
     */
    public void testSetOnClick() throws Exception {
        final List expectedAlerts = Arrays.asList(new String[] {"foo"});
        onClickPageTest("<html><body><form>" +
                "<button type='button' id='clickId' onclick='alert(\"foo\"); onclick=null;'>Item</button>" +
                "</form></body></html>", 2, expectedAlerts);
    }
}
