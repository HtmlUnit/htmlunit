/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.CREATE_XMLHTTPREQUEST_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.CREATE_XMLHTTPREQUEST_FUNCTION_NAME;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLHTTPRequest;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequestTest.BasicAuthenticationServlet;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLHTTPRequest}.
 *
 * @version $Revision$
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLHTTPRequestTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void createRequest_Microsoft_XMLHTTP() throws Exception {
        createRequest("Microsoft.XMLHTTP");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void createRequest_Msxml2_XMLHTTP() throws Exception {
        createRequest("Msxml2.XMLHTTP");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void createRequest_Msxml2_XMLHTTP_3() throws Exception {
        createRequest("Msxml2.XMLHTTP.3.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void createRequest_Msxml2_XMLHTTP_6() throws Exception {
        createRequest("Msxml2.XMLHTTP.6.0");
    }

    private void createRequest(final String activeXName) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    alert(Object.prototype.toString.call(xhr));\n"
            + "  }\n"
            + "  function " + CREATE_XMLHTTPREQUEST_FUNCTION_NAME + "() {\n"
            + "    return new ActiveXObject('" + activeXName + "');\n"
            + "  }\n";

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(xhr));\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(IE)
    @Alerts("0")
    public void properties_caseSensitive() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    alert(xhr.reAdYsTaTe);\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("undefined")
    public void onreadystatechange_created() throws Exception {
        property("onreadystatechange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "orsc:1", "opened 1", "orsc:1", "orsc:2", "orsc:3", "orsc:4", "sent 1", "opened 2", "sent 2" })
    public void onreadystatechange_sync() throws Exception {
        final String test = ""
            + "xhr.onreadystatechange = onStateChange;\n"
            // open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "alert('opened 1');\n"
            // send
            + "xhr.send();\n"
            + "alert('sent 1');\n"
            // re-open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "alert('opened 2');\n"
            // send
            + "xhr.send();\n"
            + "alert('sent 2');\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "orsc:1", "opened 1", "orsc:1", "sent 1", "orsc:2", "orsc:3", "orsc:4" })
    public void onreadystatechange_async() throws Exception {
        final String test = ""
            + "xhr.onreadystatechange = onStateChange;\n"
            // open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "delay500/\", true);\n"
            + "alert('opened 1');\n"
            // send
            + "xhr.send();\n"
            + "alert('sent 1');\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "0", "1", "4", "1", "4" })
    public void readyState_sync() throws Exception {
        property_lifecycleSync("readyState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1:1", "1:1", "2:2", "3:3", "4:4" })
    public void readyState_async() throws Exception {
        property_lifecycleAsync("readyState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "exception-created",
        "exception-opened",
        "<root/>",
        "exception-reopened",
        "<root/>" })
    public void responseText_sync() throws Exception {
        property_lifecycleSync("responseText");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1:exception-async",
        "1:exception-async",
        "2:exception-async",
        "3:exception-async",
        "4:<root/>" })
    public void responseText_async() throws Exception {
        property_lifecycleAsync("responseText");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<root/>")
    public void responseText_contentTypeNull() throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        alert(xhr.responseText);\n"
            + "      } catch(e) { alert('exception-text'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", null);
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<root/>")
    public void responseText_contentTypeText() throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        alert(xhr.responseText);\n"
            + "      } catch(e) { alert('exception-text'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", "text/plain");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<root/>")
    public void responseText_contentTypeApplicationXML() throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        alert(xhr.responseText);\n"
            + "      } catch(e) { alert('exception-text'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", "application/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * Verifies that the default encoding of <code>responseText</code> is UTF-8.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("ol\u00E9")
    public void responseText_defaultEncodingIsUTF8() throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        alert(xhr.responseText);\n"
            + "      } catch(e) { alert('exception-text'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        final String response = "ol\u00E9";
        final byte[] responseBytes = response.getBytes("UTF-8");

        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK", "text/plain",
            new ArrayList<NameValuePair>());
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "exception-created",
        "",
        "<root/>\r\n",
        "",
        "<root/>\r\n" })
    public void responseXML_sync() throws Exception {
        property_lifecycleSync("responseXML.xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1:", "1:", "2:", "3:", "4:<root/>\r\n" })
    public void responseXML_async() throws Exception {
        property_lifecycleAsync("responseXML.xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void responseXML_contentTypeNull() throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        alert(xhr.responseXML.xml);\n"
            + "      } catch(e) { alert('exception-xml'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", null);
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void responseXML_contentTypeText() throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        alert(xhr.responseXML.xml);\n"
            + "      } catch(e) { alert('exception-xml'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", "text/plain");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<root/>\r\n")
    public void responseXML_contentTypeApplicationXML() throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        alert(xhr.responseXML.xml);\n"
            + "      } catch(e) { alert('exception-xml'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, "<root/>", "application/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * Verifies that the default encoding of <code>responseXML</code> is UTF-8.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<root>ol\u00E9</root>\r\n")
    public void responseXML_defaultEncodingIsUTF8() throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + "      xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "      xhr.send();\n"
            + "      try {\n"
            + "        alert(xhr.responseXML.xml);\n"
            + "      } catch(e) { alert('exception-xml'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        final String response = "<root>ol\u00E9</root>";
        final byte[] responseBytes = response.getBytes("UTF-8");

        getMockWebConnection().setResponse(URL_SECOND, responseBytes, 200, "OK", "text/xml",
            new ArrayList<NameValuePair>());
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "exception-created",
        "exception-opened",
        "200",
        "exception-reopened",
        "200" })
    public void status_sync() throws Exception {
        property_lifecycleSync("status");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1:exception-async",
        "1:exception-async",
        "2:exception-async",
        "3:exception-async",
        "4:200" })
    public void status_async() throws Exception {
        property_lifecycleAsync("status");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "exception-created",
        "exception-opened",
        "OK",
        "exception-reopened",
        "OK" })
    public void statusText_sync() throws Exception {
        property_lifecycleSync("statusText");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1:exception-async",
        "1:exception-async",
        "2:exception-async",
        "3:exception-async",
        "4:OK" })
    public void statusText_async() throws Exception {
        property_lifecycleAsync("statusText");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "0:ex status ex text ex xml",
        "0:ex status ex text ex xml" })
    public void abort_created() throws Exception {
        final String test = ""
            + "debugRequest(xhr);\n"
            + "try {\n"
            + "  xhr.abort();\n"
            + "} catch(e) { alert('exception-abort'); }\n"
            + "debugRequest(xhr);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1:ex status ex text ",
        "0:ex status ex text ex xml" })
    public void abort_opened() throws Exception {
        final String test = ""
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "debugRequest(xhr);\n"
            + "try {\n"
            + "  xhr.abort();\n"
            + "} catch(e) { alert('exception-abort'); }\n"
            + "debugRequest(xhr);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 <root/> <root/>\r\n",
        "0:ex status ex text ex xml" })
    public void abort_sentSync() throws Exception {
        final String test = ""
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "xhr.send();\n"
            + "debugRequest(xhr);\n"
            + "try {\n"
            + "  xhr.abort();\n"
            + "} catch(e) { alert('exception-abort'); }\n"
            + "debugRequest(xhr);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1:ex status ex text ",
        "1:ex status ex text ",
        "2:ex status ex text ",
        "0:ex status ex text ex xml" })
    @NotYetImplemented(IE)
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
    @Browsers(IE)
    @Alerts({ "exception-created",
        "exception-opened",
        "Content-Type: text/xml;charset=ISO-8859-1\r\nTransfer-Encoding: chunked\r\nServer: Jetty(XXX)\r\n\r\n" })
    public void getAllResponseHeaders() throws Exception {
        final String test = ""
            // create
            + "try {\n"
            + "  alert(xhr.getAllResponseHeaders());\n"
            + "} catch(e) { alert('exception-created'); }\n"
            // open
            + "xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "try {\n"
            + "  alert(xhr.getAllResponseHeaders());\n"
            + "} catch(e) { alert('exception-opened'); }\n"
            // send
            + "xhr.send();\n"
            + "try {\n"
            + "  alert(xhr.getAllResponseHeaders().replace(/Jetty\\(.*\\)/, 'Jetty(XXX)'));\n"
            + "} catch(e) { alert('exception-sent'); }\n";

        tester(test, "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "exception-created",
        "exception-opened",
        "text/xml;charset=ISO-8859-1",
        "exception-getNull",
        "exception-getEmpty",
        "",
        "text/xml;charset=ISO-8859-1" })
    public void getResponseHeader() throws Exception {
        final String test = ""
            + "try {\n"
            + "  alert(xhr.getResponseHeader('Content-Type'));\n"
            + "} catch(e) { alert('exception-created'); }\n"
            + "xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "try {\n"
            + "  alert(xhr.getResponseHeader('Content-Type'));\n"
            + "} catch(e) { alert('exception-opened'); }\n"
            + "xhr.send();\n"
            // normal
            + "try {\n"
            + "  alert(xhr.getResponseHeader('Content-Type'));\n"
            + "} catch(e) { alert('exception-sent'); }\n"
            // null
            + "try {\n"
            + "  alert(xhr.getResponseHeader(null));\n"
            + "} catch(e) { alert('exception-getNull'); }\n"
            // empty
            + "try {\n"
            + "  alert(xhr.getResponseHeader(''));\n"
            + "} catch(e) { alert('exception-getEmpty'); }\n"
            // unknown
            + "try {\n"
            + "  alert(xhr.getResponseHeader('XXX'));\n"
            + "} catch(e) { alert('exception-getUnknown'); }\n"
            // case-sensitive
            + "try {\n"
            + "  alert(xhr.getResponseHeader('cOntEnt-typE'));\n"
            + "} catch(e) { alert('exception-getCase'); }\n";

        tester(test, "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 GET localhost/bounce?null,-1 ",
        "4:200 POST localhost/bounce?null,0 ",
        "4:200 PUT localhost/bounce?null,0 ",
        "4:200  ",
        "exception-methodTRACE", "0:ex status ex text ex xml",
        "exception-methodNull", "0:ex status ex text ex xml",
        "exception-methodEmpty", "0:ex status ex text ex xml",
        "1:ex status ex text ", "4:  " })
    public void open_method() throws Exception {
        final String test = ""
            // GET
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-methodGET'); }\n"
            // POST
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-methodPOST'); }\n"
            // PUT
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-methodPUT'); }\n"
            // HEAD
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-methodHEAD'); }\n"
            // TRACE
            + "try {\n"
            + "  xhr.open('TRACE', '/bounce', false);\n"
            + "} catch(e) { alert('exception-methodTRACE'); }\n"
            + "debugRequest(xhr);\n"
            // null
            + "try {\n"
            + "  xhr.open(null, '/bounce', false);\n"
            + "} catch(e) { alert('exception-methodNull'); }\n"
            + "debugRequest(xhr);\n"
            // empty
            + "try {\n"
            + "  xhr.open('', '/bounce', false);\n"
            + "} catch(e) { alert('exception-methodEmpty'); }\n"
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
            + "  alert(msg);\n"
            + "} catch(e) { alert('exception-methodInvalid'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 DELETE localhost/bounce?null,0 ",
        "4:200 OPTIONS localhost/bounce?null,0 " })
    @NotYetImplemented(IE)
    // HtmlUnit does not send a body for DELETE and OPTIONS requests
    public void open_methodNYI() throws Exception {
        final String test = ""
            // DELETE
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-methodDELETE'); }\n"
            // OPTIONS
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-methodOPTIONS'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 GET localhost/bounce?null,-1 ",
        "4:200 GET localhost/bounce?null,-1 ",
        "exception-urlNull", "4:200 GET localhost/bounce?null,-1 ",
        "exception-urlEmpty", "0:ex status ex text ex xml",
        "exception-urlNotFound", "4:  " })
    public void open_url() throws Exception {
        final String test = ""
            // relative
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-urlRelative'); }\n"
            // absolute
            + "try {\n"
            + "  xhr.open('GET', '" + URL_FIRST + "bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-urlAbsolute'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('GET', null, false);\n"
            + "} catch(e) { alert('exception-urlNull'); }\n"
            + "debugRequest(xhr);\n"
            // empty
            + "try {\n"
            + "  xhr.open('GET', '', false);\n"
            + "} catch(e) { alert('exception-urlEmpty'); }\n"
            + "debugRequest(xhr);\n"
            // not found
            + "try {\n"
            + "  xhr.open('GET', 'http://localhost:9876/doesntexist', false);\n"
            + "  xhr.send();\n"
            + "} catch(e) { alert('exception-urlNotFound'); }\n"
            // debug without status
            + "var msg = xhr.readyState + ':';\n"
            + "try {\n"
            + "  msg += ' ' + xhr.responseText;\n"
            + "} catch(e) { msg += ' ex text'; }\n"
            + "try {\n"
            + "  msg += ' ' + xhr.responseXML.xml;\n"
            + "} catch(e) { msg += ' ex xml'; }\n"
            + "alert(msg);\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("4:200 Basic:Zm9vOmJhcg== ")
    public void open_authentication() throws Exception {
        final String test = ""
            + "try {\n"
            + "  xhr.open('GET', '/protected/token', false, 'foo', 'bar');\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-auth'); }\n";

        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
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
            + "    alert(msg);\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/protected/token", BasicAuthenticationServlet.class);

        loadPageWithAlerts2(createTestHTML(html), servlets);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 GET localhost/bounce?null,-1 ",
        "4:200 GET localhost/bounce?null,-1 ",
        "4:200 GET localhost/bounce?null,-1 ",
        "4:200 POST localhost/bounce?null,4 " })
    public void send_get() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('GET', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 POST localhost/bounce?null,0 ",
        "4:200 POST localhost/bounce?null,0 ",
        "4:200 POST localhost/bounce?null,0 ",
        "4:200 POST localhost/bounce?null,4 " })
    public void send_post() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('POST', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 PUT localhost/bounce?null,0 ",
        "4:200 PUT localhost/bounce?null,0 ",
        "4:200 PUT localhost/bounce?null,0 ",
        "4:200 PUT localhost/bounce?null,4 " })
    public void send_put() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('PUT', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200  ",
        "4:200  ",
        "4:200  ",
        "4:200  " })
    public void send_head() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('HEAD', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 DELETE localhost/bounce?null,0 ",
        "4:200 DELETE localhost/bounce?null,0 ",
        "4:200 DELETE localhost/bounce?null,0 ",
        "4:200 DELETE localhost/bounce?null,4 " })
    @NotYetImplemented(IE)
    // HtmlUnit does not send a body for DELETE and OPTIONS requests
    public void send_delete() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('DELETE', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "4:200 OPTIONS localhost/bounce?null,0 ",
        "4:200 OPTIONS localhost/bounce?null,0 ",
        "4:200 OPTIONS localhost/bounce?null,0 ",
        "4:200 OPTIONS localhost/bounce?null,4 " })
    @NotYetImplemented(IE)
    // HtmlUnit does not send a body for DELETE and OPTIONS requests
    public void send_options() throws Exception {
        final String test = ""
            // no parameter
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send();\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNoParameter'); }\n"
            // null
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send(null);\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendNull'); }\n"
            // empty
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send('');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-sendEmpty'); }\n"
            // normal
            + "try {\n"
            + "  xhr.open('OPTIONS', '/bounce', false);\n"
            + "  xhr.send('test');\n"
            + "  debugRequest(xhr);\n"
            + "} catch(e) { alert('exception-send'); }\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "exception-resend",
        "4:200 GET localhost/bounce?null,-1 " })
    public void send_resend() throws Exception {
        final String test = ""
            + "xhr.open('GET', '/bounce', false);\n"
            + "xhr.send();\n"
            + "try {\n"
            + "  xhr.send();\n"
            + "} catch(e) { alert('exception-resend'); }\n"
            + "debugRequest(xhr);\n";

        tester_bounce(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "*/*",
        "gzip, deflate",
        "null",
        "localhost:12345",
        "§§URL§§" })
    public void send_headersDefaultEmpty() throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedHeaders = getExpectedAlerts();
        setExpectedAlerts();

        final String test = ""
            + "try {\n"
            + "  xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "  xhr.send();\n"
            + "} catch(e) { alert('exception-send'); }\n";

        tester(test, "");

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals(expectedHeaders[0], "" + headers.get("Accept"));
        assertEquals(expectedHeaders[1], "" + headers.get("Accept-Encoding"));
        assertEquals(expectedHeaders[2], "" + headers.get("Content-Length"));
        assertEquals(expectedHeaders[3], "" + headers.get("Host"));
        assertEquals(expectedHeaders[4], "" + headers.get("Referer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "*/*",
        "gzip, deflate",
        "4",
        "localhost:12345",
        "§§URL§§" })
    public void send_headersDefaultBody() throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedHeaders = getExpectedAlerts();
        setExpectedAlerts();

        final String test = ""
            + "try {\n"
            + "  xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "  xhr.send('1234');\n"
            + "} catch(e) { alert('exception-send'); }\n";

        tester(test, "");

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals(expectedHeaders[0], "" + headers.get("Accept"));
        assertEquals(expectedHeaders[1], "" + headers.get("Accept-Encoding"));
        assertEquals(expectedHeaders[2], "" + headers.get("Content-Length"));
        assertEquals(expectedHeaders[3], "" + headers.get("Host"));
        assertEquals(expectedHeaders[4], "" + headers.get("Referer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "bar",
        "application/javascript",
        "null",
        "null",
        "exception-unopened",
        "exception-setNameNull",
        "exception-setNameEmpty",
        "exception-setValueNull" })
    public void setRequestHeader() throws Exception {
        final String[] expectedAlerts = getExpectedAlerts();
        setExpectedAlerts(
                Arrays.copyOfRange(expectedAlerts, expectedAlerts.length - 4, expectedAlerts.length));
        final String[] expectedHeaders =
                Arrays.copyOfRange(expectedAlerts, 0, expectedAlerts.length - 4);

        final String test = ""
            // before open
            + "try {\n"
            + "  xhr.setRequestHeader('Foo', 'bar');\n"
            + "} catch(e) { alert('exception-unopened'); }\n"
            + "xhr.open('GET', '" + URL_SECOND + "', false);\n"
            // normal
            + "try {\n"
            + "  xhr.setRequestHeader('foo', 'bar');\n"
            + "} catch(e) { alert('exception-set'); }\n"
            // overwrite
            + "try {\n"
            + "  xhr.setRequestHeader('Accept', 'application/javascript');\n"
            + "} catch(e) { alert('exception-setOverwrite'); }\n"
            // null name
            + "try {\n"
            + "  xhr.setRequestHeader(null, 'NameNull');\n"
            + "} catch(e) { alert('exception-setNameNull'); }\n"
            // empty name
            + "try {\n"
            + "  xhr.setRequestHeader('', 'NameEmpty');\n"
            + "} catch(e) { alert('exception-setNameEmpty'); }\n"
            // null value
            + "try {\n"
            + "  xhr.setRequestHeader('ValueNull', null);\n"
            + "} catch(e) { alert('exception-setValueNull'); }\n"
            // empty value
            + "try {\n"
            + "  xhr.setRequestHeader('ValueEmpty', '');\n"
            + "} catch(e) { alert('exception-setValueEmpty'); }\n"
            // blank value
            + "try {\n"
            + "  xhr.setRequestHeader('ValueEmpty', ' ');\n"
            + "} catch(e) { alert('exception-setValueBlank'); }\n"
            + "xhr.send();\n";

        tester(test, "");

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals(expectedHeaders[0], "" + headers.get("foo"));
        assertEquals(expectedHeaders[1], "" + headers.get("Accept"));
        assertEquals(expectedHeaders[2], "" + headers.get("ValueEmpty"));
        assertEquals(expectedHeaders[3], "" + headers.get("ValueBlank"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "null", "null", "4" })
    public void setRequestHeader_contentLength() throws Exception {
        final String[] expectedHeaders = getExpectedAlerts();
        setExpectedAlerts();

        setRequestHeader_contentLength(null, expectedHeaders[0]);
        setRequestHeader_contentLength("", expectedHeaders[1]);
        setRequestHeader_contentLength("1234", expectedHeaders[2]);
    }

    private void setRequestHeader_contentLength(final String content, final String contentLength) throws Exception {
        String test = ""
            + "xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "try {\n"
            + "  xhr.setRequestHeader('Content-Length', '20');\n"
            + "} catch(e) { alert('exception-set'); }\n";
        if (content == null) {
            test += "xhr.send();\n";
        }
        else {
            test += "xhr.send('" + content + "');\n";
        }

        tester(test, "");

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals(contentLength, "" + headers.get("Content-Length"));
    }

    private void property(final String property) throws Exception {
        tester("alert(xhr." + property + ");\n");
    }

    private void property_lifecycleSync(final String property) throws Exception {
        final String test = ""
            // create
            + "try {\n"
            + "  alert(xhr." + property + ");\n"
            + "} catch(e) { alert('exception-created'); }\n"
            // open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "try {\n"
            + "  alert(xhr." + property + ");\n"
            + "} catch(e) { alert('exception-opened'); }\n"
            // send
            + "xhr.send();\n"
            + "try {\n"
            + "  alert(xhr." + property + ");\n"
            + "} catch(e) { alert('exception-sent'); }\n"
            // re-open
            + "xhr.open(\"GET\", \"" + URL_SECOND + "\", false);\n"
            + "try {\n"
            + "  alert(xhr." + property + ");\n"
            + "} catch(e) { alert('exception-reopened'); }\n"
            // send
            + "xhr.send();\n"
            + "try {\n"
            + "  alert(xhr." + property + ");\n"
            + "} catch(e) { alert('exception-sent'); }\n";

        tester(test);
    }

    private void property_lifecycleAsync(final String property) throws Exception {
        final String test = ""
            + "xhr.onreadystatechange = function() {\n"
            + "  try {\n"
            + "    alert(xhr.readyState + ':' + xhr." + property + ");\n"
            + "  } catch(e) { alert(xhr.readyState + ':exception-async'); }\n"
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
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function onStateChange() {\n"
            + "    alert('orsc:' + xhr.readyState);\n"
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
            + "    alert(msg);\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    private void tester_bounce(final String test) throws Exception {
        final String html = ""
            + "  var xhr;\n"
            + "  function test() {\n"
            + "    xhr = " + callCreateXMLHTTPRequest() + ";\n"
            + "    try {\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function onStateChange() {\n"
            + "    alert('orsc:' + xhr.readyState);\n"
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
            + "    alert(msg);\n"
            + "  }\n"
            + CREATE_XMLHTTPREQUEST_FUNCTION;

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/bounce", BounceServlet.class);

        loadPageWithAlerts2(createTestHTML(html), servlets);
    }

    /**
     * Servlet bouncing requests.
     */
    public static class BounceServlet extends HttpServlet {
        private static final long serialVersionUID = -2408889391981343295L;

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

        private void bounce(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            final Writer writer = resp.getWriter();
            writer.write(req.getMethod() + " " + req.getServerName() + req.getRequestURI()
                    + "?" + req.getQueryString() + ',' + req.getContentLength());
            writer.close();
        }
    }
}
