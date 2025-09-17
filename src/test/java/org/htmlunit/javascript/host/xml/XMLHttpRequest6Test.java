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
package org.htmlunit.javascript.host.xml;

import java.nio.charset.Charset;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XMLHttpRequest} data url handling.
 *
 * @author Ronald Brill
 */
public class XMLHttpRequest6Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo", "send done"})
    public void sendDataUrl_Get() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('GET', 'data:text/plain;charset=utf-8,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send();\n"
                + "    log('send done');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"send done", "4", "200", "foo"})
    public void sendDataUrl_GetAsync() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('GET', 'data:text/plain;charset=utf-8,foo', true);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send();\n"
                + "    log('send done');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo"})
    public void sendDataUrl_GetEncoded() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('GET', 'data:text/plain;base64,Zm9v', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send();\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo"})
    public void sendDataUrl_GetSimpleTextUrl() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('GET', 'data:,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send();\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo"})
    public void sendDataUrl_GetSimpleTextUrlMediaType() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('GET', 'data:text/plain,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send('text to get');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo"})
    public void sendDataUrl_Post() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('POST', 'data:text/plain;charset=utf-8,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send('text to post');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo"})
    public void sendDataUrl_Put() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('PUT', 'data:text/plain;charset=utf-8,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send('text to put');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo"})
    public void sendDataUrl_Delete() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('DELETE', 'data:text/plain;charset=utf-8,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send('text to delete');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo"})
    public void sendDataUrl_Patch() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('PATCH', 'data:text/plain;charset=utf-8,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send('text to patch');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", ""})
    public void sendDataUrl_Head() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('HEAD', 'data:text/plain;charset=utf-8,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send('text to head');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "200", "foo"})
    public void sendDataUrl_Options() throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head>"
                + "</head>"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    let xhr = new XMLHttpRequest();\n"
                + "    xhr.open('OPTIONS', 'data:text/plain;charset=utf-8,foo', false);\n"
                + "    xhr.onload = function() {\n"
                + "      log(xhr.readyState);\n"
                + "      log(xhr.status);\n"
                + "      log(xhr.responseText);\n"
                + "    };\n"
                + "    xhr.send('text to options');\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(requestCount + 1, getMockWebConnection().getRequestCount());
    }
}
