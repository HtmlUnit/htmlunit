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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
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
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<button id='myId'>OK</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
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
}
