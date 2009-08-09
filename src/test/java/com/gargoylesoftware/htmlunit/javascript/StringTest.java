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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * String is a native JavaScript object and therefore provided by Rhino but some tests are needed here.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class StringTest extends WebTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2783950">2783950</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("key\\:b_M")
    public void replace() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert('key:b_M'.replace(':', '\\\\:'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
