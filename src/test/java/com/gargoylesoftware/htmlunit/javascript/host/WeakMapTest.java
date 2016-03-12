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

/**
 * Tests for {@link WeakMap}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class WeakMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "value2" },
            IE = { "undefined", "undefined" })
    public void get() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    if (window.WeakMap) {\n"
            + "      var kvArray = [[{}, 'value1'], [window, 'value2']];\n"
            + "      var myMap = new WeakMap(kvArray);\n"
            + "      alert(myMap.size);\n"
            + "      alert(myMap.get(window));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void setNonObject() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    if (window.WeakMap) {"
            + "      var kvArray = [[{}, 'value1'], [window, 'value2']];\n"
            + "      var myMap = new WeakMap(kvArray);\n"
            + "      try {\n"
            + "        myMap.set(1, 2);\n"
            + "      } catch(e) {alert('exception')}\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
