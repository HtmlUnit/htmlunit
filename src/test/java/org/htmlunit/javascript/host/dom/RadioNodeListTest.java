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
 * Tests for {@link RadioNodeList}.
 *
 * @author Lai Quang Duong
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class RadioNodeListTest extends WebDriverTestCase {

    private static final String FORM_HTML = "<form name='form'>\n"
            + "<input type='text' name='first' value='0'/>\n"
            + "<input type='radio' name='first'/>\n"
            + "<input type='radio' name='first' value='2' checked/>\n"
            + "<input type='radio' name='first' value='3'/>\n"
            + "\n"
            + "<input type='radio' name='second' value='1'/>\n"
            + "\n"
            + "<input type='radio' name='third' value='1' checked/>\n"
            + "\n"
            + "<input type='radio' name='fourth' value='1'/>\n"
            + "<input type='radio' name='fourth' value='2'/>\n"
            + "\n"
            + "<input type='radio' name='fifth' value='1'/>\n"
            + "<input type='radio' name='fifth' checked/>\n"
            + "</form>";

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void instanceOf() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(document.form.first instanceof RadioNodeList);\n"
                + "    log(document.form.fourth instanceof RadioNodeList);\n"
                + "    log(document.form.fifth instanceof RadioNodeList);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + FORM_HTML
                + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "1", "1", "", "on"})
    public void getValue() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(document.form.first.value);\n"
                + "    log(document.form.second.value);\n"
                + "    log(document.form.third.value);\n"
                + "    log(document.form.fourth.value);\n"
                + "    log(document.form.fifth.value);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + FORM_HTML
                + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "on", "true"})
    public void setValue() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(document.form.first.value);\n"
                + "    document.form.first.value = 'on';\n"
                + "    log(document.form.first[1].value);\n"
                + "    log(document.form.first[1].checked);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + FORM_HTML
                + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"first", "first", "first", "first"})
    public void iterable() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    for (let e of form.first) {\n"
                + "      logEx(e)\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + FORM_HTML
                + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }
}
