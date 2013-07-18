/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
 * Unit tests for {@link HTMLOListElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLOListElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "false", "true", "true", "true", "null", "", "blah", "2",
                   "true", "false", "true", "false", "", "null", "", "null" },
        IE = { "false", "true", "true", "true", "false", "true", "true", "true",
               "true", "false", "true", "false", "true", "false", "true", "false" })
    public void compact() throws Exception {
        final String html =
                "<html>\n"
                + "  <head>\n"
                + "    <script>\n"
                + "      function test() {\n"
                + "        alert(document.getElementById('o1').compact);\n"
                + "        alert(document.getElementById('o2').compact);\n"
                + "        alert(document.getElementById('o3').compact);\n"
                + "        alert(document.getElementById('o4').compact);\n"
                + "        alert(document.getElementById('o1').getAttribute('compact'));\n"
                + "        alert(document.getElementById('o2').getAttribute('compact'));\n"
                + "        alert(document.getElementById('o3').getAttribute('compact'));\n"
                + "        alert(document.getElementById('o4').getAttribute('compact'));\n"

                + "        document.getElementById('o1').compact = true;\n"
                + "        document.getElementById('o2').compact = false;\n"
                + "        document.getElementById('o3').compact = 'xyz';\n"
                + "        document.getElementById('o4').compact = null;\n"
                + "        alert(document.getElementById('o1').compact);\n"
                + "        alert(document.getElementById('o2').compact);\n"
                + "        alert(document.getElementById('o3').compact);\n"
                + "        alert(document.getElementById('o4').compact);\n"
                + "        alert(document.getElementById('o1').getAttribute('compact'));\n"
                + "        alert(document.getElementById('o2').getAttribute('compact'));\n"
                + "        alert(document.getElementById('o3').getAttribute('compact'));\n"
                + "        alert(document.getElementById('o4').getAttribute('compact'));\n"
                + "      }\n"
                + "    </script>\n"
                + "  </head>\n"
                + "  <body onload='test()'>\n"
                + "    <ol id='o1'><li>a</li><li>b</li></ol>\n"
                + "    <ol compact='' id='o2'><li>a</li><li>b</li></ol>\n"
                + "    <ol compact='blah' id='o3'><li>a</li><li>b</li></ol>\n"
                + "    <ol compact='2' id='o4'><li>a</li><li>b</li></ol>\n"
                + "  </body>\n"
                + "</html>";
        loadPageWithAlerts2(html);
    }
}
