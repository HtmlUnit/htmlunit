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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Array is a native JavaScript object and therefore provided by Rhino but behavior should be
 * different depending on the simulated browser.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class ArrayTest extends WebDriverTestCase {

    /**
     * Test for sort algorithm used (when sort is called with callback).
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented({ Browser.IE, Browser.FF2 })
    @Alerts(FF2 = { "2<>1", "1<>2", "2<>5", "9<>5", "2<>5", "2<>1", "1<>1" },
            FF3 = { "1<>5", "5<>2", "1<>2", "5<>1", "2<>1", "1<>1", "5<>9" },
            IE = { "1<>9", "9<>5", "9<>2", "9<>1", "1<>5", "5<>1", "5<>2", "5<>1", "1<>1", "1<>2", "2<>1", "1<>1" })
    public void sort() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function compare(x, y) {\n"
            + "  alert('' + x + '<>' + y);\n"
            + "  return x - y;\n"
            + "}\n"
            + "function doTest() {\n"
            + "    var t = [1, 5, 2, 1, 9];\n"
            + "    t.sort(compare);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
