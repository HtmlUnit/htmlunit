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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE10;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest;

/**
 * Tests for {@link Element}.
 *
 * @version $Revision$
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
    @Alerts(DEFAULT = { "1", "attrName attrValue", "attrValue", "null", "anotherValue",
                    "1", "4", "<span id='label'>changed</span>" })
    public void attributes() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "    var attributes = doc.documentElement.attributes;\n"
            + "    alert(attributes.length);\n"
            + "    alert(attributes[0].name + ' ' + attributes[0].value);\n"
            + "    var root = doc.documentElement;\n"
            + "    alert(root.getAttribute('attrName'));\n"
            + "    alert(root.getAttribute('notExisting'));\n"
            + "    root.setAttribute('attrName', 'anotherValue');\n"
            + "    alert(root.getAttribute('attrName'));\n"
            + "    alert(root.getElementsByTagName('book').length);\n"
            + "    var description = root.getElementsByTagName('description')[0];\n"
            + "    alert(description.firstChild.nodeType);\n"
            + "    alert(description.firstChild.nodeValue);\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts(DEFAULT = { "1", "title" },
            IE10 = "exception")
    public void selectNodes() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      var nodes = doc.documentElement.selectNodes('//title');\n"
            + "      alert(nodes.length);\n"
            + "      alert(nodes[0].tagName);\n"
            + "    } catch (e) { alert('exception'); }\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "1" })
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void removeChild() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var parent = doc.documentElement.firstChild;\n"
            + "    alert(parent.childNodes.length);\n"
            + "    parent.removeChild(parent.firstChild);\n"
            + "    alert(parent.childNodes.length);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<books><book><title>Immortality</title><author>John Smith</author></book></books>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "lbl_SettingName", "outerHTML", "undefined" },
            IE8 = { "lbl_SettingName", "outerHTML", "Item" })
    public void getAttributeNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    parseXML(doc);\n"
            + "  }\n"
            + "  function parseXML(xml) {\n"
            + "    if (xml.documentElement.hasChildNodes()) {\n"
            + "      for (var i = 0; i < xml.documentElement.childNodes.length; i++) {\n"
            + "        var elem = xml.documentElement.childNodes.item( i );\n"
            + "        if (elem.nodeName == 'control') {\n"
            + "          var target = elem.getAttributeNode('id').value;\n"
            + "          if(document.all(target) != null) {\n"
            + "            for (var j = 0; j < elem.childNodes.length; j++) {\n"
            + "              var node = elem.childNodes.item(j);\n"
            + "              if (node.nodeName == 'tag') {\n"
            + "                var type = node.getAttributeNode('type').value;\n"
            + "                alert(target);\n"
            + "                alert(type);\n"
            + "                alert(node.text);\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts(DEFAULT = { "book", "0", "1" },
            IE10 = { "book", "exception" })
    public void selectNode_root() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var child = doc.documentElement.firstChild;\n"
            + "    alert(child.tagName);\n"
            + "    try {\n"
            + "      alert(child.selectNodes('/title').length);\n"
            + "      alert(child.selectNodes('title').length);\n"
            + "    } catch (e) { alert('exception'); }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<books><book><title>Immortality</title><author>John Smith</author></book></books>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1" },
            IE8 = "exception")
    public void getElementsByTagNameNS() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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
            + "      alert(doc.documentElement.getElementsByTagNameNS('http://myNS', 'template').length);\n"
            + "      alert(doc.documentElement.getElementsByTagNameNS(null, 'html').length);\n"
            + "    } catch (e) { alert('exception'); }\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ FF, IE10 })
    @Alerts("false")
    public void hasAttribute() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "    alert(doc.documentElement.hasAttribute('something'));\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void attributes2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('library') != undefined);\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined" },
            IE8 = { "<a><b c=\"d\">e</b></a>\r\n", "<a><b c=\"d\">e</b></a>" })
    public void xml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var text = '<a><b c=\"d\">e</b></a>';\n"
            + "  var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "  alert(doc.xml);\n"
            + "  alert(doc.documentElement.xml);\n"
            + "}\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV" },
            IE8 = { "[object],DIV",
                "[object],APP:DIV",
                "createElementNS() is not defined",
                "[object],DIV",
                "[object],APP:DIV" })
    public void html_nodeName() throws Exception {
        html("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV" },
            IE8 = { "[object],DIV",
                "[object],APP:DIV",
                "createElementNS() is not defined",
                "[object],DIV",
                "[object],APP:DIV" })
    public void html_tagName() throws Exception {
        html("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object Element],app",
                "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null" },
            IE8 = { "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined" })
    public void html_prefix() throws Exception {
        html("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLDivElement],div",
                "[object HTMLUnknownElement],app:div",
                "[object Element],dIv",
                "[object HTMLDivElement],div",
                "[object HTMLUnknownElement],app:div" },
            IE8 = { "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined" })
    @NotYetImplemented(FF)
    public void html_localName() throws Exception {
        html("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLDivElement],undefined",
                "[object HTMLUnknownElement],undefined",
                "[object Element],undefined",
                "[object HTMLDivElement],undefined",
                "[object HTMLUnknownElement],undefined" },
            IE8 = { "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined" })
    public void html_baseName() throws Exception {
        html("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLDivElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml",
                "[object Element],http://www.appcelerator.org",
                "[object HTMLDivElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml" },
            IE8 = { "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined" })
    @NotYetImplemented(FF)
    public void html_namespaceURI() throws Exception {
        html("namespaceURI");
    }

    private void html(final String methodName) throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      debug(document.createElement('dIv'));\n"
            + "      debug(document.createElement('app:dIv'));\n"
            + "      debug(document.createElementNS('http://www.appcelerator.org', 'app:dIv'));\n"
            + "    } catch (e) {alert('createElementNS() is not defined')}\n"
            + "    debug(document.getElementById('dIv1'));\n"
            + "    debug(document.getElementById('dIv2'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    alert(e + ',' + e." + methodName + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<dIv id='dIv1'></dIv>\n"
            + "<app:dIv id='dIv2'>alert(2)</app:dIv>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV"
                },
            IE8 = {"[object],DIV",
                "[object],dIv",
                "[object],ANOTHER:DIV",
                "createElementNS() is not defined",
                "[object],DIV",
                "[object],dIv",
                "[object],ANOTHER:DIV"
                })
    public void namespace_nodeName() throws Exception {
        namespace("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV"
                },
            IE8 = {"[object],DIV",
                "[object],dIv",
                "[object],ANOTHER:DIV",
                "createElementNS() is not defined",
                "[object],DIV",
                "[object],dIv",
                "[object],ANOTHER:DIV"
                })
    public void namespace_tagName() throws Exception {
        namespace("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object HTMLUnknownElement],null",
                "[object Element],app",
                "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object HTMLUnknownElement],null"
                },
            IE8 = {"[object],undefined",
                "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined",
                "[object],undefined"
                })
    public void namespace_prefix() throws Exception {
        namespace("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLDivElement],div",
                "[object HTMLUnknownElement],app:div",
                "[object HTMLUnknownElement],another:div",
                "[object Element],dIv",
                "[object HTMLDivElement],div",
                "[object HTMLUnknownElement],app:div",
                "[object HTMLUnknownElement],another:div"
                },
            IE8 = {"[object],undefined",
                "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined",
                "[object],undefined"
                })
    @NotYetImplemented(FF)
    public void namespace_localName() throws Exception {
        namespace("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLDivElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml",
                "[object Element],http://www.appcelerator.org",
                "[object HTMLDivElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement],http://www.w3.org/1999/xhtml"
                },
            IE8 = {"[object],undefined",
                "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined",
                "[object],undefined"
                })
    @NotYetImplemented(FF)
    public void namespace_namespaceURI() throws Exception {
        namespace("namespaceURI");
    }

    private void namespace(final String methodName) throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      debug(document.createElement('dIv'));\n"
            + "      debug(document.createElement('app:dIv'));\n"
            + "      debug(document.createElement('another:dIv'));\n"
            + "      debug(document.createElementNS('http://www.appcelerator.org', 'app:dIv'));\n"
            + "    } catch (e) {alert('createElementNS() is not defined')}\n"
            + "    debug(document.getElementById('dIv1'));\n"
            + "    debug(document.getElementById('dIv2'));\n"
            + "    debug(document.getElementById('dIv3'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    alert(e + ',' + e." + methodName + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<dIv id='dIv1'></dIv>\n"
            + "<app:dIv id='dIv2'></app:dIv>\n"
            + "<another:dIv id='dIv3'></another:dIv>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Element]", "dIv",
                "[object HTMLHtmlElement]", "html",
                "[object HTMLDivElement]", "div",
                "[object HTMLUnknownElement]", "dIv"
                },
            IE8 = {"[object]", "dIv",
                "[object]", "html",
                "[object]", "div",
                "[object]", "dIv"
                })
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void xml_nodeName() throws Exception {
        xml("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Element]", "dIv",
                "[object HTMLHtmlElement]", "html",
                "[object HTMLDivElement]", "div",
                "[object HTMLUnknownElement]", "dIv"
                },
            IE8 = {"[object]", "dIv",
                "[object]", "html",
                "[object]", "div",
                "[object]", "dIv"
                })
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void xml_tagName() throws Exception {
        xml("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Element]", "null",
                "[object HTMLHtmlElement]", "null",
                "[object HTMLDivElement]", "null",
                "[object HTMLUnknownElement]", "null"
                },
            IE8 = {"[object]", "",
                "[object]", "",
                "[object]", "",
                "[object]", ""
                })
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void xml_prefix() throws Exception {
        xml("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Element]", "dIv",
                "[object HTMLHtmlElement]", "html",
                "[object HTMLDivElement]", "div",
                "[object HTMLUnknownElement]", "dIv"
                },
            IE8 = {"[object]", "undefined",
                "[object]", "undefined",
                "[object]", "undefined",
                "[object]", "undefined"
                })
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void xml_localName() throws Exception {
        xml("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Element]", "undefined",
                "[object HTMLHtmlElement]", "undefined",
                "[object HTMLDivElement]", "undefined",
                "[object HTMLUnknownElement]", "undefined"
                },
            IE8 = {"[object]", "dIv",
                "[object]", "html",
                "[object]", "div",
                "[object]", "dIv"
                })
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void xml_baseName() throws Exception {
        xml("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Element]", "null",
                "[object HTMLHtmlElement]", "http://www.w3.org/1999/xhtml",
                "[object HTMLDivElement]", "http://www.w3.org/1999/xhtml",
                "[object HTMLUnknownElement]", "http://www.w3.org/1999/xhtml"
                },
            IE8 = {"[object]", "",
                "[object]", "http://www.w3.org/1999/xhtml",
                "[object]", "http://www.w3.org/1999/xhtml",
                "[object]", "http://www.w3.org/1999/xhtml"
                })
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void xml_namespaceURI() throws Exception {
        xml("namespaceURI");
    }

    private void xml(final String methodName) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "        debug(doc.documentElement.childNodes[0]);\n"
            + "        debug(doc.documentElement.childNodes[1]);\n"
            + "        debug(doc.documentElement.childNodes[1].childNodes[0]);\n"
            + "        debug(doc.documentElement.childNodes[1].childNodes[1]);\n"
            + "      }\n"
            + "      function debug(e) {\n"
            + "        try {\n"
            + "          alert(e);\n"
            + "        } catch(ex) {alert(ex)};\n"
            + "        alert(e." + methodName + ");\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "prototype found", "" },
            FF24 = {    "prototype found",
                    "ELEMENT_NODEATTRIBUTE_NODETEXT_NODECDATA_SECTION_NODEENTITY_REFERENCE_"
                    + "NODEENTITY_NODEPROCESSING_INSTRUCTION_NODECOMMENT_NODEDOCUMENT_NODEDOCUMENT_"
                    + "TYPE_NODEDOCUMENT_FRAGMENT_NODENOTATION_NODEDOCUMENT_POSITION_DISCONNECTEDDOCUMENT_"
                    + "POSITION_PRECEDINGDOCUMENT_POSITION_FOLLOWINGDOCUMENT_POSITION_"
                    + "CONTAINSDOCUMENT_POSITION_CONTAINED_BYDOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC" },
            IE8 = "exception occured")
    public void enumeratedProperties() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var str = '';\n"
            + "    try {\n"
            + "      alert(Element.prototype ? 'prototype found' : 'prototype not found');\n"
            + "      var str = '';\n"
            + "      for (var i in Element)\n"
            + "        str += i;\n"
            + "      alert(str);\n"
            + "    } catch (e) { alert('exception occured')}\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='foo' type='button' value='someValue'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "      function test() {\n"
            + "        var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "        var e = doc.getElementsByTagName('title');\n"
            + "        e[0].removeAttribute('hello');\n"
            + "        alert('finished');\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "exception")
    public void getBoundingClientRect() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  alert(typeof Element.prototype.getBoundingClientRect);\n"
            + "} catch (e) { alert('exception');}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE8 = "1")
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
            + "      if ( window.DOMParser ) {\n"
            + "        tmp = new DOMParser();\n"
            + "        xml = tmp.parseFromString( data , 'text/xml' );\n"
            + "      } else { // IE\n"
            + "        xml = new ActiveXObject( 'Microsoft.XMLDOM' );\n"
            + "        xml.async = 'false';\n"
            + "        xml.loadXML( data );\n"
            + "      }\n"
            + "    } catch( e ) {\n"
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
    @Alerts(DEFAULT = { "first", "third", "3", "second", "second" },
            IE8 = "")
    public void firstElementChild() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    if (e.firstElementChild) {\n"
            + "      alert(e.firstElementChild.id);\n"
            + "      alert(e.lastElementChild.id);\n"
            + "      alert(e.childElementCount);\n"
            + "      alert(e.firstElementChild.nextElementSibling.id);\n"
            + "      alert(e.lastElementChild.previousElementSibling.id);\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "§§URL§§", "§§URL§§" },
            IE10 = { "undefined", "undefined" })
    public void baseURI() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>foo</title><script>\n"
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
    @Alerts(FF = { "[object HTMLCollection]", "[object HTMLCollection]" },
            IE = { "undefined", "[object HTMLCollection]" })
    public void children() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var text = '<hello><child></child></hello>';\n"
            + "  var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "  alert(doc.documentElement.children);\n"
            + "  alert(document.documentElement.children);\n"
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
    @Alerts(FF = { "null", "a b c" },
            IE = { "undefined", "undefined" },
            IE10 = { "undefined", "a b c" })
    public void classList() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var text = '<hello><child></child></hello>';\n"
            + "  var doc = " + XMLDocumentTest.callLoadXMLDocumentFromString("text") + ";\n"
            + "  alert(doc.documentElement.classList);\n"
            + "  alert(document.body.classList);\n"
            + "}\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head><body onload='test()' class='a b c'>\n"
            + "  <div id='myId'>abcd</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests the usage of getAttributeNS, setAttributeNS, removeAttributeNS
     * and hasAttributeNS methods on elements under Firefox.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ FF, IE10 })
    @Alerts({ "test value", "true", "false", "finished" })
    public void attributeNS() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var doc = document.implementation.createDocument(\"\", \"\", null);\n"
            + "        var element = doc.createElementNS(\'uri:test\', \'test:element\');\n"
            + "        element.setAttributeNS(\'uri:test\', \'test:attribute\', 'test value');\n"
            + "        alert(element.getAttributeNS(\'uri:test\', \'attribute\'));\n"
            + "        alert(element.hasAttributeNS(\'uri:test\', \'attribute\'));\n"
            + "        element.removeAttributeNS(\'uri:test\', \'attribute\');\n"
            + "        alert(element.hasAttributeNS(\'uri:test\', \'attribute\'));\n"
            + "        alert('finished');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "ab", "ab" })
    // TODO [IE10]SINGLE-VS-BULK test runs when executed as single but breaks as bulk
    public void removeAttribute_case_sensitive() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'foo.xml'") + ";\n"
            + "        var e = doc.getElementsByTagName('title')[0];\n"
            + "        alert(e.getAttribute('normal'));\n"
            + "        e.removeAttribute('Normal');\n"
            + "        alert(e.getAttribute('normal'));\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
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
            + "        var doc = " + XMLDocumentTest.callCreateXMLDocument() + ";\n"
            + "        var element = doc.createElement('something');\n"
            + "        var attr = doc.createAttribute('name');\n"
            + "        attr.value = 'test';\n"
            + "        element.setAttributeNode(attr);\n"
            + "        alert(element.getAttributeNode('name').value)\n"
            + "      }\n"
            + XMLDocumentTest.CREATE_XML_DOCUMENT_FUNCTION
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

}
