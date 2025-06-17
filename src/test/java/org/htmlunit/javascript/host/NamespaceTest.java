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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.htmlunit.javascript.host.Namespace}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class NamespaceTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no namespaces")
    public void test() throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:me='http://mysite'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (document.namespaces) {\n"
            + "      for (var i = 0; i < document.namespaces.length; i++) {\n"
            + "        log(document.namespaces[i].name + ', ' + document.namespaces[i].urn);\n"
            + "      }\n"
            + "    } else {\n"
            + "      log('no namespaces');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<app:dIv xmlns='http://anotherURL'></app:dIv>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
