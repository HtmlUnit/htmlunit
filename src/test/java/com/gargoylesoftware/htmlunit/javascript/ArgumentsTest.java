/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link org.mozilla.javascript.Arguments}, which is the object for "function.arguments".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class ArgumentsTest extends WebDriverTestCase {

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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "null", "null" })
    public void argumentsShouldBeNullOutsideFunction() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "function test() {\n"
            + "}\n"
            + "alert(test.arguments);\n"
            + "test();\n"
            + "alert(test.arguments);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "2", "world", "undefined", "undefined" },
            IE = { "2", "hi", "undefined", "you" })
    public void readOnlyWhenAccessedThroughFunction() throws Exception {
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "hi", "undefined", "you" })
    public void writableWithinFunction() throws Exception {
        final String html = "<html><body><script>\n"
            + "function test1() {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  alert(arguments.length);\n"
            + "  alert(arguments[1]);\n"
            + "  alert(arguments[2]);\n"
            + "  alert(arguments[3]);\n"
            + "}\n"
            + "test1('hello', 'world');\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false", IE = "true")
    public void argumentsEqualsFnArguments() throws Exception {
        final String html = "<html><body><script>\n"
            + "function test1() {\n"
            + "  alert(arguments == test1.arguments);\n"
            + "}\n"
            + "test1('hello', 'world');\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }
}
