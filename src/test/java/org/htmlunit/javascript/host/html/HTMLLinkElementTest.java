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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HTMLLinkElement}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HTMLLinkElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", "", "", "§§URL§§test.css", "text/css", "stylesheet", "stylesheet1"})
    public void basicLinkAttributes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var s = document.createElement('link');\n"
            + "        log(s.href);\n"
            + "        log(s.type);\n"
            + "        log(s.rel);\n"
            + "        log(s.rev);\n"
            + "        s.href = 'test.css';\n"
            + "        s.type = 'text/css';\n"
            + "        s.rel  = 'stylesheet';\n"
            + "        s.rev  = 'stylesheet1';\n"
            + "        log(s.href);\n"
            + "        log(s.type);\n"
            + "        log(s.rel);\n"
            + "        log(s.rev);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "alternate help", "prefetch", "prefetch", "not supported", "notsupported"})
    public void readWriteRel() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><link id='l1'><link id='l2' rel='alternate help'><script>\n"
            + LOG_TITLE_FUNCTION
            + "var l1 = document.getElementById('l1'), l2 = document.getElementById('l2');\n"

            + "log(l1.rel);\n"
            + "log(l2.rel);\n"

            + "l1.rel = 'prefetch';\n"
            + "l2.rel = 'prefetch';\n"
            + "log(l1.rel);\n"
            + "log(l2.rel);\n"

            + "l1.rel = 'not supported';\n"
            + "l2.rel = 'notsupported';\n"
            + "log(l1.rel);\n"
            + "log(l2.rel);\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "2", "alternate", "help"})
    public void relList() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><link id='l1'><link id='l2' rel='alternate help'><script>\n"
            + LOG_TITLE_FUNCTION
            + "var l1 = document.getElementById('l1'), l2 = document.getElementById('l2');\n"

            + "try {\n"
            + "  log(l1.relList.length);\n"
            + "  log(l2.relList.length);\n"

            + "  for (var i = 0; i < l2.relList.length; i++) {\n"
            + "    log(l2.relList[i]);\n"
            + "  }\n"
            + "} catch(e) { logEx(e); }\n"

            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "2", "2", "1", "alternate", "help", "abc", "alternate help", "abc"})
    public void setRelListString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><link id='l1'><link id='l2' rel='alternate help'><script>\n"
            + LOG_TITLE_FUNCTION
            + "var a1 = document.getElementById('l1'), a2 = document.getElementById('l2');\n"

            + "try {\n"
            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  a1.relList = 'alternate help';\n"
            + "  a2.relList = 'abc';\n"

            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  for (var i = 0; i < a1.relList.length; i++) {\n"
            + "    log(a1.relList[i]);\n"
            + "  }\n"

            + "  for (var i = 0; i < a2.relList.length; i++) {\n"
            + "    log(a2.relList[i]);\n"
            + "  }\n"

            + "  log(a1.rel);\n"
            + "  log(a2.rel);\n"
            + "} catch(e) { logEx(e); }\n"

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "2", "0", "0", "", "\\s\\s\\t"})
    public void setRelListStringBlank() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><link id='l1'><link id='l2' rel='alternate help'><script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "var a1 = document.getElementById('l1'), a2 = document.getElementById('l2');\n"

            + "try {\n"
            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  a1.relList = '';\n"
            + "  a2.relList = '  \t';\n"

            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  log(a1.rel);\n"
            + "  log(a2.rel);\n"
            + "} catch(e) { logEx(e); }\n"

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "2", "1", "1", "null", "null", "null", "null"})
    public void setRelListNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><link id='l1'><link id='l2' rel='alternate help'><script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "var a1 = document.getElementById('l1'), a2 = document.getElementById('l2');\n"

            + "try {\n"
            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  a1.relList = null;\n"
            + "  a2.relList = null;\n"

            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  for (var i = 0; i < a1.relList.length; i++) {\n"
            + "    log(a1.relList[i]);\n"
            + "  }\n"

            + "  for (var i = 0; i < a2.relList.length; i++) {\n"
            + "    log(a2.relList[i]);\n"
            + "  }\n"

            + "  log(a1.rel);\n"
            + "  log(a2.rel);\n"
            + "} catch(e) { logEx(e); }\n"

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "2", "1", "1", "undefined", "undefined", "undefined", "undefined"})
    public void setRelListUndefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><link id='l1'><link id='l2' rel='alternate help'><script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "var a1 = document.getElementById('l1'), a2 = document.getElementById('l2');\n"

            + "try {\n"
            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  a1.relList = undefined;\n"
            + "  a2.relList = undefined;\n"

            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  for (var i = 0; i < a1.relList.length; i++) {\n"
            + "    log(a1.relList[i]);\n"
            + "  }\n"

            + "  for (var i = 0; i < a2.relList.length; i++) {\n"
            + "    log(a2.relList[i]);\n"
            + "  }\n"

            + "  log(a1.rel);\n"
            + "  log(a2.rel);\n"
            + "} catch(e) { logEx(e); }\n"

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
