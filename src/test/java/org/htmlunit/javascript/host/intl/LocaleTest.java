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
 * Tests for {@link Locale}.
 *
 * @author Lai Quang Duong
 * @author Ronald Brill
 */
public class LocaleTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"en", "undefined", "undefined", "en", "en",
             "zh", "undefined", "Hant", "zh-Hant", "zh-Hant",
             "zh", "TW", "Hant", "zh-Hant-TW", "zh-Hant-TW",
             "ja", "JP", "undefined", "ja-JP", "ja-JP",
             "en", "US", "undefined", "en-US", "en-US",
             "ja-JP"})
    public void construction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    function check(l) { log(l.language); log(l.region); log(l.script); log(l.baseName); log(l.toString()); }\n"
            + "    check(new Intl.Locale('en'));\n"
            + "    check(new Intl.Locale('zh-Hant'));\n"
            + "    check(new Intl.Locale('zh-Hant-TW'));\n"
            + "    check(new Intl.Locale('ja-JP'));\n"
            + "    check(new Intl.Locale('EN-US'));\n"
            + "    log(new Intl.Locale(new Intl.Locale('ja-JP')).toString());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "RangeError", "RangeError", "RangeError",
             "invalid", "en-US", "RangeError", "RangeError"})
    public void constructorErrors() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function tryLocale(tag) { try { log(new Intl.Locale(tag).toString()); } catch(e) { logEx(e) } }\n"
            + "  function test() {\n"
            + "    try { new Intl.Locale(); } catch(e) { logEx(e) }\n"
            + "    tryLocale('123');\n"
            + "    tryLocale('');\n"
            + "    tryLocale('a');\n"
            + "    tryLocale('invalid');\n"
            + "    tryLocale(['en-US']);\n"
            + "    tryLocale([]);\n"
            + "    tryLocale(['en-US', 'fr']);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined", "undefined", "undefined", "false",
             "buddhist", "emoji", "latn", "h23", "lower", "true",
             "ja", "JP", "Jpan", "japanese", "h12", "ja-Jpan-JP",
             "de", "phonebk", "de-u-co-phonebk",
             "th", "TH", "thai", "th-TH-u-nu-thai",
             "en", "upper", "en-u-kf-upper"})
    public void extensions() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    function check(l) { log(l.calendar); log(l.collation); log(l.numberingSystem); log(l.hourCycle); log(l.caseFirst); log(l.numeric); }\n"
            + "    check(new Intl.Locale('en-US'));\n"
            + "    check(new Intl.Locale('en-US-u-ca-buddhist-co-emoji-nu-latn-hc-h23-kf-lower-kn-true'));\n"
            + "    var l = new Intl.Locale('ja-Jpan-JP-u-ca-japanese-hc-h12');\n"
            + "    log(l.language); log(l.region); log(l.script);\n"
            + "    log(l.calendar); log(l.hourCycle); log(l.baseName);\n"
            + "    var co = new Intl.Locale('de-u-co-phonebk');\n"
            + "    log(co.language); log(co.collation); log(co.toString());\n"
            + "    var nu = new Intl.Locale('th-TH-u-nu-thai');\n"
            + "    log(nu.language); log(nu.region); log(nu.numberingSystem); log(nu.toString());\n"
            + "    var kf = new Intl.Locale('en-u-kf-upper');\n"
            + "    log(kf.language); log(kf.caseFirst); log(kf.toString());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"en", "true", "en-u-kn",
             "false", "en-u-kn-false",
             "false", "en-u-kn-false"})
    @HtmlUnitNYI(CHROME = {"en", "true", "en-u-kn-true",
                           "false", "en-u-kn-false",
                           "false", "en-u-kn-false"},
            EDGE = {"en", "true", "en-u-kn-true",
                    "false", "en-u-kn-false",
                    "false", "en-u-kn-false"},
            FF = {"en", "true", "en-u-kn-true",
                  "false", "en-u-kn-false",
                  "false", "en-u-kn-false"},
            FF_ESR = {"en", "true", "en-u-kn-true",
                      "false", "en-u-kn-false",
                      "false", "en-u-kn-false"})
    public void numeric() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var l = new Intl.Locale('en-u-kn-true');\n"
            + "    log(l.language); log(l.numeric); log(l.toString());\n"
            + "    var l1 = new Intl.Locale('en-u-kn-false');\n"
            + "    log(l1.numeric); log(l1.toString());\n"
            + "    var l2 = new Intl.Locale('en', {numeric: false});\n"
            + "    log(l2.numeric); log(l2.toString());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"en", "RU", "Cyrl", "buddhist", "zhuyin", "thai", "h11", "true",
             "en", "CA", "en-CA",
             "en", "GB", "en-GB",
             "islamic", "en-US-u-ca-islamic",
             "buddhist", "en-US-u-ca-buddhist"})
    public void optionsOverride() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var l = new Intl.Locale('fr-Latn-CA', {\n"
            + "      language: 'en', script: 'Cyrl', region: 'RU',\n"
            + "      calendar: 'buddhist', collation: 'zhuyin',\n"
            + "      numberingSystem: 'thai', hourCycle: 'h11',\n"
            + "      numeric: true\n"
            + "    });\n"
            + "    log(l.language); log(l.region); log(l.script);\n"
            + "    log(l.calendar); log(l.collation); log(l.numberingSystem);\n"
            + "    log(l.hourCycle); log(l.numeric);\n"
            + "    var l1 = new Intl.Locale('fr-CA', {language: 'en'});\n"
            + "    log(l1.language); log(l1.region); log(l1.toString());\n"
            + "    var l2 = new Intl.Locale('en-US', {region: 'GB'});\n"
            + "    log(l2.language); log(l2.region); log(l2.toString());\n"
            + "    var l3 = new Intl.Locale('en-US', {calendar: 'islamic'});\n"
            + "    log(l3.calendar); log(l3.toString());\n"
            + "    var l4 = new Intl.Locale('en-US-u-ca-gregory', {calendar: 'buddhist'});\n"
            + "    log(l4.calendar); log(l4.toString());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"RangeError", "RangeError", "RangeError", "RangeError", "RangeError"})
    public void invalidOptions() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try { new Intl.Locale('en', {language: '123'}); } catch(e) { logEx(e) }\n"
            + "    try { new Intl.Locale('en', {region: 'TOOLONG'}); } catch(e) { logEx(e) }\n"
            + "    try { new Intl.Locale('en', {script: 'X'}); } catch(e) { logEx(e) }\n"
            + "    try { new Intl.Locale('en', {hourCycle: 'invalid'}); } catch(e) { logEx(e) }\n"
            + "    try { new Intl.Locale('en', {caseFirst: 'invalid'}); } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ja-Jpan-JP-u-ca-japanese-hc-h12",
             "xja-Jpan-JP-u-ca-japanese-hc-h12",
             "ja-u-ca-japanese-hc-h12",
             "ja-Jpan-JP-u-ca-japanese-hc-h12",
             "ja-Jpan-JP"})
    @HtmlUnitNYI(
            CHROME = {"ja-Jpan-JP-u-ca-japanese-hc-h12",
                      "xja-Jpan-JP-u-ca-japanese-hc-h12",
                      "ja-u-ca-japanese-hc-h12",
                      "ja-JP-u-ca-japanese-hc-h12",
                      "ja-Jpan-JP"},
            EDGE = {"ja-Jpan-JP-u-ca-japanese-hc-h12",
                    "xja-Jpan-JP-u-ca-japanese-hc-h12",
                    "ja-u-ca-japanese-hc-h12",
                    "ja-JP-u-ca-japanese-hc-h12",
                    "ja-Jpan-JP"},
            FF = {"ja-Jpan-JP-u-ca-japanese-hc-h12",
                  "xja-Jpan-JP-u-ca-japanese-hc-h12",
                  "ja-u-ca-japanese-hc-h12",
                  "ja-JP-u-ca-japanese-hc-h12",
                  "ja-Jpan-JP"},
            FF_ESR = {"ja-Jpan-JP-u-ca-japanese-hc-h12",
                      "xja-Jpan-JP-u-ca-japanese-hc-h12",
                      "ja-u-ca-japanese-hc-h12",
                      "ja-JP-u-ca-japanese-hc-h12",
                      "ja-Jpan-JP"})
    public void minimizeMaximize() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var l = new Intl.Locale('ja-Jpan-JP-u-ca-japanese-hc-h12');\n"
            + "    log(l.toString());\n"
            + "    log('x' + l);\n"
            + "    log(l.minimize().toString());\n"
            + "    log(new Intl.Locale('ja-u-ca-japanese-hc-h12').maximize().toString());\n"
            + "    log(l.baseName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
