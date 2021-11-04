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
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLHRElement}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLHRElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"left", "right", "center", "wrong", ""},
            IE = {"left", "right", "center", "", ""})
    @NotYetImplemented(IE)
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <hr id='h1' align='left' />\n"
            + "  <hr id='h2' align='right' />\n"
            + "  <hr id='h3' align='center' />\n"
            + "  <hr id='h4' align='wrong' />\n"
            + "  <hr id='h5' />\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 5; i++) {\n"
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
    @Alerts(DEFAULT = {"CenTer", "8", "foo", "left", "right", "center"},
            IE = {"center", "error", "center", "error", "center", "left", "right", "center"})
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <hr id='h1' align='left' />\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) { log('error'); }\n"
            + "    log(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('h1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'center');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
