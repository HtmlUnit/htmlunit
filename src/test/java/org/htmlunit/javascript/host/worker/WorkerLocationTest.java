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
package org.htmlunit.javascript.host.worker;

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link WorkerLocation}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WorkerLocationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§worker.js#somehash")
    public void location() throws Exception {
        final String workerJs = "postMessage('' + location);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("object")
    public void typeOf() throws Exception {
        final String workerJs = "postMessage(typeof location);\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§worker.js#somehash")
    public void href() throws Exception {
        final String workerJs = "postMessage(location.href);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("http:")
    public void protocol() throws Exception {
        final String workerJs = "postMessage(location.protocol);\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void host() throws Exception {
        final String workerJs = "postMessage(location.host);\n";

        expandExpectedAlertsVariables("localhost:" + PORT);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("localhost")
    public void hostname() throws Exception {
        final String workerJs = "postMessage(location.hostname);\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void origin() throws Exception {
        final String workerJs = "postMessage(location.origin);\n";

        expandExpectedAlertsVariables("http://localhost:" + PORT);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void port() throws Exception {
        final String workerJs = "postMessage(location.port);\n";

        expandExpectedAlertsVariables("" + PORT);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("/worker.js")
    public void pathname() throws Exception {
        final String workerJs = "postMessage(location.pathname);\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void search() throws Exception {
        final String workerJs = "postMessage(location.search);\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("#somehash")
    public void hash() throws Exception {
        final String workerJs = "postMessage(location.hash);\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ctor() throws Exception {
        final String workerJs =
                "try {\n"
                + "  var l = new WorkerLocation();\n"
                + "  postMessage(l);\n"
                + "} catch(e) {\n"
                + "  postMessage('exception');\n"
                + "}";
        testJs(workerJs);
    }

    private void testJs(final String workerJs) throws Exception {
        final String html = "<html><body>\n"
            + "<script async>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "try {\n"
            + "  var myWorker = new Worker('worker.js#somehash');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    log(e.data);\n"
            + "  };\n"
            + "} catch(e) { log('exception'); }\n"
            + "</script></body></html>\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"),
                workerJs, MimeType.TEXT_JAVASCRIPT);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
