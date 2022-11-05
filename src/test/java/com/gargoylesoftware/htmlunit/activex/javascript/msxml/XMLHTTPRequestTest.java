/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.ACTIVEX_CHECK;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.CREATE_XMLHTTPREQUEST_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.CREATE_XMLHTTPREQUEST_FUNCTION_NAME;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callCreateXMLHTTPRequest;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.createTestHTML;
import static com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequestTest.BasicAuthenticationServlet;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link XMLHTTPRequest}.
 *
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLHTTPRequestTest extends WebDriverTestCase {

    /**
     * Closes the real IE; otherwise tests are failing because of cached responses.
     */
    @After
    public void shutDownRealBrowsersAfter() {
        shutDownRealIE();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "[object Object]")
    public void createRequest_Microsoft_XMLHTTP() throws Exception {
        createRequest("Microsoft.XMLHTTP");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "[object Object]")
    public void createRequest_Msxml2_XMLHTTP() throws Exception {
        createRequest("Msxml2.XMLHTTP");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "[object Object]")
    public void createRequest_Msxml2_XMLHTTP_3() throws Exception {
        createRequest("Msxml2.XMLHTTP.3.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "[object Object]")
    public void createRequest_Msxml2_XMLHTTP_6() throws Exception {
        createRequest("Msxml2.XMLHTTP.6.0");
    }

    private void createRequest(final String activeXName) throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    log(Object.prototype.toString.call(xhr));\n"
            + "  }\n"
            + "  function " + CREATE_XMLHTTPREQUEST_FUNCTION_NAME + "() {\n"
            + "    return new ActiveXObject('" + activeXName + "');\n"
            + "  }\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "[object\\sObject]")
    public void scriptableToString() throws Exception {
        tester("log(Object.prototype.toString.call(xhr));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "0")
    public void properties_caseSensitive() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    log(xhr.reAdYsTaTe);\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "undefined")
    public void onreadystatechange_created() throws Exception {
        property("onreadystatechange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"orsc:1", "opened\\s1", "orsc:1", "orsc:2", "orsc:3", "orsc:4", "sent\\s1", "opened\\s2", "sent\\s2"})
    public void onreadystatechange_sync() throws Exception {
        final String test = ""
            + "xhr.onreadystatechange = onStateChange;\n"
            // open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "log('opened 1');\n"
            // send
            + "xhr.send();\n"
            + "log('sent 1');\n"
            // re-open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "log('opened 2');\n"
            // send
            + "xhr.send();\n"
            + "log('sent 2');\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"orsc:1", "opened\\s1", "orsc:1", "sent\\s1", "orsc:2", "orsc:3", "orsc:4"})
    public void onreadystatechange_async() throws Exception {
        final String test = ""
            + "xhr.onreadystatechange = onStateChange;\n"
            // open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "delay500/\", true);\n"
            + "log('opened 1');\n"
            // send
            + "xhr.send();\n"
            + "log('sent 1');\n";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"0", "1", "4", "1", "4"})
    public void readyState_sync() throws Exception {
        property_lifecycleSync("readyState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1:1", "1:1", "2:2", "3:3", "4:4"})
    public void readyState_async() throws Exception {
        property_lifecycleAsync("readyState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"exception-created",
                  "exception-opened",
                  "<root/>",
                  "exception-reopened",
                  "<root/>"})
    public void responseText_sync() throws Exception {
        property_lifecycleSync("responseText");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1:exception-async",
                  "1:exception-async",
                  "2:exception-async",
                  "3:exception-async",
                  "4:<root/>"})
    public void responseText_async() throws Exception {
        property_lifecycleAsync("responseText");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "<root/>")
    public void responseText_contentTypeNull() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        log(xhr.responseText);\n"
            + "      } catch(e) { log('exception-text'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", null);
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "<root/>")
    public void responseText_contentTypeText() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        log(xhr.responseText);\n"
            + "      } catch(e) { log('exception-text'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", MimeType.TEXT_PLAIN);
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "<root/>")
    public void responseText_contentTypeApplicationXML() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        log(xhr.responseText);\n"
            + "      } catch(e) { log('exception-text'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", "application/xml");
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * Verifies that the default encoding of <code>responseText</code> is UTF-8.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "ol\u00E9")
    public void responseText_defaultEncodingIsUTF8() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        log(xhr.responseText);\n"
            + "      } catch(e) { log('exception-text'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        final String response = "ol\u00E9";
        final byte[] responseBytes = response.getBytes(UTF_8);

        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK", MimeType.TEXT_PLAIN,
            new ArrayList<NameValuePair>());
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"exception-created",
                  "",
                  "<root/>\\r\\n",
                  "",
                  "<root/>\\r\\n"})
    public void responseXML_sync() throws Exception {
        property_lifecycleSync("responseXML.xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1:", "1:", "2:", "3:", "4:<root/>\\r\\n"})
    public void responseXML_async() throws Exception {
        property_lifecycleAsync("responseXML.xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "")
    public void responseXML_contentTypeNull() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        log(xhr.responseXML.xml);\n"
            + "      } catch(e) { log('exception-xml'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", null);
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "")
    public void responseXML_contentTypeText() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        log(xhr.responseXML.xml);\n"
            + "      } catch(e) { log('exception-xml'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", MimeType.TEXT_PLAIN);
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "<root/>\\r\\n")
    public void responseXML_contentTypeApplicationXML() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        log(xhr.responseXML.xml);\n"
            + "      } catch(e) { log('exception-xml'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", "application/xml");
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * Verifies that the default encoding of <code>responseXML</code> is UTF-8.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "<root>ol\u00E9</root>\\r\\n")
    public void responseXML_defaultEncodingIsUTF8() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        log(xhr.responseXML.xml);\n"
            + "      } catch(e) { log('exception-xml'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        final String response = "<root>ol\u00E9</root>";
        final byte[] responseBytes = response.getBytes(UTF_8);

        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK", MimeType.TEXT_XML,
            new ArrayList<NameValuePair>());
        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"exception-created",
                  "exception-opened",
                  "200",
                  "exception-reopened",
                  "200"})
    public void status_sync() throws Exception {
        property_lifecycleSync("status");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1:exception-async",
                  "1:exception-async",
                  "2:exception-async",
                  "3:exception-async",
                  "4:200"})
    public void status_async() throws Exception {
        property_lifecycleAsync("status");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"exception-created",
                  "exception-opened",
                  "OK",
                  "exception-reopened",
                  "OK"})
    public void statusText_sync() throws Exception {
        property_lifecycleSync("statusText");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1:exception-async",
                  "1:exception-async",
                  "2:exception-async",
                  "3:exception-async",
                  "4:OK"})
    public void statusText_async() throws Exception {
        property_lifecycleAsync("statusText");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"0:ex\\sstatus\\sex\\stext\\sex\\sxml",
                  "0:ex\\sstatus\\sex\\stext\\sex\\sxml"})
    public void abort_created() throws Exception {
        final String test = ""
            + "debugRequest(xhr);\n"
            + "try {\n"
            + "  xhr.abort();\n"
            + "} catch(e) { log('exception-abort'); }\n"
            + "debugRequest(xhr);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1:ex\\sstatus\\sex\\stext\\s",
                  "0:ex\\sstatus\\sex\\stext\\sex\\sxml"})
    public void abort_opened() throws Exception {
        final String test = ""
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "debugRequest(xhr);\n"
            + "try {\n"
            + "  xhr.abort();\n"
            + "} catch(e) { log('exception-abort'); }\n"
            + "debugRequest(xhr);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"4:200\\s<root/>\\s<root/>\\r\\n",
                  "0:ex\\sstatus\\sex\\stext\\sex\\sxml"})
    public void abort_sentSync() throws Exception {
        final String test = ""
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "xhr.send();\n"
            + "debugRequest(xhr);\n"
            + "try {\n"
            + "  xhr.abort();\n"
            + "} catch(e) { log('exception-abort'); }\n"
            + "debugRequest(xhr);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1:ex\\sstatus\\sex\\stext\\s",
                  "1:ex\\sstatus\\sex\\stext\\s",
                  "2:ex\\sstatus\\sex\\stext\\s",
                  "0:ex\\sstatus\\sex\\stext\\sex\\sxml"})
    // currently the started asynchronous request is not interrupted on abortion
    public void abort_sentAsync() throws Exception {
        final String test = ""
            + "xhr.onreadystatechange = function() {\n"
            + "  debugRequest(xhr);\n"
            + "  if (xhr.readyState == 2) {\n"
            + "    xhr.abort();\n"
            + "    debugRequest(xhr);\n"
            + "  }\n"
            + "};\n"
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", true);\n"
            + "xhr.send();\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"exception-created",
                  "exception-opened",
                  "Date\\sXYZ\\sGMT\\r\\n"
                      + "Content-Type:\\stext/xml;charset=iso-8859-1\\r\\n"
                      + "Transfer-Encoding:\\schunked\\r\\nServer:\\sJetty(XXX)\\r\\n\\r\\n"})
    public void getAllResponseHeaders() throws Exception {
        final String test = ""
            // create
            + "try {\n"
            + "  log(xhr.getAllResponseHeaders());\n"
            + "} catch(e) { log('exception-created'); }\n"
            // open
            + "xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "try {\n"
            + "  log(xhr.getAllResponseHeaders());\n"
            + "} catch(e) { log('exception-opened'); }\n"
            // send
            + "xhr.send();\n"
            + "try {\n"
            + "  var txt = xhr.getAllResponseHeaders();\n"
            + "  txt = txt.replace(/Jetty\\(.*\\)/, 'Jetty(XXX)');\n"
            + "  txt = txt.replace(/Date.*GMT/, 'Date XYZ GMT');\n"
            + "  txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "  txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "  log(txt);\n"
            + "} catch(e) { log('exception-sent'); }\n";

        tester(test, "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"exception-created",
                  "exception-opened",
                  "text/xml;charset=iso-8859-1",
                  "exception-getNull",
                  "exception-getEmpty",
                  "",
                  "text/xml;charset=iso-8859-1"})
    public void getResponseHeader() throws Exception {
        final String test = ""
            + "try {\n"
            + "  log(xhr.getResponseHeader('Content-Type'));\n"
            + "} catch(e) { log('exception-created'); }\n"
            + "xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "try {\n"
            + "  log(xhr.getResponseHeader('Content-Type'));\n"
            + "} catch(e) { log('exception-opened'); }\n"
            + "xhr.send();\n"
            // normal
            + "try {\n"
            + "  log(xhr.getResponseHeader('Content-Type'));\n"
            + "} catch(e) { log('exception-sent'); }\n"
            // null
            + "try {\n"
            + "  log(xhr.getResponseHeader(null));\n"
            + "} catch(e) { log('exception-getNull'); }\n"
            // empty
            + "try {\n"
            + "  log(xhr.getResponseHeader(''));\n"
            + "} catch(e) { log('exception-getEmpty'); }\n"
            // unknown
            + "try {\n"
            + "  log(xhr.getResponseHeader('XXX'));\n"
            + "} catch(e) { log('exception-getUnknown'); }\n"
            // case-sensitive
            + "try {\n"
            + "  log(xhr.getResponseHeader('cOntEnt-typE'));\n"
            + "} catch(e) { log('exception-getCase'); }\n";

        tester(test, "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 GET localhost/bounce?null,-1 ",
                  "4:200 POST localhost/bounce?null,0 ",
                  "4:200 PUT localhost/bounce?null,0 ",
                  "4:200 ",
                  "exception-methodTRACE", "0:ex status ex text ex xml",
                  "exception-methodNull", "0:ex status ex text ex xml",
                  "exception-methodEmpty", "0:ex status ex text ex xml",
                  "1:ex status ex text ", "4: "})
    public void open_method() throws Exception {
        final String test = ""
            // GET
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-methodGET'); }\n"
            // POST
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-methodPOST'); }\n"
            // PUT
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-methodPUT'); }\n"
            // HEAD
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-methodHEAD'); }\n"
            // TRACE
            + "try {\n"
            + "  xhr.open('TRACE', '/bounce', false);\n"
            + "} catch(e) { log('exception-methodTRACE'); }\n"
            + "debugRequest(xhr);\n"
            // null
            + "try {\n"
            + "  xhr.open(null, '/bounce', false);\n"
            + "} catch(e) { log('exception-methodNull'); }\n"
            + "debugRequest(xhr);\n"
            // empty
            + "try {\n"
            + "  xhr.open('', '/bounce', false);\n"
            + "} catch(e) { log('exception-methodEmpty'); }\n"
            + "debugRequest(xhr);\n"
            // invalid
            + "try {\n"
            + "  xhr.open('XXX', '/bounce', false);\n"
            + "  debugRequest(xhr);\n"
            + "  xhr.send();\n"
            // debug without status
            + "  var msg = xhr.readyState + ':';\n"
            + "  try {\n"
            + "    msg += ' ' + xhr.responseText;\n"
            + "  } catch(e) { msg += ' ex text'; }\n"
            + "  try {\n"
            + "    msg += ' ' + xhr.responseXML.xml;\n"
            + "  } catch(e) { msg += ' ex xml'; }\n"
            + "  log(msg);\n"
            + "} catch(e) { log('exception-methodInvalid'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 DELETE localhost/bounce?null,0 ",
                  "4:200 OPTIONS localhost/bounce?null,0 "})
    @NotYetImplemented(IE)
    // HtmlUnit does not send a body for DELETE and OPTIONS requests
    public void open_method2() throws Exception {
        final String test = ""
            // DELETE
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-methodDELETE'); }\n"
            // OPTIONS
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-methodOPTIONS'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 GET localhost/bounce?null,-1 ",
                  "4:200 GET localhost/bounce?null,-1 ",
                  "exception-urlNull", "4:200 GET localhost/bounce?null,-1 ",
                  "exception-urlEmpty", "0:ex status ex text ex xml",
                  "exception-urlNotFound", "4: "})
    public void open_url() throws Exception {
        final String test = ""
            // relative
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-urlRelative'); }\n"
            // absolute
            + "try {\n"
            + "  xhr.open('GET', '" + URL_FIRST + "bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-urlAbsolute'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('GET', null, false);\n"
            + "} catch(e) { log('exception-urlNull'); }\n"
            + "debugRequest(xhr);\n"
            // empty
            + "try {\n"
            + "  xhr.open('GET', '', false);\n"
            + "} catch(e) { log('exception-urlEmpty'); }\n"
            + "debugRequest(xhr);\n"
            // not found
            + "try {\n"
            + "  xhr.open('GET', 'http://localhost:9876/doesntexist', false);\n"
            + "  xhr.send();\n"
            + "} catch(e) { log('exception-urlNotFound'); }\n"
            // debug without status
            + "var msg = xhr.readyState + ':';\n"
            + "try {\n"
            + "  msg += ' ' + xhr.responseText;\n"
            + "} catch(e) { msg += ' ex text'; }\n"
            + "try {\n"
            + "  msg += ' ' + xhr.responseXML.xml;\n"
            + "} catch(e) { msg += ' ex xml'; }\n"
            + "log(msg);\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "4:200 Basic:Zm9vOmJhcg== ")
    public void open_authentication() throws Exception {
        final String test = ""
            + "try {\n"
            + "  xhr.open('GET', '/protected/token', false, 'foo', 'bar');\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-auth'); }\n";

        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "  function debugRequest(r) {\r"
            + "    var msg = r.readyState + ':';\n"
            + "    try {\n"
            + "      msg += r.status;\n"
            + "    } catch(e) { msg += 'ex status'; }\n"
            + "    try {\n"
            + "      msg += ' ' + r.responseText;\n"
            + "    } catch(e) { msg += ' ex text'; }\n"
            + "    try {\n"
            + "      msg += ' ' + r.responseXML.xml;\n"
            + "    } catch(e) { msg += ' ex xml'; }\n"
            + "    log(msg);\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/protected/token", BasicAuthenticationServlet.class);

        loadPage2(createTestHTML(html), servlets);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 GET localhost/bounce?null,-1 ",
                  "4:200 GET localhost/bounce?null,-1 ",
                  "4:200 GET localhost/bounce?null,-1 ",
                  "4:200 POST localhost/bounce?null,4 "})
    public void send_get() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 POST localhost/bounce?null,0 ",
                  "4:200 POST localhost/bounce?null,0 ",
                  "4:200 POST localhost/bounce?null,0 ",
                  "4:200 POST localhost/bounce?null,4 "})
    public void send_post() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 PUT localhost/bounce?null,0 ",
                  "4:200 PUT localhost/bounce?null,0 ",
                  "4:200 PUT localhost/bounce?null,0 ",
                  "4:200 PUT localhost/bounce?null,4 "})
    public void send_put() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 ",
                  "4:200 ",
                  "4:200 ",
                  "4:200 "})
    public void send_head() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 DELETE localhost/bounce?null,0 ",
                  "4:200 DELETE localhost/bounce?null,0 ",
                  "4:200 DELETE localhost/bounce?null,0 ",
                  "4:200 DELETE localhost/bounce?null,4 "})
    @NotYetImplemented(IE)
    // HtmlUnit does not send a body for DELETE and OPTIONS requests
    public void send_delete() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"4:200 OPTIONS localhost/bounce?null,0 ",
                  "4:200 OPTIONS localhost/bounce?null,0 ",
                  "4:200 OPTIONS localhost/bounce?null,0 ",
                  "4:200 OPTIONS localhost/bounce?null,4 "})
    @NotYetImplemented(IE)
    // HtmlUnit does not send a body for DELETE and OPTIONS requests
    public void send_options() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { log('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"exception-resend",
                  "4:200 GET localhost/bounce?null,-1 "})
    public void send_resend() throws Exception {
        final String test = ""
            + "xhr.open('GET', '/bounce', false);\n"
            + "xhr.send();\n"
            + "try {\n"
            + "  xhr.send();\n"
            + "} catch(e) { log('exception-resend'); }\n"
            + "debugRequest(xhr);\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"*/*",
                  "gzip, deflate",
                  "null",
                  "localhost:12345",
                  "§§URL§§"})
    public void send_headersDefaultEmpty() throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedHeaders = getExpectedAlerts();
        if (getExpectedAlerts().length > 1) {
            setExpectedAlerts();
        }

        final String test = ""
            + "try {\n"
            + "  xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "  xhr.send();\n"
            + "} catch(e) { log('exception-send'); }\n";

        tester(test, "");

        if (getExpectedAlerts().length > 1) {
            final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
            final Map<String, String> headers = lastRequest.getAdditionalHeaders();
            assertEquals(expectedHeaders[0], "" + headers.get(HttpHeader.ACCEPT));
            assertEquals(expectedHeaders[1], "" + headers.get(HttpHeader.ACCEPT_ENCODING));
            assertEquals(expectedHeaders[2], "" + headers.get(HttpHeader.CONTENT_LENGTH));
            assertEquals(expectedHeaders[3], "" + headers.get(HttpHeader.HOST));
            assertEquals(expectedHeaders[4], "" + headers.get(HttpHeader.REFERER));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"*/*",
                  "gzip, deflate",
                  "4",
                  "localhost:12345",
                  "§§URL§§"})
    public void send_headersDefaultBody() throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedHeaders = getExpectedAlerts();
        if (getExpectedAlerts().length > 1) {
            setExpectedAlerts();
        }

        final String test = ""
            + "try {\n"
            + "  xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "  xhr.send('1234');\n"
            + "} catch(e) { log('exception-send'); }\n";

        tester(test, "");

        if (getExpectedAlerts().length > 1) {
            final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
            final Map<String, String> headers = lastRequest.getAdditionalHeaders();
            assertEquals(expectedHeaders[0], "" + headers.get(HttpHeader.ACCEPT));
            assertEquals(expectedHeaders[1], "" + headers.get(HttpHeader.ACCEPT_ENCODING));
            assertEquals(expectedHeaders[2], "" + headers.get(HttpHeader.CONTENT_LENGTH));
            assertEquals(expectedHeaders[3], "" + headers.get(HttpHeader.HOST));
            assertEquals(expectedHeaders[4], "" + headers.get(HttpHeader.REFERER));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"null", "text/html,application/xhtml+xml,application/xml;"
                        + "q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                       "null", "null", "no\\sActiveX"},
            EDGE = {"null", "text/html,application/xhtml+xml,application/xml;"
                        + "q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                    "null", "null", "no\\sActiveX"},
            FF = {"null", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
                  "null", "null", "no\\sActiveX"},
            FF_ESR = {"null", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
                      "null", "null", "no\\sActiveX"},
            IE = {"bar",
                  "application/javascript",
                  "null",
                  "null",
                  "exception-unopened",
                  "exception-setNameNull",
                  "exception-setNameEmpty",
                  "exception-setValueNull"})
    public void setRequestHeader() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        setExpectedAlerts(
                Arrays.copyOfRange(expectedAlerts, 4, expectedAlerts.length));
        final String[] expectedHeaders =
                Arrays.copyOfRange(expectedAlerts, 0, 4);

        final String test = ""
            // before open
            + "try {\n"
            + "  xhr.setRequestHeader('Foo', 'bar');\n"
            + "} catch(e) { log('exception-unopened'); }\n"
            + "xhr.open('GET', '" + URL_SECOND + "', false);\n"
            // normal
            + "try {\n"
            + "  xhr.setRequestHeader('foo', 'bar');\n"
            + "} catch(e) { log('exception-set'); }\n"
            // overwrite
            + "try {\n"
            + "  xhr.setRequestHeader('Accept', 'application/javascript');\n"
            + "} catch(e) { log('exception-setOverwrite'); }\n"
            // null name
            + "try {\n"
            + "  xhr.setRequestHeader(null, 'NameNull');\n"
            + "} catch(e) { log('exception-setNameNull'); }\n"
            // empty name
            + "try {\n"
            + "  xhr.setRequestHeader('', 'NameEmpty');\n"
            + "} catch(e) { log('exception-setNameEmpty'); }\n"
            // null value
            + "try {\n"
            + "  xhr.setRequestHeader('ValueNull', null);\n"
            + "} catch(e) { log('exception-setValueNull'); }\n"
            // empty value
            + "try {\n"
            + "  xhr.setRequestHeader('ValueEmpty', '');\n"
            + "} catch(e) { log('exception-setValueEmpty'); }\n"
            // blank value
            + "try {\n"
            + "  xhr.setRequestHeader('ValueEmpty', ' ');\n"
            + "} catch(e) { log('exception-setValueBlank'); }\n"
            + "xhr.send();\n";

        tester(test, "");
        Thread.sleep(100);

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals(expectedHeaders[0], String.valueOf(headers.get("foo")));
        assertEquals(expectedHeaders[1], String.valueOf(headers.get(HttpHeader.ACCEPT)));
        assertEquals(expectedHeaders[2], String.valueOf(headers.get("ValueEmpty")));
        assertEquals(expectedHeaders[3], String.valueOf(headers.get("ValueBlank")));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {})
    public void setRequestHeader_contentLength() throws Exception {
        setRequestHeader_contentLength(null, null);
        setRequestHeader_contentLength("", null);
        setRequestHeader_contentLength("1234", getExpectedAlerts().length == 0 ? "4" : null);
    }

    private void setRequestHeader_contentLength(final String content, final String contentLength) throws Exception {
        String test = ""
            + "xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "try {\n"
            + "  xhr.setRequestHeader('Content-Length', '20');\n"
            + "} catch(e) { log('exception-set'); }\n";
        if (content == null) {
            test += "xhr.send();\n";
        }
        else {
            test += "xhr.send('" + content + "');\n";
        }

        tester(test, "");

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals(contentLength, headers.get(HttpHeader.CONTENT_LENGTH));
    }

    private void property(final String property) throws Exception {
        tester("log(xhr." + property + ");\n");
    }

    private void property_lifecycleSync(final String property) throws Exception {
        final String test = ""
            // create
            + "try {\n"
            + "  var txt = '' + xhr." + property + ";\n"
            + "  txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "  txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "  log(txt);\n"
            + "} catch(e) { log('exception-created'); }\n"
            // open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "try {\n"
            + "  txt = '' + xhr." + property + ";\n"
            + "  txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "  txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "  log(txt);\n"
            + "} catch(e) { log('exception-opened'); }\n"
            // send
            + "xhr.send();\n"
            + "try {\n"
            + "  txt = '' + xhr." + property + ";\n"
            + "  txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "  txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "  log(txt);\n"
            + "} catch(e) { log('exception-sent'); }\n"
            // re-open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "try {\n"
            + "  txt = '' + xhr." + property + ";\n"
            + "  txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "  txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "  log(txt);\n"
            + "} catch(e) { log('exception-reopened'); }\n"
            // send
            + "xhr.send();\n"
            + "try {\n"
            + "  txt = '' + xhr." + property + ";\n"
            + "  txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "  txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "  log(txt);\n"
            + "} catch(e) { log('exception-sent'); }\n";

        tester(test);
    }

    private void property_lifecycleAsync(final String property) throws Exception {
        final String test = ""
            + "xhr.onreadystatechange = function() {\n"
            + "  try {\n"
            + "    var txt = xhr.readyState + ':' + xhr." + property + ";\n"
            + "    log(txt);\n"
            + "  } catch(e) { log(xhr.readyState + ':exception-async'); }\n"
            + "};\n"
            // open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            // send
            + "xhr.send();\n"
            // re-open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            // send
            + "xhr.send();\n";

        tester(test);
    }

    private void tester(final String test) throws Exception {
        tester(test, "<root/>");
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "  function onStateChange() {\n"
            + "    log('orsc:' + xhr.readyState);\n"
            + "  }\n"
            + "  function debugRequest(r) {\r"
            + "    var msg = r.readyState + ':';\n"
            + "    try {\n"
            + "      msg += r.status;\n"
            + "    } catch(e) { msg += 'ex status'; }\n"
            + "    try {\n"
            + "      msg += ' ' + r.responseText;\n"
            + "    } catch(e) { msg += ' ex text'; }\n"
            + "    try {\n"
            + "      var txt = r.responseXML.xml;\n"
            + "      txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "      txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "      msg += ' ' + txt;\n"
            + "    } catch(e) { msg += ' ex xml'; }\n"
            + "    log(msg);\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPage2(createTestHTML(html), URL_FIRST);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    private void tester_bounce(final String test) throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  var xhr;\n"
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "  function onStateChange() {\n"
            + "    log('orsc:' + xhr.readyState);\n"
            + "  }\n"
            + "  function debugRequest(r) {\r"
            + "    var msg = r.readyState + ':';\n"
            + "    try {\n"
            + "      msg += r.status;\n"
            + "    } catch(e) { msg += 'ex status'; }\n"
            + "    try {\n"
            + "      msg += ' ' + r.responseText;\n"
            + "    } catch(e) { msg += ' ex text'; }\n"
            + "    try {\n"
            + "      msg += ' ' + r.responseXML.xml;\n"
            + "    } catch(e) { msg += ' ex xml'; }\n"
            + "    log(msg);\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/bounce", BounceServlet.class);

        loadPage2(createTestHTML(html), URL_FIRST, servlets);
        verifyTitle2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Servlet bouncing requests.
     */
    public static class BounceServlet extends HttpServlet {

        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doHead(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doPut(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doOptions(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doTrace(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        private static void bounce(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            try (Writer writer = resp.getWriter()) {
                writer.write(req.getMethod() + " " + req.getServerName() + req.getRequestURI()
                    + "?" + req.getQueryString() + ',' + req.getContentLength());
            }
        }
    }
}
