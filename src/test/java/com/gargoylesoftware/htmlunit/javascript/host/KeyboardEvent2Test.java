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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link KeyboardEvent}.
 *
 * Note that special key seems to have '0' .which with FF.
 * And no keypress event in IE.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class KeyboardEvent2Test extends WebTestCase {

    /**
     * Test .type('a').
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "keydown:65 keypress:97 keyup:65", FF = "keydown:65,0,65 keypress:0,97,97 keyup:65,0,65")
    public void a() throws Exception {
        final HtmlPage page = getHtmlPage();
        page.getDocumentElement().type('a');
        final String log = page.getHtmlElementById("log").asText();
        assertTrue(log.contains(getExpectedAlerts()[0]));
    }

    private HtmlPage getHtmlPage() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
                + "  function test() {\n"
                + "    document.onkeydown = checkEvent;\n"
                + "    document.onkeyup = checkEvent;\n"
                + "    document.onkeypress = checkEvent\n"
                + "  }\n"
                + "\n"
                + "  function checkEvent(e) {\n"
                + "    if (!e) var e = window.event;\n"
                + "    var string;\n"
                + "    if (window.event) {\n"
                + "      string = e.keyCode;\n"
                + "    } else {\n"
                + "      string = e.keyCode + ',' + e.charCode + ',' + e.which;\n"
                + "    }\n"
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
    @Test(expected = IllegalArgumentException.class)
    public void dom_vk_a() throws Exception {
        final HtmlPage page = getHtmlPage();
        page.getDocumentElement().type(KeyboardEvent.DOM_VK_A);
    }

    /**
     * Test .type(KeyboardEvent.DOM_VK_F2).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "keydown:113 keyup:113", FF = "keydown:113,0,113 keypress:113,0,0 keyup:113,0,113")
    public void dom_vk_f2() throws Exception {
        final HtmlPage page = getHtmlPage();
        page.getDocumentElement().type(KeyboardEvent.DOM_VK_F2);
        final String log = page.getHtmlElementById("log").asText();
        assertTrue(log.contains(getExpectedAlerts()[0]));
    }

    /**
     * Test .type(KeyboardEvent.DOM_VK_RIGHT).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "keydown:39 keyup:39", FF = "keydown:39,0,39 keypress:39,0,0 keyup:39,0,39 ")
    public void dom_vk_right() throws Exception {
        final HtmlPage page = getHtmlPage();
        page.getDocumentElement().type(KeyboardEvent.DOM_VK_RIGHT);
        final String log = page.getHtmlElementById("log").asText();
        assertTrue(log.contains(getExpectedAlerts()[0]));
    }
}
