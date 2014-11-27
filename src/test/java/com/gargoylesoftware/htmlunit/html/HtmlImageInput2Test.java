/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlImageInput}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlImageInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§?button.x=0&button.y=0",
            CHROME = "§§URL§§?button.x=11&button.y=9&button=foo",
            IE8 = "§§URL§§?button.x=15&button.y=16",
            IE11 = "§§URL§§?button.x=14&button.y=15")
    @NotYetImplemented({ CHROME, IE })
    public void testClick_NoPosition() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='image' name='aButton' value='foo'/>\n"
            + "<input type='image' name='button' value='foo'/>\n"
            + "<input type='image' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";
        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.name("button")).click();

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], webDriver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§?button.x=0&button.y=0",
            CHROME = "§§URL§§?button.x=24&button.y=9",
            IE8 = "§§URL§§?button.x=15&button.y=16",
            IE11 = "§§URL§§?button.x=14&button.y=15")
    @NotYetImplemented({ CHROME, IE })
    public void testClick_NoPosition_NoValue() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='image' name='button'>\n"
            + "</form></body></html>";
        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.name("button")).click();

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], webDriver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-", "-", "-" })
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('image1');\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'image';\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"image\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='image1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-", "-", "-" })
    public void defaultValuesAfterclone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('image1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'image';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"image\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='image1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault" })
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var image = document.getElementById('testId');\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    image.value = 'newValue';\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    image.defaultValue = 'newDefault';\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault" })
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var image = document.getElementById('testId');\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    image.value = 'newValue';\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    image.defaultValue = 'newDefault';\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "initial-initial", "default-default", "newValue-newValue", "newDefault-newDefault" })
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var image = document.getElementById('testId');\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    image.defaultValue = 'default';\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"

            + "    image.value = 'newValue';\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"
            + "    image.defaultValue = 'newDefault';\n"
            + "    alert(image.value + '-' + image.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='image' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§?imageInput.x=0&imageInput.y=0")
    public void javascriptClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
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
     * Test for bug: http://sourceforge.net/p/htmlunit/bugs/646/.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void testClickFiresOnMouseDown() throws Exception {
        final String html = "<html><body><input type='image' src='x.png' id='i' onmousedown='alert(1)'></body></html>";

        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("i")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(webDriver));
    }

    /**
     * Test for bug: http://sourceforge.net/p/htmlunit/bugs/646/.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void testClickFiresOnMouseUp() throws Exception {
        final String html = "<html><body><input type='image' src='x.png' id='i' onmouseup='alert(1)'></body></html>";
        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("i")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(webDriver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void testOutsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='image' src='test.png' onclick='alert(1)'>\n"
            + "</body></html>";
        final WebDriver webDriver = loadPage2(html);
        webDriver.findElement(By.id("myInput")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(webDriver));
    }
}
