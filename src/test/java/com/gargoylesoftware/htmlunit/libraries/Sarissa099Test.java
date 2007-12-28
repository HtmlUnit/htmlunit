/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.libraries;

import java.util.List;

import org.mortbay.jetty.Server;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpWebConnectionTest;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for 0.9.9 version of <a href="http://sarissa.sourceforge.net">Sarissa</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class Sarissa099Test extends WebTestCase {

    private Server server_;

    /**
     * Creates an instance.
     *
     * @param name The name of the test.
     */
    public Sarissa099Test(final String name) {
        super(name);
    }

    /**
     * Returns the Sarissa version being tested.
     *
     * @return the Sarissa version being tested.
     */
    protected String getVersion() {
        return "0.9.9";
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testSarissa() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        test("SarissaTestCase");
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testXmlHttpRequest() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        test("XmlHttpRequestTestCase");
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testXMLSerializer() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        test("XMLSerializerTestCase");
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testDOMParser() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        test("DOMParserTestCase");
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testXMLDocument() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        test("XMLDocumentTestCase");
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testXMLElement() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        test("XMLElementTestCase");
    }

    /**
     * @throws Exception If an error occurs.
     */
    public void testXSLTProcessor() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        test("XSLTProcessorTestCase");
    }

    private void test(final String testName) throws Exception {
        server_ = HttpWebConnectionTest.startWebServer("src/test/resources/sarissa/" + getVersion());
        test(BrowserVersion.INTERNET_EXPLORER_7_0, testName);
        test(BrowserVersion.FIREFOX_2, testName);
    }

    private void test(final BrowserVersion browserVersion, final String testName) throws Exception {
        final WebClient client = new WebClient(browserVersion);

        final String url = "http://localhost:" + HttpWebConnectionTest.PORT + "/test/testsarissa.html";
        final HtmlPage page = (HtmlPage) client.getPage(url);
        final HtmlButton button = (HtmlButton) page.getFirstByXPath("//button");
        button.click();
        
        final List divList =
            page.getByXPath("//div[@class='placeholder']/a[@name='#" + testName + "']/../div[last()]");
        assertEquals(1, divList.size());
        final HtmlDivision div = (HtmlDivision) divList.get(0);
        assertEquals("OK!", div.asText());
    }

    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        HttpWebConnectionTest.stopWebServer(server_);
        server_ = null;
    }
}
