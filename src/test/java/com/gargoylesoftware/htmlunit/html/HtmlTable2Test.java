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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlTable}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlTable2Test extends WebDriverTestCase {

    /**
     * Table can have multiple children of &lt;thead&gt;, &lt;tbody&gt; and &lt;tfoot&gt;.
     * Also, IE adds TR between THEAD and TD if missing.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "TBODY->TR->TD->Two", "THEAD->TR->TD->One", "THEAD->TR->TD->Three" })
    public void two_theads() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    for (var child = myTable1.firstChild; child != null; child = child.nextSibling) {\n"
            + "      alert(debug(child));\n"
            + "    }\n"
            + "  }\n"
            + "  function debug(node) {\n"
            + "    return node.nodeValue != null ? node.nodeValue : (node.nodeName + '->' + debug(node.firstChild));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<table id='myTable1'>"
            + "<td>Two</td>"
            + "<thead><td>One</td></thead>"
            + "<thead><tr><td>Three</td></tr></thead>"
            + "</table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
