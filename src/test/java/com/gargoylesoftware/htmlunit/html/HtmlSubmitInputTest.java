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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for HtmlSubmitInput
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlSubmitInputTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlSubmitInputTest( final String name ) {
        super( name );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testSubmit() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='submit' name='aButton' value='foo'/>"
                 + "<input type='suBMit' name='button' value='foo'/>"
                 + "<input type='submit' name='anotherButton' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSubmitInput submitInput = (HtmlSubmitInput)form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage)submitInput.click();
        assertEquals("foo", secondPage.getTitleText());

        assertEquals(
            Collections.singletonList(new KeyValuePair("button", "foo")),
            webConnection.getLastParameters() );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testClick_onClick() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1' onSubmit='alert(\"bar\")'>"
                 + "    <input type='submit' name='button' value='foo' onClick='alert(\"foo\")'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        final CollectingAlertHandler alertHandler = new CollectingAlertHandler(collectedAlerts);
        client.setAlertHandler(alertHandler);

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );
        final HtmlSubmitInput submitInput = (HtmlSubmitInput)form.getInputByName("button");

        submitInput.click();

        final List expectedAlerts = Arrays.asList( new String[]{"foo","bar"} );
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testClick_onClick_JavascriptReturnsTrue() throws Exception {

        final String firstContent
                 = "<html><head><title>First</title></head><body>"
                 + "<form name='form1' method='get' action='http://second'>"
                 + "<input name='button' type='submit' value='PushMe' id='button1'"
                 + "onclick='return true'/></form>"
                 + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(
            URL_FIRST, firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            URL_SECOND,secondContent,200,"OK","text/html",Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final HtmlPage firstPage = ( HtmlPage )client.getPage(
                URL_FIRST, SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlSubmitInput input = (HtmlSubmitInput)firstPage.getHtmlElementById("button1");
        final HtmlPage secondPage = (HtmlPage)input.click();
        assertEquals("Second", secondPage.getTitleText());
    }
}
