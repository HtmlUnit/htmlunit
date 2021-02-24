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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLDataElement}.
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDataElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLDataElement]",
            IE = "[object HTMLUnknownElement]")
    public void tag() throws Exception {
        final String html = "<html><body>\n"
            + "  <data id='it' value='1234'>onetwothreefour</data>\n"
            + "<script>\n"
            + "  alert(document.getElementById('it'));\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1234", "#12o", "", "#12o"},
            IE = {"undefined", "#12o", "undefined", "#12o"})
    public void value() throws Exception {
        final String html = "<html><body>\n"
            + "  <data id='d1' value='1234'>onetwothreefour</data>\n"
            + "  <data id='d2' >onetwothreefour</data>\n"
            + "<script>\n"
            + "  var dat = document.getElementById('d1');\n"
            + "  alert(dat.value);\n"
            + "  dat.value = '#12o';\n"
            + "  alert(dat.value);\n"

            + "  dat = document.getElementById('d2');\n"
            + "  alert(dat.value);\n"
            + "  dat.value = '#12o';\n"
            + "  alert(dat.value);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
