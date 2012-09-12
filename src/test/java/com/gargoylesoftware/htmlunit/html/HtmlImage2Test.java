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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlImage2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "left", "right", "center", "justify", "bottom", "middle",
                "top", "absbottom", "absmiddle", "baseline", "texttop", "wrong", "" },
            FF10 = { "left", "right", "middle", "justify", "bottom", "middle",
                "top", "absbottom", "absmiddle", "bottom", "texttop", "wrong", "" },
            IE = { "left", "right", "center", "", "bottom", "middle",
                "top", "absBottom", "absMiddle", "baseline", "textTop", "", "" })
    @NotYetImplemented(Browser.IE)
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <img id='i1' align='left' />\n"
            + "  <img id='i2' align='right' />\n"
            + "  <img id='i3' align='center' />\n"
            + "  <img id='i4' align='justify' />\n"
            + "  <img id='i5' align='bottom' />\n"
            + "  <img id='i6' align='middle' />\n"
            + "  <img id='i7' align='top' />\n"
            + "  <img id='i8' align='absbottom' />\n"
            + "  <img id='i9' align='absmiddle' />\n"
            + "  <img id='i10' align='baseline' />\n"
            + "  <img id='i11' align='texttop' />\n"
            + "  <img id='i12' align='wrong' />\n"
            + "  <img id='i13' />\n"

            + "<script>\n"
            + "  alert(document.getElementById('i1').align);\n"
            + "  alert(document.getElementById('i2').align);\n"
            + "  alert(document.getElementById('i3').align);\n"
            + "  alert(document.getElementById('i4').align);\n"
            + "  alert(document.getElementById('i5').align);\n"
            + "  alert(document.getElementById('i6').align);\n"
            + "  alert(document.getElementById('i7').align);\n"
            + "  alert(document.getElementById('i8').align);\n"
            + "  alert(document.getElementById('i9').align);\n"
            + "  alert(document.getElementById('i10').align);\n"
            + "  alert(document.getElementById('i11').align);\n"
            + "  alert(document.getElementById('i12').align);\n"
            + "  alert(document.getElementById('i13').align);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "CenTer", "8", "foo", "left", "right", "center", "justify",
                "bottom", "middle", "top", "absbottom", "absmiddle", "baseline", "texttop" },
            FF10 = { "CenTer", "8", "foo", "left", "right", "middle", "justify",
                "bottom", "middle", "top", "absbottom", "absmiddle", "bottom", "texttop" },
            IE = { "center", "error", "center", "error", "center", "left", "right",
                "center", "error", "center", "bottom", "middle", "top", "absBottom",
                "absMiddle", "baseline", "textTop" })
    @NotYetImplemented
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <img id='i1' align='left' />\n"

            + "<script>\n"
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "      alert(elem.align);\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"

            + "  var elem = document.getElementById('i1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'center');\n"
            + "  setAlign(elem, 'justify');\n"
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
