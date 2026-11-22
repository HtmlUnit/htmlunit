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
package org.htmlunit.javascript.host.fetch;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebRequest;
import org.htmlunit.javascript.host.xml.XMLHttpRequest2Test;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Tests for Fetch API.
 *
 * @author Ronald Brill
 */
public class FetchTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true", "text/xml;charset=iso-8859-1",
             "<xml><content>blah</content></xml>"})
    public void fetchGet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "')\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.text();"
            + "         })\n"
            + "        .then(text => log(text))\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<xml><content>blah</content></xml>";
        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fetchGetWithBody() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      fetch('" + URL_SECOND + "', {\n"
            + "        method: 'GET',\n"
            + "        body: 'test data'\n"
            + "      })\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.text();"
            + "         })\n"
            + "        .then(text => log(text))\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "<response/>", MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_FIRST, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "done",
            FF = {"done", "TypeError"},
            FF_ESR = {"done", "TypeError"})
    public void fetchGetNotExistingUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      fetch('https://this.does.not.exist/htmlunit')\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.text();"
            + "         })\n"
            + "        .then(text => log(text))\n"
            + "        .catch(e => logEx(e));\n"
            + "        log('done');\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "<response/>", MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * Tests fetch with different HTTP methods.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true", "text/xml;charset=iso-8859-1", "<response/>"})
    public void fetchPost() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "', {\n"
            + "        method: 'POST',\n"
            + "        body: 'test data'\n"
            + "      })\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.text();"
            + "         })\n"
            + "        .then(text => log(text))\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "<response/>", MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
        assertEquals("test data", getMockWebConnection().getLastWebRequest().getRequestBody());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true",
             "text/plain;charset=iso-8859-1", "bla\\sbla"})
    public void fetchGetText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "')\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.text();"
            + "         })\n"
            + "        .then(text => log(text))\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "bla bla", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true",
             "application/json;charset=iso-8859-1", "{\\s'Html':\\s'Unit'\\s}"})
    public void fetchGetJsonText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "')\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.text();"
            + "         })\n"
            + "        .then(text => log(text))\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        final String json = "{ 'Html': 'Unit' }";
        getMockWebConnection().setResponse(URL_SECOND, json, MimeType.APPLICATION_JSON);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true",
             "application/json;charset=iso-8859-1",
             "[object\\sObject]", "Unit", "{\"Html\":\"Unit\"}"})
    public void fetchGetJson() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "')\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.json();"
            + "         })\n"
            + "        .then(json => {\n"
            + "          log(json);\n"
            + "          log(json.Html);\n"
            + "          log(JSON.stringify(json));\n"
            + "        })\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        final String json = "{ \"Html\": \"Unit\" }";
        getMockWebConnection().setResponse(URL_SECOND, json, MimeType.APPLICATION_JSON);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true", "text/plain;charset=iso-8859-1",
             "[object\\sBlob]", "4", "text/plain;charset=iso-8859-1"})
    public void fetchGetBlob() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "')\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.blob();"
            + "         })\n"
            + "        .then(blob => {\n"
            + "          log(blob);\n"
            + "          log(blob.size);\n"
            + "          log(blob.type);\n"
            + "        })\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "ABCD", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true", "text/plain;charset=iso-8859-1",
             "[object\\sArrayBuffer]", "4"})
    public void fetchGetArrayBuffer() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "')\n"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          return response.arrayBuffer();"
            + "         })\n"
            + "        .then(buffer => {\n"
            + "          log(buffer);\n"
            + "          log(buffer.byteLength);\n"
            + "        })\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "ABCD", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true"})
    public void fetchGetCustomHeader() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "', {\n"
            + "        method: 'GET',\n"
            + "        headers: {\n"
            + "          'Content-Type': 'application/json',\n"
            + "          'X-Custom-Header': 'x-test'\n"
            + "        }\n"
            + "      })"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "         })\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        final String json = "{ \"Html\": \"Unit\" }";
        getMockWebConnection().setResponse(URL_SECOND, json, MimeType.APPLICATION_JSON);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals(URL_SECOND, lastRequest.getUrl());

        assertEquals("x-test", lastRequest.getAdditionalHeader("X-Custom-Header"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true", "text/plain;charset=iso-8859-1", "x-tEsT"})
    public void fetchGetCustomResponseHeader() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      fetch('" + URL_SECOND + "', {\n"
            + "        method: 'GET',\n"
            + "        headers: {\n"
            + "          'Content-Type': 'application/json',\n"
            + "          'X-Custom-Header': 'x-test'\n"
            + "        }\n"
            + "      })"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "          log(response.headers.get('content-type'));\n"
            + "          log(response.headers.get('X-Custom-Header'));\n"
            + "         })\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("X-Custom-Header", "x-tEsT"));
        getMockWebConnection().setResponse(URL_SECOND, "HtmlUnit", 200, "ok", MimeType.TEXT_PLAIN, headers);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals(URL_SECOND, lastRequest.getUrl());

        assertEquals("x-test", lastRequest.getAdditionalHeader("X-Custom-Header"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true"})
    public void fetchPostFormData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"

            + "    <form name='testForm'>\n"
            + "      <input type='text' name='myText' value='HtmlUnit'>\n"
            + "    </form>\n"

            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      let formData = new FormData(document.testForm);"

            + "      fetch('" + URL_SECOND + "', {\n"
            + "        method: 'POST',\n"
            + "        body: formData\n"
            + "      })"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "         })\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "HtmlUnit", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals(URL_SECOND, lastRequest.getUrl());

        assertTrue(lastRequest.getRequestBody(), lastRequest.getRequestBody()
                                .contains("Content-Disposition: form-data; name=\"myText\""));
        assertTrue(lastRequest.getRequestBody(), lastRequest.getRequestBody()
                .contains("HtmlUnit"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true", "test0,Hello1\\nHello1,test1,Hello2\\nHello2"})
    public void fetchMultipartFormData() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "  <body>\n"
                + "    <script>\n"
                + LOG_TITLE_FUNCTION_NORMALIZE
                + "      fetch('" + URL_SECOND + "')"
                + "        .then(response => {\n"
                + "          log(response.status);\n"
                + "          log(response.statusText);\n"
                + "          log(response.ok);\n"

                + "          return response.formData();\n"
                + "        })\n"
                + "        .then(formData => {\n"
                + "            log(Array.from(formData.entries()));\n"
                + "        })\n"
                + "        .catch(e => log(e.message));\n"
                + "    </script>\n"
                + "  </body>\n"
                + "</html>";

        final String boundary = "0123456789";
        final String content = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"test0\"\r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n"
                + "Hello1\nHello1\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"test1\"\r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n"
                + "Hello2\nHello2\r\n"
                + "--" + boundary + "--";

        getMockWebConnection().setResponse(URL_SECOND, content, "multipart/form-data; boundary=" + boundary);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true"})
    public void fetchPostURLSearchParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      let searchParams = new URLSearchParams();\n"
            + "      searchParams.append('q', 'HtmlUnit');\n"
            + "      searchParams.append('page', '1');\n"
            + "      fetch('" + URL_SECOND + "', {\n"
            + "        method: 'POST',\n"
            + "        body: searchParams\n"
            + "      })"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "         })\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "HtmlUnit", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals(URL_SECOND, lastRequest.getUrl());

        String headerContentType = lastRequest.getAdditionalHeaders().get(HttpHeader.CONTENT_TYPE);
        headerContentType = headerContentType.split(";")[0];
        assertEquals("application/x-www-form-urlencoded", headerContentType);

        final List<NameValuePair> params = lastRequest.getRequestParameters();
        assertEquals(2, params.size());
        assertEquals("q", params.get(0).getName());
        assertEquals("HtmlUnit", params.get(0).getValue());
        assertEquals("page", params.get(1).getName());
        assertEquals("1", params.get(1).getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true"})
    public void fetchPostJSON() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "      let jsonData = {hello: 'world'};\n"
            + "      fetch('" + URL_SECOND + "', {\n"
            + "        method: 'POST',\n"
            + "        headers: {\n"
            + "          'Content-Type': 'application/json'\n"
            + "        },\n"
            + "        body: JSON.stringify(jsonData)\n"
            + "      })"
            + "        .then(response => {\n"
            + "          log(response.status);\n"
            + "          log(response.statusText);\n"
            + "          log(response.ok);\n"
            + "         })\n"
            + "        .catch(e => logEx(e));\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "HtmlUnit", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals(URL_SECOND, lastRequest.getUrl());

        String headerContentType = lastRequest.getAdditionalHeaders().get(HttpHeader.CONTENT_TYPE);
        headerContentType = headerContentType.split(";")[0];
        assertEquals("application/json", headerContentType);

        assertEquals("{\"hello\":\"world\"}", lastRequest.getRequestBody());
    }

    /**
     * Tests fetch with credentials.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void fetchWithCredentials() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  fetch('" + URL_SECOND + "', {\n"
            + "    method: 'GET',\n"
            + "    credentials: 'include'\n"
            + "  })\n"
            + "    .then(response => log(response.ok ? 'ok' : 'not ok'))\n"
            + "    .catch(e => logEx(e));\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("<response/>\n", MimeType.TEXT_XML);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Tests fetch response cloning.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true",
             "text1",
             "text2"})
    public void fetchResponseClone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  fetch('" + URL_SECOND + "')\n"
            + "    .then(response => {\n"
            + "      var cloned = response.clone();\n"
            + "      log(cloned.ok);\n"
            + "      return Promise.all([\n"
            + "        response.text(),\n"
            + "        cloned.text()\n"
            + "      ]);\n"
            + "    })\n"
            + "    .then(texts => {\n"
            + "      log('text' + (texts[0] === texts[1] ? '1' : '0'));\n"
            + "      log('text2');\n"
            + "    })\n"
            + "    .catch(e => logEx(e));\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("response body", MimeType.TEXT_PLAIN);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Tests fetch with mode option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void fetchWithMode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  fetch('" + URL_SECOND + "', {\n"
            + "    method: 'GET',\n"
            + "    mode: 'cors'\n"
            + "  })\n"
            + "    .then(response => log(response.ok ? 'ok' : 'not ok'))\n"
            + "    .catch(e => logEx(e));\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("<response/>\n", MimeType.TEXT_XML);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Tests fetch with cache option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void fetchWithCache() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  fetch('" + URL_SECOND + "', {\n"
            + "    method: 'GET',\n"
            + "    cache: 'no-cache'\n"
            + "  })\n"
            + "    .then(response => log(response.ok ? 'ok' : 'not ok'))\n"
            + "    .catch(e => logEx(e));\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("<response/>\n", MimeType.TEXT_XML);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Tests fetch response with text encoding.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ol\u00E9")
    public void fetchResponseTextEncoding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        fetch('" + URL_SECOND + "')\n"
            + "          .then(response => response.text())\n"
            + "          .then(text => log(text))\n"
            + "          .catch(e => logEx(e));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String response = "olé";
        final byte[] responseBytes = response.getBytes(UTF_8);

        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK",
            MimeType.TEXT_HTML, new ArrayList<>());
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200",
             "true",
             "<response/>"})
    public void fetchRequestObject() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const req = new Request('" + URL_SECOND + "', {method: 'POST', body: 'abc'});\n"
            + "  fetch(req)\n"
            + "    .then(r => { log(r.status); log(req.bodyUsed); return r.text(); })\n"
            + "    .then(t => log(t))\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "<response/>", MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals(URL_SECOND, lastRequest.getUrl());
        assertEquals("abc", lastRequest.getRequestBody());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true",
             "true",
             "basic",
             "§§URL§§",
             "false",
             "false",
             "[object ReadableStream]",
             "TypeError",
             "null"})
    public void responseProperties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const p = fetch('" + URL_SECOND + "#hash');\n"
            + "  log(p instanceof Promise);\n"
            + "  p.then(r => {\n"
            + "      log(r instanceof Response);\n"
            + "      log(r.type);\n"
            + "      log(r.url);\n"
            + "      log(r.redirected);\n"
            + "      log(r.bodyUsed);\n"
            + "      log(Object.prototype.toString.call(r.body));\n"
            + "      try { r.headers.set('x-a', 'b'); log('set'); } catch (e) { logEx(e); }\n"
            + "      log(r.headers.get('set-cookie'));\n"
            + "    })\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Set-Cookie", "c=v"));
        getMockWebConnection().setResponse(URL_SECOND, "x", 200, "OK", MimeType.TEXT_PLAIN, headers);

        expandExpectedAlertsVariables(URL_SECOND);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"404",
             "false",
             "Not Found",
             "missing"})
    public void status404() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "')\n"
            + "    .then(r => { log(r.status); log(r.ok); log(r.statusText); return r.text(); })\n"
            + "    .then(t => log(t))\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "missing", 404, "Not Found", MimeType.TEXT_PLAIN,
                new ArrayList<>());

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"200", "[object ReadableStream]", "[]"},
            FF = {"200", "null", "[]"},
            FF_ESR = {"200", "null", "[]"})
    @HtmlUnitNYI(CHROME = {"200", "null", "[]"},
            EDGE = {"200", "null", "[]"})
    public void head() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "', {method: 'HEAD'})\n"
            + "    .then(r => { log(r.status); log(r.body); return r.text(); })\n"
            + "    .then(t => log('[' + t + ']'))\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "content", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals("HEAD", getMockWebConnection().getLastWebRequest().getHttpMethod().name());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200",
             "true",
             "§§URL§§target",
             "target content"})
    public void redirectFollow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_FIRST + "redirect', {method: 'POST', body: 'x'})\n"
            + "    .then(r => { log(r.status); log(r.redirected); log(r.url); return r.text(); })\n"
            + "    .then(t => log(t))\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", "/target"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "redirect"), "", 302, "Found",
                MimeType.TEXT_PLAIN, headers);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "target"), "target content",
                MimeType.TEXT_PLAIN);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        // 302 for POST switches to GET
        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals(new URL(URL_FIRST, "target"), lastRequest.getUrl());
        assertEquals("GET", lastRequest.getHttpMethod().name());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError",
             "opaqueredirect 0 [] false false",
             "§§URL§§redirect",
             "null",
             "null"})
    public void redirectErrorAndManual() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_FIRST + "redirect', {redirect: 'error'})\n"
            + "    .then(r => log(r.status), e => logEx(e))\n"
            + "    .then(() => fetch('" + URL_FIRST + "redirect', {redirect: 'manual'}))\n"
            + "    .then(r => {\n"
            + "      log(r.type + ' ' + r.status + ' [' + r.statusText + '] ' + r.ok + ' ' + r.redirected);\n"
            + "      log(r.url);\n"
            + "      log(r.headers.get('location'));\n"
            + "      log(r.body);\n"
            + "    })\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", "/target"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "redirect"), "", 302, "Found",
                MimeType.TEXT_PLAIN, headers);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "target"), "target content",
                MimeType.TEXT_PLAIN);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"AbortError",
             "my reason"})
    public void abortBeforeFetch() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "', {signal: AbortSignal.abort()})\n"
            + "    .then(r => log(r.status), e => log(e.name))\n"
            + "    .then(() => fetch('" + URL_SECOND + "', {signal: AbortSignal.abort('my reason')}))\n"
            + "    .then(r => log(r.status), e => log(e));\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "x", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"aborted",
             "AbortError"})
    public void abortWhileRunning() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const ctrl = new AbortController();\n"
            + "  fetch('" + URL_SECOND + "', {signal: ctrl.signal})\n"
            + "    .then(r => log(r.status), e => log(e.name));\n"
            + "  ctrl.abort();\n"
            + "  log('aborted');\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "x", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError"})
    public void invalidArguments() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  let p;\n"
            + "  try { p = fetch(); log(p instanceof Promise); } catch (e) { logEx(e); }\n"
            + "  p.then(r => log(r.status), e => logEx(e))\n"
            + "    .then(() => fetch('http://user:pwd@localhost/'))\n"
            + "    .then(r => log(r.status), e => logEx(e))\n"
            + "    .then(() => fetch('" + URL_SECOND + "', {method: 'CONNECT'}))\n"
            + "    .then(r => log(r.status), e => logEx(e))\n"
            + "    .then(() => fetch('about:blank'))\n"
            + "    .then(r => log(r.status), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200 basic text/plain;charset=utf-8",
             "Hello World"})
    public void dataUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('data:text/plain;charset=utf-8,Hello%20World')\n"
            + "    .then(r => {\n"
            + "      log(r.status + ' ' + r.type + ' ' + r.headers.get('content-type'));\n"
            + "      return r.text();\n"
            + "    })\n"
            + "    .then(t => log(t))\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("200")
    public void requestHeaders() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "', {method: 'POST', body: 'x', headers: {'X-Custom': 'v'}})\n"
            + "    .then(r => log(r.status))\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "x", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertEquals("*/*", lastRequest.getAdditionalHeader(HttpHeader.ACCEPT));
        assertEquals("v", lastRequest.getAdditionalHeader("X-Custom"));
        assertEquals(URL_FIRST.toExternalForm(), lastRequest.getAdditionalHeader(HttpHeader.REFERER));
        assertEquals("http://localhost:" + PORT, lastRequest.getAdditionalHeader(HttpHeader.ORIGIN));
        assertTrue(lastRequest.getAdditionalHeader(HttpHeader.CONTENT_TYPE).startsWith("text/plain"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            FF = {},
            FF_ESR = {})
    @HtmlUnitNYI(FF = "TypeError",
            FF_ESR = "TypeError")
    public void crossOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_CROSS_ORIGIN + "')\n"
            + "    .then(r => log(r.status), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;charset=utf-8 "
                        + "| text/html;charset=UTF-8 "
                        + "| text/html | text/plain;charset=US-ASCII "
                        + "| text/plain | text/plain;charset=utf-8 "
                        + "| text/plain;charset=US-ASCII "
                        + "| text/plain;a=b;a=c;charset=foo "
                        + "| text/plain "
                        + "| text/plain;a=\"b c\"",
            FF = "text/plain;charset=utf-8 | text/html;charset=UTF-8 | text/html | text/plain;charset=US-ASCII | t"
                + "ext/plain | text/plain;charset=utf-8 | text/plain;charset=US-ASCII | text/plain;charset=foo;a=b "
                + "| text/plain | text/plain;a=bc",
            FF_ESR = "text/plain;charset=utf-8 | text/html;charset=UTF-8 | text/html | text/plain;charset=US-ASCII | t"
                + "ext/plain | text/plain;charset=utf-8 | text/plain;charset=US-ASCII | text/plain;charset=foo;a=b "
                + "| text/plain | text/plain;a=bc")
    public void dataUrlContentType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const urls = ['data:text/plain;charset=utf-8,x', 'data:Text/HTML;Charset=UTF-8,x',\n"
            + "                'data:text/html,x',\n"
            + "                'data:,x', 'data:;,x', 'data:;charset=utf-8,x', 'data:invalid,x',\n"
            + "                'data:text/plain;charset=foo;a=b;a=c,x', 'data:text/plain;base64,eA==',\n"
            + "                'data:text/plain ; a=\"b c\" ;base64,eA=='];\n"
            + "  Promise.all(urls.map(u => fetch(u).then(r => r.headers.get('content-type'), e => e.name)))\n"
            + "    .then(types => log(types.join(' | ')));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * The content type of a data url with a second charset parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a=b;charset=foo",
            FF = "text/plain;charset=foo;a=b",
            FF_ESR = "text/plain;charset=foo;a=b")
    public void dataUrlContentTypeDuplicateCharset() throws Exception {
        dataUrlContentType("data:text/plain;charset=foo;a=b;charset=bar,x");
    }

    /**
     * The content type of a data url with the charset parameter between other parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a=b;c=d;charset=foo",
            FF = "text/plain;a=b;charset=foo;c=d",
            FF_ESR = "text/plain;a=b;charset=foo;c=d")
    public void dataUrlContentTypeCharsetInTheMiddle() throws Exception {
        dataUrlContentType("data:text/plain;a=b;charset=foo;c=d,x");
    }

    /**
     * The type of the blob of a data url with duplicate parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "text/plain;a=b;a=c;charset=foo",
            FF = "text/plain;charset=foo;a=b",
            FF_ESR = "text/plain;charset=foo;a=b")
    public void dataUrlBlobTypeDuplicateParameters() throws Exception {
        dataUrlBlobType("data:text/plain;charset=foo;a=b;a=c,x");
    }

    /**
     * The type of the blob of a data url with upper case letters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain;a=b")
    public void dataUrlBlobTypeUpperCase() throws Exception {
        dataUrlBlobType("data:Text/Plain;A=B,x");
    }

    /**
     * The type of the blob of a data url with a quoted parameter value containing a space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain;a=\"b c\"")
    public void dataUrlBlobTypeQuotedSpace() throws Exception {
        dataUrlBlobType("data:text/plain;a=\"b c\",x");
    }

    private void dataUrlContentType(final String url) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + url + "')\n"
            + "    .then(r => log(r.headers.get('content-type')), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    private void dataUrlBlobType(final String url) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + url + "')\n"
            + "    .then(r => r.blob())\n"
            + "    .then(b => log(b.type), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    // ---------------------------------------------------------------------------
    // integrity
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc",
                      "TypeError",
                      "abc",
                      "abc",
                      "TypeError",
                      "abc",
                      "abc",
                      "abc"})
    public void integrity() throws Exception {
        final String sha256 = "sha256-ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0=";
        final String sha512 = "sha512-3a81oZNherrMQXNJriBBMRLm+k6JqX6iCp7u5ktV05ohkpkqJ0/BqDa6PCOj/uu9RU1EI2Q86A4"
                + "qmslPpUyknw==";
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  const values = ['" + sha256 + "', 'sha256-wrong', '" + sha512 + "',\n"
            + "                  'sha256-wrong " + sha512 + "', '" + sha256 + " sha512-wrong',\n"
            + "                  'md5-abc', '" + sha256.replace('+', '-').replace('/', '_') + "', ''];\n"
            + "  values.reduce((p, v) => p.then(() =>\n"
            + "      fetch(url, {integrity: v}).then(r => r.text()).then(t => log(t), e => logEx(e))),\n"
            + "    Promise.resolve());\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "abc", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    // ---------------------------------------------------------------------------
    // keepalive
    // ---------------------------------------------------------------------------

    /**
     * The body of a keepalive request is limited to 64 KiB.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200",
                      "TypeError",
                      "200"})
    public void keepaliveBodySize() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  fetch(url, {method: 'POST', keepalive: true, body: 'x'.repeat(65536)})\n"
            + "    .then(r => log(r.status), e => logEx(e))\n"
            + "    .then(() => fetch(url, {method: 'POST', keepalive: true, body: 'x'.repeat(65537)}))\n"
            + "    .then(r => log(r.status), e => logEx(e))\n"
            + "    .then(() => fetch(url, {method: 'POST', keepalive: false, body: 'x'.repeat(65537)}))\n"
            + "    .then(r => log(r.status), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "x", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME.multipliedBy(2), driver, getExpectedAlerts());
    }

    // ---------------------------------------------------------------------------
    // referrer
    // ---------------------------------------------------------------------------

    /**
     * The referrer and the referrerPolicy options.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"referer=http://localhost:22222/page?q=1",
                      "referer=null",
                      "referer=http://localhost:22222/other?x=1",
                      "referer=http://localhost:22222/page?q=1",
                      "referer=null",
                      "referer=http://localhost:22222/",
                      "referer=http://localhost:22222/page?q=1",
                      "referer=http://localhost:22222/page?q=1",
                      "referer=http://localhost:22222/"})
    public void referrerOptions() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '/referer';\n"
            + "  const inits = [{}, {referrer: ''}, {referrer: '/other?x=1'}, {referrer: 'about:client'},\n"
            + "                 {referrerPolicy: 'no-referrer'}, {referrerPolicy: 'origin'},\n"
            + "                 {referrerPolicy: 'same-origin'}, {referrerPolicy: 'unsafe-url'},\n"
            + "                 {referrer: '/other', referrerPolicy: 'origin'}];\n"
            + "  inits.reduce((p, init) => p.then(() =>\n"
            + "      fetch(url, init).then(r => r.text()).then(t => log(t), e => logEx(e))),\n"
            + "    Promise.resolve());\n"
            + "</script></head><body></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/referer", RefererServlet.class);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html, new URL(URL_FIRST, "/page?q=1"), servlets);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * The method override header with a forbidden method.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void headerMethodOverrideTrace() throws Exception {
        requestHeader("X-HTTP-Method-Override", "TRACE");
    }

    /**
     * The method override header with a forbidden method (lower case).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void headerMethodOverrideTrack() throws Exception {
        requestHeader("X-Method-Override", "track");
    }

    /**
     * The method override header with a forbidden method.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void headerHttpMethodConnect() throws Exception {
        requestHeader("X-HTTP-Method", "CONNECT");
    }

    /**
     * The method override header with a list containing a forbidden method.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void headerMethodOverrideList() throws Exception {
        requestHeader("X-HTTP-Method-Override", "GET, TRACE");
    }

    /**
     * The method override header with an allowed method.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void headerMethodOverridePut() throws Exception {
        requestHeader("X-HTTP-Method-Override", "PUT");
    }

    /**
     * The User-Agent header (removed from the forbidden headers of the Fetch standard).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "false"},
            FF = {"true", "true"},
            FF_ESR = {"true", "true"})
    public void headerUserAgent() throws Exception {
        requestHeader("User-Agent", "HtmlUnitTest");
    }

    /**
     * The Content-Transfer-Encoding header.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void headerContentTransferEncoding() throws Exception {
        requestHeader("Content-Transfer-Encoding", "HtmlUnitTest");
    }

    /**
     * Fetches with the given header; logs whether the header is part of the request headers
     * and whether the server got the header value.
     */
    private void requestHeader(final String name, final String value) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const req = new Request('/headerEcho?name=" + name + "',\n"
            + "      {headers: {'" + name + "': '" + value + "'}});\n"
            + "  log(req.headers.has('" + name + "'));\n"
            + "  fetch(req).then(r => r.text())\n"
            + "    .then(t => log(t.indexOf('" + value + "') > -1), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/headerEcho", XMLHttpRequest2Test.HeaderEchoServlet.class);

        final WebDriver driver = loadPage2(html, URL_FIRST, servlets);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Answers with the Referer header of the request.
     */
    public static class RefererServlet extends HttpServlet {
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            response.setContentType(MimeType.TEXT_PLAIN);
            response.getWriter().write("referer=" + request.getHeader(HttpHeader.REFERER));
        }
    }

    // ---------------------------------------------------------------------------
    // cache modes
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null"})
    public void cacheDefault() throws Exception {
        cache("{}");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=2;cache-control=no-cache;pragma=no-cache;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null"})
    public void cacheNoStore() throws Exception {
        cache("{cache: 'no-store'}");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=2;cache-control=no-cache;pragma=no-cache;if-none-match=null",
                      "count=2;cache-control=no-cache;pragma=no-cache;if-none-match=null"})
    public void cacheReload() throws Exception {
        cache("{cache: 'reload'}");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null"})
    public void cacheNoCache() throws Exception {
        cache("{cache: 'no-cache'}");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null"})
    public void cacheForceCache() throws Exception {
        cache("{cache: 'force-cache'}");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=1;cache-control=null;pragma=null;if-none-match=null"})
    public void cacheOnlyIfCached() throws Exception {
        cache("{cache: 'only-if-cached', mode: 'same-origin'}");
    }

    /**
     * A request with a conditional header bypasses the cache.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"count=1;cache-control=null;pragma=null;if-none-match=null",
                                "count=2;cache-control=max-age=0;pragma=null;if-none-match=\"other\"",
                                "count=1;cache-control=null;pragma=null;if-none-match=null"},
            FF = {"count=1;cache-control=null;pragma=null;if-none-match=null",
                  "count=2;cache-control=no-cache;pragma=no-cache;if-none-match=\"other\"",
                  "count=1;cache-control=null;pragma=null;if-none-match=null"},
            FF_ESR = {"count=1;cache-control=null;pragma=null;if-none-match=null",
                      "count=2;cache-control=no-cache;pragma=no-cache;if-none-match=\"other\"",
                      "count=1;cache-control=null;pragma=null;if-none-match=null"})
    @HtmlUnitNYI(CHROME = {"count=1;cache-control=null;pragma=null;if-none-match=null",
                           "count=2;cache-control=no-cache;pragma=no-cache;if-none-match=\"other\"",
                           "count=1;cache-control=null;pragma=null;if-none-match=null"},
            EDGE = {"count=1;cache-control=null;pragma=null;if-none-match=null",
                    "count=2;cache-control=no-cache;pragma=no-cache;if-none-match=\"other\"",
                    "count=1;cache-control=null;pragma=null;if-none-match=null"})
    public void cacheConditionalHeader() throws Exception {
        cache("{headers: {'If-None-Match': '\"other\"'}}");
    }

    /**
     * Fetches the (cacheable) resource with the default cache mode, then with the given init
     * and at last again with the default cache mode; logs the answers of the server.
     */
    private void cache(final String init) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '/cache?id=" + System.currentTimeMillis() + "';\n"
            + "  fetch(url)\n"
            + "    .then(r => r.text()).then(t => log(t))\n"
            + "    .then(() => fetch(url, " + init + "))\n"
            + "    .then(r => r.text()).then(t => log(t), e => logEx(e))\n"
            + "    .then(() => fetch(url))\n"
            + "    .then(r => r.text()).then(t => log(t), e => logEx(e))\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        CacheServlet.COUNT_ = 0;
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/cache", CacheServlet.class);

        final WebDriver driver = loadPage2(html, URL_FIRST, servlets);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Answers with a cacheable response (max-age and ETag) that contains the number of the request
     * and the cache headers of the request; answers conditional requests with 304.
     */
    public static class CacheServlet extends HttpServlet {
        private static int COUNT_;

        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            COUNT_++;
            response.setHeader("Cache-Control", "max-age=3600");
            response.setHeader("ETag", "\"v1\"");
            if ("\"v1\"".equals(request.getHeader("If-None-Match"))) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }

            response.setContentType(MimeType.TEXT_PLAIN);
            response.getWriter().write("count=" + COUNT_
                    + ";cache-control=" + request.getHeader("Cache-Control")
                    + ";pragma=" + request.getHeader("Pragma")
                    + ";if-none-match=" + request.getHeader("If-None-Match"));
        }
    }

    // ---------------------------------------------------------------------------
    // mode no-cors
    // ---------------------------------------------------------------------------

    /**
     * A request in mode 'no-cors' to the origin of the page is a normal request.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200 basic", "content"})
    public void noCorsSameOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "',\n"
            + "      {mode: 'no-cors', headers: {'X-Custom': 'v', 'Accept': 'text/plain'}})\n"
            + "    .then(r => { log(r.status + ' ' + r.type); return r.text(); })\n"
            + "    .then(t => log(t), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "content", MimeType.TEXT_PLAIN);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        assertNull(lastRequest.getAdditionalHeader("X-Custom"));
        assertEquals("text/plain", lastRequest.getAdditionalHeader(HttpHeader.ACCEPT));
    }
}

