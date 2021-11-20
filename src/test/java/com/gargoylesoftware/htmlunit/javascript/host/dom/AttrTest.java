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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link Attr}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class AttrTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "exception thrown"})
    public void specified() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var s = document.getElementById('testSelect');\n"
            + "    var o1 = s.options[0];\n"
            + "    log(o1.getAttributeNode('value').specified);\n"
            + "    var o2 = s.options[1];\n"
            + "    log(o2.getAttributeNode('value').specified);\n"
            + "  } catch(e) {\n"
            + "    log('exception thrown');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1' id='testSelect'>\n"
            + "    <option name='option1' value='foo'>One</option>\n"
            + "    <option>Two</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Trimming of "class" attributes during Firefox emulation was having the unintended side effect
     * of setting the attribute's "specified" attribute to "false".
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void specified2() throws Exception {
        final String html
            = "<html><body onload='test()'><div id='div' class='test'></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('div');\n"
            + "    log(div.attributes.id.specified);\n"
            + "    log(div.attributes.class.specified);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLOptionElement]")
    public void ownerElement() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  var o1 = s.options[0];\n"
            + "  log(o1.getAttributeNode('value').ownerElement);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "  <select name='select1' id='testSelect'>\n"
            + "    <option name='option1' value='foo'>One</option>\n"
            + "    <option>Two</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined"})
    public void isId() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var d = document.getElementById('d');\n"
            + "  log(d.getAttributeNode('id').isId);\n"
            + "  log(d.getAttributeNode('name').isId);\n"
            + "  log(d.getAttributeNode('width').isId);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<div iD='d' name='d' width='40'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined", "undefined", "undefined", "undefined"},
            IE = {"false", "true", "false", "true", "true"})
    public void expando() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var d = document.getElementById('d');\n"
            + "  log(d.attributes['id'].expando);\n"
            + "  log(d.attributes['name'].expando);\n"
            + "  log(d.attributes['style'].expando);\n"
            + "  log(d.attributes['custom'].expando);\n"
            + "  log(d.attributes['other'].expando);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d' name='d' style='display: block' custom='value' other></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Testcase for issue http://sourceforge.net/p/htmlunit/bugs/1493/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "false")
    public void expandoEvent() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var d = document.getElementById('d');\n"
            + "  d.setAttribute('onfocusin', 't');\n"
            + "  log(d.attributes['onfocusin'].expando);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("test()")
    public void textContent() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var a = document.body.getAttributeNode('onload');\n"
            + "  log(a.textContent);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null", "null", "null"})
    public void getAttributeNodeUndefinedAttribute() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var elem = document.getElementById('myDiv');\n"
            + "  log(elem.getAttributeNode('class'));\n"
            + "  log(elem.getAttributeNode('style'));\n"
            + "  log(elem.getAttributeNode('unknown'));\n"
            + "  log(elem.getAttributeNode('name'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null", "null", "null"})
    public void getAttributesUndefinedAttribute() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var elem = document.getElementById('myDiv');\n"
            + "  log(elem.attributes.getNamedItem('class'));\n"
            + "  log(elem.attributes.getNamedItem('style'));\n"
            + "  log(elem.attributes.getNamedItem('unknown'));\n"
            + "  log(elem.attributes.getNamedItem('name'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Attr]", "", "[object Attr]", ""})
    public void value() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var attr = document.createAttribute('hi');\n"
            + "    log(attr);\n"
            + "    log(attr.value);\n"
            + "    attr = document.implementation.createDocument('', '', null).createAttribute('hi');\n"
            + "    log(attr);\n"
            + "    log(attr.value);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Attr]", "undefined"})
    public void html_baseName() throws Exception {
        html("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Attr]", "§§URL§§"},
            IE = {"[object Attr]", "undefined"})
    public void html_baseURI() throws Exception {
        html("baseURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Attr]", "null"})
    public void html_namespaceURI() throws Exception {
        html("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Attr]", "testattr"},
            IE = {"[object Attr]", "testAttr"})
    public void html_localName() throws Exception {
        html("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Attr]", "null"})
    public void html_prefix() throws Exception {
        html("prefix");
    }

    private void html(final String methodName) throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    debug(document.getElementById('tester').attributes.getNamedItem('testAttr'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    log(e);\n"
            + "    log(e." + methodName + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='tester' testAttr='test'></div>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Attr]", "undefined"})
    public void xml_baseName() throws Exception {
        xml("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Attr]", "§§URL§§foo.xml"},
            IE = {"[object Attr]", "undefined"})
    public void xml_baseURI() throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        xml("baseURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Attr]", "null"})
    public void xml_namespaceURI() throws Exception {
        xml("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Attr]", "testAttr"})
    public void xml_localName() throws Exception {
        xml("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Attr]", "null"})
    public void xml_prefix() throws Exception {
        xml("prefix");
    }

    private void xml(final String methodName) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var request;\n"
            + "        request = new XMLHttpRequest();\n"
            + "        request.open('GET', 'foo.xml', false);\n"
            + "        request.send('');\n"
            + "        var doc = request.responseXML;\n"
            + "        debug(doc.documentElement.childNodes[0].attributes.item(0));\n"
            + "      }\n"
            + "      function debug(e) {\n"
            + "        try {\n"
            + "          log(e);\n"
            + "        } catch(ex) {log(ex)}\n"
            + "        log(e." + methodName + ");\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml>"
            + "<div testAttr='test'></div>"
            + "</xml>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }
}
