/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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

    private void testProperties(final String string, final String regexp) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str = '" + string + "'\n;"
            + "    var myRegExp = " + regexp + ";\n"
            + "    alert(myRegExp.exec(str));\n"
            + "    alert(myRegExp.source);\n"
            + "    alert(myRegExp.ignoreCase);\n"
            + "    alert(myRegExp.global);\n"
            + "    alert(myRegExp.multiline);\n"
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
    @Alerts("HtmlUnit, HtmlUnit, false, false, false, 0")
    public void regExpPropertyNone() throws Exception {
        testProperties("HtmlUnit", "new RegExp('HtmlUnit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, HtmlUnit, true, false, false, 0")
    public void regExpPropertyIgnoreCase() throws Exception {
        testProperties("HtmlUnit", "new RegExp('HtmlUnit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, HtmlUnit, false, true, false, 8")
    public void regExpPropertyGlobal() throws Exception {
        testProperties("HtmlUnit", "new RegExp('HtmlUnit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, HtmlUnit, false, false, true, 0")
    public void regExpPropertyMultiline() throws Exception {
        testProperties("HtmlUnit", "new RegExp('HtmlUnit', 'm')");
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
     * Test for bug 1455.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "html,body,div,div,div", "undefined", "undefined", "undefined",
                "html", "1", "undefined", "/html/body/div[5]/div[1]/div[1]" },
            IE8 = { "html,body,div,div,div", "25", "28", "/html/body/div[5]/div[1]/div[1]",
                    "null" })
    @NotYetImplemented
    public void regExResultProperties() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var source = '/html/body/div[5]/div[1]/div[1]';\n"
            + "    var myRegexp = new RegExp(\n"
            + "          '\\\\$?(?:(?![0-9-])[\\\\w-]+:)?(?![0-9-])[\\\\w-]+' ,'g');\n"

            + "     var result = source.match(myRegexp);\n"
            + "     alert(result);\n"
            + "     if (result) {\n"
            + "       alert(result.index);\n"
            + "       alert(result.lastIndex);\n"
            + "       alert(result.input);\n"
            + "     }\n"

            + "     result = myRegexp.exec(source);\n"
            + "     alert(result);\n"
            + "     if (result) {\n"
            + "       alert(result.index);\n"
            + "       alert(result.lastIndex);\n"
            + "       alert(result.input);\n"
            + "     }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
