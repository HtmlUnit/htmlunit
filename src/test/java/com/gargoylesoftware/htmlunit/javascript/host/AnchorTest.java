/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Anchor}.
 *
 * @version  $Revision$
 * @author  <a href="mailto:gousseff@netscape.net">Alexei Goussev</a>
 * @author Marc Guillemot
 */
public class AnchorTest extends WebTestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public AnchorTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testAnchor_getAttribute_and_href() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String content
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function doTest(anchorElement) {\n"
            + "    alert(anchorElement.href);\n"
            + "    alert(anchorElement.getAttribute('href'));\n"
            + "    anchorElement.href='testsite2.html';\n"
            + "    alert(anchorElement.href);\n"
            + "    alert(anchorElement.getAttribute('href'));\n"
            + "    alert(anchorElement.getAttribute('id'));\n"
            + "    alert(anchorElement.getAttribute('name'));\n"
            + "}\n</script></head>"
            + "<body>"
            + "<a href='testsite1.html' id='13' name='testanchor' onClick='doTest(this);return false'>"
            + "</body></html>";
        
        webConnection.setDefaultResponse( content );
        client.setWebConnection( webConnection );
        
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = (HtmlPage)(client.getPage( new URL("http://x")));
                
        final HtmlAnchor anchor = page.getAnchorByName("testanchor");
        
        anchor.click();
        
        final String[] expectedAlerts = { "http://x/testsite1.html", "testsite1.html", "http://x/testsite2.html", "testsite2.html", "13", "testanchor"};
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOnclickToString() throws Exception {
        final String content
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function test() {\n"
            + "    for (var i=0; i<document.links.length; ++i)"
            + "    {\n"
            + "        var onclick = document.links[i].onclick;\n"
            + "        alert(onclick ? (onclick.toString().indexOf('alert(') != -1) : 'not defined');\n"
            + "    }\n"
            + "}\n</script></head>"
            + "<body onload='test()'>"
            + "<a href='foo.html' onClick='alert(\"on click\")'>"
            + "<a href='foo2.html'>"
            + "</body></html>";
        
        
        final List collectedAlerts = new ArrayList();
        final String[] expectedAlerts = {"true", "not defined"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testDefaultConversionToString() throws Exception {
        final String content
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function test() {\n"
            + "  alert(document.getElementById('myAnchor'));\n"
            + "  for (var i=0; i<document.links.length; ++i)"
            + "  {\n"
            + "    alert(document.links[i]);\n"
            + "  }\n"
            + "}</script></head>"
            + "<body onload='test()'>"
            + "<a name='start' id='myAnchor'/>"
            + "<a href='foo.html'>foo</a>"
            + "<a href='javascript:void(0)'>void</a>"
            + "<a href='#'>#</a>"
            + "</body></html>";
        
        
        final List collectedAlerts = new ArrayList();
        final String[] expectedAlerts = { "", 
            "http://www.gargoylesoftware.com/foo.html",
            "javascript:void(0)",
            "http://www.gargoylesoftware.com/#"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOnClickAnchorHRef() throws Exception {
        final String content
            = "<html>"
            + "<body>"
            + "<a href='#' onclick='document.form1.submit()'>link 1</a>"
            + "<form name='form1' action='foo.html' method='post'>"
            + "<input name='testText'>"
            + "</form>"
            + "</body></html>";
        
        final HtmlPage page1 = loadPage(content);
        final Page page2 = page1.getAnchorByHref("#").click();

        assertEquals("http://www.gargoylesoftware.com/foo.html",  page2.getWebResponse().getUrl());
    }

    /**
     * Regression test for
     * https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1689798&group_id=47038
     * in href, "this" should be the window and not the link
     * @throws Exception if the test fails
     */
    public void testThisInJavascriptHRef() throws Exception {
        final String content
            = "<html>"
            + "<body>"
            + "<a href='javascript:alert(this == window)'>link 1</a>"
            + "</body></html>";
        
        final List collectedAlerts = new ArrayList();
        final String[] expectedAlerts = {"true"};
        final HtmlPage page1 = loadPage(content, collectedAlerts);
        final Page page2 = ((HtmlAnchor) page1.getAnchors().get(0)).click();

        assertEquals(expectedAlerts, collectedAlerts);
        assertSame(page1, page2);
    }

    public void testReadWriteAnchorTarget() throws Exception {
        final String content 
            = "<html>"
            + "<body onload=\"document.links[0].target += 'K';\">"
            + "<a href='#' target='O'>link 1</a>"
            + "</body></html>";
        final HtmlPage page1 = loadPage(content);
        final HtmlAnchor link = (HtmlAnchor)page1.getAnchors().get(0);
        assertEquals("OK", link.getTargetAttribute());
    }
    
    public void testReadWriteAnchorSearch() throws Exception {
        final String content 
            = "<html>"
            + "<body onload=\"document.links[0].search += '&p2=2';\">"
            + "<a href='foo.html?p1=1' target='O'>link 1</a>"
            + "</body></html>";
        final HtmlPage page1 = loadPage(content);
        final HtmlAnchor link = (HtmlAnchor)page1.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com/foo.html?p1=1&p2=2", link.getHrefAttribute());
    }

    public void testReadWriteAnchorHash() throws Exception {
        final String content 
            = "<html>"
            + "<body onload=\"document.links[0].hash += 'K';\">"
            + "<a href='foo.html#O'>link 1</a>"
            + "</body></html>";
        final HtmlPage page1 = loadPage(content);
        final HtmlAnchor link = (HtmlAnchor)page1.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com/foo.html#OK", link.getHrefAttribute());
    }

    public void testReadWriteAnchorPort() throws Exception {
        final String content 
            = "<html>"
            + "<body onload=\"document.links[0].port += '80';"
            + "    document.links[1].port += '80'; \">"
            + "<a href='foo.html#O'>link 1</a>"
            + "<a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 1</a>"
            + "</body></html>";
        final HtmlPage page1 = loadPage(content);
        HtmlAnchor link = (HtmlAnchor) page1.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com:80/foo.html#O", link.getHrefAttribute());
        link = (HtmlAnchor) page1.getAnchors().get(1);
        assertEquals("http://www.gargoylesoftware.com:8080/foo.html#O", link.getHrefAttribute());
    }

    public void testReadWritePathname() throws Exception {
        final String content 
            = "<html>"
            + "<body onload=\"document.links[0].pathname = '/bar' + document.links[0].pathname;\">"
            + "<a href='foo.html#B'>link 1</a>"
            + "</body></html>";
        final HtmlPage page1 = loadPage(content);
        final HtmlAnchor link = (HtmlAnchor) page1.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com/bar/foo.html#B", link.getHrefAttribute());
    }

    public void testReadWriteProtocol() throws Exception {
        final String content 
            = "<html>"
            + "<body onload=\"document.links[0].protocol = document.links[0].protocol.substring(0,4) + 's:';\">"
            + "<a href='foo.html#B'>link 1</a>"
            + "</body></html>";
        final HtmlPage page1 = loadPage(content);
        final HtmlAnchor link = (HtmlAnchor)page1.getAnchors().get(0);
        assertEquals("https://www.gargoylesoftware.com/foo.html#B", link.getHrefAttribute());
    }

    public void testReadWriteAnchorHost() throws Exception {
        final String content 
            = "<html>"
            + "<body onload=\"document.links[0].host += 'motion:8080';" 
            +    " document.links[1].host += 'motion';" 
            +    " document.links[2].host += '80';" 
            +    " document.links[3].host = 'www.gargoylesoftware.com'; \">"
            + "<a href='foo.html#O'>link 0</a>"
            + "<a href='foo.html#O'>link 1</a>"
            + "<a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 2</a>"
            + "<a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 3</a>"
            + "</body></html>";
        final HtmlPage page1 = loadPage(content);
        HtmlAnchor link = (HtmlAnchor)page1.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.commotion:8080/foo.html#O", link.getHrefAttribute());
        link = (HtmlAnchor) page1.getAnchors().get(1);
        assertEquals("http://www.gargoylesoftware.commotion/foo.html#O", link.getHrefAttribute());
        link = (HtmlAnchor) page1.getAnchors().get(2);
        assertEquals("http://www.gargoylesoftware.com:8080/foo.html#O", link.getHrefAttribute());
        link = (HtmlAnchor) page1.getAnchors().get(3);
        assertEquals("http://www.gargoylesoftware.com/foo.html#O", link.getHrefAttribute());
    }

    public void testReadWriteAnchorHostname() throws Exception {
        final String content 
            = "<html>"
            + "<body onload=\"document.links[0].hostname += 'motion';\">"
            + "<a href='foo.html#O'>link 1</a>"
            + "</body></html>";
        final HtmlPage page1 = loadPage(content);
        final HtmlAnchor link = (HtmlAnchor) page1.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.commotion/foo.html#O", link.getHrefAttribute());
    }
}
