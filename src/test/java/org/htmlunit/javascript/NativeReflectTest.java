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
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for the various TypedArray's.
 *
 * @author Ronald Brill
 */
public class NativeReflectTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getReceiverDefaultsToTarget() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var o = {};\n"
                + "  Object.defineProperty(o, 'p', {\n"
                + "    get() { return this === o; }\n"
                + "  });\n"
                + "  log('' + Reflect.get(o, 'p'));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false",
            FF = "false",
            FF_ESR = "false")
    public void getReceiverPassedToGetter() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var target = {};\n"
                + "  var receiver = {};\n"
                + "  Object.defineProperty(target, 'p', {\n"
                + "    get() { return this === receiver; }\n"
                + "  });\n"
                + "  log('' + Reflect.get(target, 'p', receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false",
            FF = "false",
            FF_ESR = "false")
    public void getReceiverPassedToGetterOnPrototype() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var receiver = {};\n"
                + "  var proto = {};\n"
                + "  Object.defineProperty(proto, 'p', {\n"
                + "    get() { return this === receiver; }\n"
                + "  });\n"
                + "  var target = Object.create(proto);\n"
                + "  log('' + Reflect.get(target, 'p', receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("42")
    public void getReceiverDoesNotAffectDataProperty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var target = { p: 42 };\n"
                + "  var receiver = { p: 99 };\n"
                + "  log('' + Reflect.get(target, 'p', receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false",
            FF = "false",
            FF_ESR = "false")
    public void getReceiverWithSymbolKey() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var sym = Symbol('test');\n"
                + "  var target = {};\n"
                + "  var receiver = {};\n"
                + "  Object.defineProperty(target, sym, {\n"
                + "    get() { return this === receiver; }\n"
                + "  });\n"
                + "  log('' + Reflect.get(target, sym, receiver));"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true undefined true")
    public void setMissingValueArgumentTreatedAsUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let target = {};\n"
                + "  let result = Reflect.set(target, 'p');\n"
                + "  log('' + result + ' ' + target.p + ' ' + (target.p === undefined));"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void setMissingValueWithReceiverTreatedAsUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let res = [];\n"
                + "  let proxy = new Proxy({}, {\n"
                + "       set: function(t, prop, value, receiver) {\n"
                + "               res.push(value === undefined);\n"
                + "               return true;\n"
                + "             }\n"
                + "     });\n"
                + "  Reflect.set(proxy, 'p');\n"
                + "  log('' + res);"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
