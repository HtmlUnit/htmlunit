/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for the {@link PostponedAction}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class PostponedActionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void loadingJavaScript() throws Exception {
        final String firstContent = "<html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    document.getElementById('debugDiv').innerHTML += 'before, ';\n"
            + "    var iframe2 = document.createElement('iframe');\n"
            + "    iframe2.src = '" + URL_SECOND + "';\n"
            + "    document.body.appendChild(iframe2);\n"
            + "    var iframe3 = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe3);\n"
            + "    iframe3.src = '" + URL_THIRD + "';\n"
            + "    document.getElementById('debugDiv').innerHTML += 'after, ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='debugDiv'></div>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent
            = "<script>parent.document.getElementById('debugDiv').innerHTML += 'second.html, ';</script>";
        final String thirdContent
            = "<script>parent.document.getElementById('debugDiv').innerHTML += 'third.html, ';</script>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        conn.setResponse(URL_THIRD, thirdContent);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlDivision div = page.getHtmlElementById("debugDiv");
        assertEquals("before, after, second.html, third.html, ", div.getFirstChild().getNodeValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void loadingJavaScript2() throws Exception {
        final String firstContent = "<html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    document.getElementById('debugDiv').innerHTML += 'before, ';\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe);\n"
            + "    iframe.contentWindow.location.replace('" + URL_SECOND + "');\n"
            + "    document.getElementById('debugDiv').innerHTML += 'after, ';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='debugDiv'></div>\n"
            + "</body>\n"
            + "</html>";
        final String secondContent
            = "<script>parent.document.getElementById('debugDiv').innerHTML += 'second.html, ';</script>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlDivision div = page.getHtmlElementById("debugDiv");
        assertEquals("before, after, second.html, ", div.getFirstChild().getNodeValue());
    }
}
