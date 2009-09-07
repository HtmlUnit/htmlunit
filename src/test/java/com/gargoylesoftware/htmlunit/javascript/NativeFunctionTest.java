/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Function is a native JavaScript object and therefore provided by Rhino but some tests are needed here
 * to be sure that we have the expected results (for instance "bind" is an EcmaScript 5 method that is not
 * available in FF2 or FF3).
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class NativeFunctionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "apply: function", "arguments: object", "bind: undefined", "call: function", "constructor: function",
        "toSource: function", "toString: function" })
    public void methods() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var o = function() {};\n"
            + "  var props = ['apply', 'arguments', 'bind', 'call', 'constructor', 'toSource', 'toString'];\n"
            + "  for (var i=0; i<props.length; ++i) {\n"
            + "    var p = props[i];\n"
            + "    alert(p + ': ' + typeof(o[p]));\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
