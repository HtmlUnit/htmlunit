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
package org.htmlunit.javascript.host.xml;

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the events of an asynchronous {@link XMLHttpRequest}: like in the browsers, the state changes
 * and the events are processed after the script that called send() has finished, also if the response
 * is already there (see https://github.com/HtmlUnit/htmlunit/issues/245).
 * The scripts wait some time after send() to make sure the response is available before the script ends.
 *
 * @author Ronald Brill
 */
public class XMLHttpRequestAsyncEventsTest extends WebDriverTestCase {

    // a busy wait, the response of the request is available afterwards
    private static final String WAIT = "  var start = Date.now(); while (Date.now() - start < 300) {}\n";

    private void test(final String script) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "</script></head>\n"
            + "<body>\n"
            + "<script>\n"
            + script
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "foo.txt"), "Hello", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * The handlers are assigned after send(); they are called nevertheless.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"state 2", "state 3", "state 4", "load"})
    public void eventHandlerAssignedAfterSend() throws Exception {
        test("  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.txt', true);\n"
            + "  xhr.send();\n"
            + WAIT
            + "  xhr.onreadystatechange = function() { log('state ' + xhr.readyState); };\n"
            + "  xhr.onload = function() { log('load'); };\n");
    }

    /**
     * The listeners are added after send(); they are called nevertheless.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"readystatechange 2", "readystatechange 3", "readystatechange 4", "load 4", "loadend 4"})
    public void eventListenerAddedAfterSend() throws Exception {
        test("  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.txt', true);\n"
            + "  xhr.send();\n"
            + WAIT
            + "  var listener = function(e) { log(e.type + ' ' + xhr.readyState); };\n"
            + "  xhr.addEventListener('readystatechange', listener);\n"
            + "  xhr.addEventListener('load', listener);\n"
            + "  xhr.addEventListener('loadend', listener);\n");
    }

    /**
     * While the script that called send() is running, the state does not change.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "", "load 4 200 Hello"})
    public void stateDuringScript() throws Exception {
        test("  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.txt', true);\n"
            + "  xhr.onload = function() {\n"
            + "    log('load ' + xhr.readyState + ' ' + xhr.status + ' ' + xhr.responseText);\n"
            + "  };\n"
            + "  xhr.send();\n"
            + WAIT
            + "  log(xhr.readyState);\n"
            + "  log(xhr.status);\n"
            + "  log(xhr.responseText);\n");
    }

    /**
     * The events are not processed in the middle of the script that called send().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"after send", "end of script", "state 2", "state 3", "state 4"})
    public void eventsAfterScript() throws Exception {
        test("  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.txt', true);\n"
            + "  xhr.onreadystatechange = function() { log('state ' + xhr.readyState); };\n"
            + "  xhr.send();\n"
            + "  log('after send');\n"
            + WAIT
            + "  log('end of script');\n");
    }

    /**
     * The response is written into the document by the load handler assigned after send()
     * (the case of the issue).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello")
    public void responseWrittenByHandlerAssignedAfterSend() throws Exception {
        test("  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.txt', true);\n"
            + "  xhr.send();\n"
            + WAIT
            + "  xhr.onload = function() {\n"
            + "    var div = document.createElement('div');\n"
            + "    div.id = 'result';\n"
            + "    div.textContent = xhr.responseText;\n"
            + "    document.body.appendChild(div);\n"
            + "    log(document.getElementById('result').textContent);\n"
            + "  };\n");
    }
}
