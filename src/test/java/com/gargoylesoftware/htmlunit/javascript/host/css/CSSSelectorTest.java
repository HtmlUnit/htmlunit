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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for CSS selectors.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CSSSelectorTest extends WebDriverTestCase {

    /**
     * Test for bug 3300434: CSS3 selector is not yet supported.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void css3() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('table:nth-child(1) td').length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Exception should be thrown for an invalid selector.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void invalid() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    alert(document.querySelectorAll('td:gt(4)').length);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
