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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for {@link XMLSerializer}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 */
public class XMLSerializerTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        final String expectedStringIE =
            "<note>13109<to>Tove</to>13109<from>Jani</from>13109<body>Do32not32forget32me32this32weekend!</body>"
            + "13109<outer>131099<inner>Some32Value</inner></outer>1310</note>1310";
        test(BrowserVersion.INTERNET_EXPLORER_7_0, expectedStringIE);
        final String expectedStringFF =
            "<note>32<to>Tove</to>3210<from>Jani</from>321032<body>Do32not32forget32me32this32weekend!</body>"
            + "32<outer>10323232<inner>Some32Value</inner></outer>32</note>";
        test(BrowserVersion.FIREFOX_2, expectedStringFF);
    }

    private void test(final BrowserVersion browserVersion, final String expectedString)
        throws Exception {
        final String serializationText =
                "<note> "
                + "<to>Tove</to> \\n"
                + "<from>Jani</from> \\n "
                + "<body>Do not forget me this weekend!</body> "
                + "<outer>\\n "
                + "  <inner>Some Value</inner>"
                + "</outer> "
                + "</note>";

        final HtmlPage page = loadPage(browserVersion, constructPageContent(serializationText), null);
        final HtmlTextArea textArea = (HtmlTextArea) page.getHtmlElementById("myTextArea");
        assertEquals(expectedString, textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void nameSpaces() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String expectedStringIE =
            "<?xml32version=\"1.0\"?>1310<xsl:stylesheet32version=\"1.0\"32"
            + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">13109<xsl:template32match=\"/\">131099<html>1310999"
            + "<body>1310999</body>131099</html>13109</xsl:template>1310</xsl:stylesheet>1310";
        nameSpaces(BrowserVersion.INTERNET_EXPLORER_7_0, expectedStringIE);
        final String expectedStringFF =
            "<xsl:stylesheet32version=\"1.0\"32xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">103232"
            + "<xsl:template32match=\"/\">103232<html>1032323232<body>1032323232</body>103232</html>103232"
            + "</xsl:template>10</xsl:stylesheet>";
        nameSpaces(BrowserVersion.FIREFOX_2, expectedStringFF);
    }

    private void nameSpaces(final BrowserVersion browserVersion, final String expectedString) throws Exception {
        final String serializationText =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n"
                + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\\n"
                + "  <xsl:template match=\"/\">\\n"
                + "  <html>\\n"
                + "    <body>\\n"
                + "    </body>\\n"
                + "  </html>\\n"
                + "  </xsl:template>\\n"
                + "</xsl:stylesheet>";

        final HtmlPage page = loadPage(browserVersion, constructPageContent(serializationText), null);
        final HtmlTextArea textArea = (HtmlTextArea) page.getHtmlElementById("myTextArea");
        assertEquals(expectedString, textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void attributes() throws Exception {
        final String expected = "<document32attrib=\"attribValue\">"
                          + "<outer32attrib=\"attribValue\">"
                          + "<inner32attrib=\"attribValue\"/>"
                          + "</outer>"
                          + "</document>";
        attributes(BrowserVersion.FIREFOX_2, expected);
        attributes(BrowserVersion.INTERNET_EXPLORER_7_0, expected + "1310");
    }

    private void attributes(final BrowserVersion browserVersion, final String expectedString)
        throws Exception {
        final String serializationContent = "<document attrib=\"attribValue\">"
                                            + "<outer attrib=\"attribValue\">"
                                            + "<inner attrib=\"attribValue\"/>"
                                            + "</outer></document>";

        final HtmlPage page = loadPage(browserVersion, constructPageContent(serializationContent), null);
        final HtmlTextArea textArea = (HtmlTextArea) page.getHtmlElementById("myTextArea");
        assertEquals(expectedString, textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void htmlAttributes() throws Exception {
        final String expected = "<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                          + "<head><title>html</title></head>"
                          + "<body32id=\"bodyId\">"
                          + "<span32class=\"spanClass\">foo</span>"
                          + "</body>"
                          + "</html>";
        htmlAttributes(BrowserVersion.FIREFOX_2, expected);
        htmlAttributes(BrowserVersion.INTERNET_EXPLORER_7_0, expected + "1310");
    }

    private void htmlAttributes(final BrowserVersion browserVersion, final String expectedString)
        throws Exception {
        final String serializationValue = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                                          + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                                          + "<head><title>html</title></head>"
                                          + "<body id=\"bodyId\">"
                                          + "<span class=\"spanClass\">foo</span>"
                                          + "</body>"
                                          + "</html>";

        final HtmlPage page = loadPage(browserVersion, constructPageContent(serializationValue), null);
        final HtmlTextArea textArea = (HtmlTextArea) page.getHtmlElementById("myTextArea");
        assertEquals(expectedString, textArea.getText());
    }

    /**
     * Constructs an HTML page that when loaded will parse and serialize the provided text.
     * First the provided text is parsed into a Document. Then the Document is serialized (browser-specific).
     * Finally the result is placed into the text area "myTextArea".
     */
    private String constructPageContent(final String serializationText) {
        final String escapedText = serializationText.replace("\n", "\\n");

        final StringBuffer buffer = new StringBuffer();
        buffer.append(
              "<html><head><title>foo</title><script>\n"
            + "  function test() {\n");

        buffer.append("    var text = '").append(escapedText).append("';\n").append(
              "    if (window.ActiveXObject) {\n"
            + "      var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      doc.async=false;\n"
            + "      doc.loadXML(text);\n"
            + "      var xml = doc.xml;\n"
            + "    } else {\n"
            + "      var parser=new DOMParser();\n"
            + "      var doc=parser.parseFromString(text,'text/xml');\n"
            + "      var serializer = new XMLSerializer();\n"
            + "      var xml = serializer.serializeToString(doc.documentElement);\n"
            + "    }\n"
            + "    var ta = document.getElementById('myTextArea');\n"
            + "    for (var i=0; i < xml.length; i++) {\n"
            + "      if (xml.charCodeAt(i) < 33)\n"
            + "        ta.value += xml.charCodeAt(i);\n"
            + "      else\n"
            + "        ta.value += xml.charAt(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body></html>");
        return buffer.toString();
    }
}
