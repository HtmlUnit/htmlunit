/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
import java.util.List;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlRadioButtonInput}.
 *
 * @version $Revision$
 * @author Mike Bresnahan
 * @author Marc Guillemot
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 */
public class HtmlRadioButtonInputTest extends WebTestCase {
    /**
     * Create an instance
     *
     * @param name The name of the test
     */
    public HtmlRadioButtonInputTest(final String name) {
        super(name);
    }

    /**
     * Verifies that a asText() returns "checked" or "unchecked" according to the state of the radio.
     * @throws Exception if the test fails
     */
    public void test_asTextWhenNotChecked() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='radio' name='radio' id='radio'>Check me</input>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlRadioButtonInput radio = (HtmlRadioButtonInput) page.getHtmlElementById("radio");
        assertEquals("unchecked", radio.asText());
        radio.setChecked(true);
        assertEquals("checked", radio.asText());
    }
        
    /**
     * @throws Exception if the test fails
     */
    public void testOnchangeHandlerInvoked() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='radio' name='radio' id='radio'"
            + "onchange='this.value=\"new\" + this.checked'>Check me</input>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        
        final HtmlRadioButtonInput radio = (HtmlRadioButtonInput) page.getHtmlElementById("radio");
        
        assertFalse(radio.isChecked());
        
        radio.setChecked(true);
        
        assertTrue(radio.isChecked());
        
        assertEquals("newtrue", radio.getValueAttribute());
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testOnchangeHandlerNotInvokedIfNotChanged() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='radio' name='radio' id='radio'"
            + "onchange='this.value=\"new\" + this.checked'>Check me</input>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        
        final HtmlRadioButtonInput radio = (HtmlRadioButtonInput) page.getHtmlElementById("radio");
        
        assertFalse(radio.isChecked());
        
        radio.setChecked(false);
        
        assertFalse(radio.isChecked());
        
        assertEquals("on", radio.getValueAttribute());
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testUpdateStateFirstForOnclickHandler() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<script type='text/javascript'>\n"
            + "    function itemOnClickHandler() {"
            + "        var oneItem = document.getElementById('oneItem');\n"
            + "        var twoItems = document.getElementById('twoItems');\n"
            + "        alert('oneItem.checked: ' + oneItem.checked + ' twoItems.checked: ' + twoItems.checked);\n"
            + "    }"
            + "</script>\n"
            + "<form name='testForm'>\n"
            + "Number of items:"
            + "<input type='radio' name='numOfItems' value='1' checked='checked' "
            + "  onclick='itemOnClickHandler()' id='oneItem'>\n"
            + "<label for='oneItem'>1</label>\n"
            + "<input type='radio' name='numOfItems' value='2' onclick='itemOnClickHandler()' id='twoItems'>\n"
            + "<label for='twoItems'>2</label>\n"
            + "</form></body></html>";
        
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlRadioButtonInput oneItem = (HtmlRadioButtonInput) page.getHtmlElementById("oneItem");
        final HtmlRadioButtonInput twoItems = (HtmlRadioButtonInput) page.getHtmlElementById("twoItems");

        assertTrue(oneItem.isChecked());
        assertFalse(twoItems.isChecked());

        twoItems.click();

        assertTrue(twoItems.isChecked());
        assertFalse(oneItem.isChecked());

        oneItem.click();

        assertTrue(oneItem.isChecked());
        assertFalse(twoItems.isChecked());

        final String[] expectedAlerts = {
            "oneItem.checked: false twoItems.checked: true",
            "oneItem.checked: true twoItems.checked: false"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetChecked() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input id='myRadio' type='radio' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";
        
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlRadioButtonInput radio = (HtmlRadioButtonInput) page.getHtmlElementById("myRadio");

        final HtmlPage secondPage = (HtmlPage) radio.setChecked(true);

        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * Test <code>input.checked</code> if the radio <code>&lt;input&gt;</code> do not have distinct 'value'
     * @throws Exception If the test fails
     */
    public void testRadioInputChecked() throws Exception {
        final String content
            = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "<script>\n"
            + "  var r1 = document.forms.myForm.myRadio[0];\n"
            + "  var r2 = document.forms.myForm.myRadio[1];\n"
            + "  alert(r1.checked + ',' + r2.checked);\n"
            + "  r1.checked = true;\n"
            + "  alert(r1.checked + ',' + r2.checked);\n"
            + "  r2.checked = true;\n"
            + "  alert(r1.checked + ',' + r2.checked);\n"
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"false,false", "true,false", "false,true"};
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails
     */
    public void testSetCheckedOutsideForm() throws Exception {
        final String content
            = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<input type='radio' id='radio1' name='myRadio'>\n"
            + "<input type='radio' id='radio2' name='myRadio'>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='radio3' name='myRadio'>\n"
            + "  <input type='radio' id='radio4' name='myRadio'>\n"
            + "</form>\n"
            + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        
        final HtmlRadioButtonInput radio1 = (HtmlRadioButtonInput) page.getHtmlElementById("radio1");
        final HtmlRadioButtonInput radio2 = (HtmlRadioButtonInput) page.getHtmlElementById("radio2");
        final HtmlRadioButtonInput radio3 = (HtmlRadioButtonInput) page.getHtmlElementById("radio3");
        final HtmlRadioButtonInput radio4 = (HtmlRadioButtonInput) page.getHtmlElementById("radio4");

        assertFalse(radio1.isChecked());
        assertFalse(radio2.isChecked());
        assertFalse(radio3.isChecked());
        assertFalse(radio4.isChecked());
        
        radio1.setChecked(true);
       
        assertTrue(radio1.isChecked());
        assertFalse(radio2.isChecked());
        assertFalse(radio3.isChecked());
        assertFalse(radio4.isChecked());
        
        radio2.setChecked(true);
        
        assertFalse(radio1.isChecked());
        assertTrue(radio2.isChecked());
        assertFalse(radio3.isChecked());
        assertFalse(radio4.isChecked());

        radio3.setChecked(true);
        
        assertFalse(radio1.isChecked());
        assertTrue(radio2.isChecked());
        assertTrue(radio3.isChecked());
        assertFalse(radio4.isChecked());

        radio4.setChecked(true);
        
        assertFalse(radio1.isChecked());
        assertTrue(radio2.isChecked());
        assertFalse(radio3.isChecked());
        assertTrue(radio4.isChecked());
    }

}
