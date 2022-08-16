/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link InstallTrigger}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class InstallTriggerTest extends WebDriverTestCase {

    /**
     * Strange but this is the reality for browsers.
     * Because there will be still some sites using this for browser detection the property is
     * set to null.
     * https://stackoverflow.com/questions/9847580/how-to-detect-safari-chrome-ie-firefox-and-opera-browsers
     * https://bugzilla.mozilla.org/show_bug.cgi?id=1442035
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = {"null", "object", "{\"enumerable\":true,\"configurable\":true}"},
            FF_ESR = {"[object InstallTriggerImpl]", "object", "{\"enumerable\":true,\"configurable\":true}"})
    @HtmlUnitNYI(
            FF = {"null", "object", "{\"value\":null,\"writable\":true,\"enumerable\":true,\"configurable\":true}"},
            FF_ESR = {"[object InstallTriggerImpl]",
                      "object",
                      "{\"value\":{\"SKIN\":1,\"LOCALE\":2,\"CONTENT\":4,\"PACKAGE\":7},"
                              + "\"writable\":true,\"enumerable\":false,\"configurable\":true}"})
    public void windowProperty() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(window.InstallTrigger);\n"
                + "    if (typeof InstallTrigger !== 'undefined') {\n"
                + "      log(typeof InstallTrigger);\n"
                + "      log(JSON.stringify(Object.getOwnPropertyDescriptor(window, 'InstallTrigger')));\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
