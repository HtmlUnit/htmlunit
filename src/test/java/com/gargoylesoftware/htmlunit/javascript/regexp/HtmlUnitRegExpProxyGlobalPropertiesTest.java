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
package com.gargoylesoftware.htmlunit.javascript.regexp;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

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
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlUnitRegExpProxyGlobalPropertiesTest extends WebDriverTestCase {

    private void testExec(final String string, final String regexp) throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var str = '" + string + "';\n"
            + "    var myRegExp = " + regexp + ";\n"
            + "    log(myRegExp.exec(str));\n"
            + "    log('$n');\n"
            + "    log(RegExp.$1);\n"
            + "    log(RegExp.$2);\n"
            + "    log(RegExp.$3);\n"
            + "    log(RegExp.$4);\n"
            + "    log(RegExp.$5);\n"
            + "    log(RegExp.$6);\n"
            + "    log(RegExp.$7);\n"
            + "    log(RegExp.$8);\n"
            + "    log(RegExp.$9);\n"
            + "    log('-');\n"
            + "    log(RegExp.lastMatch);\n"
            + "    log(RegExp.lastParen);\n"
            + "    log(RegExp.leftContext);\n"
            + "    log(RegExp.rightContext);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit", "$n", "", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "", "1234", "xyz"})
    public void regExpExecNoGroups() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('HtmlUnit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit,Html", "$n", "Html", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "Html", "1234", "xyz"})
    public void regExpExecOneGroup() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(Html)Unit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit,Ht,lU", "$n", "Ht", "lU", "", "", "", "", "", "", "", "-", "HtmlUnit", "lU", "1234", "xyz"})
    public void regExpExecManyGroups() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"HtmlUnitxy,H,t,m,l,U,n,i,t,x,y", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-",
                "HtmlUnitxy", "y", "1234", "z"},
            IE = {"HtmlUnitxy,H,t,m,l,U,n,i,t,x,y", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-",
                "HtmlUnitxy", "", "1234", "z"})
    @NotYetImplemented(IE)
    public void regExpExecTooManyGroups() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit", "$n", "", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "", "1234", "xyz"})
    public void regExpExecNoGroupsIgnoreCase() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit,Html", "$n", "Html", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "Html", "1234", "xyz"})
    public void regExpExecOneGroupIgnoreCase() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit,Ht,lU", "$n", "Ht", "lU", "", "", "", "", "", "", "", "-", "HtmlUnit", "lU", "1234", "xyz"})
    public void regExpExecManyGroupsIgnoreCase() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"HtmlUnitxy,H,t,m,l,U,n,i,t,x,y", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-",
                 "HtmlUnitxy", "y", "1234", "z"},
            IE = {"HtmlUnitxy,H,t,m,l,U,n,i,t,x,y", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-",
                "HtmlUnitxy", "", "1234", "z"})
    @NotYetImplemented(IE)
    public void regExpExecTooManyGroupsIgnoreCase() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit", "$n", "", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "", "1234", "xyz"})
    public void regExpExecNoGroupsGlobal() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit,Html", "$n", "Html", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "Html", "1234", "xyz"})
    public void regExpExecOneGroupGlobal() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Html,Html", "$n", "Html", "", "", "", "", "", "", "", "", "-", "Html", "Html", "1234",
            "Unit for Htnl; Htolxyz"})
    public void regExpExecOneGroupGlobalManyMatches() throws Exception {
        testExec("1234HtmlUnit for Htnl; Htolxyz", "new RegExp('(Ht.l)', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit,Ht,lU", "$n", "Ht", "lU", "", "", "", "", "", "", "", "-", "HtmlUnit", "lU", "1234", "xyz"})
    public void regExpExecManyGroupsGlobal() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"HtmlUnitxy,H,t,m,l,U,n,i,t,x,y", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-",
                "HtmlUnitxy", "y", "1234", "z"},
            IE = {"HtmlUnitxy,H,t,m,l,U,n,i,t,x,y", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-",
                "HtmlUnitxy", "", "1234", "z"})
    @NotYetImplemented(IE)
    public void regExpExecTooManyGroupsGlobal() throws Exception {
        testExec("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g')");
    }

    private void testTest(final String string, final String regexp) throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var str = '" + string + "';\n"
            + "    var myRegExp = " + regexp + ";\n"
            + "    log(myRegExp.test(str));\n"
            + "    log('$n');\n"
            + "    log(RegExp.$1);\n"
            + "    log(RegExp.$2);\n"
            + "    log(RegExp.$3);\n"
            + "    log(RegExp.$4);\n"
            + "    log(RegExp.$5);\n"
            + "    log(RegExp.$6);\n"
            + "    log(RegExp.$7);\n"
            + "    log(RegExp.$8);\n"
            + "    log(RegExp.$9);\n"
            + "    log('-');\n"
            + "    log(RegExp.lastMatch);\n"
            + "    log(RegExp.lastParen);\n"
            + "    log(RegExp.leftContext);\n"
            + "    log(RegExp.rightContext);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "", "1234", "xyz"})
    public void regExpTestNoGroups() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('HtmlUnit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "Html", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "Html", "1234", "xyz"})
    public void regExpTestOneGroup() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(Html)Unit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "Ht", "lU", "", "", "", "", "", "", "", "-", "HtmlUnit", "lU", "1234", "xyz"})
    public void regExpTestManyGroups() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-", "HtmlUnitxy", "y", "1234", "z"},
            IE = {"true", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-", "HtmlUnitxy", "", "1234", "z"})
    @NotYetImplemented(IE)
    public void regExpTestTooManyGroups() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "", "1234", "xyz"})
    public void regExpTestNoGroupsIgnoreCase() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "Html", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "Html", "1234", "xyz"})
    public void regExpTestOneGroupIgnoreCase() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "Ht", "lU", "", "", "", "", "", "", "", "-", "HtmlUnit", "lU", "1234", "xyz"})
    public void regExpTestManyGroupsIgnoreCase() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-", "HtmlUnitxy", "y", "1234", "z"},
            IE = {"true", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-", "HtmlUnitxy", "", "1234", "z"})
    @NotYetImplemented(IE)
    public void regExpTestTooManyGroupsIgnoreCase() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "", "1234", "xyz"})
    public void regExpTestNoGroupsGlobal() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "Html", "", "", "", "", "", "", "", "", "-", "HtmlUnit", "Html", "1234", "xyz"})
    public void regExpTestOneGroupGlobal() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "Html", "", "", "", "", "", "", "", "", "-", "Html", "Html", "1234",
            "Unit for Html; Htmlxyz"})
    public void regExpTestOneGroupGlobalManyMatches() throws Exception {
        testTest("1234HtmlUnit for Html; Htmlxyz", "new RegExp('(Html)', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "$n", "Ht", "lU", "", "", "", "", "", "", "", "-", "HtmlUnit", "lU", "1234", "xyz"})
    public void regExpTestManyGroupsGlobal() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-", "HtmlUnitxy", "y", "1234", "z"},
            IE = {"true", "$n", "H", "t", "m", "l", "U", "n", "i", "t", "x", "-", "HtmlUnitxy", "", "1234", "z"})
    @NotYetImplemented(IE)
    public void regExpTestTooManyGroupsGlobal() throws Exception {
        testTest("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g')");
    }
}
