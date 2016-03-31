/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLInputElement} and buttons.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLInputElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "text", "textfield1", "form1", "cat" })
    public void standardProperties_Text() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.form1.textfield1.value);\n"
            + "    alert(document.form1.textfield1.type);\n"
            + "    alert(document.form1.textfield1.name);\n"
            + "    alert(document.form1.textfield1.form.name);\n"
            + "    document.form1.textfield1.value='cat';\n"
            + "    alert(document.form1.textfield1.value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "error fileupload1", "abc", "abc", "abc", "", "abc", /*"foo", "",*/ "abc", "abc",
                        "abc", "abc", "abc", "abc", "abc", "abc", /*"#000000", "abc", "abc", "abc", "abc", "abc",
                        "abc", "", "50",*/ "abc", "abc", "abc", "abc" },
            CHROME = { "error fileupload1", "abc", "abc", "abc", "", "abc", /*"", "",*/ "abc", "abc",
                        "abc", "abc", "abc", "abc", "abc", "abc", /*"#000000", "", "abc", "", "", "",
                        "", "", "50",*/ "abc", "abc", "abc", "abc" },
            IE = { "abc", "abc", "abc", "", "abc", /*"", "",*/ "abc", "abc",
                    "abc", "abc", "abc", "abc", "abc", "abc", /*"abc", "abc", "abc", "abc", "abc", "abc",
                    "abc", "", "50",*/ "abc", "abc", "abc", "abc" })
    @Test
    public void setValueString() throws Exception {
        testValue("'abc'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "foo", "", "#000000", "abc", "abc", "abc", "abc", "abc", "abc", "", "50" },
            CHROME = { "", "", "#000000", "", "abc", "", "", "", "", "", "50" },
            IE = { "", "", "abc", "abc", "abc", "abc", "abc", "abc", "abc", "", "50" })
    @Test
    @NotYetImplemented
    public void setValueString2() throws Exception {
        testValue2("'abc'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "", "", "", "", "", /*"foo", "",*/ "", "",
                        "", "", "", "", "", "", /*"#000000", "", "", "", "", "",
                        "", "", "50",*/ "", "", "", "" },
            CHROME = { "", "", "", "", "", /*"", "",*/ "", "",
                        "", "", "", "", "", "", /*"#000000", "", "", "", "", "",
                        "", "", "50",*/ "", "", "", "" },
            IE = { "", "", "", "", "", /*"", "",*/ "", "",
                    "", "", "", "", "", "", /*"", "", "", "", "", "",
                    "", "", "50",*/ "", "", "", "" })
    @Test
    public void setValueEmptyString() throws Exception {
        testValue("''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "foo", "", "#000000", "", "", "", "", "", "", "", "50" },
            CHROME = { "", "", "#000000", "", "", "", "", "", "", "", "50" },
            IE = { "", "", "", "", "", "", "", "", "", "", "50" })
    @Test
    @NotYetImplemented
    public void setValueEmptyString2() throws Exception {
        testValue2("''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "error fileupload1", "  ", "  ", "  ", "", "  ", /*"foo", "",*/ "  ", "  ",
                        "  ", "  ", "  ", "  ", "  ", "  ", /*"#000000", "  ", "  ", "  ", "  ", "  ",
                        "  ", "", "50",*/ "  ", "", "  ", "" },
            CHROME = { "error fileupload1", "  ", "  ", "  ", "", "  ", /*"", "",*/ "  ", "  ",
                        "  ", "  ", "  ", "  ", "  ", "  ", /*"#000000", "", "  ", "", "", "",
                        "", "", "50",*/ "  ", "", "  ", "" },
            IE = { "  ", "  ", "  ", "", "  ", /*"", "",*/ "  ", "  ",
                    "  ", "  ", "  ", "  ", "  ", "  ", /*"  ", "  ", "  ", "  ", "  ", "  ",
                    "  ", "", "50",*/ "  ",  "  ", "  ", "  " })
    @Test
    public void setValueBlankString() throws Exception {
        testValue("'  '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "foo", "", "#000000", "  ", "  ", "  ", "  ", "  ", "  ", "", "50" },
            CHROME = { "", "", "#000000", "", "  ", "", "", "", "", "", "50" },
            IE = { "", "", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "", "50" })
    @Test
    @NotYetImplemented
    public void setValueBlankString2() throws Exception {
        testValue2("'  '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "error fileupload1", "12", "12", "12", "", "12", /*"foo", "",*/ "12", "12",
                        "12", "12", "12", "12", "12", "12", /*"#000000", "12", "12", "12", "12", "12",
                        "12", "12", "12",*/ "12", "12", "12", "12" },
            CHROME = { "error fileupload1", "12", "12", "12", "", "12", /*"", "",*/ "12", "12",
                        "12", "12", "12", "12", "12", "12", /*"#000000", "", "12", "", "", "",
                        "", "12", "12",*/ "12", "12", "12", "12" },
            IE = { "12", "12", "12", "", "12", /*"", "",*/ "12", "12",
                    "12", "12", "12", "12", "12", "12", /*"12", "12", "12", "12", "12", "12",
                    "12", "12", "12",*/ "12", "12", "12", "12" })
    @Test
    public void setValueNumber() throws Exception {
        testValue("12");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "foo", "", "#000000", "12", "12", "12", "12", "12", "12", "12", "12" },
            CHROME = { "", "", "#000000", "", "12", "", "", "", "", "12", "12" },
            IE = { "", "", "12", "12", "12", "12", "12", "12", "12", "12", "12" })
    @Test
    @NotYetImplemented
    public void setValueNumber2() throws Exception {
        testValue2("12");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "", "null", "", "", "", /*"foo", "",*/ "", "",
                        "", "null", "", "null", "", "", /* "#000000", "", "", "", "", "",
                        "", "", "50",*/ "", "", "", "" },
            CHROME = { "", "null", "", "", "", /*"", "",*/ "", "",
                        "", "null", "", "null", "", "", /* "#000000", "", "", "", "", "",
                        "", "", "50",*/ "", "", "", "" },
            IE = { "", "null", "", "", "", /*"", "",*/ "", "",
                    "", "null", "", "null", "", "null", /*"", "", "", "", "", "",
                    "", "", "50",*/ "", "", "", "" })
    @Test
    public void setValueNull() throws Exception {
        testValue("null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "foo", "", "#000000", "", "", "", "", "", "", "", "50" },
            CHROME = { "", "", "#000000", "", "", "", "", "", "", "", "50" },
            IE = { "", "", "", "", "", "", "", "", "", "", "50" })
    @Test
    @NotYetImplemented
    public void setValueNull2() throws Exception {
        testValue2("null");
    }

    private void testValue(final String value) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"

            + "    document.form1.button1.value = " + value + ";\n"
            + "    document.form1.button2.value = " + value + ";\n"
            + "    document.form1.checkbox1.value = " + value + ";\n"
            + "    try { document.form1.fileupload1.value = " + value + " } catch(e) { alert('error fileupload1') }\n"
            + "    document.form1.hidden1.value = " + value + ";\n"
            + "    document.form1.select1.value = " + value + ";\n"
            + "    document.form1.select2.value = " + value + ";\n"
            + "    document.form1.password1.value = " + value + ";\n"
            + "    document.form1.radio1.value = " + value + ";\n"
            + "    document.form1.reset1.value = " + value + ";\n"
            + "    document.form1.reset2.value = " + value + ";\n"
            + "    document.form1.submit1.value = " + value + ";\n"
            + "    document.form1.submit2.value = " + value + ";\n"
            + "    document.form1.textInput1.value = " + value + ";\n"
            + "    document.form1.textarea1.value = " + value + ";\n"
            + "    document.form1.color1.value = " + value + ";\n"
            + "    document.form1.date1.value = " + value + ";\n"
            + "    document.form1.datetime1.value = " + value + ";\n"
            + "    document.form1.datetimeLocal1.value = " + value + ";\n"
            + "    document.form1.time1.value = " + value + ";\n"
            + "    document.form1.week1.value = " + value + ";\n"
            + "    document.form1.month1.value = " + value + ";\n"
            + "    document.form1.number1.value = " + value + ";\n"
            + "    document.form1.range1.value = " + value + ";\n"
            + "    document.form1.search1.value = " + value + ";\n"
            + "    document.form1.email1.value = " + value + ";\n"
            + "    document.form1.tel1.value = " + value + ";\n"
            + "    document.form1.url1.value = " + value + ";\n"

            + "    alert(document.form1.button1.value);\n"
            + "    alert(document.form1.button2.value);\n"
            + "    alert(document.form1.checkbox1.value);\n"
            + "    alert(document.form1.fileupload1.value);\n"
            + "    alert(document.form1.hidden1.value);\n"
            // + "    alert(document.form1.select1.value);\n"
            // + "    alert(document.form1.select2.value);\n"
            + "    alert(document.form1.password1.value);\n"
            + "    alert(document.form1.radio1.value);\n"
            + "    alert(document.form1.reset1.value);\n"
            + "    alert(document.form1.reset2.value);\n"
            + "    alert(document.form1.submit1.value);\n"
            + "    alert(document.form1.submit2.value);\n"
            + "    alert(document.form1.textInput1.value);\n"
            + "    alert(document.form1.textarea1.value);\n"
            // + "    alert(document.form1.color1.value);\n"
            // + "    alert(document.form1.date1.value);\n"
            // + "    alert(document.form1.datetime1.value);\n"
            // + "    alert(document.form1.datetimeLocal1.value);\n"
            // + "    alert(document.form1.time1.value);\n"
            // + "    alert(document.form1.week1.value);\n"
            // + "    alert(document.form1.month1.value);\n"
            // + "    alert(document.form1.number1.value);\n"
            // + "    alert(document.form1.range1.value);\n"
            + "    alert(document.form1.search1.value);\n"
            + "    alert(document.form1.email1.value);\n"
            + "    alert(document.form1.tel1.value);\n"
            + "    alert(document.form1.url1.value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='button' name='button1'></button>\n"
            + "    <button type='button' name='button2'></button>\n"
            + "    <input type='checkbox' name='checkbox1'/>\n"
            + "    <input type='file' name='fileupload1'/>\n"
            + "    <input type='hidden' name='hidden1'/>\n"
            + "    <select name='select1'>\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <select multiple='multiple' name='select2'>\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <input type='password' name='password1'/>\n"
            + "    <input type='radio' name='radio1'/>\n"
            + "    <input type='reset' name='reset1'/>\n"
            + "    <button type='reset' name='reset2'></button>\n"
            + "    <input type='submit' name='submit1'/>\n"
            + "    <button type='submit' name='submit2'></button>\n"
            + "    <input type='text' name='textInput1'/>\n"
            + "    <textarea name='textarea1'>foo</textarea>\n"
            + "    <input type='color' name='color1'/>\n"
            + "    <input type='date' name='date1'/>\n"
            + "    <input type='datetime' name='datetime1'/>\n"
            + "    <input type='datetime-local' name='datetimeLocal1'/>\n"
            + "    <input type='time' name='time1'/>\n"
            + "    <input type='week' name='week1'/>\n"
            + "    <input type='month' name='month1'/>\n"
            + "    <input type='number' name='number1'/>\n"
            + "    <input type='range' name='range1'/>\n"
            + "    <input type='search' name='search1'/>\n"
            + "    <input type='email' name='email1'/>\n"
            + "    <input type='tel' name='tel1'/>\n"
            + "    <input type='url' name='url1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    private void testValue2(final String value) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"

            + "    document.form1.select1.value = " + value + ";\n"
            + "    document.form1.select2.value = " + value + ";\n"
            + "    document.form1.color1.value = " + value + ";\n"
            + "    document.form1.date1.value = " + value + ";\n"
            + "    document.form1.datetime1.value = " + value + ";\n"
            + "    document.form1.datetimeLocal1.value = " + value + ";\n"
            + "    document.form1.time1.value = " + value + ";\n"
            + "    document.form1.week1.value = " + value + ";\n"
            + "    document.form1.month1.value = " + value + ";\n"
            + "    document.form1.number1.value = " + value + ";\n"
            + "    document.form1.range1.value = " + value + ";\n"
            + "    document.form1.range1.value = " + value + ";\n"

            + "    alert(document.form1.select1.value);\n"
            + "    alert(document.form1.select2.value);\n"
            + "    alert(document.form1.color1.value);\n"
            + "    alert(document.form1.date1.value);\n"
            + "    alert(document.form1.datetime1.value);\n"
            + "    alert(document.form1.datetimeLocal1.value);\n"
            + "    alert(document.form1.time1.value);\n"
            + "    alert(document.form1.week1.value);\n"
            + "    alert(document.form1.month1.value);\n"
            + "    alert(document.form1.number1.value);\n"
            + "    alert(document.form1.range1.value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <select multiple='multiple' name='select2'>\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <input type='color' name='color1'/>\n"
            + "    <input type='date' name='date1'/>\n"
            + "    <input type='datetime' name='datetime1'/>\n"
            + "    <input type='datetime-local' name='datetimeLocal1'/>\n"
            + "    <input type='time' name='time1'/>\n"
            + "    <input type='week' name='week1'/>\n"
            + "    <input type='month' name='month1'/>\n"
            + "    <input type='number' name='number1'/>\n"
            + "    <input type='range' name='range1'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "button, button, checkbox, file, hidden, select-one, select-multiple, "
                + "password, radio, reset, reset, "
                + "submit, submit, text, textarea, color, date, text, datetime-local, time, week, month, number, "
                + "range, search, email, tel, url",
            FF = "button, button, checkbox, file, hidden, select-one, select-multiple, "
                + "password, radio, reset, reset, "
                + "submit, submit, text, textarea, color, text, text, text, text, text, text, number, range, "
                + "search, email, tel, url",
            IE = "button, button, checkbox, file, hidden, select-one, select-multiple, "
                + "password, radio, reset, reset, "
                + "submit, submit, text, textarea, text, text, text, text, text, text, text, number, range, "
                + "search, email, tel, url"
            )
    @NotYetImplemented
    public void type() throws Exception {
        testAttribute("type", "", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = "null, undefined, null, [object FileList], null, undefined, undefined, null, null, null,"
                + " undefined, null, undefined, null, undefined, null, null, null, null, null, null, null,"
                + " null, null, null, null, null, null",
            IE = "undefined, undefined, undefined, [object FileList], undefined, undefined, undefined, undefined,"
                + " undefined, undefined, undefined, undefined, undefined, undefined, undefined, undefined,"
                + " undefined, undefined, undefined, undefined, undefined, undefined, undefined, undefined,"
                + " undefined, undefined, undefined, undefined")
    @Test
    public void files() throws Exception {
        testAttribute("files", "", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({ "false", "undefined", "false", "false", "false", "undefined", "undefined",
                "false", "false", "false", "undefined", "false", "undefined", "false",
                "undefined", "false", "false", "false", "false", "false", "false",
                "false", "false", "false", "false", "false", "false", "false" })
    @Test
    public void checked() throws Exception {
        testAttribute("checked", "", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(DEFAULT = { "true", "undefined", "true", "true", "true", "undefined", "undefined",
                "true", "true", "true", "undefined", "true", "undefined", "true",
                "undefined", "true", "true", "true", "true", "true", "true",
                "true", "true", "true", "true", "true", "true", "true" },
            IE = { "false", "undefined", "true", "false", "false", "undefined", "undefined",
                "false", "true", "false", "undefined", "false", "undefined", "false",
                "undefined", "false", "false", "false", "false", "false", "false",
                "false", "false", "false", "false", "false", "false", "false" })
    @Test
    @NotYetImplemented(IE)
    public void checkedWithAttribute() throws Exception {
        testAttribute("checked", "checked", null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({ "true", "undefined", "true", "true", "true", "undefined", "undefined",
                "true", "true", "true", "undefined", "true", "undefined", "true",
                "undefined", "true", "true", "true", "true", "true", "true",
                "true", "true", "true", "true", "true", "true", "true" })
    @Test
    public void setCheckedTrue() throws Exception {
        testAttribute("checked", "", "true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts({ "true", "undefined", "true", "true", "true", "undefined", "undefined",
                "true", "true", "true", "undefined", "true", "undefined", "true",
                "undefined", "true", "true", "true", "true", "true", "true",
                "true", "true", "true", "true", "true", "true", "true" })
    @Test
    public void setCheckedBlank() throws Exception {
        testAttribute("checked", "", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts(FF = { "abc", "abc", "abc", "", "abc", "foo", "", "abc", "abc",
                        "abc", "abc", "abc", "abc", "abc", "foo", "#000000", "abc", "abc",
                        "abc", "abc", "abc", "abc", "", "50", "abc", "abc", "abc", "abc" },
            CHROME = { "abc", "abc", "abc", "", "abc", "foo", "", "abc", "abc",
                        "abc", "abc", "abc", "abc", "abc", "foo", "#000000", "", "abc",
                        "", "", "", "", "", "50", "abc", "abc", "abc", "abc" },
            IE = { "abc", "abc", "abc", "", "abc", "foo", "", "abc", "abc",
                        "abc", "abc", "abc", "abc", "abc", "foo", "abc", "abc", "abc",
                        "abc", "abc", "abc", "abc", "", "50", "abc", "abc", "abc", "abc" })
    @Test
    @NotYetImplemented
    public void setValueAttribute() throws Exception {
        testAttribute("value", "", "abc");
    }

    private void testAttribute(final String property, final String attrib, final String value) throws Exception {
        String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n";

        if (value != null) {
            html = html
                + "    document.form1.button1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.button2.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.checkbox1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.fileupload1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.hidden1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.select1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.select2.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.password1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.radio1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.reset1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.reset2.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.submit1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.submit2.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.textInput1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.textarea1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.color1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.date1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.datetime1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.datetimeLocal1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.time1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.week1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.month1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.number1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.range1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.search1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.email1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.tel1.setAttribute('" + property + "', '" + value + "');\n"
                + "    document.form1.url1.setAttribute('" + property + "', '" + value + "');\n";
        }

        html = html
            + "    alert(document.form1.button1." + property + ");\n"
            + "    alert(document.form1.button2." + property + ");\n"
            + "    alert(document.form1.checkbox1." + property + ");\n"
            + "    alert(document.form1.fileupload1." + property + ");\n"
            + "    alert(document.form1.hidden1." + property + ");\n"
            + "    alert(document.form1.select1." + property + ");\n"
            + "    alert(document.form1.select2." + property + ");\n"
            + "    alert(document.form1.password1." + property + ");\n"
            + "    alert(document.form1.radio1." + property + ");\n"
            + "    alert(document.form1.reset1." + property + ");\n"
            + "    alert(document.form1.reset2." + property + ");\n"
            + "    alert(document.form1.submit1." + property + ");\n"
            + "    alert(document.form1.submit2." + property + ");\n"
            + "    alert(document.form1.textInput1." + property + ");\n"
            + "    alert(document.form1.textarea1." + property + ");\n"
            + "    alert(document.form1.color1." + property + ");\n"
            + "    alert(document.form1.date1." + property + ");\n"
            + "    alert(document.form1.datetime1." + property + ");\n"
            + "    alert(document.form1.datetimeLocal1." + property + ");\n"
            + "    alert(document.form1.time1." + property + ");\n"
            + "    alert(document.form1.week1." + property + ");\n"
            + "    alert(document.form1.month1." + property + ");\n"
            + "    alert(document.form1.number1." + property + ");\n"
            + "    alert(document.form1.range1." + property + ");\n"
            + "    alert(document.form1.search1." + property + ");\n"
            + "    alert(document.form1.email1." + property + ");\n"
            + "    alert(document.form1.tel1." + property + ");\n"
            + "    alert(document.form1.url1." + property + ");\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='button' name='button1' " + attrib + "></button>\n"
            + "    <button type='button' name='button2' " + attrib + "></button>\n"
            + "    <input type='checkbox' name='checkbox1' " + attrib + "/>\n"
            + "    <input type='file' name='fileupload1' " + attrib + "/>\n"
            + "    <input type='hidden' name='hidden1' " + attrib + "/>\n"
            + "    <select name='select1' " + attrib + ">\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <select multiple='multiple' name='select2' " + attrib + ">\n"
            + "        <option>foo</option>\n"
            + "    </select>\n"
            + "    <input type='password' name='password1' " + attrib + "/>\n"
            + "    <input type='radio' name='radio1' " + attrib + "/>\n"
            + "    <input type='reset' name='reset1' " + attrib + "/>\n"
            + "    <button type='reset' name='reset2' " + attrib + "></button>\n"
            + "    <input type='submit' name='submit1' " + attrib + "/>\n"
            + "    <button type='submit' name='submit2' " + attrib + "></button>\n"
            + "    <input type='text' name='textInput1' " + attrib + "/>\n"
            + "    <textarea name='textarea1' " + attrib + ">foo</textarea>\n"
            + "    <input type='color' name='color1' " + attrib + "/>\n"
            + "    <input type='date' name='date1' " + attrib + "/>\n"
            + "    <input type='datetime' name='datetime1' " + attrib + "/>\n"
            + "    <input type='datetime-local' name='datetimeLocal1' " + attrib + "/>\n"
            + "    <input type='time' name='time1' " + attrib + "/>\n"
            + "    <input type='week' name='week1' " + attrib + "/>\n"
            + "    <input type='month' name='month1' " + attrib + "/>\n"
            + "    <input type='number' name='number1' " + attrib + "/>\n"
            + "    <input type='range' name='range1' " + attrib + "/>\n"
            + "    <input type='search' name='search1' " + attrib + "/>\n"
            + "    <input type='email' name='email1' " + attrib + "/>\n"
            + "    <input type='tel' name='tel1' " + attrib + "/>\n"
            + "    <input type='url' name='url1' " + attrib + "/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true" })
    public void checkedAttribute_Checkbox() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.form1.checkbox1.checked);\n"
            + "    document.form1.checkbox1.checked = true;\n"
            + "    alert(document.form1.checkbox1.checked);\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='cheCKbox' name='checkbox1' id='checkbox1' value='foo' />\n"
            + "</form>\n"
            + "<a href='javascript:test()' id='clickme'>click me</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkBox = driver.findElement(By.id("checkbox1"));
        assertFalse(checkBox.isSelected());
        driver.findElement(By.id("clickme")).click();
        assertTrue(checkBox.isSelected());

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "false", "false", "false", "true", "false" })
    public void checkedAttribute_Radio() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.form1.radio1[0].checked);\n"
            + "    alert(document.form1.radio1[1].checked);\n"
            + "    alert(document.form1.radio1[2].checked);\n"
            + "    document.form1.radio1[1].checked = true;\n"
            + "    alert(document.form1.radio1[0].checked);\n"
            + "    alert(document.form1.radio1[1].checked);\n"
            + "    alert(document.form1.radio1[2].checked);\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='radio' name='radio1' id='radioA' value='a' checked='checked'/>\n"
            + "    <input type='RADIO' name='radio1' id='radioB' value='b' />\n"
            + "    <input type='radio' name='radio1' id='radioC' value='c' />\n"
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
        assertFalse(radioA.isSelected());
        assertTrue(radioB.isSelected());
        assertFalse(radioC.isSelected());

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "true", "false", "true", "false", "true" })
    public void disabledAttribute() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    alert(document.form1.button1.disabled);\n"
            + "    alert(document.form1.button2.disabled);\n"
            + "    alert(document.form1.button3.disabled);\n"
            + "    document.form1.button1.disabled = true;\n"
            + "    document.form1.button2.disabled = false;\n"
            + "    document.form1.button3.disabled = true;\n"
            + "    alert(document.form1.button1.disabled);\n"
            + "    alert(document.form1.button2.disabled);\n"
            + "    alert(document.form1.button3.disabled);\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='submit' name='button1' value='1'/>\n"
            + "    <input type='submit' name='button2' value='2' disabled/>\n"
            + "    <input type='submit' name='button3' value='3'/>\n"
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
        assertFalse(button1.isEnabled());
        assertTrue(button2.isEnabled());
        assertFalse(button3.isEnabled());

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputValue() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + " document.form1.textfield1.value = 'blue';\n"
            + "}\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' onsubmit='doTest()'>\n"
            + " <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + " <input type='submit' id='clickMe'/>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        assertEquals(getDefaultUrl() + "?textfield1=blue", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputSelect_NotDefinedAsPropertyAndFunction() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + " document.form1.textfield1.select();\n"
            + "}\n"
            + "</script></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' onsubmit='doTest()'>\n"
            + " <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + " <input type='submit' id='clickMe'/>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        assertEquals(getDefaultUrl() + "?textfield1=foo", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void thisDotFormInOnClick() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<body>\n"
            + "<form name='form1'>\n"
            + "<input type='submit' id='clickMe' onClick=\"this.form.target='_blank'; return false;\">\n"
            + "</form>\n"
            + "<script>\n"
            + "alert(document.forms[0].target == '');\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);

        // HtmlUnitDriver is buggy, it returns null here
        // assertEquals("", driver.findElement(By.name("form1")).getAttribute("target"));

        driver.findElement(By.id("clickMe")).click();

        assertEquals("_blank", driver.findElement(By.name("form1")).getAttribute("target"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true" })
    public void fieldDotForm() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head><title>foo</title><script>\n"
            + "function test(){\n"
            + "  var f = document.form1;\n"
            + "  alert(f == f.mySubmit.form);\n"
            + "  alert(f == f.myText.form);\n"
            + "  alert(f == f.myPassword.form);\n"
            + "  alert(f == document.getElementById('myImage').form);\n"
            + "  alert(f == f.myButton.form);\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inputNameChange() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + " document.form1.textfield1.name = 'changed';\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' onsubmit='doTest()'>\n"
            + " <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + " <input type='submit' name='button1' id='clickMe' value='pushme' />\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();

        assertEquals(getDefaultUrl() + "?changed=foo&button1=pushme", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void onChange() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + " <input type='text' name='text1' onchange='alert(this.value)'>\n"
            + "<input name='myButton' type='button' onclick='document.form1.text1.value=\"from button\"'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textinput = driver.findElement(By.name("text1"));
        textinput.sendKeys("foo");
        final WebElement button = driver.findElement(By.name("myButton"));
        button.click();
        assertEquals("from button", textinput.getAttribute("value"));

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void onChangeSetByJavaScript() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + " <input type='text' name='text1' id='text1'>\n"
            + "<input name='myButton' type='button' onclick='document.form1.text1.value=\"from button\"'>\n"
            + "</form>\n"
            + "<script>\n"
            + "document.getElementById('text1').onchange= function(event) { alert(this.value) };\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textinput = driver.findElement(By.name("text1"));
        textinput.sendKeys("foo");
        final WebElement button = driver.findElement(By.name("myButton"));
        button.click();
        assertEquals("from button", textinput.getAttribute("value"));

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Test the default value of a radio and checkbox buttons.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"on", "on" })
    public void defautValue() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.myForm.myRadio.value);\n"
            + "    alert(document.myForm.myCheckbox.value);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='radio' name='myRadio'/>\n"
            + "<input type='checkbox' name='myCheckbox'/>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that changing type doesn't throw.
     * Test must be extended when setting type really does something.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"radio", "hidden", "image" })
    public void changeType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.myForm.myRadio;\n"
            + "    alert(input.type);\n"

            + "    try {\n"
            + "      input.type = 'hidden';\n"
            + "    } catch(e) { alert('error');}\n"
            + "    alert(input.type);\n"

            + "    try {\n"
            + "      input.setAttribute('type', 'image');\n"
            + "    } catch(e) { alert('error');}\n"
            + "    alert(input.type);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='myForm' action='foo'>\n"
            + "    <input type='radio' name='myRadio'/>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);

        if (driver instanceof HtmlUnitDriver) {
            final WebElement myRadio = driver.findElement(By.name("myRadio"));
            assertTrue(toHtmlElement(myRadio) instanceof HtmlImageInput);
        }
    }

    /**
     * Inputs have properties not only from there own type.
     * Works with Mozilla, Firefox and IE... but not with HtmlUnit now.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({
            "button: false, false, function, function, , ",
            "submit: false, false, function, function, submit it!, submit it!",
            "file: false, false, function, function, , ",
            "checkbox: true, true, function, function, , on",
            "radio: true, true, function, function, , on",
            "text: false, false, function, function, , ",
            "password: false, false, function, function, , "
        })
    public void defaultValues() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
            + "function details(_oInput) {\n"
            + "  alert(_oInput.type + ': '\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text", "null", "hidden", "hidden" })
    public void createInputAndChangeType() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.createElement('INPUT');\n"
            + "    alert(input.type);\n"
            + "    alert(input.getAttribute('type'));\n"
            + "    input.type = 'hidden';\n"
            + "    alert(input.type);\n"
            + "    alert(input.getAttribute('type'));\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text", "null", "text", "text" })
    public void createInputAndChangeTypeToText() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.createElement('INPUT');\n"
            + "    alert(input.type);\n"
            + "    alert(input.getAttribute('type'));\n"
            + "    input.type = 'text';\n"
            + "    alert(input.type);\n"
            + "    alert(input.getAttribute('type'));\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void buttonOutsideForm() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title></head><body>\n"
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
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='test' action='foo'>\n"
            + "    <input name='field1' onchange='submit()'>\n"
            + "    <img src='unknown.gif'>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><title>page 2</title><body></body></html>");

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("field1")).sendKeys("bla");
        driver.findElement(By.tagName("img")).click();
        assertEquals("page 2", driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "30", "undefined", "30", "30", "30", "40", "50", "string", "number" })
    public void maxLength() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    alert(input.maxlength);\n"
            + "    alert(input.maxLength);\n"
            + "    alert(input.MaxLength);\n"
            + "    alert(input.getAttribute('maxlength'));\n"
            + "    alert(input.getAttribute('maxLength'));\n"
            + "    alert(input.getAttribute('MaxLength'));\n"
            + "    input.setAttribute('MaXlenGth', 40);\n"
            + "    alert(input.maxLength);\n"
            + "    input.maxLength = 50;\n"
            + "    alert(input.getAttribute('maxlength'));\n"
            + "    alert(typeof input.getAttribute('maxLength'));\n"
            + "    alert(typeof input.maxLength);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' maxlength='30'/>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined", "30", "30", "30",
                "undefined", "40", "string", "number" },
            CHROME = {"undefined", "30", "undefined", "30", "30", "30", "40", "50", "string", "number" })
    public void minLength() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    alert(input.minlength);\n"
            + "    alert(input.minLength);\n"
            + "    alert(input.MinLength);\n"
            + "    alert(input.getAttribute('minlength'));\n"
            + "    alert(input.getAttribute('minLength'));\n"
            + "    alert(input.getAttribute('MinLength'));\n"
            + "    input.setAttribute('MiNlenGth', 40);\n"
            + "    alert(input.minLength);\n"
            + "    input.minLength = 50;\n"
            + "    alert(input.getAttribute('minlength'));\n"
            + "    alert(typeof input.getAttribute('minLength'));\n"
            + "    alert(typeof input.minLength);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' minlength='30'/>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeMaxLength() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<form>\n"
            + "<input type='text' id='text1' maxlength='5'/>\n"
            + "<input type='password' id='password1' maxlength='6'/>\n"
            + "</form></body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement textField = webDriver.findElement(By.id("text1"));
        textField.sendKeys("123456789");
        assertEquals("12345", textField.getAttribute("value"));
        textField.sendKeys("\b7");
        assertEquals("12347", textField.getAttribute("value"));

        final WebElement passwordField = webDriver.findElement(By.id("password1"));
        passwordField.sendKeys("123456789");
        assertEquals("123456", passwordField.getAttribute("value"));
        passwordField.sendKeys("\b7");
        assertEquals("123457", passwordField.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeMaxLengthZero() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<form>\n"
            + "<input type='text' id='text1' maxlength='0'/>\n"
            + "<input type='password' id='password1' maxlength='0'/>\n"
            + "</form></body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement textField = webDriver.findElement(By.id("text1"));
        textField.sendKeys("123456789");
        assertEquals("", textField.getAttribute("value"));
        textField.sendKeys("\b7");
        assertEquals("", textField.getAttribute("value"));

        final WebElement passwordField = webDriver.findElement(By.id("password1"));
        passwordField.sendKeys("123456789");
        assertEquals("", passwordField.getAttribute("value"));
        passwordField.sendKeys("\b7");
        assertEquals("", passwordField.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeMaxLengthAndBlanks() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<form>\n"
            + "<input type='text' id='text1' maxlength=' 2 '/>\n"
            + "<input type='password' id='password1' maxlength='    4  '/>\n"
            + "</form></body></html>";

        final WebDriver webDriver = loadPage2(html);
        final WebElement textField = webDriver.findElement(By.id("text1"));
        textField.sendKeys("123456789");
        assertEquals("12", textField.getAttribute("value"));
        textField.sendKeys("\b7");
        assertEquals("17", textField.getAttribute("value"));

        final WebElement passwordField = webDriver.findElement(By.id("password1"));
        passwordField.sendKeys("123456789");
        assertEquals("1234", passwordField.getAttribute("value"));
        passwordField.sendKeys("\b7");
        assertEquals("1237", passwordField.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"text TeXt", "password PassWord", "hidden Hidden",
                    "checkbox CheckBox", "radio rAdiO", "file FILE", "checkbox CHECKBOX" },
            IE = {"text TeXt", "password PassWord", "hidden Hidden",
                    "checkbox CheckBox", "radio rAdiO", "file FILE", "checkbox checkbox" })
    public void typeCase() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var t = document.getElementById('aText');\n"
            + "  alert(t.type + ' ' + t.getAttribute('type'));\n"
            + "  var p = document.getElementById('aPassword');\n"
            + "  alert(p.type + ' ' + p.getAttribute('type'));\n"
            + "  var h = document.getElementById('aHidden');\n"
            + "  alert(h.type + ' ' + h.getAttribute('type'));\n"
            + "  var cb = document.getElementById('aCb');\n"
            + "  alert(cb.type + ' ' + cb.getAttribute('type'));\n"
            + "  var r = document.getElementById('aRadio');\n"
            + "  alert(r.type + ' ' + r.getAttribute('type'));\n"
            + "  var f = document.getElementById('aFile');\n"
            + "  alert(f.type + ' ' + f.getAttribute('type'));\n"

            + "  try {"
            + "    f.type = 'CHECKBOX';\n"
            + "    alert(f.type + ' ' + f.getAttribute('type'));\n"
            + "  } catch(e) { alert('error');}\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void readOnly() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    alert(input.readOnly);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<input id='myInput' value='some test' readonly='false'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "left", "right", "bottom", "middle", "top", "wrong", "" },
            IE = { "", "", "", "", "", "", "" })
    @NotYetImplemented(IE)
    public void getAlign() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
            + "  for (var i = 1; i <= 7; i++) {\n"
            + "    alert(document.getElementById('i' + i).align);\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "CenTer", "8", "foo", "left", "right", "bottom", "middle", "top" },
            IE = { "", "error", "", "error", "", "", "", "", "", "" })
    @NotYetImplemented(IE)
    public void setAlign() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <form>\n"
            + "    <input id='i1' type='text' align='left' value=''/>\n"
            + "  </form>\n"

            + "<script>\n"
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) { alert('error'); }\n"
            + "    alert(elem.align);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "A", "a", "A", "a8", "8Afoo", "8", "@" })
    public void accessKey() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <input id='a1'>\n"
            + "  <input id='a2' accesskey='A'>\n"
            + "  <script>\n"
            + "    var a1 = document.getElementById('a1');\n"
            + "    var a2 = document.getElementById('a2');\n"
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
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "test", "4", "42", "2", "[object HTMLInputElement]", "25" })
    public void getAttributeAndSetValue() throws Exception {
        final String html
            = "<html>\n"
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
            + "    <input id='t'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "null", "4", "", "0" })
    public void getAttributeAndSetValueNull() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
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
            + "    <input id='t'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "2", "7" })
    public void selectionRange() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "    var input = document.getElementById('myInput');\n"
            + "    alert(input.selectionStart);"
            + "    alert(input.selectionEnd);"

            + "    if (!input.setSelectionRange) { alert('input.setSelectionRange not available'); return };\n"
            + "    input.setSelectionRange(2, 7);\n"
            + "    alert(input.selectionStart);"
            + "    alert(input.selectionEnd);"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='some test'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submitNonRequired() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function submitMe() {\n"
            + "    alert('onsubmit');\n"
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
        assertTrue(driver.getCurrentUrl().contains("myName"));
        // because we have a new page
        assertTrue(getCollectedAlerts(driver).isEmpty());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "§§URL§§" })
    @NotYetImplemented
    public void submitRequired() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function submitMe() {\n"
            + "    alert('onsubmit');\n"
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
    @Alerts({ "false", "true" })
    @NotYetImplemented
    public void checkValidity() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function checkStatus() {\n"
            + "    var elem = document.getElementById('myInput');\n"
            + "    if (elem.checkValidity) {\n"
            + "      alert(elem.checkValidity());\n"
            + "    } else {\n"
            + "      alert('checkValidity not supported');\n"
            + "    }\n"
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
        driver.findElement(By.id("myInput")).sendKeys("something");
        driver.findElement(By.id("myButton")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "§§URL§§?myName=abcdefg",
            IE = "§§URL§§")
    @NotYetImplemented(IE)
    public void maxLengthJavaScript() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "function updateValue() {\n"
            + "    document.getElementById('myInput').value = 'abcdefg';\n"
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
        assertEquals("abcdefg", driver.findElement(By.id("myInput")).getAttribute("value"));
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
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
        assertEquals("ab", driver.findElement(By.id("myInput")).getAttribute("value"));

        driver.findElement(By.id("mySubmit")).click();

        expandExpectedAlertsVariables(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "30", "undefined", "30", "30",
                "40", "50", "string", "string" },
            CHROME = {"30", "undefined", "30", "30", "40", "50", "string", "string" })
    public void min() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    alert(input.min);\n"
            + "    alert(input.Min);\n"
            + "    alert(input.getAttribute('min'));\n"
            + "    alert(input.getAttribute('Min'));\n"
            + "    input.setAttribute('MiN', 40);\n"
            + "    alert(input.min);\n"
            + "    input.min = 50;\n"
            + "    alert(input.getAttribute('min'));\n"
            + "    alert(typeof input.getAttribute('min'));\n"
            + "    alert(typeof input.min);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' min='30'/>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "30", "undefined", "30", "30",
                "40", "50", "string", "string" },
            CHROME = {"30", "undefined", "30", "30", "40", "50", "string", "string" })
    public void max() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    alert(input.max);\n"
            + "    alert(input.Max);\n"
            + "    alert(input.getAttribute('max'));\n"
            + "    alert(input.getAttribute('Max'));\n"
            + "    input.setAttribute('MaX', 40);\n"
            + "    alert(input.max);\n"
            + "    input.max = 50;\n"
            + "    alert(input.getAttribute('max'));\n"
            + "    alert(typeof input.getAttribute('max'));\n"
            + "    alert(typeof input.max);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<form name='myForm' action='foo'>\n"
            + "<input type='text' id='text1' max='30'/>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined", "undefined", "undefined", "undefined" },
            CHROME = { "0", "2", "1", "2", "1", "1" })
    public void labels() throws Exception {
        final String html =
            "<html><head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      debug(document.getElementById('e1'));\n"
            + "      debug(document.getElementById('e2'));\n"
            + "      debug(document.getElementById('e3'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      var labels = document.getElementById('e4').labels;\n"
            + "      document.body.removeChild(document.getElementById('l4'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      alert(labels ? labels.length : labels);\n"
            + "    }\n"
            + "    function debug(e) {\n"
            + "      alert(e.labels ? e.labels.length : e.labels);\n"
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

        loadPageWithAlerts2(html);
    }

}
