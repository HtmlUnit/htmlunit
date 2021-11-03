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
package com.gargoylesoftware.htmlunit.javascript.host.network;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link NetworkInformation}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NetworkInformationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined"},
            CHROME = {"[object NetworkInformation]", "undefined", "undefined"},
            EDGE = {"[object NetworkInformation]", "undefined", "undefined"})
    public void navigatorConnection() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(navigator.connection);\n"
            + "    log(navigator.mozConnection);\n"
            + "    log(navigator.webkitConnection);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no connection",
            CHROME = "undefined",
            EDGE = "undefined")
    public void type() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;\n"
            + "    if (connection) {"
            + "      log(connection.type);\n"
            + "    } else {\n"
            + "      log('no connection');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no connection",
            CHROME = "undefined",
            EDGE = "undefined")
    public void downlinkMax() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;\n"
            + "    if (connection) {"
            + "      log(connection.downlinkMax);\n"
            + "    } else {\n"
            + "      log('no connection');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no connection",
            CHROME = "4g",
            EDGE = "4g")
    public void effectiveType() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;\n"
            + "    if (connection) {"
            + "      log(connection.effectiveType);\n"
            + "    } else {\n"
            + "      log('no connection');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no connection",
            CHROME = "10",
            EDGE = "10")
    public void downlink() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;\n"
            + "    if (connection) {"
            + "      log(connection.downlink);\n"
            + "    } else {\n"
            + "      log('no connection');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no connection",
            CHROME = "50",
            EDGE = "50")
    public void rtt() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;\n"
            + "    if (connection) {"
            + "      log(connection.rtt);\n"
            + "    } else {\n"
            + "      log('no connection');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

}
