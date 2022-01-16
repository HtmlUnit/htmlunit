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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlButton}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Brad Clarke
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlButton2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLButtonElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<button id='myId'>OK</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlButton.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-undefined", "-undefined", "-"})
    public void defaultValues() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('button1');\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('button');\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"button\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='button1'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-undefined", "-", "-"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('button1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'button';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"button\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='button1'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-undefined", "initial-undefined", "newValue-undefined", "newValue-undefined",
                "newValue-newDefault", "newValue-newDefault"})
    public void resetByClick() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'newDefault';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-undefined", "initial-undefined", "newValue-undefined", "newValue-undefined",
                "newValue-newDefault", "newValue-newDefault"})
    public void resetByJS() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'newDefault';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-undefined", "initial-default", "newValue-default", "newValue-newDefault"})
    public void defaultValue() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'default';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"
            + "    button.defaultValue = 'newDefault';\n"
            + "    log(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-OK", "newValue-OK", "newValue-OK"})
    public void innerHtml() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.value + '-' + button.innerHTML);\n"

            + "    button.value = 'newValue';\n"
            + "    log(button.value + '-' + button.innerHTML);\n"

            + "    button.innerHtml = 'Cancel';\n"
            + "    log(button.value + '-' + button.innerHTML);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-OK", "newValue-OK", "newValue-Cancel"})
    public void innerText() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.value + '-' + button.innerText);\n"

            + "    button.value = 'newValue';\n"
            + "    log(button.value + '-' + button.innerText);\n"

            + "    button.innerText = 'Cancel';\n"
            + "    log(button.value + '-' + button.innerText);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-OK", "newValue-newValue-OK", "newValue-newValue-OK"})
    public void valueAttributeNode() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    var attr = button.getAttributeNode('value');\n"
            + "    log(attr.value + '-' + button.value + '-' + button.innerHTML);\n"

            + "    attr.value = 'newValue';\n"
            + "    log(attr.value + '-' + button.value + '-' + button.innerHTML);\n"

            + "    button.innerHtml = 'Cancel';\n"
            + "    log(attr.value + '-' + button.value + '-' + button.innerHTML);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * According to the HTML spec, the default type for a button is "submit".
     * IE is different than the HTML spec and has a default type of "button".
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"submit", "1", "button-pushme", "Second"})
    public void defaultButtonType_StandardsCompliantBrowser() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form id='form1' action='" + URL_SECOND + "' method='post'>\n"
            + "  <button name='button' id='button' value='pushme'>PushMe</button>\n"
            + "</form></body></html>";
        final String secondContent
            = "<html><head><title>Second</title></head><body'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        final WebElement button = driver.findElement(By.id("button"));

        assertEquals(getExpectedAlerts()[0], button.getAttribute("type"));

        button.click();

        final List<NameValuePair> params = getMockWebConnection().getLastParameters();
        assertEquals(getExpectedAlerts()[1], "" + params.size());

        if (params.size() > 0) {
            assertEquals(getExpectedAlerts()[2], params.get(0).getName() + "-" + params.get(0).getValue());
        }
        assertTitle(driver, getExpectedAlerts()[3]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void typeUnknown() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <button type='unknown' id='myButton'>Explicit Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        if (expectedReqCount > 1) {
            assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeSubmit() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <input name='text' type='text'>\n"
            + "    <button type='submit' id='myButton'>Explicit Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals(URL_SECOND.toString() + "?text=", getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeReset() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <button type='reset' id='myButton'>Explicit Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeButton() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <button type='button' id='myButton'>Explicit Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeEmpty() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <button type='button' id='myButton'>Explicit Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void submitWithoutType() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "    <button id='myButton'>Implicit Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        if (expectedReqCount > 1) {
            assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "1")
    public void typeUnknownExternal() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "  </form>\n"
            + "  <button type='unknown' id='myButton' form='myForm'>Explicit Submit</button>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        if (expectedReqCount > 1) {
            assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "1")
    public void typeSubmitExternal() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "  </form>\n"
            + "  <button type='submit' id='myButton' form='myForm'>Explicit Submit</button>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        if (expectedReqCount > 1) {
            assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void typeResetExternal() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "  </form>\n"
            + "  <button type='reset' id='myButton' form='myForm'>Explicit Submit</button>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        if (expectedReqCount > 1) {
            assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "1")
    public void submitWithoutTypeExternal() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "  </form>\n"
            + "  <button id='myButton' form='myForm'>Implicit Submit</button>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        if (expectedReqCount > 1) {
            assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void externalUnknownFrom() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "  </form>\n"
            + "  <button type='submit' id='myButton' form='unknown'>Explicit Submit</button>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        if (expectedReqCount > 1) {
            assertEquals(URL_SECOND.toString(), getMockWebConnection().getLastWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2", "second"},
            IE = {"2", "third"})
    public void externalPreferenceFrom() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm2' action='" + URL_SECOND + "'>\n"
            + "  </form>\n"
            + "  <form id='myForm3' action='" + URL_THIRD + "'>\n"
            + "    <button type='submit' id='myButton' form='myForm2'>Explicit Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>third</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        assertTitle(driver, getExpectedAlerts()[1]);

        shutDownRealIE();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2", "second"},
            IE = {"2", "third"})
    public void internalDifferentFrom() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='myForm' action='" + URL_SECOND + "'>\n"
            + "  </form>\n"
            + "  <form id='other' action='" + URL_THIRD + "'>\n"
            + "    <button type='submit' id='myButton' form='myForm'>Explicit Submit</button>\n"
            + "  </form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";
        final String thirdContent
            = "<html><head><title>third</title></head><body>\n"
            + "  <p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();

        final int expectedReqCount = Integer.parseInt(getExpectedAlerts()[0]);
        assertEquals(expectedReqCount, getMockWebConnection().getRequestCount());
        assertTitle(driver, getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submit")
    public void type() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.type);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submit")
    public void typeStandards() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    log(button.type);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF = "2",
            FF_ESR = "2",
            IE = "2")
    public void onclickDisablesSubmit() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function submitForm() {\n"
            + "      document.deliveryChannelForm.submitBtn.disabled = true;\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form action='test' name='deliveryChannelForm'>\n"
            + "    <button name='submitBtn' type='submit' onclick='submitForm();'>Save</button>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver webDriver = loadPage2(html);
        final WebElement input = webDriver.findElement(By.name("submitBtn"));
        input.click();

        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"foo", "foonewValue", "foonewValue"},
            FF = {"foo", "foonewValue", "foo"},
            FF_ESR = {"foo", "foonewValue", "foo"},
            IE = {"foo", "foonewValue", "foo"})
    public void onclickDisablesReset() throws Exception {
        final String html = "<html><head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function submitForm() {\n"
            + "      document.deliveryChannelForm.resetBtn.disabled = true;\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form action='test' name='deliveryChannelForm'>\n"
            + "    <input type='text' id='textfield' value='foo'/>\n"
            + "    <button name='resetBtn' type='reset' title='Save' onclick='submitForm();'>Save</button>\n"
            + "  </form>"
            + "</script>\n"
            + "</body></html>";

        final WebDriver webDriver = loadPage2(html);

        final WebElement textfield = webDriver.findElement(By.id("textfield"));
        assertEquals(getExpectedAlerts()[0], textfield.getAttribute("value"));
        textfield.sendKeys("newValue");
        assertEquals(getExpectedAlerts()[1], textfield.getAttribute("value"));

        final WebElement reset = webDriver.findElement(By.name("resetBtn"));
        reset.click();
        assertEquals(getExpectedAlerts()[2], textfield.getAttribute("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "false", "true", "true", "false", "false", "true", "true"},
            FF = {"true", "false", "true", "true", "true", "true", "false", "false", "true", "true"},
            FF_ESR = {"true", "false", "true", "true", "true", "true", "false", "false", "true", "true"},
            IE = {"true", "false", "true", "true", "true", "true", "false", "false", "true", "true"})
    public void willValidate() throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('b1').willValidate);\n"
                + "      log(document.getElementById('b2').willValidate);\n"
                + "      log(document.getElementById('b3').willValidate);\n"
                + "      log(document.getElementById('b4').willValidate);\n"
                + "      log(document.getElementById('b5').willValidate);\n"
                + "      log(document.getElementById('b6').willValidate);\n"
                + "      log(document.getElementById('b7').willValidate);\n"
                + "      log(document.getElementById('b8').willValidate);\n"
                + "      log(document.getElementById('b9').willValidate);\n"
                + "      log(document.getElementById('b10').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <button id='b1'>b1</button>\n"
                + "    <button id='b2' disabled>b2</button>\n"
                + "    <button id='b3' hidden>b3</button>\n"
                + "    <button id='b4' readonly>b4</button>\n"
                + "    <button id='b5' style='display: none'>b5</button>\n"
                + "    <button id='b6' type='submit'>b6</button>\n"
                + "    <button id='b7' type='reset'>b7</button>\n"
                + "    <button id='b8' type='button'>b8</button>\n"
                + "    <button id='b9' type='unknown'>b9</button>\n"
                + "    <button id='b10' type=''>b10</button>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationEmpty() throws Exception {
        validation("<button id='b1'>b1</button>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationEmpty_Submit() throws Exception {
        validation("<button id='b1' type='submit'>b1</button>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "false"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "false"})
    public void validationEmpty_Reset() throws Exception {
        validation("<button id='b1' type='reset'>b1</button>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationCustomValidity() throws Exception {
        validation("<button id='b1'>b1</button>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationCustomValidity_Submit() throws Exception {
        validation("<button id='b1' type='submit'>b1</button>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "false"},
            IE = {"true",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "false"})
    public void validationCustomValidity_Reset() throws Exception {
        validation("<button id='b1' type='reset'>b1</button>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<button id='b1'>b1</button>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationBlankCustomValidity_Submit() throws Exception {
        validation("<button id='b1' type='submit'>b1</button>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "false"},
            IE = {"true",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "false"})
    public void validationBlankCustomValidity_Reset() throws Exception {
        validation("<button id='b1' type='reset'>b1</button>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<button id='b1'>b1</button>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationResetCustomValidity_Submit() throws Exception {
        validation("<button id='b1' type='submit'>b1</button>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "false"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "false"})
    public void validationResetCustomValidity_Reset() throws Exception {
        validation("<button id='b1' type='reset'>b1</button>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    private void validation(final String htmlPart, final String jsPart) throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function logValidityState(s) {\n"
                + "      log(s.badInput"
                        + "+ '-' + s.customError"
                        + "+ '-' + s.patternMismatch"
                        + "+ '-' + s.rangeOverflow"
                        + "+ '-' + s.rangeUnderflow"
                        + "+ '-' + s.stepMismatch"
                        + "+ '-' + s.tooLong"
                        + "+ '-' + s.tooShort"
                        + " + '-' + s.typeMismatch"
                        + " + '-' + s.valid"
                        + " + '-' + s.valueMissing);\n"
                + "    }\n"
                + "    function test() {\n"
                + "      var elem = document.getElementById('b1');\n"
                + jsPart
                + "      log(elem.checkValidity());\n"
                + "      logValidityState(elem.validity);\n"
                + "      log(elem.willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + htmlPart
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
