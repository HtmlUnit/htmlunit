/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link Window}. The only difference with {@link WindowTest} is that these
 * tests already run with BrowserRunner.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class WindowTest2 extends WebTestCase {

    /**
     * "window.controllers" is used by some JavaScript libraries to determine if the
     * browser is Gecko based or not.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "not found", "hello" }, FF = { "found", "hello" })
    public void FF_controllers() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "if (window.controllers)\n"
            + "  alert('found')\n"
            + "else\n"
            + "  alert('not found')\n"
            + "window.controllers = 'hello';\n"
            + "alert(window.controllers);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
