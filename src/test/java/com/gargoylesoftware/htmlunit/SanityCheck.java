/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

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
public class SanityCheck extends WebTestCase2 {

    private static final BrowserVersion BROWSER_VERSION = BrowserVersion.FIREFOX_2;

    /**
     * Test against a live server: Yahoo mail
     * @throws Exception If something goes wrong.
     */
    public void testYahooMail() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://mail.yahoo.com/")));
    }

    /**
     * Test against a live server: Yahoo
     * @throws Exception If something goes wrong.
     */
    public void testYahoo() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://yahoo.com/")));
    }

    /**
     * Test against a live server: Yahoo
     * @throws Exception If something goes wrong.
     */
    public void testYahoo_Spanish() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://edit.europe.yahoo.com/config/mail?.intl=es")));
    }

    /**
     * Test against a live server: IBM
     * @throws Exception If something goes wrong.
     */
    public void testIBM() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        webClient.setRedirectEnabled(true);
        final HtmlPage page = (HtmlPage) webClient.getPage("http://www.ibm.com/");
        assertEquals("http://www.ibm.com/us/", page.getWebResponse().getUrl());
    }

    /**
     * Test against a live server: IBM Alphaworks
     * @throws Exception If something goes wrong.
     */
    public void testAlphaWorks() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://www.alphaworks.ibm.com")));
    }

    /**
     * Test against a live server: CNN
     * @throws Exception If something goes wrong.
     */
    public void testCNN() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://www.cnn.com")));
    }

    /**
     * Test against a live server: Toyota Canada
     * @throws Exception If something goes wrong.
     */
    public void testToyotaCanada() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://www.toyota.ca")));
    }

    /**
     * Test against a live server: HtmlUnit page on sourceforge using https
     * @throws Exception If something goes wrong.
     */
    public void testSourceForge_secure() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        webClient.setPrintContentOnFailingStatusCode(true);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("https://sourceforge.net/projects/htmlunit/")));
    }

    /**
     * Test against a live server: Yahoo secure login
     * @throws Exception If something goes wrong.
     */
    public void testYahooLogin_secure() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        final HtmlPage page = (HtmlPage) webClient.getPage("https://login.yahoo.com/");
        final HtmlForm form = page.getFormByName("login_form");
        assertNotNull(form);
    }

    /**
     * Test against a live server: Amazon Canada
     * @throws Exception If something goes wrong.
     */
    public void testAmazonCanada() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://www.amazon.ca/")));
    }

    /**
     * Test against a live server: CNN After hours
     * @throws Exception If something goes wrong.
     */
    public void testCnnAfterHours() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://money.cnn.com/markets/afterhours/")));
    }

    /**
     * Test against a live server: htmlunit.sourceforge.net
     * @throws Exception If something goes wrong.
     */
    public void testHtmlUnitHomepage() throws Exception {
        final WebClient webClient = new WebClient(BROWSER_VERSION);
        assertTrue(HtmlPage.class.isInstance(webClient.getPage("http://htmlunit.sourceforge.net")));
    }

    /**
     * Test against a live server: Adobe Acrobat Reader download step 2
     * @throws Exception If something goes wrong.
     */
    public void testAdobeAcrobatReaderDownloadStep2() throws Exception {
        final WebClient webClient = new WebClient();
        assertTrue(HtmlPage.class.isInstance(
            webClient.getPage("http://www.adobe.com/products/acrobat/readstep2.html")));
    }

    private String getPrintEnvUrl() throws MalformedURLException {
        return "http://htmlunit.sourceforge.net/cgi-bin/printenv";
    }

    /**
     * Test against htmlunit.sourceforge.net to make sure parameters are being passed
     * correctly for GET methods
     * @throws Exception If something goes wrong.
     */
    public void testGetMethodWithParameters() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage firstPage = (HtmlPage) webClient.getPage(getPrintEnvUrl());

        assertEquals("GET", firstPage.getHtmlElementById("REQUEST_METHOD").asText());

        final HtmlForm form = firstPage.getFormByName("form1");
        assertEquals("get", form.getMethodAttribute());

        final HtmlSubmitInput button = (HtmlSubmitInput) form.getInputByName("button1");
        final HtmlPage secondPage = (HtmlPage) button.click();
        assertEquals("GET", secondPage.getHtmlElementById("REQUEST_METHOD").asText());
        assertEquals("textfield1=*&button1=PushMe", secondPage.getHtmlElementById("QUERY_STRING").asText());
        assertEquals("", secondPage.getHtmlElementById("CONTENT").asText());
    }

    /**
     * Test against htmlunit.sourceforge.net to make sure parameters are being passed
     * correctly for POST methods.
     * @throws Exception If something goes wrong.
     */
    public void testPostMethodWithDuplicateParameters() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage firstPage = (HtmlPage) webClient.getPage(getPrintEnvUrl());

        assertEquals("GET", firstPage.getHtmlElementById("REQUEST_METHOD").asText());

        final HtmlForm form = firstPage.getFormByName("form1");
        form.setMethodAttribute("post");

        final HtmlSubmitInput button = (HtmlSubmitInput) form.getInputByName("button1");
        button.setAttributeValue("name", "textfield1");

        final HtmlPage secondPage = (HtmlPage) button.click();
        assertEquals("POST", secondPage.getHtmlElementById("REQUEST_METHOD").asText());
        assertEquals("", secondPage.getHtmlElementById("QUERY_STRING").asText());
        assertEquals("textfield1=*&textfield1=PushMe", secondPage.getHtmlElementById("CONTENT").asText());
    }

    /**
     * Test against htmlunit.sourceforge.net to make sure parameters are being passed
     * correctly for POST methods.
     * @throws Exception If something goes wrong.
     */
    public void testPostMethodWithParameters() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage firstPage = (HtmlPage) webClient.getPage(getPrintEnvUrl());

        assertEquals("GET", firstPage.getHtmlElementById("REQUEST_METHOD").asText());

        final HtmlForm form = firstPage.getFormByName("form1");
        form.setMethodAttribute("post");

        final HtmlSubmitInput button = (HtmlSubmitInput) form.getInputByName("button1");
        final HtmlPage secondPage = (HtmlPage) button.click();
        assertEquals("POST", secondPage.getHtmlElementById("REQUEST_METHOD").asText());
        assertEquals("", secondPage.getHtmlElementById("QUERY_STRING").asText());
        assertEquals("textfield1=*&button1=PushMe", secondPage.getHtmlElementById("CONTENT").asText());
    }
}

