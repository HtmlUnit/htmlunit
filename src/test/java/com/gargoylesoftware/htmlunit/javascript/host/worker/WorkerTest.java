/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.worker;

import java.net.URL;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@code Worker}.
 *
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class WorkerTest extends WebDriverTestCase {

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
    @Alerts("Received:worker loaded")
    public void postMessageFromWorker() throws Exception {
        final String html = "<html><body>\n"
            + "<script async>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received:' + e.data);\n"
            + "  };\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "postMessage('worker loaded');\n";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "worker.js"), workerJs);

        loadPageWithAlerts2(html, 2000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Received: Result = 15")
    public void postMessageToWorker() throws Exception {
        final String html = "<html><body><script>\n"
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

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "worker.js"), workerJs);

        loadPageWithAlerts2(html, 2000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "start worker", "in imported script1", "in imported script2", "end worker" })
    public void importScripts() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert(e.data);\n"
            + "  };\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "postMessage('start worker');\n"
                + "importScripts('scriptToImport1.js', 'scriptToImport2.js');\n"
                + "postMessage('end worker');\n";

        final String scriptToImportJs1 = "postMessage('in imported script1');\n";
        final String scriptToImportJs2 = "postMessage('in imported script2');\n";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "worker.js"), workerJs);
        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "scriptToImport1.js"), scriptToImportJs1);
        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "scriptToImport2.js"), scriptToImportJs2);

        loadPageWithAlerts2(html, 2000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object DedicatedWorkerGlobalScope]", "[object DedicatedWorkerGlobalScope]", "true" },
            CHROME = { "[object global]", "[object global]", "true" },
            IE = { "[object WorkerGlobalScope]", "[object WorkerGlobalScope]", "true" })
    public void thisAndSelf() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert(e.data);\n"
            + "  };\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "postMessage('' + this);\n"
                + "postMessage('' + self);\n"
                + "postMessage('' + (this == self));\n";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "worker.js"), workerJs);

        loadPageWithAlerts2(html, 2000);
    }
}
