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
package org.htmlunit.javascript.host;

import static java.nio.charset.StandardCharsets.UTF_16LE;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.Session.Listener.AutoDemanding;
import org.htmlunit.HttpHeader;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebServerTestCase.SSLVariant;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.JettyServerUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jakarta.servlet.Servlet;

/**
 * Tests for {@link WebSocket}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Madis Pärn
 */
public class WebSocketTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§", "", "blob"})
    public void initialNoServerAvailable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var location = 'ws://localhost:" + PORT2 + "/';\n"
            + "    var ws = new WebSocket(location);\n"
            + "    log(ws.url);\n"
            + "    log(ws.protocol);\n"
            // this makes our test instable because the real connect is
            // done by an executor and maybe already finished
            // + "    log(ws.readyState);\n"
            + "    log(ws.binaryType);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables("ws://localhost:" + PORT2 + "/");
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object WebSocket]", "§§URL§§"})
    public void earlyConstruction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var location = 'ws://localhost:" + PORT + "/';\n"
            + "    var ws = new WebSocket(location);\n"
            + "    log(ws);\n"
            + "    log(ws.url);\n"
            + "  }\n"
            + "  test();\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        expandExpectedAlertsVariables("ws://localhost:" + PORT + "/");
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"TypeError", "ws://localhost:§§URL§§/undefined", "ws://localhost:§§URL§§/null",
                       "ws://localhost:§§URL§§/", "ws://localhost:§§URL§§/"},
            FF_ESR = {"TypeError", "ws://localhost:§§URL§§/undefined", "ws://localhost:§§URL§§/null",
                      "exception empty", "ws://localhost:§§URL§§/"})
    @HtmlUnitNYI(
            FF_ESR = {"TypeError", "ws://localhost:§§URL§§/undefined", "ws://localhost:§§URL§§/null",
                      "ws://localhost:§§URL§§/", "ws://localhost:§§URL§§/"})
    public void initialWithoutUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      let ws = new WebSocket();\n"
            + "      log(ws.url);"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      let ws = new WebSocket(undefined);\n"
            + "      log(ws.url);"
            + "    } catch(e) { log('exception undefined') }\n"

            + "    try {\n"
            + "      let ws = new WebSocket(null);\n"
            + "      log(ws.url);"
            + "    } catch(e) { log('exception null') }\n"

            + "    try {\n"
            + "      let ws = new WebSocket('');\n"
            + "      log(ws.url);"
            + "    } catch(e) { log('exception empty') }\n"

            + "    try {\n"
            + "      let ws = new WebSocket(' ');\n"
            + "      log(ws.url);"
            + "    } catch(e) { log('exception blank') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(""+ PORT);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SyntaxError/DOMException", "SyntaxError/DOMException", "ws://localhost:§§URL§§/",
             "wss://localhost:§§URL§§/", "SyntaxError/DOMException", "SyntaxError/DOMException",
             "SyntaxError/DOMException"})
    @HtmlUnitNYI(
            CHROME = {"SyntaxError/DOMException", "SyntaxError/DOMException", "ws://localhost:§§URL§§",
                      "wss://localhost:§§URL§§", "SyntaxError/DOMException", "SyntaxError/DOMException",
                      "SyntaxError/DOMException"},
            EDGE = {"SyntaxError/DOMException", "SyntaxError/DOMException", "ws://localhost:§§URL§§",
                    "wss://localhost:§§URL§§", "SyntaxError/DOMException", "SyntaxError/DOMException",
                    "SyntaxError/DOMException"},
            FF = {"SyntaxError/DOMException", "SyntaxError/DOMException", "ws://localhost:§§URL§§",
                  "wss://localhost:§§URL§§", "SyntaxError/DOMException", "SyntaxError/DOMException",
                  "SyntaxError/DOMException"},
            FF_ESR = {"SyntaxError/DOMException", "SyntaxError/DOMException", "ws://localhost:§§URL§§",
                      "wss://localhost:§§URL§§", "SyntaxError/DOMException", "SyntaxError/DOMException",
                      "SyntaxError/DOMException"})
    public void invalidUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      let ws = new WebSocket('#');\n"
            + "      log(ws.url);"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      let ws = new WebSocket('javascript:alert(1)');\n"
            + "      log(ws.url);"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      let ws = new WebSocket('http://localhost:" + PORT + "');\n"
            + "      log(ws.url);"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      let ws = new WebSocket('https://localhost:" + PORT + "');\n"
            + "      log(ws.url);"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      let ws = new WebSocket('ftp://localhost:" + PORT + "');\n"
            + "      log(ws.url);"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      let ws = new WebSocket('ftps://localhost:" + PORT + "');\n"
            + "      log(ws.url);"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      let ws = new WebSocket('test://localhost:" + PORT + "');\n"
            + "      log(ws.url);"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(""+ PORT);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test that a wss:// URL is NOT silently downgraded to ws://.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("wss://localhost:§§URL§§/")
    public void urlWssSchemePreserved() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      let ws = new WebSocket('wss://localhost:" + PORT + "/');\n"
            + "      log(ws.url);\n"
            + "    } catch(e) { log('exception: ' + e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(""+ PORT);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test that connect errors fire the onerror and onclose events in JS.
     * When a WebSocket fails to connect, the spec requires an error event
     * followed by a close event with code 1006 and wasClean=false.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onError", "onClose code: 1006 wasClean: false"})
    public void connectErrorFiresEvents() throws Exception {
        stopWebServers();

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var ws = new WebSocket('ws://localhost:" + PORT + "/');\n"
            + "    ws.onerror = function(e) {\n"
            + "      log('onError');\n"
            + "    };\n"
            + "    ws.onclose = function(e) {\n"
            + "      log('onClose code: ' + e.code + ' wasClean: ' + e.wasClean);\n"
            + "    };\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Same as connectErrorFiresEvents but using addEventListener instead of on* handlers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onErrorListener", "onCloseListener code: 1006 wasClean: false"})
    public void connectErrorFiresEventsListener() throws Exception {
        stopWebServers();

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var ws = new WebSocket('ws://localhost:" + PORT + "/');\n"
            + "    ws.addEventListener('error', function(e) {\n"
            + "      log('onErrorListener');\n"
            + "    });\n"
            + "    ws.addEventListener('close', function(e) {\n"
            + "      log('onCloseListener code: ' + e.code + ' wasClean: ' + e.wasClean);\n"
            + "    });\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Test that readyState is set to CLOSED after a connect error.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "3"})
    public void connectErrorReadyState() throws Exception {
        stopWebServers();

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var ws = new WebSocket('ws://localhost:" + PORT + "/');\n"
            + "    log(ws.readyState);\n"
            + "    ws.onclose = function(e) {\n"
            + "      log(ws.readyState);\n"
            + "    };\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"blob", "blob", "arraybuffer", "blob", "blob"})
    public void binaryType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var location = 'ws://localhost:" + PORT + "/';\n"
            + "    var ws = new WebSocket(location);\n"
            + "    log(ws.binaryType);\n"

            + "    try {\n"
            + "      ws.binaryType = 'abc';\n"
            + "      log(ws.binaryType);\n"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      ws.binaryType = 'arraybuffer';\n"
            + "      log(ws.binaryType);\n"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      ws.binaryType = 'blob';\n"
            + "      log(ws.binaryType);\n"
            + "    } catch(e) { logEx(e) }\n"

            + "    try {\n"
            + "      ws.binaryType = '';\n"
            + "      log(ws.binaryType);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test case taken from <a href="http://angelozerr.wordpress.com/2011/07/23/websockets_jetty_step1/">here</a>.
     * @throws Exception if the test fails
     */
    @Test
    public void chat() throws Exception {
        final String firstResponse = "Browser: has joined!";
        final String secondResponse = "Browser: Hope you are fine!";

        stopWebServers();

        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", ChatWebSocketListener.class);
        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", null, socketListeners, null, false, SSLVariant.NONE);
        try {
            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "WebSocketTest_chat.html");

            driver.findElement(By.id("username")).sendKeys("Browser");
            driver.findElement(By.id("joinB")).click();

            assertVisible("joined", driver);

            final WebElement chatE = driver.findElement(By.id("chat"));
            long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();

            do {
                Thread.sleep(100);
            }
            while (chatE.getText().length() <= firstResponse.length() && System.currentTimeMillis() < maxWait);

            assertEquals(firstResponse, chatE.getText());

            driver.findElement(By.id("phrase")).sendKeys("Hope you are fine!");
            driver.findElement(By.id("sendB")).click();

            maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();
            do {
                Thread.sleep(100);
            }
            while (!chatE.getText().contains(secondResponse) && System.currentTimeMillis() < maxWait);

            assertEquals(firstResponse + "\n" + secondResponse, chatE.getText());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    public static class ChatWebSocketListener implements AutoDemanding {
        private static final Set<ChatWebSocketListener> webSockets_ = new CopyOnWriteArraySet<>();
        private Session session_;

        @Override
        public void onWebSocketOpen(Session session) {
            session_ = session;
            webSockets_.add(this);
        }

        @Override
        public void onWebSocketText(final String data) {
            for (final ChatWebSocketListener webSocket : webSockets_) {
                webSocket.session_.sendText(data, Callback.NOOP);
            }
        }

        @Override
        public void onWebSocketClose(final int closeCode, final String message, Callback callback) {
            webSockets_.remove(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @AfterEach
    @Override
    public void releaseResources() {
        super.releaseResources();

        for (final Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getName().contains("WebSocket")) {
                try {
                    // ok found one but let's wait a bit to start a second check before
                    // pressing the panic button
                    Thread.sleep(400);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        String lastFailing = null;
        for (final java.util.Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            final Thread thread = entry.getKey();
            if (thread.getName().contains("WebSocket")) {
                lastFailing = thread.getName();
                System.err.println();
                System.err.println("WebSocket thread named '" + lastFailing + "' still running");
                final StackTraceElement[] traces = entry.getValue();
                for (StackTraceElement trace : traces) {
                    System.err.println(trace);
                }
            }
        }

        assertNull("WebSocket thread named '" + lastFailing + "' still running", lastFailing);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({": myname=My value!1", ": myname=My value!2"})
    public void cookies() throws Exception {
        final String[] expected = getExpectedAlerts();

        stopWebServers();

        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", CookiesWebSocketListener.class);
        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", null, socketListeners, null, false, SSLVariant.NONE);

        try {
            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "WebSocketTest_cookies.html");

            driver.findElement(By.id("username")).sendKeys("Browser");
            driver.findElement(By.id("joinB")).click();
            final WebElement chatE = driver.findElement(By.id("chat"));

            long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();
            do {
                Thread.sleep(100);
            }
            while (chatE.getText().length() <= expected[0].length() && System.currentTimeMillis() < maxWait);

            assertEquals(expected[0], chatE.getText());

            driver.findElement(By.id("phrase")).sendKeys("Hope you are fine!");
            driver.findElement(By.id("sendB")).click();

            maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();
            do {
                Thread.sleep(100);
            }
            while (!chatE.getText().contains(expected[1]) && System.currentTimeMillis() < maxWait);

            assertEquals(expected[0] + "\n" + expected[1], chatE.getText());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    public static class CookiesWebSocketListener implements AutoDemanding {
        private static final Set<CookiesWebSocketListener> webSockets_ = new CopyOnWriteArraySet<>();
        private Session session_;
        private int counter_ = 1;

        @Override
        public void onWebSocketOpen(Session session) {
            session_ = session;
            webSockets_.add(this);
        }

        @Override
        public void onWebSocketText(final String data) {
            final String cookie = session_
                                    .getUpgradeRequest()
                                    .getHeaders()
                                    .get(HttpHeader.COOKIE)
                                    .get(0) + counter_++;

            for (final CookiesWebSocketListener webSocket : webSockets_) {
                webSocket.session_.sendText(cookie, Callback.NOOP);
            }
        }

        @Override
        public void onWebSocketClose(final int closeCode, final String message, Callback callback) {
            webSockets_.remove(this);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onOpenListener",
             "onOpen", "open", "[object WebSocket]", "[object WebSocket]",
             "undefined", "undefined", "undefined", "undefined",
             "onMessageTextListener", "message", "[object WebSocket]", "[object WebSocket]",
             "server_text", "§§URL§§", "", "null",
             "onMessageText", "message", "[object WebSocket]", "[object WebSocket]",
             "server_text", "§§URL§§", "", "null",
             "onMessageBinaryListener", "message", "[object WebSocket]", "[object WebSocket]",
             "[object ArrayBuffer]", "§§URL§§", "", "null",
             "onMessageBinary", "message", "[object WebSocket]", "[object WebSocket]",
             "[object ArrayBuffer]", "§§URL§§", "", "null",
             "onCloseListener code: 1000",
             "onClose code: 1000"})
    public void events() throws Exception {
        expandExpectedAlertsVariables("ws://localhost:" + PORT);
        final String expected = String.join("\n", getExpectedAlerts());

        stopWebServers();

        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", EventsWebSocketListener.class);
        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", null, socketListeners, null, false, SSLVariant.NONE);

        try {
            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "WebSocketTest_events.html");

            final WebElement logElement = driver.findElement(By.id("log"));
            final long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();

            String text;
            do {
                Thread.sleep(100);

                text = logElement.getDomProperty("value").trim().replaceAll("\r", "");
            }
            while (text.length() <= expected.length() && System.currentTimeMillis() < maxWait);

            assertEquals(expected, text);
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"onOpenListener",
                       "onOpen", "open", "[object WebSocket]", "[object WebSocket]",
                       "undefined", "undefined", "undefined", "undefined",
                       "onMessageTextListener", "message", "[object WebSocket]", "[object WebSocket]",
                       "server_text", "§§URL§§", "", "null",
                       "onMessageText", "message", "[object WebSocket]", "[object WebSocket]",
                       "server_text", "§§URL§§", "", "null",
                       "onMessageBinaryListener", "message", "[object WebSocket]", "[object WebSocket]",
                       "[object ArrayBuffer]", "§§URL§§", "", "null",
                       "onMessageBinary", "message", "[object WebSocket]", "[object WebSocket]",
                       "[object ArrayBuffer]", "§§URL§§", "", "null",
                       "onCloseListener code: 1000  wasClean: true",
                       "onClose code: 1000  wasClean: true"},
            FF = {"onOpenListener",
                  "onOpen", "open", "[object WebSocket]", "[object WebSocket]",
                  "undefined", "undefined", "undefined", "undefined",
                  "onMessageTextListener", "message", "[object WebSocket]", "[object WebSocket]",
                  "server_text", "§§URL§§", "", "null",
                  "onMessageText", "message", "[object WebSocket]", "[object WebSocket]",
                  "server_text", "§§URL§§", "", "null",
                  "onMessageBinaryListener", "message", "[object WebSocket]", "[object WebSocket]",
                  "[object ArrayBuffer]", "§§URL§§", "", "null",
                  "onMessageBinary", "message", "[object WebSocket]", "[object WebSocket]",
                  "[object ArrayBuffer]", "§§URL§§", "", "null",
                  "onCloseListener code: 1000  wasClean: false",
                  "onClose code: 1000  wasClean: false"},
            FF_ESR = {"onOpenListener",
                      "onOpen", "open", "[object WebSocket]", "[object WebSocket]",
                      "undefined", "undefined", "undefined", "undefined",
                      "onMessageTextListener", "message", "[object WebSocket]", "[object WebSocket]",
                      "server_text", "§§URL§§", "", "null",
                      "onMessageText", "message", "[object WebSocket]", "[object WebSocket]",
                      "server_text", "§§URL§§", "", "null",
                      "onMessageBinaryListener", "message", "[object WebSocket]", "[object WebSocket]",
                      "[object ArrayBuffer]", "§§URL§§", "", "null",
                      "onMessageBinary", "message", "[object WebSocket]", "[object WebSocket]",
                      "[object ArrayBuffer]", "§§URL§§", "", "null",
                      "onCloseListener code: 1000  wasClean: false",
                      "onClose code: 1000  wasClean: false"})
    @HtmlUnitNYI(FF = {"onOpenListener",
                       "onOpen", "open", "[object WebSocket]", "[object WebSocket]",
                       "undefined", "undefined", "undefined", "undefined",
                       "onMessageTextListener", "message", "[object WebSocket]", "[object WebSocket]",
                       "server_text", "§§URL§§", "", "null",
                       "onMessageText", "message", "[object WebSocket]", "[object WebSocket]",
                       "server_text", "§§URL§§", "", "null",
                       "onMessageBinaryListener", "message", "[object WebSocket]", "[object WebSocket]",
                       "[object ArrayBuffer]", "§§URL§§", "", "null",
                       "onMessageBinary", "message", "[object WebSocket]", "[object WebSocket]",
                       "[object ArrayBuffer]", "§§URL§§", "", "null",
                       "onCloseListener code: 1000  wasClean: true",
                       "onClose code: 1000  wasClean: true"},
            FF_ESR = {"onOpenListener",
                      "onOpen", "open", "[object WebSocket]", "[object WebSocket]",
                      "undefined", "undefined", "undefined", "undefined",
                      "onMessageTextListener", "message", "[object WebSocket]", "[object WebSocket]",
                      "server_text", "§§URL§§", "", "null",
                      "onMessageText", "message", "[object WebSocket]", "[object WebSocket]",
                      "server_text", "§§URL§§", "", "null",
                      "onMessageBinaryListener", "message", "[object WebSocket]", "[object WebSocket]",
                      "[object ArrayBuffer]", "§§URL§§", "", "null",
                      "onMessageBinary", "message", "[object WebSocket]", "[object WebSocket]",
                      "[object ArrayBuffer]", "§§URL§§", "", "null",
                      "onCloseListener code: 1000  wasClean: true",
                      "onClose code: 1000  wasClean: true"})
    public void wasClean() throws Exception {
        expandExpectedAlertsVariables("ws://localhost:" + PORT);
        final String expected = String.join("\n", getExpectedAlerts());

        stopWebServers();

        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", EventsWebSocketListener.class);
        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", null, socketListeners, null, false, SSLVariant.NONE);

        try {
            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "WebSocketTest_wasClean.html");

            final WebElement logElement = driver.findElement(By.id("log"));
            final long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();

            String text;
            do {
                Thread.sleep(100);

                text = logElement.getDomProperty("value").trim().replaceAll("\r", "");
            }
            while (text.length() <= expected.length() && System.currentTimeMillis() < maxWait);

            assertEquals(expected, text);
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onError[object Event]",
             "onCloseListener code: 1006  wasClean: false",
             "onClose code: 1006  wasClean: false"})
    public void eventsNoSocketServer() throws Exception {
        stopWebServers();

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", null, null, null, false, SSLVariant.NONE);

        try {
            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "WebSocketTest_wasClean.html");

            final WebElement logElement = driver.findElement(By.id("log"));
            int counter = 0;
            String text;
            do {
                Thread.sleep(DEFAULT_WAIT_TIME.toMillis());

                text = logElement.getDomProperty("value").trim().replaceAll("\r", "");
            }
            while (!text.isEmpty() && counter++ < 10);

            assertEquals(String.join("\n", getExpectedAlerts()), text);
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    private static void assertVisible(final String domId, final WebDriver driver) throws Exception {
        final WebElement domE = driver.findElement(By.id(domId));
        int counter = 0;
        do {
            Thread.sleep(100);
        }
        while (!domE.isDisplayed() && counter++ < 10);

        assertEquals("Node should be visible, domId: " + domId, true, domE.isDisplayed());
    }

    public static class EventsWebSocketListener implements AutoDemanding {
        private Session session_;

        @Override
        public void onWebSocketOpen(Session session) {
            session_ = session;
        }

        @Override
        public void onWebSocketText(final String data) {
            if ("text".equals(data)) {
                session_.sendText("server_text", Callback.NOOP);
            }
            else if ("close".equals(data)) {
                session_.close();
            }
            else {
                throw new IllegalArgumentException("Unknown request: " + data);
            }
        }

        @Override
        public void onWebSocketBinary(ByteBuffer payload, Callback callback) {
            try {
                // Extract bytes from ByteBuffer
                final byte[] bytes = new byte[payload.remaining()];
                payload.get(bytes);
                final String data = new String(bytes, UTF_16LE);

                if ("binary".equals(data)) {
                    final ByteBuffer response = ByteBuffer.wrap("server_binary".getBytes(UTF_16LE));

                    // Send binary response using new API
                    session_.sendBinary(response,
                                        Callback.from(() -> callback.succeed(), error -> callback.fail(error)));
                }
                else {
                    callback.fail(new IllegalArgumentException("Unknown request: " + data));
                }
            }
            catch (Exception e) {
                callback.fail(e);
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void prototypeUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var u = WebSocket.prototype.url;\n"
            + "      log(u);\n"
            + "    } catch(e) { log(e instanceof TypeError) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

/**
     * Verifies the default value of binaryType and validation behavior when assigning invalid values.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"blob", "blob", "arraybuffer", "arraybuffer", "arraybuffer", "arraybuffer", "blob"})
    public void binaryTypeDefaultAndValidation() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", BinaryWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    log(ws.binaryType);\n" // Default: 'blob'

                // Invalid string -> must be ignored
                + "    ws.binaryType = 'invalid_type';\n"
                + "    log(ws.binaryType);\n"

                // Valid setting
                + "    ws.binaryType = 'arraybuffer';\n"
                + "    log(ws.binaryType);\n"

                // Empty string -> ignored
                + "    ws.binaryType = '';\n"
                + "    log(ws.binaryType);\n"

                // null / undefined -> ignored
                + "    ws.binaryType = null;\n"
                + "    log(ws.binaryType);\n"

                + "    ws.binaryType = undefined;\n"
                + "    log(ws.binaryType);\n"

                // Reset back to 'blob'
                + "    ws.binaryType = 'blob';\n"
                + "    log(ws.binaryType);\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Verifies receiving binary messages when binaryType is set to 'arraybuffer'.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "[object ArrayBuffer]", "4", "1,2,3,4"})
    public void receiveBinaryAsArrayBuffer() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", BinaryWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    ws.binaryType = 'arraybuffer';\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send('get_binary');\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      log(e.data instanceof ArrayBuffer);\n"
                + "      log(Object.prototype.toString.call(e.data));\n"
                + "      var view = new Uint8Array(e.data);\n"
                + "      log(view.byteLength);\n"
                + "      log(view[0] + ',' + view[1] + ',' + view[2] + ',' + view[3]);\n"
                + "      ws.close();\n"
                + "    };\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Verifies receiving binary messages when binaryType is set to 'blob' (Finding 1.A).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "[object Blob]", "4", "4", "1,2,3,4"})
    public void receiveBinaryAsBlob() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", BinaryWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    ws.binaryType = 'blob';\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send('get_binary');\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      log(e.data instanceof Blob);\n"
                + "      log(Object.prototype.toString.call(e.data));\n"
                + "      log(e.data.size);\n"
                + "      var reader = new FileReader();\n"
                + "      reader.onload = function() {\n"
                + "        var view = new Uint8Array(reader.result);\n"
                + "        log(view.byteLength);\n"
                + "        log(view[0] + ',' + view[1] + ',' + view[2] + ',' + view[3]);\n"
                + "        ws.close();\n"
                + "      };\n"
                + "      reader.readAsArrayBuffer(e.data);\n"
                + "    };\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Verifies dynamic switching of binaryType between consecutive messages.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"msg1: [object Blob]", "msg2: [object ArrayBuffer]", "msg3: [object Blob]"})
    public void binaryTypeDynamicSwitching() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", BinaryWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    var step = 0;\n"
                + "    ws.binaryType = 'blob';\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send('get_binary');\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      step++;\n"
                + "      if (step === 1) {\n"
                + "        log('msg1: ' + Object.prototype.toString.call(e.data));\n"
                + "        ws.binaryType = 'arraybuffer';\n"
                + "        ws.send('get_binary');\n"
                + "      } else if (step === 2) {\n"
                + "        log('msg2: ' + Object.prototype.toString.call(e.data));\n"
                + "        ws.binaryType = 'blob';\n"
                + "        ws.send('get_binary');\n"
                + "      } else if (step === 3) {\n"
                + "        log('msg3: ' + Object.prototype.toString.call(e.data));\n"
                + "        ws.close();\n"
                + "      }\n"
                + "    };\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Edge case: Verifies receiving an empty binary payload (0 bytes) as both Blob and ArrayBuffer.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"blob size: 0", "arraybuffer length: 0"})
    public void receiveEmptyBinaryPayload() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", BinaryWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    var step = 0;\n"
                + "    ws.binaryType = 'blob';\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send('get_empty_binary');\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      step++;\n"
                + "      if (step === 1) {\n"
                + "        log('blob size: ' + e.data.size);\n"
                + "        ws.binaryType = 'arraybuffer';\n"
                + "        ws.send('get_empty_binary');\n"
                + "      } else if (step === 2) {\n"
                + "        log('arraybuffer length: ' + e.data.byteLength);\n"
                + "        ws.close();\n"
                + "      }\n"
                + "    };\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Edge case: Verifies receiving large binary payloads (64 KB).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"65536", "first: 10", "last: 20"})
    public void receiveLargeBinaryPayload() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", BinaryWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    ws.binaryType = 'arraybuffer';\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send('get_large_binary');\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      var view = new Uint8Array(e.data);\n"
                + "      log(view.byteLength);\n"
                + "      log('first: ' + view[0]);\n"
                + "      log('last: ' + view[view.byteLength - 1]);\n"
                + "      ws.close();\n"
                + "    };\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Server-side WebSocket listener for binary data testing.
     */
    public static class BinaryWebSocketListener implements AutoDemanding {
        private Session session_;

        @Override
        public void onWebSocketOpen(Session session) {
            session_ = session;
        }

        @Override
        public void onWebSocketText(final String data) {
            if ("get_binary".equals(data)) {
                final byte[] payload = new byte[] {1, 2, 3, 4};
                session_.sendBinary(ByteBuffer.wrap(payload), Callback.NOOP);
            }
            else if ("get_empty_binary".equals(data)) {
                session_.sendBinary(ByteBuffer.wrap(new byte[0]), Callback.NOOP);
            }
            else if ("get_large_binary".equals(data)) {
                final byte[] largePayload = new byte[65536];
                largePayload[0] = 10;
                largePayload[largePayload.length - 1] = 20;
                session_.sendBinary(ByteBuffer.wrap(largePayload), Callback.NOOP);
            }
        }
    }

    /**
     * Verifies that readyState immediately transitions to CLOSING (2) upon calling close(),
     * and to CLOSED (3) once the close frame is processed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before close: 1", "immediately after close: 2", "onclose readyState: 3"})
    public void closeReadyStateTransition() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", EchoWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    ws.onopen = function() {\n"
                + "      log('before close: ' + ws.readyState);\n"
                + "      ws.close();\n"
                + "      log('immediately after close: ' + ws.readyState);\n"
                + "    };\n"
                + "    ws.onclose = function() {\n"
                + "      log('onclose readyState: ' + ws.readyState);\n"
                + "    };\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Verifies that event handlers (onopen, onmessage, onclose) are invoked exactly once per event.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onopen count: 1", "onmessage count: 1", "onclose count: 1"})
    public void eventHandlerSingleInvocation() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", EchoWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var openCount = 0;\n"
                + "    var messageCount = 0;\n"
                + "    var closeCount = 0;\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    ws.onopen = function() {\n"
                + "      openCount++;\n"
                + "      ws.send('ping');\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      messageCount++;\n"
                + "      ws.close();\n"
                + "    };\n"
                + "    ws.onclose = function() {\n"
                + "      closeCount++;\n"
                + "      log('onopen count: ' + openCount);\n"
                + "      log('onmessage count: ' + messageCount);\n"
                + "      log('onclose count: ' + closeCount);\n"
                + "    };\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Verifies bufferedAmount property existence and return type.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial bufferedAmount: 0", "type: number"})
    public void bufferedAmountInitialAndType() throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", EchoWebSocketListener.class);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false, SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var ws = new WebSocket('ws://localhost:" + PORT + "/ws');\n"
                + "    log('initial bufferedAmount: ' + ws.bufferedAmount);\n"
                + "    log('type: ' + typeof ws.bufferedAmount);\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * Server-side Echo WebSocket listener helper class.
     */
    public static class EchoWebSocketListener implements AutoDemanding {
        private Session session_;

        @Override
        public void onWebSocketOpen(final Session session) {
            session_ = session;
        }

        @Override
        public void onWebSocketText(final String data) {
            if (session_ != null && session_.isOpen()) {
                session_.sendText(data, Callback.NOOP);
            }
        }
    }

//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    public void socketsGetClosedOnPageReplace() throws Exception {
//        startWebServer("src/test/resources/org/htmlunit/javascript/host",
//                null, null, new ChatWebSocketHandler());
//        try {
//            final WebDriver driver = getWebDriver();
//            driver.get(URL_FIRST + "WebSocketTest_chat.html");
//
//            driver.findElement(By.id("username")).sendKeys("Browser");
//            driver.findElement(By.id("joinB")).click();
//
//            assertVisible("joined", driver);
//
//            driver.get(URL_FIRST + "plain.html");
//        }
//        finally {
//            stopWebServers();
//        }
//    }

    /**
     * JS helper: renders every char outside printable ASCII as a backslash-u escape, so the
     * logged values do not depend on any encoding.
     */
    private static final String SHOW_JS
        = "  function show(s) {\n"
        + "    return String(s).replace(/[^\\x20-\\x7e]/g, function(c) {\n"
        + "      return String.fromCharCode(92) + 'u' + ('0000' + c.charCodeAt(0).toString(16)).slice(-4);\n"
        + "    });\n"
        + "  }\n";

    /**
     * Starts a server with the given listener at {@code /ws}, loads a page containing the given script
     * and verifies the logged title. The script has to define {@code test()}, which is called onload.
     * The JS variable {@code url} points to the websocket endpoint, {@code show(s)} is available.
     *
     * @param listener the server side websocket listener
     * @param script the JS code of the page
     * @throws Exception in case of failure
     */
    private void runWithServer(final Class<? extends AutoDemanding> listener, final String script)
            throws Exception {
        stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/*", MockWebConnectionServlet.class);
        final Map<String, Class<? extends AutoDemanding>> socketListeners = new HashMap<>();
        socketListeners.put("/ws", listener);

        final Server server = JettyServerUtils.startWebServer(PORT,
                "src/test/resources/org/htmlunit/javascript/host", servlets, socketListeners, null, false,
                SSLVariant.NONE);
        try {
            final MockWebConnection mockWebConnection = new MockWebConnection();
            MockWebConnectionServlet.setMockconnection(mockWebConnection);

            final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + SHOW_JS
                + "  var url = 'ws://localhost:" + PORT + "/ws';\n"
                + script
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

            mockWebConnection.setDefaultResponse(html);

            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "dummy.html");

            expandExpectedAlertsVariables("dummy", PORT);
            verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }

    /**
     * send() while CONNECTING has to throw an InvalidStateError. In a browser this is deterministic
     * because the open event cannot fire before the current script has finished; HtmlUnit may be flaky
     * here as long as the connect callbacks are not queued on the JS thread.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"state: 0", "InvalidStateError/DOMException"})
    public void sendWhileConnecting() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var ws = new WebSocket(url);\n"
                + "    log('state: ' + ws.readyState);\n"
                + "    try {\n"
                + "      ws.send('x');\n"
                + "      log('no exception');\n"
                + "    } catch(e) { logEx(e); }\n"
                + "    ws.close();\n"
                + "  }\n");
    }

    /**
     * send() while CLOSING must not throw.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"state: 2", "no exception"})
    public void sendWhileClosing() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.close();\n"
                + "      log('state: ' + ws.readyState);\n"
                + "      try {\n"
                + "        ws.send('x');\n"
                + "        log('no exception');\n"
                + "      } catch(e) { logEx(e); }\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * send() after CLOSED must not throw; the bufferedAmount line records what the browsers do with the
     * discarded data.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"state: 3", "no exception", "bufferedAmount: 3"})
    public void sendAfterClosed() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.close();\n"
                + "    };\n"
                + "    ws.onclose = function() {\n"
                + "      log('state: ' + ws.readyState);\n"
                + "      try {\n"
                + "        ws.send('abc');\n"
                + "        log('no exception');\n"
                + "      } catch(e) { logEx(e); }\n"
                + "      log('bufferedAmount: ' + ws.bufferedAmount);\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * Text conversion of send(): strings, non-BMP chars, lone surrogates (USVString conversion) and
     * non-string values (converted with ToString). One logged line per sent value, in send order.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text:hello", "text:", "text:\\u00e4\\u20ac", "text:\\ud83d\\ude00",
             "text:a\\ufffdb", "text:42", "text:null", "text:undefined", "text:[object Object]"})
    public void sendTextVariants() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var payloads = ['hello', '', '\\u00E4\\u20AC', '\\uD83D\\uDE00', 'a\\uD800b',\n"
                + "                    42, null, undefined, {}];\n"
                + "    var received = [];\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      for (var i = 0; i < payloads.length; i++) {\n"
                + "        ws.send(payloads[i]);\n"
                + "      }\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      received.push(e.data);\n"
                + "      if (received.length == payloads.length) {\n"
                + "        for (var i = 0; i < received.length; i++) {\n"
                + "          log(show(received[i]));\n"
                + "        }\n"
                + "        ws.close();\n"
                + "      }\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * Binary variants of send(): ArrayBuffer, typed arrays (full, with offset, multi byte elements, signed),
     * DataView with offset, Blob and an empty buffer. The server reports every frame as
     * "bin:length:byte,byte,...". Typed arrays must contribute their view window, not the whole buffer.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"bin:3:1,2,3", "bin:5:1,2,3,4,5", "bin:3:2,3,4", "bin:4:2,1,4,3", "bin:3:255,127,128",
             "bin:3:12,13,14", "bin:3:97,98,99", "bin:0:"})
    public void sendBinaryVariants() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var makers = [\n"
                + "      function() { return new Uint8Array([1, 2, 3]).buffer; },\n"
                + "      function() { return new Uint8Array([1, 2, 3, 4, 5]); },\n"
                + "      function() { return new Uint8Array([1, 2, 3, 4, 5]).subarray(1, 4); },\n"
                + "      function() { return new Uint16Array([258, 772]); },\n"
                + "      function() { return new Int8Array([-1, 127, -128]); },\n"
                + "      function() { return new DataView(new Uint8Array([10, 11, 12, 13, 14, 15]).buffer, 2, 3); },\n"
                + "      function() { return new Blob(['abc']); },\n"
                + "      function() { return new ArrayBuffer(0); }\n"
                + "    ];\n"
                + "    var received = [];\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      for (var i = 0; i < makers.length; i++) {\n"
                + "        ws.send(makers[i]());\n"
                + "      }\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      received.push(e.data);\n"
                + "      if (received.length == makers.length) {\n"
                + "        for (var i = 0; i < received.length; i++) {\n"
                + "          log(received[i]);\n"
                + "        }\n"
                + "        ws.close();\n"
                + "      }\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * The data has to be copied at send() time: changing the buffer right after send() must not change
     * what the server receives.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"bin:3:1,2,3", "bin:3:1,2,3"})
    public void sendBufferIsCopied() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var data = new Uint8Array([1, 2, 3]);\n"
                + "    var received = [];\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send(data);\n"
                + "      ws.send(data.buffer);\n"
                + "      data[0] = 99;\n"
                + "      data[1] = 98;\n"
                + "      data[2] = 97;\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      received.push(e.data);\n"
                + "      if (received.length == 2) {\n"
                + "        log(received[0]);\n"
                + "        log(received[1]);\n"
                + "        ws.close();\n"
                + "      }\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * Text and binary frames sent in a row have to arrive in the same order.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text:a bin:1:1 text:b bin:1:2")
    public void sendMixedOrder() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var received = [];\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send('a');\n"
                + "      ws.send(new Uint8Array([1]));\n"
                + "      ws.send('b');\n"
                + "      ws.send(new Uint8Array([2]));\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      received.push(e.data);\n"
                + "      if (received.length == 4) {\n"
                + "        log(received.join(' '));\n"
                + "        ws.close();\n"
                + "      }\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * close(code) validation: only 1000 and 3000-4999 are allowed (InvalidAccessError otherwise).
     * The check has to happen even while the socket is still CONNECTING.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none: ok", "999: InvalidAccessError", "1000: ok", "1001: InvalidAccessError", "1005: InvalidAccessError",
             "1006: InvalidAccessError", "2999: InvalidAccessError", "3000: ok", "4999: ok", "5000: InvalidAccessError",
             "negative: InvalidAccessError", "string: InvalidAccessError", "null: InvalidAccessError",
             "undefined: ok", "fraction: ok"})
    public void closeCodeValidation() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function tryClose(label, args) {\n"
                + "    var ws = new WebSocket(url);\n"
                + "    try {\n"
                + "      ws.close.apply(ws, args);\n"
                + "      log(label + ': ok');\n"
                + "    } catch(e) { log(label + ': ' + e.name); }\n"
                + "  }\n"
                + "  function test() {\n"
                + "    tryClose('none', []);\n"
                + "    tryClose('999', [999]);\n"
                + "    tryClose('1000', [1000]);\n"
                + "    tryClose('1001', [1001]);\n"
                + "    tryClose('1005', [1005]);\n"
                + "    tryClose('1006', [1006]);\n"
                + "    tryClose('2999', [2999]);\n"
                + "    tryClose('3000', [3000]);\n"
                + "    tryClose('4999', [4999]);\n"
                + "    tryClose('5000', [5000]);\n"
                + "    tryClose('negative', [-1]);\n"
                + "    tryClose('string', ['abc']);\n"
                + "    tryClose('null', [null]);\n"
                + "    tryClose('undefined', [undefined]);\n"
                + "    tryClose('fraction', [1000.5]);\n"
                + "  }\n");
    }

    /**
     * close(1000, reason) validation: the reason may be at most 123 bytes in UTF-8 (SyntaxError otherwise).
     * The multi byte cases show whether bytes or chars are counted. The last line records what happens
     * for a reason without a code.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(
            DEFAULT = {"short: ok", "empty: ok", "123 ascii: ok", "124 ascii: SyntaxError", "122 bytes: ok", "124 bytes: SyntaxError",
                       "123 bytes: ok", "126 bytes: SyntaxError", "lone surrogate: ok", "undefined: ok", "null: ok",
                       "reason without code: InvalidAccessError"},
            FF = {"short: ok", "empty: ok", "123 ascii: ok", "124 ascii: SyntaxError", "122 bytes: ok", "124 bytes: SyntaxError",
                  "123 bytes: ok", "126 bytes: SyntaxError", "lone surrogate: ok", "undefined: ok", "null: ok",
                  "reason without code: ok"},
            FF_ESR = {"short: ok", "empty: ok", "123 ascii: ok", "124 ascii: SyntaxError", "122 bytes: ok", "124 bytes: SyntaxError",
                      "123 bytes: ok", "126 bytes: SyntaxError", "lone surrogate: ok", "undefined: ok", "null: ok",
                      "reason without code: ok"})
    public void closeReasonValidation() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function rep(s, n) {\n"
                + "    return new Array(n + 1).join(s);\n"
                + "  }\n"
                + "  function tryReason(label, reason) {\n"
                + "    var ws = new WebSocket(url);\n"
                + "    try {\n"
                + "      ws.close(1000, reason);\n"
                + "      log(label + ': ok');\n"
                + "    } catch(e) { log(label + ': ' + e.name); }\n"
                + "  }\n"
                + "  function tryNoCode() {\n"
                + "    var ws = new WebSocket(url);\n"
                + "    try {\n"
                + "      ws.close(undefined, 'bye');\n"
                + "      log('reason without code: ok');\n"
                + "    } catch(e) { log('reason without code: ' + e.name); }\n"
                + "  }\n"
                + "  function test() {\n"
                + "    tryReason('short', 'bye');\n"
                + "    tryReason('empty', '');\n"
                + "    tryReason('123 ascii', rep('x', 123));\n"
                + "    tryReason('124 ascii', rep('x', 124));\n"
                + "    tryReason('122 bytes', rep('\\u00E4', 61));\n"
                + "    tryReason('124 bytes', rep('\\u00E4', 62));\n"
                + "    tryReason('123 bytes', rep('\\u20AC', 41));\n"
                + "    tryReason('126 bytes', rep('\\u20AC', 42));\n"
                + "    tryReason('lone surrogate', 'a\\uD800b');\n"
                + "    tryReason('undefined', undefined);\n"
                + "    tryReason('null', null);\n"
                + "    tryNoCode();\n"
                + "  }\n");
    }

    /**
     * The code and the reason passed to close() have to arrive at the server. For every case a second
     * socket asks the server which close it saw last ("server:" lines); "client:" is what the closing
     * socket itself reports in its close event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"client: 1005||true", "server: 1005|", "client: 1000||true", "server: 1000|", "client: 3001|bye|true",
             "server: 3001|bye", "client: 1000|\\u00e4\\u20ac|true", "server: 1000|\\u00e4\\u20ac"})
    public void closeCodeAndReasonReachServer() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  var cases = [[], [1000], [3001, 'bye'], [1000, '\\u00E4\\u20AC']];\n"
                + "  function step(i) {\n"
                + "    if (i >= cases.length) {\n"
                + "      return;\n"
                + "    }\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.close.apply(ws, cases[i]);\n"
                + "    };\n"
                + "    ws.onclose = function(e) {\n"
                + "      log('client: ' + e.code + '|' + show(e.reason) + '|' + e.wasClean);\n"
                + "      var query = new WebSocket(url);\n"
                + "      query.onopen = function() {\n"
                + "        query.send('cmd:last-close');\n"
                + "      };\n"
                + "      query.onmessage = function(m) {\n"
                + "        log('server: ' + show(m.data));\n"
                + "        query.close();\n"
                + "      };\n"
                + "      query.onclose = function() {\n"
                + "        step(i + 1);\n"
                + "      };\n"
                + "    };\n"
                + "  }\n"
                + "  function test() {\n"
                + "    step(0);\n"
                + "  }\n");
    }

    /**
     * Server initiated close with different codes and reasons: what the close event reports (code, reason,
     * wasClean) and the final readyState. Complements {@link #wasClean()}, which only covers code 1000.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1000 -> 1000||true|3", "1001 -> 1001|bye|true|3", "1008 -> 1008|policy|true|3",
                       "3000 -> 3000|x|true|3", "4000 -> 4000|custom|true|3"},
            FF = {"1000 -> 1000||false|3", "1001 -> 1001|bye|false|3", "1008 -> 1008|policy|false|3",
                  "3000 -> 3000|x|false|3", "4000 -> 4000|custom|false|3"},
            FF_ESR = {"1000 -> 1000||false|3", "1001 -> 1001|bye|false|3", "1008 -> 1008|policy|false|3",
                      "3000 -> 3000|x|false|3", "4000 -> 4000|custom|false|3"})
    public void serverCloseWithCode() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  var cases = [[1000, ''], [1001, 'bye'], [1008, 'policy'], [3000, 'x'], [4000, 'custom']];\n"
                + "  function step(i) {\n"
                + "    if (i >= cases.length) {\n"
                + "      return;\n"
                + "    }\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send('cmd:close:' + cases[i][0] + ':' + cases[i][1]);\n"
                + "    };\n"
                + "    ws.onclose = function(e) {\n"
                + "      log(cases[i][0] + ' -> ' + e.code + '|' + show(e.reason) + '|' + e.wasClean\n"
                + "          + '|' + ws.readyState);\n"
                + "      step(i + 1);\n"
                + "    };\n"
                + "  }\n"
                + "  function test() {\n"
                + "    step(0);\n"
                + "  }\n");
    }

    /**
     * A failed connection has to fire exactly one error and one close event (in this order, no open),
     * with code 1006, an empty reason and wasClean=false. The summary is logged after a delay so
     * additional events would show up.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error:error close:1006:[]:false", "final state: 3"})
    public void connectErrorEventsOnlyOnce() throws Exception {
        stopWebServers();

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var events = [];\n"
            + "    var ws = new WebSocket('ws://localhost:" + PORT + "/');\n"
            + "    ws.onopen = function() { events.push('open'); };\n"
            + "    ws.onerror = function(e) { events.push('error:' + e.type); };\n"
            + "    ws.onclose = function(e) {\n"
            + "      events.push('close:' + e.code + ':[' + e.reason + ']:' + e.wasClean);\n"
            + "    };\n"
            + "    setTimeout(function() {\n"
            + "      log(events.join(' '));\n"
            + "      log('final state: ' + ws.readyState);\n"
            + "    }, 500);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * close() while CONNECTING: the state switches to CLOSING immediately, the connection attempt fails
     * and no open event may be fired afterwards, even though the server would accept the connection.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"state after close: 2", "error close:1006:false", "final state: 3"})
    public void closeWhileConnecting() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var events = [];\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() { events.push('open'); };\n"
                + "    ws.onerror = function() { events.push('error'); };\n"
                + "    ws.onclose = function(e) { events.push('close:' + e.code + ':' + e.wasClean); };\n"
                + "    ws.close();\n"
                + "    log('state after close: ' + ws.readyState);\n"
                + "    setTimeout(function() {\n"
                + "      log(events.join(' '));\n"
                + "      log('final state: ' + ws.readyState);\n"
                + "    }, 500);\n"
                + "  }\n");
    }

    /**
     * Calling close() twice has to result in exactly one close event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"state: 2", "close events: 1", "final state: 3"})
    public void closeTwice() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var closeCount = 0;\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.close();\n"
                + "      ws.close();\n"
                + "      log('state: ' + ws.readyState);\n"
                + "    };\n"
                + "    ws.onclose = function() { closeCount++; };\n"
                + "    setTimeout(function() {\n"
                + "      log('close events: ' + closeCount);\n"
                + "      log('final state: ' + ws.readyState);\n"
                + "    }, 500);\n"
                + "  }\n");
    }

    /**
     * Calling close() inside the close handler must not throw and must not fire another close event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no exception§state: 3")
    public void closeInsideOnclose() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var closeCount = 0;\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.close();\n"
                + "    };\n"
                + "    ws.onclose = function() {\n"
                + "      closeCount++;\n"
                + "      try {\n"
                + "        ws.close();\n"
                + "        log('no exception');\n"
                + "      } catch(e) { logEx(e); }\n"
                + "      log('state: ' + ws.readyState);\n"
                + "    };\n"
                + "    setTimeout(function() {\n"
                + "      log('close events: ' + closeCount);\n"
                + "    }, 500);\n"
                + "  }\n");
    }

    /**
     * Validation of the protocols argument: invalid tokens and duplicates are a SyntaxError.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string: ok", "array: ok", "empty array: ok", "undefined: ok", "null: ok", "number: ok",
             "duplicate: SyntaxError", "different case: ok", "empty string: SyntaxError", "blank inside: SyntaxError",
             "comma: SyntaxError", "non ascii: SyntaxError", "one invalid in array: SyntaxError", "object: SyntaxError"})
    public void constructorProtocols() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function tryCtor(label, protocols) {\n"
                + "    try {\n"
                + "      new WebSocket(url, protocols);\n"
                + "      log(label + ': ok');\n"
                + "    } catch(e) { log(label + ': ' + e.name); }\n"
                + "  }\n"
                + "  function test() {\n"
                + "    tryCtor('string', 'chat');\n"
                + "    tryCtor('array', ['chat', 'superchat']);\n"
                + "    tryCtor('empty array', []);\n"
                + "    tryCtor('undefined', undefined);\n"
                + "    tryCtor('null', null);\n"
                + "    tryCtor('number', 5);\n"
                + "    tryCtor('duplicate', ['a', 'a']);\n"
                + "    tryCtor('different case', ['a', 'A']);\n"
                + "    tryCtor('empty string', '');\n"
                + "    tryCtor('blank inside', 'a b');\n"
                + "    tryCtor('comma', 'a,b');\n"
                + "    tryCtor('non ascii', '\\u00E4');\n"
                + "    tryCtor('one invalid in array', ['a', 'a b']);\n"
                + "    tryCtor('object', {});\n"
                + "  }\n");
    }

    /**
     * Extra constructor arguments are ignored; calling the constructor without new is a TypeError.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"three arguments: ok", "without new: TypeError"})
    public void constructorArguments() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    try {\n"
                + "      new WebSocket(url, 'a', 'b');\n"
                + "      log('three arguments: ok');\n"
                + "    } catch(e) { log('three arguments: ' + e.name); }\n"
                + "    try {\n"
                + "      WebSocket(url);\n"
                + "      log('without new: ok');\n"
                + "    } catch(e) { log('without new: ' + e.name); }\n"
                + "  }\n");
    }

    /**
     * The server does not select one of the offered sub protocols, so protocol stays empty.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT =  {"before open: []", "final state: 3"},
            FF = {"before open: []", "after open: []", "final state: 3"},
            FF_ESR = {"before open: []", "after open: []", "final state: 3"})
    public void protocolNotNegotiated() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var ws = new WebSocket(url, 'chat');\n"
                + "    log('before open: [' + ws.protocol + ']');\n"
                + "    ws.onopen = function() {\n"
                + "      log('after open: [' + ws.protocol + ']');\n"
                + "      ws.close();\n"
                + "    };\n"
                + "    setTimeout(function() {\n"
                + "      log('final state: ' + ws.readyState);\n"
                + "    }, 500);\n"
                + "  }\n");
    }

    /**
     * The extensions property exists and is a string. Only the type is logged, the value depends on
     * what the server negotiates (e.g. permessage-deflate).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before open: string", "after open: string"})
    public void extensionsProperty() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var ws = new WebSocket(url);\n"
                + "    log('before open: ' + typeof ws.extensions);\n"
                + "    ws.onopen = function() {\n"
                + "      log('after open: ' + typeof ws.extensions);\n"
                + "      ws.close();\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * How the url property is serialized: case normalization, relative urls, percent encoding,
     * dot segments, blanks, userinfo, IPv6. The port is replaced by PORT in the output.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"upper case: ws://localhost:§§PORT§§/Path", "relative: ws://localhost:§§PORT§§/ws",
             "space in path: ws://localhost:§§PORT§§/a%20b", "umlaut in path: ws://localhost:§§PORT§§/%C3%A4",
             "braces in path: ws://localhost:§§PORT§§/%7Bx%7D", "dot segments: ws://localhost:§§PORT§§/a/c",
             "query without path: ws://localhost:§§PORT§§/?x=1", "space in query: ws://localhost:§§PORT§§/p?q=a%20b",
             "userinfo: ws://user:pw@localhost:§§PORT§§/", "ipv6: ws://[::1]:§§PORT§§/", "surrounding blanks: ws://localhost:§§PORT§§/",
             "no host: ws://x/", "empty port: ws://localhost/x"})
    public void urlSerialization() throws Exception {
        stopWebServers();

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function check(label, input) {\n"
            + "    try {\n"
            + "      var ws = new WebSocket(input);\n"
            + "      log(label + ': ' + ws.url);\n"
            + "      ws.close();\n"
            + "    } catch(e) { log(label + ': ' + e.name); }\n"
            + "  }\n"
            + "  function test() {\n"
            + "    check('upper case', 'WS://LOCALHOST:" + PORT + "/Path');\n"
            + "    check('relative', '/ws');\n"
            + "    check('space in path', 'ws://localhost:" + PORT + "/a b');\n"
            + "    check('umlaut in path', 'ws://localhost:" + PORT + "/\\u00E4');\n"
            + "    check('braces in path', 'ws://localhost:" + PORT + "/{x}');\n"
            + "    check('dot segments', 'ws://localhost:" + PORT + "/a/./b/../c');\n"
            + "    check('query without path', 'ws://localhost:" + PORT + "?x=1');\n"
            + "    check('space in query', 'ws://localhost:" + PORT + "/p?q=a b');\n"
            + "    check('userinfo', 'ws://user:pw@localhost:" + PORT + "/');\n"
            + "    check('ipv6', 'ws://[::1]:" + PORT + "/');\n"
            + "    check('surrounding blanks', ' ws://localhost:" + PORT + "/ ');\n"
            + "    check('no host', 'ws:///x');\n"
            + "    check('empty port', 'ws://localhost:/x');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        expandExpectedAlertsVariables("dummy", PORT);
        loadPageVerifyTitle2(html);
    }

    /**
     * Any fragment, even an empty one, is a SyntaxError.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"empty fragment: SyntaxError", "fragment: SyntaxError", "path and fragment: SyntaxError", "query and fragment: SyntaxError", "http with fragment: SyntaxError"})
    public void urlFragment() throws Exception {
        stopWebServers();

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function check(label, input) {\n"
            + "    try {\n"
            + "      var ws = new WebSocket(input);\n"
            + "      log(label + ': ' + ws.url);\n"
            + "      ws.close();\n"
            + "    } catch(e) { log(label + ': ' + e.name); }\n"
            + "  }\n"
            + "  function test() {\n"
            + "    check('empty fragment', 'ws://localhost:" + PORT + "/#');\n"
            + "    check('fragment', 'ws://localhost:" + PORT + "/#frag');\n"
            + "    check('path and fragment', 'ws://localhost:" + PORT + "/p#frag');\n"
            + "    check('query and fragment', 'ws://localhost:" + PORT + "/p?q=1#frag');\n"
            + "    check('http with fragment', 'http://localhost:" + PORT + "/#frag');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The origin of a message event is the origin of the websocket url: path and query are dropped.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ws://localhost:§§PORT§§/ws?x=1", "text:hi", "ws://localhost:§§PORT§§"})
    public void messageOriginWithPathAndQuery() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var ws = new WebSocket(url + '?x=1');\n"
                + "    log(ws.url);\n"
                + "    ws.onopen = function() {\n"
                + "      ws.send('hi');\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      log(e.data);\n"
                + "      log(e.origin);\n"
                + "      ws.close();\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * 20 messages sent by the server in a burst, followed by a server side close: open first, the messages in
     * order, close last, and nothing more afterwards (checked after a delay).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"in order: true", "count: 22", "count later: 22"})
    public void eventOrderBurst() throws Exception {
        runWithServer(ControlWebSocketListener.class,
                "  function test() {\n"
                + "    var events = [];\n"
                + "    var ws = new WebSocket(url);\n"
                + "    ws.onopen = function() {\n"
                + "      events.push('open');\n"
                + "      ws.send('cmd:burst:20');\n"
                + "    };\n"
                + "    ws.onmessage = function(e) {\n"
                + "      events.push(e.data);\n"
                + "    };\n"
                + "    ws.onclose = function() {\n"
                + "      events.push('close');\n"
                + "      var expected = ['open'];\n"
                + "      for (var i = 0; i < 20; i++) {\n"
                + "        expected.push('m' + i);\n"
                + "      }\n"
                + "      expected.push('close');\n"
                + "      log('in order: ' + (events.join(',') == expected.join(',')));\n"
                + "      log('count: ' + events.length);\n"
                + "      setTimeout(function() {\n"
                + "        log('count later: ' + events.length);\n"
                + "      }, 500);\n"
                + "    };\n"
                + "  }\n");
    }

    /**
     * Server-side listener for the tests above. Every text frame is answered with "text:" + data, every
     * binary frame with "bin:length:byte,byte,...". Replies are queued, so frames that arrive in a row never
     * overlap on the wire. Commands (text frames):
     * <ul>
     *   <li>{@code cmd:close:code:reason} - closes the connection from the server side</li>
     *   <li>{@code cmd:burst:count} - sends count messages "m0".."mN" and closes with 1000</li>
     *   <li>{@code cmd:last-close} - answers with "code|reason" of the last close received from a
     *       connection that did not send this command</li>
     * </ul>
     */
    public static class ControlWebSocketListener implements AutoDemanding {
        private static final Object CLOSE = new Object();
        private static final AtomicReference<String> LAST_CLOSE = new AtomicReference<>("none");

        private final Queue<Object> outbox_ = new ConcurrentLinkedQueue<>();
        private final AtomicBoolean sending_ = new AtomicBoolean();
        private volatile Session session_;
        private volatile boolean query_;

        @Override
        public void onWebSocketOpen(final Session session) {
            session_ = session;
        }

        @Override
        public void onWebSocketText(final String data) {
            if (data.startsWith("cmd:close:")) {
                final String[] parts = data.split(":", 4);
                session_.close(Integer.parseInt(parts[2]), parts.length > 3 ? parts[3] : "", Callback.NOOP);
            }
            else if (data.startsWith("cmd:burst:")) {
                final int count = Integer.parseInt(data.substring("cmd:burst:".length()));
                for (int i = 0; i < count; i++) {
                    enqueue("m" + i);
                }
                enqueue(CLOSE);
            }
            else if ("cmd:last-close".equals(data)) {
                query_ = true;
                enqueue(LAST_CLOSE.get());
            }
            else {
                enqueue("text:" + data);
            }
        }

        @Override
        public void onWebSocketBinary(final ByteBuffer payload, final Callback callback) {
            final StringBuilder description = new StringBuilder("bin:").append(payload.remaining()).append(':');
            boolean first = true;
            while (payload.hasRemaining()) {
                if (!first) {
                    description.append(',');
                }
                description.append(payload.get() & 0xFF);
                first = false;
            }
            enqueue(description.toString());
            callback.succeed();
        }

        @Override
        public void onWebSocketClose(final int statusCode, final String reason, final Callback callback) {
            if (!query_) {
                LAST_CLOSE.set(statusCode + "|" + (reason == null ? "" : reason));
            }
            callback.succeed();
        }

        private void enqueue(final Object message) {
            outbox_.add(message);
            drain();
        }

        private void drain() {
            if (!sending_.compareAndSet(false, true)) {
                return;
            }

            final Object next = outbox_.poll();
            if (next == null) {
                sending_.set(false);
                if (!outbox_.isEmpty()) {
                    drain();
                }
                return;
            }

            if (next == CLOSE) {
                sending_.set(false);
                session_.close(1000, "done", Callback.NOOP);
                return;
            }

            session_.sendText((String) next, Callback.from(
                    () -> {
                        sending_.set(false);
                        drain();
                    },
                    error -> sending_.set(false)));
        }
    }
}
