/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlButton}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Brad Clarke
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlButtonTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testButtonClick_onClick() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")' onReset='alert(\"reset\")'>\n"
            + "    <button type='button' name='button' id='button' "
            + "onClick='alert(\"foo\")'>Push me</button>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = (HtmlPage) button.click();

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSubmitClick_onClick() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")' onReset='alert(\"reset\")'>\n"
            + "    <button type='submit' name='button' id='button' "
            + "onClick='alert(\"foo\")'>Push me</button>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();

        final String[] expectedAlerts = {"foo", "bar"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertNotSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testResetClick_onClick() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")' onReset='alert(\"reset\")'>\n"
            + "    <button type='reset' name='button' id='button' "
            + "onClick='alert(\"foo\")'>Push me</button>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();

        final String[] expectedAlerts = {"foo", "reset"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReset() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='text' name='textfield1' id='textfield1' value='foo'/>\n"
            + "<input type='password' name='password1' id='password1' value='foo'/>\n"
            + "<input type='hidden' name='hidden1' id='hidden1' value='foo'/>\n"
            + "<input type='radio' name='radioButton' value='foo' checked/>\n"
            + "<input type='radio' name='radioButton' value='bar'/>\n"
            + "<input type='checkbox' name='checkBox' value='check'/>\n"
            + "<select id='select1'>\n"
            + "    <option id='option1' selected value='1'>Option1</option>\n"
            + "    <option id='option2' value='2'>Option2</option>\n"
            + "</select>\n"
            + "<textarea id='textarea1'>Foobar</textarea>\n"
            + "<isindex prompt='Enter some text' id='isindex1'>\n"
            + "<button type='reset' id='resetButton' value='pushme'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlButton resetInput = page.getHtmlElementById("resetButton");

        // change all the values to something else
        form.<HtmlRadioButtonInput>getFirstByXPath(
            "//input[@type='radio' and @name='radioButton' and @value='bar']").setChecked(true);
        ((HtmlCheckBoxInput) form.getInputByName("checkBox")).setChecked(true);
        page.<HtmlOption>getHtmlElementById("option1").setSelected(false);
        page.<HtmlOption>getHtmlElementById("option2").setSelected(true);
        page.<HtmlTextArea>getHtmlElementById("textarea1").setText("Flintstone");
        page.<HtmlTextInput>getHtmlElementById("textfield1").setValueAttribute("Flintstone");
        page.<HtmlHiddenInput>getHtmlElementById("hidden1").setValueAttribute("Flintstone");
        page.<HtmlPasswordInput>getHtmlElementById("password1").setValueAttribute("Flintstone");
        page.<HtmlIsIndex>getHtmlElementById("isindex1").setValue("Flintstone");

        // Check to make sure they did get changed
        assertEquals("bar", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertTrue(form.<HtmlCheckBoxInput>getInputByName("checkBox").isChecked());
        assertFalse(page.<HtmlOption>getHtmlElementById("option1").isSelected());
        assertTrue(page.<HtmlOption>getHtmlElementById("option2").isSelected());
        assertEquals("Flintstone", page.<HtmlTextArea>getHtmlElementById("textarea1").getText());
        assertEquals("Flintstone", page.<HtmlTextInput>getHtmlElementById("textfield1").getValueAttribute());
        assertEquals("Flintstone", page.<HtmlHiddenInput>getHtmlElementById("hidden1").getValueAttribute());
        assertEquals("Flintstone", page.<HtmlIsIndex>getHtmlElementById("isindex1").getValue());

        final HtmlPage secondPage = (HtmlPage) resetInput.click();
        assertSame(page, secondPage);

        // Check to make sure all the values have been set back to their original values.
        assertEquals("foo", form.getCheckedRadioButton("radioButton").getValueAttribute());
        assertFalse(form.<HtmlCheckBoxInput>getInputByName("checkBox").isChecked());
        assertTrue(page.<HtmlOption>getHtmlElementById("option1").isSelected());
        assertFalse(page.<HtmlOption>getHtmlElementById("option2").isSelected());
        assertEquals("Foobar", page.<HtmlTextArea>getHtmlElementById("textarea1").getText());
        assertEquals("foo", page.<HtmlTextInput>getHtmlElementById("textfield1").getValueAttribute());
        assertEquals("foo", page.<HtmlHiddenInput>getHtmlElementById("hidden1").getValueAttribute());
        assertEquals("foo", page.<HtmlPasswordInput>getHtmlElementById("password1").getValueAttribute());
        assertEquals("", page.<HtmlIsIndex>getHtmlElementById("isindex1").getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testButtonTypeSubmit() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post' onSubmit='alert(\"bar\")' onReset='alert(\"reset\")'>\n"
            + "    <button type='submit' name='button' id='button' value='foo'"
            + "    >Push me</button>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();

        final String[] expectedAlerts = {"bar"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertNotSame(page, secondPage);
        final List<NameValuePair> expectedParameters = Arrays.asList(new NameValuePair[]{
            new NameValuePair("button", "foo")
        });
        final List<NameValuePair> collectedParameters = getMockConnection(secondPage).getLastParameters();

        assertEquals(expectedParameters, collectedParameters);
    }

    /**
     * According to the HTML spec, the default type for a button is "submit".
     * IE is different than the HTML spec and has a default type of "button".
     * @throws Exception if the test fails
     */
    @Test
    public void testDefaultButtonType_StandardsCompliantBrowser() throws Exception {
        final String expectedType = getBrowserVersion().isFirefox() ? "submit" : "button";
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form id='form1' action='" + URL_SECOND + "' method='post'>\n"
            + "    <button name='button' id='button' value='pushme'>PushMe</button>\n"
            + "</form></body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body'></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlButton button = page.getHtmlElementById("button");
        assertEquals(expectedType, button.getTypeAttribute());

        final HtmlPage page2 = button.click();
        final List<NameValuePair> expectedParameters;
        final String expectedSecondPageTitle;
        if ("submit".equals(expectedType)) {
            expectedParameters = Collections.singletonList(new NameValuePair("button", "pushme"));
            expectedSecondPageTitle = "Second";
        }
        else {
            expectedParameters = Collections.emptyList();
            expectedSecondPageTitle = "First";
        }
        assertEquals(expectedParameters, webConnection.getLastParameters());
        assertEquals(expectedSecondPageTitle, page2.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<button id='myId'/>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object HTMLButtonElement]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_3, html, collectedAlerts);
        assertTrue(HtmlButton.class.isInstance(page.getHtmlElementById("myId")));
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
