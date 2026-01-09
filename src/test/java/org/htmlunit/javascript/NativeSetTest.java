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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for NativeSet.
 *
 * @author Ronald Brill
 */
public class NativeSetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,9")
    public void intersection() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "let odds = new Set([1, 3, 5, 7, 9]);\n"
            + "let squares = new Set([1, 4, 9]);\n"
            + "let result = odds.intersection(squares);\n"
            + "log(Array.from(result).sort().join(','));\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,2,4,6,8,9")
    public void union() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "let evens = new Set([2, 4, 6, 8]);\n"
            + "let squares = new Set([1, 4, 9]);\n"
            + "let result = evens.union(squares);\n"
            + "log(Array.from(result).sort().join(','));\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3,5,7")
    public void difference() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "let odds = new Set([1, 3, 5, 7, 9]);\n"
            + "let squares = new Set([1, 4, 9]);\n"
            + "let result = odds.difference(squares);\n"
            + "log(Array.from(result).sort().join(','));\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1,2,6,8,9")
    public void symmetricDifference() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "let evens = new Set([2, 4, 6, 8]);\n"
            + "let squares = new Set([1, 4, 9]);\n"
            + "let result = evens.symmetricDifference(squares);\n"
            + "log(Array.from(result).sort().join(','));\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void isSubsetOf() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "let fours = new Set([4, 8, 12, 16]);\n"
            + "let evens = new Set([2, 4, 6, 8, 10, 12, 14, 16, 18]);\n"
            + "let result = fours.isSubsetOf(evens);\n"
            + "log(result);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void isSupersetOf() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "let fours = new Set([4, 8, 12, 16]);\n"
            + "let evens = new Set([2, 4, 6, 8, 10, 12, 14, 16, 18]);\n"
            + "let result = evens.isSupersetOf(fours);\n"
            + "log(result);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void isDisjointFrom() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "let primes = new Set([2, 3, 5, 7, 11, 13, 17, 19]);\n"
            + "let squares = new Set([1, 4, 9, 16]);\n"
            + "let result = primes.isDisjointFrom(squares);\n"
            + "log(result);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
