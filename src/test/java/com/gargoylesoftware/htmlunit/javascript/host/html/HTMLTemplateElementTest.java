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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLTemplateElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLTemplateElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "exception")
    public void prototype() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(HTMLTemplateElement.prototype == null);\n"
            + "  } catch (e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void contentCheck() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var template = document.createElement('template');\n"
            + "          log('content' in template);\n"
            + "        } catch (e) { log('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object DocumentFragment]", "0-0",
                       "[object DocumentFragment]", "0-0",
                       "[object DocumentFragment]", "0-1",
                       "[object DocumentFragment]", "0-2"},
            IE = "not available")
    public void content() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var template = document.createElement('template');\n"
            + "        if (!('content' in template)) { log('not available'); return }\n"
            + "        log(template.content);\n"
            + "        log(template.childNodes.length + '-' + template.content.childNodes.length);\n"

            + "        template = document.getElementById('tEmpty');\n"
            + "        log(template.content);\n"
            + "        log(template.childNodes.length + '-' + template.content.childNodes.length);\n"

            + "        template = document.getElementById('tText');\n"
            + "        log(template.content);\n"
            + "        log(template.childNodes.length + '-' + template.content.childNodes.length);\n"

            + "        template = document.getElementById('tDiv');\n"
            + "        log(template.content);\n"
            + "        log(template.childNodes.length + '-' + template.content.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <template id='tEmpty'></template>\n"
            + "    <template id='tText'>HtmlUnit</template>\n"
            + "    <template id='tDiv'><div>HtmlUnit</div><div>is great</div></template>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object DocumentFragment]", "0-0", "1-0"},
            IE = "not available")
    public void appendChild() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var template = document.createElement('template');\n"
            + "        if (!('content' in template)) { log('not available'); return }\n"

            + "        template = document.getElementById('tester');\n"
            + "        log(template.content);\n"
            + "        log(template.childNodes.length + '-' + template.content.childNodes.length);\n"

            + "        var div = document.createElement('div');\n"
            + "        template.appendChild(div);\n"
            + "        log(template.childNodes.length + '-' + template.content.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <template id='tester'></template>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "<p></p>", "", "HtmlUnit", "<div>HtmlUnit</div><div>is great</div>"},
            IE = {"", "<div></div>", "", "HtmlUnit", "<div>HtmlUnit</div><div>is great</div>"})
    public void innerHTML() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var template = document.createElement('template');\n"
            + "        log(template.innerHTML);\n"

            + "        var div = document.createElement('div');\n"
            + "        template.appendChild(div);\n"
            + "        log(template.innerHTML);\n"

            + "        var p = document.createElement('p');\n"
            + "        if ('content' in template) {\n"
            + "          template.content.appendChild(p);\n"
            + "          log(template.innerHTML);\n"
            + "        }\n"

            + "        template = document.getElementById('tEmpty');\n"
            + "        log(template.innerHTML);\n"

            + "        template = document.getElementById('tText');\n"
            + "        log(template.innerHTML);\n"

            + "        template = document.getElementById('tDiv');\n"
            + "        log(template.innerHTML);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <template id='tEmpty'></template>\n"
            + "    <template id='tText'>HtmlUnit</template>\n"
            + "    <template id='tDiv'><div>HtmlUnit</div><div>is great</div></template>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<template></template>", "<template>HtmlUnit</template>",
             "<template><div>HtmlUnit</div><div>is great</div></template>"})
    public void innerHTMLIncludingTemplate() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        template = document.getElementById('tEmpty');\n"
            + "        log(template.innerHTML);\n"

            + "        template = document.getElementById('tText');\n"
            + "        log(template.innerHTML);\n"

            + "        template = document.getElementById('tDiv');\n"
            + "        log(template.innerHTML);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='tEmpty'><template></template></div>\n"
            + "    <div id='tText'><template>HtmlUnit</template></div>\n"
            + "    <div id='tDiv'><template><div>HtmlUnit</div><div>is great</div></template></div>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"<template></template>", "<template></template>",
                       "<template><p></p></template>", "<template id=\"tEmpty\"></template>",
                       "<template id=\"tText\">HtmlUnit</template>",
                       "<template id=\"tDiv\"><div>HtmlUnit</div><div>is great</div></template>"},
            IE = {"<?XML:NAMESPACE PREFIX = \"PUBLIC\" NS = \"URN:COMPONENT\" /><template></template>",
                  "<template><div></div></template>", "<template id=\"tEmpty\"></template>",
                  "<template id=\"tText\">HtmlUnit</template>",
                  "<template id=\"tDiv\"><div>HtmlUnit</div><div>is great</div></template>"})
    @HtmlUnitNYI(IE = {"<template></template>", "<template><div></div></template>",
                       "<template id=\"tEmpty\"></template>",
                       "<template id=\"tText\">HtmlUnit</template>",
                       "<template id=\"tDiv\"><div>HtmlUnit</div><div>is great</div></template>"})
    public void outerHTML() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var template = document.createElement('template');\n"
            + "        log(template.outerHTML);\n"

            + "        var div = document.createElement('div');\n"
            + "        template.appendChild(div);\n"
            + "        log(template.outerHTML);\n"

            + "        var p = document.createElement('p');\n"
            + "        if ('content' in template) {\n"
            + "          template.content.appendChild(p);\n"
            + "          log(template.outerHTML);\n"
            + "        }\n"

            + "        template = document.getElementById('tEmpty');\n"
            + "        log(template.outerHTML);\n"

            + "        template = document.getElementById('tText');\n"
            + "        log(template.outerHTML);\n"

            + "        template = document.getElementById('tDiv');\n"
            + "        log(template.outerHTML);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <template id='tEmpty'></template>\n"
            + "    <template id='tText'>HtmlUnit</template>\n"
            + "    <template id='tDiv'><div>HtmlUnit</div><div>is great</div></template>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"myTemplate", "null"},
            IE = {"myTemplate", "[object HTMLDivElement]"})
    public void getElementById() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var template = document.getElementById('myTemplate');\n"
            + "        log(template.id);\n"

            + "        outer = document.getElementById('outerDiv');\n"
            + "        log(outer);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <template id='myTemplate'>\n"
            + "      <div id='outerDiv'>HtmlUnit <div id='innerDiv'>is great</div></div>\n"
            + "    </template>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"myTemplate", "0"},
            IE = {"myTemplate", "2"})
    public void getElementsByTagName() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var template = document.getElementById('myTemplate');\n"
            + "        log(template.id);\n"

            + "        var children = template.getElementsByTagName('div');"
            + "        log(children.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <template id='myTemplate'>\n"
            + "      <div id='outerDiv'>HtmlUnit <div id='innerDiv'>is great</div></div>\n"
            + "    </template>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"myTemplate", "0"},
            IE = {"myTemplate", "3"})
    public void childNodes() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var template = document.getElementById('myTemplate');\n"
            + "        log(template.id);\n"

            + "        log(template.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <template id='myTemplate'>\n"
            + "      <div id='outerDiv'>HtmlUnit <div id='innerDiv'>is great</div></div>\n"
            + "    </template>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
