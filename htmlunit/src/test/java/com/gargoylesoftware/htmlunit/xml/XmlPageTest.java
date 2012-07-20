/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for {@link XmlPage}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XmlPageTest extends WebServerTestCase {

    /**
     * Tests namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void namespace() throws Exception {
        final String content
            = "<?xml version='1.0'?>\n"
            + "<RDF xmlns='http://www.w3.org/1999/02/22-rdf-syntax-ns#' "
            + "xmlns:em='http://www.mozilla.org/2004/em-rdf#'>"
            + "<Description about='urn:mozilla:install-manifest'>"
            + "<em:name>My Plugin</em:name>"
            + "</Description>\n"
            + "</RDF>";

        final XmlPage xmlPage = testXmlDocument(content, "text/xml");
        final Node node = xmlPage.getXmlDocument().getFirstChild().getFirstChild().getFirstChild();
        assertEquals("em:name", node.getNodeName());
        assertEquals("name", node.getLocalName());
        assertEquals("http://www.mozilla.org/2004/em-rdf#", node.getNamespaceURI());
    }

    /**
     * Tests a simple valid XML document.
     * @throws Exception if the test fails
     */
    @Test
    public void validDocument() throws Exception {
        final String content
            = "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        final XmlPage xmlPage = testXmlDocument(content, "text/xml");
        assertEquals("foo", xmlPage.getXmlDocument().getFirstChild().getNodeName());
    }

    /**
     * Test that UTF-8 is used as default encoding for xml responses
     * (issue 3385410).
     * @throws Exception if the test fails
     */
    @Test
    public void defaultEncoding() throws Exception {
        final String content
            = "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             +   "\u0434\n"
             + "</foo>";

        final byte[] bytes = TextUtil.stringToByteArray(content, "UTF8");

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(bytes, 200, "OK", "text/xml");
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertTrue(XmlPage.class.isInstance(page));

        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        Assert.assertNotNull(xmlPage.getXmlDocument());

        assertEquals("foo", xmlPage.getXmlDocument().getFirstChild().getNodeName());
    }

    /**
     * Utility method to test XML page of different MIME types.
     * @param content the XML content
     * @param mimeType the MIME type
     * @return the page returned by the WebClient
     * @throws Exception if a problem occurs
     */
    private XmlPage testXmlDocument(final String content, final String mimeType) throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content, 200, "OK", mimeType);
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getWebRequest().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals(mimeType, page.getWebResponse().getContentType());
        assertTrue(XmlPage.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        Assert.assertEquals(content, xmlPage.getContent());
        Assert.assertNotNull(xmlPage.getXmlDocument());
        return xmlPage;
    }

    /**
     * Tests a simple invalid (badly formed) XML document.
     * @throws Exception if the test fails
     */
    @Test
    public void invalidDocument() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String content
            = "<?xml version=\"1.0\"?>\n"
            + "<foo>\n"
            + "    <foofoo invalid\n"
            + "    <foofoo name='first'>something</foofoo>\n"
            + "    <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        webConnection.setDefaultResponse(content, 200, "OK", "text/xml");
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getWebRequest().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals("text/xml", page.getWebResponse().getContentType());

        assertTrue(Page.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        assertNull(xmlPage.getXmlDocument());
    }

    /**
     * Tests a simple empty XML document.
     * @throws Exception if the test fails
     */
    @Test
    public void emptyDocument() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String content = "";

        webConnection.setDefaultResponse(content, 200, "OK", "text/xml");
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getWebRequest().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals("text/xml", page.getWebResponse().getContentType());

        assertTrue(Page.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        assertNull(xmlPage.getXmlDocument());
    }

    /**
     * Tests a simple empty XML document.
     * @throws Exception if the test fails
     */
    @Test
    public void blankDocument() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String content = "\t  \n\r\n";

        webConnection.setDefaultResponse(content, 200, "OK", "text/xml");
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getWebRequest().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals("text/xml", page.getWebResponse().getContentType());

        assertTrue(Page.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        assertNull(xmlPage.getXmlDocument());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void voiceXML() throws Exception {
        final String content =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<vxml xmlns=\"http://www.w3.org/2001/vxml\""
            + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + "  xsi:schemaLocation=\"http://www.w3.org/2001/vxml "
            + "   http://www.w3.org/TR/voicexml20/vxml.xsd\""
            + "   version=\"2.0\">\n"
            + "  <form>\n"
            + "    <block>Hello World!</block>\n"
            + "  </form>\n"
            + "</vxml>";

        final XmlPage xmlPage = testXmlDocument(content, "application/voicexml+xml");
        assertEquals("vxml", xmlPage.getXmlDocument().getFirstChild().getNodeName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void xpath() throws Exception {
        final String html
            = "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";
        final XmlPage xmlPage = testXmlDocument(html, "text/xml");
        assertEquals(1, xmlPage.getByXPath("//foofoo[@name='first']").size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void noResponse() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test", NoResponseServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        client.getPage("http://localhost:" + PORT + "/test");
    }

    /**
     * Servlet for {@link #noResponse()}.
     */
    public static class NoResponseServlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/xml");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
