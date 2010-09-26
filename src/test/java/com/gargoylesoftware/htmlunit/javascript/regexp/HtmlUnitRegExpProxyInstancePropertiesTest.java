/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.regexp;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlUnitRegExpProxy}.
 * Test the various properties.
 *
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlUnitRegExpProxyInstancePropertiesTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit")
    public void regExpPropertySource() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
            + "    alert(myRegExp.source);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit")
    public void regExpPropertySourceIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
            + "    alert(myRegExp.source);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit")
    public void regExpPropertySourceGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.source);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit")
    public void regExpPropertySourceMultiline() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'm');\n"
            + "    alert(myRegExp.source);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
            + "    alert(myRegExp.ignoreCase);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void regExpPropertyIgnoreCaseIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
            + "    alert(myRegExp.ignoreCase);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyIgnoreCaseGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.ignoreCase);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyIgnoreCaseMultiline() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'm');\n"
            + "    alert(myRegExp.ignoreCase);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
            + "    alert(myRegExp.global);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyGlobalIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
            + "    alert(myRegExp.global);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void regExpPropertyGlobalGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.global);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyGlobalMultiline() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'm');\n"
            + "    alert(myRegExp.global);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyMultiline() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
            + "    alert(myRegExp.multiline);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyMultilineIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
            + "    alert(myRegExp.multiline);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void regExpPropertyMultilineGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.multiline);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void regExpPropertyMultilineMultiline() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'm');\n"
            + "    alert(myRegExp.multiline);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void regExpPropertyLastIndex() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit is great'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0, true, 0")
    public void regExpPropertyLastIndexTest() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit is great'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void regExpPropertyLastIndexIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0, true, 0")
    public void regExpPropertyLastIndexIgnoreCaseTest() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit is great'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void regExpPropertyLastIndexGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0, true, 8")
    public void regExpPropertyLastIndexGlobalTest() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit is great'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0, true, 8, true, 27, true, 8, true, 27, false, 0")
    public void regExpPropertyLastIndexGlobalSetTest() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit is great (HtmlUnit)'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            // search again
            + "    myRegExp.lastIndex=0;\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            // start later
            + "    myRegExp.lastIndex=1;\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "    myRegExp.lastIndex=50;\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0, HtmlUnit, 8")
    public void regExpPropertyLastIndexGlobalExec() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit is great'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "    alert(myRegExp.exec(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void regExpPropertyLastIndexMultiline() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'm');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0, true, 0")
    public void regExpPropertyLastIndexMultilineTest() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='HtmlUnit is great'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'm');\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "    alert(myRegExp.test(str));\n"
            + "    alert(myRegExp.lastIndex);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
