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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link ClientRectList}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ClientRectListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object DOMRectList]", "1", "[object DOMRect]", "[object DOMRect]"},
            IE = {"[object ClientRectList]", "1", "[object ClientRect]", "[object ClientRect]"})
    public void getClientRects() throws Exception {
        final String html =
            "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var rects = d1.getClientRects();\n"
            + "    alert(rects);\n"
            + "    alert(rects.length);\n"
            + "    alert(rects[0]);\n"
            + "    alert(rects.item(0));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object DOMRectList]", "1", "null", "null"},
            IE = {"[object ClientRectList]", "1", "exception", "exception"})
    public void itemOutside() throws Exception {
        final String html =
            "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var rects = d1.getClientRects();\n"

            + "    alert(rects);\n"
            + "    alert(rects.length);\n"

            + "    try {\n"
            + "      alert(rects.item(1));\n"
            + "    } catch(e) { alert('exception'); }\n"

            + "    try {\n"
            + "      alert(rects.item(-1));\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object DOMRectList]", "1", "undefined", "undefined"},
            IE = {"[object ClientRectList]", "1", "undefined", "undefined"})
    public void indexOutside() throws Exception {
        final String html =
            "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var rects = d1.getClientRects();\n"

            + "    alert(rects);\n"
            + "    alert(rects.length);\n"

            + "    try {\n"
            + "      alert(rects[1]);\n"
            + "    } catch(e) { alert('exception'); }\n"

            + "    try {\n"
            + "      alert(rects[-1]);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object DOMRectList]", "0", "undefined", "undefined"},
            IE = {"[object ClientRectList]", "0", "undefined", "undefined"})
    public void empty() throws Exception {
        final String html =
            "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.createElement('div');\n"
            + "    var rects = d1.getClientRects();\n"

            + "    alert(rects);\n"
            + "    alert(rects.length);\n"

            + "    try {\n"
            + "      alert(rects[1]);\n"
            + "    } catch(e) { alert('exception'); }\n"

            + "    try {\n"
            + "      alert(rects[-1]);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
