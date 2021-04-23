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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLTextAreaElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
@RunWith(BrowserRunner.class)
public class HTMLTextAreaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234", "PoohBear"})
    public void getValue() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      alert(document.form1.textarea1.value);\n"
            + "      document.form1.textarea1.value = 'PoohBear';\n"
            + "      alert(document.form1.textarea1.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <p>hello world</p>\n"
            + "  <form name='form1' method='post' >\n"
            + "    <textarea name='textarea1' cols='45' rows='4'>1234</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void onChange() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='textarea1' onchange='alert(this.value)'></textarea>\n"
            + "    <input name='myButton' type='button' onclick='document.form1.textarea1.value=\"from button\"'>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textarea = driver.findElement(By.name("textarea1"));
        textarea.sendKeys("foo");
        driver.findElement(By.name("myButton")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Tests that setValue doesn't has side effect. Test for bug 1155063.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TEXTAREA", "INPUT"})
    public void setValue() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "    <input type='button' name='btn_submit' value='Next'>\n"
            + "  </form>\n"
            + "  <script>\n"
            + "    document.form1.question.value = 'some text';\n"
            + "    alert(document.form1.elements[0].tagName);\n"
            + "    alert(document.form1.elements[1].tagName);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"11", "0"},
            IE = {"undefined", "undefined"})
    public void textLength() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <textarea id='myTextArea'></textarea>\n"
            + "  <script>\n"
            + "    var textarea = document.getElementById('myTextArea');\n"
            + "    textarea.value = 'hello there';\n"
            + "    alert(textarea.textLength);\n"
            + "    textarea.value = '';\n"
            + "    alert(textarea.textLength);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "3,11", "3,10", "7,7"},
            IE = {"0,0", "0,0", "3,3", "3,10", "0,0"})
    public void selection() throws Exception {
        selection(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "11,11", "11,11", "7,7"},
            IE = {"0,0", "0,0", "0,0", "0,11", "0,0"})
    public void selection_outOfBounds() throws Exception {
        selection(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "10,11", "5,5", "7,7"},
            IE = {"0,0", "0,0", "10,10", "5,5", "7,7"})
    @NotYetImplemented(IE)
    public void selection_reverseOrder() throws Exception {
        selection(10, 5);
    }

    private void selection(final int selectionStart, final int selectionEnd) throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <textarea id='myTextArea'></textarea>\n"
            + "  <script>\n"
            + "    var textarea = document.getElementById('myTextArea');\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.value = 'Hello there';\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.selectionStart = " + selectionStart + ";\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.selectionEnd = " + selectionEnd + ";\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.value = 'nothing';\n"
            + "    alert(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no")
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
    @Alerts("Hello\nworld\n")
    public void value_ignoreFirstNewLine() throws Exception {
        value("\nHello\nworld\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(" \nHello\nworld\n")
    public void value_spaceBeforeFirstNewLine() throws Exception {
        value(" \nHello\nworld\n");
    }

    private void value(final String textAreaBody) throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      alert(document.form1.textarea1.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1' method='post' >\n"
            + "    <textarea name='textarea1'>" + textAreaBody + "</textarea>\n"
            + "    </textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({" foo \n bar ", " foo \n bar "})
    public void defaultValue() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var t = document.getElementById('textArea');\n"
            + "      alert(t.defaultValue);\n"
            + "      alert(t.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form id='form1'>\n"
            + "    <textarea id='textArea'>\n foo \n bar </textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void readOnly() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "  <script>\n"
            + "  function test() {\n"
            + "    var t = document.getElementById('textArea');\n"
            + "    alert(t.readOnly);\n"
            + "    t.readOnly = false;\n"
            + "    alert(t.readOnly);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form id='form1'>\n"
            + "    <textarea id='textArea' readonly>\n foo \n bar </textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "A", "a", "A", "a8", "8Afoo", "8", "@"})
    public void accessKey() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <textarea id='a1'>a1</textarea>\n"
            + "  <textarea id='a2' accesskey='A'>a2</textarea>\n"

            + "  <script>\n"
            + "    var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "    alert(a1.accessKey);\n"
            + "    alert(a2.accessKey);\n"

            + "    a1.accessKey = 'a';\n"
            + "    alert(a1.accessKey);\n"

            + "    a1.accessKey = 'A';\n"
            + "    alert(a1.accessKey);\n"

            + "    a1.accessKey = 'a8';\n"
            + "    alert(a1.accessKey);\n"

            + "    a1.accessKey = '8Afoo';\n"
            + "    alert(a1.accessKey);\n"

            + "    a1.accessKey = '8';\n"
            + "    alert(a1.accessKey);\n"

            + "    a1.accessKey = '@';\n"
            + "    alert(a1.accessKey);\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"20", "5", "8", "4", "20", "20", "20", "3"},
            IE = {"20", "5", "8", "4", "error", "4", "error", "4", "error", "4", "3"})
    public void cols() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function setCols(e, value) {\n"
            + "    try {\n"
            + "      e.cols = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <textarea id='a1'>a1</textarea>\n"
            + "  <textarea id='a2' cols='5'>a2</textarea>\n"

            + "  <script>\n"
            + "    var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "    alert(a1.cols);\n"
            + "    alert(a2.cols);\n"

            + "    setCols(a1, '8');\n"
            + "    alert(a1.cols);\n"

            + "    setCols(a1, 4);\n"
            + "    alert(a1.cols);\n"

            + "    setCols(a1, 'a');\n"
            + "    alert(a1.cols);\n"

            + "    setCols(a1, '');\n"
            + "    alert(a1.cols);\n"

            + "    setCols(a1, -1);\n"
            + "    alert(a1.cols);\n"

            + "    setCols(a1, 3.4);\n"
            + "    alert(a1.cols);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "5", "8", "4", "2", "2", "2", "3"},
            IE = {"2", "5", "8", "4", "error", "4", "error", "4", "error", "4", "3"})
    public void rows() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function setRows(e, value) {\n"
            + "    try {\n"
            + "      e.rows = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <textarea id='a1'>a1</textarea>\n"
            + "  <textarea id='a2' rows='5'>a2</textarea>\n"

            + "  <script>\n"
            + "    var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "    alert(a1.rows);\n"
            + "    alert(a2.rows);\n"

            + "    setRows(a1, '8');\n"
            + "    alert(a1.rows);\n"

            + "    setRows(a1, 4);\n"
            + "    alert(a1.rows);\n"

            + "    setRows(a1, 'a');\n"
            + "    alert(a1.rows);\n"

            + "    setRows(a1, '');\n"
            + "    alert(a1.rows);\n"

            + "    setRows(a1, -1);\n"
            + "    alert(a1.rows);\n"

            + "    setRows(a1, 3.4);\n"
            + "    alert(a1.rows);\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"9", "9", "2", "7"})
    public void selectionRange() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var ta = document.getElementById('myInput');\n"
            + "      ta.setSelectionRange(15, 15);\n"
            + "      alert(ta.selectionStart);\n"
            + "      alert(ta.selectionEnd);\n"
            + "      ta.setSelectionRange(2, 7);\n"
            + "      alert(ta.selectionStart);\n"
            + "      alert(ta.selectionEnd);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='myInput'>some test</textarea>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"test", "4", "42", "2", "[object HTMLTextAreaElement]", "28"})
    public void getAttributeAndSetValue() throws Exception {
        final String html =
            "<html>\n"
            + "  <head><title>foo</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'test';\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"

            + "        t.value = 42;\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"

            + "        t.value = document.getElementById('t');\n"
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "4", "", "0"},
            IE = {"null", "4", "null", "4"})
    public void getAttributeAndSetValueNull() throws Exception {
        final String html =
            "<html>\n"
            + "  <head><title>foo</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'null';\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"

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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"12", "2", "[object HTMLTextAreaElement]", "28"})
    public void getAttributeAndSetValueOther() throws Exception {
        final String html =
            "<html>\n"
            + "  <head><title>foo</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 12;\n"
            + "        alert(t.value);\n"
            + "        if (t.value != null)\n"
            + "          alert(t.value.length);\n"

            + "        t.value = t;\n"
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-1", "null", "32", "32", "-1", "ms"},
            IE = {"2147483647", "null", "32", "32", "2147483647", "ms"})
    public void maxLength() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.form1.textarea1.maxLength);\n"
            + "      alert(document.form1.textarea1.getAttribute('maxLength'));\n"
            + "      alert(document.form1.textarea2.maxLength);\n"
            + "      alert(document.form1.textarea2.getAttribute('maxLength'));\n"
            + "      alert(document.form1.textarea3.maxLength);\n"
            + "      alert(document.form1.textarea3.getAttribute('maxLength'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form name='form1' method='post'>\n"
            + "    <textarea name='textarea1'></textarea>\n"
            + "    <textarea name='textarea2' maxLength='32'></textarea>\n"
            + "    <textarea name='textarea3' maxLength='ms'></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-1", "null", "32", "32", "-1", "ms"},
            IE = {"undefined", "null", "undefined", "32", "undefined", "ms"})
    public void minLength() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.form1.textarea1.minLength);\n"
            + "      alert(document.form1.textarea1.getAttribute('minLength'));\n"
            + "      alert(document.form1.textarea2.minLength);\n"
            + "      alert(document.form1.textarea2.getAttribute('minLength'));\n"
            + "      alert(document.form1.textarea3.minLength);\n"
            + "      alert(document.form1.textarea3.getAttribute('minLength'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form name='form1' method='post'>\n"
            + "    <textarea name='textarea1'></textarea>\n"
            + "    <textarea name='textarea2' minLength='32'></textarea>\n"
            + "    <textarea name='textarea3' minLength='ms'></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"10", "10", "error", "10", "10", "0", "0"},
            IE = {"10", "10", "-1", "-1", "0", "0"})
    public void setMaxLength() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "  <script>\n"
            + "    function setMaxLength(length){\n"
            + "      try {\n"
            + "        document.form1.textarea1.maxLength = length;\n"
            + "      } catch(e) { alert('error'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='form1' method='post' >\n"
            + "    <textarea id='textarea1'></textarea>\n"
            + "    <script>\n"
            + "      var a = document.getElementById('textarea1');\n"

            + "      setMaxLength(10);\n"
            + "      alert(a.maxLength);\n"
            + "      alert(a.getAttribute('maxLength'));\n"

            + "      setMaxLength(-1);\n"
            + "      alert(a.maxLength);\n"
            + "      alert(a.getAttribute('maxLength'));\n"

            + "      setMaxLength('abc');\n"
            + "      alert(a.maxLength);\n"
            + "      alert(a.getAttribute('maxLength'));\n"

            + "    </script>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLFormElement]")
    public void form() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <textarea id='a'></textarea>\n"
            + "  </form>"
            + "  <script>\n"
            + "    alert(document.getElementById('a').form);\n"
            + "  </script>"
            + "</body>"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverTextarea() throws Exception {
        shutDownAll();
        mouseOver("<textarea id='tester' onmouseover='dumpEvent(event);'>HtmlUnit</textarea>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "mouse over [tester]",
            FF78 = "mouse over [tester]")
    public void mouseOverTextareaDisabled() throws Exception {
        shutDownAll();
        mouseOver("<textarea id='tester' onmouseover='dumpEvent(event);' disabled >HtmlUnit</textarea>");
    }

    private void mouseOver(final String element) throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "    function dumpEvent(event) {\n"
            + "      // target\n"
            + "      var eTarget;\n"
            + "      if (event.target) {\n"
            + "        eTarget = event.target;\n"
            + "      } else if (event.srcElement) {\n"
            + "        eTarget = event.srcElement;\n"
            + "      }\n"
            + "      // defeat Safari bug\n"
            + "      if (eTarget.nodeType == 3) {\n"
            + "        eTarget = eTarget.parentNode;\n"
            + "      }\n"
            + "      var msg = 'mouse over';\n"
            + "      if (eTarget.name) {\n"
            + "        msg = msg + ' [' + eTarget.name + ']';\n"
            + "      } else {\n"
            + "        msg = msg + ' [' + eTarget.id + ']';\n"
            + "      }\n"
            + "      document.title += msg;\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    " + element + "\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.id("tester")));
        actions.perform();

        assertTitle(driver, getExpectedAlerts()[0]);
    }
}
