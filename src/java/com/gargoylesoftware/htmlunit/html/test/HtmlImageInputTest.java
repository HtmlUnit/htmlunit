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
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  Tests for HtmlImageInput
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlImageInputTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlImageInputTest( final String name ) {
        super( name );
    }


    public void testClick_NoPosition() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='image' name='aButton' value='foo'/>"
                 + "<input type='image' name='button' value='foo'/>"
                 + "<input type='image' name='anotherButton' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlImageInput imageInput = (HtmlImageInput)form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage)imageInput.click();

        assertEquals(
            Collections.singletonList(new KeyValuePair("button", "foo")),
            webConnection.getLastParameters() );
    }


    public void testClick_WithPosition() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<input type='image' name='aButton' value='foo'/>"
                 + "<input type='image' name='button' value='foo'/>"
                 + "<input type='image' name='anotherButton' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlImageInput imageInput = (HtmlImageInput)form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage)imageInput.click(100,200);

        final List expectedPairs = Arrays.asList( new Object[]{
            new KeyValuePair("button", "foo"),
            new KeyValuePair("button.x", "100"),
            new KeyValuePair("button.y", "200")
        });

        assertEquals(
            expectedPairs,
            webConnection.getLastParameters() );
    }
}
