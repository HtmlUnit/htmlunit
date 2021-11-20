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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLVideoElement}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLVideoElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void prototype() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "log(HTMLVideoElement.prototype == null);\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

}
