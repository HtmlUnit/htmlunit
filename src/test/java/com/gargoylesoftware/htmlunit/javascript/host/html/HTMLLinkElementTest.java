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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLLinkElement}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLLinkElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", "", "", "§§URL§§test.css", "text/css", "stylesheet", "stylesheet1"})
    public void basicLinkAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var s = document.createElement('link');\n"
            + "        alert(s.href);\n"
            + "        alert(s.type);\n"
            + "        alert(s.rel);\n"
            + "        alert(s.rev);\n"
            + "        s.href = 'test.css';\n"
            + "        s.type = 'text/css';\n"
            + "        s.rel  = 'stylesheet';\n"
            + "        s.rev  = 'stylesheet1';\n"
            + "        alert(s.href);\n"
            + "        alert(s.type);\n"
            + "        alert(s.rel);\n"
            + "        alert(s.rev);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "alternate help", "prefetch", "prefetch", "not supported", "notsupported"})
    public void readWriteRel() throws Exception {
        final String html
            = "<html><body><link id='l1'><link id='l2' rel='alternate help'><script>\n"
            + "var l1 = document.getElementById('l1'), l2 = document.getElementById('l2');\n"

            + "alert(l1.rel);\n"
            + "alert(l2.rel);\n"

            + "l1.rel = 'prefetch';\n"
            + "l2.rel = 'prefetch';\n"
            + "alert(l1.rel);\n"
            + "alert(l2.rel);\n"

            + "l1.rel = 'not supported';\n"
            + "l2.rel = 'notsupported';\n"
            + "alert(l1.rel);\n"
            + "alert(l2.rel);\n"

            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "2", "alternate", "help"},
            IE = "exception")
    public void relList() throws Exception {
        final String html
            = "<html><body><link id='l1'><link id='l2' rel='alternate help'><script>\n"
            + "var l1 = document.getElementById('l1'), l2 = document.getElementById('l2');\n"

            + "try {\n"
            + "  alert(l1.relList.length);\n"
            + "  alert(l2.relList.length);\n"

            + "  for (var i = 0; i < l2.relList.length; i++) {\n"
            + "    alert(l2.relList[i]);\n"
            + "  }\n"
            + "} catch(e) { alert('exception'); }\n"

            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
