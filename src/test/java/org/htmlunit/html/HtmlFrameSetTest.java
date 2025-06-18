/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlunit.CollectingAlertHandler;
import org.htmlunit.HttpHeader;
import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlFrameSet}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Hans Donner
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlFrameSetTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void loadingFrameSet() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + URL_SECOND + "' frameborder='1' />\n"
            + "  <frame scrolling='auto' name='right' src='" + URL_THIRD + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent  = DOCTYPE_HTML + "<html><head><title>Third</title></head><body></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final WebWindow secondWebWindow = webClient.getWebWindowByName("left");
        assertSame(firstPage, ((FrameWindow) secondWebWindow).getEnclosingPage());
        assertEquals("Second", ((HtmlPage) secondWebWindow.getEnclosedPage()).getTitleText());

        final WebWindow thirdWebWindow = webClient.getWebWindowByName("right");
        assertTrue(FrameWindow.class.isInstance(thirdWebWindow));
        assertSame(firstPage, ((FrameWindow) thirdWebWindow).getEnclosingPage());
        assertEquals("Third", ((HtmlPage) thirdWebWindow.getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void loadingIFrames() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "  <iframe name='left' src='" + URL_SECOND + "' />\n"
            + "  some stuff"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final WebWindow secondWebWindow = webClient.getWebWindowByName("left");
        assertTrue(FrameWindow.class.isInstance(secondWebWindow));
        assertSame(firstPage, ((FrameWindow) secondWebWindow).getEnclosingPage());
        assertEquals("Second", ((HtmlPage) secondWebWindow.getEnclosedPage()).getTitleText());
    }

    /**
     * Regression test for Bug #203.
     * @throws Exception if the test fails
     */
    @Test
    public void loadingFrameSetWithRelativePaths() throws Exception {
        final String framesContent = DOCTYPE_HTML
            + "<html><head><title>Frames</title></head>\n"
            + "<frameset rows='110,*'>\n"
            + "  <frame src='subdir1/menu.html' name='menu' scrolling='no' border='0' noresize>\n"
            + "  <frame src='subdir2/first.html' name='test' border='0' auto>\n"
            + "</frameset>\n"
            + "<noframes>\n"
            + "  <body>Frames not supported</body>\n"
            + "</noframes>\n"
            + "</html>";
        final String menuContent = DOCTYPE_HTML
            + "<html><head><title>Menu</title></head>\n"
            + "<body>\n"
            + "  <script language='javascript'>\n"
            + "    function changeEditPage() {parent.test.location='../second.html';}\n"
            + "  </script>\n"
            + "  <a name ='changePage' onClick='javascript:changeEditPage();' href='#'>Click</a>."
            + "</body>\n"
            + "</html>";
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head>\n"
            + "<body>First/body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head>\n"
            + "<body>Second</body>\n"
            + "</html>";
        final String baseUrl = "http://framestest";

        final URL framesURL = new URL(baseUrl + "/frames.html");
        final URL menuURL = new URL(baseUrl + "/subdir1/menu.html");
        final URL firstURL = new URL(baseUrl + "/subdir2/first.html");
        final URL secondURL = new URL(baseUrl + "/second.html");

        final WebClient webClient = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(framesURL, framesContent);
        webConnection.setResponse(menuURL, menuContent);
        webConnection.setResponse(firstURL, firstContent);
        webConnection.setResponse(secondURL, secondContent);

        final HtmlPage framesPage = webClient.getPage(framesURL);
        assertEquals("Frames", framesPage.getTitleText());

        final WebWindow menuWebWindow = webClient.getWebWindowByName("menu");
        final HtmlPage menuPage = (HtmlPage) menuWebWindow.getEnclosedPage();
        assertEquals("Menu", menuPage.getTitleText());

        final WebWindow testWebWindow = webClient.getWebWindowByName("test");
        assertEquals("First", ((HtmlPage) testWebWindow.getEnclosedPage()).getTitleText());

        final HtmlAnchor changePage = menuPage.getAnchorByName("changePage");
        changePage.click();
        assertEquals("Second", ((HtmlPage) testWebWindow.getEnclosedPage()).getTitleText());
    }

    /**
     * Forward referencing issue in FrameSet.
     * Test for bug #291
     * @throws Exception if the test fails
     */
    @Test
    public void frameOnloadAccessOtherFrame() throws Exception {
        final String framesContent = DOCTYPE_HTML
            + "<html><head><title>Main</title>\n"
            + "</head>\n"
            + "  <frameset cols='18%,*'>\n"
            + "    <frame name='menu' src='" + URL_SECOND + "'>\n"
            + "    <frame name='button_pallete' src='about:blank'>\n"
            + "  </frameset>\n"
            + "</html>";

        final String menuContent = DOCTYPE_HTML
            + "<html><head><title>Menu</title>\n"
            + "  <script>\n"
            + "    function init() {\n"
            + "      var oFrame = top.button_pallete;\n"
            + "      alert((oFrame == null) ? 'Failure' : 'Success');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='init()'></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();

        final List<String> collectedAlerts = new ArrayList<>();
        final String[] expectedAlerts = {"Success"};
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webConnection.setResponse(URL_FIRST, framesContent);
        webConnection.setResponse(URL_SECOND, menuContent);

        final HtmlPage framesPage = webClient.getPage(URL_FIRST);
        assertEquals("Main", framesPage.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void refererHeader() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + URL_SECOND + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final Map<String, String> lastAdditionalHeaders = webConnection.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void scriptUnderNoFrames() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>first</title></head>\n"
            + "<frameset cols='100%'>\n"
            + "  <frame src='" + URL_SECOND + "'' id='frame1'/>\n"
            + "  <noframes>\n"
            + "    <div><script>alert(1);</script></div>\n"
            + "    <script src='" + URL_THIRD + "'></script>\n"
            + "   </noframes>\n"
            + "</frameset></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><body><script>alert(2);</script></body></html>";
        final String thirdContent
            = "alert('3');\n";
        final WebClient client = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent, "text/javascript");

        final String[] expectedAlerts = {"2"};
        final ArrayList<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for Bug #521.
     * @throws Exception if the test fails
     */
    @Test
    public void frameReloadsAnother() throws Exception {
        final URL framesetURL = new URL("http://domain/frameset.html");
        final URL leftURL = new URL("http://domain/left.html");
        final URL rightURL = new URL("http://domain/right.html");
        final URL right2URL = new URL("http://domain/right2.html");

        final String framesetHtml = DOCTYPE_HTML
            + "<html><head><title>Test Frameset</title><script>\n"
            + "function writeLeftFrame() {\n"
            + "  var leftDoc = leftFrame.document;\n"
            + "  leftDoc.open();\n"
            + "  leftDoc.writeln('<HTML><BODY>This is the left frame.<br><br>'\n"
            + "    + '<A HREF=\"javaScript:parent.showNextPage()\" id=\"node_0\">Show version 2 '\n"
            + "    + 'of right frame</A></BODY></HTML>');\n"
            + "  leftDoc.close();\n"
            + "}\n"
            + "\n"
            + "function showNextPage() {\n"
            + "  rightFrame.location = 'right2.html';\n"
            + "}\n"
            + "</script></head>\n"
            + "<frameset cols='300,*' border=1>\n"
            + "  <frame name='leftFrame' src='left.html'>\n"
            + "  <frame name='rightFrame' src='right.html'>\n"
            + "</frameset>\n"
            + "</html>";

        final String leftHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body onLoad=\"parent.writeLeftFrame()\">\n"
            + "  This is the initial left frame, to be overwritten immediately (onLoad).\n"
            + "</body></html>";

        final String rightHtml = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  This is the right frame, version 1.\n"
            + "</body></html>";

        final String right2Html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  This is the right frame, version 2.\n"
            + "</body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(framesetURL, framesetHtml);
        webConnection.setResponse(leftURL, leftHtml);
        webConnection.setResponse(rightURL, rightHtml);
        webConnection.setResponse(right2URL, right2Html);

        final HtmlPage page = client.getPage(framesetURL);
        final HtmlPage leftPage = (HtmlPage) page.getFrames().get(0).getEnclosedPage();
        final WebWindow rightWindow = page.getFrames().get(1);
        assertTrue(((HtmlPage) rightWindow.getEnclosedPage()).asXml().contains("version 1"));
        leftPage.getAnchors().get(0).click();
        assertTrue(((HtmlPage) rightWindow.getEnclosedPage()).asXml().contains("version 2"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    @HtmlUnitNYI(CHROME = "2",
            EDGE = "2",
            FF = "2",
            FF_ESR = "2")
    public void onunload() throws Exception {
        final String mainHtml =
            "<frameset onunload=\"document.location.href='3.html'\">\n"
            + "<frame name='f1' src='1.html'/>\n"
            + "</frameset>";

        final String frame1 = DOCTYPE_HTML
            + "<html><head><title>1</title></head>\n"
            + "<body><button id='myButton' onclick=\"top.location.href='2.html'\"/></body>\n"
            + "</html>";

        final String html2 = DOCTYPE_HTML
            + "<html><head><title>2</title></head>\n"
            + "<body>hello</body>\n"
            + "</html>";

        final String html3 = DOCTYPE_HTML
            + "<html><head><title>3</title></head>\n"
            + "<body>hello</body>\n"
            + "</html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, mainHtml);
        conn.setResponse(new URL(URL_FIRST, "1.html"), frame1);
        conn.setResponse(new URL(URL_FIRST, "2.html"), html2);
        conn.setResponse(new URL(URL_FIRST, "3.html"), html3);

        final HtmlPage mainPage = webClient.getPage(URL_FIRST);
        final HtmlPage framePage = (HtmlPage) mainPage.getFrameByName("f1").getEnclosedPage();
        final HtmlPage page = framePage.getHtmlElementById("myButton").click();
        assertEquals(getExpectedAlerts()[0], page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void closeShouldRemoveFramesetWindows() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + URL_SECOND + "' frameborder='1' />\n"
            + "  <frame scrolling='auto' name='right' src='" + URL_THIRD + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent  = DOCTYPE_HTML + "<html><head><title>Third</title></head><body></body></html>";

        @SuppressWarnings("resource")
        final WebClient webClient = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        assertEquals(3, webClient.getWebWindows().size());

        webClient.close();

        assertEquals(0, webClient.getWebWindows().size());
        assertEquals(0, webClient.getTopLevelWindows().size());
        assertNull(webClient.getCurrentWindow());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void navigateShouldRemoveFramesetWindows() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + URL_SECOND + "' frameborder='1' />\n"
            + "  <frame scrolling='auto' name='right' src='" + URL_THIRD + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML + "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent  = DOCTYPE_HTML + "<html><head><title>Third</title></head><body></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("First", page.getTitleText());

        assertEquals(3, webClient.getWebWindows().size());

        page = webClient.getPage(URL_SECOND);
        assertEquals("Second", page.getTitleText());

        assertEquals(1, webClient.getWebWindows().size());
    }

}
