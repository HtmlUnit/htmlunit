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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLTextAreaElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLTextAreaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1234", "PoohBear" })
    public void getValue() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "alert(document.form1.textarea1.value)\n"
            + "document.form1.textarea1.value='PoohBear';\n"
            + "alert(document.form1.textarea1.value )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='post' >\n"
            + "<textarea name='textarea1' cols='45' rows='4'>1234</textarea>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void onChange() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + " <textarea name='textarea1' onchange='alert(this.value)'></textarea>\n"
            + "<input name='myButton' type='button' onclick='document.form1.textarea1.value=\"from button\"'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textarea = driver.findElement(By.name("textarea1"));
        textarea.sendKeys("foo");
        driver.findElement(By.name("myButton")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Tests that setValue doesn't has side effect. Test for bug 1155063.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "TEXTAREA", "INPUT" })
    public void setValue() throws Exception {
        final String content = "<html><head></head>\n"
            + "<body>\n"
            + "<form name='form1'>\n"
            + "<textarea name='question'></textarea>\n"
            + "<input type='button' name='btn_submit' value='Next'>\n"
            + "</form>\n"
            + "<script>\n"
            + "document.form1.question.value = 'some text';\n"
            + "alert(document.form1.elements[0].tagName);\n"
            + "alert(document.form1.elements[1].tagName);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = {"undefined", "undefined" },
            FF = {"11", "0" })
    public void textLength() throws Exception {
        final String content = "<html>\n"
            + "<body>\n"
            + "<textarea id='myTextArea'></textarea>\n"
            + "<script>\n"
            + "    var textarea = document.getElementById('myTextArea');\n"
            + "    textarea.value = 'hello there';\n"
            + "    alert(textarea.textLength);\n"
            + "    textarea.value = '';\n"
            + "    alert(textarea.textLength);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = {"undefined,undefined", "3,undefined", "3,10" },
            FF3_6 = {"11,11", "3,11", "3,10" },
            FF = {"0,0", "3,3", "3,10" }
            )
    @NotYetImplemented(Browser.FF17)
    public void selection() throws Exception {
        selection(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = {"undefined,undefined", "-3,undefined", "-3,15" },
            FF3_6 = {"11,11", "0,11", "0,11" },
            FF = {"0,0", "0,0", "0,11" })
    @NotYetImplemented(Browser.FF17)
    public void selection_outOfBounds() throws Exception {
        selection(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = {"undefined,undefined", "10,undefined", "10,5" },
            FF3_6 = {"11,11", "10,11", "5,5" },
            FF = {"0,0", "10,10", "5,5" })
    @NotYetImplemented(Browser.FF17)
    public void selection_reverseOrder() throws Exception {
        selection(10, 5);
    }

    private void selection(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myTextArea'></textarea>\n"
            + "<script>\n"
            + "    var textarea = document.getElementById('myTextArea');\n"
            + "    textarea.value = 'Hello there';\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.selectionStart = " + selectionStart + ";\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.selectionEnd = " + selectionEnd + ";\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "yes", FF = "no")
    public void doScroll() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        if(t.doScroll) {\n"
            + "          alert('yes');\n"
            + "          t.doScroll();\n"
            + "          t.doScroll('down');\n"
            + "        } else {\n"
            + "          alert('no');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><textarea id='t'>abc</textarea></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the new line immediately following opening tag is ignored.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "Hello\r\nworld\r\n", FF = "Hello\nworld\n")
    public void value_ignoreFirstNewLine() throws Exception {
        value("\nHello\nworld\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = " \r\nHello\r\nworld\r\n", FF = " \nHello\nworld\n")
    public void value_spaceBeforeFirstNewLine() throws Exception {
        value(" \nHello\nworld\n");
    }

    private void value(final String textAreaBody) throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "  alert(document.form1.textarea1.value)\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1' method='post' >\n"
            + "<textarea name='textarea1'>" + textAreaBody + "</textarea>\n"
            + "</textarea>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { " foo \r\n bar ", " foo \r\n bar " },
            FF = { " foo \n bar ", " foo \n bar " })
    public void defaultValue() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var t = document.getElementById('textArea');\n"
            + "    alert(t.defaultValue);\n"
            + "    alert(t.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form id='form1'>\n"
            + "<textarea id='textArea'>\n foo \n bar </textarea>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "false" })
    public void readOnly() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var t = document.getElementById('textArea');\n"
            + "    alert(t.readOnly);\n"
            + "    t.readOnly = false;\n"
            + "    alert(t.readOnly);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form id='form1'>\n"
            + "<textarea id='textArea' readonly>\n foo \n bar </textarea>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "A", "a", "A", "a8", "8Afoo", "8", "@" })
    public void accessKey() throws Exception {
        final String html
            = "<html><body><textarea id='a1'>a1</textarea><textarea id='a2' accesskey='A'>a2</textarea><script>\n"
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
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = { "-1", "5", "8", "2", "0", "0", "0", "3" },
            FF = { "20", "5", "8", "2", "error", "error", "8", "2", "20", "3" },
            IE = { "20", "5", "8", "2", "error", "error", "8", "2", "error", "8", "3" })
    @NotYetImplemented(Browser.FF17)
    public void cols() throws Exception {
        final String html
            = "<html><body><textarea id='a1'>a1</textarea><textarea id='a2' cols='5'>a2</textarea><script>\n"
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.cols = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "  alert(a1.cols);\n"
            + "  alert(a2.cols);\n"
            + "  set(a1, '8');\n"
            + "  set(a2, 2);\n"
            + "  alert(a1.cols);\n"
            + "  alert(a2.cols);\n"
            + "  set(a1, 'a');\n"
            + "  set(a2, '');\n"
            + "  alert(a1.cols);\n"
            + "  alert(a2.cols);\n"
            + "  set(a1, -1);\n"
            + "  set(a2, 3.4);\n"
            + "  alert(a1.cols);\n"
            + "  alert(a2.cols);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = { "-1", "5", "8", "2", "0", "0", "0", "3" },
            FF = { "2", "5", "8", "2", "error", "error", "8", "2", "2", "3" },
            IE = { "2", "5", "8", "2", "error", "error", "8", "2", "error", "8", "3" })
    @NotYetImplemented(Browser.FF17)
    public void rows() throws Exception {
        final String html
            = "<html><body><textarea id='a1'>a1</textarea><textarea id='a2' rows='5'>a2</textarea><script>\n"
            + "  function set(e, value) {\n"
            + "    try {\n"
            + "      e.rows = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "  var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "  alert(a1.rows);\n"
            + "  alert(a2.rows);\n"
            + "  set(a1, '8');\n"
            + "  set(a2, 2);\n"
            + "  alert(a1.rows);\n"
            + "  alert(a2.rows);\n"
            + "  set(a1, 'a');\n"
            + "  set(a2, '');\n"
            + "  alert(a1.rows);\n"
            + "  alert(a2.rows);\n"
            + "  set(a1, -1);\n"
            + "  set(a2, 3.4);\n"
            + "  alert(a1.rows);\n"
            + "  alert(a2.rows);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(FF)
    @Alerts({ "9", "9", "2", "7" })
    public void selectionRange() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    var ta = document.getElementById('myInput');\n"
            + "    ta.setSelectionRange(15, 15);\n"
            + "    alert(ta.selectionStart);"
            + "    alert(ta.selectionEnd);"
            + "    ta.setSelectionRange(2, 7);\n"
            + "    alert(ta.selectionStart);"
            + "    alert(ta.selectionEnd);"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<textarea id='myInput'>some test</textarea>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "null", "4", "null", "4" }, FF = { "null", "4", "", "0" })
    @NotYetImplemented(FF)
    public void getAttributeAndSetValue() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'null';\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"
            + "\n"
            + "        t.value = null;\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea id='t'>abc</textarea>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

}
