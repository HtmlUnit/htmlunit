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
    LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
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
 * Tests for {@link XMLDOMDocumentType}.
 *
 * @version $Revision$
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMDocumentTypeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void scriptableToString() throws Exception {
        tester("alert(Object.prototype.toString.call(doctype));\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("0")
    public void attributes() throws Exception {
        tester("alert(doctype.attributes.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("a")
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
        tester("alert(doctype.childNodes.length);\n");
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
    @Alerts("0")
    public void entities() throws Exception {
        tester("alert(doctype.entities.length);\n");
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
    @Alerts("a")
    public void name() throws Exception {
        property("name");
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
    @Alerts("a")
    public void nodeName() throws Exception {
        property("nodeName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("10")
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
            + "alert(doctype.nodeValue);\n"
            // null
            + "try {\n"
            + "  doctype.nodeValue = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n"
            // empty
            + "try {\n"
            + "  doctype.nodeValue = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // normal
            + "try {\n"
            + "  doctype.nodeValue = 'test';\n"
            + "} catch(e) { alert('exception-set'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("0")
    public void notations() throws Exception {
        tester("alert(doctype.notations.length);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void ownerDocument() throws Exception {
        tester("alert(doctype.ownerDocument === doc);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("true")
    public void parentNode() throws Exception {
        tester("alert(doctype.parentNode === doc);\n");
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
    public void text() throws Exception {
        property("text");
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
            + "  doctype.text = 'text';\n"
            + "} catch(e) { alert('exception-set'); }\n"
            // empty
            + "try {\n"
            + "  doctype.text = '';\n"
            + "} catch(e) { alert('exception-setEmpty'); }\n"
            // null
            + "try {\n"
            + "  doctype.text = null;\n"
            + "} catch(e) { alert('exception-setNull'); }\n";

        tester(test);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("<!DOCTYPE a [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>")
    @NotYetImplemented(IE)
    // It seems we currently do not have access to the DTD.
    public void xml() throws Exception {
        property("xml");
    }

    private void property(final String property) throws Exception {
        tester("alert(doctype." + property + ");\n");
    }

    private void tester(final String test) throws Exception {
        final String xml = "<!DOCTYPE a [ <!ELEMENT a (b+)> <!ELEMENT b (#PCDATA)> ]>\n"
            + "<a><b>1</b><b>2</b></a>";

        tester(test, xml);
    }

    private void tester(final String test, final String xml) throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDOMDocumentFromURL("'second.xml'") + ";\n"
            + "    var root = doc.documentElement;\n"
            + "    try {\n"
            + "      var doctype = doc.doctype;\n"
            + test
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
