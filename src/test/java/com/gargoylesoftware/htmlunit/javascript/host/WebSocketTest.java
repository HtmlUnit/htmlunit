/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link WebSocket}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class WebSocketTest extends WebDriverTestCase {

    /**
     * Test case taken from <a href="http://angelozerr.wordpress.com/2011/07/23/websockets_jetty_step1/">here</a>.
     * @throws Exception if the test fails
     */
    @Test
    public void chat() throws Exception {
//        //TODO: compatibility of FF and Chrome versions.
//        if (getBrowserVersion().isFirefox()) {
//            startWebServer("src/test/resources/com/gargoylesoftware/htmlunit/javascript/host",
//                null, null, new ChatWebSocketHandler());
//            final WebDriver driver = getWebDriver();
//            driver.get("http://localhost:" + PORT + "/WebSocketTest_chat.html");
//
//            driver.findElement(By.id("username")).sendKeys("Browser");
//            driver.findElement(By.id("joinB")).click();
//            Thread.sleep(500);
//
//            final WebElement chatE = driver.findElement(By.id("chat"));
//            assertEquals("Browser: has joined!", chatE.getText());
//            driver.findElement(By.id("phrase")).sendKeys("Hope you are fine!");
//            driver.findElement(By.id("sendB")).click();
//            Thread.sleep(500);
//
//            assertEquals("Browser: has joined!\nBrowser: Hope you are fine!", chatE.getText());
//        }
    }

//    private static class ChatWebSocketHandler extends WebSocketHandler {
//
//        private final Set<ChatWebSocket> webSockets_ = new CopyOnWriteArraySet<ChatWebSocket>();
//
//        public WebSocket doWebSocketConnect(final HttpServletRequest request, final String protocol) {
//            return new ChatWebSocket();
//        }
//
//        private class ChatWebSocket implements WebSocket.OnTextMessage {
//
//            private Connection connection_;
//
//            public void onOpen(final Connection connection) {
//                this.connection_ = connection;
//                webSockets_.add(this);
//            }
//
//            public void onMessage(final String data) {
//                try {
//                    for (final ChatWebSocket webSocket : webSockets_) {
//                        webSocket.connection_.sendMessage(data);
//                    }
//                }
//                catch (final IOException x) {
//                    this.connection_.close();
//                }
//            }
//
//            public void onClose(final int closeCode, final String message) {
//                webSockets_.remove(this);
//            }
//        }
//    }
}
