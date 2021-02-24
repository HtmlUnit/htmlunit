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
package com.gargoylesoftware.htmlunit.javascript.host.worker;

import java.net.URL;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@code DedicatedWorkerGlobalScope}.
 *
 * @author Ronald Brill
 * @author Rural Hunter
 */
@RunWith(BrowserRunner.class)
public class DedicatedWorkerGlobalScopeTest extends WebDriverTestCase {

    /**
     * Closes the real ie because clearing all cookies seem to be not working
     * at the moment.
     */
    @After
    public void shutDownRealBrowsersAfter() {
        shutDownRealIE();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Received: Result = 15")
    public void onmessage() throws Exception {
        final String html = "<html><body>"
            + "<script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received: ' + e.data);\n"
            + "  };\n"
            + "  setTimeout(function() { myWorker.postMessage([5, 3]);}, 10);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "onmessage = function(e) {\n"
                + "  var workerResult = 'Result = ' + (e.data[0] * e.data[1]);\n"
                + "  postMessage(workerResult);\n"
                + "}\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Received: Result = 15")
    public void selfOnmessage() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received: ' + e.data);\n"
            + "  };\n"
            + "  setTimeout(function() { myWorker.postMessage([5, 3]);}, 10);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "self.onmessage = function(e) {\n"
                + "  var workerResult = 'Result = ' + (e.data[0] * e.data[1]);\n"
                + "  postMessage(workerResult);\n"
                + "}\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Received: Result = 15")
    public void selfAddEventListener() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received: ' + e.data);\n"
            + "  };\n"
            + "  setTimeout(function() { myWorker.postMessage([5, 3]);}, 10);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "self.addEventListener('message', (e) => {\n"
                + "  var workerResult = 'Result = ' + (e.data[0] * e.data[1]);\n"
                + "  postMessage(workerResult);\n"
                + "});\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Received: timeout")
    public void selfSetTimeout() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received: ' + e.data);\n"
            + "  };\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "self.setTimeout(function() {\n"
                + "  postMessage('timeout');\n"
                + "}, 10);\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Received: interval")
    public void selfSetInterval() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received: ' + e.data);\n"
            + "  };\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "var id = self.setInterval(function() {\n"
                + "  postMessage('interval');\n"
                + "  clearInterval(id);\n"
                + "}, 10);\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "Received: func=function addEventListener() { [native code] }",
            FF = "Received: func=function addEventListener() {\n    [native code]\n}",
            FF78 = "Received: func=function addEventListener() {\n    [native code]\n}",
            IE = "Received: func=\nfunction addEventListener() {\n    [native code]\n}\n")
    public void functionDefaultValue() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received: ' + e.data);\n"
            + "  };\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "postMessage('func='+self.addEventListener);";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Received: Result = 15",
            FF = {})
    public void workerCodeWithWrongMimeType() throws Exception {
        final String html = "<html><body>"
            + "<script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received: ' + e.data);\n"
            + "  };\n"
            + "  setTimeout(function() { myWorker.postMessage([5, 3]);}, 10);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "onmessage = function(e) {\n"
                + "  var workerResult = 'Result = ' + (e.data[0] * e.data[1]);\n"
                + "  postMessage(workerResult);\n"
                + "}\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.TEXT_HTML);

        loadPageWithAlerts2(html, 2000);
    }
}
