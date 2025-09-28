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
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for css pseudo selectors :is(), :where() and :has().
 *
 * @author Ronald Brill
 *
 */
public class CSSSelector3Test extends WebDriverTestCase {

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
}
