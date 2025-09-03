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
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

/**
 * Tests for {@link BroadcastChannel}.
 *
 * @author Ronald Brill
 */
public class BroadcastChannelTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"channel1", "i got [object MessageEvent]", "i got data Hello from main page!",
             "i got origin http://localhost:22222", "i got lastEventId ",
             "i got source null", "i got ports ",
             "got [object MessageEvent]", "got data post: Response from iframe",
             "got origin http://localhost:22222", "got lastEventId ", "got source null", "got ports "})
    public void basicBroadcastTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<iframe src='" + URL_SECOND + "'></iframe>\n"
                + "<script>\n"
                + LOG_SESSION_STORAGE_FUNCTION
                + "  var bc = new BroadcastChannel('channel1');\n"
                + "  log(bc.name);\n"
                + "  var ifr = document.querySelector('iframe');\n"
                + "  function iframeLoaded() {\n"
                + "    bc.postMessage('Hello from main page!');\n"
                + "  }\n"
                + "  ifr.addEventListener('load', iframeLoaded, false);\n"
                + "  bc.onmessage = function(e) {\n"
                + "    log('got ' + e);\n"
                + "    log('got data ' + e.data);\n"
                + "    log('got origin ' + e.origin);\n"
                + "    log('got lastEventId ' + e.lastEventId);\n"
                + "    log('got source ' + e.source);\n"
                + "    log('got ports ' + e.ports);\n"
                + "  };\n"
                + "</script>\n"
                + "</body></html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_SESSION_STORAGE_FUNCTION
                + "  var bc = new BroadcastChannel('channel1');\n"
                + "  bc.onmessage = function(e) {\n"
                + "    log('i got ' + e);\n"
                + "    log('i got data ' + e.data);\n"
                + "    log('i got origin ' + e.origin);\n"
                + "    log('i got lastEventId ' + e.lastEventId);\n"
                + "    log('i got source ' + e.source);\n"
                + "    log('i got ports ' + e.ports);\n"
                + "    bc.postMessage('post: Response from iframe');\n"
                + "  };\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);
        verifySessionStorage2(driver, getExpectedAlerts());
        // to start always with fresh session storage
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"channel1", "channel2", "bc1: Message for channel1", "bc2: Message for channel2"})
    public void differentChannelsTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<iframe src='" + URL_SECOND + "'></iframe>\n"
                + "<script>\n"
                + LOG_SESSION_STORAGE_FUNCTION
                + "  var bc1 = new BroadcastChannel('channel1');\n"
                + "  log(bc1.name);\n"

                + "  var bc2 = new BroadcastChannel('channel2');\n"
                + "  log(bc2.name);\n"

                + "  bc1.onmessage = function(e) {\n"
                + "    log('bc1: ' + e.data);\n"
                + "  };\n"
                + "  bc2.onmessage = function(e) {\n"
                + "    log('bc2: ' + e.data);\n"
                + "  };\n"
                + "</script>\n"
                + "</body></html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_SESSION_STORAGE_FUNCTION
                + "  var bc1 = new BroadcastChannel('channel1');\n"
                + "  bc1.postMessage('Message for channel1');\n"

                + "  var bc2 = new BroadcastChannel('channel2');\n"
                + "  bc2.postMessage('Message for channel2');\n"

                + "  var bc3 = new BroadcastChannel('channel3');\n"
                + "  bc3.postMessage('Message for channel3');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);
        verifySessionStorage2(driver, getExpectedAlerts());
        // to start always with fresh session storage
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"Trigger close()", "postMessage() done", "done"})
    public void closeChannelTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<iframe src='" + URL_SECOND + "'></iframe>\n"
                + "<script>\n"
                + LOG_SESSION_STORAGE_FUNCTION
                + "  var bc = new BroadcastChannel('test');\n"
                + "  var ifr = document.querySelector('iframe');\n"
                + "  bc.onmessage = function(e) {\n"
                + "    log(e.data);\n"
                + "    bc.close();\n"
                + "  };\n"
                + "</script>\n"
                + "</body></html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_SESSION_STORAGE_FUNCTION
                + "  var bc = new BroadcastChannel('test');\n"
                + "  // This message should not be received after close\n"
                + "  bc.postMessage('Trigger close()');\n"
                + "  setTimeout(function() {\n"
                + "    bc.postMessage('Should not receive this');\n"
                + "    log('postMessage() done');\n"
                + "  }, 50);\n"
                + "  setTimeout(function() {\n"
                + "    log('done');\n"
                + "  }, 100);\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);
        Thread.sleep(DEFAULT_WAIT_TIME.dividedBy(2).toMillis());
        verifySessionStorage2(driver, getExpectedAlerts());
        // to start always with fresh session storage
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("cross-origin")
    public void sameOrigin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  var bc = new BroadcastChannel('test');\n"
                + "  bc.onmessage = function(e) {\n"
                + "    log(e.data);\n"
                + "  };\n"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + "  var bc = new BroadcastChannel('test');\n"
                + "  bc.postMessage('cross-origin');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);
        final Object[] windowHandles = driver.getWindowHandles().toArray();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(URL_SECOND.toExternalForm());
        driver.close();
        driver.switchTo().window((String) windowHandles[0]);

        verifyTitle2(driver, getExpectedAlerts());
        // to start always with fresh session storage
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({})
    public void crossOrigin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var bc = new BroadcastChannel('test');\n"
                + "  bc.onmessage = function(e) {\n"
                + "    log(e.data);\n"
                + "  };\n"
                + "</script>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + "  var bc = new BroadcastChannel('test');\n"
                + "  bc.postMessage('cross-origin');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_THIRD, html2);

        final WebDriver driver = loadPage2(html);
        final Object[] windowHandles = driver.getWindowHandles().toArray();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(URL_THIRD.toExternalForm());
        driver.close();
        driver.switchTo().window((String) windowHandles[0]);

        verifyTitle2(driver, getExpectedAlerts());
        // to start always with fresh session storage
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("postMessage done")
    public void noSelfMessageTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var bc = new BroadcastChannel('selftest');\n"
                + "  bc.onmessage = function(e) {\n"
                + "    log('received: ' + e.data);\n"
                + "  };\n"
                + "  bc.postMessage('self message');\n"
                + "  log('postMessage done');\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void constructorWithNullTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    var bc = new BroadcastChannel(null);\n"
                + "    log(bc.name);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    @HtmlUnitNYI(CHROME = "TypeError",
            EDGE = "TypeError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    public void constructorWithUndefinedTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    var bc = new BroadcastChannel(undefined);\n"
                + "    log(bc.name);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void constructorWithEmptyStringTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    var bc = new BroadcastChannel('');\n"
                + "    log(bc.name);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("\\s\\sa\\sb\\s\\sc\\texu\\s\\s\\s\\s\\t\\s")
    public void constructorWithStringWhitespaceTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION_NORMALIZE
                + "  try {\n"
                + "    var bc = new BroadcastChannel('  a b  c\texu    \t ');\n"
                + "    log(bc.name);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("123")
    public void constructorWithNumberTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    var bc = new BroadcastChannel(123);\n"
                + "    log(bc.name);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("custom")
    public void constructorWithObjectTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    var bc = new BroadcastChannel({toString: function() { return 'custom'; }});\n"
                + "    log(bc.name);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("special!@#$%^&*()_+-=[]{}|;':\",./&lt;&gt;?`~")
    public void constructorWithSpecialCharactersTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    var bc = new BroadcastChannel('special!@#$%^&*()_+-=[]{}|;\\':\\\",./&lt;&gt;?`~');\n"
                + "    log(bc.name);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("TypeError")
    public void constructorWithNoArgumentsTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    var bc = new BroadcastChannel();\n"
                + "    log('success: ' + bc.name);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("message received")
    public void constructorWithWhitespaceChannelsTest() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<iframe src='" + URL_SECOND + "'></iframe>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var bc = new BroadcastChannel('  spaced  ');\n"
                + "  var ifr = document.querySelector('iframe');\n"
                + "  function iframeLoaded() {\n"
                + "    bc.postMessage('test message');\n"
                + "  }\n"
                + "  ifr.addEventListener('load', iframeLoaded, false);\n"
                + "  bc.onmessage = function(e) {\n"
                + "    log('message received');\n"
                + "  };\n"
                + "</script>\n"
                + "</body></html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + "  var bc = new BroadcastChannel('  spaced  ');\n"
                + "  bc.onmessage = function(e) {\n"
                + "    bc.postMessage('response');\n"
                + "  };\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        loadPageVerifyTitle2(html);
    }
}
