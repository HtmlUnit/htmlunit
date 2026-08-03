/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Tests for {@link HTMLInputElement} and buttons.
 *
 * @author Mike Bowler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 */
public class HTMLInputElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "text", "textfield1", "form1", "cat"})
    public void standardProperties_Text() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.form1.textfield1.value);\n"
            + "  log(document.form1.textfield1.type);\n"
            + "  log(document.form1.textfield1.name);\n"
            + "  log(document.form1.textfield1.form.name);\n"
            + "  document.form1.textfield1.value = 'cat';\n"
            + "  log(document.form1.textfield1.value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = {"error.fileupload1", "abc", "abc", "abc", "", "abc", "", "", "abc", "abc",
                       "abc", "abc", "abc", "abc", "abc", "abc", "#000000", "", "abc", "", "", "",
                       "", "", "50", "abc", "abc", "abc", "abc"},
            FF = {"error.fileupload1", "abc", "abc", "abc", "", "abc", "", "", "abc", "abc",
                  "abc", "abc", "abc", "abc", "abc", "abc", "#000000", "", "abc", "", "", "abc",
                  "abc", "", "50", "abc", "abc", "abc", "abc"},
            FF_ESR = {"error.fileupload1", "abc", "abc", "abc", "", "abc", "", "", "abc", "abc",
                      "abc", "abc", "abc", "abc", "abc", "abc", "#000000", "", "abc", "", "", "abc",
                      "abc", "", "50", "abc", "abc", "abc", "abc"})
    @Test
    public void setValueString() throws Exception {
        testValue("'abc'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"", "", "", "", "", "", "", "", "",
             "", "", "", "", "", "", "#000000", "", "", "", "", "",
             "", "", "50", "", "", "", ""})
    @Test
    public void setValueEmptyString() throws Exception {
        testValue("''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(CHROME = {"error.fileupload1", ". ", ". ", ". ", "", ". ", "", "", ". ", ". ",
                      ". ", ". ", ". ", ". ", ". ", ". ", "#000000", "", ". ", "", "", "",
                      "", "", "50", ". ", "", ". ", ""},
            EDGE = {"error.fileupload1", ". ", ". ", ". ", "", ". ", "", "", ". ", ". ",
                    ". ", ". ", ". ", ". ", ". ", ". ", "#000000", "", ". ", "", "", "",
                    "", "", "50", ". ", "", ". ", ""},
            FF = {"error.fileupload1", ". ", ". ", ". ", "", ". ", "", "", ". ", ". ",
                  ". ", ". ", ". ", ". ", ". ", ". ", "#000000", "", ". ", "", "", ". ",
                  ". ", "", "50", ". ", "", ". ", ""},
            FF_ESR = {"error.fileupload1", ". ", ". ", ". ", "", ". ", "", "", ". ", ". ",
                      ". ", ". ", ". ", ". ", ". ", ". ", "#000000", "", ". ", "", "", ". ",
                      ". ", "", "50", ". ", "", ". ", ""})
    @Test
    public void setValueBlankString() throws Exception {
        testValue("'  '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = {"error.fileupload1", "12", "12", "12", "", "12", "", "", "12", "12",
                       "12", "12", "12", "12", "12", "12", "#000000", "", "12", "", "", "",
                       "", "12", "12", "12", "12", "12", "12"},
            FF = {"error.fileupload1", "12", "12", "12", "", "12", "", "", "12", "12",
                  "12", "12", "12", "12", "12", "12", "#000000", "", "12", "", "", "12",
                  "12", "12", "12", "12", "12", "12", "12"},
            FF_ESR = {"error.fileupload1", "12", "12", "12", "", "12", "", "", "12", "12",
                      "12", "12", "12", "12", "12", "12", "#000000", "", "12", "", "", "12",
                      "12", "12", "12", "12", "12", "12", "12"})
    @Test
    public void setValueNumber() throws Exception {
        testValue("12");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"", "null", "", "", "", "", "", "", "",
             "", "null", "", "null", "", "", "#000000", "", "", "", "", "",
             "", "", "50", "", "", "", ""})
    @Test
    public void setValueNull() throws Exception {
        testValue("null");
    }

    private void testValue(final String value) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function log(msg) { window.document.title += msg.replace(' ', '.') + '§';}\n"
            + "function doTest() {\n"

            + "  document.form1.button1.value = " + value + ";\n"
            + "  document.form1.button2.value = " + value + ";\n"
            + "  document.form1.checkbox1.value = " + value + ";\n"
            + "  try { document.form1.fileupload1.value = " + value + " } catch(e) { log('error fileupload1') }\n"
            + "  document.form1.hidden1.value = " + value + ";\n"
            + "  document.form1.select1.value = " + value + ";\n"
            + "  document.form1.select2.value = " + value + ";\n"
            + "  document.form1.password1.value = " + value + ";\n"
            + "  document.form1.radio1.value = " + value + ";\n"
            + "  document.form1.reset1.value = " + value + ";\n"
            + "  document.form1.reset2.value = " + value + ";\n"
            + "  document.form1.submit1.value = " + value + ";\n"
            + "  document.form1.submit2.value = " + value + ";\n"
            + "  document.form1.textInput1.value = " + value + ";\n"
            + "  document.form1.textarea1.value = " + value + ";\n"
            + "  document.form1.color1.value = " + value + ";\n"
            + "  document.form1.date1.value = " + value + ";\n"
            + "  document.form1.datetime1.value = " + value + ";\n"
            + "  document.form1.datetimeLocal1.value = " + value + ";\n"
            + "  document.form1.time1.value = " + value + ";\n"
            + "  document.form1.week1.value = " + value + ";\n"
            + "  document.form1.month1.value = " + value + ";\n"
            + "  document.form1.number1.value = " + value + ";\n"
            + "  document.form1.range1.value = " + value + ";\n"
            + "  document.form1.search1.value = " + value + ";\n"
            + "  document.form1.email1.value = " + value + ";\n"
            + "  document.form1.tel1.value = " + value + ";\n"
            + "  document.form1.url1.value = " + value + ";\n"

            + "  log(document.form1.button1.value);\n"
            + "  log(document.form1.button2.value);\n"
            + "  log(document.form1.checkbox1.value);\n"
            + "  log(document.form1.fileupload1.value);\n"
            + "  log(document.form1.hidden1.value);\n"
            + "  log(document.form1.select1.value);\n"
            + "  log(document.form1.select2.value);\n"
            + "  log(document.form1.password1.value);\n"
            + "  log(document.form1.radio1.value);\n"
            + "  log(document.form1.reset1.value);\n"
            + "  log(document.form1.reset2.value);\n"
            + "  log(document.form1.submit1.value);\n"
            + "  log(document.form1.submit2.value);\n"
            + "  log(document.form1.textInput1.value);\n"
            + "  log(document.form1.textarea1.value);\n"
            + "  log(document.form1.color1.value);\n"
            + "  log(document.form1.date1.value);\n"
            + "  log(document.form1.datetime1.value);\n"
            + "  log(document.form1.datetimeLocal1.value);\n"
            + "  log(document.form1.time1.value);\n"
            + "  log(document.form1.week1.value);\n"
            + "  log(document.form1.month1.value);\n"
            + "  log(document.form1.number1.value);\n"
            + "  log(document.form1.range1.value);\n"
            + "  log(document.form1.search1.value);\n"
            + "  log(document.form1.email1.value);\n"
            + "  log(document.form1.tel1.value);\n"
            + "  log(document.form1.url1.value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='button' name='button1'></button>\n"
            + "  <button type='button' name='button2'></button>\n"
            + "  <input type='checkbox' name='checkbox1'/>\n"
            + "  <input type='file' name='fileupload1'/>\n"
            + "  <input type='hidden' name='hidden1'/>\n"
            + "  <select name='select1'>\n"
            + "    <option>foo</option>\n"
            + "  </select>\n"
            + "  <select multiple='multiple' name='select2'>\n"
            + "    <option>boo</option>\n"
            + "  </select>\n"
            + "  <input type='password' name='password1'/>\n"
            + "  <input type='radio' name='radio1'/>\n"
            + "  <input type='reset' name='reset1'/>\n"
            + "  <button type='reset' name='reset2'></button>\n"
            + "  <input type='submit' name='submit1'/>\n"
            + "  <button type='submit' name='submit2'></button>\n"
            + "  <input type='text' name='textInput1'/>\n"
            + "  <textarea name='textarea1'>foo</textarea>\n"
            + "  <input type='color' name='color1'/>\n"
            + "  <input type='date' name='date1'/>\n"
            + "  <input type='datetime' name='datetime1'/>\n"
            + "  <input type='datetime-local' name='datetimeLocal1'/>\n"
            + "  <input type='time' name='time1'/>\n"
            + "  <input type='week' name='week1'/>\n"
            + "  <input type='month' name='month1'/>\n"
            + "  <input type='number' name='number1'/>\n"
            + "  <input type='range' name='range1'/>\n"
            + "  <input type='search' name='search1'/>\n"
            + "  <input type='email' name='email1'/>\n"
            + "  <input type='tel' name='tel1'/>\n"
            + "  <input type='url' name='url1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"button", "button", "checkbox", "file", "hidden", "select-one", "select-multiple",
                       "password", "radio", "reset", "reset",
                       "submit", "submit", "text", "textarea", "color", "date", "text",
                       "datetime-local", "time", "week", "month", "number",
                       "range", "search", "email", "tel", "url"},
            FF = {"button", "button", "checkbox", "file", "hidden", "select-one", "select-multiple",
                  "password", "radio", "reset", "reset",
                  "submit", "submit", "text", "textarea", "color", "date", "text",
                  "datetime-local", "time", "text", "text", "number", "range",
                  "search", "email", "tel", "url"},
            FF_ESR = {"button", "button", "checkbox", "file", "hidden", "select-one", "select-multiple",
                      "password", "radio", "reset", "reset",
                      "submit", "submit", "text", "textarea", "color", "date", "text",
                      "datetime-local", "time", "text", "text", "number", "range",
                      "search", "email", "tel", "url"})
    public void type() throws Exception {
        testAttribute("type", "", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"null", "undefined", "null", "[object FileList]", "null", "undefined", "undefined", "null",
             "null", "null", "undefined", "null", "undefined", "null", "undefined", "null", "null", "null",
             "null", "null", "null", "null", "null", "null", "null", "null", "null", "null"})
    @Test
    public void files() throws Exception {
        testAttribute("files", "", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"false", "undefined", "false", "false", "false", "undefined", "undefined",
             "false", "false", "false", "undefined", "false", "undefined", "false",
             "undefined", "false", "false", "false", "false", "false", "false",
             "false", "false", "false", "false", "false", "false", "false"})
    @Test
    public void checked() throws Exception {
        testAttribute("checked", "", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"true", "undefined", "true", "true", "true", "undefined", "undefined",
             "true", "true", "true", "undefined", "true", "undefined", "true",
             "undefined", "true", "true", "true", "true", "true", "true",
             "true", "true", "true", "true", "true", "true", "true"})
    @Test
    public void checkedWithAttribute() throws Exception {
        testAttribute("checked", "checked", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"true", "undefined", "true", "true", "true", "undefined", "undefined",
             "true", "true", "true", "undefined", "true", "undefined", "true",
             "undefined", "true", "true", "true", "true", "true", "true",
             "true", "true", "true", "true", "true", "true", "true"})
    @Test
    public void setCheckedTrue() throws Exception {
        testAttribute("checked", "", "true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"true", "undefined", "true", "true", "true", "undefined", "undefined",
             "true", "true", "true", "undefined", "true", "undefined", "true",
             "undefined", "true", "true", "true", "true", "true", "true",
             "true", "true", "true", "true", "true", "true", "true"})
    @Test
    public void setCheckedBlank() throws Exception {
        testAttribute("checked", "", "");
    }

    private void testAttribute(final String property, final String attrib, final String value) throws Exception {
        String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n";

        if (value != null) {
            html = html
                + "  document.form1.button1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.button2.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.checkbox1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.fileupload1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.hidden1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.select1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.select2.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.password1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.radio1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.reset1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.reset2.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.submit1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.submit2.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.textInput1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.textarea1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.color1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.date1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.datetime1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.datetimeLocal1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.time1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.week1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.month1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.number1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.range1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.search1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.email1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.tel1.setAttribute('" + property + "', '" + value + "');\n"
                + "  document.form1.url1.setAttribute('" + property + "', '" + value + "');\n";
        }

        html = html
            + "  log(document.form1.button1." + property + ");\n"
            + "  log(document.form1.button2." + property + ");\n"
            + "  log(document.form1.checkbox1." + property + ");\n"
            + "  log(document.form1.fileupload1." + property + ");\n"
            + "  log(document.form1.hidden1." + property + ");\n"
            + "  log(document.form1.select1." + property + ");\n"
            + "  log(document.form1.select2." + property + ");\n"
            + "  log(document.form1.password1." + property + ");\n"
            + "  log(document.form1.radio1." + property + ");\n"
            + "  log(document.form1.reset1." + property + ");\n"
            + "  log(document.form1.reset2." + property + ");\n"
            + "  log(document.form1.submit1." + property + ");\n"
            + "  log(document.form1.submit2." + property + ");\n"
            + "  log(document.form1.textInput1." + property + ");\n"
            + "  log(document.form1.textarea1." + property + ");\n"
            + "  log(document.form1.color1." + property + ");\n"
            + "  log(document.form1.date1." + property + ");\n"
            + "  log(document.form1.datetime1." + property + ");\n"
            + "  log(document.form1.datetimeLocal1." + property + ");\n"
            + "  log(document.form1.time1." + property + ");\n"
            + "  log(document.form1.week1." + property + ");\n"
            + "  log(document.form1.month1." + property + ");\n"
            + "  log(document.form1.number1." + property + ");\n"
            + "  log(document.form1.range1." + property + ");\n"
            + "  log(document.form1.search1." + property + ");\n"
            + "  log(document.form1.email1." + property + ");\n"
            + "  log(document.form1.tel1." + property + ");\n"
            + "  log(document.form1.url1." + property + ");\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='button' name='button1' " + attrib + "></button>\n"
            + "  <button type='button' name='button2' " + attrib + "></button>\n"
            + "  <input type='checkbox' name='checkbox1' " + attrib + "/>\n"
            + "  <input type='file' name='fileupload1' " + attrib + "/>\n"
            + "  <input type='hidden' name='hidden1' " + attrib + "/>\n"
            + "  <select name='select1' " + attrib + ">\n"
            + "    <option>foo</option>\n"
            + "  </select>\n"
            + "  <select multiple='multiple' name='select2' " + attrib + ">\n"
            + "    <option>foo</option>\n"
            + "  </select>\n"
            + "  <input type='password' name='password1' " + attrib + "/>\n"
            + "  <input type='radio' name='radio1' " + attrib + "/>\n"
            + "  <input type='reset' name='reset1' " + attrib + "/>\n"
            + "  <button type='reset' name='reset2' " + attrib + "></button>\n"
            + "  <input type='submit' name='submit1' " + attrib + "/>\n"
            + "  <button type='submit' name='submit2' " + attrib + "></button>\n"
            + "  <input type='text' name='textInput1' " + attrib + "/>\n"
            + "  <textarea name='textarea1' " + attrib + ">foo</textarea>\n"
            + "  <input type='color' name='color1' " + attrib + "/>\n"
            + "  <input type='date' name='date1' " + attrib + "/>\n"
            + "  <input type='datetime' name='datetime1' " + attrib + "/>\n"
            + "  <input type='datetime-local' name='datetimeLocal1' " + attrib + "/>\n"
            + "  <input type='time' name='time1' " + attrib + "/>\n"
            + "  <input type='week' name='week1' " + attrib + "/>\n"
            + "  <input type='month' name='month1' " + attrib + "/>\n"
            + "  <input type='number' name='number1' " + attrib + "/>\n"
            + "  <input type='range' name='range1' " + attrib + "/>\n"
            + "  <input type='search' name='search1' " + attrib + "/>\n"
            + "  <input type='email' name='email1' " + attrib + "/>\n"
            + "  <input type='tel' name='tel1' " + attrib + "/>\n"
            + "  <input type='url' name='url1' " + attrib + "/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeInputButton() throws Exception {
        testAttribute("button1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeButton() throws Exception {
        testAttribute("button2", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeCheckbox() throws Exception {
        testAttribute("checkbox1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("")
    @Test
    public void setValueAttributeFile() throws Exception {
        testAttribute("fileupload1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeHidden() throws Exception {
        testAttribute("hidden1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("foo")
    @Test
    public void setValueAttributeSelect() throws Exception {
        testAttribute("select1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("")
    @Test
    public void setValueAttributeSelectMultiple() throws Exception {
        testAttribute("select2", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributePassword() throws Exception {
        testAttribute("password1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeRadio() throws Exception {
        testAttribute("radio1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeReset() throws Exception {
        testAttribute("reset1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeResetButton() throws Exception {
        testAttribute("reset2", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeSubmit() throws Exception {
        testAttribute("submit1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeSubmitButton() throws Exception {
        testAttribute("submit2", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeText() throws Exception {
        testAttribute("textInput1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("foo")
    @Test
    public void setValueAttributeTextArea() throws Exception {
        testAttribute("textarea1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("#000000")
    @Test
    public void setValueAttributeColor() throws Exception {
        testAttribute("color1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("")
    @HtmlUnitNYI(CHROME = "abc",
            EDGE = "abc",
            FF = "abc",
            FF_ESR = "abc")
    @Test
    public void setValueAttributeDate() throws Exception {
        testAttribute("date1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeDateTime() throws Exception {
        testAttribute("datetime1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("")
    @HtmlUnitNYI(CHROME = "abc",
            EDGE = "abc",
            FF = "abc",
            FF_ESR = "abc")
    @Test
    public void setValueAttributeDateTimeLocal() throws Exception {
        testAttribute("datetimeLocal1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("")
    @Test
    public void setValueAttributeTime() throws Exception {
        testAttribute("time1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = "",
            FF = "abc",
            FF_ESR = "abc")
    @HtmlUnitNYI(CHROME = "abc",
            EDGE = "abc")
    @Test
    public void setValueAttributeWeek() throws Exception {
        testAttribute("week1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = "",
            FF = "abc",
            FF_ESR = "abc")
    @HtmlUnitNYI(CHROME = "abc",
            EDGE = "abc")
    @Test
    public void setValueAttributeMonth() throws Exception {
        testAttribute("month1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("")
    @Test
    public void setValueAttributeNumber() throws Exception {
        testAttribute("number1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("50")
    @Test
    public void setValueAttributeRange() throws Exception {
        testAttribute("range1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeSearch() throws Exception {
        testAttribute("search1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeEmail() throws Exception {
        testAttribute("email1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeTel() throws Exception {
        testAttribute("tel1", "value", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("abc")
    @Test
    public void setValueAttributeUrl() throws Exception {
        testAttribute("url1", "value", "", "abc");
    }

    private void testAttribute(final String itemId, final String property,
                    final String attrib, final String value) throws Exception {
        String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n";

        if (value != null) {
            html = html
                + "  document.form1." + itemId + ".setAttribute('" + property + "', '" + value + "');\n";
        }

        html = html
            + "  log(document.form1." + itemId + "." + property + ");\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='button' name='button1' " + attrib + "></button>\n"
            + "  <button type='button' name='button2' " + attrib + "></button>\n"
            + "  <input type='checkbox' name='checkbox1' " + attrib + "/>\n"
            + "  <input type='file' name='fileupload1' " + attrib + "/>\n"
            + "  <input type='hidden' name='hidden1' " + attrib + "/>\n"
            + "  <select name='select1' " + attrib + ">\n"
            + "    <option>foo</option>\n"
            + "  </select>\n"
            + "  <select multiple='multiple' name='select2' " + attrib + ">\n"
            + "    <option>foo</option>\n"
            + "  </select>\n"
            + "  <input type='password' name='password1' " + attrib + "/>\n"
            + "  <input type='radio' name='radio1' " + attrib + "/>\n"
            + "  <input type='reset' name='reset1' " + attrib + "/>\n"
            + "  <button type='reset' name='reset2' " + attrib + "></button>\n"
            + "  <input type='submit' name='submit1' " + attrib + "/>\n"
            + "  <button type='submit' name='submit2' " + attrib + "></button>\n"
            + "  <input type='text' name='textInput1' " + attrib + "/>\n"
            + "  <textarea name='textarea1' " + attrib + ">foo</textarea>\n"
            + "  <input type='color' name='color1' " + attrib + "/>\n"
            + "  <input type='date' name='date1' " + attrib + "/>\n"
            + "  <input type='datetime' name='datetime1' " + attrib + "/>\n"
            + "  <input type='datetime-local' name='datetimeLocal1' " + attrib + "/>\n"
            + "  <input type='time' name='time1' " + attrib + "/>\n"
            + "  <input type='week' name='week1' " + attrib + "/>\n"
            + "  <input type='month' name='month1' " + attrib + "/>\n"
            + "  <input type='number' name='number1' " + attrib + "/>\n"
            + "  <input type='range' name='range1' " + attrib + "/>\n"
            + "  <input type='search' name='search1' " + attrib + "/>\n"
            + "  <input type='email' name='email1' " + attrib + "/>\n"
            + "  <input type='tel' name='tel1' " + attrib + "/>\n"
            + "  <input type='url' name='url1' " + attrib + "/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"abc-abc", "abc-abc", "jkl-abc"})
    @Test
    public void typeChangeNotDirty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var inpt = document.form1.textInput1;\n"
            + "  log(inpt.value + '-' + inpt.defaultValue);\n"

            + "  inpt.type = 'password';\n"
            + "  log(inpt.value + '-' + inpt.defaultValue);\n"

            + "  inpt.value = 'jkl';\n"
            + "  log(inpt.value + '-' + inpt.defaultValue);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textInput1' value='abc'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({"abc-abc", "def-abc", "ghi-abc", "ghi-abc", "jkl-abc"})
    @Test
    public void typeChangeDirty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var inpt = document.form1.textInput1;\n"
            + "  log(inpt.value + '-' + inpt.defaultValue);\n"

            + "  inpt.value = 'def';\n"
            + "  log(inpt.value + '-' + inpt.defaultValue);\n"

            + "  inpt.value = 'ghi';\n"
            + "  log(inpt.value + '-' + inpt.defaultValue);\n"

            + "  inpt.type = 'password';\n"
            + "  log(inpt.value + '-' + inpt.defaultValue);\n"

            + "  inpt.value = 'jkl';\n"
            + "  log(inpt.value + '-' + inpt.defaultValue);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textInput1' value='abc'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void checkedAttribute_Checkbox() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.form1.checkbox1.checked);\n"
            + "  document.form1.checkbox1.checked = true;\n"
            + "  log(document.form1.checkbox1.checked);\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='cheCKbox' name='checkbox1' id='checkbox1' value='foo' />\n"
            + "</form>\n"
            + "<a href='javascript:test()' id='clickme'>click me</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkBox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkBox.isSelected());

        driver.findElement(By.id("clickme")).click();
        verifyTitle2(driver, getExpectedAlerts());
        assertTrue(checkBox.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "false", "false", "true", "false"})
    public void checkedAttribute_Radio() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.form1.radio1[0].checked);\n"
            + "  log(document.form1.radio1[1].checked);\n"
            + "  log(document.form1.radio1[2].checked);\n"
            + "  document.form1.radio1[1].checked = true;\n"
            + "  log(document.form1.radio1[0].checked);\n"
            + "  log(document.form1.radio1[1].checked);\n"
            + "  log(document.form1.radio1[2].checked);\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='radio' name='radio1' id='radioA' value='a' checked='checked'/>\n"
            + "  <input type='RADIO' name='radio1' id='radioB' value='b' />\n"
            + "  <input type='radio' name='radio1' id='radioC' value='c' />\n"
            + "</form>\n"
            + "<a href='javascript:test()' id='clickme'>click me</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement radioA = driver.findElement(By.id("radioA"));
        final WebElement radioB = driver.findElement(By.id("radioB"));
        final WebElement radioC = driver.findElement(By.id("radioC"));
        assertTrue(radioA.isSelected());
        assertFalse(radioB.isSelected());
        assertFalse(radioC.isSelected());

        driver.findElement(By.id("clickme")).click();
        verifyTitle2(driver, getExpectedAlerts());
        assertFalse(radioA.isSelected());
        assertTrue(radioB.isSelected());
        assertFalse(radioC.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true", "false", "true"})
    public void disabledAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.form1.button1.disabled);\n"
            + "  log(document.form1.button2.disabled);\n"
            + "  log(document.form1.button3.disabled);\n"
            + "  document.form1.button1.disabled = true;\n"
            + "  document.form1.button2.disabled = false;\n"
            + "  document.form1.button3.disabled = true;\n"
            + "  log(document.form1.button1.disabled);\n"
            + "  log(document.form1.button2.disabled);\n"
            + "  log(document.form1.button3.disabled);\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='submit' name='button1' value='1'/>\n"
            + "  <input type='submit' name='button2' value='2' disabled/>\n"
            + "  <input type='submit' name='button3' value='3'/>\n"
            + "</form>\n"
            + "<a href='javascript:test()' id='clickme'>click me</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement button1 = driver.findElement(By.name("button1"));
        final WebElement button2 = driver.findElement(By.name("button2"));
        final WebElement button3 = driver.findElement(By.name("button3"));
        assertTrue(button1.isEnabled());
        assertFalse(button2.isEnabled());
        assertTrue(button3.isEnabled());

        driver.findElement(By.id("clickme")).click();
        verifyTitle2(driver, getExpectedAlerts());
        assertFalse(button1.isEnabled());
        assertTrue(button2.isEnabled());
        assertFalse(button3.isEnabled());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  document.form1.textfield1.value = 'blue';\n"
            + "}\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' onsubmit='doTest()'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "  <input type='submit' id='clickMe'/>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        assertEquals(URL_FIRST + "?textfield1=blue", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputSelect_NotDefinedAsPropertyAndFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  document.form1.textfield1.select();\n"
            + "}\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' onsubmit='doTest()'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "  <input type='submit' id='clickMe'/>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        assertEquals(URL_FIRST + "?textfield1=foo", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void thisDotFormInOnClick() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<form name='form1'>\n"
            + "<input type='submit' id='clickMe' onClick=\"this.form.target='_blank'; return false;\">\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.forms[0].target == '');\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        driver.findElement(By.id("clickMe")).click();

        assertEquals("_blank", driver.findElement(By.name("form1")).getDomProperty("target"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true"})
    public void fieldDotForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var f = document.form1;\n"
            + "  log(f == f.mySubmit.form);\n"
            + "  log(f == f.myText.form);\n"
            + "  log(f == f.myPassword.form);\n"
            + "  log(f == document.getElementById('myImage').form);\n"
            + "  log(f == f.myButton.form);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<form name='form1'>\n"
            + "<input type='submit' name='mySubmit'>\n"
            + "<input type='text' name='myText'>\n"
            + "<input type='password' name='myPassword'>\n"
            + "<input type='button' name='myButton'>\n"
            + "<input type='image' src='foo' name='myImage' id='myImage'>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputNameChange() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  document.form1.textfield1.name = 'changed';\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' onsubmit='doTest()'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "  <input type='submit' name='button1' id='clickMe' value='pushme' />\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();

        assertEquals(URL_FIRST + "?changed=foo&button1=pushme", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "from button"})
    public void onChange() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title></title>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='text1' onchange='document.title += this.value'>\n"
            + "  <input name='myButton' type='button' onclick='document.form1.text1.value=\"from button\"'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textinput = driver.findElement(By.name("text1"));
        textinput.sendKeys("foo");
        final WebElement button = driver.findElement(By.name("myButton"));
        button.click();
        assertTitle(driver, getExpectedAlerts()[0]);
        Thread.sleep(100);
        assertEquals(getExpectedAlerts()[1], textinput.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "from button"})
    public void onChangeSetByJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='text1' id='text1'>\n"
            + "  <input name='myButton' type='button' onclick='document.form1.text1.value=\"from button\"'>\n"
            + "</form>\n"
            + "<script>\n"
            + "  document.getElementById('text1').onchange = function(event) { document.title += this.value; };\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textinput = driver.findElement(By.name("text1"));
        textinput.sendKeys("foo");
        final WebElement button = driver.findElement(By.name("myButton"));
        button.click();
        assertTitle(driver, getExpectedAlerts()[0]);

        Thread.sleep(100);
        assertEquals(getExpectedAlerts()[1], textinput.getDomProperty("value"));
    }

    /**
     * Test the default value of a radio and checkbox buttons.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"on", "on"})
    public void defautValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.myForm.myRadio.value);\n"
            + "  log(document.myForm.myCheckbox.value);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='radio' name='myRadio'/>\n"
            + "<input type='checkbox' name='myCheckbox'/>\n"
            + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that changing type doesn't throw.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"text, checkbox, date, datetime-local, month, time, week, color, email, text, submit, "
                        + "radio, hidden, password, image, reset, button, file, number,"
                        + " range, search, tel, url, text, text",
                       "text, checkbox, date, datetime-local, month, time, week, color, email, text, submit, radio, "
                        + "hidden, password, image, reset, button, file, number, range, search, tel, url, text, text" },
            FF = {"text, checkbox, date, datetime-local, text, time, text, color, email, text, submit, radio, hidden, "
                    + "password, image, reset, button, file, number, range, search, tel, url, text, text",
                  "text, checkbox, date, datetime-local, text, time, text, color, "
                    + "email, text, submit, radio, hidden, password, "
                    + "image, reset, button, file, number, range, search, tel, url, text, text"},
            FF_ESR = {"text, checkbox, date, datetime-local, text, time, "
                    + "text, color, email, text, submit, radio, hidden, "
                    + "password, image, reset, button, file, number, range, search, tel, url, text, text",
                      "text, checkbox, date, datetime-local, text, time, text, color, "
                    + "email, text, submit, radio, hidden, password, "
                    + "image, reset, button, file, number, range, search, tel, url, text, text"})
    public void changeType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var input = document.myForm.myInput;\n"
            + "  var types = ['checkbox', 'date', 'datetime-local', 'month', 'time', 'week', 'color'"
                            + ", 'email', 'text', 'submit', 'radio', 'hidden', 'password', 'image', 'reset'"
                            + ", 'button', 'file', 'number', 'range', 'search', 'tel', 'url', 'unknown', 'text'];"
            + "  var result = input.type;\n"
            + "  for(i = 0; i < types.length; i++) {\n"
            + "    try {\n"
            + "      input.type = types[i];\n"
            + "      result = result + ', ' + input.type;\n"
            + "    } catch(e) { result = result + ', error';}\n"
            + "  }\n"
            + "  log(result);\n"
            + "  result = input.type;\n"
            + "  for(i = 0; i < types.length; i++) {\n"
            + "    try {\n"
            + "      input.setAttribute('type', types[i]);\n"
            + "      result = result + ', ' + input.type;\n"
            + "    } catch(e) { result = result + ', error';}\n"
            + "  }\n"
            + "  log(result);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='myForm' action='foo'>\n"
            + "    <input type='text' name='myInput'/>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Inputs have properties not only from there own type.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"button: false, false, function, function, , ",
             "submit: false, false, function, function, submit it!, submit it!",
             "file: false, false, function, function, , ",
             "checkbox: true, true, function, function, , on",
             "radio: true, true, function, function, , on",
             "text: false, false, function, function, , ",
             "password: false, false, function, function, , "})
    public void defaultValues() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<form name='myForm'>\n"
            + "<input type='button' name='myButton'/>\n"
            + "<input type='submit' name='mySubmit' value='submit it!'/>\n"
            + "<input type='file' name='myFile'/>\n"
            + "<input type='checkbox' name='myCheckbox' checked='true'/>\n"
            + "<input type='radio' name='myRadio' checked='true'/>\n"
            + "<input type='text' name='myText'/>\n"
            + "<input type='password' name='myPwd'/>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function details(_oInput) {\n"
            + "  log(_oInput.type + ': '\n"
            + "  + _oInput.checked + ', '\n"
            + "  + _oInput.defaultChecked + ', '\n"
            + "  + ((String(_oInput.click).indexOf('function') != -1) ? 'function' : 'unknown') + ', '\n"
            + "  + ((String(_oInput.select).indexOf('function') != -1) ? 'function' : 'unknown') + ', '\n"
            + "  + _oInput.defaultValue + ', '\n"
            + "  + _oInput.value\n"
            + " );\n"
            + "}\n"
            + "var oForm = document.myForm;\n"
            + "details(oForm.myButton);\n"
            + "details(oForm.mySubmit);\n"
            + "details(oForm.myFile);\n"
            + "details(oForm.myCheckbox);\n"
            + "details(oForm.myRadio);\n"
            + "details(oForm.myText);\n"
            + "details(oForm.myPwd);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text", "null", "hidden", "hidden"})
    public void createInputAndChangeType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var input = document.createElement('INPUT');\n"
            + "  log(input.type);\n"
            + "  log(input.getAttribute('type'));\n"
            + "  input.type = 'hidden';\n"
            + "  log(input.type);\n"
            + "  log(input.getAttribute('type'));\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text", "null", "text", "text"})
    public void createInputAndChangeTypeToText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var input = document.createElement('INPUT');\n"
            + "  log(input.type);\n"
            + "  log(input.getAttribute('type'));\n"
            + "  input.type = 'text';\n"
            + "  log(input.type);\n"
            + "  log(input.getAttribute('type'));\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void buttonOutsideForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<button id='clickme' onclick='alert(123)'>click me</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickme")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Test that field delegates submit to the containing form.
     * @throws Exception if the test fails
     */
    @Test
    public void onChangeCallsFormSubmit() throws Exception {
        final URL urlImage = new URL(URL_SECOND, "img.jpg");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);

            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='test' action='foo'>\n"
            + "    <input name='field1' onchange='submit()'>\n"
            + "    <img src='" + urlImage + "' width='4' height='4'>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(DOCTYPE_HTML + "<html><title>page 2</title><body></body></html>");

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("field1")).sendKeys("bla");
        driver.findElement(By.tagName("img")).click();
        assertTitle(driver, "page 2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "30", "undefined", "30", "30", "30", "40", "50", "string", "number"})
    public void maxLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var input = document.getElementById('text1');\n"
            + "  log(input.maxlength);\n"
            + "  log(input.maxLength);\n"
            + "  log(input.MaxLength);\n"
            + "  log(input.getAttribute('maxlength'));\n"
            + "  log(input.getAttribute('maxLength'));\n"
            + "  log(input.getAttribute('MaxLength'));\n"
            + "  input.setAttribute('MaXlenGth', 40);\n"
            + "  log(input.maxLength);\n"
            + "  input.maxLength = 50;\n"
            + "  log(input.getAttribute('maxlength'));\n"
            + "  log(typeof input.getAttribute('maxLength'));\n"
            + "  log(typeof input.maxLength);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' maxlength='30'/>\n"
            + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "30", "undefined", "30", "30", "30", "40", "50", "string", "number"})
    public void minLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var input = document.getElementById('text1');\n"
            + "  log(input.minlength);\n"
            + "  log(input.minLength);\n"
            + "  log(input.MinLength);\n"
            + "  log(input.getAttribute('minlength'));\n"
            + "  log(input.getAttribute('minLength'));\n"
            + "  log(input.getAttribute('MinLength'));\n"
            + "  input.setAttribute('MiNlenGth', 40);\n"
            + "  log(input.minLength);\n"
            + "  input.minLength = 50;\n"
            + "  log(input.getAttribute('minlength'));\n"
            + "  log(typeof input.getAttribute('minLength'));\n"
            + "  log(typeof input.minLength);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' minlength='30'/>\n"
            + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeMaxLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form>\n"
            + "<input type='text' id='text1' maxlength='5'/>\n"
            + "<input type='password' id='password1' maxlength='6'/>\n"
            + "</form></body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement textField = webDriver.findElement(By.id("text1"));
        textField.sendKeys("123456789");
        assertEquals("12345", textField.getDomProperty("value"));
        textField.sendKeys(Keys.BACK_SPACE);
        textField.sendKeys("7");
        assertEquals("12347", textField.getDomProperty("value"));

        final WebElement passwordField = webDriver.findElement(By.id("password1"));
        passwordField.sendKeys("123456789");
        assertEquals("123456", passwordField.getDomProperty("value"));
        passwordField.sendKeys(Keys.BACK_SPACE);
        passwordField.sendKeys("7");
        assertEquals("123457", passwordField.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeMaxLengthZero() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form>\n"
            + "<input type='text' id='text1' maxlength='0'/>\n"
            + "<input type='password' id='password1' maxlength='0'/>\n"
            + "</form></body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement textField = webDriver.findElement(By.id("text1"));
        textField.sendKeys("123456789");
        assertEquals("", textField.getDomProperty("value"));
        textField.sendKeys("\b7");
        assertEquals("", textField.getDomProperty("value"));

        final WebElement passwordField = webDriver.findElement(By.id("password1"));
        passwordField.sendKeys("123456789");
        assertEquals("", passwordField.getDomProperty("value"));
        passwordField.sendKeys("\b7");
        assertEquals("", passwordField.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeMaxLengthAndBlanks() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form>\n"
            + "<input type='text' id='text1' maxlength=' 2 '/>\n"
            + "<input type='password' id='password1' maxlength='    4  '/>\n"
            + "</form></body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement textField = webDriver.findElement(By.id("text1"));
        textField.sendKeys("123456789");
        assertEquals("12", textField.getDomProperty("value"));
        textField.sendKeys(Keys.BACK_SPACE);
        textField.sendKeys("7");
        assertEquals("17", textField.getDomProperty("value"));

        final WebElement passwordField = webDriver.findElement(By.id("password1"));
        passwordField.sendKeys("123456789");
        assertEquals("1234", passwordField.getDomProperty("value"));
        passwordField.sendKeys(Keys.BACK_SPACE);
        passwordField.sendKeys("7");
        assertEquals("1237", passwordField.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text TeXt", "password PassWord", "hidden Hidden",
             "checkbox CheckBox", "radio rAdiO", "file FILE", "checkbox CHECKBOX"})
    public void typeCase() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var t = document.getElementById('aText');\n"
            + "  log(t.type + ' ' + t.getAttribute('type'));\n"
            + "  var p = document.getElementById('aPassword');\n"
            + "  log(p.type + ' ' + p.getAttribute('type'));\n"
            + "  var h = document.getElementById('aHidden');\n"
            + "  log(h.type + ' ' + h.getAttribute('type'));\n"
            + "  var cb = document.getElementById('aCb');\n"
            + "  log(cb.type + ' ' + cb.getAttribute('type'));\n"
            + "  var r = document.getElementById('aRadio');\n"
            + "  log(r.type + ' ' + r.getAttribute('type'));\n"
            + "  var f = document.getElementById('aFile');\n"
            + "  log(f.type + ' ' + f.getAttribute('type'));\n"

            + "  try {\n"
            + "    f.type = 'CHECKBOX';\n"
            + "    log(f.type + ' ' + f.getAttribute('type'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form action='foo'>\n"
            + "    <input Type='TeXt' id='aText' value='some test'>\n"
            + "    <input tYpe='PassWord' id='aPassword' value='some test'>\n"
            + "    <input tyPe='Hidden' id='aHidden' value='some test'>\n"
            + "    <input typE='CheckBox' id='aCb'>\n"
            + "    <input TYPE='rAdiO' id='aRadio'>\n"
            + "    <input type='FILE' id='aFile'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text-text", "text- password", "text-hidden ", "text-checkbox ", "text-\\tradio"})
    public void typeTrim() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "function log(msg) { window.document.title += msg.replace('\t', '\\\\t') + '§';}\n"
            + "function test() {\n"
            + "  var t = document.getElementById('aText');\n"
            + "  log(t.type + '-' + t.getAttribute('type'));\n"
            + "  var p = document.getElementById('aPassword');\n"
            + "  log(p.type + '-' + p.getAttribute('type'));\n"
            + "  var h = document.getElementById('aHidden');\n"
            + "  log(h.type + '-' + h.getAttribute('type'));\n"
            + "  var cb = document.getElementById('aCb');\n"
            + "  log(cb.type + '-' + cb.getAttribute('type'));\n"
            + "  var r = document.getElementById('aRadio');\n"
            + "  log(r.type + '-' + r.getAttribute('type'));\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onLoad='test()'>\n"
            + "  <form action='foo'>\n"
            + "    <input type='text' id='aText' value='some test'>\n"
            + "    <input type=' password' id='aPassword' value='some test'>\n"
            + "    <input type='hidden ' id='aHidden' value='some test'>\n"
            + "    <input type='checkbox ' id='aCb'>\n"
            + "    <input type='\tradio' id='aRadio'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void readOnly() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.readOnly);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='some test' readonly='false'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void readOnlyInputFile() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    log(input.readOnly);\n"
            + "    input = document.getElementById('myReadonlyInput');\n"
            + "    log(input.readOnly);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' type='file' value='some test'>\n"
            + "  <input id='myReadonlyInput' type='file' value='some test' readonly='false'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"left", "right", "bottom", "middle", "top", "wrong", ""})
    public void getAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <form>\n"
            + "    <input id='i1' align='left' />\n"
            + "    <input id='i2' align='right' />\n"
            + "    <input id='i3' align='bottom' />\n"
            + "    <input id='i4' align='middle' />\n"
            + "    <input id='i5' align='top' />\n"
            + "    <input id='i6' align='wrong' />\n"
            + "    <input id='i7' />\n"
            + "  </form>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 7; i++) {\n"
            + "    log(document.getElementById('i' + i).align);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CenTer", "8", "foo", "left", "right", "bottom", "middle", "top"})
    public void setAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <form>\n"
            + "    <input id='i1' type='text' align='left' value=''/>\n"
            + "  </form>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "    log(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('i1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'bottom');\n"
            + "  setAlign(elem, 'middle');\n"
            + "  setAlign(elem, 'top');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "A", "a", "A", "a8", "8Afoo", "8", "@"})
    public void accessKey() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <input id='a1'>\n"
            + "  <input id='a2' accesskey='A'>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var a1 = document.getElementById('a1');\n"
            + "    var a2 = document.getElementById('a2');\n"
            + "    log(a1.accessKey);\n"
            + "    log(a2.accessKey);\n"

            + "    a1.accessKey = 'a';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = 'A';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = 'a8';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = '8Afoo';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = '8';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = '@';\n"
            + "    log(a1.accessKey);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"test", "4", "42", "2", "[object HTMLInputElement]", "25"})
    public void getAttributeAndSetValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'test';\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"

            + "        t.value = 42;\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"

            + "        t.value = document.getElementById('t');\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <input id='t'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "4", "", "0"})
    public void getAttributeAndSetValueNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'null';\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"

            + "        t.value = null;\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <input id='t'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "2", "7"})
    public void selectionRange() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  log(input.selectionStart);\n"
            + "  log(input.selectionEnd);\n"

            + "  input.setSelectionRange(2, 7);\n"
            + "  log(input.selectionStart);\n"
            + "  log(input.selectionEnd);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='some test'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("onsubmit")
    public void submitNonRequired() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function submitMe() {\n"
            + "  alert('onsubmit');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <form onsubmit='submitMe()'>\n"
            + "    <input id='myInput' name='myName' value=''>\n"
            + "    <input id='mySubmit' type='submit'>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("mySubmit")).click();
        verifyAlerts(driver, getExpectedAlerts());
        Thread.sleep(DEFAULT_WAIT_TIME.toMillis() / 10);
        assertTrue("Url '" + driver.getCurrentUrl() + "' does not contain 'myName'",
                driver.getCurrentUrl().contains("myName"));

        // because we have a new page
        assertTrue(getCollectedAlerts(driver, 1).isEmpty());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "§§URL§§"})
    public void submitRequired() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function submitMe() {\n"
            + "  alert('onsubmit');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <form onsubmit='submitMe()'>\n"
            + "    <input id='myInput' name='myName' value='' required>\n"
            + "    <input id='mySubmit' type='submit'>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("mySubmit")).click();

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], Integer.toString(getMockWebConnection().getRequestCount()));
        assertEquals(getExpectedAlerts()[1], driver.getCurrentUrl());
        assertTrue(getCollectedAlerts(driver).isEmpty());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void checkValidity() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function checkStatus() {\n"
            + "  var elem = document.getElementById('myInput');\n"
            + "  alert(elem.checkValidity());\n"
            + "}\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <input id='myInput' name='myName' required>\n"
            + "    <input id='mySubmit' type='submit'>\n"
            + "  </form>\n"
            + "  <button id='myButton' onclick='checkStatus()'>Check Status</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();
        verifyAlerts(driver, getExpectedAlerts()[0]);

        driver.findElement(By.id("myInput")).sendKeys("something");
        driver.findElement(By.id("myButton")).click();
        verifyAlerts(driver, getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§?myName=abcdefg")
    public void maxLengthJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function updateValue() {\n"
            + "  document.getElementById('myInput').value = 'abcdefg';\n"
            + "}\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <input id='myInput' name='myName' maxlength='2'>\n"
            + "    <input id='mySubmit' type='submit'>\n"
            + "  </form>\n"
            + "  <button id='myButton' onclick='updateValue()'>Update Value</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myButton")).click();
        assertEquals("abcdefg", driver.findElement(By.id("myInput")).getDomProperty("value"));
        driver.findElement(By.id("mySubmit")).click();

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§?myName=ab")
    public void maxLength2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <input id='myInput' name='myName' maxlength='2'>\n"
            + "    <input id='mySubmit' type='submit'>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myInput")).sendKeys("abcdefg");
        assertEquals("ab", driver.findElement(By.id("myInput")).getDomProperty("value"));

        driver.findElement(By.id("mySubmit")).click();

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"30", "undefined", "30", "30", "40", "50", "string", "string"})
    public void min() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var input = document.getElementById('text1');\n"
            + "  log(input.min);\n"
            + "  log(input.Min);\n"
            + "  log(input.getAttribute('min'));\n"
            + "  log(input.getAttribute('Min'));\n"
            + "  input.setAttribute('MiN', 40);\n"
            + "  log(input.min);\n"
            + "  input.min = 50;\n"
            + "  log(input.getAttribute('min'));\n"
            + "  log(typeof input.getAttribute('min'));\n"
            + "  log(typeof input.min);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' min='30'/>\n"
            + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"30", "undefined", "30", "30", "40", "50", "string", "string"})
    public void max() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var input = document.getElementById('text1');\n"
            + "  log(input.max);\n"
            + "  log(input.Max);\n"
            + "  log(input.getAttribute('max'));\n"
            + "  log(input.getAttribute('Max'));\n"
            + "  input.setAttribute('MaX', 40);\n"
            + "  log(input.max);\n"
            + "  input.max = 50;\n"
            + "  log(input.getAttribute('max'));\n"
            + "  log(typeof input.getAttribute('max'));\n"
            + "  log(typeof input.max);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' max='30'/>\n"
            + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "2", "1", "2", "1", "1"})
    public void labels() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      debug(document.getElementById('e1'));\n"
            + "      debug(document.getElementById('e2'));\n"
            + "      debug(document.getElementById('e3'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      var labels = document.getElementById('e4').labels;\n"
            + "      document.body.removeChild(document.getElementById('l4'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      log(labels ? labels.length : labels);\n"
            + "    }\n"
            + "    function debug(e) {\n"
            + "      log(e.labels ? e.labels.length : e.labels);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='e1'><br>\n"
            + "  <label>something <label> click here <input id='e2'></label></label><br>\n"
            + "  <label for='e3'> and here</label>\n"
            + "  <input id='e3'><br>\n"
            + "  <label id='l4' for='e4'> what about</label>\n"
            + "  <label> this<input id='e4'></label><br>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"173", "17", "173", "17", "13", "13", "13", "13"},
            FF = {"157", "18", "157", "18", "14", "14", "14", "14"},
            FF_ESR = {"161", "18", "161", "18", "14", "14", "14", "14"})
    public void defaultClientWidthHeight() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var elem = document.getElementById('txt');\n"
                + "    log(elem.clientWidth);\n"
                + "    log(elem.clientHeight);\n"
                + "    elem = document.getElementById('pw');\n"
                + "    log(elem.clientWidth);\n"
                + "    log(elem.clientHeight);\n"
                + "    elem = document.getElementById('chkbx');\n"
                + "    log(elem.clientWidth);\n"
                + "    log(elem.clientHeight);\n"
                + "    elem = document.getElementById('radio');\n"
                + "    log(elem.clientWidth);\n"
                + "    log(elem.clientHeight);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<form>\n"
                + "  <input type='text' id='txt'>\n"
                + "  <input type='password' id='pw'>\n"
                + "  <input type='checkbox' id='chkbx'/>\n"
                + "  <input type='radio' id='radio'/>\n"
                + "</form>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLFormElement]")
    public void form() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <input type='text' id='a'>\n"
            + "  </form>"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    log(document.getElementById('a').form);\n"
            + "  </script>"
            + "</body>"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverButton() throws Exception {
        mouseOver("<input id='tester' type='button' onmouseover='dumpEvent(event);' value='HtmlUnit'>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverButtonDisabled() throws Exception {
        mouseOver("<input id='tester' type='button' onmouseover='dumpEvent(event);' value='HtmlUnit' disabled >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverSubmit() throws Exception {
        mouseOver("<input id='tester' type='submit' onmouseover='dumpEvent(event);' >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverSubmitDisabled() throws Exception {
        mouseOver("<input id='tester' type='submit' onmouseover='dumpEvent(event);' disabled >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverReset() throws Exception {
        mouseOver("<input id='tester' type='reset' onmouseover='dumpEvent(event);' >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverResetDisabled() throws Exception {
        mouseOver("<input id='tester' type='reset' onmouseover='dumpEvent(event);' disabled >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverText() throws Exception {
        mouseOver("<input id='tester' type='text' onmouseover='dumpEvent(event);' value='HtmlUnit'>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverTextDisabled() throws Exception {
        mouseOver("<input id='tester' type='text' onmouseover='dumpEvent(event);' value='HtmlUnit' disabled >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverPassword() throws Exception {
        mouseOver("<input id='tester' type='password' onmouseover='dumpEvent(event);' value='HtmlUnit'>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverPasswordDisabled() throws Exception {
        mouseOver("<input id='tester' type='password' onmouseover='dumpEvent(event);' value='HtmlUnit' disabled >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverFile() throws Exception {
        mouseOver("<input id='tester' type='file' onmouseover='dumpEvent(event);'>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverFileDisabled() throws Exception {
        mouseOver("<input id='tester' type='file' onmouseover='dumpEvent(event);' disabled >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverCheckbox() throws Exception {
        mouseOver("<input id='tester' type='checkbox' onmouseover='dumpEvent(event);' value='HtmlUnit'>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverCheckboxDisabled() throws Exception {
        mouseOver("<input id='tester' type='checkbox' onmouseover='dumpEvent(event);' value='HtmlUnit' disabled >");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverRadio() throws Exception {
        mouseOver("<input id='tester' type='radio' onmouseover='dumpEvent(event);' value='HtmlUnit'>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverRadioDisabled() throws Exception {
        mouseOver("<input id='tester' type='radio' onmouseover='dumpEvent(event);' value='HtmlUnit' disabled >");
    }

    private void mouseOver(final String element) throws Exception {
        final String html = DOCTYPE_HTML
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
            + "      document.title+= msg;\n"
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Test-Test-Test", "text1-text1-text1"})
    public void getAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  input.setAttribute('value', 'text1');\n"
            + "  log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='Test'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Test-Test-Test", "text1-text1-text1"})
    public void getAttributeCase() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  input.setAttribute('vALue', 'text1');\n"
            + "  log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='Test'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Test-Test-Test", "Test-Test-Test"})
    public void setAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  input.setAttribute('autocomplete', 'text1');\n"
            + "  log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='Test'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("finish")
    public void setAttributeFromJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  input.setAttribute('value', 'text1');\n"
            + "  log('finish');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='Test' onchange=\"log('changed')\">\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void sendKeys() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <input id='myInput' value='Test' onchange=\"alert('changed')\">\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("myInput"));
        element.sendKeys("abc");
        verifyAlerts(driver);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clear() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "  <input id='myInput' value='Test' onchange=\"log('changed')\">\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("myInput"));
        element.clear();
        verifyTitle2(driver, "changed");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidate() throws Exception {
        willValidate("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false", "false"})
    public void willValidateButton() throws Exception {
        willValidate("type='button'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateCheckbox() throws Exception {
        willValidate("type='checkbox'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateColor() throws Exception {
        willValidate("type='color'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateDate() throws Exception {
        willValidate("type='date'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateDatetime() throws Exception {
        willValidate("type='datetime'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateDatetimelocal() throws Exception {
        willValidate("type='datetime-local'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateEmail() throws Exception {
        willValidate("type='email'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateFile() throws Exception {
        willValidate("type='file'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false", "false"})
    public void willValidateHidden() throws Exception {
        willValidate("type='hidden'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "false", "false", "false", "false"},
            FF = {"true", "false", "true", "false", "true"},
            FF_ESR = {"true", "false", "true", "false", "true"})
    public void willValidateImage() throws Exception {
        willValidate("type='image'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateMonth() throws Exception {
        willValidate("type='month'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateNumber() throws Exception {
        willValidate("type='number'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidatePassword() throws Exception {
        willValidate("type='password'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateRadio() throws Exception {
        willValidate("type='radio'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateRange() throws Exception {
        willValidate("type='range'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false", "false"})
    public void willValidateReset() throws Exception {
        willValidate("type='reset'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateSearch() throws Exception {
        willValidate("type='search'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateSubmit() throws Exception {
        willValidate("type='submit'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateTel() throws Exception {
        willValidate("type='tel'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateText() throws Exception {
        willValidate("type='text'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateTime() throws Exception {
        willValidate("type='time'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateUrl() throws Exception {
        willValidate("type='url'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidateWeek() throws Exception {
        willValidate("type='week'");
    }

    private void willValidate(final String type) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "      log(document.getElementById('i2').willValidate);\n"
                + "      log(document.getElementById('i3').willValidate);\n"
                + "      log(document.getElementById('i4').willValidate);\n"
                + "      log(document.getElementById('i5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <input id='i1' " + type + ">\n"
                + "    <input id='i2' " + type + "disabled>\n"
                + "    <input id='i3' " + type + "hidden>\n"
                + "    <input id='i4' " + type + "readonly>\n"
                + "    <input id='i5' " + type + "style='display: none'>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void willValidateFormNoValidate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form novalidate>\n"
                + "    <input id='i1'>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "true", "false"})
    public void formNoValidate() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.createElement('input');\n"
            + "    log(i.formNoValidate);\n"

            + "    i.formNoValidate = '';\n"
            + "    log(i.formNoValidate);\n"

            + "    i.formNoValidate = 'yes';\n"
            + "    log(i.formNoValidate);\n"

            + "    i.removeAttribute('formNoValidate');\n"
            + "    log(i.formNoValidate);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"original", "from js", "original", "changed"})
    public void reset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    let inpt = document.getElementById('theInput');\n"
            + "    let frm = document.getElementById('theForm');\n"

            + "    log(inpt.value);\n"

            + "    inpt.value = 'from js';\n"
            + "    log(inpt.value);\n"

            + "    frm.reset();\n"
            + "    log(inpt.value);\n"

            // Since the dirty flag should now be false, changing the content attribute
            // should update the live value again.
            + "    inpt.setAttribute('value', 'changed');\n"
            + "    log(inpt.value);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<form id=\"theForm\">\n"
            + "  <input id='theInput' type='text' value='original'>\r\n"
            + "</form>\r\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Setting .value to the value it already holds must not move the selection
     * (per spec, comparing against oldAPIValue before the assignment).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void settingValueToSameValue_selectionUnchanged() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  input.selectionStart = 1;\n"
            + "  input.selectionEnd = 2;\n"
            + "  input.value = 'hello';\n"
            + "  log(input.selectionStart);\n"
            + "  log(input.selectionEnd);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='hello'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Even though a same-value assignment leaves the selection
     * untouched, the dirty flag must still flip to true. Proven indirectly: if
     * the flag were still false, a later change to the 'value' content attribute
     * would still be reflected in .value; if the flag correctly flipped true,
     * that later attribute change must be ignored.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void settingValueToSameValue_stillSetsDirtyFlag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  input.value = 'hello';\n"
            + "  input.setAttribute('value', 'changed');\n"
            + "  log(input.value);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='hello'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Selection must be preserved across MULTIPLE consecutive same-value
     * assignments, not just the first one.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void settingValueToSameValueTwice_selectionPreservedBothTimes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  input.value = 'hello';\n"
            + "  input.selectionStart = 1;\n"
            + "  input.selectionEnd = 2;\n"
            + "  input.value = 'hello';\n"
            + "  log(input.selectionStart);\n"
            + "  log(input.selectionEnd);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='hello'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The "old value" comparison must be against the CURRENT raw value, not the
     * page's originally-parsed value attribute. After dirtying to 'X', setting
     * .value back to the ORIGINAL text ('hello') is a change relative to the
     * current raw value ('X'), so the cursor MUST move -- even though 'hello'
     * matches the original value attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "5"})
    public void settingValueBackToOriginalText_cursorMovesBecauseCurrentValueDiffers() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  input.value = 'X';\n"
            + "  input.selectionStart = 0;\n"
            + "  input.selectionEnd = 0;\n"
            + "  input.value = 'hello';\n"
            + "  log(input.selectionStart);\n"
            + "  log(input.selectionEnd);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='hello'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Assigning the empty string to an already-empty value should
     * also be treated as "no change" -- no cursor move, but still dirties the
     * flag.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void settingEmptyValueToAlreadyEmptyValue_selectionUnchangedButDirty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  input.value = '';\n"
            + "  input.setAttribute('value', 'late');\n"
            + "  log(input.value);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value=''>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The ordinary changing-value path must still
     * move the cursor to the end -- guards against the same-value fix
     * accidentally suppressing the cursor move for genuine changes too.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7", "7"})
    public void settingValueToDifferentValue_cursorMovesToEnd() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('myInput');\n"
            + "  input.selectionStart = 0;\n"
            + "  input.selectionEnd = 0;\n"
            + "  input.value = 'goodbye';\n"
            + "  log(input.selectionStart);\n"
            + "  log(input.selectionEnd);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='hello'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * After reset(), selectionStart and selectionEnd must be
     * consistent with each other (start <= end), not left in an inverted state.
     * Targets the current HtmlSelectableTextInput.reset() implementation, which
     * only calls setSelectionEnd(0) without also resetting selectionStart --
     * after the preceding setValue() call moved selectionStart to the END of
     * the default value, this leaves start > end unless something else clamps
     * it.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"8", "8"},
            FF = {"1", "2"},
            FF_ESR = {"1", "2"})
    @HtmlUnitNYI(FF = {"8", "8"},
            FF_ESR = {"8", "8"})
    public void resetSetsSelectionStartAndEndConsistently() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('theInput');\n"
            + "  var frm = document.getElementById('theForm');\n"
            + "  input.value = 'dirtied';\n"
            + "  input.selectionStart = 1;\n"
            + "  input.selectionEnd = 2;\n"
            + "  frm.reset();\n"
            + "  log(input.selectionStart);\n"
            + "  log(input.selectionEnd);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<form id='theForm'>\n"
            + "  <input id='theInput' type='text' value='original'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * If the 'value' content
     * attribute is changed by script WHILE .value is dirty (so the
     * dirty-flag guard suppresses the sync), a subsequent reset() must pick up
     * whatever the attribute CURRENTLY holds -- not the page's originally-parsed
     * value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("updated")
    public void resetReflectsCurrentAttributeNotOriginalParsedValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var input = document.getElementById('theInput');\n"
            + "  var frm = document.getElementById('theForm');\n"
            + "  input.value = 'X';\n"
            + "  input.setAttribute('value', 'updated');\n"
            + "  frm.reset();\n"
            + "  log(input.value);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<form id='theForm'>\n"
            + "  <input id='theInput' type='text' value='original'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Reset triggered via a real click on a &lt;input type=reset&gt; button
     * should behave identically to a scripted form.reset() call -- the existing
     * reset() test only exercises the scripted path.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("original")
    public void resetViaResetButtonClick_matchesScriptedReset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<form id='theForm'>\n"
            + "  <input id='theInput' type='text' value='original'>\n"
            + "  <input id='resetBtn' type='reset'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("theInput"));
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        input.sendKeys("-typed");

        driver.findElement(By.id("resetBtn")).click();

        final String value = input.getDomProperty("value");
        assertEquals(getExpectedAlerts()[0], value);
    }

    /**
     * The reset() called on a field whose value was NEVER
     * changed (dirty flag never set) -- with the setValue() fix applied, the
     * cursor-move there is conditional on old-value != new-value, so when
     * reset() restores the SAME value that was already showing, that condition
     * is false and the cursor-move is skipped entirely. This checks what
     * selectionStart/selectionEnd end up being in that case: whatever they were
     * BEFORE reset() (since nothing moves them), which may or may not match what
     * real browsers do -- real browsers might unconditionally reposition the
     * cursor as part of the reset algorithm itself, independent of the value
     * setter's own conditional logic.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "5", "original", "5", "5"})
    public void resetOnNeverDirtiedInput_selectionPosition() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<form id='theForm'>\n"
            + "  <input id='theInput' type='text' value='original'>\n"
            + "  <input id='resetBtn' type='reset'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("theInput"));

        // click into the field and move the caret somewhere in the middle,
        // WITHOUT ever changing its value (no sendKeys/typing, no .value set)
        input.click();
        input.sendKeys(Keys.ARROW_LEFT, Keys.ARROW_LEFT, Keys.ARROW_LEFT);

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final Long selStartBeforeReset = (Long) js.executeScript(
                "return arguments[0].selectionStart;", input);
        final Long selEndBeforeReset = (Long) js.executeScript(
                "return arguments[0].selectionEnd;", input);

        driver.findElement(By.id("resetBtn")).click();

        final Long selStartAfterReset = (Long) js.executeScript(
                "return arguments[0].selectionStart;", input);
        final Long selEndAfterReset = (Long) js.executeScript(
                "return arguments[0].selectionEnd;", input);
        final String valueAfterReset = input.getDomProperty("value");

        assertEquals(getExpectedAlerts()[0], String.valueOf(selStartBeforeReset));
        assertEquals(getExpectedAlerts()[1], String.valueOf(selEndBeforeReset));
        assertEquals(getExpectedAlerts()[2], valueAfterReset);
        assertEquals(getExpectedAlerts()[3], String.valueOf(selStartAfterReset));
        assertEquals(getExpectedAlerts()[4], String.valueOf(selEndAfterReset));
    }

    /**
     * Same "never touched" scenario, but with an explicit
     * mid-field selection RANGE (not just a collapsed caret) set beforehand via
     * script, to check whether reset() collapses/repositions a real selection
     * range too, or only affects a collapsed caret position.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"original", "2", "5"})
    public void resetOnNeverDirtiedInputWithExplicitSelectionRange_selectionPosition() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<form id='theForm'>\n"
            + "  <input id='theInput' type='text' value='original'>\n"
            + "  <input id='resetBtn' type='reset'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("theInput"));

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setSelectionRange(2, 5);", input);

        driver.findElement(By.id("resetBtn")).click();

        final Long selStartAfterReset = (Long) js.executeScript(
                "return arguments[0].selectionStart;", input);
        final Long selEndAfterReset = (Long) js.executeScript(
                "return arguments[0].selectionEnd;", input);
        final String valueAfterReset = input.getDomProperty("value");

        assertEquals(getExpectedAlerts()[0], valueAfterReset);
        assertEquals(getExpectedAlerts()[1], String.valueOf(selStartAfterReset));
        assertEquals(getExpectedAlerts()[2], String.valueOf(selEndAfterReset));
    }

    /**
     * Confirms that typing into a text input (real keyboard simulation via
     * sendKeys, not a scripted .value assignment) updates .value but leaves the
     * 'value' content attribute untouched.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-typed", "seed"})
    public void typingUpdatesValueButNotAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <input id='foo' value='seed'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("foo"));
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        input.sendKeys("-typed");

        final String value = input.getDomProperty("value");
        final String attribute = input.getDomAttribute("value");

        assertEquals(getExpectedAlerts()[0], value);
        assertEquals(getExpectedAlerts()[1], attribute);
    }

    /**
     * After dirtying .value via real typing, a clone must preserve the
     * dirtied value rather than reverting to the (cloned) 'value' attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-typed", "seed"})
    public void cloneNodeAfterTyping_cloneRetainsDirtiedValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <input id='foo' value='seed'>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("foo"));
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        input.sendKeys("-typed");

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final String cloneValue = (String) js.executeScript(
                "return arguments[0].cloneNode(true).value;", input);
        final String cloneAttribute = (String) js.executeScript(
                "return arguments[0].cloneNode(true).getAttribute('value');", input);

        assertEquals(getExpectedAlerts()[0], cloneValue);
        assertEquals(getExpectedAlerts()[1], cloneAttribute);
    }

    /**
     * After cloning a still-CLEAN input and attaching the clone to the document,
     * typing independently into the original and the clone must not
     * cross-contaminate either one's value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"original-text", "clone-text"})
    public void cloneNodeThenIndependentTyping_noCrossContamination() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <input id='foo' value=''>\n"
            + "  <script>\n"
            + "    var c = document.getElementById('foo').cloneNode(true);\n"
            + "    c.id = 'clone';\n"
            + "    document.body.appendChild(c);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement original = driver.findElement(By.id("foo"));
        final WebElement clone = driver.findElement(By.id("clone"));

        original.click();
        original.sendKeys("original-text");
        clone.click();
        clone.sendKeys("clone-text");

        final String originalValue = original.getDomProperty("value");
        final String cloneValue = clone.getDomProperty("value");

        assertEquals(getExpectedAlerts()[0], originalValue);
        assertEquals(getExpectedAlerts()[1], cloneValue);
    }

    /**
     * Resetting a clone (via a real click on its OWN form's reset button) must
     * only affect the clone -- the original, sitting in a separate form, must be
     * untouched.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-original-dirtied", "seed"})
    public void cloneNodeThenResetOnCloneOnly_doesNotAffectOriginal() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <input id='foo' value='seed'>\n"
            + "    <input id='resetBtn1' type='reset'>\n"
            + "  </form>\n"
            + "  <form id='form2'>\n"
            + "    <input id='resetBtn2' type='reset'>\n"
            + "  </form>\n"
            + "  <script>\n"
            + "    var c = document.getElementById('foo').cloneNode(true);\n"
            + "    c.id = 'clone';\n"
            + "    document.getElementById('form2').appendChild(c);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement original = driver.findElement(By.id("foo"));
        final WebElement clone = driver.findElement(By.id("clone"));

        original.click();
        original.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        original.sendKeys("-original-dirtied");

        clone.click();
        clone.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        clone.sendKeys("-clone-dirtied");

        // reset only form2, which contains the clone
        driver.findElement(By.id("resetBtn2")).click();

        final String originalValue = original.getDomProperty("value");
        final String cloneValue = clone.getDomProperty("value");

        assertEquals(getExpectedAlerts()[0], originalValue);
        assertEquals(getExpectedAlerts()[1], cloneValue);
    }

    /**
     * An editable text input with a custom validity message must
     * report checkValidity() false.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "false", "false"})
    public void setCustomValidityOnEditableInput_isInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.setCustomValidity('some error');\n"
            + "    log(i.willValidate);\n"
            + "    log(i.validity.customError);\n"
            + "    log(i.validity.valid);\n"
            + "    log(i.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A DISABLED input with a custom validity message must still
     * report checkValidity() true -- disabled bars it from constraint
     * validation entirely, so the custom error must not surface through
     * checkValidity(), even though willValidate is already known to be false
     * for this case.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void setCustomValidityOnDisabledInput_notInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.setCustomValidity('some error');\n"
            + "    log(i.willValidate);\n"
            + "    log(i.validity.customError);\n"
            + "    log(i.validity.valid);\n"
            + "    log(i.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' disabled>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Same as above but for READONLY instead of disabled -- readonly
     * also bars a text-type input from constraint validation per spec.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void setCustomValidityOnReadonlyInput_notInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.setCustomValidity('some error');\n"
            + "    log(i.willValidate);\n"
            + "    log(i.validity.customError);\n"
            + "    log(i.validity.valid);\n"
            + "    log(i.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' readonly>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Same as above but for type='hidden' -- a hidden-type input is
     * barred from constraint validation via a THIRD, distinct mechanism (its
     * own type state), not disabled or readonly. willValidateHidden() already
     * confirms willValidate is false for this type; this confirms
     * checkValidity() is consistent with that, given the routing bug found
     * elsewhere was specifically about checkValidity() not always consulting
     * willValidate() even when willValidate() itself was correct.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void setCustomValidityOnHiddenInput_notInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.setCustomValidity('some error');\n"
            + "    log(i.willValidate);\n"
            + "    log(i.validity.customError);\n"
            + "    log(i.validity.valid);\n"
            + "    log(i.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='hidden'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A required, empty text input is genuinely invalid (valueMissing)
     * when editable, but must report checkValidity() true if ALSO disabled --
     * confirms the disabled-barring check takes priority over an actual
     * constraint violation, not just over a custom validity message.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void requiredEmptyDisabledInput_checkValidityTrue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var editable = document.getElementById('editable');\n"
            + "    var disabled = document.getElementById('disabled');\n"
            + "    log(editable.checkValidity());\n"
            + "    log(disabled.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='editable' type='text' required>\n"
            + "    <input id='disabled' type='text' required disabled>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Same as above, but for readonly instead of disabled.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void requiredEmptyReadonlyInput_checkValidityTrue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var editable = document.getElementById('editable');\n"
            + "    var readonly = document.getElementById('readonly');\n"
            + "    log(editable.checkValidity());\n"
            + "    log(readonly.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='editable' type='text' required>\n"
            + "    <input id='readonly' type='text' required readonly>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Clearing a previously-set custom validity message (empty string) must
     * restore validity for an editable input -- confirms setCustomValidity is
     * reversible, not just settable.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "true"})
    public void clearCustomValidity_restoresValid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.setCustomValidity('some error');\n"
            + "    log(i.validity.valid);\n"
            + "    i.setCustomValidity('');\n"
            + "    log(i.validity.customError);\n"
            + "    log(i.validity.valid);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The validationMessage should reflect the custom validity message for a
     * validation-participating (editable) input, and should be empty for a
     * barred-from-validation (disabled) one, regardless of a custom message
     * being set.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"editable error", ""})
    public void validationMessageReflectsCustomValidityWhereApplicable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var editable = document.getElementById('editable');\n"
            + "    var disabled = document.getElementById('disabled');\n"
            + "    editable.setCustomValidity('editable error');\n"
            + "    disabled.setCustomValidity('disabled error');\n"
            + "    log(editable.validationMessage);\n"
            + "    log(disabled.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='editable' type='text'>\n"
            + "    <input id='disabled' type='text' disabled>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The reportValidity() is untested anywhere in this file -- basic coverage that
     * it returns the same boolean as checkValidity() for an input with a custom
     * validity message set.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void reportValidityMatchesCheckValidity() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.setCustomValidity('some error');\n"
            + "    log(i.checkValidity());\n"
            + "    log(i.reportValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Basic .validity object coverage independent of setCustomValidity() --
     * only the bare checkValidity() boolean is tested anywhere currently; this
     * confirms .validity.valueMissing and .validity.valid reflect a genuine
     * (non-custom) constraint violation correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "false", "true"})
    public void validityValueMissingForRequiredEmptyInput() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.valueMissing);\n"
            + "    log(i.validity.valid);\n"
            + "    i.value = 'something';\n"
            + "    log(i.validity.valueMissing);\n"
            + "    log(i.validity.valid);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' required>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"invalid fired", "false"})
    public void elementCheckValidityFiresInvalidEventDirectly() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i1 = document.getElementById('i1');\n"
            + "    i1.addEventListener('invalid', function() {\n"
            + "      log('invalid fired');\n"
            + "    });\n"
            + "    log(i1.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i1' value='' required>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void checkValidityDoesNotFireInvalidWhenActuallyValid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.addEventListener('invalid', function() { log('unexpected invalid fired'); });\n"
            + "    log(i.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='filled' required>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Companion negative check for a BARRED (disabled) control: even though a
     * disabled+required+empty input has an underlying constraint violation,
     * 'invalid' must not fire since it's excluded from validation entirely.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void checkValidityDoesNotFireInvalidOnDisabledControl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.addEventListener('invalid', function() { log('unexpected invalid fired'); });\n"
            + "    log(i.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='' required disabled>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"invalid fired", "false"})
    public void inputReportValidityFiresInvalidEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.addEventListener('invalid', function() { log('invalid fired'); });\n"
            + "    log(i.reportValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='' required>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "Please fill out this field."})
    public void validationMessage_valueMissing_input() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.valueMissing);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='' required>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "Please select an item in the list."})
    public void validationMessage_valueMissing_select() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var s = document.getElementById('s');\n"
            + "    log(s.validity.valueMissing);\n"
            + "    log(s.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <select id='s' required>"
            + "      <option value=''></option>"
            + "      <option value='a'>a</option>"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "Please fill out this field."})
    public void validationMessage_valueMissing_textarea() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    log(t.validity.valueMissing);\n"
            + "    log(t.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t' required></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "Please match the requested format."})
    public void validationMessage_patternMismatch() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.patternMismatch);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='abc' pattern='[0-9]+'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
            DEFAULT = "Please match the requested format.",
            FF = "Please match the requested format: digits only.",
            FF_ESR = "Please match the requested format: digits only.")
    @HtmlUnitNYI(FF = "Please match the requested format.",
            FF_ESR = "Please match the requested format.")
    public void validationMessage_patternMismatch_withTitle() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='abc' pattern='[0-9]+' title='digits only'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", ""})
    public void validationMessage_tooLong_input() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.value = '12345';\n"
            + "    log(i.validity.tooLong);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' maxlength='3'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", ""})
    public void validationMessage_tooShort_input() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.value = 'ab';\n"
            + "    log(i.validity.tooShort);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' minlength='5'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
            DEFAULT = {"true", "Value must be less than or equal to 10."},
            FF = {"true", "Please select a value that is no more than 10."},
            FF_ESR = {"true", "Please select a value that is no more than 10."})
    @HtmlUnitNYI(
            CHROME = {"true", "Value must be less than or equal to the maximum."},
            EDGE = {"true", "Value must be less than or equal to the maximum."},
            FF = {"true", "Value must be less than or equal to the maximum."},
            FF_ESR = {"true", "Value must be less than or equal to the maximum."})
    public void validationMessage_rangeOverflow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.rangeOverflow);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='number' value='50' max='10'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
            DEFAULT = {"true", "Value must be greater than or equal to 10."},
            FF = {"true", "Please select a value that is no less than 10."},
            FF_ESR = {"true", "Please select a value that is no less than 10."})
    @HtmlUnitNYI(
            CHROME = {"true", "Value must be greater than or equal to the minimum."},
            EDGE = {"true", "Value must be greater than or equal to the minimum."},
            FF = {"true", "Value must be greater than or equal to the minimum."},
            FF_ESR = {"true", "Value must be greater than or equal to the minimum."})
    public void validationMessage_rangeUnderflow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.rangeUnderflow);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='number' value='1' min='10'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
            DEFAULT = {"true", "Please enter a valid value. The two nearest valid values are 2 and 4."},
            FF = {"true", "Please select a valid value. The two nearest valid values are 2 and 4."},
            FF_ESR = {"true", "Please select a valid value. The two nearest valid values are 2 and 4."})
    @HtmlUnitNYI(
            CHROME = {"true", "Please enter a valid value."},
            EDGE = {"true", "Please enter a valid value."},
            FF = {"true", "Please enter a valid value."},
            FF_ESR = {"true", "Please enter a valid value."})
    public void validationMessage_stepMismatch() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.stepMismatch);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='number' value='3' step='2' min='0'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
            DEFAULT = {"true", "Please include an '@' in the email address. 'not-an-email' is missing an '@'."},
            FF = {"true", "Please enter an email address."},
            FF_ESR = {"true", "Please enter an email address."})
    @HtmlUnitNYI(CHROME = {"true", "Please enter an email address."},
            EDGE = {"true", "Please enter an email address."})
    public void validationMessage_typeMismatch_email() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.typeMismatch);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='email' value='not-an-email'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "Please enter a URL."})
    public void validationMessage_typeMismatch_url() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.typeMismatch);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='url' value='not a url'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "Please enter a valid value, thanks!"})
    public void validationMessage_customError_exactStringEchoedBack() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.setCustomValidity('Please enter a valid value, thanks!');\n"
            + "    log(i.validity.customError);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='anything'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true", "Please match the requested format."})
    public void validationMessage_priority_patternMismatchAlone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    log(i.validity.valueMissing);\n"
            + "    log(i.validity.patternMismatch);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='abc' pattern='[0-9]+' required>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true","true", "custom message wins"})
    public void validationMessage_priority_customErrorBeatsValueMissing() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    i.setCustomValidity('custom message wins');\n"
            + "    log(i.validity.valueMissing);\n"
            + "    log(i.validity.customError);\n"
            + "    log(i.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='' required>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void validationMessage_emptyWhenValid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('i').validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='fine'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void validationMessage_emptyWhenBarredViaDisabled() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('i').validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <input id='i' type='text' value='' required disabled>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
