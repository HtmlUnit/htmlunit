/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * This class runs live tests against popular webservers just to make sure
 * nothing obvious has broken. Don't run this very often lest we piss off the
 * web masters.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public final class SanityCheck {

    private static final BrowserVersion BROWSER_VERSION = BrowserVersion.FIREFOX_2;

    /**
     * Main entry point for testing.
     * @param args the arguments
     * @throws Exception if a problem occurs
     */
    public static void main(final String[] args) throws Exception {
        final SanityCheck test = new SanityCheck();
        test.yahooMail();
        test.yahoo();
        test.yahoo_Spanish();
        test.ibm();
        test.alphaWorks();
        test.cnn();
        test.toyotaCanada();
        test.sourceForge_secure();
        test.htmlUnitHomepage();
        test.adobeAcrobatReaderDownloadStep2();
        test.getMethodWithParameters();
        test.postMethodWithDuplicateParameters();
        test.postMethodWithParameters();
        test.yahooLogin_secure();
        test.amazonCanada();
        test.cnnAfterHours();
    }

    private SanityCheck() {
    }

    /**
     * Test against a live server: Yahoo mail
     * @throws Exception if something goes wrong
     */
    private void yahooMail() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://mail.yahoo.com/")));
    }

    /**
     * Test against a live server: Yahoo
     * @throws Exception if something goes wrong
     */
    private void yahoo() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://yahoo.com/")));
    }

    /**
     * Test against a live server: Yahoo
     * @throws Exception if something goes wrong
     */
    private void yahoo_Spanish() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://edit.europe.yahoo.com/config/mail?.intl=es")));
    }

    /**
     * Test against a live server: IBM
     * @throws Exception if something goes wrong
     */
    private void ibm() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        webClient.setRedirectEnabled(true);
        final HtmlPage page = webClient.getPage("http://www.ibm.com/");
        assertEquals("http://www.ibm.com/us/", page.getWebResponse().getRequestUrl());
    }

    /**
     * Test against a live server: IBM Alphaworks
     * @throws Exception if something goes wrong
     */
    private void alphaWorks() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://www.alphaworks.ibm.com")));
    }

    /**
     * Test against a live server: CNN
     * @throws Exception if something goes wrong
     */
    private void cnn() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://www.cnn.com")));
    }

    /**
     * Test against a live server: Toyota Canada
     * @throws Exception if something goes wrong
     */
    private void toyotaCanada() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://www.toyota.ca")));
    }

    /**
     * Test against a live server: HtmlUnit page on sourceforge using HTTPS
     * @throws Exception if something goes wrong
     */
    private void sourceForge_secure() throws Exception {
        try {
            final WebClient webClient = new WebClient(BROWSER_VERSION);
            webClient.setPrintContentOnFailingStatusCode(true);
            assertTrue(HtmlPage.class.isInstance(webClient.getPage("https://sourceforge.net/projects/htmlunit/")));
        }
        catch (final MalformedURLException e) {
            System.out.println("Skipping HTTPS test: testSourceForge_secure");
        }
    }

    /**
     * Test against a live server: Yahoo secure login
     * @throws Exception if something goes wrong
     */
    private void yahooLogin_secure() throws Exception {
        try {
            final WebClient webClient = new WebClient(BROWSER_VERSION);
            final HtmlPage page = webClient.getPage("https://login.yahoo.com/");
            final HtmlForm form = page.getFormByName("login_form");
            assertNotNull(form);
        }
        catch (final MalformedURLException e) {
            System.out.println("Skipping HTTPS test: testSourceForge_secure");
        }
    }

    /**
     * Test against a live server: Amazon Canada
     * @throws Exception if something goes wrong
     */
    private void amazonCanada() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://www.amazon.ca/")));
    }

    /**
     * Test against a live server: CNN After hours
     * @throws Exception if something goes wrong
     */
    private void cnnAfterHours() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://money.cnn.com/markets/afterhours/")));
    }

    /**
     * Test against a live server: htmlunit.sourceforge.net
     * @throws Exception if something goes wrong
     */
    private void htmlUnitHomepage() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://htmlunit.sourceforge.net")));
    }

    /**
     * Test against a live server: Adobe Acrobat Reader download step 2
     * @throws Exception if something goes wrong
     */
    private void adobeAcrobatReaderDownloadStep2() throws Exception {
        final WebClient webClient = new WebClient();
        assertTrue(HtmlPage.class.isInstance(
            webClient.getPage("http://www.adobe.com/products/acrobat/readstep2.html")));
    }

    private String getPrintEnvUrl() {
        return "http://htmlunit.sourceforge.net/cgi-bin/printenv";
    }

    /**
     * Test against htmlunit.sourceforge.net to make sure parameters are being passed
     * correctly for GET methods
     * @throws Exception if something goes wrong
     */
    private void getMethodWithParameters() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage firstPage = webClient.getPage(getPrintEnvUrl());

        assertEquals("GET", firstPage.<HtmlElement>getHtmlElementById("REQUEST_METHOD").asText());

        final HtmlForm form = firstPage.getFormByName("form1");
        assertEquals("get", form.getMethodAttribute());

        final HtmlSubmitInput button = form.getInputByName("button1");
        final HtmlPage secondPage = button.click();
        assertEquals("GET", secondPage.<HtmlElement>getHtmlElementById("REQUEST_METHOD").asText());
        assertEquals("textfield1=*&button1=PushMe",
            secondPage.<HtmlElement>getHtmlElementById("QUERY_STRING").asText());
        assertEquals("", secondPage.<HtmlElement>getHtmlElementById("CONTENT").asText());
    }

    /**
     * Test against htmlunit.sourceforge.net to make sure parameters are being passed correctly for POST methods.
     * @throws Exception if something goes wrong
     */
    private void postMethodWithDuplicateParameters() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage firstPage = webClient.getPage(getPrintEnvUrl());

        assertEquals("GET", firstPage.<HtmlElement>getHtmlElementById("REQUEST_METHOD").asText());

        final HtmlForm form = firstPage.getFormByName("form1");
        form.setMethodAttribute("post");

        final HtmlSubmitInput button = form.getInputByName("button1");
        button.setAttributeValue("name", "textfield1");

        final HtmlPage secondPage = button.click();
        assertEquals("POST", secondPage.<HtmlElement>getHtmlElementById("REQUEST_METHOD").asText());
        assertEquals("", secondPage.<HtmlElement>getHtmlElementById("QUERY_STRING").asText());
        assertEquals("textfield1=*&textfield1=PushMe",
            secondPage.<HtmlElement>getHtmlElementById("CONTENT").asText());
    }

    /**
     * Test against htmlunit.sourceforge.net to make sure parameters are being passed
     * correctly for POST methods.
     * @throws Exception if something goes wrong
     */
    private void postMethodWithParameters() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage firstPage = webClient.getPage(getPrintEnvUrl());

        assertEquals("GET", firstPage.<HtmlElement>getHtmlElementById("REQUEST_METHOD").asText());

        final HtmlForm form = firstPage.getFormByName("form1");
        form.setMethodAttribute("post");

        final HtmlSubmitInput button = form.getInputByName("button1");
        final HtmlPage secondPage = button.click();
        assertEquals("POST", secondPage.<HtmlElement>getHtmlElementById("REQUEST_METHOD").asText());
        assertEquals("", secondPage.<HtmlElement>getHtmlElementById("QUERY_STRING").asText());
        assertEquals("textfield1=*&button1=PushMe", secondPage.<HtmlElement>getHtmlElementById("CONTENT").asText());
    }
}
