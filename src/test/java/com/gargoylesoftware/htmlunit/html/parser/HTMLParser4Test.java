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
package com.gargoylesoftware.htmlunit.html.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Test class for {@link HTMLParser}.
 *
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLParser4Test extends WebDriverTestCase {

    /**
     * Tests that inserted TBODY and TFOOT don't conflict.
     * @throws Exception failure
     */
    @Test
    @Alerts("TABLE")
    public void table_tfoot() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>"
            + "<table><tr><td>hello</td></tr>\n"
            + "<tfoot id='tf'><tr><td>foot</td></tr></tfoot>"
            + "</table>\n"
            + "<script>\n"
            + "alert(document.getElementById('tf').parentNode.nodeName);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for the condition when there is a <tt>&lt;form&gt;</tt> inside of a <tt>&lt;table&gt;</tt> and before
     * a <tt>&lt;tr&gt;</tt>.
     * @throws Exception failure
     */
    @Test
    @Alerts("myForm")
    public void badlyFormedHTML() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>first</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementById('myInput').form.id);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <table>\n"
            + "    <form name='myForm' action='foo' id='myForm'>\n"
            + "      <tr><td>\n"
            + "      <input type='text' name='myInput' id='myInput'/>\n"
            + "      </td></tr>\n"
            + "    </form>\n"
            + "  </table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"4", "[object HTMLScriptElement]", "[object Text]",
                "[object HTMLTitleElement]", "[object Text]"})
    public void badlyFormedHTML_scriptBeforeHead() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<script>var i = 7;</script>\n"
            + "<html>\n"
            + "  <head>\n"
            + "    <title>first</title>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <script>\n"
            + "      var headChildren = document.getElementsByTagName('head')[0].childNodes;\n"
            + "      alert(headChildren.length);\n"
            + "      alert(headChildren[0]);\n"
            + "      alert(headChildren[1]);\n"
            + "      alert(headChildren[2]);\n"
            + "      alert(headChildren[3]);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"4", "[object HTMLScriptElement]", "[object Text]",
                "[object HTMLTitleElement]", "[object Text]"})
    public void badlyFormedHTML_scriptBeforeDoctype() throws Exception {
        final String html = "<script>var i = 7;</script>\n"
            + HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <title>first</title>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <script>\n"
            + "      var headChildren = document.getElementsByTagName('head')[0].childNodes;\n"
            + "      alert(headChildren.length);\n"
            + "      alert(headChildren[0]);\n"
            + "      alert(headChildren[1]);\n"
            + "      alert(headChildren[2]);\n"
            + "      alert(headChildren[3]);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"4", "[object HTMLParagraphElement]", "[object Text]",
                "[object HTMLScriptElement]", "[object Text]"})
    @NotYetImplemented
    public void badlyFormedHTML_scriptAfterHtml() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <title>first</title>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var headChildren = document.getElementsByTagName('body')[0].childNodes;\n"
            + "        alert(headChildren.length);\n"
            + "        alert(headChildren[0]);\n"
            + "        alert(headChildren[1]);\n"
            + "        alert(headChildren[2]);\n"
            + "        alert(headChildren[3]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><p>HtmlUnit</p></body>\n"
            + "</html>"
            + "<script>var i = 7;</script>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"from script", "from body"})
    public void badlyFormedHTML_duplicateHead() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "  <head>\n"
            + "    <title>foo</title>\n"
            + "    <script src='script.js'></script>\n"
            + "  </head>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <p>HtmlUnit</p>\n"
            + "    <script>alert('from body');</script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("alert('from script')", 200, "OK", null);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts("<html><head><title>foo</title></head>"
            + "<body><script>alert(document.documentElement.outerHTML);</script></body></html>")
    public void badlyFormedHTML_duplicateHeadStructure() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>"
            + "<head>"
            + "<head>"
            + "<title>foo</title>"
            + "</head>"
            + "</head>"
            + "<body>"
            + "<script>alert(document.documentElement.outerHTML);</script>"
            + "</body>"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts("<p title=\"Nimbus\ufffd X\">Nimbus\ufffd X</p>")
    public void badlyFormedHTML_invalidNumericCharacterReference() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <title>foo</title>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <div id='tester'><p title='Nimbus&#84823000 X'>Nimbus&#84823000 X</p></div>\n"
            + "    <script>\n"
            + "      alert(document.getElementById('tester').innerHTML);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test when an illegal tag is found in head as some websites do.
     * @throws Exception failure
     */
    @Test
    @Alerts("first")
    public void unknownTagInHead() throws Exception {
        // Note: the <meta> tag in this test is quite important because
        // I could adapt the TagBalancer to make it work except with this <meta http-equiv...
        // (it worked with <meta name=...)
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><mainA3>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>\n"
            + "  <title>first</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.title);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"false", "true"})
    public void duplicatedAttribute() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "</head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementById('foo') == null);\n"
            + "      alert(document.getElementById('bla') == null);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <span id='foo' id='bla'></span>"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"1", "3", "[object HTMLScriptElement]",
                "[object HTMLUnknownElement]", "[object HTMLUnknownElement]", "[object HTMLFormElement]"})
    public void namespace() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('script1'));\n"
            + "    alert(document.getElementById('script2'));\n"
            + "    alert(document.getElementById('message1'));\n"
            + "    alert(document.getElementById('form1'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script id='script1'>alert(1)</script>\n"
            + "<app:script id='script2'>alert(2)</app:script>\n"
            + "<script>alert(3)</script>\n"
            + "<app:message name='r:tasks.request' id='message1'>hello</app:message>\n"
            + "<form id='form1' xmlns='http://org.pentaho'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"1",
                "[object Element]", "app:script,app:script,http://www.appcelerator.org,app,script",
                "[object HTMLScriptElement]", "SCRIPT,SCRIPT,http://www.w3.org/1999/xhtml,null,script",
                "[object HTMLUnknownElement]", "APP:SCRIPT,APP:SCRIPT,http://www.w3.org/1999/xhtml,null,app:script"})
    public void namespace2() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      div = document.createElementNS('http://www.appcelerator.org', 'app:script');\n"
            + "      debug(div);\n"
            + "    } catch (e) {alert('createElementNS() is not defined')}\n"
            + "    debug(document.getElementById('script1'));\n"
            + "    debug(document.getElementById('script2'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    alert(e);\n"
            + "    alert(e.nodeName + ',' + e.tagName + ',' + e.namespaceURI + ',' + e.prefix + ',' + e.localName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script id='script1'>alert(1)</script>\n"
            + "<app:script id='script2'>alert(2)</app:script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

