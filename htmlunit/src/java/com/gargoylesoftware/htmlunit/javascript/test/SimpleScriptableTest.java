/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class SimpleScriptableTest extends WebTestCase {
    public SimpleScriptableTest( final String name ) {
        super(name);
    }


    public void testCallInheritedFunction() throws Exception {
        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

        final String content
             = "<html><head><title>foo</title><script>"
             + "document.form1.textfield1.focus();\n"
             + "alert('past focus');\n"
             + "</script></head><body>"
             + "<p>hello world</p>"
             + "<form name='form1'>"
             + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
             + "</form>"
             + "</body></html>";

        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final List expectedAlerts = Collections.singletonList("past focus");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", page.getTitleText());

        assertEquals( expectedAlerts, collectedAlerts );
    }
}
