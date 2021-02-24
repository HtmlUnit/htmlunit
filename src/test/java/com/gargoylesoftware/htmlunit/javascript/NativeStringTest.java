/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * String is a native JavaScript object and therefore provided by Rhino but some tests are needed here.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NativeStringTest extends WebDriverTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2783950">2783950</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("key\\:b_M")
    public void replace() throws Exception {
        final String html
            = "<html><head><script>\n"
                    + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log('key:b_M'.replace(':', '\\\\:'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"anchor: function", "big: function", "blink: function", "bold: function", "charAt: function",
            "charCodeAt: function", "concat: function", "constructor: function", "equals: undefined",
            "equalsIgnoreCase: undefined", "fixed: function", "fontcolor: function", "fontsize: function",
            "fromCharCode: undefined", "indexOf: function", "italics: function", "lastIndexOf: function",
            "link: function", "localeCompare: function", "match: function", "replace: function", "search: function",
            "slice: function", "small: function", "split: function", "strike: function", "sub: function",
            "substr: function", "substring: function", "sup: function", "toLocaleLowerCase: function",
            "toLocaleUpperCase: function", "toLowerCase: function", "toString: function", "toUpperCase: function",
            "valueOf: function"})
    public void methods_common() throws Exception {
        final String[] methods = {"anchor", "big", "blink", "bold", "charAt", "charCodeAt", "concat", "constructor",
            "equals", "equalsIgnoreCase", "fixed", "fontcolor", "fontsize", "fromCharCode", "indexOf", "italics",
            "lastIndexOf", "link", "localeCompare", "match", "replace", "search", "slice", "small", "split",
            "strike", "sub", "substr", "substring", "sup", "toLocaleLowerCase", "toLocaleUpperCase", "toLowerCase",
            "toString", "toUpperCase", "valueOf"};
        final String html = NativeDateTest.createHTMLTestMethods("'hello'", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with different expectations depending on the browsers.
     * Function contains is introduced in FF18.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"contains: undefined", "toSource: undefined", "trim: function"})
    public void methods_differences() throws Exception {
        final String[] methods = {"contains", "toSource", "trim" };
        final String html = NativeDateTest.createHTMLTestMethods("'hello'", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void trim() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var string = ' hi  ';\n"
            + "  if (''.trim) {\n"
            + "    log(string.trim().length);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "3",
            IE = {})
    public void trimRight() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var string = ' hi  ';\n"
            + "  if (''.trimRight) {\n"
            + "    log(string.trimRight().length);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "4",
            IE = {})
    public void trimLeft() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var string = ' hi  ';\n"
            + "  if (''.trimLeft) {\n"
            + "    log(string.trimLeft().length);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("contains not supported")
    public void contains() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  if ('contains' in String.prototype) {\n"
            + "    var str = 'To be, or not to be, that is the question.';\n"
            + "    log(str.contains('To be'));\n"
            + "    log(str.contains('TO'));\n"
            + "    log(str.contains(''));\n"
            + "    log(str.contains(' '));\n"
            + "    log(str.contains('To be', 0));\n"
            + "    log(str.contains('TO', 0));\n"
            + "    log(str.contains(' ', 0));\n"
            + "    log(str.contains('', 0));\n"
            + "    log(str.contains('or', 7));\n"
            + "    log(str.contains('or', 8));\n"

            + "    log(str.contains('or', -3));\n"
            + "    log(str.contains('or', 7.9));\n"
            + "    log(str.contains('or', 8.1));\n"
            + "    log(str.contains());\n"
            + "  } else {\n"
            + "    log('contains not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "false"},
            IE = "startsWith not supported")
    public void startsWith() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  if ('startsWith' in String.prototype) {\n"
            + "    var str = 'To be, or not to be, that is the question.';\n"
            + "    log(str.startsWith('To be'));\n"
            + "    log(str.startsWith('T'));\n"
            + "    log(str.startsWith(str));\n"

            + "    log(str.startsWith('question.'));\n"
            + "  } else {\n"
            + "    log('startsWith not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "false"},
            IE = "endsWith not supported")
    public void endsWith() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  if ('endsWith' in String.prototype) {\n"
            + "    var str = 'To be, or not to be, that is the question.';\n"
            + "    log(str.endsWith('question.'));\n"
            + "    log(str.endsWith('.'));\n"
            + "    log(str.endsWith(str));\n"

            + "    log(str.endsWith('the'));\n"
            + "  } else {\n"
            + "    log('endsWith not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "true", "false"},
            IE = "includes not supported")
    public void includes() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  if ('includes' in String.prototype) {\n"
            + "    var str = 'To be, or not to be, that is the question.';\n"
            + "    log(str.includes('to be'));\n"
            + "    log(str.includes('question.'));\n"
            + "    log(str.includes('To be'));\n"
            + "    log(str.includes(str));\n"

            + "    log(str.includes('test'));\n"
            + "  } else {\n"
            + "    log('includes not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "abc", "abcabc"},
            IE = "repeat not supported")
    public void repeat() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  if ('repeat' in String.prototype) {\n"
            + "    var str = 'abc';\n"
            + "    log(str.repeat(0));\n"
            + "    log(str.repeat(1));\n"
            + "    log(str.repeat(2));\n"
            + "  } else {\n"
            + "    log('repeat not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
