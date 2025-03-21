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
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link WorkerNavigator}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WorkerNavigatorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object\\sWorkerNavigator]")
    public void navigator() throws Exception {
        final String workerJs = "postMessage('' + navigator);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("object")
    public void typeOf() throws Exception {
        final String workerJs = "postMessage(typeof navigator);\n";
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Mozilla")
    public void appCodeName() throws Exception {
        final String workerJs = "postMessage(navigator.appCodeName);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Netscape")
    public void appName() throws Exception {
        final String workerJs = "postMessage(navigator.appName);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "5.0\\s(Windows\\sNT\\s10.0;\\sWin64;\\sx64)\\sAppleWebKit/537.36\\s"
                        + "(KHTML,\\slike\\sGecko)\\sChrome/134.0.0.0\\sSafari/537.36",
            EDGE = "5.0\\s(Windows\\sNT\\s10.0;\\sWin64;\\sx64)\\sAppleWebKit/537.36\\s"
                        + "(KHTML,\\slike\\sGecko)\\sChrome/134.0.0.0\\sSafari/537.36\\sEdg/134.0.0.0",
            FF = "5.0\\s(Windows)",
            FF_ESR = "5.0\\s(Windows)")
    public void appVersion() throws Exception {
        final String workerJs = "postMessage(navigator.appVersion);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            FF = "undefined",
            FF_ESR = "undefined")
    public void connection() throws Exception {
        final String workerJs = "postMessage(navigator.connection);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US")
    public void language() throws Exception {
        final String workerJs = "postMessage(navigator.language);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("en-US,en")
    public void languages() throws Exception {
        final String workerJs = "postMessage(navigator.languages);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Win32")
    public void platform() throws Exception {
        final String workerJs = "postMessage(navigator.platform);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Gecko")
    public void product() throws Exception {
        final String workerJs = "postMessage(navigator.product);\n";

        expandExpectedAlertsVariables(URL_FIRST);
        testJs(workerJs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "Mozilla/5.0\\s(Windows\\sNT\\s10.0;\\sWin64;\\sx64)\\sAppleWebKit/537.36\\s"
                        + "(KHTML,\\slike\\sGecko)\\sChrome/134.0.0.0\\sSafari/537.36",
            EDGE = "Mozilla/5.0\\s(Windows\\sNT\\s10.0;\\sWin64;\\sx64)\\sAppleWebKit/537.36\\s"
                        + "(KHTML,\\slike\\sGecko)\\sChrome/134.0.0.0\\sSafari/537.36\\sEdg/134.0.0.0",
            FF = "Mozilla/5.0\\s(Windows\\sNT\\s10.0;\\sWin64;\\sx64;\\srv:136.0)\\sGecko/20100101\\sFirefox/136.0",
            FF_ESR = "Mozilla/5.0\\s(Windows\\sNT\\s10.0;\\sWin64;\\sx64;\\srv:128.0)\\sGecko/20100101\\sFirefox/128.0")
    public void userAgent() throws Exception {
        final String workerJs = "postMessage(navigator.userAgent);\n";

        expandExpectedAlertsVariables(URL_FIRST);
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
                + "  var l = new WorkerNavigator();\n"
                + "  postMessage(l);\n"
                + "} catch(e) {\n"
                + "  postMessage('exception');\n"
                + "}";
        testJs(workerJs);
    }

    private void testJs(final String workerJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script async>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "try {\n"
            + "  var myWorker = new Worker('worker.js#somehash');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    log(e.data);\n"
            + "  };\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"),
                workerJs, MimeType.TEXT_JAVASCRIPT);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
