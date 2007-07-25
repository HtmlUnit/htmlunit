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
package com.gargoylesoftware.htmlunit.xml;

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XmlPage}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class XmlPageTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public XmlPageTest(final String name) {
        super(name);
    }

    /**
     * Tests namespace
     * @throws Exception if the test fails
     */
    public void testNamespace() throws Exception {
        final String content
            = "<?xml version='1.0'?>\n"
            + "<RDF xmlns='http://www.w3.org/1999/02/22-rdf-syntax-ns#' "
            + "xmlns:em='http://www.mozilla.org/2004/em-rdf#'>"
            + "<Description about='urn:mozilla:install-manifest'>"
            + "<em:name>My Plugin</em:name>\n"
            + "</Description>\n"
            + "</RDF>\n";

        final XmlPage xmlPage = testXmlDocument(content, "text/xml");
        final Node node = xmlPage.getXmlDocument().getFirstChild().getFirstChild().getFirstChild();
        assertEquals("em:name", node.getNodeName());
        assertEquals("name", node.getLocalName());
        assertEquals("http://www.mozilla.org/2004/em-rdf#", node.getNamespaceURI());
    }

    /**
     * Tests a simple valid xml document
     * @throws Exception if the test fails
     */
    public void testValidDocument() throws Exception {
        final String content
            = "<?xml version=\"1.0\"?>"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        final XmlPage xmlPage = testXmlDocument(content, "text/xml");
        assertEquals("foo", xmlPage.getXmlDocument().getFirstChild().getNodeName());
    }

    /**
     * Utility method to test xml page of different mime types
     * @param content the xml content
     * @param mimeType The mime type
     * @return the page returned by the WebClient
     * @throws Exception if a problem occurs
     */
    private XmlPage testXmlDocument(final String content, final String mimeType) throws Exception
    {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(content, 200, "OK", mimeType);
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(200, page.getWebResponse().getStatusCode());
        assertEquals(mimeType, page.getWebResponse().getContentType());
        assertInstanceOf(page, XmlPage.class);
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        assertNotNull(xmlPage.getXmlDocument());
        return xmlPage;
    }

    /**
     * Tests a simple invalid (badly formed) xml document
     * @throws Exception if the test fails
     */
    public void testInvalidDocument() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String content
            = "<?xml version=\"1.0\"?>"
            + "<foo>\n"
            + "    <foofoo invalid\n"
            + "    <foofoo name='first'>something</foofoo>\n"
            + "    <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        webConnection.setDefaultResponse(content, 200, "OK", "text/xml");
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(200, page.getWebResponse().getStatusCode());
        assertEquals("text/xml", page.getWebResponse().getContentType());

        assertInstanceOf(page, XmlPage.class);
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        assertNull(xmlPage.getXmlDocument());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testVoiceXML() throws Exception {
        final String content =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<vxml xmlns=\"http://www.w3.org/2001/vxml\""
            + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + "  xsi:schemaLocation=\"http://www.w3.org/2001/vxml "
            + "   http://www.w3.org/TR/voicexml20/vxml.xsd\""
            + "   version=\"2.0\">"
            + "  <form>"
            + "    <block>Hello World!</block>"
            + "  </form>"
            + "</vxml>";

        final XmlPage xmlPage = testXmlDocument(content, "application/voicexml+xml");
        assertEquals("vxml", xmlPage.getXmlDocument().getFirstChild().getNodeName());
    }
}
