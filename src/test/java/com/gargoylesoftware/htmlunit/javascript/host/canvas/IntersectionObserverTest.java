/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link IntersectionObserver}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class IntersectionObserverTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            CHROME = {"function", "function", "function", "function"})
    public void functions() throws Exception {
        final String html =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  if (window.IntersectionObserver) {\n"
            + "    var callback = function (entries, observer) {};\n"
            + "    var observer = new IntersectionObserver(callback);\n"
            + "    alert(typeof observer.observe);\n"
            + "    alert(typeof observer.unobserve);\n"
            + "    alert(typeof observer.disconnect);\n"
            + "    alert(typeof observer.takeRecords);\n"
            + " }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
