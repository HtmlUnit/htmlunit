/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.ACTIVEX_CHECK;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.CREATE_XMLDOMDOCUMENT_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.CREATE_XMLDOMDOCUMENT_FUNCTION_NAME;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMDocument}.
 *
 * @version $Revision$
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLDOMDocumentTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "[object Object]")
    public void createDocument_Microsoft_XMLDOM() throws Exception {
        createDocument("Microsoft.XMLDOM");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "[object Object]")
    public void createDocument_Msxml2_DOMDocument_3() throws Exception {
        createDocument("Msxml2.DOMDocument.3.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "[object Object]")
    public void createDocument_Msxml2_DOMDocument_6() throws Exception {
        createDocument("Msxml2.DOMDocument.6.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "[object Object]")
    public void createDocument_Msxml2_FreeThreadedDOMDocument_3() throws Exception {
        createDocument("Msxml2.FreeThreadedDOMDocument.3.0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "[object Object]")
    public void createDocument_Msxml2_FreeThreadedDOMDocument_6() throws Exception {
        createDocument("Msxml2.FreeThreadedDOMDocument.6.0");
    }

    private void createDocument(final String activeXName) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    alert(Object.prototype.toString.call(doc));\n"
            + "  }\n"
            + "  function " + CREATE_XMLDOMDOCUMENT_FUNCTION_NAME + "() {\n"
            + "    return new ActiveXObject('" + activeXName + "');\n"
            + "  }\n";

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "[object Object]")
    public void scriptableToString() throws Exception {
        tester_create("alert(Object.prototype.toString.call(doc));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "true", "false" })
    public void async() throws Exception {
        final String test = ""
            + "try {\n"
            + "  var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "  alert(doc.async);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  doc.async = false;\n"
            + "  alert(doc.async);\n"
            + "} catch(e) { alert('exception-write'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void attributes() throws Exception {
        property_create("attributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "")
    public void baseName() throws Exception {
        property_create("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "4", "#comment(8)=child-comment", "true", "child-element(1)=null", "true",
                        "child-instr(7)=", "true", "xml(7)=version=\"1.0\"", "true" })
    public void childNodes() throws Exception {
        final String test = ""
            + "var comment = doc.createComment('child-comment');\n"
            + "doc.appendChild(comment);\n"
            + "var element = doc.createElement('child-element');\n"
            + "doc.appendChild(element);\n"
            + "var fragment = doc.createDocumentFragment();\n"
            + "doc.appendChild(fragment);\n"
            + "var instr = doc.createProcessingInstruction('child-instr', '');\n"
            + "doc.appendChild(instr);\n"
            + "var xmlDecl = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "doc.appendChild(xmlDecl);\n"
            + "alert(doc.childNodes.length);\n"
            // comment
            + "debug(doc.childNodes[0]);\n"
            + "alert(doc.childNodes[0] === comment);\n"
            // element
            + "debug(doc.childNodes[1]);\n"
            + "alert(doc.childNodes[1] === element);\n"
            // no fragment
            // processing instruction
            + "debug(doc.childNodes[2]);\n"
            + "alert(doc.childNodes[2] === instr);\n"
            // XML declaration
            + "debug(doc.childNodes[3]);\n"
            + "alert(doc.childNodes[3] === xmlDecl);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "0")
    public void childNodes_none() throws Exception {
        tester_create("alert(doc.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void dataType() throws Exception {
        property_create("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void definition() throws Exception {
        property_create("definition");
    }

    /**
     * Further tests see {@link XMLDOMDocumentTypeTest}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "true", "exception-write" })
    public void doctype_created() throws Exception {
        final String test = ""
            + "try {\n"
            + "  var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "  alert(doc.doctype == null);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  doc.doctype = null;\n"
            + "} catch(e) { alert('exception-write'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "false", "exception-write" })
    public void doctype_load() throws Exception {
        final String test = ""
            + "try {\n"
            + "  alert(doc.doctype == null);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  doc.doctype = null;\n"
            + "} catch(e) { alert('exception-write'); }\n";

        final String xml = "<!DOCTYPE a [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>\n"
            + "<a><b>1</b><b>2</b></a>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "true", "true", "exception-setNull" })
    public void documentElement() throws Exception {
        final String test = ""
            + "alert(doc.documentElement == null);\n"
            // normal
            + "var element = doc.createElement('foo');\n"
            + "doc.documentElement = element;\n"
            + "alert(doc.documentElement === element);\n"
            // null
            + "try {\n"
            + "  doc.documentElement = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "true", "<new/>\r\n" })
    public void documentElement_replaceExisting() throws Exception {
        final String test = ""
            + "doc.documentElement = doc.createElement('foo');\n"
            + "doc.documentElement.appendChild(doc.createElement('bar'));\n"
            + "var element = doc.createElement('new');\n"
            + "doc.documentElement = element;\n"
            + "alert(doc.documentElement === element);\n"
            + "alert(doc.xml);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void documentElement_setElementFromOtherDocument() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc1 = " + callCreateXMLDOMDocument() + ";\n"
            + "    var doc2 = " + callCreateXMLDOMDocument() + ";\n"
            + "    var element = doc1.createElement('foo');\n"
            + "    try {\n"
            + "      doc2.documentElement = element;\n"
            + "      alert(doc2.documentElement === element);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "foo(1)=null")
    public void documentElement_comment() throws Exception {
        final String xml = ""
            + "<!--comment-->\n"
            + "<foo>\n"
            + "    <foofoo name='first'>something</foofoo>\n"
            + "    <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        tester("debug(doc.documentElement);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "a(1)=null")
    public void documentElement_documentType() throws Exception {
        final String xml = ""
            + "<!DOCTYPE a [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>\n"
            + "<a><b>1</b><b>2</b></a>";

        tester("debug(doc.documentElement);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "foo(1)=null")
    public void documentElement_element() throws Exception {
        final String xml = ""
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        tester("debug(doc.documentElement);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "foo(1)=null")
    public void documentElement_processingInstruction() throws Exception {
        final String xml = ""
             + "<?apache include file=\"header.html\" ?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        tester("debug(doc.documentElement);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "foo(1)=null")
    public void documentElement_xmlDeclaration() throws Exception {
        final String xml = ""
             + "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        tester("debug(doc.documentElement);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "true", "exception-setNull", "exception-setEmpty", "exception-set" })
    public void firstChild() throws Exception {
        final String test = ""
            + "alert(doc.firstChild == null);\n"
            // null
            + "try {\n"
            + "  doc.firstChild = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  doc.firstChild = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "doc.firstChild = doc.createElement('foo');\n"
            + "} catch(e) { alert('exception-set'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "foo(1)=null")
    public void firstChild_element() throws Exception {
        final String xml = ""
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        tester("debug(doc.firstChild);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "xml(7)=version=\"1.0\"")
    @NotYetImplemented(IE)
    // Xerces does not offer any way to access the XML declaration
    public void firstChild_xmlDeclaration() throws Exception {
        final String xml = ""
             + "<?xml version=\"1.0\"?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        tester("debug(doc.firstChild);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "apache(7)=include file=\"header.html\" ")
    public void firstChild_processingInstruction() throws Exception {
        final String xml = ""
             + "<?apache include file=\"header.html\" ?>\n"
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        tester("debug(doc.firstChild);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "a(10)=null")
    public void firstChild_documentType() throws Exception {
        final String xml = ""
            + "<!DOCTYPE a [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>\n"
            + "<a><b>1</b><b>2</b></a>";

        tester("debug(doc.firstChild);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "#comment(8)=comment")
    public void firstChild_comment() throws Exception {
        final String xml = ""
            + "<!--comment-->\n"
            + "<foo>\n"
            + "    <foofoo name='first'>something</foofoo>\n"
            + "    <foofoo name='second'>something else</foofoo>\n"
            + "</foo>";

        tester("debug(doc.firstChild);\n", xml);
    }

    /**
     * Further tests see {@link XMLDOMImplementationTest}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "false", "exception-write" })
    public void implementation() throws Exception {
        final String test = ""
            + "try {\n"
            + "  alert(doc.implementation == null);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  doc.implementation = null;\n"
            + "} catch(e) { alert('exception-write'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "true", "exception-setNull", "exception-setEmpty", "exception-set" })
    public void lastChild() throws Exception {
        final String test = ""
            + "alert(doc.lastChild == null);\n"
            // null
            + "try {\n"
            + "  doc.lastChild = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  doc.lastChild = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "doc.lastChild = doc.createElement('foo');\n"
            + "} catch(e) { alert('exception-set'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "foo(1)=null")
    public void lastChild_element() throws Exception {
        final String xml = ""
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>";

        tester("debug(doc.lastChild);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "xml(7)=version=\"1.0\"")
    @NotYetImplemented(IE)
    // Xerces does not offer any way to access the XML declaration
    public void lastChild_xmlDeclaration() throws Exception {
        final String xml = ""
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>"
             + "<?xml version=\"1.0\"?>\n";

        tester("debug(doc.lastChild);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "apache(7)=include file=\"header.html\" ")
    public void lastChild_processingInstruction() throws Exception {
        final String xml = ""
             + "<foo>\n"
             + "    <foofoo name='first'>something</foofoo>\n"
             + "    <foofoo name='second'>something else</foofoo>\n"
             + "</foo>"
             + "<?apache include file=\"header.html\" ?>\n";

        tester("debug(doc.lastChild);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "false")
    // Cannot have a DOCTYPE declaration outside of a prolog.
    public void lastChild_documentType() throws Exception {
        final String xml = ""
            + "<a><b>1</b><b>2</b></a>"
            + "<!DOCTYPE a [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>\n";

        tester("alert(doc.parseError.reason === '');\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "#comment(8)=comment")
    public void lastChild_comment() throws Exception {
        final String xml = ""
            + "<foo>\n"
            + "    <foofoo name='first'>something</foofoo>\n"
            + "    <foofoo name='second'>something else</foofoo>\n"
            + "</foo>"
            + "<!--comment-->\n";

        tester("debug(doc.lastChild);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "")
    public void namespaceURI() throws Exception {
        property_create("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void nextSibling() throws Exception {
        tester("alert(doc.nextSibling == null);\n", "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void nextSibling_created() throws Exception {
        tester_create("alert(doc.nextSibling == null);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "#document")
    public void nodeName() throws Exception {
        property_create("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "9")
    public void nodeType() throws Exception {
        property_create("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "null", "exception-setNull", "exception-setEmpty", "exception-set" })
    public void nodeValue() throws Exception {
        final String test = ""
            + "alert(doc.nodeValue);\n"
            // null
            + "try {\n"
            + "  doc.nodeValue = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  doc.nodeValue = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  doc.nodeValue = 'test';\n"
            + "} catch(e) { alert('exception-set'); }\n";

        tester(test, "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void ownerDocument() throws Exception {
        tester("alert(doc.ownerDocument == null);\n", "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void ownerDocument_created() throws Exception {
        tester_create("alert(doc.ownerDocument == null);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void parentNode() throws Exception {
        tester("alert(doc.parentNode == null);\n", "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void parentNode_created() throws Exception {
        tester_create("alert(doc.parentNode == null);\n");
    }

    /**
     * Further tests see {@link XMLDOMParseErrorTest}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "false", "exception-write" })
    public void parseError() throws Exception {
        final String test = ""
            + "try {\n"
            + "  alert(doc.parseError == null);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  doc.parseError = null;\n"
            + "} catch(e) { alert('exception-write'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "")
    public void prefix() throws Exception {
        property_create("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "false", "<root><test><element/></test></root>\r\n",
                   "true", "<root><test><element/></test></root>\r\n" })
    public void preserveWhiteSpace() throws Exception {
        final String test = ""
            + "doc.documentElement = doc.createElement('root');\n"
            + "var test = doc.createElement('test');\n"
            + "doc.documentElement.appendChild(test);\n"
            + "var element = doc.createElement('element');\n"
            + "test.appendChild(element);\n"
            + "try {\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  alert(doc.xml);\n"
            + "} catch(e) { alert('exception1'); }\n"
            + "try {\n"
            + "  doc.preserveWhiteSpace = true;\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  alert(doc.xml);\n"
            + "} catch(e) { alert('exception2'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "false", "false", "<root>\r\n\t<test>\r\n\t\t<element/>\r\n\t</test>\r\nA B  C\tD\r\n</root>\r\n",
                   "false", "true", "<root>\r\n\t<test>\r\n\t\t<element/>\r\n\t</test>\r\nA B  C\tD\r\n</root>\r\n",
                   "true", "false", "<root>\r\n<test>\r\n  <element/>\r\n</test>\r\nA B  C\tD\r\n</root>\r\n",
                   "true", "true", "<root>\r\n<test>\r\n  <element/>\r\n</test>\r\nA B  C\tD\r\n</root>\r\n" })
    public void preserveWhiteSpace_load() throws Exception {
        final String test = ""
            + "doc.async = false;\n"
            // read false, write false
            + "try {\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  doc.load('" + URL_SECOND + "');\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  alert(doc.xml);\n"
            + "} catch(e) { alert('exception1'); }\n"
            // read false, write true
            + "try {\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  doc.load('" + URL_SECOND + "');\n"
            + "  doc.preserveWhiteSpace = true;\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  alert(doc.xml);\n"
            + "} catch(e) { alert('exception2'); }\n"
            // read true, write false
            + "try {\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  doc.load('" + URL_SECOND + "');\n"
            + "  doc.preserveWhiteSpace = false;\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  alert(doc.xml);\n"
            + "} catch(e) { alert('exception2'); }\n"
            // read true, write true
            + "try {\n"
            + "  doc.preserveWhiteSpace = true;\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  doc.load('" + URL_SECOND + "');\n"
            + "  alert(doc.preserveWhiteSpace);\n"
            + "  alert(doc.xml);\n"
            + "} catch(e) { alert('exception2'); }\n";

        final String xml
            = "<root>\n"
            + "<test>\n"
            + "  <element/>\n"
            + "</test>\n"
            + "A B  C\tD\n"
            + "</root>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void previousSibling() throws Exception {
        tester("alert(doc.previousSibling == null);\n", "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void previousSibling_created() throws Exception {
        tester_create("alert(doc.previousSibling == null);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "grand-child-textgrand-child-cdata")
    public void text() throws Exception {
        final String xml = ""
            + "<?xml version=\"1.0\"?>\n"
            + "<?apache child-instr ?>\n"
            + "<!--child-comment-->\n"
            + "<child-element>"
            + "<grand-child-element1>grand-child-text</grand-child-element1>"
            + "<grand-child-element2><![CDATA[grand-child-cdata]]></grand-child-element2>"
            + "</child-element>";

        tester("alert(doc.text);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "", "exception-set", "exception-setEmpty", "exception-setNull" })
    public void text_set() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.text);\n"
            // normal
            + "try {\n"
            + "  doc.text = 'text';\n"
            + "} catch(e) { alert('exception-set'); }\n"
            // empty
            + "try {\n"
            + "  doc.text = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // null
            + "try {\n"
            + "  doc.text = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "")
    public void text_created() throws Exception {
        tester_create("alert(doc.text);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "§§URL§§second/")
    public void url() throws Exception {
        tester("alert(doc.url);\n", "<root/>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "§§URL§§second.xml")
    public void url_relative() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    try {\n"
            + "      alert(doc.url);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    if (e != null) {\n"
            + "      alert(e.nodeName + '(' + e.nodeType + ')=' + e.nodeValue);\n"
            + "    } else {\n"
            + "      alert('null');\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse("<root/>", "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "")
    public void url_error() throws Exception {
        tester("alert(doc.url);\n", "<root>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "")
    public void url_created() throws Exception {
        tester_create("alert(doc.url);\n");
    }

    /**
     * Further tests see {@link XMLSerializerTest}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "", "<foo/>\r\n" })
    public void xml() throws Exception {
        final String test = ""
            + "alert(doc.xml);\n"
            + "doc.documentElement = doc.createElement('foo');\n"
            + "alert(doc.xml);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                        "exception-appendNull",
                        "exception-appendEmpty" })
    public void appendChild() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // null
            + "try {\n"
            + "  doc.appendChild(null);\n"
            + "} catch(e) { alert('exception-appendNull'); }\n"
            // empty
            + "try {\n"
            + "  doc.appendChild('');\n"
            + "} catch(e) { alert('exception-appendEmpty'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true", "exception-appendCDATA1" })
    public void appendChild_cdata() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // cdata 1
            + "try {\n"
            + "  doc.appendChild(doc.createCDATASection('cdata1'));\n"
            + "} catch(e) { alert('exception-appendCDATA1'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "1", "true", "true", "true",
                   "2", "true", "true", "true", "true", "true" })
    public void appendChild_comment() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // comment 1
            + "var comment1 = doc.createComment('comment1');\n"
            + "doc.appendChild(comment1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === comment1);\n"
            + "alert(doc.lastChild === comment1);\n"
            // comment 2
            + "var comment2 = doc.createComment('comment2');\n"
            + "doc.appendChild(comment2);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === comment1);\n"
            + "alert(doc.firstChild.nextSibling === comment2);\n"
            + "alert(doc.lastChild === comment2);\n"
            + "alert(doc.lastChild.previousSibling === comment1);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "0", "true", "true", "true",
                   "1", "true", "true", "true" })
    public void appendChild_documentFragment() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // empty
            + "var fragment1 = doc.createDocumentFragment();\n"
            + "doc.appendChild(fragment1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild == null);\n"
            + "alert(doc.lastChild == null);\n"
            // element
            + "var fragment2 = doc.createDocumentFragment();\n"
            + "var element1 = doc.createElement('element1');\n"
            + "fragment2.appendChild(element1);\n"
            + "doc.appendChild(fragment2);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element1);\n"
            + "alert(doc.firstChild === element1);\n"
            + "alert(doc.lastChild === element1);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "exception-appendFragment",
                   "0", "true", "true", "true" })
    public void appendChild_documentFragment_cdata() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "var fragment = doc.createDocumentFragment();\n"
            + "fragment.appendChild(doc.createCDATASection('cdata1'));\n"
            + "try {\n"
            + "  doc.appendChild(fragment);\n"
            + "} catch(e) { alert('exception-appendFragment'); }\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild == null);\n"
            + "alert(doc.lastChild == null);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "exception-appendFragment",
                   "0", "true", "true", "true" })
    public void appendChild_documentFragment_text() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "var fragment = doc.createDocumentFragment();\n"
            + "fragment.appendChild(doc.createTextNode('text1'));\n"
            + "try {\n"
            + "  doc.appendChild(fragment);\n"
            + "} catch(e) { alert('exception-appendFragment'); }\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild == null);\n"
            + "alert(doc.lastChild == null);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "exception-appendFragment",
                   "0", "true", "true", "true" })
    public void appendChild_documentFragment_multipleElement() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "var fragment = doc.createDocumentFragment();\n"
            + "fragment.appendChild(doc.createElement('element1'));\n"
            + "fragment.appendChild(doc.createElement('element2'));\n"
            + "try {\n"
            + "  doc.appendChild(fragment);\n"
            + "} catch(e) { alert('exception-appendFragment'); }\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild == null);\n"
            + "alert(doc.lastChild == null);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "1", "true", "true", "true",
                   "exception-appendElement2",
                   "2", "true", "true", "true", "true", "true" })
    public void appendChild_element() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // element 1
            + "var element1 = doc.createElement('element1');\n"
            + "doc.appendChild(element1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element1);\n"
            + "alert(doc.firstChild === element1);\n"
            + "alert(doc.lastChild === element1);\n"
            // element 2
            + "try {\n"
            + "  doc.appendChild(doc.createElement('element2'));\n"
            + "} catch(e) { alert('exception-appendElement2'); }\n"
            // other node
            + "var comment = doc.createComment('comment');\n"
            + "doc.appendChild(comment);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element1);\n"
            + "alert(doc.firstChild === element1);\n"
            + "alert(doc.firstChild.nextSibling === comment);\n"
            + "alert(doc.lastChild === comment);\n"
            + "alert(doc.lastChild.previousSibling === element1);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true", "exception-appendText1" })
    public void appendChild_text() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // text 1
            + "try {\n"
            + "  doc.appendChild(doc.createTextNode('text1'));\n"
            + "} catch(e) { alert('exception-appendText1'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "1", "true", "true", "true",
                   "2", "true", "true", "true", "true", "true" })
    public void appendChild_processingInstruction() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // processing instruction 1
            + "var instr1 = doc.createProcessingInstruction('instr1', '');\n"
            + "doc.appendChild(instr1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === instr1);\n"
            + "alert(doc.lastChild === instr1);\n"
            // processing instruction 2
            + "var instr2 = doc.createProcessingInstruction('instr2', '');\n"
            + "doc.appendChild(instr2);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === instr1);\n"
            + "alert(doc.firstChild.nextSibling === instr2);\n"
            + "alert(doc.lastChild === instr2);\n"
            + "alert(doc.lastChild.previousSibling === instr1);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "1", "true", "true", "true",
                   "2", "true", "true", "true", "true", "true" })
    public void appendChild_xmlDeclaration() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // XML declaration 1
            + "var xmlDecl1 = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "doc.appendChild(xmlDecl1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === xmlDecl1);\n"
            + "alert(doc.lastChild === xmlDecl1);\n"
            // XML declaration 2
            + "var xmlDecl2 = doc.createProcessingInstruction('xml', 'version=\"1.0\" standalone=\"yes\"');\n"
            + "doc.appendChild(xmlDecl2);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === xmlDecl1);\n"
            + "alert(doc.firstChild.nextSibling === xmlDecl2);\n"
            + "alert(doc.lastChild === xmlDecl2);\n"
            + "alert(doc.lastChild.previousSibling === xmlDecl1);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "[object Object]", "foo(2)=", "true", "true",
                   "exception-createNull", "exception-createEmpty", "exception-createBlank", "exception-createXML" })
    public void createAttribute() throws Exception {
        final String test = ""
            // normal
            + "var attr = doc.createAttribute('foo');\n"
            + "alert(Object.prototype.toString.call(attr));\n"
            + "debug(attr);\n"
            + "alert(attr.ownerDocument === doc);\n"
            + "alert(attr.parentNode == null);\n"
            // null
            + "try {\n"
            + "  doc.createAttribute(null);\n"
            + "} catch(e) { alert('exception-createNull'); }\n"
            // empty
            + "try {\n"
            + "  doc.createAttribute('');\n"
            + "} catch(e) { alert('exception-createEmpty'); }\n"
            // blank
            + "try {\n"
            + "  doc.createAttribute(' ');\n"
            + "} catch(e) { alert('exception-createBlank'); }\n"
            // xml
            + "try {\n"
            + "  doc.createAttribute('<tag/>');\n"
            + "} catch(e) { alert('exception-createXML'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "FOO", "fOo", "Foo" })
    public void createAttribute_caseSensitive() throws Exception {
        final String test = ""
            + "var attr = doc.createAttribute('FOO');\n"
            + "alert(attr.nodeName);\n"
            + "attr = doc.createAttribute('fOo');\n"
            + "alert(attr.nodeName);\n"
            + "attr = doc.createAttribute('Foo');\n"
            + "alert(attr.nodeName);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "[object Object]", "foo(1)=null", "true", "true",
                   "exception-createNull", "exception-createEmpty", "exception-createBlank", "exception-createXML" })
    public void createElement() throws Exception {
        final String test = ""
            // normal
            + "var element = doc.createElement('foo');\n"
            + "alert(Object.prototype.toString.call(element));\n"
            + "debug(element);\n"
            + "alert(element.ownerDocument === doc);\n"
            + "alert(element.parentNode == null);\n"
            // null
            + "try {\n"
            + "  doc.createElement(null);\n"
            + "} catch(e) { alert('exception-createNull'); }\n"
            // empty
            + "try {\n"
            + "  doc.createElement('');\n"
            + "} catch(e) { alert('exception-createEmpty'); }\n"
            // blank
            + "try {\n"
            + "  doc.createElement(' ');\n"
            + "} catch(e) { alert('exception-createBlank'); }\n"
            // xml
            + "try {\n"
            + "  doc.createElement('<tag/>');\n"
            + "} catch(e) { alert('exception-createXML'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = { "FOO", "fOo", "Foo" })
    public void createElement_caseSensitive() throws Exception {
        final String test = ""
            + "var element = doc.createElement('FOO');\n"
            + "alert(element.nodeName);\n"
            + "element = doc.createElement('fOo');\n"
            + "alert(element.nodeName);\n"
            + "element = doc.createElement('Foo');\n"
            + "alert(element.nodeName);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "exception-insertNull",
                   "exception-insertEmpty" })
    public void insertBefore() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // null
            + "try {\n"
            + "  doc.insertBefore(null, null);\n"
            + "} catch(e) { alert('exception-insertNull'); }\n"
            // empty
            + "try {\n"
            + "  doc.insertBefore('', null);\n"
            + "} catch(e) { alert('exception-insertEmpty'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true", "exception-insertCDATA1" })
    public void insertBefore_cdata() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // cdata 1
            + "try {\n"
            + "  doc.insertBefore(doc.createCDATASection('cdata1'), null);\n"
            + "} catch(e) { alert('exception-insertCDATA1'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "1", "true", "true", "true",
                   "2", "true", "true", "true", "true", "true",
                   "3", "true", "true", "true", "true", "true",
                   "exception-insertComment4" })
    public void insertBefore_comment() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // comment 1
            + "var comment1 = doc.createComment('comment1');\n"
            + "doc.insertBefore(comment1, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === comment1);\n"
            + "alert(doc.lastChild === comment1);\n"
            // comment 2 (ref null)
            + "var comment2 = doc.createComment('comment2');\n"
            + "doc.insertBefore(comment2, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === comment1);\n"
            + "alert(doc.firstChild.nextSibling === comment2);\n"
            + "alert(doc.lastChild === comment2);\n"
            + "alert(doc.lastChild.previousSibling === comment1);\n"
            // comment 3 (ref comment1)
            + "var comment3 = doc.createComment('comment3');\n"
            + "doc.insertBefore(comment3, comment1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === comment3);\n"
            + "alert(doc.firstChild.nextSibling === comment1);\n"
            + "alert(doc.lastChild === comment2);\n"
            + "alert(doc.lastChild.previousSibling === comment1);\n"
            // comment 4 (no ref)
            + "try {\n"
            + "  doc.insertBefore(doc.createComment('comment4'));\n"
            + "} catch(e) { alert('exception-insertComment4'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "0", "true", "true", "true",
                   "1", "true", "true", "true",
                   "2", "true", "true", "true", "true", "true",
                   "3", "true", "true", "true", "true", "true",
                   "exception-insertFragment5" })
    public void insertBefore_documentFragment() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // empty
            + "var fragment1 = doc.createDocumentFragment();\n"
            + "doc.insertBefore(fragment1, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild == null);\n"
            + "alert(doc.lastChild == null);\n"
            // with element
            + "var fragment2 = doc.createDocumentFragment();\n"
            + "var element = doc.createElement('element');\n"
            + "fragment2.appendChild(element);\n"
            + "doc.insertBefore(fragment2, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element);\n"
            + "alert(doc.firstChild === element);\n"
            + "alert(doc.lastChild === element);\n"
            // with comment 1 (ref null)
            + "var fragment3 = doc.createDocumentFragment();\n"
            + "var comment1 = doc.createComment('comment1');\n"
            + "fragment3.appendChild(comment1);\n"
            + "doc.insertBefore(fragment3, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element);\n"
            + "alert(doc.firstChild === element);\n"
            + "alert(doc.firstChild.nextSibling === comment1);\n"
            + "alert(doc.lastChild === comment1);\n"
            + "alert(doc.lastChild.previousSibling === element);\n"
            // with comment 2 (ref element1)
            + "var fragment4 = doc.createDocumentFragment();\n"
            + "var comment2 = doc.createComment('comment2');\n"
            + "fragment4.appendChild(comment2);\n"
            + "doc.insertBefore(fragment4, element);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element);\n"
            + "alert(doc.firstChild === comment2);\n"
            + "alert(doc.firstChild.nextSibling === element);\n"
            + "alert(doc.lastChild === comment1);\n"
            + "alert(doc.lastChild.previousSibling === element);\n"
            // empty (no ref)
            + "try {\n"
            + "  doc.insertBefore(doc.createDocumentFragment());\n"
            + "} catch(e) { alert('exception-insertFragment5'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "exception-insertFragment",
                   "0", "true", "true", "true" })
    public void insertBefore_documentFragment_cdata() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "var fragment = doc.createDocumentFragment();\n"
            + "fragment.appendChild(doc.createCDATASection('cdata1'));\n"
            + "try {\n"
            + "  doc.insertBefore(fragment, null);\n"
            + "} catch(e) { alert('exception-insertFragment'); }\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild == null);\n"
            + "alert(doc.lastChild == null);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "exception-insertFragment",
                   "0", "true", "true", "true" })
    public void insertBefore_documentFragment_text() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "var fragment = doc.createDocumentFragment();\n"
            + "fragment.appendChild(doc.createTextNode('text1'));\n"
            + "try {\n"
            + "  doc.insertBefore(fragment, null);\n"
            + "} catch(e) { alert('exception-insertFragment'); }\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild == null);\n"
            + "alert(doc.lastChild == null);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "exception-insertFragment",
                   "0", "true", "true", "true" })
    public void insertBefore_documentFragment_multipleElement() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "var fragment = doc.createDocumentFragment();\n"
            + "fragment.appendChild(doc.createElement('element1'));\n"
            + "fragment.appendChild(doc.createElement('element2'));\n"
            + "try {\n"
            + "  doc.insertBefore(fragment, null);\n"
            + "} catch(e) { alert('exception-insertFragment'); }\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild == null);\n"
            + "alert(doc.lastChild == null);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "1", "true", "true", "true",
                   "exception-insertElement2",
                   "2", "true", "true", "true", "true", "true",
                   "3", "true", "true", "true", "true", "true" })
    public void insertBefore_element() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // element 1
            + "var element1 = doc.createElement('element1');\n"
            + "doc.insertBefore(element1, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element1);\n"
            + "alert(doc.firstChild === element1);\n"
            + "alert(doc.lastChild === element1);\n"
            // element 2
            + "try {\n"
            + "  doc.insertBefore(doc.createElement('element2'), null);\n"
            + "} catch(e) { alert('exception-insertElement2'); }\n"
            // other node (ref null)
            + "var comment1 = doc.createComment('comment1');\n"
            + "doc.insertBefore(comment1, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element1);\n"
            + "alert(doc.firstChild === element1);\n"
            + "alert(doc.firstChild.nextSibling === comment1);\n"
            + "alert(doc.lastChild === comment1);\n"
            + "alert(doc.lastChild.previousSibling === element1);\n"
            // other node (ref element 1)
            + "var comment2 = doc.createComment('comment2');\n"
            + "doc.insertBefore(comment2, element1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement === element1);\n"
            + "alert(doc.firstChild === comment2);\n"
            + "alert(doc.firstChild.nextSibling === element1);\n"
            + "alert(doc.lastChild === comment1);\n"
            + "alert(doc.lastChild.previousSibling === element1);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true", "exception-insertText1" })
    public void insertBefore_text() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // text 1
            + "try {\n"
            + "  doc.insertBefore(doc.createTextNode('text1'), null);\n"
            + "} catch(e) { alert('exception-insertText1'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "1", "true", "true", "true",
                   "2", "true", "true", "true", "true", "true",
                   "3", "true", "true", "true", "true", "true",
                   "exception-insertInstr4" })
    public void insertBefore_processingInstruction() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // processing instruction 1
            + "var instr1 = doc.createProcessingInstruction('instr1', '');\n"
            + "doc.insertBefore(instr1, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === instr1);\n"
            + "alert(doc.lastChild === instr1);\n"
            // processing instruction 2 (ref null)
            + "var instr2 = doc.createProcessingInstruction('instr2', '');\n"
            + "doc.insertBefore(instr2, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === instr1);\n"
            + "alert(doc.firstChild.nextSibling === instr2);\n"
            + "alert(doc.lastChild === instr2);\n"
            + "alert(doc.lastChild.previousSibling === instr1);\n"
            // processing instruction 3 (ref processing instruction 1)
            + "var instr3 = doc.createProcessingInstruction('instr3', '');\n"
            + "doc.insertBefore(instr3, instr1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === instr3);\n"
            + "alert(doc.firstChild.nextSibling === instr1);\n"
            + "alert(doc.lastChild === instr2);\n"
            + "alert(doc.lastChild.previousSibling === instr1);\n"
            // processing instruction 4 (no ref)
            + "try {\n"
            + "  doc.insertBefore(doc.createProcessingInstruction('instr4', ''));\n"
            + "} catch(e) { alert('exception-insertInstr4'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "0", "true",
                   "1", "true", "true", "true",
                   "2", "true", "true", "true", "true", "true",
                   "3", "true", "true", "true", "true", "true",
                   "exception-insertXMLDecl4" })
    public void insertBefore_xmlDeclaration() throws Exception {
        final String test = ""
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            // XML declaration 1
            + "var xmlDecl1 = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "doc.insertBefore(xmlDecl1, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === xmlDecl1);\n"
            + "alert(doc.lastChild === xmlDecl1);\n"
            // XML declaration 2 (ref null)
            + "var xmlDecl2 = doc.createProcessingInstruction('xml', 'version=\"1.0\" standalone=\"yes\"');\n"
            + "doc.insertBefore(xmlDecl2, null);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === xmlDecl1);\n"
            + "alert(doc.firstChild.nextSibling === xmlDecl2);\n"
            + "alert(doc.lastChild === xmlDecl2);\n"
            + "alert(doc.lastChild.previousSibling === xmlDecl1);\n"
            // XML declaration 3 (ref XML declaration 1)
            + "var xmlDecl3 = doc.createProcessingInstruction('xml', "
            + "'version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"');\n"
            + "doc.insertBefore(xmlDecl3, xmlDecl1);\n"
            + "alert(doc.childNodes.length);\n"
            + "alert(doc.documentElement == null);\n"
            + "alert(doc.firstChild === xmlDecl3);\n"
            + "alert(doc.firstChild.nextSibling === xmlDecl1);\n"
            + "alert(doc.lastChild === xmlDecl2);\n"
            + "alert(doc.lastChild.previousSibling === xmlDecl1);\n"
            // XML declaration 4 (no ref)
            + "try {\n"
            + "  doc.insertBefore(doc.createProcessingInstruction('xml', 'version=\"1.0\"'));\n"
            + "} catch(e) { alert('exception-insertXMLDecl4'); }\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "", "<foo><bar/></foo>\r\n", "foo" })
    public void loadXML() throws Exception {
        final String test = ""
            + "var text='<foo><bar/></foo>';\n"
            + "doc.async = false;\n"
            + "alert(doc.xml);\n"
            + "doc.loadXML(text);\n"
            + "alert(doc.xml);\n"
            + "alert(doc.documentElement.nodeName);\n";

        tester_create(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "<myns:foo xmlns:myns=\"http://myNS\"/>\r\n", "myns:foo" })
    public void loadXML_namespace() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var text='<myns:foo xmlns:myns=\"http://myNS\"/>';\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromString("text") + ";\n"
            + "    alert(doc.xml);\n"
            + "    alert(doc.documentElement.nodeName);\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    private void property_create(final String property) throws Exception {
        tester_create("alert(doc." + property + ");\n");
    }

    private void tester_create(final String test) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    try {\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    if (e != null) {\n"
            + "      alert(e.nodeName + '(' + e.nodeType + ')=' + e.nodeValue);\n"
            + "    } else {\n"
            + "      alert('null');\n"
            + "    }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    if (e != null) {\n"
            + "      alert(e.nodeName + '(' + e.nodeType + ')=' + e.nodeValue);\n"
            + "    } else {\n"
            + "      alert('null');\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
