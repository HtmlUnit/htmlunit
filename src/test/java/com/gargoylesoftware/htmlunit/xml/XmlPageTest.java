/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.xml;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.ByteOrderMark;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.TextUtils;

/**
 * Tests for {@link XmlPage}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XmlPageTest extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        asText("<msg>abc</msg>", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextOnlyText() throws Exception {
        final StringWebResponse response = new StringWebResponse("<msg>abc</msg>", new URL("http://www.test.com"));
        final XmlPage xmlPage = new XmlPage(response, getWebClient().getCurrentWindow());

        assertEquals("abc", ((DomText) xmlPage.getFirstByXPath("/msg/text()")).asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextComplex() throws Exception {
        final String xml
                = "<msg>1"
                + "<h1>h1</h1>"
                + "<h2>h2"
                + "<h3>h3</h3>"
                + "<h3></h3>"
                + "txt"
                + "</h2>o"
                + "</msg>";
        asText(xml, "1h1h2h3txto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextPart() throws Exception {
        final String xml
                = "<outer>outer"
                + "<msg><h1>h1</h1></msg>"
                + "xy</outer>";
        asText(xml, "h1");
    }

    /**
     * Test for issue #1817.
     * @throws Exception if the test fails
     */
    @Test
    public void asTextEmpty() throws Exception {
        asText("<msg></msg>", "");
    }

    private void asText(final String xml, final String expected) throws Exception {
        final StringWebResponse response = new StringWebResponse(xml, new URL("http://www.test.com"));
        final XmlPage xmlPage = new XmlPage(response, getWebClient().getCurrentWindow());

        final DomElement msg = (DomElement) xmlPage.getFirstByXPath("//msg");
        assertNotNull("No element found by XPath '//msg'", msg);
        assertEquals(expected, msg.asText());
    }

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

        final XmlPage xmlPage = testDocument(content, MimeType.TEXT_XML);
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
             + "  <foofoo name='first'>something</foofoo>\n"
             + "  <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        final XmlPage xmlPage = testDocument(content, MimeType.TEXT_XML);
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
             + "\u0434\n"
             + "</foo>";

        final byte[] bytes = TextUtils.stringToByteArray(content, UTF_8);

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(bytes, 200, "OK", MimeType.TEXT_XML);
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertTrue(XmlPage.class.isInstance(page));

        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getWebResponse().getContentAsString());
        assertNotNull(xmlPage.getXmlDocument());

        assertEquals("foo", xmlPage.getXmlDocument().getFirstChild().getNodeName());
    }

    /**
     * Utility method to test XML page of different MIME types.
     * @param content the XML content
     * @param mimeType the MIME type
     * @return the page returned by the WebClient
     * @throws Exception if a problem occurs
     */
    private XmlPage testDocument(final String content, final String mimeType) throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content, 200, "OK", mimeType);
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals(mimeType, page.getWebResponse().getContentType());
        assertTrue(XmlPage.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getWebResponse().getContentAsString());
        assertNotNull(xmlPage.getXmlDocument());
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
            + "  <foofoo invalid\n"
            + "  <foofoo name='first'>something</foofoo>\n"
            + "  <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        webConnection.setDefaultResponse(content, 200, "OK", MimeType.TEXT_XML);
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals(MimeType.TEXT_XML, page.getWebResponse().getContentType());

        assertTrue(Page.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getWebResponse().getContentAsString());
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

        webConnection.setDefaultResponse(content, 200, "OK", MimeType.TEXT_XML);
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals(MimeType.TEXT_XML, page.getWebResponse().getContentType());

        assertTrue(Page.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getWebResponse().getContentAsString());
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

        webConnection.setDefaultResponse(content, 200, "OK", MimeType.TEXT_XML);
        client.setWebConnection(webConnection);

        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals("text/xml", page.getWebResponse().getContentType());

        assertTrue(Page.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getWebResponse().getContentAsString());
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

        final XmlPage xmlPage = testDocument(content, "application/voicexml+xml");
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
             + "  <foofoo name='first'>something</foofoo>\n"
             + "  <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";
        final XmlPage xmlPage = testDocument(html, MimeType.TEXT_XML);
        assertEquals(1, xmlPage.getByXPath("//foofoo[@name='first']").size());
    }

    /**
     * Test for issue #1820.
     * @throws Exception if the test fails
     */
    @Test
    public void xpathAttribute() throws Exception {
        final String html
            = "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "  <MARKGR INTREGN=\"1289218\" BILING=\"Y\" OOCD=\"CH\" INTREGD=\"20160111\">\n"
             + "  </MARKGR>\n"
             + "</foo>";
        final XmlPage xmlPage = testDocument(html, MimeType.TEXT_XML);

        assertEquals(1, xmlPage.getByXPath("//MARKGR").size());
        assertNotNull(xmlPage.getFirstByXPath("//MARKGR"));

        assertEquals(0, xmlPage.getByXPath("//markgr").size());
        assertNull(xmlPage.getFirstByXPath("//markgr"));

        assertEquals(1, xmlPage.getByXPath("//MARKGR/@INTREGN").size());
        assertTrue(xmlPage.getFirstByXPath("//MARKGR/@INTREGN") instanceof DomAttr);

        assertEquals(0, xmlPage.getByXPath("//MARKGR/@intregn").size());
        assertNull(xmlPage.getFirstByXPath("//MARKGR/@intregn"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void noResponse() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test", NoResponseServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();
        client.getPage(URL_FIRST + "test");
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
            response.setContentType(MimeType.TEXT_XML);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void bom() throws Exception {
        asText(new String(ByteOrderMark.UTF_8.getBytes()) + "<msg>abc</msg>", "abc");
    }

}
