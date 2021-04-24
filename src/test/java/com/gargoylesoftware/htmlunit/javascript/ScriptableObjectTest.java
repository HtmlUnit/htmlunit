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
 * Tests for general scriptable objects in the browser context.
 *
 * @author Jake Cobb
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ScriptableObjectTest extends WebDriverTestCase {

    /**
     * Tests that writing a property which is a read-only in the prototype
     * behaves as expected.
     *
     * @see https://sourceforge.net/p/htmlunit/bugs/1633/
     * @throws Exception on failure
     */
    @Test
    @Alerts({"default", "default", "default"})
    public void readOnlyPrototype() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "  var proto = Object.create(Object.prototype, {\n"
            + "    myProp: {\n"
            + "        get: function() { return 'default'; }\n"
            + "    }\n"
            + "  });\n"
            + "  var o1 = Object.create(proto);\n"
            + "  var o2 = Object.create(proto);\n"
            + "  o2.myProp = 'bar';\n"
            + "  alert(o2.myProp);\n"
            + "  alert(o1.myProp);\n"
            + "  alert(proto.myProp)"
            + "</script>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts(DEFAULT = {"2", "symbol", "symbol", "1", "c"},
            IE = "not defined")
    public void getOwnPropertySymbols() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + "  if (Object.getOwnPropertySymbols) {\n"

                + "    var obj = {};\n"
                + "    var a = Symbol('a');\n"
                + "    var b = Symbol.for('b');\n"

                + "    obj[a] = 'localSymbol';\n"
                + "    obj[b] = 'globalSymbol';\n"
                + "    obj['c'] = 'something else';\n"

                + "    var objectSymbols = Object.getOwnPropertySymbols(obj);\n"
                + "    alert(objectSymbols.length);\n"
                + "    alert(typeof objectSymbols[0]);\n"
                + "    alert(typeof objectSymbols[1]);\n"

                + "    var objectNames = Object.getOwnPropertyNames(obj);\n"
                + "    alert(objectNames.length);\n"
                + "    alert(objectNames[0]);\n"

                + "  } else { alert('not defined'); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageWithAlerts2(html);
    }
}
