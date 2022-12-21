/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.parser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Test class for {@link HTMLParser}.
 *
 * @author Atsushi Nakagawa
 */
@RunWith(BrowserRunner.class)
public class HTMLParser5Test extends WebDriverTestCase {

    private static final String LOG_INPUT_FORMS =
            "inputs=document.getElementsByTagName('input');for(i=0;i<inputs.length;i++){f=inputs[i].form;log(f?f.name:null)}";

    @Test
    @Alerts("f1§f2")
    public void formEnclosure_table() throws Exception {
        final String[] html = {
                "<html><body>",
                "<div>",
                "<form name='f1'>",
                "  <table>",
                "    <input>",
                "</form>",
                "<form name='f2'>",
                "  </table>",
                "</div>",
                "<input>",
                "</form>",
                "<script>" + LOG_TITLE_FUNCTION + LOG_INPUT_FORMS + "</script>",
                "</body></html>",
        };

        loadPageVerifyTitle2(String.join("\n", html));
    }

    @Test
    @Alerts("f1§f2")
    public void formEnclosure_div() throws Exception {
        final String[] html = {
                "<html><body>",
                "<div>",
                "<form name='f1'>",
                "  <div>",
                "    <input>",
                "</form>",
                "<form name='f2'>",
                "  </div>",
                "</div>",
                "<input>",
                "</form>",
                "<script>" + LOG_TITLE_FUNCTION + LOG_INPUT_FORMS + "</script>",
                "</body></html>",
        };

        loadPageVerifyTitle2(String.join("\n", html));
    }

    @Test
    @Alerts("f1§f1")
    public void formEnclosure_table_nestedForms() throws Exception {
        final String[] html = {
                "<html><body>",
                "<div>",
                "<form name='f1'>",
                "  <table>",
                "    <input>",
                "<form name='f2'>",
                "  </table>",
                "</div>",
                "<input>",
                "</form>",
                "</form>",
                "<script>" + LOG_TITLE_FUNCTION + LOG_INPUT_FORMS + "</script>",
                "</body></html>",
        };

        loadPageVerifyTitle2(String.join("\n", html));
    }

    @Test
    @Alerts("f1§f1")
    public void formEnclosure_div_nestedForms() throws Exception {
        final String[] html = {
                "<html><body>",
                "<div>",
                "<form name='f1'>",
                "  <div>",
                "    <input>",
                "<form name='f2'>",
                "  </div>",
                "</div>",
                "<input>",
                "</form>",
                "</form>",
                "<script>" + LOG_TITLE_FUNCTION + LOG_INPUT_FORMS + "</script>",
                "</body></html>",
        };

        loadPageVerifyTitle2(String.join("\n", html));
    }

    @Test
    @Alerts("f1§f1§null§null")
    public void formEnclosure_nestedForms() throws Exception {
        final String[] html = {
                "<html><body>",
                "<form name='f1'>",
                "<input>",
                "<form name='f2'>",
                "<input>",
                "</form>",
                "<input>",
                "</form>",
                "<input>",
                "<script>" + LOG_TITLE_FUNCTION + LOG_INPUT_FORMS + "</script>",
                "</body></html>",
        };

        loadPageVerifyTitle2(String.join("\n", html));
    }

    @Test
    @Alerts("f1§f1§f1§f1§f1§f1§null")
    public void formEnclosure_tree1() throws Exception {
        final String[] html = {
                "<html><body>",
                "<div>",
                "<form name='f1'>",
                "  <input>",
                "  <div>",
                "    <input>",
                "    <table>",
                "      <input>",
                "</form>",
                "      <input>",
                "    </table>",
                "    <input>",
                "  </div>",
                "  <input>",
                "</div>",
                "<input>",
                "<script>" + LOG_TITLE_FUNCTION + LOG_INPUT_FORMS + "</script>",
                "</body></html>",
        };

        loadPageVerifyTitle2(String.join("\n", html));
    }

    @Test
    @Alerts("f1§f1§f1§f2§f2§f2§f1§null")
    public void formEnclosure_tree2() throws Exception {
        final String[] html = {
                "<html><body>",
                "<form name='f1'>",
                "  <input>",
                "  <div>",
                "    <input>",
                "</form>",
                "    <input>",
                "<form name='f2'>",
                "    <input>",
                "    <div>",
                "      <input>",
                "</form>",
                "      <input>",
                "    </div>",
                "    <input>",
                "  </div>",
                "  <input>",
                "<script>" + LOG_TITLE_FUNCTION + LOG_INPUT_FORMS + "</script>",
                "</body></html>",
        };

        loadPageVerifyTitle2(String.join("\n", html));
    }

    @Test
    @Alerts("f1§null")
    public void formEnclosure_tree3() throws Exception {
        final String[] html = {
                "<html><body>",
                "<form name='f1'>",
                "  <div>",
                "    <input>",
                "</form>",
                "  </div>",
                "  <input>",
                "<script>" + LOG_TITLE_FUNCTION + LOG_INPUT_FORMS + "</script>",
                "</body></html>",
        };

        loadPageVerifyTitle2(String.join("\n", html));
    }

}
