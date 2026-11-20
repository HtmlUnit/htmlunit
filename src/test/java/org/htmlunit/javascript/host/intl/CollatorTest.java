/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.intl;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Collator}.
 *
 * @author Ronald Brill
 */
public class CollatorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void collator() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var dateFormat = Intl.Collator();\n"
                + "    log(dateFormat instanceof Intl.Collator);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator() instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator(undefined) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator(null) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesEmptyString() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('') instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('EN-us') instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('zh-hant-tw') instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('en-US!@#') instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesPrivateUse() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('x-private') instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesInvalidVariant() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('no-NO-NY') instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator(42) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesBoolean() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator(true) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesSymbol() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator(Symbol('de')) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesEmptyObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator({}) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesArrayLike() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator({ length: 2, 0: 'de', 1: 'ja' }) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesStringObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator(new String('de')) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesArrayDuplicates() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator(['de', 'DE', 'ja']) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator(['de', 42]) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator(['de', null]) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorLocalesArrayUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator(['de', undefined]) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void ctorLocalesArrayInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator(['de', 'en-US!@#']) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesArrayLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator([new Intl.Locale('ja')]) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator(new Intl.Locale('ja')) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ctorLocalesEmptyArray() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator([]) instanceof Intl.Collator"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "2", "''", "true", "false", "false", "true"})
    public void compareFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en');\n"
                + "    try { log(typeof c.compare); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare.length); } catch(e) { logEx(e) }\n"
                + "    try { log('\\'' + c.compare.name + '\\''); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare === c.compare); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare === new Intl.Collator('en').compare); } catch(e) { logEx(e) }\n"
                + "    try { log('prototype' in c.compare); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare instanceof Function); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void compareOnPrototype() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.prototype.compare"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void resolvedOptionsOnPrototype() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.prototype.resolvedOptions()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "1", "0", "-1", "0", "1"})
    public void compareBasic() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('en').compare;\n"
                + "    try { log(compare('a', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('b', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('a', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('', '')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('abc', 'ab')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "1", "-1", "-1"})
    public void compareCase() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('en').compare;\n"
                + "    try { log(compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('A', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('A', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('a', 'B')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "-1", "-1", "1"})
    public void compareAccent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('en').compare;\n"
                + "    try { log(compare('a', '\\u00e1')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('\\u00e1', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('e', '\\u00e9')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('\\u00e9', 'E')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void compareNormalization() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('en').compare;\n"
                + "    try { log(compare('\\u00e9', 'e\\u0301')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('\\u00c5', 'A\\u030a')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "-1", "1", "-1", "-1"})
    // the collation rules of the jdk sort spaces and punctuation differently
    @HtmlUnitNYI(CHROME = {"1", "1", "-1", "-1", "-1"},
            EDGE = {"1", "1", "-1", "-1", "-1"},
            FF = {"1", "1", "-1", "-1", "-1"},
            FF_ESR = {"1", "1", "-1", "-1", "-1"})
    public void comparePunctuation() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('en').compare;\n"
                + "    try { log(compare('a b', 'ab')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('a-b', 'ab')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('a.b', 'a b')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('_', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('1', 'a')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "-1", "0"})
    public void compareNumbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('en').compare;\n"
                + "    try { log(compare(2, 10)); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('2', '10')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare(10, 2)); } catch(e) { logEx(e) }\n"
                + "    try { log(compare(1, '1')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "-1", "0", "0", "0",
                      "-1"})
    public void compareArguments() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('en').compare;\n"
                + "    try { log(compare()); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('undefined')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare(undefined, 'undefined')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare(null, 'null')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('a', 'b', 'c')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void compareSymbol() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('en').compare(Symbol('a'), 'a')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "1", "0", "a,b"})
    public void compareUnbound() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('en').compare;\n"
                + "    try { log(compare('a', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare.call(null, 'b', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare.call({}, 'a', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(['b', 'a'].sort(compare).join()); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "-1", "-1", "1"})
    public void compareGerman() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('de').compare;\n"
                + "    try { log(compare('\\u00e4', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('\\u00e4', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('\\u00e4', 'z')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('\\u00df', 'ss')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1"})
    public void compareSwedish() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var compare = new Intl.Collator('sv').compare;\n"
                + "    try { log(compare('\\u00e4', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('\\u00e4', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(compare('\\u00e4', 'z')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a,A,\u00E4,b,z,Z")
    public void sortGerman() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "['z', '\\u00e4', 'a', 'Z', 'b', 'A'].sort(new Intl.Collator('de').compare).join()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a,A,b,z,Z,\u00E4")
    public void sortSwedish() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "['z', '\\u00e4', 'a', 'Z', 'b', 'A'].sort(new Intl.Collator('sv').compare).join()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a,A,\u00E4,b,z,Z")
    public void sortDefault() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "['z', '\\u00e4', 'a', 'Z', 'b', 'A'].sort(new Intl.Collator().compare).join()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"locale,usage,sensitivity,ignorePunctuation,collation,numeric,caseFirst",
                      "de",
                      "sort",
                      "variant",
                      "false",
                      "default",
                      "false",
                      "false"})
    public void resolvedOptions() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var options = new Intl.Collator('de').resolvedOptions();\n"
                + "    try { log(Object.keys(options).join()); } catch(e) { logEx(e) }\n"
                + "    try { log(options.locale); } catch(e) { logEx(e) }\n"
                + "    try { log(options.usage); } catch(e) { logEx(e) }\n"
                + "    try { log(options.sensitivity); } catch(e) { logEx(e) }\n"
                + "    try { log(options.ignorePunctuation); } catch(e) { logEx(e) }\n"
                + "    try { log(options.collation); } catch(e) { logEx(e) }\n"
                + "    try { log(options.numeric); } catch(e) { logEx(e) }\n"
                + "    try { log(options.caseFirst); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void resolvedOptionsSameObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('de').resolvedOptions() === new Intl.Collator('de').resolvedOptions()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator().resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('EN-us').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    // the jdk has collation data for de-DE
    @HtmlUnitNYI(CHROME = "de-DE",
            EDGE = "de-DE",
            FF = "de-DE",
            FF_ESR = "de-DE")
    public void resolvedOptionsLocaleRegion() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('de-DE').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("zh-Hant-TW")
    public void resolvedOptionsLocaleScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('zh-hant-tw').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void resolvedOptionsLocaleUnsupported() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('ban').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void resolvedOptionsLocaleUnsupportedFirst() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator(['ban', 'de']).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de")
    public void resolvedOptionsLocaleFallback() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('de-XX').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de-u-co-phonebk")
    public void resolvedOptionsLocaleCollation() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('de-u-co-phonebk').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-u-kn")
    public void resolvedOptionsLocaleNumeric() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en-u-kn-true').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en")
    public void resolvedOptionsLocaleCalendar() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en-u-ca-buddhist').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("he")
    public void resolvedOptionsLocaleDeprecated() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('iw').resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function",
                      "en",
                      "en,ja",
                      "",
                      "de-XX"})
    public void supportedLocalesOf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try { log(typeof Intl.Collator.supportedLocalesOf); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.Collator.supportedLocalesOf('en')); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.Collator.supportedLocalesOf(['en', 'xx-YY', 'ja'])); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.Collator.supportedLocalesOf([])); } catch(e) { logEx(e) }\n"
                + "    try { log(Intl.Collator.supportedLocalesOf('de-XX')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfNoArgument() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(undefined)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(null)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfEmptyString() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf('')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void supportedLocalesOfMixedCase() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf('EN-us')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("zh-Hant-TW")
    public void supportedLocalesOfScript() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf('zh-hant-tw')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf('en-US!@#')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfPrivateUse() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf('x-private')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfInvalidVariant() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf('no-NO-NY')"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(42)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfBoolean() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(true)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfSymbol() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(Symbol('de'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfEmptyObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf({})"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de,ja")
    public void supportedLocalesOfArrayLike() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "Intl.Collator.supportedLocalesOf({ length: 2, 0: 'de', 1: 'ja' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfStringObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(new String('de'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("de,ja")
    public void supportedLocalesOfArrayDuplicates() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(['de', 'DE', 'ja'])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(['de', 42])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(['de', null])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void supportedLocalesOfArrayUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(['de', undefined])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void supportedLocalesOfArrayInvalidTag() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(['de', 'en-US!@#'])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void supportedLocalesOfArrayLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf([new Intl.Locale('ja')])"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ja")
    public void supportedLocalesOfLocale() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf(new Intl.Locale('ja'))"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void supportedLocalesOfEmptyArray() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("Intl.Collator.supportedLocalesOf([])"));
    }
}
