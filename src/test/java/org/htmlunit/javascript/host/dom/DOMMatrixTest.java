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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DOMMatrix}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DOMMatrixTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "function DOMMatrix() { [native code] }", "function DOMMatrix() { [native code] }"})
    public void webKitCSSMatrixIsAlias() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  if (typeof DOMMatrix == 'function') {\n"
                + "    log(WebKitCSSMatrix === DOMMatrix);\n"
                + "    log(WebKitCSSMatrix);\n"
                + "    log(DOMMatrix);\n"
                + "  } else {\n"
                + "    log('DOMMatrix not available');\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"false", "function SVGMatrix() { [native code] }", "function DOMMatrix() { [native code] }"})
    public void svgMatrixIsNotAlias() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  if (typeof DOMMatrix == 'function') {\n"
                + "    log(SVGMatrix === DOMMatrix);\n"
                + "    log(SVGMatrix);\n"
                + "    log(DOMMatrix);\n"
                + "  } else {\n"
                + "    log('DOMMatrix not available');\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
