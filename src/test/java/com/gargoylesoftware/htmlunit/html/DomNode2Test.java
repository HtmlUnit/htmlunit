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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link DomNode}.
 *
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DomNode2Test extends WebDriverTestCase {

    /**
     * Test for Bug #1253.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"exception", "0"})
    public void appendChild_recursive() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var e = document.createElement('div');\n"
                + "  try {\n"
                + "    log(e.appendChild(e) === e);\n"
                + "  } catch(e) {log('exception');}\n"
                + "  log(e.childNodes.length);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for Bug #1253.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "exception", "1", "0"})
    public void appendChild_recursive_parent() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var e1 = document.createElement('div');\n"
                + "  var e2 = document.createElement('div');\n"
                + "  try {\n"
                + "    log(e1.appendChild(e2) === e2);\n"
                + "    log(e2.appendChild(e1) === e1);\n"
                + "  } catch(e) {log('exception');}\n"
                + "  log(e1.childNodes.length);\n"
                + "  log(e2.childNodes.length);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void ownerDocument() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document == document.body.ownerDocument);\n"
            + "      log(document == document.getElementById('foo').ownerDocument);\n"
            + "      log(document == document.body.firstChild.ownerDocument);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "<div id='foo'>bla</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(content);
    }
}
