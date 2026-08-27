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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Smoke tests for 'new.target' support.
 *
 * @author Ronald Brill
 */
public class NewTargetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Foo instantiated with new function", "-", "Foo() must be called with new"})
    public void simple() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function Foo() {\n"
            + "    if (!new.target) {\n"
            + "      log('Foo() must be called with new');\n"
            + "      return;\n"
            + "    }\n"
            + "    log('Foo instantiated with new ' + typeof(new.target));\n"
            + "  }\n"
            + "  new Foo();"
            + "  log('-');\n"
            + "  Foo();"
            + "\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
