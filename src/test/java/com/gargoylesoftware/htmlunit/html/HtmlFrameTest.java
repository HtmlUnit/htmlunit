/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FakeWebConnection;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for HtmlFrame
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlFrameTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name Name of the test
     */
    public HtmlFrameTest( final String name ) {
        super( name );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSrcOfBlankAndEmpty() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection =
            new FakeWebConnection(webClient);
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>"
            + "<frameset cols='20%,80%'>"
            + "    <frame src='' id='frame1'>"
            + "    <frame src='about:blank'  id='frame2'>"
            + "</frameset></html>";
        webConnection.setResponse( new URL("http://first"), firstContent,
            200, "OK", "text/html", Collections.EMPTY_LIST);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage)webClient.getPage(
            new URL("http://first"), SubmitMethod.POST, Collections.EMPTY_LIST);
        assertEquals( "first", page.getTitleText() );

        final HtmlFrame frame1 = (HtmlFrame)page.getHtmlElementById("frame1");
        assertEquals( "frame1", "", ((HtmlPage)frame1.getEnclosedPage()).getTitleText() );

        final HtmlFrame frame2 = (HtmlFrame)page.getHtmlElementById("frame2");
        assertEquals( "frame2", "", ((HtmlPage)frame2.getEnclosedPage()).getTitleText() );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOnLoadHandler() throws Exception {
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection =
            new FakeWebConnection(webClient);
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>"
            + "<frameset cols='20%,80%'>"
            + "    <frame id='frame1'>"
            + "    <frame onload=\"alert('onload');\"  id='frame2'>"
            + "</frameset></html>";
        webConnection.setResponse( new URL("http://first"), firstContent,
            200, "OK", "text/html", Collections.EMPTY_LIST);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage)webClient.getPage(
            new URL("http://first"), SubmitMethod.POST, Collections.EMPTY_LIST);
        assertEquals( "first", page.getTitleText() );

        final HtmlFrame frame1 = (HtmlFrame)page.getHtmlElementById("frame1");
        assertEquals( "frame1", "", ((HtmlPage)frame1.getEnclosedPage()).getTitleText() );

        final HtmlFrame frame2 = (HtmlFrame)page.getHtmlElementById("frame2");
        assertEquals( "frame2", "", ((HtmlPage)frame2.getEnclosedPage()).getTitleText() );

        assertEquals( Collections.singletonList("onload"), collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testDocumentWrite() throws Exception {
        if( true ) {
            notImplemented();
            return;
        }
        final WebClient webClient = new WebClient();
        final FakeWebConnection webConnection =
            new FakeWebConnection(webClient);
        final List collectedAlerts = new ArrayList();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstContent
            = "<html><head><title>first</title></head>"
            + "<frameset cols='20%,80%'>"
            + "    <frame src='' name='frame1' id='frame1'>"
            + "    <frame onload=\"frame1.document.open();frame1.document.write("
            + "'<html><head><title>generated</title></head><body>generated</body></html>');"
            + "frame1.document.close()\"  id='frame2'>"
            + "</frameset></html>";
        webConnection.setResponse( new URL("http://first"), firstContent,
            200, "OK", "text/html", Collections.EMPTY_LIST);

        webClient.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage)webClient.getPage(
            new URL("http://first"), SubmitMethod.POST, Collections.EMPTY_LIST);
        assertEquals( "first", page.getTitleText() );

        final HtmlFrame frame1 = (HtmlFrame)page.getHtmlElementById("frame1");
        assertEquals( "frame1", "generated", ((HtmlPage)frame1.getEnclosedPage()).getTitleText() );

        final HtmlFrame frame2 = (HtmlFrame)page.getHtmlElementById("frame2");
        assertEquals( "frame2", "", ((HtmlPage)frame2.getEnclosedPage()).getTitleText() );
    }
}