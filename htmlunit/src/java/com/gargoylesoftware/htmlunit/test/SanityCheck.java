/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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

    public SanityCheck( final String name ) {
        super( name );
    }


    public static void main( final String args[] ) {
        com.gargoylesoftware.htmlunit.test.MainTestSuite.enableAllLogging();
        TestRunner.run( SanityCheck.class );
        System.exit( 0 );
    }


    public void testYahooMail() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "http://mail.yahoo.com/" ) );
    }


    public void testYahoo() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "http://yahoo.com/" ) );
    }


    public void testIBM() throws Exception {
        final WebClient webClient = new WebClient();
        webClient.setRedirectEnabled( true );
        final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "http://www.ibm.com/" ) );
        assertEquals( "http://www.ibm.com/us/", page.getWebResponse().getUrl().toExternalForm() );
    }


    public void testAlphaWorks() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = (HtmlPage)webClient.getPage(
            new URL( "http://www.alphaworks.ibm.com" ) );
    }


    public void testCNN() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "http://www.cnn.com" ) );
    }


    public void testToyotaCanada() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "http://www.toyota.ca" ) );
    }


    public void testSourceForge_secure() throws Exception {
        final WebClient webClient = new WebClient();
        webClient.setPrintContentOnFailingStatusCode(true);
        final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "https://sourceforge.net/projects/htmlunit/" ) );
    }


    public void testYahooLogin_secure() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = (HtmlPage)webClient.getPage( new URL( "https://login.yahoo.com/" ) );
        final HtmlForm form = page.getFormByName("login_form");
    }


    public void setUp() {
        System.out.println();
        System.out.println( "=====================================" );
        System.out.println( "==  Starting test: " + getName() );
        System.out.println( "=====================================" );
    }
}

