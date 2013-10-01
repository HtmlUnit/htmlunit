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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE6;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLDivElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLDivElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no",
            IE = "yes")
    public void doScroll() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var div = document.getElementById('d');\n"
            + "        if(div.doScroll) {\n"
            + "          alert('yes');\n"
            + "          div.doScroll();\n"
            + "          div.doScroll('down');\n"
            + "        } else {\n"
            + "          alert('no');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><div id='d'>abc</div></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "left", "right", "justify", "center", "wrong", "" },
            IE = { "left", "right", "justify", "center", "", "" })
    @NotYetImplemented({ IE6, IE8 })
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table>\n"
            + "    <div id='d1' align='left' ></div>\n"
            + "    <div id='d2' align='right' ></div>\n"
            + "    <div id='d3' align='justify' ></div>\n"
            + "    <div id='d4' align='center' ></div>\n"
            + "    <div id='d5' align='wrong' ></div>\n"
            + "    <div id='d6' ></div>\n"
            + "  </table>\n"

            + "<script>\n"
            + "  for (i=1; i<=6; i++) {\n"
            + "    alert(document.getElementById('d'+i).align);\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "CenTer", "8", "foo", "left", "right", "justify", "center" },
            IE = { "center", "error", "center", "error", "center", "left", "right", "justify", "center" })
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <table>\n"
            + "    <div id='d1' align='left' ></div>\n"
            + "  </table>\n"

            + "<script>\n"
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) { alert('error'); }\n"
            + "    alert(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('d1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'justify');\n"
            + "  setAlign(elem, 'center');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * A similar test is used by jQuery-1.4.1 to detect browser capacities.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "null", "true", "null", "true" },
            IE6 = { "undefined", "false", "undefined", "false" },
            IE8 = { "undefined", "false", "undefined", "false" })
    public void handlers() throws Exception {
        final String html
            = "<html><body>\n"
            + "<div id='d1'></div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d1');\n"
            + "alert(d.onchange);\n"
            + "alert('onchange' in d);\n"
            + "alert(d.onsubmit);\n"
            + "alert('onsubmit' in d);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
