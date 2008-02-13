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

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase2;

/**
 * Tests for {@link HtmlCheckBoxInput}.
 *
 * @version $Revision$
 * @author Mike Bresnahan
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlCheckBoxInputTest extends WebTestCase2 {

    /**
     * Verifies that a HtmlCheckBox is unchecked by default.
     * The onClick tests make this assumption.
     * @throws Exception if the test fails
     */
    @Test
    public void test_defaultState() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='checkbox' name='checkbox' id='checkbox'>Check me</input>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput) page.getHtmlElementById("checkbox");

        assertFalse(checkBox.isChecked());
    }

    /**
     * Given a onclick handler that does not cause the form to submit, this test
     * verifies that HtmlCheckBix.click()
     * <ul>
     *   <li>sets the checkbox to the "checked" state</li>
     *   <li>returns the same page</li>
     * </ul>
     * @throws Exception if the test fails
     */
    @Test
    public void test_onClick() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")' onReset='alert(\"reset\")'>\n"
            + "    <input type='checkbox' name='checkbox' id='checkbox' "
            + "onClick='alert(\"foo\");alert(event.type);'>Check me</input>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput) page.getHtmlElementById("checkbox");
        final HtmlPage secondPage = (HtmlPage) checkBox.click();

        final String[] expectedAlerts = {"foo", "click"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertSame(page, secondPage);
        assertTrue(checkBox.isChecked());
    }

    /**
     * Given a onclick handler that causes the form to submit, this test verifies that HtmlCheckBix.click()
     * <ul>
     *   <li>sets the checkbox to the "checked" state</li>
     *   <li>returns the new page</li>
     * </ul>
     * @throws Exception if the test fails
     */
    @Test
    public void test_onClickThatSubmitsForm() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' name='form1'>\n"
            + "    <input type='checkbox' name='checkbox' id='checkbox' "
            + "onClick='document.form1.submit()'>Check me</input>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput) page.getHtmlElementById("checkbox");

        final HtmlPage secondPage = (HtmlPage) checkBox.click();

        assertNotSame(page, secondPage);
        assertTrue(checkBox.isChecked());
    }

    /**
     * Verifies that a asText() returns "checked" or "unchecked" according to the state of the checkbox.
     * @throws Exception if the test fails
     */
    @Test
    public void testAsText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='checkbox' name='checkbox' id='checkbox'>Check me</input>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput) page.getHtmlElementById("checkbox");
        assertEquals("unchecked", checkBox.asText());
        checkBox.setChecked(true);
        assertEquals("checked", checkBox.asText());
    }
    
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOnchangeFires() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='checkbox' id='chkbox' onchange='alert(\"foo\");' />\n"
            + "</form>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"foo"};

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) page.getHtmlElementById("chkbox");
        checkbox.setChecked(true);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSetChecked() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input id='myCheckbox' type='checkbox' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
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
        final HtmlCheckBoxInput radio = (HtmlCheckBoxInput) page.getHtmlElementById("myCheckbox");

        final HtmlPage secondPage = (HtmlPage) radio.setChecked(true);

        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testPreventDefault() throws Exception {
        testPreventDefault(BrowserVersion.FIREFOX_2);
        testPreventDefault(BrowserVersion.INTERNET_EXPLORER_7_0);
    }

    private void testPreventDefault(final BrowserVersion browserVersion) throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e)\n"
            + "      e.preventDefault();\n"
            + "    else\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('checkbox1').onclick = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<input type='checkbox' id='checkbox1'/>\n"
            + "</body></html>";
        final HtmlPage page = (HtmlPage) loadPage(browserVersion, html, null);
        final HtmlCheckBoxInput checkbox1 = (HtmlCheckBoxInput) page.getHtmlElementById("checkbox1");
        checkbox1.click();
        assertFalse(checkbox1.isChecked());
    }
}
