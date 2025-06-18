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
package org.htmlunit.html.parser;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Test class for {@link HTMLParser}.
 *
 * @author Atsushi Nakagawa
 * @author Ronald Brill
 */
public class HTMLParser5Test extends WebDriverTestCase {

    private static final String LOG_INPUT_FORMS =
            "inputs = document.getElementsByTagName('input');"
            + "for(i=0; i<inputs.length; i++) {"
            + "  var inp = inputs[i];"
            + "  var f = inp.form;"
            + "  log(inp.id + '-' + (f?f.name:null))"
            + "}";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"i1-f1", "i2-f2"})
    public void formEnclosure_table() throws Exception {
        final String html =
            "<html><body>\n"
            + "<div>\n"
            + "<form name='f1'>\n"
            + "  <table>\n"
            + "    <input id='i1'>\n"
            + "</form>\n"
            + "<form name='f2'>\n"
            + "  </table>\n"
            + "</div>\n"
            + "<input id='i2'>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + LOG_INPUT_FORMS
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"i1-f1", "i2-f2"})
    public void formEnclosure_div() throws Exception {
        final String html =
            "<html><body>\n"
            + "<div>\n"
            + "<form name='f1'>\n"
            + "  <div>\n"
            + "    <input id='i1'>\n"
            + "</form>\n"
            + "<form name='f2'>\n"
            + "  </div>\n"
            + "</div>\n"
            + "<input id='i2'>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + LOG_INPUT_FORMS
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"i1-f1", "i2-f1"})
    public void formEnclosure_table_nestedForms() throws Exception {
        final String html =
            "<html><body>\n"
            + "<div>\n"
            + "<form name='f1'>\n"
            + "  <table>\n"
            + "    <input id='i1'>\n"
            + "<form name='f2'>\n"
            + "  </table>\n"
            + "</div>\n"
            + "<input id='i2'>\n"
            + "</form>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + LOG_INPUT_FORMS
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"i1-f1", "i2-f1"})
    public void formEnclosure_div_nestedForms() throws Exception {
        final String html =
            "<html><body>\n"
            + "<div>\n"
            + "<form name='f1'>\n"
            + "  <div>\n"
            + "    <input id='i1'>\n"
            + "<form name='f2'>\n"
            + "  </div>\n"
            + "</div>\n"
            + "<input id='i2'>\n"
            + "</form>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + LOG_INPUT_FORMS
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"i1-f1", "i2-f1", "i3-null", "i4-null"})
    public void formEnclosure_nestedForms() throws Exception {
        final String html =
            "<html><body>\n"
            + "<form name='f1'>\n"
            + "<input id='i1'>\n"
            + "<form name='f2'>\n"
            + "<input id='i2'>\n"
            + "</form>\n"
            + "<input id='i3'>\n"
            + "</form>\n"
            + "<input id='i4'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + LOG_INPUT_FORMS
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"i1-f1", "i2-f1", "i3-f1", "i4-f1", "i5-f1", "i6-f1", "i7-null"})
    public void formEnclosure_tree1() throws Exception {
        final String html =
            "<html><body>\n"
            + "<div>\n"

            + "<form name='f1'>\n"
            + "  <input id='i1'>\n"
            + "  <div>\n"
            + "    <input id='i2'>\n"

            + "    <table>\n"
            + "      <input id='i3'>\n"
            + "</form>\n"

            + "      <input id='i4'>\n"
            + "    </table>\n"

            + "    <input id='i5'>\n"
            + "  </div>\n"
            + "  <input id='i6'>\n"
            + "</div>\n"
            + "<input id='i7'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + LOG_INPUT_FORMS
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"i1-f1", "i2-f1", "i3-f1", "i4-f2", "i5-f2", "i6-f2", "i7-f1", "i8-null"})
    public void formEnclosure_tree2() throws Exception {
        final String html =
            "<html><body>\n"
            + "<form name='f1'>\n"
            + "  <input id='i1'>\n"
            + "  <div>\n"
            + "    <input id='i2'>\n"
            + "</form>\n"

            + "    <input id='i3'>\n"
            + "<form name='f2'>\n"

            + "    <input id='i4'>\n"
            + "    <div>\n"
            + "      <input id='i5'>\n"

            + "</form>\n"

            + "      <input id='i6'>\n"
            + "    </div>\n"
            + "    <input id='i7'>\n"
            + "  </div>\n"
            + "  <input id='i8'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + LOG_INPUT_FORMS
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"i1-f1", "i2-null"})
    public void formEnclosure_tree3() throws Exception {
        final String html =
            "<html><body>\n"
            + "<form name='f1'>\n"
            + "  <div>\n"
            + "    <input id='i1'>\n"
            + "</form>\n"
            + "  </div>\n"
            + "  <input id='i2'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + LOG_INPUT_FORMS
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x<form name=\"f1\">y</form>")
    public void unclosedForm() throws Exception {
        final String html =
            "<html><body>"
            + "x<form name=\"f1\">y"
            + "</body></html>";

        loadPage2(html);

        final String script = "return document.body.innerHTML";
        final String result = (String) ((JavascriptExecutor) getWebDriver()).executeScript(script);
        assertEquals(getExpectedAlerts()[0], result);
    }
}
