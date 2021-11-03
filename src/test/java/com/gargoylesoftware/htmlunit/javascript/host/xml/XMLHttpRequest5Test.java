/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Additional tests for {@link XMLHttpRequest} blob handling.
 *
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequest5Test extends WebDriverTestCase {

    @Test
    @Alerts(DEFAULT = {"multipart/form-data; boundary=----formdata123456", "Html\r\nUnit\r\n"},
            IE = {"null", "Html\r\nUnit\r\n"})
    @HtmlUnitNYI(IE = {"application/x-www-form-urlencoded", "null"})
    public void sendBlob() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType(getExpectedAlerts()[0],
                "Html\\r\\nUnit\\r\\n", getExpectedAlerts()[1]);
    }

    @Test
    @Alerts({"null", "Html\r\nUnit\r\n"})
    @HtmlUnitNYI(IE = {"application/x-www-form-urlencoded", "null"})
    public void sendBlob_emptyMimeType() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType(getExpectedAlerts()[0],
                "Html\\r\\nUnit\\r\\n", getExpectedAlerts()[1]);
    }

    @Test
    @Alerts(DEFAULT = {"doesnt/exist", "Html\r\nUnit\r\n"},
            IE = {"null", "Html\r\nUnit\r\n"})
    @HtmlUnitNYI(IE = {"application/x-www-form-urlencoded", "null"})
    public void sendBlob_badMimeType() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType(getExpectedAlerts()[0],
                "Html\\r\\nUnit\\r\\n", getExpectedAlerts()[1]);
    }

    private void sendBlobWithMimeTypeAndAssertContentType(final String desiredMimeType,
            final String sentBody, final String expectedBody) throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());
        final String url = URL_SECOND.toString();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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

    @Test
    @Alerts({"text/plain", "HtmlUnit"})
    @HtmlUnitNYI(IE = {"application/x-www-form-urlencoded", "null"})
    public void sendBlob307() throws Exception {
        final URL redirectUrl = new URL(URL_FIRST, "/redirect.html");
        final URL responseUrl = new URL(URL_FIRST, "/response.html");

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", responseUrl.toExternalForm()));
        getMockWebConnection().setResponse(redirectUrl, "", 307, "Redirected", null, headers);
        getMockWebConnection().setResponse(responseUrl, "Result");

        startWebServer(getMockWebConnection(), Charset.defaultCharset());

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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

    @Test
    @Alerts(DEFAULT = {"application/xml;charset=UTF-8", "null"},
            IE = {"application/xml; charset=utf-8", "\u00EF\u00BB\u00BF"})
    @HtmlUnitNYI(IE = {"application/xml;charset=UTF-8", "null"})
    public void sendXMLDocumentEmpty() throws Exception {
        final String createXmlDoc =
                "    var doc = document.implementation.createDocument('', '', null);\n";
        sendXMLDocument(createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    @Test
    @Alerts(DEFAULT = {"application/xml;charset=UTF-8", "<root/>"},
            IE = {"application/xml; charset=utf-8", "\u00EF\u00BB\u00BF<root />"})
    @HtmlUnitNYI(IE = {"application/xml;charset=UTF-8", "<root/>"})
    public void sendXMLDocumentRoot() throws Exception {
        final String createXmlDoc =
                "    var doc = document.implementation.createDocument('', '', null);\n"
                + "  var root = doc.createElement('root');\n"
                + "  doc.appendChild(root);\n";
        sendXMLDocument(createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    @Test
    @Alerts(DEFAULT = {"application/xml;charset=UTF-8",
                       "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"></body></html>"},
            IE = {"application/xml; charset=utf-8",
                  "\u00EF\u00BB\u00BF<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\" /></html>"})
    @HtmlUnitNYI(CHROME = {"application/xml;charset=UTF-8",
                           "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"},
            EDGE = {"application/xml;charset=UTF-8",
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"},
            FF = {"application/xml;charset=UTF-8",
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"},
            FF78 = {"application/xml;charset=UTF-8",
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"},
            IE = {"application/xml;charset=UTF-8",
                  "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"abc\"/></html>"})
    public void sendXMLDocumentRootNamespace() throws Exception {
        final String createXmlDoc =
                "    var doc = document.implementation.createDocument('http://www.w3.org/1999/xhtml', 'html', null);\n"
                + "  var body = document.createElementNS('http://www.w3.org/1999/xhtml', 'body');\n"
                + "  body.setAttribute('id', 'abc');\n"
                + "  doc.documentElement.appendChild(body);\n";
        sendXMLDocument(createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    @Test
    @Alerts(DEFAULT = {"text/html;charset=UTF-8",
                       "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">"
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
                             + "</script></head>\n"
                             + "<body onload=\"test()\">\n"
                             + "</body></html>"},
            FF78 = {"text/html;charset=UTF-8",
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
                             + "</body></html>"},
            IE = {"text/html; charset=ISO-8859-1",
                  "<!DOCTYPE HTML>"
                      + "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">"
                             + "<html><head><title>foo</title>"
                             + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"><script>\n"
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
                           "<html xmlns=\"http://www.w3.org/1999/xhtml\" ><head><title>foo</title><script>\n"
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
                         "<html xmlns=\"http://www.w3.org/1999/xhtml\" ><head><title>foo</title><script>\n"
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
                       "<html xmlns=\"http://www.w3.org/1999/xhtml\" ><head><title>foo</title><script>\n"
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
                 FF78 = {"text/html;charset=UTF-8",
                         "<html xmlns=\"http://www.w3.org/1999/xhtml\" ><head><title>foo</title><script>\n"
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
                 IE = {"text/html;charset=UTF-8",
                       "<html xmlns=\"http://www.w3.org/1999/xhtml\" ><head><title>foo</title><script>\n"
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
        sendXMLDocument(createXmlDoc, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    private void sendXMLDocument(final String createXmlDoc,
            final String expectedMimeType, final String expectedBody) throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());
        final String url = URL_SECOND.toString();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
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
