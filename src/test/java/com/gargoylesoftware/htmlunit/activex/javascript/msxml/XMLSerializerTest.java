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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.CREATE_XMLDOMDOCUMENT_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callSerializeXMLDOMDocumentToString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLSerializer}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLSerializerTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<note>13109<to>Tove</to>13109<from>Jani</from>13109<body>Do32not32forget32me32this32weekend!</body>"
                    + "13109<outer>131099<inner>Some32Value</inner></outer>1310</note>1310")
    public void test() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
        final String serializationText =
                "<note> "
                + "<to>Tove</to> \\n"
                + "<from>Jani</from> \\n "
                + "<body>Do not forget me this weekend!</body> "
                + "<outer>\\n "
                + "  <inner>Some Value</inner>"
                + "</outer> "
                + "</note>";

        final WebDriver driver = loadPageWithAlerts2(constructPageContent(serializationText));
        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedString, textArea.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<a><!--32abc32--></a>1310")
    public void comment() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
        final String serializationText = "<a><!-- abc --></a>";
        final WebDriver driver = loadPageWithAlerts2(constructPageContent(serializationText));
        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedString, textArea.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<a>&lt;&gt;&amp;</a>1310")
    public void xmlEntities() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
        final String serializationText = "<a>&lt;&gt;&amp;</a>";
        final WebDriver driver = loadPageWithAlerts2(constructPageContent(serializationText));
        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedString, textArea.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<?xml32version=\"1.0\"?>1310<xsl:stylesheet32version=\"1.0\"32"
                    + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">13109<xsl:template32match=\"/\">131099<html>"
                    + "1310999<body>1310999</body>131099</html>13109</xsl:template>1310</xsl:stylesheet>1310")
    @NotYetImplemented(IE)
    // so far we are not able to add the XML header
    public void nameSpaces() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
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

        final WebDriver driver = loadPageWithAlerts2(constructPageContent(serializationText));
        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedString, textArea.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<document32attrib=\"attribValue\"><outer32attrib=\"attribValue\">"
                    + "<inner32attrib=\"attribValue\"/><meta32attrib=\"attribValue\"/></outer></document>1310")
    public void attributes() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
        final String serializationText = "<document attrib=\"attribValue\">"
                                            + "<outer attrib=\"attribValue\">"
                                            + "<inner attrib=\"attribValue\"/>"
                                            + "<meta attrib=\"attribValue\"/>"
                                            + "</outer></document>";

        final WebDriver driver = loadPageWithAlerts2(constructPageContent(serializationText));
        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedString, textArea.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<?xml32version=\"1.0\"?>1310<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head><title>html</title></head>"
                    + "<body32id=\"bodyId\">"
                    + "<span32class=\"spanClass\">foo</span>"
                    + "</body>"
                    + "</html>1310")
    @NotYetImplemented(IE)
    // so far we are not able to add the XML header
    public void htmlAttributes() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
        final String serializationText = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                                          + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                                          + "<head><title>html</title></head>"
                                          + "<body id=\"bodyId\">"
                                          + "<span class=\"spanClass\">foo</span>"
                                          + "</body>"
                                          + "</html>";

        final WebDriver driver = loadPageWithAlerts2(constructPageContent(serializationText));
        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedString, textArea.getAttribute("value"));
    }

    /**
     * Constructs an HTML page that when loaded will parse and serialize the provided text.
     * First the provided text is parsed into a Document. Then the Document is serialized (browser-specific).
     * Finally the result is placed into the text area "myTextArea".
     */
    private String constructPageContent(final String serializationText) {
        final String escapedText = serializationText.replace("\n", "\\n");

        final StringBuilder buffer = new StringBuilder();
        buffer.append(
              "<html><head><title>foo</title><script>\n"
            + "  function test() {\n");

        buffer.append("    var text = '").append(escapedText).append("';\n").append(
              "    var doc = " + callLoadXMLDOMDocumentFromString("text") + ";\n"
            + "    var xml = " + callSerializeXMLDOMDocumentToString("doc") + ";\n"
            + "    var ta = document.getElementById('myTextArea');\n"
            + "    for (var i=0; i < xml.length; i++) {\n"
            + "      if (xml.charCodeAt(i) < 33)\n"
            + "        ta.value += xml.charCodeAt(i);\n"
            + "      else\n"
            + "        ta.value += xml.charAt(i);\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body></html>");
        return buffer.toString();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "<foo/>\r\n", "<foo/>" })
    public void document() throws Exception {
        final String html = "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "      doc.documentElement = doc.createElement('foo');\n"
            + "      alert(" + callSerializeXMLDOMDocumentToString("doc") + ");\n"
            + "      alert(" + callSerializeXMLDOMDocumentToString("doc.documentElement") + ");\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "<img/>", "<?myTarget myData?>" })
    public void xml() throws Exception {
        final String html = "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "      testFragment(doc);\n"
            + "      var pi = doc.createProcessingInstruction('myTarget', 'myData');\n"
            + "      alert(" + callSerializeXMLDOMDocumentToString("pi") + ");\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function testFragment(doc) {\n"
            + "    var fragment = doc.createDocumentFragment();\n"
            + "    var img = doc.createElement('img');\n"
            + "    fragment.appendChild(img);\n"
            + "    alert(" + callSerializeXMLDOMDocumentToString("fragment") + ");\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<root><my:parent xmlns:my=\"myUri\"><my:child/><another_child/></my:parent></root>\r\n")
    public void namespace() throws Exception {
        final String html = "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "      var root = doc.createElement('root');\n"
            + "      doc.appendChild(root);\n"
            + "      var parent = createNS(doc, 'my:parent', 'myUri');\n"
            + "      root.appendChild(parent);\n"
            + "      parent.appendChild(createNS(doc, 'my:child', 'myUri'));\n"
            + "      parent.appendChild(doc.createElement('another_child'));\n"
            + "      alert(" + callSerializeXMLDOMDocumentToString("doc") + ");\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function createNS(doc, name, uri) {\n"
            + "    return typeof doc.createNode == 'function' || typeof doc.createNode == 'unknown' ? "
            + "doc.createNode(1, name, uri) : doc.createElementNS(uri, name);\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<teXtaREa/>")
    public void mixedCase() throws Exception {
        final String html = "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "      var t = doc.createElement('teXtaREa');\n"
            + "      alert(" + callSerializeXMLDOMDocumentToString("t") + ");\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<img href=\"mypage.htm\"/>")
    public void noClosingTagWithAttribute() throws Exception {
        final String html = "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "      var t = doc.createElement('img');\n"
            + "      t.setAttribute('href', 'mypage.htm');\n"
            + "      alert(" + callSerializeXMLDOMDocumentToString("t") + ");\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION
            + SERIALIZE_XMLDOMDOCUMENT_TO_STRING_FUNCTION;
        loadPageWithAlerts2(createTestHTML(html));
    }
}
