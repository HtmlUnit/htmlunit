/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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


    public void testSubmit() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='submit' name='aButton' value='foo'/>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "<input type='submit' name='anotherButton' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlSubmitInput submitInput = (HtmlSubmitInput)form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage)submitInput.click();

        assertEquals(
            Collections.singletonList(new KeyValuePair("button", "foo")),
            webConnection.getLastParameters() );
    }


    public void testClick_onClick() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1' onSubmit='alert(\"bar\")'>"
                 + "    <input type='submit' name='button' value='foo' onClick='alert(\"foo\")'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        final CollectingAlertHandler alertHandler = new CollectingAlertHandler(collectedAlerts);
        client.setAlertHandler(alertHandler);

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );
        final HtmlSubmitInput submitInput = (HtmlSubmitInput)form.getInputByName("button");

        final HtmlPage secondPage = (HtmlPage)submitInput.click();

        final List expectedAlerts = Arrays.asList( new String[]{"foo"} );
        assertEquals( expectedAlerts, collectedAlerts );

        assertSame( page, secondPage );
    }
}
