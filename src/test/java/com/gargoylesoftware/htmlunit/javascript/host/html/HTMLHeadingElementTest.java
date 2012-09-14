/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
 * Tests for {@link HTMLHeadingElement}.
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLHeadingElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "left", "right", "center", "justify", "wrong", "" },
            IE = { "left", "right", "center", "justify", "", "" })
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <h1 id='h1' align='left' ></h1>\n"
            + "  <h2 id='h2' align='right' ></h2>\n"
            + "  <h3 id='h3' align='center' ></h3>\n"
            + "  <h2 id='h4' align='justify' ></h2>\n"
            + "  <h2 id='h5' align='wrong' ></h2>\n"
            + "  <h2 id='h6' ></h2>\n"

            + "<script>\n"
            + "  for (i=1; i<=6; i++) {\n"
            + "    alert(document.getElementById('h'+i).align);\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "center", "8", "foo", "left", "right", "center", "justify" },
            IE = { "center", "error", "center", "error", "center", "left", "right", "center", "justify" })
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <h2 id='e1' align='left' ></h2>\n"

            + "<script>\n"
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) { alert('error'); }\n"
            + "    alert(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('e1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'center');\n"
            + "  setAlign(elem, 'justify');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
