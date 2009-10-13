/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link XMLSerializer}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 */
@RunWith(BrowserRunner.class)
public class XMLSerializerTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        final String expectedString;
        if (getBrowserVersion().isIE()) {
            expectedString =
                "<note>13109<to>Tove</to>13109<from>Jani</from>13109<body>Do32not32forget32me32this32weekend!</body>"
                + "13109<outer>131099<inner>Some32Value</inner></outer>1310</note>1310";
        }
        else {
            expectedString =
                "<note>32<to>Tove</to>3210<from>Jani</from>321032<body>Do32not32forget32me32this32weekend!</body>"
                + "32<outer>10323232<inner>Some32Value</inner></outer>32</note>";
        }
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
        assertEquals(expectedString, textArea.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void nameSpaces() throws Exception {
        final String expectedString;
        if (getBrowserVersion().isIE()) {
            expectedString = "<?xml32version=\"1.0\"?>1310<xsl:stylesheet32version=\"1.0\"32"
                + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">13109<xsl:template32match=\"/\">131099<html>"
                + "1310999<body>1310999</body>131099</html>13109</xsl:template>1310</xsl:stylesheet>1310";
        }
        else {
            expectedString =
                "<xsl:stylesheet32version=\"1.0\"32xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">103232"
                + "<xsl:template32match=\"/\">103232<html>1032323232<body>1032323232</body>103232</html>103232"
                + "</xsl:template>10</xsl:stylesheet>";
        }
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
        assertEquals(expectedString, textArea.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void attributes() throws Exception {
        String expectedString = "<document32attrib=\"attribValue\">"
                          + "<outer32attrib=\"attribValue\">"
                          + "<inner32attrib=\"attribValue\"/>"
                          + "</outer>"
                          + "</document>";
        if (getBrowserVersion().isIE()) {
            expectedString += "1310";
        }
        final String serializationText = "<document attrib=\"attribValue\">"
                                            + "<outer attrib=\"attribValue\">"
                                            + "<inner attrib=\"attribValue\"/>"
                                            + "</outer></document>";

        final WebDriver driver = loadPageWithAlerts2(constructPageContent(serializationText));
        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedString, textArea.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    public void htmlAttributes() throws Exception {
        final String expectedString;
        if (getBrowserVersion().isIE()) {
            expectedString = "<?xml32version=\"1.0\"?>1310<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head><title>html</title></head>"
                + "<body32id=\"bodyId\">"
                + "<span32class=\"spanClass\">foo</span>"
                + "</body>"
                + "</html>1310";
        }
        else {
            expectedString = "<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head><title>html</title></head>"
                + "<body32id=\"bodyId\">"
                + "<span32class=\"spanClass\">foo</span>"
                + "</body>"
                + "</html>";
        }
        final String serializationText = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                                          + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                                          + "<head><title>html</title></head>"
                                          + "<body id=\"bodyId\">"
                                          + "<span class=\"spanClass\">foo</span>"
                                          + "</body>"
                                          + "</html>";

        final WebDriver driver = loadPageWithAlerts2(constructPageContent(serializationText));
        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        assertEquals(expectedString, textArea.getValue());
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "<foo/>", "<foo/>" })
    public void document() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    if (!document.all) {\n"
            + "      var doc = document.implementation.createDocument('', 'foo', null);\n"
            + "      alert(new XMLSerializer().serializeToString(doc));\n"
            + "      alert(new XMLSerializer().serializeToString(doc.documentElement));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "<div/>", "<DIV/>", "<?myTarget myData?>" })
    public void xml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    if (!document.all) {"
            + "      testFragment(doc);\n"
            + "      testFragment(document);\n"
            + "      var pi = doc.createProcessingInstruction('myTarget', 'myData');\n"
            + "      alert(new XMLSerializer().serializeToString(pi));\n"
            + "    }\n"
            + "  }\n"
            + "  function testFragment(doc) {\n"
            + "    var fragment = doc.createDocumentFragment();\n"
            + "    var div = doc.createElement('div');\n"
            + "    fragment.appendChild(div);\n"
            + "    alert(new XMLSerializer().serializeToString(fragment));\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "<root><my:parent xmlns:my=\"myUri\"><my:child/><another_child/></my:parent></root>",
            IE = "<root><my:parent xmlns:my=\"myUri\"><my:child/><another_child/></my:parent></root>\r\n")
    public void namespace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    var root = doc.createElement('root');\n"
            + "    doc.appendChild(root);\n"
            + "    var parent = createNS(doc, 'my:parent', 'myUri');\n"
            + "    root.appendChild(parent);\n"
            + "    parent.appendChild(createNS(doc, 'my:child', 'myUri'));\n"
            + "    parent.appendChild(doc.createElement('another_child'));\n"
            + "    alert(document.all ? doc.xml : new XMLSerializer().serializeToString(doc));\n"
            + "  }\n"
            + "  function createNS(doc, name, uri) {\n"
            + "    return document.all ? doc.createNode(1, name, uri) : doc.createElementNS(uri, name);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
