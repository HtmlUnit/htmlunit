/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

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
public class HTMLTextAreaElementTest extends WebTestCase {
    private static final String LS = System.getProperty("line.separator");

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1234", "PoohBear" })
    public void testGetValue() throws Exception {
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOnChange() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + " <textarea name='textarea1' onchange='alert(this.value)'></textarea>\n"
            + "<input name='myButton' type='button' onclick='document.form1.textarea1.value=\"from button\"'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = page.getFormByName("form1");
        final HtmlTextArea textarea = form.getTextAreaByName("textarea1");
        textarea.setText("foo");
        final HtmlButtonInput button = form.getInputByName("myButton");
        button.click();

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Method type(...) should not trigger onchange!
     * @throws Exception if the test fails
     */
    @Test
    public void type_onchange() throws Exception {
        final String content
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function changed(e) {\n"
            + "    log('changed: ' + e.value)\n"
            + "  }\n"
            + "  function keypressed(e) {\n"
            + "    log('keypressed: ' + e.value)\n"
            + "  }\n"
            + "  function log(msg) {\n"
            + "    document.getElementById('log').value += msg + '\\n';\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "<textarea id='textArea1' onchange='changed(this)' onkeypress='keypressed(this)'></textarea>\n"
            + "<textarea id='log'></textarea>"
            + "</form>"
            + "</body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlTextArea textArea = page.getHtmlElementById("textArea1");
        textArea.type("hello");
        page.setFocusedElement(null); // remove focus on textarea to trigger onchange

        final HtmlTextArea log = page.getHtmlElementById("log");
        final String expectation = "keypressed: " + LS
            + "keypressed: h" + LS
            + "keypressed: he" + LS
            + "keypressed: hel" + LS
            + "keypressed: hell" + LS
            + "changed: hello" + LS;
        assertEquals(expectation, log.asText());
    }

    /**
     * Tests that setValue doesn't has side effect. Test for bug 1155063.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "TEXTAREA", "INPUT" })
    public void testSetValue() throws Exception {
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

        loadPageWithAlerts(content);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = {"undefined", "undefined" },
            FF = {"11", "0" })
    public void testTextLength() throws Exception {
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

        loadPageWithAlerts(content);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = {"undefined,undefined", "3,undefined", "3,10" },
            FF = {"11,11", "3,11", "3,10" })
    public void testSelection() throws Exception {
        testSelection(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = {"undefined,undefined", "-3,undefined", "-3,15" },
            FF = {"11,11", "0,11", "0,11" })
    public void testSelection_outOfBounds() throws Exception {
        testSelection(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(IE = {"undefined,undefined", "10,undefined", "10,5" },
            FF = {"11,11", "10,11", "5,5" })
    public void testSelection_reverseOrder() throws Exception {
        testSelection(10, 5);
    }

    private void testSelection(final int selectionStart, final int selectionEnd) throws Exception {
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "yes" },
            FF = { "no" })
    public void testDoScroll() throws Exception {
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

        loadPageWithAlerts(html);
    }

    /**
     * Test that the new line immediately following opening tag is ignored.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Hello\r\nworld\r\n" },
            FF = { "Hello\nworld\n" })
    public void value_ignoreFirstNewLine() throws Exception {
        testValue("\nHello\nworld\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { " \r\nHello\r\nworld\r\n" },
            FF = { " \nHello\nworld\n" })
    public void value_spaceBeforeFirstNewLine() throws Exception {
        testValue(" \nHello\nworld\n");
    }

    private void testValue(final String textAreaBody) throws Exception {
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

        loadPageWithAlerts(html);
    }
}
