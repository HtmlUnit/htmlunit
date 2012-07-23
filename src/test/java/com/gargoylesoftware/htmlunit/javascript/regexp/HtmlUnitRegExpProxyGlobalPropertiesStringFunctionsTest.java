/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
public class HtmlUnitRegExpProxyGlobalPropertiesStringFunctionsTest extends WebDriverTestCase {

    private void testMatch(final String string, final String regexp) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str = '" + string + "'\n;"
            + "    var myRegExp = " + regexp + ";\n"
            + "    alert(str.match(myRegExp));\n"
            + "    alert('$n');\n"
            + "    alert(RegExp.$1);\n"
            + "    alert(RegExp.$2);\n"
            + "    alert(RegExp.$3);\n"
            + "    alert(RegExp.$4);\n"
            + "    alert(RegExp.$5);\n"
            + "    alert(RegExp.$6);\n"
            + "    alert(RegExp.$7);\n"
            + "    alert(RegExp.$8);\n"
            + "    alert(RegExp.$9);\n"
            + "    alert('-');\n"
            + "    alert(RegExp.lastMatch);\n"
            + "    alert(RegExp.lastParen);\n"
            + "    alert(RegExp.leftContext);\n"
            + "    alert(RegExp.rightContext);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpMatchNoGroups() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('HtmlUnit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit,Html, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpMatchOneGroup() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(Html)Unit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit,Ht,lU, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpMatchManyGroups() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnitxy,H,t,m,l,U,n,i,t,x,y, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpMatchTooManyGroups() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpMatchNoGroupsIgnoreCase() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit,Html, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpMatchOneGroupIgnoreCase() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit,Ht,lU, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpMatchManyGroupsIgnoreCase() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnitxy,H,t,m,l,U,n,i,t,x,y, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpMatchTooManyGroupsIgnoreCase() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpMatchNoGroupsGlobal() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpMatchOneGroupGlobal() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Html,Htnl,Htol, $n, Htol, , , , , , , , , -, Htol, Htol, 1234HtmlUnit for Htnl; , xyz")
    public void regExpMatchOneGroupGlobalManyMatches() throws Exception {
        testMatch("1234HtmlUnit for Htnl; Htolxyz", "new RegExp('(Ht.l)', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpMatchManyGroupsGlobal() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnitxy, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpMatchTooManyGroupsGlobal() throws Exception {
        testMatch("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g')");
    }

    private void testSearch(final String string, final String regexp) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str = '" + string + "'\n;"
            + "    var myRegExp = " + regexp + ";\n"
            + "    alert(str.search(myRegExp));\n"
            + "    alert('$n');\n"
            + "    alert(RegExp.$1);\n"
            + "    alert(RegExp.$2);\n"
            + "    alert(RegExp.$3);\n"
            + "    alert(RegExp.$4);\n"
            + "    alert(RegExp.$5);\n"
            + "    alert(RegExp.$6);\n"
            + "    alert(RegExp.$7);\n"
            + "    alert(RegExp.$8);\n"
            + "    alert(RegExp.$9);\n"
            + "    alert('-');\n"
            + "    alert(RegExp.lastMatch);\n"
            + "    alert(RegExp.lastParen);\n"
            + "    alert(RegExp.leftContext);\n"
            + "    alert(RegExp.rightContext);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpSearchNoGroups() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('HtmlUnit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpSearchOneGroup() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(Html)Unit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpSearchManyGroups() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpSearchTooManyGroups() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpSearchNoGroupsIgnoreCase() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpSearchOneGroupIgnoreCase() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpSearchManyGroupsIgnoreCase() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpSearchTooManyGroupsIgnoreCase() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpSearchNoGroupsGlobal() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpSearchOneGroupGlobal() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, Html, , , , , , , , , -, Html, Html, 1234, Unit for Html; Htmlxyz")
    public void regExpSearchOneGroupGlobalManyMatches() throws Exception {
        testSearch("1234HtmlUnit for Html; Htmlxyz", "new RegExp('(Html)', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpSearchManyGroupsGlobal() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpSearchTooManyGroupsGlobal() throws Exception {
        testSearch("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g')");
    }

    private void testReplace(final String string, final String regexp) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str = '" + string + "'\n;"
            + "    var myRegExp = " + regexp + ";\n"
            + "    alert(str.replace(myRegExp, 'RegularExpressions'));\n"
            + "    alert('$n');\n"
            + "    alert(RegExp.$1);\n"
            + "    alert(RegExp.$2);\n"
            + "    alert(RegExp.$3);\n"
            + "    alert(RegExp.$4);\n"
            + "    alert(RegExp.$5);\n"
            + "    alert(RegExp.$6);\n"
            + "    alert(RegExp.$7);\n"
            + "    alert(RegExp.$8);\n"
            + "    alert(RegExp.$9);\n"
            + "    alert('-');\n"
            + "    alert(RegExp.lastMatch);\n"
            + "    alert(RegExp.lastParen);\n"
            + "    alert(RegExp.leftContext);\n"
            + "    alert(RegExp.rightContext);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpReplaceNoGroups() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('HtmlUnit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpReplaceOneGroup() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(Html)Unit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpReplaceManyGroups() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsz, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpReplaceTooManyGroups() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpReplaceNoGroupsIgnoreCase() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpReplaceOneGroupIgnoreCase() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpReplaceManyGroupsIgnoreCase() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsz, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpReplaceTooManyGroupsIgnoreCase() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpReplaceNoGroupsGlobal() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('HtmlUnit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpReplaceOneGroupGlobal() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(Html)Unit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsUnit for RegularExpressions; RegularExpressionsxyz, "
               + "$n, Html, , , , , , , , , -, Html, Html, 1234HtmlUnit for Html; , xyz")
    public void regExpReplaceOneGroupGlobalManyMatches() throws Exception {
        testReplace("1234HtmlUnit for Html; Htmlxyz", "new RegExp('(Html)', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsxyz, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpReplaceManyGroupsGlobal() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(Ht)m(lU)nit', 'g')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234RegularExpressionsz, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpReplaceTooManyGroupsGlobal() throws Exception {
        testReplace("1234HtmlUnitxyz", "new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g')");
    }
}
