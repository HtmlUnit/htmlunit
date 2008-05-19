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
package com.gargoylesoftware.htmlunit.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XmlPage}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class XmlPageTest extends WebTestCase {

    /**
     * Tests namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void testNamespace() throws Exception {
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
    public void testValidDocument() throws Exception {
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
     * Utility method to test XML page of different MIME types.
     * @param content the XML content
     * @param mimeType the MIME type
     * @return the page returned by the WebClient
     * @throws Exception if a problem occurs
     */
    private XmlPage testXmlDocument(final String content, final String mimeType) throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(content, 200, "OK", mimeType);
        client.setWebConnection(webConnection);
        final Page page = client.getPage(URL_FIRST);
        assertEquals(URL_FIRST, page.getWebResponse().getUrl());
        assertEquals("OK", page.getWebResponse().getStatusMessage());
        assertEquals(HttpStatus.SC_OK, page.getWebResponse().getStatusCode());
        assertEquals(mimeType, page.getWebResponse().getContentType());
        assertTrue(XmlPage.class.isInstance(page));
        final XmlPage xmlPage = (XmlPage) page;
        assertEquals(content, xmlPage.getContent());
        Assert.assertNotNull(xmlPage.getXmlDocument());
        return xmlPage;
    }

    /**
     * Tests a simple invalid (badly formed) XML document.
     * @throws Exception if the test fails
     */
    @Test
    public void testInvalidDocument() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

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
        assertEquals(URL_FIRST, page.getWebResponse().getUrl());
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
    public void testVoiceXML() throws Exception {
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
    public void testLoad_XMLComment() throws Exception {
        final URL firstURL = new URL("http://htmlunit/first.html");
        final URL secondURL = new URL("http://htmlunit/second.xml");
        
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + "second.xml" + "'));\n"
            + "    alert(doc.documentElement.childNodes[0].nodeType);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        
        final String xml = "<test><!-- --></test>";

        final String[] expectedAlerts = new String[] {"true", "8"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(firstURL, html);
        conn.setResponse(secondURL, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(firstURL);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testCreateElement() throws Exception {
        final String[] expectedAlerts;
        if (getBrowserVersion() == BrowserVersion.FIREFOX_2) {
            expectedAlerts = new String[] {"true", "14"};
        }
        else {
            expectedAlerts = new String[] {"true", "16"};
        }

        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.appendChild(doc.createElement('elementName'));\n"
            + "    var xml;\n"
            + "    if (window.ActiveXObject)\n"
            + "      xml = doc.xml;\n"
            + "    else\n"
            + "      xml = new XMLSerializer().serializeToString(doc.documentElement);\n"
            + "    alert(xml.indexOf('<elementName/>') != -1);\n"
            + "    alert(xml.length);"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getBrowserVersion(), content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
