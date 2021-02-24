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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlLink}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlLinkTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("body onLoad")
    public void onLoad() throws Exception {
        getWebClientWithMockWebConnection().getOptions().setCssEnabled(false);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");

        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <link rel='stylesheet' href='simple.css' "
                        + "onload='alert(\"onLoad\")' onerror='alert(\"onError\")'>\n"
                + "</head>\n"
                + "<body onload='alert(\"body onLoad\")'>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts(html);
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoadDynamic() throws Exception {
        getWebClientWithMockWebConnection().getOptions().setCssEnabled(false);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");

        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var dynLink = document.createElement('link');\n"
                + "      dynLink.rel = 'stylesheet';\n"
                + "      dynLink.type = 'text/css';\n"
                + "      dynLink.href = 'simple.css';"
                + "      dynLink.onload = function (e) { log(\"onLoad \" + e) };\n"
                + "      dynLink.onerror = function (e) { log(\"onError \" + e) };\n"
                + "      document.head.appendChild(dynLink);\n"

                + "      var dynLink = document.createElement('link');\n"
                + "      dynLink.rel = 'stylesheet';\n"
                + "      dynLink.type = 'text/css';\n"
                + "      dynLink.href = 'unknown.css';"
                + "      dynLink.onload = function (e) { log(\"onLoad \" + e) };\n"
                + "      dynLink.onerror = function (e) { log(\"onError \" + e) };\n"
                + "      document.head.appendChild(dynLink);\n"
                + "    }\n"

                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'></body>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final String text = page.getElementById("log").getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);

        assertEquals(1, getMockWebConnection().getRequestCount());
    }
}
