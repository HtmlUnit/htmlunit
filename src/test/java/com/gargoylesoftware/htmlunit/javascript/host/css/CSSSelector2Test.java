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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for css pseudo selectors :placeholder-shown and :-ms-input-placeholder.
 *
 * @author Thorsten Wendelmuth
 *
 */
@RunWith(BrowserRunner.class)
public class CSSSelector2Test extends WebDriverTestCase {

    private static final String PLACEHOLDER_SHOWN_HTML_HEAD = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<style>:placeholder-shown {border: 10px solid;}</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.querySelector(':placeholder-shown'));\n"
            + "    } catch (exception) {\n"
            + "      alert(exception.name);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n";

    private static final String MS_PLACEHOLDER_HTML_HEAD = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<style>:-ms-input-placeholder {border: 10px solid;}</style>"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.querySelector(':-ms-input-placeholder'));\n"
            + "    } catch (exception) {\n"
            + "      alert(exception.name);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n";

    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement]",
            IE = "SyntaxError")
    public void placeholderShown() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown'>\n"
                + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement]",
            IE = "SyntaxError")
    public void placeholderShown_number() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form>\n"
                + "  <input type='number' placeholder='200'>\n"
                + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement]",
            IE = "SyntaxError")
    public void placeholderShown_displayNone() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown'>\n"
                + "</form></body></html>";
        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "null",
            IE = "SyntaxError")
    public void placeholderShown_hasValue() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown' value='dont show placeholder'>\n"
                + "</form></body></html>";
        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "null",
            IE = "SyntaxError")
    public void placeholderShown_noInput() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <div placeholder='htmlUnit supports placeholder-shown' value='dont show placeholder'>\n"
                + "</form></body></html>";
        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "SyntaxError",
            IE = "[object HTMLInputElement]")
    public void msPlaceholder() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown'>\n"
                + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "SyntaxError",
            IE = "[object HTMLInputElement]")
    public void msPlaceholder_number() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form>\n"
                + "  <input type='number' placeholder='2'>\n"
                + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "SyntaxError",
            IE = "[object HTMLInputElement]")
    public void msPlaceholder_displayNone() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown'>\n"
                + "</form></body></html>";
        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "SyntaxError",
            IE = "null")
    public void msPlaceholder_hasValue() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown' value='dont show placeholder'>\n"
                + "</form></body></html>";
        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "SyntaxError",
            IE = "null")
    public void msPlaceholder_noInput() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <div placeholder='htmlUnit supports placeholder-shown' value='dont show placeholder'>\n"
                + "</form></body></html>";
        loadPageWithAlerts2(html);
    }

}
