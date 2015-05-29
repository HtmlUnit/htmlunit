/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF38;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.NamedNodeMap}.
 *
 * @version $Revision$
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
    @NotYetImplemented({ FF, IE })
    @Alerts(FF = { "baz=blah", "foo=bar", "id=f", "name=f" },
            CHROME = { "name=f", "id=f", "foo=bar", "baz=blah" },
            IE8 = { "onresizeend=null", "onrowenter=null",
                    "aria-haspopup=", "ondragleave=null", "onbeforepaste=null",
                    "ondragover=null", "onbeforecopy=null", "aria-disabled=",
                    "onpage=null", "onbeforeactivate=null", "accessKey=",
                    "onbeforeeditfocus=null", "oncontrolselect=null", "aria-hidden=",
                    "onblur=null", "hideFocus=false", "style=null", "onbeforedeactivate=null",
                    "dir=", "aria-expanded=", "onkeydown=null", "ondragstart=null", "onscroll=null",
                    "onpropertychange=null", "ondragenter=null", "id=f", "aria-level=0",
                    "onrowsinserted=null", "lang=", "onmouseup=null", "aria-busy=",
                    "oncontextmenu=null", "language=", "dataSrc=null", "implementation=null",
                    "onbeforeupdate=null", "onreadystatechange=null", "onmouseenter=null",
                    "onresize=null", "aria-checked=", "aria-readonly=", "oncopy=null",
                    "onselectstart=null", "onmove=null", "ondragend=null", "onrowexit=null",
                    "aria-secret=", "onactivate=null", "class=", "onfocus=null", "onfocusin=null",
                    "onmouseover=null", "oncut=null", "onmousemove=null", "title=", "role=",
                    "dataFld=null", "onfocusout=null", "onfilterchange=null", "disabled=false",
                    "aria-posinset=0", "ondrop=null", "ondblclick=null", "onrowsdelete=null",
                    "tabIndex=0", "onkeypress=null", "aria-relevant=", "onlosecapture=null", "aria-live=",
                    "ondeactivate=null", "aria-labelledby=", "aria-pressed=", "ondatasetchanged=null",
                    "ondataavailable=null", "aria-invalid=", "onafterupdate=null", "onmousewheel=null",
                    "onkeyup=null", "onmovestart=null", "aria-valuenow=", "aria-selected=",
                    "onmouseout=null", "aria-owns=", "aria-valuemax=", "onmoveend=null",
                    "contentEditable=inherit", "dataFormatAs=null", "oncellchange=null", "aria-valuemin=",
                    "onlayoutcomplete=null", "onhelp=null", "onerrorupdate=null", "onmousedown=null",
                    "aria-setsize=0", "onpaste=null", "onmouseleave=null", "onclick=null", "ondrag=null",
                    "aria-controls=", "onresizestart=null", "aria-flowto=", "ondatasetcomplete=null",
                    "aria-required=", "aria-describedby=", "onbeforecut=null", "aria-activedescendant=",
                    "aria-multiselectable=", "accept-charset=UNKNOWN", "encType=application/x-www-form-urlencoded",
                    "onsubmit=null", "method=get", "onreset=null",
                    "name=f", "action=", "target=", "baz=blah", "foo=bar" },
            IE11 = { "name=f", "id=f", "baz=blah", "foo=bar" })
    public void attributes() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    for(var i = 0; i < f.attributes.length; i++) {\n"
            + "      alert(f.attributes[i].name + '=' + f.attributes[i].value);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='f' id='f' foo='bar' baz='blah'></form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html); // properties order is the reverse of what I get with FF3.6
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "name", "f", "name", "f", "name", "f", "name", "f", "null" },
            IE8 = { "name", "f", "name", "f", "name", "f", "exception", "null" })
    public void getNamedItem_HTML() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    alert(f.attributes.getNamedItem('name').nodeName);\n"
            + "    alert(f.attributes.getNamedItem('name').nodeValue);\n"
            + "    alert(f.attributes.getNamedItem('NaMe').nodeName);\n"
            + "    alert(f.attributes.getNamedItem('nAmE').nodeValue);\n"
            + "    try {\n"
            + "      alert(f.attributes.name.nodeName);\n"
            + "      alert(f.attributes.name.nodeValue);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    try {\n"
            + "      alert(f.attributes.NaMe.nodeName);\n"
            + "      alert(f.attributes.nAmE.nodeValue);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(f.attributes.getNamedItem('notExisting'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form name='f' id='f' foo='bar' baz='blah'></form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Looks like FF38 has a strange bug when using attrib names with uppercase letters.
     * see https://bugzilla.mozilla.org/show_bug.cgi?id=1169384
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "myAttr", "myattr2", "myAttr", "myattr2", "myattr2" },
            FF38 = { "myAttr", "myattr2", "not found", "myattr2", "myattr2" })
    @NotYetImplemented(FF38)
    public void getNamedItem_HTML_Case() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var elem = document.getElementById('tester');\n"

            + "    var node = document.createAttribute('myAttr');\n"
            + "    elem.attributes.setNamedItem(node);\n"

            + "    var node = document.createAttribute('myattr2');\n"
            + "    elem.attributes.setNamedItem(node);\n"

            + "    for(var i = 0; i < elem.attributes.length; i++) {\n"
            + "      var name = elem.attributes[i].name;\n"
            + "      if (name.indexOf('my') === 0) { alert(name); }\n"
            + "    }\n"

            + "    var item = elem.attributes.getNamedItem('myAttr');\n"
            + "    if (item) {\n"
            + "      alert(item.nodeName);\n"
            + "    } else {\n"
            + "      alert('not found');\n"
            + "    }\n"

            + "    alert(elem.attributes.getNamedItem('myattr2').name);\n"
            + "    alert(elem.attributes.getNamedItem('MYaTTr2').name);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "name", "y", "name", "y", "null", "undefined", "null" },
            IE8 = { "name", "y", "exception", "null", "undefined", "null" })
    public void getNamedItem_XML() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'second.xml'") + ";\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('name').nodeName);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('name').nodeValue);\n"
            + "    try {\n"
            + "      alert(doc.documentElement.attributes.name.nodeName);\n"
            + "      alert(doc.documentElement.attributes.name.nodeValue);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('NaMe'));\n"
            + "    alert(doc.documentElement.attributes.NaMe);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('nonExistent'));\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<blah name='y'></blah>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myattr")
    public void setNamedItem_HTML() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var node = document.createAttribute('myattr');\n"
            + "    var elem = document.getElementById('tester');\n"
            + "    elem.attributes.setNamedItem(node);\n"
            + "    var item = elem.attributes.getNamedItem('myAttr');"
            + "    if (item) {\n"
            + "      alert(item.nodeName);\n"
            + "    } else {\n"
            + "      alert('not found');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myAttr")
    public void setNamedItem_XML() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = " + XMLDocumentTest.callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var node = doc.createAttribute('myAttr');\n"
            + "    doc.documentElement.attributes.setNamedItem(node);\n"
            + "    alert(doc.documentElement.attributes.getNamedItem('myAttr').nodeName);\n"
            + "  }\n"
            + XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "  <div id='tester'></div\n"
            + "</body></html>";

        final String xml = "<test></test>";

        getMockWebConnection().setResponse(URL_SECOND, xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "true", "[object Attr]", "true", "[object Attr]" })
    public void has() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html ng-app><body>\n"
            + "<script>\n"
            + "var attributes = document.documentElement.attributes;\n"
            + "alert(0 in attributes);\n"
            + "alert(attributes[0]);\n"
            + "alert('0' in attributes);\n"
            + "alert(attributes['0']);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "div1", "" })
    public void removeNamedItem() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<div id='div1' style='background-color:#FFFFC1;'>div1</div>\n"
            + "<script>\n"
            + "  var el = document.getElementById('div1');\n"
            + "  alert(el.id);\n"
            + "  el.attributes.removeNamedItem('id');\n"
            + "  alert(el.id);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined" },
            IE8 = { "[object]", "[object]", "[object]" })
    public void unspecifiedAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.body.attributes.language);\n"
            + "    alert(document.body.attributes.id);\n"
            + "    alert(document.body.attributes.dir);\n"
            + "  }\n"
            + "</script>\n"
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
    @Alerts("media=\"screen\"")
    @NotYetImplemented(IE8)
    public void changedAttribute() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"

            + "<style id='myStyle'>my { }</style>\n"

            + "<script>\n"
            + "function doTest() {\n"
            + "  style = document.getElementById('myStyle');\n"
            + "  style.media = 'screen';\n"

            + "  var attributes = style.attributes;\n"
            + "  for (var i = 0; i < attributes.length; i++) {\n"
            + "    if (attributes[i].name === 'media') {\n"
            + "      alert(attributes[i].name + '=\"' + attributes[i].value + '\"');\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
