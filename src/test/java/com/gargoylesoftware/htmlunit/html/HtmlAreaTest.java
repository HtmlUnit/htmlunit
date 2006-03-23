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
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for HtmlAnchor
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 */
public class HtmlAreaTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name Name of the test
     */
    public HtmlAreaTest( final String name ) {
        super( name );
    }


    private WebClient createWebClient( final String onClick ) {
        final String firstContent
            = "<html><head><title>first</title></head><body>"
            + "<img src='/images/planets.gif' width='145' height='126' usemap='#planetmap'>"
            + "<map id='planetmap' name='planetmap'>"
            + "<area shape='rect' onClick=\""+onClick+"\" coords='0,0,82,126' id='second' href='http://second'>"
            + "<area shape='circle' coords='90,58,3' id='third' href='http://third'>"
            + "</map></body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent
            = "<html><head><title>third</title></head><body></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection( webConnection );
        return client;
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick() throws Exception {

        final WebClient client = createWebClient("");

        final HtmlPage page = ( HtmlPage )client.getPage(URL_FIRST);
        final HtmlArea area = ( HtmlArea )page.getHtmlElementById( "third" );

        // Test that the correct value is being passed back up to the server
        final HtmlPage thirdPage = ( HtmlPage )area.click();
        assertEquals("third", thirdPage.getTitleText());
    }


    /**
     * @throws Exception if the test fails
     */
    public void testClick_onclickReturnsFalse() throws Exception {

        final WebClient client = createWebClient("alert('foo');return false;");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = ( HtmlPage )client.getPage(URL_FIRST);
        final HtmlArea area = ( HtmlArea )page.getHtmlElementById( "second" );

        final HtmlPage thirdPage = ( HtmlPage )area.click();
        assertEquals( Collections.singletonList("foo"), collectedAlerts);
        assertEquals("first", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick_onclickReturnsTrue() throws Exception {

        final WebClient client = createWebClient("alert('foo');return true;");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = ( HtmlPage )client.getPage(URL_FIRST);
        final HtmlArea area = ( HtmlArea )page.getHtmlElementById( "second" );

        final HtmlPage thirdPage = ( HtmlPage )area.click();
        assertEquals( Collections.singletonList("foo"), collectedAlerts);
        assertEquals("second", thirdPage.getTitleText());
    }
}
