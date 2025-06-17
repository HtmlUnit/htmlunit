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

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link KeyboardEvent}.
 *
 * Note that special key seems to have '0' {@code .which} with FF.
 * And no keypress event in IE.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class KeyboardEvent2Test extends SimpleWebTestCase {

    /**
     * Test .type('a').
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("keydown:65,0,65 keypress:97,97,97 keyup:65,0,65")
    public void a() throws Exception {
        final HtmlPage page = getHtmlPage();
        page.getDocumentElement().type('a');
        final String log = page.getHtmlElementById("log").asNormalizedText();
        assertTrue(log, log.contains(getExpectedAlerts()[0]));
    }

    private HtmlPage getHtmlPage() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><title>foo</title><script>\n"
                + "  function test() {\n"
                + "    document.onkeydown = checkEvent;\n"
                + "    document.onkeyup = checkEvent;\n"
                + "    document.onkeypress = checkEvent;\n"
                + "  }\n"
                + "\n"
                + "  function checkEvent(e) {\n"
                + "    if (!e) var e = window.event;\n"
                + "    var string = e.keyCode + ',' + e.charCode + ',' + e.which;\n"
                + "    log(e.type + ':' + string + ' ');\n"
                + "    if (e.type == 'keyup') {\n"
                + "      log('\\n');\n"
                + "    }\n"
                + "  }\n"
                + "\n"
                + "  function log(msg) {\n"
                + "    document.getElementById('log').value += msg;\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "  <textarea id='log' rows=40 cols=80></textarea>\n"
                + "</body></html>";
        return loadPage(html);
    }

    /**
     * Test .type(KeyboardEvent.DOM_VK_A).
     * @throws Exception if the test fails
     */
    @Test
    public void dom_vk_a() throws Exception {
        final HtmlPage page = getHtmlPage();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> page.getDocumentElement().type(KeyboardEvent.DOM_VK_A));
    }

    /**
     * Test .type(KeyboardEvent.DOM_VK_F2).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "keydown:113,0,113 keyup:113,0,113",
            FF = "keydown:113,0,113 keypress:113,0,0 keyup:113,0,113",
            FF_ESR = "keydown:113,0,113 keypress:113,0,0 keyup:113,0,113")
    public void dom_vk_f2() throws Exception {
        final HtmlPage page = getHtmlPage();
        page.getDocumentElement().type(KeyboardEvent.DOM_VK_F2);
        final String log = page.getHtmlElementById("log").asNormalizedText();
        assertTrue(log, log.contains(getExpectedAlerts()[0]));
    }

    /**
     * Test .type(KeyboardEvent.DOM_VK_RIGHT).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "keydown:39,0,39 keyup:39,0,39",
            FF = "keydown:39,0,39 keypress:39,0,0 keyup:39,0,39",
            FF_ESR = "keydown:39,0,39 keypress:39,0,0 keyup:39,0,39")
    public void dom_vk_right() throws Exception {
        final HtmlPage page = getHtmlPage();
        page.getDocumentElement().type(KeyboardEvent.DOM_VK_RIGHT);
        final String log = page.getHtmlElementById("log").asNormalizedText();
        assertTrue(log, log.contains(getExpectedAlerts()[0]));
    }
}
