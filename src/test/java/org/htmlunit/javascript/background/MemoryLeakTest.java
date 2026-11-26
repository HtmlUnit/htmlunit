/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.background;

import java.net.URL;
import java.util.List;

import org.htmlunit.BrowserVersion;
import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MemoryLeakDetector;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;

/**
 * <p>Tests for memory leaks: after {@link WebClient#close()} the client, its windows and its pages
 * have to be garbage collectable, whatever the page did (timers, requests, frames, workers, ...).</p>
 *
 * <p>Every test uses its own {@link WebClient} and {@link MockWebConnection}; the client is
 * used in a separate method, so no local variable of the test method references it.
 * The objects are tracked with weak references (see {@link MemoryLeakDetector}).</p>
 *
 * <p>A failing test should be verified with a profiler (path to the GC root of the page or the
 * window) before a fix is done; there is no guarantee that the JVM collects everything on request.</p>
 *
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class MemoryLeakTest extends SimpleWebTestCase {

    private static final String SAMPLE_PAGE = DOCTYPE_HTML + "<html><head><title>second</title></head></html>";

    /**
     * Configures the responses of the connection.
     */
    @FunctionalInterface
    private interface ConnectionSetup {
        /**
         * Configures the responses.
         * @param conn the connection
         * @throws Exception in case of error
         */
        void setup(MockWebConnection conn) throws Exception;
    }

    /**
     * Something done with the loaded page.
     */
    @FunctionalInterface
    private interface PageAction {
        /**
         * Does something with the page.
         * @param page the page
         * @throws Exception in case of error
         */
        void run(HtmlPage page) throws Exception;
    }

    /**
     * Loads the page with a new client, closes the client and checks that the client,
     * the window and the page can be garbage collected.
     *
     * @param html the content of the page
     * @param setup configures the responses of the other urls
     * @param pageHeaders the response headers of the page
     * @param action something done with the page after loading it
     * @throws Exception if the test fails
     */
    private void assertNoLeak(final String html, final ConnectionSetup setup,
            final List<NameValuePair> pageHeaders, final PageAction action) throws Exception {
        final MemoryLeakDetector detector = new MemoryLeakDetector();

        loadAndClose(detector, "", conn -> {
            conn.setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, pageHeaders);
            setup.setup(conn);
        }, action);

        assertTrue("WebClient can't be garbage collected.", detector.canBeGCed("client"));
        assertTrue("Window can't be garbage collected.", detector.canBeGCed("window"));
        assertTrue("Page can't be garbage collected.", detector.canBeGCed("page"));
    }

    private void assertNoLeak(final String html, final ConnectionSetup setup) throws Exception {
        assertNoLeak(html, setup, List.of(), page -> { });
    }

    private void assertNoLeak(final String html) throws Exception {
        assertNoLeak(html, conn -> { });
    }

    /**
     * Uses a new client and a new connection; nothing created here is referenced
     * after this method has returned (except by the weak references of the detector).
     */
    private static void loadAndClose(final MemoryLeakDetector detector, final String suffix,
            final ConnectionSetup setup, final PageAction action) throws Exception {
        final MockWebConnection conn = new MockWebConnection();
        setup.setup(conn);

        try (WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            client.setWebConnection(conn);
            client.getOptions().setThrowExceptionOnScriptError(false);

            final HtmlPage page = client.getPage(URL_FIRST);
            action.run(page);
            client.waitForBackgroundJavaScript(1_000);

            detector.register("client" + suffix, client);
            detector.register("window" + suffix, page.getEnclosingWindow());
            detector.register("page" + suffix, page);
        }
    }

    /**
     * A plain page.
     * @throws Exception if the test fails
     */
    @Test
    public void simplePage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><p id='p'>hello</p>\n"
            + "<script>document.getElementById('p').textContent = 'world';</script>\n"
            + "</body></html>";

        assertNoLeak(html);
    }

    /**
     * A running interval (see the old bugs 639 and 648).
     * @throws Exception if the test fails
     */
    @Test
    public void setInterval() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>setInterval(function() { document.title += 'x'; }, 50);</script>\n"
            + "</body></html>";

        assertNoLeak(html);
    }

    /**
     * A timeout that is still pending when the client is closed.
     * @throws Exception if the test fails
     */
    @Test
    public void pendingTimeout() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>setTimeout(function() { document.title = 'late'; }, 60000);</script>\n"
            + "</body></html>";

        assertNoLeak(html);
    }

    /**
     * An asynchronous XMLHttpRequest with handlers.
     * @throws Exception if the test fails
     */
    @Test
    public void xmlHttpRequest() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.onload = function() { document.title = xhr.responseText; };\n"
            + "  xhr.open('GET', 'foo.txt', true);\n"
            + "  xhr.send();\n"
            + "</script></body></html>";

        assertNoLeak(html, conn -> {
            conn.setResponse(new URL(URL_FIRST, "foo.txt"), "Hello", MimeType.TEXT_PLAIN);
        });
    }

    /**
     * fetch() with promise chains.
     * @throws Exception if the test fails
     */
    @Test
    public void fetch() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  fetch('foo.txt')\n"
            + "    .then(function(response) { return response.text(); })\n"
            + "    .then(function(text) { document.title = text; });\n"
            + "</script></body></html>";

        assertNoLeak(html, conn -> {
            conn.setResponse(new URL(URL_FIRST, "foo.txt"), "Hello", MimeType.TEXT_PLAIN);
        });
    }

    /**
     * A page with an iframe that runs a timer and accesses its parent.
     * @throws Exception if the test fails
     */
    @Test
    public void iframe() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><iframe src='frame.html'></iframe></body></html>";
        final String frame = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  setInterval(function() { parent.document.title += 'x'; }, 50);\n"
            + "</script></body></html>";

        assertNoLeak(html, conn -> {
            conn.setResponse(new URL(URL_FIRST, "frame.html"), frame);
        });
    }

    /**
     * A page that opens a popup window.
     * @throws Exception if the test fails
     */
    @Test
    public void windowOpen() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  var w = window.open('" + URL_SECOND + "', 'popup');\n"
            + "</script></body></html>";

        assertNoLeak(html, conn -> {
            conn.setResponse(URL_SECOND, SAMPLE_PAGE);
        });
    }

    /**
     * Event listeners with closures referencing the DOM.
     * @throws Exception if the test fails
     */
    @Test
    public void eventListeners() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>d</div><script>\n"
            + "  var d = document.getElementById('d');\n"
            + "  document.addEventListener('click', function() { d.textContent = 'clicked'; });\n"
            + "  window.addEventListener('resize', function() { d.textContent = document.title; });\n"
            + "  d.onclick = function() { window.lastTarget = d; };\n"
            + "  d.click();\n"
            + "</script></body></html>";

        assertNoLeak(html);
    }

    /**
     * A MutationObserver that is still observing.
     * @throws Exception if the test fails
     */
    @Test
    public void mutationObserver() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'></div><script>\n"
            + "  var d = document.getElementById('d');\n"
            + "  var observer = new MutationObserver(function(records) { document.title = records.length; });\n"
            + "  observer.observe(document.body, { childList: true, subtree: true });\n"
            + "  d.appendChild(document.createElement('span'));\n"
            + "</script></body></html>";

        assertNoLeak(html);
    }

    /**
     * A dedicated worker exchanging messages with the page.
     * @throws Exception if the test fails
     */
    @Test
    public void worker() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  var worker = new Worker('worker.js');\n"
            + "  worker.onmessage = function(e) { document.title = e.data; };\n"
            + "  worker.postMessage('ping');\n"
            + "</script></body></html>";
        final String workerJs = "onmessage = function(e) { postMessage(e.data + ' pong'); };";

        assertNoLeak(html, conn -> {
            conn.setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.TEXT_JAVASCRIPT);
        });
    }

    /**
     * A Content-Security-Policy violation reported by a securitypolicyviolation listener.
     * @throws Exception if the test fails
     */
    @Test
    public void contentSecurityPolicyViolation() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "    document.title = e.violatedDirective;\n"
            + "  });\n"
            + "</script>\n"
            + "<img src='img.png' onerror='document.title += \"error\"'>\n"
            + "</body></html>";

        assertNoLeak(html, conn -> {
            conn.setResponse(new URL(URL_FIRST, "img.png"), "", MimeType.IMAGE_PNG);
        }, List.of(new NameValuePair("Content-Security-Policy", "img-src 'none'")), page -> { });
    }

    /**
     * A Content-Security-Policy violation caused by the Java API (no script is running);
     * the pending violation must not keep the page reachable from the current thread.
     * @throws Exception if the test fails
     */
    @Test
    public void contentSecurityPolicyViolationFromJava() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "    document.title = e.violatedDirective;\n"
            + "  });\n"
            + "</script>\n"
            + "<img id='i' onerror='document.title += \"error\"'>\n"
            + "</body></html>";

        assertNoLeak(html, conn -> conn.setResponse(new URL(URL_FIRST, "img.png"), "", MimeType.IMAGE_PNG),
                List.of(new NameValuePair("Content-Security-Policy", "img-src 'none'")),
                page -> page.getHtmlElementById("i").setAttribute("src", "img.png"));
    }

    /**
     * A page loaded several times with new clients; none of the clients may be retained
     * (e.g. by a static cache or a thread local).
     * @throws Exception if the test fails
     */
    @Test
    public void repeatedClients() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><style>div { color: red; } @media (min-width: 100px) { p { color: blue; } }</style>\n"
            + "</head><body><div id='d' style='--custom-var: 1px'>d</div><script>\n"
            + "  var d = document.getElementById('d');\n"
            + "  d.style.setProperty('--other-var', '2px');\n"
            + "  document.title = getComputedStyle(d).color;\n"
            + "  setTimeout(function() { document.title += 'x'; }, 10);\n"
            + "</script></body></html>";

        final MemoryLeakDetector detector = new MemoryLeakDetector();
        final int count = 20;
        for (int i = 0; i < count; i++) {
            loadAndClose(detector, Integer.toString(i), conn -> conn.setResponse(URL_FIRST, html), page -> { });
        }

        for (int i = 0; i < count; i++) {
            assertTrue("WebClient " + i + " can't be garbage collected.", detector.canBeGCed("client" + i));
            assertTrue("Page " + i + " can't be garbage collected.", detector.canBeGCed("page" + i));
        }
    }

    /**
     * The thread of the event loop is stopped by {@link WebClient#close()}.
     * @throws Exception if the test fails
     */
    @Test
    public void eventLoopThreadStopped() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>setInterval(function() { document.title += 'x'; }, 10);</script>\n"
            + "</body></html>";

        final String threadName = loadAndCloseReturnThreadName(html);

        final long end = System.currentTimeMillis() + 5_000;
        while (isThreadAlive(threadName) && System.currentTimeMillis() < end) {
            Thread.sleep(50);
        }
        assertFalse("Thread '" + threadName + "' is still alive.", isThreadAlive(threadName));
    }

    /**
     * An endless loop in a timer is stopped by {@link WebClient#close()}; the close does not wait
     * for the timeout of the event loop thread (10s) and the thread does not keep the page reachable
     * (Thread.stop() is not supported by newer JVMs).
     * @throws Exception if the test fails
     */
    @Test
    public void endlessLoopInTimer() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "  setTimeout(function() { while (true) { document.title = 'x'; } }, 10);\n"
            + "</script></body></html>";

        final MemoryLeakDetector detector = new MemoryLeakDetector();
        final long start = System.currentTimeMillis();
        final String threadName = loadWaitForLoopAndClose(detector, html);
        final long duration = System.currentTimeMillis() - start;

        assertTrue("close() took " + duration + "ms", duration < 5_000);

        final long end = System.currentTimeMillis() + 5_000;
        while (isThreadAlive(threadName) && System.currentTimeMillis() < end) {
            Thread.sleep(50);
        }
        assertFalse("Thread '" + threadName + "' is still alive.", isThreadAlive(threadName));
        assertTrue("Page can't be garbage collected.", detector.canBeGCed("page"));
    }

    private static String loadWaitForLoopAndClose(final MemoryLeakDetector detector, final String html)
            throws Exception {
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);

        try (WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            client.setWebConnection(conn);
            final HtmlPage page = client.getPage(URL_FIRST);
            // give the timer the time to start the loop
            client.waitForBackgroundJavaScript(300);

            detector.register("page", page);
            return "JS executor for " + client;
        }
    }

    private static String loadAndCloseReturnThreadName(final String html) throws Exception {
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);

        try (WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
            client.setWebConnection(conn);
            client.getPage(URL_FIRST);
            client.waitForBackgroundJavaScript(100);

            // see DefaultJavaScriptExecutor.getThreadName()
            return "JS executor for " + client;
        }
    }

    private static boolean isThreadAlive(final String name) {
        for (final Thread thread : Thread.getAllStackTraces().keySet()) {
            if (name.equals(thread.getName()) && thread.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
