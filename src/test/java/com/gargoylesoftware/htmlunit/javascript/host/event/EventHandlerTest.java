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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link EventHandler}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class EventHandlerTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void caller() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(test.caller);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTrue(driver.getTitle().contains("function onload(event)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function onload(event) { test() }",
             "function onload(event) { test() }",
             "function onload(event) { test() }"})
    @HtmlUnitNYI(CHROME = {"function onload(event) { test(); }",
                           "function onload(event) { test(); }",
                           "function onload(event) { test(); }"},
            EDGE = {"function onload(event) { test(); }",
                    "function onload(event) { test(); }",
                    "function onload(event) { test(); }"},
            FF = {"function onload(event) { test(); }",
                  "function onload(event) { test(); }",
                  "function onload(event) { test(); }"},
            FF78 = {"function onload(event) { test(); }",
                    "function onload(event) { test(); }",
                    "function onload(event) { test(); }"},
            IE = {"function onload(event) { test(); }",
                  "function onload(event) { test(); }",
                  "function onload(event) { test(); }"})
    public void testToString() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = test.caller;\n"
            + "    log(e);\n"
            + "    log('' + e);\n"
            + "    log(e.toString());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
