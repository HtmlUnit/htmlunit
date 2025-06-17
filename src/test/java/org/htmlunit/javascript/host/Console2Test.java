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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebConsole;
import org.htmlunit.WebConsole.Logger;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for Console.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class Console2Test extends SimpleWebTestCase {

    private final class LoggerMock implements Logger {
        private final List<String> messages_;

        private LoggerMock(final List<String> messages) {
            messages_ = messages;
        }

        @Override
        public void warn(final Object message) {
        }

        @Override
        public void trace(final Object message) {
        }

        @Override
        public void info(final Object message) {
            messages_.add("info: " + message);
        }

        @Override
        public void error(final Object message) {
        }

        @Override
        public void debug(final Object message) {
        }

        @Override
        public boolean isTraceEnabled() {
            return false;
        }

        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public boolean isInfoEnabled() {
            return true;
        }

        @Override
        public boolean isWarnEnabled() {
            return true;
        }

        @Override
        public boolean isErrorEnabled() {
            return true;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: [\"one\",\"two\",\"three\",{}]")
    public void log() throws Exception {
        log("['one', 'two', 'three', document.body.children]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: string: HtmlUnit; numb: 4, 4; float: 4.2, link: \"http://htmlunit.sourceforge.net/\"")
    public void logSimplePlaceholder() throws Exception {
        log("'string: %s; numb: %d, %i; float: %f, link: %o', 'HtmlUnit', 4.2, 4, 4.2,"
                + " 'http://htmlunit.sourceforge.net/'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: string: %s; %i;")
    public void logMissingParam() throws Exception {
        log("'string: %s; %i;'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: string: #; %i; %i;")
    public void logMissingParam2() throws Exception {
        log("'string: %s; %i; %i;', '#'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: string: param1; param2")
    public void logMissingPlaceholder() throws Exception {
        log("'string: %s;', 'param1', 'param2'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: %i; 1; %i; % 2 3 4")
    public void logEscaping() throws Exception {
        log("'%%i; %i; %%i; %', 1, 2, 3, 4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: 12%i3; 4")
    public void logContinous() throws Exception {
        log("'%i%i%%i%i;', 1, 2, 3, 4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: %x 1 10%  % ; 2")
    public void logPercent() throws Exception {
        log("'%x %i 10%  %% ;', 1, 2");
    }

    /**
     * Regression test for issue #1711.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: $Version$")
    public void logDollar() throws Exception {
        log("'%s', '$Version$'");
    }

    private void log(final String logInput) throws Exception {
        final WebConsole console = getWebClient().getWebConsole();
        final List<String> messages = new ArrayList<>();
        console.setLogger(new LoggerMock(messages));

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  window.console.log(" + logInput + ");\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPage(html);
        assertEquals(getExpectedAlerts(), messages);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: from worker")
    public void fromWorker() throws Exception {
        final WebConsole console = getWebClient().getWebConsole();
        final List<String> messages = new ArrayList<>();
        console.setLogger(new LoggerMock(messages));

        final String workerJs = "console.log('from worker');\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script async>\n"
                + "try {\n"
                + "  var myWorker = new Worker('worker.js');\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script></body></html>\n";

        loadPage(html);
        Thread.sleep(100);
        assertEquals(getExpectedAlerts(), messages);
    }
}
