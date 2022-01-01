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
 * Tests for {@link XMLDOMText}.
 *
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
    @Alerts(DEFAULT = "no ActiveX",
            IE = "[object Object]")
    public void scriptableToString() throws Exception {
        tester("log(Object.prototype.toString.call(text));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "null")
    public void attributes() throws Exception {
        property("attributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "")
    public void baseName() throws Exception {
        property("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "0")
    public void childNodes() throws Exception {
        tester("log(text.childNodes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"X", "X", "X",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void data() throws Exception {
        final String test = ""
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            + "log(text.text);\n"
            // null
            + "try {\n"
            + "  text.data = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "text.data = '';\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            + "log(text.text);\n"
            // normal
            + "text.data = 'test';\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            + "log(text.text);\n"
            // linebreak
            + "text.data = 'test\\ntest';\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            + "log(text.text);\n"
            // xml
            + "text.data = '<tag/>';\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            + "log(text.text);\n";

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
    @Alerts(DEFAULT = "no ActiveX",
            IE = "null")
    public void dataType() throws Exception {
        property("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "null")
    public void definition() throws Exception {
        property("definition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "null")
    public void firstChild() throws Exception {
        property("firstChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "null")
    public void lastChild() throws Exception {
        property("lastChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"1", "0", "4", "9", "6"})
    public void length() throws Exception {
        final String test = ""
            + "log(text.length);\n"
            // empty
            + "text.data = '';\n"
            + "log(text.length);\n"
            // normal
            + "text.data = 'test';\n"
            + "log(text.length);\n"
            // linebreak
            + "text.data = 'test\\ntest';\n"
            + "log(text.length);\n"
            // xml
            + "text.data = '<tag/>';\n"
            + "log(text.length);\n";

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
    @Alerts(DEFAULT = "no ActiveX",
            IE = "")
    public void namespaceURI() throws Exception {
        property("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "#text")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "3")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"X", "X", "X",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void nodeValue() throws Exception {
        final String test = ""
            + "log(text.nodeValue);\n"
            + "log(text.data);\n"
            + "log(text.text);\n"
            // null
            + "try {\n"
            + "  text.nodeValue = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "text.nodeValue = '';\n"
            + "log(text.nodeValue);\n"
            + "log(text.data);\n"
            + "log(text.text);\n"
            // normal
            + "text.nodeValue = 'test';\n"
            + "log(text.nodeValue);\n"
            + "log(text.data);\n"
            + "log(text.text);\n"
            // linebreak
            + "text.nodeValue = 'test\\ntest';\n"
            + "log(text.nodeValue);\n"
            + "log(text.data);\n"
            + "log(text.text);\n"
            // xml
            + "text.nodeValue = '<tag/>';\n"
            + "log(text.nodeValue);\n"
            + "log(text.data);\n"
            + "log(text.text);\n";

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
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"true", "true", "true"})
    @NotYetImplemented(IE)
    // Xerxes is the problem here as they do treat whitespaces as text nodes.
    public void nodeValue_empty() throws Exception {
        final String test =
            // space
              "log(text.firstChild == null);\n"
            // tab
            + "text = text.nextSibling;\n"
            + "log(text.firstChild == null);\n"
            // linebreak
            + "text = text.nextSibling;\n"
            + "log(text.firstChild == null);\n";

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
    @Alerts(DEFAULT = "no ActiveX",
            IE = "true")
    public void ownerDocument() throws Exception {
        tester("log(text.ownerDocument === doc);\n");
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
            + "    var text = doc.createTextNode('something');\n"
            + "    try {\n"
            + "      log(text.ownerDocument === doc);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "true")
    public void parentNode() throws Exception {
        tester("log(root === text.parentNode);\n");
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
            + "    var text = doc.createTextNode('something');\n"
            + "    try {\n"
            + "      log(text.parentNode == null);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "")
    public void prefix() throws Exception {
        property("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"X", "X", "X",
                  "exception-setNull",
                  "", "", "",
                  "test", "test", "test",
                  "test\\ntest", "test\\ntest", "test\\ntest",
                  "<tag/>", "<tag/>", "<tag/>"})
    public void text() throws Exception {
        final String test = ""
            + "log(text.text);\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            // null
            + "try {\n"
            + "  text.text = null;\n"
            + "} catch(e) { log('exception-setNull'); }\n"
            // empty
            + "text.text = '';\n"
            + "log(text.text);\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            // normal
            + "text.text = 'test';\n"
            + "log(text.text);\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            // linebreak
            + "text.text = 'test\\ntest';\n"
            + "log(text.text);\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n"
            // xml
            + "text.text = '<tag/>';\n"
            + "log(text.text);\n"
            + "log(text.data);\n"
            + "log(text.nodeValue);\n";

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
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"text", "text\\r\\ntext", "##text##text##"})
    public void xml() throws Exception {
        final String test =
            // text
            "var txt = text.firstChild.xml;\n"
            + "txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "log(txt);\n"
            // linebreak
            + "text = text.nextSibling;\n"
            + "txt = text.firstChild.xml;\n"
            + "txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "log(txt);\n"
            // space
            + "text = text.nextSibling;\n"
            + "var txt = text.firstChild.xml;\n"
            + "txt = txt.replace(/\\r/g, '\\\\r');\n"
            + "txt = txt.replace(/\\n/g, '\\\\n');\n"
            + "txt = txt.replace(/ /g, '#');\n"
            + "log(txt);\n";

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
    @Alerts(DEFAULT = "no ActiveX",
            IE = "")
    public void xml_created() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var text = doc.createTextNode('');\n"
            + "    try {\n"
            + "      log(text.xml);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"myText", "myText-appended", "exception-appendNull", "myText-appended", "myText-appended\\n"})
    public void appendData() throws Exception {
        final String test = ""
            + "log(text.data);\n"
            // normal
            + "text.appendData('-appended');\n"
            + "log(text.data);\n"
            // null
            + "try {\n"
            + "  text.appendData(null);\n"
            + "} catch(e) { log('exception-appendNull'); }\n"
            // empty
            + "text.appendData('');\n"
            + "log(text.data);\n"
            // linebreak
            + "text.appendData('\\n');\n"
            + "log(text.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"myText", "myTex", "myx", "yx",
                  "exception-deletePosNegative", "yx",
                  "exception-deletePosTooHigh", "yx",
                  "yx",
                  "exception-deleteCntNegative", "yx",
                  "y"})
    public void deleteData() throws Exception {
        final String test = ""
            + "log(text.data);\n"
            // back
            + "text.deleteData(5, 1);\n"
            + "log(text.data);\n"
            // middle
            + "text.deleteData(2, 2);\n"
            + "log(text.data);\n"
            // front
            + "text.deleteData(0, 1);\n"
            + "log(text.data);\n"
            // position negative
            + "try {\n"
            + "  text.deleteData(-1, 1);\n"
            + "} catch(e) { log('exception-deletePosNegative'); }\n"
            + "log(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.deleteData(5, 1);\n"
            + "} catch(e) { log('exception-deletePosTooHigh'); }\n"
            + "log(text.data);\n"
            // count zero
            + "text.deleteData(1, 0);\n"
            + "log(text.data);\n"
            // count negative
            + "try {\n"
            + "  text.deleteData(1, -1);\n"
            + "} catch(e) { log('exception-deleteCntNegative'); }\n"
            + "log(text.data);\n"
            // count too high
            + "text.deleteData(1, 5);\n"
            + "log(text.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"myText", "myText-b", "myT-m-ext-b", "f-myT-m-ext-b",
                  "exception-insertNull", "f-myT-m-ext-b", "\\nf-myT-m-ext-b",
                  "\\nf-myT-m-ext-b",
                  "exception-insertPosNegative", "\\nf-myT-m-ext-b",
                  "\\nf-myT-m-ext-b",
                  "exception-insertPosTooHigh", "\\nf-myT-m-ext-b"})
    public void insertData() throws Exception {
        final String test = ""
            + "log(text.data);\n"
            // back
            + "text.insertData(6, '-b');\n"
            + "log(text.data);\n"
            // middle
            + "text.insertData(3, '-m-');\n"
            + "log(text.data);\n"
            // front
            + "text.insertData(0, 'f-');\n"
            + "log(text.data);\n"
            // null
            + "try {\n"
            + "  text.insertData(0, null);\n"
            + "} catch(e) { log('exception-insertNull'); }\n"
            // empty
            + "text.insertData(5, '');\n"
            + "log(text.data);\n"
            // linebreak
            + "text.insertData(0, '\\n');\n"
            + "log(text.data);\n"
            // empty and position negative
            + "text.insertData(-1, '');\n"
            + "log(text.data);\n"
            // position negative
            + "try {\n"
            + "  text.insertData(-1, 'X');\n"
            + "} catch(e) { log('exception-insertPosNegative'); }\n"
            + "log(text.data);\n"
            // empty and position too high
            + "text.insertData(25, '');\n"
            + "log(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.insertData(25, 'Y');\n"
            + "} catch(e) { log('exception-insertPosTooHigh'); }\n"
            + "log(text.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"myText", "myTex-b", "my-m-x-b", "f-y-m-x-b",
                  "exception-replaceNull", "f-y--x-b", "f-y\\nx-b",
                  "exception-replacePosNegative", "f-y\\nx-b",
                  "exception-replacePosTooHigh", "f-y\\nx-b",
                  "f-y\\nx-b",
                  "exception-replaceCntNegative", "f-y\\nx-b",
                  "f"})
    public void replaceData() throws Exception {
        final String test = ""
            + "log(text.data);\n"
            // back
            + "text.replaceData(5, 1, '-b');\n"
            + "log(text.data);\n"
            // middle
            + "text.replaceData(2, 2, '-m-');\n"
            + "log(text.data);\n"
            // front
            + "text.replaceData(0, 1, 'f-');\n"
            + "log(text.data);\n"
            // null
            + "try {\n"
            + "  text.replaceData(0, 1, null);\n"
            + "} catch(e) { log('exception-replaceNull'); }\n"
            // empty
            + "text.replaceData(4, 1, '');\n"
            + "log(text.data);\n"
            // linebreak
            + "text.replaceData(3, 2, '\\n');\n"
            + "log(text.data);\n"
            // position negative
            + "try {\n"
            + "  text.replaceData(-1, 1, '');\n"
            + "} catch(e) { log('exception-replacePosNegative'); }\n"
            + "log(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.replaceData(25, 1, '');\n"
            + "} catch(e) { log('exception-replacePosTooHigh'); }\n"
            + "log(text.data);\n"
            // count zero
            + "text.replaceData(1, 0, '');\n"
            + "log(text.data);\n"
            // count negative
            + "try {\n"
            + "  text.replaceData(1, -1, '');\n"
            + "} catch(e) { log('exception-replaceCntNegative'); }\n"
            + "log(text.data);\n"
            // count too high
            + "text.replaceData(1, 25, '');\n"
            + "log(text.data);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"1", "2", "#text", "2", "my", "#text", "4", "Text", "true",
                  "exception-splitPosNegative", "my",
                  "exception-splitPosTooHigh", "my"})
    public void splitText() throws Exception {
        final String test = ""
            + "log(root.childNodes.length);\n"
            + "var node = text.splitText(2);\n"
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
            + "  text.splitText(-1);\n"
            + "} catch(e) { log('exception-splitPosNegative'); }\n"
            + "log(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.splitText(25);\n"
            + "} catch(e) { log('exception-splitPosTooHigh'); }\n"
            + "log(text.data);\n";
        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"myText", "t", "myText", "Te", "myText", "m", "myText",
                  "exception-substringPosNegative", "myText",
                  "exception-substringPosTooHigh", "myText",
                  "", "myText",
                  "exception-substringCntNegative", "myText",
                  "yText", "myText"})
    public void substringData() throws Exception {
        final String test = ""
            + "log(text.data);\n"
            // back
            + "log(text.substringData(5, 1));\n"
            + "log(text.data);\n"
            // middle
            + "log(text.substringData(2, 2));\n"
            + "log(text.data);\n"
            // front
            + "log(text.substringData(0, 1));\n"
            + "log(text.data);\n"
            // position negative
            + "try {\n"
            + "  text.substringData(-1, 1);\n"
            + "} catch(e) { log('exception-substringPosNegative'); }\n"
            + "log(text.data);\n"
            // position too high
            + "try {\n"
            + "  text.substringData(25, 1);\n"
            + "} catch(e) { log('exception-substringPosTooHigh'); }\n"
            + "log(text.data);\n"
            // count zero
            + "log(text.substringData(1, 0));\n"
            + "log(text.data);\n"
            // count negative
            + "try {\n"
            + "  text.substringData(1, -1);\n"
            + "} catch(e) { log('exception-substringCntNegative'); }\n"
            + "log(text.data);\n"
            // count too high
            + "log(text.substringData(1, 25));\n"
            + "log(text.data);\n";

        tester(test);
    }

    private void property(final String property) throws Exception {
        tester("log(text." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<root>myText</root>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + LOG_TITLE_NORMALIZE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    try {\n"
            + "      var text = root.firstChild;\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(createTestHTML(html));
    }
}
