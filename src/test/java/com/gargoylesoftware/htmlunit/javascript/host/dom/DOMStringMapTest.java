/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link DOMStringMap}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class DOMStringMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "there" },
            IE8 = { })
    public void get() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    if (document.body.dataset) {\n"
            + "      alert(document.body.dataset.hi);\n"
            + "      alert(document.body.dataset.hello);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()' data-hello='there'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "old", "old", "null", "null" },
            IE8 = { })
    public void put() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    if (document.body.dataset) {\n"
            + "      document.body.dataset.dateOfBirth = 'old';\n"
            + "      alert(document.body.dataset.dateOfBirth);\n"
            + "      alert(document.body.getAttribute('data-date-of-birth'));\n"
            + "      document.body.dataset.dateOfBirth = null;\n"
            + "      alert(document.body.dataset.dateOfBirth);\n"
            + "      alert(document.body.getAttribute('data-date-of-birth'));\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
