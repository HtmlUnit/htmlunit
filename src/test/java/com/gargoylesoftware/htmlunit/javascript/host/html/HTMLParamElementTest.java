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
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Unit tests for {@link HTMLParamElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLParamElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "myValue", "", "", "" })
    public void value() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var param = document.getElementById('tester1');\n"
            + "    alert(param.value);\n"

            + "    param = document.getElementById('tester2');\n"
            + "    alert(param.value);\n"

            + "    param = document.getElementById('tester3');\n"
            + "    alert(param.value);\n"

            + "    param = document.getElementById('tester4');\n"
            + "    alert(param.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <object>\n"
            + "    <param id='tester1' name='testParam' value='myValue'/>\n"
            + "    <param id='tester2' name='testParam' value=''/>\n"
            + "    <param id='tester3' name='testParam' />\n"
            + "    <param id='tester4' />\n"
            + "  </object>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "testParam", "", "", "" })
    public void name() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var param = document.getElementById('tester1');\n"
            + "    alert(param.name);\n"

            + "    param = document.getElementById('tester2');\n"
            + "    alert(param.name);\n"

            + "    param = document.getElementById('tester3');\n"
            + "    alert(param.name);\n"

            + "    param = document.getElementById('tester4');\n"
            + "    alert(param.name);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <object>\n"
            + "    <param id='tester1' name='testParam' value='myValue'/>\n"
            + "    <param id='tester2' name='' value='myValue'/>\n"
            + "    <param id='tester3' value='myValue'/>\n"
            + "    <param id='tester4' />\n"
            + "  </object>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
