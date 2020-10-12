/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSS}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CSSTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function CSS() { [native code] }", "[object CSS]"},
            FF = {"[object Object]", "undefined"},
            FF78 = {"[object Object]", "undefined"},
            IE = "Exception")
    public void global() throws Exception {
        final String html
            = "<html><body>\n"
            + "<style>@charset 'UTF-8';</style>\n"
            + "<script>\n"
            + "  try {\n"
            + "    alert(CSS);"
            + "    alert(CSS.prototype);"
            + "  } catch (e) { alert('Exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CSS]",
            FF = "ctor Exception",
            FF78 = "ctor Exception",
            IE = "ctor Exception")
    @HtmlUnitNYI(CHROME = "[object Object]",
            EDGE = "[object Object]")
    public void constructor() throws Exception {
        final String html
            = "<html><body>\n"
            + "<style>@charset 'UTF-8';</style>\n"
            + "<script>\n"
            + "  try {\n"
            + "    var o = Object.create(CSS.prototype);\n"
            + "    alert(o);"
            + "  } catch (e) { alert('ctor Exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
