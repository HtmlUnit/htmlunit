/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.fail;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlForm}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Jun Chen</a>
 * @author George Murnock
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Philip Graf
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlFormTest extends SimpleWebTestCase {

    /**
     * Tests the good case for setCheckedRatdioButton().
     * @exception Exception If the test fails
     */
    @Test
    public void setSelectedRadioButton_ValueExists() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='radio' name='foo' value='1' selected='selected' id='input1'/>\n"
            + "<input type='radio' name='foo' value='2' id='input2'/>\n"
            + "<input type='radio' name='foo' value='3'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSubmitInput pushButton = form.getInputByName("button");

        form.<HtmlRadioButtonInput>getFirstByXPath(
                "//input[@type='radio' and @name='foo' and @value='2']").setChecked(true);

        assertFalse(page.<HtmlRadioButtonInput>getHtmlElementById("input1").isChecked());
        assertTrue(page.<HtmlRadioButtonInput>getHtmlElementById("input2").isChecked());

        // Test that only one value for the radio button is being passed back to the server
        final HtmlPage secondPage = (HtmlPage) pushButton.click();

        assertEquals("url", URL_FIRST + "?foo=2&button=foo", secondPage.getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
    }

    /**
     * Tests setCheckedRadioButton() with a value that doesn't exist.
     * @exception Exception If the test fails
     */
    @Test
    public void setSelectedRadioButton_ValueDoesNotExist_DoNotForceSelection() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='radio' name='foo' value='1' selected='selected'/>\n"
            + "<input type='radio' name='foo' value='2'/>\n"
            + "<input type='radio' name='foo' value='3'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlInput pushButton = form.getInputByName("button");
        assertNotNull(pushButton);

        assertNull(form.getFirstByXPath("//input[@type='radio' and @name='foo' and @value='4']"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_String() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input id='submitButton' type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlForm form = page.getHtmlElementById("form1");

        // Regression test: this used to blow up
        form.submit((HtmlSubmitInput) page.getHtmlElementById("submitButton"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_ExtraParameters() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "  <input type='text' name='textfield' value='*'/>\n"
            + "  <input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSubmitInput button = form.getInputByName("button");
        button.click();

        final List<NameValuePair> expectedParameters = Arrays.asList(new NameValuePair[]{
            new NameValuePair("textfield", "*"), new NameValuePair("button", "foo")
        });
        final List<NameValuePair> collectedParameters = webConnection.getLastParameters();

        assertEquals(expectedParameters, collectedParameters);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_BadSubmitMethod() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='put'>\n"
            + "  <input type='text' name='textfield' value='*'/>\n"
            + "  <input type='submit' name='button' id='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final MockWebConnection webConnection = getMockConnection(page);
        page.getHtmlElementById("button").click();
        assertSame(HttpMethod.GET, webConnection.getLastMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_onSubmitHandler() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action='" + URL_SECOND + "' onSubmit='alert(\"clicked\")'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setDefaultResponse(secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertEquals("Second", secondPage.getTitleText());

        assertEquals(new String[] {"clicked"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_onSubmitHandler_returnFalse() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action='" + URL_SECOND + "' "
            + "onSubmit='alert(\"clicked\");return false;'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertEquals(firstPage.getTitleText(), secondPage.getTitleText());

        assertEquals(new String[] {"clicked"}, collectedAlerts);
    }

    /**
     * Testcase for 3072010.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clicked")
    public void submit_onSubmitHandler_preventDefaultOnly() throws Exception {
        final String firstHtml
            = "<html><body>\n"
            + "<form method='post' action='/foo' >\n"
            + "<input type='submit' id='button'/>\n"
            + "</form>\n"
            + "<script>\n"
            + "function foo(e) {\n"
            + "  alert('clicked');\n"
            + "  e.returnValue = false;\n"
            + "  if (e.preventDefault) {\n"
            + "    e.preventDefault();\n"
            + "  }\n"
            + "}\n"
            + "var oForm = document.forms[0];\n"
            + "oForm.addEventListener('submit', foo, false);\n"
            + "</script>\n"
            + "</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        getMockWebConnection().setResponse(URL_FIRST, firstHtml);
        getMockWebConnection().setDefaultResponse("");

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertSame(firstPage, secondPage);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Regression test for NullPointerException when submitting forms.
     * @throws Exception if the test fails
     */
    @Test
    public void submit_onSubmitHandler_fails() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action='" + URL_SECOND + "' onSubmit='return null'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setDefaultResponse(secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");
        final HtmlPage secondPage = button.click();
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_onSubmitHandler_javascriptDisabled() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action='" + URL_SECOND + "' onSubmit='alert(\"clicked\")'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        client.getOptions().setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setDefaultResponse(secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertEquals("First", firstPage.getTitleText());
        assertEquals("Second", secondPage.getTitleText());

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_javascriptAction() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action='javascript:alert(\"clicked\")'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertEquals(firstPage.getTitleText(), secondPage.getTitleText());

        assertEquals(new String[] {"clicked"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_javascriptActionMixedCase() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action='jaVAscrIpt:alert(\"clicked\")'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertEquals(firstPage.getTitleText(), secondPage.getTitleText());

        assertEquals(new String[] {"clicked"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_javascriptActionLeadingWhitespace() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action=' javascript:alert(\"clicked\")'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertEquals(firstPage.getTitleText(), secondPage.getTitleText());

        assertEquals(new String[] {"clicked"}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_javascriptAction_javascriptDisabled() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action='javascript:alert(\"clicked\")'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        client.getOptions().setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, html);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertSame(firstPage, secondPage);
    }

    /**
     * Regression test for a bug that caused a NullPointer exception to be thrown during submit.
     * @throws Exception if the test fails
     */
    @Test
    public void submitRadioButton() throws Exception {
        final String html
            = "<html><body><form method='POST' action='" + URL_FIRST + "'>\n"
            + "<table><tr> <td ><input type='radio' name='name1' value='foo'> "
            + "Option 1</td> </tr>\n"
            + "<tr> <td ><input type='radio' name='name1' value='bar' checked >\n"
            + "Option 2</td> </tr>\n"
            + "<tr> <td ><input type='radio' name='name1' value='baz'> Option 3</td> </tr>\n"
            + "</table><input type='submit' value='Login' name='loginButton1'></form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlSubmitInput loginButton
            = page.getDocumentElement().getOneHtmlElementByAttribute("input", "value", "Login");
        loginButton.click();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reset_onResetHandler() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='get' action='" + URL_SECOND + "' "
            + "onReset='alert(\"clicked\");alert(event.type)'>\n"
            + "<input name='button' type='reset' value='PushMe' id='button'/></form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();

        final HtmlPage firstPage = loadPage(html, collectedAlerts);
        final HtmlResetInput button = (HtmlResetInput) firstPage.getHtmlElementById("button");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = button.click();
        assertSame(firstPage, secondPage);

        final String[] expectedAlerts = {"clicked", "reset"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * <p>Simulate a bug report where an anchor contained JavaScript that caused a form submit.
     * According to the bug report, the form would be submitted even though the onsubmit
     * handler would return false. This wasn't reproducible but I added a test for it anyway.</p>
     *
     * <p>UPDATE: If the form submit is triggered by JavaScript then the onsubmit handler is not
     * supposed to be called so it doesn't matter what value it returns.</p>
     * @throws Exception if the test fails
     */
    @Test
    public void submit_AnchorCausesSubmit_onSubmitHandler_returnFalse() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head>\n"
            + "<script>function doalert(message){alert(message);}</script>\n"
            + "<body><form name='form1' method='get' action='" + URL_SECOND + "' "
            + "onSubmit='doalert(\"clicked\");return false;'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "<a id='link1' href='javascript:document.form1.submit()'>Click me</a>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setDefaultResponse(secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlAnchor anchor = firstPage.getHtmlElementById("link1");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        final HtmlPage secondPage = anchor.click();
        assertEquals("Second", secondPage.getTitleText());

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_CheckboxClicked() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<script language='javascript'>\n"
            + "function setFormat() {\n"
            + "  if (document.form1.Format.checked) {\n"
            + "    document.form1.Format.value = 'html';\n"
            + "  } else {\n"
            + "    document.form1.Format.value = 'plain';\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form name='form1' id='form1' method='post'>\n"
            + "  <input type=checkbox name=Format value='' onclick='setFormat()'>\n"
            + "  <input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final HtmlPage page1 = loadPage(html);
        final MockWebConnection webConnection1 = getMockConnection(page1);
        final HtmlForm form1 = page1.getHtmlElementById("form1");
        final HtmlSubmitInput button1 = form1.getInputByName("button");

        final HtmlPage page2 = button1.click();
        final List<NameValuePair> collectedParameters1 = webConnection1.getLastParameters();
        final List<NameValuePair> expectedParameters1 =
            Arrays.asList(new NameValuePair[] {new NameValuePair("button", "foo")});

        final MockWebConnection webConnection2 = getMockConnection(page2);
        final HtmlForm form2 = page2.getHtmlElementById("form1");
        final HtmlCheckBoxInput checkBox2 = form2.getInputByName("Format");
        final HtmlSubmitInput button2 = form2.getInputByName("button");

        checkBox2.click();
        button2.click();
        final List<NameValuePair> collectedParameters2 = webConnection2.getLastParameters();
        final List<NameValuePair> expectedParameters2 = Arrays.asList(new NameValuePair[] {
            new NameValuePair("Format", "html"),
            new NameValuePair("button", "foo")
        });

        assertEquals(expectedParameters1, collectedParameters1);
        assertEquals(expectedParameters2, collectedParameters2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getInputByValue() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='button' value='xxx'/>\n"
            + "  <input type='text' name='textfield' value='foo'/>\n"
            + "  <input type='submit' name='button1' value='foo'/>\n"
            + "  <input type='reset' name='button2' value='foo'/>\n"
            + "  <input type='submit' name='button' value='bar'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlForm form = page.getHtmlElementById("form1");

        final List<String> actualInputs = new ArrayList<>();
        for (final HtmlInput input : form.getInputsByValue("foo")) {
            actualInputs.add(input.getNameAttribute());
        }

        final String[] expectedInputs = {"textfield", "button1", "button2"};
        assertEquals("Get all", expectedInputs, actualInputs);
        assertEquals(Collections.EMPTY_LIST, form.getInputsByValue("none-matching"));

        assertEquals("Get first", "button", form.getInputByValue("bar").getNameAttribute());
        try {
            form.getInputByValue("none-matching");
            fail("Expected ElementNotFoundException");
        }
        catch (final ElementNotFoundException e) {
            // Expected path.
        }
    }

    /**
     * Test that {@link HtmlForm#getTextAreaByName(String)} returns
     * the first textarea with the given name.
     *
     * @throws Exception if the test page can't be loaded
     */
    @Test
    public void getTextAreaByName() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <textarea id='ta1_1' name='ta1'>hello</textarea>\n"
            + "  <textarea id='ta1_2' name='ta1'>world</textarea>\n"
            + "  <textarea id='ta2_1' name='ta2'>!</textarea>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlForm form = page.getHtmlElementById("form1");

        assertEquals("First textarea with name 'ta1'", page.getElementById("ta1_1"),
            form.getTextAreaByName("ta1"));
        assertEquals("First textarea with name 'ta2'", page.getElementById("ta2_1"),
            form.getTextAreaByName("ta2"));

        try {
            form.getTextAreaByName("ta3");
            fail("Expected ElementNotFoundException as there is no textarea with name 'ta3'");
        }
        catch (final ElementNotFoundException e) {
            // pass: exception is expected
        }
    }

    /**
     * Test that {@link HtmlForm#getButtonByName(String)} returns
     * the first button with the given name.
     *
     * @throws Exception if the test page can't be loaded
     */
    @Test
    public void getButtonByName() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <button id='b1_1' name='b1' value='hello' type='button'/>\n"
            + "  <button id='b1_2' name='b1' value='world' type='button'/>\n"
            + "  <button id='b2_1' name='b2' value='!' type='button'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlForm form = page.getHtmlElementById("form1");

        assertEquals("First button with name 'b1'", page.getElementById("b1_1"),
            form.getButtonByName("b1"));
        assertEquals("First button with name 'b2'", page.getElementById("b2_1"),
            form.getButtonByName("b2"));

        try {
            form.getTextAreaByName("b3");
            fail("Expected ElementNotFoundException as there is no button with name 'b3'");
        }
        catch (final ElementNotFoundException e) {
            // pass: exception is expected
        }
    }

    /**
     * Tests that the result of the form will get loaded into the window specified by "target".
     * @throws Exception if the test fails
     */
    @Test
    public void submitToTargetWindow() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<form id='form1' target='window2' action='" + URL_SECOND + "' method='post'>\n"
            + "  <input type='submit' name='button' value='push me'/>\n"
            + "</form></body></html>";
        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponseAsGenericHtml(URL_SECOND, "second");

        final HtmlPage page = client.getPage(URL_FIRST);
        final WebWindow firstWindow = client.getCurrentWindow();
        assertEquals("first window name", "", firstWindow.getName());
        assertSame(page, firstWindow.getEnclosedPage());

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSubmitInput button = form.getInputByName("button");
        final HtmlPage secondPage = button.click();
        assertEquals("window2", secondPage.getEnclosingWindow().getName());
        assertSame(secondPage.getEnclosingWindow(), client.getCurrentWindow());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_SelectHasNoOptions() throws Exception {
        final String html
            = "<html><body><form name='form' method='GET' action='action.html'>\n"
            + "  <select name='select'>\n"
            + "  </select>\n"
            + "  <input type='submit' id='clickMe'>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlPage secondPage = page.getHtmlElementById("clickMe").click();

        assertNotNull(secondPage);
        assertEquals("parameters", Collections.EMPTY_LIST, webConnection.getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit_SelectOptionWithoutValueAttribute() throws Exception {
        final String html
            = "<html><body><form name='form' action='action.html'>\n"
            + "<select name='select'>\n"
            + "  <option>first value</option>\n"
            + "  <option selected>second value</option>\n"
            + "</select>\n"
            + "<input type='submit' id='mySubmit'>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlPage secondPage = page.getHtmlElementById("mySubmit").click();

        assertNotNull(secondPage);
        assertEquals(page.getUrl() + "action.html?select=second+value", secondPage.getUrl());
    }

    /**
     * At one point this test was failing because deeply nested inputs weren't getting picked up.
     * @throws Exception if the test fails
     */
    @Test
    public void submit_DeepInputs() throws Exception {
        final String html
            = "<html><form method='post' action=''>\n"
            + "<table><tr><td>\n"
            + "<input value='NOT_SUBMITTED' name='data' type='text'/>\n"
            + "</td></tr></table>\n"
            + "<input id='submitButton' name='submit' type='submit'/>\n"
            + "</form></html>";
        final HtmlPage page = loadPage(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlInput submitButton = page.getHtmlElementById("submitButton");
        submitButton.click();

        final List<NameValuePair> collectedParameters = webConnection.getLastParameters();
        final List<NameValuePair> expectedParameters = Arrays.asList(new NameValuePair[] {
            new NameValuePair("data", "NOT_SUBMITTED"),
            new NameValuePair("submit", "Submit Query")
        });
        assertEquals(expectedParameters, collectedParameters);
    }

    /**
     * Test order of submitted parameters matches order of elements in form.
     * @throws Exception if the test fails
     */
    @Test
    public void submit_FormElementOrder() throws Exception {
        final String html
            = "<html><head></head><body><form method='post' action=''>\n"
            + "<input type='submit' name='dispatch' value='Save' id='submitButton'>\n"
            + "<input type='hidden' name='dispatch' value='TAB'>\n"
            + "</form></body></html>";
        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(html);

        final WebRequest request = new WebRequest(URL_FIRST, HttpMethod.POST);

        final HtmlPage page = client.getPage(request);
        final HtmlInput submitButton = page.getHtmlElementById("submitButton");
        submitButton.click();

        final List<NameValuePair> collectedParameters = webConnection.getLastParameters();
        final List<NameValuePair> expectedParameters = Arrays.asList(new NameValuePair[] {
            new NameValuePair("dispatch", "Save"),
            new NameValuePair("dispatch", "TAB"),
        });
        assertEquals(expectedParameters, collectedParameters);
    }

    /**
     * Tests the 'Referer' HTTP header.
     * @throws Exception on test failure
     */
    @Test
    public void submit_refererHeader() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form method='post' action='" + URL_SECOND + "'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput button = firstPage.getHtmlElementById("button");

        button.click();

        final Map<String, String> lastAdditionalHeaders = webConnection.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void urlAfterSubmit()
        throws Exception {
        urlAfterSubmit("get", "foo", "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo");
        // for a get submit, query parameters in action are lost in browsers
        urlAfterSubmit("get", "foo?foo=12", "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo");
        urlAfterSubmit("post", "foo", "foo");
        urlAfterSubmit("post", "foo?foo=12", "foo?foo=12");
        urlAfterSubmit("post", "", "");
        urlAfterSubmit("post", "?a=1&b=2", "?a=1&b=2");
        final URL url = new URL(URL_FIRST + "?a=1&b=2");
        urlAfterSubmit(url, "post", "", url.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo#anchor",
                        "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo#anchor",
                        "foo#anchor",
                        "foo?foo=12#anchor"},
            IE = {"foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo",
                    "foo?textField=foo&nonAscii=Flo%DFfahrt&button=foo",
                    "foo#anchor",
                    "foo?foo=12#anchor"})
    public void urlAfterSubmitWithAnchor() throws Exception {
        urlAfterSubmit("get", "foo#anchor", getExpectedAlerts()[0]);
        urlAfterSubmit("get", "foo?foo=12#anchor", getExpectedAlerts()[1]);
        urlAfterSubmit("post", "foo#anchor", getExpectedAlerts()[2]);
        urlAfterSubmit("post", "foo?foo=12#anchor", getExpectedAlerts()[3]);
    }

    /**
     * Utility for {@link #testUrlAfterSubmit()}
     * @param method the form method to use
     * @param action the form action to use
     * @param expectedUrl the expected URL
     * @throws Exception if the test fails
     */
    private void urlAfterSubmit(final URL url, final String method, final String action,
            final String expectedUrl) throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='" + method + "' action='" + action + "'>\n"
            + "<input type='text' name='textField' value='foo'/>\n"
            + "<input type='text' name='nonAscii' value='Flo\u00DFfahrt'/>\n"
            + "<input id='submitButton' type='submit' name='button' value='foo'/>\n"
            + "<input type='button' name='inputButton' value='foo'/>\n"
            + "<button type='button' name='buttonButton' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null, url);
        final Page page2 = page.getHtmlElementById("submitButton").click();

        assertEquals(expectedUrl, page2.getUrl());
    }

    /**
     * Utility for {@link #testUrlAfterSubmit()}. Calls {@link #urlAfterSubmit(URL, String, String, String)} with
     * the default url.
     * @param method the form method to use
     * @param action the form action to use
     * @param expectedUrlEnd the expected URL
     * @throws Exception if the test fails
     */
    private void urlAfterSubmit(final String method, final String action, final String expectedUrlEnd)
        throws Exception {
        urlAfterSubmit(URL_FIRST, method, action, URL_FIRST + expectedUrlEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submitRequestCharset() throws Exception {
        submitRequestCharset("utf-8", null, null, UTF_8);
        submitRequestCharset(null, "utf-8", null, UTF_8);
        submitRequestCharset("iso-8859-1", null, "utf-8", UTF_8);
        submitRequestCharset("iso-8859-1", null, "utf-8, iso-8859-1", UTF_8);
        submitRequestCharset("utf-8", null, "iso-8859-1 utf-8", ISO_8859_1);
        submitRequestCharset("iso-8859-1", null, "utf-8, iso-8859-1", UTF_8);
    }

    /**
     * Utility for {@link #submitRequestCharset()}
     * @param headerCharset the charset for the content type header if not null
     * @param metaCharset the charset for the meta http-equiv content type tag if not null
     * @param formCharset the charset for the form's accept-charset attribute if not null
     * @param expectedRequestCharset the charset expected for the form submission
     * @throws Exception if the test fails
     */
    private void submitRequestCharset(final String headerCharset,
            final String metaCharset, final String formCharset,
            final Charset expectedRequestCharset) throws Exception {

        final String formAcceptCharset;
        if (formCharset == null) {
            formAcceptCharset = "";
        }
        else {
            formAcceptCharset = " accept-charset='" + formCharset + "'";
        }

        final String metaContentType;
        if (metaCharset == null) {
            metaContentType = "";
        }
        else {
            metaContentType = "<meta http-equiv='Content-Type' content='text/html; charset="
                + metaCharset + "'>\n";
        }

        final String html = "<html><head><title>foo</title>\n"
            + metaContentType
            + "</head><body>\n"
            + "<form name='form1' method='post' action='foo'"
            + formAcceptCharset + ">\n"
            + "<input type='text' name='textField' value='foo'/>\n"
            + "<input type='text' name='nonAscii' value='Flo\u00DFfahrt'/>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final WebClient client = getWebClientWithMockWebConnection();
        final MockWebConnection webConnection = getMockWebConnection();

        String contentType = MimeType.TEXT_HTML;
        if (headerCharset != null) {
            contentType += ";charset=" + headerCharset;
        }
        webConnection.setDefaultResponse(html, 200, "ok", contentType);
        final HtmlPage page = client.getPage(URL_FIRST);

        final String firstPageEncoding = StringUtils.defaultString(metaCharset, headerCharset).toUpperCase(Locale.ROOT);
        assertEquals(firstPageEncoding, page.getCharset().name());

        final HtmlForm form = page.getFormByName("form1");
        form.getInputByName("button").click();

        assertSame(expectedRequestCharset, webConnection.getLastWebRequest().getCharset());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void sumbit_submitInputValue() throws Exception {
        final String html =
            "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "  <input id='myButton' type='submit' name='Save'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage firstPage = loadPage(html);
        final HtmlSubmitInput submitInput = firstPage.getHtmlElementById("myButton");
        final HtmlPage secondPage = submitInput.click();
        assertEquals(URL_SECOND + "?Save=Submit+Query", secondPage.getUrl());
    }

    /**
     * Regression test for bug #536.
     * @throws Exception if the test fails
     */
    @Test
    public void submitWithOnClickThatReturnsFalse() throws Exception {
        final String firstHtml = "<html><head><title>foo</title></head><body>\n"
            + "<form action='" + URL_SECOND + "' method='post'>\n"
            + "  <input type='submit' name='mySubmit' onClick='document.forms[0].submit(); return false;'>\n"
            + "</form></body></html>";

        final String secondHtml = "<html><head><title>foo</title><script>\n"
            + "  Number.prototype.gn = false;\n"
            + "  function test() {\n"
            + "    var v = 0;\n"
            + "    alert(typeof v);\n"
            + "    alert(v.gn);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"number", "false"};
        final List<String> collectedAlerts = new ArrayList<>();
        final WebClient client = getWebClientWithMockWebConnection();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, firstHtml);
        conn.setResponse(URL_SECOND, secondHtml);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlForm form = page.getForms().get(0);
        final HtmlSubmitInput submit = form.getInputByName("mySubmit");
        submit.click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests that submitting a form without parameters does not trail the URL with a question mark (IE only).
     *
     * @throws Exception if the test fails
     */
    @Test
    public void submitURLWithoutParameters() throws Exception {
        final String firstHtml = "<html><head><title>foo</title></head><body>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "  <input type='submit' name='mySubmit' onClick='document.forms[0].submit(); return false;'>\n"
            + "</form></body></html>";

        final String secondHtml = "<html><head><title>second</title></head></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final WebClient client = getWebClientWithMockWebConnection();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, firstHtml);
        conn.setDefaultResponse(secondHtml);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlForm form = page.getForms().get(0);
        final HtmlSubmitInput submit = form.getInputByName("mySubmit");
        final HtmlPage secondPage = submit.click();

        final String expectedURL = URL_SECOND.toString();
        assertEquals(expectedURL, secondPage.getUrl());
    }

    /**
     * @throws Exception if the test page can't be loaded
     */
    @Test
    public void malformedHtml_fieldGetters() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<div>\n"
            + "<form id='form1' method='get' action='foo'>\n"
            + "  <input name='field1' value='val1'/>\n"
            + "  <input name='field2' value='val2'/>\n"
            + "  <input type='radio' name='radio1' value='val2'/>\n"
            + "  <input type='submit' id='submitButton'/>\n"
            + "</div>\n"
            + "  <input name='field3' value='val1'/>\n"
            + "  <input name='field4' value='val2'/>\n"
            + "  <input type='radio' name='radio1' value='val3'/>\n"
            + "  <input type='submit' id='submitButton'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlForm form = page.getForms().get(0);

        assertEquals("val1", form.getInputByName("field3").getValueAttribute());
        assertEquals(2, form.getInputsByName("radio1").size());

        assertEquals(3, form.getInputsByValue("val2").size());
        assertEquals("radio1", form.getInputByValue("val3").getNameAttribute());

        assertEquals(2, form.getRadioButtonsByName("radio1").size());
    }

    /**
     * Regression test for bug #784 (form lost children breakage when there is more than one
     * form element with the same name).
     * @throws Exception if an error occurs
     */
    @Test
    public void malformedHtml_formAndTables() throws Exception {
        final String html
            = "<html><body>\n"
            + "\n"
            + "<table id='table1'>\n"
            + "<tr>\n"
            + "<td>this is table1</td>\n"
            + "\n"
            + "<form name='cb_form' method='post' onSubmit='return formsubmit(\"submit\");'>\n"
            + "<input type='hidden' name='ls' value='0'>\n"
            + "<input type='hidden' name='seller' value='OTTO'>\n"
            + "<input type='hidden' name='getLA' value='true'>\n"
            + "<input type='hidden' name='li_count' value='10'>\n"
            + "<input type='hidden' name='EmailTo' value=''>\n"
            + "\n"
            + "</tr>\n"
            + "</table>\n"
            + "\n"
            + "<table id='table2'>\n"
            + "\n"
            + "<tr>\n"
            + "<td><input type='text' value='' name='OrderNr'></td>\n"
            + "<td><input type='text' value='' name='Size'></td>\n"
            + "<td><input type='text' value='' name='Quantity'></td>\n"
            + "</tr>\n"
            + "\n"
            + "<tr>\n"
            + "<td><input type='text' value='' name='OrderNr'></td>\n"
            + "<td><input type='text' value='' name='Size'></td>\n"
            + "<td><input type='text' value='' name='Quantity'></td>\n"
            + "</tr>\n"
            + "\n"
            + "<tr>\n"
            + "<td><input type='text' value='' name='OrderNr'></td>\n"
            + "<td><input type='text' value='' name='Size'></td>\n"
            + "<td><input type='text' value='' name='Quantity'></td>\n"
            + "</tr>\n"
            + "\n"
            + "</form>\n"
            + "</table>\n"
            + "\n"
            + "<script>\n"
            + "var i = 0;\n"
            + "while (document.cb_form.Quantity[i]) {\n"
            + "document.cb_form.Quantity[i].value = document.cb_form.Quantity[i].value.replace(/[^0-9]/g, '');\n"
            + "if ((document.cb_form.Quantity[i].value.length == 0)) {document.cb_form.Quantity[i].value = '1';}\n"
            + "i++;\n"
            + "}\n"
            + "</script>\n"
            + "\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final List<DomElement> quantities = page.getElementsByName("Quantity");
        assertEquals(3, quantities.size());
        for (final DomElement quantity : quantities) {
            assertEquals("1", quantity.getAttribute("value"));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void urlAfterSubmit2() throws Exception {
        final URL url = new URL(URL_FIRST, "test.html");
        urlAfterSubmit(url, "post", "?hi", url + "?hi");
        urlAfterSubmit(new URL(URL_FIRST, "test.html?there"), "post", "?hi",
                URL_FIRST + "test.html?hi");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_emptyTag() throws Exception {
        final String html =
            "<html><body>\n"
            + "<form></form>\n"
            + "<div>test</div>\n"
            + "</body></html>";

        final String xml =
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
            + "<html>\r\n"
            + "  <head/>\r\n"
            + "  <body>\r\n"
            + "    <form>\r\n"
            + "    </form>\r\n"
            + "    <div>\r\n"
            + "      test\r\n"
            + "    </div>\r\n"
            + "  </body>\r\n"
            + "</html>\r\n";

        final HtmlPage page = loadPage(html);
        assertEquals(xml, page.asXml());
    }
}
