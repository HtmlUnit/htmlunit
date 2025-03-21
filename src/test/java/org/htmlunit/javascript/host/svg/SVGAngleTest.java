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
package org.htmlunit.javascript.host.svg;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link SVGAngle}.
 *
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class SVGAngleTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function SVGAngle() { [native code] }", "0", "1", "2", "3", "4"})
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(window.SVGAngle);\n"
            + "    if (window.SVGAngle) {\n"
            + "      log(SVGAngle.SVG_ANGLETYPE_UNKNOWN);\n"
            + "      log(SVGAngle.SVG_ANGLETYPE_UNSPECIFIED);\n"
            + "      log(SVGAngle.SVG_ANGLETYPE_DEG);\n"
            + "      log(SVGAngle.SVG_ANGLETYPE_RAD);\n"
            + "      log(SVGAngle.SVG_ANGLETYPE_GRAD);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
