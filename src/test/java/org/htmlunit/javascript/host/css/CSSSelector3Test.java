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
package org.htmlunit.javascript.host.css;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for css pseudo selectors :not(), :is(), :where() and :has().
 *
 * @author Ronald Brill
 *
 */
public class CSSSelector3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"6", "[object HTMLBodyElement]", "[object HTMLLIElement]", "[object HTMLScriptElement]",
             "5", "[object HTMLBodyElement]", "[object HTMLScriptElement]",
             "5", "[object HTMLBodyElement]", "[object HTMLScriptElement]"})
    public void notElement() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title></title></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li>ul - item 0</li>\n"
                + "</ul>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(':not(ul)');"
                + "    log(items.length);\n"
                + "    log(items[3]);\n"
                + "    log(items[4]);\n"
                + "    log(items[5]);\n"

                + "    items = document.querySelectorAll(':not(ul, li)');"
                + "    log(items.length);\n"
                + "    log(items[3]);\n"
                + "    log(items[4]);\n"

                + "    items = document.querySelectorAll(':not(ul):not(li)');"
                + "    log(items.length);\n"
                + "    log(items[3]);\n"
                + "    log(items[4]);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void notStar() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title></title></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li>ul - item 0</li>\n"
                + "</ul>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(':not(*)');"
                + "    log(items.length);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "[object HTMLUListElement]"})
    public void notId() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title></title></head>\n"
                + "<body>\n"
                + "<ul id='foo'>\n"
                + "  <li>ul - item 0</li>\n"
                + "</ul>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('#foo:not(#bar)');"
                + "    log(items.length);\n"
                + "    log(items[0]);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "<a>my</a>", "0"})
    public void notTable() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title></title></head>\n"
                + "<body>\n"
                + "<table>\n"
                + "  <tr><td><a>my</a></td></tr>\n"
                + "</table>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('body :not(table) a');"
                + "    log(items.length);\n"
                + "    log(items[0].outerHTML);\n"

                + "    items = document.querySelectorAll('body a:not(table a)');"
                + "    log(items.length);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void notDoubleColon() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title></title></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li>ul - item 0</li>\n"
                + "</ul>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('::not(ul)');"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "[object HTMLUListElement]",
             "2", "[object HTMLUListElement]", "[object HTMLOListElement]"})
    public void isElement() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li>ul - item 0</li>\n"
                + "  <li>ul - item 1</li>\n"
                + "</ul>\n"
                + "<ol>\n"
                + "  <li>ol - item 0</li>\n"
                + "  <li>ol - item 1</li>\n"
                + "</ol>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(':is(ul)');"
                + "    log(items.length);\n"
                + "    log(items[0]);\n"

                + "    items = document.querySelectorAll(':is(ul, ol)');"
                + "    log(items.length);\n"
                + "    log(items[0]);\n"
                + "    log(items[1]);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "ul - item 1", "ol - item 1"})
    public void isAttribute() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li name='i0'>ul - item 0</li>\n"
                + "  <li name='i1'>ul - item 1</li>\n"
                + "</ul>\n"
                + "<ol>\n"
                + "  <li name='i7'>ol - item 0</li>\n"
                + "  <li name='i2'>ol - item 1</li>\n"
                + "</ol>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\":is([name='i1'], [name='i2'])\");"
                + "    log(items.length);\n"
                + "    log(items[0].innerText);\n"
                + "    log(items[1].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "ul - item 0 ul - item 1", "ul - item 1",
             "ol - item 0 ol - item 1", "ol - item 1"})
    public void isDuplicates() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li name='i0'>ul - item 0</li>\n"
                + "  <li name='i1'>ul - item 1</li>\n"
                + "</ul>\n"
                + "<ol name='i1'>\n"
                + "  <li name='i7'>ol - item 0</li>\n"
                + "  <li name='i2'>ol - item 1</li>\n"
                + "</ol>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\":is(ul, [name='i1'], [name='i2'])\");"
                + "    log(items.length);\n"
                + "    log(items[0].innerText);\n"
                + "    log(items[1].innerText);\n"
                + "    log(items[2].innerText);\n"
                + "    log(items[3].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void isDoubleColon() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title></title></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li>ul - item 0</li>\n"
                + "</ul>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('::is(ul)');"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "[object HTMLUListElement]",
             "2", "[object HTMLUListElement]", "[object HTMLOListElement]"})
    public void whereElement() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li>ul - item 0</li>\n"
                + "  <li>ul - item 1</li>\n"
                + "</ul>\n"
                + "<ol>\n"
                + "  <li>ol - item 0</li>\n"
                + "  <li>ol - item 1</li>\n"
                + "</ol>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(':where(ul)');"
                + "    log(items.length);\n"
                + "    log(items[0]);\n"

                + "    items = document.querySelectorAll(':where(ul, ol)');"
                + "    log(items.length);\n"
                + "    log(items[0]);\n"
                + "    log(items[1]);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "ul - item 1", "ol - item 1"})
    public void whereAttribute() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li name='i0'>ul - item 0</li>\n"
                + "  <li name='i1'>ul - item 1</li>\n"
                + "</ul>\n"
                + "<ol>\n"
                + "  <li name='i7'>ol - item 0</li>\n"
                + "  <li name='i2'>ol - item 1</li>\n"
                + "</ol>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\":where([name='i1'], [name='i2'])\");"
                + "    log(items.length);\n"
                + "    log(items[0].innerText);\n"
                + "    log(items[1].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "ul - item 0 ul - item 1", "ul - item 1",
             "ol - item 0 ol - item 1", "ol - item 1"})
    public void whereDuplicates() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li name='i0'>ul - item 0</li>\n"
                + "  <li name='i1'>ul - item 1</li>\n"
                + "</ul>\n"
                + "<ol name='i1'>\n"
                + "  <li name='i7'>ol - item 0</li>\n"
                + "  <li name='i2'>ol - item 1</li>\n"
                + "</ol>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\":where(ul, [name='i1'], [name='i2'])\");"
                + "    log(items.length);\n"
                + "    log(items[0].innerText);\n"
                + "    log(items[1].innerText);\n"
                + "    log(items[2].innerText);\n"
                + "    log(items[3].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void whereDoubleColon() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title></title></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li>ul - item 0</li>\n"
                + "</ul>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('::where(ul)');"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "SECTION / a0 a1"})
    public void hasDescandant() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article class='featured'>a0</article>\n"
                + "  <article>a1</article>\n"
                + "</section>\n"
                + "<section>\n"
                + "  <article>a2</article>\r\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\"section:has(.featured)\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "SECTION / a0 a1"})
    public void hasDescandantDeep() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <div>\n"
                + "    <article class='featured'>a0</article>\n"
                + "    <article>a1</article>\n"
                + "  </div>\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\"section:has(.featured)\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "SECTION / a0 a1"})
    public void hasChild() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article class='featured'>a0</article>\n"
                + "  <article>a1</article>\n"
                + "</section>\n"
                + "<section>\n"
                + "  <article>a2</article>\r\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\"section:has(> .featured)\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void hasChildDeep() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <div>\n"
                + "    <article class='featured'>a0</article>\n"
                + "    <article>a1</article>\n"
                + "  </div>\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\"section:has(> .featured)\");"
                + "    log(items.length);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "H1 / h1 1"})
    public void hasNextSiblingCombinator() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article>\n"
                + "    <h1>h1 0</h1>\n"
                + "    <p>p0</p>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <h1>h1 1</h1>\n"
                + "    <h2>h2 0</h2>\n"
                + "    <p>p1</p>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <h1>h1 2</h1>\n"
                + "    <p>p2</p>\n"
                + "    <h2>h2 1</h2>\n"
                + "  </article>\n"
                + "</section>\n"
                + "<section>\n"
                + "  <article>a2</article>\r\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\"h1:has(+ h2)\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "H1 / h1 1", "H1 / h1 2"})
    public void hasSubsequentSiblingCombinator() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article>\n"
                + "    <h1>h1 0</h1>\n"
                + "    <p>p0</p>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <h1>h1 1</h1>\n"
                + "    <h2>h2 0</h2>\n"
                + "    <p>p1</p>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <h1>h1 2</h1>\n"
                + "    <p>p2</p>\n"
                + "    <h2>h2 1</h2>\n"
                + "  </article>\n"
                + "</section>\n"
                + "<section>\n"
                + "  <article>a2</article>\r\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\"h1:has(~ h2)\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "    log(items[1].tagName + ' / ' + items[1].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "H1 / h1 1", "H2 / h2 0",
             "2", "H1 / h1 1", "H2 / h2 0"})
    public void hasIs() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article>\n"
                + "    <h1>h1 0</h1>\n"
                + "    <p>p0</p>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <h1>h1 1</h1>\n"
                + "    <h2>h2 0</h2>\n"
                + "    <h3>h3 0</h3>\n"
                + "    <p>p1</p>\n"
                + "  </article>\n"
                + "</section>\n"
                + "<section>\n"
                + "  <article>a2</article>\r\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\":is(h1, h2, h3):has(+ :is(h2, h3, h4))\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "    log(items[1].tagName + ' / ' + items[1].innerText);\n"

                + "    items = document.querySelectorAll(\":is(h1, h2, h3):has(+ h2, + h3, + h4)\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "    log(items[1].tagName + ' / ' + items[1].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "ARTICLE / h1 0", "ARTICLE / p0"})
    public void hasOr() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article>\n"
                + "    <h1>h1 0</h1>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <p>p0</p>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <div>div0</div>\n"
                + "  </article>\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\"article:has(h1, p)\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "    log(items[1].tagName + ' / ' + items[1].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "ARTICLE / h1 1 p1"})
    public void hasAnd() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article>\n"
                + "    <h1>h1 0</h1>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <p>p0</p>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <h1>h1 1</h1>\n"
                + "    <p>p1</p>\n"
                + "  </article>\n"
                + "  <article>\n"
                + "    <div>div0</div>\n"
                + "  </article>\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll(\"article:has(h1):has(p)\");"
                + "    log(items.length);\n"
                + "    log(items[0].tagName + ' / ' + items[0].innerText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "SECTION"})
    public void hasWithDescendantClass() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <div><span class='target'>inside</span></div>\n"
                + "</section>\n"
                + "<section>\n"
                + "  <div><span>no match</span></div>\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('section:has(.target)');\n"
                + "    log(items.length);\n"
                + "    log(items[0].tagName);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * :has() should match the ANCESTOR, not the element with the class itself.
     * If the bug is present, the result will include the span or return 0
     * instead of correctly returning the section.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "DIV"})
    public void hasMatchesAncestorNotTarget() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<div>\n"
                + "  <p class='highlight'>text</p>\n"
                + "</div>\n"
                + "<div>\n"
                + "  <p>other</p>\n"
                + "</div>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('div:has(.highlight)');\n"
                + "    log(items.length);\n"
                + "    log(items[0].tagName);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The element matched by :has() must NOT itself carry the class —
     * only a descendant does. This isolates the "tests element itself" bug:
     * if selects() tests the section against .featured directly, it returns
     * false (section has no class), so the result would be 0 instead of 1.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "SECTION"})
    public void hasDoesNotTestElementItself() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article class='featured'>a0</article>\n"
                + "</section>\n"
                + "<section class='featured'>\n"
                + "  <article>a1</article>\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('section:has(.featured)');\n"
                + "    log(items.length);\n"
                // Must be the FIRST section (child has class), not the second (element itself has class)
                + "    log(items[0].tagName);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * :has() with a deeply nested descendant (not a direct child).
     * Tests that the traversal goes beyond one level deep.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "ARTICLE"})
    public void hasWithDeeplyNestedDescendant() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<article>\n"
                + "  <div>\n"
                + "    <div>\n"
                + "      <span>\n"
                + "        <em id='deep'>deep</em>\n"
                + "      </span>\n"
                + "    </div>\n"
                + "  </div>\n"
                + "</article>\n"
                + "<article>\n"
                + "  <div><span>shallow</span></div>\n"
                + "</article>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('article:has(#deep)');\n"
                + "    log(items.length);\n"
                + "    log(items[0].tagName);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * :has() with a child combinator — only direct children qualify.
     * section:has(> .direct) should NOT match when .direct is a grandchild.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "SECTION"})
    public void hasWithChildCombinator() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <div class='direct'>direct child</div>\n"
                + "</section>\n"
                + "<section>\n"
                + "  <div><span class='direct'>grandchild</span></div>\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('section:has(> .direct)');\n"
                + "    log(items.length);\n"
                // Only the first section qualifies; the second has .direct as a grandchild
                + "    log(items[0].tagName);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * :has() should return 0 when no element has the matching descendant.
     * Guards against false positives from the bug.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void hasNoMatch() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<section>\n"
                + "  <article>a0</article>\n"
                + "</section>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('section:has(.featured)');\n"
                + "    log(items.length);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "SyntaxError/DOMException"})
    public void hasSizzleJQuery182InvalidContains() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    items = document.querySelectorAll(\"#form select:has(option:first-child)\");"
                + "    log(items.length);\n"

                + "    items = document.querySelectorAll(\"#form select:has(option:first-child:contains('o'))\");"
                + "    log(items.length);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void hasDoubleColon() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title></title></head>\n"
                + "<body>\n"
                + "<ul>\n"
                + "  <li>ul - item 0</li>\n"
                + "</ul>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let items = document.querySelectorAll('::has(ul)');"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"

                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
