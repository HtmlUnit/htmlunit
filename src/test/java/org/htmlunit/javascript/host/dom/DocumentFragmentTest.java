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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DocumentFragment}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class DocumentFragmentTest extends WebDriverTestCase {

    /**
     * Regression test for bug 3191431 on computation from child selector
     * in a document fragment.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object CSSStyleDeclaration]",
            FF = "[object CSSStyleProperties]",
            FF_ESR = "[object CSS2Properties]")
    public void getComputedStyleOnChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><style>\n"
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
            + "  } catch(e) { logEx(e); }\n"
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
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
    @Alerts({"0", "null", "null", "1", "myDiv", "myDiv"})
    public void firstElementChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
    @Alerts({"0", "null", "null", "0", "null", "null", "1", "myDiv", "myDiv"})
    public void firstElementChildTextNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"

            + "    log(fragment.childElementCount);\n"
            + "    log(fragment.firstElementChild);\n"
            + "    log(fragment.lastElementChild);\n"

            + "    if (fragment.childElementCount === undefined) { return; };\n"

            + "    var txt = document.createTextNode('HtmlUnit');\n"
            + "    fragment.appendChild(txt);\n"

            + "    log(fragment.childElementCount);\n"
            + "    log(fragment.firstElementChild);\n"
            + "    log(fragment.lastElementChild);\n"

            + "    var d = document.createElement('div');\n"
            + "    d.id = 'myDiv';\n"
            + "    fragment.appendChild(d);\n"

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
        final String html = DOCTYPE_HTML
            + "<html>\n"
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

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true"})
    public void ownerDocument() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      log(document === fragment.ownerDocument);\n"

            + "      var div = document.createElement('div');\n"
            + "      fragment.appendChild(div);\n"
            + "      log(div.ownerDocument === document);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"false", "true", "false", "true", "false", "true", "true", "false"})
    public void getRootNode() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (!document.body.getRootNode) {\n"
            + "        log('-'); return;\n"
            + "      }\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      log(document === fragment.getRootNode());\n"
            + "      log(fragment === fragment.getRootNode());\n"

            + "      var div = document.createElement('div');\n"
            + "      fragment.appendChild(div);\n"
            + "      log(document === div.getRootNode());\n"
            + "      log(fragment === div.getRootNode());\n"

            + "      document.body.appendChild(fragment);\n"
            + "      log(document === fragment.getRootNode());\n"
            + "      log(fragment === fragment.getRootNode());\n"
            + "      log(document === div.getRootNode());\n"
            + "      log(fragment === div.getRootNode());\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("0")
    public void ctor() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var fragment = new DocumentFragment();\n"
            + "      log(fragment.querySelectorAll('p').length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object HTMLDivElement]"})
    public void append() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      let div = document.createElement('div');\n"
            + "      fragment.append(div);"
            + "      log(fragment.children.length);\n"
            + "      log(fragment.children[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("0")
    public void appendNoParam() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      fragment.append();"
            + "      log(fragment.children.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object Text]", "abcd",
             "2", "[object Text]", "1234"})
    public void appendText() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      fragment.append('abcd');"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"

            + "      let txt = document.createTextNode('1234');\n"
            + "      fragment.append(txt);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[1]);\n"
            + "      log(fragment.childNodes[1].textContent);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object HTMLDivElement]",
             "2", "[object Text]", "abcd",
             "3", "[object HTMLDivElement]",
             "4", "[object Text]", "1234",
             "5", "[object HTMLDivElement]"})
    public void appendMixed() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"

            + "      let div = document.createElement('div');\n"
            + "      fragment.append(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"

            + "      fragment.append('abcd');"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[1]);\n"
            + "      log(fragment.childNodes[1].textContent);\n"

            + "      div = document.createElement('div');\n"
            + "      fragment.append(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[2]);\n"

            + "      let txt = document.createTextNode('1234');\n"
            + "      fragment.append(txt);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[3]);\n"
            + "      log(fragment.childNodes[3].textContent);\n"

            + "      div = document.createElement('div');\n"
            + "      fragment.append(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[4]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object HTMLDivElement]"})
    public void prepend() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      let div = document.createElement('div');\n"
            + "      fragment.prepend(div);"
            + "      log(fragment.children.length);\n"
            + "      log(fragment.children[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("0")
    public void prependNoParam() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      fragment.prepend();"
            + "      log(fragment.children.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object Text]", "abcd",
             "2", "[object Text]", "1234"})
    public void prependText() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      fragment.prepend('abcd');"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"

            + "      let txt = document.createTextNode('1234');\n"
            + "      fragment.prepend(txt);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object HTMLDivElement]",
             "2", "[object Text]", "abcd",
             "3", "[object HTMLDivElement]",
             "4", "[object Text]", "1234",
             "5", "[object HTMLDivElement]"})
    public void prependMixed() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"

            + "      let div = document.createElement('div');\n"
            + "      fragment.prepend(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"

            + "      fragment.prepend('abcd');"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"

            + "      div = document.createElement('div');\n"
            + "      fragment.prepend(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"

            + "      let txt = document.createTextNode('1234');\n"
            + "      fragment.prepend(txt);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"

            + "      div = document.createElement('div');\n"
            + "      fragment.prepend(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object HTMLDivElement]"})
    public void replaceChildren() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      let div = document.createElement('div');\n"
            + "      fragment.replaceChildren(div);"
            + "      log(fragment.children.length);\n"
            + "      log(fragment.children[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("0")
    public void replaceChildrenNoParam() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      fragment.replaceChildren();"
            + "      log(fragment.children.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object Text]", "abcd",
             "1", "[object Text]", "1234"})
    public void replaceChildrenText() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"
            + "      fragment.replaceChildren('abcd');"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"

            + "      let txt = document.createTextNode('1234');\n"
            + "      fragment.replaceChildren(txt);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object HTMLDivElement]",
             "1", "[object Text]", "abcd",
             "1", "[object HTMLDivElement]",
             "1", "[object Text]", "1234",
             "1", "[object HTMLDivElement]"})
    public void replaceChildrenMixed() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      let fragment = new DocumentFragment();\n"

            + "      let div = document.createElement('div');\n"
            + "      fragment.replaceChildren(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"

            + "      fragment.replaceChildren('abcd');"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"

            + "      div = document.createElement('div');\n"
            + "      fragment.replaceChildren(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"

            + "      let txt = document.createTextNode('1234');\n"
            + "      fragment.replaceChildren(txt);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "      log(fragment.childNodes[0].textContent);\n"

            + "      div = document.createElement('div');\n"
            + "      fragment.replaceChildren(div);"
            + "      log(fragment.childNodes.length);\n"
            + "      log(fragment.childNodes[0]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"DIV", "SPAN", "P", "DIV", "P", "SPAN"},
            FF_ESR = {"DIV", "SPAN", "P", "no moveBefore()"})
    public void moveBefore_basic() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment.appendChild(div);\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(p);\n"

            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(p, span);\n"
            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"DIV", "SPAN", "P", "P", "DIV", "SPAN"},
            FF_ESR = {"DIV", "SPAN", "P", "no moveBefore()"})
    public void moveBefore_moveToFirst() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment.appendChild(div);\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(p);\n"

            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(p, div);\n"
            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"DIV", "SPAN", "P", "DIV", "SPAN", "P"},
            FF_ESR = {"DIV", "SPAN", "P", "no moveBefore()"})
    public void moveBefore_samePosition() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment.appendChild(div);\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(p);\n"

            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(span, p);\n"
            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"DIV", "SPAN", "P", "DIV", "SPAN", "P"},
            FF_ESR = {"DIV", "SPAN", "P", "no moveBefore()"})
    public void moveBefore_samePositionFirst() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment.appendChild(div);\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(p);\n"

            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(div, span);\n"
            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"DIV", "SPAN", "P", "DIV", "SPAN", "P"},
            FF_ESR = {"DIV", "SPAN", "P", "no moveBefore()"})
    public void moveBefore_itself() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment.appendChild(div);\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(p);\n"

            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(span, span);\n"
            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "HierarchyRequestError/DOMException",
            FF_ESR = "no moveBefore()")
    public void moveBefore_ancestor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    fragment.appendChild(div);\n"
            + "    div.appendChild(span);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      span.moveBefore(div, null);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NotFoundError/DOMException",
            FF_ESR = "no moveBefore()")
    public void moveBefore_differentParents() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment1 = document.createDocumentFragment();\n"
            + "    var fragment2 = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment1.appendChild(div);\n"
            + "    fragment2.appendChild(p);\n"

            + "    if (!fragment1.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment1.moveBefore(div, p);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NotFoundError/DOMException",
            FF_ESR = "no moveBefore()")
    public void moveBefore_descendant() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment.appendChild(div);\n"
            + "    div.appendChild(span);\n"
            + "    fragment.appendChild(p);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      div.moveBefore(span, fragment);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NotFoundError/DOMException",
            FF_ESR = "no moveBefore()")
    public void moveBefore_movedNotChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment1 = document.createDocumentFragment();\n"
            + "    var fragment2 = document.createDocumentFragment();\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment1.appendChild(span);\n"
            + "    fragment2.appendChild(p);\n"

            + "    if (!fragment1.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment1.moveBefore(span, p);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NotFoundError/DOMException",
            FF_ESR = "no moveBefore()")
    public void moveBefore_referenceNotChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment1 = document.createDocumentFragment();\n"
            + "    var fragment2 = document.createDocumentFragment();\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment1.appendChild(span);\n"
            + "    fragment2.appendChild(p);\n"

            + "    if (!fragment1.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment1.moveBefore(span, p);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"SPAN", "DIV", "P", "3", "A"},
            FF_ESR = "no moveBefore()")
    public void moveBefore_preservesChildren() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var spanA = document.createElement('span');\n"
            + "    spanA.textContent = 'A';\n"
            + "    var spanB = document.createElement('span');\n"
            + "    spanB.textContent = 'B';\n"
            + "    var spanC = document.createElement('span');\n"
            + "    spanC.textContent = 'C';\n"
            + "    div.appendChild(spanA);\n"
            + "    div.appendChild(spanB);\n"
            + "    div.appendChild(spanC);\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(p);\n"
            + "    fragment.appendChild(div);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(div, p);\n"
            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"
            + "    log(div.children.length);\n"
            + "    log(div.children[0].textContent);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"text1", "myClass"},
            FF_ESR = "no moveBefore()")
    public void moveBefore_preservesAttributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    div.setAttribute('data-test', 'text1');\n"
            + "    div.className = 'myClass';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(div);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(div, span);\n"
            + "    log(div.getAttribute('data-test'));\n"
            + "    log(div.className);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF_ESR = "no moveBefore()")
    public void moveBefore_returnValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(div);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    var result = fragment.moveBefore(div, span);\n"
            + "    log(result === undefined);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"DIV", "P", "SPAN", "SECTION"},
            FF_ESR = "no moveBefore()")
    public void moveBefore_multipleOperations() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    var p = document.createElement('p');\n"
            + "    p.id = 'p1';\n"
            + "    var section = document.createElement('section');\n"
            + "    section.id = 'section1';\n"
            + "    fragment.appendChild(div);\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(p);\n"
            + "    fragment.appendChild(section);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(section, span);\n"
            + "    fragment.moveBefore(p, section);\n"
            + "    fragment.moveBefore(span, section);\n"

            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "    log(fragment.childNodes[2].tagName);\n"
            + "    log(fragment.childNodes[3].tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2", "DIV", "SPAN"},
            FF_ESR = "no moveBefore()")
    public void moveBefore_nullReferenceNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(div);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(span, null);\n"

            + "    log(fragment.childNodes.length);\n"
            + "    log(fragment.childNodes[0].tagName);\n"
            + "    log(fragment.childNodes[1].tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NotFoundError/DOMException",
            FF_ESR = "no moveBefore()")
    public void moveBefore_detachedReferenceNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var divDetached = document.createElement('div');\n"
            + "    fragment.appendChild(div);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment.moveBefore(div, divDetached);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "HierarchyRequestError/DOMException",
            FF_ESR = "no moveBefore()")
    @HtmlUnitNYI(CHROME = "success", EDGE = "success", FF = "success")
    public void moveBefore_detachedMovedNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var divDetached = document.createElement('div');\n"
            + "    fragment.appendChild(div);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment.moveBefore(divDetached, div);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "no moveBefore()")
    public void moveBefore_missingArgs() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment.moveBefore();\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "no moveBefore()")
    public void moveBefore_missingReferenceNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    fragment.appendChild(div);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment.moveBefore(div);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF_ESR = "no moveBefore()")
    @HtmlUnitNYI(CHROME = "false", EDGE = "false", FF = "false")
    public void moveBefore_staysInDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'div1';\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    fragment.appendChild(span);\n"
            + "    fragment.appendChild(div);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    fragment.moveBefore(div, span);\n"
            + "    log(fragment.contains(div));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2", "SPAN", "DIV", "1", "SPAN", "3", "SPAN", "DIV", "DIV", "0", "1", "SPAN"},
            FF_ESR = "no moveBefore()")
    public void moveBefore_nullLevelUp() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    fragment.appendChild(span);\n"
            + "    var div1 = document.createElement('div');\n"
            + "    div1.id = 'div1';\n"
            + "    fragment.appendChild(div1);\n"
            + "    var div2 = document.createElement('div');\n"
            + "    div2.id = 'div2';\n"
            + "    div1.appendChild(div2);\n"
            + "    span = document.createElement('span');\n"
            + "    div2.appendChild(span);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    log(fragment.children.length);\n"
            + "    log(fragment.children[0].tagName);\n"
            + "    log(fragment.children[1].tagName);\n"

            + "    log(div2.children.length);\n"
            + "    log(div2.children[0].tagName);\n"

            + "    fragment.moveBefore(div2, null);\n"

            + "    log(fragment.children.length);\n"
            + "    log(fragment.children[0].tagName);\n"
            + "    log(fragment.children[1].tagName);\n"
            + "    log(fragment.children[2].tagName);\n"

            + "    log(div1.children.length);\n"
            + "    log(div2.children.length);\n"
            + "    log(div2.children[0].tagName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "no moveBefore()")
    public void moveBefore_wrongMovedNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    fragment.appendChild(span);\n"

            + "    var parent = document.getElementById('parent');\n"
            + "    var div = document.getElementById('div');\n"
            + "    var divDetached = document.createElement('div');\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment.moveBefore('HtmlUnit', null);\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "no moveBefore()")
    public void moveBefore_wrongReferenceNodeNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var span = document.createElement('span');\n"
            + "    span.id = 'span1';\n"
            + "    fragment.appendChild(span);\n"

            + "    if (!fragment.moveBefore) { log('no moveBefore()'); return; }\n"

            + "    try {\n"
            + "      fragment.moveBefore(span, 'HtmlUnit');\n"
            + "      log('success');\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
