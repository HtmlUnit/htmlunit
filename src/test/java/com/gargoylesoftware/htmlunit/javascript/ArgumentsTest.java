/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for org.mozilla.javascript.Arguments, which is the object for "function.arguments".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ArgumentsTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "1", "0" })
    public void arguments() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  alert(test.arguments.length);\n"
            + "  test1('hi');\n"
            + "}\n"
            + "function test1(hello) {\n"
            + "  alert(test.arguments.length);\n"
            + "  alert(test1.arguments.length);\n"
            + "  alert(arguments.callee.caller.arguments.length);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void passedCountDifferentFromDeclared() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  test1('hi', 'there');\n"
            + "}\n"
            + "function test1() {\n"
            + "  alert(test1.arguments.length);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = { "2", "hi", "undefined", "you" }, IE = { "2", "hi", "undefined", "you" },
            CHROME = { "2", "world", "undefined", "undefined" })
    //FF14 same as chrome
    public void readOnly() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  test1('hello', 'world');\n"
            + "}\n"
            + "function test1() {\n"
            + "  test1.arguments[1] = 'hi';\n"
            + "  test1.arguments[3] = 'you';\n"
            + "  alert(test1.arguments.length);\n"
            + "  alert(test1.arguments[1]);\n"
            + "  alert(test1.arguments[2]);\n"
            + "  alert(test1.arguments[3]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
