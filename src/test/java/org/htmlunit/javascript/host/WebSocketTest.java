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

import static java.nio.charset.StandardCharsets.UTF_16LE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
    @Alerts(DEFAULT = {"exception no param", "ws://localhost:22222/undefined", "ws://localhost:22222/null",
                       "ws://localhost:22222/", "ws://localhost:22222/", "exception invalid"},
            FF = {"exception no param", "ws://localhost:22222/undefined", "ws://localhost:22222/null",
                  "exception empty", "ws://localhost:22222/", "exception invalid"},
            FF_ESR = {"exception no param", "ws://localhost:22222/undefined", "ws://localhost:22222/null",
                      "exception empty", "ws://localhost:22222/", "exception invalid"})
    @HtmlUnitNYI(
            CHROME = {"exception no param", "ws://localhost:22222/undefined", "ws://localhost:22222/null",
                      "ws://localhost:22222/", "ws://localhost:22222/", "ws://localhost:22222/#"},
            EDGE = {"exception no param", "ws://localhost:22222/undefined", "ws://localhost:22222/null",
                    "ws://localhost:22222/", "ws://localhost:22222/", "ws://localhost:22222/#"},
            FF = {"exception no param", "ws://localhost:22222/undefined", "ws://localhost:22222/null",
                  "ws://localhost:22222/", "ws://localhost:22222/", "ws://localhost:22222/#"},
            FF_ESR = {"exception no param", "ws://localhost:22222/undefined", "ws://localhost:22222/null",
                      "ws://localhost:22222/", "ws://localhost:22222/", "ws://localhost:22222/#"})
    public void initialWithoutUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      let ws = new WebSocket();\n"
            + "      log(ws.url);"
            + "    } catch(e) { log('exception no param') }\n"

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

            + "    try {\n"
            + "      let ws = new WebSocket('#');\n"
            + "      log(ws.url);"
            + "    } catch(e) { log('exception invalid') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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

        startWebServer("src/test/resources/org/htmlunit/javascript/host",
            null, null, new ChatWebSocketHandler());
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
            stopWebServers();
        }
    }

    private static class ChatWebSocketHandler extends WebSocketHandler {

        private final Set<ChatWebSocket> webSockets_ = new CopyOnWriteArraySet<>();

        ChatWebSocketHandler() {
        }

        @Override
        public void configure(final WebSocketServletFactory factory) {
            factory.register(ChatWebSocket.class);
            factory.setCreator(new WebSocketCreator() {
                @Override
                public Object createWebSocket(final ServletUpgradeRequest servletUpgradeRequest,
                        final ServletUpgradeResponse servletUpgradeResponse) {
                    return new ChatWebSocket();
                }
            });
        }

        private class ChatWebSocket extends WebSocketAdapter {
            private Session session_;

            ChatWebSocket() {
            }

            @Override
            public void onWebSocketConnect(final Session session) {
                session_ = session;
                webSockets_.add(this);
            }

            @Override
            public void onWebSocketText(final String data) {
                try {
                    for (final ChatWebSocket webSocket : webSockets_) {
                        webSocket.session_.getRemote().sendString(data);
                    }
                }
                catch (final IOException e) {
                    session_.close();
                }
            }

            @Override
            public void onWebSocketClose(final int closeCode, final String message) {
                webSockets_.remove(this);
            }
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
                for (int i = 0; i < traces.length; i++) {
                    System.err.println(traces[i]);
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

        startWebServer("src/test/resources/org/htmlunit/javascript/host",
            null, null, new CookiesWebSocketHandler());
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
            stopWebServers();
        }
    }

    private static class CookiesWebSocketHandler extends WebSocketHandler {

        private final Set<CookiesWebSocket> webSockets_ = new CopyOnWriteArraySet<>();

        CookiesWebSocketHandler() {
        }

        @Override
        public void configure(final WebSocketServletFactory factory) {
            factory.register(CookiesWebSocket.class);
            factory.setCreator(new WebSocketCreator() {
                @Override
                public Object createWebSocket(final ServletUpgradeRequest servletUpgradeRequest,
                        final ServletUpgradeResponse servletUpgradeResponse) {
                    return new CookiesWebSocket();
                }
            });
        }

        private class CookiesWebSocket extends WebSocketAdapter {
            private Session session_;
            private int counter_ = 1;

            CookiesWebSocket() {
            }

            @Override
            public void onWebSocketConnect(final Session session) {
                session_ = session;
                webSockets_.add(this);
            }

            @Override
            public void onWebSocketText(final String data) {
                try {
                    final String cookie = session_.getUpgradeRequest().getHeaders()
                                                        .get(HttpHeader.COOKIE).get(0) + counter_++;
                    for (final CookiesWebSocket webSocket : webSockets_) {
                        webSocket.session_.getRemote().sendString(cookie);
                    }
                }
                catch (final IOException e) {
                    session_.close();
                }
            }

            @Override
            public void onWebSocketClose(final int closeCode, final String message) {
                webSockets_.remove(this);
            }
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

        startWebServer("src/test/resources/org/htmlunit/javascript/host",
            null, null, new EventsWebSocketHandler());
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
            stopWebServers();
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

        startWebServer("src/test/resources/org/htmlunit/javascript/host",
            null, null, new EventsWebSocketHandler());
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
            stopWebServers();
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
        startWebServer("src/test/resources/org/htmlunit/javascript/host", null, null, null);
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
            while (text.length() > 0 && counter++ < 10);

            assertEquals(String.join("\n", getExpectedAlerts()), text);
        }
        finally {
            stopWebServers();
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

    private static class EventsWebSocketHandler extends WebSocketHandler {

        EventsWebSocketHandler() {
        }

        @Override
        public void configure(final WebSocketServletFactory factory) {
            factory.register(EventsWebSocket.class);
            factory.setCreator(new WebSocketCreator() {
                @Override
                public EventsWebSocket createWebSocket(final ServletUpgradeRequest servletUpgradeRequest,
                        final ServletUpgradeResponse servletUpgradeResponse) {
                    return new EventsWebSocket();
                }
            });
        }

        private static class EventsWebSocket extends WebSocketAdapter {

            EventsWebSocket() {
            }

            @Override
            public void onWebSocketText(final String data) {
                if ("text".equals(data)) {
                    try {
                        getRemote().sendString("server_text");
                    }
                    catch (final IOException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
                else if ("close".equals(data)) {
                    getSession().close();
                }
                else {
                    throw new IllegalArgumentException("Unknown request: " + data);
                }
            }

            @Override
            public void onWebSocketBinary(final byte[] payload, final int offset, final int len) {
                final String data = new String(payload, offset, len, UTF_16LE);
                if ("binary".equals(data)) {
                    final ByteBuffer response = ByteBuffer.wrap("server_binary".getBytes(UTF_16LE));
                    try {
                        getRemote().sendBytes(response);
                    }
                    catch (final IOException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
                else {
                    throw new IllegalArgumentException("Unknown request: " + data);
                }
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
     * @throws Exception if the test fails
     */
    @Test
    public void socketsGetClosedOnPageReplace() throws Exception {
        startWebServer("src/test/resources/org/htmlunit/javascript/host",
                null, null, new ChatWebSocketHandler());
        try {
            final WebDriver driver = getWebDriver();
            driver.get(URL_FIRST + "WebSocketTest_chat.html");

            driver.findElement(By.id("username")).sendKeys("Browser");
            driver.findElement(By.id("joinB")).click();

            assertVisible("joined", driver);

            driver.get(URL_FIRST + "plain.html");
        }
        finally {
            stopWebServers();
        }
    }
}
