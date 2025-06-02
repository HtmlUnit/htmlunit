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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DOMRectList}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DOMRectListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "1", "[object DOMRect]", "[object DOMRect]"})
    public void getClientRects() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var rects = d1.getClientRects();\n"
            + "    log(rects);\n"
            + "    log(rects.length);\n"
            + "    log(rects[0]);\n"
            + "    log(rects.item(0));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "1", "null", "null"})
    public void itemOutside() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var rects = d1.getClientRects();\n"

            + "    log(rects);\n"
            + "    log(rects.length);\n"

            + "    try {\n"
            + "      log(rects.item(1));\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      log(rects.item(-1));\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "1", "undefined", "undefined"})
    public void indexOutside() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var rects = d1.getClientRects();\n"

            + "    log(rects);\n"
            + "    log(rects.length);\n"

            + "    try {\n"
            + "      log(rects[1]);\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      log(rects[-1]);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DOMRectList]", "0", "undefined", "undefined"})
    public void empty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var d1 = document.createElement('div');\n"
            + "    var rects = d1.getClientRects();\n"

            + "    log(rects);\n"
            + "    log(rects.length);\n"

            + "    try {\n"
            + "      log(rects[1]);\n"
            + "    } catch(e) { logEx(e); }\n"

            + "    try {\n"
            + "      log(rects[-1]);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
