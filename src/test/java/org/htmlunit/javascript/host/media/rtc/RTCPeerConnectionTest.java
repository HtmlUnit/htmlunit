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
package org.htmlunit.javascript.host.media.rtc;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link RTCPeerConnection}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class RTCPeerConnectionTest extends WebDriverTestCase {

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
            + "      log('RTCPeerConnection' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object RTCPeerConnection]", "", "false"},
            CHROME = {"[object RTCPeerConnection]", "[object RTCPeerConnection]", "true"},
            EDGE = {"[object RTCPeerConnection]", "[object RTCPeerConnection]", "true"})
    public void webkitRTCPeerConnection() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if ('RTCPeerConnection' in window) {\n"
            + "    var pc = new RTCPeerConnection();\n"
            + "    var wkPc = '';\n"
            + "    if (typeof(webkitRTCPeerConnection) == 'function') {\n"
            + "      wkPc = new webkitRTCPeerConnection();\n"
            + "    }\n"
            + "    log(pc);\n"
            + "    log(wkPc);\n"
            + "    log(Object.getPrototypeOf(pc) == Object.getPrototypeOf(wkPc));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
