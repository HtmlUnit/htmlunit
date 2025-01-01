/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.css;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

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
            + LOG_TITLE_FUNCTION
            + "    try {\n"
            + "      log(document.querySelector(':placeholder-shown'));\n"
            + "    } catch (exception) {\n"
            + "      log(exception.name);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n";

    private static final String MS_PLACEHOLDER_HTML_HEAD = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<style>:-ms-input-placeholder {border: 10px solid;}</style>"
            + "<script>\n"
            + "  function test() {\n"
            + LOG_TITLE_FUNCTION
            + "    try {\n"
            + "      log(document.querySelector(':-ms-input-placeholder'));\n"
            + "    } catch (exception) {\n"
            + "      log(exception.name);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n";

    @Test
    @Alerts("[object HTMLInputElement]")
    public void placeholderShown() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown'>\n"
                + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("[object HTMLInputElement]")
    public void placeholderShown_number() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form>\n"
                + "  <input type='number' placeholder='200'>\n"
                + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("[object HTMLInputElement]")
    public void placeholderShown_displayNone() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown'>\n"
                + "</form></body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("null")
    public void placeholderShown_hasValue() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown' value='dont show placeholder'>\n"
                + "</form></body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("null")
    public void placeholderShown_noInput() throws Exception {
        final String html = PLACEHOLDER_SHOWN_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <div placeholder='htmlUnit supports placeholder-shown' value='dont show placeholder'>\n"
                + "</form></body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("SyntaxError")
    public void msPlaceholder() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown'>\n"
                + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("SyntaxError")
    public void msPlaceholder_number() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form>\n"
                + "  <input type='number' placeholder='2'>\n"
                + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("SyntaxError")
    public void msPlaceholder_displayNone() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown'>\n"
                + "</form></body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("SyntaxError")
    public void msPlaceholder_hasValue() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <input placeholder='htmlUnit supports placeholder-shown' value='dont show placeholder'>\n"
                + "</form></body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("SyntaxError")
    public void msPlaceholder_noInput() throws Exception {
        final String html = MS_PLACEHOLDER_HTML_HEAD
                + "<body onload='test();'>\n"
                + "<form style='display:none;'>\n"
                + "  <div placeholder='htmlUnit supports placeholder-shown' value='dont show placeholder'>\n"
                + "</form></body></html>";
        loadPageVerifyTitle2(html);
    }

}
