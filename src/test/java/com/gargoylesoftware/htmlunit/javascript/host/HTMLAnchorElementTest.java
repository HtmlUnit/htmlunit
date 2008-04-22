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
package com.gargoylesoftware.htmlunit.javascript.host;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLAnchorElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:gousseff@netscape.net">Alexei Goussev</a>
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLAnchorElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getAttribute_and_href() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String html
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function doTest(anchorElement) {\n"
            + "    alert(anchorElement.href);\n"
            + "    alert(anchorElement.getAttribute('href'));\n"
            + "    anchorElement.href='testsite2.html';\n"
            + "    alert(anchorElement.href);\n"
            + "    alert(anchorElement.getAttribute('href'));\n"
            + "    alert(anchorElement.getAttribute('id'));\n"
            + "    alert(anchorElement.getAttribute('name'));\n"
            + "    var link2 = document.getElementById('link2');\n"
            + "    alert(link2.href);\n"
            + "}\n</script></head>\n"
            + "<body>\n"
            + "<a href='testsite1.html' id='13' name='testanchor' onClick='doTest(this);return false'>bla</a>\n"
            + "<a href='mailto:' id='link2'>mail</a>\n"
            + "</body></html>";
        
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);
        
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) (client.getPage("http://x"));
                
        final HtmlAnchor anchor = page.getAnchorByName("testanchor");
        
        anchor.click();
        
        final String[] expectedAlerts = {"http://x/testsite1.html", "testsite1.html",
            "http://x/testsite2.html", "testsite2.html", "13", "testanchor", "mailto:"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "not defined" })
    public void testOnclickToString() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function test() {\n"
            + "    for (var i=0; i<document.links.length; ++i)\n"
            + "    {\n"
            + "        var onclick = document.links[i].onclick;\n"
            + "        alert(onclick ? (onclick.toString().indexOf('alert(') != -1) : 'not defined');\n"
            + "    }\n"
            + "}\n</script></head>\n"
            + "<body onload='test()'>\n"
            + "<a href='foo.html' onClick='alert(\"on click\")'>\n"
            + "<a href='foo2.html'>\n"
            + "</body></html>";
        
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "http://www.gargoylesoftware.com/foo.html",
        "javascript:void(0)", "http://www.gargoylesoftware.com/#", "mailto:" })
    public void testDefaultConversionToString() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function test() {\n"
            + "  alert(document.getElementById('myAnchor'));\n"
            + "  for (var i=0; i<document.links.length; ++i)\n"
            + "  {\n"
            + "    alert(document.links[i]);\n"
            + "  }\n"
            + "}</script></head>\n"
            + "<body onload='test()'>\n"
            + "<a name='start' id='myAnchor'/>\n"
            + "<a href='foo.html'>foo</a>\n"
            + "<a href='javascript:void(0)'>void</a>\n"
            + "<a href='#'>#</a>\n"
            + "<a href='mailto:'>mail</a>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOnClickAnchorHRef() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<a href='#' onclick='document.form1.submit()'>link 1</a>\n"
            + "<form name='form1' action='foo.html' method='post'>\n"
            + "<input name='testText'>\n"
            + "</form>\n"
            + "</body></html>";
        
        final HtmlPage page1 = loadPage(getBrowserVersion(), html, null);
        final Page page2 = page1.getAnchorByHref("#").click();

        assertEquals("http://www.gargoylesoftware.com/foo.html",  page2.getWebResponse().getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7 })
    public void testJavaScriptAnchorClick() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "</script></head><body>\n"
            + "<a id='link1' href='#' onclick='document.form1.submit()'>link 1</a>\n"
            + "<form name='form1' action='" + URL_SECOND + "' method='post'>\n"
            + "<input type=button id='button1' value='Test' onclick='document.getElementById(\"link1\").click()'>\n"
            + "<input name='testText'>\n"
            + "</form>\n"
            + "</body></html>";

        final String secondContent
            = "<html>\n"
            + "<head><title>Second</title></head>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(conn);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlButtonInput button = (HtmlButtonInput) page.getHtmlElementById("button1");
        final HtmlPage page2 = (HtmlPage) button.click();

        assertEquals("Second",  page2.getTitleText());
    }
    
    /**
     * Regression test for https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1689798&group_id=47038.
     * In href, "this" should be the window and not the link.
     * @throws Exception if the test fails
     */
    @Test
    public void testThisInJavascriptHRef() throws Exception {
        final String content
            = "<html>\n"
            + "<body>\n"
            + "<a href='javascript:alert(this == window)'>link 1</a>\n"
            + "</body></html>";
        
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"true"};
        final HtmlPage page1 = loadPage(getBrowserVersion(), content, collectedAlerts);
        final Page page2 = page1.getAnchors().get(0).click();

        assertEquals(expectedAlerts, collectedAlerts);
        assertSame(page1, page2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadWriteAnchorTarget() throws Exception {
        final String content
            = "<html>\n"
            + "<body onload=\"document.links[0].target += 'K';\">\n"
            + "<a href='#' target='O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page1 = loadPage(getBrowserVersion(), content, null);
        final HtmlAnchor link = page1.getAnchors().get(0);
        assertEquals("OK", link.getTargetAttribute());
    }
    
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadWriteAnchorSearch() throws Exception {
        final String content
            = "<html>\n"
            + "<body onload=\"document.links[0].search += '&p2=2';\">\n"
            + "<a href='foo.html?p1=1' target='O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page1 = loadPage(getBrowserVersion(), content, null);
        final HtmlAnchor link = page1.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com/foo.html?p1=1&p2=2", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadWriteAnchorHash() throws Exception {
        final String content
            = "<html>\n"
            + "<body onload=\"document.links[0].hash += 'K';\">\n"
            + "<a href='foo.html#O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), content, null);
        final HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com/foo.html#OK", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadWriteAnchorPort() throws Exception {
        final String content
            = "<html>\n"
            + "<body onload=\"document.links[0].port += '80';\n"
            + "    document.links[1].port += '80'; \">\n"
            + "<a href='foo.html#O'>link 1</a>\n"
            + "<a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), content, null);
        HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com:80/foo.html#O", link.getHrefAttribute());
        link = page.getAnchors().get(1);
        assertEquals("http://www.gargoylesoftware.com:8080/foo.html#O", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadWritePathname() throws Exception {
        final String content
            = "<html>\n"
            + "<body onload=\"document.links[0].pathname = '/bar' + document.links[0].pathname;\">\n"
            + "<a href='foo.html#B'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), content, null);
        final HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com/bar/foo.html#B", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadWriteProtocol() throws Exception {
        final String content
            = "<html>\n"
            + "<body onload=\"document.links[0].protocol = document.links[0].protocol.substring(0,4) + 's:';\">\n"
            + "<a href='foo.html#B'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), content, null);
        final HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("https://www.gargoylesoftware.com/foo.html#B", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadWriteAnchorHost() throws Exception {
        final String content
            = "<html>\n"
            + "<body onload=\"document.links[0].host += 'motion:8080';\n"
            +    " document.links[1].host += 'motion';\n"
            +    " document.links[2].host += '80';\n"
            +    " document.links[3].host = 'www.gargoylesoftware.com'; \">\n"
            + "<a href='foo.html#O'>link 0</a>\n"
            + "<a href='foo.html#O'>link 1</a>\n"
            + "<a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 2</a>\n"
            + "<a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 3</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), content, null);
        HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.commotion:8080/foo.html#O", link.getHrefAttribute());
        link = page.getAnchors().get(1);
        assertEquals("http://www.gargoylesoftware.commotion/foo.html#O", link.getHrefAttribute());
        link = page.getAnchors().get(2);
        assertEquals("http://www.gargoylesoftware.com:8080/foo.html#O", link.getHrefAttribute());
        link = page.getAnchors().get(3);
        assertEquals("http://www.gargoylesoftware.com/foo.html#O", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testReadWriteAnchorHostname() throws Exception {
        final String content
            = "<html>\n"
            + "<body onload=\"document.links[0].hostname += 'motion';\">\n"
            + "<a href='foo.html#O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), content, null);
        final HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.commotion/foo.html#O", link.getHrefAttribute());
    }
}
