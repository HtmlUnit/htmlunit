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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FunctionWrapper}.
 *
 * @author Ahmed Ashour
 */
public class FunctionsWrapperTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void function_toString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var x = test.toString;\n"
            + "  x.guid = 1;\n"
            + "  x[0] = 2;\n"
            + "  log(x.guid);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void symbolHasInstance() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Function.prototype.toString instanceof Function);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Function]", "[object Function]", "[object Function]"})
    public void symbolToStringTag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var toString = Object.prototype.toString;\n"
            + "  log(toString.call(Function.prototype.toString));\n"
            + "  log(toString.call(Object.prototype.toString));\n"
            + "  log(toString.call(Array.prototype.toString));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "false", "false"})
    public void symbolPropertyAccess() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var fn = Function.prototype.toString;\n"
                + "  log(fn[Symbol.toStringTag]);\n"
                + "  log(fn[Symbol.toPrimitive]);\n"
                + "  log(Symbol.toStringTag in fn);\n"
                + "  log(Symbol.toPrimitive in fn);\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "hello", "true"})
    public void symbolPropertyWriteRead() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var fn = Function.prototype.toString;\n"
            + "  var sym = Symbol('test');\n"
            + "  log(fn[sym]);\n"
            + "  fn[sym] = 'hello';\n"
            + "  log(fn[sym]);\n"
            + "  log(sym in fn);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello", "true", "undefined", "false"})
    public void symbolPropertyDelete() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var fn = Function.prototype.toString;\n"
            + "  var sym = Symbol('del');\n"
            + "  fn[sym] = 'hello';\n"
            + "  log(fn[sym]);\n"
            + "  log(sym in fn);\n"
            + "  delete fn[sym];\n"
            + "  log(fn[sym]);\n"
            + "  log(sym in fn);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

}
