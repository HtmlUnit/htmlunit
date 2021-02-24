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
 * Tests for setup done in {@link net.sourceforge.htmlunit.corejs.javascript.NativeGlobal}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NativeGlobalTest extends WebDriverTestCase {

    /**
     * Test assignment of constants.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "NaN", "Infinity"})
    public void assignConst() throws Exception {
        final String html
            = "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "undefined = 123;\n"
            + "log(undefined);\n"
            + "NaN = 123;\n"
            + "log(NaN);\n"
            + "Infinity = 123;\n"
            + "log(Infinity);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for re-declaration of constants.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "undefined", "NaN", "Infinity"})
    public void redeclareConst() throws Exception {
        final String html
            = "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "var undefined;\n"
            + "var NaN;\n"
            + "var Infinity;\n"
            + "log(window.foo == undefined);\n"
            + "undefined = 123;\n"
            + "log(undefined);\n"
            + "log(NaN);\n"
            + "log(Infinity);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }
}
