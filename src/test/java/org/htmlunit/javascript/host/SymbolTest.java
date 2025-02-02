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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for Symbol.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SymbolTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.iterator)", "true"})
    public void iterator() throws Exception {
        name("iterator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.match)", "true"})
    public void match() throws Exception {
        name("match");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.replace)", "true"})
    public void replace() throws Exception {
        name("replace");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.search)", "true"})
    public void search() throws Exception {
        name("search");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.split)", "true"})
    public void split() throws Exception {
        name("split");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.hasInstance)", "true"})
    public void hasInstance() throws Exception {
        name("hasInstance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.isConcatSpreadable)", "true"})
    public void isConcatSpreadable() throws Exception {
        name("isConcatSpreadable");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.unscopables)", "true"})
    public void unscopables() throws Exception {
        name("unscopables");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.species)", "true"})
    public void species() throws Exception {
        name("species");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.toPrimitive)", "true"})
    public void toPrimitive() throws Exception {
        name("toPrimitive");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"symbol", "Symbol(Symbol.toStringTag)", "true"})
    public void toStringTag() throws Exception {
        name("toStringTag");
    }

    private void name(final String name) throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (!window.Symbol) { log('not supported'); return; }\n"
            + "    log(typeof Symbol." + name + ");\n"
            + "    log(Symbol." + name + " ? Symbol." + name + ".toString() : '-');\n"
            + "    log(Symbol." + name + " === Symbol." + name + ");\n"
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
    @Alerts({"Symbol()", "Symbol(foo)", "Symbol(Symbol.iterator)", "TypeError"})
    public void string() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (!window.Symbol) { log('not supported'); return; }\n"
            + "    log(Symbol().toString());\n"
            + "    log(Symbol('foo').toString());\n"
            + "    log(Symbol.iterator.toString());\n"
            + "    try { log(Symbol.replace) } catch(e) { log(e.name); };\n"
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
    @Alerts({"Symbol()", "Symbol(foo)", "Symbol(Symbol.iterator)"})
    public void defaultValue() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (!window.Symbol) { log('not supported'); return; }\n"
            + "    try {\n"
            + "      log(Symbol().toString());\n"
            + "      log(Symbol('foo').toString());\n"
            + "      log(Symbol.iterator.toString());\n"
            + "    } catch(e) {log(e.name)}\n"
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
    @Alerts({"function", "symbol", "symbol", "symbol"})
    public void typeOf() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (!window.Symbol) { log('not supported'); return; }\n"
            + "    try {\n"
            + "      log(typeof Symbol);\n"
            + "      log(typeof Symbol());\n"
            + "      log(typeof Symbol('foo'));\n"
            + "      log(typeof Symbol.iterator);\n"
            + "    } catch(e) {log(e.name)}\n"
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
    @Alerts({"false", "true", "Symbol(mario)"})
    public void symbolFor() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (!window.Symbol) { log('not supported'); return; }\n"
            + "    try {\n"
            + "      log(Symbol('bar') === Symbol('bar'));\n"
            + "      log(Symbol.for('bar') === Symbol.for('bar'));\n"

            + "      var sym = Symbol.for('mario');\n"
            + "      log(sym.toString());\n"
            + "    } catch(e) {log(e.name)}\n"
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
    @Alerts({"true", "false"})
    public void symbolForGlobal() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  if (window.Symbol) { globSym = Symbol.for('global'); }\n"
            + "</script>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (!window.Symbol) { log('not supported'); return; }\n"
            + "    try {\n"
            + "      log(Symbol.for('global') === globSym);\n"
            + "      log(Symbol('global') === globSym);\n"
            + "    } catch(e) {log(e.name)}\n"
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
    @Alerts({"TypeError", "TypeError"})
    public void symbolNew() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (!window.Symbol) { log('not supported'); return; }\n"
            + "    try {\n"
            + "      new Symbol();\n"
            + "    } catch(e) {log(e.name)}\n"
            + "    try {\n"
            + "      new Symbol('foo');\n"
            + "    } catch(e) {log(e.name)}\n"
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
    @Alerts({"TypeError", "TypeError"})
    public void globalSymbolRegistry() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (!window.Symbol) { log('not supported'); return; }\n"
            + "    try {\n"
            + "      new Symbol();\n"
            + "    } catch(e) {log(e.name)}\n"
            + "    try {\n"
            + "      new Symbol('foo');\n"
            + "    } catch(e) {log(e.name)}\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("called")
    public void inFunction() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.Symbol) {\n"
            + "    [].forEach.call('_', function(e) {\n"
            + "      var x = Symbol.toPrimitive;\n"
            + "      log('called');\n"
            + "    });\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("called")
    public void inFunction2() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.Symbol) {\n"
            + "    [].forEach.call('_', function(e) {\n"
            + "      try {\n"
            + "        var x = Symbol('hello');\n"
            + "        log('called');\n"
            + "      } catch(e) {log(e.name);}\n"
            + "    });\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function")
    public void prototypeAddFunction() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  if (window.Symbol) {\n"
                + "    Symbol.prototype.myCustomFunction = function() {};\n"
                + "    log(typeof Symbol.prototype.myCustomFunction);\n"
                + "  }\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("object")
    public void prototypeTypeOf() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  if (window.Symbol) {\n"
                + "    log(typeof Symbol.prototype);\n"
                + "  }\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }
}
