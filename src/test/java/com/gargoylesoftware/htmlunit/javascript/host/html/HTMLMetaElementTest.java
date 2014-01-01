/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLMetaElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLMetaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "undefined", "text/html; charset=utf-8", "Content-Type", "", "", "undefined" },
            IE = { "", "text/html; charset=utf-8", "Content-Type", "", "", "" })
    public void name() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <meta http-equiv='Content-Type' content='text/html; charset=utf-8'>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var meta = document.getElementsByTagName('meta')[0];\n"
            + "        alert(meta.charset);\n"
            + "        alert(meta.content);\n"
            + "        alert(meta.httpEquiv);\n"
            + "        alert(meta.name);\n"
            + "        alert(meta.scheme);\n"
            + "        alert(meta.url);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

}
