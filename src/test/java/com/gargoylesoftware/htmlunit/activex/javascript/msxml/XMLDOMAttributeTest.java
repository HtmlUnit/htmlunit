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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMAttribute}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMAttributeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "[object Object]")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(att));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void attributes() throws Exception {
        property("attributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "testAttr")
    public void baseName() throws Exception {
        property("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "1", "#text", "test", "true", "true" })
    public void childNodes() throws Exception {
        final String test = ""
            + "alert(att.childNodes.length);\n"
            + "alert(att.childNodes.item(0).nodeName);\n"
            + "alert(att.childNodes.item(0).text);\n"
            + "alert(att.childNodes.item(0).parentNode === att);\n"
            + "alert(att.childNodes.item(0).ownerDocument === doc);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "0")
    public void childNodes_empty() throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr=''/>"
            + "</root>";

        tester("alert(att.childNodes.length);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void dataType() throws Exception {
        property("dataType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "null")
    public void definition() throws Exception {
        property("definition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "true", "#text", "test", "true", "true" })
    public void firstChild() throws Exception {
        final String test = ""
            + "alert(att.firstChild != null);\n"
            + "alert(att.firstChild.nodeName);\n"
            + "alert(att.firstChild.text);\n"
            + "alert(att.firstChild.parentNode === att);\n"
            + "alert(att.firstChild.ownerDocument === doc);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "false")
    public void firstChild_empty() throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr=''/>"
            + "</root>";

        tester("alert(att.firstChild != null);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "true", "#text", "test", "true", "true" })
    public void lastChild() throws Exception {
        final String test = ""
            + "alert(att.lastChild != null);\n"
            + "alert(att.lastChild.nodeName);\n"
            + "alert(att.lastChild.text);\n"
            + "alert(att.firstChild.parentNode === att);\n"
            + "alert(att.firstChild.ownerDocument === doc);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "false")
    public void lastChild_empty() throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr=''/>"
            + "</root>";

        tester("alert(att.lastChild != null);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "testAttr", "exception-write" })
    public void name() throws Exception {
        final String test = ""
            + "try {\n"
            + "  alert(att.name);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  att.name = 'other';\n"
            + "} catch(e) { alert('exception-write'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "")
    public void namespaceURI() throws Exception {
        property("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "testAttr")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "2")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "test", "other", "other", "other", "1" })
    public void nodeValue() throws Exception {
        final String test = ""
            + "try {\n"
            + "  alert(att.nodeValue);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  att.nodeValue = 'other';\n"
            + "  alert(att.nodeValue);\n"
            + "  alert(att.text);\n"
            + "  alert(att.value);\n"
            + "  alert(att.childNodes.length);\n"
            + "} catch(e) { alert('exception-write'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void ownerDocument() throws Exception {
        tester("alert(att.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void ownerDocument_created() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var att = doc.createAttribute('something');\n"
            + "    try {\n"
            + "      alert(att.ownerDocument === doc);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + CREATE_XMLDOMDOCUMENT_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void parentNode() throws Exception {
        tester("alert(att.parentNode == null);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "")
    public void prefix() throws Exception {
        property("prefix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "true")
    public void specified() throws Exception {
        property("specified");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "false")
    public void specified_inDTD() throws Exception {
        final String xml = ""
            + "<!DOCTYPE root [ "
            + "<!ELEMENT root (elem+)> <!ELEMENT elem (#PCDATA)> <!ATTLIST elem testAttr CDATA \"0\">"
            + " ]>\n"
            + "<root>"
            + "<elem/>"
            + "</root>";

        property("specified", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "test", "other", "other", "other", "1" })
    public void text() throws Exception {
        final String test = ""
            + "try {\n"
            + "  alert(att.text);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  att.text = 'other';\n"
            + "  alert(att.text);\n"
            + "  alert(att.nodeValue);\n"
            + "  alert(att.value);\n"
            + "  alert(att.childNodes.length);\n"
            + "} catch(e) { alert('exception-write'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = { "test", "other", "other", "other", "1" })
    public void value() throws Exception {
        final String test = ""
            + "try {\n"
            + "  alert(att.value);\n"
            + "} catch(e) { alert('exception-read'); }\n"
            + "try {\n"
            + "  att.value = 'other';\n"
            + "  alert(att.value);\n"
            + "  alert(att.nodeValue);\n"
            + "  alert(att.text);\n"
            + "  alert(att.childNodes.length);\n"
            + "} catch(e) { alert('exception-write'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "testAttr=\"test\"")
    public void xml() throws Exception {
        property("xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "testAttr=\"\"")
    public void xml_empty() throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr=''/>"
            + "</root>";

        property("xml", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "undefined")
    public void not_baseURI() throws Exception {
        property("baseURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "undefined")
    public void not_expando() throws Exception {
        property("expando");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "undefined")
    public void not_localName() throws Exception {
        property("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX", IE = "undefined")
    public void not_textContent() throws Exception {
        property("textContent");
    }

    private void property(final String property) throws Exception {
        tester("alert(att." + property + ");\n");
    }

    private void property(final String property, final String xml) throws Exception {
        tester("alert(att." + property + ");\n", xml);
    }

    private void tester(final String test) throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr='test'/>"
            + "</root>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      var elem = doc.documentElement.firstChild;\n"
            + "      var att = elem.getAttributeNode('testAttr');\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
