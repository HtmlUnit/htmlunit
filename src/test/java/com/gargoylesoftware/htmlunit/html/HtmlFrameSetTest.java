/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.assertSame;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Tests for {@link HtmlFrameSet}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Hans Donner
 * @author Ahmed Ashour
 */
public class HtmlFrameSetTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testLoadingFrameSet() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + URL_SECOND + "' frameborder='1' />\n"
            + "  <frame scrolling='auto' name='right' src='" + URL_THIRD + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdContent  = "<html><head><title>Third</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
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
    public void testLoadingIFrames() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "  <iframe name='left' src='" + URL_SECOND + "' />\n"
            + "  some stuff"
            + "</html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final WebWindow secondWebWindow = webClient.getWebWindowByName("left");
        assertTrue(FrameWindow.class.isInstance(secondWebWindow));
        assertSame(firstPage, ((FrameWindow) secondWebWindow).getEnclosingPage());
        assertEquals("Second", ((HtmlPage) secondWebWindow.getEnclosedPage()).getTitleText());
    }

    /**
     * Regression test for http://sourceforge.net/tracker/index.php?func=detail&aid=1101525&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void testLoadingFrameSetWithRelativePaths() throws Exception {
        final String framesContent
            = "<html><head><title>Frames</title></head>\n"
            + "<frameset rows='110,*'>\n"
            + "  <frame src='subdir1/menu.html' name='menu' scrolling='no' border='0' noresize>\n"
            + "  <frame src='subdir2/first.html' name='test' border='0' auto>\n"
            + "</frameset>\n"
            + "<noframes>\n"
            + "  <body>Frames not supported</body>\n"
            + "</noframes>\n"
            + "</html>";
        final String menuContent
            = "<html><head><title>Menu</title></head>\n"
            + "<body>\n"
            + "  <script language='javascript'>\n"
            + "    function changeEditPage() {parent.test.location='../second.html';}\n"
            + "  </script>\n"
            + "  <a name ='changePage' onClick='javascript:changeEditPage();' href='#'>Click</a>."
            + "</body>\n"
            + "</html>";
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>First/body>\n"
            + "</html>";
        final String secondContent
            = "<html><head><title>Second</title></head>\n"
            + "<body>Second</body>\n"
            + "</html>";
        final String baseUrl = "http://framestest";

        final URL framesURL = new URL(baseUrl + "/frames.html");
        final URL menuURL = new URL(baseUrl + "/subdir1/menu.html");
        final URL firstURL = new URL(baseUrl + "/subdir2/first.html");
        final URL secondURL = new URL(baseUrl + "/second.html");

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(framesURL, framesContent);
        webConnection.setResponse(menuURL, menuContent);
        webConnection.setResponse(firstURL, firstContent);
        webConnection.setResponse(secondURL, secondContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage framesPage = (HtmlPage) webClient.getPage(framesURL);
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
     * Test for bug 1239285
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1239285&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    @Test
    public void frameOnloadAccessOtherFrame() throws Exception {
        final String framesContent
            = "<html><head><title>Main</title>\n"
            + "</head>\n"
            + "  <frameset cols='18%,*'>\n"
            + "    <frame name='menu' src='" + URL_SECOND + "'>\n"
            + "    <frame name='button_pallete' src='about:blank'>\n"
            + "  </frameset>\n"
            + "</html>";

        final String menuContent = "<html><head><title>Menu</title>\n"
            + "  <script>\n"
            + "    function init()\n"
            + "    {\n"
            + "      var oFrame = top.button_pallete;\n"
            + "      alert((oFrame == null) ? 'Failure' : 'Success');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='init()'></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"Success"};
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webConnection.setResponse(URL_FIRST, framesContent);
        webConnection.setResponse(URL_SECOND, menuContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage framesPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("Main", framesPage.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void refererHeader() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + URL_SECOND + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient webClient = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(webClient);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final Map<String, String> lastAdditionalHeaders = webConnection.getLastAdditionalHeaders();
        assertEquals(URL_FIRST.toString(), lastAdditionalHeaders.get("Referer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void scriptUnderNoFrames() throws Exception {
        final String firstContent
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='100%'>\n"
            + "  <frame src='" + URL_SECOND + "'' id='frame1'/>\n"
            + "  <noframes>\n"
            + "    <div><script>alert(1);</script></div>\n"
            + "    <script src='" + URL_THIRD + "'></script>\n"
            + "   </noframes>\n"
            + "</frameset></html>";
        final String secondContent
            = "<html><body><script>alert(2);</script></body></html>";
        final String thirdContent
            = "alert('3');\n";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent, "text/javascript");

        client.setWebConnection(webConnection);
        
        final String[] expectedAlerts = {"2"};
        final ArrayList<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        
        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for https://sf.net/tracker/index.php?func=detail&aid=1794764&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void frameReloadsAnother() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final URL framesetURL = new URL("http://domain/frameset.html");
        final URL leftURL = new URL("http://domain/left.html");
        final URL rightURL = new URL("http://domain/right.html");
        final URL right2URL = new URL("http://domain/right2.html");

        final String framesetHtml
            = "<html><head><title>Test Frameset</title><script>\n"
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

        final String leftHtml
            = "<html>"
            + "<body onLoad=\"parent.writeLeftFrame()\">\n"
            + "  This is the initial left frame, to be overwritten immediately (onLoad).\n"
            + "</body></html>";

        final String rightHtml
            = "<html>"
            + "<body>\n"
            + "  This is the right frame, version 1.\n"
            + "</body></html>";

        final String right2Html
            = "<html>"
            + "<body>\n"
            + "  This is the right frame, version 2.\n"
            + "</body></html>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(framesetURL, framesetHtml);
        webConnection.setResponse(leftURL, leftHtml);
        webConnection.setResponse(rightURL, rightHtml);
        webConnection.setResponse(right2URL, right2Html);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(framesetURL);
        final HtmlPage leftPage = (HtmlPage) page.getFrames().get(0).getEnclosedPage();
        final HtmlPage rightPage = (HtmlPage) page.getFrames().get(1).getEnclosedPage();
        assertTrue(rightPage.asXml().contains("version 1"));
        leftPage.getAnchors().get(0).click();
        assertTrue(rightPage.asXml().contains("version 2"));
    }
}
