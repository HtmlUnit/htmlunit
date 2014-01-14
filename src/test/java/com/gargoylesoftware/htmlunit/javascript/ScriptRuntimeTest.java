/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
 * Test for ScriptRuntime.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ScriptRuntimeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "50", "100", "xxx", "zzz", "yyy" },
            FF17 = {"xxx", "50", "zzz", "100", "0", "yyy" },
            IE8 = {"xxx", "50", "zzz", "100", "0", "yyy" })
    public void enumChangeObject() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var value = {\n"
            + "    'xxx': 'testxxx',\n"
            + "    '50': 'test50',\n"
            + "    'zzz': 'testzzz',\n"
            + "    '100': 'test100',\n"
            + "    '0': 'test0',\n"
            + "    'yyy': 'testyyy'\n"
            + "    };\n"
            + "  for (var x in value) {\n"
            + "   alert(x);\n"
            + "  };"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
