/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *  Tests that whack on a live web server.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class LiveServerTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public LiveServerTest( final String name ) {
        super( name );
    }


    private URL getPrintEnvUrl() throws MalformedURLException {
        return new URL("http://htmlunit.sourceforge.net/cgi-bin/printenv");
    }


    public static Test suite() {
        return new TestSuite();
    }


    public void testGetMethodWithParameters() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage firstPage = (HtmlPage)webClient.getPage(getPrintEnvUrl());

        assertEquals("GET", firstPage.getHtmlElementById("REQUEST_METHOD").asText());

        final HtmlForm form = firstPage.getFormByName("form1");
        assertEquals("get", form.getMethodAttribute());

        final HtmlSubmitInput button = (HtmlSubmitInput)form.getInputByName("button1");
        final HtmlPage secondPage = (HtmlPage)button.submit();
        assertEquals("GET", secondPage.getHtmlElementById("REQUEST_METHOD").asText());
        assertEquals("textfield1=*&button1=PushMe",
            secondPage.getHtmlElementById("QUERY_STRING").asText());
        assertEquals("", secondPage.getHtmlElementById("CONTENT").asText());
    }


    public void testPostMethodWithParameters() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage firstPage = (HtmlPage)webClient.getPage(getPrintEnvUrl());

        assertEquals("GET", firstPage.getHtmlElementById("REQUEST_METHOD").asText());

        final HtmlForm form = firstPage.getFormByName("form1");
        form.setMethodAttribute("post");

        final HtmlSubmitInput button = (HtmlSubmitInput)form.getInputByName("button1");
        final HtmlPage secondPage = (HtmlPage)button.submit();
        assertEquals("POST", secondPage.getHtmlElementById("REQUEST_METHOD").asText());
        assertEquals("", secondPage.getHtmlElementById("QUERY_STRING").asText());
        assertEquals("textfield1=*&button1=PushMe",
            secondPage.getHtmlElementById("CONTENT").asText());
    }
}

