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
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    CREATE_XMLDOMDOCUMENT_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMDocumentFragment}.
 *
 * @version $Revision$
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMDocumentFragmentTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(fragment));\n");
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
    @Alerts({ "4", "#cdata-section=child-cdata", "true", "#comment=child-comment", "true",
        "child-element=null", "true", "#text=child-text", "true" })
    public void childNodes() throws Exception {
        final String test = ""
            + "var cdata = doc.createCDATASection('child-cdata');\n"
            + "fragment.appendChild(cdata);\n"
            + "var comment = doc.createComment('child-comment');\n"
            + "fragment.appendChild(comment);\n"
            + "var element = doc.createElement('child-element');\n"
            + "fragment.appendChild(element);\n"
            + "var text = doc.createTextNode('child-text');\n"
            + "fragment.appendChild(text);\n"
            + "var fragment2 = doc.createDocumentFragment();\n"
            + "fragment.appendChild(fragment2);\n"
            + "alert(fragment.childNodes.length);\n"
            // cdata
            + "debug(fragment.childNodes[0]);\n"
            + "alert(fragment.childNodes[0] === cdata);\n"
            // comment
            + "debug(fragment.childNodes[1]);\n"
            + "alert(fragment.childNodes[1] === comment);\n"
            // element
            + "debug(fragment.childNodes[2]);\n"
            + "alert(fragment.childNodes[2] === element);\n"
            // text
            + "debug(fragment.childNodes[3]);\n"
            + "alert(fragment.childNodes[3] === text);\n";
            // no fragment

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("0")
    public void childNodes_none() throws Exception {
        tester("alert(fragment.childNodes.length);\n");
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
    @Alerts({ "child-element=null", "true" })
    public void firstChild() throws Exception {
        final String test = ""
            + "var element = doc.createElement('child-element');\n"
            + "fragment.appendChild(element);\n"
            + "debug(fragment.firstChild);\n"
            + "alert(fragment.firstChild === element);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void firstChild_none() throws Exception {
        property("firstChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "child-element=null", "true" })
    public void lastChild() throws Exception {
        final String test = ""
            + "var element = doc.createElement('child-element');\n"
            + "fragment.appendChild(element);\n"
            + "debug(fragment.lastChild);\n"
            + "alert(fragment.lastChild === element);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void lastChild_none() throws Exception {
        property("lastChild");
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
    @Alerts("#document-fragment")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("11")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "null", "exception-setNull", "exception-setEmpty", "exception-set" })
    public void nodeValue() throws Exception {
        final String test = ""
            + "alert(fragment.nodeValue);\n"
            // null
            + "try {\n"
            + "  fragment.nodeValue = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  fragment.nodeValue = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  fragment.nodeValue = 'test';\n"
            + "} catch(e) { alert('exception-set'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void ownerDocument() throws Exception {
        tester("alert(fragment.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("null")
    public void parentNode() throws Exception {
        property("parentNode");
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
    @Alerts("child-cdatagrand-child-textchild-text")
    public void text() throws Exception {
        final String test = ""
            + "var cdata = doc.createCDATASection('child-cdata');\n"
            + "fragment.appendChild(cdata);\n"
            + "var comment = doc.createComment('child-comment');\n"
            + "fragment.appendChild(comment);\n"
            + "var element = doc.createElement('child-element');\n"
            + "fragment.appendChild(element);\n"
            + "var element2 = doc.createElement('child-element2');\n"
            + "fragment.appendChild(element2);\n"
            + "var text2 = doc.createTextNode('grand-child-text');\n"
            + "element2.appendChild(text2);\n"
            + "var text = doc.createTextNode('child-text');\n"
            + "fragment.appendChild(text);\n"
            + "var fragment2 = doc.createDocumentFragment();\n"
            + "fragment.appendChild(fragment2);\n"
            + "alert(fragment.text);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "exception-set", "exception-setEmpty", "exception-setNull" })
    public void text_set() throws Exception {
        final String test =
            // normal
              "try {\n"
            + "  fragment.text = 'text';\n"
            + "} catch(e) { alert('exception-set'); }\n"
            // empty
            + "try {\n"
            + "  fragment.text = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // null
            + "try {\n"
            + "  fragment.text = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("c\nct\nt")
    public void text_lineBreak() throws Exception {
        final String test = ""
            + "var cdata = doc.createCDATASection('c\\nc');\n"
            + "fragment.appendChild(cdata);\n"
            + "var text = doc.createTextNode('t\\nt');\n"
            + "fragment.appendChild(text);\n"
            + "alert(fragment.text);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void text_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var fragment = doc.createDocumentFragment();\n"
            + "    try {\n"
            + "      alert(fragment.text);\n"
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
    @Alerts("<![CDATA[child-cdata]]><!--child-comment--><child-element/>"
            + "<child-element2><grand-child-element/></child-element2>child-text")
    public void xml() throws Exception {
        final String test = ""
            + "var cdata = doc.createCDATASection('child-cdata');\n"
            + "fragment.appendChild(cdata);\n"
            + "var comment = doc.createComment('child-comment');\n"
            + "fragment.appendChild(comment);\n"
            + "var element = doc.createElement('child-element');\n"
            + "fragment.appendChild(element);\n"
            + "var element2 = doc.createElement('child-element2');\n"
            + "fragment.appendChild(element2);\n"
            + "var element2a = doc.createElement('grand-child-element');\n"
            + "element2.appendChild(element2a);\n"
            + "var text = doc.createTextNode('child-text');\n"
            + "fragment.appendChild(text);\n"
            + "var fragment2 = doc.createDocumentFragment();\n"
            + "fragment.appendChild(fragment2);\n"
            + "alert(fragment.xml);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("child-text\r\n")
    public void xml_lineBreak() throws Exception {
        final String test =
                "var text = doc.createTextNode('child-text\\n');\n"
              + "fragment.appendChild(text);\n"
              + "alert(fragment.xml);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("")
    public void xml_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var fragment = doc.createDocumentFragment();\n"
            + "    try {\n"
            + "      alert(fragment.xml);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    private void property(final String property) throws Exception {
        tester("alert(fragment." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        tester(test, "<root/>");
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    var fragment = doc.createDocumentFragment();\n"
            + "    try {\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    if (e != null) {\n"
            + "      alert(e.nodeName + '=' + e.nodeValue);\n"
            + "    } else {\n"
            + "      alert('null');\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
