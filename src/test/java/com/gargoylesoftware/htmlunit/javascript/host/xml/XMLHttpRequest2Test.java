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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

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
    @Alerts("text/javascript, application/javascript, */*:ar-eg")
    public void setRequestHeader() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/first.html", SetRequestHeaderServlet1.class);
        servlets.put("/second.html", SetRequestHeaderServlet2.class);

        startWebServer(".", null, servlets);
        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/first.html");
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Servlet for {@link #setRequestHeader}.
     */
    public static class SetRequestHeaderServlet1 extends ServletContentWrapper {
        private static final long serialVersionUID = -7430217650295223528L;

        /** Constructor. */
        public SetRequestHeaderServlet1() {
            super(getModifiedContent("<html><head><script>\n"
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
                + "    alert(request.responseText);\n"
                + "  }\n"
                + "</script></head><body onload='test()'></body></html>"));
        }
    }

    /**
     * Servlet for {@link #setRequestHeader}.
     */
    public static class SetRequestHeaderServlet2 extends HttpServlet {
        private static final long serialVersionUID = 738149883022903302L;

        /** {@inheritDoc} */
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            resp.setContentType("text/html");
            resp.getWriter().write(req.getHeader("Accept") + ':' + req.getHeader("Accept-Language"));
        }
    }

}
