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
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callCreateXMLDOMDocument;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callLoadXMLDOMDocumentFromString;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callLoadXMLDOMDocumentFromURL;
import static com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLTestHelper.createTestHTML;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link XMLDOMParseError}.
 *
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XMLDOMParseErrorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = "[object Object]")
    public void scriptableToString() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "    try {\n"
            + "      log(Object.prototype.toString.call(doc.parseError));\n"
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
            IE = {"false", "true", "true", "true", "true", "true", "true", "true", "exception-Other"})
    public void parseError_createXMLDocument() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    try {\n"
            + "      var doc = " + callCreateXMLDOMDocument() + ";\n"
            + "      log(doc.parseError == null);\n"
            + "      try {\n"
            + "        log(doc.parseError.errorCode === 0);\n"
            + "        log(doc.parseError.filepos === 0);\n"
            + "        log(doc.parseError.line === 0);\n"
            + "        log(doc.parseError.linepos === 0);\n"
            + "        log(doc.parseError.reason === '');\n"
            + "        log(doc.parseError.srcText === '');\n"
            + "        log(doc.parseError.url === '');\n"
            + "      } catch(e) { log('exception-MSXML'); }\n"
            + "      try {\n"
            + "        log(doc.documentElement.nodeName == 'parsererror');\n"
            + "        log(doc.documentElement.childNodes[0].nodeValue);\n"
            + "      } catch(e) { log('exception-Other'); }\n"
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
            IE = {"false", "false", "false", "false", "false", "false", "false", "true", "exception-Other"})
    public void parseError_loadXML() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var text='<root><element></root>';\n"
            + "    try {\n"
            + "      var doc = " + callLoadXMLDOMDocumentFromString("text") + ";\n"
            + "      log(doc.parseError == null);\n"
            + "      try {\n"
            + "        log(doc.parseError.errorCode === 0);\n"
            + "        log(doc.parseError.filepos === 0);\n"
            + "        log(doc.parseError.line === 0);\n"
            + "        log(doc.parseError.linepos === 0);\n"
            + "        log(doc.parseError.reason === '');\n"
            + "        log(doc.parseError.srcText === '');\n"
            + "        log(doc.parseError.url === '');\n"
            + "      } catch(e) { log('exception-MSXML'); }\n"
            + "      try {\n"
            + "        log(doc.documentElement.nodeName == 'parsererror');\n"
            + "        log(doc.documentElement.childNodes[0].nodeValue === '');\n"
            + "      } catch(e) { log('exception-Other'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_STRING_FUNCTION;

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"false", "false", "false", "false", "false", "false", "false", "false", "exception-Other"})
    public void parseError_load() throws Exception {
        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    try {\n"
            + "      var doc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "      log(doc.parseError == null);\n"
            + "      try {\n"
            + "        log(doc.parseError.errorCode === 0);\n"
            + "        log(doc.parseError.filepos === 0);\n"
            + "        log(doc.parseError.line === 0);\n"
            + "        log(doc.parseError.linepos === 0);\n"
            + "        log(doc.parseError.reason === '');\n"
            + "        log(doc.parseError.srcText === '');\n"
            + "        log(doc.parseError.url === '');\n"
            + "      } catch(e) { log('exception-MSXML'); }\n"
            + "      try {\n"
            + "        log(doc.documentElement.nodeName == 'parsererror');\n"
            + "        log(doc.documentElement.childNodes[0].nodeValue === '');\n"
            + "      } catch(e) { log('exception-Other'); }\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

        final String xml = ""
            + "<root>\n"
            + "  <element>\n"
            + "</root>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(createTestHTML(html));
    }
}
