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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF45;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Reflect}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ReflectTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true"},
            FF38 = "no Reflect",
            IE = "no Reflect")
    @NotYetImplemented({CHROME, FF45})
    public void has() throws Exception {
        final String html = "<html><head><title>First</title><body>\n"
            + "<script>\n"
            + "  if (typeof Reflect != 'undefined') {\n"
            + "    alert(Reflect.has({x: 0}, 'x'));\n"
            + "    alert(Reflect.has({x: 0}, 'y'));\n"
            + "    alert(Reflect.has({x: 0}, 'toString'));\n"
            + "  } else { alert('no Reflect'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
