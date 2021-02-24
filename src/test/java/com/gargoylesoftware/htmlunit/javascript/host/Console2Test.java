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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebConsole;
import com.gargoylesoftware.htmlunit.WebConsole.Logger;

/**
 * Tests for {@link Console}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Console2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: [\"one\", \"two\", \"three\", ({})]")
    public void log() throws Exception {
        log("['one', 'two', 'three', document.body.children]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: string: HtmlUnit; numb: 4, 4; float: 4.2, link: http://htmlunit.sourceforge.net/")
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
    @Alerts("info: %i; 1; %i; % 2.0 3.0 4.0")
    public void logEscaping() throws Exception {
        log("'%%i; %i; %%i; %', 1, 2, 3, 4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: 12%i3; 4.0")
    public void logContinous() throws Exception {
        log("'%i%i%%i%i;', 1, 2, 3, 4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("info: %x 1 10%  % ; 2.0")
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
        console.setLogger(new Logger() {

            @Override
            public void warn(final Object message) {
            }

            @Override
            public void trace(final Object message) {
            }

            @Override
            public void info(final Object message) {
                messages.add("info: " + message);
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
        });

        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  window.console.log(" + logInput + ");\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPage(html);
        assertEquals(getExpectedAlerts(), messages);
    }
}
