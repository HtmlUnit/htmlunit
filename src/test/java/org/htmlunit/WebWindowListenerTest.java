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

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
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
    @Alerts("changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; "
            + "changed 'Test 1 [org.htmlunit.TopLevelWindow]' - 'Test 2 [org.htmlunit.TopLevelWindow]'; ")
    @HtmlUnitNYI(CHROME = "changed 'Test 1 [org.htmlunit.TopLevelWindow]' - 'Test 2 [org.htmlunit.TopLevelWindow]'; "
                    + "changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; ",
            EDGE = "changed 'Test 1 [org.htmlunit.TopLevelWindow]' - 'Test 2 [org.htmlunit.TopLevelWindow]'; "
                    + "changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; ",
            FF = "changed 'Test 1 [org.htmlunit.TopLevelWindow]' - 'Test 2 [org.htmlunit.TopLevelWindow]'; "
                    + "changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; ",
            FF_ESR = "changed 'Test 1 [org.htmlunit.TopLevelWindow]' - 'Test 2 [org.htmlunit.TopLevelWindow]'; "
                    + "changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; ")
    public void eventOrder() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>Test 1</title></head>\n"
            + "<body>\n"
            + "<script type='text/javascript'>\n"
            + "  document.location.href = '" + URL_SECOND + "';\n"
            + "</script>\n"
            + "<p>First Page<p>\n"
            + "</body></html>";

        final String secondHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>Test 2</title></head>\n"
            + "<body><p>Second Page<p></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final LoggingWebWindowListener logger = new LoggingWebWindowListener();
        getWebClient().addWebWindowListener(logger);

        loadPage(firstHtml);
        assertEquals(getExpectedAlerts()[0], logger.getMsg());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; "
            + "closed 'Test 1 [org.htmlunit.TopLevelWindow]' - '(null)'; "
            + "opened '(null)' - '(null)'; ")
    public void eventOrderCloseLast() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>Test 1</title></head>\n"
            + "<body>\n"
            + "</body></html>";

        final LoggingWebWindowListener logger = new LoggingWebWindowListener();
        getWebClient().addWebWindowListener(logger);

        loadPage(firstHtml);
        ((TopLevelWindow) getWebClient().getCurrentWindow()).close();

        assertEquals(getExpectedAlerts()[0], logger.getMsg());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; "
            + "closed 'Test 1 [org.htmlunit.TopLevelWindow]' - '(null)'; "
            + "opened '(null)' - '(null)'; ")
    public void eventOrderReset() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>Test 1</title></head>\n"
            + "<body>\n"
            + "</body></html>";

        final LoggingWebWindowListener logger = new LoggingWebWindowListener();
        getWebClient().addWebWindowListener(logger);

        loadPage(firstHtml);
        getWebClient().reset();

        assertEquals(getExpectedAlerts()[0], logger.getMsg());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; "
            + "closed 'Test 1 [org.htmlunit.TopLevelWindow]' - '(null)'; ")
    public void eventOrderCloseWebClient() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>Test 1</title></head>\n"
            + "<body>\n"
            + "</body></html>";

        final LoggingWebWindowListener logger = new LoggingWebWindowListener();
        getWebClient().addWebWindowListener(logger);

        loadPage(firstHtml);
        getWebClient().close();

        assertEquals(getExpectedAlerts()[0], logger.getMsg());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("opened '(null)' - '(null)'; "
            + "changed '(null)' - 'about:blank [org.htmlunit.html.FrameWindow]'; "
            + "changed '(null)' - 'Test 1 [org.htmlunit.TopLevelWindow]'; "
            + "changed 'about:blank [org.htmlunit.html.FrameWindow]' - 'iFrame [org.htmlunit.html.FrameWindow]'; "
            + "closed 'iFrame [org.htmlunit.html.FrameWindow]' - '(null)'; "
            + "closed 'Test 1 [org.htmlunit.TopLevelWindow]' - '(null)'; ")
    public void eventOrderIFrameCloseWebClient() throws Exception {
        final String firstHtml = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>Test 1</title></head>\n"
                + "<body>\n"
                + "<iframe src='" + URL_SECOND + "'></iframe>\n"
                + "</body></html>";

        final String iframeContent = DOCTYPE_HTML
                + "<html>\n"
                + "<head><title>iFrame</title></head>\n"
                + "<body>iframe</body>\n"
                + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, iframeContent);

        final LoggingWebWindowListener logger = new LoggingWebWindowListener();
        getWebClient().addWebWindowListener(logger);

        loadPage(firstHtml);
        getWebClient().close();

        assertEquals(getExpectedAlerts()[0], logger.getMsg());
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
                final HtmlPage htmlPage = (HtmlPage) page;
                if (StringUtils.isNotBlank(htmlPage.getTitleText())) {
                    msg_.append(htmlPage.getTitleText());
                }
                else {
                    msg_.append(htmlPage.getUrl());
                }
                msg_.append(" ");
            }
            if (page != null) {
                msg_.append("[")
                    .append(page.getEnclosingWindow().getClass().getName())
                    .append("]");
            }
            else {
                msg_.append("(null)");
            }

            msg_.append("' - '");
            page = event.getNewPage();
            if (page instanceof HtmlPage) {
                final HtmlPage htmlPage = (HtmlPage) page;
                if (StringUtils.isNotBlank(htmlPage.getTitleText())) {
                    msg_.append(htmlPage.getTitleText());
                }
                else {
                    msg_.append(htmlPage.getUrl());
                }
                msg_.append(" ");
            }
            if (page != null) {
                msg_.append("[")
                    .append(page.getEnclosingWindow().getClass().getName())
                    .append("]");
            }
            else {
                msg_.append("(null)");
            }

            msg_.append("'; ");
        }

        public String getMsg() {
            return msg_.toString();
        }
    }
}
