/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.activex.javascript.msxml;

import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.ACTIVEX_CHECK;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.CREATE_XMLDOMDOCUMENT_FUNCTION;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callCreateXMLDOMDocument;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callLoadXMLDOMDocumentFromURL;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.createTestHTML;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link XMLDOMProcessingInstruction}.
 *
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLDOMProcessingInstructionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void scriptableToString() throws Exception {
        tester("log(Object.prototype.toString.call(instr));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void attributes() throws Exception {
        property("attributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"1", "version=1.0"})
    // DOM processing instructions do not support attributes
    public void attributes_xmlDecl() throws Exception {
        final String test = ""
            + "log(instr.attributes.length);\n"
            + "var attr = instr.attributes[0];\n"
            + "log(attr.nodeName + '=' + attr.nodeValue);\n";
        tester_xmlDecl(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"3", "version=1.0", "encoding=utf-8", "standalone=yes"})
    // DOM processing instructions do not support attributes
    public void attributes_complete_xmlDecl() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + ACTIVEX_CHECK
            + "  var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "  var instr = doc.createProcessingInstruction("
            + "'xml', 'version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"');\n"
            + "  doc.appendChild(instr);\n"
            + "  try {\n"
            + "    log(instr.attributes.length);\n"
            + "    var attr = instr.attributes[0];\n"
            + "    log(attr.nodeName + '=' + attr.nodeValue);\n"
            + "    attr = instr.attributes[1];\n"
            + "    log(attr.nodeName + '=' + attr.nodeValue);\n"
            + "    attr = instr.attributes[2];\n"
            + "    log(attr.nodeName + '=' + attr.nodeValue);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void baseName() throws Exception {
        property("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void baseName_xmlDecl() throws Exception {
        property_xmlDecl("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void childNodes() throws Exception {
        tester("log(instr.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void childNodes_xmlDecl() throws Exception {
        tester_xmlDecl("log(instr.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"include file=\"header.html\" ", "include file=\"header.html\" ", "include file=\"header.html\" ",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test test", "test test", "test test",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void data() throws Exception {
        final String test = ""
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.text);\n"
            // null
            + "try {\n"
            + "  instr.data = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "instr.data = '';\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.text);\n"
            // normal
            + "instr.data = 'test';\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.text);\n"
            // linebreak
            + "instr.data = 'test\\ntest';\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.text);\n"
            // xml
            + "instr.data = '<tag/>';\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.text);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"version=\"1.0\"", "version=\"1.0\"", "version=\"1.0\"",
                  "exception-setNull",
                  "exception-setEmpty",
                  "exception-set"})
    public void data_xmlDecl() throws Exception {
        final String test = ""
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.text);\n"
            // null
            + "try {\n"
            + "  instr.data = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  instr.data = '';\n"
            + "} catch(e) { log('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  instr.data = 'test';\n"
            + "} catch(e) { log('exception-set'); }\n";

        tester_xmlDecl(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void dataType() throws Exception {
        property("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void dataType_xmlDecl() throws Exception {
        property_xmlDecl("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void definition() throws Exception {
        property("definition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void definition_xmlDecl() throws Exception {
        property_xmlDecl("definition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void firstChild() throws Exception {
        property("firstChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void firstChild_xmlDecl() throws Exception {
        property_xmlDecl("firstChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void lastChild() throws Exception {
        property("lastChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void lastChild_xmlDecl() throws Exception {
        property_xmlDecl("lastChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void namespaceURI() throws Exception {
        property("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void namespaceURI_xmlDecl() throws Exception {
        property_xmlDecl("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void nodeName_xmlDecl() throws Exception {
        property_xmlDecl("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void nodeType_xmlDecl() throws Exception {
        property_xmlDecl("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"include file=\"header.html\" ", "include file=\"header.html\" ", "include file=\"header.html\" ",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test test", "test test", "test test",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void nodeValue() throws Exception {
        final String test = ""
            + "log(instr.nodeValue);\n"
            + "log(instr.data);\n"
            + "log(instr.text);\n"
            // null
            + "try {\n"
            + "  instr.nodeValue = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "instr.nodeValue = '';\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.data);\n"
            + "log(instr.text);\n"
            // normal
            + "instr.nodeValue = 'test';\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.data);\n"
            + "log(instr.text);\n"
            // linebreak
            + "instr.nodeValue = 'test\\ntest';\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.data);\n"
            + "log(instr.text);\n"
            // xml
            + "instr.nodeValue = '<tag/>';\n"
            + "log(instr.nodeValue);\n"
            + "log(instr.data);\n"
            + "log(instr.text);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"version=\"1.0\"", "version=\"1.0\"", "version=\"1.0\"",
                  "exception-setNull",
                  "exception-setEmpty",
                  "exception-set"})
    public void nodeValue_xmlDecl() throws Exception {
        final String test = ""
            + "log(instr.nodeValue);\n"
            + "log(instr.data);\n"
            + "log(instr.text);\n"
            // null
            + "try {\n"
            + "  instr.nodeValue = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  instr.nodeValue = '';\n"
            + "} catch(e) { log('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  instr.nodeValue = 'test';\n"
            + "} catch(e) { log('exception-set'); }\n";

        tester_xmlDecl(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void ownerDocument() throws Exception {
        tester("log(instr.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void ownerDocument_xmlDecl() throws Exception {
        tester_xmlDecl("log(instr.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void ownerDocument_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('apache', 'file=\"header.html\"');\n"
            + "    try {\n"
            + "      log(instr.ownerDocument === doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void ownerDocument_created_xmlDecl() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "    try {\n"
            + "      log(instr.ownerDocument === doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void parentNode() throws Exception {
        tester("log(doc === instr.parentNode);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void parentNode_xmlDecl() throws Exception {
        tester_xmlDecl("log(doc === instr.parentNode);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void parentNode_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('apache', 'file=\"header.html\"');\n"
            + "    try {\n"
            + "      log(instr.parentNode == null);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void parentNode_created_xmlDecl() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "    try {\n"
            + "      log(instr.parentNode == null);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void prefix() throws Exception {
        property("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void prefix_xmlDecl() throws Exception {
        property_xmlDecl("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void target() throws Exception {
        property("target");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void target_xmlDecl() throws Exception {
        property_xmlDecl("target");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"include file=\"header.html\" ", "include file=\"header.html\" ", "include file=\"header.html\" ",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test test", "test test", "test test",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void text() throws Exception {
        final String test = ""
            + "log(instr.text);\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            // null
            + "try {\n"
            + "  instr.text = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "instr.text = '';\n"
            + "log(instr.text);\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            // normal
            + "instr.text = 'test';\n"
            + "log(instr.text);\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            // linebreak
            + "instr.text = 'test\\ntest';\n"
            + "log(instr.text);\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            // xml
            + "instr.text = '<tag/>';\n"
            + "log(instr.text);\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"version=\"1.0\"", "version=\"1.0\"", "version=\"1.0\"",
                  "exception-setNull",
                  "exception-setEmpty",
                  "exception-set"})
    public void text_xmlDecl() throws Exception {
        final String test = ""
            + "log(instr.text);\n"
            + "log(instr.data);\n"
            + "log(instr.nodeValue);\n"
            // null
            + "try {\n"
            + "  instr.text = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  instr.text = '';\n"
            + "} catch(e) { log('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  instr.text = 'test';\n"
            + "} catch(e) { log('exception-set'); }\n";

        tester_xmlDecl(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void xml() throws Exception {
        property("xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void xml_xmlDecl() throws Exception {
        property_xmlDecl("xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void xml_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('x', '');\n"
            + "    try {\n"
            + "      log(instr.xml);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    private void property(final String property) throws Exception {
        tester("log(instr." + property + ");\n");
    }

    private void property_xmlDecl(final String property) throws Exception {
        tester_xmlDecl("log(instr." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<?apache include file=\"header.html\" ?>\n"
            + "<root/>";

        tester(test, xml);
    }

    private void tester_xmlDecl(final String test) throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var instr = doc.createProcessingInstruction('xml', 'version=\"1.0\"');\n"
            + "    doc.appendChild(instr);\n"
            + "    try {\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    try {\n"
            + "      var instr = doc.firstChild;\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(createTestHTML(html));
    }
}
