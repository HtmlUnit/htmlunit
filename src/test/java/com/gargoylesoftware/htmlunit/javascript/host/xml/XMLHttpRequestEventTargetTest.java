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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link XMLHttpRequestEventTarget}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequestEventTargetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void inWindow() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('XMLHttpRequestEventTarget' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("exception")
    public void ctor() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  try {\n"
                + "    var xhr = new XMLHttpRequestEventTarget();\n"
                + "    log(xhr);\n"
                + "  } catch(e) { log('exception'); }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function get onabort() { [native code] }",
                       "function set onabort() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function onabort() { [native code] }",
                  "function onabort() { [native code] }",
                  "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                    "function onabort() { [native code] }",
                    "function onabort() { [native code] }",
                    "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                  " function onabort() { [native code] } ",
                  " function onabort() { [native code] } ",
                  "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function onabort() { [native code] }",
                           "function onabort() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function onabort() { [native code] }",
                    "function onabort() { [native code] }",
                    "true", "true"})
    public void getOwnPropertyDescriptor_onabort() throws Exception {
        getOwnPropertyDescriptor("onabort");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function get onerror() { [native code] }",
                       "function set onerror() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function onerror() { [native code] }",
                  "function onerror() { [native code] }",
                  "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                    "function onerror() { [native code] }",
                    "function onerror() { [native code] }",
                    "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                  " function onerror() { [native code] } ",
                  " function onerror() { [native code] } ",
                  "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function onerror() { [native code] }",
                           "function onerror() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function onerror() { [native code] }",
                    "function onerror() { [native code] }",
                    "true", "true"})
    public void getOwnPropertyDescriptor_onerror() throws Exception {
        getOwnPropertyDescriptor("onerror");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function get onload() { [native code] }",
                       "function set onload() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function onload() { [native code] }",
                  "function onload() { [native code] }",
                  "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                    "function onload() { [native code] }",
                    "function onload() { [native code] }",
                    "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                  " function onload() { [native code] } ",
                  " function onload() { [native code] } ",
                  "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function onload() { [native code] }",
                           "function onload() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function onload() { [native code] }",
                    "function onload() { [native code] }",
                    "true", "true"})
    public void getOwnPropertyDescriptor_onload() throws Exception {
        getOwnPropertyDescriptor("onload");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function get onloadstart() { [native code] }",
                       "function set onloadstart() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function onloadstart() { [native code] }",
                  "function onloadstart() { [native code] }",
                  "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                    "function onloadstart() { [native code] }",
                    "function onloadstart() { [native code] }",
                    "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                  " function onloadstart() { [native code] } ",
                  " function onloadstart() { [native code] } ",
                  "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function onloadstart() { [native code] }",
                           "function onloadstart() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function onloadstart() { [native code] }",
                    "function onloadstart() { [native code] }",
                    "true", "true"})
    public void getOwnPropertyDescriptor_onloadstart() throws Exception {
        getOwnPropertyDescriptor("onloadstart");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function get onloadend() { [native code] }",
                       "function set onloadend() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function onloadend() { [native code] }",
                  "function onloadend() { [native code] }",
                  "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                    "function onloadend() { [native code] }",
                    "function onloadend() { [native code] }",
                    "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                  " function onloadend() { [native code] } ",
                  " function onloadend() { [native code] } ",
                  "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function onloadend() { [native code] }",
                           "function onloadend() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function onloadend() { [native code] }",
                    "function onloadend() { [native code] }",
                    "true", "true"})
    public void getOwnPropertyDescriptor_onloadend() throws Exception {
        getOwnPropertyDescriptor("onloadend");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function get onprogress() { [native code] }",
                       "function set onprogress() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function onprogress() { [native code] }",
                  "function onprogress() { [native code] }",
                  "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                    "function onprogress() { [native code] }",
                    "function onprogress() { [native code] }",
                    "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                  " function onprogress() { [native code] } ",
                  " function onprogress() { [native code] } ",
                  "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function onprogress() { [native code] }",
                           "function onprogress() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function onprogress() { [native code] }",
                    "function onprogress() { [native code] }",
                    "true", "true"})
    public void getOwnPropertyDescriptor_onprogress() throws Exception {
        getOwnPropertyDescriptor("onprogress");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("undefined")
    public void getOwnPropertyDescriptor_onreadystatechange() throws Exception {
        getOwnPropertyDescriptor("onreadystatechange");
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts(DEFAULT = {"[object Object]", "undefined", "undefined",
                       "function get ontimeout() { [native code] }",
                       "function set ontimeout() { [native code] }",
                       "true", "true"},
            FF = {"[object Object]", "undefined", "undefined",
                  "function ontimeout() { [native code] }",
                  "function ontimeout() { [native code] }",
                  "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                    "function ontimeout() { [native code] }",
                    "function ontimeout() { [native code] }",
                    "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                  " function ontimeout() { [native code] } ",
                  " function ontimeout() { [native code] } ",
                  "true", "true"})
    @HtmlUnitNYI(CHROME = {"[object Object]", "undefined", "undefined",
                           "function ontimeout() { [native code] }",
                           "function ontimeout() { [native code] }",
                           "true", "true"},
            EDGE = {"[object Object]", "undefined", "undefined",
                    "function ontimeout() { [native code] }",
                    "function ontimeout() { [native code] }",
                    "true", "true"})
    public void getOwnPropertyDescriptor_ontimeout() throws Exception {
        getOwnPropertyDescriptor("ontimeout");
    }

    private void getOwnPropertyDescriptor(final String event) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var request;\n"
            + "      function test() {\n"
            + "        var desc = Object.getOwnPropertyDescriptor("
                                + "XMLHttpRequestEventTarget.prototype, '" + event + "');\n"
            + "        log(desc);\n"
            + "        if(!desc) { return; }\n"

            + "        log(desc.value);\n"
            + "        log(desc.writable);\n"
            + "        log(desc.get);\n"
            + "        log(desc.set);\n"
            + "        log(desc.configurable);\n"
            + "        log(desc.enumerable);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
