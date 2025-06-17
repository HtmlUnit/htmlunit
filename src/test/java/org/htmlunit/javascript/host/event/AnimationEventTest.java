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
package org.htmlunit.javascript.host.event;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.javascript.host.animations.AnimationEvent;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link AnimationEvent}.
 *
 * @author Ronald Brill
 */
public class AnimationEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    log(event);\n"
            + "    log(event.type);\n"
            + "    log(event.bubbles);\n"
            + "    log(event.cancelable);\n"
            + "    log(event.composed);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object AnimationEvent]", "animationstart", "false", "false", "false"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new AnimationEvent('animationstart');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object AnimationEvent]", "", "false", "false", "false"},
            FF = "NotSupportedError/DOMException",
            FF_ESR = "NotSupportedError/DOMException")
    public void create_createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('AnimationEvent');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void inWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('AnimationEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for feature-request #229.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"animationstart", "animationend"})
    @HtmlUnitNYI(CHROME = {},
            EDGE = {},
            FF = {},
            FF_ESR = {})
    public void animate() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<style>\n"
            + "  .animate {  animation: identifier .1s ; }\n"
            + "  @keyframes identifier {\n"
            + "    0% { width: 0px; }\n"
            + "    100% { width: 30px; }\n"
            + "  }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var el = document.getElementById('div1');\n"
            + "  el.addEventListener('animationstart', function(e) {\n"
            + "    log(e.type);\n"
            + "  });\n"
            + "  el.addEventListener('animationend', function(e) {\n"
            + "    log(e.type);\n"
            + "  });\n"
            + "  el.className = 'animate';\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div id='div1'>TXT</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        Thread.sleep(DEFAULT_WAIT_TIME.toMillis() / 2);
        verifyTitle2(driver, getExpectedAlerts());
    }
}
