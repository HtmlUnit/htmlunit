/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.regexp;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlUnitRegExpProxy}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author Carsten Steurl
 * @author Leszek Hoppe
 * @author Atsushi Nakagawa
 */
@RunWith(BrowserRunner.class)
public class HtmlUnitRegExpProxyTest extends WebDriverTestCase {

    private static final String scriptTestMatch_ = "function arrayToString(_arr) {\n"
        + "  if (_arr == null) return null;\n"
        + "  var s = '[';\n"
        + "  for (var i = 0; i < _arr.length; i++)\n"
        + "  {\n"
        + "    if (i != 0) s += ', ';\n"
        + "    s += _arr[i];\n"
        + "  }\n"
        + "  s += ']';\n"
        + "  return s;\n"
        + "}\n"
        + "function assertArrEquals(actual, expected) {\n"
        + "  if (expected == null) {\n"
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
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab21")
    public void replaceNormalStringReplace() throws Exception {
        testEvaluate("'121'.replace('1', 'ab')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz121")
    public void replaceNormalStringEmptySearch() throws Exception {
        testEvaluate("'121'.replace('', 'xyz')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void replaceNormalStringEmptyReplace() throws Exception {
        testEvaluate("'121'.replace('21', '')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void replaceNormalStringEmptyStringEmptySearch() throws Exception {
        testEvaluate("''.replace('', 'xyz')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a$c21")
    public void replaceNormalStringReplaceSingleDollar() throws Exception {
        testEvaluate("'121'.replace('1', 'a$c')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a$c21")
    public void replaceNormalStringReplaceDoubleDollar() throws Exception {
        testEvaluate("'121'.replace('1', 'a$$c')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a121")
    public void replaceNormalStringReplaceSubstring() throws Exception {
        testEvaluate("'121'.replace('1', 'a$&')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abaabe")
    public void replaceNormalStringReplacePreceeding() throws Exception {
        testEvaluate("'abcde'.replace('cd', 'a$`')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a21")
    public void replaceNormalStringReplacePreceedingEmpty() throws Exception {
        testEvaluate("'121'.replace('1', 'a$`')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abaee")
    public void replaceNormalStringReplaceFollowing() throws Exception {
        testEvaluate("'abcde'.replace('cd', \"a$'\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aba")
    public void replaceNormalStringReplaceFollowingEmpty() throws Exception {
        testEvaluate("'abcd'.replace('cd', \"a$'\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aba$0")
    public void replaceNormalStringReplaceGroupZero() throws Exception {
        testEvaluate("'abcd'.replace('cd', 'a$0')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aba$1")
    public void replaceNormalStringReplaceGroupOne() throws Exception {
        testEvaluate("'abcd'.replace('cd', 'a$1')");
    }

    /**
     * Test for bug http://sourceforge.net/p/htmlunit/bugs/513/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123456")
    public void replaceNormalStringWithRegexpChars() throws Exception {
        testEvaluate("'123456'.replace('/{\\d+}', '')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123456")
    public void replaceWithUndefinedPattern() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var pattern;\n"
            + "    log('123456'.replace(pattern, ''));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123456")
    public void replace() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var pattern = /{\\d+}/g;\n"
            + "    log('123456'.replace(pattern, ''));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void match() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + scriptTestMatch_
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void index() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var match = '#{tests} tests'.match(/(^|.|\\r|\\n)(#\\{(.*?)\\})/);\n"
            + "    log(match.index);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab,a")
    public void match_NotFirstCharacter() throws Exception {
        testEvaluate("\"ab\".match(/^(.)[^\\1]$/)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("boo();")
    public void regExp_exec() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var re = new RegExp('(?:<s' + 'cript.*?>)(.*)<\\/script>');\n"
            + "    var t = 'foo <scr' + 'ipt>boo();</' + 'script>bar';\n"
            + "    var r = re.exec(t);\n"
            + "    log(r[1]);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<script>boo();</script>")
    public void flag_global() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var str = 'foo <script>boo();<'+'/script>bar';\n"
            + "    var regExp = new RegExp('<script[^>]*>([\\\\S\\\\s]*?)<\\/script>', 'img');\n"
            + "    log(str.match(regExp));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Minimal case exists in {@link #test_minimal()}.  Once successful, either one to be removed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "true"})
    public void prototype() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var regexp = /^(?:(?:(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|"
            + "(?:(?:16|[2468][048]|[3579][26])00)))(\\/|-|\\.)(?:0?2\\1(?:29)))|(?:(?:(?:1[6-9]|[2-9]\\d)?\\d{2})"
            + "(\\/|-|\\.)(?:(?:(?:0?[13578]|1[02])\\2(?:31))|(?:(?:0?[1,3-9]|1[0-2])\\2(29|30))|(?:(?:0?[1-9])|"
            + "(?:1[0-2]))\\2(?:0?[1-9]|1\\d|2[0-8]))))$/;\n"
            + "    var str = '2001-06-16';\n"
            + "    log(regexp.test(str));\n"
            + "    log(regexp.test('hello'));\n"
            + "    log(regexp.exec(str) != null);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        test("/foo/", "hello foo", true, true, false);
        test("/foo/gi", "hello foo", true, false, false);
        test("/foo/gi", "hello Foo", true, false, false);
    }

    private void test(final String regexp, final String testString, final boolean... expectedResults) throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var regexp = " + regexp + ";\n"
            + "    var str = '" + testString + "';\n"
            + "    log(regexp.test(str));\n"
            + "    log(regexp.exec(str) != null);\n"
            + "    log(regexp.test('blabla'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        setExpectedAlerts(String.valueOf(expectedResults[0]), String.valueOf(expectedResults[1]),
            String.valueOf(expectedResults[2]));

        loadPageVerifyTitle2(html);
    }

    /**
     * Minimized version of {@link #test()}.  Once successful, either one to be removed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test_minimal() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var regexp = /((?:2001)-)/;\n"
            + "    var str = '2001-';\n"
            + "    log(regexp.test(str));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests usage of regex with non escaped curly braces, such as is used by dhtmlGrid.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("{#abcd},{,abcd,}")
    public void regexWithNonEscapedCurlyBraces() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var regexp = /(^|{)#([^}]+)(}|$)/;\n"
            + "    var str = '|{#abcd}|';\n"
            + "    log(str.match(regexp));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests usage of regex with non escaped curly braces, such as is used by dhtmlGrid.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aa-b-b-")
    public void backSpace() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var regexp = /[p\\bz]/g;\n"
            + "    var str = 'aapbzb' + String.fromCharCode(8);\n"
            + "    log(str.replace(regexp, '-'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void dollarSignAndCurlyBracket() throws Exception {
        testEvaluate("''.replace(/\\${/g, '')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "["})
    public void openingSquareBracketInCharacterClass() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var re = /[[]/;\n"
            + "  log('div'.match(re));\n"
            + "  log('['.match(re));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * RegExp used in JQuery 1.3.1 that wasn't correctly transformed to Java RegExp in HtmlUnit-2.4.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("div")
    public void jquerySizzleChunker() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + " var re = /((?:\\((?:\\([^()]+\\)|[^()]+)+\\)|\\[(?:\\[[^[\\]]*\\]|['\"][^'\"]+['\"]|[^[\\]'\"]+)+\\]"
            + "|\\\\.|[^ >+~,(\\[]+)+|[>+~])(\\s*,\\s*)?/g\n"
            + "  function test() {\n"
            + "    log('div'.match(re));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * RegExp used in JQuery 1.3.1 that wasn't correctly transformed to Java RegExp in HtmlUnit-2.4.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({":toto,toto,,", "null"})
    public void jqueryPseudo() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + " var re = /:((?:[\\w\\u00c0-\\uFFFF_-]|\\\\.)+)(?:\\((['\"]*)((?:\\([^\\)]+\\)"
            + "|[^\\2\\(\\)]*)+)\\2\\))?/;\n"
            + "  function test() {\n"
            + "    log(':toto'.match(re));\n"
            + "    log('foo'.match(re));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Sample from ExtJS.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "[floating=true],floating,=,,true",
                "[floating=\"true\"],floating,=,\",true",
                "[floating=\"true'],floating,=,,\"true'",
                "[floating=\"true],floating,=,,\"true",
                "[floating=true\"],floating,=,,true\""})
    public void extJs() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var re = /^(?:\\[((?:[@?$])?[\\w\\-]*)\\s*(?:([\\^$*~%!\\/]?=)\\s*(['\\\"])?((?:\\\\\\]|.)*?)\\3)?(?!\\\\)\\])/;\n"
            + "  function test() {\n"
            + "    log('[floating=true]'.match(re));\n"
            + "    log('[floating=\"true\"]'.match(re));\n"
            + "    log('[floating=\"true\\']'.match(re));\n"
            + "    log('[floating=\"true]'.match(re));\n"
            + "    log('[floating=true\"]'.match(re));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Javascript has a different idea about back references to optional groups.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"axxxxa,a", "xxxx,", "xxxx,", "xxxx,"})
    public void backReferenceToOptionalGroup() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var re = /(a)?x+\\1/;\n"
            + "  log('axxxxa'.match(re));\n"
            + "  log('axxxx'.match(re));\n"
            + "  log('xxxxa'.match(re));\n"
            + "  log('xxxx'.match(re));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Browsers ignore back references pointing to not finished groups.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abcx\u0004,b,x", "null", "null", "null"})
    public void backReferenceToNotDefinedGroupsAreHandledAsOctal() throws Exception {
        final String html = "<html><head>\n<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + "  var re = /(?:a)(b)c(x)\\4/;\n"
            + "  log('abcx\\04'.match(re));\n"
            + "  log('abcx'.match(re));\n"
            + "  log('abb'.match(re));\n"
            + "  log('abbxx'.match(re));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * Browsers ignore back references pointing to not finished groups.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abcx,b,x", "abcx,b,x", "null", "null"})
    public void ignoreBackReferenceNotFinishedGroups() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var re = /(?:a)(b)c(x\\2)/;\n"
            + "  log('abcx'.match(re));\n"
            + "  log('abcx\\02'.match(re));\n"
            + "  log('abb'.match(re));\n"
            + "  log('abbxx'.match(re));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Browsers ignore back references in character classes.
     * This is a simplified case of {@link #jqueryPseudo()}
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "abb,a,b", "abd,a,b"})
    public void ignoreBackReferenceInCharacterClass() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var re = /(a)(b)[^\\2c]/;\n"
            + "  log('abc'.match(re));\n"
            + "  log('abb'.match(re));\n"
            + "  log('abd'.match(re));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests basic usage of back references in a <tt>string.replace(...)</tt> invocation.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "afood$0$7b",
            IE = "afoodfoo$7b")
    public void replace_backReferences() throws Exception {
        testEvaluate("'afoob'.replace(/(foo)/g, '$1d$0$7')");
    }

    /**
     * Use back reference in replacement when it exists.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("hllo")
    public void replace_backReference_existing() throws Exception {
        testEvaluate("'hello'.replace(/(h)e/g, '$1')");
    }

    /**
     * If no back reference is present, use $ as litteral.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("$1llo")
    public void replace_backReference_notExisting() throws Exception {
        testEvaluate("'hello'.replace(/he/g, '$1')");
    }

    /**
     * Regression test for bug 2638813 (dollar signs with no index are not back references).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("I$want$these$periods$to$be$$s")
    public void replace_backReferences_2() throws Exception {
        testEvaluate("'I.want.these.periods.to.be.$s'.replace(/\\./g, '$')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("food bar")
    public void replace_backReference_ampersand() throws Exception {
        testEvaluate("'foo bar'.replace(/foo/g, '$&d')");
        testEvaluate("'foo bar'.replace(/foo/, '$&d')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("foo foo ")
    public void replace_backReference_backtick()  throws Exception {
        testEvaluate("'foo bar'.replace(/bar/g, '$`')");
        testEvaluate("'foo bar'.replace(/bar/, '$`')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(" bar bar")
    public void replace_backReference_tick() throws Exception {
        testEvaluate("'foo bar'.replace(/foo/g, '$\\'')");
        testEvaluate("'foo bar'.replace(/foo/, '$\\'')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("$' bar")
    public void replace_$backReference_tick() throws Exception {
        testEvaluate("'foo bar'.replace(/foo/g, '$$\\'')");
        testEvaluate("'foo bar'.replace(/foo/, '$$\\'')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ad$0db",
            IE = "adfoodb")
    public void replace_backReference_$0() throws Exception {
        testEvaluate("'afoob'.replace(/(foo)/g, 'd$0d')");
        testEvaluate("'afoob'.replace(/(foo)/, 'd$0d')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ad$0db",
            IE = "adfkxxxkodb")
    public void replace_backReference_$0WithMultipleGroups() throws Exception {
        testEvaluate("'afkxxxkob'.replace(/(f)k(.*)k(o)/g, 'd$0d')");
        testEvaluate("'afkxxxkob'.replace(/(f)k(.*)k(o)/, 'd$0d')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ad$0db",
            IE = "adfoodb")
    public void replace_backReference_$0WithNoGroups() throws Exception {
        testEvaluate("'afoob'.replace(/foo/g, 'd$0d')");
        testEvaluate("'afoob'.replace(/foo/, 'd$0d')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ad$0dbfuoc",
            IE = "adfoodbfuoc")
    public void replace_backReference_$0WithMultipleHits() throws Exception {
        testEvaluate("'afoobfuoc'.replace(/(f.o)/, 'd$0d')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "ad$0dbd$0dc",
            IE = "adfoodbdfuodc")
    public void replace_backReference_$0WithMultipleHitsGlobal() throws Exception {
        testEvaluate("'afoobfuoc'.replace(/(f.o)/g, 'd$0d')");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("kid\\'s toys")
    public void escapeQuote() throws Exception {
        testEvaluate("\"kid's toys\".replace(/'/g, \"\\\\'\")");
    }

    private static String buildHtml(final String script) {
        return "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + script
            + "\n}</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("INPUT")
    public void test2() throws Exception {
        final String html = buildHtml("var description = 'INPUT#BasisRenameInput';\n"
                + "if(description.match(/^\\s*([a-z0-9\\_\\-]+)/i)) {\n"
                + "  log(RegExp.$1);\n"
                + "}");
        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("a")
    public void stackOverflow() throws Exception {
        final String s = IOUtils.toString(getClass().getResourceAsStream("stackOverflow.txt"),
                ISO_8859_1);
        final String html = buildHtml(
                  "var s = '" + s + "';\n"
                + "s = s.replace(/(\\s*\\S+)*/, 'a');\n"
                + "log(s);\n");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Regression test for bug 2890953.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("$$x$")
    public void replaceDollarDollar() throws Exception {
        testEvaluate("'x'.replace(/(x)/g, '$$$$x$$')");
    }

    /**
     * Original test resides in
     * <a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/test/com/google/gwt/emultest/java/lang/StringTest.java">StringTest</a>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\\*\\[", "\\\\", "+1", "abcdef", "1\\1abc123\\123de1234\\1234f", "\n  \n", "x  x", "x\"\\", "$$x$"})
    public void replaceAll() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      var regex, replacement, x1, x2, x3, x4, x5;\n"
            + "      regex = $replaceAll('*[', "
            + "'([/\\\\\\\\\\\\.\\\\*\\\\+\\\\?\\\\|\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}])', '\\\\\\\\$1');\n"
            + "      log(regex);\n"
            + "      replacement = "
            + "$replaceAll($replaceAll('\\\\', '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '\\\\$', '\\\\\\\\$');\n"
            + "      log(replacement);\n"
            + "      log($replaceAll('*[1', regex, '+'));\n"
            + "      x1 = 'xxxabcxxdexf';\n"
            + "      log($replaceAll(x1, 'x*', ''));\n"
            + "      x2 = '1abc123de1234f';\n"
            + "      log($replaceAll(x2, '([1234]+)', '$1\\\\\\\\$1'));\n"
            + "      x3 = 'x  x';\n"
            + "      log($replaceAll(x3, 'x', '\\n'));\n"
            + "      x4 = 'x  \\n';\n"
            + "      log($replaceAll(x4, '\\\\\\n', 'x'));\n"
            + "      x5 = 'x';\n"
            + "      log($replaceAll(x5, 'x', '\\\\x\\\\\"\\\\\\\\'));\n"
            + "      log($replaceAll(x5, '(x)', '\\\\$\\\\$$1\\\\$'));\n"
            + "    }\n"
            + "    function $replaceAll(this$static, regex, replace){\n"
            + "      replace = __translateReplaceString(replace);\n"
            + "      return this$static.replace(RegExp(regex, 'g'), replace);\n"
            + "    }\n"
            + "    function __translateReplaceString(replaceStr){\n"
            + "      var pos = 0;\n"
            + "      while (0 <= (pos = replaceStr.indexOf('\\\\', pos))) {\n"
            + "        if (replaceStr.charCodeAt(pos + 1) == 36) {\n"
            + "          replaceStr = replaceStr.substr(0, pos - 0) + '$' + $substring(replaceStr, ++pos);\n"
            + "        }\n"
            + "        else {\n"
            + "          replaceStr = replaceStr.substr(0, pos - 0) + $substring(replaceStr, ++pos);\n"
            + "        }\n"
            + "      }\n"
            + "      return replaceStr;\n"
            + "    }\n"
            + "    function $substring(this$static, beginIndex){\n"
            + "      return this$static.substr(beginIndex, this$static.length - beginIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * Regression test for bug 2890953.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("\\$0")
    public void replaceBackslashDollar() throws Exception {
        testEvaluate("'$0'.replace(/\\$/g, '\\\\$')");
    }

    private void testEvaluate(final String expression) throws Exception {
        final String script = "log(" + expression + ");";
        final String html = buildHtml(script);

        loadPageVerifyTextArea2(html);
    }

    /**
     * Regression test for bug 2879412.
     * In RegularExpression, backslash followed by a letter that has no special
     * signification are ignored.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void backslash() throws Exception {
        final String html = buildHtml("var regexp = /(\\https:\\/\\/)/;\n"
                + "var url = 'http://localhost/test.html';\n"
                + "log(url.match(regexp));");
        loadPageVerifyTextArea2(html);
    }

    /**
     * Regression test for bug 2906293.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("\\\\$11")
    public void dollar() throws Exception {
        testEvaluate("'\\\\$1'.replace(/\\$/g, '\\\\$1')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foobar", "$0bar", "$1bar", "\\$1bar", "\\1", "cb", "cb", "a$$b", "a$1b", "a$`b", "a$'b"})
    public void replaceString() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log($replace('bazbar', 'baz', 'foo'));\n"
            + "      log($replace('foobar', 'foo', '$0'));\n"
            + "      log($replace('foobar', 'foo', '$1'));\n"
            + "      log($replace('foobar', 'foo', '\\\\$1'));\n"
            + "      log($replace('*[)1', '*[)', '\\\\'));\n"
            + "      log($replace('$ab', '$a', 'c'));\n"
            + "      log($replace('^ab', '^a', 'c'));\n"
            + "      log($replace('a[x]b', '[x]', '$$'));\n"
            + "      log($replace('a[x]b', '[x]', '$1'));\n"
            + "      log($replace('a[x]b', '[x]', '$`'));\n"
            + "      log($replace('a[x]b', '[x]', \"$'\"));\n"
            + "    }\n"
            + "    function $replace(this$static, from, to){\n"
            + "      var regex, replacement;\n"
            + "      regex = $replaceAll(from, "
            + "'([/\\\\\\\\\\\\.\\\\*\\\\+\\\\?\\\\|\\\\(\\\\)\\\\[\\\\]\\\\{\\\\}$^])', '\\\\\\\\$1');\n"
            + "      replacement = $replaceAll("
            + "$replaceAll(to, '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '\\\\$', '\\\\\\\\$');\n"
            + "      return $replaceAll(this$static, regex, replacement);\n"
            + "    }\n"
            + "    function $replaceAll(this$static, regex, replace){\n"
            + "      replace = __translateReplaceString(replace);\n"
            + "      return this$static.replace(RegExp(regex, 'g'), replace);\n"
            + "    }\n"
            + "    function __translateReplaceString(replaceStr){\n"
            + "      var pos = 0;\n"
            + "      while (0 <= (pos = replaceStr.indexOf('\\\\', pos))) {\n"
            + "        if (replaceStr.charCodeAt(pos + 1) == 36) {\n"
            + "          replaceStr = replaceStr.substr(0, pos - 0) + '$' + $substring(replaceStr, ++pos);\n"
            + "        }\n"
            + "        else {\n"
            + "          replaceStr = replaceStr.substr(0, pos - 0) + $substring(replaceStr, ++pos);\n"
            + "        }\n"
            + "      }\n"
            + "      return replaceStr;\n"
            + "    }\n"
            + "    function $substring(this$static, beginIndex){\n"
            + "      return this$static.substr(beginIndex, this$static.length - beginIndex);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2949446.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[k]")
    public void replace_group2digits() throws Exception {
        testEvaluate("'abcdefghijkl'.replace(/(a)(b)(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)/g, '[\\$11]')");
    }

    /**
     * When replacement reference is two digits but not so much groups exist, one digit replacement index is taken.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[a8]")
    public void replace_group2digits_doesntExist() throws Exception {
        testEvaluate("'abcdefghijkl'.replace(/(a)(b)(c)(d)(e)(f)(g)(h)(i)(j)(k)(l)/g, '[\\$18]')");
    }

    /**
     * When replacement reference is two digits but not so much groups exist, one digit replacement index is taken.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(":")
    public void replaceUnicode() throws Exception {
        testEvaluate("'\\u003a'.replace(/[\\u00c0-\\u00c1]/, 'cat')");
    }

    /**
     * Regression test for bug 3005485.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("part1:part2")
    public void replace_laterFilledGroup() throws Exception {
        testEvaluate("'#part1\\:part2'.replace(/^#|\\\\(:)/g, '$1')");
    }

    /**
     * Test for bug 2969230.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("\\[ab\\]\\(xy\\)\\{z\\} \\{\\[\\(o\\}\\]\\)")
    public void replace_EscapeBrackets() throws Exception {
        testEvaluate("'[ab](xy){z} {[(o}])'.replace(/([.*+?^${}()|[\\]\\/\\\\])/g, \"\\\\$1\")");
    }

    /**
     * Test for bug 3000784.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("><")
    public void replace_T() throws Exception {
        testEvaluate("'>' + 'a\\\\.b'.replace(/([\\u00C0-\\uFFFF]|[\\w]|\\\\.)+/, '') + '<'");
    }

    /**
     * Test from Prototype suite running very fast with Java 6 but taking ages with Java 7 &amp; 8.
     * @throws Exception if an error occurs
     */
    @Test(timeout = 1000)
    @Alerts({"2200915", "2000915"})
    public void replace_huge() throws Exception {
        final String html = "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "String.prototype.times = function(n) {\n"
            + "  var s = '';\n"
            + "  for (var i =0; i < n; i++) {\n"
            + "    s += this;\n"
            + "  }\n"
            + "  return s;\n"
            + "}\n"
            + "var size = 100;\n"
            + "var longString = '\"' + '123456789\\\\\"'.times(size * 10) + '\"';\n"
            + "var object = '{' + longString + ': ' + longString + '},';\n"
            + "var huge = '[' + object.times(size) + '{\"test\": 123}]';\n"
            + "log(huge.length);\n"
            + "log(huge.replace(/\\\\./g, '@').length);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true", "true"})
    public void nullCharacter() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var regex = new RegExp('[\\0-\\x08]');\n"
            + "    log(regex.test('\\0'));\n"
            + "    log('\\0'.match(regex) != null);\n"
            + "    regex = new RegExp('[\\\\0-\\x08]');\n"
            + "    log(regex.test('\\0'));\n"
            + "    log('\\0'.match(regex) != null);\n"
            + "    regex = new RegExp('[\\\\\\0-\\x08]');\n"
            + "    log(regex.test('\\0'));\n"
            + "    log('\\0'.match(regex) != null);\n"
            + "    log('\\0'.match(/^\\0/) != null);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void specialBrackets1() throws Exception {
        // [] matches no character in JS
        testEvaluate("'abc'.replace(/[]/, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void specialBrackets2() throws Exception {
        // [] matches no character in JS
        testEvaluate("'abc'.match(/[]*/)[0]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xaxbxcx")
    public void specialBrackets3() throws Exception {
        // [] matches no character in JS
        testEvaluate("'abc'.replace(/[]*/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xaxbxxcx*xdx")
    public void specialBrackets4() throws Exception {
        // [] matches no character in JS
        testEvaluate("'ab]c*d'.replace(/[]*]*/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xxx")
    public void specialBrackets5() throws Exception {
        // [^] matches any character in JS
        testEvaluate("'abc'.replace(/[^]/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab\nc")
    public void specialBrackets6() throws Exception {
        // [^] matches any character in JS
        testEvaluate("'ab\\nc'.match(/[^]*/)[0]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("axcd")
    public void specialBrackets7() throws Exception {
        // [^] matches any character in JS
        testEvaluate("'ab]cd'.replace(/[^]]/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("axbxc d efg 1 23")
    public void regExMinusInRangeBorderCase1() throws Exception {
        testEvaluate("'a-b_c d efg 1 23'.replace(/[_-]+/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("axbxcxdxefgx1x23")
    public void regExMinusInRangeBorderCase2() throws Exception {
        testEvaluate("'a-b_c d efg 1 23'.replace(/[_-\\s]+/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x x x x x")
    public void regExMinusInRangeBorderCase3() throws Exception {
        testEvaluate("'a-b_c d efg 1 23'.replace(/[_-\\S]+/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x x x x x")
    public void regExMinusInRangeBorderCase4() throws Exception {
        testEvaluate("'a-b_c d efg 1 23'.replace(/[_-\\w]+/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("axbxcxdxefgx1x23")
    public void regExMinusInRangeBorderCase5() throws Exception {
        testEvaluate("'a-b_c d efg 1 23'.replace(/[_-\\W]+/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("axbxc d efg x x")
    public void regExMinusInRangeBorderCase6() throws Exception {
        testEvaluate("'a-b_c d efg 1 23'.replace(/[_-\\d]+/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x1x23")
    public void regExMinusInRangeBorderCase7() throws Exception {
        testEvaluate("'a-b_c d efg 1 23'.replace(/[_-\\D]+/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x-bxc d efg 1 23")
    public void regExMinusInRangeBorderCase8() throws Exception {
        testEvaluate("'a-b_c d efg 1 23'.replace(/[_-\\a]+/g, 'x')");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("https://www.youtube.com/watch?v=1234567,https,1234567")
    public void realWorld1() throws Exception {
        testEvaluate("'https://www.youtube.com/watch?v=1234567'.match(/^(?:(https?):\\/\\/)?(?:(?:www|m)\\.)?youtube\\.com\\/watch.*v=([a-zA-Z0-9_-]+)/)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("http://m.youtu.be/abcd,http,abcd")
    public void realWorld2() throws Exception {
        testEvaluate("'http://m.youtu.be/abcd'.match(/^(?:(https?):\\/\\/)?(?:(?:www|m)\\.)?youtu\\.be\\/([a-zA-Z0-9_-]+)/)");
    }
}
