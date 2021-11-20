/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
 * Tests for {@link XMLDOMAttribute}.
 *
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
    @Alerts(DEFAULT = "no ActiveX",
            IE = "[object Object]")
    public void scriptableToString() throws Exception {
        tester("log(Object.prototype.toString.call(att));\n");
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
            IE = "testAttr")
    public void baseName() throws Exception {
        property("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"1", "#text", "test", "true", "true"})
    public void childNodes() throws Exception {
        final String test = ""
            + "log(att.childNodes.length);\n"
            + "log(att.childNodes.item(0).nodeName);\n"
            + "log(att.childNodes.item(0).text);\n"
            + "log(att.childNodes.item(0).parentNode === att);\n"
            + "log(att.childNodes.item(0).ownerDocument === doc);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "0")
    public void childNodes_empty() throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr=''/>"
            + "</root>";

        tester("log(att.childNodes.length);\n", xml);
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
            IE = {"true", "#text", "test", "true", "true"})
    public void firstChild() throws Exception {
        final String test = ""
            + "log(att.firstChild != null);\n"
            + "log(att.firstChild.nodeName);\n"
            + "log(att.firstChild.text);\n"
            + "log(att.firstChild.parentNode === att);\n"
            + "log(att.firstChild.ownerDocument === doc);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "false")
    public void firstChild_empty() throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr=''/>"
            + "</root>";

        tester("log(att.firstChild != null);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"true", "#text", "test", "true", "true"})
    public void lastChild() throws Exception {
        final String test = ""
            + "log(att.lastChild != null);\n"
            + "log(att.lastChild.nodeName);\n"
            + "log(att.lastChild.text);\n"
            + "log(att.firstChild.parentNode === att);\n"
            + "log(att.firstChild.ownerDocument === doc);\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "false")
    public void lastChild_empty() throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr=''/>"
            + "</root>";

        tester("log(att.lastChild != null);\n", xml);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"testAttr", "exception-write"})
    public void name() throws Exception {
        final String test = ""
            + "try {\n"
            + "  log(att.name);\n"
            + "} catch(e) { log('exception-read'); }\n"
            + "try {\n"
            + "  att.name = 'other';\n"
            + "} catch(e) { log('exception-write'); }\n";

        tester(test);
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
            IE = "testAttr")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "2")
    public void nodeType() throws Exception {
        property("nodeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"test", "other", "other", "other", "1"})
    public void nodeValue() throws Exception {
        final String test = ""
            + "try {\n"
            + "  log(att.nodeValue);\n"
            + "} catch(e) { log('exception-read'); }\n"
            + "try {\n"
            + "  att.nodeValue = 'other';\n"
            + "  log(att.nodeValue);\n"
            + "  log(att.text);\n"
            + "  log(att.value);\n"
            + "  log(att.childNodes.length);\n"
            + "} catch(e) { log('exception-write'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "true")
    public void ownerDocument() throws Exception {
        tester("log(att.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "true")
    public void ownerDocument_created() throws Exception {
        final String html = LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    var att = doc.createAttribute('something');\n"
            + "    try {\n"
            + "      log(att.ownerDocument === doc);\n"
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
        tester("log(att.parentNode == null);\n");
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
            IE = "true")
    public void specified() throws Exception {
        property("specified");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "false")
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
            IE = {"test", "other", "other", "other", "1"})
    public void text() throws Exception {
        final String test = ""
            + "try {\n"
            + "  log(att.text);\n"
            + "} catch(e) { log('exception-read'); }\n"
            + "try {\n"
            + "  att.text = 'other';\n"
            + "  log(att.text);\n"
            + "  log(att.nodeValue);\n"
            + "  log(att.value);\n"
            + "  log(att.childNodes.length);\n"
            + "} catch(e) { log('exception-write'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"test", "other", "other", "other", "1"})
    public void value() throws Exception {
        final String test = ""
            + "try {\n"
            + "  log(att.value);\n"
            + "} catch(e) { log('exception-read'); }\n"
            + "try {\n"
            + "  att.value = 'other';\n"
            + "  log(att.value);\n"
            + "  log(att.nodeValue);\n"
            + "  log(att.text);\n"
            + "  log(att.childNodes.length);\n"
            + "} catch(e) { log('exception-write'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "testAttr=\"test\"")
    public void xml() throws Exception {
        property("xml");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "testAttr=\"\"")
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
    @Alerts(DEFAULT = "no ActiveX",
            IE = "undefined")
    public void not_baseURI() throws Exception {
        property("baseURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "undefined")
    public void not_expando() throws Exception {
        property("expando");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "undefined")
    public void not_localName() throws Exception {
        property("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "undefined")
    public void not_textContent() throws Exception {
        property("textContent");
    }

    private void property(final String property) throws Exception {
        tester("log(att." + property + ");\n");
    }

    private void property(final String property, final String xml) throws Exception {
        tester("log(att." + property + ");\n", xml);
    }

    private void tester(final String test) throws Exception {
        final String xml = ""
            + "<root>"
            + "<elem testAttr='test'/>"
            + "</root>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    try {\n"
            + "      var elem = doc.documentElement.firstChild;\n"
            + "      var att = elem.getAttributeNode('testAttr');\n"
            + test
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(createTestHTML(html));
    }
}
