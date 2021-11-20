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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link IntersectionObserver}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class IntersectionObserverTest extends WebDriverTestCase {

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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('IntersectionObserver' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function", "function", "function", "function"},
            IE = "no IntersectionObserver")
    public void functions() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.IntersectionObserver) {\n"
            + "    var callback = function (entries, observer) {};\n"
            + "    var observer = new IntersectionObserver(callback);\n"
            + "    log(typeof observer.observe);\n"
            + "    log(typeof observer.unobserve);\n"
            + "    log(typeof observer.disconnect);\n"
            + "    log(typeof observer.takeRecords);\n"
            + "  } else { log('no IntersectionObserver'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
