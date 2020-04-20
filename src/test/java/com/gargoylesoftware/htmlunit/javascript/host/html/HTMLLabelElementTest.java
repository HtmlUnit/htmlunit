/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
            + "\n"
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
            + "\n"
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
            + "\n"
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
            + "\n"
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
    @Alerts({ "unknown", "null" })
    public void htmlForSetToUnknown() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "\n"
            + "      function doTest() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').htmlFor = 'unknown';\n"
            + "        } catch (e) {"
            + "          log('exception');\n"
            + "        }\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
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
    @Alerts({ "div1", "null" })
    public void htmlForSetToNotLabelable() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "\n"
            + "      function doTest() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').htmlFor = 'div1';\n"
            + "        } catch (e) {"
            + "          log('exception');\n"
            + "        }\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "        log(document.getElementById('label1').control);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <label id='label1'>Item</label>\n"
            + "    <div id='div1'></div>\n"
            + "  </body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "text1", "[object HTMLInputElement]" })
    public void htmlForSetToLabelable() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "\n"
            + "      function doTest() {\n"
            + "        try {\n"
            + "          document.getElementById('label1').htmlFor = 'text1';\n"
            + "        } catch (e) {"
            + "          log('exception');\n"
            + "        }\n"
            + "        log(document.getElementById('label1').htmlFor);\n"
            + "        log(document.getElementById('label1').control);\n"
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
    // XXX rename and move
    @Test
    public void htmlFor() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>First</title>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            + "    document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <label id='label1'>My Label</label>\n"
            + "  <input type='checkbox' id='checkbox1'>\n"
            + "</body></html>";

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
    @Alerts("null")
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
    @Alerts("null")
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
    @Alerts("null")
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
    @Alerts("null")
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
    @Alerts("[object HTMLButtonElement]:button1")
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
    @Alerts("[object HTMLInputElement]:text1")
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
    @Alerts("null")
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
            IE = "null")
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
            IE = "null")
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
    @Alerts("[object HTMLProgressElement]:progress1")
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
    @Alerts("[object HTMLSelectElement]:select1")
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
    @Alerts("[object HTMLTextAreaElement]:text1")
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
    @Alerts("null")
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
    @Alerts("[object HTMLButtonElement]:button1")
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
    @Alerts("[object HTMLInputElement]:text1")
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
    @Alerts("null")
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
            IE = "null")
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
            IE = "null")
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
    @Alerts("[object HTMLProgressElement]:progress1")
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
    @Alerts("[object HTMLSelectElement]:select1")
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
    @Alerts("[object HTMLTextAreaElement]:text1")
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
    @Alerts("[object HTMLInputElement]:text2")
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
    @Alerts("null")
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
    @Alerts("null")
    public void controlForNotLabelableVersusNested() throws Exception {
        final String html =
              "  <label id='label1' for='div1'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <div id='div1'></div>\n";

        final WebDriver driver = loadPage2(generateControlPage(html));
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    private String generateControlPage(final String snippet) {
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
    @Alerts("null")
    public void controlSet() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "\n"
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
     * Tests that clicking the label by JavaScript does not change 'htmlFor' attribute in FF!!
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    // in fact not used as JS alerts...
    public void htmlFor_click() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "}\n"
            + "function delegateClick() {\n"
            + "  try {\n"
            + "    document.getElementById('label1').click();\n"
            + "  } catch (e) {}\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<label id='label1'>My Label</label>\n"
            + "<input type='checkbox' id='checkbox1'><br>\n"
            + "<input type=button id='button1' value='Test' onclick='delegateClick()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkbox.isSelected());
        driver.findElement(By.id("button1")).click();

        assertEquals(getExpectedAlerts()[0], "" + checkbox.isSelected());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "A", "a", "A", "a8", "8Afoo", "8", "@"})
    public void accessKey() throws Exception {
        final String html
            = "<html><body><label id='a1'>a1</label><label id='a2' accesskey='A'>a2</label><script>\n"
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a';\n"
            + "a2.accessKey = 'A';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a8';\n"
            + "a2.accessKey = '8Afoo';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = '8';\n"
            + "a2.accessKey = '@';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"null", "[object HTMLFormElement]"},
            IE = {"[object HTMLFormElement]", "[object HTMLFormElement]"})
    public void form() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <label id='a'>a</label>"

            + "    <label id='b' for='checkbox1'>My Label</label>\n"
            + "    <input type='checkbox' id='checkbox1'>\n"
            + "  </form>"

            + "  <script>\n"
            + "    alert(document.getElementById('a').form);\n"
            + "    alert(document.getElementById('b').form);\n"
            + "  </script>"
            + "</body>"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
