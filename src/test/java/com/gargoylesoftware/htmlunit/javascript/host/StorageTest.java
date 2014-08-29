/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Storage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Jake Cobb
 */
@RunWith(BrowserRunner.class)
public class StorageTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "[object Storage]", "[object Storage]" },
            IE8 = { "undefined", "[object]", "[object]" })
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
    @Alerts({ "true", "true", "true" })
    public void storageEquals() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "alert(window.globalStorage === window.globalStorage);\n"
            + "alert(window.localStorage === window.localStorage);\n"
            + "alert(window.sessionStorage === window.sessionStorage);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "string", "1" })
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
    @Alerts({ "0", "2", "there", "world", "1", "0" })
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

    /**
     * Note that this test will work only with WebDriver instances that support starting 2 instances in parallel.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "I was here",
            IE8 = "")
    @BuggyWebDriver({ CHROME, FF })
    // The way ChromeDriver and FFDriver start the real browsers clears the LocalStorage somehow.
    // But when executed manually the LocalStorage is shared.
    @NotYetImplemented
    // TODO somehow persist the LocalStorage
    public void localStorageShouldBeShared() throws Exception {
        final String html1 = "<html><body><script>\n"
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('hello', 'I was here');\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";
        final WebDriver driver = loadPage2(html1);
        final List<String> alerts = getCollectedAlerts(driver);

        final String html2 = "<html><body><script>\n"
            + "try {\n"
            + "    alert(localStorage.getItem('hello'));\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";
        getMockWebConnection().setResponse(getDefaultUrl(), html2);

        releaseResources();
        // we have to control 2nd driver by ourself
        WebDriver driver2 = null;
        try {
            driver2 = buildWebDriver();
            driver2.get(getDefaultUrl().toString());
            final List<String> newAlerts = getCollectedAlerts(driver2);
            alerts.addAll(newAlerts);
            assertEquals(getExpectedAlerts(), alerts);
        }
        finally {
            if (!(driver2 instanceof HtmlUnitDriver)) {
                shutDownAll();
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "null", "extraMethod called", "null" },
        IE8 = { "undefined", "null", "exception" })
    public void prototypeIsExtensible() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  alert(localStorage.extraMethod);\n"
            + "  alert(localStorage.getItem('extraMethod'));\n"
            + "  Storage.prototype.extraMethod = function() {\n"
            + "    alert('extraMethod called');\n"
            + "  };\n"
            + "  try {\n"
            + "    localStorage.extraMethod();\n"
            + "  } catch (e2) {\n"
            + "    alert('localStorage.extraMethod not callable');\n"
            + "  }\n"
            + "  alert(localStorage.getItem('extraMethod'));\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function", "null", "function", "value", "1" },
            IE = { "function", "null", "string", "value", "1" })
    public void prototypePropertiesAreVisible() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  alert(typeof localStorage.hasOwnProperty);\n"
            + "  alert(localStorage.getItem('hasOwnProperty'));\n"
            + "  localStorage.setItem('hasOwnProperty', 'value');\n"
            + "  alert(typeof localStorage.hasOwnProperty);\n"
            + "  alert(localStorage.getItem('hasOwnProperty'));\n"
            + "} catch (e) { alert('exception'); }\n"
            + "  alert(localStorage.length);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function", "null", "string", "value", "1" },
            CHROME = { "function", "null", "string", "null", "0" })
    public void writeToPrototypeProperty() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  alert(typeof localStorage.hasOwnProperty);\n"
            + "  alert(localStorage.getItem('hasOwnProperty'));\n"
            + "  localStorage.hasOwnProperty = 'value';\n"
            + "  alert(typeof localStorage.hasOwnProperty);\n"
            + "  alert(localStorage.getItem('hasOwnProperty'));\n"
            + "  alert(localStorage.length);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
