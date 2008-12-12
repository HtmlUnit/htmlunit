/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * Tests for {@link HtmlUnitRegExpProxy}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlUnitRegExpProxyTest extends WebTestCase {

    private final String str_ = "(?:<script.*?>)((\\n|\\r|.)*?)(?:<\\/script>)";
    private final String begin_ = "<div>bla</div>";
    private final String end_ = "foo\n<span>bla2</span>";
    private final String text_ = begin_ + "<script>var a = 123;</script>" + end_;
    private final String expected_ = begin_ + end_;
    private final String src_ = "var re = new RegExp(str, 'img');\n"
        + "var s = text.replace(re, '');\n"
        + "if (s != expected)\n"
        + " throw 'Expected >' + expected + '< but got >' + s + '<';";

    private final String scriptTestMatch_ = "function arrayToString(_arr)\n"
        + "{\n"
        + "  if (_arr == null) return null;\n"
        + "  var s = '[';\n"
        + "  for (var i=0; i<_arr.length; ++i)\n"
        + "  {\n"
        + "    if (i != 0) s += ', '\n"
        + "    s += _arr[i];\n"
        + "  }\n"
        + "  s += ']';\n"
        + "  return s;\n"
        + "}\n"
        + "function assertArrEquals(actual, expected)\n"
        + "{\n"
        + "  if (expected == null)\n"
        + "  {\n"
        + "    if (actual != null)\n"
        + "      throw 'Expected >null< got >' + actual + '<';\n"
        + "    else return;\n"
        + "  }\n"
        + "  var expectedStr = arrayToString(expected);\n"
        + "  var actualStr = arrayToString(actual);\n"
        + "  if (expectedStr != actualStr)\n"
        + "    throw 'Expected >' + expectedStr + '< got >' + actualStr + '<';\n"
        + "}\n"
        + "assertArrEquals('ab'.match(), null);\n"
        + "assertArrEquals('ab'.match('foo'), null);\n"
        + "assertArrEquals('ab'.match('a'), ['a']);\n"
        + "assertArrEquals('abab'.match('a'), ['a']);\n"
        + "assertArrEquals('abab'.match('.a'), ['ba']);\n"
        + "assertArrEquals('abab'.match(/.a/), ['ba']);\n"
        + "assertArrEquals('li'.match(/^([^a-z0-9_-])?([a-z0-9_-]+)(.*)/i), ['li', undefined, 'li', '']);\n"
        + "assertArrEquals('abab'.match(new RegExp('.a')), ['ba']);\n"
        + "var s = '<script>var a = 1;</' + 'script>';\n"
        + "var re = '(?:<script.*?>)((\\n|\\r|.)*?)(?:<\\/script>)';\n"
        + "assertArrEquals(s.match(re), [s, 'var a = 1;', ';']);\n";

    /**
     * Test that string.replace works correctly (?) in HtmlUnit.
     * @throws Exception if the test fails
     */
    @Test
    public void fixedInHtmlUnit() throws Exception {
        final String html = "<html></html>";
        final HtmlPage page = loadPage(html);
        final Window topScope = ((Window) page.getEnclosingWindow().getScriptObject());
        topScope.put("str", topScope, str_);
        topScope.put("text", topScope, text_);
        topScope.put("expected", topScope, expected_);
        page.executeJavaScript(src_);
    }

    /**
     * Tests if custom patch is still needed.
     */
    @Test
    public void needCustomFix() {
        final WebClient client = new WebClient();
        final ContextFactory cf = client.getJavaScriptEngine().getContextFactory();
        final Context ctx = cf.enterContext();
        try {
            final ScriptableObject topScope = ctx.initStandardObjects();
            topScope.put("str", topScope, str_);
            topScope.put("text", topScope, text_);
            topScope.put("expected", topScope, expected_);
            assertEquals(begin_ + end_, text_.replaceAll(str_, ""));
            try {
                ctx.evaluateString(topScope, src_, "test script", 0, null);
            }
            catch (final JavaScriptException e) {
                assertTrue(e.getMessage().indexOf("Expected >") == 0);
            }
        }
        finally {
            Context.exit();
        }
    }

    /**
     * Test for bug http://sf.net/tracker/index.php?func=detail&aid=1780089&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void replaceNormalStringWithRegexpChars() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert('123456'.replace('/{\\d+}', ''));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"123456"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void replaceWithUndefinedPattern() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var pattern;\n"
            + "    alert('123456'.replace(pattern, ''));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"123456"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void replace() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var pattern = /{\\d+}/g;\n"
            + "    alert('123456'.replace(pattern, ''));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"123456"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void match() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + scriptTestMatch_
            + "</script></head><body>\n"
            + "</body></html>";

        final String[] expectedAlerts = {};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test if the custom fix is needed or not. When this test fails, then it means that the problem is solved in
     * Rhino and that custom fix for String.match in {@link HtmlUnitRegExpProxy} is not needed anymore (and that
     * this test can be removed, or turned positive).
     * @throws Exception if the test fails
     */
    @Test
    public void matchFixNeeded() throws Exception {
        final WebClient client = new WebClient();
        final ContextFactory cf = client.getJavaScriptEngine().getContextFactory();
        final Context cx = cf.enterContext();
        try {
            final ScriptableObject topScope = cx.initStandardObjects();
            cx.evaluateString(topScope, scriptTestMatch_, "test script String.match", 0, null);
            try {
                cx.evaluateString(topScope, scriptTestMatch_, "test script String.match", 0, null);
            }
            catch (final JavaScriptException e) {
                assertTrue(e.getMessage().indexOf("Expected >") == 0);
            }
        }
        finally {
            Context.exit();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void index() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var match = '#{tests} tests'.match(/(^|.|\\r|\\n)(#\\{(.*?)\\})/);\n"
            + "    alert(match.index);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"0"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void match_NotFirstCharacter() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(\"ab\".match(/^(.)[^\\1]$/))\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"ab,a"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void regExp_exec() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var re = new RegExp('(?:<s' + 'cript.*?>)(.*)<\\/script>');\n"
            + "    var t = 'foo <scr' + 'ipt>boo();</' + 'script>bar';\n"
            + "    var r = re.exec(t);\n"
            + "    alert(r[1]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"boo();"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void flag_global() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var str = 'foo <script>boo();<'+'/script>bar';\n"
            + "    var regExp = new RegExp('<script[^>]*>([\\\\S\\\\s]*?)<\\/script>', 'img');\n"
            + "    alert(str.match(regExp));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"<script>boo();</script>"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Minimal case exists in {@link #test_minimal()}.  Once successful, either one to be removed.
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var regexp = /^(?:(?:(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|"
            + "(?:(?:16|[2468][048]|[3579][26])00)))(\\/|-|\\.)(?:0?2\\1(?:29)))|(?:(?:(?:1[6-9]|[2-9]\\d)?\\d{2})"
            + "(\\/|-|\\.)(?:(?:(?:0?[13578]|1[02])\\2(?:31))|(?:(?:0?[1,3-9]|1[0-2])\\2(29|30))|(?:(?:0?[1-9])|"
            + "(?:1[0-2]))\\2(?:0?[1-9]|1\\d|2[0-8]))))$/;\n"
            + "    var str = '2001-06-16';\n"
            + "    alert(regexp.test(str))\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"true"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Minimized version of {@link #test()}.  Once successful, either one to be removed.
     * @throws Exception if the test fails
     */
    @Test
    public void test_minimal() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var regexp = /((?:2001)-)/;\n"
            + "    var str = '2001-';\n"
            + "    alert(regexp.test(str))\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"true"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that curly braces can be used non escaped in JS regexp.
     */
    @Test
    public void testEscapeCurlyBraces() {
        assertEquals("\\{", HtmlUnitRegExpProxy.escapeJSCurly("{"));
        assertEquals("\\{", HtmlUnitRegExpProxy.escapeJSCurly("\\{"));
        assertEquals("\\}", HtmlUnitRegExpProxy.escapeJSCurly("}"));
        assertEquals("\\}", HtmlUnitRegExpProxy.escapeJSCurly("\\}"));
        assertEquals("(^|\\{)#([^\\}]+)(\\}|$)", HtmlUnitRegExpProxy.escapeJSCurly("(^|{)#([^}]+)(}|$)"));

        assertEquals("a{5}", HtmlUnitRegExpProxy.escapeJSCurly("a{5}"));
        assertEquals("a{5,}", HtmlUnitRegExpProxy.escapeJSCurly("a{5,}"));
        assertEquals("a{5,10}", HtmlUnitRegExpProxy.escapeJSCurly("a{5,10}"));
    }

    /**
     * Tests usage of regex with non escaped curly braces, such as is used by dhtmlGrid.
     * @throws Exception if the test fails
     */
    @Test
    public void testRegexWithNonEscapedCurlyBraces() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var regexp = /(^|{)#([^}]+)(}|$)/;\n"
            + "    var str = '|{#abcd}|';\n"
            + "    alert(str.match(regexp))\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"{#abcd},{,abcd,}"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests usage of regex with non escaped curly braces, such as is used by dhtmlGrid.
     * @throws Exception if the test fails
     */
    @Test
    public void testBackSpace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var regexp = /[p\\bz]/g;\n"
            + "    var str = 'aapbzb' + String.fromCharCode(8);\n"
            + "    alert(str.replace(regexp, '-'))\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"aa-b-b-"};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void dollarSignAndCurlyBracket() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var value = ''.replace(/\\${/g, '');\n"
            + "    alert(value)\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {""};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
