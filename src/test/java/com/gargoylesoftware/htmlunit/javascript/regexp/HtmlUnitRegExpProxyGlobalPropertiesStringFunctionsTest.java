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
public class HtmlUnitRegExpProxyGlobalPropertiesStringFunctionsTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HtmlUnit, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpMatchNoGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
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
    @Alerts("HtmlUnit,Html, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpMatchOneGroup() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit');\n"
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
    @Alerts("HtmlUnit,Ht,lU, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpMatchManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit');\n"
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
    @Alerts("HtmlUnitxy,H,t,m,l,U,n,i,t,x,y, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpMatchTooManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)');\n"
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
    public void regExpMatchNoGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
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
    @Alerts("HtmlUnit,Html, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpMatchOneGroupIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'i');\n"
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
    @Alerts("HtmlUnit,Ht,lU, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpMatchManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'i');\n"
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
    @Alerts("HtmlUnitxy,H,t,m,l,U,n,i,t,x,y, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpMatchTooManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i');\n"
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
    public void regExpMatchNoGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
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
    @Alerts("HtmlUnit, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpMatchOneGroupGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'g');\n"
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
    @Alerts("Html,Htnl,Htol, $n, Htol, , , , , , , , , -, Htol, Htol, 1234HtmlUnit for Htnl; , xyz")
    public void regExpMatchOneGroupGlobalManyMatches() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnit for Htnl; Htolxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht.l)', 'g');\n"
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
    @Alerts("HtmlUnit, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpMatchManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'g');\n"
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
    @Alerts("HtmlUnitxy, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpMatchTooManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g');\n"
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
    @Alerts("4, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpSearchNoGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
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
    @Alerts("4, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpSearchOneGroup() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit');\n"
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
    @Alerts("4, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpSearchManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit');\n"
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
    @Alerts("4, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpSearchTooManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)');\n"
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
    public void regExpSearchNoGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
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
    @Alerts("4, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpSearchOneGroupIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'i');\n"
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
    @Alerts("4, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpSearchManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'i');\n"
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
    @Alerts("4, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpSearchTooManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i');\n"
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
    public void regExpSearchNoGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
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
    @Alerts("4, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpSearchOneGroupGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'g');\n"
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
    @Alerts("4, $n, Html, , , , , , , , , -, Html, Html, 1234, Unit for Html; Htmlxyz")
    public void regExpSearchOneGroupGlobalManyMatches() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnit for Html; Htmlxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)', 'g');\n"
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
    @Alerts("4, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpSearchManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'g');\n"
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
    @Alerts("4, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpSearchTooManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g');\n"
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
    @Alerts("1234RegularExpressionsxyz, $n, , , , , , , , , , -, HtmlUnit, , 1234, xyz")
    public void regExpReplaceNoGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit');\n"
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
    @Alerts("1234RegularExpressionsxyz, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpReplaceOneGroup() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit');\n"
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
    @Alerts("1234RegularExpressionsxyz, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpReplaceManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit');\n"
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
    @Alerts("1234RegularExpressionsz, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpReplaceTooManyGroups() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)');\n"
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
    public void regExpReplaceNoGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'i');\n"
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
    @Alerts("1234RegularExpressionsxyz, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpReplaceOneGroupIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'i');\n"
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
    @Alerts("1234RegularExpressionsxyz, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpReplaceManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'i');\n"
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
    @Alerts("1234RegularExpressionsz, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpReplaceTooManyGroupsIgnoreCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'i');\n"
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
    public void regExpReplaceNoGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('HtmlUnit', 'g');\n"
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
    @Alerts("1234RegularExpressionsxyz, $n, Html, , , , , , , , , -, HtmlUnit, Html, 1234, xyz")
    public void regExpReplaceOneGroupGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)Unit', 'g');\n"
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
    @Alerts("1234RegularExpressionsUnit for RegularExpressions; RegularExpressionsxyz, "
               + "$n, Html, , , , , , , , , -, Html, Html, 1234HtmlUnit for Html; , xyz")
    public void regExpReplaceOneGroupGlobalManyMatches() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnit for Html; Htmlxyz'\n;"
            + "    var myRegExp=new RegExp('(Html)', 'g');\n"
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
    @Alerts("1234RegularExpressionsxyz, $n, Ht, lU, , , , , , , , -, HtmlUnit, lU, 1234, xyz")
    public void regExpReplaceManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(Ht)m(lU)nit', 'g');\n"
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
    @Alerts("1234RegularExpressionsz, $n, H, t, m, l, U, n, i, t, x, -, HtmlUnitxy, y, 1234, z")
    public void regExpReplaceTooManyGroupsGlobal() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str='1234HtmlUnitxyz'\n;"
            + "    var myRegExp=new RegExp('(H)(t)(m)(l)(U)(n)(i)(t)(x)(y)', 'g');\n"
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
}
