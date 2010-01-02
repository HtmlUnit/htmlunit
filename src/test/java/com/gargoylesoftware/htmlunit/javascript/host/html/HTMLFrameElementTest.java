/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLFrameElement} when used for {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Thomas Robbs
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLFrameElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Frame2")
    public void testFrameName() throws Exception {
        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame id='frame1'>\n"
            + "    <frame name='Frame2' onload='alert(this.name)' id='frame2'>\n"
            + "</frameset></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for http://sourceforge.net/tracker/index.php?func=detail&aid=1101525&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void testLocation() throws Exception {
        testLocation("Frame1.location = \"frame.html\"");
        testLocation("Frame1.location.replace(\"frame.html\")");
    }

    private void testLocation(final String jsExpr) throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='*' onload='" + jsExpr + "'>\n"
            + "    <frame name='Frame1' src='subdir/frame.html'>\n"
            + "</frameset></html>";
        final String defaultContent
            = "<html><head><script>alert(location)</script></head></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(defaultContent);

        final String[] expectedAlerts = {URL_FIRST + "subdir/frame.html", URL_FIRST + "frame.html"};

        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts("true")
    public void testContentDocument() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  alert(document.getElementById('myFrame').contentDocument == frames.foo.document);\n"
                + "}\n"
                + "</script></head>\n"
                + "<frameset rows='*' onload='test()'>\n"
                + "<frame name='foo' id='myFrame' src='about:blank'/>\n"
                + "</frameset>\n"
                + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Alerts("true")
    @Test
    public void testContentWindow() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  alert(document.getElementById('myFrame').contentWindow == frames.foo);\n"
                + "}\n"
                + "</script></head>\n"
                + "<frameset rows='*' onload='test()'>\n"
                + "<frame name='foo' id='myFrame' src='about:blank'/>\n"
                + "</frameset>\n"
                + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 1236048.
     * See http://sourceforge.net/tracker/index.php?func=detail&aid=1236048&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Alerts("2")
    @Test
    public void testWriteFrameset() throws Exception {
        final String content1 = "<html><head>\n"
            + "<script>\n"
            + "    document.write('<frameset>');\n"
            + "    document.write('<frame src=\"page2.html\" name=\"leftFrame\">');\n"
            + "    document.write('</frameset>');\n"
            + "</script>\n"
            + "</head></html>";
        final String content2 = "<html><head><script>alert(2)</script></head></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webClient.setWebConnection(webConnection);

        webConnection.setDefaultResponse(content2);
        webConnection.setResponse(URL_FIRST, content1);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Regression test for bug 1289060.
     * See http://sourceforge.net/tracker/index.php?func=detail&aid=1289060&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Alerts("DIV")
    @Test
    public void testFrameLoadedAfterParent() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<iframe name='testFrame' src='testFrame.html'></iframe>\n"
            + "<div id='aButton'>test text</div>\n"
            + "</body></html>";
        final String frameContent
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "alert(top.document.getElementById('aButton').tagName);\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl() + "testFrame.html"), frameContent);
        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 1192854.
     * @throws Exception if the test fails
     */
    @Alerts({ "frame=OK", "frames.length=2", "frame=OK", "frames.length=0", "frame=OK", "frames.length=0" })
    @Test
    public void testFrameTag1192854() throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "var root=this;\n"
            + "function listframes(frame) {\n"
            + "  if (frame == null) {\n"
            + "    alert('frame=null');\n"
            + "  } else {\n"
            + "    alert('frame=OK');\n"
            + "    var len = frame.frames.length;\n"
            + "    alert('frames.length=' + len);\n"
            + "    for (var i=0; i<len; i++) {\n"
            + "      listframes(frame.frames[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "document.write('<frameset id=\"frameset1\" "
            + "rows=\"50,50\"><frame id=\"frame1-1\" "
            + "src=\"about:blank\"><frame id=\"frame1-2\" "
            + "src=\"about:blank\"></frameset>');\n"
            + "listframes(root);\n"
            + "</script>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "\nfunction handler() {\n}\n", "null" })
    public void testOnloadNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function handler() {}\n"
            + "  function test() {\n"
            + "    var iframe = document.getElementById('myFrame');\n"
            + "    iframe.onload = handler;\n"
            + "    alert(iframe.onload);\n"
            + "    iframe.onload = null;\n"
            + "    alert(iframe.onload);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Frame2")
    public void serialization() throws Exception {
        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame id='frame1'>\n"
            + "    <frame name='Frame2' onload='alert(this.name)' id='frame2'>\n"
            + "</frameset></html>";

        final HtmlPage page = loadPageWithAlerts(html);

        final ObjectOutputStream objectOS = new ObjectOutputStream(new ByteArrayOutputStream());
        objectOS.writeObject(page);
    }

    /**
     * Illustrates problem of issue #2314485.
     * See https://sourceforge.net/tracker/?func=detail&atid=448266&aid=2314485&group_id=47038
     * @throws Exception if the test fails
     */
    @Alerts({ "about:blank", "oFrame.foo: undefined", "/frame1.html", "oFrame.foo: foo of frame 1",
        "/frame2.html", "oFrame.foo: foo of frame 2" })
    @Test
    public void changingFrameDocumentLocation() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html><head><script>\n"
            + "var oFrame;\n"
            + "function init() {\n"
            + "  oFrame = self.frames['theFrame'];\n"
            + "}\n"
            + "function test(fileName) {\n"
            + "  if (oFrame.document.location == 'about:blank')\n" // to avoid different expectations for IE and FF
            + "    alert('about:blank');\n"
            + "  else\n"
            + "    alert(oFrame.document.location.pathname);\n"
            + "  alert('oFrame.foo: ' + oFrame.foo);\n"
            + "  oFrame.document.location.href = fileName;\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='init()'>\n"
            + "<iframe name='theFrame'></iframe>\n"
            + "<button id='btn1' onclick='test(\"frame1.html\")'>load frame1</button>\n"
            + "<button id='btn2' onclick='test(\"frame2.html\")'>load frame2</button>\n"
            + "<button id='btn3' onclick='test(\"about:blank\")'>load about:blank</button>\n"
            + "</body></html>";

        final String frame1Html = "<html><head><title>frame 1</title>\n"
            + "<script>var foo = 'foo of frame 1'</script></head>\n"
            + "<body>frame 1</body></html>";
        final String frame2Html = frame1Html.replaceAll("frame 1", "frame 2");

        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(new URL(URL_FIRST, "frame1.html"), frame1Html);
        webConnection.setResponse(new URL(URL_FIRST, "frame2.html"), frame2Html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        page.<HtmlElement>getHtmlElementById("btn1").click();
        page.<HtmlElement>getHtmlElementById("btn2").click();
        page.<HtmlElement>getHtmlElementById("btn3").click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.FF)
    @Alerts(IE = "[object]", FF = "undefined")
    public void frames() throws Exception {
        final String mainHtml =
            "<html><head><title>frames</title></head>\n"
            + "<frameset>\n"
            + "<frame id='f1' src='1.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body onload=\"alert(parent.frames['f1'])\"></body>\n"
            + "</html>";

        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        webClient.setWebConnection(conn);

        conn.setResponse(URL_FIRST, mainHtml);
        conn.setResponse(new URL(URL_FIRST, "1.html"), frame1);

        webClient.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

}
