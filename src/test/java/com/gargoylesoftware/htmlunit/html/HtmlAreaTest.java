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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlArea}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public class HtmlAreaTest extends WebTestCase {

    private WebClient createWebClient(final String onClick) {
        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<img src='/images/planets.gif' width='145' height='126' usemap='#planetmap'>\n"
            + "<map id='planetmap' name='planetmap'>\n"
            + "<area shape='rect' onClick=\"" + onClick + "\" coords='0,0,82,126' id='second' "
            + "href='" + URL_SECOND + "'>\n"
            + "<area shape='circle' coords='90,58,3' id='third' href='" + URL_THIRD + "'>\n"
            + "</map></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);
        return client;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick() throws Exception {
        final WebClient client = createWebClient("");

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlArea area = (HtmlArea) page.getHtmlElementById("third");

        // Test that the correct value is being passed back up to the server
        final HtmlPage thirdPage = (HtmlPage) area.click();
        assertEquals("third", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onclickReturnsFalse() throws Exception {
        final WebClient client = createWebClient("alert('foo');return false;");
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlArea area = (HtmlArea) page.getHtmlElementById("second");

        final HtmlPage thirdPage = (HtmlPage) area.click();
        assertEquals(new String[] {"foo"}, collectedAlerts);
        assertEquals("first", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onclickReturnsTrue() throws Exception {
        final WebClient client = createWebClient("alert('foo');return true;");
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlArea area = (HtmlArea) page.getHtmlElementById("second");

        final HtmlPage thirdPage = (HtmlPage) area.click();
        assertEquals(new String[] {"foo"}, collectedAlerts);
        assertEquals("second", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body><map>\n"
            + "<area href='javascript:alert(\"clicked\")' id='a2' coords='0,0,10,10'/>\n"
            + "</map></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlArea area = (HtmlArea) page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = (HtmlPage) area.click();

        assertEquals(new String[] {"clicked"}, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body><map>\n"
            + "<area href='javascript:alert(\"clicked\")' id='a2' coords='0,0,10,10'/>\n"
            + "</map></body></html>";
        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);
        final HtmlArea area = (HtmlArea) page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = (HtmlPage) area.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * In action "this" should be the window and not the area.
     * @throws Exception if the test fails
     */
    @Test
    public void testThisInJavascriptHRef() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body><map>\n"
            + "<area href='javascript:alert(this == window)' id='a2' coords='0,0,10,10'/>\n"
            + "</map></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"true"};
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final Page page2 = ((HtmlArea) page.getHtmlElementById("a2")).click();

        assertEquals(expectedAlerts, collectedAlerts);
        assertSame(page, page2);
    }

}
