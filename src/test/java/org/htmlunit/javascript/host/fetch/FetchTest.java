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
package org.htmlunit.javascript.host.fetch;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebRequest;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

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
    @Disabled
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
    @Disabled
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
    @Disabled
    @Alerts("TypeError")
    public void fetchGetWrongUrl() throws Exception {
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
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "<response/>", MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * Tests fetch with different HTTP methods.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
    @Alerts(DEFAULT = {"200", "OK", "true", "text/plain;charset=iso-8859-1",
                       "[object\\sBlob]", "4", "text/plain"},
            FF = {"200", "OK", "true", "text/plain;charset=iso-8859-1",
                  "[object\\sBlob]", "4", "text/plain;charset=iso-8859-1"},
            FF_ESR = {"200", "OK", "true", "text/plain;charset=iso-8859-1",
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
    @Alerts({"200", "OK", "true"})
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
                + "         })\n"
                + "        .then(response => {\n"
                + "          return response.formData();\n"
                + "        })\n"
                + "        .then(formData => {\n"
                + "            log(formData.get('test0'));\n"
                + "            log(formData.get('test1'));\n"
                + "        })\n"
                + "        .catch(e => logEx(e));\n"
                + "    </script>\n"
                + "  </body>\n"
                + "</html>";

        final String content = "--0123456789\r\nContent-Disposition: form-data;name=test0\r\nContent-Type: text/plain\r\nHello1\nHello1\r\n--0123456789\r\nContent-Disposition: form-data;name=test1\r\nContent-Type: text/plain\r\nHello2\nHello2\r\n--0123456789--";
        getMockWebConnection().setResponse(URL_SECOND, content, "multipart/form-data; boundary=0123456789");

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        assertEquals(URL_SECOND, getMockWebConnection().getLastWebRequest().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
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
    @Disabled
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

//    /**
//     * Tests fetch with credentials.
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("ok")
//    public void fetchWithCredentials() throws Exception {
//        final String html = DOCTYPE_HTML
//            + "<html><head>\n"
//            + "<script>\n"
//            + LOG_TITLE_FUNCTION
//            + "function test() {\n"
//            + "  fetch('" + URL_SECOND + "', {\n"
//            + "    method: 'GET',\n"
//            + "    credentials: 'include'\n"
//            + "  })\n"
//            + "    .then(response => log(response.ok ? 'ok' : 'not ok'))\n"
//            + "    .catch(e => logEx(e));\n"
//            + "}\n"
//            + "</script>\n"
//            + "</head>\n"
//            + "<body onload='test()'></body></html>";
//
//        getMockWebConnection().setDefaultResponse("<response/>\n", MimeType.TEXT_XML);
//        final WebDriver driver = loadPage2(html);
//        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
//    }
//
//    /**
//     * Tests fetch response cloning.
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts({"true", "text1", "text2"})
//    public void fetchResponseClone() throws Exception {
//        final String html = DOCTYPE_HTML
//            + "<html><head>\n"
//            + "<script>\n"
//            + LOG_TITLE_FUNCTION
//            + "function test() {\n"
//            + "  fetch('" + URL_SECOND + "')\n"
//            + "    .then(response => {\n"
//            + "      var cloned = response.clone();\n"
//            + "      log(cloned.ok);\n"
//            + "      return Promise.all([\n"
//            + "        response.text(),\n"
//            + "        cloned.text()\n"
//            + "      ]);\n"
//            + "    })\n"
//            + "    .then(texts => {\n"
//            + "      log('text' + (texts[0] === texts[1] ? '1' : '0'));\n"
//            + "      log('text2');\n"
//            + "    })\n"
//            + "    .catch(e => logEx(e));\n"
//            + "}\n"
//            + "</script>\n"
//            + "</head>\n"
//            + "<body onload='test()'></body></html>";
//
//        getMockWebConnection().setDefaultResponse("response body", MimeType.TEXT_PLAIN);
//        final WebDriver driver = loadPage2(html);
//        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
//    }
//
//    /**
//     * Tests fetch with mode option.
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("ok")
//    public void fetchWithMode() throws Exception {
//        final String html = DOCTYPE_HTML
//            + "<html><head>\n"
//            + "<script>\n"
//            + LOG_TITLE_FUNCTION
//            + "function test() {\n"
//            + "  fetch('" + URL_SECOND + "', {\n"
//            + "    method: 'GET',\n"
//            + "    mode: 'cors'\n"
//            + "  })\n"
//            + "    .then(response => log(response.ok ? 'ok' : 'not ok'))\n"
//            + "    .catch(e => logEx(e));\n"
//            + "}\n"
//            + "</script>\n"
//            + "</head>\n"
//            + "<body onload='test()'></body></html>";
//
//        getMockWebConnection().setDefaultResponse("<response/>\n", MimeType.TEXT_XML);
//        final WebDriver driver = loadPage2(html);
//        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
//    }
//
//    /**
//     * Tests fetch with cache option.
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("ok")
//    public void fetchWithCache() throws Exception {
//        final String html = DOCTYPE_HTML
//            + "<html><head>\n"
//            + "<script>\n"
//            + LOG_TITLE_FUNCTION
//            + "function test() {\n"
//            + "  fetch('" + URL_SECOND + "', {\n"
//            + "    method: 'GET',\n"
//            + "    cache: 'no-cache'\n"
//            + "  })\n"
//            + "    .then(response => log(response.ok ? 'ok' : 'not ok'))\n"
//            + "    .catch(e => logEx(e));\n"
//            + "}\n"
//            + "</script>\n"
//            + "</head>\n"
//            + "<body onload='test()'></body></html>";
//
//        getMockWebConnection().setDefaultResponse("<response/>\n", MimeType.TEXT_XML);
//        final WebDriver driver = loadPage2(html);
//        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
//    }
//
//    /**
//     * Tests fetch response with text encoding.
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("olé")
//    public void fetchResponseTextEncoding() throws Exception {
//        final String html = DOCTYPE_HTML
//            + "<html>\n"
//            + "  <head>\n"
//            + "    <script>\n"
//            + LOG_TITLE_FUNCTION
//            + "      function test() {\n"
//            + "        fetch('" + URL_SECOND + "')\n"
//            + "          .then(response => response.text())\n"
//            + "          .then(text => log(text))\n"
//            + "          .catch(e => logEx(e));\n"
//            + "      }\n"
//            + "    </script>\n"
//            + "  </head>\n"
//            + "  <body onload='test()'>\n"
//            + "  </body>\n"
//            + "</html>";
//
//        final String response = "olé";
//        final byte[] responseBytes = response.getBytes(UTF_8);
//
//        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK",
//            MimeType.TEXT_HTML, new ArrayList<>());
//        final WebDriver driver = loadPage2(html);
//        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
//    }
}
