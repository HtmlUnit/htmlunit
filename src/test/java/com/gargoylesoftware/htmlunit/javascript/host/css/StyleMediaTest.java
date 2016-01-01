/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link StyleMedia}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StyleMediaTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object StyleMedia]", "screen" },
            FF = "undefined")
    public void type() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "      alert(window.styleMedia);\n"
            + "      if (window.styleMedia) {\n"
            + "        alert(window.styleMedia.type);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "false" },
            FF = "")
    public void matchMedium() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "      if (window.styleMedia) {\n"
            + "        alert(window.styleMedia.matchMedium('screen'));\n"
            + "        alert(window.styleMedia.matchMedium('SCREEN'));\n"
            + "        alert(window.styleMedia.matchMedium('screen, handheld'));\n"
            + "        alert(window.styleMedia.matchMedium('handheld'));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
