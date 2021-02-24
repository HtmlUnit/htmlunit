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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.net.URL;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Unit tests for {@code Worker}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
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

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Received:worker loaded")
    public void postMessageFromWorker2() throws Exception {
        final String html = "<html><body>\n"
            + "<script async>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.addEventListener('message', (e) => {\n"
            + "    alert('Received:' + e.data);\n"
            + "  });\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        final String workerJs = "postMessage('worker loaded');\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
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

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("start worker in imported script1 in imported script2 end worker")
    public void importScripts() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    document.title += e.data;\n"
            + "  };\n"
            + "} catch(e) { document.title += ' exception'; }\n"
            + "</script></body></html>\n";

        final String workerJs = "postMessage('start worker');\n"
                + "importScripts('scriptToImport1.js', 'scriptToImport2.js');\n"
                + "postMessage(' end worker');\n";

        final String scriptToImportJs1 = "postMessage(' in imported script1');\n";
        final String scriptToImportJs2 = "postMessage(' in imported script2');\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "scriptToImport1.js"), scriptToImportJs1,
                                                    MimeType.APPLICATION_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "scriptToImport2.js"), scriptToImportJs2,
                                                    MimeType.APPLICATION_JAVASCRIPT);

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "start worker import exception end worker",
            IE = "start worker in imported script1 end worker")
    public void importScriptsWrongContentType() throws Exception {
        importScripts(MimeType.TEXT_HTML);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("start worker in imported script1 end worker")
    public void importScriptsContentType() throws Exception {
        importScripts("application/ecmascript");
        importScripts(MimeType.APPLICATION_JAVASCRIPT);
        importScripts("application/x-ecmascript");
        importScripts("application/x-javascript");
        importScripts("text/ecmascript");
        importScripts("text/javascript");
        importScripts("text/javascript1.0");
        importScripts("text/javascript1.1");
        importScripts("text/javascript1.2");
        importScripts("text/javascript1.3");
        importScripts("text/javascript1.4");
        importScripts("text/javascript1.5");
        importScripts("text/jscript");
        importScripts("text/livescript");
        importScripts("text/x-ecmascript");
        importScripts("text/x-javascript");
    }

    private void importScripts(final String contentType) throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    document.title += e.data;\n"
            + "  };\n"
            + "} catch(e) { document.title += ' exception'; }\n"
            + "</script></body></html>\n";

        final String workerJs = "postMessage('start worker');\n"
                + "try {\n"
                + "  importScripts('scriptToImport1.js');\n"
                + "} catch(e) { postMessage(' import exception'); }\n"
                + "postMessage(' end worker');\n";

        final String scriptToImportJs1 = "postMessage(' in imported script1');\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "scriptToImport1.js"), scriptToImportJs1,
                contentType);

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object DedicatedWorkerGlobalScope] [object DedicatedWorkerGlobalScope] true",
            IE = "[object WorkerGlobalScope] [object WorkerGlobalScope] true")
    public void thisAndSelf() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    document.title += e.data;\n"
            + "  };\n"
            + "} catch(e) { document.tilte += ' exception'; }\n"
            + "</script></body></html>\n";

        final String workerJs = "postMessage(' ' + this);\n"
                + "postMessage(' ' + self);\n"
                + "postMessage(' ' + (this == self));\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception catched")
    public void createFromPrototypeAndDefineProperty() throws Exception {
        final String html = "<html><body><script>\n"
            + "var f = function() {};\n"
            + "f.prototype = Object.create(window.Worker.prototype);\n"
            + "try {\n"
            + "  f.prototype['onmessage'] = function() {};\n"
            + "  alert('no exception');\n"
            + "} catch(e) { alert('exception catched'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function")
    public void onmessageFunction() throws Exception {
        final String html = "<html><body><script>\n"
                + "  var myWorker = new Worker('worker.js');\n"
                + "  myWorker.onmessage = function(e) {};\n"
                + "  alert(typeof myWorker.onmessage);\n"
                + "</script></body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "exception Error")
    @NotYetImplemented(IE)
    public void onmessageNumber() throws Exception {
        final String html = "<html><body><script>\n"
                + "  var myWorker = new Worker('worker.js');\n"
                + "  try {\n"
                + "    myWorker.onmessage = 17;\n"
                + "    alert(myWorker.onmessage);\n"
                + "  } catch(e) { alert('exception ' + e.name); }\n"
                + "</script></body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "HtmlUnit")
    @NotYetImplemented(IE)
    public void onmessageString() throws Exception {
        final String html = "<html><body><script>\n"
                + "  var myWorker = new Worker('worker.js');\n"
                + "  try {\n"
                + "    myWorker.onmessage = 'HtmlUnit';\n"
                + "    alert(myWorker.onmessage);\n"
                + "  } catch(e) { alert('exception ' + e.name); }\n"
                + "</script></body></html>\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SGVsbG8gV29ybGQh", "Hello World!"})
    public void atob() throws Exception {
        final String workerJs
            = "  var data = btoa('Hello World!');\n"
            + "  postMessage(data);\n"
            + "  postMessage(atob(data));\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"exception", "exception"})
    public void atobUnicode() throws Exception {
        final String workerJs
            = "  try {\n"
            + "    btoa('I \\u2661 Unicode!');\n"
            + "  } catch(e) {postMessage('exception')}\n"
            + "  try {\n"
            + "    atob('I \\u2661 Unicode!');\n"
            + "  } catch(e) {postMessage('exception')}\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"M8OuwqY=", "3\u00C3\u00AE\u00C2\u00A6"})
    public void atobUnicodeOutput() throws Exception {
        final String workerJs
            = "  var data = btoa('3\u00C3\u00AE\u00C2\u00A6');\n"
            + "  postMessage(data);\n"
            + "  postMessage(atob(data));\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"CSAe", "\t \u001e"})
    public void atobControlChar() throws Exception {
        final String workerJs
            = "  var data = btoa('\\t \\u001e');\n"
            + "  postMessage(data);\n"
            + "  postMessage(atob(data));\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"bnVsbA==", "null"})
    public void atobNull() throws Exception {
        final String workerJs
            = "  var data = btoa(null);\n"
            + "  postMessage(data);\n"
            + "  postMessage(atob(data));\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"dW5kZWZpbmVk", "undefined"})
    public void atobUndefined() throws Exception {
        final String workerJs
            = "  var data = btoa(undefined);\n"
            + "  postMessage(data);\n"
            + "  postMessage(atob(data));\n";
        testJs(workerJs);
    }

    private void testJs(final String workerJs) throws Exception {
        final String html = "<html><body>\n"
            + "<script async>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert(e.data);\n"
            + "  };\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Received:worker loaded",
            FF = {})
    public void workerCodeWithWrongMimeType() throws Exception {
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

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.TEXT_HTML);

        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }
}
