/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Node}.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Bruce Faulkner
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class Node2Test extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testReplaceChild_WithSameNode() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    var a = document.getElementById('a');\n"
            + "    var b = document.getElementById('b');\n"
            + "    a.replaceChild(b, b);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'><div id='a'><div id='b'/></div></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        assertNotNull(page.getHtmlElementById("b").getParentNode());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = { "[object]", "[object]" })
    public void event() throws Exception {
        final String firstHtml = "<html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe);\n"
            + "    iframe.contentWindow.location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "    <input type='button' id='myInput' value='Test me'>\n"
            + "    <div id='myDiv'></div>\n"
            + "</body>\n"
            + "</html>";
        final String secondHtml =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      var handler = function() {\n"
            + "        alert(parent.event);\n"
            + "        parent.document.getElementById('myDiv').style.display = 'none';\n"
            + "        alert(parent.event);\n"
            + "      }\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          parent.document.body.attachEvent('onclick', handler);\n"
            + "        } catch(e) { alert('exception') };\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, firstHtml);
        conn.setResponse(URL_SECOND, secondHtml);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        page.getHtmlElementById("myInput").click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Verifies that attributes belonging to cloned nodes are available via JavaScript.
     * http://sourceforge.net/tracker/index.php?func=detail&aid=2024741&group_id=47038&atid=448266
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id=bar")
    public void testCloneAttributesAvailable() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function go() {\n"
            + "        var node = document.getElementById('foo');\n"
            + "        var clone = node.cloneNode(true);\n"
            + "        clone.id = 'bar';\n"
            + "        node.appendChild(clone);\n"
            + "        alert(clone.attributes[0].nodeName + '=' + clone.attributes[0].nodeValue);\n"
            + "    }\n"
            + "  </script>\n"
            + "  </head>\n"
            + "  <body onload='go()'>\n"
            + "    <div id='foo'></div>\n"
            + "  </body>\n"
            + "</html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlElement element = page.getHtmlElementById("bar");
        final String value = element.getAttribute("id");
        assertEquals("bar", value);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void eventListener() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function clicking1() {\n"
            + "    alert(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    alert(2);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    if (e.addEventListener) {\n"
            + "      e.addEventListener('click', clicking1, false);\n"
            + "      e.addEventListener('click', clicking2, false);\n"
            + "    } else if (e.attachEvent) {\n"
            + "      e.attachEvent('onclick', clicking1);\n"
            + "      e.attachEvent('onclick', clicking2);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a href='" + URL_SECOND + "' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = page.getHtmlElementById("myAnchor").click();
        //IE doesn't have specific order
        Collections.sort(collectedAlerts);
        assertEquals(getExpectedAlerts(), collectedAlerts);
        assertEquals(URL_SECOND.toExternalForm(),
                page2.getWebResponse().getWebRequest().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void eventListener_return_false() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function clicking1() {\n"
            + "    alert(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    alert(2);\n"
            + "    return false;\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    if (e.addEventListener) {\n"
            + "      e.addEventListener('click', clicking1, false);\n"
            + "      e.addEventListener('click', clicking2, false);\n"
            + "    } else if (e.attachEvent) {\n"
            + "      e.attachEvent('onclick', clicking1);\n"
            + "      e.attachEvent('onclick', clicking2);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a href='" + URL_SECOND + "' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = page.getHtmlElementById("myAnchor").click();
        //IE doesn't have specific order
        Collections.sort(collectedAlerts);
        assertEquals(getExpectedAlerts(), collectedAlerts);

        final URL expectedURL;
        if (getBrowserVersion().isIE()) {
            expectedURL = getDefaultUrl();
        }
        else {
            expectedURL = URL_SECOND;
        }
        assertEquals(expectedURL.toExternalForm(),
                page2.getWebResponse().getWebRequest().getUrl().toExternalForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void eventListener_returnValue_false() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function clicking1() {\n"
            + "    alert(1);\n"
            + "  }\n"
            + "  function clicking2() {\n"
            + "    alert(2);\n"
            + "    if (window.event)\n"
            + "      window.event.returnValue = false;\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myAnchor');\n"
            + "    if (e.addEventListener) {\n"
            + "      e.addEventListener('click', clicking1, false);\n"
            + "      e.addEventListener('click', clicking2, false);\n"
            + "    } else if (e.attachEvent) {\n"
            + "      e.attachEvent('onclick', clicking1);\n"
            + "      e.attachEvent('onclick', clicking2);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a href='" + URL_SECOND + "' id='myAnchor'>Click me</a>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = page.getHtmlElementById("myAnchor").click();
        //IE doesn't have specific order
        Collections.sort(collectedAlerts);
        assertEquals(getExpectedAlerts(), collectedAlerts);

        final URL expectedURL;
        if (getBrowserVersion().isIE()) {
            expectedURL = getDefaultUrl();
        }
        else {
            expectedURL = URL_SECOND;
        }
        assertEquals(expectedURL.toExternalForm(),
                page2.getWebResponse().getWebRequest().getUrl().toExternalForm());
    }

}
