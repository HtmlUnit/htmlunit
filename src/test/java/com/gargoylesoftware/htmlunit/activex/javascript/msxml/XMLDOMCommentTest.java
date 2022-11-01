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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link XMLDOMComment}.
 *
 * @author Mirko Friedenhagen
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLDOMCommentTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "[object\\sObject]")
    public void scriptableToString() throws Exception {
        tester("log(Object.prototype.toString.call(comment));\n");
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
        tester("log(comment.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"", "", "",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void data() throws Exception {
        final String test = ""
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.text);\n"
            // null
            + "try {\n"
            + "  comment.data = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "comment.data = '';\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.text);\n"
            // normal
            + "comment.data = 'test';\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.text);\n"
            // linebreak
            + "comment.data = 'test\\ntest';\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.text);\n"
            // xml
            + "comment.data = '<tag/>';\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.text);\n";

        final String xml = ""
            + "<root>"
            + "<!---->"
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
            IE = {"0", "0", "4", "9", "6"})
    public void length() throws Exception {
        final String test = ""
            + "log(comment.length);\n"
            // empty
            + "comment.data = '';\n"
            + "log(comment.length);\n"
            // normal
            + "comment.data = 'test';\n"
            + "log(comment.length);\n"
            // linebreak
            + "comment.data = 'test\\ntest';\n"
            + "log(comment.length);\n"
            // xml
            + "comment.data = '<tag/>';\n"
            + "log(comment.length);\n";

        final String xml = ""
            + "<root>"
            + "<!---->"
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
            IE = "#comment")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "8")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"", "", "",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void nodeValue() throws Exception {
        final String test = ""
            + "log(comment.nodeValue);\n"
            + "log(comment.data);\n"
            + "log(comment.text);\n"
            // null
            + "try {\n"
            + "  comment.nodeValue = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "comment.nodeValue = '';\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.data);\n"
            + "log(comment.text);\n"
            // normal
            + "comment.nodeValue = 'test';\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.data);\n"
            + "log(comment.text);\n"
            // linebreak
            + "comment.nodeValue = 'test\\ntest';\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.data);\n"
            + "log(comment.text);\n"
            // xml
            + "comment.nodeValue = '<tag/>';\n"
            + "log(comment.nodeValue);\n"
            + "log(comment.data);\n"
            + "log(comment.text);\n";

        final String xml = ""
            + "<root>"
            + "<!---->"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = "true")
    public void ownerDocument() throws Exception {
        tester("log(comment.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "true")
    public void ownerDocument_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var comment = doc.createComment('something');\n"
            + "    try {\n"
            + "      log(comment.ownerDocument === doc);\n"
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
        tester("log(root === comment.parentNode);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "true")
    public void parentNode_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var comment = doc.createComment('something');\n"
            + "    try {\n"
            + "      log(comment.parentNode == null);\n"
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
            IE = {"", "", "",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void text() throws Exception {
        final String test = ""
            + "log(comment.text);\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            // null
            + "try {\n"
            + "  comment.text = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "comment.text = '';\n"
            + "log(comment.text);\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            // normal
            + "comment.text = 'test';\n"
            + "log(comment.text);\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            // linebreak
            + "comment.text = 'test\\ntest';\n"
            + "log(comment.text);\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n"
            // xml
            + "comment.text = '<tag/>';\n"
            + "log(comment.text);\n"
            + "log(comment.data);\n"
            + "log(comment.nodeValue);\n";

        final String xml = ""
            + "<root>"
            + "<!---->"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"<!---->", "<!--##-->", "<!--text-->", "<!--text\\r\\ntext-->", "<!--##text##text##-->"})
    public void xml() throws Exception {
        final String test =
            // empty
            "var txt = comment.xml;\n"
            + "txt = txt.replace(/ /g, '#');\n"
            + "log(txt);\n"
            // blank
            + "comment = root.childNodes[1];\n"
            + "txt = comment.xml;\n"
            + "txt = txt.replace(/ /g, '#');\n"
            + "log(txt);\n"
            // normal
            + "comment = root.childNodes[2];\n"
            + "txt = comment.xml;\n"
            + "txt = txt.replace(/ /g, '#');\n"
            + "log(txt);\n"
            // linebreak
            + "comment = root.childNodes[3];\n"
            + "txt = comment.xml;\n"
            + "txt = txt.replace(/ /g, '#');\n"
            + "log(txt);\n"
            // space
            + "comment = root.childNodes[4];\n"
            + "txt = comment.xml;\n"
            + "txt = txt.replace(/ /g, '#');\n"
            + "log(txt);\n";

        final String xml = ""
            + "<root>"
            + "<!---->"
            + "<!--  -->"
            + "<!--text-->"
            + "<!--text\ntext-->"
            + "<!--  text  text  -->"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "<!---->")
    public void xml_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var comment = doc.createComment('');\n"
            + "    try {\n"
            + "      log(comment.xml);\n"
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
            IE = {"myComment", "myComment-appended", "exception-appendNull", "myComment-appended", "myComment-appended\\n"})
    public void appendData() throws Exception {
        final String test = ""
            + "log(comment.data);\n"
            // normal
            + "comment.appendData('-appended');\n"
            + "log(comment.data);\n"
            // null
            + "try {\n"
            + "  comment.appendData(null);\n"
            + "} catch(e) { log('exception-appendNull'); }\n"
            // empty
            + "comment.appendData('');\n"
            + "log(comment.data);\n"
            // linebreak
            + "comment.appendData('\\n');\n"
            + "log(comment.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myComment", "myCommen", "myCen", "yCen",
                  "exception-deletePosNegative", "yCen",
                  "exception-deletePosTooHigh", "yCen",
                  "yCen",
                  "exception-deleteCntNegative", "yCen",
                  "y"})
    public void deleteData() throws Exception {
        final String test = ""
            + "log(comment.data);\n"
            // back
            + "comment.deleteData(8, 1);\n"
            + "log(comment.data);\n"
            // middle
            + "comment.deleteData(3, 3);\n"
            + "log(comment.data);\n"
            // front
            + "comment.deleteData(0, 1);\n"
            + "log(comment.data);\n"
            // position negative
            + "try {\n"
            + "  comment.deleteData(-1, 1);\n"
            + "} catch(e) { log('exception-deletePosNegative'); }\n"
            + "log(comment.data);\n"
            // position too high
            + "try {\n"
            + "  comment.deleteData(5, 1);\n"
            + "} catch(e) { log('exception-deletePosTooHigh'); }\n"
            + "log(comment.data);\n"
            // count zero
            + "comment.deleteData(1, 0);\n"
            + "log(comment.data);\n"
            // count negative
            + "try {\n"
            + "  comment.deleteData(1, -1);\n"
            + "} catch(e) { log('exception-deleteCntNegative'); }\n"
            + "log(comment.data);\n"
            // count too high
            + "comment.deleteData(1, 5);\n"
            + "log(comment.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myComment", "myComment-b", "myCom-m-ment-b", "f-myCom-m-ment-b",
                  "exception-insertNull", "f-myCom-m-ment-b", "\\nf-myCom-m-ment-b",
                  "\\nf-myCom-m-ment-b",
                  "exception-insertPosNegative", "\\nf-myCom-m-ment-b",
                  "\\nf-myCom-m-ment-b",
                  "exception-insertPosTooHigh", "\\nf-myCom-m-ment-b"})
    public void insertData() throws Exception {
        final String test = ""
            + "log(comment.data);\n"
            // back
            + "comment.insertData(9, '-b');\n"
            + "log(comment.data);\n"
            // middle
            + "comment.insertData(5, '-m-');\n"
            + "log(comment.data);\n"
            // front
            + "comment.insertData(0, 'f-');\n"
            + "log(comment.data);\n"
            // null
            + "try {\n"
            + "  comment.insertData(0, null);\n"
            + "} catch(e) { log('exception-insertNull'); }\n"
            // empty
            + "comment.insertData(5, '');\n"
            + "log(comment.data);\n"
            // linebreak
            + "comment.insertData(0, '\\n');\n"
            + "log(comment.data);\n"
            // empty and position negative
            + "comment.insertData(-1, '');\n"
            + "log(comment.data);\n"
            // position negative
            + "try {\n"
            + "  comment.insertData(-1, 'X');\n"
            + "} catch(e) { log('exception-insertPosNegative'); }\n"
            + "log(comment.data);\n"
            // empty and position too high
            + "comment.insertData(25, '');\n"
            + "log(comment.data);\n"
            // position too high
            + "try {\n"
            + "  comment.insertData(25, 'Y');\n"
            + "} catch(e) { log('exception-insertPosTooHigh'); }\n"
            + "log(comment.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myComment", "myCommen-b", "myC-m-en-b", "f-yC-m-en-b",
                  "exception-replaceNull", "f-yCen-b", "f-y\\nn-b",
                  "exception-replacePosNegative", "f-y\\nn-b",
                  "exception-replacePosTooHigh", "f-y\\nn-b",
                  "f-yC\\nn-b",
                  "exception-replaceCntNegative", "f-yC\\nn-b",
                  "fX"})
    public void replaceData() throws Exception {
        final String test = ""
            + "log(comment.data);\n"
            // back
            + "comment.replaceData(8, 1, '-b');\n"
            + "log(comment.data);\n"
            // middle
            + "comment.replaceData(3, 3, '-m-');\n"
            + "log(comment.data);\n"
            // front
            + "comment.replaceData(0, 1, 'f-');\n"
            + "log(comment.data);\n"
            // null
            + "try {\n"
            + "  comment.replaceData(0, 1, null);\n"
            + "} catch(e) { log('exception-replaceNull'); }\n"
            // empty
            + "comment.replaceData(4, 3, '');\n"
            + "log(comment.data);\n"
            // linebreak
            + "comment.replaceData(3, 2, '\\n');\n"
            + "log(comment.data);\n"
            // position negative
            + "try {\n"
            + "  comment.replaceData(-1, 1, '');\n"
            + "} catch(e) { log('exception-replacePosNegative'); }\n"
            + "log(comment.data);\n"
            // position too high
            + "try {\n"
            + "  comment.replaceData(25, 1, '');\n"
            + "} catch(e) { log('exception-replacePosTooHigh'); }\n"
            + "log(comment.data);\n"
            // count zero
            + "comment.replaceData(3, 0, 'C');\n"
            + "log(comment.data);\n"
            // count negative
            + "try {\n"
            + "  comment.replaceData(1, -1, '');\n"
            + "} catch(e) { log('exception-replaceCntNegative'); }\n"
            + "log(comment.data);\n"
            // count too high
            + "comment.replaceData(1, 25, 'X');\n"
            + "log(comment.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no\\sActiveX",
            IE = {"myComment", "t", "myComment", "omm", "myComment", "m", "myComment",
                  "exception-substringPosNegative", "myComment",
                  "exception-substringPosTooHigh", "myComment",
                  "", "myComment",
                  "exception-substringCntNegative", "myComment",
                  "yComment", "myComment"})
    public void substringData() throws Exception {
        final String test = ""
            + "log(comment.data);\n"
            // back
            + "log(comment.substringData(8, 1));\n"
            + "log(comment.data);\n"
            // middle
            + "log(comment.substringData(3, 3));\n"
            + "log(comment.data);\n"
            // front
            + "log(comment.substringData(0, 1));\n"
            + "log(comment.data);\n"
            // position negative
            + "try {\n"
            + "  comment.substringData(-1, 1);\n"
            + "} catch(e) { log('exception-substringPosNegative'); }\n"
            + "log(comment.data);\n"
            // position too high
            + "try {\n"
            + "  comment.substringData(25, 1);\n"
            + "} catch(e) { log('exception-substringPosTooHigh'); }\n"
            + "log(comment.data);\n"
            // count zero
            + "log(comment.substringData(1, 0));\n"
            + "log(comment.data);\n"
            // count negative
            + "try {\n"
            + "  comment.substringData(1, -1);\n"
            + "} catch(e) { log('exception-substringCntNegative'); }\n"
            + "log(comment.data);\n"
            // count too high
            + "log(comment.substringData(1, 25));\n"
            + "log(comment.data);\n";

        tester(test);
    }

    private void property(final String property) throws Exception {
        tester("log(comment." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<root><!--myComment--></root>";

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
            + "      var comment = root.firstChild;\n"
            + test
            + "    } catch(e) { log('exception'+e.message); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(createTestHTML(html));
    }
}
