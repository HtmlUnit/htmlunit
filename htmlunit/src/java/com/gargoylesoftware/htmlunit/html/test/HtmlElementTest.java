/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.Collections;

/**
 *  Tests for HtmlForm
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlElementTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlElementTest( final String name ) {
        super( name );
    }


    public void testGetEnclosingForm()
        throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'>"
                 + "<table><tr><td><input type='text' id='foo'/></td></tr></table>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
            new URL( "http://www.gargoylesoftware.com" ),
            SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlInput input = ( HtmlInput )form.getHtmlElementById( "foo" );
        assertSame( form, input.getEnclosingForm() );
    }


    public void testAsText_WithComments() throws Exception {
        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<p id='p1'>foo<!--bar--></p>"
                 + "</body></html>";
        final WebClient client = new WebClient();

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
            new URL( "http://www.gargoylesoftware.com" ),
            SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlElement element = page.getHtmlElementById("p1");
        assertEquals("foo", element.asText());
    }


    public void testConstants() {
        assertEquals( "", HtmlElement.ATTRIBUTE_NOT_DEFINED );
        assertEquals( "", HtmlElement.ATTRIBUTE_VALUE_EMPTY );
        assertTrue( "Not the same object",
            HtmlElement.ATTRIBUTE_NOT_DEFINED != HtmlElement.ATTRIBUTE_VALUE_EMPTY );
    }

}
