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
package org.htmlunit.javascript.host.event;

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link StorageEvent}.
 *
 * @author Ronald Brill
 */
public class StorageEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
            + "    log(event);\n"
            + "    log(event.type);\n"
            + "    log(event.bubbles);\n"
            + "    log(event.cancelable);\n"
            + "    log(event.composed);\n"
            + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object StorageEvent]", "storage", "false", "false", "false"})
    public void create_ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new StorageEvent('storage');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    @HtmlUnitNYI(CHROME = {"[object StorageEvent]", "undefined", "false", "false", "false"},
                EDGE = {"[object StorageEvent]", "undefined", "false", "false", "false"},
                FF = {"[object StorageEvent]", "undefined", "false", "false", "false"},
                FF_ESR = {"[object StorageEvent]", "undefined", "false", "false", "false"})
    public void create_ctorWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new StorageEvent();\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object StorageEvent]", "42", "false", "false", "false"})
    public void create_ctorNumericType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new StorageEvent(42);\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object StorageEvent]", "null", "false", "false", "false"})
    public void create_ctorNullType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new StorageEvent(null);\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ReferenceError")
    public void create_ctorUnknownType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new StorageEvent(unknown);\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object StorageEvent]", "HtmlUnitEvent", "false", "false", "false"})
    public void create_ctorArbitraryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new StorageEvent('HtmlUnitEvent');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object StorageEvent]", "storage", "false", "false", "false"})
    public void create_ctorAllDetails() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var debug = {hello: 'world'};\n"
            + "      var event = new StorageEvent('storage', {\n"
            + "        'key': 'aKey',\n"
            + "        'newValue': 'aNewValue',\n"
            + "        'oldValue': 'anOldValue',\n"
            + "        'storageArea': null,\n"
            + "        'url': 'aUrl'\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object StorageEvent]", "storage", "false", "false", "false"})
    public void create_ctorAllDetailsMissingData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new StorageEvent('storage', {\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void create_ctorAllDetailsWrongData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new StorageEvent('storage', {\n"
            + "        'storageArea': 'wrong'\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object StorageEvent]", "", "false", "false", "false"})
    @HtmlUnitNYI(CHROME = "NotSupportedError/DOMException",
            EDGE = "NotSupportedError/DOMException",
            FF = "NotSupportedError/DOMException",
            FF_ESR = "NotSupportedError/DOMException")
    public void create_createEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('StorageEvent');\n"
            + "      dump(event);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void inWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('StorageEvent' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The storage event is fired in the other windows of the same origin, not in the own one.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"storage,k,null,v1,§§URL§§,true,true,false,false",
             "storage,k,v1,v2,§§URL§§,true,true,false,false"})
    public void storageEventSetItem() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  localStorage.clear();\n"
            + "</script></head><body>\n"
            + "<iframe id='f' src='frame.html'></iframe>\n"
            + "<script>\n"
            + "  window.addEventListener('storage', function(e) { log('own window ' + e.key); });\n"
            + "  window.onload = function() {\n"
            + "    localStorage.setItem('k', 'v1');\n"
            + "    localStorage.setItem('k', 'v2');\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  window.addEventListener('storage', function(e) {\n"
            + "    parent.log(e.type + ',' + e.key + ',' + e.oldValue + ',' + e.newValue + ',' + e.url\n"
            + "        + ',' + (e.storageArea === localStorage) + ',' + (e instanceof StorageEvent)\n"
            + "        + ',' + e.bubbles + ',' + e.cancelable);\n"
            + "  });\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frameHtml);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Setting the same value again does not fire an event.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("storage,k,null,v1,§§URL§§,true,true,false,false")
    public void storageEventSameValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  localStorage.clear();\n"
            + "</script></head><body>\n"
            + "<iframe id='f' src='frame.html'></iframe>\n"
            + "<script>\n"
            + "  window.addEventListener('storage', function(e) { log('own window ' + e.key); });\n"
            + "  window.onload = function() {\n"
            + "    localStorage.setItem('k', 'v1');\n"
            + "    localStorage.setItem('k', 'v1');\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  window.addEventListener('storage', function(e) {\n"
            + "    parent.log(e.type + ',' + e.key + ',' + e.oldValue + ',' + e.newValue + ',' + e.url\n"
            + "        + ',' + (e.storageArea === localStorage) + ',' + (e instanceof StorageEvent)\n"
            + "        + ',' + e.bubbles + ',' + e.cancelable);\n"
            + "  });\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frameHtml);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Removing an item fires an event, removing a missing one does not.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"storage,k,null,v1,§§URL§§,true,true,false,false",
             "storage,k,v1,null,§§URL§§,true,true,false,false"})
    public void storageEventRemoveItem() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  localStorage.clear();\n"
            + "</script></head><body>\n"
            + "<iframe id='f' src='frame.html'></iframe>\n"
            + "<script>\n"
            + "  window.addEventListener('storage', function(e) { log('own window ' + e.key); });\n"
            + "  window.onload = function() {\n"
            + "    localStorage.setItem('k', 'v1');\n"
            + "    localStorage.removeItem('k');\n"
            + "    localStorage.removeItem('missing');\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  window.addEventListener('storage', function(e) {\n"
            + "    parent.log(e.type + ',' + e.key + ',' + e.oldValue + ',' + e.newValue + ',' + e.url\n"
            + "        + ',' + (e.storageArea === localStorage) + ',' + (e instanceof StorageEvent)\n"
            + "        + ',' + e.bubbles + ',' + e.cancelable);\n"
            + "  });\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frameHtml);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Clearing fires an event with the key null (only if there was something to clear).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"storage,k,null,v1,§§URL§§,true,true,false,false",
             "storage,null,null,null,§§URL§§,true,true,false,false"})
    public void storageEventClear() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  localStorage.clear();\n"
            + "</script></head><body>\n"
            + "<iframe id='f' src='frame.html'></iframe>\n"
            + "<script>\n"
            + "  window.addEventListener('storage', function(e) { log('own window ' + e.key); });\n"
            + "  window.onload = function() {\n"
            + "    localStorage.setItem('k', 'v1');\n"
            + "    localStorage.clear();\n"
            + "    localStorage.clear();\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  window.addEventListener('storage', function(e) {\n"
            + "    parent.log(e.type + ',' + e.key + ',' + e.oldValue + ',' + e.newValue + ',' + e.url\n"
            + "        + ',' + (e.storageArea === localStorage) + ',' + (e instanceof StorageEvent)\n"
            + "        + ',' + e.bubbles + ',' + e.cancelable);\n"
            + "  });\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frameHtml);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * The session storage fires the event in the same origin iframes of the tab.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("storage,k,null,v1,§§URL§§,true,true,false,false")
    public void storageEventSessionStorage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  sessionStorage.clear();\n"
            + "</script></head><body>\n"
            + "<iframe id='f' src='frame.html'></iframe>\n"
            + "<script>\n"
            + "  window.addEventListener('storage', function(e) { log('own window ' + e.key); });\n"
            + "  window.onload = function() {\n"
            + "    sessionStorage.setItem('k', 'v1');\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        final String frameHtml = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  window.addEventListener('storage', function(e) {\n"
            + "    parent.log(e.type + ',' + e.key + ',' + e.oldValue + ',' + e.newValue + ',' + e.url\n"
            + "        + ',' + (e.storageArea === sessionStorage) + ',' + (e instanceof StorageEvent)\n"
            + "        + ',' + e.bubbles + ',' + e.cancelable);\n"
            + "  });\n"
            + "</script></body></html>\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frameHtml);
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
