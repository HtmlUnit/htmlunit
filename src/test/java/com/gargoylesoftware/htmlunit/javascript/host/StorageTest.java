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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link Storage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StorageTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined", "undefined" }, IE8 = { "undefined", "[object]", "[object]" },
            FF = { "[object StorageList]", "[object Storage]", "[object Storage]" })
    @NotYetImplemented(Browser.FF3_6)
    public void storage() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "  alert(window.globalStorage);\n"
            + "  alert(window.localStorage);\n"
            + "  alert(window.sessionStorage);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = { "string", "1" },
            FF = { "string", "1" })
    public void localStorage() throws Exception {
        final String firstHtml
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "  if (window.localStorage) {\n"
            + "    localStorage.hello = 1;\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "  if (window.localStorage) {\n"
            + "    alert(typeof localStorage.hello);\n"
            + "    alert(localStorage.hello);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPage2(firstHtml);
        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = getWebDriver();
        driver.get(URL_SECOND.toExternalForm());

        final List<String> actualAlerts = getCollectedAlerts(driver);
        assertEquals(getExpectedAlerts(), actualAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = { "0", "2", "there", "world", "1", "0" },
            FF = { "0", "2", "there", "world", "1", "0" })
    public void sessionStorage() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "  if (window.sessionStorage) {\n"
            + "    alert(sessionStorage.length);\n"
            + "    sessionStorage.hi = 'there';\n"
            + "    sessionStorage.setItem('hello', 'world');\n"
            + "    alert(sessionStorage.length);\n"
            + "    alert(sessionStorage.getItem('hi'));\n"
            + "    alert(sessionStorage.getItem('hello'));\n"
            + "    sessionStorage.removeItem(sessionStorage.key(0));\n"
            + "    alert(sessionStorage.length);\n"
            + "    if (sessionStorage.clear) {\n"
            + "      sessionStorage.clear();\n"
            + "      alert(sessionStorage.length);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object StorageObsolete]", "error" })
    public void globalStorage() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "  if (window.globalStorage) {\n"
            + "    try {\n"
            + "      alert(globalStorage['" + URL_FIRST.getHost() + "']);\n"
            + "      alert(globalStorage['otherHost']);\n"
            + "    }\n"
            + "    catch(e) {alert('error')};"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
