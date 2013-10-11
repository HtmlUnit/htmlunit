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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLEmbedElement}.
 * @version $Revision$
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLEmbedElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "left", "right", "bottom", "middle", "top",
                        "absbottom", "absmiddle", "baseline", "texttop", "wrong", "" },
            FF17 = { "left", "right", "bottom", "middle", "top",
                    "absbottom", "absmiddle", "bottom", "texttop", "wrong", "" },
            IE = { "undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                "undefined", "undefined", "undefined", "undefined", "undefined" })
    @NotYetImplemented
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <embed id='e1' align='left' ></embed>\n"
            + "  <embed id='e2' align='right' ></embed>\n"
            + "  <embed id='e3' align='bottom' ></embed>\n"
            + "  <embed id='e4' align='middle' ></embed>\n"
            + "  <embed id='e5' align='top' ></embed>\n"
            + "  <embed id='e6' align='absbottom' ></embed>\n"
            + "  <embed id='e7' align='absmiddle' ></embed>\n"
            + "  <embed id='e8' align='baseline' ></embed>\n"
            + "  <embed id='e9' align='texttop' ></embed>\n"
            + "  <embed id='e10' align='wrong' ></embed>\n"
            + "  <embed id='e11' ></embed>\n"

            + "<script>\n"
            + "  for (i=1; i<=11; i++) {\n"
            + "    alert(document.getElementById('e'+i).align);\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "CenTer", "8", "foo", "left", "right", "bottom", "middle", "top",
                        "absbottom", "absmiddle", "baseline", "texttop" },
            FF17 = { "CenTer", "8", "foo", "left", "right", "bottom", "middle", "top",
                    "absbottom", "absmiddle", "bottom", "texttop" },
            IE8 = { "center", "error", "center", "error", "center", "left", "right", "bottom", "middle", "top",
                    "absbottom", "absmiddle", "baseline", "texttop" })
    @NotYetImplemented({ IE8, FF17 })
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <embed id='e1' align='left' ></embed>\n"

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
            + "  setAlign(elem, 'bottom');\n"
            + "  setAlign(elem, 'middle');\n"
            + "  setAlign(elem, 'top');\n"
            + "  setAlign(elem, 'absbottom');\n"
            + "  setAlign(elem, 'absmiddle');\n"
            + "  setAlign(elem, 'baseline');\n"
            + "  setAlign(elem, 'texttop');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
