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
    @Alerts({"0", "-1", "0", "0", "0", "-1"})
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
    @Alerts({"locale,usage,sensitivity,ignorePunctuation,collation,numeric,caseFirst", "de", "sort", "variant", "false",
             "default", "false", "false"})
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
    @Alerts({"function", "en", "en,ja", "", "de-XX"})
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("variant")
    public void optionsUndefined() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', undefined).resolvedOptions().sensitivity"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void optionsNull() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', null).resolvedOptions().sensitivity"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("variant")
    public void optionsNumber() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('en', 42).resolvedOptions().sensitivity"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("variant")
    public void optionsString() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', 'base').resolvedOptions().sensitivity"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("variant")
    public void optionsEmptyObject() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('en', {}).resolvedOptions().sensitivity"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("usage,localeMatcher,collation,numeric,caseFirst,sensitivity,ignorePunctuation")
    public void optionsGetterOrder() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var read = [];\n"
                + "    var options = {};\n"
                + "    ['usage', 'localeMatcher', 'collation', 'numeric', 'caseFirst', 'sensitivity',"
                + " 'ignorePunctuation'].forEach(function(p) {\n"
                + "      Object.defineProperty(options, p, { get: function() { read.push(p); } });\n"
                + "    });\n"
                + "    new Intl.Collator('en', options);\n"
                + "    try { log(read.join()); } catch(e) { logEx(e) }\n"
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
    @Alerts("sort")
    public void usageSort() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { usage: 'sort' }).resolvedOptions().usage"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("search")
    public void usageSearch() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { usage: 'search' }).resolvedOptions().usage"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void usageInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { usage: 'invalid' }).resolvedOptions().usage"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("search")
    public void usageToString() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { usage: { toString: function() { return 'search'; } } })"
                + ".resolvedOptions().usage"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"variant", "default", "false"})
    public void usageSearchDefaults() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var options = new Intl.Collator('en', { usage: 'search' }).resolvedOptions();\n"
                + "    try { log(options.sensitivity); } catch(e) { logEx(e) }\n"
                + "    try { log(options.collation); } catch(e) { logEx(e) }\n"
                + "    try { log(options.ignorePunctuation); } catch(e) { logEx(e) }\n"
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
    @Alerts("en")
    public void localeMatcherLookup() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { localeMatcher: 'lookup' }).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en")
    public void localeMatcherBestFit() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { localeMatcher: 'best fit' }).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void localeMatcherInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { localeMatcher: 'invalid' }).resolvedOptions().locale"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"base", "0", "0", "-1", "0"})
    public void sensitivityBase() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { sensitivity: 'base' });\n"
                + "    try { log(c.resolvedOptions().sensitivity); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', '\\u00e1')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A', '\\u00e1')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"accent", "0", "-1", "-1", "-1"})
    public void sensitivityAccent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { sensitivity: 'accent' });\n"
                + "    try { log(c.resolvedOptions().sensitivity); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', '\\u00e1')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A', '\\u00e1')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"case", "-1", "0", "-1", "1"})
    public void sensitivityCase() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { sensitivity: 'case' });\n"
                + "    try { log(c.resolvedOptions().sensitivity); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', '\\u00e1')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A', '\\u00e1')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"variant", "-1", "-1", "-1", "-1"})
    public void sensitivityVariant() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { sensitivity: 'variant' });\n"
                + "    try { log(c.resolvedOptions().sensitivity); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', '\\u00e1')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A', '\\u00e1')); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void sensitivityInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('en', { sensitivity: 'invalid' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"variant", "-1", "-1", "-1", "-1"})
    public void sensitivityDefault() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en');\n"
                + "    try { log(c.resolvedOptions().sensitivity); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', '\\u00e1')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A', '\\u00e1')); } catch(e) { logEx(e) }\n"
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
    @Alerts("A,\u00E1,a,b,B")
    public void sensitivityBaseSort() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { sensitivity: 'base' });\n"
                + "    try { log(['b', 'A', '\\u00e1', 'a', 'B'].sort(c.compare).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"true", "0", "0", "0", "-1", "-1"})
    public void ignorePunctuation() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { ignorePunctuation: true });\n"
                + "    try { log(c.resolvedOptions().ignorePunctuation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a b', 'ab')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a-b', 'ab')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a.b', 'a b')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'b')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a,b', 'ac')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"false", "-1"})
    // the collation rules of the jdk sort spaces and punctuation differently
    @HtmlUnitNYI(CHROME = {"false", "1"},
            EDGE = {"false", "1"},
            FF = {"false", "1"},
            FF_ESR = {"false", "1"})
    public void ignorePunctuationFalse() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { ignorePunctuation: false });\n"
                + "    try { log(c.resolvedOptions().ignorePunctuation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a b', 'ab')); } catch(e) { logEx(e) }\n"
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
    public void ignorePunctuationTruthy() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { ignorePunctuation: 'no' }).resolvedOptions().ignorePunctuation"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void ignorePunctuationThai() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('th').resolvedOptions().ignorePunctuation"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void ignorePunctuationThaiFalse() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('th', { ignorePunctuation: false }).resolvedOptions().ignorePunctuation"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "en", "-1", "-1", "0", "0", "1", "-1", "-1"})
    public void numeric() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { numeric: true });\n"
                + "    try { log(c.resolvedOptions().numeric); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('2', '10')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a2', 'a10')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a02', 'a2')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a1b', 'a01b')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('10', '9')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A1', 'a2')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('1.5', '1.10')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"false", "1"})
    public void numericFalse() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { numeric: false });\n"
                + "    try { log(c.resolvedOptions().numeric); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('2', '10')); } catch(e) { logEx(e) }\n"
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
    @Alerts("1,2,02,9,10,100")
    public void numericSort() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "['10', '9', '2', '1', '100', '02'].sort(new Intl.Collator('en', { numeric: true }).compare).join()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void numericTruthy() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { numeric: 'no' }).resolvedOptions().numeric"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "en-u-kn", "-1"})
    public void numericExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en-u-kn');\n"
                + "    try { log(c.resolvedOptions().numeric); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('2', '10')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"true", "en-u-kn"})
    public void numericExtensionTrue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en-u-kn-true');\n"
                + "    try { log(c.resolvedOptions().numeric); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"false", "en-u-kn-false", "1"})
    public void numericExtensionFalse() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en-u-kn-false');\n"
                + "    try { log(c.resolvedOptions().numeric); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('2', '10')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"true", "en", "-1"})
    public void numericOptionOverridesExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en-u-kn-false', { numeric: true });\n"
                + "    try { log(c.resolvedOptions().numeric); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('2', '10')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"true", "en-u-kn"})
    public void numericOptionSameAsExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en-u-kn', { numeric: true });\n"
                + "    try { log(c.resolvedOptions().numeric); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"upper", "en", "1", "-1", "-1", "A,a,B,b"})
    public void caseFirstUpper() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { caseFirst: 'upper' });\n"
                + "    try { log(c.resolvedOptions().caseFirst); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'B')); } catch(e) { logEx(e) }\n"
                + "    try { log(['b', 'A', 'B', 'a'].sort(c.compare).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"lower", "en", "-1", "1", "-1", "a,A,b,B"})
    public void caseFirstLower() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { caseFirst: 'lower' });\n"
                + "    try { log(c.resolvedOptions().caseFirst); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'B')); } catch(e) { logEx(e) }\n"
                + "    try { log(['b', 'A', 'B', 'a'].sort(c.compare).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts({"false", "en", "-1", "1", "-1", "a,A,b,B"})
    public void caseFirstFalse() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { caseFirst: 'false' });\n"
                + "    try { log(c.resolvedOptions().caseFirst); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('A', 'a')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'B')); } catch(e) { logEx(e) }\n"
                + "    try { log(['b', 'A', 'B', 'a'].sort(c.compare).join()); } catch(e) { logEx(e) }\n"
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
    @Alerts("RangeError")
    public void caseFirstInvalid() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml("new Intl.Collator('en', { caseFirst: 'invalid' })"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void caseFirstBoolean() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { caseFirst: false }).resolvedOptions().caseFirst"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"upper", "en-u-kf-upper", "1"})
    public void caseFirstExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en-u-kf-upper');\n"
                + "    try { log(c.resolvedOptions().caseFirst); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"lower", "en", "-1"})
    public void caseFirstOptionOverridesExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en-u-kf-upper', { caseFirst: 'lower' });\n"
                + "    try { log(c.resolvedOptions().caseFirst); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
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
    @Alerts("0")
    public void caseFirstSensitivityBase() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { caseFirst: 'upper', sensitivity: 'base' });\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1", "0"})
    public void caseFirstSensitivityCase() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { caseFirst: 'upper', sensitivity: 'case' });\n"
                + "    try { log(c.compare('a', 'A')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('a', '\\u00e1')); } catch(e) { logEx(e) }\n"
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
    @Alerts(DEFAULT = {"phonebk", "de-u-co-phonebk"},
            FF = {"phonebk", "de"},
            FF_ESR = {"phonebk", "de"})
    public void collationPhonebook() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de', { collation: 'phonebk' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"1", "-1", "-1", "-1"})
    public void collationPhonebookCompare() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de', { collation: 'phonebk' });\n"
                + "    var d = new Intl.Collator('de');\n"
                + "    try { log(c.compare('\\u00e4', 'ae')); } catch(e) { logEx(e) }\n"
                + "    try { log(d.compare('\\u00e4', 'ae')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('\\u00e4b', 'af')); } catch(e) { logEx(e) }\n"
                + "    try { log(d.compare('\\u00e4b', 'af')); } catch(e) { logEx(e) }\n"
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
    @Alerts({"default", "en"})
    public void collationUnsupportedForLocale() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en', { collation: 'phonebk' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts("default")
    public void collationStandard() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('de', { collation: 'standard' }).resolvedOptions().collation"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("default")
    public void collationSearch() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('de', { collation: 'search' }).resolvedOptions().collation"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void collationInvalidSyntax() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('de', { collation: 'x' }).resolvedOptions().collation"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("emoji")
    public void collationEmoji() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { collation: 'emoji' }).resolvedOptions().collation"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("eor")
    public void collationEor() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "new Intl.Collator('en', { collation: 'eor' }).resolvedOptions().collation"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"pinyin", "zh-u-co-pinyin"},
            FF = {"pinyin", "zh"},
            FF_ESR = {"pinyin", "zh"})
    public void collationPinyin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('zh', { collation: 'pinyin' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts(DEFAULT = {"trad", "es-u-co-trad"},
            FF = {"trad", "es"},
            FF_ESR = {"trad", "es"})
    public void collationTraditionalSpanish() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('es', { collation: 'trad' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"phonebk", "de-u-co-phonebk"})
    public void collationExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de-u-co-phonebk');\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"default", "en"})
    public void collationExtensionUnsupported() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('en-u-co-phonebk');\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"eor", "de"})
    public void collationOptionOverridesExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de-u-co-phonebk', { collation: 'eor' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"phonebk", "de-u-co-phonebk"})
    public void collationOptionSameAsExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de-u-co-phonebk', { collation: 'phonebk' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"locale,usage,sensitivity,ignorePunctuation,collation,numeric,caseFirst", "de", "search", "base", "true",
             "default", "true", "upper"})
    public void resolvedOptionsAllOptions() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var options = new Intl.Collator('de', { usage: 'search', sensitivity: 'base',"
                + " ignorePunctuation: true, collation: 'phonebk', numeric: true, caseFirst: 'upper' })"
                + ".resolvedOptions();\n"
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
    @Alerts({"de-u-co-phonebk-kf-upper-kn", "phonebk", "true", "upper"})
    public void resolvedOptionsAllExtensions() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var options = new Intl.Collator('de-u-co-phonebk-kf-upper-kn').resolvedOptions();\n"
                + "    try { log(options.locale); } catch(e) { logEx(e) }\n"
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
    @Alerts("Mueller,M\u00FCller,Muf,Mund")
    public void collationPhonebookSort() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "['Muf', 'Mueller', 'M\\u00fcller', 'Mund']"
                + ".sort(new Intl.Collator('de', { collation: 'phonebk' }).compare).join()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Mueller,Muf,M\u00FCller,Mund")
    public void collationDefaultSortGerman() throws Exception {
        loadPageVerifyTitle2(IntlTest.localesArgumentHtml(
                "['Muf', 'Mueller', 'M\\u00fcller', 'Mund'].sort(new Intl.Collator('de').compare).join()"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "-1", "1", "-1"})
    public void collationTraditionalSpanishCompare() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('es', { collation: 'trad' });\n"
                + "    var d = new Intl.Collator('es');\n"
                + "    try { log(c.compare('chico', 'cuna')); } catch(e) { logEx(e) }\n"
                + "    try { log(d.compare('chico', 'cuna')); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('llama', 'luz')); } catch(e) { logEx(e) }\n"
                + "    try { log(d.compare('llama', 'luz')); } catch(e) { logEx(e) }\n"
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
    @Alerts(DEFAULT = {"emoji", "de-u-co-emoji"},
            FF = {"emoji", "de"},
            FF_ESR = {"emoji", "de"})
    public void collationEmojiLocale() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de', { collation: 'emoji' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts(DEFAULT = {"phonebk", "de-u-co-phonebk-kn", "true"},
            FF = {"phonebk", "de-u-kn", "true"},
            FF_ESR = {"phonebk", "de-u-kn", "true"})
    public void collationOptionWithOtherExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de-u-kn', { collation: 'phonebk' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().numeric); } catch(e) { logEx(e) }\n"
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
    @Alerts(DEFAULT = {"default", "de-u-co-phonebk"},
            FF = {"default", "de"},
            FF_ESR = {"default", "de"})
    public void usageSearchCollationExtension() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de-u-co-phonebk', { usage: 'search' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
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
    @Alerts({"default", "de", "1"})
    public void usageSearchCollationOption() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var c = new Intl.Collator('de', { usage: 'search', collation: 'phonebk' });\n"
                + "    try { log(c.resolvedOptions().collation); } catch(e) { logEx(e) }\n"
                + "    try { log(c.resolvedOptions().locale); } catch(e) { logEx(e) }\n"
                + "    try { log(c.compare('\\u00e4', 'ae')); } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
