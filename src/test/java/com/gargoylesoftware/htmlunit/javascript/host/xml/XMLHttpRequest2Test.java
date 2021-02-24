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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequestTest.BasicAuthenticationServlet;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Additional tests for {@link XMLHttpRequest} using already WebDriverTestCase.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Sebastian Cato
 * @author Frank Danek
 * @author Thorsten Wendelmuth
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequest2Test extends WebDriverTestCase {

    /**
     * This produced a deadlock situation with HtmlUnit-2.6 and HttmlUnit-2.7-SNAPSHOT on 17.09.09.
     * The reason is that HtmlUnit has currently one "JS execution thread" per window, synchronizing on the
     * owning page BUT the XHR callback execution are synchronized on their "owning" page.
     * This test isn't really executed now to avoid the deadlock.
     * Strangely, this test seem to fail even without the implementation of the "/setStateXX" handling
     * on the "server side".
     * Strange thing.
     *
     * Update 28.01.2013:
     * no deadlock occur anymore (we use a single JS execution thread for a while). Activating the test as it may help.
     * Update 28.02.2013:
     * deadlock does occur (at least on the build server). Disabling the test again.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Ignore
    public void deadlock() throws Exception {
        final String jsCallSynchXHR = "function callSynchXHR(url) {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', url, false);\n"
            + "  xhr.send('');\n"
            + "}\n";
        final String jsCallASynchXHR = "function callASynchXHR(url) {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  var handler = function() {\n"
            + "    if (xhr.readyState == 4)\n"
            + "      alert(xhr.responseText);\n"
            + "  }\n"
            + "  xhr.onreadystatechange = handler;\n"
            + "  xhr.open('GET', url, true);\n"
            + "  xhr.send('');\n"
            + "}\n";
        final String html = "<html><head><script>\n"
            + jsCallSynchXHR
            + jsCallASynchXHR
            + "function testMain() {\n"
            + "  // set state 1 and wait for state 2\n"
            + "  callSynchXHR('/setState1/setState3');\n"
            + "  // call function with XHR and handler in frame\n"
            + "  myFrame.contentWindow.callASynchXHR('/fooCalledFromFrameCalledFromMain');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='testMain()'>\n"
            + "<iframe id='myFrame' src='frame.html'></iframe>\n"
            + "</body></html>";

        final String frame = "<html><head><script>\n"
            + jsCallSynchXHR
            + jsCallASynchXHR
            + "function testFrame() {\n"
            + "  // set state 2\n"
            + "  callSynchXHR('/setState2');\n"
            + "  // call function with XHR and handler in parent\n"
            + "  parent.callASynchXHR('/fooCalledFromMainCalledFromFrame');\n"
            + "}\n"
            + "setTimeout(testFrame, 10);\n"
            + "</script></head>\n"
            + "<body></body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frame);
        getMockWebConnection().setDefaultResponse(""); // for all XHR

        loadPage2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setRequestHeader() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'second.html', false);\n"
            + "    xhr.setRequestHeader('Accept', 'text/javascript, application/javascript, */*');\n"
            + "    xhr.setRequestHeader('Accept-Language', 'ar-eg');\n"
            + "    xhr.send('');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals("text/javascript, application/javascript, */*", headers.get(HttpHeader.ACCEPT));
        assertEquals("ar-eg", headers.get(HttpHeader.ACCEPT_LANGUAGE));
    }

    /**
     * Content-Length header is simply ignored by browsers as it
     * is the browser's responsibility to set it.
     * @throws Exception if an error occurs
     */
    @Test
    public void requestHeader_contentLength() throws Exception {
        requestHeader_contentLength("1234");
        requestHeader_contentLength("11");
        requestHeader_contentLength(null);
    }

    private void requestHeader_contentLength(final String headerValue) throws Exception {
        final String body = "hello world";
        final String setHeader = headerValue == null ? ""
                : "xhr.setRequestHeader('Content-length', 1234);\n";
        final String html = "<html><body><script>\n"
            + "var xhr = new XMLHttpRequest();\n"
            + "xhr.open('POST', 'second.html', false);\n"
            + "var body = '" + body + "';\n"
            + "xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');\n"
            + setHeader
            + "xhr.send(body);\n"
            + "</script></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals("" + body.length(), headers.get(HttpHeader.CONTENT_LENGTH));
    }

    /**
     * XHR.open throws an exception if URL parameter is null or empty string.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"5", "pass", "pass", "pass", "pass"},
            IE = {"1", "exception", "exception", "pass", "pass"})
    @HtmlUnitNYI(IE = {"3", "exception", "exception", "pass", "pass"})
    // real IE invokes just one request and returns the other two responses from it's cache
    public void openThrowOnEmptyUrl() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "var xhr = new XMLHttpRequest();\n"
            + "var values = [null, '', ' ', '  \\t  '];\n"
            + "for (var i = 0; i < values.length; i++) {\n"
            + "  try {\n"
            + "    xhr.open('GET', values[i], false);\n"
            + "    xhr.send('');\n"
            + "    alert('pass');\n"
            + "  } catch(e) { alert('exception') }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body></body>\n</html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final int expectedRequests = Integer.parseInt(getExpectedAlerts()[0]);
        setExpectedAlerts(Arrays.copyOfRange(getExpectedAlerts(), 1, getExpectedAlerts().length));

        loadPageWithAlerts2(html);

        assertEquals(expectedRequests, getMockWebConnection().getRequestCount());
    }

    /**
     * Test access to the XML DOM.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "bla", "someAttr", "someValue", "true", "foo", "2", "fi1"})
    public void responseXML() throws Exception {
        shutDownRealIE();

        testResponseXML(MimeType.TEXT_XML);
        testResponseXML(null);
    }

    /**
     * Test access to responseXML when the content type indicates that it is not XML.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_badContentType() throws Exception {
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.xml', false);\n"
            + "  xhr.send('');\n"
            + "  alert(xhr.responseXML);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlFoo = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlFoo, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
                MimeType.TEXT_PLAIN);
        loadPageWithAlerts2(html);
    }

    private void testResponseXML(final String contentType) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.xml', false);\n"
            + "  xhr.send('');\n"
            + "  var childNodes = xhr.responseXML.childNodes;\n"
            + "  alert(childNodes.length);\n"
            + "  var rootNode = childNodes[0];\n"
            + "  alert(rootNode.nodeName);\n"
            + "  alert(rootNode.attributes[0].nodeName);\n"
            + "  alert(rootNode.attributes[0].nodeValue);\n"
            + "  alert(rootNode.attributes['someAttr'] == rootNode.attributes[0]);\n"
            + "  alert(rootNode.firstChild.nodeName);\n"
            + "  alert(rootNode.firstChild.childNodes.length);\n"
            + "  alert(xhr.responseXML.getElementsByTagName('fi').item(0).attributes[0].nodeValue);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlFoo = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlFoo, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            contentType);
        loadPageWithAlerts2(html);
    }

    /**
     * Test access to responseXML when the content type indicates that it is not XML.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_sendNotCalled() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.xml', false);\n"
            + "  alert(xhr.responseXML);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlFoo = new URL(URL_FIRST, "foo.xml");
        getMockWebConnection().setResponse(urlFoo, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
                MimeType.TEXT_PLAIN);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for Bug 2891430: HtmlUnit should not violate the same-origin policy with FF3.
     * Note: FF3.5 doesn't enforce this same-origin policy.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void sameOriginPolicy() throws Exception {
        sameOriginPolicy(URL_THIRD.toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "ok",
            IE = "exception")
    public void sameOriginPolicy_aboutBlank() throws Exception {
        sameOriginPolicy("about:blank");
    }

    private void sameOriginPolicy(final String url) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  try {\n"
            + "    xhr.open('GET', '" + url + "', false);\n"
            + "    alert('ok');\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_THIRD, "<bla/>", MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void put() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('PUT', 'second.html', false);\n"
            + "    xhr.send('Something');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);

        final String requestBody = getMockWebConnection().getLastWebRequest().getRequestBody();
        assertEquals("Something", requestBody);
    }

    /**
     * Regression test for bug 2952333.
     * This test was causing a java.lang.ClassCastException:
     * com.gargoylesoftware.htmlunit.xml.XmlPage cannot be cast to com.gargoylesoftware.htmlunit.html.HtmlPage
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void iframeInResponse() throws Exception {
        final String html = "<html><head><script>\n"
            + "var xhr = new XMLHttpRequest();\n"
            + "xhr.open('GET', 'foo.xml', false);\n"
            + "xhr.send('');\n"
            + "alert(xhr.responseXML);\n"
            + "</script></head><body></body></html>";

        final String xml = "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
            + "<body><iframe></iframe></body></html>";
        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * Ensures that XHR download is performed without altering other JS jobs.
     * Currently HtmlUnit doesn't behave correctly here because download and callback execution
     * are executed within the same synchronize block on the HtmlPage.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"in timeout", "hello"})
    @HtmlUnitNYI(CHROME = {"hello", "in timeout"},
            EDGE = {"hello", "in timeout"},
            FF = {"hello", "in timeout"},
            FF78 = {"hello", "in timeout"},
            IE = {"hello", "in timeout"})
    // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void xhrDownloadInBackground() throws Exception {
        final String html = "<html><head><script>\n"
            + "var xhr = new XMLHttpRequest();\n"
            + "var handler = function() {\n"
            + "  if (xhr.readyState == 4)\n"
            + "    alert(xhr.responseText);\n"
            + "}\n"
            + "xhr.onreadystatechange = handler;\n"
            + "xhr.open('GET', '/delay200/foo.txt', true);\n"
            + "xhr.send('');\n"
            + "setTimeout(function() { alert('in timeout');}, 5);\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setDefaultResponse("hello", MimeType.TEXT_PLAIN);
        loadPageWithAlerts2(html);
    }

    /**
     * Ensures that XHR callback is executed before a timeout, even if it is time
     * to execute this one.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("hello in timeout")
    @BuggyWebDriver(FF78 = "in timeouthello",
                    FF = "in timeouthello",
                    IE = "in timeouthello")
    // IEDriver catches "in timeout", "hello" but real IE gets the correct order
    public void xhrCallbackBeforeTimeout() throws Exception {
        final String html = "<html><head><script>\n"
            + "function wait() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', '/delay200/foo.txt', false);\n"
            + "  xhr.send('');\n"
            + "}\n"
            + "function doTest() {\n"
            + "  setTimeout(function() { document.title += ' in timeout'; }, 5);\n"
            + "  wait();\n"
            + "  var xhr2 = new XMLHttpRequest();\n"
            + "  var handler = function() {\n"
            + "    if (xhr2.readyState == 4)\n"
            + "      document.title += xhr2.responseText;\n"
            + "  }\n"
            + "  xhr2.onreadystatechange = handler;\n"
            + "  xhr2.open('GET', '/foo.txt', true);\n"
            + "  xhr2.send('');\n"
            + "  wait();\n"
            + "}\n"
            + "setTimeout(doTest, 10);\n"
            + "</script></head><body></body></html>";

        getMockWebConnection().setDefaultResponse("hello", MimeType.TEXT_PLAIN);
        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("a=b,0")
    public void post() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('POST', '/test2?a=b', false);\n"
            + "  xhr.send('');\n"
            + "  alert(xhr.responseText);\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PostServlet2.class);

        loadPageWithAlerts2(html, servlets);
    }

    /**
     * Servlet for {@link #post()}.
     */
    public static class PostServlet2 extends HttpServlet {

        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            final Writer writer = resp.getWriter();
            writer.write(req.getQueryString() + ',' + req.getContentLength());
        }
    }

    /**
     * Firefox up to 3.6 does not call "onreadystatechange" handler if sync.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4")
    public void onreadystatechange_sync() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"
            + "      function onStateChange() {\n"
            + "        alert(xhr.readyState);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "<content>blah2</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * Firefox up to 3.6 does not call "onreadystatechange" handler if sync.
     * Firefox provides an event parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Event]#[object XMLHttpRequest]")
    public void onreadystatechangeSyncWithParam() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"
            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          if(e) alert(e + '#' + e.target);\n"
            + "          else alert('no param');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    /**
     * Firefox up to 3.6 does not call "onreadystatechange" handler if sync.
     * Firefox provides an event parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Event]#[object XMLHttpRequest]")
    public void onreadystatechangeAsyncWithParam() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        xhr.open('GET', '" + URL_SECOND + "', true);\n"
            + "        xhr.onreadystatechange = onStateChange;\n"
            + "        xhr.send('');\n"
            + "      }\n"
            + "      function onStateChange(e) {\n"
            + "        if (xhr.readyState == 4) {\n"
            + "          if(e) alert(e + '#' + e.target);\n"
            + "          else alert('no param');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html, 2 * DEFAULT_WAIT_TIME);
    }

    /**
     * Test the simplest CORS case. A cross-origin simple request,
     * server replies "allow *".
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"ok", "4"})
    public void sameOriginCorsSimple() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  try {\n"
            + "    xhr.open('GET', '" + URL_CROSS_ORIGIN + "', false);\n"
            + "    alert('ok');\n"
            + "    xhr.send();\n"
            + "    alert(xhr.readyState);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("access-control-allow-origin", "*"));
        getMockWebConnection().setResponse(URL_CROSS_ORIGIN,
                                           "<empty/>",
                                           200,
                                           "OK",
                                           MimeType.TEXT_XML,
                                           UTF_8, responseHeaders);
        loadPageWithAlerts2(html);
    }

    /**
     * Test the correct origin header.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"ok", "4", "<null>"})
    public void baseUrlAbsoluteRequest() throws Exception {
        final String html = "<html><head>\n"
            + "<base href='" + URL_CROSS_ORIGIN_BASE + "'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  try {\n"
            + "    xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "    alert('ok');\n"
            + "    xhr.send();\n"
            + "    alert(xhr.readyState);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("access-control-allow-origin", "*"));
        getMockWebConnection().setResponse(URL_SECOND,
                                           "<empty/>",
                                           200,
                                           "OK",
                                           MimeType.TEXT_XML,
                                           UTF_8, responseHeaders);

        final WebDriver driver = loadPage2(html);
        verifyAlerts(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 2));

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        String origin = lastAdditionalHeaders.get(HttpHeader.ORIGIN);
        if (origin == null) {
            origin = "<null>";
        }
        assertEquals(getExpectedAlerts()[2], origin);
    }

    /**
     * Test the correct origin header.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"ok", "4", "§§URL§§"})
    public void baseUrlAbsoluteRequestOtherUrl() throws Exception {
        final String html = "<html><head>\n"
            + "<base href='" + URL_CROSS_ORIGIN_BASE + "'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  try {\n"
            + "    xhr.open('GET', '" + URL_CROSS_ORIGIN2 + "', false);\n"
            + "    alert('ok');\n"
            + "    xhr.send();\n"
            + "    alert(xhr.readyState);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("access-control-allow-origin", "*"));
        getMockWebConnection().setResponse(URL_CROSS_ORIGIN2,
                                           "<empty/>",
                                           200,
                                           "OK",
                                           MimeType.TEXT_XML,
                                           UTF_8, responseHeaders);

        expandExpectedAlertsVariables("http://localhost:" + PORT);
        final WebDriver driver = loadPage2(html);
        verifyAlerts(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 2));

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        String origin = lastAdditionalHeaders.get(HttpHeader.ORIGIN);
        if (origin == null) {
            origin = "<null>";
        }
        assertEquals(getExpectedAlerts()[2], origin);
    }

    /**
     * Test the correct origin header.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"ok", "4", "§§URL§§"})
    public void baseUrlRelativeRequest() throws Exception {
        final String html = "<html><head>\n"
            + "<base href='" + URL_CROSS_ORIGIN_BASE + "'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  try {\n"
            + "    xhr.open('GET', 'corsAllowAll', false);\n"
            + "    alert('ok');\n"
            + "    xhr.send();\n"
            + "    alert(xhr.readyState);\n"
            + "  } catch(e) { alert('exception ' + e); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final List<NameValuePair> responseHeaders = new ArrayList<>();
        responseHeaders.add(new NameValuePair("access-control-allow-origin", "*"));
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "/corsAllowAll"),
                                           "<empty/>",
                                           200,
                                           "OK",
                                           MimeType.TEXT_XML,
                                           UTF_8, responseHeaders);

        expandExpectedAlertsVariables("http://localhost:" + PORT);
        final WebDriver driver = loadPage2(html);
        verifyAlerts(driver, Arrays.copyOfRange(getExpectedAlerts(), 0, 2));

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        String origin = lastAdditionalHeaders.get(HttpHeader.ORIGIN);
        if (origin == null) {
            origin = "<null>";
        }
        assertEquals(getExpectedAlerts()[2], origin);
    }

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Test XMLHttpRequest with basic authentication.
     * @throws Exception on failure
     */
    @Test
    @Alerts("Basic:Zm9vOmJhcg==")
    public void basicAuthenticationRequest() throws Exception {
        final String html =
                "<html>\n"
                        + "  <head>\n"
                        + "    <title>XMLHttpRequest Test</title>\n"
                        + "    <script>\n"
                        + "      var request;\n"
                        + "      function testBasicAuth() {\n"
                        + "        var request = new XMLHttpRequest();\n"
                        + "        request.open('GET', '/protected/token', false, 'foo', 'bar');\n"
                        + "        request.send();\n"
                        + "        alert(request.responseText);\n"
                        + "      }\n"
                        + "    </script>\n"
                        + "  </head>\n"
                        + "  <body onload='testBasicAuth()'>\n"
                        + "  </body>\n"
                        + "</html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/protected/token", BasicAuthenticationServlet.class);

        loadPageWithAlerts2(html, servlets);
    }

    /**
     * Test XMLHttpRequest with basic authentication.
     * @throws Exception on failure
     */
    @Test
    @Alerts("<xml></xml>")
    public void openNullUserIdNullPassword() throws Exception {
        final String html =
                "<html>\n"
                        + "  <head>\n"
                        + "    <title>XMLHttpRequest Test</title>\n"
                        + "    <script>\n"
                        + "      var request;\n"
                        + "      function testBasicAuth() {\n"
                        + "        var request = new XMLHttpRequest();\n"
                        + "        request.open('GET', '" + URL_SECOND + "', false, null, null);\n"
                        + "        request.send();\n"
                        + "        alert(request.responseText);\n"
                        + "      }\n"
                        + "    </script>\n"
                        + "  </head>\n"
                        + "  <body onload='testBasicAuth()'>\n"
                        + "  </body>\n"
                        + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "<xml></xml>", MimeType.TEXT_XML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("PATCH|some body data")
    public void patch() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('PATCH', '/test2', false);\n"
            + "  xhr.send('some body data');\n"
            + "  alert(xhr.responseText);\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PatchServlet2.class);

        loadPageWithAlerts2(html, servlets);
    }

    /**
     * Servlet for {@link #patch()}.
     */
    public static class PatchServlet2 extends HttpServlet {

        @Override
        protected void service(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            final Writer writer = resp.getWriter();
            writer.write(req.getMethod());
            writer.write('|');
            writer.write(IOUtils.toString(req.getReader()));
        }
    }

    /**
     * Servlet for {@link #encodedXml()}.
     */
    public static class EncodedXmlServlet extends HttpServlet {
        private static final String RESPONSE = "<xml><content>blah</content></xml>";

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final byte[] bytes = RESPONSE.getBytes(UTF_8);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final GZIPOutputStream gout = new GZIPOutputStream(bos);
            gout.write(bytes);
            gout.finish();

            final byte[] encoded = bos.toByteArray();

            response.setContentType(MimeType.TEXT_XML);
            response.setCharacterEncoding(UTF_8.name());
            response.setStatus(200);
            response.setContentLength(encoded.length);
            response.setHeader("Content-Encoding", "gzip");

            final OutputStream rout = response.getOutputStream();
            rout.write(encoded);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"<xml><content>blah</content></xml>", "text/xml;charset=utf-8", "gzip", "45"},
            IE = {"<xml><content>blah</content></xml>", "text/xml;charset=utf-8", "null", "null"})
    @HtmlUnitNYI(IE = {"<xml><content>blah</content></xml>", "text/xml;charset=utf-8", "gzip", "45"})
    public void encodedXml() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test", EncodedXmlServlet.class);

        final String html =
                "<html>\n"
                        + "  <head>\n"
                        + "    <title>XMLHttpRequest Test</title>\n"
                        + "    <script>\n"
                        + "      var request;\n"
                        + "      function testBasicAuth() {\n"
                        + "        var request = new XMLHttpRequest();\n"
                        + "        request.open('GET', '/test', false, null, null);\n"
                        + "        request.send();\n"
                        + "        alert(request.responseText);\n"
                        + "        alert(request.getResponseHeader('content-type'));\n"
                        + "        alert(request.getResponseHeader('content-encoding'));\n"
                        + "        alert(request.getResponseHeader('content-length'));\n"
                        + "      }\n"
                        + "    </script>\n"
                        + "  </head>\n"
                        + "  <body onload='testBasicAuth()'>\n"
                        + "  </body>\n"
                        + "</html>";

        loadPageWithAlerts2(html, servlets);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "",
                "content-type: text/xml;charset=iso-8859-1\n"
                + "date XYZ GMT\n"
                + "server: Jetty(XXX)\n"
                + "transfer-encoding: chunked\n"},
            IE = {"", "",
                "Date XYZ GMT\nContent-Type: text/xml;charset=iso-8859-1\n"
                    + "Transfer-Encoding: chunked\n"
                    + "Server: Jetty(XXX)\n\n"})
    @HtmlUnitNYI(CHROME = {"", "",
                "Date XYZ GMT\nContent-Type: text/xml;charset=iso-8859-1\n"
                    + "Transfer-Encoding: chunked\n"
                    + "Server: Jetty(XXX)\n"},
            EDGE = {"", "", "Date XYZ GMT\nContent-Type: text/xml;charset=iso-8859-1\n"
                + "Transfer-Encoding: chunked\n"
                + "Server: Jetty(XXX)\n"},
            FF = {"", "", "Date XYZ GMT\nContent-Type: text/xml;charset=iso-8859-1\n"
                + "Transfer-Encoding: chunked\n"
                + "Server: Jetty(XXX)\n"},
            FF78 = {"", "", "Date XYZ GMT\nContent-Type: text/xml;charset=iso-8859-1\n"
                + "Transfer-Encoding: chunked\n"
                + "Server: Jetty(XXX)\n"})
    public void getAllResponseHeaders() throws Exception {
        shutDownRealIE();

        final String html =
                "<html>\n"
                        + "  <head>\n"
                        + "    <title>XMLHttpRequest Test</title>\n"
                        + "    <script>\n"
                        + "      var request;\n"
                        + "      function testBasicAuth() {\n"
                        + "        var request = new XMLHttpRequest();\n"
                        + "        try {\n"
                        + "          alert(request.getAllResponseHeaders());\n"
                        + "        } catch(e) { alert('exception-created'); }\n"

                        + "        request.open('GET', '" + URL_SECOND + "', false, null, null);\n"
                        + "        try {\n"
                        + "          alert(request.getAllResponseHeaders());\n"
                        + "        } catch(e) { alert('exception-opened'); }\n"

                        + "        request.send();\n"
                        + "        try {\n"
                        + "          alert(request.getAllResponseHeaders().replace(/Jetty\\(.*\\)/, 'Jetty(XXX)')"
                        + "             .replace(/Date.*GMT/, 'Date XYZ GMT').replace(/date.*GMT/, 'date XYZ GMT'));\n"
                        + "        } catch(e) { alert('exception-sent'); }\n"
                        + "      }\n"
                        + "    </script>\n"
                        + "  </head>\n"
                        + "  <body onload='testBasicAuth()'>\n"
                        + "  </body>\n"
                        + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "<xml></xml>", MimeType.TEXT_XML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null", "null", "null",
            "text/xml;charset=iso-8859-1", "text/xml;charset=iso-8859-1",
            "text/xml;charset=iso-8859-1", "null"})
    public void getResponseHeader() throws Exception {
        final String html =
                "<html>\n"
                        + "  <head>\n"
                        + "    <title>XMLHttpRequest Test</title>\n"
                        + "    <script>\n"
                        + "      var request;\n"
                        + "      function testBasicAuth() {\n"
                        + "        var request = new XMLHttpRequest();\n"
                        + "        try {\n"
                        + "          alert(request.getResponseHeader('Content-Type'));\n"
                        + "          alert(request.getResponseHeader('unknown'));\n"
                        + "        } catch(e) { alert('exception-created'); }\n"

                        + "        request.open('GET', '" + URL_SECOND + "', false, null, null);\n"
                        + "        try {\n"
                        + "          alert(request.getResponseHeader('Content-Type'));\n"
                        + "          alert(request.getResponseHeader('unknown'));\n"
                        + "        } catch(e) { alert('exception-opened'); }\n"

                        + "        request.send();\n"
                        + "        try {\n"
                        + "          alert(request.getResponseHeader('Content-Type'));\n"
                        + "          alert(request.getResponseHeader('content-type'));\n"
                        + "          alert(request.getResponseHeader('coNTENt-type'));\n"
                        + "          alert(request.getResponseHeader('unknown'));\n"
                        + "        } catch(e) { alert('exception-sent'); }\n"
                        + "      }\n"
                        + "    </script>\n"
                        + "  </head>\n"
                        + "  <body onload='testBasicAuth()'>\n"
                        + "  </body>\n"
                        + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, "<xml></xml>", MimeType.TEXT_XML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception catched")
    public void createFromPrototypeAndDefineProperty() throws Exception {
        final String html = "<html><body><script>\n"
            + "var f = function() {};\n"
            + "f.prototype = Object.create(window.XMLHttpRequest.prototype);\n"
            + "try {\n"
            + "  f.prototype['onerror'] = function() {};\n"
            + "  alert('no exception');\n"
            + "} catch(e) { alert('exception catched'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception for onerror")
    @HtmlUnitNYI(CHROME = "read onerror",
            EDGE = "read onerror",
            FF = "read onerror",
            FF78 = "read onerror",
            IE = "read onerror")
    public void readPropertyFromPrototypeShouldThrow() throws Exception {
        final String html = "<html><body><script>\n"
            + "var p = 'onerror';\n"
            + "try {\n"
            + "  var x = window.XMLHttpRequest.prototype[p];\n"
            + "  alert('read ' + p);\n"
            + "} catch(e) { alert('exception for ' + p); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts("4")
    public void onreadystatechange_eventListener() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var xhr;\n"
            + "      function test() {\n"
            + "        xhr = new XMLHttpRequest();\n"
            + "        xhr.open('GET', '" + URL_SECOND + "', false);\n"
            + "        xhr.addEventListener('readystatechange', onStateChange);\n"
            + "        xhr.send('');\n"
            + "      }\n"
            + "      function onStateChange() {\n"
            + "        alert(xhr.readyState);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>\n"
            + "<content>blah</content>\n"
            + "<content>blah2</content>\n"
            + "</xml>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts("3")
    public void sendPostWithRedirect307() throws Exception {
        postRedirect(307, HttpMethod.POST, new URL(URL_FIRST, "/page2.html").toExternalForm(), "param=content");
    }

    @Test
    @Alerts("3")
    public void sendPostWithRedirect308() throws Exception {
        postRedirect(308, HttpMethod.POST, new URL(URL_FIRST, "/page2.html").toExternalForm(), "param=content");
    }

    private void postRedirect(final int code, final HttpMethod httpMethod,
            final String redirectUrl, final String content) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    try {\n"
            + "      xhr.open('POST', 'redirect.html', false);\n"
            + "      xhr.send('" + content + "');\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final int reqCount = getMockWebConnection().getRequestCount();

        final URL url = new URL(URL_FIRST, "page2.html");
        getMockWebConnection().setResponse(url, html);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", redirectUrl));
        getMockWebConnection().setDefaultResponse("", code, "* Redirect", null, headers);

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html);

        assertEquals(reqCount + Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());
        assertEquals(httpMethod, getMockWebConnection().getLastWebRequest().getHttpMethod());
        assertNotNull(getMockWebConnection().getLastWebRequest().getRequestBody());
        assertFalse(getMockWebConnection().getLastWebRequest().getRequestBody().isEmpty());
        assertEquals(content, getMockWebConnection().getLastWebRequest().getRequestBody());
    }
}
