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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link Set}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "true" },
            IE = { "1", "false" })
    public void has() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Set) {\n"
            + "      var myArray = ['value1', 'value2', 'value3'];\n"
            + "      var mySet = new Set(myArray);\n"
            + "      mySet.add('value1');\n"
            + "      alert(mySet.size);\n"
            + "      alert(mySet.has('value2'));\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function values() { [native code] }",
                "[object Set Iterator]", "0", "1", "[object Object]" },
            FF = { "function values() {\n    [native code]\n}",
                    "[object Set Iterator]", "0", "1", "[object Object]" },
            IE = { })
    public void iterator() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var set = new Set();\n"
            + "      set.add('0');\n"
            + "      set.add(1);\n"
            + "      set.add({});\n"
            + "      alert(set[Symbol.iterator]);\n"
            + "      var iter = set[Symbol.iterator]();\n"
            + "      alert(iter);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function values() { [native code] }",
                "[object Set Iterator]", "0", "1", "[object Object]" },
            FF = { "function values() {\n    [native code]\n}",
                "[object Set Iterator]", "0", "1", "[object Object]" },
            IE = { })
    public void values() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Symbol) {\n"
            + "      var set = new Set();\n"
            + "      set.add('0');\n"
            + "      set.add(1);\n"
            + "      set.add({});\n"
            + "      alert(set.values);\n"
            + "      var iter = set.values();\n"
            + "      alert(iter);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "      alert(iter.next().value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
