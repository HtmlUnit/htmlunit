/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.base.testing.BaseTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import junit.framework.AssertionFailedError;

/**
 * Common superclass for HtmlUnit tests
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WebTestCase extends BaseTestCase {
    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public WebTestCase( final String name ) {
        super( name );
    }


    /**
     * Load a page with the specified html.
     * @param html The html to use.
     * @return The new page.
     * @throws Exception if something goes wrong.
     */
    protected final HtmlPage loadPage( final String html ) throws Exception {
        return loadPage(html, null);
    }


    /**
     * Load a page with the specified html and collect alerts into the list.
     * @param html The HTML to use.
     * @param collectedAlerts The list to hold the alerts.
     * @return The new page.
     * @throws Exception If something goes wrong.
     */
    protected final HtmlPage loadPage( final String html, final List collectedAlerts )
        throws Exception {

        final WebClient client = new WebClient();
        if( collectedAlerts != null ) {
            client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );
        }

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( html );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        return page;
    }


    /**
     * Assert that the specified object is null.
     * @param object The object to check.
     */
    public static void assertNull( final Object object ) {
        if( object != null ) {
            throw new AssertionFailedError("Expected null but found ["+object+"]");
        }
    }
}

