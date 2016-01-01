/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link V8BreakIterator}.
 *
 * @author Ahmed Ashour
 *
 * @see https://code.google.com/p/v8/source/browse#svn%2Ftrunk%2Ftest%2Fintl%2Fbreak-iterator
 */
@RunWith(BrowserRunner.class)
public class V8BreakIteratorTest extends WebDriverTestCase {

    private static final String LINE_ = "line";
    private static final String CHARACTER_ = "character";
    private static final String SENTENCE_ = "sentence";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "0, none, 0, none, 0, none, 4, letter, 5, none, 8, letter, 9, none, 13, letter, 14, none, "
            + "15, none, 19, letter, 20, none, 24, letter, 25, none, 29, letter, 30, none, 30, none")
    public void sample() throws Exception {
        test("en", null, "Jack and Jill, went over hill!");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "0, none, 0, none, 0, none, 5, none, 9, none, 15, none, 20, none, 25, none, 30, none, 30, none")
    public void sampleLine() throws Exception {
        test("en", LINE_, "Jack and Jill, went over hill!");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "0, none, 0, none, 0, none, 1, none, 2, none, 3, none, 4, none, 5, none, 6, none, 7, none, "
            + "8, none, 9, none, 10, none, 11, none, 12, none, 13, none, 14, none, 15, none, 16, none, 17, none, "
            + "18, none, 19, none, 20, none, 21, none, 22, none, 23, none, 24, none, 25, none, 26, none, 27, none, "
            + "28, none, 29, none, 30, none, 30, none")
    public void sampleCharacter() throws Exception {
        test("en", CHARACTER_, "Jack and Jill, went over hill!");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "0, none, 0, none, 0, none, 30, none, 30, none")
    public void sampleSentence() throws Exception {
        test("en", SENTENCE_, "Jack and Jill, went over hill!");
    }

    /**
     * @param type can be null
     */
    private void test(final String language, final String type, final String text) throws Exception {
        final String html = ""
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Intl && window.Intl.v8BreakIterator) {\n"
            + "      var iterator = new Intl.v8BreakIterator('" + language + "'"
            + (type == null ? "" : ", {type: '" + type + "'}")
            + ");\n"
            + "      log(iterator);\n"
            + "      var text = '" + text.replace("'", "\\'") + "';\n"
            + "      iterator.adoptText(text);\n"
            + "      log(iterator);\n"

            + "      var pos = iterator.first();\n"
            + "      log(iterator);\n"

            + "      while (pos !== -1) {\n"
            + "        var nextPos = iterator.next();\n"
            + "        log(iterator);\n"
            + "        if (nextPos === -1) {\n"
            + "          break;\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "  function log(iterator) {\n"
            + "    alert(iterator.current());\n"
            + "    alert(iterator.breakType());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "en-US")
    public void defaultLocale() throws Exception {
        final String html = ""
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Intl && window.Intl.v8BreakIterator) {\n"
            + "      var iterator = new Intl.v8BreakIterator([]);\n"
            + "      var options = iterator.resolvedOptions();\n"
            + "      alert(options.locale);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "0, none, 0, none, 0, none, 4, letter, 5, none, 8, letter, 9, none, 13, letter, 14, none, "
            + "15, none, 19, letter, 20, none, 24, letter, 25, none, 29, letter, 30, none, 31, none, 34, letter, "
            + "35, none, 38, letter, 39, none, 43, letter, 44, none, 45, none, 50, letter, 51, none, 51, none")
    public void enBreak() throws Exception {
        test("en", null, "Jack and Jill, went over hill, and got lost. Alert!");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "0, none, 0, none, 0, none, 1, none, 2, none, 3, none, 4, none, 5, none, 6, none, 7, none, "
            + "8, none, 9, none, 10, none, 11, none, 12, none, 13, none, 14, none, 15, none, 15, none")
    public void zh() throws Exception {
        test("zh", null, "\u56FD\u52A1\u9662\u5173\u4E8E\u300A\u571F\u5730\u623F\u5C4B\u7BA1\u7406\u6761\u4F8B\u300B");
    }

}
