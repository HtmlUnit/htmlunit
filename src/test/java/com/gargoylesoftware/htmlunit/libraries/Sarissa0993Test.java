/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.NONE;

import java.io.File;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for 0.9.9.3 version of <a href="http://sarissa.sourceforge.net">Sarissa</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class Sarissa0993Test extends WebServerTestCase {

    private static final Log LOG = LogFactory.getLog(Sarissa0993Test.class);

    private static HtmlPage Page_;

    /**
     * @throws Exception if an error occurs
     */
    @Before
    public void init() throws Exception {
        try {
            getBrowserVersion();
        }
        catch (final Exception e) {
            return;
        }
        startWebServer("src/test/resources/libraries/sarissa/" + getVersion());
        if (Page_ == null) {
            final WebClient client = getWebClient();
            final String url = "http://localhost:" + PORT + "/test/testsarissa.html";
            Page_ = client.getPage(url);
            Page_.<HtmlButton>getFirstByXPath("//button").click();

            // dump the result page
            if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null) {
                final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
                final File f = new File(tmpDir, "sarissa0993_result.html");
                FileUtils.writeStringToFile(f, Page_.asXml(), "UTF-8");
                LOG.info("Test result written to: " + f.getAbsolutePath());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected WebClient createNewWebClient() {
        return new WebClient(BrowserVersion.getDefault());
    }

    /**
     * Returns the Sarissa version being tested.
     *
     * @return the Sarissa version being tested
     */
    protected String getVersion() {
        return "0.9.9.3";
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void sarissa() throws Exception {
        test("SarissaTestCase");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xmlHttpRequest() throws Exception {
        test("XmlHttpRequestTestCase");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xmlSerializer() throws Exception {
        test("XMLSerializerTestCase");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void domParser() throws Exception {
        test("DOMParserTestCase");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xmlDocument() throws Exception {
        test("XMLDocumentTestCase");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xmlElement() throws Exception {
        test("XMLElementTestCase");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xsltProcessor() throws Exception {
        test("XSLTProcessorTestCase", "++++F+++");
    }

    private void test(final String testName) throws Exception {
        final List<?> divList =
            Page_.getByXPath("//div[@class='placeholder']/a[@name='#" + testName + "']/../div[last()]");
        assertEquals(1, divList.size());
        final HtmlDivision div = (HtmlDivision) divList.get(0);
        assertEquals("OK!", div.asText());
    }

    /**
     * This is used in case a failing test is expected to happen.
     *
     * @param expectedResult in the form of "+++F+++" (see the results in a real browser)
     */
    private void test(final String testName, final String expectedResult) throws Exception {
        final HtmlAnchor anchor =
            Page_.getFirstByXPath("//div[@class='placeholder']/a[@name='#" + testName + "']");
        final StringBuilder builder = new StringBuilder();
        for (Node node = anchor.getNextSibling().getNextSibling(); node instanceof DomText;
            node = node.getNextSibling()) {
            builder.append(((DomText) node).asText());
        }
        assertEquals(expectedResult, builder.toString());
    }

    /**
     * Performs deconstruction.
     * @throws Exception if an error occurs
     */
    @AfterClass
    public static void clean() throws Exception {
        Page_ = null;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(NONE)
    public void xslt() throws Exception {
        final String input = "<root><element attribute=\"value\"/></root>";
        final String style = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n"
            + "<xsl:output method=\"xml\" omit-xml-declaration=\"yes\"/>\n"
            + "<xsl:param select=\"'anonymous'\" name=\"user\"/>\n"
            + "<xsl:template match=\"/\">\n"
            + "<p id=\"user\">User: <xsl:value-of select=\"$user\"/>\n"
            + "</p>\n"
            + "<xsl:apply-templates/>\n"
            + "<hr/>\n"
            + "</xsl:template>\n"
            + "<xsl:template match=\"greeting\">\n"
            + "<p>\n"
            + "<xsl:apply-templates/>\n"
            + "</p>\n"
            + "</xsl:template>\n"
            + "</xsl:stylesheet>";

        final Source xmlSource = new StreamSource(new StringReader(input));
        final Source xsltSource = new StreamSource(new StringReader(style));

        final Document containerDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        final Element containerElement = containerDocument.createElement("container");
        containerDocument.appendChild(containerElement);

        final DOMResult result = new DOMResult(containerElement);

        final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
        transformer.transform(xmlSource, result);
    }
}
