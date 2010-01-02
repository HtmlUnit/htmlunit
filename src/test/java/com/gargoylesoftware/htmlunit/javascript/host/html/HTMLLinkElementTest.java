/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HTMLLinkElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLLinkElementTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "", "", "§§URL§§test.css", "text/css", "stylesheet" })
    public void basicLinkAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "    <body onload='test()'>\n"
            + "        <script>\n"
            + "            function test() {\n"
            + "                var s = document.createElement('link');\n"
            + "                alert(s.href);\n"
            + "                alert(s.type);\n"
            + "                alert(s.rel);\n"
            + "                s.href= 'test.css';\n"
            + "                s.type = 'text/css';\n"
            + "                s.rel = 'stylesheet';\n"
            + "                alert(s.href);\n"
            + "                alert(s.type);\n"
            + "                alert(s.rel);\n"
            + "            }\n"
            + "        </script>\n"
            + "    </body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

}
