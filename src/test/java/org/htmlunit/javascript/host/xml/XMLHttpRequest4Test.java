/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link XMLHttpRequest}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Stuart Begg
 * @author Sudhan Moghe
 * @author Thorsten Wendelmuth
 * @author Atsushi Nakagawa
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequest4Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setLocation_onreadystatechange() throws Exception {
        final String content =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Page1</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        request = new XMLHttpRequest();\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        request.open('GET', 'foo.xml', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        if (request.readyState == 4) {\n"
            + "          window.location.href = 'about:blank';\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebWindow window = loadPage(content).getEnclosingWindow();
        assertEquals(0, window.getWebClient().waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals("about:blank", window.getEnclosedPage().getUrl());
    }

    @Test
    public void setLocation_addEventListener() throws Exception {
        final String content =
              "<html>\n"
            + "  <head>\n"
            + "    <title>Page1</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        request = new XMLHttpRequest();\n"
            + "        request.addEventListener('readystatechange', onReadyStateChange);\n"
            + "        request.open('GET', 'foo.xml', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        if (request.readyState == 4) {\n"
            + "          window.location.href = 'about:blank';\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebWindow window = loadPage(content).getEnclosingWindow();
        assertEquals(0, window.getWebClient().waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals("about:blank", window.getEnclosedPage().getUrl());
    }

    /**
     * Testing event invocation order.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onreadystatechange_1: readyState=1",
             "onreadystatechange_2: readyState=1",
             "onreadystatechange_p: readyState=1",
             "onreadystatechange_3: readyState=1",
             "onreadystatechange_4: readyState=1",
             "onreadystatechange_1: readyState=2",
             "onreadystatechange_2: readyState=2",
             "onreadystatechange_p: readyState=2",
             "onreadystatechange_3: readyState=2",
             "onreadystatechange_4: readyState=2",
             "onreadystatechange_1: readyState=3",
             "onreadystatechange_2: readyState=3",
             "onreadystatechange_p: readyState=3",
             "onreadystatechange_3: readyState=3",
             "onreadystatechange_4: readyState=3",
             "onreadystatechange_1: readyState=4",
             "onreadystatechange_2: readyState=4",
             "onreadystatechange_p: readyState=4",
             "onreadystatechange_3: readyState=4",
             "onreadystatechange_4: readyState=4"})
    public void eventInvocationOrder() throws Exception {
        final String html = ""
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "\n"
            + "  var onreadystatechange_1 = function (e) {\n"
            + "    alert('onreadystatechange_1: readyState=' + xhr.readyState);\n"
            + "  }\n"
            + "  var onreadystatechange_2 = function (e) {\n"
            + "    alert('onreadystatechange_2: readyState=' + xhr.readyState);\n"
            + "    e.stopPropagation();\n"
            + "  }\n"
            + "  var onreadystatechange_3 = function (e) {\n"
            + "    alert('onreadystatechange_3: readyState=' + xhr.readyState);\n"
            + "    e.stopPropagation();\n"
            + "  }\n"
            + "  var onreadystatechange_4 = function (e) {\n"
            + "    alert('onreadystatechange_4: readyState=' + xhr.readyState);\n"
            + "  }\n"
            + "\n"
            + "  var onreadystatechange_p = function (e) {\n"
            + "    alert('onreadystatechange_p: readyState=' + xhr.readyState);\n"
            + "  }\n"
            + "\n"
            + "  xhr.addEventListener('readystatechange', onreadystatechange_1, false);\n"
            + "  xhr.addEventListener('readystatechange', onreadystatechange_2, true);\n"
            + "  xhr.onreadystatechange = onreadystatechange_p;\n"
            + "  xhr.addEventListener('readystatechange', onreadystatechange_3, false);\n"
            + "  xhr.addEventListener('readystatechange', onreadystatechange_4, true);\n"
            + "  xhr.open('GET', 'foo.xml');\n"
            + "  xhr.send();\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>\n";

        getMockWebConnection().setDefaultResponse("");
        loadPageWithAlerts(html, URL_FIRST, 1000);
    }

    @Test
    @Alerts("onreadystatechange [object Event]§readystatechange§1§"
            + "onreadystatechange [object Event]§readystatechange§4§")
    public void sendLocalFileAllowed() throws Exception {
        // see org.htmlunit.javascript.host.xml.XMLHttpRequest5Test.sendLocalFile()
        final URL fileURL = getClass().getClassLoader().getResource("testfiles/localxmlhttprequest/index.html");
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        final WebClient client = getWebClient();
        client.getOptions().setFileProtocolForXMLHttpRequestsAllowed(true);

        final HtmlPage page = client.getPage(fileURL);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals(getExpectedAlerts()[0], page.getTitleText());
    }

    @Test
    @Alerts("onreadystatechange [object Event]§readystatechange§1§"
            + "onreadystatechange [object Event]§readystatechange§4§")
    public void sendLocalSubFileAllowed() throws Exception {
        // see org.htmlunit.javascript.host.xml.XMLHttpRequest5Test.sendLocalFile()
        final URL fileURL = getClass().getClassLoader().getResource("testfiles/localxmlhttprequest/index_sub.html");
        final File file = new File(fileURL.toURI());
        assertTrue("File '" + file.getAbsolutePath() + "' does not exist", file.exists());

        final WebClient client = getWebClient();
        client.getOptions().setFileProtocolForXMLHttpRequestsAllowed(true);

        final HtmlPage page = client.getPage(fileURL);

        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        assertEquals(getExpectedAlerts()[0], page.getTitleText());
    }
}
