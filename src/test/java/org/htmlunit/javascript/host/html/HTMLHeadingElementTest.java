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
 * Tests for {@link HTMLHeadingElement}.
 * @author Ronald Brill
 */
public class HTMLHeadingElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"left", "right", "center", "justify", "wrong", ""})
    public void getAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <h1 id='h1' align='left' ></h1>\n"
            + "  <h2 id='h2' align='right' ></h2>\n"
            + "  <h3 id='h3' align='center' ></h3>\n"
            + "  <h2 id='h4' align='justify' ></h2>\n"
            + "  <h2 id='h5' align='wrong' ></h2>\n"
            + "  <h2 id='h6' ></h2>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    log(document.getElementById('h' + i).align);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CenTer", "8", "foo", "left", "right", "center", "justify"})
    public void setAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <h2 id='e1' align='left' ></h2>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "    log(elem.align);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"37", "27", "22", "18", "16", "12"},
            FF = {"38", "28", "22", "18", "16", "13"},
            FF_ESR = {"38", "28", "22", "18", "16", "13"})
    public void clientHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var elt = document.getElementById('h1');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h2');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h3');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h4');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h5');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h6');\n"
            + "      log(elt.clientHeight);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <h1 id='h1'>HtmlUnit</h1>\n"
            + "  <h2 id='h2'>HtmlUnit</h2>\n"
            + "  <h3 id='h3'>HtmlUnit</h3>\n"
            + "  <h4 id='h4'>HtmlUnit</h4>\n"
            + "  <h5 id='h5'>HtmlUnit</h5>\n"
            + "  <h6 id='h6'>HtmlUnit</h6>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "0"})
    public void clientHeightEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var elt = document.getElementById('h1');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h2');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h3');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h4');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h5');\n"
            + "      log(elt.clientHeight);\n"
            + "      var elt = document.getElementById('h6');\n"
            + "      log(elt.clientHeight);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <h1 id='h1'></h1>\n"
            + "  <h2 id='h2'></h2>\n"
            + "  <h3 id='h3'></h3>\n"
            + "  <h4 id='h4'></h4>\n"
            + "  <h5 id='h5'></h5>\n"
            + "  <h6 id='h6'></h6>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
