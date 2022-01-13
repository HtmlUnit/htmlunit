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

import static com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser.FF_ESR;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link Storage}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Jake Cobb
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class StorageTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "[object Storage]", "[object Storage]"})
    public void storage() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(window.globalStorage);\n"
            + "  log(window.localStorage);\n"
            + "  log(window.sessionStorage);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"global: true", "local: true", "session: true"})
    public void storageEquals() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('global: ' + (window.globalStorage === window.globalStorage));\n"
            + "  try { log('local: ' + (window.localStorage === window.localStorage)); }"
                        + " catch(e) { log('exception'); }\n"
            + "  try { log('session: ' + (window.sessionStorage === window.sessionStorage)); }"
                        + " catch(e) { log('exception'); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "1"})
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
            + LOG_TITLE_FUNCTION
            + "  if (window.localStorage) {\n"
            + "    log(typeof localStorage.hello);\n"
            + "    log(localStorage.hello);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPage2(firstHtml);
        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = getWebDriver();
        driver.get(URL_SECOND.toExternalForm());
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "2", "there", "world", "1", "0"})
    public void sessionStorage() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (window.sessionStorage) {\n"
            + "    log(sessionStorage.length);\n"
            + "    sessionStorage.hi = 'there';\n"
            + "    sessionStorage.setItem('hello', 'world');\n"
            + "    log(sessionStorage.length);\n"
            + "    log(sessionStorage.getItem('hi'));\n"
            + "    log(sessionStorage.getItem('hello'));\n"
            + "    sessionStorage.removeItem(sessionStorage.key(0));\n"
            + "    log(sessionStorage.length);\n"
            + "    if (sessionStorage.clear) {\n"
            + "      sessionStorage.clear();\n"
            + "      log(sessionStorage.length);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void globalStorage() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (window.globalStorage) {\n"
            + "    try {\n"
            + "      log(globalStorage['" + URL_FIRST.getHost() + "']);\n"
            + "      log(globalStorage['otherHost']);\n"
            + "    }\n"
            + "    catch(e) {log('error')}"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Note that this test will work only with WebDriver instances that support starting 2 instances in parallel.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("I was here")
    @BuggyWebDriver(CHROME = "",
                    EDGE = "",
                    FF = "",
                    FF_ESR = "")
    // The way ChromeDriver and FFDriver start the real browsers clears the LocalStorage somehow.
    // But when executed manually the LocalStorage is shared.
    @NotYetImplemented
    // TODO somehow persist the LocalStorage
    public void localStorageShouldBeShared() throws Exception {
        final String html1 = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('hello', 'I was here');\n"
            + "} catch(e) { log('exception'); }\n"
            + "</script></body></html>";
        final WebDriver driver = loadPage2(html1);
        final List<String> alerts = getCollectedAlerts(driver);

        final String html2 = "<html><body><script>\n"
            + "try {\n"
            + "  log(localStorage.getItem('hello'));\n"
            + "} catch(e) { log('exception'); }\n"
            + "</script></body></html>";
        getMockWebConnection().setResponse(URL_FIRST, html2);

        releaseResources();
        // we have to control 2nd driver by ourself
        WebDriver driver2 = null;
        try {
            driver2 = buildWebDriver();
            driver2.get(URL_FIRST.toString());
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
    @Alerts({"undefined", "null", "extraMethod called", "null"})
    public void prototypeIsExtensible() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  log(localStorage.extraMethod);\n"
            + "  log(localStorage.getItem('extraMethod'));\n"
            + "  Storage.prototype.extraMethod = function() {\n"
            + "    log('extraMethod called');\n"
            + "  };\n"
            + "  try {\n"
            + "    localStorage.extraMethod();\n"
            + "  } catch (e2) {\n"
            + "    log('localStorage.extraMethod not callable');\n"
            + "  }\n"
            + "  log(localStorage.getItem('extraMethod'));\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "null", "function", "value", "1"},
            IE = {"function", "null", "string", "value", "1"})
    public void prototypePropertiesAreVisible() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  log(typeof localStorage.hasOwnProperty);\n"
            + "  log(localStorage.getItem('hasOwnProperty'));\n"
            + "  localStorage.setItem('hasOwnProperty', 'value');\n"
            + "  log(typeof localStorage.hasOwnProperty);\n"
            + "  log(localStorage.getItem('hasOwnProperty'));\n"
            + "} catch (e) { log('exception'); }\n"
            + "  log(localStorage.length);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "null", "string", "null", "0"},
            FF = {"function", "null", "function", "value", "1"},
            FF_ESR = {"function", "null", "function", "value", "1"},
            IE = {"function", "null", "string", "value", "1"})
    @NotYetImplemented({FF, FF_ESR})
    public void writeToPrototypeProperty() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  log(typeof localStorage.hasOwnProperty);\n"
            + "  log(localStorage.getItem('hasOwnProperty'));\n"
            + "  localStorage.hasOwnProperty = 'value';\n"
            + "  log(typeof localStorage.hasOwnProperty);\n"
            + "  log(localStorage.getItem('hasOwnProperty'));\n"
            + "  log(localStorage.length);\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
