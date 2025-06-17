/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link Storage}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Jake Cobb
 * @author Ronald Brill
 */
public class StorageTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Storage]", "[object Storage]"})
    public void storage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
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
    @Alerts({"local: true", "session: true"})
    public void storageEquals() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try { log('local: ' + (window.localStorage === window.localStorage)); }"
                        + " catch(e) { logEx(e); }\n"
            + "  try { log('session: ' + (window.sessionStorage === window.sessionStorage)); }"
                        + " catch(e) { logEx(e); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "1"})
    public void localStorage() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + "  if (window.localStorage) {\n"
            + "    localStorage.hello = 1;\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        final String secondHtml = DOCTYPE_HTML
            + "<html><head></head><body>\n"
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
    @Alerts({"works 5200000", "fails 5290000"})
    public void localStorageSizeOneEntry() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (window.localStorage) {\n"
            + "    localStorage.clear();\n"

            + "    var content = '';"
            + "    for (var i = 0; i < 10000; i++) { content += '0123456789' }"
            + "    var bigContent = '';\n"

            + "    for (var i = 0; i < 52; i++) {\n"
            + "      bigContent += content;\n"
            + "    }"

            + "    try {"
            + "      localStorage.setItem('HtmlUnit', bigContent);\n"
            + "      log('works ' + bigContent.length);\n"
            + "    } catch(e) {\n"
            + "      log('fails ' + bigContent.length);\n"
            + "    }\n"

            + "    content = '';"
            + "    for (var i = 0; i < 9000; i++) { content += '0123456789' }"
            + "    bigContent += content;\n"
            + "    try {"
            + "      localStorage.setItem('HtmlUnit', bigContent);\n"
            + "      log('works ' + bigContent.length);\n"
            + "    } catch(e) {\n"
            + "      log('fails ' + bigContent.length);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(firstHtml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"works 52", "fails"})
    public void localStorageSizeManyEntries() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (window.localStorage) {\n"
            + "    localStorage.clear();\n"

            + "    var content = '';"
            + "    for (var i = 0; i < 10000; i++) { content += '0123456789' }"
            + "    var bigContent = content;\n"

            + "    for (var i = 0; i < 52; i++) {\n"
            + "      try {"
            + "        localStorage.setItem('HtmlUnit-' + i, bigContent);\n"
            + "      } catch(e) {\n"
            + "        log('fails ' + i);\n"
            + "        break;\n"
            + "      }\n"
            + "    }"
            + "    log('works ' + i);\n"

            + "    try {"
            + "      localStorage.setItem('HtmlUnit-xx', bigContent);\n"
            + "      log('works');\n"
            + "    } catch(e) {\n"
            + "      log('fails');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(firstHtml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "null", "null"})
    public void localStorageKey() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (window.localStorage) {\n"
            + "    localStorage.clear();\n"

            + "    localStorage.setItem('HtmlUnit', '0');\n"
            + "    localStorage.setItem('HtmlUnit 1', '1');\n"
            + "    localStorage.setItem('HtmlUnit 2', '2');\n"

            + "    log(localStorage.key(0).startsWith('HtmlUnit'));\n"
            + "    log(localStorage.key(1).startsWith('HtmlUnit'));\n"
            + "    log(localStorage.key(3));\n"
            + "    log(localStorage.key(-1));\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "2", "there", "world", "1", "0"})
    public void sessionStorage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
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
     * Note that this test will work only with WebDriver instances that support starting 2 instances in parallel.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "I was here§"})
    @HtmlUnitNYI(CHROME = {"", "null§"},
            EDGE = {"", "null§"},
            FF = {"", "null§"},
            FF_ESR = {"", "null§"})
    // TODO somehow persist the LocalStorage
    public void localStorageShouldBeShared() throws Exception {
        final String html1 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('hello', 'I was here');\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        WebDriver driver = loadPage2(html1);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());
        releaseResources();

        final String html2 = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(localStorage.getItem('hello'));\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";
        getMockWebConnection().setResponse(URL_FIRST, html2);

        // we have to control 2nd driver by ourself
        driver = loadPage2(html2);
        assertEquals(getExpectedAlerts()[1], driver.getTitle());

        if (!(driver instanceof HtmlUnitDriver)) {
            shutDownAll();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "null", "extraMethod called", "null"})
    public void prototypeIsExtensible() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
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
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "null", "function", "value", "1"})
    public void prototypePropertiesAreVisible() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  localStorage.clear();\n"
            + "  log(typeof localStorage.hasOwnProperty);\n"
            + "  log(localStorage.getItem('hasOwnProperty'));\n"
            + "  localStorage.setItem('hasOwnProperty', 'value');\n"
            + "  log(typeof localStorage.hasOwnProperty);\n"
            + "  log(localStorage.getItem('hasOwnProperty'));\n"
            + "} catch(e) { logEx(e); }\n"
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
            FF_ESR = {"function", "null", "function", "value", "1"})
    @HtmlUnitNYI(FF = {"function", "null", "string", "value", "1"},
            FF_ESR = {"function", "null", "string", "value", "1"})
    public void writeToPrototypeProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
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
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
