/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link KeyboardEvent}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class KeyboardEventTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "exception", FF = { "[object KeyboardEvent]", "[object KeyboardEvent]" })
    public void createEvent() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.createEvent('KeyEvents'));\n"
            + "      alert(document.createEvent('KeyboardEvent'));\n"
            + "    } catch(e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "exception", FF = { "0-0", "undefined-undefined" })
    public void keyCode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyEvents');\n"
            + "      var mouseEvent = document.createEvent('MouseEvents');\n"
            + "      alert(keyEvent.keyCode + '-' + keyEvent.charCode);\n"
            + "      alert(mouseEvent.keyCode + '-' + mouseEvent.charCode);\n"
            + "    } catch(e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "exception",
            FF = { "keydown, true, true, true, true, true, true, 65, 0",
                "keyup, false, false, false, false, false, false, 32, 0" })
    public void initKeyEvent() throws Exception {
        final String html = "<html><head><script>\n"
            + "  var properties = ['type', 'bubbles', 'cancelable', /*'view',*/ 'ctrlKey', 'altKey',\n"
            + "        'shiftKey', 'metaKey', 'keyCode', 'charCode'];\n"
            + "  function dumpEvent(e) {\n"
            + "    var str = '';\n"
            + "    for (var i=0; i<properties.length; ++i) str += ', ' + e[properties[i]];\n"
            + "    alert(str.substring(2));\n"
            + "  }\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var keyEvent = document.createEvent('KeyEvents');\n"
            + "      keyEvent.initKeyEvent('keydown', true, true, null, true, true, true, true, 65, 65);\n"
            + "      dumpEvent(keyEvent);\n"
            + "      keyEvent = document.createEvent('KeyEvents');\n"
            + "      keyEvent.initKeyEvent('keyup', false, false, null, false, false, false, false, 32, 32);\n"
            + "      dumpEvent(keyEvent);\n"
            + "    } catch(e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
