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
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMText}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMTextTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Object]", FF = "no ActiveX")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(text));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null", FF = "no ActiveX")
    public void attributes() throws Exception {
        property("attributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "", FF = "no ActiveX")
    public void baseName() throws Exception {
        property("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0", FF = "no ActiveX")
    public void childNodes() throws Exception {
        tester("alert(text.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "X", "X", "X",
                "exception-setNull",
                "", "", "",
                "test", "test", "test",
                "test\ntest", "test\ntest", "test\ntest",
                "<tag/>", "<tag/>", "<tag/>" },
        FF = "no ActiveX")
    public void data() throws Exception {
        final String test = ""
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.text);\n"
            // null
            + "try {\n"
            + "  text.data = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "text.data = '';\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.text);\n"
            // normal
            + "text.data = 'test';\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.text);\n"
            // linebreak
            + "text.data = 'test\\ntest';\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.text);\n"
            // xml
            + "text.data = '<tag/>';\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.text);\n";

        final String xml = ""
            + "<root>"
            + "X"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null", FF = "no ActiveX")
    public void dataType() throws Exception {
        property("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null", FF = "no ActiveX")
    public void definition() throws Exception {
        property("definition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null", FF = "no ActiveX")
    public void firstChild() throws Exception {
        property("firstChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null", FF = "no ActiveX")
    public void lastChild() throws Exception {
        property("lastChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "4", "9", "6" },
            FF = "no ActiveX")
    public void length() throws Exception {
        final String test = ""
            + "alert(text.length);\n"
            // empty
            + "text.data = '';\n"
            + "alert(text.length);\n"
            // normal
            + "text.data = 'test';\n"
            + "alert(text.length);\n"
            // linebreak
            + "text.data = 'test\\ntest';\n"
            + "alert(text.length);\n"
            // xml
            + "text.data = '<tag/>';\n"
            + "alert(text.length);\n";

        final String xml = ""
            + "<root>"
            + "X"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "", FF = "no ActiveX")
    public void namespaceURI() throws Exception {
        property("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "#text", FF = "no ActiveX")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "3", FF = "no ActiveX")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "X", "X", "X",
                        "exception-setNull",
                        "", "", "",
                        "test", "test", "test",
                        "test\ntest", "test\ntest", "test\ntest",
                        "<tag/>", "<tag/>", "<tag/>" },
        FF = "no ActiveX")
    public void nodeValue() throws Exception {
        final String test = ""
            + "alert(text.nodeValue);\n"
            + "alert(text.data);\n"
            + "alert(text.text);\n"
            // null
            + "try {\n"
            + "  text.nodeValue = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "text.nodeValue = '';\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.data);\n"
            + "alert(text.text);\n"
            // normal
            + "text.nodeValue = 'test';\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.data);\n"
            + "alert(text.text);\n"
            // linebreak
            + "text.nodeValue = 'test\\ntest';\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.data);\n"
            + "alert(text.text);\n"
            // xml
            + "text.nodeValue = '<tag/>';\n"
            + "alert(text.nodeValue);\n"
            + "alert(text.data);\n"
            + "alert(text.text);\n";

        final String xml = ""
            + "<root>"
            + "X"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" }, FF = "no ActiveX")
    @NotYetImplemented(IE)
    // Xerxes is the problem here as they do treat whitespaces as text nodes.
    public void nodeValue_empty() throws Exception {
        final String test =
            // space
              "alert(text.firstChild == null);\n"
            // tab
            + "text = text.nextSibling;\n"
            + "alert(text.firstChild == null);\n"
            // linebreak
            + "text = text.nextSibling;\n"
            + "alert(text.firstChild == null);\n";

        final String xml = ""
            + "<root>"
            + "<space> </space>"
            + "<tab>\t</tab>"
            + "<lineBreak>\n</lineBreak>"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true", FF = "no ActiveX")
    public void ownerDocument() throws Exception {
        tester("alert(text.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true", FF = "no ActiveX")
    public void ownerDocument_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var text = doc.createTextNode('something');\n"
            + "    try {\n"
            + "      alert(text.ownerDocument === doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true", FF = "no ActiveX")
    public void parentNode() throws Exception {
        tester("alert(root === text.parentNode);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true", FF = "no ActiveX")
    public void parentNode_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var text = doc.createTextNode('something');\n"
            + "    try {\n"
            + "      alert(text.parentNode == null);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "", FF = "no ActiveX")
    public void prefix() throws Exception {
        property("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "X", "X", "X",
                        "exception-setNull",
                        "", "", "",
                        "test", "test", "test",
                        "test\ntest", "test\ntest", "test\ntest",
                        "<tag/>", "<tag/>", "<tag/>" },
            FF = "no ActiveX")
    public void text() throws Exception {
        final String test = ""
            + "alert(text.text);\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            // null
            + "try {\n"
            + "  text.text = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "text.text = '';\n"
            + "alert(text.text);\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            // normal
            + "text.text = 'test';\n"
            + "alert(text.text);\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            // linebreak
            + "text.text = 'test\\ntest';\n"
            + "alert(text.text);\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n"
            // xml
            + "text.text = '<tag/>';\n"
            + "alert(text.text);\n"
            + "alert(text.data);\n"
            + "alert(text.nodeValue);\n";

        final String xml = ""
            + "<root>"
            + "X"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "text", "text\r\ntext", "  text  text  " }, FF = "no ActiveX")
    public void xml() throws Exception {
        final String test =
            // text
              "alert(text.firstChild.xml);\n"
            // linebreak
            + "text = text.nextSibling;\n"
            + "alert(text.firstChild.xml);\n"
            // space
            + "text = text.nextSibling;\n"
            + "alert(text.firstChild.xml);\n";

        final String xml = ""
            + "<root>"
            + "<text>text</text>"
            + "<lineBreak>text\ntext</lineBreak>"
            + "<space>  text  text  </space>"
            + "</root>";

        tester(test, xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "", FF = "no ActiveX")
    public void xml_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var text = doc.createTextNode('');\n"
            + "    try {\n"
            + "      alert(text.xml);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myText", "myText-appended", "exception-appendNull", "myText-appended", "myText-appended\n" },
            FF = "no ActiveX")
    public void appendData() throws Exception {
        final String test = ""
            + "alert(text.data);\n"
            // normal
            + "text.appendData('-appended');\n"
            + "alert(text.data);\n"
            // null
            + "try {\n"
            + "  text.appendData(null);\n"
            + "} catch(e) { alert('exception-appendNull'); }\n"
            // empty
            + "text.appendData('');\n"
            + "alert(text.data);\n"
            // linebreak
            + "text.appendData('\\n');\n"
            + "alert(text.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myText", "myTex", "myx", "yx",
                        "exception-deletePosNegative", "yx",
                        "exception-deletePosTooHigh", "yx",
                        "yx",
                        "exception-deleteCntNegative", "yx",
                        "y" },
        FF = "no ActiveX")
    public void deleteData() throws Exception {
        final String test = ""
            + "alert(text.data);\n"
            // back
            + "text.deleteData(5, 1);\n"
            + "alert(text.data);\n"
            // middle
            + "text.deleteData(2, 2);\n"
            + "alert(text.data);\n"
            // front
            + "text.deleteData(0, 1);\n"
            + "alert(text.data);\n"
            // position negative
            + "try {\n"
            + "  text.deleteData(-1, 1);\n"
            + "} catch(e) { alert('exception-deletePosNegative'); }\n"
            + "alert(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.deleteData(5, 1);\n"
            + "} catch(e) { alert('exception-deletePosTooHigh'); }\n"
            + "alert(text.data);\n"
            // count zero
            + "text.deleteData(1, 0);\n"
            + "alert(text.data);\n"
            // count negative
            + "try {\n"
            + "  text.deleteData(1, -1);\n"
            + "} catch(e) { alert('exception-deleteCntNegative'); }\n"
            + "alert(text.data);\n"
            // count too high
            + "text.deleteData(1, 5);\n"
            + "alert(text.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myText", "myText-b", "myT-m-ext-b", "f-myT-m-ext-b",
                        "exception-insertNull", "f-myT-m-ext-b", "\nf-myT-m-ext-b",
                        "\nf-myT-m-ext-b",
                        "exception-insertPosNegative", "\nf-myT-m-ext-b",
                        "\nf-myT-m-ext-b",
                        "exception-insertPosTooHigh", "\nf-myT-m-ext-b" },
           FF = "no ActiveX")
    public void insertData() throws Exception {
        final String test = ""
            + "alert(text.data);\n"
            // back
            + "text.insertData(6, '-b');\n"
            + "alert(text.data);\n"
            // middle
            + "text.insertData(3, '-m-');\n"
            + "alert(text.data);\n"
            // front
            + "text.insertData(0, 'f-');\n"
            + "alert(text.data);\n"
            // null
            + "try {\n"
            + "  text.insertData(0, null);\n"
            + "} catch(e) { alert('exception-insertNull'); }\n"
            // empty
            + "text.insertData(5, '');\n"
            + "alert(text.data);\n"
            // linebreak
            + "text.insertData(0, '\\n');\n"
            + "alert(text.data);\n"
            // empty and position negative
            + "text.insertData(-1, '');\n"
            + "alert(text.data);\n"
            // position negative
            + "try {\n"
            + "  text.insertData(-1, 'X');\n"
            + "} catch(e) { alert('exception-insertPosNegative'); }\n"
            + "alert(text.data);\n"
            // empty and position too high
            + "text.insertData(25, '');\n"
            + "alert(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.insertData(25, 'Y');\n"
            + "} catch(e) { alert('exception-insertPosTooHigh'); }\n"
            + "alert(text.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myText", "myTex-b", "my-m-x-b", "f-y-m-x-b",
                        "exception-replaceNull", "f-y--x-b", "f-y\nx-b",
                        "exception-replacePosNegative", "f-y\nx-b",
                        "exception-replacePosTooHigh", "f-y\nx-b",
                        "f-y\nx-b",
                        "exception-replaceCntNegative", "f-y\nx-b",
                        "f" },
           FF = "no ActiveX")
    public void replaceData() throws Exception {
        final String test = ""
            + "alert(text.data);\n"
            // back
            + "text.replaceData(5, 1, '-b');\n"
            + "alert(text.data);\n"
            // middle
            + "text.replaceData(2, 2, '-m-');\n"
            + "alert(text.data);\n"
            // front
            + "text.replaceData(0, 1, 'f-');\n"
            + "alert(text.data);\n"
            // null
            + "try {\n"
            + "  text.replaceData(0, 1, null);\n"
            + "} catch(e) { alert('exception-replaceNull'); }\n"
            // empty
            + "text.replaceData(4, 1, '');\n"
            + "alert(text.data);\n"
            // linebreak
            + "text.replaceData(3, 2, '\\n');\n"
            + "alert(text.data);\n"
            // position negative
            + "try {\n"
            + "  text.replaceData(-1, 1, '');\n"
            + "} catch(e) { alert('exception-replacePosNegative'); }\n"
            + "alert(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.replaceData(25, 1, '');\n"
            + "} catch(e) { alert('exception-replacePosTooHigh'); }\n"
            + "alert(text.data);\n"
            // count zero
            + "text.replaceData(1, 0, '');\n"
            + "alert(text.data);\n"
            // count negative
            + "try {\n"
            + "  text.replaceData(1, -1, '');\n"
            + "} catch(e) { alert('exception-replaceCntNegative'); }\n"
            + "alert(text.data);\n"
            // count too high
            + "text.replaceData(1, 25, '');\n"
            + "alert(text.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "2", "#text", "2", "my", "#text", "4", "Text", "true",
                        "exception-splitPosNegative", "my",
                        "exception-splitPosTooHigh", "my" },
            FF = "no ActiveX")
    public void splitText() throws Exception {
        final String test = ""
            + "alert(root.childNodes.length);\n"
            + "var node = text.splitText(2);\n"
            + "alert(root.childNodes.length);\n"
            + "alert(root.childNodes[0].nodeName);\n"
            + "alert(root.childNodes[0].length);\n"
            + "alert(root.childNodes[0].text);\n"
            + "alert(root.childNodes[1].nodeName);\n"
            + "alert(root.childNodes[1].length);\n"
            + "alert(root.childNodes[1].text);\n"
            + "alert(node === root.childNodes[1]);\n"
            // position negative
            + "try {\n"
            + "  text.splitText(-1);\n"
            + "} catch(e) { alert('exception-splitPosNegative'); }\n"
            + "alert(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.splitText(25);\n"
            + "} catch(e) { alert('exception-splitPosTooHigh'); }\n"
            + "alert(text.data);\n";
        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myText", "t", "myText", "Te", "myText", "m", "myText",
                        "exception-substringPosNegative", "myText",
                        "exception-substringPosTooHigh", "myText",
                        "", "myText",
                        "exception-substringCntNegative", "myText",
                        "yText", "myText" },
            FF = "no ActiveX")
    public void substringData() throws Exception {
        final String test = ""
            + "alert(text.data);\n"
            // back
            + "alert(text.substringData(5, 1));\n"
            + "alert(text.data);\n"
            // middle
            + "alert(text.substringData(2, 2));\n"
            + "alert(text.data);\n"
            // front
            + "alert(text.substringData(0, 1));\n"
            + "alert(text.data);\n"
            // position negative
            + "try {\n"
            + "  text.substringData(-1, 1);\n"
            + "} catch(e) { alert('exception-substringPosNegative'); }\n"
            + "alert(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.substringData(25, 1);\n"
            + "} catch(e) { alert('exception-substringPosTooHigh'); }\n"
            + "alert(text.data);\n"
            // count zero
            + "alert(text.substringData(1, 0));\n"
            + "alert(text.data);\n"
            // count negative
            + "try {\n"
            + "  text.substringData(1, -1);\n"
            + "} catch(e) { alert('exception-substringCntNegative'); }\n"
            + "alert(text.data);\n"
            // count too high
            + "alert(text.substringData(1, 25));\n"
            + "alert(text.data);\n";

        tester(test);
    }

    private void property(final String property) throws Exception {
        tester("alert(text." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<root>myText</root>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    try {\n"
            + "      var text = root.firstChild;\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
