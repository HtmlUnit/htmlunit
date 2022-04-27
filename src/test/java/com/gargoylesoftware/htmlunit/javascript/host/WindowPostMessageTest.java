package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

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
    @Alerts(DEFAULT = {"type: message", "bubbles: false", "cancelable: false", "data: hello",
                       "origin: ", "source: [object Window]", "lastEventId: "},
            IE = {"type: message", "bubbles: false", "cancelable: false", "data: hello",
                  "origin: ", "source: [object Window]", "lastEventId: undefined"})
    public void postMessage() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        expectedAlerts[4] += "http://localhost:" + PORT;
        setExpectedAlerts(expectedAlerts);

        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function receiveMessage(event) {\n"
            + "    log('type: ' + event.type);\n"
            + "    log('bubbles: ' + event.bubbles);\n"
            + "    log('cancelable: ' + event.cancelable);\n"
            + "    log('data: ' + event.data);\n"
            + "    log('origin: ' + event.origin);\n"
            + "    log('source: ' + event.source);\n"
            + "    log('lastEventId: ' + event.lastEventId);\n"
            + "  }\n"

            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage('hello', '*');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for #1589 NullPointerException because of missing context.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data: hello")
    public void postMessageFromClick() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function receiveMessage(event) {\n"
            + "    alert('data: ' + event.data);\n"
            + "  }\n"

            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe id='myFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body>\n"
            + "  <button id='clickme' onclick='top.postMessage(\"hello\", \"*\");'>Click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        final WebDriver driver = loadPage2(html);
        driver.switchTo().frame("myFrame");
        driver.findElement(By.id("clickme")).click();

        verifyAlerts(driver, getExpectedAlerts());
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
    @Alerts("posted received")
    public void postMessage_exactURL() throws Exception {
        postMessage(URL_FIRST.toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("posted received")
    public void postMessage_slash() throws Exception {
        postMessage("/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("posted")
    public void postMessage_otherHost() throws Exception {
        postMessage("http://127.0.0.1:" + PORT + "/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("posted")
    public void postMessage_otherPort() throws Exception {
        postMessage("http://localhost:" + (PORT + 1) + "/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("posted")
    public void postMessage_otherProtocol() throws Exception {
        postMessage("https://localhost:" + PORT + "/");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void postMessage_invalidTargetOrigin() throws Exception {
        postMessage("abcdefg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void postMessage_emptyTargetOrigin() throws Exception {
        postMessage("");
    }

    private void postMessage(final String url) throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function receiveMessage(event) {\n"
            + "    document.title += ' received';\n"
            + "  }\n"
            + "  window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "  <iframe src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  try {\n"
            + "    top.postMessage('hello', '" + url + "');\n"
            + "    top.document.title += ' posted';\n"
            + "  } catch (e) {\n"
            + "    top.document.title += ' exception';\n"
            + "  }\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        final WebDriver driver = loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
    }

    @Test
    @Alerts({"data: 2"})
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
            + "  <iframe src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage({outer: 2}, '*');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"data: innerProperty"})
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
            + "  <iframe src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "  top.postMessage({inner: {property: 'innerProperty'}}, '*');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        loadPageVerifyTitle2(html);
    }
    
}
