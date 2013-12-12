/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.
    LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestUtil.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XMLDOMParseError}.
 *
 * @version $Revision$
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class XMLDOMParseErrorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts("[object Object]")
    public void scriptableToString() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    try {\n"
            + "      alert(Object.prototype.toString.call(doc.parseError));\n"
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
    @Alerts({ "false", "true", "true", "true", "true", "true", "true", "true", "exception-Other" })
    public void parseError_createXMLDocument() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "      alert(doc.parseError == null);\n"
            + "      try {\n"
            + "        alert(doc.parseError.errorCode === 0);\n"
            + "        alert(doc.parseError.filepos === 0);\n"
            + "        alert(doc.parseError.line === 0);\n"
            + "        alert(doc.parseError.linepos === 0);\n"
            + "        alert(doc.parseError.reason === '');\n"
            + "        alert(doc.parseError.srcText === '');\n"
            + "        alert(doc.parseError.url === '');\n"
            + "      } catch(e) { alert('exception-MSXML'); }\n"
            + "      try {\n"
            + "        alert(doc.documentElement.nodeName == 'parsererror');\n"
            + "        alert(doc.documentElement.childNodes[0].nodeValue);\n"
            + "      } catch(e) { alert('exception-Other'); }\n"
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
    @Alerts({ "false", "false", "false", "false", "false", "false", "false", "true", "exception-Other" })
    public void parseError_loadXML() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    var text='<root><element></root>';\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDOMDocumentFromString("text") + ";\n"
            + "      alert(doc.parseError == null);\n"
            + "      try {\n"
            + "        alert(doc.parseError.errorCode === 0);\n"
            + "        alert(doc.parseError.filepos === 0);\n"
            + "        alert(doc.parseError.line === 0);\n"
            + "        alert(doc.parseError.linepos === 0);\n"
            + "        alert(doc.parseError.reason === '');\n"
            + "        alert(doc.parseError.srcText === '');\n"
            + "        alert(doc.parseError.url === '');\n"
            + "      } catch(e) { alert('exception-MSXML'); }\n"
            + "      try {\n"
            + "        alert(doc.documentElement.nodeName == 'parsererror');\n"
            + "        alert(doc.documentElement.childNodes[0].nodeValue === '');\n"
            + "      } catch(e) { alert('exception-Other'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;

        loadPageWithAlerts2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "false", "false", "false", "false", "false", "false", "false", "false", "exception-Other" })
    public void parseError_load() throws Exception {
        final String html = ""
            + "  function test() {\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "      alert(doc.parseError == null);\n"
            + "      try {\n"
            + "        alert(doc.parseError.errorCode === 0);\n"
            + "        alert(doc.parseError.filepos === 0);\n"
            + "        alert(doc.parseError.line === 0);\n"
            + "        alert(doc.parseError.linepos === 0);\n"
            + "        alert(doc.parseError.reason === '');\n"
            + "        alert(doc.parseError.srcText === '');\n"
            + "        alert(doc.parseError.url === '');\n"
            + "      } catch(e) { alert('exception-MSXML'); }\n"
            + "      try {\n"
            + "        alert(doc.documentElement.nodeName == 'parsererror');\n"
            + "        alert(doc.documentElement.childNodes[0].nodeValue === '');\n"
            + "      } catch(e) { alert('exception-Other'); }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml = ""
            + "<root>\n"
            + "  <element>\n"
            + "</root>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(createTestHTML(html));
    }
}
