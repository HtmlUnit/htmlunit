/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;

import junit.framework.TestCase;

import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;

/**
 * test driver for the new HTMLParser implementation
 *
 * @version  $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 */
public class HTMLParserTest extends TestCase {

    /**
     * test the new HTMLParser on a simple HTML string and use the Jaxen XPath navigator
     * to validate results
     *
     * @throws Exception failure
     */
    public void testSimpleHTMLString() throws Exception {

        WebClient webClient = new WebClient();
        WebResponse webResponse = new StringWebResponse(
            "<html><head><title>TITLE</title><noscript>TEST</noscript></head><body></body></html>");

        HtmlPage page = HTMLParser.parse(webResponse, webClient.getCurrentWindow());

        HtmlUnitXPath xpath = new HtmlUnitXPath("//noscript");
        String stringVal = xpath.stringValueOf(page);

        assertEquals("TEST", stringVal);

        xpath = new HtmlUnitXPath("//*[./text() = 'TEST']");
        HtmlElement node = (HtmlElement)xpath.selectSingleNode(page);

        assertEquals(node.getTagName(), HtmlNoScript.TAG_NAME);
    }

    /**
     * test the new HTMLParser by accessing the HtmlUnit home page and detecting the copyright
     * string.
     *
     * @throws Exception failure
     */
    public static void testHtmlUnitHomePage() throws Exception {

        URL htmlUnitSite = new URL("http://htmlunit.sourceforge.net");
        try {
            URLConnection connection = htmlUnitSite.openConnection();
            connection.connect();
        }
        catch (ConnectException e) {
            /* sf.net's flaky web servers and not being able to connect
             * here from the shell server can cause this, doesn't mean something
             * is broken 
             */
            System.out.println("Connection could not be made to " + htmlUnitSite.toExternalForm());
            return; 
        }
        
        WebClient webClient = new WebClient();
        WebResponse webResponse = new HttpWebConnection(webClient).getResponse(
                htmlUnitSite,
                SubmitMethod.GET,
                Collections.EMPTY_LIST,
                Collections.EMPTY_MAP
        );

        HtmlPage page = HTMLParser.parse(webResponse, webClient.getCurrentWindow());

        //find the copyright string
        HtmlUnitXPath xpath = new HtmlUnitXPath("//div[@id='footer']/table/tr[2]/td");
        String stringVal = xpath.stringValueOf(page).trim();
        assertEquals("\u00A9 2002-2004, Gargoyle Software Inc.", stringVal);

        //see if the Google adds were added via Javascript
        xpath = new HtmlUnitXPath("//iframe[@name = 'google_ads_frame']");
        HtmlInlineFrame inline = (HtmlInlineFrame)xpath.selectSingleNode(page);

        assertNotNull("find Google ads", inline);

        HtmlPage innerPage = (HtmlPage)inline.getEnclosedPage();
        assertNotNull(innerPage);
    }
}
