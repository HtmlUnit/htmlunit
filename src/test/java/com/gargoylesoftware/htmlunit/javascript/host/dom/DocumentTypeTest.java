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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link DocumentType}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class DocumentTypeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object DocumentType]", "true", "html,10,null,undefined,undefined,undefined",
                       "html,-//W3C//DTD XHTML 1.0 Strict//EN,http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd,"
                        + "undefined,undefined,undefined"},
            IE = {"[object DocumentType]", "true", "html,10,null,null,null,null",
                  "html,-//W3C//DTD XHTML 1.0 Strict//EN,http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd,"
                        + "null,null,null"})
    public void doctype() throws Exception {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var t = document.doctype;\n"
            + "      log(t);\n"
            + "      if (t != null) {\n"
            + "        log(t == document.firstChild);\n"
            + "        log(t.nodeName + ',' + t.nodeType + ',' + t.nodeValue + ',' + t.prefix "
            + "+ ',' + t.localName + ',' + t.namespaceURI);\n"
            + "        log(t.name + ',' + t.publicId + ',' + t.systemId + ',' + t.internalSubset"
            + " + ',' + t.entities + ',' + t.notations);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object DocumentType]", "greeting,10,null,undefined,undefined,undefined",
                       "greeting,MyIdentifier,hello.dtd,undefined,undefined,undefined"},
            IE = {"[object DocumentType]", "greeting,10,null,null,null,null",
                  "greeting,MyIdentifier,hello.dtd,null,null,null"})
    public void doctype_xml() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + LOG_TITLE_FUNCTION
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('GET', 'foo.xml', false);\n"
            + "        request.send('');\n"
            + "        var doc = request.responseXML;\n"
            + "        var t = doc.doctype;\n"
            + "        log(t);\n"
            + "        if (t != null) {\n"
            + "          log(t.nodeName + ',' + t.nodeType + ',' + t.nodeValue + ',' + t.prefix "
            + "+ ',' + t.localName + ',' + t.namespaceURI);\n"
            + "          log(t.name + ',' + t.publicId + ',' + t.systemId + ',' + t.internalSubset"
            + " + ',' + t.entities + ',' + t.notations);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml = "<!DOCTYPE greeting PUBLIC 'MyIdentifier' 'hello.dtd'>\n"
              + "<greeting/>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void html_previousSibling() throws Exception {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "  <title>Test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      if (document.body.parentElement) {\n"
            + "        //.text is defined for Comment in IE\n"
            + "        alert(typeof document.body.parentElement.previousSibling.text);\n"
            + "        }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object DocumentType]", "[object HTMLHtmlElement]"})
    public void document_children() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html>\n"
            + "<head>\n"
            + "  <title>Test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      for (var elem = document.firstChild; elem; elem = elem.nextSibling) {\n"
            + "        alert(elem);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
