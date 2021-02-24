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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link AudioProcessingEvent}.
 *
 * @author Ahmed Ashour
 * @author Madis Pärn
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class AudioProcessingEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    alert(event);\n"
            + "    alert(event.type);\n"
            + "    alert(event.bubbles);\n"
            + "    alert(event.cancelable);\n"
            + "    alert(event.composed);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new AudioProcessingEvent('audioprocessing');\n"
            + "    } catch (e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object AudioProcessingEvent]", "audioprocessing", "false", "false", "false"},
            FF = "exception",
            FF78 = "exception",
            IE = "exception")
    // audioCtx.createBuffer is missing
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception")
    public void create_ctorAllDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var audioCtx = new AudioContext();\n"
            + "      var inputBuffer = audioCtx.createBuffer(2, audioCtx.sampleRate * 3, audioCtx.sampleRate);\n"
            + "      var outputBuffer = audioCtx.createBuffer(2, audioCtx.sampleRate * 3, audioCtx.sampleRate);\n"
            + "      var event = new AudioProcessingEvent('audioprocessing', {"
            + "        'inputBuffer': inputBuffer,\n"
            + "        'outputBuffer': outputBuffer,\n"
            + "        'playbackTime': 4,\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + DUMP_EVENT_FUNCTION
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void create_ctorMissingDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new AudioProcessingEvent('audioprocessing');\n"
            + "      dump(event);\n"
            + "    } catch (e) { alert('exception') }\n"
            + DUMP_EVENT_FUNCTION
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void create_createEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('AudioProcessingEvent');\n"
            + "      dump(event);\n"
            + "    } catch (e) { document.title += 'exception' }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(" ", getExpectedAlerts()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void inWindow() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert('AudioProcessingEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for feature-request #229.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object AnimationEvent]")
    @NotYetImplemented
    public void end() throws Exception {
        final String html
            = "<html><head>\n"
            + "<style>\n"
            + "  .animate {  animation: identifier .1s ; }\n"
            + "  @keyframes identifier {\n"
            + "    0% { width: 0px; }\n"
            + "    100% { width: 30px; }\n"
            + "  }\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var el = document.getElementById('div1');\n"
            + "  el.addEventListener('animationend', function(e) {\n"
            + "    alert(e);\n"
            + "  });\n"
            + "  el.className = 'animate';\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div id='div1'>TXT</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
