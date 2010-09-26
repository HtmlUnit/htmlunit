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
public class HtmlUnitRegExpProxyGlobalPropertiesTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpExecNoGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnit,Html, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpExecOneGroup() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnit,Ht,lU, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpExecManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnitxy,H,t,m,l,U,n,i,t,x,y, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpExecTooManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    public void regExpExecNoGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnit,Html, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpExecOneGroupIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'i');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnit,Ht,lU, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpExecManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'i');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnitxy,H,t,m,l,U,n,i,t,x,y, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpExecTooManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    public void regExpExecNoGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnit,Html, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpExecOneGroupGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'g');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("Html,Html, $n, Html, , , , , , , , , -, Html, Html, 1234, Unit for Htnl; Htolxyz")
    public void regExpExecOneGroupGlobalManyMatches() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnit for Htnl; Htolxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht.l)', 'g');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnit,Ht,lU, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpExecManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'g');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("HtmlUnitxy,H,t,m,l,U,n,i,t,x,y, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpExecTooManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g');\n"
            + "    alert(myRegExp.exec(str));\n"
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
    @Alerts("true, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpTestNoGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpTestOneGroup() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpTestManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpTestTooManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpTestNoGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpTestOneGroupIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'i');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpTestManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'i');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpTestTooManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpTestNoGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpTestOneGroupGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'g');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, Html, , , , , , , , , -, Html, Html, 1234, Unit for Html; Htmlxyz")
    public void regExpTestOneGroupGlobalManyMatches() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnit for Html; Htmlxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)', 'g');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpTestManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'g');\n"
            + "    alert(myRegExp.test(str));\n"
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
    @Alerts("true, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpTestTooManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g');\n"
            + "    alert(myRegExp.test(str));\n"
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
}
