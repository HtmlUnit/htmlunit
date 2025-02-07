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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

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
            FF_ESR = "[object CSS2Properties]")
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
    @Alerts({"0", "null", "null", "1", "myDiv", "myDiv"})
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
    @Alerts({"0", "null", "null", "0", "null", "null", "1", "myDiv", "myDiv"})
    public void firstElementChildTextNode() throws Exception {
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

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true"})
    public void ownerDocument() throws Exception {
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
        final String content = "<html>\n"
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
}
