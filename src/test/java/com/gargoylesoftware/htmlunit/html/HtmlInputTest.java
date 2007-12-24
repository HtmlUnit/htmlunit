/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlInput}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public final class HtmlInputTest extends WebTestCase {
    /**
     * Create an instance
     *
     * @param name The name of the test
     */
    public HtmlInputTest(final String name) {
        super(name);
    }

    /**
     * Test that selecting one radio button will deselect all the others
     *
     * @exception Exception If the test fails
     */
    public void testRadioButtonsAreMutuallyExclusive() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='radio' name='foo' value='1' selected='selected'/>\n"
            + "<input type='radio' name='foo' value='2'/>\n"
            + "<input type='radio' name='foo' value='3'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        final HtmlRadioButtonInput radioButton = (HtmlRadioButtonInput) form.getByXPath(
            "//input[@type='radio' and @name='foo' and @value='2']").get(0);

        final HtmlSubmitInput pushButton = (HtmlSubmitInput) form.getInputByName("button");

        radioButton.setChecked(true);

        // Test that only one value for the radio button is being passed back to the server
        final HtmlPage secondPage = (HtmlPage) pushButton.click();

        assertEquals("url", URL_GARGOYLE.toExternalForm() + "?foo=2&button=foo",
                secondPage.getWebResponse().getUrl());
        assertEquals("method", SubmitMethod.GET, webConnection.getLastMethod());
        assertNotNull(secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSetChecked_CheckBox() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='checkbox' name='foo'/>\n"
            + "<input type='checkbox' name='bar'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        final HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) form.getInputByName("foo");
        assertFalse("Initial state", checkbox.isChecked());
        checkbox.setChecked(true);
        assertTrue("After setSelected(true)", checkbox.isChecked());
        checkbox.setChecked(false);
        assertFalse("After setSelected(false)", checkbox.isChecked());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetChecked_RadioButton() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='radio' name='radio1'>\n"
            + "<input type='RADIO' name='radio1' value='bar' checked>\n"
            + "<input type='submit' name='button' value='foo'>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        final List radioButtons = form.getRadioButtonsByName("radio1");
        assertEquals(2, radioButtons.size());

        assertFalse(((HtmlRadioButtonInput) radioButtons.get(0)).isChecked());
        assertTrue(((HtmlRadioButtonInput) radioButtons.get(1)).isChecked());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOnChangeHandler() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='text' name='text1' onchange='alert(\"changed\")')>\n"
            + "</form></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final HtmlTextInput input = (HtmlTextInput) form.getInputByName("text1");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        input.setValueAttribute("foo");
        assertEquals(new String[] {"changed"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testCheckboxDefaultValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='checkbox' name='checkbox1')>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final HtmlCheckBoxInput input = (HtmlCheckBoxInput) form.getInputByName("checkbox1");
        assertEquals("on", input.getValueAttribute());
    }

    /**
     * Test that clicking a radio button will select it
     *
     * @exception Exception If the test fails
     */
    public void testClickRadioButton() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='radio' name='foo' value='1' selected='selected'/>\n"
            + "<input type='radio' name='foo' value='2'/>\n"
            + "<input type='radio' name='foo' value='3'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        final HtmlRadioButtonInput radioButton = (HtmlRadioButtonInput) form.getByXPath(
                "//input[@type='radio' and @name='foo' and @value='2']").get(0);

        assertFalse("Should not be checked before click", radioButton.isChecked());
        radioButton.click();
        assertTrue("Should be checked after click", radioButton.isChecked());
    }

    /**
     * Test that default type of input is text
     *
     * @exception Exception If the test fails
     */
    public void testInputNoType() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input name='foo'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        assertEquals("text", form.getInputByName("foo").getTypeAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOnChangeHandlerNotFiredOnLoad() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='file' name='text1' onchange='alert(\"changed\")')>\n"
            + "</form></body></html>";
        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testBadInputType() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<body onload='alert(document.form1.text1.type)'>\n"
            + "<form name='form1'>\n"
            + "<input type='foo' name='text1'>\n"
            + "</form></body></html>";
        final String[] expectedAlerts = {"text"};
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(htmlContent, collectedAlerts);
    }

    /**
     * @throws Exception If the test fails.
     */
    public void testOnchangeNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function handler() {}\n"
            + "  function test() {\n"
            + "    var elem = document.getElementById('myInput');\n"
            + "    elem.onchange = handler;\n"
            + "    alert(elem.onchange);\n"
            + "    elem.onchange = null;\n"
            + "    alert(elem.onchange);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <input id='myInput'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"\nfunction handler() {\n}\n", "null"};
        final List collectedAlerts = new ArrayList();
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
