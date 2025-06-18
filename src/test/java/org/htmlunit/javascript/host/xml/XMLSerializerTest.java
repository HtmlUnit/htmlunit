/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.xml;

import java.net.URL;

import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link XMLSerializer}.
 *
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 * @author Frank Danek
 * @author Ronald Brill
 * @author Michael Anstis
 */
public class XMLSerializerTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<note>32<to>Tove</to>3210<from>Jani</from>321032<body>Do32not32forget32me32this32weekend!</body>"
                    + "32<outer>10323232<inner>Some32Value</inner></outer>32</note>")
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

        final WebDriver driver = loadPageVerifyTitle2(constructPageContent(serializationText));
        verifyTextArea2(driver, expectedString);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<a><!--32abc32--></a>")
    public void comment() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
        final String serializationText = "<a><!-- abc --></a>";
        final WebDriver driver = loadPageVerifyTitle2(constructPageContent(serializationText));
        verifyTextArea2(driver, expectedString);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<a>&lt;&gt;&amp;</a>")
    public void xmlEntities() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
        final String serializationText = "<a>&lt;&gt;&amp;</a>";
        final WebDriver driver = loadPageVerifyTitle2(constructPageContent(serializationText));
        verifyTextArea2(driver, expectedString);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<?xml32version=\"1.0\"32encoding=\"UTF-8\"?>1310<xsl:stylesheet32version=\"1.0\"32"
                    + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">103232<xsl:template32match=\"/\">103232<html>"
                    + "1032323232<body>1032323232</body>103232</html>103232</xsl:template>10</xsl:stylesheet>",
            CHROME = "<?xml32version=\"1.0\"32encoding=\"ISO-8859-1\"?><xsl:stylesheet32xmlns:xsl="
                    + "\"http://www.w3.org/1999/XSL/Transform\"32version=\"1.0\">103232<xsl:template32"
                    + "match=\"/\">103232<html>1032323232<body>1032323232</body>103232</html>103232"
                    + "</xsl:template>10</xsl:stylesheet>",
            EDGE = "<?xml32version=\"1.0\"32encoding=\"ISO-8859-1\"?><xsl:stylesheet32xmlns:xsl="
                    + "\"http://www.w3.org/1999/XSL/Transform\"32version=\"1.0\">103232<xsl:template32"
                    + "match=\"/\">103232<html>1032323232<body>1032323232</body>103232</html>103232"
                    + "</xsl:template>10</xsl:stylesheet>")
    @HtmlUnitNYI(CHROME = "<xsl:stylesheet32version=\"1.0\"32xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
                    + "103232<xsl:template32match=\"/\">103232<html>1032323232<body>1032323232</body>103232</html>"
                    + "103232</xsl:template>10</xsl:stylesheet>",
            EDGE = "<xsl:stylesheet32version=\"1.0\"32xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
                    + "103232<xsl:template32match=\"/\">103232<html>1032323232<body>1032323232</body>103232</html>"
                    + "103232</xsl:template>10</xsl:stylesheet>",
            FF_ESR = "<xsl:stylesheet32version=\"1.0\"32"
                    + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">103232<xsl:template32match=\"/\">103232<html>"
                    + "1032323232<body>1032323232</body>103232</html>103232</xsl:template>10</xsl:stylesheet>",
            FF = "<xsl:stylesheet32version=\"1.0\"32"
                    + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">103232<xsl:template32match=\"/\">103232<html>"
                    + "1032323232<body>1032323232</body>103232</html>103232</xsl:template>10</xsl:stylesheet>")
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

        final WebDriver driver = loadPageVerifyTitle2(constructPageContent(serializationText));
        verifyTextArea2(driver, expectedString);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<document32attrib=\"attribValue\"><outer32attrib=\"attribValue\">"
                + "<inner32attrib=\"attribValue\"/><meta32attrib=\"attribValue\"/></outer></document>")
    public void attributes() throws Exception {
        final String expectedString = getExpectedAlerts()[0];
        setExpectedAlerts();
        final String serializationText = "<document attrib=\"attribValue\">"
                                            + "<outer attrib=\"attribValue\">"
                                            + "<inner attrib=\"attribValue\"/>"
                                            + "<meta attrib=\"attribValue\"/>"
                                            + "</outer></document>";

        final WebDriver driver = loadPageVerifyTitle2(constructPageContent(serializationText));
        verifyTextArea2(driver, expectedString);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<?xml32version=\"1.0\"32encoding=\"UTF-8\"?>1310<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head><title>html</title></head>"
                    + "<body32id=\"bodyId\">"
                    + "<span32class=\"spanClass\">foo</span>"
                    + "</body>"
                    + "</html>",
            CHROME = "<?xml32version=\"1.0\"32encoding=\"ISO-8859-1\"?><html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head><title>html</title></head>"
                    + "<body32id=\"bodyId\">"
                    + "<span32class=\"spanClass\">foo</span>"
                    + "</body>"
                    + "</html>",
            EDGE = "<?xml32version=\"1.0\"32encoding=\"ISO-8859-1\"?><html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head><title>html</title></head>"
                    + "<body32id=\"bodyId\">"
                    + "<span32class=\"spanClass\">foo</span>"
                    + "</body>"
                    + "</html>")
    @HtmlUnitNYI(CHROME = "<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head><title>html</title></head>"
                    + "<body32id=\"bodyId\">"
                    + "<span32class=\"spanClass\">foo</span>"
                    + "</body>"
                    + "</html>",
            EDGE = "<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head><title>html</title></head>"
                    + "<body32id=\"bodyId\">"
                    + "<span32class=\"spanClass\">foo</span>"
                    + "</body>"
                    + "</html>",
            FF_ESR = "<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head><title>html</title></head>"
                    + "<body32id=\"bodyId\">"
                    + "<span32class=\"spanClass\">foo</span>"
                    + "</body>"
                    + "</html>",
            FF = "<html32xmlns=\"http://www.w3.org/1999/xhtml\">"
                    + "<head><title>html</title></head>"
                    + "<body32id=\"bodyId\">"
                    + "<span32class=\"spanClass\">foo</span>"
                    + "</body>"
                    + "</html>")
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

        final WebDriver driver = loadPageVerifyTitle2(constructPageContent(serializationText));
        verifyTextArea2(driver, expectedString);
    }

    /**
     * Constructs an HTML page that when loaded will parse and serialize the provided text.
     * First the provided text is parsed into a Document. Then the Document is serialized (browser-specific).
     * Finally the result is placed into the text area "myTextArea".
     */
    private static String constructPageContent(final String serializationText) {
        final String escapedText = serializationText.replace("\n", "\\n");

        final StringBuilder builder = new StringBuilder();
        builder.append(
              "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n");

        builder.append("    var text = '").append(escapedText).append("';\n").append(
              "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "    var xml = " + XMLDocumentTest.callSerializeXMLDocumentToString("doc") + ";\n"
            + "    var ta = document.getElementById('myLog');\n"
            + "    for (var i = 0; i < xml.length; i++) {\n"
            + "      if (xml.charCodeAt(i) < 33)\n"
            + "        ta.value += xml.charCodeAt(i);\n"
            + "      else\n"
            + "        ta.value += xml.charAt(i);\n"
            + "    }\n"
            + "    ta.value += '\\u00a7';\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + XMLDocumentTest.SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>");
        return builder.toString();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"bodyId\"></body></html>",
             "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body id=\"bodyId\"></body></html>"})
    @HtmlUnitNYI(CHROME = {"<html><body id=\"bodyId\"></body></html>",
                           "<html><body id=\"bodyId\"></body></html>"},
            EDGE = {"<html><body id=\"bodyId\"></body></html>",
                    "<html><body id=\"bodyId\"></body></html>"},
            FF = {"<html><body id=\"bodyId\"></body></html>",
                  "<html><body id=\"bodyId\"></body></html>"},
            FF_ESR = {"<html><body id=\"bodyId\"></body></html>",
                      "<html><body id=\"bodyId\"></body></html>"})
    public void xhtmlDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var doc = document.implementation.createDocument('http://www.w3.org/1999/xhtml', 'html', null);\n"
            + "  var body = document.createElementNS('http://www.w3.org/1999/xhtml', 'body');\n"
            + "  body.setAttribute('id', 'bodyId');\n"
            + "  doc.documentElement.appendChild(body);"

            + "  log(new XMLSerializer().serializeToString(doc));\n"
            + "  log(new XMLSerializer().serializeToString(doc.documentElement));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<html xmlns=\"http://www.w3.org/1999/xhtml\"><body xmlns=\"\" id=\"bodyId\"/></html>",
             "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body xmlns=\"\" id=\"bodyId\"/></html>"})
    @HtmlUnitNYI(CHROME = {"<html><body id=\"bodyId\"></body></html>",
                           "<html><body id=\"bodyId\"></body></html>"},
            EDGE = {"<html><body id=\"bodyId\"></body></html>",
                    "<html><body id=\"bodyId\"></body></html>"},
            FF = {"<html><body id=\"bodyId\"></body></html>",
                  "<html><body id=\"bodyId\"></body></html>"},
            FF_ESR = {"<html><body id=\"bodyId\"></body></html>",
                      "<html><body id=\"bodyId\"></body></html>"})
    public void xhtmlDocumentBodyEmptyNamespace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var doc = document.implementation.createDocument('http://www.w3.org/1999/xhtml', 'html', null);\n"
            + "  var body = document.createElementNS('', 'body');\n"
            + "  body.setAttribute('id', 'bodyId');\n"
            + "  doc.documentElement.appendChild(body);"

            + "  log(new XMLSerializer().serializeToString(doc));\n"
            + "  log(new XMLSerializer().serializeToString(doc.documentElement));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>",
             "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body/></soap:Envelope>"})
    @HtmlUnitNYI(CHROME = {"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><Body></Body></soap:Envelope>",
                           "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><Body></Body></soap:Envelope>"},
            EDGE = {"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><Body></Body></soap:Envelope>",
                    "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><Body></Body></soap:Envelope>"},
            FF = {"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><Body></Body></soap:Envelope>",
                  "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><Body></Body></soap:Envelope>"},
            FF_ESR = {"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><Body></Body></soap:Envelope>",
                      "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><Body></Body></soap:Envelope>"})
    public void soapTest() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var dom = document.implementation.createDocument('http://schemas.xmlsoap.org/soap/envelope/', 'soap:Envelope', null);\n"

            + "  var soapEnv = dom.documentElement;\n"
            + "  soapBody = dom.createElementNS('http://schemas.xmlsoap.org/soap/envelope/', 'Body');\n"
            + "  soapEnv.appendChild(soapBody);"

            + "  log(new XMLSerializer().serializeToString(dom));\n"
            + "  log(new XMLSerializer().serializeToString(dom.documentElement));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<foo/>", "<foo/>"})
    public void document() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var doc = document.implementation.createDocument('', 'foo', null);\n"
            + "  log(new XMLSerializer().serializeToString(doc));\n"
            + "  log(new XMLSerializer().serializeToString(doc.documentElement));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("#")
    public void emptyDocumentFragment() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var fragment = document.createDocumentFragment();\n"
            + "  log('#' + new XMLSerializer().serializeToString(fragment));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">HtmlUnit</h1><h2 xmlns=\"http://www.w3.org/1999/xhtml\">is great</h2>")
    @HtmlUnitNYI(CHROME = "<h1 xmlns=\"http://www.w3.org/1999/xhtml\" >HtmlUnit</h1><h2 xmlns=\"http://www.w3.org/1999/xhtml\" >is great</h2>",
            EDGE = "<h1 xmlns=\"http://www.w3.org/1999/xhtml\" >HtmlUnit</h1><h2 xmlns=\"http://www.w3.org/1999/xhtml\" >is great</h2>",
            FF = "<h1 xmlns=\"http://www.w3.org/1999/xhtml\" >HtmlUnit</h1><h2 xmlns=\"http://www.w3.org/1999/xhtml\" >is great</h2>",
            FF_ESR = "<h1 xmlns=\"http://www.w3.org/1999/xhtml\" >HtmlUnit</h1><h2 xmlns=\"http://www.w3.org/1999/xhtml\" >is great</h2>")
    public void documentFragment() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var fragment = document.createDocumentFragment();\n"

            + "  var heading = document.createElement('h1');\n"
            + "  heading.textContent = 'HtmlUnit';\n"
            + "  fragment.appendChild(heading);\n"

            + "  heading = document.createElement('h2');\n"
            + "  heading.textContent = 'is great';\n"
            + "  fragment.appendChild(heading);\n"

            + "  log(new XMLSerializer().serializeToString(fragment));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<img/>", "<img xmlns=\"http://www.w3.org/1999/xhtml\" />", "<?myTarget myData?>"})
    public void xml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    testFragment(doc);\n"
            + "    testFragment(document);\n"
            + "    var pi = doc.createProcessingInstruction('myTarget', 'myData');\n"
            + "    log(new XMLSerializer().serializeToString(pi));\n"
            + "  }\n"
            + "  function testFragment(doc) {\n"
            + "    var fragment = doc.createDocumentFragment();\n"
            + "    var img = doc.createElement('img');\n"
            + "    fragment.appendChild(img);\n"
            + "    log(new XMLSerializer().serializeToString(fragment));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<root><my:parent xmlns:my=\"myUri\"><my:child/><another_child/></my:parent></root>")
    public void namespace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = document.implementation.createDocument('', '', null);\n"
            + "    var root = doc.createElement('root');\n"
            + "    doc.appendChild(root);\n"
            + "    var parent = createNS(doc, 'my:parent', 'myUri');\n"
            + "    root.appendChild(parent);\n"
            + "    parent.appendChild(createNS(doc, 'my:child', 'myUri'));\n"
            + "    parent.appendChild(doc.createElement('another_child'));\n"
            + "    log(" + XMLDocumentTest.callSerializeXMLDocumentToString("doc") + ");\n"
            + "  }\n"
            + "  function createNS(doc, name, uri) {\n"
            + "    return typeof doc.createNode == 'function' || typeof doc.createNode == 'unknown' ? "
            + "doc.createNode(1, name, uri) : doc.createElementNS(uri, name);\n"
            + "  }\n"
            + XMLDocumentTest.SERIALIZE_XML_DOCUMENT_TO_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<textarea xmlns=\"http://www.w3.org/1999/xhtml\"></textarea>")
    public void mixedCase() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.createElement('teXtaREa');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<area xmlns=\"http://www.w3.org/1999/xhtml\" />",
             "<base xmlns=\"http://www.w3.org/1999/xhtml\" />",
             "<basefont xmlns=\"http://www.w3.org/1999/xhtml\" />",
             "<br xmlns=\"http://www.w3.org/1999/xhtml\" />",
             "<hr xmlns=\"http://www.w3.org/1999/xhtml\" />",
             "<input xmlns=\"http://www.w3.org/1999/xhtml\" />",
             "<link xmlns=\"http://www.w3.org/1999/xhtml\" />",
             "<meta xmlns=\"http://www.w3.org/1999/xhtml\" />"})
    public void noClosingTag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.createElement('area');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('base');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('basefont');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('br');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('hr');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('input');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('link');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('meta');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * There is a small difference between HtmlUnit and FF.
     * HtmlUnit adds type=text per default to the input
     * FF handles input without type as type=text
     * the fix for this is too big, so i made this workaround
     * and add another (not yet implemented) test
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<input xmlns=\"http://www.w3.org/1999/xhtml\" />")
    public void inputTagWithoutType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.createElement('input');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for some not self closing tags.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div xmlns=\"http://www.w3.org/1999/xhtml\"></div>",
             "<h1 xmlns=\"http://www.w3.org/1999/xhtml\"></h1>",
             "<p xmlns=\"http://www.w3.org/1999/xhtml\"></p>",
             "<li xmlns=\"http://www.w3.org/1999/xhtml\"></li>",
             "<textarea xmlns=\"http://www.w3.org/1999/xhtml\"></textarea>"})
    public void otherTags() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.createElement('div');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('h1');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('p');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('li');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "    var t = document.createElement('textarea');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<img xmlns=\"http://www.w3.org/1999/xhtml\" href=\"mypage.htm\" />")
    public void noClosingTagWithAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.createElement('img');\n"
            + "    t.setAttribute('href', 'mypage.htm');\n"
            + "    log(new XMLSerializer().serializeToString(t));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<catalog>\n"
                    + "  <cd>\n"
                    + "    <title>Empire Burlesque</title>\n"
                    + "    <artist>Bob Dylan \u1042</artist>\n"
                    + "  </cd>\n"
                    + "</catalog>",
            EDGE = "<catalog>\n"
                    + "  <cd>\n"
                    + "    <title>Empire Burlesque</title>\n"
                    + "    <artist>Bob Dylan \u1042</artist>\n"
                    + "  </cd>\n"
                    + "</catalog>",
            FF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF_ESR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    @HtmlUnitNYI(FF_ESR = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    public void outputXmlIndent() throws Exception {
        transform("<xsl:output method='xml' indent='yes' />");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<catalog>\n"
                    + "  <cd>\n"
                    + "    <title>Empire Burlesque</title>\n"
                    + "    <artist>Bob Dylan \u1042</artist>\n"
                    + "  </cd>\n"
                    + "</catalog>",
            EDGE = "<catalog>\n"
                    + "  <cd>\n"
                    + "    <title>Empire Burlesque</title>\n"
                    + "    <artist>Bob Dylan \u1042</artist>\n"
                    + "  </cd>\n"
                    + "</catalog>",
            FF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF_ESR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    @HtmlUnitNYI(FF_ESR = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    public void outputIndent() throws Exception {
        transform("<xsl:output indent='yes' />");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            EDGE = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF_ESR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    @HtmlUnitNYI(FF_ESR = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    public void outputNoIndent() throws Exception {
        transform("<xsl:output indent='no' />");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            EDGE = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF_ESR = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    @HtmlUnitNYI(FF_ESR = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    public void outputEncoding1252() throws Exception {
        transform("<xsl:output encoding='Windows-1252' />");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            EDGE = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n"
                + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF_ESR = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n"
                + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    @HtmlUnitNYI(FF_ESR = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    public void outputEncoding1251() throws Exception {
        transform("<xsl:output encoding='Windows-1251' />");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    public void outputOmitXmlDeclaration() throws Exception {
        transform("<xsl:output omit-xml-declaration='yes' />");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            EDGE = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF_ESR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    @HtmlUnitNYI(FF_ESR = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>",
            FF = "<catalog><cd><title>Empire Burlesque</title><artist>Bob Dylan \u1042</artist></cd></catalog>")
    public void noOutput() throws Exception {
        transform("");
    }

    private void transform(final String xslOutput) throws Exception {
        final String xml
                = "<?xml version='1.0' encoding='ISO-8859-1'?>"
                + "<catalog><cd><title>Empire Burlesque</title>"
                + "<artist>Bob Dylan &#x1042;</artist>"
                + "</cd>"
                + "</catalog>";

        final String xsl
                = "<?xml version='1.0' encoding='ISO-8859-1'?>"
                + "<xsl:stylesheet version='1.0' "
                + "xmlns:xsl='http://www.w3.org/1999/XSL/Transform' >"
                + "  " + xslOutput
                + "  <xsl:template match='@*|node()'>"
                + "    <xsl:copy>"
                + "      <xsl:apply-templates select='@*|node()'/>"
                + "    </xsl:copy>"
                + "  </xsl:template>"
                + "</xsl:stylesheet>";

        final String html = DOCTYPE_HTML
                + "<html><head><title>foo</title><script>\n"
                + "  function test() {\n"
                + "    var ta = document.getElementById('myLog');\n"
                + "    try {\n"
                + "      var xsltProcessor = new XSLTProcessor();\n"
                + "      var xmlDoc = new DOMParser().parseFromString(\"" + xml + "\", \"application/xml\");\n"
                + "      var xsltDoc = new DOMParser().parseFromString(\"" + xsl + "\", \"application/xml\");\n"
                + "      xsltProcessor.importStylesheet(xsltDoc);\n"
                + "      var resultDocument = xsltProcessor.transformToDocument(xmlDoc);\n"
                + "      var xml = new XMLSerializer().serializeToString(resultDocument);\n"
                + "      ta.value = xml + '\\u00a7';\n"
                + "    } catch(e) { ta.value = 'exception'; }\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + LOG_TEXTAREA
                + "</body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_SECOND, "1"), xml, MimeType.TEXT_XML);
        conn.setResponse(new URL(URL_SECOND, "2"), xsl, MimeType.TEXT_XML);

        final WebDriver driver = loadPage2(html);

        verifyTextArea2(driver, getExpectedAlerts()[0]);
    }
}
