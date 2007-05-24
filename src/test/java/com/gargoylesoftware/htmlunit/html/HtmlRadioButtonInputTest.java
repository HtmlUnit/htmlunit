/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for HtmlRadioButtonInput
 *
 * @version $Revision$
 * @author Mike Bresnahan
 * @author Marc Guillemot
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 */
public class HtmlRadioButtonInputTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlRadioButtonInputTest( final String name ) {
        super( name );
    }

    /**
     * Verifies that a asText() returns "checked" or "unckecked" according to the state of the radio.
     * @throws Exception if the test fails
     */
    public void test_asTextWhenNotChecked() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "    <input type='radio' name='radio' id='radio'>Check me</input>"
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
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "    <input type='radio' name='radio' id='radio'"
            + "onchange='this.value=\"new\" + this.checked'>Check me</input>"
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
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "    <input type='radio' name='radio' id='radio'"
            + "onchange='this.value=\"new\" + this.checked'>Check me</input>"
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
            = "<html><head><title>foo</title></head><body>"
            + "<script type='text/javascript'>"
            + "    function itemOnClickHandler() {"
            + "        var oneItem = document.getElementById('oneItem');"
            + "        var twoItems = document.getElementById('twoItems');"
            + "        alert('oneItem.checked: ' + oneItem.checked + ' twoItems.checked: ' + twoItems.checked);"
            + "    }"
            + "</script>"
            + "<form name='testForm'>"
            + "Number of items:"
            + "<input type='radio' name='numOfItems' value='1' checked='checked' "
            + "  onclick='itemOnClickHandler()' id='oneItem'>"
            + "<label for='oneItem'>1</label>"
            + "<input type='radio' name='numOfItems' value='2' onclick='itemOnClickHandler()' id='twoItems'>"
            + "<label for='twoItems'>2</label>" 
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

        final List expectedAlerts = Arrays.asList(new String[] { 
            "oneItem.checked: false twoItems.checked: true",
            "oneItem.checked: true twoItems.checked: false"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetChecked() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>"
            + "<form>"
            + "<input id='myRadio' type='radio' onchange=\"window.location.href='http://second'\">"
            + "</form>"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";
        
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlRadioButtonInput radio = (HtmlRadioButtonInput) page.getHtmlElementById( "myRadio" );

        final HtmlPage secondPage = (HtmlPage) radio.setChecked( true );

        assertEquals( "Second", secondPage.getTitleText() );
    }

}
