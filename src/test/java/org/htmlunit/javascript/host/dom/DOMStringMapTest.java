/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DOMStringMap}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class DOMStringMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "there", "heho"})
    public void get() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.body.dataset.hi);\n"
            + "  log(document.body.dataset.hello);\n"
            + "  log(document.body.dataset.helloWorld);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()' data-hello='there' data-hello-world='heho'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"heho", "ReferenceError", "world is not defined",
             "ReferenceError", "World is not defined"})
    @HtmlUnitNYI(
            CHROME = {"heho", "ReferenceError", "\"world\" is not defined",
                      "ReferenceError", "\"World\" is not defined"},
            EDGE = {"heho", "ReferenceError", "\"world\" is not defined",
                    "ReferenceError", "\"World\" is not defined"},
            FF = {"heho", "ReferenceError", "\"world\" is not defined",
                  "ReferenceError", "\"World\" is not defined"},
            FF_ESR = {"heho", "ReferenceError", "\"world\" is not defined",
                      "ReferenceError", "\"World\" is not defined"})
    public void getInvalidName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.body.dataset.helloWorld);\n"

            + "  try {\n"
            + "    log(document.body.dataset.hello-world);\n"
            + "  } catch(e) { logEx(e); log(e.message);}\n"

            + "  try {\n"
            + "    log(document.body.dataset.hello-World);\n"
            + "  } catch(e) { logEx(e); log(e.message);}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()' data-hello='hi' data-world='huhu' data-hello-world='heho'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"old", "old", "null", "null"})
    public void put() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  document.body.dataset.dateOfBirth = 'old';\n"
            + "  log(document.body.dataset.dateOfBirth);\n"
            + "  log(document.body.getAttribute('data-date-of-birth'));\n"

            + "  document.body.dataset.dateOfBirth = null;\n"
            + "  log(document.body.dataset.dateOfBirth);\n"
            + "  log(document.body.getAttribute('data-date-of-birth'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "there", "undefined", "heho", "undefined"})
    public void delete() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.body.dataset.hi);\n"
            + "  delete document.body.dataset.hi;\n"
            + "  log(document.body.dataset.hi);\n"

            + "  log(document.body.dataset.hello);\n"
            + "  delete document.body.dataset.hello;\n"
            + "  log(document.body.dataset.hello);\n"

            + "  log(document.body.dataset.helloWorld);\n"
            + "  delete document.body.dataset.helloWorld;\n"
            + "  log(document.body.dataset.helloWorld);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()' data-hello='there' data-hello-world='heho'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
