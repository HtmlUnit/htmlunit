/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.base.testing.EventCatcher;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * Tests for {@link TopLevelWindow}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class TopLevelWindowTest extends SimpleWebTestCase {

    /**
     * Tests closing the only open window.
     * @throws Exception if the test fails
     */
    @Test
    public void closeTheOnlyWindow() throws Exception {
        final WebClient webClient = getWebClient();
        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(webClient);

        final WebWindow windowToClose = webClient.getCurrentWindow();
        ((TopLevelWindow) windowToClose).close();

        final List<WebWindowEvent> expectedEvents = Arrays.asList(
            new WebWindowEvent(windowToClose, WebWindowEvent.CLOSE, null, null)
        );
        assertEquals(expectedEvents, eventCatcher.getEvents());

        // Since this was the only open window, a new window should have
        // been created when this one was closed. Verify this.
        assertNotNull(webClient.getCurrentWindow());
        assertNotSame(webClient.getCurrentWindow(), windowToClose);

        assertEquals(1, webClient.getWebWindows().size());
    }

    /**
     * Tests the use of a custom job manager.
     * @throws Exception if an error occurs
     */
    @Test
    public void useCustomJobManager() throws Exception {
        final MutableInt jobCount = new MutableInt(0);
        final JavaScriptJobManager mgr = new JavaScriptJobManager() {
            /** {@inheritDoc} */
            public int waitForJobsStartingBefore(final long delayMillis) {
                return jobCount.intValue();
            }
            /** {@inheritDoc} */
            public int waitForJobs(final long timeoutMillis) {
                return jobCount.intValue();
            }
            /** {@inheritDoc} */
            public void stopJob(final int id) {
                // Empty.
            }
            /** {@inheritDoc} */
            public void shutdown() {
                // Empty.
            }
            /** {@inheritDoc} */
            public void removeJob(final int id) {
                // Empty.
            }
            /** {@inheritDoc} */
            public void removeAllJobs() {
                // Empty.
            }
            /** {@inheritDoc} */
            public int getJobCount() {
                return jobCount.intValue();
            }
            /** {@inheritDoc} */
            public int addJob(final JavaScriptJob job, final Page page) {
                jobCount.increment();
                return jobCount.intValue();
            }
            /** {@inheritDoc} */
            public JavaScriptJob getEarliestJob() {
                return null;
            }
            /** {@inheritDoc} */
            public boolean runSingleJob(final JavaScriptJob job) {
                // Empty
                return false;
            }
            @Override
            public String jobStatusDump() {
                return null;
            }
        };

        final WebWindowListener listener = new WebWindowListener() {
            /** {@inheritDoc} */
            public void webWindowOpened(final WebWindowEvent event) {
                ((WebWindowImpl) event.getWebWindow()).setJobManager(mgr);
            }
            /** {@inheritDoc} */
            public void webWindowContentChanged(final WebWindowEvent event) {
                // Empty.
            }
            /** {@inheritDoc} */
            public void webWindowClosed(final WebWindowEvent event) {
                // Empty.
            }
        };

        final WebClient client = getWebClientWithMockWebConnection();
        client.addWebWindowListener(listener);

        final TopLevelWindow window = (TopLevelWindow) client.getCurrentWindow();
        window.setJobManager(mgr);

        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<html><body><script>window.setTimeout('', 500);</script></body></html>");

        client.getPage(URL_FIRST);
        assertEquals(1, jobCount.intValue());

        client.getPage(URL_FIRST);
        assertEquals(2, jobCount.intValue());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void history() throws Exception {
        final WebClient client = getWebClientWithMockWebConnection();
        final TopLevelWindow window = (TopLevelWindow) client.getCurrentWindow();
        final History history = window.getHistory();

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, "<html><body><a name='a' href='" + URL_SECOND + "'>foo</a>"
            + "<a name='b' href='#b'>bar</a></body></html>");
        conn.setResponse(URL_SECOND, "<html><body><a name='a' href='" + URL_THIRD + "'>foo</a></body></html>");
        conn.setResponse(URL_THIRD, "<html><body><a name='a' href='" + URL_FIRST + "'>foo</a></body></html>");

        assertEquals(0, history.getLength());
        assertEquals(-1, history.getIndex());

        // Load the first page.
        HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(1, history.getLength());
        assertEquals(0, history.getIndex());
        assertNull(history.getUrl(-1));
        assertEquals(URL_FIRST, history.getUrl(0));
        assertEquals(URL_FIRST, window.getEnclosedPage().getUrl());
        assertNull(history.getUrl(1));

        // Go to the second page.
        page = page.getAnchorByName("a").click();
        assertEquals(2, history.getLength());
        assertEquals(1, history.getIndex());
        assertNull(history.getUrl(-1));
        assertEquals(URL_FIRST, history.getUrl(0));
        assertEquals(URL_SECOND, history.getUrl(1));
        assertEquals(URL_SECOND, window.getEnclosedPage().getUrl());
        assertNull(history.getUrl(2));

        // Go to the third page.
        page = page.getAnchorByName("a").click();
        assertEquals(3, history.getLength());
        assertEquals(2, history.getIndex());
        assertNull(history.getUrl(-1));
        assertEquals(URL_FIRST, history.getUrl(0));
        assertEquals(URL_SECOND, history.getUrl(1));
        assertEquals(URL_THIRD, history.getUrl(2));
        assertEquals(URL_THIRD, window.getEnclosedPage().getUrl());
        assertNull(history.getUrl(3));

        // Cycle around back to the first page.
        page = page.getAnchorByName("a").click();
        assertEquals(4, history.getLength());
        assertEquals(3, history.getIndex());
        assertNull(history.getUrl(-1));
        assertEquals(URL_FIRST, history.getUrl(0));
        assertEquals(URL_SECOND, history.getUrl(1));
        assertEquals(URL_THIRD, history.getUrl(2));
        assertEquals(URL_FIRST, history.getUrl(3));
        assertEquals(URL_FIRST, window.getEnclosedPage().getUrl());
        assertNull(history.getUrl(4));

        // Go to a hash on the current page.
        final URL firstUrlWithHash = new URL(URL_FIRST, "#b");
        page = page.getAnchorByName("b").click();
        assertEquals(5, history.getLength());
        assertEquals(4, history.getIndex());
        assertNull(history.getUrl(-1));
        assertEquals(URL_FIRST, history.getUrl(0));
        assertEquals(URL_SECOND, history.getUrl(1));
        assertEquals(URL_THIRD, history.getUrl(2));
        assertEquals(URL_FIRST, history.getUrl(3));
        assertEquals(firstUrlWithHash, history.getUrl(4));
        assertEquals(firstUrlWithHash, window.getEnclosedPage().getUrl());
        assertNull(history.getUrl(5));

        history.back().back();
        assertEquals(5, history.getLength());
        assertEquals(2, history.getIndex());
        assertNull(history.getUrl(-1));
        assertEquals(URL_FIRST, history.getUrl(0));
        assertEquals(URL_SECOND, history.getUrl(1));
        assertEquals(URL_THIRD, history.getUrl(2));
        assertEquals(URL_FIRST, history.getUrl(3));
        assertEquals(firstUrlWithHash, history.getUrl(4));
        assertEquals(URL_THIRD, window.getEnclosedPage().getUrl());
        assertNull(history.getUrl(5));

        history.forward();
        assertEquals(5, history.getLength());
        assertEquals(3, history.getIndex());
        assertNull(history.getUrl(-1));
        assertEquals(URL_FIRST, history.getUrl(0));
        assertEquals(URL_SECOND, history.getUrl(1));
        assertEquals(URL_THIRD, history.getUrl(2));
        assertEquals(URL_FIRST, history.getUrl(3));
        assertEquals(firstUrlWithHash, history.getUrl(4));
        assertEquals(URL_FIRST, window.getEnclosedPage().getUrl());
        assertNull(history.getUrl(5));
    }

    /**
     * Regression test for bug 2808520: onbeforeunload not called when window.close() is called.
     * @throws Exception if an error occurs
     */
    @Test
    public void onBeforeUnloadCalledOnClose() throws Exception {
        final String html = "<html><body onbeforeunload='alert(7)'>abc</body></html>";
        final List<String> alerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, alerts);
        assertTrue(alerts.isEmpty());
        final TopLevelWindow w = (TopLevelWindow) page.getEnclosingWindow();
        w.close();
        assertEquals(Arrays.asList("7"), alerts);
    }

    /**
     * Test that no JavaScript error is thrown when using the setTimeout() JS function
     * while the page is unloading.
     * Regression test for bug 2956550.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("closing")
    public void setTimeoutDuringOnUnload() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "function f() {\n"
            + "  alert('closing');\n"
            + "  setTimeout(function(){ alert('started in onunload'); }, 0);\n"
            + "}\n"
            + "if (window.addEventListener)\n"
            + "  window.addEventListener('unload', f, true);\n"
            + "else\n"
            + "  attachEvent('onunload', f);\n"
            + "</script></head>\n"
            + "<body></body></html>";
        final List<String> alerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, alerts);
        assertTrue(alerts.isEmpty());
        final TopLevelWindow w = (TopLevelWindow) page.getEnclosingWindow();
        w.close();
        getWebClient().waitForBackgroundJavaScript(1000);
        assertEquals(getExpectedAlerts(), alerts);
    }

}