// this tests fails and breaks the driver for follow up tests - have to investigate what is going wrong here
//    /**
//     * See issue #1830.
//     * @throws Exception failure
//     */
//    @Test
//    @Alerts({"[object HTMLHeadElement]",
//                "HEAD,HEAD,http://www.w3.org/1999/xhtml,null,head",
//                "[object HTMLBodyElement]", "BODY,BODY,http://www.w3.org/1999/xhtml,null,body"})
//    @NotYetImplemented
//    public void namespace_svg() throws Exception {
//        final String html =
//            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
//                            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
//            + "<html xmlns=\"http://www.w3.org/2000/svg\">\n"
//            + "<head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    elem = document.getElementsByTagName('head')[0];\n"
//            + "    debug(elem);\n"
//            + "    elem = document.getElementsByTagName('body')[0];\n"
//            + "    debug(elem);\n"
//            + "  }\n"
//            + "  function debug(e) {\n"
//            + "    alert(e);\n"
//            + "    alert(e.nodeName + ',' + e.tagName + ',' + e.namespaceURI + ',' + e.prefix + ',' + e.localName);\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head>\n"
//            + "<body onload='test()'>\n"
//            + "</body></html>";
//
//        loadPageWithAlerts2(html);
//    }

    /**
     * See issue #1830.
     * @throws Exception failure
     */
    @Test
    @Alerts({"[object HTMLHeadElement]",
                "HEAD,HEAD,http://www.w3.org/1999/xhtml,null,head",
                "[object HTMLBodyElement]", "BODY,BODY,http://www.w3.org/1999/xhtml,null,body",
                "[object SVGGElement]", "g,g,http://www.w3.org/2000/svg,null,g"})
    public void svg_withoutNamespace() throws Exception {
        final String html =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    elem = document.getElementsByTagName('head')[0];\n"
            + "    debug(elem);\n"
            + "    elem = document.getElementsByTagName('body')[0];\n"
            + "    debug(elem);\n"
            + "    elem = document.getElementById('rectangles');\n"
            + "    debug(elem);\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    alert(e);\n"
            + "    alert(e.nodeName + ',' + e.tagName + ',' + e.namespaceURI + ',' + e.prefix + ',' + e.localName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <svg>\n"
            + "    <g id='rectangles'>\n"
            + "      <rect x='1' y='11' width='8' height='8'/>\n"
            + "    </g>\n"
            + "  </svg>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for a case where complete HTML page is present inside DIV tag.
     * Browsers ignore only HTML, HEAD and BODY tags. Contents of HEAD and BODY are added to
     * the current node (DIV tag in test case).
     *
     * @throws Exception failure
     */
    @Test
    @Alerts({"titles", "HEAD", "Outer Html", "DIV", "Inner Html",
                "bodyTitles", "DIV", "Inner Html",
                "innerDiv", "outerDiv"})
    @BuggyWebDriver(IE = {"titles", "HEAD", "Outer Html", "DIV", "",
            "bodyTitles", "DIV", "", "innerDiv", "outerDiv"})
    // This is pretty mysterious because the second title HAS the text 'Inner Html' inside.
    // Currently I do not know why it behaves this way so I take the default behavior.
    public void completeHtmlInsideDiv() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <title>Outer Html</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        alert(titles[i].parentNode.nodeName);\n"
            + "        alert(titles[i].text);\n"
            + "      }\n"
            + "      alert('bodyTitles');\n"
            + "      var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < bodyTitles.length; i++) {\n"
            + "        alert(bodyTitles[i].parentNode.nodeName);\n"
            + "        alert(bodyTitles[i].text);\n"
            + "      }\n"
            + "      alert('innerDiv');\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      alert(innerDiv.parentNode.id);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <DIV id=outerDiv>\n"
            + "    Outer DIV\n"
            + "    <html>\n"
            + "      <head><title>Inner Html</title></head>\n"
            + "      <body>\n"
            + "        <DIV id=innerDiv>Inner DIV</DIV>\n"
            + "      </body>\n"
            + "    </html>\n"
            + "  </DIV>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for a case where complete HTML page is added using document.write() inside DIV tag.
     * Browsers ignore only HTML, HEAD and BODY tags. Contents of HEAD and BODY are added to
     * the current node (DIV tag in test case).
     *
     * @throws Exception failure
     */
    @Test
    @Alerts({"titles", "HEAD", "Outer Html", "DIV", "Inner Html",
                "bodyTitles", "DIV", "Inner Html",
                "innerDiv", "outerDiv"})
    @BuggyWebDriver(IE = {"titles", "HEAD", "Outer Html", "DIV", "",
                 "bodyTitles", "DIV", "", "innerDiv", "outerDiv"})
    // This is pretty mysterious because the second title HAS the text 'Inner Html' inside.
    // Currently I do not know why it behaves this way so I take the default behavior.
    public void writeCompleteHtmlInsideDIV() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <title>Outer Html</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        alert(titles[i].parentNode.nodeName);\n"
            + "        alert(titles[i].text);\n"
            + "      }\n"
            + "      alert('bodyTitles');\n"
            + "      var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < bodyTitles.length; i++) {\n"
            + "        alert(bodyTitles[i].parentNode.nodeName);\n"
            + "        alert(bodyTitles[i].text);\n"
            + "      }\n"
            + "      alert('innerDiv');\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      alert(innerDiv.parentNode.id);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <DIV id=outerDiv>\n"
            + "     Outer DIV\n"
            + "     <script>\n"
            + "       document.write('<html><head><title>Inner Html</title></head>"
            + "         <body><DIV id=innerDiv>Inner DIV</DIV></body></html>');\n"
            + "     </script>\n"
            + "  </DIV>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for a case where complete HTML page is set in innerHTML of DIV tag.
     * Behavior is same for any TAG inside body including BODY tag.
     * Browsers ignore only HTML, HEAD and BODY tags. Contents of HEAD and BODY are added to
     * the current node (DIV tag in test case).
     *
     * @throws Exception failure
     */
    @Test
    @Alerts({"titles", "HEAD", "Outer Html", "DIV", "Inner Html",
                "bodyTitles", "DIV", "Inner Html",
                "innerDiv", "outerDiv"})
    public void setCompleteHtmlToDIV_innerHTML() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <title>Outer Html</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        alert(titles[i].parentNode.nodeName);\n"
            + "        alert(titles[i].text);\n"
            + "      }\n"
            + "      alert('bodyTitles');\n"
            + "      var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < bodyTitles.length; i++) {\n"
            + "        alert(bodyTitles[i].parentNode.nodeName);\n"
            + "        alert(bodyTitles[i].text);\n"
            + "      }\n"
            + "      alert('innerDiv');\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      alert(innerDiv.parentNode.id);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <DIV id=outerDiv>\n"
            + "     Outer DIV\n"
            + "  </DIV>\n"
            + "    <script>\n"
            + "    document.getElementById('outerDiv').innerHTML ="
            + "        '<html><head><title>Inner Html</title></head>"
            + "        <body><DIV id=innerDiv>Inner DIV</DIV></body></html>';\n"
            + "    </script>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for a case where complete HTML page is set in innerHTML of HTML tag.
     * Others replace the current content of the HTML node by the new one.
     *
     * @throws Exception failure
     */
    @Test
    @Alerts({"titles", "HEAD", "Inner Html", "misc", "true", "BODY"})
    @NotYetImplemented
    // currently the content of HEAD and BODY are added directly to HTML
    public void setCompleteHtmlToHTML_innerHTML() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <title>Outer Html</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        alert(titles[i].parentNode.nodeName);\n"
            + "        alert(titles[i].text);\n"
            + "      }\n"
            + "      alert('misc');\n"
            + "      alert(document.body != null);\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      if (innerDiv != null) {\n"
            + "        alert(innerDiv.parentNode.nodeName);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <DIV id=outerDiv>\n"
            + "    Outer DIV\n"
            + "  </DIV>\n"
            + "  <script>\n"
            + "    try {\n"
            + "      document.getElementsByTagName('html')[0].innerHTML ="
            + "        '<html><head><title>Inner Html</title></head>"
            + "        <body><DIV id=innerDiv>Inner DIV</DIV></body></html>';\n"
            + "    } catch(e) { alert('exception') }\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    public void specialComments() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head>\n"
                + "  <title>Outer Html</title>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var body = document.getElementById('tester');\n"
                + "      var text = body.innerText;"
                + "      alert(text);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body id='tester' onload='test()'>\n"
                + "  <div>before1<!---->after1</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before2<!--->after2</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before3<!-->after3</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before4<!->after4</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before5<!>after5</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before6<>after6</div>\n"
                + "  <!--good comment-->\n"
                + "</body>\n"
                + "</html>\n";

        final WebDriver driver = loadPage2(html);
        final String alerts = getCollectedAlerts(driver, 1).get(0);

        assertTrue(alerts, alerts.contains("before1after1"));
        assertTrue(alerts, alerts.contains("before2after2"));
        assertTrue(alerts, alerts.contains("before3after3"));
        assertTrue(alerts, alerts.contains("before4after4"));
        assertTrue(alerts, alerts.contains("before5after5"));
        assertTrue(alerts, alerts.contains("before6<>after6"));
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts(DEFAULT = "before1after1\nbefore2\nbefore3\nbefore4after4\nbefore5after5\nbefore6< >after6",
            IE = "before1after1\n \nbefore2 \nbefore3 \nbefore4after4\n \nbefore5after5\n \nbefore6< >after6\n ")
    @HtmlUnitNYI(CHROME = "\n  before1after1\n  \n  before2\n  before3\n"
                + "  before4after4\n  \n  before5after5\n  \n  before6< >after6\n  \n\n",
            EDGE = "\n  before1after1\n  \n  before2\n  before3\n"
                + "  before4after4\n  \n  before5after5\n  \n  before6< >after6\n  \n\n",
            FF = "\n  before1after1\n  \n  before2\n  before3\n"
                + "  before4after4\n  \n  before5after5\n  \n  before6< >after6\n  \n\n",
            FF78 = "\n  before1after1\n  \n  before2\n  before3\n"
                + "  before4after4\n  \n  before5after5\n  \n  before6< >after6\n  \n\n",
            IE = "\n  before1after1\n  \n  before2\n  before3\n"
                + "  before4after4\n  \n  before5after5\n  \n  before6< >after6\n  \n\n")
    public void specialComments2() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head>\n"
                + "  <title>Outer Html</title>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var body = document.getElementById('tester');\n"
                + "      var text = body.innerText;"
                + "      alert(text);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body id='tester' onload='test()'>\n"
                + "  <div>before1<!-- -->after1</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before2<!-- ->after2</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before3<!-- >after3</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before4<!- >after4</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before5<! >after5</div>\n"
                + "  <!--good comment-->\n"
                + "  <div>before6< >after6</div>\n"
                + "  <!--good comment-->\n"
                + "</body>\n"
                + "</html>\n";

        loadPageWithAlerts2(html);
    }
}
