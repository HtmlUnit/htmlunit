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
}
