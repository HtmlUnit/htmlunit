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
package org.htmlunit;

import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.NotYetImplemented;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link WebWindowListener}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebWindowListenerTest extends SimpleWebTestCase {

    /**
     * Testcase for issue #1101.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void eventOrder() throws Exception {
        final String firstHtml
            = "<html>\n"
            + "<head><title>Test 1</title></head>\n"
            + "<body>\n"
            + "<script type='text/javascript'>\n"
            + "  document.location.href = '" + URL_SECOND + "';\n"
            + "</script>\n"
            + "<p>First Page<p>\n"
            + "</body></html>";

        final String secondHtml
            = "<html>\n"
            + "<head><title>Test 2</title></head>\n"
            + "<body><p>Second Page<p></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final LoggingWebWindowListener logger = new LoggingWebWindowListener();
        getWebClient().addWebWindowListener(logger);

        loadPage(firstHtml);
        assertEquals("changed '' - 'Test 1'; changed 'Test 1' - 'Test 2'; ", logger.getMsg());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void eventOrderCloseLast() throws Exception {
        final String firstHtml
            = "<html>\n"
            + "<head><title>Test 1</title></head>\n"
            + "<body>\n"
            + "</body></html>";

        final LoggingWebWindowListener logger = new LoggingWebWindowListener();
        getWebClient().addWebWindowListener(logger);

        loadPage(firstHtml);
        ((TopLevelWindow) getWebClient().getCurrentWindow()).close();

        assertEquals("changed '' - 'Test 1'; closed 'Test 1' - ''; opened '' - ''; ", logger.getMsg());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void eventOrderReset() throws Exception {
        final String firstHtml
            = "<html>\n"
            + "<head><title>Test 1</title></head>\n"
            + "<body>\n"
            + "</body></html>";

        final LoggingWebWindowListener logger = new LoggingWebWindowListener();
        getWebClient().addWebWindowListener(logger);

        loadPage(firstHtml);
        getWebClient().reset();

        assertEquals("changed '' - 'Test 1'; closed 'Test 1' - ''; opened '' - ''; ", logger.getMsg());
    }

    private final class LoggingWebWindowListener implements WebWindowListener {
        private final StringBuilder msg_ = new StringBuilder();

        @Override
        public void webWindowOpened(final WebWindowEvent event) {
            log("opened", event);
        }

        @Override
        public void webWindowContentChanged(final WebWindowEvent event) {
            log("changed", event);
        }
        @Override
        public void webWindowClosed(final WebWindowEvent event) {
            log("closed", event);
        }

        private void log(final String prefix, final WebWindowEvent event) {
            msg_.append(prefix).append(" '");
            Page page = event.getOldPage();
            if (page instanceof HtmlPage) {
                msg_.append(((HtmlPage) page).getTitleText());
            }
            msg_.append("' - '");
            page = event.getNewPage();
            if (page instanceof HtmlPage) {
                msg_.append(((HtmlPage) page).getTitleText());
            }
            msg_.append("'; ");
        }

        public String getMsg() {
            return msg_.toString();
        }
    }
}
