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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HTMLFormControlsCollection}.
 *
 * @author Lai Quang Duong
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLFormControlsCollectionTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true", "2", "true", "1", "null"})
    public void namedItem() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var elements = document.form.elements;\n"
                + "\n"
                + "    log(elements instanceof HTMLFormControlsCollection);\n"
                + "    log(elements.namedItem('first') instanceof RadioNodeList);\n"
                + "    log(elements.namedItem('first').value);\n"
                + "\n"
                + "    log(elements.namedItem('second') instanceof HTMLInputElement);\n"
                + "    log(elements.namedItem('second').value);\n"
                + "\n"
                + "    log(elements.namedItem('third'));\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<form name='form'>\n"
                + "<input type='text' name='first' value='0'/>\n"
                + "<input type='radio' name='first'/>\n"
                + "<input type='radio' name='first' value='2' checked/>\n"
                + "<input type='radio' name='first' value='3'/>\n"
                + "\n"
                + "<input type='radio' name='second' value='1'/>\n"
                + "</form>"
                + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"first", "first", "first", "first", "second"})
    public void iterable() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    for (let e of document.form.elements) {\n"
                + "      logEx(e)\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<form name='form'>\n"
                + "<input type='text' name='first' value='0'/>\n"
                + "<input type='radio' name='first'/>\n"
                + "<input type='radio' name='first' value='2' checked/>\n"
                + "<input type='radio' name='first' value='3'/>\n"
                + "\n"
                + "<input type='radio' name='second' value='1'/>\n"
                + "</form>"
                + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }
}
