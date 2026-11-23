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

    /**
     * The content type of data urls is serialized like the browsers do it.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;charset=utf-8 | text/html;charset=UTF-8 | text/html | "
                + "text/plain;charset=US-ASCII | text/plain;charset=utf-8 | text/plain;charset=US-ASCII | "
                + "text/plain;a=b;a=c;charset=foo | text/plain | ",
            FF = "text/plain;charset=utf-8 | text/html;charset=UTF-8 | text/html | "
                + "text/plain;charset=US-ASCII | text/plain;charset=utf-8 | text/plain;charset=US-ASCII | "
                + "text/plain;charset=foo;a=b | text/plain | ",
            FF_ESR = "text/plain;charset=utf-8 | text/html;charset=UTF-8 | text/html | "
                + "text/plain;charset=US-ASCII | text/plain;charset=utf-8 | text/plain;charset=US-ASCII | "
                + "text/plain;charset=foo;a=b | text/plain | ")
    public void sendDataUrl_ContentType() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  const urls = ['data:text/plain;charset=utf-8,x', 'data:Text/HTML;Charset=UTF-8,x',\n"
                + "                'data:text/html,x', 'data:,x', 'data:;charset=utf-8,x', 'data:invalid,x',\n"
                + "                'data:text/plain;charset=foo;a=b;a=c,x', 'data:text/plain;base64,eA=='];\n"
                + "  let result = '';\n"
                + "  for (const url of urls) {\n"
                + "    try {\n"
                + "      const xhr = new XMLHttpRequest();\n"
                + "      xhr.open('GET', url, false);\n"
                + "      xhr.send();\n"
                + "      result += xhr.getResponseHeader('content-type') + ' | ';\n"
                + "    } catch (e) { result += e.name + ' | '; }\n"
                + "  }\n"
                + "  log(result);\n"
                + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The response text of data urls without charset is decoded as UTF-8.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1:228 1:228 2:195 2:195 ")
    public void sendDataUrl_ResponseTextCharset() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  const urls = ['data:text/plain,%C3%A4', 'data:text/plain;charset=utf-8,%C3%A4',\n"
                + "                'data:text/plain;charset=iso-8859-1,%C3%A4', 'data:,%C3%A4'];\n"
                + "  let result = '';\n"
                + "  for (const url of urls) {\n"
                + "    try {\n"
                + "      const xhr = new XMLHttpRequest();\n"
                + "      xhr.open('GET', url, false);\n"
                + "      xhr.send();\n"
                + "      const text = xhr.responseText;\n"
                + "      result += text.length + ':' + text.charCodeAt(0) + ' ';\n"
                + "    } catch (e) { result += e.name + ' '; }\n"
                + "  }\n"
                + "  log(result);\n"
                + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The content type of a data url with a second charset parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a=b;charset=foo",
            FF = "text/plain;charset=foo;a=b",
            FF_ESR = "text/plain;charset=foo;a=b")
    public void sendDataUrl_ContentTypeDuplicateCharset() throws Exception {
        sendDataUrlContentType("data:text/plain;charset=foo;a=b;charset=bar,x");
    }

    /**
     * The content type of a data url with the charset parameter between other parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a=b;c=d;charset=foo",
            FF = "text/plain;a=b;charset=foo;c=d",
            FF_ESR = "text/plain;a=b;charset=foo;c=d")
    public void sendDataUrl_ContentTypeCharsetInTheMiddle() throws Exception {
        sendDataUrlContentType("data:text/plain;a=b;charset=foo;c=d,x");
    }

    /**
     * The content type of a data url with two parameters with the same name in different case.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;A=b;a=c",
            FF = "text/plain;a=b",
            FF_ESR = "text/plain;a=b")
    public void sendDataUrl_ContentTypeDuplicateNameCase() throws Exception {
        sendDataUrlContentType("data:text/plain;A=b;a=c,x");
    }

    /**
     * The content type of a data url with an ignored (empty) parameter followed by a valid one with the same name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a=;a=b",
            FF = "text/plain;a=b",
            FF_ESR = "text/plain;a=b")
    public void sendDataUrl_ContentTypeDuplicateAfterEmpty() throws Exception {
        sendDataUrlContentType("data:text/plain;a=;a=b,x");
    }

    /**
     * The content type of a data url with a value with a space followed by a parameter with the same name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a=b c;a=d",
            FF = "text/plain;a=\"b c\"",
            FF_ESR = "text/plain;a=\"b c\"")
    public void sendDataUrl_ContentTypeDuplicateAfterSpace() throws Exception {
        sendDataUrlContentType("data:text/plain;a=b c;a=d,x");
    }

    /**
     * The content type of a data url with a quoted value containing a semicolon followed by a parameter
     * with the same name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a=\"b\";a=d",
            FF = "text/plain;a=\"b;c\"",
            FF_ESR = "text/plain;a=\"b;c\"")
    public void sendDataUrl_ContentTypeDuplicateQuoted() throws Exception {
        sendDataUrlContentType("data:text/plain;a=\"b;c\";a=d,x");
    }

    /**
     * The content type of a data url with a parameter with a space in the name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a b=c;d=e",
            FF = "text/plain;d=e",
            FF_ESR = "text/plain;d=e")
    public void sendDataUrl_ContentTypeInvalidName() throws Exception {
        sendDataUrlContentType("data:text/plain;a b=c;d=e,x");
    }

    /**
     * The content type of a data url with a parameter with an invalid character as name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;@=x;d=e",
            FF = "text/plain;d=e",
            FF_ESR = "text/plain;d=e")
    public void sendDataUrl_ContentTypeInvalidNameChar() throws Exception {
        sendDataUrlContentType("data:text/plain;@=x;d=e,x");
    }

    /**
     * The content type of a data url with spaces around the equals sign of a parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain;c=d")
    public void sendDataUrl_ContentTypeSpaceAroundEquals() throws Exception {
        sendDataUrlContentType("data:text/plain;a = b;c=d,x");
    }

    /**
     * The content type of a data url with an empty charset parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain")
    public void sendDataUrl_ContentTypeEmptyCharset() throws Exception {
        sendDataUrlContentType("data:text/plain;charset=,x");
    }

    /**
     * The content type of a data url with a quoted charset parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain;charset=utf-8")
    public void sendDataUrl_ContentTypeQuotedCharset() throws Exception {
        sendDataUrlContentType("data:text/plain;charset=\"utf-8\",x");
    }

    /**
     * The content type of a data url with a quoted value containing an escaped quote.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain;a=\"b\\\"c\";d=e")
    public void sendDataUrl_ContentTypeQuotedBackslash() throws Exception {
        sendDataUrlContentType("data:text/plain;a=\"b\\\\\"c\";d=e,x");
    }

    /**
     * The content type of a data url with spaces around the parameters (no base64).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain;a=b;c=d")
    public void sendDataUrl_ContentTypeSpacesNoBase64() throws Exception {
        sendDataUrlContentType("data:text/plain ; a=b ; c=d,x");
    }

    /**
     * The content type of a data url with spaces and an upper case base64.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"text/plain;a=\"b c\"",
                                "x"},
            FF = {"text/plain;a=bc",
                  "x"},
            FF_ESR = {"text/plain;a=bc",
                      "x"})
    public void sendDataUrl_ContentTypeSpacesBase64UpperCase() throws Exception {
        sendDataUrlContentTypeAndText("data:text/plain ; a=\"b c\" ; BASE64,eA==");
    }

    /**
     * The content type of a data url with base64 not as the last parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain;a=b",
                      "eA=="})
    public void sendDataUrl_ContentTypeBase64NotLast() throws Exception {
        sendDataUrlContentTypeAndText("data:text/plain;base64;a=b,eA==");
    }

    private void sendDataUrlContentType(final String url) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    const xhr = new XMLHttpRequest();\n"
                + "    xhr.open('GET', '" + url + "', false);\n"
                + "    xhr.send();\n"
                + "    log(xhr.getResponseHeader('content-type'));\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    private void sendDataUrlContentTypeAndText(final String url) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    const xhr = new XMLHttpRequest();\n"
                + "    xhr.open('GET', '" + url + "', false);\n"
                + "    xhr.send();\n"
                + "    log(xhr.getResponseHeader('content-type'));\n"
                + "    log(xhr.responseText);\n"
                + "  } catch (e) { logEx(e); }\n"
                + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }
}
