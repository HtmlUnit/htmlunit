/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlunit.MockWebConnection;
import org.htmlunit.StringWebResponse;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomElement;
import org.htmlunit.xml.XmlPage;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Unit tests for {@link XmlUtils}.
 *
 * @author Ronald Brill
 */
public class XmlUtilsTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buildDocument_nullResponse_returnsEmptyDocument() throws Exception {
        final Document document = XmlUtils.buildDocument(null);

        assertNotNull(document);
        assertNull(document.getDocumentElement());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buildDocument_simpleValidXml() throws Exception {
        final Document document = buildFrom("<root><child>text</child></root>");

        assertEquals("root", document.getDocumentElement().getTagName());
        assertEquals("child", document.getDocumentElement().getFirstChild().getNodeName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buildDocument_leadingWhitespaceBeforeRoot() throws Exception {
        final Document document = buildFrom("   \n\t  <root/>");

        assertEquals("root", document.getDocumentElement().getTagName());
    }

    /**
     * Blank (whitespace-only, or fully empty) content must not throw --
     * it must produce a fresh, empty document instead.
     * @throws Exception if the test fails
     */
    @Test
    public void buildDocument_blankContent_returnsEmptyDocumentInsteadOfThrowing() throws Exception {
        final Document document = buildFrom("   \n   \t  ");

        assertNotNull(document);
        assertNull(document.getDocumentElement());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buildDocument_emptyContent_returnsEmptyDocument() throws Exception {
        final Document document = buildFrom("");

        assertNotNull(document);
        assertNull(document.getDocumentElement());
    }

    /**
     * Genuinely malformed (non-blank) XML must still throw, not be silently
     * swallowed the way blank content is.
     */
    @Test
    public void buildDocument_malformedNonBlankXml_throwsSAXException() {
        assertThrows(SAXException.class, () -> buildFrom("<root><unclosed></root>"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buildDocument_malformedXml_completelyInvalid_throws() {
        assertThrows(SAXException.class, () -> buildFrom("not xml at all <<<"));
    }

    /**
     * A DOCTYPE with an external entity reference must not cause an attempt
     * to actually fetch the external resource -- the custom entity resolver
     * should return empty content instead.
     * @throws Exception if the test fails
     */
    @Test
    public void buildDocument_doesNotResolveExternalEntities() throws Exception {
        final String xml = "<!DOCTYPE root [<!ENTITY xxe SYSTEM \"http://192.0.2.1/should-not-be-fetched\">]>"
                + "<root>&xxe;</root>";

        // must not throw / hang / attempt network access
        final Document document = buildFrom(xml);
        assertNotNull(document);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buildDocument_namespaceAware() throws Exception {
        final Document document = buildFrom("<root xmlns:foo='http://example.com/foo'><foo:child/></root>");

        final Element child = (Element) document.getDocumentElement().getFirstChild();
        assertEquals("http://example.com/foo", child.getNamespaceURI());
        assertEquals("child", child.getLocalName());
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appendChild_elementsTextCommentsAndCData() throws Exception {
        final String xml = "<root>"
                + "<child attr='v'>text<!--a comment--><![CDATA[cdata content]]></child>"
                + "</root>";

        final XmlPage page = loadXmlPage(xml);
        final DomElement root = page.getDocumentElement();

        assertEquals("root", root.getTagName());
        final DomElement child = (DomElement) root.getFirstChild();
        assertEquals("child", child.getTagName());
        assertEquals("v", child.getAttribute("attr"));

        // text + comment + CDATA all present among the children
        assertTrue(child.asXml().contains("text"));
        assertTrue(child.asXml().contains("cdata content"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appendChild_processingInstruction() throws Exception {
        final String xml = "<root><?target data?></root>";

        final XmlPage page = loadXmlPage(xml);
        assertTrue(page.asXml().contains("<?target data?>"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appendChild_namespacedElementsAndAttributes() throws Exception {
        final String xml = "<root xmlns:ns='http://example.com/ns'>"
                + "<ns:child ns:attr='value'/>"
                + "</root>";

        final XmlPage page = loadXmlPage(xml);
        final DomElement root = page.getDocumentElement();
        final DomElement child = (DomElement) root.getFirstChild();

        assertEquals("ns:child", child.getNodeName());
        assertEquals("value", child.getAttribute("ns:attr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void appendChild_nestedElements_deepCopy() throws Exception {
        final String xml = "<a><b><c><d>deep</d></c></b></a>";

        final XmlPage page = loadXmlPage(xml);
        final DomElement a = page.getDocumentElement();
        final DomElement b = (DomElement) a.getFirstChild();
        final DomElement c = (DomElement) b.getFirstChild();
        final DomElement d = (DomElement) c.getFirstChild();

        assertEquals("d", d.getTagName());
        assertEquals("deep", d.getTextContent());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lookupNamespaceURI_declaredOnElementItself() throws Exception {
        final XmlPage page = loadXmlPage("<root xmlns:ns='http://example.com/ns'><child/></root>");
        final DomElement root = page.getDocumentElement();

        assertEquals("http://example.com/ns", XmlUtils.lookupNamespaceURI(root, "ns"));
    }

    /**
     * KEY CASE: a namespace declared on an ANCESTOR must be found when
     * looking up from a descendant -- this is the correct behavior
     * lookupNamespaceURI() already implements (contrast with lookupPrefix()
     * below, which does not).
     * @throws Exception if the test fails
     */
    @Test
    public void lookupNamespaceURI_declaredOnAncestor_foundFromDescendant() throws Exception {
        final XmlPage page = loadXmlPage(
                "<root xmlns:ns='http://example.com/ns'><child><grandchild/></child></root>");
        final DomElement root = page.getDocumentElement();
        final DomElement child = (DomElement) root.getFirstChild();
        final DomElement grandchild = (DomElement) child.getFirstChild();

        assertEquals("http://example.com/ns", XmlUtils.lookupNamespaceURI(grandchild, "ns"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lookupNamespaceURI_defaultNamespace_emptyPrefix() throws Exception {
        final XmlPage page = loadXmlPage("<root xmlns='http://example.com/default'><child/></root>");
        final DomElement root = page.getDocumentElement();

        assertEquals("http://example.com/default", XmlUtils.lookupNamespaceURI(root, ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lookupNamespaceURI_unknownPrefix_returnsNull() throws Exception {
        final XmlPage page = loadXmlPage("<root xmlns:ns='http://example.com/ns'/>");
        final DomElement root = page.getDocumentElement();

        assertNull(XmlUtils.lookupNamespaceURI(root, "doesNotExist"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void whitespaceTrackingReader_offsetZero_leadingWhitespaceSkippedCorrectly() throws Exception {
        try (final Reader reader = newTrackingReader("   X")) {
            final char[] cbuf = new char[10];
            Arrays.fill(cbuf, 'Z');

            final int result = reader.read(cbuf, 0, 10);

            assertEquals(1, result);
            assertEquals('X', cbuf[0]);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void whitespaceTrackingReader_nonZeroOffset_currentlyReturnsCorruptedData() throws Exception {
        // 3 spaces then 'Y'
        try(Reader reader = newTrackingReader("   Y")) {
            final char[] cbuf = new char[20];
            Arrays.fill(cbuf, 'Z'); // poison the whole buffer first

            final int off = 5;
            final int len = 10;
            final int result = reader.read(cbuf, off, len);

            assertEquals(1, result);
            assertEquals('Y', cbuf[off]);
        }
    }

    /**
     * A stream containing ONLY whitespace must report wasBlank() true and
     * never falsely flip to false.
     * @throws Exception if the test fails
     */
    @Test
    public void whitespaceTrackingReader_allWhitespace_staysBlank() throws Exception {
        try (final Reader reader = newTrackingReaderInstance("     ")) {
            final char[] cbuf = new char[10];

            int total = 0;
            int n;
            while ((n = reader.read(cbuf, 0, cbuf.length)) != -1) {
                total += n;
            }

            assertTrue(total >= 0);
            assertTrue((Boolean) invokeWasBlank(reader));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void whitespaceTrackingReader_nonBlankContent_reportsNotBlank() throws Exception {
        final Object tracker = newTrackingReaderInstance("X");
        try (Reader reader = (Reader) tracker) {
            final char[] cbuf = new char[10];
            reader.read(cbuf, 0, cbuf.length);

            assertTrue(!(Boolean) invokeWasBlank(tracker));
        }
    }

    private static Document buildFrom(final String xmlContent) throws IOException, SAXException,
            ParserConfigurationException {
        final URL url = new URL("http://xmlutils-test.example/data.xml");

        return XmlUtils.buildDocument(
                new StringWebResponse(xmlContent, StandardCharsets.UTF_8, url));
    }

    private static XmlPage loadXmlPage(final String xmlContent) throws Exception {
        try (WebClient webClient = new WebClient()) {
            final MockWebConnection conn = new MockWebConnection();
            conn.setDefaultResponse(xmlContent, MimeType.TEXT_XML);
            webClient.setWebConnection(conn);
            return (XmlPage) webClient.getPage("http://xmlutils-test.example/data.xml");
        }
    }

    private static Reader newTrackingReader(final String content) throws Exception {
        return newTrackingReaderInstance(content);
    }

    private static Reader newTrackingReaderInstance(final String content) throws Exception {
        final Class<?> clazz =
                Class.forName("org.htmlunit.util.XmlUtils$TrackBlankContentAndSkipLeadingWhitespaceReader");
        final Constructor<?> ctor = clazz.getDeclaredConstructor(Reader.class);
        ctor.setAccessible(true);
        return (Reader) ctor.newInstance(new StringReader(content));
    }

    private static Object invokeWasBlank(final Object trackerInstance) throws Exception {
        return trackerInstance.getClass().getMethod("wasBlank").invoke(trackerInstance);
    }
}