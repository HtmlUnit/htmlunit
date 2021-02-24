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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLLabelElement}.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLLabelElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void htmlForNone() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <input type='text' id='text1'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void htmlForEmpty() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1' for=''>Item</label>\n"
            + "    <input type='text' id='text1'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unknown")
    public void htmlForUnknown() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1' for='unknown'>Item</label>\n"
            + "    <input type='text' id='text1'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text1")
    public void htmlForKnown() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1' for='text1'>Item</label>\n"
            + "    <input type='text' id='text1'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"unknown", "null", "null"},
            IE = {"unknown", "undefined", "null"})
    public void htmlForSetToUnknown() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').htmlFor = 'unknown';\n"
            + "        } catch (e) {"
            + "          log('exception');\n"
            + "        }\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "        log(document.getElementById('label1').control);\n"
            + "        log(document.getElementById('label1').form);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <form id='form1'>\n"
            + "      <input type='text' id='text1'>\n"
            + "    </form>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"div1", "null", "null"},
            IE = {"div1", "undefined", "null"})
    public void htmlForSetToNotLabelable() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').htmlFor = 'div1';\n"
            + "        } catch (e) {"
            + "          log('exception');\n"
            + "        }\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "        log(document.getElementById('label1').control);\n"
            + "        log(document.getElementById('label1').form);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <form id='form1'>\n"
            + "      <div id='div1'></div>\n"
            + "    </form>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"text1", "[object HTMLInputElement]", "[object HTMLFormElement]"},
            IE = {"text1", "undefined", "null"})
    public void htmlForSetToLabelable() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').htmlFor = 'text1';\n"
            + "        } catch (e) {"
            + "          log('exception');\n"
            + "        }\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "        log(document.getElementById('label1').control);\n"
            + "        log(document.getElementById('label1').form);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <form id='form1'>\n"
            + "      <input type='text' id='text1'>\n"
            + "    </form>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlNone() throws Exception {
        final String html =
              "  <label id='label1'>Item</label>\n"
            + "  <input type='text' id='text1'>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlForEmpty() throws Exception {
        final String html =
              "  <label id='label1' for=''>Item</label>\n"
            + "  <input type='text' id='text1'>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlForUnknown() throws Exception {
        final String html =
              "  <label id='label1' for='unknown'>Item</label>\n"
            + "  <input type='text' id='text1'>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlForNotLabelable() throws Exception {
        final String html =
              "  <label id='label1' for='div1'>Item</label>\n"
            + "  <div id='div1'></div>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLButtonElement]:button1",
            IE = "undefined")
    public void controlForButton() throws Exception {
        final String html =
              "  <label id='label1' for='button1'>Item</label>\n"
            + "  <button id='button1'></button>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement]:text1",
            IE = "undefined")
    public void controlForInput() throws Exception {
        final String html =
              "  <label id='label1' for='text1'>Item</label>\n"
            + "  <input type='text' id='text1'>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlForInputHidden() throws Exception {
        final String html =
              "  <label id='label1' for='hidden1'>Item</label>\n"
            + "  <input type='hidden' id='hidden1'>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMeterElement]:meter1",
            IE = "undefined")
    public void controlForMeter() throws Exception {
        final String html =
              "  <label id='label1' for='meter1'>Item</label>\n"
            + "  <meter id='meter1'></meter>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLOutputElement]:output1",
            IE = "undefined")
    public void controlForOutput() throws Exception {
        final String html =
              "  <label id='label1' for='output1'>Item</label>\n"
            + "  <output id='output1'></output>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLProgressElement]:progress1",
            IE = "undefined")
    public void controlForProgress() throws Exception {
        final String html =
              "  <label id='label1' for='progress1'>Item</label>\n"
            + "  <progress id='progress1'></progress>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLSelectElement]:select1",
            IE = "undefined")
    public void controlForSelect() throws Exception {
        final String html =
              "  <label id='label1' for='select1'>Item</label>\n"
            + "  <select id='select1'><option>Option</option></select>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTextAreaElement]:text1",
            IE = "undefined")
    public void controlForTextArea() throws Exception {
        final String html =
              "  <label id='label1' for='text1'>Item</label>\n"
            + "  <textarea id='text1'></textarea>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlNestedNotLabelable() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <div id='div1'></div>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLButtonElement]:button1",
            IE = "undefined")
    public void controlNestedButton() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <button id='button1'></button>\n"
            + "    <button id='button2'></button>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement]:text1",
            IE = "undefined")
    public void controlNestedInput() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "    <input type='text' id='text2'>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlNestedInputHidden() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <input type='hidden' id='hidden1'>\n"
            + "    <input type='hidden' id='hidden2'>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMeterElement]:meter1",
            IE = "undefined")
    public void controlNestedMeter() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <meter id='meter1'></meter>\n"
            + "    <meter id='meter2'></meter>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLOutputElement]:output1",
            IE = "undefined")
    public void controlNestedOutput() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <output id='output1'></output>\n"
            + "    <output id='output2'></output>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLProgressElement]:progress1",
            IE = "undefined")
    public void controlNestedProgress() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <progress id='progress1'></progress>\n"
            + "    <progress id='progress2'></progress>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLSelectElement]:select1",
            IE = "undefined")
    public void controlNestedSelect() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <select id='select1'><option>Option</option></select>\n"
            + "    <select id='select2'><option>Option</option></select>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTextAreaElement]:text1",
            IE = "undefined")
    public void controlNestedTextArea() throws Exception {
        final String html =
              "  <label id='label1'>Item\n"
            + "    <textarea id='text1'></textarea>\n"
            + "    <textarea id='text2'></textarea>\n"
            + "  </label>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement]:text2",
            IE = "undefined")
    public void controlForVersusNested() throws Exception {
        final String html =
              "  <label id='label1' for='text2'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2'>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlForUnknownVersusNested() throws Exception {
        final String html =
              "  <label id='label1' for='unknown'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2'>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void controlForNotLabelableVersusNested() throws Exception {
        final String html =
              "  <label id='label1' for='div1'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <div id='div1'></div>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    private static String generateControlPage(final String snippet) {
        return "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(e) {\n"
            + "        document.title += e + (e ? ':' + e.id : '') + ';';\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='log(document.getElementById(\"label1\").control);'>\n"
            + snippet
            + "  </body>\n"
            + "</html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "[object HTMLInputElement]")
    public void controlSet() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').control = document.getElementById('text1');\n"
            + "        } catch (e) {"
            + "          log('exception');\n"
            + "        }\n"
            + "        log(document.getElementById('label1').control);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <input type='text' id='text1'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void formNoForm() throws Exception {
        final String html
            = "  <label id='label1'>Item</label>\n"
            + "  <input type='text' id='text1'>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void formOutsideForm() throws Exception {
        final String html
            = "  <label id='label1'>Item</label>\n"
            + "  <form id='form1'>\n"
            + "    <input type='text' id='text1'>\n"
            + "  </form>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "[object HTMLFormElement]:form1")
    public void formInsideForm() throws Exception {
        final String html
            = "  <form id='form1'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <input type='text' id='text1'>\n"
            + "  </form>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void formForLabelableOutsideForm() throws Exception {
        final String html
            = "  <label id='label1' for='text1'>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "  <form id='form1'>\n"
            + "    <input type='text' id='text2'>\n"
            + "  </form>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void formForNotLabelableInsideForm() throws Exception {
        final String html
            = "  <label id='label1' for='div1'>Item</label>\n"
            + "  <form id='form1'>\n"
            + "    <div id='div1'></div>\n"
            + "  </form>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFormElement]:form1",
            IE = "null")
    public void formForLabelableInsideForm() throws Exception {
        final String html
            = "  <label id='label1' for='text1'>Item</label>\n"
            + "  <form id='form1'>\n"
            + "    <input type='text' id='text1'>\n"
            + "  </form>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void formNestedLabelableOutsideForm() throws Exception {
        final String html
            = "  <label id='label1'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <form id='form1'>\n"
            + "    <input type='text' id='text2'>\n"
            + "  </form>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "[object HTMLFormElement]:form1")
    public void formNestedNotLabelableInsideForm() throws Exception {
        final String html
            = "  <form id='form1'>\n"
            + "    <label id='label1'>Item\n"
            + "      <div id='div1'></div>\n"
            + "    </label>\n"
            + "  </form>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLFormElement]:form1")
    public void formNestedLabelableInsideForm() throws Exception {
        final String html
            = "  <form id='form1'>\n"
            + "    <label id='label1'>Item\n"
            + "      <input type='text' id='text1'>\n"
            + "    </label>\n"
            + "  </form>\n";

        final WebDriver driver = loadPage2(generateFormPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    private static String generateFormPage(final String snippet) {
        return "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(e) {\n"
            + "        document.title += e + (e ? ':' + e.id : '') + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        log(document.getElementById('label1').form);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + snippet
            + "  </body>\n"
            + "</html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void formSet() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      function doTest() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').form = document.getElementById('form1');\n"
            + "        } catch (e) {"
            + "          log('exception');\n"
            + "        }\n"
            + "        log(document.getElementById('label1').form);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <form id='form1'>\n"
            + "      <input type='text' id='text1'>\n"
            + "    </form>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickAfterHtmlForSetByJS() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function doTest() {\n"
            + "        document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <input type='checkbox' id='checkbox1'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkbox.isSelected());
        driver.findElement(By.id("label1")).click();
        assertTrue(checkbox.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickAfterNestedByJS() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function doTest() {\n"
            + "        document.getElementById('label1').appendChild(document.getElementById('checkbox1'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <input type='checkbox' id='checkbox1'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkbox.isSelected());
        driver.findElement(By.id("label1")).click();
        assertTrue(checkbox.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickByJSAfterHtmlForSetByJS() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function doTest() {\n"
            + "        document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "      }\n"
            + "      function delegateClick() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').click();\n"
            + "        } catch (e) {}\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>My Label</label>\n"
            + "    <input type='checkbox' id='checkbox1'><br>\n"
            + "    <input type=button id='button1' value='Test' onclick='delegateClick()'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkbox.isSelected());
        driver.findElement(By.id("button1")).click();
        assertTrue(checkbox.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickByJSAfterNestedByJS() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function doTest() {\n"
            + "        document.getElementById('label1').appendChild(document.getElementById('checkbox1'));\n"
            + "      }\n"
            + "      function delegateClick() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').click();\n"
            + "        } catch (e) {}\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>My Label</label>\n"
            + "    <input type='checkbox' id='checkbox1'><br>\n"
            + "    <input type=button id='button1' value='Test' onclick='delegateClick()'>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkbox.isSelected());
        driver.findElement(By.id("button1")).click();
        assertTrue(checkbox.isSelected());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "A", "a", "A", "a8", "8Afoo", "8", "@" })
    public void accessKey() throws Exception {
        final String html
            = "<html>\n"
            + "  <body>\n"
            + "    <label id='a1'>a1</label>\n"
            + "    <label id='a2' accesskey='A'>a2</label>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "      var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "      log(a1.accessKey);\n"
            + "      log(a2.accessKey);\n"
            + "      a1.accessKey = 'a';\n"
            + "      a2.accessKey = 'A';\n"
            + "      log(a1.accessKey);\n"
            + "      log(a2.accessKey);\n"
            + "      a1.accessKey = 'a8';\n"
            + "      a2.accessKey = '8Afoo';\n"
            + "      log(a1.accessKey);\n"
            + "      log(a2.accessKey);\n"
            + "      a1.accessKey = '8';\n"
            + "      a2.accessKey = '@';\n"
            + "      log(a1.accessKey);\n"
            + "      log(a2.accessKey);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }
}
