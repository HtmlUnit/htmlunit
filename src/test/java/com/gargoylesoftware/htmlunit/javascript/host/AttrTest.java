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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Attr}.
 *
 * @version $Revision$
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
    @Alerts(DEFAULT = { "true", "exception thrown" },
            IE8 = { "true", "false" })
    public void specified() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var s = document.getElementById('testSelect');\n"
            + "    var o1 = s.options[0];\n"
            + "    alert(o1.getAttributeNode('value').specified);\n"
            + "    var o2 = s.options[1];\n"
            + "    alert(o2.getAttributeNode('value').specified);\n"
            + "  } catch(e) {\n"
            + "    alert('exception thrown');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option name='option1' value='foo'>One</option>\n"
            + "        <option>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Trimming of "class" attributes during Firefox emulation was having the unintended side effect
     * of setting the attribute's "specified" attribute to "false".
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(FF)
    @Alerts(FF = { "true", "true" })
    public void specified2() throws Exception {
        final String html
            = "<html><body onload='test()'><div id='div' class='test'></div>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var div = document.getElementById('div');\n"
            + "    alert(div.attributes.id.specified);\n"
            + "    alert(div.attributes.class.specified);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLOptionElement]",
            IE8 = "undefined")
    public void ownerElement() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  var o1 = s.options[0];\n"
            + "  alert(o1.getAttributeNode('value').ownerElement);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option name='option1' value='foo'>One</option>\n"
            + "        <option>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined" },
            FF17 = { "true", "false", "false" })
    public void isId() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var d = document.getElementById('d');\n"
            + "  alert(d.getAttributeNode('id').isId);\n"
            + "  alert(d.getAttributeNode('name').isId);\n"
            + "  alert(d.getAttributeNode('width').isId);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<div iD='d' name='d' width='40'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined", "undefined", "undefined" },
            IE = { "false", "true", "false", "true", "true" })
    public void expando() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var d = document.getElementById('d');\n"
            + "  alert(d.attributes['id'].expando);\n"
            + "  alert(d.attributes['name'].expando);\n"
            + "  alert(d.attributes['style'].expando);\n"
            + "  alert(d.attributes['custom'].expando);\n"
            + "  alert(d.attributes['other'].expando);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d' name='d' style='display: block' custom='value' other></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "function test() {\n"
            + "  var d = document.getElementById('d');\n"
            + "  d.setAttribute('onfocusin', 't');\n"
            + "  alert(d.attributes['onfocusin'].expando);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "test()",
            IE8 = "undefined")
    public void textContent() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var a = document.body.getAttributeNode('onload');\n"
            + "  alert(a.textContent);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "null", "null", "null", "null" },
            IE8 = { "[object]", "[object]", "null", "null" })
    public void getAttributeNodeUndefinedAttribute() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var elem = document.getElementById('myDiv');\n"
            + "  alert(elem.getAttributeNode('class'));\n"
            + "  alert(elem.getAttributeNode('style'));\n"
            + "  alert(elem.getAttributeNode('unknown'));\n"
            + "  alert(elem.getAttributeNode('name'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'></div>"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "null", "null", "null", "null" },
            IE8 = { "[object]", "[object]", "null", "null" })
    public void getAttributesUndefinedAttribute() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var elem = document.getElementById('myDiv');\n"
            + "  alert(elem.attributes.getNamedItem('class'));\n"
            + "  alert(elem.attributes.getNamedItem('style'));\n"
            + "  alert(elem.attributes.getNamedItem('unknown'));\n"
            + "  alert(elem.attributes.getNamedItem('name'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'></div>"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "", "[object Attr]", "" },
            IE8 = { "[object]", "undefined", "[object]", "" })
    @NotYetImplemented(IE8)
    public void value() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var attr = document.createAttribute('hi');\n"
            + "    alert(attr);\n"
            + "    alert(attr.value)\n"
            + "    attr = createXmlDocument().createAttribute('hi');\n"
            + "    alert(attr);\n"
            + "    alert(attr.value)\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "undefined" },
            IE8 = { "[object]", "undefined" })
    public void html_baseName() throws Exception {
        html("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "§§URL§§" },
            IE = { "[object]", "undefined" },
            IE11 = { "[object Attr]", "undefined" })
    public void html_baseURI() throws Exception {
        html("baseURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "null" },
            IE8 = { "[object]", "undefined" })
    public void html_namespaceURI() throws Exception {
        html("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "testattr" },
            IE = { "[object]", "undefined" },
            IE11 = { "[object Attr]", "testAttr" })
    public void html_localName() throws Exception {
        html("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "null" },
            IE8 = { "[object]", "undefined" })
    public void html_prefix() throws Exception {
        html("prefix");
    }

    private void html(final String methodName) throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    debug(document.getElementById('tester').attributes.getNamedItem('testAttr'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    alert(e);\n"
            + "    alert(e." + methodName + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='tester' testAttr='test'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "undefined" },
            IE8 = { "[object]", "testAttr" })
    public void xml_baseName() throws Exception {
        xml("baseName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "§§URL§§foo.xml" },
            IE = { "[object]", "undefined" },
            IE11 = { "[object Attr]", "undefined" })
    public void xml_baseURI() throws Exception {
        xml("baseURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "null" },
            IE8 = { "[object]", "" })
    public void xml_namespaceURI() throws Exception {
        xml("namespaceURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "testAttr" },
            IE8 = { "[object]", "undefined" })
    public void xml_localName() throws Exception {
        xml("localName");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Attr]", "null" },
            IE8 = { "[object]", "" })
    public void xml_prefix() throws Exception {
        xml("prefix");
    }

    private void xml(final String methodName) throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var request;\n"
            + "        if (window.XMLHttpRequest) {\n"
            + "          request = new XMLHttpRequest();\n"
            + "        } else if (window.ActiveXObject) {\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        }\n"
            + "        request.open('GET', 'foo.xml', false);\n"
            + "        request.send('');\n"
            + "        var doc = request.responseXML;\n"
            + "        debug(doc.documentElement.childNodes[0].attributes.item(0));\n"
            + "      }\n"
            + "      function debug(e) {\n"
            + "        try {\n"
            + "          alert(e);\n"
            + "        } catch(ex) {alert(ex)};\n"
            + "        alert(e." + methodName + ");\n"
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

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }
}
