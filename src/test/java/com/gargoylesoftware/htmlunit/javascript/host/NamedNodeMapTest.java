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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.NamedNodeMap}.
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NamedNodeMapTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"name=f", "id=f", "foo=bar", "baz=blah"},
            IE = {"name=f", "id=f", "baz=blah", "foo=bar"})
    @NotYetImplemented(IE)
    public void attributes() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    for(var i = 0; i < f.attributes.length; i++) {\n"
            + "      if (f.attributes[i]) {\n"
            + "        log(f.attributes[i].name + '=' + f.attributes[i].value);\n"
            + "      } else {\n"
            + "        log(i);\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='f' id='f' foo='bar' baz='blah'></form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"name", "f", "name", "f", "name", "f", "name", "f", "null"})
    public void getNamedItem_HTML() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    log(f.attributes.getNamedItem('name').nodeName);\n"
            + "    log(f.attributes.getNamedItem('name').nodeValue);\n"
            + "    log(f.attributes.getNamedItem('NaMe').nodeName);\n"
            + "    log(f.attributes.getNamedItem('nAmE').nodeValue);\n"
            + "    try {\n"
            + "      log(f.attributes.name.nodeName);\n"
            + "      log(f.attributes.name.nodeValue);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    try {\n"
            + "      log(f.attributes.NaMe.nodeName);\n"
            + "      log(f.attributes.nAmE.nodeValue);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    log(f.attributes.getNamedItem('notExisting'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='f' id='f' foo='bar' baz='blah'></form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT =  {"myattr", "myattr2", "myattr", "myattr2", "myattr2"},
            IE = {"myAttr", "myattr2", "myAttr", "myattr2", "myattr2"})
    public void getNamedItem_HTML_Case() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elem = document.getElementById('tester');\n"

            + "    var node = document.createAttribute('myAttr');\n"
            + "    elem.attributes.setNamedItem(node);\n"

            + "    var node = document.createAttribute('myattr2');\n"
            + "    elem.attributes.setNamedItem(node);\n"

            + "    for(var i = 0; i < elem.attributes.length; i++) {\n"
            + "      var name = elem.attributes[i].name;\n"
            + "      if (name.indexOf('my') === 0) { log(name); }\n"
            + "    }\n"

            + "    var item = elem.attributes.getNamedItem('myAttr');\n"
            + "    if (item) {\n"
            + "      log(item.nodeName);\n"
            + "    } else {\n"
            + "      log('not found');\n"
            + "    }\n"

            + "    log(elem.attributes.getNamedItem('myattr2').name);\n"
            + "    log(elem.attributes.getNamedItem('MYaTTr2').name);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"name", "y", "name", "y", "null", "undefined", "null"})
    public void getNamedItem_XML() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'second.xml'") + ";\n"
            + "    log(doc.documentElement.attributes.getNamedItem('name').nodeName);\n"
            + "    log(doc.documentElement.attributes.getNamedItem('name').nodeValue);\n"
            + "    try {\n"
            + "      log(doc.documentElement.attributes.name.nodeName);\n"
            + "      log(doc.documentElement.attributes.name.nodeValue);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "    log(doc.documentElement.attributes.getNamedItem('NaMe'));\n"
            + "    log(doc.documentElement.attributes.NaMe);\n"
            + "    log(doc.documentElement.attributes.getNamedItem('nonExistent'));\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<blah name='y'></blah>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myattr")
    public void setNamedItem_HTML() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var node = document.createAttribute('myattr');\n"
            + "    var elem = document.getElementById('tester');\n"
            + "    elem.attributes.setNamedItem(node);\n"
            + "    var item = elem.attributes.getNamedItem('myAttr');\n"
            + "    if (item) {\n"
            + "      log(item.nodeName);\n"
            + "    } else {\n"
            + "      log('not found');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myAttr")
    public void setNamedItem_XML() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var node = doc.createAttribute('myAttr');\n"
            + "    doc.documentElement.attributes.setNamedItem(node);\n"
            + "    log(doc.documentElement.attributes.getNamedItem('myAttr').nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div\n"
            + "</body></html>";

        final String xml = "<test></test>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "[object Attr]", "true", "[object Attr]"})
    public void has() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html ng-app><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var attributes = document.documentElement.attributes;\n"
            + "log(0 in attributes);\n"
            + "log(attributes[0]);\n"
            + "log('0' in attributes);\n"
            + "log(attributes['0']);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"div1", ""})
    public void removeNamedItem() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<div id='div1' style='background-color:#FFFFC1;'>div1</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var el = document.getElementById('div1');\n"
            + "  log(el.id);\n"
            + "  el.attributes.removeNamedItem('id');\n"
            + "  log(el.id);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined"})
    public void unspecifiedAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.body.attributes.language);\n"
            + "    log(document.body.attributes.id);\n"
            + "    log(document.body.attributes.dir);\n"
            + "  }\n"
            + "</script>\n"
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
    @Alerts("media=\"screen\"")
    public void changedAttribute() throws Exception {
        final String html
            = "<html><head>\n"

            + "<style id='myStyle'>my { }</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  style = document.getElementById('myStyle');\n"
            + "  style.media = 'screen';\n"

            + "  var attributes = style.attributes;\n"
            + "  for (var i = 0; i < attributes.length; i++) {\n"
            + "    if (attributes[i].name === 'media') {\n"
            + "      log(attributes[i].name + '=\"' + attributes[i].value + '\"');\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See issue #1716.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<input id=\"myinput\" name=\"test_input\">")
    public void readAccessOnlyDefinesNewAttribs() throws Exception {
        final String html =
                "<html>\n"
              + "<head>\n"
              + "</head>\n"
              + "<body>\n"
              + "  <input id='myinput' name='test_input' />\n"
              + "  <script type='text/javascript'>\n"
              + LOG_TITLE_FUNCTION
              + "    var input = document.getElementById('myinput');\n"
              + "    var attrs = input.attributes;\n"
              + "    for(var i = 0; i < attrs.length; i++) {\n"
              + "      attrs[i];\n"
              + "    }\n"
              + "    log(input.outerHTML);\n"
              + "  </script>\n"
              + "</body>\n"
              + "</html>";
        loadPageVerifyTitle2(html);
    }
}
