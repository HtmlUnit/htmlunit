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

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.NameValuePair;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Additional tests for {@link XMLHttpRequest} blob handling.
 *
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 */
public class XMLHttpRequest5Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"multipart/form-data; boundary=----formdata123456", "Html\r\nUnit\r\n"})
    public void sendBlob() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType(getExpectedAlerts()[0],
                "Html\\r\\nUnit\\r\\n", getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "Html\r\nUnit\r\n"})
    public void sendBlob_emptyMimeType() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType(getExpectedAlerts()[0],
                "Html\\r\\nUnit\\r\\n", getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"doesnt/exist", "Html\r\nUnit\r\n"})
    public void sendBlob_badMimeType() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType(getExpectedAlerts()[0],
                "Html\\r\\nUnit\\r\\n", getExpectedAlerts()[1]);
    }

    private void sendBlobWithMimeTypeAndAssertContentType(final String desiredMimeType,
            final String sentBody, final String expectedBody) throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());
        final String url = URL_SECOND.toString();

        final String html = DOCTYPE_HTML
                + "<html><head><title>foo</title>"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var xhr = new XMLHttpRequest();\n"
                + "      xhr.open('POST', '"
                + url
                + "', false);\n"
                + "      var body = ['" + sentBody + "'];\n"
                + "      var blob = new Blob(body, {type:'" + desiredMimeType + "'});\n"
                + "      xhr.send(blob);\n"
                + "  } catch (exception) { \n"
                + "    alert(exception);\n"
                + "  }\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "<form id='form'>"
                + "<input type='text' value='something'> "
                + "<input type='field' >"
                + "</form>"
                + "</body></html>";

        setExpectedAlerts();
        loadPageWithAlerts2(html);

        Assert.assertEquals("Never received a call to URL_SECOND", URL_SECOND,
                getMockWebConnection().getLastWebRequest().getUrl());

        final String contentType = getMockWebConnection().getLastWebRequest()
                .getAdditionalHeader(HttpHeader.CONTENT_TYPE);

        final String requestBody = getMockWebConnection().getLastWebRequest().getRequestBody();

        Assert.assertEquals("Unexpected Content-Type header", desiredMimeType,
                contentType == null ? "null" : contentType);
        Assert.assertEquals(expectedBody, requestBody == null ? "null" : requestBody);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain", "HtmlUnit"})
    public void sendBlob307() throws Exception {
        final URL redirectUrl = new URL(URL_FIRST, "/redirect.html");
        final URL responseUrl = new URL(URL_FIRST, "/response.html");

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", responseUrl.toExternalForm()));
        getMockWebConnection().setResponse(redirectUrl, "", 307, "Redirected", null, headers);
        getMockWebConnection().setResponse(responseUrl, "Result");

        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = DOCTYPE_HTML
                + "<html><head><title>foo</title>"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var xhr = new XMLHttpRequest();\n"
                + "      xhr.open('POST', '/redirect.html', false);\n"
                + "      var body = ['HtmlUnit'];\n"
                + "      var blob = new Blob(body, {type:'text/plain'});\n"
                + "      xhr.send(blob);\n"
                + "  } catch (exception) { \n"
                + "    alert(exception);\n"
                + "  }\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPage2(html);

        Assert.assertEquals("Never received a call to '" + responseUrl + "'", responseUrl,
                getMockWebConnection().getLastWebRequest().getUrl());

        Assert.assertEquals(3, getMockWebConnection().getRequestCount());
        final String contentType = getMockWebConnection().getLastWebRequest()
                .getAdditionalHeader(HttpHeader.CONTENT_TYPE);

        final String requestBody = getMockWebConnection().getLastWebRequest().getRequestBody();

        Assert.assertEquals("Unexpected Content-Type header", getExpectedAlerts()[0],
                contentType == null ? "null" : contentType);
        Assert.assertEquals(getExpectedAlerts()[1], requestBody == null ? "null" : requestBody);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "127", "128", "255"})
    public void sendXUserDefined() throws Exception {
        final URL responseUrl = new URL(URL_FIRST, "/response");
        getMockWebConnection().setResponse(responseUrl, "\u0000\u007f\u0080\u00ff");

        startWebServer(getMockWebConnection(), StandardCharsets.US_ASCII);

        final String html = DOCTYPE_HTML
                + "<html><head>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var xhr = new XMLHttpRequest();\n"
                + "      xhr.open('POST', '/response', false);\n"
                + "      xhr.overrideMimeType('text/plain; charset=x-user-defined');\n"
                + "      xhr.send(null);\n"
                + "      log(xhr.responseText.charCodeAt(0) & 0xff);\n"
                + "      log(xhr.responseText.charCodeAt(1) & 0xff);\n"
                + "      log(xhr.responseText.charCodeAt(2) & 0xff);\n"
                + "      log(xhr.responseText.charCodeAt(3) & 0xff);\n"
                + "  } catch (exception) { \n"
                + "    log(exception);\n"
                + "  }\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onreadystatechange [object Event]", "readystatechange", "1",
             "NetworkError"})
    public void sendLocalFile() throws Exception {
        final URL fileURL = getClass().getClassLoader().getResource("testfiles/tiny-png.img");
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        startWebServer(getMockWebConnection(), StandardCharsets.US_ASCII);

        final String html = DOCTYPE_HTML
                + "<html><head>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var xhr = new XMLHttpRequest();\n"
                + "      xhr.onreadystatechange = function(event) {\n"
                + "                    log('onreadystatechange ' + event);\n"
                + "                    log(event.type);\n"
                + "                    log(xhr.readyState);\n"
                + "                  };\n"
                + "      xhr.onerror = function(event) {\n"
                + "                    log('error ' + event);\n"
                + "                    log(event.type);\n"
                + "                    log(event.lengthComputable);\n"
                + "                    log(event.loaded);\n"
                + "                    log(event.total);\n"
                + "                  };\n"
                + "      xhr.open('GET', '" + fileURL + "', false);\n"
                + "      xhr.send('');\n"
                + "  } catch (exception) { \n"
                + "    log(exception.name);\n"
                + "  }\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onreadystatechange [object Event]", "readystatechange", "1",
             "onreadystatechange [object Event]", "readystatechange", "4",
             "error [object ProgressEvent]", "error", "false", "0", "0"})
    public void sendLocalFileAsync() throws Exception {
        final URL fileURL = getClass().getClassLoader().getResource("testfiles/tiny-png.img");
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        startWebServer(getMockWebConnection(), StandardCharsets.US_ASCII);

        final String html = DOCTYPE_HTML
                + "<html><head>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      var xhr = new XMLHttpRequest();\n"
                + "      xhr.onreadystatechange = function(event) {\n"
                + "                    log('onreadystatechange ' + event);\n"
                + "                    log(event.type);\n"
                + "                    log(xhr.readyState);\n"
                + "                  };\n"
                + "      xhr.onerror = function(event) {\n"
                + "                    log('error ' + event);\n"
                + "                    log(event.type);\n"
                + "                    log(event.lengthComputable);\n"
                + "                    log(event.loaded);\n"
                + "                    log(event.total);\n"
                + "                  };\n"
                + "      xhr.open('GET', '" + fileURL + "', true);\n"
                + "      xhr.send('');\n"
                + "  } catch (exception) { \n"
                + "    log(exception);\n"
                + "  }\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/xml;charset=UTF-8", "null"})
    public void sendXMLDocumentEmpty() throws Exception {
        final String createXmlDoc =
                "    var doc = document.implementation.createDocument('', '', null);\n";
        sendXMLDocument(DOCTYPE_HTML, createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/xml;charset=UTF-8", "<root/>"})
    public void sendXMLDocumentRoot() throws Exception {
        final String createXmlDoc =
                "    var doc = document.implementation.createDocument('', '', null);\n"
                + "  var root = doc.createElement('root');\n"
                + "  doc.appendChild(root);\n";
        sendXMLDocument(DOCTYPE_HTML, createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"application/xml;charset=UTF-8",
             "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"></body></html>"})
    @HtmlUnitNYI(CHROME = {"application/xml;charset=UTF-8",
                           "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"},
            EDGE = {"application/xml;charset=UTF-8",
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"},
            FF = {"application/xml;charset=UTF-8",
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"},
            FF_ESR = {"application/xml;charset=UTF-8",
                      "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"})
    public void sendXMLDocumentRootNamespace() throws Exception {
        final String createXmlDoc =
                "    var doc = document.implementation.createDocument('http://www.w3.org/1999/xhtml', 'html', null);\n"
                + "  var body = document.createElementNS('http://www.w3.org/1999/xhtml', 'body');\n"
                + "  body.setAttribute('id', 'abc');\n"
                + "  doc.documentElement.appendChild(body);\n";
        sendXMLDocument(DOCTYPE_HTML, createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/html;charset=UTF-8",
             "<!DOCTYPE html>"
                    + "<html><head><title>foo</title><script>\n"
                    + "  function test() {\n"
                    + "    try {\n"
                    + "    var doc = document;\n"
                    + "      var xhr = new XMLHttpRequest();\n"
                    + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                    + "      xhr.send(doc);\n"
                    + "  } catch (exception) { \n"
                    + "    alert(exception);\n"
                    + "  }\n"
                    + "}\n"
                    + "</script></head>\n"
                    + "<body onload=\"test()\">\n"
                    + "</body></html>"})
    @HtmlUnitNYI(CHROME = {"text/html;charset=UTF-8",
                           "<!DOCTYPE html>"
                             + "<html><head><title>foo</title><script>\n"
                             + "  function test() {\n"
                             + "    try {\n"
                             + "    var doc = document;\n"
                             + "      var xhr = new XMLHttpRequest();\n"
                             + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                             + "      xhr.send(doc);\n"
                             + "  } catch (exception) { \n"
                             + "    alert(exception);\n"
                             + "  }\n"
                             + "}\n"
                             + "</script></head>"
                             + "<body onload=\"test()\">\n"
                             + "</body></html>"},
                 EDGE = {"text/html;charset=UTF-8",
                         "<!DOCTYPE html>"
                           + "<html><head><title>foo</title><script>\n"
                           + "  function test() {\n"
                           + "    try {\n"
                           + "    var doc = document;\n"
                           + "      var xhr = new XMLHttpRequest();\n"
                           + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                           + "      xhr.send(doc);\n"
                           + "  } catch (exception) { \n"
                           + "    alert(exception);\n"
                           + "  }\n"
                           + "}\n"
                           + "</script></head>"
                           + "<body onload=\"test()\">\n"
                           + "</body></html>"},
                 FF = {"text/html;charset=UTF-8",
                       "<!DOCTYPE html>"
                           + "<html><head><title>foo</title><script>\n"
                           + "  function test() {\n"
                           + "    try {\n"
                           + "    var doc = document;\n"
                           + "      var xhr = new XMLHttpRequest();\n"
                           + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                           + "      xhr.send(doc);\n"
                           + "  } catch (exception) { \n"
                           + "    alert(exception);\n"
                           + "  }\n"
                           + "}\n"
                           + "</script></head>"
                           + "<body onload=\"test()\">\n"
                           + "</body></html>"},
                 FF_ESR = {"text/html;charset=UTF-8",
                           "<!DOCTYPE html>"
                           + "<html><head><title>foo</title><script>\n"
                           + "  function test() {\n"
                           + "    try {\n"
                           + "    var doc = document;\n"
                           + "      var xhr = new XMLHttpRequest();\n"
                           + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                           + "      xhr.send(doc);\n"
                           + "  } catch (exception) { \n"
                           + "    alert(exception);\n"
                           + "  }\n"
                           + "}\n"
                           + "</script></head>"
                           + "<body onload=\"test()\">\n"
                           + "</body></html>"})
    public void sendDocument() throws Exception {
        final String createXmlDoc =
                "    var doc = document;\n";
        sendXMLDocument(DOCTYPE_HTML, createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/html;charset=UTF-8",
             "<html><head><title>foo</title><script>\n"
                    + "  function test() {\n"
                    + "    try {\n"
                    + "    var doc = document;\n"
                    + "      var xhr = new XMLHttpRequest();\n"
                    + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                    + "      xhr.send(doc);\n"
                    + "  } catch (exception) { \n"
                    + "    alert(exception);\n"
                    + "  }\n"
                    + "}\n"
                    + "</script></head>\n"
                    + "<body onload=\"test()\">\n"
                    + "</body></html>"})
    @HtmlUnitNYI(CHROME = {"text/html;charset=UTF-8",
                           "<html><head><title>foo</title><script>\n"
                             + "  function test() {\n"
                             + "    try {\n"
                             + "    var doc = document;\n"
                             + "      var xhr = new XMLHttpRequest();\n"
                             + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                             + "      xhr.send(doc);\n"
                             + "  } catch (exception) { \n"
                             + "    alert(exception);\n"
                             + "  }\n"
                             + "}\n"
                             + "</script></head>"
                             + "<body onload=\"test()\">\n"
                             + "</body></html>"},
                 EDGE = {"text/html;charset=UTF-8",
                         "<html><head><title>foo</title><script>\n"
                           + "  function test() {\n"
                           + "    try {\n"
                           + "    var doc = document;\n"
                           + "      var xhr = new XMLHttpRequest();\n"
                           + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                           + "      xhr.send(doc);\n"
                           + "  } catch (exception) { \n"
                           + "    alert(exception);\n"
                           + "  }\n"
                           + "}\n"
                           + "</script></head>"
                           + "<body onload=\"test()\">\n"
                           + "</body></html>"},
                 FF = {"text/html;charset=UTF-8",
                       "<html><head><title>foo</title><script>\n"
                           + "  function test() {\n"
                           + "    try {\n"
                           + "    var doc = document;\n"
                           + "      var xhr = new XMLHttpRequest();\n"
                           + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                           + "      xhr.send(doc);\n"
                           + "  } catch (exception) { \n"
                           + "    alert(exception);\n"
                           + "  }\n"
                           + "}\n"
                           + "</script></head>"
                           + "<body onload=\"test()\">\n"
                           + "</body></html>"},
                 FF_ESR = {"text/html;charset=UTF-8",
                           "<html><head><title>foo</title><script>\n"
                           + "  function test() {\n"
                           + "    try {\n"
                           + "    var doc = document;\n"
                           + "      var xhr = new XMLHttpRequest();\n"
                           + "      xhr.open('POST', 'http://localhost:22222/second/', false);\n"
                           + "      xhr.send(doc);\n"
                           + "  } catch (exception) { \n"
                           + "    alert(exception);\n"
                           + "  }\n"
                           + "}\n"
                           + "</script></head>"
                           + "<body onload=\"test()\">\n"
                           + "</body></html>"})
    public void sendDocumentNoDoctype() throws Exception {
        final String createXmlDoc =
                "    var doc = document;\n";
        sendXMLDocument("", createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    private void sendXMLDocument(final String doctype, final String createXmlDoc,
            final String expectedMimeType, final String expectedBody) throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());
        final String url = URL_SECOND.toString();

        final String html = doctype
                + "<html><head><title>foo</title>"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"

                + createXmlDoc

                + "      var xhr = new XMLHttpRequest();\n"
                + "      xhr.open('POST', '" + url + "', false);\n"
                + "      xhr.send(doc);\n"
                + "  } catch (exception) { \n"
                + "    alert(exception);\n"
                + "  }\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        setExpectedAlerts();
        loadPageWithAlerts2(html);

        Assert.assertEquals("Never received a call to URL_SECOND", URL_SECOND,
                getMockWebConnection().getLastWebRequest().getUrl());

        final String contentType = getMockWebConnection().getLastWebRequest()
                .getAdditionalHeader(HttpHeader.CONTENT_TYPE);

        final String requestBody = getMockWebConnection().getLastWebRequest().getRequestBody();

        Assert.assertEquals("Unexpected Content-Type header", expectedMimeType,
                contentType == null ? "null" : contentType);
        Assert.assertEquals(expectedBody, requestBody == null ? "null" : requestBody);
    }
}
