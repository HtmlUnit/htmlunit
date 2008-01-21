/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XMLElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XMLElementTest extends WebTestCase {

    /**
     * Creates a new test instance.
     * @param name The name of the new test instance.
     */
    public XMLElementTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAttributes() throws Exception {
        testAttributes(BrowserVersion.INTERNET_EXPLORER_7_0);
        testAttributes(BrowserVersion.FIREFOX_2);
    }
    
    private void testAttributes(final BrowserVersion browserVersion) throws Exception {
        final String firstContent = "<html><head><title>foo</title><script>\n"
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
        
        final String secondContent
            = "<books attrName=\"attrValue\">\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "    <description><![CDATA[<span id='label'>changed</span>]]></description>\n"
            + "  </book>\n"
            + "</books>";

        final String[] expectedAlerts = {"true", "1", "attrName attrValue", "attrValue", "null", "anotherValue",
            "1", "4", "<span id='label'>changed</span>"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(new URL(URL_FIRST, "foo.xml"), secondContent, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testSelectNodes() throws Exception {
        testSelectNodes(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true", "1", "title"});
        try {
            testSelectNodes(BrowserVersion.FIREFOX_2, new String[] {"true", "1", "title"});
            fail("selectNodes is not supported in Firefox.");
        }
        catch (final Exception e) {
            //expected
        }
    }
    
    private void testSelectNodes(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testRemoveChild() throws Exception {
        testRemoveChild(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"true", "2", "1"});
        testRemoveChild(BrowserVersion.FIREFOX_2, new String[] {"true", "2", "1"});
    }
    
    private void testRemoveChild(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetAttributeNode() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        testGetAttributeNode(BrowserVersion.INTERNET_EXPLORER_7_0,
                new String[] {"lbl_SettingName", "outerHTML", "Item"});
        testGetAttributeNode(BrowserVersion.FIREFOX_2,
                new String[] {"lbl_SettingName", "outerHTML", "undefined"});
    }
    
    private void testGetAttributeNode(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
