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
    @Alerts({"myValue", "", "", ""})
    public void value() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    for(var i = 1; i < 5; i++) {\n"
            + "      var param = document.getElementById('tester' + i);\n"
            + "      alert(param.value);\n"
            + "    }\n"
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
    @Alerts({"testParam", "", "", ""})
    public void name() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    for(var i = 1; i < 5; i++) {\n"
            + "      var param = document.getElementById('tester' + i);\n"
            + "      alert(param.name);\n"
            + "    }\n"
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ttt", "", "", "", "ttt", "", "", "", "ttt", "ttt", "ttt"})
    public void type() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    for(var i = 1; i < 12; i++) {\n"
            + "      var param = document.getElementById('tester' + i);\n"
            + "      alert(param.type);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <object>\n"
            + "    <param id='tester1' type='ttt' value='myValue'/>\n"
            + "    <param id='tester2' type='' value='myValue'/>\n"
            + "    <param id='tester3' value='myValue'/>\n"
            + "    <param id='tester4' />\n"

            + "    <param id='tester5' type='ttt' value='myValue' valueType='ref'/>\n"
            + "    <param id='tester6' type='' value='myValue' valueType='ref'/>\n"
            + "    <param id='tester7' value='myValue' valueType='ref'/>\n"
            + "    <param id='tester8' valueType='ref'/>\n"

            + "    <param id='tester9' type='ttt' value='myValue' valueType='data'/>\n"
            + "    <param id='tester10' type='ttt' value='myValue' valueType='object'/>\n"
            + "    <param id='tester11' type='ttt' value='myValue' valueType='foo'/>\n"
            + "  </object>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ref", "object", "data", "foo"})
    public void valueType() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    for(var i = 1; i < 5; i++) {\n"
            + "      var param = document.getElementById('tester' + i);\n"
            + "      alert(param.valueType);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <object>\n"
            + "    <param id='tester1' valuetype='ref' value='myValue'/>\n"
            + "    <param id='tester2' valuetype='object' value='myValue'/>\n"
            + "    <param id='tester3' valuetype='data' value='myValue'/>\n"
            + "    <param id='tester4' valuetype='foo' value='myValue'/>\n"
            + "  </object>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
