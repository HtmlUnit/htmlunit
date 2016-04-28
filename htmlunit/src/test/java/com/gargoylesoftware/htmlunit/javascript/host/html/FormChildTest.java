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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link FormChild}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class FormChildTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"f1", "f2"})
    public void formSimple() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var elem = document.getElementById('i1');\n"
            + "  alert(elem.form ? elem.form.id : elem.form);\n"
            + "  elem = document.getElementById('i2');\n"
            + "  alert(elem.form ? elem.form.id : elem.form);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form id='f1'>\n"
            + "    <input id='i1' name='myInput' type='text'>\n"
            + "  </form>\n"
            + "  <form id='f2'>\n"
            + "    <input id='i2' name='myInput' type='text'>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void formBeforeAfter() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var elem = document.getElementById('i1');\n"
            + "  alert(elem.form ? elem.form.id : elem.form);\n"
            + "  elem = document.getElementById('i2');\n"
            + "  alert(elem.form ? elem.form.id : elem.form);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='i1' name='myInput' type='text'>\n"
            + "  <form id='f1'>\n"
            + "  </form>\n"
            + "  <input id='i2' name='myInput' type='text'>\n"
            + "  <form id='f2'>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
