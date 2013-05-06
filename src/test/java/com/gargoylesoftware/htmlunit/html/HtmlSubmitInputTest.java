/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlSubmitInput}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlSubmitInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submit() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='submit' name='aButton' value='foo'/>\n"
            + "<input type='suBMit' name='button' value='foo'/>\n"
            + "<input type='submit' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";

        final WebDriver wd = loadPageWithAlerts2(html);

        final WebElement button = wd.findElement(By.name("button"));
        button.click();

        assertEquals("foo", wd.getTitle());

        assertEquals(Collections.singletonList(new NameValuePair("button", "foo")),
            getMockWebConnection().getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "foo", "bar" })
    public void click_onClick() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\"); return false;'>\n"
            + "    <input type='submit' name='button' value='foo' onClick='alert(\"foo\")'/>\n"
            + "</form></body></html>";

        final WebDriver wd = loadPage2(html);

        final WebElement button = wd.findElement(By.name("button"));
        button.click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(wd));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_onClick_JavascriptReturnsTrue() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1' method='get' action='foo.html'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button1'"
            + "onclick='return true'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "foo.html"), secondHtml);

        final WebDriver wd = loadPageWithAlerts2(html);

        final WebElement button = wd.findElement(By.id("button1"));
        button.click();

        assertEquals("Second", wd.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void outsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='submit' onclick='alert(1)'>\n"
            + "</body></html>";

        final WebDriver wd = loadPage2(html);
        final WebElement input = wd.findElement(By.id("myInput"));
        input.click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(wd));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void doubleSubmission() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function submitForm() {\n"
            + "      document.deliveryChannelForm.submitBtn.disabled = true;\n"
            + "      document.deliveryChannelForm.submit();\n"
            + "    }\n"
            + "  </script>"
            + "</head>\n"
            + "<body>\n"
            + "  <form action='test' name='deliveryChannelForm'>\n"
            + "    <input name='submitBtn' type='submit' value='Save' title='Save' onclick='submitForm();'>\n"
            + "  </form>"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver wd = loadPageWithAlerts2(html);
        final WebElement input = wd.findElement(By.name("submitBtn"));
        input.click();

        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void doubleSubmissionWithRedirect() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "function submitForm(btn) {\n"
            + "  btn.form.submit();\n"
            + "  btn.disabled = true;\n"
            + "}\n"
            + "</script>"
            + "<form method='post' action='test'>\n"
            + "  <input name='text' type='text'>\n"
            + "  <input name='btn' type='submit' onclick='submitForm(this);'>\n"
            + "</form>"
            + "</body></html>";

        final MockWebConnection mockWebConnection = getMockWebConnection();

        final List<NameValuePair> redirectHeaders = Arrays.asList(new NameValuePair("Location", "/nextPage"));
        mockWebConnection.setResponse(new URL(getDefaultUrl() + "test"), "", 302, "Found", null, redirectHeaders);

        mockWebConnection.setResponse(new URL(getDefaultUrl() + "nextPage"),
            "<html><head><title>next page</title></head></html>");

        final WebDriver wd = loadPageWithAlerts2(html);
        final WebElement input = wd.findElement(By.name("btn"));
        input.click();

        assertEquals("next page", wd.getTitle());
        assertEquals(3, mockWebConnection.getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-", "-", "-" },
            IE = { "Submit Query-", "Submit Query-", "Submit Query-" })
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('submit1');\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'submit';\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"submit\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='submit1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-", "-", "-" },
            IE = { "Submit Query-", "Submit Query-", "Submit Query-" })
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('submit1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'submit';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"submit\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='submit1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault" },
            IE = { "initial-initial", "initial-initial", "newValue-initial", "newValue-initial",
                "newValue-newDefault", "newValue-newDefault" })
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var submit = document.getElementById('testId');\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.value = 'newValue';\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.defaultValue = 'newDefault';\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault" },
            IE = { "initial-initial", "initial-initial", "newValue-initial", "newValue-initial",
                "newValue-newDefault", "newValue-newDefault" })
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var submit = document.getElementById('testId');\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.value = 'newValue';\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.defaultValue = 'newDefault';\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-initial", "default-default", "newValue-newValue", "newdefault-newdefault" },
            IE8 = { "initial-initial", "initial-default", "newValue-default", "newValue-newdefault" })
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var submit = document.getElementById('testId');\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.defaultValue = 'default';\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"

            + "    submit.value = 'newValue';\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"
            + "    submit.defaultValue = 'newdefault';\n"
            + "    alert(submit.value + '-' + submit.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='submit' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
