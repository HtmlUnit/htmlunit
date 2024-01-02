/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for changing the type attribute for {@link HtmlInput}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public final class HtmlInput3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234-abcd-abcd", "1234-1234-1234"},
            IE = {"1234-abcd-abcd", "1234-abcd-abcd"})
    public void none_button() throws Exception {
        changeType("value='abcd'", "1234", "button");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "1234--null"})
    public void noneNoValueAttr_button() throws Exception {
        changeType("", "1234", "button");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234-abcd-abcd", "1234-1234-1234"},
            IE = {"1234-abcd-abcd", "abcd-abcd-abcd"})
    public void none_checkbox() throws Exception {
        changeType("value='abcd'", "1234", "checkbox");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "on--null"})
    public void noneNoValueAttr_checkbox() throws Exception {
        changeType("", "1234", "checkbox");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"#aaaaaa-#ffffff-#ffffff", "#aaaaaa-#ffffff-#ffffff"},
            IE = {"#aaaaaa-#ffffff-#ffffff", "error"})
    public void none_color() throws Exception {
        changeType("value='#ffffff'", "#aaaaaa", "color");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"#aaaaaa--null", "#aaaaaa--null"},
            IE = {"#aaaaaa--null", "error"})
    public void noneNoValueAttr_color() throws Exception {
        changeType("", "#aaaaaa", "color");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2019-07-11-2018-06-12-2018-06-12", "2019-07-11-2018-06-12-2018-06-12"},
            IE = {"2019-07-11-2018-06-12-2018-06-12", "error"})
    public void none_date() throws Exception {
        changeType("value='2018-06-12'", "2019-07-11", "date");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2019-07-11--null", "2019-07-11--null"},
            IE = {"2019-07-11--null", "error"})
    public void noneNoValueAttr_date() throws Exception {
        changeType("", "2019-07-11", "date");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2019-07-11T4:16-2018-06-12T19:30-2018-06-12T19:30",
                       "-2018-06-12T19:30-2018-06-12T19:30"},
            IE = {"2019-07-11T4:16-2018-06-12T19:30-2018-06-12T19:30", "error"})
    public void none_datetimelocal() throws Exception {
        changeType("value='2018-06-12T19:30'", "2019-07-11T4:16", "datetime-local");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2019-07-11T4:16--null",
                       "--null"},
            IE = {"2019-07-11T4:16--null", "error"})
    public void noneNoValueAttr_datetimelocal() throws Exception {
        changeType("", "2019-07-11T4:16", "datetime-local");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-htmlunit.txt-htmlunit.txt", "-htmlunit.txt-htmlunit.txt"})
    public void none_file() throws Exception {
        changeType("value='htmlunit.txt'", "1234", "file");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "--null"})
    public void noneNoValueAttr_file() throws Exception {
        changeType("", "1234", "file");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234-abcd-abcd", "1234-1234-1234"},
            IE = {"1234-abcd-abcd", "1234-abcd-abcd"})
    public void none_hidden() throws Exception {
        changeType("value='abcd'", "1234", "hidden");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "1234--null"})
    public void noneNoValueAttr_hidden() throws Exception {
        changeType("", "1234", "hidden");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234-abcd-abcd", "1234-1234-1234"},
            IE = {"1234-abcd-abcd", "abcd-abcd-abcd"})
    public void none_image() throws Exception {
        changeType("src='test.png' value='abcd'", "1234", "image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "--null"})
    public void noneNoValueAttr_image() throws Exception {
        changeType("src='test.png'", "1234", "image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12-7-7", "12-7-7"},
            CHROME = {"12-7-7", "-7-7"},
            EDGE = {"12-7-7", "-7-7"},
            IE = {"12-7-7", "error"})
    public void none_month() throws Exception {
        changeType("value='7'", "12", "month");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12--null", "12--null"},
            CHROME = {"12--null", "--null"},
            EDGE = {"12--null", "--null"},
            IE = {"12--null", "error"})
    public void noneNoValueAttr_month() throws Exception {
        changeType("", "12", "month");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-3.14-3.14", "1234-3.14-3.14"})
    public void none_number() throws Exception {
        changeType("value='3.14'", "1234", "number");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void noneNoValueAttr_number() throws Exception {
        changeType("", "1234", "number");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-abcd-abcd", "1234-abcd-abcd"})
    public void none_password() throws Exception {
        changeType("value='abcd'", "1234", "password");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void noneNoValueAttr_password() throws Exception {
        changeType("", "1234", "password");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234-abcd-abcd", "1234-1234-1234"},
            IE = {"1234-abcd-abcd", "abcd-abcd-abcd"})
    public void none_radio() throws Exception {
        changeType("value='abcd'", "1234", "radio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "on--null"})
    public void noneNoValueAttr_radio() throws Exception {
        changeType("", "1234", "radio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7-4-4", "7-4-4"})
    public void none_range() throws Exception {
        changeType("min='0' max='11' value='4'", "7", "range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7--null", "7--null"})
    public void noneNoValueAttr_range() throws Exception {
        changeType("min='0' max='11'", "7", "range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234-abcd-abcd", "1234-1234-1234"},
            IE = {"1234-abcd-abcd", "1234-abcd-abcd"})
    @HtmlUnitNYI(IE = {"1234-abcd-abcd", "abcd-abcd-abcd"})
    public void none_reset() throws Exception {
        changeType("value='abcd'", "1234", "reset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "Reset-Reset-Reset"})
    public void noneNoValueAttr_reset() throws Exception {
        changeType("", "1234", "reset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-abcd-abcd", "1234-abcd-abcd"})
    public void none_search() throws Exception {
        changeType("value='abcd'", "1234", "search");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void noneNoValueAttr_search() throws Exception {
        changeType("", "1234", "search");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234-abcd-abcd", "1234-1234-1234"},
            IE = {"1234-abcd-abcd", "1234-abcd-abcd"})
    @HtmlUnitNYI(IE = {"1234-abcd-abcd", "abcd-abcd-abcd"})
    public void none_submit() throws Exception {
        changeType("value='abcd'", "1234", "submit");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "Submit Query-Submit Query-Submit Query"})
    public void noneNoValueAttr_submit() throws Exception {
        changeType("", "1234", "submit");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-0177 6012345-0177 6012345", "1234-0177 6012345-0177 6012345"})
    public void none_tel() throws Exception {
        changeType("value='0177 6012345'", "1234", "tel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void noneNoValueAttr_tel() throws Exception {
        changeType("", "1234", "tel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234-abcd-abcd", "1234-abcd-abcd"})
    public void none_text() throws Exception {
        changeType("value='abcd'", "1234", "text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void noneNoValueAttr_text() throws Exception {
        changeType("", "1234", "text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4:16-19:30-19:30", "-19:30-19:30"},
            IE = {"4:16-19:30-19:30", "error"})
    public void none_time() throws Exception {
        changeType("value='19:30'", "4:16", "time");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4:16--null", "--null"},
            IE = {"4:16--null", "error"})
    public void noneNoValueAttr_time() throws Exception {
        changeType("", "4:16", "time");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"https://www.wetator.org-https://www.htmlunit.org-https://www.htmlunit.org",
             "https://www.wetator.org-https://www.htmlunit.org-https://www.htmlunit.org"})
    public void none_url() throws Exception {
        changeType("value='https://www.htmlunit.org'", "https://www.wetator.org", "url");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"https://www.wetator.org--null",
             "https://www.wetator.org--null"})
    public void noneNoValueAttr_url() throws Exception {
        changeType("", "https://www.wetator.org", "url");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"24-42-42", "24-42-42"},
            CHROME = {"24-42-42", "-42-42"},
            EDGE = {"24-42-42", "-42-42"},
            IE = {"24-42-42", "error"})
    public void none_week() throws Exception {
        changeType("value='42'", "24", "week");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"24--null", "24--null"},
            CHROME = {"24--null", "--null"},
            EDGE = {"24--null", "--null"},
            IE = {"24--null", "error"})
    public void noneNoValueAttr_week() throws Exception {
        changeType("", "24", "week");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2020-04-7T1:13-2018-06-12T19:30-2018-06-12T19:30",
                       "2020-04-7T1:13-2018-06-12T19:30-2018-06-12T19:30"},
            IE = {"2020-04-7T1:13-2018-06-12T19:30-2018-06-12T19:30", "error"})
    public void none_datetime() throws Exception {
        changeType("value='2018-06-12T19:30'", "2020-04-7T1:13", "datetime");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2020-04-7T1:13--null",
                       "2020-04-7T1:13--null"},
            IE = {"2020-04-7T1:13--null", "error"})
    public void noneNoValueAttr_datetime() throws Exception {
        changeType("", "2020-04-7T1:13", "datetime");
    }

    private void changeType(final String inputAttribs, final String value, final String targetType) throws Exception {
        final String html =
                "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var input = document.getElementById('tester');\n"
                + "    input.value = '" + value + "';\n"
                + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

                + "    try {\n"
                + "      input.type = '" + targetType + "';\n"
                + "      log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
                + "    } catch(e) { log('error'); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<form>\n"
                + "  <input id='tester' " + inputAttribs + ">\n"
                + "</form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "1234--null"})
    public void detached_button() throws Exception {
        changeTypeDetached("1234", "button");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "on--null"})
    public void detached_checkbox() throws Exception {
        changeTypeDetached("1234", "checkbox");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"#aaaaaa--null", "#aaaaaa--null"},
            IE = {"#aaaaaa--null", "error"})
    public void detached_color() throws Exception {
        changeTypeDetached("#aaaaaa", "color");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2019-07-11--null", "2019-07-11--null"},
            IE = {"2019-07-11--null", "error"})
    public void detached_date() throws Exception {
        changeTypeDetached("2019-07-11", "date");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2019-07-11T4:16--null",
                       "--null"},
            IE = {"2019-07-11T4:16--null", "error"})
    public void detached_datetimelocal() throws Exception {
        changeTypeDetached("2019-07-11T4:16", "datetime-local");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "--null"})
    public void detached_file() throws Exception {
        changeTypeDetached("1234", "file");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "1234--null"})
    public void detached_hidden() throws Exception {
        changeTypeDetached("1234", "hidden");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "--null"})
    public void detached_image() throws Exception {
        changeTypeDetached("1234", "image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12--null", "12--null"},
            CHROME = {"12--null", "--null"},
            EDGE = {"12--null", "--null"},
            IE = {"12--null", "error"})
    public void detached_month() throws Exception {
        changeTypeDetached("12", "month");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void detached_number() throws Exception {
        changeTypeDetached("1234", "number");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void detached_password() throws Exception {
        changeTypeDetached("1234", "password");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "on--null"})
    public void detached_radio() throws Exception {
        changeTypeDetached("1234", "radio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7--null", "7--null"})
    public void detached_range() throws Exception {
        changeTypeDetached("7", "range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "Reset-Reset-Reset"})
    public void detached_reset() throws Exception {
        changeTypeDetached("1234", "reset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void detached_search() throws Exception {
        changeTypeDetached("1234", "search");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1234--null", "1234-1234-1234"},
            IE = {"1234--null", "Submit Query-Submit Query-Submit Query"})
    public void detached_submit() throws Exception {
        changeTypeDetached("1234", "submit");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void detached_tel() throws Exception {
        changeTypeDetached("1234", "tel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234--null", "1234--null"})
    public void detached_text() throws Exception {
        changeTypeDetached("1234", "text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4:16--null", "--null"},
            IE = {"4:16--null", "error"})
    public void detached_time() throws Exception {
        changeTypeDetached("4:16", "time");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"https://www.wetator.org--null",
             "https://www.wetator.org--null"})
    public void detached_url() throws Exception {
        changeTypeDetached("https://www.wetator.org", "url");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"24--null", "24--null"},
            CHROME = {"24--null", "--null"},
            EDGE = {"24--null", "--null"},
            IE = {"24--null", "error"})
    public void detached_week() throws Exception {
        changeTypeDetached("24", "week");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2020-04-7T1:13--null",
                       "2020-04-7T1:13--null"},
            IE = {"2020-04-7T1:13--null", "error"})
    public void detached_datetime() throws Exception {
        changeTypeDetached("2020-04-7T1:13", "datetime");
    }

    private void changeTypeDetached(final String value, final String targetType) throws Exception {
        final String html =
                "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var input = document.createElement('input');\n"
                + "    input.value = '" + value + "';\n"
                + "    log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

                + "    try {\n"
                + "      input.type = '" + targetType + "';\n"
                + "      log(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
                + "    } catch(e) { log('error'); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
