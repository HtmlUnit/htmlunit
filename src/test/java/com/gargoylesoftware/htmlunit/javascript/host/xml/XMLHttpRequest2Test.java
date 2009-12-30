/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.net.URL;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Additional tests for {@link XMLHttpRequest} using already WebDriverTestCase.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequest2Test extends WebDriverTestCase {
    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    /**
     * This produced a deadlock situation with HtmlUnit-2.6 and HttmlUnit-2.7-SNAPSHOT on 17.09.09.
     * The reason is that HtmlUnit has currently one "JS execution thread" per window, synchronizing on the
     * owning page BUT the XHR callback execution are synchronized on their "owning" page.
     * This test isn't really executed now to avoid the deadlock.
     * Strangely, this test seem to fail even without the implementation of the "/setStateXX" handling
     * on the "server side".
     * Strange thing.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void deadlock() throws Exception {
        final String jsCallSynchXHR = "function callSynchXHR(url) {\n"
            + "  var xhr = " + XHRInstantiation_ + ";\n"
            + "  xhr.open('GET', url, false);\n"
            + "  xhr.send('');\n"
            + "}\n";
        final String jsCallASynchXHR = "function callASynchXHR(url) {\n"
            + "  var xhr = " + XHRInstantiation_ + ";\n"
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
            + "  callSynchXHR('/setState1/setState3')\n"
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
            + "  callSynchXHR('/setState2')\n"
            + "  // call function with XHR and handler in parent\n"
            + "  parent.callASynchXHR('/fooCalledFromMainCalledFromFrame');\n"
            + "}\n"
            + "setTimeout(testFrame, 10);\n"
            + "</script></head>\n"
            + "<body></body></html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "frame.html"), frame);
        getMockWebConnection().setDefaultResponse(""); // for all XHR

        // just to avoid unused variable warning when the next line is commented
        getMockWebConnection().setResponse(getDefaultUrl(), html);
        // loadPageWithAlerts2(html);
        Assert.fail("didn't run the real test as it causes a deadlock");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setRequestHeader() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var request = " + XHRInstantiation_ + ";\n"
            + "    request.open('GET', 'second.html', false);\n"
            + "    request.setRequestHeader('Accept', 'text/javascript, application/javascript, */*');\n"
            + "    request.setRequestHeader('Accept-Language', 'ar-eg');\n"
            + "    request.send('');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);

        final WebRequestSettings lastRequest = getMockWebConnection().getLastWebRequestSettings();
        final Map<String, String> headers = lastRequest.getAdditionalHeaders();
        assertEquals("text/javascript, application/javascript, */*", headers.get("Accept"));
        assertEquals("ar-eg", headers.get("Accept-Language"));
    }

    /**
     * XHR.open throws an exception if URL parameter is null or empty string.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "exception", "exception", "pass", "pass" })
    public void openThrowOnEmptyUrl() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "var request = " + XHRInstantiation_ + ";\n"
            + "var values = [null, '', ' ', '  \\t  '];\n"
            + "for (var i=0; i<values.length; ++i) {\n"
            + "  try {\n"
            + "    request.open('GET', values[i], false);\n"
            + "    request.send('');\n"
            + "    alert('pass');\n"
            + "  } catch(e) { alert('exception') }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body></body>\n</html>";

        loadPageWithAlerts2(html);
        assertEquals(3, getMockWebConnection().getRequestCount());
    }

    /**
     * Test access to the XML DOM.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "bla", "someAttr", "someValue", "true", "foo", "2", "fi1" })
    public void responseXML() throws Exception {
        testResponseXML("text/xml");
        testResponseXML(null);
    }

    /**
     * Test access to responseXML when the content type indicates that it is not XML.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void responseXML_badContentType() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = " + XHRInstantiation_ + ";\n"
            + "  request.open('GET', 'foo.xml', false);\n"
            + "  request.send('');\n"
            + "  alert(request.responseXML);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlFoo = new URL(URL_FIRST + "foo.xml");
        getMockWebConnection().setResponse(urlFoo, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            "text/plain");
        loadPageWithAlerts2(html);
    }

    private void testResponseXML(final String contentType) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var request = " + XHRInstantiation_ + ";\n"
            + "  request.open('GET', 'foo.xml', false);\n"
            + "  request.send('');\n"
            + "  var childNodes = request.responseXML.childNodes;\n"
            + "  alert(childNodes.length);\n"
            + "  var rootNode = childNodes[0];\n"
            + "  alert(rootNode.nodeName);\n"
            + "  alert(rootNode.attributes[0].nodeName);\n"
            + "  alert(rootNode.attributes[0].nodeValue);\n"
            + "  alert(rootNode.attributes['someAttr'] == rootNode.attributes[0]);\n"
            + "  alert(rootNode.firstChild.nodeName);\n"
            + "  alert(rootNode.firstChild.childNodes.length);\n"
            + "  alert(request.responseXML.getElementsByTagName('fi').item(0).attributes[0].nodeValue);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlFoo = new URL(URL_FIRST + "foo.xml");
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
            + "  var request = " + XHRInstantiation_ + ";\n"
            + "  request.open('GET', 'foo.xml', false);\n"
            + "  alert(request.responseXML);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        final URL urlFoo = new URL(URL_FIRST + "foo.xml");
        getMockWebConnection().setResponse(urlFoo, "<bla someAttr='someValue'><foo><fi id='fi1'/><fi/></foo></bla>\n",
            "text/plain");
        loadPageWithAlerts2(html);
    }

    /**
     * Test for Bug 2891430: HtmlUnit should not violate the same-origin policy with FF3.
     * Note: FF3.5 doesn't enforce this same-origin policy.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void sameOriginPolicy() throws Exception {
        sameOriginPolicy(URL_THIRD.toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = "ok")
    public void sameOriginPolicy_aboutBlank() throws Exception {
        sameOriginPolicy("about:blank");
    }

    private void sameOriginPolicy(final String url) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = " + XHRInstantiation_ + ";\n"
            + "  try {\n"
            + "    xhr.open('GET', '" + url + "', false);\n"
            + "    alert('ok');\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        getMockWebConnection().setResponse(URL_THIRD, "<bla/>", "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void put() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var request = " + XHRInstantiation_ + ";\n"
            + "    request.open('PUT', 'second.html', false);\n"
            + "    request.send('Something');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);

        final String requestBody = getMockWebConnection().getLastWebRequestSettings().getRequestBody();
        assertEquals("Something", requestBody);
    }
}
