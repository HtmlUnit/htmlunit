/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
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

public class WebTestCase extends BaseTestCase {
    public WebTestCase( final String name ) {
        super( name );
    }


    /**
     * @param  label
     * @param  clazz
     * @param  object
     */
    public void assertInstanceOf( final String label, final Object object, final Class clazz ) {
        if( clazz.isAssignableFrom( object.getClass() ) == false ) {
            fail( label + ": object [" + object + "] is not an instance of class ["
                + clazz.getName() + "]" );
        }
    }


    public void assertInstanceOf( final Object object, final Class clazz ) {
        if( clazz.isAssignableFrom( object.getClass() ) == false ) {
            fail("object ["+object+"] is not an instance of class ["+clazz.getName()+"]" );
        }
    }


    /**
     * @exception  Throwable
     */
//    public void runBare()
//        throws Throwable {
//        final long startTime = System.currentTimeMillis();
//        super.runBare();
//        final long endTime = System.currentTimeMillis();
//    }


    protected final HtmlPage loadPage( final String html ) throws Exception {
        return loadPage(html, null);
    }


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


    public static void assertNull( final Object object ) {
        if( object != null ) {
            throw new AssertionFailedError("Expected null but found ["+object+"]");
        }
    }
}

