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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlInput}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public final class HtmlInputTest extends WebTestCase {

    /**
     * Tests that selecting one radio button will deselect all the others.
     * @exception Exception If the test fails
     */
    @Test
    public void radioButtonsAreMutuallyExclusive() throws Exception {
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

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlRadioButtonInput radioButton = form.getFirstByXPath(
            "//input[@type='radio' and @name='foo' and @value='2']");

        final HtmlSubmitInput pushButton = form.getInputByName("button");

        radioButton.setChecked(true);

        // Test that only one value for the radio button is being passed back to the server
        final HtmlPage secondPage = (HtmlPage) pushButton.click();

        assertEquals("url", getDefaultUrl() + "?foo=2&button=foo",
                secondPage.getWebResponse().getWebRequest().getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        assertNotNull(secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setChecked_CheckBox() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='checkbox' name='foo'/>\n"
            + "<input type='checkbox' name='bar'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlCheckBoxInput checkbox = form.getInputByName("foo");
        Assert.assertFalse("Initial state", checkbox.isChecked());
        checkbox.setChecked(true);
        assertTrue("After setSelected(true)", checkbox.isChecked());
        checkbox.setChecked(false);
        Assert.assertFalse("After setSelected(false)", checkbox.isChecked());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getChecked_RadioButton() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='radio' name='radio1'>\n"
            + "<input type='RADIO' name='radio1' value='bar' checked>\n"
            + "<input type='submit' name='button' value='foo'>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");

        final List<HtmlRadioButtonInput> radioButtons = form.getRadioButtonsByName("radio1");
        assertEquals(2, radioButtons.size());

        assertFalse(radioButtons.get(0).isChecked());
        assertTrue(radioButtons.get(1).isChecked());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onChangeHandler() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='text' name='text1' onchange='alert(\"changed\")')>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlTextInput input = form.getInputByName("text1");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        input.setValueAttribute("foo");
        assertEquals(new String[] {"changed"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void checkboxDefaultValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='checkbox' name='checkbox1')>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlCheckBoxInput input = form.getInputByName("checkbox1");
        assertEquals("on", input.getValueAttribute());
    }

    /**
     * Tests that clicking a radio button will select it.
     * @exception Exception If the test fails
     */
    @Test
    public void clickRadioButton() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='radio' name='foo' value='1' selected='selected'/>\n"
            + "<input type='radio' name='foo' value='2'/>\n"
            + "<input type='radio' name='foo' value='3'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlRadioButtonInput radioButton = form.getFirstByXPath(
                "//input[@type='radio' and @name='foo' and @value='2']");

        Assert.assertFalse("Should not be checked before click", radioButton.isChecked());
        radioButton.click();
        assertTrue("Should be checked after click", radioButton.isChecked());
    }

    /**
     * Tests that default type of input is text.
     * @exception Exception If the test fails
     */
    @Test
    public void inputNoType() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input name='foo'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlForm form = page.getHtmlElementById("form1");

        assertEquals("text", form.<HtmlInput>getInputByName("foo").getTypeAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onChangeHandlerNotFiredOnLoad() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='file' name='text1' onchange='alert(\"changed\")')>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(htmlContent, collectedAlerts);
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void badInputType() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<body onload='alert(document.form1.text1.type)'>\n"
            + "<form name='form1'>\n"
            + "<input type='foo' name='text1'>\n"
            + "</form></body></html>";
        final String[] expectedAlerts = {"text"};
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(htmlContent, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "\nfunction handler() {\n}\n", "null" })
    public void onchangeNull() throws Exception {
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
        loadPageWithAlerts(html);
    }

    /**
     * Tests that clicking a radio button will select it.
     * @exception Exception If the test fails
     */
    @Test
    public void select() throws Exception {
        final String content
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var form = document.getElementById('form1');\n"
            + "    for (var i=0; i<form.elements.length; ++i)\n"
            + "      form.elements[i].select();\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form id='form1'>\n"
            + "<input type='radio' name='foo' value='1'/>\n"
            + "<input type='password' name='pwd'/>\n"
            + "<input type='checkbox' name='cb'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "<textarea name='t'></textarea>\n"
            + "</form></body></html>";

        createTestPageForRealBrowserIfNeeded(content, new String[] {});
        loadPage(content);
    }
}
