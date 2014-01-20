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
import com.gargoylesoftware.htmlunit.activex.javascript.msxml.XMLDOMComment;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMComment}.
 *
 * @version $Revision$
 * @author Mirko Friedenhagen
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMCommentTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(comment));\n");
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
    @Alerts("")
    public void baseName() throws Exception {
        property("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("0")
    public void childNodes() throws Exception {
        tester("alert(comment.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "", "", "",
        "exception-setNull",
        "", "", "",
        "test", "test", "test",
        "test\ntest", "test\ntest", "test\ntest",
        "<tag/>", "<tag/>", "<tag/>" })
    public void data() throws Exception {
        final String test = ""
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.text);\n"
            // null
            + "try {\n"
            + "  comment.data = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "comment.data = '';\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.text);\n"
            // normal
            + "comment.data = 'test';\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.text);\n"
            // linebreak
            + "comment.data = 'test\\ntest';\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.text);\n"
            // xml
            + "comment.data = '<tag/>';\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.text);\n";

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
    public void definition() throws Exception {
        property("definition");
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
    public void lastChild() throws Exception {
        property("lastChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "0", "0", "4", "9", "6" })
    public void length() throws Exception {
        final String test = ""
            + "alert(comment.length);\n"
            // empty
            + "comment.data = '';\n"
            + "alert(comment.length);\n"
            // normal
            + "comment.data = 'test';\n"
            + "alert(comment.length);\n"
            // linebreak
            + "comment.data = 'test\\ntest';\n"
            + "alert(comment.length);\n"
            // xml
            + "comment.data = '<tag/>';\n"
            + "alert(comment.length);\n";

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
    @Alerts("#comment")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("8")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "", "", "",
        "exception-setNull",
        "", "", "",
        "test", "test", "test",
        "test\ntest", "test\ntest", "test\ntest",
        "<tag/>", "<tag/>", "<tag/>" })
    public void nodeValue() throws Exception {
        final String test = ""
            + "alert(comment.nodeValue);\n"
            + "alert(comment.data);\n"
            + "alert(comment.text);\n"
            // null
            + "try {\n"
            + "  comment.nodeValue = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "comment.nodeValue = '';\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.data);\n"
            + "alert(comment.text);\n"
            // normal
            + "comment.nodeValue = 'test';\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.data);\n"
            + "alert(comment.text);\n"
            // linebreak
            + "comment.nodeValue = 'test\\ntest';\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.data);\n"
            + "alert(comment.text);\n"
            // xml
            + "comment.nodeValue = '<tag/>';\n"
            + "alert(comment.nodeValue);\n"
            + "alert(comment.data);\n"
            + "alert(comment.text);\n";

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
    @Browsers(IE)
    @Alerts("true")
    public void ownerDocument() throws Exception {
        tester("alert(comment.ownerDocument === doc);\n");
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
            + "    var comment = doc.createComment('something');\n"
            + "    try {\n"
            + "      alert(comment.ownerDocument === doc);\n"
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
        tester("alert(root === comment.parentNode);\n");
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
            + "    var comment = doc.createComment('something');\n"
            + "    try {\n"
            + "      alert(comment.parentNode == null);\n"
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
    @Alerts({ "", "", "",
        "exception-setNull",
        "", "", "",
        "test", "test", "test",
        "test\ntest", "test\ntest", "test\ntest",
        "<tag/>", "<tag/>", "<tag/>" })
    public void text() throws Exception {
        final String test = ""
            + "alert(comment.text);\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            // null
            + "try {\n"
            + "  comment.text = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "comment.text = '';\n"
            + "alert(comment.text);\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            // normal
            + "comment.text = 'test';\n"
            + "alert(comment.text);\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            // linebreak
            + "comment.text = 'test\\ntest';\n"
            + "alert(comment.text);\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n"
            // xml
            + "comment.text = '<tag/>';\n"
            + "alert(comment.text);\n"
            + "alert(comment.data);\n"
            + "alert(comment.nodeValue);\n";

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
    @Browsers(IE)
    @Alerts({ "<!---->", "<!--  -->", "<!--text-->", "<!--text\r\ntext-->", "<!--  text  text  -->" })
    public void xml() throws Exception {
        final String test =
            // empty
              "alert(comment.xml);\n"
            // blank
            + "comment = root.childNodes[1];\n"
            + "alert(comment.xml);\n"
            // normal
            + "comment = root.childNodes[2];\n"
            + "alert(comment.xml);\n"
            // linebreak
            + "comment = root.childNodes[3];\n"
            + "alert(comment.xml);\n"
            // space
            + "comment = root.childNodes[4];\n"
            + "alert(comment.xml);\n";

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
    @Browsers(IE)
    @Alerts("<!---->")
    public void xml_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var comment = doc.createComment('');\n"
            + "    try {\n"
            + "      alert(comment.xml);\n"
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
    @Alerts({ "myComment", "myComment-appended", "exception-appendNull", "myComment-appended", "myComment-appended\n" })
    public void appendData() throws Exception {
        final String test = ""
            + "alert(comment.data);\n"
            // normal
            + "comment.appendData('-appended');\n"
            + "alert(comment.data);\n"
            // null
            + "try {\n"
            + "  comment.appendData(null);\n"
            + "} catch(e) { alert('exception-appendNull'); }\n"
            // empty
            + "comment.appendData('');\n"
            + "alert(comment.data);\n"
            // linebreak
            + "comment.appendData('\\n');\n"
            + "alert(comment.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "myComment", "myCommen", "myCen", "yCen",
        "exception-deletePosNegative", "yCen",
        "exception-deletePosTooHigh", "yCen",
        "yCen",
        "exception-deleteCntNegative", "yCen",
        "y" })
    public void deleteData() throws Exception {
        final String test = ""
            + "alert(comment.data);\n"
            // back
            + "comment.deleteData(8, 1);\n"
            + "alert(comment.data);\n"
            // middle
            + "comment.deleteData(3, 3);\n"
            + "alert(comment.data);\n"
            // front
            + "comment.deleteData(0, 1);\n"
            + "alert(comment.data);\n"
            // position negative
            + "try {\n"
            + "  comment.deleteData(-1, 1);\n"
            + "} catch(e) { alert('exception-deletePosNegative'); }\n"
            + "alert(comment.data);\n"
            // position too high
            + "try {\n"
            + "  comment.deleteData(5, 1);\n"
            + "} catch(e) { alert('exception-deletePosTooHigh'); }\n"
            + "alert(comment.data);\n"
            // count zero
            + "comment.deleteData(1, 0);\n"
            + "alert(comment.data);\n"
            // count negative
            + "try {\n"
            + "  comment.deleteData(1, -1);\n"
            + "} catch(e) { alert('exception-deleteCntNegative'); }\n"
            + "alert(comment.data);\n"
            // count too high
            + "comment.deleteData(1, 5);\n"
            + "alert(comment.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "myComment", "myComment-b", "myCom-m-ment-b", "f-myCom-m-ment-b",
        "exception-insertNull", "f-myCom-m-ment-b", "\nf-myCom-m-ment-b",
        "\nf-myCom-m-ment-b",
        "exception-insertPosNegative", "\nf-myCom-m-ment-b",
        "\nf-myCom-m-ment-b",
        "exception-insertPosTooHigh", "\nf-myCom-m-ment-b" })
    public void insertData() throws Exception {
        final String test = ""
            + "alert(comment.data);\n"
            // back
            + "comment.insertData(9, '-b');\n"
            + "alert(comment.data);\n"
            // middle
            + "comment.insertData(5, '-m-');\n"
            + "alert(comment.data);\n"
            // front
            + "comment.insertData(0, 'f-');\n"
            + "alert(comment.data);\n"
            // null
            + "try {\n"
            + "  comment.insertData(0, null);\n"
            + "} catch(e) { alert('exception-insertNull'); }\n"
            // empty
            + "comment.insertData(5, '');\n"
            + "alert(comment.data);\n"
            // linebreak
            + "comment.insertData(0, '\\n');\n"
            + "alert(comment.data);\n"
            // empty and position negative
            + "comment.insertData(-1, '');\n"
            + "alert(comment.data);\n"
            // position negative
            + "try {\n"
            + "  comment.insertData(-1, 'X');\n"
            + "} catch(e) { alert('exception-insertPosNegative'); }\n"
            + "alert(comment.data);\n"
            // empty and position too high
            + "comment.insertData(25, '');\n"
            + "alert(comment.data);\n"
            // position too high
            + "try {\n"
            + "  comment.insertData(25, 'Y');\n"
            + "} catch(e) { alert('exception-insertPosTooHigh'); }\n"
            + "alert(comment.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "myComment", "myCommen-b", "myC-m-en-b", "f-yC-m-en-b",
        "exception-replaceNull", "f-yCen-b", "f-y\nn-b",
        "exception-replacePosNegative", "f-y\nn-b",
        "exception-replacePosTooHigh", "f-y\nn-b",
        "f-yC\nn-b",
        "exception-replaceCntNegative", "f-yC\nn-b",
        "fX" })
    public void replaceData() throws Exception {
        final String test = ""
            + "alert(comment.data);\n"
            // back
            + "comment.replaceData(8, 1, '-b');\n"
            + "alert(comment.data);\n"
            // middle
            + "comment.replaceData(3, 3, '-m-');\n"
            + "alert(comment.data);\n"
            // front
            + "comment.replaceData(0, 1, 'f-');\n"
            + "alert(comment.data);\n"
            // null
            + "try {\n"
            + "  comment.replaceData(0, 1, null);\n"
            + "} catch(e) { alert('exception-replaceNull'); }\n"
            // empty
            + "comment.replaceData(4, 3, '');\n"
            + "alert(comment.data);\n"
            // linebreak
            + "comment.replaceData(3, 2, '\\n');\n"
            + "alert(comment.data);\n"
            // position negative
            + "try {\n"
            + "  comment.replaceData(-1, 1, '');\n"
            + "} catch(e) { alert('exception-replacePosNegative'); }\n"
            + "alert(comment.data);\n"
            // position too high
            + "try {\n"
            + "  comment.replaceData(25, 1, '');\n"
            + "} catch(e) { alert('exception-replacePosTooHigh'); }\n"
            + "alert(comment.data);\n"
            // count zero
            + "comment.replaceData(3, 0, 'C');\n"
            + "alert(comment.data);\n"
            // count negative
            + "try {\n"
            + "  comment.replaceData(1, -1, '');\n"
            + "} catch(e) { alert('exception-replaceCntNegative'); }\n"
            + "alert(comment.data);\n"
            // count too high
            + "comment.replaceData(1, 25, 'X');\n"
            + "alert(comment.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "myComment", "t", "myComment", "omm", "myComment", "m", "myComment",
        "exception-substringPosNegative", "myComment",
        "exception-substringPosTooHigh", "myComment",
        "", "myComment",
        "exception-substringCntNegative", "myComment",
        "yComment", "myComment" })
    public void substringData() throws Exception {
        final String test = ""
            + "alert(comment.data);\n"
            // back
            + "alert(comment.substringData(8, 1));\n"
            + "alert(comment.data);\n"
            // middle
            + "alert(comment.substringData(3, 3));\n"
            + "alert(comment.data);\n"
            // front
            + "alert(comment.substringData(0, 1));\n"
            + "alert(comment.data);\n"
            // position negative
            + "try {\n"
            + "  comment.substringData(-1, 1);\n"
            + "} catch(e) { alert('exception-substringPosNegative'); }\n"
            + "alert(comment.data);\n"
            // position too high
            + "try {\n"
            + "  comment.substringData(25, 1);\n"
            + "} catch(e) { alert('exception-substringPosTooHigh'); }\n"
            + "alert(comment.data);\n"
            // count zero
            + "alert(comment.substringData(1, 0));\n"
            + "alert(comment.data);\n"
            // count negative
            + "try {\n"
            + "  comment.substringData(1, -1);\n"
            + "} catch(e) { alert('exception-substringCntNegative'); }\n"
            + "alert(comment.data);\n"
            // count too high
            + "alert(comment.substringData(1, 25));\n"
            + "alert(comment.data);\n";

        tester(test);
    }

    private void property(final String property) throws Exception {
        tester("alert(comment." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<root><!--myComment--></root>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    try {\n"
            + "      var comment = root.firstChild;\n"
            + test
            + "    } catch(e) { alert('exception'+e.message); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
