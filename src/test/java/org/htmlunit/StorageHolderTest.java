/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link StorageHolder}.
 * <p>
 * The local storage of the real browsers survives the tests, therefore every test starts
 * with clearing the storage. The {@link StorageHolder} API is checked if running with HtmlUnit.
 *
 * @author Ronald Brill
 * @author Vidya m
 */
public class StorageHolderTest extends WebDriverTestCase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean needThreeConnections() {
        // the second server on PORT2 is used for the other origin
        return true;
    }

    /**
     * @throws Exception in case of error
     */
    @Test
    @Alerts({"1", "Tom", "1", "Tom", "0"})
    public void localStorage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('myCat', 'Tom');\n"
            + "  log(localStorage.length);\n"
            + "  log(localStorage.getItem('myCat'));\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 2));

        if (driver instanceof HtmlUnitDriver) {
            final Map<String, String> localStorage = getWebClient().getStorageHolder().getLocalStorage(URL_FIRST);
            assertEquals(1, localStorage.size());
            assertEquals("Tom", localStorage.get("myCat"));
        }

        final String html2 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(localStorage.length);\n"
            + "  log(localStorage.getItem('myCat'));\n"
            + "  localStorage.clear();\n"
            + "  log(localStorage.length);\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        driver.get(URL_SECOND.toExternalForm());
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 2, 5));

        if (driver instanceof HtmlUnitDriver) {
            assertEquals(0, getWebClient().getStorageHolder().getLocalStorage(URL_FIRST).size());
        }
    }

    /**
     * The local storage is per origin, pages from the same host but another port
     * must not see the data.
     * @throws Exception in case of error
     */
    @Test
    @Alerts({"secret", "null"})
    public void localStorageOtherPort() throws Exception {
        final URL otherPortUrl = new URL(URL_CROSS_ORIGIN_BASE, "storage.html");

        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('token', 'secret');\n"
            + "  log(localStorage.getItem('token'));\n"
            + "</script>\n"
            + "</body></html>";

        final String html2 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('' + localStorage.getItem('token'));\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(otherPortUrl, html2);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts()[0]);

        driver.get(otherPortUrl.toExternalForm());
        verifyTitle2(driver, getExpectedAlerts()[1]);

        if (driver instanceof HtmlUnitDriver) {
            final StorageHolder storageHolder = getWebClient().getStorageHolder();
            assertEquals("secret", storageHolder.getLocalStorage(URL_FIRST).get("token"));
            assertEquals(0, storageHolder.getLocalStorage(otherPortUrl).size());
        }
    }

    /**
     * @throws Exception in case of error
     */
    @Test
    @Alerts({"1", "Tom", "1", "Tom", "0"})
    public void sessionStorage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  sessionStorage.clear();\n"
            + "  sessionStorage.setItem('myCat', 'Tom');\n"
            + "  log(sessionStorage.length);\n"
            + "  log(sessionStorage.getItem('myCat'));\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 2));

        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = getWebClient();
            final Map<String, String> sessionStorage =
                    webClient.getStorageHolder().getSessionStorage(webClient.getCurrentWindow());
            assertEquals(1, sessionStorage.size());
            assertEquals("Tom", sessionStorage.get("myCat"));
        }

        final String html2 = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(sessionStorage.length);\n"
            + "  log(sessionStorage.getItem('myCat'));\n"
            + "  sessionStorage.clear();\n"
            + "  log(sessionStorage.length);\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        driver.get(URL_SECOND.toExternalForm());
        verifyTitle2(driver, Arrays.copyOfRange(getExpectedAlerts(), 2, 5));

        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = getWebClient();
            assertEquals(0, webClient.getStorageHolder().getSessionStorage(webClient.getCurrentWindow()).size());
        }
    }

    /**
     * The session storage is per origin; another origin in the same tab must not see the data,
     * but the data is still there when coming back.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"one", "null", "one"})
    public void sessionStorageOtherOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  sessionStorage.clear();\n"
            + "  sessionStorage.setItem('s', 'one');\n"
            + "  log(sessionStorage.getItem('s'));\n"
            + "</script>\n"
            + "</body></html>";
        final String html2 = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  log('' + sessionStorage.getItem('s'));\n"
            + "</script>\n"
            + "</body></html>";
        final String html3 = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  log(sessionStorage.getItem('s'));\n"
            + "</script>\n"
            + "</body></html>";
        final URL otherOrigin = new URL(URL_THIRD, "storage.html");
        getMockWebConnection().setResponse(otherOrigin, html2);
        final URL back = new URL(URL_FIRST, "back.html");
        getMockWebConnection().setResponse(back, html3);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts()[0]);

        driver.get(otherOrigin.toExternalForm());
        verifyTitle2(driver, getExpectedAlerts()[1]);

        driver.get(back.toExternalForm());
        verifyTitle2(driver, getExpectedAlerts()[2]);
    }

    /**
     * The session storage is per origin; the same host with another port is another origin.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"one", "null"})
    public void sessionStorageOtherPort() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  sessionStorage.clear();\n"
            + "  sessionStorage.setItem('s', 'one');\n"
            + "  log(sessionStorage.getItem('s'));\n"
            + "</script>\n"
            + "</body></html>";
        final String html2 = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  log('' + sessionStorage.getItem('s'));\n"
            + "</script>\n"
            + "</body></html>";
        final URL otherPortUrl = new URL(URL_CROSS_ORIGIN_BASE, "storage.html");
        getMockWebConnection().setResponse(otherPortUrl, html2);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts()[0]);

        driver.get(otherPortUrl.toExternalForm());
        verifyTitle2(driver, getExpectedAlerts()[1]);
    }

    /**
     * An about:blank iframe has the origin of the parent and shares the storage.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent",
                      "child"})
    public void localStorageIframeAboutBlank() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<iframe id='f'></iframe>\n"
            + "<script>\n"
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('b', 'parent');\n"
            + "  var win = document.getElementById('f').contentWindow;\n"
            + "  log(win.localStorage.getItem('b'));\n"
            + "  win.localStorage.setItem('c', 'child');\n"
            + "  log(localStorage.getItem('c'));\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A same origin iframe shares the storage with the parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent",
                      "child"})
    public void localStorageIframeSameOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('b', 'parent');\n"
            + "</script>\n"
            + "<iframe src='frame.html'></iframe>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  parent.log(localStorage.getItem('b'));\n"
            + "  localStorage.setItem('c', 'child');\n"
            + "  parent.log(parent.localStorage.getItem('c'));\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frameHtml);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * A cross origin iframe must not see the storage of the parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void localStorageIframeCrossOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('b', 'parent');\n"
            + "  window.addEventListener('message', function(e) { log(e.data); });\n"
            + "</script>\n"
            + "<iframe src='" + URL_CROSS_ORIGIN_BASE + "frame.html'></iframe>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  parent.postMessage('' + localStorage.getItem('b'), '*');\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "frame.html"), frameHtml);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * An about:blank iframe has the origin of the parent and shares the storage.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent",
                      "child"})
    public void sessionStorageIframeAboutBlank() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<iframe id='f'></iframe>\n"
            + "<script>\n"
            + "  sessionStorage.clear();\n"
            + "  sessionStorage.setItem('b', 'parent');\n"
            + "  var win = document.getElementById('f').contentWindow;\n"
            + "  log(win.sessionStorage.getItem('b'));\n"
            + "  win.sessionStorage.setItem('c', 'child');\n"
            + "  log(sessionStorage.getItem('c'));\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A same origin iframe shares the storage with the parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parent",
                      "child"})
    public void sessionStorageIframeSameOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  sessionStorage.clear();\n"
            + "  sessionStorage.setItem('b', 'parent');\n"
            + "</script>\n"
            + "<iframe src='frame.html'></iframe>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  parent.log(sessionStorage.getItem('b'));\n"
            + "  sessionStorage.setItem('c', 'child');\n"
            + "  parent.log(parent.sessionStorage.getItem('c'));\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frameHtml);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * A cross origin iframe must not see the storage of the parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void sessionStorageIframeCrossOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  sessionStorage.clear();\n"
            + "  sessionStorage.setItem('b', 'parent');\n"
            + "  window.addEventListener('message', function(e) { log(e.data); });\n"
            + "</script>\n"
            + "<iframe src='" + URL_CROSS_ORIGIN_BASE + "frame.html'></iframe>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  parent.postMessage('' + sessionStorage.getItem('b'), '*');\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "frame.html"), frameHtml);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * A window opened by window.open() starts with a copy of the session storage of the opener;
     * changes are not shared.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"one",
                      "null"})
    public void sessionStorageWindowOpen() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  sessionStorage.clear();\n"
            + "  sessionStorage.setItem('a', 'one');\n"
            + "  function check() { log('' + sessionStorage.getItem('b')); }\n"
            + "  window.open('popup.html');\n"
            + "</script>\n"
            + "</body></html>";
        final String popupHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  opener.log('' + sessionStorage.getItem('a'));\n"
            + "  sessionStorage.setItem('b', 'two');\n"
            + "  opener.check();\n"
            + "  window.close();\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "popup.html"), popupHtml);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * All windows of the same origin share the local storage.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"one",
                      "two"})
    public void localStorageWindowOpen() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head><body>\n"
            + "<script>\n"
            + "  localStorage.clear();\n"
            + "  localStorage.setItem('a', 'one');\n"
            + "  function check() { log('' + localStorage.getItem('b')); }\n"
            + "  window.open('popup.html');\n"
            + "</script>\n"
            + "</body></html>";
        final String popupHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  opener.log('' + localStorage.getItem('a'));\n"
            + "  localStorage.setItem('b', 'two');\n"
            + "  opener.check();\n"
            + "  window.close();\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "popup.html"), popupHtml);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
