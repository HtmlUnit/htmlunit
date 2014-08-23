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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLElement}.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Hans Donner
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ethan Glasser-Camp
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLElement3Test extends SimpleWebTestCase {

    /**
     * Test the <tt>#default#homePage</tt> default IE behavior.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "addBehavior not available", "http://localhost:12345/" },
            IE8 = { "isHomePage = false", "isHomePage = true", "isHomePage = true",
                    "isHomePage = false", "http://localhost:12345/second/" })
    public void addBehaviorDefaultHomePage() throws Exception {
        final URL url1 = URL_FIRST;
        final URL url2 = URL_SECOND;
        final String html1 =
            "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "       var body = document.body;\n"
            + "       if (!body.addBehavior) { alert('addBehavior not available'); return }\n"

            + "       // Test adding the behavior via script. Note that the URL\n"
            + "       // used to test must be part of the SAME domain as this\n"
            + "       // document, otherwise isHomePage() always returns false.\n"
            + "       body.addBehavior('#default#homePage');\n"
            + "       var url = '" + url1 + "';\n"
            + "       alert('isHomePage = ' + body.isHomePage(url));\n"
            + "       body.setHomePage(url);\n"
            + "       alert('isHomePage = ' + body.isHomePage(url));\n"
            + "       // Test behavior added via style attribute.\n"
            + "       // Also test case-insensitivity of default behavior names.\n"
            + "       alert('isHomePage = ' + hp.isHomePage(url));\n"
            + "       // Make sure that (as mentioned above) isHomePage() always\n"
            + "       // returns false when the url specified is the actual\n"
            + "       // homepage, but the document checking is on a DIFFERENT domain.\n"
            + "       hp.setHomePage('" + url2 + "');\n"
            + "       alert('isHomePage = ' + hp.isHomePage(url));\n"
            + "       // Test navigation to homepage.\n"
            + "       body.navigateHomePage();\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <span id='hp' style='behavior:url(#default#homepage)'></span>\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = "<html></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(url1, html1);
        webConnection.setResponse(url2, html2);

        final String[] alerts = getExpectedAlerts();
        setExpectedAlerts(ArrayUtils.subarray(alerts, 0, alerts.length - 1));
        final HtmlPage page = loadPageWithAlerts(html1, url1, 1000);

        assertEquals(alerts[alerts.length - 1], page.getUrl().toExternalForm());
    }

    /**
     * Test the <tt>#default#download</tt> default IE behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "startDownload not available",
            IE = { "Refused", "foo" })
    public void addBehaviorDefaultDownload() throws Exception {
        final URL url1 = new URL("http://htmlunit.sourceforge.net/");
        final URL url2 = new URL("http://htmlunit.sourceforge.net/test.txt");
        // The download behavior doesn't accept downloads from a different domain ...
        final URL url3 = new URL("http://www.domain2.com/test.txt");

        final String html1 =
            "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "      if (!hp.startDownload) { alert('startDownload not available'); return }\n"

            + "      try {\n"
            + "        hp.startDownload('http://www.domain2.com/test.txt', callback);\n"
            + "      }\n"
            + "      catch (e)\n"
            + "      {\n"
            + "        alert('Refused');\n"
            + "      }\n"
            + "      hp.startDownload('test.txt', callback);\n"
            + "    }\n"
            + "    function callback(content) {\n"
            + "      alert(content);\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <span id='hp' style='behavior:url(#default#download)'></span>\n"
            + "  </body>\n"
            + "</html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(url1, html1);
        webConnection.setResponse(url2, "foo");
        webConnection.setResponse(url3, "foo2");
        loadPageWithAlerts(html1, url1, 1000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnclick() throws Exception {
        eventHandlerSetterGetterTest("onclick");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOndblclick() throws Exception {
        eventHandlerSetterGetterTest("ondblclick");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnblur() throws Exception {
        eventHandlerSetterGetterTest("onblur");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnfocus() throws Exception {
        eventHandlerSetterGetterTest("onfocus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnkeydown() throws Exception {
        eventHandlerSetterGetterTest("onkeydown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnkeypress() throws Exception {
        eventHandlerSetterGetterTest("onkeypress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnkeyup() throws Exception {
        eventHandlerSetterGetterTest("onkeyup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmousedown() throws Exception {
        eventHandlerSetterGetterTest("onmousedown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmouseup() throws Exception {
        eventHandlerSetterGetterTest("onmouseup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmouseover() throws Exception {
        eventHandlerSetterGetterTest("onmouseover");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmouseout() throws Exception {
        eventHandlerSetterGetterTest("onmouseout");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnmousemove() throws Exception {
        eventHandlerSetterGetterTest("onmousemove");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnresize() throws Exception {
        eventHandlerSetterGetterTest("onresize");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setOnerror() throws Exception {
        eventHandlerSetterGetterTest("onerror");
    }

    /**
     * @param eventName the name of the event
     * @throws Exception if the test fails
     */
    private void eventHandlerSetterGetterTest(final String eventName) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function handler(event) {}\n"
            + "function test() {\n"
            + "    var oDiv = document.getElementById('myDiv');\n"
            + "    oDiv." + eventName + " = handler;\n"
            + "    if (oDiv." + eventName + " == handler) {\n"
            + "        alert('success');\n"
            + "    } else {\n"
            + "        alert('fail');\n"
            + "    }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'><br/><div><span>test</span></div></div>\n"
            + "</body>\n"
            + "</html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"success"};
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlElement div = page.getHtmlElementById("myDiv");

        assertTrue("Event handler was not set", div.hasEventHandlers(eventName));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickHashAnchor() throws Exception {
        final String html
            = "<html><head><title>HashAnchor</title></head>\n"
            + "<body>\n"
            + "  <script language='javascript'>\n"
            + "    function test() {alert('test hash');}\n"
            + "  </script>\n"
            + "  <a onClick='javascript:test();' href='#' name='hash'>Click</a>\n"
            + "</body>\n"
            + "</html>";
        final String[] expectedAlerts = {"test hash"};
        // first use direct load
        final List<String> loadCollectedAlerts = new ArrayList<String>();
        final HtmlPage loadPage = loadPage(getBrowserVersion(), html, loadCollectedAlerts);
        final HtmlAnchor loadHashAnchor = loadPage.getAnchorByName("hash");
        loadHashAnchor.click();

        assertEquals(expectedAlerts, loadCollectedAlerts);

        // finally try via the client
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);
        final CollectingAlertHandler clientCollectedAlertsHandler = new CollectingAlertHandler();
        webClient.setAlertHandler(clientCollectedAlertsHandler);
        final HtmlPage clientPage = webClient.getPage(URL_FIRST);
        final HtmlAnchor clientHashAnchor = clientPage.getAnchorByName("hash");
        clientHashAnchor.click();

        assertEquals(expectedAlerts, clientCollectedAlertsHandler.getCollectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "clicked", "fireEvent not available" },
            IE8 = { "clicked", "clicked" })
    public void fireEvent_WithoutTemplate() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "      var elem = document.getElementById('a');\n"
            + "      if (!elem.fireEvent) { alert('fireEvent not available'); return }\n"
            + "      elem.fireEvent('onclick');\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "<body>\n"
            + "  <div id='a' onclick='alert(\"clicked\")'>foo</div>\n"
            + "  <div id='b' onmouseover='doTest()'>bar</div>\n"
            + "</body></html>";

        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, actual);
        page.getHtmlElementById("a").click();
        page.getHtmlElementById("b").mouseOver();
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "click", "fireEvent not available", "fireEvent not available" },
            IE8 = { "click", "click", "click" })
    public void fireEvent_WithTemplate() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doAlert(e) {\n"
            + "      alert(e.type);\n"
            + "    }\n"
            + "    function doTest() {\n"
            + "      var elem = document.getElementById('a');\n"
            + "      if (!elem.fireEvent) { alert('fireEvent not available'); return }\n"
            + "      elem.fireEvent('onclick');\n"
            + "    }\n"
            + "    function doTest2() {\n"
            + "      var elem = document.getElementById('a');\n"
            + "      if (!elem.fireEvent) { alert('fireEvent not available'); return }\n"
            + "      var template = document.createEventObject();\n"
            + "      elem.fireEvent('onclick', template);\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "<body>\n"
            + "<div id='a' onclick='doAlert(event)'>foo</div>\n"
            + "<div id='b' onmouseover='doTest()'>bar</div>\n"
            + "<div id='c' onmouseover='doTest2()'>baz</div>\n"
            + "</body></html>";

        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, actual);
        page.getHtmlElementById("a").click();
        page.getHtmlElementById("b").mouseOver();
        page.getHtmlElementById("c").mouseOver();
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "body1", "body1", "body1", "setActive not available" },
            IE = {"body1", "button1", "text1", "[object]", "onfocus text2", "text2", "onfocus text1", "onfocus text2" })
    public void setActiveAndFocus() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html>\n"
            + "<head>\n"
            + "  <title>First</title>\n"
            + "  <script>var win2;</script>\n"
            + "</head>\n"
            + "<body id='body1' onload='alert(document.activeElement.id)'><form name='form1'>\n"
            + "  <input id='text1' onfocus='alert(\"onfocus text1\"); win2.focus();'>\n"
            + "  <button id='button1' onClick='win2=window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html>\n"
            + "<head>\n"
            + "  <title>Second</title>\n"
            + "</head>\n"
            + "<body id='body2'>\n"
            + "  <input id='text2' onfocus='alert(\"onfocus text2\")'>\n"
            + "  <button id='button2' onClick='doTest();'>Click me</a>\n"
            + "  <script>\n"
            + "     function doTest() {\n"
            + "         var elem = opener.document.getElementById('text1');\n"
            + "         alert(opener.document.activeElement.id);\n"
            + "         if (!elem.setActive) { alert('setActive not available'); return }\n"
            + "         elem.setActive();\n"
            + "         alert(opener.document.activeElement.id);\n"
            + "         alert(document.activeElement);\n"
            + "         document.getElementById('text2').setActive();\n"
            + "         alert(document.activeElement.id);\n"
            + "         opener.focus();\n"
            + "    }\n"
            + "  </script>\n"
            + "</body></html>";
        webConnection.setResponse(URL_SECOND, secondHtml);

        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlButton button1 = firstPage.getHtmlElementById("button1");
        final HtmlPage secondPage = button1.click();
        assertEquals("Second", secondPage.getTitleText());
        secondPage.getHtmlElementById("button2").click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onfocus text1", "onfocus text2", "onfocus text1", "onfocus text2" })
    public void onFocusOnWindowFocusGain() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html><head><title>First</title></head>\n"
            + "<body><form name='form1'>\n"
            + "<input id='text1' onfocus='alert(\"onfocus text1\")'>\n"
            + "<button type='button' id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html><head><title>Second</title></head>\n"
            + "<body onLoad='doTest()'>\n"
            + "<input id='text2' onfocus='alert(\"onfocus text2\")'>\n"
            + "<script>\n"
            + "     function doTest() {\n"
            + "         opener.document.getElementById('text1').focus();\n"
            + "         document.getElementById('text2').focus();\n"
            + "    }\n"
            + "</script></body></html>";

        webConnection.setResponse(URL_SECOND, secondHtml);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        final HtmlPage secondPage = buttonA.click();
        assertEquals("Second", secondPage.getTitleText());
        webClient.setCurrentWindow(firstPage.getEnclosingWindow());
        webClient.setCurrentWindow(secondPage.getEnclosingWindow());
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onblur text2", "onblur text1" })
    public void onBlurOnWindowFocusChange() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html><head><title>First</title></head>\n"
            + "<body><form name='form1'>\n"
            + "<input id='text1' onblur='alert(\"onblur text1\")'>\n"
            + "<button type='button' id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html><head><title>Second</title></head>\n"
            + "<body onLoad='doTest()'>\n"
            + "<input id='text2' onblur='alert(\"onblur text2\")'>\n"
            + "<script>\n"
            + "     function doTest() {\n"
            + "         opener.document.getElementById('text1').focus();\n"
            + "         document.getElementById('text2').focus();\n"
            + "    }\n"
            + "</script></body></html>";

        webConnection.setResponse(URL_SECOND, secondHtml);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        final HtmlPage secondPage = buttonA.click();
        assertEquals("Second", secondPage.getTitleText());
        webClient.setCurrentWindow(firstPage.getEnclosingWindow());
        webClient.setCurrentWindow(secondPage.getEnclosingWindow());
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Document.write after setting innerHTML.
     * @throws Exception if the test fails
     */
    @Test
    public void setInnerHTMLDocumentWrite() throws Exception {
        final String html = "<html><head><title>test</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "     var a = document.createElement('a');\n"
            + "     a.innerHTML = 'break';\n"
            + "     document.write('hello');\n"
            + "</script></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("test" + LINE_SEPARATOR + "hello", page.asText());
    }
}
