/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SimpleCredentialProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.MalformedURLException;
import java.net.URL;
import junit.textui.TestRunner;

/**
 *  This class runs live tests against popular webservers just to make sure
 *  nothing obvious has broken. Don't run this very often lest we piss off the
 *  web masters.
 *
 * @version    $Revision$
 * @author     <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class SanityCheck extends WebTestCase {
    private static final BrowserVersion browserVersion_ = BrowserVersion.MOZILLA_1_0;

    public SanityCheck( final String name ) {
        super( name );
    }


    public static void main( final String args[] ) {
        com.gargoylesoftware.htmlunit.test.MainTestSuite.enableAllLogging();
        TestRunner.run( SanityCheck.class );
        System.exit( 0 );
    }


    public void testAcceptance() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        webClient.setCredentialProvider( new SimpleCredentialProvider("ecpinten","password") );
        assertInstanceOf(webClient.getPage( new URL( "http://tcisuacpt01.toyota.ca/ecp/index.html" ) ), HtmlPage.class);
    }


    public void testTest() throws Exception {
        final WebClient webClient = new WebClient(browserVersion_);
        webClient.setCredentialProvider( new SimpleCredentialProvider("ecpinten","password") );
        assertInstanceOf(webClient.getPage( new URL( "http://mbecp.toyota.ca:9090/ecp/index.html" ) ), HtmlPage.class);
    }


    public void setUp() {
        System.out.println();
        System.out.println( "=====================================" );
        System.out.println( "==  Starting test: " + getName() );
        System.out.println( "=====================================" );
    }
}

