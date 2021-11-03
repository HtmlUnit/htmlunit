/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLFieldSetElement}.
 *
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLFieldSetElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
                       "undefined", "undefined", "undefined", "undefined", "undefined"},
            IE = {"left", "right", "bottom", "middle",
                  "top", "absBottom", "absMiddle", "baseline", "textTop", "", ""})
    @HtmlUnitNYI(IE = {"left", "right", "", "", "", "", "", "", "", "", ""})
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <form>\n"
            + "    <fieldset id='f1' align='left' ></fieldset>\n"
            + "    <fieldset id='f2' align='right' ></fieldset>\n"
            + "    <fieldset id='f3' align='bottom' ></fieldset>\n"
            + "    <fieldset id='f4' align='middle' ></fieldset>\n"
            + "    <fieldset id='f5' align='top' ></fieldset>\n"
            + "    <fieldset id='f6' align='absbottom' ></fieldset>\n"
            + "    <fieldset id='f7' align='absmiddle' ></fieldset>\n"
            + "    <fieldset id='f8' align='baseline' ></fieldset>\n"
            + "    <fieldset id='f9' align='texttop' ></fieldset>\n"
            + "    <fieldset id='f10' align='wrong' ></fieldset>\n"
            + "    <fieldset id='f11' ></fieldset>\n"
            + "  </form>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 11; i++) {\n"
            + "    log(document.getElementById('f' + i).align);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"CenTer", "8", "foo", "left", "right",
                       "bottom", "middle", "top", "absbottom", "absmiddle", "baseline", "texttop"},
           IE = {"center", "error", "center", "error", "center", "left", "right",
                 "bottom", "middle", "top", "absBottom", "absMiddle", "baseline", "textTop"})
    @NotYetImplemented(IE)
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <form>\n"
            + "    <fieldset id='i1' align='left' />\n"
            + "  <form>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) { log('error'); }\n"
            + "    log(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('i1');\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLFormElement]")
    public void form() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <fieldset id='a' />\n"
            + "  </form>"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    log(document.getElementById('a').form);\n"
            + "  </script>"
            + "</body>"
            + "</html>";
        loadPageVerifyTitle2(html);
    }
}
