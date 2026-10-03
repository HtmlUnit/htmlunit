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
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

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
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
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
}
