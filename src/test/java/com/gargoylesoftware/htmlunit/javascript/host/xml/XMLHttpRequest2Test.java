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
            + "  var xhr;\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    xhr = new XMLHttpRequest();\n"
            + "  else\n"
            + "    xhr = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "  xhr.open('GET', url, false);\n"
            + "  xhr.send('');\n"
            + "}\n";
        final String jsCallASynchXHR = "function callASynchXHR(url) {\n"
            + "  var xhr;\n"
            + "  if (window.XMLHttpRequest)\n"
            + "    xhr = new XMLHttpRequest();\n"
            + "  else\n"
            + "    xhr = new ActiveXObject('Microsoft.XMLHTTP');\n"
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
            + "    var request;\n"
            + "    if (window.XMLHttpRequest)\n"
            + "      request = new XMLHttpRequest();\n"
            + "    else if (window.ActiveXObject)\n"
            + "      request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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
            + "var request;\n"
            + "if (window.XMLHttpRequest)\n"
            + "  request = new XMLHttpRequest();\n"
            + "else\n"
            + "  request = new ActiveXObject('Microsoft.XMLHTTP');\n"
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
}
