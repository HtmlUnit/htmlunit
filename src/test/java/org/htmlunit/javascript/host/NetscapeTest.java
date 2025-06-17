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
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Netscape}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class NetscapeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "TypeError"},
            FF = {"[object Object]", "undefined", "[object Object]", "undefined"},
            FF_ESR = {"[object Object]", "undefined", "[object Object]", "undefined"})
    public void netscape() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(window.netscape);\n"
            + "  log(window.Netscape);\n"
            + "  log(window.netscape.security);\n"
            + "  log(window.netscape.security.PrivilegeManager);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            FF = {"true", "false", "true"},
            FF_ESR = {"true", "false", "true"})
    @HtmlUnitNYI(FF = {"undefined", "true", "true"},
            FF_ESR = {"undefined", "true", "true"})
    public void netscapeDescriptor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var d1 = Object.getOwnPropertyDescriptor(window, 'netscape');\n"
            + "  log(d1.writable);\n"
            + "  log(d1.enumerable);\n"
            + "  log(d1.configurable);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }
}
