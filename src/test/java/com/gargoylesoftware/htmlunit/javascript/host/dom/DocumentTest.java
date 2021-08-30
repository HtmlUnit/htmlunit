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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION;
import static com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocumentTest.callLoadXMLDocumentFromFile;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link Document}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Michael Ottati
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Ahmed Ashour
 * @author Rob Di Marco
 * @author Sudhan Moghe
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DocumentTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "form1", "form2"})
    public void formsAccessor_TwoForms() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.forms.length);\n"
            + "  for(var i = 0; i < document.forms.length; i++) {\n"
            + "    log(document.forms[i].name);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "<form name='form2'>\n"
            + "  <input type='text' name='textfield2' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Previously, forms with no names were not being returned by document.forms.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void formsAccessor_FormWithNoName() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.forms.length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form>\n"
            + "  <input type='text' name='textfield1' value='foo' />\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void formsAccessor_NoForms() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.forms.length);\n"
            + "  for(var i = 0; i < document.forms.length; i++) {\n"
            + "    log(document.forms[i].name);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "second"})
    public void formArray() throws Exception {
        final String firstHtml
            = "<html><head><SCRIPT lang='JavaScript'>\n"
            + "function doSubmit(formName){\n"
            + "  var form = document.forms[formName];\n"
            + "  form.submit();\n"
            + "}\n"
            + "</SCRIPT></head><body><form name='formName' method='POST' "
            + "action='" + URL_SECOND + "'>\n"
            + "<a href='.' id='testJavascript' name='testJavascript' "
            + "onclick=\" doSubmit('formName');return false;\">\n"
            + "Test Link </a><input type='submit' value='Login' "
            + "name='loginButton'></form>\n"
            + "</body></html> ";
        final String secondHtml
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(firstHtml);
        assertTitle(driver, getExpectedAlerts()[0]);

        driver.findElement(By.id("testJavascript")).click();
        assertTitle(driver, getExpectedAlerts()[1]);
    }

    /**
     * Test that forms is a live collection.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "1", "true"})
    public void formsLive() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.forms;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.forms.length);\n"
            + "  log(document.forms == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form id='myForm' action='foo.html'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.anchors</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "1", "1", "true", "name: end"},
            IE = {"0", "3", "3", "true", "id: firstLink"})
    public void anchors() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.anchors;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.anchors.length);\n"
            + "  log(document.anchors == oCol);\n"
            + "  if (document.anchors[0].name)\n"
            + "    log('name: ' + document.anchors[0].name);\n"
            + "  else\n"
            + "    log('id: ' + document.anchors[0].id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<a href='foo.html' id='firstLink'>foo</a>\n"
            + "<a href='foo2.html'>foo2</a>\n"
            + "<a name='end'/>\n"
            + "<a href=''>null2</a>\n"
            + "<a id='endId'/>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.anchors</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "true"})
    public void anchorsEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.anchors;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.anchors.length);\n"
            + "  log(document.anchors == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.applets</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "0", "0", "true"},
            IE = {"0", "3", "3", "true"})
    public void applets() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.applets;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.applets.length);\n"
            + "  log(document.applets == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<applet id='firstApplet'></applet>\n"
            + "<applet name='end'></applet>\n"
            + "<applet id='endId'></applet>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.applets</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "true"})
    public void appletsEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.applets;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.applets.length);\n"
            + "  log(document.applets == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.embeds</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "3", "3", "true", "firstEmbed"})
    public void embeds() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.embeds;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.embeds.length);\n"
            + "  log(document.embeds == oCol);\n"
            + "  log(document.embeds[0].id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<embed id='firstEmbed' />\n"
            + "<embed name='end' />\n"
            + "<embed id='endId'/>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.embeds</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "true"})
    public void embedsEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.embeds;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.embeds.length);\n"
            + "  log(document.embeds == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.embeds</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "3", "3", "true", "firstEmbed"})
    public void plugins() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.plugins;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.plugins.length);\n"
            + "  log(document.plugins == oCol);\n"
            + "  log(document.embeds[0].id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<embed id='firstEmbed' />\n"
            + "<embed name='end' />\n"
            + "<embed id='endId'/>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.embeds</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "true"})
    public void pluginsEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.plugins;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.plugins.length);\n"
            + "  log(document.plugins == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.links</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "3", "3", "true", "firstLink"})
    public void links() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.links;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.links.length);\n"
            + "  log(document.links == oCol);\n"
            + "  log(document.links[0].id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<a href='foo.html' id='firstLink'>foo</a>\n"
            + "<a href='foo2.html'>foo2</a>\n"
            + "<a name='end'/>\n"
            + "<a href=''>null2</a>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.links</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "true"})
    public void linksEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.links;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.links.length);\n"
            + "  log(document.links == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Ensures that <tt>document.createElement()</tt> works correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"parentNode: null", "DIV", "1", "null", "DIV", "button1value", "text1value", "text"})
    public void createElement() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function doTest() {\n"
            + "        // Create a DIV element.\n"
            + "        var div1 = document.createElement('div');\n"
            + "        log('parentNode: ' + div1.parentNode);\n"
            + "        div1.id = 'div1';\n"
            + "        document.body.appendChild(div1);\n"
            + "        log(div1.tagName);\n"
            + "        log(div1.nodeType);\n"
            + "        log(div1.nodeValue);\n"
            + "        log(div1.nodeName);\n"
            + "        // Create an INPUT element.\n"
            + "        var input = document.createElement('input');\n"
            + "        input.id = 'text1id';\n"
            + "        input.name = 'text1name';\n"
            + "        input.value = 'text1value';\n"
            + "        var form = document.getElementById('form1');\n"
            + "        form.appendChild(input);\n"
            + "        log(document.getElementById('button1id').value);\n"
            + "        log(document.getElementById('text1id').value);\n"
            + "        // The default type of an INPUT element is 'text'.\n"
            + "        log(document.getElementById('text1id').type);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "    <form name='form1' id='form1'>\n"
            + "      <input type='button' id='button1id' name='button1name' value='button1value'/>\n"
            + "      This is form1.\n"
            + "    </form>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DIV,DIV,http://www.w3.org/1999/xhtml,null,div",
                "HI:DIV,HI:DIV,http://www.w3.org/1999/xhtml,null,hi:div"})
    public void documentCreateElement2() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function doTest() {\n"
            + "        div = document.createElement('Div');\n"
            + "        log(div.nodeName + ',' + div.tagName + ',' + div.namespaceURI + ',' + "
            + "div.prefix + ',' + div.localName);\n"
            + "        div = document.createElement('Hi:Div');\n"
            + "        log(div.nodeName + ',' + div.tagName + ',' + div.namespaceURI + ',' + "
            + "div.prefix + ',' + div.localName);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='doTest()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Ensures that <tt>document.createElementNS()</tt> works correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some:Div", "Some:Div", "myNS", "Some", "Div", "svg", "svg", "http://www.w3.org/2000/svg", "null", "svg"})
    public void createElementNS() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var div = document.createElementNS('myNS', 'Some:Div');\n"
            + "    log(div.nodeName);\n"
            + "    log(div.tagName);\n"
            + "    log(div.namespaceURI);\n"
            + "    log(div.prefix);\n"
            + "    log(div.localName);\n"

            + "    var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
            + "    log(svg.nodeName);\n"
            + "    log(svg.tagName);\n"
            + "    log(svg.namespaceURI);\n"
            + "    log(svg.prefix);\n"
            + "    log(svg.localName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>createTextNode</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Some Text", "9", "3", "Some Text", "#text"})
    public void createTextNode() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var text1=document.createTextNode('Some Text');\n"
            + "  var body1=document.getElementById('body');\n"
            + "  body1.appendChild(text1);\n"
            + "  log(text1.data);\n"
            + "  log(text1.length);\n"
            + "  log(text1.nodeType);\n"
            + "  log(text1.nodeValue);\n"
            + "  log(text1.nodeName);\n"
            + "}\n"
            + "</script></head><body onload='doTest()' id='body'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for RFE 741930.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void appendChild() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var form = document.forms['form1'];\n"
            + "    var div = document.createElement('DIV');\n"
            + "    form.appendChild(div);\n"
            + "    var elements = document.getElementsByTagName('DIV');\n"
            + "    log(elements.length);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that <tt>document.appendChild()</tt>doesn't work.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "exception"})
    public void appendChildAtDocumentLevel() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var div = document.createElement('div');\n"
            + "      div.innerHTML = 'test';\n"
            + "      try {\n"
            + "        log(document.childNodes.length);\n"
            + "        document.appendChild(div); // Error\n"
            + "        log(document.childNodes.length);\n"
            + "        log(document.childNodes[0].tagName);\n"
            + "        log(document.childNodes[1].tagName);\n"
            + "        log(document.getElementsByTagName('div').length);\n"
            + "      } catch(ex) {\n"
            + "        log('exception');\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for appendChild of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Some Text")
    public void appendChild_textNode() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var form = document.forms['form1'];\n"
            + "    var child = document.createTextNode('Some Text');\n"
            + "    form.appendChild(child);\n"
            + "    log(form.lastChild.data);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>cloneNode</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true"})
    public void cloneNode() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var form = document.forms['form1'];\n"
            + "    var cloneShallow = form.cloneNode(false);\n"
            + "    log(cloneShallow != null);\n"
            + "    log(cloneShallow.firstChild == null);\n"
            + "    var cloneDeep = form.cloneNode(true);\n"
            + "    log(cloneDeep != null);\n"
            + "    log(cloneDeep.firstChild != null);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "<p>hello world</p>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>insertBefore</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void insertBefore() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var form = document.forms['form1'];\n"
            + "    var oldChild = document.getElementById('oldChild');\n"
            + "    var div = document.createElement('DIV');\n"
            + "    form.insertBefore(div, oldChild);\n"
            + "    log(form.firstChild == div);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'><div id='oldChild'/></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/javascript")
    public void getElementById_scriptType() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script id='script1' type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  doTest=function() {\n"
            + "  log(top.document.getElementById('script1').type);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 740665.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§script/")
    public void getElementById_scriptSrc() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "<script id='script1' src='" + URL_FIRST + "script/'>\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final String script
            = "doTest = function() {\n"
            + "  log(top.document.getElementById('script1').src);\n"
            + "}";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script/"), script, "text/javascript");

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>parentNode</tt> with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("parentDiv")
    public void parentNode_Nested() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var div1=document.getElementById('childDiv');\n"
            + "    log(div1.parentNode.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>parentNode</tt> of document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void parentNode_Document() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    log(document.parentNode == null);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>parentNode</tt> and <tt>createElement</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void parentNode_CreateElement() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var div1=document.createElement('div');\n"
            + "    log(div1.parentNode == null);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>parentNode</tt> and <tt>appendChild</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("parentDiv")
    public void parentNode_AppendChild() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var childDiv=document.getElementById('childDiv');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(childDiv);\n"
            + "    log(childDiv.parentNode.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'></div><div id='childDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>documentElement</tt> of document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "HTML", "true"})
    public void documentElement() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    log(document.documentElement != null);\n"
            + "    log(document.documentElement.tagName);\n"
            + "    log(document.documentElement.parentNode == document);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>firstChild</tt> with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("childDiv")
    public void firstChild_Nested() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var div1=document.getElementById('parentDiv');\n"
            + "    log(div1.firstChild.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv'/><div id='childDiv2'/></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for <tt>firstChild</tt> and <tt>appendChild</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("childDiv")
    public void firstChild_AppendChild() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var childDiv=document.getElementById('childDiv');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(childDiv);\n"
            + "    var childDiv2=document.getElementById('childDiv2');\n"
            + "    parentDiv.appendChild(childDiv2);\n"
            + "    log(parentDiv.firstChild.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'/><div id='childDiv'/><div id='childDiv2'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for lastChild with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("childDiv")
    public void lastChild_Nested() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var div1=document.getElementById('parentDiv');\n"
            + "    log(div1.lastChild.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='childDiv1'></div><div id='childDiv'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for lastChild and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("childDiv")
    public void lastChild_AppendChild() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var childDiv1=document.getElementById('childDiv1');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(childDiv1);\n"
            + "    var childDiv=document.getElementById('childDiv');\n"
            + "    parentDiv.appendChild(childDiv);\n"
            + "    log(parentDiv.lastChild.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'/><div id='childDiv1'/><div id='childDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for nextSibling with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("nextDiv")
    public void nextSibling_Nested() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var div1 = document.getElementById('previousDiv');\n"
            + "    log(div1.nextSibling.id);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='previousDiv'></div><div id='nextDiv'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for nextSibling and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("nextDiv")
    public void nextSibling_AppendChild() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var previousDiv=document.getElementById('previousDiv');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(previousDiv);\n"
            + "    var nextDiv=document.getElementById('nextDiv');\n"
            + "    parentDiv.appendChild(nextDiv);\n"
            + "    log(previousDiv.nextSibling.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'/><div id='junk1'/><div id='previousDiv'/><div id='junk2'/><div id='nextDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for previousSibling with nested elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("previousDiv")
    public void previousSibling_Nested() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var div1 = document.getElementById('nextDiv');\n"
            + "    log(div1.previousSibling.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'><div id='previousDiv'></div><div id='nextDiv'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for previousSibling and appendChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("previousDiv")
    public void previousSibling_AppendChild() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var previousDiv=document.getElementById('previousDiv');\n"
            + "    var parentDiv=document.getElementById('parentDiv');\n"
            + "    parentDiv.appendChild(previousDiv);\n"
            + "    var nextDiv=document.getElementById('nextDiv');\n"
            + "    parentDiv.appendChild(nextDiv);\n"
            + "    log(nextDiv.previousSibling.id);\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='parentDiv'/><div id='junk1'/><div id='previousDiv'/><div id='junk2'/><div id='nextDiv'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"tangerine", "ginger"})
    public void allProperty_KeyByName() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      log(document.all['input1'].value);\n"
            + "      log(document.all['foo2'].value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form id='form1'>\n"
            + "    <input id='input1' name='foo1' type='text' value='tangerine' />\n"
            + "    <input id='input2' name='foo2' type='text' value='ginger' />\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 707750.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("DIV")
    public void allProperty_CalledDuringPageLoad() throws Exception {
        final String html
            = "<html><body>\n"
            + "<div id='ARSMenuDiv1' style='VISIBILITY: hidden; POSITION: absolute; z-index: 1000000'></div>\n"
            + "<script language='Javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  var divObj = document.all['ARSMenuDiv1'];\n"
            + "  log(divObj.tagName);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void referrer() throws Exception {
        final String firstHtml = "<html><head><title>First</title></head><body>\n"
            + "<a href='" + URL_SECOND + "'>click me</a></body></html>";

        final String secondHtml = "<html><head><title>Second</title></head><body onload='alert(document.referrer);'>\n"
            + "</form></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = loadPage2(firstHtml);
        driver.findElement(By.linkText("click me")).click();

        expandExpectedAlertsVariables(URL_FIRST);

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void referrer_NoneSpecified() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body onload='log(document.referrer);'>\n"
            + "</form></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void url() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body onload='log(document.URL);'>\n"
            + "</form></body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"button", "button", "true"})
    public void getElementsByTagName() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var elements = document.getElementsByTagName('input');\n"
            + "    for (var i = 0; i < elements.length; i++) {\n"
            + "      log(elements[i].type);\n"
            + "      log(elements.item(i).type);\n"
            + "    }\n"
            + "    log(elements == document.getElementsByTagName('input'));\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 740636.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("button")
    public void getElementsByTagName_CaseInsensitive() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    var elements = document.getElementsByTagName('InPuT');\n"
            + "    for(i = 0; i < elements.length; i++) {\n"
            + "      log(elements[i].type);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 740605.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void getElementsByTagName_Inline() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script type=\"text/javascript\">\n"
            + LOG_TITLE_FUNCTION
            + "log(document.getElementsByTagName('script').length);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 740605.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void getElementsByTagName_LoadScript() throws Exception {
        final String html = "<html><body><script src=\"" + URL_FIRST + "script\"></script></body></html>";

        final String script = "alert(document.getElementsByTagName('script').length);\n";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script"), script, "text/javascript");

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "<nested>Three</nested>", "Four",
                       "1", "Two", "0", "0"},
            IE = {"2", "undefined", "undefined",
                  "1", "undefined", "0", "0"})
    public void getElementsByTagNameXml() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "</head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  var xmlString = [\n"
            + "                 '<ResultSet>',\n"
            + "                 '<Result>One</Result>',\n"
            + "                 '<RESULT>Two</RESULT>',\n"
            + "                 '<result><nested>Three</nested></result>',\n"
            + "                 '<result>Four</result>',\n"
            + "                 '</ResultSet>'\n"
            + "                ].join('');\n"
            + "  if (window.DOMParser) {\n"
            + "    var parser = new DOMParser();\n"
            + "    xml = parser.parseFromString(xmlString, 'text/xml');\n"
            + "  } else { // IE\n"
            + "    var parser = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    parser.async = 'false';\n"
            + "    parser.loadXML(xmlString);\n"
            + "  }\n"
            + "  var xmlDoc = parser.parseFromString(xmlString, 'text/xml');\n"
            + "  try {\n"

            + "    var res = xmlDoc.getElementsByTagName('result');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"
            + "    log(res[1].innerHTML);\n"

            + "    res = xmlDoc.getElementsByTagName('RESULT');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"

            + "    res = xmlDoc.getElementsByTagName('resulT');\n"
            + "    log(res.length);\n"

            + "    res = xmlDoc.getElementsByTagName('rEsulT');\n"
            + "    log(res.length);\n"
            + "  } catch(e) {log('exception ' + e)}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HTML", "HEAD", "TITLE", "SCRIPT", "BODY"})
    public void all_WithParentheses() throws Exception {
        final String html
            = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var length = document.all.length;\n"
            + "  for(i = 0; i < length; i++) {\n"
            + "    try {\n"
            + "      var all = document.all(i);\n"
            + "      if (all == null) {\n"
            + "        log('all == null');\n"
            + "      } else {\n"
            + "        log(all.tagName);\n"
            + "      }\n"
            + "    } catch(e) { log(e); }\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HTML", "HEAD", "TITLE", "SCRIPT", "BODY"})
    public void all_IndexByInt() throws Exception {
        final String html
            = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var length = document.all.length;\n"
            + "  for(i = 0; i < length; i++) {\n"
            + "    log(document.all[i].tagName);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HTML")
    public void all_Item() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.all.item(0).tagName);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void all_NamedItem_Unknown() throws Exception {
        namedItem("foo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("form1<->")
    public void all_NamedItem_ById() throws Exception {
        namedItem("form1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<->form2")
    public void all_NamedItem_ByName_formWithoutId() throws Exception {
        namedItem("form2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("f3<->form3")
    public void all_NamedItem_ByName() throws Exception {
        namedItem("form3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"coll 2", "f4<->form4_1", "f4<->form4_2"},
            IE = "f4<->form4_1")
    public void all_NamedItem_DuplicateId() throws Exception {
        namedItem("f4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"coll 2", "f5_1<->form5", "f5_2<->form5"},
            IE = "f5_1<->form5")
    public void all_NamedItem_DuplicateName() throws Exception {
        namedItem("form5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"coll 2", "f6<->form6", "form6<->form6_2"},
            IE = "f6<->form6")
    public void all_NamedItem_DuplicateIdName() throws Exception {
        namedItem("form6");
    }

    private void namedItem(final String name) throws Exception {
        final String html
            = "<!doctype html>\n"
            + "<html><head>\n"
            + "<script>\n"
            + "  var res = '';"
            + "  function log(msg) { res += msg + '§';}\n"
            + "  function doTest() {\n"
            + "    var result = document.all.namedItem('" + name + "');\n"
            + "    if (result == null) {\n"
            + "      log(result);\n"
            + "    } else if (result.id || result.name) {\n"
            + "      log(result.id + '<->' + result.name);\n"
            + "    } else {\n"
            + "      log('coll ' + result.length);\n"
            + "      for(i = 0; i < result.length; i++) {\n"
            + "        log(result.item(i).id + '<->' + result.item(i).name);\n"
            + "      }\n"
            + "    }\n"
            + "    window.document.title = res;"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form id='form1'></form>\n"
            + "  <form name='form2'></form>\n"
            + "  <form id='f3' name='form3'></form>\n"
            + "  <form id='f4' name='form4_1'></form>\n"
            + "  <form id='f4' name='form4_2'></form>\n"
            + "  <form id='f5_1' name='form5'></form>\n"
            + "  <form id='f5_2' name='form5'></form>\n"
            + "  <form id='f6' name='form6'></form>\n"
            + "  <form id='form6' name='form6_2'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"a", "b", "a", "b", "0"})
    public void all_tags() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  try {\n"
            + "    var inputs = document.all.tags('input');\n"
            + "    var inputCount = inputs.length;\n"
            + "    for(i = 0; i < inputCount; i++) {\n"
            + "      log(inputs[i].name);\n"
            + "    }\n"
            + "    // Make sure tags() returns an element array that you can call item() on.\n"
            + "    log(document.all.tags('input').item(0).name);\n"
            + "    log(document.all.tags('input').item(1).name);\n"
            + "    // Make sure tags() returns an empty element array if there are no matches.\n"
            + "    log(document.all.tags('xxx').length);\n"
            + "  } catch (e) { log('exception') }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<input type='text' name='a' value='1'>\n"
            + "<input type='text' name='b' value='1'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * {@code document.all} is "hidden".
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "undefined"})
    public void all() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.all ? true : false);\n"
            + "  log(Boolean(document.all));\n"
            + "  log(typeof document.all);\n"
            + "}\n"
            + "</script><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Makes sure that the document.all collection contents are not cached if the
     * collection is accessed before the page has finished loading.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void all_Caching() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body onload='log(document.all.b.value)'>\n"
            + "<input type='text' name='a' value='1'>\n"
            + "<script>log(document.all.a.value)</script>\n"
            + "<input type='text' name='b' value='2'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "null"},
            IE = {"undefined", "null", "undefined"})
    public void all_NotExisting() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.all('notExisting'));\n"
            + "  log(document.all.item('notExisting'));\n"
            + "  log(document.all.namedItem('notExisting'));\n"
            + "}\n"
            + "</script><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"value1", "value1", "value2", "value2"})
    public void getElementsByName() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var elements = document.getElementsByName('name1');\n"
            + "  for (var i = 0; i < elements.length; i++) {\n"
            + "    log(elements[i].value);\n"
            + "    log(elements.item(i).value);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form>\n"
            + "<input type='radio' name='name1' value='value1'>\n"
            + "<input type='radio' name='name1' value='value2'>\n"
            + "<input type='button' name='name2' value='value3'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("IAmTheBody")
    public void body_read() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body id='IAmTheBody' onload='log(document.body.id)'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("FRAMESET")
    public void body_readFrameset() throws Exception {
        final String html = "<html>\n"
            + "<frameset onload='alert(document.body.tagName)'>\n"
            + "<frame src='about:blank' name='foo'>\n"
            + "</frameset></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests for <tt>document.images</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "3", "3", "true", "firstImage"})
    public void images() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.images;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.images.length);\n"
            + "  log(document.images == oCol);\n"
            + "  log(document.images[0].id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<img id='firstImage' />\n"
            + "<img name='end' />\n"
            + "<img id='endId'/>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests for <tt>document.embeds</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "true"})
    public void imagesEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var oCol = document.images;\n"
            + "log(oCol.length);\n"
            + "function test() {\n"
            + "  log(oCol.length);\n"
            + "  log(document.images.length);\n"
            + "  log(document.images == oCol);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test the access to the images value. This should return the 2 images in the document
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "2", "true"})
    public void allImages() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.images.length);\n"
            + "  log(allImages.length);\n"
            + "  log(document.images == allImages);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='firstImage'>\n"
            + "<script>\n"
            + "var allImages = document.images;\n"
            + "log(allImages.length);\n"
            + "</script>\n"
            + "<form>\n"
            + "<img src='2ndImage'>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * Test setting and reading the title for an existing title.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("correct title")
    public void settingTitle() throws Exception {
        final String html
            = "<html><head><title>Bad Title</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  document.title = 'correct title';\n"
            + "  alert(document.title);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Test setting and reading the title for when the is not in the page to begin.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("correct title")
    public void settingMissingTitle() throws Exception {
        final String html = "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  document.title = 'correct title';\n"
            + "  alert(document.title);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test setting and reading the title for when the is not in the page to begin.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("correct title")
    public void settingBlankTitle() throws Exception {
        final String html = "<html><head><title></title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  document.title = 'correct title';\n"
            + "  alert(document.title);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void title() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  alert(document.title);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Test the ReadyState.
     * <a href="http://sourceforge.net/tracker/?func=detail&aid=3030247&group_id=47038&atid=448266">issue 1139</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"loading", "complete"})
    public void readyState() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function testIt() {\n"
            + "  log(document.readyState);\n"
            + "}\n"
            + "log(document.readyState);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onLoad='testIt()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Calling document.body before the page is fully loaded used to cause an exception.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void documentWithNoBody() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.body);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * IE has a bug which returns the element by name if it cannot find it by ID.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "byId"})
    public void getElementById_findByName() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "<input type='text' name='findMe'>\n"
            + "<input type='text' id='findMe2' name='byId'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var o = document.getElementById('findMe');\n"
            + "  log(o ? o.name : 'null');\n"
            + "  log(document.getElementById('findMe2').name);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that <tt>img</tt> and <tt>form</tt> can be retrieved directly by name, but not <tt>a</tt>, <tt>input</tt>
     * or <tt>button</tt>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"myImageId", "2", "FORM", "undefined", "undefined", "undefined", "undefined"})
    public void directAccessByName() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(document.myImage.id);\n"
            + "  log(document.myImage2.length);\n"
            + "  log(document.myForm.tagName);\n"
            + "  log(document.myAnchor);\n"
            + "  log(document.myInput);\n"
            + "  log(document.myInputImage);\n"
            + "  log(document.myButton);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <img src='foo' name='myImage' id='myImageId'>\n"
            + "  <img src='foo' name='myImage2'>\n"
            + "  <img src='foo' name='myImage2'>\n"
            + "  <a name='myAnchor'/>\n"
            + "  <form name='myForm'>\n"
            + "    <input name='myInput' type='text'>\n"
            + "    <input name='myInputImage' type='image' src='foo'>\n"
            + "    <button name='myButton'>foo</button>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

     /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLCollection]", "2"})
    public void scriptsArray() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
            + "<script lang='JavaScript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function doTest() {\n"
            + "    log(document.scripts);\n"
            + "    try {\n"
            + "      log(document.scripts.length);\n" // This line used to blow up
            + "    }\n"
            + "    catch (e) { log('exception occured') }\n"
            + "}\n"
            + "</script></head><body onload='doTest();'>\n"
            + "<script>var scriptTwo = 1;</script>\n"
            + "</body></html> ";

        loadPageVerifyTitle2(html);
    }

    /**
     * Any document.foo should first look at elements named "foo" before using standard functions.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"object", "FORM"},
            IE = {"function", "undefined"})
    public void precedence() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='writeln'>foo</form>\n"
            + "  <script>log(typeof document.writeln);</script>\n"
            + "  <script>log(document.writeln.tagName);</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "false"},
            IE = {"true", "true"})
    public void defaultViewAndParentWindow() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.defaultView == window);\n"
            + "  log(document.parentWindow == window);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html> ";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "123"})
    public void put() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.foo);\n"
                + "  if (!document.foo) document.foo = 123;\n"
                + "  log(document.foo);\n"
                + "</script>\n"
                + "</form>\n" + "</body>\n" + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests <tt>document.cloneNode()</tt>.
     * IE specific.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLDocument]", "[object HTMLBodyElement]",
                "true", "true", "true", "false", "true", "false"})
    public void documentCloneNode() throws Exception {
        final String html = "<html><body id='hello' onload='doTest()'>\n"
                + "  <script id='jscript'>\n"
                + LOG_TITLE_FUNCTION
                + "    function doTest() {\n"
                + "      var clone = document.cloneNode(true);\n"
                + "      log(clone);\n"
                + "      if (clone != null) {\n"
                + "        log(clone.body);\n"
                + "        log(clone.body !== document.body);\n"
                + "        log(clone.getElementById(\"id1\") !== document.getElementById(\"id1\"));\n"
                + "        log(document.ownerDocument == null);\n"
                + "        log(clone.ownerDocument == document);\n"
                + "        log(document.getElementById(\"id1\").ownerDocument === document);\n"
                + "        log(clone.getElementById(\"id1\").ownerDocument === document);\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "  <div id='id1'>hello</div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void createStyleSheet() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var s = document.createStyleSheet('foo.css', 1);\n"
            + "  log(s);\n"
            + "}\n"
            + "catch(ex) {\n"
            + "  log('exception');\n"
            + "}\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("#document-fragment_null_11_null_0_")
    public void createDocumentFragment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    var textarea = document.getElementById('myTextarea');\n"
            + "    textarea.value += fragment.nodeName + '_';\n"
            + "    textarea.value += fragment.nodeValue + '_';\n"
            + "    textarea.value += fragment.nodeType + '_';\n"
            + "    textarea.value += fragment.parentNode + '_';\n"
            + "    textarea.value += fragment.childNodes.length + '_';\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<textarea id='myTextarea' cols='40'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String expected = getExpectedAlerts()[0];
        assertEquals(expected, driver.findElement(By.id("myTextarea")).getAttribute("value"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "object", "[object Event]", "false"})
    public void createEvent_Event() throws Exception {
        createEvent("Event");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "object", "[object Event]", "false"})
    public void createEvent_Events() throws Exception {
        createEvent("Events");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "object", "[object Event]", "false"})
    public void createEvent_HTMLEvents() throws Exception {
        createEvent("HTMLEvents");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void createEvent_Bogus() throws Exception {
        createEvent("Bogus");
    }

    private void createEvent(final String eventType) throws Exception {
        final String html =
              "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var e = document.createEvent('" + eventType + "');\n"
            + "  log(e != null);\n"
            + "  log(typeof e);\n"
            + "  log(e);\n"
            + "  log(e.cancelable);\n"
            + "}\n"
            + "catch (e) { log('exception') }\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "null", "[object HTMLDivElement]"})
    public void createEvent_target() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='d' onclick='log(event.target)'>abc</div>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var event = document.createEvent('MouseEvents');\n"
            + "          log(event.target);\n"
            + "          event.initMouseEvent('click', true, true, window,\n"
            + "               1, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "          log(event.target);\n"
            + "          document.getElementById('d').dispatchEvent(event);\n"
            + "        } catch (e) { log('exception') }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void createEvent_overridden() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='d' onclick='log(onload.toString().indexOf(\"hi\") != -1)'"
            + " onload='log(\"hi\")'>abc</div>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var event = document.createEvent('MouseEvents');\n"
            + "          event.initMouseEvent('click', true, true, window,\n"
            + "               1, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "          document.getElementById('d').dispatchEvent(event);\n"
            + "        } catch (e) { log('exception') }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "test",
            IE = "null")
    @NotYetImplemented
    public void createEvent_caller() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='d' onclick='var c = arguments.callee.caller; log(c ? c.name : c)'>abc</div>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var event = document.createEvent('MouseEvents');\n"
            + "          event.initMouseEvent('click', true, true, window,\n"
            + "               1, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "          document.getElementById('d').dispatchEvent(event);\n"
            + "        } catch (e) { log('exception') }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    @NotYetImplemented
    public void caller() throws Exception {
        final String html =
              "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var c = arguments.callee.caller;\n"
            + "        log(c ? c.name : c);\n"
            + "      }\n"
            + "      test();\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "onload",
            IE = "undefined")
    @NotYetImplemented(IE)
    public void caller_event() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var c = arguments.callee.caller;\n"
            + "        log(c ? c.name : c);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void createEventObject_IE() throws Exception {
        final String html =
              "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var e = document.createEventObject();\n"
            + "  log(e != null);\n"
            + "  log(typeof e);\n"
            + "  log(e);\n"
            + "} catch(ex) {\n"
            + "  log('exception');\n"
            + "}\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void elementFromPoint() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.elementFromPoint(-1,-1);\n"
            + "    log(e != null ? e.nodeName : null);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object StyleSheetList]", "0", "true"})
    public void styleSheets() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.styleSheets);\n"
            + "    log(document.styleSheets.length);\n"
            + "    log(document.styleSheets == document.styleSheets);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Various <tt>document.designMode</tt> tests when the document is in the root HTML page.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"off", "off", "on", "on", "on", "off", "off", "off", "off"},
            IE = {"inherit", "!", "inherit", "on", "on", "!", "on", "off", "off", "inherit", "inherit"})
    public void designMode_root() throws Exception {
        designMode("document");
    }

    /**
     * Various <tt>document.designMode</tt> tests when the document is in an <tt>iframe</tt>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"off", "off", "on", "on", "on", "off", "off", "off", "off"},
            IE = {"inherit", "!", "inherit", "on", "on", "!", "on", "off", "off", "inherit", "inherit"})
    public void designMode_iframe() throws Exception {
        designMode("window.frames['f'].document");
    }

    private void designMode(final String doc) throws Exception {
        final String html = "<html><body><iframe name='f' id='f'></iframe><script>\n"
            + LOG_TITLE_FUNCTION
            + "var d = " + doc + ";\n"
            + "log(d.designMode);\n"
            + "try{d.designMode = 'abc';}catch(e){log('!');}\n"
            + "log(d.designMode);\n"
            + "try{d.designMode = 'on';}catch(e){log('!');}\n"
            + "log(d.designMode);\n"
            + "try{d.designMode = 'On';}catch(e){log('!');}\n"
            + "log(d.designMode);\n"
            + "try{d.designMode = 'abc';}catch(e){log('!');}\n"
            + "log(d.designMode);\n"
            + "try{d.designMode = 'Off';}catch(e){log('!');}\n"
            + "log(d.designMode);\n"
            + "try{d.designMode = 'off';}catch(e){log('!');}\n"
            + "log(d.designMode);\n"
            + "try{d.designMode = 'Inherit';}catch(e){log('!');}\n"
            + "log(d.designMode);\n"
            + "try{d.designMode = 'inherit';}catch(e){log('!');}\n"
            + "log(d.designMode);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that enabling design mode on a document in Firefox implicitly creates a selection range.
     * Required for YUI rich text editor unit tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "0", "0"},
            FF = {"0", "1", "1"},
            FF78 = {"0", "1", "1"})
    public void designMode_createsSelectionRange() throws Exception {
        final String html1 = "<html><body><iframe id='i' src='" + URL_SECOND + "'></iframe></body></html>";
        final String html2 = "<html><body onload='test()'>\n"
            + "<script>\n"
            + "  var selection = document.selection;\n"
            + "  if(!selection) selection = window.getSelection();\n"
            + "  function test() {\n"
            + "    alert(selection.rangeCount);\n"
            + "    document.designMode = 'on';\n"
            + "    alert(selection.rangeCount);\n"
            + "    document.designMode = 'off';\n"
            + "    alert(selection.rangeCount);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageWithAlerts2(html1);
    }

    /**
     * Minimal test for {@code execCommand}.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "false"},
            CHROME = {"false", "false"},
            EDGE = {"false", "false"})
    public void execCommand() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    document.designMode = 'On';\n"
            + "    log(document.execCommand('Bold', false, null));\n"
            + "    try {\n"
            + "      log(document.execCommand('foo', false, null));\n"
            + "    }\n"
            + "    catch (e) {\n"
            + "      log('command foo not supported');\n"
            + "    }\n"
            + "    document.designMode = 'Off';\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLHeadingElement]",
            IE = "not available")
    public void evaluate_caseInsensitiveAttribute() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if(document.evaluate) {\n"
            + "    var expr = './/*[@CLASS]';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    log(result.iterateNext());\n"
            + "  } else { log('not available'); }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <h1 class='title'>Some text</h1>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLHtmlElement]",
            IE = "not available")
    public void evaluate_caseInsensitiveTagName() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if(document.evaluate) {\n"
            + "      var expr = '/hTmL';\n"
            + "      var result = document.evaluate(expr, "
                        + "document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "      log(result.iterateNext());\n"
            + "    } else { log('not available'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <h1 class='title'>Some text</h1>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that HtmlUnit behaves correctly when a document is missing the <tt>body</tt> tag (it
     * needs to be added once the document has finished loading).
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1: null", "2: null", "3: [object HTMLBodyElement]"})
    public void noBodyTag() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "    </script>\n"
            + "    <script>log('1: ' + document.body);</script>\n"
            + "    <script defer=''>log('2: ' + document.body);</script>\n"
            + "    <script>window.onload = function() { log('3: ' + document.body); }</script>\n"
            + "  </head>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that HtmlUnit behaves correctly when an iframe's document is missing the <tt>body</tt> tag (it
     * needs to be added once the document has finished loading).
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1: [object HTMLBodyElement]", "2: [object HTMLBodyElement]"})
    public void noBodyTag_IFrame() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <iframe id='i'></iframe>\n"
            + "    <script>\n"
            + "      log('1: ' + document.getElementById('i').contentWindow.document.body);\n"
            + "      window.onload = function() {\n"
            + "        log('2: ' + document.getElementById('i').contentWindow.document.body);\n"
            + "      };\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that the document object has a <tt>fireEvent</tt> method and that it works correctly (IE only).
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void fireEvent() throws Exception {
        final String html =
              "<html><body>\n"
            + " <span id='s' onclick='\n"
            + "  if(document.fireEvent) {\n"
            + "    document.onkeydown = function() {log(\"x\")};\n"
            + "    document.fireEvent(\"onkeydown\");\n"
            + "  }\n"
            + " '>abc</span>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Test the value of document.ownerDocument.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void ownerDocument() throws Exception {
        final String html = "<html>\n"
                + "<body id='hello' onload='doTest()'>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function doTest() {\n"
                + "      log(document.ownerDocument);\n"
                + "    }\n"
                + "  </script>\n"
                + "</body>\n" + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"null", "text1", "not available"},
            IE = {"null", "text1", "onfocus text2", "text2", "onfocus text2"})
    @NotYetImplemented(IE)
    // the execution order is not yet correct: the onfocus is called during onload not after it
    public void setActive() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.activeElement);\n"
            + "  function test() {\n"
            + "    log(document.activeElement.id);\n"
            + "    var inp = document.getElementById('text2');\n"
            + "    if (inp.setActive) {\n"
            + "      inp.setActive();\n"
            + "      log(document.activeElement.id);\n"
            + "    } else { log('not available'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <input id='text1' onclick='test()'>\n"
            + "  <input id='text2' onfocus='log(\"onfocus text2\")'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts()[0]);
        Thread.sleep(100);

        driver.findElement(By.id("text1")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Test for bug #658 (we were missing the document.captureEvents(...) method).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"123", "captured"})
    public void captureEvents() throws Exception {
        final String content = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function t() { log('captured'); }\n"

            + "  if(document.captureEvents) {\n"
            + "    document.captureEvents(Event.CLICK);\n"
            + "    document.onclick = t;\n"
            + "  } else { log('not available'); }\n"
            + "</script></head><body>\n"
            + "<div id='theDiv' onclick='log(123)'>foo</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(content);
        driver.findElement(By.id("theDiv")).click();

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "true", "true", "false", "false"},
            IE = {"-", "-", "-", "-", "-", "-", "-"})
    public void contains() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var testnode = document.getElementById('myNode');\n"
            + "    log(document.contains ? document.contains(testnode) : '-');\n"

            + "    var newnode = document.createComment('some comment');\n"
            + "    log(document.contains ? document.contains(newnode) : '-');\n"

            + "    log(document.contains ? document.contains(document.documentElement) : '-');\n"
            + "    log(document.contains ? document.contains(document.body) : '-');\n"
            + "    log(document.contains ? document.contains(document.firstElementChild) : '-');\n"

            + "    log(document.contains ? document.contains(null) : '-');\n"
            + "    log(document.contains ? document.contains(undefined) : '-');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myNode'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object Comment]", "false"},
            IE = {"[object Comment]", "-"})
    public void createComment() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var elt = document.createComment('some comment');\n"
            + "  log(elt);\n"
            + "  log(document.contains ? document.contains(elt) : '-');\n"
            + "}\n"
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
    @Alerts({"books", "books", "3", "#text", "0"})
    public void createAttribute() throws Exception {
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var cid = document.createAttribute('id');\n"
            + "    cid.nodeValue = 'a1';\n"
            + "    log(doc.documentElement.nodeName);\n"
            + "    log(doc.childNodes[0].nodeName);\n"
            + "    log(doc.childNodes[0].childNodes.length);\n"
            + "    log(doc.childNodes[0].childNodes[0].nodeName);\n"
            + "    log(doc.getElementsByTagName('books').item(0).attributes.length);\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1"})
    public void getElementsByTagNameNS() throws Exception {
        shutDownRealIE();

        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    if (!document.all) {\n"
            + "      log(document.getElementsByTagNameNS('*', 'books').length);\n"
            + "      log(doc.getElementsByTagNameNS('*', 'books').length);\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"
            + "  <books xmlns='http://www.example.com/ns1'>\n"
            + "    <book>\n"
            + "      <title>Immortality</title>\n"
            + "      <author>John Smith</author>\n"
            + "    </book>\n"
            + "  </books>\n"
            + "</soap:Envelope>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void oninput() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('oninput' in document);\n"
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
    @Alerts({"undefined", "42"})
    public void documentDefineProperty() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.testProp);\n"

            + "      Object.defineProperty(document, 'testProp', {\n"
            + "        value: 42,\n"
            + "        writable: true,\n"
            + "        enumerable: true,\n"
            + "        configurable: true\n"
            + "      });\n"
            + "      log(document.testProp);\n"
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
    @Alerts(DEFAULT = {"§§URL§§", "undefined"},
            IE = {"§§URL§§", "§§URL§§"})
    public void urlUnencoded() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.URL);\n"
            + "      log(document.URLUnencoded);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final URL url = new URL(URL_FIRST, "abc%20def");
        expandExpectedAlertsVariables(url);

        final WebDriver driver = loadPage2(html, url);
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "[object HTMLHtmlElement]"},
            IE = "not found")
    public void children() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (document.children) {\n"
            + "        log(document.children.length);\n"
            + "        log(document.children.item(0));\n"
            + "      }\n"
            + "      else {\n"
            + "        log('not found');\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        final URL url = new URL(URL_FIRST, "abc%20def");
        expandExpectedAlertsVariables(url);

        final WebDriver driver = loadPage2(html, url);
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"application/xml", "text/html"},
            IE = {"undefined", "undefined"})
    public void contentType() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var xmlDocument = document.implementation.createDocument('', '', null);\n"
            + "      log(xmlDocument.contentType);\n"
            + "      log(document.contentType);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"null", "null"},
            FF = {"undefined", "undefined"},
            FF78 = {"undefined", "undefined"},
            IE = {"", ""})
    public void xmlEncoding() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var xmlDocument = document.implementation.createDocument('', '', null);\n"
            + "      log(xmlDocument.xmlEncoding);\n"
            + "      log(document.xmlEncoding);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "false"},
            FF = {"undefined", "undefined"},
            FF78 = {"undefined", "undefined"})
    public void xmlStandalone() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var xmlDocument = document.implementation.createDocument('', '', null);\n"
            + "      log(xmlDocument.xmlStandalone);\n"
            + "      log(document.xmlStandalone);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1.0", "null"},
            FF = {"undefined", "undefined"},
            FF78 = {"undefined", "undefined"},
            IE = {"1.0", ""})
    public void xmlVersion() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var xmlDocument = document.implementation.createDocument('', '', null);\n"
            + "      log(xmlDocument.xmlVersion);\n"
            + "      log(document.xmlVersion);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null"})
    public void rootElement() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var xmlDocument = document.implementation.createDocument('', '', null);\n"
            + "      log(xmlDocument.rootElement);\n"
            + "      log(document.rootElement);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "[object HTMLHtmlElement]", "[object HTMLHtmlElement]"},
            IE = {"undefined", "undefined", "undefined"})
    public void firstElementChild() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.childElementCount);\n"
            + "      log(document.firstElementChild);\n"
            + "      log(document.lastElementChild);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "[object HTMLHtmlElement]", "[object HTMLHtmlElement]"},
            IE = {"undefined", "undefined", "undefined"})
    public void firstElementChildDoctype() throws Exception {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.childElementCount);\n"
            + "      log(document.firstElementChild);\n"
            + "      log(document.lastElementChild);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "test"})
    public void useInMap() throws Exception {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var map = new Map();\n"
            + "      map.set(document, 'test');\n"
            + "      log(map.has(document));\n"
            + "      log(map.get(document));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "test"})
    public void useInWeakMap() throws Exception {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var map = new WeakMap();\n"
            + "      map.set(document, 'test');\n"
            + "      log(map.has(document));\n"
            + "      log(map.get(document));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void useInSet() throws Exception {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var set = new Set();\n"
            + "      set.add(document, 'test');\n"
            + "      log(set.has(document));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "no WeakSet")
    public void useInWeakSet() throws Exception {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (window.WeakSet) {\n"
            + "        var set = new WeakSet();\n"
            + "        set.add(document, 'test');\n"
            + "        log(set.has(document));\n"
            + "      } else {\n"
            + "        log('no WeakSet');\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
