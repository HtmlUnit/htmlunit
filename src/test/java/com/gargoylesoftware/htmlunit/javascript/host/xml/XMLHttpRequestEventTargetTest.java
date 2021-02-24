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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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
            + "    function test() {\n"
            + "      alert('XMLHttpRequestEventTarget' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts("exception")
    public void ctor() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var xhr = new XMLHttpRequestEventTarget();\n"
                + "    alert(xhr);\n"
                + "  } catch(e) { alert('exception'); }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
        loadPageWithAlerts2(html);
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
                        "function onabort() {\n    [native code]\n}",
                        "function onabort() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function onabort() {\n    [native code]\n}",
                        "function onabort() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onabort() {\n    [native code]\n}\n",
                        "\nfunction onabort() {\n    [native code]\n}\n",
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
                        "function onerror() {\n    [native code]\n}",
                        "function onerror() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function onerror() {\n    [native code]\n}",
                        "function onerror() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onerror() {\n    [native code]\n}\n",
                        "\nfunction onerror() {\n    [native code]\n}\n",
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
                        "function onload() {\n    [native code]\n}",
                        "function onload() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function onload() {\n    [native code]\n}",
                        "function onload() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onload() {\n    [native code]\n}\n",
                        "\nfunction onload() {\n    [native code]\n}\n",
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
                        "function onloadstart() {\n    [native code]\n}",
                        "function onloadstart() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function onloadstart() {\n    [native code]\n}",
                        "function onloadstart() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onloadstart() {\n    [native code]\n}\n",
                        "\nfunction onloadstart() {\n    [native code]\n}\n",
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
                        "function onloadend() {\n    [native code]\n}",
                        "function onloadend() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function onloadend() {\n    [native code]\n}",
                        "function onloadend() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onloadend() {\n    [native code]\n}\n",
                        "\nfunction onloadend() {\n    [native code]\n}\n",
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
                        "function onprogress() {\n    [native code]\n}",
                        "function onprogress() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function onprogress() {\n    [native code]\n}",
                        "function onprogress() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction onprogress() {\n    [native code]\n}\n",
                        "\nfunction onprogress() {\n    [native code]\n}\n",
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
                        "function ontimeout() {\n    [native code]\n}",
                        "function ontimeout() {\n    [native code]\n}",
                        "true", "true"},
            FF78 = {"[object Object]", "undefined", "undefined",
                        "function ontimeout() {\n    [native code]\n}",
                        "function ontimeout() {\n    [native code]\n}",
                        "true", "true"},
            IE = {"[object Object]", "undefined", "undefined",
                        "\nfunction ontimeout() {\n    [native code]\n}\n",
                        "\nfunction ontimeout() {\n    [native code]\n}\n",
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
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function test() {\n"
            + "        var desc = Object.getOwnPropertyDescriptor("
                                + "XMLHttpRequestEventTarget.prototype, '" + event + "');\n"
            + "        alert(desc);\n"
            + "        if(!desc) { return; }\n"

            + "        alert(desc.value);\n"
            + "        alert(desc.writable);\n"
            + "        alert(desc.get);\n"
            + "        alert(desc.set);\n"
            + "        alert(desc.configurable);\n"
            + "        alert(desc.enumerable);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
