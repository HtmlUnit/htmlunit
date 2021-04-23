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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlInput}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public final class HtmlInput2Test extends WebDriverTestCase {
    private static final String TEST_ID = "clickId";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "error", "handler", "null", "error"})
    public void onchangeDirectCall() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function handler() { alert('handler');}\n"
            + "  function test() {\n"
            + "    var elem = document.getElementById('myInput');\n"
            + "    try {\n"
            + "      alert(elem.onchange);\n"
            + "      elem.onchange();\n"
            + "      alert('onchange called');\n"
            + "    } catch (e) {alert('error')}\n"

            + "    elem.onchange = handler;\n"
            + "    elem.onchange();\n"

            + "    elem.onchange = null;\n"
            + "    try {\n"
            + "      alert(elem.onchange);\n"
            + "      elem.onchange();\n"
            + "      alert('onchange called');\n"
            + "    } catch (e) {alert('error')}\n"

            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <input id='myInput'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function handler() {}", "null"})
    @NotYetImplemented
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
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mousedown; onfocus; mouseup; onclick;", ""})
    public void clickButtonEventSequence() throws Exception {
        testClickEventSequence("<input type='button' id='" + TEST_ID + "'>Check", false);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mousedown; onfocus; mouseup; onclick;", ""})
    public void clickImageEventSequence() throws Exception {
        testClickEventSequence("<input type='image' id='" + TEST_ID + "'>Check", true);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"mousedown; onfocus; mouseup; onclick; onchange;", ""},
            IE = {"mousedown; onfocus; mouseup; onchange; onclick;", ""})
    public void clickCheckboxEventSequence() throws Exception {
        testClickEventSequence("<input type='checkbox' id='" + TEST_ID + "'>Check", false);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mousedown; onfocus; mouseup; onclick;", ""})
    public void clickPasswordEventSequence() throws Exception {
        testClickEventSequence("<input type='password' id='" + TEST_ID + "'>Check", false);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"mousedown; onfocus; mouseup; onclick; onchange;", ""},
            IE = {"mousedown; onfocus; mouseup; onchange; onclick;", ""})
    public void clickRadioEventSequence() throws Exception {
        testClickEventSequence("<input type='radio' name='test' id='" + TEST_ID + "'>Check", false);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mousedown; onfocus; mouseup; onclick;", ""})
    public void clickResetEventSequence() throws Exception {
        testClickEventSequence("<input type='reset' id='" + TEST_ID + "'>Check", true);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mousedown; onfocus; mouseup; onclick;", ""})
    public void clickTextEventSequence() throws Exception {
        testClickEventSequence("<input type='text' id='" + TEST_ID + "'>Check", false);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mousedown; onfocus; mouseup; onchange; onclick;", ""})
    @BuggyWebDriver(CHROME = {"onfocus; onchange; mouseup; onclick;", ""},
                    EDGE = {"onfocus; onchange; mouseup; onclick;", ""},
                    FF = {"mousedown; onfocus; onchange; mouseup; onclick;", ""},
                    FF78 = {"mousedown; onfocus; onchange; mouseup; onclick;", ""},
                    IE = {"mousedown; onchange; mouseup; onclick; onfocus;", ""})
    public void clickOptionEventSequence() throws Exception {
        testClickEventSequence("<select size='2'><option id='" + TEST_ID + "'>1</option></select>", false);
    }

    private void testClickEventSequence(final String input, final boolean onClickRetFalse) throws Exception {
        final String events = " onmousedown=\"log('mousedown')\" onmouseup=\"log('mouseup')\" "
                + "onfocus=\"log('onfocus')\" onchange=\"log('onchange')\" "
                + "onclick=\"log('onclick')\"";
        String tag = StringUtils.replaceOnce(input, ">", events + ">");
        if (onClickRetFalse) {
            tag = StringUtils.replaceOnce(tag, "onclick')", "onclick');return false;");
        }

        final String html = "<html><head>\n"
                + "<script>\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log_').value += x + '; ';\n"
                + "  }\n"
                + "</script>\n"

                + "</head>\n<body>\n"
                + "<form>\n"
                + "  <textarea id='log_' rows='4' cols='50'></textarea>\n"
                + tag + "\n"
                + "  <input type='text' id='next'>\n"
                + "</form>\n"

                + "</body></html>";

        final List<String> alerts = new LinkedList<>();

        final WebDriver driver = loadPage2(html);
        final WebElement log = driver.findElement(By.id("log_"));

        driver.findElement(By.id(TEST_ID)).click();
        alerts.add(log.getAttribute("value").trim());

        log.clear();
        driver.findElement(By.id("next")).click();

        alerts.add(log.getAttribute("value").trim());
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("something")
    public void placeholder() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var elem = document.getElementById('myInput');\n"
            + "    alert(elem.placeholder);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <input id='myInput' placeholder='something'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
