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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLIFrameElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void style() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.getElementById('myIFrame').style == undefined);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe id='myIFrame' src='about:blank'></iframe></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "myIFrame" })
    public void referenceFromJavaScript() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(window.frames.length);\n"
            + "    alert(window.frames['myIFrame'].name);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe name='myIFrame' src='about:blank'></iframe></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 1562872.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "about:blank", "about:blank" })
    public void directAccessPerName() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(myIFrame.location);\n"
            + "    alert(Frame.location);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe name='myIFrame' src='about:blank'></iframe>\n"
            + "<iframe name='Frame' src='about:blank'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Tests that the <tt>&lt;iframe&gt;</tt> node is visible from the contained page when it is loaded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IFRAME")
    public void onLoadGetsIFrameElementByIdInParent() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<iframe id='myIFrame' src='frame.html'></iframe></body></html>";

        final String frameContent
            = "<html><head><title>Frame</title><script>\n"
            + "function doTest() {\n"
            + "    alert(parent.document.getElementById('myIFrame').tagName);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse(frameContent);
        webConnection.setResponse(URL_FIRST, firstContent);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "loaded", "loaded", "loaded" })
    public void onLoadCalledEachTimeFrameContentChanges() throws Exception {
        final String html =
              "<html>\n"
            + "  <body>\n"
            + "    <iframe id='i' onload='alert(\"loaded\");'></iframe>\n"
            + "    <div id='d1' onclick='i.contentWindow.location.replace(\"blah.html\")'>1</div>\n"
            + "    <div id='d2' onclick='i.contentWindow.location.href=\"blah.html\"'>2</div>\n"
            + "    <script>var i = document.getElementById(\"i\")</script>\n"
            + "  </body>\n"
            + "</html>";

        final String frameHtml = "<html><body>foo</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse(frameHtml);
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = webClient.getPage(URL_FIRST);
        page.<HtmlDivision>getHtmlElementById("d1").click();
        page.<HtmlDivision>getHtmlElementById("d2").click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = "true")
    public void contentDocument() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  alert(document.getElementById('myFrame').contentDocument == frames.foo.document);\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "<iframe name='foo' id='myFrame' src='about:blank'></iframe>\n"
                + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void frameElement() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  alert(document.getElementById('myFrame') == frames.foo.frameElement);\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "<iframe name='foo' id='myFrame' src='about:blank'></iframe>\n"
                + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Verifies that writing to an iframe keeps the same intrinsic variables around (window,
     * document, etc) and in a usable form. Bug detected via the jQuery 1.1.3.1 unit tests.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "false", "true", "true", "true", "object", "object" })
    public void writeToIFrame() throws Exception {
        final String html =
              "<html><body onload='test()'><script>\n"
            + "    function test() {\n"
            + "        \n"
            + "        var frame = document.createElement('iframe');\n"
            + "        document.body.appendChild(frame);\n"
            + "        var win = frame.contentWindow;\n"
            + "        var doc = frame.contentWindow.document;\n"
            + "        alert(win == window);\n"
            + "        alert(doc == document);\n"
            + "        \n"
            + "        doc.open();\n"
            + "        doc.write(\"<html><body><input type='text'/></body></html>\");\n"
            + "        doc.close();\n"
            + "        var win2 = frame.contentWindow;\n"
            + "        var doc2 = frame.contentWindow.document;\n"
            + "        alert(win == win2);\n"
            + "        alert(doc == doc2);\n"
            + "        \n"
            + "        var input = doc.getElementsByTagName('input')[0];\n"
            + "        var input2 = doc2.getElementsByTagName('input')[0];\n"
            + "        alert(input == input2);\n"
            + "        alert(typeof input);\n"
            + "        alert(typeof input2);\n"
            + "    }\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Verifies that writing to an iframe keeps the same intrinsic variables around (window,
     * document, etc) and in a usable form. Bug detected via the jQuery 1.1.3.1 unit tests.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "123", "undefined" })
    public void iFrameReinitialized() throws Exception {
        final String html =
              "<html><body><a href='2.html' target='theFrame'>page 2 in frame</a>\n"
            + "<iframe name='theFrame' src='1.html'></iframe>\n"
            + "</body></html>";

        final String frame1 = "<html><head><script>window.foo = 123; alert(window.foo);</script></head></html>";
        final String frame2 = "<html><head><script>alert(window.foo);</script></head></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(new URL(URL_FIRST, "1.html"), frame1);
        webConnection.setResponse(new URL(URL_FIRST, "2.html"), frame2);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = webClient.getPage(URL_FIRST);
        page.getAnchors().get(0).click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setSrcAttribute() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "   document.getElementById('iframe1').setAttribute('src', '" + URL_SECOND + "');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe id='iframe1'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        assertEquals(URL_SECOND.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Second", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());

        iframe.setSrcAttribute(URL_THIRD.toExternalForm());
        assertEquals(URL_THIRD.toExternalForm(), iframe.getSrcAttribute());
        assertEquals("Third", ((HtmlPage) iframe.getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void setSrc_JavascriptUrl() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "   document.getElementById('iframe1').src = 'javascript:void(0)';\n"
            + "   alert(window.frames[0].location);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<iframe id='iframe1'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Verify that an iframe.src with a "javascript:..." URL loaded by a client with JavaScript
     * disabled does not cause a NullPointerException (reported on the mailing list).
     * @throws Exception if an error occurs
     */
    @Test
    public void srcJavaScriptUrl_JavaScriptDisabled() throws Exception {
        final String html = "<html><body><iframe src='javascript:false;'></iframe></body></html>";

        final WebClient client = getWebClient();
        client.setJavaScriptEnabled(false);

        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"", "hello", "left", "hi", "right" },
            IE = {"", "error", "", "left", "error", "left", "right" })
    public void align() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var iframe = document.getElementById('f');\n"
            + "        alert(iframe.align);\n"
            + "        set(iframe, 'hello');\n"
            + "        alert(iframe.align);\n"
            + "        set(iframe, 'left');\n"
            + "        alert(iframe.align);\n"
            + "        set(iframe, 'hi');\n"
            + "        alert(iframe.align);\n"
            + "        set(iframe, 'right');\n"
            + "        alert(iframe.align);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.align = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><iframe id='f'></iframe></body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"", "100", "foo", "20%", "0", "30", "400", "abc", "0", "100", "10%", "300" },
            IE = {"", "100", "", "20%", "-5", "30", "error", "400", "100", "-5", "100", "10%", "300" })
    public void width() throws Exception {
        final String html
            = "<html><body>\n"
            + "<iframe id='i1'></iframe>\n"
            + "<iframe id='i2' width='100'></iframe>\n"
            + "<iframe id='i3' width='foo'></iframe>\n"
            + "<iframe id='i4' width='20%'></iframe>\n"
            + "<iframe id='i5' width='-5'></iframe>\n"
            + "<iframe id='i6' width='30.2'></iframe>\n"
            + "<script>\n"
            + "function set(e, value) {\n"
            + "  try {\n"
            + "    e.width = value;\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "}\n"
            + "var i1 = document.getElementById('i1');\n"
            + "var i2 = document.getElementById('i2');\n"
            + "var i3 = document.getElementById('i3');\n"
            + "var i4 = document.getElementById('i4');\n"
            + "var i5 = document.getElementById('i5');\n"
            + "var i6 = document.getElementById('i6');\n"
            + "alert(i1.width);\n"
            + "alert(i2.width);\n"
            + "alert(i3.width);\n"
            + "alert(i4.width);\n"
            + "alert(i5.width);\n"
            + "alert(i6.width);\n"
            + "set(i1, '400');\n"
            + "set(i2, 'abc');\n"
            + "set(i3, -5);\n"
            + "set(i4, 100.2);\n"
            + "set(i5, '10%');\n"
            + "set(i6, 300);\n"
            + "alert(i1.width);\n"
            + "alert(i2.width);\n"
            + "alert(i3.width);\n"
            + "alert(i4.width);\n"
            + "alert(i5.width);\n"
            + "alert(i6.width);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"", "100", "foo", "20%", "0", "30", "400", "abc", "0", "100", "10%", "300" },
            IE = {"", "100", "", "20%", "-5", "30", "error", "400", "100", "-5", "100", "10%", "300" })
    public void height() throws Exception {
        final String html
            = "<html><body>\n"
            + "<iframe id='i1'></iframe>\n"
            + "<iframe id='i2' height='100'></iframe>\n"
            + "<iframe id='i3' height='foo'></iframe>\n"
            + "<iframe id='i4' height='20%'></iframe>\n"
            + "<iframe id='i5' height='-5'></iframe>\n"
            + "<iframe id='i6' height='30.2'></iframe>\n"
            + "<script>\n"
            + "function set(e, value) {\n"
            + "  try {\n"
            + "    e.height = value;\n"
            + "  } catch (e) {\n"
            + "    alert('error');\n"
            + "  }\n"
            + "}\n"
            + "var i1 = document.getElementById('i1');\n"
            + "var i2 = document.getElementById('i2');\n"
            + "var i3 = document.getElementById('i3');\n"
            + "var i4 = document.getElementById('i4');\n"
            + "var i5 = document.getElementById('i5');\n"
            + "var i6 = document.getElementById('i6');\n"
            + "alert(i1.height);\n"
            + "alert(i2.height);\n"
            + "alert(i3.height);\n"
            + "alert(i4.height);\n"
            + "alert(i5.height);\n"
            + "alert(i6.height);\n"
            + "set(i1, '400');\n"
            + "set(i2, 'abc');\n"
            + "set(i3, -5);\n"
            + "set(i4, 100.2);\n"
            + "set(i5, '10%');\n"
            + "set(i6, 300);\n"
            + "alert(i1.height);\n"
            + "alert(i2.height);\n"
            + "alert(i3.height);\n"
            + "alert(i4.height);\n"
            + "alert(i5.height);\n"
            + "alert(i6.height);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Test the ReadyState which is an IE feature.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "loading", "complete" },
            IE = { "loading", "complete" })
    public void readyState_IFrame() throws Exception {
        final String html = "<html><head></head>\n"
            + "  <body>\n"
            + "    <iframe id='i'></iframe>\n"
            + "    <script>\n"
            + "      alert(document.getElementById('i').contentWindow.document.readyState);\n"
            + "      window.onload = function() {\n"
            + "        alert(document.getElementById('i').contentWindow.document.readyState);\n"
            + "      };\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "null", "[object HTMLBodyElement]" },
            IE = { "null", "[object]" })
    public void body() throws Exception {
        final String html =
              "<html><body>\n"
            + "<iframe name='theFrame' src='1.html'></iframe>\n"
            + "</body></html>";

        final String frame = "<html><head><script>alert(document.body);</script></head>\n"
            + "<body><script>alert(document.body);</script></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setDefaultResponse(frame);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("128")
    public void width_px() throws Exception {
        final String html
            = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var iframe = document.getElementById('myFrame');\n"
            + "    iframe.width = '128px';\n"
            + "    alert(iframe.width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * IE: getElementById() returns a different object than with direct 'id' variable.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "[object]", "[object]", "undefined", "" },
            FF = { "[object HTMLIFrameElement]", "[object HTMLIFrameElement]", "", "" })
    @NotYetImplemented(Browser.IE)
    public void idByName() throws Exception {
        final String html
            = "<html><head>"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(myFrame);\n"
            + "    alert(document.getElementById('myFrame'));\n"
            + "    alert(myFrame.width);\n"
            + "    alert(document.getElementById('myFrame').width);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 2940926.
     * @throws Exception if an error occurs
     */
    @Test
    public void settingInnerHtmlTriggersFrameLoad() throws Exception {
        final String html
            = "<html><body><div id='d' onclick='loadFrame()'>Click me to show frame</div><script>\n"
            + "function loadFrame() {\n"
            + "  var s = '<iframe id=\"i\" src=\"frame.html\">';\n"
            + "  s += '<p>Your browser does not support frames</p>';\n"
            + "  s += '</iframe>';\n"
            + "  var d = document.getElementById('d');\n"
            + "  d.innerHTML = s;\n"
            + "}\n"
            + "</script></body></html>";
        final String html2 = "<html><body>foo</body></html>";

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(new URL(URL_FIRST, "frame.html"), html2);

        final WebClient client = getWebClient();
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        page.getElementById("d").click();

        final HtmlInlineFrame frame = (HtmlInlineFrame) page.getElementById("i");
        final HtmlPage framePage = (HtmlPage) frame.getEnclosedPage();
        assertEquals("foo", framePage.getBody().asText());
    }
}
