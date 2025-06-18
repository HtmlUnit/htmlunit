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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Native JavaScript object Map.
 *
 * @author Ronald Brill
 */
public class NativeMapTest extends WebDriverTestCase {

    /**
     * See https://github.com/HtmlUnit/htmlunit-core-js/issues/4.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"key type string", "value type object"})
    public void entries() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var m = new Map();\n"
            + "    m.set('key', null);\n"
            + "    for (var entry of m.entries()) {\r\n"
            + "      log('key type ' + typeof(entry[0]));\n"
            + "      log('value type ' + typeof(entry[1]));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
