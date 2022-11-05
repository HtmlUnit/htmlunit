/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.ACTIVEX_CHECK;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.CREATE_XMLDOMDOCUMENT_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.createTestHTML;
import static com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link XMLDOMCDATASection}.
 *
 * @author Mirko Friedenhagen
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLDOMCDATASectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "[object\\sObject]")
    public void scriptableToString() throws Exception {
        tester("log(Object.prototype.toString.call(cdata));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "null")
    public void attributes() throws Exception {
        property("attributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "")
    public void baseName() throws Exception {
        property("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "0")
    public void childNodes() throws Exception {
        tester("log(cdata.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"#", "\\s", "\\s", "\\s",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void data() throws Exception {
        final String test = ""
            + "log('#');\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.text);\n"
            // null
            + "try {\n"
            + "  cdata.data = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "cdata.data = '';\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.text);\n"
            // normal
            + "cdata.data = 'test';\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.text);\n"
            // linebreak
            + "cdata.data = 'test\\ntest';\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.text);\n"
            // xml
            + "cdata.data = '<tag/>';\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.text);\n";

        final String xml = ""
            + "<root>"
            + "<![CDATA[ ]]>"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "null")
    public void dataType() throws Exception {
        property("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "null")
    public void definition() throws Exception {
        property("definition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "null")
    public void firstChild() throws Exception {
        property("firstChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "null")
    public void lastChild() throws Exception {
        property("lastChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1", "0", "4", "9", "6"})
    public void length() throws Exception {
        final String test = ""
            + "log(cdata.length);\n"
            // empty
            + "cdata.data = '';\n"
            + "log(cdata.length);\n"
            // normal
            + "cdata.data = 'test';\n"
            + "log(cdata.length);\n"
            // linebreak
            + "cdata.data = 'test\\ntest';\n"
            + "log(cdata.length);\n"
            // xml
            + "cdata.data = '<tag/>';\n"
            + "log(cdata.length);\n";

        final String xml = ""
            + "<root>"
            + "<![CDATA[ ]]>"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "")
    public void namespaceURI() throws Exception {
        property("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "#cdata-section")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "4")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"#", "\\s", "\\s", "\\s",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void nodeValue() throws Exception {
        final String test = ""
            + "log('#');\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.data);\n"
            + "log(cdata.text);\n"
            // null
            + "try {\n"
            + "  cdata.nodeValue = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "cdata.nodeValue = '';\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.data);\n"
            + "log(cdata.text);\n"
            // normal
            + "cdata.nodeValue = 'test';\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.data);\n"
            + "log(cdata.text);\n"
            // linebreak
            + "cdata.nodeValue = 'test\\ntest';\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.data);\n"
            + "log(cdata.text);\n"
            // xml
            + "cdata.nodeValue = '<tag/>';\n"
            + "log(cdata.nodeValue);\n"
            + "log(cdata.data);\n"
            + "log(cdata.text);\n";

        final String xml = ""
            + "<root>"
            + "<![CDATA[ ]]>"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "")
    @NotYetImplemented(IE)
    // Xerxes is the problem here as they drop empty CDATA sections. So <root> has no children and our test fails.
    // see: https://issues.apache.org/jira/browse/XERCESJ-1033
    public void nodeValue_empty() throws Exception {
        final String xml = ""
            + "<root>"
            + "<![CDATA[]]>"
            + "</root>";

        tester("log(cdata.nodeValue);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "true")
    public void ownerDocument() throws Exception {
        tester("log(cdata.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "true")
    public void ownerDocument_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var cdata = doc.createCDATASection('something');\n"
            + "    try {\n"
            + "      log(cdata.ownerDocument === doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "true")
    public void parentNode() throws Exception {
        final String xml = ""
            + "<root>"
            + "<child-element/>"
            + "</root>";

        tester("log(root === root.childNodes[0].parentNode);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "true")
    public void parentNode_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var cdata = doc.createCDATASection('something');\n"
            + "    try {\n"
            + "      log(cdata.parentNode == null);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "")
    public void prefix() throws Exception {
        property("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"#", "\\s", "\\s", "\\s",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void text() throws Exception {
        final String test = ""
            + "log('#');\n"
            + "log(cdata.text);\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            // null
            + "try {\n"
            + "  cdata.text = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "cdata.text = '';\n"
            + "log(cdata.text);\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            // normal
            + "cdata.text = 'test';\n"
            + "log(cdata.text);\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            // linebreak
            + "cdata.text = 'test\\ntest';\n"
            + "log(cdata.text);\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n"
            // xml
            + "cdata.text = '<tag/>';\n"
            + "log(cdata.text);\n"
            + "log(cdata.data);\n"
            + "log(cdata.nodeValue);\n";

        final String xml = ""
            + "<root>"
            + "<![CDATA[ ]]>"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"<![CDATA[\\s]]>", "<![CDATA[text]]>", "<![CDATA[text\\r\\ntext]]>", "<![CDATA[<tag/>]]>"})
    public void xml() throws Exception {
        final String test = ""
            + "log(cdata.xml);\n"
            // text
            + "cdata = root.childNodes[1];\n"
            + "log(cdata.xml);\n"
            // linebreak
            + "cdata = root.childNodes[2];\n"
            + "log(cdata.xml);\n"
            // xml
            + "cdata = root.childNodes[3];\n"
            + "log(cdata.xml);\n";

        final String xml = ""
            + "<root>"
            + "<![CDATA[ ]]>"
            + "<![CDATA[text]]>"
            + "<![CDATA[text\ntext]]>"
            + "<![CDATA[<tag/>]]>"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "<![CDATA[]]>")
    public void xml_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var cdata = doc.createCDATASection('');\n"
            + "    try {\n"
            + "      log(cdata.xml);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myCDATA", "myCDATA-appended", "exception-appendNull", "myCDATA-appended", "myCDATA-appended\\n"})
    public void appendData() throws Exception {
        final String test = ""
            + "log(cdata.data);\n"
            // normal
            + "cdata.appendData('-appended');\n"
            + "log(cdata.data);\n"
            // null
            + "try {\n"
            + "  cdata.appendData(null);\n"
            + "} catch(e) { log('exception-appendNull'); }\n"
            // empty
            + "cdata.appendData('');\n"
            + "log(cdata.data);\n"
            // linebreak
            + "cdata.appendData('\\n');\n"
            + "log(cdata.data);\n";
        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myCDATA", "myCDAT", "myT", "yT",
                  "exception-deletePosNegative", "yT",
                  "exception-deletePosTooHigh", "yT",
                  "yT",
                  "exception-deleteCntNegative", "yT",
                  "y"})
    public void deleteData() throws Exception {
        final String test = ""
            + "log(cdata.data);\n"
            // back
            + "cdata.deleteData(6, 1);\n"
            + "log(cdata.data);\n"
            // middle
            + "cdata.deleteData(2, 3);\n"
            + "log(cdata.data);\n"
            // front
            + "cdata.deleteData(0, 1);\n"
            + "log(cdata.data);\n"
            // position negative
            + "try {\n"
            + "  cdata.deleteData(-1, 1);\n"
            + "} catch(e) { log('exception-deletePosNegative'); }\n"
            + "log(cdata.data);\n"
            // position too high
            + "try {\n"
            + "  cdata.deleteData(5, 1);\n"
            + "} catch(e) { log('exception-deletePosTooHigh'); }\n"
            + "log(cdata.data);\n"
            // count zero
            + "cdata.deleteData(1, 0);\n"
            + "log(cdata.data);\n"
            // count negative
            + "try {\n"
            + "  cdata.deleteData(1, -1);\n"
            + "} catch(e) { log('exception-deleteCntNegative'); }\n"
            + "log(cdata.data);\n"
            // count too high
            + "cdata.deleteData(1, 5);\n"
            + "log(cdata.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myCDATA", "myCDATA-b", "myC-m-DATA-b", "f-myC-m-DATA-b",
                  "exception-insertNull", "f-myC-m-DATA-b", "\\nf-myC-m-DATA-b",
                  "\\nf-myC-m-DATA-b",
                  "exception-insertPosNegative", "\\nf-myC-m-DATA-b",
                  "\\nf-myC-m-DATA-b",
                  "exception-insertPosTooHigh", "\\nf-myC-m-DATA-b"})
    public void insertData() throws Exception {
        final String test = ""
            + "log(cdata.data);\n"
            // back
            + "cdata.insertData(7, '-b');\n"
            + "log(cdata.data);\n"
            // middle
            + "cdata.insertData(3, '-m-');\n"
            + "log(cdata.data);\n"
            // front
            + "cdata.insertData(0, 'f-');\n"
            + "log(cdata.data);\n"
            // null
            + "try {\n"
            + "  cdata.insertData(0, null);\n"
            + "} catch(e) { log('exception-insertNull'); }\n"
            // empty
            + "cdata.insertData(5, '');\n"
            + "log(cdata.data);\n"
            // linebreak
            + "cdata.insertData(0, '\\n');\n"
            + "log(cdata.data);\n"
            // empty and position negative
            + "cdata.insertData(-1, '');\n"
            + "log(cdata.data);\n"
            // position negative
            + "try {\n"
            + "  cdata.insertData(-1, 'X');\n"
            + "} catch(e) { log('exception-insertPosNegative'); }\n"
            + "log(cdata.data);\n"
            // empty and position too high
            + "cdata.insertData(25, '');\n"
            + "log(cdata.data);\n"
            // position too high
            + "try {\n"
            + "  cdata.insertData(25, 'Y');\n"
            + "} catch(e) { log('exception-insertPosTooHigh'); }\n"
            + "log(cdata.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myCDATA", "myCDAT-b", "my-m-T-b", "f-y-m-T-b",
                  "exception-replaceNull", "f-y--T-b", "f-y\\nT-b",
                  "exception-replacePosNegative", "f-y\\nT-b",
                  "exception-replacePosTooHigh", "f-y\\nT-b",
                  "f-y\\nT-b",
                  "exception-replaceCntNegative", "f-y\\nT-b",
                  "f"})
    public void replaceData() throws Exception {
        final String test = ""
            + "log(cdata.data);\n"
            // back
            + "cdata.replaceData(6, 1, '-b');\n"
            + "log(cdata.data);\n"
            // middle
            + "cdata.replaceData(2, 3, '-m-');\n"
            + "log(cdata.data);\n"
            // front
            + "cdata.replaceData(0, 1, 'f-');\n"
            + "log(cdata.data);\n"
            // null
            + "try {\n"
            + "  cdata.replaceData(0, 1, null);\n"
            + "} catch(e) { log('exception-replaceNull'); }\n"
            // empty
            + "cdata.replaceData(4, 1, '');\n"
            + "log(cdata.data);\n"
            // linebreak
            + "cdata.replaceData(3, 2, '\\n');\n"
            + "log(cdata.data);\n"
            // position negative
            + "try {\n"
            + "  cdata.replaceData(-1, 1, '');\n"
            + "} catch(e) { log('exception-replacePosNegative'); }\n"
            + "log(cdata.data);\n"
            // position too high
            + "try {\n"
            + "  cdata.replaceData(25, 1, '');\n"
            + "} catch(e) { log('exception-replacePosTooHigh'); }\n"
            + "log(cdata.data);\n"
            // count zero
            + "cdata.replaceData(1, 0, '');\n"
            + "log(cdata.data);\n"
            // count negative
            + "try {\n"
            + "  cdata.replaceData(1, -1, '');\n"
            + "} catch(e) { log('exception-replaceCntNegative'); }\n"
            + "log(cdata.data);\n"
            // count too high
            + "cdata.replaceData(1, 25, '');\n"
            + "log(cdata.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"1", "2", "#cdata-section", "2", "my", "#cdata-section", "5", "CDATA", "true",
                  "exception-splitPosNegative", "my",
                  "exception-splitPosTooHigh", "my"})
    public void splitText() throws Exception {
        final String test = ""
            + "log(root.childNodes.length);\n"
            + "var node = cdata.splitText(2);\n"
            + "log(root.childNodes.length);\n"
            + "log(root.childNodes[0].nodeName);\n"
            + "log(root.childNodes[0].length);\n"
            + "log(root.childNodes[0].text);\n"
            + "log(root.childNodes[1].nodeName);\n"
            + "log(root.childNodes[1].length);\n"
            + "log(root.childNodes[1].text);\n"
            + "log(node === root.childNodes[1]);\n"
            // position negative
            + "try {\n"
            + "  cdata.splitText(-1);\n"
            + "} catch(e) { log('exception-splitPosNegative'); }\n"
            + "log(cdata.data);\n"
            // position too high
            + "try {\n"
            + "  cdata.splitText(25);\n"
            + "} catch(e) { log('exception-splitPosTooHigh'); }\n"
            + "log(cdata.data);\n";
        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myCDATA", "A", "myCDATA", "CDA", "myCDATA", "m", "myCDATA",
                  "exception-substringPosNegative", "myCDATA",
                  "exception-substringPosTooHigh", "myCDATA",
                  "", "myCDATA",
                  "exception-substringCntNegative", "myCDATA",
                  "yCDATA", "myCDATA"})
    public void substringData() throws Exception {
        final String test = ""
            + "log(cdata.data);\n"
            // back
            + "log(cdata.substringData(6, 1));\n"
            + "log(cdata.data);\n"
            // middle
            + "log(cdata.substringData(2, 3));\n"
            + "log(cdata.data);\n"
            // front
            + "log(cdata.substringData(0, 1));\n"
            + "log(cdata.data);\n"
            // position negative
            + "try {\n"
            + "  cdata.substringData(-1, 1);\n"
            + "} catch(e) { log('exception-substringPosNegative'); }\n"
            + "log(cdata.data);\n"
            // position too high
            + "try {\n"
            + "  cdata.substringData(25, 1);\n"
            + "} catch(e) { log('exception-substringPosTooHigh'); }\n"
            + "log(cdata.data);\n"
            // count zero
            + "log(cdata.substringData(1, 0));\n"
            + "log(cdata.data);\n"
            // count negative
            + "try {\n"
            + "  cdata.substringData(1, -1);\n"
            + "} catch(e) { log('exception-substringCntNegative'); }\n"
            + "log(cdata.data);\n"
            // count too high
            + "log(cdata.substringData(1, 25));\n"
            + "log(cdata.data);\n";

        tester(test);
    }

    private void property(final String property) throws Exception {
        tester("log(cdata." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<root><![CDATA[myCDATA]]></root>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    try {\n"
            + "      var cdata = root.firstChild;\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(createTestHTML(html));
    }
}
