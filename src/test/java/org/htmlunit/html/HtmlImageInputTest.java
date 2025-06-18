/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Tests for {@link HtmlImageInput}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlImageInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§?button.x=0&button.y=0",
            CHROME = "§§URL§§?button.x=16&button.y=8",
            EDGE = "§§URL§§?button.x=16&button.y=8")
    @HtmlUnitNYI(CHROME = "§§URL§§?button.x=0&button.y=0",
            EDGE = "§§URL§§?button.x=0&button.y=0")
    public void click_NoPosition() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='image' name='aButton' value='foo'/>\n"
            + "  <input type='image' name='button' value='foo'/>\n"
            + "  <input type='image' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";
        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.name("button")).click();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], webDriver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§?button.x=0&button.y=0",
            CHROME = "§§URL§§?button.x=28&button.y=8",
            EDGE = "§§URL§§?button.x=28&button.y=8")
    @HtmlUnitNYI(CHROME = "§§URL§§?button.x=0&button.y=0",
            EDGE = "§§URL§§?button.x=0&button.y=0")
    public void click_NoPosition_NoValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='image' name='button'>\n"
            + "</form></body></html>";
        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.name("button")).click();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], webDriver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§?button.x=0&button.y=0",
            CHROME = "§§URL§§?button.x=18&button.y=12",
            EDGE = "§§URL§§?button.x=18&button.y=12")
    @HtmlUnitNYI(CHROME = "§§URL§§?button.x=0&button.y=0",
            EDGE = "§§URL§§?button.x=0&button.y=0")
    public void click_Position() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='image' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final WebDriver webDriver = loadPage2(html);

        final WebElement elem = webDriver.findElement(By.name("button"));
        final Actions actions = new Actions(webDriver);
        actions.moveToElement(elem).moveByOffset(1, 4).click().perform();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], webDriver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValues() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('image1');\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'image';\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"image\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='image1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"--null", "--null", "--null"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('image1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'image';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"image\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='image1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "initial-initial-initial",
                "newValue-newValue-newValue", "newValue-newValue-newValue",
                "newDefault-newDefault-newDefault", "newDefault-newDefault-newDefault"})
    public void resetByClick() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var image = document.getElementById('testId');\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    image.value = 'newValue';\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    image.defaultValue = 'newDefault';\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "initial-initial-initial",
                "newValue-newValue-newValue", "newValue-newValue-newValue",
                "newDefault-newDefault-newDefault", "newDefault-newDefault-newDefault"})
    public void resetByJS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var image = document.getElementById('testId');\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    image.value = 'newValue';\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    image.defaultValue = 'newDefault';\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    document.forms[0].reset;\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial-initial", "default-default-default",
                "newValue-newValue-newValue", "attribValue-attribValue-attribValue",
                "newDefault-newDefault-newDefault"})
    public void value() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var image = document.getElementById('testId');\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    image.defaultValue = 'default';\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    image.value = 'newValue';\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    image.setAttribute('value', 'attribValue');\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"

            + "    image.defaultValue = 'newDefault';\n"
            + "    log(image.value + '-' + image.defaultValue + '-' + image.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "textLength not available",
            FF = "7",
            FF_ESR = "7")
    public void textLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    if(text.textLength) {\n"
            + "      log(text.textLength);\n"
            + "    } else {\n"
            + "      log('textLength not available');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§?imageInput.x=0&imageInput.y=0")
    public void javascriptClick() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>foo</title>\n"
                + "</head><body>\n"
                + "<form>\n"
                + "  <input type='image' name='imageInput'>\n"
                + "  <input type='button' id='submit' value='submit' "
                        + "onclick='document.getElementsByName(\"imageInput\")[0].click()'>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("submit")).click();

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], webDriver.getCurrentUrl());
    }

    /**
     * Test for bug #646.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void clickFiresOnMouseDown() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body><input type='image' src='x.png' id='i' onmousedown='log(1)'></body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("i")).click();

        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * Test for bug #646.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void clickFiresOnMouseUp() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head>\n"
                + "<body><input type='image' src='x.png' id='i' onmouseup='log(1)'></body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("i")).click();

        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void outsideForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<input id='myInput' type='image' src='test.png' onclick='log(1)'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("myInput")).click();

        verifyTitle2(webDriver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§abcd/img.gif", "2"})
    @HtmlUnitNYI(CHROME = {"§§URL§§abcd/img.gif", "1"},
            EDGE = {"§§URL§§abcd/img.gif", "1"},
            FF = {"§§URL§§abcd/img.gif", "1"},
            FF_ESR = {"§§URL§§abcd/img.gif", "1"})
    public void resolveImage() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_SECOND, "abcd/img.gif");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok",
                    MimeType.IMAGE_GIF, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' type='image' src='" + URL_SECOND + "abcd/img.gif'>\n"
            + "</body>\n"
            + "</html>";

        final int startCount = getMockWebConnection().getRequestCount();
        final int expectedRequestCount = Integer.parseInt(getExpectedAlerts()[1]);

        expandExpectedAlertsVariables(URL_SECOND);
        setExpectedAlerts(getExpectedAlerts()[0]);
        loadPageVerifyTitle2(html);

        assertEquals(expectedRequestCount, getMockWebConnection().getRequestCount() - startCount);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§abcd/img.gif", "2"})
    @HtmlUnitNYI(CHROME = {"§§URL§§abcd/img.gif", "1"},
            EDGE = {"§§URL§§abcd/img.gif", "1"},
            FF = {"§§URL§§abcd/img.gif", "1"},
            FF_ESR = {"§§URL§§abcd/img.gif", "1"})
    public void resolveImageRelative() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "abcd/img.gif");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok",
                    MimeType.IMAGE_GIF, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' type='image' src='abcd/img.gif'>\n"
            + "</body>\n"
            + "</html>";

        final int startCount = getMockWebConnection().getRequestCount();
        final int expectedRequestCount = Integer.parseInt(getExpectedAlerts()[1]);

        expandExpectedAlertsVariables(URL_FIRST);
        setExpectedAlerts(getExpectedAlerts()[0]);
        loadPageVerifyTitle2(html);

        assertEquals(expectedRequestCount, getMockWebConnection().getRequestCount() - startCount);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§", "1"})
    public void resolveImageEmptySource() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_SECOND, "abcd/img.gif");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok",
                    MimeType.IMAGE_GIF, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' type='image' src=''>\n"
            + "</body>\n"
            + "</html>";

        final int startCount = getMockWebConnection().getRequestCount();
        final int expectedRequestCount = Integer.parseInt(getExpectedAlerts()[1]);

        expandExpectedAlertsVariables(URL_FIRST);
        setExpectedAlerts(getExpectedAlerts()[0]);
        loadPageVerifyTitle2(html);

        assertEquals(expectedRequestCount, getMockWebConnection().getRequestCount() - startCount);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "1"})
    public void resolveImageNoSource() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_SECOND, "abcd/img.gif");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok",
                    MimeType.IMAGE_GIF, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' type='image'>\n"
            + "</body>\n"
            + "</html>";

        final int startCount = getMockWebConnection().getRequestCount();
        final int expectedRequestCount = Integer.parseInt(getExpectedAlerts()[1]);

        expandExpectedAlertsVariables(URL_FIRST);
        setExpectedAlerts(getExpectedAlerts()[0]);
        loadPageVerifyTitle2(html);

        assertEquals(expectedRequestCount, getMockWebConnection().getRequestCount() - startCount);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§abcd/img.gif")
    public void lineBreaksInUrl() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_SECOND, "abcd/img.gif");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok",
                    MimeType.IMAGE_GIF, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' type='image' src='" + URL_SECOND + "a\rb\nc\r\nd/img.gif'>\n"
            + "</body>\n"
            + "</html>";

        expandExpectedAlertsVariables(URL_SECOND);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "§§URL§§abcd/img.gif"})
    public void setSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.src);\n"
            + "    input.src='" + URL_FIRST + "abcd/img.gif';\n"
            + "    log(input.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' type='image'>\n"
            + "</body>\n"
            + "</html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "§§URL§§abcd/img.gif", "1", "2"})
    @HtmlUnitNYI(CHROME = {"", "§§URL§§abcd/img.gif", "1", "1"},
            EDGE = {"", "§§URL§§abcd/img.gif", "1", "1"},
            FF = {"", "§§URL§§abcd/img.gif", "1", "1"},
            FF_ESR = {"", "§§URL§§abcd/img.gif", "1", "1"})
    public void resolveImageOnChange() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-gif.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "abcd/img.gif");
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok",
                    MimeType.IMAGE_GIF, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.src);\n"
            + "  }\n"
            + "  function update() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    input.src='" + URL_FIRST + "abcd/img.gif';\n"
            + "    log(input.src);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' type='image'>\n"
            + "  <button id='myUpdate' onclick='update()'>Update</button>\n"
            + "</body>\n"
            + "</html>";

        final int startCount = getMockWebConnection().getRequestCount();
        final int expectedRequestCount = Integer.parseInt(getExpectedAlerts()[2]);
        final int expectedRequestCount2 = Integer.parseInt(getExpectedAlerts()[3]);

        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts()[0]);
        assertEquals(expectedRequestCount, getMockWebConnection().getRequestCount() - startCount);

        driver.findElement(By.id("myUpdate")).click();
        verifyTitle2(driver, getExpectedAlerts()[0], getExpectedAlerts()[1]);

        Thread.sleep(400); // CHROME processes the image async
        assertEquals(expectedRequestCount2, getMockWebConnection().getRequestCount() - startCount);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("--")
    public void minMaxStep() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    log(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "false", "false", "true"},
            CHROME = {"true", "true", "true", "true", "true"},
            EDGE = {"true", "true", "true", "true", "true"})
    public void checkValidity() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity(' ');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('invalid');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('');\n"
            + "    log(foo.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='image' id='foo'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
