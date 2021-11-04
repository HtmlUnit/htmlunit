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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

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
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.iterator)", "true"},
            IE = "not supported")
    public void iterator() throws Exception {
        name("iterator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.match)", "true"},
            IE = "not supported")
    public void match() throws Exception {
        name("match");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.replace)", "true"},
            IE = "not supported")
    public void replace() throws Exception {
        name("replace");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.search)", "true"},
            IE = "not supported")
    public void search() throws Exception {
        name("search");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.split)", "true"},
            IE = "not supported")
    public void split() throws Exception {
        name("split");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.hasInstance)", "true"},
            IE = "not supported")
    public void hasInstance() throws Exception {
        name("hasInstance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.isConcatSpreadable)", "true"},
            IE = "not supported")
    public void isConcatSpreadable() throws Exception {
        name("isConcatSpreadable");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.unscopables)", "true"},
            IE = "not supported")
    public void unscopables() throws Exception {
        name("unscopables");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.species)", "true"},
            IE = "not supported")
    public void species() throws Exception {
        name("species");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.toPrimitive)", "true"},
            IE = "not supported")
    public void toPrimitive() throws Exception {
        name("toPrimitive");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"symbol", "Symbol(Symbol.toStringTag)", "true"},
            IE = "not supported")
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
    @Alerts(DEFAULT = {"Symbol()", "Symbol(foo)", "Symbol(Symbol.iterator)", "exception"},
            IE = "not supported")
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
            + "    try { log(Symbol.replace) } catch(e) { log('exception'); };\n"
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
    @Alerts(DEFAULT = {"Symbol()", "Symbol(foo)", "Symbol(Symbol.iterator)"},
            IE = "not supported")
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
            + "    } catch(e) {log('exception')}\n"
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
    @Alerts(DEFAULT = {"function", "symbol", "symbol", "symbol"},
            IE = "not supported")
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
            + "    } catch(e) {log('exception')}\n"
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
    @Alerts(DEFAULT = {"false", "true", "Symbol(mario)"},
            IE = "not supported")
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
            + "    } catch(e) {log('exception')}\n"
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
    @Alerts(DEFAULT = {"true", "false"},
            IE = "not supported")
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
            + "    } catch(e) {log('exception')}\n"
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
    @Alerts(DEFAULT = {"exception", "exception"},
            IE = "not supported")
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
            + "    } catch(e) {log('exception')}\n"
            + "    try {\n"
            + "      new Symbol('foo');\n"
            + "    } catch(e) {log('exception')}\n"
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
    @Alerts(DEFAULT = {"exception", "exception"},
            IE = "not supported")
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
            + "    } catch(e) {log('exception')}\n"
            + "    try {\n"
            + "      new Symbol('foo');\n"
            + "    } catch(e) {log('exception')}\n"
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
    @Alerts(DEFAULT = "called",
            IE = {})
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
    @Alerts(DEFAULT = "called",
            IE = {})
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
            + "      } catch(e) {log('exception');}\n"
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
    @Alerts(DEFAULT = "function",
            IE = {})
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
    @Alerts(DEFAULT = "object",
            IE = {})
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
