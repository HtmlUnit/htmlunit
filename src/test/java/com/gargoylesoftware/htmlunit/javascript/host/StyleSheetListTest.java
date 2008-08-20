/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link StyleSheetList}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class StyleSheetListTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testLength() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <link href='style1.css'></link>\n"
            + "    <link href='style2.css' rel='stylesheet'></link>\n"
            + "    <link href='style3.css' type='text/css'></link>\n"
            + "    <link href='style4.css' rel='stylesheet' type='text/css'></link>\n"
            + "    <style>div.x { color: red; }</style>\n"
            + "  </head>\n"
            + "  <body onload='alert(document.styleSheets.length)'>\n"
            + "    <style>div.y { color: green; }</style>\n"
            + "  </body>\n"
            + "</html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(html, actual);
        final String[] expected = {"4"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testgetComputedStyle_Link() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='" + URL_SECOND + "'/>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var div = document.getElementById('myDiv');\n"
            + "        alert(window.getComputedStyle(div, null).color);\n"
            + "        var div2 = document.getElementById('myDiv2');\n"
            + "        alert(window.getComputedStyle(div2, null).color);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='myDiv'></div>\n"
            + "    <div id='myDiv2'></div>\n"
            + "  </body>\n"
            + "</html>";

        final String css = "div {color:red}";

        final String[] expectedAlerts = {"red", "red"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_2);
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, css, "text/css");
        webClient.setWebConnection(webConnection);

        webClient.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals(2, webConnection.getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testArrayIndexOutOfBoundAccess() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.styleSheets.length);\n"
            + "        alert(document.styleSheets[0]);\n"
            + "        alert(document.styleSheets[46]);\n"
            + "        try {\n"
            + "          alert(document.styleSheets[-2]);\n"
            + "        }\n"
            + "        catch (e) {\n"
            + "          alert('exception for -2');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String[] expectedAlerts = {"0", "undefined", "undefined", "exception for -2"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        assertEquals(expectedAlerts, actual);
    }
}
