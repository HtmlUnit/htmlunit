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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link HtmlOutput}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlOutputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "true", "true"},
            FF_ESR = {"true", "true", "false", "false", "true"},
            IE = "no checkValidity()")
    @HtmlUnitNYI(FF_ESR = {"true", "true", "true", "true", "true"})
    public void checkValidity() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"

            + "    if (!foo.checkValidity) {log('no checkValidity()'); return;}\n"

            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity(' ');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('invalid');\n"
            + "    log(foo.checkValidity());\n"

            + "    foo.setCustomValidity('');\n"
            + "    log(foo.checkValidity());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <output id='foo'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
