/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link Element}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class ElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "1", "attrName attrValue", "attrValue", "null", "anotherValue",
            "1", "4", "<span id='label'>changed</span>" })
    public void attributes() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('foo.xml'));\n"
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
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "true", "1", "title" })
    public void selectNodes() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    var nodes = doc.documentElement.selectNodes('//title');\n"
            + "    alert(nodes.length);\n"
            + "    alert(nodes[0].tagName);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "2", "1" })
    public void removeChild() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    alert(doc.load('" + URL_SECOND + "'));\n"
            + "    var parent = doc.documentElement.firstChild;\n"
            + "    alert(parent.childNodes.length);\n"
            + "    parent.removeChild(parent.firstChild);\n"
            + "    alert(parent.childNodes.length);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<books><book><title>Immortality</title><author>John Smith</author></book></books>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "lbl_SettingName", "outerHTML", "Item" },
            FF = { "lbl_SettingName", "outerHTML", "undefined" })
    public void getAttributeNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    doc.load('" + URL_SECOND + "');\n"
            + "    parseXML(doc);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "book", "0", "1" })
    public void selectNode_root() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    doc.load('" + URL_SECOND + "');\n"
            + "    var child = doc.documentElement.firstChild;\n"
            + "    alert(child.tagName);\n"
            + "    alert(child.selectNodes('/title').length);\n"
            + "    alert(child.selectNodes('title').length);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<books><book><title>Immortality</title><author>John Smith</author></book></books>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "1", "1" })
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
            + "    if (window.ActiveXObject) {\n"
            + "      var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      doc.async=false;\n"
            + "      doc.loadXML(text);\n"
            + "    } else {\n"
            + "      var parser=new DOMParser();\n"
            + "      var doc=parser.parseFromString(text,'text/xml');\n"
            + "    }\n"
            + "    alert(doc.documentElement.getElementsByTagNameNS('http://myNS', 'template').length);\n"
            + "    alert(doc.documentElement.getElementsByTagNameNS(null, 'html').length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts("false")
    public void hasAttribute() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    doc.load('foo.xml');\n"
            + "    alert(doc.documentElement.hasAttribute('something'));\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void attributes2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    doc.async = false;\n"
            + "    doc.load('foo.xml');\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('library') != undefined);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "<a><b c=\"d\">e</b></a>\r\n", "<a><b c=\"d\">e</b></a>" },
            FF = { "undefined", "undefined" })
    public void xml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var text = '<a><b c=\"d\">e</b></a>';\n"
            + "  if (window.ActiveXObject) {\n"
            + "    var doc = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    doc.async = false;\n"
            + "    doc.loadXML(text);\n"
            + "  } else {\n"
            + "    var parser = new DOMParser();\n"
            + "    var doc = parser.parseFromString(text, 'text/xml');\n"
            + "  }\n"
            + "  alert(doc.xml);\n"
            + "  alert(doc.documentElement.xml);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],DIV",
                "[object],APP:DIV",
                "createElementNS() is not defined",
                "[object],DIV",
                "[object],APP:DIV"
                },
            FF = {"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV"
                })
    public void html_nodeName() throws Exception {
        html("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],DIV",
                "[object],APP:DIV",
                "createElementNS() is not defined",
                "[object],DIV",
                "[object],APP:DIV"
                },
            FF = {"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV"
                })
    public void html_tagName() throws Exception {
        html("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined"
                },
            FF = {"[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object Element],app",
                "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null"
                })
    public void html_prefix() throws Exception {
        html("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined"
                },
            FF = {"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object Element],dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV"
                })
    public void html_localName() throws Exception {
        html("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined"
                },
            FF = {"[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object Element],http://www.appcelerator.org",
                "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null"
                })
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],DIV",
                "[object],dIv",
                "[object],ANOTHER:DIV",
                "createElementNS() is not defined",
                "[object],DIV",
                "[object],dIv",
                "[object],ANOTHER:DIV"
                },
            FF = {"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV"
                })
    public void namespace_nodeName() throws Exception {
        namespace("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],DIV",
                "[object],dIv",
                "[object],ANOTHER:DIV",
                "createElementNS() is not defined",
                "[object],DIV",
                "[object],dIv",
                "[object],ANOTHER:DIV"
                },
            FF = {"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV",
                "[object Element],app:dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV"
                })
    public void namespace_tagName() throws Exception {
        namespace("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],undefined",
                "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined",
                "[object],undefined"
                },
            FF = {"[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object HTMLUnknownElement],null",
                "[object Element],app",
                "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object HTMLUnknownElement],null"
                })
    public void namespace_prefix() throws Exception {
        namespace("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],undefined",
                "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined",
                "[object],undefined"
                },
            FF = {"[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV",
                "[object Element],dIv",
                "[object HTMLDivElement],DIV",
                "[object HTMLUnknownElement],APP:DIV",
                "[object HTMLUnknownElement],ANOTHER:DIV"
                })
    public void namespace_localName() throws Exception {
        namespace("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object],undefined",
                "[object],undefined",
                "[object],undefined",
                "createElementNS() is not defined",
                "[object],undefined",
                "[object],undefined",
                "[object],undefined"
                },
            FF = {"[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object HTMLUnknownElement],null",
                "[object Element],http://www.appcelerator.org",
                "[object HTMLDivElement],null",
                "[object HTMLUnknownElement],null",
                "[object HTMLUnknownElement],null"
                })
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object]", "dIv",
            "[object]", "html",
            "[object]", "div",
            "[object]", "dIv"
            },
        FF = {"[object Element]", "dIv",
            "[object HTMLHtmlElement]", "html",
            "[object HTMLDivElement]", "div",
            "[object HTMLUnknownElement]", "dIv"
            })
    public void xml_nodeName() throws Exception {
        xml("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object]", "dIv",
            "[object]", "html",
            "[object]", "div",
            "[object]", "dIv"
            },
        FF = {"[object Element]", "dIv",
            "[object HTMLHtmlElement]", "html",
            "[object HTMLDivElement]", "div",
            "[object HTMLUnknownElement]", "dIv"
            })
    public void xml_tagName() throws Exception {
        xml("tagName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object]", "",
            "[object]", "",
            "[object]", "",
            "[object]", ""
            },
        FF = {"[object Element]", "null",
            "[object HTMLHtmlElement]", "null",
            "[object HTMLDivElement]", "null",
            "[object HTMLUnknownElement]", "null"
            })
    public void xml_prefix() throws Exception {
        xml("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object]", "undefined",
            "[object]", "undefined",
            "[object]", "undefined",
            "[object]", "undefined"
            },
        FF = {"[object Element]", "dIv",
            "[object HTMLHtmlElement]", "html",
            "[object HTMLDivElement]", "div",
            "[object HTMLUnknownElement]", "dIv"
            })
    public void xml_localName() throws Exception {
        xml("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object]", "",
            "[object]", "http://www.w3.org/1999/xhtml",
            "[object]", "http://www.w3.org/1999/xhtml",
            "[object]", "http://www.w3.org/1999/xhtml"
            },
        FF = {"[object Element]", "null",
            "[object HTMLHtmlElement]", "http://www.w3.org/1999/xhtml",
            "[object HTMLDivElement]", "http://www.w3.org/1999/xhtml",
            "[object HTMLUnknownElement]", "http://www.w3.org/1999/xhtml"
            })
    public void xml_namespaceURI() throws Exception {
        xml("namespaceURI");
    }

    private void xml(final String methodName) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.open('GET', 'foo.xml', false);\n"
            + "        request.send('');\n"
            + "        var doc = request.responseXML;\n"
            + "        debug(doc.documentElement.childNodes[0]);\n"
            + "        debug(doc.documentElement.childNodes[1]);\n"
            + "        debug(doc.documentElement.childNodes[1].childNodes[0]);\n"
            + "        debug(doc.documentElement.childNodes[1].childNodes[1]);\n"
            + "      }\n"
            + "      function debug(e) {\n"
            + "        alert(e);\n"
            + "        alert(e." + methodName + ");\n"
            + "      }\n"
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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "button", "getAttributeNS() not supported" }, FF = { "button", "", "false", "true" })
    public void attributeNS() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('foo');\n"
            + "    alert(e.getAttribute('type'));\n"
            + "    try {\n"
            + "      alert(e.getAttributeNS('bar', 'type'));\n"
            + "      alert(e.hasAttributeNS('bar', 'type'));\n"
            + "      e.removeAttributeNS('bar', 'type');\n"
            + "      alert(e.hasAttribute('type'));\n"
            + "    } catch (e) {alert('getAttributeNS() not supported')}\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='foo' type='button' value='someValue'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.FF2)
    @Alerts(IE = "exception occured",
            FF2 = { "prototype found", "QueryInterface" },
            FF3 = { "prototype found", "" })
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

        loadPageWithAlerts(html);
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
            + "        var request;\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.open('GET', 'foo.xml', false);\n"
            + "        request.send('');\n"
            + "        var doc = request.responseXML;\n"
            + "        var e = doc.getElementsByTagName('title');\n"
            + "        e[0].removeAttribute('hello');\n"
            + "        alert('finished');\n"
            + "      }\n"
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
        loadPageWithAlerts(html);
    }
}
