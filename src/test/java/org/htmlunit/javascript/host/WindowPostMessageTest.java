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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests postMessage within the Windows context.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 *
 */
@RunWith(BrowserRunner.class)
public class WindowPostMessageTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"type: message", "bubbles: false", "cancelable: false", "data: hello",
             "origin: ", "source: true false", "lastEventId: "})
    public void postMessage() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        expectedAlerts[4] += "http://127.0.0.1:" + PORT;
        setExpectedAlerts(expectedAlerts);

        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <iframe id='myFrame' src='" + URL_THIRD + "'></iframe>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var win = document.getElementById('myFrame').contentWindow;\n"

            + "  function receiveMessage(event) {\n"
            + "    log('type: ' + event.type);\n"
            + "    log('bubbles: ' + event.bubbles);\n"
            + "    log('cancelable: ' + event.cancelable);\n"
            + "    log('data: ' + event.data);\n"
            + "    log('origin: ' + event.origin);\n"
            + "    log('source: ' + (event.source === win) + ' ' + (event.source === window));\n"
            + "    log('lastEventId: ' + event.lastEventId);\n"
            + "  }\n"

            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage('hello', '*');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_THIRD, iframe);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"type: message", "bubbles: false", "cancelable: false", "data: hello",
             "origin: ", "source: false true", "lastEventId: "})
    public void postMessageFromIframe() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        expectedAlerts[4] += "http://localhost:" + PORT;
        setExpectedAlerts(expectedAlerts);

        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <iframe id='myFrame' src='" + URL_THIRD + "'></iframe>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var win = document.getElementById('myFrame').contentWindow;\n"

            + "  function receiveMessage(event) {\n"
            + "    log('type: ' + event.type);\n"
            + "    log('bubbles: ' + event.bubbles);\n"
            + "    log('cancelable: ' + event.cancelable);\n"
            + "    log('data: ' + event.data);\n"
            + "    log('origin: ' + event.origin);\n"
            + "    log('source: ' + (event.source === win) + ' ' + (event.source === window));\n"
            + "    log('lastEventId: ' + event.lastEventId);\n"
            + "  }\n"

            + "  win.addEventListener('message', receiveMessage, false);\n"
            + "  win.postMessage('hello', '*');\n"
            + "</script>\n"
            + "</body></html>";

        final String iframe = "<html><body><p>inside frame</p></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void postMessageMissingParameters() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    window.postMessage();\n"
            + "  } catch (e) {\n"
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"type: message", "bubbles: false", "cancelable: false", "data: hello",
             "origin: ", "source: false true", "lastEventId: "})
    public void postMessageWithoutTargetOrigin() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        expectedAlerts[4] += "http://localhost:" + PORT;
        setExpectedAlerts(expectedAlerts);

        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <iframe id='myFrame' src='" + URL_THIRD + "'></iframe>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var win = document.getElementById('myFrame').contentWindow;\n"

            + "  function receiveMessage(event) {\n"
            + "    log('type: ' + event.type);\n"
            + "    log('bubbles: ' + event.bubbles);\n"
            + "    log('cancelable: ' + event.cancelable);\n"
            + "    log('data: ' + event.data);\n"
            + "    log('origin: ' + event.origin);\n"
            + "    log('source: ' + (event.source === win) + ' ' + (event.source === window));\n"
            + "    log('lastEventId: ' + event.lastEventId);\n"
            + "  }\n"

            + "  win.addEventListener('message', receiveMessage, false);\n"
            + "  win.postMessage('hello');\n"
            + "</script>\n"
            + "</body></html>";

        final String iframe = "<html><body><p>inside frame</p></body></html>";

        getMockWebConnection().setResponse(URL_THIRD, iframe);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for #1589 NullPointerException because of missing context.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"data: hello", "source: true false"})
    public void postMessageFromClick() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function receiveMessage(event) {\n"
            + "    log('data: ' + event.data);\n"
            + "    var win = document.getElementById('myFrame').contentWindow;\n"
            + "    log('source: ' + (event.source === win) + ' ' + (event.source === window));\n"
            + "  }\n"

            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe id='myFrame' src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body>\n"
            + "  <button id='clickme' onclick='top.postMessage(\"hello\", \"*\");'>Click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_THIRD, iframe);
        final WebDriver driver = loadPage2(html);
        driver.switchTo().frame("myFrame");
        driver.findElement(By.id("clickme")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("sync: false")
    public void postMessageSyncOrAsync() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var sync = true;\n"
            + "  function receiveMessage(event) {\n"
            + "    log('sync: ' + sync);\n"
            + "  }\n"
            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage('hello', '*');\n"
            + "  top.sync = false;\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("received")
    public void postMessage_exactURL() throws Exception {
        postMessage(URL_FIRST.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void postMessage_slash() throws Exception {
        postMessage("/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("received")
    public void postMessageSameOrigin_slash() throws Exception {
        postMessageSameOrigin("/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void postMessage_otherHost() throws Exception {
        postMessage("http://127.0.0.1:" + PORT + "/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void postMessageSameOrigin_otherHost() throws Exception {
        postMessageSameOrigin("http://127.0.0.1:" + PORT + "/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void postMessage_otherPort() throws Exception {
        postMessage("http://localhost:" + (PORT + 1) + "/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void postMessageSameOrigin_otherPort() throws Exception {
        postMessageSameOrigin("http://localhost:" + (PORT + 1) + "/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void postMessage_otherProtocol() throws Exception {
        postMessage("https://localhost:" + PORT + "/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void postMessageSameOrigin_otherProtocol() throws Exception {
        postMessageSameOrigin("https://localhost:" + PORT + "/");
    }

    private void postMessage(final String url) throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function receiveMessage(event) {\n"
            + "    log('received');\n"
            + "  }\n"
            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage('hello', '" + url + "');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_THIRD, iframe);
        loadPageVerifyTitle2(html, getExpectedAlerts());
    }

    private void postMessageSameOrigin(final String url) throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function receiveMessage(event) {\n"
            + "    log('received');\n"
            + "  }\n"
            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage('hello', '" + url + "');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        loadPageVerifyTitle2(html, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void postMessageTargetOriginNotUrl() throws Exception {
        postMessageInvalidTargetOrigin("abcd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void postMessageTargetOriginEmpty() throws Exception {
        postMessageInvalidTargetOrigin("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void postMessageTargetOriginSubpath() throws Exception {
        postMessageInvalidTargetOrigin("/abc");
    }

    private void postMessageInvalidTargetOrigin(final String targetOrigin) throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    window.postMessage('hello', '" + targetOrigin + "');\n"
            + "  } catch (e) {\n"
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("data: 2")
    public void postMessage_jsonPayload() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function receiveMessage(event) {\n"
            + "    log('data: ' + event.data.outer);\n"
            + "  }\n"

            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage({outer: 2}, '*');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_THIRD, iframe);
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("data: innerProperty")
    public void postMessage_jsonPayloadWithNestedObjects() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function receiveMessage(event) {\n"
            + "    log('data: ' + event.data.inner.property);\n"
            + "  }\n"

            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe src='" + URL_THIRD + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage({inner: {property: 'innerProperty'}}, '*');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_THIRD, iframe);
        loadPageVerifyTitle2(html);
    }

}
