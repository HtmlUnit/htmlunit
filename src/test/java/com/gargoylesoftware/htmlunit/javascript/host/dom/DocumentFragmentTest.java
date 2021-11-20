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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link DocumentFragment}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class DocumentFragmentTest extends WebDriverTestCase {

    /**
     * Regression test for bug 3191431 on computation from child selector
     * in a document fragment.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object CSSStyleDeclaration]",
            FF = "[object CSS2Properties]",
            FF78 = "[object CSS2Properties]")
    public void getComputedStyleOnChild() throws Exception {
        final String html = "<html><head><style>\n"
            + "  body > div { background-color: green#FF0000; }\n"
            + "</style></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var frag = document.createDocumentFragment();\n"
            + "    var d = document.createElement('div');\n"
            + "    frag.appendChild(d);\n"
            + "    log(window.getComputedStyle(d, null));\n"
            + "  } catch (e) { log('exception'); }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void createElement() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var frag = document.createDocumentFragment();\n"
            + "        if (frag.createElement) {\n"
            + "          var d = frag.createElement('div');\n"
            + "          log(d.tagName);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "DIV", "DIV"})
    public void querySelector() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var frag = document.createDocumentFragment();\n"
            + "  var d = document.createElement('div');\n"
            + "  frag.appendChild(d);\n"

            + "  log(frag.querySelectorAll('div').length);\n"
            + "  log(frag.querySelectorAll('div')[0].tagName);\n"
            + "  log(frag.querySelector('div').tagName);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<div id='root'>\n"
            + "</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void children() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var fragment = document.createDocumentFragment();\n"
            + "  fragment.textContent = '';\n"
            + "  log(fragment.childNodes.length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object DocumentFragment]", "undefined"})
    public void url() throws Exception {
        final String html = "<!DOCTYPE><html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var fragment = document.createDocumentFragment();\n"
            + "  log(fragment);\n"
            + "  log(fragment.URL);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "null", "null", "1", "myDiv", "myDiv"},
            IE = {"undefined", "undefined", "undefined"})
    public void firstElementChild() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"

            + "    log(fragment.childElementCount);\n"
            + "    log(fragment.firstElementChild);\n"
            + "    log(fragment.lastElementChild);\n"

            + "    if (fragment.childElementCount === undefined) { return; };\n"

            + "    var d = document.createElement('div');\n"
            + "    d.id = 'myDiv';\n"
            + "    fragment.appendChild(d);\n"
            + "    var e = document.createElement('input');\n"
            + "    e.id = 'first';\n"
            + "    d.appendChild(e);\n"

            + "    log(fragment.childElementCount);\n"
            + "    log(fragment.firstElementChild.id);\n"
            + "    log(fragment.lastElementChild.id);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null", "null", "null", "[object HTMLDivElement]", "null", "null"})
    public void getElementById() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var d = document.createElement('div');\n"
            + "    d.id = 'myDiv';\n"
            + "    fragment.appendChild(d);\n"
            + "    var e = document.createElement('input');\n"
            + "    e.id = 'first';\n"
            + "    d.appendChild(e);\n"

            + "    log(document.getElementById(''));\n"
            + "    log(document.getElementById(undefined));\n"
            + "    log(document.getElementById(null));\n"
            + "    log(document.getElementById('unknown'));\n"
            + "    log(document.getElementById('myDiv'));\n"
            + "    log(document.getElementById('mydiv'));\n"
            + "    log(document.getElementById('first'));\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'>\n"
            + "  <div></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
