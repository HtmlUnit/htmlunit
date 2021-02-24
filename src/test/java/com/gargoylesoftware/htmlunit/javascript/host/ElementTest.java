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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link Element}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class ElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "attrName attrValue", "attrValue", "null", "anotherValue",
                    "1", "4", "<span id='label'>changed</span>"})
    public void attributes() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "    var attributes = doc.documentElement.attributes;\n"
            + "    log(attributes.length);\n"
            + "    log(attributes[0].name + ' ' + attributes[0].value);\n"
            + "    var root = doc.documentElement;\n"
            + "    log(root.getAttribute('attrName'));\n"
            + "    log(root.getAttribute('notExisting'));\n"
            + "    root.setAttribute('attrName', 'anotherValue');\n"
            + "    log(root.getAttribute('attrName'));\n"
            + "    log(root.getElementsByTagName('book').length);\n"
            + "    var description = root.getElementsByTagName('description')[0];\n"
            + "    log(description.firstChild.nodeType);\n"
            + "    log(description.firstChild.nodeValue);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books attrName=\"attrValue\">\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "    <description><![CDATA[<span id='label'>changed</span>]]></description>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void selectNodes() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      var nodes = doc.documentElement.selectNodes('//title');\n"
            + "      log(nodes.length);\n"
            + "      log(nodes[0].tagName);\n"
            + "    } catch (e) { log('exception'); }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "1"})
    public void removeChild() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var parent = doc.documentElement.firstChild;\n"
            + "    log(parent.childNodes.length);\n"
            + "    parent.removeChild(parent.firstChild);\n"
            + "    log(parent.childNodes.length);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<books><book><title>Immortality</title><author>John Smith</author></book></books>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"lbl_SettingName", "outerHTML", "undefined"})
    public void getAttributeNode() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    parseXML(doc);\n"
            + "  }\n"
            + "  function parseXML(xml) {\n"
            + "    if (xml.documentElement.hasChildNodes()) {\n"
            + "      for (var i = 0; i < xml.documentElement.childNodes.length; i++) {\n"
            + "        var elem = xml.documentElement.childNodes.item(i);\n"
            + "        if (elem.nodeName == 'control') {\n"
            + "          var target = elem.getAttributeNode('id').value;\n"
            + "          if(document.all(target) != null) {\n"
            + "            for (var j = 0; j < elem.childNodes.length; j++) {\n"
            + "              var node = elem.childNodes.item(j);\n"
            + "              if (node.nodeName == 'tag') {\n"
            + "                var type = node.getAttributeNode('type').value;\n"
            + "                log(target);\n"
            + "                log(type);\n"
            + "                log(node.text);\n"
            + "                eval('document.all(\"' + target + '\").' + type + '=\"' + node.text + '\"');\n"
            + "              }\n"
            + "            }\n"
            + "          }\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "  <div id='lbl_SettingName'/>\n"
            + "</body></html>";

        final String xml
            = "<responsexml>\n"
            + "  <control id='lbl_SettingName'>\n"
            + "    <tag type='outerHTML'><span id='lbl_SettingName' class='lbl-white-001'>Item</span></tag>\n"
            + "  </control>\n"
            + "</responsexml>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"book", "exception"})
    public void selectNode_root() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var child = doc.documentElement.firstChild;\n"
            + "    log(child.tagName);\n"
            + "    try {\n"
            + "      log(child.selectNodes('/title').length);\n"
            + "      log(child.selectNodes('title').length);\n"
            + "    } catch (e) { log('exception'); }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<books><book><title>Immortality</title><author>John Smith</author></book></books>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1"})
    public void getElementsByTagNameNS() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "    try {\n"
            + "      log(doc.documentElement.getElementsByTagNameNS('http://myNS', 'template').length);\n"
            + "      log(doc.documentElement.getElementsByTagNameNS(null, 'html').length);\n"
            + "    } catch (e) { log('exception'); }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "3"})
    public void getElementsByTagNameNSAsterisk() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "    try {\n"
            + "      log(doc.documentElement.getElementsByTagNameNS('http://myNS', '*').length);\n"
            + "      log(doc.documentElement.getElementsByTagNameNS(null, '*').length);\n"
            + "      log(doc.documentElement.getElementsByTagNameNS('*', '*').length);\n"
            + "    } catch (e) { log('exception'); }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "<nested>Three</nested>", "Four",
                "1", "Two", "0", "0"},
            IE = {"2", "undefined", "undefined",
                    "1", "undefined", "0", "0"})
    public void getElementsByTagNameXml() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "</head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var xmlString = [\n"
            + "                 '<ResultSet>',\n"
            + "                 '<Result>One</Result>',\n"
            + "                 '<RESULT>Two</RESULT>',\n"
            + "                 '<result><nested>Three</nested></result>',\n"
            + "                 '<result>Four</result>',\n"
            + "                 '</ResultSet>'\n"
            + "                ].join('');\n"
            + "  if (window.DOMParser) {\n"
            + "    var parser = new DOMParser();\n"
            + "    xml = parser.parseFromString(xmlString, 'text/xml');\n"
            + "  } else { // IE\n"
            + "    var parser = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    parser.async = 'false';\n"
            + "    parser.loadXML(xmlString);\n"
            + "  }\n"
            + "  var xmlDoc = parser.parseFromString(xmlString, 'text/xml');\n"
            + "  var de = xmlDoc.documentElement;\n"
            + "  try {\n"

            + "    var res = de.getElementsByTagName('result');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"
            + "    log(res[1].innerHTML);\n"

            + "    res = de.getElementsByTagName('RESULT');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"

            + "    res = de.getElementsByTagName('resulT');\n"
            + "    log(res.length);\n"

            + "    res = de.getElementsByTagName('rEsulT');\n"
            + "    log(res.length);\n"
            + "  } catch(e) {log('exception ' + e)}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void hasAttribute() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "    if (!doc.documentElement.hasAttribute) { log('hasAttribute not available'); return }\n"
            + "    log(doc.documentElement.hasAttribute('something'));\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void attributes2() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "    log(doc.documentElement.attributes.getNamedItem('library') != undefined);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books library=\"Hope\">\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined"})
    public void xml() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var text = '<a><b c=\"d\">e</b></a>';\n"
            + "  var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "  log(doc.xml);\n"
            + "  log(doc.documentElement.xml);\n"
            + "}\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV"})
    public void html_nodeName() throws Exception {
        html("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV"})
    public void html_tagName() throws Exception {
        html("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object Element],app",
                "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null"})
    public void html_prefix() throws Exception {
        html("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],div",
                "[object HTMLUnknownElement],app:div",
                "[object Element],dIv",
                "[object HTMLDivElement],div",
                "[object HTMLUnknownElement],app:div"})
    public void html_localName() throws Exception {
        html("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],undefined",
                "[object HTMLUnknownElement],undefined",
                "[object Element],undefined",
                "[object HTMLDivElement],undefined",
                "[object HTMLUnknownElement],undefined"})
    public void html_baseName() throws Exception {
        html("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml",
                "[object Element],http://www.appcelerator.org",
                "[object HTMLDivElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml"})
    public void html_namespaceURI() throws Exception {
        html("namespaceURI");
    }

    private void html(final String methodName) throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    debug(document.createElement('dIv'));\n"
            + "    debug(document.createElement('app:dIv'));\n"
            + "    debug(document.createElementNS('http://www.appcelerator.org', 'app:dIv'));\n"
            + "    debug(document.getElementById('dIv1'));\n"
            + "    debug(document.getElementById('dIv2'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    log(e + ',' + e." + methodName + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<dIv id='dIv1'></dIv>\n"
            + "<app:dIv id='dIv2'>log(2)</app:dIv>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV"})
    public void namespace_nodeName() throws Exception {
        namespace("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV"})
    public void namespace_tagName() throws Exception {
        namespace("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object HTMLUnknownElement],null",
                "[object Element],app",
                "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object HTMLUnknownElement],null"})
    public void namespace_prefix() throws Exception {
        namespace("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],div",
                "[object HTMLUnknownElement],app:div",
                "[object HTMLUnknownElement],another:div",
                "[object Element],dIv",
                "[object HTMLDivElement],div",
                "[object HTMLUnknownElement],app:div",
                "[object HTMLUnknownElement],another:div"})
    public void namespace_localName() throws Exception {
        namespace("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDivElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml",
                "[object Element],http://www.appcelerator.org",
                "[object HTMLDivElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml"})
    public void namespace_namespaceURI() throws Exception {
        namespace("namespaceURI");
    }

    private void namespace(final String methodName) throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    debug(document.createElement('dIv'));\n"
            + "    debug(document.createElement('app:dIv'));\n"
            + "    debug(document.createElement('another:dIv'));\n"
            + "    debug(document.createElementNS('http://www.appcelerator.org', 'app:dIv'));\n"
            + "    debug(document.getElementById('dIv1'));\n"
            + "    debug(document.getElementById('dIv2'));\n"
            + "    debug(document.getElementById('dIv3'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    log(e + ',' + e." + methodName + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<dIv id='dIv1'></dIv>\n"
            + "<app:dIv id='dIv2'></app:dIv>\n"
            + "<another:dIv id='dIv3'></another:dIv>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "dIv",
                "[object HTMLHtmlElement]", "html",
                "[object HTMLDivElement]", "div",
                "[object HTMLUnknownElement]", "dIv"})
    public void xml_nodeName() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        xml("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "dIv",
                "[object HTMLHtmlElement]", "html",
                "[object HTMLDivElement]", "div",
                "[object HTMLUnknownElement]", "dIv"})
    public void xml_tagName() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        xml("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "null",
                "[object HTMLHtmlElement]", "null",
                "[object HTMLDivElement]", "null",
                "[object HTMLUnknownElement]", "null"})
    public void xml_prefix() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        xml("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "dIv",
                "[object HTMLHtmlElement]", "html",
                "[object HTMLDivElement]", "div",
                "[object HTMLUnknownElement]", "dIv"})
    public void xml_localName() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        xml("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "undefined",
                "[object HTMLHtmlElement]", "undefined",
                "[object HTMLDivElement]", "undefined",
                "[object HTMLUnknownElement]", "undefined"})
    public void xml_baseName() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        xml("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Element]", "null",
                "[object HTMLHtmlElement]", "http://www.w3.org/1999/xhtml",
                "[object HTMLDivElement]", "http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement]", "http://www.w3.org/1999/xhtml"})
    public void xml_namespaceURI() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        xml("namespaceURI");
    }

    private void xml(final String methodName) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "        debug(doc.documentElement.childNodes[0]);\n"
            + "        debug(doc.documentElement.childNodes[1]);\n"
            + "        debug(doc.documentElement.childNodes[1].childNodes[0]);\n"
            + "        debug(doc.documentElement.childNodes[1].childNodes[1]);\n"
            + "      }\n"
            + "      function debug(e) {\n"
            + "        try {\n"
            + "          log(e);\n"
            + "        } catch(ex) {log(ex)}\n"
            + "        log(e." + methodName + ");\n"
            + "      }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>"
            + "<dIv></dIv>"
            + "<html xmlns='http://www.w3.org/1999/xhtml'>"
            + "<div></div>"
            + "<dIv></dIv>"
            + "</html>"
            + "</xml>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"prototype found", ""
                    + "ELEMENT_NODE, ATTRIBUTE_NODE, TEXT_NODE, CDATA_SECTION_NODE, ENTITY_REFERENCE_NODE, "
                    + "ENTITY_NODE, PROCESSING_INSTRUCTION_NODE, COMMENT_NODE, DOCUMENT_NODE, DOCUMENT_TYPE_NODE, "
                    + "DOCUMENT_FRAGMENT_NODE, NOTATION_NODE, DOCUMENT_POSITION_DISCONNECTED, "
                    + "DOCUMENT_POSITION_PRECEDING, "
                    + "DOCUMENT_POSITION_FOLLOWING, DOCUMENT_POSITION_CONTAINS, DOCUMENT_POSITION_CONTAINED_BY, "
                    + "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC, "},
            IE = {"prototype found", ""})
    public void enumeratedProperties() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var str = '';\n"
            + "    try {\n"
            + "      log(Element.prototype ? 'prototype found' : 'prototype not found');\n"
            + "      var str = '';\n"
            + "      for (var i in Element)\n"
            + "        str += i + ', ';\n"
            + "      log(str);\n"
            + "    } catch (e) { log('exception occured')}\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("finished")
    public void removeAttribute() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "        var e = doc.getElementsByTagName('title');\n"
            + "        e[0].removeAttribute('hello');\n"
            + "        log('finished');\n"
            + "      }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function")
    public void getBoundingClientRect() throws Exception {
        final String html = "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(typeof Element.prototype.getBoundingClientRect);\n"
            + "} catch (e) { log('exception');}\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void commentIsElement() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='myId'><!-- --></div>\n"
            + "<script>\n"
            + "  alert(myId.getElementsByTagName('*').length);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Node should not have 'innerText', however HTMLElement should have.
     * The below case checks if Element (which is Node) doesn't define it.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void nodeHasUndefinedInnerText() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var data = \"<?xml version='1.0' encoding='UTF-8'?>\\\n"
            + "        <dashboard> \\\n"
            + "          <locations class='foo'> \\\n"
            + "            <location for='bar' checked='different'> \\\n"
            + "              <infowindowtab normal='ab' mixedCase='yes'> \\\n"
            + "                <tab title='Location'><![CDATA[blabla]]></tab> \\\n"
            + "                <tab title='Users'><![CDATA[blublu]]></tab> \\\n"
            + "              </infowindowtab> \\\n"
            + "            </location> \\\n"
            + "          </locations> \\\n"
            + "        </dashboard>\";\n"
            + "    var xml, tmp;\n"
            + "    try {\n"
            + "      if (window.DOMParser) {\n"
            + "        tmp = new DOMParser();\n"
            + "        xml = tmp.parseFromString(data, 'text/xml');\n"
            + "      } else { // IE\n"
            + "        xml = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "        xml.async = 'false';\n"
            + "        xml.loadXML(data);\n"
            + "      }\n"
            + "    } catch(e) {\n"
            + "      xml = undefined;\n"
            + "    }\n"
            + "\n"
            + "    alert(xml.getElementsByTagName('tab')[0].innerText);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'/></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "third", "3", "second", "second"})
    public void firstElementChild() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    if (e.firstElementChild) {\n"
            + "      log(e.firstElementChild.id);\n"
            + "      log(e.lastElementChild.id);\n"
            + "      log(e.childElementCount);\n"
            + "      log(e.firstElementChild.nextElementSibling.id);\n"
            + "      log(e.lastElementChild.previousElementSibling.id);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>\n"
            + "    <input id='first' type='button' value='someValue'>\n"
            + "    <br id='second'/>\n"
            + "    <input id='third' type=button' value='something'>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"§§URL§§", "§§URL§§"},
            IE = {"undefined", "undefined"})
    public void baseURI() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><script>\n"
            + "function test() {\n"
            + "  var text = '<hello><child></child></hello>';\n"
            + "  var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "  var e = doc.documentElement.firstChild;\n"
            + "  alert(e.baseURI);\n"
            + "\n"
            + "  e = document.getElementById('myId');\n"
            + "  alert(e.baseURI);\n"
            + "}\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myId'>abcd</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLCollection] 1", "[object HTMLCollection] 2", "[object HTMLCollection] 0"},
            IE = {"exception", "[object HTMLCollection] 2", "[object HTMLCollection] 0"})
    public void children() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var text = '<hello><child></child></hello>';\n"
            + "  var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"

            + "  try {\n"
            + "    var children = doc.documentElement.children;\n"
            + "    log(children + ' ' + children.length);\n"
            + "  } catch (e) { log('exception'); }\n"

            + "  try {\n"
            + "    children = document.documentElement.children;\n"
            + "    log(children + ' ' + children.length);\n"
            + "  } catch (e) { log('exception'); }\n"

            + "  try {\n"
            + "    children = document.getElementById('myId').children;\n"
            + "    log(children + ' ' + children.length);\n"
            + "  } catch (e) { log('exception'); }\n"
            + "}\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myId'>abcd</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "a b c"},
            IE = {"undefined", "a b c"})
    public void classList() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var text = '<hello><child></child></hello>';\n"
            + "  var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "  log(doc.documentElement.classList);\n"
            + "  log(document.body.classList);\n"
            + "}\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()' class='a b c'>\n"
            + "  <div id='myId'>abcd</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests the usage of getAttributeNS, setAttributeNS, removeAttributeNS
     * and hasAttributeNS methods.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"test value", "true", "false", "finished"})
    public void attributeNS() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var doc = document.implementation.createDocument(\"\", \"\", null);\n"
            + "        var element = doc.createElementNS(\'uri:test\', \'test:element\');\n"
            + "        element.setAttributeNS(\'uri:test\', \'test:attribute\', 'test value');\n"
            + "        log(element.getAttributeNS(\'uri:test\', \'attribute\'));\n"
            + "        log(element.hasAttributeNS(\'uri:test\', \'attribute\'));\n"
            + "        element.removeAttributeNS(\'uri:test\', \'attribute\');\n"
            + "        log(element.hasAttributeNS(\'uri:test\', \'attribute\'));\n"
            + "        log('finished');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ab", "ab"})
    public void removeAttribute_case_sensitive() throws Exception {
        // TODO [IE]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
        shutDownRealIE();

        final String html = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "        var e = doc.getElementsByTagName('title')[0];\n"
            + "        log(e.getAttribute('normal'));\n"
            + "        e.removeAttribute('Normal');\n"
            + "        log(e.getAttribute('normal'));\n"
            + "      }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title normal=\"ab\">Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("test")
    public void setAttributeNode() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var doc = document.implementation.createDocument('', '', null);\n"
            + "        var element = doc.createElement('something');\n"
            + "        var attr = doc.createAttribute('name');\n"
            + "        attr.value = 'test';\n"
            + "        element.setAttributeNode(attr);\n"
            + "        alert(element.getAttributeNode('name').value);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined"},
            IE = {"available", "null"})
    public void currentStyle() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var e = document.getElementById('tester');\n"
            + "      log(e.currentStyle ? 'available' : e.currentStyle);\n"
            + "      e = document.createElement('div');\n"
            + "      log(e.currentStyle ? 'available' : e.currentStyle);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "undefined"})
    public void allowKeyboardInput() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(Element.ALLOW_KEYBOARD_INPUT);\n"
            + "      log(typeof Element.ALLOW_KEYBOARD_INPUT);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void enumeratedPropertiesForNativeFunction() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var str = '';\n"
            + "    for (var i in test)\n"
            + "      str += i + ', ';\n"
            + "    alert(str);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function Element() { [native code] }", "[object Element]",
                        "function Element() { [native code] }"},
            FF = {"function Element() {\n    [native code]\n}",
                    "[object Element]", "function Element() {\n    [native code]\n}"},
            FF78 = {"function Element() {\n    [native code]\n}",
                    "[object Element]", "function Element() {\n    [native code]\n}"},
            IE = {"[object Element]", "[object ElementPrototype]", "[object Element]"})
    @NotYetImplemented(IE)
    public void prototypConstructor() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      process(Element);\n"
            + "      process(Element.prototype);\n"
            + "      process(Element.prototype.constructor);\n"
            + "    } catch (e) {log('exception')}\n"
            + "  }\n"
            + "  function process(obj) {\n"
            + "    try {\n"
            + "      alert(obj);\n"
            + "    } catch (e) {alert('exception')}\n"
            + "   }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"function Element() { [native code] }", "[object Element]",
                        "function Element() { [native code] }"},
            FF = {"function Element() {\n    [native code]\n}",
                        "[object Element]", "function Element() {\n    [native code]\n}"},
            FF78 = {"function Element() {\n    [native code]\n}",
                        "[object Element]", "function Element() {\n    [native code]\n}"},
            IE = {"[object Element]", "[object ElementPrototype]", "[object Element]"})
    @NotYetImplemented(IE)
    public void prototypConstructorStandards() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    process(Element);\n"
            + "    process(Element.prototype);\n"
            + "    process(Element.prototype.constructor);\n"
            + "  }\n"
            + "  function process(obj) {\n"
            + "    alert(obj);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0-0", "0-1", "2-5"})
    public void childElementCount() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    info(document.getElementById('emptyDiv'));\n"
            + "    info(document.getElementById('whitespaceDiv'));\n"
            + "    info(document.getElementById('testDiv'));\n"
            + "  }\n"
            + "  function info(e) {\n"
            + "    log(e.childElementCount + '-' + e.childNodes.length);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='emptyDiv'></div>\n"
            + "  <div id='whitespaceDiv'>\n"
            + "  </div>\n"
            + "  <div id='testDiv'>\n"
            + "    <div>first</div>\n"
            + "    <div>\n"
            + "      <span>second</span>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1-2", "3-7"})
    public void childElementCountTable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    info(document.getElementById('myTable'));\n"
            + "    info(document.getElementById('myTr'));\n"
            + "  }\n"
            + "  function info(e) {\n"
            + "    log(e.childElementCount + '-' + e.childNodes.length);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <table id='myTable'>\n"
            + "    <tr id='myTr'>\n"
            + "      <td>first</td>\n"
            + "      <td><div>second</div></td>\n"
            + "      <td>\n"
            + "        third\n"
            + "      </td>\n"
            + "    </tr>\n"
            + "  </table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void hasAttributes() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.body.hasAttributes());\n"
            + "    log(document.body.childNodes.item(1).hasAttributes());\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "Philippine eagle",
            IE = "not found")
    public void matches() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var birds = document.getElementsByTagName('li');\n"
            + "    var found = false;"
            + "    for (var i = 0; i < birds.length; i++) {\n"
            + "      if (birds[i].matches && birds[i].matches('.endangered')) {\n"
            + "        log(birds[i].textContent);\n"
            + "        found = true;"
            + "      }\n"
            + "    }\n"
            + "    if (!found) {\n"
            + "      log('not found');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <ul id='birds'>\n"
            + "    <li>Orange-winged parrot</li>\n"
            + "    <li class='endangered'>Philippine eagle</li>\n"
            + "    <li>Great white pelican</li>\n"
            + "  </ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"SyntaxError", "done"},
            IE = "done")
    public void matchesInvalidSelector() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var birds = document.getElementsByTagName('li');\n"
            + "    try {\n"
            + "      for (var i = 0; i < birds.length; i++) {\n"
            + "        if (birds[i].matches && birds[i].matches('invalidSelector!=:::x')) {\n"
            + "          log(birds[i].textContent);\n"
            + "        }\n"
            + "      }\n"
            + "    } catch (e) { log(e.name); }\n"
            + "    log('done');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <ul id='birds'>\n"
            + "    <li>Great white pelican</li>\n"
            + "  </ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"TypeError", "done"})
    public void matchesWindow() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var docElem = document.documentElement;\n"
            + "    var matches = docElem.matchesSelector\n"
            + "         || docElem.mozMatchesSelector\n"
            + "         || docElem.webkitMatchesSelector\n"
            + "         || docElem.msMatchesSelector;\n"
            + "    try {\n"
            + "      matches.call(window, ':visible')\n"
            + "    } catch (e) { log(e.name); }\n"
            + "    log('done');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <ul id='birds'>\n"
            + "    <li>Great white pelican</li>\n"
            + "  </ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
