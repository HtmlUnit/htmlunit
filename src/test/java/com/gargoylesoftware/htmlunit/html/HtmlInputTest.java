/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;

/**
 * Tests for {@link HtmlInput}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public final class HtmlInputTest extends SimpleWebTestCase {

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
        final HtmlPage secondPage = pushButton.click();

        assertEquals("url", URL_FIRST + "?foo=2&button=foo", secondPage.getUrl());
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
        assertFalse("Initial state", checkbox.isChecked());
        checkbox.setChecked(true);
        assertTrue("After setSelected(true)", checkbox.isChecked());
        checkbox.setChecked(false);
        assertFalse("After setSelected(false)", checkbox.isChecked());
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
    public void setValueAttribute() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='text' name='text1' onchange='alert(\"changed\")')>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlTextInput input = form.getInputByName("text1");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        input.setValueAttribute("foo");
        input.setValue("foo");
        assertNotEquals(Arrays.asList("changed").toString(), collectedAlerts.toString());
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
        assertEquals("on", input.getValue());
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

        assertFalse("Should not be checked before click", radioButton.isChecked());
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

        assertEquals("text", form.getInputByName("foo").getTypeAttribute());
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
        final List<String> collectedAlerts = new ArrayList<>();
        loadPage(htmlContent, collectedAlerts);
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception
     *         if the test fails
     */
    @Test
    public void testRequiredValidation() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input id='foo' required='required'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlInput input = (HtmlInput) page.getElementById("foo");
        assertFalse(input.isValid());
    }
}
