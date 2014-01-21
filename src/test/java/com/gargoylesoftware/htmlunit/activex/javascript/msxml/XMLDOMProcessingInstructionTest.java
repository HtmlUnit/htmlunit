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
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.CREATE_XMLDOMDOCUMENT_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMProcessingInstruction}.
 *
 * @version $Revision$
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMProcessingInstructionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(instr));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void attributes() throws Exception {
        property("attributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "1", "version=1.0" })
    @NotYetImplemented(IE)
    // DOM processing instructions do not support attributes
    public void attributes_xmlDecl() throws Exception {
        final String test = ""
            + "alert(instr.attributes.length);\n"
            + "var attr = instr.attributes[0];\n"
            + "alert(attr.nodeName + '=' + attr.nodeValue);\n";
        tester_xmlDecl(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "3", "version=1.0", "encoding=utf-8", "standalone=yes" })
    @NotYetImplemented(IE)
    // DOM processing instructions do not support attributes
    public void attributes_complete_xmlDecl() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction("
            + "'xml', 'version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"');\n"
            + "    doc.appendChild(instr);\n"
            + "    try {\n"
            + "      alert(instr.attributes.length);\n"
            + "      var attr = instr.attributes[0];\n"
            + "      alert(attr.nodeName + '=' + attr.nodeValue);\n"
            + "      attr = instr.attributes[1];\n"
            + "      alert(attr.nodeName + '=' + attr.nodeValue);\n"
            + "      attr = instr.attributes[2];\n"
            + "      alert(attr.nodeName + '=' + attr.nodeValue);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("apache")
    public void baseName() throws Exception {
        property("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("xml")
    public void baseName_xmlDecl() throws Exception {
        property_xmlDecl("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("0")
    public void childNodes() throws Exception {
        tester("alert(instr.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("0")
    public void childNodes_xmlDecl() throws Exception {
        tester_xmlDecl("alert(instr.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "include file=\"header.html\" ", "include file=\"header.html\" ", "include file=\"header.html\" ",
        "exception-setNull",
        "", "", "",
        "test", "test", "test",
        "test\ntest", "test\ntest", "test\ntest",
        "<tag/>", "<tag/>", "<tag/>" })
    public void data() throws Exception {
        final String test = ""
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.text);\n"
            // null
            + "try {\n"
            + "  instr.data = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "instr.data = '';\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.text);\n"
            // normal
            + "instr.data = 'test';\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.text);\n"
            // linebreak
            + "instr.data = 'test\\ntest';\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.text);\n"
            // xml
            + "instr.data = '<tag/>';\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.text);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "version=\"1.0\"", "version=\"1.0\"", "version=\"1.0\"",
        "exception-setNull",
        "exception-setEmpty",
        "exception-set" })
    public void data_xmlDecl() throws Exception {
        final String test = ""
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.text);\n"
            // null
            + "try {\n"
            + "  instr.data = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  instr.data = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  instr.data = 'test';\n"
            + "} catch(e) { alert('exception-set'); }\n";

        tester_xmlDecl(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void dataType() throws Exception {
        property("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void dataType_xmlDecl() throws Exception {
        property_xmlDecl("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void definition() throws Exception {
        property("definition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void definition_xmlDecl() throws Exception {
        property_xmlDecl("definition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void firstChild() throws Exception {
        property("firstChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void firstChild_xmlDecl() throws Exception {
        property_xmlDecl("firstChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void lastChild() throws Exception {
        property("lastChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void lastChild_xmlDecl() throws Exception {
        property_xmlDecl("lastChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void namespaceURI() throws Exception {
        property("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void namespaceURI_xmlDecl() throws Exception {
        property_xmlDecl("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("apache")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("xml")
    public void nodeName_xmlDecl() throws Exception {
        property_xmlDecl("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("7")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("7")
    public void nodeType_xmlDecl() throws Exception {
        property_xmlDecl("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "include file=\"header.html\" ", "include file=\"header.html\" ", "include file=\"header.html\" ",
        "exception-setNull",
        "", "", "",
        "test", "test", "test",
        "test\ntest", "test\ntest", "test\ntest",
        "<tag/>", "<tag/>", "<tag/>" })
    public void nodeValue() throws Exception {
        final String test = ""
            + "alert(instr.nodeValue);\n"
            + "alert(instr.data);\n"
            + "alert(instr.text);\n"
            // null
            + "try {\n"
            + "  instr.nodeValue = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "instr.nodeValue = '';\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.data);\n"
            + "alert(instr.text);\n"
            // normal
            + "instr.nodeValue = 'test';\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.data);\n"
            + "alert(instr.text);\n"
            // linebreak
            + "instr.nodeValue = 'test\\ntest';\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.data);\n"
            + "alert(instr.text);\n"
            // xml
            + "instr.nodeValue = '<tag/>';\n"
            + "alert(instr.nodeValue);\n"
            + "alert(instr.data);\n"
            + "alert(instr.text);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "version=\"1.0\"", "version=\"1.0\"", "version=\"1.0\"",
        "exception-setNull",
        "exception-setEmpty",
        "exception-set" })
    public void nodeValue_xmlDecl() throws Exception {
        final String test = ""
            + "alert(instr.nodeValue);\n"
            + "alert(instr.data);\n"
            + "alert(instr.text);\n"
            // null
            + "try {\n"
            + "  instr.nodeValue = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  instr.nodeValue = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  instr.nodeValue = 'test';\n"
            + "} catch(e) { alert('exception-set'); }\n";

        tester_xmlDecl(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void ownerDocument() throws Exception {
        tester("alert(instr.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void ownerDocument_xmlDecl() throws Exception {
        tester_xmlDecl("alert(instr.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void ownerDocument_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('apache', 'file=\"header.html\"');\n"
            + "    try {\n"
            + "      alert(instr.ownerDocument === doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void ownerDocument_created_xmlDecl() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "    try {\n"
            + "      alert(instr.ownerDocument === doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void parentNode() throws Exception {
        tester("alert(doc === instr.parentNode);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void parentNode_xmlDecl() throws Exception {
        tester_xmlDecl("alert(doc === instr.parentNode);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void parentNode_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('apache', 'file=\"header.html\"');\n"
            + "    try {\n"
            + "      alert(instr.parentNode == null);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void parentNode_created_xmlDecl() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "    try {\n"
            + "      alert(instr.parentNode == null);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void prefix() throws Exception {
        property("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void prefix_xmlDecl() throws Exception {
        property_xmlDecl("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("apache")
    public void target() throws Exception {
        property("target");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("xml")
    public void target_xmlDecl() throws Exception {
        property_xmlDecl("target");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "include file=\"header.html\" ", "include file=\"header.html\" ", "include file=\"header.html\" ",
        "exception-setNull",
        "", "", "",
        "test", "test", "test",
        "test\ntest", "test\ntest", "test\ntest",
        "<tag/>", "<tag/>", "<tag/>" })
    public void text() throws Exception {
        final String test = ""
            + "alert(instr.text);\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            // null
            + "try {\n"
            + "  instr.text = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "instr.text = '';\n"
            + "alert(instr.text);\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            // normal
            + "instr.text = 'test';\n"
            + "alert(instr.text);\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            // linebreak
            + "instr.text = 'test\\ntest';\n"
            + "alert(instr.text);\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            // xml
            + "instr.text = '<tag/>';\n"
            + "alert(instr.text);\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "version=\"1.0\"", "version=\"1.0\"", "version=\"1.0\"",
        "exception-setNull",
        "exception-setEmpty",
        "exception-set" })
    public void text_xmlDecl() throws Exception {
        final String test = ""
            + "alert(instr.text);\n"
            + "alert(instr.data);\n"
            + "alert(instr.nodeValue);\n"
            // null
            + "try {\n"
            + "  instr.text = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  instr.text = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  instr.text = 'test';\n"
            + "} catch(e) { alert('exception-set'); }\n";

        tester_xmlDecl(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<?apache include file=\"header.html\" ?>")
    public void xml() throws Exception {
        property("xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<?xml version=\"1.0\"?>")
    public void xml_xmlDecl() throws Exception {
        property_xmlDecl("xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<?x ?>")
    public void xml_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('x', '');\n"
            + "    try {\n"
            + "      alert(instr.xml);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    private void property(final String property) throws Exception {
        tester("alert(instr." + property + ");\n");
    }

    private void property_xmlDecl(final String property) throws Exception {
        tester_xmlDecl("alert(instr." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<?apache include file=\"header.html\" ?>\n"
            + "<root/>";

        tester(test, xml);
    }

    private void tester_xmlDecl(final String test) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "    doc.appendChild(instr);\n"
            + "    try {\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    try {\n"
            + "      var instr = doc.firstChild;\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
