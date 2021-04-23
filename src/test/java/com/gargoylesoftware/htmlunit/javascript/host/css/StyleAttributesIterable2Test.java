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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link StyleAttributes}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class StyleAttributesIterable2Test extends WebDriverTestCase {

    /**
     * Some properties exist only in {@link CSSStyleDeclaration}, not in {@link ComputedCSSStyleDeclaration}.
     * Seems Rhino checks for prototype, so they will always be available, even for {@link ComputedCSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "done",
            IE = {"in style", "done"})
    @HtmlUnitNYI(IE = {"in style", "in computed style", "done"})
    public void notInComputed() throws Exception {
        styleVsComputed("pixelBottom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"in style", "in computed style", "done"})
    public void inComputed() throws Exception {
        styleVsComputed("wordBreak");
    }

    private void styleVsComputed(final String property) throws Exception {
        final String html =
            "<html><head><script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    for (var i in e.style) {\n"
            + "      if (i == '" + property + "') {\n"
            + "        alert('in style');\n"
            + "      }\n"
            + "    }\n"
            + "    for (var i in window.getComputedStyle(e, null)) {\n"
            + "      if (i == '" + property + "') {\n"
            + "        alert('in computed style');\n"
            + "      }\n"
            + "    }\n"
            + "    alert('done');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
