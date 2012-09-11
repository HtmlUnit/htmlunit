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
 * @version $Revision: 7286 $
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
            IE = { "left", "right", "center", "justify", "bottom", "middle",
                "top", "absbottom", "absmiddle", "baseline", "texttop", "", "" })
    @NotYetImplemented(Browser.IE)
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form>\n"
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
            + "</form>\n"

            + "<script>\n"
            + "  alert(i1.align);\n"
            + "  alert(i2.align);\n"
            + "  alert(i3.align);\n"
            + "  alert(i4.align);\n"
            + "  alert(i5.align);\n"
            + "  alert(i6.align);\n"
            + "  alert(i7.align);\n"
            + "  alert(i8.align);\n"
            + "  alert(i9.align);\n"
            + "  alert(i10.align);\n"
            + "  alert(i11.align);\n"
            + "  alert(i12.align);\n"
            + "  alert(i13.align);\n"
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
            IE = { "center", "error", "center", "error", "center" })
    @NotYetImplemented
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form>\n"
            + "  <img id='i1' align='left' />\n"
            + "</form>\n"

            + "<script>\n"
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"

            + "  var elem = document.getElementById('i1');\n"
            + "  setAlign(elem, 'CenTer');\n"
            + "  alert(elem.align);\n"

            + "  setAlign(i1, '8');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'foo');\n"
            + "  alert(i1.align);\n"

            + "  setAlign(i1, 'left');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'right');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'center');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'justify');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'bottom');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'middle');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'top');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'absbottom');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'absmiddle');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'baseline');\n"
            + "  alert(i1.align);\n"
            + "  setAlign(i1, 'texttop');\n"
            + "  alert(i1.align);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
