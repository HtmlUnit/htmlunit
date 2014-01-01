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
 * Tests for {@link HTMLMeterElement}.
 * @version $Revision$
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLMeterElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            FF = "[object HTMLMeterElement]",
            IE8 = "[object]")
    public void tag() throws Exception {
        final String html = "<html><body>\n"
            + "<meter id='it' min='200' max='500' value='350'>\n"
            + "<script>\n"
            + "alert(document.getElementById('it'));\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { },
            FF = { "number200", "number500",
            "number200", "number500", "number350", "number350" })
    public void properties() throws Exception {
        final String html = "<html><body>\n"
            + "<meter id='it' min='200' max='500' value='350'>\n"
            + "<script>\n"
            + "var elt = document.getElementById('it');\n"
            + "if (window.HTMLMeterElement) {\n"
            + "  alert(typeof(elt.min) + elt.min);\n"
            + "  alert(typeof(elt.max) + elt.max);\n"
            + "  alert(typeof(elt.low) + elt.low);\n"
            + "  alert(typeof(elt.high) + elt.high);\n"
            + "  alert(typeof(elt.value) + elt.value);\n"
            + "  alert(typeof(elt.optimum) + elt.optimum);\n"
            + "}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
