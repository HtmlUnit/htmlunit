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
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    @Alerts("[object HTMLDataElement]")
    public void tag() throws Exception {
        final String html = "<html><body>\n"
            + "  <data id='it' value='1234'>onetwothreefour</data>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.getElementById('it'));\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1234", "#12o", "", "#12o"})
    public void value() throws Exception {
        final String html = "<html><body>\n"
            + "  <data id='d1' value='1234'>onetwothreefour</data>\n"
            + "  <data id='d2' >onetwothreefour</data>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var dat = document.getElementById('d1');\n"
            + "  log(dat.value);\n"
            + "  dat.value = '#12o';\n"
            + "  log(dat.value);\n"

            + "  dat = document.getElementById('d2');\n"
            + "  log(dat.value);\n"
            + "  dat.value = '#12o';\n"
            + "  log(dat.value);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
